/**
 * @(#)AwardBean.java 1.0 01/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;

import edu.mit.coeus.utils.CoeusVector;

/**
 * This class is used to set and get the Award Header
 *
 * @author Prasanna Kumar K
 * @version 1.0 Created on January 03, 2004 12:30 PM
 */
public class AwardBean extends AwardBaseBean implements java.io.Serializable {

	// private String modificationNumber;
	// private String sponsorAwardNumber;
	// private int statusCode;
	// private String statusDescription;
	// private int templateCode;
	// private java.sql.Date awardExecutionDate;
	// private java.sql.Date awardEffectiveDate;
	// private java.sql.Date beginDate;
	// private String sponsorCode;
	// private String accountNumber;
	// private String apprvdEquipmentIndicator;
	// private String apprvdForeignTripIndicator;
	// private String apprvdSubcontractIndicator;
	// private String paymentScheduleIndicator;
	// private String idcIndicator;
	// private String transferSponsorIndicator;
	// private String costSharingIndicator;
	// private String specialReviewIndicator;
	// private String scienceCodeIndicator;
	// private String nsfCode;
	private CoeusVector awardInvestigators;
	private AwardHeaderBean awardHeaderBean;
	private CoeusVector awardAmountInfo;
	// private String sponsorName;
	private CoeusVector awardComments;
	// private CoeusVector awardCustomElements;
	private CoeusVector awardApprovedSubcontracts;
	private CoeusVector subcontractFundingSource;
	private AwardDetailsBean awardDetailsBean;
	// holds Mode
	private char mode;
	// COEUSQA 2111 STARTS
	private AwardDocumentRouteBean latestAwardDocumentRouteBean;

	private String leadUnitNumber;

	// COEUSQA 2111 ENDS
	/**
	 * Default Constructor
	 */
	public AwardBean() {
		awardDetailsBean = new AwardDetailsBean();
	}

	/**
	 * Equals method to check whether Primary Key values are same for the given
	 * Bean
	 *
	 * @param obj
	 *            Object
	 * @return boolean indicating whether the two beans are same or not
	 */
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	/**
	 * Getter for property accountNumber.
	 *
	 * @return Value of property accountNumber.
	 */
	public java.lang.String getAccountNumber() {
		return this.awardDetailsBean.getAccountNumber();
	}

	/**
	 * Getter for property apprvdEquipmentIndicator.
	 *
	 * @return Value of property apprvdEquipmentIndicator.
	 */
	public java.lang.String getApprvdEquipmentIndicator() {
		return this.awardDetailsBean.getApprvdEquipmentIndicator();
	}

	/**
	 * Getter for property apprvdForeignTripIndicator.
	 *
	 * @return Value of property apprvdForeignTripIndicator.
	 */
	public java.lang.String getApprvdForeignTripIndicator() {
		return this.awardDetailsBean.getApprvdForeignTripIndicator();
	}

	/**
	 * Getter for property apprvdSubcontractIndicator.
	 *
	 * @return Value of property apprvdSubcontractIndicator.
	 */
	public java.lang.String getApprvdSubcontractIndicator() {
		return this.awardDetailsBean.getApprvdSubcontractIndicator();
	}

	/**
	 * Getter for property awardAmountInfo.
	 *
	 * @return Value of property awardAmountInfo.
	 */
	public CoeusVector getAwardAmountInfo() {
		return awardAmountInfo;
	}

	// /** Getter for property awardCustomElements.
	// * @return Value of property awardCustomElements.
	// */
	// public edu.mit.coeus.utils.CoeusVector getAwardCustomElements() {
	// return awardCustomElements;
	// }
	// /** Setter for property awardCustomElements.
	// * @param awardCustomElements New value of property awardCustomElements.
	// */
	// public void setAwardCustomElements(edu.mit.coeus.utils.CoeusVector
	// awardCustomElements) {
	// this.awardCustomElements = awardCustomElements;
	// }
	/**
	 * Getter for property awardApprovedSubcontracts.
	 *
	 * @return Value of property awardApprovedSubcontracts.
	 */
	public edu.mit.coeus.utils.CoeusVector getAwardApprovedSubcontracts() {
		return awardApprovedSubcontracts;
	}

	/**
	 * Getter for property awardComments.
	 *
	 * @return Value of property awardComments.
	 */
	public edu.mit.coeus.utils.CoeusVector getAwardComments() {
		return awardComments;
	}

