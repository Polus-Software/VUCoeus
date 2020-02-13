/*
 * @(#)CommitteeMemberStatusChangeBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.bean;
/**
 * The class used to hold the information of <code>MembershipStatus Detail</code>
 * The data will get attached with the <code>MembershipStatus Detail</code> form.
 *
 * @author  phani
 * @version 1.0
 * Created on September 26, 2002, 4:16 PM
 */

public class CommitteeMemberStatusChangeBean implements java.io.Serializable {
    //holds membership Id
    private String membershipId;
    //holds Seq Number
    private int sequenceNumber;
    //holds membership status code
    private int membershipStatusCode;
    //holds membership status Description
    private String statusDescription;
    //holds Start date
    private java.sql.Date startDate;
    //holds End date
    private java.sql.Date endDate;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds account type
    private String acType;
    
    /** 
     * Creates a new instance of CommitteeMemberStatusChangeBean 
     */
    public CommitteeMemberStatusChangeBean() {
    }
    
    /**
     * Method used to get the Membership Id
     * @return membershipId String
     */
    public String getMembershipId() {
        return membershipId;
    }
    
    /**
     * Method used to set the Membership Id
     * @param membershipId String
     */
    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }
    
    /**
     * Method used to get the SeqNumber
     * @return sequenceNumber int
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    
    /**
     * Method used to set the SeqNumber
     * @param sequenceNumber int
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
    /**
     * Method used to get the Membership StatusCode
     * @return membershipStatusCode int
     */
    public int getMembershipStatusCode() {
        return membershipStatusCode;
    }
    
    /**
     * Method used to set the Membership StatusCode
     * @param membershipStatusCode int
     */
    public void setMembershipStatusCode(int membershipStatusCode) {
        this.membershipStatusCode = membershipStatusCode;
    }
    
    /**
     * Method used to get the Status Description
     * @return statusDescription String
     */
    public String  getStatusDescription() {
        return statusDescription;
    }
    
    /**
     * Method used to set the Status Description
     * @param statusDescription String String
     */
    public void setStatusDescription(String statusDescription ) {
        this.statusDescription = statusDescription;
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
