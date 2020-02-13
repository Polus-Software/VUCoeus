/*
 * @(#)ResponderBean.java 1.0 8/13/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.brokers;

import edu.mit.coeus.exception.CoeusException;
import javax.swing.*;
import java.util.Vector;
import java.awt.Component;
import java.util.Hashtable;
// Added by Shivakumar for locking enhancement
import edu.mit.coeus.utils.locking.LockingBean;
/**
 * This bean class holder the form,toolbar and menubar need to be displayed on the applet
 * /
 * @version :1.0 August 13, 2002, 12:45 PM
 * @author Guptha K
 *
 */
public class ResponderBean implements java.io.Serializable{
    
    //response status
    private boolean responseStatus;
    //message to display on applet
    private String message;
    
    private Object dataObject;
    
    private Vector dataObjects;
    
    private Exception exception;
    
    private String id;
    
    private boolean closeRequired;
    
    private boolean locked;
    
    private Hashtable authorizationOperators;  
    
    // Added by Shivakumar for locking enhancement
    private Object lockingBean;

    private byte[] authSignature;
    
    private String authKey;
    //JIRA COEUSQA 2527 - START
    private String sessionId;
    //JIRA COEUSQA 2527 - END
    //Added by UCSD - rdias for coeus personalization
    private Object persnXMLObject = null;    

    private Object parameterValue;

    public Object getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(Object parameterValue) {
        this.parameterValue = parameterValue;
    }

  

    
    /**
     * Default constructor
     */
    public ResponderBean(){
    }
    //setters
    
    /**
     * This method is used to set the Data Object.
     * @param dataObject  DataObject
     */    
    public void setDataObject(Object dataObject){
        this.dataObject = dataObject;
    }
    /**
     * This method is used to set the Exception.
     * @param ex Exception
     */    
    public void setException(Exception ex){
        this.exception = ex;
    }
    
    /**
     * This method is used to set the Id.
     * @param id  String value
     */    
    public void setId(String id){
        this.id = id;
    }
    /**
     * This method is used to set collection of Data Objects.
     * @param dataObjects  collection of Data Objects.
     */  
    public void setDataObjects(Vector dataObjects){
        this.dataObjects = dataObjects;
    }
    
    /**
     * This methos is used to set the Response Status.
     * 
     * @param responseStatus  True if successful else False
     */    
    public void setResponseStatus(boolean responseStatus){
        this.responseStatus = responseStatus;
    }
    
    /**
     * This method is used to set the Message.
     * @param message String value
     */    
    public void setMessage(String message){
        this.message = message;
    }
    
    /**
     ** This method is used to get the Data Object.
     * @return  Object Data Object
     */ 
    public Object getDataObject(){
        return dataObject;
    }
    /**
     * This method is used to set the Exception.
     * @return Exception exception
     */ 
    public Exception getException(){
        return exception;
    }
     /**
     * This method will fetch the ID.
     * @return  String id
     */ 
    public String getId(){
        return id;
    }
    /**
     * This method is used to get collection of Data Objects.
     * @return Vector  collection of Data Objects.
     */  
    public Vector getDataObjects(){
        return dataObjects;
    }
    
    /**
     * This method is used to check the response operation value.
     * @return  boolean True if successful else False
     * @deprecated As of Coeus 4.0a,
     * replaced by <code>hasResponse()</code> which throws exception if the response is false
     */    
    public boolean isSuccessfulResponse(){
        return responseStatus;
    }
    
    /**
     * This method is used to check the response operation value.
     * @return  boolean True if successful else throws <code>CoeusException</code>
     *
     */
    public boolean hasResponse() throws CoeusException{
        if(!responseStatus){
            throw new CoeusException(message);
        }
        return responseStatus;
    }    
    /**
     * This method is used to get the Message.
     * @return String message
     */    
    public String getMessage(){
        return message;
    }
    
    /**
     * This methos is used to set the close required flag.
     * @param closeRequired  True if close required else False
     */    
    public void setCloseRequired(boolean closeRequired){
        this.closeRequired = closeRequired;
    }
    
    /**
     * This method is used to check the close Required flag.
     * @return boolean True if close required else False
     */    
    public boolean isCloseRequired(){
        return this.closeRequired;
    }

    /** 
     * This method is used to set the Lock.
     * @param locked boolean value
     */    
    public void setLocked(boolean locked){
        this.locked = locked;
    }
    
    /**
     * This method is used to check Lock status.
     * @return boolean true if locked else False
     */    
    public boolean isLocked(){
        return this.locked;
    }
    
    /** Getter for property authorizationOperators.
     * @return Value of property authorizationOperators.
     *
     */
    public java.util.Hashtable getAuthorizationOperators() {
        return authorizationOperators;
    }
    
    /** Setter for property authorizationOperators.
     * @param authorizationOperators New value of property authorizationOperators.
     *
     */
    public void setAuthorizationOperators(java.util.Hashtable authorizationOperators) {
        this.authorizationOperators = authorizationOperators;
    }
    
    /** Getter for property lockingBean.
     * @return Value of property lockingBean.
     *
     */
    public java.lang.Object getLockingBean() {
        return lockingBean;
    }
    
    /** Setter for property lockingBean.
     * @param lockingBean New value of property lockingBean.
     *
     */
    public void setLockingBean(java.lang.Object lockingBean) {
        this.lockingBean = lockingBean;
    }
    
    /**
     * Getter for property authSignature.
     * @return Value of property authSignature.
     */
    public byte[] getAuthSignature() {
        return this.authSignature;
    }    
    
    /**
     * Setter for property authSignature.
     * @param authSignature New value of property authSignature.
     */
    public void setAuthSignature(byte[] authSignature) {
        this.authSignature = authSignature;
    }    
    
    /**
     * Getter for property authKey.
     * @return Value of property authKey.
     */
    public java.lang.String getAuthKey() {
        return authKey;
    }
    
    /**
     * Setter for property authKey.
     * @param authKey New value of property authKey.
     */
    public void setAuthKey(java.lang.String authKey) {
        this.authKey = authKey;
    }

	/**
	 * @return the persnXMLObject
	 */
	public Object getPersnXMLObject() {
		return persnXMLObject;
	}

	/**
	 * @param persnXMLObject the persnXMLObject to set
	 */
	public void setPersnXMLObject(Object persnXMLObject) {
		this.persnXMLObject = persnXMLObject;
	}

    /**
     * @return the sessionId
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId the sessionId to set
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    
    
}
