/**
 * @(#)AwardBean.java 1.0 01/19/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.subcontract.bean;
import java.util.Vector;
import edu.mit.coeus.utils.CoeusVector;
/**
 * This class is used to set and get the SubContract details
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on January 19, 2004 12:30 PM
**/

public class SubContractBean extends SubContractBaseBean{
    
    private String subContractId;
    private java.sql.Date startDate;
    private java.sql.Date endDate;
    private int subAwardTypeCode;
    private String subAwardTypeDescription;
    private String purchaseOrderNumber;
    private String title;
    private int statusCode;
    private String statusDescription;
    private String accountNumber;
    private String vendorNumber;
    private String requisitionerId;
    private String requisitionerName;
    private String requisitionerUnit;
    private String requisitionerUnitName;
    private String archiveLocation;
    private java.sql.Date closeOutDate;
    private String closeOutIndicator;
    private String fundingSourceIndicator;
    private String comments;    
    private CoeusVector subContractAmountInfo;
    private String subContractorName;
    //private String unitName;
    private CoeusVector subContractAmtReleased;
   //Case #3338 - Add new elements the person and rolodex details to Subcontract schema -Start 
    private CoeusVector auditorData;
    private CoeusVector ospAdminisratorData;
    private CoeusVector principalInvestigatorData;
    private CoeusVector administrativeOfficerData;
    // Modified for COEUSQA-1563
    private int siteInvestigator;
    private String siteInvestigatorName;
   //Case #3338 - Add new elements the person and rolodex details to Subcontract schema -End
    // Added for COEUSQA-1412 Subcontract Module changes - Change - Start
    private String negotiationNumber;
    private int costType;
    private String costTypeDescription;
    private java.sql.Date dateOfFullyExecuted;
    private String requistionNumber;
    private String reportsIndicator;
    private String attachmentIndicator;
    private String templateInfoIndicator;
    // Added for COEUSQA-1412 Subcontract Module changes - Change - End
    
    /* JM 2-26-2015 modify roles */
    private boolean hasModify, hasModifyDocuments, hasCreate;
    /* JM END */
    
    /**
     *	Default Constructor
     */
    
    public SubContractBean(){
    }    
    
    /* JM 2-26-2015 modify roles */
    public boolean getHasCreate() {
    	return hasCreate;
    }
    
    public void setHasCreate(boolean hasCreate) {
    	this.hasCreate = hasCreate;
    }
    
    public boolean getHasModify() {
    	return hasModify;
    }
    
    public void setHasModify(boolean hasModify) {
    	this.hasModify = hasModify;
    }
    
    public boolean getHasModifyDocuments() {
    	return hasModifyDocuments;
    }
    
    public void setHasModifyDocuments(boolean hasModifyDocuments) {
    	this.hasModifyDocuments = hasModifyDocuments;
    }
    /* JM END */
    
    /** Getter for property subContractId.
     * @return Value of property subContractId.
     */
    public java.lang.String getSubContractId() {
        return subContractId;
    }
    
    /** Setter for property subContractId.
     * @param subContractId New value of property subContractId.
     */
    public void setSubContractId(java.lang.String subContractId) {
        this.subContractId = subContractId;
    }
    
    /** Getter for property startDate.
     * @return Value of property startDate.
     */
    public java.sql.Date getStartDate() {
        return startDate;
    }
    
    /** Setter for property startDate.
     * @param startDate New value of property startDate.
     */
    public void setStartDate(java.sql.Date startDate) {
        this.startDate = startDate;
    }
    
    /** Getter for property endDate.
     * @return Value of property endDate.
     */
    public java.sql.Date getEndDate() {
        return endDate;
    }
    
    /** Setter for property endDate.
     * @param endDate New value of property endDate.
     */
    public void setEndDate(java.sql.Date endDate) {
        this.endDate = endDate;
    }
    
    /** Getter for property subAwardTypeCode.
     * @return Value of property subAwardTypeCode.
     */
    public int getSubAwardTypeCode() {
        return subAwardTypeCode;
    }
    
