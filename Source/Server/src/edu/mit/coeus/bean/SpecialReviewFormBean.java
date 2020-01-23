/*
 * @(#)SpecialReviewFormBean.java 1.0 05/27/03 5:56 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 30-MAY-2011
 * by Bharati
 */

package edu.mit.coeus.bean;

import java.beans.*;
//import java.sql.Timestamp;
import java.sql.Date;
//import edu.mit.coeus.bean.BaseBean;
/** The class is used to hold the information of Special Review Forms
 *
 * @author Senthil Nathan
 * @version 1.0
 * Created on May 19, 2003, 11:45 AM
 */

public class SpecialReviewFormBean implements java.io.Serializable, CoeusBean {

    // holds the fund source type code
    private static final String PROP_PROTOCOL_SPECIAL_REVIEW_TYPE_CODE = "specialReviewCode";
    // holds the fund source type code
    private static final String PROP_PROTOCOL_APPROVAL_TYPE_CODE = "approvalCode";
   
    //holds protocol number
    //private  String protocolNumber;
    
    //holds sequence number
    private int sequenceNumber;
    //holds Special review number
    private int specialReviewNumber;

    private int specialReviewCode;
    
    private String specialReviewDescription;

    private int approvalCode;
    
    private String approvalDescription;

    private String spRevProtocolNumber;
    
    private String comments;
    
    private java.sql.Date applicationDate;
    
    private java.sql.Date approvalDate;

    //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds Action type
    private String acType;
   
    private PropertyChangeSupport propertySupport;
    
    //holds row Id
    private int rowId;
    
    //Added for COEUSQA-2984 : Statuses in special review - start
    private int protocolStatusCode;
    private String protocolDescription;
    //Added for COEUSQA-2984 : Statuses in special review - end

    //For the Coeus Enhancement case:#1799 start step:1
    private String prevSpRevProtocolNumber;
    private int protoSequenceNumber;
    private int aw_ApprovalCode;
    private int aw_SpecialReviewCode;
    //End Coeus Enhancement case:#1799 step:1
    
    /** Creates new ProtocolSpecialReviewFormBean */
    public SpecialReviewFormBean() {
        propertySupport = new PropertyChangeSupport( this );
    }
    /*public SpecialReviewFormBean(SpecialReviewFormBean specialReviewFormBean) {
        propertySupport = new PropertyChangeSupport( this );
        
        setSequenceNumber(specialReviewFormBean.getSequenceNumber());
        setSpecialReviewNumber(specialReviewFormBean.getSpecialReviewNumber());
        setSpecialReviewCode(specialReviewFormBean.getSpecialReviewCode());
        setSpecialReviewDescription(specialReviewFormBean.getSpecialReviewDescription());
        setApprovalCode(specialReviewFormBean.getApprovalCode());
        setApprovalDescription(specialReviewFormBean.getApprovalDescription());
        setProtocolSPRevNumber(specialReviewFormBean.getProtocolSPRevNumber());
        setComments(specialReviewFormBean.getComments());
        setApplicationDate(specialReviewFormBean.getApplicationDate());
        setApprovalDate(specialReviewFormBean.getApprovalDate());
        setUpdateUser(specialReviewFormBean.getUpdateUser());
        setUpdateTimestamp(specialReviewFormBean.getUpdateTimestamp());
        setAcType(specialReviewFormBean.getAcType());
    }*/    
    
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
    /*public String getProtocolNumber() {
        return protocolNumber;
    }*/

    /**
     * Method used to set the protocol number
     * @param protocolNumber String
     */
    /*public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }*/

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
     * Method used to get the special review number
     * @return specialReviewNumber int
     */
    public int getSpecialReviewNumber() {
        return specialReviewNumber;
    }

    /**
     * Method used to set the specialReviewNumber 
     * @param specialReviewNumber int
     */
    public void setSpecialReviewNumber(int specialReviewNumber) {
        this.specialReviewNumber = specialReviewNumber;
    }

    /**
     * Method used to get the specialReviewCode
     * @return specialReviewCode int
     */
    public int getSpecialReviewCode() {
        return specialReviewCode;
    }

    /**
     * Method used to set the fund source Type code 
     * @param newSpecialReviewCode integer
     */
    public void setSpecialReviewCode(int newSpecialReviewCode) {
        int oldSpecialReviewCode = specialReviewCode;
        this.specialReviewCode = newSpecialReviewCode;
         propertySupport.firePropertyChange(PROP_PROTOCOL_SPECIAL_REVIEW_TYPE_CODE, 
                oldSpecialReviewCode, specialReviewCode);
    }
    
    /**
     * Method used to get the approvalCode
     * @return approvalCode int
     */
    public int getApprovalCode() {
        return approvalCode;
    }

    /**
     * Method used to set the fund source Type code 
     * @param newApprovalCode integer
     */
    public void setApprovalCode(int newApprovalCode) {
        int oldApprovalCode = approvalCode;
        this.approvalCode = newApprovalCode;
         propertySupport.firePropertyChange(PROP_PROTOCOL_APPROVAL_TYPE_CODE, 
                oldApprovalCode, approvalCode);
    }

    /**
     * Method used to get the spRevProtocolNumber 
     * @return spRevProtocolNumber String
     */
    public String getProtocolSPRevNumber() {
        return spRevProtocolNumber;
    }

