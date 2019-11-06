/*
 * @(#)PersonnelLineItemEdiCalculator.java January 23, 2004, 04:41 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.budget.edi;

import java.util.*;
import java.text.SimpleDateFormat;

import edu.mit.coeus.budget.calculator.*;
import edu.mit.coeus.budget.calculator.bean.*;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.budget.edi.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
import java.sql.Date;

/**
 * Used to calculate the personnel line item edi rates by breaking into smaller periods 
 * according to the rate changes, calculating for each breakup period and will 
 * return the breakup periods.
 *
 * @author  Sagin
 * @version 1.0
 * Created on January 23, 2004, 04:41 PM
 *
 */
public class PersonnelLineItemEdiCalculator extends PersonnelLineItemCalculator {

  ///////////////////////////////////////
  // operations


    private int period;
    /**
     * Constructor...
     * Accepts BudgetPersonnelDetailsBean which contains the personnel line item details
     * 
     * @param budgetPersonnelDetails 
     *
     */
    public PersonnelLineItemEdiCalculator() throws BudgetCalculateException{  
        super();
    } // end PersonnelLineItemCalculator         

    /**
     * Starts the calculation of the personnel line item and sets the calculated rates in the 
     * personnel line item cal amts.
     */
    public void calculate(BudgetPersonnelDetailsEdiBean budgetPersonnelDetailsEdiBean) {        
        budgetPersonnelDetails = budgetPersonnelDetailsEdiBean; 
        lineItemStartDate = budgetPersonnelDetailsEdiBean.getStartDate();
        lineItemEndDate = budgetPersonnelDetailsEdiBean.getEndDate();
        cvLIBreakupIntervals = new CoeusVector();
        
        try {
            if (budgetPersonnelDetailsEdiBean != null) {
                initValues();
                if (cvPersonnelLineItemCalcAmts != null && 
                                    cvPersonnelLineItemCalcAmts.size() > 0) {
                    filterRates();
                    createBreakupIntervals();
                    calculateBreakupIntervals();
                }
            }   
        } catch (Exception ex) {
            ex.printStackTrace();
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

 
 } // end PersonnelLineItemCalculator



