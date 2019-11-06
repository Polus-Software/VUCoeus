/*
 * @(#)FormulaMaker.java October 14, 2003, 12:58 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.budget.calculator;

import edu.mit.coeus.budget.calculator.bean.ValidCalcTypesBean;
import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.query.Equals;
import java.util.*;

/**
 * Holds the Calculation rules and order of calculation.
 *
 * @author  Sagin
 * @version 1.0
 * Created on October 14, 2003, 12:58 PM
 *
 */
public class FormulaMaker {

  ///////////////////////////////////////
  // attributes


/**
 * Represents valid Calculation rate class types which needs to be included in 
 * budget calculation.
 */
    private CoeusVector cvValidCalcTypes; 
    
    //Holds EB on LA Rate Class Code
    private int EBonLARateClassCode = 0;
    
    //Holds EB on LA Rate Type Code
    private int EBonLARateTypeCode = 0;
    
    //Holds VA on LA Rate Class Code
    private int VAonLARateClassCode = 0;
    
    //Holds VA on LA Rate Type Code
    private int VAonLARateTypeCode = 0;



  ///////////////////////////////////////
  // operations


/**
 * Constructor....
 */
    public  FormulaMaker() throws CoeusException { 
        try {
            initCalcTypes();
            sortCalcTypes();
            setEBAndVAonLAValues();
        } catch (DBException dbEx) {
            throw new CoeusException(dbEx.getMessage());
        }
    } // end FormulaMaker         

/**
 * Initializes the calculation order of different rates in 
 * line item cal amts.
 * 
 */
    public void initCalcTypes() throws DBException, CoeusException {
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        cvValidCalcTypes = budgetDataTxnBean.getValidCalcTypes(
                                CoeusConstants.CALCULATION_RULE_TYPE_ID);
    } // end initCalcTypes       

/**
 * Does the sorting of Rate Class Types based on dependencies. ie, all the 
 * Rate Class Types which is not dependent on any other comes first, followed by
 * all other rate class types.
 */
    public void sortCalcTypes() {        
        if (cvValidCalcTypes != null && cvValidCalcTypes.size() > 0) {
            int vectorSize = cvValidCalcTypes.size();
            CoeusVector cvTemp = new CoeusVector();
            ValidCalcTypesBean validCalcTypesBean = new ValidCalcTypesBean();
            String depRateClassType = "";
            String rateClassType = "";
            Equals equals;
            CoeusVector filteredVector;
            for (int index = 0; index < vectorSize; index++) {
                validCalcTypesBean = (ValidCalcTypesBean) cvValidCalcTypes.get(index);
                
                rateClassType = validCalcTypesBean.getRateClassType();
                equals = new Equals("rateClassType", rateClassType);
                
                //check whether this rate class type already exists in cvTemp, if exists, skip
                if (cvTemp != null && cvTemp.size() > 0) {
                    filteredVector = cvTemp.filter(equals);
                    if (filteredVector != null && filteredVector.size() > 0) {
                        continue;
                    }
                }
                
                //if dependent seq no. is 0 indicates no dependency, just add it
                if (validCalcTypesBean.getDependentSeqNumber() == 0) {
                    cvTemp.add(validCalcTypesBean);
                    
                //dependency exists for this rate class type
                } else {
                    depRateClassType = validCalcTypesBean.getDependentRateClassType();
                    if ( depRateClassType != null) {
                        /*Get all other dependent rate class types and then loop & add all 
                         * before adding this rate class type
                         */
                        CoeusVector matchedRateClass = cvValidCalcTypes.filter(equals);
                        if (matchedRateClass != null && matchedRateClass.size() > 0) {
                            int filterSize = matchedRateClass.size();
                            for (int filterIndex = 0; filterIndex < filterSize; filterIndex++) {
                                ValidCalcTypesBean calcTypesBean = (ValidCalcTypesBean) matchedRateClass.get(filterIndex);
                                depRateClassType = calcTypesBean.getDependentRateClassType();
                                /*check whether the dependent rate class type already present
                                 *if not search & add it.
                                 */
                                equals = new Equals("rateClassType", depRateClassType);
                                filteredVector = cvTemp.filter(equals);

                                if (filteredVector != null && filteredVector.size() > 0) {
                                    continue;
                                } else {
                                    filteredVector = cvValidCalcTypes.filter(equals);
                                    if (filteredVector != null && filteredVector.size() > 0) {
                                        cvTemp.add((ValidCalcTypesBean) filteredVector.get(0));
                                    }
                                }
                            }
                            cvTemp.addAll(matchedRateClass);
                        }
                    }  
                }
            }
            cvValidCalcTypes = cvTemp;
        }
    }
    
    
    /** 
     *Sets the EBonLA and VAonLA values.
     */
    private void setEBAndVAonLAValues() {
        
        CoeusVector cvTempCalcTypes;
        Equals eqRCType;
        ValidCalcTypesBean validCalcTypesBean;
        
        
            
        // get the EB on LA RateClassCode & RateTypeCode if any
        eqRCType = new Equals("rateClassType", RateClassTypeConstants.EMPLOYEE_BENEFITS);
        cvTempCalcTypes = cvValidCalcTypes.filter(eqRCType);
        if (cvTempCalcTypes.size() > 0) {
            validCalcTypesBean = (ValidCalcTypesBean) cvTempCalcTypes.get(0);
            EBonLARateClassCode = validCalcTypesBean.getRateClassCode();
            EBonLARateTypeCode = validCalcTypesBean.getRateTypeCode();
        }

        // get the VA on LA RateClassCode & RateTypeCode if any
        eqRCType = new Equals("rateClassType", RateClassTypeConstants.VACATION);
        cvTempCalcTypes = cvValidCalcTypes.filter(eqRCType);
        if (cvTempCalcTypes.size() > 0) {
            validCalcTypesBean = (ValidCalcTypesBean) cvTempCalcTypes.get(0);
            VAonLARateClassCode = validCalcTypesBean.getRateClassCode();
            VAonLARateTypeCode = validCalcTypesBean.getRateTypeCode();
        }
    }
    
