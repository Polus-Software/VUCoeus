/*
 * ProposalNarrativeFormBean.java
 *
 * Created on April 4, 2003, 4:57 PM
 */

package edu.mit.coeus.propdev.bean;

import edu.mit.coeus.bean.CoeusAttachmentBean;
import java.beans.*;
import java.util.Vector;

/**
 *
 * @author  mukund
 */
public class ProposalNarrativeFormBean extends CoeusAttachmentBean implements java.io.Serializable {

    private static final String MODULE_TITLE ="moduleTitle";
    private static final String MODULE_STATUS_CODE ="moduleStatusCode";
    private static final String CONTACT_NAME ="contactName";
    private static final String PHONE_NUMBER ="phoneNumber";
    private static final String EMAIL_ADDRESS ="emailAddress";
    private static final String COMMENTS ="comments";
    private static final String NARRATIVE_TYPE_CODE = "narrativeTypeCode";
    //Added for case#2420 - Upload of files on Edit Module Details Window
    private static final String FILE_NAME = "fileName";
    
     //holds the proposal number
    private String proposalNumber;
    //holds the module number
    private int moduleNumber;
    //holds the module sequence number
    private int moduleSequenceNumber;
    //holds the module title
    private String moduleTitle;
    //holds the module status code
    private char moduleStatusCode;
    //holds contact name
    private String contactName;
    //holds Phone Number
    private String phoneNumber;
    //holds email Address
    private String emailAddress;
    //holds comments
    private String comments;
    //holds whether Source Module Number is present
    private boolean hasSourceModuleNumber;
    //holds whether PDF Module Number is present
    private boolean hasPDFModuleNumber;
    //holds propModuleUsers
    private Vector propModuleUsers;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds Action type
    private String acType;
    //Added for case#2420 - Upload of files on Edit Module Details Window - start
    //holds file name
    private String fileName;
    //holds file bytes
    //commented with case 4007:Icon based on attachement Type
//    private byte[] fileBytes;
    //holds attachment acType
    private String attachmentAcType;
    //Added for case#2420 - Upload of files on Edit Module Details Window - end

    private PropertyChangeSupport propertySupport;
    
    //holds narrativeTypeCode,added for the narrative enhancement case:1624 start 1
    private int narrativeTypeCode;
    //added for the narrative enhancement case:1624 end 1
    // Added for Proposal Hierarchy Enhancement Case# 3183 - Start
    private boolean parentProposal;
    
    private ProposalNarrativePDFSourceBean propNarrativePDFSourceBean;
   // Added for Proposal Hierarchy Enhancement Case# 3183 - End
     
    /** Creates new ProposalNarrtiveFormBean */
    public ProposalNarrativeFormBean() {
        propertySupport = new PropertyChangeSupport( this );
    }

