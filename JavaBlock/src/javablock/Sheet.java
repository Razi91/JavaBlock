package javablock;

import java.awt.Color;
import java.util.List;
import javablock.flowchart.JBlock;
import javablock.gui.Interpreter;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import org.w3c.dom.Element;


public abstract class Sheet extends JComponent{
    /**
     * Manager in which this sheet is located and managed
     */
    protected FlowchartManager manager;
    
    public Interpreter I;
    public int interval=200;
    public JSplitPane split;
    
    
    public Sheet(FlowchartManager manager){
        this.manager=manager;
    }
    
    public FlowchartManager getManager(){
        return manager;
    }
    
    JComponent workspaceInstance;
    public JComponent getWorkspace(){
        return workspaceInstance;
    }
    public void setWorkspace(JComponent set){
        workspaceInstance=set;
    }
    
    /**
     * Method called to close sheet and end working over it<br/>
     * Should close all running threads
     */
    public abstract void close();
    
    /**
     * Method used to delete sheet from document
     */
    public abstract void delete();
    
    /**
     * Returns this sheet name
     * @return returns string
     */
    @Override
    public abstract String getName();
    
    /**
     * Set new name
     */
    public abstract void setName(String name);
    
    /**
     * Sets color to selected items
     */
    public abstract void setColor(Color c, int mode);
    
    /**
     * Used to block editing
     * @param editing 
     */
    public abstract void setEditable(boolean editing);
    /**
     * optimize all sheet's elements id's
     * @deprecated
     */
    @Deprecated
    public abstract void optimizeID();
    
    /**
     * Returns all selected elements
     * @return 
     * @deprecated
     */
    @Deprecated
    public abstract List<JBlock> getSelected();
    
    @Deprecated
    public abstract List<JBlock> getBlocks();
    
    public void generateBlocks(){
    }
    
    public  abstract void copy();
    public  abstract void cut();
    public  abstract void paste();
    
    @Deprecated
    public abstract void update();
    
    /**
     * Method used to save sheet as image do file (definied by url)
     */
    public abstract void saveAsImage(String url, String name);
    /**
     * Method used to save sheet as image do file (opens dialog)
     */
    public abstract void saveAsImage();

    /**
     * Saves sheet to exists XML DOM
     * @param root 
     */
    public abstract void saveXml(Element root);
    
    /**
     * 
     * @param f - node
     */
    public abstract void parseXml(Element f);
    
    /**
     * Generates Python script to use in global scope
     * @return Ready Python script
     */
    public abstract String makePythonFunctions();
    
    /**
     * Generates JavaScript script to use in global scope
     * @return Ready Python script
     */
    public abstract String makeJavaScriptFunctions();
    
}