    /** Getter for property cvValidCalcTypes.
     * @return Value of property cvValidCalcTypes.
     */
    public CoeusVector getCvValidCalcTypes() {
        return cvValidCalcTypes;
    }
    
    /** Setter for property cvValidCalcTypes.
     * @param cvValidCalcTypes New value of property cvValidCalcTypes.
     */
    public void setCvValidCalcTypes(CoeusVector cvValidCalcTypes) {
        this.cvValidCalcTypes = cvValidCalcTypes;
    }    
     
    /** Getter for property EBonLARateClassCode.
     * @return Value of property EBonLARateClassCode.
     *
     */
    public int getEBonLARateClassCode() {
        return EBonLARateClassCode;
    }
    
    /** Setter for property EBonLARateClassCode.
     * @param EBonLARateClassCode New value of property EBonLARateClassCode.
     *
     */
    public void setEBonLARateClassCode(int EBonLARateClassCode) {
        this.EBonLARateClassCode = EBonLARateClassCode;
    }
    
    /** Getter for property EBonLARateTypeCode.
     * @return Value of property EBonLARateTypeCode.
     *
     */
    public int getEBonLARateTypeCode() {
        return EBonLARateTypeCode;
    }
    
    /** Setter for property EBonLARateTypeCode.
     * @param EBonLARateTypeCode New value of property EBonLARateTypeCode.
     *
     */
    public void setEBonLARateTypeCode(int EBonLARateTypeCode) {
        this.EBonLARateTypeCode = EBonLARateTypeCode;
    }
    
    /** Getter for property VAonLARateClassCode.
     * @return Value of property VAonLARateClassCode.
     *
     */
    public int getVAonLARateClassCode() {
        return VAonLARateClassCode;
    }
    
    /** Setter for property VAonLARateClassCode.
     * @param VAonLARateClassCode New value of property VAonLARateClassCode.
     *
     */
    public void setVAonLARateClassCode(int VAonLARateClassCode) {
        this.VAonLARateClassCode = VAonLARateClassCode;
    }
    
    /** Getter for property VAonLARateTypeCode.
     * @return Value of property VAonLARateTypeCode.
     *
     */
    public int getVAonLARateTypeCode() {
        return VAonLARateTypeCode;
    }
    
    /** Setter for property VAonLARateTypeCode.
     * @param VAonLARateTypeCode New value of property VAonLARateTypeCode.
     *
     */
    public void setVAonLARateTypeCode(int VAonLARateTypeCode) {
        this.VAonLARateTypeCode = VAonLARateTypeCode;
    }
    
    /*public static void main(String s[]){
        try {
            //throw new IllegalArgumentException("Thrown purposefully");
            FormulaMaker formulaMaker = new FormulaMaker();
            
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }*/    

 } // end FormulaMaker



