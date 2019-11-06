/**
 * @(#)ValidRatesBean.java 1.0 April 27, 2004 12:43 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.bean;
import java.util.Vector;
import java.sql.Date;
import edu.mit.coeus.bean.BaseBean;
/**
 * This class is used to hold Valid Rates data
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on April 27, 2004 12:43 PM
 */

public class ValidRatesBean implements java.io.Serializable, BaseBean{
    
    private double onCampusRate;
    private double offCampusRate;
    private char rateClassType;
    private String adjustmentKey;
    private java.sql.Timestamp updateTimestamp; 
    private String updateUser;

    /**
     *	Default Constructor
     */
    
    public ValidRatesBean(){
    }          
    
    /** Getter for property onCampusRate.
     * @return Value of property onCampusRate.
     */
    public double getOnCampusRate() {
        return onCampusRate;
    }    
    
    /** Setter for property onCampusRate.
     * @param onCampusRate New value of property onCampusRate.
     */
    public void setOnCampusRate(double onCampusRate) {
        this.onCampusRate = onCampusRate;
    }
    
    /** Getter for property offCampusRate.
     * @return Value of property offCampusRate.
     */
    public double getOffCampusRate() {
        return offCampusRate;
    }
    
    /** Setter for property offCampusRate.
     * @param offCampusRate New value of property offCampusRate.
     */
    public void setOffCampusRate(double offCampusRate) {
        this.offCampusRate = offCampusRate;
    }
    
    /** Getter for property rateClassType.
     * @return Value of property rateClassType.
     */
    public char getRateClassType() {
        return rateClassType;
    }
    
    /** Setter for property rateClassType.
     * @param rateClassType New value of property rateClassType.
     */
    public void setRateClassType(char rateClassType) {
        this.rateClassType = rateClassType;
    }
    
    /** Getter for property adjustmentKey.
     * @return Value of property adjustmentKey.
     */
    public java.lang.String getAdjustmentKey() {
        return adjustmentKey;
    }
    
    /** Setter for property adjustmentKey.
     * @param adjustmentKey New value of property adjustmentKey.
     */
    public void setAdjustmentKey(java.lang.String adjustmentKey) {
        this.adjustmentKey = adjustmentKey;
    }
    
    /** Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /** Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /** Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /** Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }    
}