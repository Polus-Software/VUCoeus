/*
 * @(#)ProtocolSubmissionVoteFormBean.java  March 20, 2003, 5:24 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.irb.bean;

import java.beans.*;
import java.sql.Timestamp;
import java.sql.Date;
import java.io.Serializable;
import java.util.Vector;

/**
 * The class used to hold the information of <code>ProtocolSubmission Vote Details</code>
 *
 * @author  Mukundan.C
 * @version 1.0
 * Created on March 20, 2003, 5:24 PM
 */
public class ProtocolSubmissionVoteFormBean implements java.io.Serializable {
    
     // holds the submission type code
    private static final String PROP_SCHEDULE_ID = "scheduleId";
    // holds the yes vote count
    private static final String PROP_YES_VOTE = "yesVoteCount";
    // holds the no vote count
    private static final String PROP_NO_VOTE = "noVoteCount";
    
    // holds the protocol number
    private String protocolNumber;
    // holds the sequence number
    private int sequenceNumber;
    // holds the schedule id
    private String scheduleId;
    // holds the yes vote count
    private int yesVoteCount;
    // holds the no vote count
    private int noVoteCount;
    //prps start jul 2nd 2003
    // hold abstainer Count 
    private int abstainerCount ;
    private static final String PROP_ABSTAINER_COUNT = "abstainerCount" ;
    private String votingComments ;
    private static final String PROP_VOTING_COMMENTS = "votingComments" ;
    private int submissionNumber ;
    private static final String PROP_SUBMISSION_NUMBER = "submissionNumber" ;
    //prps end
    //holds protocol vote abstainees bean
    private Vector protocolVoteAbstainees;
     //holds update timestamp
    private Timestamp updateTimestamp;
    //holds update user id
    private String updateUser;
    //holds Action type
    private String acType;
    
    private PropertyChangeSupport propertySupport;
    
    /** Creates a new instance of Class */
    public ProtocolSubmissionVoteFormBean() {
        propertySupport = new PropertyChangeSupport( this );
    }

    /**
     * Method used to get the protocol number
     * @return protocolNumber String
     */
    public String getProtocolNumber() {
        return protocolNumber;
    }

    /**
     * Method used to set the protocol number
     * @param protocolNumber String
     */
    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }
    
    /**
     * Method used to get the sequnece number
     * @return sequenceNumber int
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Method used to set the sequnece number
     * @param sequenceNumber int
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * Method used to get the schedule id
     * @return scheduleId String
     */
    public String getScheduleId() {
        return scheduleId;
    }

    /**
     * Method used to set the schedule id
     * @param newScheduleId String
     */
     public void setScheduleId(String newScheduleId){
        String oldScheduleId = scheduleId;
        this.scheduleId = newScheduleId;
        propertySupport.firePropertyChange(PROP_SCHEDULE_ID, 
                oldScheduleId, scheduleId);
    }
    
    /**
     * Method used to get the yes vote count
     * @return yesVoteCount int
     */
    public int getYesVoteCount() {
        return yesVoteCount;
    }

    /**
     * Method used to set the yes vote count
     * @param newYesVoteCount integer
     */
    public void setYesVoteCount(int newYesVoteCount){
        int oldYesVoteCount = yesVoteCount;
        this.yesVoteCount = newYesVoteCount;
        propertySupport.firePropertyChange(PROP_YES_VOTE, 
                oldYesVoteCount, yesVoteCount);
    }

    /**
     * Method used to get the no vote count
     * @return noVoteCount int
     */
    public int getNoVoteCount() {
        return noVoteCount;
    }

    /**
     * Method used to set the no vote count
     * @param newNoVoteCount integer
     */
    public void setNoVoteCount(int newNoVoteCount){
        int oldNoVoteCount = noVoteCount;
        this.noVoteCount = newNoVoteCount;
        propertySupport.firePropertyChange(PROP_NO_VOTE, 
                oldNoVoteCount, noVoteCount);
    }
    
    
    //prps start jul 2nd 2003
    
    /**
     * Method used to get the abstainerCount
     * @return abstainerCount int
     */
    public int getAbstainerCount() {
        return abstainerCount;
    }

    /**
     * Method used to set abstainerCount 
     * @param abstainerCount integer
     */
    public void setAbstainerCount(int newAbstainerCount){
        int oldAbstainerCount = abstainerCount;
        this.abstainerCount = newAbstainerCount;
        propertySupport.firePropertyChange(PROP_ABSTAINER_COUNT, 
                oldAbstainerCount, abstainerCount);
    }
    
    
    /**
     * Method used to get votingComments
     * @return voting votingComments String
     */
    public String getVotingComments() {
        return votingComments ;
    }

    /**
     * Method used to set votingComments
     * @param votingComments String
     */
    public void setVotingComments(String newVotingComments){
        String oldVotingComments = votingComments;
        this.votingComments = newVotingComments;
        propertySupport.firePropertyChange(PROP_VOTING_COMMENTS, 
                oldVotingComments, votingComments);
    }
        
     /**
     * Method used to get the submission number
     * @return submission number int
     */
    public int getSubmissionNumber() {
        return submissionNumber ;
    }

    /**
     * Method used to set submission number 
     * @param submission number int
     */
    public void setSubmissionNumber(int submissionNumber){
        int oldSubmissionNumber = submissionNumber;
        this.submissionNumber = submissionNumber;
        propertySupport.firePropertyChange(PROP_SUBMISSION_NUMBER, 
                oldSubmissionNumber, submissionNumber);
    }
        
       
    //prps end
    
    /**
     * Method used to get the Vector of protocolVoteAbstainees
     * @return protocolVoteAbstainees Vector
     */
    public Vector getProtocolVoteAbstainee(){
        return protocolVoteAbstainees;
    }
    
    /**
     * Method used to set the Vector of protocolVoteAbstainees
     * @param protocolVoteAbstainees Vector
     */
    public void setProtocolVoteAbstainee(Vector protocolVoteAbstainees){
        this.protocolVoteAbstainees = protocolVoteAbstainees;
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
     * Method used to get the Action Type
     * @return acType String
     */
    public String getAcType(){
        return acType;
    }
    
    /**
     * Method used to set the Action Type
     * @param acType String
     */
    public void setAcType(String acType){
        this.acType = acType;
    }
     /**
     * Method used to add propertychange listener to the fields
     * @param listener PropertyChangeListener
     */ 
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    /**
     * Method used to remove propertychange listener
     * @param listener PropertyChangeListener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
}