    /** Setter for property subAwardTypeCode.
     * @param subAwardTypeCode New value of property subAwardTypeCode.
     */
    public void setSubAwardTypeCode(int subAwardTypeCode) {
        this.subAwardTypeCode = subAwardTypeCode;
    }
    
    /** Getter for property purchaseOrderNumber.
     * @return Value of property purchaseOrderNumber.
     */
    public java.lang.String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }
    
    /** Setter for property purchaseOrderNumber.
     * @param purchaseOrderNumber New value of property purchaseOrderNumber.
     */
    public void setPurchaseOrderNumber(java.lang.String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
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
    
    /** Getter for property accountNumber.
     * @return Value of property accountNumber.
     */
    public java.lang.String getAccountNumber() {
        return accountNumber;
    }
    
    /** Setter for property accountNumber.
     * @param accountNumber New value of property accountNumber.
     */
    public void setAccountNumber(java.lang.String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    /** Getter for property vendorNumber.
     * @return Value of property vendorNumber.
     */
    public java.lang.String getVendorNumber() {
        return vendorNumber;
    }
    
    /** Setter for property vendorNumber.
     * @param vendorNumber New value of property vendorNumber.
     */
    public void setVendorNumber(java.lang.String vendorNumber) {
        this.vendorNumber = vendorNumber;
    }
    
    /** Getter for property requisitionerId.
     * @return Value of property requisitionerId.
     */
    public java.lang.String getRequisitionerId() {
        return requisitionerId;
    }
    
    /** Setter for property requisitionerId.
     * @param requisitionerId New value of property requisitionerId.
     */
    public void setRequisitionerId(java.lang.String requisitionerId) {
        this.requisitionerId = requisitionerId;
    }
    
    /** Getter for property requisitionerUnit.
     * @return Value of property requisitionerUnit.
     */
    public java.lang.String getRequisitionerUnit() {
        return requisitionerUnit;
    }
    
    /** Setter for property requisitionerUnit.
     * @param requisitionerUnit New value of property requisitionerUnit.
     */
    public void setRequisitionerUnit(java.lang.String requisitionerUnit) {
        this.requisitionerUnit = requisitionerUnit;
    }
    
    /** Getter for property archiveLocation.
     * @return Value of property archiveLocation.
     */
    public java.lang.String getArchiveLocation() {
        return archiveLocation;
    }
    
    /** Setter for property archiveLocation.
     * @param archiveLocation New value of property archiveLocation.
     */
    public void setArchiveLocation(java.lang.String archiveLocation) {
        this.archiveLocation = archiveLocation;
    }
    
    /** Getter for property closeOutDate.
     * @return Value of property closeOutDate.
     */
    public java.sql.Date getCloseOutDate() {
        return closeOutDate;
    }
    
    /** Setter for property closeOutDate.
     * @param closeOutDate New value of property closeOutDate.
     */
    public void setCloseOutDate(java.sql.Date closeOutDate) {
        this.closeOutDate = closeOutDate;
    }
    
    /** Getter for property closeOutIndicator.
     * @return Value of property closeOutIndicator.
     */
    public java.lang.String getCloseOutIndicator() {
        return closeOutIndicator;
    }
    
    /** Setter for property closeOutIndicator.
     * @param closeOutIndicator New value of property closeOutIndicator.
     */
    public void setCloseOutIndicator(java.lang.String closeOutIndicator) {
        this.closeOutIndicator = closeOutIndicator;
    }
    
    /** Getter for property fundingSourceIndicator.
     * @return Value of property fundingSourceIndicator.
     */
    public java.lang.String getFundingSourceIndicator() {
        return fundingSourceIndicator;
    }
    
    /** Setter for property fundingSourceIndicator.
     * @param fundingSourceIndicator New value of property fundingSourceIndicator.
     */
    public void setFundingSourceIndicator(java.lang.String fundingSourceIndicator) {
        this.fundingSourceIndicator = fundingSourceIndicator;
    }
    
    /** Getter for property comments.
     * @return Value of property comments.
     */
    public java.lang.String getComments() {
        return comments;
    }
    
    /** Setter for property comments.
     * @param comments New value of property comments.
     */
    public void setComments(java.lang.String comments) {
        this.comments = comments;
    }    
    
    /** Getter for property subContractAmountInfo.
     * @return Value of property subContractAmountInfo.
     */
    public CoeusVector getSubContractAmountInfo() {
        return subContractAmountInfo;
    }
    
    /** Setter for property subContractAmountInfo.
     * @param subContractAmountInfo New value of property subContractAmountInfo.
     */
    public void setSubContractAmountInfo(CoeusVector subContractAmountInfo) {
        this.subContractAmountInfo = subContractAmountInfo;
    }
    
    /** Getter for property subContractorName.
     * @return Value of property subContractorName.
     */
    public java.lang.String getSubContractorName() {
        return subContractorName;
    }
    
    /** Setter for property subContractorName.
     * @param subContractorName New value of property subContractorName.
     */
    public void setSubContractorName(java.lang.String subContractorName) {
        this.subContractorName = subContractorName;
    }
    
    /** Getter for property unitName.
     * @return Value of property unitName.
     */
    /*public java.lang.String getUnitName() {
        return unitName;
    }*/
    
    /** Setter for property unitName.
     * @param unitName New value of property unitName.
     */
    /*public void setUnitName(java.lang.String unitName) {
        this.unitName = unitName;
    }*/
    
    /** Getter for property subContractAmtReleased.
     * @return Value of property subContractAmtReleased.
     */
    public CoeusVector getSubContractAmtReleased() {
        return subContractAmtReleased;
    }
    
    /** Setter for property subContractAmtReleased.
     * @param subContractAmtReleased New value of property subContractAmtReleased.
     */
    public void setSubContractAmtReleased(CoeusVector subContractAmtReleased) {
        this.subContractAmtReleased = subContractAmtReleased;
    }
    
    /** Getter for property requisitionerName.
     * @return Value of property requisitionerName.
     */
    public java.lang.String getRequisitionerName() {
        return requisitionerName;
    }
    
    /** Setter for property requisitionerName.
     * @param requisitionerName New value of property requisitionerName.
     */
    public void setRequisitionerName(java.lang.String requisitionerName) {
        this.requisitionerName = requisitionerName;
    }
    
    /** Getter for property requisitionerUnitName.
     * @return Value of property requisitionerUnitName.
     */
    public java.lang.String getRequisitionerUnitName() {
        return requisitionerUnitName;
    }
    
    /** Setter for property requisitionerUnitName.
     * @param requisitionerUnitName New value of property requisitionerUnitName.
     */
    public void setRequisitionerUnitName(java.lang.String requisitionerUnitName) {
        this.requisitionerUnitName = requisitionerUnitName;
    }
    
    /** Getter for property subAwardTypeDescription.
     * @return Value of property subAwardTypeDescription.
     */
    public java.lang.String getSubAwardTypeDescription() {
        return subAwardTypeDescription;
    }
    
    /** Setter for property subAwardTypeDescription.
     * @param subAwardTypeDescription New value of property subAwardTypeDescription.
     */
    public void setSubAwardTypeDescription(java.lang.String subAwardTypeDescription) {
        this.subAwardTypeDescription = subAwardTypeDescription;
    }
    
    /** Getter for property statusDescription.
     * @return Value of property statusDescription.
     */
    public java.lang.String getStatusDescription() {
        return statusDescription;
    }
    
    /** Setter for property statusDescription.
     * @param statusDescription New value of property statusDescription.
     */
    public void setStatusDescription(java.lang.String statusDescription) {
        this.statusDescription = statusDescription;
    }
    public boolean equals(Object obj) {
        SubContractBean subContractBean = (SubContractBean)obj;
        if(subContractBean.getSubContractCode().equals(getSubContractCode()) && 
                subContractBean.getSequenceNumber() == getSequenceNumber()) {
            return true;       
        }else {
            return false;
        }
    }
    
    /**
     * Getter for property administrativeOfficerData.
     * @return Value of property administrativeOfficerData.
     */
    public edu.mit.coeus.utils.CoeusVector getAdministrativeOfficerData() {
        return administrativeOfficerData;
    }
    
    /**
     * Setter for property administrativeOfficerData.
     * @param administrativeOfficerData New value of property administrativeOfficerData.
     */
    public void setAdministrativeOfficerData(edu.mit.coeus.utils.CoeusVector administrativeOfficerData) {
        this.administrativeOfficerData = administrativeOfficerData;
    }
    
    /**
     * Getter for property auditorData.
     * @return Value of property auditorData.
     */
    public edu.mit.coeus.utils.CoeusVector getAuditorData() {
        return auditorData;
    }
    
    /**
     * Setter for property auditorData.
     * @param auditorData New value of property auditorData.
     */
    public void setAuditorData(edu.mit.coeus.utils.CoeusVector auditorData) {
        this.auditorData = auditorData;
    }
    
    /**
     * Getter for property ospAdminisratorData.
     * @return Value of property ospAdminisratorData.
     */
    public edu.mit.coeus.utils.CoeusVector getOspAdminisratorData() {
        return ospAdminisratorData;
    }
    
    /**
     * Setter for property ospAdminisratorData.
     * @param ospAdminisratorData New value of property ospAdminisratorData.
     */
    public void setOspAdminisratorData(edu.mit.coeus.utils.CoeusVector ospAdminisratorData) {
        this.ospAdminisratorData = ospAdminisratorData;
    }
    
    /**
     * Getter for property principalInvestigatorData.
     * @return Value of property principalInvestigatorData.
     */
    public edu.mit.coeus.utils.CoeusVector getPrincipalInvestigatorData() {
        return principalInvestigatorData;
    }
    
    /**
     * Setter for property principalInvestigatorData.
     * @param principalInvestigatorData New value of property principalInvestigatorData.
     */
    public void setPrincipalInvestigatorData(edu.mit.coeus.utils.CoeusVector principalInvestigatorData) {
        this.principalInvestigatorData = principalInvestigatorData;
    }
        public int getSiteInvestigator(){
        return siteInvestigator;
    }
    /** Setter for property siteInvestigator
     *
     * @param inSiteInvestigator New value of property siteInvestigator
     */
    public void setSiteInvestigator(int inSiteInvestigator){
        siteInvestigator = inSiteInvestigator;
    }

    /**
     * Getter for property siteInvestigatorName
     * @return Value of property siteInvestigatorName
     */
    public String getSiteInvestigatorName(){
        return siteInvestigatorName;
    }
    /**
     * Setter for property siteInvestigatorName
     * @param inSiteInvestigatorName New value of property siteInvestigatorName
     */
    public void setSiteInvestigatorName(String inSiteInvestigatorName){
        siteInvestigatorName = inSiteInvestigatorName;
    }

    public String getNegotiationNumber() {
        return negotiationNumber;
    }

    public void setNegotiationNumber(String negotiationNumber) {
        this.negotiationNumber = negotiationNumber;
    }

    public int getCostType() {
        return costType;
    }

    public void setCostType(int costType) {
        this.costType = costType;
    }

    public java.sql.Date getDateOfFullyExecuted() {
        return dateOfFullyExecuted;
    }

    public void setDateOfFullyExecuted(java.sql.Date dateOfFullyExecuted) {
        this.dateOfFullyExecuted = dateOfFullyExecuted;
    }

    public String getRequistionNumber() {
        return requistionNumber;
    }

    public void setRequistionNumber(String requistionNumber) {
        this.requistionNumber = requistionNumber;
    }

    public String getCostTypeDescription() {
        return costTypeDescription;
    }

    public void setCostTypeDescription(String costTypeDescription) {
        this.costTypeDescription = costTypeDescription;
    }

    public String getReportsIndicator() {
        return reportsIndicator;
    }

    public void setReportsIndicator(String reportsIndicator) {
        this.reportsIndicator = reportsIndicator;
    }
}