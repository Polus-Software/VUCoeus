/*
 * @(#)BudgetPeriodBean.java 1.0 09/26/03 11:23 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.budget.bean;

import java.sql.Date;
import java.util.Hashtable;
import edu.mit.coeus.bean.PrimaryKey;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;
/**
 * The class used to hold the information of <code>Budget Periods</code>
 *
 * @author  Prasanna Kumar
 * @version 1.0
 * Created on September 26, 2003, 9:58 AM
 */
public class BudgetPeriodBean extends BudgetBean implements PrimaryKey{
    
    //holds budgetPeriod
    private int budgetPeriod = -1;
    //holds startDate
    private Date startDate = null;
    //holds endDate
    private Date endDate = null;
    //holds totalCost
    private double totalCost;
    //holds totalDirectCost
    private double totalDirectCost;
    //holds totalIndirectCost
    private double totalIndirectCost;
    //holds costSharingAmount
    private double costSharingAmount;
    //holds underRecoveryAmount
    private double underRecoveryAmount;
    //holds totalCostLimit
    private double totalCostLimit;
    //holds comments
    private String comments = null;
    //holds aw_BudgetPeriod
    private int aw_BudgetPeriod = -1;
    //Code added for Case#3472 - Sync to Direct Cost Limit
    //For adding total direct cost limit
    private double totalDirectCostLimit;
    //Added for Case 3197 - Allow for the generation of project period greater than 12 months -start
    private double noOfPeriodMonths;
    //Added for Case 3197 - Allow for the generation of project period greater than 12 months - end
    
    /** Getter for property budgetPeriod.
     * @return Value of property budgetPeriod.
     */
    public int getBudgetPeriod() {
        return budgetPeriod;
    }
    
