/*
 * @(#)BudgetInfo.java September 29, 2003, 2:28 PM
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
 * The class used to hold the information of <code>Budget Details Calculated Amounts</code>
 *
 * @author  Prasanna Kumar
 * @version 1.0
 * Created on September 29, 2003, 2:28 PM
 */
public class BudgetDetailCalAmountsBean extends BudgetBean implements PrimaryKey, java.io.Serializable{
    //holds budget period
    private int budgetPeriod = -1;
    //holds lineItemNumber
    private int lineItemNumber = -1;
    //holds rate class code
    private int rateClassCode = -1;
    //holds rate class description
    private String rateClassDescription;
    //holds rate type code
    private int rateTypeCode = -1;
    //holds rate type description
    private String rateTypeDescription;
    //holds apply rate flag
    private boolean applyRateFlag;
    //holds calculated cost
    private double calculatedCost;
    //holds calculated cost sharing
    private double calculatedCostSharing;
    //holds rate class type
    private String rateClassType;
    
    /** Creates a new instance of BudgetDetailCalAmountsBean */
    public BudgetDetailCalAmountsBean() {
        
    }
    
    /** Does ...
     *
     *
     * @return
     */
    public Object getPrimaryKey() {
        return new String(getProposalNumber()+getVersionNumber()+getBudgetPeriod()+getLineItemNumber()+getRateClassCode()+getRateTypeCode());
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
    
    /** Getter for property rateClassCode.
     * @return Value of property rateClassCode.
     */
    public int getRateClassCode() {
        return rateClassCode;
    }
    
    /** Setter for property rateClassCode.
     * @param rateClassCode New value of property rateClassCode.
     */
    public void setRateClassCode(int rateClassCode) {
        this.rateClassCode = rateClassCode;
    }
    
    /** Getter for property rateTypeCode.
     * @return Value of property rateTypeCode.
     */
    public int getRateTypeCode() {
        return rateTypeCode;
    }
    
    /** Setter for property rateTypeCode.
     * @param rateTypeCode New value of property rateTypeCode.
     */
    public void setRateTypeCode(int rateTypeCode) {
        this.rateTypeCode = rateTypeCode;
    }
    
    /** Getter for property applyRateFlag.
     * @return Value of property applyRateFlag.
     */
    public boolean isApplyRateFlag() {
        return applyRateFlag;
    }
    
    /** Setter for property applyRateFlag.
     * @param applyRateFlag New value of property applyRateFlag.
     */
    public void setApplyRateFlag(boolean applyRateFlag) {
        this.applyRateFlag = applyRateFlag;
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
    
    /** Getter for property calculatedCostSharing.
     * @return Value of property calculatedCostSharing.
     */
    public double getCalculatedCostSharing() {
        return calculatedCostSharing;
    }
    
    /** Setter for property calculatedCostSharing.
     * @param calculatedCostSharing New value of property calculatedCostSharing.
     */
    public void setCalculatedCostSharing(double calculatedCostSharing) {
        this.calculatedCostSharing = calculatedCostSharing;
    }
    
    public boolean isLike(ComparableBean comparableBean) throws CoeusException{
        boolean success = false;
        if(comparableBean instanceof BudgetDetailCalAmountsBean){
            BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)comparableBean;
            //Proposal Number
            if(budgetDetailCalAmountsBean.getProposalNumber()!=null){
                if(getProposalNumber().equals(budgetDetailCalAmountsBean.getProposalNumber())){
                    success = true;
                }else{
                    return false;
                }
            }
            //Version Number
            if(budgetDetailCalAmountsBean.getVersionNumber()!=-1){
                if(getVersionNumber() == budgetDetailCalAmountsBean.getVersionNumber()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Period Number
            if(budgetDetailCalAmountsBean.getBudgetPeriod()!=-1){
                if(getBudgetPeriod() == budgetDetailCalAmountsBean.getBudgetPeriod()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Line Item Number
            if(budgetDetailCalAmountsBean.getLineItemNumber()!=-1){
                if(getLineItemNumber() == budgetDetailCalAmountsBean.getLineItemNumber()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Rate Class Code
            if(budgetDetailCalAmountsBean.getRateClassCode()!=-1){
                if(getRateClassCode() == budgetDetailCalAmountsBean.getRateClassCode()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Rate Type Code
            if(budgetDetailCalAmountsBean.getRateTypeCode()!=-1){
                if(getRateTypeCode() == budgetDetailCalAmountsBean.getRateTypeCode()){
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
    
    /** Getter for property rateClassDescription.
     * @return Value of property rateClassDescription.
     */
    public java.lang.String getRateClassDescription() {
        return rateClassDescription;
    }
    
    /** Setter for property rateClassDescription.
     * @param rateClassDescription New value of property rateClassDescription.
     */
    public void setRateClassDescription(java.lang.String rateClassDescription) {
        this.rateClassDescription = rateClassDescription;
    }
    
    /** Getter for property rateTypeDescription.
     * @return Value of property rateTypeDescription.
     */
    public java.lang.String getRateTypeDescription() {
        return rateTypeDescription;
    }
    
    /** Setter for property rateTypeDescription.
     * @param rateTypeDescription New value of property rateTypeDescription.
     */
    public void setRateTypeDescription(java.lang.String rateTypeDescription) {
        this.rateTypeDescription = rateTypeDescription;
    }
    
    /** Getter for property rateClassType.
     * @return Value of property rateClassType.
     */
    public java.lang.String getRateClassType() {
        return rateClassType;
    }
    
    /** Setter for property rateClassType.
     * @param rateClassType New value of property rateClassType.
     */
    public void setRateClassType(java.lang.String rateClassType) {
        this.rateClassType = rateClassType;
    }
    
    // Added by Chandra 04/10/2003 - start
    public boolean equals(Object obj) {
        BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)obj;
        if(budgetDetailCalAmountsBean.getProposalNumber().equals(getProposalNumber()) &&
        budgetDetailCalAmountsBean.getVersionNumber() == getVersionNumber() &&
        budgetDetailCalAmountsBean.getBudgetPeriod()== getBudgetPeriod() &&
        budgetDetailCalAmountsBean.getLineItemNumber()== getLineItemNumber()&&
        budgetDetailCalAmountsBean.getRateClassCode()== getRateClassCode()&&
        budgetDetailCalAmountsBean.getRateTypeCode()== getRateTypeCode()){
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
        strBffr.append("Rate Class Code =>"+rateClassCode);
        strBffr.append(";");
        strBffr.append("Rate Class Description =>"+rateClassDescription);
        strBffr.append(";");                
        strBffr.append("Rate Type Code =>"+rateTypeCode);
        strBffr.append(";");
        strBffr.append("Rate Type Description =>"+rateTypeDescription);
        strBffr.append(";");
        strBffr.append("Apply Rate Flag =>"+applyRateFlag);
        strBffr.append(";");
        strBffr.append("Calculated Cost =>"+calculatedCost);
        strBffr.append(";");        
        strBffr.append("Calculated Cost Sharing =>"+calculatedCostSharing);
        strBffr.append(";");        
        strBffr.append("Rate Class Type =>"+rateClassType);
        strBffr.append(";");             
        strBffr.append("Update User =>"+this.getUpdateUser());
        strBffr.append(";");
        strBffr.append("Update Time Stamp =>"+this.getUpdateTimestamp());
        strBffr.append(";");
        strBffr.append("AcType =>"+this.getAcType());    
        return strBffr.toString();
    }
}