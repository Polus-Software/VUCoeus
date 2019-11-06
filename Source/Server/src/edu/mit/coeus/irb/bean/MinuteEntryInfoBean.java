/*
 * @(#)MinuteEntryInfoBean.java 11/27/2002 6:22 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The class used to hold the information of <code>Minute Entry Form</code>
 * 
 *
 * 
 * @author  Mukundan.C Created on November 27, 2002, 6:22 PM
 * @version 1.0
 */

public class MinuteEntryInfoBean implements Serializable {
    // holds the minute entry type code
    private static final String PROP_MINUTE_ENTRY_CODE = "minuteEntryTypeCode";
    // holds the protocol number
    private static final String PROP_PROTOCOL_NUMBER = "protocolNumber";
    // holds the sequence number
    private static final String PROP_SEQUENCE_NUMBER = "sequenceNumber";
    // holds the private comment flag
    private static final String PROP_PRIVATE_COM_FLAG = "privateCommentFlag";
    // holds the protocol contingency code
    private static final String PROP_PROTO_CON_CODE = "protocolContingencyCode";
     // holds the minute entry
    private static final String PROP_MINUTE_ENTRY = "minuteEntry";
    
    // holds the schedule id
    private String scheduleId;
    // holds the entry number
    private int entryNumber;
    // holds the minute entry type code
    private int minuteEntryTypeCode;
    // holds the minute entry type description
    private String minuteEntryTypeDesc;
    // holds the protocol number
    private String protocolNumber;
    // holds the sequence number
    private int sequenceNumber;
    //holds submission Number
    private int submissionNumber;
    // holds the private comment flag
    private boolean privateCommentFlag;
    // holds the protocol contingency code
    private String protocolContingencyCode;
    // holds the protocol contingency description
    private String protocolContingencyDesc;
    // holds the minute entry
    private String minuteEntry;
    // holds the minute entry
    private String otherItemDesc;
    //holds update timestamp
    private Timestamp updateTimestamp;
    //holds update user id
    private String updateUser;
    //holds account type
    private String acType;
    // 3282: Reviewer view of Protocol materials - Start
    private boolean finalFlag;
    private String personId;
    private String updateUserName;
    // 3282: Reviewer view of Protocol materials - End
    private PropertyChangeSupport propertySupport;
    //Added for COEUSQA-2291 : Hide Reviewer Name in Review Comments - Start
    private String canUserViewRewierName;
    private String createUser;
    private String createUserName;
    private Timestamp createTimestamp;
    private String reviewerName;
    private String personName;
    private boolean isReviewCommentSwapped;
    //COEUSQA-2291 : End
    //Added for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start
    private boolean isUserAdmin;
    //COEUSQA-2288 : End
    /** Creates a new instance of MinuteEntryInfoBean */
    public MinuteEntryInfoBean() {
        propertySupport = new PropertyChangeSupport( this );
    }

    /**
     * Method used to get the schedule id
     * @return String scheduleId
     */
    public String getScheduleId() {
        return scheduleId;
    }

    /**
     * Method used to set the schedule id
     * @param scheduleId holds the scheduleId
     */
    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    /**
     * Method used to get the entry number
     * @return integer entryNumber 
     */
    public int getEntryNumber() {
        return entryNumber;
    }

    /**
     * Method used to set the entry number
     * @param entryNumber holds the entry number
     */
    public void setEntryNumber(int entryNumber) {
        this.entryNumber = entryNumber;
    }

    /**
     * Method used to get the entry type code
     * @return integer minuteEntryTypeCode 
     */
    public int getMinuteEntryTypeCode() {
        return minuteEntryTypeCode;
    }

    /**
     * Method used to set the entry type code
     * @param newMinuteEntryTypeCode holds the minute entry type code
     */
   public void setMinuteEntryTypeCode(int newMinuteEntryTypeCode){
        int oldMinuteEntryTypeCode = minuteEntryTypeCode;
        this.minuteEntryTypeCode = newMinuteEntryTypeCode;
        propertySupport.firePropertyChange(PROP_MINUTE_ENTRY_CODE, 
                oldMinuteEntryTypeCode, minuteEntryTypeCode);
    }

