/*
 * @(#)BreakUpInterval.java October 14, 2003, 12:58 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.budget.calculator;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.budget.calculator.bean.*;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.budget.calculator.FormulaMaker;
import edu.mit.coeus.budget.calculator.Boundary;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.query.*;
import java.util.*;

/**
 * Holds all the info required for the breakup interval for which calculation
 * has to be performed.
 *
 * @author  Sagin
 * @version 1.0
 * Created on October 14, 2003, 12:58 PM
 *
 */
public class BreakUpInterval implements BaseBean {
    
    ///////////////////////////////////////
    // attributes
    
    
    /**
     * Represents the boundary for the breakup period
     */
    private Boundary boundary;
    
    /**
     * Represents the Under Recovery
     */
    private double underRecovery = 0;
    
    /**
     * Represents all the Proposal Rates
     */
    private CoeusVector cvPropRates;
    
    /**
     * Represents all the Proposal LA Rates
     */
    private CoeusVector cvPropLARates;
    
    /**
     * Represents vector of AmountBean which holds all the calculated amounts for a
     * rate class - rate type for each breakup interval
     */
    private CoeusVector cvAmountDetails;
    
    /**
     * Represents the order of calculation of different rate class types
     */
    private FormulaMaker formulaMaker;
    
    /**
     * Represents the applicable amount(line item cost) for this breakup period
     * based on which all the rates are calculated.
     */
    private double applicableAmt;
    
    /**
     * Represents the applicable amount cost sharing (line item cost sharing)
     * for this breakup period based on which all the rates are calculated.
     */
    private double applicableAmtCostSharing;
    
    /**
     * Used for Under-recovery calculation (enhancement)"
     */
    private ProposalRatesBean uRRatesBean;
    
    private boolean laWithEBVACalculated = false;
    
    /** Added for the case Id #1881-start
     */
    private String proposalNumber;
    private int budgetPeriod;
    private int lineItemNumber;
    private int versionNumber;
    private int rateNumber;
    private CoeusVector cvRateBase ;
    /** Added for the case Id #1881-End
     */
   
    //Added for COEUSQA-2393  Revamp Budget Engine - Start
    
    CoeusVector cvAmoutDetails ;
    CoeusVector cvRatesInHierachy ;
    CoeusVector cvAmoutBeanCollection2 ;
    private static String RATE_CLASS_CODE = "rateClassCode";
    private static String RATE_TYPE_CODE = "rateTypeCode";
    private static String RATE_CLASS_CODE_INCL = "rateClassCodeIncl";
    private static String CALCULATED_COST = "calculatedCost";
    private static String CALCULATED_COST_SHARING = "calculatedCostSharing";
    
    //Added for COEUSQA-2393 - End

    ///////////////////////////////////////
    // operations
    private boolean uRMatchesOh = false; 
    
    /**
     * Calculate all the rates for the breakup interval
     *
     * @param formulaMaker
     * @param perDayCost
     * @param perDayCostSharing
     */
    public void calculateBreakupInterval(FormulaMaker formulaMaker) {
        
        //initialize formula maker & Under Recovery
        this.formulaMaker = formulaMaker;
        underRecovery = 0;
        
        // get the calculation order from formula maker
        CoeusVector cvValidCalcTypes = formulaMaker.getCvValidCalcTypes();
        
        if (cvValidCalcTypes != null && cvValidCalcTypes.size() > 0) {
            int calcTypesSize = cvValidCalcTypes.size();
            ValidCalcTypesBean validCalcTypesBean;
            String rateClassType = "";
            String prevRateClassType = "";
            CoeusVector cvTempAmtDetails;
            CoeusVector cvTempCalcTypes;
            Equals eqRCType;
            int EBonLARateClassCode = 0;
            int EBonLARateTypeCode = 0;
            int VAonLARateClassCode = 0;
            int VAonLARateTypeCode = 0;
            
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
            
            
            /**
             * loop thru and calculate all the rates using the RateClassType order
             * of the cvValidCalcTypes CoeusVector
             */
            for (int calcTypeIndex = 0; calcTypeIndex < calcTypesSize; calcTypeIndex++) {
                AmountBean amountBean;
                int rateClassCode = 0;
                int rateTypeCode = 0;
                double rate = 0;
                double calculatedCost = 0;
                double calculatedCostSharing = 0;
                Equals equalsRC;
                Equals equalsRT;
                And RCandRT;
                CoeusVector cvTempRates;
                ProposalLARatesBean proposalLARatesBean;
                
                validCalcTypesBean = (ValidCalcTypesBean) cvValidCalcTypes.get(calcTypeIndex);
                
                //get the Rate Class Type
                rateClassType = validCalcTypesBean.getRateClassType();
                
                //If this Rate Class Type is already calculated then skip
                if (prevRateClassType.equals("")) {
                    prevRateClassType = rateClassType;
                } else if (rateClassType.equalsIgnoreCase(prevRateClassType)) {
                    continue;
                } else {
                    prevRateClassType = rateClassType;
                }
                
                //get all the matching RateClassTypes from cvAmountDetails
                eqRCType = new Equals("rateClassType", rateClassType);
                cvTempAmtDetails = cvAmountDetails.filter(eqRCType);
                
                /**
                 *if RateClassType = 'E'(Employee Benefits) then remove all EBonLA
                 *since its already calculated alongwith 'Y'
                 */
                if (rateClassType.equals(RateClassTypeConstants.EMPLOYEE_BENEFITS) &&
                        laWithEBVACalculated) {
                    NotEquals notEqualsRC = new NotEquals("rateClassCode", new Integer(EBonLARateClassCode));
                    NotEquals notEqualsRT = new NotEquals("rateTypeCode", new Integer(EBonLARateTypeCode));
                    And notEqRCandnotEqRT = new And(notEqualsRC, notEqualsRT);
                    Or neRCandneRTOrneRT = new Or(notEqRCandnotEqRT, notEqualsRT);
                    cvTempAmtDetails = cvTempAmtDetails.filter(neRCandneRTOrneRT);
                }
                
                /**
                 *if RateClassType = 'V'(Vacation) then remove all VAonLA
                 *since its already calculated alongwith 'Y'
                 */
                if (rateClassType.equals(RateClassTypeConstants.VACATION) &&
                        laWithEBVACalculated) {
                    NotEquals notEqualsRC = new NotEquals("rateClassCode", new Integer(VAonLARateClassCode));
                    NotEquals notEqualsRT = new NotEquals("rateTypeCode", new Integer(VAonLARateTypeCode));
                    And notEqRCandnotEqRT = new And(notEqualsRC, notEqualsRT);
                    Or neRCandneRTOrneRT = new Or(notEqRCandnotEqRT, notEqualsRT);
                    cvTempAmtDetails = cvTempAmtDetails.filter(neRCandneRTOrneRT);
                }
                
                //if no amt details then skip
                if (cvTempAmtDetails.size() == 0) {
                    continue;
                }
                
                //Calculate LA rate classes which get EB and vacation (rateClassType = 'Y')
                if (rateClassType.equals(RateClassTypeConstants.LA_WITH_EB_VA)) {
                    calculateLAWithEBandVA(cvTempAmtDetails, EBonLARateClassCode,
                            EBonLARateTypeCode, VAonLARateClassCode,
                            VAonLARateTypeCode);
                    laWithEBVACalculated = true;
                    continue;
                    
                    //Calculate overhead, (rateClassType = 'O')
                } else if (rateClassType.equals(RateClassTypeConstants.OVERHEAD)) {
                    calculateOHandUnderRecovery(cvTempAmtDetails, EBonLARateClassCode,
                            EBonLARateTypeCode, VAonLARateClassCode,
                            VAonLARateTypeCode);
                    continue;
                }
                
                //calculate all the rates for this RateClassType
                int amtDetailsSize = cvTempAmtDetails.size();
                
                //loop thru each amount detail bean and calculate
                for (int amtDetailsIndex = 0; amtDetailsIndex < amtDetailsSize; amtDetailsIndex++) {
                    amountBean = (AmountBean) cvTempAmtDetails.get(amtDetailsIndex);
                    rateClassCode = amountBean.getRateClassCode();
                    rateTypeCode = amountBean.getRateTypeCode();
                    equalsRC = new Equals("rateClassCode", new Integer(rateClassCode));
                    equalsRT = new Equals("rateTypeCode", new Integer(rateTypeCode));
                    RCandRT = new And(equalsRC, equalsRT);
                    
                    //get the rate applicable for this RateClassCode & RateTypeCode
                    if (rateClassType.equals(RateClassTypeConstants.LAB_ALLOCATION)) {
                        cvTempRates = cvPropLARates.filter(RCandRT);
                    } else {
                        cvTempRates = cvPropRates.filter(RCandRT);
                    }
                    
                    //if rates available calculate amount
                    if (cvTempRates.size() > 0) {
                        proposalLARatesBean = (ProposalLARatesBean) cvTempRates.get(0);
                        rate = proposalLARatesBean.getApplicableRate();
                        
                        rate = (double)Math.round(rate*Math.pow(10.0, 2) ) / 100;
                        //calculate cost & costSharing, set it to AmountBean
                        calculatedCost = (applicableAmt * rate)/100;
                        // Bug fix #1835 -start
                        calculatedCost = (double)Math.round(calculatedCost*Math.pow(10.0, 2) ) / 100;
                        // Bug fix #1835 -End
                        calculatedCostSharing = (applicableAmtCostSharing * rate)/100;
                        //Case Id #1811 start
                        amountBean.setBaseAmount(applicableAmt);
                        //Case Id #1811 End
                        amountBean.setAppliedRate(rate);
                        amountBean.setCalculatedCost(calculatedCost);
                        amountBean.setCalculatedCostSharing(calculatedCostSharing);
                        
                        //rates not available, so set to zero
                    } else {
                        amountBean.setCalculatedCost(0);
                        amountBean.setCalculatedCostSharing(0);
                        //Case Id #1811 start
                        amountBean.setBaseAmount(0);
                        //Case Id #1811 End
                    }
                    //getRateBase(amountBean);
                    
                }
                
            }
            
        }
        
        /** Add Calculated amounts and fetch the data to the BudgetRateBaseBean
         *Hold the breakup intervals data into the dataholder and save to the
         *database as and when calculate is called
         *It is an enhacement. Case Id #1811
         *Start - 10-Aug-2005
         */
        CoeusVector cvData = getCvAmountDetails();
        int rateNum = getRateNumber();
        if(cvData!= null && cvData.size() >0){
            cvRateBase = new CoeusVector();
            for(int dataIndex=0; dataIndex <cvData.size(); dataIndex++){
                BudgetRateBaseBean budgetRateBaseBean = new BudgetRateBaseBean();
                AmountBean amountBean = (AmountBean)cvData.get(dataIndex);
                Boundary boundary = getBoundary();
                boolean flag = amountBean.isApplyRateFlag();
                budgetRateBaseBean.setProposalNumber(getProposalNumber());
                budgetRateBaseBean.setVersionNumber(getVersionNumber());
                budgetRateBaseBean.setBudgetPeriod(getBudgetPeriod());
                budgetRateBaseBean.setRateNumber(++rateNum);
                budgetRateBaseBean.setLineItemNumber(getLineItemNumber());
                budgetRateBaseBean.setStartDate(new java.sql.Date(boundary.getStartDate().getTime()));;
                budgetRateBaseBean.setEndDate(new java.sql.Date(boundary.getEndDate().getTime()));
                budgetRateBaseBean.setRateClassCode(amountBean.getRateClassCode());
                budgetRateBaseBean.setRateTypeCode(amountBean.getRateTypeCode());
                budgetRateBaseBean.setOnOffCampusFlag(flag);
                budgetRateBaseBean.setAppliedRate(amountBean.getAppliedRate());
                budgetRateBaseBean.setBaseCost(amountBean.getBaseAmount());
                budgetRateBaseBean.setCalculatedCost(amountBean.getCalculatedCost());
                budgetRateBaseBean.setBaseCostSharing(getApplicableAmtCostSharing());
                budgetRateBaseBean.setCalculatedCostSharing(amountBean.getCalculatedCostSharing());
                budgetRateBaseBean.setAcType("I");
                cvRateBase.add(budgetRateBaseBean);
            }
        }//End - 10-Aug-2005- Case Id#1811
    }
    
