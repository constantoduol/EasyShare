/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quest.share;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author connie
 */
public class ChatServer extends Chat {
   private ConcurrentHashMap clients=new ConcurrentHashMap();
   public ChatServer(int port) throws IOException{
       super(port);
   }
   
   @Override
   protected void messageReceived(int playerID, Object message) {
       executor.execute(new MessageProcessor(message, playerID));
   }
   
    @Override
   protected void clientIsConnected(int playerID) {
     ShareWindow.setInfo("New client connection accepted");  
   }
    
    @Override
   protected void clientIsDisconnected(int clientID) {
      clients.remove(clientID);
   }
    
   public void receiveFile(Socket socket){
        try {
            InputStream is = null;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            int bufferSize = 0;
            try {
                is = socket.getInputStream();
                bufferSize = socket.getReceiveBufferSize();
                System.out.println("Buffer size: " + bufferSize);
            } catch (IOException ex) {
                System.out.println("Can't get socket input stream. ");
            }

            try {
                String saveDirectory = ShareWindow.getSaveDirectory();
                fos = new FileOutputStream(saveDirectory);
                bos = new BufferedOutputStream(fos);

            } catch (FileNotFoundException ex) {
                System.out.println("File not found. ");
            }

            byte[] bytes = new byte[bufferSize];
            int count;
            while ((count = is.read(bytes)) > 0) {
                bos.write(bytes, 0, count);
            }
            bos.flush();
            bos.close();
            is.close();
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
   }

   
  private class MessageProcessor implements Runnable{
    private Object message;
    private int clientID;
    public MessageProcessor(Object message, int clientID){
       this.message=message;
       this.clientID=clientID;
    }
    
   @Override
    public void run(){
      if(message instanceof Request){
         Request newRequest=(Request)message;  
         String msg=newRequest.getMessage();
         if(msg.equals("identity")){
            String identity=(String) newRequest.getRequestObject();
            clients.put(clientID, identity );
            ShareWindow.setInfo("Connection accepted from "+identity); 
         } 
         else if(msg.equals("receive_files")){
          int confirm = JOptionPane.showConfirmDialog(null, "Receive files from "+newRequest.getRequestObject());
          System.out.println(confirm);
          try {
            InetAddress address=InetAddress.getLocalHost();
             if(confirm==0){        
                sendToOne(clientID, new Response(address.getHostName(),"accepted"));
                  //prepare for file receipt
                 //get the socket to read data
                Socket clientSocket = ChatServer.this.getClientSocket(clientID);
                receiveFile(clientSocket);    
              }
             else{
                sendToOne(clientID, new Response(address.getHostName(),"rejected")); 
              }
            } catch (UnknownHostException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
        
    }
    // this means a client is responding to a previous request
    else if(message instanceof Response){
         Response newResponse=(Response)message;
         String msg = newResponse.getMessage();
         String ip=(String) newResponse.getResponse();
         ShareWindow.setInfo(msg);
       }
     }
   }
}
