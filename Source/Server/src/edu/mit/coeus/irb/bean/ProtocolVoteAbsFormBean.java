/*
 * @(#)ProtocolVoteAbsFormBean.java 1.0 03/20/03 11:11 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.irb.bean;

import java.beans.*;
import java.sql.Timestamp;

/**
 * The class used to hold the information of <code>Protocol Vote Abstainees</code>
 *
 * @author  Mukundan C
 * @version 1.0
 * Created on March 20, 2003, 11:11 AM
 */

public class ProtocolVoteAbsFormBean implements java.io.Serializable {
     // holds the non employee flag
    private static final String PROP_NON_EMPLOYEE_FLAG = "nonEmployeeFlag";
    private String protocolNumber;
    private int sequenceNumber;
    private String scheduleId;
    private String personId;
    private String personName;
    private String comments;
    // holds the non employee flag
    private boolean nonEmployeeFlag;
    //holds update timestamp
    private Timestamp updateTimestamp;
    //holds update user id
    private String updateUser;
    //holds account type
    private String acType;
    
    private PropertyChangeSupport propertySupport;
    /** Creates new ProtocolVoteAbsFormBean */
    public ProtocolVoteAbsFormBean() {
        propertySupport = new PropertyChangeSupport( this );
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
    /**
     * Method used to get the protocol number
     * @return protocolNumber String
     */
    public String getProtocolNumber(){
        return protocolNumber;
    }
    
    /**
     * Method used to set the protocol number
     * @param protocolNumber String
     */
    public void setProtocolNumber(String protocolNumber){
        this.protocolNumber = protocolNumber;
    }

    /**
     * Method used to get the sequence number
     * @return sequenceNumber int
     */
    public int getSequenceNumber(){
        return sequenceNumber;
    }
    
    /**
     * Method used to set the sequence number
     * @param sequenceNumber int
     */
    public void setSequenceNumber(int sequenceNumber){
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * Method used to get the scheduleId 
     * @return scheduleId String
     */
    public String getScheduleId(){
        return scheduleId;
    }
    
    /**
     * Method used to set the scheduleId 
     * @param scheduleId String
     */
    public void setScheduleId(String scheduleId){
        this.scheduleId = scheduleId;
    }

    /**
     * Method used to get the personId 
     * @return personId String
     */
    public String getPersonId(){
        return personId;
    }
    
    /**
     * Method used to set the personId 
     * @param personId String
     */
    public void setPersonId(String personId){
        this.personId = personId;
    }
    
    /**
     * Method used to get the personName 
     * @return personName String
     */
    public String getPersonName(){
        return personName;
    }
    
    /**
     * Method used to set the personName 
     * @param personName String
     */
    public void setPersonName(String personName){
        this.personName = personName;
    }
    
    /**
     * Method used to get the comments 
     * @return comments String
     */
    public String getComments(){
        return comments;
    }
    
    /**
     * Method used to set the comments 
     * @param comments String
     */
    public void setComments(String comments){
        this.comments = comments;
    }
    
        /**
     * Method used to get the non employee flag
     * @return nonEmployeeFlag boolean
     */
    public boolean getNonEmployeeFlag() {
        return nonEmployeeFlag;
    }

    /**
     * Method used to set the non employee flag
     * @param newNonEmployeeFlag boolean
     */
    public void setNonEmployeeFlag(boolean newNonEmployeeFlag){
        boolean oldNonEmployeeFlag = nonEmployeeFlag;
        this.nonEmployeeFlag = newNonEmployeeFlag;
        propertySupport.firePropertyChange(PROP_NON_EMPLOYEE_FLAG, 
                oldNonEmployeeFlag, nonEmployeeFlag);
    }
    
     /**
     * Method used to get the update timestamp
     * @return Timestamp updateTimestamp 
     */
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    /**
     * Method used to set the update timestamp
     * @param updateTimestamp holds the timestamp for the record
     */
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    /**
     * Method used to get the update user id
     * @return String updateUser 
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * Method used to set the update user id
     * @param updateUser holds the user who logged in
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * Method used to get the Action Type
     * @return String acType 
     */
    public String getAcType() {
        return acType;
    }

    /**
     * Method used to set the Action Type
     * @param acType holds the Action type for insert/modify/delete
     */
    public void setAcType(String acType) {
        this.acType = acType;
    }
    
}
