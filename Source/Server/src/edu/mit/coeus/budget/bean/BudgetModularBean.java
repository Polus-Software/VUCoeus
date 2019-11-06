/*
 * BudgetModularBean.java
 *
 * Created on September 21, 2005, 5:49 PM
 */

package edu.mit.coeus.budget.bean;

import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;

/**
 *
 * @author  tarique
 */
public class BudgetModularBean extends BudgetBean implements java.io.Serializable{
    
    private int budgetPeriod;
    private double directCostFA;
    private double consortiumFNA;
    private double totalDirectCost;
    /** Creates a new instance of BudgetModularBean */
    public BudgetModularBean() {
        
    }
    
    /**
     * Getter for property budgetPeriod.
     * @return Value of property budgetPeriod.
     */
    public int getBudgetPeriod() {
        return budgetPeriod;
    }    
    
    
    
   public boolean equals(Object obj) {
        BudgetModularBean budgetModularBean = (BudgetModularBean)obj;
        if(budgetModularBean.getProposalNumber().equals(getProposalNumber()) &&
            budgetModularBean.getVersionNumber() == getVersionNumber() &&
            budgetModularBean.getBudgetPeriod() == getBudgetPeriod()){
            return true;       
        }else {
            return false;
        }
    }
   public boolean isLike(ComparableBean comparableBean) throws CoeusException{
        boolean success = false;
        if(comparableBean instanceof BudgetModularBean){
            BudgetModularBean budgetModularBean = (BudgetModularBean)comparableBean;
            //Proposal Number
            if(budgetModularBean.getProposalNumber()!=null){
                if(getProposalNumber().equals(budgetModularBean.getProposalNumber())){
                    success = true;
                }else{
                    return false;
                }
            }
            //Version Number
            if(budgetModularBean.getVersionNumber()!=-1){
                if(getVersionNumber() == budgetModularBean.getVersionNumber()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Budget Period
            if(budgetModularBean.getBudgetPeriod()!=-1){
                if(getBudgetPeriod() == budgetModularBean.getBudgetPeriod()){
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
   
   /**
    * Getter for property directCostFA.
    * @return Value of property directCostFA.
    */
   public double getDirectCostFA() {
       return directCostFA;
   }
   
   /**
    * Setter for property directCostFA.
    * @param directCostFA New value of property directCostFA.
    */
   public void setDirectCostFA(double directCostFA) {
       this.directCostFA = directCostFA;
   }
   
   /**
    * Getter for property consortiumFNA.
    * @return Value of property consortiumFNA.
    */
   public double getConsortiumFNA() {
       return consortiumFNA;
   }
   
   /**
    * Setter for property consortiumFNA.
    * @param consortiumFNA New value of property consortiumFNA.
    */
   public void setConsortiumFNA(double consortiumFNA) {
       this.consortiumFNA = consortiumFNA;
   }
   
   /**
    * Getter for property totalDirectCost.
    * @return Value of property totalDirectCost.
    */
   public double getTotalDirectCost() {
       return totalDirectCost;
   }
   
   /**
    * Setter for property totalDirectCost.
    * @param totalDirectCost New value of property totalDirectCost.
    */
   public void setTotalDirectCost(double totalDirectCost) {
       this.totalDirectCost = totalDirectCost;
   }
   
   /**
    * Setter for property budgetPeriod.
    * @param budgetPeriod New value of property budgetPeriod.
    */
   public void setBudgetPeriod(int budgetPeriod) {
       this.budgetPeriod = budgetPeriod;
   }
   
}
