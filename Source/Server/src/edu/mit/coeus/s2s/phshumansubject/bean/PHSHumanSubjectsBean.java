/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.s2s.phshumansubject.bean;

import java.util.List;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;
import java.sql.Timestamp;
/**
 *
 * @author Polusdev
 */
public class PHSHumanSubjectsBean extends ValidatorForm{

    private String proposalNumber;
    private String isHuman;
    private String exemptFedReg;
    private List<String> exemptionNumberList;
    private String isInvolveHumanSpecimen;
    private List<PHSHumanSubjectAttachments> phsHumanSubjectAttList;
    private Timestamp updateTimestamp;
    private String updateUser;
    private String acType;
    private String headerAcType;
    private List<PHSHumanSubjectFormBean> phsHumnSubjtFormBeanList;
    private List<PHSHumanSubjectDelayedStudyBean> phsHumnSubjtDlyStdyList;
    private FormFile humanSbjtFormFile;
    private FormFile otherAttmntFormFile;
    private FormFile delayedFormFile;
    private FormFile s2sFormFile;
    private String fileName;
    private String ContentType;
    private byte[] attchment;
    private Integer attachmentNumber;
    private Integer phsHumnsubjtAttachmentType;
    private String attachmentAcType;
    private String studyTitle; 
    private String isAnticipatedCt;
    private String description;
    private Integer studyNumber;
    private Integer formNumber;

    public String getHeaderAcType() {
        return headerAcType;
    }

    public void setHeaderAcType(String headerAcType) {
        this.headerAcType = headerAcType;
    }

    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStudyTitle() {
        return studyTitle;
    }

    public void setStudyTitle(String studyTitle) {
        this.studyTitle = studyTitle;
    }
   

    public String getIsAnticipatedCt() {
        return isAnticipatedCt;
    }

    public void setIsAnticipatedCt(String isAnticipatedCt) {
        this.isAnticipatedCt = isAnticipatedCt;
    }
   
    
    public String getProposalNumber() {
        return proposalNumber;
    }

    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }

    public String getIsHuman() {
        return isHuman;
    }

    public void setIsHuman(String isHuman) {
        this.isHuman = isHuman;
    }

    public String getIsInvolveHumanSpecimen() {
        return isInvolveHumanSpecimen;
    }

    public void setIsInvolveHumanSpecimen(String isInvolveHumanSpecimen) {
        this.isInvolveHumanSpecimen = isInvolveHumanSpecimen;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public FormFile getHumanSbjtFormFile() {
        return humanSbjtFormFile;
    }

    public void setHumanSbjtFormFile(FormFile humanSbjtFormFile) {
        this.humanSbjtFormFile = humanSbjtFormFile;
    }

    public FormFile getOtherAttmntFormFile() {
        return otherAttmntFormFile;
    }

    public void setOtherAttmntFormFile(FormFile otherAttmntFormFile) {
        this.otherAttmntFormFile = otherAttmntFormFile;
    }

    public FormFile getDelayedFormFile() {
        return delayedFormFile;
    }

    public void setDelayedFormFile(FormFile delayedFormFile) {
        this.delayedFormFile = delayedFormFile;
    }

    public FormFile getS2sFormFile() {
        return s2sFormFile;
    }

    public void setS2sFormFile(FormFile s2sFormFile) {
        this.s2sFormFile = s2sFormFile;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * @return the exemptionNumberList
     */
    public List<String> getExemptionNumberList() {
        return exemptionNumberList;
    }

    /**
     * @param exemptionNumberList the exemptionNumberList to set
     */
    public void setExemptionNumberList(List<String> exemptionNumberList) {
        this.exemptionNumberList = exemptionNumberList;
    }

    /**
     * @return the exemptFedReg
     */
    public String getExemptFedReg() {
        return exemptFedReg;
    }

    /**
     * @param exemptFedReg the exemptFedReg to set
     */
    public void setExemptFedReg(String exemptFedReg) {
        this.exemptFedReg = exemptFedReg;
    }

    /**
     * @return the phsHumnSubjtFormBeanList
     */
    public List<PHSHumanSubjectFormBean> getPhsHumnSubjtFormBeanList() {
        return phsHumnSubjtFormBeanList;
    }

    /**
     * @param phsHumnSubjtFormBeanList the phsHumnSubjtFormBeanList to set
     */
    public void setPhsHumnSubjtFormBeanList(List<PHSHumanSubjectFormBean> phsHumnSubjtFormBeanList) {
        this.phsHumnSubjtFormBeanList = phsHumnSubjtFormBeanList;
    }

    /**
     * @return the phsHumnSubjtDlyStdyList
     */
    public List<PHSHumanSubjectDelayedStudyBean> getPhsHumnSubjtDlyStdyList() {
        return phsHumnSubjtDlyStdyList;
    }

    /**
     * @param phsHumnSubjtDlyStdyList the phsHumnSubjtDlyStdyList to set
     */
    public void setPhsHumnSubjtDlyStdyList(List<PHSHumanSubjectDelayedStudyBean> phsHumnSubjtDlyStdyList) {
        this.phsHumnSubjtDlyStdyList = phsHumnSubjtDlyStdyList;
    }

    /**
     * @return the phsHumanSubjectAttList
     */
    public List<PHSHumanSubjectAttachments> getPhsHumanSubjectAttList() {
        return phsHumanSubjectAttList;
    }

    /**
     * @param phsHumanSubjectAttList the phsHumanSubjectAttList to set
     */
    public void setPhsHumanSubjectAttList(List<PHSHumanSubjectAttachments> phsHumanSubjectAttList) {
        this.phsHumanSubjectAttList = phsHumanSubjectAttList;
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
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return the ContentType
     */
    public String getContentType() {
        return ContentType;
    }

    /**
     * @return the attchment
     */
    public byte[] getAttchment() {
        return attchment;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContentType(String ContentType) {
        this.ContentType = ContentType;
    }

    public void setAttchment(byte[] attchment) {
        this.attchment = attchment;
    }

    public void setAttachmentNumber(Integer attachmentNumber) {
        this.attachmentNumber = attachmentNumber;
    }

    public void setAttachmentAcType(String attachmentAcType) {
        this.attachmentAcType = attachmentAcType;
    }

    /**
     * @return the attachmentNumber
     */
    public Integer getAttachmentNumber() {
        return attachmentNumber;
    }

    /**
     * @return the attachmentAcType
     */
    public String getAttachmentAcType() {
        return attachmentAcType;
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
     * @return the studyNumber
     */
    public Integer getStudyNumber() {
        return studyNumber;
    }

    /**
     * @param studyNumber the studyNumber to set
     */
    public void setStudyNumber(Integer studyNumber) {
        this.studyNumber = studyNumber;
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

    public Integer getFormNumber() {
        return formNumber;
    }

    public void setFormNumber(Integer formNumber) {
        this.formNumber = formNumber;
    }

}