    /**return the vector which contains Budget Rate and Base data
     *Case Id#1811
     */
    public CoeusVector getRateBase() {
        return cvRateBase;
    }
    /**
     * Calculate all the LA having EB & VA, (RateClassType = 'Y')
     * and the EB & VA rates on this LA
     *
     * @param CoeusVector cvLAwithEBAmtDetails
     */
    public void calculateLAWithEBandVA(CoeusVector cvLAwithEBAmtDetails,
            int EBonLARateClassCode, int EBonLARateTypeCode,
            int VAonLARateClassCode, int VAonLARateTypeCode) {
        
        AmountBean amountBean;
        int rateClassCode = 0;
        int rateTypeCode = 0;
        double rate = 0;
        double LAcalculatedCost = 0;
        double LAcalculatedCostSharing = 0;
        double EBonLAcalculatedCost = 0;
        double EBonLAcalculatedCostSharing = 0;
        double VAonLAcalculatedCost = 0;
        double VAonLAcalculatedCostSharing = 0;
        Equals equalsRC;
        Equals equalsRT;
        And RCandRT;
        CoeusVector cvTempRates;
        CoeusVector cvEBonLAAmtDetails;
        CoeusVector cvVAonLAAmtDetails;
        ProposalLARatesBean proposalLARatesBean;
        
        //calculate all the rates for 'Y' RateClassType
        int amtDetailsSize = cvLAwithEBAmtDetails.size();
        
        //loop thru each amount detail bean and calculate
        for (int amtDetailsIndex = 0; amtDetailsIndex < amtDetailsSize; amtDetailsIndex++) {
            amountBean = (AmountBean) cvLAwithEBAmtDetails.get(amtDetailsIndex);
            rateClassCode = amountBean.getRateClassCode();
            rateTypeCode = amountBean.getRateTypeCode();
            equalsRC = new Equals("rateClassCode", new Integer(rateClassCode));
            equalsRT = new Equals("rateTypeCode", new Integer(rateTypeCode));
            RCandRT = new And(equalsRC, equalsRT);
            
            //get the rate applicable for LA RateClassCode & RateTypeCode
            cvTempRates = cvPropLARates.filter(RCandRT);
            
            //if rates available calculate amount
            if (cvTempRates.size() > 0) {
                proposalLARatesBean = (ProposalLARatesBean) cvTempRates.get(0);
                rate = proposalLARatesBean.getApplicableRate();
                
                //calculate cost & costSharing for LA, set it to AmountBean
                LAcalculatedCost = (applicableAmt * rate)/100;
                // Bug fix #1835 -start
                LAcalculatedCost = (double)Math.round(LAcalculatedCost*Math.pow(10.0, 2) ) / 100;
                // Bug fix #1835 -End
                LAcalculatedCostSharing = (applicableAmtCostSharing * rate)/100;
                //Case Id #1811 start
                amountBean.setBaseAmount(applicableAmt);
                //Case Id #1811  End
                amountBean.setAppliedRate(rate);
                amountBean.setCalculatedCost(LAcalculatedCost);
                amountBean.setCalculatedCostSharing(LAcalculatedCostSharing);
                
                //rates not available, so set to zero
            } else {
                amountBean.setCalculatedCost(0);
                amountBean.setCalculatedCostSharing(0);
                //Case Id #1811 - start
                amountBean.setBaseAmount(0);
                //Case Id #1811 -End
            }
            
            /****** Calculate EB on this LA ******/
            //get the EB on LA Amountbean
            equalsRC = new Equals("rateClassCode", new Integer(EBonLARateClassCode));
            equalsRT = new Equals("rateTypeCode", new Integer(EBonLARateTypeCode));
            RCandRT = new And(equalsRC, equalsRT);
            cvEBonLAAmtDetails = cvAmountDetails.filter(RCandRT);
            
            if (cvEBonLAAmtDetails.size() > 0) {
                amountBean = (AmountBean) cvEBonLAAmtDetails.get(0);
                rateClassCode = amountBean.getRateClassCode();
                rateTypeCode = amountBean.getRateTypeCode();
                equalsRC = new Equals("rateClassCode", new Integer(rateClassCode));
                equalsRT = new Equals("rateTypeCode", new Integer(rateTypeCode));
                RCandRT = new And(equalsRC, equalsRT);
                
                //get the rate applicable for EBonLA RateClassCode & RateTypeCode
                cvTempRates = cvPropRates.filter(RCandRT);
                
                //if rates available calculate amount
                if (cvTempRates.size() > 0) {
                    proposalLARatesBean = (ProposalLARatesBean) cvTempRates.get(0);
                    rate = proposalLARatesBean.getApplicableRate();
                    
                    //calculate cost & costSharing for EBonLA, set it to AmountBean
                    EBonLAcalculatedCost = (LAcalculatedCost * rate)/100;
                    // Bug fix #1835 -start
                    EBonLAcalculatedCost = (double)Math.round(EBonLAcalculatedCost*Math.pow(10.0, 2) ) / 100;
                    // Bug fix #1835 -End
                    EBonLAcalculatedCostSharing = (LAcalculatedCostSharing * rate)/100;
                    //Case Id #1811 - start
                    amountBean.setBaseAmount(LAcalculatedCost );
                    //Case Id #1811 -End
                    amountBean.setAppliedRate(rate);
                    amountBean.setCalculatedCost(amountBean.getCalculatedCost() + EBonLAcalculatedCost);
                    amountBean.setCalculatedCostSharing(amountBean.getCalculatedCostSharing() + EBonLAcalculatedCostSharing);
                    
                    //rates not available, so set to zero
                } else {
                    amountBean.setCalculatedCost(0);
                    amountBean.setCalculatedCostSharing(0);
                    //Case Id #1811 - start
                    amountBean.setBaseAmount(0);
                    //Case Id #1811 - End
                }
            }
            /*** Calculation of EB on LA ends here ***/
            
            /****** Calculate VA on this LA ******/
            //get the VA on LA Amountbean
            equalsRC = new Equals("rateClassCode", new Integer(VAonLARateClassCode));
            equalsRT = new Equals("rateTypeCode", new Integer(VAonLARateTypeCode));
            RCandRT = new And(equalsRC, equalsRT);
            cvVAonLAAmtDetails = cvAmountDetails.filter(RCandRT);
            
            if (cvVAonLAAmtDetails.size() > 0) {
                amountBean = (AmountBean) cvVAonLAAmtDetails.get(0);
                rateClassCode = amountBean.getRateClassCode();
                rateTypeCode = amountBean.getRateTypeCode();
                equalsRC = new Equals("rateClassCode", new Integer(rateClassCode));
                equalsRT = new Equals("rateTypeCode", new Integer(rateTypeCode));
                RCandRT = new And(equalsRC, equalsRT);
                
                //get the rate applicable for VAonLA RateClassCode & RateTypeCode
                cvTempRates = cvPropRates.filter(RCandRT);
                
                //if rates available calculate amount
                if (cvTempRates.size() > 0) {
                    proposalLARatesBean = (ProposalLARatesBean) cvTempRates.get(0);
                    rate = proposalLARatesBean.getApplicableRate();
                    
                    //calculate cost & costSharing for VAonLA, set it to AmountBean
                    VAonLAcalculatedCost = (LAcalculatedCost * rate)/100;
                    // Bug fix #1835 -start
                    VAonLAcalculatedCost = (double)Math.round(VAonLAcalculatedCost*Math.pow(10.0, 2) ) / 100;
                    // Bug fix #1835 -End
                    VAonLAcalculatedCostSharing = (LAcalculatedCostSharing * rate)/100;
                    //Case Id #1811 - start
                    amountBean.setBaseAmount(LAcalculatedCost );
                    //Case Id #1811 End
                    amountBean.setAppliedRate(rate);
                    amountBean.setCalculatedCost(amountBean.getCalculatedCost() + VAonLAcalculatedCost);
                    amountBean.setCalculatedCostSharing(amountBean.getCalculatedCostSharing() + VAonLAcalculatedCostSharing);
                    
                    //rates not available, so set to zero
                } else {
                    amountBean.setCalculatedCost(0);
                    amountBean.setCalculatedCostSharing(0);
                    //Case Id #1811 start
                    amountBean.setBaseAmount(0);
                    //Case Id #1811 End
                }
            }
            /*** Calculation of VA on LA ends here ***/
            
        } //for loop of rate class type 'Y' ends here
    }
    
