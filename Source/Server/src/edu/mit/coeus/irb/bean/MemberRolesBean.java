/*
 * @(#)MemberRolesBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.bean;

/**
 * The class used to hold the information of <code>Member Roles Bean</code>
 * The data will get attached with the <code>MemberMaintenanceForm</code>.
 *
 * 
 * @author  phani
 * @version 1.0
 * Created on September 24, 2002, 7:51 PM
 */

public class MemberRolesBean implements java.io.Serializable {
    //holds membership role code
    private int membershipRoleCode;
    //holds membership role descrition
    private String membershipRoleDescription;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    
    /** Creates a new instance of MemberRolesBean */
    public MemberRolesBean() {
    }

    /**
     * Method used to get the membership role code
     * @return membershipRoleCode integer
     */
    public int getMembershipRoleCode() {
        return membershipRoleCode;
    }

    /**
     * Method used to set the membership role code
     * @param membershipRoleCode - integer
     */
    public void setMembershipRoleCode(int membershipRoleCode) {
        this.membershipRoleCode = membershipRoleCode;
    }

    /**
     * Method used to get the membership role description
     * @return membershipRoleDescription String
     */
    public String getMembershipRoleDescription() {
        return membershipRoleDescription;
    }

    /**
     * Method used to set the membership role description
     * @param membershipRoleDescription String
     */
    public void setMembershipRoleDescription(String membershipRoleDescription) {
        this.membershipRoleDescription = membershipRoleDescription;
    }

    /**
     * Method used to get the update user id
     * @return updateUser String
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * Method used to set the update user id
     * @param updateUser String
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * Method used to get the update timestamp
     * @return updateTimestamp Timestamp
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    /**
     * Method used to set the update timestamp
     * @param updateTimestamp Timestamp
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

}
