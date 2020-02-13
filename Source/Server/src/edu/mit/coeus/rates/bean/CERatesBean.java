/*
 * CERatesBean.java
 *
 * Created on August 17, 2007, 2:16 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.rates.bean;

import edu.mit.coeus.bean.BaseBean;
import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author  talarianand
 */
public class CERatesBean implements BaseBean, java.io.Serializable {
    
    private String unitNumber = null;
    private String costElement = null;
    private String description = null;
    private String fiscalYear = null;
    private double monthlyRate = 0.0;
    private String updateUser = null;
    private Timestamp updateTimeStamp = null;
    private String acType = null;
    private Date startDate = null;
    private Date oldStrdate = null;
    
    /** Creates a new instance of CERatesBean */
    public CERatesBean() {
    }
    
    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }
    
    public String getUnitNumber() {
        return unitNumber;
    }
    
    public void setCostElement(String costElement) {
        this.costElement = costElement;
    }
    
    public String getCostElement() {
        return costElement;
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
    
    public void setUpdateTimeStamp(Timestamp timeStamp) {
        this.updateTimeStamp = timeStamp;
    }
    
    public Timestamp getUpdateTimeStamp() {
        return updateTimeStamp;
    }
    
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    
    public String getUpdateUser() {
        return updateUser;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setAcType(String acType) {
        this.acType = acType;
    }
    
    public String getAcType() {
        return acType;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public void setModStrDate(Date modStrDate) {
        this.oldStrdate = modStrDate;
    }
    
    public Date getModStrDate() {
        return oldStrdate;
    }
}
