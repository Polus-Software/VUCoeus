/*
 * @(#)BudgetPersonnelDetailsEdiBean.java January 20, 2004, 12:58 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.budget.edi.bean;

import edu.mit.coeus.bean.PrimaryKey;
import edu.mit.coeus.budget.bean.BudgetPersonnelDetailsBean;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import java.util.Hashtable;
import edu.mit.coeus.exception.CoeusException;
/**
 * The class used to hold the information of <code>Budget Personnel Details for EDI</code>
 *
 * @author  Sagin
 * @version 1.0
 * Created on January 20, 2004, 12:58 PM
 */
public class BudgetPersonnelDetailsEdiBean extends BudgetPersonnelDetailsBean implements PrimaryKey{

    //holds rateNumber
    private int rateNumber = -1;
    
    /** Creates a new instance of BudgetPersonnelDetailsEdiBean */
    public BudgetPersonnelDetailsEdiBean() {
    }
    
    /** 
     * Creates a new instance of BudgetPersonnelDetailsEdiBean 
     * and copy the personnel details from the personnel details bean which is 
     * received as argument.
     * @param personnelDetailsBean.
     */
    public BudgetPersonnelDetailsEdiBean(BudgetPersonnelDetailsBean personnelDetailsBean) {
        setProposalNumber(personnelDetailsBean.getProposalNumber());
        setVersionNumber(personnelDetailsBean.getVersionNumber());
        setBudgetPeriod(personnelDetailsBean.getBudgetPeriod());
        setLineItemNumber(personnelDetailsBean.getLineItemNumber());
        setPersonNumber(personnelDetailsBean.getPersonNumber());
        setRateNumber(1);
        setPersonId(personnelDetailsBean.getPersonId());
        setJobCode(personnelDetailsBean.getJobCode());
        setStartDate(personnelDetailsBean.getStartDate());
        setEndDate(personnelDetailsBean.getEndDate());
        setPeriodType(personnelDetailsBean.getPeriodType());
        setLineItemDescription(personnelDetailsBean.getLineItemDescription());
        setSequenceNumber(personnelDetailsBean.getSequenceNumber());
        setSalaryRequested(personnelDetailsBean.getSalaryRequested());
        setPercentCharged(personnelDetailsBean.getPercentCharged());
        setPercentEffort(personnelDetailsBean.getPercentEffort());
        setCostSharingPercent(personnelDetailsBean.getCostSharingPercent());
        setCostSharingAmount(personnelDetailsBean.getCostSharingAmount());
        setUnderRecoveryAmount(personnelDetailsBean.getUnderRecoveryAmount());
        setOnOffCampusFlag(personnelDetailsBean.isOnOffCampusFlag());
        setApplyInRateFlag(personnelDetailsBean.isApplyInRateFlag());
        setBudgetJustification(personnelDetailsBean.getBudgetJustification());
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
    
    /** Does ...
     *
     *
     * @return
     */
    public Object getPrimaryKey() {
        return new String(getProposalNumber() + getBudgetPeriod() +
                    getLineItemNumber() + getPersonNumber() + getRateNumber());
    }
    
    public boolean isLike(ComparableBean comparableBean) throws CoeusException{
        boolean success = false;
        if(comparableBean instanceof BudgetPersonnelDetailsEdiBean){
            BudgetPersonnelDetailsEdiBean budgetPersonnelDetailsEdiBean = (BudgetPersonnelDetailsEdiBean)comparableBean;
            //Proposal Number
            if(budgetPersonnelDetailsEdiBean.getProposalNumber()!=null){
                if(getProposalNumber().equals(budgetPersonnelDetailsEdiBean.getProposalNumber())){
                    success = true;
                }else{
                    return false;
                }
            }
            //Period Number
            if(budgetPersonnelDetailsEdiBean.getBudgetPeriod() != -1) {
                if(getBudgetPeriod() == budgetPersonnelDetailsEdiBean.getBudgetPeriod()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Line Item Number
            if(budgetPersonnelDetailsEdiBean.getLineItemNumber() != -1) {
                if(getLineItemNumber() == budgetPersonnelDetailsEdiBean.getLineItemNumber()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Person Number
            if(budgetPersonnelDetailsEdiBean.getPersonNumber() != -1) {
                if(getPersonNumber() == budgetPersonnelDetailsEdiBean.getPersonNumber()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Rate Number
            if(budgetPersonnelDetailsEdiBean.getRateNumber() != -1) {
                if(getRateNumber() == budgetPersonnelDetailsEdiBean.getRateNumber()){
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
        BudgetPersonnelDetailsEdiBean budgetPersonnelDetailsEdiBean = (BudgetPersonnelDetailsEdiBean)obj;
        if(budgetPersonnelDetailsEdiBean.getProposalNumber().equals(getProposalNumber()) &&
        budgetPersonnelDetailsEdiBean.getBudgetPeriod() == getBudgetPeriod() &&
        budgetPersonnelDetailsEdiBean.getLineItemNumber() == getLineItemNumber()&&
        budgetPersonnelDetailsEdiBean.getPersonNumber() == getPersonNumber()&&
        budgetPersonnelDetailsEdiBean.getRateNumber() == getRateNumber()){
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
        StringBuffer strBffr = new StringBuffer(super.toString().substring(1, super.toString().length() - 2));
        strBffr.append("Rate Number =>"+rateNumber);
        strBffr.append(";");
        strBffr.append("\n");
        return strBffr.toString();
    }
    
}