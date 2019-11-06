/* 
 * @(#)Boundary.java October 14, 2003, 12:58 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.budget.calculator;

import java.util.*;
import java.util.Date;

/**
 * This class holds the date boundaries ie, Start Date & End Date. Also
 * have methods which to get the date related values like the no of days in
 * StartDate, month of StartDate, etc.
 *
 * @author  Sagin
 * @version 1.0
 * Created on October 14, 2003, 12:58 PM
 * 
 */
public class Boundary {

  ///////////////////////////////////////
  // attributes


/**
 * Represents boundary Start Date
 */
    private Date startDate; 

/**
 * Represents boundary End Date
 */
    private Date endDate; 

/**
 * Represents Calendar
 */
    private Calendar calendar = Calendar.getInstance();

    private Hashtable noOfDaysList;
    
  ///////////////////////////////////////
  // operations


/**
 * Default Constructor...
 */
    public  Boundary() {        
        // your code here
    } // end Boundary 


/**
 * Constructor...
 * Accepts 2 parameters, viz startDate & endDate and sets the values to the 
 * class variables
 * 
 * @param startDate 
 * @param endDate 
 */
    public  Boundary(Date startDate, Date endDate) {        
        this.startDate = startDate;
        this.endDate = endDate;
    } // end Boundary       

    
/**
 * Returns the startDate Month
 * 
 * @return int
 */
    public int getStartDateMonth() {        
        calendar.setTime(startDate);
        return calendar.get(Calendar.MONTH)+1;
    } // end getStartDateMonth        

    public Hashtable getNoOfDaysList(){
        noOfDaysList = new Hashtable();
        calendar.setTime(startDate);
        int stYear = calendar.get(Calendar.YEAR);
        int stMonth = calendar.get(Calendar.MONTH);
        calendar.setTime(endDate);
        int endYear = calendar.get(Calendar.YEAR);
        int endMonth = calendar.get(Calendar.MONTH);
        /** Bug Fix 2041 start - step1
         *Get the static month count
         */
        int staticMonth =0;
        if(endMonth==0){
            staticMonth =12;
        }else{
            staticMonth =13;
        }// End Bug Fix 2041 - step1
        
        if(stYear == endYear && stMonth==endMonth){
                noOfDaysList.put(startDate,
                                new Integer((int)this.getDateDifference()));
        }else{
            noOfDaysList.put(startDate, new Integer(this.getStartDateRemainingDays()));
            for(int yrIndex=stYear;yrIndex<=endYear;yrIndex++){
                /** Bug Fix 2041 - staticMonth is added
                 *start - step2
                 */
                int tmpEndMonth = yrIndex==endYear?endMonth:staticMonth;
                // Bug Fix 2041 - End - step2
                for(int monthIndex=stMonth+1;monthIndex<tmpEndMonth;monthIndex++){
                    calendar.set(yrIndex,monthIndex,1);
                    int noOfDays = calendar.getActualMaximum(Calendar.DATE);
                    noOfDaysList.put(calendar.getTime(), new Integer(noOfDays));
                }
                stMonth=-1;
            }
            noOfDaysList.put(endDate,new Integer(this.getEndDateNoOfDays()));
        }
        return noOfDaysList;
    }
    
/**
 * Returns the total no. of days in startDate
 * 
 * @return int
 */
    public int getStartDateTotalDays() {        
        calendar.setTime(startDate);
        return calendar.getActualMaximum(Calendar.DATE);
    } // end getStartDateNoOfDays        

/**
 * Returns the remaining no. of days in startDate
 * 
 * @return int
 */
    public int getStartDateRemainingDays() {        
        calendar.setTime(startDate);
        return calendar.getActualMaximum(Calendar.DATE) - calendar.get(Calendar.DATE) + 1;
    } // end getStartDateNoOfDays       

/**
 * Returns the endDate Month * 
 * 
 * @return int
 */
    public int getEndDateMonth() {        
        calendar.setTime(endDate);
        return calendar.get(Calendar.MONTH)+1;
    }

/**
 * Returns the total no. of days in endDate
 * 
 * @return int
 */
    public int getEndDateTotalDays() {        
        calendar.setTime(endDate);
        return calendar.getActualMaximum(Calendar.DATE);
    }        

/**
 * Returns the no. of days in endDate
 * 
 * @return int
 */
    public int getEndDateNoOfDays() {        
        calendar.setTime(endDate);
        return calendar.get(Calendar.DATE);
    }   

/**
 * Returns the no. of days between the startDate & endDate
 * 
 * @return long
 */
    public long getDateDifference() {
//        Calendar startCal = Calendar.getInstance();
//        startCal.setTime(startDate);
//        startCal.set(Calendar.AM_PM,Calendar.AM);
//        startCal.set(Calendar.HOUR,0);
//        startCal.set(Calendar.MINUTE,0);
//        startCal.set(Calendar.SECOND,0);
//        long startTimeInMillis = startCal.getTimeInMillis();
//
//        Calendar endCal = Calendar.getInstance();
//        endCal.setTime(endDate);
//        endCal.set(Calendar.AM_PM,Calendar.AM);
//        endCal.set(Calendar.HOUR,0);
//        endCal.set(Calendar.MINUTE,0);
//        endCal.set(Calendar.SECOND,0);
//        long endTimeInMillis = endCal.getTimeInMillis();
//        
//        System.out.println((endTimeInMillis - startTimeInMillis)/86400000d);
//        return Math.round(((endTimeInMillis - startTimeInMillis)/86400000d)+1);
        return Math.round((endDate.getTime() - startDate.getTime())/86400000.0d + 1);
    }
    
    /** Getter for property startDate.
     * @return Value of property startDate.
     */
    public java.util.Date getStartDate() {
        return startDate;
    }
    
    /** Setter for property startDate.
     * @param startDate New value of property startDate.
     */
    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }
    
    /** Getter for property endDate.
     * @return Value of property endDate.
     */
    public java.util.Date getEndDate() {
        return endDate;
    }
    
    /** Setter for property endDate.
     * @param endDate New value of property endDate.
     */
    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }
    /**
     * Overridden method of toString. It will form a string representation of
     * each element associated with this class.
     * @return Concatinated string representation of each element 
     */
    public String toString(){
        StringBuffer strBffr = new StringBuffer("");
        strBffr.append("Start date=>"+startDate);
        strBffr.append(";");
        strBffr.append("End date=>"+endDate);  
        strBffr.append("\n");
        return strBffr.toString();
    }
    
    /** For testing **/
    /*public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();
        cal.set(2001,10,10);
        Date startDate = cal.getTime();
        cal.set(2002,1,18);
        Date endDate = cal.getTime();
//        Date startDate = new Date("03/10/2001");
//        Date endDate = new Date("03/09/2002");
        Boundary boundary = new Boundary(startDate, endDate);
        System.out.println("StartDate - " + boundary.getStartDate());
        System.out.println("EndDate - " + boundary.getEndDate());
        System.out.println("NoofDays - " + boundary.getDateDifference());
        System.out.println("StartDateMonth - " + boundary.getStartDateMonth());
        System.out.println("StartDateTotalDays - " + boundary.getStartDateTotalDays());
        System.out.println("StartDateRemainingDays - " + boundary.getStartDateRemainingDays());
        System.out.println("EndDateMonth - " + boundary.getEndDateMonth());
        System.out.println("EndDateTotalDays - " + boundary.getEndDateTotalDays());
        System.out.println("EndDateNoOfDays - " + boundary.getEndDateNoOfDays());
        System.out.println("No of days list=>"+boundary.getNoOfDaysList().toString());
    }*/
    
 } // end Boundary



