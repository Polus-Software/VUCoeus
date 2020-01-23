/*
 * @(#)RequestBean.java 1.0 8/13/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.brokers;


import java.util.Hashtable;
import java.util.Vector;
import edu.mit.coeus.utils.CoeusVector;

/**
 * This class used to send the requests to the servlet. It is a serialized object
 * that contain setter and getter methods of requests.
 *
 * @version :1.0 August 13,2002
 * @author Guptha K
 */
public class RequesterBean implements java.io.Serializable {

    private char functionType; // A - add, D - display, M - Modify
    private String id; // in case of Modify/Display sendunit id to servlet to fetch information from db
    private String requestedForm;  // inform servlet the requested form name
    private Hashtable params;
    private Object dataObject;
    private Vector dataObjects;
    private String userName; //for storing name of logged in user
    //Added for Authorization enhancement - Prasanna
    private boolean isAuthorizationRequired;
    private CoeusVector servletNames;
    private Hashtable authorizationOperators;    
    private byte[] authSignature;
    private String authKey;
    //JIRA COEUSQA 2527 - START
    private String sessionId;
    //JIRA COEUSQA 2527 - END
    //Added by UCSD - rdias for coeus personalization
    private Object persnXMLObject = null;    
    private Object ParameterValue;

    public Object getParameterValue() {
        return ParameterValue;
    }

    public void setParameterValue(Object ParameterValue) {
        this.ParameterValue = ParameterValue;
    }

    

    /**
     * Default constructor
     */
    public RequesterBean(){
    }
    
    /**
     * This method is used to set the Function Type
     * @param functionType character value
     */    
    public void setFunctionType(char functionType){
        this.functionType = functionType;
    }
    
    /**
     * This method is used to set the Id.
     * @param id  String value
     */    
    public void setId(String id){
        this.id = id;
    }
    /**
     * This method is used to set the Requested Form.
     *
     * @param requestedForm  String
     */    
    public void setRequestedForm(String requestedForm){
        this.requestedForm = requestedForm;
    }
    //getters
    /**
     * This method is used to get the Function Type.
     * @return  char Function Type.
     */    
    public char getFunctionType(){
        return functionType;
    }
    /**
     * This method will fetch the ID.
     * @return  String id
     */    
    public String getId(){
        return id;
    }
    /**
     * This method is used to get the RequestedForm.
     * @return  String RequestedForm
     */    
    public String getRequestedForm(){
        return requestedForm;
    }
    /**
     * This method is used to set the Request Parameters.
     * @param inparams collection of Request parameters
     */    
    public void setParams(Hashtable inparams){
        params = inparams;
    }
    /**
     * This method is used set the Request Parameters.
     * @return  Hashtable collection of Request parameters 
     */    
    public Hashtable getParams(){
        return params;
    }
    
    /**
     * This method is used to set the Data Object.
     * @param inDataObject  DataObject
     */    
    public void setDataObject(Object inDataObject){
        dataObject = inDataObject;
    }
    
    /**
     ** This method is used to get the Data Object.
     * @return  Object Data Object
     */    
    public Object getDataObject(){
        return dataObject;
    }

    /**
     * This method is used to set collection of Data Objects.
     * @param inDataObjects  collection of Data Objects.
     */    
    public void setDataObjects(Vector inDataObjects){
        dataObjects = inDataObjects;
    }
    
    /**
     * This method is used to get collection of Data Objects.
     * @return Vector  collection of Data Objects.
     */    
    public Vector getDataObjects(){
        return dataObjects;
    }

    /**
     * This method is used to the set the user name.
     *
     * @param inUserName  user name
     */    
    public void setUserName(String inUserName){
        userName = inUserName;
    }
    
    /**
     * This method is used to get the user name.
     * @return  String user name
     */    
    public String getUserName(){
        return userName;
    }
    
    /** Getter for property isAuthorizationRequired.
     * @return Value of property isAuthorizationRequired.
     */
    public boolean isIsAuthorizationRequired() {
        return isAuthorizationRequired;
    }
    
    /** Setter for property isAuthorizationRequired.
     * @param isAuthorizationRequired New value of property isAuthorizationRequired.
     */
    public void setIsAuthorizationRequired(boolean isAuthorizationRequired) {
        this.isAuthorizationRequired = isAuthorizationRequired;
    }
    
    /** Getter for property servletNames.
     * @return Value of property servletNames.
     */
    public CoeusVector getServletNames() {
        return servletNames;
    }
    
    /** Setter for property servletNames.
     * @param servletNames New value of property servletNames.
     */
    public void setServletNames(CoeusVector servletNames) {
        this.servletNames = servletNames;
    }
    
    /** Getter for property authorizationOperators.
     * @return Value of property authorizationOperators.
     */
    public Hashtable getAuthorizationOperators() {
        return authorizationOperators;
    }
    
    /** Setter for property authorizationOperators.
     * @param authorizationOperators New value of property authorizationOperators.
     */
    public void setAuthorizationOperators(Hashtable authorizationOperators) {
        this.authorizationOperators = authorizationOperators;
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