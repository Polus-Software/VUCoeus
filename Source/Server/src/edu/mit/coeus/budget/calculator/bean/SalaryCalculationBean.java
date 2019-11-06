/*
 * @(#)SalaryCalculationBean.java October 14, 2003, 12:58 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.budget.calculator.bean;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.budget.calculator.Boundary;
import edu.mit.coeus.exception.CoeusException;
import java.math.BigDecimal;
import java.util.*;
import edu.mit.coeus.utils.AppointmentTypeConstants;

/**
 * Holds all the Salary Calculation Info for each breakup period. Also performs the 
 * calculation of the requested salary.
 *
 * @author  Sagin 
 * @version 1.0
 * Created on October 14, 2003, 12:58 PM
 *
 */
public class SalaryCalculationBean {

  ///////////////////////////////////////
  // attributes


/**
 * Represents the date boundaries ie, Start Date &amp; End Date. Also
 * have methods which to get the date related values like the no of days in
 * StartDate, month of StartDate, etc.
 */
    private Boundary boundary; 

/**
 * Represents the Appointement Type for the Budget Person.
 */
    private String appointmentType; 

/**
 * Represents the Actual Base Salary from the Budget Persons table.
 */
    private double actualBaseSalary; 

/**
 * Represents the calculated Salary. This value is set using the calculateSalary method 
 * which makes use of Boundary, Appointment Type and Actual Base Salary.
 */
    private double calculatedSalary; 


  ///////////////////////////////////////
  // operations


/**
 * Does calculate the calculatedSalary based on the Boundary, Appointment Type & 
 * Base Salary.
 * 
 */
    public double calculateSalary() {
        
        double monthlySalary=0;
        // Get the paid months from the AppointmentTypeConstants
        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
        //Integer workingMonths = new Integer(12);
        Double workingMonths = new Double(12);
        if(appointmentType!=null){
            //workingMonths = (Integer) AppointmentTypeConstants.appointmentTypes.get(
            //                                        appointmentType.trim());
            AppointmentTypeConstants.fetchAppointmentTypes();
            BigDecimal workMonths = (BigDecimal)AppointmentTypeConstants.appointmentTypes.get(
                                                       appointmentType.trim());
            if(workMonths != null){
                workingMonths = workMonths.doubleValue();
            }
        }
        //int paidMonths = (workingMonths==null)?12:(workingMonths.intValue());
        double paidMonths = (workingMonths==null)?12:(workingMonths);
        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
        double perMonthSalary = this.getActualBaseSalary()/paidMonths;
        
        Hashtable noOfDaysList = this.boundary.getNoOfDaysList();
        Enumeration enumerate = noOfDaysList.keys();
        while(enumerate.hasMoreElements()){
            Date date = (Date)enumerate.nextElement();
            Calendar calc = Calendar.getInstance();
            calc.setTime(date);
            int noOfDays = ((Integer)noOfDaysList.get(date)).intValue();
            int noOfTotalDays = calc.getActualMaximum(Calendar.DATE);
            calculatedSalary+=(perMonthSalary/noOfTotalDays*noOfDays);
        }
//        System.out.println("calculated salary=>"+calculatedSalary);
        return calculatedSalary;
    }
    
    /** Getter for property boundary.
     * @return Value of property boundary.
     */
    public edu.mit.coeus.budget.calculator.Boundary getBoundary() {
        return boundary;
    }
    
    /** Setter for property boundary.
     * @param boundary New value of property boundary.
     */
    public void setBoundary(edu.mit.coeus.budget.calculator.Boundary boundary) {
        this.boundary = boundary;
    }
    
    /** Getter for property appointmentType.
     * @return Value of property appointmentType.
     */
    public java.lang.String getAppointmentType() {
        return appointmentType;
    }
    
    /** Setter for property appointmentType.
     * @param appointmentType New value of property appointmentType.
     */
    public void setAppointmentType(java.lang.String appointmentType) {
        this.appointmentType = appointmentType;
    }
    
    /** Getter for property actualBaseSalary.
     * @return Value of property actualBaseSalary.
     */
    public double getActualBaseSalary() {
        return actualBaseSalary;
    }
    
    /** Setter for property actualBaseSalary.
     * @param actualBaseSalary New value of property actualBaseSalary.
     */
    public void setActualBaseSalary(double actualBaseSalary) {
        this.actualBaseSalary = actualBaseSalary;
    }
    
    /** Getter for property calculatedSalary.
     * @return Value of property calculatedSalary.
     */
    public double getCalculatedSalary() {
        return calculatedSalary;
    }
    
    /** Setter for property calculatedSalary.
     * @param calculatedSalary New value of property calculatedSalary.
     */
    public void setCalculatedSalary(double calculatedSalary) {
        this.calculatedSalary = calculatedSalary;
    }
    
    /**
     * Overridden method of toString. It will form a string representation of
     * each element associated with this class.
     * @return Concatinated string representation of each element 
     */
    public String toString(){
        StringBuffer strBffr = new StringBuffer("");
        strBffr.append("Actual Base Salary=>"+actualBaseSalary);
        strBffr.append(";");
        strBffr.append("Appointment Type=>"+appointmentType);  
        strBffr.append(";");
        strBffr.append("Boundary=>"+boundary.toString());
        strBffr.append(";");
        strBffr.append("Calculated salary=>"+calculatedSalary);
        strBffr.append("\n");
        return strBffr.toString();
    }
    
 // end calculateSalary            
 } // end SalaryCalculationBean



