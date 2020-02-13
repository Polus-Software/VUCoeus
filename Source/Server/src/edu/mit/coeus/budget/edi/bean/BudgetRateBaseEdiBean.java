/*
 * @(#)BudgetRateBaseEdiBean.java January 20, 2004, 3:30 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.budget.edi.bean;

import edu.mit.coeus.bean.PrimaryKey;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.budget.bean.BudgetBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;

/**
 * The class used to hold the information of <code>Budget Details</code>
 *
 * @author  Sagin
 * @version 1.0
 * Created on January 20 2004, 3:30 PM
 */
public class BudgetRateBaseEdiBean extends BudgetBean implements PrimaryKey{
    //holds budget period
    private int budgetPeriod = -1;
    //holds lineItemNumber
    private int lineItemNumber = -1;
    //holds budget category code
    private int rateNumber = -1;
    private java.sql.Date startDate = null;
    //holds Line Item End Date date
    private java.sql.Date endDate = null;
    //holds Rate Class Code
    private int rateClassCode = -1;
    //holds Rate Type Code
    private int rateTypeCode = -1;
    //holds the on Off Campus flag
    private boolean onOffCampusFlag;
    //holds Applied Rate
    private double appliedRate;
    //holds the Base Cost
    private double baseCost;
    //holds the Base Cost Sharing
    private double baseCostSharing;
    //holds Calculated Cost
    private double calculatedCost;
    //holds the Calculated Cost Sharing
    private double calculatedCostSharing;
    