    /**
     * Calculate OH Amounts and Under recovery
     *
     * @param CoeusVector cvOHAmtDetails
     */
    private void calculateOHandUnderRecovery(CoeusVector cvOHAmtDetails,
            int EBonLARateClassCode, int EBonLARateTypeCode,
            int VAonLARateClassCode, int VAonLARateTypeCode) {
        
        AmountBean amountBean;
        int rateClassCode = 0;
        int rateTypeCode = 0;
        double applicableRate = 0;
        double instituteRate = 0;
       //COEUS-702: Budget issue calculating UR with "FUNSN"
        double urApplicableRate = 0; 
       //COEUS-702: Budget issue calculating UR with "FUNSN"
        double underRecoveryRate = 0;
        double OHcalculatedCost = 0;
        double OHcalculatedCostSharing = 0;
        double EBCalculatedCost = 0;
        double VACalculatedCost = 0;
        double EBCalculatedCostSharing = 0;
        double VACalculatedCostSharing = 0;
        Equals equalsRCType;
        Equals equalsRC;
        Equals equalsRT;
        NotEquals notEqualsRC;
        NotEquals notEqualsRT;
        And RCandRT;
        And neRCAndneRT;
        And eqRCTypeAndneRCAndneRTOrneRT;
        Or neRCandneRTOrneRT;
        CoeusVector cvTempRates;
        ProposalLARatesBean proposalLARatesBean;
        
        //get all the EB amts. Take care to exclude EB on LA
        equalsRCType = new Equals("rateClassType", RateClassTypeConstants.EMPLOYEE_BENEFITS);
        notEqualsRC = new NotEquals("rateClassCode", new Integer(EBonLARateClassCode));
        notEqualsRT = new NotEquals("rateTypeCode", new Integer(EBonLARateTypeCode));
        neRCAndneRT = new And(notEqualsRC, notEqualsRT);
        neRCandneRTOrneRT = new Or(neRCAndneRT, notEqualsRT);
        eqRCTypeAndneRCAndneRTOrneRT = new And(equalsRCType, neRCandneRTOrneRT);
        
        //sum up all the EB amts
        //EBCalculatedCost = cvAmountDetails.sum("calculatedCost", eqRCTypeAndneRCAndneRTOrneRT);
        // Bug fix #1835 -start
        EBCalculatedCost = ((double)Math.round(cvAmountDetails.sum("calculatedCost", eqRCTypeAndneRCAndneRTOrneRT)*
                Math.pow(10.0, 2) )) / 100;
        // Bug fix #1835 -End
        
        EBCalculatedCostSharing = cvAmountDetails.sum("calculatedCostSharing", eqRCTypeAndneRCAndneRTOrneRT);
        
        //get all the VA amts. Take care to exclude VA on LA
        equalsRCType = new Equals("rateClassType", RateClassTypeConstants.VACATION);
        notEqualsRC = new NotEquals("rateClassCode", new Integer(VAonLARateClassCode));
        notEqualsRT = new NotEquals("rateTypeCode", new Integer(VAonLARateTypeCode));
        neRCAndneRT = new And(notEqualsRC, notEqualsRT);
        neRCandneRTOrneRT = new Or(neRCAndneRT, notEqualsRT);
        eqRCTypeAndneRCAndneRTOrneRT = new And(equalsRCType, neRCandneRTOrneRT);
        
        //sum up all the VA amts
//        VACalculatedCost = cvAmountDetails.sum("calculatedCost", eqRCTypeAndneRCAndneRTOrneRT);
        // Bug fix #1835 -start
        VACalculatedCost = ((double)Math.round(cvAmountDetails.sum("calculatedCost", eqRCTypeAndneRCAndneRTOrneRT)*
                Math.pow(10.0, 2) )) / 100;
        // Bug fix #1835 -End
        VACalculatedCostSharing = cvAmountDetails.sum("calculatedCostSharing", eqRCTypeAndneRCAndneRTOrneRT);
        
        //Now calculate OH & Under-recovery amounts
        int amtDetailsSize = cvOHAmtDetails.size();
        
        //loop thru each amount detail bean and calculate
        for (int amtDetailsIndex = 0; amtDetailsIndex < amtDetailsSize; amtDetailsIndex++) {
            amountBean = (AmountBean) cvOHAmtDetails.get(amtDetailsIndex);
            rateClassCode = amountBean.getRateClassCode();
            rateTypeCode = amountBean.getRateTypeCode();
            equalsRC = new Equals("rateClassCode", new Integer(rateClassCode));
            equalsRT = new Equals("rateTypeCode", new Integer(rateTypeCode));
            RCandRT = new And(equalsRC, equalsRT);
            
            //get the applicable rate & institute rate for this RateClassCode & RateTypeCode
            cvTempRates = cvPropRates.filter(RCandRT);
            
            //if rates available calculate OH & Under recovery amounts
            if (cvTempRates.size() > 0) {
                proposalLARatesBean = (ProposalLARatesBean) cvTempRates.get(0);
                applicableRate = proposalLARatesBean.getApplicableRate();
                
                //If Under-recovery rate is present, then use this for Institute Rate
                if (uRRatesBean != null) {
                    urApplicableRate = uRRatesBean.getInstituteRate();//COEUS-702: Budget issue calculating UR with "FUNSN"  
                }else{
                       if(isURMatchesOh()){
                                    urApplicableRate = proposalLARatesBean.getInstituteRate()  ;
                         }
                 } 
                /*
                 * Commented for COEUS-702: Budget issue calculating UR with "FUNSN"                 
                else {
                    instituteRate = proposalLARatesBean.getInstituteRate();
                }
                */                
                if (!amountBean.isApplyRateFlag()) {
                    underRecoveryRate = urApplicableRate;//COEUS-702: Budget issue calculating UR with "FUNSN"  
                } else {
                    //calculate OH cost & costSharing, set it to AmountBean
                    OHcalculatedCost = (applicableAmt + EBCalculatedCost +
                            VACalculatedCost) * applicableRate/100;
//                    // bug fix 1835 - start
                    OHcalculatedCost = (double)Math.round(OHcalculatedCost*Math.pow(10.0, 2) ) / 100;
//                    // bug fix 1835 - End
                    // Getting the Base Amount for the OH Calculation  = EB CalculatedCost+LI Cost+Va Caculated Cost
                    double baseAmount = EBCalculatedCost+VACalculatedCost+applicableAmt;
                    OHcalculatedCostSharing = (applicableAmtCostSharing + EBCalculatedCostSharing +
                            VACalculatedCostSharing) * applicableRate/100;
                    amountBean.setAppliedRate(applicableRate);
                    //Case Id #1811 start
                    //amountBean.setBaseAmount(applicableAmt);
                    amountBean.setBaseAmount(baseAmount);
                    //Case Id #1811 End
                    amountBean.setCalculatedCost(OHcalculatedCost);
                    amountBean.setCalculatedCostSharing(OHcalculatedCostSharing);
                    
                    underRecoveryRate = urApplicableRate - applicableRate;//COEUS-702: Budget issue calculating UR with "FUNSN"  
                    
                }
                
                /**
                 *Calculate Under Recovery and add it to underRecovery attribute
                 *of this breakup interval
                 */
                //if (underRecoveryRate > 0) {
                underRecovery = underRecovery + ((applicableAmt + EBCalculatedCost +
                        VACalculatedCost) * underRecoveryRate/100);
                underRecovery = underRecovery + ((applicableAmtCostSharing + EBCalculatedCostSharing +
                        VACalculatedCostSharing) * underRecoveryRate/100);
                //}
                
                //rates not available, so set to zero
            } else {
                amountBean.setCalculatedCost(0);
                amountBean.setCalculatedCostSharing(0);
                //Case Id #1811 start
                amountBean.setBaseAmount(0);
                //Case Id #1811 End
            }
            
        }
    }
    
    /** Getter for property boundary.
     * @return Value of property boundary.
     */
    public Boundary getBoundary() {
        return boundary;
    }
    
    /** Setter for property boundary.
     * @param boundary New value of property boundary.
     */
    public void setBoundary(Boundary boundary) {
        this.boundary = boundary;
    }
    
    /** Getter for property underRecovery.
     * @return Value of property underRecovery.
     */
    public double getUnderRecovery() {
        return underRecovery;
    }
    
    /** Setter for property underRecovery.
     * @param underRecovery New value of property underRecovery.
     */
    public void setUnderRecovery(double underRecovery) {
        this.underRecovery = underRecovery;
    }
    
    /** Getter for property cvPropRates.
     * @return Value of property cvPropRates.
     */
    public CoeusVector getCvPropRates() {
        return cvPropRates;
    }
    
    /** Setter for property cvPropRates.
     * @param cvPropRates New value of property cvPropRates.
     */
    public void setCvPropRates(CoeusVector cvPropRates) {
        this.cvPropRates = cvPropRates;
    }
    
    /** Getter for property cvPropLARates.
     * @return Value of property cvPropLARates.
     */
    public CoeusVector getCvPropLARates() {
        return cvPropLARates;
    }
    
    /** Setter for property cvPropLARates.
     * @param cvPropLARates New value of property cvPropLARates.
     */
    public void setCvPropLARates(CoeusVector cvPropLARates) {
        this.cvPropLARates = cvPropLARates;
    }
    
    /** Getter for property cvAmountDetails.
     * @return Value of property cvAmountDetails.
     */
    public CoeusVector getCvAmountDetails() {
        return cvAmountDetails;
    }
    
    /** Setter for property cvAmountDetails.
     * @param cvAmountDetails New value of property cvAmountDetails.
     */
    public void setCvAmountDetails(CoeusVector cvAmountDetails) {
        this.cvAmountDetails = cvAmountDetails;
    }
    
    /** Getter for property applicableAmt.
     * @return Value of property applicableAmt.
     *
     */
    public double getApplicableAmt() {
        return applicableAmt;
    }
    
    /** Setter for property applicableAmt.
     * @param applicableAmt New value of property applicableAmt.
     *
     */
    public void setApplicableAmt(double applicableAmt) {
        // Bug fix #1835 -start
        applicableAmt = (double)Math.round(applicableAmt*Math.pow(10.0, 2) ) / 100;
        // Bug fix #1835 -End
        this.applicableAmt = applicableAmt;
    }
    
    /** Getter for property applicableAmtCostSharing.
     * @return Value of property applicableAmtCostSharing.
     *
     */
    public double getApplicableAmtCostSharing() {
        return applicableAmtCostSharing;
    }
    
    /** Setter for property applicableAmtCostSharing.
     * @param applicableAmtCostSharing New value of property applicableAmtCostSharing.
     *
     */
    public void setApplicableAmtCostSharing(double applicableAmtCostSharing) {
        applicableAmtCostSharing = (double)Math.round(applicableAmtCostSharing*Math.pow(10.0, 2) ) / 100;
        this.applicableAmtCostSharing = applicableAmtCostSharing;
    }
    
