/*
 * ProposalRateController.java
 *
 * Created on October 3, 2003, 3:04 PM
 */

/*
 * PMD check performed, and commented unused imports and variables on 01-MAR-2011
 * by Maharaja Palanichamy
 */
package edu.mit.coeus.budget.controller;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.AbstractAction;
import java.awt.*;
import java.awt.event.*;
//import java.awt.Cursor;
import java.util.Hashtable;
import java.util.Vector;

import edu.mit.coeus.budget.bean.InstituteLARatesBean;
import edu.mit.coeus.budget.controller.Controller;
import edu.mit.coeus.budget.gui.ProposalRateForm;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
//import edu.mit.coeus.gui.CoeusAppletMDIForm;

import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.Or;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;

import edu.mit.coeus.budget.bean.InstituteRatesBean;
import edu.mit.coeus.budget.bean.RatesBean;
import edu.mit.coeus.budget.bean.ProposalRatesBean;
import edu.mit.coeus.budget.bean.ProposalLARatesBean;
import edu.mit.coeus.budget.gui.ProposalRateInstPanelBean;
import edu.mit.coeus.budget.gui.ProposalRateLAPanelBean;
import edu.mit.coeus.exception.CoeusClientException;
import java.util.HashMap;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  ranjeeva
 */

public class ProposalRateController  extends Controller implements TypeConstants,ActionListener {
    
    //holds Parant Component for which the Dialog is a modal
    java.awt.Component parent;
    //holds Porposal Number of Proposal
    private String proposalNumber = null;
    //holds Version Number of Proposal
    private int versionNumber = -1;
    //holds the Activity Code of the Proposal for which the Rates are applicable
    private int activityTypeCode = -1;
    //holds the Unit Number of the Proposal for which the LA Rates are applicable
    private String unitNumber;
    
    private java.sql.Date budgetStartDate;
    private java.sql.Date budgetEndDate;
    
    //the reference to the ProposalRate Form
    private ProposalRateForm proposalRateForm;
    
    // boolean variable to check whether a rate is modified
    private boolean isRateModified ;
    boolean modal;
    
    //Constants indiacting the action on Button
    private final int OK_ACTION_CODE = 0;
    private final int CANCEL_ACTION_CODE = 1;
    private final int RESET_ACTION_CODE = 2;
    private final int SYNC_ACTION_CODE = 3;
    private int ACTION_CODE = 1;
    private final char GET_MASTER_DATA_TO_SYNC = 'G';
    
    private final String SAVE_CHANGES = "budget_saveChanges_exceptionCode.1210"; //"Do you want to save changes?    ";
    private final String SAVE_MESSAGE = "budget_proposalRates_exceptionCode.1251"; //"Saving will entail recalculation of the budget. Continue ?";
    private final String UNDO_RATE_CHANGES = "budget_proposalRates_exceptionCode.1252"; //"Do you want to undo changes and reset rates ?";
    private final String SYNC_MESSAGE = "budget_proposalRates_exceptionCode.1253"; //"Do you want to Sync Proposal rates with the currrent Institute rates ?";
    private static final String COULD_NOT_CONTACT_SERVER = "Could not contact server";
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    private final String conURL = "/BudgetMaintenanceServlet";
    //holds the connection string
    private String connectionURL = CoeusGuiConstants.CONNECTION_URL;
    
    private BudgetInfoBean budgetInfoBean;
    private Vector masterRateVector;
    
    // Initial Bean instance for the Rates
    private InstituteRatesBean instituteRatesBean = new InstituteRatesBean();
    private InstituteLARatesBean instituteLARatesBean = new InstituteLARatesBean();
    private ProposalRatesBean proposalRatesBean = new ProposalRatesBean();
    private ProposalLARatesBean proposalLARatesBean = new ProposalLARatesBean();
    
    //Vector containing the beans added to the Panel rows
    private Vector vecProposalRateInstPanelBeans;
    private Vector vecProposalRateLAPanelBeans;
    
    
    //Vector containing either Institute or ProposalRate Beans based on FunctionType
    private CoeusVector vecInstRateBeans = new CoeusVector();
    //Vector containing either InstituteLA or ProposalLARate Beans based on FunctionType
    private CoeusVector vecLARateBeans = new CoeusVector();
    
    //Vector containing either ProposalRate on the Basewindow [Copy]
    private CoeusVector vectBaseWindowInstRateBeans = null;
    //Vector containing either ProposalLARate Beans on the Basewindow [Copy]
    CoeusVector vectBaseWindowLARateBeans = null;
    //private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    //Query Instance
    private QueryEngine queryEngine =  QueryEngine.getInstance();
    private static final String ACTYPE =  "acType";
    
    private static final String RATECLASSCODE =  "rateClassCode";
    private static final String RATETYPECODE =  "rateTypeCode";
    private static final String FISCALYEAR =  "fiscalYear";
    private static final String ONOFFCAMPUS =  "onOffCampusFlag";
    private static final String STARTDATE =  "startDate";
    private char functionType;
    private char mode;
    private int clicked;
    private boolean isParent;
    private Vector dataObject;
    private static final char GET_BUDGET_RATES = 'l';
    private static final char GET_BUDGET_INFO = 'j';
    public  static final String SERVLET = "/BudgetMaintenanceServlet";
    //Case 2453 - start
    private boolean calculationChanged;
    //Case 2453 - End
    
    //COEUSQA-1689 Role Restrictions for Budget Rates - Start
    private static final String HIERARCHY_SERVLET = "/ProposalHierarchyServlet";
    private static final char CHECK_MODIFY_PROPOSAL_RATES_RIGHT = 'T';
    //COEUSQA-1689 Role Restrictions for Budget Rates - End
    
    /** Creates a new instance of ProposalRateController */
    public ProposalRateController() {
        this.parent = CoeusGuiConstants.getMDIForm();
        this.modal = true;
        initialiseController();
    }
    
    /** Creates a new instance of ProposalRateController with Budget Bean instance
     * @param parent Componet parent form instance
     * @param budgetInfoBean budgetInfoBean BudgetInfoBean instance
     * @param modal if <true> model window
     */
    public ProposalRateController(java.awt.Component parent,boolean modal,BudgetInfoBean budgetInfoBean,char functionType) {
        super(budgetInfoBean);
        this.budgetInfoBean = budgetInfoBean;
        this.parent = parent;
        this.modal = modal;
        initialiseController();
        // Added by chandr1043a 17-sept-2004
        setFunctionType(functionType);
        setFormData(budgetInfoBean);
        // end Chandra 17-sept-2004

    }
    // Added overloaded constructor for the Bug Fix#1441
    //COEUSQA-1689 Role Restrictions for Budget Rates - Start
    //Additional parameter unitNumber is passed while initializing the controller
    public ProposalRateController(java.awt.Component parent,boolean modal,BudgetInfoBean budgetInfoBean,char functionType,char mode, String unitNumber) {
        super(budgetInfoBean);
        this.budgetInfoBean = budgetInfoBean;
        this.parent = parent;
        this.modal = modal;
        this.mode = mode;
        this.unitNumber = unitNumber;
         initialiseController();
        // Added by chandr1043a 17-sept-2004
        setFunctionType(functionType);
        setFormData(budgetInfoBean);
        // end Chandra 17-sept-2004
    }
    //COEUSQA-1689 Role Restrictions for Budget Rates - End
    
     /** This constructor will be invoked when the Proposal is in Hierarchy and the
      *selected Propsoal is the Parent Propsoal. It accepts a boolean isParent to
      *identify the proposal is parent
      */
    public ProposalRateController(java.awt.Component parent,boolean modal,
        BudgetInfoBean budgetInfoBean,char functionType,char mode,boolean isParent) {
        super(budgetInfoBean);
        this.budgetInfoBean = budgetInfoBean;
        this.parent = parent;
        this.modal = modal;
        this.mode = mode;
        this.isParent = isParent;
        initialiseController();
        setFunctionType(functionType);
    }
    
    /**
     * Initialisation of Controller
     */
    
    private void initialiseController() {
        isRateModified = false;
        coeusMessageResources = CoeusMessageResources.getInstance();
        masterRateVector = new Vector();
        vecProposalRateInstPanelBeans = new Vector();
        vecProposalRateLAPanelBeans = new Vector();
        proposalRateForm =  new ProposalRateForm(parent,modal);
        registerComponents();
        // Commented by chandra , the formatfileds will be called in setFunctionType
        // in the Superclass - 17-Sept-2004
        //formatFields();
//        setFormData(budgetInfoBean);
        // end chandra 17-Sept-2004
        setFocusInComp();
        setFocusTraversal();
        
    }
    
