/**
 * @(#)AwardBean.java 1.0 01/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;

/**
 * This class is used to set and get the Award Header
 *
 * @author Prasanna Kumar K
 * @version 1.0 Created on January 03, 2004 12:30 PM
 */

public class AwardDetailsBean extends AwardBaseBean implements java.io.Serializable {

	private String modificationNumber;
	private String sponsorAwardNumber;
	private int statusCode;
	private String statusDescription;
	private int templateCode;
	private java.sql.Date awardExecutionDate;
	private java.sql.Date awardEffectiveDate;
	private java.sql.Date beginDate;
	private String sponsorCode;
	private String accountNumber;
	private String apprvdEquipmentIndicator;
	private String apprvdForeignTripIndicator;
	private String apprvdSubcontractIndicator;
	private String paymentScheduleIndicator;
	private String idcIndicator;
	private String transferSponsorIndicator;
	private String costSharingIndicator;
	private String specialReviewIndicator;
	private String scienceCodeIndicator;
	private String nsfCode;
	private String nsfDescription;
	private String sponsorName;
	private String title;
	// 3823: Key Person record needed in IP and Award
	private String keyPersonIndicator;

	// Malini:12/14/15
	private String procPriorCode;
	private String procPriorCodeDescription;
	private String reviewDate;
	// Malini:12/14/15

	/**
	 * Default Constructor
	 */

