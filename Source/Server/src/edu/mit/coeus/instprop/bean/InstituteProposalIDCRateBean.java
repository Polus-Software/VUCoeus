/*
 * @(#)InstituteProposalIDCRateBean.java 1.0 April 26, 2004, 11:55 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.instprop.bean;

import java.util.*;
import java.sql.Date;
import edu.mit.coeus.bean.PrimaryKey;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;

/** This class is used to hold IDC Rates of a Institute Proposal
 *
 * @version :1.0 April 26, 2004, 11:55 AM
 * @author Prasanna Kumar K
 */

public class InstituteProposalIDCRateBean extends InstituteProposalBaseBean{
    
    //Holds Row Id.
    //This is used to identify the record when the user is allowed to
    //modify Primary Key values.
    private int rowId;
    //holds cost sharing percentage
    private double applicableIDCRate;
    //holds idc rate type code
    private int idcRateTypeCode;
    //holds idc rate type description
    private String idcRateTypeDesc;
    //holds fiscal year
    private String fiscalYear = null;
    //holds the on Off Campus flag
    private boolean onOffCampusFlag;
    //holds Under Recovery of IDC
    private double underRecoveryIDC;
    //holds Source Account
    private String sourceAccount = null;
    //holds aw fiscal year
    private String aw_FiscalYear = null;
    //holds aw cost sharing percentage
    private double aw_ApplicableIDCRate;
    //holds aw on Off Campus flag
    private boolean aw_OnOffCampusFlag;
    //holds aw Source Account
    private String aw_SourceAccount = null;
    //holds idc rate type code
    private int aw_IdcRateTypeCode;    
    
    public InstituteProposalIDCRateBean(){
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
    
    /** Getter for property underRecoveryIDC.
     * @return Value of property underRecoveryIDC.
     */
    public double getUnderRecoveryIDC() {
        return underRecoveryIDC;
    }
    
    /** Setter for property underRecoveryIDC.
     * @param underRecoveryIDC New value of property underRecoveryIDC.
     */
    public void setUnderRecoveryIDC(double underRecoveryIDC) {
        this.underRecoveryIDC = underRecoveryIDC;
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
    
     // Equals implementation
    public boolean equals(Object obj) {
        InstituteProposalIDCRateBean instituteProposalIDCRateBean = (InstituteProposalIDCRateBean)obj;            
        if(instituteProposalIDCRateBean.getProposalNumber().equals(getProposalNumber()) &&
            instituteProposalIDCRateBean.getSequenceNumber() == getSequenceNumber()&&
            instituteProposalIDCRateBean.getRowId() == getRowId()) {
            return true;
        }else{
            return false;
        }
    }
    
    /** Getter for property aw_FiscalYear.
     * @return Value of property aw_FiscalYear.
     */
    public java.lang.String getAw_FiscalYear() {
        return aw_FiscalYear;
    }
    
    /** Setter for property aw_FiscalYear.
     * @param aw_FiscalYear New value of property aw_FiscalYear.
     */
    public void setAw_FiscalYear(java.lang.String aw_FiscalYear) {
        this.aw_FiscalYear = aw_FiscalYear;
    }
    
    /** Getter for property aw_ApplicableIDCRate.
     * @return Value of property aw_ApplicableIDCRate.
     */
    public double getAw_ApplicableIDCRate() {
        return aw_ApplicableIDCRate;
    }
    
    /** Setter for property aw_ApplicableIDCRate.
     * @param aw_ApplicableIDCRate New value of property aw_ApplicableIDCRate.
     */
    public void setAw_ApplicableIDCRate(double aw_ApplicableIDCRate) {
        this.aw_ApplicableIDCRate = aw_ApplicableIDCRate;
    }
    
    /** Getter for property aw_OnOffCampusFlag.
     * @return Value of property aw_OnOffCampusFlag.
     */
    public boolean isAw_OnOffCampusFlag() {
        return aw_OnOffCampusFlag;
    }
    
    /** Setter for property aw_OnOffCampusFlag.
     * @param aw_OnOffCampusFlag New value of property aw_OnOffCampusFlag.
     */
    public void setAw_OnOffCampusFlag(boolean aw_OnOffCampusFlag) {
        this.aw_OnOffCampusFlag = aw_OnOffCampusFlag;
    }
    
    /** Getter for property aw_SourceAccount.
     * @return Value of property aw_SourceAccount.
     */
    public java.lang.String getAw_SourceAccount() {
        return aw_SourceAccount;
    }
    
    /** Setter for property aw_SourceAccount.
     * @param aw_SourceAccount New value of property aw_SourceAccount.
     */
    public void setAw_SourceAccount(java.lang.String aw_SourceAccount) {
        this.aw_SourceAccount = aw_SourceAccount;
    }
    
    /** Getter for property rowId.
     * @return Value of property rowId.
     */
    public int getRowId() {
        return rowId;
    }
    
    /** Setter for property rowId.
     * @param rowId New value of property rowId.
     */
    public void setRowId(int rowId) {
        this.rowId = rowId;
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
    
    /** Getter for property aw_IdcRateTypeCode.
     * @return Value of property aw_IdcRateTypeCode.
     */
    public int getAw_IdcRateTypeCode() {
        return aw_IdcRateTypeCode;
    }
    
    /** Setter for property aw_IdcRateTypeCode.
     * @param aw_IdcRateTypeCode New value of property aw_IdcRateTypeCode.
     */
    public void setAw_IdcRateTypeCode(int aw_IdcRateTypeCode) {
        this.aw_IdcRateTypeCode = aw_IdcRateTypeCode;
    }    
    
    /**
     * Getter for property idcRateTypeDesc.
     * @return Value of property idcRateTypeDesc.
     */
    public String getIdcRateTypeDesc() {
        return idcRateTypeDesc;
    }
    
    /**
     * Setter for property idcRateTypeDesc.
     * @param idcRateTypeDesc New value of property idcRateTypeDesc.
     */
    public void setIdcRateTypeDesc(java.lang.String idcRateTypeDesc) {
        this.idcRateTypeDesc = idcRateTypeDesc;
    }
    
} // end