/*
 * @(#)BudgetInfo.java September 29, 2003, 2:28 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.budget.bean;

import java.beans.*;
import edu.mit.coeus.budget.bean.BudgetBean;

/**
 * The class is used to hold the information of the budget
 * total table in the Total Tab of the budget base window
 *
 * @author  Vyjayanthi
 * @version 1.0
 * Created on November 4, 2003, 5:00 PM
 */
public class BudgetTotalBean extends BudgetBean implements java.io.Serializable {
    //holds the costElement
    private String costElement;
    
    //holds the costElement
    private String costElementValue;
    
    //holds the costElementDescription
    private String costElementDescription;
    
    //holds the personId if the bean stores personnel data
    private String personId;
    
    //holds a double array of periodCost
    private double[] periodCost;
    
    //holds the total amount
    private double total = 0;
    
    //holds the total number of budget periods
    private int budgetPeriods = 0;
    
    //holds the rateClassCode
    private int rateClassCode = -1;

    //holds the rateTypeCode
    private int rateTypeCode = -1;
    
    public static final double INITIAL_VALUE = 0.0000000001;
    
    /** Creates new BudgetTotalBean */
    public BudgetTotalBean() {
        
    }
    
    /** Getter for property costElement.
     * @return Value of property costElement.
     *
     */
    public String getCostElement(){
        return costElement;
    }
    
    /** Getter for property costElementDescription.
     * @return Value of property costElementDescription.
     *
     */
    public String getCostElementDescription(){
        return costElementDescription;
    }
    
    /** Getter for property total.
     * @return Value of property total.
     *
     */
    public double getTotal(){
        return total;
    }
    
    /** Setter for property costElement.
     * @param costElement New value of property costElement.
     *
     */
    public void setCostElement(String costElement){
        this.costElement = costElement;
    }
    
    /** Setter for property costElementDescription.
     * @param costElementDescription New value of property costElementDescription.
     *
     */
    public void setCostElementDescription(String costElementDescription){
        this.costElementDescription = costElementDescription;
    }
    
    /** Setter for property periodCost.
     */
    public void setTotal(){
        for(int index = 0; index < periodCost.length; index++) {
            if (periodCost[index] != INITIAL_VALUE) {
                total += periodCost[index];
            }
        }
    }
    
    /** Getter for property budgetPeriods.
     * @return Value of property budgetPeriods.
     *
     */
    public int getBudgetPeriods() {
        return budgetPeriods;
    }
    
    /** Setter for property budgetPeriods.
     * @param budgetPeriods New value of property budgetPeriods.
     *
     */
    public void setBudgetPeriods(int budgetPeriods) {
        this.budgetPeriods = budgetPeriods;
        periodCost = new double[budgetPeriods];
        for (int index = 0; index < periodCost.length; index++) {
            periodCost[index] = INITIAL_VALUE;
        }
    }

    /** Setter for property periodCost.
     * @param index takes the index of property periodCost array
     * @param value New value of property periodCost.
     *
     */    
    public void setPeriodCost(int index, double value){
        this.periodCost[index] = value;
    }
    
    /** Getter for property periodCost.
     * @param index takes the index of property periodCost array
     * @return Value returns an array of property periodCost.
     */
    public double getPeriodCost(int index) {
        return this.periodCost[index];
    }
    
    /** Getter for property periodCost.
     * @param index takes the index of array periodCost
     * @return Value returns a single value of property periodCost
     * corresponding to the index
     */
    public double getPeriodCostValue(int index){
        return this.periodCost[index];
    }

    /** An overridden method
     * @param comparableBean ComparableBean
     * @throws CoeusException CoeusException
     * @return boolean
     */   
    public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) throws edu.mit.coeus.exception.CoeusException {
        return false;
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
    
    /** Getter for property personId.
     * @return Value of property personId.
     *
     */
    public java.lang.String getPersonId() {
        return personId;
    }
    
    /** Setter for property personId.
     * @param personId New value of property personId.
     *
     */
    public void setPersonId(java.lang.String personId) {
        this.personId = personId;
    }
    
    /** Getter for property costElementValue.
     * @return Value of property costElementValue.
     *
     */
    public java.lang.String getCostElementValue() {
        return costElementValue;
    }
    
    /** Setter for property costElementValue.
     * @param costElementValue New value of property costElementValue.
     *
     */
    public void setCostElementValue(java.lang.String costElementValue) {
        this.costElementValue = costElementValue;
    }
    
}
