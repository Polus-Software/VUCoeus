/*
 * @(#)DepartmentBioSourceFormBean.java 1.0 03/31/03 12:19 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean;

import java.beans.*;
import java.sql.Timestamp;
/**
 * The class used to hold the information of <code>Proposal Person Biography Source</code>
 * 
 *
 * @author  Prasanna Kumar K
 * @version 1.0
 * Created on April 05, 2004, 12:15 PM
 */

public class ProposalPersonBioSourceBean implements java.io.Serializable {
   //holds proposal number
    private String proposalNumber;
    //holds the person id
    private String personId;
    //holds the bio number
    private int bioNumber;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private Timestamp updateTimestamp;
    //holds account type
    private String acType;    
    //holds the file name
    private String fileName;    
    //holds file data
    private byte[] fileBytes;    
    //holds the source editor
    private String sourceEditor;
    //holds the input type
    private char inputType;
    //holds the platform type
    private char platformType;
    
    /** Creates new ProposalPersonBioSourceBean */
    public ProposalPersonBioSourceBean() {        
    }    

    /** Getter for property proposalNumber.
     * @return Value of property proposalNumber.
     */
    public java.lang.String getProposalNumber() {
        return proposalNumber;
    }
    
    /** Setter for property proposalNumber.
     * @param proposalNumber New value of property proposalNumber.
     */
    public void setProposalNumber(java.lang.String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
    /** Getter for property personId.
     * @return Value of property personId.
     */
    public java.lang.String getPersonId() {
        return personId;
    }
    
    /** Setter for property personId.
     * @param personId New value of property personId.
     */
    public void setPersonId(java.lang.String personId) {
        this.personId = personId;
    }
    
    /** Getter for property bioNumber.
     * @return Value of property bioNumber.
     */
    public int getBioNumber() {
        return bioNumber;
    }
    
    /** Setter for property bioNumber.
     * @param bioNumber New value of property bioNumber.
     */
    public void setBioNumber(int bioNumber) {
        this.bioNumber = bioNumber;
    }
    
    /** Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /** Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }
    
    /** Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /** Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /** Getter for property acType.
     * @return Value of property acType.
     */
    public java.lang.String getAcType() {
        return acType;
    }
    
    /** Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
    }
    
    /** Getter for property fileName.
     * @return Value of property fileName.
     */
    public java.lang.String getFileName() {
        return fileName;
    }
    
    /** Setter for property fileName.
     * @param fileName New value of property fileName.
     */
    public void setFileName(java.lang.String fileName) {
        this.fileName = fileName;
    }
    
    /** Getter for property fileBytes.
     * @return Value of property fileBytes.
     */
    public byte[] getFileBytes() {
        return this.fileBytes;
    }
    
    /** Setter for property fileBytes.
     * @param fileBytes New value of property fileBytes.
     */
    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }
    
    /** Getter for property sourceEditor.
     * @return Value of property sourceEditor.
     */
    public java.lang.String getSourceEditor() {
        return sourceEditor;
    }
    
    /** Setter for property sourceEditor.
     * @param sourceEditor New value of property sourceEditor.
     */
    public void setSourceEditor(java.lang.String sourceEditor) {
        this.sourceEditor = sourceEditor;
    }
    
    /** Getter for property inputType.
     * @return Value of property inputType.
     */
    public char getInputType() {
        return inputType;
    }
    
    /** Setter for property inputType.
     * @param inputType New value of property inputType.
     */
    public void setInputType(char inputType) {
        this.inputType = inputType;
    }
    
    /** Getter for property platformType.
     * @return Value of property platformType.
     */
    public char getPlatformType() {
        return platformType;
    }
    
    /** Setter for property platformType.
     * @param platformType New value of property platformType.
     */
    public void setPlatformType(char platformType) {
        this.platformType = platformType;
    }    
}