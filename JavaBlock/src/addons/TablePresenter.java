package addons;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;


public class TablePresenter extends JFrame {
    Object tab[];
    Object points[][];
    TablePresenter(){
        tab=new Object[0];
        this.setSize(300, 100);
        setVisible(true);
    }
    TablePresenter(Object t[]){
        tab=t;
        this.setSize(300, t.length+100);
        setVisible(true);
    }
    public void set(Object t[]){
        tab=t;
        this.setSize(300, t.length+100);
        setVisible(true);
    }
    @Override
    public void paint(Graphics g){
        Graphics2D g2d=(Graphics2D)g;
        g2d.translate(10,50);
        for(int i=0; i<tab.length; i++){
        }
    }
}
