/*
 * @(#)BudgetInfo.java January 20, 2004, 2:58 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.budget.edi.bean;

import edu.mit.coeus.bean.PrimaryKey;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.budget.bean.BudgetPersonnelCalAmountsBean;
import edu.mit.coeus.bean.ComparableBean;
import java.util.Hashtable;
import edu.mit.coeus.exception.CoeusException;

/**
 * The class used to hold the information of <code>Budget Details Calculated Amounts for EDI</code>
 *
 * @author  Sagin
 * @version 1.0
 * Created on January 20, 2004, 2:58 PM
 */
public class BudgetPersonnelCalAmountsEdiBean extends BudgetPersonnelCalAmountsBean implements PrimaryKey, java.io.Serializable{

    //holds rateNumber
    private int rateNumber = -1;
    
    //holds Applied Rate
    private double appliedRate;
    
    /** Creates a new instance of BudgetDetailCalAmountsBean */
    public BudgetPersonnelCalAmountsEdiBean() {
        
    } 
    
    /** 
     * Creates a new instance of BudgetPersonnelCalAmountsEdiBean 
     * and copy the personnel cal amounts from the personnel cal amounts bean which is 
     * received as argument.
     * @param personnelDetailsBean.
     */
    public BudgetPersonnelCalAmountsEdiBean(BudgetPersonnelCalAmountsBean personnelCalAmountsBean) {
        setProposalNumber(personnelCalAmountsBean.getProposalNumber());
        setVersionNumber(personnelCalAmountsBean.getVersionNumber());
        setBudgetPeriod(personnelCalAmountsBean.getBudgetPeriod());
        setLineItemNumber(personnelCalAmountsBean.getLineItemNumber());
        setPersonNumber(personnelCalAmountsBean.getPersonNumber());
        setRateNumber(1);
        setRateClassCode(personnelCalAmountsBean.getRateClassCode());
        setRateTypeCode(personnelCalAmountsBean.getRateTypeCode());
        setApplyRateFlag(personnelCalAmountsBean.isApplyRateFlag());
        setAppliedRate(0);
        setCalculatedCost(personnelCalAmountsBean.getCalculatedCost());
        setCalculatedCostSharing(personnelCalAmountsBean.getCalculatedCostSharing());
        
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

    /** Does ...
     *
     *
     * @return
     */
    public Object getPrimaryKey() {
        return new String(getProposalNumber() + getBudgetPeriod() +
            getLineItemNumber() + getPersonNumber() + getRateNumber() + getRateClassCode() +
            getRateTypeCode());
    } 
    
    /**
     * @param coeusBean CoeusBean
     * @throws CoeusException
     * @return boolean
     */    
    public boolean isLike(ComparableBean comparableBean) throws CoeusException{
        boolean success = false;        
        if(comparableBean instanceof BudgetPersonnelDetailsEdiBean){
            BudgetPersonnelCalAmountsEdiBean budgetPersonnelCalAmountsEdiBean = (BudgetPersonnelCalAmountsEdiBean)comparableBean;
            //Proposal Number
            if(budgetPersonnelCalAmountsEdiBean.getProposalNumber() != null){
                if(getProposalNumber().equals(budgetPersonnelCalAmountsEdiBean.getProposalNumber())){
                    success = true;
                }else{
                    return false;
                }
            }
            //Period Number
            if(budgetPersonnelCalAmountsEdiBean.getBudgetPeriod()!=-1){
                if(getBudgetPeriod() == budgetPersonnelCalAmountsEdiBean.getBudgetPeriod()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Line Item Number
            if(budgetPersonnelCalAmountsEdiBean.getLineItemNumber()!=-1){
                if(getLineItemNumber() == budgetPersonnelCalAmountsEdiBean.getLineItemNumber()){
                    success = true;
                }else{
                    return false;
                }
            }            
            //Person Number
            if(budgetPersonnelCalAmountsEdiBean.getPersonNumber()!=-1){
                if(getPersonNumber() == budgetPersonnelCalAmountsEdiBean.getPersonNumber()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Rate Number
            if(budgetPersonnelCalAmountsEdiBean.getRateNumber() != -1) {
                if(getRateNumber() == budgetPersonnelCalAmountsEdiBean.getRateNumber()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Rate Class Code
            if(budgetPersonnelCalAmountsEdiBean.getRateClassCode()!=-1){
                if(getRateClassCode() == budgetPersonnelCalAmountsEdiBean.getRateClassCode()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Rate Type Code
            if(budgetPersonnelCalAmountsEdiBean.getRateTypeCode()!=-1){
                if(getRateTypeCode() == budgetPersonnelCalAmountsEdiBean.getRateTypeCode()){
                    success = true;
                }else{
                    return false;
                }
            }                        
            //Rate Class Type
            if(budgetPersonnelCalAmountsEdiBean.getRateClassType()!=null){
                if(getRateClassType() == budgetPersonnelCalAmountsEdiBean.getRateClassType()){
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
        BudgetPersonnelCalAmountsEdiBean budgetPersonnelCalAmountsEdiBean = (BudgetPersonnelCalAmountsEdiBean)obj;
        if(budgetPersonnelCalAmountsEdiBean.getProposalNumber().equals(getProposalNumber()) &&
        budgetPersonnelCalAmountsEdiBean.getBudgetPeriod()== getBudgetPeriod() &&
        budgetPersonnelCalAmountsEdiBean.getLineItemNumber()== getLineItemNumber()&&
        budgetPersonnelCalAmountsEdiBean.getPersonNumber()== getPersonNumber()&&
        budgetPersonnelCalAmountsEdiBean.getRateNumber() == getRateNumber() &&
        budgetPersonnelCalAmountsEdiBean.getRateClassCode()== getRateClassCode()&&
        budgetPersonnelCalAmountsEdiBean.getRateTypeCode()== getRateTypeCode()){
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
        StringBuffer strBffr = new StringBuffer(super.toString().substring(1, super.toString().length() - 2));
        strBffr.append("Rate Number =>"+rateNumber);
        strBffr.append(";");
        strBffr.append("Applied Rate =>"+appliedRate);
        strBffr.append(";");
        strBffr.append("\n");
        return strBffr.toString();
    }
    
}