    /**
     * Method used to set the fund source Type description
     * @param protocolSPRevNumber String
     */
    public void setProtocolSPRevNumber(String protocolSPRevNumber) {
        this.spRevProtocolNumber = protocolSPRevNumber;
    }

    /**
     * Method used to get the applicationDate
     * @return applicationDate Date
     */
    public Date getApplicationDate() {
        return applicationDate;
    }

    /**
     * Method used to set the applicationDate
     * @param applicationDate Date
     */
    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }
    
    /**
     * Method used to get the applicationDate
     * @return applicationDate Date
     */
    public Date getApprovalDate() {
        return approvalDate;
    }

    /**
     * Method used to set the approvalDate
     * @param approvalDate Date
     */
    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
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
    
    /** Getter for property specialReviewDescription.
     * @return Value of property specialReviewDescription.
     */
    public java.lang.String getSpecialReviewDescription() {
        return specialReviewDescription;
    }
    
    /** Setter for property specialReviewDescription.
     * @param specialReviewDescription New value of property specialReviewDescription.
     */
    public void setSpecialReviewDescription(java.lang.String specialReviewDescription) {
        this.specialReviewDescription = specialReviewDescription;
    }
    
    /** Getter for property approvalDescription.
     * @return Value of property approvalDescription.
     */
    public java.lang.String getApprovalDescription() {
        return approvalDescription;
    }    
    
    /** Setter for property approvalDescription.
     * @param approvalDescription New value of property approvalDescription.
     */
    public void setApprovalDescription(java.lang.String approvalDescription) {
        this.approvalDescription = approvalDescription;
    }        
    
    public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) throws edu.mit.coeus.exception.CoeusException {
        return true;
    }  
    
    /** Getter for property rowId.
     * @return Value of property rowId.
     *
     */
    public int getRowId() {
        return rowId;
    }
    
    /** Setter for property rowId.
     * @param rowId New value of property rowId.
     *
     */
    public void setRowId(int rowId) {
        this.rowId = rowId;
    }
    
    //For the Coeus Enhancement case:#1799 start step:2
    /**
     * Getter for property prevSpRevProtocolNumber.
     * @return Value of property prevSpRevProtocolNumber.
     */
    public java.lang.String getPrevSpRevProtocolNumber() {
        return prevSpRevProtocolNumber;
    }
    
    /**
     * Setter for property prevSpRevProtocolNumber.
     * @param prevSpRevProtocolNumber New value of property prevSpRevProtocolNumber.
     */
    public void setPrevSpRevProtocolNumber(java.lang.String prevSpRevProtocolNumber) {
        this.prevSpRevProtocolNumber = prevSpRevProtocolNumber;
    }
        
    /**
     * Getter for property protoSequenceNumber.
     * @return Value of property protoSequenceNumber.
     */
    public int getProtoSequenceNumber() {
        return protoSequenceNumber;
    }
    
    /**
     * Setter for property protoSequenceNumber.
     * @param protoSequenceNumber New value of property protoSequenceNumber.
     */
    public void setProtoSequenceNumber(int protoSequenceNumber) {
        this.protoSequenceNumber = protoSequenceNumber;
    }
    
    /**
     * Getter for property aw_ApprovalCode.
     * @return Value of property aw_ApprovalCode.
     */
    public int getAw_ApprovalCode() {
        return aw_ApprovalCode;
    }
    
    /**
     * Setter for property aw_ApprovalCode.
     * @param aw_ApprovalCode New value of property aw_ApprovalCode.
     */
    public void setAw_ApprovalCode(int aw_ApprovalCode) {
        this.aw_ApprovalCode = aw_ApprovalCode;
    }
    
    /**
     * Getter for property aw_SpecialReviewCode.
     * @return Value of property aw_SpecialReviewCode.
     */
    public int getAw_SpecialReviewCode() {
        return aw_SpecialReviewCode;
    }
    
    /**
     * Setter for property aw_SpecialReviewCode.
     * @param aw_SpecialReviewCode New value of property aw_SpecialReviewCode.
     */
    public void setAw_SpecialReviewCode(int aw_SpecialReviewCode) {
        this.aw_SpecialReviewCode = aw_SpecialReviewCode;
    }
    
     //End Coeus Enhancement case:#1799 step:
    
    //COEUSQA-2984 : Statuses in special review - start
    /**
     * Method used to get the protocol Status Code
     * @return protocolStatusCode int
     */
    public int getProtocolStatusCode() {
        return protocolStatusCode;
    }
    
    /**
     * Method used to set the protocol Status Code
     * @param protocolStatusCode int
     */
    public void setProtocolStatusCode(int protocolStatusCode) {
        this.protocolStatusCode = protocolStatusCode;
    }
    
    /**
     * Method used to get the protocol Status Description
     * @return protocolDescription String
     */
    public String getProtocolStatusDescription() {
        return protocolDescription;
    }
    
    /**
     * Method used to set the protocol Status Description
     * @param protocolDescription String
     */
    public void setProtocolStatusDescription(String protocolDescription) {
        this.protocolDescription = protocolDescription;
    }
    //COEUSQA-2984 : Statuses in special review - end
}