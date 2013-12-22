package net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.*;

public class guestBook extends javax.swing.JFrame {

    public guestBook() {
        initComponents();
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        name = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        rate = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        comment = new javax.swing.JTextArea();
        send = new javax.swing.JButton();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config/lang/lang"); // NOI18N
        jLabel1.setText(bundle.getString("guest.yourName")); // NOI18N

        jLabel3.setText(bundle.getString("guest.link")); // NOI18N

        jLabel4.setText(bundle.getString("guest.rate")); // NOI18N

        jLabel5.setText(bundle.getString("guest.comment")); // NOI18N

        rate.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "1", "2", "3", "4", "5" }));

        comment.setColumns(10);
        comment.setLineWrap(true);
        comment.setRows(5);
        jScrollPane1.setViewportView(comment);

        send.setText(bundle.getString("guest.send")); // NOI18N
        send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rate, 0, 176, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                            .addComponent(email, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                            .addComponent(name, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(send, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(rate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(send)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void clear(){
        this.name.setText("");
        this.email.setText("");
        this.comment.setText("");
        this.rate.setSelectedIndex(0);
    }

    @Override
    public void show(){
        clear();
        super.show();
    }

    private void sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendActionPerformed
        if (name.getText().length() < 3) {
            JOptionPane.showMessageDialog(comment, "Too short name (at least 3)");
            return;
        }
        if (comment.getText().length() < 10) {
            JOptionPane.showMessageDialog(comment, "Too short comment (at least 10)");
            return;
        }
        Thread t=new Thread(
                new Runnable() {
                        public void run() { // <editor-fold defaultstate="collapsed" desc="send">
                        try {
                            String data = URLEncoder.encode("send_id", "UTF-8") + "="
                                    + URLEncoder.encode("1", "UTF-8");
                            data += "&" + URLEncoder.encode("author", "UTF-8") + "="
                                    + URLEncoder.encode(name.getText(), "UTF-8");

                            data += "&" + URLEncoder.encode("location", "UTF-8") + "="
                                    + URLEncoder.encode(System.getProperty("user.language"), "UTF-8");

                            data += "&" + URLEncoder.encode("contact", "UTF-8") + "="
                                    + URLEncoder.encode(email.getText(), "UTF-8");

                            data += "&" + URLEncoder.encode("content", "UTF-8") + "="
                                    + URLEncoder.encode(comment.getText(), "UTF-8");

                            data += "&" + URLEncoder.encode("rate", "UTF-8") + "="
                                    + URLEncoder.encode(rate.getSelectedItem().toString(), "UTF-8");
                            System.out.println(data);
                            URL url = new URL("http://javablock.sourceforge.net/book/index.php");
                            URLConnection conn = url.openConnection();
                            conn.setDoOutput(true);
                            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                            wr.write(data);
                            wr.flush();
                            // Get the response

                            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            String address = rd.readLine();

                            JPanel panel = new JPanel();
                            panel.add(new JLabel("Comment added"));
                            panel.add(new JTextArea("visit: http://javablock.sourceforge.net/"));

                            JOptionPane.showMessageDialog(null, new JLabel("Comment sended correctly!"));

                            wr.close();
                            rd.close();
                            hide();
                } catch (IOException ex) {
                    Logger.getLogger(guestBook.class.getName()).log(Level.SEVERE, null, ex);
                }// </editor-fold>
            }
        }
        );
        t.start();
    }//GEN-LAST:event_sendActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea comment;
    private javax.swing.JTextField email;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField name;
    private javax.swing.JComboBox rate;
    private javax.swing.JButton send;
    // End of variables declaration//GEN-END:variables

}
