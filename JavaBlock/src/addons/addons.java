package addons;

import javablock.gui.Interpreter;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class addons {
    public ScriptEngine engine;
    public Interpreter interpreter;
    public canvas2d canvas2d(int x, int y){
        return new canvas2d(x,y);
    }
    public logo logo(int x, int y){
        return new logo(x,y);
    }
    public logo logo(int x, int y, int sub){
        return new logo(x,y, sub);
    }

    public File dir = new File(System.getProperty("user.home") + "/.JavaBlock/classes/");
    public ClassLoader loader = null;

    public Object load(String name){
        if (!dir.exists()) 
                return null;
        try {
            URL[] urls = new URL[]{dir.toURL()};
            if(loader==null)
                loader = new URLClassLoader(urls);
            Class c=loader.loadClass(name);
            Object o=null;
            try{
                Constructor con=c.getConstructor(ScriptEngine.class);
                o=con.newInstance(engine);
            } catch (InstantiationException ex) {
                Logger.getLogger(addons.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(addons.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(addons.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(addons.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchMethodException ex) {
                o=c.newInstance();
            }
            return o;
        }  catch (SecurityException ex) {
            Logger.getLogger(addons.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(addons.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(addons.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            try {
                engine.eval("Println(\"Class '"+name+"' not found\");");
                interpreter.stop();
            } catch (ScriptException ex1) {
                Logger.getLogger(addons.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(addons.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public Object load(String jarFile, String name){
        File dir=new File(this.dir.getAbsolutePath()+jarFile+".jar");
        if (!dir.exists()) 
                return null;
        try {
            URL[] urls = new URL[]{dir.toURL()};
            if(loader==null)
                loader = new URLClassLoader(urls);
            Class c=loader.loadClass(name);
            Object o=null;
            try{
                Constructor con=c.getConstructor(ScriptEngine.class);
                o=con.newInstance(engine);
            } catch (InstantiationException ex) {
                Logger.getLogger(addons.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(addons.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(addons.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(addons.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchMethodException ex) {
                o=c.newInstance();
            }
            return o;
        }  catch (SecurityException ex) {
            Logger.getLogger(addons.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(addons.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(addons.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            try {
                engine.eval("Println(\"Class '"+name+"' not found\");");
                interpreter.stop();
            } catch (ScriptException ex1) {
                Logger.getLogger(addons.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(addons.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int[] getTime(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-ss");
        String st[]=sdf.format(cal.getTime()).split("-");
        int t[]={
            Integer.parseInt(st[0]),
            Integer.parseInt(st[1]),
            Integer.parseInt(st[2])
        };
        return t;
    }
}
