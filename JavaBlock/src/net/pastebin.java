package net;

import config.global;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;



public final class pastebin {
    public static String email="";
    public static String expire="1M 1 Month";
    public static String exposure="Public";
    public static boolean askForName=true;
    public static String name;
    public static String xml;
    public static String data;

    public static boolean working=false;

    public static void send( String xml1 ){
        xml=xml1;
        if(askForName){
            global.conf.pastebinPane.remove(global.conf.pastebinConf);
            int ok=JOptionPane.showConfirmDialog(global.Window, global.conf.pastebinConf, "Send", JOptionPane.OK_CANCEL_OPTION);
            global.conf.pastebinPane.setLayout(new BorderLayout());
            global.conf.pastebinPane.add(global.conf.pastebinConf);
            if(ok==JOptionPane.CANCEL_OPTION) return;
            email=global.conf.pastebinEmail.getText();
            name=global.conf.pastebinName.getText();
            exposure=global.conf.pastebinPublic.getSelectedItem().toString();
            expire=global.conf.pastebinExpire.getSelectedItem().toString();
            
        }
        try {// <editor-fold defaultstate="collapsed" desc="Send">
            data="";
            working=true;
            if (askForName) {
                if (name.length() > 0) {
                    data += URLEncoder.encode("paste_name", "UTF-8") + "="
                         + URLEncoder.encode(name, "UTF-8");
                } else {
                    data += URLEncoder.encode("paste_name", "UTF-8") + "="
                         + URLEncoder.encode("JavaBlock", "UTF-8");
                }
            } else {
                data += URLEncoder.encode("paste_name", "UTF-8") + "="
                     + URLEncoder.encode("JavaBlock", "UTF-8");
            }
            //brak api w nazwach
            data += "&" + URLEncoder.encode("api_dev_key", "UTF-8") + "="
                 + URLEncoder.encode("464f791c879e77742a1b377a1a4a8a2d", "UTF-8");
            data += "&" + URLEncoder.encode("api_paste_code", "UTF-8") + "="
                 + URLEncoder.encode(xml, "UTF-8");
            data += "&" + URLEncoder.encode("api_paste_format", "UTF-8") + "="
                 + URLEncoder.encode("xml", "UTF-8");
            data += "&" + URLEncoder.encode("api_paste_expire_date", "UTF-8") + "=" 
                 + URLEncoder.encode(expire.split(" ")[0], "UTF-8");
            data += "&" + URLEncoder.encode("api_paste_email", "UTF-8") + "="
                 + URLEncoder.encode(email, "UTF-8");
            data += "&" + URLEncoder.encode("api_paste_private", "UTF-8") + "="
                 + URLEncoder.encode((exposure.equals("Public") ? "0" : "1"), "UTF-8");
            data += "&" + URLEncoder.encode("api_option", "UTF-8") + "=" 
                + URLEncoder.encode("paste", "UTF-8");
        } catch (Exception e) {
            working=false;
        }
        final URLDownloader down=new URLDownloader(
                //zly adres
                //"http://javablock.sf.net/",
                //"http://javablock.pastebin.com/api_public.php",
                "http://pastebin.com/api/api_post.php",
                data,
                "Exporting to pastebin");
        global.Window.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = down.get1();
                global.Window.setEnabled(true);
                if(address==null){
                    JOptionPane.showMessageDialog(null, "Connection error", "error", JOptionPane.ERROR_MESSAGE);
                    working=false;
                    return ;
                }
                if(!address.isEmpty()){
                    String[] u = address.split("/");
                    JPanel panel = global.conf.pastebinSummary;
                    global.conf.pastebinURL.setText(address);
                    global.conf.pastebinID.setText(u[u.length - 1]);
                    global.conf.pastebinApplet.setText("http://javablock.sf.net/applet.php?url=http://pastebin.com/download.php?i=" + u[u.length - 1]);
                    JOptionPane.showMessageDialog(null, panel);
                }
                working=false;
            }
        }).start();
        // </editor-fold>
    }
    
    public static String load(String id){
        String xml="";
        if(id.length()<5)
            return "";
        try {
            working=true;
            URL url = new URL("http://pastebin.com/download.php?i="+id);
            URLConnection conn=url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            xml="";
            String str;
            while ((str = reader.readLine()) != null){
                xml+=str;
            } reader.close();
            working=false;
            return xml.toString();
        } catch(IOException ex){
            JOptionPane.showMessageDialog(null," Load error");
        }
        working=false;
        return xml;
    }
}
