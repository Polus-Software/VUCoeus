/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.budget.controller;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.text.ParseException;
import javax.swing.event.*;
import javax.swing.table.*;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.budget.controller.Controller;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.budget.gui.PersonnelBudgetLineItemDetailsForm;
import edu.mit.coeus.gui.event.*;

/** This class controls the <CODE>PersonnelBudgetLineItemDetailsForm</CODE>
 * PersonnelBudgetLineItemController.java
 * @author  Vyjayanthi
 * Created on October 23, 2003, 1:42 PM
 */
public class PersonnelBudgetLineItemController extends Controller implements
TypeConstants, ActionListener{

    private QueryEngine queryEngine;
    private String queryKey;

    private PersonnelBudgetLineItemDetailsForm personnelLineItemsForm;
    private DateUtils dateUtils;
    private java.text.SimpleDateFormat dtFormat
            = new java.text.SimpleDateFormat("MM/dd/yyyy");
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String EMPTY_STRING = "";
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    private String previousStartDate, previousEndDate;
    private java.sql.Date periodStartDate, periodEndDate;
    private BudgetPersonnelDetailsBean personnelDetailsBean;
    private ScreenFocusTraversalPolicy  traversePolicy;
    private BudgetPersonnelDetailsBean newBean;
    private BudgetDetailBean budgetDetailBean;
    private String periodType;
    private String message = EMPTY_STRING;
    private static final int WIDTH = 650;
    private static final int HEIGHT = 325;
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
    /*
    private static final int PERIOD_ACADEMIC = 0;
    private static final int PERIOD_CALENDAR = 1;
    private static final int PERIOD_CYCLE = 2;
    private static final int PERIOD_SUMMER = 3;
    
    private static final String ACADEMIC = "AP";
    private static final String CALENDAR = "CY";
    private static final String CYCLE = "CC";
    private static final String SUMMER = "SP";
    */
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
    private static final String PROPOSAL = "proposalNumber";
    private static final String VERSION = "versionNumber";
    private static final String BUDGET_PERIOD = "budgetPeriod";
    private static final String LINE_ITEM_NUMBER = "lineItemNumber";
    
    private static final String COST_SHARING_PERCENT_VALIDATION = "budgetLineItem_exceptionCode.1357";
    private static final String PERCENT_EFFORT_VALIDATION = "budgetLineItem_exceptionCode.1358";
    private static final String PERCENT_CHARGED_VALIDATION = "budgetLineItem_exceptionCode.1359";
    
    private static final String INVALID_START_DATE = "budget_common_exceptionCode.1001";
    private static final String INVALID_END_DATE = "budget_common_exceptionCode.1002";
    private static final String START_DATE_LATER_THAN_END_DATE = "budgetLineItem_exceptionCode.1351";
    private static final String END_DATE_EARLIER_THAN_START_DATE = "budgetLineItem_exceptionCode.1353";
    private static final String START_DATE_BETWEEN_PERIOD = "budgetLineItem_exceptionCode.1355";
    private static final String END_DATE_BETWEEN_PERIOD = "budgetLineItem_exceptionCode.1356";
    
    /** Holds CoeusMessageResources instance used for reading message Properties. */
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    
    /** Creates a new instance of PersonnelBudgetLineItemController */    
    public PersonnelBudgetLineItemController(){
        queryEngine = QueryEngine.getInstance();
        dateUtils = new DateUtils();
        this.personnelDetailsBean = personnelDetailsBean;
        personnelLineItemsForm = new PersonnelBudgetLineItemDetailsForm(
                CoeusGuiConstants.getMDIForm(), true);
        personnelLineItemsForm.dlgPersonnelBudgetLineItem.setSize(WIDTH,HEIGHT);
        registerComponents();
    }
    
    /** Creates a new instance of PersonnelBudgetLineItemController and sets the form data
     *  @param personnelDetailsBean takes a <CODE>personnelDetailsBean</CODE> */
    public PersonnelBudgetLineItemController(BudgetPersonnelDetailsBean personnelDetailsBean) {
        this();
        setFormData(personnelDetailsBean);
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
            setSaveRequired( isDataModified() );
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
                        personnelLineItemsForm.dlgPersonnelBudgetLineItem.dispose();
                        break;
                    case ( JOptionPane.CANCEL_OPTION ) :
                        personnelLineItemsForm.dlgPersonnelBudgetLineItem.setVisible(true);
                        break;
                }
            }else {
                personnelLineItemsForm.dlgPersonnelBudgetLineItem.dispose();
            }
        }else{
            personnelLineItemsForm.dlgPersonnelBudgetLineItem.dispose();
        }
    }

    /** Method to check if the data in the form has changed
     * @return true if data is modified false otherwise
     */
    public boolean isDataModified(){
        newBean = new BudgetPersonnelDetailsBean();
        boolean isEqual = false;
        Date modifiedDate;
        String value;
        /** Construct a new BudgetPersonnelDetailsBean and set the values */
        newBean.setProposalNumber(personnelDetailsBean.getProposalNumber());
        newBean.setVersionNumber(personnelDetailsBean.getVersionNumber());
        newBean.setBudgetPeriod(personnelDetailsBean.getBudgetPeriod());
        newBean.setLineItemNumber(personnelDetailsBean.getLineItemNumber());
        newBean.setPersonNumber(personnelDetailsBean.getPersonNumber());
        newBean.setPersonId(personnelDetailsBean.getPersonId());
        newBean.setFullName(personnelDetailsBean.getFullName());
        newBean.setJobCode(personnelDetailsBean.getJobCode());
        newBean.setSequenceNumber(personnelDetailsBean.getSequenceNumber());
        newBean.setSalaryRequested(personnelDetailsBean.getSalaryRequested());
        newBean.setBudgetJustification(personnelDetailsBean.getBudgetJustification());
        newBean.setDirectCost(personnelDetailsBean.getDirectCost());
        newBean.setIndirectCost(personnelDetailsBean.getIndirectCost());
        newBean.setCostSharingAmount(personnelDetailsBean.getCostSharingAmount());
        newBean.setUnderRecoveryAmount(personnelDetailsBean.getUnderRecoveryAmount());
        newBean.setOnOffCampusFlag(personnelDetailsBean.isOnOffCampusFlag());
        newBean.setApplyInRateFlag(personnelDetailsBean.isApplyInRateFlag());
        newBean.setUpdateUser(personnelDetailsBean.getUpdateUser());
        newBean.setUpdateTimestamp(personnelDetailsBean.getUpdateTimestamp());
        newBean.setAcType(personnelDetailsBean.getAcType());
        
        try{
            newBean.setLineItemDescription(personnelLineItemsForm.txtArDescription.getText().trim());
            newBean.setPercentCharged(Double.parseDouble(personnelLineItemsForm.txtPercentCharged.getText().toString()));
            newBean.setPercentEffort(Double.parseDouble(personnelLineItemsForm.txtPercentEffort.getText().toString()));
            newBean.setCostSharingPercent(Double.parseDouble(personnelLineItemsForm.txtCostSharingPercent.getText().toString()));
            //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
            /*int selectedPeriod = personnelLineItemsForm.cmbPeriod.getSelectedIndex();
            if( selectedPeriod == PERIOD_ACADEMIC ){
                newBean.setPeriodType(ACADEMIC);
            }else if( selectedPeriod == PERIOD_CALENDAR ){
                newBean.setPeriodType(CALENDAR);
            }else if( selectedPeriod == PERIOD_CYCLE ){
                newBean.setPeriodType(CYCLE);
            }else if( selectedPeriod == PERIOD_SUMMER ){
                newBean.setPeriodType(SUMMER);
            }*/
            String selectedPeriod = (String)personnelLineItemsForm.cmbPeriod.getSelectedItem();
            //newBean.setPeriodType(selectedPeriod);
            String [][] periodArray = fetchPeriodTypes();
            for(int i=0;i<periodArray.length;i++ ) {
                if(selectedPeriod.equalsIgnoreCase(periodArray[i][1])) {
                    newBean.setPeriodType(periodArray[i][0]);
                }
            }
            //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
            value = dateUtils.restoreDate(personnelLineItemsForm.txtStartDate.getText(), DATE_SEPARATERS);
            if( value.trim().length() >0 && value != null ){
                modifiedDate = dtFormat.parse(value);
                newBean.setStartDate( new java.sql.Date(modifiedDate.getTime()));
            }
            value = dateUtils.restoreDate(personnelLineItemsForm.txtEndDate.getText(), DATE_SEPARATERS);
            if( value.trim().length() >0 && value != null ){
                modifiedDate = dtFormat.parse(value);
                newBean.setEndDate( new java.sql.Date(modifiedDate.getTime()));
            }
            StrictEquals strictEquals = new StrictEquals();
            isEqual = strictEquals.compare(personnelDetailsBean, newBean);
        }catch (NumberFormatException numFormatException){
            numFormatException.getMessage();
        }catch (ParseException parseException){
            parseException.getMessage();
        }catch(Exception exception){
            exception.getMessage();
        }
        return !isEqual;
    }

    /** Displays the Form which is being controlled. */
    public void display() {
        if( getFunctionType() == MODIFY_MODE || getFunctionType() == ADD_MODE ){
            personnelLineItemsForm.txtStartDate.requestFocusInWindow();
        }else{
            personnelLineItemsForm.btnCancel.requestFocusInWindow();
        }
        personnelLineItemsForm.dlgPersonnelBudgetLineItem.show();
    }
    
    /** Perform field formatting.
     * enabling, disabling components depending on the
     * function type. */
    public void formatFields() {
        if(getFunctionType() == DISPLAY_MODE ){
            personnelLineItemsForm.txtStartDate.setEditable(false);
            personnelLineItemsForm.txtEndDate.setEditable(false);
            personnelLineItemsForm.cmbPeriod.setEnabled(false);
            personnelLineItemsForm.txtArDescription.setEditable(false);
            personnelLineItemsForm.txtArDescription.setBackground(
                (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            personnelLineItemsForm.txtPercentCharged.setEditable(false);
            personnelLineItemsForm.txtPercentEffort.setEditable(false);
            personnelLineItemsForm.txtCostSharingPercent.setEditable(false);
            personnelLineItemsForm.btnOk.setEnabled(false);
        }else{
            personnelLineItemsForm.btnOk.setEnabled(true);
        }
        personnelLineItemsForm.pnlLineItemCalAmtsTable.tblCalculatedAmounts.setEnabled(false);
    }
    
    /** Returns the Component which is being controlled by this Controller.
     * @return Component which is being controlled by this Controller. */
    public java.awt.Component getControlledUI() {
        return personnelLineItemsForm;
    }
    
    /** Returns the form data
     * @return the form data */
    public Object getFormData() {
        return personnelLineItemsForm;
    }
    
    /** registers GUI Components with event Listeners. */
    public void registerComponents() {
        //Code for focus traversal - start
        java.awt.Component[] components = { personnelLineItemsForm.txtStartDate,
                                            personnelLineItemsForm.txtEndDate,
                                            personnelLineItemsForm.cmbPeriod,
                                            personnelLineItemsForm.txtArDescription,
                                            personnelLineItemsForm.txtPercentCharged,
                                            personnelLineItemsForm.txtPercentEffort,
                                            personnelLineItemsForm.txtCostSharingPercent,
                                            personnelLineItemsForm.btnOk,
                                            personnelLineItemsForm.btnCancel,
                                            };
        traversePolicy = new ScreenFocusTraversalPolicy( components );
        personnelLineItemsForm.setFocusTraversalPolicy(traversePolicy);
        personnelLineItemsForm.setFocusCycleRoot(true);
        //Code for focus traversal - end      

        personnelLineItemsForm.txtStartDate.addFocusListener(new CustomFocusAdapter());        
        personnelLineItemsForm.txtEndDate.addFocusListener(new CustomFocusAdapter());
        personnelLineItemsForm.txtPercentCharged.addFocusListener(new CustomFocusAdapter());
        personnelLineItemsForm.txtPercentEffort.addFocusListener(new CustomFocusAdapter());
        personnelLineItemsForm.txtCostSharingPercent.addFocusListener(new CustomFocusAdapter());
        personnelLineItemsForm.btnOk.addActionListener(this);
        personnelLineItemsForm.btnCancel.addActionListener(this);
        
        personnelLineItemsForm.dlgPersonnelBudgetLineItem.addEscapeKeyListener(
            new AbstractAction("escPressed"){
                public void actionPerformed(ActionEvent ae){
                    performWindowClosing();
                }
        });
        
        personnelLineItemsForm.dlgPersonnelBudgetLineItem.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performWindowClosing();
                return;
            }
        });      
    }
    
    /** Saves the Form Data. */
    public void saveFormData() {
        if ( !isSaveRequired() ){
            personnelLineItemsForm.dlgPersonnelBudgetLineItem.dispose();
            return;
        }
        try{
            if (validate()){
                boolean dataSaved = updatePersonnelLineItem();
                if( dataSaved ){
                    personnelLineItemsForm.dlgPersonnelBudgetLineItem.dispose();
                }
            }
        }catch (CoeusUIException coeusUIException){
            coeusUIException.getMessage();
        }      
    }
    
    /** Supporting method to save the Form Data.
     * @return returns true if updation is successful, false otherwise */
    private boolean updatePersonnelLineItem(){
        personnelDetailsBean.setAcType(UPDATE_RECORD);
        personnelDetailsBean.setLineItemDescription(newBean.getLineItemDescription());
        personnelDetailsBean.setCostSharingAmount(newBean.getCostSharingAmount());
        personnelDetailsBean.setPercentCharged(newBean.getPercentCharged());
        personnelDetailsBean.setPercentEffort(newBean.getPercentEffort());
        personnelDetailsBean.setCostSharingPercent(newBean.getCostSharingPercent());
        personnelDetailsBean.setStartDate(newBean.getStartDate());
        personnelDetailsBean.setEndDate(newBean.getEndDate());
        personnelDetailsBean.setPeriodType(newBean.getPeriodType());
        return true;
    }

    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data to set to the form */
    public void setFormData(Object data) {
        if( data == null ) return;
        personnelDetailsBean = ( BudgetPersonnelDetailsBean ) data;
        queryKey = personnelDetailsBean.getProposalNumber() + 
                personnelDetailsBean.getVersionNumber();
        Equals eqProposal = new Equals(PROPOSAL,
                personnelDetailsBean.getProposalNumber());
        Equals eqVersion = new Equals(VERSION,
                new Integer(personnelDetailsBean.getVersionNumber() ));
        Equals eqBudgetPeriod = new Equals(BUDGET_PERIOD,
                new Integer(personnelDetailsBean.getBudgetPeriod() ));
        Equals eqLineItem = new Equals(LINE_ITEM_NUMBER,
                new Integer(personnelDetailsBean.getLineItemNumber() ));
        And eqPropAndEqVersion = new And(eqProposal, eqVersion);
        And eqPrdAndEqLineItem = new And(eqBudgetPeriod, eqLineItem);
        And operator = new And(eqPropAndEqVersion, eqPrdAndEqLineItem);

        /** Query and get the budgetDetailBean to get the line item start and end dates */
        try{
            CoeusVector cvBudgetDetail = queryEngine.executeQuery(queryKey,
                    BudgetDetailBean.class, operator);
            budgetDetailBean = (BudgetDetailBean) cvBudgetDetail.get(0);
        }catch (CoeusException coeusException){
            coeusException.getMessage();
        }
        periodStartDate = budgetDetailBean.getLineItemStartDate();
        periodEndDate = budgetDetailBean.getLineItemEndDate();
        
        previousStartDate = dateUtils.formatDate(
                budgetDetailBean.getLineItemStartDate().toString(), REQUIRED_DATEFORMAT);
        previousEndDate = dateUtils.formatDate(
                budgetDetailBean.getLineItemEndDate().toString(), REQUIRED_DATEFORMAT);

        periodType = personnelDetailsBean.getPeriodType();
        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
        /*
        if( periodType.equals(ACADEMIC) ){
            personnelLineItemsForm.cmbPeriod.setSelectedIndex(PERIOD_ACADEMIC);
        }else if( periodType.equals(CALENDAR) ){
            personnelLineItemsForm.cmbPeriod.setSelectedIndex(PERIOD_CALENDAR);
        }else if( periodType.equals(CYCLE) ){
            personnelLineItemsForm.cmbPeriod.setSelectedIndex(PERIOD_CYCLE);
        }else if( periodType.equals(SUMMER) ){
            personnelLineItemsForm.cmbPeriod.setSelectedIndex(PERIOD_SUMMER);
        }*/
        HashMap hmPeriodData = new HashMap();        
        int size = personnelLineItemsForm.cmbPeriod.getItemCount();
        for(int k=0;k<size;k++){
            hmPeriodData.put(k,(String)personnelLineItemsForm.cmbPeriod.getItemAt(k));
        }
        for(int i=0;i<hmPeriodData.size();i++ ) {
            if(periodType.equals(hmPeriodData.get(i))){
                personnelLineItemsForm.cmbPeriod.setSelectedIndex(i);
            }
        }
        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
        personnelLineItemsForm.txtName.setText(personnelDetailsBean.getFullName());
        personnelLineItemsForm.txtJobCode.setText(personnelDetailsBean.getJobCode());
        personnelLineItemsForm.txtStartDate.setText( dateUtils.formatDate(
                personnelDetailsBean.getStartDate().toString(), REQUIRED_DATEFORMAT) );
        personnelLineItemsForm.txtEndDate.setText( dateUtils.formatDate(
                personnelDetailsBean.getEndDate().toString(), REQUIRED_DATEFORMAT) );
        personnelLineItemsForm.txtArDescription.setText(
                personnelDetailsBean.getLineItemDescription());
        personnelLineItemsForm.txtArDescription.setCaretPosition(0);
        personnelLineItemsForm.txtPercentCharged.setText(
                String.valueOf(personnelDetailsBean.getPercentCharged()));
        personnelLineItemsForm.txtPercentEffort.setText(
                String.valueOf(personnelDetailsBean.getPercentEffort()));
        personnelLineItemsForm.txtCostSharingPercent.setText(
                String.valueOf(personnelDetailsBean.getCostSharingPercent()));
        personnelLineItemsForm.txtCostSharing.setText(
                String.valueOf(personnelDetailsBean.getCostSharingAmount()));
        personnelLineItemsForm.txtReqSalary.setText(
                String.valueOf(personnelDetailsBean.getSalaryRequested()));
        personnelLineItemsForm.txtUnderRecovery.setText(
                String.valueOf(personnelDetailsBean.getUnderRecoveryAmount()));
    }
    
    /** This method is used to set the calculated amounts table data
     * @param vecPersonnelCalAmts to set to the table data */
    public void setCalAmountsData(CoeusVector vecPersonnelCalAmts){
        personnelLineItemsForm.pnlLineItemCalAmtsTable.setFormData(vecPersonnelCalAmts);
    }
    
    /** Validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false. */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        if((personnelLineItemsForm.txtStartDate.getText()== null)
        || (personnelLineItemsForm.txtStartDate.getText().trim().length() == 0) ){
            message = coeusMessageResources.parseMessageKey(INVALID_START_DATE);
            CoeusOptionPane.showErrorDialog(message);
            return false;
        }else if( (personnelLineItemsForm.txtEndDate.getText() == null)
        || (personnelLineItemsForm.txtEndDate.getText().trim().length() == 0) ){
            message = coeusMessageResources.parseMessageKey(INVALID_END_DATE);
            CoeusOptionPane.showErrorDialog(message);
            return false;
        }else if (!validateStartDate(
            dateUtils.restoreDate(personnelLineItemsForm.txtStartDate.getText().trim(), DATE_SEPARATERS) ) ) {
            return false;
        }else if (!validateEndDate(
            dateUtils.restoreDate(personnelLineItemsForm.txtEndDate.getText().trim(), DATE_SEPARATERS)) ) {
            return false;
        }else if( !validateCostSharingPercent()){
            return false;
        }
        return true;
    }
    
    /** Validate the Cost Sharing Percent
     * @return true if
     * validation is through else returns false. */
    private boolean validateCostSharingPercent(){
        double value = 0, percentCharged, percentEffort, costSharingPercent;
        try{
            percentCharged = Double.parseDouble( personnelLineItemsForm.txtPercentCharged.getText() );
            percentEffort = Double.parseDouble( personnelLineItemsForm.txtPercentEffort.getText() );

            if( percentCharged > 100 ){
                message = coeusMessageResources.parseMessageKey(PERCENT_CHARGED_VALIDATION);
                CoeusOptionPane.showErrorDialog(message);
                personnelLineItemsForm.txtPercentCharged.requestFocus();
                personnelLineItemsForm.txtPercentCharged.setText(EMPTY_STRING+0.00);
                percentCharged = Double.parseDouble( personnelLineItemsForm.txtPercentCharged.getText() );
                newBean.setPercentCharged( percentCharged );
                return false;
            }else if( percentEffort > 100 ) {
                message = coeusMessageResources.parseMessageKey(PERCENT_EFFORT_VALIDATION);
                CoeusOptionPane.showErrorDialog(message);
                personnelLineItemsForm.txtPercentEffort.requestFocus();
                personnelLineItemsForm.txtPercentEffort.setText(EMPTY_STRING+0.00);
                percentEffort = Double.parseDouble( personnelLineItemsForm.txtPercentEffort.getText() );
                newBean.setPercentEffort(percentEffort);
                return false;
            }else{
                // Commented by chandra for the bug fix #774 - start 15th June 2004
                
//                if( percentCharged < percentEffort ){
//                    value = percentEffort - percentCharged;
//                    personnelLineItemsForm.txtCostSharingPercent.setText(String.valueOf(value));
//                }else{
//                    value = 0.00;
//                }
                // Commented by chandra for the bug fix #774 - End 15th June 2004
                costSharingPercent = Double.parseDouble( personnelLineItemsForm.txtCostSharingPercent.getText() );
                if( costSharingPercent > 100 ){
                    message = coeusMessageResources.parseMessageKey(COST_SHARING_PERCENT_VALIDATION);
                    CoeusOptionPane.showErrorDialog(message);
                    personnelLineItemsForm.txtCostSharingPercent.requestFocus();
                    personnelLineItemsForm.txtCostSharingPercent.setText(EMPTY_STRING + value);
                    newBean.setCostSharingPercent(value);
                    return false;
                }else {
                    // Commented by chandra for the bug fix #774 - start 15th June 2004
                    newBean.setCostSharingPercent(Double.parseDouble(personnelLineItemsForm.txtCostSharingPercent.getText().toString()));
                    personnelLineItemsForm.txtCostSharingPercent.setText(personnelLineItemsForm.txtCostSharingPercent.getText());
                    //newBean.setCostSharingPercent(value);
                    //personnelLineItemsForm.txtCostSharingPercent.setText(EMPTY_STRING + value);
                    // Commented by chandra for the bug fix #774 - End 15th June 2004
                }
            }
            return true;
        }catch (NumberFormatException numberFormatException){
            numberFormatException.getMessage();
        }
        return false;
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if ( source == personnelLineItemsForm.btnOk ){
            setSaveRequired( isDataModified() );
            saveFormData();
            BeanEvent beanEvent = new BeanEvent();
            beanEvent.setBean(personnelDetailsBean);
            beanEvent.setSource(this);
            fireBeanUpdated(beanEvent);
        }
        else if ( source == personnelLineItemsForm.btnCancel ){
            performWindowClosing();
        }
    }
    
    /** This method used to check the start date validation on focus lost
     * @return returns true if validation is successful, false otherwise
     * @param strDate takes the start date */
    private boolean validateStartDate(String strDate) {        
	boolean isValid=true;
        message = null;
        String convertedDate = dateUtils.formatDate(strDate, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
        if (convertedDate==null){
            message = coeusMessageResources.parseMessageKey(INVALID_START_DATE);
        }else if ( personnelLineItemsForm.txtEndDate.getText() !=null 
                && personnelLineItemsForm.txtEndDate.getText().trim().length() > 0 ) {
            // compare the date
            Date startDate = null;
            Date endDate = null;
            try {
                startDate = dtFormat.parse(dateUtils.restoreDate(convertedDate, DATE_SEPARATERS));
                endDate = dtFormat.parse(dateUtils.restoreDate(personnelLineItemsForm.txtEndDate.getText(), DATE_SEPARATERS));
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
            //personnelLineItemsForm.txtStartDate.grabFocus();
            CoeusOptionPane.showErrorDialog(message);
            personnelLineItemsForm.txtStartDate.setText(previousStartDate);
        }else{
            String focusDate = dateUtils.formatDate(strDate, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
            personnelLineItemsForm.txtStartDate.setText(focusDate);
            previousStartDate = personnelLineItemsForm.txtStartDate.getText();
       }
       return isValid;
    }
    
    /** This method used to check the end date validation on focus lost
     * @return returns true if validation is successful, false otherwise
     * @param strDate takes the end date */
    private boolean validateEndDate(String strDate) {
        boolean isValid=true;
        message = null;
        String convertedDate = dateUtils.formatDate(strDate, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
        if (convertedDate==null){
            message = coeusMessageResources.parseMessageKey(INVALID_END_DATE);
        }else if ( personnelLineItemsForm.txtStartDate.getText() !=null 
                && personnelLineItemsForm.txtStartDate.getText().trim().length() > 0 ) {
            // compare the date
            Date startDate = null;
            Date endDate = null;
            try {
                startDate = dtFormat.parse(dateUtils.restoreDate(personnelLineItemsForm.txtStartDate.getText(), DATE_SEPARATERS));
                endDate = dtFormat.parse(dateUtils.restoreDate(convertedDate, DATE_SEPARATERS));
            }catch(java.text.ParseException pe){
                personnelLineItemsForm.txtEndDate.grabFocus();
                message = coeusMessageResources.parseMessageKey(INVALID_END_DATE);
                //return false;
            }
            if(startDate !=null && startDate.compareTo(endDate)>0){
                message = coeusMessageResources.parseMessageKey(END_DATE_EARLIER_THAN_START_DATE);
            }else if(endDate.after(periodEndDate)){
                message = coeusMessageResources.parseMessageKey(END_DATE_BETWEEN_PERIOD);
            }
        }

        if (message != null){
            isValid=false;
            personnelLineItemsForm.txtEndDate.grabFocus();
            CoeusOptionPane.showErrorDialog(message);
            personnelLineItemsForm.txtEndDate.setText(previousEndDate);
        }else{
            String focusDate = dateUtils.formatDate(strDate, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
            personnelLineItemsForm.txtEndDate.setText(focusDate);
            previousEndDate = personnelLineItemsForm.txtEndDate.getText();
        }
        return isValid;
    }

    /** Custom focus adapter which is used for text fields which consists of
    * date values. This is mainly used to format and restore the date value
    * during focus gained / focus lost of the text field. */
    private class CustomFocusAdapter extends FocusAdapter{
        //hols the data display Text Field
        CoeusTextField dateField;        
        String strDate = EMPTY_STRING;
        String oldData = EMPTY_STRING;
        boolean temporary = false;
        double value, percentCharged, percentEffort, costSharingPercent;
        public void focusGained (FocusEvent fe){
            if (fe.getSource().equals( personnelLineItemsForm.txtStartDate ) ||
                    fe.getSource().equals( personnelLineItemsForm.txtEndDate )){
    
                dateField = (CoeusTextField)fe.getSource();
                if ( (dateField.getText() != null) &&  (!dateField.getText().trim().equals(EMPTY_STRING))) {
                    oldData = dateField.getText();
                    String focusDate = dateUtils.restoreDate(
                            dateField.getText(), DATE_SEPARATERS);
                    dateField.setText(focusDate);
                }
            }
        }

//        public void focusLost (FocusEvent fe){
//            String formatedStartDate = dateUtils.formatDate(budgetDetailBean.getLineItemStartDate().toString(),SIMPLE_DATE_FORMAT);
//            String formatedEndDate = dateUtils.formatDate(budgetDetailBean.getLineItemEndDate().toString(),SIMPLE_DATE_FORMAT);
//            temporary = fe.isTemporary();
//            if( !temporary ){
//                temporary  = true;
//                if ( fe.getSource()== personnelLineItemsForm.txtStartDate ){
//                    if(personnelLineItemsForm.txtStartDate==null || personnelLineItemsForm.txtStartDate.getText().trim().length()==0 ||
//                    personnelLineItemsForm.txtStartDate.equals(EMPTY_STRING)){
//                        personnelLineItemsForm.txtStartDate.requestFocus();
//                        message = coeusMessageResources.parseMessageKey(INVALID_START_DATE);
//                        CoeusOptionPane.showErrorDialog(message);
//                        personnelLineItemsForm.txtStartDate.setText(formatedStartDate);
//                        previousStartDate = personnelLineItemsForm.txtStartDate.getText();
//                    }else if ( personnelLineItemsForm.txtStartDate.getText().trim().length() > 0 &&
//                    !validateStartDate( personnelLineItemsForm.txtStartDate.getText().trim()) ) {
//                        personnelLineItemsForm.txtStartDate.requestFocus();
//                        temporary  = true;
//                    }
//                }else if (fe.getSource()== personnelLineItemsForm.txtEndDate){
//                    if(personnelLineItemsForm.txtEndDate==null || personnelLineItemsForm.txtEndDate.getText().trim().length()==0 ||
//                    personnelLineItemsForm.txtEndDate.equals(EMPTY_STRING)){
//                        temporary  = true;
//                        personnelLineItemsForm.txtEndDate.requestFocus();
//                        message = coeusMessageResources.parseMessageKey(INVALID_END_DATE);
//                        CoeusOptionPane.showErrorDialog(message);
//                        personnelLineItemsForm.txtEndDate.setText(formatedEndDate);
//                        previousEndDate = personnelLineItemsForm.txtEndDate.getText();
//                    }else if ( personnelLineItemsForm.txtEndDate.getText().trim().length() > 0 &&
//                    !validateEndDate( personnelLineItemsForm.txtEndDate.getText().trim()) ) {
//                        personnelLineItemsForm.txtEndDate.requestFocus();
//                        temporary  = true;
//                    }
//                }else if ( fe.getSource()== personnelLineItemsForm.txtPercentCharged ||
//                    fe.getSource()== personnelLineItemsForm.txtPercentEffort ||
//                    fe.getSource()== personnelLineItemsForm.txtCostSharingPercent ){
//                    try{
//                        percentCharged = Double.parseDouble( personnelLineItemsForm.txtPercentCharged.getText() );
//                        percentEffort = Double.parseDouble( personnelLineItemsForm.txtPercentEffort.getText() );
//                        if( percentCharged > 100 ){
//                            temporary  = true;
//                            personnelLineItemsForm.txtPercentCharged.setText(EMPTY_STRING+0.00);
//                            personnelLineItemsForm.txtPercentCharged.requestFocus();
//                            message = coeusMessageResources.parseMessageKey(PERCENT_CHARGED_VALIDATION);
//                            CoeusOptionPane.showErrorDialog(message);
//                            percentCharged = Double.parseDouble( personnelLineItemsForm.txtPercentCharged.getText() );
//                        }else if( percentEffort > 100 ) {
//                            temporary  = true;
//                            personnelLineItemsForm.txtPercentEffort.setText(EMPTY_STRING+0.00);
//                            personnelLineItemsForm.txtPercentEffort.requestFocus();
//                            message = coeusMessageResources.parseMessageKey(PERCENT_EFFORT_VALIDATION);
//                            CoeusOptionPane.showErrorDialog(message);
//                            percentEffort = Double.parseDouble( personnelLineItemsForm.txtPercentEffort.getText() );
//                        }
//                        // Commented by chandra for the bug fix #774 - start 15th June 2004
//                        
//                      //  if( percentCharged < percentEffort ){
//                       //     value = percentEffort - percentCharged;
//                      //      personnelLineItemsForm.txtCostSharingPercent.setText(String.valueOf(value));
//                       // }else{
//                            //costSharingPercent = Double.parseDouble( personnelLineItemsForm.txtCostSharingPercent.getText() );
//                            costSharingPercent = Double.parseDouble( personnelLineItemsForm.txtPercentEffort.getText() ) -
//                                                 Double.parseDouble( personnelLineItemsForm.txtPercentCharged.getText() );
//                            //if( costSharingPercent <= 0 ){
//                               // personnelLineItemsForm.txtCostSharingPercent.setText(EMPTY_STRING+0.00);
//                            //}else 
//                            // if( costSharingPercent == 0.0 || costSharingPercent == 0.00 ){
//                              //  personnelLineItemsForm.txtCostSharingPercent.setText(EMPTY_STRING+0.00);
//                           // }else
//                                if( costSharingPercent > 100 ){
//                                temporary  = true;
//                                personnelLineItemsForm.txtCostSharingPercent.requestFocus();
//                                message = coeusMessageResources.parseMessageKey(COST_SHARING_PERCENT_VALIDATION);
//                                CoeusOptionPane.showErrorDialog(message);
//                                personnelLineItemsForm.txtCostSharingPercent.setText(EMPTY_STRING + value);
//                            }else{
//                                personnelLineItemsForm.txtCostSharingPercent.setText(personnelLineItemsForm.txtCostSharingPercent.getText());
//                            }
//                       // }
//                            // Commented by chandra for the bug fix #774 - End 15th June 2004
//                    }catch (NumberFormatException numberFormatException){
//                        numberFormatException.getMessage();
//                    }
//
//                }
//            }
//            temporary=true;
//        }
        
        
        /** Added by chandra
         *Added code to get the modified values or not.
         */
           public void focusLost(FocusEvent fe){
               String formatedStartDate = dateUtils.formatDate(budgetDetailBean.getLineItemStartDate().toString(),SIMPLE_DATE_FORMAT);
               String formatedEndDate = dateUtils.formatDate(budgetDetailBean.getLineItemEndDate().toString(),SIMPLE_DATE_FORMAT);
               percentCharged = Double.parseDouble( personnelLineItemsForm.txtPercentCharged.getText() );
               percentEffort = Double.parseDouble( personnelLineItemsForm.txtPercentEffort.getText() );
               costSharingPercent= Double.parseDouble( personnelLineItemsForm.txtCostSharingPercent.getText() );
               boolean dataChanegd = false;
               temporary = fe.isTemporary();
               if( !temporary ){
                   temporary  = true;
                   if ( fe.getSource()== personnelLineItemsForm.txtStartDate ){
                       if(personnelLineItemsForm.txtStartDate==null || personnelLineItemsForm.txtStartDate.getText().trim().length()==0 ||
                       personnelLineItemsForm.txtStartDate.equals(EMPTY_STRING)){
                           personnelLineItemsForm.txtStartDate.requestFocus();
                           message = coeusMessageResources.parseMessageKey(INVALID_START_DATE);
                           CoeusOptionPane.showErrorDialog(message);
                           personnelLineItemsForm.txtStartDate.setText(formatedStartDate);
                           previousStartDate = personnelLineItemsForm.txtStartDate.getText();
                       }else if ( personnelLineItemsForm.txtStartDate.getText().trim().length() > 0 &&
                       !validateStartDate( personnelLineItemsForm.txtStartDate.getText().trim()) ) {
                           personnelLineItemsForm.txtStartDate.requestFocus();
                           temporary  = true;
                       }
                   }else if (fe.getSource()== personnelLineItemsForm.txtEndDate){
                       if(personnelLineItemsForm.txtEndDate==null || personnelLineItemsForm.txtEndDate.getText().trim().length()==0 ||
                       personnelLineItemsForm.txtEndDate.equals(EMPTY_STRING)){
                           temporary  = true;
                           personnelLineItemsForm.txtEndDate.requestFocus();
                           message = coeusMessageResources.parseMessageKey(INVALID_END_DATE);
                           CoeusOptionPane.showErrorDialog(message);
                           personnelLineItemsForm.txtEndDate.setText(formatedEndDate);
                           previousEndDate = personnelLineItemsForm.txtEndDate.getText();
                       }else if ( personnelLineItemsForm.txtEndDate.getText().trim().length() > 0 &&
                       !validateEndDate( personnelLineItemsForm.txtEndDate.getText().trim()) ) {
                           personnelLineItemsForm.txtEndDate.requestFocus();
                           temporary  = true;
                       }
                   }else if ( fe.getSource()== personnelLineItemsForm.txtPercentCharged){
                       if(isDataModified()){
                       if( percentCharged > 100 ){
                           temporary  = true;
                           dataChanegd = true;
                           personnelLineItemsForm.txtPercentCharged.setText(EMPTY_STRING+0.00);
                           personnelLineItemsForm.txtPercentCharged.requestFocus();
                           message = coeusMessageResources.parseMessageKey(PERCENT_CHARGED_VALIDATION);
                           CoeusOptionPane.showErrorDialog(message);
                           percentCharged = Double.parseDouble( personnelLineItemsForm.txtPercentCharged.getText() );
                       }
                           if( costSharingPercent > 100 ){
                               temporary  = true;
                               personnelLineItemsForm.txtCostSharingPercent.requestFocus();
                               message = coeusMessageResources.parseMessageKey(COST_SHARING_PERCENT_VALIDATION);
                               CoeusOptionPane.showErrorDialog(message);
                               personnelLineItemsForm.txtCostSharingPercent.setText(EMPTY_STRING + value);
                           }else{
                               double costSharingPercent1 = Double.parseDouble( personnelLineItemsForm.txtPercentEffort.getText() ) -
                                                 Double.parseDouble( personnelLineItemsForm.txtPercentCharged.getText() );
                               personnelLineItemsForm.txtCostSharingPercent.setText(EMPTY_STRING+costSharingPercent1);
                           }
                       }else{
                           personnelLineItemsForm.txtCostSharingPercent.setText(personnelLineItemsForm.txtCostSharingPercent.getText());
                       }
                   }else if(fe.getSource()== personnelLineItemsForm.txtPercentEffort){
                        if(isDataModified()){
                       if( percentEffort > 100 ) {
                           temporary  = true;
                           personnelLineItemsForm.txtPercentEffort.setText(EMPTY_STRING+0.00);
                           personnelLineItemsForm.txtPercentEffort.requestFocus();
                           message = coeusMessageResources.parseMessageKey(PERCENT_EFFORT_VALIDATION);
                           CoeusOptionPane.showErrorDialog(message);
                           percentEffort = Double.parseDouble( personnelLineItemsForm.txtPercentEffort.getText() );
                       }
                           if( costSharingPercent > 100 ){
                               temporary  = true;
                               personnelLineItemsForm.txtCostSharingPercent.requestFocus();
                               message = coeusMessageResources.parseMessageKey(COST_SHARING_PERCENT_VALIDATION);
                               CoeusOptionPane.showErrorDialog(message);
                               personnelLineItemsForm.txtCostSharingPercent.setText(EMPTY_STRING + value);
                           }else{
                               double costSharingPercent1 = Double.parseDouble( personnelLineItemsForm.txtPercentEffort.getText() ) -
                                                 Double.parseDouble( personnelLineItemsForm.txtPercentCharged.getText() );
                               personnelLineItemsForm.txtCostSharingPercent.setText(EMPTY_STRING +costSharingPercent1);
                           }
                       }else{
                           personnelLineItemsForm.txtCostSharingPercent.setText(personnelLineItemsForm.txtCostSharingPercent.getText());
                       }
                   }
               }
               temporary=true;
           }
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
    }
}
