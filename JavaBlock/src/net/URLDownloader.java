package net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import config.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;

public class URLDownloader implements Runnable{
    String data;
    String url;
    String info;
    String returned;
    boolean showInfo=false;
    public URLDownloader(String url, String data){
        this.url=url;
        this.data=data;
        this.info="Get HTTP";
    }
    public URLDownloader(String url, String data, String info){
        this.url=url;
        this.data=data;
        this.info=info;
        showInfo=true;
    }

    public String get(){
        String g = "";
        synchronized(this){
            try {
                Thread t = new Thread(this);
                t.setName("URLConnection");
                t.start();
                this.wait();
                return getData();
            } catch (InterruptedException ex) {
                Logger.getLogger(URLDownloader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return g;
    }
    int downloaded=0;
    Thread t;
    public String get1(){
        String g = "";
        t = new Thread(this);
        synchronized(t){
            try {
                downloaded=0;
                t.start();
                while(downloaded==0){
                    t.wait();
                }
                if(downloaded==-1)
                    return "";
                return returned;
            } catch (InterruptedException ex) {
                Logger.getLogger(URLDownloader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return g;
    }


    void stop(){
        downloaded=-1;
        synchronized(this){
            notifyAll();
        }
    }

    public void set(){

    }

    public final String getData(){
        synchronized(t){
            try {
                t.wait();
                return returned;
            } catch (InterruptedException ex) {
                Logger.getLogger(URLDownloader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "";
    }

    @Override
    public void run() {
        try {
            JProgressBar progress=null;
            JFrame wnd = null;
            if(showInfo){
                progress = new JProgressBar();
                progress.setMaximum(10);
                progress.setMinimum(0);
                progress.setValue(0);
                progress.setDoubleBuffered(true);
                progress.setStringPainted(true);
                progress.setString("Connecting");
                JPanel panel = new JPanel();
                JPanel sou = new JPanel();
                sou.setLayout(new BorderLayout());
                sou.add(progress);
                JButton cancel = new JButton("Cancel");
                wnd = new JFrame();
                final JFrame WND=wnd;
                final URLDownloader d=this;
                cancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        d.downloaded = -1;
                        WND.setVisible(false);
                        WND.dispose();
                        synchronized(t){
                            t.notifyAll();
                        }
                        t.interrupt();
                    }
                });
                sou.add(cancel, BorderLayout.EAST);
                panel.setLayout(new BorderLayout());
                panel.add(sou, BorderLayout.SOUTH);
                panel.add(new JLabel(
                        info), BorderLayout.NORTH);
                
                wnd.setContentPane(panel);
                wnd.pack();
                wnd.setResizable(false);
                wnd.show();
                misc.center2(wnd);
            }

            URL Url = new URL(this.url);
            //
            if(showInfo){
                progress.setString("Connecting");
                progress.setValue(0);
            }
            URLConnection conn = Url.openConnection();
            conn.setDoOutput(true);
            conn.setReadTimeout(10000);
            //
            if(showInfo){
                progress.setString("Get output Stream");
                progress.setValue(1);
            }
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            if(showInfo){
                progress.setString("Writting headers");
                progress.setValue(3);
            }
            wr.write(data);
            if(showInfo){
                progress.setString("Flush");
                progress.setValue(4);
            }
            wr.flush();
            // Get the response
            int l=conn.getContentLength();
            if(showInfo){
                progress.setString("Opening input stream");
                progress.setValue(5);
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            returned="";
            String line;
            if(showInfo){
                progress.setString("Reading");
                progress.setValue(7);
            }
            while((line= rd.readLine())!=null){
                returned+=line;
            }
            if(showInfo){
                progress.setString("Finnishing");
                progress.setValue(10);
            }
            wr.close();
            rd.close();
            if(showInfo){
                wnd.setVisible(false);
                //wnd.dispose();
            }
            downloaded=1;
            synchronized(t){
                t.notify();
            }
        } catch (UnknownHostException e) {
            System.err.println("Can not connect to server");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
