/*
 * @(#)ProtocolFundingSourceBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.iacuc.bean.*;
import java.beans.*;
import edu.mit.coeus.bean.IBaseDataBean;
/**
 * The class used to hold the information of <code>Protocol FundSource</code>
 *
 * @author  phani
 * @version 1.0
 * Created on October 24, 2002, 12:06 PM
 */

public class ProtocolFundingSourceBean implements java.io.Serializable, IBaseDataBean {
    // holds the fund source 
    private static final String PROP_PROTOCOL_FUNDING_SOURCE = "fundingSource";
    // holds the fund source type description
    private static final String PROP_PROTOCOL_FUNDING_SOURCE_DESC = "fundingSourceTypeDesc";
    // holds the fund source name
    private static final String PROP_PROTOCOL_FUNDING_SOURCE_NAME = "fundingSourceName";
    // holds the fund source type code
    private static final String PROP_PROTOCOL_FUNDING_SOURCE_TYPE_CODE = "fundingSourceTypeCode";
   
    //holds protocol number
    private  String protocolNumber;
    //holds sequence number
    private int sequenceNumber;
    //holds fund source type code
    private int fundingSourceTypeCode;
    
    private int awFundingSourceTypeCode;
    
    //holds fund source type description
    private String fundingSourceTypeDesc;
    //holds fundSource 
    private String fundingSource;
    //holds fundSource name
    private String fundingSourceName;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds Action type
    private String acType;
    
      
    private PropertyChangeSupport propertySupport;
    
    /** Creates new ProtocolFundingSourceBean */
    public ProtocolFundingSourceBean() {
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
     * Method used to get the fund source Type code
     * @return fundingSourceTypeCode int
     */
    public int getFundingSourceTypeCode() {
        return fundingSourceTypeCode;
    }

    /**
     * Method used to set the fund source Type code 
     * @param newfundingSourceTypeCode integer
     */
    public void setFundingSourceTypeCode(int newfundingSourceTypeCode) {
        int oldfundingSourceTypeCode = fundingSourceTypeCode;
        this.fundingSourceTypeCode = newfundingSourceTypeCode;
         propertySupport.firePropertyChange(PROP_PROTOCOL_FUNDING_SOURCE_TYPE_CODE, 
                oldfundingSourceTypeCode, fundingSourceTypeCode);
    }

    /**
     * Method used to get the fund source Type description
     * @return fundingSourceTypeDesc String
     */
    public String getFundingSourceTypeDesc() {
        return fundingSourceTypeDesc;
    }

    /**
     * Method used to set the fund source Type description
     * @param newfundingSourceTypeDesc String
     */
    public void setFundingSourceTypeDesc(String newfundingSourceTypeDesc) {
        String oldfundingSourceTypeDesc = fundingSourceTypeDesc;
        this.fundingSourceTypeDesc = newfundingSourceTypeDesc;
        propertySupport.firePropertyChange(PROP_PROTOCOL_FUNDING_SOURCE_DESC, 
                oldfundingSourceTypeDesc, fundingSourceTypeDesc);
       
    }

    /**
     * Method used to get the funding source
     * @return fundingSource String
     */
    public String getFundingSource() {
        return fundingSource;
    }

    /**
     * Method used to set the funding source
     * @param newFundingSource String
     */
    public void setFundingSource(String newFundingSource) {
        String oldFundingSource = fundingSource;
        this.fundingSource = newFundingSource;
        propertySupport.firePropertyChange(PROP_PROTOCOL_FUNDING_SOURCE, 
                oldFundingSource, fundingSource);
        
    }
    
    /**
     * Method used to get the fundingSource name
     * @return fundingSourceName String
     */
    public String getFundingSourceName() {
        return fundingSourceName;
    }

    /**
     * Method used to set the fundingSource name
     * @param newfundingSourceName String
     */
    public void setFundingSourceName(String newfundingSourceName) {
        String oldFundingSourceName = fundingSourceName;
        this.fundingSourceName = newfundingSourceName;
        propertySupport.firePropertyChange(PROP_PROTOCOL_FUNDING_SOURCE_NAME, 
                oldFundingSourceName, fundingSourceName);
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
    
    /** Getter for property awFundingSourceTypeCode.
     * @return Value of property awFundingSourceTypeCode.
     */
    public int getAwFundingSourceTypeCode() {
        return awFundingSourceTypeCode;
    }    

    /** Setter for property awFundingSourceTypeCode.
     * @param awFundingSourceTypeCode New value of property awFundingSourceTypeCode.
     */
    public void setAwFundingSourceTypeCode(int awFundingSourceTypeCode) {
        this.awFundingSourceTypeCode = awFundingSourceTypeCode;
    }
    
     
    
}
