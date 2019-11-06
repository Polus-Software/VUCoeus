/*
 * BudgetSubAwardDetailBean.java
 *
 * Created on June 29, 2011, 4:08 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved
 */

package edu.mit.coeus.budget.bean;

import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;
import java.sql.Timestamp;

/**
 *
 * @author satheeshkumarkn
 */
public class BudgetSubAwardDetailBean extends BudgetBean implements java.io.Serializable, Comparable<BudgetSubAwardDetailBean>{
    
    private int subAwardNumber;
    private int budgetPeriod;
    private double directCost;
    private double indirectCost;
    private double costSharingAmount;
    private double totalCost;
    private double beforeModifiedDirectCost;
    private double beforeModifiedIndirectCost;
    private double beforeModifiedCostSharingAmount;
    private double beforeModifiedTotalCost;
    
    private String organizationName;
    
    private Timestamp awUpdateTimestamp;
    private String awUpdateUser;
    private String acType; 
    private java.sql.Date periodStartDate = null;
    private java.sql.Date periodEndDate = null;
    
    /**
     * Creates a new instance of BudgetSubAwardDetailBean
     */
    public BudgetSubAwardDetailBean() {
    }

    /**
     * Getter method to get the sub award number
     * @return subAwardNumber
     */
    public int getSubAwardNumber() {
        return subAwardNumber;
    }

    /**
     * Setter method to set the sub award number
     * @param subAwardNumber 
     */
    public void setSubAwardNumber(int subAwardNumber) {
        this.subAwardNumber = subAwardNumber;
    }

    /**
     * Getter method to get the budget period
     * @return budgetPeriod
     */
    public int getBudgetPeriod() {
        return budgetPeriod;
    }

    /**
     * Setter method to set the budget period
     * @param budgetPeriod 
     */
    public void setBudgetPeriod(int budgetPeriod) {
        this.budgetPeriod = budgetPeriod;
    }

    /**
     * Getter method to get the direct cost
     * @return directCost
     */
    public double getDirectCost() {
        return directCost;
    }

    /**
     * Setter method to set the direct cost
     * @param directCost 
     */
    public void setDirectCost(double directCost) {
        this.directCost = directCost;
    }

    /**
     * Getter method to get the indirect cost
     * @return indirectCost
     */
    public double getIndirectCost() {
        return indirectCost;
    }

    /**
     * Settter method to set the indirect cost
     * @param indirectCost 
     */
    public void setIndirectCost(double indirectCost) {
        this.indirectCost = indirectCost;
    }

    /**
     * Getter method to get the cost sharing amount
     * @return costSharingAmount
     */
    public double getCostSharingAmount() {
        return costSharingAmount;
    }

    /**
     * Setter method to set the cost sharing amount
     * @param costSharingAmount 
     */
    public void setCostSharingAmount(double costSharingAmount) {
        this.costSharingAmount = costSharingAmount;
    }

    /**
     * Getter method to get the aw update timestamp
     * 
     * @return awUpdateTimestamp
     */
    public Timestamp getAwUpdateTimestamp() {
        return awUpdateTimestamp;
    }

    /**
     * Setter method to set the  aw udpate timestamp
     * 
     * @param awUpdateTimestamp
     */
    public void setAwUpdateTimestamp(Timestamp updateTimestamp) {
        this.awUpdateTimestamp = updateTimestamp;
    }

    /**
     * Getter method to get the  aw update user
     * 
     * @return awUpdateUser
     */
    public String getAwUpdateUser() {
        return awUpdateUser;
    }

    /**
     * Setter method to set the aw udpate user
     * 
     * @param awUpdateUser
     */
    public void setAwUpdateUser(String updateUser) {
        this.awUpdateUser = updateUser;
    }

    /**
     * Getter method to get the actype
     * @return acType
     */
    public String getAcType() {
        return acType;
    }

    /**
     * Setter method to ser the acType
     * @param acType 
     */
    public void setAcType(String acType) {
        this.acType = acType;
    }
    
