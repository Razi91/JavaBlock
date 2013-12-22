package javablock.flowchart.blockEditors;
import config.global;
import config.translator;
import java.awt.Color;
import java.lang.reflect.Array;
import javablock.flowchart.blocks.addons.logoBlock;
import javablock.flowchart.*;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import widgets.ComboText;

public class logoEditor extends javax.swing.JPanel implements BlockEditor {
    ComboText commands[]={
        new ComboText(translator.addons.getString("logo.create.icon"),
            translator.addons.getString("logo.create"), "MAKE"),
        new ComboText(translator.addons.getString("logo.forward.icon"),
            translator.addons.getString("logo.forward"), "forward"),
        new ComboText(translator.addons.getString("logo.backward.icon"),
            translator.addons.getString("logo.backward"), "backward"),
        new ComboText(translator.addons.getString("logo.turnLeft.icon"),
            translator.addons.getString("logo.turnLeft"),  "turnLeft"),
        new ComboText(translator.addons.getString("logo.turnRight.icon"),
            translator.addons.getString("logo.turnRight"), "turnRight"),
        new ComboText(translator.addons.getString("logo.pickPen.icon"),
            translator.addons.getString("logo.pickPen"), "pickPen"),
        new ComboText(translator.addons.getString("logo.dropPen.icon"),
            translator.addons.getString("logo.dropPen"), "dropPen"),
        new ComboText(translator.addons.getString("logo.hideTurtle.icon"),
            translator.addons.getString("logo.hideTurtle"), "hideTurtle"),
        new ComboText(translator.addons.getString("logo.showTurtle.icon"),
            translator.addons.getString("logo.showTurtle"), "showTurtle"),
        new ComboText(translator.addons.getString("logo.setColor.icon"),
            translator.addons.getString("logo.setColor"), "setColor"),
        new ComboText(translator.addons.getString("logo.fill.icon"),
            translator.addons.getString("logo.fill"), "fill"),
        new ComboText(translator.addons.getString("logo.distance.icon"),
            translator.addons.getString("logo.distance"), "distance")
    };
    public logoBlock editing=null;
    public logoEditor() {
        initComponents();
        for(ComboText c:commands){
            command.addItem(c);
        }
        tipBox.setVisible(false);
    }

