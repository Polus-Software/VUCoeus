/*
 * @(#)CommitteeMemberRolesBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.iacuc.bean.*;

/**
 * The class used to hold the information of <code>MembershipRoles Detail</code>
 * The data will get attached with the <code>MembershipRoles Detail</code> form.
 * 
 * @author  phani
 * @version 1.0
 * Created on September 24, 2002, 7:48 PM
 */

public class CommitteeMemberRolesBean implements java.io.Serializable {
    //holds committee membership Id
    private String commMembershipId;
    //holds member SeqNumber
    private int memberSeqNumber;
    //holds membership Role Code
    private int membershipRoleCode;
    //holds membership Role Description
    private String membershipRoleDesc;
    //holds start Date
    private java.sql.Date startDate;
    //holds End Date
    private java.sql.Date endDate;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds account type
    private String acType;
    
    /** 
     * Creates a new instance of CommitteeMemberRolesBean 
     */
    public CommitteeMemberRolesBean() {
    }
    
    /**
     * Method used to get the committeeMembership Id
     * @return commMembershipId String
     */
    public String getCommMembershipId() {
        return commMembershipId;
    }
    
    /**
     * Method used to set the committeeMembership Id
     * @param commMembershipId String 
     */
    public void setCommMembershipId(String commMembershipId) {
        this.commMembershipId = commMembershipId;
    }
    
    /**
     * Method used to get the Membership SeqNumber
     * @return memberSeqNumber int
     */
    public int getMemberSeqNumber() {
        return memberSeqNumber;
    }
    
    /**
     * Method used to set the Membership SeqNumber
     * @param memberSeqNumber int
     */
    public void setMemberSeqNumber(int memberSeqNumber) {
        this.memberSeqNumber = memberSeqNumber;
    }
    
    /**
     * Method used to get the Membership Role Code
     * @return membershipRoleCode int
     */
    public int getMembershipRoleCode() {
        return membershipRoleCode;
    }
    
    /**
     * Method used to set the Membership Role Code
     * @param membershipRoleCode int
     */
    public void setMembershipRoleCode(int membershipRoleCode) {
        this.membershipRoleCode = membershipRoleCode;
    }
    
    /**
     * Method used to get the Membership Role Description
     * @return membershipRoleDesc String
     */
    public String getMembershipRoleDesc() {
        return membershipRoleDesc;
    }
    
    /**
     * Method used to set the Membership Role Description
     * @param membershipRoleDesc String
     */
    public void setMembershipRoleDesc(String membershipRoleDesc) {
        this.membershipRoleDesc = membershipRoleDesc;
    }
    
    /**
     * Method used to get the Start Date
     * @return startDate Date
     */
    public java.sql.Date getStartDate() {
        return startDate;
    }
    
    /**
     * Method used to set the Start Date
     * @param startDate Date
     */
    public void setStartDate(java.sql.Date startDate) {
        this.startDate = startDate;
    }
    
    /**
     * Method used to get the End Date
     * @return endDate Date
     */
    public java.sql.Date getEndDate() {
        return endDate;
    }
    
    /**
     * Method used to set the End Date
     * @param endDate Date
     */
    public void setEndDate(java.sql.Date endDate) {
        this.endDate = endDate;
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
    
    /**
     * Method used to get the Action Type
     * @return acType String
     */
    public String getAcType() {
        return acType;
    }
    
    /**
     * Method used to set the Action Type
     * @param acType String
     */
    public void setAcType(String acType) {
        this.acType = acType;
    }
    
}