    /**
     * Getter method to get the total cost
     * @return totalCost
     */
    public double getTotalCost() {
        return totalCost;
    }

    /**
     * Setter method to set the total cost
     * @param totalCost 
     */
    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
    
    public boolean isLike(ComparableBean comparableBean)throws CoeusException {
        throw new CoeusException("Do Not Use isLike use QueryEngine.filter instead");
    }
    
    /**
     * Overridden method of toString. It will form a string representation of
     * each element associated with this class.
     * @return Concatinated string representation of each element
     */
    public String toString(){
        StringBuffer strBffr = new StringBuffer("");
        strBffr.append("Proposal Number =>"+getProposalNumber());
        strBffr.append(";");
        strBffr.append("Version Name =>"+getVersionNumber());
        strBffr.append(";");
        strBffr.append("Sub Award number =>"+subAwardNumber);
        strBffr.append(";");
        strBffr.append("Update User =>"+getUpdateUser());
        strBffr.append(";");
        strBffr.append("Update Time Stamp =>"+getUpdateTimestamp());
        strBffr.append(";");
        strBffr.append("AcType =>"+getAcType());
        return strBffr.toString();
    }

    /**
     * Method to get the direct cost before modification
     * @return beforeModifiedDirectCost
     */
    public double getBeforeModifiedDirectCost() {
        return beforeModifiedDirectCost;
    }

    /**
     * Method to get the direct cost before modification
     * @param beforeModifiedDirectCost 
     */
    public void setBeforeModifiedDirectCost(double beforeModifiedDirectCost) {
        this.beforeModifiedDirectCost = beforeModifiedDirectCost;
    }

    /**
     * Method to get the indirect cost before modification
     * @return beforeModifiedIndirectCost
     */
    public double getBeforeModifiedIndirectCost() {
        return beforeModifiedIndirectCost;
    }

    /**
     * Method to get the in indirect cost before modification
     * @param beforeModifiedIndirectCost 
     */
    public void setBeforeModifiedIndirectCost(double beforeModifiedIndirectCost) {
        this.beforeModifiedIndirectCost = beforeModifiedIndirectCost;
    }

    /**
     * Method to get the ost sharing amount before modification
     * @return beforeModifiedCostSharingAmount
     */
    public double getBeforeModifiedCostSharingAmount() {
        return beforeModifiedCostSharingAmount;
    }

    /**
     * Method to get the cost sharing amount before modification
     * @param beforeModifiedCostSharingAmount 
     */
    public void setBeforeModifiedCostSharingAmount(double beforeModifiedCostSharingAmount) {
        this.beforeModifiedCostSharingAmount = beforeModifiedCostSharingAmount;
    }

    /**
     * Method to get the total cost before modification
     * @return beforeModifiedTotalCost
     */
    public double getBeforeModifiedTotalCost() {
        return beforeModifiedTotalCost;
    }

    /**
     * Method to set the total cost before modification
     * @param beforeModifiedTotalCost 
     */
    public void setBeforeModifiedTotalCost(double beforeModifiedTotalCost) {
        this.beforeModifiedTotalCost = beforeModifiedTotalCost;
    }

    /**
     * Method to get the period start date
     * @return periodStartDate
     */
    public java.sql.Date getPeriodStartDate() {
        return periodStartDate;
    }

    /**
     * Method to set the period start date
     * @param periodStartDate 
     */
    public void setPeriodStartDate(java.sql.Date periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    /**
     * Method to get the period end date
     * @return periodEndDate
     */
    public java.sql.Date getPeriodEndDate() {
        return periodEndDate;
    }

    /**
     * Method to set the period End date
     * @param periodEndDate 
     */
    public void setPeriodEndDate(java.sql.Date periodEndDate) {
        this.periodEndDate = periodEndDate;
    }

    /**
     * Getter method to get the organizationName
     * @return organizationName
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * Setter method to ser the organizationName
     * @param organizationName 
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public int compareTo(BudgetSubAwardDetailBean budgetSubAwardDetailBean) {
        return this.budgetPeriod - budgetSubAwardDetailBean.budgetPeriod;
    }
}
