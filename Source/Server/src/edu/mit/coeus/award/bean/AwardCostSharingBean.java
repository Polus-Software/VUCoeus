/**
 * @(#)AwardCostSharingBean.java 1.0 01/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;
import java.util.Vector;
import java.sql.Date;
/**
 * This class is used to hold Award Cost Sharing data
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on March 05, 2004 12:30 PM
 */

public class AwardCostSharingBean extends AwardBaseBean{
    
    private String fiscalYear;
    private double costSharingPercentage;
    private int costSharingType;
    private String sourceAccount;
    private String destinationAccount;
    private double amount;
    private int rowId;
    private String aw_FiscalYear;
    private int aw_CostSharingType;
    private String aw_SourceAccount;
    private String aw_DestinationAccount;    
    
    /**
     *	Default Constructor
     */
    
    public AwardCostSharingBean(){
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
    
    /** Getter for property costSharingType.
     * @return Value of property costSharingType.
     */
    public int getCostSharingType() {
        return costSharingType;
    }
    
    /** Setter for property costSharingType.
     * @param costSharingType New value of property costSharingType.
     */
    public void setCostSharingType(int costSharingType) {
        this.costSharingType = costSharingType;
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
    
     /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        if(super.equals(obj)){
            AwardCostSharingBean awardCostSharingBean = 
                    (AwardCostSharingBean)obj;
            if(awardCostSharingBean.getRowId() == getRowId()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
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
    
    /** Getter for property aw_CostSharingType.
     * @return Value of property aw_CostSharingType.
     *
     */
    public int getAw_CostSharingType() {
        return aw_CostSharingType;
    }
    
    /** Setter for property aw_CostSharingType.
     * @param aw_CostSharingType New value of property aw_CostSharingType.
     *
     */
    public void setAw_CostSharingType(int aw_CostSharingType) {
        this.aw_CostSharingType = aw_CostSharingType;
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
    
}