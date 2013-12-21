package config;

import addons.Syntax;
import java.awt.Color;
import java.awt.Rectangle;
import java.io.*;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.xml.parsers.*;
import net.FileDownloader;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import widgets.ComboText;

public final class configurator extends javax.swing.JFrame {
    String configPath=(System.getProperty("user.home")+"/.JavaBlock/config.jbc");
    /** Creates new form configurator */
    public configurator() {
        loadConfig();
        global.setSystemLaF(true);
        initComponents();
        init();
        updateConfigGUI();
        saveConfig();
        misc.center(this);
    }
    void init(){
        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
            LaF.addItem(info.getName());
        }
        engines.addItem("JavaScript");
        engines.addItem("python");
        highlighting.removeAllItems();
        highlighting.addItem(new ComboText(translator.config.getString("hlight.NONE"), "NONE"));
        highlighting.addItem(new ComboText(translator.config.getString("hlight.HIGHLIGHT"), "HIGHLIGHT"));
        highlighting.addItem(new ComboText(translator.config.getString("hlight.SCALE"), "SCALE"));
        highlighting.addItem(new ComboText(translator.config.getString("hlight.AUTO"), "AUTO"));
    }

    public boolean colorCheck(){
        String colors[]=colorPalette.getText().split("\n");
        int error=0;
        boolean r=true;
        for(String c:colors){
            try{
                Color.decode(c);
            } catch (NumberFormatException e) {
                error++;
                    break;
            }
        }
        if(error>0){
            colorPalette.setBackground(Color.red); r=false;}
        else
            colorPalette.setBackground(Color.white);
        error=0;
        try{ colorBlockBg.setBackground(Color.white); Color.decode(colorBlockBg.getText());
        } catch (NumberFormatException e) {
                r=false; colorBlockBg.setBackground(Color.red);
        }
        try{ colorBlockBorder.setBackground(Color.white); Color.decode(colorBlockBorder.getText());
        } catch (NumberFormatException e) {
                r=false; colorBlockBorder.setBackground(Color.red);
        }
        try{ colorBlockText.setBackground(Color.white); Color.decode(colorBlockText.getText());
        } catch (NumberFormatException e) {
                r=false; colorBlockText.setBackground(Color.red);
        }
        try{ colorFlowBg.setBackground(Color.white); Color.decode(colorFlowBg.getText());
        } catch (NumberFormatException e) {
                r=false; colorFlowBg.setBackground(Color.red);
        }

        return r;
    }

    public void saveConfig() {
        {
            //if(global.applet) return ;
            FileWriter fw = null;
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = factory.newDocumentBuilder();
                Document doc = docBuilder.newDocument();
                Element root = doc.createElement("JavaBlockConfig");

                //save settings
                if(colorCheck()){
                    global.colorPalette=this.colorPalette.getText();
                    global.colors[0]=this.colorBlockBg.getText();
                    global.colors[1]=this.colorBlockBorder.getText();
                    global.colors[2]=this.colorBlockText.getText();
                    global.colors[3]=this.colorFlowBg.getText();
                    global.transparentPNG=this.colorTransparentPNG.isSelected();
                }
                
                // <editor-fold defaultstate="collapsed" desc="Color Tab">
                {
                    global.colorResolutionX=Integer.parseInt(colorHorPix.getValue().toString());
                    global.colorResolutionY=Integer.parseInt(colorVertPix.getValue().toString());
                    Element colorPalletex = doc.createElement("colorPalette");
                    colorPalletex.appendChild(doc.createTextNode(global.colorPalette));
                    root.appendChild(colorPalletex);

                    Element colorResolution = doc.createElement("colorPaletteResolution");
                    colorResolution.setAttribute("x", ""+global.colorResolutionX);
                    colorResolution.setAttribute("y", ""+global.colorResolutionY);
                    root.appendChild(colorResolution);
                    Element colors=doc.createElement("colors");
                        colors.setAttribute("blockBg", global.colors[0]);
                        colors.setAttribute("blockBorder", global.colors[1]);
                        colors.setAttribute("blockText", global.colors[2]);
                        colors.setAttribute("flowBg", global.colors[3]);
                        colors.setAttribute("transparentPNG", global.transparentPNG+"");
                    root.appendChild(colors);

                }

                // </editor-fold>

                // <editor-fold defaultstate="collapsed" desc="Pastebin">
                {
                    Element pastebin = doc.createElement("pastebin");
                    //pastebin.setAttribute("email", pastebinEmail.getText());
                    if(pastebinExpire.getSelectedItem()!=null)
                        pastebin.setAttribute("expire",pastebinExpire.getSelectedItem().toString());
                    if(pastebinPublic.getSelectedItem()!=null)
                        pastebin.setAttribute("exposure", pastebinPublic.getSelectedItem().toString());
                    pastebin.setAttribute("ask", labelPastebinShowThisDialog.isSelected()+"");
                    root.appendChild(pastebin);
                }// </editor-fold>

                // <editor-fold defaultstate="collapsed" desc="General">
                {
                    Element general = doc.createElement("general");
                    general.setAttribute("lastFlow", global.lastFlow);
                    general.setAttribute("checkUpdate", ""+global.checkUpdate);
                    //general.setAttribute("scriptSingleCall", "" + scriptSingle.isSelected());
                    general.setAttribute("prerenderingGraphic", "" + drawingPrerender.isSelected());
                    general.setAttribute("gradients", "" + drawingGradients.isSelected());
                    general.setAttribute("bezierCurves", "" + drawingBezier.isSelected());
                    general.setAttribute("useJLabels", "" + useJLabels.isSelected());
                    general.setAttribute("hwAccel", "" + hwAccel.isSelected());
                    
                    general.setAttribute("animations", "" + animations.isSelected());

                    general.setAttribute("bolderBorder", "" + exeBolderBorder.isSelected());
                    general.setAttribute("markChanges", "" + scriptMark.isSelected());
                    general.setAttribute("highlightLinks", "" + scriptHighlightAll.isSelected());
                    general.setAttribute("replaceCommands", "" + scriptReplace.isSelected());
                    general.setAttribute("makeJumps", "" + editingAutojumps.isSelected());
                    general.setAttribute("snapToGrid", "" + snapToGrid.isSelected());
                    general.setAttribute("highlight",
                            ((ComboText)this.highlighting.getSelectedItem()).getValue());
                    general.setAttribute("scriptEngine", engines.getSelectedItem().toString());
                    if(global.Window!=null)
                        global.WindowSize=global.Window.getBounds();
                    general.setAttribute("windowX", "" + global.WindowSize.x);
                    general.setAttribute("windowY", "" + global.WindowSize.y);
                    general.setAttribute("windowW", "" + global.WindowSize.width);
                    general.setAttribute("windowH", "" + global.WindowSize.height);

                    general.setAttribute("flowMarks", "" + flowMarks.isSelected());
                    general.setAttribute("LaF", LaF.getSelectedItem().toString());
                    root.appendChild(general);
                }// </editor-fold>

                // <editor-fold defaultstate="collapsed" desc="Window">
                {
                    Element wnd = doc.createElement("window");
                    wnd.setAttribute("windowX", "" + global.WindowSize.x);
                    wnd.setAttribute("windowY", "" + global.WindowSize.y);
                    wnd.setAttribute("windowW", "" + global.WindowSize.width);
                    wnd.setAttribute("windowH", "" + global.WindowSize.height);
                    root.appendChild(wnd);
                }// </editor-fold>

                // <editor-fold defaultstate="collapsed" desc="Startup">
                {
                    Element startup = doc.createElement("startup");
                    startup.setAttribute("showSplash", "" + startupSplash.isSelected());
                    startup.setAttribute("loadLast", "" + startupLoadLast.isSelected());
                    root.appendChild(startup);
                }// </editor-fold>

                //save file
                doc.appendChild(root);
                File f=new File(System.getProperty("user.home")+"/.JavaBlock");
                if(!f.exists())
                    f.mkdir();
                fw = new FileWriter(new File(configPath));
                fw.write(misc.DoctoString(doc));
                fw.close();

                loadConfig();
            } catch (IOException ex) { System.err.println("Save: IOException");
            } catch (ParserConfigurationException ex) { System.err.println("Save: ParserException");
            } catch (Exception ex) {
            }
        }
    }

    public void updateConfigGUI(){
        // <editor-fold defaultstate="collapsed" desc="Colors">
        colorPalette.setText(global.colorPalette);
        colorBlockBg.setText(global.colors[0]);
        colorBlockBorder.setText(global.colors[1]);
        colorBlockText.setText(global.colors[2]);
        colorFlowBg.setText(global.colors[3]);
        colorHorPix.setValue(global.colorResolutionX);
        colorVertPix.setValue(global.colorResolutionY);
        colorTransparentPNG.setSelected(global.transparentPNG);// </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="pastebin">
        pastebinEmail.setText(net.pastebin.email);
        if(net.pastebin.expire.length()>1)
            pastebinExpire.setSelectedItem(net.pastebin.expire);
        if(net.pastebin.exposure.length()>1)
            pastebinPublic.setSelectedItem(net.pastebin.exposure);
        labelPastebinShowThisDialog.setSelected(net.pastebin.askForName);// </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="General">
        //scriptSingle.setSelected(global.singleCall);
        checkUpdate.setSelected(global.checkUpdate);
        scriptHighlightAll.setSelected(global.highlightLinks);
        scriptReplace.setSelected(global.scriptReplace);
        drawingPrerender.setSelected(global.prerender);
        drawingGradients.setSelected(global.gradients);
        drawingBezier.setSelected(global.bezierCurves);
        useJLabels.setSelected(global.useJLabels);
        hwAccel.setSelected(global.accel);
        exeBolderBorder.setSelected(global.bolderBorder);
        
        flowMarks.setSelected(global.flowMarks);

        editingAutojumps.setSelected(global.autoJumps);
        snapToGrid.setSelected(global.snapToGrid);
        
        animations.setSelected(global.animations);

        scriptMark.setSelected(global.markChanges);
        LaF.setSelectedItem(global.LaF);
        engines.setSelectedItem(global.scriptEngine);

        for(int i=0; i<highlighting.getItemCount(); i++){
            ComboText c=(ComboText)highlighting.getItemAt(i);
            if(c.getValue().equals(global.hlight.toString())){
                highlighting.setSelectedItem(c);
                break;
            }
        }
        //highlighting.setSelectedItem(
        //        translator.config.getString("hlight."+global.hlight.toString())
        //        );
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Startup">
        startupSplash.setSelected(global.showSplash);
        startupLoadLast.setSelected(global.loadLast);// </editor-fold>
    }

    public void loadConfig(){
        {
            //if(global.applet) return ;
            try {
                BufferedReader reader = new BufferedReader(new FileReader(configPath));
                // <editor-fold defaultstate="collapsed" desc="File opening">
                global.reset();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder;
                String in = "";
                String line = reader.readLine();
                while (line != null) {
                    in += line += "\n";
                    line = reader.readLine();
                }
                docBuilder = factory.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(in));
                Document doc = docBuilder.parse(is);
                Element main=doc.getDocumentElement();
                // </editor-fold>
                NodeList set=null;
                // <editor-fold defaultstate="collapsed" desc="Colors">
                set = main.getElementsByTagName("colorPalette");
                if (set.getLength() != 0) {
                    Element c = (Element) set.item(0);
                    global.colorPalette = c.getTextContent();
                }

                set = main.getElementsByTagName("colorPaletteResolution");
                if (set.getLength() != 0) {
                    Element c = (Element) set.item(0);
                    global.colorResolutionX = Integer.parseInt(c.getAttribute("x"));
                    global.colorResolutionY = Integer.parseInt(c.getAttribute("y"));
                }
                set = main.getElementsByTagName("colors");
                if (set.getLength() != 0) {
                    Element c = (Element) set.item(0);
                    global.colors[0] = c.getAttribute("blockBg");
                    global.colors[1] = c.getAttribute("blockBorder");
                    global.colors[2] = c.getAttribute("blockText");
                    global.colors[3] = c.getAttribute("flowBg");
                    global.transparentPNG = c.getAttribute("transparentPNG").equals("true");
                }// </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="Pastebin">
                set = main.getElementsByTagName("pastebin");
                if (set.getLength() > 0) {
                    Element p = (Element) set.item(0);
                    net.pastebin.email = p.getAttribute("email");
                    net.pastebin.expire = p.getAttribute("expire");
                    net.pastebin.exposure = p.getAttribute("exposure");
                    net.pastebin.askForName = Boolean.parseBoolean(p.getAttribute("ask"));
                }// </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="General">
                set = main.getElementsByTagName("general");
                if (set.getLength() > 0) {
                    Element p = (Element) set.item(0);
                    if(p.hasAttribute("lastFlow"))
                        global.lastFlow=p.getAttribute("lastFlow");
                    global.checkUpdate=!p.getAttribute("checkUpdate").equals("false");
                    global.singleCall = !p.getAttribute("scriptSingleCall").equals("false");
                    global.prerender = !p.getAttribute("prerenderingGraphic").equals("false");
                    global.highlightLinks = p.getAttribute("highlightLinks").equals("true");
                    global.showToolbar = p.getAttribute("showToolbar").equals("true");
                    global.scriptReplace = !p.getAttribute("replaceCommands").equals("false");
                    global.gradients = !p.getAttribute("gradients").equals("false");
                    global.autoJumps = !p.getAttribute("makeJumps").equals("false");
                    //global.systemFont=p.getAttribute("TlwgMonoFont").equals("false");
                    global.markChanges=p.getAttribute("markChanges").equals("true");
                    global.bolderBorder=!p.getAttribute("bolderBorder").equals("false");
                    
                    global.animations=!p.getAttribute("animations").equals("false");

                    global.snapToGrid=!p.getAttribute("snapToGrid").equals("false");

                    if(p.hasAttribute("useJLabels"))
                        global.useJLabels=!p.getAttribute("useJLabels").equals("false");
                    if(p.hasAttribute("flowMarks"))
                        global.flowMarks=p.getAttribute("flowMarks").equals("true");
                    if(p.hasAttribute("scriptEngine"))
                        global.scriptEngine=p.getAttribute("scriptEngine");
                    if(p.hasAttribute("highlight"))
                        global.hlight=global.hlightT.valueOf(p.getAttribute("highlight"));
                    if(p.hasAttribute("bezierCurves"))
                        global.bezierCurves=p.getAttribute("bezierCurves").equals("true");
                    if(p.hasAttribute("hwAccel"))
                        global.accel=p.getAttribute("hwAccel").equals("true");
                    if(p.hasAttribute("LaF"))
                        global.LaF=p.getAttribute("LaF");
                }// </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="Window">
                set = main.getElementsByTagName("window");
                if (set.getLength() > 0) {
                    Element p = (Element) set.item(0);
                    global.WindowSize=new Rectangle(
                            Integer.parseInt(p.getAttribute("windowX")),
                            Integer.parseInt(p.getAttribute("windowY")),
                            Integer.parseInt(p.getAttribute("windowW")),
                            Integer.parseInt(p.getAttribute("windowH"))
                            );
                }// </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="Startup">
                set = main.getElementsByTagName("startup");
                if (set.getLength() > 0) {
                    Element p = (Element) set.item(0);
                    global.showSplash = p.getAttribute("showSplash").equals("true");
                    global.loadLast = p.getAttribute("loadLast").equals("true");
              }// </editor-fold>

                
            } catch (IOException ex) { System.err.println("Load: IOException");
            } catch (SAXException ex) {System.err.println("SAXException");
            } catch (ParserConfigurationException ex) { System.err.println("ParseException");
            }
        }
    }

    @Override
    public void show(){
        updateConfigGUI();
        super.show();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pastebinSummary = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        pastebinURL = new javax.swing.JTextField();
        pastebinID = new javax.swing.JTextField();
        pastebinApplet = new javax.swing.JTextField();
        tabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JScrollPane();
        general = new javax.swing.JPanel();
        scriptPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        engines = new javax.swing.JComboBox();
        scriptHighlightAll = new javax.swing.JCheckBox();
        scriptReplace = new javax.swing.JCheckBox();
        scriptMark = new javax.swing.JCheckBox();
        exeBolderBorder = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        startupSplash = new javax.swing.JCheckBox();
        startupLoadLast = new javax.swing.JCheckBox();
        checkUpdate = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        editingAutojumps = new javax.swing.JCheckBox();
        labelHighlighting = new javax.swing.JLabel();
        highlighting = new javax.swing.JComboBox();
        snapToGrid = new javax.swing.JCheckBox();
        colorsPane = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        labelColorBlockBackground = new javax.swing.JLabel();
        labelColorBlockBorderColor = new javax.swing.JLabel();
        labelColorBlockTextColor = new javax.swing.JLabel();
        labelColorFlowchartBackground = new javax.swing.JLabel();
        colorBlockBg = new javax.swing.JTextField();
        colorBlockBorder = new javax.swing.JTextField();
        colorBlockText = new javax.swing.JTextField();
        colorFlowBg = new javax.swing.JTextField();
        colorTransparentPNG = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        labelStaticPalette = new javax.swing.JLabel();
        labelColorHorPixel = new javax.swing.JLabel();
        labelVertPixel = new javax.swing.JLabel();
        colorHorPix = new javax.swing.JSpinner();
        colorVertPix = new javax.swing.JSpinner();
        jScrollPane1 = new javax.swing.JScrollPane();
        colorPalette = new javax.swing.JTextArea();
        drawingPanel = new javax.swing.JPanel();
        drawingPrerender = new javax.swing.JCheckBox();
        drawingGradients = new javax.swing.JCheckBox();
        drawingBezier = new javax.swing.JCheckBox();
        hwAccel = new javax.swing.JCheckBox();
        flowMarks = new javax.swing.JCheckBox();
        useJLabels = new javax.swing.JCheckBox();
        animations = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        LaF = new javax.swing.JComboBox();
        pastebinPane = new javax.swing.JPanel();
        pastebinConf = new javax.swing.JPanel();
        pastebinNameLabel = new javax.swing.JLabel();
        pastebinName = new javax.swing.JTextField();
        pastebinPublic = new javax.swing.JComboBox();
        labelPastebinExposure = new javax.swing.JLabel();
        labelPastebinExpiration = new javax.swing.JLabel();
        pastebinExpire = new javax.swing.JComboBox();
        pastebinEmail = new javax.swing.JTextField();
        labelPastebinEmail = new javax.swing.JLabel();
        labelPastebinShowThisDialog = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        syntaxDownload = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jythonDownload = new javax.swing.JButton();
        applyButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        defaultButton = new javax.swing.JButton();
        restartRequired = new javax.swing.JLabel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config/lang/settings"); // NOI18N
        jLabel2.setText(bundle.getString("configurator.jLabel2.text")); // NOI18N

        jLabel3.setText(bundle.getString("configurator.jLabel3.text")); // NOI18N

        jLabel4.setText(bundle.getString("configurator.jLabel4.text")); // NOI18N

        pastebinURL.setText(bundle.getString("configurator.pastebinURL.text")); // NOI18N

        pastebinID.setText(bundle.getString("configurator.pastebinID.text")); // NOI18N
        pastebinID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pastebinIDActionPerformed(evt);
            }
        });

        pastebinApplet.setText(bundle.getString("configurator.pastebinApplet.text")); // NOI18N

        javax.swing.GroupLayout pastebinSummaryLayout = new javax.swing.GroupLayout(pastebinSummary);
        pastebinSummary.setLayout(pastebinSummaryLayout);
        pastebinSummaryLayout.setHorizontalGroup(
            pastebinSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pastebinSummaryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pastebinSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pastebinSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pastebinID, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(pastebinURL, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(pastebinApplet, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))
                .addContainerGap())
        );
        pastebinSummaryLayout.setVerticalGroup(
            pastebinSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pastebinSummaryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pastebinSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(pastebinURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pastebinSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(pastebinID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pastebinSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(pastebinApplet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setTitle(bundle.getString("configurator.title")); // NOI18N

        tabs.setPreferredSize(new java.awt.Dimension(0, 0));

        general.setPreferredSize(new java.awt.Dimension(0, 0));

        scriptPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("configurator.scriptPanel.border.title"))); // NOI18N

        jLabel1.setText(bundle.getString("configurator.jLabel1.text")); // NOI18N

        scriptHighlightAll.setText(bundle.getString("configurator.scriptHighlightAll.text")); // NOI18N

        scriptReplace.setText(bundle.getString("configurator.scriptReplace.text")); // NOI18N

        scriptMark.setText(bundle.getString("configurator.scriptMark.text")); // NOI18N

        exeBolderBorder.setText(bundle.getString("configurator.exeBolderBorder.text")); // NOI18N

        javax.swing.GroupLayout scriptPanelLayout = new javax.swing.GroupLayout(scriptPanel);
        scriptPanel.setLayout(scriptPanelLayout);
        scriptPanelLayout.setHorizontalGroup(
            scriptPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scriptPanelLayout.createSequentialGroup()
                .addGroup(scriptPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(scriptPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(engines, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scriptReplace))
                    .addComponent(scriptHighlightAll)
                    .addComponent(scriptMark)
                    .addComponent(exeBolderBorder))
                .addContainerGap(304, Short.MAX_VALUE))
        );
        scriptPanelLayout.setVerticalGroup(
            scriptPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scriptPanelLayout.createSequentialGroup()
                .addGroup(scriptPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(engines, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scriptReplace))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scriptHighlightAll)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scriptMark)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(exeBolderBorder))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("configurator.jPanel6.border.title"))); // NOI18N

        startupSplash.setText(bundle.getString("configurator.startupSplash.text")); // NOI18N

        startupLoadLast.setText(bundle.getString("configurator.startupLoadLast.text")); // NOI18N
        startupLoadLast.setToolTipText(bundle.getString("configurator.startupLoadLast.toolTipText")); // NOI18N

        checkUpdate.setText(bundle.getString("configurator.checkUpdate.text")); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(startupSplash)
                    .addComponent(startupLoadLast)
                    .addComponent(checkUpdate))
                .addContainerGap(183, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(startupSplash)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(startupLoadLast)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(checkUpdate))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("configurator.jPanel5.border.title"))); // NOI18N

        editingAutojumps.setText(bundle.getString("configurator.editingAutojumps.text")); // NOI18N
        editingAutojumps.setToolTipText(bundle.getString("configurator.editingAutojumps.toolTipText")); // NOI18N

        labelHighlighting.setText(bundle.getString("configurator.labelHighlighting.text")); // NOI18N

        snapToGrid.setText(bundle.getString("configurator.snapToGrid.text")); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editingAutojumps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(labelHighlighting)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(highlighting, 0, 176, Short.MAX_VALUE))
                    .addComponent(snapToGrid))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(editingAutojumps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(snapToGrid)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelHighlighting)
                    .addComponent(highlighting, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout generalLayout = new javax.swing.GroupLayout(general);
        general.setLayout(generalLayout);
        generalLayout.setHorizontalGroup(
            generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scriptPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(generalLayout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        generalLayout.setVerticalGroup(
            generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scriptPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(128, Short.MAX_VALUE))
        );

        jPanel1.setViewportView(general);

        tabs.addTab(bundle.getString("configurator.jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        colorsPane.setPreferredSize(new java.awt.Dimension(0, 0));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("configurator.defaultColors.text:"))); // NOI18N

        labelColorBlockBackground.setText(bundle.getString("configurator.labelColorBlockBackground.text")); // NOI18N

        labelColorBlockBorderColor.setText(bundle.getString("configurator.labelColorBlockBorderColor.text")); // NOI18N

        labelColorBlockTextColor.setText(bundle.getString("configurator.labelColorBlockTextColor.text")); // NOI18N

        labelColorFlowchartBackground.setText(bundle.getString("configurator.labelColorFlowchartBackground.text")); // NOI18N

        colorTransparentPNG.setText(bundle.getString("configurator.colorTransparentPNG.text")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelColorBlockBackground)
                            .addComponent(labelColorBlockBorderColor)
                            .addComponent(labelColorFlowchartBackground)
                            .addComponent(labelColorBlockTextColor))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(colorBlockBg, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                            .addComponent(colorFlowBg, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                            .addComponent(colorBlockText, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                            .addComponent(colorBlockBorder, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)))
                    .addComponent(colorTransparentPNG, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelColorBlockBackground)
                    .addComponent(colorBlockBg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelColorBlockBorderColor)
                    .addComponent(colorBlockBorder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelColorBlockTextColor)
                    .addComponent(colorBlockText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelColorFlowchartBackground)
                    .addComponent(colorFlowBg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(colorTransparentPNG)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("configurator.jPanel3.border.title"))); // NOI18N

        labelStaticPalette.setText(bundle.getString("configurator.labelStaticPalette.text")); // NOI18N

        labelColorHorPixel.setText(bundle.getString("configurator.labelColorHorPixel.text")); // NOI18N

        labelVertPixel.setText(bundle.getString("configurator.labelVertPixel.text")); // NOI18N

        colorHorPix.setModel(new javax.swing.SpinnerNumberModel(2, 1, 30, 1));

        colorVertPix.setModel(new javax.swing.SpinnerNumberModel(2, 1, 30, 1));

        colorPalette.setColumns(5);
        colorPalette.setRows(6);
        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("config/lang/descriptions"); // NOI18N
        colorPalette.setToolTipText(bundle1.getString("config.palette")); // NOI18N
        jScrollPane1.setViewportView(colorPalette);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelColorHorPixel)
                    .addComponent(labelStaticPalette)
                    .addComponent(labelVertPixel))
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(colorHorPix)
                    .addComponent(colorVertPix, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(labelStaticPalette))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(colorHorPix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelColorHorPixel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(colorVertPix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelVertPixel)))
        );

        drawingPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("configurator.drawingPanel.border.title"))); // NOI18N

        drawingPrerender.setText(bundle.getString("configurator.drawingPrerender.text")); // NOI18N
        drawingPrerender.setToolTipText(bundle1.getString("config.prerendered")); // NOI18N

        drawingGradients.setText(bundle.getString("configurator.drawingGradients.text")); // NOI18N

        drawingBezier.setText(bundle.getString("configurator.drawingBezier.text")); // NOI18N
        drawingBezier.setToolTipText(bundle.getString("configurator.drawingBezier.toolTipText")); // NOI18N

        hwAccel.setText(bundle.getString("configurator.hwAccel.text")); // NOI18N
        hwAccel.setToolTipText(bundle.getString("configurator.hwAccel.toolTipText")); // NOI18N

        flowMarks.setText(bundle.getString("configurator.flowMarks.text")); // NOI18N
        flowMarks.setToolTipText(bundle.getString("configurator.flowMarks.toolTipText")); // NOI18N

        useJLabels.setText(bundle.getString("configurator.useJLabels.text")); // NOI18N

        animations.setText(bundle.getString("configurator.animations.text")); // NOI18N
        animations.setToolTipText(bundle.getString("configurator.animations.toolTipText")); // NOI18N

        javax.swing.GroupLayout drawingPanelLayout = new javax.swing.GroupLayout(drawingPanel);
        drawingPanel.setLayout(drawingPanelLayout);
        drawingPanelLayout.setHorizontalGroup(
            drawingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(drawingPanelLayout.createSequentialGroup()
                .addGroup(drawingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(drawingPrerender)
                    .addComponent(drawingGradients)
                    .addGroup(drawingPanelLayout.createSequentialGroup()
                        .addComponent(drawingBezier)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(animations))
                    .addComponent(hwAccel)
                    .addGroup(drawingPanelLayout.createSequentialGroup()
                        .addComponent(flowMarks)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(useJLabels)))
                .addContainerGap(75, Short.MAX_VALUE))
        );
        drawingPanelLayout.setVerticalGroup(
            drawingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(drawingPanelLayout.createSequentialGroup()
                .addComponent(drawingPrerender)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(drawingGradients)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(drawingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(drawingBezier)
                    .addComponent(animations))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hwAccel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(drawingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(flowMarks)
                    .addComponent(useJLabels))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("configurator.jPanel4.border.title"))); // NOI18N

        jLabel5.setText(bundle.getString("configurator.jLabel5.text")); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LaF, 0, 164, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(LaF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout colorsPaneLayout = new javax.swing.GroupLayout(colorsPane);
        colorsPane.setLayout(colorsPaneLayout);
        colorsPaneLayout.setHorizontalGroup(
            colorsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(colorsPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(colorsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(drawingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(colorsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        colorsPaneLayout.setVerticalGroup(
            colorsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(colorsPaneLayout.createSequentialGroup()
                .addGroup(colorsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(colorsPaneLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(drawingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(colorsPaneLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(87, Short.MAX_VALUE))
        );

        tabs.addTab(bundle.getString("configurator.colorsPane.TabConstraints.tabTitle"), colorsPane); // NOI18N

        pastebinNameLabel.setText(bundle.getString("configurator.pastebinNameLabel.text")); // NOI18N

        pastebinName.setText(bundle.getString("configurator.pastebinName.text")); // NOI18N

        pastebinPublic.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Public", "Private" }));
        pastebinPublic.setSelectedIndex(1);

        labelPastebinExposure.setText(bundle.getString("configurator.labelPastebinExposure.text")); // NOI18N

        labelPastebinExpiration.setText(bundle.getString("configurator.labelPastebinExpiration.text")); // NOI18N

        pastebinExpire.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "N Never", "10M 10 Minutes", "1H 1 Hour", "1D 1 Day", "1M 1 Month" }));
        pastebinExpire.setSelectedIndex(4);
        pastebinExpire.setSelectedItem(pastebinExpire);

        labelPastebinEmail.setText(bundle.getString("configurator.labelPastebinEmail.text")); // NOI18N

        labelPastebinShowThisDialog.setSelected(true);
        labelPastebinShowThisDialog.setText(bundle.getString("configurator.labelPastebinShowThisDialog.text")); // NOI18N

        javax.swing.GroupLayout pastebinConfLayout = new javax.swing.GroupLayout(pastebinConf);
        pastebinConf.setLayout(pastebinConfLayout);
        pastebinConfLayout.setHorizontalGroup(
            pastebinConfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pastebinConfLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pastebinConfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelPastebinShowThisDialog)
                    .addGroup(pastebinConfLayout.createSequentialGroup()
                        .addGroup(pastebinConfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelPastebinExposure)
                            .addComponent(labelPastebinExpiration)
                            .addComponent(labelPastebinEmail)
                            .addComponent(pastebinNameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pastebinConfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pastebinName)
                            .addComponent(pastebinEmail)
                            .addComponent(pastebinPublic, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pastebinExpire, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pastebinConfLayout.setVerticalGroup(
            pastebinConfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pastebinConfLayout.createSequentialGroup()
                .addGroup(pastebinConfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPastebinEmail)
                    .addComponent(pastebinEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pastebinConfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPastebinExpiration)
                    .addComponent(pastebinExpire, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pastebinConfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPastebinExposure)
                    .addComponent(pastebinPublic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pastebinConfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pastebinNameLabel)
                    .addComponent(pastebinName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelPastebinShowThisDialog))
        );

        javax.swing.GroupLayout pastebinPaneLayout = new javax.swing.GroupLayout(pastebinPane);
        pastebinPane.setLayout(pastebinPaneLayout);
        pastebinPaneLayout.setHorizontalGroup(
            pastebinPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pastebinPaneLayout.createSequentialGroup()
                .addComponent(pastebinConf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(435, Short.MAX_VALUE))
        );
        pastebinPaneLayout.setVerticalGroup(
            pastebinPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pastebinPaneLayout.createSequentialGroup()
                .addComponent(pastebinConf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(264, Short.MAX_VALUE))
        );

        tabs.addTab(bundle.getString("configurator.pastebinPane.TabConstraints.tabTitle"), pastebinPane); // NOI18N

        syntaxDownload.setText(bundle.getString("configurator.download")); // NOI18N
        syntaxDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                syntaxDownloadActionPerformed(evt);
            }
        });

        jLabel6.setText(bundle.getString("configurator.jLabel6.text")); // NOI18N

        jLabel7.setText(bundle.getString("configurator.jLabel7.text")); // NOI18N

        jythonDownload.setText(bundle.getString("configurator.jythonDownload.text")); // NOI18N
        jythonDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jythonDownloadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(syntaxDownload))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jythonDownload)))
                .addContainerGap(353, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(syntaxDownload))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jythonDownload)
                    .addComponent(jLabel7))
                .addContainerGap(325, Short.MAX_VALUE))
        );

        tabs.addTab(bundle.getString("configurator.modules"), jPanel7); // NOI18N

        applyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/24/dialog-apply.png"))); // NOI18N
        applyButton.setText(bundle.getString("configurator.applyButton.text")); // NOI18N
        applyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyButtonActionPerformed(evt);
            }
        });

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/24/gtk-no.png"))); // NOI18N
        cancelButton.setText(bundle.getString("configurator.cancelButton.text")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        okButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/24/dialog-ok.png"))); // NOI18N
        okButton.setText(bundle.getString("configurator.okButton.text")); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("DejaVu Sans", 0, 18));
        jLabel8.setText(bundle.getString("configurator.jLabel8.text")); // NOI18N

        defaultButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/24/go-home.png"))); // NOI18N
        defaultButton.setText(bundle.getString("configurator.defaultButton.text")); // NOI18N
        defaultButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defaultButtonActionPerformed(evt);
            }
        });

        restartRequired.setText(bundle.getString("configurator.restartRequired.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(restartRequired)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                        .addComponent(defaultButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(okButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(applyButton)))
                .addContainerGap())
            .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 726, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(applyButton)
                        .addComponent(cancelButton)
                        .addComponent(okButton)
                        .addComponent(defaultButton))
                    .addComponent(restartRequired))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        saveConfig();
        hide();
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        hide();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void applyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyButtonActionPerformed
        saveConfig();
    }//GEN-LAST:event_applyButtonActionPerformed

    private void defaultButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defaultButtonActionPerformed
        File f=new File(System.getProperty("user.home")+"/.JavaBlock/config.jbc");
        if(f.exists())
            f.delete();
        global.Default();
        loadConfig();
        this.updateConfigGUI();
    }//GEN-LAST:event_defaultButtonActionPerformed

    private void pastebinIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pastebinIDActionPerformed

    }//GEN-LAST:event_pastebinIDActionPerformed

    private void syntaxDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_syntaxDownloadActionPerformed
        File dir=new File(System.getProperty("user.home") +
                        "/.JavaBlock/modules/");
        dir.mkdir();
        new Thread(){
            @Override
            public void run() {
                FileDownloader url = new FileDownloader(
                        "http://javablock.sourceforge.net/JavaBlock/modules/jsyntaxpane.jar",
                        "jsyntaxpane",
                        System.getProperty("user.home")
                        + "/.JavaBlock/modules/jsyntaxpane.jar");
                if (url.get()) {
                    JOptionPane.showMessageDialog(null,
                            "Module Syntax Highlighter has been successfully installed!\n\n"
                            + "You must restart JavaBlock to take effect",
                             "Installed!",
                            JOptionPane.INFORMATION_MESSAGE);
                    Syntax.init();
                }
            }
        }.start();
    }//GEN-LAST:event_syntaxDownloadActionPerformed

    public static void installJython(){
        File dir=new File(System.getProperty("user.home") +
                        "/.JavaBlock/modules/");
        dir.mkdir();
        new Thread(){
            @Override
            public void run() {
                FileDownloader jar = new FileDownloader(
                        "http://javablock.sourceforge.net/JavaBlock/modules/jython.jar",
                        "Jython",
                        System.getProperty("user.home")
                        + "/.JavaBlock/modules/jython.jar");
                FileDownloader lib = new FileDownloader(
                        "http://javablock.sourceforge.net/JavaBlock/modules/Lib",
                        "Jython Libraries",
                        System.getProperty("user.home")
                        + "/.JavaBlock/modules/Lib");
                boolean ok=jar.get() && lib.get();
                if (ok) {
                    JOptionPane.showMessageDialog(null,
                            "Jython has been successfully installed!\n\n"
                            + "You must restart JavaBlock to take effect",
                             "Installed!",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }.start();
    }
    private void jythonDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jythonDownloadActionPerformed
        installJython();
    }//GEN-LAST:event_jythonDownloadActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox LaF;
    private javax.swing.JCheckBox animations;
    private javax.swing.JButton applyButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JCheckBox checkUpdate;
    private javax.swing.JTextField colorBlockBg;
    private javax.swing.JTextField colorBlockBorder;
    private javax.swing.JTextField colorBlockText;
    private javax.swing.JTextField colorFlowBg;
    private javax.swing.JSpinner colorHorPix;
    private javax.swing.JTextArea colorPalette;
    private javax.swing.JCheckBox colorTransparentPNG;
    private javax.swing.JSpinner colorVertPix;
    private javax.swing.JPanel colorsPane;
    private javax.swing.JButton defaultButton;
    private javax.swing.JCheckBox drawingBezier;
    private javax.swing.JCheckBox drawingGradients;
    private javax.swing.JPanel drawingPanel;
    private javax.swing.JCheckBox drawingPrerender;
    private javax.swing.JCheckBox editingAutojumps;
    private javax.swing.JComboBox engines;
    private javax.swing.JCheckBox exeBolderBorder;
    private javax.swing.JCheckBox flowMarks;
    private javax.swing.JPanel general;
    private javax.swing.JComboBox highlighting;
    private javax.swing.JCheckBox hwAccel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jythonDownload;
    private javax.swing.JLabel labelColorBlockBackground;
    private javax.swing.JLabel labelColorBlockBorderColor;
    private javax.swing.JLabel labelColorBlockTextColor;
    private javax.swing.JLabel labelColorFlowchartBackground;
    private javax.swing.JLabel labelColorHorPixel;
    private javax.swing.JLabel labelHighlighting;
    private javax.swing.JLabel labelPastebinEmail;
    private javax.swing.JLabel labelPastebinExpiration;
    private javax.swing.JLabel labelPastebinExposure;
    private javax.swing.JCheckBox labelPastebinShowThisDialog;
    private javax.swing.JLabel labelStaticPalette;
    private javax.swing.JLabel labelVertPixel;
    private javax.swing.JButton okButton;
    public javax.swing.JTextField pastebinApplet;
    public javax.swing.JPanel pastebinConf;
    public javax.swing.JTextField pastebinEmail;
    public javax.swing.JComboBox pastebinExpire;
    public javax.swing.JTextField pastebinID;
    public javax.swing.JTextField pastebinName;
    private javax.swing.JLabel pastebinNameLabel;
    public javax.swing.JPanel pastebinPane;
    public javax.swing.JComboBox pastebinPublic;
    public javax.swing.JPanel pastebinSummary;
    public javax.swing.JTextField pastebinURL;
    private javax.swing.JLabel restartRequired;
    private javax.swing.JCheckBox scriptHighlightAll;
    private javax.swing.JCheckBox scriptMark;
    private javax.swing.JPanel scriptPanel;
    private javax.swing.JCheckBox scriptReplace;
    private javax.swing.JCheckBox snapToGrid;
    private javax.swing.JCheckBox startupLoadLast;
    private javax.swing.JCheckBox startupSplash;
    private javax.swing.JButton syntaxDownload;
    public javax.swing.JTabbedPane tabs;
    private javax.swing.JCheckBox useJLabels;
    // End of variables declaration//GEN-END:variables

}
