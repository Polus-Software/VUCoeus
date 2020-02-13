/* 
 * @(#)UserMaintRoleBean.java 1.0 07/15/03 11:10 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.user.bean;

import java.io.Serializable;
import edu.mit.coeus.bean.RoleBean;

/**
 * The class used to hold the information of <code>Role id and user id</code>
 *
 * @author  Prasanna Kumar
 * @version 1.0
 * Created on July 15, 2003, 11:10 AM 
 */
public class UserMaintRoleBean extends RoleBean implements Serializable {
    //holds the unit Number
    private String unitNumber;
    //holds the descend Flag
    private char descendFlag;
    //holds the status
    private char status;    
    //holds roleName
    private String roleName;
    //holds roleType
    private String roleType;
    //holds update user
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    
    /** Creates a new instance of RoleBean */
    public UserMaintRoleBean() {
    }
    
    public UserMaintRoleBean ( RoleBean roleBean ) {
        setRoleId(roleBean.getRoleId());
        setUserId(roleBean.getUserId());
        setAcType(roleBean.getAcType());
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
    
    /** Getter for property descendFlag.
     * @return Value of property descendFlag.
     */
    public char getDescendFlag() {
        return descendFlag;
    }
    
    /** Setter for property descendFlag.
     * @param descendFlag New value of property descendFlag.
     */
    public void setDescendFlag(char descendFlag) {
        this.descendFlag = descendFlag;
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
    
    /** Getter for property roleType.
     * @return Value of property roleType.
     */
    public java.lang.String getRoleType() {
        return roleType;
    }
    
    /** Setter for property roleType.
     * @param roleType New value of property roleType.
     */
    public void setRoleType(java.lang.String roleType) {
        this.roleType = roleType;
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
   
}
