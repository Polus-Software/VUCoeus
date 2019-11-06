/*
 * ProposalUploadForm.java
 *
 * Created on July 11, 2006, 3:08 PM
 */

package edu.utk.coeuslite.propdev.form;

import edu.mit.coeus.utils.TypeConstants;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

/**
 *
 * @author  noorula
 */
public class ProposalUploadForm extends ValidatorForm{
    //Change the bean property name
    private String description;
    private FormFile document;
    private int code;
    private String narrativeTypeGroup;
    private String documentType;
    private String proposalNumber;
    private String uploadStatusCode;
    private int moduleNumber;
    private int bioNumber;
    private String biosketch;
    private String currentPending;
    private String bioTimestamp ;
    private String currentPendingTimestamp ;
    private String personId;
    private String personName;
    private String documentTypeCode;
    private String acType;
    private String fileName;
    private String moduleStatusCode ;
    private String contactName ;
    private String phoneNumber ;
    private String emailAddress ;
    private String comments ;
    private String updateUser;
    private String updateTimestamp ;
    private String awUpdateTimestamp ;
    private Vector vecDocumentDetails;
    private byte[] fileBytes;
    private String awUpdateUser;
    private char accessType;
    //Added for case id 2731 -- start
    private String userName;
    //Added for case id2731 -- end

    /** Creates a new instance of ProposalUploadForm */
    public ProposalUploadForm() {
    }
    
    /**
     * Getter for property description.
     * @return Value of property description.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Getter for property code.
     * @return Value of property code.
     */
    public int getCode() {
        return code;
    }
    
    /**
     * Setter for property code.
     * @param code New value of property code.
     */
    public void setCode(int code) {
        this.code = code;
    }
    
    /**
     * Getter for property narrativeTypeGroup.
     * @return Value of property narrativeTypeGroup.
     */
    public java.lang.String getNarrativeTypeGroup() {
        return narrativeTypeGroup;
    }
    
    /**
     * Setter for property narrativeTypeGroup.
     * @param narrativeTypeGroup New value of property narrativeTypeGroup.
     */
    public void setNarrativeTypeGroup(java.lang.String narrativeTypeGroup) {
        this.narrativeTypeGroup = narrativeTypeGroup;
    }
    
    /**
     * Getter for property documentType.
     * @return Value of property documentType.
     */
    public java.lang.String getDocumentType() {
        return documentType;
    }
    
    /**
     * Setter for property documentType.
     * @param documentType New value of property documentType.
     */
    public void setDocumentType(java.lang.String documentType) {
        this.documentType = documentType;
    }
    
    /**
     * Getter for property proposalNumber.
     * @return Value of property proposalNumber.
     */
    public java.lang.String getProposalNumber() {
        return proposalNumber;
    }
    
