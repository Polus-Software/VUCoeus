/*
 * RateClassBaseInclusionBean.java
 *
 * Created on October 31, 2011, 12:47 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.budget.calculator.bean;

import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author mohann
 */
public class RateClassBaseInclusionBean implements CoeusBean, Serializable{
    /**
     * holds rateClassCode
     */
    private int rateClassCode;
    
    /**
     * holds rateTypeCode
     */
    private int rateTypeCode;
    
    /**
     * holds rateClassCodeIncl
     */
    private int rateClassCodeIncl;
    
    /**
     * holds rateTypeCodeIncl
     */
    private int rateTypeCodeIncl;
    
    /**
     * holds rowIndex
     */
    private int rowIndex;
    
    /**
     * holds parentRowIndex
     */
    private int parentRowIndex;
    
    /**
     * holds isCalculated
     */
    private boolean isCalculated;
    
    
    
    //holds account type
    private String acType = null;
    
    //holds update user id
    private String updateUser = null;
    
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp = null;
    
    /** Creates a new instance of RateClassBaseBean */
    public RateClassBaseInclusionBean() {
    }
    
    
    public int getRateClassCode() {
        return rateClassCode;
    }
    
    public void setRateClassCode(int rateClassCode) {
        this.rateClassCode = rateClassCode;
    }
    
    public int getRateTypeCode() {
        return rateTypeCode;
    }
    
    public void setRateTypeCode(int rateTypeCode) {
        this.rateTypeCode = rateTypeCode;
    }
    
    public int getRateClassCodeIncl() {
        return rateClassCodeIncl;
    }
    
    public void setRateClassCodeIncl(int rateClassCodeIncl) {
        this.rateClassCodeIncl = rateClassCodeIncl;
    }
    
    public int getRateTypeCodeIncl() {
        return rateTypeCodeIncl;
    }
    
    public void setRateTypeCodeIncl(int rateTypeCodeIncl) {
        this.rateTypeCodeIncl = rateTypeCodeIncl;
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
    
    public boolean isLike(ComparableBean comparableBean) throws CoeusException{
        return true;
    }
    
    public boolean equals(CoeusBean coeusBean) throws CoeusException {
        return isLike(coeusBean);
    }
    
    public int getRowIndex() {
        return rowIndex;
    }
    
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
    
    
    
    public boolean isIsCalculated() {
        return isCalculated;
    }
    
    public void setIsCalculated(boolean isCalculated) {
        this.isCalculated = isCalculated;
    }
    
    public int getParentRowIndex() {
        return parentRowIndex;
    }
    
    public void setParentRowIndex(int parentRowIndex) {
        this.parentRowIndex = parentRowIndex;
    }
    
    
    
    
    
}
