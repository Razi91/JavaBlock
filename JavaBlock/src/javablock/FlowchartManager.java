package javablock;


import javablock.flowchart.BlockEditor;
import config.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.*;

import javablock.gui.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.logging.*;
import javablock.flowchart.Flowchart;
import javablock.flowchart.JBlock;
import javablock.flowchart.connector;
import javax.swing.*;
import javax.swing.text.EditorKit;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import widgets.help;

public class FlowchartManager extends JPanel implements ActionListener{
    public MetaInfo meta;

    public Flowchart flow;
    public List<Sheet> flows=new ArrayList();

    //JTabbedPane tabs=new JTabbedPane();
    Workspace workspace;
    public JSplitPane MainSplit=new JSplitPane();
    public JSplitPane SecSplit=new JSplitPane();

    public String scriptEngine="JavaScript";
    public boolean isReady=false;
    public JFileChooser fc;

    MainWindow gui;
    public FlowchartManager(MainWindow gui){
        meta=new MetaInfo();
        this.gui=gui;
        gui.Manager=this;
        //tabs.setTabPlacement(JTabbedPane.BOTTOM);
        workspace=new Workspace(this, global.workspaceType);
        workspace.setSheetList(flows);
        setLayout(new BorderLayout());
        New();
        isReady=true;
    }

    public void close(){
        for(Sheet f:flows){
            f.close();
        }
    }
    public boolean hasRunner(ScriptRunner r){
        for(Sheet f: flows){
        //    if(f.getRunner()==r)
        //        return true;
        }
        return false;
    }
    
