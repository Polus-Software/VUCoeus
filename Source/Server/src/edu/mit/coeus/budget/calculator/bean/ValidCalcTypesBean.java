/*
 * @(#)ValidCalcTypesBean.java October 14, 2003, 12:58 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.budget.calculator.bean;

import java.util.*;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;

/**
 * Holds all the valid calculation types in the table OSP$VALID_CALC_TYPES
 *
 * @author  Sagin
 * @version 1.0
 * Created on October 14, 2003, 12:58 PM
 * 
 */
public class ValidCalcTypesBean  implements CoeusBean, java.io.Serializable{

  ///////////////////////////////////////
  // attributes


/**
 * Represents the Calculation Type ID (Primary Key)
 *
 */
    private String calcTypeID;
    
/**
 * Represents Rate Class Type
 * For Eg: L - Lab Allocation, O - OverHead, I - Inflation
 *
 */
    private String rateClassType;  

/**
 * Represents the sequence number to indicate multiple dependent rate class types. 
 * If no dependent rate class types, then this should be zero.
 */
    private int dependentSeqNumber = -1;

/**
 * Represents Rate Class Type on which the above Rate Class Type depends.
 * For eg: Rate Class Type O is dependent on Rate Class Types E & V
 */
    private String dependentRateClassType; 

/**
 * Represents the Rate Class Code of Column 2 which is dependent on column 4.
 * For eg, Rate Class Code 5 EB on LA(Rate Class Type E) depends on Y(LA having EB & VA).
 */
    private int rateClassCode; 

/**
 * Represents the Rate Type Code of  Column 2 Rate Class Type which is dependent on 
 * column 4 Rate Class Type. For eg, EB on LA Rate Type Code 3(Rate Class Type E) 
 * depends on Y(LA having EB & VA).
 */
    private int rateTypeCode; 
    
    //holds account type
    private String acType = null;
    
    //holds update user id
    private String updateUser = null;
    
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp = null;
    
    //holds rate class description
    private String rateClassDescription;
    
    //holds rate Type description
    private String rateTypeDescription;    

    /** Getter for property calcTypeID.
     * @return Value of property calcTypeID.
     */
    public String getCalcTypeID() {
        return calcTypeID;
    }    

    /** Setter for property calcTypeID.
     * @param calcTypeID New value of property calcTypeID.
     */
    public void setCalcTypeID(String calcTypeID) {
        this.calcTypeID = calcTypeID;
    }
    
    /** Getter for property rateClassType.
     * @return Value of property rateClassType.
     */
    public String getRateClassType() {
        return rateClassType;
    }
    
    /** Setter for property rateClassType.
     * @param rateClassType New value of property rateClassType.
     */
    public void setRateClassType(String rateClassType) {
        this.rateClassType = rateClassType;
    }
    
    /** Getter for property dependentSeqNumber.
     * @return Value of property dependentSeqNumber.
     */
    public int getDependentSeqNumber() {
        return dependentSeqNumber;
    }
    
    /** Setter for property dependentSeqNumber.
     * @param dependentSeqNumber New value of property dependentSeqNumber.
     */
    public void setDependentSeqNumber(int dependentSeqNumber) {
        this.dependentSeqNumber = dependentSeqNumber;
    }
    
    /** Getter for property dependentRateClassType.
     * @return Value of property dependentRateClassType.
     */
    public String getDependentRateClassType() {
        return dependentRateClassType;
    }
    
    /** Setter for property dependentRateClassType.
     * @param dependentRateClassType New value of property dependentRateClassType.
     */
    public void setDependentRateClassType(String dependentRateClassType) {
        this.dependentRateClassType = dependentRateClassType;
    }
    
    /** Getter for property rateClassCode.
     * @return Value of property rateClassCode.
     */
    public int getRateClassCode() {
        return rateClassCode;
    }
    
    /** Setter for property rateClassCode.
     * @param rateClassCode New value of property rateClassCode.
     */
    public void setRateClassCode(int rateClassCode) {
        this.rateClassCode = rateClassCode;
    }
    
    /** Getter for property rateTypeCode.
     * @return Value of property rateTypeCode.
     */
    public int getRateTypeCode() {
        return rateTypeCode;
    }
    
    /** Setter for property rateTypeCode.
     * @param rateTypeCode New value of property rateTypeCode.
     */
    public void setRateTypeCode(int rateTypeCode) {
        this.rateTypeCode = rateTypeCode;
    }
    
    /** Getter for property acType.
     * @return Value of property acType.
     */
    public java.lang.String getAcType() {
        return acType;
    }
    
    /** Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
    }
    
    /** Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /** Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }
    
    /** Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /** Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    } 
    
    public boolean equals(CoeusBean coeusBean) throws CoeusException {
        return isLike(coeusBean);
     }
    
    public boolean isLike(ComparableBean comparableBean) throws CoeusException{
        boolean success = false;
        if(comparableBean instanceof ValidCalcTypesBean){
            ValidCalcTypesBean validCalcTypesBean = (ValidCalcTypesBean)comparableBean;
            //Valid Calc Type ID
            if(validCalcTypesBean.getCalcTypeID()!=null){
                if(getCalcTypeID().equals(validCalcTypesBean.getCalcTypeID())){
                    success = true;
                }else{
                    return false;
                }
            }
            //Rate Class Type
            if(validCalcTypesBean.getRateClassType()!= null){
                if(getRateClassType() == validCalcTypesBean.getRateClassType()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Dependent Seq No.
            if(validCalcTypesBean.getDependentSeqNumber()!=-1){
                if(getDependentSeqNumber() == validCalcTypesBean.getDependentSeqNumber()){
                    success = true;
                }else{
                    return false;
                }
            }           
        }else{
            throw new CoeusException("budget_exception.1000");
        }
        return success;
    }   
    
    /** Getter for property rateClassDescription.
     * @return Value of property rateClassDescription.
     */
    public java.lang.String getRateClassDescription() {
        return rateClassDescription;
    }
    
    /** Setter for property rateClassDescription.
     * @param rateClassDescription New value of property rateClassDescription.
     */
    public void setRateClassDescription(java.lang.String rateClassDescription) {
        this.rateClassDescription = rateClassDescription;
    }
    
    /** Getter for property rateTypeDescription.
     * @return Value of property rateTypeDescription.
     */
    public java.lang.String getRateTypeDescription() {
        return rateTypeDescription;
    }
    
    /** Setter for property rateTypeDescription.
     * @param rateTypeDescription New value of property rateTypeDescription.
     */
    public void setRateTypeDescription(java.lang.String rateTypeDescription) {
        this.rateTypeDescription = rateTypeDescription;
    }
    
 } // end ValidCalcTypesBean



