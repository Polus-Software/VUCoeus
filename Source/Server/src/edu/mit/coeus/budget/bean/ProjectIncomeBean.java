/*
 * ProjectIncomeBean.java
 *
 * Created on June 9, 2005, 11:12 AM
 */

package edu.mit.coeus.budget.bean;

import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.bean.PrimaryKey;
import edu.mit.coeus.exception.CoeusException;

/**
 *
 * @author  tarique
 */
public class ProjectIncomeBean extends BudgetBean implements PrimaryKey{
    
    //holds budgetPeriod
    private int budgetPeriod = -1;
    // Holds the income number
    private int incomeNumber = -1;
    // Holds the Project income details
    private double amount;
    // Holds the Project income description
    private String description;
    /** Creates a new instance of ProjectIncomeBean */
    public ProjectIncomeBean() {
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
     * Getter for property incomeNumber.
     * @return Value of property incomeNumber.
     */
    public int getIncomeNumber() {
        return incomeNumber;
    }
    
    /**
     * Setter for property incomeNumber.
     * @param incomeNumber New value of property incomeNumber.
     */
    public void setIncomeNumber(int incomeNumber) {
        this.incomeNumber = incomeNumber;
    }
    
    /**
     * Getter for property amount.
     * @return Value of property amount.
     */
    public double getAmount() {
        return amount;
    }
    
    /**
     * Setter for property amount.
     * @param amount New value of property amount.
     */
    public void setAmount(double amount) {
        this.amount = amount;
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
     * @param coeusBean CoeusBean
     * @throws CoeusException
     * @return  boolean
     */
    public boolean isLike(ComparableBean comparableBean) throws CoeusException{
        boolean success = false;
//        if(comparableBean instanceof BudgetPeriodBean){
//            BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)comparableBean;
//            //Proposal Number
//            if(budgetPeriodBean.getProposalNumber()!=null){
//                if(getProposalNumber().equals(budgetPeriodBean.getProposalNumber())){
//                    success = true;
//                }else{
//                    return false;
//                }
//            }
//            //Version Number
//            if(budgetPeriodBean.getVersionNumber()!=-1){
//                if(getVersionNumber() == budgetPeriodBean.getVersionNumber()){
//                    success = true;
//                }else{
//                    return false;
//                }
//            }
//            //Period Number
//            if(budgetPeriodBean.getBudgetPeriod()!=-1){
//                if(getBudgetPeriod() == budgetPeriodBean.getBudgetPeriod()){
//                    success = true;
//                }else{
//                    return false;
//                }
//            }
//        }else{
//            throw new CoeusException("budget_exception.1000");
//        }
        return success;
    }
    
     public Object getPrimaryKey() {
        return new Integer(getVersionNumber());
    }
    
     public String toString(){
        StringBuffer strBffr = new StringBuffer("");
        strBffr.append("Proposal Number =>"+getProposalNumber());
        strBffr.append(";");
        strBffr.append("Version Number =>"+getVersionNumber());
        strBffr.append(";");
        strBffr.append("Income Number =>"+incomeNumber);
        strBffr.append(";");
        strBffr.append("Budget Period =>"+getBudgetPeriod());
        strBffr.append(";");
        strBffr.append("Update User =>"+getUpdateUser());
        strBffr.append(";");
        strBffr.append("Update Time Stamp =>"+getUpdateTimestamp());
        strBffr.append(";");
        strBffr.append("AcType =>"+getAcType());    
        return strBffr.toString();
    } 
    
    public boolean equals(Object obj) {
        ProjectIncomeBean projectIncomeBean = (ProjectIncomeBean)obj;
        if(projectIncomeBean.getProposalNumber().equals(getProposalNumber()) &&
        projectIncomeBean.getVersionNumber() == getVersionNumber() && 
        projectIncomeBean.getIncomeNumber() == getIncomeNumber() &&
        projectIncomeBean.getBudgetPeriod() == getBudgetPeriod()){
            return true;
        }else{
            return false;
        }
    }
    
    
}
