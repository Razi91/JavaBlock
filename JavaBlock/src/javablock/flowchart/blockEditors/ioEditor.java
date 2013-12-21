package javablock.flowchart.blockEditors;

import config.global;
import config.translator;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javablock.flowchart.blocks.ioBlock;
import javablock.flowchart.*;
import widgets.ComboText;

public class ioEditor extends javax.swing.JPanel implements BlockEditor, ActionListener {
    //standardEditor s=new standardEditor();
   
    public ioBlock editing=null;
    public ioEditor() {
        initComponents();
        input.addActionListener(this);
        output.addActionListener(this);
        for(ComboText t:types){
                inType.addItem(t);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ioType = new javax.swing.ButtonGroup();
        inputPanel = new javax.swing.JPanel();
        inArray = new javax.swing.JCheckBox();
        inArrEl = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        inType = new javax.swing.JComboBox();
        inDrawMessage = new javax.swing.JCheckBox();
        outputPanel = new javax.swing.JPanel();
        outLn = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        outSuffix = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        variable = new javax.swing.JTextField();
        input = new javax.swing.JRadioButton();
        output = new javax.swing.JRadioButton();
        message = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        pane = new javax.swing.JPanel();
        drawStandard = new javax.swing.JCheckBox();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config/lang/lang"); // NOI18N
        inputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ioEditor.input"))); // NOI18N

        inArray.setText(bundle.getString("ioEditor.array")); // NOI18N

        inArrEl.setModel(new javax.swing.SpinnerNumberModel(1, 1, 20, 1));

        jLabel3.setText(bundle.getString("ioEditor.elements")); // NOI18N

        jLabel5.setText(bundle.getString("ioEditor.type")); // NOI18N

        inDrawMessage.setText(bundle.getString("ioEditor.drawMessage")); // NOI18N

        javax.swing.GroupLayout inputPanelLayout = new javax.swing.GroupLayout(inputPanel);
        inputPanel.setLayout(inputPanelLayout);
        inputPanelLayout.setHorizontalGroup(
            inputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputPanelLayout.createSequentialGroup()
                .addGroup(inputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inputPanelLayout.createSequentialGroup()
                        .addComponent(inArray)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inArrEl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addGroup(inputPanelLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inType, 0, 147, Short.MAX_VALUE))
                    .addComponent(inDrawMessage))
                .addContainerGap())
        );
        inputPanelLayout.setVerticalGroup(
            inputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputPanelLayout.createSequentialGroup()
                .addGroup(inputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inArray)
                    .addComponent(inArrEl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(inputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(inType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inDrawMessage))
        );

        outLn.setText(bundle.getString("ioEditor.newLine")); // NOI18N

        jLabel4.setText(bundle.getString("ioEditor.messageSufix")); // NOI18N

        outSuffix.setText("jTextField1");

        javax.swing.GroupLayout outputPanelLayout = new javax.swing.GroupLayout(outputPanel);
        outputPanel.setLayout(outputPanelLayout);
        outputPanelLayout.setHorizontalGroup(
            outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(outputPanelLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(outSuffix, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE))
                    .addComponent(outLn))
                .addContainerGap())
        );
        outputPanelLayout.setVerticalGroup(
            outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(outSuffix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(outLn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ioEditor.header"))); // NOI18N

        jLabel1.setText(bundle.getString("ioEditor.variable")); // NOI18N

        variable.setText("jTextField1");

        ioType.add(input);
        input.setSelected(true);
        input.setText(bundle.getString("ioEditor.input")); // NOI18N

        ioType.add(output);
        output.setText(bundle.getString("ioEditor.output")); // NOI18N

        message.setText("jTextField2");

        jLabel2.setText(bundle.getString("ioEditor.mesasge")); // NOI18N

        pane.setLayout(new java.awt.BorderLayout());

        drawStandard.setText(bundle.getString("ioEditor.drawStandard")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2))
                    .addComponent(input))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(variable, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addComponent(message, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addComponent(output))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(drawStandard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(pane, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(variable, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(message, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(drawStandard)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(input)
                    .addComponent(output))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    String makeCode(){
        String eng=global.getManager().scriptEngine;
        String code="";
        String vars[]=variable.getText().replaceAll(", ", " ").split("[\\s,]");
        if(output.isSelected()){
            for(String var:vars){
                if(var.length()==0) continue;
                code+="Write";
                if(outLn.isSelected())
                    code+="ln";
                
                code+="(";
                //if(message.getText().length()>0)
                    code+="\""+message.getText()+"\"";
                code+=" + (getValueOf("+var+"))";
                if(outSuffix.getText().length()>0)
                    code+="+\""+outSuffix.getText()+"\"";
                code+=")\n";
            }
            if(vars[0].length()==0){
                code+="Write";
                if(outLn.isSelected())
                    code+="ln";
                code+="(";
                if(message.getText().length()>0)
                    code+="\""+message.getText()+"\"";
                if(message.getText().length()>0 && outSuffix.getText().length()>0)
                    code+="+";
                if(outSuffix.getText().length()>0)
                    code+="\""+outSuffix.getText()+"\"";
                code+=")\n";
            }
        }
        if(input.isSelected()){
            DataType type=DataType.valueOf(((ComboText)inType.getSelectedItem()).getValue());
            for(String var:vars){
                if(inArray.isSelected()){
                    if(eng.equals("JavaScript"))
                        code+="var "+var+"=new Array("+inArrEl.getValue()+")\n";
                    if(eng.equals("Python"))
                        code+=var+"=Array("+inArrEl.getValue()+")\n";
                    for(int i=0; i<(Integer)inArrEl.getValue(); i++){
                        code+=""+var+"["+i+"]=";
                        code+="Read";
                        switch(type){
                            case NUMBER:    code+="Number";    break;
                            case INTEGER:   code+="Integer";   break;
                            case LOGIC:     code+="Logic";     break;
                            case CHARARRAY: code+="CharArray"; break;
                        }
                        code+="(\"["+i+"] "+message.getText()+"\")\n";
                    }
                }
                else{
                    code+=""+var+"=";
                    code+="Read";
                    switch(type){
                            case NUMBER:    code+="Number";    break;
                            case INTEGER:   code+="Integer";   break;
                            case LOGIC:     code+="Logic";     break;
                            case CHARARRAY: code+="CharArray"; break;
                        }
                    code+="(\""+message.getText()+"\")\n";
                }
            }
        }
        code+="\n";
        return code;
    }
    String makeComment(){
        String comment="";
        if (output.isSelected()) {
            if(drawStandard.isSelected()){
                comment=translator.misc.getString("write");
                if(this.outLn.isSelected())
                    comment+=translator.misc.getString("ln")+" ";
                else
                    comment+=": ";
            }
            if (message.getText().length() > 0)
                comment += "\"" + message.getText() + "\" ";
            if (message.getText().length() > 0 && variable.getText().length() > 0)
                comment += "+";
            if (variable.getText().length() > 0)
                comment += variable.getText();
            if (outSuffix.getText().length() > 0 && variable.getText().length() > 0)
                comment += "+";
            if (outSuffix.getText().length() > 0)
                comment += " \"" + outSuffix.getText() + "\" ";
        }
        if (input.isSelected()) {
            if(drawStandard.isSelected()){
                if(inDrawMessage.isSelected())
                    comment+=translator.misc.getString("write")+": \""+message.getText()+"\"\n";
                comment+=translator.misc.getString("read")+" ";
            }
            else if(inDrawMessage.isSelected() && !message.getText().isEmpty())
                comment+="\""+message.getText()+"\"\n";
            comment += variable.getText()
                    + (inArray.isSelected() ? " [" + inArrEl.getValue() + "]" : "")
                    + " \n";
        }
        return comment;
    }

    void setInput(){
        if(!input.isSelected())
            input.setSelected(true); String t="";
        switch(editing.inputType){
            case 0: t="NUMBER"; break;
            case 1: t="STRING"; break;
            case 2: t="INTEGER"; break;
            case 3: t="CHARARRAY"; break;
            case 4: t="LOGIC"; break;
        }
        for(ComboText c:types){
            if(c.getValue().equals(t))
                inType.setSelectedItem(c);
        }
        if(editing.array>0){
            inArrEl.setValue(editing.array);
            inArray.setSelected(true);
        }
        else{
            inArrEl.setValue(0);
            inArray.setSelected(false);
        }
        pane.removeAll();
        pane.add(this.inputPanel);
        revalidate();
        repaint();
    }
    void setOutput(){
        if(!output.isSelected())
            output.setSelected(true);
        outSuffix.setText(editing.messageSuffix);
        outLn.setSelected(editing.newLine);
        pane.removeAll();
        pane.add(this.outputPanel);
        revalidate();
        repaint();
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==input)
            if(input.isSelected())
                setInput();
        if(e.getSource()==output)
            if(output.isSelected())
                setOutput();
    }

    void reset(){
        output.setSelected(true);
        input.setSelected(false);
        drawStandard.setSelected(false);
        variable.setText("");
        inType.setSelectedItem(types[0]);
        inDrawMessage.setSelected(true);
        inArray.setSelected(false);
    }
    public void setEditedBlock(JBlock b) {
        reset();
        editing=(ioBlock)b;
        message.setText(editing.message);
        variable.setText(editing.variable);
        switch(Math.abs(editing.ioType)){
            case 1:setOutput(); break;
            case 2:setInput(); break;
        }
        if(editing.ioType<0)
            drawStandard.setSelected(true);
        else
            drawStandard.setSelected(false);
    }

    public void finnishEdit() {
        saveBlock();
        editing=null;
    }

    public boolean changes() {
        return true;
    }

    public void saveBlock() {
        try {
            inArrEl.commitEdit();
        } catch (ParseException ex) {
        }
        editing.message=message.getText();
        editing.variable=variable.getText();
        editing.ioType=(input.isSelected()?2:1);
        if(editing.ioType==2){
            editing.array=(inArray.isSelected()?
                (Integer)inArrEl.getValue()
                :0);
            switch(DataType.valueOf(
                    ((ComboText)inType.getSelectedItem()).getValue()
                    )){
                case NUMBER:
                    editing.inputType=0; break;
                case STRING:
                    editing.inputType=1; break;
                case INTEGER:
                    editing.inputType=2; break;
                case CHARARRAY:
                    editing.inputType=3; break;
                case LOGIC:
                    editing.inputType=4; break;
            }
        }
        if(editing.ioType==1){
            editing.newLine=outLn.isSelected();
            editing.messageSuffix=outSuffix.getText();
        }
        if(this.drawStandard.isSelected())
            editing.ioType*=-1;
        editing.displayComment=true;
        editing.setCode(makeCode());
        editing.setComment(makeComment());
        editing.requestRepaint();
    }

    public JBlock getBlock(){
        return editing;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox drawStandard;
    private javax.swing.JSpinner inArrEl;
    private javax.swing.JCheckBox inArray;
    private javax.swing.JCheckBox inDrawMessage;
    private javax.swing.JComboBox inType;
    private javax.swing.JRadioButton input;
    private javax.swing.JPanel inputPanel;
    private javax.swing.ButtonGroup ioType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField message;
    private javax.swing.JCheckBox outLn;
    private javax.swing.JTextField outSuffix;
    private javax.swing.JRadioButton output;
    private javax.swing.JPanel outputPanel;
    private javax.swing.JPanel pane;
    private javax.swing.JTextField variable;
    // End of variables declaration//GEN-END:variables

    /*
     * 0 - NUMBER
     * 1 - STRING
     * 2 - Integer
     * 3 - char Array
     * 4 - Logic
     */
    public enum DataType{
        NUMBER, STRING, INTEGER, CHARARRAY, LOGIC, ANY
    }
    ComboText types[]={
        new ComboText(translator.get("ioEditor.typeNumber"), "NUMBER"),
        new ComboText(translator.get("ioEditor.typeString"), "STRING"),
        new ComboText(translator.get("ioEditor.typeInteger"), "INTEGER"),
        new ComboText(translator.get("ioEditor.typeCharArray"), "CHARARRAY"),
        new ComboText(translator.get("ioEditor.typeLogic"), "LOGIC"),
    };
}
