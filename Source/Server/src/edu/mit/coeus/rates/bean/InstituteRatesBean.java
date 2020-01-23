/*
 * @(#)InstituteRatesBean.java September 26, 2003, 12:58 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.rates.bean;

import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.bean.PrimaryKey;
import java.util.*;
import edu.mit.coeus.exception.CoeusException;

/**
 * The class is the base class of all Budget related classes
 *
 * @author  Prasanna Kumar
 * @version 1.0
 * Created on September 26, 2003, 12:58 PM
 */

public class InstituteRatesBean extends RatesBean{

    //holds activityTypeCode
    private int activityCode = -1;
    //holds activity Type Description
    private String activityTypeDescription = null;
    
    /** Creates a new instance of InstituteRatesBean */
    public InstituteRatesBean() {
        
    }   
    
    /** Getter for property activityCode.
     * @return Value of property activityCode.
     */
    public int getActivityCode() {
        return activityCode;
    }    
    
    /** Setter for property activityCode.
     * @param activityCode New value of property activityCode.
     */
    public void setActivityCode(int activityCode) {
        this.activityCode = activityCode;
    }    
    
    public boolean isLike(ComparableBean comparableBean) throws CoeusException {
        boolean success = false;        
        if(comparableBean instanceof InstituteRatesBean){
            InstituteRatesBean instituteRatesBean = (InstituteRatesBean)comparableBean;
            //Rate Class Code
            if(instituteRatesBean.getRateClassCode()!=-1){
                if(getRateClassCode() == instituteRatesBean.getRateClassCode()){
                    success = true;
                }else{
                    return false;
                }
            }            
            //Rate Type Code
            if(instituteRatesBean.getRateTypeCode()!=-1){
                if(getRateTypeCode() == instituteRatesBean.getRateTypeCode()){
                    success = true;
                }else{
                    return false;
                }
            }            
            //Activity Type Code
            if(instituteRatesBean.getActivityCode()!=-1){
                if(getActivityCode() == instituteRatesBean.getActivityCode()){
                    success = true;
                }else{
                    return false;
                }
            }          
            //Fiscal year
            if(instituteRatesBean.getFiscalYear()!=null){
                if(getFiscalYear().equals(instituteRatesBean.getFiscalYear())){
                    success = true;
                }else{
                    return false;
                }
            }
            //Start Date
            if(instituteRatesBean.getStartDate()!=null){
                if(getStartDate().equals(instituteRatesBean.getStartDate())){
                    success = true;
                }else{
                    return false;
                }
            }
            //On Off Campus Flag
            /*if(isOnOffCampusFlag() == instituteRatesBean.isOnOffCampusFlag()){
                success = true;
            }else{
                return false;
            }*/
        }else{
            throw new CoeusException("budget_exception.1000");
        }
        return success;
    }    
    
    /** Getter for property activityTypeDescription.
     * @return Value of property activityTypeDescription.
     */
    public java.lang.String getActivityTypeDescription() {
        return activityTypeDescription;
    }
    
    /** Setter for property activityTypeDescription.
     * @param activityTypeDescription New value of property activityTypeDescription.
     */
    public void setActivityTypeDescription(java.lang.String activityTypeDescription) {
        this.activityTypeDescription = activityTypeDescription;
    }
    
      // Added by Chandra 04/10/2003 - start
    public boolean equals(Object obj) {
        InstituteRatesBean instituteRatesBean = (InstituteRatesBean)obj;
        if(instituteRatesBean.getUnitNumber().equals(getUnitNumber()) &&
        instituteRatesBean.getRowId() == getRowId()){
            return true;
        }else{
            return false;
        }
    }
     // Added by Chandra 04/10/2003 - end
    
 } // end InstituteRatesBean