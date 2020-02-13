/*
 * @(#)ProposalCostSharingBean.java 1.0 October 08, 2003, 11:55 AM
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

/** This class is used to hold Cost Sharing Rates of a Budget Version 
 *
 * @version :1.0 October 08, 2003, 11:55 AM
 * @author Prasanna Kumar K
 */

public class ProposalCostSharingBean extends BudgetBean implements PrimaryKey{
    
    //Holds Row Id. 
    //This is used to identify the record when the user is allowed to 
    //modify Primary Key values.
    private int rowId;
    
    //holds fiscal year
    private String fiscalYear = null;    
    
    //holds cost sharing percentage
    private double costSharingPercentage;
    
    //holds Source Account
    private String sourceAccount = null;    
    
    //holds amount
    private double amount;
    
    //holds aw fiscal year
    private String aw_FiscalYear = null;
   
    //holds aw Source Account
    private String aw_SourceAccount = null;
    
    public ProposalCostSharingBean(){
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
        if(comparableBean instanceof ProposalCostSharingBean){
            ProposalCostSharingBean proposalCostSharingBean = (ProposalCostSharingBean)comparableBean;
            //Proposal Number
            if(proposalCostSharingBean.getProposalNumber()!=null){
                if(getProposalNumber().equals(proposalCostSharingBean.getProposalNumber())){
                    success = true;
                }else{
                    return false;
                }
            }
            //Version Number
            if(proposalCostSharingBean.getVersionNumber()!=-1){
                if(getVersionNumber() == proposalCostSharingBean.getVersionNumber()){
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
        // Added for COEUSDEV-1125 Unable to save data in Project Year column of Dev Prop Cost Sharing window - Start
        if(fiscalYear != null && !fiscalYear.equals("")){
            fiscalYear = fiscalYear.trim();
        }
        // COEUSDEV-1125 - End
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
        // Added for COEUSDEV-1125 Unable to save data in Project Year column of Dev Prop Cost Sharing window - Start
        if(sourceAccount != null && !sourceAccount.equals("")){
            sourceAccount = sourceAccount.trim();
        }
        // COEUSDEV-1125 - End
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
    
      // Added by Chandra 04/10/2003 - start
    public boolean equals(Object obj) {
        ProposalCostSharingBean proposalCostSharingBean = (ProposalCostSharingBean)obj;
//        if(proposalCostSharingBean.getProposalNumber().equals(getProposalNumber()) &&
//            proposalCostSharingBean.getVersionNumber() == getVersionNumber() &&
//            (proposalCostSharingBean.getAw_FiscalYear()!=null && getAw_FiscalYear() != null && proposalCostSharingBean.getAw_FiscalYear().trim().equals(getAw_FiscalYear().trim())) &&
//            (proposalCostSharingBean.getAw_SourceAccount()!=null && getAw_SourceAccount() != null && proposalCostSharingBean.getAw_SourceAccount().trim().equals(getAw_SourceAccount().trim()))
//            ) {  
        
        if(proposalCostSharingBean.getProposalNumber().equals(getProposalNumber()) &&
            proposalCostSharingBean.getVersionNumber() == getVersionNumber()&&
            proposalCostSharingBean.getRowId() == getRowId()) {
            return true;
        } else {
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