     public String getProposalNumber() {
        return proposalNumber;
    }

    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }


    public int getModuleNumber() {
        return moduleNumber;
    }

    public void setModuleNumber(int moduleNumber) {
        this.moduleNumber = moduleNumber;
    }

    /** Getter for property module_sequence_number.
     * @return Value of property module_sequence_number.
     */
    public int getModuleSequenceNumber() {
        return moduleSequenceNumber;
    }

    /** Setter for property module_sequence_number.
     * @param module_sequence_number New value of property module_sequence_number.
     */
    public void setModuleSequenceNumber(int moduleSequenceNumber) {
        this.moduleSequenceNumber = moduleSequenceNumber;
    }

    /** Getter for property moduleTitle.
     * @return Value of property moduleTitle.
     */
    public java.lang.String getModuleTitle() {
        return moduleTitle;
    }

    /** Setter for property moduleTitle.
     * @param moduleTitle New value of property moduleTitle.
     */
    public void setModuleTitle(String newModuleTitle) {
        String oldModuleTitle = moduleTitle;
        this.moduleTitle = newModuleTitle;
        propertySupport.firePropertyChange(MODULE_TITLE, oldModuleTitle, moduleTitle);
    }

    /** Getter for property moduleStatusCode.
     * @return Value of property moduleStatusCode.
     */
    public char getModuleStatusCode() {
        return moduleStatusCode;
    }

    /** Setter for property moduleStatusCode.
     * @param moduleStatusCode New value of property moduleStatusCode.
     */
    public void setModuleStatusCode(char newModuleStatusCode) {
        char oldModuleStatusCode = moduleStatusCode;
        this.moduleStatusCode = newModuleStatusCode;
        propertySupport.firePropertyChange(MODULE_STATUS_CODE, oldModuleStatusCode, moduleStatusCode);
    }

    /** Getter for property contactName.
     * @return Value of property contactName.
     */
    public java.lang.String getContactName() {
        return contactName;
    }

    /** Setter for property contactName.
     * @param contactName New value of property contactName.
     */
    public void setContactName(String newContactName) {
        String oldContactName = contactName;
        this.contactName = newContactName;
        propertySupport.firePropertyChange(CONTACT_NAME, oldContactName, contactName);        
    }

    /** Getter for property phoneNumber.
     * @return Value of property phoneNumber.
     */
    public java.lang.String getPhoneNumber() {
        return phoneNumber;
    }

    /** Setter for property phoneNumber.
     * @param phoneNumber New value of property phoneNumber.
     */
    public void setPhoneNumber(String newPhoneNumber) {
        String oldPhoneNumber = phoneNumber;
        this.phoneNumber = newPhoneNumber;
        propertySupport.firePropertyChange(PHONE_NUMBER, oldPhoneNumber, phoneNumber);                
               
    }

    /** Getter for property emailAddress.
     * @return Value of property emailAddress.
     */
    public java.lang.String getEmailAddress() {
        return emailAddress;
    }

    /** Setter for property emailAddress.
     * @param emailAddress New value of property emailAddress.
     */
    public void setEmailAddress(String newEmailAddress) {
        String oldEmailAddress = emailAddress;
        this.emailAddress = newEmailAddress;
        propertySupport.firePropertyChange(EMAIL_ADDRESS, oldEmailAddress, emailAddress);                
    }

    /** Getter for property comments.
     * @return Value of property comments.
     */
    public java.lang.String getComments() {
        return comments;
    }

    /** Setter for property comments.
     * @param comments New value of property comments.
     */
    public void setComments(String newComments) {
        String oldComments = comments;
        this.comments = newComments;
        propertySupport.firePropertyChange(COMMENTS, oldComments, comments);                        
    }

    /** Getter for property hasSourceModuleNumber.
     * @return Value of property hasSourceModuleNumber.
     */
    public boolean getHasSourceModuleNumber() {
        return hasSourceModuleNumber;
    }

    /** Setter for property hasSourceModuleNumber.
     * @param hasSourceModuleNumber New value of property hasSourceModuleNumber.
     */
    public void setHasSourceModuleNumber(boolean hasSourceModuleNumber) {
        this.hasSourceModuleNumber = hasSourceModuleNumber;
    }

    /** Getter for property hasPDFModuleNumber.
     * @return Value of property hasPDFModuleNumber.
     */
    public boolean getHasPDFModuleNumber() {
        return hasPDFModuleNumber;
    }

    /** Setter for property hasPDFModuleNumber.
     * @param hasPDFModuleNumber New value of property hasPDFModuleNumber.
     */
    public void setHasPDFModuleNumber(boolean hasPDFModuleNumber) {
        this.hasPDFModuleNumber = hasPDFModuleNumber;
    }


    /** Getter for property propModuleUsers.
     * @return Value of property propModuleUsers.
     */
    public Vector getPropModuleUsers() {
        return propModuleUsers;
    }

    /** Setter for property propModuleUsers.
     * @param propModuleUsers New value of property propModuleUsers.
     */
    public void setPropModuleUsers(Vector propModuleUsers) {
        this.propModuleUsers = propModuleUsers;
    }


    public String getUpdateUser(){
        return updateUser;
    }

    /**
     * Method used to set the update user id
     * @param updateUser String
     */
    public void setUpdateUser(String updateUser){
        this.updateUser = updateUser;
    }

    /**
     * Method used to get the update timestamp
     * @return updateTimestamp Timestamp
     */
    public java.sql.Timestamp getUpdateTimestamp(){
        return updateTimestamp;
    }

    /**
     * Method used to set the update timestamp
     * @param updateTimestamp Timestamp
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp){
        this.updateTimestamp = updateTimestamp;
    }

    /**
     * Method used to get the Action Type
     * @return acType String
     */
    public String getAcType(){
        return acType;
    }

    public void setAcType(String acType){
        this.acType = acType;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    /** Getter for property propertySupport.
     * @return Value of property propertySupport.
     */
    public java.beans.PropertyChangeSupport getPropertySupport() {
        return propertySupport;
    }

    /** Setter for property propertySupport.
     * @param propertySupport New value of property propertySupport.
     */
    public void setPropertySupport(java.beans.PropertyChangeSupport propertySupport) {
        this.propertySupport = propertySupport;
    }
    
    /**
     * Getter for property narrativeTypeCode.
     * @return Value of property narrativeTypeCode.
     */
    public int getNarrativeTypeCode() {
        return narrativeTypeCode;
    }    

    /**
     * Setter for property narrativeTypeCode.
     * @param narrativeTypeCode New value of property narrativeTypeCode.
     */
    public void setNarrativeTypeCode(int newNarrativeTypeCode) {
        int oldNarrativeTypeCode = narrativeTypeCode;
        this.narrativeTypeCode = newNarrativeTypeCode;
        propertySupport.firePropertyChange(NARRATIVE_TYPE_CODE, oldNarrativeTypeCode, narrativeTypeCode);  
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String newfileName) {
        String oldFileName = fileName;
        this.fileName = newfileName;
        propertySupport.firePropertyChange(FILE_NAME, oldFileName, fileName);
    }    

    public String getAttachmentAcType() {
        return attachmentAcType;
    }

    public void setAttachmentAcType(String attachmentAcType) {
        this.attachmentAcType = attachmentAcType;
    }
    //Added for case # 3183 - Proposal Hierarchy enhancements -start
    /** 
     * Getter for property parentProposal.
     * @return Value of property parentProposal.
     */
    public boolean isParentProposal() {
        return parentProposal;
    }
    
    /**
     * Setter for property parentProposal.
     * @param parentProposal New value of property parentProposal.
     */
    public void setParentProposal(boolean parentProposal) {
        this.parentProposal = parentProposal;
    }
    
    /** 
     * Getter for property propNarrativePDFSourceBean.
     * @return Value of property propNarrativePDFSourceBean.
     */
    public ProposalNarrativePDFSourceBean getPropNarrativePDFSourceBean(){
        return propNarrativePDFSourceBean;
    }
    
    /**
     * Setter for property propNarrativePDFSourceBean.
     * @param propNarrativePDFSourceBean New value of property propNarrativePDFSourceBean.
     */
    public void setPropNarrativePDFSourceBean(ProposalNarrativePDFSourceBean propNarrativePDFSourceBean){
        this.propNarrativePDFSourceBean = propNarrativePDFSourceBean;
    }
    //Added for case # 3183 - Proposal Hierarchy enhancements -end
}
