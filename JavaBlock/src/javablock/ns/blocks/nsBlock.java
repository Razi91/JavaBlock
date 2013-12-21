package javablock.ns.blocks;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javablock.ns.NS;
import javax.swing.JLabel;


public class nsBlock {
    NS ns;
    ArrayList<nsBlock> childs= new ArrayList<nsBlock>();
    
    public nsBlock(NS parent){
        this.ns=parent;
    }
    
    public NS getNs(){
        return ns;
    }
    
    public boolean isRemovable(){
        return true;
    }
    
    JLabel contentLabel=new JLabel("[void]");
    String code;
    String comment;
    
    Shape shape;
    protected Shape getShape(){
        int w=getWidth();
        int h=getHeight();
        Rectangle2D rect=new Rectangle2D.Float(-w/2, 0, w,h);
        return null;
    }
    
    public void reShape(){
        shape=getShape();
    }
    
    BufferedImage prerendered=null;
    public void draw(Graphics2D g2d){
        if(prerendered==null){
            Dimension size=getSize();
            prerendered=new BufferedImage(size.width, size.height,
                    BufferedImage.TYPE_4BYTE_ABGR);
        }
        
        
        
    }
    
    public int getWidth(){
        int width=0;
        for(nsBlock b:childs)
            width+=b.getWidth();
        int thisWidth=contentLabel.getWidth()+10;
        return Math.max(width, thisWidth);
    }
    public int getHeight(){
        return contentLabel.getHeight()+10;
    }
    public Dimension getSize(){
        return new Dimension(getWidth(), getHeight());
    }
    
    public nsBlock[] getChilds(){
        return null;
    }
}