    /** Getter for property uRRatesBean.
     * @return Value of property uRRatesBean.
     *
     */
    public ProposalRatesBean getURRatesBean() {
        return uRRatesBean;
    }
    
    /** Setter for property uRRatesBean.
     * @param uRRatesBean New value of property uRRatesBean.
     *
     */
    public void setURRatesBean(ProposalRatesBean uRRatesBean) {
        this.uRRatesBean = uRRatesBean;
    }
    
    /**
     * Getter for property proposalNumber.
     * @return Value of property proposalNumber.
     */
    public java.lang.String getProposalNumber() {
        return proposalNumber;
    }
    
    /**
     * Setter for property proposalNumber.
     * @param proposalNumber New value of property proposalNumber.
     */
    public void setProposalNumber(java.lang.String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
    /**
     * Getter for property budgetPeriod.
     * @return Value of property budgetPeriod.
     */
    public int getBudgetPeriod() {
        return budgetPeriod;
    }
    
    /**
     * Setter for property budgetPeriod.
     * @param budgetPeriod New value of property budgetPeriod.
     */
    public void setBudgetPeriod(int budgetPeriod) {
        this.budgetPeriod = budgetPeriod;
    }
    
    /**
     * Getter for property lineItemNumber.
     * @return Value of property lineItemNumber.
     */
    public int getLineItemNumber() {
        return lineItemNumber;
    }
    
    /**
     * Setter for property lineItemNumber.
     * @param lineItemNumber New value of property lineItemNumber.
     */
    public void setLineItemNumber(int lineItemNumber) {
        this.lineItemNumber = lineItemNumber;
    }
    
    /**
     * Getter for property versionNumber.
     * @return Value of property versionNumber.
     */
    public int getVersionNumber() {
        return versionNumber;
    }
    
    /**
     * Setter for property versionNumber.
     * @param versionNumber New value of property versionNumber.
     */
    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }
    
    /**
     * Getter for property rateNumber.
     * @return Value of property rateNumber.
     */
    public int getRateNumber() {
        return rateNumber;
    }
    
    /**
     * Setter for property rateNumber.
     * @param rateNumber New value of property rateNumber.
     */
    public void setRateNumber(int rateNumber) {
        this.rateNumber = rateNumber;
    }
    
    // end calculateBreakupInterval
    
    
    // Added for COEUSQA-2393 Revamp Coeus Budget Engine - Start
    /**
     * This method is used for to Calculate Rates for the LineItem based on the calculation methology defined
     * in the Inclusion and Exclusion table for each breakup interval.
     * @return void
     */
    public void calcBreakUpIntervalForInclAndExcl() throws DBException, CoeusException{
        
        CoeusVector cvInclusion = new CoeusVector();
        CoeusVector cvExclusion = new CoeusVector();
        
        CoeusVector cvAmountDetailsforCE = cvAmountDetails;
        Equals eqRcInclWithZero;
        NotEquals notEqRcInclWithZero;
        
        
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        
        //Get the Rate class inclusion details
        cvInclusion =  budgetDataTxnBean.getRateClassBaseInculsions();
        
        //Get the Rate class exclusion details
        cvExclusion =  budgetDataTxnBean.getRateClassBaseExclusions();
        
        cvInclusion = cvInclusion == null ? new CoeusVector():cvInclusion;
        cvExclusion = cvExclusion == null ? new CoeusVector():cvExclusion;
        
        //Filter and get all the RateClassCodeInclusion data, which have zero's' in the osp$rate_class_base_inclusion table
        eqRcInclWithZero = new Equals(RATE_CLASS_CODE_INCL, new Integer("0"));
        CoeusVector cvRateClassCodeInclwithZero = cvInclusion.filter(eqRcInclWithZero);
        
        //Filter and Get all the RateClassCodeInclusion data, which have non zero's' in osp$rate_class_base_inclusion table
        notEqRcInclWithZero = new NotEquals(RATE_CLASS_CODE_INCL, new Integer("0"));
        CoeusVector cvRateClassCodeInclwithoutZero = cvInclusion.filter(notEqRcInclWithZero);      
        
        
        // Iterate all the RateClassCode which has inclusion is zero for RateClassCodeInclusion field
        // if same Rateclasscode is not exist in non zero's collection cvRateClassCodeInclwithoutZero, calculate the lineitem cost.
        applyRatesForBreakUpInterval(cvRateClassCodeInclwithZero, cvRateClassCodeInclwithoutZero, cvAmountDetailsforCE, cvInclusion);
        
        
        // Iterate all the RateClassCodeInclusion and RateTypeCodeInclusion data which have non zero's' in osp$rate_class_base_inclusion table
        applyRatesForInclusionBreakUpInterval(cvRateClassCodeInclwithoutZero, cvRateClassCodeInclwithZero, cvInclusion,  cvExclusion,  cvAmountDetailsforCE);
        
        // Update Rate and Base bean details for line item.
        updateRateBaseDetails(cvAmountDetailsforCE);
        
    }
    /**
     * This method is used for to Iterate all the RateClassCode and RateTypeCode in Inclusion table
     * which has inclusion value 'ZERO' for RateClassCodeInclusion field.
     * if same RateClasscode and RateTypeCode is not exist in non ZERO's collection cvRateClassCodeInclwithoutZero and
     * calculate the lineitem cost.
     * @param cvRateClassCodeInclwithZero : collection has the values which has inclusion value 'ZERO' for RateClassCodeInclusion field.
     * @param cvRateClassCodeInclwithoutZero : collection has the values which has inclusion value 'NON-ZERO' for RateClassCodeInclusion field.
     * @param cvAmountDetailsforCE :collection has all the amount details for the LineItem
     * @param cvInclusion : collection has all the inclusion data
     * @return void
     */
    private void applyRatesForBreakUpInterval(CoeusVector cvRateClassCodeInclwithZero, CoeusVector cvRateClassCodeInclwithoutZero, CoeusVector cvAmountDetailsforCE, CoeusVector cvInclusion){
        And RCandRT;
        Equals equalsRT;
        Equals equalsRC;
        And rateClassCodeAndRateTypeCode;
        NotEquals notEqRateTypeCode;
        Equals eqRateTypeCode;
        Equals eqRateClassCode;
        RateClassBaseInclusionBean rateClassBaseInclusionBean = null;
        ProposalLARatesBean proposalLARatesBean = null;
        CoeusVector cvTempRates;
        
        //Iterate all the RateClassCode which has inclusion is zero for RateClassCodeInclusion field
        //if same Rateclasscode is not exist in non zero's collection cvRateClassCodeInclwithoutZero, calculate the lineitem cost.
        if(cvRateClassCodeInclwithZero != null && !cvRateClassCodeInclwithZero.isEmpty()){
            cvRateClassCodeInclwithZero.sort(RATE_CLASS_CODE,true);
            
            //filter based on RateTypeCode which has the value non zero's
            NotEquals notEqRateTypeWithZero =  new NotEquals(RATE_TYPE_CODE, new Integer("0"));
            CoeusVector cvRateTypeCodeWithoutZero = cvInclusion.filter(notEqRateTypeWithZero);
            
            for(Object objRatesClassCodeInclwithZero : cvRateClassCodeInclwithZero){
                rateClassBaseInclusionBean = (RateClassBaseInclusionBean) objRatesClassCodeInclwithZero;
                eqRateClassCode = new Equals(RATE_CLASS_CODE, rateClassBaseInclusionBean.getRateClassCode());
                eqRateTypeCode =  new Equals(RATE_TYPE_CODE, rateClassBaseInclusionBean.getRateTypeCode());
                notEqRateTypeCode = new NotEquals(RATE_TYPE_CODE, new Integer("0"));
                
                CoeusVector cvLineItemRates = null;
                
                // If inclusion table has RateClassCode and RateTypeCode exist, add both to the And condition
                if(rateClassBaseInclusionBean.getRateTypeCode() > 0){
                    rateClassCodeAndRateTypeCode = new And(eqRateClassCode, eqRateTypeCode);
                    cvLineItemRates = cvAmountDetailsforCE.filter(rateClassCodeAndRateTypeCode);
                }else{
                    // If inclusion table has RateTypeCode is zero, add both not equals to zero to And condition
                    rateClassCodeAndRateTypeCode = new And(eqRateClassCode, notEqRateTypeCode);
                    
                    // filter the line Item rates for RateClassCode and RateTypeCode which is zero i.e. NULL 
                    // and remove the rates which has RateTypeCode values defined in the table
                     cvLineItemRates = filterLineItemRates(cvAmountDetailsforCE, rateClassCodeAndRateTypeCode, cvRateTypeCodeWithoutZero);
                }
                
                
                //CoeusVector cvAmountDetailsforCE has all the applicable rates for lineitem.
                //Check whether lineitem rates are exist for rateclasscode in rateClassBaseInclusionBean
//                CoeusVector cvLineItemRates = cvAmountDetailsforCE.filter(rateClassCodeAndRateTypeCode);
                if(cvLineItemRates != null && !cvLineItemRates.isEmpty()){
                    
                    for( Object objLineItemRates : cvLineItemRates){
                        cvTempRates = new CoeusVector();
                        double rate = 0;
                        double calculatedCost = 0;
                        double calculatedCostSharing = 0;
                        
                        AmountBean amountBean = (AmountBean) objLineItemRates;
                        equalsRC = new Equals(RATE_CLASS_CODE, new Integer(amountBean.getRateClassCode()));
                        equalsRT = new Equals(RATE_TYPE_CODE, new Integer(amountBean.getRateTypeCode()));
                        RCandRT = new And(equalsRC, equalsRT);
                        
                        //Check whether the LineItem's rateClass and rateType exist in non zero collection, if not exist calculate baseCost
                        if(cvRateClassCodeInclwithoutZero != null && cvRateClassCodeInclwithoutZero.filter(RCandRT).isEmpty()){
                            //get the rate applicable for this RateClassCode & RateTypeCode
                            // check lineItem RateClassCode and RateTypeCode is exist in Proposal rate, if not check in the LARates collection
                            cvTempRates = cvPropRates.filter(RCandRT);
                            if(cvTempRates == null || cvTempRates.isEmpty()){
                                cvTempRates = cvPropLARates.filter(RCandRT);
                            }
                            
                            //if rates available calculate amount
                            if (cvTempRates.size() > 0) {
                                proposalLARatesBean = (ProposalLARatesBean) cvTempRates.get(0);
                                rate = proposalLARatesBean.getApplicableRate();
                                
                                rate = roundDecimalValue(rate, 2);
                                
                                if (RateClassTypeConstants.OVERHEAD.equals(amountBean.getRateClassType()) && amountBean.isApplyRateFlag()) {
                                    calculatedCost = (applicableAmt * rate)/100;
                                    calculatedCost = roundDecimalValue(calculatedCost, 2);
                                    
                                    calculatedCostSharing = (applicableAmtCostSharing * rate)/100;
                                    calculatedCostSharing = roundDecimalValue(calculatedCostSharing, 2);
                                    
                                    amountBean.setBaseAmount(applicableAmt);
                                    
                                    amountBean.setAppliedRate(rate);
                                    amountBean.setCalculatedCost(calculatedCost);
                                    amountBean.setCalculatedCostSharing(calculatedCostSharing);
                                    amountBean.setCalculated(true);
                                    
                                }else if(!RateClassTypeConstants.OVERHEAD.equals(amountBean.getRateClassType())){
                                    //calculate cost & costSharing, set it to AmountBean
                                    calculatedCost = (applicableAmt * rate)/100;
                                    calculatedCost = roundDecimalValue(calculatedCost, 2);
                                    
                                    calculatedCostSharing = (applicableAmtCostSharing * rate)/100;
                                    calculatedCostSharing = roundDecimalValue(calculatedCostSharing, 2);
                                    
                                    amountBean.setBaseAmount(applicableAmt);
                                    
                                    amountBean.setAppliedRate(rate);
                                    amountBean.setCalculatedCost(calculatedCost);
                                    amountBean.setCalculatedCostSharing(calculatedCostSharing);
                                    amountBean.setCalculated(true);
                                }
                                
                                //rates not available, so set to zero
                            } else {
                                amountBean.setCalculatedCost(0);
                                amountBean.setCalculatedCostSharing(0);
                                amountBean.setBaseAmount(0);
                                
                            }
                        }
                        
                    }
                    
                }
                
            }
            
        }
        
    }
    