    /**
     * Method used to get the entry type description
     * @return String minuteEntryTypeDesc 
     */
    public String getMinuteEntryTypeDesc() {
        return minuteEntryTypeDesc;
    }

    /**
     * Method used to set the entry type description
     * @param minuteEntryTypeDesc holds entry type description
     */
    public void setMinuteEntryTypeDesc(String minuteEntryTypeDesc) {
        this.minuteEntryTypeDesc = minuteEntryTypeDesc;
    }

    /**
     * Method used to get the protocol number
     * @return String protocolNumber 
     */
    public String getProtocolNumber() {
        return protocolNumber;
    }

    /**
     * Method used to set the protocol number
     * @param newProtocolNumber holds protocol number
     */
    public void setProtocolNumber(String newProtocolNumber){
        String oldProtocolNumber = protocolNumber;
        this.protocolNumber = newProtocolNumber;
        propertySupport.firePropertyChange(PROP_PROTOCOL_NUMBER, 
                oldProtocolNumber, protocolNumber);   
    }
    /**
     * Method used to get the sequence number
     * @return integer sequenceNumber 
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Method used to set the sequence number
     * @param newSequenceNumber holds sequence number
     */
   public void setSequenceNumber(int newSequenceNumber){
        int oldSequenceNumber = sequenceNumber;
        this.sequenceNumber = newSequenceNumber;
        propertySupport.firePropertyChange(PROP_SEQUENCE_NUMBER, 
                oldSequenceNumber, sequenceNumber);
    }
    /**
     * Method used to get the private comment flag
     * @return boolean privateCommentFlag 
     */
    public boolean isPrivateCommentFlag() {
        return privateCommentFlag;
    }

    /**
     * Method used to set the private comment flag
     * @param newPrivateCommentFlag holds comment flag
     */
    public void setPrivateCommentFlag(boolean newPrivateCommentFlag){
        boolean oldPrivateCommentFlag = privateCommentFlag;
        this.privateCommentFlag = newPrivateCommentFlag;
        propertySupport.firePropertyChange(PROP_PRIVATE_COM_FLAG, 
                oldPrivateCommentFlag, privateCommentFlag);
    }
    
    /**
     * Method used to get the protocol contingency code
     * @return String protocolContingencyCode 
     */
    public String getProtocolContingencyCode() {
        return protocolContingencyCode;
    }

    /**
     * Method used to set the protocol contingency code
     * @param newProtocolContingencyCode holds protocol contingency code
     */
    public void setProtocolContingencyCode(String newProtocolContingencyCode){
        String oldProtocolContingencyCode = protocolContingencyCode;
        this.protocolContingencyCode = newProtocolContingencyCode;
        propertySupport.firePropertyChange(PROP_PROTO_CON_CODE, 
                oldProtocolContingencyCode, protocolContingencyCode);
    }
    
    /**
     * Method used to get the protocol contingency description
     * @return String protocolContingencyDesc 
     */
    public String getProtocolContingencyDesc() {
        return protocolContingencyDesc;
    }

    /**
     * Method used to set the protocol contingency description
     * @param protocolContingencyDesc holds protocol contingency description
     */
    public void setProtocolContingencyDesc(String protocolContingencyDesc) {
        this.protocolContingencyDesc = protocolContingencyDesc;
    }

    /**
     * Method used to get the minute entry
     * @return String minuteEntry 
     */
    public String getMinuteEntry() {
        return minuteEntry;
    }

    /**
     * Method used to set the minute entry
     * @param newMinuteEntry holds minute entry
     */
    public void setMinuteEntry(String newMinuteEntry){
        String oldMinuteEntry = minuteEntry;
        this.minuteEntry = newMinuteEntry;
        propertySupport.firePropertyChange(PROP_MINUTE_ENTRY, 
                                 oldMinuteEntry, minuteEntry);
    }
    
