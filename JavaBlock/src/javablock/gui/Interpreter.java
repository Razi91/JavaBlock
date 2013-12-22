package javablock.gui;
import addons.addons;
import config.*;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javablock.*;
import javablock.flowchart.*;
import javax.swing.*;
import javax.script.*;
import widgets.JTable2;
import widgets.TableModelEx;

public class Interpreter extends javax.swing.JPanel implements ComponentListener {
    public ScriptEngine script;
    public Sheet flow;
    private FlowchartManager manager;
    /** Creates new form Interpreter */
    int lines;
    addons add=null;
    reader read;
    
    int wait=0;

    public ScriptRunner run;
    public reader inputReader;

    Sheet called=null;

    public Interpreter(Sheet flow) {
        addComponentListener(this);
        this.flow=flow;
        initComponents();
        initManager();
        read=new reader(this);
        //run=new ScriptRunner(this);
        inputReader=new reader(this);
        add=new addons();
        ValuesPanel.setViewportView(values2);
        ValuesPanel.validate();
        init();
    }

    public void setSheet(Sheet flow){
        if(flow!=this.flow)
            reset();
        this.flow=flow;
    }
    public void setConsole(Object c){
        this.Out=(JTextArea) c;
    }

    private void init(){
        commandLabel.setVisible(false);
        exeButton.setVisible(false);
        command.setVisible(false);
        StartButton = global.Window.scriptStart;
        RunButton = global.Window.scriptRun;
        StepButton = global.Window.scriptStep;
        StopButton = global.Window.scriptStop;
        IntervalSpiner = global.Window.scriptInterval;
        //embeddConsole.add(controll, BorderLayout.WEST);
    }

