/*
 * @(#)LineItemEdiCalculator.java January 23, 2004, 3:44 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/* LineItemEDIConverter.java
 *
 * Created on January 27, 2004, 11:09 AM
 */

package edu.mit.coeus.budget.edi;

/**
 *
 * @author  sharathk
 */

import java.util.*;

import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.budget.edi.bean.*;
import edu.mit.coeus.budget.calculator.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.budget.calculator.bean.AmountBean;

public class LineItemEDIConverter {
    
    private String key;
    private QueryEngine queryEngine;
    private static final char PERSONNEL = 'P';
    CoeusVector vecEdiDetails; 
    private int period;

    /**
     * Represents Vector containing rates not available messages
     */
    private Vector vecMessages; 
    
    /** Creates a new instance of LineItemEDIConverter */
    public LineItemEDIConverter() {
        queryEngine = QueryEngine.getInstance();  
        vecMessages = new Vector();
    }
    
    public void calculateEDI() {
        copyEDIDetails();
        updateDataCollection();
        
    }
    
    private void copyEDIDetails() {
        Character personnel = new Character(PERSONNEL);
        NotEquals nePersonnel = new NotEquals("categoryType", personnel);
        Equals eqNull = new Equals("categoryType", null);
        
        Or nePersonnelOreqNull = new Or(nePersonnel, eqNull);
        LineItemEdiCalculator lineItemEdiCalculator = new LineItemEdiCalculator();
        
        final String LA = "L", SALARY = "Y";
        
        try{
            CoeusVector vecNP = queryEngine.executeQuery(key, BudgetDetailBean.class, nePersonnelOreqNull);
            /*
             *Added by Geo
             */
//            CoeusVector tempVecNP = queryEngine.executeQuery(key, BudgetDetailBean.class, nePersonnelOreqNull);
//             Equals eqBudgetPer = new Equals("budgetPeriod", new Integer(getPeriod()));
//             CoeusVector vecNP = tempVecNP.filter(eqBudgetPer);
            /*
             *
             */            
            int size = 0, calAmtsSize;
            if(vecNP != null) {
                size = vecNP.size();
            }
            
            BudgetDetailBean budgetDetailBean;
            BudgetDetailCalAmountsBean budgetDetailCalAmountsBean;
            BudgetPersonnelCalAmountsEdiBean budgetPersonnelCalAmountsEdiBean;
            BudgetRateBaseEdiBean budgetRateBaseEdiBean;
            BreakUpInterval breakUpInterval;
            BudgetPersonnelDetailsEdiBean budgetPersonnelDetailsEdiBean;
            
            ProposalRatesBean proposalRatesBean;
            ProposalLARatesBean proposalLARatesBean;
            
            AmountBean amountBean;
            
//            Equals eqPropNo, eqVersion, eqPeriod, eqLINumber;
            And propNoAndVersion, periodAndLINumber, propAndVrsnAndPrdAndLINum;
            CoeusVector vecCalAmts, vecBreakUpInterval, vecPersonnel, vecAmountBean;
            vecEdiDetails = new CoeusVector();
            
            double baseCost, baseCostSharing, appliedRate = 0, calculatedCost, calculatedCostSharing;
            int rateNumber = 0;
            Date startDate, endDate;
            
            for(int index = 0; index < size; index++) {
                budgetDetailBean = (BudgetDetailBean)vecNP.get(index);
                /*
                 *Commented by Geo on 03-May-2005
                 * Not being used anywhere
                 */
                //BEGIN COMMENT BLOCK
//                eqPropNo = new Equals("proposalNumber", budgetDetailBean.getProposalNumber());
//                eqVersion = new Equals("versionNumber", new Integer(budgetDetailBean.getVersionNumber()));
//                eqPeriod = new Equals("budgetPeriod", new Integer(budgetDetailBean.getBudgetPeriod()));
//                eqLINumber = new Equals("lineItemNumber", new Integer(budgetDetailBean.getLineItemNumber()));
//                
//                propNoAndVersion = new And(eqPropNo, eqVersion);
//                periodAndLINumber = new And(eqPeriod, eqLINumber);
//                
//                propAndVrsnAndPrdAndLINum = new And(propNoAndVersion, periodAndLINumber);
                //END COMMENT BLOCK
                lineItemEdiCalculator.calculate(budgetDetailBean);
                vecBreakUpInterval = lineItemEdiCalculator.getCvLIBreakupIntervals();
                
                
                
                int rateChangeSize = 0;
                if(vecBreakUpInterval != null) {
                    rateChangeSize= vecBreakUpInterval.size();
                }
                
                rateNumber = 0;
                for(int rateChangeIndex = 0; rateChangeIndex < rateChangeSize; rateChangeIndex++) {
                    breakUpInterval = (BreakUpInterval)vecBreakUpInterval.get(rateChangeIndex);
                    
                    vecAmountBean = breakUpInterval.getCvAmountDetails();
                    
                    int amountBeanSize = 0;
                    if(vecAmountBean != null) {
                        amountBeanSize = vecAmountBean.size();
                    }
                    
                    for(int amountIndex = 0; amountIndex < amountBeanSize; amountIndex++) {
                        
                        amountBean = (AmountBean)vecAmountBean.get(amountIndex);
                        
                        budgetRateBaseEdiBean = new BudgetRateBaseEdiBean();
                        budgetRateBaseEdiBean.setAcType(TypeConstants.INSERT_RECORD);
                      
                        appliedRate = amountBean.getAppliedRate();
                     
                        budgetRateBaseEdiBean.setAppliedRate(appliedRate);
                        
                        calculatedCost = amountBean.getCalculatedCost();
                        calculatedCostSharing = amountBean.getCalculatedCostSharing();
                        
                        //baseCost = calculatedCost * 100 / appliedRate;
                        //baseCostSharing = calculatedCostSharing * 100 / appliedRate;
                       
                        budgetRateBaseEdiBean.setBaseCost(breakUpInterval.getApplicableAmt());
                        budgetRateBaseEdiBean.setBaseCostSharing(breakUpInterval.getApplicableAmtCostSharing());
                        
                        budgetRateBaseEdiBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
                        budgetRateBaseEdiBean.setCalculatedCost(calculatedCost);
                        budgetRateBaseEdiBean.setCalculatedCostSharing(calculatedCostSharing);
                        
                        endDate = breakUpInterval.getBoundary().getEndDate();
                        budgetRateBaseEdiBean.setEndDate(new java.sql.Date(endDate.getTime()));
                        
                        budgetRateBaseEdiBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
                        budgetRateBaseEdiBean.setOnOffCampusFlag(budgetDetailBean.isOnOffCampusFlag());
                        budgetRateBaseEdiBean.setProposalNumber(budgetDetailBean.getProposalNumber());
                        budgetRateBaseEdiBean.setRateClassCode(amountBean.getRateClassCode());
                        budgetRateBaseEdiBean.setRateNumber(++rateNumber);
                        budgetRateBaseEdiBean.setRateTypeCode(amountBean.getRateTypeCode());
                        
                        startDate = breakUpInterval.getBoundary().getStartDate();
                        budgetRateBaseEdiBean.setStartDate(new java.sql.Date(startDate.getTime()));
                        
                        budgetRateBaseEdiBean.setVersionNumber(budgetDetailBean.getVersionNumber());
                        
                        vecEdiDetails.add(budgetRateBaseEdiBean);
                        
                    }//End for Amount Bean
                    
                }//End FOR Rate Change / Break Up Interval
                
                //                }//End For Cal Amts
                
            }//End For Budget Detail Beans
                
            //Get the list of rates not available messages
            vecMessages.addAll(lineItemEdiCalculator.getVecMessages());
            
            /*
            //Get Personnel Line Items from EDI_Details
            Equals eqPersonId = new Equals("personId", "999999999");
            Equals eqJobCode = new Equals("jobCode", "LA");
            And personIdAndJobCode = new And(eqPersonId, eqJobCode);
            vecPersonnel = queryEngine.executeQuery(key, BudgetPersonnelDetailsEdiBean.class, personIdAndJobCode);
            int perSize = 0;
            if(vecPersonnel != null){
                perSize= vecPersonnel.size();
            }
            CoeusVector vecCheck;
            for(int perIndex = 0; perIndex < perSize; perIndex++) {
                budgetPersonnelDetailsEdiBean = (BudgetPersonnelDetailsEdiBean)vecPersonnel.get(perIndex);
                Equals eqLINum = new Equals("lineItemNumber", new Integer(budgetPersonnelDetailsEdiBean.getLineItemNumber()));
                vecCheck = vecNP.filter(eqLINum);
                //Check if this is Non Personnel. if non personnel continue with next Element
                if(vecCheck != null && vecCheck.size() > 0) {
                    continue;
                }
                eqPropNo = new Equals("proposalNumber", budgetPersonnelDetailsEdiBean.getProposalNumber());
                eqVersion = new Equals("versionNumber", new Integer(budgetPersonnelDetailsEdiBean.getVersionNumber()));
                eqPeriod = new Equals("budgetPeriod", new Integer(budgetPersonnelDetailsEdiBean.getBudgetPeriod()));
                eqLINumber = new Equals("lineItemNumber", new Integer(budgetPersonnelDetailsEdiBean.getLineItemNumber()));
                Equals eqRateClass = new Equals("rateClassType", SALARY);
                
                propNoAndVersion = new And(eqPropNo, eqVersion);
                periodAndLINumber = new And(eqPeriod, eqLINumber);
                
                propAndVrsnAndPrdAndLINum = new And(propNoAndVersion, periodAndLINumber);
                And propAndVrsnAndPrdAndLINumAndRateClass = new And(propAndVrsnAndPrdAndLINum, eqRateClass);
                vecCalAmts = queryEngine.executeQuery(key, BudgetPersonnelCalAmountsEdiBean.class, propAndVrsnAndPrdAndLINumAndRateClass);
                if(vecCalAmts != null) {
                    calAmtsSize = vecCalAmts.size();
                }else {
                    calAmtsSize = 0;
                }
                //For Every Cal Amount insert a record in details EDI
                rateNumber = 0; //Set rate back to 0 for new entrant.
                for(int calAmtsIndex = 0; calAmtsIndex < calAmtsSize; calAmtsIndex++) {
                    budgetPersonnelCalAmountsEdiBean = (BudgetPersonnelCalAmountsEdiBean)vecCalAmts.get(calAmtsIndex);
                    
                    budgetRateBaseEdiBean = new BudgetRateBaseEdiBean();
                    
                    budgetRateBaseEdiBean.setAcType(TypeConstants.INSERT_RECORD);
                    
                    appliedRate = budgetPersonnelCalAmountsEdiBean.getAppliedRate();
                    calculatedCost = budgetPersonnelDetailsEdiBean.getSalaryRequested();
                    calculatedCostSharing = budgetPersonnelDetailsEdiBean.getCostSharingAmount();
                    baseCost = (calculatedCost * 100) / appliedRate;
                    baseCostSharing = (calculatedCostSharing * 100) / appliedRate;
                    
                    budgetRateBaseEdiBean.setAppliedRate(appliedRate);
                    budgetRateBaseEdiBean.setBaseCost(baseCost);
                    budgetRateBaseEdiBean.setBaseCostSharing(baseCostSharing);
                    budgetRateBaseEdiBean.setBudgetPeriod(budgetPersonnelDetailsEdiBean.getBudgetPeriod());
                    budgetRateBaseEdiBean.setCalculatedCost(calculatedCost);
                    budgetRateBaseEdiBean.setCalculatedCostSharing(calculatedCostSharing);
                    budgetRateBaseEdiBean.setEndDate(budgetPersonnelDetailsEdiBean.getEndDate());
                    budgetRateBaseEdiBean.setLineItemNumber(budgetPersonnelDetailsEdiBean.getLineItemNumber());
                    budgetRateBaseEdiBean.setOnOffCampusFlag(budgetPersonnelDetailsEdiBean.isOnOffCampusFlag());
                    budgetRateBaseEdiBean.setProposalNumber(budgetPersonnelDetailsEdiBean.getProposalNumber());
                    budgetRateBaseEdiBean.setRateClassCode(budgetPersonnelCalAmountsEdiBean.getRateClassCode());
                    budgetRateBaseEdiBean.setRateNumber(++rateNumber);
                    budgetRateBaseEdiBean.setRateTypeCode(budgetPersonnelCalAmountsEdiBean.getRateTypeCode());
                    budgetRateBaseEdiBean.setStartDate(budgetPersonnelDetailsEdiBean.getStartDate());
                    //budgetRateBaseEdiBean.setUpdateTimestamp(
                    budgetRateBaseEdiBean.setVersionNumber(budgetPersonnelDetailsEdiBean.getVersionNumber());
                    
                    vecEdiDetails.add(budgetRateBaseEdiBean);
                    
                }
                
            }//End For Personnel
            
             */
            
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }
    
    public void updateDataCollection() {
        if(vecEdiDetails == null || vecEdiDetails.size() == 0) return ;
        int size = vecEdiDetails.size();
        for(int index = 0; index < size; index++) {
            queryEngine.insert(key, (CoeusBean)vecEdiDetails.get(index));
        }
    }
    
    public Vector getEdiDetails() {
        return vecEdiDetails;
    }
    
    /** Getter for property key.
     * @return Value of property key.
     *
     */
    public String getKey() {
        return key;
    }
    
    /** Setter for property key.
     * @param key New value of property key.
     *
     */
    public void setKey(String key) {
        this.key = key;
    }
    
    /** Getter for property vecMessages.
     * @return Value of property vecMessages.
     *
     */
    public Vector getVecMessages() {
        return vecMessages;
    }
    
    /** Setter for property vecMessages.
     * @param vecMessages New value of property vecMessages.
     *
     */
    public void setVecMessages(Vector vecMessages) {
        this.vecMessages = vecMessages;
    }
    
    /**
     * Getter for property period.
     * @return Value of property period.
     */
    public int getPeriod() {
        return period;
    }
    
    /**
     * Setter for property period.
     * @param period New value of property period.
     */
    public void setPeriod(int period) {
        this.period = period;
    }
    
}
