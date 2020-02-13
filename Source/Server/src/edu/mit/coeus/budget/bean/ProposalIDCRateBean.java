/*
 * @(#)ProposalIDCRateBean.java 1.0 October 08, 2003, 11:55 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.budget.bean;

import java.util.*;
import java.sql.Date;
import edu.mit.coeus.bean.PrimaryKey;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;

/** This class is used to hold IDC Rates of a Budget Version
 *
 * @version :1.0 October 08, 2003, 11:55 AM
 * @author Prasanna Kumar K
 */

public class ProposalIDCRateBean extends BudgetBean implements PrimaryKey{
    
    //Holds Row Id. 
    //This is used to identify the record when the user is allowed to 
    //modify Primary Key values.
    private int rowId;
    
    //holds fiscal year
    private String fiscalYear = null;    
    
    //holds cost sharing percentage
    private double applicableIDCRate;
    
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
    
    public ProposalIDCRateBean(){
    }
    
    /**
     * This method is used to return the Primary key object of the bean class.
     * @return Object Primary key Object
     */
    public Object getPrimaryKey() {
        return new Integer(getVersionNumber());
    }
    
    /**
     * @param coeusBean
     * @throws CoeusException
     * @return  */    
    public boolean isLike(ComparableBean comparableBean) throws CoeusException{
        boolean success = false;
        if(comparableBean instanceof BudgetInfoBean){
            BudgetInfoBean budgetInfoBean = (BudgetInfoBean)comparableBean;
            //Proposal Number
            if(budgetInfoBean.getProposalNumber()!=null){
                if(getProposalNumber().equals(budgetInfoBean.getProposalNumber())){
                    success = true;
                }else{
                    return false;
                }
            }
            //Version Number
            if(budgetInfoBean.getVersionNumber()!=-1){
                if(getVersionNumber() == budgetInfoBean.getVersionNumber()){
                    success = true;
                }else{
                    return false;
                }
            }          
        }else{
            throw new CoeusException("budget_exception.1000");
        }
        return success;        
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
    
     // Added by Chandra 04/10/2003 - start
    public boolean equals(Object obj) {
        ProposalIDCRateBean proposalIDCRateBean = (ProposalIDCRateBean)obj;
//        if( proposalIDCRateBean.getAw_SourceAccount() == null ) return false;
//        if(proposalIDCRateBean.getProposalNumber().equals(getProposalNumber()) &&
//        proposalIDCRateBean.getVersionNumber() == getVersionNumber()&&
//        proposalIDCRateBean.getAw_FiscalYear()!=null && getAw_FiscalYear() != null && proposalIDCRateBean.getAw_FiscalYear().trim().equals(getAw_FiscalYear().trim()) &&
//        proposalIDCRateBean.getAw_SourceAccount()!=null && getAw_SourceAccount()!=null && proposalIDCRateBean.getAw_SourceAccount().trim().equals(getAw_SourceAccount().trim())&&
//        proposalIDCRateBean.getAw_ApplicableIDCRate() == getAw_ApplicableIDCRate()){
            
        if(proposalIDCRateBean.getProposalNumber().equals(getProposalNumber()) &&
            proposalIDCRateBean.getVersionNumber() == getVersionNumber()&&
            proposalIDCRateBean.getRowId() == getRowId()) {
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
    
     // Added by Chandra 04/10/2003 - end
    
} // end BudgetInfoBean
