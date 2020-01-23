/*
 * InstituteProposalAttachmentBean.java
 *
 * Created on January 13, 2010, 10:28 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved
 */

package edu.mit.coeus.instprop.bean;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.bean.CoeusAttachmentBean;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Timestamp;

/**
 *
 * @author satheeshkumarkn
 */
public class InstituteProposalAttachmentBean extends CoeusAttachmentBean  implements BaseBean,java.io.Serializable{
    private String proposalNumber;
    private int sequenceNumber;
    private int attachmentNumber;
    private String attachmentTitle;
    private int attachmentTypeCode;
    private String attachmentTypeDescription;
    private String contactName;
    private String phoneNumber;
    private String emailAddress;
    private String comments;
    private String updateUser;
    private String updateUserName;
    private Timestamp updateTimestamp;
    private String docUpdateUser;
    private String docUpdateUserName;
    private Timestamp docUpdateTimestamp;
    private String acType;
    private String fileName;
    private PropertyChangeSupport propertySupport;
    private static final String ATTACHMENT_TITLE ="attachmentTitle";
    private static final String CONTACT_NAME ="contactName";
    private static final String PHONE_NUMBER ="phoneNumber";
    private static final String EMAIL_ADDRESS ="emailAddress";
    private static final String COMMENTS ="comments";
    private static final String ATTACHMENT_TYPE_CODE = "attachmentTypeCode";
    private static final String FILE_NAME = "fileName";
    //COEUSQA:1525 - Attachments for Institute Proposal - Start
    private int awSequenceNumber;
    //COEUSQA:1525 - End
    /**
     * Creates a new instance of InstituteProposalAttachmentBean
     */
    public InstituteProposalAttachmentBean() {
        propertySupport = new PropertyChangeSupport( this );
    }
    
    /*
     * Getter method to get the proposal number
     * @return proposalNumber - String
     */
    public String getProposalNumber() {
        return proposalNumber;
    }
    
    /*
     * Setter method to set proposal number
     * @param proposalNumber - String
     */
    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
    /*
     * Getter method to get the proposal sequence number
     * @return sequenceNumber - int
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    
    /*
     * Setter method to set sequence number
     * @param sequenceNumber - int
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
    /*
     * Getter method to get the attachment number
     * @return attachmentNumber - int
     */
    public int getAttachmentNumber() {
        return attachmentNumber;
    }
    
    /*
     * Setter method to set attachment number
     * @param attachmentNumber - int
     */
    public void setAttachmentNumber(int attachmentNumber) {
        this.attachmentNumber = attachmentNumber;
    }
    
    /*
     * Getter method to get the attachment title
     * @return attachmentTitle - String
     */
    public String getAttachmentTitle() {
        return attachmentTitle;
    }
    
    /*
     * Setter method to set attachment title
     * @param attachmentTitle - String
     */
    public void setAttachmentTitle(String newAttachmentTitle) {
        String oldAttachmentTitle = attachmentTitle;
        this.attachmentTitle = newAttachmentTitle;
        propertySupport.firePropertyChange(ATTACHMENT_TITLE, oldAttachmentTitle, attachmentTitle);
    }
    
    /*
     * Getter method to get the attachment type code
     * @return attachmentTypeCode - int
     */
    public int getAttachmentTypeCode() {
        return attachmentTypeCode;
    }
    
    /*
     * Setter method to set attachment type code
     * @param attachmentTypeCode - int
     */
    public void setAttachmentTypeCode(int newAttachmentTypeCode) {
        int oldAttachmentTypeCode = attachmentTypeCode;
        this.attachmentTypeCode = newAttachmentTypeCode;
        propertySupport.firePropertyChange(ATTACHMENT_TYPE_CODE, oldAttachmentTypeCode, attachmentTypeCode);
    }
    
    /*
     * Getter method to get the contact name
     * @return contactName - String
     */
    public String getContactName() {
        return contactName;
    }
    
    /*
     * Setter method to set contact name
     * @param contactName - String
     */
    public void setContactName(String newContactName) {
        String oldContactName = contactName;
        this.contactName = newContactName;
        propertySupport.firePropertyChange(CONTACT_NAME, oldContactName, contactName);
    }
    
    /*
     * Getter method to get the phone number
     * @return phoneNumber - String
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    /*
     * Setter method to set phone number
     * @param phoneNumber - String
     */
    public void setPhoneNumber(String newPhoneNumber) {
        String oldPhoneNumber = phoneNumber;
        this.phoneNumber = newPhoneNumber;
        propertySupport.firePropertyChange(PHONE_NUMBER, oldPhoneNumber, phoneNumber);
    }
    
