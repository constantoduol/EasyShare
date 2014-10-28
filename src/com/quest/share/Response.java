package com.quest.share;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author constant oduol
 * @version 1.0(26/5/12)
 */

/**
 * This file defines a response. Responses are sent by servers or clients in response to previous requests
 * 
 * 
 */
public class Response implements Serializable{
    private Object response;
    private Date date;
    private String msg;
    
    /**
     * this constructs a response object, a response object can be constructed either by a server or a client in
     * response to a request
     * @param response this object represents the result the server or client sends in response to a request
     * @param message  this is the message in the request that this response is responding to
     */
    public Response(Object response, String message){
       this.response=response;
       this.date=new Date();
       this.msg=message;
    }
    
    /**
     * this constructs a response object, a response object can be constructed either by a server or a client in
     * response to a request
     * @param message  this is the message in the request that this response is responding to 
     */
    public Response(String message){
        this(null,message);
    }
   
    /**
     * this method returns the object response that was wrapped in this response object by the server or client
     */
    public Object getResponse(){
        return this.response;
    }
    
    /**
     * this method returns the time when the response was made by the server to the client
     * or by the client to the server
     */
    public Date getResponseTime(){
        return this.date;
    }
    
    /**
     * this method returns the message this response is all about, the message could be telling the client to login
     * or a message in response to previous request
     */
    public String getMessage(){
      return this.msg;
   }

}