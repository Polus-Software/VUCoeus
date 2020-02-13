/**
 * @(#)AwardIDCRateBean.java 1.0 01/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;
import java.util.Vector;
import java.sql.Date;

/**
 * This class is used to hold Award IDC Rates
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on March 05, 2004 12:30 PM
 */

public class AwardIDCRateBean extends AwardBaseBean{
    
    private double applicableIDCRate;
    private int idcRateTypeCode;
    private String fiscalYear;
    private boolean onOffCampusFlag = true;
    private double underRecoveryOfIDC;
    private String sourceAccount;
    private String destinationAccount;
    private Date startDate;
    private Date endDate;
    private double aw_ApplicableIDCRate;
    private int aw_IdcRateTypeCode;
    private String aw_FiscalYear;
    private boolean aw_OnOffCampusFlag;
    private String aw_SourceAccount;
    private String aw_DestinationAccount;
    private Date aw_StartDate;
    private int rowId;
    /**
     *	Default Constructor
     */
    
    public AwardIDCRateBean(){
    }    
    
    /** Getter for property applicableIDCRate.
     * @return Value of property applicableIDCRate.
     */
    public double getApplicableIDCRate() {
        return applicableIDCRate;
    }
    
    /** Setter for property applicableIDCRate.
     * @param applicableIDCRate New value of property applicableIDCRate.
     */
    public void setApplicableIDCRate(double applicableIDCRate) {
        this.applicableIDCRate = applicableIDCRate;
    }
    
    /** Getter for property idcRateTypeCode.
     * @return Value of property idcRateTypeCode.
     */
    public int getIdcRateTypeCode() {
        return idcRateTypeCode;
    }
    
    /** Setter for property idcRateTypeCode.
     * @param idcRateTypeCode New value of property idcRateTypeCode.
     */
    public void setIdcRateTypeCode(int idcRateTypeCode) {
        this.idcRateTypeCode = idcRateTypeCode;
    }
    
    /** Getter for property fiscalYear.
     * @return Value of property fiscalYear.
     */
    public java.lang.String getFiscalYear() {
        return fiscalYear;
    }
    
    /** Setter for property fiscalYear.
     * @param fiscalYear New value of property fiscalYear.
     */
    public void setFiscalYear(java.lang.String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }
    
    /** Getter for property onOffCampusFlag.
     * @return Value of property onOffCampusFlag.
     */
    public boolean isOnOffCampusFlag() {
        return onOffCampusFlag;
    }
    
    /** Setter for property onOffCampusFlag.
     * @param onOffCampusFlag New value of property onOffCampusFlag.
     */
    public void setOnOffCampusFlag(boolean onOffCampusFlag) {
        this.onOffCampusFlag = onOffCampusFlag;
    }
    
    /** Getter for property underRecoveryOfIDC.
     * @return Value of property underRecoveryOfIDC.
     */
    public double getUnderRecoveryOfIDC() {
        return underRecoveryOfIDC;
    }
    
    /** Setter for property underRecoveryOfIDC.
     * @param underRecoveryOfIDC New value of property underRecoveryOfIDC.
     */
    public void setUnderRecoveryOfIDC(double underRecoveryOfIDC) {
        this.underRecoveryOfIDC = underRecoveryOfIDC;
    }
    
    /** Getter for property sourceAccount.
     * @return Value of property sourceAccount.
     */
    public java.lang.String getSourceAccount() {
        return sourceAccount;
    }
    
    /** Setter for property sourceAccount.
     * @param sourceAccount New value of property sourceAccount.
     */
    public void setSourceAccount(java.lang.String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }
    
    /** Getter for property destinationAccount.
     * @return Value of property destinationAccount.
     */
    public java.lang.String getDestinationAccount() {
        return destinationAccount;
    }
    
