/*
 * @(#)BudgetPersonnelDetailsBean.java September 29, 2003, 11:58 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.budget.bean;

import edu.mit.coeus.bean.PrimaryKey;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import java.util.Hashtable;
import edu.mit.coeus.exception.CoeusException;
/**
 * The class used to hold the information of <code>Budget Personnel Details</code>
 *
 * @author  Prasanna Kumar
 * @version 1.0
 * Created on September 29, 2003, 11:58 AM
 */
public class BudgetPersonnelDetailsBean extends BudgetBean implements PrimaryKey{
    //holds budget period
    private int budgetPeriod = -1;
    //holds lineItemNumber
    private int lineItemNumber = -1;
    //holds Person Number
    private int personNumber = -1;
    //holds person id
    private String personId = null;
    //holds person full name
    private String fullName = null;
    //holds job code
    private String jobCode = null;
    //holds Start Date
    private java.sql.Date startDate = null;
    //holds End Date
    private java.sql.Date endDate = null;
    //holds period type
    private String periodType = null;
    //holds line item description
    private String lineItemDescription = null;
    //holds sequence Number
    private int sequenceNumber = -1;
    //holds salary requested
    private double salaryRequested;
    //holds percent charged
    private double percentCharged;
    //holds percent effort
    private double percentEffort;
    //holds Cost sharing percent
    private double costSharingPercent;
    //holds Cost sharing amount
    private double costSharingAmount;
    //holds Under recovery amount
    private double underRecoveryAmount;
    //holds onOffCampus flag
    private boolean onOffCampusFlag;
    //holds apply in rate flag
    private boolean applyInRateFlag;
    //holds budget justification
    private String budgetJustification = null;  
    //holds Direct Cost
    private double directCost;    
    //holds Indirect Cost
    private double indirectCost;
    //holds the calculated CostSharing
    private double totalCostSharing;
    
    private String personStartDate =null;
    
    private String personEndDate =null;
    
    /** Creates a new instance of BudgetInfo */
    public BudgetPersonnelDetailsBean() {
    }
    
    /** Does ...
     *
     *
     * @return
     */
    public Object getPrimaryKey() {
        return new String(getProposalNumber()+getVersionNumber()+getBudgetPeriod()+getLineItemNumber()+getPersonNumber());
    }
    
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
    
    /** Getter for property lineItemNumber.
     * @return Value of property lineItemNumber.
     */
    public int getLineItemNumber() {
        return lineItemNumber;
    }
    
    /** Setter for property lineItemNumber.
     * @param lineItemNumber New value of property lineItemNumber.
     */
    public void setLineItemNumber(int lineItemNumber) {
        this.lineItemNumber = lineItemNumber;
    }
    
    /** Getter for property personNumber.
     * @return Value of property personNumber.
     */
    public int getPersonNumber() {
        return personNumber;
    }
    
    /** Setter for property personNumber.
     * @param personNumber New value of property personNumber.
     */
    public void setPersonNumber(int personNumber) {
        this.personNumber = personNumber;
    }
    
    /** Getter for property personId.
     * @return Value of property personId.
     */
    public java.lang.String getPersonId() {
        return personId;
    }
    
    /** Setter for property personId.
     * @param personId New value of property personId.
     */
    public void setPersonId(java.lang.String personId) {
        this.personId = personId;
    }
    
    /** Getter for property fullName.
     * @return Value of property fullName.
     */
    public java.lang.String getFullName() {
        return fullName;
    }
    
    /** Setter for property fullName.
     * @param fullName New value of property fullName.
     */
    public void setFullName(java.lang.String fullName) {
        this.fullName = fullName;
    }
    
    /** Getter for property jobCode.
     * @return Value of property jobCode.
     */
    public java.lang.String getJobCode() {
        return jobCode;
    }
    
