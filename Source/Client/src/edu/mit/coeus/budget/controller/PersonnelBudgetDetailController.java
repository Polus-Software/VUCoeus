/*
 * PersonnelBudgetDetailController.java
 *
 * Created on October 15, 2003, 17:04 PM
 */

/*
 * PMD check performed, and commented unused imports and variables on 01-MAR-2011
 * by Maharaja Palanichamy
 */

package edu.mit.coeus.budget.controller;

import edu.mit.coeus.budget.bean.BudgetPeriodBean;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.ListSelectionModel;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import java.util.Vector;
import java.util.Hashtable;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.budget.controller.Controller;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.budget.gui.PersonnelBudgetDetailsForm;
import edu.mit.coeus.budget.bean.BudgetDetailBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetPersonnelDetailsBean;
import edu.mit.coeus.budget.bean.BudgetDetailCalAmountsBean;
import edu.mit.coeus.budget.bean.BudgetPersonnelCalAmountsBean;
import edu.mit.coeus.budget.bean.BudgetPersonsBean;
import edu.mit.coeus.budget.bean.ProposalRatesBean;
import edu.mit.coeus.budget.bean.ProposalLARatesBean;
import edu.mit.coeus.exception.CoeusClientException;

import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.budget.gui.BudgetMessageForm;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.DateUtils ;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.budget.gui.PersonnelBudgetDetailTable;
import edu.mit.coeus.propdev.bean.ProposalHierarchyBean;
import edu.mit.coeus.propdev.gui.ProposalHierarchyContoller;
import edu.mit.coeus.utils.StrictEquals;
//import javax.swing.SwingUtilities;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author ranjeeva
 */

public class PersonnelBudgetDetailController extends Controller implements TypeConstants, ActionListener , BeanUpdatedListener{
    
    //constants
    /** Constants for Salary Calculation */
    private final char CALCULATE_SALARY ='S';
    /** Constants for Calculation of All PersonnelBudget */
    private final char PER_BUDGET_CALCULATION ='P';
    /** Constants for Maximum Percentage Entry */
    private final int MAX_PER_CHARGE_ENTRY = 100;
    private final int MAX_PER_EFFORT_ENTRY =100;
    private static final String COULD_NOT_CONTACT_SERVER = "Could not contact server";
    
    //Validation Messages
    private static final String SAVE_CHANGES = "budget_saveChanges_exceptionCode.1210"; //"Do you want to save changes?          \n ";
    private static final String DELETE_MESSAGES = "budget_deleteTableRow_exceptionCode.1211"; //"Do you want to delete this row ?     ";
    private static final String INVALID_PER_EFFORT_CHARGED = "budget_personnelBudget_exceptionCode.1201"; //"Percentage of Effort cannot be less than Percentage Charged  ";
    private static final String INVALID_PER_CHARGE_ENTRY = "budget_personnelBudget_exceptionCode.1202"; //"Percentage Charged cannot be greater than 100.";
    private static final String INVALID_PER_EFFORT_ENTRY = "budget_personnelBudget_exceptionCode.1203"; //"Percentage Effort cannot be greater than 100.";
    
    private static final String DISPLAY_DATE_FORMAT = "dd-MMM-yyyy";
    private static final String ACTYPE = "acType";
    private static final String PERSONNUMBER = "personNumber";
    
    private BudgetDetailBean budgetDetailBean;
    private BudgetInfoBean budgetInfoBean;
    private BudgetPersonnelDetailsBean budgetPersDetailsBean;
    private BudgetPersonnelCalAmountsBean budgetPersonnelCalAmountsBean;
    private PersonnelBudgetLineItemController personnelLineItemController;
    
    /** Vector to Hold all the BaseWindow BudgetPersonnelDetailsBeans */
    public CoeusVector vecBudgetPersonnelDetailsBean;
    
    /** Vector to Hold all the BaseWindow BudgetPersonnelCalAmountsBean */
    public CoeusVector vecBudgetPersonnelCalAmountsBean;
    
    /** Hashtable to hold the Vector of CalAmountBeans for each Person */
    public Hashtable hashTabALLPersonnelCalAmountsBean;
    
    
    /** Vector to Hold all deleted BudgetPersonnelDetailsBeans from the above Vector */
    public CoeusVector vecDeletedPersonnelDetailsBean;
    
    /** Vector to Hold all deleted BudgetPersonnelCalAmountsBean from the above Vector */
    public CoeusVector vecDeletedPersonnelCalAmountsBean;
    
    private QueryEngine queryEngine;
    private String queryKey;
    private DateUtils dateUtils;
    private final String conURL = "/BudgetCalculationMaintenanceServlet";
    
    /** PersonnelBudgetDetailsForm form instance */
    public PersonnelBudgetDetailsForm personnelBudgetDetailsForm;
    
    private CoeusMessageResources coeusMessageResources;
    private Component parent;
    boolean modal;
    boolean isFormModified ;
    private boolean parentProposal;
    private ProposalHierarchyBean proposalHierarchyBean;
    
    /** Creates a new instance of PersonnelBudgetDetailController */
    
    public PersonnelBudgetDetailController() {
        super();
        this.parent= CoeusGuiConstants.getMDIForm();
        this.modal =  true;
        budgetDetailBean = null;
        initialiseController();
    }
    
    /** Creates a new instance of PersonnelBudgetDetailController with Budget Bean instance
     * @param parent CoeusAppletMDIForm instance
     * @param modal if <true> a Model window
     * @param budgetDetailBean BudgetDetailBean
     */
    
    public PersonnelBudgetDetailController(CoeusAppletMDIForm  parent, boolean modal, BudgetDetailBean budgetDetailBean) {
        super(budgetDetailBean);
        this.parent= parent;
        this.budgetDetailBean = budgetDetailBean;
        initialiseController();
        setFormData(budgetDetailBean);
    }
    
    /*
     * initialising the Controller
     */
    private void initialiseController() {
        personnelBudgetDetailsForm =  new PersonnelBudgetDetailsForm((Component) parent,modal);
        dateUtils = new DateUtils();
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        formatFields();
    }
    
    
    /** Get the Table Instance of PersonnelBudgetDetailTable
     * @return instance of PersonnelBudgetDetailTable
     */
    
    public PersonnelBudgetDetailTable getTableInstance() {
        return (PersonnelBudgetDetailTable) ( (PersonnelBudgetDetailsForm) getControlledUI()).getPersonnelBudgetDetailTable();
    }
    
    /** To Get the Table Model of PersonnelBudgetDetailTable
     * @return Model of PersonnelBudgetDetailTable
     */
    
    public PersonnelBudgetDetailTable.PersonnelBudgetTableModel getParentTableModel() {
        return getTableInstance().getTableModel();
    }
    
    /** Method to Return the Current Vector of BudgetPersonnelDetailsBean for the Form Instance
     * @return CoeusVector containing BudgetPersonnelDetailsBean
     */
    
    public CoeusVector getVecBudgetPersonnelDetailsBean() {
        if(vecBudgetPersonnelDetailsBean != null) {
            return vecBudgetPersonnelDetailsBean;
        }
        else
            return new CoeusVector();
    }
    
    /** Confirmation Window to check whether to ssave the changes and save on
     * confirmation
     */
    public void checkForSaveChanges() {
        int option = JOptionPane.NO_OPTION;
        option  = CoeusOptionPane.showQuestionDialog(
        coeusMessageResources.parseMessageKey(SAVE_CHANGES),
        CoeusOptionPane.OPTION_YES_NO_CANCEL,
        CoeusOptionPane.DEFAULT_CANCEL);
        switch(option) {
            case ( JOptionPane.YES_OPTION ):
                try{
                    
                    if(validate()) {
                        saveFormData();
                        close();
                    }
                    
                }catch(Exception e){
                    e.getMessage();
                    CoeusOptionPane.showErrorDialog(e.getMessage());
                }
                break;
                
            case ( JOptionPane.NO_OPTION ):
                close();
                break;
            case ( JOptionPane.CANCEL_OPTION ) :
                return;
        }
    }
    
    /*
     * Check Is Form being Modified
     */
    
    /** Check if the Form Modified
     * @return if <true> when Form Modified
     */
    public boolean checkIsFormModified() {
        try {
            
            if(!(budgetDetailBean.getQuantity() ==
//            Integer.parseInt(personnelBudgetDetailsForm.txtQuantity.getText()))) {
            Double.parseDouble(personnelBudgetDetailsForm.txtQuantity.getText()))) {
                return true ;
            }
            
            BudgetDetailBean budgetDetailBeanTemp = new BudgetDetailBean();
            budgetDetailBean.setLineItemCost(
            Double.parseDouble(personnelBudgetDetailsForm.txtCost.getValue()));
            budgetDetailBean.setCostSharingAmount(
            Double.parseDouble(personnelBudgetDetailsForm.txtCostShare.getValue()));
            budgetDetailBean.setUnderRecoveryAmount(
            Double.parseDouble(personnelBudgetDetailsForm.txtUnderRecovery.getValue()));
            
            budgetDetailBean.setQuantity(
            Double.parseDouble(personnelBudgetDetailsForm.txtQuantity.getText()));
//            Integer.parseInt(personnelBudgetDetailsForm.txtQuantity.getText()));
            
            
            Equals equalsActype = new Equals(ACTYPE,null);
            NotEquals notequalsActype = new NotEquals(ACTYPE,TypeConstants.DELETE_RECORD);
            Or actypeBoth = new Or(equalsActype,notequalsActype);
            CoeusVector vecBudgetDetailBean = queryEngine.executeQuery(queryKey,budgetDetailBeanTemp.getClass(),actypeBoth);
            if(vecBudgetDetailBean != null && vecBudgetDetailBean.size() > 0) {
                budgetDetailBeanTemp = (BudgetDetailBean) vecBudgetDetailBean.get(0);
            }
            
            
            //Stricts Equals to compare the budgetDetailBean if changed before clicking Ok
            StrictEquals strictEquals = new StrictEquals();
            if(!strictEquals.compare(budgetDetailBeanTemp, budgetDetailBean)) {
                return true;
            }
        } catch(Exception e) {
            e.getMessage();
        }
        
        return false;
    }
    
    /** The actionPerformed method for responding to actions on the buttons of the Form
     * @param actionEvent Object
     */
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        
        //***************** OK Button  action *******************
        
        if (source.equals(personnelBudgetDetailsForm.btnOk)){
            
            try {
                getTableInstance().getTableCellEditor().stopCellEditing();
                ((PersonnelBudgetDetailsForm) getControlledUI()).dlgPersonnelBudgetDetailsForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                ( (PersonnelBudgetDetailsForm) getControlledUI()).dlgPersonnelBudgetDetailsForm.getGlassPane().setVisible(true);
                
                if(isSaveRequired()) {
                    if(validate()) {
                        // first calculate and then save the Data
                        saveFormData();
                        close();
                    }
                }
                else {
                    if(validate()) {
                        saveFormData();
                        close();
                    }
                }
                
            }catch(Exception e) {
                e.getMessage();
            }
            
            
            ((PersonnelBudgetDetailsForm) getControlledUI()).dlgPersonnelBudgetDetailsForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            ((PersonnelBudgetDetailsForm) getControlledUI()).dlgPersonnelBudgetDetailsForm.getGlassPane().setVisible(false);
            
            
        }
        
