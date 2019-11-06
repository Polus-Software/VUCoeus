/*
 * @(#)UnitDetailFormBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
* Created on August 28, 2002, 9:29 AM
 * @author  Geo Thomas
 * @version 1.0
 */

package edu.mit.coeus.unit.bean;

import edu.mit.coeus.utils.UtilFactory;
import java.sql.Timestamp;
/**
 *  The class used to hold the information of <code>Unit Details</code>
 *  The data will get attached with the <code>Unit Detail</code> form.
 */

public class UnitDetailFormBean implements java.io.Serializable{

    //holds unit number
    private String unitNumber;
    //holds parent unit number
    private String parentUnitNumber;
    //holds unit name
    private String unitName;
    //holds parent unit name
    private String parentUnitName;
    //holds admin officer id
    private String adminOfficerId;
    //holds admin officer full name
    private String adminOfficerName;
    //holds unit head id
    private String unitHeadId;
    //holds unit head full name
    private String unitHeadName;
    //holds dean vp id
    private String deanVpId;
    //holds dean vp full name
    private String deanVpName;
    //holds person id of other individual to notify
    private String otherIndToNotifyId;
    //holds full name of other individual to notify
    private String otherIndToNotifyName;
    //holds osp administrator id
    private String ospAdminId;
    //holds osp administrator full name
    private String ospAdminName;
    // holds the flag whether the unit has children
    private String childrenFlag;
    //holds update tmiestamp
    private java.sql.Timestamp updateTimestamp;
    //holds update user id
    private String updateUser;
    //ele addition 4-7-04
    //holds major subdivision unit name
    private String majorSubdivisionUnitName;
    //Added for Organization name enhancement start 1
    private String organizationName;
    private String organizationId;
    //Added for Organization name enhancement end 1
    
    /* JM 7-14-2015 added status */
    private String status;
    
    /** Creates new UnitDetailFormBean */
    public UnitDetailFormBean() {
    }
    
    /* JM 7-14-2015 added status */
    /**
     *  Method used to get status
     * @return status
     */
    public String getStatus() {
        return status;
    }
    
    /**  Method used to set status
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }
    /* JM END */
    
    
    /**
     *  Method used to get the unit number
     * @return Unit Number
     */
    public String getUnitNumber() {
        return unitNumber;
    }
    
    /**  Method used to set the unit number
     * @param unitNumber  Unit Number
     */
    public void setUnitNumber(String unitNumber) {
        this.unitNumber = checkNullStr(unitNumber);
    }

    /**
     *  Method used to get the parent unit number
     * @return Parent Unit Number
     */
    public String getParentUnitNumber() {
        return parentUnitNumber;
    }
    
    /**  Method used to set the parent unit number
     * @param parentUnitNumber  Parent Unit Number 
     */
    public void setParentUnitNumber(String parentUnitNumber) {
        this.parentUnitNumber = checkNullStr(parentUnitNumber);
    }

    /**
     *  Method used to get the parent unit name
     * @return Parent Unit Name
     */
    public String getParentUnitName() {
        return parentUnitName;
    }
    
    /**  Method used to set the parent unit name
     * @param parentUnitName  parent unit name 
     */
    public void setParentUnitName(String parentUnitName) {
        this.parentUnitName = checkNullStr(parentUnitName);
    }
    
    /**
     *  Method used to get the person id of administrative officer 
     * @return Administrative Officer Id
     */
    public String getAdminOfficerId() {
        return adminOfficerId;
    }
    
    /**  Method used to set the perosn id of administrative officer
     * @param adminOfficerId  administrative officer id 
     */
    public void setAdminOfficerId(String adminOfficerId) {
        this.adminOfficerId = checkNullStr(adminOfficerId);
    }
    
    /**
     *  Method used to get the fullname of administrative officer
     * @return Administrative Officer Fullname
     */
    public String getAdminOfficerName() {
        return adminOfficerName;
    }
    
    /**  Method used to set the fullname of administrative officer
     * @param adminOfficerName  administrative officer name */
    public void setAdminOfficerName(String adminOfficerName) {
        this.adminOfficerName = checkNullStr(adminOfficerName);
    }
    
    /**
     *  Method used to get the unit name
     * @return Unit Name
     */
    public String getUnitName() {
        return unitName;
    }
    
    /**  Method used to set the unit name
     * @param unitName  unit name
     */
    public void setUnitName(String unitName) {
        this.unitName = checkNullStr(unitName);
    }

    /**
     *  Method used to get the person id of unit head
     * @return Unit Head Id
     */
    public String getUnitHeadId() {
        return unitHeadId;
    }
    
    /**  Method used to set the person id of unit head
     * @param unitHeadId  unit head
     */
    public void setUnitHeadId(String unitHeadId) {
        this.unitHeadId = checkNullStr(unitHeadId);
    }
    
    /**
     *  Method used to get the fullname of unit head
     * @return Unit Head Fullname
     */
    public String getUnitHeadName() {
        return unitHeadName;
    }
    
    /**  Method used to set the fullname of unit head
     * @param unitHeadName  unit head
     */
    public void setUnitHeadName(String unitHeadName) {
        this.unitHeadName = checkNullStr(unitHeadName);
    }
    
    /**
     *  Method used to get the person id of dean vp
     * @return Dean Vp Id
     */
    public String getDeanVpId() {
        return deanVpId;
    }
    
    /**  Method used to set the person id of dean vp
     * @param deanVpId  dean vp
     */
    public void setDeanVpId(String deanVpId) {
        this.deanVpId = checkNullStr(deanVpId);
    }
    