    /** Setter for property jobCode.
     * @param jobCode New value of property jobCode.
     */
    public void setJobCode(java.lang.String jobCode) {
        this.jobCode = jobCode;
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
    
    /** Getter for property periodType.
     * @return Value of property periodType.
     */
    public java.lang.String getPeriodType() {
        return periodType;
    }
    
    /** Setter for property periodType.
     * @param periodType New value of property periodType.
     */
    public void setPeriodType(java.lang.String periodType) {
        this.periodType = periodType;
    }
    
    /** Getter for property lineItemDescription.
     * @return Value of property lineItemDescription.
     */
    public java.lang.String getLineItemDescription() {
        return lineItemDescription;
    }
    
    /** Setter for property lineItemDescription.
     * @param lineItemDescription New value of property lineItemDescription.
     */
    public void setLineItemDescription(java.lang.String lineItemDescription) {
        this.lineItemDescription = lineItemDescription;
    }
    
    /** Getter for property sequenceNumber.
     * @return Value of property sequenceNumber.
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    
    /** Setter for property sequenceNumber.
     * @param sequenceNumber New value of property sequenceNumber.
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
    /** Getter for property salaryRequested.
     * @return Value of property salaryRequested.
     */
    public double getSalaryRequested() {
        return salaryRequested;
    }
    
    /** Setter for property salaryRequested.
     * @param salaryRequested New value of property salaryRequested.
     */
    public void setSalaryRequested(double salaryRequested) {
        this.salaryRequested = salaryRequested;
    }
    
    /** Getter for property percentCharged.
     * @return Value of property percentCharged.
     */
    public double getPercentCharged() {
        return percentCharged;
    }
    
    /** Setter for property percentCharged.
     * @param percentCharged New value of property percentCharged.
     */
    public void setPercentCharged(double percentCharged) {
        this.percentCharged = percentCharged;
    }
    
    /** Getter for property percentEffort.
     * @return Value of property percentEffort.
     */
    public double getPercentEffort() {
        return percentEffort;
    }
    
    /** Setter for property percentEffort.
     * @param percentEffort New value of property percentEffort.
     */
    public void setPercentEffort(double percentEffort) {
        this.percentEffort = percentEffort;
    }
    
    /** Getter for property costSharingPercent.
     * @return Value of property costSharingPercent.
     */
    public double getCostSharingPercent() {
        return costSharingPercent;
    }
    
    /** Setter for property costSharingPercent.
     * @param costSharingPercent New value of property costSharingPercent.
     */
    public void setCostSharingPercent(double costSharingPercent) {
        this.costSharingPercent = costSharingPercent;
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
    
    /** Getter for property onOffCampusFlag.
     * @return Value of property onOffCampusFlag.
     */
    public boolean isOnOffCampusFlag() {
        return onOffCampusFlag;
    }
    
    /** Setter for property onOffCampusFlag.
     * @param onOffCampus New value of property onOffCampusFlag.
     */
    public void setOnOffCampusFlag(boolean onOffCampusFlag) {
        this.onOffCampusFlag = onOffCampusFlag;
    }
    
    /** Getter for property applyInRateFlag.
     * @return Value of property applyInRateFlag.
     */
    public boolean isApplyInRateFlag() {
        return applyInRateFlag;
    }
    
    /** Setter for property applyInRateFlag.
     * @param applyInRate New value of property applyInRateFlag.
     */
    public void setApplyInRateFlag(boolean applyInRateFlag) {
        this.applyInRateFlag = applyInRateFlag;
    }
    
    
    /** Getter for property budgetJustification.
     * @return Value of property budgetJustification.
     */
    public java.lang.String getBudgetJustification() {
        return budgetJustification;
    }
    
    /** Setter for property budgetJustification.
     * @param budgetJustification New value of property budgetJustification.
     */
    public void setBudgetJustification(java.lang.String budgetJustification) {
        this.budgetJustification = budgetJustification;
    }      
    
    /** Getter for property directCost.
     * @return Value of property directCost.
     */
    public double getDirectCost() {
        return directCost;
    }
    
    /** Setter for property directCost.
     * @param directCost New value of property directCost.
     */
    public void setDirectCost(double directCost) {
        this.directCost = directCost;
    }
    
    /** Getter for property indirectCost.
     * @return Value of property indirectCost.
     */
    public double getIndirectCost() {
        return indirectCost;
    }
    
    /** Setter for property indirectCost.
     * @param indirectCost New value of property indirectCost.
     */
    public void setIndirectCost(double indirectCost) {
        this.indirectCost = indirectCost;
    }
    
    public boolean isLike(ComparableBean comparableBean) throws CoeusException{
        boolean success = false;
        if(comparableBean instanceof BudgetPersonnelDetailsBean){
            BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean)comparableBean;
            //Proposal Number
            if(budgetPersonnelDetailsBean.getProposalNumber()!=null){
                if(getProposalNumber().equals(budgetPersonnelDetailsBean.getProposalNumber())){
                    success = true;
                }else{
                    return false;
                }
            }
            //Version Number
            if(budgetPersonnelDetailsBean.getVersionNumber()!=-1){
                if(getVersionNumber() == budgetPersonnelDetailsBean.getVersionNumber()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Period Number
            if(budgetPersonnelDetailsBean.getBudgetPeriod()!=-1){
                if(getBudgetPeriod() == budgetPersonnelDetailsBean.getBudgetPeriod()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Line Item Number
            if(budgetPersonnelDetailsBean.getLineItemNumber()!=-1){
                if(getLineItemNumber() == budgetPersonnelDetailsBean.getLineItemNumber()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Person Number
            if(budgetPersonnelDetailsBean.getPersonNumber()!=-1){
                if(getPersonNumber() == budgetPersonnelDetailsBean.getPersonNumber()){
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
        BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean)obj;
        if(budgetPersonnelDetailsBean.getProposalNumber().equals(getProposalNumber()) &&
        budgetPersonnelDetailsBean.getVersionNumber() == getVersionNumber() &&
        budgetPersonnelDetailsBean.getBudgetPeriod()== getBudgetPeriod() &&
        budgetPersonnelDetailsBean.getLineItemNumber()== getLineItemNumber()&&
        budgetPersonnelDetailsBean.getPersonNumber()== getPersonNumber()){
            return true;
        }else {
            return false;
        }
    }
    // Added by Chandra 04/10/2003 - End
    
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
        strBffr.append("Line Item Number =>"+lineItemNumber);
        strBffr.append(";");
        strBffr.append("Person Number =>"+personNumber);
        strBffr.append(";");
        strBffr.append("Person Id =>"+personId);
        strBffr.append(";");
        strBffr.append("Full Name =>"+fullName);
        strBffr.append(";");
        strBffr.append("Job Code =>"+jobCode);
        strBffr.append(";");             
        strBffr.append("Start Date =>"+startDate);
        strBffr.append(";");                     
        strBffr.append("End Date =>"+endDate);
        strBffr.append(";");                     
        strBffr.append("Period Type =>"+periodType);
        strBffr.append(";");                     
        strBffr.append("Period Type =>"+periodType);
        strBffr.append(";");
        strBffr.append("Line Item Description =>"+lineItemDescription);
        strBffr.append(";");
        strBffr.append("Sequence Number =>"+sequenceNumber);
        strBffr.append(";");
        strBffr.append("Salary Requested =>"+salaryRequested);
        strBffr.append(";");
        strBffr.append(";");        
        strBffr.append("Direct Cost =>"+directCost);
        strBffr.append(";");        
        strBffr.append("Indirect Cost =>"+indirectCost);
        strBffr.append(";");        
        strBffr.append("Total Cost Sharing =>"+totalCostSharing);
        strBffr.append(";");        
        strBffr.append("Percentage Charged =>"+percentCharged);
        strBffr.append(";");
        strBffr.append("Percentage Effort =>"+percentEffort);
        strBffr.append(";");
        strBffr.append("Cost Sharing Percent =>"+costSharingPercent);
        strBffr.append(";");
        strBffr.append("Cost Sharing Amount =>"+costSharingAmount);
        strBffr.append(";");
        strBffr.append("Under Recovery Amount =>"+underRecoveryAmount);
        strBffr.append(";");
        strBffr.append("On Off Campus Flag =>"+onOffCampusFlag);
        strBffr.append(";");
        strBffr.append("Apply In Rate Flag =>"+applyInRateFlag);
        strBffr.append(";");        
        strBffr.append("Budget Justification =>"+budgetJustification);
        strBffr.append(";");        
        strBffr.append("Update User =>"+this.getUpdateUser());
        strBffr.append(";");
        strBffr.append("Update Time Stamp =>"+this.getUpdateTimestamp());
        strBffr.append(";");
        strBffr.append("AcType =>"+this.getAcType());    
        strBffr.append("\n");
        return strBffr.toString();
    }    
    
    /** Getter for property totalCostSharing.
     * @return Value of property totalCostSharing.
     *
     */
    public double getTotalCostSharing() {
        return totalCostSharing;
    }
    
    /** Setter for property totalCostSharing.
     * @param totalCostSharing New value of property totalCostSharing.
     *
     */
    public void setTotalCostSharing(double totalCostSharing) {
        this.totalCostSharing = totalCostSharing;
    }
    
    /**
     * Getter for property personStartDate.
     * @return Value of property personStartDate.
     */
    public java.lang.String getPersonStartDate() {
        return personStartDate;
    }
    
    /**
     * Setter for property personStartDate.
     * @param personStartDate New value of property personStartDate.
     */
    public void setPersonStartDate(java.lang.String personStartDate) {
        this.personStartDate = personStartDate;
    }
    
    /**
     * Getter for property personEndDate.
     * @return Value of property personEndDate.
     */
    public java.lang.String getPersonEndDate() {
        return personEndDate;
    }
    
    /**
     * Setter for property personEndDate.
     * @param personEndDate New value of property personEndDate.
     */
    public void setPersonEndDate(java.lang.String personEndDate) {
        this.personEndDate = personEndDate;
    }
    
}