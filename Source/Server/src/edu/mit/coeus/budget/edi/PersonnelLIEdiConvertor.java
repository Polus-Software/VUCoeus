/*
 * @(#)PersonnelLIEdiConvertor.java January 20, 2004, 4:42 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/*
 * PersonnelLIEdiConvertor.java
 *
 * Created on January 20, 2004, 4:42 PM
 */

package edu.mit.coeus.budget.edi;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.budget.calculator.*;
import edu.mit.coeus.budget.calculator.bean.AmountBean;
import edu.mit.coeus.budget.edi.bean.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.CoeusException;

import java.util.Vector;
/**
 * Used for EDI calculation for Personnel Line Items. Each personnel line item will be split  
 * into multiple rows according to the rate changes, and cost calculated for each row 
 * inserted.
 *
 * @author  Sagin
 * @version 1.0
 * Created on January 20, 2004, 4:42 PM
 *
 */
public class PersonnelLIEdiConvertor {

  ///////////////////////////////////////
  // attributes


    /**
     * Represents all the Budget Personnel Details For EDI
     */
    private CoeusVector cvPersonnelDetailsEdi; 
    
    /**
     * Represents all the Budget Personnel Cal Amounts For EDI
     */
    private CoeusVector cvPersonnelCalAmtsEdi;    
    
    /**
     * Represents all the Budget Rate Base For EDI
     */
    private CoeusVector cvRateBaseEdi;  

    /**
     * Represents key , Proposal Number + Version Number
     */
    private String key = "";

    /**
     * Represents the line item cost sharing applicable for a day
     */
    private QueryEngine queryEngine = QueryEngine.getInstance();

    /**
     * Represents the personnel line item edi calculator
     */
    private PersonnelLineItemEdiCalculator personnelLineItemEdiCalculator;

    /**
     * Represents the line item edi calculator
     */
    private LineItemEdiCalculator lineItemEdiCalculator;
    
    private static final String PERSON_ID = "999999999";
    
    private static final String LA_JOB_CODE = "LA";
    
    private static final String P_CATEGORY_JOB_CODE = "000000";
    private static char PERSONNEL_BUDGET_CATEGORY = 'P'; 

    /**
     * Represents Vector containing salary not available messages
     */
    private Vector vecMessages; 
    /**
     *holds the budget period
     */
    private int period;
    
    /** Creates a new instance of PersonnelLIEdiConvertor */
    public PersonnelLIEdiConvertor() {  
        vecMessages = new Vector();
    }
    