    /**
     *  Method used to get the fullname of dean vp
     * @return Dean Vp Fullname
     */
    public String getDeanVpName() {
        return deanVpName;
    }
    
    /**  Method used to set the fullname of dean vp
     * @param deanVpName  dean vp
     */
    public void setDeanVpName(String deanVpName) {
        this.deanVpName = checkNullStr(deanVpName);
    }
    
    /**
     *  Method used to get the person id of other individual to notify
     * @return Other Individual To Notify Id
     */
    public String getOtherIndToNotifyId() {
        return otherIndToNotifyId;
    }
    
    /**  Method used to set the person id of other individual to notify
     * @param otherIndToNotifyId other individual 
     */
    public void setOtherIndToNotifyId(String otherIndToNotifyId) {
        this.otherIndToNotifyId = checkNullStr(otherIndToNotifyId);
    }
    
    /**
     *  Method used to get the fullname of other individual to notify
     * @return Other Individual To Notify Fullname
     */
    public String getOtherIndToNotifyName() {
        return otherIndToNotifyName;
    }
    
    /**  Method used to set the fullname of other individual to notify
     * @param otherIndToNotifyName  other individual 
     */
    public void setOtherIndToNotifyName(String otherIndToNotifyName) {
        this.otherIndToNotifyName = checkNullStr(otherIndToNotifyName);
    }
    
    /**
     *  Method used to get the person id of osp administrator
     * @return Osp Administrator Id
     */
    public String getOspAdminId() {
        return ospAdminId;
    }
    
    /**  Method used to set the person id of osp administrator
     * @param ospAdminId  osp administrator
     */
    public void setOspAdminId(String ospAdminId) {
        this.ospAdminId = checkNullStr(ospAdminId);
    }
    
    /**
     *  Method used to get the updated timestamp
     * @return Updated Timestamp
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /**  Method used to set the updated timestamp
     * @param updateTimestamp  updated timestamp
     */
    public void setUpdateTimestamp(String updateTimestamp) {
      this.updateTimestamp = java.sql.Timestamp.valueOf(updateTimestamp);
    }

    /**  Method used to set the updated timestamp
     * @param updateTimestamp updateTimestamp  
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
      this.updateTimestamp = updateTimestamp;
    }
    
    /**
     *  Method used to get the update user id
     * @return Update User Id
     */
    public String getUpdateUser() {
        return updateUser;
    }
    
    /**  Method used to set the update user id
     * @param updateUser  update user
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = checkNullStr(updateUser);
    }

    /**
     *  Method used to get the fullname of Osp Administrator
     * @return Osp Administrator Fullname
     */
    public String getOspAdminName() {
        return ospAdminName;
    }
    
    /**  Method used to set the fullname of Osp Administrator
     * @param ospAdminName  Osp Administrator
     */
    public void setOspAdminName(String ospAdminName) {
        this.ospAdminName = checkNullStr(ospAdminName);
    }
    
    //start addition by ele 4-7-04
      /**
     *  Method used to get the major subdivision unit name
     * @return major subdivision Unit Name
     */
    public String getMajorSubdivisionUnitName() {
        return majorSubdivisionUnitName;
    }
    
    /**  Method used to set the majorSubdivisionUnitName
     * @param majorSubdivisionUnitName  major subdivision unit name
     */
    public void setMajorSubdivisionUnitName(String majorSubdivisionUnitName) {
        this.majorSubdivisionUnitName = checkNullStr(majorSubdivisionUnitName);
    }
    //end ele addition
    private String checkNullStr(String str){
        return ("null".equalsIgnoreCase(str) ? null:str);
    }    
    /**
     * This method is used to return the string value of the Unit Detail Bean
     * @return  String Unit information.
     */    
    public String toString(){
        StringBuffer strBffr = new StringBuffer();
        strBffr.append("{UnitNumber=>"+unitNumber);
        strBffr.append("ParentUnitNumber=>"+parentUnitNumber);
        strBffr.append("UnitName=>"+unitName);
        strBffr.append("AdminOfficerId=>"+adminOfficerId);
        strBffr.append("AdminOfficerName=>"+adminOfficerName);
        strBffr.append("UnitHeadId=>"+unitHeadId);
        strBffr.append("UnitHeadName=>"+unitHeadName);
        strBffr.append("DeanVpId=>"+deanVpId);
        strBffr.append("DeanVpName=>"+deanVpName);
        strBffr.append("OtherIndToNotifyId=>"+otherIndToNotifyId);
        strBffr.append("OtherIndToNotifyName=>"+otherIndToNotifyName);
        strBffr.append("OspAdminId=>"+ospAdminId);
        strBffr.append("OspAdminName=>"+ospAdminName);
        strBffr.append("UpdateTimestamp=>"+updateTimestamp);
        strBffr.append("UpdateUser=>"+updateUser);
        return strBffr.toString();
    }
    //Added for Organization name enhancement start 2
    /**
     * Getter for property organizationName.
     * @return Value of property organizationName.
     */
    public java.lang.String getOrganizationName() {
        return organizationName;
    }
    
    /**
     * Setter for property organizationName.
     * @param organizationName New value of property organizationName.
     */
    public void setOrganizationName(java.lang.String organizationName) {
        this.organizationName = organizationName;
    }
    
    /**
     * Getter for property organizationId.
     * @return Value of property organizationId.
     */
    public java.lang.String getOrganizationId() {
        return organizationId;
    }
    
    /**
     * Setter for property organizationId.
     * @param organizationId New value of property organizationId.
     */
    public void setOrganizationId(java.lang.String organizationId) {
        this.organizationId = organizationId;
    }
    //Added for Organization name enhancement end 2
}