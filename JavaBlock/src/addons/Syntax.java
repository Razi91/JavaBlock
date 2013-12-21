package addons;

import config.misc;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;

public class Syntax {
    public static Object js;
    public static Object py;
    public static Object cpp;
    //public static Object pascal;
    public static Object plain;
    public static Object xml;
    public static Object html;
    public static boolean loaded=false;
    private static ClassLoader loader;
    
    public static void clearUndos(JEditorPane ed){
        if(!loaded) return;
        try {
            Object o=ed.getDocument();
            o.getClass().getMethod("clearUndos").invoke(o);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Syntax.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Syntax.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Syntax.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(Syntax.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Syntax.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void clearUndos(){
        if(!loaded) return;
        try {
            Class c;
            c = loader.loadClass("jsyntaxpane.syntaxkits.JavaScriptSyntaxKit");
            c.getMethod("initKit").invoke(js);

            c = loader.loadClass("jsyntaxpane.syntaxkits.PythonSyntaxKit");

            c = loader.loadClass("jsyntaxpane.syntaxkits.PlainSyntaxKit");

            c = loader.loadClass("jsyntaxpane.syntaxkits.XmlSyntaxKit");

            c = loader.loadClass("jsyntaxpane.syntaxkits.XHTMLSyntaxKit");
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Syntax.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Syntax.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Syntax.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(Syntax.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Syntax.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Syntax.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void init(){
        try {
            loader=new String().getClass().getClassLoader();
            String libsLoc="";
            Syntax o=new Syntax();
            String p=o.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            File fi=new File(p);
            libsLoc=fi.getParent()+"/lib/jsyntaxpane.jar";
            if(!new File(libsLoc).exists()){
                File f= new File(System.getProperty("user.home") +
                        "/.JavaBlock/modules/jsyntaxpane.jar");
                if(!f.exists())
                    f= new File(fi.getParent()+"/modules/jsyntaxpane.jar");
                if(!f.exists())
                    return ;
                libsLoc=f.getAbsolutePath();
            }
            loader = null;
            URL[] urls = new URL[]{new File(libsLoc).toURL()};
            misc.addURLToSystemClassLoader(urls[0]);
            loader = new URLClassLoader(urls);
            Class c;
            Constructor con;
            try {
                c=loader.loadClass("jsyntaxpane.DefaultSyntaxKit");
                c.getMethod("initKit").invoke(null);
                
                c=loader.loadClass("jsyntaxpane.syntaxkits.JavaScriptSyntaxKit");
                con = c.getConstructor();
                js = con.newInstance();
                //c.getMethod("initKit").invoke(js);
                
                c=loader.loadClass("jsyntaxpane.syntaxkits.PythonSyntaxKit");
                con = c.getConstructor();
                py = con.newInstance();
                //c.getMethod("initKit").invoke(py);
                
                c=loader.loadClass("jsyntaxpane.syntaxkits.CppSyntaxKit");
                con = c.getConstructor();
                cpp = con.newInstance();
                //c.getMethod("initKit").invoke(py);
                
                //c=loader.loadClass("jsyntaxpane.syntaxkits.PasSyntaxKit");
                //con = c.getConstructor();
                //pascal = con.newInstance();
                //c.getMethod("initKit").invoke(py);
                
                c=loader.loadClass("jsyntaxpane.syntaxkits.PlainSyntaxKit");
                con = c.getConstructor();
                plain = con.newInstance();
                //c.getMethod("initKit").invoke(plain);
                
                c=loader.loadClass("jsyntaxpane.syntaxkits.XmlSyntaxKit");
                con = c.getConstructor();
                xml = con.newInstance();
                //c.getMethod("initKit").invoke(xml);
                
                c=loader.loadClass("jsyntaxpane.syntaxkits.XHTMLSyntaxKit");
                con = c.getConstructor();
                html = con.newInstance();
                //c.getMethod("initKit").invoke(html);
                
                loaded=true;
                config.global.jsyntaxpane=true;
            } catch (InstantiationException ex) {Logger.getLogger(Syntax.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {Logger.getLogger(Syntax.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {Logger.getLogger(Syntax.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {Logger.getLogger(Syntax.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchMethodException ex) {Logger.getLogger(Syntax.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {Logger.getLogger(Syntax.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {Logger.getLogger(Syntax.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