	/**
	 * Getter for property awardDetailsBean.
	 *
	 * @return Value of property awardDetailsBean.
	 */
	public edu.mit.coeus.award.bean.AwardDetailsBean getAwardDetailsBean() {
		return awardDetailsBean;
	}

	/**
	 * Getter for property awardEffectiveDate.
	 *
	 * @return Value of property awardEffectiveDate.
	 */
	public java.sql.Date getAwardEffectiveDate() {
		return this.awardDetailsBean.getAwardEffectiveDate();
	}

	/**
	 * Getter for property awardExecutionDate.
	 *
	 * @return Value of property awardExecutionDate.
	 */
	public java.sql.Date getAwardExecutionDate() {
		return this.awardDetailsBean.getAwardExecutionDate();
	}

	/**
	 * Getter for property awardHeaderBean.
	 *
	 * @return Value of property awardHeaderBean.
	 */
	public edu.mit.coeus.award.bean.AwardHeaderBean getAwardHeaderBean() {
		return awardHeaderBean;
	}

	/**
	 * Getter for property awardInvestigators.
	 *
	 * @return Value of property awardInvestigators.
	 */
	public CoeusVector getAwardInvestigators() {
		return awardInvestigators;
	}

	/**
	 * Getter for property beginDate.
	 *
	 * @return Value of property beginDate.
	 */
	public java.sql.Date getBeginDate() {
		return this.awardDetailsBean.getBeginDate();
	}

	/**
	 * Getter for property costSharingIndicator.
	 *
	 * @return Value of property costSharingIndicator.
	 */
	public java.lang.String getCostSharingIndicator() {
		return this.awardDetailsBean.getCostSharingIndicator();
	}

	/**
	 * Getter for property idcIndicator.
	 *
	 * @return Value of property idcIndicator.
	 */
	public java.lang.String getIdcIndicator() {
		return this.awardDetailsBean.getIdcIndicator();
	}

	// 3823: Key Person record needed in IP and Award - Start
	/**
	 * Getter for property keyPersonIndicator.
	 *
	 * @return Value of property keyPersonIndicator.
	 */
	public java.lang.String getKeyPersonIndicator() {
		return this.awardDetailsBean.getKeyPersonIndicator();
	}

	public AwardDocumentRouteBean getLatestAwardDocumentRouteBean() {
		return latestAwardDocumentRouteBean;
	}

	public String getLeadUnitNumber() {
		return leadUnitNumber;
	}

	/**
	 * Getter for property mitAwardNumber.
	 *
	 * @return Value of property mitAwardNumber.
	 */
	@Override
	public java.lang.String getMitAwardNumber() {
		return super.getMitAwardNumber();
	}

	/**
	 * Getter for property mode.
	 *
	 * @return Value of property mode.
	 */
	public char getMode() {
		return mode;
	}

	/**
	 * Getter for property modificationNumber.
	 *
	 * @return Value of property modificationNumber.
	 */
	public java.lang.String getModificationNumber() {
		return this.awardDetailsBean.getModificationNumber();
	}

	/**
	 * Getter for property nsfCode.
	 *
	 * @return Value of property nsfCode.
	 */
	public java.lang.String getNsfCode() {
		return this.awardDetailsBean.getNsfCode();
	}

	/**
	 * Getter for property nsfDescription.
	 *
	 * @return Value of property nsfDescription.
	 */
	public java.lang.String getNsfDescription() {
		return this.awardDetailsBean.getNsfDescription();
	}

	/**
	 * Getter for property paymentScheduleIndicator.
	 *
	 * @return Value of property paymentScheduleIndicator.
	 */
	public java.lang.String getPaymentScheduleIndicator() {
		return this.awardDetailsBean.getPaymentScheduleIndicator();
	}

	/*
	 * Malini:12/14/15 Added getters/setters for the review code and description
	 */
	/**
	 * Getter for property statusCode.
	 *
	 * @return Value of property statusCode.
	 */
	public String getPriorProcCode() {
		return this.awardDetailsBean.getProcPriorCode();
	}

	/**
	 * Getter for property statusDescription.
	 *
	 * @return Value of property statusDescription.
	 */
	public java.lang.String getPriorProcCodeDescription() {
		return this.awardDetailsBean.getProcPriorCodeDescription();
	}

	/**
	 * Getter for property scienceCodeIndicator.
	 *
	 * @return Value of property scienceCodeIndicator.
	 */
	public java.lang.String getScienceCodeIndicator() {
		return this.awardDetailsBean.getScienceCodeIndicator();
	}

	/**
	 * Getter for property sequenceNumber.
	 *
	 * @return Value of property sequenceNumber.
	 */
	@Override
	public int getSequenceNumber() {
		return super.getSequenceNumber();
	}

