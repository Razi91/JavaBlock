package config;

import java.lang.reflect.InvocationTargetException;
import javablock.gui.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.Class;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javablock.FlowchartManager;
import javablock.flowchart.JBlock;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

public class global {
    public static boolean GUI=true;
    public  enum hlightT{
        NONE, SCALE, HIGHLIGHT, AUTO
    }
    public static ArrayList<ScriptRunner> runners=new ArrayList();
    private static FlowchartManager Manager=null;
    public static void setGlobalManager(FlowchartManager n){
        for(int i=0; i<runners.size();){
            if(n.hasRunner(runners.get(i))) 
                i++;
            else{
                runners.get(i).close();
                runners.remove(i);
            }
        }
        Manager=n;
        Window.updateConfig(n);
    }
    public static FlowchartManager getManager(){
        return Manager;
    }
    //Plugins status
    public static boolean jsyntaxpane=false;
    
    public static int version=6010;
    public static boolean debugMode=false;
    
    public static boolean checkUpdate=true;
    public static boolean debug=false;
    public static boolean applet=false;
    public static boolean editable=true;
    public static String startWith=null;
    public static void setApplet(boolean s){
        if(global.debug)
        System.out.println("[init] Applet="+s);
        applet=s;
    }

    
    public static String confDir;
    public static String lastFlow;

    //GFX
    public static boolean antialiasing=true;
    public static boolean grid=true;
    public static boolean pascalMode=false;
    public static boolean fullConnectorValue=true;
    public static boolean prerender=true;
    public static boolean gradients=true;
    public static boolean flowMarks=true;
    public static boolean bolderBorder=true;
    
    public static boolean animations=true;
    
    public static boolean useJLabels=true;
    
    public static int workspaceType=0;

    //public static boolean systemFont=true;
    public static boolean bezierCurves=true;
    public static boolean accel=false;



    //GUI
    public static MainWindow Window=null;
    public static Rectangle WindowSize=new Rectangle(800,600);
    public static configurator conf;
    public static translator translate;
    public static boolean showToolbar=true;
    public static boolean showToolTips=true;
    public static String LaF="Nimbus";

    //STARTUP
    public static boolean showSplash=true;
    public static boolean loadLast=true;

    //COLOR
    public static boolean transparentPNG=false;
    public static String colorPalette="0xffff00\n"+"0xff00ff\n"+"0x00ffff\n"+
                    "0xff8888\n"+"0x88ff88\n"+"0x8888ff\n"+
                    "0xfdfdfd\n"+"0xcccccc\n"+"0x888888\n"+
                    "0x444444\n"+"0x000000\n"+"0x000000";
    public static int colorResolutionX=2, colorResolutionY=2;
    public static String colors[]={"0xffff00", "0x000000", "0x000000", "0xffffff"};

    //SCRIPT
    public static boolean singleCall=true;
    public static boolean highlightLinks=false;
    public static boolean scriptReplace=true;
    public static boolean markChanges=true;
    public static String scriptEngine="JavaScript";
    public static boolean ready=false;

    //EDITING
    public static boolean autoJumps=true;
    public static hlightT hlight=hlightT.AUTO;
    public static boolean snapToGrid=true;

    //FONTS
    public static Font monoFont=new Font("monospaced", Font.PLAIN, 12);
    public static Font monoFontBold=new Font("monospaced", Font.BOLD, 12);
    public static Font monospacedFont=new Font("monospaced", Font.PLAIN, 12);
    //public static Font monoFont=new Font("monospace", Font.PLAIN, 12);
    public static FontRenderContext frc;


    //STROKES
    public static BasicStroke strokeSelection = new BasicStroke(1, BasicStroke.CAP_ROUND,
                     BasicStroke.JOIN_ROUND, 0, new float[]{2,4,2,4}, 0);
    public static BasicStroke strokeNormal = new BasicStroke(1, BasicStroke.CAP_ROUND,
                     BasicStroke.JOIN_ROUND, 0);
    public static BasicStroke strokeBold = new BasicStroke(2, BasicStroke.CAP_SQUARE,
                     BasicStroke.JOIN_MITER, 1);
    public static BasicStroke strokeBold4 = new BasicStroke(4, BasicStroke.CAP_SQUARE,
                     BasicStroke.JOIN_MITER, 1);
    public static BasicStroke strokeBold6 = new BasicStroke(6, BasicStroke.CAP_SQUARE,
                     BasicStroke.JOIN_MITER, 1);
    public static BasicStroke strokeDotted = new BasicStroke(1, BasicStroke.CAP_SQUARE,
                     BasicStroke.JOIN_ROUND, 0, new float[]{4,4}, 0);
    //public static BasicStroke strokeShadow = new BasicStroke(4, BasicStroke.CAP_SQUARE,
    //                 BasicStroke.JOIN_ROUND, 0, new float[]{4,4}, 0);

