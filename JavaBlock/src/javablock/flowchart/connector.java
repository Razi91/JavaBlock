package javablock.flowchart;

import config.global;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class connector implements FlowElement {
    public JBlock f=null, n=null;
    public String value="";

    public connector(JBlock from, JBlock to){
        f=from; n=to;
        needUpdate=true;
    }

    public void delete(){
        f.connects.remove(this);
        n.connectsIn.remove(this);
        f=null;
        n=null;
    }

    public void setValue(String v){
        value=v;
        needUpdate=true;
    }

    Line2D lineS=new Line2D.Float();
    GeneralPath curve = new GeneralPath();;
    GeneralPath arrow = new GeneralPath();;
    Shape line;
    Shape s;
    public boolean needUpdate=true;
    Point2D from, to;

    public float angle, angle2;
    float angleL;
    TextLayout val;
    
    static double arrowLength=12;

    public void shape(){
        needUpdate=true;
        if(n.needUpdate)
            n.shape();
        from=new Point2D.Float(f.posX, f.posY);
        to=new Point2D.Float(n.posX, n.posY);
        angle=(float) Math.atan2(to.getX()-from.getX(), to.getY()-from.getY());
        angle2=(float) Math.atan2(from.getX()-to.getX(), from.getY()-to.getY());
        from=f.connectPoint(angle2);
        to=n.connectPoint(angle);
        angleL=(float) Math.atan2(from.getX()-to.getX(), from.getY()-to.getY());

        //float ang=(float) Math.atan2(from.getX()-to.getX(), from.getY()-to.getY());

        if(global.bezierCurves && Math.abs(angle)!=0 &&
                Math.abs(angle)!=Math.PI/2 && Math.abs(angle)!=Math.PI){
            curve.reset();
            curve.moveTo(from.getX(),from.getY());
            Point2D f1=f.connectPointBezier(angle2, to, 1.0f);
            Point2D f2=n.connectPointBezier(angle, from, 1.0f);
            Point2D f2a=new Point2D.Double(
                    to.getX()-Math.signum(to.getX()-f2.getX())*10,
                    to.getY()-Math.signum(to.getY()-f2.getY())*10
                    );
            f2.setLocation(
                    f2.getX()-Math.signum(to.getX()-f2.getX())*10,
                    f2.getY()-Math.signum(to.getY()-f2.getY())*10
                    );
            curve.curveTo(f1.getX(), f1.getY(), f2.getX(), f2.getY(), f2a.getX(), f2a.getY());
            curve.lineTo(to.getX(), to.getY());
            line=curve;
            GeneralPath g=new GeneralPath();
            double an=0;
                 if(angle >=-Math.PI*0.25 && angle<=Math.PI*0.25)
                an=Math.PI;
            else if(angle >= Math.PI * 0.75 || angle <= -Math.PI * 0.75)
                an=0;
            else if(angle >= Math.PI * 0.25 && angle <= Math.PI * 0.75)
                an=-Math.PI/2;
            else if(angle <= -Math.PI * 0.25 && angle >= -Math.PI * 0.75)
                an=Math.PI/2;
            //g.moveTo(to.getX(), to.getY());
            g.moveTo(to.getX()+Math.sin(an+0.3)*arrowLength, to.getY()+Math.cos(an+0.3)*arrowLength);
            //g.moveTo(to.getX(), to.getY());
            g.lineTo(to.getX(), to.getY());
            g.lineTo(to.getX()+Math.sin(an-0.3)*arrowLength, to.getY()+Math.cos(an-0.3)*arrowLength);
            //g.lineTo(to.getX(), to.getY());
            s=g;
        }
        else{
            lineS.setLine(from, to);
            line=lineS;
            GeneralPath g=new GeneralPath();
            //g.moveTo(to.getX(), to.getY());
            g.moveTo(to.getX()+Math.sin(angleL+0.3)*arrowLength, to.getY()+Math.cos(angleL+0.3)*arrowLength);
            //g.moveTo(to.getX(), to.getY());
            g.lineTo(to.getX(), to.getY());
            g.lineTo(to.getX()+Math.sin(angleL-0.3)*arrowLength, to.getY()+Math.cos(angleL-0.3)*arrowLength);
            
            s=g;
        }
        String value=this.value;
        if(value.length()==0)
            value=" ";
        if(config.translator.misc.containsKey(value))
            val=new TextLayout(config.translator.misc.getString(value), global.monoFont, f.flow.frc);
        else
            val=new TextLayout(value, global.monoFont, f.flow.frc);
        //if(n.type==JBlock.Type.JUMP)
        //    ((jumpBlock)n).needUpdate=true;
        needUpdate=false;
    }
    
    public void draw(Graphics2D g2d){
        if(n==null || f==null) return ;
        if(needUpdate==true)
            shape();
        g2d.setColor(f.borderColor);
        if(global.fullConnectorValue==false){
            if(value == null ? "false" == null : value.equals("false"))
                g2d.setStroke(global.strokeSelection);
            else
                g2d.setStroke(global.strokeNormal);
        }
        else{
            if(value.length()>1){
                AffineTransform transform = g2d.getTransform();
                    g2d.translate(from.getX(), from.getY());
                    if(angleL<0){
                        g2d.rotate(-angleL - Math.PI/2);
                        val.draw(g2d, 2, -3);
                    }
                    else{
                        g2d.rotate(-angleL - Math.PI/2);
                        g2d.scale(-1, -1);
                        val.draw(g2d, (float)-val.getBounds().getWidth()-2, -3);
                    }
                g2d.setTransform(transform);
            }
        }
        if(n.drawArrow(this))
            g2d.fill(s);
            //g2d.draw(s);
        g2d.draw(line);
        g2d.setStroke(global.strokeNormal);
    }

    public boolean isEditable() {
        return false;
    }

    public BlockEditor getEditor() {
        return null;
    }

    public boolean contains(double x, double y) {
        return line.intersects(new Rectangle2D.Double(x-2, y-2,4,4));
    }
    public boolean intersects(Shape s) {
        return line.intersects(s.getBounds2D());
    }

    public Rectangle2D bound2D() {
        return line.getBounds2D();
    }

    public Rectangle bound() {
        return line.getBounds();
    }

    public void draw(Graphics2D g2d, boolean drawFull) {
        draw(g2d);
    }

    public void drawSelection(Graphics2D g2d) {
    }

    public boolean highLight(Graphics2D g2d) {
        return false;
    }

    public void drawShadow(Graphics2D g2d) {
    }
}
