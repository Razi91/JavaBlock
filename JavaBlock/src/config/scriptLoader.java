package config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class scriptLoader {
    public static String JS=null;
    public static String Py=null;
    private static StringBuffer fBuf;
    private static String line;
    public static String getScript(String engine){
        if(engine.equals("JavaScript")){
            if(JS==null) load();
            return JS;
        }
        else if(engine.equals("python")){
            if(Py==null) load();
            return Py;
        }
        return "";
    }
    private static void load(){
        JS=misc.readTextFromJar("/config/scriptInit.js");
        Py=misc.readTextFromJar("/config/scriptInit.py");
    }
}
