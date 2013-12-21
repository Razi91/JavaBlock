package javablock.flowchart.blocks.addons;

import javablock.flowchart.blockEditors.logoEditor;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import javablock.Sheet;
import javablock.flowchart.BlockEditor;
import javablock.flowchart.Flowchart;
import javablock.flowchart.JBlock;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class logoBlock extends JBlock {
    public static logoEditor editor=null;
    public String command="forward";
    public String obj="logo";
    public Object data[]={0};

    public logoBlock(Flowchart parent){
        super(Type.LOGO, parent);
        if(editor==null)
            editor=new logoEditor();
        displayComment=true;
    }

    @Override
    public boolean popUpEditor(){return false;}
    @Override
    public BlockEditor getEditor(){
        return editor;
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
        Element logo=xml.getOwnerDocument().createElement("logo");
        logo.setAttribute("command", command);
        logo.setAttribute("object", obj);
        if(command.equals("MAKE")){
            logo.setAttribute("width", data[0].toString());
            logo.setAttribute("height", data[1].toString());
            logo.setAttribute("closedCanvas", data[2].toString());
        }
        if(command.equals("forward") || command.equals("backward")
                || command.equals("turnRight") || command.equals("turnLeft")){
            logo.setAttribute("value", data[0].toString());
        }
        if(command.equals("setColor")){
            logo.setAttribute("color", data[0].toString());
        }
        xml.appendChild(logo);
    }
    @Override
    protected void specialXmlLoad(Element xml, boolean connect){
        NodeList n=xml.getElementsByTagName("logo");
        if(n.getLength()==0) return ;
        Element logo=(Element)n.item(0);
        command=logo.getAttribute("command");
        obj=logo.getAttribute("object");
        if(command.equals("MAKE")){
            data=new Object[3];
            data[0]=Integer.parseInt(logo.getAttribute("width"));
            data[1]=Integer.parseInt(logo.getAttribute("height"));
            data[2]=logo.getAttribute("closedCanvas").equals("true");
        }
        if(command.equals("forward") || command.equals("backward")
                || command.equals("turnRight") || command.equals("turnLeft")){
            data=new Object[1];
            data[0]=logo.getAttribute("value");
        }
        if(command.equals("setColor")){
            data=new Object[1];
            data[0]=logo.getAttribute("color");
        }
        displayComment=true;
    }



}