	public AwardDetailsBean() {
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof AwardDetailsBean))
			return false;
		AwardDetailsBean awardDetailsBean = (AwardDetailsBean) object;

		if (getMitAwardNumber().equals(awardDetailsBean.getMitAwardNumber())
				&& getSequenceNumber() == awardDetailsBean.getSequenceNumber()) {
			return true;
		}

		return false;
	}

	/**
	 * Getter for property accountNumber.
	 *
	 * @return Value of property accountNumber.
	 */
	public java.lang.String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * Getter for property apprvdEquipmentIndicator.
	 *
	 * @return Value of property apprvdEquipmentIndicator.
	 */
	public java.lang.String getApprvdEquipmentIndicator() {
		return apprvdEquipmentIndicator;
	}

	/**
	 * Getter for property apprvdForeignTripIndicator.
	 *
	 * @return Value of property apprvdForeignTripIndicator.
	 */
	public java.lang.String getApprvdForeignTripIndicator() {
		return apprvdForeignTripIndicator;
	}

	/**
	 * Getter for property apprvdSubcontractIndicator.
	 *
	 * @return Value of property apprvdSubcontractIndicator.
	 */
	public java.lang.String getApprvdSubcontractIndicator() {
		return apprvdSubcontractIndicator;
	}

	/**
	 * Getter for property awardEffectiveDate.
	 *
	 * @return Value of property awardEffectiveDate.
	 */
	public java.sql.Date getAwardEffectiveDate() {
		return awardEffectiveDate;
	}

	/**
	 * Getter for property awardExecutionDate.
	 *
	 * @return Value of property awardExecutionDate.
	 */
	public java.sql.Date getAwardExecutionDate() {
		return awardExecutionDate;
	}

	/**
	 * Getter for property beginDate.
	 *
	 * @return Value of property beginDate.
	 */
	public java.sql.Date getBeginDate() {
		return beginDate;
	}

	/**
	 * Getter for property costSharingIndicator.
	 *
	 * @return Value of property costSharingIndicator.
	 */
	public java.lang.String getCostSharingIndicator() {
		return costSharingIndicator;
	}

	/**
	 * Getter for property idcIndicator.
	 *
	 * @return Value of property idcIndicator.
	 */
	public java.lang.String getIdcIndicator() {
		return idcIndicator;
	}

	// 3823: Key Person record needed in IP and Award - Start
	public String getKeyPersonIndicator() {
		return keyPersonIndicator;
	}

	/**
	 * Getter for property modificationNumber.
	 *
	 * @return Value of property modificationNumber.
	 */
	public java.lang.String getModificationNumber() {
		return modificationNumber;
	}

	/**
	 * Getter for property nsfCode.
	 *
	 * @return Value of property nsfCode.
	 */
	public java.lang.String getNsfCode() {
		return nsfCode;
	}

	/**
	 * Getter for property nsfDescription.
	 *
	 * @return Value of property nsfDescription.
	 */
	public java.lang.String getNsfDescription() {
		return nsfDescription;
	}

	/**
	 * Getter for property paymentScheduleIndicator.
	 *
	 * @return Value of property paymentScheduleIndicator.
	 */
	public java.lang.String getPaymentScheduleIndicator() {
		return paymentScheduleIndicator;
	}

	/**
	 * @return the reviewCode
	 */
	public String getProcPriorCode() {
		return procPriorCode;
	}

	/**
	 * @return the reviewCodeDescription
	 */
	public String getProcPriorCodeDescription() {
		return procPriorCodeDescription;
	}

	/**
	 * @return the reviewDate
	 */
	public String getReviewDate() {
		return reviewDate;
	}

	/**
	 * Getter for property scienceCodeIndicator.
	 *
	 * @return Value of property scienceCodeIndicator.
	 */
	public java.lang.String getScienceCodeIndicator() {
		return scienceCodeIndicator;
	}

	/**
	 * Getter for property specialReviewIndicator.
	 *
	 * @return Value of property specialReviewIndicator.
	 */
	public java.lang.String getSpecialReviewIndicator() {
		return specialReviewIndicator;
	}

	/**
	 * Getter for property sponsorAwardNumber.
	 *
	 * @return Value of property sponsorAwardNumber.
	 */
	public java.lang.String getSponsorAwardNumber() {
		return sponsorAwardNumber;
	}

	/**
	 * Getter for property sponsorCode.
	 *
	 * @return Value of property sponsorCode.
	 */
	public java.lang.String getSponsorCode() {
		return sponsorCode;
	}

	/**
	 * Getter for property sponsorName.
	 *
	 * @return Value of property sponsorName.
	 */
	public java.lang.String getSponsorName() {
		return sponsorName;
	}

	/**
	 * Getter for property statusCode.
	 *
	 * @return Value of property statusCode.
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * Getter for property statusDescription.
	 *
	 * @return Value of property statusDescription.
	 */
	public java.lang.String getStatusDescription() {
		return statusDescription;
	}

	/**
	 * Getter for property templateCode.
	 *
	 * @return Value of property templateCode.
	 */
	public int getTemplateCode() {
		return templateCode;
	}

	/**
	 * Getter for property title.
	 *
	 * @return Value of property title.
	 *
	 */
	public java.lang.String getTitle() {
		return title;
	}

	/**
	 * Getter for property transferSponsorIndicator.
	 *
	 * @return Value of property transferSponsorIndicator.
	 */
	public java.lang.String getTransferSponsorIndicator() {
		return transferSponsorIndicator;
	}

	/**
	 * Setter for property accountNumber.
	 *
	 * @param accountNumber
	 *            New value of property accountNumber.
	 */
	public void setAccountNumber(java.lang.String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * Setter for property apprvdEquipmentIndicator.
	 *
	 * @param apprvdEquipmentIndicator
	 *            New value of property apprvdEquipmentIndicator.
	 */
	public void setApprvdEquipmentIndicator(java.lang.String apprvdEquipmentIndicator) {
		this.apprvdEquipmentIndicator = apprvdEquipmentIndicator;
	}

	/**
	 * Setter for property apprvdForeignTripIndicator.
	 *
	 * @param apprvdForeignTripIndicator
	 *            New value of property apprvdForeignTripIndicator.
	 */
	public void setApprvdForeignTripIndicator(java.lang.String apprvdForeignTripIndicator) {
		this.apprvdForeignTripIndicator = apprvdForeignTripIndicator;
	}

	/**
	 * Setter for property apprvdSubcontractIndicator.
	 *
	 * @param apprvdSubcontractIndicator
	 *            New value of property apprvdSubcontractIndicator.
	 */
	public void setApprvdSubcontractIndicator(java.lang.String apprvdSubcontractIndicator) {
		this.apprvdSubcontractIndicator = apprvdSubcontractIndicator;
	}

	/**
	 * Setter for property awardEffectiveDate.
	 *
	 * @param awardEffectiveDate
	 *            New value of property awardEffectiveDate.
	 */
	public void setAwardEffectiveDate(java.sql.Date awardEffectiveDate) {
		this.awardEffectiveDate = awardEffectiveDate;
	}

	/**
	 * Setter for property awardExecutionDate.
	 *
	 * @param awardExecutionDate
	 *            New value of property awardExecutionDate.
	 */
	public void setAwardExecutionDate(java.sql.Date awardExecutionDate) {
		this.awardExecutionDate = awardExecutionDate;
	}

	/**
	 * Setter for property beginDate.
	 *
	 * @param beginDate
	 *            New value of property beginDate.
	 */
	public void setBeginDate(java.sql.Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * Setter for property costSharingIndicator.
	 *
	 * @param costSharingIndicator
	 *            New value of property costSharingIndicator.
	 */
	public void setCostSharingIndicator(java.lang.String costSharingIndicator) {
		this.costSharingIndicator = costSharingIndicator;
	}

	/**
	 * Setter for property idcIndicator.
	 *
	 * @param idcIndicator
	 *            New value of property idcIndicator.
	 */
	public void setIdcIndicator(java.lang.String idcIndicator) {
		this.idcIndicator = idcIndicator;
	}

	public void setKeyPersonIndicator(String keyPersonIndicator) {
		this.keyPersonIndicator = keyPersonIndicator;
	}
	// 3823: Key Person record needed in IP and Award - End

	/**
	 * Setter for property modificationNumber.
	 *
	 * @param modificationNumber
	 *            New value of property modificationNumber.
	 */
	public void setModificationNumber(java.lang.String modificationNumber) {
		this.modificationNumber = modificationNumber;
	}

	/**
	 * Setter for property nsfCode.
	 *
	 * @param nsfCode
	 *            New value of property nsfCode.
	 */
	public void setNsfCode(java.lang.String nsfCode) {
		this.nsfCode = nsfCode;
	}

	/**
	 * Setter for property nsfDescription.
	 *
	 * @param nsfDescription
	 *            New value of property nsfDescription.
	 */
	public void setNsfDescription(java.lang.String nsfDescription) {
		this.nsfDescription = nsfDescription;
	}

	/**
	 * Setter for property paymentScheduleIndicator.
	 *
	 * @param paymentScheduleIndicator
	 *            New value of property paymentScheduleIndicator.
	 */
	public void setPaymentScheduleIndicator(java.lang.String paymentScheduleIndicator) {
		this.paymentScheduleIndicator = paymentScheduleIndicator;
	}

	/**
	 * @param reviewCode
	 *            the reviewCode to set
	 */
	public void setProcPriorCode(String procPriorCode) {
		this.procPriorCode = procPriorCode;
	}

	/**
	 * @param reviewCodeDescription
	 *            the reviewCodeDescription to set
	 */
	public void setProcPriorCodeDescription(String procPriorCodeDescription) {
		this.procPriorCodeDescription = procPriorCodeDescription;
	}

	/**
	 * @param reviewDate
	 *            the reviewDate to set
	 */
	public void setReviewDate(String reviewDate) {
		this.reviewDate = reviewDate;
	}

	/**
	 * Setter for property scienceCodeIndicator.
	 *
	 * @param scienceCodeIndicator
	 *            New value of property scienceCodeIndicator.
	 */
	public void setScienceCodeIndicator(java.lang.String scienceCodeIndicator) {
		this.scienceCodeIndicator = scienceCodeIndicator;
	}

	/**
	 * Setter for property specialReviewIndicator.
	 *
	 * @param specialReviewIndicator
	 *            New value of property specialReviewIndicator.
	 */
	public void setSpecialReviewIndicator(java.lang.String specialReviewIndicator) {
		this.specialReviewIndicator = specialReviewIndicator;
	}

	/**
	 * Setter for property sponsorAwardNumber.
	 *
	 * @param sponsorAwardNumber
	 *            New value of property sponsorAwardNumber.
	 */
	public void setSponsorAwardNumber(java.lang.String sponsorAwardNumber) {
		this.sponsorAwardNumber = sponsorAwardNumber;
	}

	/**
	 * Setter for property sponsorCode.
	 *
	 * @param sponsorCode
	 *            New value of property sponsorCode.
	 */
	public void setSponsorCode(java.lang.String sponsorCode) {
		this.sponsorCode = sponsorCode;
	}

	/**
	 * Setter for property sponsorName.
	 *
	 * @param sponsorName
	 *            New value of property sponsorName.
	 */
	public void setSponsorName(java.lang.String sponsorName) {
		this.sponsorName = sponsorName;
	}

	/**
	 * Setter for property statusCode.
	 *
	 * @param statusCode
	 *            New value of property statusCode.
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * Setter for property statusDescription.
	 *
	 * @param statusDescription
	 *            New value of property statusDescription.
	 */
	public void setStatusDescription(java.lang.String statusDescription) {
		this.statusDescription = statusDescription;
	}

	/**
	 * Setter for property templateCode.
	 *
	 * @param templateCode
	 *            New value of property templateCode.
	 */
	public void setTemplateCode(int templateCode) {
		this.templateCode = templateCode;
	}

	/**
	 * Setter for property title.
	 *
	 * @param title
	 *            New value of property title.
	 *
	 */
	public void setTitle(java.lang.String title) {
		this.title = title;
	}

	/**
	 * Setter for property transferSponsorIndicator.
	 *
	 * @param transferSponsorIndicator
	 *            New value of property transferSponsorIndicator.
	 */
	public void setTransferSponsorIndicator(java.lang.String transferSponsorIndicator) {
		this.transferSponsorIndicator = transferSponsorIndicator;
	}

}