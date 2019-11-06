/* 
 * @(#)RoleInfoBean.java 1.0 04/10/03 5:41 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.bean;

/**
 * The class used to hold the information of <code>Role details</code>
 *
 * @author  ravikanth
 * @version 1.0
 * Created on April 10, 2003, 5:41 PM
 */
public class RoleInfoBean implements java.io.Serializable {
    //holds the role id
    private int roleId;
    //holds the role description
    private String roleDesc;
    //holds the role name
    private String roleName;
    //holds the role type
    private char roleType;
    //holds the unit number
    private String unitNumber;
    //holds the unit Name
    private String unitName;    
    //holds the descend
    private boolean descend;
    //holds the status
    private char status;
    //holds the user id
    private String userId;
    //holds the ac type
    private String acType;    
    
    private String updateUser;
    private java.sql.Timestamp updateTimestamp;
    
    /** Creates a new instance of RoleInfoBean */
    public RoleInfoBean() {
    }
    
    /** Getter for property unitNumber.
     * @return Value of property unitNumber.
     */
    public java.lang.String getUnitNumber() {
        return unitNumber;
    }
    
    /** Setter for property unitNumber.
     * @param unitNumber New value of property unitNumber.
     */
    public void setUnitNumber(java.lang.String unitNumber) {
        this.unitNumber = unitNumber;
    }
    
    /** Getter for property roleType.
     * @return Value of property roleType.
     */
    public char getRoleType() {
        return roleType;
    }
    
    /** Setter for property roleType.
     * @param roleType New value of property roleType.
     */
    public void setRoleType(char roleType) {
        this.roleType = roleType;
    }
    
    /** Getter for property roleName.
     * @return Value of property roleName.
     */
    public java.lang.String getRoleName() {
        return roleName;
    }
    
    /** Setter for property roleName.
     * @param roleName New value of property roleName.
     */
    public void setRoleName(java.lang.String roleName) {
        this.roleName = roleName;
    }
    
    /** Getter for property roleDesc.
     * @return Value of property roleDesc.
     */
    public java.lang.String getRoleDesc() {
        return roleDesc;
    }
    
    /** Setter for property roleDesc.
     * @param roleDesc New value of property roleDesc.
     */
    public void setRoleDesc(java.lang.String roleDesc) {
        this.roleDesc = roleDesc;
    }
    
    /** Getter for property roleId.
     * @return Value of property roleId.
     */
    public int getRoleId() {
        return roleId;
    }
    
    /** Setter for property roleId.
     * @param roleId New value of property roleId.
     */
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
    
       
    /** Getter for property descend.
     * @return Value of property descend.
     */
    public boolean isDescend() {
        return descend;
    }
    
    /** Setter for property descend.
     * @param descend New value of property descend.
     */
    public void setDescend(boolean descend) {
        this.descend = descend;
    }
    
    /** Getter for property status.
     * @return Value of property status.
     */
    public char getStatus() {
        return status;
    }
    
    /** Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(char status) {
        this.status = status;
    }
    
    /** Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /** Setter for property updateUser.
     * @param roleName New value of property updateUser.
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
     * @param roleName New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
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
    
    /** Getter for property unitName.
     * @return Value of property unitName.
     */
    public java.lang.String getUnitName() {
        return unitName;
    }
    
    /** Setter for property unitName.
     * @param unitName New value of property unitName.
     */
    public void setUnitName(java.lang.String unitName) {
        this.unitName = unitName;
    }
    
}
