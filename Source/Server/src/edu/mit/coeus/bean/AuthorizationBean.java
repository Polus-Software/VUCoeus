/*
 * @(#)AuthorizationBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.bean;

import edu.mit.coeus.bean.BaseBean;
/**
 * The class used to hold the information of <code>Authorization</code>
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on March 17, 2003, 12:26 PM
 */

public class AuthorizationBean implements java.io.Serializable, BaseBean {

    private String functionType; // ROLE or RIGHT or OSP
    private String function; //Role or Right Id
    private String qualifierType;//PROPOSAL or PROTOCOL or UNIT
    private String qualifier; //Proposal No. or Protocol No. or Unit No.
    private String person;// User Id
    
    /** Creates new AuthorizationBean */
    public AuthorizationBean() {
    }    

    /** Getter for property functionType.
     * @return Value of property functionType.
     */
    public String getFunctionType() {
        return functionType;
    }
    
    /** Setter for property functionType.
     * @param functionType New value of property functionType.
     */
    public void setFunctionType(String functionType) {
        this.functionType = functionType;
    }
    
    /** Getter for property function.
     * @return Value of property function.
     */
    public java.lang.String getFunction() {
        return function;
    }
    
    /** Setter for property function.
     * @param function New value of property function.
     */
    public void setFunction(java.lang.String function) {
        this.function = function;
    }
    
    /** Getter for property qualifierType.
     * @return Value of property qualifierType.
     */
    public String getQualifierType() {
        return qualifierType;
    }
    
    /** Setter for property qualifierType.
     * @param qualifierType New value of property qualifierType.
     */
    public void setQualifierType(String qualifierType) {
        this.qualifierType = qualifierType;
    }
    
    /** Getter for property qualifier.
     * @return Value of property qualifier.
     */
    public java.lang.String getQualifier() {
        return qualifier;
    }
    
    /** Setter for property qualifier.
     * @param qualifier New value of property qualifier.
     */
    public void setQualifier(java.lang.String qualifier) {
        this.qualifier = qualifier;
    }
    
    /** Getter for property person.
     * @return Value of property person.
     */
    public java.lang.String getPerson() {
        return person;
    }
    
    /** Setter for property person.
     * @param person New value of property person.
     */
    public void setPerson(java.lang.String person) {
        this.person = person;
    }
}