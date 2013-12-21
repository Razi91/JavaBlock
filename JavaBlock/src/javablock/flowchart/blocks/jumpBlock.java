package javablock.flowchart.blocks;
import config.global;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import javablock.*;
import javablock.flowchart.Flowchart;
import javablock.flowchart.JBlock;
import javablock.flowchart.connector;
import javax.script.ScriptEngine;


public class jumpBlock extends JBlock {
    JBlock next=null;
    boolean open;
    public jumpBlock(Flowchart parent){
        super(Type.JUMP, parent);
        code=" ";
        open=true;
    }

    @Override
    public boolean isEditable(){
        return false;
    }
    @Override
    public JBlock executeCode(ScriptEngine script){
        if(connects.size()==1)
            return connects.get(0).n.nextExe();
        return null;
    }

   @Override
   public void addedToScene(){
       shape();
   }

    @Override
    public void shape(){
        prepareText();
        shape=new Ellipse2D.Double(
                -10,
                 bound.getY()-3,
                +20,
                +20 
                );
        afterShape();
    }
    private void connectsUpdate(){
        if(oldNeed!=needDraw){
            for(connector c: connects)
                c.shape();
            for(connector c: connectsIn)
                c.shape();
            oldNeed=needDraw;
        }
    }
    @Override
    public void update(){
        if(connects.size()==1 && connectsIn.size()==1){
            needDraw=false;
            connectsUpdate(); return ;
        }
        else if(connects.size()==0){
            needDraw=true;
            connectsUpdate(); return ;
        }
        needDraw=true;
        double a=connects.get(0).angle;
        for(connector c:connectsIn){
            if(Math.abs(c.angle-a)/Math.PI==0){
            //if(c.angle==a){
                needDraw=false;
                break;
            }
        }
        connectsUpdate();
        //super.update();
    }

    int inS=0;
    int connect=0;
    boolean needDraw(boolean n){
        if(!n || !needUpdate) return needDraw;
        boolean oldNeed=needDraw;
        needUpdate=false;
        needDraw=true;
        if(connects.size()==1 && 1==connectsIn.size()){
            needDraw=false; 
        }
        else if(connects.size()==0 || connectsIn.size()==0){
            needDraw=true;
            return true;
        }
        for(connector c:connectsIn){
            if(Math.abs(c.angle-connects.get(0).angle)/Math.PI==0){
                needDraw=false;
            }
        }
        if(oldNeed!=needDraw){
            for(connector c:connectsIn)
                c.shape();
            for(connector c:connects)
                c.shape();
        }
        return needDraw;
    }
    boolean oldNeed=false;
    boolean needDraw=true;
    static Shape cir=new Ellipse2D.Double(-3,-3,6,6);
    Point2D out=new Point2D.Double(0,0);
    @Override
    public void draw(Graphics2D g2d, boolean n){
        if(needDraw || connects.isEmpty() || connectsIn.isEmpty() || nowExecute){
            super.draw(g2d,false);
            if(connects.size()==1){
                //out=this.connectPoint(connects.get(0).angle2);
                //g2d.translate(out.getX(), out.getY());
                //g2d.fill(cir);
                //g2d.translate(-out.getX(), -out.getY());
            }
        }
    }
    @Override
    public boolean highLight(Graphics2D g2d){
        //if(needDraw())
        AffineTransform af = g2d.getTransform();
            super.draw(g2d, false);
        //else
        g2d.setTransform(af);
        return super.highLight(g2d);
    }
    @Override
    public void drawShadow(Graphics2D g2d){
        if(needDraw){
            AffineTransform af = g2d.getTransform();
            g2d.translate(posX, posY);
            g2d.setColor(shadowColor);
            g2d.translate(2,2);
            g2d.fill(shape);
            g2d.setTransform(af);
        }
    }
    @Override
    public JBlock nextExe(){
        if(global.highlightLinks || connects.size()==0)
            return this;
        else if(connects.size() == 1)
            return connects.get(0).n.nextExe();
        return this;
    }

    @Override
    public JBlock nextBlock(){
        if(global.highlightLinks || connects.size()==0)
            return this;
        else if(connects.size() == 1)
            return connects.get(0).n.nextExe();
        return this;
    }

    @Override
    public Point2D connectPoint(float angle){
        if(!needDraw){
            return new Point2D.Float(posX, posY);
        }
        return super.connectPoint(angle);
    }

    @Override
    public boolean contains(double x, double y){
        return shape.contains(x-posX,
               y-posY);
    }

    @Override
    public boolean drawArrow(){
        if(connectsIn.size()==1 && connects.size()>0)
            return false;
        return true;
    }
    @Override
    public boolean drawArrow(connector c){
        if(connectsIn.size()==1 && connects.size()==1)
            return false;
        if(connects.size()==1)
        if(Math.abs(c.angle-connects.get(0).angle)/Math.PI==0){
            return false;
        }
        return true;
    }
}
