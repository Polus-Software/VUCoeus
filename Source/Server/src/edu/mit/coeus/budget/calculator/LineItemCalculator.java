/*
 * @(#)LineItemCalculator.java October 14, 2003, 12:58 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.budget.calculator;

import edu.mit.coeus.budget.calculator.bean.*;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;

import java.util.*;
import java.text.SimpleDateFormat;

/**
 * Used to calculate the line item rates by breaking into smaller periods 
 * according to the rate changes, calculating for each breakup period, 
 * adding up to get the rates for the whole line item period.
 *
 * @author  Sagin
 * @version 1.0
 * Created on October 14, 2003, 12:58 PM
 *
 */
public class LineItemCalculator {

  ///////////////////////////////////////
  // attributes


    /**
     * Represents the Budget Line Item Details.
     */
    protected BudgetDetailBean budgetDetailBean; 

    /**
     * Represents all the Line Item Calculated Amounts
     */
    protected CoeusVector cvLineItemCalcAmts; 

    /**
     * Represents Vector of BreakupInterval objects
     */
    protected CoeusVector cvLIBreakupIntervals; 

    /**
     * Represents vector of Proposal Rates applicable for the line item
     */
    private CoeusVector cvLineItemPropRates; 

    /**
     * Represents vector of Proposal LA Rates applicable for the line item
     */
    private CoeusVector cvLineItemPropLARates; 

    /**
     * Represents the line item cost applicable for a day
     */
    private double perDayCost = 0; 

    /**
     * Represents the line item cost sharing applicable for a day
     */
    private double perDayCostSharing = 0; 

    /**
     * Represents the line item cost sharing applicable for a day
     */
    protected QueryEngine queryEngine = QueryEngine.getInstance(); 

    /**
     * Represents the order of calculation of different rate class types
     */
    private FormulaMaker formulaMaker; 

    /**
     * Represents Vector containing rates not available messages
     */
    protected Vector vecMessages; 
    
    /**
     * Used for formatting date to "MMM-dd-yyyy"
     */
    private DateUtils dateUtils = new DateUtils();
    
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
     * Flag which indicates whether to get the cal amounts from Query Engine or
     * whether it is already set for calculation.
     */
    private boolean getCalAmtFromQE = true;
    
    /**
     * Enhancement ID : 709 Case 3
     * Flag which indicates whether the under-recovery rate base is present for 
     * the line item.
     */
    private boolean uRRateValidForCE = true;
    // Added for the case Id #1811
    private CoeusVector cvRateBaseData;
    // Added for Case 2228 - Print Budget Summary -Start
    private CoeusVector cvPersonnelRateAndBase = null;
   // Added for Case 2228 - Print Budget Summary -End
  ///////////////////////////////////////
  // operations

    /**
     * Constructor...
     */
    public  LineItemCalculator() {
        vecMessages = new Vector();
        initCalcTypes();
    } // end LineItemCalculator        

    /**
     * Initializes the calculation order of different rates in 
     * line item cal amts.
     * 
     */
    public void initCalcTypes() {
        try {
            formulaMaker = new FormulaMaker(); 
        } catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    } // end initCalcTypes       

