/*
 * BudgetModularIDCBean.java
 *
 * Created on February 3, 2006, 11:03 AM
 */

package edu.mit.coeus.budget.bean;

import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;

/**
 *
 * @author  chandrashekara
 */
public class BudgetModularIDCBean extends BudgetBean implements java.io.Serializable{
    
    
    private int budgetPeriod;
    private int rateNumber;
    private String description;
    private double idcRate;
    private double idcBase;
    private double fundRequested;
    
    //Case 2260 Start 1
    private Integer awRateNumber;
    //Case 2260 End 1
    
    
    /** Creates a new instance of BudgetModularIDCBean */
    public BudgetModularIDCBean() {
    }
    
    public boolean equals(Object obj) {
        BudgetModularIDCBean budgetModularIDCBean = (BudgetModularIDCBean)obj;
        if(budgetModularIDCBean.getProposalNumber().equals(getProposalNumber()) &&
            budgetModularIDCBean.getVersionNumber() == getVersionNumber() &&
            budgetModularIDCBean.getBudgetPeriod() == getBudgetPeriod()&&
            //Case 2260 Start 2
            //budgetModularIDCBean.getRateNumber() == getRateNumber()){
            budgetModularIDCBean.getAwRateNumber()!= null &&
            budgetModularIDCBean.getAwRateNumber().equals(getAwRateNumber())){
            //Case 2260 End 2
            return true;       
        }else {
            return false;
        }
    }
   public boolean isLike(ComparableBean comparableBean) throws CoeusException{
        boolean success = false;
        if(comparableBean instanceof BudgetModularBean){
            BudgetModularIDCBean budgetModularIDCBean = (BudgetModularIDCBean)comparableBean;
            //Proposal Number
            if(budgetModularIDCBean.getProposalNumber()!=null){
                if(getProposalNumber().equals(budgetModularIDCBean.getProposalNumber())){
                    success = true;
                }else{
                    return false;
                }
            }
            //Version Number
            if(budgetModularIDCBean.getVersionNumber()!=-1){
                if(getVersionNumber() == budgetModularIDCBean.getVersionNumber()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Budget Period
            if(budgetModularIDCBean.getBudgetPeriod()!=-1){
                if(getBudgetPeriod() == budgetModularIDCBean.getBudgetPeriod()){
                    success = true;
                }else{
                    return false;
                }
            }
            
            //Rate Number
            if(budgetModularIDCBean.getRateNumber()!=-1){
                if(getRateNumber() == budgetModularIDCBean.getRateNumber()){
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
    * Getter for property budgetPeriod.
    * @return Value of property budgetPeriod.
    */
   public int getBudgetPeriod() {
       return budgetPeriod;
   }
   
   /**
    * Setter for property budgetPeriod.
    * @param budgetPeriod New value of property budgetPeriod.
    */
   public void setBudgetPeriod(int budgetPeriod) {
       this.budgetPeriod = budgetPeriod;
   }
   
   /**
    * Getter for property rateNumber.
    * @return Value of property rateNumber.
    */
   public int getRateNumber() {
       return rateNumber;
   }
   
   /**
    * Setter for property rateNumber.
    * @param rateNumber New value of property rateNumber.
    */
   public void setRateNumber(int rateNumber) {
       this.rateNumber = rateNumber;
   }
   
   /**
    * Getter for property description.
    * @return Value of property description.
    */
   public java.lang.String getDescription() {
       return description;
   }
   
   /**
    * Setter for property description.
    * @param description New value of property description.
    */
   public void setDescription(java.lang.String description) {
       this.description = description;
   }
   
   /**
    * Getter for property idcRate.
    * @return Value of property idcRate.
    */
   public double getIdcRate() {
       return idcRate;
   }
   
   /**
    * Setter for property idcRate.
    * @param idcRate New value of property idcRate.
    */
   public void setIdcRate(double idcRate) {
       this.idcRate = idcRate;
   }
   
   /**
    * Getter for property idcBase.
    * @return Value of property idcBase.
    */
   public double getIdcBase() {
       return idcBase;
   }
   
   /**
    * Setter for property idcBase.
    * @param idcBase New value of property idcBase.
    */
   public void setIdcBase(double idcBase) {
       this.idcBase = idcBase;
   }
   
   /**
    * Getter for property fundRequested.
    * @return Value of property fundRequested.
    */
   public double getFundRequested() {
       return fundRequested;
   }
   
   /**
    * Setter for property fundRequested.
    * @param fundRequested New value of property fundRequested.
    */
   public void setFundRequested(double fundRequested) {
       this.fundRequested = fundRequested;
   }
   
   //Case 2260 Start 3
   /**
    * Getter for property awRateNumber.
    * @return Value of property awRateNumber.
    */
   public Integer getAwRateNumber() {
       return awRateNumber;
   }
   
   /**
    * Setter for property awRateNumber.
    * @param awRateNumber New value of property awRateNumber.
    */
   public void setAwRateNumber(Integer awRateNumber) {
       this.awRateNumber = awRateNumber;
   }
   //Case 2260 End 3
}
