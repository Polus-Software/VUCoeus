/**
 * @(#)AwardApprovedSubcontract.java 1.0 01/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;
import java.util.Vector;
import java.sql.Date;
/**
 * This class is used to hold Award Approved Subcontract data
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on March 05, 2004 12:30 PM
 */

public class AwardApprovedForeignTripBean extends AwardBaseBean implements java.io.Serializable{
    
    private String personId;    
    private String personName;
    private String destination;    
    private Date dateFrom;    
    private Date dateTo;
    private double amount;
    private String aw_PersonId;
    private String aw_Destination;
    private Date aw_DateFrom;    
    private int rowId;
    
    /**
     *	Default Constructor
     */
    
    public AwardApprovedForeignTripBean(){
    }    
    
    /** Getter for property personId.
     * @return Value of property personId.
     */
    public java.lang.String getPersonId() {
        return personId;
    }
    
    /** Setter for property personId.
     * @param personId New value of property personId.
     */
    public void setPersonId(java.lang.String personId) {
        this.personId = personId;
    }
    
    /** Getter for property personName.
     * @return Value of property personName.
     */
    public java.lang.String getPersonName() {
        return personName;
    }
    
    /** Setter for property personName.
     * @param personName New value of property personName.
     */
    public void setPersonName(java.lang.String personName) {
        this.personName = personName;
    }
    
    /** Getter for property destination.
     * @return Value of property destination.
     */
    public java.lang.String getDestination() {
        return destination;
    }
    
    /** Setter for property destination.
     * @param destination New value of property destination.
     */
    public void setDestination(java.lang.String destination) {
        this.destination = destination;
    }
    
    /** Getter for property dateFrom.
     * @return Value of property dateFrom.
     */
    public java.sql.Date getDateFrom() {
        return dateFrom;
    }
    
    /** Setter for property dateFrom.
     * @param dateFrom New value of property dateFrom.
     */
    public void setDateFrom(java.sql.Date dateFrom) {
        this.dateFrom = dateFrom;
    }
    
    /** Getter for property dateTo.
     * @return Value of property dateTo.
     */
    public java.sql.Date getDateTo() {
        return dateTo;
    }
    
    /** Setter for property dateTo.
     * @param dateTo New value of property dateTo.
     */
    public void setDateTo(java.sql.Date dateTo) {
        this.dateTo = dateTo;
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
    
    /** Getter for property aw_PersonId.
     * @return Value of property aw_PersonId.
     *
     */
    public java.lang.String getAw_PersonId() {
        return aw_PersonId;
    }
    
    /** Setter for property aw_PersonId.
     * @param aw_PersonId New value of property aw_PersonId.
     *
     */
    public void setAw_PersonId(java.lang.String aw_PersonId) {
        this.aw_PersonId = aw_PersonId;
    }
    
    /** Getter for property aw_Destination.
     * @return Value of property aw_Destination.
     *
     */
    public java.lang.String getAw_Destination() {
        return aw_Destination;
    }
    
    /** Setter for property aw_Destination.
     * @param aw_Destination New value of property aw_Destination.
     *
     */
    public void setAw_Destination(java.lang.String aw_Destination) {
        this.aw_Destination = aw_Destination;
    }
    
    /** Getter for property aw_DateFrom.
     * @return Value of property aw_DateFrom.
     *
     */
    public java.sql.Date getAw_DateFrom() {
        return aw_DateFrom;
    }
    
    /** Setter for property aw_DateFrom.
     * @param aw_DateFrom New value of property aw_DateFrom.
     *
     */
    public void setAw_DateFrom(java.sql.Date aw_DateFrom) {
        this.aw_DateFrom = aw_DateFrom;
    }
    
     /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        if(super.equals(obj)){
            AwardApprovedForeignTripBean awardApprovedForeignTripBean = 
                    (AwardApprovedForeignTripBean)obj;
            if(awardApprovedForeignTripBean.getRowId()==getRowId()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }    
}