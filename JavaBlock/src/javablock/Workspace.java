/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javablock;

import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;

/**
 *
 * @author razi
 */
public class Workspace {
    final static int VIEWTYPE_TABS=0;
    final static int VIEWTYPE_MDI=1;
    
    FlowchartManager mng;
    List <Sheet> sheets;
    Workspace(FlowchartManager mng, int type){
        this.mng=mng;
        this.viewType=type;
        switch(viewType){
            case VIEWTYPE_TABS:{
                container=new JTabbedPane();
                ((JTabbedPane)container).setTabPlacement(JTabbedPane.BOTTOM);
            }break;
            case VIEWTYPE_MDI:{
                container=new JDesktopPane();
            }break;
        }
        sheets=new ArrayList<Sheet>();
    }
    Boolean addSheet(Sheet add){
        if(!sheets.contains(add))
            sheets.add(add);
        switch(viewType){
            case VIEWTYPE_TABS:{
                JTabbedPane tabs=(JTabbedPane)container;
                add.setWorkspace(add);
                tabs.add(add.getName(), add);
            }break;
            case VIEWTYPE_MDI:{
                JDesktopPane mdi=(JDesktopPane)container;
                JInternalFrame intframe = new JInternalFrame(add.getName(),true,false,true,true); 
                intframe.setContentPane(add);
                mdi.add(intframe);
                intframe.setVisible(true);
                intframe.putClientProperty("JComponent.sizeVariant", "mini");
                add.setWorkspace(intframe);
                intframe.setBounds(0,0,400,400);
            }break;
        }
        return true;
    }
    void removeSheet(Sheet rem){
        if(!sheets.contains(rem))
            return;
        switch(viewType){
            case VIEWTYPE_TABS:{
                JTabbedPane tabs=(JTabbedPane)container;
                tabs.remove(rem);
            }break;
            case VIEWTYPE_MDI:{
                JDesktopPane mdi=(JDesktopPane)container;
                mdi.remove(rem.getWorkspace());
            }break;
        }
        sheets.remove(rem);
    }
    void removeAll(){
        container.removeAll();
    }
    Sheet getActive(){
        switch(viewType){
            case VIEWTYPE_TABS:{
                JTabbedPane tabs=(JTabbedPane)container;
                return (Sheet) tabs.getSelectedComponent();
            }
            case VIEWTYPE_MDI:{
                JDesktopPane mdi=(JDesktopPane)container;
                return (Sheet) mdi.getSelectedFrame().getContentPane();
            }
        }
        return null;
    }
    void setActive(String name){
        Sheet a=null;
        for(Sheet s:sheets){
            if(s.getName().equals(name)){
                a=s;
                break;
            }
        }
        if(a==null) return ;
        switch(viewType){
            case VIEWTYPE_TABS:{
                JTabbedPane tabs=(JTabbedPane)container;
                tabs.setSelectedComponent(a);
            }break;
            case VIEWTYPE_MDI:{
                JDesktopPane mdi=(JDesktopPane)container;
                ((JInternalFrame)a.getParent()).restoreSubcomponentFocus();
            }break;
        }
    }
    void renameSheetName(String newName, Sheet sheet){
        switch(viewType){
            case VIEWTYPE_TABS:{
                JTabbedPane tabs=(JTabbedPane)container;
                tabs.setTitleAt(tabs.getSelectedIndex(), newName);
            }break;
            case VIEWTYPE_MDI:{
                JDesktopPane mdi=(JDesktopPane)container;
                JInternalFrame intframe=(JInternalFrame)sheet.getWorkspace();
                intframe.setTitle(newName);
            }break;
        }
    }
    List<Sheet> getSheets(){
        return sheets;
    }
    void setSheetList(List<Sheet> list){
        removeAll();
        sheets=list;
        for(Sheet s:sheets){
            addSheet(s);
        }   
    }
    int viewType=VIEWTYPE_TABS;
    JComponent container;
    JComponent getComponent(){
        return container;
    }
}
