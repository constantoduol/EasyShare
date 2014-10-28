
package com.quest.share;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author constant oduol
 * @version 1.0(25/4/12)
 */

/**
 * A request is a way of communication between a client and a server
 * a server can send a request to a client and also a client can send a
 * request to a server. If an object is wrapped in a request, the object's class
 * has to implement the java.io.Serializable interface.A client or server does not 
 * have to respond to a request, it may carry out what the request requires but not
 * send a corresponding response
 */
public class Request implements Serializable {
    private Date date;
    private String msg;
    private Object obj;
    private String service;
    /**
     * this constructor constructs a request object which is used by the client or server to send a message to the server
     * or to the client
     * @param message this is the message the server or client want to exchange
     */
    public Request(String message){
       this.msg=message;
       this.date=new Date();
    }
    
    /**
     * constructs a request object to capable of sending an object to the server or client
     * the object's class should implement the serializable interface
     * @param message this is the message the server or client want to exchange
     * @param obj this is the object associated with this request, this happens when the server or client
     *        needs extra data to process a request
     */
    public Request(String message, Object obj){
        this(message);
        this.obj=obj;
    }
    
    /**
     * this constructor creates a request object capable of requesting for services on the server
     * @param service the name of the service we are sending a request for
     * @param message the string representing the sub service on the server, this string
     *                is used to choose which section of the service needs to be invoked
     * @param obj the object wrapped in this request object
     */
    public Request(String service, String message, Object obj){
      this(message,obj);
      this.service=service;
    }
    
    
    /**
     * this method returns the time this request was made by the client
     */
    public Date getRequestTime(){
        return this.date;
    }
    
    
     /**
      * this method returns the message associated with this request, for example the 
      * server could have sent a message to the client requiring it to login,then the client 
      * responds by sending the same message with the required details
      */
     public String getMessage(){
         return this.msg;
     }
     
     /**
      * this method returns the object wrapped in a request object
      * the method returns null if no object was wrapped in this request object
      */
     public Object getRequestObject(){
         return this.obj;
     }
     
     /**
      * this method returns the service this request is associated with
      */
     public String getService(){
         return this.service;
     }
}