    private String makeCode(){
        String c=((ComboText)command.getSelectedItem()).getValue();
        if(c.equals("MAKE")){
            return logoObj.getText()+"=addons.logo("+makeW.getValue()+","+makeH.getValue()+"); "
                    + logoObj.getText()+".setClosedCanvas("+closedCanvas.isSelected()+");";
        }
        else if(c.equals("forward") || c.equals("backward")
                || c.equals("turnRight") || c.equals("turnLeft")
                || c.equals("distance")){
            return logoObj.getText()+"."+c+"("+value.getText()+")";
        }
        else if(c.equals("setColor")){
            return logoObj.getText()+"."+c+"(\""+colorHEX.getText()+"\")";
        }
        else if(c.equals("distance")){
            return variable.getText()+"="+logoObj.getText()+"."+c+"(("+value.getText()+"))";
        }
        else
            return logoObj.getText()+"."+c+"()";
    }
    private String makeComment(){
        String c=((ComboText)command.getSelectedItem()).getValue();
        String t=((ComboText)command.getSelectedItem()).getText();
        String ico=((ComboText)command.getSelectedItem()).getIcon();
        String obj=logoObj.getText();
        //if(global.useJLabels)
        //    t="<b>"+ico+"</b>"+t;
        //else
        //    t=""+t;
        if(c.equals("MAKE")){
            return t+" "+obj;
        }
        if(obj.equals("logo")) obj="";
        else obj+=".";
        if(c.equals("forward") || c.equals("backward")
                || c.equals("turnRight") || c.equals("turnLeft")
                || c.equals("distance")){
            return obj+t+" "+value.getText()+" ";
        }
        if(c.equals("setColor")){
            return obj+t+" "+colorHEX.getText()+"";
        }
        else
            return obj+t;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        valueOnly = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        value = new javax.swing.JTextField();
        color = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        colorHEX = new javax.swing.JTextField();
        colorPick = new javax.swing.JButton();
        make = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        makeW = new javax.swing.JSpinner();
        makeH = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        closedCanvas = new javax.swing.JCheckBox();
        variablePane = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        variable = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        logoObj = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        command = new javax.swing.JComboBox();
        editor = new javax.swing.JPanel();
        tipBox = new javax.swing.JPanel();
        tips = new javax.swing.JLabel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config/lang/addons"); // NOI18N
        jLabel3.setText(bundle.getString("gui.value")); // NOI18N

        javax.swing.GroupLayout valueOnlyLayout = new javax.swing.GroupLayout(valueOnly);
        valueOnly.setLayout(valueOnlyLayout);
        valueOnlyLayout.setHorizontalGroup(
            valueOnlyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(valueOnlyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(value, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE))
        );
        valueOnlyLayout.setVerticalGroup(
            valueOnlyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(valueOnlyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel3)
                .addComponent(value, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel8.setText("HEX:");

        colorHEX.setColumns(8);
        colorHEX.setText("0x000000");

        colorPick.setText(bundle.getString("gui.pickColor")); // NOI18N
        colorPick.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorPickActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout colorLayout = new javax.swing.GroupLayout(color);
        color.setLayout(colorLayout);
        colorLayout.setHorizontalGroup(
            colorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, colorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(colorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(colorPick, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                    .addGroup(colorLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(colorHEX, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)))
                .addContainerGap())
        );
        colorLayout.setVerticalGroup(
            colorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(colorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(colorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(colorHEX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(colorPick)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel4.setText(bundle.getString("gui.makeWidth")); // NOI18N

        jLabel5.setText(bundle.getString("gui.makeHeight")); // NOI18N

        makeW.setModel(new javax.swing.SpinnerNumberModel(500, 100, 2000, 50));

        makeH.setModel(new javax.swing.SpinnerNumberModel(500, 100, 2000, 50));

        jLabel6.setText("px");

        jLabel7.setText("px");

        closedCanvas.setText(bundle.getString("gui.closedCanvas")); // NOI18N

        javax.swing.GroupLayout makeLayout = new javax.swing.GroupLayout(make);
        make.setLayout(makeLayout);
        makeLayout.setHorizontalGroup(
            makeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(makeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(makeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(makeLayout.createSequentialGroup()
                        .addGroup(makeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(makeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(makeH)
                            .addComponent(makeW))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(makeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addContainerGap())
                    .addComponent(closedCanvas, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        makeLayout.setVerticalGroup(
            makeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(makeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(makeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(makeW, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(makeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(makeH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(closedCanvas)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel9.setText("Variable:");

        javax.swing.GroupLayout variablePaneLayout = new javax.swing.GroupLayout(variablePane);
        variablePane.setLayout(variablePaneLayout);
        variablePaneLayout.setHorizontalGroup(
            variablePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(variablePaneLayout.createSequentialGroup()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(variable, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE))
        );
        variablePaneLayout.setVerticalGroup(
            variablePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(variablePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel9)
                .addComponent(variable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        setBorder(javax.swing.BorderFactory.createTitledBorder("Logo editor"));

        jLabel1.setText(bundle.getString("gui.object")); // NOI18N

        logoObj.setText("logo");

        jLabel2.setText(bundle.getString("gui.command")); // NOI18N

        command.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                commandItemStateChanged(evt);
            }
        });

        editor.setLayout(new javax.swing.BoxLayout(editor, javax.swing.BoxLayout.Y_AXIS));

        tipBox.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("gui.tooltip"))); // NOI18N

        tips.setText(bundle.getString("gui.tip")); // NOI18N

        javax.swing.GroupLayout tipBoxLayout = new javax.swing.GroupLayout(tipBox);
        tipBox.setLayout(tipBoxLayout);
        tipBoxLayout.setHorizontalGroup(
            tipBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tips, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
        );
        tipBoxLayout.setVerticalGroup(
            tipBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tips, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(logoObj, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                    .addComponent(command, javax.swing.GroupLayout.Alignment.TRAILING, 0, 156, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(tipBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(editor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(logoObj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(command, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tipBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(editor, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void commandItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_commandItemStateChanged
        String c=((ComboText)command.getSelectedItem()).getValue();
        editor.removeAll();
        //At first: variable
        if(c.equals("distance")){
            editor.add(variablePane);
        }
        //Then: arguments
        if(c.equals("forward") || c.equals("backward")
                || c.equals("turnRight") || c.equals("turnLeft")
                || c.equals("distance")){
            editor.add(valueOnly);
        }
        if(c.equals("MAKE")){
            editor.add(make);
        }
        if(c.equals("setColor")){
            editor.add(color);
        }
        
        if(global.showToolTips){
            try{
                String tip=translator.addons.getString("logo."+c+".help");
                tips.setText(tip);
                tipBox.setVisible(true);
            }catch(Exception e){
                tipBox.setVisible(false);
            }
        }
        
        editor.validate();
        repaint();
    }//GEN-LAST:event_commandItemStateChanged

    private void colorPickActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorPickActionPerformed
        Color c=JColorChooser.showDialog( this,
                     "Choose a color", Color.decode(colorHEX.getText()) );
        if(c!=null)
        colorHEX.setText( "0x"+Integer.toHexString(
                c.getRed()*256*256+
                c.getGreen()*256+
                c.getBlue()
                ).substring(0, 6) );
    }//GEN-LAST:event_colorPickActionPerformed

    private void reset(){
        value.setText("0");
        makeW.setValue(500);makeH.setValue(500);
        colorHEX.setText("0x000000");
    }

    public void setEditedBlock(JBlock b) {
        if(b==editing) return ;
        editing=(logoBlock)b;
        reset();
        for(ComboText c: commands){
            if(c.getValue().equals(editing.command)){
                command.setSelectedItem(c);
                break;
            }
        }
        logoObj.setText(editing.obj);
        String c=((ComboText)command.getSelectedItem()).getValue();
        if(c.equals("forward") || c.equals("backward")
                || c.equals("turnRight") || c.equals("turnLeft")){
            value.setText(""+editing.data[0]);
        }
        if(c.equals("MAKE")){
            makeW.setValue(editing.data[0]);
            makeH.setValue(editing.data[1]);
            closedCanvas.setSelected(editing.data[2].equals(true));
        }
        if(c.equals("setColor")){
            colorHEX.setText(editing.data[0].toString());
        }
    }

    @Override
    public void finnishEdit() {
        saveBlock();
        editing=null;
    }

    @Override
    public boolean changes() {
        return true;
    }

    @Override
    public void saveBlock() {
        colorHEX.requestFocus();
        editing.obj=logoObj.getText();
        editing.commentIsHTML=global.useJLabels;
        String c=((ComboText)command.getSelectedItem()).getValue();
        editing.command=c;
        if(c.equals("forward") || c.equals("backward")
                || c.equals("turnRight") || c.equals("turnLeft")){
            editing.data=new Object[1];
            editing.data[0]=value.getText();
        }
        if(c.equals("MAKE")){
            editing.data=new Object[3];
            editing.data[0]=makeW.getValue();
            editing.data[1]=makeH.getValue();
            editing.data[2]=closedCanvas.isSelected();
        }
        if(c.equals("setColor")){
            editing.data=new Object[1];
            editing.data[0]=colorHEX.getText();
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
    private javax.swing.JCheckBox closedCanvas;
    private javax.swing.JPanel color;
    private javax.swing.JTextField colorHEX;
    private javax.swing.JButton colorPick;
    private javax.swing.JComboBox command;
    private javax.swing.JPanel editor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField logoObj;
    private javax.swing.JPanel make;
    private javax.swing.JSpinner makeH;
    private javax.swing.JSpinner makeW;
    private javax.swing.JPanel tipBox;
    private javax.swing.JLabel tips;
    private javax.swing.JTextField value;
    private javax.swing.JPanel valueOnly;
    private javax.swing.JTextField variable;
    private javax.swing.JPanel variablePane;
    // End of variables declaration//GEN-END:variables

}
