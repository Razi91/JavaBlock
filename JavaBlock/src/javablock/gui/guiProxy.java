package javablock.gui;

import javablock.FlowchartManager;

public interface guiProxy {
    public FlowchartManager Manager=null;
    public void undoAvaiable(boolean is);
    public void redoAvaiable(boolean is);
    public void updateConfig();
    public void updateConfig(FlowchartManager aThis);
}
