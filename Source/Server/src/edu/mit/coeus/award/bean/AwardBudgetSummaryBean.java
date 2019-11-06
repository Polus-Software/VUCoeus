/*
 * AwardBudgetSummaryBean.java
 *
 * Created on July 14, 2005, 7:22 PM
 */

package edu.mit.coeus.award.bean;

import java.util.Date;

/**
 *
 * @author  chandrashekara
 */
public class AwardBudgetSummaryBean extends AwardBaseBean{
    private int budgetVersion;
    private Date startDate;
    private Date expirationDate;
    private int budgetStatusCode;
    private String budgetStatusDescription;
    private int awardBudgetTypeCode;
    private String awardBudgetTypeDesc;
    private double budgetAmount;
    private int amountSequenceNumber;
    
    
    /** Creates a new instance of AwardBudgetSummaryBean */
    public AwardBudgetSummaryBean() {
    }
    
    /**
     * Getter for property budgetVersion.
     * @return Value of property budgetVersion.
     */
    public int getBudgetVersion() {
        return budgetVersion;
    }
    
    /**
     * Setter for property budgetVersion.
     * @param budgetVersion New value of property budgetVersion.
     */
    public void setBudgetVersion(int budgetVersion) {
        this.budgetVersion = budgetVersion;
    }
    
    /**
     * Getter for property startDate.
     * @return Value of property startDate.
     */
    public Date getStartDate() {
        return startDate;
    }
    
    /**
     * Setter for property startDate.
     * @param startDate New value of property startDate.
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    /**
     * Getter for property expirationDate.
     * @return Value of property expirationDate.
     */
    public Date getExpirationDate() {
        return expirationDate;
    }
    
    /**
     * Setter for property expirationDate.
     * @param expirationDate New value of property expirationDate.
     */
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
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
     * Getter for property budgetStatusDescription.
     * @return Value of property budgetStatusDescription.
     */
    public java.lang.String getBudgetStatusDescription() {
        return budgetStatusDescription;
    }
    
    /**
     * Setter for property budgetStatusDescription.
     * @param budgetStatusDescription New value of property budgetStatusDescription.
     */
    public void setBudgetStatusDescription(java.lang.String budgetStatusDescription) {
        this.budgetStatusDescription = budgetStatusDescription;
    }
    
    /**
     * Getter for property budgetAmount.
     * @return Value of property budgetAmount.
     */
    public double getBudgetAmount() {
        return budgetAmount;
    }
    
    /**
     * Setter for property budgetAmount.
     * @param budgetAmount New value of property budgetAmount.
     */
    public void setBudgetAmount(double budgetAmount) {
        this.budgetAmount = budgetAmount;
    }
    
    /**
     * Getter for property amountSequenceNumber.
     * @return Value of property amountSequenceNumber.
     */
    public int getAmountSequenceNumber() {
        return amountSequenceNumber;
    }
    
    /**
     * Setter for property amountSequenceNumber.
     * @param amountSequenceNumber New value of property amountSequenceNumber.
     */
    public void setAmountSequenceNumber(int amountSequenceNumber) {
        this.amountSequenceNumber = amountSequenceNumber;
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
     * Getter for property awardBudgetTypeDesc.
     * @return Value of property awardBudgetTypeDesc.
     */
    public java.lang.String getAwardBudgetTypeDesc() {
        return awardBudgetTypeDesc;
    }
    
    /**
     * Setter for property awardBudgetTypeDesc.
     * @param awardBudgetTypeDesc New value of property awardBudgetTypeDesc.
     */
    public void setAwardBudgetTypeDesc(java.lang.String awardBudgetTypeDesc) {
        this.awardBudgetTypeDesc = awardBudgetTypeDesc;
    }
    
}