    /** Creates a new instance of BudgetInfo */
    public BudgetRateBaseEdiBean() {
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
    
    /** Getter for property rateNumber.
     * @return Value of property rateNumber.
     *
     */
    public int getRateNumber() {
        return rateNumber;
    }
    
    /** Setter for property rateNumber.
     * @param rateNumber New value of property rateNumber.
     *
     */
    public void setRateNumber(int rateNumber) {
        this.rateNumber = rateNumber;
    }
    
    /** Getter for property startDate.
     * @return Value of property startDate.
     *
     */
    public java.sql.Date getStartDate() {
        return startDate;
    }
    
    /** Setter for property startDate.
     * @param startDate New value of property startDate.
     *
     */
    public void setStartDate(java.sql.Date startDate) {
        this.startDate = startDate;
    }
    
    /** Getter for property endDate.
     * @return Value of property endDate.
     *
     */
    public java.sql.Date getEndDate() {
        return endDate;
    }
    
    /** Setter for property endDate.
     * @param endDate New value of property endDate.
     *
     */
    public void setEndDate(java.sql.Date endDate) {
        this.endDate = endDate;
    }
    
    /** Getter for property rateClassCode.
     * @return Value of property rateClassCode.
     *
     */
    public int getRateClassCode() {
        return rateClassCode;
    }
    
    /** Setter for property rateClassCode.
     * @param rateClassCode New value of property rateClassCode.
     *
     */
    public void setRateClassCode(int rateClassCode) {
        this.rateClassCode = rateClassCode;
    }
    
    /** Getter for property rateTypeCode.
     * @return Value of property rateTypeCode.
     *
     */
    public int getRateTypeCode() {
        return rateTypeCode;
    }
    
    /** Setter for property rateTypeCode.
     * @param rateTypeCode New value of property rateTypeCode.
     *
     */
    public void setRateTypeCode(int rateTypeCode) {
        this.rateTypeCode = rateTypeCode;
    }
    
    /** Getter for property onOffCampusFlag.
     * @return Value of property onOffCampusFlag.
     *
     */
    public boolean isOnOffCampusFlag() {
        return onOffCampusFlag;
    }
    
    /** Setter for property onOffCampusFlag.
     * @param onOffCampusFlag New value of property onOffCampusFlag.
     *
     */
    public void setOnOffCampusFlag(boolean onOffCampusFlag) {
        this.onOffCampusFlag = onOffCampusFlag;
    }
    
    /** Getter for property appliedRate.
     * @return Value of property appliedRate.
     *
     */
    public double getAppliedRate() {
        return appliedRate;
    }
    
    /** Setter for property appliedRate.
     * @param appliedRate New value of property appliedRate.
     *
     */
    public void setAppliedRate(double appliedRate) {
        this.appliedRate = appliedRate;
    }
    
    /** Getter for property baseCost.
     * @return Value of property baseCost.
     *
     */
    public double getBaseCost() {
        return baseCost;
    }
    
    /** Setter for property baseCost.
     * @param baseCost New value of property baseCost.
     *
     */
    public void setBaseCost(double baseCost) {
        this.baseCost = baseCost;
    }
    
    /** Getter for property baseCostSharing.
     * @return Value of property baseCostSharing.
     *
     */
    public double getBaseCostSharing() {
        return baseCostSharing;
    }
    
    /** Setter for property baseCostSharing.
     * @param baseCostSharing New value of property baseCostSharing.
     *
     */
    public void setBaseCostSharing(double baseCostSharing) {
        this.baseCostSharing = baseCostSharing;
    }
    
    /** Getter for property calculatedCost.
     * @return Value of property calculatedCost.
     *
     */
    public double getCalculatedCost() {
        return calculatedCost;
    }
    
    /** Setter for property calculatedCost.
     * @param calculatedCost New value of property calculatedCost.
     *
     */
    public void setCalculatedCost(double calculatedCost) {
        this.calculatedCost = calculatedCost;
    }
    
    /** Getter for property calculatedCostSharing.
     * @return Value of property calculatedCostSharing.
     *
     */
    public double getCalculatedCostSharing() {
        return calculatedCostSharing;
    }
    
    /** Setter for property calculatedCostSharing.
     * @param calculatedCostSharing New value of property calculatedCostSharing.
     *
     */
    public void setCalculatedCostSharing(double calculatedCostSharing) {
        this.calculatedCostSharing = calculatedCostSharing;
    }
    
    /**
     * This method is used to return the Primary key object of the bean class.
     * @return Object Primary key Object
     */
    public Object getPrimaryKey() {
        return new Integer(getProposalNumber() + getBudgetPeriod() +
            getLineItemNumber() + getRateNumber() + getRateClassCode() +
            getRateTypeCode());
    }
    
    public boolean isLike(ComparableBean comparableBean) throws CoeusException{
        boolean success = false;
        if(comparableBean instanceof BudgetRateBaseEdiBean){
            BudgetRateBaseEdiBean budgetRateBaseEdiBean = (BudgetRateBaseEdiBean)comparableBean;
            //Proposal Number
            if(budgetRateBaseEdiBean.getProposalNumber()!=null){
                if(getProposalNumber().equals(budgetRateBaseEdiBean.getProposalNumber())){
                    success = true;
                }else{
                    return false;
                }
            }
            //Period Number
            if(budgetRateBaseEdiBean.getBudgetPeriod()!=-1){
                if(getBudgetPeriod() == budgetRateBaseEdiBean.getBudgetPeriod()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Line Item Number
            if(budgetRateBaseEdiBean.getLineItemNumber()!=-1){
                if(getLineItemNumber() == budgetRateBaseEdiBean.getLineItemNumber()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Rate Number
            if(budgetRateBaseEdiBean.getRateNumber() != -1) {
                if(getRateNumber() == budgetRateBaseEdiBean.getRateNumber()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Rate Class Code
            if(budgetRateBaseEdiBean.getRateClassCode()!=-1){
                if(getRateClassCode() == budgetRateBaseEdiBean.getRateClassCode()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Rate Type Code
            if(budgetRateBaseEdiBean.getRateTypeCode()!=-1){
                if(getRateTypeCode() == budgetRateBaseEdiBean.getRateTypeCode()){
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
    
    public boolean equals(Object obj) {
        BudgetRateBaseEdiBean budgetRateBaseEdiBean = (BudgetRateBaseEdiBean)obj;
        if(budgetRateBaseEdiBean.getProposalNumber().equals(getProposalNumber()) &&
            budgetRateBaseEdiBean.getBudgetPeriod()== getBudgetPeriod() &&
            budgetRateBaseEdiBean.getLineItemNumber()== getLineItemNumber()&&
            budgetRateBaseEdiBean.getRateNumber() == getRateNumber() &&
            budgetRateBaseEdiBean.getRateClassCode()== getRateClassCode()&&
            budgetRateBaseEdiBean.getRateTypeCode()== getRateTypeCode()){
            return true;
        }else {
            return false;
        }
    }
    
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
        strBffr.append("Rate Number =>"+rateNumber);
        strBffr.append(";");                  
        strBffr.append("Start Date =>"+startDate);
        strBffr.append(";");                     
        strBffr.append("End Date =>"+endDate);
        strBffr.append(";");
        strBffr.append("Rate Class Code =>"+rateClassCode);
        strBffr.append(";");                
        strBffr.append("Rate Type Code =>"+rateTypeCode);
        strBffr.append(";");
        strBffr.append("On Off Campus Flag =>"+onOffCampusFlag);
        strBffr.append(";");
        strBffr.append("Applied Rate =>"+appliedRate);
        strBffr.append(";");        
        strBffr.append("Base Cost =>"+baseCost);
        strBffr.append(";");        
        strBffr.append("Base Cost Sharing =>"+baseCostSharing);
        strBffr.append(";");        
        strBffr.append("Calculated Cost =>"+calculatedCost);
        strBffr.append(";");        
        strBffr.append("Calculated Cost Sharing =>"+calculatedCostSharing);
        strBffr.append(";");              
        strBffr.append("Update User =>"+this.getUpdateUser());
        strBffr.append(";");
        strBffr.append("Update Time Stamp =>"+this.getUpdateTimestamp());
        strBffr.append(";");
        strBffr.append("AcType =>"+this.getAcType());    
        return strBffr.toString();
    }
    
}