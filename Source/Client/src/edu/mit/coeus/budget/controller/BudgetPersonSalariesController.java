/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.budget.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.URLOpener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.budget.gui.BudgetPersonSalariesForm;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.*;
import java.applet.AppletContext;
import java.net.MalformedURLException;
import java.net.URL;

/** This class controls the <CODE>BudgetPersonSalariesForm</CODE>
 * BudgetPersonSalariesController.java
 * @author  Vyjayanthi
 * Created on November 25, 2003, 11:24 AM
 */
public class BudgetPersonSalariesController extends Controller 
implements ActionListener {
    
    private BudgetPersonSalariesForm budgetPersonSalariesForm;
    private BudgetInfoBean budgetInfoBean;
    
    private QueryEngine queryEngine;
    private String queryKey;
    private DollarCurrencyTextField dollarData;
    private PersonSalariesTableModel personSalariesTableModel;
    private PersonSalariesRenderer personSalariesRenderer;
    private boolean hasPersons = false;
    private int budgetPeriods = 0;
    private int totalColumns = 0;
    private int insertIndex = 0;
    private double value;

    /** Holds the BudgetTotalBeans for display in the table */
    private CoeusVector cvTableData;
    
    /** Holds the sorted BudgetTotalBeans for display in the table */
    private CoeusVector cvSortedTableData;
    
    /** Holds the initial size of the vector cvSortedTableData
     * when it contains only the budget personnel line item details */
    private int initialVecSize = 0;
    
    private BudgetTotalBean budgetTotalBean;
    
    /** Holds CoeusMessageResources instance used for reading message Properties. */    
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    
    /** Holds the parsed message */
    private String message = EMPTY_STRING;
    
    private CoeusVector cvCEExists = new CoeusVector();

    private static final int COST_ELEMENT_COLUMN = 0;
    private static final int NAME_COLUMN = 1;
    private static final int TOTAL_COLUMN = 3;
    
    private static final int COLUMN_WIDTH = 100;
    private static final int NAME_COLUMN_WIDTH = 250;
    
    private static final String COST_ELEMENT = "Cost Element";
    private static final String NAME = "Name";
    private static final String PERIOD = "Period ";
    private static final String TOTAL = "Total";
    private static final String CALCULATED_AMTS = "Calculated Amounts";
    
    private static final String AC_TYPE = "acType";
    private static final String LINE_ITEM_COST_ELEMENT = "costElement";
    private static final String COST_ELEMENT_DESCRIPTION = "costElementDescription";
    private static final String LINE_ITEM_NUMBER = "lineItemNumber";
    private static final String PERSON_ID = "personId";
    private static final String BUDGET_PERIOD = "budgetPeriod";
    private static final String RATE_CLASS_CODE = "rateClassCode";
    private static final String RATE_TYPE_CODE = "rateTypeCode";
    private static final String CALCULATED_COST = "calculatedCost";
    private static final String PERIOD_COST_VALUE = "periodCostValue";
    private static final String COST_ELEMENT_VALUE = "costElementValue";
    private static final String OH = "OH - ";
    private static final String EMPTY_STRING = "";
    private static final String NO_SALARY_DETAILS = "budget_common_exceptionCode.1004";
    
    private static final double INITIAL_VALUE = 0.0000000001;
    
    private static final char BUDGET_SALARY_REPORT = 'S';
    //private static final String connect = CoeusGuiConstants.CONNECTION_URL+"/printServlet";
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + "/ReportConfigServlet";

    /** Creates a new instance of BudgetPersonSalariesController */
    public BudgetPersonSalariesController() {
        budgetPersonSalariesForm = new BudgetPersonSalariesForm(CoeusGuiConstants.getMDIForm(), true);
        queryEngine = QueryEngine.getInstance();
        // Bug Id #1910 - start
        dollarData = new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,true);
        // Bug Id #1910 - End
        registerComponents();
        budgetPersonSalariesForm.btnPrintSetup.setVisible(false);
    }
    
    /** Creates a new instance of BudgetPersonSalariesController and sets the form data
     * @param budgetInfoBean takes a <CODE>budgetInfoBean</CODE> */
    public BudgetPersonSalariesController( BudgetInfoBean budgetInfoBean ) {
        this();
        setFormData( budgetInfoBean );
        budgetPersonSalariesForm.btnPrintSetup.setVisible(false);
    }
    
    /** Displays the Form which is being controlled. */    
    public void display() {
        if( hasPersons ){
//            budgetPersonSalariesForm.btnPrintSetup.requestFocusInWindow();  
            budgetPersonSalariesForm.btnPrint.requestFocusInWindow();
            budgetPersonSalariesForm.dlgBudgetPersonSalaries.show();
        }else{
            message = coeusMessageResources.parseMessageKey(NO_SALARY_DETAILS);
            CoeusOptionPane.showInfoDialog(message);
            hasPersons = false;
        }
    }
    
    /** Perform field formatting.
     * enabling, disabling components depending on the function type. */
    public void formatFields() {
    }
    
    /** An overridden method of the controller
     * @return budgetTotalForm returns the controlled form component */
    public Component getControlledUI() {
        return budgetPersonSalariesForm;
    }
    
    /** An overridden method of the Controller to return the form object
     * @return returns the form object, since its not used here it returns null */
    public Object getFormData() {
        return null;
    }
    
    /** This method is used to set the listeners to the components. */
    public void registerComponents() {
        budgetPersonSalariesForm.btnClose.addActionListener(this);
        budgetPersonSalariesForm.btnPrint.addActionListener(this);
        budgetPersonSalariesForm.btnPrintSetup.addActionListener(this);

        /** Code for focus traversal - start */
        java.awt.Component[] components = { budgetPersonSalariesForm.btnClose,
                                            budgetPersonSalariesForm.btnPrint,
                                            budgetPersonSalariesForm.btnPrintSetup        
        };
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        budgetPersonSalariesForm.setFocusTraversalPolicy( traversePolicy );
        budgetPersonSalariesForm.setFocusCycleRoot(true);
        
        /** Code for focus traversal - end */
        
        budgetPersonSalariesForm.dlgBudgetPersonSalaries.addEscapeKeyListener(
            new AbstractAction("escPressed"){
                public void actionPerformed(ActionEvent ae){
                    budgetPersonSalariesForm.dlgBudgetPersonSalaries.dispose();
                }
        });
        
        budgetPersonSalariesForm.dlgBudgetPersonSalaries.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                budgetPersonSalariesForm.dlgBudgetPersonSalaries.dispose();
                return;
            }
        });
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source == budgetPersonSalariesForm.btnClose){
            budgetPersonSalariesForm.dlgBudgetPersonSalaries.dispose();
        }else if(source == budgetPersonSalariesForm.btnPrint){
            try{
                printSalaryReport();
            }catch(CoeusException coeusException){
                CoeusOptionPane.showErrorDialog(coeusException.getMessage());
                coeusException.printStackTrace();
            }
        }else if(source == budgetPersonSalariesForm.btnPrintSetup){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                "funcNotImpl_exceptionCode.1100"));
        }
    }
    
    /** Method to set the column widths of the table */   
    public void setTableEditors(){
        int[] colSize = new int[ personSalariesTableModel.getColumnCount() ];
        TableColumn tableColumn = null;
        JTableHeader header = budgetPersonSalariesForm.tblPersonSalaries.getTableHeader();
        header.setFont(CoeusFontFactory.getLabelFont());
        header.setReorderingAllowed(false);
        for(int col = 0; col < colSize.length; col++) {
            tableColumn = budgetPersonSalariesForm.tblPersonSalaries.getColumnModel().getColumn(col);
            if ( col == COST_ELEMENT_COLUMN || col == NAME_COLUMN ){
                //tableColumn.setMinWidth(NAME_COLUMN_WIDTH);
                //tableColumn.setMaxWidth(NAME_COLUMN_WIDTH + 250 );
                tableColumn.setPreferredWidth(NAME_COLUMN_WIDTH);
            }else{
                //tableColumn.setMinWidth(COLUMN_WIDTH);
                //tableColumn.setMaxWidth(COLUMN_WIDTH + 25 );
                tableColumn.setPreferredWidth(COLUMN_WIDTH);
            }
            tableColumn.setCellRenderer(personSalariesRenderer);
        }
    }
    
    /** Saves the Form Data. */
    public void saveFormData() {
    }
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form */
    public void setFormData(Object data) {
        if( data == null ) return;
        budgetInfoBean = ( BudgetInfoBean ) data;
        queryKey = budgetInfoBean.getProposalNumber() + budgetInfoBean.getVersionNumber();
        BudgetPeriodBean budgetPeriodBean = new BudgetPeriodBean();
        budgetTotalBean = new BudgetTotalBean();
        
        //Operators used to filter and retrieve unique data for the table
        Equals equalsBudgetPeriod, equalsCostElmDesc, equalsCostElm, equalsPersonName,
                equalsLineItemNum, equalsRateClassCode, equalsRateTypeCode;
        NotEquals notEqualsCostElmDesc, notEqualsLineItemNum, notEqualsCostElm,
            notEqualsRateClassCode, notEqualsRateTypeCode, notEqualsPersonName;
        And andRateClassType, andRateClassNotRateType;
        Or orOperator;
        
        CoeusVector cvPersonnelDetails = new CoeusVector();
        
        /** Check if personnel line items exist for the budget version */

        NotEquals notEqualsDelete = new NotEquals(AC_TYPE, TypeConstants.DELETE_RECORD);
        Equals equalsNull = new Equals(AC_TYPE, null);
        Or notEqualsDeleteOrEqualsNull = new Or(notEqualsDelete, equalsNull);

        try{
            cvPersonnelDetails = queryEngine.executeQuery(queryKey, BudgetPersonnelDetailsBean.class, notEqualsDeleteOrEqualsNull);
            if ( cvPersonnelDetails != null && cvPersonnelDetails.size() > 0){
                hasPersons = true;
            }else {
                return;
            }
        }catch (CoeusException coeusException) {
            coeusException.getMessage();
        }
        
        /** If personnel line items exist then proceed... */

        int budgetPeriod = 0;
        int rateClassCode = 0, rateTypeCode = 0, lineItemNumber = 0;
        String costElement, rateClassType, costElementDesc;

        CoeusVector cvPeriodLineItems = new CoeusVector();
        CoeusVector cvCostElements = new CoeusVector();
        CoeusVector cvPersonnelLineItems = new CoeusVector();
        CoeusVector cvBudgetPersons = new CoeusVector();
        CoeusVector cvPeriodCalAmts = new CoeusVector();
        CoeusVector cvSortedCEData = new CoeusVector();
        CoeusVector cvSortedPersons = new CoeusVector();
        CoeusVector cvCalAmts = new CoeusVector();
        CoeusVector cvCalAmtsExists = new CoeusVector();
        CoeusVector cvRateClassElements = new CoeusVector();
        cvTableData = new CoeusVector();
        cvSortedTableData = new CoeusVector();
        double existingSalary = 0;
        BudgetDetailBean budgetDetailBean = new BudgetDetailBean();
        BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = new BudgetPersonnelDetailsBean ();
        BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean ();

        try{
            CoeusVector cvBudgetPeriods = queryEngine.executeQuery(queryKey, BudgetPeriodBean.class, notEqualsDeleteOrEqualsNull);
            CoeusVector cvBudgetDetails = queryEngine.executeQuery(queryKey, BudgetDetailBean.class, notEqualsDeleteOrEqualsNull);
            CoeusVector cvBudgetPersonnelDetails = queryEngine.executeQuery(queryKey, BudgetPersonnelDetailsBean.class, notEqualsDeleteOrEqualsNull);
            CoeusVector cvBudgetPersonnelCalAmts = queryEngine.executeQuery(queryKey, BudgetPersonnelCalAmountsBean.class, notEqualsDeleteOrEqualsNull);

            budgetPeriods = cvBudgetPeriods.size();
            if( cvBudgetPeriods != null && cvBudgetPeriods.size() > 0){
                /** Table Data for budget personnel line items */
                /** Loop through each budget period and get the unique cost element details */
                for( int periodIndex = 0; periodIndex < cvBudgetPeriods.size(); periodIndex++){
                    budgetPeriodBean = (BudgetPeriodBean) cvBudgetPeriods.get(periodIndex);
                    budgetPeriod = budgetPeriodBean.getBudgetPeriod();
                    equalsBudgetPeriod = new Equals(BUDGET_PERIOD, new Integer(budgetPeriod));
                    /** Filter and get all budget personnel line item details for each period */
                    cvPersonnelDetails = cvBudgetPersonnelDetails.filter(equalsBudgetPeriod);
                    cvPeriodLineItems = cvBudgetDetails.filter(equalsBudgetPeriod);
                    
                    if( cvPersonnelDetails != null && cvPersonnelDetails.size() > 0){
                        while (true) {
                            /** Get the first element of the cvPeriodLineItems
                             * since it contains only one element at a time, as it is filtered
                             * after creating a new budgetTotalBean of unique cost element */
                            budgetPersonnelDetailsBean = ( BudgetPersonnelDetailsBean )cvPersonnelDetails.get(0);
                            lineItemNumber = budgetPersonnelDetailsBean.getLineItemNumber();
                            equalsLineItemNum = new Equals(LINE_ITEM_NUMBER, new Integer(lineItemNumber));
                            notEqualsLineItemNum = new NotEquals(LINE_ITEM_NUMBER, new Integer(lineItemNumber));

                            cvCostElements = cvPeriodLineItems.filter(equalsLineItemNum);

                            budgetDetailBean = (BudgetDetailBean)cvCostElements.get(0);
                            costElementDesc = budgetDetailBean.getCostElementDescription();
                            costElement = budgetDetailBean.getCostElement();
                            equalsCostElm = new Equals(COST_ELEMENT_DESCRIPTION, costElement);
                            notEqualsCostElm = new NotEquals(COST_ELEMENT_DESCRIPTION, costElement);
                            
                            equalsCostElmDesc = new Equals(LINE_ITEM_COST_ELEMENT, costElementDesc);
                            notEqualsCostElmDesc = new NotEquals(LINE_ITEM_COST_ELEMENT, costElementDesc);

                            if (cvTableData != null && cvTableData.size() > 0) {
                                /** Filter the budgetTotalBean from cvTableData 
                                 * corresponding to the given cost element and store
                                 * it into the vector cvCEExists */
                                cvCEExists = cvTableData.filter(equalsCostElmDesc);
                                if (cvCEExists.size() > 0) {
                                    /** Remove the budgetTotalBean from cvTableData
                                     * corresponding to the given cost element */
                                    for( int index = 0; index < cvTableData.size(); index ++ ) {
                                        budgetTotalBean = (BudgetTotalBean) cvTableData.get(index);
                                        String existingCEDesc = budgetTotalBean.getCostElement();
                                        if( costElementDesc.equals(existingCEDesc) ){
                                            insertIndex = index + 1;
                                            break;
                                        }
                                    }
                                }else {
                                    /** Create a new budgetTotalBean and set the required
                                     * properties other than the period cost */
                                    budgetTotalBean = new BudgetTotalBean();
                                    budgetTotalBean.setBudgetPeriods(budgetPeriods);
                                    budgetTotalBean.setCostElement(costElementDesc);
                                    budgetTotalBean.setCostElementValue(costElement);
                                    budgetTotalBean.setCostElementDescription(EMPTY_STRING);
                                    cvTableData.addElement(budgetTotalBean);
                                }
                                cvPersonnelLineItems = cvPersonnelDetails.filter(equalsLineItemNum);

                                if( cvPersonnelLineItems.size() > 0 ){
                                    for( int personIndex = 0; personIndex < cvPersonnelLineItems.size(); personIndex++ ){
                                        budgetPersonnelDetailsBean = ( BudgetPersonnelDetailsBean )
                                            cvPersonnelLineItems.get(personIndex);
                                        
                                        lineItemNumber = budgetPersonnelDetailsBean.getLineItemNumber();
                                        equalsLineItemNum = new Equals(LINE_ITEM_NUMBER, new Integer(lineItemNumber));
                                        notEqualsLineItemNum = new NotEquals(LINE_ITEM_NUMBER, new Integer(lineItemNumber));
                                        
                                        CoeusVector cvCEDesc = cvPeriodLineItems.filter(equalsLineItemNum);

                                        budgetDetailBean = (BudgetDetailBean)cvCEDesc.get(0);
                                        String CEDesc = ":"+budgetDetailBean.getCostElementDescription();
                                        Equals eqCEDesc = new Equals(LINE_ITEM_COST_ELEMENT, CEDesc);
                                        
                                        equalsPersonName = new Equals(COST_ELEMENT_DESCRIPTION,
                                            budgetPersonnelDetailsBean.getFullName());
                                        notEqualsPersonName = new NotEquals(COST_ELEMENT_DESCRIPTION,
                                            budgetPersonnelDetailsBean.getFullName());
                                        And eqCEDescAndEqPerson = new And(eqCEDesc, equalsPersonName );
                                        cvBudgetPersons = cvTableData.filter(eqCEDescAndEqPerson);
                                        int newInsertIndex = 0;
                                        if (cvBudgetPersons.size() > 0 ){
                                            budgetTotalBean = (BudgetTotalBean)cvBudgetPersons.get(0);
                                            budgetTotalBean = (BudgetTotalBean)cvBudgetPersons.get(0);
                                            String existingCEDesc = budgetTotalBean.getCostElement();
                                            String existingPerson = budgetTotalBean.getCostElementDescription();
                                            for( int tableIndex = 0; tableIndex < cvTableData.size(); tableIndex ++ ) {
                                                budgetTotalBean = (BudgetTotalBean) cvTableData.get(tableIndex);
                                                existingSalary = budgetTotalBean.getPeriodCost(periodIndex);
                                                if( budgetTotalBean.getCostElement().equals(existingCEDesc) &&
                                                budgetTotalBean.getCostElementDescription().equals(existingPerson)){
                                                    newInsertIndex = tableIndex;
                                                    break;
                                                }
                                            }
                                            budgetTotalBean.setPeriodCost(periodIndex, existingSalary + 
                                                budgetPersonnelDetailsBean.getSalaryRequested());
                                            existingSalary = 0;
                                        }else{
                                            boolean hasCEDesc = false;
                                            int existingIndex = 0;
                                            for( int tableIndex = 0; tableIndex < cvTableData.size(); tableIndex ++ ) {
                                                budgetTotalBean = (BudgetTotalBean) cvTableData.get(tableIndex);
                                                if( budgetTotalBean.getCostElement().equals(":"+costElementDesc) ){
                                                    hasCEDesc = true;
                                                    existingIndex = tableIndex;
                                                    break;
                                                }
                                            }

                                            BudgetTotalBean budgetTotalBean = new BudgetTotalBean();
                                            budgetTotalBean.setBudgetPeriods(budgetPeriods);
                                            budgetTotalBean.setCostElement(":" + costElementDesc);
                                            budgetTotalBean.setCostElementDescription(
                                                budgetPersonnelDetailsBean.getFullName());
                                            budgetTotalBean.setCostElementValue(costElement);
                                            budgetTotalBean.setPersonId(budgetPersonnelDetailsBean.getPersonId());
                                            budgetTotalBean.setPeriodCost(periodIndex,
                                                budgetPersonnelDetailsBean.getSalaryRequested());
                                            if ( hasCEDesc ){
                                                cvTableData.add(existingIndex, budgetTotalBean);
                                                hasCEDesc = false;
                                            }else{
                                                cvTableData.addElement(budgetTotalBean);
                                            }
                                        }
                                    }
                                }
                                cvPersonnelDetails = cvPersonnelDetails.filter(notEqualsLineItemNum);                                
                            }else {
                                /** Create a new budgetTotalBean and set the required
                                 * properties other than the period cost */
                                budgetTotalBean = new BudgetTotalBean();
                                budgetTotalBean.setBudgetPeriods(budgetPeriods);
                                budgetTotalBean.setCostElement(costElementDesc);
                                budgetTotalBean.setCostElementValue(costElement);
                                budgetTotalBean.setCostElementDescription(EMPTY_STRING);
                                cvTableData.addElement(budgetTotalBean);
                            }
                            /** Remove all the budgetDetailBeans from cvPeriodLineItems
                             * corresponding to the given cost element */
                            if (cvPersonnelDetails.size() == 0) {
                                break;
                            }
                        }
                    }
                }

                /** Table Data for personnel budget line item calculated amounts */
                /* Loop through each budget period and get the unique
                 * line item calculated amounts */
                for( int periodIndex = 0; periodIndex < cvBudgetPeriods.size(); periodIndex++){
                    budgetPeriodBean = (BudgetPeriodBean) cvBudgetPeriods.get(periodIndex);
                    budgetPeriod = budgetPeriodBean.getBudgetPeriod();
                    equalsBudgetPeriod = new Equals(BUDGET_PERIOD, new Integer(budgetPeriod));
                    cvPeriodCalAmts = cvBudgetPersonnelCalAmts.filter(equalsBudgetPeriod);
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
                            value = cvRateClassElements.sum(CALCULATED_COST, andRateClassType);

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
                                budgetTotalBean.setCostElement(EMPTY_STRING);//setCostElementDescription
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
                    /** Filter and get all the cost element descriptions
                     * and sort it in ascending order */
                    Equals eqEmptyString = new Equals(COST_ELEMENT_DESCRIPTION, EMPTY_STRING);
                    cvSortedCEData = cvTableData.filter(eqEmptyString);
                    cvSortedCEData.sort(COST_ELEMENT_VALUE, true);
                    for( int CEIndex = 0; CEIndex < cvSortedCEData.size(); CEIndex++){
                        budgetTotalBean = (BudgetTotalBean)cvSortedCEData.get(CEIndex);
                        cvSortedTableData.addElement(budgetTotalBean);
                        String CEDesc = ":" + budgetTotalBean.getCostElement();
                        Equals eqSortedCE = new Equals(LINE_ITEM_COST_ELEMENT, CEDesc);
                        cvSortedPersons = cvTableData.filter(eqSortedCE);
                        cvSortedPersons.sort(PERSON_ID, true);
                        /** Sort all the persons in each cost element in
                         * ascending order of personId */
                        for( int personIndex = 0; personIndex < cvSortedPersons.size(); personIndex++){
                            cvSortedTableData.addElement(cvSortedPersons.get(personIndex));
                        }
                    }
                    BudgetTotalBean budgetTotalBean = new BudgetTotalBean();
                    budgetTotalBean.setBudgetPeriods(budgetPeriods);
                    budgetTotalBean.setCostElement(CALCULATED_AMTS);
                    budgetTotalBean.setCostElementDescription(EMPTY_STRING);
                    cvSortedTableData.addElement(budgetTotalBean);
                    initialVecSize = cvSortedTableData.size();
                }
                if( cvCalAmts != null && cvCalAmts.size() > 0){
                    /** Sort the vector based on <CODE>rateClassCode</CODE> */
                    cvCalAmts.sort(RATE_CLASS_CODE, true);
                }
                for( int index = 0; index < cvCalAmts.size(); index++){
                    cvSortedTableData.addElement(cvCalAmts.get(index));
                }
                
                if( cvBudgetDetails != null &&  cvBudgetDetails.size() > 0 ){
                    boolean hasCostElement = false;
                    /** Check if the budgetDetailBean contains Cost Element */
                    for( int budgetDetailIndex = 0; budgetDetailIndex < cvBudgetDetails.size();
                    budgetDetailIndex++){
                        budgetDetailBean = (BudgetDetailBean)cvBudgetDetails.get(budgetDetailIndex);
                        if( !budgetDetailBean.getCostElement().equals(EMPTY_STRING) ){
                            hasCostElement = true;
                        }
                    }
                    
                    if( hasCostElement ){
                        /** Data for the last row of the table */
                        budgetTotalBean = new BudgetTotalBean();
                        budgetTotalBean.setCostElement(EMPTY_STRING);
                        budgetTotalBean.setCostElementDescription(TOTAL);
                        budgetTotalBean.setBudgetPeriods(budgetPeriods);
                        for(int index = 0; index < budgetPeriods; index++ ){
                            value = cvSortedTableData.sum(PERIOD_COST_VALUE , 
                                DataType.getClass(DataType.INT) , new Integer(index));
                            // Bug Id #1910 - Start
                            value = (double)Math.round (value*Math.pow(10.0, 2) ) / 100;
                            // Bug Id #1910 - End
                            budgetTotalBean.setPeriodCost(index, value);
                        }
                        cvSortedTableData.addElement(budgetTotalBean);
                    }

                }
                /** Data for the Total column of the table */
                /** Set the total property for all the budgetTotalBeans */
                for( int totalIndex = 0; totalIndex < cvSortedTableData.size(); totalIndex ++){
                    budgetTotalBean = ( BudgetTotalBean )cvSortedTableData.get(totalIndex);
                    budgetTotalBean.setTotal();
                }                
            }
        }catch (NumberFormatException numberFormatException ){
            numberFormatException.getMessage();
        }catch (CoeusException coeusException){
            coeusException.getMessage();
        }
        personSalariesTableModel= new PersonSalariesTableModel();
        budgetPersonSalariesForm.tblPersonSalaries.setModel(personSalariesTableModel);
        personSalariesTableModel.setData(cvSortedTableData);
        personSalariesRenderer = new PersonSalariesRenderer();
        setTableEditors();
    }

    //add for printing budgetSalaries
    public void printSalaryReport()throws CoeusException{
        Hashtable htPrintParams = new Hashtable(); 
        htPrintParams.put("PROPOSAL_NUM",budgetInfoBean.getProposalNumber());
        htPrintParams.put("VERSION_NUM",""+budgetInfoBean.getVersionNumber());
        htPrintParams.put("START_DATE",budgetInfoBean.getStartDate());
        htPrintParams.put("END_DATE",budgetInfoBean.getEndDate());
        htPrintParams.put("DATA",cvSortedTableData);
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(BUDGET_SALARY_REPORT);
        requester.setDataObject(htPrintParams);
        
        //For Streaming
        requester.setId("Budget/Salaries");
        requester.setFunctionType('R');
        //For Streaming
        
        AppletServletCommunicator comm
         = new AppletServletCommunicator(connect, requester);
         
        comm.send();
        ResponderBean responder = comm.getResponse();
        String fileName = "";
        if(responder.isSuccessfulResponse()){
//             AppletContext coeusContxt = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
//             
//           
             fileName = (String)responder.getDataObject();
//             System.out.println("Report Filename is=>"+fileName);
//             
//             fileName.replace('\\', '/') ; // this is fix for Mac
//             URL reportUrl = null;
//             try{
//                reportUrl = new URL( CoeusGuiConstants.CONNECTION_URL + fileName );
//             
//             
//             if (coeusContxt != null) {
//                 coeusContxt.showDocument( reportUrl, "_blank" );
//             }else {
//                 javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
//                 bs.showDocument( reportUrl );
//             }
//             }catch(MalformedURLException muEx){
//                 throw new CoeusException(muEx.getMessage());
//             }catch(Exception uaEx){
//                 throw new CoeusException(uaEx.getMessage());
//             }
             try{
                 URL url = new URL(fileName);
                 URLOpener.openUrl(url);
             }catch (MalformedURLException malformedURLException) {
                 throw new CoeusException(malformedURLException.getMessage());
             }
         }else{
             throw new CoeusException(responder.getMessage());
         }
                 
    }
    
    /** Validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if validation is through else returns false.
     * here since the form is only for disply it always returns false */
    public boolean validate() throws CoeusUIException {
        return false;
    }
    
    //Inner Class Table Model - Start
    class PersonSalariesTableModel extends AbstractTableModel{

        String[] colNames = {COST_ELEMENT, NAME, "Period1", EMPTY_STRING};

        Class[] types = new Class [] {
            String.class,String.class,String.class,String.class
        };
        
        private Vector dataBean;
        private static final int STATIC_COLUMNS = 2;
        
        PersonSalariesTableModel() {
            //Creating the table columns
            if( budgetPeriods > 1){
                colNames = new String[ (colNames.length - 1) + budgetPeriods];
                types = new Class[(colNames.length - 1) + budgetPeriods];
                types[COST_ELEMENT_COLUMN] = String.class;
                types[NAME_COLUMN] = String.class;
                colNames[COST_ELEMENT_COLUMN] = COST_ELEMENT;
                colNames[NAME_COLUMN] = NAME;
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
            String costElementDesc;
            switch (column) {
                case COST_ELEMENT_COLUMN:
                    if( budgetTotalBean.getCostElement().startsWith(":")){
                        return EMPTY_STRING;
                    }else{
                        return budgetTotalBean.getCostElement();
                    }
                case NAME_COLUMN:
                    return budgetTotalBean.getCostElementDescription();
                    
                default:
                    if(column == totalColumns) {
                        costElementDesc = budgetTotalBean.getCostElementDescription();
                        if( costElementDesc.equals(EMPTY_STRING)){
                            return EMPTY_STRING;
                        }else{
                            dollarData.setText(EMPTY_STRING+budgetTotalBean.getTotal());
                            return dollarData.getText();
                        }
                    }else{
                        periodCost =  budgetTotalBean.getPeriodCost(column - STATIC_COLUMNS);
                        if (periodCost == INITIAL_VALUE) {
                            return EMPTY_STRING;
                        } else { 
                            dollarData.setText(EMPTY_STRING+periodCost);
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
    
    //PersonSalariesRenderer - Start
    class PersonSalariesRenderer extends DefaultTableCellRenderer {
        
        private JLabel label;

        PersonSalariesRenderer() {
            label = new JLabel();            
        }
        
        public Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            label.setFont(CoeusFontFactory.getNormalFont());
            if( row >= initialVecSize && column < PersonSalariesTableModel.STATIC_COLUMNS ){
                /** Set the brown color to the line item calculated amounts description */
                label.setForeground(new Color(185,98,83));
            }else{
                label.setForeground(Color.black);
            }
            if( row == ( cvSortedTableData.size() - 1 )){//cvTableData
                /** Set the bold font to the last row of the table */
                label.setForeground(Color.black);
                label.setFont( CoeusFontFactory.getLabelFont() );
                if( column == totalColumns ) {
                    /** Set the blue color to the last cell of the table */
                    label.setForeground(Color.blue);
                }
            }
            if( column == totalColumns ){
                /** Set the bold font to the last column of the table */
                label.setFont( CoeusFontFactory.getLabelFont() );
            }            
            if( column >= PersonSalariesTableModel.STATIC_COLUMNS ){
                /** Set right alignment to all columns containing currency data */
                label.setHorizontalAlignment(JLabel.RIGHT);
            }else{
                label.setHorizontalAlignment(JLabel.LEFT);
            }
            if( value != null ){
                label.setText(" "+value.toString());
            }
            return label;
        }
    }//PersonSalariesRenderer - End
    
}
