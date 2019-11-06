/*
 * @(#)InstituteProposalIPReviewActivityBean.java 1.0 May 06 2004, 11:01 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.instprop.bean;

import java.sql.Date;
import java.sql.Timestamp;

/** This class is used to hold IP Review Activity of a Institute Proposal
 *
 * @version :1.0 May 06 2004, 11:01 AM
 * @author Prasanna Kumar K
 */

public class InstituteProposalIPReviewBean extends InstituteProposalBaseBean{
    
    private int proposalTypeCode;
    private int statusCode;
    private String title;
    private String sponsorCode;
    private String currentAccountNumber;
    private Date requestStartDateInitial;
    private Date requestStartDateTotal;
    private Date requestEndDateInitial;
    private Date requestEndDateTotal;
    private double totalDirectCostInitial;
    private double totalDirectCostTotal;
    private double totalInDirectCostInitial;
    private double totalInDirectCostTotal;
    private Timestamp createTimeStamp;
    private String initialContractAdmin;    
    private String unitNumber;
    private String unitName;
    private String personName;
    private String sponsorName;
    private int sponsorTypeCode;
    private String sponsorTypeDescription;
    private String personFullName;
    
    /** Getter for property proposalTypeCode.
     * @return Value of property proposalTypeCode.
     */
    public int getProposalTypeCode() {
        return proposalTypeCode;
    }    