        //***************** ADD Button  action *******************
        if (source.equals(personnelBudgetDetailsForm.btnAdd)){ 
            ((PersonnelBudgetDetailsForm) getControlledUI()).dlgPersonnelBudgetDetailsForm.setCursor(new java.awt.Cursor(Cursor.WAIT_CURSOR));
            ( (PersonnelBudgetDetailsForm) getControlledUI()).dlgPersonnelBudgetDetailsForm.getGlassPane().setVisible(true);
            
            
            SelectBudgetPersonsController selectBudgetPersonsController = new SelectBudgetPersonsController(this);
            selectBudgetPersonsController.setFormData(budgetDetailBean);
            
            setSaveRequired(true);
            
            ((PersonnelBudgetDetailsForm) getControlledUI()).dlgPersonnelBudgetDetailsForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            ((PersonnelBudgetDetailsForm) getControlledUI()).dlgPersonnelBudgetDetailsForm.getGlassPane().setVisible(false);
            
        }
        //***************** CANCEL Button  action *******************
        if (source.equals(personnelBudgetDetailsForm.btnCancel)){
            try {
                int rowSelected = getTableInstance().getSelectedRow();
                if(rowSelected > -1) {
                    getTableInstance().getTableCellEditor().stopCellEditing();
                    if(!(budgetDetailBean.getQuantity() ==
                    Double.parseDouble(personnelBudgetDetailsForm.txtQuantity.getText()))) {
//                    Integer.parseInt(personnelBudgetDetailsForm.txtQuantity.getText()))) {
                        setSaveRequired(true);
                    }
                    
                    
                    if(isSaveRequired() )
                        checkForSaveChanges();
                    else {
                        if(validate()) {
                            close();
                        }
                    }
                    
                } else {
                    if(isSaveRequired() ) {
                        checkForSaveChanges();
                    }
                    else
                        close();
                    
                }
                
            }catch(Exception e) {
                e.getMessage();
            }
            return;
        }
        
