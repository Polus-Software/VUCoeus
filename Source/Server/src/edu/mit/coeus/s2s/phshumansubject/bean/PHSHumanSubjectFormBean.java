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
import java.util.List;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author Polusdev
 */
public class PHSHumanSubjectFormBean implements CoeusBean, java.io.Serializable {

    private int formNumber;
    private int awformNumber;
    private String description;
    private String awDescription;
    private byte userAttachedS2SPDF[];
    private String pdfUpdateUser;
    private Timestamp pdfUpdateTimestamp;
    private String pdfAcType;
    private String pdfFileName;
    private char userAttachedS2SXML[];
    private String namespace;
    private String formName;
    private List attachments;
    private String proposalNumber = null;
    private String updateUser = null;
    private java.sql.Timestamp updateTimestamp = null;
    private String acType = null;
    private List<PHSHumanSubjectFormAttachmentBean> phsHumnSubjtFormAttBeanList;
    
    public PHSHumanSubjectFormBean() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getUserAttachedS2SPDF() {
        return userAttachedS2SPDF;
    }

    public void setUserAttachedS2SPDF(byte userAttachedS2SPDF[]) {
        this.userAttachedS2SPDF = userAttachedS2SPDF;
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

    public String getPdfUpdateUser() {
        return pdfUpdateUser;
    }

    public void setPdfUpdateUser(String pdfUpdateUser) {
        this.pdfUpdateUser = pdfUpdateUser;
    }

    public Timestamp getPdfUpdateTimestamp() {
        return pdfUpdateTimestamp;
    }

    public void setPdfUpdateTimestamp(Timestamp pdfUpdateTimestamp) {
        this.pdfUpdateTimestamp = pdfUpdateTimestamp;
    }

    public char[] getUserAttachedS2SXML() {
        return userAttachedS2SXML;
    }

    public void setUserAttachedS2SXML(char[] userAttachedS2SXML) {
        this.userAttachedS2SXML = userAttachedS2SXML;
    }

    public boolean isLike(ComparableBean comparableBean)
            throws CoeusException {
        throw new CoeusException("Do Not Use isLike use QueryEngine.filter instead");
    }

    public String getPdfAcType() {
        return pdfAcType;
    }

    public void setPdfAcType(String pdfAcType) {
        this.pdfAcType = pdfAcType;
    }

    public String getPdfFileName() {
        return pdfFileName;
    }

    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }

    public List getAttachments() {
        return attachments;
    }

    public void setAttachments(List attachments) {
        this.attachments = attachments;
    }

    /**
     * @return the namespace
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * @param namespace the namespace to set
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    /**
     * @return the formName
     */
    public String getFormName() {
        return formName;
    }

    /**
     * @param formName the formName to set
     */
    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getAwDescription() {
        return awDescription;
    }

    public void setAwDescription(String awDescription) {
        this.awDescription = awDescription;
    }

    /**
     * @return the phsHumnSubjtFormAttBeanList
     */
    public List<PHSHumanSubjectFormAttachmentBean> getPhsHumnSubjtFormAttBeanList() {
        return phsHumnSubjtFormAttBeanList;
    }

    /**
     * @param phsHumnSubjtFormAttBeanList the phsHumnSubjtFormAttBeanList to set
     */
    public void setPhsHumnSubjtFormAttBeanList(List<PHSHumanSubjectFormAttachmentBean> phsHumnSubjtFormAttBeanList) {
        this.phsHumnSubjtFormAttBeanList = phsHumnSubjtFormAttBeanList;
    }
}
