package addons;

import config.global;
import config.misc;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class canvas2d extends JPanel {
    protected int w, h;
    protected JFrame f;
    protected JApplet f2;
    public boolean autoUpdate=true;
    JMenuBar menu;
    public canvas2d(int width, int height){
        global.setSystemLaF(true);
        img=new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        this.w=width;
        this.h=height;
        this.setSize(w, h);
        this.setPreferredSize(new Dimension(w,h));
            f=new JFrame();
            f.setTitle("JavaBlock Canvas2D");
            f.setLayout(new java.awt.BorderLayout());
            menu=makeMenu();
            f.setJMenuBar(menu);
            f.add(this, java.awt.BorderLayout.CENTER);
            f.pack();
            clear();
            f.setVisible(true);
            misc.center(f);
            f.setSize(w,h+menu.getPreferredSize().height);
        update();
    }

    ActionListener menuAction = new ActionListener() {
        void save(){
            JFileChooser fc = new JFileChooser();
            javax.swing.filechooser.FileFilter ff = new javax.swing.filechooser.FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".png");}
                @Override
                public String getDescription() {
                    return "Portable Network Graphics (*.png)";}
            };
            fc.setFileFilter(ff);
            fc.showSaveDialog(fc);
            if (fc.getSelectedFile() != null) {
                File f = fc.getSelectedFile();
                String fname[] = f.getPath().split("\\.");
                if (fname.length < 1) {
                    f = new File(f.getPath() + ".png");
                } else if (!fname[fname.length - 1].equals("png")) {
                    f = new File(f.getPath() + ".png");
                }
                try {
                    ImageIO.write(img, "png", f);
                } catch (Exception ex) {
                }
            }
        }
        void onTop(){
            f.setAlwaysOnTop(onTop.isSelected());
        }
        void scale(int s){
            f.setSize((w*s)/100, (h*s)/100 +menu.getPreferredSize().height
                    +(progress.isVisible()?progress.getHeight():0));
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("save"))
                save();
            else if(e.getActionCommand().equals("onTop"))
                onTop();
            else if(e.getActionCommand().startsWith("scale/"))
                scale(Integer.parseInt(e.getActionCommand().split("/")[1]));
        }
    };
    JCheckBoxMenuItem onTop;
    
    final JMenuBar makeMenu(){
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config/lang/lang");
        JMenuBar m=new JMenuBar();
        JMenuItem save=new JMenuItem();
        //save.setText("save");
        save.setText(bundle.getString("main.save"));
        save.setActionCommand(bundle.getString("main.save"));
        save.addActionListener(menuAction);
        onTop=new JCheckBoxMenuItem();
        onTop.setText("Show on top");
        onTop.setActionCommand("onTop");
        onTop.addActionListener(menuAction);
        JMenu scalesMenu=new JMenu("Scales");
        int scales[]={15, 25, 50, 75, 100, 125, 150, 175, 200, 400};
        for(int s:scales){
            JMenuItem it=new JMenuItem();
            it.setText(""+s+"%");
            it.setActionCommand("scale/"+s);
            it.addActionListener(menuAction);
            scalesMenu.add(it);
        }
        m.add(save);
        m.add(onTop);
        m.add(scalesMenu);
        return m;
    }

    public Graphics2D G;
    protected BufferedImage img;
    public void clear(){
        img=new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
        G=(Graphics2D) img.getGraphics();
        G.setBackground(Color.WHITE);
        G.clearRect(0,0,w, h);
        G.setColor(Color.BLACK);
    }
    public void clearColor(int r, int g, int b){
        clear();
        G.setBackground(new Color(r, g, b));
        G.clearRect(0,0,w, h);
        G.setColor(Color.BLACK);
    }
    public Graphics2D g2d;
    @Override
    public void paint(Graphics g){
        g2d=(Graphics2D) g;
        g2d.scale((float)(getWidth())/w, (float)(getHeight())/h);
        if(img!=null)
            g.drawImage(img,0,0, null);
        else{
            g.setColor(Color.YELLOW);
            g.drawRect(0,0,w,h);
        }
    }
    public void update(){
        if(!global.applet)
        if(!f.isShowing())
            f.show();
        repaint();
    }
    
    public void setColor(int r, int g, int b){
        G.setColor(new Color(r,g,b));
    }
    public void setColor(double r, double g, double b){
        G.setColor(new Color((int)(r),(int)(g),(int)(b)));
    }
    public void setColor(String c){
        G.setColor(Color.decode(c));
    }

    public void setAA(boolean set){
        if(set)
            G.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        else
            G.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_OFF);
    }
    Rectangle2D pix=new Rectangle2D.Double(0,0,1,1);
    public void drawPixel(String x, String y){
        drawPixel(Double.parseDouble(x), Double.parseDouble(y));
        Color c;
    }
    public void drawPixel(double x, double y){
        pix.setRect(x,y,1,1);
        G.fill(pix);
        if(autoUpdate)
            update();
    }
    protected Line2D pen=new Line2D.Double();
    protected Point2D penFrom=new Point2D.Double();
    protected Point2D penTo=new Point2D.Double();
    protected boolean isPen=false;
    public void lineTo(double x, double y){
        penFrom.setLocation(penTo);
        penTo.setLocation(x, y);
        pen.setLine(penFrom, penTo);
        G.draw(pen);
        if(autoUpdate)
            update();
    }
    public void lineFrom(double x, double y){
        penTo.setLocation(x, y);
        penFrom.setLocation(x,y);
        pen.setLine(penFrom, penTo);
    }
    public void lineDraw(){
        G.draw(pen);
        if(autoUpdate)
            update();
    }

    public void drawLine(double x1, double y1, double x2, double y2){
        Line2D l=new Line2D.Double(x1, y1, x2, y2);
        G.draw(l);
        if(autoUpdate)
            update();
    }

    protected JProgressBar progress=new JProgressBar();
    public void addProgress(int max){
        progress.setMaximum(max);
        progress.setMinimum(0);
        progress.setValue(0);
        progress.setString("");
        progress.setStringPainted(true);
        f.add(progress, java.awt.BorderLayout.SOUTH);
        f.pack();
    }
    public void setProgress(int m){
        progress.setValue(m);
        progress.setString("Progress: "+(m*100)/progress.getMaximum()+"%");
    }


    public Area Area(){
        Area ar=new Area();
        return ar;
    }
    public Rectangle2D Rectangle(){
        Rectangle2D rec=new Rectangle2D.Double();
        return rec;
    }
    public Line2D Line(){
        Line2D rec=new Line2D.Double();
        return rec;
    }
    public Path2D Path(){
        Path2D rec=new Path2D.Double();
        return rec;

    }
    public static AffineTransform transform=new AffineTransform();
    
    protected Color colors[]={
        Color.WHITE,
        Color.BLACK,
        Color.GRAY, Color.lightGray,
        Color.RED,  Color.GREEN,    Color.BLUE,
        Color.CYAN, Color.MAGENTA,  Color.YELLOW
    };
    
    public void drawMap(int map[][]){
        int x=0,y=0;
        int w=this.w/map[0].length, h=this.h/map.length;
        for(x=0; x<map.length;x++){
            for (y=0; y<map[x].length; y++) {
                if(map[y][x]>0 && map[x][y]<=colors.length){
                    G.setColor(colors[map[x][y]-1]);
                    G.fillRect(x*w, y*h, w, h);
                }
            }
        }
        if(autoUpdate)
            update();
    }
    
    public void drawMap(String map){
        int x=0,y=0;
        String line[]=map.split("\n");
        int w=this.w/line[0].length(), h=this.h/line.length;
        for(y=0; y<line.length;y++){
            for (x=0; x<line[y].length(); x++) {
                if(line[y].charAt(x)-'0'>=0 && line[y].charAt(x)-'0'<=colors.length){
                    G.setColor(colors[(line[y].charAt(x)-'0')]);
                    G.fillRect(x*w, y*h, w, h);
                }
            }
        }
        if(autoUpdate)
            update();
    }
    public int getColor(long x, long y){
        return getColor((int)x, (int)y);
    }
    public int getColor(int x, int y){
        Color c=new Color(img.getRGB(x, y));
        for(int i=0; i<colors.length; i++)
            if(c.equals(colors[i]))
                return i;
        return -1;
    }
    
    
}
