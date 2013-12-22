/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NS.java
 *
 * Created on 2011-08-11, 00:51:37
 */
package javablock.ns;
import java.awt.Color;
import java.util.List;
import javablock.*;
import javablock.flowchart.JBlock;
import javablock.interfaces.*;
import org.w3c.dom.*;

/**
 *
 * @author razi
 */
public class NS extends Sheet {

    /** Creates new form NS */
    public NS(FlowchartManager m) {
        super(m);
        initComponents();
        viewport.setViewportView(new Canvas(this));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        viewport = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        editor = new javax.swing.JPanel();
        colorPane = new javax.swing.JPanel();

        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setRightComponent(viewport);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config/lang/lang"); // NOI18N
        jButton1.setText(bundle.getString("ns.moveUp")); // NOI18N

        jButton2.setText(bundle.getString("ns.addAbove")); // NOI18N

        jButton3.setText(bundle.getString("ns.addBelow")); // NOI18N

        jButton4.setText(bundle.getString("ns.moveDown")); // NOI18N

        javax.swing.GroupLayout editorLayout = new javax.swing.GroupLayout(editor);
        editor.setLayout(editorLayout);
        editorLayout.setHorizontalGroup(
            editorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 199, Short.MAX_VALUE)
        );
        editorLayout.setVerticalGroup(
            editorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 123, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout colorPaneLayout = new javax.swing.GroupLayout(colorPane);
        colorPane.setLayout(colorPaneLayout);
        colorPaneLayout.setHorizontalGroup(
            colorPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 199, Short.MAX_VALUE)
        );
        colorPaneLayout.setVerticalGroup(
            colorPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 61, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
            .addComponent(editor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(colorPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(colorPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4))
        );

        jSplitPane1.setLeftComponent(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel colorPane;
    private javax.swing.JPanel editor;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JScrollPane viewport;
    // End of variables declaration//GEN-END:variables

    void init(){
        
    }
    
    @Override
    public void close() {
    }

    @Override
    public void delete() {
    }
    
    String name="null";
    @Override
    public String getName() {
        return name;
    }
    @Override
    public void setName(String name) {
        this.name=name;
    }

    @Override
    public void setColor(Color c, int mode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setEditable(boolean editing) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void optimizeID() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<JBlock> getSelected() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<JBlock> getBlocks() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void copy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void cut() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void paste() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void saveAsImage(String url, String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void saveAsImage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void saveXml(Element root) {
        Document doc=root.getOwnerDocument();
        Element m=doc.createElement("NS");
        root.appendChild(m);
    }
    
    @Override
    public void parseXml(Element f) {
        
    }

    @Override
    public String makePythonFunctions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String makeJavaScriptFunctions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}
