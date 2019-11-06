/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * ReviewAttachmentsBean.java
 *
 * Created on April 5, 2011, 3:20 PM
 * 
 */

package edu.mit.coeus.irb.bean;

import edu.mit.coeus.bean.CoeusAttachment;
import edu.mit.coeus.bean.CoeusBean;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author divyasusendran
 */
public class ReviewAttachmentsBean implements Serializable,CoeusAttachment {
    
    
    private int sequenceNumber;
    private int submissionNumber;    
    private int attachmentNumber;    
    private String description;
    private String fileName;    
    private String protocolNumber;    
    private String acType;    
    private String personId;
    private String personName;
    private String canUserViewReviewerName;
    private String reviewerName;
    private String updateUser;
    private String updateUserName;
    private String createUser;
    private String createUserName;    
    private String mimeType;
    private String privateAttachmentFlag;
    private String finalAttachmentFlag;
    private Timestamp createTimestamp;
    private Timestamp updateTimestamp;
    private boolean userAdmin;    
    private byte[] attachment;
    
    /** Creates a new instance of ReviewAttachmentsBean */
    public ReviewAttachmentsBean() {
    }
    
    /**
     * Method used to get the sequenceNumber
     * @return sequenceNumber 
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    
    /**
     * Method used to set the sequenceNumber
     * @param sequenceNumber 
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
    /**
     * Method used to get the submissionNumber
     * @return submissionNumber 
     */
    public int getSubmissionNumber() {
        return submissionNumber;
    }
    
    /**
     * Method used to set the submissionNumber
     * @param submissionNumber 
     */
    public void setSubmissionNumber(int submissionNumber) {
        this.submissionNumber = submissionNumber;
    }
    
    /**
     * Method used to get the protocol number
     * @return protocolNumber 
     */
    public String getProtocolNumber() {
        return protocolNumber;
    }
    
    /**
     * Method used to set the protocol number
     * @param protocolNumber 
     */
    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }
    
    /**
     * Method used to get the reviewerName
     * @return reviewerName 
     */
    public String getReviewerName() {
        return reviewerName;
    }
    
     /**
     * Method used to set the reviewerName
     * @param reviewerName 
     */
    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }
    
    /**
     * Method used to get the personName
     * @return personName 
     */
    public String getPersonName() {
        return personName;
    }
    
    /**
     * Method used to set the personName
     * @param personName 
     */
    public void setPersonName(String personName) {
        this.personName = personName;
    }
    
    /**
     * Method used to get the updateUser
     * @return updateUser 
     */
    public String getUpdateUser() {
        return updateUser;
    }
    
    /**
     * Method used to set the updateUser
     * @param updateUser 
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    
    /**
     * Method used to get the acType
     * @return acType 
     */
    public String getAcType() {
        return acType;
    }
    
    /**
     * Method used to set the acType
     * @param acType 
     */
    public void setAcType(String acType) {
        this.acType = acType;
    }
    
    /**
     * Method used to get the personId
     * @return personId 
     */
    public String getPersonId() {
        return personId;
    }
    
    /**
     * Method used to set the personId
     * @param personId 
     */
    public void setPersonId(String personId) {
        this.personId = personId;
    }
    
    /**
     * Method used to get the updateUserName
     * @return updateUserName 
     */
    public String getUpdateUserName() {
        return updateUserName;
    }
    
    /**
     * Method used to set the updateUserName
     * @param updateUserName 
     */
    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }
    
    /**
     * Method used to get the canUserViewReviewerName
     * @return canUserViewReviewerName 
     */
    public String getCanUserViewReviewerName() {
        return canUserViewReviewerName;
    }
    
    /**
     * Method used to set the canUserViewReviewerName
     * @param canUserViewReviewerName 
     */
    public void setCanUserViewReviewerName(String canUserViewReviewerName) {
        this.canUserViewReviewerName = canUserViewReviewerName;
    }
    
    /**
     * Method used to get the createUser
     * @return createUser 
     */
    public String getCreateUser() {
        return createUser;
    }
    
    /**
     * Method used to set the createUser
     * @param createUser 
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
    
    /**
     * Method used to get the createUserName
     * @return createUserName 
     */
    public String getCreateUserName() {
        return createUserName;
    }
    
    /**
     * Method used to set the createUserName
     * @param createUserName 
     */
    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }
    
    /**
     * Method used to get the createTimestamp
     * @return createTimestamp 
     */
    public Timestamp getCreateTimestamp() {
        return createTimestamp;
    }
    
    /**
     * Method used to set the createTimestamp
     * @param createTimestamp 
     */
    public void setCreateTimestamp(Timestamp createTimestamp) {
        this.createTimestamp = createTimestamp;
    }
    
    /**
     * Method used to get the updateTimestamp
     * @return updateTimestamp 
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /**
     * Method used to set the updateTimestamp
     * @param updateTimestamp 
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
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
     * Method used to get the attachmentNumber
     * @return attachmentNumber 
     */
    public int getAttachmentNumber() {
        return attachmentNumber;
    }
    
    /**
     * Method used to set the attachmentNumber
     * @param attachmentNumber 
     */
    public void setAttachmentNumber(int attachmentNumber) {
        this.attachmentNumber = attachmentNumber;
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
     * Method used to get the privateAttachmentFlag
     * @return privateAttachmentFlag 
     */
    public String getPrivateAttachmentFlag() {
        return privateAttachmentFlag;
    }
    
    /**
     * Method used to set the privateAttachmentFlag
     * @param privateAttachmentFlag 
     */
    public void setPrivateAttachmentFlag(String privateAttachmentFlag) {
        this.privateAttachmentFlag = privateAttachmentFlag;
    }

    /**
     * Method used to get the finalAttachmentFlag
     * @return finalAttachmentFlag 
     */
    public String getFinalAttachmentFlag() {
        return finalAttachmentFlag;
    }
    
    /**
     * Method used to set the finalAttachmentFlag
     * @param finalAttachmentFlag 
     */
    public void setFinalAttachmentFlag(String finalAttachmentFlag) {
        this.finalAttachmentFlag = finalAttachmentFlag;
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
    
     /**
      * Method used to check if userAdmin
      * @return userAdmin
      */
    public boolean isUserAdmin() {
        return userAdmin;
    }
    
    /**
     * Method used to set the userAdmin
     * @param userAdmin 
     */
    public void setUserAdmin(boolean userAdmin) {
        this.userAdmin = userAdmin;
    }
}
