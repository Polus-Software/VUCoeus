/*
 * AwardtFNABean.java
 *
 * Created on September 19, 2005, 11:04 AM
 */

package edu.mit.coeus.award.bean;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author  vinayks
 */
public class AwardAmountFNABean extends AwardBaseBean {
    private int amountSequenceNumber;
    private int budgetPeriod;
    private Date startDate;
    private Date endDate;
    private double directCost;
    private double indirectCost;
    private int awBudgetPeriod;
    private int awAmtSeqNumber;
    private int rowId;
    
    
    
    
    /** Creates a new instance of AwardtFNABean */
    public AwardAmountFNABean() {
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
    
    
    public double getDirectCost() {
        return directCost;
    }    
    
    /**
     * Setter for property directCost.
     * @param directCost New value of property directCost.
     */
    public void setDirectCost(double directCost) {
        this.directCost = directCost;
    }    
    
    /**
     * Getter for property indirectCost.
     * @return Value of property indirectCost.
     */
    public double getIndirectCost() {
        return indirectCost;
    }
    
    /**
     * Setter for property indirectCost.
     * @param indirectCost New value of property indirectCost.
     */
    public void setIndirectCost(double indirectCost) {
        this.indirectCost = indirectCost;
    }
        
    /**
     * Getter for property rowId.
     * @return Value of property rowId.
     */
    public int getRowId() {
        return rowId;
    }
    
    /**
     * Setter for property rowId.
     * @param rowId New value of property rowId.
     */
    public void setRowId(int rowId) {
        this.rowId = rowId;
    }
    
    /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        if(super.equals(obj)){
            AwardAmountFNABean awardAmountFNABean = (AwardAmountFNABean)obj;
            if(awardAmountFNABean.getRowId() == getRowId()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    } 
    
    /**
     * Getter for property awBudgetPeriod.
     * @return Value of property awBudgetPeriod.
     */
    public int getAwBudgetPeriod() {
        return awBudgetPeriod;
    }
    
    /**
     * Setter for property awBudgetPeriod.
     * @param awBudgetPeriod New value of property awBudgetPeriod.
     */
    public void setAwBudgetPeriod(int awBudgetPeriod) {
        this.awBudgetPeriod = awBudgetPeriod;
    }
    
    /**
     * Getter for property awAmtSeqNumber.
     * @return Value of property awAmtSeqNumber.
     */
    public int getAwAmtSeqNumber() {
        return awAmtSeqNumber;
    }
    
    /**
     * Setter for property awAmtSeqNumber.
     * @param awAmtSeqNumber New value of property awAmtSeqNumber.
     */
    public void setAwAmtSeqNumber(int awAmtSeqNumber) {
        this.awAmtSeqNumber = awAmtSeqNumber;
    }
    
}
