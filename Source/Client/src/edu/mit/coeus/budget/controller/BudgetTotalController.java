/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.budget.controller;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.budget.gui.BudgetTotalForm;
import edu.mit.coeus.utils.RateClassTypeConstants;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DollarCurrencyTextField;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.gui.event.*;

/** This class controls the <CODE>BudgetTotalForm</CODE>
 * BudgetTotalController.java
 * @author  Vyjayanthi
 * Created on October 8, 2003, 12:02 PM
 */
public class BudgetTotalController extends Controller implements
BeanUpdatedListener, BeanAddedListener, BeanDeletedListener {
    
    private BudgetTotalForm budgetTotalForm;
    private BudgetInfoBean budgetInfoBean;
    
    private QueryEngine queryEngine;
    private String queryKey;
    private DollarCurrencyTextField dollarData;
    private BudgetTotalTableModel budgetTotalTableModel;
    private BudgetTotalRenderer budgetTotalRenderer;
    private int budgetPeriods = 0;
    private int totalColumns = 0;
    private double value;

    
    /** Holds the BudgetTotalBeans for display in the table */
    private CoeusVector cvTableData;
    
    /** Holds the BudgetInfoBeans */
    private CoeusVector cvBudgetInfo;
    
    /** Holds the initial size of the vector cvTableData
     * when it contains only the budget period line item details */
    private int initialVecSize = 0;
    
    private BudgetTotalBean budgetTotalBean;
    
    private boolean refreshRequired;
    
    private static final int COST_ELEMENT_COLUMN = 0;
    private static final int DESCRIPTION_COLUMN = 1;
    private static final int TOTAL_COLUMN = 3;
    private static final int COLUMN_WIDTH = 100;
    private static final int DESC_COLUMN_WIDTH = 250;
    
    private static final String COST_ELEMENT = "Cost Element";
    private static final String DESCRIPTION = "Description";
    private static final String PERIOD = "Period ";
    private static final String TOTAL = "Total";
    
    private static final String AC_TYPE = "acType";
    private static final String BUDGET_PERIOD = "budgetPeriod";
    private static final String RATE_CLASS_CODE = "rateClassCode";
    private static final String RATE_TYPE_CODE = "rateTypeCode";
    private static final String CALCULATED_COST = "calculatedCost";
    private static final String PERIOD_COST_VALUE = "periodCostValue";
    private static final String OH = "OH - ";
    private static final String LINE_ITEM_COST_ELEMENT = "costElement";
    private static final String LINE_ITEM_COST = "lineItemCost";
    private static final String EMPTY_STRING ="";
    
    private static final double INITIAL_VALUE = 0.0000000001;
    
    //add for print summary total page
    public CoeusVector cvTotalPageTableData;
    
    /** Creates a new instance of BudgetTotalController */
    public BudgetTotalController(){
        budgetTotalForm = new BudgetTotalForm();
        queryEngine = QueryEngine.getInstance();
        dollarData = new DollarCurrencyTextField();
        registerComponents();
    }

    /** Creates a new instance of BudgetTotalController and sets the form data
     * @param budgetInfoBean takes a <CODE>budgetInfoBean</CODE> */
    public BudgetTotalController(BudgetInfoBean budgetInfoBean){
        this();
        setFormData(budgetInfoBean);
    }
    
    /** Perform field formatting.
     * enabling, disabling components depending on the function type. */
    public void formatFields() {
    }
    
    /** An overridden method of the controller
     * @return budgetTotalForm returns the controlled form component
     */
    public Component getControlledUI() {
        return budgetTotalForm;
    }
    
    /** This method will refresh the form with the modified data */
    public void refresh(){
        if(! refreshRequired) return ;
        /** Query and get the latest budgetInfoBean to refresh the Total tab page */
        try{
            cvBudgetInfo = queryEngine.executeQuery(queryKey, budgetInfoBean);
            if( cvBudgetInfo != null && cvBudgetInfo.size() > 0) {
                budgetInfoBean = ( BudgetInfoBean ) cvBudgetInfo.get(0);
                setFormData( budgetInfoBean );
            }
            refreshRequired = false;
        }catch (CoeusException coeusException){
            coeusException.getMessage();
        }
    }
    
    /** An overridden method of the Controller to return the form object
     * @return returns the form object, since its not used here it returns null
     */
    public Object getFormData() {
        return null;
    }

    /** This method is used to set the listeners to the components. */
    public void registerComponents() {
        addBeanUpdatedListener(this, BudgetPeriodBean.class);
        addBeanUpdatedListener(this, BudgetDetailBean.class);
        addBeanAddedListener(this, BudgetPeriodBean.class);
        addBeanDeletedListener(this, BudgetPeriodBean.class);
    }
    
    /** Method to garbage collect the added listeners
     * @throws Throwable throws the exception */
    protected void finalize() throws Throwable {
        removeBeanAddedListener(this, BudgetPeriodBean.class);
        removeBeanUpdatedListener(this, BudgetPeriodBean.class);
        removeBeanUpdatedListener(this, BudgetDetailBean.class);
        removeBeanDeletedListener(this, BudgetPeriodBean.class);
        super.finalize();
    }

    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
    
    }

    /** Method to set the column widths of the table */    
    public void setTableEditors(){
        int[] colSize = new int[ budgetTotalTableModel.getColumnCount() ];
        TableColumn tableColumn = null;
        JTableHeader header = budgetTotalForm.tblBudgetTotal.getTableHeader();
        header.setFont(CoeusFontFactory.getLabelFont());
        header.setReorderingAllowed(false);
        for(int col = 0; col < colSize.length; col++) {
            tableColumn = budgetTotalForm.tblBudgetTotal.getColumnModel().getColumn(col);
            if ( col == DESCRIPTION_COLUMN ){
                //tableColumn.setMinWidth(DESC_COLUMN_WIDTH);
                //tableColumn.setMaxWidth(DESC_COLUMN_WIDTH + 250 );
                tableColumn.setPreferredWidth(DESC_COLUMN_WIDTH);
            }else{
                //tableColumn.setMinWidth(COLUMN_WIDTH);
                //tableColumn.setMaxWidth(COLUMN_WIDTH + 25 );
                tableColumn.setPreferredWidth(COLUMN_WIDTH);
            }
            tableColumn.setCellRenderer(budgetTotalRenderer);
        }
    }
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data takes data to set to the form */
    public void setFormData(Object data) {
        if( data == null ) return;
        budgetInfoBean = ( BudgetInfoBean ) data;
        queryKey = budgetInfoBean.getProposalNumber() + budgetInfoBean.getVersionNumber();
        BudgetPeriodBean budgetPeriodBean = new BudgetPeriodBean();
        budgetTotalBean = new BudgetTotalBean();
        
        //Operators used to filter and retrieve unique data for the table
        Equals equalsBudgetPeriod, equalsCostElm, equalsRateClassCode, equalsRateTypeCode;
        NotEquals notEqualsCostElm, notEqualsRateClassCode, notEqualsRateTypeCode;
        And andRateClassType, andRateClassNotRateType;
        Or orOperator;
        
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
        cvTableData = new CoeusVector();
        BudgetDetailBean budgetDetailBean = new BudgetDetailBean ();
        BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean ();
        
        NotEquals notEqualsDelete = new NotEquals(AC_TYPE, TypeConstants.DELETE_RECORD);
        Equals equalsNull = new Equals(AC_TYPE, null);
        Or notEqualsDeleteOrEqualsNull = new Or(notEqualsDelete, equalsNull);
        
        try{
            CoeusVector cvBudgetPeriods = queryEngine.executeQuery(queryKey, BudgetPeriodBean.class, notEqualsDeleteOrEqualsNull);
            CoeusVector cvBudgetDetails = queryEngine.executeQuery(queryKey, BudgetDetailBean.class, notEqualsDeleteOrEqualsNull);
            CoeusVector cvBudgetDetailCalAmts = queryEngine.executeQuery(queryKey, BudgetDetailCalAmountsBean.class, notEqualsDeleteOrEqualsNull);

            budgetPeriods = cvBudgetPeriods.size();
            if( cvBudgetPeriods != null && cvBudgetPeriods.size() > 0){
                /** Table Data for budget line items */
                /** Loop through each budget period and get the unique cost element details */
                for( int periodIndex = 0; periodIndex < cvBudgetPeriods.size(); periodIndex++){
                    budgetPeriodBean = (BudgetPeriodBean) cvBudgetPeriods.get(periodIndex);
                    budgetPeriod = budgetPeriodBean.getBudgetPeriod();
                    equalsBudgetPeriod = new Equals(BUDGET_PERIOD, new Integer(budgetPeriod));
                    /** Filter and get all budget line item details for each period */
                    cvPeriodLineItems = cvBudgetDetails.filter(equalsBudgetPeriod);
                    if( cvPeriodLineItems != null && cvPeriodLineItems.size() > 0){
                       
                        while (true) {
                            /** Get the first element of the cvPeriodLineItems
                             * since it contains only one element at a time, as it is filtered
                             * after creating a new budgetTotalBean of unique cost element */
                            budgetDetailBean = ( BudgetDetailBean )cvPeriodLineItems.get(0);
                            costElement = budgetDetailBean.getCostElement();
                            equalsCostElm = new Equals(LINE_ITEM_COST_ELEMENT, costElement);
                            notEqualsCostElm = new NotEquals(LINE_ITEM_COST_ELEMENT, costElement);
                            cvCostElements = cvPeriodLineItems.filter(equalsCostElm);
                            /** Get the sum of Line Item Cost of all budgetDetailBeans
                             * in cvCostElements corresponding to the given cost element */
                            
                            //value = cvCostElements.sum(LINE_ITEM_COST, equalsCostElm);
                            // Bug fix #1835 - start
                            value = ((double)Math.round(cvCostElements.sum(LINE_ITEM_COST, equalsCostElm)*
                            Math.pow(10.0, 2) )) / 100;
                            // Bug fix #1835 - End
                            /** Checking if cost element is Empty or null */
                            if(costElement == null || costElement.equals(EMPTY_STRING)) {
                                cvPeriodLineItems = cvPeriodLineItems.filter(notEqualsCostElm);
                                if(cvPeriodLineItems.size() > 0) {
                                    continue;
                                }else {
                                    break;
                                }
                            }
                            
                            if (cvTableData != null && cvTableData.size() > 0) {
                                /** Filter the budgetTotalBean from cvTableData 
                                 * corresponding to the given cost element and store
                                 * it into the vector cvCEExists */
                                cvCEExists = cvTableData.filter(equalsCostElm);
                                if (cvCEExists.size() > 0) {
                                    budgetTotalBean = (BudgetTotalBean) cvCEExists.get(0);
                                    /** Remove the budgetTotalBean from cvTableData
                                     * corresponding to the given cost element */
                                    cvTableData = cvTableData.filter(notEqualsCostElm);
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
                            cvTableData.addElement(budgetTotalBean);

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
                for( int periodIndex = 0; periodIndex < cvBudgetPeriods.size(); periodIndex++){
                    budgetPeriodBean = (BudgetPeriodBean) cvBudgetPeriods.get(periodIndex);
                    budgetPeriod = budgetPeriodBean.getBudgetPeriod();
                    equalsBudgetPeriod = new Equals(BUDGET_PERIOD, new Integer(budgetPeriod));
                    cvPeriodCalAmts = cvBudgetDetailCalAmts.filter(equalsBudgetPeriod);
                    if( cvPeriodCalAmts != null && cvPeriodCalAmts.size() > 0){
                        while (true) {
                            budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)cvPeriodCalAmts.get(0);
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
                            value = ((double)Math.round(cvRateClassElements.sum(CALCULATED_COST, andRateClassType)*
                            Math.pow(10.0, 2) )) / 100;
                            // Bug fix #1835 - End
                            if (cvCalAmts != null && cvCalAmts.size() > 0) {
                                /** Filter the budgetTotalBean from cvTableData 
                                 * corresponding to the given rate class code 
                                 * and rate type code. Store it into cvCalAmtsExists */
                                cvCalAmtsExists = cvCalAmts.filter(andRateClassType);
                                if (cvCalAmtsExists.size() > 0) {
                                    budgetTotalBean = (BudgetTotalBean) cvCalAmtsExists.get(0);
                                    /** Remove the budgetTotalBean from cvTableData
                                     * corresponding to the given condition in orOperator */
                                    cvCalAmts = cvCalAmts.filter(orOperator);
                                    
                                } else {
                                    /** Create a new budgetTotalBean and set the required
                                     * properties other than the period cost */
                                    budgetTotalBean = new BudgetTotalBean();
                                    budgetTotalBean.setBudgetPeriods(budgetPeriods);
                                    budgetTotalBean.setCostElement(EMPTY_STRING);
                                    rateClassType = budgetDetailCalAmountsBean.getRateClassType();
                                    /** If rate class type is overhead then display
                                     * the rate class description as OH */
                                    if( rateClassType.equals(RateClassTypeConstants.OVERHEAD) ){
                                        budgetTotalBean.setCostElementDescription(OH +
                                                budgetDetailCalAmountsBean.getRateTypeDescription());
                                    }else{
                                        budgetTotalBean.setCostElementDescription(
                                                budgetDetailCalAmountsBean.getRateClassDescription() + " - " +
                                                budgetDetailCalAmountsBean.getRateTypeDescription());
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
                                /** If rate class type is overhead then display
                                 * the rate class description as OH */
                                if( rateClassType.equals(RateClassTypeConstants.OVERHEAD) ){
                                    budgetTotalBean.setCostElementDescription(OH +
                                            budgetDetailCalAmountsBean.getRateTypeDescription());
                                }else{
                                    budgetTotalBean.setCostElementDescription(
                                            budgetDetailCalAmountsBean.getRateClassDescription() + " - " +
                                            budgetDetailCalAmountsBean.getRateTypeDescription());
                                }
                                budgetTotalBean.setRateClassCode(budgetDetailCalAmountsBean.getRateClassCode());
                                budgetTotalBean.setRateTypeCode(budgetDetailCalAmountsBean.getRateTypeCode());

                            }
                            /** Set the period cost property for the existing budgetTotalBean */
                            budgetTotalBean.setPeriodCost(periodIndex, value);
                            cvCalAmts.addElement(budgetTotalBean);

                            /** Remove all the budgetCalAmtsBeans from cvPeriodCalAmts
                             * corresponding to the given operator */
                            cvPeriodCalAmts = cvPeriodCalAmts.filter(orOperator);

                            if (cvPeriodCalAmts.size() == 0) {
                                break;
                            }
                        }
                    }
                }
                if( cvTableData != null && cvTableData.size() > 0){
                    /** Sort the vector based on <CODE>costElement</CODE>  */
                    cvTableData.sort(LINE_ITEM_COST_ELEMENT, true);
                    initialVecSize = cvTableData.size();
                }
                if( cvCalAmts != null && cvCalAmts.size() > 0){
                    /** Sort the vector based on <CODE>rateClassCode</CODE> */
                    cvCalAmts.sort(RATE_CLASS_CODE, true);
                }
                for( int index = 0; index < cvCalAmts.size(); index++){
                    cvTableData.addElement(cvCalAmts.get(index));
                }
                
                if( cvBudgetDetails != null &&  cvBudgetDetails.size() > 0 ){
                    boolean hasCostElement = false;
                    /** Check if the budgetDetailBean contains Cost Element */
                    for( int budgetDetailIndex = 0; budgetDetailIndex < cvBudgetDetails.size();
                    budgetDetailIndex++){
                        budgetDetailBean = (BudgetDetailBean)cvBudgetDetails.get(budgetDetailIndex);
                        if( !budgetDetailBean.getCostElement().equals(EMPTY_STRING) ) {
                            hasCostElement = true;
                        }
                    }
                    
                    if( hasCostElement ){
                        budgetTotalBean = new BudgetTotalBean();
                        budgetTotalBean.setCostElement(EMPTY_STRING);
                        budgetTotalBean.setCostElementDescription(TOTAL);
                        budgetTotalBean.setBudgetPeriods(budgetPeriods);
                        for(int index = 0; index < budgetPeriods; index++ ){
                            // Bug fix #1835 - start
                            //value = cvTableData.sum(PERIOD_COST_VALUE , DataType.getClass(DataType.INT) , new Integer(index));
                            value = ((double)Math.round(cvTableData.sum(PERIOD_COST_VALUE , DataType.getClass(DataType.INT) , new Integer(index))*Math.pow(10.0, 2) )) / 100;
                            // Bug fix #1835 - End
                            budgetTotalBean.setPeriodCost(index, value);
                        }
                        cvTableData.addElement(budgetTotalBean);
                    }

                }
                /** Data for the Total column of the table */
                /** Set the total property for all the budgetTotalBeans */
                for( int totalIndex = 0; totalIndex < cvTableData.size(); totalIndex ++){
                    budgetTotalBean = ( BudgetTotalBean )cvTableData.get(totalIndex);
                    budgetTotalBean.setTotal();
                }                
            }
        }catch (NumberFormatException numberFormatException ){
            numberFormatException.getMessage();
        }catch (CoeusException coeusException){
            coeusException.getMessage();
        }
        budgetTotalTableModel = new BudgetTotalTableModel();
        budgetTotalForm.tblBudgetTotal.setModel(budgetTotalTableModel);
        budgetTotalTableModel.setData(cvTableData);
        cvTotalPageTableData = cvTableData;
        budgetTotalRenderer = new BudgetTotalRenderer();
        setTableEditors();
    }
/** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data takes data to set to the form */
    public CoeusVector getIndsrlCumulativeData() throws CoeusException{
        cvBudgetInfo = queryEngine.executeQuery(queryKey, budgetInfoBean);
        if( cvBudgetInfo != null && cvBudgetInfo.size() > 0) {
            budgetInfoBean = ( BudgetInfoBean ) cvBudgetInfo.get(0);        
        }
        if(budgetInfoBean==null) return null;
        queryKey = budgetInfoBean.getProposalNumber() + budgetInfoBean.getVersionNumber();
        BudgetPeriodBean budgetPeriodBean = new BudgetPeriodBean();
        budgetTotalBean = new BudgetTotalBean();
        
        //Operators used to filter and retrieve unique data for the table
        Equals equalsBudgetPeriod, equalsCostElm, equalsRateClassCode, equalsRateTypeCode;
        NotEquals notEqualsCostElm, notEqualsRateClassCode, notEqualsRateTypeCode;
        And andRateClassType, andRateClassNotRateType;
        Or orOperator;
        
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
        CoeusVector indsrlCumData = new CoeusVector();
        BudgetDetailBean budgetDetailBean = new BudgetDetailBean ();
        BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean ();
        
        NotEquals notEqualsDelete = new NotEquals(AC_TYPE, TypeConstants.DELETE_RECORD);
        Equals equalsNull = new Equals(AC_TYPE, null);
        Or notEqualsDeleteOrEqualsNull = new Or(notEqualsDelete, equalsNull);
        
        try{
            CoeusVector cvBudgetPeriods = queryEngine.executeQuery(queryKey, BudgetPeriodBean.class, notEqualsDeleteOrEqualsNull);
            CoeusVector cvBudgetDetails = queryEngine.executeQuery(queryKey, BudgetDetailBean.class, notEqualsDeleteOrEqualsNull);
            CoeusVector cvBudgetDetailCalAmts = queryEngine.executeQuery(queryKey, BudgetDetailCalAmountsBean.class, notEqualsDeleteOrEqualsNull);

            budgetPeriods = cvBudgetPeriods.size();
            if( cvBudgetPeriods != null && cvBudgetPeriods.size() > 0){
                /** Table Data for budget line items */
                /** Loop through each budget period and get the unique cost element details */
                for( int periodIndex = 0; periodIndex < cvBudgetPeriods.size(); periodIndex++){
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
                    if( cvPeriodLineItems != null && cvPeriodLineItems.size() > 0){
                       
                        while (true) {
                            /** Get the first element of the cvPeriodLineItems
                             * since it contains only one element at a time, as it is filtered
                             * after creating a new budgetTotalBean of unique cost element */
                            budgetDetailBean = ( BudgetDetailBean )cvPeriodLineItems.get(0);
                            costElement = budgetDetailBean.getCostElement();
                            equalsCostElm = new Equals(LINE_ITEM_COST_ELEMENT, costElement);
                            notEqualsCostElm = new NotEquals(LINE_ITEM_COST_ELEMENT, costElement);
                            cvCostElements = cvPeriodLineItems.filter(equalsCostElm);
                            /** Get the sum of Line Item Cost of all budgetDetailBeans
                             * in cvCostElements corresponding to the given cost element */
                            
                            //value = cvCostElements.sum(LINE_ITEM_COST, equalsCostElm);
                            // Bug fix #1835 - start
                            value = ((double)Math.round(cvCostElements.sum(LINE_ITEM_COST, equalsCostElm)*
                            Math.pow(10.0, 2) )) / 100;
                            /*
                             *Added by Geo as an enhancement to Industrial Cumulative budget on 02/06/2006
                             */
                            //BEGIN #1
//                            if(indCum){
                                int ceListSize = cvCostElements.size();
                                for(int ceIndex=0;ceIndex<ceListSize;ceIndex++){
                                    BudgetDetailBean detBean = ( BudgetDetailBean )cvCostElements.get(ceIndex);
                                    Equals equalsLiNum = new Equals("lineItemNumber",new Integer(detBean.getLineItemNumber()));
                                    Equals equalsOhType = new Equals("rateClassType",RateClassTypeConstants.OVERHEAD);
                                    Equals equalsEbType = new Equals("rateClassType",RateClassTypeConstants.EMPLOYEE_BENEFITS);
                                    Or ohOrEb = new Or(equalsOhType,equalsEbType);
                                    And liItemNumAndEbOrOh = new And(ohOrEb, equalsLiNum);
                                    // Added for Case 4478 - Cumulative Industrial Budget is wrong for LA departments -Start
                                    // Commented filter section only Rate Class Type OVERHEAD and EMPLOYEE BENEFITS.
                                    //Added Only LineItemNumber to filter, will take all the avaliable Rates for LineItem.
//                                    CoeusVector cvLICalAmounts = cvPeriodCalAmts.filter(liItemNumAndEbOrOh);
                                    CoeusVector cvLICalAmounts = cvPeriodCalAmts.filter(equalsLiNum);
                                    // Added for Case 4478 -End
                                    double ohCost = cvLICalAmounts.sum("calculatedCost");
                                    ohCost = ((double)Math.round(ohCost*Math.pow(10.0, 2) )) / 100;
    //                                System.out.println("ohcost for costElement =>"+ohCost);
                                    value +=ohCost;
                                }
//                            }
                            //END 
                            // Bug fix #1835 - End
                            /** Checking if cost element is Empty or null */
                            if(costElement == null || costElement.equals(EMPTY_STRING)) {
                                cvPeriodLineItems = cvPeriodLineItems.filter(notEqualsCostElm);
                                if(cvPeriodLineItems.size() > 0) {
                                    continue;
                                }else {
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
                for( int periodIndex = 0; periodIndex < cvBudgetPeriods.size(); periodIndex++){
                    budgetPeriodBean = (BudgetPeriodBean) cvBudgetPeriods.get(periodIndex);
                    budgetPeriod = budgetPeriodBean.getBudgetPeriod();
                    equalsBudgetPeriod = new Equals(BUDGET_PERIOD, new Integer(budgetPeriod));
                    cvPeriodCalAmts = cvBudgetDetailCalAmts.filter(equalsBudgetPeriod);
                    boolean ohORebFlag = false;
                    if( cvPeriodCalAmts != null && cvPeriodCalAmts.size() > 0){
                        while (true) {
                            budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)cvPeriodCalAmts.get(0);
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
                            value = ((double)Math.round(cvRateClassElements.sum(CALCULATED_COST, andRateClassType)*
                            Math.pow(10.0, 2) )) / 100;
                            
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
                                    if( (rateClassType.equals(RateClassTypeConstants.OVERHEAD) ||
                                        rateClassType.equals(RateClassTypeConstants.EMPLOYEE_BENEFITS)) ){
                                        ohORebFlag = true;
                                    }
                                    //END #1
                                    if(  (rateClassType.equals(RateClassTypeConstants.OVERHEAD) ||
                                        rateClassType.equals(RateClassTypeConstants.EMPLOYEE_BENEFITS) )){
                                        ohORebFlag = true;
                                    }
                                /** If rate class type is overhead then display
                                     * the rate class description as OH */
                                    if( rateClassType.equals(RateClassTypeConstants.OVERHEAD) ){
                                        budgetTotalBean.setCostElementDescription(OH +
                                                budgetDetailCalAmountsBean.getRateTypeDescription());
                                    }else{
                                        budgetTotalBean.setCostElementDescription(
                                                budgetDetailCalAmountsBean.getRateClassDescription() + " - " +
                                                budgetDetailCalAmountsBean.getRateTypeDescription());
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
                                if( (rateClassType.equals(RateClassTypeConstants.OVERHEAD) ||
                                    rateClassType.equals(RateClassTypeConstants.EMPLOYEE_BENEFITS)) ){
                                    ohORebFlag = true;
                                }
                                //END #1

                                /** If rate class type is overhead then display
                                 * the rate class description as OH */
                                if( rateClassType.equals(RateClassTypeConstants.OVERHEAD) ){
                                    
                                    budgetTotalBean.setCostElementDescription(OH +
                                            budgetDetailCalAmountsBean.getRateTypeDescription());
                                }else{
                                    budgetTotalBean.setCostElementDescription(
                                            budgetDetailCalAmountsBean.getRateClassDescription() + " - " +
                                            budgetDetailCalAmountsBean.getRateTypeDescription());
                                }
                                budgetTotalBean.setRateClassCode(budgetDetailCalAmountsBean.getRateClassCode());
                                budgetTotalBean.setRateTypeCode(budgetDetailCalAmountsBean.getRateTypeCode());

                            }
                            /** Set the period cost property for the existing budgetTotalBean */
                            budgetTotalBean.setPeriodCost(periodIndex, value);
                            
                            if( !ohORebFlag ){
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
                if( indsrlCumData != null && indsrlCumData.size() > 0){
                    /** Sort the vector based on <CODE>costElement</CODE>  */
                    indsrlCumData.sort(LINE_ITEM_COST_ELEMENT, true);
                    initialVecSize = indsrlCumData.size();
                }
                if( cvCalAmts != null && cvCalAmts.size() > 0){
                    /** Sort the vector based on <CODE>rateClassCode</CODE> */
                    cvCalAmts.sort(RATE_CLASS_CODE, true);
                }
                for( int index = 0; index < cvCalAmts.size(); index++){
                    indsrlCumData.addElement(cvCalAmts.get(index));
                }
                
                if( cvBudgetDetails != null &&  cvBudgetDetails.size() > 0 ){
                    boolean hasCostElement = false;
                    /** Check if the budgetDetailBean contains Cost Element */
                    for( int budgetDetailIndex = 0; budgetDetailIndex < cvBudgetDetails.size();
                    budgetDetailIndex++){
                        budgetDetailBean = (BudgetDetailBean)cvBudgetDetails.get(budgetDetailIndex);
                        if( !budgetDetailBean.getCostElement().equals(EMPTY_STRING) ) {
                            hasCostElement = true;
                        }
                    }
                    
                    if( hasCostElement ){
                        budgetTotalBean = new BudgetTotalBean();
                        budgetTotalBean.setCostElement(EMPTY_STRING);
                        budgetTotalBean.setCostElementDescription(TOTAL);
                        budgetTotalBean.setBudgetPeriods(budgetPeriods);
                        for(int index = 0; index < budgetPeriods; index++ ){
                            // Bug fix #1835 - start
                            //value = indsrlCumData.sum(PERIOD_COST_VALUE , DataType.getClass(DataType.INT) , new Integer(index));
                            value = ((double)Math.round(indsrlCumData.sum(PERIOD_COST_VALUE , DataType.getClass(DataType.INT) , new Integer(index))*Math.pow(10.0, 2) )) / 100;
                            // Bug fix #1835 - End
                            budgetTotalBean.setPeriodCost(index, value);
                        }
                        indsrlCumData.addElement(budgetTotalBean);
                    }

                }
                /** Data for the Total column of the table */
                /** Set the total property for all the budgetTotalBeans */
                for( int totalIndex = 0; totalIndex < indsrlCumData.size(); totalIndex ++){
                    budgetTotalBean = ( BudgetTotalBean )indsrlCumData.get(totalIndex);
                    budgetTotalBean.setTotal();
                }                
            }
        }catch (NumberFormatException numberFormatException ){
            numberFormatException.getMessage();
        }catch (CoeusException coeusException){
            coeusException.getMessage();
        }
    return indsrlCumData;
        
    }
    
        
    /** Validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if validation is through else returns false.
     * here since the form is only for disply it always returns false
     */
    public boolean validate() throws CoeusUIException {
        return false;
    }
    

    /** Displays the Form which is being controlled. */    
    public void display() {
    }
    
    /** Saves the Form Data. */
    public void saveFormData() {
    }

    /** Method to perform some action when the beanUpdated event is triggered
     * here it sets the <CODE>refreshRequired</CODE> flag
     * @param beanEvent takes the beanEvent */
    public void beanUpdated(BeanEvent beanEvent) {
        refreshRequired = true;
    }
    
    /** Method to check if refreshing is required
     * @return refreshRequired which indicates if form data is modified */
    public boolean isRefreshRequired() {
        return refreshRequired;
    }
    
    /** Method to set the <CODE>refreshRequired</CODE> flag
     * @param refreshRequired set to true if data is modified, false otherwise */
    public void setRefreshRequired(boolean refreshRequired) {
         this.refreshRequired = refreshRequired;
    }

    /** Method to perform some action when the beanAdded event is triggered
     * @param beanEvent takes the beanEvent */
    public void beanAdded(BeanEvent beanEvent) {
        refreshRequired = true;
        refresh();
    }

    /** Method to perform some action when the beanDeleted event is triggered
     * @param beanEvent takes the beanEvent */
    public void beanDeleted(BeanEvent beanEvent) {
        refreshRequired = true;
        refresh();
    }
    
    //Inner Class Table Model - Start
    class BudgetTotalTableModel extends AbstractTableModel{

        String[] colNames = {COST_ELEMENT, DESCRIPTION, "Period1", EMPTY_STRING};

        Class[] types = new Class [] {
            String.class,String.class,String.class,String.class
        };
        
        private Vector dataBean;
        private static final int STATIC_COLUMNS = 2;
        
        BudgetTotalTableModel() {
            //Creating the table columns
            if( budgetPeriods > 1){
                colNames = new String[ (colNames.length - 1) + budgetPeriods];
                types = new Class[(colNames.length - 1) + budgetPeriods];
                types[COST_ELEMENT_COLUMN] = String.class;
                types[DESCRIPTION_COLUMN] = String.class;
                colNames[COST_ELEMENT_COLUMN] = COST_ELEMENT;
                colNames[DESCRIPTION_COLUMN] = DESCRIPTION;
                for( int index = 1, col = index+1; index <= budgetPeriods; index++, col++){
                    colNames[col] = PERIOD + index;
                    types[col] = String.class;
                    if( col > budgetPeriods ){
                        col++;
                        colNames[col] = TOTAL;
                        types[col] = String.class;
                    }
                }
            }else{
                colNames[TOTAL_COLUMN] = TOTAL;
            }
        }
       
        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
        
        public boolean isCellEditable(int row, int col){
            return false;
        }
        
        public void setValueAt(Object value, int row, int col) {
        }

        public Object getValueAt(int row, int column) {
            budgetTotalBean = ( BudgetTotalBean ) dataBean.get(row);
            totalColumns = STATIC_COLUMNS + budgetTotalBean.getBudgetPeriods();
            double periodCost;
            switch (column) {
                case COST_ELEMENT_COLUMN:
                    return budgetTotalBean.getCostElement();
                case DESCRIPTION_COLUMN:
                    return budgetTotalBean.getCostElementDescription();
                default:                    
                    if(column == totalColumns) {
                        dollarData.setText(EMPTY_STRING+budgetTotalBean.getTotal());
                        return dollarData.getText();
                    }else{
                        periodCost =  budgetTotalBean.getPeriodCost(column - STATIC_COLUMNS);
                        if (periodCost == INITIAL_VALUE) {
                            return EMPTY_STRING;
                        } else { 
                            //dollarData.setText(EMPTY_STRING+periodCost);
                            dollarData.setValue(periodCost);
                            return dollarData.getText();
                        }
                    }
                }
        }
        
        public void setData(Vector dataBean) {
            this.dataBean = dataBean;
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
                
        public int getColumnCount() {
            return colNames.length;
        }        
        
        public int getRowCount() {
            if(dataBean == null) return 0;
            return dataBean.size();
        }
        
    }//Inner Class Table Model - End
    
    //BudgetTotalRenderer - Start
    class BudgetTotalRenderer extends DefaultTableCellRenderer {
        
        private JLabel label;

        BudgetTotalRenderer() {
            label = new JLabel();            
        }
        
        public Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            label.setFont(CoeusFontFactory.getNormalFont());
            if( row >= initialVecSize && column < BudgetTotalTableModel.STATIC_COLUMNS ){
                /** Set the brown color to the line item calculated amounts description */
                label.setForeground(new Color(185,98,83));
            }else{
                label.setForeground(Color.black);
            }
            if( row == ( cvTableData.size() - 1 )){
                /** Set the bold font to the last row of the table */
                label.setForeground(Color.black);
                label.setFont( CoeusFontFactory.getLabelFont() );
                if( column == totalColumns ){
                    /** Set the blue color to the last cell of the table */
                    label.setForeground(Color.blue);
                }
            }
            if( column == totalColumns ){
                /** Set the bold font to the last column of the table */
                label.setFont( CoeusFontFactory.getLabelFont() );
            }            
            if( column >= BudgetTotalTableModel.STATIC_COLUMNS ){
                /** Set right alignment to all columns containing currency data */
                label.setHorizontalAlignment(JLabel.RIGHT);
            }else{
                label.setHorizontalAlignment(JLabel.LEFT);
            }
            
            label.setText(" "+value.toString());
            return label;
        }
    }//BudgetTotalRenderer - End

}
