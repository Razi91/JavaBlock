package javablock.flowchart.blockEditors;
import config.global;
import config.translator;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Array;
import javablock.flowchart.blocks.addons.canvas2dBlock;
import javablock.flowchart.blocks.addons.logoBlock;
import javablock.flowchart.*;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.text.EditorKit;
import widgets.ComboText;

public class canvas2dEditor extends javax.swing.JPanel implements BlockEditor {
    ComboText commands[]={
        new ComboText(translator.addons.getString("canvas2d.create"), "MAKE"),
        new ComboText(translator.addons.getString("canvas2d.drawPixel"), "drawPixel"),
        new ComboText(translator.addons.getString("canvas2d.drawLine"), "drawLine"),
        new ComboText(translator.addons.getString("canvas2d.lineFrom"), "lineFrom"),
        new ComboText(translator.addons.getString("canvas2d.lineTo"), "lineTo"),
        new ComboText(translator.addons.getString("canvas2d.setColor"), "setColor"),
        new ComboText(translator.addons.getString("canvas2d.drawMap"), "drawMap"),
        new ComboText(translator.addons.getString("canvas2d.update"), "update"),
    };
    public canvas2dBlock editing=null;
    public canvas2dEditor() {
        initComponents();
        for(ComboText c:commands){
            command.addItem(c);
        }
        tipBox.setVisible(false);
        if(addons.Syntax.loaded)
            map.setEditorKit((EditorKit) addons.Syntax.js);
    }