    /**
     * This method is used for to Iterate all the RateClassCode and RateTypeCode in Inclusion table
     * which has inclusion value 'NON-ZERO' for RateClassCodeInclusion and RateTypeCodeInclusion fields.
     * @param cvRateClassCodeInclwithoutZero : collection has the values which has inclusion value 'NON-ZERO' for RateClassCodeInclusion field.
     * @param cvRateClassCodeInclwithZero : collection has the values which has inclusion value 'ZERO' for RateClassCodeInclusion field.
     * @param cvInclusion : collection has all the inclusion data
     * @param cvExclusion : collection has all the exclusion data
     * @param cvAmountDetailsforCE :collection has all the amount details for the LineItem
     * @return void
     */
    private void applyRatesForInclusionBreakUpInterval(CoeusVector cvRateClassCodeInclwithoutZero,CoeusVector cvRateClassCodeInclwithZero, CoeusVector cvInclusion, CoeusVector cvExclusion, CoeusVector cvAmountDetailsforCE){
        Equals equalsRT;
        Equals equalsRC;
        And RCandRT;
        And rateClassCodeAndRateTypeCode;
        NotEquals notEqRateTypeCode;
        Equals eqRateTypeCode;
        Equals eqRateClassCode;
        
        RateClassBaseInclusionBean rateClassBaseInclusionBean = null;
        ProposalLARatesBean proposalLARatesBean = null;
        CoeusVector cvTempRates;
        
        //Iterate all the RateClassCodeInclusion and RateTypeCodeInclu data which has non zero's' in osp$rate_class_base_inclusion table
        if(cvRateClassCodeInclwithoutZero != null && !cvRateClassCodeInclwithoutZero.isEmpty()){
//            cvRateClassCodeInclwithoutZero.sort("rateClassCode",true);
            
            CoeusVector cvAmountBeanDetails = null;
            int prevRateClassCode = -1;
            int prevRateTypeCode = -1;
            
            //filter based on RateTypeCode which has the value non zero's
            NotEquals notEqRateTypeWithZero =  new NotEquals(RATE_TYPE_CODE, new Integer("0"));
            CoeusVector cvRateTypeCodeWithoutZero = cvInclusion.filter(notEqRateTypeWithZero);
            
            for( Object objRateClassCodeInclwithoutZero : cvRateClassCodeInclwithoutZero){
                // check for inclusion
                cvAmountBeanDetails = new CoeusVector();
                cvAmoutDetails = new CoeusVector();
                CoeusVector fileterRateClassData = null;
                 CoeusVector cvLineItemRates = null;
                
                rateClassBaseInclusionBean = (RateClassBaseInclusionBean) objRateClassCodeInclwithoutZero;
                
                //If RateClassCode and RateTypeCode is already calculated, need to skip those items.
                if (rateClassBaseInclusionBean.getRateClassCode() == prevRateClassCode &&
                        rateClassBaseInclusionBean.getRateTypeCode() == prevRateTypeCode) {
                    continue;
                } else {
                    prevRateClassCode = rateClassBaseInclusionBean.getRateClassCode();
                    prevRateTypeCode = rateClassBaseInclusionBean.getRateTypeCode();
                }
                
                eqRateClassCode = new Equals(RATE_CLASS_CODE, rateClassBaseInclusionBean.getRateClassCode());
                eqRateTypeCode =  new Equals(RATE_TYPE_CODE, rateClassBaseInclusionBean.getRateTypeCode());
                notEqRateTypeCode = new NotEquals(RATE_TYPE_CODE, new Integer("0"));
                
                // If inclusion table has RateClassCode and RateTypeCode exist, add both to the And condition
                if(rateClassBaseInclusionBean.getRateTypeCode() > 0){
                    rateClassCodeAndRateTypeCode = new And(eqRateClassCode, eqRateTypeCode);
                    RCandRT = new And(eqRateClassCode, eqRateTypeCode);
                    
                    fileterRateClassData = cvRateClassCodeInclwithoutZero.filter(rateClassCodeAndRateTypeCode);
                    cvLineItemRates = cvAmountDetailsforCE.filter(RCandRT);
                }else{
                    // If inclusion table has RateTypeCode is zero, add only RateClassCode
                    rateClassCodeAndRateTypeCode = new And(eqRateClassCode, eqRateTypeCode);
                    RCandRT = new And(eqRateClassCode, notEqRateTypeCode);
                    
                    fileterRateClassData = cvRateClassCodeInclwithoutZero.filter(rateClassCodeAndRateTypeCode);
                    
                    // filter the line Item rates for RateClassCode and RateTypeCode which is zero i.e. NULL
                    // and remove the rates which has RateTypeCode values defined in the table
                    cvLineItemRates = filterLineItemRates(cvAmountDetailsforCE, RCandRT, cvRateTypeCodeWithoutZero);
                    
                }
                
//                CoeusVector cvLineItemRates = cvAmountDetailsforCE.filter(RCandRT);
                
                // Filter and get RateClassCode data, if it has more than one data, need to sum up all the base cost based on the inclusion
                if(cvLineItemRates != null && !cvLineItemRates.isEmpty()){
//                    for( Object objLineItemRates : cvLineItemRates){
                    cvTempRates = new CoeusVector();
                    double rate = 0;
                    double calculatedCost = 0;
                    double calculatedCostSharing = 0;
                    double instituteRate = 0;
                   //COEUS-702: Budget issue calculating UR with "FUNSN"
                    double urApplicableRate = 0;
                   //COEUS-702: Budget issue calculating UR with "FUNSN" 
                    double underRecoveryRate = 0;
                    double costSharingBase = 0;
                    
                    AmountBean amountBean = (AmountBean)cvLineItemRates.get(0);
                    equalsRC = new Equals(RATE_CLASS_CODE, new Integer(amountBean.getRateClassCode()));
                    equalsRT = new Equals(RATE_TYPE_CODE, new Integer(amountBean.getRateTypeCode()));
                    RCandRT = new And(equalsRC, equalsRT);
                    
                    Double baseCost = 0.0;
                    if(fileterRateClassData !=null && !fileterRateClassData.isEmpty()){
                        for( Object objRateClassData : fileterRateClassData){
                            RateClassBaseInclusionBean rateClassBaseInclBean = (RateClassBaseInclusionBean) objRateClassData;
//                            System.out.println("RateClassCode "+rateClassBaseInclBean.getRateClassCode());
//                            System.out.println("RateType "+rateClassBaseInclBean.getRateTypeCode());
                            cvAmountBeanDetails = calcualteAllRatesForInclusion(rateClassBaseInclBean, cvRateClassCodeInclwithoutZero, cvInclusion, cvExclusion, cvAmountDetailsforCE);
                        }
                    }
                    
                    cvTempRates = cvPropRates.filter(RCandRT);
                    if(cvTempRates == null || cvTempRates.isEmpty()){
                        cvTempRates = cvPropLARates.filter(RCandRT);
                    }
                    
                    CoeusVector fileterRateClass = cvRateClassCodeInclwithZero.filter(rateClassCodeAndRateTypeCode);
                    
                    //if rates available calculate amount
                    if (cvTempRates.size() > 0) {
                        proposalLARatesBean = (ProposalLARatesBean) cvTempRates.get(0);
                        rate = proposalLARatesBean.getApplicableRate();
                      
                        rate = roundDecimalValue(rate, 2);
                        
                        //If Under-recovery rate is present, then use this for Institute Rate
                        if(RateClassTypeConstants.OVERHEAD.equals(amountBean.getRateClassType())){
                            boolean isOHCalculated = false;
                            if (uRRatesBean != null) {
                                urApplicableRate = uRRatesBean.getInstituteRate();//COEUS-702: Budget issue calculating UR with "FUNSN"
                            }else{
                                if(isURMatchesOh()){
                                    urApplicableRate = proposalLARatesBean.getInstituteRate();
                                }
                            }
                            /*
                             * Commented for COEUS-702: Budget issue calculating UR with "FUNSN"
                             * else {
                                instituteRate = proposalLARatesBean.getInstituteRate();
                            }                             
                             */
                            if( fileterRateClass !=null && !fileterRateClass.isEmpty() && amountBean.isApplyRateFlag()){
                                baseCost = amountBean.getBaseAmount()+cvAmountBeanDetails.sum(CALCULATED_COST) ;
                                costSharingBase = applicableAmtCostSharing +cvAmountBeanDetails.sum(CALCULATED_COST_SHARING) ;
                                isOHCalculated = true;
                            }else if( fileterRateClass !=null && !fileterRateClass.isEmpty() && !amountBean.isApplyRateFlag()){
                                baseCost = applicableAmt+cvAmountBeanDetails.sum(CALCULATED_COST) ;;
                                costSharingBase = applicableAmtCostSharing+cvAmountBeanDetails.sum(CALCULATED_COST_SHARING) ;;
                                isOHCalculated = true;
                            }else{
                                baseCost = cvAmountBeanDetails.sum(CALCULATED_COST) ;
                                costSharingBase = cvAmountBeanDetails.sum(CALCULATED_COST_SHARING) ;
                                isOHCalculated = true;
                            }
                            
                            if (!amountBean.isApplyRateFlag()) {
                                underRecoveryRate = urApplicableRate;//COEUS-702: Budget issue calculating UR with "FUNSN"
                                
                                //If there OH is not calculated , then use applicable amount as base cost. 
                                if(!isOHCalculated){
                                    baseCost = applicableAmt;
                                    costSharingBase = applicableAmtCostSharing;
                                }
                                
                            }else {

                                baseCost = roundDecimalValue(baseCost, 2); 
                                //calculate cost & costSharing, set it to AmountBeank
                                calculatedCost = ( baseCost * rate)/100;
                                calculatedCost = roundDecimalValue(calculatedCost, 2); 
                                
                                calculatedCostSharing = ( costSharingBase * rate)/100;
                                calculatedCostSharing = roundDecimalValue(calculatedCostSharing, 2); 
                                
                                amountBean.setBaseAmount(baseCost);
                                amountBean.setAppliedRate(rate);
                                amountBean.setCalculatedCost(calculatedCost);
                                amountBean.setCalculatedCostSharing(calculatedCostSharing);
                                underRecoveryRate = urApplicableRate - rate;//COEUS-702: Budget issue calculating UR with "FUNSN"
                                
                            }/**
                             *Calculate Under Recovery and add it to underRecovery attribute
                             *of this breakup interval
                             */
                            underRecovery = underRecovery + (baseCost * underRecoveryRate/100);
                            underRecovery = underRecovery + (costSharingBase * underRecoveryRate/100);
                            
                        }else{
                            
                            if( fileterRateClass !=null && !fileterRateClass.isEmpty()){
                                baseCost = amountBean.getBaseAmount()+cvAmountBeanDetails.sum(CALCULATED_COST) ;
                                costSharingBase = applicableAmtCostSharing +cvAmountBeanDetails.sum(CALCULATED_COST_SHARING) ;
                            }else if(cvAmountBeanDetails !=null && !cvAmountBeanDetails.isEmpty()){
                                baseCost = cvAmountBeanDetails.sum(CALCULATED_COST) ;
                                costSharingBase = cvAmountBeanDetails.sum(CALCULATED_COST_SHARING) ;
                            }else{
                                // If amount bean is not existing, use the applicable amount.
                                baseCost = applicableAmt;
                                costSharingBase = applicableAmtCostSharing;
                            }
                            
//                            baseCost = cvAmountBeanDetails.sum(CALCULATED_COST) ;
                            
                            baseCost = roundDecimalValue(baseCost, 2); 
                            
                            //calculate cost & costSharing, set it to AmountBeank
                            calculatedCost = ( baseCost * rate)/100;
                            calculatedCost = roundDecimalValue(calculatedCost, 2); 
                            
//                            calculatedCostSharing = (applicableAmtCostSharing * rate)/100;
                            calculatedCostSharing = (costSharingBase * rate)/100;
                            calculatedCostSharing = roundDecimalValue(calculatedCostSharing, 2); 
                            
                            amountBean.setBaseAmount(baseCost);
                            amountBean.setAppliedRate(rate);
                            amountBean.setCalculatedCost(calculatedCost);
                            amountBean.setCalculatedCostSharing(calculatedCostSharing);
                            amountBean.setCalculated(true);
                        }
                        
                        //rates not available, so set to zero
                    } else {
                        amountBean.setCalculatedCost(0);
                        amountBean.setCalculatedCostSharing(0);
                        amountBean.setBaseAmount(0);
                    }
                    
                }
            }
        }
    }
    
