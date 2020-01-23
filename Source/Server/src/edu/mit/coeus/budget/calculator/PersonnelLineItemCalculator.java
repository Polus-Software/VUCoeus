/*
 * @(#)PersonnelLineItemCalculator.java October 14, 2003, 12:58 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.budget.calculator;

import java.util.*;
import java.text.SimpleDateFormat;

import edu.mit.coeus.budget.calculator.bean.*;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
import java.sql.Date;

/**
 * Used to calculate the personnel line item rates by breaking into smaller periods 
 * according to the rate changes, calculating for each breakup period, 
 * adding up to get the rates for the whole line item period.
 *
 * @author  Sagin
 * @version 1.0
 * Created on October 14, 2003, 12:58 PM
 *
 */
public class PersonnelLineItemCalculator {

  ///////////////////////////////////////
  // attributes

//    /**
//     * Represents the Budget Line Item Details.
//     */
//    private BudgetDetailBean budgetDetailBean; 
//    
    /**
     * Represents the Budget Personnel Line Item Details.
     */
    protected BudgetPersonnelDetailsBean budgetPersonnelDetails; 

    /**
     * Represents all the Personnel Line Item Calculated Amounts
     */
    protected CoeusVector cvPersonnelLineItemCalcAmts; 

//    /**
//     * Represents all the Line Item Calculated Amounts
//     */
//    private CoeusVector cvLineItemCalcAmts; 

    /**
     * Represents Vector of BreakupInterval objects
     */
    protected CoeusVector cvLIBreakupIntervals; 

    /**
     * Represents Formula Maker which contains information regarding the
     * calculation type to be applied
     */
    private FormulaMaker formulaMaker; 

    /**
     * Represents vector of Proposal Rates applicable for the line item
     */
    protected CoeusVector cvLineItemPropRates; 

    /**
     * Represents vector of Proposal LA Rates applicable for the line item
     */
    protected CoeusVector cvLineItemPropLARates; 

    /**
     * Represents the line item cost sharing applicable for a day
     */
    protected QueryEngine queryEngine = QueryEngine.getInstance(); 

    /**
     * Represents Vector containing rates not available messages
     */
    protected Vector vecMessages; 

    /**
     * Represents Vector containing salary not available messages for persons.
     * This is used to diplay while opening Budget Summary report
     */
    protected Vector vecErrMessages;
    
    /**
     * Used for calculating Salary
     */
    protected SalaryCalculator salaryCalculator;
    
    /**
     * Represents personnel line item Start Date
     */
        protected Date lineItemStartDate; 

    /**
     * Represents personnel line item End Date
     */
        protected Date lineItemEndDate; 
    
    /**
     * Used for Under-recovery calculation (enhancement)"
     */
    private boolean uRMatchesOh = false; 

    /**
     * Represents vector of Proposal Rates for calculating under-recovery
     */
    private CoeusVector cvUnderRecoveryRates; 
    
    /**
     * holds UR Rate Class Code
     */
    private int uRRateClassCode = -1;
    
    /**
     * holds UR Rate Type Code
     */
    private int uRRateTypeCode = -1;
    
    /**
     * holds Cost Element
     */
    private String costElement;
    
    /**
     * Enhancement ID : 709 Case 3
     * Flag which indicates whether the under-recovery rate base is present for 
     * the line item.
     */
    private boolean uRRateValidForCE = true;
    // Case Id #1811
    private CoeusVector cvRateBaseData = null;
    // Instance variable added for 3854: Warning in Lite when salary effective date not in place for a calculation
    private Vector vecSalaryMessages;
    // Added for Case 2228 - Print Budget Summary -Start  
    private CoeusVector cvPersonnelRateAndBase = null;
    // Added for Case 2228 - Print Budget Summary -End  
///////////////////////////////////////
  // operations


    /**
     * Constructor...
     * Accepts BudgetPersonnelDetailsBean which contains the personnel line item details
     * 
     * @param budgetPersonnelDetails 
     *
     */
    public  PersonnelLineItemCalculator() throws BudgetCalculateException{  
        vecMessages = new Vector();
        vecErrMessages = new Vector();
        vecSalaryMessages =new Vector();
        initCalcTypes(); 
    } // end PersonnelLineItemCalculator         

    /**
     * Initializes the calculation order of different rates in 
     * line item cal amts.
     * 
     */
    public void initCalcTypes() throws BudgetCalculateException{
        try {
            formulaMaker = new FormulaMaker(); 
        } catch (CoeusException coeusException) {
            throw new BudgetCalculateException(coeusException.getMessage());
        }
    } // end initCalcTypes         

