/*
 * @(#)ProposalHierarchyLinkBean.java 1.0
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * Created on December 4, 2007, 8:10 PM
 */

package edu.mit.coeus.propdev.bean;

import edu.mit.coeus.bean.BaseBean;

/**
 *
 * @author  divyasusendran
 */
public class ProposalHierarchyLinkBean implements BaseBean, java.io.Serializable {
    
    private int linkNumber;
    
    private String parentProposalNumber;
    
    private int parentModuleNumber;
    
    private String personId;
    
    private String childProposalNumber;
    
    private int childModuleNumber;
    
    private int documentTypeCode;
    
    private String linkType;
    
    private String updateUser;
    
    private java.sql.Timestamp updateTimestamp;    
    
    /** Creates a new instance of ProposalHierarchyLinkBean */
    public ProposalHierarchyLinkBean() {
    }
    
    /**
     * Getter for property linkNumber.
     * @return Value of property linkNumber.
     */
    public int getLinkNumber() {
        return linkNumber;
    }
    
    /**
     * Setter for property linkNumber.
     * @param linkNumber New value of property linkNumber.
     */
    public void setLinkNumber(int linkNumber) {
        this.linkNumber = linkNumber;
    }
    
    /**
     * Getter for property parentProposalNumber.
     * @return Value of property parentProposalNumber.
     */
    public java.lang.String getParentProposalNumber() {
        return parentProposalNumber;
    }
    
    /**
     * Setter for property parentProposalNumber.
     * @param parentProposalNumber New value of property parentProposalNumber.
     */
    public void setParentProposalNumber(java.lang.String parentProposalNumber) {
        this.parentProposalNumber = parentProposalNumber;
    }
    
    /**
     * Getter for property parentModuleNumber.
     * @return Value of property parentModuleNumber.
     */
    public int getParentModuleNumber() {
        return parentModuleNumber;
    }
    
    /**
     * Setter for property parentModuleNumber.
     * @param parentModuleNumber New value of property parentModuleNumber.
     */
    public void setParentModuleNumber(int parentModuleNumber) {
        this.parentModuleNumber = parentModuleNumber;
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
     * Getter for property childProposalNumber.
     * @return Value of property childProposalNumber.
     */
    public java.lang.String getChildProposalNumber() {
        return childProposalNumber;
    }
    
    /**
     * Setter for property childProposalNumber.
     * @param childProposalNumber New value of property childProposalNumber.
     */
    public void setChildProposalNumber(java.lang.String childProposalNumber) {
        this.childProposalNumber = childProposalNumber;
    }
    
    /**
     * Getter for property childModuleNumber.
     * @return Value of property childModuleNumber.
     */
    public int getChildModuleNumber() {
        return childModuleNumber;
    }
    
    /**
     * Setter for property childModuleNumber.
     * @param childModuleNumber New value of property childModuleNumber.
     */
    public void setChildModuleNumber(int childModuleNumber) {
        this.childModuleNumber = childModuleNumber;
    }
    
    /**
     * Getter for property documentTypeCode.
     * @return Value of property documentTypeCode.
     */
    public int getDocumentTypeCode() {
        return documentTypeCode;
    }
    
    /**
     * Setter for property documentTypeCode.
     * @param documentTypeCode New value of property documentTypeCode.
     */
    public void setDocumentTypeCode(int documentTypeCode) {
        this.documentTypeCode = documentTypeCode;
    }
    
    /**
     * Getter for property linkType.
     * @return Value of property linkType.
     */
    public java.lang.String getLinkType() {
        return linkType;
    }
    
    /**
     * Setter for property linkType.
     * @param linkType New value of property linkType.
     */
    public void setLinkType(java.lang.String linkType) {
        this.linkType = linkType;
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
     * Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /**
     * Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
}
