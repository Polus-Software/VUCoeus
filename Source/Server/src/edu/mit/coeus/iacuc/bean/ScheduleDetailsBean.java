/*
 * @(#)ScheduleDetailsBean.java September 26, 2002, 12:02 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 16-JULY-2010
 * by George J Nirappeal
 */
package edu.mit.coeus.iacuc.bean;

import java.util.Vector;

/**
 * The class used to hold the information of <code>Schedule Details</code>
 *
 * @author  phani
 * @version 1.0
 * Created on September 26, 2002, 12:02 PM
 */

public class ScheduleDetailsBean implements java.io.Serializable {
    //holds schedule Id
    private String scheduleId;
    //holds committee Id
    private String committeeId;
    //holds committee name
    private String committeeName;
    //holds Schedule Date 
    private java.sql.Date scheduleDate;
    //holds Scheduled Place
    private String scheduledPlace;
    //holds Scheduled Time
    private java.sql.Time scheduledTime;
    //holds protocol Submission DeadLine
    private java.sql.Date protocolSubDeadLine;
    //holds schedule StatusCode
    private int scheduleStatusCode;
    //holds scheduleStatus Description
    private String scheduleStatusDesc;
    //holds meeting Date
    private java.sql.Date meetingDate;
    //holds meeting Start Time
    private java.sql.Time meetingStartTime;
    //holds meeting End Time
    private java.sql.Time meetingEndTime;
    //holds Last Agenda ProdRevDate
    private java.sql.Date lastAgendaProdRevDate;
    //holds Max Protocols
    private int maxProtocols;
    //holds comments
    private String comments;
    //holds protocol submission list bean
    private Vector submissionList;
    //holds protocol other actions bean
    private Vector otherActionsList;
    //holds protocol attendentees bean
    private Vector attendeesList;
    //holds protocol abentees bean
    private Vector absenteesList;
    //holds protocol minute bean
    private Vector minuteList;
    //holds server system date
    private java.sql.Timestamp serverSysDate;
    //holds Update user
    private String updateUser;
    //holds update Timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds Action type
    private String acType;
    private String refId;
    //Pras - Start
    //holds list of non-deferred Protocol Number
    private Vector nonDeferredProtocols; 
    //Pras - End
    
    //prps start - jan 15 2004
    private String homeUnitNumber;
    // 3282: Reviewer view in Lite 
    private boolean viewInLite;
    //prps end - jan 15 2004
    //IACUC Changes - Start
    private int committeeTypeCode;
    //IACUC Changes - End
    // Added for COEUSQA-2685  IACUC - comm member areas of research should point to IACUC areas of research _start
    // Added for IACUC Reviewer implementation - Start
    private Vector vecProtocolsForScheduleMinutes;
    // Added for IACUC Reviewer implementation - End
    // Added for COEUSQA-2685  IACUC - comm member areas of research should point to IACUC areas of research _end
    /** 
     * Creates a new instance of ScheduleDetailsBean 
     */
    public ScheduleDetailsBean() {
    }
    
    /**
     * Method used to get the Schedule Id
     * @return scheduleId String
     */
    public String getScheduleId() {
        return scheduleId;
    }
    
