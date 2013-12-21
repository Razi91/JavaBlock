/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javablock.flowchart.blocks;
import config.global;
import java.awt.BasicStroke;
import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;
import javablock.*;
import javablock.flowchart.Flowchart;
import javablock.flowchart.JBlock;

/**
 *
 * @author razi
 */
public class commentBlock extends JBlock {

    public commentBlock(Flowchart parent){
        super(Type.COMMENT, parent);
        border=global.strokeDotted;
    }
    @Override
    public boolean isSwitchable(){return false;}

    @Override
    public String getScriptFragmentForJavaScript(){
        return "";
    }
    @Override
    public String getScriptFragmentForPython(){
        return "";
    }

    @Override
    public boolean canBeConnected(JBlock b){
        return false;
    }

    @Override
    public void shape(){
        prepareText();
        RoundRectangle2D cpu=new RoundRectangle2D.Double(
                        bound.getX()-10,
                        bound.getY()-10,
                        bound.getWidth()+20,
                        bound.getHeight()+20,
                        25,25
                        );
                shape=cpu;
        afterShape();
    }
}
