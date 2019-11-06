/*
 * @(#)CoeusParameterBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.bean;

import edu.mit.coeus.bean.BaseBean;
import java.sql.Date;
/**
 * The class used to hold the information of <code>Parameter Name and Values</code>
 * 
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on April 30, 2004, 12:26 PM
 */

public class CoeusParameterBean implements java.io.Serializable, BaseBean {

    private String parameterName;
    private String parameterValue;
    private Date effectiveDate;    
    private String updateUser;
    private java.sql.Timestamp updateTimestamp;
    private String acType;
    
    /** Creates new ParameterBean */
    public CoeusParameterBean() {
    }
    
    /** Getter for property parameterName.
     * @return Value of property parameterName.
     */
    public java.lang.String getParameterName() {
        return parameterName;
    }
    
    /** Setter for property parameterName.
     * @param parameterName New value of property parameterName.
     */
    public void setParameterName(java.lang.String parameterName) {
        this.parameterName = parameterName;
    }
    
    /** Getter for property parameterValue.
     * @return Value of property parameterValue.
     */
    public java.lang.String getParameterValue() {
        return parameterValue;
    }
    
    /** Setter for property parameterValue.
     * @param parameterValue New value of property parameterValue.
     */
    public void setParameterValue(java.lang.String parameterValue) {
        this.parameterValue = parameterValue;
    }
    
    /** Getter for property effectiveDate.
     * @return Value of property effectiveDate.
     */
    public java.sql.Date getEffectiveDate() {
        return effectiveDate;
    }
    
    /** Setter for property effectiveDate.
     * @param effectiveDate New value of property effectiveDate.
     */
    public void setEffectiveDate(java.sql.Date effectiveDate) {
        this.effectiveDate = effectiveDate;
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
}