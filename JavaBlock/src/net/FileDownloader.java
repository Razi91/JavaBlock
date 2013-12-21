package net;

import java.awt.event.ActionEvent;
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
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class FileDownloader implements Runnable{
    String name;
    File output;
    String url;
    boolean returned=false;
    public FileDownloader(String url, String name, String output){
        this.url=url;
        this.name=name;
        this.output=new File(output);
    }
    Thread t;
    public boolean get(){
        String g = "";
        try {
            synchronized(this){
                t = new Thread(this);
                t.start();
                do{
                    System.out.println("wait");
                    wait();
                }while(status);
                System.out.println("OK");
                return returned;
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(URLDownloader.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    static String prefixes[]={"B", "KiB", "MiB", "GiB", "TiB", "PiB"};


    boolean status=true;
    public void run() {
        synchronized(this){
        try {
            URL Url = new URL(this.url);
            URLConnection conn = Url.openConnection();
            conn.setDoOutput(true);
            conn.setReadTimeout(10000);
            
            status=true;
            returned=false;
            JProgressBar progress = new JProgressBar();
            progress.setMaximum(100);
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
            cancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    status = false;
                    t.interrupt();
                }
            });
            sou.add(cancel, BorderLayout.EAST);
            panel.setLayout(new BorderLayout());
            panel.add(sou, BorderLayout.SOUTH);
            panel.add(new JLabel(
                    "Downloading: " + name), BorderLayout.NORTH);
            JFrame wnd = new JFrame();
            wnd.setContentPane(panel);
            wnd.pack();
            wnd.show();
            wnd.setResizable(false);
            misc.center2(wnd);

            int l=conn.getContentLength();
            int size=l;
            int p = 0;
            while (size > 10240) {
                p++;
                size /= 1024;
            }
            progress.setString("Size: "+size+" "+prefixes[p]);

            int prog=0;
            int prog2=0;

            FileOutputStream fos = new java.io.FileOutputStream(output);
            BufferedOutputStream bout = new BufferedOutputStream(fos,1024);
            byte data[] = new byte[1024];
            int count=0;
            int whole=0;

            BufferedInputStream rd = new BufferedInputStream(conn.getInputStream());
            int s=0, p2, p3;
            while(status && (count = rd.read(data,0,1024)) != -1){
                bout.write(data,0,count);
                whole+=count;
                prog=(whole*100)/(l);
                if(prog2!=prog){
                    progress.setValue(prog);
                    long diff=whole-s;
                    s=whole;
                    p2=0;p3=0;
                    while (s > 10240) {
                        p2++;
                        s /= 1024;
                    }
                    progress.setString(s+" "+prefixes[p2]+" of "
                            +size+" "+prefixes[p]
                            );
                    prog2=prog;
                }
            }
            status=false;
            wnd.setVisible(false);
            bout.close();
            rd.close();
            returned=true;
            this.notifyAll();this.notifyAll();this.notifyAll();
        } catch (Exception e) {
        }
        }
    }
}
