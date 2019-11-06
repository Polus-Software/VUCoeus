/*
 * @(#)CommitteeMembershipDetailsBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.bean;


import java.util.Vector;
import edu.mit.coeus.irb.bean.CommitteeMemberStatusChangeBean;

/**
 * The class used to hold the information of 
 * <code>CommitteeMembership Details</code>
 * The data will get attached with the <code>CommitteeMembership Details</code> 
 * form.
 *
 * @author  phani
 * @version 1.0
 * Created on September 23, 2002, 2:41 PM
 */
public class CommitteeMembershipDetailsBean implements java.io.Serializable {
    //holds committee Id
    private String committeeId;
    //holds membership Id
    private String membershipId;
    //holds person Id
    private String personId;
    //holds person name 
    private String personName;
    //holds sequence number
    private int sequenceNumber;
    //holds employee flag to find out person or from rolodex
    private char nonEmployeeFlag;
    //holds flag to check paid member or not
    private char paidMemberFlag = 'N';
    //holds term start date
    private java.sql.Date termStartDate;
    //holds term end date
    private java.sql.Date termEndDate;
    //holds membership type code
    private int membershipTypeCode;
    //holds membership type description
    private String membershipTypeDesc;
    //holds comments
    private String comments;
    //holds member roles
    private Vector memberRoles;
    //holds member expertise
    private Vector memberExpertise;
    //holds member status history
    private Vector memberStatusHistory;
    //holds CommitteeMemberStatusChangeBean
    private CommitteeMemberStatusChangeBean statusInfo; 
    //holds status description
    private String statusDescription;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds account type
    private String acType;
    
    private boolean memberRolesModified;
    
    private boolean memberStatusModified;
    
    private boolean memberExpertiseModified;
    
    /** 
     * Creates a new instance of CommitteeMembershipDetailsBean 
     */
    public CommitteeMembershipDetailsBean() {
    }

    /**
     * Method used to get the committee Id
     * @return committee Id String
     */
    public String getCommitteeId() {
        return committeeId;
    }