    /**
     * This method is used to Calculate Rates for each RateClassCode and RateTypeCode Inclusion.
     * If there any exculation for RateClass and RateType will exclude the data and will build the hierarchy and Apply the rates.
     * @param rateClassBaseInclusionBean : It contains Inclusion Details for each RateClass and RateType
     * @param cvRateinclusionData : It contains Inclusion details for each RateClass and RateType
     * @param cvRateinclusionAllData : collection has all the inclusion data
     * @param cvExclusion : collection has all the exclusion data
     * @param cvAmountDetailsforCE :collection has all the amount details for the LineItem
     * @return CoeusVector - cvAmoutDetails
     */
    private CoeusVector calcualteAllRatesForInclusion(RateClassBaseInclusionBean rateClassBaseInclusionBean, CoeusVector cvRateinclusionData, CoeusVector cvRateinclusionAllData, CoeusVector cvExclusion, CoeusVector cvAmountDetailsforCE) {
        
        Equals eqRateClassCodeIncl;
        Equals eqRateTypeCodeIncl;
        Equals eqRateClassCodeExcl;
        Equals eqRateTypeCodeExcl;
        
        And rateClassCodeAndRateTypeCodeIncl;
        And rateClassCodeAndRateTypeCodeExcl;
        CoeusVector cvFilteredInclRates;
        Equals eqRateClassCode;
        Equals eqRateTypeCode;
        And rateClassCodeAndRateTypeCode;
        
        CoeusVector cvFileteredExclDetails;
        RateClassBaseExclusionBean rateClassBaseExclusionBean = null;
        
        eqRateClassCodeIncl = new Equals(RATE_CLASS_CODE, rateClassBaseInclusionBean.getRateClassCodeIncl());
        eqRateTypeCodeIncl =  new Equals(RATE_TYPE_CODE, rateClassBaseInclusionBean.getRateTypeCodeIncl());
        
        // If inclusion table has RateClassCode and RateTypeCode exist, add both to the And condition
        if(rateClassBaseInclusionBean.getRateTypeCodeIncl() > 0){
            rateClassCodeAndRateTypeCodeIncl = new And(eqRateClassCodeIncl, eqRateTypeCodeIncl);
            cvFilteredInclRates = cvRateinclusionAllData.filter(rateClassCodeAndRateTypeCodeIncl);
        }else{
            // If inclusion table has RateTypeCode is zero, add both not equals to zero to And condition
            cvFilteredInclRates = cvRateinclusionAllData.filter(eqRateClassCodeIncl);
        }
        
        // If filtered rates (RateClass & RateType) are exist in the exclusion list, remove it.
        eqRateClassCode = new Equals(RATE_CLASS_CODE, rateClassBaseInclusionBean.getRateClassCode());
        eqRateTypeCode =  new Equals(RATE_TYPE_CODE, rateClassBaseInclusionBean.getRateTypeCode());
        if(rateClassBaseInclusionBean.getRateTypeCode() > 0){
            rateClassCodeAndRateTypeCode = new And(eqRateClassCode, eqRateTypeCode);
            cvFileteredExclDetails = cvExclusion.filter(rateClassCodeAndRateTypeCode);
        }else{
            // If inclusion table has RateTypeCode is zero, add both not equals to zero to And condition
            cvFileteredExclDetails = cvExclusion.filter(eqRateClassCode);
        }
        //From Exclusion, check whether any exclusion is present if exist remove from the collection.
        if(cvFileteredExclDetails !=null && cvFileteredExclDetails.size() > 0){
            CoeusVector cvAmtBeanDetails = null;
            
            for( Object objFileteredExclDetails : cvFileteredExclDetails) {
                rateClassBaseExclusionBean = (RateClassBaseExclusionBean) objFileteredExclDetails;
                eqRateClassCodeExcl = new Equals(RATE_CLASS_CODE, rateClassBaseExclusionBean.getRateClassCodeExcl());
                eqRateTypeCodeExcl =  new Equals(RATE_TYPE_CODE, rateClassBaseExclusionBean.getRateTypeCodeExcl());
                if(rateClassBaseExclusionBean.getRateTypeCodeExcl() > 0){
                    rateClassCodeAndRateTypeCodeExcl = new And(eqRateClassCodeExcl, eqRateTypeCodeExcl);
                    cvAmtBeanDetails = cvFilteredInclRates.filter(rateClassCodeAndRateTypeCodeExcl);
                }else{
                    // If inclusion table has RateTypeCode is zero, add both not equals to zero to And condition
                    cvAmtBeanDetails = cvFilteredInclRates.filter(eqRateClassCodeExcl);
                }
                
                if (cvAmtBeanDetails != null && cvAmtBeanDetails.size() > 0){
                    cvFilteredInclRates.removeAll(cvAmtBeanDetails);
                }
                
            }
            
        }
        
        //filter based on RateTypeCode which has the value non zero's
        NotEquals notEqRateTypeWithZero =  new NotEquals(RATE_TYPE_CODE, new Integer("0"));        
        CoeusVector cvRateTypeCodeWithoutZero = cvRateinclusionAllData.filter(notEqRateTypeWithZero);
        
        cvRatesInHierachy = new CoeusVector();
        cvAmoutBeanCollection2 = new CoeusVector();
        CoeusVector cvAmoutBeanDetails = new CoeusVector();
        
        try {
            // Reset all the values
            cvFilteredInclRates.setUpdate(RateClassBaseInclusionBean.class, "parentRowIndex", DataType.getClass(DataType.INT),
                    new Integer(0), null );
        } catch (CoeusException ex) {
            UtilFactory.log( ex.getMessage() , ex , "BreakUpInterval" , "calcualteAllRatesForInclusion()" );
//            ex.printStackTrace();
        }
        //Build the calculation hierarchy based on the Inclusion and exclusion table.
        buildHierarchyForInclusionRates(cvFilteredInclRates, rateClassBaseInclusionBean, cvRateinclusionData, cvRateinclusionAllData, cvExclusion, cvAmountDetailsforCE);
        //
        cvAmoutBeanDetails = applyRatesForInclusionBuildedHierarchy(cvRatesInHierachy, cvAmountDetailsforCE, cvRateTypeCodeWithoutZero);
        
        if ( cvAmoutBeanDetails != null && !cvAmoutBeanDetails.isEmpty()){
            cvAmoutDetails.addAll(cvAmoutBeanDetails);
        }
        
        return cvAmoutDetails;
    }
    
