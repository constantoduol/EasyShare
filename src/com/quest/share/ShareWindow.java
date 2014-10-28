/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ShareWindow.java
 *
 * Created on Jun 24, 2013, 11:29:18 PM
 */
package com.quest.share;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import net.iharder.dnd.FileDrop;

/**
 *
 * @author connie
 */
public class ShareWindow extends javax.swing.JFrame {
    private static ConcurrentHashMap<String,ChatClient> clients=new ConcurrentHashMap();
    private static final int PORT=37829;
    private static String currentRecipient;
    private static File[] currentFiles;
    /** Creates new form ShareWindow */
    public ShareWindow() {
        initComponents();
        try {
            final ChatServer server=new ChatServer(PORT);
            UniqueRandom rand=new UniqueRandom(8);
            String key=rand.nextRandom();
            setInfo("Session Key : "+key);
            ProcessBuilder pb=new ProcessBuilder("Elevate.exe","netsh","wlan","set", "hostednetwork","mode=allow","ssid=EasyShare","key="+key+"");
            pb.start();
            ProcessBuilder pb1=new ProcessBuilder("Elevate.exe","netsh","wlan","start","hostednetwork");
            pb1.start();
            jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             final String ip=jTextField1.getText();
              Thread th=new Thread(){
                    @Override
               public void run(){
                try {
                   
                   if(clients.contains(ip)){
                      setInfo("Already connected to host: "+ip);
                      return; 
                    }
                    setInfo("Connecting to host: "+ip);
                    jButton1.setEnabled(false);
                    ChatClient client=new ChatClient(ip, PORT);
                    clients.put(ip, client);
                    currentRecipient=ip;
                    jButton1.setEnabled(true);
                } catch (IOException ex) {
                    jButton1.setEnabled(true);
                    setInfo("Error connecting to host: "+ip);
                    clients.remove(ip);
                }
               }
              };
             th.start();
            }
        });
          this.addWindowListener(new WindowAdapter() {  // A listener to end the program when the user closes the window.
              @Override
              public void windowClosing(WindowEvent evt) {
                  try {
                    // disconnect all connected clients
                    // and send disconnect messages to all servers
                    ProcessBuilder pb1=new ProcessBuilder("Elevate.exe","netsh","wlan","stop","hostednetwork");
                    pb1.start();
                    Iterator<String> iter = clients.keySet().iterator();
                    while(iter.hasNext()){
                       clients.get(iter.next()).disconnect();
                    }
                   if(server!=null){
                     server.shutDownHub();   
                   }    
                    Thread.sleep(1000);
                    } catch (Exception ex) {
                        Logger.getLogger(ShareWindow.class.getName()).log(Level.SEVERE, null, ex);
                        System.exit(0); 
                    }
                   System.exit(0); 
              }
           });
           dragFiles();
          } catch (IOException ex) {
            Logger.getLogger(ShareWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    private void dragFiles(){
         final javax.swing.JFrame frame = new javax.swing.JFrame( "Files To Transfer" );
                      //javax.swing.border.TitledBorder dragBorder = new javax.swing.border.TitledBorder( "Drop 'em" );
         final javax.swing.JTextArea text = new javax.swing.JTextArea();
         frame.getContentPane().add( 
         new javax.swing.JScrollPane( text ), 
         java.awt.BorderLayout.CENTER );
         frame.setBounds( 100, 100, 300, 400 );
         frame.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE);
         frame.addWindowListener(new WindowAdapter() {  // A listener to end the program when the user closes the window.
              @Override
              public void windowClosing(WindowEvent evt) {
                frame.setVisible(false);
                text.setText("");
              }
           });
         FileDrop dp=new FileDrop( System.out, jPanel2, /*dragBorder,*/ new FileDrop.Listener(){  
           @Override
            public void filesDropped( java.io.File[] files ){ 
              currentFiles=files;
              for( int i = 0; i < files.length; i++ ) { 
                 try
                    {
                      String path=files[i].getCanonicalPath();
                      text.append(path);
                      text.append("\n");
                    }   // end try
                    catch( java.io.IOException e ) {}
                }   // end for: through each dropped file
            frame.setVisible(true);
            }   // end filesDropped
        }); // end FileDrop.Listener
       
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton2 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("EasyShare");
        setResizable(false);

        jLabel1.setText("Computer Name");

        jButton1.setText("Connect");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 204, 255));
        jPanel2.setForeground(new java.awt.Color(0, 204, 255));

        jLabel4.setFont(new java.awt.Font("Candara", 0, 18));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("DRAG HERE TO TRANSFER");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );

        jLabel2.setText("jLabel2");

        jLabel3.setFont(new java.awt.Font("Candara", 0, 14));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jButton2.setText("Send");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel5.setText("Save Directory");

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jButton3.setText("Select Folder");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButton1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton2))
                                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(26, 26, 26))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jProgressBar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE))
                        .addContainerGap())))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(0, 227, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addGap(0, 228, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addGap(13, 13, 13)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(0, 154, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addGap(0, 154, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jButton1ActionPerformed

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            // TODO add your handling code here:
            //send the file
            ChatClient client = clients.get(currentRecipient);
            InetAddress address=InetAddress.getLocalHost();
            client.send(new Request("receive_files", address.getHostName()));
            jButton1.setEnabled(false);
            jButton2.setEnabled(false);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ShareWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        
}//GEN-LAST:event_jButton2ActionPerformed

private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jTextField2ActionPerformed

private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
// TODO add your handling code here:
     File file = selectFile("Select directory to save files found");
     jTextField2.setText(file.getAbsolutePath());
}//GEN-LAST:event_jButton3ActionPerformed


private static File selectFile(String title) {
   JFileChooser fileDialog=null;
   if (fileDialog == null)
      fileDialog = new JFileChooser();
      fileDialog.setDialogTitle(title);
      fileDialog.setSelectedFile(null);  // No file is initially selected.
      fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
       int option = fileDialog.showOpenDialog(null);
       if (option != JFileChooser.APPROVE_OPTION)
         return null;  // User canceled or clicked the dialog's close box.
       File selectedFile = fileDialog.getSelectedFile();
     return selectedFile;
      
}
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ShareWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ShareWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ShareWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ShareWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ShareWindow().setVisible(true);
            }
        });
    }
    
      public static void setInfo(String info){
        jLabel3.setText(info);
      }
      
      public static String getSaveDirectory(){
          return jTextField2.getText();
      }
      
 
    
  
    public static void initiateFileTransfer(){
       for(int x=0; x<currentFiles.length; x++){
        FileInputStream fis = null;
        try {
            ChatClient client = clients.get(currentRecipient);
            Socket sock = client.getClientSocket();
            long length = currentFiles[x].length();
            if (length > Integer.MAX_VALUE) {
                System.out.println("File is too large.");
            }
            byte[] bytes = new byte[(int) length];
            fis = new FileInputStream(currentFiles[x]);
            BufferedInputStream bis = new BufferedInputStream(fis);
            BufferedOutputStream out = new BufferedOutputStream(sock.getOutputStream());
            int count;
            while ((count = bis.read(bytes)) > 0) {
                out.write(bytes, 0, count);
            }
            out.flush();
            out.close();
            fis.close();
            bis.close();
        } catch (Exception ex) {
            Logger.getLogger(ShareWindow.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(ShareWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       }
    }



    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private static javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JTextField jTextField1;
    private static javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
