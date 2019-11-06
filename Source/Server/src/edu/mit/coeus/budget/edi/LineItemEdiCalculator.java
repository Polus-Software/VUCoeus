/*
 * @(#)LineItemEdiCalculator.java January 23, 2004, 3:44 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.budget.edi;

import edu.mit.coeus.budget.calculator.bean.*;
import edu.mit.coeus.budget.calculator.LineItemCalculator;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.CoeusException;

import java.util.*;
import java.text.SimpleDateFormat;

/**
 * Used to calculate the line item rates for Edi by breaking into smaller periods 
 * according to the rate changes, calculating for each breakup period and will 
 * return the breakup periods.
 *
 * @author  Sagin
 * @version 1.0
 * Created on January 23, 2004, 3:44 PM
 *
 */
public class LineItemEdiCalculator extends LineItemCalculator {

    private int period;
   ///////////////////////////////////////
  // operations

    /**
     * Constructor...
     */
    public LineItemEdiCalculator() {
        super();
        
    } // end LineItemCalculator   

    /**
     * Starts the calculation of the line item and sets the calculated rates in the 
     * line item cal amts.
     * Accepts BudgetDetailBean which contains the line item details
     * 
     * @param budgetDetailBean 
     */
    public void calculate(BudgetDetailBean budgetDetailBean) {  
        
        //Initialize values
        budgetDetailBean = budgetDetailBean;        
        cvLIBreakupIntervals = new CoeusVector();
        this.budgetDetailBean = budgetDetailBean;
        if (budgetDetailBean != null) {
            try {
                initValues();
                if (cvLineItemCalcAmts != null && cvLineItemCalcAmts.size() > 0) {
                    filterRates();
                    createBreakupIntervals();
                    calculateBreakupIntervals();
                }
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Getter for property period.
     * @return Value of property period.
     */
    public int getPeriod() {
        return period;
    }
    
    /**
     * Setter for property period.
     * @param period New value of property period.
     */
    public void setPeriod(int period) {
        this.period = period;
    }
    
 // end Calculate    
    
 } // end LineItemCalculator



