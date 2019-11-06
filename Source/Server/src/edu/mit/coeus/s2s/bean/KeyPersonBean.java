/*
 * @(#)KeyPersonBean.java 
 *
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.bean;

import edu.mit.coeus.bean.CoeusBean;

import java.beans.*;
import java.math.BigDecimal;
import java.util.Vector;
 

public class KeyPersonBean extends CompensationBean{
 
//    private String sortId = null;
    private int sortId = 0;
    private String personId = null;
    private String lastName = null;
    private String firstName = null;
    private String middleName = null;
    private String prefix = null;
    private String suffix = null;
      
    private String role = null;
    private boolean nonMITPersonFlag;
 
   
    
    private String updateUser = null;
    //holds update timestamp - needed to implement CoeusBean
    private java.sql.Timestamp updateTimestamp = null;
    //holds account type - needed to implement CoeusBean
    private String acType = null;

   
 
    /**
     *	Default Constructor
     */
    
    public KeyPersonBean(){
    }
      
    /** Getter for property sortId.
     * @return Value of property sortId.
     
    public java.lang.String getSortId() {
        return sortId;
    }
    
    /** Setter for property sortId.
     * @param personId New value of property sortId.
     
    public void setSortId(java.lang.String sortId) {
        this.sortId = sortId;
    }
    */
    
     /** Getter for property sortId.
     * @return Value of property sortId.
     */
    public int getSortId() {
        return sortId;
    }
    
    /** Setter for property sortId.
     * @param personId New value of property sortId.
     */
    public void setSortId(int sortId) {
        this.sortId = sortId;
    }
      
    /** Getter for property personId.
     * @return Value of property personId.
     */
    public java.lang.String getPersonId() {
        return personId;
    }
    
    /** Setter for property personId.
     * @param personId New value of property personId.
     */
    public void setPersonId(java.lang.String personId) {
        this.personId = personId;
    }
    
    
     /** Getter for property lastName.
     * @return Value of property lastName.
     */
    public java.lang.String getLastName() {
        return lastName;
    }
    
    /** Setter for property lastName.
     * @param lastName New value of property lastName.
     */
    public void setLastName(java.lang.String lastName) {
        this.lastName = lastName;
    }
    
     /** Getter for property firstName.
     * @return Value of property firstName.
     */
    public java.lang.String getFirstName() {
        return firstName;
    }
    
    /** Setter for property firstName.
     * @param firstName New value of property firstName.
     */
    public void setFirstName(java.lang.String firstName) {
        this.firstName = firstName;
    }
    
    /** Getter for property middleName.
     * @return Value of property middleName.
     */
    public java.lang.String getMiddleName() {
        return middleName;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     */
    public void setMiddleName(java.lang.String middleName) {
        this.middleName = middleName;
    }
       
   
    /** Getter for property role.
     * @return Value of property role.
     */
    public java.lang.String getRole() {
        return role;
    }
    
    /** Setter for property role.
     * @param role New value of property role.
     */
    public void setRole(java.lang.String role) {
        this.role = role;
    }
    
    /** Setter for nonMITPersonFlag.
     * @return nonMITPersonFlag
     */
    public boolean getNonMITPersonFlag(){
        return nonMITPersonFlag;
    }
    
    /** Getter for nonMITPersonFlag
     * @param nonMITPersonFlag
     */
    public void setNonMITPersonFlag(boolean nonMITPersonFlag){
        this.nonMITPersonFlag = nonMITPersonFlag;
    }
    
    

    
}