    /**
     * Starts the calculation of the line item and sets the calculated rates in the 
     * line item cal amts.
     * Accepts BudgetDetailBean which contains the line item details
     * 
     * @param budgetDetailBean 
     */
    public void calculate(BudgetDetailBean budgetDetailBean) {  
        
        //Initialize values
        this.budgetDetailBean = budgetDetailBean;  
        
        //Bug Fix : 732 - Starts here
        budgetDetailBean.setDirectCost(budgetDetailBean.getLineItemCost());
        budgetDetailBean.setIndirectCost(0);
        //Bug Fix : 732 - Ends here
        
        // Enhancement ID : 709 Case 3 - Starts Here
        boolean OHAvailable = true;
        budgetDetailBean.setUnderRecoveryAmount(0);
        // Enhancement ID : 709 Case 3 - Ends Here
        
        if (budgetDetailBean != null) {
            try {
                initValues();
                if (cvLineItemCalcAmts != null && cvLineItemCalcAmts.size() > 0) {
                    filterRates();
                    createBreakupIntervals();
                    calculateBreakupIntervals();
                    updateLIDetailCostsAndCalAmts();
                    // Added for Case 2228 - Print Budget Summary -Start
                    if(budgetDetailBean.getCategoryType() == 'P'){
                        updatePersonnelLIDetailCostsAndRateBase();
                    }
                    // Added for Case 2228 - Print Budget Summary -End
                    //updateDataCollection(); Commented for Bug Fix : 732
                    
                    
                    // Enhancement ID : 709 Case 3 - Starts Here
                    if (!uRMatchesOh) {
                        //Check whether any OH Rate is present
                        Equals eqRateClassType = new Equals("rateClassType", 
                                                RateClassTypeConstants.OVERHEAD);
                        CoeusVector cvOHRate = cvLineItemCalcAmts.filter(eqRateClassType);
                        if (cvOHRate == null || cvOHRate.size() == 0) {
                            OHAvailable = false;
                        }
                    }
                    // Enhancement ID : 709 Case 3 - Ends Here
                            
                }
                 //Modified by Geo on 21-Jul-2005 for the bug fix #1228
                //If total cost sharing value is less than cost sharing value,
                //update total cost sharing with the cost sharing.
                else{
                    budgetDetailBean.setTotalCostSharing(
                            budgetDetailBean.getCostSharingAmount());
                }
                //END FIX
                
                updateDataCollection(); //Bug Fix : 732
                
                // Enhancement ID : 709 Case 3 - Starts Here
                if (!uRMatchesOh && (!OHAvailable || cvLineItemCalcAmts == null ||
                    cvLineItemCalcAmts.size() == 0) ) {
                    calculateURBase(); 
                }    
                //Enhancement ID : 709 Case 3 - Ends Here
                
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    } // end Calculate          

    /**
     * Will use Queryengine to get and set all the required values for line item 
     * calculation.
     */
    public void initValues() throws CoeusException{
        
        //set per day cost & per day cost sharing
        setPerDayCost();
        setPerDayCostSharing();
        
        String key = budgetDetailBean.getProposalNumber() + 
                                budgetDetailBean.getVersionNumber();
        cvLineItemPropLARates = new CoeusVector();
        cvLineItemPropRates = new CoeusVector();
        
        //get the on-off campus flag
        boolean onOffCampusFlag = budgetDetailBean.isOnOffCampusFlag();
        Equals equalsOnOff = new Equals("onOffCampusFlag", onOffCampusFlag);
        //System.out.println("onOffCampusFlag >> " + onOffCampusFlag);
        
        //get the line item cal amts
        Equals equalsPeriod = new Equals("budgetPeriod", 
                                    new Integer(budgetDetailBean.getBudgetPeriod()));
        Equals equalsLineItemNo = new Equals("lineItemNumber", 
                                    new Integer(budgetDetailBean.getLineItemNumber()));
        And periodANDLineItemNo = new And(equalsPeriod, equalsLineItemNo);
        
        //Check whether to get cal amounts from Query Engine
        if (getCalAmtFromQE) {
            cvLineItemCalcAmts = queryEngine.getActiveData(key, 
                            BudgetDetailCalAmountsBean.class, periodANDLineItemNo);
        } else {
            setGetCalAmtFromQE(true);
        }
        //System.out.println("cvLineItemCalcAmts.size() >> " + cvLineItemCalcAmts.size());
        
        /**
         * Get all rates from Proposal Rates & Proposal LA Rates which matches 
         * with the rates in line item cal amts
         */
        if (cvLineItemCalcAmts != null && cvLineItemCalcAmts.size() > 0) {
            int calAmtSize = cvLineItemCalcAmts.size();
            BudgetDetailCalAmountsBean calAmtsBean;
            int rateClassCode, rateTypeCode = 0;
            String rateClassType = "";
            Equals equalsRC;
            Equals equalsRT;
            And RCandRT;
            And RCRTandOnOff;
            
            for(int index=0; index < calAmtSize; index++) {
                CoeusVector ratesVector = new CoeusVector();
                calAmtsBean = (BudgetDetailCalAmountsBean) cvLineItemCalcAmts.get(index);
                rateClassCode = calAmtsBean.getRateClassCode();
                rateTypeCode = calAmtsBean.getRateTypeCode();
                rateClassType = calAmtsBean.getRateClassType();
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
        
        //Filter out all rates that lies outside the line item end date
        Date lineItemEndDate = budgetDetailBean.getLineItemEndDate();
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
    protected void createBreakupIntervals() {        

        //Initialize the Message that should be shown if rate not avalilable for any period
        String messageTemplate = "";
        String multipleRatesMesgTemplate = "";
        String message = "";
        cvLIBreakupIntervals = new CoeusVector();
        if (budgetDetailBean.isOnOffCampusFlag()) {
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
        Date liStartDate = budgetDetailBean.getLineItemStartDate();
        Date liEndDate = budgetDetailBean.getLineItemEndDate();
        Vector vecBoundaries = createBreakupBoundaries(cvCombinedRates, liStartDate,
                                                            liEndDate);
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
                //Case Id #1811 -step1 - Start
                breakUpInterval.setProposalNumber(budgetDetailBean.getProposalNumber());
                breakUpInterval.setVersionNumber(budgetDetailBean.getVersionNumber());
                breakUpInterval.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
                breakUpInterval.setLineItemNumber(budgetDetailBean.getLineItemNumber());
                //Case Id #1811 -step1 - End
                CoeusVector cvAmountDetails = new CoeusVector();
                CoeusVector cvBreakupPropRates = new CoeusVector();
                CoeusVector cvBreakupPropLARates = new CoeusVector();
                CoeusVector cvTempRates = new CoeusVector();
                CoeusVector cvMultipleRates = new CoeusVector();
                String rateClassType = "";
                int rateClassCode = 0;
                int rateTypeCode = 0;
                boolean applyRateFlag;
                long noOfDays = 0;
                
                // find the no. of days in this small period
                noOfDays = boundary.getDateDifference();

                // find the applicable amount
                breakUpInterval.setApplicableAmt(noOfDays * perDayCost);
                breakUpInterval.setApplicableAmtCostSharing(noOfDays * perDayCostSharing);
                //COEUS-702: Budget issue calculating UR with "FUNSN"
                breakUpInterval.setURMatchesOh(isURMatchesOh());
                //COEUS-702: Budget issue calculating UR with "FUNSN"
                //Loop and add all data required in breakup interval
                int calAmtsSize = cvLineItemCalcAmts.size();
                BudgetDetailCalAmountsBean calAmtsBean;
                for (int calAmtIndex = 0; calAmtIndex < calAmtsSize; calAmtIndex++) {
                    calAmtsBean = (BudgetDetailCalAmountsBean) cvLineItemCalcAmts.get(calAmtIndex);
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
                        // Commenetd to fix 1091 - Sagin/Chandra - 13-Aug End
                    
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
     * Use the combined & sorted Prop & LA rates to create Boundary beans. Each 
     * Boundary bean will contain start date & end date. Check whether any rate changes,
     * and break at this point to create a new boundary.
     *
     * @return CoeusVector vector of boundary beans
     */
    public static Vector createBreakupBoundaries(CoeusVector cvCombinedRates, Date liStartDate, 
                                                Date liEndDate) {        
        
        Vector vecBoundaries = new Vector();
        if ( cvCombinedRates != null && cvCombinedRates.size() > 0) {
            Date tempStartDate = liStartDate;
            Date tempEndDate = liEndDate;
            Date rateChangeDate;
            ProposalLARatesBean ratesBean;
            
            //take all rates greater than start date
            GreaterThan greaterThan = new GreaterThan("startDate", liStartDate);
            cvCombinedRates = cvCombinedRates.filter(greaterThan);

            //sort asc
            cvCombinedRates.sort("startDate", true);

            int size = cvCombinedRates.size();
            for (int index = 0; index < size; index++) {
                ratesBean = (ProposalLARatesBean) cvCombinedRates.get(index);
                rateChangeDate = ratesBean.getStartDate();
            //System.out.println("ratesBean.getStartDate() >> " + ratesBean.getStartDate());
                if (rateChangeDate.compareTo(tempStartDate) > 0) {
                    // rate changed so get the previous day date and use it as endDate
                    tempEndDate = new Date(rateChangeDate.getTime() - 86400000);
                    Boundary boundary = new Boundary(tempStartDate, tempEndDate);
                    vecBoundaries.add(boundary);
                    tempStartDate = rateChangeDate;
                }

            }
            /**
             *add one more boundary if no rate change on endDate and atleast
             *one boundary is present
             */
            if ( vecBoundaries.size() > 0) {
                Boundary boundary = new Boundary(tempStartDate, liEndDate);
                vecBoundaries.add(boundary);
            }

            /**
             *if no rate changes during the period create one boundary with
             *startDate & endDate same as that for line item
             */
            if( vecBoundaries.size() == 0) {
                Boundary boundary = new Boundary(liStartDate, liEndDate);
                vecBoundaries.add(boundary);
            }
            
        }
        
        return vecBoundaries;
    } // end createBreakupBoundaries      

    /**
     * calculate the cost for each beakup interval.
     */
    protected void calculateBreakupIntervals() {
        int rateNumber = 0;
        String key = budgetDetailBean.getProposalNumber()+budgetDetailBean.getVersionNumber();
        CoeusVector cvRateBase = null;
        
        if (cvLIBreakupIntervals != null && cvLIBreakupIntervals.size() > 0) {
            int size = cvLIBreakupIntervals.size();
            BreakUpInterval breakUpInterval;
            //case Id #1811-step2
            cvRateBaseData = new CoeusVector();
            for (int index = 0; index < size; index++) {
                breakUpInterval = (BreakUpInterval) cvLIBreakupIntervals.get(index);
                breakUpInterval.setRateNumber(rateNumber);
               
                //Added for COEUSQA-2393 Revamp Coeus Budget Engine -start
                
                // This method is to Calculate Rates for the LineItem based on the calculation methology defined in the
                // Inclusion and Exclusion table for each breakup interval.
                try {
                    
                    breakUpInterval.calcBreakUpIntervalForInclAndExcl();
                } catch (Exception ex) {
                    UtilFactory.log( ex.getMessage() , ex , "LineItemCalculator" , "calculateBreakupIntervals()" );
                }
                
                //COMMENTED OLD METHOD : This method is calculation logic was hardcoded.
//                breakUpInterval.calculateBreakupInterval(formulaMaker);
                
                //Added for COEUSQA-2393 Revamp Coeus Budget Engine -end
                
                //case Id #1811 - step3-start
                if(breakUpInterval.getRateBase()!= null && breakUpInterval.getRateBase().size() >0){
                    
                    //Added for COEUSQA-2889 PDF Print>Budget Summary by Period Always Prints ON CAMPUS - Start
                    // Take all the Rate and base values for the line item from breakup interval, set the correct campus flag value.
                    for(Object objRateAndBase : breakUpInterval.getRateBase()){                        
                        BudgetRateBaseBean budgetRateAndBaseBean = (BudgetRateBaseBean) objRateAndBase;
                        budgetRateAndBaseBean.setOnOffCampusFlag(budgetDetailBean.isOnOffCampusFlag());
                    }
                    //COEUSQA-2889 -End

                    cvRateBaseData.addAll(breakUpInterval.getRateBase());
                    rateNumber = rateNumber+ breakUpInterval.getCvAmountDetails().size();
                }
                //case Id #1811-step3-end

            }
            //case Id #1811-step4 -start
            try{
                    cvRateBase = queryEngine.getDetails(key,BudgetRateBaseBean.class);
                    cvRateBase.addAll(cvRateBaseData);
                    queryEngine.addCollection(key,BudgetRateBaseBean.class,cvRateBase);
            }catch (edu.mit.coeus.exception.CoeusException exception){
                exception.printStackTrace();
            }//case Id #1811 - step4- End
        }
        //System.out.println("Lien Item Calculation over");
    } // end calculateBreakupIntervals
    
    /** @return CoeusVector
     *retrun the ratebasedata of the budget rate and base
     *case Id #1811
     */
    public CoeusVector getRateBaseData(){
        return cvRateBaseData;
    }

    /**
     * - Sum up all the calculated costs for each breakup interval and then update the 
     * line item cal amts.
     * - Sum up all the underRecovery costs for each breakup interval and then update the 
     * line item details.
     * - Sum up all direct costs ie, rates for RateClassType <> 'O', for each breakup interval 
     * and then update the line item details.
     */
    private void updateLIDetailCostsAndCalAmts() {        
        if (cvLineItemCalcAmts != null && cvLineItemCalcAmts.size() > 0 
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
            And RCandRT = null;
            CoeusVector cvCombinedAmtDetails = new CoeusVector();
            BudgetDetailCalAmountsBean calAmtsBean;
            
            //Loop and add all the amount details from all the breakup intervals
            size = cvLIBreakupIntervals.size();
            for (index = 0; index < size; index++) {
                cvCombinedAmtDetails.addAll( ((BreakUpInterval) cvLIBreakupIntervals.get(index)).getCvAmountDetails());
            }
            
            //loop thru all cal amount rates, sum up the costs and set it
            size = cvLineItemCalcAmts.size();
            for (index = 0; index < size; index++) {
                calAmtsBean = (BudgetDetailCalAmountsBean) cvLineItemCalcAmts.get(index);
                
                //if this rate need not be applied skip
                if (!calAmtsBean.isApplyRateFlag()) {
                    continue;
                }
                
                rateClassCode = calAmtsBean.getRateClassCode();
                rateTypeCode = calAmtsBean.getRateTypeCode();
                equalsRC = new Equals("rateClassCode", new Integer(rateClassCode));
                equalsRT = new Equals("rateTypeCode", new Integer(rateTypeCode));
                RCandRT = new And(equalsRC, equalsRT);
                
               // totalCalculatedCost = cvCombinedAmtDetails.sum("calculatedCost", RCandRT);
                // Bgu fix #1835 -start
                  totalCalculatedCost = ((double)Math.round(cvCombinedAmtDetails.sum("calculatedCost", RCandRT)*
                        Math.pow(10.0, 2) )) / 100;
                // Bgu fix #1835 -End
                
                calAmtsBean.setCalculatedCost(totalCalculatedCost);
                
                totalCalculatedCostSharing = cvCombinedAmtDetails.sum("calculatedCostSharing", RCandRT);
                calAmtsBean.setCalculatedCostSharing(totalCalculatedCostSharing);
            }
            
            /**
             * Sum up all the underRecovery costs for each breakup interval and then update the 
             * line item details.
             */       
            totalUnderRecovery = cvLIBreakupIntervals.sum("underRecovery");
            budgetDetailBean.setUnderRecoveryAmount(totalUnderRecovery);
            
            /**
             * Sum up all direct costs ie, rates for RateClassType <> 'O', for each breakup interval
             * plus the line item cost, and then update the line item details.
             */
            NotEquals notEqualsOH = new NotEquals("rateClassType", RateClassTypeConstants.OVERHEAD);
            
            //directCost = cvCombinedAmtDetails.sum("calculatedCost", notEqualsOH);
            // Bgu fix #1835 -start
            directCost = ((double)Math.round(cvCombinedAmtDetails.sum("calculatedCost", notEqualsOH)*
                        Math.pow(10.0, 2) )) / 100;
            // Bgu fix #1835 -End
            
            budgetDetailBean.setDirectCost(directCost + budgetDetailBean.getLineItemCost());
           
            // System.out.println("The Direct cost is : "+budgetDetailBean.getDirectCost());
            
            /**
             * Sum up all Indirect costs ie, rates for RateClassType = 'O', for each breakup interval 
             * and then update the line item details.
             */
            Equals equalsOH = new Equals("rateClassType", RateClassTypeConstants.OVERHEAD);
            
//            indirectCost = cvCombinedAmtDetails.sum("calculatedCost", equalsOH);
            // Bgu fix #1835 -start
            indirectCost = ((double)Math.round(cvCombinedAmtDetails.sum("calculatedCost", equalsOH)*
                        Math.pow(10.0, 2) )) / 100;
            // Bgu fix #1835 -End
//            
            
            //System.out.println("The Indirect Cost is : "+infdirectCost);
            budgetDetailBean.setIndirectCost(indirectCost);
            
            /**
             * Sum up all Cost Sharing amounts ie, rates for RateClassType <> 'O' and set 
             * in the calculatedCostSharing field of line item details
             */
            totalCalculatedCostSharing = cvCombinedAmtDetails.sum("calculatedCostSharing");

            budgetDetailBean.setTotalCostSharing(budgetDetailBean.getCostSharingAmount() + 
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
        CoeusVector cvValidOHRatesForCE = budgetDataTxnBean.getValidOHRateTypesForCE(budgetDetailBean.getCostElement());
        
        //Filter and get the OH rate corresponding to the UR Rate Base
        Equals equalsRC = new Equals("rateClassCode", new Integer(uRRateClassCode));
        Equals equalsRT = new Equals("rateTypeCode", new Integer(uRRateTypeCode));
        And RCandRT = new And(equalsRC, equalsRT);
        
        CoeusVector cvURRate = cvValidOHRatesForCE.filter(RCandRT);
        
        //If the UR rate available for the cost element, then calculate under-recovery
        if (cvValidOHRatesForCE != null && cvValidOHRatesForCE.size() > 0) {
            //Filter and get all rates for the UR Rate and which lies before line item end date
            boolean onOffCampusFlag = budgetDetailBean.isOnOffCampusFlag();
            Equals equalsOnOff = new Equals("onOffCampusFlag", onOffCampusFlag);
            And RCRTandOnOff = new And(RCandRT, equalsOnOff);
            
            Date lineItemEndDate = budgetDetailBean.getLineItemEndDate();
            LesserThan leStartDate = new LesserThan("startDate", lineItemEndDate);
            Equals eqStartDate = new Equals("startDate", lineItemEndDate);
            Or lesserThanOrEqStDate = new Or(leStartDate, eqStartDate);
            And RCRTandOnOffAndLeOrEqStDate = new And(RCRTandOnOff, lesserThanOrEqStDate);
            String key = budgetDetailBean.getProposalNumber() + 
                                budgetDetailBean.getVersionNumber();
            cvLineItemPropRates = queryEngine.getActiveData(key, 
                        ProposalRatesBean.class, RCRTandOnOffAndLeOrEqStDate);
            cvLineItemPropLARates = new CoeusVector();
            
            //Create a new Cal Amount Bean with this UR Rate for doing calculation
            BudgetDetailCalAmountsBean calAmountsBean = new BudgetDetailCalAmountsBean();
            calAmountsBean.setApplyRateFlag(true);
            calAmountsBean.setRateClassType(RateClassTypeConstants.OVERHEAD);
            calAmountsBean.setRateClassCode(uRRateClassCode);
            calAmountsBean.setRateTypeCode(uRRateTypeCode);
            calAmountsBean.setCalculatedCost(0);
            calAmountsBean.setCalculatedCostSharing(0);
            
            cvLineItemCalcAmts.clear();
            cvLineItemCalcAmts.add(calAmountsBean);
            
            //Calculate the rate for this UR Base
            createBreakupIntervals();
            calculateBreakupIntervals();
            
            /**
             * Sum up all the calculated costs for each breakup interval and then update the 
             * line item detail underrecovery.
             */
            //Added for COEUSQA-3588 CLONE - Budget produces erroneous UR calculation - Start
            // If Cost Element has OH rates corresponding to the UR Rate Base in cvURRate, then calculate the underrecovery amount            
            if (cvLIBreakupIntervals != null && cvLIBreakupIntervals.size() > 0 && !cvURRate.isEmpty()) {
                CoeusVector cvCombinedAmtDetails = new CoeusVector();
                
                //Loop and add all the amount details from all the breakup intervals
                int size = cvLIBreakupIntervals.size();
                for (int index = 0; index < size; index++) {
                    cvCombinedAmtDetails.addAll( ((BreakUpInterval) cvLIBreakupIntervals.get(index)).getCvAmountDetails());
                }
                budgetDetailBean.setUnderRecoveryAmount(cvCombinedAmtDetails.sum("calculatedCost"));
            //COEUSQA-3588 -End
                //Added for COEUSQA-3588 CLONE - Budget produces erroneous UR calculation -start
                if(cvLineItemCalcAmts != null){
                    cvLineItemCalcAmts.clear();
                }
                //COEUSQA-3588 - end
                //Added for Case COEUSDEV-302 - Error in saving budget
                // Error saving budget when personnel cost element has EB but no OH -start
                // If Overhead is not available, this updateDataCollection() method will calculate Underrecovery Base and 
                // all the data will be update using updateDataCollection(). This updateDataCollection() was called before calling calculateURBase(), 
                // CoeusVector of cvPersonnelRateAndBase will be available in instance level,  
                // same collection is updating twice, because of that clearing the collection.
               //Added for COEUSQA-3588 CLONE - Budget produces erroneous UR calculation -start
                if(cvPersonnelRateAndBase != null){
                    cvPersonnelRateAndBase.clear();
                }   
                //COEUSQA-3588 - end
                //Added for Case COEUSDEV-302 -End

                //Update to queryEngine
                updateDataCollection();
            }
            
            
        }
    }
    //Enhancement ID : 709 Case 3 - Ends here

    /**
     * Updates BudgetDetailBean & vector of BudgetCalAmtBeans to the 
     * data collection (hash table) in the query engine.
     */
    private void updateDataCollection() throws CoeusException {    
        String key = budgetDetailBean.getProposalNumber() + budgetDetailBean.getVersionNumber();
        
        //Update BudgetDetailBean
        if (budgetDetailBean.getAcType() == null) {
            budgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
        }
        queryEngine.update(key, budgetDetailBean);
        
        //Update Cal Amt beans
        if (cvLineItemCalcAmts != null && cvLineItemCalcAmts.size() > 0) {
            int size = cvLineItemCalcAmts.size();
            BudgetDetailCalAmountsBean calAmtsBean;
            for (int index = 0; index < size; index++) {
                calAmtsBean = (BudgetDetailCalAmountsBean) cvLineItemCalcAmts.get(index);
                if (calAmtsBean.getAcType() == null) {
                    calAmtsBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
                queryEngine.update(key, calAmtsBean);
            }
        }
        // Added for Case 2228 - Print Budget Summary -Start
         BudgetPersonnelDetailsRateBaseBean budgetPerDetRateBaseBean;
        if(cvPersonnelRateAndBase !=null && cvPersonnelRateAndBase.size() > 0 && budgetDetailBean.getCategoryType() == 'P'){
             for(int rateIndex = 0; rateIndex < cvPersonnelRateAndBase.size(); rateIndex++ ){
                budgetPerDetRateBaseBean =  (BudgetPersonnelDetailsRateBaseBean) cvPersonnelRateAndBase.get(rateIndex);
                budgetPerDetRateBaseBean.setAcType(TypeConstants.INSERT_RECORD);
//                queryEngine.update(key, budgetPerDetRateBaseBean);
             }
            CoeusVector cvRateBase = queryEngine.getDetails(key,BudgetPersonnelDetailsRateBaseBean.class);
             cvRateBase.addAll(cvPersonnelRateAndBase);
              queryEngine.addCollection(key,BudgetPersonnelDetailsRateBaseBean.class,cvRateBase);
              //Added for COEUSQA-3080 Could not save budget details" error -start
              //Once person Rate and base data collection is added to query engine and need to clear the collection,
              //otherwise if personnel line item does not have Rate and base, previous rate and base will be added for the new line item
              // in this case, duplicates will be added and getting unique constraint error.
              cvPersonnelRateAndBase.clear();
              //Added for COEUSQA-3080 -end
        }
         // Added for Case 2228 - Print Budget Summary -End
        //System.out.println("Update Data Collection over");
    } // end updateDataCollection   

    /**
     * set the per day cost by dividing the line item cost with the total
     * no. of days in the line item period
     */
    private void setPerDayCost() {        
        if (budgetDetailBean != null) {
            double lineItemCost = budgetDetailBean.getLineItemCost();
            Date liStartDate = budgetDetailBean.getLineItemStartDate();
            Date liEndDate = budgetDetailBean.getLineItemEndDate();
            perDayCost = lineItemCost/((liEndDate.getTime() - 
                                        liStartDate.getTime())/86400000 + 1);
            
//            perDayCost = (double)Math.round (perDayCost*Math.pow(10.0, 2) ) / 100; 
        }
    } // end setPerDayCost        

    /**
     * set the per day cost sharing by dividing the line item cost sharing 
     * with the total no. of days in the line item period
     */
    private void setPerDayCostSharing() {         
        if (budgetDetailBean != null) {
            double lineItemCostSharing = budgetDetailBean.getCostSharingAmount();
            Date liStartDate = budgetDetailBean.getLineItemStartDate();
            Date liEndDate = budgetDetailBean.getLineItemEndDate();
            perDayCostSharing = lineItemCostSharing/((liEndDate.getTime() - 
                                            liStartDate.getTime())/86400000 + 1);
            
        }
    } // end setPerDayCostSharing    
    
    /** Getter for property budgetDetailBean.
     * @return Value of property budgetDetailBean.
     */
    public BudgetDetailBean getBudgetDetailBean() {
        return budgetDetailBean;
    }
    
    /** Setter for property budgetDetailBean.
     * @param budgetDetailBean New value of property budgetDetailBean.
     */
    public void setBudgetDetailBean(BudgetDetailBean budgetDetailBean) {
        this.budgetDetailBean = budgetDetailBean;
    }
    
    /** Getter for property cvLineItemCalcAmts.
     * @return Value of property cvLineItemCalcAmts.
     */
    public CoeusVector getCvLineItemCalcAmts() {
        return cvLineItemCalcAmts;
    }
    
    /** Setter for property cvLineItemCalcAmts.
     * @param cvLineItemCalcAmts New value of property cvLineItemCalcAmts.
     */
    public void setCvLineItemCalcAmts(CoeusVector cvLineItemCalcAmts) {
        this.cvLineItemCalcAmts = cvLineItemCalcAmts;
    }
    
     
    /*public static void main(String s[]) {
        QueryEngine queryEngine = QueryEngine.getInstance();
        TestCalculator test = new TestCalculator();
        //Hashtable budgetData = test.getAllBudgetData("01100555", 10);
        //String key = "01100555" + 10;
        Hashtable budgetData = test.getAllBudgetData("01100693", 1);
        String key = "01100693" + 1;
        try {
            queryEngine.addDataCollection(key, budgetData);
            //System.out.println("before");
            CoeusVector cvLineItemDetails = queryEngine.getDetails(key,BudgetDetailBean.class);
            //System.out.println("cvLineItemDetails.size() - >> " + cvLineItemDetails.size());
            //BudgetDetailBean budgetDetailBean = (BudgetDetailBean) cvLineItemDetails.get(0);
            //budgetDetailBean.setLineItemCost(300000);
            //budgetDetailBean.setCostSharingAmount(150000);
            LineItemCalculator lineItemCalculator = new LineItemCalculator();
            lineItemCalculator.calculate( (BudgetDetailBean) cvLineItemDetails.get(0) );
            //System.out.println("LineItemCalculator: perDayCost - >> " + lineItemCalculator.perDayCost);
            //System.out.println("LineItemCalculator: perDayCostSharing - >> " + lineItemCalculator.perDayCostSharing);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }*/
    
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
    
    /** Getter for property getCalAmtFromQE.
     * @return Value of property getCalAmtFromQE.
     *
     */
    public boolean isGetCalAmtFromQE() {
        return getCalAmtFromQE;
    }
    
    /** Setter for property getCalAmtFromQE.
     * @param getCalAmtFromQE New value of property getCalAmtFromQE.
     *
     */
    public void setGetCalAmtFromQE(boolean getCalAmtFromQE) {
        this.getCalAmtFromQE = getCalAmtFromQE;
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
    
   // Added for Case 2228 - Print Budget Summary -Start 
    /** 
     * Update the Rate and Base Break Up data for Personnel Line Item with out persons.
     */
    private void updatePersonnelLIDetailCostsAndRateBase() {
        if (cvLineItemCalcAmts != null && cvLineItemCalcAmts.size() > 0
                && cvLIBreakupIntervals != null && cvLIBreakupIntervals.size() > 0) {
            /**
             * Sum up all the calculated costs for each breakup interval and
             * then update the line item cal amts.
             * Set Person Id as '999999999' for Personnel Line Item does not have any person
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
                CoeusVector cvAmountDetails =  interval.getCvAmountDetails();
                if( cvAmountDetails !=null && cvAmountDetails.size()>0){
                    for(int amtIndex = 0; amtIndex < cvAmountDetails.size(); amtIndex++){
                        BudgetPersonnelDetailsRateBaseBean budgetPerDetRateBaseBean = new BudgetPersonnelDetailsRateBaseBean();
                        AmountBean amountBean = (AmountBean) cvAmountDetails.get(amtIndex);
                        if( budgetDetailBean !=null ){
                            budgetPerDetRateBaseBean.setPersonNumber(1);
                            budgetPerDetRateBaseBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
                            budgetPerDetRateBaseBean.setPersonId("999999999");
                            budgetPerDetRateBaseBean.setAppliedRate(amountBean.getAppliedRate());
                            budgetPerDetRateBaseBean.setBaseCost(amountBean.getBaseAmount());
                            budgetPerDetRateBaseBean.setCalculatedCost(amountBean.getCalculatedCost());
                            budgetPerDetRateBaseBean.setCalculatedCostSharing(amountBean.getCalculatedCostSharing());
                            budgetPerDetRateBaseBean.setStartDate(startDate);
                            budgetPerDetRateBaseBean.setEndDate(endDate);
                            budgetPerDetRateBaseBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
//                            budgetPerDetRateBaseBean.setJobCode(budgetDetailBean.getJobCode());
                            budgetPerDetRateBaseBean.setRateClassCode(amountBean.getRateClassCode());
                            budgetPerDetRateBaseBean.setRateTypeCode(amountBean.getRateTypeCode());
                            budgetPerDetRateBaseBean.setRateNumber(rateNumber++);
                            budgetPerDetRateBaseBean.setProposalNumber(budgetDetailBean.getProposalNumber());
                            budgetPerDetRateBaseBean.setVersionNumber(budgetDetailBean.getVersionNumber());
                            budgetPerDetRateBaseBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
                            budgetPerDetRateBaseBean.setAcType(TypeConstants.INSERT_RECORD);
                            budgetPerDetRateBaseBean.setOnOffCampusFlag(budgetDetailBean.isOnOffCampusFlag());
                            budgetPerDetRateBaseBean.setSalaryRequested(salaryRequested);
                        }
                        
                        //If Rate Class Code is 10 and Rate Type Code = 1 (Lab Allocation -Salaries), set the Job Code as 'LA'
                        if (budgetPerDetRateBaseBean.getRateClassCode() == 10 && budgetPerDetRateBaseBean.getRateTypeCode() == 1 ){
                            budgetPerDetRateBaseBean.setJobCode("LA");
                        }
                        //If Rate Class Code is 5 and Rate Type Code = 3 (Employee Benefit), set the Job Code as 'ELA'
                        if((budgetPerDetRateBaseBean.getRateClassCode() == 5 && budgetPerDetRateBaseBean.getRateTypeCode() == 3)){
                            budgetPerDetRateBaseBean.setJobCode("ELA");
                        } //If Rate Class Code is 8 and Rate Type Code = 2 (Vacation), set the Job Code as 'VLA'
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
    
 } // end LineItemCalculator



