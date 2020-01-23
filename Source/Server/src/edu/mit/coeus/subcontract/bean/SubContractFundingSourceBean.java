/**
 * @(#)SubContractAmountInfoBean.java 1.0 01/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.subcontract.bean;

import java.sql.Date;
import java.sql.Timestamp;
import edu.mit.coeus.bean.BaseBean;
/**
 * This class is used to set and get the Subcontract Amount Info
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on January 03, 2004 12:30 PM
 */

public class SubContractFundingSourceBean extends SubContractBaseBean{
    
    private String mitAwardNumber;
    private String organizationName;
    private int statusCode;    
    private double obligatedAmount;
	//Added by Jobin
	private String accountNumber;
	private String statusDescription;
	private String sponsorCode;
	private String sponsorName;
	private Date finalExpirationDate;
	private int rowId;
    
    /**
     *	Default Constructor
     */
    
    public SubContractFundingSourceBean(){
    }    
    
    /** Getter for property mitAwardNumber.
     * @return Value of property mitAwardNumber.
     */
    public java.lang.String getMitAwardNumber() {
        return mitAwardNumber;
    }    
    
    /** Setter for property mitAwardNumber.
     * @param mitAwardNumber New value of property mitAwardNumber.
     */
    public void setMitAwardNumber(java.lang.String mitAwardNumber) {
        this.mitAwardNumber = mitAwardNumber;
    }
    
    /** Getter for property organizationName.
     * @return Value of property organizationName.
     */
    public java.lang.String getOrganizationName() {
        return organizationName;
    }
    
    /** Setter for property organizationName.
     * @param organizationName New value of property organizationName.
     */
    public void setOrganizationName(java.lang.String organizationName) {
        this.organizationName = organizationName;
    }
    
    /** Getter for property statusCode.
     * @return Value of property statusCode.
     */
    public int getStatusCode() {
        return statusCode;
    }
    
    /** Setter for property statusCode.
     * @param statusCode New value of property statusCode.
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    
    /** Getter for property obligatedAmount.
     * @return Value of property obligatedAmount.
     */
    public double getObligatedAmount() {
        return obligatedAmount;
    }
    
    /** Setter for property obligatedAmount.
     * @param obligatedAmount New value of property obligatedAmount.
     */
    public void setObligatedAmount(double obligatedAmount) {
        this.obligatedAmount = obligatedAmount;
    }    
	
	/**
	 * Getter for property accountNumber.
	 * @return Value of property accountNumber.
	 */
	public java.lang.String getAccountNumber() {
		return accountNumber;
	}
	
	/**
	 * Setter for property accountNumber.
	 * @param accountNumber New value of property accountNumber.
	 */
	public void setAccountNumber(java.lang.String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	/**
	 * Getter for property statusDescription.
	 * @return Value of property statusDescription.
	 */
	public String getStatusDescription() {
		return statusDescription;
	}
	
	/**
	 * Setter for property statusDescription.
	 * @param statusDescription New value of property statusDescription.
	 */
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
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
	 * Getter for property finalExpirationDate.
	 * @return Value of property finalExpirationDate.
	 */
	public java.sql.Date getFinalExpirationDate() {
		return finalExpirationDate;
	}
	
	/**
	 * Setter for property finalExpirationDate.
	 * @param finalExpirationDate New value of property finalExpirationDate.
	 */
	public void setFinalExpirationDate(java.sql.Date finalExpirationDate) {
		this.finalExpirationDate = finalExpirationDate;
	}
     /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */ 
     // Code added by Shivakumar   
        public boolean equals(Object obj) {            
            if(super.equals(obj)){
                SubContractFundingSourceBean subContractFundingSourceBean = (SubContractFundingSourceBean)obj;
                if(subContractFundingSourceBean.getMitAwardNumber().equals(getMitAwardNumber()) &&
					subContractFundingSourceBean.getRowId() == getRowId()) {
                     return true;
                } else {
                   return false; 
                }
            }else{
                return false;
            }    
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
		
}