    /** Added by chandra
     *bug Fix for setting the default focus in the table not on the buttons
     *This method will set the default focus in the component
     *16th June 2004
     */
    public void setFocusInComp(){
        Component Instcomp; 
        
        ProposalRateInstPanelBean proposalRateInstPanelBean;
        ProposalRateLAPanelBean proposalRateLAPanelBean;
        if(vecProposalRateInstPanelBeans != null && vecProposalRateInstPanelBeans.size() > 0) {
            proposalRateInstPanelBean = (ProposalRateInstPanelBean)vecProposalRateInstPanelBeans.get(0);
            Instcomp = proposalRateInstPanelBean.txtApplicableRates;
            Instcomp.requestFocusInWindow();
        }
//        if(vecProposalRateLAPanelBeans != null && vecProposalRateLAPanelBeans.size() > 0) {
//            proposalRateLAPanelBean = (ProposalRateLAPanelBean)vecProposalRateLAPanelBeans.get(0);
//            comp = proposalRateLAPanelBean.txtApplicableRates;
//            comp.requestFocusInWindow();
//        }
    }
    // End chandra - 16 June 2004
    
    
    //added by sharath for text box traversal(Bug Fix:768, 769) - 22-Apr-2004 - START
    private void setFocusTraversal() {
        int count;
        Component components[];
        ProposalRateFocusTraversal traversePolicy;
        
        if(vecProposalRateInstPanelBeans != null && vecProposalRateInstPanelBeans.size() > 0) {
            count = vecProposalRateInstPanelBeans.size();
            components = new Component[count];
            
            ProposalRateInstPanelBean proposalRateInstPanelBean;
            
            for(int index = 0; index < count; index++) {
                proposalRateInstPanelBean = (ProposalRateInstPanelBean)vecProposalRateInstPanelBeans.get(index);
                components[index] = proposalRateInstPanelBean.txtApplicableRates;
            }
            
            traversePolicy = new ProposalRateFocusTraversal( components );
            proposalRateForm.pnlInstituteTabPane.setFocusTraversalPolicy(traversePolicy);
            proposalRateForm.pnlInstituteTabPane.setFocusCycleRoot(true);
        }
        
        //focus traversal for LA
        if(vecProposalRateLAPanelBeans != null && vecProposalRateLAPanelBeans.size() > 0) {
            count = vecProposalRateLAPanelBeans.size();
            components = new Component[count];
            
            ProposalRateLAPanelBean proposalRateLAPanelBean;
            
            for(int index = 0; index < count; index++) {
                proposalRateLAPanelBean = (ProposalRateLAPanelBean)vecProposalRateLAPanelBeans.get(index);
                components[index] = proposalRateLAPanelBean.txtApplicableRates;
            }
            
            //traversePolicy = new ScreenFocusTraversalPolicy( components );
            
            traversePolicy = new ProposalRateFocusTraversal( components );
            
            proposalRateForm.pnlLATabPane.setFocusTraversalPolicy(traversePolicy);
            proposalRateForm.pnlLATabPane.setFocusCycleRoot(true);
        }
        
    }
    
    //added by sharath for text box traversal(Bug Fix:768, 769) - 22-Apr-2004 - END
    
    /** Getter for property ProposalNumber. of the current Budget
     * @return proposalNumber String : ProposalNumber property.
     */
    
    public String getProposalNumber() {
        return proposalNumber;
    }
    
    /** Setter property ProposalNumber. of the current Budget
     * @param proposalNumber String ProposalNumber property.
     */
    
    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
    /** Getter for property versionNumber.
     * @return versionNumber int versionNumber property.
     */
    
    public int getVersionNumber() {
        return versionNumber;
    }
    