    /** Setter for property proposalTypeCode.
     * @param proposalTypeCode New value of property proposalTypeCode.
     */
    public void setProposalTypeCode(int proposalTypeCode) {
        this.proposalTypeCode = proposalTypeCode;
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
    
    /** Getter for property title.
     * @return Value of property title.
     */
    public java.lang.String getTitle() {
        return title;
    }
    
    /** Setter for property title.
     * @param title New value of property title.
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }
    
    /** Getter for property sponsorCode.
     * @return Value of property sponsorCode.
     */
    public java.lang.String getSponsorCode() {
        return sponsorCode;
    }
    
    /** Setter for property sponsorCode.
     * @param sponsorCode New value of property sponsorCode.
     */
    public void setSponsorCode(java.lang.String sponsorCode) {
        this.sponsorCode = sponsorCode;
    }
    
    /** Getter for property currentAccountNumber.
     * @return Value of property currentAccountNumber.
     */
    public java.lang.String getCurrentAccountNumber() {
        return currentAccountNumber;
    }
    
    /** Setter for property currentAccountNumber.
     * @param currentAccountNumber New value of property currentAccountNumber.
     */
    public void setCurrentAccountNumber(java.lang.String currentAccountNumber) {
        this.currentAccountNumber = currentAccountNumber;
    }
    
    /** Getter for property requestStartDateInitial.
     * @return Value of property requestStartDateInitial.
     */
    public java.sql.Date getRequestStartDateInitial() {
        return requestStartDateInitial;
    }
    
    /** Setter for property requestStartDateInitial.
     * @param requestStartDateInitial New value of property requestStartDateInitial.
     */
    public void setRequestStartDateInitial(java.sql.Date requestStartDateInitial) {
        this.requestStartDateInitial = requestStartDateInitial;
    }
    
    /** Getter for property requestStartDateTotal.
     * @return Value of property requestStartDateTotal.
     */
    public java.sql.Date getRequestStartDateTotal() {
        return requestStartDateTotal;
    }
    
    /** Setter for property requestStartDateTotal.
     * @param requestStartDateTotal New value of property requestStartDateTotal.
     */
    public void setRequestStartDateTotal(java.sql.Date requestStartDateTotal) {
        this.requestStartDateTotal = requestStartDateTotal;
    }
    
    /** Getter for property requestEndDateInitial.
     * @return Value of property requestEndDateInitial.
     */
    public java.sql.Date getRequestEndDateInitial() {
        return requestEndDateInitial;
    }
    
    /** Setter for property requestEndDateInitial.
     * @param requestEndDateInitial New value of property requestEndDateInitial.
     */
    public void setRequestEndDateInitial(java.sql.Date requestEndDateInitial) {
        this.requestEndDateInitial = requestEndDateInitial;
    }
    
    /** Getter for property requestEndDateTotal.
     * @return Value of property requestEndDateTotal.
     */
    public java.sql.Date getRequestEndDateTotal() {
        return requestEndDateTotal;
    }
    
    /** Setter for property requestEndDateTotal.
     * @param requestEndDateTotal New value of property requestEndDateTotal.
     */
    public void setRequestEndDateTotal(java.sql.Date requestEndDateTotal) {
        this.requestEndDateTotal = requestEndDateTotal;
    }
    
    /** Getter for property totalDirectCostInitial.
     * @return Value of property totalDirectCostInitial.
     */
    public double getTotalDirectCostInitial() {
        return totalDirectCostInitial;
    }
    
    /** Setter for property totalDirectCostInitial.
     * @param totalDirectCostInitial New value of property totalDirectCostInitial.
     */
    public void setTotalDirectCostInitial(double totalDirectCostInitial) {
        this.totalDirectCostInitial = totalDirectCostInitial;
    }
    
    /** Getter for property totalDirectCostTotal.
     * @return Value of property totalDirectCostTotal.
     */
    public double getTotalDirectCostTotal() {
        return totalDirectCostTotal;
    }
    
    /** Setter for property totalDirectCostTotal.
     * @param totalDirectCostTotal New value of property totalDirectCostTotal.
     */
    public void setTotalDirectCostTotal(double totalDirectCostTotal) {
        this.totalDirectCostTotal = totalDirectCostTotal;
    }
    
    /** Getter for property totalInDirectCostInitial.
     * @return Value of property totalInDirectCostInitial.
     */
    public double getTotalInDirectCostInitial() {
        return totalInDirectCostInitial;
    }
    
    /** Setter for property totalInDirectCostInitial.
     * @param totalInDirectCostInitial New value of property totalInDirectCostInitial.
     */
    public void setTotalInDirectCostInitial(double totalInDirectCostInitial) {
        this.totalInDirectCostInitial = totalInDirectCostInitial;
    }
    
    /** Getter for property totalInDirectCostTotal.
     * @return Value of property totalInDirectCostTotal.
     */
    public double getTotalInDirectCostTotal() {
        return totalInDirectCostTotal;
    }
    
    /** Setter for property totalInDirectCostTotal.
     * @param totalInDirectCostTotal New value of property totalInDirectCostTotal.
     */
    public void setTotalInDirectCostTotal(double totalInDirectCostTotal) {
        this.totalInDirectCostTotal = totalInDirectCostTotal;
    }
    
    /** Getter for property createTimeStamp.
     * @return Value of property createTimeStamp.
     */
    public Timestamp getCreateTimeStamp() {
        return createTimeStamp;
    }
    
    /** Setter for property createTimeStamp.
     * @param createTimeStamp New value of property createTimeStamp.
     */
    public void setCreateTimeStamp(Timestamp createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }
    
    /** Getter for property initialContractAdmin.
     * @return Value of property initialContractAdmin.
     */
    public java.lang.String getInitialContractAdmin() {
        return initialContractAdmin;
    }
    
    /** Setter for property initialContractAdmin.
     * @param initialContractAdmin New value of property initialContractAdmin.
     */
    public void setInitialContractAdmin(java.lang.String initialContractAdmin) {
        this.initialContractAdmin = initialContractAdmin;
    }
    
    /** Getter for property unitNumber.
     * @return Value of property unitNumber.
     */
    public java.lang.String getUnitNumber() {
        return unitNumber;
    }
    
    /** Setter for property unitNumber.
     * @param unitNumber New value of property unitNumber.
     */
    public void setUnitNumber(java.lang.String unitNumber) {
        this.unitNumber = unitNumber;
    }
    
    /** Getter for property unitName.
     * @return Value of property unitName.
     */
    public java.lang.String getUnitName() {
        return unitName;
    }
    
    /** Setter for property unitName.
     * @param unitName New value of property unitName.
     */
    public void setUnitName(java.lang.String unitName) {
        this.unitName = unitName;
    }
    
    /** Getter for property personName.
     * @return Value of property personName.
     */
    public java.lang.String getPersonName() {
        return personName;
    }
    
    /** Setter for property personName.
     * @param personName New value of property personName.
     */
    public void setPersonName(java.lang.String personName) {
        this.personName = personName;
    }
    
    /** Getter for property sponsorName.
     * @return Value of property sponsorName.
     */
    public java.lang.String getSponsorName() {
        return sponsorName;
    }
    
    /** Setter for property sponsorName.
     * @param sponsorName New value of property sponsorName.
     */
    public void setSponsorName(java.lang.String sponsorName) {
        this.sponsorName = sponsorName;
    }
    
    /** Getter for property sponsorTypeCode.
     * @return Value of property sponsorTypeCode.
     */
    public int getSponsorTypeCode() {
        return sponsorTypeCode;
    }
    
    /** Setter for property sponsorTypeCode.
     * @param sponsorTypeCode New value of property sponsorTypeCode.
     */
    public void setSponsorTypeCode(int sponsorTypeCode) {
        this.sponsorTypeCode = sponsorTypeCode;
    }
    
    /** Getter for property personFullName.
     * @return Value of property personFullName.
     */
    public java.lang.String getPersonFullName() {
        return personFullName;
    }
    
    /** Setter for property personFullName.
     * @param personFullName New value of property personFullName.
     */
    public void setPersonFullName(java.lang.String personFullName) {
        this.personFullName = personFullName;
    }    
    
    /** Getter for property sponsorTypeDescription.
     * @return Value of property sponsorTypeDescription.
     */
    public java.lang.String getSponsorTypeDescription() {
        return sponsorTypeDescription;
    }
    
    /** Setter for property sponsorTypeDescription.
     * @param sponsorTypeDescription New value of property sponsorTypeDescription.
     */
    public void setSponsorTypeDescription(java.lang.String sponsorTypeDescription) {
        this.sponsorTypeDescription = sponsorTypeDescription;
    }
    
} // end