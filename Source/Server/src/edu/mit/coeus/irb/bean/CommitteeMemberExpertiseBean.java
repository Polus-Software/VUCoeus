/*
 * @(#)CommitteeMemberExpertiseBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.bean;

/**
 * The class used to hold the information of <code>MembershipExpertise Detail</code>
 * The data will get attached with the <code>MembershipExpertise Detail</code> form.
 *
 * @author  phani
 * @version 1.0
 * Created on September 26, 2002, 4:13 PM
 */

public class CommitteeMemberExpertiseBean implements java.io.Serializable {
    //holds membership Id
    private String membershipId;
    //holds sequence number
    private int sequenceNumber;
    //holds research Area Code
    private String researchAreaCode;
    //holds researchArea Description
    private String researchAreaDesc;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds account type
    private String acType;
    
    /** 
     * Creates a new instance of CommitteeMemberExpertiseBean 
     */
    public CommitteeMemberExpertiseBean() {
        
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
     * Method used to get the Sequence Number
     * @return sequenceNumber int
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    
    /**
     * Method used to set the Sequence Number
     * @param sequenceNumber int
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
    /**
     * Method used to get the Research Area Code
     * @return researchAreaCode String
     */
    public String getResearchAreaCode() {
        return researchAreaCode;
    }
    
    /**
     * Method used to set the Research Area Code
     * @param researchAreaCode String
     */
    public void setResearchAreaCode(String researchAreaCode) {
        this.researchAreaCode = researchAreaCode;
    }
    
    /**
     * Method used to get the Research Area Description
     * @return researchAreaDesc String
     */
    public String getResearchAreaDesc() {
        return researchAreaDesc;
    }
    
    /**
     * Method used to set the Research Area Description
     * @param researchAreaDesc String
     */
    public void setResearchAreaDesc(String researchAreaDesc) {
        this.researchAreaDesc = researchAreaDesc;
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
