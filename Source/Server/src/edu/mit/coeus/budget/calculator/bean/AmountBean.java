/*
 * @(#)AmountBean.java October 14, 2003, 12:58 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.budget.calculator.bean;

import java.util.*;
import edu.mit.coeus.bean.BaseBean;

/**
 * Holds all the calculated amounts for a rate class - rate type combination for each
 * breakup interval.
 *
 * @author  Sagin
 * @version 1.0
 * Created on October 14, 2003, 12:58 PM
 * 
 */
public class AmountBean implements BaseBean {

  ///////////////////////////////////////
  // attributes
  

/**
 * Represents Rate Class Type
 */
    private String rateClassType;

/**
 * Represents Rate Class Code
 */
    private int rateClassCode; 

/**
 * Represents Rate Type Code
 */
    private int rateTypeCode; 

/**
 * Represents Apply Rate Flag
 */
    private boolean applyRateFlag;  

/**
 * Represents Applied Rate
 */
    private double appliedRate = 0;

/**
 * Represents Calculated Cost
 */
    private double calculatedCost = 0; 

/**
 * Represents Calculated Cost Sharing
 */
    private double calculatedCostSharing = 0; 

/**
 * Represents the Under Recovery
 */
    private double underRecovery = 0; 
    /** Case Id #1811.
     *Holds the Base Cost details
     */
    private double baseAmount = 0;
 
 //Adding COEUSQA-2393  Revamp Budget Engine - Start   
/**
 * Represents isCalculated or not
 */
    private boolean calculated;
    
/**
 * Represents rowIndex
 */
    private int rowIndex;
    
  /**
 * Represents rowIndex
 */
    private int parentRowIndex;  
    
 //Adding COEUSQA-2393  Revamp Budget Engine - End
    
    /** Getter for property rateClassCode.
     * @return Value of property rateClassCode.
     */
    public int getRateClassCode() {
        return rateClassCode;
    }    

    /** Setter for property rateClassCode.
     * @param rateClassCode New value of property rateClassCode.
     */
    public void setRateClassCode(int rateClassCode) {
        this.rateClassCode = rateClassCode;
    }
    
    /** Getter for property rateTypeCode.
     * @return Value of property rateTypeCode.
     */
    public int getRateTypeCode() {
        return rateTypeCode;
    }
    
    /** Setter for property rateTypeCode.
     * @param rateTypeCode New value of property rateTypeCode.
     */
    public void setRateTypeCode(int rateTypeCode) {
        this.rateTypeCode = rateTypeCode;
    }
    
    /** Getter for property applyRateFlag.
     * @return Value of property applyRateFlag.
     */
    public boolean isApplyRateFlag() {
        return applyRateFlag;
    }
    
    /** Setter for property applyRateFlag.
     * @param applyRateFlag New value of property applyRateFlag.
     */
    public void setApplyRateFlag(boolean applyRateFlag) {
        this.applyRateFlag = applyRateFlag;
    }
    
    /** Getter for property calculatedCost.
     * @return Value of property calculatedCost.
     */
    public double getCalculatedCost() {
        return calculatedCost;
    }
    
    /** Setter for property calculatedCost.
     * @param calculatedCost New value of property calculatedCost.
     */
    public void setCalculatedCost(double calculatedCost) {
        this.calculatedCost = calculatedCost;
    }
    
    /** Getter for property calculatedCostSharing.
     * @return Value of property calculatedCostSharing.
     */
    public double getCalculatedCostSharing() {
        return calculatedCostSharing;
    }
    
    /** Setter for property calculatedCostSharing.
     * @param calculatedCostSharing New value of property calculatedCostSharing.
     */
    public void setCalculatedCostSharing(double calculatedCostSharing) {
        this.calculatedCostSharing = calculatedCostSharing;
    }
    
    /** Getter for property rateClassType.
     * @return Value of property rateClassType.
     */
    public java.lang.String getRateClassType() {
        return rateClassType;
    }
    
    /** Setter for property rateClassType.
     * @param rateClassType New value of property rateClassType.
     */
    public void setRateClassType(java.lang.String rateClassType) {
        this.rateClassType = rateClassType;
    }
    
    /** Getter for property underRecovery.
     * @return Value of property underRecovery.
     */
    public double getUnderRecovery() {
        return underRecovery;
    }
    
    /** Setter for property underRecovery.
     * @param underRecovery New value of property underRecovery.
     */
    public void setUnderRecovery(double underRecovery) {
        this.underRecovery = underRecovery;
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
    
    public boolean equals(Object obj) {
        AmountBean amountBean = (AmountBean)obj;
        if(amountBean.getRateClassType().equals(getRateClassType()) &&
            amountBean.getRateClassCode() == getRateClassCode() &&
            amountBean.getRateTypeCode() == getRateTypeCode()){
            return true;
        }else {
            return false;
        }
    }
    
    /**
     * Getter for property baseAmount.
     * @return Value of property baseAmount.
     */
    public double getBaseAmount() {
        return baseAmount;
    }
    
    /**
     * Setter for property baseAmount.
     * @param baseAmount New value of property baseAmount.
     */
    public void setBaseAmount(double baseAmount) {
        this.baseAmount = baseAmount;
    }

    public boolean isCalculated() {
        return calculated;
    }

    public void setCalculated(boolean calculated) {
        this.calculated = calculated;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getParentRowIndex() {
        return parentRowIndex;
    }

    public void setParentRowIndex(int parentRowIndex) {
        this.parentRowIndex = parentRowIndex;
    }
    
 } // end AmountBean