    public static void Default(){
        if(!applet){
            File settings=new File("./.JavaBlock/config.jbc");
            settings.delete();
        }
        checkUpdate=true;
        if(global.debug)
            System.out.println("[settings] set Default");
        colorPalette="0xffff00\n"+"0xff00ff\n"+"0x00ffff\n"+
                    "0xff8888\n"+"0x88ff88\n"+"0x8888ff\n"+
                    "0xfdfdfd\n"+"0xcccccc\n"+"0x888888\n"+
                    "0x444444\n"+"0x000000\n"+"0x000000";
        colors="0xffff00,0x000000,0x000000,0xffffff".split(",");
        colorResolutionX=2; colorResolutionY=2;
        bolderBorder=true;
        antialiasing=true;
        grid=true;
        pascalMode=false;
        fullConnectorValue=true;
        flowMarks=true;
        singleCall=true;
        prerender=true;
        useJLabels=true;
        gradients=true;
        bezierCurves=true;
        snapToGrid=true;
        accel=false;
        highlightLinks=false;
        //systemFont=true;
        markChanges=true;
        hlight=hlightT.AUTO;
        LaF="Nimbus";
        scriptEngine="JavaScript";

        showToolbar=true;
        scriptReplace=true;
        showSplash=true;
        loadLast=true;
        confDir=System.getProperty("user.home")+"/.JavaBlock";
        lastFlow=confDir+"/last.jbf";
    }

    public static void reset(){
        antialiasing=true;
        grid=false;
        pascalMode=false;
        fullConnectorValue=true;
    }

    public static void init(){
        loadPlugins();
        reset();
        confDir=System.getProperty("user.home")+"/.JavaBlock";
        lastFlow=confDir+"/last.jbf";
        conf=new configurator();
        icons.icons.load(conf);
        translate=new translator();
        addons.Syntax.init();
        frc=((Graphics2D)
                new BufferedImage(1,1,BufferedImage.TYPE_4BYTE_ABGR).getGraphics()
                ).getFontRenderContext();
    }

    public static void setSystemLaF(boolean s){
        //if(true) return ;
        s=true;
        try {
            if(s){
                boolean find=false;
                for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if (LaF.equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        find=true;
                        break;
                    }
                }
                if(!find)
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                //UIManager.put("nimbusBase", new Color(51,140,98));
            }
            else
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(global.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(global.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(global.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(global.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static ClassLoader Classes;
    public static void loadPlugins(){
        loadPlugins(new File(System.getProperty("user.home")+"/.JavaBlock/plugins"));
        loadPlugins(new File("./plugins"));
    }
    public static void loadPlugins(File dir){
        //File dir=new File(System.getProperty("user.home")+"/.JavaBlock/plugins");
        if(!dir.exists()) return;
        String[] children = dir.list();
        for (String filename : children) {
            try {
                if (!filename.endsWith(".jar"))
                    continue;
                System.out.println("[Plugin] load: "+filename);
                ZipClassLoader jar=new ZipClassLoader(
                        System.getProperty("user.home")+"/.JavaBlock/plugins/"+filename);
                URLClassLoader jar3=new URLClassLoader(new URL[]{
                        new File(System.getProperty("user.home")+"/.JavaBlock/plugins/"+filename).toURI().toURL()}
                        ,Classes);
                try {
                    Class c=jar.loadClass("javablock.plugin.Main");
                    try {
                        String modules[]=(String[])c.getMethod("modulesList").invoke(null);
                        for(String module:modules){
                            String info[]=module.split(":");
                            if(info[0].contains("block")){
                                Class newType=jar.loadClass("javablock.plugin."+info[1]);
                                JBlock.addCustomType(
                                        newType.asSubclass(JBlock.class),
                                        newType.getMethod("getName")
                                            .invoke(null).toString()  
                                        //newType.getName()
                                    );
                            }
                            else if(info[0].contains("generator")){
                                Class<javablock.flowchart.generator.Generator> newType
                                        =(Class<javablock.flowchart.generator.Generator>) jar.loadClass("javablock.plugin."+info[1]);
                                javablock.flowchart.generator.Manager.addGenerator(
                                        newType.getMethod("getGeneratorName")
                                            .invoke(null).toString(),
                                        newType

                                    );
                            }
                            else if(info[0].contains("method")){
                                c.getMethod(info[1]).invoke(null);
                            }
                        }
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(global.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(global.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(global.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    catch (Exception ex) {
                        Logger.getLogger(global.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(global.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(global.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}

final class ZipClassLoader extends ClassLoader {
    private final ZipFile file;
    public ZipClassLoader(String filename) throws IOException {
        this.file = new ZipFile(filename);
    }
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if(!name.startsWith("javablock.plugin")){
            
        }
        ZipEntry entry = this.file.getEntry(name.replace('.', '/') + ".class");
        if (entry == null) {
            return global.Classes.loadClass(name);
            //throw new ClassNotFoundException(name);
        }
        try {
            byte[] array = new byte[1024];
            InputStream in = this.file.getInputStream(entry);
            ByteArrayOutputStream out = new ByteArrayOutputStream(array.length);
            int length = in.read(array);
            while (length > 0) {
                out.write(array, 0, length);
                length = in.read(array);
            }
            return defineClass(name, out.toByteArray(), 0, out.size());
        }
        catch (IOException exception) {
            throw new ClassNotFoundException(name, exception);
        }
        catch (Exception exception) {
            throw new ClassNotFoundException(name, exception);
        }
    }
}