/*
 * TuitionFeeBean.java
 *
 * Created on August 24, 2007, 2:59 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.budget.bean;

import edu.mit.coeus.bean.BaseBean;
import java.sql.Timestamp;

/**
 *
 * @author  talarianand
 */
public class TuitionFeeBean implements BaseBean, java.io.Serializable {
    
    private Timestamp ceStrtYear;
    private Timestamp ceEndYear;
    private int numOfMonths;
    private String fiscalYear;
    private double monthlyRate;
    private boolean autoCalculation;
    private boolean calculate;
    private String categoryCode;
    private int startingMonth;
    private String costElement;
    private String description;
    
    /** Creates a new instance of TuitionFeeBean */
    public TuitionFeeBean() {
    }
    
    public void setStartDate(Timestamp ceStrtYear) {
        this.ceStrtYear = ceStrtYear;
    }
    
    public Timestamp getStartDate() {
        return ceStrtYear;
    }
    
    public void setEndDate(Timestamp ceEndYear) {
        this.ceEndYear = ceEndYear;
    }
    
    public Timestamp getEndDate() {
        return ceEndYear;
    }
    
    public void setNumOfMonths(int numOfMonths) {
        this.numOfMonths = numOfMonths;
    }
    
    public int getNumOfMonths() {
        return numOfMonths;
    }
    
    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }
    
    public String getFiscalYear() {
        return fiscalYear;
    }
    
    public void setMonthlyRate(double monthlyRate) {
        this.monthlyRate = monthlyRate;
    }
    
    public double getMonthlyRate() {
        return monthlyRate;
    }
    
    public void setAutoCalculation(boolean calculation) {
        this.autoCalculation = calculation;
    }
    
    public boolean getAutoCalculation() {
        return autoCalculation;
    }
    
    public void setCalculate(boolean calculate) {
        this.calculate = calculate;
    }
    
    public boolean getCalculate() {
        return calculate;
    }
    
    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }
    
    public String getCategoryCode() {
        return categoryCode;
    }
    
    public void setStartingMonth(int startingMonth) {
        this.startingMonth = startingMonth;
    }
    
    public int getStartingMonth() {
        return startingMonth;
    }
    
    public void setCostElement(String costElement) {
        this.costElement = costElement;
    }
    
    public String getCostElement() {
        return costElement;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