    /**
     * Method used to get the otherItemDesc 
     * @return String otherItemDesc 
     */
    public String getOtherItemDesc() {
        return otherItemDesc;
    }

    /**
     * Method used to set the other Item Description
     * @param otherItemDesc holds other Item Description
     */
    public void setOtherItemDesc(String otherItemDesc){
        this.otherItemDesc = otherItemDesc;
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
    
    /** Getter for property submissionNumber.
     * @return Value of property submissionNumber.
     */
    public int getSubmissionNumber() {
        return submissionNumber;
    }
    
    /** Setter for property submissionNumber.
     * @param submissionNumber New value of property submissionNumber.
     */
    public void setSubmissionNumber(int submissionNumber) {
        this.submissionNumber = submissionNumber;
    }
    // 3282: Reviewer view of Protocol materials - Start
    public boolean isFinalFlag() {
        return finalFlag;
    }

    public void setFinalFlag(boolean finalFlag) {
        this.finalFlag = finalFlag;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
   
    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }
   // 3282: Reviewer view of Protocol materials - End
    
    //Added for COEUSQA-2291 : Hide Reviewer Name in Review Comments - Start
    /*
     * Method to get create user
     * @return createUser - String
     */
    public String getCreateUser() {
        return createUser;
    }
    
    /*
     * Method to set create user
     * @param createuser - String
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
    
    /*
     * Method to get create time stamp
     * @return createTimestamp - Timestamp
     */
    public Timestamp getCreateTimestamp() {
        return createTimestamp;
    }
    
    /*
     * Method to set create time stamp
     * @param createTimestamp - Timestamp
     */
    public void setCreateTimestamp(Timestamp createTimestamp) {
        this.createTimestamp = createTimestamp;
    }
    
    /*
     * Method to get reviewer name
     * @return reviewerName - String
     */
    public String getReviewerName() {
        return reviewerName;
    }
    
    /*
     * Method to set reviewer name
     * @param reviewerName - String
     */
    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }
    /**
     * Getter for property canUserViewRewierName.
     * 
     * 
     * @return Value of property canUserViewRewierName.
     */
    public String canUserViewRewierName() {
        return canUserViewRewierName;
    }
    
    /**
     * Setter for property canUserViewRewierName.
     * 
     * 
     * @param canUserViewRewierName New value of property canUserViewRewierName.
     */
    public void setCanUserViewRewierName(String canUserViewRewier) {
        this.canUserViewRewierName = canUserViewRewier;
    }

    /*
     * Method to get create user name
     * @return createUserName - String
     */
    public String getCreateUserName() {
        return createUserName;
    }
    
    /*
     * Method to set create user name
     * @param createUserName - String
     */
    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    /*
     * Method to get personName
     * @return personName - String
     */
    public String getPersonName() {
        return personName;
    }
    
    /*
     * Method to set person Name
     * @param personName - String
     */
    public void setPersonName(String personName) {
        this.personName = personName;
    }
    //COEUSQA-2291 : End
    //Added for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start
   /*
    * Method to get whether logged-in user is IRB Admin at the protocol sequence
    * @return isUserAdmin
    */
    public boolean isUserAdmin() {
        return isUserAdmin;
    }
    
    /*
     * Method to set whether logged-in user is IRB admin at the protocol sequence
     */
    public void setIsUserAdmin(boolean isUserAdmin) {
        this.isUserAdmin = isUserAdmin;
    }
    //COEUSQA-2288 : End
    //Added for COEUSQA-2291 : Hide Reviewer Name in Review Comments - Start
    /*
     * Method to check is the review comment is swapped
     * @return isReviewCommentSwapped - boolean 
     */
    public boolean isReviewCommentSwapped() {
        return isReviewCommentSwapped;
    }
    
    /*
     * Method to set is the review comment is swapped
     * @param isReviewCommentSwapped - boolean
     */
    public void setReviewCommentSwapped(boolean isReviewCommentSwapped) {
        this.isReviewCommentSwapped = isReviewCommentSwapped;
    }
    //COEUSQA-2291 : End
}
