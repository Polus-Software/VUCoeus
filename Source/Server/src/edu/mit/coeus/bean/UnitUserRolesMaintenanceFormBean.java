/*
 * UnitUserRolesMaintenanceFormBean.java
 *
 * Created on May 12, 2011, 3:54 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.bean;

import java.io.Serializable;
import java.util.Vector;

/**
 *
 * @author divyasusendran
 */
public class UnitUserRolesMaintenanceFormBean implements Serializable {
    
    private int roleId;
    private String roleName;
    private char roleType;
    private boolean descend;
    private String unitNumber;
    private String unitName; 
    private Vector vecRoles;    
    private String userId;    
    private String acType; 
    /** Creates a new instance of UnitUserRolesMaintenanceFormBean */
    public UnitUserRolesMaintenanceFormBean() {
    }
    
    /** 
     *Getter for property roleId.
     *@return Value of property roleId.
     */
    public int getRoleId() {
        return roleId;
    }
    
    /** 
     *Setter for property roleId.
     *@param roleId New value of property roleId.
     */
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
    
    /** 
     *Getter for property roleName.
     *@return Value of property roleName.
     */
    public String getRoleName() {
        return roleName;
    }
    
    /** 
     *Setter for property roleName.
     *@param roleName New value of property roleName.
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /** 
     *Getter for property roleType.
     *@return Value of property roleType.
     */
    public char getRoleType() {
        return roleType;
    }
    
    /** 
     *Setter for property roleType.
     *@param roleType New value of property roleType.
     */
    public void setRoleType(char roleType) {
        this.roleType = roleType;
    }

    /** 
     *Getter for property descend.
     *@return Value of property descend.
     */
    public boolean isDescend() {
        return descend;
    }
    
    /** 
     *Setter for property descend.
     *@param descend New value of property descend.
     */
    public void setDescend(boolean descend) {
        this.descend = descend;
    }
    
    /** 
     *Getter for property unitNumber.
     *@return Value of property unitNumber.
     */
    public String getUnitNumber() {
        return unitNumber;
    }
    
    /** 
     *Setter for property unitNumber.
     *@param unitNumber New value of property unitNumber.
     */
    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }
    
    /** 
     *Getter for property unitName.
     *@return Value of property unitName.
     */
    public String getUnitName() {
        return unitName;
    }
    
    /** 
     *Setter for property unitName.
     *@param unitName New value of property unitName.
     */
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
    
    /** 
     *Getter for property vecRoles.
     *@return Value of property vecRoles.
     */
    public Vector getVecRoles() {
        return vecRoles;
    }
    
    /** 
     *Setter for property vecRoles.
     *@param vecRoles New value of property vecRoles.
     */
    public void setVecRoles(Vector vecRoles) {
        this.vecRoles = vecRoles;
    }
    
    /** 
     *Getter for property userId.
     *@return Value of property userId.
     */
    public String getUserId() {
        return userId;
    }
    
    /** 
     *Setter for property userId.
     *@param userId New value of property userId.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    /** 
     *Getter for property acType.
     *@return Value of property acType.
     */
    public String getAcType() {
        return acType;
    }
    
    /** 
     *Setter for property acType.
     *@param acType New value of property acType.
     */
    public void setAcType(String acType) {
        this.acType = acType;
    }
    
}
