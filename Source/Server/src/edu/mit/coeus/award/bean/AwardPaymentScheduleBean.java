/**
 * @(#)AwardPaymentScheduleBean.java 1.0 01/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;
import java.util.Vector;
import java.sql.Date;
/**
 * This class is used to hold Award Payment Schedule data
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on March 05, 2004 12:30 PM
 */

public class AwardPaymentScheduleBean extends AwardBaseBean{
    
    private Date dueDate;    
    private double amount;
    private Date submitDate;
    private String submitBy;
    private String invoiceNumber;
    private String statusDescription;
    private String personFullName;
    private int rowId;
    private Date aw_Duedate;
    /**
     *	Default Constructor
     */
    
    public AwardPaymentScheduleBean(){        
    }              
    
    /** Getter for property dueDate.
     * @return Value of property dueDate.
     */
    public java.sql.Date getDueDate() {
        return dueDate;
    }
    
    /** Setter for property dueDate.
     * @param dueDate New value of property dueDate.
     */
    public void setDueDate(java.sql.Date dueDate) {
        this.dueDate = dueDate;
    }
    
    /** Getter for property amount.
     * @return Value of property amount.
     */
    public double getAmount() {
        return amount;
    }
    
    /** Setter for property amount.
     * @param amount New value of property amount.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    /** Getter for property submitDate.
     * @return Value of property submitDate.
     */
    public java.sql.Date getSubmitDate() {
        return submitDate;
    }
    
    /** Setter for property submitDate.
     * @param submitDate New value of property submitDate.
     */
    public void setSubmitDate(java.sql.Date submitDate) {
        this.submitDate = submitDate;
    }
    
    /** Getter for property submitBy.
     * @return Value of property submitBy.
     */
    public java.lang.String getSubmitBy() {
        return submitBy;
    }
    
    /** Setter for property submitBy.
     * @param submitBy New value of property submitBy.
     */
    public void setSubmitBy(java.lang.String submitBy) {
        this.submitBy = submitBy;
    }
    
    /** Getter for property invoiceNumber.
     * @return Value of property invoiceNumber.
     */
    public java.lang.String getInvoiceNumber() {
        return invoiceNumber;
    }
    
    /** Setter for property invoiceNumber.
     * @param invoiceNumber New value of property invoiceNumber.
     */
    public void setInvoiceNumber(java.lang.String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
    
    /** Getter for property statusDescription.
     * @return Value of property statusDescription.
     */
    public java.lang.String getStatusDescription() {
        return statusDescription;
    }
    
    /** Setter for property statusDescription.
     * @param statusDescription New value of property statusDescription.
     */
    public void setStatusDescription(java.lang.String statusDescription) {
        this.statusDescription = statusDescription;
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
    
    /** Getter for property personFullName.
     * @return Value of property personFullName.
     *
     */
    public java.lang.String getPersonFullName() {
        return personFullName;
    }
    
    /** Setter for property personFullName.
     * @param personFullName New value of property personFullName.
     *
     */
    public void setPersonFullName(java.lang.String personFullName) {
        this.personFullName = personFullName;
    }
    
     /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        if(super.equals(obj)){
            AwardPaymentScheduleBean awardPaymentScheduleBean = 
                    (AwardPaymentScheduleBean)obj;
            if(awardPaymentScheduleBean.getRowId()==getRowId()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }    
    
    /**
     * Getter for property aw_Duedate.
     * @return Value of property aw_Duedate.
     */
    public java.sql.Date getAw_Duedate() {
        return aw_Duedate;
    }    
    
    /**
     * Setter for property aw_Duedate.
     * @param aw_Duedate New value of property aw_Duedate.
     */
    public void setAw_Duedate(java.sql.Date aw_Duedate) {
        this.aw_Duedate = aw_Duedate;
    }    
    
}