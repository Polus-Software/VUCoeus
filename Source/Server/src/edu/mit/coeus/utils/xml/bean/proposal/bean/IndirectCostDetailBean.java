/*
 * @(#)IndirectCostDetailBean.java 
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.utils.xml.bean.proposal.bean;


import java.beans.*;

public class IndirectCostDetailBean implements java.io.Serializable{
 
  
    private double baseAmount = 0;
    private double rate = 0;
  
    /**
     *	Default Constructor
     */
    
    public IndirectCostDetailBean(){
    }
      
     
    /** Getter for property baseAmount.
     * @return Value of property baseAmount.
     */
    public double getBaseAmount() {
        return baseAmount;
    }
    
    /** Setter for property baseAmount.
     * @param baseAmount New value of property baseAmount.
     */
    public void setBaseAmount(double baseAmount) {
        this.baseAmount = baseAmount;
    }
    
     /** Getter for property rate.
     * @return Value of property rate.
     */
    public double getRate() {
        return rate;
    }
    
    /** Setter for property rate.
     * @param rate New value of property rate.
     */
    public void setRate(double rate) {
        this.rate = rate;
    }
  
}