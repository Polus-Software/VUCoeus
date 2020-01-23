/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.beans;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Sony
 */
public class CoiDocumentsInfoBean implements Serializable{

    public CoiDocumentsInfoBean() {
    }

    private String coiDisclosureNumber;
    private Integer sequenceNumber;
    private String dsclDispositionstatus;
    private String coiDocumentTypeCode;
    private String documentType;
    private String description;
    private String fileName;
    private String document;
    private Date updateTimestamp;
    private String updateUser;

    /**
     * @return the coiDisclosureNumber
     */
    public String getCoiDisclosureNumber() {
        return coiDisclosureNumber;
    }

    /**
     * @param coiDisclosureNumber the coiDisclosureNumber to set
     */
    public void setCoiDisclosureNumber(String coiDisclosureNumber) {
        this.coiDisclosureNumber = coiDisclosureNumber;
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
     * @return the dsclDispositionstatus
     */
    public String getDsclDispositionstatus() {
        return dsclDispositionstatus;
    }

    /**
     * @param dsclDispositionstatus the dsclDispositionstatus to set
     */
    public void setDsclDispositionstatus(String dsclDispositionstatus) {
        this.dsclDispositionstatus = dsclDispositionstatus;
    }

    /**
     * @return the coiDocumentTypeCode
     */
    public String getCoiDocumentTypeCode() {
        return coiDocumentTypeCode;
    }

    /**
     * @param coiDocumentTypeCode the coiDocumentTypeCode to set
     */
    public void setCoiDocumentTypeCode(String coiDocumentTypeCode) {
        this.coiDocumentTypeCode = coiDocumentTypeCode;
    }

    /**
     * @return the documentType
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     * @param documentType the documentType to set
     */
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
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
    public String getDocument() {
        return document;
    }

    /**
     * @param document the document to set
     */
    public void setDocument(String document) {
        this.document = document;
    }

    /**
     * @return the updateTimestamp
     */
    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    /**
     * @param updateTimestamp the updateTimestamp to set
     */
    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
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

    

}
