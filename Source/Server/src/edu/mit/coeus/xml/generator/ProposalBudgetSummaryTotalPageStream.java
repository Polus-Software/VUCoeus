/*
 * ProposalBudgetSummaryTotalPage.java
 *
 * Created on October 19, 2005, 2:46 PM
 */

package edu.mit.coeus.xml.generator;
import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.budget.bean.BudgetDetailBean;
import edu.mit.coeus.budget.bean.BudgetDetailCalAmountsBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetPeriodBean;
import edu.mit.coeus.budget.bean.BudgetTotalBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.utils.xml.bean.budget.budgetSalary.*;
import java.math.BigDecimal;
import java.util.*;
import javax.xml.bind.JAXBException;


/**
 *
 * @author  jenlu
 */
public class ProposalBudgetSummaryTotalPageStream extends ReportBaseStream{
    private ObjectFactory objFactory;
    private CoeusXMLGenrator xmlGenerator;    
    private String propNumber;
    private String propTitle;
    private String headerTitle, summaryComments, printBudgetComment = "No";
    private String piName;
    private int versionNumber;
    private int budgetTotolPeriods;
    private Date startDate;
    private Date endDate;
    private CoeusVector cvPropSalaryData;
    private ProposalDevelopmentTxnBean propTxnBean;
    private static final String packageName = "edu.mit.coeus.utils.xml.bean.budget.budgetSalary";
   
   
    /** Creates a new instance of ProposalBudgetSummaryTotalPage */
    public ProposalBudgetSummaryTotalPageStream() {
        objFactory = new ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();
        propTxnBean = new ProposalDevelopmentTxnBean();
    }
    