    /** Starting point for EDI Calculation. */
    public void calculateEDI() {
        try {
            
            //Initialize all the EDI vectors
            cvPersonnelDetailsEdi = new CoeusVector();
            cvPersonnelCalAmtsEdi = new CoeusVector();
            cvRateBaseEdi = new CoeusVector();
            
            personnelLineItemEdiCalculator = new PersonnelLineItemEdiCalculator();
            personnelLineItemEdiCalculator.setPeriod(getPeriod());
            lineItemEdiCalculator = new LineItemEdiCalculator();
            lineItemEdiCalculator.setPeriod(getPeriod());
            processPersonnelLIandCalAmts(); //Process personnel line items
            processPersonnelCategoryLIandCalAmts(); //Process personnel category line items not having personnel line items
            convertLASalariesToPerson(); //Convert all LA salaries to personnel line items
            processLASalaryLineItemAndCalAmts(); //Process LA Salaries line items
            updateDataCollection();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    /** 
     * Splits the Personnel Line items according to rate changes and for each 
     * record, a new BudgetPersonnelDetailsEdiBean and corresponding BudgetPersonnelCalAmountEdi Beans
     * are created.
     */
    private void splitPersonDetails() {
        try {
        processPersonnelLIandCalAmts();
        //copyPersonnelCategoryLIandCalAmts();
        //copyLASalaryLineItemAndCalAmts();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        
    }
    
    /**
     * Process & Copy all the Personnel details to cvPersonnelDetailsEdi - vector which holds
     * all BudgetPersonnelDetailsEdiBeans
     * Process & Copy all personnel cal amounts to cvPersonnelCalAmtsEdi  - vector which holds
     * all BudgetPersonnelCalAmountsEdiBeans
     */
    private void processPersonnelLIandCalAmts() throws Exception {
        CoeusVector cvPersonnelDetails;
        CoeusVector cvPersonnelCalAmts, cvTempPersonnelCalAmts;
        
        /*
         *Commenting by Geo to invoke the calculation only for a particular period
         * The EDI calculation is being fired from servlet for a period. So no need to 
         * loop thru all the person details all the time.
         *  Fetch the persons for a particular period and loop thru.
         *  Commented on 28-Apr-2005
         */
        /*
         *BEGIN COMMENT BLOCK
         */
//        //Get the personnel details & cal amounts
        cvPersonnelDetails = queryEngine.getDetails(key, BudgetPersonnelDetailsBean.class);
        cvPersonnelCalAmts = queryEngine.getDetails(key, BudgetPersonnelCalAmountsBean.class);
        /*
         *END COMMENT BLOCK
         */
        /*
         *BEGIN CODE
         */
////        CoeusVector personnelDetails = queryEngine.getDetails(key, BudgetPersonnelDetailsBean.class);
////        CoeusVector personnelCalAmts = queryEngine.getDetails(key, BudgetPersonnelCalAmountsBean.class);
//        Equals eqBudgetPer = new Equals("budgetPeriod", new Integer(getPeriod()));         
////        cvPersonnelDetails = personnelDetails.filter(eqBudgetPer);
////        cvPersonnelCalAmts = personnelCalAmts.filter(eqBudgetPer);
//        cvPersonnelDetails = queryEngine.executeQuery(key, BudgetPersonnelDetailsBean.class,eqBudgetPer);
//        cvPersonnelCalAmts = queryEngine.executeQuery(key, BudgetPersonnelCalAmountsBean.class,eqBudgetPer);
        /*
         *END CODE
         */
        //Copy the personnel details to edi
        if (cvPersonnelDetails == null || cvPersonnelDetails.size() == 0) {
            return;
        }
        
        CoeusVector cvTempPersonnelDetailsEdi = new CoeusVector();  //Holds Personnel edi beans
        CoeusVector cvTempPersonnelCalAmtsEdi = new CoeusVector();  //Holds personnel cal amt edi beans
        CoeusVector cvAmountDetails;
        CoeusVector cvLIBreakupIntervals; //Initialize breakup intervals vector 
        int size = cvPersonnelDetails.size();
        BudgetPersonnelDetailsEdiBean personnelDetailsEdiBean, tempPersonnelDetailsEdiBean;
        BudgetPersonnelDetailsBean personnelDetailsBean;
        BudgetPersonnelCalAmountsBean personnelCalAmountsBean;
        BudgetPersonnelCalAmountsEdiBean personnelCalAmountsEdiBean;
        int breakupSize = 0; //breakup size for calculated amount breakup intervals
        int amtDetailSize = 0; //Amount bean vector size
        int rateNumber = 1;
        int rateBaseNumber = 0; //Used for setting Rate Number in Rate Base EDI
        BreakUpInterval breakupInterval;
        Equals eqPeriod; //Equals Budget Period
        Equals eqLi; //Equals Budget line item
        Equals eqPerson; //Equals Person Number
        And periodAndLi; //Checks equality for budget period and line item number
        And periodAndLiAndPerson; //Checks equality for budget period, line item & person no
        AmountBean amountBean; //Contain amount details for each rate class / rate type

        for (int index = 0; index < size; index++) {
            rateNumber = 1; //Initialize rateNumber
            rateBaseNumber = 1; //Initialize rateNumber for Rate Base EDI
            personnelDetailsBean = (BudgetPersonnelDetailsBean) 
                                        cvPersonnelDetails.get(index);
            personnelDetailsEdiBean = new BudgetPersonnelDetailsEdiBean(personnelDetailsBean);

            /**
             * Filter and get a personnel cal amt bean for this person which will be 
             * used for creating personnel edi cal amt beans
             */
            eqPeriod = new Equals("budgetPeriod", new Integer(personnelDetailsEdiBean.getBudgetPeriod()));
            eqLi = new Equals("lineItemNumber", new Integer(personnelDetailsEdiBean.getLineItemNumber()));
            eqPerson = new Equals("personNumber", new Integer(personnelDetailsEdiBean.getPersonNumber()));
            periodAndLi = new And(eqPeriod, eqLi);
            periodAndLiAndPerson = new And(eqPerson, periodAndLi);
            cvTempPersonnelCalAmts = cvPersonnelCalAmts.filter(periodAndLiAndPerson);
            if (cvTempPersonnelCalAmts != null && cvTempPersonnelCalAmts.size() > 0) {
                personnelCalAmountsBean = (BudgetPersonnelCalAmountsBean) 
                                                    cvTempPersonnelCalAmts.get(0);
            } else {
                personnelCalAmountsBean = null;
            }
            
            
            //Calculate the personnel line item for each rate change/breakup period
            personnelLineItemEdiCalculator.calculate(personnelDetailsEdiBean);

            /**
             *Get the calculated breakup periods. Loop thru and insert edi records
             *corresponding to each breakup interval
             */
            cvLIBreakupIntervals = personnelLineItemEdiCalculator.getCvLIBreakupIntervals();
            if (cvLIBreakupIntervals == null || cvLIBreakupIntervals.size() == 0) {
                continue;
            }
            breakupSize = cvLIBreakupIntervals.size();
            for (int breakupIndex = 0; breakupIndex < breakupSize; breakupIndex++) {
                //Get the brekup interval
                breakupInterval = (BreakUpInterval) cvLIBreakupIntervals.get(breakupIndex);
                
                //Copy and get a new personnel edi bean
                tempPersonnelDetailsEdiBean = (BudgetPersonnelDetailsEdiBean) ObjectCloner.deepCopy(personnelDetailsEdiBean);
                
                //Set the rateNumber, startDate and endDate
                tempPersonnelDetailsEdiBean.setRateNumber(rateNumber++);
                tempPersonnelDetailsEdiBean.setStartDate(new java.sql.Date(breakupInterval.getBoundary().getStartDate().getTime()));
                tempPersonnelDetailsEdiBean.setEndDate(new java.sql.Date(breakupInterval.getBoundary().getEndDate().getTime()));
                tempPersonnelDetailsEdiBean.setSalaryRequested(breakupInterval.getApplicableAmt());
                tempPersonnelDetailsEdiBean.setCostSharingAmount(breakupInterval.getApplicableAmtCostSharing());
                tempPersonnelDetailsEdiBean.setAcType(TypeConstants.INSERT_RECORD);
                cvTempPersonnelDetailsEdi.add(tempPersonnelDetailsEdiBean);
                
                //Copy the personnel cal amts to edi from the breakup interval
                cvAmountDetails = breakupInterval.getCvAmountDetails();
                if (cvAmountDetails == null || cvAmountDetails.size() == 0) {
                    continue;
                }
                amtDetailSize = cvAmountDetails.size();
                for (int amtDetailIndex = 0; amtDetailIndex < amtDetailSize; amtDetailIndex++) {
                    //tempPersonnelCalAmountsBean = ObjectCloner.deepCopy(personnelCalAmountsBean);
                    amountBean = (AmountBean) cvAmountDetails.get(amtDetailIndex);
                    personnelCalAmountsEdiBean = new BudgetPersonnelCalAmountsEdiBean(personnelCalAmountsBean);
                    
                    personnelCalAmountsEdiBean.setRateNumber(tempPersonnelDetailsEdiBean.getRateNumber());
                    personnelCalAmountsEdiBean.setRateClassType(amountBean.getRateClassType());
                    personnelCalAmountsEdiBean.setRateClassCode(amountBean.getRateClassCode());
                    personnelCalAmountsEdiBean.setRateTypeCode(amountBean.getRateTypeCode());
                    personnelCalAmountsEdiBean.setApplyRateFlag(amountBean.isApplyRateFlag());
                    personnelCalAmountsEdiBean.setAppliedRate(amountBean.getAppliedRate());
                    personnelCalAmountsEdiBean.setCalculatedCost(amountBean.getCalculatedCost());
                    personnelCalAmountsEdiBean.setCalculatedCostSharing(amountBean.getCalculatedCostSharing());
                    personnelCalAmountsEdiBean.setAcType(TypeConstants.INSERT_RECORD);
                    cvTempPersonnelCalAmtsEdi.add(personnelCalAmountsEdiBean);
                    
                    /**
                     * Check whether the Rate Class Type is 'Y'. If yes, then add a 
                     * record in Rate Base EDI
                     */
                    if (amountBean.getRateClassType().equals(RateClassTypeConstants.LA_WITH_EB_VA)) {
                        processRateBase(tempPersonnelDetailsEdiBean, 
                            personnelCalAmountsEdiBean, rateBaseNumber++);
                    }
                    
                }//For loop for Amount details end here 
            }   //For loop for Breakup intervals end here
        }//For loop for personnel line items end here
            
        //Add all the edi data to the main collection
        if (cvTempPersonnelDetailsEdi != null && cvTempPersonnelDetailsEdi.size() > 0) {
            cvPersonnelDetailsEdi.addAll(cvTempPersonnelDetailsEdi);
            if (cvTempPersonnelCalAmtsEdi != null && cvTempPersonnelCalAmtsEdi.size() > 0) {
                cvPersonnelCalAmtsEdi.addAll(cvTempPersonnelCalAmtsEdi);
            }
        }
                
        //Get the list of salary not available messages for persons
        vecMessages.addAll(personnelLineItemEdiCalculator.getVecErrMessages());
    }
    
    /**
     * Process & Copy all the Personnel details to cvPersonnelDetailsEdi - all the Line Items & 
     * cal amts from OSP$BUDGET_DETAILS, OSP$BUDGET_DETAILS_CAL_AMTS, of category ‘PERSONNEL’ 
     * (category type = ‘P’) and which doesn’t have any personnel line items. 
     * For each people row inserted, set person_number = 1, rate_number = 1, 
     * person_id = ‘999999999’, job_code = ‘000000’, period_type = ‘CC’, 
     * percent_charged = 100, percent_effort = 100.
     */
    private void processPersonnelCategoryLIandCalAmts() throws CoeusException {
        CoeusVector cvLIDetails;
        CoeusVector cvPersonnelDetails;
        CoeusVector cvLICalAmts;
        Equals eqBudgetCategory = new Equals("categoryType", new Character(PERSONNEL_BUDGET_CATEGORY));
        
        //Get the Line Item details for Personnel Category
        cvLIDetails = queryEngine.executeQuery(key, BudgetDetailBean.class, eqBudgetCategory);
        
        //Copy the LI details of Personnel category and not having any personnel LIs to edi
        if (cvLIDetails == null || cvLIDetails.size() == 0) {
            return;
        }
        
        //Get the personnel details & Cal Amounts
        cvPersonnelDetails = queryEngine.getDetails(key, BudgetPersonnelDetailsBean.class);
        cvLICalAmts = queryEngine.getDetails(key, BudgetDetailCalAmountsBean.class);
        
        CoeusVector cvTempPersonnelDetailsEdi = new CoeusVector();  //Holds Personnel edi beans
        CoeusVector cvTempPersonnelCalAmtsEdi = new CoeusVector();  //Holds personnel cal amt edi beans
        int budgetDetailSize = cvLIDetails.size();
        int breakupSize = 0; //breakup size for calculated amount breakup intervals
        int rateNumber = 1;
        int rateBaseNumber = 0; //Used for setting Rate Number in Rate Base EDI
        int amtDetailSize = 0; //Amount bean vector size
        BreakUpInterval breakupInterval;
        BudgetPersonnelDetailsEdiBean personnelDetailsEdiBean;
        BudgetPersonnelCalAmountsEdiBean personnelCalAmountsEdiBean;
        BudgetDetailBean budgetDetailBean;
        BudgetDetailCalAmountsBean detailCalAmountsBean;
        Equals eqPeriod;
        Equals eqLi;
        And periodAndLi;
        AmountBean amountBean; //Contain amount details for each rate class / rate type
        CoeusVector cvAmountDetails;
        CoeusVector cvLIBreakupIntervals; //Initialize breakup intervals vector 

        for (int index = 0; index < budgetDetailSize; index++) {
            rateNumber = 1; //Initialize rateNumber
            rateBaseNumber = 1; //Initialize rateNumber for Rate Base EDI
            budgetDetailBean = (BudgetDetailBean) 
                                        cvLIDetails.get(index);
            /**
             *Check whether any personnel line item exists for this LI. 
             *If exists skip and continue with the next.
             */
            eqPeriod = new Equals("budgetPeriod", new Integer(budgetDetailBean.getBudgetPeriod()));
            eqLi = new Equals("lineItemNumber", new Integer(budgetDetailBean.getLineItemNumber()));
            periodAndLi = new And(eqPeriod, eqLi);
            cvPersonnelDetails = queryEngine.executeQuery(key, BudgetPersonnelDetailsBean.class, periodAndLi);
            if (cvPersonnelDetails != null && cvPersonnelDetails.size() > 0) {
                continue;
            }
            
            //Calculate the line item for each rate change/breakup period
            lineItemEdiCalculator.calculate(budgetDetailBean);

            /**
             *Get the calculated breakup periods. Loop thru and insert edi records
             *corresponding to each breakup interval
             */
            cvLIBreakupIntervals = lineItemEdiCalculator.getCvLIBreakupIntervals();
            if (cvLIBreakupIntervals == null || cvLIBreakupIntervals.size() == 0) {
                continue;
            }
            breakupSize = cvLIBreakupIntervals.size();
            for (int breakupIndex = 0; breakupIndex < breakupSize; breakupIndex++) {
                //Get the brekup interval
                breakupInterval = (BreakUpInterval) cvLIBreakupIntervals.get(breakupIndex);
                
                //Create a new personnel edi bean
                personnelDetailsEdiBean = new BudgetPersonnelDetailsEdiBean();
               
                //Set the values in personnel edi bean
                personnelDetailsEdiBean.setProposalNumber(budgetDetailBean.getProposalNumber());
                personnelDetailsEdiBean.setVersionNumber(budgetDetailBean.getVersionNumber());
                personnelDetailsEdiBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
                personnelDetailsEdiBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
                personnelDetailsEdiBean.setPersonNumber(1);
                personnelDetailsEdiBean.setRateNumber(rateNumber++);
                personnelDetailsEdiBean.setPersonId(PERSON_ID);
                personnelDetailsEdiBean.setJobCode(P_CATEGORY_JOB_CODE);
                personnelDetailsEdiBean.setStartDate(new java.sql.Date(breakupInterval.getBoundary().getStartDate().getTime()));
                personnelDetailsEdiBean.setEndDate(new java.sql.Date(breakupInterval.getBoundary().getEndDate().getTime()));
                personnelDetailsEdiBean.setPeriodType("CC");
                personnelDetailsEdiBean.setLineItemDescription(budgetDetailBean.getLineItemDescription());
                personnelDetailsEdiBean.setSequenceNumber(0);
                personnelDetailsEdiBean.setSalaryRequested(breakupInterval.getApplicableAmt());
                personnelDetailsEdiBean.setPercentCharged(100.00);
                personnelDetailsEdiBean.setPercentEffort(100.00);
                personnelDetailsEdiBean.setCostSharingPercent(0.00);
                personnelDetailsEdiBean.setCostSharingAmount(breakupInterval.getApplicableAmtCostSharing());
                personnelDetailsEdiBean.setUnderRecoveryAmount(breakupInterval.getUnderRecovery());
                personnelDetailsEdiBean.setOnOffCampusFlag(budgetDetailBean.isOnOffCampusFlag());
                personnelDetailsEdiBean.setApplyInRateFlag(budgetDetailBean.isApplyInRateFlag());
                personnelDetailsEdiBean.setBudgetJustification(budgetDetailBean.getBudgetJustification());
                personnelDetailsEdiBean.setAcType(TypeConstants.INSERT_RECORD);
                cvTempPersonnelDetailsEdi.add(personnelDetailsEdiBean);
                
                //Create the personnel cal amts edi from the amount beans vector in breakup interval
                cvAmountDetails = breakupInterval.getCvAmountDetails();
                if (cvAmountDetails == null || cvAmountDetails.size() == 0) {
                    continue;
                }
                amtDetailSize = cvAmountDetails.size();
                for (int amtDetailIndex = 0; amtDetailIndex < amtDetailSize; amtDetailIndex++) {
                    //tempPersonnelCalAmountsBean = ObjectCloner.deepCopy(personnelCalAmountsBean);
                    amountBean = (AmountBean) cvAmountDetails.get(amtDetailIndex);
                    personnelCalAmountsEdiBean = new BudgetPersonnelCalAmountsEdiBean();
                    
                    personnelCalAmountsEdiBean.setProposalNumber(budgetDetailBean.getProposalNumber());
                    personnelCalAmountsEdiBean.setVersionNumber(budgetDetailBean.getVersionNumber());
                    personnelCalAmountsEdiBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
                    personnelCalAmountsEdiBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
                    personnelCalAmountsEdiBean.setPersonNumber(1);
                    personnelCalAmountsEdiBean.setRateNumber(personnelDetailsEdiBean.getRateNumber());
                    personnelCalAmountsEdiBean.setRateClassType(amountBean.getRateClassType());
                    personnelCalAmountsEdiBean.setRateClassCode(amountBean.getRateClassCode());
                    personnelCalAmountsEdiBean.setRateTypeCode(amountBean.getRateTypeCode());
                    personnelCalAmountsEdiBean.setApplyRateFlag(amountBean.isApplyRateFlag());
                    personnelCalAmountsEdiBean.setAppliedRate(amountBean.getAppliedRate());
                    personnelCalAmountsEdiBean.setCalculatedCost(amountBean.getCalculatedCost());
                    personnelCalAmountsEdiBean.setCalculatedCostSharing(amountBean.getCalculatedCostSharing());
                    personnelCalAmountsEdiBean.setAcType(TypeConstants.INSERT_RECORD);
                    cvTempPersonnelCalAmtsEdi.add(personnelCalAmountsEdiBean);
                    
                    /**
                     * Check whether the Rate Class Type is 'Y'. If yes, then add a 
                     * record in Rate Base EDI
                     */
                    if (amountBean.getRateClassType().equals(RateClassTypeConstants.LA_WITH_EB_VA)) {
                        processRateBase(personnelDetailsEdiBean, 
                            personnelCalAmountsEdiBean, rateBaseNumber++);
                    }
                    
                } //For loop for Amount details ends here 
                
            } //For loop for Breakup intervals ends here
            
        } //For loop for budget detail beans ends here
            
        //Add all the edi data to the main collection
        if (cvTempPersonnelDetailsEdi != null && cvTempPersonnelDetailsEdi.size() > 0) {
            cvPersonnelDetailsEdi.addAll(cvTempPersonnelDetailsEdi);
            if (cvTempPersonnelCalAmtsEdi != null && cvTempPersonnelCalAmtsEdi.size() > 0) {
                cvPersonnelCalAmtsEdi.addAll(cvTempPersonnelCalAmtsEdi);
            }
        }
                
        //Get the list of rates not available messages
        vecMessages.addAll(lineItemEdiCalculator.getVecMessages());
        
    }
    
    
    /**
     * Treat LA - Salaries for non-personnel items as people. Ie, proces all line items for 
     * which rate class type ‘Y’ rates are applicable. For this, instead of converting one LA 
     * row to a person row should check if there is a LA rate change for that period and if so 
     * should split this LA to smaller chunks and insert as many people rows as there are 
     * rate changes into OSP$BUDGET_PER_DETAILS_FOR_EDI table (BudgetPersonnelDetailsEdiBean).
     * For each people row inserted, set person_number = breakup number, rate_number = 1, 
     * person_id = ‘999999999’, job_code = ‘000000’, period_type = ‘CC’, percent_charged = 100, 
     * percent_effort = 100. Apply the rate on the line item cost for each period and set 
     * salary_requested. Apply the rate on the line item cost sharing for each period and set 
     * cost_sharing_amount. Underrecovery_amount, on_off_campus_flag, apply_in_rate_flag, and 
     * budget_justification should all be copied from the Line Item details.
     *
     * Copy EB on LA for the LA amount that was converted to person into CAL_AMT_EDI. Set the 
     * person_number = breakup number, the same used to set the new person row. Set the 
     * applied_rate, calculated_cost & calculated_cost_sharing to 0.
     *
     * Copy Vacation on LA for the LA amount that was converted to person into CAL_AMT_EDI. 
     * Set the person_number = breakup number, the same used to set the new person row. Set the 
     * applied_rate, calculated_cost & calculated_cost_sharing to 0.
     */
    private void processLASalaryLineItemAndCalAmts() throws CoeusException, Exception{
        CoeusVector cvLIDetails;
        CoeusVector cvPersonnelDetails;
        CoeusVector cvLICalAmts, cvOrigLICalAmts;
        
        CoeusVector cvTempPersonnelDetailsEdi = new CoeusVector();  //Holds Personnel edi beans
        CoeusVector cvTempPersonnelCalAmtsEdi = new CoeusVector();  //Holds personnel cal amt edi beans
        CoeusVector cvTempPersonnelDetailsLAEdi = new CoeusVector();  //Holds Personnel edi beans
        CoeusVector cvTempPersonnelCalAmtsLAEdi = new CoeusVector();  //Holds personnel cal amt edi beans
        int budgetDetailSize = 0;
        int breakupSize = 0; //breakup size for calculated amount breakup intervals
        int amtDetailSize = 0; //Amount bean vector size
        int eBVAOnLASize = 0;
        int rateNumber = 0;
        BreakUpInterval breakupInterval;
        BudgetPersonnelDetailsEdiBean personnelDetailsEdiBean, tempPersonnelDetailsEdiBean;
        BudgetPersonnelCalAmountsEdiBean personnelCalAmountsEdiBean;
        BudgetDetailBean budgetDetailBean;
        BudgetDetailCalAmountsBean detailCalAmountsBean;
        Equals eqPeriod, eqCategory;
        Equals eqLi, eqEbRC, eqEbRT, eqVaRC, eqVaRT;
        Equals eqRCType = new Equals("rateClassType", RateClassTypeConstants.LA_WITH_EB_VA);
        And periodAndLi, periodAndLiAndRCType, eqEbRCAndEqEbRT, eqVaRCAndEqVaRT;
        And liPeriodAndEbOrVaOnLA;
        Or ebOnLAOrVaonLa, categoryNeqPersonnel;
        NotEquals neqRCType = new NotEquals("rateClassType", RateClassTypeConstants.LA_WITH_EB_VA);;
        AmountBean amountBean; //Contain amount details for each rate class / rate type
        CoeusVector cvAmountDetails; //Holds the Amount Beans
        CoeusVector cvLIBreakupIntervals; //Initialize breakup intervals vector
        CoeusVector cvEBAndVAonLA; //Holds the EBonLA and VAonLA rates for an LA Salary
        FormulaMaker formulaMaker = lineItemEdiCalculator.getFormulaMaker();
        int EBonLARateClassCode = formulaMaker.getEBonLARateClassCode();
        int EBonLARateTypeCode = formulaMaker.getEBonLARateTypeCode();
        int VAonLARateClassCode = formulaMaker.getVAonLARateClassCode();
        int VAonLARateTypeCode = formulaMaker.getVAonLARateTypeCode();
        
        eqEbRC = new Equals("rateClassCode", new Integer(EBonLARateClassCode));
        eqEbRT = new Equals("rateTypeCode", new Integer(EBonLARateTypeCode));
        eqVaRC = new Equals("rateClassCode", new Integer(VAonLARateClassCode));
        eqVaRT = new Equals("rateTypeCode", new Integer(VAonLARateTypeCode));
        eqEbRCAndEqEbRT = new And(eqEbRC, eqEbRT);
        eqVaRCAndEqVaRT = new And(eqVaRC, eqVaRT);
        ebOnLAOrVaonLa = new Or(eqEbRCAndEqEbRT, eqVaRCAndEqVaRT);
        
        //Get the original Cal amounts. This is done to set it back after the LA Salary breakup process
        cvOrigLICalAmts = queryEngine.getDetails(key, BudgetDetailCalAmountsBean.class);
        
        //Filter and get all the Line Item details of budget category other than Personnel ('P')
        categoryNeqPersonnel = new Or(new Equals("categoryType", null), new NotEquals("categoryType",new Character(PERSONNEL_BUDGET_CATEGORY)));
        cvLIDetails = queryEngine.executeQuery(key, BudgetDetailBean.class, categoryNeqPersonnel);
        
        //Copy the LI details of Personnel category and not having any personnel LIs to edi
        if (cvLIDetails == null || cvLIDetails.size() == 0) {
            return;
        }
        
        //Filter and get all cal amounts of rate class type 'Y'
        budgetDetailSize = cvLIDetails.size();
        for (int index = 0; index < budgetDetailSize; index++) {
            budgetDetailBean = (BudgetDetailBean) 
                                        cvLIDetails.get(index);
            /**
             *Check whether any LA Salary rate is applicable for this line item 
             *If not exists then continue with the next line item
             */
            eqPeriod = new Equals("budgetPeriod", new Integer(budgetDetailBean.getBudgetPeriod()));
            eqLi = new Equals("lineItemNumber", new Integer(budgetDetailBean.getLineItemNumber()));
            periodAndLi = new And(eqPeriod, eqLi);
            periodAndLiAndRCType = new And(periodAndLi, eqRCType);
            cvLICalAmts = cvOrigLICalAmts.filter(periodAndLiAndRCType);
            if (cvLICalAmts == null || cvLICalAmts.size() == 0) {
                continue;
            }
            
            //Get the EBonLA & VAonLA rates
            liPeriodAndEbOrVaOnLA = new And(periodAndLi, ebOnLAOrVaonLa);
            cvEBAndVAonLA = cvOrigLICalAmts.filter(liPeriodAndEbOrVaOnLA);
            
            /**
             * Set the 'Y' cal amount in calculator since we need to get the breakup 
             * for 'Y' alone.
             */
            lineItemEdiCalculator.setCvLineItemCalcAmts(cvLICalAmts);
            lineItemEdiCalculator.setGetCalAmtFromQE(false);
            
            
            //Calculate the line item for each rate change/breakup period
            lineItemEdiCalculator.calculate(budgetDetailBean);

            /**
             *Get the calculated breakup periods. Loop thru and insert edi records
             *corresponding to each breakup interval
             */
            cvLIBreakupIntervals = lineItemEdiCalculator.getCvLIBreakupIntervals();
            if (cvLIBreakupIntervals == null || cvLIBreakupIntervals.size() == 0) {
                continue;
            }
            breakupSize = cvLIBreakupIntervals.size();
            for (int breakupIndex = 0; breakupIndex < breakupSize; breakupIndex++) {
                //Get the breakup interval
                breakupInterval = (BreakUpInterval) cvLIBreakupIntervals.get(breakupIndex);
                
                //Get the amount details
                cvAmountDetails = breakupInterval.getCvAmountDetails();
                if (cvAmountDetails == null || cvAmountDetails.size() == 0) {
                    continue;
                }
                
                amtDetailSize = cvAmountDetails.size();
                for (int amtDetailIndex = 0; amtDetailIndex < amtDetailSize; amtDetailIndex++) {
                    amountBean = (AmountBean) cvAmountDetails.get(amtDetailIndex);
                
                    //Create a new personnel edi bean
                    personnelDetailsEdiBean = new BudgetPersonnelDetailsEdiBean();

                    //Set the values in personnel edi bean
                    personnelDetailsEdiBean.setProposalNumber(budgetDetailBean.getProposalNumber());
                    personnelDetailsEdiBean.setVersionNumber(budgetDetailBean.getVersionNumber());
                    personnelDetailsEdiBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
                    personnelDetailsEdiBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
                    personnelDetailsEdiBean.setPersonNumber(breakupIndex + 1);
                    personnelDetailsEdiBean.setRateNumber(1);
                    personnelDetailsEdiBean.setPersonId(PERSON_ID);
                    personnelDetailsEdiBean.setJobCode(LA_JOB_CODE);
                    personnelDetailsEdiBean.setStartDate(new java.sql.Date(breakupInterval.getBoundary().getStartDate().getTime()));
                    personnelDetailsEdiBean.setEndDate(new java.sql.Date(breakupInterval.getBoundary().getEndDate().getTime()));
                    personnelDetailsEdiBean.setPeriodType("CC");
                    personnelDetailsEdiBean.setLineItemDescription(budgetDetailBean.getLineItemDescription());
                    personnelDetailsEdiBean.setSequenceNumber(0);
                    personnelDetailsEdiBean.setSalaryRequested(amountBean.getCalculatedCost());
                    personnelDetailsEdiBean.setPercentCharged(100.00);
                    personnelDetailsEdiBean.setPercentEffort(100.00);
                    personnelDetailsEdiBean.setCostSharingPercent(0.00);
                    personnelDetailsEdiBean.setCostSharingAmount(amountBean.getCalculatedCostSharing());
                    personnelDetailsEdiBean.setUnderRecoveryAmount(breakupInterval.getUnderRecovery());
                    personnelDetailsEdiBean.setOnOffCampusFlag(budgetDetailBean.isOnOffCampusFlag());
                    personnelDetailsEdiBean.setApplyInRateFlag(budgetDetailBean.isApplyInRateFlag());
                    personnelDetailsEdiBean.setBudgetJustification(budgetDetailBean.getBudgetJustification());
                    personnelDetailsEdiBean.setAcType(TypeConstants.INSERT_RECORD);
                    cvTempPersonnelDetailsLAEdi.add(personnelDetailsEdiBean);
                    
                } //For loop for Amount beans end here
            } //For loop for breakup intervals end here
/****************************************************************************************************************/
            /**
             * Create breakup intervals again for EBonLA & VAonLA rate changes for
             * each of the LA line item inserted above.
             */
            int laEdiSize = cvTempPersonnelDetailsLAEdi.size();
            for (int laEdiIndex = 0; laEdiIndex < laEdiSize; laEdiIndex++) {
                rateNumber = 0;
                personnelDetailsEdiBean = (BudgetPersonnelDetailsEdiBean) 
                                    cvTempPersonnelDetailsLAEdi.get(laEdiIndex);
                
                //set the start date, end date & line item cost from personnel edi bean
                budgetDetailBean.setLineItemStartDate(personnelDetailsEdiBean.getStartDate());
                budgetDetailBean.setLineItemEndDate(personnelDetailsEdiBean.getEndDate());
                budgetDetailBean.setLineItemCost(personnelDetailsEdiBean.getSalaryRequested());
                budgetDetailBean.setCostSharingAmount(personnelDetailsEdiBean.getCostSharingAmount());
                
                //set the EBonLA & VAonLA cal amounts to calculator
                lineItemEdiCalculator.setCvLineItemCalcAmts(cvEBAndVAonLA);
                lineItemEdiCalculator.setGetCalAmtFromQE(false);
                
                //Calculate the line item for each rate change/breakup period
                lineItemEdiCalculator.calculate(budgetDetailBean);

                /**
                 *Get the calculated breakup periods. Loop thru and insert edi records
                 *corresponding to each breakup interval
                 */
                cvLIBreakupIntervals = lineItemEdiCalculator.getCvLIBreakupIntervals();
                if (cvLIBreakupIntervals == null || cvLIBreakupIntervals.size() == 0) {
                    continue;
                }
                breakupSize = cvLIBreakupIntervals.size();
                for (int breakupIndex = 0; breakupIndex < breakupSize; breakupIndex++) {
                    //Get the breakup interval
                    breakupInterval = (BreakUpInterval) cvLIBreakupIntervals.get(breakupIndex);

                    //Get the amount details
                    cvAmountDetails = breakupInterval.getCvAmountDetails();
                    if (cvAmountDetails == null || cvAmountDetails.size() == 0) {
                        continue;
                    }

                    //Create a new personnel edi bean
                    tempPersonnelDetailsEdiBean = (BudgetPersonnelDetailsEdiBean) 
                                ObjectCloner.deepCopy(personnelDetailsEdiBean);

                    //Set the values in personnel edi bean

                    tempPersonnelDetailsEdiBean.setRateNumber(++rateNumber);
                    tempPersonnelDetailsEdiBean.setStartDate(new java.sql.Date(breakupInterval.getBoundary().getStartDate().getTime()));
                    tempPersonnelDetailsEdiBean.setEndDate(new java.sql.Date(breakupInterval.getBoundary().getEndDate().getTime()));
                    tempPersonnelDetailsEdiBean.setSalaryRequested(breakupInterval.getApplicableAmt());
                    tempPersonnelDetailsEdiBean.setCostSharingAmount(breakupInterval.getApplicableAmtCostSharing());
                    tempPersonnelDetailsEdiBean.setUnderRecoveryAmount(breakupInterval.getUnderRecovery());
                    tempPersonnelDetailsEdiBean.setAcType(TypeConstants.INSERT_RECORD);
                    cvTempPersonnelDetailsEdi.add(tempPersonnelDetailsEdiBean);
                    
                    //Get the EBonLA & VAonLA cal amts for this personnel edi
                    amtDetailSize = cvAmountDetails.size();
                    for (int amtDetailIndex = 0; amtDetailIndex < amtDetailSize; amtDetailIndex++) {
                        amountBean = (AmountBean) cvAmountDetails.get(amtDetailIndex);

                        //Create a new personnel cal amt bean for each EBonLA and VAonLA rates
                        personnelCalAmountsEdiBean = new BudgetPersonnelCalAmountsEdiBean();
                        personnelCalAmountsEdiBean.setProposalNumber(tempPersonnelDetailsEdiBean.getProposalNumber());
                        personnelCalAmountsEdiBean.setVersionNumber(tempPersonnelDetailsEdiBean.getVersionNumber());
                        personnelCalAmountsEdiBean.setBudgetPeriod(tempPersonnelDetailsEdiBean.getBudgetPeriod());
                        personnelCalAmountsEdiBean.setLineItemNumber(tempPersonnelDetailsEdiBean.getLineItemNumber());
                        personnelCalAmountsEdiBean.setPersonNumber(tempPersonnelDetailsEdiBean.getPersonNumber());
                        personnelCalAmountsEdiBean.setRateNumber(tempPersonnelDetailsEdiBean.getRateNumber());
                        personnelCalAmountsEdiBean.setRateClassType(amountBean.getRateClassType());
                        personnelCalAmountsEdiBean.setRateClassCode(amountBean.getRateClassCode());
                        personnelCalAmountsEdiBean.setRateTypeCode(amountBean.getRateTypeCode());
                        personnelCalAmountsEdiBean.setApplyRateFlag(amountBean.isApplyRateFlag());
                        personnelCalAmountsEdiBean.setAppliedRate(amountBean.getAppliedRate());
                        personnelCalAmountsEdiBean.setCalculatedCost(amountBean.getCalculatedCost());
                        personnelCalAmountsEdiBean.setCalculatedCostSharing(amountBean.getCalculatedCostSharing());
                        personnelCalAmountsEdiBean.setAcType(TypeConstants.INSERT_RECORD);
                        cvTempPersonnelCalAmtsEdi.add(personnelCalAmountsEdiBean);
                    } //For loop for Amount beans end here
                } //For loop for breakup intervals end here
                
            } //For loop for temp LA details end here
            
        } //For loop for budget detail beans end here
            
        //Add all the edi data to the main collection
        if (cvTempPersonnelDetailsEdi != null && cvTempPersonnelDetailsEdi.size() > 0) {
            cvPersonnelDetailsEdi.addAll(cvTempPersonnelDetailsEdi);
            if (cvTempPersonnelCalAmtsEdi != null && cvTempPersonnelCalAmtsEdi.size() > 0) {
                cvPersonnelCalAmtsEdi.addAll(cvTempPersonnelCalAmtsEdi);
            }
        }
    }
    
    /**
     * Adds a Rate Base EDI bean to the cvRateBaseEdi vector.
     * 
     * @param BudgetPersonnelDetailsEdiBean personnelDetailsEdiBean 
     * @param BudgetPersonnelCalAmountsEdiBean personnelCalAmountsEdiBean 
     * @param int rateNumber
     */
    private void processRateBase(BudgetPersonnelDetailsEdiBean personnelDetailsEdiBean, 
        BudgetPersonnelCalAmountsEdiBean personnelCalAmountsEdiBean, int rateNumber) {
            
        BudgetRateBaseEdiBean budgetRateBaseEdiBean = new BudgetRateBaseEdiBean();

        budgetRateBaseEdiBean.setAcType(TypeConstants.INSERT_RECORD);

        double appliedRate = personnelCalAmountsEdiBean.getAppliedRate();
        double calculatedCost = personnelCalAmountsEdiBean.getCalculatedCost();
        double calculatedCostSharing = personnelCalAmountsEdiBean.getCalculatedCostSharing();
        //double baseCost = (calculatedCost * 100) / appliedRate;
        //double baseCostSharing = (calculatedCostSharing * 100) / appliedRate;

        budgetRateBaseEdiBean.setProposalNumber(personnelDetailsEdiBean.getProposalNumber());
        budgetRateBaseEdiBean.setVersionNumber(personnelDetailsEdiBean.getVersionNumber());
        budgetRateBaseEdiBean.setBudgetPeriod(personnelDetailsEdiBean.getBudgetPeriod());
        budgetRateBaseEdiBean.setLineItemNumber(personnelDetailsEdiBean.getLineItemNumber());
        budgetRateBaseEdiBean.setRateNumber(rateNumber);
        budgetRateBaseEdiBean.setStartDate(personnelDetailsEdiBean.getStartDate());
        budgetRateBaseEdiBean.setEndDate(personnelDetailsEdiBean.getEndDate());
        budgetRateBaseEdiBean.setRateClassCode(personnelCalAmountsEdiBean.getRateClassCode());
        budgetRateBaseEdiBean.setRateTypeCode(personnelCalAmountsEdiBean.getRateTypeCode());
        budgetRateBaseEdiBean.setOnOffCampusFlag(personnelDetailsEdiBean.isOnOffCampusFlag());
        budgetRateBaseEdiBean.setAppliedRate(appliedRate);
        budgetRateBaseEdiBean.setBaseCost(personnelDetailsEdiBean.getSalaryRequested());
        budgetRateBaseEdiBean.setBaseCostSharing(personnelDetailsEdiBean.getCostSharingAmount());
        budgetRateBaseEdiBean.setCalculatedCost(calculatedCost);
        budgetRateBaseEdiBean.setCalculatedCostSharing(calculatedCostSharing);

        //Add to Rate Base EDI vector
        cvRateBaseEdi.add(budgetRateBaseEdiBean);
    }
    
    /**
     * Converts all LA Salary records in cal amt edi beans vector to Persons.
     * Take all LA Salary rows in CAL_AMT_EDI (Rate class type 'Y') and for 
     * every LA cal amt edi bean found, create a new personnel edi bean and delete 
     * the corresponding personnel cal amt bean. This way LA can be treated as a person 
     * and not as a calculated cost.
     */
    private void convertLASalariesToPerson() throws Exception {
        
        if (cvPersonnelCalAmtsEdi == null || cvPersonnelCalAmtsEdi.size() == 0) {
            return;
        }
        
        CoeusVector cvLACalAmts;
        CoeusVector cvTempPersonnelDetailsEdi = new CoeusVector();  //Holds Personnel edi beans
        int calAmtSize = 0;
        
        //Filter and get all the LA Salaries (Rate Class Type = 'Y')
        Equals eqLASalaries = new Equals("rateClassType", RateClassTypeConstants.LA_WITH_EB_VA);
        cvLACalAmts = cvPersonnelCalAmtsEdi.filter(eqLASalaries);
        
        if (cvPersonnelCalAmtsEdi == null || cvPersonnelCalAmtsEdi.size() == 0) {
            return;
        }
        
        calAmtSize =  cvLACalAmts.size();
        BudgetPersonnelDetailsEdiBean personnelDetailsLAEdiBean;
        BudgetPersonnelCalAmountsEdiBean personnelCalAmountsEdiBean;
        Equals eqPeriod, eqLI, eqPersonNo, eqRateNo, eqEbRC, eqEbRT, eqVaRC, eqVaRT;
        And eqPeriodAndEqLI, eqPersonNoAndEqRateNo, periodAndLIAndPersonNoAndRateNo;
        And eqEbRCAndEqEbRT, eqVaRCAndEqVaRT, filterEBonLAAndVAonLA;
        Or ebRCAndEbRTOrVaRCAndVaRT;
        CoeusVector cvPersonEdiLI, cvEBandVAEdiCalAmts;
        FormulaMaker formulaMaker = personnelLineItemEdiCalculator.getFormulaMaker();
        int newPersonNo = 0, size = 0;
        int EBonLARateClassCode = formulaMaker.getEBonLARateClassCode();
        int EBonLARateTypeCode = formulaMaker.getEBonLARateTypeCode();
        int VAonLARateClassCode = formulaMaker.getVAonLARateClassCode();
        int VAonLARateTypeCode = formulaMaker.getVAonLARateTypeCode();
        
        eqEbRC = new Equals("rateClassCode", new Integer(EBonLARateClassCode));
        eqEbRT = new Equals("rateTypeCode", new Integer(EBonLARateTypeCode));
        eqVaRC = new Equals("rateClassCode", new Integer(VAonLARateClassCode));
        eqVaRT = new Equals("rateTypeCode", new Integer(VAonLARateTypeCode));
        eqEbRCAndEqEbRT = new And(eqEbRC, eqEbRT);
        eqVaRCAndEqVaRT = new And(eqVaRC, eqVaRT);
        ebRCAndEbRTOrVaRCAndVaRT = new Or(eqEbRCAndEqEbRT, eqVaRCAndEqVaRT);
        
        for (int index = 0; index < calAmtSize; index++) {
            personnelCalAmountsEdiBean = (BudgetPersonnelCalAmountsEdiBean) 
                                                        cvLACalAmts.get(index);
            //Find the corresponding personnel edi
            eqPeriod = new Equals("budgetPeriod", new Integer(personnelCalAmountsEdiBean.getBudgetPeriod()));
            eqLI = new Equals("lineItemNumber", new Integer(personnelCalAmountsEdiBean.getLineItemNumber()));
            eqPersonNo = new Equals("personNumber", new Integer(personnelCalAmountsEdiBean.getPersonNumber()));
            eqRateNo = new Equals("rateNumber", new Integer(personnelCalAmountsEdiBean.getRateNumber()));
            eqPeriodAndEqLI = new And(eqPeriod, eqLI);
            eqPersonNoAndEqRateNo = new And(eqPersonNo, eqRateNo);
            periodAndLIAndPersonNoAndRateNo = new And(eqPeriodAndEqLI, eqPersonNoAndEqRateNo);
            
            //Create a new personnel edi bean for LA
            personnelDetailsLAEdiBean = (BudgetPersonnelDetailsEdiBean) 
                cvPersonnelDetailsEdi.filter(periodAndLIAndPersonNoAndRateNo).get(0);
            personnelDetailsLAEdiBean = (BudgetPersonnelDetailsEdiBean) ObjectCloner.deepCopy(personnelDetailsLAEdiBean);
            
            //Get the max person number for this period and line item
            cvPersonEdiLI = cvPersonnelDetailsEdi.filter(eqPeriodAndEqLI);
            if(cvPersonEdiLI == null || cvPersonEdiLI.size() == 0) {
                newPersonNo = 1;
            } else {
                cvPersonEdiLI.sort("personNumber", false);
                newPersonNo = ((BudgetPersonnelDetailsEdiBean) 
                                    cvPersonEdiLI.get(0)).getPersonNumber() + 1;
            }
            
            //Set the new person number
            personnelDetailsLAEdiBean.setPersonNumber(newPersonNo);
            personnelDetailsLAEdiBean.setRateNumber(1);
            personnelDetailsLAEdiBean.setPersonId(PERSON_ID);
            personnelDetailsLAEdiBean.setJobCode(LA_JOB_CODE);
            personnelDetailsLAEdiBean.setSalaryRequested(personnelCalAmountsEdiBean.getCalculatedCost());
            personnelDetailsLAEdiBean.setPercentCharged(100);
            personnelDetailsLAEdiBean.setPercentEffort(100);
            personnelDetailsLAEdiBean.setCostSharingAmount(personnelCalAmountsEdiBean.getCalculatedCostSharing());
            personnelDetailsLAEdiBean.setUnderRecoveryAmount(0);
            
            //Add the new person edi to vector
            cvPersonnelDetailsEdi.add(personnelDetailsLAEdiBean);
            
            //Remove the LA edi cal amt bean from vector
            cvPersonnelCalAmtsEdi.remove(personnelCalAmountsEdiBean);
            
            //Copy EBonLA & VAonLA for this person
            filterEBonLAAndVAonLA = new And(periodAndLIAndPersonNoAndRateNo, ebRCAndEbRTOrVaRCAndVaRT);
            cvEBandVAEdiCalAmts = cvPersonnelCalAmtsEdi.filter(filterEBonLAAndVAonLA);
            if (cvEBandVAEdiCalAmts == null || cvEBandVAEdiCalAmts.size() == 0) {
                continue;
            }
            size = cvEBandVAEdiCalAmts.size();
            for (int calAmtIndex = 0; calAmtIndex < size; calAmtIndex++) {
                personnelCalAmountsEdiBean = (BudgetPersonnelCalAmountsEdiBean)
                                                cvEBandVAEdiCalAmts.get(calAmtIndex);
                
                //remove this bean from vector
                cvPersonnelCalAmtsEdi.remove(personnelCalAmountsEdiBean);
                
                //set the new person number and rate number from the new LA personnel edi bean
                personnelCalAmountsEdiBean.setPersonNumber(
                                    personnelDetailsLAEdiBean.getPersonNumber());
                personnelCalAmountsEdiBean.setRateNumber(
                                    personnelDetailsLAEdiBean.getRateNumber());
                
                //add the cal amt bean to vector
                cvPersonnelCalAmtsEdi.add(personnelCalAmountsEdiBean);
                
            }
        }
    }

    /**
     * Updates vector of Personnel EDI beans, vector of Personnel Cal Amount EDI Beans 
     * & Vector of Rate Base EDI beans to the data collection (hash table) in the 
     * query engine.
     */
    private void updateDataCollection() throws CoeusException {    
        //Update Personnel Detail EDI Beans
        if (cvPersonnelDetailsEdi == null || cvPersonnelDetailsEdi.size() == 0) {
            return;
        }
        
        int size = cvPersonnelDetailsEdi.size();
        BudgetPersonnelDetailsEdiBean personnelDetailsEdiBean;
        for (int index = 0; index < size; index++) {
            personnelDetailsEdiBean = (BudgetPersonnelDetailsEdiBean) 
                                            cvPersonnelDetailsEdi.get(index);
            queryEngine.insert(key, personnelDetailsEdiBean);
        }
        
        //Update Personnel Detail Cal Amount EDI Beans
        if (cvPersonnelCalAmtsEdi == null || cvPersonnelCalAmtsEdi.size() == 0) {
            return;
        }
        size = cvPersonnelCalAmtsEdi.size();
        BudgetPersonnelCalAmountsEdiBean personnelCalAmountsEdiBean;
        for (int index = 0; index < size; index++) {
            personnelCalAmountsEdiBean = (BudgetPersonnelCalAmountsEdiBean) 
                                            cvPersonnelCalAmtsEdi.get(index);
            queryEngine.insert(key, personnelCalAmountsEdiBean);
        }
        
        //Update Rate Base EDI Beans
        if (cvRateBaseEdi == null || cvRateBaseEdi.size() == 0) {
            return;
        }
        size = cvRateBaseEdi.size();
        BudgetRateBaseEdiBean rateBaseEdiBean;
        for (int index = 0; index < size; index++) {
            rateBaseEdiBean = (BudgetRateBaseEdiBean) 
                                            cvRateBaseEdi.get(index);
            queryEngine.insert(key, rateBaseEdiBean);
        }
        //System.out.println("Update Data Collection over");
    } // end updateDataCollection
    
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
