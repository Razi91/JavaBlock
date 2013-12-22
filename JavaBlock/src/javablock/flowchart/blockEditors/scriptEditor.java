package javablock.flowchart.blockEditors;

import javablock.flowchart.*;
import javablock.flowchart.blocks.scrBlock;
import javax.swing.text.EditorKit;


public class scriptEditor extends javax.swing.JPanel implements BlockEditor{

    public scriptEditor() {
        initComponents();
        setKit();
    }

    private void setKit(){
        if(addons.Syntax.loaded){
            js.setEditorKit((EditorKit) addons.Syntax.js);
            py.setEditorKit((EditorKit) addons.Syntax.py);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        name = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        js = new javax.swing.JEditorPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        py = new javax.swing.JEditorPane();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config/lang/addons"); // NOI18N
        jLabel1.setText(bundle.getString("gui.name")); // NOI18N

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setFont(new java.awt.Font("Monospaced", 0, 13)); // NOI18N

        js.setEditorKit(null);
        js.setFont(new java.awt.Font("Monospaced", 0, 13)); // NOI18N
        jScrollPane1.setViewportView(js);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("JavaScript", jPanel1);

        py.setEditorKit(null);
        py.setFont(new java.awt.Font("Monospaced", 0, 13)); // NOI18N
        jScrollPane2.setViewportView(py);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Python", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(name, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE))
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    scrBlock editing;
    public void setEditedBlock(JBlock b) {
        //setKit();
        editing=(scrBlock)b;
        js.setText(editing.js);
        py.setText(editing.py);
        name.setText(editing.name);
    }

    public void finnishEdit() {
        saveBlock();
        editing=null;
    }

    public boolean changes() {
        return true;
    }

    public void saveBlock() {
        editing.js=js.getText();
        editing.py=py.getText();
        editing.name=name.getText();
        editing.shape();
    }

    public JBlock getBlock() {
        return editing;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JEditorPane js;
    private javax.swing.JTextField name;
    private javax.swing.JEditorPane py;
    // End of variables declaration//GEN-END:variables

}
