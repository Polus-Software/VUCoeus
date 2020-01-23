/*
 * NegotiationHeaderBean.java
 *
 * Created on July 15, 2004, 10:24 AM
 */

package edu.mit.coeus.negotiation.bean;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class NegotiationHeaderBean extends NegotiationBaseBean {
	
	private String proposalNumber;
	
	private int proposalTypeCode;
	
	private String title;
	
	private String sponsorCode;
	
	private String initialContractAdmin;
	
	private String piID;
	
	private String leadUnit;
	
	private String piName;
	
	private String sponsorName;
	
	private String unitName;
	
	private String proposalTypeDescription;
        
        //COEUSDEV - 733 Create a new notification for negotiation module - Start
        private String userId;
        //COEUSDEV - 733 Create a new notification for negotiation module - End
        
        //case 3961 start
        private String primeSponsorCode;
        private String primeSponsorName;
        //case 3961 end 
        
        //COEUSQA-119 : View negotiations in lite - Start
        private String lastUpdateUser = null;
        private java.sql.Timestamp lastUpdateTimestamp = null;
        //COEUSQA-119 : end

	/** Creates a new instance of NegotiationHeaderBean */
	public NegotiationHeaderBean() {
	}
	
	/**
	 * Getter for property proposalNumber.
	 * @return Value of property proposalNumber.
	 */
	public java.lang.String getProposalNumber() {
		return proposalNumber;
	}
	
	/**
	 * Setter for property proposalNumber.
	 * @param proposalNumber New value of property proposalNumber.
	 */
	public void setProposalNumber(java.lang.String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}
	
	/**
	 * Getter for property proposalTypeCode.
	 * @return Value of property proposalTypeCode.
	 */
	public int getProposalTypeCode() {
		return proposalTypeCode;
	}
	
	/**
	 * Setter for property proposalTypeCode.
	 * @param proposalTypeCode New value of property proposalTypeCode.
	 */
	public void setProposalTypeCode(int proposalTypeCode) {
		this.proposalTypeCode = proposalTypeCode;
	}
	
	/**
	 * Getter for property title.
	 * @return Value of property title.
	 */
	public java.lang.String getTitle() {
		return title;
	}
	
	/**
	 * Setter for property title.
	 * @param title New value of property title.
	 */
	public void setTitle(java.lang.String title) {
		this.title = title;
	}
	
	/**
	 * Getter for property sponsorCode.
	 * @return Value of property sponsorCode.
	 */
	public java.lang.String getSponsorCode() {
		return sponsorCode;
	}
	
	/**
	 * Setter for property sponsorCode.
	 * @param sponsorCode New value of property sponsorCode.
	 */
	public void setSponsorCode(java.lang.String sponsorCode) {
		this.sponsorCode = sponsorCode;
	}
	
	/**
	 * Getter for property initialContractAdmin.
	 * @return Value of property initialContractAdmin.
	 */
	public java.lang.String getInitialContractAdmin() {
		return initialContractAdmin;
	}
	
	/**
	 * Setter for property initialContractAdmin.
	 * @param initialContractAdmin New value of property initialContractAdmin.
	 */
	public void setInitialContractAdmin(java.lang.String initialContractAdmin) {
		this.initialContractAdmin = initialContractAdmin;
	}
	
	/**
	 * Getter for property piID.
	 * @return Value of property piID.
	 */
	public java.lang.String getPiID() {
		return piID;
	}
	
	/**
	 * Setter for property piID.
	 * @param piID New value of property piID.
	 */
	public void setPiID(java.lang.String piID) {
		this.piID = piID;
	}
	
	/**
	 * Getter for property leadUnit.
	 * @return Value of property leadUnit.
	 */
	public java.lang.String getLeadUnit() {
		return leadUnit;
	}
	
	/**
	 * Setter for property leadUnit.
	 * @param leadUnit New value of property leadUnit.
	 */
	public void setLeadUnit(java.lang.String leadUnit) {
		this.leadUnit = leadUnit;
	}
	
	/**
	 * Getter for property piName.
	 * @return Value of property piName.
	 */
	public java.lang.String getPiName() {
		return piName;
	}
	
	/**
	 * Setter for property piName.
	 * @param piName New value of property piName.
	 */
	public void setPiName(java.lang.String piName) {
		this.piName = piName;
	}
        
        /** Getter for property lastUpdateUser.
         * @return Value of property lastUpdateUser.
         */
        public java.lang.String getLastUpdateUser() {
            return lastUpdateUser;
        }
        
        /** Setter for property lastUpdateUser.
         * @param lastUpdateUser New value of property lastUpdateUser.
         */
        public void setLastUpdateUser(java.lang.String lastUpdateUser) {
            this.lastUpdateUser = lastUpdateUser;
        }
        
        /** Getter for property lastUpdateTimestamp.
         * @return Value of property lastUpdateTimestamp.
         */
        public java.sql.Timestamp getLastUpdateTimestamp() {
            return lastUpdateTimestamp;
        }
        
        /** Setter for property lastUpdateTimestamp.
         * @param lastUpdateTimestamp New value of property lastUpdateTimestamp.
         */
        public void setLastUpdateTimestamp(java.sql.Timestamp lastUpdateTimestamp) {
            this.lastUpdateTimestamp = lastUpdateTimestamp;
        }


        
	
	/** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        NegotiationHeaderBean negotiationHeaderBean = (NegotiationHeaderBean)obj;
        if (negotiationHeaderBean.getNegotiationNumber().equals(getNegotiationNumber())) {
            return true;
        } else {
            return false;
        }
    }
	
	/**
	 * Getter for property sponsorName.
	 * @return Value of property sponsorName.
	 */
	public java.lang.String getSponsorName() {
		return sponsorName;
	}
	
	/**
	 * Setter for property sponsorName.
	 * @param sponsorName New value of property sponsorName.
	 */
	public void setSponsorName(java.lang.String sponsorName) {
		this.sponsorName = sponsorName;
	}
	
	/**
	 * Getter for property unitName.
	 * @return Value of property unitName.
	 */
	public java.lang.String getUnitName() {
		return unitName;
	}
	
	/**
	 * Setter for property unitName.
	 * @param unitName New value of property unitName.
	 */
	public void setUnitName(java.lang.String unitName) {
		this.unitName = unitName;
	}
	
	/**
	 * Getter for property proposalTypeDescription.
	 * @return Value of property proposalTypeDescription.
	 */
	public java.lang.String getProposalTypeDescription() {
		return proposalTypeDescription;
	}
	
	/**
	 * Setter for property proposalTypeDescription.
	 * @param proposalTypeDescription New value of property proposalTypeDescription.
	 */
	public void setProposalTypeDescription(java.lang.String proposalTypeDescription) {
		this.proposalTypeDescription = proposalTypeDescription;
	}
	
        //case 3961 start
        public java.lang.String getPrimeSponsorCode() {
		return primeSponsorCode;
	}
	
	/**
	 * Setter for property sponsorCode.
	 * @param sponsorCode New value of property sponsorCode.
	 */
	public void setPrimeSponsorCode(java.lang.String primeSponsorCode) {
		this.primeSponsorCode = primeSponsorCode;
	}
        
        public java.lang.String getPrimeSponsorName() {
		return primeSponsorName;
	}
	
	/**
	 * Setter for property sponsorName.
	 * @param sponsorName New value of property sponsorName.
	 */
	public void setPrimeSponsorName(java.lang.String primeSponsorName) {
		this.primeSponsorName = primeSponsorName;
	}
        //case 3961 end
        
        //COEUSDEV - 733 Create a new notification for negotiation module - Start
        /**
	 * Getter for property userId.
	 * @return Value of property userId.
	 */
	public java.lang.String getUserId() {
		return userId;
	}
	
	/**
	 * Setter for property userId.
	 * @param userId New value of property userId.
	 */
	public void setUserId(java.lang.String userId) {
		this.userId = userId;
	}
        //COEUSDEV - 733 Create a new notification for negotiation module - End
}
