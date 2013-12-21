package javablock.flowchart.blocks.addons;

import javablock.flowchart.blockEditors.canvas2dEditor;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import javablock.Sheet;
import javablock.flowchart.BlockEditor;
import javablock.flowchart.Flowchart;
import javablock.flowchart.JBlock;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class canvas2dBlock extends JBlock {
    public static canvas2dEditor editor=null;
    public String command="drawPixel";
    public String obj="canvas2d";
    public Object data[]={0,0};

    public canvas2dBlock(Flowchart parent){
        super(Type.CANVAS2D, parent);
        if(editor==null)
            editor=new canvas2dEditor();
        displayComment=true;
    }

    @Override
    public boolean popUpEditor(){return false;}
    @Override
    public BlockEditor getEditor(){
        return (BlockEditor) editor;
    }

    @Override
    public void shape(){
        prepareText();
        GeneralPath l=new GeneralPath();
        l.moveTo(bound.getX()-10,                  bound.getY()-10);
        l.lineTo(bound.getX()+bound.getWidth()/2,  bound.getY()-5);
        l.lineTo(bound.getX()+bound.getWidth()+10, bound.getY()-10);
        l.lineTo(bound.getX()+bound.getWidth()+10, bound.getY()+bound.getHeight()+10);
        l.lineTo(bound.getX()+bound.getWidth()/2,  bound.getY()+bound.getHeight()+5);
        l.lineTo(bound.getX()-10,                  bound.getY()+bound.getHeight()+10);
        l.lineTo(bound.getX()-10,                  bound.getY()-10);
        shape=l;
        afterShape();
    }

    @Override
    public Point2D connectPoint(float angle){
        Point2D c=new Point2D.Float();
             if(angle >=-Math.PI*0.25 && angle<=Math.PI*0.25)
            c.setLocation(posX, posY-shape.getBounds2D().getHeight()/2.0+10);
        else if(angle >= Math.PI * 0.75 || angle <= -Math.PI * 0.75)
            c.setLocation(posX, posY+shape.getBounds2D().getHeight()/2.0-10);
        else if(angle >= Math.PI * 0.25 && angle <= Math.PI * 0.75)
            c.setLocation(posX-shape.getBounds2D().getWidth()/2.0, posY);
        else if(angle <= -Math.PI * 0.25 && angle >= -Math.PI * 0.75)
            c.setLocation(posX+shape.getBounds2D().getWidth()/2.0, posY);
        //c.setLocation(c.getX()-0.5, c.getY()-0.5);
        return c;
    }


    @Override
    protected void specialXmlSave(Element xml){
        Element c2d=xml.getOwnerDocument().createElement("canvas2d");
        c2d.setAttribute("command", command);
        c2d.setAttribute("obj", obj);
        if(command.equals("MAKE")){
            c2d.setAttribute("width", data[0].toString());
            c2d.setAttribute("height", data[1].toString());
            c2d.setAttribute("autoUpdate", data[2].toString());
            c2d.setAttribute("antiAliasing", data[3].toString());
        }
        if(command.equals("setColor")){
            c2d.setAttribute("color", data[0].toString());
        }
        if(command.equals("drawMap")){
            c2d.setAttribute("drawMap", data[0].toString());
        }
        if(command.equals("drawPixel") || command.equals("lineFrom") || command.equals("lineTo")){
            c2d.setAttribute("X", data[0].toString());
            c2d.setAttribute("Y", data[1].toString());
        }
        if(command.equals("drawLine")){
            c2d.setAttribute("lineFromX", data[0].toString());
            c2d.setAttribute("lineFromY", data[1].toString());
            c2d.setAttribute("lineToX", data[2].toString());
            c2d.setAttribute("lineToY", data[3].toString());
        }
        xml.appendChild(c2d);
    }
    @Override
    protected void specialXmlLoad(Element xml, boolean connect){
        NodeList n=xml.getElementsByTagName("canvas2d");
        if(n.getLength()==0) return ;
        Element c2d=(Element)n.item(0);
        command=c2d.getAttribute("command");
        obj=c2d.getAttribute("obj");
        if(command.equals("MAKE")){
            data=new Object[4];
            data[0]=Integer.parseInt(c2d.getAttribute("width"));
            data[1]=Integer.parseInt(c2d.getAttribute("height"));
            data[2]=Boolean.parseBoolean(c2d.getAttribute("autoUpdate"));
            data[3]=Boolean.parseBoolean(c2d.getAttribute("antiAliasing"));
        }
        if(command.equals("drawMap")){
            data=new Object[1];
            data[0]=c2d.getAttribute("drawMap");
        }
        if(command.equals("setColor")){
            data=new Object[1];
            data[0]=c2d.getAttribute("color");
        }
        if(command.equals("drawPixel") || command.equals("lineTo") || command.equals("lineFrom")){
            data=new Object[2];
            data[0]=c2d.getAttribute("X");
            data[1]=c2d.getAttribute("Y");
        }
        if(command.equals("drawLine")){
            data=new Object[4];
            data[0]=c2d.getAttribute("lineFromX");
            data[1]=c2d.getAttribute("lineFromY");
            data[2]=c2d.getAttribute("lineToX");
            data[3]=c2d.getAttribute("lineToY");
        }
        displayComment=true;
    }

}
