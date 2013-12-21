/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javablock.interfaces;

/**
 * Interface implements by all objects used in simulation
 * @author razi
 */
public interface ScriptFragment {
    public String getCodeFor(String lang);
    public void prepareToExecute();
    public void releaseFromExecute();
    public ScriptFragment execute();
}
