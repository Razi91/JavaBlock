package config;

import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.swing.JFrame;
import javax.swing.JWindow;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.*;
import org.w3c.dom.Document;


public class misc {
    static public void saveToFile(File f, String s){
        FileWriter fw = null;
        try {
            fw = new FileWriter(f);
            fw.write(s);
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(misc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(misc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    static public String DoctoString(Document document) {
        String result = null;
        if (document != null) {
            StringWriter strWtr = new StringWriter();
            StreamResult strResult = new StreamResult(strWtr);
            TransformerFactory tfac = TransformerFactory.newInstance();
            try {
                Transformer t = tfac.newTransformer();
                t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                t.setOutputProperty(OutputKeys.INDENT, "yes");
                t.setOutputProperty(OutputKeys.METHOD, "xml"); //xml, html, text
                t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                t.transform(new DOMSource(document.getDocumentElement()), strResult);
            } catch (Exception e) {
                System.err.println("XML.toString(Document): " + e);
            }
            result = strResult.getWriter().toString();
        }
        return result;
    }//toString()

    public static String readTextFromJar(String s) {
        InputStream is = null;
        BufferedReader br = null;
        String line;
        String list="";
        try {
          is = misc.class.getResourceAsStream(s);
          br = new BufferedReader(new InputStreamReader(is));
          while (null != (line = br.readLine())) {
             list+=line+"\n";
          }
        }
        catch (Exception e) {
          System.err.println("Error");
        }
        finally {
          try {
            if (br != null) br.close();
            if (is != null) is.close();
          }
          catch (IOException e) {
            System.err.println("Error");
          }
        }
        return list;
      }
    
    public static void addURLToSystemClassLoader(URL url){// throws IntrospectionException {
        URLClassLoader systemClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<URLClassLoader> classLoaderClass = URLClassLoader.class;

        try {
            Method method = classLoaderClass.getDeclaredMethod("addURL", new Class[]{URL.class});
            method.setAccessible(true);
            method.invoke(systemClassLoader, new Object[]{url});
        } catch (Throwable t) {
            t.printStackTrace();
            //throw new IntrospectionException("Error when adding url to system ClassLoader ");
        }
    }

   public static void center(JFrame frame) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point center = ge.getCenterPoint();
        Rectangle bounds = ge.getMaximumWindowBounds();
        int w = Math.max(bounds.width/2, Math.min(frame.getWidth(), bounds.width));
        int h = Math.max(bounds.height/2, Math.min(frame.getHeight(), bounds.height));
        int x = center.x - w/2, y = center.y - h/2;
        frame.setBounds(x, y, w, h);
        if (w == bounds.width && h == bounds.height)
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.validate();
    }
   public static void center2(JFrame frame) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point center = ge.getCenterPoint();
        Rectangle bounds = ge.getMaximumWindowBounds();
        int w = frame.getBounds().width;
        int h = frame.getBounds().height;
        int x = center.x - w/2, y = center.y - h/2;
        frame.setBounds(x, y, w, h);
        if (w == bounds.width && h == bounds.height)
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.validate();
    }
    public static void center(JWindow frame) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point center = ge.getCenterPoint();
        Rectangle bounds = ge.getMaximumWindowBounds();
        int w = Math.min(bounds.width/2, Math.min(frame.getWidth(), bounds.width));
        int h = Math.min(bounds.height/2, Math.min(frame.getHeight(), bounds.height));
        int x = center.x - w/2, y = center.y - h/2;
        frame.setBounds(x, y, w, h);
        frame.validate();
    }

   public static String[] getScriptEngines(){
       ScriptEngineManager mgr = javablock.gui.Interpreter.getScriptManager();
        List<ScriptEngineFactory> factories =
                mgr.getEngineFactories();
        String engs[]=new String[factories.size()];
        int i=0;
        for (ScriptEngineFactory factory: factories) {
            engs[i] = factory.getEngineName();
            i++;
        }
        return engs;
   }

    public static void javaInfo(){
        ScriptEngineManager mgr = new ScriptEngineManager();
        List<ScriptEngineFactory> factories =
                mgr.getEngineFactories();
        for (ScriptEngineFactory factory: factories) {
            System.out.println("ScriptEngineFactory Info");
            String engName = factory.getEngineName();
            String engVersion = factory.getEngineVersion();
            String langName = factory.getLanguageName();
            String langVersion = factory.getLanguageVersion();
            System.out.printf("\tScript Engine: %s (%s)\n",
                    engName, engVersion);
            List<String> engNames = factory.getNames();
            for(String name: engNames) {
                System.out.printf("\tEngine Alias: %s\n", name);
            }
            System.out.printf("\tLanguage: %s (%s)\n",
                    langName, langVersion);
        }
    }
}



