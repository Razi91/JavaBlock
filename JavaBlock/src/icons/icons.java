package icons;

import javax.swing.ImageIcon;

public class icons {
    public static ImageIcon alignVer;
    public static ImageIcon alignHor;
    public static ImageIcon alignGrid;
    //public static ImageIcon alignVer;
    public static void load(Object o){
        alignVer=new ImageIcon(o.getClass().getResource("/icons/align-vertical.png"));
        alignHor=new ImageIcon(o.getClass().getResource("/icons/align-horisontal.png"));
        alignGrid=new ImageIcon(o.getClass().getResource("/icons/align-grid.png"));
    }
}