        //***************** DELETE Button action *******************
        if (source.equals(personnelBudgetDetailsForm.btnDelete)){
            getTableInstance().getTableCellEditor().stopCellEditing();
            int rowIndex = getTableInstance().getSelectedRow();
            
            if(vecBudgetPersonnelDetailsBean != null && vecBudgetPersonnelDetailsBean.size() < 1) {
                return;
            }
            
            
            try {
                if(rowIndex != -1 && rowIndex >= 0){
                    
                    int selectedOption = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(DELETE_MESSAGES),
                    CoeusOptionPane.OPTION_YES_NO,
                    CoeusOptionPane.DEFAULT_YES);
                    if(selectedOption == CoeusOptionPane.SELECTION_YES){
                        setSaveRequired(true);
                        calculateCostElementDetails(rowIndex);
                        
                        //Add the deleted vector to a vector that holds deleted vector
                        vecDeletedPersonnelDetailsBean.add(vecBudgetPersonnelDetailsBean.get(rowIndex));
                        
                        vecBudgetPersonnelDetailsBean.remove(rowIndex);
                        getTableInstance().getTableModel().fireTableRowsDeleted(rowIndex,vecBudgetPersonnelDetailsBean.size());
                        
                        getTableInstance().getTableModel().setData(vecBudgetPersonnelDetailsBean);
                        if(vecBudgetPersonnelDetailsBean.size() > 1 ) {
                            if(rowIndex > 0) {
                                getTableInstance().setRowSelectionInterval(rowIndex-1,rowIndex-1);
                            }
                            else
                                getTableInstance().setRowSelectionInterval(rowIndex,rowIndex);
                        }
                        
                        if(vecBudgetPersonnelDetailsBean.size() == 1 ) {
                            getTableInstance().setRowSelectionInterval(0,0);
                        }
                        
                        if(vecBudgetPersonnelDetailsBean.size() < 1) {
                            getTableInstance().getTableModel().setData(new CoeusVector());
                            personnelBudgetDetailsForm.pnlLineItemCalAmount.setFormData(new CoeusVector());
                            String zeroString = "0";
                            personnelBudgetDetailsForm.pnlLineItemCalAmount.setFormData(new CoeusVector());
                            personnelBudgetDetailsForm.txtCost.setText(zeroString);
                            personnelBudgetDetailsForm.txtQuantity.setText(zeroString);
                            personnelBudgetDetailsForm.txtCostShare.setText(zeroString);
                            personnelBudgetDetailsForm.txtUnderRecovery.setText(zeroString);
                        }
                        
                    }
                }
                
                setSaveRequired(true);
            } catch(Exception e) {
                e.getMessage();
            }
            //setting the exact quanity field value
            personnelBudgetDetailsForm.txtQuantity.setText(getNumOfQuantity(vecBudgetPersonnelDetailsBean));
            setSaveRequired(true);
            
        }
        
        //***************** DETAILS Button action *******************
        if (source.equals(personnelBudgetDetailsForm.btnDetails)){
            getTableInstance().getTableCellEditor().stopCellEditing();
            callPersonnelBudgetLineItemBean(getTableInstance().getSelectedRow());
        }
        //***************** CALCULATE Button action *******************
        if (source.equals(personnelBudgetDetailsForm.btnCalculate)){
            ((PersonnelBudgetDetailsForm) getControlledUI()).dlgPersonnelBudgetDetailsForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            ( (PersonnelBudgetDetailsForm) getControlledUI()).dlgPersonnelBudgetDetailsForm.getGlassPane().setVisible(true);
            
            try {
                if(validate()) {
                    int rowSelected = getTableInstance().getSelectedRow();
                    if(rowSelected > -1) {
                        
                        getTableInstance().getTableCellEditor().stopCellEditing();
                      // Modified for COEUSQA-2641 4.4.2 - Calculate Button not working - Start
                      // When user click Calculate Button, this method will calculate rates for all the users and updated to the bean.  
                           personnelBudgetCalculation();
                      // Modified for COEUSQA-2641 - End
                        /** Bug fix :Id - 700.
                         * Refresh the requested salary value 
                         * after the salary is recalculated.
                         * start 16/03/2004 - Bug fix :  Id - 700
                         */
                        getTableInstance().getTableModel().setData(vecBudgetPersonnelDetailsBean);
                        getTableInstance().getTableModel().fireTableRowsUpdated(getTableInstance().getSelectedRow(), getTableInstance().getSelectedRow());
                        // end 16/03/2004 - Bug fix :  Id - 700
                        
                        String personNumber = ((BudgetPersonnelDetailsBean) vecBudgetPersonnelDetailsBean.get(rowSelected)).getPersonNumber()+"";
                        CoeusVector calAmountBean = (CoeusVector) hashTabALLPersonnelCalAmountsBean.get(personNumber);
                        personnelBudgetDetailsForm.pnlLineItemCalAmount.setFormData(calAmountBean );
                        
                        calculatePersonnelBudgetDetails();
                        setSaveRequired(true);
                    }
                }
            } catch(Exception e) {
                e.getMessage();
            }
            
            ((PersonnelBudgetDetailsForm) getControlledUI()).dlgPersonnelBudgetDetailsForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            ((PersonnelBudgetDetailsForm) getControlledUI()).dlgPersonnelBudgetDetailsForm.getGlassPane().setVisible(false);
            
        }
        
        //***************** PERSONS Button action *******************
        if (source.equals(personnelBudgetDetailsForm.btnPersons)){
            ((PersonnelBudgetDetailsForm) getControlledUI()).dlgPersonnelBudgetDetailsForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            ( (PersonnelBudgetDetailsForm) getControlledUI()).dlgPersonnelBudgetDetailsForm.getGlassPane().setVisible(true);
            
            if(isParentProposal()){
                try{
                     ProposalHierarchyContoller hierarchyController  = new ProposalHierarchyContoller(getProposalHierarchyBean());
                     hierarchyController.setModule(CoeusGuiConstants.BUDGET_MODULE);
                     hierarchyController.setFormData(budgetInfoBean.getUnitNumber());
                     ((PersonnelBudgetDetailsForm) getControlledUI()).dlgPersonnelBudgetDetailsForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                     ((PersonnelBudgetDetailsForm) getControlledUI()).dlgPersonnelBudgetDetailsForm.getGlassPane().setVisible(false);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }else{
                //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
                if(budgetInfoBean.getUnitNumber()==null){
                    budgetInfoBean.setUnitNumber(budgetDetailBean.getUnitNumber());
                }
                //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
                BudgetPersonController budgetPersonController = new  BudgetPersonController((CoeusAppletMDIForm) parent,budgetInfoBean);
                budgetPersonController.setFormData(budgetInfoBean);
                budgetPersonController.setFunctionType(getFunctionType());
                budgetPersonController.display();
                ((PersonnelBudgetDetailsForm) getControlledUI()).dlgPersonnelBudgetDetailsForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                ((PersonnelBudgetDetailsForm) getControlledUI()).dlgPersonnelBudgetDetailsForm.getGlassPane().setVisible(false);
            }
        }
        
        
    } // end of actionperformed method
    
    
    /**
     * Method to call server side  calculation for PersonnelBudgetDetail and reset the
     * PersonnelBudgetDetail and PersonnelCalAmount details with calculated value
     *
     */
    private void personnelBudgetCalculation() {
        
        
        try {
            Hashtable personnelBudgetCalculationResult = (Hashtable) getCalculatedHashTable( getBeansForBtnCalculate(),PER_BUDGET_CALCULATION);
            if(personnelBudgetCalculationResult != null) {
                vecBudgetPersonnelDetailsBean = (CoeusVector) personnelBudgetCalculationResult.get(BudgetPersonnelDetailsBean.class);
                CoeusVector vecPersonnelCalAmountsBean = (CoeusVector) personnelBudgetCalculationResult.get(BudgetPersonnelCalAmountsBean.class);
                
                hashTabALLPersonnelCalAmountsBean = new Hashtable();
               
                for(int i=0;i<vecBudgetPersonnelDetailsBean.size();i++) {
                    
                    BudgetPersonnelDetailsBean budgetDetailsBean = (BudgetPersonnelDetailsBean) vecBudgetPersonnelDetailsBean.get(i);
                    String personNumber = budgetDetailsBean.getPersonNumber()+"";
                    Equals equalsPersonNumber = new Equals(PERSONNUMBER,new Integer(personNumber));
                    CoeusVector vecPersonnelCalAmountsBeanFiltered = (CoeusVector) vecPersonnelCalAmountsBean.filter(equalsPersonNumber);
                    hashTabALLPersonnelCalAmountsBean.put(personNumber,(Object) vecPersonnelCalAmountsBeanFiltered);
                    
                }                
            }
            
        } catch (Exception expCalulate) {
            expCalulate.getMessage();
        }
        
    }
    
    
    /**
     * Method to get the Hash Table of Calculated Components from the Server
     *@param hashTableObj Hashtable containing the vector of required Beans for calcuation
     *@param functionType char Function Type
     *@return Hashtable of vector of Calculated Beans
     */
    // @ modified by chandra to handle Exception.
    public Hashtable getCalculatedHashTable(Hashtable hashTableObj,char functionType) throws CoeusClientException{
        
        RequesterBean requesterBean  = new RequesterBean();
        requesterBean.setFunctionType(functionType);
        requesterBean.setDataObject(hashTableObj);
        String connectTo = CoeusGuiConstants.CONNECTION_URL + conURL;
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean );
        comm.send();
        ResponderBean responderBean = comm.getResponse();
         if ( responderBean !=null ){
            if(responderBean.isSuccessfulResponse()) {
                Vector vecofMessages = responderBean.getDataObjects();
                if (vecofMessages  != null && vecofMessages.size() > 0) {
                    displayRatesNotAvailableMessages(vecofMessages);
                }
                return (Hashtable) responderBean.getDataObject();
            }else {
                throw new CoeusClientException(responderBean.getMessage(), CoeusClientException.ERROR_MESSAGE);
            }
         }else{
             throw new CoeusClientException(COULD_NOT_CONTACT_SERVER);
         }
        
       // return null;
    }
    
    /**
     * Display Rates Not available in a BudgetMessageForm
     *@param vecofMessages Vector of messages
     */
    
    public void displayRatesNotAvailableMessages(Vector vecofMessages) {
        BudgetMessageForm budgetMessageForm = new BudgetMessageForm();
        budgetMessageForm.setFormData(vecofMessages);
        budgetMessageForm.display();
    }
    
    /**
     * Method to get the Vector of Beans for Salary Calculation in a Hashtable
     *@param passBudgetPersonnelDetailsBean BudgetPersonnelDetailsBean
     *@return Hashtable of vector of Calculated Beans
     */
    
    public Hashtable getSalaryCalculationBeans(BudgetPersonnelDetailsBean passBudgetPersonnelDetailsBean ) {
        
        BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = null;
        int rowIndex = getTableInstance().getSelectedRow();
        budgetPersonnelDetailsBean  = (BudgetPersonnelDetailsBean) vecBudgetPersonnelDetailsBean.get(rowIndex);
        
        vecBudgetPersonnelDetailsBean.set(rowIndex, passBudgetPersonnelDetailsBean);
        
        Hashtable salaryCalHashTable = new Hashtable();
        CoeusVector coeusVector = new CoeusVector();
        coeusVector.add(budgetPersonnelDetailsBean);
        salaryCalHashTable.put( BudgetPersonnelDetailsBean.class,coeusVector);
        
        CoeusVector vecBudgetPersonBean = null;
        CoeusVector vecProposalRatesBean = null;       
        
        try {
            
            // Getting the BudgetPersonsBean of the selected Person
            BudgetPersonsBean budgetPersonsBean =  new BudgetPersonsBean();
            budgetPersonsBean.setProposalNumber(budgetDetailBean.getProposalNumber());
            budgetPersonsBean.setVersionNumber(budgetDetailBean.getVersionNumber());
            budgetPersonsBean.setJobCode(budgetPersonnelDetailsBean.getJobCode());
            //Code added for Case#3652 - Adding two tabs to budget
            budgetPersonsBean.setPersonId(budgetPersonnelDetailsBean.getPersonId());
            
            Equals equalsActype = new Equals(ACTYPE,null);
            NotEquals notequalsActype = new NotEquals(ACTYPE,TypeConstants.DELETE_RECORD);
            Or actypeBoth = new Or(equalsActype,notequalsActype);
            
            //Code commented and modified for Case#3652 - Adding two tabs to budget
//            Equals equalsJobCode = new Equals("jobCode",budgetPersonnelDetailsBean.getJobCode());
            Equals equalsJobCode = new Equals("personId",budgetPersonnelDetailsBean.getPersonId());
            And bothType = new And(equalsJobCode,actypeBoth);
            
            vecBudgetPersonBean = queryEngine.executeQuery(queryKey,budgetPersonsBean.getClass(),bothType);
            salaryCalHashTable.put( BudgetPersonsBean.class, vecBudgetPersonBean);
            
            // Getting the ProposalRatesBean  with Inflation values
            ProposalRatesBean  proposalRatesBean = new ProposalRatesBean();
            proposalRatesBean.setProposalNumber(budgetDetailBean.getProposalNumber());
            proposalRatesBean.setVersionNumber(budgetDetailBean.getVersionNumber());
            
            vecProposalRatesBean = queryEngine.executeQuery(queryKey,proposalRatesBean.getClass(),actypeBoth);
            
            salaryCalHashTable.put(ProposalRatesBean.class, vecProposalRatesBean);
            
            //Added for Case 3197 - Allow for the generation of project period greater than 12 months -start
            CoeusVector vecBudgetPeriodBean = queryEngine.executeQuery(queryKey, BudgetPeriodBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            salaryCalHashTable.put( BudgetPeriodBean.class, vecBudgetPeriodBean);
            //3197- End
            
            CoeusVector vecBudgetDetailBean = new CoeusVector();
            vecBudgetDetailBean.add(budgetDetailBean);
            salaryCalHashTable.put(BudgetDetailBean.class, vecBudgetDetailBean);
            
        } catch(CoeusException coeusException) {
            coeusException.getMessage();
        }
        
        
        return salaryCalHashTable;
    }
    
    /**
     * Method to get the Vector of Beans for Calculation in a Hashtable
     *@return Hashtable of vector of Calculated Beans
     */
    
    public Hashtable getBeansForBtnCalculate() {
        
        Hashtable calulateCompHashTable = new Hashtable();
        CoeusVector vecBudgetPersonBean = null;
        CoeusVector vecProposalRatesBean = null;
        
        try {
            
            Equals equalsActype = new Equals(ACTYPE,null);
            NotEquals notequalsActype = new NotEquals(ACTYPE,TypeConstants.DELETE_RECORD);
            Or actypeBoth = new Or(equalsActype,notequalsActype);
            
            // Adding BudgetDetailBean
            CoeusVector vecBudgetDetailBean = new CoeusVector();
            vecBudgetDetailBean.add(budgetDetailBean);
            calulateCompHashTable.put(BudgetDetailBean.class, vecBudgetDetailBean);
            
            // Adding BudgetInfoBean
            CoeusVector vecBudgetInfoBean = new CoeusVector();
            BudgetInfoBean budgetInfoBean =  new BudgetInfoBean();
            
            vecBudgetInfoBean = queryEngine.executeQuery(queryKey,budgetInfoBean.getClass(),actypeBoth);
            calulateCompHashTable.put(BudgetInfoBean.class, vecBudgetInfoBean);
            
            // Adding BudgetPersonnelDetailsBean
            calulateCompHashTable.put(BudgetPersonnelDetailsBean.class, vecBudgetPersonnelDetailsBean);
            
            CoeusVector vecPersonnelBudgetCalAmountBean = new CoeusVector();
            java.util.Enumeration enumPersonnelBudgetCalAmountBean = hashTabALLPersonnelCalAmountsBean.elements();
            
            while(enumPersonnelBudgetCalAmountBean.hasMoreElements()) {
                CoeusVector vecEachCalAmountBean = new CoeusVector();
                vecEachCalAmountBean = (CoeusVector) enumPersonnelBudgetCalAmountBean.nextElement();
                for(int i=0;i< vecEachCalAmountBean.size();i++) {
                    BudgetPersonnelCalAmountsBean budgetCalAmountsBean = (BudgetPersonnelCalAmountsBean) vecEachCalAmountBean.get(i);
                    vecPersonnelBudgetCalAmountBean.add(budgetCalAmountsBean);
                }
            }
            
            // Adding BudgetPersonnelCalAmountsBean
            calulateCompHashTable.put(BudgetPersonnelCalAmountsBean.class, vecPersonnelBudgetCalAmountBean);
            
            // Getting the BudgetPersonsBean of the selected Person
            BudgetPersonsBean budgetPersonsBean =  new BudgetPersonsBean();
            vecBudgetPersonBean = queryEngine.executeQuery(queryKey,budgetPersonsBean.getClass(),actypeBoth);
            calulateCompHashTable.put( BudgetPersonsBean.class, vecBudgetPersonBean);
            
            // Getting the ProposalRatesBean  with Inflation values
            ProposalRatesBean  proposalRatesBean = new ProposalRatesBean();
            vecProposalRatesBean = queryEngine.executeQuery(queryKey,proposalRatesBean.getClass(),actypeBoth);
            
            calulateCompHashTable.put(ProposalRatesBean.class, vecProposalRatesBean);
            
            // Getting the ProposalLARatesBean  with Inflation values
            ProposalLARatesBean proposalLARatesBean = new ProposalLARatesBean();
            CoeusVector vecProposalLARatesBean = queryEngine.executeQuery(queryKey,proposalLARatesBean.getClass(),actypeBoth);
            calulateCompHashTable.put(ProposalLARatesBean.class, vecProposalLARatesBean);
            
             //Added for Case 3197 - Allow for the generation of project period greater than 12 months -Start
            // Getting the BudgetPeriods information
            CoeusVector vecBudgetPeriodBean = queryEngine.executeQuery(queryKey, BudgetPeriodBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            calulateCompHashTable.put( BudgetPeriodBean.class, vecBudgetPeriodBean);            
            //3197- End
            
            
        } catch(CoeusException coeusException) {
            coeusException.getMessage();
        }
        
        
        return calulateCompHashTable;
    }
    
    /*
     * Method to get the Period Type for the Period Type Description
     *@return String Period Type "AP","CY","CC","SP"
     */
    
    private String getPeriodType(String periodTypeDesc) {
        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
        /*String periodArray [][] = {
            {"AP","Academic"},
            {"CY","Calendar"},
            {"CC","Cycle"},
            {"SP","Summer"}
        };
         for(int i=0;i<periodArray.length;i++ ) {
            if(periodTypeDesc.equalsIgnoreCase(periodArray[i][1])) {
                return periodArray[i][0];
            }
        }
        return periodTypeDesc;
         */
        String periodCode = "";
        try{
            String periodArray [][] = fetchPeriodTypes();
            for(int i=0;i<periodArray.length;i++ ) {
                if(periodTypeDesc.equalsIgnoreCase(periodArray[i][1])) {
                    periodCode = periodArray[i][0];
                }
            }
        }catch(CoeusClientException cex){
            cex.printStackTrace();
        }
        return periodCode;
        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
    }
    
    /**
     * Method to calculatePersonnelLineItem and Set the new calculated values of the Beans
     *@param budgetPersonnelDetailsBeanforcal Calculated BudgetPersonnelDetailsBean
     *@return BudgetPersonnelDetailsBean
     */
    
    public BudgetPersonnelDetailsBean calculatePersonnelLineItem(BudgetPersonnelDetailsBean budgetPersonnelDetailsBeanforcal ) 
    throws CoeusClientException{
               
        BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = null;
        //System.out.println("calculatePersonnelLineItem");
        if(vecBudgetPersonnelDetailsBean != null && vecBudgetPersonnelDetailsBean.size() > 0) {
            
            int rowIndex = getTableInstance().getSelectedRow();
            
            try {
                if(rowIndex < 0) {
                    return budgetPersonnelDetailsBean;
                }
                
                if(budgetPersonnelDetailsBeanforcal == null) {
                    budgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean) vecBudgetPersonnelDetailsBean.get(rowIndex);
                }
                else
                    budgetPersonnelDetailsBean = budgetPersonnelDetailsBeanforcal;
                
                //setting the onoffCamous flag to the bean
                budgetPersonnelDetailsBean.setOnOffCampusFlag(budgetDetailBean.isOnOffCampusFlag());
                budgetPersonnelDetailsBean.setApplyInRateFlag(budgetDetailBean.isApplyInRateFlag());
                
                budgetPersonnelDetailsBean.setProposalNumber(budgetDetailBean.getProposalNumber());
                budgetPersonnelDetailsBean.setVersionNumber(budgetDetailBean.getVersionNumber());
                
                
                
                
                Hashtable salaryCalculationResult = (Hashtable) getCalculatedHashTable( getSalaryCalculationBeans(budgetPersonnelDetailsBean),CALCULATE_SALARY);
                if(salaryCalculationResult != null) {
                    CoeusVector vecPersonnelDetailsBean = (CoeusVector) salaryCalculationResult.get(BudgetPersonnelDetailsBean.class);
                    
                    BudgetPersonnelDetailsBean personnelDetailsBean = (BudgetPersonnelDetailsBean ) vecPersonnelDetailsBean.get(0);
                    
                    StrictEquals strictEquals = new StrictEquals();
                    if(!strictEquals.compare(budgetPersonnelDetailsBean, personnelDetailsBean)) {
                        setSaveRequired(true);
                    }
                    
                    budgetPersonnelDetailsBean.setSalaryRequested(personnelDetailsBean.getSalaryRequested());
                    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
                    //personnelDetailsBean.setPeriodType(getPeriodType(personnelDetailsBean.getPeriodType()));
                    String periodType = getPeriodType(personnelDetailsBean.getPeriodType());
                    if(periodType!=null && periodType.length()>0){
                        personnelDetailsBean.setPeriodType(periodType);
                    }else{
                        personnelDetailsBean.setPeriodType(personnelDetailsBean.getPeriodType());
                    }
                    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
                    budgetPersonnelDetailsBean.setPeriodType(personnelDetailsBean.getPeriodType());
                    budgetPersonnelDetailsBean.setCostSharingAmount(personnelDetailsBean.getCostSharingAmount());
                    
                    if(personnelDetailsBean.getCostSharingPercent() > 0) {
                        budgetPersonnelDetailsBean.setCostSharingPercent(personnelDetailsBean.getCostSharingPercent());
                    }
                    else {
                        double costSharingPercent = (personnelDetailsBean.getPercentEffort() - personnelDetailsBean.getPercentCharged());    
                        if(costSharingPercent > 0) {
                            costSharingPercent = (double)Math.round(costSharingPercent*Math.pow(10.0,1.0)/10);
                            budgetPersonnelDetailsBean.setCostSharingPercent(costSharingPercent);
                        }
                    }
                    
                    budgetPersonnelDetailsBean.setUnderRecoveryAmount(personnelDetailsBean.getUnderRecoveryAmount());
                    budgetPersonnelDetailsBean.setApplyInRateFlag(personnelDetailsBean.isApplyInRateFlag());
                    budgetPersonnelDetailsBean.setOnOffCampusFlag(personnelDetailsBean.isOnOffCampusFlag());
                    
                }
                //Bug Fix:1571 Start 3
                //getTableInstance().getTableModel().fireTableRowsUpdated(rowIndex,rowIndex); // vecBudgetPersonnelDetailsBean.size());
                //Bug Fix:1571 End 3 
                
                calculatePersonnelBudgetDetails();
            }catch(Exception e) {
                e.getMessage();
            }
        }
        
        return budgetPersonnelDetailsBean;
    }
    
    /*
     * Method to calcuate the Cost.CostShare Underrecovery by calculating the sum of individual
     * PersonnelBudgetLineItem details and update the Cost,Cost Share,Underrecovery Fields
     */
    /** Calculate of all the PersonnelBudgetDetail Item */
    public void calculatePersonnelBudgetDetails() {
        BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = new BudgetPersonnelDetailsBean();
        //System.out.println("calculatePersonnelBudgetDetails");
        double totalCostValue = 0;
        double totalCostShare = 0;
        double totalUnderRecovery = 0;
        
        for (int index=0;index <vecBudgetPersonnelDetailsBean.size();index++) {
            
            budgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean ) vecBudgetPersonnelDetailsBean.get(index);
            double salaryValue = budgetPersonnelDetailsBean.getSalaryRequested();
            double costShare = budgetPersonnelDetailsBean.getCostSharingAmount();
            double underRecovery = budgetPersonnelDetailsBean.getUnderRecoveryAmount();
            
            
            if(salaryValue < 1) {
                salaryValue = 0;
            }
            if(underRecovery < 1) {
                underRecovery = 0;
            }
            if(costShare < 1) {
                costShare = 0;
            }
            
            totalCostValue+=salaryValue;
            totalCostShare+=costShare;
            totalUnderRecovery+=underRecovery;
        }
        
        String txtCostValue= totalCostValue+"";
        String txtCostShareValue = totalCostShare+"";
        String txtUnderRecoveryValue = totalUnderRecovery+"";
        
        ((PersonnelBudgetDetailsForm) getControlledUI()).txtCost.setText(txtCostValue);
        ((PersonnelBudgetDetailsForm) getControlledUI()).txtCostShare.setText(txtCostShareValue);
        ((PersonnelBudgetDetailsForm) getControlledUI()).txtUnderRecovery.setText(txtUnderRecoveryValue);
        //(getTableInstance().getTableModel()).fireTableRowsUpdated(selectedRow,selectedRow);
    }
    
    /**
     * Method to recalculate The Total Cost Elements on the Panel by summing up all the
     * PersonnelBudgetLineItem thats being added to the Table
     *@param  removedIndex int selected index of the table
     */
    
    public void calculateCostElementDetails(int removedIndex) {
        //System.out.println("calculateCostElementDetails");
        if(vecBudgetPersonnelDetailsBean == null || vecBudgetPersonnelDetailsBean.size() < 1) {
            return;
        }
        
        BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = new BudgetPersonnelDetailsBean();
        budgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean ) vecBudgetPersonnelDetailsBean.get(removedIndex);
        double salaryValue = budgetPersonnelDetailsBean.getSalaryRequested();
        double costShare = budgetPersonnelDetailsBean.getCostSharingAmount();
        double underRecovery = budgetPersonnelDetailsBean.getUnderRecoveryAmount();
        
        double totalCostValue = Double.parseDouble(((PersonnelBudgetDetailsForm) getControlledUI()).txtCost.getValue());
        double totalCostShare = Double.parseDouble(((PersonnelBudgetDetailsForm) getControlledUI()).txtCostShare.getValue());
        double totalUnderRecovery = Double.parseDouble(((PersonnelBudgetDetailsForm) getControlledUI()).txtUnderRecovery.getValue());
        
        totalCostValue-=salaryValue;
        totalCostShare-=costShare;
        totalUnderRecovery-=underRecovery;
        
        String txtCostValue= totalCostValue+"";
        String txtCostShareValue = totalCostShare+"";
        String txtUnderRecoveryValue = totalUnderRecovery+"";
        
        ((PersonnelBudgetDetailsForm) getControlledUI()).txtCost.setText(txtCostValue);
        ((PersonnelBudgetDetailsForm) getControlledUI()).txtCostShare.setText(txtCostShareValue);
        ((PersonnelBudgetDetailsForm) getControlledUI()).txtUnderRecovery.setText(txtUnderRecoveryValue);
        
        
    }
    
    /*
     * Method to return all the PersonnelBudgetDetailBeans so far existing
     * in the Table of PersonnelBudgetForm
     *@return CoeusVector returns the all PersonnelBudgetBeans added to
     * vecBudgetPersonnelDetailsBean CoeusVector
     */
    
    /** Return Vector of all PersonnelBudgetDetailsBeans
     * @return PersonnelBudgetDetailsBeans CoeusVector
     */
    public CoeusVector getAllPersonnelBudgetBeans() {
        return vecBudgetPersonnelDetailsBean;
    }
    
    /**
     * Query the basewindow for the exisiting BudgetPersonnelBeans
     *@param key String ProposalNumber plur Version Number of the Budget
     *@return CoeusVector CoeusVector containing the Beans queried using QueryEngine
     */
    
    public CoeusVector getBaseWindowBeans(String key) {
        CoeusVector vecExistingBeansBaseWin = null;
        try {
            
            BudgetPersonnelDetailsBean budgetPersonnelDetailsBean =  new BudgetPersonnelDetailsBean();
            budgetPersonnelDetailsBean.setProposalNumber(budgetDetailBean.getProposalNumber());
            budgetPersonnelDetailsBean.setVersionNumber(budgetDetailBean.getVersionNumber());
            budgetPersonnelDetailsBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
            budgetPersonnelDetailsBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
            
            Equals equalsActype = new Equals(ACTYPE,null);
            NotEquals notequalsActype = new NotEquals(ACTYPE,TypeConstants.DELETE_RECORD);
            Or actypeBoth = new Or(equalsActype,notequalsActype);
            
            Equals equalsBudgetPeriod = new Equals("budgetPeriod",new Integer(budgetDetailBean.getBudgetPeriod()));
            Equals equalsLineItem = new Equals("lineItemNumber",new Integer(budgetDetailBean.getLineItemNumber()));
            
            And thisPeriodLineItem = new And(equalsBudgetPeriod,equalsLineItem);
            And matchBoth = new And(thisPeriodLineItem,actypeBoth);
            
            vecExistingBeansBaseWin = queryEngine.executeQuery(key,budgetPersonnelDetailsBean.getClass(),matchBoth);
            
        } catch(CoeusException coeusException) {
            coeusException.getMessage();
        }
        return vecExistingBeansBaseWin;
    }
    
    /**
     * Saves the Form Data.
     */
    
    public void saveFormData() {
        try {
            
            if(vecBudgetPersonnelDetailsBean == null) {
                return;
            }
            
            //Deleting all the Each PersonnelBudgetDetails Line Item From the Base
            
            for (int deleteIndex=0;deleteIndex< vecDeletedPersonnelDetailsBean.size();deleteIndex++) {
                BudgetPersonnelDetailsBean deletedPersonnelDetailsBean = (BudgetPersonnelDetailsBean) vecDeletedPersonnelDetailsBean.get(deleteIndex);
                deletedPersonnelDetailsBean.setAcType(TypeConstants.DELETE_RECORD);
                queryEngine.delete(queryKey,deletedPersonnelDetailsBean);
                String deletedPersonNumber = deletedPersonnelDetailsBean.getPersonNumber()+"";
                CoeusVector vecPersonnelBudgetCalAmtBean = (CoeusVector) hashTabALLPersonnelCalAmountsBean.get(deletedPersonNumber);
                // Added for COEUSQA-2510 Error saving budget - DW_UPDATE_BUDGET_PER_CAL_AMTS - Start
                if(vecPersonnelBudgetCalAmtBean !=null){
                for (int subIndex=0;subIndex < vecPersonnelBudgetCalAmtBean.size();subIndex++) {
                    BudgetPersonnelCalAmountsBean budgetPersonnelCalAmountsBean = (BudgetPersonnelCalAmountsBean) vecPersonnelBudgetCalAmtBean.get(subIndex);
                    budgetPersonnelCalAmountsBean.setAcType(TypeConstants.DELETE_RECORD);
                    queryEngine.delete(queryKey,budgetPersonnelCalAmountsBean);
                }
                } 
                // Added for COEUSQA-2510 - End
            }
            
            for (int index=0;index < vecBudgetPersonnelDetailsBean.size();index++) {
                
                BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean) vecBudgetPersonnelDetailsBean.get(index);
                //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
                //budgetPersonnelDetailsBean.setPeriodType(getPeriodType(budgetPersonnelDetailsBean.getPeriodType()));
                String periodType = getPeriodType(budgetPersonnelDetailsBean.getPeriodType());
                if(periodType!=null && periodType.length()>0){
                    budgetPersonnelDetailsBean.setPeriodType(periodType);
                }else{
                    budgetPersonnelDetailsBean.setPeriodType(budgetPersonnelDetailsBean.getPeriodType());
                }
                //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
                budgetPersonnelDetailsBean.setOnOffCampusFlag(budgetDetailBean.isOnOffCampusFlag());
                
                budgetPersonnelDetailsBean.setIndirectCost(budgetDetailBean.getIndirectCost());
                
                
                Equals equalsActype = new Equals(ACTYPE,null);
                Equals equalsPersonNumber = new Equals(PERSONNUMBER,new Integer(budgetPersonnelDetailsBean.getPersonNumber()));
                NotEquals notequalsActype = new NotEquals(ACTYPE,TypeConstants.DELETE_RECORD);
                Or actypeBoth = new Or(equalsActype,notequalsActype);
                
                Equals equalsBudgetPeriod = new Equals("budgetPeriod",new Integer(budgetPersonnelDetailsBean.getBudgetPeriod()));
                Equals equalsLineItem = new Equals("lineItemNumber",new Integer(budgetPersonnelDetailsBean.getLineItemNumber()));
                And thisPeriodLineItem = new And(equalsBudgetPeriod,equalsLineItem);
                And matchBoth = new And(thisPeriodLineItem,equalsPersonNumber);
                
                And bothANDpersonNumber = new And(actypeBoth,matchBoth);
                
                CoeusVector vecExistingBeansBaseWin = queryEngine.executeQuery(queryKey,budgetPersonnelDetailsBean.getClass(),bothANDpersonNumber);
                
                //if start vecExistingBeansBaseWin != null
                if(vecExistingBeansBaseWin != null && vecExistingBeansBaseWin.size() > 0) {
                    
                    //if start
                    if(budgetPersonnelDetailsBean.getAcType()!= null &&  budgetPersonnelDetailsBean.getAcType().equalsIgnoreCase(TypeConstants.UPDATE_RECORD)) {
                        
                        budgetPersonnelDetailsBean.setAcType(TypeConstants.UPDATE_RECORD);
                        queryEngine.update(queryKey,budgetPersonnelDetailsBean);
                        
                        String budgetPersonNumber = budgetPersonnelDetailsBean.getPersonNumber()+"";
                        //Update the CalAmount for this PersonnelBudgetDetailBean
                        CoeusVector vecPersonnelBudgetCalAmtBean = (CoeusVector) hashTabALLPersonnelCalAmountsBean.get(budgetPersonNumber);
                        for (int subIndex=0;subIndex < vecPersonnelBudgetCalAmtBean.size();subIndex++) {
                            BudgetPersonnelCalAmountsBean budgetPersonnelCalAmountsBean = (BudgetPersonnelCalAmountsBean) vecPersonnelBudgetCalAmtBean.get(subIndex);
                            budgetPersonnelCalAmountsBean.setAcType(TypeConstants.UPDATE_RECORD);
                            queryEngine.update(queryKey,budgetPersonnelCalAmountsBean);
                            
                        }
                        
                    }//if end
                    
                    
                }//if start vecExistingBeansBaseWin != null
                
                
                
                //INSERT INTO BASEWINDOW IF NOT there
                //Code commented and modified for Case#3652 - Adding two tabs to budget
                //if(vecExistingBeansBaseWin.size() <= 1) {
                if(vecExistingBeansBaseWin.size() < 1) {
                    budgetPersonnelDetailsBean.setAcType(TypeConstants.INSERT_RECORD);
                    queryEngine.insert(queryKey, budgetPersonnelDetailsBean);
                    String insertPersonNumber = budgetPersonnelDetailsBean.getPersonNumber()+"";
                    //INSERT the CalAmount for this PersonnelBudgetDetailBean
                    CoeusVector vecPersonnelBudgetCalAmtBean = (CoeusVector) hashTabALLPersonnelCalAmountsBean.get(insertPersonNumber);
                    //Code added for Case#3652 - Adding two tabs to budget
                    vecPersonnelBudgetCalAmtBean = (vecPersonnelBudgetCalAmtBean == null) ? new CoeusVector() : vecPersonnelBudgetCalAmtBean;
                    for (int subIndex=0;subIndex < vecPersonnelBudgetCalAmtBean.size();subIndex++) {
                        BudgetPersonnelCalAmountsBean budgetPersonnelCalAmountsBean = (BudgetPersonnelCalAmountsBean) vecPersonnelBudgetCalAmtBean.get(subIndex);
                        budgetPersonnelCalAmountsBean.setAcType(TypeConstants.INSERT_RECORD);
                        queryEngine.insert(queryKey, budgetPersonnelCalAmountsBean);
                    }
                    
                }
                
                
            }
            
            
            
            
            
            if(getTableInstance().getRowCount() > 0) {
                budgetDetailBean.setLineItemCost(Double.parseDouble(personnelBudgetDetailsForm.txtCost.getValue()));
                budgetDetailBean.setCostSharingAmount(Double.parseDouble(personnelBudgetDetailsForm.txtCostShare.getValue()));
                budgetDetailBean.setUnderRecoveryAmount(Double.parseDouble(personnelBudgetDetailsForm.txtUnderRecovery.getValue()));
//                budgetDetailBean.setQuantity(Integer.parseInt(personnelBudgetDetailsForm.txtQuantity.getText()));
                budgetDetailBean.setQuantity(Double.parseDouble(personnelBudgetDetailsForm.txtQuantity.getText()));
            } else {
                budgetDetailBean.setLineItemCost(0);
                budgetDetailBean.setCostSharingAmount(0);
                budgetDetailBean.setUnderRecoveryAmount(0);
                budgetDetailBean.setQuantity(0);
                budgetDetailBean.setDirectCost(0);
                budgetDetailBean.setIndirectCost(0);
                budgetDetailBean.setTotalCostSharing(0);
            }
            
            queryEngine.update(queryKey,budgetDetailBean);
            
            // Modified for COEUSDEV-1212 :  Proposal Versions $ and Budget summary $ totals show a single year rather than multi-year total - Start
            // All the periods will be calculated
//            calculate(queryKey, budgetDetailBean.getBudgetPeriod());
            calculate(queryKey, -1);
            // Modified for COEUSDEV-1212 :  Proposal Versions $ and Budget summary $ totals show a single year rather than multi-year total - End
            
            
            BeanEvent beanEvent = new BeanEvent();
            beanEvent.setBean(budgetDetailBean);
            beanEvent.setSource(this);
            fireBeanUpdated(beanEvent);
            
        }catch(Exception e){
            e.getMessage();
        }
        
        
    }
    
    /** Method to check Percent Charged is greater than equals Percent Effort
     * @return if <true>  Percent Charged is laess than equals Percent Effort
     */
    
    public boolean validatePerEntry() {
        
        /** Modified chandra to get the rowSelectionInterval. Also to stop the 
         *popping error messages continously. Bug id #
         */
        
//        int rowCount = getTableInstance().getRowCount();
//        int selectedRow = getTableInstance().getSelectedRow();
//        if(selectedRow == -1 || rowCount==-1) {
//            return false;
//        }
        boolean isvalidatePerEntry = true;
        
        if(vecBudgetPersonnelDetailsBean == null) {
            return isvalidatePerEntry;
        }
        
        int size = vecBudgetPersonnelDetailsBean.size();
        for (int index=0;index <size;index++) {
            BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean ) vecBudgetPersonnelDetailsBean.get(index);
            double percentCharged = budgetPersonnelDetailsBean.getPercentCharged();
            double percentEffort = budgetPersonnelDetailsBean.getPercentEffort();
            
            if(percentEffort < percentCharged) {
                CoeusOptionPane.showErrorDialog(
                coeusMessageResources.parseMessageKey(INVALID_PER_EFFORT_CHARGED));
                getTableInstance().setRowSelectionInterval(index,index);
                isvalidatePerEntry = false;
                break;
            }
        }
        
        return isvalidatePerEntry;
    }
    
    /** Method to check validate Percentage Effort and Charge Entry
     * @return if <true> Percent Entry  is less than Maximum value defined by
     *                          MAX_PER_CHARGE_ENTRY and MAX_PER_EFFORT_ENTRY variable
     */
    
    public boolean validatePerValueEntry() {
        
        for (int index=0;index <vecBudgetPersonnelDetailsBean.size();index++) {
            BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean ) vecBudgetPersonnelDetailsBean.get(index);
            double percentCharged = budgetPersonnelDetailsBean.getPercentCharged();
            double percentEffort = budgetPersonnelDetailsBean.getPercentEffort();
            
            if(percentCharged > MAX_PER_CHARGE_ENTRY) {
                CoeusOptionPane.showErrorDialog(
                coeusMessageResources.parseMessageKey(INVALID_PER_CHARGE_ENTRY));
                return false;
            }
            
            if(percentEffort > MAX_PER_EFFORT_ENTRY) {
                CoeusOptionPane.showErrorDialog(
                coeusMessageResources.parseMessageKey(INVALID_PER_EFFORT_ENTRY));
                return false;
            }
            
        }
        
        return true;
    }
    
    /** Method to check Duplicate Person Entry
     * @return if <true> Duplicate Person have been added to Table
     */
    public boolean duplicatePersonEntry() {
        
        int rowCount = getTableInstance().getRowCount();
        int selectedRow = getTableInstance().getSelectedRow();
        if(selectedRow == -1 || rowCount==-1) {
            return false;
        }
        
        BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = null;
        
        
        for (int index=0;index <vecBudgetPersonnelDetailsBean.size();index++) {
            
            if(vecBudgetPersonnelDetailsBean != null && vecBudgetPersonnelDetailsBean.size() > 0) {
                budgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean ) vecBudgetPersonnelDetailsBean.get(index);
            }
            
            //Code commented and modified for Case#3652 - Adding two tabs to budget
//            Equals jobCodeEquals = new Equals("jobCode", budgetPersonnelDetailsBean.getJobCode());
            Equals personIdEquals = new Equals("personId", budgetPersonnelDetailsBean.getPersonId());
            Equals effDateEquals = new Equals("startDate", budgetPersonnelDetailsBean.getStartDate());
            Equals nameEquals = new Equals("fullName", budgetPersonnelDetailsBean.getFullName());
            //Code commented and modified for Case#3652 - Adding two tabs to budget
//            And jobAndDate = new And(jobCodeEquals, effDateEquals);
            And jobAndDate = new And(personIdEquals, effDateEquals);            
            And jobAndDateAndName = new And(jobAndDate, nameEquals);
            CoeusVector coeusVector = vecBudgetPersonnelDetailsBean.filter(jobAndDateAndName);
            if(coeusVector.size()==-1) {
                return false;
            }
            if(coeusVector!=null && coeusVector.size() > 1) {
                CoeusOptionPane.showErrorDialog("Duplicate entries for "+budgetPersonnelDetailsBean.getFullName()+" with the same start date");
                getTableInstance().setRowSelectionInterval(selectedRow, selectedRow);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Defining the validate method of Controller interface
     * for validation check on the UI form
     * @throws CoeusUIException throws CoeusUIException
     * @return if <true> validation success
     */
    
    public boolean validate() throws CoeusUIException {
        boolean isPercentValidated = false;
        boolean isDuplicatePersonEntry = false;
        boolean validatePerValueEntry = true;
        
        if(vecBudgetPersonnelDetailsBean == null || vecBudgetPersonnelDetailsBean.size() < 1) {
            return true;
        }
        
        
        getTableInstance().getTableCellEditor().stopCellEditing();
        
        isPercentValidated = validatePerEntry();
        if(!isPercentValidated) {
            return false;
        }
        
        
        isDuplicatePersonEntry = duplicatePersonEntry();
        validatePerValueEntry = validatePerValueEntry();
        if(isPercentValidated) {
            if(validatePerValueEntry ) {
                if(!isDuplicatePersonEntry) {
                    return true;
                }
                else
                    return false;
            } else
                return false;
            
        }
        
        return false;
    }
    
    
    /**
     * to enable the text field baed on the functiontype
     */
    
    public void formatFields() {
        
        personnelBudgetDetailsForm.txtCostElmCode.setEditable(false);
        personnelBudgetDetailsForm.txtCostElmDescription.setEditable(false);
        personnelBudgetDetailsForm.txtStartDate.setEditable(false);
        personnelBudgetDetailsForm.txtEndDate.setEditable(false);
        personnelBudgetDetailsForm.txtDescription.setEditable(false);
        personnelBudgetDetailsForm.txtCost.setEditable(false);
        personnelBudgetDetailsForm.txtCostShare.setEditable(false);
        personnelBudgetDetailsForm.txtUnderRecovery.setEditable(false);
        // added by chandra - bug fix #902 - Start
        personnelBudgetDetailsForm.pnlLineItemCalAmount.tblCalculatedAmounts.setEnabled(false);
        // added by chandra - bug fix #902 - End
        //For case #1638 start 1
        /*personnelBudgetDetailsForm.txtCostElmCode.setBackground(Color.WHITE);
        personnelBudgetDetailsForm.txtCostElmDescription.setBackground(Color.WHITE);
        personnelBudgetDetailsForm.txtStartDate.setBackground(Color.WHITE);
        personnelBudgetDetailsForm.txtEndDate.setBackground(Color.WHITE);
        personnelBudgetDetailsForm.txtDescription.setBackground(Color.WHITE);*/
        //For case #1638 end 1
        personnelBudgetDetailsForm.txtCost.setBackground(Color.WHITE);
        personnelBudgetDetailsForm.txtCostShare.setBackground(Color.WHITE);
        personnelBudgetDetailsForm.txtUnderRecovery.setBackground(Color.WHITE);
        //For case #1638 start 2
        Color backGroundColor = (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background");
        //For case #1638 end 2
        //if the Form is open in display mode
        if(getFunctionType() == DISPLAY_MODE) {
            //For case #1638 start 3
            //Color backGroundColor = (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background");
            //For case #1638 end 3
                
            personnelBudgetDetailsForm.btnCalculate.setEnabled(false);
            personnelBudgetDetailsForm.btnDelete.setEnabled(false);
            personnelBudgetDetailsForm.btnAdd.setEnabled(false);
            personnelBudgetDetailsForm.btnOk.setEnabled(false);
            personnelBudgetDetailsForm.txtQuantity.setEditable(false);
            
            personnelBudgetDetailsForm.txtCostElmCode.setBackground(backGroundColor);
            personnelBudgetDetailsForm.txtCostElmDescription.setBackground(backGroundColor);
            personnelBudgetDetailsForm.txtStartDate.setBackground(backGroundColor);
            personnelBudgetDetailsForm.txtEndDate.setBackground(backGroundColor);
            personnelBudgetDetailsForm.txtDescription.setBackground(backGroundColor);
            personnelBudgetDetailsForm.txtCost.setBackground(backGroundColor);
            personnelBudgetDetailsForm.txtCostShare.setBackground(backGroundColor);
            personnelBudgetDetailsForm.txtUnderRecovery.setBackground(backGroundColor);
            personnelBudgetDetailsForm.txtQuantity.setBackground(backGroundColor);
            personnelBudgetDetailsForm.btnCancel.requestFocus();
            
            
        } else {
            //For case #1638 start 4
            personnelBudgetDetailsForm.txtCostElmCode.setBackground(backGroundColor);
            personnelBudgetDetailsForm.txtCostElmDescription.setBackground(backGroundColor);
            personnelBudgetDetailsForm.txtStartDate.setBackground(backGroundColor);
            personnelBudgetDetailsForm.txtEndDate.setBackground(backGroundColor);
            personnelBudgetDetailsForm.txtDescription.setBackground(backGroundColor);
            //For case #1638 end 4
            personnelBudgetDetailsForm.btnCalculate.setEnabled(true);
            personnelBudgetDetailsForm.btnDelete.setEnabled(true);
            personnelBudgetDetailsForm.btnAdd.setEnabled(true);
            personnelBudgetDetailsForm.btnOk.setEnabled(true);
            getTableInstance().setBackground(Color.WHITE);
            
           
        }
    }
    
    /** To generate the Maximun PersonNumber for the New BudgetPerson Added
     * @return int Maximun Person Number that is allocated to each Person added to PersonnelBudgetDetail LineItem
     * @param vecBudgetPersonnelDetailBeans Vector of BudgetPersonnelDetail Beans
     */
    
    public int getMaxPersonNumber(CoeusVector vecBudgetPersonnelDetailBeans) {
        if(vecBudgetPersonnelDetailBeans !=  null &&
        vecBudgetPersonnelDetailBeans.size() > 0) {
            vecBudgetPersonnelDetailBeans.sort(PERSONNUMBER,false);
            return  ((BudgetPersonnelDetailsBean) vecBudgetPersonnelDetailBeans.get(0)).getPersonNumber();
        }
        return 0;
    }
    
    /** Method to get BudgetPersonnelCalAmountsBean Vector for each BudgetPersonnelDetailsBean
     * and for a new Person added it takes from budgetDetailCalAmountsBean and
     * returns the vector of beans with set Cost and Cost sharing set to zero initially
     * @param bpdBean bpdBean instance of BudgetPersonnelDetailsBean
     * @param bdBean Object bdBean  instance of BudgetDetailBean
     * @return Vector of CalAmount Beans
     */
    
    public CoeusVector getBudgetLineItemCalAmount(Object bpdBean,Object bdBean) {
        CoeusVector vecBudgetLineItemCalAmounts = null;
        CoeusVector newVecPersonnelCalAmountsBean =  new CoeusVector();
        try{
            
            if(bpdBean != null && (bpdBean instanceof BudgetPersonnelDetailsBean) ) {
                BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean) bpdBean;
                queryKey = budgetPersonnelDetailsBean.getProposalNumber() + budgetPersonnelDetailsBean.getVersionNumber();
                budgetPersonnelCalAmountsBean = new BudgetPersonnelCalAmountsBean();
                budgetPersonnelCalAmountsBean.setProposalNumber(budgetPersonnelDetailsBean.getProposalNumber());
                budgetPersonnelCalAmountsBean.setVersionNumber(budgetPersonnelDetailsBean.getVersionNumber());
                budgetPersonnelCalAmountsBean.setBudgetPeriod(budgetPersonnelDetailsBean.getBudgetPeriod());
                budgetPersonnelCalAmountsBean.setLineItemNumber(budgetPersonnelDetailsBean.getLineItemNumber());
                budgetPersonnelCalAmountsBean.setPersonNumber(budgetPersonnelDetailsBean.getPersonNumber());
                
                Equals equalsActype = new Equals(ACTYPE,null);
                NotEquals notequalsActype = new NotEquals(ACTYPE,TypeConstants.DELETE_RECORD);
                Or actypeBoth = new Or(equalsActype,notequalsActype);
                //personNumber
                Equals equalsBudgetPeriod = new Equals("budgetPeriod", new Integer(budgetPersonnelDetailsBean.getBudgetPeriod()));
                Equals equalsPersonNumber = new Equals("personNumber", new Integer(budgetPersonnelDetailsBean.getPersonNumber()));
                Equals equalsLineItem = new Equals("lineItemNumber", new Integer(budgetPersonnelDetailsBean.getLineItemNumber()));
                
                And andPeriodLineItem = new And(equalsBudgetPeriod,equalsLineItem);
                And andPersonNumber = new And(andPeriodLineItem,equalsPersonNumber);
                
                And matchBoth = new And(andPersonNumber, actypeBoth);
                
                vecBudgetLineItemCalAmounts = queryEngine.executeQuery(queryKey, budgetPersonnelCalAmountsBean.getClass(), matchBoth);
                newVecPersonnelCalAmountsBean = vecBudgetLineItemCalAmounts;
            }
            
            int personNumber = 1;
            
            if( vecBudgetLineItemCalAmounts != null && vecBudgetLineItemCalAmounts.size() > 0);
            else {
                if(bdBean != null && (bdBean instanceof BudgetDetailBean) ) {
                    BudgetDetailBean budgetDetailBean = (BudgetDetailBean) bdBean;
                    queryKey = budgetDetailBean.getProposalNumber() + budgetDetailBean.getVersionNumber();
                    BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
                    budgetDetailCalAmountsBean.setProposalNumber(budgetDetailBean.getProposalNumber());
                    budgetDetailCalAmountsBean.setVersionNumber(budgetDetailBean.getVersionNumber());
                    budgetDetailCalAmountsBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
                    budgetDetailCalAmountsBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
                    
                    Equals equalsActype = new Equals(ACTYPE,null);
                    NotEquals notequalsActype = new NotEquals(ACTYPE,TypeConstants.DELETE_RECORD);
                    Or actypeBoth = new Or(equalsActype,notequalsActype);
                    Equals equalsLineItem = new Equals("lineItemNumber", new Integer(budgetDetailCalAmountsBean.getLineItemNumber()));
                    Equals equalsBudgetPeriod = new Equals("budgetPeriod", new Integer(budgetDetailCalAmountsBean.getBudgetPeriod()));
                    And equalsBoth = new And(equalsLineItem, equalsBudgetPeriod);
                    And matchBoth = new And(equalsBoth, actypeBoth);
                    vecBudgetLineItemCalAmounts = queryEngine.executeQuery(queryKey, budgetDetailCalAmountsBean.getClass(),matchBoth);
                    
                    if(vecBudgetPersonnelDetailsBean !=  null &&
                    vecBudgetPersonnelDetailsBean.size() > 0) {
                        vecBudgetPersonnelDetailsBean.sort(PERSONNUMBER,false);
                        personNumber = ((BudgetPersonnelDetailsBean) vecBudgetPersonnelDetailsBean.get(0)).getPersonNumber();
                        
                    }
                    vecBudgetPersonnelDetailsBean.sort(PERSONNUMBER,true);
                    
                    
                    for (int loopIndex =0; loopIndex < vecBudgetLineItemCalAmounts.size(); loopIndex ++ ) {
                        
                        budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean ) vecBudgetLineItemCalAmounts.elementAt(loopIndex);
                        
                        BudgetPersonnelCalAmountsBean budgetPersonnelCalAmountsBean = new BudgetPersonnelCalAmountsBean();
                        budgetPersonnelCalAmountsBean.setProposalNumber(budgetDetailCalAmountsBean.getProposalNumber());
                        budgetPersonnelCalAmountsBean.setVersionNumber(budgetDetailCalAmountsBean.getVersionNumber());
                        budgetPersonnelCalAmountsBean.setBudgetPeriod(budgetDetailCalAmountsBean.getBudgetPeriod());
                        budgetPersonnelCalAmountsBean.setLineItemNumber(budgetDetailCalAmountsBean.getLineItemNumber());
                        
                        budgetPersonnelCalAmountsBean.setRateClassCode(budgetDetailCalAmountsBean.getRateClassCode());
                        budgetPersonnelCalAmountsBean.setRateClassDescription(budgetDetailCalAmountsBean.getRateClassDescription());
                        budgetPersonnelCalAmountsBean.setRateTypeCode(budgetDetailCalAmountsBean.getRateTypeCode());
                        budgetPersonnelCalAmountsBean.setRateTypeDescription(budgetDetailCalAmountsBean.getRateTypeDescription());
                        budgetPersonnelCalAmountsBean.setApplyRateFlag(budgetDetailCalAmountsBean.isApplyRateFlag());
                        budgetPersonnelCalAmountsBean.setRateClassType(budgetDetailCalAmountsBean.getRateClassType());
                        
                        budgetPersonnelCalAmountsBean.setPersonNumber(personNumber);
                        budgetPersonnelCalAmountsBean.setCalculatedCost(0);
                        budgetPersonnelCalAmountsBean.setCalculatedCostSharing(0);
                        budgetPersonnelCalAmountsBean.setUpdateTimestamp(budgetDetailCalAmountsBean.getUpdateTimestamp());
                        budgetPersonnelCalAmountsBean.setUpdateUser(budgetDetailCalAmountsBean.getUpdateUser());
                        budgetPersonnelCalAmountsBean.setAcType(TypeConstants.INSERT_RECORD);
                        newVecPersonnelCalAmountsBean.add(budgetPersonnelCalAmountsBean);
                        
                    }
                    
                    vecBudgetLineItemCalAmounts = newVecPersonnelCalAmountsBean;
                    
                }
                if(!hashTabALLPersonnelCalAmountsBean.containsKey(personNumber+"")) {
                    hashTabALLPersonnelCalAmountsBean.put(personNumber+"",(Object) newVecPersonnelCalAmountsBean);
                }
            }
            
        }catch (CoeusException coeusException) {
            coeusException.getMessage();
        }
        
        return vecBudgetLineItemCalAmounts;
    }
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data to set to the form
     */
    
    public void setFormData(Object data) {
        
        budgetDetailBean = (BudgetDetailBean) data;
        if(budgetDetailBean == null) {
            return ;
        }
        
        
        try {
            setSaveRequired(false);
            getTableInstance().setControllerInstance(this);
            personnelBudgetDetailsForm.txtCostElmCode.setText(budgetDetailBean.getCostElement());
            personnelBudgetDetailsForm.txtCostElmDescription.setText(budgetDetailBean.getCostElementDescription());
            
            personnelBudgetDetailsForm.txtStartDate.setText(
            dateUtils.formatDate(budgetDetailBean.getLineItemStartDate().toString(), DISPLAY_DATE_FORMAT));
            personnelBudgetDetailsForm.txtEndDate.setText(
            dateUtils.formatDate(budgetDetailBean.getLineItemEndDate().toString(), DISPLAY_DATE_FORMAT));
            
            personnelBudgetDetailsForm.txtDescription.setText(budgetDetailBean.getLineItemDescription());
            personnelBudgetDetailsForm.txtCost.setText(budgetDetailBean.getLineItemCost()+"");
            personnelBudgetDetailsForm.txtCostShare.setText(budgetDetailBean.getCostSharingAmount()+"");
            personnelBudgetDetailsForm.txtUnderRecovery.setText(budgetDetailBean.getUnderRecoveryAmount()+"");
            personnelBudgetDetailsForm.txtQuantity.setText(budgetDetailBean.getQuantity()+"");
            
            budgetInfoBean = new BudgetInfoBean();
            budgetInfoBean.setProposalNumber(budgetDetailBean.getProposalNumber());
            budgetInfoBean.setVersionNumber(budgetDetailBean.getVersionNumber());
            queryKey  = (budgetDetailBean.getProposalNumber()+budgetDetailBean.getVersionNumber());
            CoeusVector coeusVector = queryEngine.executeQuery(queryKey,budgetInfoBean);
            
            if(coeusVector.size() > 0) {
                budgetInfoBean = (BudgetInfoBean) coeusVector.get(0);
            }
            
        }catch (Exception e) {
            e.getMessage();
        }
        
        
        queryKey = budgetDetailBean.getProposalNumber()+budgetDetailBean.getVersionNumber();
        budgetPersDetailsBean =  new BudgetPersonnelDetailsBean();
        budgetPersDetailsBean.setProposalNumber(budgetDetailBean.getProposalNumber());
        budgetPersDetailsBean.setVersionNumber(budgetDetailBean.getVersionNumber());
        budgetPersDetailsBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
        budgetPersDetailsBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
        
        
        try {
            
            vecBudgetPersonnelDetailsBean = getBaseWindowBeans(queryKey);
            //initialise the Vector which will hold Deleted Item
            vecDeletedPersonnelDetailsBean = new CoeusVector();
            vecDeletedPersonnelCalAmountsBean = new CoeusVector();
            hashTabALLPersonnelCalAmountsBean = new Hashtable();
            
            
            
            if(vecBudgetPersonnelDetailsBean != null && vecBudgetPersonnelDetailsBean.size() > 0) {
                setSaveRequired(false);
                
                
                // here to set the data to the table
                ( (PersonnelBudgetDetailsForm) getControlledUI()).getPersonnelBudgetDetailTable().setTableData((Object) vecBudgetPersonnelDetailsBean);
                getTableInstance().requestFocus();
                getTableInstance().setRowSelectionInterval(0, 0);
                
                //Bug Fix:1571 Start 2 
                //getTableInstance().editCellAt(0,2);
                //getTableInstance().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                //Bug Fix:1571 End 2 
                
                // get the Vector of CalAmopunts to set the data to lineitemcalamount table
                vecBudgetPersonnelCalAmountsBean = getBudgetLineItemCalAmount(vecBudgetPersonnelDetailsBean.get(0),budgetDetailBean);
                
                for(int index=0;index < vecBudgetPersonnelDetailsBean.size();index++) {
                    CoeusVector vecCalAmountsBean =  getBudgetLineItemCalAmount(vecBudgetPersonnelDetailsBean.get(index),budgetDetailBean);
                    String personNum = ((BudgetPersonnelDetailsBean) vecBudgetPersonnelDetailsBean.get(index)).getPersonNumber()+"";
                    if(!hashTabALLPersonnelCalAmountsBean.containsKey(personNum)) {
                        hashTabALLPersonnelCalAmountsBean.put(personNum,vecCalAmountsBean);
                    }
                }
                
                personnelBudgetDetailsForm.pnlLineItemCalAmount.setFormData(vecBudgetPersonnelCalAmountsBean);
                personnelBudgetDetailsForm.pnlLineItemCalAmount.tblCalculatedAmounts.setEnabled(false);
                
                
            } else {
                ( (PersonnelBudgetDetailsForm) getControlledUI()).getPersonnelBudgetDetailTable().setTableData((Object) new CoeusVector() );
                personnelBudgetDetailsForm.pnlLineItemCalAmount.setFormData((Object)new CoeusVector());
            }
            
        }catch(Exception exp) {
            exp.getMessage();
        }
        
        
    }
    
    /**
     * Method to find the Number of BudgetPersonnel Quantity from the Vector containing all the
     * BudgetPersonnelDetailsBean added to the PersonnelBudgetDetailTable
     *@param vecPersonnelDetailsBean CoeusVector
     *@return String Quantity Value
     */
    
    public String getNumOfQuantity(CoeusVector vecPersonnelDetailsBean) {
        Hashtable persons = new Hashtable();
        
        int numberofQuantity =0;
        if(vecPersonnelDetailsBean != null && vecPersonnelDetailsBean.size() > 0) {
            for(int rowCount=0;rowCount < vecPersonnelDetailsBean.size();rowCount++) {
                BudgetPersonnelDetailsBean personnelDetailsBean = (BudgetPersonnelDetailsBean) vecPersonnelDetailsBean.get(rowCount);
                String personId = personnelDetailsBean.getPersonId();
                
                if(!persons.contains(personId)) {
                    persons.put(personId,personId);
                    numberofQuantity++;
                }
            }
        }
        
        return numberofQuantity+"";
        
    }
    
    
    /** Method for invoking PersonnelBudgetLineItemBean on Click of Detail Button
     * @param rowSelected int selected Row index
     */
    
    public void callPersonnelBudgetLineItemBean(int rowSelected ) {
        
        
        ((AbstractCellEditor) getTableInstance().getTableCellEditor()).stopCellEditing();
        
        //System.out.println("callPersonnelBudgetLineItemBean");
        int selectedRow = getTableInstance().getSelectedRow();
        
        if(rowSelected > -1) {
            selectedRow = rowSelected ;
        }
        
        //        if(selectedRow < 0) {
        //            int rowSelected = getTableInstance().getSelectedRow();
        //            if(rowSelected < 0) {
        //                return;
        //            }
        //            else
        //                selectedRow = rowSelected;
        //        }
        
        if(selectedRow > -1) {
            
            if(vecBudgetPersonnelDetailsBean != null && vecBudgetPersonnelDetailsBean.size() > 0){
                
                BudgetPersonnelDetailsBean personnelDetailsBean = (BudgetPersonnelDetailsBean) vecBudgetPersonnelDetailsBean.get(selectedRow);
                //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
                //personnelDetailsBean.setPeriodType(getPeriodType(personnelDetailsBean.getPeriodType()));
                String periodType = getPeriodType(personnelDetailsBean.getPeriodType());
                if(periodType!=null && periodType.length()>0){
                    personnelDetailsBean.setPeriodType(periodType);
                }else{
                    personnelDetailsBean.setPeriodType(personnelDetailsBean.getPeriodType());
                }
                //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
                // Added by chandra to Fix #1102 - 10-Sept-2004 - Start
                if(((PersonnelBudgetDetailsForm) getControlledUI()).getPersonnelBudgetDetailTable().isAmountChanged()){
                    double costSharing = personnelDetailsBean.getPercentEffort() - personnelDetailsBean.getPercentCharged();
                    personnelDetailsBean.setCostSharingPercent(costSharing);
                }// Added by chandra to Fix #1102 - 10-Sept-2004 - End
                
                personnelLineItemController = new PersonnelBudgetLineItemController(personnelDetailsBean);
                personnelLineItemController.setFormData(personnelDetailsBean);
                personnelLineItemController.setFunctionType(getFunctionType());
                //personnelLineItemController.setBudgetDetailBean(budgetDetailBean);
                vecBudgetPersonnelCalAmountsBean = getBudgetLineItemCalAmount(vecBudgetPersonnelDetailsBean.get(selectedRow),budgetDetailBean);
                personnelLineItemController.setCalAmountsData(vecBudgetPersonnelCalAmountsBean);
                personnelLineItemController.display();
                ((PersonnelBudgetDetailsForm) getControlledUI()).getPersonnelBudgetDetailTable().setAmountChanged(false);
            
            }
        }
    }
    
    /** To set the LineItem Calculated Amount for each Budget person in the Personnel Budget table
     * @param index the value to get the corresponding Bean from the Vector
     */
    
    public void setLineItemCalculatedAmount(int index) {
        int personNumber = ((BudgetPersonnelDetailsBean) vecBudgetPersonnelDetailsBean.get(index)).getPersonNumber();
        CoeusVector calAmountBean = (CoeusVector) hashTabALLPersonnelCalAmountsBean.get(personNumber+"");
        personnelBudgetDetailsForm.pnlLineItemCalAmount.setFormData(calAmountBean);
    }
    
    /** defined to returns the form data
     * @return returns the form data
     */
    
    public Object getFormData() {
        //here to get all the values from the Form
        return personnelBudgetDetailsForm;
    }
    
    /** returns the Component which is being controlled by this Controller.
     * @return Component which is being controlled by this Controller here
     *         it is personnelBudgetDetailsForm instance
     */
    
    public Component getControlledUI() {
        return personnelBudgetDetailsForm;
    }
    
    /**
     * registers GUI Components with event Listeners.
     */
    
    public void registerComponents() {
        //Bug Fix: 1571 Start 1
//        getTableInstance().addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent e) {
//                try {
//                    
//                   javax.swing.SwingUtilities.invokeLater( new Runnable() {
//                        public void run() {
//                            getTableInstance().dispatchEvent(new KeyEvent(
//                            getTableInstance(),KeyEvent.KEY_PRESSED,0,0,KeyEvent.VK_F2,
//                            KeyEvent.CHAR_UNDEFINED) );
//                        }
//                    });
//                    
//                } catch(Exception excep) {
//                    excep.getMessage();
//                }
//                
//            }
//        });
        //Bug Fix: 1571 End 1
        
        addBeanUpdatedListener(this, BudgetPersonnelDetailsBean.class);
        
        getTableInstance().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        getTableInstance().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        
        ( (PersonnelBudgetDetailsForm) getControlledUI()).btnOk.addActionListener(this);
        ( (PersonnelBudgetDetailsForm) getControlledUI()).btnCancel.addActionListener(this);
        ( (PersonnelBudgetDetailsForm) getControlledUI()).btnAdd.addActionListener(this);
        ( (PersonnelBudgetDetailsForm) getControlledUI()).btnDelete.addActionListener(this);
        ( (PersonnelBudgetDetailsForm) getControlledUI()).btnDetails.addActionListener(this);
        ( (PersonnelBudgetDetailsForm) getControlledUI()).btnCalculate.addActionListener(this);
        ( (PersonnelBudgetDetailsForm) getControlledUI()).btnPersons.addActionListener(this);
        
        
        // Travel all the components while pressing tab button
        java.awt.Component[] components = {
            ( (PersonnelBudgetDetailsForm) getControlledUI()).btnOk,
            ( (PersonnelBudgetDetailsForm) getControlledUI()).btnCancel,
            ( (PersonnelBudgetDetailsForm) getControlledUI()).btnAdd,
            ( (PersonnelBudgetDetailsForm) getControlledUI()).btnDelete,
            ( (PersonnelBudgetDetailsForm) getControlledUI()).btnDetails,
            ( (PersonnelBudgetDetailsForm) getControlledUI()).btnCalculate,
            ( (PersonnelBudgetDetailsForm) getControlledUI()).btnPersons,
            ((JTable) getTableInstance())
        };
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        personnelBudgetDetailsForm.pnlActiobButtonPanel.setFocusTraversalPolicy(traversePolicy);
        personnelBudgetDetailsForm.pnlActiobButtonPanel.setFocusCycleRoot(true);
        
        ( (PersonnelBudgetDetailsForm) getControlledUI()).dlgPersonnelBudgetDetailsForm.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent ke){
                if(ke.getKeyCode() == KeyEvent.VK_ESCAPE){
                    
                    try {
                        getTableInstance().getTableCellEditor().stopCellEditing();
                        personnelBudgetDetailsForm.requestFocus();
                        if(!(budgetDetailBean.getQuantity() ==
//                        Integer.parseInt(personnelBudgetDetailsForm.txtQuantity.getText()))) {
                        Double.parseDouble(personnelBudgetDetailsForm.txtQuantity.getText()))) {
                            setSaveRequired(true);
                        }
                        
                        if(isSaveRequired()) {
                            checkForSaveChanges();
                        } else
                            close();
                        
                    }catch(Exception e) {
                        e.getMessage();
                    }
                }
            }
        });
        
        ((PersonnelBudgetDetailsForm) getControlledUI()).dlgPersonnelBudgetDetailsForm.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                try {
                    getTableInstance().getTableCellEditor().stopCellEditing();
                    if(isSaveRequired()) {
                        checkForSaveChanges();
                    } else
                        close();
                }catch (Exception exp ) {
                    exp.getMessage();
                }
            }
            
        });
        
        
        ((PersonnelBudgetDetailsForm) getControlledUI()).dlgPersonnelBudgetDetailsForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        
        ( (PersonnelBudgetDetailsForm) getControlledUI()).dlgPersonnelBudgetDetailsForm.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                try {
                    getTableInstance().getTableCellEditor().stopCellEditing();
                    
                    if(isSaveRequired()) {
                        
                        int option = JOptionPane.NO_OPTION;
                        option  = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(SAVE_CHANGES),
                        CoeusOptionPane.OPTION_YES_NO_CANCEL,
                        CoeusOptionPane.DEFAULT_CANCEL);
                        switch(option) {
                            case ( JOptionPane.YES_OPTION ):
                                try{
                                    
                                    if(validate()) {
                                        saveFormData();
                                        close();
                                    }
                                    
                                }catch(Exception e){
                                    e.getMessage();
                                    CoeusOptionPane.showErrorDialog(e.getMessage());
                                }
                                break;
                                
                            case ( JOptionPane.NO_OPTION ):
                                close();
                                break;
                            case ( JOptionPane.CANCEL_OPTION ) :
                                ( (PersonnelBudgetDetailsForm) getControlledUI()).btnCancel.requestFocus();
                                return ;
                        }
                    } else
                        close();
                    
                }catch(Exception e) {
                    e.getMessage();
                }
            }
        });
        
        
        //  =====================Table cell ListSelectionListener ==============================
        getTableInstance().getSelectionModel().addListSelectionListener(
        new TableListSelectionListener());
        
        
        //  =====================END Table cell ListSelectionListener ==============================
        
        //  =====================MouseListener for PersonnelBudgetLineItem ==============================
        getTableInstance().addMouseListener(new MouseAdapter() {
            
            public void mouseClicked(MouseEvent mouseEvent) {
                //Object source = mouseEvent.getSource();
                int selectedRow = getTableInstance().getSelectedRow();
                //System.out.println("Controller Mouse clicked");
                if(mouseEvent.getClickCount() == 2) {
                    callPersonnelBudgetLineItemBean(selectedRow);
                }
            }
            
        });
    }

    /**
     * Closing the Current Active Window
     */
    public void close() {
        ( (PersonnelBudgetDetailsForm) getControlledUI()).dlgPersonnelBudgetDetailsForm.setVisible(false);
    }
    
    
    /**
     * Displays the Form which is being controlled.
     */
    public void display() {
        formatFields();
        ( (PersonnelBudgetDetailsForm) getControlledUI()).dlgPersonnelBudgetDetailsForm.setVisible(true);
    }
    
    /**
     * display alert message
     *
     * @param mesg the message to be displayed
     */
    public void log(String mesg) {
        CoeusOptionPane.showErrorDialog(mesg);
    }
    
    
    /** To Update Bean from other part of the application
     * @param beanEvent beanEvent instance of BeanEvent
     */
    public void beanUpdated(BeanEvent beanEvent) {
        try{
            //BudgetPersonnelDetailsBean newBudgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean) beanEvent.getBean();
            int selectedRow = getTableInstance().getSelectedRow();
            if(vecBudgetPersonnelDetailsBean != null && vecBudgetPersonnelDetailsBean.size() > 0) {
                //BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean) vecBudgetPersonnelDetailsBean.get(selectedRow);
                (getTableInstance().getTableModel()).fireTableRowsUpdated(selectedRow,selectedRow);
                calculatePersonnelLineItem(null);

            }
        }catch (CoeusClientException coeusClientException){
            CoeusOptionPane.showDialog(coeusClientException);
            
        }
        
    }
    
    /** Added by chandra to fix #1102 and #1103- start Aug-9
     */
    public void cleanUp() {
        removeBeanUpdatedListener(this, BudgetPersonnelDetailsBean.class);
    }
    
    
    
    /**
     * Getter for property proposalHierarchyBean.
     * @return Value of property proposalHierarchyBean.
     */
    public edu.mit.coeus.propdev.bean.ProposalHierarchyBean getProposalHierarchyBean() {
        return proposalHierarchyBean;
    }
    
    /**
     * Setter for property proposalHierarchyBean.
     * @param proposalHierarchyBean New value of property proposalHierarchyBean.
     */
    public void setProposalHierarchyBean(edu.mit.coeus.propdev.bean.ProposalHierarchyBean proposalHierarchyBean) {
        this.proposalHierarchyBean = proposalHierarchyBean;
    }
    
    /**
     * Getter for property parentProposal.
     * @return Value of property parentProposal.
     */
    public boolean isParentProposal() {
        return parentProposal;
    }
    
    /**
     * Setter for property parentProposal.
     * @param parentProposal New value of property parentProposal.
     */
    public void setParentProposal(boolean parentProposal) {
        this.parentProposal = parentProposal;
    }
    
    // End Chandra 
    
    
    
    /*
     * TableListSelectionListener is the Table ListeSlection Listener for PersonnelBudgetDetailTable
     */
    class TableListSelectionListener implements javax.swing.event.ListSelectionListener{
        
        public void valueChanged(javax.swing.event.ListSelectionEvent listSelectionEvent) {
            int  selectedRow = 0;
            ListSelectionModel lsm =(ListSelectionModel)listSelectionEvent.getSource();
            
            try {
                //System.out.println("ListSelection Listener");

                ((AbstractCellEditor) getTableInstance().getTableCellEditor()).stopCellEditing();

                
                if(lsm.equals(getTableInstance().getSelectionModel())) {
                    
                    if(getTableInstance().getRowCount() > 0) {
                        
                        selectedRow = getTableInstance().getSelectedRow();
                        
                        if(selectedRow >-1) {
                            personnelBudgetDetailsForm.pnlLineItemCalAmount.setFormData(null);
                            int editingCol = getTableInstance().getSelectedColumn();
                            

                            getTableInstance().editCellAt(selectedRow,editingCol);

                            
                            try {
                                if(vecBudgetPersonnelDetailsBean != null && vecBudgetPersonnelDetailsBean.size() > 0 ) {
                                    
                                    int personNumber = ((BudgetPersonnelDetailsBean) vecBudgetPersonnelDetailsBean.get(selectedRow)).getPersonNumber();
                                    vecBudgetPersonnelCalAmountsBean = (CoeusVector) hashTabALLPersonnelCalAmountsBean.get(personNumber+"");
                                    if (vecBudgetPersonnelCalAmountsBean == null || vecBudgetPersonnelCalAmountsBean.size() == 0) {
                                        vecBudgetPersonnelCalAmountsBean = getBudgetLineItemCalAmount(vecBudgetPersonnelDetailsBean.get(selectedRow),budgetDetailBean);
                                    }
                                    
                                    //setLineItemCalculatedAmount(selectedRow);
                                    personnelBudgetDetailsForm.pnlLineItemCalAmount.setFormData(vecBudgetPersonnelCalAmountsBean);
                                    
                                } else {
                                    personnelBudgetDetailsForm.pnlLineItemCalAmount.setFormData(null);
                                    
                                }
                            }catch(Exception exp ) {
                                exp.getMessage();
                            }
                            //Bug Fix:1571 Start 9
                            //getTableInstance().setRowSelectionInterval(selectedRow, selectedRow);
                            //Bug Fix:1571 End 9
                        }
                        
                        
                    }
                    
                }
            }catch (Exception e){
                e.getMessage();
                // personnelBudgetDetailsForm.pnlLineItemCalAmount.setFormData(null);
                //getTableInstance().setTableData(new Vector());
            }
            
            
        }
        
    }//end of class
}//End Class







