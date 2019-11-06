/*
 * @(#)BudgetInfo.java December 29, 2003, 3:58 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.budget.bean;

import edu.mit.coeus.bean.BaseBean;
/**
 * The class used to hold the information of <code>Budget Summary</code>
 * This is used while generating Budget Summary Report
 *
 * @author  Prasanna Kumar
 * @version 1.0
 * Created on December 29, 2003, 3:58 PM
 */
public class BudgetSummaryReportBean implements java.io.Serializable, BaseBean{
    //holds Start Date 
    private java.sql.Date startDate = null;
    //holds End Date 
    private java.sql.Date endDate = null;    
    //holds budget category code
    private int budgetCategoryCode = -1;
    //holds budget Category Description
    private String budgetCategoryDescription = null;
    //holds Investigator Flag
    private int investigatorFlag;
    //holds Person Name
    private String personName;
    //holds salary requested
    private double salaryRequested;
    //holds percent charged
    private double percentCharged;
    //holds percent effort
    private double percentEffort;
    //holds Employee Benefits AppliedRate
    private String employeeBenefitRate;
    //holds Vacation AppliedRate
    private String vacationRate;
    //holds fringe
    private double fringe;
    //holds onOffCampus flag
    private boolean onOffCampus;
    //holds calculatedCost
    private double calculatedCost;
    //holds sortId
    private int sortId;
    //holds costElementDescription
    private String costElementDescription;
    //holds applied Rate
    private double appliedRate;
    //holds rate type Description
    private String rateTypeDesc;
    //holds rate Class Description
    private String rateClassDesc;    
    private double costSharingAmount;
    private double costSharingPercentage;
   
    
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
    
    /** Getter for property budgetCategoryCode.
     * @return Value of property budgetCategoryCode.
     */
    public int getBudgetCategoryCode() {
        return budgetCategoryCode;
    }
    
    /** Setter for property budgetCategoryCode.
     * @param budgetCategoryCode New value of property budgetCategoryCode.
     */
    public void setBudgetCategoryCode(int budgetCategoryCode) {
        this.budgetCategoryCode = budgetCategoryCode;
    }
    
    /** Getter for property budgetCategoryDescription.
     * @return Value of property budgetCategoryDescription.
     */
    public java.lang.String getBudgetCategoryDescription() {
        return budgetCategoryDescription;
    }
    
    /** Setter for property budgetCategoryDescription.
     * @param budgetCategoryDescription New value of property budgetCategoryDescription.
     */
    public void setBudgetCategoryDescription(java.lang.String budgetCategoryDescription) {
        this.budgetCategoryDescription = budgetCategoryDescription;
    }
    
    /** Getter for property investigatorFlag.
     * @return Value of property investigatorFlag.
     */
    public int getInvestigatorFlag() {
        return investigatorFlag;
    }
    
    /** Setter for property investigatorFlag.
     * @param investigatorFlag New value of property investigatorFlag.
     */
    public void setInvestigatorFlag(int investigatorFlag) {
        this.investigatorFlag = investigatorFlag;
    }
    
    /** Getter for property personName.
     * @return Value of property personName.
     */
    public java.lang.String getPersonName() {
        return personName;
    }
    