	/**
	 * Getter for property specialReviewIndicator.
	 *
	 * @return Value of property specialReviewIndicator.
	 */
	public java.lang.String getSpecialReviewIndicator() {
		return this.awardDetailsBean.getSpecialReviewIndicator();
	}

	/**
	 * Getter for property sponsorAwardNumber.
	 *
	 * @return Value of property sponsorAwardNumber.
	 */
	public java.lang.String getSponsorAwardNumber() {
		return this.awardDetailsBean.getSponsorAwardNumber();
	}

	/**
	 * Getter for property sponsorCode.
	 *
	 * @return Value of property sponsorCode.
	 */
	public java.lang.String getSponsorCode() {
		return this.awardDetailsBean.getSponsorCode();
	}

	/**
	 * Getter for property sponsorName.
	 *
	 * @return Value of property sponsorName.
	 */
	public java.lang.String getSponsorName() {
		return this.awardDetailsBean.getSponsorName();
	}

	/**
	 * Getter for property statusCode.
	 *
	 * @return Value of property statusCode.
	 */
	public int getStatusCode() {
		return this.awardDetailsBean.getStatusCode();
	}

	/**
	 * Getter for property statusDescription.
	 *
	 * @return Value of property statusDescription.
	 */
	public java.lang.String getStatusDescription() {
		return this.awardDetailsBean.getStatusDescription();
	}

	/**
	 * Getter for property subcontractFundingSource.
	 *
	 * @return Value of property subcontractFundingSource.
	 */
	public edu.mit.coeus.utils.CoeusVector getSubcontractFundingSource() {
		return subcontractFundingSource;
	}

	/**
	 * Getter for property templateCode.
	 *
	 * @return Value of property templateCode.
	 */
	public int getTemplateCode() {
		return this.awardDetailsBean.getTemplateCode();
	}

	/**
	 * Getter for property transferSponsorIndicator.
	 *
	 * @return Value of property transferSponsorIndicator.
	 */
	public java.lang.String getTransferSponsorIndicator() {
		return this.awardDetailsBean.getTransferSponsorIndicator();
	}

	/**
	 * Setter for property accountNumber.
	 *
	 * @param accountNumber
	 *            New value of property accountNumber.
	 */
	public void setAccountNumber(java.lang.String accountNumber) {
		this.awardDetailsBean.setAccountNumber(accountNumber);
	}

	/**
	 * Setter for property apprvdEquipmentIndicator.
	 *
	 * @param apprvdEquipmentIndicator
	 *            New value of property apprvdEquipmentIndicator.
	 */
	public void setApprvdEquipmentIndicator(java.lang.String apprvdEquipmentIndicator) {
		this.awardDetailsBean.setApprvdEquipmentIndicator(apprvdEquipmentIndicator);
	}

	/**
	 * Setter for property apprvdForeignTripIndicator.
	 *
	 * @param apprvdForeignTripIndicator
	 *            New value of property apprvdForeignTripIndicator.
	 */
	public void setApprvdForeignTripIndicator(java.lang.String apprvdForeignTripIndicator) {
		this.awardDetailsBean.setApprvdForeignTripIndicator(apprvdForeignTripIndicator);
	}

	/**
	 * Setter for property apprvdSubcontractIndicator.
	 *
	 * @param apprvdSubcontractIndicator
	 *            New value of property apprvdSubcontractIndicator.
	 */
	public void setApprvdSubcontractIndicator(java.lang.String apprvdSubcontractIndicator) {
		this.awardDetailsBean.setApprvdSubcontractIndicator(apprvdSubcontractIndicator);
	}

	/**
	 * Setter for property awardAmountInfo.
	 *
	 * @param awardAmountInfo
	 *            New value of property awardAmountInfo.
	 */
	public void setAwardAmountInfo(CoeusVector awardAmountInfo) {
		this.awardAmountInfo = awardAmountInfo;
	}

	/**
	 * Setter for property awardApprovedSubcontracts.
	 *
	 * @param awardApprovedSubcontracts
	 *            New value of property awardApprovedSubcontracts.
	 */
	public void setAwardApprovedSubcontracts(edu.mit.coeus.utils.CoeusVector awardApprovedSubcontracts) {
		this.awardApprovedSubcontracts = awardApprovedSubcontracts;
	}

	/**
	 * Setter for property awardComments.
	 *
	 * @param awardComments
	 *            New value of property awardComments.
	 */
	public void setAwardComments(edu.mit.coeus.utils.CoeusVector awardComments) {
		this.awardComments = awardComments;
	}

