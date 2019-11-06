/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables 
 * on 25-JULY-2011 by Satheesh Kumar K N
 */
package edu.mit.coeus.budget.controller;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import javax.swing.*;
import java.text.ParseException;
import javax.swing.event.*;
import javax.swing.table.*;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.budget.controller.Controller;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.budget.gui.BudgetLineItemDetailsForm;
import edu.mit.coeus.gui.event.*;

/** This class controls the <CODE>BudgetLineItemDetailsForm</CODE>
 * BudgetLineItemController.java
 * @author  Vyjayanthi
 * Created on October 13, 2003, 3:11 PM
 */
public class BudgetLineItemController extends Controller implements
TypeConstants, ActionListener{
    
    private BudgetLineItemDetailsForm budgetLineItemDetailsForm;
    private QueryEngine queryEngine;
    private String queryKey;
    private Vector vecCategory;
    private boolean showJustification;
    private DateUtils dateUtils;
    private boolean hasPersons = false;
    private boolean hasMultiplePersons = false;
    private boolean dataModified = false;
    private java.text.SimpleDateFormat dtFormat
            = new java.text.SimpleDateFormat("MM/dd/yyyy");
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String EMPTY_STRING = "";
    private String previousStartDate, previousEndDate;
    private java.sql.Date periodStartDate, periodEndDate;
    private BudgetDetailBean budgetDetailBean;
    private BudgetPeriodBean budgetPeriodBean;
    private Vector vecPersonnelDetails;
    private ScreenFocusTraversalPolicy  traversePolicy;
    private static final int WIDTH = 730;
    private static final int ORIGINAL_HEIGHT = 315;
    private static final int NEW_HEIGHT = 475;
    private BudgetDetailBean newBean;
    private String message = EMPTY_STRING;
    private char categoryType;
    
    private static final String PROPOSAL = "proposalNumber";
    private static final String VERSION = "versionNumber";
    private static final String BUDGET_PERIOD = "budgetPeriod";
    private static final String AC_TYPE = "acType";
    private static final String LINE_ITEM_NUMBER = "lineItemNumber";
    
    private static final String INVALID_START_DATE = "budget_common_exceptionCode.1001";
    private static final String INVALID_END_DATE = "budget_common_exceptionCode.1002";
    private static final String START_DATE_LATER_THAN_END_DATE = "budgetLineItem_exceptionCode.1351";
    private static final String START_DATE_BETWEEN_PERIOD = "budgetLineItem_exceptionCode.1352";
    private static final String END_DATE_EARLIER_THAN_START_DATE = "budgetLineItem_exceptionCode.1353";
    private static final String END_DATE_BETWEEN_PERIOD = "budgetLineItem_exceptionCode.1354";
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    
    /** Creates a new instance of BudgetLineItemController */    
    public BudgetLineItemController(){
        dateUtils = new DateUtils();
        queryEngine = QueryEngine.getInstance();
        this.budgetDetailBean = budgetDetailBean;
        budgetLineItemDetailsForm = new BudgetLineItemDetailsForm(
                CoeusGuiConstants.getMDIForm(), true);
        budgetLineItemDetailsForm.remove(
                budgetLineItemDetailsForm.pnlJustify);
        budgetLineItemDetailsForm.dlgBudgetLineItem.setSize(WIDTH,ORIGINAL_HEIGHT);
        registerComponents();
    }
    
    /** Creates a new instance of BudgetLineItemController taking the budgetDetailBean
     * @param budgetDetailBean takes the budget details bean */
    public BudgetLineItemController( BudgetDetailBean budgetDetailBean ) {
        this();
        setFormData(budgetDetailBean);
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source == budgetLineItemDetailsForm.btnJustify){
            if ( !showJustification ){
                budgetLineItemDetailsForm.dlgBudgetLineItem.setSize(WIDTH,NEW_HEIGHT);
                java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 8;
                budgetLineItemDetailsForm.add(
                        budgetLineItemDetailsForm.pnlJustify, gridBagConstraints);
                /** Add the Justification text area component to the array */
                //Code for focus traversal - start
                java.awt.Component[] components = { budgetLineItemDetailsForm.cmbCategory,
                                                    budgetLineItemDetailsForm.rdBtnOnCampus,
                                                    budgetLineItemDetailsForm.rdBtnOffCampus,
                                                    budgetLineItemDetailsForm.txtStartDate,
                                                    budgetLineItemDetailsForm.txtEndDate,
                                                    budgetLineItemDetailsForm.txtArDescription,
                                                    budgetLineItemDetailsForm.txtCost,
                                                    budgetLineItemDetailsForm.txtQuantity,
                                                    budgetLineItemDetailsForm.chkInflation,
                                                    budgetLineItemDetailsForm.txtCostSharing,
                                                    budgetLineItemDetailsForm.chkSubmitCostSharing, //COEUSQA-1693 - Cost Sharing Submission 
                                                    budgetLineItemDetailsForm.txtArJustification,
                                                    budgetLineItemDetailsForm.btnOk,
                                                    budgetLineItemDetailsForm.btnCancel,
                                                    budgetLineItemDetailsForm.btnJustify,
                                                    budgetLineItemDetailsForm.pnlLineItemCalAmtsTable.tblCalculatedAmounts
                };
                traversePolicy = new ScreenFocusTraversalPolicy( components );
                budgetLineItemDetailsForm.setFocusTraversalPolicy(traversePolicy);
                budgetLineItemDetailsForm.setFocusCycleRoot(true);
                //Code for focus traversal - end
                showJustification = true;
            }else{
                budgetLineItemDetailsForm.remove(
                         budgetLineItemDetailsForm.pnlJustify);                
                budgetLineItemDetailsForm.dlgBudgetLineItem.setSize(WIDTH,ORIGINAL_HEIGHT);
                /** Remove the Justification text area component from the array */
                //Code for focus traversal - start
                java.awt.Component[] components = { budgetLineItemDetailsForm.cmbCategory,
                                                    budgetLineItemDetailsForm.rdBtnOnCampus,
                                                    budgetLineItemDetailsForm.rdBtnOffCampus,
                                                    budgetLineItemDetailsForm.txtStartDate,
                                                    budgetLineItemDetailsForm.txtEndDate,
                                                    budgetLineItemDetailsForm.txtArDescription,
                                                    budgetLineItemDetailsForm.txtCost,
                                                    budgetLineItemDetailsForm.txtQuantity,
                                                    budgetLineItemDetailsForm.chkInflation,
                                                    budgetLineItemDetailsForm.txtCostSharing,
                                                    budgetLineItemDetailsForm.chkSubmitCostSharing, //COEUSQA-1693 - Cost Sharing Submission 
                                                    budgetLineItemDetailsForm.btnOk,
                                                    budgetLineItemDetailsForm.btnCancel,
                                                    budgetLineItemDetailsForm.btnJustify,
                                                    budgetLineItemDetailsForm.pnlLineItemCalAmtsTable.tblCalculatedAmounts
                };
                traversePolicy = new ScreenFocusTraversalPolicy( components );
                budgetLineItemDetailsForm.setFocusTraversalPolicy(traversePolicy);
                budgetLineItemDetailsForm.setFocusCycleRoot(true);
                //Code for focus traversal - end
                showJustification = false;
            }

        }else if ( source == budgetLineItemDetailsForm.btnOk ){
            dataModified = isDataModified() ;
            setSaveRequired( dataModified || isTableDataModified());
            saveFormData();
                        
        }
        else if ( source == budgetLineItemDetailsForm.btnCancel ){
            performWindowClosing();
        }
    }

    public void display(){
        budgetLineItemDetailsForm.dlgBudgetLineItem.show();
    }
    /** Displays the Form which is being controlled. */
    public boolean displayDetails() {
        budgetLineItemDetailsForm.dlgBudgetLineItem.setVisible(true);
        return dataModified;
    }
    
    /** perform field formatting.
     * enabling, disabling components depending on the
     * function type.
     */
    public void formatFields() {
        if(getFunctionType() != DISPLAY_MODE){
            // Modified for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
            // If the line item is from sub award, only Submit cost sharing field is allowed to edit
            if(budgetDetailBean != null && budgetDetailBean.getSubAwardNumber() > 0){
                budgetLineItemDetailsForm.cmbCategory.setEnabled(false);        
                budgetLineItemDetailsForm.rdBtnOnCampus.setEnabled(false); 
                budgetLineItemDetailsForm.rdBtnOffCampus.setEnabled(false);              
                budgetLineItemDetailsForm.txtStartDate.setEditable(false);
                budgetLineItemDetailsForm.txtEndDate.setEditable(false);
                budgetLineItemDetailsForm.txtCost.setEditable(false);
                budgetLineItemDetailsForm.txtQuantity.setEditable(false);
                budgetLineItemDetailsForm.chkInflation.setEnabled(false);
                budgetLineItemDetailsForm.txtCostSharing.setEditable(false);
                budgetLineItemDetailsForm.txtArDescription.setEditable(false);
                budgetLineItemDetailsForm.txtArDescription.setBackground(
                        (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
         // commented for COEUSQA-4060 OH calc flag is allowed to edit       
              //  budgetLineItemDetailsForm.pnlLineItemCalAmtsTable.tblCalculatedAmounts.setEnabled(false);
        // commented for COEUSQA-4060 OH calc flag is allowed to edit         
                budgetLineItemDetailsForm.btnOk.setEnabled(true);
                budgetLineItemDetailsForm.btnJustify.setEnabled(false);
            }else{
                if( hasPersons ) {
                    //disable all components except the description txt area and justication txt area
                    budgetLineItemDetailsForm.cmbCategory.setEnabled(false);
                    // Modified for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item -Start
                    // Check if same line item has multiple persons. Restricted to line items with only ONE detail line is in the Personnel Budget Detail window
                    if(hasMultiplePersons){
                        budgetLineItemDetailsForm.rdBtnOnCampus.setEnabled(false);
                        budgetLineItemDetailsForm.rdBtnOffCampus.setEnabled(false);
                    }else{
                        budgetLineItemDetailsForm.rdBtnOnCampus.setEnabled(true);
                        budgetLineItemDetailsForm.rdBtnOffCampus.setEnabled(true);
                        
                    }
//                budgetLineItemDetailsForm.rdBtnOnCampus.setEnabled(false);
//                budgetLineItemDetailsForm.rdBtnOffCampus.setEnabled(false);
                    // Modified for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item - end
                    budgetLineItemDetailsForm.txtStartDate.setEditable(false);
                    budgetLineItemDetailsForm.txtEndDate.setEditable(false);
                    budgetLineItemDetailsForm.txtCost.setEditable(false);
                    budgetLineItemDetailsForm.txtQuantity.setEditable(false);
                    budgetLineItemDetailsForm.chkInflation.setEnabled(false);
                    budgetLineItemDetailsForm.txtCostSharing.setEditable(false);
                }else{
                    budgetLineItemDetailsForm.cmbCategory.setEnabled(true);
                    budgetLineItemDetailsForm.rdBtnOnCampus.setEnabled(true);
                    budgetLineItemDetailsForm.rdBtnOffCampus.setEnabled(true);
                    budgetLineItemDetailsForm.txtStartDate.setEditable(true);
                    budgetLineItemDetailsForm.txtEndDate.setEditable(true);
                    budgetLineItemDetailsForm.txtCost.setEditable(true);
                    budgetLineItemDetailsForm.txtQuantity.setEditable(true);
                    budgetLineItemDetailsForm.chkInflation.setEnabled(true);
                    budgetLineItemDetailsForm.txtCostSharing.setEditable(true);
                }
                budgetLineItemDetailsForm.btnOk.setEnabled(true);
                budgetLineItemDetailsForm.btnJustify.setEnabled(true);
                budgetLineItemDetailsForm.txtArDescription.setEditable(true);
                budgetLineItemDetailsForm.chkSubmitCostSharing.setEnabled(true); //COEUSQA-1693 - Cost Sharing Submission
                budgetLineItemDetailsForm.txtArDescription.setBackground(Color.WHITE);
            }
            // Modified for COEUSQA-2115 Subaward budgeting for Proposal Development - End
        }else{
            budgetLineItemDetailsForm.cmbCategory.setEnabled(false);
            budgetLineItemDetailsForm.rdBtnOnCampus.setEnabled(false);
            budgetLineItemDetailsForm.rdBtnOffCampus.setEnabled(false);
            budgetLineItemDetailsForm.txtStartDate.setEditable(false);
            budgetLineItemDetailsForm.txtEndDate.setEditable(false);
            budgetLineItemDetailsForm.txtCost.setEditable(false);
            budgetLineItemDetailsForm.txtQuantity.setEditable(false);
            budgetLineItemDetailsForm.chkInflation.setEnabled(false);
            budgetLineItemDetailsForm.chkSubmitCostSharing.setEnabled(false); //COEUSQA-1693 - Cost Sharing Submission 
            budgetLineItemDetailsForm.txtCostSharing.setEditable(false);
            budgetLineItemDetailsForm.txtArDescription.setEditable(false);
            budgetLineItemDetailsForm.txtArDescription.setBackground(
                (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            budgetLineItemDetailsForm.pnlLineItemCalAmtsTable.tblCalculatedAmounts.setEnabled(false);
            budgetLineItemDetailsForm.btnOk.setEnabled(false);
            budgetLineItemDetailsForm.btnJustify.setEnabled(false);
        }
    }
    
    /** returns the Component which is being controlled by this Controller.
     * @return Component which is being controlled by this Controller.
     */
    public Component getControlledUI() {
        return budgetLineItemDetailsForm;
    }
    
    /** returns the form data
     * @return the form data
     */
    public Object getFormData() {
        return budgetLineItemDetailsForm;
    }
    
    /** registers GUI Components with event Listeners.
     */
    public void registerComponents() {
        DecimalFormat decimalFormat = (DecimalFormat)NumberFormat.getNumberInstance();
        decimalFormat.setMinimumIntegerDigits(1);
        decimalFormat.setMaximumIntegerDigits(4);
        
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setDecimalSeparatorAlwaysShown(true);
        
        JTextField textField = budgetLineItemDetailsForm.txtQuantity;
        FormattedDocument formattedDocument = new FormattedDocument(decimalFormat,textField);
        formattedDocument.setNegativeAllowed(true);
        textField.setDocument(formattedDocument);
        textField.setText("0.00");
        textField.setHorizontalAlignment(JFormattedTextField.RIGHT);
        
        /** Initial array of components when the form is instantiated
         * (ie. when Justification text area is not displayed) */
        //Code for focus traversal - start
        java.awt.Component[] components = { budgetLineItemDetailsForm.cmbCategory,
                                            budgetLineItemDetailsForm.rdBtnOnCampus,
                                            budgetLineItemDetailsForm.rdBtnOffCampus,
                                            budgetLineItemDetailsForm.txtStartDate,
                                            budgetLineItemDetailsForm.txtEndDate,
                                            budgetLineItemDetailsForm.txtArDescription,
                                            budgetLineItemDetailsForm.txtCost,
                                            budgetLineItemDetailsForm.txtQuantity,
                                            budgetLineItemDetailsForm.chkInflation,
                                            budgetLineItemDetailsForm.txtCostSharing,
                                            budgetLineItemDetailsForm.chkSubmitCostSharing,  //COEUSQA-1693 - Cost Sharing Submission 
                                            budgetLineItemDetailsForm.btnOk,
                                            budgetLineItemDetailsForm.btnCancel,
                                            budgetLineItemDetailsForm.btnJustify,
                                            budgetLineItemDetailsForm.pnlLineItemCalAmtsTable.tblCalculatedAmounts
        };
        traversePolicy = new ScreenFocusTraversalPolicy( components );
        budgetLineItemDetailsForm.setFocusTraversalPolicy(traversePolicy);
        budgetLineItemDetailsForm.setFocusCycleRoot(true);
        //Code for focus traversal - end

        budgetLineItemDetailsForm.txtStartDate.addFocusListener(new CustomFocusAdapter());        
        budgetLineItemDetailsForm.txtEndDate.addFocusListener(new CustomFocusAdapter());
        budgetLineItemDetailsForm.txtQuantity.addFocusListener(new CustomFocusAdapter());//Added for Case#3121 Tuition Fee Calculation
        budgetLineItemDetailsForm.btnOk.addActionListener(this);
        budgetLineItemDetailsForm.btnCancel.addActionListener(this);
        budgetLineItemDetailsForm.btnJustify.addActionListener(this);
        
        budgetLineItemDetailsForm.dlgBudgetLineItem.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performWindowClosing();
                return;
            }

        });
        
        budgetLineItemDetailsForm.dlgBudgetLineItem.addEscapeKeyListener(
            new AbstractAction("escPressed"){
                public void actionPerformed(ActionEvent ae){
                    performWindowClosing();
                }
        });
        
        budgetLineItemDetailsForm.dlgBudgetLineItem.addComponentListener(
            new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    requestDefaultFocus();
                }
        });
    }
    
    /** To set the default focus for the components depending 
     * on the function type */
    private void requestDefaultFocus(){
        budgetLineItemDetailsForm.dlgBudgetLineItem.setLocation(CoeusDlgWindow.CENTER);
        if( getFunctionType() == MODIFY_MODE || getFunctionType() == ADD_MODE ){
            if( hasPersons ) {
                budgetLineItemDetailsForm.txtArDescription.requestFocus();
                hasPersons = false;
            }else {
                budgetLineItemDetailsForm.cmbCategory.requestFocus();
            }
        }else{
            budgetLineItemDetailsForm.btnCancel.requestFocus();
        }
    }
    /** Method to check if the data in the table has changed
     * @return true if table data is modified false otherwise
     */
    private boolean isTableDataModified(){
        return budgetLineItemDetailsForm.pnlLineItemCalAmtsTable.isSaveRequired();
    }
    
    /** Method to check if the data in the form has changed
     * @return true if data is modified false otherwise
     */
    public boolean isDataModified(){
        newBean = new BudgetDetailBean();
        boolean isEqual = false;
        Date modifiedDate;
        String value;
        CoeusVector vecNewBean;
        queryKey = budgetDetailBean.getProposalNumber() + budgetDetailBean.getVersionNumber();
        newBean.setProposalNumber(budgetDetailBean.getProposalNumber());
        newBean.setVersionNumber(budgetDetailBean.getVersionNumber());
        newBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
        newBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
        try{
            vecNewBean = queryEngine.executeQuery(queryKey, budgetDetailBean);
            if( vecNewBean != null && vecNewBean.size() > 0 ){
                newBean = (BudgetDetailBean)vecNewBean.get(0);

                ComboBoxBean categoryBean = 
                        (ComboBoxBean)budgetLineItemDetailsForm.cmbCategory.getSelectedItem();
                //BudgetCategoryBean budgetCategoryBean = ( BudgetCategoryBean )categoryBean;
                categoryType = getBudgetCategoryType(categoryBean.getCode());
                //if( !categoryBean.getCode().equals(EMPTY_STRING) )
                //if( budgetDetailBean.getBudgetCategoryCode() == 0 ){
                    //newBean.setBudgetCategoryCode(0);
                //}else {
                if (categoryBean.getCode().trim().length() > 0 ){
                    newBean.setBudgetCategoryCode(Integer.parseInt( categoryBean.getCode()) );
                }else{
                    newBean.setBudgetCategoryCode(0);
                }
                newBean.setBudgetJustification(budgetLineItemDetailsForm.txtArJustification.getText().trim());
                newBean.setLineItemDescription(budgetLineItemDetailsForm.txtArDescription.getText().trim());
                newBean.setApplyInRateFlag(budgetLineItemDetailsForm.chkInflation.isSelected());
                //COEUSQA-1693 - Cost Sharing Submission - start
                 newBean.setSubmitCostSharingFlag(budgetLineItemDetailsForm.chkSubmitCostSharing.isSelected());
                //COEUSQA-1693 - Cost Sharing Submission - end  
                               
                newBean.setBasedOnLineItem(budgetDetailBean.getBasedOnLineItem());
                if( budgetDetailBean.getBudgetCategoryCode() != newBean.getBudgetCategoryCode() ){
                    newBean.setCategoryType(categoryType);
                }else{
                    newBean.setCategoryType(budgetDetailBean.getCategoryType());
                }
                newBean.setCostElement(budgetDetailBean.getCostElement());
                newBean.setCostElementDescription(budgetDetailBean.getCostElementDescription());
                newBean.setDirectCost(budgetDetailBean.getDirectCost());
                newBean.setIndirectCost(budgetDetailBean.getIndirectCost());
                newBean.setLineItemSequence(budgetDetailBean.getLineItemSequence());
                newBean.setTotalCostSharing(budgetDetailBean.getTotalCostSharing());
                newBean.setUnderRecoveryAmount(budgetDetailBean.getUnderRecoveryAmount());
                newBean.setCostSharingAmount(budgetDetailBean.getCostSharingAmount());
                newBean.setAcType(budgetDetailBean.getAcType());
                
                String quantity = budgetLineItemDetailsForm.txtQuantity.getText();
                if( !(quantity.trim().equals(EMPTY_STRING))){
                    //Modified for Case #3132 - start
                    //Changing quantity field from integer to float
//                    newBean.setQuantity(Integer.parseInt(quantity));
                    quantity = quantity.replaceAll(",", "");
                    newBean.setQuantity(Double.parseDouble(quantity));
                    //Modified for Case #3132 - end
                }else {
                    newBean.setQuantity(0);
                }
                newBean.setOnOffCampusFlag(budgetLineItemDetailsForm.rdBtnOnCampus.isSelected());

                value = dateUtils.restoreDate(budgetLineItemDetailsForm.txtStartDate.getText(),DATE_SEPARATERS);
                if ( value.trim().length() >0 && value != null ){
                    modifiedDate = dtFormat.parse(value);
                    newBean.setLineItemStartDate( new java.sql.Date(modifiedDate.getTime()));
                }
                
                value = dateUtils.restoreDate(budgetLineItemDetailsForm.txtEndDate.getText(),DATE_SEPARATERS);
                if( value.trim().length() >0 && value != null ){
                    modifiedDate = dtFormat.parse(value);
                    newBean.setLineItemEndDate( new java.sql.Date(modifiedDate.getTime()));
                }
                
                newBean.setCostSharingAmount(Double.parseDouble(budgetLineItemDetailsForm.txtCostSharing.getValue()));
                newBean.setLineItemCost(Double.parseDouble(budgetLineItemDetailsForm.txtCost.getValue()));
                
                StrictEquals strictEquals = new StrictEquals();
                isEqual = strictEquals.compare(budgetDetailBean, newBean);
            }else{
                return isEqual;
            }
        }catch (CoeusException coeusException) {
            coeusException.getMessage();
        }catch (NumberFormatException numFormatException){
            numFormatException.printStackTrace();
        }catch (ParseException parseException){
            parseException.getMessage();
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return !isEqual;
    }

    /** saves the Form Data.
     */
    public void saveFormData() {
        boolean dataSaved = false;
        if ( !isSaveRequired() ){
            budgetLineItemDetailsForm.dlgBudgetLineItem.dispose();
            budgetLineItemDetailsForm.remove(
                    budgetLineItemDetailsForm.pnlJustify);
            budgetLineItemDetailsForm.dlgBudgetLineItem.setSize(WIDTH,ORIGINAL_HEIGHT);
            showJustification = false;
            return;
        }
        try{
            if ( isTableDataModified() ){
                budgetLineItemDetailsForm.pnlLineItemCalAmtsTable.saveFormData();
                BeanEvent beanEvent = new BeanEvent();
                beanEvent.setBean(budgetDetailBean);
                beanEvent.setSource(this);
                fireBeanUpdated(beanEvent);
                dataSaved = true;
            }
            if( dataModified ){
                if ( validate() ){
                    dataSaved = updateBudgetLineItem();
                }
            }
            if( dataSaved ){
                budgetLineItemDetailsForm.dlgBudgetLineItem.dispose();
                budgetLineItemDetailsForm.remove(
                        budgetLineItemDetailsForm.pnlJustify);
                budgetLineItemDetailsForm.dlgBudgetLineItem.setSize(WIDTH,ORIGINAL_HEIGHT);
                showJustification = false;
            }
        }catch (CoeusUIException coeusUIException){
            coeusUIException.getMessage();
        }
    }
    
    /** Supporting method to save the Form Data.
     * @return returns true if updation is successful, false otherwise
     */
    private boolean updateBudgetLineItem(){
        // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item -start
         BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = null;
                 
        // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item -end         
        budgetDetailBean.setAcType(UPDATE_RECORD);
        if( hasPersons ){
            budgetDetailBean.setLineItemDescription(newBean.getLineItemDescription());
            budgetDetailBean.setBudgetJustification(newBean.getBudgetJustification());
            // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item -start
            budgetDetailBean.setOnOffCampusFlag(newBean.isOnOffCampusFlag());
            // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item -end
        }else{
            budgetDetailBean.setApplyInRateFlag(newBean.isApplyInRateFlag());
            budgetDetailBean.setBudgetCategoryCode(newBean.getBudgetCategoryCode());
            budgetDetailBean.setCategoryType(newBean.getCategoryType());
            budgetDetailBean.setBudgetJustification(newBean.getBudgetJustification());
            budgetDetailBean.setLineItemDescription(newBean.getLineItemDescription());
            budgetDetailBean.setLineItemCost(newBean.getLineItemCost());
            budgetDetailBean.setLineItemEndDate(newBean.getLineItemEndDate());
            budgetDetailBean.setLineItemStartDate(newBean.getLineItemStartDate());
            budgetDetailBean.setCostSharingAmount(newBean.getCostSharingAmount());
            budgetDetailBean.setOnOffCampusFlag(newBean.isOnOffCampusFlag());
            budgetDetailBean.setQuantity(newBean.getQuantity());
            //COEUSQA-1693 - Cost Sharing Submission - start
            budgetDetailBean.setSubmitCostSharingFlag(newBean.isSubmitCostSharingFlag());
            //COEUSQA-1693 - Cost Sharing Submission - end
            //Added for case#3121 - start
            BudgetPeriodController periodController = new BudgetPeriodController();
            BudgetInfoBean budgetInfoBean = new BudgetInfoBean();
            budgetInfoBean.setProposalNumber(budgetDetailBean.getProposalNumber());
            double rate = periodController.calculateUnitRates(budgetDetailBean, budgetInfoBean);
            if(periodController.getTuitionFeeAutoCalculation()) {
                budgetDetailBean.setLineItemCost(rate);
            }
            //Added for case#3121 - end
            
            // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item -start
            budgetPersonnelDetailsBean = new BudgetPersonnelDetailsBean();
            budgetPersonnelDetailsBean.setProposalNumber(budgetDetailBean.getProposalNumber());
            budgetPersonnelDetailsBean.setVersionNumber(budgetDetailBean.getVersionNumber());
            budgetPersonnelDetailsBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
            budgetPersonnelDetailsBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
            
            try{
                Vector vecBudgetPersonnelDetails = queryEngine.executeQuery(queryKey,
                        budgetPersonnelDetailsBean);
                if ( vecBudgetPersonnelDetails != null && vecBudgetPersonnelDetails.size() > 0){
                    budgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean)vecBudgetPersonnelDetails.get(0);
                    budgetPersonnelDetailsBean.setOnOffCampusFlag(newBean.isOnOffCampusFlag());
                }
            }catch (CoeusException coeusException) {
                coeusException.getMessage();
                coeusException.printStackTrace();
            }
            
            // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item -end
        }
        try{
            // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item -start
            
            //COEUSQA-3715 - Coeus4.5: Salary Details Removal Issue -Start
            //If PersonnelDetailsBean has AcType 'D', then we should not add to the queryengine
            if(budgetPersonnelDetailsBean !=null && (budgetPersonnelDetailsBean.getAcType() == null || !budgetPersonnelDetailsBean.getAcType().equalsIgnoreCase("D") ) ){
                queryEngine.update(queryKey, budgetPersonnelDetailsBean);
            }
             //COEUSQA-3715 - End
            
            // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item -end
            queryEngine.update(queryKey, budgetDetailBean);
            
            BeanEvent beanEvent = new BeanEvent();
            beanEvent.setBean(budgetDetailBean);
            beanEvent.setSource(this);
            fireBeanUpdated(beanEvent);
            
        }catch(CoeusException coeusException){
            coeusException.getMessage();
            coeusException.printStackTrace();
        }            
        return true;
    }
    
    /** Method to get the budget category description
     * for the given budget category code
     * @param budgetCategoryCode takes a budgetCategoryCode
     * @return returns the budget category description
     */
    private char getBudgetCategoryType(String budgetCategoryCode){
        int budgetCategorySize = vecCategory.size();
            for(int index = 0 ; index < budgetCategorySize ; index++){
                //ComboBoxBean bean = (ComboBoxBean)vecCategory.elementAt(index);
                BudgetCategoryBean bean = (BudgetCategoryBean)vecCategory.elementAt(index);
                if (bean.getCode().equals(budgetCategoryCode)){
                    categoryType = bean.getCategoryType();
                    break;
                }
            }
        return categoryType;
    }
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data to set to the form
     */
    public void setFormData(Object data) {
        if( data == null ) return;
        budgetDetailBean = ( BudgetDetailBean ) data;

        queryKey = budgetDetailBean.getProposalNumber() + 
                budgetDetailBean.getVersionNumber();
        Equals eqProposal = new Equals(PROPOSAL,
                budgetDetailBean.getProposalNumber());
        Equals eqVersion = new Equals(VERSION,
                new Integer(budgetDetailBean.getVersionNumber() ));
        Equals eqBudgetPeriod = new Equals(BUDGET_PERIOD, 
                new Integer( budgetDetailBean.getBudgetPeriod() ));
        And eqPropAndEqVer = new And(eqProposal, eqVersion);
        And eqPropAndEqVerAndEqPeriod = new And(eqPropAndEqVer, eqBudgetPeriod);
        
        /** Query and get the budgetPeriodBean to get the period start and end dates */
        try{
            CoeusVector cvBudgetPeriod = queryEngine.executeQuery(queryKey, 
                    BudgetPeriodBean.class, eqPropAndEqVerAndEqPeriod);
            budgetPeriodBean = (BudgetPeriodBean) cvBudgetPeriod.get(0);
        }catch (CoeusException coeusException){
            coeusException.getMessage();
        }
        Equals eqLineItemNumber = new Equals(LINE_ITEM_NUMBER,
                new Integer( budgetDetailBean.getLineItemNumber() ));
        And eqPeriodAndEqLineItemNum = new And(eqBudgetPeriod, eqLineItemNumber);

        NotEquals neqDelete = new NotEquals(AC_TYPE, TypeConstants.DELETE_RECORD);
        
        Equals eqNull = new Equals(AC_TYPE, null);
        Or eqNullOrNeqDelete = new Or(neqDelete, eqNull);
        
        And operator = new And( eqPeriodAndEqLineItemNum, eqNullOrNeqDelete );
        
        // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item -Start
        hasMultiplePersons = false;
        // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item -End
        try{
            vecPersonnelDetails = queryEngine.executeQuery(queryKey, 
                    BudgetPersonnelDetailsBean.class, operator);
            if ( vecPersonnelDetails != null && vecPersonnelDetails.size() > 0){
                hasPersons = true;
                // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item -Start
                if(vecPersonnelDetails.size() > 1){
                    hasMultiplePersons = true;
                }
                // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item -End
            }
        }catch (CoeusException coeusException) {
            coeusException.getMessage();
        }

        periodStartDate = budgetPeriodBean.getStartDate();
        periodEndDate = budgetPeriodBean.getEndDate();
        
        previousStartDate = dateUtils.formatDate(
                budgetPeriodBean.getStartDate().toString(), REQUIRED_DATEFORMAT);
        previousEndDate = dateUtils.formatDate(
                budgetPeriodBean.getEndDate().toString(), REQUIRED_DATEFORMAT);

        //setting the category look up
        try{
            vecCategory = queryEngine.getDetails(queryKey, BudgetCategoryBean.class);
        }catch(CoeusException coeusException){
            coeusException.getMessage();
        }
        
        if( ( vecCategory != null ) && ( vecCategory.size() > 0 )
        && ( budgetLineItemDetailsForm.cmbCategory.getItemCount() == 0 ) ) {
            int categorySize = vecCategory.size();
            budgetLineItemDetailsForm.cmbCategory.addItem(
                new ComboBoxBean(EMPTY_STRING,EMPTY_STRING));
            for(int index = 0 ; index < categorySize ; index++){
                budgetLineItemDetailsForm.cmbCategory.addItem((ComboBoxBean)vecCategory.elementAt(index));
            }
        }
       
        //setting the data for other fields
        budgetLineItemDetailsForm.txtCostElm.setText( budgetDetailBean.getCostElement() );
        budgetLineItemDetailsForm.lblCostElementDescription.setText(        
                budgetDetailBean.getCostElementDescription() );
        if ( budgetDetailBean.getBudgetCategoryCode() == -1 ||
        budgetDetailBean.getBudgetCategoryCode() == 0 ){
            budgetLineItemDetailsForm.cmbCategory.setSelectedIndex(0);
        }else {
            budgetLineItemDetailsForm.cmbCategory.setSelectedItem( String.valueOf(
                budgetDetailBean.getBudgetCategoryCode()) );
        }
        
        if ( budgetDetailBean.isOnOffCampusFlag() ){
            budgetLineItemDetailsForm.rdBtnOnCampus.setSelected(true);
        }else{
            budgetLineItemDetailsForm.rdBtnOffCampus.setSelected(true);
        }
        //COEUSQA-1693 - Cost Sharing Submission - start
        budgetLineItemDetailsForm.chkSubmitCostSharing.setSelected(budgetDetailBean.isSubmitCostSharingFlag());
        //COEUSQA-1693 - Cost Sharing Submission - end
        
        budgetLineItemDetailsForm.txtStartDate.setText( dateUtils.formatDate(
                budgetDetailBean.getLineItemStartDate().toString(), REQUIRED_DATEFORMAT) );
        budgetLineItemDetailsForm.txtEndDate.setText( dateUtils.formatDate(
                budgetDetailBean.getLineItemEndDate().toString(), REQUIRED_DATEFORMAT) );
        String temp = budgetDetailBean.getLineItemDescription();
        if( !(temp == null || temp.trim().equals(EMPTY_STRING)) ) {
            budgetDetailBean.setLineItemDescription( temp.trim() );
        }
        budgetLineItemDetailsForm.txtArDescription.setText( budgetDetailBean.getLineItemDescription() );
        budgetLineItemDetailsForm.txtArDescription.setCaretPosition(0);
        budgetLineItemDetailsForm.txtCost.setValue( budgetDetailBean.getLineItemCost() );
        if( budgetDetailBean.getQuantity() == 0 ){
            budgetLineItemDetailsForm.txtQuantity.setText(EMPTY_STRING);
        }else{
            budgetLineItemDetailsForm.txtQuantity.setText(String.valueOf(budgetDetailBean.getQuantity()));
        }
        budgetLineItemDetailsForm.chkInflation.setSelected( budgetDetailBean.isApplyInRateFlag() );
        budgetLineItemDetailsForm.txtCostSharing.setValue( budgetDetailBean.getCostSharingAmount() );
        budgetLineItemDetailsForm.txtUnderRecovery.setValue( budgetDetailBean.getUnderRecoveryAmount() ) ;
        temp = budgetDetailBean.getBudgetJustification();
        if( !(temp == null || temp.trim().equals(EMPTY_STRING)) ) {
            budgetDetailBean.setBudgetJustification(temp.trim());
        }
        budgetLineItemDetailsForm.txtArJustification.setText( budgetDetailBean.getBudgetJustification());
        budgetLineItemDetailsForm.txtArJustification.setCaretPosition(0);
        budgetLineItemDetailsForm.pnlLineItemCalAmtsTable.setFormData(budgetDetailBean);
    }
    
    /** validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public boolean validate() throws CoeusUIException {
        if((budgetLineItemDetailsForm.txtStartDate.getText()== null)
        || (budgetLineItemDetailsForm.txtStartDate.getText().trim().length() == 0) ){
            message = coeusMessageResources.parseMessageKey(INVALID_START_DATE);
            budgetLineItemDetailsForm.txtStartDate.requestFocus();
            CoeusOptionPane.showErrorDialog(message);
            return false;
        }else if( (budgetLineItemDetailsForm.txtEndDate.getText() == null)
        || (budgetLineItemDetailsForm.txtEndDate.getText().trim().length() == 0) ){
            message = coeusMessageResources.parseMessageKey(INVALID_END_DATE);
            budgetLineItemDetailsForm.txtEndDate.requestFocus();
            CoeusOptionPane.showErrorDialog(message);
            return false;
        }else if (!validateStartDate(
            dateUtils.restoreDate(budgetLineItemDetailsForm.txtStartDate.getText().trim(), DATE_SEPARATERS) ) ) {
            return false;
        }else if (!validateEndDate(
            dateUtils.restoreDate(budgetLineItemDetailsForm.txtEndDate.getText().trim(), DATE_SEPARATERS)) ) {
            return false;
        }
        return true;
    }
    
    /**
     * This method is used to perform the Window closing operation. Before closing
     * the window it checks the saveRequired flag and the function type.
     * If the saveRequired is true then it saves the details
     * else dispose this JDialog.
     */
    private void performWindowClosing(){
        int option = JOptionPane.NO_OPTION;
        if(getFunctionType() != DISPLAY_MODE){
            dataModified = isDataModified();
            setSaveRequired( dataModified || isTableDataModified());
            if(isSaveRequired()){
                option
                    = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(
                                                "saveConfirmCode.1002"),
                        CoeusOptionPane.OPTION_YES_NO_CANCEL,
                        CoeusOptionPane.DEFAULT_YES);

                switch( option ){
                    case ( JOptionPane.YES_OPTION ):
                        try{
                            saveFormData();
                        }catch(Exception e){
                            e.getMessage();
                        }
                        break;
                    case ( JOptionPane.NO_OPTION ):
                        budgetLineItemDetailsForm.dlgBudgetLineItem.dispose();
                        budgetLineItemDetailsForm.remove(
                                budgetLineItemDetailsForm.pnlJustify);
                        budgetLineItemDetailsForm.dlgBudgetLineItem.setSize(WIDTH,ORIGINAL_HEIGHT);
                        budgetLineItemDetailsForm.pnlLineItemCalAmtsTable.setSaveRequired(false);
                        showJustification = false;                        
                        break;
                    case ( JOptionPane.CANCEL_OPTION ) :
                        budgetLineItemDetailsForm.dlgBudgetLineItem.setVisible(true);
                        break;
                }
            }else {
                budgetLineItemDetailsForm.dlgBudgetLineItem.dispose();
                budgetLineItemDetailsForm.remove(
                        budgetLineItemDetailsForm.pnlJustify);
                budgetLineItemDetailsForm.dlgBudgetLineItem.setSize(WIDTH,ORIGINAL_HEIGHT);
                showJustification = false;
            }
        }else{
            budgetLineItemDetailsForm.dlgBudgetLineItem.dispose();
        }
    }

    /** This method used to check the start date validation on focus lost
     * @return returns true if validation is successful, false otherwise
     * @param strDate takes the start date
     */
    private boolean validateStartDate(String strDate) {
        
	boolean isValid=true;
        message = null;
        String convertedDate = dateUtils.formatDate(strDate, DATE_SEPARATERS , REQUIRED_DATEFORMAT);
        if (convertedDate==null){
            message = coeusMessageResources.parseMessageKey(INVALID_START_DATE);
        }else if ( budgetLineItemDetailsForm.txtEndDate.getText() !=null 
                && budgetLineItemDetailsForm.txtEndDate.getText().trim().length() > 0 ) {
            // compare the date
            Date startDate = null;
            Date endDate = null;

            try {
                startDate = dtFormat.parse(dateUtils.restoreDate(convertedDate, DATE_SEPARATERS));
                endDate = dtFormat.parse(dateUtils.restoreDate(budgetLineItemDetailsForm.txtEndDate.getText(), DATE_SEPARATERS));
            }catch(java.text.ParseException pe){
                message = coeusMessageResources.parseMessageKey(INVALID_START_DATE);
            }
            if(endDate!=null && endDate.compareTo(startDate)<0){
                message = coeusMessageResources.parseMessageKey(START_DATE_LATER_THAN_END_DATE);
            }else if(startDate.before(periodStartDate)){
                message = coeusMessageResources.parseMessageKey(START_DATE_BETWEEN_PERIOD);
            }
        }
        if (message != null){
            isValid=false;
            //budgetLineItemDetailsForm.txtStartDate.grabFocus();
            CoeusOptionPane.showErrorDialog(message);
            budgetLineItemDetailsForm.txtStartDate.setText(previousStartDate);
        }else{
            String focusDate = dateUtils.formatDate(strDate, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
            budgetLineItemDetailsForm.txtStartDate.setText(focusDate);
            previousStartDate = budgetLineItemDetailsForm.txtStartDate.getText();
       }        
       return isValid;
    }
    
    /** This method used to check the end date validation on focus lost
     * @return returns true if validation is successful, false otherwise
     * @param strDate takes the end date
     */
    private boolean validateEndDate(String strDate) {
        
        boolean isValid=true;
        message = null;
        String convertedDate = dateUtils.formatDate(strDate, DATE_SEPARATERS , REQUIRED_DATEFORMAT);
        if (convertedDate==null){
            //budgetLineItemDetailsForm.txtEndDate.grabFocus();
            message = coeusMessageResources.parseMessageKey(INVALID_END_DATE);            
        }else if ( budgetLineItemDetailsForm.txtStartDate.getText() !=null 
                && budgetLineItemDetailsForm.txtStartDate.getText().trim().length() > 0 ) {
            // compare the date
            Date startDate = null;
            Date endDate = null;

            try {
                startDate = dtFormat.parse(dateUtils.restoreDate(budgetLineItemDetailsForm.txtStartDate.getText(), DATE_SEPARATERS));
                endDate = dtFormat.parse(dateUtils.restoreDate(convertedDate, DATE_SEPARATERS));
            }catch(java.text.ParseException pe){
                budgetLineItemDetailsForm.txtEndDate.grabFocus();
                message = coeusMessageResources.parseMessageKey(INVALID_END_DATE);                
                //return false;
            }
            if(startDate !=null && startDate.compareTo(endDate)>0){
                budgetLineItemDetailsForm.txtEndDate.grabFocus();
                message = coeusMessageResources.parseMessageKey(END_DATE_EARLIER_THAN_START_DATE);                
            }else if(endDate.after(periodEndDate)){
                budgetLineItemDetailsForm.txtEndDate.grabFocus();
                message = coeusMessageResources.parseMessageKey(END_DATE_BETWEEN_PERIOD);
            }
        }

        if (message != null){
            isValid=false;
            budgetLineItemDetailsForm.txtEndDate.grabFocus();
            CoeusOptionPane.showErrorDialog(message);
            budgetLineItemDetailsForm.txtEndDate.setText(previousEndDate);
        }else{
            String focusDate = dateUtils.formatDate(strDate, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
            budgetLineItemDetailsForm.txtEndDate.setText(focusDate);
            previousEndDate = budgetLineItemDetailsForm.txtEndDate.getText();
        }
        return isValid;
    }
    
    /**
    * Custom focus adapter which is used for text fields which consists of
    * date values. This is mainly used to format and restore the date value
    * during focus gained / focus lost of the text field.
    */
    private class CustomFocusAdapter extends FocusAdapter{
        //holds the data display Text Field
        CoeusTextField dateField;
        String strDate = EMPTY_STRING;
        String oldData = EMPTY_STRING;
        boolean temporary = false;
        public void focusGained (FocusEvent fe){
            if (fe.getSource().equals( budgetLineItemDetailsForm.txtStartDate ) ||
                    fe.getSource().equals( budgetLineItemDetailsForm.txtEndDate )){
                dateField = (CoeusTextField)fe.getSource();
                if ( (dateField.getText() != null) &&  (!dateField.getText().trim().equals(EMPTY_STRING))) {
                    oldData = dateField.getText();
                    String focusDate = dateUtils.restoreDate(
                            dateField.getText(),DATE_SEPARATERS);
                    dateField.setText(focusDate);
                }
            }
        }

        public void focusLost (FocusEvent fe){
            String formatedStartDate = dateUtils.formatDate(budgetPeriodBean.getStartDate().toString(),SIMPLE_DATE_FORMAT);
            String formatedEndDate = dateUtils.formatDate(budgetPeriodBean.getEndDate().toString(),SIMPLE_DATE_FORMAT);
            temporary = fe.isTemporary();
            if( !temporary ){
                temporary  = true;
                if ( fe.getSource()== budgetLineItemDetailsForm.txtStartDate ){
                    if(budgetLineItemDetailsForm.txtStartDate==null || budgetLineItemDetailsForm.txtStartDate.getText().trim().length()==0 ||
                    budgetLineItemDetailsForm.txtStartDate.equals(EMPTY_STRING)){
                        budgetLineItemDetailsForm.txtStartDate.requestFocus();
                        message = coeusMessageResources.parseMessageKey(INVALID_START_DATE);
                        CoeusOptionPane.showErrorDialog(message);
                        budgetLineItemDetailsForm.txtStartDate.setText(formatedStartDate);
                        previousStartDate = budgetLineItemDetailsForm.txtStartDate.getText();
                    }else if ( budgetLineItemDetailsForm.txtStartDate.getText().trim().length() > 0 &&
                    !validateStartDate( budgetLineItemDetailsForm.txtStartDate.getText().trim()) ) {
                        budgetLineItemDetailsForm.txtStartDate.requestFocus();
                        temporary  = true;
                    }  else {
                        //Modification for Case #3121 - start
                        Date strDate = new Date();
                        Date endDate = new Date();
                        String value = dateUtils.restoreDate(budgetLineItemDetailsForm.txtStartDate.getText(),DATE_SEPARATERS);
                        String strEndDate = dateUtils.restoreDate(budgetLineItemDetailsForm.txtEndDate.getText(),DATE_SEPARATERS);
                        try {
                            if ( value.trim().length() >0 && value != null ){
                                strDate = dtFormat.parse(value);
                            }
                            if(strEndDate != null && strEndDate.trim().length() > 0) {
                                endDate = dtFormat.parse(strEndDate);
                            }
                        }catch(Exception ex) {
                            ex.printStackTrace();
                        }
//                        CoeusVector cvRates = new CoeusVector();
                        BudgetInfoBean budgetInfoBean = new BudgetInfoBean();
                        budgetInfoBean.setProposalNumber(budgetDetailBean.getProposalNumber());
                        java.sql.Date oldStartDate = budgetDetailBean.getLineItemStartDate();
                        java.sql.Date oldEndDate = budgetDetailBean.getLineItemEndDate();
                        budgetDetailBean.setLineItemStartDate(new java.sql.Date(strDate.getTime()));
                        budgetDetailBean.setLineItemEndDate(new java.sql.Date(endDate.getTime()));
                        BudgetPeriodController periodController = new BudgetPeriodController();
                        String strQuantity = budgetLineItemDetailsForm.txtQuantity.getText().trim();
                        if(strQuantity != null) {
                            strQuantity = strQuantity.replaceAll(",", "");
                        }
                        double quantity;
                        if(strQuantity.equals(EMPTY_STRING)){
                            quantity = 0.0;
                        }else{
                            quantity = Double.parseDouble(strQuantity);
                        }       
                        double oldQuantity = 0.0;
                        oldQuantity = budgetDetailBean.getQuantity();
                        budgetDetailBean.setQuantity(quantity);
                        double rate = periodController.calculateUnitRates(budgetDetailBean, budgetInfoBean);
                        if(rate != 0.0) {
                            budgetLineItemDetailsForm.txtCost.setValue(rate);
                        }
                        budgetDetailBean.setLineItemStartDate(oldStartDate);
                        budgetDetailBean.setLineItemEndDate(oldEndDate);
                        budgetDetailBean.setQuantity(oldQuantity);
                    }
                }else if (fe.getSource()== budgetLineItemDetailsForm.txtEndDate){
                    if(budgetLineItemDetailsForm.txtEndDate==null || budgetLineItemDetailsForm.txtEndDate.getText().trim().length()==0 ||
                    budgetLineItemDetailsForm.txtEndDate.equals(EMPTY_STRING)){
                        budgetLineItemDetailsForm.txtEndDate.requestFocus();
                        message = coeusMessageResources.parseMessageKey(INVALID_END_DATE);
                        CoeusOptionPane.showErrorDialog(message);
                        budgetLineItemDetailsForm.txtEndDate.setText(formatedEndDate);
                        previousEndDate = budgetLineItemDetailsForm.txtEndDate.getText();
                        temporary  = true;
                    }else if ( budgetLineItemDetailsForm.txtEndDate.getText().trim().length() > 0 &&
                    !validateEndDate( budgetLineItemDetailsForm.txtEndDate.getText().trim()) ) {
                        budgetLineItemDetailsForm.txtEndDate.requestFocus();
                        temporary  = true;
                    } else {
                        Date endDate = new Date();
                        Date startDate = new Date();
                        try {
                            String value = dateUtils.restoreDate(budgetLineItemDetailsForm.txtEndDate.getText(),DATE_SEPARATERS);
                            String strStartDate = dateUtils.restoreDate(budgetLineItemDetailsForm.txtStartDate.getText(),DATE_SEPARATERS);
                            if( value.trim().length() >0 && value != null ){
                                endDate = dtFormat.parse(value);
                            }
                            if(strStartDate != null && strStartDate.trim().length() > 0) {
                                startDate = dtFormat.parse(strStartDate);
                            }
                        }catch(Exception ex) {
                            ex.printStackTrace();
                        }
//                        CoeusVector cvRates = new CoeusVector();
                        BudgetInfoBean budgetInfoBean = new BudgetInfoBean();
                        budgetInfoBean.setProposalNumber(budgetDetailBean.getProposalNumber());
                        java.sql.Date oldEndDate = budgetDetailBean.getLineItemEndDate();
                        java.sql.Date oldStartDate = budgetDetailBean.getLineItemStartDate();
                        budgetDetailBean.setLineItemEndDate(new java.sql.Date(endDate.getTime()));
                        budgetDetailBean.setLineItemStartDate(new java.sql.Date(startDate.getTime()));
                        BudgetPeriodController periodController = new BudgetPeriodController();
                        String strQuantity = budgetLineItemDetailsForm.txtQuantity.getText().trim();
                        if(strQuantity != null) {
                            strQuantity = strQuantity.replaceAll(",", "");
                        }
                        double quantity;
                        if(strQuantity.equals(EMPTY_STRING)){
                            quantity = 0.0;
                        }else{
                            quantity = Double.parseDouble(strQuantity);
                        }       
                        double oldQuantity = 0.0;
                        oldQuantity = budgetDetailBean.getQuantity();
                        budgetDetailBean.setQuantity(quantity);
                        double rate = periodController.calculateUnitRates(budgetDetailBean, budgetInfoBean);
                        if(rate != 0.0) {
                            budgetLineItemDetailsForm.txtCost.setValue(rate);
                        }
                        budgetDetailBean.setLineItemStartDate(oldStartDate);
                        budgetDetailBean.setLineItemEndDate(oldEndDate);
                        budgetDetailBean.setQuantity(oldQuantity);
                    }
                } else if(fe.getSource() == budgetLineItemDetailsForm.txtQuantity) {
//                        CoeusVector cvRates = new CoeusVector();
                        BudgetInfoBean budgetInfoBean = new BudgetInfoBean();
                        budgetInfoBean.setProposalNumber(budgetDetailBean.getProposalNumber());
                        //Added for case#3121 - start
                        BudgetPeriodController periodController = new BudgetPeriodController();
                        String value = budgetLineItemDetailsForm.txtQuantity.getText().trim();
                        if(value != null) {
                            value = value.replaceAll(",", "");
                        }
                        double quantity;
                        if(value.equals(EMPTY_STRING)){
                            quantity = 0.0;
                        }else{
                            quantity = Double.parseDouble(value);
                        }       
                        double oldQuantity = 0.0;
                        oldQuantity = budgetDetailBean.getQuantity();
                        budgetDetailBean.setQuantity(quantity);
                        //Added for case#3121 - end
                        Date endDate = new Date();
                        Date startDate = new Date();
                        try {
                            String strStartDate = dateUtils.restoreDate(budgetLineItemDetailsForm.txtStartDate.getText(),DATE_SEPARATERS);
                            String strEndDate = dateUtils.restoreDate(budgetLineItemDetailsForm.txtEndDate.getText(),DATE_SEPARATERS);
                            if(strStartDate != null && strStartDate.trim().length() > 0) {
                                startDate = dtFormat.parse(strStartDate);
                            }
                            if(strEndDate != null && strEndDate.trim().length() > 0) {
                                endDate = dtFormat.parse(strEndDate);
                            }
                        }catch(Exception ex) {
                            ex.printStackTrace();
                        }
                        java.sql.Date oldStartDate = budgetDetailBean.getLineItemStartDate();
                        java.sql.Date oldEndDate = budgetDetailBean.getLineItemEndDate();
                        budgetDetailBean.setLineItemStartDate(new java.sql.Date(startDate.getTime()));
                        budgetDetailBean.setLineItemEndDate(new java.sql.Date(endDate.getTime()));
                        double rate = periodController.calculateUnitRates(budgetDetailBean, budgetInfoBean);
                        if(rate != 0.0) {
                            budgetLineItemDetailsForm.txtCost.setValue(rate);
                        } else if(quantity == 0) {
                            budgetLineItemDetailsForm.txtCost.setValue(rate * quantity);
                        }
                        budgetDetailBean.setQuantity(oldQuantity);
                        budgetDetailBean.setLineItemStartDate(oldStartDate);
                        budgetDetailBean.setLineItemEndDate(oldEndDate);
                        //Modification for Case #3121 - end
                }
            }
            temporary  = true;
        }
    }
    
    //Added for Case #3121 - start
    /**
     * Is used to format the fields based on the ENABLE_AUTO_STIPEND_TUITION_CALC parameter
     * @param boolean
     * @return void
     */
    public void enableCostElement(boolean tuition_fee_auto_calculation) {
        if(budgetLineItemDetailsForm.txtQuantity.isEditable()) {
            budgetLineItemDetailsForm.txtCost.setEditable(tuition_fee_auto_calculation);
        } else {
            budgetLineItemDetailsForm.txtCost.setEditable(false);
        }
    }
    //Added for Case #3121 - end
}