    /** Setter property versionNumber.
     * @param versionNumber int versionNumber of the Budget
     */
    
    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }
    
    
    /** Getter for property activityTypeCode.
     * @return activityTypeCode int activityTypeCode property.
     */
    
    public int getActivityTypeCode() {
        return activityTypeCode;
        
    }
    
    /** Setter property activityTypeCode.
     * @param activityTypeCode int activityTypeCode property.
     */
    
    public void setActivityTypeCode(int activityTypeCode) {
        this.activityTypeCode = activityTypeCode;
    }
    
    
    /** Getter for property unitNumber.
     * @return Value of property unitNumber.
     */
    public String getUnitNumber() {
        return unitNumber;
    }
    
    /** Setter for property unitNumber.
     * @param unitNumber New value of property unitNumber.
     */
    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }
    
    /**
     * Checks and return a Boolean flag if any values in the Rates Tab is being modified
     *@return boolean true if rates have been modified in either Rate Tab
     */
    
    private boolean isTabRateChanged() {
        boolean isBeanUpdated = false;
        
        for(int indexVal=0;indexVal < vecProposalRateInstPanelBeans.size();indexVal++ ) {
            ProposalRateInstPanelBean propRateInstPanelBean =  (ProposalRateInstPanelBean) vecProposalRateInstPanelBeans.elementAt(indexVal);
            if(propRateInstPanelBean.isBeanUpdated()) {
                isBeanUpdated = true;
            }
        }
        
        for(int indexVal=0;indexVal < vecProposalRateLAPanelBeans.size();indexVal++ ) {
            ProposalRateLAPanelBean propRateLAPanelBean =  (ProposalRateLAPanelBean) vecProposalRateLAPanelBeans.elementAt(indexVal);
            if(propRateLAPanelBean.isBeanUpdated()) {
                isBeanUpdated = true;
            }
        }
        
        return isBeanUpdated;
    }
    
    
    /** The actionPerformed method for responding to actions on OK cancel Reset and Sync button of the Form
     * @param actionEvent Object
     */
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        try{
            Object source = actionEvent.getSource();

            //"**************** ACTION BUTTON OK **************** "

            if (source.equals(proposalRateForm.btnOK)){
                performAction(confirmButtonAction(SAVE_MESSAGE),OK_ACTION_CODE);
                return;
            }

            //"**************** ACTION BUTTON CANCEL **************** "

            if (source.equals(proposalRateForm.btnCancel)){
                clicked = 1;
                checkOnCancel();
            }

            //"**************** ACTION BUTTON RESET **************** "

            if (source.equals(proposalRateForm.btnReset)){

                performAction(confirmButtonAction(UNDO_RATE_CHANGES),RESET_ACTION_CODE);
                setFocusInComp();
            }

            //"**************** ACTION BUTTON SYNC **************** "

            if (source.equals(proposalRateForm.btnSync)){
                performAction(confirmButtonAction(SYNC_MESSAGE),SYNC_ACTION_CODE);
                setFocusInComp();
            }
        }catch (CoeusClientException coeusClientException){
            CoeusOptionPane.showDialog(coeusClientException);
        }

        
    } // end of actionperformed method
    
    /**
     * Method to Confirm a Cancel Action  and calls the saveData
     * method to save on confirmation
     */
    
    public void checkOnCancel() {
        
        boolean showConfirmWindow = isTabRateChanged();
        if(showConfirmWindow || isRateModified ) {
            int option = JOptionPane.NO_OPTION;
            option  = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(SAVE_CHANGES),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            CoeusOptionPane.DEFAULT_YES);
            switch(option) {
                case ( JOptionPane.YES_OPTION ):
                    
                    option  = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(SAVE_MESSAGE),
                    CoeusOptionPane.OPTION_YES_NO,
                    CoeusOptionPane.DEFAULT_NO);
                    switch(option) {
                        case ( JOptionPane.YES_OPTION ):
                            saveData();
                            proposalRateForm.dlgProposalRateForm.dispose();
                            break;
                    }
                    
                    break;
                    
                case ( JOptionPane.NO_OPTION ):
                    proposalRateForm.dlgProposalRateForm.dispose();
                    break;
                case ( JOptionPane.CANCEL_OPTION):
                    break;
            }
            
            
        }
        else
            proposalRateForm.dlgProposalRateForm.dispose();
        return;
    }
    
    /**
     * This method is used to confirm the action on the Buttons
     * If the saveRequired is true then it proceeds further
     * else dispose this JDialog.
     */
    private int confirmButtonAction(String coeusMessagesKeyCode){
        
        int option = JOptionPane.NO_OPTION;
        option  = CoeusOptionPane.showQuestionDialog(
        coeusMessageResources.parseMessageKey(coeusMessagesKeyCode),
        CoeusOptionPane.OPTION_YES_NO,
        CoeusOptionPane.DEFAULT_NO);
        return option;
    }
    
    /** Check the confirmation action and do the necessary operation
     * @param confirmOptionValue int
     * @param ACTION_CODE int Type of action :Code for each Button action
     */
    public void performAction(int confirmOptionValue,int ACTION_CODE) throws CoeusClientException{
        //sett the action code for the action
        this.ACTION_CODE = ACTION_CODE;
        switch(confirmOptionValue){
            case ( JOptionPane.YES_OPTION ):
                try{
                    if(getFunctionType() != DISPLAY_MODE) {
                        checkButtonAction();
                        return;
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    CoeusOptionPane.showErrorDialog(e.getMessage());
                }
                
                break;
            case ( JOptionPane.NO_OPTION ):
                if(ACTION_CODE == CANCEL_ACTION_CODE) {
                    proposalRateForm.dlgProposalRateForm.dispose();
                    break;
                }
                
        }
        
        
    }
    
    /**
     * SaveFormData calls the saveData to save the Rates to Base Window
     */
    
    public void saveFormData() {
        saveData();
    }
    
    /**
     * Check for each Button action
     */
    public void checkButtonAction() throws CoeusClientException{
        
        if(ACTION_CODE == OK_ACTION_CODE) {
            if(saveData()) {
                proposalRateForm.dlgProposalRateForm.dispose();
            }
            
        }
        if(ACTION_CODE == CANCEL_ACTION_CODE) {
            
            int option = JOptionPane.NO_OPTION;
            option  = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(SAVE_MESSAGE),
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            switch(option) {
                case ( JOptionPane.YES_OPTION ):
                    saveData();
                    break;
                    
                case ( JOptionPane.NO_OPTION ):
                    proposalRateForm.dlgProposalRateForm.dispose();
                    break;
            }
            
        }
        if(ACTION_CODE == RESET_ACTION_CODE) {
            
            for(int indexVal=0;indexVal < vecProposalRateInstPanelBeans.size();indexVal++ ) {
                ProposalRateInstPanelBean panelBeans =  (ProposalRateInstPanelBean) vecProposalRateInstPanelBeans.elementAt(indexVal);
                panelBeans.reset();
            }
            
            for(int indexVal=0;indexVal < vecProposalRateLAPanelBeans.size();indexVal++ ) {
                ProposalRateLAPanelBean panelBeans =  (ProposalRateLAPanelBean) vecProposalRateLAPanelBeans.elementAt(indexVal);
                panelBeans.reset();
            }
        }
        
        if(ACTION_CODE == SYNC_ACTION_CODE) {
                ((ProposalRateForm) getControlledUI()).dlgProposalRateForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                ((ProposalRateForm) getControlledUI()).dlgProposalRateForm.setVisible(true);
            
            budgetInfoBean.setUnitNumber(unitNumber);
            budgetInfoBean.setActivityTypeCode(activityTypeCode);
            RequesterBean requesterBean  = new RequesterBean();
            requesterBean.setFunctionType(GET_MASTER_DATA_TO_SYNC);
            requesterBean.setDataObject(budgetInfoBean);
            
            String connectTo = connectionURL + conURL;
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean );
            comm.send();
            ResponderBean responderBean = comm.getResponse();
            if(responderBean==null){
                throw new CoeusClientException(COULD_NOT_CONTACT_SERVER, CoeusClientException.ERROR_MESSAGE); 
            }
            if(responderBean.isSuccessfulResponse()) {
                if ( responderBean !=null ){
                    masterRateVector = (Vector) responderBean.getDataObjects();
                    
                    CoeusVector vecInstituteRateBean = (CoeusVector) masterRateVector.elementAt(0);
                    CoeusVector vecInstituteLARateBean = (CoeusVector) masterRateVector.elementAt(1);
                    
                    //Bug Fix:1653 Start
//                    if(vecInstituteRateBean == null  || vecInstituteLARateBean ==null) {
//                        ((ProposalRateForm) getControlledUI()).dlgProposalRateForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//                        ((ProposalRateForm) getControlledUI()).dlgProposalRateForm.getGlassPane().setVisible(false);
//                        return;
//                    }
                    
                    if(vecInstituteRateBean != null){
                        //============Filter Part====================
                        vecInstituteRateBean = getFilteredVector(vecInstituteRateBean);
                        
                        //vecInstituteRateBean.sort(RATECLASSCODE,true);
    //                    String fieldNames [] ={
    //                        RATECLASSCODE,
    //                        RATETYPECODE,
    //                        FISCALYEAR,
    //                        //ONOFFCAMPUS,
    //                        STARTDATE
    //                    };
                        /** Added  by chandra to fix #1100 bug.
                         *Sort the values only on Rate class code, Rate type code &
                         *Fiscal Year.
                         *start - 2nd August 2004
                         */  
                        String fieldNames [] ={
                            RATECLASSCODE,
                            RATETYPECODE,
                            FISCALYEAR,
                        };
                        // End chandra - 2nd August 2004.
                        vecInstituteRateBean.sort(fieldNames,true);

                        

                        //============Filter Part====================

                        // setting the sync value to the Form
                        // Resetting the Form with Master Table Values

                        vecProposalRateInstPanelBeans = new Vector();
                        
                        vecInstRateBeans = vecInstituteRateBean;
                        
//                        proposalRateForm.dlgProposalRateForm.dispose();
//                        proposalRateForm =  new ProposalRateForm(parent,true);
//                        registerComponents();
//                        isRateModified  = true;
//                        
                        
                    }
                    
                    if(vecInstituteLARateBean != null){
                        //============Filter Part====================
                        
                        vecInstituteLARateBean = getFilteredVector(vecInstituteLARateBean);
                        //vecInstituteRateBean.sort(RATECLASSCODE,true);
    //                    String fieldNames [] ={
    //                        RATECLASSCODE,
    //                        RATETYPECODE,
    //                        FISCALYEAR,
    //                        //ONOFFCAMPUS,
    //                        STARTDATE
    //                    };
                        /** Added  by chandra to fix #1100 bug.
                         *Sort the values only on Rate class code, Rate type code &
                         *Fiscal Year.
                         *start - 2nd August 2004
                         */  
                        String fieldNames [] ={
                            RATECLASSCODE,
                            RATETYPECODE,
                            FISCALYEAR,
                        };
                        // End chandra - 2nd August 2004.
                        

                        vecInstituteLARateBean.sort(fieldNames,true);

                        //============Filter Part====================

                        // setting the sync value to the Form
                        // Resetting the Form with Master Table Values

                        
                        vecProposalRateLAPanelBeans = new Vector();
                        
                        vecLARateBeans = vecInstituteLARateBean;
//                        proposalRateForm.dlgProposalRateForm.dispose();
//                        proposalRateForm =  new ProposalRateForm(parent,true);
//                        registerComponents();
//                        isRateModified  = true;
                        
                        
                    }
                    proposalRateForm.dlgProposalRateForm.dispose();
                    proposalRateForm =  new ProposalRateForm(parent,true);
                    registerComponents();
                    isRateModified  = true;
                    if(vecInstituteRateBean != null){
                        setupInstituteRateTabDetails(vecInstituteRateBean);
                    }
                    
                    if(vecInstituteLARateBean != null){
                        setupLARateTabDetails(vecInstituteLARateBean);
                    }
                    
                    display();
                    //Case 2453 - start
                    calculationChanged= true;
                    //Case 2453 - End
                    ((ProposalRateForm) getControlledUI()).dlgProposalRateForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    ((ProposalRateForm) getControlledUI()).dlgProposalRateForm.getGlassPane().setVisible(false);
                    //Bug Fix:1653 End
                }
            }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
    }
    
    /*
     *  Method that calls individual save method for Institute and LA Rates
     *@return  true if both rates are saved successfully
     */
    
    public boolean saveData() {
        boolean isSavedSuccessful = false;
        
        if(saveInstituteRates()) {
            isSavedSuccessful = true;
            //Case 2453 - start
            calculationChanged = true;
            //Case 2453 - End
        }
        else{
            isSavedSuccessful= false;
            //Case 2453 - start
            calculationChanged = false;
            //Case 2453 - End
        }
        
        if(saveLARates()) {
            isSavedSuccessful= true;
            //Case 2453 - start
            calculationChanged = true;
            //Case 2453 - End
        }
        else{
            isSavedSuccessful= false;
            //Case 2453 - start
            calculationChanged = false;
            //Case 2453 - End
        }
        return isSavedSuccessful;
        
    }
    
    /**
     * Delete the existing ProposalRates beans in Base window details on SYNC operation
     */
    public void deleteInstRateFromBaseWindow() {
        
        try {
            ProposalRatesBean proposalRatesBean = new ProposalRatesBean();
            proposalRatesBean.setProposalNumber(proposalNumber);
            proposalRatesBean.setVersionNumber(versionNumber);
            Equals equalsActype = new Equals(ACTYPE,null);
            NotEquals notequalsActype = new NotEquals(ACTYPE,TypeConstants.DELETE_RECORD);
            Or actypeBoth = new Or(equalsActype,notequalsActype);
            CoeusVector existingInstBeans = queryEngine.executeQuery((proposalNumber+versionNumber), ProposalRatesBean.class,actypeBoth);
            for(int i=0;i < existingInstBeans.size();i++ ) {
                ProposalRatesBean baseProposalRatesBean  = (ProposalRatesBean) existingInstBeans.get(i);
                baseProposalRatesBean.setAcType(TypeConstants.DELETE_RECORD);
                queryEngine.delete((proposalNumber+versionNumber),baseProposalRatesBean);
            }
            
        }catch(Exception exception){
            exception.getMessage();
        }
    }
    
    /**
     * Delete the existing ProposalLARates beans in base window on SYNC operation
     */
    
    public void deleteLARatesFromBaseWindow() {
        
        try {
            ProposalLARatesBean newProposalLARatesBean = new ProposalLARatesBean();
            newProposalLARatesBean.setProposalNumber(proposalNumber);
            newProposalLARatesBean.setVersionNumber(versionNumber);
            Equals equalsActype = new Equals(ACTYPE,null);
            NotEquals notequalsActype = new NotEquals(ACTYPE,TypeConstants.DELETE_RECORD);
            Or actypeBoth = new Or(equalsActype,notequalsActype);
            
            CoeusVector existingLABeans = queryEngine.executeQuery((proposalNumber+versionNumber), ProposalLARatesBean.class,actypeBoth);
            for(int i=0;i < existingLABeans.size();i++ ) {
                ProposalLARatesBean baseProposalLARatesBean = (ProposalLARatesBean) existingLABeans.get(i);
                baseProposalLARatesBean.setAcType(TypeConstants.DELETE_RECORD);
                queryEngine.delete((proposalNumber+versionNumber),baseProposalLARatesBean);
            }
            
            
        }catch(Exception exception) {
            exception.getMessage();
        }
    }
    
    /**
     * Save the ProposalRates beans to the base window on UPDATE/SYNC operation
     */
    
    public boolean saveInstituteRates() {
        boolean isSaveInstituteRates = true;
        CoeusVector newRates = new CoeusVector();
        ProposalRatesBean newProposalRatesBean = null;
        
        if(vecInstRateBeans != null && vecInstRateBeans.size() >0) {
            //===============SAVE WITH NEW INSITITUTE RATES ON SYNC OPERATION ============
            try {
                if(vecInstRateBeans.get(0).getClass() == instituteRatesBean.getClass()) {
                    deleteInstRateFromBaseWindow();
                    
                    for(int indexVal=0;indexVal < vecProposalRateInstPanelBeans.size();indexVal++ ) {
                        ProposalRateInstPanelBean panelBeans =  (ProposalRateInstPanelBean) vecProposalRateInstPanelBeans.elementAt(indexVal);
                        newProposalRatesBean = new ProposalRatesBean();
                        
                        for(int insideIndex=0;insideIndex< vecInstRateBeans.size();insideIndex++ ) {
                            InstituteRatesBean instituteRatesBean = (InstituteRatesBean) vecInstRateBeans.get(insideIndex);
                            String key = (  instituteRatesBean.getRateClassCode()+
                            instituteRatesBean.getRateTypeCode()+
                            instituteRatesBean.getFiscalYear()+
                            instituteRatesBean.isOnOffCampusFlag()+
                            instituteRatesBean.getStartDate()+
                            instituteRatesBean.getActivityCode()+
                            instituteRatesBean.getInstituteRate()
                            );
                            
                            if(panelBeans.getPanelIdentityKey().equalsIgnoreCase(key)) {
                                
                                double appPanelRate = Double.parseDouble(panelBeans.getApplicableRate());
                                // double appBeanRate = instituteRatesBean.getInstituteRate();
                                newProposalRatesBean = new ProposalRatesBean();
                                newProposalRatesBean.setProposalNumber(proposalNumber);
                                newProposalRatesBean.setVersionNumber(versionNumber);
                                newProposalRatesBean.setAcType(TypeConstants.INSERT_RECORD);
                                newProposalRatesBean.setRateClassCode(instituteRatesBean.getRateClassCode());
                                newProposalRatesBean.setRateClassDescription(instituteRatesBean.getRateClassDescription());
                                newProposalRatesBean.setRateTypeCode(instituteRatesBean.getRateTypeCode());
                                newProposalRatesBean.setRateTypeDescription(instituteRatesBean.getRateTypeDescription());
                                newProposalRatesBean.setActivityCode(instituteRatesBean.getActivityCode());
                                newProposalRatesBean.setActivityTypeDescription(instituteRatesBean.getActivityTypeDescription());
                                newProposalRatesBean.setFiscalYear(instituteRatesBean.getFiscalYear());
                                newProposalRatesBean.setOnOffCampusFlag(instituteRatesBean.isOnOffCampusFlag());
                                newProposalRatesBean.setStartDate(instituteRatesBean.getStartDate());
                                newProposalRatesBean.setInstituteRate(instituteRatesBean.getInstituteRate());
                                newProposalRatesBean.setApplicableRate(appPanelRate);
                                newProposalRatesBean.setAw_ActivityTypeCode(instituteRatesBean.getActivityCode());
                                
                                CoeusVector  timeStampCoeusVector = queryEngine.executeQuery((proposalNumber+versionNumber), newProposalRatesBean);
                                if(timeStampCoeusVector != null && timeStampCoeusVector.size() > 0) {
                                    newProposalRatesBean.setUpdateTimestamp(((ProposalRatesBean) timeStampCoeusVector.get(0)).getUpdateTimestamp());
                                }
                                
                                newRates.add(newProposalRatesBean);
                            }
                        }
                    }
                    
                    // Inserting the New Rates to base window
                    try{
                        for(int j=0;j< newRates.size();j++ ) {
                            ProposalRatesBean insertProposalRatesBean = (ProposalRatesBean) newRates.get(j);
                            queryEngine.insert((proposalNumber+versionNumber), insertProposalRatesBean);
                        }
                        newProposalRatesBean = new ProposalRatesBean();
                        newProposalRatesBean.setProposalNumber(proposalNumber);
                        newProposalRatesBean.setVersionNumber(versionNumber);
                        CoeusVector  coeusVect = queryEngine.executeQuery((proposalNumber+versionNumber), newProposalRatesBean);
                        coeusVect.sort(ACTYPE,true);
                        
                    }catch(Exception exception){
                        exception.getMessage();
                        isSaveInstituteRates =  false;
                    }
                    
                    isSaveInstituteRates = true;
                    
                }
                
            } catch(Exception exception){
                exception.getMessage();
            }
            //================== UPDATE PROPOSAL RATES ==========================
            if(vecInstRateBeans.get(0).getClass() == proposalRatesBean.getClass()) {
                isSaveInstituteRates = true;
                
                for(int indexVal=0;indexVal < vecProposalRateInstPanelBeans.size();indexVal++ ) {
                    ProposalRateInstPanelBean panelBeans =  (ProposalRateInstPanelBean) vecProposalRateInstPanelBeans.elementAt(indexVal);
                    newProposalRatesBean = new ProposalRatesBean();
                    
                    for(int insideIndex=0;insideIndex< vecInstRateBeans.size();insideIndex++ ) {
                        ProposalRatesBean proposalRatesBean = (ProposalRatesBean) vecInstRateBeans.get(insideIndex);
                        String key = (  proposalRatesBean.getRateClassCode()+
                        proposalRatesBean.getRateTypeCode()+
                        proposalRatesBean.getFiscalYear()+
                        proposalRatesBean.isOnOffCampusFlag()+
                        proposalRatesBean.getStartDate()+
                        proposalRatesBean.getActivityCode()+
                        proposalRatesBean.getInstituteRate()
                        );
                        
                        if(panelBeans.getPanelIdentityKey().equalsIgnoreCase(key)) {
                            
                            double appPanelRate = Double.parseDouble(panelBeans.getApplicableRate());
                            double appBeanRate = proposalRatesBean.getApplicableRate();
                            try{
                                if(appPanelRate != appBeanRate ) {
                                    proposalRatesBean.setAcType(TypeConstants.UPDATE_RECORD);
                                    proposalRatesBean.setApplicableRate(appPanelRate);
                                    queryEngine.update((proposalNumber+versionNumber), proposalRatesBean);
                                }
                            }catch(CoeusException coeusException){
                                coeusException.getMessage();
                                isSaveInstituteRates =  false;
                            }
                            
                        }
                        
                        
                    }
                }
                
                
                
            }
            
        }
        return isSaveInstituteRates;
    }// end of method
    
    /**
     * Save the ProposalLARates beans to the base window on UPDATE/SYNC operation
     */
    
    public boolean saveLARates() {
        boolean isSaveLARates = true;
        //***************************************************************************
        CoeusVector newLARates = new CoeusVector();
        ProposalLARatesBean newProposalLARatesBean = null;  //new ProposalLARatesBean();
        
        if(vecLARateBeans != null && vecLARateBeans.size() >0) {
            //=======================SAVE WITH NEW LA ON SYNC OPERATION START===========
            try {
                
                if(vecLARateBeans.get(0).getClass() == instituteLARatesBean.getClass()) {
                    deleteLARatesFromBaseWindow();
                    
                    for(int indexVal=0;indexVal < vecProposalRateLAPanelBeans.size();indexVal++ ) {
                        ProposalRateLAPanelBean panelBeans =  (ProposalRateLAPanelBean) vecProposalRateLAPanelBeans.elementAt(indexVal);
                        
                        
                        for(int insideIndex=0;insideIndex< vecLARateBeans.size();insideIndex++ ) {
                            InstituteLARatesBean instituteLARatesBean = (InstituteLARatesBean) vecLARateBeans.get(insideIndex);
                            String key = (  instituteLARatesBean.getRateClassCode()+
                            instituteLARatesBean.getRateTypeCode()+
                            instituteLARatesBean.getFiscalYear()+
                            instituteLARatesBean.isOnOffCampusFlag()+
                            instituteLARatesBean.getStartDate()+
                            instituteLARatesBean.getInstituteRate()
                            );
                            
                            if(panelBeans.getPanelIdentityKey().equalsIgnoreCase(key)) {
                                
                                double appPanelRate = Double.parseDouble(panelBeans.getApplicableRate());
                                
                                newProposalLARatesBean = new ProposalLARatesBean();
                                newProposalLARatesBean.setProposalNumber(proposalNumber);
                                newProposalLARatesBean.setVersionNumber(versionNumber);
                                newProposalLARatesBean.setAcType(TypeConstants.INSERT_RECORD);
                                newProposalLARatesBean.setRateClassCode(instituteLARatesBean.getRateClassCode());
                                newProposalLARatesBean.setRateClassDescription(instituteLARatesBean.getRateClassDescription());
                                newProposalLARatesBean.setRateTypeCode(instituteLARatesBean.getRateTypeCode());
                                newProposalLARatesBean.setRateTypeDescription(instituteLARatesBean.getRateTypeDescription());
                                newProposalLARatesBean.setFiscalYear(instituteLARatesBean.getFiscalYear());
                                newProposalLARatesBean.setOnOffCampusFlag(instituteLARatesBean.isOnOffCampusFlag());
                                newProposalLARatesBean.setStartDate(instituteLARatesBean.getStartDate());
                                newProposalLARatesBean.setInstituteRate(instituteLARatesBean.getInstituteRate());
                                newProposalLARatesBean.setApplicableRate(appPanelRate);
                                
                                
                                CoeusVector  timeStampCoeusVector = queryEngine.executeQuery((proposalNumber+versionNumber), newProposalLARatesBean);
                                
                                if(timeStampCoeusVector != null && timeStampCoeusVector.size() > 0) {
                                    newProposalLARatesBean.setUpdateTimestamp(((ProposalLARatesBean) timeStampCoeusVector.get(0)).getUpdateTimestamp());
                                }
                                
                                
                                newLARates.add(newProposalLARatesBean);
                            }
                        }
                    }
                    
                    // Inserting the New LARates to base window
                    try{
                        
                        for(int j=0;j< newLARates.size();j++ ) {
                            ProposalLARatesBean insertProposalLARatesBean = (ProposalLARatesBean) newLARates.get(j);
                            queryEngine.insert((proposalNumber+versionNumber), insertProposalLARatesBean);
                        }
                        newProposalLARatesBean = new ProposalLARatesBean();
                        newProposalLARatesBean.setProposalNumber(proposalNumber);
                        newProposalLARatesBean.setVersionNumber(versionNumber);
                        CoeusVector  coeusVect = queryEngine.executeQuery((proposalNumber+versionNumber), newProposalLARatesBean);
                        coeusVect.sort(ACTYPE,true);
                        
                    }catch(Exception exception){
                        exception.getMessage();
                        isSaveLARates =  false;
                    }
                    
                    isSaveLARates =  true;
                    
                    
                }
                
            }catch(Exception exception){
                exception.getMessage();
            }
            //=======================SAVE WITH NEW LA ON SYNC OPERATION END========
            
            //=======================SAVE MODIFICATION OF LA RATES START===========
            if(vecLARateBeans.get(0).getClass() == proposalLARatesBean.getClass()) {
                isSaveLARates = true;
                for(int indexVal=0;indexVal < vecProposalRateLAPanelBeans.size();indexVal++ ) {
                    ProposalRateLAPanelBean panelBeans =  (ProposalRateLAPanelBean) vecProposalRateLAPanelBeans.elementAt(indexVal);
                    newProposalLARatesBean = new ProposalLARatesBean();
                    
                    for(int insideIndex=0;insideIndex< vecLARateBeans.size();insideIndex++ ) {
                        ProposalLARatesBean proposalLARatesBean = (ProposalLARatesBean) vecLARateBeans.get(insideIndex);
                        String key = (  proposalLARatesBean.getRateClassCode()+
                        proposalLARatesBean.getRateTypeCode()+
                        proposalLARatesBean.getFiscalYear()+
                        proposalLARatesBean.isOnOffCampusFlag()+
                        proposalLARatesBean.getStartDate()+
                        proposalLARatesBean.getInstituteRate()
                        );
                        
                        if(panelBeans.getPanelIdentityKey().equalsIgnoreCase(key)) {
                            
                            double appPanelRate = Double.parseDouble(panelBeans.getApplicableRate());
                            double appBeanRate = proposalLARatesBean.getApplicableRate();
                            
                            try{
                                if(appPanelRate != appBeanRate ) {
                                    proposalLARatesBean.setAcType(TypeConstants.UPDATE_RECORD);
                                    proposalLARatesBean.setApplicableRate(appPanelRate);
                                    queryEngine.update((proposalNumber+versionNumber), proposalLARatesBean);
                                }
                            }catch(CoeusException coeusException){
                                coeusException.getMessage();
                                isSaveLARates =  false;
                            }
                            
                        }
                        
                        
                    }
                }
                
            }
            //=======================SAVE MODIFICATION OF LA RATES END=============
            
        }
        return isSaveLARates;
    }// end of method
    
    
     /*
      * Method to filter the Vector for each Rates and finding all applicable
      * for each Rates during the Proposal Period
      *@param vecRateBean CoeusVector to be filtered based on the Startdate and End Date
      *@return CoeusVector filtered CoeusVector
      */
    
    public CoeusVector getFilteredVector(CoeusVector vecRateBean) {
        Hashtable rateCode = new Hashtable();
        CoeusVector completeVector = new CoeusVector();
        CoeusVector vecEachRateBean;
        if(vecRateBean != null ) {
            
            for(int filterIndex=0; filterIndex< vecRateBean.size();filterIndex ++) {
                
                int rateClassCode = ((RatesBean ) vecRateBean.get(filterIndex)).getRateClassCode();
                int rateTypeCode = ((RatesBean ) vecRateBean.get(filterIndex)).getRateTypeCode();
                
                if(!rateCode.contains((rateClassCode+"-"+rateTypeCode))) {
                    
                    Equals equalRateClassCodeObj  =  new Equals(RATECLASSCODE,new Integer(rateClassCode));
                    Equals equalRateTypeCodeObj  =  new Equals("rateTypeCode",new Integer(rateTypeCode));
                    And equalRateClassAndType = new And(equalRateClassCodeObj,equalRateTypeCodeObj);
                    
                    vecEachRateBean = vecRateBean.filter(equalRateClassAndType);
                    //Case 3447 - START
                    boolean inflation = false;
                    if(rateClassCode == 7) inflation = true; 
                    CoeusVector vecRateBeanOnThePeriods = getRatesOnThePeriod(vecEachRateBean,budgetInfoBean, inflation);
                    completeVector.addAll(vecRateBeanOnThePeriods);
                    
                    CoeusVector vecRateBeanWithInPeriod = getRatesWithInPeriod(vecEachRateBean,budgetInfoBean, inflation);
                    //Case 3447 - END
                    completeVector.addAll(vecRateBeanWithInPeriod);
                    
                    rateCode.put((rateClassCode+"-"+rateTypeCode),(rateClassCode+"-"+rateTypeCode));
                }
                
            }
            
        }
        
        return completeVector;
    }
    
    
    /**
     * Defining the validate method of Controller interface
     *  for validation check on the UI form
     *@throws  CoeusUIException
     */
    
    public boolean validate() throws CoeusUIException {
        
        return true;
    }
    
    /**
     * To get the Date Utils instance
     */
    
    public DateUtils getDateUtilsInstance() {
        return new DateUtils();
    }
    
    /**
     * To format the fields based on the functiontype
     */
    
    public void formatFields() {
        
        //if the Form is open in display mode
        if(getFunctionType() == DISPLAY_MODE) {
            proposalRateForm.btnOK.setEnabled(false);
            proposalRateForm.btnSync.setEnabled(false);
            proposalRateForm.btnReset.setEnabled(false);
            //proposalRateForm.txtInstApplicRatesValue.setEnabled(false);
            
            for(int indexVal=0;indexVal < vecProposalRateInstPanelBeans.size();indexVal++ ) {
                ProposalRateInstPanelBean propRateInstPanelBean =  (ProposalRateInstPanelBean) vecProposalRateInstPanelBeans.elementAt(indexVal);
                propRateInstPanelBean.txtApplicableRates.setEditable(false);
            }
            
            for(int indexVal=0;indexVal < vecProposalRateLAPanelBeans.size();indexVal++ ) {
                ProposalRateLAPanelBean propRateLAPanelBean =  (ProposalRateLAPanelBean) vecProposalRateLAPanelBeans.elementAt(indexVal);
                propRateLAPanelBean.txtApplicableRates.setEditable(false);
            }
            
            //proposalRateForm.btnCancel.requestFocus();
            
        } else {
            //COEUSQA-1689 Role Restrictions for Budget Rates - Start
            //proposalRateForm.btnOK.setEnabled(true);
            proposalRateForm.btnSync.setEnabled(true);
            proposalRateForm.btnReset.setEnabled(true);
            //proposalRateForm.btnCancel.requestFocus();
            try {
                // To check whether the user has the rights to modify proposal rates
                // If the user has the rights then the buttons are enabled else disabled
                if(hasRightToModifyProposalRates()){
                    proposalRateForm.btnOK.setEnabled(true);
                    //proposalRateForm.btnSync.setEnabled(true);
                    //proposalRateForm.btnReset.setEnabled(true);
                }else{
                    proposalRateForm.btnOK.setEnabled(false);
                    //proposalRateForm.btnSync.setEnabled(false);
                    //proposalRateForm.btnReset.setEnabled(false);                    
                }
            } catch (CoeusException ex) {
                ex.printStackTrace();
            }
            //COEUSQA-1689 Role Restrictions for Budget Rates - End
        }
    }
    
    
    /**
     * This will return the Vector of beans for Institute Tab Pane based on the functiontype
     * @return CoeusVector  Vector of InstituteRateBean
     *@param key Object represents the Primary Key
     *@return CoeusVector CoeusVector of Insititute Rate Beans
     */
    
    public CoeusVector getInstituteRateBean(Object key) {
        
        CoeusVector vectorOfBeans = null;
//        System.out.println("The function Type is : "+getFunctionType());
        if(mode == ADD_MODE ) {
            // ============ get values from the Institute Rate bean ===========
            try {
                instituteRatesBean =  new InstituteRatesBean();
                instituteRatesBean.setActivityCode(getActivityTypeCode());
                // Added & commented by chandra 17 Sept 2004 
//                vectorOfBeans = queryEngine.executeQuery(key,instituteRatesBean);
                vectorOfBeans = queryEngine.executeQuery(key,InstituteRatesBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
                // End chandra 17th Sept 2004
                vectBaseWindowInstRateBeans = null;
                
            }catch(Exception e){
                e.getMessage();
            }
        } else {
            try {
                proposalRatesBean =  new ProposalRatesBean();
                proposalRatesBean.setProposalNumber(getProposalNumber());
                proposalRatesBean.setVersionNumber(getVersionNumber());
                
                Equals equalsActype = new Equals(ACTYPE,null);
                NotEquals notequalsActype = new NotEquals(ACTYPE,TypeConstants.DELETE_RECORD);
                Or actypeBoth = new Or(equalsActype,notequalsActype);
                vectorOfBeans = queryEngine.executeQuery(key,proposalRatesBean.getClass(),actypeBoth);
                
                vectBaseWindowInstRateBeans = vectorOfBeans;
                
            }catch(Exception e){
                e.getMessage();
            }
            
        }
        return vectorOfBeans;
    }
    
    /**
     * This will return the Vector of beans for LA Tab Pane based on the functiontype
     *@return CoeusVector  Vector of InstituteLARateBean
     *@param CoeusVector  CoeusVector of InstituteLARate Beans
     */
    public CoeusVector getInstituteLARateBean(Object key) {
        
        CoeusVector vectorOfBeans = null;
        if(getFunctionType() == ADD_MODE) {
            try {
                InstituteLARatesBean instituteLARatesBean = new InstituteLARatesBean();
                instituteLARatesBean.setUnitNumber(getUnitNumber());
                vectorOfBeans = queryEngine.executeQuery(key,instituteLARatesBean);
                vectBaseWindowLARateBeans = null;
                
            }catch(Exception e){
                e.getMessage();
            }
        }
        else {
            try {
                ProposalLARatesBean proposalLARatesBean =  new ProposalLARatesBean();
                proposalLARatesBean.setProposalNumber(getProposalNumber());
                proposalLARatesBean.setVersionNumber(getVersionNumber());
                Equals equalsActype = new Equals(ACTYPE,null);
                NotEquals notequalsActype = new NotEquals(ACTYPE,TypeConstants.DELETE_RECORD);
                Or actypeBoth = new Or(equalsActype,notequalsActype);
                vectorOfBeans = queryEngine.executeQuery(key,proposalLARatesBean.getClass(),actypeBoth);
                
                vectBaseWindowLARateBeans = vectorOfBeans;
                
            }catch(Exception e){
                e.getMessage();
            }
        }
        
        return vectorOfBeans;
    }
    
    /**
     * To set the Institute Rate Tab  with either InstituteRateBean or ProposalRatesBean
     * Based on the vector received from queryBean
     *@param CoeusVector  Vector of CoeusType
     */
    
    public void setupInstituteRateTabDetails(CoeusVector coeusVector){
        
        Hashtable eachRowPanel  = new Hashtable();
        //COEUSQA-1689 Role Restrictions for Budget Rates - Start
        boolean hasRight = false;
        try {
            // To check whether the user has the rights to modify proposal rates
            hasRight = hasRightToModifyProposalRates();
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
        //COEUSQA-1689 Role Restrictions for Budget Rates - End
        for ( int index = 0; index < coeusVector.size(); index ++ ){
            Object bean = null;
            String eachpanelGroupKey = null;
            
            if(coeusVector.elementAt(0).getClass() == instituteRatesBean.getClass()) {
                bean = (InstituteRatesBean) coeusVector.elementAt(index);
                
                eachpanelGroupKey =(
                ((InstituteRatesBean) bean).getActivityCode()+""+
                ((InstituteRatesBean) bean).getRateClassCode()+
                ((InstituteRatesBean) bean).getRateTypeCode()
                );
            }
            if(coeusVector.elementAt(0).getClass() == proposalRatesBean.getClass()) {
                bean = (ProposalRatesBean) coeusVector.elementAt(index);
                
                eachpanelGroupKey = (
                ((ProposalRatesBean) bean).getProposalNumber()+
                ((ProposalRatesBean) bean).getVersionNumber()+
                ((ProposalRatesBean) bean).getActivityCode()+
                ((ProposalRatesBean) bean).getRateClassCode()+
                ((ProposalRatesBean) bean).getRateTypeCode());
                
            }
            
            
            JPanel pnlEachInstRatePanelGroup= new JPanel();
            pnlEachInstRatePanelGroup.setLayout(new javax.swing.BoxLayout(pnlEachInstRatePanelGroup, javax.swing.BoxLayout.Y_AXIS));
            
            if(!eachRowPanel.containsKey(eachpanelGroupKey)) {
                pnlEachInstRatePanelGroup.add(proposalRateForm.setInstRateClassPanelRows(bean));
                eachRowPanel.put(eachpanelGroupKey,eachpanelGroupKey);
            }
            
            ProposalRateInstPanelBean proposalRateInstPanelBean = new ProposalRateInstPanelBean(bean);
            
            if(getFunctionType() == DISPLAY_MODE) {
                proposalRateInstPanelBean.setFieldOption(true);
            }
            //COEUSQA-1689 Role Restrictions for Budget Rates - Start
            // To check whether the user has the rights to modify proposal rates
            else if(!hasRight){
                proposalRateInstPanelBean.setFieldOption(true);
            }
            //COEUSQA-1689 Role Restrictions for Budget Rates - End
            pnlEachInstRatePanelGroup.add(proposalRateInstPanelBean);
            proposalRateForm.pnlInstRateTabularPanel.add(pnlEachInstRatePanelGroup);
            vecProposalRateInstPanelBeans.add(proposalRateInstPanelBean);
            
        }
        
    }
    
    /**
     * To set the LA Rate Tab  with either InstituteLARateBean or ProposalLARatesBean
     * Based on the vector received from queryBean
     *@param CoeusVector  Vector of CoeusType
     */
    
    public void setupLARateTabDetails(CoeusVector coeusVector) {
        
        Hashtable eachRowPanel  = new Hashtable();
        //COEUSQA-1689 Role Restrictions for Budget Rates - Start
        boolean hasRight = false;
        try {
            // To check whether the user has the rights to modify proposal rates
            hasRight = hasRightToModifyProposalRates();
        } catch (CoeusException ex) {
        }
        //COEUSQA-1689 Role Restrictions for Budget Rates - End
        
        for ( int index = 0; index < coeusVector.size(); index ++ ){
            Object bean = null;
            String eachpanelGroupKey = null;
            if(coeusVector.elementAt(0).getClass() == instituteLARatesBean.getClass()) {
                bean = (InstituteLARatesBean) coeusVector.elementAt(index);
                
                eachpanelGroupKey =(
                ((InstituteLARatesBean) bean).getUnitNumber() +
                ((InstituteLARatesBean) bean).getRateClassCode() +
                ((InstituteLARatesBean) bean).getRateTypeCode()
                );
            }
            if(coeusVector.elementAt(0).getClass() == proposalLARatesBean.getClass()) {
                bean = (ProposalLARatesBean) coeusVector.elementAt(index);
                
                eachpanelGroupKey = (
                ((ProposalLARatesBean) bean).getProposalNumber()+
                ((ProposalLARatesBean) bean).getVersionNumber()+
                ((ProposalLARatesBean) bean).getRateClassCode()+
                ((ProposalLARatesBean) bean).getRateTypeCode());
            }
            
            JPanel pnlEachInstRatePanelGroup= new JPanel();
            pnlEachInstRatePanelGroup.setLayout(new javax.swing.BoxLayout(pnlEachInstRatePanelGroup, javax.swing.BoxLayout.Y_AXIS));
            
            if(!eachRowPanel.containsKey(eachpanelGroupKey)) {
                pnlEachInstRatePanelGroup.add(proposalRateForm.setLARateClassPanelRows(bean));
                eachRowPanel.put(eachpanelGroupKey,bean);
            }
            ProposalRateLAPanelBean proposalRateLAPanelBean = new ProposalRateLAPanelBean(bean);
            
            if(getFunctionType() == DISPLAY_MODE) {
                proposalRateLAPanelBean.setFieldOption(true);
            }
            //COEUSQA-1689 Role Restrictions for Budget Rates - Start
            // To check whether the user has the rights to modify proposal rates
            else if(!hasRight){
                proposalRateLAPanelBean.setFieldOption(true);
            }
            //COEUSQA-1689 Role Restrictions for Budget Rates - End
            
            pnlEachInstRatePanelGroup.add(proposalRateLAPanelBean);
            proposalRateForm.pnlLATabularPanel.add(pnlEachInstRatePanelGroup);
            vecProposalRateLAPanelBeans.add(proposalRateLAPanelBean);
            
        }
        
        /*  The tab page has to be added only if the 
           *LA rates are present for that Department after sync button clicked.
           *Added by chandra - 18/03/2004 - start
           *bug Fix id GNBGT-DEF-720 
         */
        if(vecLARateBeans.size() > 0){
            proposalRateForm.tbdRatesTabPane.addTab("       LA       ", new javax.swing.ImageIcon(""), proposalRateForm.pnlLATabPane, "");
        }// Added by chandra - bug Id : 720  - 18/03/2004 - end
        
        
    }
    
    
    /** This method is to be defined to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form
     */
    public void setFormData(Object data) {
        
        budgetInfoBean = (BudgetInfoBean ) data;
        if(budgetInfoBean == null) {
            return;
        }
        
        try {
            proposalNumber = budgetInfoBean.getProposalNumber();
            versionNumber = budgetInfoBean.getVersionNumber();
            
            if(budgetInfoBean.getUnitNumber() != null && budgetInfoBean.getActivityTypeCode() > 0 ) {
                activityTypeCode =  budgetInfoBean.getActivityTypeCode();
                unitNumber =  budgetInfoBean.getUnitNumber();
            }
            
            CoeusVector vecBudgetInfoBean = queryEngine.executeQuery(
            (proposalNumber + versionNumber),
            budgetInfoBean);
            budgetInfoBean = (BudgetInfoBean) vecBudgetInfoBean.get(0);
            if(budgetInfoBean.getUnitNumber() != null && budgetInfoBean.getActivityTypeCode() > 0 ) {
                activityTypeCode =  budgetInfoBean.getActivityTypeCode();
                unitNumber =  budgetInfoBean.getUnitNumber();
            }
            budgetInfoBean.setUnitNumber(getUnitNumber());
            budgetInfoBean.setActivityTypeCode(activityTypeCode);
            
            budgetStartDate = budgetInfoBean.getStartDate();
            budgetEndDate = budgetInfoBean.getEndDate();
            proposalRatesBean.setProposalNumber(budgetInfoBean.getProposalNumber());
            proposalRatesBean.setVersionNumber(budgetInfoBean.getVersionNumber());
            proposalLARatesBean.setProposalNumber(budgetInfoBean.getProposalNumber());
            proposalLARatesBean.setVersionNumber(budgetInfoBean.getVersionNumber());
            
        } catch(Exception e) {
            e.getMessage();
        }
        
        String fieldNames [] ={
            RATECLASSCODE
            , RATETYPECODE
            , FISCALYEAR
            //, ONOFFCAMPUS
            //, STARTDATE
        };
        
        //here to set the Values to the Tab Pane
        String key =(proposalNumber+versionNumber);
        CoeusVector initialVectorofBeans = null;
        initialVectorofBeans = getInstituteRateBean(key);
        
        if(initialVectorofBeans != null && initialVectorofBeans.size() > 0) {
            
            if(initialVectorofBeans.elementAt(0).getClass() == instituteRatesBean.getClass()) {
                initialVectorofBeans = getFilteredVector(initialVectorofBeans);
            }
            
            //initialVectorofBeans.sort(RATECLASSCODE,true);
            initialVectorofBeans.sort(fieldNames,true);
            vecInstRateBeans = initialVectorofBeans;
        setupInstituteRateTabDetails(initialVectorofBeans);
            
        }
        
        initialVectorofBeans = getInstituteLARateBean(key);
        if(initialVectorofBeans != null && initialVectorofBeans.size() > 0) {
            if(initialVectorofBeans.elementAt(0).getClass() == instituteLARatesBean.getClass()) {
                initialVectorofBeans = getFilteredVector(initialVectorofBeans);
            }
            //initialVectorofBeans.sort(RATECLASSCODE,true);
            initialVectorofBeans.sort(fieldNames,true);
            vecLARateBeans = initialVectorofBeans;
            
            /*  The tab page has to be added only if the 
           *LA rates are present for that Department.
           *Added by chandra - 18/03/2004 - start
           *bug Fix id GNBGT-DEF-004 
         */
        if(vecLARateBeans.size() > 0){
            proposalRateForm.tbdRatesTabPane.addTab("       LA       ", new javax.swing.ImageIcon(""), proposalRateForm.pnlLATabPane, "");
        }// Added by chandra - 18/03/2004 - end
            
            setupLARateTabDetails(initialVectorofBeans);
            
        }
    }
    /** This method will be invoked from the parent component. When the Propsoal
     *is in Hierarchy and the selected one is the parent propsoal then this method
     *will be invoked in display mode to display the Propsoal Rates and InstituteLARates
     *data
     */
    public void setFormDataForHiearchy() {
        String proposalNumber = "";
        int versionNumber = -1;
        try{
            proposalNumber = (String)getDataObject().elementAt(0);
            versionNumber = ((Integer)getDataObject().elementAt(1)).intValue();
            // Get the BudgetInfo data
            CoeusVector cvBudgetInfo = getBudgetInfoFromServer(proposalNumber,versionNumber);
            BudgetInfoBean budgetInfoBean = (BudgetInfoBean) cvBudgetInfo.get(0);
            if(budgetInfoBean.getUnitNumber() != null && budgetInfoBean.getActivityTypeCode() > 0 ) {
                activityTypeCode =  budgetInfoBean.getActivityTypeCode();
                unitNumber =  budgetInfoBean.getUnitNumber();
            }
            budgetInfoBean.setUnitNumber(getUnitNumber());
            budgetInfoBean.setActivityTypeCode(activityTypeCode);
            
            budgetStartDate = budgetInfoBean.getStartDate();
            budgetEndDate = budgetInfoBean.getEndDate();
            proposalRatesBean.setProposalNumber(budgetInfoBean.getProposalNumber());
            proposalRatesBean.setVersionNumber(budgetInfoBean.getVersionNumber());
            proposalLARatesBean.setProposalNumber(budgetInfoBean.getProposalNumber());
            proposalLARatesBean.setVersionNumber(budgetInfoBean.getVersionNumber());
            
            String fieldNames [] ={
                RATECLASSCODE
                , RATETYPECODE
                , FISCALYEAR
            };
            /** Set the necessary paramter to get the server data for the Proposal
             *Rates Data and InstituteLARates data
             */
            
            Vector serverDataObject = new Vector();
            serverDataObject.add(0,proposalNumber);
            serverDataObject.add(1,new Integer(versionNumber));
            serverDataObject.add(2,budgetInfoBean.getUnitNumber());
            serverDataObject.add(3,new Integer(budgetInfoBean.getActivityTypeCode()));
            
            HashMap hmRatesData = getRatesDataFromServer(serverDataObject);
            //here to set the Values to the Tab Pane
            // Get the Proposal Rates Data
            CoeusVector initialVectorofBeans = (CoeusVector)hmRatesData.get(ProposalRatesBean.class);

            if(initialVectorofBeans != null && initialVectorofBeans.size() > 0) {
                
                if(initialVectorofBeans.elementAt(0).getClass() == instituteRatesBean.getClass()) {
                    initialVectorofBeans = getFilteredVector(initialVectorofBeans);
                }
                initialVectorofBeans.sort(fieldNames,true);
                vecInstRateBeans = initialVectorofBeans;
                setupInstituteRateTabDetails(initialVectorofBeans);
            }
            
            // Get the InstituteLARates data and set to the compoent
            initialVectorofBeans = (CoeusVector)hmRatesData.get(InstituteLARatesBean.class);
            if(initialVectorofBeans != null && initialVectorofBeans.size() > 0) {
                if(initialVectorofBeans.elementAt(0).getClass() == instituteLARatesBean.getClass()) {
                    initialVectorofBeans = getFilteredVector(initialVectorofBeans);
                }
                initialVectorofBeans.sort(fieldNames,true);
                vecLARateBeans = initialVectorofBeans;
                if(vecLARateBeans.size() > 0){
                    proposalRateForm.tbdRatesTabPane.addTab(
                    "       LA       ", new javax.swing.ImageIcon(""), proposalRateForm.pnlLATabPane, "");
                    setupLARateTabDetails(initialVectorofBeans);
                    
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
        
    }
     /** This method will be invoked when the Propsoal is in Hieracrchy and 
     *the Propsoal is Parent. Get the BudgetInfo data for the selected
     *Node
     *@param propsoalNumber, versionNumber
      *@returns CoeusVector containing the BudgetInfoBean
      */
    private CoeusVector getBudgetInfoFromServer(String propsoalNumber, int versionNumber)throws Exception{
        RequesterBean request = new RequesterBean();
       CoeusVector data = null;
       CoeusVector cvData = new CoeusVector();
       cvData.addElement(propsoalNumber);
       cvData.addElement(new Integer(versionNumber));
       request.setDataObjects(cvData);
       request.setFunctionType(GET_BUDGET_INFO);
       AppletServletCommunicator comm = 
        new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL+SERVLET, request);
       comm.send();
       ResponderBean response = comm.getResponse();
       if(response.isSuccessfulResponse()){
           return (CoeusVector)response.getDataObjects();
       }else {
           throw new Exception(response.getMessage());
       }
    }
    
    /** Get the Proposal Rates data data from the server when it is opening from the Proposal 
      *Hiearchy.Get the data for the  selected node which contains Propsoal Number
      *and Version number
      *@param Propsoal Number, VersionNumber
      *@returns HashMap contains the Propsoal Rates data
      */
    private HashMap getRatesDataFromServer(Vector dataObject) throws Exception{
       RequesterBean request = new RequesterBean();
       request.setDataObjects(dataObject);
       request.setFunctionType(GET_BUDGET_RATES);
       AppletServletCommunicator comm = 
        new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL+SERVLET, request);
       comm.send();
       ResponderBean response = comm.getResponse();
       if(response.isSuccessfulResponse()){
           return (HashMap)response.getDataObject();
       }else {
           throw new Exception(response.getMessage());
       }
    }
    
    /** defined to returns the form data
     * @return returns the form data
     */
    public Object getFormData() {
        
        //here to get all the values from the Form
        return proposalRateForm;
    }
    
    /** returns the Component which is being controlled by this Controller.
     * @return Component which is being controlled by this Controller here
     *         it is proposalRateForm instance
     */
    
    public Component getControlledUI() {
        return proposalRateForm;
    }
    
    /**
     * registers GUI Components with event Listeners.
     */
    
    public void registerComponents() {
        
        ( (ProposalRateForm) getControlledUI()).btnOK.addActionListener(this);
        ( (ProposalRateForm) getControlledUI()).btnCancel.addActionListener(this);
        ( (ProposalRateForm) getControlledUI()).btnReset.addActionListener(this);
        ( (ProposalRateForm) getControlledUI()).btnSync.addActionListener(this);
        
        // Travel all the components while pressing tab button
        java.awt.Component[] components = {
            ( (ProposalRateForm) getControlledUI()).btnOK,
            ( (ProposalRateForm) getControlledUI()).btnCancel,
            ( (ProposalRateForm) getControlledUI()).btnReset,
            ( (ProposalRateForm) getControlledUI()).btnSync,
            ( (ProposalRateForm) getControlledUI()).txtInstApplicRatesValue
        };
      
        ProposalRateFocusTraversal traversePolicy = new ProposalRateFocusTraversal( components );
        proposalRateForm.pnlProposalRateMainPanel.setFocusTraversalPolicy(traversePolicy);
        proposalRateForm.pnlProposalRateMainPanel.setFocusCycleRoot(true);
                
        ( (ProposalRateForm) getControlledUI()).dlgProposalRateForm.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent ke){
                if(ke.getKeyCode() == KeyEvent.VK_ESCAPE){
                    ( (ProposalRateForm) getControlledUI()).requestFocus();
                    try{
                        checkBeforeClosing();
                    }catch (CoeusClientException coeusClientException){
                        CoeusOptionPane.showDialog(coeusClientException);
                    }
                    
                }
            }
        });
        
        ((ProposalRateForm) getControlledUI()).dlgProposalRateForm.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                checkOnCancel();
            }
        });
        
        ( (ProposalRateForm) getControlledUI()).dlgProposalRateForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        
        
        ( (ProposalRateForm) getControlledUI()).dlgProposalRateForm.addWindowListener(new java.awt.event.WindowAdapter() {
            
//            public void windowOpening(java.awt.event.WindowEvent evt){
//                ProposalRateInstPanelBean proposalRateInstPanelBean=null;
//                 proposalRateInstPanelBean.setFocusInComp();
//            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                ( (ProposalRateForm) getControlledUI()).dlgProposalRateForm.requestFocus();
                checkOnCancel();
                //checkBeforeClosing();
                
            }
        });
        
        
    }
    
    /**
     * Check for any change before closing the Window using Close Button on Title Bar
     */
    
    private void checkBeforeClosing() throws CoeusClientException {
        if(!isTabRateChanged()) {
            ( (ProposalRateForm) getControlledUI()).dlgProposalRateForm.dispose();
        }
        else
            performAction(confirmButtonAction(SAVE_MESSAGE),OK_ACTION_CODE);
        
    }
    
    /**
     * Displays the Form which is being controlled.
     */
    public void display() {
        String displayTitleValue = "";
        
        if(getFunctionType() == DISPLAY_MODE) {
            displayTitleValue = "Display";
        }
        else
            displayTitleValue = "Modify";
        proposalRateForm.setTitle(proposalRateForm.getFormTitle(
        budgetInfoBean.getProposalNumber(),budgetInfoBean.getVersionNumber()+"",displayTitleValue));
        ( (ProposalRateForm) getControlledUI()).dlgProposalRateForm.setVisible(true);
    }
    
    public int displayRates() {
        String displayTitleValue = "";
            if(getFunctionType() == DISPLAY_MODE) {
                displayTitleValue = "Display";
            }
            else
                displayTitleValue = "Modify";
            proposalRateForm.setTitle(proposalRateForm.getFormTitle(
            budgetInfoBean.getProposalNumber(),budgetInfoBean.getVersionNumber()+"",displayTitleValue));
            ( (ProposalRateForm) getControlledUI()).dlgProposalRateForm.setVisible(true);
            return clicked;
    }
    
    /**
     * Getter for property dataObject.
     * @return Value of property dataObject.
     */
    public java.util.Vector getDataObject() {
        return dataObject;
    }    
    
    /**
     * Setter for property dataObject.
     * @param dataObject New value of property dataObject.
     */
    public void setDataObject(java.util.Vector dataObject) {
        this.dataObject = dataObject;
    }
    
    /**
     * Getter for property calculationChanged.
     * @return Value of property calculationChanged.
     */
    public boolean isCalculationChanged() {
        return calculationChanged;
    }
    
    /**
     * Setter for property calculationChanged.
     * @param calculationChanged New value of property calculationChanged.
     */
    public void setCalculationChanged(boolean calculationChanged) {
        this.calculationChanged = calculationChanged;
    }
    
    //Bug Fix  - START
    class ProposalRateFocusTraversal extends ScreenFocusTraversalPolicy {
        int min, max, visibleHeight, visibleMax;
        
        ProposalRateFocusTraversal(Component screenComponents[]) {
            super(screenComponents);
            max = proposalRateForm.scrPnInstTabDataPanel.getVerticalScrollBar().getMaximum();
            min = proposalRateForm.scrPnInstTabDataPanel.getVerticalScrollBar().getMinimum();
            visibleHeight = proposalRateForm.scrPnInstTabDataPanel.getHeight();
        }
        
        public Component getComponentBefore(Container container, Component component) {
            Component before = super.getComponentBefore(container, component);
            Container c1 = before.getParent();
            Container c2 = null;
            if(c1!= null){
             c2= c1.getParent();   
            }
            if(c2== null){
                c2=c1;
            }
            Rectangle rect = c2.getBounds();
            proposalRateForm.scrPnInstTabDataPanel.scrollRectToVisible(rect);
                if(rect.getY() > visibleHeight) {
            //Scroll to component
                visibleMax = proposalRateForm.scrPnInstTabDataPanel.getComponent(0).getHeight();
                int value = (max * (int)rect.getY()) / visibleMax;
                proposalRateForm.scrPnInstTabDataPanel.getVerticalScrollBar().setValue(value);
            }
            return before;
        }
        
        public Component getComponentAfter(Container container, Component component) {
            Component next = super.getComponentAfter(container, component);
            Container c2 = null;
            Container c1 = next.getParent();
            if(c1!= null){
                c2 = c1.getParent();
            }
            if(c2==null){
                c2=c1;
            }
            Rectangle rect = c2.getBounds();
            //rect.setSize(next.getSize());
            if(rect.getY() > visibleHeight) {
            
            //Scroll to component
                visibleMax = proposalRateForm.scrPnInstTabDataPanel.getComponent(0).getHeight();
                int value = (max * (int)rect.getY()) / visibleMax;
                proposalRateForm.scrPnInstTabDataPanel.getVerticalScrollBar().setValue(value);
            }
            return next;
        }
    }
    
    //Bug Fix  - END
    //COEUSQA-1689 Role Restrictions for Budget Rates - Start
    /**
     *     
     * To check if the user has rights to modify the proposal rates
     * @return boolean value for right     
     * @throws CoeusException if exception
     */
    private boolean hasRightToModifyProposalRates() throws CoeusException{
        String connectTo = CoeusGuiConstants.CONNECTION_URL + HIERARCHY_SERVLET;
        RequesterBean request = new RequesterBean();
        Boolean hasRight = null;        
        CoeusVector cvDataToServer = new CoeusVector();        
        cvDataToServer.add(budgetInfoBean.getProposalNumber());        
        cvDataToServer.add(getUnitNumber());        
        cvDataToServer.add(new Boolean(isParentProposal()));
        cvDataToServer.add(budgetInfoBean);        
        request.setDataObject(cvDataToServer);
        request.setFunctionType(CHECK_MODIFY_PROPOSAL_RATES_RIGHT);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null && response.isSuccessfulResponse()){
            hasRight = (Boolean)response.getDataObject();
        }else {
            throw new CoeusException(response.getMessage());
        }
        return hasRight;
    }
    //COEUSQA-1689 Role Restrictions for Budget Rates - End
    
}//end of class

