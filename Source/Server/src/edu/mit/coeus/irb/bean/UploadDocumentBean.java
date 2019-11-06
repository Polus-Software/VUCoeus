/*
 * UploadDocumentBean.java
 *
 * Created on April 14, 2005, 3:05 PM
 */

/* PMD check performed, and commented unused imports and variables on 11-MAY-2007
 * by Leena
 */

package edu.mit.coeus.irb.bean;

import edu.mit.coeus.bean.CoeusBean;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author  ajaygm
 */
public class UploadDocumentBean implements CoeusBean,Serializable{
    private String protocolNumber;
    private int sequenceNumber;
    private int uploadId;
    private int docCode;
    private String docType;
    private String description;
    private String fileName;
    private byte[] document;
    private String acType;
    private String updateUser;
    private Timestamp updateTimeStamp;
    
    private String aw_protocolNumber;
    private int aw_sequenceNumber;
    private int aw_uploadID;
    //Added for userid to username enhancement
    private String updateUserName;
    //Added for Protocol Upload Document enhancement start 1
    private int aw_versionNumber;
    private int versionNumber;
    private int aw_docCode;
    private int statusCode;
    private int aw_StatusCode;
    private String statusDescription;
    private boolean changeStatus;
    // Added for Coeus4.3 enhancement - start
    //Document id field is added to identify a doument
    private int documentId;
    private int aw_documentId;
    private boolean amended;
    // Added for Coeus4.3 enhancement - end
    //Added for Protocol Upload Document enhancement end 1
    
    /** Creates a new instance of UploadDocumentBean */
    public UploadDocumentBean() {
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
     * Getter for property updateTimeStamp.
     * @return Value of property updateTimeStamp.
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimeStamp;
    }
    
    /**
     * Setter for property updateTimeStamp.
     * @param updateTimeStamp New value of property updateTimeStamp.
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimeStamp = updateTimestamp;
    }
    
    public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) throws edu.mit.coeus.exception.CoeusException {
        return true;
    }
    
    /**
     * Getter for property aw_protocolNumber.
     * @return Value of property aw_protocolNumber.
     */
    public java.lang.String getAw_protocolNumber() {
        return aw_protocolNumber;
    }
    
    /**
     * Setter for property aw_protocolNumber.
     * @param aw_protocolNumber New value of property aw_protocolNumber.
     */
    public void setAw_protocolNumber(java.lang.String aw_protocolNumber) {
        this.aw_protocolNumber = aw_protocolNumber;
    }
    
    /**
     * Getter for property aw_sequenceNumber.
     * @return Value of property aw_sequenceNumber.
     */
    public int getAw_sequenceNumber() {
        return aw_sequenceNumber;
    }
    
    /**
     * Setter for property aw_sequenceNumber.
     * @param aw_sequenceNumber New value of property aw_sequenceNumber.
     */
    public void setAw_sequenceNumber(int aw_sequenceNumber) {
        this.aw_sequenceNumber = aw_sequenceNumber;
    }
    
    /**
     * Getter for property document.
     * @return Value of property document.
     */
    public byte[] getDocument() {
        return this.document;
    }
    
    /**
     * Setter for property document.
     * @param document New value of property document.
     */
    public void setDocument(byte[] document) {
        this.document = document;
    }
    
    /**
     * Getter for property docCode.
     * @return Value of property docCode.
     */
    public int getDocCode() {
        return docCode;
    }
    
    /**
     * Setter for property docCode.
     * @param docCode New value of property docCode.
     */
    public void setDocCode(int docCode) {
        this.docCode = docCode;
    }
    
    /**
     * Getter for property docType.
     * @return Value of property docType.
     */
    public java.lang.String getDocType() {
        return docType;
    }
    
    /**
     * Setter for property docType.
     * @param docType New value of property docType.
     */
    public void setDocType(java.lang.String docType) {
        this.docType = docType;
    }
    
    /**
     * Getter for property uploadId.
     * @return Value of property uploadId.
     */
    public int getUploadId() {
        return uploadId;
    }
    
    /**
     * Setter for property uploadId.
     * @param uploadId New value of property uploadId.
     */
    public void setUploadId(int uploadId) {
        this.uploadId = uploadId;
    }
    
    /**
     * Getter for property aw_uploadID.
     * @return Value of property aw_uploadID.
     */
    public int getAw_uploadID() {
        return aw_uploadID;
    }
    
