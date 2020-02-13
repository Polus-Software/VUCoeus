/*
 * @(#)NSFSeniorPersonnelBean.java 
 *
 * This bean contains information for printing NSFSenior Personnel
 * added as part of NSF schema extensions
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.utils.xml.bean.proposal.bean;

import edu.mit.coeus.bean.CoeusBean;
import java.beans.*;
import java.util.Vector;
import java.math.*;
 

public class NSFSeniorPersonnelBean implements CoeusBean, java.io.Serializable{
 
  
    
    private int rowNumber;  
    private String name; 
    private String personId;
    private String title = null;
    private int budgetPeriod;
    private BigDecimal summerMonthsFunded ;
    private BigDecimal academicMonthsFunded ;
    private BigDecimal calendarMonthsFunded ;
    private BigDecimal fundsRequested ;
   
    private String updateUser = null;
    //holds update timestamp - needed to implement CoeusBean
    private java.sql.Timestamp updateTimestamp = null;
    //holds account type - needed to implement CoeusBean
    private String acType = null;

 
    /**
     *	Default Constructor
     */
    
    public NSFSeniorPersonnelBean(){
    }
      
   
    /** Getter for property rowNumber.
     * @return Value of property rowNumber.
    */
    public int getRowNumber() {
        return rowNumber;
    }    
    
    /** Setter for property rowNumber.
     * @param rowNumber New value of property rowNumber.
    */
    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }
    
    /** Getter for property name.
     * @return Value of property name.
     */
    public String getName() {
        return name;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     */
    public void setName(String name) {
        this.name = name;
    }
    
      /** Getter for property personId.
     * @return Value of property personId.
     */
    public String getPersonId() {
        return personId;
    }
    
    /** Setter for property personId.
     * @param personId New value of property personId.
     */
    public void setPersonId(String personId) {
        this.personId = personId;
    }
    
     /** getter for property budgetPeriod.
     * @return Value of property budgetPeriod.
     */
    public int getBudgetPeriod() {
        return budgetPeriod;
    }    
    
    /** Setter for property budgetPeriod.
     * @param budgetPeriod New value of property budgetPeriod.
    */
    public void setBudgetPeriod(int budgetPeriod) {
        this.budgetPeriod = budgetPeriod;
    }
    
     /** Getter for property title.
     * @return Value of property title.
     */
    public java.lang.String getTitle() {
        return title;
    }
    
    /** Setter for property title.
     * @param title New value of property title.
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }
   
  
     /** Getter for property summerMonthsFunded.
     * @return Value of property summerMonthsFunded.
     */
    public BigDecimal getSummerMonthsFunded() {
        return summerMonthsFunded;
    }    
    
    /** Setter for property summerMonthsFunded.
     * @param summerMonthsFunded New value of property summerMonthsFunded.
     */
    public void setSummerMonthsFunded(BigDecimal summerMonthsFunded) {
        this.summerMonthsFunded = summerMonthsFunded;
    }
    
    /** Getter for property academicMonthsFunded.
     * @return Value of property academicMonthsFunded.
     */
    public BigDecimal getAcademicMonthsFunded() {
        return academicMonthsFunded;
    }    
    
    /** Setter for property academicMonthsFunded
     * @param academicMonthsFunded New value of property academicMonthsFunded.
     */
    public void setAcademicMonthsFunded(BigDecimal academicMonthsFunded) {
        this.academicMonthsFunded = academicMonthsFunded;
    }
    /** Getter for property calendarMonthsFunded.
     * @return Value of property calendarMonthsFunded.
     */
    public BigDecimal getCalendarMonthsFunded() {
        return calendarMonthsFunded;
    }
    
    /** Setter for property calendarMonthsFunded.
     * @param calendarMonthsFunded New value of property calendarMonthsFunded.
     */
    public void setCalendarMonthsFunded(BigDecimal calendarMonthsFunded) {
        this.calendarMonthsFunded = calendarMonthsFunded;
    }
    
    /** Getter for property fundsRequested.
     * @return Value of property fundsRequested.
     */
    public BigDecimal getFundsRequested() {
        return fundsRequested;
    }
    
    /** Setter for property fundsRequested.
     * @param fundsRequested New value of property fundsRequested.
     */
    public void setFundsRequested(BigDecimal fundsRequested) {
        this.fundsRequested = fundsRequested;
    }
   
    
    
     // following methods are necessary to implement CoeusBean  
   
    public String getUpdateUser() {
     return updateUser;
    }
    public void setUpdateUser(String userId) {
    }
    
  public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) throws edu.mit.coeus.exception.CoeusException {
    return true;
  }

    public java.lang.String getAcType() {
     return acType;
    }
    
    public void setAcType(java.lang.String acType) {      
    }  
   
    public java.sql.Timestamp getUpdateTimestamp() {
     return updateTimestamp;
    }
    
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {   
    }
    
    
}