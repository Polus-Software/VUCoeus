/*
 * AwardBudgetHeaderBean.java
 *
 * Created on August 8, 2005, 6:57 PM
 */

package edu.mit.coeus.award.bean;

import java.sql.Date;

/**
 *
 * @author  ajaygm
 */
public class AwardBudgetHeaderBean extends AwardBaseBean{
    
    private String accountNumber;
    private String comments;
    private String description;
    private int budgetStatusCode;
    private int awardBudgetTypeCode;
    private String budgetStatusDesc;
    private String budgetInitiator;
    private boolean onOffCampusFlag;
    private int versionNo;
    private int ohRateClassCode;
    private int ohRateTypeCode;
    private int amountSequenceNo;
    private double oblChangeAmount;
    private double oblDisributableAmount;
    private double totalCost;
    private Date startDate;
    private Date endDate;
    
    
    
    /** Creates a new instance of AwardBudgetHeaderBean */
    public AwardBudgetHeaderBean() {
    }
    
    /**
     * Getter for property accountNumber.
     * @return Value of property accountNumber.
     */
    public java.lang.String getAccountNumber() {
        return accountNumber;
    }
    
    /**
     * Setter for property accountNumber.
     * @param accountNumber New value of property accountNumber.
     */
    public void setAccountNumber(java.lang.String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
   
    /**
     * Getter for property comments.
     * @return Value of property comments.
     */
    public java.lang.String getComments() {
        return comments;
    }
    
    /**
     * Setter for property comments.
     * @param comments New value of property comments.
     */
    public void setComments(java.lang.String comments) {
        this.comments = comments;
    }
    
    /**
     * Getter for property budgetStatusCode.
     * @return Value of property budgetStatusCode.
     */
    public int getBudgetStatusCode() {
        return budgetStatusCode;
    }
    
    /**
     * Setter for property budgetStatusCode.
     * @param budgetStatusCode New value of property budgetStatusCode.
     */
    public void setBudgetStatusCode(int budgetStatusCode) {
        this.budgetStatusCode = budgetStatusCode;
    }
    
  
    
    /**
     * Getter for property startDate.
     * @return Value of property startDate.
     */
    public java.sql.Date getStartDate() {
        return startDate;
    }
    
    /**
     * Setter for property startDate.
     * @param startDate New value of property startDate.
     */
    public void setStartDate(java.sql.Date startDate) {
        this.startDate = startDate;
    }
    
    /**
     * Getter for property endDate.
     * @return Value of property endDate.
     */
    public java.sql.Date getEndDate() {
        return endDate;
    }
    
    /**
     * Setter for property endDate.
     * @param endDate New value of property endDate.
     */
    public void setEndDate(java.sql.Date endDate) {
        this.endDate = endDate;
    }
    
    /**
     * Getter for property oblChangeAmount.
     * @return Value of property oblChangeAmount.
     */
    public double getOblChangeAmount() {
        return oblChangeAmount;
    }
    
    /**
     * Setter for property oblChangeAmount.
     * @param oblChangeAmount New value of property oblChangeAmount.
     */
    public void setOblChangeAmount(double oblChangeAmount) {
        this.oblChangeAmount = oblChangeAmount;
    }
    
    /**
     * Getter for property oblDisributableAmount.
     * @return Value of property oblDisributableAmount.
     */
    public double getOblDisributableAmount() {
        return oblDisributableAmount;
    }
    
    /**
     * Setter for property oblDisributableAmount.
     * @param oblDisributableAmount New value of property oblDisributableAmount.
     */
    public void setOblDisributableAmount(double oblDisributableAmount) {
        this.oblDisributableAmount = oblDisributableAmount;
    }
    
    /**
     * Getter for property ohRateClassCode.
     * @return Value of property ohRateClassCode.
     */
    public int getOhRateClassCode() {
        return ohRateClassCode;
    }
    
    /**
     * Setter for property ohRateClassCode.
     * @param ohRateClassCode New value of property ohRateClassCode.
     */
    public void setOhRateClassCode(int ohRateClassCode) {
        this.ohRateClassCode = ohRateClassCode;
    }
    
    /**
     * Getter for property ohRateTypeCode.
     * @return Value of property ohRateTypeCode.
     */
    public int getOhRateTypeCode() {
        return ohRateTypeCode;
    }
    
    /**
     * Setter for property ohRateTypeCode.
     * @param ohRateTypeCode New value of property ohRateTypeCode.
     */
    public void setOhRateTypeCode(int ohRateTypeCode) {
        this.ohRateTypeCode = ohRateTypeCode;
    }
    
    /**
     * Getter for property versionNo.
     * @return Value of property versionNo.
     */
    public int getVersionNo() {
        return versionNo;
    }
    
    /**
     * Setter for property versionNo.
     * @param versionNo New value of property versionNo.
     */
    public void setVersionNo(int versionNo) {
        this.versionNo = versionNo;
    }
    
    /**
     * Getter for property amountSequenceNo.
     * @return Value of property amountSequenceNo.
     */
    public int getAmountSequenceNo() {
        return amountSequenceNo;
    }
    
    /**
     * Setter for property amountSequenceNo.
     * @param amountSequenceNo New value of property amountSequenceNo.
     */
    public void setAmountSequenceNo(int amountSequenceNo) {
        this.amountSequenceNo = amountSequenceNo;
    }
    
    /**
     * Getter for property budgetStatusDesc.
     * @return Value of property budgetStatusDesc.
     */
    public java.lang.String getBudgetStatusDesc() {
        return budgetStatusDesc;
    }
    
    /**
     * Setter for property budgetStatusDesc.
     * @param budgetStatusDesc New value of property budgetStatusDesc.
     */
    public void setBudgetStatusDesc(java.lang.String budgetStatusDesc) {
        this.budgetStatusDesc = budgetStatusDesc;
    }
    
    /**
     * Getter for property totalCost.
     * @return Value of property totalCost.
     */
    public double getTotalCost() {
        return totalCost;
    }
    
    /**
     * Setter for property totalCost.
     * @param totalCost New value of property totalCost.
     */
    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
    
    /**
     * Getter for property awardBudgetTypeCode.
     * @return Value of property awardBudgetTypeCode.
     */
    public int getAwardBudgetTypeCode() {
        return awardBudgetTypeCode;
    }
    
    /**
     * Setter for property awardBudgetTypeCode.
     * @param awardBudgetTypeCode New value of property awardBudgetTypeCode.
     */
    public void setAwardBudgetTypeCode(int awardBudgetTypeCode) {
        this.awardBudgetTypeCode = awardBudgetTypeCode;
    }
    
    /**
     * Getter for property onOffCampusFlag.
     * @return Value of property onOffCampusFlag.
     */
    public boolean isOnOffCampusFlag() {
        return onOffCampusFlag;
    }
    
    /**
     * Setter for property onOffCampusFlag.
     * @param onOffCampusFlag New value of property onOffCampusFlag.
     */
    public void setOnOffCampusFlag(boolean onOffCampusFlag) {
        this.onOffCampusFlag = onOffCampusFlag;
    }
    
    /**
     * Getter for property budgetInitiator.
     * @return Value of property budgetInitiator.
     */
    public java.lang.String getBudgetInitiator() {
        return budgetInitiator;
    }
    
    /**
     * Setter for property budgetInitiator.
     * @param budgetInitiator New value of property budgetInitiator.
     */
    public void setBudgetInitiator(java.lang.String budgetInitiator) {
        this.budgetInitiator = budgetInitiator;
    }
    
    /**
     * Getter for property description.
     * @return Value of property description.
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /**
     * Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
}
