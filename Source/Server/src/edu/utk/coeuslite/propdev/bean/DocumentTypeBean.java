/*
 * DocumentTypeBean.java
 *
 * Created on July 18, 2006, 7:59 PM
 */

package edu.utk.coeuslite.propdev.bean;

/**
 *
 * @author  noorula
 */
public class DocumentTypeBean {
    private String documentType;
    private String description;
    private String bioNumber;
    private String timeStamp;
    private String updateUser;
    private String personId;
    private String personName;
    private String fileName;
    private boolean isDocumentPresent;
    
    /** Creates a new instance of DocumentTypeBean */
    public DocumentTypeBean() {
    }
    
    /**
     * Getter for property documentType.
     * @return Value of property documentType.
     */
    public java.lang.String getDocumentType() {
        return documentType;
    }
    
    /**
     * Setter for property documentType.
     * @param documentType New value of property documentType.
     */
    public void setDocumentType(java.lang.String documentType) {
        this.documentType = documentType;
    }
    
    /**
     * Getter for property description.
     * @return Value of property description.
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /**
     * Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    /**
     * Getter for property bioNumber.
     * @return Value of property bioNumber.
     */
    public java.lang.String getBioNumber() {
        return bioNumber;
    }
    
    /**
     * Setter for property bioNumber.
     * @param bioNumber New value of property bioNumber.
     */
    public void setBioNumber(java.lang.String bioNumber) {
        this.bioNumber = bioNumber;
    }
    
    /**
     * Getter for property timeStamp.
     * @return Value of property timeStamp.
     */
    public java.lang.String getTimeStamp() {
        return timeStamp;
    }
    
    /**
     * Setter for property timeStamp.
     * @param timeStamp New value of property timeStamp.
     */
    public void setTimeStamp(java.lang.String timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    /**
     * Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /**
     * Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }
    
    /**
     * Getter for property personId.
     * @return Value of property personId.
     */
    public java.lang.String getPersonId() {
        return personId;
    }
    
    /**
     * Setter for property personId.
     * @param personId New value of property personId.
     */
    public void setPersonId(java.lang.String personId) {
        this.personId = personId;
    }
    
    /**
     * Getter for property personName.
     * @return Value of property personName.
     */
    public java.lang.String getPersonName() {
        return personName;
    }
    
    /**
     * Setter for property personName.
     * @param personName New value of property personName.
     */
    public void setPersonName(java.lang.String personName) {
        this.personName = personName;
    }
    
    /**
     * Getter for property fileName.
     * @return Value of property fileName.
     */
    public java.lang.String getFileName() {
        return fileName;
    }
    
    /**
     * Setter for property fileName.
     * @param fileName New value of property fileName.
     */
    public void setFileName(java.lang.String fileName) {
        this.fileName = fileName;
    }
    
    /**
     * Getter for property isDocumentPresent.
     * @return Value of property isDocumentPresent.
     */
    public boolean isDocumentPresent() {
        return isDocumentPresent;
    }
    
    /**
     * Setter for property isDocumentPresent.
     * @param isDocumentPresent New value of property isDocumentPresent.
     */
    public void setIsDocumentPresent(boolean isDocumentPresent) {
        this.isDocumentPresent = isDocumentPresent;
    }
    
}
