/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.formbeans;

import java.util.Date;
import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author Mr Lijo
 */
public class Coiv2Attachment extends ActionForm {
    private String disclosureNumber;
    private int sequenceNumber;
    private String acType;
    private String mode;
    private String docType;
    private String attType;
    private int discDetaisNumber;
     private String entName;
    private String docName;
    private String description;
    private String fileName;
    private FormFile document;
    private int entityNumber;
    private byte[] fileBytes;
    private String fileNameHidden;
    private Date updateTimeStamp;
    private String updateUser;
    private String pjtName;
    private String entity;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getPjtName() {
        return pjtName;
    }

    public void setPjtName(String pjtName) {
        this.pjtName = pjtName;
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
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * @param sequenceNumber the sequenceNumber to set
     */
    public void setSequenceNumber(int sequenceNumber) {
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
    public int getEntityNumber() {
        return entityNumber;
    }

    /**
     * @param entityNumber the entityNumber to set
     */
    public void setEntityNumber(int entityNumber) {
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
     * @return the discDetaisNumber
     */
    public int getDiscDetaisNumber() {
        return discDetaisNumber;
    }

    /**
     * @param discDetaisNumber the discDetaisNumber to set
     */
    public void setDiscDetaisNumber(int discDetaisNumber) {
        this.discDetaisNumber = discDetaisNumber;
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

    
    
}
