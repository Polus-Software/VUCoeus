/**
 * @(#)UnitMapDetailsBean.java 1.0 01/02/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.propdev.bean;

import java.sql.Timestamp;
import java.util.Vector;
import edu.mit.coeus.bean.BaseBean;
/**
 * This class used to set and get Unit Map details
  *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on January 02, 2004 11:30 PM
 */

public class UnitMapDetailsBean implements BaseBean, java.io.Serializable{
    
    private int mapId;
    private int levelNumber;
    private int stopNumber;
    private String userId;    
    private boolean primaryApproverFlag;
    private String description;
    private Timestamp updateTimeStamp;
    private String updateUser;
    //Added for Userid to Username Enhancement
    private String updateUserName;
    private String acType;
    //Added for Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - start
    //Added new fields roleType, roleCode, roleDescription, qualifierCode,
    //qualifierDescription, approverNumber,awApproverNumber
    private boolean roleType;
    private String roleCode;
    private String roleDescription;
    private String qualifierCode;
    private String qualifierDescription;
    private int approverNumber;
    //Added for Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end
    /**
     *	Default Constructor
     */
    
    public UnitMapDetailsBean(){
    }
    
    /** Getter for property mapId.
     * @return Value of property mapId.
     */
    public int getMapId() {
        return mapId;
    }    

    /** Setter for property mapId.
     * @param mapId New value of property mapId.
     */
    public void setMapId(int mapId) {
        this.mapId = mapId;
    }   

    /** Getter for property levelNumber.
     * @return Value of property levelNumber.
     */
    public int getLevelNumber() {
        return levelNumber;
    }    
    
    /** Setter for property levelNumber.
     * @param levelNumber New value of property levelNumber.
     */
    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }
    
    /** Getter for property stopNumber.
     * @return Value of property stopNumber.
     */
    public int getStopNumber() {
        return stopNumber;
    }
    
    /** Setter for property stopNumber.
     * @param stopNumber New value of property stopNumber.
     */
    public void setStopNumber(int stopNumber) {
        this.stopNumber = stopNumber;
    }
    
    /** Getter for property userId.
     * @return Value of property userId.
     */
    public java.lang.String getUserId() {
        return userId;
    }
    
    /** Setter for property userId.
     * @param userId New value of property userId.
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }
    
    /** Getter for property primaryApproverFlag.
     * @return Value of property primaryApproverFlag.
     */
    public boolean isPrimaryApproverFlag() {
        return primaryApproverFlag;
    }
    
    /** Setter for property primaryApproverFlag.
     * @param primaryApproverFlag New value of property primaryApproverFlag.
     */
    public void setPrimaryApproverFlag(boolean primaryApproverFlag) {
        this.primaryApproverFlag = primaryApproverFlag;
    }
    
    /** Getter for property description.
     * @return Value of property description.
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /** Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    /** Getter for property updateTimeStamp.
     * @return Value of property updateTimeStamp.
     */
    public java.sql.Timestamp getUpdateTimeStamp() {
        return updateTimeStamp;
    }
    
    /** Setter for property updateTimeStamp.
     * @param updateTimeStamp New value of property updateTimeStamp.
     */
    public void setUpdateTimeStamp(java.sql.Timestamp updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
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

    //UserId to UserName Enhancement - Start
    /** Getter for property updateUserName.
     * @return Value of property updateUserName.
     */
    public java.lang.String getUpdateUserName() {
        return updateUserName;
    }
    
    /** Setter for property updateUserName.
     * @param updateUsername New value of property updateUserName.
     */
    public void setUpdateUserName(java.lang.String updateUserName) {
        this.updateUserName = updateUserName;
    }
    //UserId to UserName Enhancement - End

    //Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - start
    /**
     * Getter for property roleType
     * @return value of property roleType
     */
    public boolean isRoleType() {
        return roleType;
    }
    
    /**
     * Setter for property roleType
     * @param roleType New value of property roleType.
     */
    public void setRoleType(boolean roleType) {
        this.roleType = roleType;
    }
    
    /**
     * Getter for property qualifierCode
     * @return value of property qualifierCode
     */
    public String getQualifierCode() {
        return qualifierCode;
    }
    
    /**
     * Setter for property qualifierCode
     * @param qualifierCode New value of property qualifierCode
     */
    public void setQualifierCode(String qualifierCode) {
        this.qualifierCode = qualifierCode;
    }
    
    /**
     * Getter for property qualifierDescription
     * @return value of property qualifierDescription
     */
    public String getQualifierDescription() {
        return qualifierDescription;
    }
    
    /**
     * Setter for property qualifierDescription
     * @param qualifierDescription New value of property qualifierDescription
     */
    public void setQualifierDescription(String qualifierDescription) {
        this.qualifierDescription = qualifierDescription;
    }
    
    /**
     * Getter for property roleCode
     * @return value of property roleCode
     */
    public String getRoleCode() {
        return roleCode;
    }
    
    /**
     * Setter for property roleCode
     * @param roleCode New value of property roleCode
     */
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
    
    /**
     * Getter for property roleDescription
     * @return value of property roleDescription
     */
    public String getRoleDescription() {
        return roleDescription;
    }
    
    /**
     * Setter for property roleDescription
     * @param roleDescription New value of property roleDescription
     */
    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }
    
    /**
     * Getter for property approverNumber
     * @return value of property approverNumber
     */
    public int getApproverNumber() {
        return approverNumber;
    }
    
    /**
     * Setter for property approverNumber
     * @param approverNumber New value of property approverNumber
     */
    public void setApproverNumber(int approverNumber) {
        this.approverNumber = approverNumber;
    }
    //Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end
}