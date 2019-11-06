/*
 * @(#)BudgetBean.java August 17, 2004, 1:16 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.rates.bean;

import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.bean.PrimaryKey;
import java.util.*;
import edu.mit.coeus.exception.CoeusException;

/**
 * The class is the base class of all Budget related classes
 *
 * @author  Shivakumar
 * @version 1.0
 * Created on August 17, 2004, 1:16 PM
 */

public abstract class RatesBean implements CoeusBean, java.io.Serializable {

    //holds rate class code
    private int rateClassCode = -1;
    //holds rateTypeCode
    private int rateTypeCode = -1;
    //holds Fiscal Year
    private String fiscalYear = null;
    
    // Holds the unit number.
    // It is an enhancement, both LA Rates and Institute Rates are pertained to the Unit Number
    private String unitNumber = null;
    //holds start date
    private java.sql.Date startDate = null;   
    //holds the on Off Campus flag
    private boolean onOffCampusFlag;    
    //holds rate
    private double instituteRate;
    //holds rate class description
    private String rateClassDescription = null;
    //holds rate type description
    private String rateTypeDescription = null;    
    
    //holds update user id
    private String updateUser = null;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp = null;
    //holds account type
    private String acType = null;
    
    private double rate = -1;
    
    private int rowId;
    
    private String parentUnitNumber;
    private String rateClassType = null;
    
    /** Creates a new instance of RatesBean */
    public RatesBean() {
        
    }   
    /** Getter for property rateClassCode.
     * @return Value of property rateClassCode.
     */
     public String getRateClassType() {
        return rateClassType;
    }

    public void setRateClassType(java.lang.String rateClassType) {
        this.rateClassType = rateClassType;
    }
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
    
    /** Getter for property fiscalYear.
     * @return Value of property fiscalYear.
     */
    public java.lang.String getFiscalYear() {
        return fiscalYear;
    }
    
    /** Setter for property fiscalYear.
     * @param fiscalYear New value of property fiscalYear.
     */
    public void setFiscalYear(java.lang.String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }
    
    /** Getter for property startDate.
     * @return Value of property startDate.
     */
    public java.sql.Date getStartDate() {
        return startDate;
    }
    
    /** Setter for property startDate.
     * @param startDate New value of property startDate.
     */
    public void setStartDate(java.sql.Date startDate) {
        this.startDate = startDate;
    }
    
    /** Getter for property onOffCampusFlag.
     * @return Value of property onOffCampusFlag.
     */
    public boolean isOnOffCampusFlag() {
        return onOffCampusFlag;
    }
    
    /** Setter for property onOffCampusFlag.
     * @param onOffCampusFlag New value of property onOffCampusFlag.
     */
    public void setOnOffCampusFlag(boolean onOffCampusFlag) {
        this.onOffCampusFlag = onOffCampusFlag;
    }
    
    public String getOnOffCampusFlag() {
        //modified for the Bug Fix:1427 start 
        return ""+onOffCampusFlag;
        //End Bug Fix:1427
    }
    
    /** Getter for property instituteRate.
     * @return Value of property instituteRate.
     */
    public double getInstituteRate() {
        return instituteRate;
    }
    
    /** Setter for property instituteRate.
     * @param rate New value of property instituteRate.
     */
    public void setInstituteRate(double instituteRate) {
        this.instituteRate = instituteRate;
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
    
    /** Getter for property acType.
     * @return Value of property acType.
     */
    public java.lang.String getAcType() {
        return acType;
    }
    
    /** Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
    }    
    
    /** Getter for property rateClassDescription.
     * @return Value of property rateClassDescription.
     */
    public java.lang.String getRateClassDescription() {
        return rateClassDescription;
    }
    
    /** Setter for property rateClassDescription.
     * @param rateClassDescription New value of property rateClassDescription.
     */
    public void setRateClassDescription(java.lang.String rateClassDescription) {
        this.rateClassDescription = rateClassDescription;
    }
    
    /** Getter for property rateTypeDescription.
     * @return Value of property rateTypeDescription.
     */
    public java.lang.String getRateTypeDescription() {
        return rateTypeDescription;
    }
    
    /** Setter for property rateTypeDescription.
     * @param rateTypeDescription New value of property rateTypeDescription.
     */
    public void setRateTypeDescription(java.lang.String rateTypeDescription) {
        this.rateTypeDescription = rateTypeDescription;
    }
      
    public boolean equals(CoeusBean coeusBean) throws CoeusException {
        return isLike(coeusBean);
     }
    
    /** Getter for property unitNumber.
     * @return Value of property unitNumber.
     *
     */
    public java.lang.String getUnitNumber() {
        return unitNumber;
    }
    
    /** Setter for property unitNumber.
     * @param unitNumber New value of property unitNumber.
     *
     */
    public void setUnitNumber(java.lang.String unitNumber) {
        this.unitNumber = unitNumber;
    }
    
    /** Getter for property rate.
     * @return Value of property rate.
     *
     */
    public double getRate() {
        return rate;
    }    
    
    /** Setter for property rate.
     * @param rate New value of property rate.
     *
     */
    public void setRate(double rate) {
        this.rate = rate;
    }
    
    /** Getter for property rowId.
     * @return Value of property rowId.
     *
     */
    public int getRowId() {
        return rowId;
    }
    
    /** Setter for property rowId.
     * @param rowId New value of property rowId.
     *
     */
    public void setRowId(int rowId) {
        this.rowId = rowId;
    }
    
    public boolean equals(Object obj) {
        RatesBean ratesBean = (RatesBean)obj;        
        if(ratesBean.getRowId() == getRowId()) {
            return true;       
        }else {
            return false;
        }
        
    }
    
    /** Getter for property parentUnitNumber.
     * @return Value of property parentUnitNumber.
     *
     */
    public java.lang.String getParentUnitNumber() {
        return parentUnitNumber;
    }
    
    /** Setter for property parentUnitNumber.
     * @param parentUnitNumber New value of property parentUnitNumber.
     *
     */
    public void setParentUnitNumber(java.lang.String parentUnitNumber) {
        this.parentUnitNumber = parentUnitNumber;
    }
    
 } // end BudgetBean




