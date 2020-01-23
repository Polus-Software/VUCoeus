/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.beans;

import java.util.Date;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author Mr Lijo
 */
public class Coiv2AttachmentBean {

    private String disclosureNumber;
    private Integer sequenceNumber;
    private String acType;
    private String mode;
    private String docType;
    private String docName;
    private String description;
    private String docdescription;
    private String fileName;
    private String pjctTitle;
    private FormFile document;
    private Integer entityNumber;
     private String entName;
    private byte[] fileBytes;
    private String fileNameHidden;
    private Date updateTimeStamp;
    private String updateUser;
    private String userName;
    private String pId;
   private Integer discDetailsNumber;
   private String projectName;
   private String moduleItemKey;
   private String entityNum;

    public String getEntityNum() {
        return entityNum;
    }

    public void setEntityNum(String entityNum) {
        this.entityNum = entityNum;
    }

    /**
     * @return the disclosureNumber
     */
    public String getDisclosureNumber() {
        return disclosureNumber;
    }

    /**
     * @param disclosureNumber the disclosureNumber to set
     */
    public void setDisclosureNumber(String disclosureNumber) {
        this.disclosureNumber = disclosureNumber;
    }

    /**
     * @return the sequenceNumber
     */
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * @param sequenceNumber the sequenceNumber to set
     */
    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
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
     * @return the mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * @return the docType
     */
    public String getDocType() {
        return docType;
    }

    /**
     * @param docType the docType to set
     */
    public void setDocType(String docType) {
        this.docType = docType;
    }

    /**
     * @return the docCode
     */
    public String getDocCode() {
        return docName;
    }

    /**
     * @param docCode the docCode to set
     */
    public void setDocCode(String docCode) {
        this.docName = docCode;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
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
     * @return the document
     */
    public FormFile getDocument() {
        return document;
    }

    /**
     * @param document the document to set
     */
    public void setDocument(FormFile document) {
        this.document = document;
    }

    /**
     * @return the entityNumber
     */
    public Integer getEntityNumber() {
        return entityNumber;
    }

    /**
     * @param entityNumber the entityNumber to set
     */
    public void setEntityNumber(Integer entityNumber) {
        this.entityNumber = entityNumber;
    }

    /**
     * @return the fileBytes
     */
    public byte[] getFileBytes() {
        return fileBytes;
    }

    /**
     * @param fileBytes the fileBytes to set
     */
    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    /**
     * @return the fileNameHidden
     */
    public String getFileNameHidden() {
        return fileNameHidden;
    }

    /**
     * @param fileNameHidden the fileNameHidden to set
     */
    public void setFileNameHidden(String fileNameHidden) {
        this.fileNameHidden = fileNameHidden;
    }

    /**
     * @return the updateTimeStamp
     */
    public Date getUpdateTimeStamp() {
        return updateTimeStamp;
    }

    /**
     * @param updateTimeStamp the updateTimeStamp to set
     */
    public void setUpdateTimeStamp(Date updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
    }

    /**
     * @return the updateUser
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * @param updateUser the updateUser to set
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the discDetailsNumber
     */
    public Integer getDiscDetailsNumber() {
        return discDetailsNumber;
    }

    /**
     * @param discDetailsNumber the discDetailsNumber to set
     */
    public void setDiscDetailsNumber(Integer discDetailsNumber) {
        this.discDetailsNumber = discDetailsNumber;
    }

    /**
     * @return the projectName
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * @param projectName the projectName to set
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * @return the pId
     */
    public String getpId() {
        return pId;
    }

    /**
     * @param pId the pId to set
     */
    public void setpId(String pId) {
        this.pId = pId;
    }

    /**
     * @return the docdescription
     */
    public String getDocdescription() {
        return docdescription;
    }

    /**
     * @param docdescription the docdescription to set
     */
    public void setDocdescription(String docdescription) {
        this.docdescription = docdescription;
    }

    /**
     * @return the entName
     */
    public String getEntName() {
        return entName;
    }

    /**
     * @param entName the entName to set
     */
    public void setEntName(String entName) {
        this.entName = entName;
    }

    /**
     * @return the pjctTitle
     */
    public String getPjctTitle() {
        return pjctTitle;
    }

    /**
     * @param pjctTitle the pjctTitle to set
     */
    public void setPjctTitle(String pjctTitle) {
        this.pjctTitle = pjctTitle;
    }

    /**
     * @return the moduleItemKey
     */
    public String getModuleItemKey() {
        return moduleItemKey;
    }

    /**
     * @param moduleItemKey the moduleItemKey to set
     */
    public void setModuleItemKey(String moduleItemKey) {
        this.moduleItemKey = moduleItemKey;
    }
    
}
