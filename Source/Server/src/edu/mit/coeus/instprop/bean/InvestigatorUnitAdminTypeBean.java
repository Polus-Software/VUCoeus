/*
 * InvestigatorUnitAdminTypeBean.java
 *
 * Created on September 25, 2006, 2:14 PM
 */

package edu.mit.coeus.instprop.bean;

import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.IBaseDataBean;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author  tarique
 */
public class InvestigatorUnitAdminTypeBean implements Serializable, CoeusBean, IBaseDataBean{
    String acType;
    String updateUser;
    Timestamp updateTimestamp;
    String moduleNumber;
    int sequenceNumber;
    int unitAdminType;
    String administrator;
    String adminName;
    //for holding old data
    String awModuleNumber;
    int awSequenceNo;
    int awUnitAdminType;
    String awAdministrator;
    //Added for Brown's enhancement
    String phoneNumber;
    String emailAddress;
    //Added for Brown's enhancement
    
    /** Creates a new instance of InvestigatorUnitAdminTypeBean */
    public InvestigatorUnitAdminTypeBean() {
    }
    
    public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) 
                            throws edu.mit.coeus.exception.CoeusException {
        return true;
    }
    
    /**
     * Getter for property acType.
     * @return Value of property acType.
     */
    public java.lang.String getAcType() {
        return acType;
    }    
    
    /**
     * Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
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
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /**
     * Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /**
     * Getter for property unitAdminType.
     * @return Value of property unitAdminType.
     */
    public int getUnitAdminType() {
        return unitAdminType;
    }
    
    /**
     * Setter for property unitAdminType.
     * @param unitAdminType New value of property unitAdminType.
     */
    public void setUnitAdminType(int unitAdminType) {
        this.unitAdminType = unitAdminType;
    }
    
    /**
     * Getter for property administrator.
     * @return Value of property administrator.
     */
    public java.lang.String getAdministrator() {
        return administrator;
    }
    
    /**
     * Setter for property administrator.
     * @param administrator New value of property administrator.
     */
    public void setAdministrator(java.lang.String administrator) {
        this.administrator = administrator;
    }
    
    /**
     * Getter for property moduleNumber.
     * @return Value of property moduleNumber.
     */
    public java.lang.String getModuleNumber() {
        return moduleNumber;
    }
    
    /**
     * Setter for property moduleNumber.
     * @param moduleNumber New value of property moduleNumber.
     */
    public void setModuleNumber(java.lang.String moduleNumber) {
        this.moduleNumber = moduleNumber;
    }
    
    /**
     * Getter for property awModuleNumber.
     * @return Value of property awModuleNumber.
     */
    public java.lang.String getAwModuleNumber() {
        return awModuleNumber;
    }
    
    /**
     * Setter for property awModuleNumber.
     * @param awModuleNumber New value of property awModuleNumber.
     */
    public void setAwModuleNumber(java.lang.String awModuleNumber) {
        this.awModuleNumber = awModuleNumber;
    }
    
    /**
     * Getter for property awSequenceNo.
     * @return Value of property awSequenceNo.
     */
    public int getAwSequenceNo() {
        return awSequenceNo;
    }
    
    /**
     * Setter for property awSequenceNo.
     * @param awSequenceNo New value of property awSequenceNo.
     */
    public void setAwSequenceNo(int awSequenceNo) {
        this.awSequenceNo = awSequenceNo;
    }
    
    /**
     * Getter for property awUnitAdminType.
     * @return Value of property awUnitAdminType.
     */
    public int getAwUnitAdminType() {
        return awUnitAdminType;
    }
    
    /**
     * Setter for property awUnitAdminType.
     * @param awUnitAdminType New value of property awUnitAdminType.
     */
    public void setAwUnitAdminType(int awUnitAdminType) {
        this.awUnitAdminType = awUnitAdminType;
    }
    
    /**
     * Getter for property awAdministrator.
     * @return Value of property awAdministrator.
     */
    public java.lang.String getAwAdministrator() {
        return awAdministrator;
    }
    
    /**
     * Setter for property awAdministrator.
     * @param awAdministrator New value of property awAdministrator.
     */
    public void setAwAdministrator(java.lang.String awAdministrator) {
        this.awAdministrator = awAdministrator;
    }
    
    /**
     * Getter for property adminName.
     * @return Value of property adminName.
     */
    public java.lang.String getAdminName() {
        return adminName;
    }
    
    /**
     * Setter for property adminName.
     * @param adminName New value of property adminName.
     */
    public void setAdminName(java.lang.String adminName) {
        this.adminName = adminName;
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
    
    /**
     * Getter for property phoneNumber.
     * @return Value of property phoneNumber.
     */
    public java.lang.String getPhoneNumber() {
        return phoneNumber;
    }
    
    /**
     * Setter for property phoneNumber.
     * @param phoneNumber New value of property phoneNumber.
     */
    public void setPhoneNumber(java.lang.String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * Getter for property emailAddress.
     * @return Value of property emailAddress.
     */
    public java.lang.String getEmailAddress() {
        return emailAddress;
    }
    
    /**
     * Setter for property emailAddress.
     * @param emailAddress New value of property emailAddress.
     */
    public void setEmailAddress(java.lang.String emailAddress) {
        this.emailAddress = emailAddress;
    }
    
}
