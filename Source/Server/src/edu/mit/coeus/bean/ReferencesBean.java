/*
 * @(#)ProtocolReferencesBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.bean;

import java.beans.*;
import java.sql.Date;

/**
 * The class used to hold the information of <code>References</code>
 *
 * @author
 * @version 1.0
 * Created on July 14, 2003, 10:26 PM
 */

public class ReferencesBean implements java.io.Serializable {

    // holds the fund reference type code
    private static final String PROTOCOL_REFERENCE_TYPE_CODE = "referenceTypeCode";

    //holds Reference number
    private int referenceNumber;

    private int referenceTypeCode;

    private String referenceTypeDescription;

    private String referenceKey;

    private java.sql.Date applicationDate;

    private java.sql.Date approvalDate;
    
    //COEUSQA-1724-Added for New Expiration Date column-start
    private java.sql.Date expirationDate;
    //COEUSQA-1724-Added for New Expiration Date column-end
    
    private String comments;

    //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds Action type
    private String acType;

    private PropertyChangeSupport propertySupport;

    /** Creates new ReferencesBean */
    public ReferencesBean() {
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
     * Method used to get the reference number
     * @return referenceNumber int
     */
    public int getReferenceNumber() {
        return referenceNumber;
    }

    /**
     * Method used to set the reference number
     * @param referenceNumber int
     */
    public void setReferenceNumber(int referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    /**
     * Method used to get the referenceTypeCode
     * @return referenceTypeCode int
     */
    public int getReferenceTypeCode() {
        return referenceTypeCode;
    }

    /**
     * Method used to set the Reference Type Code
     * @param newReferenceTypeCode integer
     */
    public void setReferenceTypeCode(int newReferenceTypeCode) {
        int oldReferenceTypeCode = referenceTypeCode;
        this.referenceTypeCode = newReferenceTypeCode;
         propertySupport.firePropertyChange(PROTOCOL_REFERENCE_TYPE_CODE,
                oldReferenceTypeCode, referenceTypeCode);
    }

    /**
     * Method used to get the referenceKey
     * @return referenceKey String
     */
    public String getReferenceKey() {
        return referenceKey;
    }

    /**
     * Method used to set the referenceKey
     * @param referenceKey String
     */
    public void setReferenceKey(String referenceKey) {
        this.referenceKey = referenceKey;
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
     * Method used to get the approvalDate
     * @return approvalDate Date
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
    
    //COEUSQA-1724-Added for New Expiration Date column-start
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
    //COEUSQA-1724-Added for New Expiration Date column-end
    
    /**
     * Method used to get Comments
     * @return comments String
     */
    public String getComments() {
        return comments;
    }

    /**
     * Method used to set Comments
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

    /** Getter for property referenceTypeDescription.
     * @return Value of property referenceTypeDescription.
     */
    public java.lang.String getReferenceTypeDescription() {
        return referenceTypeDescription;
    }

    /** Setter for property referenceTypeDescription.
     * @param referenceTypeDescription New value of property referenceTypeDescription.
     */
    public void setReferenceTypeDescription(java.lang.String referenceTypeDescription) {
        this.referenceTypeDescription = referenceTypeDescription;
    }
}