	/**
	 * Setter for property awardDetailsBean.
	 *
	 * @param awardDetailsBean
	 *            New value of property awardDetailsBean.
	 */
	public void setAwardDetailsBean(edu.mit.coeus.award.bean.AwardDetailsBean awardDetailsBean) {
		this.awardDetailsBean = awardDetailsBean;
	}

	/**
	 * Setter for property awardEffectiveDate.
	 *
	 * @param awardEffectiveDate
	 *            New value of property awardEffectiveDate.
	 */
	public void setAwardEffectiveDate(java.sql.Date awardEffectiveDate) {
		this.awardDetailsBean.setAwardEffectiveDate(awardEffectiveDate);
	}

	/**
	 * Setter for property awardExecutionDate.
	 *
	 * @param awardExecutionDate
	 *            New value of property awardExecutionDate.
	 */
	public void setAwardExecutionDate(java.sql.Date awardExecutionDate) {
		this.awardDetailsBean.setAwardExecutionDate(awardExecutionDate);
	}

	/**
	 * Setter for property awardHeaderBean.
	 *
	 * @param awardHeaderBean
	 *            New value of property awardHeaderBean.
	 */
	public void setAwardHeaderBean(edu.mit.coeus.award.bean.AwardHeaderBean awardHeaderBean) {
		this.awardHeaderBean = awardHeaderBean;
	}

	/**
	 * Setter for property awardInvestigators.
	 *
	 * @param awardInvestigators
	 *            New value of property awardInvestigators.
	 */
	public void setAwardInvestigators(CoeusVector awardInvestigators) {
		this.awardInvestigators = awardInvestigators;
	}

	/**
	 * Setter for property beginDate.
	 *
	 * @param beginDate
	 *            New value of property beginDate.
	 */
	public void setBeginDate(java.sql.Date beginDate) {
		this.awardDetailsBean.setBeginDate(beginDate);
	}

	/**
	 * Setter for property costSharingIndicator.
	 *
	 * @param costSharingIndicator
	 *            New value of property costSharingIndicator.
	 */
	public void setCostSharingIndicator(java.lang.String costSharingIndicator) {
		this.awardDetailsBean.setCostSharingIndicator(costSharingIndicator);
	}

	/**
	 * Setter for property idcIndicator.
	 *
	 * @param idcIndicator
	 *            New value of property idcIndicator.
	 */
	public void setIdcIndicator(java.lang.String idcIndicator) {
		this.awardDetailsBean.setIdcIndicator(idcIndicator);
	}

	/**
	 * Setter for property keyPersonIndicator.
	 *
	 * @param keyPersonIndicator
	 *            New value of property keyPersonIndicator.
	 */
	public void setKeyPersonIndicator(java.lang.String keyPersonIndicator) {
		this.awardDetailsBean.setKeyPersonIndicator(keyPersonIndicator);
	}
	// 3823: Key Person record needed in IP and Award - End

	public void setLatestAwardDocumentRouteBean(AwardDocumentRouteBean latestAwardDocumentRouteBean) {
		this.latestAwardDocumentRouteBean = latestAwardDocumentRouteBean;
	}

	public void setLeadUnitNumber(String leadUnitNumber) {
		this.leadUnitNumber = leadUnitNumber;
	}

	/**
	 * Setter for property mitAwardNumber.
	 *
	 * @param mitAwardNumber
	 *            New value of property mitAwardNumber.
	 */
	@Override
	public void setMitAwardNumber(java.lang.String mitAwardNumber) {
		super.setMitAwardNumber(mitAwardNumber);
		this.awardDetailsBean.setMitAwardNumber(mitAwardNumber);
	}

	/**
	 * Setter for property mode.
	 *
	 * @param mode
	 *            New value of property mode.
	 */
	public void setMode(char mode) {
		this.mode = mode;
	}

	/**
	 * Setter for property modificationNumber.
	 *
	 * @param modificationNumber
	 *            New value of property modificationNumber.
	 */
	public void setModificationNumber(java.lang.String modificationNumber) {
		this.awardDetailsBean.setModificationNumber(modificationNumber);
	}

	/**
	 * Setter for property nsfCode.
	 *
	 * @param nsfCode
	 *            New value of property nsfCode.
	 */
	public void setNsfCode(java.lang.String nsfCode) {
		this.awardDetailsBean.setNsfCode(nsfCode);
	}

