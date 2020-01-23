/*
 * @(#)ProtocolInvestigatorsBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.iacuc.bean;

import java.beans.*;
import edu.mit.coeus.bean.IBaseDataBean;
/**
 * The class used to hold the information of <code>ProtocolInvestigatorsBean</code>
 * 
 * @author  phani
 * @version 1.0
 * Created on October 24, 2002, 12:33 PM
 */
public class ProtocolInvestigatorsBean  implements java.io.Serializable, IBaseDataBean {
    
    private static final String PROP_PERSON_ID = "personId";
    private static final String PROP_NON_EMPLOYEE_FLAG = "nonEmployeeFlag";
    private static final String PROP_FACULTY_FLAG = "facultyFlag";
    private static final String PROP_PRINCIPAL_INVESTIGATOR_FLAG = "principalInvestigatorFlag";
    private static final String AFFILIATION_TYPE_CODE = "affiliationTypeCode";
    
    private String protocolNumber;
    private int sequenceNumber;
    private String personId;
    private String personName;
    private boolean nonEmployeeFlag;
    private boolean facultyFlag;
    private boolean principalInvestigatorFlag;
    private java.util.Vector investigatorUnits;
    private String updateUser;
    private java.sql.Timestamp updateTimestamp;
    private String acType;
    
    private int affiliationTypeCode;
    private String affiliationTypeDescription;
    
    
    private PropertyChangeSupport propertySupport;
    //Coeus enhancement 32.2 - step 1 : start
    private boolean trainingFlag;
    //Coeus enhancement 32.2 - step 1 : end
    /** Creates new ProtocolInvestigatorsBean */
    public ProtocolInvestigatorsBean() {
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
     * Method used to remove PropertyChangeListener
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
         String oldPersonId  = personId;
        this.personId  = newPersonId;
        propertySupport.firePropertyChange(PROP_PERSON_ID, 
                oldPersonId, personId);
    }
    /**
     * Method used to get the personName
     * @return personName Stringv
     */
    public String getPersonName() {
        return personName;
    }
    /**
     * Method used to set the personName
     * @param personName String
     */
    public void setPersonName(String personName) {
        this.personName = personName;
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
     * @param newNonEmployeeFlag boolean
     */
    public void setNonEmployeeFlag(boolean newNonEmployeeFlag) {
        boolean oldNonEmployeeFlag  = nonEmployeeFlag;
        this.nonEmployeeFlag  = newNonEmployeeFlag;
        propertySupport.firePropertyChange(PROP_NON_EMPLOYEE_FLAG, 
                oldNonEmployeeFlag, nonEmployeeFlag);
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
        boolean oldFacultyFlag  = facultyFlag;
        this.facultyFlag  = newFacultyFlag;
        propertySupport.firePropertyChange(PROP_FACULTY_FLAG, 
                oldFacultyFlag, facultyFlag);
    }
    /**
     * Method used to get the principalInvestigatorFlag
     * @return principalInvestigatorFlag boolean
     */
    public boolean isPrincipalInvestigatorFlag() {
        return principalInvestigatorFlag;
    }
    /**
     * Method used to set the PrincipalInvestigatorFlag
     * @param newPrincipalInvestigatorFlag boolean
     */
    public void setPrincipalInvestigatorFlag(boolean newPrincipalInvestigatorFlag) {
        boolean oldPrincipalInvestigatorFlag   = principalInvestigatorFlag ;
        this.principalInvestigatorFlag = newPrincipalInvestigatorFlag;
        propertySupport.firePropertyChange(PROP_PRINCIPAL_INVESTIGATOR_FLAG, 
                oldPrincipalInvestigatorFlag, principalInvestigatorFlag);
    }
    /**
     * Method used to get the investigatorUnits
     * @return investigatorUnits Vector
     */
    public java.util.Vector getInvestigatorUnits(){
        return investigatorUnits;
        
    }
    /**
     * Method used to set the InvestigatorUnits
     * @param investigatorUnits Vector
     */
    public void setInvestigatorUnits(java.util.Vector investigatorUnits){
        this.investigatorUnits = investigatorUnits;
    }
    /**
     * Method used to get the updateUser
     * @return updateUser String
     */
    public String getUpdateUser() {
        return updateUser;
    }
    /**
     * Method used to set the updateUser
     * @param updateUser String
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    /**
     * Method used to get the updateTimestamp
     * @return updateTimestamp Timestamp
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    /**
     * Method used to set the updateTimestamp
     * @param updateTimestamp Timestamp
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    /**
     * Method used to get the acType
     * @return acType String
     */
    public String getAcType(){
        return acType;
    }
    /**
     * Method used to set the acType
     * @param acType String
     */
    public void setAcType(String acType){
        this.acType = acType;
    }
    
    /**
     * Method used to get the affiliationTypeCode
     * @return affiliationTypeCode int
     */
    public int getAffiliationTypeCode(){
        return affiliationTypeCode;
    }
    /**
     * Method used to set the affiliationTypeCode
     * @param acType String
     */
    public void setAffiliationTypeCode(int newAffiliationTypeCode){
        this.affiliationTypeCode = affiliationTypeCode;
        
        int oldAffiliationTypeCode   = affiliationTypeCode ;
        this.affiliationTypeCode = newAffiliationTypeCode;
        propertySupport.firePropertyChange(AFFILIATION_TYPE_CODE, 
                oldAffiliationTypeCode, affiliationTypeCode);        
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
        strBffr.append("facultyFlag   =>"+facultyFlag);
        strBffr.append("affiliation Code =>"+affiliationTypeCode);
        strBffr.append("affiliation description =>"+affiliationTypeDescription);
        strBffr.append("principalInvestigatorFlag  =>"+principalInvestigatorFlag);
        strBffr.append("no of investigatorUnits =>"+investigatorUnits.size());
        strBffr.append("updateUser =>"+updateUser);
        strBffr.append("updateTimestamp =>"+updateTimestamp);
        strBffr.append("acType =>"+acType);
    
        return strBffr.toString();
    }
    //Coeus enhancement 32.2 - step 2 : start
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
     //Coeus enhancement 32.2 - step 2 : end

}
