/*
 * LockBean.java
 *
 * Created on August 7, 2006, 4:49 PM
 */

package edu.mit.coeuslite.utils;

import java.sql.Timestamp;

/**
 *
 * @author  chandrashekara
 */
public class LockBean implements java.io.Serializable{
    private String moduleKey;
    private String lockId;
    private String unitNumber;
    private String moduleUnitNumber;
    private String userId;
    private String userName;
    private Timestamp updateTimestamp;
    private String moduleNumber;
    private String mode;
    private String sessionId;
    /** Creates a new instance of LockBean */
    public LockBean() {
    }
    
    /**
     * Getter for property moduleKey.
     * @return Value of property moduleKey.
     */
    public java.lang.String getModuleKey() {
        return moduleKey;
    }
    
    /**
     * Setter for property moduleKey.
     * @param moduleKey New value of property moduleKey.
     */
    public void setModuleKey(java.lang.String moduleKey) {
        this.moduleKey = moduleKey;
    }
    
    /**
     * Getter for property lockId.
     * @return Value of property lockId.
     */
    public java.lang.String getLockId() {
        return lockId;
    }
    
    /**
     * Setter for property lockId.
     * @param lockId New value of property lockId.
     */
    public void setLockId(java.lang.String lockId) {
        this.lockId = lockId;
    }
    
    /**
     * Getter for property unitNumber.
     * @return Value of property unitNumber.
     */
    public java.lang.String getUnitNumber() {
        return unitNumber;
    }
    
    /**
     * Setter for property unitNumber.
     * @param unitNumber New value of property unitNumber.
     */
    public void setUnitNumber(java.lang.String unitNumber) {
        this.unitNumber = unitNumber;
    }
    
    /**
     * Getter for property moduleUnitNumber.
     * @return Value of property moduleUnitNumber.
     */
    public java.lang.String getModuleUnitNumber() {
        return moduleUnitNumber;
    }
    
    /**
     * Setter for property moduleUnitNumber.
     * @param moduleUnitNumber New value of property moduleUnitNumber.
     */
    public void setModuleUnitNumber(java.lang.String moduleUnitNumber) {
        this.moduleUnitNumber = moduleUnitNumber;
    }
    
    /**
     * Getter for property userId.
     * @return Value of property userId.
     */
    public java.lang.String getUserId() {
        return userId;
    }
    
    /**
     * Setter for property userId.
     * @param userId New value of property userId.
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }
    
    /**
     * Getter for property userName.
     * @return Value of property userName.
     */
    public java.lang.String getUserName() {
        return userName;
    }
    
    /**
     * Setter for property userName.
     * @param userName New value of property userName.
     */
    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }
    
    /**
     * Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /**
     * Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /**
     * Getter for property moduleNumber.
     * @return Value of property moduleNumber.
     */
    public java.lang.String getModuleNumber() {
        return moduleNumber;
    }
    
    /**
     * Setter for property moduleNumber.
     * @param moduleNumber New value of property moduleNumber.
     */
    public void setModuleNumber(java.lang.String moduleNumber) {
        this.moduleNumber = moduleNumber;
    }
    
    /**
     * Getter for property mode.
     * @return Value of property mode.
     */
    public java.lang.String getMode() {
        return mode;
    }
    
    /**
     * Setter for property mode.
     * @param mode New value of property mode.
     */
    public void setMode(java.lang.String mode) {
        this.mode = mode;
    }
    
    public String toString(){
        StringBuffer strBffr = new StringBuffer("");
        strBffr.append("Module Key  =>"+this.getModuleKey());
        strBffr.append("Module Mode   =>"+this.getMode());
        strBffr.append("Lock Id  =>"+this.getLockId());
        strBffr.append("Module Number  =>"+this.getModuleNumber());
        strBffr.append("Module Unit Number  =>"+this.getModuleUnitNumber());
        strBffr.append("Unit Number  =>"+this.getUnitNumber());
        strBffr.append("User Id  =>"+this.getUserId());
        return strBffr.toString();
    }
    
    /**
     * Getter for property sessionId.
     * @return Value of property sessionId.
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }
    
    /**
     * Setter for property sessionId.
     * @param sessionId New value of property sessionId.
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }
    
}
