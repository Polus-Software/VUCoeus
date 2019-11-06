/*
 * NegotiationInfoBean.java
 *
 * Created on July 13, 2004, 6:23 PM
 */

package edu.mit.coeus.negotiation.bean;

import java.sql.Date;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class NegotiationInfoBean extends NegotiationBaseBean {
	
	private String negotiatorId;
	private String negotiatorName;
	private String docFileAddress;
	private Date startDate;
	private int statusCode;
	private String statusDescription;
	//case 3590 start
        private int negotiationAgreeTypeCode;
        private String negotiationAgreeTypeDescription;
        private String primeSponsorCode;
        private String primeSponsorName;
        private Date proposedStartDate;
        //COEUSDEV - 733 Create a new notification for negotiation module - Start
        private int awStatusCode;
        //COEUSDEV - 733 Create a new notification for negotiation module - End
        //case 3590 end
        //Added for case 4185 - New field closed date
        private Date closedDate;
	/** Creates a new instance of NegotiationInfoBean */
	public NegotiationInfoBean() {
	}
	
	/** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        NegotiationInfoBean negotiationInfoBean = (NegotiationInfoBean)obj;
        if (negotiationInfoBean.getNegotiationNumber().equals(getNegotiationNumber())) {
            return true;
        } else {
            return false;
        }
    }
	
	/**
	 * Getter for property negotiatorName.
	 * @return Value of property negotiatorName.
	 */
	public java.lang.String getNegotiatorName() {
		return negotiatorName;
	}
	
	/**
	 * Setter for property negotiatorName.
	 * @param negotiatorName New value of property negotiatorName.
	 */
	public void setNegotiatorName(java.lang.String negotiatorName) {
		this.negotiatorName = negotiatorName;
	}
	
	/**
	 * Getter for property negotiatorId.
	 * @return Value of property negotiatorId.
	 */
	public java.lang.String getNegotiatorId() {
		return negotiatorId;
	}
	
	/**
	 * Setter for property negotiatorId.
	 * @param negotiatorId New value of property negotiatorId.
	 */
	public void setNegotiatorId(java.lang.String negotiatorId) {
		this.negotiatorId = negotiatorId;
	}
	
	/**
	 * Getter for property docFileAddress.
	 * @return Value of property docFileAddress.
	 */
	public java.lang.String getDocFileAddress() {
		return docFileAddress;
	}
	
	/**
	 * Setter for property docFileAddress.
	 * @param docFileAddress New value of property docFileAddress.
	 */
	public void setDocFileAddress(java.lang.String docFileAddress) {
		this.docFileAddress = docFileAddress;
	}
	
	/**
	 * Getter for property startDate.
	 * @return Value of property startDate.
	 */
	public java.sql.Date getStartDate() {
		return startDate;
	}
	
	/**
	 * Setter for property startDate.
	 * @param startDate New value of property startDate.
	 */
	public void setStartDate(java.sql.Date startDate) {
		this.startDate = startDate;
	}
	
	/**
	 * Getter for property statusCode.
	 * @return Value of property statusCode.
	 */
	public int getStatusCode() {
		return statusCode;
	}
	
	/**
	 * Setter for property statusCode.
	 * @param statusCode New value of property statusCode.
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	/**
	 * Getter for property statusDescription.
	 * @return Value of property statusDescription.
	 */
	public java.lang.String getStatusDescription() {
		return statusDescription;
	}
	
	/**
	 * Setter for property statusDescription.
	 * @param statusDescription New value of property statusDescription.
	 */
	public void setStatusDescription(java.lang.String statusDescription) {
		this.statusDescription = statusDescription;
	}
	//case 3590 start
        public int getNegotiationAgreeTypeCode() {
		return negotiationAgreeTypeCode;
	}	
	public void setNegotiationAgreeTypeCode(int negotiationAgreeTypeCode) {
		this.negotiationAgreeTypeCode = negotiationAgreeTypeCode;
	}
                       
        public java.lang.String getNegotiationAgreeTypeDescription() {
		return negotiationAgreeTypeDescription;
	}	
	public void setNegotiationAgreeTypeDescription(java.lang.String negotiationAgreeTypeDescription) {
		this.negotiationAgreeTypeDescription = negotiationAgreeTypeDescription;
	}
        
        public java.lang.String getPrimeSponsorCode() {
		return primeSponsorCode;
	}
        public void setPrimeSponsorCode(java.lang.String primeSponsorCode) {
		this.primeSponsorCode = primeSponsorCode;
	}
        
        public java.lang.String getPrimeSponsorName() {
		return primeSponsorName;
	}
        public void setPrimeSponsorName(java.lang.String primeSponsorName) {
		this.primeSponsorName = primeSponsorName;
	}
      
        public java.sql.Date getProposedStartDate() {
		return proposedStartDate;
	}	
	public void setProposedStartDate(java.sql.Date proposedStartDate) {
		this.proposedStartDate = proposedStartDate;
	}
        //case 3590 end
        //Case 4185 - New field Closed Date - Start
        public Date getClosedDate() {
            return closedDate;
        }
        
        public void setClosedDate(Date closedDate) {
            this.closedDate = closedDate;
        }
        //Case 4185 - New field Closed Date - End
        
        //COEUSDEV - 733 Create a new notification for negotiation module - Start
        /**
	 * Getter for property awStatusCode.
	 * @return Value of property awStatusCode.
	 */
	public int getAwStatusCode() {
		return awStatusCode;
	}
	
	/**
	 * Setter for property awStatusCode.
	 * @param awStatusCode New value of property awStatusCode.
	 */
	public void setAwStatusCode(int awStatusCode) {
		this.awStatusCode = awStatusCode;
	}
        //COEUSDEV - 733 Create a new notification for negotiation module - End
        
}
