/*
 * UserInfoBean.java
 *
 * Created on August 28, 2002, 5:58 PM
 */

package edu.mit.coeus.bean;

import java.io.Serializable;
import java.sql.Timestamp;
/**
 * This class holds User Information like PersonID, UserID, UnitNumber. 
 * It is a data bean which provides acessor method to get and set the above 
 * parameters.
 * 
 * @author  geo
 * @version 1.0
 */
public class UserInfoBean implements java.io.Serializable{
   
    //holds person id
    private String personId;
    
    //holds person Name
    private String personName;
    
    //holds user id
    private String userId;
    //holds unit number
    private String unitNumber;
    
    private String userName;
    private boolean nonEmployee;
    private char userType;
    private char status;
    //holds aw Status
    private char aw_Status;
    private String unitName;
    private String emailId;
    
    private String acType;
    
    private String updateUser;
    private Timestamp updateTimestamp;
    private char descendFlag;
    
    /** Creates new UserInfoBean */
    public UserInfoBean() {
    }

    /**
     *  Get Person Id
     *  @return String Person Id
     */
    public String getPersonId() {
        return personId;
    }
    /**
     *  Set Person Id
     *  @param personId String Person Id
     */
    public void setPersonId(String personId) {
        this.personId = personId;
    }

    /**
     *  Get Person Name
     *  @return String Person Name
     */
    public String getPersonName() {
        return personName;
    }
    /**
     *  Set Person Name
     *  @param personId String Person Name
     */
    public void setPersonName(String personName) {
        this.personName = personName;
    }    
    
    /**
     *  Get User Id
     *  @return String user Id
     */
    public String getUserId() {
        return userId;
    }
    /**
     *  Set User Id
     *  @param userId String User Id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    /**
     *  Get Unit Number
     *  @return String Unit Number
     */
    public String getUnitNumber() {
        return unitNumber;
    }
    /**
     *  Set Unit Number
     *  @param unitNumber String Unit Number
     */
    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    /** Getter for property unitName.
     * @return Value of property unitName.
     */
    public java.lang.String getUnitName() {
        return unitName;
    }
    
    /** Setter for property unitName.
     * @param unitName New value of property unitName.
     */
    public void setUnitName(java.lang.String unitName) {
        this.unitName = unitName;
    }
    
    /** Getter for property status.
     * @return Value of property status.
     */
    public char getStatus() {
        return status;
    }
    
    /** Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(char status) {
        this.status = status;
    }
    
    /** Getter for property userType.
     * @return Value of property userType.
     */
    public char getUserType() {
        return userType;
    }
    
    /** Setter for property userType.
     * @param userType New value of property userType.
     */
    public void setUserType(char userType) {
        this.userType = userType;
    }
    
    /** Getter for property nonEmployee.
     * @return Value of property nonEmployee.
     */
    public boolean isNonEmployee() {
        return nonEmployee;
    }
    
    /** Setter for property nonEmployee.
     * @param nonEmployee New value of property nonEmployee.
     */
    public void setNonEmployee(boolean nonEmployee) {
        this.nonEmployee = nonEmployee;
    }
    
    /** Getter for property userName.
     * @return Value of property userName.
     */
    public java.lang.String getUserName() {
        return userName;
    }
    
    /** Setter for property userName.
     * @param userName New value of property userName.
     */
    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }
    
    /** Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /** Setter for property userName.
     * @param userName New value of property userName.
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }
    
        /** Getter for property updateTimeStamp.
     * @return Value of property updateTimeStamp.
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /** Setter for property updateTimeStamp.
     * @param userName New value of property updateTimeStamp.
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /** Getter for property emailId.
     * @return Value of property emailId.
     */
    public java.lang.String getEmailId() {
        return emailId;
    }
    
    /** Setter for property emailId.
     * @param emailId New value of property emailId.
     */
    public void setEmailId(java.lang.String emailId) {
        this.emailId = emailId;
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
    
    /** Getter for property descendFlag.
     * @return Value of property descendFlag.
     */
    public char getDescendFlag() {
        return descendFlag;
    }
    
    /** Setter for property descendFlag.
     * @param descendFlag New value of property descendFlag.
     */
    public void setDescendFlag(char descendFlag) {
        this.descendFlag = descendFlag;
    }    
    
    /** Getter for property aw_Status.
     * @return Value of property aw_Status.
     */
    public char getAw_Status() {
        return aw_Status;
    }
    
    /** Setter for property aw_Status.
     * @param aw_Status New value of property aw_Status.
     */
    public void setAw_Status(char aw_Status) {
        this.aw_Status = aw_Status;
    }
}
