/*
 * @(#)ProtocolAmendRenewalBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.bean;

import java.beans.*;
import java.sql.Date;
import edu.mit.coeus.bean.IBaseDataBean;
import java.util.Vector;
/**
 * The class used to hold the information of <code>Protocol Amendments & Renewals</code>
 *
 * @author Prasanna Kumar
 * @version 1.0
 * Created on August 21, 2003, 6:08 PM
 */

public class ProtocolAmendRenewalBean implements java.io.Serializable, IBaseDataBean {

    //holds protocol Amend/Renewal number
    private String protocolAmendRenewalNumber;    
    //holds protocol number
    private String protocolNumber;
    //holds sequence number
    private int sequenceNumber;
    //holds created date
    private java.sql.Date dateCreated;
    //holds Update user
    private String updateUser;
    //holds update Timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds Action type
    private String acType;    
    //holds Summary
    private String summary;
    //holds int protocol status
    private int protocolStatus;
    //holds String protocol Status Description
    private String protocolStatusDescription;
    //holds String protocol module data
    private Vector vecModuleData;
    // Added with CoeusQA2313: Completion of Questionnaire for Submission
    //holds questionnaire selected for this amendment/renewal
    private Vector vecSelectedOrigProtoQnr;
    // CoeusQA2313: Completion of Questionnaire for Submission - End
    
    /** Creates new ProtocolAmendRenewalBean */
    public ProtocolAmendRenewalBean() {
    }

    /**
     * Method used to get the protocol number
     * @return protocolNumber String
     */
    public String getProtocolNumber() {
        return protocolNumber;
    }

    /**
     * Method used to set the protocol number
     * @param protocolNumber String
     */
    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    /**
     * Method used to get the sequence number
     * @return sequenceNumber int
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Method used to set the sequence number
     * @param sequenceNumber int
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
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
    
    /**
     * Method used to get the Action Type
     * @return acType String
     */
    public String getAcType() {
        return acType;
    }

    /**
     * Method used to set the Action Type
     * @param acType String
     */
    public void setAcType(String acType) {
        this.acType = acType;
    }       
    
    /** Getter for property protocolAmendRenewalNumber.
     * @return Value of property protocolAmendRenewalNumber.
     */
    public java.lang.String getProtocolAmendRenewalNumber() {
        return protocolAmendRenewalNumber;
    }
    
    /** Setter for property protocolAmendRenewalNumber.
     * @param protocolAmendRenewalNumber New value of property protocolAmendRenewalNumber.
     */
    public void setProtocolAmendRenewalNumber(java.lang.String protocolAmendRenewalNumber) {
        this.protocolAmendRenewalNumber = protocolAmendRenewalNumber;
    }
    
    /** Getter for property summary.
     * @return Value of property summary.
     */
    public java.lang.String getSummary() {
        return summary;
    }
    
    /** Setter for property summary.
     * @param summary New value of property summary.
     */
    public void setSummary(java.lang.String summary) {
        this.summary = summary;
    }
    
    /** Getter for property dateCreated.
     * @return Value of property dateCreated.
     */
    public java.sql.Date getDateCreated() {
        return dateCreated;
    }
    
    /** Setter for property dateCreated.
     * @param dateCreated New value of property dateCreated.
     */
    public void setDateCreated(java.sql.Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    /** Getter for property protocolStatus.
     * @return Value of property protocolStatus.
     */
    public int getProtocolStatus() {
        return protocolStatus;
    }
    
    /** Setter for property protocolStatus.
     * @param protocolStatus New value of property protocolStatus.
     */
    public void setProtocolStatus(int protocolStatus) {
        this.protocolStatus = protocolStatus;
    }
    
    /** Getter for property protocolStatusDescription.
     * @return Value of property protocolStatusDescription.
     */
    public java.lang.String getProtocolStatusDescription() {
        return protocolStatusDescription;
    }
    
    /** Setter for property protocolStatusDescription.
     * @param protocolStatusDescription New value of property protocolStatusDescription.
     */
    public void setProtocolStatusDescription(java.lang.String protocolStatusDescription) {
        this.protocolStatusDescription = protocolStatusDescription;
    }

    public Vector getVecModuleData() {
        return vecModuleData;
    }

    public void setVecModuleData(Vector vecModuleData) {
        this.vecModuleData = vecModuleData;
    }
    
    // Added with CoeusQA2313: Completion of Questionnaire for Submission
    /** Getter for property vecSelectedOrigProtoQnr.
     * @return Value of property vecSelectedOrigProtoQnr.
     */
    public Vector getVecSelectedOrigProtoQnr() {
        return vecSelectedOrigProtoQnr;
    }
    
    /** Setter for property vecSelectedOrigProtoQnr.
     * @param vecSelectedOrigProtoQnr New value of property vecSelectedOrigProtoQnr.
     */
    public void setVecSelectedOrigProtoQnr(Vector vecSelectedOrigProtoQnr) {
        this.vecSelectedOrigProtoQnr = vecSelectedOrigProtoQnr;
    }
    // CoeusQA2313: Completion of Questionnaire for Submission - End

    
}
