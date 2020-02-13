/*
 * Period.java
 *
 * Created on October 30, 2007, 10:27 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.utils;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 *
 * @author talarianand
 */
public class Period {
    
    private Date startDate;
    
    private Date endDate;
    
    /** Creates a new instance of Period */
    public Period(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    /**
     * Creates a new instance of Period from a start date and duration
     * 
     * @param date  start date of the period
     * @field  unit of duration like day, month, year, etc.(expressed as Calendar field)
     * @duration count of units between start date and end date
     */
    public Period(Date date, int field, int duration ){
        this.startDate = date;
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getDefault()); 
        calendar.setTime(startDate);
        calendar.add(field, duration);
        
        // Reduce the date by 1 day, to ensure the period is inclusive of start and
        // end date
        calendar.add(Calendar.DATE, -1);
        endDate = calendar.getTime();
    }
    
    /**
     * Is used to get the start date
     * @return Date start date
     */
    public Date getStartDate() {
        return startDate;
    }
    
    /**
     *  This method sets the startDate
     *  @param startDate
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    /**
     * Is used to get the end date
     * @return java.util.Date end date
     */
    public Date getEndDate() {
        return endDate;
    }
    
    /**
     *  This method sets the endDate
     *  @param endDate
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    /**
     * Finds the periods that is common to the mainPeriod and subPeriod
     * 
     * @param mainPeriod the main period to which the comparison is done
     * @param subPeriod the sub period which is used for comparing with main period
     * @return Period period to the given main period and sub period
     */
      public static Period getOverlappingPeriod(Period mainPeriod, Period subPeriod){
        Period period = null;
        Date startDateSubPeriod = subPeriod.getStartDate();
        Date endDateSubPeriod = subPeriod.getEndDate();
        Date startDateMainPeriod = mainPeriod.getStartDate();
        Date endDateMainPeriod = mainPeriod.getEndDate();

        if (startDateSubPeriod.before(startDateMainPeriod)) {
            // 1. Subperiod ends is before main period
            if (endDateSubPeriod.before(startDateMainPeriod)) {
                period = null;
            } 
            
            // 2. Sub period partially included in main period
            else if (endDateSubPeriod.before(endDateMainPeriod)){
                period = new Period(startDateMainPeriod, endDateSubPeriod);
            }
            
            // 3. Subperiod bigger than main period
            else {
                period = new Period(startDateMainPeriod,endDateMainPeriod);                                
            }             
        }
        else {
            // 4: Sub period starts after main period
            if (endDateMainPeriod.before( startDateSubPeriod) ) {
                period = null;
            }
            
            // 5: Sub Period is within main period
            else if ( endDateMainPeriod.after(endDateSubPeriod)) {
                period = new Period(startDateSubPeriod, endDateSubPeriod);
            }
            
            // 6: Sub period partially overlaps main period
            else {
                period = new Period(startDateSubPeriod, endDateMainPeriod);                
            }
            
        }
        return period;
    }
      
    public String toString(){
        if(this!=null){
            return "Start Date:" + this.startDate + " End Date:"+ this.endDate;
        }else 
            return "Null";
    }
}
