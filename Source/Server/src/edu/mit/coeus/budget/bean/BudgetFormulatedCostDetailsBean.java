/*
 * BudgetFormulatedCostDetailsBean.java
 *
 * Created on December 1, 2011, 1:08 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.budget.bean;

import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;

/**
 *
 * @author satheeshkumarkn
 */
public class BudgetFormulatedCostDetailsBean extends BudgetBean{
    
    private int budgetPeriod;
    private int lineItemNumber;
    private int formulatedNumber;
    private int formulatedCode;
    private String formulatedCodeDescription;
    private double unitCost;
    private int count;
    private int frequency;
    private double calculatedExpenses;
    private int awFormulatedNumber;
    private int awFormulatedCode;
    
    /** Creates a new instance of BudgetFormulatedCostDetailsBean */
    public BudgetFormulatedCostDetailsBean() {
    }
    
    /**
     * Method to compare the bean
     * @param comparableBean 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @return boolean
     */
    public boolean isLike(ComparableBean comparableBean) throws CoeusException {
        return false;
    }

    /**
     * Method to get the budget period
     * @return budgetPeriod
     */
    public int getBudgetPeriod() {
        return budgetPeriod;
    }

    /**
     * Method to set the budget period
     * @param budgetPeriod 
     */
    public void setBudgetPeriod(int budgetPeriod) {
        this.budgetPeriod = budgetPeriod;
    }

    /**
     * Method to get the line item number
     * @return lineItemNumber
     */
    public int getLineItemNumber() {
        return lineItemNumber;
    }

    /**
     * Method to set the line item number
     * @param lineItemNumber 
     */
    public void setLineItemNumber(int lineItemNumber) {
        this.lineItemNumber = lineItemNumber;
    }

    /**
     * Method to get the formulated number
     * @return formulatedNumber
     */
    public int getFormulatedNumber() {
        return formulatedNumber;
    }

    /**
     * Method to set the formulated number
     * @param formulatedNumber 
     */
    public void setFormulatedNumber(int formulatedNumber) {
        this.formulatedNumber = formulatedNumber;
    }

    /**
     * Method to get the formulated code
     * @return formulatedCode
     */
    public int getFormulatedCode() {
        return formulatedCode;
    }

    /**
     * Method to set the formulated code
     * @param formulatedCode 
     */
    public void setFormulatedCode(int formulatedCode) {
        this.formulatedCode = formulatedCode;
    }

    /**
     * Method to get the unit cost
     * @return unitCost
     */
    public double getUnitCost() {
        return unitCost;
    }

    /**
     * Method to set the unit cost
     * @param unitCost 
     */
    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    /**
     * Method to get the count
     * @return count
     */
    public int getCount() {
        return count;
    }

    /**
     * Method to set the count
     * @param count 
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Method to get the frequency
     * @return frequency
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * Method to set the frequency
     * @param frequency 
     */
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    /**
     * Method to get the calculated expenses
     * @return calculatedExpenses
     */
    public double getCalculatedExpenses() {
        return calculatedExpenses;
    }

    /**
     * 
     * Method to set calculatedExpenses
     * @param calculatedExpenses
     */
    public void setCalculatedExpenses(double calculatedExpense) {
        this.calculatedExpenses = calculatedExpense;
    }

    /**
     * Method to get formulated code description
     * @return 
     */
    public String getFormulatedCodeDescription() {
        return formulatedCodeDescription;
    }

    /**
     * Method to set formulated code description
     * @param formulatedCodeDescription 
     */
    public void setFormulatedCodeDescription(String formulatedCodeDescription) {
        this.formulatedCodeDescription = formulatedCodeDescription;
    }

    /**
     * Method to get awFormulated number
     * @return awFormulatedNumber
     */
    public int getAwFormulatedNumber() {
        return awFormulatedNumber;
    }

    /**
     * Method to set awFormulated number
     * @param awFormulatedNumber 
     */
    public void setAwFormulatedNumber(int awFormulatedNumber) {
        this.awFormulatedNumber = awFormulatedNumber;
    }

    /**
     * Method to get awFormulated code
     * @return awFormulatedCode
     */
    public int getAwFormulatedCode() {
        return awFormulatedCode;
    }

    /**
     * Method to set awFormulated code
     * @param awFormulatedCode 
     */
    public void setAwFormulatedCode(int awFormulatedCode) {
        this.awFormulatedCode = awFormulatedCode;
    }
    
    /**
     * Method to check the equals of the object
     * @param obj 
     * @return isEquals
     */
    public boolean equals(Object obj) {
        boolean isEquals = false;
        BudgetFormulatedCostDetailsBean budgetFormulatedCostDetailsBean = (BudgetFormulatedCostDetailsBean)obj;
        if(budgetFormulatedCostDetailsBean.getProposalNumber().equals(getProposalNumber()) &&
                budgetFormulatedCostDetailsBean.getVersionNumber() == getVersionNumber() &&
                budgetFormulatedCostDetailsBean.getBudgetPeriod()== getBudgetPeriod() &&
                budgetFormulatedCostDetailsBean.getLineItemNumber()== getLineItemNumber() &&
                budgetFormulatedCostDetailsBean.getFormulatedNumber() == getFormulatedNumber()){
            isEquals = true;
        }
        return isEquals;
    }
    
}