    /**
     * This method is used for to Build the Rates Hierarchy based on the Inclusion and exclusion table.
     * For Building Hierarchy, this method will be called recursively
     * @param cvFilteredInclRates : It contains rates after the exclusion.
     * @param rateClassBaseInclusionBean : It contains Inclusion Details for each RateClass and RateType
     * @param cvRateinclusionData : It contains Inclusion details for each RateClass and RateType
     * @param cvRateinclusionAllData : collection has all the inclusion data
     * @param cvExclusion : collection has all the exclusion data
     * @param cvAmountDetailsforCE :collection has all the amount details for the LineItem
     * @return void
     *
     */
    private void  buildHierarchyForInclusionRates(CoeusVector cvFilteredInclRates, RateClassBaseInclusionBean rateClassBaseInclusionBean, CoeusVector cvRateinclusionData, CoeusVector cvRateinclusionAllData, CoeusVector cvExclusion, CoeusVector cvAmountDetailsforCE) {
        Equals eqRateClassCode;
        Equals eqRateTypeCode;
        Equals eqRateClassCodeIncl;
        Equals eqRateTypeCodeIncl;
        And rateClassCodeAndRateTypeCode;
        And rateClassCodeAndRateTypeCodeIncl = null;
        
        CoeusVector cvConstructHierarchy = new CoeusVector();
        
        if(cvFilteredInclRates != null && !cvFilteredInclRates.isEmpty()){
            
            cvConstructHierarchy = cvFilteredInclRates;
            rateClassBaseInclusionBean = (RateClassBaseInclusionBean) cvConstructHierarchy.elementAt(0);
            
            eqRateClassCode = new Equals(RATE_CLASS_CODE, rateClassBaseInclusionBean.getRateClassCode());
            eqRateTypeCode =  new Equals(RATE_TYPE_CODE, rateClassBaseInclusionBean.getRateTypeCode());
            
            if( rateClassBaseInclusionBean.getRateClassCodeIncl() == 0){
                int rowIndex = cvRatesInHierachy.size()+1;
                RateClassBaseInclusionBean rateClassInclBean =  (RateClassBaseInclusionBean) cvConstructHierarchy.get(0);
                rateClassInclBean.setRowIndex(rowIndex);
                cvRatesInHierachy.add(rateClassInclBean);
                cvConstructHierarchy.remove(0);
                buildHierarchyForInclusionRates(cvConstructHierarchy, rateClassBaseInclusionBean, cvRateinclusionData, cvRateinclusionAllData, cvExclusion, cvAmountDetailsforCE);
                
            }else{
                
                eqRateClassCodeIncl = new Equals(RATE_CLASS_CODE, rateClassBaseInclusionBean.getRateClassCodeIncl());
                eqRateTypeCodeIncl =  new Equals(RATE_TYPE_CODE, rateClassBaseInclusionBean.getRateTypeCodeIncl());
                
                // If inclusion table has RateClassCode and RateTypeCode exist, add both to the And condition
                if(rateClassBaseInclusionBean.getRateTypeCodeIncl() > 0){
                    rateClassCodeAndRateTypeCodeIncl = new And(eqRateClassCodeIncl, eqRateTypeCodeIncl);
                    cvFilteredInclRates = cvRateinclusionAllData.filter(rateClassCodeAndRateTypeCodeIncl);
                    
                }else{
                    // If inclusion table has RateTypeCode is zero, add both not equals to zero to And condition
                    cvFilteredInclRates = cvRateinclusionAllData.filter(eqRateClassCodeIncl);
                }
                if (cvFilteredInclRates !=null && !cvFilteredInclRates.isEmpty()){
                    RateClassBaseInclusionBean rateClassInclBean =  (RateClassBaseInclusionBean) cvConstructHierarchy.get(0);
                    
                    int rowIndex = cvRatesInHierachy.size()+1;
                    rateClassInclBean.setRowIndex(rowIndex);
                    
                    cvRatesInHierachy.add(rateClassInclBean);
                    cvConstructHierarchy.remove(0);
                    CoeusVector tempCollection = new CoeusVector();
                    tempCollection.addAll(cvConstructHierarchy);
                    cvConstructHierarchy.clear();
                    
                    for(Object objInclRates : cvFilteredInclRates){
                        RateClassBaseInclusionBean rateClassInclusionBean =  (RateClassBaseInclusionBean) objInclRates;
                        eqRateClassCode = new Equals(RATE_CLASS_CODE, rateClassInclusionBean.getRateClassCode());
                        eqRateTypeCode =  new Equals(RATE_TYPE_CODE, rateClassInclusionBean.getRateTypeCode());
                        rateClassCodeAndRateTypeCode = new And(eqRateClassCode, eqRateTypeCode);
                        CoeusVector cvCollectionExist = cvRatesInHierachy.filter(rateClassCodeAndRateTypeCode);
                        //Check whether same collection exist, if exist clone the new object
                        if(cvCollectionExist != null && !cvCollectionExist.isEmpty()) {
                            try{
                                
                                RateClassBaseInclusionBean rateClassInclusionBean2 =  (RateClassBaseInclusionBean) cvCollectionExist.get(0);
                                RateClassBaseInclusionBean rateClassInclusionBeanCloned = (RateClassBaseInclusionBean) ObjectCloner.deepCopy(rateClassInclusionBean2);
                                rateClassInclusionBeanCloned.setParentRowIndex(rowIndex);
                                cvFilteredInclRates.remove(rateClassInclusionBean2);
                                cvFilteredInclRates.add(rateClassInclusionBeanCloned);
                            }catch(Exception ex){
                                UtilFactory.log( ex.getMessage() , ex , "BreakUpInterval" , "buildHierarchyForInclusionRates()" );
                                ex.printStackTrace();
                            }
                        }else{
                            rateClassInclusionBean.setParentRowIndex(rowIndex);
                        }
                    }
                    cvConstructHierarchy.addAll(cvFilteredInclRates);
                    cvConstructHierarchy.addAll(tempCollection);
                    
                    buildHierarchyForInclusionRates(cvConstructHierarchy, rateClassBaseInclusionBean, cvRateinclusionData, cvRateinclusionAllData, cvExclusion, cvAmountDetailsforCE);
                    
                }
            }
        }
    }
    