    /** Setter for property destinationAccount.
     * @param destinationAccount New value of property destinationAccount.
     */
    public void setDestinationAccount(java.lang.String destinationAccount) {
        this.destinationAccount = destinationAccount;
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
    
    /** Getter for property aw_ApplicableIDCRate.
     * @return Value of property aw_ApplicableIDCRate.
     *
     */
    public double getAw_ApplicableIDCRate() {
        return aw_ApplicableIDCRate;
    }
    
    /** Setter for property aw_ApplicableIDCRate.
     * @param aw_ApplicableIDCRate New value of property aw_ApplicableIDCRate.
     *
     */
    public void setAw_ApplicableIDCRate(double aw_ApplicableIDCRate) {
        this.aw_ApplicableIDCRate = aw_ApplicableIDCRate;
    }
    
    /** Getter for property aw_IdcRateTypeCode.
     * @return Value of property aw_IdcRateTypeCode.
     *
     */
    public int getAw_IdcRateTypeCode() {
        return aw_IdcRateTypeCode;
    }
    
    /** Setter for property aw_IdcRateTypeCode.
     * @param aw_IdcRateTypeCode New value of property aw_IdcRateTypeCode.
     *
     */
    public void setAw_IdcRateTypeCode(int aw_IdcRateTypeCode) {
        this.aw_IdcRateTypeCode = aw_IdcRateTypeCode;
    }
    
    /** Getter for property aw_FiscalYear.
     * @return Value of property aw_FiscalYear.
     *
     */
    public java.lang.String getAw_FiscalYear() {
        return aw_FiscalYear;
    }
    
    /** Setter for property aw_FiscalYear.
     * @param aw_FiscalYear New value of property aw_FiscalYear.
     *
     */
    public void setAw_FiscalYear(java.lang.String aw_FiscalYear) {
        this.aw_FiscalYear = aw_FiscalYear;
    }
    
    /** Getter for property aw_OnOffCampusFlag.
     * @return Value of property aw_OnOffCampusFlag.
     *
     */
    public boolean isAw_OnOffCampusFlag() {
        return aw_OnOffCampusFlag;
    }
    
    /** Setter for property aw_OnOffCampusFlag.
     * @param aw_OnOffCampusFlag New value of property aw_OnOffCampusFlag.
     *
     */
    public void setAw_OnOffCampusFlag(boolean aw_OnOffCampusFlag) {
        this.aw_OnOffCampusFlag = aw_OnOffCampusFlag;
    }
    
    /** Getter for property aw_SourceAccount.
     * @return Value of property aw_SourceAccount.
     *
     */
    public java.lang.String getAw_SourceAccount() {
        return aw_SourceAccount;
    }
    
    /** Setter for property aw_SourceAccount.
     * @param aw_SourceAccount New value of property aw_SourceAccount.
     *
     */
    public void setAw_SourceAccount(java.lang.String aw_SourceAccount) {
        this.aw_SourceAccount = aw_SourceAccount;
    }
    
    /** Getter for property aw_DestinationAccount.
     * @return Value of property aw_DestinationAccount.
     *
     */
    public java.lang.String getAw_DestinationAccount() {
        return aw_DestinationAccount;
    }
    
    /** Setter for property aw_DestinationAccount.
     * @param aw_DestinationAccount New value of property aw_DestinationAccount.
     *
     */
    public void setAw_DestinationAccount(java.lang.String aw_DestinationAccount) {
        this.aw_DestinationAccount = aw_DestinationAccount;
    }
    
    /** Getter for property aw_StartDate.
     * @return Value of property aw_StartDate.
     *
     */
    public java.sql.Date getAw_StartDate() {
        return aw_StartDate;
    }
    
    /** Setter for property aw_StartDate.
     * @param aw_StartDate New value of property aw_StartDate.
     *
     */
    public void setAw_StartDate(java.sql.Date aw_StartDate) {
        this.aw_StartDate = aw_StartDate;
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
    
      // Equals implementation
    public boolean equals(Object obj) {
        AwardIDCRateBean awardIDCRateBean= (AwardIDCRateBean)obj;            
        if(awardIDCRateBean.getMitAwardNumber().equals(getMitAwardNumber()) &&
            awardIDCRateBean.getSequenceNumber() == getSequenceNumber()&&
            awardIDCRateBean.getRowId() == getRowId()) {
            return true;
        }else{
            return false;
        }
    }
    
}