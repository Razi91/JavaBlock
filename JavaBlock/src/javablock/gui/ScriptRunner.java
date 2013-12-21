package javablock.gui;

import config.global;
import java.util.logging.Level;
import java.util.logging.Logger;
import javablock.flowchart.JBlock;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.swing.JOptionPane;

public class ScriptRunner extends Thread {
    Interpreter GUI;
    ScriptEngine script;

    ScriptRunner(Interpreter gui){
        GUI=gui;
        setName("ScriptRunner");
        global.runners.add(this);
        start();
    }

    void reset(){
        script=GUI.script;
        actual=GUI.actual;
        wait=-10;
    }
    /**
     *  -11  nothing
     *  -10  stop
     *   -2  wait for step
     *    0  instant
     *   0<  wait ms
     */
    int wait=-10;
    
    instantRunner IR;

    void pause(){
        if( IR!=null){
            IR.interrupt();
        }
        wait=-11;
        synchronized(this){
            notifyAll();
        }
    }
    synchronized void step(){
        if(wait==-1 && IR!=null)
            IR.interrupt();
        if(wait<=-10)
            wait=-2;
        notifyAll();
    }
    synchronized void running(){
        if(wait==-1 && IR!=null)
            IR.interrupt();
        int w=wait;
        notifyAll();
        wait=w;
    }
    public void close(){
        wait=-1000;
        synchronized(this){
            notifyAll();
        }
        interrupt();
    }
    JBlock actual=null;

    public boolean isRunning(){
        if(wait>=0 || wait==-1)
            return true;
        return false;
    }
    
    public boolean stopped(){
        return wait == -1000;
    }

    void sleep(int ms){
        try {
            wait=ms;
            Thread.currentThread().wait((ms>=0?ms:0));
        } catch (InterruptedException ex) {
            Logger.getLogger(ScriptRunner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    synchronized void Wait(){
        try {
            if(wait<-1)
                    wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(ScriptRunner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void cancel(){
        if(IR!=null)
            IR.interrupt();
        this.wait=-100;
    }
    @Override
    public void run(){
        synchronized(this){
            while(true){
                if(wait==-1000) return;
                Wait();
                while(actual!=null){
                    while(wait<-2){ //czekanie na krok
                        if(wait==-1000) return; //zatrzymanie całkowite
                        if(wait==-100) break; //reset
                        Wait();
                    }
                    if(wait==-1){ //instant
                        IR=new instantRunner(this, actual.flow.getName()+"_runFrom("+actual.ID+");", script);
                        IR.start();
                        while(wait==-1)
                            Wait();
                        if(wait==-11 && IR!=null)
                            IR.interrupt();
                        wait=-11;
                    }
                    if(wait==-2){ //Krok
                        GUI.addLines(actual.code.split("\n").length);
                        actual=actual.execute(script, (wait==-2 || wait>0?true:false));
                        GUI.updateVisual();
                        wait=-10;
                        if(actual==null)
                            GUI.simulateEnd(true);
                        continue;
                    }
                    else if(wait>=0){
                        GUI.addLines(actual.code.split("\n").length);
                        actual=actual.execute(script, (wait==-2 || wait>0?true:false));
                        GUI.updateVisual();
                         sleep(wait);
                    }
                    if(actual == null || wait == -11){//Stop
                        GUI.simulateEnd(true);
                        actual=null;
                        GUI.updateVisual();
                        continue;
                    }
                }//while(actual!=null)
            }//while(true)
        }//synchronized
    }
}

class instantRunner extends Thread{
    String script;
    ScriptEngine engine;
    ScriptRunner SR;
    boolean M=true;
    instantRunner(ScriptRunner SR, String script, ScriptEngine engine){
        this.script=script;
        this.engine=engine;
        this.SR=SR;
    }
    @Override
    public void interrupt(){
        try{
            end();
            this.stop();
        } catch(Exception e){
            System.err.println(e.getMessage());
        }
        synchronized(SR){
            SR.notifyAll();
        }
    }

    private void end(){
        if(M){
            M=false;
            SR.wait=-11;
            synchronized(SR){
                SR.notifyAll();
            }
        }
    }

    @Override
    public void run(){
        try {
            engine.eval(script);
        } catch (ScriptException ex) {
            if(M){
                JOptionPane.showMessageDialog(global.Window,
                        "Error while instant evaluate\n Please use interval >0",
                        "Script Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
                end();
            }
        } catch(Exception e){
        }
        if(M)
            end();
    }
}