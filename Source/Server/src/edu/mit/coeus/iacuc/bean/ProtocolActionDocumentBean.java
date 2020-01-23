/*
 * ProtocolActionDocumentBean.java
 *
 * Created on January 30, 2008, 5:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.bean.CoeusAttachmentBean;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.iacuc.bean.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author nandkumarsn
 */
public class ProtocolActionDocumentBean extends CoeusAttachmentBean implements CoeusBean, Serializable{
        
    private String protocolNumber;
    private int sequenceNumber;
    private int submissionNumber;
    private int documentId;
//    private String fileName;
//    private byte[] fileBytes;    
    private Timestamp updateTimestamp;
    private String updateUser;
    private String acType;
    // COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document - Start
    private String description;
    private String updateUserName;
    // COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document - End
    
    /** Creates a new instance of ProtocolActionDocumentBean */
    public ProtocolActionDocumentBean() {
    }

    public String getProtocolNumber() {
        return protocolNumber;
    }

    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getSubmissionNumber() {
        return submissionNumber;
    }

    public void setSubmissionNumber(int submissionNumber) {
        this.submissionNumber = submissionNumber;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }
       //Commented with Case 4007:Icon based on Mime Type
//    public String getFileName() {
//        return fileName;
//    }
//
//    public void setFileName(String fileName) {
//        this.fileName = fileName;
//    }
//
//    public byte[] getFileBytes() {
//        return fileBytes;
//    }
//
//    public void setFileBytes(byte[] fileBytes) {
//        this.fileBytes = fileBytes;
//    }
    //4007 End
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getAcType() {
        return acType;
    }

    public void setAcType(String acType) {
        this.acType = acType;
    }
    
    public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) throws edu.mit.coeus.exception.CoeusException {
        return true;
    }        
    // COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document - Start
    /**
     * Getter method for document description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter method for document description
     * @param description 
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter method for update user name
     * @return updateUserName
     */
    public String getUpdateUserName() {
        return updateUserName;
    }

    /**
     * Setter method for update user name
     * @param updateUserName 
     */
    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }
  // COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document - End  
}
