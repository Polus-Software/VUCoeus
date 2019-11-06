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

/** This class is used to hold cost sharing of a Institute Proposal
 *
 * @version :1.0 April 26, 2004, 11:55 AM
 * @author Prasanna Kumar K
 */

public class InstituteProposalCostSharingBean extends InstituteProposalBaseBean{
    
    //Holds Row Id.
    //This is used to identify the record when the user is allowed to
    //modify Primary Key values.
    private int rowId;
    //holds fiscal year
    private String fiscalYear = null;
    //holds cost sharing percentage
    private double costSharingPercentage;
    //holds cost sharing type code
    private int costSharingTypeCode;
    //holds cost sharing type description
    private String costSharingTypeDesc;
    //holds Source Account
    private String sourceAccount;
    //holds Amount
    private double amount;
    //holds aw fiscal year
    private String aw_FiscalYear = null;
    //holds aw cost sharing type code
    private int aw_CostSharingTypeCode;
    //holds aw Source Account
    private String aw_SourceAccount = null;
    
    public InstituteProposalCostSharingBean(){
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
    
    /** Getter for property costSharingPercentage.
     * @return Value of property costSharingPercentage.
     */
    public double getCostSharingPercentage() {
        return costSharingPercentage;
    }
    
    /** Setter for property costSharingPercentage.
     * @param costSharingPercentage New value of property costSharingPercentage.
     */
    public void setCostSharingPercentage(double costSharingPercentage) {
        this.costSharingPercentage = costSharingPercentage;
    }
    
    /** Getter for property costSharingTypeCode.
     * @return Value of property costSharingTypeCode.
     */
    public int getCostSharingTypeCode() {
        return costSharingTypeCode;
    }
    
    /** Setter for property costSharingTypeCode.
     * @param costSharingTypeCode New value of property costSharingTypeCode.
     */
    public void setCostSharingTypeCode(int costSharingTypeCode) {
        this.costSharingTypeCode = costSharingTypeCode;
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
    
    /** Getter for property amount.
     * @return Value of property amount.
     */
    public double getAmount() {
        return amount;
    }
    
    /** Setter for property amount.
     * @param amount New value of property amount.
     */
    public void setAmount(double amount) {
        this.amount = amount;
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
    
    /** Getter for property aw_CostSharingTypeCode.
     * @return Value of property aw_CostSharingTypeCode.
     */
    public int getAw_CostSharingTypeCode() {
        return aw_CostSharingTypeCode;
    }
    
    /** Setter for property aw_CostSharingTypeCode.
     * @param aw_CostSharingTypeCode New value of property aw_CostSharingTypeCode.
     */
    public void setAw_CostSharingTypeCode(int aw_CostSharingTypeCode) {
        this.aw_CostSharingTypeCode = aw_CostSharingTypeCode;
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
    
     // Equals implementation
    public boolean equals(Object obj) {
        InstituteProposalCostSharingBean instituteProposalCostSharingBean = (InstituteProposalCostSharingBean)obj;            
        if(instituteProposalCostSharingBean.getProposalNumber().equals(getProposalNumber()) &&
            instituteProposalCostSharingBean.getSequenceNumber() == getSequenceNumber()&&
            instituteProposalCostSharingBean.getRowId() == getRowId()) {
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * Getter for property costSharingTypeDesc.
     * @return Value of property costSharingTypeDesc.
     */
    public java.lang.String getCostSharingTypeDesc() {
        return costSharingTypeDesc;
    }
    
    /**
     * Setter for property costSharingTypeDesc.
     * @param costSharingTypeDesc New value of property costSharingTypeDesc.
     */
    public void setCostSharingTypeDesc(java.lang.String costSharingTypeDesc) {
        this.costSharingTypeDesc = costSharingTypeDesc;
    }
    
} // end