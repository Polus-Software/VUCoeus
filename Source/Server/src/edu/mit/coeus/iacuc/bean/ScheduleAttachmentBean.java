/*
 * ScheduleAttachmentBean.java
 *
 * Created on November 29, 2011, 3:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.bean.CoeusAttachment;

/**
 *
 * @author manjunathabn
 */
public class ScheduleAttachmentBean implements java.io.Serializable,CoeusAttachment {
    
    /** Creates a new instance of ScheduleAttachmentBean */
    public ScheduleAttachmentBean() {
    }
    
    private String scheduleId;
    private int attachmentId;
    private int attachmentTypeCode;
    private String fileName;
    private String description;
    private byte[] attachment;
    private String mimeType;
    private String attachmentType;
    private String updateUser;
    private java.sql.Timestamp updateTimestamp;
    private String acType;
    
    /**
     * Method used to get the attachmentId
     * @return attachmentId
     */
    public int getAttachmentId() {
        return attachmentId;
    }
    
    /**
     * Method used to set the attachmentId
     * @param attachmentId
     */
    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
    }
    
    /**
     * Method used to get the attachmentTypeCode
     * @return attachmentTypeCode
     */
    public int getAttachmentTypeCode() {
        return attachmentTypeCode;
    }
    
    /**
     * Method used to set the attachmentTypeCode
     * @param attachmentTypeCode
     */
    public void setAttachmentTypeCode(int attachmentTypeCode) {
        this.attachmentTypeCode = attachmentTypeCode;
    }
    /**
     * Method used to get the fileName
     * @return fileName
     */
    
    public String getFileName() {
        return fileName;
    }
    
    /**
     * Method used to set the fileName
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    /**
     * Method used to get the description
     * @return description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Method used to set the description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Method used to get the attachment
     * @return attachment
     */
    public byte[] getAttachment() {
        return attachment;
    }
    
    /**
     * Method used to set the attachment
     * @param attachment
     */
    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }
    
    /**
     * Method used to get the mimeType
     * @return mimeType
     */
    public String getMimeType() {
        return mimeType;
    }
    
    /**
     * Method used to set the mimeType
     * @param mimeType
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    /**
     * Method used to get the attachmentType
     * @return attachmentType
     */
    
    public String getAttachmentType() {
        return attachmentType;
    }
    
    /**
     * Method used to set the attachmentType
     * @param attachmentType
     */
    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }
    /**
     * Method used to set the fileBytes
     * @param fileBytes
     */
    public void setFileBytes(byte[] fileBytes) {
        setAttachment(fileBytes);
    }
    
    /**
     * Method used to get the fileBytes
     * @return getAttachment() method
     */
    public byte[] getFileBytes() {
        return getAttachment();
    }

    public String getScheduleId() {
        return scheduleId;
    }
    
    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getAcType() {
        return acType;
    }

    public void setAcType(String acType) {
        this.acType = acType;
    }
    
}
