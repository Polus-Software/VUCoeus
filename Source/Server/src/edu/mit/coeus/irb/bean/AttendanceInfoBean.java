/*
 * @(#)AttendanceInfoBean.java 1.0 11/18/02 3:40 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.bean;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.sql.Timestamp;
import java.io.Serializable;

/** The class used to hold the information of <code>Schedule Attendees</code>
 * The data will get attached with the <code>Schedule Attendance</code> form.
 *
 *
 * @author Mukundan C  Created on November 18, 2002, 3:40 PM
 * @version 1.0
 */

public class AttendanceInfoBean implements Serializable {
    // holds the guest flag
    private static final String PROP_GUEST_FLAG = "guestFlag";
    // holds the alternate flag
    private static final String PROP_ALTERNATE_FLAG = "alternateFlag";
    // holds the alternate for
    private static final String PROP_ALTERNATE_FOR = "alternateFor";
    // holds the non employee flag
    private static final String PROP_NON_EMPLOYEE_FLAG = "nonEmployeeFlag";
    // holds the comments
    private static final String PROP_COMMENTS = "comments";
    // holds the schedule id
    private String scheduleId;
    // holds the person id
    private String personId;
    // holds the person name
    private String personName;
    // holds the alternate PersonName
    private String alternatePersonName;
    // holds the guest flag
    private boolean guestFlag;
     // holds the alternate flag
    private boolean alternateFlag;
    // holds the alternate for
    private String alternateFor;
    // holds the non employee flag
    private boolean nonEmployeeFlag;
    // holds the comments
    private String comments;
    //holds update timestamp
    private Timestamp updateTimestamp;
    //holds update user id
    private String updateUser;
    //holds account type
    private String acType;
    
    // Holds whether the person has Alternate Role or Not -Enhancement-1588
    private boolean alternatePerson;
    
    private PropertyChangeSupport propertySupport;
    
    /** Creates a new instance of AttendanceInfoBean */
    public AttendanceInfoBean() {
        propertySupport = new PropertyChangeSupport( this );
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
     * @param scheduleId String
     */
    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    /**
     * Method used to get the person id
     * @return personId String
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * Method used to set the person id
     * @param personId String
     */
    public void setPersonId(String personId) {
        this.personId = personId;
    }

    /**
     * Method used to get the person name
     * @return personName String
     */
    public String getPersonName() {
        return personName;
    }

    /**
     * Method used to set the person name
     * @param personName String
     */
    public void setPersonName(String personName) {
        this.personName = personName;
    }

    /**
     * Method used to get the alternatePerson Name
     * @return alternatePersonName String
     *Case #1588
     */
    public String getAlternatePersonName() {
        return alternatePersonName;
    }

    /**
     * Method used to set the alternatePerson Name
     * @param alternatePersonName String
     *Case #1588
     */
    public void setAlternatePersonName(String alternatePersonName) {
        this.alternatePersonName = alternatePersonName;
    }
    /**
     * Method used to get the guset flag
     * @return guestFlag boolean
     */
    public boolean getGuestFlag() {
        return guestFlag;
    }

    /**
     * Method used to set the guest flag
     * @param newGuestFlag boolean
     */
    public void setGuestFlag(boolean newGuestFlag){
        boolean oldGuestFlag = guestFlag;
        this.guestFlag = newGuestFlag;
        propertySupport.firePropertyChange(PROP_GUEST_FLAG, 
                oldGuestFlag, guestFlag);
    }

    /**
     * Method used to get the alternate flag
     * @return alternateFlag boolean
     */
    public boolean getAlternateFlag() {
        return alternateFlag;
    }

    /**
     * Method used to set the alternate flag
     * @param newAlternateFlag boolean
     */
    public void setAlternateFlag(boolean newAlternateFlag){
        boolean oldAlternateFlag = alternateFlag;
        this.alternateFlag = newAlternateFlag;
        propertySupport.firePropertyChange(PROP_ALTERNATE_FLAG, 
                oldAlternateFlag, alternateFlag);
    }

    /**
     * Method used to get the alternate for
     * @return alternateFor String
     */
    public String getAlternateFor() {
        return alternateFor;
    }

    /**
     * Method used to set the alternate for
     * @param newAlternateFor String
     */
    public void setAlternateFor(String newAlternateFor){
        String oldAlternateFor = alternateFor;
        this.alternateFor = newAlternateFor;
        propertySupport.firePropertyChange(PROP_ALTERNATE_FOR, 
                oldAlternateFor, alternateFor);
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
     * Method used to get the comments
     * @return comments String
     */
    public String getComments() {
        return comments;
    }

     /**
     * Method used to set the comments
     * @param newComments String
     */
    public void setComments(String newComments){
        String oldComments = comments;
        this.comments = newComments;
        propertySupport.firePropertyChange(PROP_COMMENTS, 
                oldComments, comments);
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

     /** Method used to get the Action Type
      * @return acType String
      */
    public String getAcType(){
        return acType;
    }
    
    /** Method used to set the Action Type
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
    
    /**
     * Getter for property alternatePerson.
     * @return Value of property alternatePerson.
     */
    public boolean isAlternatePerson() {
        return alternatePerson;
    }
    
    /**
     * Setter for property alternatePerson.
     * @param alternatePerson New value of property alternatePerson.
     */
    public void setAlternatePerson(boolean alternatePerson) {
        this.alternatePerson = alternatePerson;
    }
    
}
