/*
 * ProtocolLinkBean.java
 *
 * Created on September 1, 2005, 2:35 PM
 */

package edu.mit.coeus.iacuc.bean;

/**
 *
 * @author  surekhan
 */
public class ProtocolLinkBean {
    //holds protocolNumber
    private String protocolNumber;
    //holds update protocol sequence Number
    private int sequenceNumber;
    //holds the modulecode
    private int moduleCode;
    //holds the item number i.e awrad or proposal number
    private String moduleItemKey;
    //holds the moduleItemSeqNumberi.e awrad or proposal  sequence number
    private int moduleItemSeqNumber;
    //holds the action i.e inserted or deleted
    private String actionType;
    //holds comments 
    private String comments;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp; 
    
    
    
    /** Creates a new instance of ProtocolLinkBean */
    public ProtocolLinkBean() {
    }
    
    /**
     * Getter for property protocolNumber.
     * @return Value of property protocolNumber.
     */
    public java.lang.String getProtocolNumber() {
        return protocolNumber;
    }    
    
    /**
     * Setter for property protocolNumber.
     * @param protocolNumber New value of property protocolNumber.
     */
    public void setProtocolNumber(java.lang.String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }    
    
    /**
     * Getter for property sequenceNumber.
     * @return Value of property sequenceNumber.
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    
    /**
     * Setter for property sequenceNumber.
     * @param sequenceNumber New value of property sequenceNumber.
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
    /**
     * Getter for property moduleCode.
     * @return Value of property moduleCode.
     */
    public int getModuleCode() {
        return moduleCode;
    }
    
    /**
     * Setter for property moduleCode.
     * @param moduleCode New value of property moduleCode.
     */
    public void setModuleCode(int moduleCode) {
        this.moduleCode = moduleCode;
    }
    
    /**
     * Getter for property moduleItemKey.
     * @return Value of property moduleItemKey.
     */
    public java.lang.String getModuleItemKey() {
        return moduleItemKey;
    }
    
    /**
     * Setter for property moduleItemKey.
     * @param moduleItemKey New value of property moduleItemKey.
     */
    public void setModuleItemKey(java.lang.String moduleItemKey) {
        this.moduleItemKey = moduleItemKey;
    }
    
    /**
     * Getter for property moduleItemSeqNumber.
     * @return Value of property moduleItemSeqNumber.
     */
    public int getModuleItemSeqNumber() {
        return moduleItemSeqNumber;
    }
    
    /**
     * Setter for property moduleItemSeqNumber.
     * @param moduleItemSeqNumber New value of property moduleItemSeqNumber.
     */
    public void setModuleItemSeqNumber(int moduleItemSeqNumber) {
        this.moduleItemSeqNumber = moduleItemSeqNumber;
    }
    
    /**
     * Getter for property actionType.
     * @return Value of property actionType.
     */
    public java.lang.String getActionType() {
        return actionType;
    }
    
    /**
     * Setter for property actionType.
     * @param actionType New value of property actionType.
     */
    public void setActionType(java.lang.String actionType) {
        this.actionType = actionType;
    }
    
    /**
     * Getter for property comments.
     * @return Value of property comments.
     */
    public java.lang.String getComments() {
        return comments;
    }
    
    /**
     * Setter for property comments.
     * @param comments New value of property comments.
     */
    public void setComments(java.lang.String comments) {
        this.comments = comments;
    }
    
    /**
     * Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /**
     * Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
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
    
}
