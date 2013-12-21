package javablock.flowchart.blocks;
import javablock.flowchart.blockEditors.*;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import javablock.*;
import javablock.flowchart.BlockEditor;
import javablock.flowchart.Flowchart;
import javablock.flowchart.JBlock;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ioBlock extends JBlock {
    public static ioEditor editor=new ioEditor();
    public boolean Editor=true;
    //public
    public String message="", messageSuffix="";
    public String variable="";
    //input
    public int array=0;
    public int inputType=0;
    /*
     * 0 - Number
     * 1 - String
     * 2 - Integer
     * 3 - char Array
     * 4 - Logic
     */
    public int ioType=0;
    /*
     * -1 - standard, input
     * -1 - standard, output
     *  0 - custom
     *  1 - output
     *  2 - input
     */
    //output
    public boolean newLine=true;

    public ioBlock(Flowchart parent){
        super(Type.IO, parent);
    }


    @Override
    public boolean popUpEditor(){return (ioType==0?true:false);}
    @Override
    public BlockEditor getEditor(){
        if(ioType==0)
            return super.getEditor();
        return (BlockEditor) editor;
    }

    @Override
    public void shape(){
        prepareText();
        if(ioType<=0){
            GeneralPath io=new GeneralPath();
                io.moveTo(bound.getX()-7,                  bound.getY()-10);
                io.lineTo(bound.getX()+bound.getWidth()+13, bound.getY()-10);
                io.lineTo(bound.getX()+bound.getWidth()+7, bound.getY()+bound.getHeight()+10);
                io.lineTo(bound.getX()-13,                  bound.getY()+bound.getHeight()+10);
                io.lineTo(bound.getX()-7,                  bound.getY()-10);
            shape=io;
        }
        if(ioType==1){
            GeneralPath io=new GeneralPath();
                io.moveTo(bound.getX()-10,                  bound.getY()-5);
                io.lineTo(bound.getX()+bound.getWidth()+2, bound.getY()-5);
                io.lineTo(bound.getX()+bound.getWidth()+15, bound.getY()+bound.getHeight()/2);
                io.lineTo(bound.getX()+bound.getWidth()+2, bound.getY()+bound.getHeight()+5);
                io.lineTo(bound.getX()-10,                  bound.getY()+bound.getHeight()+5);
                io.lineTo(bound.getX()-10,                  bound.getY()-5);
            Area s=new Area(io);
            io=new GeneralPath();
                io.moveTo(bound.getX()+bound.getWidth()+2  +0.5,   bound.getY()-5);
                io.lineTo(bound.getX()+bound.getWidth()+15 +0.5,  bound.getY()+bound.getHeight()/2);
                io.lineTo(bound.getX()+bound.getWidth()+2  +0.5,   bound.getY()+bound.getHeight()+5);
                io.lineTo(bound.getX()+bound.getWidth()+15 +1.5,  bound.getY()+bound.getHeight()+5);
                io.lineTo(bound.getX()+bound.getWidth()+15 +1.5,  bound.getY()-5);
                io.lineTo(bound.getX()+bound.getWidth()+2  +0.5,   bound.getY()-5);
            s.add(new Area(io));
            shape=s;
        }
        if(ioType==2){
            GeneralPath io=new GeneralPath();
                io.moveTo(bound.getX()-15,                  bound.getY()-5);
                io.lineTo(bound.getX()+bound.getWidth()+10, bound.getY()-5);
                io.lineTo(bound.getX()+bound.getWidth()+10, bound.getY()+bound.getHeight()+5);
                io.lineTo(bound.getX()-15,                  bound.getY()+bound.getHeight()+5);
                io.lineTo(bound.getX()-2 ,                  bound.getY()+bound.getHeight()/2);
                io.lineTo(bound.getX()-15,                  bound.getY()-5);
            Area s=new Area(io);
            io=new GeneralPath();
                io.moveTo(bound.getX()-15-1.5,                  bound.getY()-5);
                io.lineTo(bound.getX()-15-0.5,                  bound.getY()-5);
                io.lineTo(bound.getX()-2- 0.5,                  bound.getY()+bound.getHeight()/2);
                io.lineTo(bound.getX()-15-0.5,                  bound.getY()+bound.getHeight()+5);
                io.lineTo(bound.getX()-15-1.5,                  bound.getY()+bound.getHeight()+5);
                io.lineTo(bound.getX()-15-1.5,                  bound.getY()-5);
                s.add(new Area(io));
            shape=s;
        }
        afterShape();
    }


    @Override
    public void nonCodeBased(boolean ncb){
        if(ncb)
            ioType=1;
        else
            ioType=0;
    }

    @Override
    protected void specialXmlSave(Element xml){
        if(ioType==0) return;
        Element io=xml.getOwnerDocument().createElement("io");
        io.setAttribute("ioType", ""+ioType);
        io.setAttribute("variable", variable);
        io.setAttribute("message", message);
        if(ioType==1){
            io.setAttribute("newLine", ""+newLine);
            io.setAttribute("messageSuffix", this.messageSuffix);
        }
        if(ioType==2){
            io.setAttribute("array", ""+array);
            io.setAttribute("inputType", ""+inputType);
        }
        xml.appendChild(io);
    }
    @Override
    protected void specialXmlLoad(Element xml, boolean connect){
        NodeList n=xml.getElementsByTagName("io");
        if(n.getLength()==0) return ;
        Element io=(Element)n.item(0);
        ioType=Integer.parseInt(io.getAttribute("ioType"));
        variable=io.getAttribute("variable");
        message=io.getAttribute("message");
        if(ioType==1){
            newLine=Boolean.parseBoolean(io.getAttribute("newLine"));
            messageSuffix=io.getAttribute("messageSuffix");
        }
        if(ioType==2){
            array=Integer.parseInt(io.getAttribute("array"));
            inputType=Integer.parseInt(io.getAttribute("inputType"));
        }
        displayComment=true;
    }

    @Override
    public Point2D connectPoint(float angle){
        Point2D c=new Point2D.Float();
        if(ioType<=0){
             if(angle >=-Math.PI*0.25 && angle<=Math.PI*0.25)
            c.setLocation(posX, posY-shape.getBounds2D().getHeight()/2.0);
        else if(angle >= Math.PI * 0.75 || angle <= -Math.PI * 0.75)
            c.setLocation(posX, posY+shape.getBounds2D().getHeight()/2.0);
        else if(angle >= Math.PI * 0.25 && angle <= Math.PI * 0.75)
            c.setLocation(posX+3-shape.getBounds2D().getWidth()/2.0, posY);
        else if(angle <= -Math.PI * 0.25 && angle >= -Math.PI * 0.75)
            c.setLocation(posX-3+shape.getBounds2D().getWidth()/2.0, posY);
        }
        else if(ioType==1){
             if(angle >=-Math.PI*0.25 && angle<=Math.PI*0.25)
            c.setLocation(posX, posY-shape.getBounds2D().getHeight()/2.0);
        else if(angle >= Math.PI * 0.75 || angle <= -Math.PI * 0.75)
            c.setLocation(posX, posY+shape.getBounds2D().getHeight()/2.0);
        else if(angle >= Math.PI * 0.25 && angle <= Math.PI * 0.75)
            c.setLocation(posX+2-shape.getBounds2D().getWidth()/2.0, posY);
        else if(angle <= -Math.PI * 0.25 && angle >= -Math.PI * 0.75)
            c.setLocation(posX+3+shape.getBounds2D().getWidth()/2.0, posY);
        }
        else if(ioType==2){
             if(angle >=-Math.PI*0.25 && angle<=Math.PI*0.25)
            c.setLocation(posX, posY-shape.getBounds2D().getHeight()/2.0);
        else if(angle >= Math.PI * 0.75 || angle <= -Math.PI * 0.75)
            c.setLocation(posX, posY+shape.getBounds2D().getHeight()/2.0);
        else if(angle >= Math.PI * 0.25 && angle <= Math.PI * 0.75)
            c.setLocation(posX-3-shape.getBounds2D().getWidth()/2.0, posY);
        else if(angle <= -Math.PI * 0.25 && angle >= -Math.PI * 0.75)
            c.setLocation(posX-2+shape.getBounds2D().getWidth()/2.0, posY);
        }
        //c.setLocation(c.getX()-0.5, c.getY()-0.5);
        return c;
    }
}
