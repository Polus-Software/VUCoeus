/*
 * @(#)ProtoCorrespRecipientsBean.java October 24, 2002, 12:49 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.bean;

import java.beans.*;

/**
 * The class used to hold the information of <code>Protocol Correspondents</code>
 *
 * @author  Prasanna Kumar
 * @version 1.0
 * Created on October 24, 2002, 12:49 PM
 */

public class ProtoCorrespRecipientsBean implements java.io.Serializable {
    //holds protocol number
    private String protocolNumber;
    //holds action Id
    private int actionId;
    //holds Proto Corresp type code
    private int protoCorrespTypeCode;
    //holds Proto Corresp Description
    private String protoCorrespDescription;
    //holds person id
    private String personId;
    //holds person name
    private String personName;
    //holds non employee flag
    private boolean nonEmployeeFlag;
    //holds Mail Id
    private String mailId;  
    
    private int awProtoCorrespTypeCode;
    //holds correspondent comments
    private String comments;
    //holds no of copies
    private int numberOfCopies;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds Action type
    private String acType;
    // Added for Coeus 4.3 enhancement - starts
    private boolean finalFlag;
    private int sequenceNumber;
    // Added for Coeus 4.3 enhancement - ends

    /** Getter for property protocolNumber.
     * @return Value of property protocolNumber.
     */
    public java.lang.String getProtocolNumber() {
        return protocolNumber;
    }
    
    /** Setter for property protocolNumber.
     * @param protocolNumber New value of property protocolNumber.
     */
    public void setProtocolNumber(java.lang.String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }
    
    /** Getter for property actionId.
     * @return Value of property actionId.
     */
    public int getActionId() {
        return actionId;
    }
    
    /** Setter for property actionId.
     * @param actionId New value of property actionId.
     */
    public void setActionId(int actionId) {
        this.actionId = actionId;
    }
    
    /** Getter for property protoCorrespTypeCode.
     * @return Value of property protoCorrespTypeCode.
     */
    public int getProtoCorrespTypeCode() {
        return protoCorrespTypeCode;
    }
    
    /** Setter for property protoCorrespTypeCode.
     * @param protoCorrespTypeCode New value of property protoCorrespTypeCode.
     */
    public void setProtoCorrespTypeCode(int protoCorrespTypeCode) {
        this.protoCorrespTypeCode = protoCorrespTypeCode;
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
    
    /** Getter for property personName.
     * @return Value of property personName.
     */
    public java.lang.String getPersonName() {
        return personName;
    }
    
    /** Setter for property personName.
     * @param personName New value of property personName.
     */
    public void setPersonName(java.lang.String personName) {
        this.personName = personName;
    }
    
    /** Getter for property nonEmployeeFlag.
     * @return Value of property nonEmployeeFlag.
     */
    public boolean isNonEmployeeFlag() {
        return nonEmployeeFlag;
    }
    
    /** Setter for property nonEmployeeFlag.
     * @param nonEmployeeFlag New value of property nonEmployeeFlag.
     */
    public void setNonEmployeeFlag(boolean nonEmployeeFlag) {
        this.nonEmployeeFlag = nonEmployeeFlag;
    }
    
    /** Getter for property awProtoCorrespTypeCode.
     * @return Value of property awProtoCorrespTypeCode.
     */
    public int getAwProtoCorrespTypeCode() {
        return awProtoCorrespTypeCode;
    }
    
    /** Setter for property awProtoCorrespTypeCode.
     * @param awProtoCorrespTypeCode New value of property awProtoCorrespTypeCode.
     */
    public void setAwProtoCorrespTypeCode(int awProtoCorrespTypeCode) {
        this.awProtoCorrespTypeCode = awProtoCorrespTypeCode;
    }
    
    /** Getter for property comments.
     * @return Value of property comments.
     */
    public java.lang.String getComments() {
        return comments;
    }
    
    /** Setter for property comments.
     * @param comments New value of property comments.
     */
    public void setComments(java.lang.String comments) {
        this.comments = comments;
    }
    
    /** Getter for property numberOfCopies.
     * @return Value of property numberOfCopies.
     */
    public int getNumberOfCopies() {
        return numberOfCopies;
    }
    
    /** Setter for property numberOfCopies.
     * @param numberOfCopies New value of property numberOfCopies.
     */
    public void setNumberOfCopies(int numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
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
    
    /** Getter for property mailId.
     * @return Value of property mailId.
     */
    public java.lang.String getMailId() {
        return mailId;
    }
    
    /** Setter for property mailId.
     * @param mailId New value of property mailId.
     */
    public void setMailId(java.lang.String mailId) {
        this.mailId = mailId;
    }
    
    public String toString(){
        StringBuffer strBffr = new StringBuffer();
        strBffr.append("Action Id : "+getActionId());
        strBffr.append("protocolNumber : "+getProtocolNumber());
        strBffr.append("protoCorrespTypeCode : "+getProtoCorrespTypeCode());
        strBffr.append("personId : "+getPersonId());    
        strBffr.append("personName : " +getPersonName());
        strBffr.append("nonEmployeeFlag : " +isNonEmployeeFlag());
        strBffr.append("mailId : "+getMailId());
        strBffr.append("awProtoCorrespTypeCode :"+getAwProtoCorrespTypeCode());  
        strBffr.append("comments : "+getComments());
        strBffr.append("numberOfCopies :"+getNumberOfCopies());
        strBffr.append("updateUser : "+getUpdateUser());
        strBffr.append("updateTimestamp : " +getUpdateTimestamp());
        strBffr.append("acType : "+getAcType());
        return strBffr.toString();
    }
    
    /** Getter for property protoCorrespDescription.
     * @return Value of property protoCorrespDescription.
     */
    public java.lang.String getProtoCorrespDescription() {
        return protoCorrespDescription;
    }
    
    /** Setter for property protoCorrespDescription.
     * @param protoCorrespDescription New value of property protoCorrespDescription.
     */
    public void setProtoCorrespDescription(java.lang.String protoCorrespDescription) {
        this.protoCorrespDescription = protoCorrespDescription;
    }
    
    /**
     * Getter for property finalFlag.
     * @return Value of property finalFlag.
     */
    public boolean isFinalFlag() {
        return finalFlag;
    }
    
    /**
     * Setter for property finalFlag.
     * @param finalFlag New value of property finalFlag.
     */
    public void setFinalFlag(boolean finalFlag) {
        this.finalFlag = finalFlag;
    }
    
    /**
     * Getter for property sequenceNumber.
     * @return Value of property sequenceNumber.
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    
    /**
     * Setter for property sequenceNumber.
     * @param sequenceNumber New value of property sequenceNumber.
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
}
