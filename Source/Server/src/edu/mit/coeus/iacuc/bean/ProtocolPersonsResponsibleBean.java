/*
 * ProtocolPersonsResponsibleBean.java
 *
 * Created on December 22, 2010, 5:44 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved
 */
/* PMD check performed, and commented unused imports and variables on 09-MARCH-2011
 * by Md.Ehtesham Ansari
 */
package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.bean.IBaseDataBean;

/**
 *
 * @author ehtesham
 */
public class ProtocolPersonsResponsibleBean implements java.io.Serializable, IBaseDataBean, BaseBean{
    
    private String protocolNumber;
    private int sequenceNumber;
    private int studyGroupId;
    private String personId;
    private boolean nonEmployeeFlag;
    private String personName; 
    private boolean trained;
    private java.sql.Timestamp updateTimestamp;
    private String updateUser;
    private String acType;        
              
    /**
     * Getter method for protocol number
     * @return protocolNumber - String
     */
    public String getProtocolNumber() {
        return protocolNumber;
    }

    /**
     * Setter method for protocol number
     * @param protocoNumber - String
     */
    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }
    
    /**
     * Getter method for protocol sequence number
     * @return sequenceNumber - int
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Setter method for protocol sequence number
     * @param sequenceNumber - int
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * Getter method for study group id
     * @return studyGroupId - int
     */
    public int getStudyGroupId() {
        return studyGroupId;
    }

    /**
     * Setter method for study group id
     * param studyGroupId - int
     */
    public void setStudyGroupId(int studyGroupId) {
        this.studyGroupId = studyGroupId;
    }
 
    /**
     * Getter method for study group person responsible personId
     * @return personId - String
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * Setter method for study group person responsible personId
     * @param personId - String
     */
    public void setPersonId(String personId) {         
        this.personId = personId;       
    }
      
    /*
     * Method to set person name For the bean
     * @param personName - String
     */
    public String getPersonName() {
        return personName;
    }

    /**
     * Setter method for personName
     * @param personName 
     */
    public void setPersonName(String personName) {
        this.personName = personName;
    }
    
    /*
     * Method to set is trained For the bean
     * @param trained - boolean value
     */
    public boolean isTrained() {
        return trained;
    }

    /**
     * Setter method for is trained
     * @param trained 
     */
    public void setTrained(boolean trained) {
        this.trained = trained;
    }
    
    /**
     * Getter method for update time stamp 
     * @return updateTimestamp - java.sql.Timestamp
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    /**
     * Setter method for update timestamp
     * @param updateTimestamp 
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    /**
     * 
     * Getter method for update user id
     * @return updateUser
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * 
     * Setter method for update user id
     * @param updateUser 
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
   
    /*
     * Method to get the actype of the bean
     * @return acType - String
     */
    public String getAcType() {
        return acType;
    }
    
    /*
     * Method to set acType For the bean
     * @param acType - String
     */
     public void setAcType(String acType) {
        this.acType = acType;
    } 
     
    // Added for COEUSQA-3694 : Coeus4.5: IACUC Procedure Personnel Issue Remains - Start
    /**
     * Method get the boolean value for nonEmployeeFlag
     * @return nonEmployeeFlag
     */
    public boolean isNonEmployeeFlag() {
        return nonEmployeeFlag;
    }

    /**
     * Method set the boolean value for nonEmployeeFlag
     * @param nonEmployeeFlag 
     */
    public void setNonEmployeeFlag(boolean nonEmployeeFlag) {
        this.nonEmployeeFlag = nonEmployeeFlag;
    }
    // Added for COEUSQA-3694 : Coeus4.5: IACUC Procedure Personnel Issue Remains - End
        
}
