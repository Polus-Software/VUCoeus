/*
 * @(#)BudgetCalculator.java October 14, 2003, 12:58 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.budget.calculator;

import edu.mit.coeus.budget.bean.BudgetPersonnelDetailsRateBaseBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.exception.CoeusException;
import java.util.*;
import edu.mit.coeus.utils.dbengine.DBException; // Enhancement ID : 709 Case 3

/**
 * <p>
 * This class is the starting point of budget calculation. Whenever a
 * request is received to calculate, this class needs to be invoked. Makes
 * use of LineItemCalculator to calculate the Line Items,
 * PersonnelLineItemCalculator for calculating personnel line items, and
 * SalaryCaculator for calculating person salary.
 * </p>
 * 
 * 
 */
public class BudgetCalculator {

  ///////////////////////////////////////
  // attributes

    /**
     * Represents the Budget Info Bean.
     */
    private BudgetInfoBean budgetInfoBean; 
    
    private String proposalNumber;

    /**
     * Represents Query Engine instance
     * 
     */
    private QueryEngine queryEngine = QueryEngine.getInstance();  

    /**
     * Represents key , Proposal Number + Version Number
     * 
     */
    private String key = "";  

    /**
     * Represents Line Item Calculator
     * 
     */
    private LineItemCalculator lineItemCalculator;   

    /**
     * Represents Personnel Line Item Calculator
     * 
     */
    private PersonnelLineItemCalculator personnelLineItemCalculator; 

    /**
     * Represents Vector containing rates not available messages
     */
    private Vector vecMessages = new CoeusVector(); 
    
    /**
     * Used for calculating Salary
     */
    private SalaryCalculator salaryCalculator;
    
    private static final int UR_RATE_TYPE_CODE = 1;
    
    //Enhancement ID : 709 Case 3 - Starts here
    /**
     * Used for Under-recovery calculation (enhancement)"
     */
    private boolean uRMatchesOh = false; 
    
    /**
     * holds UR Rate Class Code
     */
    private int uRRateClassCode = -1;
    
    /**
     * holds UR Rate Type Code
     */
    private int uRRateTypeCode = -1;
    
    /**
     * Enhancement ID : 709 Case 3
     * Flag which indicates whether the under-recovery rate base is present for 
     * the line item.
     */
    private boolean uRRateValidForCE = true;
    // Enhancement ID : 709 Case 3 - Ends Here
    
    private int budgetPeriod=-1;
    private CoeusVector cvPersonnelRateBase;
    CoeusVector cvRateBase = null;
   // Added for Case 2228 - Print Budget Summary - Start 
    private static char PERSONNEL_BUDGET_CATEGORY = 'P';     
    private static final String PERSON_ID = "999999999";    
    private static final String LA_JOB_CODE = "LA";    
    private static final String P_CATEGORY_JOB_CODE = "000000";
   // Added for Case 2228 - Print Budget Summary - End 
   // 3854: Warning in Lite when salary effective date not in place for a calculation
    private Vector vecSalaryMessages = new Vector();    
  ///////////////////////////////////////
  // operations


    /**
     * Constructor...
     * 
     * @param cvBudgetInfo 
     */
    public BudgetCalculator(BudgetInfoBean budgetInfoBean) {
        try {
            
            lineItemCalculator = new LineItemCalculator();
            personnelLineItemCalculator = new PersonnelLineItemCalculator();
            this.budgetInfoBean = budgetInfoBean;
            proposalNumber = budgetInfoBean.getProposalNumber();
            //Set the match flag if the Under-recovery Rate is same as OH Rate
            if (budgetInfoBean.getOhRateClassCode() == budgetInfoBean.getUrRateClassCode()) {
                lineItemCalculator.setURMatchesOh(true);
                personnelLineItemCalculator.setURMatchesOh(true);
                setURMatchesOh(true); //Enhancement ID : 709 Case 3
            } else {
                //Enhancement ID : 709 Case 3 - Starts here
                setURMatchesOh(false); //Enhancement ID : 709 Case 3
                setURRateClassCode(budgetInfoBean.getUrRateClassCode());
                setURRateTypeCode(UR_RATE_TYPE_CODE);
                //Enhancement ID : 709 Case 3 - Ends here
                
                lineItemCalculator.setURRateClassCode(budgetInfoBean.getUrRateClassCode());
                lineItemCalculator.setURRateTypeCode(UR_RATE_TYPE_CODE);
                
                personnelLineItemCalculator.setURRateClassCode(budgetInfoBean.getUrRateClassCode());
                personnelLineItemCalculator.setURRateTypeCode(UR_RATE_TYPE_CODE);
            }
            
        } catch (BudgetCalculateException budgetEx) {
            budgetEx.printStackTrace();
        }
        //key = budgetInfoBean.getProposalNumber() + budgetInfoBean.getVersionNumber();
    } // end BudgetCalculator        

