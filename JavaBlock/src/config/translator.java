package config;

import java.io.File;
import java.util.Properties;
import java.util.ResourceBundle;


public class translator {
    private File f;
    public static Properties lang = new Properties();
    public static ResourceBundle bundle;
    public static void init(){
        bundle = ResourceBundle.getBundle("config/lang/lang"); // NOI18N
        init=true;
    }
    static boolean init=false;
    public static ResourceBundle tooltips = ResourceBundle.getBundle("config/lang/descriptions");
    public static ResourceBundle settings = ResourceBundle.getBundle("config/lang/settings");
    public static ResourceBundle addons = ResourceBundle.getBundle("config/lang/addons");
    public static ResourceBundle config = ResourceBundle.getBundle("config/lang/settings");

    public static ResourceBundle misc = ResourceBundle.getBundle("config/lang/misc");
    public static String get(String c){
        if(!init)
            init();
        if(bundle.containsKey(c))
            return bundle.getString(c);
        else
            return c;
    }
}
