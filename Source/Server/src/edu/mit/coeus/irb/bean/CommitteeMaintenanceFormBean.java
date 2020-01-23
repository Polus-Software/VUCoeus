/*
 * @(#)CommitteeMaintenanceFormBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.bean;

import java.sql.Timestamp;
import java.util.Vector;

/**
 * The class used to hold the information of <code>Committee Maintenance</code>
 * The data will get attached with the <code>Committee Details</code> form.
 *
 * 
 * @author  phani Created on September 23, 2002, 2:36 PM
 * @version 1.0
 */
public class CommitteeMaintenanceFormBean implements java.io.Serializable {
    
    //holds committee Id
    private String committeeId;
    //holds committee name
    private String committeeName;
    //holds unit number
    private String unitNumber;
    //holds unit name
    private String unitName;
    //holds committee description
    private String description;
    //holds schedule description
    private String scheduleDescription;
    //holds committee type code
    private int committeeTypeCode;
    //holds committee type description
    private String committeeTypeDesc;
    //holds minimum Members
    private int minMembers;
    //holds committee members
    private Vector committeeMembers;
    //holds committee ResearchAreas
    private Vector committeeResearchAreas;
    //holds committee Schedules
    private Vector committeeSchedules;
    //holds max Protocols
    private int maxProtocols;
    //holds Advance submission days
    private int advSubmissionDaysReq;
    //holds review type code
    private int reviewTypeCode;
    //holds review type description
    private String reviewTypeDesc;
    //holds application review type code
    private int applReviewTypeCode;
    //holds name of who is going to create
    private String createUser;
    //holds create timestamp
    private Timestamp createTimeStamp;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private Timestamp updateTimestamp;
    //holds account type
    private String acType;
    private String refId;
     
