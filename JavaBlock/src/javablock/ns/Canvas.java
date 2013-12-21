/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javablock.ns;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author razi
 */
public class Canvas extends JComponent {
    NS parent;
    Canvas(NS parent){
        this.parent=parent;
        this.setDoubleBuffered(true);
    }
    public void paint(Graphics g){
        Graphics2D g2d=(Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
    }
}
