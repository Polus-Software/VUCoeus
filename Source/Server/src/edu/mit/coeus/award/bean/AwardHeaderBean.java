/**
 * @(#)AwardHeaderBean.java 1.0 01/03/04
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

public class AwardHeaderBean extends AwardBaseBean {

	// private String mitAwardNumber;
	// private int sequenceNumber;
	private String proposalNumber;
	private String title;
	private int awardTypeCode;
	private String awardTypeDescription;

	/*
	 * Changed by Geo on 1-Nov-2004
	 */
	// Changing the primitive datatype to object
	// private double specialEBRateOffCampus;
	// private double specialEBRateOnCampus;
	// private double preAwardAuthorizedAmount;
	// private int finalInvoiceDue;

	private Double specialEBRateOffCampus;
	private Double specialEBRateOnCampus;
	private Double preAwardAuthorizedAmount;
	private Integer finalInvoiceDue;
	/*
	 * End block
	 */

	private java.sql.Date preAwardEffectiveDate;
	private String cfdaNumber;
	private String dfafsNumber;
	private String subPlanFlag;
	private String procurementPriorityCode;
	private String primeSponsorCode;
	private String primeSponsorName;
	private int nonCompetingContPrpslDue;
	private int competingRenewalPrpslDue;
	private int basisOfPaymentCode;
	private int methodOfPaymentCode;
	private int paymentInvoiceFreqCode;
	private int invoiceNoOfCopies;
	private int activityTypeCode;
	private String activityTypeDescription;
	private String procurementPriorityDescription;
	private int accountTypeCode;
	private String accountTypeDescription;
	// holds update user id
	// private String updateUser = null;
	// holds update timestamp
	// private java.sql.Timestamp updateTimestamp = null;

	/**
	 * Default Constructor
	 */

	public AwardHeaderBean() {
	}

	// /** Getter for property mitAwardNumber.
	// * @return Value of property mitAwardNumber.
	// */
	// public java.lang.String getMitAwardNumber() {
	// return mitAwardNumber;
	// }
	//
	// /** Setter for property mitAwardNumber.
	// * @param mitAwardNumber New value of property mitAwardNumber.
	// */
	// public void setMitAwardNumber(java.lang.String mitAwardNumber) {
	// this.mitAwardNumber = mitAwardNumber;
	// }
	//
	// /** Getter for property sequenceNumber.
	// * @return Value of property sequenceNumber.
	// */
	// public int getSequenceNumber() {
	// return sequenceNumber;
	// }
	//
	// /** Setter for property sequenceNumber.
	// * @param sequenceNumber New value of property sequenceNumber.
	// */
	// public void setSequenceNumber(int sequenceNumber) {
	// this.sequenceNumber = sequenceNumber;
	// }

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
	 * Getter for property accountTypeCode.
	 *
	 * @return Value of property accountTypeCode.
	 */
	public int getAccountTypeCode() {
		return accountTypeCode;
	}

	/**
	 * Getter for property accountTypeDescription.
	 *
	 * @return Value of property accountTypeDescription.
	 */
	public java.lang.String getAccountTypeDescription() {
		return accountTypeDescription;
	}

	/**
	 * Getter for property activityTypeCode.
	 *
	 * @return Value of property activityTypeCode.
	 */
	public int getActivityTypeCode() {
		return activityTypeCode;
	}

	/**
	 * Getter for property activityTypeDescription.
	 *
	 * @return Value of property activityTypeDescription.
	 */
	public java.lang.String getActivityTypeDescription() {
		return activityTypeDescription;
	}

	/**
	 * Getter for property awardTypeCode.
	 *
	 * @return Value of property awardTypeCode.
	 */
	public int getAwardTypeCode() {
		return awardTypeCode;
	}

	/**
	 * Getter for property awardTypeDescription.
	 *
	 * @return Value of property awardTypeDescription.
	 */
	public java.lang.String getAwardTypeDescription() {
		return awardTypeDescription;
	}

	/**
	 * Getter for property basisOfPaymentCode.
	 *
	 * @return Value of property basisOfPaymentCode.
	 */
	public int getBasisOfPaymentCode() {
		return basisOfPaymentCode;
	}

	/**
	 * Getter for property cfdaNumber.
	 *
	 * @return Value of property cfdaNumber.
	 */
	public java.lang.String getCfdaNumber() {
		return cfdaNumber;
	}

	// /** Getter for property specialEBRateOffCampus.
	// * @return Value of property specialEBRateOffCampus.
	// */
	// public double getSpecialEBRateOffCampus() {
	// return specialEBRateOffCampus;
	// }
	//
	// /** Setter for property specialEBRateOffCampus.
	// * @param specialEBRateOffCampus New value of property
	// specialEBRateOffCampus.
	// */
	// public void setSpecialEBRateOffCampus(double specialEBRateOffCampus) {
	// this.specialEBRateOffCampus = specialEBRateOffCampus;
	// }
	//
	// /** Getter for property specialEBRateOnCampus.
	// * @return Value of property specialEBRateOnCampus.
	// */
	// public double getSpecialEBRateOnCampus() {
	// return specialEBRateOnCampus;
	// }
	//
	// /** Setter for property specialEBRateOnCampus.
	// * @param specialEBRateOnCampus New value of property
	// specialEBRateOnCampus.
	// */
	// public void setSpecialEBRateOnCampus(double specialEBRateOnCampus) {
	// this.specialEBRateOnCampus = specialEBRateOnCampus;
	// }
	//
	// /** Getter for property preAwardAuthorizedAmount.
	// * @return Value of property preAwardAuthorizedAmount.
	// */
	// public double getPreAwardAuthorizedAmount() {
	// return preAwardAuthorizedAmount;
	// }
	//
	// /** Setter for property preAwardAuthorizedAmount.
	// * @param preAwardAuthorizedAmount New value of property
	// preAwardAuthorizedAmount.
	// */
	// public void setPreAwardAuthorizedAmount(double preAwardAuthorizedAmount)
	// {
	// this.preAwardAuthorizedAmount = preAwardAuthorizedAmount;
	// }

	/**
	 * Getter for property competingRenewalPrpslDue.
	 *
	 * @return Value of property competingRenewalPrpslDue.
	 */
	public int getCompetingRenewalPrpslDue() {
		return competingRenewalPrpslDue;
	}

	/**
	 * Getter for property dfafsNumber.
	 *
	 * @return Value of property dfafsNumber.
	 */
	public java.lang.String getDfafsNumber() {
		return dfafsNumber;
	}

	/**
	 * Getter for property finalInvoiceDue.
	 *
	 * @return Value of property finalInvoiceDue.
	 */
	public java.lang.Integer getFinalInvoiceDue() {
		return finalInvoiceDue;
	}

	/**
	 * Getter for property invoiceNoOfCopies.
	 *
	 * @return Value of property invoiceNoOfCopies.
	 */
	public int getInvoiceNoOfCopies() {
		return invoiceNoOfCopies;
	}

	/**
	 * Getter for property methodOfPaymentCode.
	 *
	 * @return Value of property methodOfPaymentCode.
	 */
	public int getMethodOfPaymentCode() {
		return methodOfPaymentCode;
	}

	/**
	 * Getter for property nonCompetingContPrpslDue.
	 *
	 * @return Value of property nonCompetingContPrpslDue.
	 */
	public int getNonCompetingContPrpslDue() {
		return nonCompetingContPrpslDue;
	}

	/**
	 * Getter for property paymentInvoiceFreqCode.
	 *
	 * @return Value of property paymentInvoiceFreqCode.
	 */
	public int getPaymentInvoiceFreqCode() {
		return paymentInvoiceFreqCode;
	}

	/**
	 * Getter for property preAwardAuthorizedAmount.
	 *
	 * @return Value of property preAwardAuthorizedAmount.
	 */
	public java.lang.Double getPreAwardAuthorizedAmount() {
		return preAwardAuthorizedAmount;
	}

	/**
	 * Getter for property preAwardEffectiveDate.
	 *
	 * @return Value of property preAwardEffectiveDate.
	 */
	public java.sql.Date getPreAwardEffectiveDate() {
		return preAwardEffectiveDate;
	}

	/**
	 * Getter for property primeSponsorCode.
	 *
	 * @return Value of property primeSponsorCode.
	 */
	public java.lang.String getPrimeSponsorCode() {
		return primeSponsorCode;
	}

	public String getPrimeSponsorName() {
		return primeSponsorName;
	}

	/**
	 * Getter for property procurementPriorityCode.
	 *
	 * @return Value of property procurementPriorityCode.
	 */
	public java.lang.String getProcurementPriorityCode() {
		return procurementPriorityCode;
	}

	/**
	 * @return the procurementPriorityDescription
	 */
	public String getProcurementPriorityDescription() {
		return procurementPriorityDescription;
	}

	/**
	 * Getter for property proposalNumber.
	 *
	 * @return Value of property proposalNumber.
	 */
	public java.lang.String getProposalNumber() {
		return proposalNumber;
	}

	/**
	 * Getter for property specialEBRateOffCampus.
	 *
	 * @return Value of property specialEBRateOffCampus.
	 */
	public java.lang.Double getSpecialEBRateOffCampus() {
		return specialEBRateOffCampus;
	}

	/**
	 * Getter for property specialEBRateOnCampus.
	 *
	 * @return Value of property specialEBRateOnCampus.
	 */
	public java.lang.Double getSpecialEBRateOnCampus() {
		return specialEBRateOnCampus;
	}

	/**
	 * Getter for property subPlanFlag.
	 *
	 * @return Value of property subPlanFlag.
	 */
	public java.lang.String getSubPlanFlag() {
		return subPlanFlag;
	}

	/**
	 * Getter for property title.
	 *
	 * @return Value of property title.
	 */
	public java.lang.String getTitle() {
		return title;
	}

	/**
	 * Setter for property accountTypeCode.
	 *
	 * @param accountTypeCode
	 *            New value of property accountTypeCode.
	 */
	public void setAccountTypeCode(int accountTypeCode) {
		this.accountTypeCode = accountTypeCode;
	}

	/**
	 * Setter for property accountTypeDescription.
	 *
	 * @param accountTypeDescription
	 *            New value of property accountTypeDescription.
	 */
	public void setAccountTypeDescription(java.lang.String accountTypeDescription) {
		this.accountTypeDescription = accountTypeDescription;
	}

	/**
	 * Setter for property activityTypeCode.
	 *
	 * @param activityTypeCode
	 *            New value of property activityTypeCode.
	 */
	public void setActivityTypeCode(int activityTypeCode) {
		this.activityTypeCode = activityTypeCode;
	}

	/**
	 * Setter for property activityTypeDescription.
	 *
	 * @param activityTypeDescription
	 *            New value of property activityTypeDescription.
	 */
	public void setActivityTypeDescription(java.lang.String activityTypeDescription) {
		this.activityTypeDescription = activityTypeDescription;
	}

	/**
	 * Setter for property awardTypeCode.
	 *
	 * @param awardTypeCode
	 *            New value of property awardTypeCode.
	 */
	public void setAwardTypeCode(int awardTypeCode) {
		this.awardTypeCode = awardTypeCode;
	}

	/**
	 * Setter for property awardTypeDescription.
	 *
	 * @param awardTypeDescription
	 *            New value of property awardTypeDescription.
	 */
	public void setAwardTypeDescription(java.lang.String awardTypeDescription) {
		this.awardTypeDescription = awardTypeDescription;
	}

	// /** Getter for property finalInvoiceDue.
	// * @return Value of property finalInvoiceDue.
	// */
	// public int getFinalInvoiceDue() {
	// return finalInvoiceDue;
	// }
	//
	// /** Setter for property finalInvoiceDue.
	// * @param finalInvoiceDue New value of property finalInvoiceDue.
	// */
	// public void setFinalInvoiceDue(int finalInvoiceDue) {
	// this.finalInvoiceDue = finalInvoiceDue;
	// }

	/**
	 * Setter for property basisOfPaymentCode.
	 *
	 * @param basisOfPaymentCode
	 *            New value of property basisOfPaymentCode.
	 */
	public void setBasisOfPaymentCode(int basisOfPaymentCode) {
		this.basisOfPaymentCode = basisOfPaymentCode;
	}

	/**
	 * Setter for property cfdaNumber.
	 *
	 * @param cfdaNumber
	 *            New value of property cfdaNumber.
	 */
	public void setCfdaNumber(java.lang.String cfdaNumber) {
		this.cfdaNumber = cfdaNumber;
	}

	/**
	 * Setter for property competingRenewalPrpslDue.
	 *
	 * @param competingRenewalPrpslDue
	 *            New value of property competingRenewalPrpslDue.
	 */
	public void setCompetingRenewalPrpslDue(int competingRenewalPrpslDue) {
		this.competingRenewalPrpslDue = competingRenewalPrpslDue;
	}

	/**
	 * Setter for property dfafsNumber.
	 *
	 * @param dfafsNumber
	 *            New value of property dfafsNumber.
	 */
	public void setDfafsNumber(java.lang.String dfafsNumber) {
		this.dfafsNumber = dfafsNumber;
	}

	// /** Getter for property updateUser.
	// * @return Value of property updateUser.
	// */
	// public java.lang.String getUpdateUser() {
	// return updateUser;
	// }
	//
	// /** Setter for property updateUser.
	// * @param updateUser New value of property updateUser.
	// */
	// public void setUpdateUser(java.lang.String updateUser) {
	// this.updateUser = updateUser;
	// }
	//
	// /** Getter for property updateTimestamp.
	// * @return Value of property updateTimestamp.
	// */
	// public java.sql.Timestamp getUpdateTimestamp() {
	// return updateTimestamp;
	// }
	//
	// /** Setter for property updateTimestamp.
	// * @param updateTimestamp New value of property updateTimestamp.
	// */
	// public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
	// this.updateTimestamp = updateTimestamp;
	// }

	/**
	 * Setter for property finalInvoiceDue.
	 *
	 * @param finalInvoiceDue
	 *            New value of property finalInvoiceDue.
	 */
	public void setFinalInvoiceDue(java.lang.Integer finalInvoiceDue) {
		this.finalInvoiceDue = finalInvoiceDue;
	}

	/**
	 * Setter for property invoiceNoOfCopies.
	 *
	 * @param invoiceNoOfCopies
	 *            New value of property invoiceNoOfCopies.
	 */
	public void setInvoiceNoOfCopies(int invoiceNoOfCopies) {
		this.invoiceNoOfCopies = invoiceNoOfCopies;
	}

	/**
	 * Setter for property methodOfPaymentCode.
	 *
	 * @param methodOfPaymentCode
	 *            New value of property methodOfPaymentCode.
	 */
	public void setMethodOfPaymentCode(int methodOfPaymentCode) {
		this.methodOfPaymentCode = methodOfPaymentCode;
	}

	/**
	 * Setter for property nonCompetingContPrpslDue.
	 *
	 * @param nonCompetingContPrpslDue
	 *            New value of property nonCompetingContPrpslDue.
	 */
	public void setNonCompetingContPrpslDue(int nonCompetingContPrpslDue) {
		this.nonCompetingContPrpslDue = nonCompetingContPrpslDue;
	}

	/**
	 * Setter for property paymentInvoiceFreqCode.
	 *
	 * @param paymentInvoiceFreqCode
	 *            New value of property paymentInvoiceFreqCode.
	 */
	public void setPaymentInvoiceFreqCode(int paymentInvoiceFreqCode) {
		this.paymentInvoiceFreqCode = paymentInvoiceFreqCode;
	}

	/**
	 * Setter for property preAwardAuthorizedAmount.
	 *
	 * @param preAwardAuthorizedAmount
	 *            New value of property preAwardAuthorizedAmount.
	 */
	public void setPreAwardAuthorizedAmount(java.lang.Double preAwardAuthorizedAmount) {
		this.preAwardAuthorizedAmount = preAwardAuthorizedAmount;
	}

	/**
	 * Setter for property preAwardEffectiveDate.
	 *
	 * @param preAwardEffectiveDate
	 *            New value of property preAwardEffectiveDate.
	 */
	public void setPreAwardEffectiveDate(java.sql.Date preAwardEffectiveDate) {
		this.preAwardEffectiveDate = preAwardEffectiveDate;
	}

	/**
	 * Setter for property primeSponsorCode.
	 *
	 * @param primeSponsorCode
	 *            New value of property primeSponsorCode.
	 */
	public void setPrimeSponsorCode(java.lang.String primeSponsorCode) {
		this.primeSponsorCode = primeSponsorCode;
	}

	public void setPrimeSponsorName(String primeSponsorName) {
		this.primeSponsorName = primeSponsorName;
	}

	/**
	 * Setter for property procurementPriorityCode.
	 *
	 * @param procurementPriorityCode
	 *            New value of property procurementPriorityCode.
	 */
	public void setProcurementPriorityCode(java.lang.String procurementPriorityCode) {
		this.procurementPriorityCode = procurementPriorityCode;
	}

	/**
	 * @param procurementPriorityDescription
	 *            the procurementPriorityDescription to set
	 */
	public void setProcurementPriorityDescription(String procurementPriorityDescription) {
		this.procurementPriorityDescription = procurementPriorityDescription;
	}

	/**
	 * Setter for property proposalNumber.
	 *
	 * @param proposalNumber
	 *            New value of property proposalNumber.
	 */
	public void setProposalNumber(java.lang.String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}

	/**
	 * Setter for property specialEBRateOffCampus.
	 *
	 * @param specialEBRateOffCampus
	 *            New value of property specialEBRateOffCampus.
	 */
	public void setSpecialEBRateOffCampus(java.lang.Double specialEBRateOffCampus) {
		this.specialEBRateOffCampus = specialEBRateOffCampus;
	}

	/**
	 * Setter for property specialEBRateOnCampus.
	 *
	 * @param specialEBRateOnCampus
	 *            New value of property specialEBRateOnCampus.
	 */
	public void setSpecialEBRateOnCampus(java.lang.Double specialEBRateOnCampus) {
		this.specialEBRateOnCampus = specialEBRateOnCampus;
	}

	/**
	 * Setter for property subPlanFlag.
	 *
	 * @param subPlanFlag
	 *            New value of property subPlanFlag.
	 */
	public void setSubPlanFlag(java.lang.String subPlanFlag) {
		this.subPlanFlag = subPlanFlag;
	}

	/**
	 * Setter for property title.
	 *
	 * @param title
	 *            New value of property title.
	 */
	public void setTitle(java.lang.String title) {
		this.title = title;
	}

}