    /*
     * Getter method to get the email address
     * @return emailAddress - String
     */
    public String getEmailAddress() {
        return emailAddress;
    }
    
    /*
     * Setter method to set email address
     * @param emailAddress - String
     */
    public void setEmailAddress(String newEmailAddress) {
        String oldEmailAddress = emailAddress;
        this.emailAddress = newEmailAddress;
        propertySupport.firePropertyChange(EMAIL_ADDRESS, oldEmailAddress, emailAddress);
    }
    
    /*
     * Getter method to get the comments
     * @return comments - String
     */
    public String getComments() {
        return comments;
    }
    
    /*
     * Setter method to set comments
     * @param comments - String
     */
    public void setComments(String newComments) {
        String oldComments = comments;
        this.comments = newComments;
        propertySupport.firePropertyChange(COMMENTS, oldComments, comments);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    /*
     * Getter method to get the attachment update user
     * @return updateUser - String
     */
    public String getUpdateUser() {
        return updateUser;
    }
    
    /*
     * Setter method to set attachment update user
     * @param updateUser - String
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    
    /*
     * Getter method to get the update user name
     * @return updateUserName - String
     */
    public String getUpdateUserName() {
        return updateUserName;
    }
    
    /*
     * Setter method to set  update user name
     * @param updateUserName - String
     */
    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }
    
    /*
     * Getter method to get the attachment update time stamp
     * @return updateTimestamp - Timestamp
     */
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /*
     * Setter method to set attachment update time stamp
     * @param updateTimestamp - Timestamp
     */
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /*
     * Getter method to get the document update user
     * @return docUpdateUser - String
     */
    public String getDocUpdateUser() {
        return docUpdateUser;
    }
    
    /*
     * Getter method to get the document update user
     * @return docUpdateUser - String
     */
    public void setDocUpdateUser(String docUpdateUser) {
        this.docUpdateUser = docUpdateUser;
    }
    
    /*
     * Getter method to get the document update user name
     * @return docUpdateUserName - String
     */
    public String getDocUpdateUserName() {
        return docUpdateUserName;
    }
    
    /*
     * Setter method to set document update user name
     * @param docUpdateUserName - String
     */
    public void setDocUpdateUserName(String docUpdateUserName) {
        this.docUpdateUserName = docUpdateUserName;
    }
    
    /*
     * Getter method to get the document update time stamp
     * @return docUpdateTimestamp - Timestamp
     */
    public Timestamp getDocUpdateTimestamp() {
        return docUpdateTimestamp;
    }
    
    /*
     * Setter method to set document update time stamp
     * @param docUpdateTimestamp - Timestamp
     */
    public void setDocUpdateTimestamp(Timestamp docUpdateTimestamp) {
        this.docUpdateTimestamp = docUpdateTimestamp;
    }
    
    /*
     * Getter method to get the attachment acType
     * @return acType - String
     */
    public String getAcType() {
        return acType;
    }
    
    /*
     * Setter method to set attachmetn actype
     * @param acType - String
     */
    public void setAcType(String acType) {
        this.acType = acType;
    }
    
    /*
     * Getter method to get the file name
     * @return fileName - String
     */
    public String getFileName() {
        return fileName;
    }
    
    /*
     * Setter method to set file name
     * @param newfileName - String
     */
    public void setFileName(String newfileName) {
        String oldFileName = fileName;
        this.fileName = newfileName;
        propertySupport.firePropertyChange(FILE_NAME, oldFileName, fileName);
    }
    
    /*
     * Getter method to get the attachment type description
     * @return attachmentTypeDescription - String
     */
    public String getAttachmentTypeDescription() {
        return attachmentTypeDescription;
    }
    
    /*
     * Setter method to set attachment type description
     * @param attachmentTypeDescription - String
     */
    public void setAttachmentTypeDescription(String attachmentTypeDescription) {
        this.attachmentTypeDescription = attachmentTypeDescription;
    }
    
    /*
     * Getter method to get the proposal awSequenceNumber
     * @return awSequenceNumber - int
     */
    public int getAwSequenceNumber() {
        return awSequenceNumber;
    }
    
    /*
     * Setter method to set awSequenceNumber
     * @param awSequenceNumber - int
     */
    public void setAwSequenceNumber(int awSequenceNumber) {
        this.awSequenceNumber = awSequenceNumber;
    }
    
}
