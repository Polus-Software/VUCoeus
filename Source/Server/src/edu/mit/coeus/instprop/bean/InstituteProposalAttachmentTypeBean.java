/*
 * InstituteProposalAttachmentTypeBean.java
 *
 * Created on May 27, 2005, 12:11 PM
 */

package edu.mit.coeus.instprop.bean;

import java.sql.Timestamp;

/**
 *
 * @author  surekhan
 */
public class InstituteProposalAttachmentTypeBean implements java.io.Serializable{
    //holds  attachmentTypeCode
    private int attachmentTypeCode;
    
    //holds  description
    private String description;
    
    //holds allowMultiple value
    private String allowMultiple;
    
    //holds update user id
     private String updateUser;
     
    //holds update timestamp
     private java.sql.Timestamp updateTimestamp;
    
    
    /**
     * Creates a new instance of InstituteProposalAttachmentTypeBean
     */
    public InstituteProposalAttachmentTypeBean() {
    }
    
    /**
     * Getter for property attachmentTypeCode.
     * 
     * @return Value of property attachmentTypeCode.
     */
    public int getAttachmentTypeCode() {
        return attachmentTypeCode;
    }    
    
    /**
     * Setter for property attachmentTypeCode.
     * 
     * @param attachmentTypeCode New value of property attachmentTypeCode.
     */
    public void setAttachmentTypeCode(int narrativeTypeCode) {
        this.attachmentTypeCode = narrativeTypeCode;
    }    
    
    /**
     * Getter for property description.
     * @return Value of property description.
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /**
     * Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    /**
     * Getter for property allowMultiple.
     * @return Value of property allowMultiple.
     */
    public java.lang.String getAllowMultiple() {
        return allowMultiple;
    }
    
    /**
     * Setter for property allowMultiple.
     * @param allowMultiple New value of property allowMultiple.
     */
    public void setAllowMultiple(java.lang.String allowMultiple) {
        this.allowMultiple = allowMultiple;
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
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /**
     * Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
}
