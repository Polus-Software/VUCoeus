/*
 * @(#)BudgetJustificationBean.java September 29, 2003, 11:58 AM
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
 * The class used to hold the information of <code>Budget Persons</code>
 *
 * @author  Prasanna Kumar
 * @version 1.0
 * Created on September 29, 2003, 11:58 AM
 */
public class BudgetJustificationBean extends BudgetBean implements PrimaryKey{
    //holds budget justification 
    private String justification = null;
    
    /** Creates a new instance of BudgetInfo */
    public BudgetJustificationBean() {
    }
    
    /** Does ...
     *
     *
     * @return
     */
    public Object getPrimaryKey() {
        return new String(getProposalNumber()+getVersionNumber());
    }            

    /** Getter for property justification.
     * @return Value of property justification.
     */
    public java.lang.String getJustification() {
        return justification;
    }    
    
    /** Setter for property justification.
     * @param justification New value of property justification.
     */
    public void setJustification(java.lang.String justification) {
        this.justification = justification;
    }    
    
    public boolean isLike(ComparableBean comparableBean) throws CoeusException{
        boolean success = false;
        if(comparableBean instanceof BudgetInfoBean){
            BudgetJustificationBean budgetJustificationBean = (BudgetJustificationBean)comparableBean;
            //Proposal Number
            if(budgetJustificationBean.getProposalNumber()!=null){
                if(getProposalNumber().equals(budgetJustificationBean.getProposalNumber())){
                    success = true;
                }else{
                    return false;
                }
            }
            //Version Number
            if(budgetJustificationBean.getVersionNumber()!=-1){
                if(getVersionNumber() == budgetJustificationBean.getVersionNumber()){
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
        BudgetJustificationBean budgetJustificationBean = (BudgetJustificationBean)obj;
        if(budgetJustificationBean.getProposalNumber().equals(getProposalNumber()) &&
            budgetJustificationBean.getVersionNumber() == getVersionNumber()){
                return true;
        }else {
                return false;
        }
    }
    // Added by Chandra 04/10/2003 - End
    
}