    /**
     * Setter for property proposalNumber.
     * @param proposalNumber New value of property proposalNumber.
     */
    public void setProposalNumber(java.lang.String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
  
    
    /**
     * Getter for property moduleNumber.
     * @return Value of property moduleNumber.
     */
    public int getModuleNumber() {
        return moduleNumber;
    }
    
    /**
     * Setter for property moduleNumber.
     * @param moduleNumber New value of property moduleNumber.
     */
    public void setModuleNumber(int moduleNumber) {
        this.moduleNumber = moduleNumber;
    }
    
    /**
     * Getter for property bioNumber.
     * @return Value of property bioNumber.
     */
    public int getBioNumber() {
        return bioNumber;
    }
    
    /**
     * Setter for property bioNumber.
     * @param bioNumber New value of property bioNumber.
     */
    public void setBioNumber(int bioNumber) {
        this.bioNumber = bioNumber;
    }
    
    /**
     * Getter for property acType.
     * @return Value of property acType.
     */
    public java.lang.String getAcType() {
        return acType;
    }
    
    /**
     * Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
    }
    
   
    
    /**
     * Getter for property document.
     * @return Value of property document.
     */
    public org.apache.struts.upload.FormFile getDocument() {
        return document;
    }
    
    /**
     * Setter for property document.
     * @param document New value of property document.
     */
    public void setDocument(org.apache.struts.upload.FormFile document) {
        this.document = document;
    }
    
    /**
     * Getter for property fileName.
     * @return Value of property fileName.
     */
    public java.lang.String getFileName() {
        return fileName;
    }
    
    /**
     * Setter for property fileName.
     * @param fileName New value of property fileName.
     */
    public void setFileName(java.lang.String fileName) {
        this.fileName = fileName;
    }
    
    /**
     * Getter for property moduleStatusCode.
     * @return Value of property moduleStatusCode.
     */
    public java.lang.String getModuleStatusCode() {
        return moduleStatusCode;
    }
    
    /**
     * Setter for property moduleStatusCode.
     * @param moduleStatusCode New value of property moduleStatusCode.
     */
    public void setModuleStatusCode(java.lang.String moduleStatusCode) {
        this.moduleStatusCode = moduleStatusCode;
    }
    
    /**
     * Getter for property contactName.
     * @return Value of property contactName.
     */
    public java.lang.String getContactName() {
        return contactName;
    }
    
    /**
     * Setter for property contactName.
     * @param contactName New value of property contactName.
     */
    public void setContactName(java.lang.String contactName) {
        this.contactName = contactName;
    }
    
    /**
     * Getter for property phoneNumber.
     * @return Value of property phoneNumber.
     */
    public java.lang.String getPhoneNumber() {
        return phoneNumber;
    }
    
    /**
     * Setter for property phoneNumber.
     * @param phoneNumber New value of property phoneNumber.
     */
    public void setPhoneNumber(java.lang.String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * Getter for property emailAddress.
     * @return Value of property emailAddress.
     */
    public java.lang.String getEmailAddress() {
        return emailAddress;
    }
    
    /**
     * Setter for property emailAddress.
     * @param emailAddress New value of property emailAddress.
     */
    public void setEmailAddress(java.lang.String emailAddress) {
        this.emailAddress = emailAddress;
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
    public java.lang.String getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /**
     * Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(java.lang.String updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /**
     * Getter for property awUpdateTimestamp.
     * @return Value of property awUpdateTimestamp.
     */
    public java.lang.String getAwUpdateTimestamp() {
        return awUpdateTimestamp;
    }
    
    /**
     * Setter for property awUpdateTimestamp.
     * @param awUpdateTimestamp New value of property awUpdateTimestamp.
     */
    public void setAwUpdateTimestamp(java.lang.String awUpdateTimestamp) {
        this.awUpdateTimestamp = awUpdateTimestamp;
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest req){
        ActionErrors actionErrors = new ActionErrors();
        String fileName = getDocument().getFileName();
        String reqFileName = req.getParameter("fileName");
        String acType = req.getParameter("acType");
        acType = (acType==null)? "" : acType;
        boolean isValidFile = true;
        if(uploadStatusCode==null || uploadStatusCode.equals("")) {
            actionErrors.add("documentType",new ActionMessage("error.uploadAttachments.documentTypeCode"));
        }

        if(req.getParameter("page")!=null && !req.getParameter("page").equals("I")) {
            if(description==null || description.trim().length()>150) {
               actionErrors.add("description",new ActionMessage("error.uploadAttachments.description"));
            }
        }
        
        if(req.getParameter("page")!=null && req.getParameter("page").equals("I")) {
            if(personId==null || personId.equals("")) {
               actionErrors.add("personName",new ActionMessage("error.uploadAttachments.personName"));
            }
            //Added for case#3450 - start
            //Modified Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - Start
            if(description == null || description.equals("")){
                actionErrors.add("description",new ActionMessage("error.uploadAttachments.descriptionRequired"));
            }else if(description.trim().length() > 200){
                actionErrors.add("description",new ActionMessage("error.uploadAttachments.descriptionPersonnel"));
            }
            //Modified for Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - End
            //Added for case#3450 - end
        }
        if(fileName != null && !fileName.trim().equals("")){
            int index = fileName.lastIndexOf('.');
            if(index != -1 && index != fileName.length()){
                String extension = fileName.substring(index+1,fileName.length());
                if(extension==null || extension.equals("")) {
                    isValidFile = false;
                }
            } else {
                isValidFile = false;
            }
        } else if(!acType.equals(TypeConstants.UPDATE_RECORD)) {
            isValidFile = false;
            
        }else if(reqFileName==null || reqFileName.equals("")) {
            isValidFile = false;
            
        } else {            
            setDocument(null);
        }
        
        if(!isValidFile){
            actionErrors.add("validFileName", new ActionMessage("error.uploadAttachments.validFileName"));
        }
        //JIRA COEUSQA 1540 - START
        /*boolean filePatternMatch = Pattern.matches("([A-Za-z0-9._-]+)", description);
        if(!filePatternMatch){
            actionErrors.add("validFileName", new ActionMessage("error.uploadAttachments.fileNameChars"));
        }*/
        //JIRA COEUSQA 1540 - END
        return actionErrors;
    }
     

    
    /**
     * Getter for property personName.
     * @return Value of property personName.
     */
    public java.lang.String getPersonName() {
        return personName;
    }
    
    /**
     * Setter for property personName.
     * @param personName New value of property personName.
     */
    public void setPersonName(java.lang.String personName) {
        this.personName = personName;
    }
    
    /**
     * Getter for property documentTypeCode.
     * @return Value of property documentTypeCode.
     */
    public java.lang.String getDocumentTypeCode() {
        return documentTypeCode;
    }
    
    /**
     * Setter for property documentTypeCode.
     * @param documentTypeCode New value of property documentTypeCode.
     */
    public void setDocumentTypeCode(java.lang.String documentTypeCode) {
        this.documentTypeCode = documentTypeCode;
    }
   
    /**
     * Getter for property personId.
     * @return Value of property personId.
     */
    public java.lang.String getPersonId() {
        return personId;
    }
    
    /**
     * Setter for property personId.
     * @param personId New value of property personId.
     */
    public void setPersonId(java.lang.String personId) {
        this.personId = personId;
    }
    
    /**
     * Getter for property biosketch.
     * @return Value of property biosketch.
     */
    public java.lang.String getBiosketch() {
        return biosketch;
    }
    
    /**
     * Setter for property biosketch.
     * @param biosketch New value of property biosketch.
     */
    public void setBiosketch(java.lang.String biosketch) {
        this.biosketch = biosketch;
    }
    
    /**
     * Getter for property currentPending.
     * @return Value of property currentPending.
     */
    public java.lang.String getCurrentPending() {
        return currentPending;
    }
    
    /**
     * Setter for property currentPending.
     * @param currentPending New value of property currentPending.
     */
    public void setCurrentPending(java.lang.String currentPending) {
        this.currentPending = currentPending;
    }
    
    /**
     * Getter for property currentPendingTimestamp.
     * @return Value of property currentPendingTimestamp.
     */
    public java.lang.String getCurrentPendingTimestamp() {
        return currentPendingTimestamp;
    }
    
    /**
     * Setter for property currentPendingTimestamp.
     * @param currentPendingTimestamp New value of property currentPendingTimestamp.
     */
    public void setCurrentPendingTimestamp(java.lang.String currentPendingTimestamp) {
        this.currentPendingTimestamp = currentPendingTimestamp;
    }
    
    /**
     * Getter for property bioTimestamp.
     * @return Value of property bioTimestamp.
     */
    public java.lang.String getBioTimestamp() {
        return bioTimestamp;
    }
    
    /**
     * Setter for property bioTimestamp.
     * @param bioTimestamp New value of property bioTimestamp.
     */
    public void setBioTimestamp(java.lang.String bioTimestamp) {
        this.bioTimestamp = bioTimestamp;
    }
    
    /**
     * Getter for property uploadStatusCode.
     * @return Value of property uploadStatusCode.
     */
    public java.lang.String getUploadStatusCode() {
        return uploadStatusCode;
    }
    
    /**
     * Setter for property uploadStatusCode.
     * @param uploadStatusCode New value of property uploadStatusCode.
     */
    public void setUploadStatusCode(java.lang.String uploadStatusCode) {
        this.uploadStatusCode = uploadStatusCode;
    }
    
    /**
     * Getter for property vecDocumentDetails.
     * @return Value of property vecDocumentDetails.
     */
    public Vector getVecDocumentDetails() {
        return vecDocumentDetails;
    }
    
    /**
     * Setter for property vecDocumentDetails.
     * @param vecDocumentDetails New value of property vecDocumentDetails.
     */
    public void setVecDocumentDetails(Vector vecDocumentDetails) {
        this.vecDocumentDetails = vecDocumentDetails;
    }
    
    /**
     * Getter for property fileBytes.
     * @return Value of property fileBytes.
     */
    public byte[] getFileBytes() {
        return this.fileBytes;
    }
    
    /**
     * Setter for property fileBytes.
     * @param fileBytes New value of property fileBytes.
     */
    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }
    
    /**
     * Getter for property awUpdateUser.
     * @return Value of property awUpdateUser.
     */
    public java.lang.String getAwUpdateUser() {
        return awUpdateUser;
    }
    
    /**
     * Setter for property awUpdateUser.
     * @param awUpdateUser New value of property awUpdateUser.
     */
    public void setAwUpdateUser(java.lang.String awUpdateUser) {
        this.awUpdateUser = awUpdateUser;
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

    public char getAccessType() {
        return accessType;
    }

    public void setAccessType(char accessType) {
        this.accessType = accessType;
    }
   
}
