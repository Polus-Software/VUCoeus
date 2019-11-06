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
 * @author Mr
 */
public class CoiCommonAttachmentNoteForm extends ActionForm{
        private String coiDisclosureNumber;
    private String coiSequenceNumber;
    private String entiryNumber;
    private String coiNotepadEntryTypeCode;
    private String title;
    private String comments;
    private String resttrictedView;
    private String updateTimestamp;
    private String updateUser;
    private String acType;
    private String disclosureNumber;
    private int sequenceNumber;
    private String mode;
    private String docType;
    private String docName;
    private String description;
    private String fileName;
    private FormFile document;
    private int entityNumber;
    private byte[] fileBytes;
    private String fileNameHidden;
    private Date updateTimeStamp;

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
     * @return the coiSequenceNumber
     */
    public String getCoiSequenceNumber() {
        return coiSequenceNumber;
    }

    /**
     * @param coiSequenceNumber the coiSequenceNumber to set
     */
    public void setCoiSequenceNumber(String coiSequenceNumber) {
        this.coiSequenceNumber = coiSequenceNumber;
    }

    /**
     * @return the entiryNumber
     */
    public String getEntiryNumber() {
        return entiryNumber;
    }

    /**
     * @param entiryNumber the entiryNumber to set
     */
    public void setEntiryNumber(String entiryNumber) {
        this.entiryNumber = entiryNumber;
    }

    /**
     * @return the coiNotepadEntryTypeCode
     */
    public String getCoiNotepadEntryTypeCode() {
        return coiNotepadEntryTypeCode;
    }

    /**
     * @param coiNotepadEntryTypeCode the coiNotepadEntryTypeCode to set
     */
    public void setCoiNotepadEntryTypeCode(String coiNotepadEntryTypeCode) {
        this.coiNotepadEntryTypeCode = coiNotepadEntryTypeCode;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * @return the resttrictedView
     */
    public String getResttrictedView() {
        return resttrictedView;
    }

    /**
     * @param resttrictedView the resttrictedView to set
     */
    public void setResttrictedView(String resttrictedView) {
        this.resttrictedView = resttrictedView;
    }

    /**
     * @return the updateTimestamp
     */
    public String getUpdateTimestamp() {
        return updateTimestamp;
    }

    /**
     * @param updateTimestamp the updateTimestamp to set
     */
    public void setUpdateTimestamp(String updateTimestamp) {
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
     * @return the docName
     */
    public String getDocName() {
        return docName;
    }

    /**
     * @param docName the docName to set
     */
    public void setDocName(String docName) {
        this.docName = docName;
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
    

}
