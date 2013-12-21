/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javablock.flowchart.blocks;
import config.global;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import javablock.*;
import javablock.flowchart.Flowchart;
import javablock.flowchart.JBlock;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.swing.JOptionPane;

/**
 *
 * @author razi
 */
public class cpuBlock extends JBlock {
    public cpuBlock(Flowchart parent){
        super(Type.CPU, parent);
    }

    @Override
    public void shape(){
        prepareText();
        Rectangle2D cpu=new Rectangle.Double(
                        bound.getX()-10,
                        bound.getY()-10,
                        bound.getWidth()+20,
                        bound.getHeight()+20
                        );
                shape=cpu;
        afterShape();
    }
}
