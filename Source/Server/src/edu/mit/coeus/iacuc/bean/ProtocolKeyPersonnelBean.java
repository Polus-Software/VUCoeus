/*
 * @(#)ProtocolKeyPersonnelBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.bean.BaseBean;
import java.beans.*;
import edu.mit.coeus.bean.IBaseDataBean;
/**
 * The class used to hold the information of <code>ProtocolKeyPersonnelBean</code>
 *
 * @author  phani
 * @version 1.0
 * Created on October 24, 2002, 7:48 PM
 */

public class ProtocolKeyPersonnelBean implements java.io.Serializable, IBaseDataBean, BaseBean {
    
    private static final String PROP_PERSON_ID = "PersonId";
    private static final String PROP_FACULTY_FLAG = "FacultyFlag";
    private static final String PROP_PERSON_ROLE = "personRoleCode";
    private static final String PROTO_AFFILIATION_TYPE_CODE = "affiliationTypeCode";
    private String protocolNumber;
    private int sequenceNumber;
    private String personId;
    private String personName;
    private int personRoleCode;
    private String personRoleDesc;
    private String personTitle;
    private boolean nonEmployeeFlag;
    private boolean facultyFlag;
    private int affiliationTypeCode;
    private String affiliationTypeDescription;
    private String updateUser;
    private java.sql.Timestamp updateTimestamp;
    private String acType;
     //Coeus enhancement 32.2 - step 1 : start
    private boolean trainingFlag;
    //Coeus enhancement 32.2 - step 1 : end
    private PropertyChangeSupport propertySupport;
    
    /** Creates new ProtocolKeyPersonnelBean */
    public ProtocolKeyPersonnelBean() {
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
     * Method used to get the personId
     * @return personId String
     */
    public String getPersonId() {
        return personId;
    }
    /**
     * Method used to set the protocol number
     * @param newPersonId String
     */
    public void setPersonId(String newPersonId) {
        String oldPersonId = personId;
        this.personId = newPersonId;
        propertySupport.firePropertyChange(PROP_PERSON_ID, oldPersonId, 
                personId);
    }
    /**
     * Method used to get the personName
     * @return personName Stringv
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
     * Method used to get the personRoleCode
     * 
     * @return personRoleCode String
     */
    public int getPersonRoleCode() {
        return personRoleCode;
    }
    /**
     * Method used to get the personTitle
     * @return personTitle String
     */
    public String getPersonTitle(){
        return personTitle;
    }
    /**
     * Method used to set the personTitle
     * @param personTitle String
     */
    public void setPersonTitle(String personTitle){
        this.personTitle = personTitle;
    }
     /**
     * Method used to set the PersonRole
     * @param newPersonRole String
     */
    public void setPersonRoleCode(int newPersonRoleCode) {
        int oldPersonRoleCode = personRoleCode;
        this.personRoleCode = newPersonRoleCode;
        propertySupport.firePropertyChange(PROP_PERSON_ROLE, oldPersonRoleCode, 
                personRoleCode);

    }
    /**
     * Method used to get the nonEmployeeFlag
     * @return nonEmployeeFlag boolean
     */
    public boolean isNonEmployeeFlag() {
        return nonEmployeeFlag;
    }
    /**
     * Method used to set the NonEmployeeFlag
     * @param nonEmployeeFlag boolean
     */
    public void setNonEmployeeFlag(boolean nonEmployeeFlag) {
     this.nonEmployeeFlag = nonEmployeeFlag;
    }
    /**
     * Method used to get the facultyFlag
     * @return facultyFlag boolean
     */
    public boolean isFacultyFlag() {
        return facultyFlag;
    }
    /**
     * Method used to set the FacultyFlag
     * @param newFacultyFlag boolean
     */
    public void setFacultyFlag(boolean newFacultyFlag) {
        boolean oldFacultyFlag = facultyFlag;
        this.facultyFlag = newFacultyFlag;
        propertySupport.firePropertyChange(PROP_FACULTY_FLAG, oldFacultyFlag, 
                facultyFlag);

    }

    /**
     * Method used to get the affiliationTypeCode
     * @return facultyFlag int
     */
    public int getAffiliationTypeCode() {
        return affiliationTypeCode;
    }
    /**
     * Method used to set the affiliationTypeCode
     * @param newAffiliationTypeCode int
     */
    public void setAffiliationTypeCode(int newAffiliationTypeCode) {
        int oldAffiliationTypeCode = affiliationTypeCode;
        this.affiliationTypeCode = newAffiliationTypeCode;
        propertySupport.firePropertyChange(PROTO_AFFILIATION_TYPE_CODE, oldAffiliationTypeCode, 
                affiliationTypeCode);

    }    
    
    /**
     * Method used to get the affiliationTypeDescription
     * @return acType String
     */
    public String getAffiliationTypeDescription(){
        return affiliationTypeDescription;
    }
    /**
     * Method used to set the affiliationTypeDescription
     * @param affiliationTypeDescription String
     */
    public void setAffiliationTypeDescription(String affiliationTypeDescription){
        this.affiliationTypeDescription = affiliationTypeDescription;
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
        strBffr.append("personRole  =>"+personRoleCode );
        strBffr.append("nonEmployeeFlag   =>"+nonEmployeeFlag);
        strBffr.append("facultyFlag   =>"+facultyFlag);
        strBffr.append("updateUser =>"+updateUser);
        strBffr.append("updateTimestamp =>"+updateTimestamp);
        strBffr.append("acType =>"+acType);
        return strBffr.toString();
    }
     
     /**
      * Getter for property trainingFlag.
      * @return Value of property trainingFlag.
      */
     public boolean isTrainingFlag() {
         return trainingFlag;
     }
     
     /**
      * Setter for property trainingFlag.
      * @param trainingFlag New value of property trainingFlag.
      */
     public void setTrainingFlag(boolean trainingFlag) {
         this.trainingFlag = trainingFlag;
     }

    public String getPersonRoleDesc() {
        return personRoleDesc;
    }

    public void setPersonRoleDesc(String personRoleDesc) {
        this.personRoleDesc = personRoleDesc;
    }
     
}
