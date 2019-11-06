/*
 * @(#)ProtocolSubmissionInfoBean.java 1.0 10/24/02 12:27 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


package edu.mit.coeus.irb.bean;

import java.beans.*;
import edu.mit.coeus.bean.IBaseDataBean; 
/**
 * The class used to hold the information of <code>Protocol VulnerableSubject</code>
 *
 * @author  phani
 * @version 1.0
 * Created on October 24, 2002, 12:27 PM
 */

public class ProtocolVulnerableSubListsBean  implements java.io.Serializable, IBaseDataBean {
    
    private static final String PROP_SUBJECT_CODE = "subjectCount";
     // holds the protocol number
    private String protocolNumber;
     // holds the sequence number
    private int sequenceNumber;
    // holds the vulnerable subject type code
    private int vulnerableSubjectTypeCode;
    // holds the vulnerable subject type description
    private String vulnerableSubjectTypeDesc;
    
    //Protocol Enhancement - Saving null in db Start 1
    // holds the subject count.
    //private int subjectCount;
    private Integer subjectCount;
    //Protocol Enhancement - Saving null in db End 1
     //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds Action type
    private String acType;
   
    
    private PropertyChangeSupport propertySupport;
    
    /** Creates new ProtocolVulnerableSubListsBean */
    public ProtocolVulnerableSubListsBean() {
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
     * Method used to get the vulnerable subject type code
     * @return vulnerableSubjectTypeCode integer
     */
    public int getVulnerableSubjectTypeCode() {
        return vulnerableSubjectTypeCode;
    }

    /**
     * Method used to set the vulnerable subject type code
     * @param vulnerableSubjectTypeCode integer
     */
    public void setVulnerableSubjectTypeCode(int vulnerableSubjectTypeCode) {
        this.vulnerableSubjectTypeCode = vulnerableSubjectTypeCode;
    }

    /**
     * Method used to get the vulnerable subject type description
     * @return vulnerableSubjectTypeDesc String
     */
    public String getVulnerableSubjectTypeDesc() {
        return vulnerableSubjectTypeDesc;
    }

    /**
     * Method used to set the vulnerable subject type description
     * @param vulnerableSubjectTypeDesc String
     */
    public void setVulnerableSubjectTypeDesc(String vulnerableSubjectTypeDesc) {
        this.vulnerableSubjectTypeDesc = vulnerableSubjectTypeDesc;
    }

    //Protocol Enhancement - Saving null in db Start 2
    /**
     * Method used to get the subject count
     * @return subjectCount integer
     */
    /*public int getSubjectCount() {
        return subjectCount;
    }/*

    /**
     * Method used to set the subject count
     * @param subjectCount integer
     */
    /*public void setSubjectCount(int newSubjectCode) {
        int oldSubjectCode = subjectCount;
        this.subjectCount = newSubjectCode;
        propertySupport.firePropertyChange(PROP_SUBJECT_CODE, 
                oldSubjectCode, subjectCount);   
    }*/
    
    
     /**
      * Getter for property subjectCount.
      * @return Value of property subjectCount.
      */
     public java.lang.Integer getSubjectCount() {
         return subjectCount;
     }
     
     /**
      * Setter for property subjectCount.
      * @param subjectCount New value of property subjectCount.
      */
     public void setSubjectCount(java.lang.Integer newSubjectCode) {
         Integer oldSubjectCode = subjectCount;
         this.subjectCount = newSubjectCode;
         propertySupport.firePropertyChange(PROP_SUBJECT_CODE, 
                oldSubjectCode, subjectCount);
     }
     
     
    
    //Protocol Enhancement - Saving null in db End 2
     
    /**
     * Method used to get the update user id
     * @return updateUser String
     */
    public String getUpdateUser(){
        return updateUser;
    }
    
    /**
     * Method used to set the update user id
     * @param updateUser String
     */
    public void setUpdateUser(String updateUser){
        this.updateUser = updateUser;
    }
    
    /**
     * Method used to get the update timestamp
     * @return updateTimestamp Timestamp
     */
    public java.sql.Timestamp getUpdateTimestamp(){
        return updateTimestamp;
    }
    
    /**
     * Method used to set the update timestamp
     * @param updateTimestamp Timestamp
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp){
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
     *  Method used to get the Action Type
     *  @param acType Action Type
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
        strBffr.append("vulnerableSubjectTypeCode  =>"+ vulnerableSubjectTypeCode);
        strBffr.append("vulnerableSubjectTypeDesc =>"+vulnerableSubjectTypeDesc);
        strBffr.append("updateUser =>"+updateUser);
        strBffr.append("updateTimestamp =>"+updateTimestamp);
        strBffr.append("acType =>"+acType);
    
        return strBffr.toString();
    }    

    
}
