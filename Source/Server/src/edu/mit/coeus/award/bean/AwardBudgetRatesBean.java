/*
 * AwardBudgetRatesBean.java
 *
 * Created on September 3, 2005, 1:57 PM
 */

package edu.mit.coeus.award.bean;

/**
 *
 * @author  ajaygm
 */
public class AwardBudgetRatesBean extends AwardBaseBean{
    private String costElement;
    private String ohType;
    private double rate;
    private boolean onOffCampusFlag;
    
    /** Creates a new instance of AwardBudgetRatesBean */
    public AwardBudgetRatesBean() {
    }
    
    /**
     * Getter for property costElement.
     * @return Value of property costElement.
     */
    public java.lang.String getCostElement() {
        return costElement;
    }
    
    /**
     * Setter for property costElement.
     * @param costElement New value of property costElement.
     */
    public void setCostElement(java.lang.String costElement) {
        this.costElement = costElement;
    }
    
    /**
     * Getter for property ohType.
     * @return Value of property ohType.
     */
    public java.lang.String getOhType() {
        return ohType;
    }
    
    /**
     * Setter for property ohType.
     * @param ohType New value of property ohType.
     */
    public void setOhType(java.lang.String ohType) {
        this.ohType = ohType;
    }
    
    /**
     * Getter for property rate.
     * @return Value of property rate.
     */
    public double getRate() {
        return rate;
    }
    
    /**
     * Setter for property rate.
     * @param rate New value of property rate.
     */
    public void setRate(double rate) {
        this.rate = rate;
    }
    
    /**
     * Getter for property onOffCampusFlag.
     * @return Value of property onOffCampusFlag.
     */
    public boolean isOnOffCampusFlag() {
        return onOffCampusFlag;
    }
    
    /**
     * Setter for property onOffCampusFlag.
     * @param onOffCampusFlag New value of property onOffCampusFlag.
     */
    public void setOnOffCampusFlag(boolean onOffCampusFlag) {
        this.onOffCampusFlag = onOffCampusFlag;
    }
    
}
