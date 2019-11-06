/*
 * @(#)MailInfoBean.java 1.0 24/07/03
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.bean;

/**
 * This class contain the Mail Information like personID, Name, User Id,
 *  Role Id, Email Id and so forth. It provides
 * the acessor methods to get/set the person attributes/parameters.
 *
 * @version 1.0 July 24, 2003, 12:27 AM
 * @author  Prasanna Kumar K
 */
public class MailInfoBean implements java.io.Serializable{
    //holds person id
    private String personId;
    //holds full Person name
    private String personName;
    //holds email
    private String emailId;
    //holds Role Name
    private String roleName;
    //holds MIT flag
    private boolean nonEmployeeFlag;
    //holds Update user
    private String updateUser;
    //holds Update Timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds AcType
    private String acType;

    /** Creates new MailInfoBean */
    public MailInfoBean() {
    }
    /**
     *  Get Person Id
     *  @return String Person Id
     */
    public String getPersonID() {
        return personId;
    }
    /**
     *  Set Person Id
     *  @param personId String Person Id
     */
    public void setPersonID(String personId) {
        this.personId = personId;
    }
    /**
     *  Get Person Name
     *  @return String Full Name
     */
    public String getPersonName() {
        return personName;
    }
    /**
     *  Set Person Name
     *  @param personName String represent Full Name
     */
    public void setPersonName(String personName) {
        this.personName = personName;
    }
    /**
     *  Get EmailId
     *  @return String EmailId
     */
    public String getEmailId() {
        return emailId;
    }
    /**
     *  Set Email
     *  @param emailId String EmailId
     */
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
    
    /** Getter for property roleName.
     * @return Value of property roleName.
     */
    public java.lang.String getRoleName() {
        return roleName;
    }
    
    /** Setter for property roleName.
     * @param roleName New value of property roleName.
     */
    public void setRoleName(java.lang.String roleName) {
        this.roleName = roleName;
    }
    
    /** Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /** Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }    
    
    /** Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /** Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }            
    
    /** Getter for property acType.
     * @return Value of property acType.
     */
    public java.lang.String getAcType() {
        return acType;
    }
    
    /** Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
    }
    
    /** Getter for property nonEmployeeFlag.
     * @return Value of property nonEmployeeFlag.
     */
    public boolean getNonEmployeeFlag() {
        return nonEmployeeFlag;
    }    

    /** Setter for property nonEmployeeFlag.
     * @param nonEmployeeFlag New value of property nonEmployeeFlag.
     */
    public void setNonEmployeeFlag(boolean nonEmployeeFlag) {
        this.nonEmployeeFlag = nonEmployeeFlag;
    }    
}