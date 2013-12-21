/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javablock.flowchart.blocks;
import config.global;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javablock.flowchart.*;
import javax.script.ScriptEngine;
import org.w3c.dom.*;

/**
 *
 * @author razi
 */
public class linkBlock extends JBlock {
    JBlock next=null;
    public linkBlock(Flowchart parent){
        super(Type.LINK, parent);
        code="<>";
    }
    @Override
    public boolean isSwitchable(){return false;}
    @Override
    public void addedToScene(){
        if(linkTo!=null){
            flow.selected.add(linkTo);
            return ;
        }
        linkTo=new linkBlock(flow);
        linkTo.linkTo=this;
        linkTo.translate(0, 20);
        flow.addBlock(linkTo);
        flow.selected.add(linkTo);
        linkTo.translate(0, 100);
    }
    @Override
    public boolean oneOut(){return false;}
    @Override
    public boolean moveWhenAdded(){
        return false;
    }

    @Override
    public void delete(){
        super.delete();
        if(linkTo!=null){
            linkTo.linkTo=null;
            linkTo.delete();
        }
        if(flow!=null)
            flow.blocks.remove(this);
    }

    @Override
    public void shape(){
        prepareText();
        shape=new Ellipse2D.Double(-5,-5,10,10);
        shape=new Ellipse2D.Double(
                bound.getX()-5,
                bound.getY()-5,
                bound.getWidth()+10,
                bound.getHeight()+10
                );
        afterShape();
    }
    @Override
    public void draw(Graphics2D g2d){
        if(this.nowExecute){
            g2d.setStroke(global.strokeSelection);
            Line2D l=new Line2D.Double(posX, posY, linkTo.posX, linkTo.posY);
            g2d.draw(l);
            g2d.setStroke(global.strokeNormal);
        }
        super.draw(g2d);
    }
    @Override
    public void drawSelection(Graphics2D g2d){
        AffineTransform af = g2d.getTransform();
        g2d.translate(posX, posY);

        g2d.setStroke(global.strokeSelection);
        g2d.setColor(Color.BLACK);
        g2d.translate(0.5, -1);
        g2d.draw(new Rectangle2D.Float(shape.getBounds().x - 2.5f,
                shape.getBounds().y - 2.5f,
                shape.getBounds().width + 5.5f,
                shape.getBounds().height + 5.5f));
        g2d.setTransform(af);
        g2d.setStroke(global.strokeSelection);
            Line2D l=new Line2D.Double(posX, posY, linkTo.posX, linkTo.posY);
            g2d.draw(l);
        g2d.setStroke(global.strokeNormal);
    }

    @Override
    public void prepareToExe(){
        nowExecute=true;
        makeGradient();
        linkTo.nowExecute=true;
        linkTo.makeGradient();
    }

    @Override
    public JBlock nextExe(){
        if(global.highlightLinks || linkTo.connects.size()==0)
            return this;
        else if(linkTo.connects.size() == 1)
            return linkTo.connects.get(0).n.nextExe();
        return this;
    }

    @Override
    public JBlock executeCode(ScriptEngine script){
        next=null;
        if(linkTo.connects.size()==1){
            next=linkTo.connects.get(0).n;
        }
        return next;
    }

    @Override
    public JBlock execute(ScriptEngine script, boolean highlight){
        JBlock r=executeCode(script);
        if(highlight && r!=null){
            r.prepareToExe();
            flow.update();
        }
        releaseFromExe();
        linkTo.releaseFromExe();
        return r;
    }

    @Override
    public void setColor(Color c){
        color=c;
        linkTo.color=c;
        linkTo.makeGradient();
        makeGradient();
    }

    @Override
    public void setCode(String c){
        code=c;
        linkTo.code=c;
        shape();
        linkTo.shape();
    }

    @Override
    protected void specialXmlSave(Element xml){
        Element link=xml.getOwnerDocument().createElement("link");
        if(linkTo!=null)
        link.setAttribute("ID", linkTo.ID+"");
        xml.appendChild(link);
    }
    @Override
    protected void specialXmlLoad(Element xml, boolean connect){
        if(connect){
            NodeList lines=xml.getElementsByTagName("link");
            Element l=(Element)lines.item(0);
            linkTo=(linkBlock) flow.getBlockById(
                    Integer.parseInt(l.getAttribute("ID")));
            linkTo.linkTo=this;
        }
    }
}
