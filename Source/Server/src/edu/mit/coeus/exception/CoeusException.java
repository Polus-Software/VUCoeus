/*
 * @(#)CoeusException.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on April 2, 2002, 12:16 PM
 */

package edu.mit.coeus.exception;

/**
 *
 * @author Geo Thomas
 * @version 1.0
 * This Class is an Exception class, which is used to represnt any exception comes
 * in COEUS web module. Exception handling  has done in COEUS web module by make use of CoeusException
 * class. If any exception comes in any class, handle that exception from the beans and servlets
 * and throw this exception instance to the ErrorPage.
 * <li>There are two ways to throw CoeusException
 *  <li>1)Throw an exceptioncode with the syntax of exceptionCode.<<errorNumber>>
 *  <li>2)Throw a normal error message.
 */
public class CoeusException extends java.lang.Exception {
    
    private String errorMessage;
    private int errorId;
    // added by manoj to classify the message type(warning/error/information)
    private int messageType;
    /**
     * Creates new <code>CoeusException</code> without detail message.
     */
    public CoeusException() { 
    }
    
    public CoeusException(Exception ex) { 
        super(ex);
    }
    
    /**
     * Constructs an <code>CoeusException</code> with the specified detail message.
     * @param msg errorMessage the detail message.
     */
    public CoeusException(String msg) {
        super(msg);
        this.errorMessage = msg;
    }
    public CoeusException(String msg,int messageType){
        this.errorMessage = msg;
        this.messageType = messageType;
    }
    
    /**
     *  Get Error Message
     *  @returns String Error Message
     */
    public String getMessage(){
        return errorMessage;
    }
    
    /**
     *  Set Error Message
     *  @param msg String Error Message
     */
    public void setMessage(String msg){
        this.errorMessage = msg;
    }

    /**
     * This Method is used to get the User Assigned Message.
     * @return String user defined erronous message
     */
    public String getUserMessage() {
        return errorMessage;        
    }
    
    /**
     * This method is used to get the Error ID.
     * @return int Error ID.
     */
    public int getErrorId() {
        int index=0;
        if(errorMessage!=null && ((index =  errorMessage.indexOf("exceptionCode"))!=-1)){
            try{
                errorId = Integer.parseInt(errorMessage.substring((index+"exceptionCode".length()+1),errorMessage.length()));
            }catch(java.lang.NumberFormatException ex){}
        }
        return errorId;
    }
    
    /** Getter for property messageType.
     * @return Value of property messageType.
     */
    public int getMessageType() {
        return messageType;
    }
    
    /** Setter for property messageType.
     * @param messageType New value of property errorType.
     */
    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
    
}