    /**
     * Once Rates Hierarchy is constructed, This method will apply the rates for lineitem based on the hierarchy.
     *
     * @param cvRatesHierachy : It contains hierarchy details.
     * @param cvAmountDetailsforCE :collection has all the amount details for the LineItem
     * @param cvRateTypeCodeWithoutZero : collection has all filtered data for RateTypeCode which has the value non zero's
     * @return CoeusVector cvAmoutBeanDetails
     */
    private CoeusVector applyRatesForInclusionBuildedHierarchy(CoeusVector cvRatesHierachy, CoeusVector cvAmountDetailsforCE, CoeusVector cvRateTypeCodeWithoutZero){
        
        Equals eqRateTypeCode;
        Equals eqRateClassCode;
        CoeusVector cvFilteredInclusions = new CoeusVector();
        Equals eqParentRowIndex;
        NotEquals notEqRateTypeCode;
        And RCandRT;
        Equals equalsRC;
        Equals equalsRT;
        CoeusVector cvTempRates;
        CoeusVector cvAmoutBeanDet = new CoeusVector();
        ProposalLARatesBean proposalLARatesBean;
        CoeusVector cvAmoutBeanDetails = new CoeusVector();
        
        if( cvRatesHierachy != null && !cvRatesHierachy.isEmpty()){
            
            for(int ratesIndex = cvRatesHierachy.size(); ratesIndex > 0; ratesIndex-- ){
                RateClassBaseInclusionBean rateClassInclBean =  (RateClassBaseInclusionBean) cvRatesHierachy.get(ratesIndex-1);
                boolean isCalculated = false;
                
                eqParentRowIndex  = new Equals("parentRowIndex", rateClassInclBean.getRowIndex());
                cvFilteredInclusions = cvRatesHierachy.filter(eqParentRowIndex);
                
                if( cvFilteredInclusions !=null && !cvFilteredInclusions.isEmpty()){
                    
                    for(Object objFilteredIncl : cvFilteredInclusions){
                        CoeusVector cvLineItemRates = null;
                        RateClassBaseInclusionBean rateClassInclBean2 =  (RateClassBaseInclusionBean) objFilteredIncl;                        
                        eqRateClassCode = new Equals(RATE_CLASS_CODE, rateClassInclBean2.getRateClassCode());
                        eqRateTypeCode =  new Equals(RATE_TYPE_CODE, rateClassInclBean2.getRateTypeCode());
                        notEqRateTypeCode = new NotEquals(RATE_TYPE_CODE, new Integer("0"));
                        if(rateClassInclBean2.getRateTypeCode() > 0){
                            RCandRT = new And(eqRateClassCode, eqRateTypeCode);
                            cvLineItemRates = cvAmountDetailsforCE.filter(RCandRT);
                        }else{
                            // If inclusion table has RateTypeCode is zero, add both not equals to zero to And condition
//                            cvAmtBeanDetails = cvFilteredInclusions.filter(eqRateClassCode);
                            RCandRT = new And(eqRateClassCode, notEqRateTypeCode);
                            // filter the line Item rates for RateClassCode and RateTypeCode which is zero i.e. NULL
                            // and remove the rates which has RateTypeCode values defined in the table
                            cvLineItemRates = filterLineItemRates(cvAmountDetailsforCE, RCandRT, cvRateTypeCodeWithoutZero);
                        }
                        
//                        CoeusVector cvLineItemRates = cvAmountDetailsforCE.filter(RCandRT);
                        
                        // Filter and get RateClassCode data, if it has more than one data, need to sum up all the base cost based on the inclusion
                        if(cvLineItemRates != null && !cvLineItemRates.isEmpty()){
//                            for( Object objLineItemRates : cvLineItemRates){
                            cvTempRates = new CoeusVector();
                            double rate = 0;
                            double calculatedCost = 0;
                            double calculatedCostSharing = 0;
                            double baseCost =0;
                            double costSharingBase = 0;
//                                AmountBean amountBean2 = (AmountBean)objLineItemRates;
                            AmountBean amountBean2 = (AmountBean) cvLineItemRates.get(0);
                            equalsRC = new Equals(RATE_CLASS_CODE, new Integer(amountBean2.getRateClassCode()));
                            equalsRT = new Equals(RATE_TYPE_CODE, new Integer(amountBean2.getRateTypeCode()));
                            RCandRT = new And(equalsRC, equalsRT);
                            
                            cvTempRates = cvPropRates.filter(RCandRT);
                            if(cvTempRates == null || cvTempRates.isEmpty()){
                                cvTempRates = cvPropLARates.filter(RCandRT);
                            }
                            
                            if(amountBean2.isCalculated()){
                                cvAmoutBeanDet.add(amountBean2);
                            }else{
                                //if rates available calculate amount
                                if (cvTempRates.size() > 0) {
                                    proposalLARatesBean = (ProposalLARatesBean) cvTempRates.get(0);
                                    rate = proposalLARatesBean.getApplicableRate();
                                    rate = roundDecimalValue(rate, 2);                              
                                    
                                    baseCost = cvAmoutBeanDet.sum(CALCULATED_COST) ;
                                    costSharingBase = cvAmoutBeanDet.sum(CALCULATED_COST_SHARING) ;
                                    
                                    //calculate cost & costSharing, set it to AmountBean
                                    calculatedCost = (baseCost * rate)/100;
                                    calculatedCost = roundDecimalValue(calculatedCost, 2);
                                    
                                    calculatedCostSharing = (costSharingBase * rate)/100;
                                    calculatedCostSharing = roundDecimalValue(calculatedCostSharing, 2);
                                    
                                    amountBean2.setBaseAmount(baseCost);
                                    amountBean2.setAppliedRate(rate);
                                    amountBean2.setCalculatedCost(calculatedCost);
                                    amountBean2.setCalculatedCostSharing(calculatedCostSharing);
                                    amountBean2.setCalculated(true);
//                                    cvAmoutBeanDet.clear();
                                    cvAmoutBeanDet.add(amountBean2);
                                    //rates not available, so set to zero
                                }
                            }
                            isCalculated = true;
                        }
                    }
                }
                
                if(isCalculated || (!isCalculated && rateClassInclBean.getParentRowIndex()== 0)){
                    
                    CoeusVector cvLineItemRates = null;
                    eqRateClassCode = new Equals(RATE_CLASS_CODE, rateClassInclBean.getRateClassCode());
                    eqRateTypeCode =  new Equals(RATE_TYPE_CODE, rateClassInclBean.getRateTypeCode());
                    notEqRateTypeCode = new NotEquals(RATE_TYPE_CODE, new Integer("0"));
                    if(rateClassInclBean.getRateTypeCode() > 0){
                        RCandRT = new And(eqRateClassCode, eqRateTypeCode);
                        cvLineItemRates = cvAmountDetailsforCE.filter(RCandRT);
                    }else{
                        // If inclusion table has RateTypeCode is zero, add both not equals to zero to And condition
                        RCandRT = new And(eqRateClassCode, notEqRateTypeCode);
                       
                        // filter the line Item rates for RateClassCode and RateTypeCode which is zero i.e. NULL 
                        // and remove the rates which has RateTypeCode values defined in the table
                        cvLineItemRates = filterLineItemRates(cvAmountDetailsforCE, RCandRT, cvRateTypeCodeWithoutZero);
                    }

                    
//                    CoeusVector cvLineItemRates = cvAmountDetailsforCE.filter(RCandRT);
                    
                    // Filter and get RateClassCode data, if it has more than one data, need to sum up all the base cost based on the inclusion
                    if(cvLineItemRates != null && !cvLineItemRates.isEmpty()){
//                            for( Object objLineItemRates : cvLineItemRates){
                        cvTempRates = new CoeusVector();
                        double rate = 0;
                        double calculatedCost = 0;
                        double calculatedCostSharing = 0;
                        double baseCost =0;
                        double costSharingBase =0;
//                                AmountBean amountBean2 = (AmountBean)objLineItemRates;
                        AmountBean amountBean2 = (AmountBean) cvLineItemRates.get(0);
                        equalsRC = new Equals(RATE_CLASS_CODE, new Integer(amountBean2.getRateClassCode()));
                        equalsRT = new Equals(RATE_TYPE_CODE, new Integer(amountBean2.getRateTypeCode()));
                        RCandRT = new And(equalsRC, equalsRT);
                        
                        cvTempRates = cvPropRates.filter(RCandRT);
                        if(cvTempRates == null || cvTempRates.isEmpty()){
                            cvTempRates = cvPropLARates.filter(RCandRT);
                        }
                        
                        if(amountBean2.isCalculated()){
                            cvAmoutBeanDet.clear();
                            if(rateClassInclBean.getParentRowIndex() == 0){
                                cvAmoutBeanDetails.add(amountBean2);
                            }
                            
                        }else{
                            //if rates available calculate amount
                            if (cvTempRates.size() > 0) {
                                proposalLARatesBean = (ProposalLARatesBean) cvTempRates.get(0);
                                rate = proposalLARatesBean.getApplicableRate();
                                rate = roundDecimalValue(rate, 2);
                                
//                                baseCost = cvAmoutBeanDet.sum(CALCULATED_COST) ;
//                                costSharingBase = cvAmoutBeanDet.sum(CALCULATED_COST_SHARING) ;
                                
                                if(cvAmoutBeanDet !=null && !cvAmoutBeanDet.isEmpty()){
                                    baseCost = cvAmoutBeanDet.sum(CALCULATED_COST) ;
                                    costSharingBase = cvAmoutBeanDet.sum(CALCULATED_COST_SHARING) ;
                                }else{
                                    // If amount bean is not existing, use the applicable amount.
                                    baseCost = applicableAmt;
                                    costSharingBase = applicableAmtCostSharing;
                                }
                                
                                //calculate cost & costSharing, set it to AmountBean
                                calculatedCost = (baseCost * rate)/100;
                                calculatedCost = roundDecimalValue(calculatedCost, 2);
                                
                                
                                calculatedCostSharing = (costSharingBase * rate)/100;
                                calculatedCostSharing = roundDecimalValue(calculatedCostSharing, 2);
                                
                                amountBean2.setBaseAmount(baseCost);
                                amountBean2.setAppliedRate(rate);
                                amountBean2.setCalculatedCost(calculatedCost);
                                amountBean2.setCalculatedCostSharing(calculatedCostSharing);
                                amountBean2.setCalculated(true);
                                cvAmoutBeanDet.clear();
                                if(rateClassInclBean.getParentRowIndex() == 0){
                                    cvAmoutBeanDetails.add(amountBean2);
                                }
                                
                                //rates not available, so set to zero
                            } else {
                                amountBean2.setCalculatedCost(0);
                                amountBean2.setCalculatedCostSharing(0);
                                amountBean2.setBaseAmount(0);
                            }
                        }
                        
                    }
                }
                
            }
            
        }
        return cvAmoutBeanDetails;
    }
    
    /** Add Calculated amounts and fetch the data to the BudgetRateBaseBean
     *Hold the breakup intervals data into the dataholder and save to the
     *database as and when calculate is called
     *@param cvLIAmountDetails : collection has all the amount details for the LineItem
     */
    private void updateRateBaseDetails(CoeusVector cvLIAmountDetails){
        int rateNum = getRateNumber();
        if(cvLIAmountDetails!= null && cvLIAmountDetails.size() >0){
            cvRateBase = new CoeusVector();
            for(Object objLIAmountDetails : cvLIAmountDetails){
                BudgetRateBaseBean budgetRateBaseBean = new BudgetRateBaseBean();
                AmountBean amountBean = (AmountBean) objLIAmountDetails;
                Boundary boundary = getBoundary();
                boolean flag = amountBean.isApplyRateFlag();
                budgetRateBaseBean.setProposalNumber(getProposalNumber());
                budgetRateBaseBean.setVersionNumber(getVersionNumber());
                budgetRateBaseBean.setBudgetPeriod(getBudgetPeriod());
                budgetRateBaseBean.setRateNumber(++rateNum);
                budgetRateBaseBean.setLineItemNumber(getLineItemNumber());
                budgetRateBaseBean.setStartDate(new java.sql.Date(boundary.getStartDate().getTime()));;
                budgetRateBaseBean.setEndDate(new java.sql.Date(boundary.getEndDate().getTime()));
                budgetRateBaseBean.setRateClassCode(amountBean.getRateClassCode());
                budgetRateBaseBean.setRateTypeCode(amountBean.getRateTypeCode());
                budgetRateBaseBean.setOnOffCampusFlag(flag);
                budgetRateBaseBean.setAppliedRate(amountBean.getAppliedRate());
                budgetRateBaseBean.setBaseCost(amountBean.getBaseAmount());
                budgetRateBaseBean.setCalculatedCost(amountBean.getCalculatedCost());
                budgetRateBaseBean.setBaseCostSharing(getApplicableAmtCostSharing());
                budgetRateBaseBean.setCalculatedCostSharing(amountBean.getCalculatedCostSharing());
                budgetRateBaseBean.setAcType("I");
                cvRateBase.add(budgetRateBaseBean);
            }
        }
        
    }
    
    /** This method is used for to round the decimal value.
     *@param value : value to be rounded
     *@param noOfDecimal : no of decimal to be rounded
     */
    private double roundDecimalValue(double value, int noOfDecimal){
        double roundedValue = 0.0;
        roundedValue = (double)Math.round(value * Math.pow(10.0, noOfDecimal) ) / 100;
        return roundedValue;
    }
    
    /** This method is used for to filter the line Item rates for RateClassCode and RateTypeCode which is zero i.e. NULL in the table.
     *  If RateTpeCode is not defined i.e. NULL in the table, while filtering RateTypeCode using Line item rates with non zero should remove
     *  and exclude the RateTypeCode which has values defined in the table.
     *  @param cvAmountDetailsforCE : Amount Details for the LineItem
     *  @param Operator : And (RateClassCode and RateTypeCode)
     *  @param cvRateTypeCodeWithoutZero : collection which have RateTypeCode values.
     *  @return CoeusVector : filtered LineItemRates
     */
    private CoeusVector filterLineItemRates(CoeusVector cvAmountDetailsforCE, And RCandRT , CoeusVector cvRateTypeCodeWithoutZero) {
        Equals equalsRC;
        Equals equalsRT;
        CoeusVector cvLineItemRates = cvAmountDetailsforCE.filter(RCandRT);
        if(cvLineItemRates != null && !cvLineItemRates.isEmpty() ){
            for(int index =0; index < cvLineItemRates.size(); index++){
                AmountBean amountBean = (AmountBean) cvLineItemRates.get(index);
                equalsRC = new Equals(RATE_CLASS_CODE, new Integer(amountBean.getRateClassCode()));
                equalsRT = new Equals(RATE_TYPE_CODE, new Integer(amountBean.getRateTypeCode()));
                RCandRT = new And(equalsRC, equalsRT);
                CoeusVector isCollectionExist =  cvRateTypeCodeWithoutZero.filter(RCandRT);
                if(isCollectionExist != null && !isCollectionExist.isEmpty()){
                    cvLineItemRates.remove(index);
                }
                
            }
        }
        return cvLineItemRates;
    }
    
    // Added for COEUSQA-2393 Revamp Coeus Budget Engine - End
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
} // end BreakUpInterval



