/* 
 * @(#)RoleBean.java 1.0 04/14/03 6:10 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.bean;

import java.io.Serializable;

/**
 * The class used to hold the information of <code>Role id and user id</code>
 *
 * @author  ravikanth
 * @version 1.0
 * Created on April 14, 2003, 6:10 PM 
 */
public class RoleBean implements Serializable {
    //holds the role id
    private int roleId;
    //holds the user id
    private String userId;
    //holds the ac type
    private String acType;
    
    /** Creates a new instance of RoleBean */
    public RoleBean() {
    }
    
    public RoleBean ( RoleBean roleBean ) {
        this.roleId = roleBean.getRoleId();
        this.userId = roleBean.getUserId();
        this.acType = roleBean.getAcType();
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
    
}