    /** Setter for property budgetPeriod.
     * @param budgetPeriod New value of property budgetPeriod.
     */
    public void setBudgetPeriod(int budgetPeriod) {
        this.budgetPeriod = budgetPeriod;
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
    
    /** Getter for property totalCost.
     * @return Value of property totalCost.
     */
    public double getTotalCost() {
        return totalCost;
    }
    
    /** Setter for property totalCost.
     * @param totalCost New value of property totalCost.
     */
    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
    
    /** Getter for property totalDirectCost.
     * @return Value of property totalDirectCost.
     */
    public double getTotalDirectCost() {
        return totalDirectCost;
    }
    
    /** Setter for property totalDirectCost.
     * @param totalDirectCost New value of property totalDirectCost.
     */
    public void setTotalDirectCost(double totalDirectCost) {
        this.totalDirectCost = totalDirectCost;
    }
    
    /** Getter for property totalIndirectCost.
     * @return Value of property totalIndirectCost.
     */
    public double getTotalIndirectCost() {
        return totalIndirectCost;
    }
    
    /** Setter for property totalIndirectCost.
     * @param totalIndirectCost New value of property totalIndirectCost.
     */
    public void setTotalIndirectCost(double totalIndirectCost) {
        this.totalIndirectCost = totalIndirectCost;
    }
    
    /** Getter for property costSharingAmount.
     * @return Value of property costSharingAmount.
     */
    public double getCostSharingAmount() {
        return costSharingAmount;
    }
    
    /** Setter for property costSharingAmount.
     * @param costSharingAmount New value of property costSharingAmount.
     */
    public void setCostSharingAmount(double costSharingAmount) {
        this.costSharingAmount = costSharingAmount;
    }
    
    /** Getter for property underRecoveryAmount.
     * @return Value of property underRecoveryAmount.
     */
    public double getUnderRecoveryAmount() {
        return underRecoveryAmount;
    }
    
    /** Setter for property underRecoveryAmount.
     * @param underRecoveryAmount New value of property underRecoveryAmount.
     */
    public void setUnderRecoveryAmount(double underRecoveryAmount) {
        this.underRecoveryAmount = underRecoveryAmount;
    }
    
    /** Getter for property totalCostLimit.
     * @return Value of property totalCostLimit.
     */
    public double getTotalCostLimit() {
        return totalCostLimit;
    }
    
    /** Setter for property totalCostLimit.
     * @param totalCostLimit New value of property totalCostLimit.
     */
    public void setTotalCostLimit(double totalCostLimit) {
        this.totalCostLimit = totalCostLimit;
    }
    
    /** Getter for property comments.
     * @return Value of property comments.
     */
    public String getComments() {
        return comments;
    }
    
    /** Setter for property comments.
     * @param comments New value of property comments.
     */
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    /** Does ...
     *
     *
     * @return
     */
    public Object getPrimaryKey() {
        return new Integer(getBudgetPeriod());
    }
    
    /**
     * @param coeusBean CoeusBean
     * @throws CoeusException
     * @return  boolean
     */
    public boolean isLike(ComparableBean comparableBean) throws CoeusException{
        boolean success = false;
        if(comparableBean instanceof BudgetPeriodBean){
            BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)comparableBean;
            //Proposal Number
            if(budgetPeriodBean.getProposalNumber()!=null){
                if(getProposalNumber().equals(budgetPeriodBean.getProposalNumber())){
                    success = true;
                }else{
                    return false;
                }
            }
            //Version Number
            if(budgetPeriodBean.getVersionNumber()!=-1){
                if(getVersionNumber() == budgetPeriodBean.getVersionNumber()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Period Number
            if(budgetPeriodBean.getBudgetPeriod()!=-1){
                if(getBudgetPeriod() == budgetPeriodBean.getBudgetPeriod()){
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
    
    // Added by Chandra 04/10/2003 - start
    public boolean equals(Object obj) {
        BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)obj;
        if(budgetPeriodBean.getProposalNumber().equals(getProposalNumber()) &&
        budgetPeriodBean.getVersionNumber() == getVersionNumber() &&
        //budgetPeriodBean.getBudgetPeriod()== getBudgetPeriod() && 
        budgetPeriodBean.getAw_BudgetPeriod() == getAw_BudgetPeriod()){
            return true;
        }else{
            return false;
        }
    }
     // Added by Chandra 04/10/2003 - end
    
    /**
     * Overridden method of toString. It will form a string representation of
     * each element associated with this class.
     * @return Concatinated string representation of each element 
     */
    public String toString(){
        StringBuffer strBffr = new StringBuffer("");
        strBffr.append("Proposal Number =>"+this.getProposalNumber());
        strBffr.append(";");
        strBffr.append("Version Name =>"+this.getVersionNumber());
        strBffr.append(";");
        strBffr.append("Budget Period =>"+budgetPeriod);
        strBffr.append(";");
        strBffr.append(";");                     
        strBffr.append("Period Start Date =>"+startDate);
        strBffr.append(";");                     
        strBffr.append("Period End Date =>"+endDate);
        strBffr.append(";");
        strBffr.append("Cost Limit =>"+totalCostLimit);
        strBffr.append(";"); 
        strBffr.append("Total Cost =>"+totalCost);
        strBffr.append(";");       
        strBffr.append("Total Direct Cost =>"+totalDirectCost);
        strBffr.append(";");        
        strBffr.append("Total Indirect Cost =>"+totalIndirectCost);
        strBffr.append(";");       
        strBffr.append("Under Recovery Amount =>"+underRecoveryAmount);
        strBffr.append(";");       
        strBffr.append("Cost Sharing Amount =>"+costSharingAmount);
        strBffr.append(";");
        strBffr.append("Update User =>"+this.getUpdateUser());
        strBffr.append(";");
        strBffr.append("Update Time Stamp =>"+this.getUpdateTimestamp());
        strBffr.append(";");
        strBffr.append("AcType =>"+this.getAcType());    
        return strBffr.toString();
    }
    
    /** Getter for property aw_BudgetPeriod.
     * @return Value of property aw_BudgetPeriod.
     */
    public int getAw_BudgetPeriod() {
        return aw_BudgetPeriod;
    }
    
    /** Setter for property aw_BudgetPeriod.
     * @param aw_BudgetPeriod New value of property aw_BudgetPeriod.
     */
    public void setAw_BudgetPeriod(int aw_BudgetPeriod) {
        this.aw_BudgetPeriod = aw_BudgetPeriod;
    }

    public double getTotalDirectCostLimit() {
        return totalDirectCostLimit;
    }

    public void setTotalDirectCostLimit(double totalDirectCostLimit) {
        this.totalDirectCostLimit = totalDirectCostLimit;
    }

    public double getNoOfPeriodMonths() {
        return noOfPeriodMonths;
    }

    public void setNoOfPeriodMonths(double noOfPeriodMonths) {
        this.noOfPeriodMonths = noOfPeriodMonths;
    }
    
} // end BudgetPeriodBean



