package widgets;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JComponent;
import javax.swing.JSplitPane;


public class Resizer extends JComponent implements MouseListener, MouseMotionListener{
    JComponent parent=null;
    int Orientation=0;//HOR VER
    int min=100;
    int max=1000;
    boolean minimalistic=false;
    public static int HORIZONTAL=0;
    public static int VERTICAL=1;
    public Resizer(JComponent resize){
        parent=resize;
        setMinimumSize(new Dimension(10,10));
        this.setPreferredSize(new Dimension(10,10));
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    public void setOrientation(int orient){
        if(orient!=HORIZONTAL && orient!=VERTICAL)
            return;
        Orientation=orient;
    }
    public void setMinimalistic(boolean m){
        minimalistic=m;
        if(m){
            setMinimumSize(new Dimension(2,2));
            setPreferredSize(new Dimension(2,2));
        }
        else if(!m)
        {
            setMinimumSize(new Dimension(10,10));
            setPreferredSize(new Dimension(10,10));
        }
    }
    public void setMin(int m){
        min=m;
    }
    public void setMax(int m){
        max=m;
    }

    @Override
    public void paintComponent(Graphics g){
        g.setColor(Color.GRAY);
        g.fillRect(2, 2, getWidth()-5, getHeight()-5);
        //g.setColor(Color.BLACK);
        //g.drawLine(getWidth()-1, 0, getWidth()-1, getHeight()-1);
    }
    Point pos=new Point(0,0);
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        pos=e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        parent.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(Orientation==VERTICAL)
            setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
        else if(Orientation==HORIZONTAL)
            setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    int round(int a){
        a+=10;
        a/=20;
        return a*20;
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        if(Orientation==HORIZONTAL){
            if(parent.getWidth()+e.getX()-pos.x>min){
                if(e.isControlDown())
                    parent.setPreferredSize(
                            new Dimension(round(parent.getWidth()+e.getX()-pos.x),
                            parent.getHeight()));
                else parent.setPreferredSize(
                            new Dimension(parent.getWidth()+e.getX()-pos.x,
                            parent.getHeight()));

            }
        }
        else if(Orientation==VERTICAL){
            if(parent.getHeight()+e.getY()-pos.y>min){
                if(e.isControlDown())
                    parent.setPreferredSize(
                        new Dimension(parent.getWidth(),
                        round(parent.getHeight()+e.getY()-pos.y))
                        );
                else
                    parent.setPreferredSize(
                        new Dimension(parent.getWidth(),
                        parent.getHeight()+e.getY()-pos.y)
                        );
            }
        }
        parent.revalidate();
        //pos=e.getPoint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}
