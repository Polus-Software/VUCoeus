/**
 * @(#)MessageBean.java 1.0 12/20/03
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.propdev.bean;

import java.sql.Timestamp;

/**
 * This class used to set and get Message details
  *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on December 20, 2003 4:30 PM
 */

public class MessageBean implements java.io.Serializable{
    
    private String messageId;
    private String message;
    private Timestamp updateTimeStamp;
    private String updateUser;
    private String acType;
    
    /**
     *	Default Constructor
     */
    
    public MessageBean(){
    }
    
    /** Getter for property messageId.
     * @return Value of property messageId.
     */
    public String getMessageId() {
        return messageId;
    }
    
    /** Setter for property messageId.
     * @param messageId New value of property messageId.
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    
    /** Getter for property message.
     * @return Value of property message.
     */
    public java.lang.String getMessage() {
        return message;
    }
    
    /** Setter for property message.
     * @param message New value of property message.
     */
    public void setMessage(java.lang.String message) {
        this.message = message;
    }
    
    /** Getter for property updateTimeStamp.
     * @return Value of property updateTimeStamp.
     */
    public java.sql.Timestamp getUpdateTimeStamp() {
        return updateTimeStamp;
    }
    
    /** Setter for property updateTimeStamp.
     * @param updateTimeStamp New value of property updateTimeStamp.
     */
    public void setUpdateTimeStamp(java.sql.Timestamp updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
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
}