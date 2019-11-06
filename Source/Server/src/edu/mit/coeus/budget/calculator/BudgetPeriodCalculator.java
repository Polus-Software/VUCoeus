/*
 * BudgetPeriodCalculator.java
 *
 * Created on October 30, 2007, 12:59 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.budget.calculator;

import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Vector;

/**
 *
 * @author talarianand
 */
public class BudgetPeriodCalculator {
    
    /** Creates a new instance of BudgetPeriodCalculator */
    public BudgetPeriodCalculator() {
    }
    //Commented for case# 3121 - To change the period calculation to extend
    //the entire budget period - end
    
    //Added for case# 3121 - To change the period calculation to extend
    //the entire budget period - start
    /**
     * Is used to split the line item periods as per the budget period.
     * @param periodStartDate start date of the budget period
     * @param periodEndDate end date of the budget period
     * @param startingMonth starting month for the new line item start date
     * @param numMonths number of months for calculating the line item end date
     * @return Vector which consists of Line item periods
     */
    public Vector splitLineItemPeriods(Date periodStartDate, Date periodEndDate, int startingMonth,
    int numMonths) {
        
        Period budgetPeriod = new Period(periodStartDate, periodEndDate);
        
        Vector vecPeriods = new Vector();
        
        // For a line item period that cross a calendar year, there will be a
        // part period at the beginning of the year. To get this part period,
        // start checing the line item period one year before the start of the
        // budget period.
        Period lineItemPeriod = getApplicableLineitemPeriod(
        DateUtils.dateAdd(Calendar.YEAR,budgetPeriod.getStartDate(),-1),startingMonth,  numMonths);
        
        while (true ) {
            
            // If the calculated line item starts after the budget period end,
            // we have accounted all the applicable periods. The while loop can
            // be terminated.
            if(budgetPeriod.getEndDate().before(lineItemPeriod.getStartDate())) {
                break;
            }
            
            // Find the first applicable line item period for the budget period.
            Period period = Period.getOverlappingPeriod(budgetPeriod, lineItemPeriod);
            
            // For budget period starting after the end month of line item period
            // there will be no overlap, and null will be returned. Ignore the null values
            if (period != null) {
                vecPeriods.add(period);
            }
            
            // If the overlapping period is not found (period = null), or the line
            // item period is completely placed in the budget period (line item
            // end date = period end date), there will be no applicable
            // line item period for the calendar year. Try to find an overlapping
            // period in the next year.
            if ((period  == null) || (period.getEndDate().equals(lineItemPeriod.getEndDate()))) {
                // The line item period ended before budget period
                // get line item for next year
                lineItemPeriod = new Period(DateUtils.dateAdd(Calendar.YEAR, lineItemPeriod.getStartDate(),1),
                Calendar.MONTH,numMonths);
                // The line item is partially accomodated in the budget period.
            }else {
                // If the applicable period ends with budget period
                //  we have accounted all the applicable periods. The while loop can
                // be terminated.
                if (period.getEndDate().equals(budgetPeriod.getEndDate())) {
                    break;
                }
                
                // Shift the start date of budget period after the line item
                // period to continue finding the applicable periods again
                budgetPeriod.setEndDate(DateUtils.dateAdd(Calendar.DATE,period.getEndDate(),1));
                lineItemPeriod = getApplicableLineitemPeriod(budgetPeriod.getStartDate(),startingMonth,  numMonths);
            }
        }
        return vecPeriods;
    }
    /**
     * This method calculates the line item period applicable to the year of
     * the budget start date
     * @param budgetStartDate
     * @param startingMonth starting month for the new line item start date
     * @param numMonths number of months for calculating the line item end date
     * @return Period
     */
    private Period getApplicableLineitemPeriod(Date budgetStartDate, int startingMonth, int numMonths ) {
        GregorianCalendar calendar = new GregorianCalendar();
        
        calendar.setTime(budgetStartDate);
        //The calendar counts the months starting from 0.
        //Starting month is counted from 1.
        calendar.set(Calendar.MONTH, startingMonth - 1);
        Date newStartDate = calendar.getTime();
        
        return new Period(newStartDate,Calendar.MONTH, numMonths);
    }
    
    
    //Added for case# 3121 - To change the period calculation to extend
    //the entire budget period - end
    