    /** 
     * Creates a new instance of CommitteeMaintenanceFormBean 
     */
    public CommitteeMaintenanceFormBean() {
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
     * Method used to get the committee name
     * @return committee name String
     */
    public String getCommitteeName() {
        return committeeName;
    }

    /**
     * Method used to set the committee name
     * @param committeeName String
     */
    public void setCommitteeName(String committeeName) {
        this.committeeName = committeeName;
    }

    /**
     * Method used to get the unit number
     * @return unit number String
     */
    public String getUnitNumber() {
        return unitNumber;
    }

    /**
     * Method used to set the unit number
     * @param unitNumber String
     */
    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    /**
     * Method used to get the unit name for unit number
     * @return unit name String
     */
    public String getUnitName() {
        return unitName;
    }

    /**
     * Method used to set the unit name for unit number
     * @param unitName String
     */
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    /**
     * Method used to get the committee description
     * @return description String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Method used to set the committee decription
     * @param description String
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Method used to get the schedule decription
     * @return decription String
     */
    public String getScheduleDescription() {
        return scheduleDescription;
    }

    /**
     * Method used to set the schedule decription
     * @param scheduleDescription String
     */
    public void setScheduleDescription(String scheduleDescription) {
        this.scheduleDescription = scheduleDescription;
    }

    /**
     * Method used to get the committee type code
     * @return committeeTypeCode int
     */
    public int getCommitteeTypeCode() {
        return committeeTypeCode;
    }

    /**
     * Method used to set the committee type code
     * @param committeeTypeCode int
     */
    public void setCommitteTypeCode(int committeeTypeCode) {
        this.committeeTypeCode = committeeTypeCode;
    }

    /**
     * Method used to get the committee type description
     * @return committeeTypeDesc String
     */
    public String getCommitteeTypeDesc() {
        return committeeTypeDesc;
    }

    /**
     * Method used to set the committee type description
     * @param committeeTypeDesc String
     */
    public void setCommitteTypeDesc(String committeeTypeDesc) {
        this.committeeTypeDesc = committeeTypeDesc;
    }

    /**
     * Method used to get the Vector of CommitteeMembershipDetailsBean
     * @return committeeMembers Vector
     */
    public Vector getCommitteeMembers() {
        return committeeMembers;
    }

    /**
     * Method used to set the Vector of CommitteeMembershipDetailsBean
     * @param committeeMembers Vector
     */
    public void setCommitteeMembers(Vector committeeMembers) {
        this.committeeMembers = committeeMembers;
    }

    /**
     * Method used to get the Vector of CommitteeResearchAreasBean
     * @return committeeResearchAreas Vector
     */
    public Vector getCommitteeResearchAreas(){
        return committeeResearchAreas;
    }
    
    /**
     * Method used to set the Vector of CommitteeResearchAreasBean
     * @param committeeResearchAreas Vector
     */
    public void setCommitteeResearchAreas(Vector committeeResearchAreas){
        this.committeeResearchAreas = committeeResearchAreas;
    }
    
    /**
     * Method used to get the Vector of ScheduleDetailsBean
     * @return ScheduleDetailsBean Vector
     */
    public Vector getCommitteeSchedules() {
        return committeeSchedules;
    }

    /**
     * Method used to set the Vector of ScheduleDetailsBean
     * @param committeeSchedules Vector
     */
    public void setCommitteeSchedules(Vector committeeSchedules) {
        this.committeeSchedules = committeeSchedules;
    }

    /**
     * Method used to get the minimum members
     * @return minMembers int
     */
    public int getMinMembers() {
        return minMembers;
    }

    /**
     * Method used to set the minimum members
     * @param minMembers int
     */
    public void setMinMembers(int minMembers) {
        this.minMembers = minMembers;
    }

    /**
     * Method used to get the Max Protocols
     * @return maxProtocols int
     */
    public int getMaxProtocols() {
        return maxProtocols;
    }

    /**
     * Method used to set the Max Protocols
     * @param maxProtocols int
     */
    public void setMaxProtocols(int maxProtocols) {
        this.maxProtocols = maxProtocols;
    }

    /**
     * Method used to get the Advance Submission days
     * @return advSubmissionDaysReq int
     */
    public int getAdvSubmissionDaysReq() {
        return advSubmissionDaysReq;
    }

    /**
     * Method used to set the Advance Submission days
     * @param advSubmissionDaysReq int
     */
    public void setAdvSubmissionDaysReq(int advSubmissionDaysReq) {
        this.advSubmissionDaysReq = advSubmissionDaysReq;
    }

    /**
     * Method used to get the Review Type Code
     * @return reviewTypeCode int
     */
    public int getReviewTypeCode() {
        return reviewTypeCode;
    }

    /**
     * Method used to set the Review Type Code
     * @param reviewTypeCode int
     */
    public void setReviewTypeCode(int reviewTypeCode) {
        this.reviewTypeCode = reviewTypeCode;
    }
    
    /**
     * Method used to get the Review Type description
     * @return reviewTypeDesc String
     */
    public String getReviewTypeDesc(){
        return reviewTypeDesc;
    }

    /**
     * Method used to set the Review Type description
     * @param reviewTypeDesc String
     */
    public void setReviewTypeDesc(String reviewTypeDesc){
        this.reviewTypeDesc = reviewTypeDesc;
    }
    /**
     * Method used to get the Application Review Type code
     * @return applReviewTypeCode int
     */
    public int getApplReviewTypeCode() {
        return applReviewTypeCode;
    }

    /**
     * Method used to set the Application Review Type code
     * @param applReviewTypeCode int
     */
    public void setApplReviewTypeCode(int applReviewTypeCode) {
        this.applReviewTypeCode = applReviewTypeCode;
    }

    /**
     * Method used to get the create user id
     * @return createUser String
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * Method used to set the create user id
     * @param createUser String
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    /**
     * Method used to get the create timestamp
     * @return createTimeStamp Timestamp
     */
    public Timestamp getCreateTimeStamp() {
        return createTimeStamp;
    }

    /**
     * Method used to set the create timestamp
     * @param createTimeStamp Timestamp
     */
    public void setCreateTimeStamp(Timestamp createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
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
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    /**
     * Method used to set the update timestamp
     * @param updateTimestamp Timestamp
     */
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
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
    
    /**
     * Method used to set the reference Id
     * @param refId String
     */
    public void setRefId(String refId){
        this.refId = refId;
    }
    
    /**
     * Method used to get the reference Id
     * @return refId String
     */
    public String getRefId(){
        return this.refId;
    }


}