    /**
     * Setter for property aw_uploadID.
     * @param aw_uploadID New value of property aw_uploadID.
     */
    public void setAw_uploadID(int aw_uploadID) {
        this.aw_uploadID = aw_uploadID;
    }
    //Added for Protocol Upload Document enhancement start 2
    /**
     * Getter for property versionNumber.
     * @return Value of property versionNumber.
     */
    public int getVersionNumber() {
        return versionNumber;
    }
    
    /**
     * Setter for property versionNumber.
     * @param versionNumber New value of property versionNumber.
     */
    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }
    
    /**
     * Getter for property aw_versionNumber.
     * @return Value of property aw_versionNumber.
     */
    public int getAw_versionNumber() {
        return aw_versionNumber;
    }
    
    /**
     * Setter for property aw_versionNumber.
     * @param aw_versionNumber New value of property aw_versionNumber.
     */
    public void setAw_versionNumber(int aw_versionNumber) {
        this.aw_versionNumber = aw_versionNumber;
    }
    
    /**
     * Getter for property aw_docCode.
     * @return Value of property aw_docCode.
     */
    public int getAw_docCode() {
        return aw_docCode;
    }
    
    /**
     * Setter for property aw_docCode.
     * @param aw_docCode New value of property aw_docCode.
     */
    public void setAw_docCode(int aw_docCode) {
        this.aw_docCode = aw_docCode;
    }
    
    /**
     * Getter for property statusCode.
     * @return Value of property statusCode.
     */
    public int getStatusCode() {
        return statusCode;
    }
    
    /**
     * Setter for property statusCode.
     * @param statusCode New value of property statusCode.
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    
    /**
     * Getter for property statusDescription.
     * @return Value of property statusDescription.
     */
    public java.lang.String getStatusDescription() {
        return statusDescription;
    }
    
    /**
     * Setter for property statusDescription.
     * @param statusDescription New value of property statusDescription.
     */
    public void setStatusDescription(java.lang.String statusDescription) {
        this.statusDescription = statusDescription;
    }
    
    /**
     * Getter for property aw_StatusCode.
     * @return Value of property aw_StatusCode.
     */
    public int getAw_StatusCode() {
        return aw_StatusCode;
    }
    
    /**
     * Setter for property aw_StatusCode.
     * @param aw_StatusCode New value of property aw_StatusCode.
     */
    public void setAw_StatusCode(int aw_StatusCode) {
        this.aw_StatusCode = aw_StatusCode;
    }
    
    /**
     * Getter for property changeStatus.
     * @return Value of property changeStatus.
     */
    public boolean isChangeStatus() {
        return changeStatus;
    }
    
    /**
     * Setter for property changeStatus.
     * @param changeStatus New value of property changeStatus.
     */
    public void setChangeStatus(boolean changeStatus) {
        this.changeStatus = changeStatus;
    }
    
    //Added for Protocol Upload Document enhancement end 2

    //UserId to Username enhancement - Start
    /**
     * Method used to get the update user name
     * @return updateUserName String
     */
    public String getUpdateUserName() {
        return updateUserName;
    }

    /**
     * Method used to set the update user name
     * @param updateUserName String
     */
    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }
    // Added for Coeus4.3 enhancement - start
    /**
     * Getter for property aw_documentId.
     * @return Value of property aw_documentId.
     */
    public int getAw_documentId() {
        return aw_documentId;
    }
    
    /**
     * Setter for property aw_documentId.
     * @param aw_documentId New value of property aw_documentId.
     */
    public void setAw_documentId(int aw_documentId) {
        this.aw_documentId = aw_documentId;
    }
    
    /**
     * Getter for property documentId.
     * @return Value of property documentId.
     */
    public int getDocumentId() {
        return documentId;
    }
    
    /**
     * Setter for property documentId.
     * @param documentId New value of property documentId.
     */
    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }
    
    /**
     * Getter for property amended.
     * @return Value of property amended.
     */
    public boolean isAmended() {
        return amended;
    }
    
    /**
     * Setter for property amended.
     * @param amended New value of property amended.
     */
    public void setAmended(boolean amended) {
        this.amended = amended;
    }
    // Added for Coeus4.3 enhancement - end
    //UserId to Username enhancement - End
}
