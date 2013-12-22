/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * For.java
 *
 * Created on 2011-05-26, 01:01:36
 */
package javablock.flowchart.generator;

import config.global;
import config.translator;
import javablock.Sheet;
import javablock.flowchart.Flowchart;
import javablock.flowchart.JBlock;
import javablock.flowchart.blocks.*;
import javax.swing.JOptionPane;

/**
 *
 * @author razi
 */
class For extends javax.swing.JPanel implements Generator{

    /** Creates new form For */
    public For() {
        initComponents();
    }
    void reset(){
        variable.setText("i");
        initial.setText("0");
        end.setText("10");
        comp.setSelectedIndex(0);
        iterType.setSelectedIndex(0);
        iterNum.setText("1");
        useClips.setSelected(false);
        declare.setSelected(true);
    }
    @Override
    public JBlock[] get(Flowchart f){
        For g=this;
        int res=JOptionPane.showConfirmDialog(global.Window, g, 
                translator.get("generator.for.title"),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if(res==JOptionPane.OK_OPTION){
            int p=0;
            if(!g.useClips.isSelected()){
                JBlock[] list=new JBlock[8];
                cpuBlock init=(cpuBlock) JBlock.make(JBlock.Type.CPU, f);
                init.setCode((g.declare.isSelected()?"var ":"")+
                        g.variable.getText()+" = "+g.initial.getText());
                init.setPos(0,-90);
                list[p++]=init;
                
                decBlock condition=(decBlock) JBlock.make(JBlock.Type.DECISION, f);
                condition.setCode(g.variable.getText()+" "+g.comp.getSelectedItem()+" "+
                        g.end.getText());
                list[p++]=condition;
                
                cpuBlock iter=(cpuBlock) JBlock.make(JBlock.Type.CPU, f);
                iter.setCode(g.variable.getText()+" "+g.iterType.getSelectedItem()
                        +"= "+g.iterNum.getText());
                iter.setPos(100,100);
                list[p++]=iter;
                
                jumpBlock jump, jump2;
                
                jump=(jumpBlock) JBlock.make(JBlock.Type.JUMP, f);
                jump.setPos(0, -50);
                init.connectTo(jump);
                jump.connectTo(condition);
                list[p++]=jump;
                
                jump2=(jumpBlock) JBlock.make(JBlock.Type.JUMP, f);
                jump2.setPos(100,0);
                condition.connectTo(jump2);
                jump2.connectTo(iter);
                list[p++]=jump2;
                
                jump2=(jumpBlock) JBlock.make(JBlock.Type.JUMP, f);
                jump2.setPos(200,-50);
                jump2.connectTo(jump);
                list[p++]=jump2;
                
                jump=(jumpBlock) JBlock.make(JBlock.Type.JUMP, f);
                jump.setPos(200, 100);
                jump.connectTo(jump2);
                iter.connectTo(jump);
                list[p++]=jump;
                
                jump=(jumpBlock) JBlock.make(JBlock.Type.JUMP, f);
                jump.setPos(0,100);
                condition.connectTo(jump);
                list[p++]=jump;
                
                return list;
            }
            else{
                JBlock[] list=new JBlock[7];
                cpuBlock init=(cpuBlock) JBlock.make(JBlock.Type.CPU, f);
                init.setCode((g.declare.isSelected()?"var ":"")+
                        g.variable.getText()+" = "+g.initial.getText());
                init.setPos(0,-90);
                list[p++]=init;
                
                decBlock condition=(decBlock) JBlock.make(JBlock.Type.DECISION, f);
                condition.setCode(g.variable.getText()+" "+g.comp.getSelectedItem()+" "+
                        g.end.getText());
                init.connectTo(condition);
                list[p++]=condition;
                
                cpuBlock iter=(cpuBlock) JBlock.make(JBlock.Type.CPU, f);
                iter.setCode(g.variable.getText()+" "+g.iterType.getSelectedItem()
                        +"= "+g.iterNum.getText());
                iter.setPos(180,100);
                list[p++]=iter;
                
                jumpBlock jump;
                braceBlock cl1, cl2;
                
                cl1=(braceBlock) JBlock.make(JBlock.Type.BRACE, f);
                cl1.setOpen(true);
                cl1.setPos(100, 0);
                cl2=(braceBlock) JBlock.make(JBlock.Type.BRACE, f);
                cl2.setOpen(false);
                cl2.setPos(100, 100);
                cl1.setLinkTo(cl2);
                list[p++]=cl1;
                list[p++]=cl2;
                
                condition.connectTo(cl1);
                iter.connectTo(cl2);
                
                jump=(jumpBlock) JBlock.make(JBlock.Type.JUMP, f);
                jump.setPos(180, 0);
                cl1.connectTo(jump);
                jump.connectTo(iter);
                list[p++]=jump;
                
                jump=(jumpBlock) JBlock.make(JBlock.Type.JUMP, f);
                jump.setPos(0, 100);
                condition.connectTo(jump);
                list[p++]=jump;
                
                return list;
            }
        }
        return null;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        variable = new javax.swing.JTextField();
        initial = new javax.swing.JTextField();
        end = new javax.swing.JTextField();
        comp = new javax.swing.JComboBox();
        iterNum = new javax.swing.JTextField();
        iterType = new javax.swing.JComboBox();
        useClips = new javax.swing.JCheckBox();
        declare = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config/lang/lang"); // NOI18N
        jLabel1.setText(bundle.getString("generator.for.variable")); // NOI18N

        jLabel2.setText(bundle.getString("generator.for.initValue")); // NOI18N

        jLabel4.setText(bundle.getString("generator.for.comparsion")); // NOI18N

        jLabel5.setText(bundle.getString("generator.for.iteration")); // NOI18N

        variable.setText("i");

        initial.setText("0");

        end.setText("10");

        comp.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<", "≤", "=", "≥", ">", "≠" }));

        iterNum.setText("1");

        iterType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "+", "-", "*", "/" }));

        useClips.setText(bundle.getString("generator.for.useBraces")); // NOI18N

        declare.setSelected(true);
        declare.setText(bundle.getString("generator.for.declare")); // NOI18N

        jLabel3.setText("←");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(declare)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(useClips))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(iterType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(end, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                            .addComponent(iterNum, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(variable, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(initial, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(variable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(initial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(comp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(end, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(iterType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iterNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(useClips)
                    .addComponent(declare))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox comp;
    private javax.swing.JCheckBox declare;
    private javax.swing.JTextField end;
    private javax.swing.JTextField initial;
    private javax.swing.JTextField iterNum;
    private javax.swing.JComboBox iterType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JCheckBox useClips;
    private javax.swing.JTextField variable;
    // End of variables declaration//GEN-END:variables
}
