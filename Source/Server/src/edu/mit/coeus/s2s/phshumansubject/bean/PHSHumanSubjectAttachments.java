/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeus.s2s.phshumansubject.bean;

import java.sql.Timestamp;

/**
 *
 * @author polusDev
 */
public class PHSHumanSubjectAttachments {
 private String proposalNumber;
 private Integer attachmentNumber;
 private Integer phsHumnsubjtAttachmentType;
 private byte[] attchment;
 private String fileName;
 private String ContentType;
 private Timestamp updateTimestamp;
 private String UpdateUser;
 private String acType;
    /**
     * @return the proposalNumber
     */
    public String getProposalNumber() {
        return proposalNumber;
    }

    /**
     * @param proposalNumber the proposalNumber to set
     */
    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }

    /**
     * @return the attachmentNumber
     */
    public Integer getAttachmentNumber() {
        return attachmentNumber;
    }

    /**
     * @param attachmentNumber the attachmentNumber to set
     */
    public void setAttachmentNumber(Integer attachmentNumber) {
        this.attachmentNumber = attachmentNumber;
    }

    /**
     * @return the phsHumnsubjtAttachmentType
     */
    public Integer getPhsHumnsubjtAttachmentType() {
        return phsHumnsubjtAttachmentType;
    }

    /**
     * @param phsHumnsubjtAttachmentType the phsHumnsubjtAttachmentType to set
     */
    public void setPhsHumnsubjtAttachmentType(Integer phsHumnsubjtAttachmentType) {
        this.phsHumnsubjtAttachmentType = phsHumnsubjtAttachmentType;
    }

    /**
     * @return the attchment
     */
    public byte[] getAttchment() {
        return attchment;
    }

    /**
     * @param attchment the attchment to set
     */
    public void setAttchment(byte[] attchment) {
        this.attchment = attchment;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the ContentType
     */
    public String getContentType() {
        return ContentType;
    }

    /**
     * @param ContentType the ContentType to set
     */
    public void setContentType(String ContentType) {
        this.ContentType = ContentType;
    }

    /**
     * @return the UpdateUser
     */
    public String getUpdateUser() {
        return UpdateUser;
    }

    /**
     * @param UpdateUser the UpdateUser to set
     */
    public void setUpdateUser(String UpdateUser) {
        this.UpdateUser = UpdateUser;
    }

    /**
     * @return the acType
     */
    public String getAcType() {
        return acType;
    }

    /**
     * @param acType the acType to set
     */
    public void setAcType(String acType) {
        this.acType = acType;
    }

    /**
     * @return the updateTimestamp
     */
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    /**
     * @param updateTimestamp the updateTimestamp to set
     */
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
        
}
