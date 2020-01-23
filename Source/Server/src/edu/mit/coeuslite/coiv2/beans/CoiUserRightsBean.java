/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.beans;

import java.io.Serializable;

/**
 *
 * @author Sony
 */
public class CoiUserRightsBean implements Serializable{

    public CoiUserRightsBean() {
    }

    private String userId;
    private String roleID;
    private String rightId;

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the roleID
     */
    public String getRoleID() {
        return roleID;
    }

    /**
     * @param roleID the roleID to set
     */
    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    /**
     * @return the rightId
     */
    public String getRightId() {
        return rightId;
    }

    /**
     * @param rightId the rightId to set
     */
    public void setRightId(String rightId) {
        this.rightId = rightId;
    }

    

}
