package javablock.flowchart;

import config.global;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import javablock.Sheet;
import javax.script.ScriptEngine;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class blockGroup extends JBlock {
    public List<JBlock> blocks=new ArrayList();
    public blockGroup(Flowchart parent){
        super(Type.GROUP, parent);
        displayComment=true;        
        setColor(color);
        setBorderColor(borderColor);
        setTextColor(textColor);
    }

    @Override
    public void delete(){
        deleted=true;
        for(int i=0; i<connects.size(); i++){
            connects.get(i).n.removeConnectWith(this);
        }
        for(int i=0; i<connectsIn.size(); i++){
            connectsIn.get(i).f.removeConnectWith(this);
        }
        connects.clear();
        connectsIn.clear();
        prerender=null;
        gradient=null;
        txtList.clear();
        if(flow!=null)
            while(flow.groups.remove(this)){};
        flow=null;
    }

    @Override
    public void addedToScene(){
       flow.movingSelected=false;
   }


    @Override
    public connector connectTo(JBlock n){
       if(n==this) return null;
       if(n.type==Type.GROUP){
           //if(true) return null;
           blockGroup g=(blockGroup)n;
           if(g.blocks.contains(this))
               return null;
       }
       modifyGroup(n);
       return null;
   }
    @Override
    public void setColor(Color c){
        color=new Color(c.getRed(), c.getGreen(), c.getBlue(), 128);
        needUpdate=true;
    }
    @Override
    public void setBorderColor(Color c){
        borderColor=new Color(c.getRed(), c.getGreen(), c.getBlue(), 128);
        needUpdate=true;
    }
    @Override
    public void setTextColor(Color c){
        textColor=new Color(c.getRed(), c.getGreen(), c.getBlue(), 128);
        needUpdate=true;
    }

    public void group(){
       l=0;
        for(JBlock b:flow.selected){
            if(b.type!=Type.GROUP){
                blocks.add(b);
                b.groups.add(this);
                l++;
            }
        }
        flow.blocks.remove(this);
        if(l>0)
            flow.groups.add(this);
        needUpdate=true;
    }

    @Override
    public boolean isEditable(){
        return false;
    }
    @Override
    public boolean canBeConnected(JBlock b){
        return false;
    }

    public void modifyGroup(JBlock n){
        if(blocks.contains(n)){
            blocks.remove(n);
            n.groups.remove(this);
            l--;
        }
        else{
            blocks.add(n);
            n.groups.add(this);
            l++;
        }
        flow.selected.clear();
        flow.selected.add(this);
        if(l==0) delete();
        shape();
    }


    @Override
    public JBlock executeCode(ScriptEngine script){
        return null;
    }

    public int l=0;

    @Override
    public void makeGradient(){
        prerendered=false;
        prerender=null;
        if(gradient!=null)
            gradient=null;
        return ;
/*
        if(global.gradients){
            gradient=new GradientPaint(0,shape.getBounds().y, color,
                    0,
                    (float)shape.getBounds().y+shape.getBounds().height, color.darker());
        }
        else{
            gradient=new GradientPaint(0,shape.getBounds().y, color, 0,
                (float) shape.getBounds().y+shape.getBounds().height, color);
        }*/
    }

    @Override
    public void prepareText(){
        Area allBlocks=new Area();
        l=0;
        for(JBlock b: blocks){
            if(b.deleted){
                continue;
            }
            l++;
            Area n=new Area(b.bound2D());
            allBlocks.add(n);
        }
        bound=allBlocks.getBounds2D();
    }

    @Override
    public void shape(){
        needUpdate=false;
        prepareText();
        double field=bound.getWidth()*bound.getHeight();
        RoundRectangle2D box=new RoundRectangle2D.Double(
                        bound.getX()-2-l - field/bound.getHeight()/40,
                        bound.getY()-2-l - field/bound.getWidth()/40,
                        bound.getWidth()+4+2*l + field/bound.getHeight()/20,
                        bound.getHeight()+4+2*l + field/bound.getWidth()/20,
                        30,30
                        );
                shape=box;
        afterShape();
    }

    @Override
    public void draw(Graphics2D g2d){
        if(needUpdate)
            shape();
        g2d.setStroke(global.strokeNormal);
        g2d.setColor(this.color);
        //g2d.setPaint(gradient);
        g2d.fill(shape);
        g2d.setColor(this.borderColor);
        g2d.draw(shape);
    }
    @Override
    public void drawSelection(Graphics2D g2d){
        Rectangle2D b = shape.getBounds2D();
        Rectangle2D sel = new Rectangle2D.Double(
                b.getX() - 2,
                b.getY() - 2,
                b.getWidth() + 4,
                b.getHeight() + 4);
        g2d.setStroke(global.strokeSelection);
        g2d.draw(sel);
        g2d.setStroke(global.strokeNormal);
    }

    public boolean contains(double x, double y){
        return shape.contains(x, y);
    }
    @Override
    protected void parseXml(Element b, boolean connect){
         if(connect==false){
            type=getTypeFromString(b.getAttribute("type"));
            ID=Integer.parseInt(b.getAttribute("id"));
            NodeList options=b.getElementsByTagName("options");
            for(int i=0; i<options.getLength(); i++){
                Element option=(Element)options.item(i);
                if(option.hasAttribute("displayComment"))
                    displayComment=Boolean.parseBoolean(option.getAttribute("displayComment"));
            }
            Element visual=(Element)b.getElementsByTagName("visual").item(0);
                //posX=Double.parseDouble(visual.getAttribute("posX"));
                //posY=Double.parseDouble(visual.getAttribute("posY"));
                String c[]=visual.getAttribute("color").split(" ");
                setColor(new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2])));
                if(visual.hasAttribute("borderColor")){
                    c=visual.getAttribute("borderColor").split(" ");
                    setBorderColor(new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2])));
                }
                if(visual.hasAttribute("textColor")){
                    c=visual.getAttribute("textColor").split(" ");
                    setTextColor(new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2])));
                }
            NodeList lines=b.getElementsByTagName("content");
            code="";
            for(int i=0; i<lines.getLength(); i++){
                Element content=(Element)lines.item(i);
                code+=content.getTextContent();
                if(i!=lines.getLength()-1)
                    code+="\n";
            }
            lines=b.getElementsByTagName("comment");
            comment="";
            for(int i=0; i<lines.getLength(); i++){
                Element content=(Element)lines.item(i);
                comment+=content.getTextContent();
                if(i!=lines.getLength()-1)
                    comment+="\n";
            }

        }
    }

    @Override
    public Element makeXml(Element root, boolean connect, int IDs){
        xmlTagName="group";
        Element block=super.makeXml(root, connect, IDs);
        //Element block=root.getOwnerDocument().createElement("group");
        for(JBlock b:blocks){
            Element blockID=root.getOwnerDocument().createElement("block");
            blockID.setAttribute("ID", ""+b.ID);
            block.appendChild(blockID);
        }
        
        return block;
    }
    @Override
    public void loadXml(Element b, boolean connect){
        parseXml(b, connect);
        NodeList blockList=b.getElementsByTagName("block");
        blocks.clear();
        if(connect){
            for(int i=0; i<blockList.getLength(); i++){
                JBlock a=flow.getBlockById(
                        Integer.parseInt(
                        ((Element)blockList.item(i)).getAttribute("ID")
                        ));
                if(a==null)
                    a=flow.getGroupById(
                        Integer.parseInt(
                        ((Element)blockList.item(i)).getAttribute("ID")
                        ));
                if(a!=null){
                    blocks.add(a);
                    if(!a.groups.contains(this))
                        a.groups.add(this);
                }
            }
        }//if
    }

    @Override
    public Rectangle2D bound2D(){
        Rectangle2D rect=shape.getBounds2D();
        return rect;
    }

    @Override
    public void translate(float x, float y){
        for(JBlock b: blocks){
            if(!flow.selected.contains(b))
                b.translate(x,y);
        }
    }

}
