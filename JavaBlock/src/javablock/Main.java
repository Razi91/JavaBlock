package javablock;

import java.awt.event.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

import config.*;
import javablock.gui.*;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import net.FileDownloader;
import net.URLDownloader;


public class Main extends JApplet implements ActionListener, Runnable {
    //public Flowchart Flow;
    public Main(){
        //try{
        //    if(getParameter("compact")!=null)
        //        global.compactR=getParameter("compact").equals("true");
        //}catch(Exception e){
        //}
        global.Classes=this.getClass().getClassLoader();
        global.setSystemLaF(true);
        global.setApplet(true);
        global.init();
        MainWindow w=new MainWindow();
        w.remove(w.menu);
        //this.removeAll();
        this.setLayout(new BorderLayout());
        this.add(w.menu, BorderLayout.PAGE_START);
        add(w.getContentPane(), BorderLayout.CENTER);
        //this.validate();
        repaint();
        //new Thread(this).start();
    }

    @Override
    public void init(){
        global.setSystemLaF(true);
        if(getParameter("url")!=null)
        if(!(getParameter("url").equals("none"))){
            //loaders.WebFile file=null;
            String in="";
            try {
                URL url = new URL(getParameter("url"));
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                in="";
                String str;
                while ((str = reader.readLine()) != null){
                    in+=str;
                } reader.close();
                global.getManager().loadXml(in.toString());
            } catch (IOException ex) {
            }
        }
        if(getParameter("editable")!=null)
        super.init();
    }

    public static void checkVersion(){
        if(!global.checkUpdate) return;
        URLDownloader url=new URLDownloader("http://javablock.sourceforge.net/version", "");
        //url.get1();
        String v;
        v=url.get1();
        String ver=""+global.version;
        if(true) return ;
        if(v!=null)
            if(Integer.parseInt(v)>Integer.parseInt(ver)){
            try {
                int an=JOptionPane.showConfirmDialog(null,translator.get("popup.newVersion"),
                        translator.get("popup.newVersion.head"),
                        JOptionPane.YES_NO_OPTION);
                if(an==JOptionPane.YES_OPTION)
                    java.awt.Desktop.getDesktop().browse(
                            new URI("http://javablock.sf.net/"));
            } catch (URISyntaxException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
    }
    public static boolean judgeInited=false;
    public static void judgeInit(){
        global.GUI=false;
        global.init();
        judgeInited=true;
    }
    public static void judgeStart(JTextArea console, String argv[]){
        if(!judgeInited)
            judgeInit();
        boolean saveScripts=false;
        boolean saveImages=false;
        String list[]=new String[100];
        int i=0;
        //System.out.println(argv.length);
        for(String ar:argv){
            System.out.println(ar);
            if(ar.equals("-saveScripts"))
                saveScripts=true;
            if(ar.equals("-saveImages"))
                saveImages=true;
            else{
                if(ar.endsWith(".jbf")){
                    global.lastFlow=ar;
                    list[i]=ar;
                    i++;
                }
            }
        }
        i--;
        if(i>=0 && (saveScripts || saveImages)){
            global.GUI=false;
            while(i>=0){
                MainWindow w=new MainWindow();
                console.append("File: "+new File(list[i]).getName()+"\n");
                console.append("\tLoading document\n");
                global.getManager().loadFile(list[i]);
                global.getManager().fc=new JFileChooser();
                File f=new File(list[i]);
                global.getManager().fc.setSelectedFile(f);
                if(saveImages){
                    console.append("\tSaving image\n");
                    global.prerender=true;
                    System.out.println("Drawing");
                    global.getManager().saveAsImages(f.getParent(),
                            f.getName().substring(0, f.getName().length()-4));
                }
                if(saveScripts){
                    console.append("\tSaving temporary script\n");
                    global.getManager().savePython();
                }
                i--;
            }
        }
    }
    
    public static void main(String argv[]) {
        //misc.javaInfo();
        global.setApplet(false);
        global.init();
        global.setSystemLaF(true);
        boolean saveScripts=false;
        boolean saveImages=false;
        String list[]=new String[100];
        int i=0;
        //System.out.println(argv.length);
        for(String ar:argv){
            System.out.println(ar);
            if(ar.equals("-saveScripts"))
                saveScripts=true;
            if(ar.equals("-saveImages"))
                saveImages=true;
            else if(ar.equals("-uneditable"))
                global.editable=false;
            else{
                if(ar.endsWith(".jbf")){
                    global.lastFlow=ar;
                    list[i]=ar;
                    i++;
                }
            }
        }
        i--;
        if(i>=0 && (saveScripts || saveImages)){
            global.GUI=false;
            while(i>=0){
                MainWindow w=new MainWindow();
                global.getManager().loadFile(list[i]);
                global.getManager().fc=new JFileChooser();
                File f=new File(list[i]);
                global.getManager().fc.setSelectedFile(f);
                if(saveImages){
                    global.prerender=true;
                    System.out.println("Drawing");
                    //global.getManager().saveAsImages(f.getParent(), 
                    //        f.getAbsolutePath()
                    //        .replaceAll(f.getParent(), "")
                    //        .replaceAll(".jbf", "")
                    //        );
                    global.getManager().saveAsImages(f.getParent(),
                            f.getName().substring(0, f.getName().length()-4));
                }
                if(saveScripts)
                    global.getManager().savePython();
                i--;
            }
            System.exit(0);
        }
        else{
            if (global.showSplash) {
                Splash spl = new Splash(4000);
                spl.showSplash();
            }
            MainWindow w=new MainWindow();
            misc.center(w);
            w.setVisible(true);
            checkVersion();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
    MainWindow w;
    public void run() {
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run(){
            w=new MainWindow();
            w.remove(w.menu);
            setLayout(new BorderLayout());
            add(w.menu, BorderLayout.PAGE_START);
            add(w.getContentPane(), BorderLayout.CENTER);
            repaint();
            }
        });

    }
    @Override
    public void destroy(){
        try {
            for (ScriptRunner run : global.runners) {
                run.close();
            }
            if(global.getManager()!=null)
                global.getManager().saveExit();
            global.conf.saveConfig();
            super.finalize();
            System.exit(0);
        } catch (Throwable ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }     
}



