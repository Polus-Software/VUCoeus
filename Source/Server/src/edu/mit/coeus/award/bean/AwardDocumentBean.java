/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * AwardDocumentBean.java
 *
 * Created on October 3, 2007, 4:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.award.bean;

import edu.mit.coeus.bean.CoeusAttachment;
import edu.mit.coeus.bean.CoeusAttachmentBean;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author divyasusendran
 */
public class AwardDocumentBean extends AwardBaseBean implements CoeusBean,Serializable,CoeusAttachment{
    private String awardNumber;
    private int sequenceNumber;
    private int documentTypeCode;
    private String documentTypeDescription;
    private String description;
    private String fileName;
    private byte[] document;
    private String acType;
    private String docStatusCode;
    private String docStatusDescription;
    private int documentId;
    private String updateUser;
    private String updateUserName;
    private Timestamp updateTimeStamp;

    public Timestamp getUpdateTimeStamp() {
        return updateTimeStamp;
    }

    public void setUpdateTimeStamp(Timestamp updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
    }
    private String mimeType;

    public String getAwardNumber() {
        return awardNumber;
    }

    public void setAwardNumber(String awardNumber) {
        this.awardNumber = awardNumber;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getDocumentTypeCode() {
        return documentTypeCode;
    }

    public void setDocumentTypeCode(int documentTypeCode) {
        this.documentTypeCode = documentTypeCode;
    }

    public String getDocumentTypeDescription() {
        return documentTypeDescription;
    }

    public void setDocumentTypeDescription(String documentTypeDescription) {
        this.documentTypeDescription = documentTypeDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getDocument() {
        return document;
    }

    public void setDocument(byte[] document) {
        this.document = document;
    }

    public String getAcType() {
        return acType;
    }

    public void setAcType(String acType) {
        this.acType = acType;
    }

    public String getDocStatusCode() {
        return docStatusCode;
    }

    public void setDocStatusCode(String docStatusCode) {
        this.docStatusCode = docStatusCode;
    }

    public String getDocStatusDescription() {
        return docStatusDescription;
    }

    public void setDocStatusDescription(String docStatusDescription) {
        this.docStatusDescription = docStatusDescription;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }
   

    public void setUpdateUser(String userId) {
        this.updateUser = userId;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public boolean isLike(ComparableBean comparableBean) throws CoeusException {
        return true;
    }

    public String getMimeType() {
         return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    
    public void setFileBytes(byte[] fileBytes) {
        setDocument(fileBytes);
    }

     public byte[] getFileBytes() {
        return getDocument();
    }
       
}
