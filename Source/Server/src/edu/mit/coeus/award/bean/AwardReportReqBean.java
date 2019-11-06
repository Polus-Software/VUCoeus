/**
 * @(#)AwardReportTermsBean.java 1.0 June 01, 2005 4:30 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;

import java.util.Vector;
import java.sql.Date;
/**
 * This class is used to hold Award Report Terms data
 *
 * @author Shivakumar M J
 * @version 1.0
 * Created on July 12, 2004 11:49 AM
 */

public class AwardReportReqBean extends AwardReportTermsBean{
    private int reportNumber;
    private int reportStatusCode;
    private int overdueCounter;
    private Date activityDate;
    private String comments;
    private String personId;
    private String address;
    private String reportStatusDescription;
    private String fullName;
    private Date frequencyBase;
    
    //Bijosh added
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
    /*BEGIN case 2074
     *String to date
     */
    private Date stringDueDate;
    private Date stringActivityDate;
    /*END case 2074
     **String to date
     */
    //Added for userid to username enhancement
    private String updateUserName;
    
    /** Creates new AwardReportReqBean */
    public AwardReportReqBean() {
    }
    
    /** Getter for property reportNumber.
     * @return Value of property reportNumber.
     *
     */
    public int getReportNumber() {
        return reportNumber;
    }    
    
    /** Setter for property reportNumber.
     * @param reportNumber New value of property reportNumber.
     *
     */
    public void setReportNumber(int reportNumber) {
        this.reportNumber = reportNumber;
    }
    
    /** Getter for property reportStatusCode.
     * @return Value of property reportStatusCode.
     *
     */
    public int getReportStatusCode() {
        return reportStatusCode;
    }
    
    /** Setter for property reportStatusCode.
     * @param reportStatusCode New value of property reportStatusCode.
     *
     */
    public void setReportStatusCode(int reportStatusCode) {
        this.reportStatusCode = reportStatusCode;
    }
    
    /** Getter for property overdueCounter.
     * @return Value of property overdueCounter.
     *
     */
    public int getOverdueCounter() {
        return overdueCounter;
    }
    
    /** Setter for property overdueCounter.
     * @param overdueCounter New value of property overdueCounter.
     *
     */
    public void setOverdueCounter(int overdueCounter) {
        this.overdueCounter = overdueCounter;
    }
    
    /** Getter for property activityDate.
     * @return Value of property activityDate.
     *
     */
    public java.sql.Date getActivityDate() {
        return activityDate;
    }
    
    /** Setter for property activityDate.
     * @param activityDate New value of property activityDate.
     *
     */
    public void setActivityDate(java.sql.Date activityDate) {
        this.activityDate = activityDate;
    }
    
    /** Getter for property comments.
     * @return Value of property comments.
     *
     */
    public java.lang.String getComments() {
        return comments;
    }
    
    /** Setter for property comments.
     * @param comments New value of property comments.
     *
     */
    public void setComments(java.lang.String comments) {
        this.comments = comments;
    }
    
    /** Getter for property personId.
     * @return Value of property personId.
     *
     */
    public java.lang.String getPersonId() {
        return personId;
    }
    
    /** Setter for property personId.
     * @param personId New value of property personId.
     *
     */
    public void setPersonId(java.lang.String personId) {
        this.personId = personId;
    }
    
    /** Getter for property address.
     * @return Value of property address.
     *
     */
    public java.lang.String getAddress() {
        return address;
    }
    
    /** Setter for property address.
     * @param address New value of property address.
     *
     */
    public void setAddress(java.lang.String address) {
        this.address = address;
    }
    
    /** Getter for property reportStatusDescription.
     * @return Value of property reportStatusDescription.
     *
     */
    public java.lang.String getReportStatusDescription() {
        return reportStatusDescription;
    }
    
    /** Setter for property reportStatusDescription.
     * @param reportStatusDescription New value of property reportStatusDescription.
     *
     */
    public void setReportStatusDescription(java.lang.String reportStatusDescription) {
        this.reportStatusDescription = reportStatusDescription;
    }
    
    /** Getter for property fullName.
     * @return Value of property fullName.
     *
     */
    public java.lang.String getFullName() {
        return fullName;
    }
    
    /** Setter for property fullName.
     * @param fullName New value of property fullName.
     *
     */
    public void setFullName(java.lang.String fullName) {
        this.fullName = fullName;
    }
    
    /** Getter for property frequencyBase.
     * @return Value of property frequencyBase.
     *
     */
    public java.sql.Date getFrequencyBase() {
        return frequencyBase;
    }
    
    /** Setter for property frequencyBase.
     * @param frequencyBase New value of property frequencyBase.
     *
     */
    public void setFrequencyBase(java.sql.Date frequencyBase) {
        this.frequencyBase = frequencyBase;
    }
    
    //Code added by Ajay 
    /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        AwardReportReqBean awardReportReqBean = (AwardReportReqBean)obj;
		
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
         * Getter for property stringDueDate.
         * @return Value of property stringDueDate.
         */
        /* Begin Case 2074 date not sorting 
         *changed data type from string to date
         */
        public java.sql.Date getStringDueDate() {
            return stringDueDate;
        }
        
        /**
         * Setter for property stringDueDate.
         * @param stringDueDate New value of property stringDueDate.
         */
        public void setStringDueDate(java.sql.Date stringDueDate) {
            this.stringDueDate = stringDueDate;
        }
        
        /**
         * Getter for property stringActivityDate.
         * @return Value of property stringActivityDate.
         */
        public java.sql.Date getStringActivityDate() {
            return stringActivityDate;
        }
        
        /**
         * Setter for property stringActivityDate.
         * @param stringActivityDate New value of property stringActivityDate.
         */
        public void setStringActivityDate(java.sql.Date stringActivityDate) {
            this.stringActivityDate = stringActivityDate;
        }
        /* End Case 2074 date not sorting 
         *changed data type from string to date
         */
       //UserId to Username enhancement - Start
       /**
         * Method used to get the update user name
         * @return updateUserName String
         */
        public String getUpdateUserName() {
            return updateUserName;
        }

        /**
         * Method used to set the update user name
         * @param updateUserName String
         */
        public void setUpdateUserName(String updateUserName) {
            this.updateUserName = updateUserName;
        }
      //UserId to Username enhancement - End
}    




