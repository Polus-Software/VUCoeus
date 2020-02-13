/*
 * @(#)BudgetBean.java September 26, 2003, 12:58 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.budget.bean;

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

public class InstituteLARatesBean extends RatesBean {

    //holds unitNumber
    private String unitNumber = null;    
    
    /** Creates a new instance of InstituteLARatesBean */
    public InstituteLARatesBean() {
        
    }   
    
    /** Getter for property unitNumber.
     * @return Value of property unitNumber.
     */
    public java.lang.String getUnitNumber() {
        return unitNumber;
    }    
    
    /** Setter for property unitNumber.
     * @param unitNumber New value of property unitNumber.
     */
    public void setUnitNumber(java.lang.String unitNumber) {
        this.unitNumber = unitNumber;
    }   
    
    public boolean isLike(ComparableBean comparableBean) throws CoeusException{
        boolean success = false;        
        if(comparableBean instanceof InstituteLARatesBean){
            InstituteLARatesBean instituteLARatesBean = (InstituteLARatesBean)comparableBean;
            //Unit Number
            if(instituteLARatesBean.getUnitNumber()!=null){
                if(getUnitNumber().equals(instituteLARatesBean.getUnitNumber())){
                    success = true;
                }else{
                    return false;
                }
            }
            
            //Rate Class Code
            if(instituteLARatesBean.getRateClassCode()!=-1){
                if(getRateClassCode() == instituteLARatesBean.getRateClassCode()){
                    success = true;
                }else{
                    return false;
                }
            }
            
            //Rate Type Code
            if(instituteLARatesBean.getRateTypeCode()!=-1){
                if(getRateTypeCode() == instituteLARatesBean.getRateTypeCode()){
                    success = true;
                }else{
                    return false;
                }
            }
            
            //Fiscal year
            if(instituteLARatesBean.getFiscalYear()!=null){
                if(getFiscalYear().equals(instituteLARatesBean.getFiscalYear())){
                    success = true;
                }else{
                    return false;
                }
            }

            //Start Date
            if(instituteLARatesBean.getStartDate()!=null){
                if(getStartDate().equals(instituteLARatesBean.getStartDate())){
                    success = true;
                }else{
                    return false;
                }
            } 
            
            //On Off Campus Flag
            /*if(isOnOffCampusFlag() == instituteLARatesBean.isOnOffCampusFlag()){
                success = true;
            }else{
                return false;
            }*/
        }else{
            throw new CoeusException("budget_exception.1000");
        }
        return success;
    }    
    
     // Added by Chandra 04/10/2003 - start
    public boolean equals(Object obj) {
        InstituteLARatesBean instituteLARatesBean = (InstituteLARatesBean)obj;
        if(instituteLARatesBean.getUnitNumber().equals(getUnitNumber()) &&
        instituteLARatesBean.getRateTypeCode() == getRateTypeCode() &&
        instituteLARatesBean.getRateClassCode() == getRateClassCode() &&
        instituteLARatesBean.getFiscalYear().equals(getFiscalYear())&&
        instituteLARatesBean.getStartDate().equals(getStartDate())&&
        instituteLARatesBean.isOnOffCampusFlag()==(isOnOffCampusFlag())){
            return true;
        }else{
            return false;
        }
    }
     // Added by Chandra 04/10/2003 - end
    
 } // end BudgetBean