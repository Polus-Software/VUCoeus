/*
 * AwardBudgetCopyBean.java
 *
 * Created on July 18, 2005, 10:27 AM
 */

package edu.mit.coeus.award.bean;

import java.util.Date;

/**
 *
 * @author  chandrashekara
 */
public class AwardBudgetCopyBean extends AwardBaseBean{
    private String proposalNumber;
    private int versionNumber;
    private int budgetPeriod;
    private int amountSequenceNumber;
    private Date startDate;
    private Date endDate;
    private double totalCost;
    
    
    /** Creates a new instance of AwardBudgetCopyBean */
    public AwardBudgetCopyBean() {
    }
    
    /**
     * Getter for property proposalNumber.
     * @return Value of property proposalNumber.
     */
    public java.lang.String getProposalNumber() {
        return proposalNumber;
    }
    
    /**
     * Setter for property proposalNumber.
     * @param proposalNumber New value of property proposalNumber.
     */
    public void setProposalNumber(java.lang.String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
    /**
     * Getter for property versionNumber.
     * @return Value of property versionNumber.
     */
    public int getVersionNumber() {
        return versionNumber;
    }
    
    /**
     * Setter for property versionNumber.
     * @param versionNumber New value of property versionNumber.
     */
    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }
    
    /**
     * Getter for property budgetPeriod.
     * @return Value of property budgetPeriod.
     */
    public int getBudgetPeriod() {
        return budgetPeriod;
    }
    
    /**
     * Setter for property budgetPeriod.
     * @param budgetPeriod New value of property budgetPeriod.
     */
    public void setBudgetPeriod(int budgetPeriod) {
        this.budgetPeriod = budgetPeriod;
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
     * Getter for property endDate.
     * @return Value of property endDate.
     */
    public Date getEndDate() {
        return endDate;
    }
    
    /**
     * Setter for property endDate.
     * @param endDate New value of property endDate.
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
    
}