    private static String allScripts="";
    static String preScript=null;
    public void loadScripts(){
        script.put("outStream", Out);
        script.put("JOptionPanel", new JOptionPane());
        script.put("InputReader", inputReader);
        script.put("addons", add);
        inputReader.reset();
        if (allScripts.length()==0 || preScript==null ||
                !preScript.equals(global.getManager().scriptEngine)) {
            BufferedReader reader = null;
            allScripts = "";
            preScript=global.getManager().scriptEngine;
            try {
                String tmp;
                add.interpreter = this;
                add.engine = script;
                allScripts+=config.scriptLoader.getScript(global.getManager().scriptEngine);
                File dir = new File(System.getProperty("user.home") + "/.JavaBlock/scripts");
                String ex="js";
                if(global.getManager().scriptEngine.equals("python")){
                    ex="py";
                    
                }
                if (dir.exists()) {
                    String[] children = dir.list();
                    if (children == null) {
                    } else {
                        for (String filename : children) {
                            if (!filename.endsWith("."+ex)) {
                                continue;
                            }
                            reader = new BufferedReader(new FileReader(
                                    new File(System.getProperty("user.home")
                                    + "/.JavaBlock/scripts/" + filename)));
                            while ((tmp = reader.readLine()) != null) {
                                allScripts += "\n" + tmp;
                            }
                            reader.close();
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        try {
            script.eval(allScripts);
        } catch (ScriptException ex) {
            Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ScriptEngineManager m=null;
    public static ScriptEngineManager getScriptManager(){
        if(m==null)
            initManager();
        return m;
    }
    static void initManager(){
        if (m != null) 
            return;
        m = new ScriptEngineManager();
        if(global.applet){
            return ;
        }
        //if (m.getEngineByName("python") != null) {
        //    return;
        //}
        File f = new File(System.getProperty("user.home") + "/.JavaBlock/modules/jython.jar");
        if(!f.exists())
            f = new File(new File(global.Window.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getParent()+"/lib/jython.jar");
        if(!f.exists())
            f = new File(new File(global.Window.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getParent()+"/modules/jython.jar");
        if (f.exists()) {
            try {
                URL[] urls = new URL[]{
                    f.toURL()
                };
                ClassLoader load = new URLClassLoader(urls);
                m = new ScriptEngineManager(load);
                if(false){//Python
                    Class PythonInterpreter=load.loadClass("org.python.util.PythonInterpreter");
                    Method initialize=PythonInterpreter.getDeclaredMethod(
                            "initialize", Properties.class, Properties.class , String[].class);
                    Properties props = new Properties();
                    props.setProperty("python.path", f.getParent());
                    initialize.invoke(null, System.getProperties(), props,
                            new String[]{""});
                }
            } catch (SecurityException ex) {
                Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
                m = new ScriptEngineManager();
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
                m = new ScriptEngineManager();
            } catch (MalformedURLException ex) {
                System.out.println("Error");
                m = new ScriptEngineManager();
            } catch (Exception ex) {
                ex.printStackTrace();
                m = new ScriptEngineManager();
            }
        } else {
            m = new ScriptEngineManager();
        }
    }
    static boolean pythonErrorInformed=false;
    private void setEngine(){
        if(global.applet){
            if(global.getManager().scriptEngine.equals("python")){
                if(!pythonErrorInformed)
                JOptionPane.showMessageDialog(global.Window,
                        translator.get("popup.pythonApplet"));
                pythonErrorInformed=true;
                global.getManager().scriptEngine="JavaScript";
                global.scriptEngine="JavaScript";
                script=m.getEngineByName(global.getManager().scriptEngine);
            }
        }
        script=m.getEngineByName(global.getManager().scriptEngine);
        if(script==null && global.getManager().scriptEngine.equals("python")){
            initManager();
            script=m.getEngineByName(global.getManager().scriptEngine);
            if(script==null){
                int ok=JOptionPane.showConfirmDialog(global.Window, 
                        translator.get("popup.pythonNotFound"), 
                        translator.get("popup.pythonNotFound.head"), JOptionPane.YES_NO_OPTION);
                if(ok==JOptionPane.YES_OPTION)
                    configurator.installJython();
                else{
                    global.getManager().scriptEngine="JavaScript";
                    global.scriptEngine="JavaScript";
                    script=m.getEngineByName(global.getManager().scriptEngine);
                }
            }
        }
        if(script==null){
            JOptionPane.showMessageDialog(global.Window, 
                    global.getManager().scriptEngine+" "+translator.get("popup.notFound") +"!");
            global.getManager().scriptEngine="JavaScript";
            global.scriptEngine="JavaScript";
            script=m.getEngineByName(global.getManager().scriptEngine);
        }
        //System.out.println("Script started: "+global.getManager().scriptEngine);
        loadScripts();
        inputReader.reset();
    }
    public void reset(){
        if(!global.ready) return ;
        if(run != null && run.isRunning())
            run.pause();
        if(script!=null){
            script=null;
            System.gc();
        }
        setEngine();
        //StepButton.setEnabled(false);
        //StopButton.setEnabled(false);
        //RunButton.setEnabled(false);
        //StartButton.setEnabled(true);
        setButtons(false,true,false,false);
        exeButton.setEnabled(false);
        if(actual!=null)
            actual.releaseFromExe();
        actual=null;
        lines=0;
        if(flow!=null){
            flow.setEditable(true);
            for(JBlock b:flow.getBlocks()){
                b.releaseFromExe();
            }
            flow.update();
        }
        if(run != null){
        run.actual=null;
        run.reset();
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        controllFull = new javax.swing.JPanel();
        StopButton = new javax.swing.JButton();
        StartButton = new javax.swing.JButton();
        StepButton = new javax.swing.JButton();
        RunButton = new javax.swing.JButton();
        IntervalSpiner = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        exec = new javax.swing.JTextField();
        controll = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        panel = new javax.swing.JPanel();
        embeddConsole = new javax.swing.JPanel();
        consolePane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        outScroll = new javax.swing.JScrollPane();
        Out = new javax.swing.JTextArea();
        ClearButton = new javax.swing.JButton();
        command = new javax.swing.JTextField();
        commandLabel = new javax.swing.JLabel();
        exeButton = new javax.swing.JButton();
        tracker = new javax.swing.JPanel();
        ValuesPanel = new javax.swing.JScrollPane();
        values = new javax.swing.JTable();
        track = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        search = new javax.swing.JCheckBox();
        inputPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        input = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        arguments = new javax.swing.JTextArea();
        consoleButton = new javax.swing.JButton();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config/lang/lang"); // NOI18N
        controllFull.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("scriptPanel.control"))); // NOI18N

        StopButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/media-playback-stop.png"))); // NOI18N
        StopButton.setText(bundle.getString("scriptPanel.reset")); // NOI18N
        StopButton.setActionCommand("Reset|Stop");
        StopButton.setEnabled(false);
        StopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StopButtonActionPerformed(evt);
            }
        });

        StartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/media-playback-start.png"))); // NOI18N
        StartButton.setText(bundle.getString("scriptPanel.start")); // NOI18N
        StartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StartButtonActionPerformed(evt);
            }
        });

        StepButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/media-playback-pause.png"))); // NOI18N
        StepButton.setText(bundle.getString("scriptPanel.step")); // NOI18N
        StepButton.setEnabled(false);
        StepButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StepButtonActionPerformed(evt);
            }
        });

        RunButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/run.png"))); // NOI18N
        RunButton.setText(bundle.getString("scriptPanel.run")); // NOI18N
        RunButton.setEnabled(false);
        RunButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RunButtonActionPerformed(evt);
            }
        });

        IntervalSpiner.setModel(new javax.swing.SpinnerNumberModel(200, 0, 2000, 5));
        IntervalSpiner.setValue(200);

        jLabel1.setText(bundle.getString("scriptPanel.interval")); // NOI18N

        jLabel2.setText("ms");

        jLabel3.setText(bundle.getString("scriptPanel.lisesExecuted")); // NOI18N

        exec.setEditable(false);
        exec.setText("0");

        javax.swing.GroupLayout controllFullLayout = new javax.swing.GroupLayout(controllFull);
        controllFull.setLayout(controllFullLayout);
        controllFullLayout.setHorizontalGroup(
            controllFullLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controllFullLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(IntervalSpiner, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE))
            .addComponent(StopButton, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
            .addComponent(StartButton, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
            .addComponent(StepButton, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
            .addComponent(RunButton, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
            .addGroup(controllFullLayout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exec, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE))
        );
        controllFullLayout.setVerticalGroup(
            controllFullLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controllFullLayout.createSequentialGroup()
                .addComponent(StopButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StartButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StepButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RunButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(controllFullLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(IntervalSpiner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(controllFullLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(exec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        controll.setOrientation(javax.swing.SwingConstants.VERTICAL);
        controll.setRollover(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/media-playback-stop.png"))); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        controll.add(jButton1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/media-playback-start.png"))); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        controll.add(jButton2);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/step.png"))); // NOI18N
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        controll.add(jButton3);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/system-run.png"))); // NOI18N
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        controll.add(jButton4);

        jButton5.setText("X");
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        controll.add(jButton5);

        panel.setPreferredSize(new java.awt.Dimension(236, 359));

        embeddConsole.setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder());

        Out.setColumns(20);
        Out.setFont(new java.awt.Font("Monospaced", 0, 13)); // NOI18N
        Out.setRows(1);
        outScroll.setViewportView(Out);

        ClearButton.setText(bundle.getString("scriptPanel.clear")); // NOI18N
        ClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearButtonActionPerformed(evt);
            }
        });

        commandLabel.setText(bundle.getString("interpreter.command")); // NOI18N

        exeButton.setText("exe");
        exeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(outScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(commandLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(command, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ClearButton, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(outScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ClearButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(command, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commandLabel)
                    .addComponent(exeButton)))
        );

        consolePane.addTab(bundle.getString("scriptPanel.output"), jPanel1); // NOI18N

        values.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Value"
            }
        ));
        ValuesPanel.setViewportView(values);

        jLabel4.setText(bundle.getString("scriptPanel.track")); // NOI18N

        search.setSelected(true);
        search.setText(bundle.getString("scriptPanel.addVars")); // NOI18N

        javax.swing.GroupLayout trackerLayout = new javax.swing.GroupLayout(tracker);
        tracker.setLayout(trackerLayout);
        trackerLayout.setHorizontalGroup(
            trackerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ValuesPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
            .addGroup(trackerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(trackerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(trackerLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(track, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE))
                    .addGroup(trackerLayout.createSequentialGroup()
                        .addComponent(search, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        trackerLayout.setVerticalGroup(
            trackerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, trackerLayout.createSequentialGroup()
                .addComponent(ValuesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(search)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(trackerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(track, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        consolePane.addTab(bundle.getString("scriptPanel.values"), tracker); // NOI18N

        input.setColumns(1);
        input.setRows(1);
        jScrollPane2.setViewportView(input);

        jLabel5.setText(bundle.getString("interpreter.defaultArguments")); // NOI18N

        jLabel6.setText(bundle.getString("interpreter.predefiniedInput")); // NOI18N

        arguments.setColumns(1);
        arguments.setRows(1);
        jScrollPane3.setViewportView(arguments);

        javax.swing.GroupLayout inputPanelLayout = new javax.swing.GroupLayout(inputPanel);
        inputPanel.setLayout(inputPanelLayout);
        inputPanelLayout.setHorizontalGroup(
            inputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                    .addGroup(inputPanelLayout.createSequentialGroup()
                        .addGroup(inputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        inputPanelLayout.setVerticalGroup(
            inputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                .addContainerGap())
        );

        consolePane.addTab(bundle.getString("scriptPanel.Input"), inputPanel); // NOI18N

        embeddConsole.add(consolePane, java.awt.BorderLayout.CENTER);

        consoleButton.setText(bundle.getString("scriptPanel.showHideConsole")); // NOI18N
        consoleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                consoleButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(consoleButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(embeddConsole, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addComponent(embeddConsole, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(consoleButton))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void ClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearButtonActionPerformed
        Out.setText("");
    }//GEN-LAST:event_ClearButtonActionPerformed

    private void StopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StopButtonActionPerformed
        reset();
    }//GEN-LAST:event_StopButtonActionPerformed

    JBlock actual=null;
    boolean highlight=true;
    TableModelEx table;

    JTable2 values2=new JTable2(new TableModelEx());

    private void variablesTablePrepare(){
        JTable2 values=values2;
        String tr=track.getText().replaceAll(" ", "");
        String tracking[]=tr.split(",");
        final String hor[]={translator.get("table.name"), translator.get("table.value")};
        final String row[][]=new String[tracking.length][2];
        table=new TableModelEx(hor);
        int i=0;
        for(String t: tracking){
            row[i][0]=t;
            row[i][1]="";
            table.addRow((Object[])row[i]);
            i++;
        }
        if(this.search.isSelected()){
            String c;
            Pattern pattern=Pattern.compile("var\\s([a-zA-Z_][a-zA-Z0-9_\\[\\]]*)");
            String args[]=((Flowchart)flow).getArgumentsList();
            if(args!=null)
            for(String arg:args){
                String data[]={arg, ""};
                table.addRow(data);
            }
            for(JBlock b:flow.getBlocks()){
                c=b.code;
                Matcher m = pattern.matcher(c);
                while (m.find()) {
                    String data[]={
                        m.group(1),""
                    };
                    table.addRow(data);
                }
            }
        }

        table.optimize();
        values.setModel(table);
        values.getColumnModel().getColumn(0).setPreferredWidth(30);
    }

    public void updateVisual(){
        JTable2 values=values2;
        if(global.markChanges)
            values.low();
        String n;
        if(script!=null)
            for(int i=0; i<values.getRowCount(); i++){
                n=(String)values.getValueAt(i, 0);
                try {
                    Object o=script.eval("getValueOf("+n+")");
                    String v="null";
                    if(o!=null)
                        v = o.toString();
                    if(!v.equals(values.getValueAt(i, 1)))
                        values.setValueAt(v, i, 1);
                } catch (ScriptException ex) {
                }
            }
        if(global.markChanges)
            values.repaint();
        flow.update();
    }

    private void addAllFlows(){
        String code="";
        if(flow.getManager().scriptEngine.equals("JavaScript")){
            code+="";
        }
        else if(flow.getManager().scriptEngine.equals("python")){
            code+="from addons.Geometry import *\n";
        }
        for(Sheet f:flow.getManager().flows){
            if(f instanceof Flowchart){
                Flowchart ff=(Flowchart)f;
                if(flow.getManager().scriptEngine.equals("JavaScript"))
                    code+=ff.makeJavaScript();
                else if(flow.getManager().scriptEngine.equals("python"))
                    code+=ff.makePythonScript();
            }
        }
        try {
            script.eval(code);
        } catch (ScriptException ex) {
            Out.append(translator.get("console.containsError")+"\n");
            ex.printStackTrace();
            String l[]=code.split("\n");
            int i=0;
            for(String c:l){
                System.err.println(i+": "+c);
                i++;
            }
        }
    }

    private void StartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StartButtonActionPerformed
        manager=flow.getManager();
        if(run == null || !run.isAlive()){
            run = new ScriptRunner(this);
        }
        manager.updateFocus();
        if(!manager.scriptEngine.equals(preScript) || script==null){
            reset();
        }
        exec.setText("0");
        setButtons(true,false,true,true);
        exeButton.setEnabled(true);
        addAllFlows();
        flow.setEditable(false);
        flow.update();
        highlight=true;

        variablesTablePrepare();

        run.actual=flow.getBlocks().get(0);
        run.actual.prepareToExe();
        inputReader.inputLine=0;
        run.pause();
    }//GEN-LAST:event_StartButtonActionPerformed
    public void start(){
        StartButtonActionPerformed(null);
    }

    public void stop(){
        reset();
    }
    public void delete(){
        if(run != null)
            run.close();
    }


    public void step(){
        run.step();
    }

    public void addLines(int l){
        exec.setText((Integer.parseInt(exec.getText())+l)+"");
    }

    public void simulateEnd(boolean set){
        run.wait=-100;
        synchronized(run){
            run.notifyAll();
        }
        for(JBlock b:flow.getBlocks()){
            b.releaseFromExe();
        }
        flow.setEditable(set);
        flow.update();
        if(set)
            Out.append("\n"+translator.get("console.end")+"\n");
        setButtons(false,true,false,false);
    }

    synchronized
    private void StepButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StepButtonActionPerformed
        run.step();
    }//GEN-LAST:event_StepButtonActionPerformed

    //boolean running=false;
    private void RunButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RunButtonActionPerformed
        wait=Integer.parseInt(IntervalSpiner.getValue().toString());
        if(!run.isRunning()){
            run.wait=wait;
            if(global.singleCall && wait==0){
                run.wait=-1;
                RunButton.setEnabled(false);
                StepButton.setEnabled(false);
            }
            run.running();
        }
        else{
            run.wait=-10;
        }
    }//GEN-LAST:event_RunButtonActionPerformed
    public void run(){
        RunButtonActionPerformed(null);
    }
    boolean consoleVisible=false;
    private void consoleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_consoleButtonActionPerformed
        consoleVisible^=true;
        if(consoleVisible)
            openConsole();
        else
            hideConsole();
    }//GEN-LAST:event_consoleButtonActionPerformed

    private void exeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exeButtonActionPerformed
        if(StartButton.isEnabled()) return ;
        try {
            String c=command.getText();
            if(c!=null)
                script.eval(command.getText());
            command.setText("");
        } catch (ScriptException ex) {
            JOptionPane.showMessageDialog(global.Window,
                    translator.get("popup.error")+":\n"+ex.getMessage(),
                    translator.get("popup.error"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_exeButtonActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if(consolePane.isVisible()){
            consolePane.setVisible(false);
            controll.setOrientation(JToolBar.HORIZONTAL);
        }
        else{
            consolePane.setVisible(true);
            controll.setOrientation(JToolBar.VERTICAL);
        }
    }//GEN-LAST:event_jButton5ActionPerformed
    
    
    
    Rectangle b;
    //@Override
    public void show1(){
        super.show();
        if(b!=null)
            this.setBounds(b);
    }

    //@Override
    public void hide1(){
        b=this.getBounds();
        this.setBounds(b.x, b.y, 25, b.height);
        
        super.hide();
        global.getManager().makeUI(false);
        global.getManager().SecSplit.setDividerLocation(0.0f);
    }

    public boolean state[]={false,true,false,false};
    public int inter=200;
    void setButtons(boolean stop,boolean start,boolean step,boolean run){
        StartButton.setEnabled(start);
        StopButton.setEnabled(stop);
        RunButton.setEnabled(run);
        StepButton.setEnabled(step);
        state[0]=stop;
        state[1]=start;
        state[2]=step;
        state[3]=run;
        inter=(Integer)IntervalSpiner.getValue();
    }
    public void resetButtons(){
        StopButton.setEnabled(state[0]);
        StartButton.setEnabled(state[1]);
        StepButton.setEnabled(state[2]);
        RunButton.setEnabled(state[3]);
        IntervalSpiner.setValue(inter);
    }
    public int getInterval(){
        if(flow.isVisible())
            inter=(Integer)IntervalSpiner.getValue();
        return inter;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ClearButton;
    public javax.swing.JSpinner IntervalSpiner;
    public javax.swing.JTextArea Out;
    private javax.swing.JButton RunButton;
    private javax.swing.JButton StartButton;
    private javax.swing.JButton StepButton;
    private javax.swing.JButton StopButton;
    private javax.swing.JScrollPane ValuesPanel;
    public javax.swing.JTextArea arguments;
    private javax.swing.JTextField command;
    private javax.swing.JLabel commandLabel;
    private javax.swing.JButton consoleButton;
    public javax.swing.JTabbedPane consolePane;
    private javax.swing.JToolBar controll;
    private javax.swing.JPanel controllFull;
    public javax.swing.JPanel embeddConsole;
    private javax.swing.JButton exeButton;
    private javax.swing.JTextField exec;
    public javax.swing.JTextArea input;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane outScroll;
    private javax.swing.JPanel panel;
    private javax.swing.JCheckBox search;
    private javax.swing.JTextField track;
    private javax.swing.JPanel tracker;
    private javax.swing.JTable values;
    // End of variables declaration//GEN-END:variables
    JFrame console=null;
    private void openConsole(){
        if(console==null||true){
            console=new JFrame();
            console.setAlwaysOnTop(true);
            console.addWindowListener(new WindowListener() {
                public void windowOpened(WindowEvent e) {}
                public void windowClosing(WindowEvent e) {hideConsole();}
                public void windowClosed(WindowEvent e) {}
                public void windowIconified(WindowEvent e) {}
                public void windowDeiconified(WindowEvent e) {}
                public void windowActivated(WindowEvent e) {}
                public void windowDeactivated(WindowEvent e) {}
            });
        }
        embeddConsole.removeAll();
        embeddConsole.validate();
        embeddConsole.repaint();
        console.setLayout(new BorderLayout());
        console.add(consolePane);
        console.pack();
        console.validate();
        console.setVisible(true);
        consoleVisible=true;
    }
    private void hideConsole(){
        if(console.isVisible()==false) return ;
        console.setVisible(false);
        console.removeAll();
        embeddConsole.add(consolePane);
        embeddConsole.validate();
        embeddConsole.repaint();
        consoleVisible=false;
    }

    public int Width=-1;
    public void componentResized(ComponentEvent e) {
        if(this.isShowing())
            Width=this.getWidth();
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
        resetButtons();
    }
    public void componentHidden(ComponentEvent e) {
        hideConsole();
    }

    
    
    
    public class reader extends SwingWorker<String, Void> {
        Interpreter i;
        int inputLine = 0;
        int argLine=0;
        public void reset(){
            inputLine = 0;
            argLine=0;
        }
        String question="";
        public String readArgument(String arg){
            String[] lines = ((Flowchart)flow).getPredefiniedArguments();
            if (argLine == 0 && lines[0].length() == 0) {
            } else if (lines.length > argLine) {
                argLine++;
                if (lines[argLine - 1].equals("?")) {
                } else {
                    return lines[argLine - 1];
                }
            }
            String a = "";
            a= JOptionPane.showInputDialog(translator.get("popup.inputArgument") +arg);
            if (a == null) {
                i.stop();
                return null;
            }
            return a;
        }
        public reader(Interpreter i) {
            this.i = i;
        }
        public double readNumber(String m) {
            String[] lines = i.input.getText().split("\n");
            if (inputLine == 0 && lines[0].length() == 0) {
            } else if (lines.length > inputLine) {
                try {
                    inputLine++;
                    if (lines[inputLine - 1].equals("?")) {
                    } else {
                        double l = Double.parseDouble(lines[inputLine - 1]);
                        return l;
                    }
                } catch (NumberFormatException ex) {
                }
            }
            String a = "";
            do {
                a = JOptionPane.showInputDialog(m);
                if (a == null) {
                    i.stop();
                    return 0;
                }
                a = a.replaceAll(",", ".");
                try {
                    double l = Double.parseDouble(a);
                    return l;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(i, translator.get("popup.inputNumber.error"));
                }
            } while (true);
        }
        public long readInteger(String m) {
            String[] lines = i.input.getText().split("\n");
            if (inputLine == 0 && lines[0].length() == 0) {
            } else if (lines.length > inputLine) {
                try {
                    inputLine++;
                    if (lines[inputLine - 1].equals("?")) {
                    } else {
                        long l = Long.parseLong(lines[inputLine - 1]);
                        return l;
                    }
                } catch (NumberFormatException ex) {
                }
            }
            String a = "";
            do {
                a = JOptionPane.showInputDialog(m);
                if (a == null) {
                    i.stop();
                    return 0;
                }
                a = a.replaceAll(",", ".");
                try {
                    long l = Long.parseLong(a);
                    return l;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(i, translator.get("popup.inputInteger.error"));
                }
            } while (true);
        }
        public boolean readLogic(String m) {
            String[] lines = i.input.getText().split("\n");
            if (inputLine == 0 && lines[0].length() == 0) {
            } else if (lines.length > inputLine) {
                try {
                    inputLine++;
                    if (lines[inputLine - 1].equals("?")) {
                    } else {
                        String l = lines[inputLine - 1];
                        return Boolean.parseBoolean(l);
                    }
                } catch (NumberFormatException ex) {
                }
            }
            String a = "";
            do {
                int b=JOptionPane.showConfirmDialog(i, m);
                if (b!=JOptionPane.NO_OPTION && b!=JOptionPane.OK_OPTION) {
                    i.stop();
                    return false;
                }
                try {
                    boolean l= (b==JOptionPane.OK_OPTION);
                    return l;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(i, translator.get("popup.inputLogic.error"));
                }
            } while (true);
        }
        public String readString(String m) {
            String[] lines = i.input.getText().split("\n");
            if (inputLine == 0 && lines[0].length() == 0) {
            } else if (lines.length > inputLine) {
                inputLine++;
                if (lines[inputLine - 1].equals("?")) {
                } else {
                    return lines[inputLine - 1];
                }
            }
            String a = "";
            a = JOptionPane.showInputDialog(m);
            if (a == null) {
                i.stop();
            }
            return a;
        }
        public char[] readCharArray(String m) {
            String[] lines = i.input.getText().split("\n");
            if (inputLine == 0 && lines[0].length() == 0) {
            } else if (lines.length > inputLine) {
                inputLine++;
                if (lines[inputLine - 1].equals("?")) {
                } else {
                    return lines[inputLine - 1].toCharArray();
                }
            }
            String a = "";
            a = JOptionPane.showInputDialog(m);
            if (a == null) {
                i.stop();
                return "".toCharArray();
            }
            return a.toCharArray();
        }
        public void Write(String m) {
            i.Out.append(m);
            if(m.indexOf("\n")>=0)
                i.outScroll.getVerticalScrollBar().setValue(1000000);
        }

        @Override
        protected String doInBackground() throws Exception {
            return JOptionPane.showInputDialog(question);
        }
    }

}


