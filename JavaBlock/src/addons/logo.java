package addons;

import config.global;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class logo extends canvas2d {    
    public logo(int width, int height){
        super(width,height);
        pos=new Point2D.Double(0,0);
        f.setTitle("LogoBlock");
        pos.setLocation((int)w/2, (int)h/2);
        update();
    }
    int sub=1;
    
    public logo(int width, int height, int sub){
        super(width,height);
        pos=new Point2D.Double(0,0);
        pos.setLocation((int)w/2, (int)h/2);
        update();
    }

    AffineTransform AF=null;
    Point2D pos=new Point2D.Double(0,0);
    Line2D line=new Line2D.Double(0,0,0,0);
    double angle=0;

    boolean penDown=true;
    boolean hideTurtle=false;

    private void drawTurtle(Graphics2D g){
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g.translate(pos.getX(), pos.getY());
        g.rotate(Math.PI-Math.toRadians(angle));
        g.setColor(Color.red);
        g.drawLine(-5, 1, 5, 1);
        g.drawLine(-4, 2, 4, 2);
        g.setColor(Color.black);
        g.drawLine(-5, 0, 5, 0);
        g.drawLine(-5, 0, 0, 20);
        g.drawLine( 5, 0, 0, 20);
        g.drawLine( 0, 0, 0, 3);
    }

    @Override
    public void clear(){
        img=new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
        G=(Graphics2D)img.getGraphics();
        G.setBackground(Color.WHITE);
        G.clearRect(0,0,w, h);
        G.setColor(Color.BLACK);
    }
    public void reset(){
        clear();
    }
    
    public void setAA(boolean set){
        if(set)
            G.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        else
            G.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_OFF);
    }
    boolean closedCanvas=false;

    public boolean isClosedCanvas() {
        return closedCanvas;
    }

    public void setClosedCanvas(boolean closedCanvas) {
        this.closedCanvas = closedCanvas;
    }

    Graphics2D g2d;
    @Override
    public void paint(Graphics g){
        g2d=(Graphics2D) g;
        g2d.scale((float)(getWidth())/w, (float)(getHeight())/h);
        if(img!=null){
            g2d.drawImage(img,0,0, null);
            g2d=(Graphics2D)g;
            //g2d.rotate(Math.PI);
            if(!hideTurtle)
                drawTurtle(g2d);
        }
        else{
            g.setColor(Color.RED);
            g.drawRect(0,0,w,h);
        }
    }

    @Override
    public void update(){
        if(!global.applet)
            if(!f.isShowing())
                f.show();
        //AF=G.getTransform();
        repaint();
    }
    Point2D old=new Point2D.Double(0,0);
    public void forward(double n){fw(n);}
    public void fw(double n){    
        if(!closedCanvas){
            double left=Math.abs(n);
            double l=0;
            while(left>0){
                l=Math.min(left, Math.sqrt(w*h)/2);
                left-=Math.sqrt(w*h)/2;
                l*=Math.signum(n);
                old.setLocation(pos);
                pos.setLocation(
                            (pos.getX()-Math.sin(Math.toRadians(angle))*l),
                            (pos.getY()-Math.cos(Math.toRadians(angle))*l)
                            );
                if(penDown){
                    line.setLine(old, pos);
                    G.draw(line); 
                    if(!getBounds().contains(pos)){
                        double x=pos.getX();
                        double y=pos.getY();
                        line.setLine(old.getX()+w, old.getY(), pos.getX()+w, pos.getY());
                        G.draw(line);
                        line.setLine(old.getX()-w, old.getY(), pos.getX()-w, pos.getY());
                        G.draw(line);
                        line.setLine(old.getX(), old.getY()-h, pos.getX(), pos.getY()-h);
                        G.draw(line);
                        line.setLine(old.getX(), old.getY()+h, pos.getX(), pos.getY()+h);
                        G.draw(line);

                        line.setLine(old.getX()+w, old.getY()+h, pos.getX()+w, pos.getY()+h);
                        G.draw(line);
                        line.setLine(old.getX()-w, old.getY()+h, pos.getX()-w, pos.getY()+h);
                        G.draw(line);
                        line.setLine(old.getX()+w, old.getY()-h, pos.getX()+w, pos.getY()-h);
                        G.draw(line);
                        line.setLine(old.getX()-w, old.getY()-h, pos.getX()-w, pos.getY()-h);
                        G.draw(line);
                        while(x<0)  x+=this.w;
                        while(x>=w) x-=this.w;
                        while(y<0)  y+=this.h;
                        while(y>=h) y-=this.h;
                        pos.setLocation(x, y);
                    }
                }
            }
        }
        else{
            old.setLocation(pos);
            pos.setLocation(
                    (pos.getX()-Math.sin(Math.toRadians(angle))*n),
                    (pos.getY()-Math.cos(Math.toRadians(angle))*n)
                    );
            if(penDown){
                    line.setLine(old, pos);
                    G.draw(line); 
            }
        }
        update();
    }

    public void backward(int n){bw(n);}
    public void bw(double n){
        fw(-n);
    }

    public void turnRight(double n){tr(n);}
    public void tr(double n){
        //G.rotate(Math.toRadians(n));
        angle-=n;
        while(angle<0)    angle+=360;
        while(angle>=360) angle-=360;
        update();
    }
    public void turnLeft(double n){tl(n);}
    public void tl(double n){
        //G.rotate(Math.toRadians(-n));
        angle+=n;
        while(angle<0)    angle+=360;
        while(angle>=360) angle-=360;
        update();
    }

    @Override
    public void setColor(int r, int g, int b){
        G.setColor(new Color(r,g,b));
    }
    @Override
    public void setColor(String c){
        G.setColor(Color.decode(c));
    }
    
    private boolean fillPixel(int x, int y, int C, int dir) {
        if (x < 0 || x >= w - 1 || y < 0 || y >= h)
            return false;
        int CC = img.getRGB(x, y);
        if (C != CC) return false;
        img.setRGB(x, y, G.getColor().getRGB());
        int x2 = x + 1;
        while (img.getRGB(x2, y) == C) {
            img.setRGB(x2, y, G.getColor().getRGB());
            if (dir != 4)
                fillPixel(x2, y + 1, C, 3);
            if (dir != 3)
                fillPixel(x2, y - 1, C, 4);
            x2++;
            if (x2 == w)
                break;
        }
        x2 = x - 1;
        while (img.getRGB(x2, y) == C) {
            img.setRGB(x2, y, G.getColor().getRGB());
            if (dir != 4) 
                fillPixel(x2, y + 1, C, 3);
            if (dir != 3) 
                fillPixel(x2, y - 1, C, 4);
            x2--;
            if (x2 < 0) 
                break;
        }
        return true;
    }

    public void fill(){
        int x=(int)pos.getX();
        int y=(int)pos.getY();
        System.out.println(x+";"+y);
        int C=img.getRGB(x, y);
        System.out.println(C);
        fillPixel(x, y, C, 0);
        update();
    }

    public void dropPen(){dp();}
    public void dp(){
        penDown=true;
    }
    public void pickPen(){pp();}
    public void pp(){
        penDown=false;
    }

    public void hideTurtle(){ht();}
    public void ht(){
        hideTurtle=true;
        update();
    }
    public void showTurtle(){st();}
    public void st(){
        hideTurtle=false;
        update();
    }

    public int distance(double angle){
        angle-=this.angle;
        double s=Math.sin(Math.toRadians(angle));
        double c=Math.cos(Math.toRadians(angle));
        int len=10;
        int maxLen=(int)Math.sqrt(w*w + h*h);
        int col=this.getColor(Math.round(pos.getX()+s), Math.round(pos.getY()-c));
        double posX=pos.getX()+len*s, posY=pos.getY()-len*c;
        while(len++<maxLen){
            posX+=s;
            posY-=c;
            if(getColor(Math.round(posX), Math.round(posY))!=col)
                return len;
        }
        return -1;
    }
    
    public void e(String s){
        eval(s);
    }
    public void eval(String s){
        String e[]=s.split(" ");
        if(e.length==0) return ;
        try{
            if(e[0].equals("fw") || e[0].equals("forward"))
                fw(Double.parseDouble(e[1]));
            else if(e[0].equals("bw") || e[0].equals("backward"))
                bw(Double.parseDouble(e[1]));
            else if(e[0].equals("tl") || e[0].equals("turnLeft"))
                tl(Double.parseDouble(e[1]));
            else if(e[0].equals("tr") || e[0].equals("turnRight"))
                tr(Double.parseDouble(e[1]));

            else if(e[0].equals("pp") || e[0].equals("pickPen"))
                pp();
            else if(e[0].equals("dp") || e[0].equals("dropkPen"))
                dp();
            else if(e[0].equals("ht") || e[0].equals("hideTurtle"))
                ht();
            else if(e[0].equals("st") || e[0].equals("showTurtle"))
                st();
            else if(e[0].equals("dist") || e[0].equals("distance"))
                st();

            else if(e[0].equals("setColor")){
                if(e.length<4) return ;
                setColor(Integer.parseInt(e[1]),
                        Integer.parseInt(e[2]),
                        Integer.parseInt(e[3]));
            }
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(global.Window, "Error while parsing Number",
                    "Script error", JOptionPane.ERROR);
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(global.Window, "Error while parsing LOGO command",
                    "Script error", JOptionPane.ERROR);
        }
    }

    @Override
    public String toString(){
        String s="";
        s+="Turtle pos: "+pos.getX()+"; "+pos.getY();
        return s;
    }
}
