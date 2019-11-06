/*
 * ProtocolHeaderDetailsBean.java
 *
 * Created on August 17, 2006, 3:49 PM
 */

package edu.mit.coeuslite.irb.bean;

import java.sql.Date;

/**
 *
 * @author  divyasusendran
 */
public class ProtocolHeaderDetailsBean {
    
    private String protocolNumber;
    private String sequenceNumber;
    private String principalInvestigator;
    private String protocolStatusDescription;
    private Date approvalDate;
    private Date expirationDate;
    
    //Added for CoeusLite4.3 header changes enhacement - Start
    private Date lastApprovalDate;
    //Added for CoeusLite4.3 header changes enhacement - end
    
    private String leadUnit;
    private String unitNumber;
    private String title;
    private String updateTimeStamp;
    private String updateUser;
    private String createTimestamp;
    private String createUser;
    // Added for displaying user name for user Id
    private String updateUserName;
    private String createUserName;
    // End
    //  Added for Amendment Renewal Creation - Start
    private int protocolStatusCode;
    //  Added for Amendment Renewal Creation - End
    
    //Added for case#2490 - Coeus Lite - Protocol Submission - Meeting Date
    private Date scheduledDate;
    
    /** Creates a new instance of ProtocolHeaderDetailsBean */
    public ProtocolHeaderDetailsBean() {
    }
    
    /**
     * Getter for property protocolNumber.
     * @return Value of property protocolNumber.
     */
    public java.lang.String getProtocolNumber() {
        return protocolNumber;
    }
    
    /**
     * Setter for property protocolNumber.
     * @param protocolNumber New value of property protocolNumber.
     */
    public void setProtocolNumber(java.lang.String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }
    
    /**
     * Getter for property sequenceNumber.
     * @return Value of property sequenceNumber.
     */
    public java.lang.String getSequenceNumber() {
        return sequenceNumber;
    }    
    
    /**
     * Setter for property sequenceNumber.
     * @param sequenceNumber New value of property sequenceNumber.
     */
    public void setSequenceNumber(java.lang.String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
    /**
     * Getter for property principalInvestigator.
     * @return Value of property principalInvestigator.
     */
    public java.lang.String getPrincipalInvestigator() {
        return principalInvestigator;
    }
    
    /**
     * Setter for property principalInvestigator.
     * @param principalInvestigator New value of property principalInvestigator.
     */
    public void setPrincipalInvestigator(java.lang.String principalInvestigator) {
        this.principalInvestigator = principalInvestigator;
    }
    
    /**
     * Getter for property protocolStatusDescription.
     * @return Value of property protocolStatusDescription.
     */
    public java.lang.String getProtocolStatusDescription() {
        return protocolStatusDescription;
    }
    
    /**
     * Setter for property protocolStatusDescription.
     * @param protocolStatusDescription New value of property protocolStatusDescription.
     */
    public void setProtocolStatusDescription(java.lang.String protocolStatusDescription) {
        this.protocolStatusDescription = protocolStatusDescription;
    }
    
    /**
     * Getter for property approvalDate.
     * @return Value of property approvalDate.
     */
    public Date getApprovalDate() {
        return approvalDate;
    }
    
    /**
     * Setter for property approvalDate.
     * @param approvalDate New value of property approvalDate.
     */
    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }
    
    /**
     * Getter for property expirationDate.
     * @return Value of property expirationDate.
     */
    public Date getExpirationDate() {
        return expirationDate;
    }
    
    /**
     * Setter for property expirationDate.
     * @param expirationDate New value of property expirationDate.
     */
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
    
    /**
     * Getter for property leadUnit.
     * @return Value of property leadUnit.
     */
    public java.lang.String getLeadUnit() {
        return leadUnit;
    }
    
    /**
     * Setter for property leadUnit.
     * @param leadUnit New value of property leadUnit.
     */
    public void setLeadUnit(java.lang.String leadUnit) {
        this.leadUnit = leadUnit;
    }
    
    /**
     * Getter for property title.
     * @return Value of property title.
     */
    public java.lang.String getTitle() {
        return title;
    }
    
    /**
     * Setter for property title.
     * @param title New value of property title.
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }
    
    /**
     * Getter for property updateTimeStamp.
     * @return Value of property updateTimeStamp.
     */
    public java.lang.String getUpdateTimeStamp() {
        return updateTimeStamp;
    }
    
    /**
     * Setter for property updateTimeStamp.
     * @param updateTimeStamp New value of property updateTimeStamp.
     */
    public void setUpdateTimeStamp(java.lang.String updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
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
     * Getter for property createTimestamp.
     * @return Value of property createTimestamp.
     */
    public java.lang.String getCreateTimestamp() {
        return createTimestamp;
    }
    
    /**
     * Setter for property createTimestamp.
     * @param createTimestamp New value of property createTimestamp.
     */
    public void setCreateTimestamp(java.lang.String createTimestamp) {
        this.createTimestamp = createTimestamp;
    }
    
    /**
     * Getter for property createUser.
     * @return Value of property createUser.
     */
    public java.lang.String getCreateUser() {
        return createUser;
    }
    
    /**
     * Setter for property createUser.
     * @param createUser New value of property createUser.
     */
    public void setCreateUser(java.lang.String createUser) {
        this.createUser = createUser;
    }
    
    /**
     * Getter for property unitNumber.
     * @return Value of property unitNumber.
     */
    public java.lang.String getUnitNumber() {
        return unitNumber;
    }
    
    /**
     * Setter for property unitNumber.
     * @param unitNumber New value of property unitNumber.
     */
    public void setUnitNumber(java.lang.String unitNumber) {
        this.unitNumber = unitNumber;
    }
    
    // Added for displaying user name for user Id
    /**
     * Getter for property updateUserName.
     * @return Value of property updateUserName.
     */
    public java.lang.String getUpdateUserName() {
        return updateUserName;
    }
    
    /**
     * Setter for property updateUserName.
     * @param updateUserName New value of property updateUserName.
     */
    public void setUpdateUserName(java.lang.String updateUserName) {
        this.updateUserName = updateUserName;
    }
    
    /**
     * Getter for property createUserName.
     * @return Value of property createUserName.
     */
    public java.lang.String getCreateUserName() {
        return createUserName;
    }
    
    /**
     * Setter for property createUserName.
     * @param createUserName New value of property createUserName.
     */
    public void setCreateUserName(java.lang.String createUserName) {
        this.createUserName = createUserName;
    }
    
    //Added for CoeusLite4.3 header changes enhacement - Start
    /**
     * Getter for property lastApprovalDate.
     * @return Value of property lastApprovalDate.
     */
    public java.sql.Date getLastApprovalDate() {
        return lastApprovalDate;
    }
    
    /**
     * Setter for property lastApprovalDate.
     * @param lastApprovalDate New value of property lastApprovalDate.
     */
    public void setLastApprovalDate(java.sql.Date lastApprovalDate) {
        this.lastApprovalDate = lastApprovalDate;
    }
    //Added for CoeusLite4.3 header changes enhacement - end

    public int getProtocolStatusCode() {
        return protocolStatusCode;
    }

    public void setProtocolStatusCode(int protocolStatusCode) {
        this.protocolStatusCode = protocolStatusCode;
    }

    //Added for case#2490 - Coeus Lite - Protocol Submission - Meeting Date - start
    public Date getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }
    //Added for case#2490 - Coeus Lite - Protocol Submission - Meeting Date - end
    
}