    /**
     * Starts the calculation of the personnel line item and sets the calculated rates in the 
     * personnel line item cal amts.
     */
    public void calculate(BudgetPersonnelDetailsBean budgetPersonnelDetails) {        
        this.budgetPersonnelDetails = budgetPersonnelDetails; 
        lineItemStartDate = budgetPersonnelDetails.getStartDate();
        lineItemEndDate = budgetPersonnelDetails.getEndDate();  
        
        //Bug Fix : 732 - Starts here
        budgetPersonnelDetails.setDirectCost(budgetPersonnelDetails.getSalaryRequested());
        budgetPersonnelDetails.setIndirectCost(0);
        //Bug Fix : 732 - Ends here
        
        // Enhancement ID : 709 Case 3 - Starts Here
        boolean OHAvailable = true;
        budgetPersonnelDetails.setUnderRecoveryAmount(0);
        // Enhancement ID : 709 Case 3 - Ends Here
        
        try {
            if (budgetPersonnelDetails != null) {
                initValues();
                if (cvPersonnelLineItemCalcAmts != null && 
                                    cvPersonnelLineItemCalcAmts.size() > 0) {
                    filterRates();
                    createBreakupIntervals();
                    calculateBreakupIntervals();
                    updatePersonnelLIDetailCostsAndCalAmts();
                      // Added for Case 2228 - Print Budget Summary -Start 
                    updatePersonnelLIDetailCostsAndRateBase();
                      // Added for Case 2228 - Print Budget Summary -End 
                    //updateDataCollection(); Commented for Bug Fix : 732
                    
                    
                    // Enhancement ID : 709 Case 3 - Starts Here
                    if (!uRMatchesOh) {
                        //Check whether any OH Rate is present
                        Equals eqRateClassType = new Equals("rateClassType", 
                                                RateClassTypeConstants.OVERHEAD);
                        CoeusVector cvOHRate = cvPersonnelLineItemCalcAmts.filter(eqRateClassType);
                        if (cvOHRate == null || cvOHRate.size() == 0) {
                            OHAvailable = false;
                        }
                    }
                    // Enhancement ID : 709 Case 3 - Ends Here
                            
                }
                //Bug Fix : 2198 - START
                else {
                    cvRateBaseData = new CoeusVector();
                    //bug fix #2643
                    budgetPersonnelDetails.setTotalCostSharing(budgetPersonnelDetails.getCostSharingAmount());
                }
                //Bug Fix : 2198 - END
                
                updateDataCollection(); //Bug Fix : 732
                
                // Enhancement ID : 709 Case 3 - Starts Here
                if (!uRMatchesOh && (!OHAvailable || cvPersonnelLineItemCalcAmts == null ||
                    cvPersonnelLineItemCalcAmts.size() == 0) ) {
                    calculateURBase(); 
                }    
                //Enhancement ID : 709 Case 3 - Ends Here
            }   
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } // end Calculate                

    /**
     * Will use Queryengine to get and set all the required values for personnel 
     * line item calculation.
     */
    public void initValues() throws CoeusException{
        
        //set per day cost & per day cost sharing
        //setPerDayCost();
        //setPerDayCostSharing();
        
        String key = budgetPersonnelDetails.getProposalNumber() + 
                                budgetPersonnelDetails.getVersionNumber();
        cvLineItemPropLARates = new CoeusVector();
        cvLineItemPropRates = new CoeusVector();
        
        //get the on-off campus flag
        boolean onOffCampusFlag = budgetPersonnelDetails.isOnOffCampusFlag();
        Equals equalsOnOff = new Equals("onOffCampusFlag", onOffCampusFlag);
        //System.out.println("onOffCampusFlag >> " + onOffCampusFlag);
        
        //get the line item cal amts
        Equals equalsPeriod = new Equals("budgetPeriod", 
                                    new Integer(budgetPersonnelDetails.getBudgetPeriod()));
        Equals equalsLineItemNo = new Equals("lineItemNumber", 
                                    new Integer(budgetPersonnelDetails.getLineItemNumber()));
        Equals equalsPersonNo = new Equals("personNumber", 
                                    new Integer(budgetPersonnelDetails.getPersonNumber()));
        And periodANDLineItemNo = new And(equalsPeriod, equalsLineItemNo);
        And periodANDLineItemNoANDpersonNo = new And(periodANDLineItemNo, equalsPersonNo);
        cvPersonnelLineItemCalcAmts = queryEngine.getActiveData(key, 
                            BudgetPersonnelCalAmountsBean.class, periodANDLineItemNoANDpersonNo);
        //System.out.println("cvPersonnelLineItemCalcAmts.size() >> " + cvPersonnelLineItemCalcAmts.size());
        
        /**
         * Get all rates from Proposal Rates & Proposal LA Rates which matches 
         * with the rates in line item cal amts
         */
        if (cvPersonnelLineItemCalcAmts != null && cvPersonnelLineItemCalcAmts.size() > 0) {
            int calAmtSize = cvPersonnelLineItemCalcAmts.size();
            BudgetPersonnelCalAmountsBean personnelCalAmtsBean;
            int rateClassCode, rateTypeCode = 0;
            String rateClassType = "";
            Equals equalsRC;
            Equals equalsRT;
            And RCandRT;
            And RCRTandOnOff;
            
            for(int index=0; index < calAmtSize; index++) {
                CoeusVector ratesVector = new CoeusVector();
                personnelCalAmtsBean = (BudgetPersonnelCalAmountsBean) cvPersonnelLineItemCalcAmts.get(index);
                rateClassCode = personnelCalAmtsBean.getRateClassCode();
                rateTypeCode = personnelCalAmtsBean.getRateTypeCode();
                rateClassType = personnelCalAmtsBean.getRateClassType();
        //System.out.println("rateClassCode >> " + rateClassCode);
        //System.out.println("rateTypeCode >> " + rateTypeCode);
        //System.out.println("rateClassType >> " + rateClassType);
                equalsRC = new Equals("rateClassCode", new Integer(rateClassCode));
                equalsRT = new Equals("rateTypeCode", new Integer(rateTypeCode));
                RCandRT = new And(equalsRC, equalsRT);
                RCRTandOnOff = new And(RCandRT, equalsOnOff);
                
                if (rateClassType.equalsIgnoreCase("L") ||
                    rateClassType.equalsIgnoreCase("Y")) {
                    ratesVector = queryEngine.getActiveData(key, 
                                                ProposalLARatesBean.class, RCRTandOnOff);
                    if (ratesVector != null && ratesVector.size() > 0) {
                        cvLineItemPropLARates.addAll(ratesVector);
                    }
                } else {
                    ratesVector = queryEngine.getActiveData(key, 
                                                ProposalRatesBean.class, RCRTandOnOff);
            //System.out.println("ratesVector.size() >> " + ratesVector.size());
                    if (ratesVector != null && ratesVector.size() > 0) {
                        cvLineItemPropRates.addAll(ratesVector);
                    }
                }
                
            }
                
            //Get all the Under-recovery rates if UR Rate differs from OH Rate
            if (!isURMatchesOh()) {
                equalsRC = new Equals("rateClassCode", new Integer(uRRateClassCode));
                equalsRT = new Equals("rateTypeCode", new Integer(uRRateTypeCode));
                RCandRT = new And(equalsRC, equalsRT);
                RCRTandOnOff = new And(RCandRT, equalsOnOff);
                cvUnderRecoveryRates = queryEngine.getActiveData(key, 
                                            ProposalRatesBean.class, RCRTandOnOff);

            }
            //System.out.println("cvLineItemPropLARates.size() >> " + cvLineItemPropLARates.size());
            //System.out.println("cvLineItemPropRates.size() >> " + cvLineItemPropRates.size());
        }
    } // end initValues      

    /**
     * Filter out all the rates which lies outside the
     * line item end date.
     */
    protected void filterRates() { 
        
        //Filter out all rates that lies outside the personnel line item end date
        Date lineItemEndDate = budgetPersonnelDetails.getEndDate();
        LesserThan lesserThan = new LesserThan("startDate", lineItemEndDate);
        Equals equals = new Equals("startDate", lineItemEndDate);
        Or or = new Or(lesserThan, equals);
        try {
            cvLineItemPropRates = cvLineItemPropRates.filter(or);
            cvLineItemPropLARates = cvLineItemPropLARates.filter(or);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //System.out.println("cvLineItemPropLARates.size() after filter >> " + cvLineItemPropLARates.size());
        //System.out.println("cvLineItemPropRates.size() after filter >> " + cvLineItemPropRates.size());
        
        
        
    } // end filterRates         

    /**
     * Combine the sorted Prop & LA rates, which should be in sorted
     * order(asc). Now create the breakup boundaries and use it to create 
     * breakup intervals and set all the values required for calculation.
     * Then call calculateBreakupInterval method for each AmountBean for
     * setting the calculated cost & calculated cost sharing ie for each rate
     * class & rate type.
     */
    protected void createBreakupIntervals() throws CoeusException {        

        //Initialize the Message that should be shown if rate not avalilable for any period
        String messageTemplate = "";
        String multipleRatesMesgTemplate = "";
        String message = "";
        cvLIBreakupIntervals = new CoeusVector();
        if (budgetPersonnelDetails.isOnOffCampusFlag()) {
            messageTemplate = "On-Campus rate information not available for Rate Class - \'";
            multipleRatesMesgTemplate = "Multiple On-Campus rates found for the period ";
        } else {
            messageTemplate = "Off-Campus rate information not available for Rate Class - \'";
            multipleRatesMesgTemplate = "Multiple Off-Campus rates found for the period ";
        }
        // combine the sorted Prop & LA Rates
        CoeusVector cvCombinedRates = new CoeusVector();
        cvCombinedRates.addAll(cvLineItemPropRates);
        cvCombinedRates.addAll(cvLineItemPropLARates);
        //System.out.println("cvCombinedRates.size() -- " + cvCombinedRates.size());
        
        //sort the combined rates in asc order
        cvCombinedRates.sort("startDate", true);
        
        //Now start creating breakup periods
        //First get the breakup boundaries
        Date liStartDate = budgetPersonnelDetails.getStartDate();
        Date liEndDate = budgetPersonnelDetails.getEndDate();
        Vector vecBoundaries = LineItemCalculator.createBreakupBoundaries(cvCombinedRates, 
                                                liStartDate, liEndDate);
        //System.out.println("vecBoundaries.size() >> " + vecBoundaries.size());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM-dd-yyyy");
        
        //create breakup intervals based on the breakup boundaries
        if (vecBoundaries != null && vecBoundaries.size() > 0) {
            int boundarySize = vecBoundaries.size();
            Boundary boundary;
            for (int boundaryIndex = 0; boundaryIndex < boundarySize; boundaryIndex++) {
                boundary = (Boundary) vecBoundaries.get(boundaryIndex);
                BreakUpInterval breakUpInterval = new BreakUpInterval();
                breakUpInterval.setBoundary(boundary);
                //Case Id #1811 -step2 -start
                breakUpInterval.setProposalNumber(budgetPersonnelDetails.getProposalNumber());
                breakUpInterval.setVersionNumber(budgetPersonnelDetails.getVersionNumber());
                breakUpInterval.setBudgetPeriod(budgetPersonnelDetails.getBudgetPeriod());
                breakUpInterval.setLineItemNumber(budgetPersonnelDetails.getLineItemNumber());
                //Case Id #1811 -step2 - end
                
                //Get the salary for the breakup boundary
                budgetPersonnelDetails.setStartDate(new java.sql.Date(boundary.getStartDate().getTime()));
                budgetPersonnelDetails.setEndDate(new java.sql.Date(boundary.getEndDate().getTime()));
                salaryCalculator = new SalaryCalculator(budgetPersonnelDetails);
                salaryCalculator.distributeCalculatedSal();
                vecMessages.addAll(salaryCalculator.getVecMessages());
                // 3854: Warning in Lite when salary effective date not in place for a calculation - Start
                vecSalaryMessages.addAll(salaryCalculator.getVecMessages());
                // 3854: Warning in Lite when salary effective date not in place for a calculation - End
                vecErrMessages.addAll(salaryCalculator.getVecErrMessages());
                
                // Set the applicable amount
                breakUpInterval.setApplicableAmt(budgetPersonnelDetails.getSalaryRequested());
                breakUpInterval.setApplicableAmtCostSharing(budgetPersonnelDetails.getCostSharingAmount());
                //COEUS-702: Budget issue calculating UR with "FUNSN"
                breakUpInterval.setURMatchesOh(isURMatchesOh());
                //COEUS-702: Budget issue calculating UR with "FUNSN"
                
                
                CoeusVector cvAmountDetails = new CoeusVector();
                CoeusVector cvBreakupPropRates = new CoeusVector();
                CoeusVector cvBreakupPropLARates = new CoeusVector();
                CoeusVector cvTempRates = new CoeusVector();
                CoeusVector cvMultipleRates = new CoeusVector();
                String rateClassType = "";
                int rateClassCode = 0;
                int rateTypeCode = 0;
                boolean applyRateFlag;
                
                //Loop and add all data required in breakup interval
                int calAmtsSize = cvPersonnelLineItemCalcAmts.size();
                BudgetDetailCalAmountsBean calAmtsBean;
                for (int calAmtIndex = 0; calAmtIndex < calAmtsSize; calAmtIndex++) {
                    calAmtsBean = (BudgetDetailCalAmountsBean) cvPersonnelLineItemCalcAmts.get(calAmtIndex);
                    applyRateFlag = calAmtsBean.isApplyRateFlag();
                    rateClassType = calAmtsBean.getRateClassType();
                    rateClassCode = calAmtsBean.getRateClassCode();
                    rateTypeCode = calAmtsBean.getRateTypeCode();
                    
                    //form the rate not available message
                    message = messageTemplate + calAmtsBean.getRateClassDescription() + 
                                "\'  Rate Type - \'" + calAmtsBean.getRateTypeDescription() + "\' for Period - " ;
                    
                    //if apply flag is false and rate class type is not Overhead then skip
                    if ( !applyRateFlag && !rateClassType.equals(RateClassTypeConstants.OVERHEAD)) {
                        continue;
                    }
                    
                    AmountBean amountBean = new AmountBean();
                    amountBean.setApplyRateFlag(applyRateFlag);
                    amountBean.setRateClassType(rateClassType);
                    amountBean.setRateClassCode(rateClassCode);
                    amountBean.setRateTypeCode(rateTypeCode);
                    cvAmountDetails.add(amountBean);
                    
                    //filter & store the rates applicable for this rate class / rate type
                    Equals equalsRC = new Equals("rateClassCode", new Integer(rateClassCode));
                    Equals equalsRT = new Equals("rateTypeCode", new Integer(rateTypeCode));
                    LesserThan ltEndDate = new LesserThan("startDate", boundary.getEndDate());
                    Equals equalsEndDate = new Equals("startDate", boundary.getEndDate());
                    GreaterThan gtStartDate = new GreaterThan("startDate", boundary.getStartDate());
                    Equals equalsStartDate = new Equals("startDate", boundary.getStartDate());
                    Or gtStartDateOrEqStartDate = new Or(gtStartDate, equalsStartDate);
                    Or ltEndDateOrEqEndDate = new Or(ltEndDate, equalsEndDate);
                    And gtOrEqStartDateAndltOrEqEndDate = new And(gtStartDateOrEqStartDate, ltEndDateOrEqEndDate);
                    And RCandRT = new And(equalsRC, equalsRT);
                    And RCRTandLtStartDate = new And(RCandRT, ltEndDateOrEqEndDate);
                    And RCRTandgtStartDateAndltEndDate = new And(RCandRT, gtOrEqStartDateAndltOrEqEndDate);
                    
                    if (rateClassType.equalsIgnoreCase("L") ||
                        rateClassType.equalsIgnoreCase("Y")) {
                        cvTempRates = cvLineItemPropLARates.filter(RCRTandLtStartDate);
                        if (cvTempRates != null && cvTempRates.size() > 0) {
                            
                            /**
                             * Check if multiple rates are present got this period.
                             * If there, then show message and don't add any rates.
                             */
                            cvMultipleRates = cvLineItemPropLARates.filter(RCRTandgtStartDateAndltEndDate);
                            if (cvMultipleRates != null && cvMultipleRates.size() > 1) {
                                
                                //Store the multiple rates available message in a message vector

                                message = multipleRatesMesgTemplate + 
                                            simpleDateFormat.format(boundary.getStartDate()) + 
                                            " to " +
                                            simpleDateFormat.format(boundary.getEndDate()) +
                                            " for Rate Class - \'"+ calAmtsBean.getRateClassDescription() + 
                                            "\'  Rate Type - \'" + calAmtsBean.getRateTypeDescription(); 
                                vecMessages.add(message);
                            
                            } else {
                                /**
                                 *sort the rates in desc order and take the first rate 
                                 *which is the latest
                                 */
                                cvTempRates.sort("startDate", false);
                                cvBreakupPropLARates.add(cvTempRates.get(0));
                            }
                        } else {
                            //Store the rate not available message in a message vector
                            
                            message = message + 
                                        simpleDateFormat.format(boundary.getStartDate()) + 
                                        " to " +
                                        simpleDateFormat.format(boundary.getEndDate()); 
                            vecMessages.add(message);
                        }
                    } else {
                        cvTempRates = cvLineItemPropRates.filter(RCRTandLtStartDate);
                        if (cvTempRates != null && cvTempRates.size() > 0) {
                            
                            /**
                             * Check if multiple rates are present got this period.
                             * If there, then show message and don't add any rates.
                             */
                            cvMultipleRates = cvLineItemPropLARates.filter(RCRTandgtStartDateAndltEndDate);
                            if (cvMultipleRates != null && cvMultipleRates.size() > 1) {
                                
                                //Store the multiple rates available message in a message vector

                                message = multipleRatesMesgTemplate + 
                                            simpleDateFormat.format(boundary.getStartDate()) + 
                                            " to " +
                                            simpleDateFormat.format(boundary.getEndDate()) +
                                            " for Rate Class - \'"+ calAmtsBean.getRateClassDescription() + 
                                            "\'  Rate Type - \'" + calAmtsBean.getRateTypeDescription(); 
                                vecMessages.add(message);
                            } else {
                                /**
                                 *sort the rates in desc order and take the first rate 
                                 *which is the latest
                                 */
                                cvTempRates.sort("startDate", false);
                                cvBreakupPropRates.add(cvTempRates.get(0));
                            }
                        } else {
                            //Store the rate not available message in a message vector
                            message = message + 
                                        simpleDateFormat.format(boundary.getStartDate()) + 
                                        " to " +
                                        simpleDateFormat.format(boundary.getEndDate()); 
                            vecMessages.add(message);
                        }
                    }
                    
                }//breakup interval data setting loop ends here
                    
                //set the values for the breakup interval in the BreakupInterval bean
                if (cvAmountDetails != null && cvAmountDetails.size() > 0) {
                    breakUpInterval.setCvAmountDetails(cvAmountDetails);
                    breakUpInterval.setCvPropRates(cvBreakupPropRates);
                    breakUpInterval.setCvPropLARates(cvBreakupPropLARates);
                    cvLIBreakupIntervals.add(breakUpInterval);
                }
                
                //Set the URRates if required
                if (!isURMatchesOh()) {
                    //Enhancement ID : 709 Case 3 - Starts here
                    if (!isURRateValidForCE()) {
                         // Commenetd to fix 1091 - Sagin/Chandra - 13-Aug- Start
//                        ProposalRatesBean uRRatesBean = new ProposalRatesBean();
//                        uRRatesBean.setInstituteRate(0);
//                        breakUpInterval.setURRatesBean(uRRatesBean);
                    //Enhancement ID : 709 Case 3 - Ends here
                         // Commenetd to fix 1091 - Sagin/Chandra - 13-Aug- End
                    
                    } else if (cvUnderRecoveryRates != null && cvUnderRecoveryRates.size() > 0) {
                        LesserThan ltEndDate = new LesserThan("startDate", boundary.getEndDate());
                        Equals equalsEndDate = new Equals("startDate", boundary.getEndDate());
                        Or ltEndDateOrEqEndDate = new Or(ltEndDate, equalsEndDate);
                        
                        cvTempRates = cvUnderRecoveryRates.filter(ltEndDateOrEqEndDate);
                        if (cvTempRates != null && cvTempRates.size() > 0) {
                            /**
                             *sort the rates in desc order and take the first rate 
                             *which is the latest
                             */
                            cvTempRates.sort("startDate", false);
                            breakUpInterval.setURRatesBean((ProposalRatesBean)cvTempRates.get(0));
                        }
                    }
                }
                
            }//breakup interval creation loop ends here
        //System.out.println("cvLIBreakupIntervals.size() >> " + cvLIBreakupIntervals.size());
        
        }//if for vecBoundaries checking ends here

    } // end CreateBreakupIntervals          

    /**
     * calculate the cost for each beakup interval.
     */
    protected void calculateBreakupIntervals() {        
        String key = budgetPersonnelDetails.getProposalNumber() + budgetPersonnelDetails.getVersionNumber();
        int rateNumber=0;
        CoeusVector cvRateBase = null;
        
        if (cvLIBreakupIntervals != null && cvLIBreakupIntervals.size() > 0) {
            int size = cvLIBreakupIntervals.size();
            BreakUpInterval breakUpInterval;
            cvRateBaseData = new CoeusVector();
            for (int index = 0; index < size; index++) {
                breakUpInterval = (BreakUpInterval) cvLIBreakupIntervals.get(index);
                //Case Id #1811 -step1 - start
                breakUpInterval.setRateNumber(rateNumber);
                
                //Added for COEUSQA-2393 Revamp Coeus Budget Engine -start
                
                // This method is to Calculate Rates for the LineItem based on the calculation methology defined in the
                // Inclusion and Exclusion table for each breakup interval.
                
                try {
                    breakUpInterval.calcBreakUpIntervalForInclAndExcl();
                } catch (Exception ex) {
                    UtilFactory.log( ex.getMessage() , ex , "PersonnelLineItemCalculator" , "calculateBreakupIntervals()" );
                }
                
                //COMMENTED OLD METHOD : This method is calculation logic was hardcoded.
//                 breakUpInterval.calculateBreakupInterval(formulaMaker);
                
                //Added for COEUSQA-2393 Revamp Coeus Budget Engine -end
                
                                 
                //Added for COEUSQA-2889 PDF Print>Budget Summary by Period Always Prints ON CAMPUS - Start
                if(breakUpInterval.getRateBase()!= null && breakUpInterval.getRateBase().size() >0){
                    // Take all the Rate and base values for the line item from breakup interval, set the correct campus flag value.
                    for(Object objRateAndBase : breakUpInterval.getRateBase()){
                        BudgetRateBaseBean budgetRateAndBaseBean = (BudgetRateBaseBean) objRateAndBase;
                        budgetRateAndBaseBean.setOnOffCampusFlag(budgetPersonnelDetails.isOnOffCampusFlag());
                    }
                    
                    cvRateBaseData.addAll(breakUpInterval.getRateBase());
                    rateNumber = rateNumber+breakUpInterval.getCvAmountDetails().size();
                }
                //COEUSQA-2889 -End
                
               
                //Case Id #1811 -step1 - End
            }
        }
         
           
        try {
            //Calculate the Total salary of the Person for the whole period
            budgetPersonnelDetails.setStartDate(lineItemStartDate);
            budgetPersonnelDetails.setEndDate(lineItemEndDate);
            salaryCalculator = new SalaryCalculator(budgetPersonnelDetails);
            salaryCalculator.distributeCalculatedSal();
        } catch(CoeusException coEx) {
            coEx.printStackTrace();
        }
        
        //System.out.println("Personnel Line Item Calculation over");
    } // end calculateBreakupIntervals     
    
    /** return the CoeusVector which contains the Rate and Base for the 
     *personnel Line Item
     *Case Id #1811
     */
    public CoeusVector getPersonalRateBase(){
        return cvRateBaseData;
        
        }

    /**
     * - Sum up all the calculated costs for each breakup interval and then update the 
     * personnel line item cal amts.
     * - Sum up all the underRecovery costs for each breakup interval and then update the 
     * personnel line item details.
     * - Sum up all direct costs ie, rates for RateClassType <> 'O', for each breakup interval 
     * and then update the personnel line item details.
     * - Sum up all Indirect costs ie, rates for RateClassType = 'O', for each breakup interval 
     * and then update the personnel line item details.
     */
    private void updatePersonnelLIDetailCostsAndCalAmts() {        
        if (cvPersonnelLineItemCalcAmts != null && cvPersonnelLineItemCalcAmts.size() > 0 
            && cvLIBreakupIntervals != null && cvLIBreakupIntervals.size() > 0) {
            /**
             * Sum up all the calculated costs for each breakup interval and 
             * then update the line item cal amts. 
             */  
            int size = 0;
            int index = 0;
            int rateClassCode = 0;
            int rateTypeCode = 0;
            double totalCalculatedCost = 0;
            double totalCalculatedCostSharing = 0;
            double totalUnderRecovery = 0;
            double directCost = 0;
            double indirectCost = 0;
            Equals equalsRC;
            Equals equalsRT;
            And RCandRT;
            CoeusVector cvCombinedAmtDetails = new CoeusVector();
            BudgetPersonnelCalAmountsBean personnelCalAmtsBean;
            
            //Loop and add all the amount details from all the breakup intervals
            size = cvLIBreakupIntervals.size();
            for (index = 0; index < size; index++) {
                cvCombinedAmtDetails.addAll( ((BreakUpInterval) cvLIBreakupIntervals.get(index)).getCvAmountDetails());
            }
            
            //loop thru all cal amount rates, sum up the costs and set it
            size = cvPersonnelLineItemCalcAmts.size();
            for (index = 0; index < size; index++) {
                personnelCalAmtsBean = (BudgetPersonnelCalAmountsBean) cvPersonnelLineItemCalcAmts.get(index);
                
                //if this rate need not be applied skip
                if (!personnelCalAmtsBean.isApplyRateFlag()) {
                    continue;
                }
                
                rateClassCode = personnelCalAmtsBean.getRateClassCode();
                rateTypeCode = personnelCalAmtsBean.getRateTypeCode();
                equalsRC = new Equals("rateClassCode", new Integer(rateClassCode));
                equalsRT = new Equals("rateTypeCode", new Integer(rateTypeCode));
                RCandRT = new And(equalsRC, equalsRT);
                
                /** Bug Fix #1885 - start
                 */
//                totalCalculatedCost = cvCombinedAmtDetails.sum("calculatedCost", RCandRT);
                totalCalculatedCost = ((double)Math.round(cvCombinedAmtDetails.sum("calculatedCost",RCandRT)*
                        Math.pow(10.0, 2) )) / 100;
                
                personnelCalAmtsBean.setCalculatedCost(totalCalculatedCost);
                //totalCalculatedCostSharing = cvCombinedAmtDetails.sum("calculatedCostSharing", RCandRT);
                totalCalculatedCostSharing = ((double)Math.round(cvCombinedAmtDetails.sum("calculatedCostSharing",RCandRT)*
                        Math.pow(10.0, 2) )) / 100;
                /** Bug Fix #1885 - End
                 */
                personnelCalAmtsBean.setCalculatedCostSharing(totalCalculatedCostSharing);
            }
            
            /**
             * Sum up all the underRecovery costs for each breakup interval and then update the 
             * personnel line item details.
             */       
            //COEUSDEV:932 - Error in Under recovery calculation in budget summary & distribution screen - Start
            //Rounding off the UnderRecoveryAmount for two decimal places.
//            totalUnderRecovery = cvLIBreakupIntervals.sum("underRecovery");
             totalUnderRecovery = Math.round(cvLIBreakupIntervals.sum("underRecovery") * 100.00) / 100.00;
            //COEUSDEV:932 - End
            budgetPersonnelDetails.setUnderRecoveryAmount(totalUnderRecovery);
            
            /**
             * Sum up all direct costs ie, rates for RateClassType <> 'O', for each breakup interval
             * plus the line item cost, and then update the personnel line item details.
             */
            NotEquals notEqualsOH = new NotEquals("rateClassType", RateClassTypeConstants.OVERHEAD);
            //directCost = cvCombinedAmtDetails.sum("calculatedCost", notEqualsOH);
            // Bgu fix #1835 -start
            directCost = ((double)Math.round(cvCombinedAmtDetails.sum("calculatedCost", notEqualsOH)*
                        Math.pow(10.0, 2) )) / 100;
            // Bgu fix #1835 -End
            budgetPersonnelDetails.setDirectCost(directCost + budgetPersonnelDetails.getSalaryRequested());
            /**
             * Sum up all Indirect costs ie, rates for RateClassType = 'O', for each breakup interval 
             * and then update the personnel line item details.
             */
            Equals equalsOH = new Equals("rateClassType", RateClassTypeConstants.OVERHEAD);
            //indirectCost = cvCombinedAmtDetails.sum("calculatedCost", equalsOH);
            // Bgu fix #1835 -start
            indirectCost = ((double)Math.round(cvCombinedAmtDetails.sum("calculatedCost", equalsOH)*
                        Math.pow(10.0, 2) )) / 100;
            // Bgu fix #1835 -End
            
           // System.out.println("The InDirect cost of Personnel is : "+indirectCost);
            budgetPersonnelDetails.setIndirectCost(indirectCost);
            
            /**
             * Sum up all Cost Sharing amounts ie, rates for RateClassType <> 'O' and set 
             * in the calculatedCostSharing field of personnel line item details
             */
            //totalCalculatedCostSharing = cvCombinedAmtDetails.sum("calculatedCostSharing");
            // Bgu fix #1835 -start
            totalCalculatedCostSharing = ((double)Math.round(cvCombinedAmtDetails.sum("calculatedCostSharing")*
                        Math.pow(10.0, 2) )) / 100;
            // Bgu fix #1835 -End
            
            budgetPersonnelDetails.setTotalCostSharing(budgetPersonnelDetails.getCostSharingAmount() + 
                                                    totalCalculatedCostSharing);
            
        }
         //System.out.println("Update LI Detail Costs & Calc Amts sum up over");
    } // end updateLIDetailCostsAndCalAmts              

    /**
     * Enhancement ID : 709 Case 3 - Starts here
     * Update the Underrecovery amount based on the UR Rate Base and valid OH rates
     * available for this cost element.
     */
    private void calculateURBase() throws CoeusException, DBException { 
        //Get all the valid OH Rates for this cost element
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        CoeusVector cvValidOHRatesForCE = budgetDataTxnBean.getValidOHRateTypesForCE(costElement);
        
        //Filter and get the OH rate corresponding to the UR Rate Base
        Equals equalsRC = new Equals("rateClassCode", new Integer(uRRateClassCode));
        Equals equalsRT = new Equals("rateTypeCode", new Integer(uRRateTypeCode));
        And RCandRT = new And(equalsRC, equalsRT);
        
        CoeusVector cvURRate = cvValidOHRatesForCE.filter(RCandRT);
        
        //If the UR rate available for the cost element, then calculate under-recovery
        if (cvValidOHRatesForCE != null && cvValidOHRatesForCE.size() > 0) {
            //Filter and get all rates for the UR Rate and which lies before line item end date
            boolean onOffCampusFlag = budgetPersonnelDetails.isOnOffCampusFlag();
            Equals equalsOnOff = new Equals("onOffCampusFlag", onOffCampusFlag);
            And RCRTandOnOff = new And(RCandRT, equalsOnOff);
            
            Date lineItemEndDate = budgetPersonnelDetails.getEndDate();
            LesserThan leStartDate = new LesserThan("startDate", lineItemEndDate);
            Equals eqStartDate = new Equals("startDate", lineItemEndDate);
            Or lesserThanOrEqStDate = new Or(leStartDate, eqStartDate);
            And RCRTandOnOffAndLeOrEqStDate = new And(RCRTandOnOff, lesserThanOrEqStDate);
            String key = budgetPersonnelDetails.getProposalNumber() + 
                                budgetPersonnelDetails.getVersionNumber();
            cvLineItemPropRates = queryEngine.getActiveData(key, 
                        ProposalRatesBean.class, RCRTandOnOffAndLeOrEqStDate);
            cvLineItemPropLARates = new CoeusVector();
            
            //Create a new Cal Amount Bean with this UR Rate for doing calculation
            BudgetPersonnelCalAmountsBean personnelCalAmountsBean = new BudgetPersonnelCalAmountsBean();
            personnelCalAmountsBean.setApplyRateFlag(true);
            personnelCalAmountsBean.setRateClassType(RateClassTypeConstants.OVERHEAD);
            personnelCalAmountsBean.setRateClassCode(uRRateClassCode);
            personnelCalAmountsBean.setRateTypeCode(uRRateTypeCode);
            personnelCalAmountsBean.setCalculatedCost(0);
            personnelCalAmountsBean.setCalculatedCostSharing(0);
            
            cvPersonnelLineItemCalcAmts.clear();
            cvPersonnelLineItemCalcAmts.add(personnelCalAmountsBean);
            
            //Calculate the rate for this UR Base
            createBreakupIntervals();
            calculateBreakupIntervals();
            
            /**
             * Sum up all the calculated costs for each breakup interval and then update the 
             * line item detail underrecovery.
             */
            if (cvLIBreakupIntervals != null && cvLIBreakupIntervals.size() > 0) {
                CoeusVector cvCombinedAmtDetails = new CoeusVector();
                
                //Loop and add all the amount details from all the breakup intervals
                int size = cvLIBreakupIntervals.size();
                for (int index = 0; index < size; index++) {
                    cvCombinedAmtDetails.addAll( ((BreakUpInterval) cvLIBreakupIntervals.get(index)).getCvAmountDetails());
                }
                budgetPersonnelDetails.setUnderRecoveryAmount(cvCombinedAmtDetails.sum("calculatedCost"));

                cvPersonnelLineItemCalcAmts.clear();
                //Added for Case COEUSDEV-234 Error saving budget when personnel cost element has EB but no OH -start
                // If Overhead is not available, this updateDataCollection() method will calculate Underrecovery Base and 
                // all the data will be update using updateDataCollection(). This updateDataCollection() was called before calling calculateURBase(), 
                // CoeusVector of cvPersonnelRateAndBase will be available in instance level,  
                // same collection is updating twice, because of that clearing the collection.
                cvPersonnelRateAndBase.clear();
                //Added for Case COEUSDEV-234 -End
                //Update to queryEngine
                updateDataCollection();
            }
            
            
        }
    }    
    //Enhancement ID : 709 Case 3 - Ends here

    /**
     * - Sum up all the calculated amounts for Personnel Cal Amts and update the
     * line item cal amts.
     * - Sum up all the Requested Salary for Personnel Details and then update the 
     * line Item Cost of line item details.
     * - Sum up all the CostSharing for Personnel Details and then update the 
     * CostSharing of line item details.
     * - Sum up all the underRecovery costs for Personnel Details and then update the 
     * Underrecovery of line item details.
     * - Sum up all direct costs for Personnel Details and then update the 
     * DirectCost of line item details.
     * - Sum up all Indirect costs for Personnel Details and then update the 
     * IndirectCost of line item details.
     */
//    private void updateLIDetailCostsAndCalAmts() {        
//        if (cvLineItemCalcAmts != null && cvLineItemCalcAmts.size() > 0 
//            && cvLIBreakupIntervals != null && cvLIBreakupIntervals.size() > 0) {
//            /**
//             * Sum up all the calculated costs for each breakup interval and 
//             * then update the line item cal amts. 
//             */  
//            int size = 0;
//            int index = 0;
//            int rateClassCode = 0;
//            int rateTypeCode = 0;
//            double totalCalculatedCost = 0;
//            double totalCalculatedCostSharing = 0;
//            double totalUnderRecovery = 0;
//            double directCost = 0;
//            double indirectCost = 0;
//            Equals equalsRC;
//            Equals equalsRT;
//            And RCandRT;
//            CoeusVector cvCombinedAmtDetails = new CoeusVector();
//            BudgetDetailCalAmountsBean calAmtsBean;
//            
//            //Loop and add all the amount details from all the breakup intervals
//            size = cvLIBreakupIntervals.size();
//            for (index = 0; index < size; index++) {
//                cvCombinedAmtDetails.addAll( ((BreakUpInterval) cvLIBreakupIntervals.get(index)).getCvAmountDetails());
//            }
//            
//            //loop thru all cal amount rates, sum up the costs and set it
//            size = cvLineItemCalcAmts.size();
//            for (index = 0; index < size; index++) {
//                calAmtsBean = (BudgetDetailCalAmountsBean) cvLineItemCalcAmts.get(index);
//                
//                //if this rate need not be applied skip
//                if (!calAmtsBean.isApplyRateFlag()) {
//                    continue;
//                }
//                
//                rateClassCode = calAmtsBean.getRateClassCode();
//                rateTypeCode = calAmtsBean.getRateTypeCode();
//                equalsRC = new Equals("rateClassCode", new Integer(rateClassCode));
//                equalsRT = new Equals("rateTypeCode", new Integer(rateTypeCode));
//                RCandRT = new And(equalsRC, equalsRT);
//                totalCalculatedCost = cvCombinedAmtDetails.sum("calculatedCost", RCandRT);
//                calAmtsBean.setCalculatedCost(totalCalculatedCost);
//                totalCalculatedCostSharing = cvCombinedAmtDetails.sum("calculatedCostSharing", RCandRT);
//                calAmtsBean.setCalculatedCostSharing(totalCalculatedCostSharing);
//            }
//            
//            /**
//             * Sum up all the underRecovery costs for each breakup interval and then update the 
//             * line item details.
//             */       
//            totalUnderRecovery = cvLIBreakupIntervals.sum("underRecovery");
//            budgetDetailBean.setUnderRecoveryAmount(totalUnderRecovery);
//            
//            /**
//             * Sum up all direct costs ie, rates for RateClassType <> 'O', for each breakup interval
//             * plus the line item cost, and then update the line item details.
//             */
//            NotEquals notEqualsOH = new NotEquals("rateClassType", RateClassTypeConstants.OVERHEAD);
//            directCost = cvCombinedAmtDetails.sum("calculatedCost", notEqualsOH);
//            budgetDetailBean.setDirectCost(directCost + budgetDetailBean.getLineItemCost());
//            
//            /**
//             * Sum up all Indirect costs ie, rates for RateClassType = 'O', for each breakup interval 
//             * and then update the line item details.
//             */
//            Equals equalsOH = new Equals("rateClassType", RateClassTypeConstants.OVERHEAD);
//            indirectCost = cvCombinedAmtDetails.sum("calculatedCost", equalsOH);
//            budgetDetailBean.setIndirectCost(indirectCost);
//            
//            /**
//             * Sum up all Cost Sharing amounts ie, rates for RateClassType <> 'O' and set 
//             * in the calculatedCostSharing field of line item details
//             */
//            totalCalculatedCostSharing = cvCombinedAmtDetails.sum("calculatedCostSharing");
//            budgetDetailBean.setTotalCostSharing(budgetDetailBean.getCostSharingAmount() + 
//                                                    totalCalculatedCostSharing);
//            
//        }
//         System.out.println("Update LI Detail Costs & Calc Amts sum up over");
//    } // end updateLIDetailCostsAndCalAmts      

    /**
     * Updates BudgetDetailBean & vector of BudgetCalAmtBeans to the 
     * data collection (hash table) in the query engine.
     */
    private void updateDataCollection() throws CoeusException {    
        String key = budgetPersonnelDetails.getProposalNumber() + budgetPersonnelDetails.getVersionNumber();
        
        //Update BudgetPersonnelDetailsBean
        if (budgetPersonnelDetails.getAcType() == null) {
            budgetPersonnelDetails.setAcType(TypeConstants.UPDATE_RECORD);
        }
        queryEngine.update(key, budgetPersonnelDetails);
        
        //Update Personnel Cal Amt beans
        int size = cvPersonnelLineItemCalcAmts.size();
        BudgetPersonnelCalAmountsBean personnelCalAmtsBean;
        for (int index = 0; index < size; index++) {
            personnelCalAmtsBean = (BudgetPersonnelCalAmountsBean) cvPersonnelLineItemCalcAmts.get(index);
            if (personnelCalAmtsBean.getAcType() == null) {
                personnelCalAmtsBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
            queryEngine.update(key, personnelCalAmtsBean);
        }
        // Added for Case 2228 - Print Budget Summary -Start   
        BudgetPersonnelDetailsRateBaseBean budgetPerDetRateBaseBean;
        if(cvPersonnelRateAndBase !=null && cvPersonnelRateAndBase.size() > 0){
             for(int rateIndex = 0; rateIndex < cvPersonnelRateAndBase.size(); rateIndex++ ){
                budgetPerDetRateBaseBean =  (BudgetPersonnelDetailsRateBaseBean) cvPersonnelRateAndBase.get(rateIndex);
                budgetPerDetRateBaseBean.setAcType(TypeConstants.INSERT_RECORD);
//                queryEngine.update(key, budgetPerDetRateBaseBean);
             }
            CoeusVector cvRateBase = queryEngine.getDetails(key,BudgetPersonnelDetailsRateBaseBean.class);
             cvRateBase.addAll(cvPersonnelRateAndBase);
              queryEngine.addCollection(key,BudgetPersonnelDetailsRateBaseBean.class,cvRateBase);
              //Added for Case COEUSDEV-302 : ERROR saving budget-Start
               // CoeusVector of cvPersonnelRateAndBase will be available in instance level,  
                // same collection is updating twice, because of that clearing the collection.
                cvPersonnelRateAndBase.clear();
                //Added for Case COEUSDEV-302 -End
        }
         // Added for Case 2228 - Print Budget Summary -End 
        //System.out.println("Personnel Update Data Collection over");
    } // end updateDataCollection
    
    /** Getter for property budgetPersonnelDetails.
     * @return Value of property budgetPersonnelDetails.
     */
    public BudgetPersonnelDetailsBean getBudgetPersonnelDetails() {
        return budgetPersonnelDetails;
    }
    
    /** Setter for property budgetPersonnelDetails.
     * @param budgetPersonnelDetails New value of property budgetPersonnelDetails.
     */
    public void setBudgetPersonnelDetails(BudgetPersonnelDetailsBean budgetPersonnelDetails) {
        this.budgetPersonnelDetails = budgetPersonnelDetails;
    }
    
    /** Getter for property cvPersonnelLineItemCalcAmts.
     * @return Value of property cvPersonnelLineItemCalcAmts.
     */
    public CoeusVector getCvPersonnelLineItemCalcAmts() {
        return cvPersonnelLineItemCalcAmts;
    }
    
    /** Setter for property cvPersonnelLineItemCalcAmts.
     * @param cvPersonnelLineItemCalcAmts New value of property cvPersonnelLineItemCalcAmts.
     */
    public void setCvPersonnelLineItemCalcAmts(CoeusVector cvPersonnelLineItemCalcAmts) {
        this.cvPersonnelLineItemCalcAmts = cvPersonnelLineItemCalcAmts;
    }
    
    /** Getter for property vecMessages.
     * @return Value of property vecMessages.
     *
     */
    public java.util.Vector getVecMessages() {
        return vecMessages;
    }
    
    /** Setter for property vecMessages.
     * @param vecMessages New value of property vecMessages.
     *
     */
    public void setVecMessages(java.util.Vector vecMessages) {
        this.vecMessages = vecMessages;
    }
    
    /** Getter for property uRMatchesOh.
     * @return Value of property uRMatchesOh.
     *
     */
    public boolean isURMatchesOh() {
        return uRMatchesOh;
    }
    
    /** Setter for property uRMatchesOh.
     * @param uRMatchesOh New value of property uRMatchesOh.
     *
     */
    public void setURMatchesOh(boolean uRMatchesOh) {
        this.uRMatchesOh = uRMatchesOh;
    }
    
    /** Getter for property cvUnderRecoveryRates.
     * @return Value of property cvUnderRecoveryRates.
     *
     */
    public edu.mit.coeus.utils.CoeusVector getCvUnderRecoveryRates() {
        return cvUnderRecoveryRates;
    }
    
    /** Setter for property cvUnderRecoveryRates.
     * @param cvUnderRecoveryRates New value of property cvUnderRecoveryRates.
     *
     */
    public void setCvUnderRecoveryRates(edu.mit.coeus.utils.CoeusVector cvUnderRecoveryRates) {
        this.cvUnderRecoveryRates = cvUnderRecoveryRates;
    }
    
    /** Getter for property uRRateClassCode.
     * @return Value of property uRRateClassCode.
     *
     */
    public int getURRateClassCode() {
        return uRRateClassCode;
    }
    
    /** Setter for property uRRateClassCode.
     * @param uRRateClassCode New value of property uRRateClassCode.
     *
     */
    public void setURRateClassCode(int uRRateClassCode) {
        this.uRRateClassCode = uRRateClassCode;
    }
    
    /** Getter for property uRRateTypeCode.
     * @return Value of property uRRateTypeCode.
     *
     */
    public int getURRateTypeCode() {
        return uRRateTypeCode;
    }
    
    /** Setter for property uRRateTypeCode.
     * @param uRRateTypeCode New value of property uRRateTypeCode.
     *
     */
    public void setURRateTypeCode(int uRRateTypeCode) {
        this.uRRateTypeCode = uRRateTypeCode;
    }
    
    /** Getter for property cvLIBreakupIntervals.
     * @return Value of property cvLIBreakupIntervals.
     *
     */
    public CoeusVector getCvLIBreakupIntervals() {
        return cvLIBreakupIntervals;
    }
    
    /** Setter for property cvLIBreakupIntervals.
     * @param cvLIBreakupIntervals New value of property cvLIBreakupIntervals.
     *
     */
    public void setCvLIBreakupIntervals(CoeusVector cvLIBreakupIntervals) {
        this.cvLIBreakupIntervals = cvLIBreakupIntervals;
    }
    
    /** Getter for property lineItemStartDate.
     * @return Value of property lineItemStartDate.
     *
     */
    public java.sql.Date getLineItemStartDate() {
        return lineItemStartDate;
    }
    
    /** Setter for property lineItemStartDate.
     * @param lineItemStartDate New value of property lineItemStartDate.
     *
     */
    public void setLineItemStartDate(java.sql.Date lineItemStartDate) {
        this.lineItemStartDate = lineItemStartDate;
    }
    
    /** Getter for property lineItemEndDate.
     * @return Value of property lineItemEndDate.
     *
     */
    public java.sql.Date getLineItemEndDate() {
        return lineItemEndDate;
    }
    
    /** Setter for property lineItemEndDate.
     * @param lineItemEndDate New value of property lineItemEndDate.
     *
     */
    public void setLineItemEndDate(java.sql.Date lineItemEndDate) {
        this.lineItemEndDate = lineItemEndDate;
    }
    
    /** Getter for property formulaMaker.
     * @return Value of property formulaMaker.
     *
     */
    public FormulaMaker getFormulaMaker() {
        return formulaMaker;
    }
    
    /** Setter for property formulaMaker.
     * @param formulaMaker New value of property formulaMaker.
     *
     */
    public void setFormulaMaker(FormulaMaker formulaMaker) {
        this.formulaMaker = formulaMaker;
    }
    
    /** Getter for property vecErrMessages.
     * @return Value of property vecErrMessages.
     *
     */
    public Vector getVecErrMessages() {
        return vecErrMessages;
    }
    /** Setter for property vecErrMessages.
     * @param vecErrMessages New value of property vecErrMessages.
     *
     */
    public void setVecErrMessages(Vector vecErrMessages) {
        this.vecErrMessages = vecErrMessages;
    }
    
    // Enhancement ID : 709 Case 3 - Starts Here
    /** Getter for property costElement.
     * @return Value of property costElement.
     *
     */
    public String getCostElement() {
        return costElement;
    }
    
    /** Setter for property costElement.
     * @param costElement New value of property costElement.
     *
     */
    public void setCostElement(String costElement) {
        this.costElement = costElement;
    }
    
    /** Getter for property isURRateValidForCE.
     * @return Value of property isURRateValidForCE.
     *
     */
    public boolean isURRateValidForCE() {
        return uRRateValidForCE;
    }
    
    /** Setter for property isURRateValidForCE.
     * @param isURRateValidForCE New value of property isURRateValidForCE.
     *
     */
    public void setURRateValidForCE(boolean uRRateValidForCE) {
        this.uRRateValidForCE = uRRateValidForCE;
    }
    // Enhancement ID : 709 Case 3 - Ends Here
    
 // end setPerDayCostSharing  
 // 3854: Warning in Lite when salary effective date not in place for a calculation - Start
    public Vector getVecSalaryMessages() {
        return vecSalaryMessages;
    }
 // 3854: Warning in Lite when salary effective date not in place for a calculation - End
   
   // Added for Case 2228 - Print Budget Summary -Start 
    /** 
     * Update the Rate and Base Break Up data for Personnel Line Item with out persons.
     */
    private void updatePersonnelLIDetailCostsAndRateBase() {
        if (cvPersonnelLineItemCalcAmts != null && cvPersonnelLineItemCalcAmts.size() > 0
                && cvLIBreakupIntervals != null && cvLIBreakupIntervals.size() > 0) {
            /**
             * Sum up all the calculated costs for each breakup interval and
             * then update the line item cal amts.
             */
            int size = 0;
            int index = 0;
            
            CoeusVector cvCombinedAmtDetails = new CoeusVector();
            BudgetPersonnelCalAmountsBean personnelCalAmtsBean;
            
            //Loop and add all the amount details from all the breakup intervals
            size = cvLIBreakupIntervals.size();
            cvPersonnelRateAndBase = new CoeusVector();
            int rateNumber = 1;
            for (index = 0; index < size; index++) {
                BreakUpInterval interval= (BreakUpInterval) cvLIBreakupIntervals.get(index);
                Boundary boundary = interval.getBoundary();
                java.sql.Date startDate = new java.sql.Date(boundary.getStartDate().getTime());
                java.sql.Date endDate = new java.sql.Date(boundary.getEndDate().getTime());
                double salaryRequested = interval.getApplicableAmt();
                //Getting Base Cost Sharing amount for each interval
                double applicableCostSharing = interval.getApplicableAmtCostSharing();
                CoeusVector cvAmountDetails =  interval.getCvAmountDetails();
                if( cvAmountDetails !=null && cvAmountDetails.size()>0){
                    for(int amtIndex = 0; amtIndex < cvAmountDetails.size(); amtIndex++){
                        BudgetPersonnelDetailsRateBaseBean budgetPerDetRateBaseBean = new BudgetPersonnelDetailsRateBaseBean();
                        AmountBean amountBean = (AmountBean) cvAmountDetails.get(amtIndex);
                        if( budgetPersonnelDetails !=null ){
                            budgetPerDetRateBaseBean.setPersonNumber(budgetPersonnelDetails.getPersonNumber());
                            budgetPerDetRateBaseBean.setBudgetPeriod(budgetPersonnelDetails.getBudgetPeriod());
                            budgetPerDetRateBaseBean.setPersonId(budgetPersonnelDetails.getPersonId());
                            budgetPerDetRateBaseBean.setAppliedRate(amountBean.getAppliedRate());
                            budgetPerDetRateBaseBean.setBaseCost(amountBean.getBaseAmount());
                            budgetPerDetRateBaseBean.setCalculatedCost(amountBean.getCalculatedCost());
                            budgetPerDetRateBaseBean.setCalculatedCostSharing(amountBean.getCalculatedCostSharing());
                            budgetPerDetRateBaseBean.setStartDate(startDate);
                            budgetPerDetRateBaseBean.setEndDate(endDate);
                            budgetPerDetRateBaseBean.setLineItemNumber(budgetPersonnelDetails.getLineItemNumber());
                            budgetPerDetRateBaseBean.setJobCode(budgetPersonnelDetails.getJobCode());
                            budgetPerDetRateBaseBean.setRateClassCode(amountBean.getRateClassCode());
                            budgetPerDetRateBaseBean.setRateTypeCode(amountBean.getRateTypeCode());
                            budgetPerDetRateBaseBean.setRateNumber(rateNumber++);
                            budgetPerDetRateBaseBean.setProposalNumber(budgetPersonnelDetails.getProposalNumber());
                            budgetPerDetRateBaseBean.setVersionNumber(budgetPersonnelDetails.getVersionNumber());
                            budgetPerDetRateBaseBean.setBudgetPeriod(budgetPersonnelDetails.getBudgetPeriod());
                            budgetPerDetRateBaseBean.setAcType(TypeConstants.INSERT_RECORD);
                            budgetPerDetRateBaseBean.setOnOffCampusFlag(budgetPersonnelDetails.isOnOffCampusFlag());
                            //Setting Base Cost Sharing amount for each interval
                            budgetPerDetRateBaseBean.setBaseCostSharing(applicableCostSharing);
                            budgetPerDetRateBaseBean.setSalaryRequested(salaryRequested);
                        }
                        
                        //If Rate Class Code is 10 and Rate Type Code = 1 (Lab Allocation -Salaries), set the Job Code as 'LA'
                        if (budgetPerDetRateBaseBean.getRateClassCode() == 10 && budgetPerDetRateBaseBean.getRateTypeCode() == 1 ){
//                            budgetPersonnelDetailsRateBaseBean.setPersonId(PERSON_ID);
                            budgetPerDetRateBaseBean.setJobCode("LA");
//                            budgetPersonnelDetailsRateBaseBean.setPersonNumber(2);
                        }
                         //If Rate Class Code is 5 and Rate Type Code = 3 (Employee Benefit), set the Job Code as 'ELA'
                        if((budgetPerDetRateBaseBean.getRateClassCode() == 5 && budgetPerDetRateBaseBean.getRateTypeCode() == 3)){
                            budgetPerDetRateBaseBean.setJobCode("ELA");
                        }//If Rate Class Code is 8 and Rate Type Code = 2 (Vacation), set the Job Code as 'VLA'
                        else if((budgetPerDetRateBaseBean.getRateClassCode() == 8 && budgetPerDetRateBaseBean.getRateTypeCode() == 2)){
                            budgetPerDetRateBaseBean.setJobCode("VLA");
                        }                        
                        cvPersonnelRateAndBase.add(budgetPerDetRateBaseBean);                        
                    }                   
                }
            }
        }
    }
    // Added for Case 2228 - Print Budget Summary -End 
 } // end PersonnelLineItemCalculator