    public Object getObjectStream(Hashtable params) throws DBException, CoeusException {
        //JIRA COEUSQA-3296 - START
        String reportId = (String) params.get("REPORT_ID");
        String BUDGET_PERIOD = "budgetPeriod";
        String RATE_CLASS_CODE = "rateClassCode";
        String RATE_TYPE_CODE = "rateTypeCode";
        String CALCULATED_COST = "calculatedCost";
        String LINE_ITEM_COST_ELEMENT = "costElement";
        String LINE_ITEM_COST = "lineItemCost";
        String TOTAL = "Total";
        String PERIOD_COST_VALUE = "periodCostValue";
        String OH = "OH - ";
        String EMPTY_STRING = "";

        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();

        BudgetPeriodBean budgetPeriodBean;
        BudgetTotalBean budgetTotalBean;
        double value;

        int budgetPeriod = 0;
        int rateClassCode = 0, rateTypeCode = 0;
        String costElement, rateClassType;
        CoeusVector cvPeriodLineItems = new CoeusVector();
        CoeusVector cvCostElements = new CoeusVector();
        CoeusVector cvPeriodCalAmts = new CoeusVector();
        CoeusVector cvCEExists = new CoeusVector();
        CoeusVector cvCalAmts = new CoeusVector();
        CoeusVector cvCalAmtsExists = new CoeusVector();
        CoeusVector cvRateClassElements = new CoeusVector();

        BudgetDetailBean budgetDetailBean = new BudgetDetailBean();
        BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();

        if (reportId.equals("ProposalBudget/budgettotalbyperiod") && !params.containsKey("DATA")) {
            //For Coeuslite
            CoeusVector cvTableData = new CoeusVector();
            Map entry = (Map) params.get("BUDGET_DATA");
            CoeusVector coeusVec = (CoeusVector) entry.get(edu.mit.coeus.budget.bean.BudgetInfoBean.class);
            BudgetInfoBean bean = (BudgetInfoBean) coeusVec.get(0);
            propNumber = bean.getProposalNumber();
            startDate = bean.getStartDate();
            endDate = bean.getEndDate();
            versionNumber = bean.getVersionNumber();
               //Create beans for Data
            //get budget Periods
            //Calculate BudgetTotal - START

            //Operators used to filter and retrieve unique data for the table
            Equals equalsBudgetPeriod, equalsCostElm, equalsRateClassCode, equalsRateTypeCode;
            NotEquals notEqualsCostElm, notEqualsRateClassCode, notEqualsRateTypeCode;
            And andRateClassType, andRateClassNotRateType;
            Or orOperator;

            CoeusVector cvBudgetPeriods = (CoeusVector) params.get("BUDGET_PERIODS");//queryEngine.executeQuery(queryKey, BudgetPeriodBean.class, notEqualsDeleteOrEqualsNull);
            CoeusVector cvBudgetDetails = (CoeusVector) entry.get(edu.mit.coeus.budget.bean.BudgetDetailBean.class);//queryEngine.executeQuery(queryKey, BudgetDetailBean.class, notEqualsDeleteOrEqualsNull);
            CoeusVector cvBudgetDetailCalAmts = budgetDataTxnBean.getBudgetDetailCalAmounts(propNumber, versionNumber);//queryEngine.executeQuery(queryKey, BudgetDetailCalAmountsBean.class, notEqualsDeleteOrEqualsNull);

            int budgetPeriods = cvBudgetPeriods.size();
            if (cvBudgetPeriods != null && cvBudgetPeriods.size() > 0) {
                // Table Data for budget line items
                // Loop through each budget period and get the unique cost element details
                for (int periodIndex = 0; periodIndex < cvBudgetPeriods.size(); periodIndex++) {
                    budgetPeriodBean = (BudgetPeriodBean) cvBudgetPeriods.get(periodIndex);
                    budgetPeriod = budgetPeriodBean.getBudgetPeriod();
                    equalsBudgetPeriod = new Equals(BUDGET_PERIOD, new Integer(budgetPeriod));
                    // Filter and get all budget line item details for each period
                    cvPeriodLineItems = cvBudgetDetails.filter(equalsBudgetPeriod);
                    if (cvPeriodLineItems != null && cvPeriodLineItems.size() > 0) {

                        while (true) {
                            // Get the first element of the cvPeriodLineItems
                            // since it contains only one element at a time, as it is filtered
                            // after creating a new budgetTotalBean of unique cost element
                            budgetDetailBean = (BudgetDetailBean) cvPeriodLineItems.get(0);
                            costElement = budgetDetailBean.getCostElement();
                            equalsCostElm = new Equals(LINE_ITEM_COST_ELEMENT, costElement);
                            notEqualsCostElm = new NotEquals(LINE_ITEM_COST_ELEMENT, costElement);
                            cvCostElements = cvPeriodLineItems.filter(equalsCostElm);
                            // Get the sum of Line Item Cost of all budgetDetailBeans
                            // in cvCostElements corresponding to the given cost element
                            //value = cvCostElements.sum(LINE_ITEM_COST, equalsCostElm);
                            // Bug fix #1835 - start
                            value = ((double) Math.round(cvCostElements.sum(LINE_ITEM_COST, equalsCostElm)
                                    * Math.pow(10.0, 2))) / 100;
                            // Bug fix #1835 - End
                            // Checking if cost element is Empty or null
                            if (costElement == null || costElement.equals(EMPTY_STRING)) {
                                cvPeriodLineItems = cvPeriodLineItems.filter(notEqualsCostElm);
                                if (cvPeriodLineItems.size() > 0) {
                                    continue;
                                } else {
                                    break;
                                }
                            }

                            if (cvTableData != null && cvTableData.size() > 0) {
                                // Filter the budgetTotalBean from cvTableData
                                // corresponding to the given cost element and store
                                // it into the vector cvCEExists
                                cvCEExists = cvTableData.filter(equalsCostElm);
                                if (cvCEExists.size() > 0) {
                                    budgetTotalBean = (BudgetTotalBean) cvCEExists.get(0);
                                    // Remove the budgetTotalBean from cvTableData
                                    // corresponding to the given cost element
                                    cvTableData = cvTableData.filter(notEqualsCostElm);
                                } else {
                                    // create a new budgetTotalBean and set the required
                                    // properties other than the period cost /
                                    budgetTotalBean = new BudgetTotalBean();
                                    budgetTotalBean.setBudgetPeriods(budgetPeriods);
                                    budgetTotalBean.setCostElement(budgetDetailBean.getCostElement());
                                    budgetTotalBean.setCostElementDescription(budgetDetailBean.getCostElementDescription());
                                }
                            } else {
                                // Create a new budgetTotalBean and set the required
                                // properties other than the period cost
                                budgetTotalBean = new BudgetTotalBean();
                                budgetTotalBean.setBudgetPeriods(budgetPeriods);
                                budgetTotalBean.setCostElement(budgetDetailBean.getCostElement());
                                budgetTotalBean.setCostElementDescription(budgetDetailBean.getCostElementDescription());
                            }
                            // Set the period cost property for the existing budgetTotalBean
                            budgetTotalBean.setPeriodCost(periodIndex, value);
                            cvTableData.addElement(budgetTotalBean);

                            // Remove all the budgetDetailBeans from cvPeriodLineItems
                            // corresponding to the given cost element
                            cvPeriodLineItems = cvPeriodLineItems.filter(notEqualsCostElm);

                            if (cvPeriodLineItems.size() == 0) {
                                break;
                            }
                        }
                    }
                }
                // Table Data for budget line item calculated amounts
                // Loop through each budget period and get the unique
                // line item calculated amounts
                for (int periodIndex = 0; periodIndex < cvBudgetPeriods.size(); periodIndex++) {
                    budgetPeriodBean = (BudgetPeriodBean) cvBudgetPeriods.get(periodIndex);
                    budgetPeriod = budgetPeriodBean.getBudgetPeriod();
                    equalsBudgetPeriod = new Equals(BUDGET_PERIOD, new Integer(budgetPeriod));
                    cvPeriodCalAmts = cvBudgetDetailCalAmts.filter(equalsBudgetPeriod);
                    if (cvPeriodCalAmts != null && cvPeriodCalAmts.size() > 0) {
                        while (true) {
                            budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean) cvPeriodCalAmts.get(0);
                            rateClassCode = budgetDetailCalAmountsBean.getRateClassCode();
                            rateTypeCode = budgetDetailCalAmountsBean.getRateTypeCode();
                            equalsRateClassCode = new Equals(RATE_CLASS_CODE, new Integer(rateClassCode));
                            notEqualsRateClassCode = new NotEquals(RATE_CLASS_CODE, new Integer(rateClassCode));
                            equalsRateTypeCode = new Equals(RATE_TYPE_CODE, new Integer(rateTypeCode));
                            notEqualsRateTypeCode = new NotEquals(RATE_TYPE_CODE, new Integer(rateTypeCode));
                            andRateClassType = new And(equalsRateClassCode, equalsRateTypeCode);
                            andRateClassNotRateType = new And(equalsRateClassCode, notEqualsRateTypeCode);
                            // The orOperator corresponds to the following condition
                            // ( rateClassCode == <rateClassCode> && rateTypeCode != <rateTypeCode> ) ||
                            // ( rateClassCode != <rateClassCode> )
                            orOperator = new Or(andRateClassNotRateType, notEqualsRateClassCode);
                            cvRateClassElements = cvPeriodCalAmts.filter(andRateClassType);
                            // Get the sum of Calculated Cost of all budgetCalAmtsBeans
                            // in cvRateClassElements corresponding to the given rate class code
                            // and rate type code
                            //value = cvRateClassElements.sum(CALCULATED_COST, andRateClassType);
                            // Bug fix #1835 - start
                            value = ((double) Math.round(cvRateClassElements.sum(CALCULATED_COST, andRateClassType)
                                    * Math.pow(10.0, 2))) / 100;
                            // Bug fix #1835 - End
                            if (cvCalAmts != null && cvCalAmts.size() > 0) {
                                // Filter the budgetTotalBean from cvTableData
                                // corresponding to the given rate class code
                                // and rate type code. Store it into cvCalAmtsExists
                                cvCalAmtsExists = cvCalAmts.filter(andRateClassType);
                                if (cvCalAmtsExists.size() > 0) {
                                    budgetTotalBean = (BudgetTotalBean) cvCalAmtsExists.get(0);
                                    // Remove the budgetTotalBean from cvTableData
                                    // corresponding to the given condition in orOperator
                                    cvCalAmts = cvCalAmts.filter(orOperator);

                                } else {
                                    // Create a new budgetTotalBean and set the required
                                    // properties other than the period cost
                                    budgetTotalBean = new BudgetTotalBean();
                                    budgetTotalBean.setBudgetPeriods(budgetPeriods);
                                    budgetTotalBean.setCostElement(EMPTY_STRING);
                                    rateClassType = budgetDetailCalAmountsBean.getRateClassType();
                                    // If rate class type is overhead then display
                                    // the rate class description as OH
                                    if (rateClassType.equals(RateClassTypeConstants.OVERHEAD)) {
                                        budgetTotalBean.setCostElementDescription(OH
                                                + budgetDetailCalAmountsBean.getRateTypeDescription());
                                    } else {
                                        budgetTotalBean.setCostElementDescription(
                                                budgetDetailCalAmountsBean.getRateClassDescription() + " - "
                                                + budgetDetailCalAmountsBean.getRateTypeDescription());
                                    }
                                    budgetTotalBean.setRateClassCode(budgetDetailCalAmountsBean.getRateClassCode());
                                    budgetTotalBean.setRateTypeCode(budgetDetailCalAmountsBean.getRateTypeCode());
                                }
                            } else {
                                // Create a new budgetTotalBean and set the required
                                // properties other than the period cost
                                budgetTotalBean = new BudgetTotalBean();
                                budgetTotalBean.setBudgetPeriods(budgetPeriods);
                                budgetTotalBean.setCostElement(EMPTY_STRING);
                                rateClassType = budgetDetailCalAmountsBean.getRateClassType();
                                // If rate class type is overhead then display
                                // the rate class description as OH
                                if (rateClassType.equals(RateClassTypeConstants.OVERHEAD)) {
                                    budgetTotalBean.setCostElementDescription(OH
                                            + budgetDetailCalAmountsBean.getRateTypeDescription());
                                } else {
                                    budgetTotalBean.setCostElementDescription(
                                            budgetDetailCalAmountsBean.getRateClassDescription() + " - "
                                            + budgetDetailCalAmountsBean.getRateTypeDescription());
                                }
                                budgetTotalBean.setRateClassCode(budgetDetailCalAmountsBean.getRateClassCode());
                                budgetTotalBean.setRateTypeCode(budgetDetailCalAmountsBean.getRateTypeCode());

                            }
                            // Set the period cost property for the existing budgetTotalBean
                            budgetTotalBean.setPeriodCost(periodIndex, value);
                            cvCalAmts.addElement(budgetTotalBean);

                            // Remove all the budgetCalAmtsBeans from cvPeriodCalAmts
                            // corresponding to the given operator
                            cvPeriodCalAmts = cvPeriodCalAmts.filter(orOperator);

                            if (cvPeriodCalAmts.size() == 0) {
                                break;
                            }
                        }
                    }
                }
                if (cvTableData != null && cvTableData.size() > 0) {
                    // Sort the vector based on <CODE>costElement</CODE>
                    cvTableData.sort(LINE_ITEM_COST_ELEMENT, true);
                    //initialVecSize = cvTableData.size();
                }
                if (cvCalAmts != null && cvCalAmts.size() > 0) {
                    // Sort the vector based on <CODE>rateClassCode</CODE>
                    cvCalAmts.sort(RATE_CLASS_CODE, true);
                }
                for (int index = 0; index < cvCalAmts.size(); index++) {
                    cvTableData.addElement(cvCalAmts.get(index));
                }

                if (cvBudgetDetails != null && cvBudgetDetails.size() > 0) {
                    boolean hasCostElement = false;
                    // Check if the budgetDetailBean contains Cost Element
                    for (int budgetDetailIndex = 0; budgetDetailIndex < cvBudgetDetails.size();
                            budgetDetailIndex++) {
                        budgetDetailBean = (BudgetDetailBean) cvBudgetDetails.get(budgetDetailIndex);
                        if (!budgetDetailBean.getCostElement().equals(EMPTY_STRING)) {
                            hasCostElement = true;
                        }
                    }

                    if (hasCostElement) {
                        budgetTotalBean = new BudgetTotalBean();
                        budgetTotalBean.setCostElement(EMPTY_STRING);
                        budgetTotalBean.setCostElementDescription(TOTAL);
                        budgetTotalBean.setBudgetPeriods(budgetPeriods);
                        for (int index = 0; index < budgetPeriods; index++) {
                            // Bug fix #1835 - start
                            //value = cvTableData.sum(PERIOD_COST_VALUE , DataType.getClass(DataType.INT) , new Integer(index));
                            value = ((double) Math.round(cvTableData.sum(PERIOD_COST_VALUE, DataType.getClass(DataType.INT), new Integer(index)) * Math.pow(10.0, 2))) / 100;
                            // Bug fix #1835 - End
                            budgetTotalBean.setPeriodCost(index, value);
                        }
                        cvTableData.addElement(budgetTotalBean);
                    }

                }
                // Data for the Total column of the table
                // Set the total property for all the budgetTotalBeans
                for (int totalIndex = 0; totalIndex < cvTableData.size(); totalIndex++) {
                    budgetTotalBean = (BudgetTotalBean) cvTableData.get(totalIndex);
                    budgetTotalBean.setTotal();
                }
            }

            params.put("DATA", cvTableData);
            //Calculate BudgetTotal - END
        } else if (reportId.equals("ProposalBudget/indsrlcumbudget") && !params.containsKey("DATA")) {
            //For Coeuslite
            Map entry = (Map) params.get("BUDGET_DATA");
            CoeusVector coeusVec = (CoeusVector) entry.get(edu.mit.coeus.budget.bean.BudgetInfoBean.class);
            BudgetInfoBean bean = (BudgetInfoBean) coeusVec.get(0);
            propNumber = bean.getProposalNumber();
            startDate = bean.getStartDate();
            endDate = bean.getEndDate();
            versionNumber = bean.getVersionNumber();

            CoeusVector indsrlCumData = new CoeusVector();

            //Operators used to filter and retrieve unique data for the table
            Equals equalsBudgetPeriod, equalsCostElm, equalsRateClassCode, equalsRateTypeCode;
            NotEquals notEqualsCostElm, notEqualsRateClassCode, notEqualsRateTypeCode;
            And andRateClassType, andRateClassNotRateType;
            Or orOperator;

            CoeusVector cvBudgetPeriods = (CoeusVector) params.get("BUDGET_PERIODS");//CoeusVector cvBudgetPeriods = queryEngine.executeQuery(queryKey, BudgetPeriodBean.class, notEqualsDeleteOrEqualsNull);
            CoeusVector cvBudgetDetails = (CoeusVector) entry.get(edu.mit.coeus.budget.bean.BudgetDetailBean.class);//CoeusVector cvBudgetDetails = queryEngine.executeQuery(queryKey, BudgetDetailBean.class, notEqualsDeleteOrEqualsNull);
            CoeusVector cvBudgetDetailCalAmts = budgetDataTxnBean.getBudgetDetailCalAmounts(propNumber, versionNumber);//CoeusVector cvBudgetDetailCalAmts = queryEngine.executeQuery(queryKey, BudgetDetailCalAmountsBean.class, notEqualsDeleteOrEqualsNull);

            int budgetPeriods = cvBudgetPeriods.size();
            if (cvBudgetPeriods != null && cvBudgetPeriods.size() > 0) {
                /** Table Data for budget line items */
                /** Loop through each budget period and get the unique cost element details */
                for (int periodIndex = 0; periodIndex < cvBudgetPeriods.size(); periodIndex++) {
                    budgetPeriodBean = (BudgetPeriodBean) cvBudgetPeriods.get(periodIndex);
                    budgetPeriod = budgetPeriodBean.getBudgetPeriod();
                    equalsBudgetPeriod = new Equals(BUDGET_PERIOD, new Integer(budgetPeriod));
                    /*
                     *Added by Geo as an enhancement to Industrial Cumulative budget on 02/06/2006
                     */
                    //BEGIN #1
                    cvPeriodCalAmts = cvBudgetDetailCalAmts.filter(equalsBudgetPeriod);
                    //END

                    /** Filter and get all budget line item details for each period */
                    cvPeriodLineItems = cvBudgetDetails.filter(equalsBudgetPeriod);
                    if (cvPeriodLineItems != null && cvPeriodLineItems.size() > 0) {

                        while (true) {
                            /** Get the first element of the cvPeriodLineItems
                             * since it contains only one element at a time, as it is filtered
                             * after creating a new budgetTotalBean of unique cost element */
                            budgetDetailBean = (BudgetDetailBean) cvPeriodLineItems.get(0);
                            costElement = budgetDetailBean.getCostElement();
                            equalsCostElm = new Equals(LINE_ITEM_COST_ELEMENT, costElement);
                            notEqualsCostElm = new NotEquals(LINE_ITEM_COST_ELEMENT, costElement);
                            cvCostElements = cvPeriodLineItems.filter(equalsCostElm);
                            /** Get the sum of Line Item Cost of all budgetDetailBeans
                             * in cvCostElements corresponding to the given cost element */
                            //value = cvCostElements.sum(LINE_ITEM_COST, equalsCostElm);
                            // Bug fix #1835 - start
                            value = ((double) Math.round(cvCostElements.sum(LINE_ITEM_COST, equalsCostElm)
                                    * Math.pow(10.0, 2))) / 100;
                            /*
                             *Added by Geo as an enhancement to Industrial Cumulative budget on 02/06/2006
                             */
                            //BEGIN #1
//                            if(indCum){
                            int ceListSize = cvCostElements.size();
                            for (int ceIndex = 0; ceIndex < ceListSize; ceIndex++) {
                                BudgetDetailBean detBean = (BudgetDetailBean) cvCostElements.get(ceIndex);
                                Equals equalsLiNum = new Equals("lineItemNumber", new Integer(detBean.getLineItemNumber()));
                                //Equals equalsOhType = new Equals("rateClassType", RateClassTypeConstants.OVERHEAD);
                                //Equals equalsEbType = new Equals("rateClassType", RateClassTypeConstants.EMPLOYEE_BENEFITS);
                                //Or ohOrEb = new Or(equalsOhType, equalsEbType);
                                //And liItemNumAndEbOrOh = new And(ohOrEb, equalsLiNum);
                                // Added for Case 4478 - Cumulative Industrial Budget is wrong for LA departments -Start
                                // Commented filter section only Rate Class Type OVERHEAD and EMPLOYEE BENEFITS.
                                //Added Only LineItemNumber to filter, will take all the avaliable Rates for LineItem.
//                                    CoeusVector cvLICalAmounts = cvPeriodCalAmts.filter(liItemNumAndEbOrOh);
                                CoeusVector cvLICalAmounts = cvPeriodCalAmts.filter(equalsLiNum);
                                // Added for Case 4478 -End
                                double ohCost = cvLICalAmounts.sum("calculatedCost");
                                ohCost = ((double) Math.round(ohCost * Math.pow(10.0, 2))) / 100;
                                //                                System.out.println("ohcost for costElement =>"+ohCost);
                                value += ohCost;
                            }
//                            }
                            //END
                            // Bug fix #1835 - End
                            /** Checking if cost element is Empty or null */
                            if (costElement == null || costElement.equals(EMPTY_STRING)) {
                                cvPeriodLineItems = cvPeriodLineItems.filter(notEqualsCostElm);
                                if (cvPeriodLineItems.size() > 0) {
                                    continue;
                                } else {
                                    break;
                                }
                            }

                            if (indsrlCumData != null && indsrlCumData.size() > 0) {
                                /** Filter the budgetTotalBean from indsrlCumData
                                 * corresponding to the given cost element and store
                                 * it into the vector cvCEExists */
                                cvCEExists = indsrlCumData.filter(equalsCostElm);
                                if (cvCEExists.size() > 0) {
                                    budgetTotalBean = (BudgetTotalBean) cvCEExists.get(0);
                                    /** Remove the budgetTotalBean from indsrlCumData
                                     * corresponding to the given cost element */
                                    indsrlCumData = indsrlCumData.filter(notEqualsCostElm);
                                } else {
                                    /** create a new budgetTotalBean and set the required
                                     * properties other than the period cost */
                                    budgetTotalBean = new BudgetTotalBean();
                                    budgetTotalBean.setBudgetPeriods(budgetPeriods);
                                    budgetTotalBean.setCostElement(budgetDetailBean.getCostElement());
                                    budgetTotalBean.setCostElementDescription(budgetDetailBean.getCostElementDescription());
                                }
                            } else {
                                /** Create a new budgetTotalBean and set the required
                                 * properties other than the period cost */
                                budgetTotalBean = new BudgetTotalBean();
                                budgetTotalBean.setBudgetPeriods(budgetPeriods);
                                budgetTotalBean.setCostElement(budgetDetailBean.getCostElement());
                                budgetTotalBean.setCostElementDescription(budgetDetailBean.getCostElementDescription());
                            }
                            /** Set the period cost property for the existing budgetTotalBean */
                            budgetTotalBean.setPeriodCost(periodIndex, value);
                            indsrlCumData.addElement(budgetTotalBean);

                            /** Remove all the budgetDetailBeans from cvPeriodLineItems
                             * corresponding to the given cost element */
                            cvPeriodLineItems = cvPeriodLineItems.filter(notEqualsCostElm);

                            if (cvPeriodLineItems.size() == 0) {
                                break;
                            }
                        }
                    }
                }
                /** Table Data for budget line item calculated amounts */
                /* Loop through each budget period and get the unique
                 * line item calculated amounts */
                for (int periodIndex = 0; periodIndex < cvBudgetPeriods.size(); periodIndex++) {
                    budgetPeriodBean = (BudgetPeriodBean) cvBudgetPeriods.get(periodIndex);
                    budgetPeriod = budgetPeriodBean.getBudgetPeriod();
                    equalsBudgetPeriod = new Equals(BUDGET_PERIOD, new Integer(budgetPeriod));
                    cvPeriodCalAmts = cvBudgetDetailCalAmts.filter(equalsBudgetPeriod);
                    boolean ohORebFlag = false;
                    if (cvPeriodCalAmts != null && cvPeriodCalAmts.size() > 0) {
                        while (true) {
                            budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean) cvPeriodCalAmts.get(0);
                            rateClassCode = budgetDetailCalAmountsBean.getRateClassCode();
                            rateTypeCode = budgetDetailCalAmountsBean.getRateTypeCode();
                            equalsRateClassCode = new Equals(RATE_CLASS_CODE, new Integer(rateClassCode));
                            notEqualsRateClassCode = new NotEquals(RATE_CLASS_CODE, new Integer(rateClassCode));
                            equalsRateTypeCode = new Equals(RATE_TYPE_CODE, new Integer(rateTypeCode));
                            notEqualsRateTypeCode = new NotEquals(RATE_TYPE_CODE, new Integer(rateTypeCode));
                            andRateClassType = new And(equalsRateClassCode, equalsRateTypeCode);
                            andRateClassNotRateType = new And(equalsRateClassCode, notEqualsRateTypeCode);
                            /** The orOperator corresponds to the following condition
                             * ( rateClassCode == <rateClassCode> && rateTypeCode != <rateTypeCode> ) ||
                             * ( rateClassCode != <rateClassCode> ) */
                            orOperator = new Or(andRateClassNotRateType, notEqualsRateClassCode);
                            cvRateClassElements = cvPeriodCalAmts.filter(andRateClassType);
                            /** Get the sum of Calculated Cost of all budgetCalAmtsBeans
                             * in cvRateClassElements corresponding to the given rate class code
                             * and rate type code */
//                            value = cvRateClassElements.sum(CALCULATED_COST, andRateClassType);
                            // Bug fix #1835 - start
                            value = ((double) Math.round(cvRateClassElements.sum(CALCULATED_COST, andRateClassType)
                                    * Math.pow(10.0, 2))) / 100;

                            // Bug fix #1835 - End
                            if (cvCalAmts != null && cvCalAmts.size() > 0) {
                                /** Filter the budgetTotalBean from indsrlCumData
                                 * corresponding to the given rate class code
                                 * and rate type code. Store it into cvCalAmtsExists */
                                cvCalAmtsExists = cvCalAmts.filter(andRateClassType);
                                if (cvCalAmtsExists.size() > 0) {
                                    budgetTotalBean = (BudgetTotalBean) cvCalAmtsExists.get(0);
                                    /** Remove the budgetTotalBean from indsrlCumData
                                     * corresponding to the given condition in orOperator */
                                    cvCalAmts = cvCalAmts.filter(orOperator);

                                } else {
                                    /** Create a new budgetTotalBean and set the required
                                     * properties other than the period cost */
                                    budgetTotalBean = new BudgetTotalBean();
                                    budgetTotalBean.setBudgetPeriods(budgetPeriods);
                                    budgetTotalBean.setCostElement(EMPTY_STRING);
                                    rateClassType = budgetDetailCalAmountsBean.getRateClassType();
                                    /*
                                     *Added by Geo as an enhancement to Industrial Cumulative budget on 02/06/2006
                                     */
                                    //BEGIN #1
                                    if ((rateClassType.equals(RateClassTypeConstants.OVERHEAD)
                                            || rateClassType.equals(RateClassTypeConstants.EMPLOYEE_BENEFITS))) {
                                        ohORebFlag = true;
                                    }
                                    //END #1
                                    if ((rateClassType.equals(RateClassTypeConstants.OVERHEAD)
                                            || rateClassType.equals(RateClassTypeConstants.EMPLOYEE_BENEFITS))) {
                                        ohORebFlag = true;
                                    }
                                    /** If rate class type is overhead then display
                                     * the rate class description as OH */
                                    if (rateClassType.equals(RateClassTypeConstants.OVERHEAD)) {
                                        budgetTotalBean.setCostElementDescription(OH
                                                + budgetDetailCalAmountsBean.getRateTypeDescription());
                                    } else {
                                        budgetTotalBean.setCostElementDescription(
                                                budgetDetailCalAmountsBean.getRateClassDescription() + " - "
                                                + budgetDetailCalAmountsBean.getRateTypeDescription());
                                    }
                                    budgetTotalBean.setRateClassCode(budgetDetailCalAmountsBean.getRateClassCode());
                                    budgetTotalBean.setRateTypeCode(budgetDetailCalAmountsBean.getRateTypeCode());
                                }
                            } else {
                                /** Create a new budgetTotalBean and set the required
                                 * properties other than the period cost */
                                budgetTotalBean = new BudgetTotalBean();
                                budgetTotalBean.setBudgetPeriods(budgetPeriods);
                                budgetTotalBean.setCostElement(EMPTY_STRING);
                                rateClassType = budgetDetailCalAmountsBean.getRateClassType();
                                /*
                                 *Added by Geo as an enhancement to Industrial Cumulative budget on 02/06/2006
                                 */
                                //BEGIN #1
                                if ((rateClassType.equals(RateClassTypeConstants.OVERHEAD)
                                        || rateClassType.equals(RateClassTypeConstants.EMPLOYEE_BENEFITS))) {
                                    ohORebFlag = true;
                                }
                                //END #1

                                /** If rate class type is overhead then display
                                 * the rate class description as OH */
                                if (rateClassType.equals(RateClassTypeConstants.OVERHEAD)) {

                                    budgetTotalBean.setCostElementDescription(OH
                                            + budgetDetailCalAmountsBean.getRateTypeDescription());
                                } else {
                                    budgetTotalBean.setCostElementDescription(
                                            budgetDetailCalAmountsBean.getRateClassDescription() + " - "
                                            + budgetDetailCalAmountsBean.getRateTypeDescription());
                                }
                                budgetTotalBean.setRateClassCode(budgetDetailCalAmountsBean.getRateClassCode());
                                budgetTotalBean.setRateTypeCode(budgetDetailCalAmountsBean.getRateTypeCode());

                            }
                            /** Set the period cost property for the existing budgetTotalBean */
                            budgetTotalBean.setPeriodCost(periodIndex, value);

                            if (!ohORebFlag) {
                                cvCalAmts.addElement(budgetTotalBean);
                            }

                            /** Remove all the budgetCalAmtsBeans from cvPeriodCalAmts
                             * corresponding to the given operator */
                            cvPeriodCalAmts = cvPeriodCalAmts.filter(orOperator);

                            if (cvPeriodCalAmts.size() == 0) {
                                break;
                            }
                        }
                    }
                }
                if (indsrlCumData != null && indsrlCumData.size() > 0) {
                    /** Sort the vector based on <CODE>costElement</CODE>  */
                    indsrlCumData.sort(LINE_ITEM_COST_ELEMENT, true);
                    //initialVecSize = indsrlCumData.size();
                }
                if (cvCalAmts != null && cvCalAmts.size() > 0) {
                    /** Sort the vector based on <CODE>rateClassCode</CODE> */
                    cvCalAmts.sort(RATE_CLASS_CODE, true);
                }
                for (int index = 0; index < cvCalAmts.size(); index++) {
                    indsrlCumData.addElement(cvCalAmts.get(index));
                }

                if (cvBudgetDetails != null && cvBudgetDetails.size() > 0) {
                    boolean hasCostElement = false;
                    /** Check if the budgetDetailBean contains Cost Element */
                    for (int budgetDetailIndex = 0; budgetDetailIndex < cvBudgetDetails.size();
                            budgetDetailIndex++) {
                        budgetDetailBean = (BudgetDetailBean) cvBudgetDetails.get(budgetDetailIndex);
                        if (!budgetDetailBean.getCostElement().equals(EMPTY_STRING)) {
                            hasCostElement = true;
                        }
                    }

                    if (hasCostElement) {
                        budgetTotalBean = new BudgetTotalBean();
                        budgetTotalBean.setCostElement(EMPTY_STRING);
                        budgetTotalBean.setCostElementDescription(TOTAL);
                        budgetTotalBean.setBudgetPeriods(budgetPeriods);
                        for (int index = 0; index < budgetPeriods; index++) {
                            // Bug fix #1835 - start
                            //value = indsrlCumData.sum(PERIOD_COST_VALUE , DataType.getClass(DataType.INT) , new Integer(index));
                            value = ((double) Math.round(indsrlCumData.sum(PERIOD_COST_VALUE, DataType.getClass(DataType.INT), new Integer(index)) * Math.pow(10.0, 2))) / 100;
                            // Bug fix #1835 - End
                            budgetTotalBean.setPeriodCost(index, value);
                        }
                        indsrlCumData.addElement(budgetTotalBean);
                    }

                }
                /** Data for the Total column of the table */
                /** Set the total property for all the budgetTotalBeans */
                for (int totalIndex = 0; totalIndex < indsrlCumData.size(); totalIndex++) {
                    budgetTotalBean = (BudgetTotalBean) indsrlCumData.get(totalIndex);
                    budgetTotalBean.setTotal();
                }
            }
            params.put("DATA", indsrlCumData);
        }else {
            propNumber = (String)params.get("PROPOSAL_NUM");
            versionNumber = Integer.parseInt(params.get("VERSION_NUM").toString());
            startDate = (Date)params.get("START_DATE");
            endDate = (Date)params.get("END_DATE");
        }
        headerTitle = (String)params.get("HEADER_TITLE");
        //JIRA COEUSQA-3296 - END
        cvPropSalaryData = (CoeusVector)params.get("DATA");
        // Modified for COEUSQA-1683 : Print option to display Version Comments on Budget Reports - Start
        reportId = (String)params.get("REPORT_ID");
        // Modified for COEUSQA-3425 : Print Comments for Budget Reports - remaining issues - Start
