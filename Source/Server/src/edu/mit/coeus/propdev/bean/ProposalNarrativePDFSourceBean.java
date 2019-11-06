/*
 * @(#)ProposalNarrativePDFSourceBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


package edu.mit.coeus.propdev.bean;

import edu.mit.coeus.bean.CoeusAttachmentBean;
import java.sql.Timestamp;
import java.beans.*;
/**
 * The class used to hold the information of <code>Propsoal Narrative PDF</code>
 *
 * 
 * @author  Prasanna Kumar
 * @version 1.0
 * Created on January 24, 2004, 3:15 PM
 */
public class ProposalNarrativePDFSourceBean extends CoeusAttachmentBean implements java.io.Serializable {
    
    private String proposalNumber;
    private int moduleNumber;
    //Removed with case 4007:Icon based on attachement Type
//    private byte[] fileBytes;
//    private String fileName;
    private String inputType;
    private String platFormType;
    
    private Timestamp updateTimestamp;
    private String updateUser;
    private String acType;    
    //Added for case id 3183 - start
    private String updateUserName;
    //Added for case id 3183 - end
    /** Creates a new instance of CorrespondenceFormBean */
    public ProposalNarrativePDFSourceBean() {
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
    
    /** Getter for property moduleNumber.
     * @return Value of property moduleNumber.
     */
    public int getModuleNumber() {
        return moduleNumber;
    }
    
    /** Setter for property moduleNumber.
     * @param moduleNumber New value of property moduleNumber.
     */
    public void setModuleNumber(int moduleNumber) {
        this.moduleNumber = moduleNumber;
    }
  
    /** Getter for property inputType.
     * @return Value of property inputType.
     */
    public java.lang.String getInputType() {
        return inputType;
    }
    
    /** Setter for property inputType.
     * @param inputType New value of property inputType.
     */
    public void setInputType(java.lang.String inputType) {
        this.inputType = inputType;
    }
    
    /** Getter for property platFormType.
     * @return Value of property platFormType.
     */
    public java.lang.String getPlatFormType() {
        return platFormType;
    }
    
    /** Setter for property platFormType.
     * @param platFormType New value of property platFormType.
     */
    public void setPlatFormType(java.lang.String platFormType) {
        this.platFormType = platFormType;
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
    
    //Added for case id 3183 - start
    /** Setter for property updateUserName.
     * @param updateUserName New value of property updateUserName.
     */
    public void setUpdateUserName(String updateUserName){
        this.updateUserName = updateUserName;
    }
    
    /** Getter for property updateUserName.
     * @return Value of property updateUserName.
     */
     public String getUpdateUserName() {
        return updateUserName;
    }
     //Added for case id 3183 - end
}