/*
 * @(#)ProtocolSpecialReviewFormBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.iacuc.bean;

import java.beans.*;
import java.sql.Date;
import edu.mit.coeus.bean.IBaseDataBean;
/**
 * The class used to hold the information of <code>Protocol Special Review</code>
 *
 * @author  
 * @version 1.0
 * Created on April 09, 2003, 12:06 PM
 */

public class ProtocolSpecialReviewFormBean implements java.io.Serializable, IBaseDataBean {
    
    // holds the fund source type code
    private static final String PROP_PROTOCOL_SPECIAL_REVIEW_TYPE_CODE = "specialReviewCode";
    // holds the fund source type code
    private static final String PROP_PROTOCOL_APPROVAL_TYPE_CODE = "approvalCode";
   
    //holds protocol number
    private  String protocolNumber;
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
    
    //COEUSQA-1724-Added for Expiration date -start
    private java.sql.Date expirationDate;
    //COEUSQA-1724-Added for Expiration date -end

    //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds Action type
    private String acType;
   
    
    private PropertyChangeSupport propertySupport;
    
    /** Creates new ProtocolSpecialReviewFormBean */
    public ProtocolSpecialReviewFormBean() {
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
     * @param newfundingSourceTypeCode integer
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
     * @param newfundingSourceTypeCode integer
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
     * @param newfundingSourceTypeDesc String
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
    
    //COEUSQA-1724-Added for Expiration date -start
    /**
     * Method used to get the expirationDate
     * @return expirationDate Date
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * Method used to set the expirationDate
     * @param expirationDate Date
     */
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
     //COEUSQA-1724-Added for Expiration date -end
    
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
    
}