    private String makeCode(){
        String c=((ComboText)command.getSelectedItem()).getValue();
        if(c.equals("MAKE")){
            String code=objName.getText()+"=addons.canvas2d("+makeW.getValue()+","+makeH.getValue()+");\n";
            code+=objName.getText()+".autoUpdate="+makeUpdate.isSelected()+"\n";
            code+=objName.getText()+".setAA("+makeAA.isSelected()+")";
            return code;
            
        }
        else if(c.equals("drawMap")){
            String code=objName.getText()+".drawMap(\"";
            String m[]=map.getText().split("\n"); int i=0;
            for(String line:m){
                code+=line;
                if(i++ < m.length)code+="\\n";
            }
            code+="\")";
            return code;
        }
        else if(c.equals("drawPixel")||c.equals("lineFrom")||c.equals("lineTo")){
            return objName.getText()+"."+c+"("+
                    "("+drawX.getText()+"),"
                    +"("+drawY.getText()+"))";
        }
        else if(c.equals("drawLine")){
            return objName.getText()+"."+c+"("+
                    "("+lineFromX.getText()+"),"+
                    "("+lineFromY.getText()+"),"+
                    "("+lineToX.getText()+"),"+
                    "("+lineToY.getText()+")"+
                    ")";
        }
        else if(c.equals("setColor")){
            return objName.getText()+"."+c+"(\""+colorHEX.getText()+"\")";
        }
        return objName.getText()+"."+c+"();";
    }
    private String makeComment(){
        String c=((ComboText)command.getSelectedItem()).getValue();
        String t=((ComboText)command.getSelectedItem()).getText();
        String obj=objName.getText();
        if(c.equals("MAKE")){
            return t+" "+obj;
        }
        if(obj.equals("canvas2d")) obj="";
        else obj+=":\n";
        if(c.equals("drawPixel")||c.equals("lineFrom")||c.equals("lineTo")){
            return obj+t+" "+drawX.getText()+", "+drawY.getText()+"";
        }
        else if(c.equals("drawMap")){
            return obj+t;
        }
        if(c.equals("drawLine")){
            return obj+t+" "+
                    "("+lineFromX.getText()+";"+
                    lineFromY.getText()+");"+
                    "("+lineToX.getText()+";"+
                    ""+lineToY.getText()+")";
        }
        if(c.equals("setColor")){
            return obj+t+"";
        }
        return "";
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pointPane = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        drawX = new javax.swing.JTextField();
        drawY = new javax.swing.JTextField();
        create = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        makeUpdate = new javax.swing.JCheckBox();
        makeAA = new javax.swing.JCheckBox();
        makeW = new javax.swing.JSpinner();
        makeH = new javax.swing.JSpinner();
        setColor = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        colorHEX = new javax.swing.JTextField();
        pickColor = new javax.swing.JButton();
        drawLine = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        lineToX = new javax.swing.JTextField();
        lineToY = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lineFromX = new javax.swing.JTextField();
        lineFromY = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        mapPane = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        map = new javax.swing.JEditorPane();
        jLabel1 = new javax.swing.JLabel();
        objName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        command = new javax.swing.JComboBox();
        editor = new javax.swing.JPanel();
        tipBox = new javax.swing.JPanel();
        tips = new javax.swing.JLabel();

        jLabel3.setText("x:");

        jLabel4.setText("y:");

        drawX.setText("jTextField1");

        drawY.setText("jTextField2");

        javax.swing.GroupLayout pointPaneLayout = new javax.swing.GroupLayout(pointPane);
        pointPane.setLayout(pointPaneLayout);
        pointPaneLayout.setHorizontalGroup(
            pointPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pointPaneLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(pointPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pointPaneLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(drawY, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                    .addGroup(pointPaneLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(drawX, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pointPaneLayout.setVerticalGroup(
            pointPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pointPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pointPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(drawX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pointPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(drawY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config/lang/addons"); // NOI18N
        jLabel5.setText(bundle.getString("gui.makeWidth")); // NOI18N

        jLabel6.setText(bundle.getString("gui.makeHeight")); // NOI18N

        makeUpdate.setText(bundle.getString("gui.autoUpdate")); // NOI18N

        makeAA.setText(bundle.getString("gui.antiAliasing")); // NOI18N

        javax.swing.GroupLayout createLayout = new javax.swing.GroupLayout(create);
        create.setLayout(createLayout);
        createLayout.setHorizontalGroup(
            createLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(createLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(createLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(createLayout.createSequentialGroup()
                        .addGroup(createLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(createLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(makeH, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                            .addComponent(makeW, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)))
                    .addComponent(makeUpdate)
                    .addComponent(makeAA))
                .addContainerGap())
        );
        createLayout.setVerticalGroup(
            createLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(createLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(createLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(makeW, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(createLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(makeH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(makeUpdate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(makeAA)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel7.setText("HEX:");

        colorHEX.setText("jTextField1");

        pickColor.setText("Pick color");
        pickColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pickColorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout setColorLayout = new javax.swing.GroupLayout(setColor);
        setColor.setLayout(setColorLayout);
        setColorLayout.setHorizontalGroup(
            setColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(setColorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(setColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pickColor, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                    .addGroup(setColorLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(colorHEX, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)))
                .addContainerGap())
        );
        setColorLayout.setVerticalGroup(
            setColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(setColorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(setColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(colorHEX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pickColor)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel10.setText("x:");

        jLabel11.setText("y:");

        jLabel9.setText("y:");

        jLabel8.setText("x:");

        jLabel12.setFont(new java.awt.Font("TeXGyreBonum", 1, 13)); // NOI18N
        jLabel12.setText("From:");

        jLabel13.setFont(new java.awt.Font("TeXGyreBonum", 1, 13)); // NOI18N
        jLabel13.setText("To:");

        javax.swing.GroupLayout drawLineLayout = new javax.swing.GroupLayout(drawLine);
        drawLine.setLayout(drawLineLayout);
        drawLineLayout.setHorizontalGroup(
            drawLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(drawLineLayout.createSequentialGroup()
                .addGroup(drawLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(drawLineLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(drawLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(drawLineLayout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lineToX, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE))
                            .addGroup(drawLineLayout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lineToY, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE))
                            .addGroup(drawLineLayout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lineFromX, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE))
                            .addGroup(drawLineLayout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lineFromY, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE))))
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addContainerGap())
        );
        drawLineLayout.setVerticalGroup(
            drawLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(drawLineLayout.createSequentialGroup()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(drawLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(lineFromX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(drawLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(lineFromY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addGap(4, 4, 4)
                .addGroup(drawLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(lineToX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(drawLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(lineToY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(map);

        javax.swing.GroupLayout mapPaneLayout = new javax.swing.GroupLayout(mapPane);
        mapPane.setLayout(mapPaneLayout);
        mapPaneLayout.setHorizontalGroup(
            mapPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
        );
        mapPaneLayout.setVerticalGroup(
            mapPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
        );

        setBorder(javax.swing.BorderFactory.createTitledBorder("Canvas2D editor"));

        jLabel1.setText(bundle.getString("gui.object")); // NOI18N

        objName.setText("canvas2d");

        jLabel2.setText(bundle.getString("gui.command")); // NOI18N

        command.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                commandItemStateChanged(evt);
            }
        });

        editor.setLayout(new java.awt.BorderLayout());

        tipBox.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("gui.tooltip"))); // NOI18N

        tips.setText(bundle.getString("gui.tip")); // NOI18N

        javax.swing.GroupLayout tipBoxLayout = new javax.swing.GroupLayout(tipBox);
        tipBox.setLayout(tipBoxLayout);
        tipBoxLayout.setHorizontalGroup(
            tipBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tips, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
        );
        tipBoxLayout.setVerticalGroup(
            tipBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tips, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(command, 0, 145, Short.MAX_VALUE)
                    .addComponent(objName, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(editor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
            .addComponent(tipBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(objName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(command, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tipBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(editor, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void pickColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pickColorActionPerformed
        Color c=JColorChooser.showDialog( this,
                     "Choose a color", Color.decode(colorHEX.getText()) );
        colorHEX.setText( "0x"+Integer.toHexString(
                c.getRed()*256*256+
                c.getGreen()*256+
                c.getBlue()
                ).substring(0, 6) );
    }//GEN-LAST:event_pickColorActionPerformed

    private void commandItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_commandItemStateChanged
        String c=((ComboText)command.getSelectedItem()).getValue();
        editor.removeAll();
        if(c.equals("MAKE")){
            editor.add(this.create);
        }
        if(c.equals("drawMap")){
            editor.add(this.mapPane);
        }
        if(c.equals("drawPixel")||c.equals("lineFrom")||c.equals("lineTo")){
            editor.add(this.pointPane);
        }
        if(c.equals("drawLine")){
            editor.add(this.drawLine);
        }
        if(c.equals("setColor")){
            editor.add(this.setColor);
        }
        if(global.showToolTips){
            try{
                String tip=translator.addons.getString("canvas2d."+c+".help");
                tips.setText(tip);
                tipBox.setVisible(true);
            }catch(Exception e){
                tipBox.setVisible(false);
            }
        }
        editor.validate();
        repaint();
    }//GEN-LAST:event_commandItemStateChanged

    private void reset(){
        makeW.setValue(500);makeH.setValue(500);
        makeAA.setSelected(false);makeUpdate.setSelected(true);
        colorHEX.setText("0x000000");
        drawX.setText("0");drawY.setText("0");
        lineFromX.setText("0");lineFromY.setText("0");
        lineToX.setText("0");lineToY.setText("0");
        map.setText("");
    }

    public void setEditedBlock(JBlock b) {
        if(b==editing) return ;
        editing=(canvas2dBlock)b;
        reset();
        for(ComboText c: commands){
            if(c.getValue().equals(editing.command)){
                command.setSelectedItem(c);
                break;
            }
        }
        String c=((ComboText)command.getSelectedItem()).getValue();
        objName.setText(editing.obj);

        if(c.equals("MAKE")){
            makeW.setValue(editing.data[0]);
            makeH.setValue(editing.data[1]);
            makeUpdate.setSelected(editing.data[2].toString().equals("true"));
            makeAA.setSelected(editing.data[3].toString().equals("true"));
        }
        if(c.equals("drawMap")){
            map.setText(editing.data[0].toString());
        }
        if(c.equals("setColor")){
            colorHEX.setText(editing.data[0].toString());
        }
        if(c.equals("drawPixel") ||c.equals("lineFrom")||c.equals("lineTo")){
            drawX.setText(editing.data[0].toString());
            drawY.setText(editing.data[1].toString());
        }
        if(c.equals("drawLine")){
            lineFromX.setText(editing.data[0].toString());
            lineFromY.setText(editing.data[1].toString());
            lineToX.setText(editing.data[2].toString());
            lineToY.setText(editing.data[3].toString());
        }

        objName.setText(editing.obj);
    }

    public void finnishEdit() {
        saveBlock();
        editing=null;
    }

    public boolean changes() {
        return true;
    }

    public void saveBlock() {
        colorHEX.requestFocus();
        editing.obj=objName.getText();
        String c=((ComboText)command.getSelectedItem()).getValue();
        editing.command=c;
        if(c.equals("MAKE")){
            editing.data=new Object[4];
            editing.data[0]=makeW.getValue();
            editing.data[1]=makeH.getValue();
            editing.data[2]=makeUpdate.isSelected();
            editing.data[3]=makeAA.isSelected();
        }
        if(c.equals("setColor")){
            editing.data=new Object[1];
            editing.data[0]=colorHEX.getText();
        }
        if(c.equals("drawMap")){
            editing.data=new Object[1];
            editing.data[0]=map.getText();
        }
        if(c.equals("drawPixel") ||c.equals("lineFrom")||c.equals("lineTo")){
            editing.data=new Object[2];
            editing.data[0]=drawX.getText();
            editing.data[1]=drawY.getText();
        }
        if(c.equals("drawLine")){
            editing.data=new Object[4];
            editing.data[0]=lineFromX.getText();
            editing.data[1]=lineFromY.getText();
            editing.data[2]=lineToX.getText();
            editing.data[3]=lineToY.getText();
        }
        editing.setCode(makeCode());
        editing.setComment(makeComment());
        editing.shape();
        editing.requestRepaint();
    }

    public JBlock getBlock(){
        return editing;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField colorHEX;
    private javax.swing.JComboBox command;
    private javax.swing.JPanel create;
    private javax.swing.JPanel drawLine;
    private javax.swing.JTextField drawX;
    private javax.swing.JTextField drawY;
    private javax.swing.JPanel editor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField lineFromX;
    private javax.swing.JTextField lineFromY;
    private javax.swing.JTextField lineToX;
    private javax.swing.JTextField lineToY;
    private javax.swing.JCheckBox makeAA;
    private javax.swing.JSpinner makeH;
    private javax.swing.JCheckBox makeUpdate;
    private javax.swing.JSpinner makeW;
    private javax.swing.JEditorPane map;
    private javax.swing.JPanel mapPane;
    private javax.swing.JTextField objName;
    private javax.swing.JButton pickColor;
    private javax.swing.JPanel pointPane;
    private javax.swing.JPanel setColor;
    private javax.swing.JPanel tipBox;
    private javax.swing.JLabel tips;
    // End of variables declaration//GEN-END:variables

}
