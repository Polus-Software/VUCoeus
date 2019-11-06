/*
 * @(#)SubcontractingReportsInfoBean.java 
 *
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.xml.generator.bean;

import edu.mit.coeus.bean.CoeusBean;

import java.beans.*;
import java.math.BigDecimal;
import edu.mit.coeus.utils.CoeusVector;
import java.sql.Date;
import java.util.Vector;
 

public class SubcontractingReportsInfoBean {
 
   /**
    * String reportNumber
    * values = 294 or 295
   */
   private String reportNumber;  
   /** fields common to 294 and 295
   */
   private String companyName;
   private String companyStreetAddress;
   private String companyCity;
   private String companyState;
   private String companyZip;
   private String contractorID;
   private java.util.Date dateSubmitted;
   private String reportFY;
 
   private String reportType;
   private boolean isMarchReport;
   private boolean isSeptReport;
   private boolean isPrime;
   private boolean isSub;
   private String officialName;
   private String officialAreaCode;
   private String officialPhone;
    
   /** fields for 294
    */
   private String awardNumber;    
   private String primeContractNumber;
   private String subContractNumber;  

   /** fields for 295
    */
   private String officialTitle;
   private String ceoName;
   private String ceoTitle;
   
   //detailInfo is vector of SubcontractingReportsDetailBeans
   // there will be one SubcontractingReportDetailBean for 294,
   // and multiple (one for each admin activity) for 295
   private CoeusVector detailInfo;
 
   
    
    private String updateUser = null;
    //holds update timestamp - needed to implement CoeusBean
    private java.sql.Timestamp updateTimestamp = null;
    //holds account type - needed to implement CoeusBean
    private String acType = null;

   
 
    /**
     *	Default Constructor
     */
    
    public SubcontractingReportsInfoBean(){
    }
      
    /**
     * Getter for property awardNumber.
     * @return Value of property awardNumber.
     */
    public java.lang.String getAwardNumber() {
        return awardNumber;
    }    
   
    /**
     * Setter for property awardNumber.
     * @param awardNumber New value of property awardNumber.
     */
    public void setAwardNumber(java.lang.String awardNumber) {
        this.awardNumber = awardNumber;
    }    
   
    /**
     * Getter for property companyName.
     * @return Value of property companyName.
     */
    public java.lang.String getCompanyName() {
        return companyName;
    }    

    /**
     * Setter for property companyName.
     * @param companyName New value of property companyName.
     */
    public void setCompanyName(java.lang.String companyName) {
        this.companyName = companyName;
    }    
    
    /**
     * Getter for property companyStreetAddress.
     * @return Value of property companyStreetAddress.
     */
    public java.lang.String getCompanyStreetAddress() {
        return companyStreetAddress;
    }
    
    /**
     * Setter for property companyStreetAddress.
     * @param companyStreetAddress New value of property companyStreetAddress.
     */
    public void setCompanyStreetAddress(java.lang.String companyStreetAddress) {
        this.companyStreetAddress = companyStreetAddress;
    }
    
    /**
     * Getter for property companyCity.
     * @return Value of property companyCity.
     */
    public java.lang.String getCompanyCity() {
        return companyCity;
    }
    
    /**
     * Setter for property companyCity.
     * @param companyCity New value of property companyCity.
     */
    public void setCompanyCity(java.lang.String companyCity) {
        this.companyCity = companyCity;
    }
    
    /**
     * Getter for property companyState.
     * @return Value of property companyState.
     */
    public java.lang.String getCompanyState() {
        return companyState;
    }
    
    /**
     * Setter for property companyState.
     * @param companyState New value of property companyState.
     */
    public void setCompanyState(java.lang.String companyState) {
        this.companyState = companyState;
    }
    
    /**
     * Getter for property companyZip.
     * @return Value of property companyZip.
     */
    public java.lang.String getCompanyZip() {
        return companyZip;
    }
    
    /**
     * Setter for property companyZip.
     * @param companyZip New value of property companyZip.
     */
    public void setCompanyZip(java.lang.String companyZip) {
        this.companyZip = companyZip;
    }
    
    /**
     * Getter for property contractorID.
     * @return Value of property contractorID.
     */
    public java.lang.String getContractorID() {
        return contractorID;
    }
    
    /**
     * Setter for property contractorID.
     * @param contractorID New value of property contractorID.
     */
    public void setContractorID(java.lang.String contractorID) {
        this.contractorID = contractorID;
    }
    
    
     
    
    /**
     * Getter for property reportFY.
     * @return Value of property reportFY.
     */
    public java.lang.String getReportFY() {
        return reportFY;
    }
    
    /**
     * Setter for property reportFY.
     * @param reportFY New value of property reportFY.
     */
    public void setReportFY(java.lang.String reportFY) {
        this.reportFY = reportFY;
    }
    
    /**
     * Getter for property reportType.
     * @return Value of property reportType.
     */
    public java.lang.String getReportType() {
        return reportType;
    }
    
    /**
     * Setter for property reportType.
     * @param reportType New value of property reportType.
     */
    public void setReportType(java.lang.String reportType) {
        this.reportType = reportType;
    }
    
    /**
     * Getter for property isPrime.
     * @return Value of property isPrime.
     */
    public boolean getIsPrime() {
        return isPrime;
    }
    
    /**
     * Setter for property isPrime.
     * @param isPrime New value of property isPrime.
     */
    public void setIsPrime(boolean isPrime) {
        this.isPrime = isPrime;
    }
    
    /**
     * Getter for property isSub.
     * @return Value of property isSub.
     */
    public boolean getIsSub() {
        return isSub;
    }
    
    /**
     * Setter for property isSub.
     * @param isSub New value of property isSub.
     */
    public void setIsSub(boolean isSub) {
        this.isSub = isSub;
    }
    
    /**
     * Getter for property primeContractNumber.
     * @return Value of property primeContractNumber.
     */
    public java.lang.String getPrimeContractNumber() {
        return primeContractNumber;
    }
    
    /**
     * Setter for property primeContractNumber.
     * @param primeContractNumber New value of property primeContractNumber.
     */
    public void setPrimeContractNumber(java.lang.String primeContractNumber) {
        this.primeContractNumber = primeContractNumber;
    }
    
    /**
     * Getter for property subContractNumber.
     * @return Value of property subContractNumber.
     */
    public java.lang.String getSubContractNumber() {
        return subContractNumber;
    }
    
    /**
     * Setter for property subContractNumber.
     * @param subContractNumber New value of property subContractNumber.
     */
    public void setSubContractNumber(java.lang.String subContractNumber) {
        this.subContractNumber = subContractNumber;
    }
    
    /**
     * Getter for property officialName.
     * @return Value of property officialName.
     */
    public java.lang.String getOfficialName() {
        return officialName;
    }
    
    /**
     * Setter for property officialName.
     * @param officialName New value of property officialName.
     */
    public void setOfficialName(java.lang.String officialName) {
        this.officialName = officialName;
    }
    
    /**
     * Getter for property ceoName.
     * @return Value of property ceoName.
     */
    public java.lang.String getCeoName() {
        return ceoName;
    }
    
    /**
     * Setter for property ceoName.
     * @param ceoName New value of property ceoName.
     */
    public void setCeoName(java.lang.String ceoName) {
        this.ceoName = ceoName;
    }
    
    /**
     * Getter for property detailInfo.
     * @return Value of property detailInfo.
     */
    public edu.mit.coeus.utils.CoeusVector getDetailInfo() {
        return detailInfo;
    }
    
    /**
     * Setter for property detailInfo.
     * @param detailInfo New value of property detailInfo.
     */
    public void setDetailInfo(edu.mit.coeus.utils.CoeusVector detailInfo) {
        this.detailInfo = detailInfo;
    }
    // following methods are necessary to implement CoeusBean  
   
    public String getUpdateUser() {
     return updateUser;
    }
    public void setUpdateUser(String userId) {
    }
    
  public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) throws edu.mit.coeus.exception.CoeusException {
    return true;
  }

    public java.lang.String getAcType() {
     return acType;
    }
    
    public void setAcType(java.lang.String acType) {      
    }  
   
    public java.sql.Timestamp getUpdateTimestamp() {
     return updateTimestamp;
    }
    
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {   
    }
    
    /**
     * Getter for property reportNumber.
     * @return Value of property reportNumber.
     */
    public java.lang.String getReportNumber() {
        return reportNumber;
    }
    
    /**
     * Setter for property reportNumber.
     * @param reportNumber New value of property reportNumber.
     */
    public void setReportNumber(java.lang.String reportNumber) {
        this.reportNumber = reportNumber;
    }
    
   
    
    /**
     * Getter for property officialAreaCode.
     * @return Value of property officialAreaCode.
     */
    public java.lang.String getOfficialAreaCode() {
        return officialAreaCode;
    }
    
    /**
     * Setter for property officialAreaCode.
     * @param officialAreaCode New value of property officialAreaCode.
     */
    public void setOfficialAreaCode(java.lang.String officialAreaCode) {
        this.officialAreaCode = officialAreaCode;
    }
    
    /**
     * Getter for property officialPhone.
     * @return Value of property officialPhone.
     */
    public java.lang.String getOfficialPhone() {
        return officialPhone;
    }
    
    /**
     * Setter for property officialPhone.
     * @param officialPhone New value of property officialPhone.
     */
    public void setOfficialPhone(java.lang.String officialPhone) {
        this.officialPhone = officialPhone;
    }
    
    /**
     * Getter for property agencyName.
     * @return Value of property agencyName.
     *
    public java.lang.String getAgencyName() {
        return agencyName;
    }
    
    /**
     * Setter for property agencyName.
     * @param agencyName New value of property agencyName.
     *
    public void setAgencyName(java.lang.String agencyName) {
        this.agencyName = agencyName;
    }
    
    /**
     * Getter for property agencyStreetAddress.
     * @return Value of property agencyStreetAddress.
     *
    public java.lang.String getAgencyStreetAddress() {
        return agencyStreetAddress;
    }
    
    /**
     * Setter for property agencyStreetAddress.
     * @param agencyStreetAddress New value of property agencyStreetAddress.
     *
    public void setAgencyStreetAddress(java.lang.String agencyStreetAddress) {
        this.agencyStreetAddress = agencyStreetAddress;
    }
    
    /**
     * Getter for property agencyCity.
     * @return Value of property agencyCity.
     *
    public java.lang.String getAgencyCity() {
        return agencyCity;
    }
    
    /**
     * Setter for property agencyCity.
     * @param agencyCity New value of property agencyCity.
     *
    public void setAgencyCity(java.lang.String agencyCity) {
        this.agencyCity = agencyCity;
    }
    
    /**
     * Getter for property agencyState.
     * @return Value of property agencyState.
     *
    public java.lang.String getAgencyState() {
        return agencyState;
    }
    
    /**
     * Setter for property agencyState.
     * @param agencyState New value of property agencyState.
     *
    public void setAgencyState(java.lang.String agencyState) {
        this.agencyState = agencyState;
    }
    
    /**
     * Getter for property agencyZip.
     * @return Value of property agencyZip.
     *
    public java.lang.String getAgencyZip() {
        return agencyZip;
    }
    
    /**
     * Setter for property agencyZip.
     * @param agencyZip New value of property agencyZip.
     *
    public void setAgencyZip(java.lang.String agencyZip) {
        this.agencyZip = agencyZip;
    }
    
    /**
     * Getter for property officialTitle.
     * @return Value of property officialTitle.
     */
    public java.lang.String getOfficialTitle() {
        return officialTitle;
    }
    
    /**
     * Setter for property officialTitle.
     * @param officialTitle New value of property officialTitle.
     */
    public void setOfficialTitle(java.lang.String officialTitle) {
        this.officialTitle = officialTitle;
    }
    
    /**
     * Getter for property ceoTitle.
     * @return Value of property ceoTitle.
     */
    public java.lang.String getCeoTitle() {
        return ceoTitle;
    }
    
    /**
     * Setter for property ceoTitle.
     * @param ceoTitle New value of property ceoTitle.
     */
    public void setCeoTitle(java.lang.String ceoTitle) {
        this.ceoTitle = ceoTitle;
    }
    
    /**
     * Getter for property isMarchReport.
     * @return Value of property isMarchReport.
     */
    public boolean getIsMarchReport() {
        return isMarchReport;
    }
    
    /**
     * Setter for property isMarchReport.
     * @param isMarchReport New value of property isMarchReport.
     */
    public void setIsMarchReport(boolean isMarchReport) {
        this.isMarchReport = isMarchReport;
    }
    
    /**
     * Getter for property isSeptReport.
     * @return Value of property isSeptReport.
     */
    public boolean getIsSeptReport() {
        return isSeptReport;
    }
     
    /**
     * Setter for property isSeptReport.
     * @param isSeptReport New value of property isSeptReport.
     */
    public void setIsSeptReport(boolean isSeptReport) {
        this.isSeptReport = isSeptReport;
    }
    
    /**
     * Getter for property dateSubmitted.
     * @return Value of property dateSubmitted.
     */
    public java.util.Date getDateSubmitted() {
        return dateSubmitted;
    }
    
    /**
     * Setter for property dateSubmitted.
     * @param dateSubmitted New value of property dateSubmitted.
     */
    public void setDateSubmitted(java.util.Date dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }
    
}