    private void showMeta(){
        MetaInfo newMeta=new MetaInfo(meta);
        int o=JOptionPane.showConfirmDialog(global.Window, newMeta,
                "Informations", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
        if(o==JOptionPane.OK_OPTION){
            meta=newMeta;
        }
    }

    /**
     * Call before program exit
     * @return -1 when user choosed "Cancel"<br/>
     * 0 when user choosed "No"<br/>
     * 1 when user choosed "Yes"
     */
    public int saveExit(){
        try {
            if(global.applet){
                file=new File(global.confDir+"/last.jbf");
                Writer fw = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
                fw.write(saveXml());
                return 1;
            }
            file=fc.getSelectedFile();
            if(file==null)
                file=new File(global.confDir+"/last.jbf");
            else{
                int o=JOptionPane.showConfirmDialog(global.Window, 
                        "Czy chcesz zapisać plik przed wyjściem?");
                if(o==JOptionPane.NO_OPTION)
                    return 0;
                else if(o==JOptionPane.CANCEL_OPTION)
                    return -1;
            }
            Writer fw = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            fw.write(saveXml());
            fw.close();
            global.lastFlow=file.getAbsolutePath();
            return 1;
        } catch (Throwable ex) {
            Logger.getLogger(FlowchartManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 1;
    }
    public int saveClose(){
        if(global.applet){
            saveFile();
            return 1;
        }
        int o = JOptionPane.showConfirmDialog(global.Window,
                "Czy chcesz zapisać zmiany?");
        if (o == JOptionPane.NO_OPTION)
            return 0;
        else if (o == JOptionPane.CANCEL_OPTION)
            return -1;
        else
            saveFile();
        return 1;
    }
    
    public void loadLast(){
        try{
            newFileChooser();
            file=new File(global.lastFlow);
            if(file.exists()){
                System.out.println("load: "+global.lastFlow);
                loadFile(global.lastFlow);
                if(!global.lastFlow.equals(global.confDir+"/last.jbf"))
                    fc.setSelectedFile(new File(global.lastFlow));
                else
                    fc.setSelectedFile(null);
                if(flows.size()==0){
                    JOptionPane.showMessageDialog(global.Window, translator.get("popup.loadLastError"),
                        translator.get("popup.loadError.head"), JOptionPane.ERROR_MESSAGE);
                    New();
                }
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(global.Window, translator.get("popup.loadLastError"),
                    translator.get("popup.loadError.head"), JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            New();
        }
        
    }

    
    public void makeUI(boolean floated){
        removeAll();
            add(workspace.getComponent());
        this.validate();
    }

    @Deprecated
    public void setInterpreter(Interpreter set){
        gui.updateConfig();
    }
    
    public void setActiveSheet(Sheet f){
        flow=(Flowchart)f;
        gui.updateConfig(this);
    }

    private List<String> history=new ArrayList();
    int historyPos;
    public boolean historyArchive=false;


    void newFileChooser(){
        fc = new JFileChooser();
        javax.swing.filechooser.FileFilter ff = new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".jbf");
            }
            public String getDescription() {
                return "JavaBlock File (*.jbf)";
            }
        };
        fc.setFileFilter((javax.swing.filechooser.FileFilter) ff);
    }

    public void New(){
        New("flowchart");
    }
    public void New(String type){
        workspace.removeAll();
        for(Sheet f:flows){
            f.delete();
        }
        scriptEngine=global.scriptEngine;
        historyArchive=false;
        global.reset();
        history.clear();
        historyPos=0;
        workspace.removeAll();
        flows.clear();
        if(type.equals("flowchart"))
            flow=new javablock.flowchart.Flowchart(this);
        //else if(type.equals("NS"))
        //    flow=new javablock.ns.NS(this);
        flows.add(flow);
        workspace.setSheetList(flows);
        updateFocus();
        this.repaint();
        newFileChooser();
        historyArchive=true;
        history.add(saveXml());
        historyClear();
        if(gui!=null)
            gui.updateConfig();
        file=null;
        makeUI(false);
        System.gc();
    }

    public void addFlowchart(){
        flow=new javablock.flowchart.Flowchart(this);
        flows.add(flow);
        workspace.addSheet(flow);
        //tabs.setSelectedComponent(flow);
        renameFlowchart();
        selectedBlock(flow);
    }
    public void removeFlowchart(){
        if(flows.size()==1) return ;
        workspace.removeSheet(flow);
        flows.remove(flow);
        //flow=(Sheet)tabs.getSelectedComponent();
        flow=(Flowchart)workspace.getActive();
        selectedBlock(flow);
    }
    public void renameFlowchart(){
        //flow=(Sheet)tabs.getSelectedComponent();
        flow=(Flowchart)workspace.getActive();
        do{
            flow.setName(JOptionPane.showInputDialog(
                    translator.get("main.flowcharts.rename.info"),
                    flow.getName()
                    ));
            boolean is=false;
            for(Sheet f:flows){
                if(f==flow) continue;
                if(f.getName().equals(flow.getName())){
                    JOptionPane.showMessageDialog(MainSplit, translator.get("popup.flowMustBeUnique"),
                            translator.get("popup.flowMustBeUnique.head"),JOptionPane.WARNING_MESSAGE);
                    is=true;
                    break;
                }
            }
            if(is) continue;
            break;
        }while(true);
        //tabs.setTitleAt(tabs.getSelectedIndex(), flow.getName());
        workspace.renameSheetName(flow.getName(), workspace.getActive());
    }

    public Element clipBoard=null;
    public void copy(){
        flow.copy();
    }
    public void cut(){
        flow.cut();
    }
    public void paste(){
        flow.paste();
    }

    @Deprecated
    public JBlock addNewBySelected(){
        if(flow.getSelected().size()!=1) return null;
        if(flow.getSelected().get(0).type==JBlock.Type.START) return null;
        Element clipBoardTMP=clipBoard;
        JBlock editing=flow.getSelected().get(0);
        copy();
        paste();
        JBlock n=flow.getSelected().get(0);
        if(editing.connects.size()==1){
            n.connectTo(editing.connects.get(0).n);
        }
        editing.connectTo(n);
        //global.getManager().cancelMoving();
        cancelMoving();
        n.translate(0,
                n.shape.getBounds().height/2+editing.shape.getBounds().height/2+30);
        flow.update();
        clipBoard= clipBoardTMP;
        return n;
    }

    public boolean keepHistory=true;
    public void historyAdd(){
        if(!keepHistory) return ;
        if(global.applet) return ;
        if(!historyArchive) return ;
        String n=saveXml();
        if(true){
            if(history.size()>0 && historyPos<history.size())
            if(n.equals(history.get(historyPos)))
                return ;
            if(history.size()>historyPos+1)
                while(history.size()-1>historyPos)
                    history.remove(historyPos+1);
            history.add(n);
            historyPos++;
        }
        while(history.size()>11){
            history.remove(0);
            historyPos--;
        }
        gui.undoAvaiable(historyPos>0);
        gui.redoAvaiable(false);
        return ;
    }
    public void historyUndo(){
        if(global.applet) return ;
        if(historyPos==0){
            gui.redoAvaiable(historyPos+1<history.size());
            gui.undoAvaiable(historyPos>0);
            return ;
        }
        historyPos--;
        historyArchive=false;
        loadHistoryXml(history.get(historyPos));
        historyArchive=true;
        gui.redoAvaiable(historyPos+1<history.size());
        gui.undoAvaiable(historyPos>0);
        gui.updateConfig();
    }
    public void historyRedo(){
        if(global.applet) return ;
        if(historyPos+1==history.size()){
            gui.redoAvaiable(historyPos+1<history.size());
            gui.undoAvaiable(historyPos>0);
            return ;
        }
        historyPos++;
        historyArchive=false;
        loadHistoryXml(history.get(historyPos));
        historyArchive=true;
        gui.redoAvaiable(historyPos+1<history.size());
        gui.undoAvaiable(historyPos>0);
        gui.updateConfig();
    }
    public void historyClear(){
        while(history.size()>1)
            history.remove(0);
        historyPos=0;
        gui.undoAvaiable(false);
        gui.redoAvaiable(false);
        gui.updateConfig();
    }

    public void cancelMoving(){
        //flow.cancelMoving();
    }

    String path="";
    File file=null;
    boolean saveAs=false;
    
    public void saveAsImages(String url, String name){
        try{
        for(Sheet f:flows){
            f.saveAsImage(url, name);
        }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void saveFileAs(){
        file=null;
        fc.setSelectedFile(null);
        saveAs=true;
        saveFile();
        saveAs=false;
    }
    public void saveFile(){saveFile(false);}
    public void saveFile(boolean exp){
        String s=saveXml();
        if(exp){
            JEditorPane area=new JEditorPane();
            JScrollPane scroll=new JScrollPane();
            scroll.setViewportView(area);
            if(addons.Syntax.loaded)
                area.setEditorKit((EditorKit)addons.Syntax.xml);
            area.setText(s);
            JFrame f=new JFrame();
            f.add(scroll);
            f.setSize(550, 500);//f.pack();
            f.setVisible(true);
        }
        else{
            if(fc.getSelectedFile()==null){
                int returnVal = fc.showSaveDialog(this);
                if(returnVal==0)
                    file = fc.getSelectedFile();
                else return ;
            }
            try {
                File f=fc.getSelectedFile();
                String fname[]=f.getPath().split("\\.");
                if(fname.length<1)
                    f=new File(f.getPath()+".jbf");
                else if(!fname[fname.length - 1].equals("jbf"))
                    f=new File(f.getPath()+".jbf");
                fc.setSelectedFile(f);
                if(saveAs && f.exists()){
                    int ok=JOptionPane.showConfirmDialog(this,
                            translator.get("popup.overwrite"),
                            f.getAbsolutePath(), JOptionPane.YES_NO_OPTION);
                    if(ok==JOptionPane.NO_OPTION)
                        return;
                }
                file=f;
                Writer w = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
                w.write(s);
                w.close();
                //FileWriter fw = new FileWriter(fc.getSelectedFile());
                //fw.write(s);
                //fw.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, translator.get("popup.saveError"),
                        translator.get("popup.saveError"), JOptionPane.ERROR_MESSAGE);
                return ;
            }
        }
    }
    public int loadFile(){ return loadFile(false);}
    public int loadFile(boolean imp){
        String in="";
        if(imp){
            in=JOptionPane.showInputDialog(null);
            if(in!=null){
                boolean ok=loadXml(in);
                if(ok) return 1;
                return -1;
            }
            return 0;
        }
        else{
            int returnVal = fc.showOpenDialog(this);
            if(returnVal != 0) 
                return 0;
            file = fc.getSelectedFile();
            boolean ok = loadXml(file);
            if(ok)
                return 1;
            return -1;
        }
    }
    
    public boolean loadFile(String url){
        String in="";
        File f = new File(url);
        if (fc != null) {
            fc.setSelectedFile(f);
        }
        if (!f.exists()) {
            return false;
        }
        return loadXml(f);
    }


    public String saveXml(){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element root = doc.createElement("JavaBlocks");
            root.setAttribute("version", ""+global.version);
            root.setAttribute("scriptEngine", scriptEngine);
            if(meta.author.getText().length()>0)
                root.setAttribute("author", meta.author.getText());
            if(meta.name.getText().length()>0)
                root.setAttribute("name", meta.name.getText());
            if(meta.description.getText().length()>0){
                Element desc=doc.createElement("description");
                desc.appendChild(doc.createTextNode(meta.description.getText()));
                root.appendChild(desc);
            }
            doc.appendChild(root);
            Element options=doc.createElement("option");
            options.setAttribute("pascal", global.pascalMode+"");
            options.setAttribute("grid", global.grid+"");
            options.setAttribute("fullConnectorValues", global.fullConnectorValue+"");
            root.appendChild(options);
            for(int i=0; i<flows.size(); i++)
                flows.get(i).saveXml(root);
            String out=misc.DoctoString(doc);
            return out;
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(FlowchartManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public void loadHistoryXml(String in){
        try {
            global.reset();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder;
            docBuilder = factory.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(in));
            Document doc = docBuilder.parse(is);
            //int tab=tabs.getSelectedIndex();
            //tabs.removeAll();
            String oldActive=workspace.getActive().getName();
            flows.clear();
            Element main=doc.getDocumentElement();
            NodeList options=main.getElementsByTagName("option");
            for(int i=0; i<options.getLength(); i++){
                Element o=(Element)options.item(i);
                if(o.getAttribute("pascal").equals("true"))
                    global.pascalMode=true;
                if(o.hasAttribute("grid"))
                    global.grid=Boolean.parseBoolean(o.getAttribute("grid"));
                if(o.hasAttribute("fullConnectorValues"))
                    global.fullConnectorValue=
                            Boolean.parseBoolean(o.getAttribute("fullConnectorValues"));
            }
            NodeList flowList=main.getElementsByTagName("flowchart");
            for(int i=0; i<flowList.getLength(); i++){
                Sheet f=new javablock.flowchart.Flowchart(this, (Element)flowList.item(i));
                //f.parseXml((Element)flowList.item(i));
                flow=(Flowchart)f;
                flows.add(f);
                workspace.addSheet(flow);
            }
            //while(tab>=tabs.getTabCount()) tab--;
            //tabs.setSelectedIndex(tab);
            workspace.setActive(oldActive);
            updateFocus();
            repaint();
            gui.updateConfig();
        } catch (SAXException ex) {
            JOptionPane.showMessageDialog(MainSplit, "[h] SAXException\n"+ex.getMessage(),
                    "Load error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(MainSplit, "[h] IOException\n"+ex.getMessage(),
                    "Load error", JOptionPane.ERROR_MESSAGE);
        } catch (ParserConfigurationException ex) {
            JOptionPane.showMessageDialog(MainSplit, "[h] ParserConfigurationException\n"+ex.getMessage(),
                    "Load error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean loadXml(String in){
        try {
            historyArchive=false;
            history.clear();
            historyPos=0;
            global.reset();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder;
            docBuilder = factory.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(in));
            Document doc = docBuilder.parse(is);
            File fi=file;
            New();
            fc.setSelectedFile(fi);
            //tabs.removeAll();
            flows.clear();
            Element main=doc.getDocumentElement();
            return parseXml(main);
        } catch (SAXException ex) {
            JOptionPane.showMessageDialog(MainSplit, "SAXException\n"+ex.getMessage(),
                    "Load error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (IOException ex) {
            if(ex.getMessage().equals("Invalid byte 2 of 3-byte UTF-8 sequence.")) return true;
            JOptionPane.showMessageDialog(MainSplit, "Read file error\n"+ex.getMessage(),
                    "Load error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (ParserConfigurationException ex) {
            JOptionPane.showMessageDialog(MainSplit, "Error while parsing XML\n"+ex.getMessage(),
                    "Load error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean loadXml(File in){
        try{
            historyArchive=false;
            history.clear();
            historyPos=0;
            global.reset();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder;
            docBuilder = factory.newDocumentBuilder();
            Document doc = docBuilder.parse (in);
            File fi=file;
            New();
            fc.setSelectedFile(fi);
            workspace.removeAll();
            flows.clear();
            Element main=doc.getDocumentElement();
            return parseXml(main);
        } catch (SAXException ex) {
            JOptionPane.showMessageDialog(MainSplit, "SAXException\n"+ex.getMessage(),
                    "Load error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (IOException ex) {
            if(ex.getMessage().equals("Invalid byte 2 of 3-byte UTF-8 sequence.")) return true;
            JOptionPane.showMessageDialog(MainSplit, "Read file error\n"+ex.getMessage(),
                    "Load error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (ParserConfigurationException ex) {
            JOptionPane.showMessageDialog(MainSplit, "Error while parsing XML\n"+ex.getMessage(),
                    "Load error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    private boolean parseXml(Element main){
        try {
            if (main.hasAttribute("version")) {
                int v = Integer.parseInt(main.getAttribute("version"));
                if (v / 10 > global.version / 10) {
                    JOptionPane.showMessageDialog(global.Window, 
                            translator.get("popup.flowchartNewer"));
                }
            }
            if (main.hasAttribute("author")) {
                meta.author.setText(main.getAttribute("author"));
            }
            if (main.hasAttribute("name")) {
                meta.name.setText(main.getAttribute("name"));
            }
            if (main.hasAttribute("scriptEngine")) {
                scriptEngine = main.getAttribute("scriptEngine");
            }

            {
                NodeList desc = main.getElementsByTagName("description");
                if (desc.getLength() > 0) {
                    Element e = (Element) desc.item(0);
                    meta.description.setText(e.getTextContent());
                }
            }

            NodeList options = main.getElementsByTagName("option");
            for (int i = 0; i < options.getLength(); i++) {
                Element o = (Element) options.item(i);
                if (o.getAttribute("pascal").equals("true")) {
                    global.pascalMode = true;
                }
                if (o.hasAttribute("grid")) {
                    global.grid = Boolean.parseBoolean(o.getAttribute("grid"));
                }
                if (o.hasAttribute("fullConnectorValues")) {
                    global.fullConnectorValue =
                            Boolean.parseBoolean(o.getAttribute("fullConnectorValues"));
                }
            }
            NodeList flowList = main.getElementsByTagName("flowchart");
            for (int i = 0; i < flowList.getLength(); i++) {
                Sheet f = new javablock.flowchart.Flowchart(this, (Element) flowList.item(i));
                //f.parseXml((Element)flowList.item(i));
                flow = (Flowchart)f;
                // TODO: Remove!!
                //setInterpreter(flow.I);
                flows.add(f);
                workspace.addSheet(flow);
            }
            updateFocus();
            this.repaint();
            gui.updateConfig();
            historyArchive = true;
            flow.optimizeID();
            history.add(saveXml());
            historyClear();
            return true;
        } catch(Exception e){

        }
        return true;
    }

    public void savePython(){
        File fn=new File(fc.getSelectedFile().getAbsolutePath()+".py");
        System.out.println("path:"+fn.getAbsolutePath());
        String py="";//from JavaBlock import *\n";
        for(Sheet fl: flows){
            py+=fl.makePythonFunctions();
        }
        py+="\n\n";
        py+="def getAuthor():"
                + "\treturn \""+this.meta.author.getText()+"\"\n";
        //py+=flows.get(0).getName()+"()\n";
        misc.saveToFile(fn, py);
    }

    private void showScript(){
        JFrame f=new JFrame();
        f.setLayout(new BorderLayout());
        JTabbedPane t=new JTabbedPane();
        f.setContentPane(t);
        JEditorPane js=new JEditorPane();
        JEditorPane py=new JEditorPane();
        JScrollPane jss=new JScrollPane(js);
        JScrollPane pys=new JScrollPane(py);
        String j="", p="";
        for(Sheet fl: flows){
            j+=fl.makeJavaScriptFunctions();
            p+=fl.makePythonFunctions();
        }
        if(addons.Syntax.loaded){
            js.setEditorKit((EditorKit)addons.Syntax.js);
            py.setEditorKit((EditorKit)addons.Syntax.py);
        }
        js.setText(j);
        py.setText(p);
        t.add("JavaScript", jss);
        t.add("python", pys);
        f.setSize(500, 500);
        f.setVisible(true);
    }
    BlockEditor editor=null;
    
    public void updateFocus(){
        //flow=(Flowchart)tabs.getSelectedComponent();
        flow=(Flowchart) workspace.getActive();
        if(flow==null) return;
        int w=SecSplit.getDividerLocation();
        SecSplit.setRightComponent(((Flowchart)flow).I);
        SecSplit.setDividerLocation(w);
        if(false){
            //if(blockEdit!=null)
            //    blockEdit.setActiveSheet(flow);
        }else{
            boolean resize=true;
            if (flow.getSelected().size() == 1) {
                JBlock selected=flow.getSelected().get(0);
                if (selected.isEditable()) {
                    if (editor != null) {
                        resize=false;
                        editor.finnishEdit();
                        if (editor.getBlock() == selected) {
                        } else {
                            //((Flowchart)flow).editorPane.remove((Component) editor);
                        }
                    }
                    editor = selected.getEditor();
                    editor.setEditedBlock(selected);
                    // TODO: Remove
                    flow.editorPane.add((Component) editor, BorderLayout.CENTER);
                    selected.getEditor().setEditedBlock(flow.selected.get(0));
                    flow.editorPane.setVisible(true);
                    flow.editorPane.setType(selected.type);
                    flow.editorPane.revalidate();
                    if(resize){
                        flow.cur.setLocation(flow.cur.getX()+flow.editorPane.getWidth()
                                , flow.cur.getY());
                    }
                    flow.repaint();
                    this.historyAdd();
                } else {
                    if (editor != null) {
                        editor.finnishEdit();
                        flow.editorPane.remove((Component) editor);
                        this.historyAdd();
                    }
                    editor = null;
                    flow.editorPane.setVisible(false);
                }
            } else {
                if (editor != null) {
                    flow.editorPane.remove((Component) editor);
                    editor.finnishEdit();
                    this.historyAdd();
                }
                editor = null;
                flow.editorPane.setVisible(false);
            }
        }
        flow.flow.requestFocusInWindow();
        flow.flow.requestFocus();
    }
    public void selectedBlock(Sheet f){
        flow = (Flowchart)f;
        updateFocus();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String[] action=e.getActionCommand().split("/");
        if(action[0].equals("history")){
            if(action[1].equals("undo"))
                historyUndo();
            else if(action[1].equals("redo"))
                historyRedo();
        }
        else if(action[0].equals("manage")){
            if(action[1].equals("new"))
                New();
            if(action[1].equals("save"))
                saveFile(action[2].equals("true"));
            if(action[1].equals("saveas"))
                saveFileAs();
            if(action[1].equals("saveImage")){
                Sheet f=(Sheet)workspace.getActive();
                f.saveAsImage();
            }
            if(action[1].equals("savePython"))
                savePython();
        }

        else if(action[0].equals("flowcharts")){
            if(action[1].equals("add"))
                this.addFlowchart();
            if(action[1].equals("remove"))
                this.removeFlowchart();
            if(action[1].equals("rename"))
                this.renameFlowchart();
            if(action[1].equals("script"))
                this.showScript();
        }

        else if(action[0].equals("show")){
            if(action[1].equals("help"))
                help.showHelp();
        }

        else if(action[0].equals("clp")){
            if(action[1].equals("cut"))
                cut();
            if(action[1].equals("copy"))
                copy();
            if(action[1].equals("paste"))
                paste();
        }

        else if(action[0].equals("showMeta"))
            this.showMeta();
       
    }

}