    /**
     * - This is the starting point of Budget calculation.
     * - Use Query Engine to get all the Budget Periods
     * - Call CalculateBudgetPeriod method to do the calculation for a period
     * 
     */
    public void calculate() {
        try {
            CoeusVector cvBudgetPeriods = queryEngine.executeQuery(key, 
                        BudgetPeriodBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            /** Case Id #1811 - BudgetRateAndBase updation -start
             */
            queryEngine.addCollection(key,BudgetRateBaseBean.class,new CoeusVector());
            // case Id #1811 - end
            // Added for Case 2228 - Print Budget Summary - Start 
             queryEngine.addCollection(key,BudgetPersonnelDetailsRateBaseBean.class,new CoeusVector());
            // Added for Case 2228 - Print Budget Summary - End 
            BudgetPeriodBean budgetPeriodBean;

            //Loop through all the budget periods & calculate
            if (cvBudgetPeriods != null && cvBudgetPeriods.size() > 0) {
                int size = cvBudgetPeriods.size();
                for (int index = 0; index < size; index++) {
                    budgetPeriodBean = (BudgetPeriodBean) cvBudgetPeriods.get(index);
                    calculatePeriod(budgetPeriodBean.getBudgetPeriod());                    
                }
                
                //Refresh Budget Info Bean Total Costs
                //syncBudgetTotals();
            }
        } catch(CoeusException coeusEx) {
            coeusEx.printStackTrace();
        }
    } // end Calculate        

    /**
     * Does the calculation for line items in the Budget Period specified
     */
    public void calculatePeriod(int budgetPeriod) throws CoeusException {  
        this.budgetPeriod = budgetPeriod;
        Equals equals = new Equals("budgetPeriod", new Integer(budgetPeriod));
        CoeusVector cvLineItemDetails = queryEngine.getActiveData(key,
                                                BudgetDetailBean.class, equals);
        BudgetDetailBean budgetDetailBean;
        CoeusVector cvPersonnelLineItems;
        int lineItemNo = 0;
        Equals equalsBudgetPeriod = new Equals("budgetPeriod", new Integer(budgetPeriod));
        Equals equalsLineItem;
        And eQBudgetPeriodAndeQLineItem;
       
        //Loop through all the budget line item details & calculate
        if (cvLineItemDetails != null && cvLineItemDetails.size() > 0) {
            int size = cvLineItemDetails.size();
            for (int index = 0; index < size; index++) {
                budgetDetailBean = (BudgetDetailBean) cvLineItemDetails.get(index);
                
                //Enhancement ID : 709 Case 3 - Starts here
                //Check whether the cost element is having UR Rate
                setURRateValidForCE(checkURRateValidForCE(budgetDetailBean));
                //Enhancement ID : 709 Case 3 - Ends here
                
                //Check whether personnel line items are present
                lineItemNo = budgetDetailBean.getLineItemNumber();
                equalsLineItem = new Equals("lineItemNumber", new Integer(lineItemNo));
                eQBudgetPeriodAndeQLineItem = new And(equalsBudgetPeriod, equalsLineItem);
                
                cvPersonnelLineItems = queryEngine.getActiveData(key,
                                                BudgetPersonnelDetailsBean.class, eQBudgetPeriodAndeQLineItem);
                if (cvPersonnelLineItems != null && cvPersonnelLineItems.size() > 0) {
                    
                    //Enhancement ID : 709 Case 3 - Starts here
                    //calculatePersonnelLineItem(budgetPeriod, lineItemNo);
                    calculatePersonnelLineItem(budgetPeriod, lineItemNo,
                        budgetDetailBean.getCostElement());
                    // Case Id #1811 - start
                    filterPersonnelRateAndBase();
                    // Case Id 1811 - End
                    //Enhancement ID : 709 Case 3 - Ends here
                    
                } else {
                    //Its a non personnel line item
                    lineItemCalculator.setURRateValidForCE(isURRateValidForCE()); //Enhancement ID : 709 Case 3
                    lineItemCalculator.calculate(budgetDetailBean);
                   
                }
                //Added for case 2228-Print Budget Summary Enhancement - Start
                // Commented for Case 2228 - Print Budget Summary -Start
//                calculatePersonnelRateBase(budgetPeriod, lineItemNo,
//                        budgetDetailBean.getCostElement());
                // Commented for Case 2228 - Print Budget Summary -End
                //Added for case 2228-Print Budget Summary Enhancement - End
            }
            //Get the list of rates not available messages
            //vecMessages = lineItemCalculator.getVecMessages();
            if (lineItemCalculator.getVecMessages().size() > 0) {
                vecMessages.addAll(lineItemCalculator.getVecMessages());
                //Initialize the vec messages for the next period
                vecMessages.removeAllElements();
                //lineItemCalculator = new LineItemCalculator();
            }
        }
        //Refresh Budget Period costs
        syncBudgetTotals();
        //Added for Case 3197 - Allow for the generation of project period greater than 12 months - start
          calculatePeriodNumberOfMonths();
        //Added for Case 3197 - Allow for the generation of project period greater than 12 months - end
        
        //
    } // end CalculatePeriod   
    
    

    
    
    
    /** Filter the Budget Rate And Base data, if the line item is having personnel
     *line item. In the same Line item if it contain multiple personnel line
     *item then group it and then calculate the cost
     *Case Id #1811
     */
    private void filterPersonnelRateAndBase() throws CoeusException{
        if(cvPersonnelRateBase!= null && cvPersonnelRateBase.size() > 0){
            Equals eqRateClass;
            Equals eqRateType;
            Equals eqRateNumber;
            And eqRateClassAndeqRateType;
            And eqRateClassAndeqRateTypeAndeqRateNumber;
            int size =cvPersonnelRateBase.size();
            Equals eqPeriod = null;
            CoeusVector cvRateData = new CoeusVector();
            int index=0; 
            int rateNum = 1;
            while(index< size ){
                edu.mit.coeus.budget.bean.BudgetRateBaseBean bean = 
                    (edu.mit.coeus.budget.bean.BudgetRateBaseBean)cvPersonnelRateBase.get(0);
                
                eqRateClass = new Equals("rateClassCode",new Integer(bean.getRateClassCode()));
                eqRateType = new Equals("rateTypeCode",new Integer(bean.getRateTypeCode()));
               // eqRateNumber = new Equals("rateNumber",new Integer(bean.getRateNumber()));
                eqRateClassAndeqRateType = new And(eqRateClass,eqRateType);
               // eqRateClassAndeqRateTypeAndeqRateNumber = new And(eqRateClassAndeqRateType,eqRateNumber);
                // Filter based on applied rate not on rate number
                Equals eqRate = new Equals("appliedRate", new Double(bean.getAppliedRate()));
                And eqRateAndeqRateClassAndeqRateType = new And(eqRateClassAndeqRateType,eqRate);
                
                CoeusVector filterData = cvPersonnelRateBase.filter(eqRateAndeqRateClassAndeqRateType);
                double calculatedCost = filterData.sum("calculatedCost");
                double calculatedCostSharing = filterData.sum("calculatedCostSharing");
                // Getting the sum of base cost and base cost sharing - start step1
                double baseCost = filterData.sum("baseCost");
                double baseCostSharing = filterData.sum("baseCostSharing");
                // Getting the sum of base cost and base cost sharing - end step1
                bean.setCalculatedCost(calculatedCost);
                bean.setCalculatedCostSharing(calculatedCostSharing);
                bean.setRateNumber(rateNum++);
                // setting the sum of base cost and base cost sharing - start step2
                bean.setBaseCost(baseCost);
                bean.setBaseCostSharing(baseCostSharing);
                // setting the sum of base cost and base cost sharing - End step2
                cvRateData.addElement(bean);
                cvPersonnelRateBase.removeAll(filterData);
                size = cvPersonnelRateBase.size();
            }
            cvRateBase = queryEngine.getDetails(key,BudgetRateBaseBean.class);
            cvRateBase.addAll(cvRateData);
            queryEngine.addCollection(key,BudgetRateBaseBean.class,cvRateBase);
        }
    }// end filterPersonnelRateAndBase...
    
    private void filterRAB() throws CoeusException{
        if(cvPersonnelRateBase!= null){
            Equals eqRateClass;
            Equals eqRateType;
            Equals eqRateNumber;
            And eqRateClassAndeqRateType;
            And eqRateClassAndeqRateTypeAndeqRateNumber;
            int size =cvPersonnelRateBase.size();
            Equals eqPeriod = null;
            CoeusVector cvRateData = new CoeusVector();
            for(int index=0; index< size/2; index++){
                edu.mit.coeus.budget.bean.BudgetRateBaseBean bean = 
                    (edu.mit.coeus.budget.bean.BudgetRateBaseBean)cvPersonnelRateBase.get(index);
                
                eqRateClass = new Equals("rateClassCode",new Integer(bean.getRateClassCode()));
                eqRateType = new Equals("rateTypeCode",new Integer(bean.getRateTypeCode()));
                eqRateNumber = new Equals("rateNumber",new Integer(bean.getRateNumber()));
                eqRateClassAndeqRateType = new And(eqRateClass,eqRateType);
                eqRateClassAndeqRateTypeAndeqRateNumber = new And(eqRateClassAndeqRateType,eqRateNumber);
                
                CoeusVector filterData = cvPersonnelRateBase.filter(eqRateClassAndeqRateTypeAndeqRateNumber);
                //double baseCost = filterData.sum("baseCost");
                //double baseCostSharing = filterData.sum("baseCostSharing");
                double calculatedCost = filterData.sum("calculatedCost");
                double calculatedCostSharing = filterData.sum("calculatedCostSharing");
                
                //bean.setBaseCost(baseCost);
                //bean.setBaseCostSharing(baseCostSharing);
                bean.setCalculatedCost(calculatedCost);
                bean.setCalculatedCostSharing(calculatedCostSharing);
                bean.setRateNumber(index+1);
                cvRateData.addElement(bean);
            }
            cvRateBase = queryEngine.getDetails(key,BudgetRateBaseBean.class);
            cvRateBase.addAll(cvRateData);
            queryEngine.addCollection(key,BudgetRateBaseBean.class,cvRateBase);
        }
    }

    /**
     * Does salary calculation for the requested Line Item Person by using the
     * Salary Calculator
     */
    public void calculateSalary() throws CoeusException {  
        
        CoeusVector cvPersonnelLineItems = queryEngine.executeQuery(key, 
                    BudgetPersonnelDetailsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
        if (cvPersonnelLineItems != null && cvPersonnelLineItems.size() > 0) {
            BudgetPersonnelDetailsBean personnelDetailsBean = (BudgetPersonnelDetailsBean) 
                                                    cvPersonnelLineItems.get(0);
            
           //Initialize budget calculator
            salaryCalculator = new SalaryCalculator(personnelDetailsBean);
            salaryCalculator.distributeCalculatedSal();
            if(personnelDetailsBean.getAcType() == null) {
                personnelDetailsBean.setAcType("U");
            }
            //Update to QueryEngine
            queryEngine.update(key, personnelDetailsBean);
            
            //Get the Salary not available messages
            vecMessages = salaryCalculator.getVecMessages();
        }
    } // end calculateSalary        

    /**
     * Does Personnel Line Item calculation
     */
    
    //Enhancement ID : 709 Case 3 - Starts here
    //public void calculatePersonnelLineItem(int budgetPeriod, int lineItemNo) {
    public void calculatePersonnelLineItem(int budgetPeriod, int lineItemNo, 
        String costElement) {
    //Enhancement ID : 709 Case 3 - Ends here
        
        //Calculate all personnel line items
        Equals equalsBudgetPeriod = new Equals("budgetPeriod", new Integer(budgetPeriod));
        Equals equalsLineItem = new Equals("lineItemNumber", new Integer(lineItemNo));
        And eQBudgetPeriodAndeQLineItem = new And(equalsBudgetPeriod, equalsLineItem);
        try {
            CoeusVector cvPersonnelLineItems = queryEngine.getActiveData(key, 
                        BudgetPersonnelDetailsBean.class, eQBudgetPeriodAndeQLineItem);
            if (cvPersonnelLineItems != null && cvPersonnelLineItems.size() > 0) {
                int personnelLiSize = cvPersonnelLineItems.size();
                cvPersonnelRateBase = new CoeusVector();
                personnelLineItemCalculator.setCostElement(costElement); //Enhancement ID : 709 Case 3 - Starts here
                for (int index = 0; index < personnelLiSize; index++) {
                    BudgetPersonnelDetailsBean personnelDetailsBean = (BudgetPersonnelDetailsBean) 
                                                        cvPersonnelLineItems.get(index);
                    personnelLineItemCalculator.setURRateValidForCE(isURRateValidForCE()); //Enhancement ID : 709 Case 3
                    personnelLineItemCalculator.calculate(personnelDetailsBean);
                    // Case Id #1811 - start
                    if(personnelLineItemCalculator.getPersonalRateBase()!= null && 
                        personnelLineItemCalculator.getPersonalRateBase().size() >0){
                        cvPersonnelRateBase.addAll(personnelLineItemCalculator.getPersonalRateBase());
                    }
                    //case Id #1811 - End
                }
                syncPersonnelLineItems(eQBudgetPeriodAndeQLineItem);
                // 3854: Warning in Lite when salary effective date not in place for a calculation - Start
                if (personnelLineItemCalculator.getVecSalaryMessages().size() > 0) {
                    vecSalaryMessages.addAll(personnelLineItemCalculator.getVecSalaryMessages());
                }
                // 3854: Warning in Lite when salary effective date not in place for a calculation - End
                if (personnelLineItemCalculator.getVecMessages().size() > 0) {
                    vecMessages.addAll(personnelLineItemCalculator.getVecMessages());

                    //Initialize the vec messages for the next period
                    personnelLineItemCalculator = new PersonnelLineItemCalculator();
                }
            }
        } catch(CoeusException coeusEx) {
            coeusEx.printStackTrace();
        }
    } // end calculatePersonnelLineItem        

    /**
     * Does the synchronisation of all Personnel Line Item Details & Calculated Amounts to the
     * Budget Line Item Details & Cal Amounts
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
     * - Sum up all Total Cost Sharing for Personnel Details and then update the 
     * Total Cost Sharing of line item details.
     */
    private void syncPersonnelLineItems(Operator eQBudgetPeriodAndeQLineItem) throws CoeusException {   
        Equals equals;
        
        //Get the line item which needs to sync
        BudgetDetailBean budgetDetailBean = null;
        CoeusVector cvBudgetLineItems = queryEngine.getActiveData(key, 
                    BudgetDetailBean.class, eQBudgetPeriodAndeQLineItem);
        if (cvBudgetLineItems != null && cvBudgetLineItems.size() > 0) {
            budgetDetailBean = (BudgetDetailBean) cvBudgetLineItems.get(0);
        }
        
        //Get the line item cal amounts which needs to sync
        CoeusVector cvLineItemCalAmts = queryEngine.getActiveData(key, 
                    BudgetDetailCalAmountsBean.class, eQBudgetPeriodAndeQLineItem);
        
        //Get all the personnel line items for the current line item
        CoeusVector cvPersonnelLineItems = queryEngine.getActiveData(key, 
                BudgetPersonnelDetailsBean.class, eQBudgetPeriodAndeQLineItem);
        
        
        if (cvPersonnelLineItems != null && cvPersonnelLineItems.size() > 0) {
            CoeusVector cvPersonnelLiCalAmts = queryEngine.getActiveData(key, 
                BudgetPersonnelCalAmountsBean.class, eQBudgetPeriodAndeQLineItem);
        
            double totalRequestedSalary = 0;
            double totalCostSharing = 0;
            double totalCalculatedCost = 0;
            double totalCalculatedCostSharing = 0;
            double totalUnderRecovery = 0;
            double directCost = 0;
            double indirectCost = 0;
            
            //Sync to Budget Periods
            if (cvPersonnelLiCalAmts != null && cvPersonnelLiCalAmts.size() > 0) {
            
                int size = 0;
                int index = 0;
                int rateClassCode = 0;
                int rateTypeCode = 0;
                Equals equalsRC;
                Equals equalsRT;
                And RCandRT;
                BudgetDetailCalAmountsBean calAmtsBean;

                /**
                 *loop thru all cal amount rates, sum up the costs from personnel cal amts 
                 *and set it in line item cal amts
                 */
                size = cvLineItemCalAmts.size();
                for (index = 0; index < size; index++) {
                    calAmtsBean = (BudgetDetailCalAmountsBean) cvLineItemCalAmts.get(index);

                    rateClassCode = calAmtsBean.getRateClassCode();
                    rateTypeCode = calAmtsBean.getRateTypeCode();
                    equalsRC = new Equals("rateClassCode", new Integer(rateClassCode));
                    equalsRT = new Equals("rateTypeCode", new Integer(rateTypeCode));
                    RCandRT = new And(equalsRC, equalsRT);
                    
                    //totalCalculatedCost = cvPersonnelLiCalAmts.sum("calculatedCost", RCandRT);
                    
                    totalCalculatedCost = ((double)Math.round(cvPersonnelLiCalAmts.sum("calculatedCost", RCandRT)*
                        Math.pow(10.0, 2) )) / 100;
                    
                    
                    
                    calAmtsBean.setCalculatedCost(totalCalculatedCost);
                    totalCalculatedCostSharing = cvPersonnelLiCalAmts.sum("calculatedCostSharing", RCandRT);
                    calAmtsBean.setCalculatedCostSharing(totalCalculatedCostSharing);

                    //Update to queryEngine Data Collection
                    if (calAmtsBean.getAcType() == null) {
                        calAmtsBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                    queryEngine.update(key, calAmtsBean);
                }
            }

            /**
             * Sum up all the Requested Salary for Personnel Details and then update the 
             * line Item Cost of line item details.
             */       
            //totalRequestedSalary = cvPersonnelLineItems.sum("salaryRequested");
            // Bgu fix #1835 -start
            totalRequestedSalary = ((double)Math.round(cvPersonnelLineItems.sum("salaryRequested")*
                        Math.pow(10.0, 2) )) / 100;
            budgetDetailBean.setLineItemCost(totalRequestedSalary);
            // Bgu fix #1835 -End
            /**
             * Sum up all the CostSharing for Personnel Details and then update the 
             * CostSharing of line item details.
             */       
            totalCostSharing = cvPersonnelLineItems.sum("costSharingAmount");
            budgetDetailBean.setCostSharingAmount(totalCostSharing);

            /**
             * Sum up all the underRecovery costs for Personnel Details and then update the 
             * Underrecovery of line item details.
             */  
            //COEUSDEV:932 Error in Under recovery calculation in budget summary & distribution screen - Start
            //Rounding off the UnderRecoveryAmount for two decimal places.
//             totalUnderRecovery = cvPersonnelLineItems.sum("underRecoveryAmount");
            totalUnderRecovery = Math.round(cvPersonnelLineItems.sum("underRecoveryAmount") * 100.00) / 100.00;
            //COEUSDEV:932 - End          
            budgetDetailBean.setUnderRecoveryAmount(totalUnderRecovery);

            /**
             * Sum up all direct costs for Personnel Details and then update the 
             * DirectCost of line item details.
             */
            //directCost = cvPersonnelLineItems.sum("directCost");
            // Bgu fix #1835 -start
            directCost = ((double)Math.round(cvPersonnelLineItems.sum("directCost")*
                        Math.pow(10.0, 2) )) / 100;
            // Bgu fix #1835 -End
            
            budgetDetailBean.setDirectCost(directCost);
            
            //System.out.println("Personnel Direct cost is : "+directCost);

            /**
             * Sum up all Indirect costs for Personnel Details and then update the 
             * IndirectCost of line item details.
             */
            //indirectCost = cvPersonnelLineItems.sum("indirectCost");
            // Bgu fix #1835 -start
            indirectCost = ((double)Math.round(cvPersonnelLineItems.sum("indirectCost")*
                        Math.pow(10.0, 2) )) / 100;
            // Bgu fix #1835 -End
            budgetDetailBean.setIndirectCost(indirectCost);

            /**
             * Sum up all Total Cost Sharing for Personnel Details and then update the 
             * Total Cost Sharing of line item details.
             */
            totalCalculatedCostSharing = cvPersonnelLineItems.sum("totalCostSharing");
            budgetDetailBean.setTotalCostSharing(totalCalculatedCostSharing);
            //Update to queryEngine Data Collection
            if (budgetDetailBean.getAcType() == null) {
                budgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
            queryEngine.update(key, budgetDetailBean);
            
        }         
            
    } // end syncPersonnelLineItems        

    /**
     * Does the synchronisation of all Line Item Calculated Amounts to the
     * Budget Period Amount Totals & Budget Info Amount Totals
     */
    private void syncBudgetTotals() throws CoeusException {   
        Equals equals;
        CoeusVector cvBudgetPeriods = queryEngine.executeQuery(key, 
                        BudgetPeriodBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
        
        //Sync to Budget Periods
        if (cvBudgetPeriods != null && cvBudgetPeriods.size() > 0) {
            int periodSize = cvBudgetPeriods.size();
            BudgetPeriodBean budgetPeriodBean;
            int budgetPeriod = 0;
            for(int index = 0; index < periodSize; index++) {
                budgetPeriodBean = (BudgetPeriodBean) cvBudgetPeriods.get(index);
                budgetPeriod = budgetPeriodBean.getBudgetPeriod();
                equals = new Equals("budgetPeriod", new Integer(budgetPeriod));
                CoeusVector cvLineItemDetails = queryEngine.getActiveData(key,
                                                    BudgetDetailBean.class, equals);
                if (cvLineItemDetails != null && cvLineItemDetails.size() > 0) {
                    /** Added by chandra - To round the values and send to the client
                     *Aug 2004-10
                     */
                    double directCost = ((double)Math.round(cvLineItemDetails.sum("directCost")*
                        Math.pow(10.0, 2) )) / 100;
                    
                    double indirectCost = ((double)Math.round(cvLineItemDetails.sum("indirectCost")*
                        Math.pow(10.0, 2) )) / 100;
                    // Added by chandra to Fix #1228
                    //START FIX
                    
                    double totCostSharing = ((double)Math.round(cvLineItemDetails.sum("totalCostSharing")*
                        Math.pow(10.0, 2) )) / 100;
                     //END FIX by Geo
                    //COEUSDEV:932 - Error in Under recovery calculation in budget summary & distribution screen - Start
                    //Rounding off the UnderRecoveryAmount for two decimal places.
//                    double underRecovery = ((double)Math.round(cvLineItemDetails.sum("underRecoveryAmount")*
//                        Math.pow(10.0, 2) )) / 100;
                    double underRecovery = Math.round(cvLineItemDetails.sum("underRecoveryAmount") * 100.00) / 100.00;
                    //COEUSDEV:932 - End
                    
                    
                   // System.out.println("The total Direct of the Budget of the period is )))  : "+directCost);
                    budgetPeriodBean.setTotalDirectCost(directCost);
                    budgetPeriodBean.setTotalIndirectCost(indirectCost);
                    budgetPeriodBean.setCostSharingAmount(totCostSharing);
                    budgetPeriodBean.setUnderRecoveryAmount(underRecovery);
                    budgetPeriodBean.setTotalCost(directCost+indirectCost);
                    
//                    budgetPeriodBean.setTotalDirectCost(cvLineItemDetails.sum("directCost"));
//                    budgetPeriodBean.setTotalIndirectCost(cvLineItemDetails.sum("indirectCost"));
//                    budgetPeriodBean.setCostSharingAmount(cvLineItemDetails.sum("totalCostSharing"));
//                    budgetPeriodBean.setUnderRecoveryAmount(cvLineItemDetails.sum("underRecoveryAmount"));
//                    budgetPeriodBean.setTotalCost(budgetPeriodBean.getTotalDirectCost() + 
//                                                    budgetPeriodBean.getTotalIndirectCost());
                }// End Chandra
                //COEUSDEV:975 - Budget Summary not saving new entries - Start
                //Set budgetPeriodBean details to zero, if there is no Lineitems for Budget period.
//                else{
//                    budgetPeriodBean.setTotalDirectCost(0);
//                    budgetPeriodBean.setTotalIndirectCost(0);
//                    budgetPeriodBean.setCostSharingAmount(0);
//                    budgetPeriodBean.setUnderRecoveryAmount(0);
//                    budgetPeriodBean.setTotalCost(0);
//                }
                //COEUSDEV:975 - End
            
                //Update to queryEngine Data Collection
                if (budgetPeriodBean.getAcType() == null) {
                    budgetPeriodBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
                queryEngine.update(key, budgetPeriodBean);
            }
            
            
            //Sync to Budget Info
            budgetInfoBean.setTotalDirectCost(cvBudgetPeriods.sum("totalDirectCost"));
            budgetInfoBean.setTotalIndirectCost(cvBudgetPeriods.sum("totalIndirectCost"));
            double value = ((double)Math.round(cvBudgetPeriods.sum("costSharingAmount")*
                        Math.pow(10.0, 2) )) / 100;
            budgetInfoBean.setCostSharingAmount(((double)Math.round(
                cvBudgetPeriods.sum("costSharingAmount")* Math.pow(10.0, 2) )) / 100);
            budgetInfoBean.setUnderRecoveryAmount(((double)Math.round(
                cvBudgetPeriods.sum("underRecoveryAmount")* Math.pow(10.0, 2) )) / 100);
            budgetInfoBean.setTotalCost(cvBudgetPeriods.sum("totalCost"));
            
            //Update to queryEngine Data Collection
            if (budgetInfoBean.getAcType() == null) {
                budgetInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
            queryEngine.update(key, budgetInfoBean);
            
        }           
            
    } // end syncBudgetTotals         

    /**
     * Enhancement ID : 709 Case 3
     * Checks whether the Under-recovery rate is present for the Cost element.
     * If present, will return true, otherwise return false.
     */
    private boolean checkURRateValidForCE(BudgetDetailBean budgetDetailBean) {  
        CoeusVector cvValidOHRatesForCE = new CoeusVector();
        try {
            //Get all the valid OH Rates for this cost element
            BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
            cvValidOHRatesForCE = budgetDataTxnBean.getValidOHRateTypesForCE(budgetDetailBean.getCostElement());
        } catch(Exception exception) {
            exception.printStackTrace();
        }
        
        //Filter and get the OH rate corresponding to the UR Rate Base
        Equals equalsRC = new Equals("rateClassCode", new Integer(getURRateClassCode()));
       // Equals equalsRT = new Equals("rateTypeCode", new Integer(getURRateTypeCode()));// commented for COEUS-702
       // And RCandRT = new And(equalsRC, equalsRT);// commented for COEUS-702
        CoeusVector cvURRate = null;
        /** Added by chandra.
         *If n a budget if you adda line item with a cost element which is not mapped 
         *to any valid rate class rate type combination, then it should just calculate without
         *throwing any message
         * bug Fix #1142
         *10-Aug-2004
         */
        if(cvValidOHRatesForCE!= null && cvValidOHRatesForCE.size() > 0){
        
            cvURRate = cvValidOHRatesForCE.filter(equalsRC);
            if(cvURRate== null && cvURRate.size()==0){
                cvURRate = new CoeusVector();
            }
            int rateType = getURValidRateType(cvURRate);
            lineItemCalculator.setURRateTypeCode(rateType);
            personnelLineItemCalculator.setURRateTypeCode(rateType);
            setURRateTypeCode(rateType);           
            
        }// End Chandra 10-Aug-20004
        //If the UR rate available for the cost element, then calculate under-recovery
        if (cvURRate != null && cvURRate.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
    
     
    /*public static void main(String s[]) {
        QueryEngine queryEngine = QueryEngine.getInstance();
        TestCalculator test = new TestCalculator();
        //Hashtable budgetData = test.getAllBudgetData("01100555", 10);
        //String key = "01100555" + 10;
        //Hashtable budgetData = test.getAllBudgetData("01100693", 1);
        //String key = "01100693" + 1;
        //Hashtable budgetData = test.getAllBudgetData("01100675", 3);
        //String key = "01100675" + 3;
        Hashtable budgetData = test.getAllBudgetData("01100761", 1);
        String key = "01100761" + 1;
        BudgetInfoBean budgetInfoBean = new BudgetInfoBean();
        CoeusVector cvLineItemDetails;
        BudgetDetailBean budgetDetails = new BudgetDetailBean();
        try {
            queryEngine.addDataCollection(key, budgetData);
            //System.out.println("before");
            CoeusVector cvBudgetInfo = queryEngine.getDetails(key,BudgetInfoBean.class);
            budgetInfoBean = (BudgetInfoBean) cvBudgetInfo.get(0);
//            cvLineItemDetails = queryEngine.getDetails(key,BudgetDetailBean.class);
//            System.out.println("cvLineItemDetails.size() - >> " + cvLineItemDetails.size());
//            budgetDetails = (BudgetDetailBean) cvLineItemDetails.get(0);
//            budgetDetails.setLineItemCost(300);
//            budgetDetails.setCostSharingAmount(200);
//            budgetDetails.setAcType("U");
//            queryEngine.update(key, budgetDetails);
//            budgetDetails = (BudgetDetailBean) cvLineItemDetails.get(1);
//            budgetDetails.setLineItemCost(1500);
//            budgetDetails.setCostSharingAmount(1000);
//            budgetDetails.setAcType("U");
//            queryEngine.update(key, budgetDetails);
            BudgetCalculator budgetCalculator = new BudgetCalculator(budgetInfoBean);
            budgetCalculator.setKey(key);
            budgetCalculator.calculate();
            
            //print Budget Info
            cvBudgetInfo = queryEngine.getDetails(key,BudgetInfoBean.class);
            for(int i=0; i < cvBudgetInfo.size(); i++) {
                budgetInfoBean = (BudgetInfoBean) cvBudgetInfo.get(i);
                //System.out.println("budgetInfoBean"+i+" >>> "+ budgetInfoBean.toString());
            }
            
            //print Budget Periods
            CoeusVector cvBudgetPeriods = queryEngine.getDetails(key,BudgetPeriodBean.class);
            for(int i=0; i < cvBudgetPeriods.size(); i++) {
                BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean) cvBudgetPeriods.get(i);
                //System.out.println("BudgetPeriodBean"+i+" >>> "+ budgetPeriodBean.toString());
            }
            
            //print Budget Details
            cvLineItemDetails = queryEngine.getDetails(key,BudgetDetailBean.class);
            for(int i=0; i < cvLineItemDetails.size(); i++) {
                budgetDetails = (BudgetDetailBean) cvLineItemDetails.get(i);
                //System.out.println("budgetDetails"+i+" >>> "+ budgetDetails.toString());
            }
            
            //print Budget Details
            CoeusVector cvPersonnelLineItems = queryEngine.getDetails(key,BudgetPersonnelDetailsBean.class);
            BudgetPersonnelDetailsBean personnelDetailsBean;
            for(int i=0; i < cvPersonnelLineItems.size(); i++) {
                personnelDetailsBean = (BudgetPersonnelDetailsBean) cvPersonnelLineItems.get(i);
                //System.out.println("personnelDetailsBean"+i+" >>> "+ personnelDetailsBean.toString());
            }
            
            //print Budget Line Item Cal Amts
            CoeusVector cvLineItemCalAmts = queryEngine.getDetails(key,BudgetDetailCalAmountsBean.class);
            BudgetDetailCalAmountsBean calAmtsBean;
            for(int i=0; i < cvLineItemCalAmts.size(); i++) {
                calAmtsBean = (BudgetDetailCalAmountsBean) cvLineItemCalAmts.get(i);
                //System.out.println("calAmtsBean"+i+" >>> "+ calAmtsBean.toString());
            }
            //System.out.println("Budget Calculation Over");
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
    
    
    
    public CoeusVector getBudgetPeriods() throws CoeusException{
         CoeusVector cvBudgetPeriods = queryEngine.executeQuery(key, 
                        BudgetPeriodBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            return cvBudgetPeriods;
    }
    
    public CoeusVector getLineItem() throws CoeusException{
        Equals equals = new Equals("budgetPeriod", new Integer(budgetPeriod));
         CoeusVector cvLineItemDetails = queryEngine.getActiveData(key, BudgetDetailBean.class, equals);
         return cvLineItemDetails;
    }
    
    public String getProposalNumber() throws CoeusException{
        return proposalNumber;
    }
    
      //Added for case 2228-Print Budget Summary Enhancement - Start
    //This method is used to Calcuate the Personnel Details Rate and Base Calcualtion and save the data to OSP$BUD_PER_DET_RATE_AND_BASE
    
    public void calculatePersonnelRateBase(int budgetPeriod, int lineItemNo,
            String costElement) throws CoeusException {
        
        CoeusVector cvRateBase = new CoeusVector();
        CoeusVector cvRateData = new CoeusVector();
        BudgetPersonnelDetailsRateBaseBean budgetPersonnelDetailsRateBaseBean = null;
        //Calculate all personnel line items
        Equals equalsBudgetPeriod = new Equals("budgetPeriod", new Integer(budgetPeriod));
        Equals equalsLineItem = new Equals("lineItemNumber", new Integer(lineItemNo));
        And eQBudgetPeriodAndeQLineItem = new And(equalsBudgetPeriod, equalsLineItem);
        
        //Get the line item
        BudgetDetailBean budgetDetailBean = null;
        CoeusVector cvBudgetLineItems = queryEngine.getActiveData(key,
                BudgetDetailBean.class, eQBudgetPeriodAndeQLineItem);
        if (cvBudgetLineItems != null && cvBudgetLineItems.size() > 0) {
            budgetDetailBean = (BudgetDetailBean) cvBudgetLineItems.get(0);
        }
        
        //Get the line item cal amounts for current line item
        CoeusVector cvLineItemCalAmts = queryEngine.getActiveData(key,
                BudgetDetailCalAmountsBean.class, eQBudgetPeriodAndeQLineItem);
        
        //Get all the personnel line items for the current line item
        CoeusVector cvPersonnelLineItems = queryEngine.getActiveData(key,
                BudgetPersonnelDetailsBean.class, eQBudgetPeriodAndeQLineItem);
        
        // Get the rate and base value for the current line item
        CoeusVector cvBudgetRateAndBase = queryEngine.getActiveData(key,
                BudgetRateBaseBean.class, eQBudgetPeriodAndeQLineItem);
        
        // Check Personnel Budget Category for all the line items
        Equals eqBudgetCategory = new Equals("categoryType", new Character(PERSONNEL_BUDGET_CATEGORY));
        And eqBudgetCategoryAndeQBudgetPeriodAndeQLineItem = new And(eqBudgetCategory,eQBudgetPeriodAndeQLineItem);
        
        Equals eqEBRateClassCode = new Equals("rateClassCode", new Integer(5));
        Equals eqEBRateClassType = new Equals("rateTypeCode", new Integer(3));
        Equals eqVacRateClassCode = new Equals("rateClassCode", new Integer(8));
        Equals eqVacRateClassType = new Equals("rateTypeCode", new Integer(2));
        Equals eqLARateClassCode = new Equals("rateClassCode", new Integer(10));
        Equals eqLARateClassType = new Equals("rateTypeCode", new Integer(1));
        
        And eqEBRateClassCodeAndeqEBRateClassType = new And(eqEBRateClassCode,eqEBRateClassType);
        And eqVacRateClassCodeAndeqVacRateClassType = new And(eqVacRateClassCode,eqVacRateClassType);
        And eqLARateClassCodeAndeqLARateClassType = new And(eqLARateClassCode,eqLARateClassType);
        Or eqEBRateClassAndTypeOreqVacRateClassAndeqType = new Or(eqEBRateClassCodeAndeqEBRateClassType,eqVacRateClassCodeAndeqVacRateClassType);
        Or eqLARateClassAndTypeOreqEBRateClassAndTypeOreqVacRateClassAndeqType = new Or(eqLARateClassCodeAndeqLARateClassType,eqEBRateClassAndTypeOreqVacRateClassAndeqType);
               
        //Get the Line Item details for Personnel Category, if this line item is belongs to PERSONNEL LINE ITEM but WITH OUT ANY PERSONS (with out having any
        //personnel budgets ), add person id as 999999999 , Job Code as 000000, Percent Charged as 100% and Percent Effort as 100%
        CoeusVector cvLIDetails = queryEngine.executeQuery(key, BudgetDetailBean.class, eqBudgetCategoryAndeQBudgetPeriodAndeQLineItem);
        boolean PER_LI_WITHOUT_PERSON = false;
        HashMap hmSalaryData = new HashMap();
        
        if ( cvLIDetails !=null && cvLIDetails.size() > 0 && cvPersonnelLineItems == null || cvPersonnelLineItems.isEmpty()){
            //  int rateNumber = 1;
            //Create a new personnel edi bean
            int personRateNum = 1;
            budgetPersonnelDetailsRateBaseBean = new BudgetPersonnelDetailsRateBaseBean();
            for(int detailIndex = 0; detailIndex < cvLIDetails.size() ; detailIndex++){
                //Set the values in personnel edi bean
                BudgetDetailBean   budgetLIDetailBean = (BudgetDetailBean)cvLIDetails.get(detailIndex);
                budgetPersonnelDetailsRateBaseBean.setProposalNumber(budgetLIDetailBean.getProposalNumber());
                budgetPersonnelDetailsRateBaseBean.setVersionNumber(budgetLIDetailBean.getVersionNumber());
                budgetPersonnelDetailsRateBaseBean.setBudgetPeriod(budgetLIDetailBean.getBudgetPeriod());
                budgetPersonnelDetailsRateBaseBean.setLineItemNumber(budgetLIDetailBean.getLineItemNumber());
                budgetPersonnelDetailsRateBaseBean.setPersonNumber(1);
                budgetPersonnelDetailsRateBaseBean.setRateNumber(personRateNum++);
                budgetPersonnelDetailsRateBaseBean.setPersonId(PERSON_ID);
                
                budgetPersonnelDetailsRateBaseBean.setStartDate(budgetLIDetailBean.getLineItemStartDate());
                budgetPersonnelDetailsRateBaseBean.setEndDate(budgetLIDetailBean.getLineItemEndDate());
                budgetPersonnelDetailsRateBaseBean.setRateClassCode(0);
                budgetPersonnelDetailsRateBaseBean.setRateTypeCode(0);
                budgetPersonnelDetailsRateBaseBean.setOnOffCampusFlag(budgetLIDetailBean.isOnOffCampusFlag());
                budgetPersonnelDetailsRateBaseBean.setAppliedRate(0);
                budgetPersonnelDetailsRateBaseBean.setSalaryRequested(budgetLIDetailBean.getLineItemCost());
                
                hmSalaryData.put(new Integer(budgetLIDetailBean.getLineItemNumber()), new Double(budgetLIDetailBean.getLineItemCost()));
                budgetPersonnelDetailsRateBaseBean.setBaseCostSharing(0.00);
                budgetPersonnelDetailsRateBaseBean.setCalculatedCost(budgetLIDetailBean.getLineItemCost());
                budgetPersonnelDetailsRateBaseBean.setCalculatedCostSharing(budgetLIDetailBean.getCostSharingAmount());
                budgetPersonnelDetailsRateBaseBean.setAcType(TypeConstants.INSERT_RECORD);                
                budgetPersonnelDetailsRateBaseBean.setJobCode(P_CATEGORY_JOB_CODE);
                PER_LI_WITHOUT_PERSON = true;
                //            budgetPersonnelDetailsRateBaseBean.setAcType(TypeConstants.INSERT_RECORD);
                cvRateData.addElement(budgetPersonnelDetailsRateBaseBean);
            }
            //Adding Rates to PERSONNEL LINE ITEM but WITH OUT ANY PERSONS (with out having any
            //personnel budgets )
            if(cvLineItemCalAmts !=null && cvLineItemCalAmts.size() > 0 && PER_LI_WITHOUT_PERSON){
                int rateNumber = 1;
                HashMap hmLASalData = new HashMap();
                BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
                //filter Rate Class code is 10 and Rate TYpe Code is 1 - LA - Salaries
                CoeusVector cvLASalData =   cvLineItemCalAmts.filter(eqLARateClassCodeAndeqLARateClassType);
                
                if(cvLASalData !=null && cvLASalData.size() > 0){
                    for(int salIndex =0; salIndex < cvLASalData.size(); salIndex++){
                        budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean) cvLASalData.get(salIndex);
                        hmLASalData.put(new Integer(budgetDetailCalAmountsBean.getLineItemNumber()), new Double(budgetDetailCalAmountsBean.getCalculatedCost()));
                    }
                }
                
                int personNumber = 1;
                for(int index2 =0; index2 < cvLineItemCalAmts.size(); index2++){
                    budgetPersonnelDetailsRateBaseBean = new BudgetPersonnelDetailsRateBaseBean();
                    budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean) cvLineItemCalAmts.get(index2);
                    budgetPersonnelDetailsRateBaseBean.setProposalNumber(budgetDetailCalAmountsBean.getProposalNumber());
                    budgetPersonnelDetailsRateBaseBean.setVersionNumber(budgetDetailCalAmountsBean.getVersionNumber());
                    budgetPersonnelDetailsRateBaseBean.setBudgetPeriod(budgetDetailCalAmountsBean.getBudgetPeriod());
                    budgetPersonnelDetailsRateBaseBean.setLineItemNumber(budgetDetailCalAmountsBean.getLineItemNumber());
                    budgetPersonnelDetailsRateBaseBean.setPersonNumber(1);
                    budgetPersonnelDetailsRateBaseBean.setRateNumber(1);
                    
                    //If Rate Class Code is 10 and Rate Type Code = 1 (Lab Allocation -Salaries), set the Person Id as 9999999LA, Otherwise set 0
                    if (budgetDetailCalAmountsBean.getRateClassCode() == 10 && budgetDetailCalAmountsBean.getRateTypeCode() == 1 ){
                        budgetPersonnelDetailsRateBaseBean.setPersonId(PERSON_ID);
                        budgetPersonnelDetailsRateBaseBean.setJobCode(LA_JOB_CODE);
                        budgetPersonnelDetailsRateBaseBean.setPersonNumber(2);
                        //Set Base Cost as Person Salary, if Rate Class Code is 10 and Rate Type Code is 1
                        Double salaryReq = (Double)hmSalaryData.get(new Integer(budgetDetailCalAmountsBean.getLineItemNumber()));
                        budgetPersonnelDetailsRateBaseBean.setBaseCost(salaryReq.doubleValue());
                    }else{
                        budgetPersonnelDetailsRateBaseBean.setPersonId("0");
                    }
                    
                    if(cvBudgetLineItems !=null && cvBudgetLineItems.size() > 0){
                        BudgetDetailBean budgetDetailsBean = (BudgetDetailBean) cvBudgetLineItems.get(0);
                        // budgetPersonnelDetailsRateBaseBean.setPersonId(budgetPersonnelDetailsBean.getPersonId());
                        budgetPersonnelDetailsRateBaseBean.setStartDate(budgetDetailsBean.getLineItemStartDate());
                        budgetPersonnelDetailsRateBaseBean.setEndDate(budgetDetailsBean.getLineItemEndDate());
                        budgetPersonnelDetailsRateBaseBean.setOnOffCampusFlag(budgetDetailsBean.isOnOffCampusFlag());
                    }
                    
                    
                    budgetPersonnelDetailsRateBaseBean.setRateClassCode(budgetDetailCalAmountsBean.getRateClassCode());
                    budgetPersonnelDetailsRateBaseBean.setRateTypeCode(budgetDetailCalAmountsBean.getRateTypeCode());
                    //If RateClass is 5 and Ratetype is 3 OR RateClass is 8 and RateType is 2 set the PersonNumber as 2
                    if((budgetDetailCalAmountsBean.getRateClassCode() == 5 && budgetDetailCalAmountsBean.getRateTypeCode() == 3)
                    || (budgetDetailCalAmountsBean.getRateClassCode() == 8 && budgetDetailCalAmountsBean.getRateTypeCode() == 2) ){
                        budgetPersonnelDetailsRateBaseBean.setPersonNumber(2);
                        budgetPersonnelDetailsRateBaseBean.setJobCode("EV");
                    }
                    //Based on RateCass and Ratetype extract the Applied rate and Base Cost sharing data from BudgetRateBase bean
                    Equals eqEBRateClassCode2 = new Equals("rateClassCode", new Integer(budgetDetailCalAmountsBean.getRateClassCode()));
                    Equals eqEBRateClassType2 = new Equals("rateTypeCode", new Integer(budgetDetailCalAmountsBean.getRateTypeCode()));
                    And eqEBRateClassCodeAndeqEBRateClassType2 = new And(eqEBRateClassCode2,eqEBRateClassType2);
                    
                    CoeusVector cvfilterRateAndBase = cvBudgetRateAndBase.filter(eqEBRateClassCodeAndeqEBRateClassType2);
                    
                    if(cvfilterRateAndBase !=null && cvfilterRateAndBase.size() > 0){
                        BudgetRateBaseBean budgetRateBaseBean = (BudgetRateBaseBean) cvfilterRateAndBase.get(0);
                        budgetPersonnelDetailsRateBaseBean.setAppliedRate(budgetRateBaseBean.getAppliedRate());
                        budgetPersonnelDetailsRateBaseBean.setBaseCostSharing(budgetRateBaseBean.getBaseCostSharing());
                    }
                    //If  Rate Class Code is 5 and Rate Type Code is 1 , Set the Person Salary to Salary Requested.
                    if((budgetDetailCalAmountsBean.getRateClassCode() == 5 && budgetDetailCalAmountsBean.getRateTypeCode() == 1) ||
                             (budgetDetailCalAmountsBean.getRateClassCode() == 1 && budgetDetailCalAmountsBean.getRateTypeCode() == 1) ||
                            (budgetDetailCalAmountsBean.getRateClassCode() == 8 && budgetDetailCalAmountsBean.getRateTypeCode() == 1) ||
                            (budgetDetailCalAmountsBean.getRateClassCode() == 9 && budgetDetailCalAmountsBean.getRateTypeCode() == 1) ||
                            (budgetDetailCalAmountsBean.getRateClassCode() == 11 && budgetDetailCalAmountsBean.getRateTypeCode() == 1) ||
                            (budgetDetailCalAmountsBean.getRateClassCode() == 12 && budgetDetailCalAmountsBean.getRateTypeCode() == 1)){
                        Double salaryReq = (Double)hmSalaryData.get(new Integer(budgetDetailCalAmountsBean.getLineItemNumber()));
                        budgetPersonnelDetailsRateBaseBean.setSalaryRequested(salaryReq.doubleValue());
                    }//If  Rate Class Code is 5 and Rate Type Code is 3 , Set the LA -Salaries (10 / 1)cost to Salary Requested.
                    else if((budgetDetailCalAmountsBean.getRateClassCode() == 5 && budgetDetailCalAmountsBean.getRateTypeCode() == 3) ||
                            (budgetDetailCalAmountsBean.getRateClassCode() == 8 && budgetDetailCalAmountsBean.getRateTypeCode() == 2)){
                        Double laSalReq = (Double)hmLASalData.get(new Integer(budgetDetailCalAmountsBean.getLineItemNumber()));
                        budgetPersonnelDetailsRateBaseBean.setSalaryRequested(laSalReq.doubleValue());
                    } else{
                        budgetPersonnelDetailsRateBaseBean.setSalaryRequested(budgetDetailCalAmountsBean.getCalculatedCost());
                    }
                    
                    
                    budgetPersonnelDetailsRateBaseBean.setCalculatedCost(budgetDetailCalAmountsBean.getCalculatedCost());
                    budgetPersonnelDetailsRateBaseBean.setCalculatedCostSharing(budgetDetailCalAmountsBean.getCalculatedCostSharing());
                    
                    budgetPersonnelDetailsRateBaseBean.setAcType(TypeConstants.INSERT_RECORD);
                    cvRateData.addElement(budgetPersonnelDetailsRateBaseBean);
                }
                
            }
        }// END OF  PERSONNEL LINE ITEM but WITH OUT ANY PERSONS (with out having any personnel budgets )
        
        
        HashMap hmPerSalData = new HashMap();
        //Get all the PERSONNEL Cal amounts for the current line item
        if (cvPersonnelLineItems != null && cvPersonnelLineItems.size() > 0) {
            
            CoeusVector cvPersonnelLiCalAmts = queryEngine.getActiveData(key,
                    BudgetPersonnelCalAmountsBean.class, eQBudgetPeriodAndeQLineItem);
            
            
            //Prepare the data to include SALARY REQUESTED for every PERSON - ADDING SALARY REQUEST FOR EVERY PERSONS
            for(int personIndex = 0 ; personIndex < cvPersonnelLineItems.size(); personIndex++){
                budgetPersonnelDetailsRateBaseBean = new BudgetPersonnelDetailsRateBaseBean();
                BudgetPersonnelDetailsBean budgetPerDetailsBean = (BudgetPersonnelDetailsBean) cvPersonnelLineItems.get(personIndex);
                budgetPersonnelDetailsRateBaseBean.setProposalNumber(budgetPerDetailsBean.getProposalNumber());
                budgetPersonnelDetailsRateBaseBean.setVersionNumber(budgetPerDetailsBean.getVersionNumber());
                budgetPersonnelDetailsRateBaseBean.setBudgetPeriod(budgetPerDetailsBean.getBudgetPeriod());
                budgetPersonnelDetailsRateBaseBean.setLineItemNumber(budgetPerDetailsBean.getLineItemNumber());
                budgetPersonnelDetailsRateBaseBean.setPersonNumber(budgetPerDetailsBean.getPersonNumber());
                budgetPersonnelDetailsRateBaseBean.setRateNumber(1);
                budgetPersonnelDetailsRateBaseBean.setPersonId(budgetPerDetailsBean.getPersonId());
                budgetPersonnelDetailsRateBaseBean.setStartDate(budgetPerDetailsBean.getStartDate());
                budgetPersonnelDetailsRateBaseBean.setEndDate(budgetPerDetailsBean.getEndDate());
                budgetPersonnelDetailsRateBaseBean.setRateClassCode(0);
                budgetPersonnelDetailsRateBaseBean.setRateTypeCode(0);
                budgetPersonnelDetailsRateBaseBean.setOnOffCampusFlag(budgetPerDetailsBean.isOnOffCampusFlag());
                budgetPersonnelDetailsRateBaseBean.setAppliedRate(0);
                String key = ""+budgetPerDetailsBean.getLineItemNumber()+""+budgetPerDetailsBean.getPersonNumber();
                hmPerSalData.put(key,new Double(budgetPerDetailsBean.getSalaryRequested()));
                budgetPersonnelDetailsRateBaseBean.setSalaryRequested(budgetPerDetailsBean.getSalaryRequested());
                budgetPersonnelDetailsRateBaseBean.setBaseCostSharing(0);
                
                budgetPersonnelDetailsRateBaseBean.setCalculatedCost(budgetPerDetailsBean.getSalaryRequested());
                budgetPersonnelDetailsRateBaseBean.setCalculatedCostSharing(budgetPerDetailsBean.getCostSharingAmount());
                budgetPersonnelDetailsRateBaseBean.setJobCode(budgetPerDetailsBean.getJobCode());
                budgetPersonnelDetailsRateBaseBean.setAcType(TypeConstants.INSERT_RECORD);
                cvRateData.addElement(budgetPersonnelDetailsRateBaseBean);
            }
            
            
            
            //Prepare the data for Budget PERSONNEL RATE AND BASE bean EXCEPT 5 & 3 , 8 & 2 and 10 & 1.
            NotEquals neqEBRateClassCode = new NotEquals("rateClassCode", new Integer(5));
            NotEquals neqEBRateClassType = new NotEquals("rateTypeCode", new Integer(3));
            NotEquals neqVacRateClassCode = new NotEquals("rateClassCode", new Integer(8));
            NotEquals neqVacRateClassType = new NotEquals("rateTypeCode", new Integer(2));
            NotEquals neqLARateClassCode = new NotEquals("rateClassCode", new Integer(10));
            NotEquals neqLARateClassType = new NotEquals("rateTypeCode", new Integer(1));
            
            And neqEBRateClassCodeAndneqEBRateClassType = new And(neqEBRateClassCode,neqEBRateClassType);
            And neqVacRateClassCodeAndneqVacRateClassType = new And(neqVacRateClassCode,neqVacRateClassType);
            And neqLARateClassCodeAndneqLARateClassType = new And(neqLARateClassCode,neqLARateClassType);
            Or neqEBRateClassAndTypeOrneqVacRateClassAndeqType = new Or(neqEBRateClassCodeAndneqEBRateClassType,neqVacRateClassCodeAndneqVacRateClassType);
            Or neqLARateClassAndTypeOrneqEBRateClassAndTypeOreqVacRateClassAndeqType = new Or(neqLARateClassCodeAndneqLARateClassType,neqEBRateClassAndTypeOrneqVacRateClassAndeqType);
            
            int maxPersonnelPersonNumber = 0;
            Vector vecPersonNumData = new Vector();
            // Vector vecPerDetData = new Vector();
            CoeusVector cvPerLiCalAmts =  cvPersonnelLiCalAmts.filter(neqLARateClassAndTypeOrneqEBRateClassAndTypeOreqVacRateClassAndeqType);
            if(cvPerLiCalAmts !=null && cvPerLiCalAmts.size() > 0){
                int rateNum = 1;
                
                for(int index =0; index < cvPerLiCalAmts.size(); index++){
                    budgetPersonnelDetailsRateBaseBean = new BudgetPersonnelDetailsRateBaseBean();
                    BudgetPersonnelCalAmountsBean budgetPersonnelCalAmountsBean = (BudgetPersonnelCalAmountsBean) cvPerLiCalAmts.get(index);
                    if((budgetPersonnelCalAmountsBean.getRateClassCode() == 5 && budgetPersonnelCalAmountsBean.getRateTypeCode() == 3)
                    || (budgetPersonnelCalAmountsBean.getRateClassCode() == 8 && budgetPersonnelCalAmountsBean.getRateTypeCode() == 2)
                    || (budgetPersonnelCalAmountsBean.getRateClassCode() == 10 && budgetPersonnelCalAmountsBean.getRateTypeCode() == 1 )){
                        continue;
                    }
                    budgetPersonnelDetailsRateBaseBean.setProposalNumber(budgetPersonnelCalAmountsBean.getProposalNumber());
                    budgetPersonnelDetailsRateBaseBean.setVersionNumber(budgetPersonnelCalAmountsBean.getVersionNumber());
                    budgetPersonnelDetailsRateBaseBean.setBudgetPeriod(budgetPersonnelCalAmountsBean.getBudgetPeriod());
                    budgetPersonnelDetailsRateBaseBean.setLineItemNumber(budgetPersonnelCalAmountsBean.getLineItemNumber());
//                    budgetPersonnelDetailsRateBaseBean.setPersonNumber(1);
                    budgetPersonnelDetailsRateBaseBean.setPersonNumber(budgetPersonnelCalAmountsBean.getPersonNumber());
                    budgetPersonnelDetailsRateBaseBean.setRateNumber(1);
                    //Set the Person number to Vector and MaxPersonnelPersonNumber
                    
                    if(maxPersonnelPersonNumber != budgetPersonnelCalAmountsBean.getPersonNumber()){
                        vecPersonNumData.addElement(new Integer(budgetPersonnelCalAmountsBean.getPersonNumber()));
                        maxPersonnelPersonNumber = budgetPersonnelCalAmountsBean.getPersonNumber();
                    }
                    
                    //Set the RateClass and Type
                    budgetPersonnelDetailsRateBaseBean.setRateClassCode(budgetPersonnelCalAmountsBean.getRateClassCode());
                    budgetPersonnelDetailsRateBaseBean.setRateTypeCode(budgetPersonnelCalAmountsBean.getRateTypeCode());
                    
                    
                    //If RateClass and Ratetype is any one of the condition, set the Person Salary as Salary Requested
                    if((budgetPersonnelCalAmountsBean.getRateClassCode() == 5 && budgetPersonnelCalAmountsBean.getRateTypeCode() == 1) ||
                            (budgetPersonnelCalAmountsBean.getRateClassCode() == 1 && budgetPersonnelCalAmountsBean.getRateTypeCode() == 1) ||
                            (budgetPersonnelCalAmountsBean.getRateClassCode() == 8 && budgetPersonnelCalAmountsBean.getRateTypeCode() == 1) ||
                            (budgetPersonnelCalAmountsBean.getRateClassCode() == 9 && budgetPersonnelCalAmountsBean.getRateTypeCode() == 1) ||
                            (budgetPersonnelCalAmountsBean.getRateClassCode() == 11 && budgetPersonnelCalAmountsBean.getRateTypeCode() == 1) ||
                            (budgetPersonnelCalAmountsBean.getRateClassCode() == 12 && budgetPersonnelCalAmountsBean.getRateTypeCode() == 1)){
                        String key = ""+budgetPersonnelCalAmountsBean.getLineItemNumber()+""+budgetPersonnelCalAmountsBean.getPersonNumber();
                        if( hmPerSalData.get(key) !=null){
                            budgetPersonnelDetailsRateBaseBean.setSalaryRequested( ((Double)hmPerSalData.get(key)).doubleValue() );
                        }
                    } else{
                        budgetPersonnelDetailsRateBaseBean.setSalaryRequested(budgetPersonnelCalAmountsBean.getCalculatedCost());
                    }
                    
                    
                    budgetPersonnelDetailsRateBaseBean.setCalculatedCost(budgetPersonnelCalAmountsBean.getCalculatedCost());
                    budgetPersonnelDetailsRateBaseBean.setCalculatedCostSharing(budgetPersonnelCalAmountsBean.getCalculatedCostSharing());
                    
                    budgetPersonnelDetailsRateBaseBean.setAcType(TypeConstants.INSERT_RECORD);
                    
                    Equals eqPersonNumber = new Equals("personNumber", new Integer(budgetPersonnelCalAmountsBean.getPersonNumber()));
                    And eQBudgetPeriodAndeQLineItemAndeQPersonNumber = new And(eQBudgetPeriodAndeQLineItem,eqPersonNumber);
                    
                    CoeusVector cvPersonnelLineItemsForPerson = queryEngine.getActiveData(key,
                            BudgetPersonnelDetailsBean.class, eQBudgetPeriodAndeQLineItemAndeQPersonNumber);
                    
                    if(cvPersonnelLineItemsForPerson !=null && cvPersonnelLineItemsForPerson.size() > 0){
                        BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean) cvPersonnelLineItemsForPerson.get(0);
                        budgetPersonnelDetailsRateBaseBean.setStartDate(budgetPersonnelDetailsBean.getStartDate());
                        budgetPersonnelDetailsRateBaseBean.setEndDate(budgetPersonnelDetailsBean.getEndDate());
                        budgetPersonnelDetailsRateBaseBean.setOnOffCampusFlag(budgetPersonnelDetailsBean.isOnOffCampusFlag());
                    }
                    
                    //If Rate Class Code is 10 and Rate Type Code = 1 (Lab Allocation -Salaries), set the Person Id as 9999999LA, Otherwise set 0
                    if (budgetPersonnelCalAmountsBean.getRateClassCode() == 10 && budgetPersonnelCalAmountsBean.getRateTypeCode() == 1 ){
                        budgetPersonnelDetailsRateBaseBean.setPersonId(PERSON_ID);
                        budgetPersonnelDetailsRateBaseBean.setJobCode(LA_JOB_CODE);
                        budgetPersonnelDetailsRateBaseBean.setPersonNumber(2);
                    }else{
                        budgetPersonnelDetailsRateBaseBean.setPersonId("0");
                    }
                    
                    //applied rate
                    Equals eqRateClass = new Equals("rateClassCode",new Integer(budgetPersonnelCalAmountsBean.getRateClassCode()));
                    Equals eqRateType = new Equals("rateTypeCode",new Integer(budgetPersonnelCalAmountsBean.getRateTypeCode()));
                    
                    And eqRateClassAndeqRateType = new And(eqRateClass,eqRateType);
                    CoeusVector cvfilterRateBase = cvBudgetRateAndBase.filter(eqRateClassAndeqRateType);
                    double appliedRate = 0.0;
                    double salaryRequested = 0.0;
                    double baseCostSharing = 0.0;
                    double calculatedCost = 0.0;
                    double calculatedCostSharing = 0.0;
                    
                    if(cvfilterRateBase !=null && cvfilterRateBase.size() > 0){
                        BudgetRateBaseBean budgetRateBaseBean = (BudgetRateBaseBean) cvfilterRateBase.get(0);
                        budgetPersonnelDetailsRateBaseBean.setAppliedRate(budgetRateBaseBean.getAppliedRate());
                        budgetPersonnelDetailsRateBaseBean.setBaseCostSharing(budgetRateBaseBean.getBaseCostSharing());
                    }
                    
                    cvRateData.addElement(budgetPersonnelDetailsRateBaseBean);
                }
                // Prepare data for If RATE CLASS and RATE TYPE CODE is 5&3 , set the LA-Salaries (10 & 1) data to 5 & 3
                
                CoeusVector cvPerLACalamts =   cvPersonnelLiCalAmts.filter(eqLARateClassCodeAndeqLARateClassType);
                HashMap hmPerLaSalData = new HashMap();
                if(cvPerLACalamts !=null && cvPerLACalamts.size() > 0){
                    for(int salIndex =0; salIndex < cvPerLACalamts.size(); salIndex++){
                        BudgetPersonnelCalAmountsBean budgetPerCalAmountsBean = (BudgetPersonnelCalAmountsBean) cvPerLACalamts.get(salIndex);
                        String key = ""+budgetPerCalAmountsBean.getLineItemNumber()+""+budgetPerCalAmountsBean.getPersonNumber();
                        hmPerLaSalData.put(key, new Double(budgetPerCalAmountsBean.getCalculatedCost()));
                    }
                }
                
                // Prepare the data for Budget PERSONNEL RATE AND BASE ONLY FOR RATE CLASS and RATE TYPE CODE is 5&3,8&2, 10&1
                
                CoeusVector cvPerEVLCalAmts =  cvPersonnelLiCalAmts.filter(eqLARateClassAndTypeOreqEBRateClassAndTypeOreqVacRateClassAndeqType);
                if(cvPerEVLCalAmts !=null && cvPerEVLCalAmts.size() > 0){
                    HashMap hmMaxPersonNum =  new HashMap();
                    //Take the maxPersonnelPersonNumber and increase the person number and set to Hashmap
                    if(vecPersonNumData !=null && vecPersonNumData.size() > 0){
                        for(int i = 0; i < vecPersonNumData.size(); i++){
                            Integer perNum = (Integer)  vecPersonNumData.get(i);
                            hmMaxPersonNum.put(perNum, new Integer(++maxPersonnelPersonNumber));
                            //   maxPersonnelPersonNumber++;
                        }
                    }
                    
                    
                    for(int index =0; index < cvPerEVLCalAmts.size(); index++){
                        budgetPersonnelDetailsRateBaseBean = new BudgetPersonnelDetailsRateBaseBean();
                        BudgetPersonnelCalAmountsBean budgetPersonnelCalAmountsBean = (BudgetPersonnelCalAmountsBean) cvPerEVLCalAmts.get(index);
                        budgetPersonnelDetailsRateBaseBean.setProposalNumber(budgetPersonnelCalAmountsBean.getProposalNumber());
                        budgetPersonnelDetailsRateBaseBean.setVersionNumber(budgetPersonnelCalAmountsBean.getVersionNumber());
                        budgetPersonnelDetailsRateBaseBean.setBudgetPeriod(budgetPersonnelCalAmountsBean.getBudgetPeriod());
                        budgetPersonnelDetailsRateBaseBean.setLineItemNumber(budgetPersonnelCalAmountsBean.getLineItemNumber());
//                    budgetPersonnelDetailsRateBaseBean.setPersonNumber(1);
                        if (hmMaxPersonNum.get(new Integer(budgetPersonnelCalAmountsBean.getPersonNumber())) !=null){
                            Integer maxval = (Integer) hmMaxPersonNum.get(new Integer(budgetPersonnelCalAmountsBean.getPersonNumber()));
                            budgetPersonnelDetailsRateBaseBean.setPersonNumber(maxval.intValue());
                        }
                        // budgetPersonnelDetailsRateBaseBean.setPersonNumber(budgetPersonnelCalAmountsBean.getPersonNumber());
                        budgetPersonnelDetailsRateBaseBean.setRateNumber(1);
                        
                        budgetPersonnelDetailsRateBaseBean.setRateClassCode(budgetPersonnelCalAmountsBean.getRateClassCode());
                        budgetPersonnelDetailsRateBaseBean.setRateTypeCode(budgetPersonnelCalAmountsBean.getRateTypeCode());
                        //If RateClass is 5 and Ratetype is 3 OR RateClass is 8 and RateType is 2 set the PersonNumber as 2
                        if((budgetPersonnelCalAmountsBean.getRateClassCode() == 5 && budgetPersonnelCalAmountsBean.getRateTypeCode() == 3)
                        || (budgetPersonnelCalAmountsBean.getRateClassCode() == 8 && budgetPersonnelCalAmountsBean.getRateTypeCode() == 2) ){
//                            budgetPersonnelDetailsRateBaseBean.setPersonNumber(2);
                            budgetPersonnelDetailsRateBaseBean.setJobCode("EV");
                        }
                        // If Rate is any one of the type , set LA Salaries to Salary Requested
                        if((budgetPersonnelCalAmountsBean.getRateClassCode() == 5 && budgetPersonnelCalAmountsBean.getRateTypeCode() == 3) ||
                                (budgetPersonnelCalAmountsBean.getRateClassCode() == 8 && budgetPersonnelCalAmountsBean.getRateTypeCode() == 2) ){
                            if( hmPerLaSalData !=null && hmPerLaSalData.size() > 0){
                                String key = ""+budgetPersonnelCalAmountsBean.getLineItemNumber()+""+budgetPersonnelCalAmountsBean.getPersonNumber();
                                if( hmPerLaSalData.get(key) !=null){
                                    budgetPersonnelDetailsRateBaseBean.setSalaryRequested(((Double) hmPerLaSalData.get(key)).doubleValue());
                                }
                            }
                        } else{
                            budgetPersonnelDetailsRateBaseBean.setSalaryRequested(budgetPersonnelCalAmountsBean.getCalculatedCost());
                        }
                        
                        budgetPersonnelDetailsRateBaseBean.setCalculatedCost(budgetPersonnelCalAmountsBean.getCalculatedCost());
                        budgetPersonnelDetailsRateBaseBean.setCalculatedCostSharing(budgetPersonnelCalAmountsBean.getCalculatedCostSharing());
                        
                        
                        budgetPersonnelDetailsRateBaseBean.setAcType(TypeConstants.INSERT_RECORD);
                        
                        Equals eqPersonNumber = new Equals("personNumber", new Integer(budgetPersonnelCalAmountsBean.getPersonNumber()));
                        And eQBudgetPeriodAndeQLineItemAndeQPersonNumber = new And(eQBudgetPeriodAndeQLineItem,eqPersonNumber);
                        
                        CoeusVector cvPersonnelLineItemsForPerson = queryEngine.getActiveData(key,
                                BudgetPersonnelDetailsBean.class, eQBudgetPeriodAndeQLineItemAndeQPersonNumber);
                        
                        if(cvPersonnelLineItemsForPerson !=null && cvPersonnelLineItemsForPerson.size() > 0){
                            BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean) cvPersonnelLineItemsForPerson.get(0);
                            // budgetPersonnelDetailsRateBaseBean.setPersonId(budgetPersonnelDetailsBean.getPersonId());
                            budgetPersonnelDetailsRateBaseBean.setStartDate(budgetPersonnelDetailsBean.getStartDate());
                            budgetPersonnelDetailsRateBaseBean.setEndDate(budgetPersonnelDetailsBean.getEndDate());
                            budgetPersonnelDetailsRateBaseBean.setOnOffCampusFlag(budgetPersonnelDetailsBean.isOnOffCampusFlag());
                        }
                        
                        //If Rate Class Code is 10 and Rate Type Code = 1 (Lab Allocation -Salaries), set the Person Id as 9999999LA, Otherwise set 0
                        if (budgetPersonnelCalAmountsBean.getRateClassCode() == 10 && budgetPersonnelCalAmountsBean.getRateTypeCode() == 1 ){
                            budgetPersonnelDetailsRateBaseBean.setPersonId(PERSON_ID);
                            budgetPersonnelDetailsRateBaseBean.setJobCode(LA_JOB_CODE);
                             //If RateClassCode is 10  and Rate Type Code is 1 set the Peson Cost to Base Cost 
                            if( hmPerSalData !=null && hmPerSalData.size() > 0){
                                String key = ""+budgetPersonnelCalAmountsBean.getLineItemNumber()+""+budgetPersonnelCalAmountsBean.getPersonNumber();
                                if( hmPerSalData.get(key) !=null){
                                    budgetPersonnelDetailsRateBaseBean.setBaseCost(((Double) hmPerSalData.get(key)).doubleValue());
                                }
                            }
//                         budgetPersonnelDetailsRateBaseBean.setPersonNumber(2);
                        }else{
                            budgetPersonnelDetailsRateBaseBean.setPersonId("0");
                        }
                        
                        //applied rate
                        Equals eqRateClass = new Equals("rateClassCode",new Integer(budgetPersonnelCalAmountsBean.getRateClassCode()));
                        Equals eqRateType = new Equals("rateTypeCode",new Integer(budgetPersonnelCalAmountsBean.getRateTypeCode()));
                        
                        And eqRateClassAndeqRateType = new And(eqRateClass,eqRateType);
                        CoeusVector cvfilterRateBase = cvBudgetRateAndBase.filter(eqRateClassAndeqRateType);
                        double appliedRate = 0.0;
                        double salaryRequested = 0.0;
                        double baseCostSharing = 0.0;
                        double calculatedCost = 0.0;
                        double calculatedCostSharing = 0.0;
                        
                        if(cvfilterRateBase !=null && cvfilterRateBase.size() > 0){
                            BudgetRateBaseBean budgetRateBaseBean = (BudgetRateBaseBean) cvfilterRateBase.get(0);
                            budgetPersonnelDetailsRateBaseBean.setAppliedRate(budgetRateBaseBean.getAppliedRate());
                            budgetPersonnelDetailsRateBaseBean.setBaseCostSharing(budgetRateBaseBean.getBaseCostSharing());
                        }
                        
                        cvRateData.addElement(budgetPersonnelDetailsRateBaseBean);
                    }
                } // End of Personnel -
                
                
            }
            //If it is NON PERSONNEL LINE ITEM, get the values from Budget Rate and Base
        }else{
            
            //filter Rate Class code is 10 and Rate TYpe Code is 1 - LA - Salaries
            CoeusVector cvLASalData =   cvLineItemCalAmts.filter(eqLARateClassCodeAndeqLARateClassType);
            HashMap hmNonPerLASalData = new HashMap();
            if(cvLASalData !=null && cvLASalData.size() > 0){
                for(int salIndex =0; salIndex < cvLASalData.size(); salIndex++){
                    BudgetDetailCalAmountsBean  budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean) cvLASalData.get(salIndex);
                    hmNonPerLASalData.put(new Integer(budgetDetailCalAmountsBean.getLineItemNumber()), new Double(budgetDetailCalAmountsBean.getCalculatedCost()));
                }
            }
            
            //    CoeusVector cvBudgetNonPerLineItemCalAmts = cvLineItemCalAmts.filter(eqLARateClassAndTypeOreqEBRateClassAndTypeOreqVacRateClassAndeqType);
            if(cvLineItemCalAmts !=null && cvLineItemCalAmts.size() > 0 && !PER_LI_WITHOUT_PERSON){
                int rateNumber = 1;
                BudgetDetailCalAmountsBean budgetPerCalAmtBean = new BudgetDetailCalAmountsBean();
                for(int index2 =0; index2 < cvLineItemCalAmts.size(); index2++){
                    budgetPersonnelDetailsRateBaseBean = new BudgetPersonnelDetailsRateBaseBean();
                    budgetPerCalAmtBean = (BudgetDetailCalAmountsBean) cvLineItemCalAmts.get(index2);
                    budgetPersonnelDetailsRateBaseBean.setProposalNumber(budgetPerCalAmtBean.getProposalNumber());
                    budgetPersonnelDetailsRateBaseBean.setVersionNumber(budgetPerCalAmtBean.getVersionNumber());
                    budgetPersonnelDetailsRateBaseBean.setBudgetPeriod(budgetPerCalAmtBean.getBudgetPeriod());
                    budgetPersonnelDetailsRateBaseBean.setLineItemNumber(budgetPerCalAmtBean.getLineItemNumber());
                    budgetPersonnelDetailsRateBaseBean.setPersonNumber(1);
                    budgetPersonnelDetailsRateBaseBean.setRateNumber(1);
                    budgetPersonnelDetailsRateBaseBean.setJobCode("NP");
                    //If Rate Class Code is 10 and Rate Type Code = 1 (Lab Allocation -Salaries), set the Person Id as 9999999LA, Otherwise set 0
                    if (budgetPerCalAmtBean.getRateClassCode() == 10 && budgetPerCalAmtBean.getRateTypeCode() == 1 ){
                        budgetPersonnelDetailsRateBaseBean.setPersonId(PERSON_ID);
                        budgetPersonnelDetailsRateBaseBean.setJobCode(LA_JOB_CODE);
                    }else{
                        budgetPersonnelDetailsRateBaseBean.setPersonId("0");
                    }
                   
                    if(cvBudgetLineItems !=null && cvBudgetLineItems.size() > 0){
                        BudgetDetailBean budgetDetailsBean = (BudgetDetailBean) cvBudgetLineItems.get(0);
                        // budgetPersonnelDetailsRateBaseBean.setPersonId(budgetPersonnelDetailsBean.getPersonId());
                        budgetPersonnelDetailsRateBaseBean.setStartDate(budgetDetailsBean.getLineItemStartDate());
                        budgetPersonnelDetailsRateBaseBean.setEndDate(budgetDetailsBean.getLineItemEndDate());
                        budgetPersonnelDetailsRateBaseBean.setOnOffCampusFlag(budgetDetailsBean.isOnOffCampusFlag());
                        //If RateClassCode is 10 or 11 or 12 and Rate Type Code is 1 set the line item Cost to Base Cost 
                        if ((budgetPerCalAmtBean.getRateClassCode() == 10 && budgetPerCalAmtBean.getRateTypeCode() == 1) ||
                             (budgetPerCalAmtBean.getRateClassCode() == 11 && budgetPerCalAmtBean.getRateTypeCode() == 1 ) ||
                             (budgetPerCalAmtBean.getRateClassCode() == 12 && budgetPerCalAmtBean.getRateTypeCode() == 1 )){
                               budgetPersonnelDetailsRateBaseBean.setBaseCost(budgetDetailsBean.getLineItemCost());
                        }
                    }
                    
                    budgetPersonnelDetailsRateBaseBean.setRateClassCode(budgetPerCalAmtBean.getRateClassCode());
                    budgetPersonnelDetailsRateBaseBean.setRateTypeCode(budgetPerCalAmtBean.getRateTypeCode());
                    
                    //Based on RateCass and Ratetype extract the Applied rate and Base Cost sharing data from BudgetRateBase bean
                    Equals eqEBRateClassCode2 = new Equals("rateClassCode", new Integer(budgetPerCalAmtBean.getRateClassCode()));
                    Equals eqEBRateClassType2 = new Equals("rateTypeCode", new Integer(budgetPerCalAmtBean.getRateTypeCode()));
                    And eqEBRateClassCodeAndeqEBRateClassType2 = new And(eqEBRateClassCode2,eqEBRateClassType2);
                    
                    CoeusVector cvfilterRateAndBase = cvBudgetRateAndBase.filter(eqEBRateClassCodeAndeqEBRateClassType2);
                    
                    if(cvfilterRateAndBase !=null && cvfilterRateAndBase.size() > 0){
                        BudgetRateBaseBean budgetRateBaseBean = (BudgetRateBaseBean) cvfilterRateAndBase.get(0);
                        budgetPersonnelDetailsRateBaseBean.setAppliedRate(budgetRateBaseBean.getAppliedRate());
                        budgetPersonnelDetailsRateBaseBean.setBaseCostSharing(budgetRateBaseBean.getBaseCostSharing());
                    }
                    if((budgetPerCalAmtBean.getRateClassCode() == 5 && budgetPerCalAmtBean.getRateTypeCode() == 3) ||
                            (budgetPerCalAmtBean.getRateClassCode() == 8 && budgetPerCalAmtBean.getRateTypeCode() == 2) ||
                            (budgetPerCalAmtBean.getRateClassCode() == 11 && budgetPerCalAmtBean.getRateTypeCode() == 1) ||
                            (budgetPerCalAmtBean.getRateClassCode() == 12 && budgetPerCalAmtBean.getRateTypeCode() == 1)){
                        if( hmNonPerLASalData !=null && hmNonPerLASalData.size() > 0){
                            if(hmNonPerLASalData.get(new Integer(budgetPerCalAmtBean.getLineItemNumber())) !=null){
                                budgetPersonnelDetailsRateBaseBean.setSalaryRequested(((Double)hmNonPerLASalData.get(new Integer(budgetPerCalAmtBean.getLineItemNumber()))).doubleValue());
                            }
                        }
                    }else{
                        budgetPersonnelDetailsRateBaseBean.setSalaryRequested(budgetPerCalAmtBean.getCalculatedCost());
                    }
                    
                    budgetPersonnelDetailsRateBaseBean.setCalculatedCost(budgetPerCalAmtBean.getCalculatedCost());
                    budgetPersonnelDetailsRateBaseBean.setCalculatedCostSharing(budgetPerCalAmtBean.getCalculatedCostSharing());
                    
                    budgetPersonnelDetailsRateBaseBean.setAcType(TypeConstants.INSERT_RECORD);
                    cvRateData.addElement(budgetPersonnelDetailsRateBaseBean);
                }
                
            }
        }
        cvRateBase = queryEngine.getDetails(key,BudgetPersonnelDetailsRateBaseBean.class);
        cvRateBase.addAll(cvRateData);
        queryEngine.addCollection(key,BudgetPersonnelDetailsRateBaseBean.class,cvRateBase);
        
    }
    
    //Added for case 2228-Print Budget Summary Enhancement - End
    // 3854: Warning in Lite when salary effective date not in place for a calculation - Start
    public Vector getVecSalaryMessages() {
        return vecSalaryMessages;
    }
    // 3854: Warning in Lite when salary effective date not in place for a calculation - End
    
    //Added for Case 3197 - Allow for the generation of project period greater than 12 months -start 
    
     /**
     * Calculate the number of months between the periods start date and end date.     * 
     */
    private void calculatePeriodNumberOfMonths() {
        CoeusVector cvValidOHRatesForCE = new CoeusVector();
        try {
            //Get all the valid OH Rates for this cost element
            BudgetPeriodBean budgetPeriodBean = null;
            CoeusVector cvBudgetPeriods = queryEngine.executeQuery(key,
                    BudgetPeriodBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
            if(cvBudgetPeriods !=null && cvBudgetPeriods.size() > 0){
                for (int index = 0; index < cvBudgetPeriods.size(); index++) {
                    budgetPeriodBean = (BudgetPeriodBean) cvBudgetPeriods.get(index);
                    double noOfMonths = budgetDataTxnBean.getNumberOfMonths(budgetPeriodBean.getStartDate(), budgetPeriodBean.getEndDate());
                    budgetPeriodBean.setNoOfPeriodMonths(noOfMonths);
                }
                queryEngine.addCollection(key,BudgetPeriodBean.class,cvBudgetPeriods);
            }
            
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }
    
   //Added for Case 3197 - Allow for the generation of project period greater than 12 months -End
  
    // Added for COEUSQA-3044 Added Personnel type Cost Element does not calculate in Lite - start
    /**
     * Does salary calculation for the all the Line Item Person in a period by using the Salary Calculator
     * @param budgetPeriod
     */
    public void calculateSalaryForBudgetPeriod(int budgetPeriod) throws CoeusException {
        
        CoeusVector cvPersonnelLineItems = queryEngine.executeQuery(key,
                BudgetPersonnelDetailsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
        
        if (cvPersonnelLineItems != null && !cvPersonnelLineItems.isEmpty()) {
            for(Object objPersonnelLI : cvPersonnelLineItems){
                
                BudgetPersonnelDetailsBean personnelDetailsBean = (BudgetPersonnelDetailsBean) objPersonnelLI;
                if(budgetPeriod == personnelDetailsBean.getBudgetPeriod()){
                    
                    //Initialize budget calculator
                    salaryCalculator = new SalaryCalculator(personnelDetailsBean);
                    salaryCalculator.distributeCalculatedSal();
                    if(personnelDetailsBean.getAcType() == null) {
                        personnelDetailsBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                    //Update to QueryEngine
                    queryEngine.update(key, personnelDetailsBean);
                    
                    //Get the Salary not available messages
                    vecMessages = salaryCalculator.getVecMessages();
                }
                
            }
        }
    } // end calculateSalary
    
    // Added for COEUSQA-3044 - end
    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
    /**
     * Method to calculate base salary for all periods
     * @param BudgetPersonsBean budgetPersonsBean
     * @param Vector
     * @param double
     * @return HashMap
     * @throws CoeusException if exception occur
     */
    public HashMap calculateBaseSalaryForAllPeriods(BudgetPersonsBean budgetPersonsBean, Vector vecBudgetPeriods,
            double inflationRate) throws CoeusException, DBException{
        HashMap hmBaseSalForPeriods = new HashMap(10);
        int periodKey;
        //fetch the calculation base of the person
        double currentPeriodSalary = budgetPersonsBean.getCalculationBase();
        for(int periodCount = 0;periodCount<vecBudgetPeriods.size();periodCount++){
            BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)vecBudgetPeriods.get(periodCount);
            periodKey = periodCount+1;
            currentPeriodSalary = calculateBaseSalaryForPeriod(currentPeriodSalary, inflationRate, budgetPersonsBean,
                    budgetPeriodBean);
            hmBaseSalForPeriods.put(periodKey,currentPeriodSalary);
        }
        return hmBaseSalForPeriods;
    }
    
    /**
     * Method to calculate base salary for particular period
     * @param BudgetPersonsBean
     * @param Double
     * @param Double
     * @param BudgetPeriodBean
     * @return double
     * @throws CoeusException if exception occur
     */
    private double calculateBaseSalaryForPeriod(Double baseSalary, Double inflationRate, BudgetPersonsBean budgetPersonsBean,
            BudgetPeriodBean budgetPeriodBean) throws CoeusException, DBException {
        java.sql.Date periodStartDate = budgetPeriodBean.getStartDate();        
        java.sql.Date effectiveDate = budgetPersonsBean.getEffectiveDate();
        double baseSalaryForPeriod = 0.00;
        
        //effective date is equal to period start date
        if(effectiveDate!=null && effectiveDate.compareTo(periodStartDate)==0){
            baseSalaryForPeriod = baseSalary;
        }else if(effectiveDate!=null && effectiveDate.compareTo(periodStartDate)>0){
            baseSalaryForPeriod = baseSalary;
        }else if(effectiveDate!=null && effectiveDate.compareTo(periodStartDate)<0){
            baseSalaryForPeriod = calculateBaseSalary(baseSalary, inflationRate);
        }
        return baseSalaryForPeriod;
    }
    
    /**
     * Calculate the salary by using base salary and inflation rate
     * @param double
     * @param double
     * @return double
     */
    private double calculateBaseSalary(double baseSalary,double inflationRate){
        return (baseSalary+((baseSalary*inflationRate)/100));
    }
    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End

    private int getURValidRateType(CoeusVector cvURRate) {
     if(cvURRate != null && !cvURRate.isEmpty() ){  
        ValidCERateTypesBean validCERateTypesBean = (ValidCERateTypesBean)cvURRate.get(0);
        if(validCERateTypesBean != null){
            return validCERateTypesBean.getRateTypeCode();
        }
     }
      return getURRateTypeCode();
    }
} // end BudgetCalculator





