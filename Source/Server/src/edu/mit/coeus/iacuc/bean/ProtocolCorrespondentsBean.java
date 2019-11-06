/*
 * @(#)ProtocolCorrespondentsBean.java October 24, 2002, 12:49 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.iacuc.bean.*;
import java.beans.*;
import edu.mit.coeus.bean.IBaseDataBean;

/**
 * The class used to hold the information of <code>Protocol Correspondents</code>
 *
 * @author  phani
 * @version 1.0
 * Created on October 24, 2002, 12:49 PM
 */

public class ProtocolCorrespondentsBean implements java.io.Serializable, IBaseDataBean, BaseBean {
    // holds the person id 
    private static final String PROP_PERSON_ID = "personId";
    // holds the employee flag 
    private static final String PROP_NON_EMPLOYEE_FLAG = "nonEmployeeFlag";
    // holds the correspondent type code 
    private static final String 
                    PROP_CORRESPONDENT_TYPE_CODE = "correspondentTypeCode";
    // holds the correspondents cooments 
    private static final String PROP_COMMENTS = "comments";
    
    //holds protocol number
    private String protocolNumber;
    //holds sequence number
    private int sequenceNumber;
    //holds person id
    private String personId;
    //holds person name 
    private String personName;
    //holds non employee flag 
    private boolean nonEmployeeFlag;
    //holds correspondent type code
    private int correspondentTypeCode;
    
    private int awCorrespondentTypeCode;
    //holds correspondent type description
    private String correspondentTypeDesc;
    //holds correspondent comments
    private String comments;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds Action type
    private String acType;
    // property added to fix the updation issue
    private String awPersonId;
    
    private PropertyChangeSupport propertySupport;
    
    /** Creates new ProtocolCorrespondentsBean */
    public ProtocolCorrespondentsBean() {
        propertySupport = new PropertyChangeSupport( this );
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
     * Method used to get the sequence number
     * @return sequenceNumber int
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Method used to set the sequence number
     * @param sequenceNumber int
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
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
     * @param newPersonId String
     */
    public void setPersonId(String newPersonId) {
         String oldPersonId  = personId;
        this.personId  = newPersonId;
        propertySupport.firePropertyChange(PROP_PERSON_ID, 
                oldPersonId, personId);
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
     * Method used to get the nonemployee flag
     * @return nonEmployeeFlag boolean
     */
    public boolean  isNonEmployeeFlag() {
        return nonEmployeeFlag;
    }

    /**
     * Method used to set the nonemployee flag
     * @param newNonEmployeeFlag boolean
     */
    public void setNonEmployeeFlag(boolean newNonEmployeeFlag) {
        boolean oldNonEmployeeFlag  = nonEmployeeFlag;
        this.nonEmployeeFlag  = newNonEmployeeFlag;
        propertySupport.firePropertyChange(PROP_NON_EMPLOYEE_FLAG, 
                oldNonEmployeeFlag, nonEmployeeFlag);
    }

     /**
     * Method used to get the correspondent type code
     * @return correspondentTypeCode int
     */
    public int getCorrespondentTypeCode() {
        return correspondentTypeCode;
    }

    /**
     * Method used to set the orrespondent type code
     * @param newCorrespondentTypeCode integer
     */
    public void setCorrespondentTypeCode(int newCorrespondentTypeCode) {
        int oldCorrespondentTypeCode  = correspondentTypeCode;
        this.correspondentTypeCode = newCorrespondentTypeCode;
        propertySupport.firePropertyChange(PROP_CORRESPONDENT_TYPE_CODE, 
                oldCorrespondentTypeCode, correspondentTypeCode);
    }

     /**
     * Method used to get the correspondent type description
     * @return correspondentTypeDesc String
     */
    public String getCorrespondentTypeDesc() {
        return correspondentTypeDesc;
    }

    /**
     * Method used to set the correspondent type description
     * @param correspondentTypeDesc String
     */
    public void setCorrespondentTypeDesc(String correspondentTypeDesc) {
        this.correspondentTypeDesc = correspondentTypeDesc;
    }

     /**
     * Method used to get the correspondent comments
     * @return comments String
     */
    public String getComments() {
        return comments;
    }

    /**
     * Method used to set the correspondent comments
     * @param newComments String
     */
    public void setComments(String newComments) {
        String oldComments = comments;
        this.comments = newComments;
        propertySupport.firePropertyChange(PROP_COMMENTS, 
                oldComments, comments);
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
     * To print the bean data in the server side 
     * 
     * @return String 
     */
     public String toString(){
        StringBuffer strBffr = new StringBuffer();
        strBffr.append("{ Protocol Number =>"+protocolNumber);
        strBffr.append("sequenceNumber =>"+sequenceNumber);
        strBffr.append("personId  =>"+ personId);
        strBffr.append("personName =>"+personName);
        strBffr.append("nonEmployeeFlag  =>"+nonEmployeeFlag );
        strBffr.append("correspondentTypeCode  =>"+correspondentTypeCode);
        strBffr.append("correspondentTypeDesc =>"+correspondentTypeDesc);
        strBffr.append("comments =>"+comments);
        strBffr.append("updateUser =>"+updateUser);
        strBffr.append("updateTimestamp =>"+updateTimestamp);
        strBffr.append("acType =>"+acType);
    
        return strBffr.toString();
    }

     /** Getter for property awCorrespondentTypeCode.
      * @return Value of property awCorrespondentTypeCode.
      */
     public int getAwCorrespondentTypeCode() {
         return awCorrespondentTypeCode;
     }     

     /** Setter for property awCorrespondentTypeCode.
      * @param awCorrespondentTypeCode New value of property awCorrespondentTypeCode.
      */
     public void setAwCorrespondentTypeCode(int awCorrespondentTypeCode) {
         this.awCorrespondentTypeCode = awCorrespondentTypeCode;
     }
     
     // Added to fix the updation issue - Start
    /**
     * Method to get the person id from the database
     * @return awPersonId - String
     */
     public String getAwPersonId() {
         return awPersonId;
     }
     
    /**
     * Method to set the person id from the database
     * @param awPersonId - String
     */
     public void setAwPersonId(String awPersonId) {
         this.awPersonId = awPersonId;
     }
     // Added to fix the updation issue - Start
}
