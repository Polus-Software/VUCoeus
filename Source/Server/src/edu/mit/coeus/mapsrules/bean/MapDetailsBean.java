  /*
   * MapDetailsBean.java
   *
   * Created on October 14, 2005, 3:11 PM
   * Copyright (c) Massachusetts Institute of Technology
   * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
   * All rights reserved.
   *
   */

package edu.mit.coeus.mapsrules.bean;

/**
 *
 * @author  vinayks
 */
public class MapDetailsBean extends MapHeaderBean{
    private int levelNumber;
    private int stopNumber;
    private String userId;
    private boolean primayApproverFlag;
    private String detailDescription;
    private int awStopNumber;
    private int awLevelNumber;
    private String awUserId;
    //Added for useid to username enhancement
    private String updateUserName;
    //Added for Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - start
    //Added new fields roleType, roleCode, roleDescription, qualifierCode,
    //qualifierDescription, approverNumber,awApproverNumber
    private boolean roleType;
    private String roleCode;
    private String roleDescription;
    private String qualifierCode;
    private String qualifierDescription;
    private int approverNumber;
    private int awApproverNumber;
    //Added for Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end
    
    
    /** Creates a new instance of MapUnitDetailsBean */
    public MapDetailsBean() {
    }
    
    /**
     * Getter for property levelNumber.
     * @return Value of property levelNumber.
     */
    public int getLevelNumber() {
        return levelNumber;
    }
    
    /**
     * Setter for property levelNumber.
     * @param levelNumber New value of property levelNumber.
     */
    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }
    
    /**
     * Getter for property stopNumber.
     * @return Value of property stopNumber.
     */
    public int getStopNumber() {
        return stopNumber;
    }
    
    /**
     * Setter for property stopNumber.
     * @param stopNumber New value of property stopNumber.
     */
    public void setStopNumber(int stopNumber) {
        this.stopNumber = stopNumber;
    }
    
    /**
     * Getter for property userId.
     * @return Value of property userId.
     */
    public java.lang.String getUserId() {
        return userId;
    }
    
    /**
     * Setter for property userId.
     * @param userId New value of property userId.
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }
    
    /**
     * Getter for property primayApproverFlag.
     * @return Value of property primayApproverFlag.
     */
    public boolean isPrimayApproverFlag() {
        return primayApproverFlag;
    }
    
    /**
     * Setter for property primayApproverFlag.
     * @param primayApproverFlag New value of property primayApproverFlag.
     */
    public void setPrimayApproverFlag(boolean primayApproverFlag) {
        this.primayApproverFlag = primayApproverFlag;
    }
    
    /**
     * Getter for property detailDescription.
     * @return Value of property detailDescription.
     */
    public java.lang.String getDetailDescription() {
        return detailDescription;
    }
    
    /**
     * Setter for property detailDescription.
     * @param detailDescription New value of property detailDescription.
     */
    public void setDetailDescription(java.lang.String detailDescription) {
        this.detailDescription = detailDescription;
    }
    
    /**
     * Getter for property awStopNumber.
     * @return Value of property awStopNumber.
     */
    public int getAwStopNumber() {
        return awStopNumber;
    }
    
    /**
     * Setter for property awStopNumber.
     * @param awStopNumber New value of property awStopNumber.
     */
    public void setAwStopNumber(int awStopNumber) {
        this.awStopNumber = awStopNumber;
    }
    
    /**
     * Getter for property awLevelNumber.
     * @return Value of property awLevelNumber.
     */
    public int getAwLevelNumber() {
        return awLevelNumber;
    }
    
    /**
     * Setter for property awLevelNumber.
     * @param awLevelNumber New value of property awLevelNumber.
     */
    public void setAwLevelNumber(int awLevelNumber) {
        this.awLevelNumber = awLevelNumber;
    }
    
    /**
     * Getter for property awUserId.
     * @return Value of property awUserId.
     */
    public java.lang.String getAwUserId() {
        return awUserId;
    }
    
    /**
     * Setter for property awUserId.
     * @param awUserId New value of property awUserId.
     */
    public void setAwUserId(java.lang.String awUserId) {
        this.awUserId = awUserId;
    }
    
    //Useid to username enhancement - Start
    /**
     * Getter for property updateUserName.
     * @return value of property updateUserName.
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
    //Userid to username enhancement - End
    
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
    
    /**
     * Getter for property awApproverNumber
     * @return value of property awApproverNumber
     */
    public int getAwApproverNumber() {
        return awApproverNumber;
    }
    
    /**
     * Setter for property awApproverNumber
     * @param awApproverNumber New value of property awApproverNumber
     */
    public void setAwApproverNumber(int awApproverNumber) {
        this.awApproverNumber = awApproverNumber;
    }
    //Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end
}
