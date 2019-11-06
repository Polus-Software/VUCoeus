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
public class BudgetPersonnelCalAmountsBean extends BudgetDetailCalAmountsBean implements PrimaryKey, java.io.Serializable{
    //holds budget period
    //private int budgetPeriod = -1;
    //holds lineItemNumber
    //private int lineItemNumber = -1;
    //holds person Number
    private int personNumber = -1;
    //holds rate class code
    //private int rateClassCode = -1;
    //holds rate type code
    //private int rateTypeCode = -1;
    //holds apply rate flag
    //private boolean applyRateFlag;
    //holds calculated cost
    //private double calculatedCost;
    //holds calculated cost sharing
    //private double calculatedCostSharing;
    
    /** Creates a new instance of BudgetDetailCalAmountsBean */
    public BudgetPersonnelCalAmountsBean() {
        
    }

    /** Does ...
     *
     *
     * @return
     */
    public Object getPrimaryKey() {
        return new String(getProposalNumber()+getVersionNumber()+getBudgetPeriod()+getLineItemNumber()+getPersonNumber()+getRateClassCode()+getRateTypeCode());
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
    
    /**
     * @param coeusBean CoeusBean
     * @throws CoeusException
     * @return boolean
     */    
    public boolean isLike(ComparableBean comparableBean) throws CoeusException{
        boolean success = false;        
        if(comparableBean instanceof BudgetPersonnelCalAmountsBean){
            BudgetPersonnelCalAmountsBean budgetPersonnelCalAmountsBean = (BudgetPersonnelCalAmountsBean)comparableBean;
            //Proposal Number
            if(budgetPersonnelCalAmountsBean.getProposalNumber()!=null){
                if(getProposalNumber().equals(budgetPersonnelCalAmountsBean.getProposalNumber())){
                    success = true;
                }else{
                    return false;
                }
            }
            //Version Number
            if(budgetPersonnelCalAmountsBean.getVersionNumber()!=-1){
                if(getVersionNumber() == budgetPersonnelCalAmountsBean.getVersionNumber()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Period Number
            if(budgetPersonnelCalAmountsBean.getBudgetPeriod()!=-1){
                if(getBudgetPeriod() == budgetPersonnelCalAmountsBean.getBudgetPeriod()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Line Item Number
            if(budgetPersonnelCalAmountsBean.getLineItemNumber()!=-1){
                if(getLineItemNumber() == budgetPersonnelCalAmountsBean.getLineItemNumber()){
                    success = true;
                }else{
                    return false;
                }
            }            
            //Person Id
            if(budgetPersonnelCalAmountsBean.getPersonNumber()!=-1){
                if(getPersonNumber() == budgetPersonnelCalAmountsBean.getPersonNumber()){
                    success = true;
                }else{
                    return false;
                }
            }
            
            //Rate Class Code
            if(budgetPersonnelCalAmountsBean.getRateClassCode()!=-1){
                if(getRateClassCode() == budgetPersonnelCalAmountsBean.getRateClassCode()){
                    success = true;
                }else{
                    return false;
                }
            }
            
            //Rate Type Code
            if(budgetPersonnelCalAmountsBean.getRateTypeCode()!=-1){
                if(getRateTypeCode() == budgetPersonnelCalAmountsBean.getRateTypeCode()){
                    success = true;
                }else{
                    return false;
                }
            }                        
            //Rate Class Type
            if(budgetPersonnelCalAmountsBean.getRateClassType()!=null){
                if(getRateClassType() == budgetPersonnelCalAmountsBean.getRateClassType()){
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
        BudgetPersonnelCalAmountsBean budgetPersonnelCalAmountsBean = (BudgetPersonnelCalAmountsBean)obj;
        if(budgetPersonnelCalAmountsBean.getProposalNumber().equals(getProposalNumber()) &&
        budgetPersonnelCalAmountsBean.getVersionNumber() == getVersionNumber() &&
        budgetPersonnelCalAmountsBean.getBudgetPeriod()== getBudgetPeriod() &&
        budgetPersonnelCalAmountsBean.getLineItemNumber()== getLineItemNumber()&&
        budgetPersonnelCalAmountsBean.getRateClassCode()== getRateClassCode()&&
        budgetPersonnelCalAmountsBean.getPersonNumber()== getPersonNumber()&&
        budgetPersonnelCalAmountsBean.getRateTypeCode()== getRateTypeCode()){
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
        strBffr.append("Budget Period =>"+this.getBudgetPeriod());
        strBffr.append(";");
        strBffr.append("Line Item Number =>"+this.getLineItemNumber());
        strBffr.append(";");
        strBffr.append("Person Number =>"+personNumber);
        strBffr.append(";");     
        strBffr.append("Rate Class Code =>"+this.getRateClassCode());
        strBffr.append(";");
        strBffr.append("Rate Class Description =>"+this.getRateClassDescription());
        strBffr.append(";");                
        strBffr.append("Rate Type Code =>"+this.getRateTypeCode());
        strBffr.append(";");
        strBffr.append("Rate Type Description =>"+this.getRateTypeDescription());
        strBffr.append(";");
        strBffr.append("Apply Rate Flag =>"+this.isApplyRateFlag());
        strBffr.append(";");
        strBffr.append("Calculated Cost =>"+this.getCalculatedCost());
        strBffr.append(";");        
        strBffr.append("Calculated Cost Sharing =>"+this.getCalculatedCostSharing());
        strBffr.append(";");        
        strBffr.append("Rate Class Type =>"+this.getRateClassType());
        strBffr.append(";");             
        strBffr.append("Update User =>"+this.getUpdateUser());
        strBffr.append(";");
        strBffr.append("Update Time Stamp =>"+this.getUpdateTimestamp());
        strBffr.append(";");
        strBffr.append("AcType =>"+this.getAcType());    
        strBffr.append("\n");
        return strBffr.toString();
    }    
}