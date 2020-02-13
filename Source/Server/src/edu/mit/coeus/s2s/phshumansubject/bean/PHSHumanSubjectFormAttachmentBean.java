/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.s2s.phshumansubject.bean;

import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;
import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author Polusdev
 */
public class PHSHumanSubjectFormAttachmentBean implements CoeusBean, java.io.Serializable {

    private int formAttachmentNumber;
    private int awformAttachmentNumber;
    private int formNumber;
    private int awformNumber;
    private String filename;
    private String awFilename;
    private byte attachment[];
    private String contentType;
    private String contentId;
    private String proposalNumber = null;
    private String updateUser = null;
    private java.sql.Timestamp updateTimestamp = null;
    private String acType = null;

    public PHSHumanSubjectFormAttachmentBean() {
    }

    public int getFormAttachmentNumber() {
        return formAttachmentNumber;
    }

    public void setFormAttachmentNumber(int formAttachmentNumber) {
        this.formAttachmentNumber = formAttachmentNumber;
    }

    public int getAwformAttachmentNumber() {
        return awformAttachmentNumber;
    }

    public void setAwformAttachmentNumber(int awformAttachmentNumber) {
        this.awformAttachmentNumber = awformAttachmentNumber;
    }

    public int getFormNumber() {
        return formNumber;
    }

    public void setFormNumber(int formNumber) {
        this.formNumber = formNumber;
    }

    public int getAwformNumber() {
        return awformNumber;
    }

    public void setAwformNumber(int awformNumber) {
        this.awformNumber = awformNumber;
    }

    public String getProposalNumber() {
        return proposalNumber;
    }

    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }

    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getAcType() {
        return acType;
    }

    public void setAcType(String acType) {
        this.acType = acType;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public boolean isLike(ComparableBean comparableBean)
            throws CoeusException {
        throw new CoeusException("Do Not Use isLike use QueryEngine.filter instead");
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return the awFilename
     */
    public String getAwFilename() {
        return awFilename;
    }

    /**
     * @param awFilename the awFilename to set
     */
    public void setAwFilename(String awFilename) {
        this.awFilename = awFilename;
    }

    /**
     * @return the attachment
     */
    public byte[] getAttachment() {
        return attachment;
    }

    /**
     * @param attachment the attachment to set
     */
    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    /**
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @return the contentId
     */
    public String getContentId() {
        return contentId;
    }

    /**
     * @param contentId the contentId to set
     */
    public void setContentId(String contentId) {
        this.contentId = contentId;
    }
}