    /**
     * Method used to set the Schedule Id
     * @param scheduleId String
     */
    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }
    
    /**
     * Method used to get the committee Id
     * @return committee Id String
     */
    public String getCommitteeId() {
        return committeeId;
    }
    
    /**
     * Method used to get the committee name
     * @return committeeName String
     */
    public String getCommitteeName() {
        return committeeName;
    }
    
    /**
     * Method used to set the committee Id
     * @param committeeId String
     */
    public void setCommitteeId(String committeeId) {
        this.committeeId = committeeId;
    }
    
    /**
     * Method used to set the committee name
     * @param committeeName String
     */
    public void setCommitteeName(String committeeName) {
        this.committeeName = committeeName;
    }
    
    /**
     * Method used to get the Schedule Date
     * @return scheduleDate Date
     */
    public java.sql.Date getScheduleDate() {
        return scheduleDate;
    }
    
    /**
     * Method used to set the Schedule Date
     * @param scheduleDate Date
     */
    public void setScheduleDate(java.sql.Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }
    
    /**
     * Method used to get the Scheduled Place
     * @return scheduledPlace String
     */
    public String getScheduledPlace() {
        return scheduledPlace;
    }
    
    /**
     * Method used to set the Scheduled Place
     * @param scheduledPlace String
     */
    public void setScheduledPlace(String scheduledPlace) {
        this.scheduledPlace = scheduledPlace;
    }
    
    /**
     * Method used to get the Scheduled Time
     * @return Scheduled Time
     */
    public java.sql.Time getScheduledTime() {
        return scheduledTime;
    }
    
    /**
     * Method used to set the Scheduled Time
     * @param scheduledTime Time
     */
    public void setScheduledTime(java.sql.Time scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
    
    /**
     * Method used to get the Protocol Submission DeadLine
     * @return protocolSubDeadLine Date
     */
    public java.sql.Date getProtocolSubDeadLine() {
        return protocolSubDeadLine;
    }
    
    /**
     * Method used to set the Protocol Submission DeadLine
     * @param protocolSubDeadLine Date
     */
    public void setProtocolSubDeadLine(java.sql.Date protocolSubDeadLine) {
        this.protocolSubDeadLine = protocolSubDeadLine;
    }
    
    /**
     * Method used to get the Schedule Status Code
     * @return scheduleStatusCode int
     */
    public int getScheduleStatusCode() {
        return scheduleStatusCode;
    }
    
    /**
     * Method used to set the Schedule Status Code
     * @param scheduleStatusCode int
     */
    public void setScheduleStatusCode(int scheduleStatusCode) {
        this.scheduleStatusCode = scheduleStatusCode;
    }
    
    /**
     * Method used to get the Schedule Status Description
     * @return scheduleStatusDesc String
     */
    public String getScheduleStatusDesc() {
        return scheduleStatusDesc;
    }
    
    /**
     * Method used to set the Schedule Status Description
     * @param scheduleStatusDesc String
     */
    public void setScheduleStatusDesc(String scheduleStatusDesc) {
        this.scheduleStatusDesc = scheduleStatusDesc;
    }
    
    
    /**
     * Method used to get the Meeting Date
     * @return meetingDate Date
     */
    
    public java.sql.Date getMeetingDate() {
        return meetingDate;
    }
    
    /**
     * Method used to set the Meeting Date
     * @param meetingDate Date
     */
    public void setMeetingDate(java.sql.Date meetingDate) {
        this.meetingDate = meetingDate;
    }
    
    /**
     * Method used to get the Meeting Start Time
     * @return meetingStartTime Time
     */
    public java.sql.Time getMeetingStartTime() {
        return meetingStartTime;
    }
    
    /**
     * Method used to set the Meeting Start Time
     * @param meetingStartTime Time
     */
    public void setMeetingStartTime(java.sql.Time meetingStartTime) {
        this.meetingStartTime = meetingStartTime;
    }
    
    /**
     * Method used to get the Meeting End Time
     * @return meetingEndTime Time
     */
    public java.sql.Time getMeetingEndTime() {
        return meetingEndTime;
    }
    
    /**
     * Method used to set the Meeting End Time
     * @param meetingEndTime Time
     */
    public void setMeetingEndTime(java.sql.Time meetingEndTime) {
        this.meetingEndTime = meetingEndTime;
    }
    
    /**
     * Method used to get the LastAgendaProd Rev Date
     * @return lastAgendaProdRevDate Date
     */
    public java.sql.Date getLastAgendaProdRevDate() {
        return lastAgendaProdRevDate;
    }
    
    /**
     * Method used to set the LastAgendaProd Rev Date
     * @param lastAgendaProdRevDate Date
     */
    public void setLastAgendaProdRevDate(java.sql.Date lastAgendaProdRevDate) {
        this.lastAgendaProdRevDate = lastAgendaProdRevDate;
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
     * Method used to get the Vector of SubmissionInfoBean
     * @return submissionList Vector
     */
    public Vector getSubmissionsList(){
        return submissionList;
    }
    
    /**
     * Method used to set the Vector of SubmissionInfoBean
     * @param submissionList Vector
     */
    public void setSubmissionsList(Vector submissionList){
        this.submissionList = submissionList;
    }

    /**
     * Method used to get the Vector of OtherActionInfoBean
     * @return otherActionsList Vector
     */
    public Vector getOtherActionsList(){
        return otherActionsList;
    }
    
    /**
     * Method used to set the Vector of OtherActionInfoBean
     * @param otherActionsList Vector
     */
    public void setOtherActionsList(Vector otherActionsList){
        this.otherActionsList = otherActionsList;
    }

    /**
     * Method used to get the Vector of AttendanceInfoBean
     * @return attendeesList Vector
     */
    public Vector getAttendeesLists(){
        return attendeesList;
    }
    
    /**
     * Method used to set the Vector of AttendanceInfoBean
     * @param attendeesList Vector
     */
    public void setAttendeesLists(Vector attendeesList){
        this.attendeesList = attendeesList;
    }
    /**
     * Method used to get the Vector of AbsenteesInfoBean
     * @return absenteesList Vector
     */
    public Vector getAbsenteesLists(){
        return absenteesList;
    }
    
    /**
     * Method used to set the Vector of AbsenteesInfoBean
     * @param absenteesList Vector
     */
    public void setAbsenteesLists(Vector absenteesList){
        this.absenteesList = absenteesList;
    }

    /**
     * Method used to get the Vector of MinuteEntryInfoBean
     * @return minuteList Vector
     */
    public Vector getMinuteList(){
        return minuteList;
    }
    
    /**
     * Method used to set the Vector of MinuteEntryInfoBean
     * @param minuteList Vector
     */
    public void setMinuteList(Vector minuteList){
        this.minuteList = minuteList;
    }
    
      /**
     * Method used to get the server system date
     * @return Timestamp server system date
     */
    public java.sql.Timestamp getServerSysDate() {
        return serverSysDate;
    }

    /**
     * Method used to set the update timestamp
     * @param serverSysDate Timestamp
     */
    public void setServerSysDate(java.sql.Timestamp serverSysDate) {
        this.serverSysDate = serverSysDate;
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

    /** Getter for property nonDeferredProtocols.
     * @return Value of property nonDeferredProtocols.
     */
    public java.util.Vector getNonDeferredProtocols() {
        return nonDeferredProtocols;
    }
    
    /** Setter for property nonDeferredProtocols.
     * @param nonDeferredProtocols New value of property nonDeferredProtocols.
     */
    public void setNonDeferredProtocols(java.util.Vector nonDeferredProtocols) {
        this.nonDeferredProtocols = nonDeferredProtocols;
    }
    
    /** Getter for property homeUnitNumber.
     * @return Value of property homeUnitNumber.
     *
     */
    public java.lang.String getHomeUnitNumber() {
        return homeUnitNumber;
    }
    
    /** Setter for property homeUnitNumber.
     * @param homeUnitNumber New value of property homeUnitNumber.
     *
     */
    public void setHomeUnitNumber(java.lang.String homeUnitNumber) {
        this.homeUnitNumber = homeUnitNumber;
    }
    // 3282: Reviewer View in Lite - Start
    public boolean isViewInLite() {
        return viewInLite;
    }
    
    public void setViewInLite(boolean viewInLite) {
        this.viewInLite = viewInLite;
    }
    // 3282: Reviewer View in Lite - End

    
    /** Getter for property committeeTypeCode.
     * @return Value of property committeeTypeCode.
     *
     */
    public int getCommitteeTypeCode() {
        return committeeTypeCode;
    }
    /** Setter for property committeeTypeCode.
     * @param committeeTypeCode.
     */
    public void setCommitteeTypeCode(int committeeTypeCode) {
        this.committeeTypeCode = committeeTypeCode;
    }
    // Added for COEUSQA-2685  IACUC - comm member areas of research should point to IACUC areas of research _start
    /**
     * Method to get the protocol for the schdule minutes
     * @return vecProtocolsForScheduleMinutes - Vector
     */
    public Vector getProtocolsForScheduleMinutes() {
        return vecProtocolsForScheduleMinutes;
    }

    /**
     * Method to set the protocols for the schdule minutes
     * @param vecProtocolsForScheduleMinutes 
     */
    public void setProtocolsForScheduleMinutes(Vector vecProtocolsForScheduleMinutes) {
        this.vecProtocolsForScheduleMinutes = vecProtocolsForScheduleMinutes;
    }
    // Added for COEUSQA-2685  IACUC - comm member areas of research should point to IACUC areas of research _end
}