    /** Setter for property personName.
     * @param personName New value of property personName.
     */
    public void setPersonName(java.lang.String personName) {
        this.personName = personName;
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
    
    /** Getter for property employeeBenefitRate.
     * @return Value of property employeeBenefitRate.
     */
    public String getEmployeeBenefitRate() {
        return employeeBenefitRate;
    }
    
    /** Setter for property employeeBenefitRate.
     * @param employeeBenefitRate New value of property employeeBenefitRate.
     */
    public void setEmployeeBenefitRate(String employeeBenefitRate) {
        this.employeeBenefitRate = employeeBenefitRate;
    }
    
    /** Getter for property vacationRate.
     * @return Value of property vacationRate.
     */
    public String getVacationRate() {
        return vacationRate;
    }
    
    /** Setter for property vacationRate.
     * @param vacationRate New value of property vacationRate.
     */
    public void setVacationRate(String vacationRate) {
        this.vacationRate = vacationRate;
    }
    
    /** Getter for property fringe.
     * @return Value of property fringe.
     */
    public double getFringe() {
        return fringe;
    }
    
    /** Setter for property fringe.
     * @param fringe New value of property fringe.
     */
    public void setFringe(double fringe) {
        this.fringe = fringe;
    }
    
    /** Getter for property onOffCampus.
     * @return Value of property onOffCampus.
     */
    public boolean isOnOffCampus() {
        return onOffCampus;
    }
    
    /** Setter for property onOffCampus.
     * @param onOffCampus New value of property onOffCampus.
     */
    public void setOnOffCampus(boolean onOffCampus) {
        this.onOffCampus = onOffCampus;
    }
    
    /** Getter for property calculatedCost.
     * @return Value of property calculatedCost.
     */
    public double getCalculatedCost() {
        return calculatedCost;
    }
    
    /** Setter for property calculatedCost.
     * @param calculatedCost New value of property calculatedCost.
     */
    public void setCalculatedCost(double calculatedCost) {
        this.calculatedCost = calculatedCost;
    }
    
    /** Getter for property sortId.
     * @return Value of property sortId.
     */
    public int getSortId() {
        return sortId;
    }
    
    /** Setter for property sortId.
     * @param sortId New value of property sortId.
     */
    public void setSortId(int sortId) {
        this.sortId = sortId;
    }
    
    /** Getter for property costElementDescription.
     * @return Value of property costElementDescription.
     */
    public java.lang.String getCostElementDescription() {
        return costElementDescription;
    }
    
    /** Setter for property costElementDescription.
     * @param costElementDescription New value of property costElementDescription.
     */
    public void setCostElementDescription(java.lang.String costElementDescription) {
        this.costElementDescription = costElementDescription;
    }
    
    /** Getter for property appliedRate.
     * @return Value of property appliedRate.
     */
    public double getAppliedRate() {
        return appliedRate;
    }
    
    /** Setter for property appliedRate.
     * @param appliedRate New value of property appliedRate.
     */
    public void setAppliedRate(double appliedRate) {
        this.appliedRate = appliedRate;
    }
    
    /** Getter for property rateTypeDesc.
     * @return Value of property rateTypeDesc.
     */
    public java.lang.String getRateTypeDesc() {
        return rateTypeDesc;
    }
    
    /** Setter for property rateTypeDesc.
     * @param rateTypeDesc New value of property rateTypeDesc.
     */
    public void setRateTypeDesc(java.lang.String rateTypeDesc) {
        this.rateTypeDesc = rateTypeDesc;
    }
    
    /** Getter for property rateClassDesc.
     * @return Value of property rateClassDesc.
     */
    public java.lang.String getRateClassDesc() {
        return rateClassDesc;
    }
    
    /** Setter for property rateClassDesc.
     * @param rateClassDesc New value of property rateClassDesc.
     */
    public void setRateClassDesc(java.lang.String rateClassDesc) {
        this.rateClassDesc = rateClassDesc;
    }    
    
    /**
     * Getter for property costSharingAmount.
     * @return Value of property costSharingAmount.
     */
    public double getCostSharingAmount() {
        return costSharingAmount;
    }
    
    /**
     * Setter for property costSharingAmount.
     * @param costSharingAmount New value of property costSharingAmount.
     */
    public void setCostSharingAmount(double costSharingAmount) {
        this.costSharingAmount = costSharingAmount;
    }
    
    /**
     * Getter for property costSharingPercentage.
     * @return Value of property costSharingPercentage.
     */
    public double getCostSharingPercentage() {
        return costSharingPercentage;
    }
    
    /**
     * Setter for property costSharingPercentage.
     * @param costSharingPercentage New value of property costSharingPercentage.
     */
    public void setCostSharingPercentage(double costSharingPercentage) {
        this.costSharingPercentage = costSharingPercentage;
    }
    
}