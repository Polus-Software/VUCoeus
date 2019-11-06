/*
 * LockingBean.java
 *
 * Created on July 22, 2004, 2:29 PM
 */

package edu.mit.coeus.utils.locking;

import edu.mit.coeus.bean.BaseBean;
/**
 *
 * @author  shivakumarmj
 */
public class LockingBean implements java.io.Serializable,BaseBean{
   
    private String moduleName;
    
    private String key1;
    
    private String key2;
    
    private String userID;
    
    private String unitNumber;
    
    private String unitName;
    
    private java.sql.Timestamp createTimestamp;
    
    private java.sql.Timestamp updateTimestamp;
    
    private String updateUser;
    
    private String errMessage;
    
    private String lockID;
    
    private String updateID;
    
    private boolean gotLock;
    
    //Added for useid to username enhancement
    private String updateUserName;
    
    //Adding since the the lock ID has to be deleted from Hashtable at the client 
    //side after he closes the form. 01-09-2004
    private String acType;
    
    /** Getter for property moduleName.
     * @return Value of property moduleName.
     *
     */
    public java.lang.String getModuleName() {
        return moduleName;
    }    
    
    /** Setter for property moduleName.
     * @param moduleName New value of property moduleName.
     *
     */
    public void setModuleName(java.lang.String moduleName) {
        this.moduleName = moduleName;
    }    
    
    /** Getter for property key1.
     * @return Value of property key1.
     *
     */
    public java.lang.String getKey1() {
        return key1;
    }    
    
    /** Setter for property key1.
     * @param key1 New value of property key1.
     *
     */
    public void setKey1(java.lang.String key1) {
        this.key1 = key1;
    }    
    
    /** Getter for property key2.
     * @return Value of property key2.
     *
     */
    public java.lang.String getKey2() {
        return key2;
    }    
    
    /** Setter for property key2.
     * @param key2 New value of property key2.
     *
     */
    public void setKey2(java.lang.String key2) {
        this.key2 = key2;
    }    
    
    /** Getter for property userID.
     * @return Value of property userID.
     *
     */
    public java.lang.String getUserID() {
        return userID;
    }    
    
    /** Setter for property userID.
     * @param userID New value of property userID.
     *
     */
    public void setUserID(java.lang.String userID) {
        this.userID = userID;
    }
    
    /** Getter for property unitNumber.
     * @return Value of property unitNumber.
     *
     */
    public java.lang.String getUnitNumber() {
        return unitNumber;
    }
    
    /** Setter for property unitNumber.
     * @param unitNo New value of property unitNumber.
     *
     */
    public void setUnitNumber(java.lang.String unitNumber) {
        this.unitNumber = unitNumber;
    }
    
    /** Getter for property unitName.
     * @return Value of property unitName.
     *
     */
    public java.lang.String getUnitName() {
        return unitName;
    }
    
    /** Setter for property unitName.
     * @param unitName New value of property unitName.
     *
     */
    public void setUnitName(java.lang.String unitName) {
        this.unitName = unitName;
    }
    
    /** Getter for property createTimestamp.
     * @return Value of property createTimestamp.
     *
     */
    public java.sql.Timestamp getCreateTimestamp() {
        return createTimestamp;
    }
    
    /** Setter for property createTimestamp.
     * @param createTimestamp New value of property createTimestamp.
     *
     */
    public void setCreateTimestamp(java.sql.Timestamp createTimestamp) {
        this.createTimestamp = createTimestamp;
    }
    
    /** Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     *
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /** Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     *
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /** Getter for property updateUser.
     * @return Value of property updateUser.
     *
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /** Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     *
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }        
    
    
    /** Getter for property lockID.
     * @return Value of property lockID.
     *
     */
    public java.lang.String getLockID() {
        return lockID;
    }
    
    /** Setter for property lockID.
     * @param lockID New value of property lockID.
     *
     */
    public void setLockID(java.lang.String lockID) {
        this.lockID = lockID;
    }
//    
//    /** Getter for property gotLock.
//     * @return Value of property gotLock.
//     *
//     */
//    public int getGotLock() {
//        return gotLock;
//    }
    
    /** Setter for property gotLock.
     * @param gotLock New value of property gotLock.
     *
     */
//    public void setGotLock(int gotLock) {
//        this.gotLock = gotLock;
//    }
    
    /** Getter for property updateID.
     * @return Value of property updateID.
     *
     */
    public java.lang.String getUpdateID() {
        return updateID;
    }
    
    /** Setter for property updateID.
     * @param updateID New value of property updateID.
     *
     */
    public void setUpdateID(java.lang.String updateID) {
        this.updateID = updateID;
    }
    
    /** Getter for property gotLock.
     * @return Value of property gotLock.
     *
     */
    public boolean isGotLock() {
        return gotLock;
    }
    
    /** Setter for property gotLock.
     * @param gotLock New value of property gotLock.
     *
     */
    public void setGotLock(boolean gotLock) {
        this.gotLock = gotLock;
    }
    
    /** Getter for property errMessage.
     * @return Value of property errMessage.
     *
     */
    public java.lang.String getErrMessage() {
        return errMessage;
    }
    
    /** Setter for property errMessage.
     * @param errMessage New value of property errMessage.
     *
     */
    public void setErrMessage(java.lang.String errMessage) {
        this.errMessage = errMessage;
    }
    
    /** Getter for property acType.
     * @return Value of property acType.
     *
     */
    public java.lang.String getAcType() {
        return acType;
    }
    
    /** Setter for property acType.
     * @param acType New value of property acType.
     *
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
    }
    
    //Useid to username enhancement - Start
    /** Getter for property updateUserName.
     * @return Value of property updateUserName.
     *
     */
    public java.lang.String getUpdateUserName() {
        return updateUserName;
    }
    
    /** Setter for property updateUserName.
     * @param updateUserName New value of property updateUserName.
     *
     */
    public void setUpdateUserName(java.lang.String updateUserName) {
        this.updateUserName = updateUserName;
    }        
    //Useid to username enhancement - End
}