    /**
     * Method used to set the committee Id
     * @param committeeId String
     */
    public void setCommitteeId(String committeeId) {
        this.committeeId = committeeId;
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
     * Method used to get the person Id
     * @return personId String
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * Method used to set the person Id
     * @param personId String
     */
    public void setPersonId(String personId) {
        this.personId = personId;
    }

    /**
     * Method used to get the person name for person id
     * @return personName String
     */
    public String getPersonName() {
        return personName;
    }

    /**
     * Method used to set the person name for person id
     * @param personName String
     */
    public void setPersonName(String personName) {
        this.personName = personName;
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
     * Method used to get the Non Employee Flag to find person or rolodex
     * @return nonEmployeeFlag char
     */
    public char getNonEmployeeFlag() {
        return nonEmployeeFlag;
    }

    /**
     * Method used to set the Non Employee Flag to find person or rolodex
     * @param nonEmployeeFlag char
     */
    public void setNonEmployeeFlag(char nonEmployeeFlag) {
        this.nonEmployeeFlag = nonEmployeeFlag;
    }

    /**
     * Method used to get the Paid Member Flag to find member paid or not
     * @return paidMemberFlag char
     */
     public char getPaidMemberFlag() {
        return paidMemberFlag;
    }

     /**
     * Method used to set the Paid Member Flag to find member paid or not
     * @param paidMemberFlag char
     */
    public void setPaidMemberFlag(char paidMemberFlag) {
        this.paidMemberFlag = paidMemberFlag;
    }
    
    /**
     * Method used to get the Term Start Date
     * @return termStartDate Date
     */
    public java.sql.Date getTermStartDate() {
        return termStartDate;
    }

    /**
     * Method used to set the Term Start Date
     * @param termStartDate Date
     */
    public void setTermStartDate(java.sql.Date termStartDate) {
        this.termStartDate = termStartDate;
    }
    /**
     * Method used to get the Term End Date
     * @return termEndDate Date
     */
    public java.sql.Date getTermEndDate() {
        return termEndDate;
    }

    /**
     * Method used to set the Term End Date
     * @param termEndDate Date
     */
    public void setTermEndDate(java.sql.Date termEndDate) {
        this.termEndDate = termEndDate;
    }

    /**
     * Method used to get the Membership Type Code
     * @return membershipTypeCode int
     */
    public int getMembershipTypeCode() {
        return membershipTypeCode;
    }

    /**
     * Method used to set the Membership Type Code
     * @param membershipTypeCode int
     */
    public void setMembershipTypeCode(int membershipTypeCode) {
        this.membershipTypeCode = membershipTypeCode;
    }

    /**
     * Method used to get the Membership Type Description
     * @return MembershipTypeDesc String
     */
    public String getMembershipTypeDesc() {
        return membershipTypeDesc;
    }

    /**
     * Method used to set the Membership Type Description
     * @param membershipTypeDesc String
     */
    public void setMembershipTypeDesc(String membershipTypeDesc) {
        this.membershipTypeDesc = membershipTypeDesc;
    }
    
    /**
     * Method used to get the comments
     * @return comments String
     */
    public String getComments() {
        return comments;
    }

    /**
     * Method used to set the comments
     * @param comments String
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Method used to get the Vector of CommitteeMemberRolesBean
     * @return memberRoles Vector
     */
    public Vector getMemberRoles() {
        return memberRoles;
    }
    
    /**
     * Method used to set the Vector of CommitteeMemberRolesBean
     * @param memberRoles Vector
     */
    public void setMemberRoles(Vector memberRoles){
        this.memberRoles = memberRoles;
    }
    
    /**
     * Method used to set the boolean roles modified
     * @param modified boolean
     */
    public void setMemberRolesModified(boolean modified){
        this.memberRolesModified = modified;
    }
    
    /**
     * Method used to get the boolean roles modified
     * @return memberRolesModified boolean
     */
    public boolean getMemberRolesModified(){
        return memberRolesModified;
    }

    /**
     * Method used to set the boolean status modified
     * @param modified boolean
     */
    public void setMemberStatusModified(boolean modified){
        this.memberStatusModified = modified;
    }

    /**
     * Method used to get the boolean status modified
     * @return memberStatusModified boolean
     */
    public boolean getMemberStatusModified(){
        return memberStatusModified;
    }

    /**
     * Method used to set the boolean expertise modified
     * @param modified boolean
     */
    public void setMemberExpertiseModified(boolean modified){
        this.memberExpertiseModified = modified;
    }
    
     /**
     * Method used to get the boolean expertise modified
     * @return memberExpertiseModified boolean
     */
    public boolean getMemberExpertiseModified(){
        return memberExpertiseModified;
    }
    
    /**
     * Method used to get Vector of CommitteeMemberExpertiseBean
     * @return memberExpertise Vector
     */
    public Vector getMemberExpertise(){
        return memberExpertise;
    }
    
    /**
     * Method used to set the Vector of CommitteeMemberExpertiseBean
     * @param memberExpertise Vector
     */
    public void setMemberExpertise(Vector memberExpertise){
        this.memberExpertise = memberExpertise;
    }
    
    /**
     * Method used to get the Vector of CommitteeMemberStatusChangeBean
     * @return memberStatusHistory Vector
     */
    public Vector getMemberStatusHistory(){
        return memberStatusHistory;
    }
    
    /**
     * Method used to set the Vector of CommitteeMemberStatusChangeBean
     * @param memberStatusHistory Vector
     */
    public void setMemberStatusHistory(Vector memberStatusHistory){
        this.memberStatusHistory = memberStatusHistory;
    }
    
    /**
     * Method used to get the Vector of CommitteeMemberStatusChangeBean
     * @return statusInfo Vector
     */
    public CommitteeMemberStatusChangeBean getStatusInfo(){
        return statusInfo;
    }
    
    /**
     * Method used to set the Vector of CommitteeMemberStatusChangeBean
     * @param statusInfo Vector
     */
    public void setStatusInfo(CommitteeMemberStatusChangeBean statusInfo){
        this.statusInfo = statusInfo;
    }
    
    /**
     * Method used to get the Status Description
     * @return statusDescription String
     */
    public String getStatusDescription(){
        return statusDescription;
    }
    
    /**
     * Method used to set the Status Description
     * @param statusDescription String
     */
    public void setStatusDescription(String statusDescription){
        this.statusDescription = statusDescription;
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
