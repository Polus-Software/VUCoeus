/*
 * @(#)CheckListBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on 27th August, 2003
 * @author Prasanna Kumar
 * @version 1.0
 */
package edu.mit.coeus.bean;


/**
 * The Class is used to populate any Check List
 */
public class CheckListBean implements java.io.Serializable{
    private String checkListCode;
    private String description;
    private boolean isChecked; 
    
    /** Default Constructor */
    public CheckListBean() {
    }
    
    /** Constructor with parameters */
    public CheckListBean(String checkListCode, String description) {
        this.checkListCode = checkListCode;
        this.description = description;
    }
    
    /** Getter for property checkListCode.
     * @return Value of property checkListCode.
     */
    public String getCheckListCode() {
        return checkListCode;
    }
    
    /** Setter for property checkListCode.
     * @param checkListCode New value of property checkListCode.
     */
    public void setCheckListCode(String checkListCode) {
        this.checkListCode = checkListCode;
    }
    
    /** Getter for property description.
     * @return Value of property description.
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /** Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    /** Getter for property isChecked.
     * @return Value of property isChecked.
     */
    public boolean isChecked() {
        return isChecked;
    }    

    /** Setter for property isChecked.
     * @param isChecked New value of property isChecked.
     */
    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }    
    
}