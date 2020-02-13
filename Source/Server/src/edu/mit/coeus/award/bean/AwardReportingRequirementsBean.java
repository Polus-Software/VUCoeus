/*
 * AwardReportingRequirementsBean.java
 *
 * Created on August 3, 2004, 10:59 AM
 */

package edu.mit.coeus.award.bean;
import java.sql.Date;
/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class AwardReportingRequirementsBean extends AwardReportReqBean {
	
	private Date baseDate;
        private String principleInvestigator;
	private String contact;
	private String title;
        private String unitNumber;
        private String unitName;
        private String sponsorCode;
        private String sponsorName;
        private String sponsorAwardNumber;
        private String reportClassDescription;
        private String frequencyBaseDate;
        private int ospDistributionCode;
        private int numberOfCopies;
        private Date stringDueDate;
        private Date stringActivityDate;


	
	/** Creates a new instance of AwardReportingRequirementsBean */
	public AwardReportingRequirementsBean() {
	}
	
	/**
	 * Getter for property baseDate.
	 * @return Value of property baseDate.
	 */
	public Date getBaseDate() {
		return baseDate;
	}
	
	/**
	 * Setter for property baseDate.
	 * @param baseDate New value of property baseDate.
	 */
	public void setBaseDate(Date baseDate) {
		this.baseDate = baseDate;
	}
	
	/**
	 * Getter for property contact.
	 * @return Value of property contact.
	 */
	public java.lang.String getContact() {
		return contact;
	}
	
	/**
	 * Setter for property contact.
	 * @param contact New value of property contact.
	 */
	public void setContact(java.lang.String contact) {
		this.contact = contact;
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
	 * Getter for property ospDistributionCode.
	 * @return Value of property ospDistributionCode.
	 */
	public int getOspDistributionCode() {
		return ospDistributionCode;
	}
	
	/**
	 * Setter for property ospDistributionCode.
	 * @param ospDistributionCode New value of property ospDistributionCode.
	 */
	public void setOspDistributionCode(int ospDistributionCode) {
		this.ospDistributionCode = ospDistributionCode;
	}
	
	/**
	 * Getter for property numberOfCopies.
	 * @return Value of property numberOfCopies.
	 */
	public int getNumberOfCopies() {
		return numberOfCopies;
	}
	
	/**
	 * Setter for property numberOfCopies.
	 * @param numberOfCopies New value of property numberOfCopies.
	 */
	public void setNumberOfCopies(int numberOfCopies) {
		this.numberOfCopies = numberOfCopies;
	}
	/** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        AwardReportingRequirementsBean awardReportReqBean = (AwardReportingRequirementsBean)obj;
		
		if(equalsMitAwardNumber(awardReportReqBean.getMitAwardNumber()) &&
		awardReportReqBean.getReportNumber() == getReportNumber()){
			return true;
		}else {
			return false;
		}
    }
	
	private boolean equalsMitAwardNumber(String mitAwardNumber) {
		if(mitAwardNumber == null && getMitAwardNumber() == null) {
			return true;
		}else if(mitAwardNumber == null && getMitAwardNumber() != null) {
			return false;
		}else if(mitAwardNumber != null && getMitAwardNumber() == null) {
			return false;
		}else {
			return mitAwardNumber.equals(getMitAwardNumber());
		}
	}
        
        /**
         * Getter for property principleInvestigator.
         * @return Value of property principleInvestigator.
         */
        public java.lang.String getPrincipleInvestigator() {
            return principleInvestigator;
        }
        
        /**
         * Setter for property principleInvestigator.
         * @param principleInvestigator New value of property principleInvestigator.
         */
        public void setPrincipleInvestigator(java.lang.String principleInvestigator) {
            this.principleInvestigator = principleInvestigator;
        }
        
        /**
         * Getter for property unitNumber.
         * @return Value of property unitNumber.
         */
        public java.lang.String getUnitNumber() {
            return unitNumber;
        }
        
        /**
         * Setter for property unitNumber.
         * @param unitNumber New value of property unitNumber.
         */
        public void setUnitNumber(java.lang.String unitNumber) {
            this.unitNumber = unitNumber;
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
         * Getter for property sponsorAwardNumber.
         * @return Value of property sponsorAwardNumber.
         */
        public java.lang.String getSponsorAwardNumber() {
            return sponsorAwardNumber;
        }
        
        /**
         * Setter for property sponsorAwardNumber.
         * @param sponsorAwardNumber New value of property sponsorAwardNumber.
         */
        public void setSponsorAwardNumber(java.lang.String sponsorAwardNumber) {
            this.sponsorAwardNumber = sponsorAwardNumber;
        }
        
        /**
         * Getter for property reportClassDescription.
         * @return Value of property reportClassDescription.
         */
        public java.lang.String getReportClassDescription() {
            return reportClassDescription;
        }
        
        /**
         * Setter for property reportClassDescription.
         * @param reportClassDescription New value of property reportClassDescription.
         */
        public void setReportClassDescription(java.lang.String reportClassDescription) {
            this.reportClassDescription = reportClassDescription;
        }

        /**
         * Getter for property frequencyBaseDate.
         * @return Value of property frequencyBaseDate.
         */
        public java.lang.String getFrequencyBaseDate() {
            return frequencyBaseDate;
        }
        
        /**
         * Setter for property frequencyBaseDate.
         * @param frequencyBaseDate New value of property frequencyBaseDate.
         */
        public void setFrequencyBaseDate(java.lang.String frequencyBaseDate) {
            this.frequencyBaseDate = frequencyBaseDate;
        }
        
       
        
}