	/**
	 * Setter for property nsfDescription.
	 *
	 * @param nsfDescription
	 *            New value of property nsfDescription.
	 */
	public void setNsfDescription(java.lang.String nsfDescription) {
		this.awardDetailsBean.setNsfDescription(nsfDescription);
	}

	/**
	 * Setter for property paymentScheduleIndicator.
	 *
	 * @param paymentScheduleIndicator
	 *            New value of property paymentScheduleIndicator.
	 */
	public void setPaymentScheduleIndicator(java.lang.String paymentScheduleIndicator) {
		this.awardDetailsBean.setPaymentScheduleIndicator(paymentScheduleIndicator);
	}

	/**
	 * Setter for property statusCode.
	 *
	 * @param statusCode
	 *            New value of property statusCode.
	 */
	public void setProcPriorCode(String procPriorCode) {
		this.awardDetailsBean.setProcPriorCode(procPriorCode);
	}

	/**
	 * Getter for property statusDescription.
	 *
	 * @return Value of property statusDescription.
	 */
	public void setProcPriorCodeDescription(String description) {
		this.awardDetailsBean.setProcPriorCodeDescription(description);
	}

	/**
	 * Setter for property scienceCodeIndicator.
	 *
	 * @param scienceCodeIndicator
	 *            New value of property scienceCodeIndicator.
	 */
	public void setScienceCodeIndicator(java.lang.String scienceCodeIndicator) {
		this.awardDetailsBean.setScienceCodeIndicator(scienceCodeIndicator);
	}

	/**
	 * Setter for property sequenceNumber.
	 *
	 * @param sequenceNumber
	 *            New value of property sequenceNumber.
	 */
	@Override
	public void setSequenceNumber(int sequenceNumber) {
		super.setSequenceNumber(sequenceNumber);
		this.awardDetailsBean.setSequenceNumber(sequenceNumber);
	}

	/**
	 * Setter for property specialReviewIndicator.
	 *
	 * @param specialReviewIndicator
	 *            New value of property specialReviewIndicator.
	 */
	public void setSpecialReviewIndicator(java.lang.String specialReviewIndicator) {
		this.awardDetailsBean.setSpecialReviewIndicator(specialReviewIndicator);
	}

	/**
	 * Setter for property sponsorAwardNumber.
	 *
	 * @param sponsorAwardNumber
	 *            New value of property sponsorAwardNumber.
	 */
	public void setSponsorAwardNumber(java.lang.String sponsorAwardNumber) {
		this.awardDetailsBean.setSponsorAwardNumber(sponsorAwardNumber);
	}

	/**
	 * Setter for property sponsorCode.
	 *
	 * @param sponsorCode
	 *            New value of property sponsorCode.
	 */
	public void setSponsorCode(java.lang.String sponsorCode) {
		this.awardDetailsBean.setSponsorCode(sponsorCode);
	}

	/**
	 * Setter for property sponsorName.
	 *
	 * @param sponsorName
	 *            New value of property sponsorName.
	 */
	public void setSponsorName(java.lang.String sponsorName) {
		this.awardDetailsBean.setSponsorName(sponsorName);
	}

	/**
	 * Setter for property statusCode.
	 *
	 * @param statusCode
	 *            New value of property statusCode.
	 */
	public void setStatusCode(int statusCode) {
		this.awardDetailsBean.setStatusCode(statusCode);
	}

	/**
	 * Setter for property statusDescription.
	 *
	 * @param statusDescription
	 *            New value of property statusDescription.
	 */
	public void setStatusDescription(java.lang.String statusDescription) {
		this.awardDetailsBean.setStatusDescription(statusDescription);
	}

	/**
	 * Setter for property subcontractFundingSource.
	 *
	 * @param subcontractFundingSource
	 *            New value of property subcontractFundingSource.
	 */
	public void setSubcontractFundingSource(edu.mit.coeus.utils.CoeusVector subcontractFundingSource) {
		this.subcontractFundingSource = subcontractFundingSource;
	}

	/**
	 * Setter for property templateCode.
	 *
	 * @param templateCode
	 *            New value of property templateCode.
	 */
	public void setTemplateCode(int templateCode) {
		this.awardDetailsBean.setTemplateCode(templateCode);
	}

	/**
	 * Setter for property transferSponsorIndicator.
	 *
	 * @param transferSponsorIndicator
	 *            New value of property transferSponsorIndicator.
	 */
	public void setTransferSponsorIndicator(java.lang.String transferSponsorIndicator) {
		this.awardDetailsBean.setTransferSponsorIndicator(transferSponsorIndicator);
	}
}