    //Commented for case# 3121 - To change the period calculation to extend
    //the entire budget period - start
    /**
     * Is used to split the line item periods as per the budget period.
     * @param budgetPeriod Period object of Period, contains the budget period
     * start date and end date
     * @param startingMonth int Starting month for the new line item start date
     * @param numMonths int Number of months for calculating the line item end date
     * @return Vector which consists of Line item periods
     */
    /*public Vector splitLineItemPeriods(Period budgetPeriod, int startingMonth,
            int numMonths) {
        Vector vecLineItem = new Vector();
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getDefault());
        
        Date startDate = budgetPeriod.getStartDate();
        Date endDate = budgetPeriod.getEndDate();
        
        //Calculation of new Start date
        cal.setTime(startDate);
        cal.set(Calendar.MONTH, startingMonth - 1);
        Date newStartDate = cal.getTime();
        
        //Calculation of new End date
        cal.setTime(newStartDate);
        cal.add(Calendar.MONTH, numMonths);
        cal.add(Calendar.DATE, -1);
        Date newEndDate = cal.getTime();
        
        if (newStartDate.after(endDate)) {
            // Start date is after the budget period. Reset line item dates
            // to budget start and end date.
            int years = DateUtils.calculateDateDiff(Calendar.YEAR, startDate, newEndDate);
            cal.setTime(newEndDate);
            //Calculate the end date
            for(int i = 0; i < years; i++) {
                cal.add(Calendar.YEAR, -1);
                endDate = cal.getTime();
            }
            newStartDate = startDate;
            newEndDate = endDate;
            Period lineItem = new Period(newStartDate, newEndDate);
            vecLineItem.add(lineItem);
        } else
            if (!newStartDate.before(startDate)){
            // The new date is same as or after the budget period start
            // daate. Ensure that the end date is within the budget period.
            if (newEndDate.after(endDate)) {
                // if new end date is after budget period end date, check
                // if any approprivate months fit into the budget period, if yes,
                // create a new line item for the suitable months
                int years = DateUtils.calculateDateDiff(Calendar.YEAR, startDate, newEndDate);
                cal.setTime(newEndDate);
                for(int i = 0; i < years; i++) {
                    cal.add(Calendar.YEAR, -1);
                }
                if(years > 0 && !(cal.getTime().before(startDate))) {
                    int months = 0;
                    months = DateUtils.calculateDateDiff(Calendar.MONTH, newStartDate, endDate);
                    if(months > numMonths) {
                        months = (months + 1) - numMonths;
                    } else {
                        months = numMonths - ( months + 1 );
                    }
                    cal.setTime(startDate);
                    cal.add(Calendar.MONTH, months);
                    cal.add(Calendar.DATE, -1);
                    if(!cal.getTime().before(newStartDate)) {
                        cal.setTime(newStartDate);
                        cal.add(Calendar.DATE, -1);
                        Period lineItem = new Period(startDate, cal.getTime());
                        vecLineItem.add(lineItem);
                    } else {
                        Period lineItem = new Period(startDate, cal.getTime());
                        vecLineItem.add(lineItem);
                    }
                }
                newEndDate = endDate;
            }
            Period lineItem = new Period(newStartDate, newEndDate);
            vecLineItem.add(lineItem);
            } else if(newEndDate.before(startDate)) {
            // The start date is before the budget start date. try to
            // increment the years to find a date after the budget
            // start date.
            int years = DateUtils.calculateDateDiff(Calendar.YEAR, startDate, endDate);
            cal.setTime(newStartDate);
            for (int i =0; i<years; i++) {
                // increment the year
                cal.add(Calendar.YEAR,1);
                
                if (!cal.getTime().before( startDate)){
                    // The incremented date is found to be same as or,
                    // after the budget period start date. Calculate the
                    // end date and exit the loop.
                    newStartDate = cal.getTime();
                    cal.add(Calendar.MONTH, numMonths);
                    cal.add(Calendar.DATE, -1);
                    newEndDate = cal.getTime();
                    if(newEndDate.after(endDate)) {
                        newEndDate = endDate;
                    }
                    if(!newStartDate.after(endDate)) {
                        Period lineItem = new Period(newStartDate, newEndDate);
                        vecLineItem.add(lineItem);
                    }
                    break;
                }
            }
            } else {
            if(!newEndDate.after(endDate)) {
                if(!newEndDate.before(startDate)) {
                    Period lineItem = new Period(startDate, newEndDate);
                    vecLineItem.add(lineItem);
                }
                int months = DateUtils.calculateDateDiff(Calendar.MONTH, startDate, newEndDate);
                if(months > numMonths) {
                    months = ( months + 1 ) - numMonths;
                } else {
                    months = numMonths - ( months + 1 );
                }
                // The start date is before the budget start date. try to
                // increment the years to find a date after the budget
                // start date.
                int years = DateUtils.calculateDateDiff(Calendar.YEAR, startDate, endDate);
                cal.setTime(newStartDate);
                for (int i =0; i<years; i++) {
                    // increment the year
                    cal.add(Calendar.YEAR,1);
                    
                    if (!cal.getTime().before( startDate)){
                        // The incremented date is found to be same as or,
                        // after the budget period start date. Calculate the
                        // end date and exit the loop.
                        newStartDate = cal.getTime();
                        cal.add(Calendar.MONTH, months);
                        cal.add(Calendar.DATE, -1);
                        newEndDate = cal.getTime();
                        if(newEndDate.after(endDate)) {
                            newEndDate = endDate;
                        }
                        if(!newStartDate.after(endDate)) {
                            Period lineItem = new Period(newStartDate, newEndDate);
                            vecLineItem.add(lineItem);
                        }
                        break;
                    }
                }
            } else {
                newEndDate = endDate;
                // See if the start date is after the end date
                if (newStartDate.before(startDate) || newStartDate.after(endDate)) {
                    newStartDate = startDate;
                }
                if (newEndDate.after(endDate) || newEndDate.before(startDate)){
                    newEndDate = endDate;
                }
                Period lineItem = new Period(newStartDate, newEndDate);
                vecLineItem.add(lineItem);
            }
            }
        return vecLineItem;
    }*/
}