//        summaryComments = (String)params.get("SUMMARY_COMMENTS");
        Hashtable htBudgetData = (Hashtable)params.get("BUDGET_DATA");
        CoeusVector cvBudgetInfo = (CoeusVector)htBudgetData.get(BudgetInfoBean.class);
        if(cvBudgetInfo != null && !cvBudgetInfo.isEmpty()){
            BudgetInfoBean budgetInfoBean = (BudgetInfoBean)cvBudgetInfo.get(0);
            summaryComments = budgetInfoBean.getComments();
        }
        // Modified for COEUSQA-3425 : Print Comments for Budget Reports - remaining issues - End
        if(!"Budget/SummaryTotal".equals(reportId)){
            if(params.get("PRINT_BUDGET_COMMENT") != null && ((Boolean)params.get("PRINT_BUDGET_COMMENT")).booleanValue()){
                printBudgetComment = "Yes";
            }
        }
        // Modified for COEUSQA-1683 : Print option to display Version Comments on Budget Reports - End                
        propTitle =  propTxnBean.getProposalTitle(propNumber);
        piName = propTxnBean.getPIUserName(propNumber);
        
        
        return getBudgetSalay();
    }
    public org.w3c.dom.Document getStream(Hashtable params) throws DBException,CoeusException {
        propNumber = (String)params.get("PROPOSAL_NUM");
        headerTitle = (String)params.get("HEADER_TITLE");
        versionNumber = Integer.parseInt(params.get("VERSION_NUM").toString());
        startDate = (Date)params.get("START_DATE");
        endDate = (Date)params.get("END_DATE");
        cvPropSalaryData = (CoeusVector)params.get("DATA");
        propTitle =  propTxnBean.getProposalTitle(propNumber);
        piName = propTxnBean.getPIUserName(propNumber);
        // Modified for COEUSQA-1683 : Print option to display Version Comments on Budget Reports - Start
//        summaryComments = (String)params.get("SUMMARY_COMMENTS");
        String reportId = (String)params.get("REPORT_ID");
        // Modified for COEUSQA-3425 : Print Comments for Budget Reports - remaining issues - Start
//        summaryComments = (String)params.get("SUMMARY_COMMENTS");
        Hashtable htBudgetData = (Hashtable)params.get("BUDGET_DATA");
        CoeusVector cvBudgetInfo = (CoeusVector)htBudgetData.get(BudgetInfoBean.class);
        if(cvBudgetInfo != null && !cvBudgetInfo.isEmpty()){
            BudgetInfoBean budgetInfoBean = (BudgetInfoBean)cvBudgetInfo.get(0);
            summaryComments = budgetInfoBean.getComments();
        }
        // Modified for COEUSQA-3425 : Print Comments for Budget Reports - remaining issues - End
        
        if(!"Budget/SummaryTotal".equals(reportId)){
            if(params.get("PRINT_BUDGET_COMMENT") != null && ((Boolean)params.get("PRINT_BUDGET_COMMENT")).booleanValue()){
                printBudgetComment = "Yes";
            }else{
                printBudgetComment = "No";
            }
        }
        // Modified for COEUSQA-1683 : Print option to display Version Comments on Budget Reports - End
        BudgetSalaryType budgetSalaryType = getBudgetSalay();
        return xmlGenerator.marshelObject(budgetSalaryType,packageName);
        
     }
    private BudgetSalaryType getBudgetSalay()throws CoeusXMLException,DBException,CoeusException{
        BudgetSalaryType budgetSalaryType = null;
        try{       
            budgetSalaryType = objFactory.createBudgetSalary();
            budgetSalaryType.setProposalNumber(propNumber);
            budgetSalaryType.setBudgetVersion(versionNumber);
            budgetSalaryType.setHeaderTitle(headerTitle);
            budgetSalaryType.setPIName(piName);
            budgetSalaryType.setTitle(propTitle);
            // Added for COEUSQA-1683 : Print option to display Version Comments on Budget Reports - Start
            budgetSalaryType.setComments(summaryComments);
            budgetSalaryType.setPrintBudgetComment(printBudgetComment);
            // Added for COEUSQA-1683 : Print option to display Version Comments on Budget Reports - End
            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(new Date());
            budgetSalaryType.setCurrentDate(currentDate);
            if (startDate != null){
                Calendar sDate = Calendar.getInstance();
                sDate.setTime(startDate);
                budgetSalaryType.setStartDate(sDate);
            }
            if (endDate != null){
                Calendar eDate = Calendar.getInstance();
                eDate.setTime(endDate);
                budgetSalaryType.setEndDate(eDate);
            }
            if (cvPropSalaryData != null && cvPropSalaryData.size() > 0 ){
                BudgetTotalBean bTBean = (BudgetTotalBean)cvPropSalaryData.get(0);
                budgetTotolPeriods = bTBean.getBudgetPeriods();
                budgetSalaryType.setTotalPeriod(budgetTotolPeriods);
                budgetSalaryType.getSalary().addAll(getSalaries());
            }
        }catch(JAXBException jaxbEx){
            UtilFactory.log(jaxbEx.getMessage(),jaxbEx,"ProposalBudgetSummaryTotalPageStream","getStream()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
        return budgetSalaryType;
    }
    
    private Vector getSalaries() throws JAXBException,CoeusException,DBException{
        Vector vcSalary = new Vector();
        BudgetTotalBean budgetTotalBean;
        int salarySize = cvPropSalaryData==null?0:cvPropSalaryData.size();
        for (int salaryIndex = 0; salaryIndex < salarySize; salaryIndex++ ){
            SalaryType salaryType = objFactory.createSalaryType();
            budgetTotalBean = (BudgetTotalBean)cvPropSalaryData.elementAt(salaryIndex);
            if (budgetTotalBean.getCostElement() != null ){
                salaryType.setCostElementCode(UtilFactory.convertNull(budgetTotalBean.getCostElement()));
            }          
            salaryType.setName(UtilFactory.convertNull(budgetTotalBean.getCostElementDescription()));
            salaryType.setTotal((new BigDecimal(budgetTotalBean.getTotal()).setScale(2,BigDecimal.ROUND_HALF_DOWN)));
            for (int periodIndex = 0; periodIndex < budgetTotolPeriods; periodIndex++){
                BudgetPeriodData budgetPeriodData = objFactory.createBudgetPeriodData();
                budgetPeriodData.setBudgetPeriodID(periodIndex+1);
                if (budgetTotalBean.getPeriodCost(periodIndex) != budgetTotalBean.INITIAL_VALUE) {                        
                    budgetPeriodData.setPeriodCost((new BigDecimal(budgetTotalBean.getPeriodCost(periodIndex)).setScale(2,BigDecimal.ROUND_HALF_DOWN)));                          
                }
                salaryType.getPeriod().add(budgetPeriodData);   
            }
             
            vcSalary.addElement(salaryType);    
        }
        return vcSalary;
        
    }
}
