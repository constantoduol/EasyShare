/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quest.share;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author connie
 */
public class ChatClient extends Client {
    private String host;
    public ChatClient(String host,int port) throws IOException{
        super(host, port);
        this.host=host;
    }

    @Override
    protected void messageReceived(Object message) {
       if(message instanceof Response){
         Response res=(Response)message;
         String msg = res.getMessage();
         if(msg.equals("rejected")){
           JOptionPane.showMessageDialog(null,res.getResponse()+" rejected your file transfer request");   
         }
         else if(msg.equals("accepted")) {
            ShareWindow.setInfo(res.getResponse()+" accepted file transfer request");
            ShareWindow.initiateFileTransfer();
         }
       }
       ShareWindow.setInfo((String)message);
    }
    
    @Override
    protected void ClientDisconnected(int clientID){
      ShareWindow.setInfo(host+" is offline or you have been disconnected"); 
      
    }
    
    @Override
    protected void ClientConnected(int clientID){
        try {
            ShareWindow.setInfo("You have been connected to: "+host);
            InetAddress address= InetAddress.getLocalHost();
            send(new Request("identity", address.getHostName()));
        } catch (UnknownHostException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
