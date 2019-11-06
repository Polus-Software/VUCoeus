/*
 * @(#)BudgetMaintenanceServlet.java 1.0 9/26/03 8:11 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 17-OCT-2011
 * by Maharaja Palanichamy
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.budget.calculator.BudgetCalculator;
//import edu.mit.coeus.s2s.bean.BudgetSummaryDataBean;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;

//import edu.mit.coeus.moduleparameters.bean.ParameterBean;
//import edu.mit.coeus.moduleparameters.parser.ProcessParameterXML;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.budget.calculator.bean.ValidCalcTypesBean;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.utils.xml.bean.budget.generator.BudgetStream;
import edu.mit.coeus.propdev.bean.ProposalActionTxnBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.budget.report.ReportGenerator;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingException;
import edu.mit.coeus.rates.bean.RatesTxnBean;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.customelements.bean.CustomElementsDataTxnBean;
import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;
import edu.mit.coeus.customelements.bean.CustomElementsUsageBean;
//import edu.mit.coeus.budget.bean.ProjectIncomeBean;
import edu.mit.coeus.propdev.bean.ProposalHierarchyBean;
import edu.mit.coeus.propdev.bean.ProposalHierarchyTxnBean;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 *
 * @author Prasanna Kumar K.
 * @version :1.0 September 26, 2003 8:11 PM
 *
 */

public class BudgetMaintenanceServlet extends CoeusBaseServlet implements TypeConstants{
    
    private final char GET_ALL_BUDGETS = 'A';
    //Added by shiji for Right Checking - step 1 : start
    private final char GET_BUDGETS ='d';
    //Right Checking - step 1 : end
    private final char GET_BUDGET_FOR_PROPOSAL = 'B';
    private final char UPDATE_STATUS_FINAL_VERSION = 'C';
    private final char GET_APPOINTMENTS_FOR_PERSON = 'D';
    private final char USER_HAS_PROP_RIGHT = 'E';
    private final char GET_COST_ELEMENT_DETAILS = 'F';
    private final char GET_MASTER_DATA_TO_SYNC = 'G';
    private final char GET_VALID_OH_RATE_TYPE = 'H';
    private final char SAVE_BUDGET = 'S';
    private final char GET_COST_ELEMENT_LIST = 'I';
    private final char GET_VALID_RATE_TYPE_FOR_CE = 'J';
    private final char GET_RATES_FOR_NEW_VERSION = 'K';
    private final char CHECK_ACTIVITY_TYPE_CHANGED = 'L';
    private final char GET_RATE_TYPE_FOR_RATE_CLASS = 'M';
    private final char GET_VALID_OH_RATE_TYPE_FOR_CE = 'N';
    private final char RELEASE_BUDGET_LOCK = 'O';
    
    // Added by Shivakumar for Cost element
    private final char GET_COST_ELEMENTS_RATE_TYPES = 'P';
    private final char UPDATE_COST_ELEMENT_RATE_TYPES = 'Q';
    // Shivakumar -- End
    private final char GET_VALID_COST_ELEMENT_JOB_CODES = 'X';
    private final char UPDATE_VALID_CE_JOB_CODES = 'T';
    
    private final char GET_COST_ELEMENT_AND_BUDGET_CATEGORY = 'Y';
    private final char UPDATE_COST_ELEMENT = 'Z';
    
    private final char GET_CUSTOM_DATA_ELEMENTS = 'U';
    private final char DELETE_CUSTOM_ELEMENT = 'V';
    private final char UPDATE_CUSTOM_ELEMENTS = 'a';
    
    private final char GET_MODULE_AND_CODE_TABLE_DATA = 'W';
    private static final char GET_MODULAR_BUDGET_DATA = 'b';
    private static final char GET_PERSON_NAME = 'c';
    private static final char UPDATE_PERSONS = 'e';
    private static final char GET_BUD_PER_IN_NEW_MODE =  'g';
    /** Case #1801 :Parameterize Under-recovery and cost-sharing distribution Start 1
     */
    private static final char GET_BUD_PARAMETER_VALUE = 'f';
    
    private static final char GET_COST_SHARING_DISTRIBUTION = 'h';
    private static final char GET_BUDGET_PERIOD = 'i';
    private static final char GET_BUDGET_INFO = 'j';
    private static final char GET_PROPOSAL_DATES = 'k';
    private static final char GET_BUDGET_RATES = 'l';
    private static final char GET_ALL_APPOINTMENTS_FOR_PERSON = 'm';
    //For Budget Modular Enhancement case #2087 start 1
    private static final char SYNC_MODULAR_BUDGET = 'q';
    // Update the budget with Modular Budget data which is in hierarchy
    private static final char UPDATE_BUDGET_IN_HIERARCHY = 'u';
    //For Budget Modular Enhancement case #2087 start 1
    
    /** Case #1801 :Parameterize Under-recovery and cost-sharing distribution End 1
     */
    //Rights
    private static final String VIEW_ANY_PROPOSAL = "VIEW_ANY_PROPOSAL";
    //Added by shiji for Right Checking - step 2 : start
    private static final String MODIFY_ANY_PROPOSAL = "MODIFY_ANY_PROPOSAL";
    //Right Checking - step 2 : end
    
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
    private static final char GET_INACTIVE_COST_ELEMENTS = '3';
    private static final char GET_COST_ELEMENTS = '4';
    //Added for COEUSQA-3273  - end
    
    //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
    private static final char GET_APPOINTMENT_AND_PERIOD_TYPES = '9';
    private static final char GET_APPOINTMENT_AND_PERIOD_DETAILS = '6';
    private static final char GET_INACTIVE_APPOINT_AND_PERIOD_TYPES = '7';
    private static final char GET_APP_AND_PER_TYPES_FOR_BUDGET_COPY = '8';
    //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end
    
    private static final String VIEW_BUDGET = "VIEW_BUDGET";
    private static final String MODIFY_BUDGET  = "MODIFY_BUDGET";
    
    private static final String BUDGET_PERIOD = "Period ";
    //Added for bug fixed for case #2354 start 1
    private static final char GET_VALID_JOB_CODES_FOR_CE = 't';
    private static final char GET_JOB_CODE_VALIDATION_ENABLED = 'v';
    //Added for bug fixed for case #2354 end 1
    //Added for case #2372 start 1
    private static final char GET_BUDGET_INFO_FOR_COPY = 'w';
    // To get Budget TBA persons
    private static final char GET_TBA_PERSONS = 'p';
    // To get TBA person id
    private static final char GET_TBA_PERSON_ID = 'r';
    //Added for case #3121 start
    private static final char GET_COST_ELEMENT_PERIOD = 'x';
    private static final char GET_COST_ELEMENT_RATE = 'z';
    //Added for case #3121 start
    //Added for case 2158: Budgetary Validations
    private static final char CHECK_BUDGET_VERSION = 'y';
    //2158 End
    //Added for Case 3197 - Allow for the generation of project period greater than 12 months -start
    private static final char GET_NUMBER_OF_MONTHS = 's';
    //Added for Case 3197 - end
    //Added for case #2372 end 1
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
    private final char GET_PERIOD_TYPE ='n';
    private final char GET_ACTIVE_PERIOD_TYPE ='o';
    private final char GET_APPOINTMENT_TYPE ='1';
    private final char GET_ACTIVE_APPOINTMENT_TYPE ='2';
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
    private final char CALCULATE_BASE_SALARY_FOR_PERIOD ='5';
    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
    private final char GET_UNIT_FORMUALTED_TYPES = '~';
    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
    
    /**
     *  This method is used for applets.
     *  Post the information into server using object serialization.
     */
    public void doPost(HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        
        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = new ResponderBean();
        
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        
        String loggedinUser ="";
        String unitNumber = "";
        
        try {
            // get an input stream
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            // get the user
            UserInfoBean userBean = (UserInfoBean)new
            UserDetailsBean().getUserInfo(requester.getUserName());
            
            BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
            
            //Should always be the User Id with which user logged in - March 20, 2004
            //loggedinUser = userBean.getUserId();
            loggedinUser = requester.getUserName();
            unitNumber = userBean.getUnitNumber();
            
            // keep all the beans into vector
            Vector dataObjects = new Vector();
            
            char functionType = requester.getFunctionType();
            //BudgetCollectionBean
            BudgetInfoBean budgetInfoBean = null;
            String proposalNumber="";
            int versionNumber;
            int activityTypeCode;
            /* Get All Budgets*/
            if (functionType == GET_ALL_BUDGETS) {
                CoeusVector coeusVector = null;
                proposalNumber = (String)requester.getId();
                
                Boolean lockBudget = (Boolean)requester.getDataObject();
                
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                /* Check if the user has any OSP right
                 *Case # 1856
                 */
                boolean hasRight = userMaintDataTxnBean.getUserHasAnyOSPRight(loggedinUser);
                //Check OSP Right VIEW_ANY_PROPOSAL
                if(!hasRight){
                    hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, VIEW_ANY_PROPOSAL);
                }
                //If not present check VIEW_BUDGET
                if(!hasRight){
                    hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, VIEW_BUDGET);
                    //If not present check MODIFY_BUDGET
                    if(!hasRight){
                        hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, MODIFY_BUDGET);
                    }
                }
                //If User has atleast one of the above rights
                if(hasRight){
                    //Check whether Proposal can be modified
                    ProposalActionTxnBean proposalActionTxnBean = new ProposalActionTxnBean();
                    int canModify = proposalActionTxnBean.canModifyDevelopmentProposal(proposalNumber);
                    if(canModify > 0){
                        //If can Modify, check whether user has MODIFY_BUDGET right
                        hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, MODIFY_BUDGET);
                    }else{
                        hasRight = false;
                    }
                    //Get all budgets for this proposal
                    //Lock all Budgets for this Proposal
                    coeusVector = budgetDataTxnBean.getBudgetForProposal(proposalNumber);
                    
                    //Get Proposal Development Details
                    ProposalDevelopmentFormBean proposalDevelopmentFormBean = budgetDataTxnBean.getProposalDetailsForBudget(proposalNumber);
                    
                    //Protocol Enhancment - Parameterize
                    //Under-recovery and cost-sharing distribution Start 1
                    CoeusFunctions coeusFunctions = new CoeusFunctions();
                    
                    boolean validateCostShr = false;
                    String parmCost = coeusFunctions.getParameterValue(CoeusConstants.FORCE_COST_SHARING_DISTRIBUTION);
                    if(parmCost == null || parmCost.trim().equals("0")){
                        validateCostShr = false;
                    }else if(parmCost.trim().equals("1")){
                        validateCostShr = true;
                    }
                    
                    boolean validateUndrRec = false;
                    String parmUnder = coeusFunctions.getParameterValue(CoeusConstants.FORCE_UNDER_RECOVERY_DISTRIBUTION);
                    if(parmUnder == null || parmUnder.trim().equals("0")){
                        validateUndrRec = false;
                    }else if(parmUnder.trim().equals("1")){
                        validateUndrRec = true;
                    }
                    //Protocol Enhancment - Parameterize
                    //Under-recovery and cost-sharing distribution End 1
                    
                    
                    dataObjects = new Vector();
                    
                    dataObjects.addElement(coeusVector);
                    dataObjects.addElement(proposalDevelopmentFormBean);
                    dataObjects.addElement(new Boolean(hasRight));
                    
                    //Protocol Enhancment - Parameterize
                    //Under-recovery and cost-sharing distribution Start 2
                    dataObjects.addElement(new Boolean(validateCostShr));
                    dataObjects.addElement(new Boolean(validateUndrRec));
                    //Protocol Enhancment - Parameterize
                    //Under-recovery and cost-sharing distribution End 2
                    
                    responder.setResponseStatus(true);
                    responder.setDataObjects(dataObjects);
                    responder.setMessage(null);
                    //Now try to get Lock only if select budget is not opened in Display mode.
                    //Budget Data is required to display in Display mode
                    //even if Budget is locked by some other User
                    if(lockBudget.booleanValue()==true && hasRight == true){
                        //Method commented by Shivakumar as this method has been modified for locking enhancement.
                        //                        budgetDataTxnBean.getBudgetLock(proposalNumber);
                        // Code added by Shivakumar for locking enhancement
                        // Code added by Shivakumar -BEGIN
                        LockingBean lockingBean = budgetDataTxnBean.getBudgetLock(proposalNumber,loggedinUser,unitNumber);
                        boolean isAvailable = lockingBean.isGotLock();
                        if(isAvailable){
                            try{
                                budgetDataTxnBean.transactionCommit();
                                responder.setLockingBean(lockingBean);
                            }catch(DBException dbEx){
                                dbEx.printStackTrace();
                                budgetDataTxnBean.transactionRollback();
                                throw dbEx;
                            }finally{
                                budgetDataTxnBean.endConnection();
                            }
                        }
                        // Code added by Shivakumar -END
                    }
                }else{
                    //Message
                    throw new CoeusException("budgetAuthorization_exceptionCode.4200");
                }
                //Modofied for bug id 1856 : start
                //Added by shiji for Right Checking - step 3 : start
            }else if (functionType == GET_BUDGETS) {
                CoeusVector coeusVector = null;
                proposalNumber = (String)requester.getId();
                
                Boolean lockBudget = (Boolean)requester.getDataObject();
                
                Vector data=(Vector)requester.getDataObjects();
                boolean isBudgOpenedFromBaseWindow = ((Boolean)data.get(0)).booleanValue();
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                //Get Proposal Development Details
                ProposalDevelopmentFormBean proposalDevelopmentFormBean = budgetDataTxnBean.getProposalDetailsForBudget(proposalNumber);
                int statusCode = proposalDevelopmentFormBean.getCreationStatusCode();
                String leadUnit=null;
//                String topLevelUnit=null;
                boolean hasRight= false;
                boolean hasViewRightOnly=false;
                //unitNumber=(String)data.get(1);
                leadUnit=(String)data.get(1);
                //If Budget is opened from base window
                // if(isBudgOpenedFromBaseWindow) {
//                ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                //Check user has MODIFY_ANY_PROPOSAL right at lead unit
                
                hasRight=userMaintDataTxnBean.getUserHasAnyOSPRight(loggedinUser);
                if(hasRight) {
                    //COEUSQA-1433 - Allow Recall from Routing - Start
                    //if(statusCode != 1 && statusCode != 3) {
                    if(statusCode != 1 && statusCode != 3 && statusCode != 8) {
                        //COEUSQA-1433 - Allow Recall from Routing - End
                        hasViewRightOnly = true;
                    }
                }
                
                hasRight=userMaintDataTxnBean.getUserHasRight(loggedinUser,MODIFY_ANY_PROPOSAL,leadUnit);
                if(hasRight) {
                    //COEUSQA-1433 - Allow Recall from Routing - Start
                    //if(statusCode != 1 && statusCode != 3) {
                    if(statusCode != 1 && statusCode != 3 && statusCode != 8) {
                        //COEUSQA-1433 - Allow Recall from Routing - End
                        hasViewRightOnly = true;
                    }
                }
                //If not check user has MODIFY_BUDGET right
                else if(!hasRight){
                    hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, MODIFY_BUDGET);
                    if(hasRight) {
                        //COEUSQA-1433 - Allow Recall from Routing - Start
                        //if(statusCode != 1 && statusCode != 3) {
                        if(statusCode != 1 && statusCode != 3 && statusCode != 8) {
                            //COEUSQA-1433 - Allow Recall from Routing - End
                            hasViewRightOnly = true;
                        }
                    }
                    //If not present user has VIEW_ANY_PROPOSAL right at lead unit level
                    else if(!hasRight) {
                        hasRight=userMaintDataTxnBean.getUserHasRight(loggedinUser,VIEW_ANY_PROPOSAL,leadUnit);
                        if(hasRight) {
                            hasViewRightOnly=true;
                        }
                        //If not check user has VIEW_BUDGET right
                        else if(!hasRight) {
                            hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, VIEW_BUDGET);
                            if(hasRight) {
                                hasViewRightOnly=true;
                            }
                            //IF user has any OSP right, and the proposal status is (2:Approval In Progress,4: Approved,5: Submitted,
                            //6. Post-Submission Approval or 7. Post-Submission Rejection).
                            else if(!hasRight) {
                                if(statusCode == 2 || statusCode== 4 || statusCode == 5 || statusCode == 6 || statusCode == 7) {
                                    hasRight =  userMaintDataTxnBean.getUserHasAnyOSPRight(loggedinUser);
                                    if(hasRight) {
                                        hasViewRightOnly=true;
                                    }
                                }
                            }
                        }
                    }
                }
                //}
                //                if(!isBudgOpenedFromBaseWindow) {
                //                    if(isDisplayMode) {
                //                        hasViewRightOnly=true;
                //                    }else {
                //                        hasViewRightOnly=false;
                //                    }
                //                    hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, MODIFY_BUDGET);
                //                    if(!hasRight) {
                //                        hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, VIEW_BUDGET);
                //                        if(hasRight) {
                //                            hasViewRightOnly=true;
                //                        }
                //                    }
                //}
                //If User has atleast one of the above rights
                if(hasRight){
                    if(!hasViewRightOnly) {
                        //Check whether Proposal can be modified
                        ProposalActionTxnBean proposalActionTxnBean = new ProposalActionTxnBean();
                        int canModify = proposalActionTxnBean.canModifyDevelopmentProposal(proposalNumber);
                        if(canModify <= 0){
                            hasViewRightOnly=true;
                        }
                    }
                    
                    //Get all budgets for this proposal
                    //Lock all Budgets for this Proposal
                    coeusVector = budgetDataTxnBean.getBudgetForProposal(proposalNumber);
                    //Protocol Enhancment - Parameterize
                    //Under-recovery and cost-sharing distribution Start 1
                    CoeusFunctions coeusFunctions = new CoeusFunctions();
                    
                    boolean validateCostShr = false;
                    String parmCost = coeusFunctions.getParameterValue(CoeusConstants.FORCE_COST_SHARING_DISTRIBUTION);
                    if(parmCost == null || parmCost.trim().equals("0")){
                        validateCostShr = false;
                    }else if(parmCost.trim().equals("1")){
                        validateCostShr = true;
                    }
                    
                    boolean validateUndrRec = false;
                    String parmUnder = coeusFunctions.getParameterValue(CoeusConstants.FORCE_UNDER_RECOVERY_DISTRIBUTION);
                    if(parmUnder == null || parmUnder.trim().equals("0")){
                        validateUndrRec = false;
                    }else if(parmUnder.trim().equals("1")){
                        validateUndrRec = true;
                    }
                    //Protocol Enhancment - Parameterize
                    //Under-recovery and cost-sharing distribution End 1
                    
                    //Proposal Hierarchy Enhancment Start
                    // gnprh - Commented for Propsoal Hierarchy - start
                    ProposalHierarchyBean proposalHierarchyBean = null;
                    HashMap hmData = null;
                    if(isBudgOpenedFromBaseWindow){
                        ProposalHierarchyTxnBean txn = new ProposalHierarchyTxnBean();
                        hmData = txn.getParentProposalData(proposalNumber);
                        String rootProposal = (String)hmData.get("PARENT_PROPOSAL");
                        if(rootProposal != null){
                            proposalHierarchyBean = txn.getHierarchyData(rootProposal);
                        }
                    }
                    //Proposal Hierarchy Enhancment End
                    
                    dataObjects = new Vector();
                    
                    dataObjects.addElement(coeusVector);
                    dataObjects.addElement(proposalDevelopmentFormBean);
                    dataObjects.addElement(new Boolean(hasRight));
                    
                    //Protocol Enhancment - Parameterize
                    //Under-recovery and cost-sharing distribution Start 2
                    dataObjects.addElement(new Boolean(validateCostShr));
                    dataObjects.addElement(new Boolean(validateUndrRec));
                    //Protocol Enhancment - Parameterize
                    //Under-recovery and cost-sharing distribution End 2
                    dataObjects.addElement(new Boolean(hasViewRightOnly));
                    
                    //Proposal Hierarchy Enhancment Start
                    // gnprh - Commeted for Proposal Hierarchy - start
                    if(isBudgOpenedFromBaseWindow){
                        //Proposal hierarchy related details
                        dataObjects.addElement(hmData);
                        //Proposal Hierarchy Tree data
                        dataObjects.addElement(proposalHierarchyBean);
                    }
                    //* End                
                    //Proposal Hierarchy Enhancment End
                    
                    responder.setResponseStatus(true);
                    responder.setDataObjects(dataObjects);
                    responder.setMessage(null);
                    //Now try to get Lock only if select budget is not opened in Display mode.
                    //Budget Data is required to display in Display mode
                    //even if Budget is locked by some other User
                    if(lockBudget.booleanValue()==true && hasRight == true &&hasViewRightOnly==false){
                        //Method commented by Shivakumar as this method has been modified for locking enhancement.
                        //                        budgetDataTxnBean.getBudgetLock(proposalNumber);
                        // Code added by Shivakumar for locking enhancement
                        // Code added by Shivakumar -BEGIN
                        LockingBean lockingBean = budgetDataTxnBean.getBudgetLock(proposalNumber,loggedinUser,unitNumber);
                        boolean isAvailable = lockingBean.isGotLock();
                        if(isAvailable){
                            try{
                                budgetDataTxnBean.transactionCommit();
                                responder.setLockingBean(lockingBean);
                            }catch(DBException dbEx){
                                dbEx.printStackTrace();
                                budgetDataTxnBean.transactionRollback();
                                throw dbEx;
                            }finally{
                                budgetDataTxnBean.endConnection();
                            }
                        }
                        // Code added by Shivakumar -END
                    }
                }else{
                    //Message
                    throw new CoeusException("budgetAuthorization_exceptionCode.4200");
                }
            }//Right Checking - step 3 : end
            //bug id 1856 : end
            else if(functionType == GET_BUDGET_FOR_PROPOSAL){
                //budgetInfoBean = (BudgetInfoBean)requester.getDataObject();
                Hashtable budget = (Hashtable)requester.getDataObject();
                Boolean isReleaseLock = (Boolean)budget.get(CoeusConstants.IS_RELEASE_LOCK);
                budgetInfoBean = (BudgetInfoBean)budget.get(BudgetInfoBean.class);
                
                budget = new Hashtable();
                // Getting the budget data from BudgetTxnBean - start
                BudgetTxnBean budgetTxnBean = new BudgetTxnBean();
                budget = budgetTxnBean.getBudgetData(budgetInfoBean);
                // Getting the budget data from BudgetTxnBean - End
                //Release lock if opened in Display mode
                if(isReleaseLock.booleanValue()){
                    // Commented by Shivakumar for locking enhancement
                    //                    budgetDataTxnBean.releaseEdit(budgetInfoBean.getProposalNumber());
                    // Code added by Shivakumar -BEGIN
                    //                    budgetDataTxnBean.releaseEdit(budgetInfoBean.getProposalNumber(), loggedinUser);
                    //                    Calling releaseLock method to fix the bug in Locking system
                    LockingBean lockingBean = budgetDataTxnBean.releaseLock(budgetInfoBean.getProposalNumber(), loggedinUser);
                    responder.setLockingBean(lockingBean);
                    // Code added by Shivakumar -END
                }
                
                responder.setResponseStatus(true);
                responder.setDataObject(budget);
                responder.setMessage(null);
            }else if(functionType == UPDATE_STATUS_FINAL_VERSION){
                //Update final version flag in OSP$BUDGET table
                CoeusVector coeusVector = null;
                Vector dataObject = (Vector)requester.getDataObjects();
                coeusVector = (CoeusVector)dataObject.elementAt(0);
                ProposalDevelopmentFormBean proposalDevelopmentFormBean = null;
                proposalDevelopmentFormBean = (ProposalDevelopmentFormBean)dataObject.elementAt(1);
                BudgetUpdateTxnBean budgetUpdateTxnBean = new BudgetUpdateTxnBean(loggedinUser);
                boolean success = budgetUpdateTxnBean.updateBudgetFinalVersion(coeusVector, proposalDevelopmentFormBean);
                //Proposal Hierarchy Sync Enhancment by tarique Start
                proposalNumber = requester.getId();
                // gnprh - Commented for Proposal Hierarchy start
                ProposalHierarchyTxnBean propHierarchyTxnBean =
                new ProposalHierarchyTxnBean(loggedinUser);
                int count = propHierarchyTxnBean.updateSyncFlag(proposalNumber , "B");
                
                //Proposal Hierarchy Sync Enhancment by tarique End
                //Now release Budget lock
                // Commented by Shivakumar for locking enhancement
                //                budgetDataTxnBean.releaseEdit(proposalDevelopmentFormBean.getProposalNumber());
                // Code added by Shivakumar -BEGIN
                //                 budgetDataTxnBean.releaseEdit(proposalDevelopmentFormBean.getProposalNumber(),loggedinUser);
                // Calling the releaseLock method to fix the bug in locking system
                
                //COEUSQA-2546	Two users can modify a budget at same time. Locking not working -START
                // Check the budget status, if status is only Complete, then it allows to release the lock.
                // If budget status is Incomplete, will not allow release the budget lock.
                String budgetStatus = proposalDevelopmentFormBean.getBudgetStatus();
                if(budgetStatus != null && budgetStatus.equalsIgnoreCase("C") ){
                LockingBean lockingBean = budgetDataTxnBean.releaseLock(proposalDevelopmentFormBean.getProposalNumber(),loggedinUser);
                responder.setLockingBean(lockingBean);
                }
                //COEUSQA-2546	Two users can modify a budget at same time. Locking not working -END
                // Code added by Shivakumar -END
                
                proposalDevelopmentFormBean = budgetDataTxnBean.getProposalDetailsForBudget(proposalDevelopmentFormBean.getProposalNumber());
                responder.setDataObject(proposalDevelopmentFormBean);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_APPOINTMENTS_FOR_PERSON){
                CoeusVector coeusVector = null;
                String personId = (String)requester.getDataObject();
                coeusVector = budgetDataTxnBean.getAppointmentsForPerson(personId);
                responder.setDataObject(coeusVector);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == USER_HAS_PROP_RIGHT){
                Vector dataObject = (Vector)requester.getDataObjects();
                proposalNumber = (String)dataObject.elementAt(0);
                String rightId = (String)dataObject.elementAt(1);
                
                ProposalDevelopmentTxnBean proposalDevelopmentTxnBean
                = new ProposalDevelopmentTxnBean();
                int count = proposalDevelopmentTxnBean.getUserHasPropRightCount(loggedinUser, proposalNumber, rightId);
                responder.setDataObject(new Integer(count));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_COST_ELEMENT_DETAILS){
                String costElement = (String)requester.getDataObject();
                //COEUSQA-1414 Allow schools to indicate if cost element is still active - Start
                //CostElementsBean costElementsBean = budgetDataTxnBean.getCostElementsDetails(costElement);
                CostElementsBean costElementsBean = budgetDataTxnBean.getActiveCostElementsDetails(costElement);
                //COEUSQA-1414 Allow schools to indicate if cost element is still active - End
                responder.setDataObject(costElementsBean);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_COST_ELEMENT_LIST){
                DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
                Vector costElementList = departmentPersonTxnBean.getPersonBudgetDetails();
                responder.setDataObjects(costElementList);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_MASTER_DATA_TO_SYNC){
                budgetInfoBean = (BudgetInfoBean)requester.getDataObject();
                proposalNumber = budgetInfoBean.getProposalNumber();
                versionNumber = budgetInfoBean.getVersionNumber();
                
                unitNumber = budgetInfoBean.getUnitNumber();
                activityTypeCode = budgetInfoBean.getActivityTypeCode();
                dataObjects = new Vector();
                
                /*
                 * Updated by Geo on 16-Sep-2004
                 *  To pass the top level unit to get the institute rates
                 */
                RatesTxnBean ratesTxnBean = new RatesTxnBean();
                String topLevelUnitNum = ratesTxnBean.getTopLevelUnit(unitNumber);
                //Budget Institute Rate
                CoeusVector coeusVector = new CoeusVector();
                // Code commented by Shivakumar as we need to pass unit number
                // along with activity type code
                //                coeusVector = budgetDataTxnBean.getInstituteRates(activityTypeCode);
                /* The following method has been written for budget enhancement
                 */
                // Code added by Shivakumar - BEGIN
                /*
                 *  Changed by Geo to pass top level unit instead of proposal unit number
                 */
                coeusVector = budgetDataTxnBean.getInstituteRates(activityTypeCode, topLevelUnitNum);
                // Code added by Shivakumar - END
                dataObjects.addElement(coeusVector);
                
                //Budget Institute LA Rate
                coeusVector = new CoeusVector();
                coeusVector = budgetDataTxnBean.getInstituteLARates(unitNumber);
                dataObjects.addElement(coeusVector);
                
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_VALID_OH_RATE_TYPE){
                Vector vctCostElements = (Vector)requester.getDataObject();
                Hashtable validOHRateTypes = null;
                validOHRateTypes = budgetDataTxnBean.getValidCERateTypes(vctCostElements);
                
                responder.setDataObject(validOHRateTypes);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == SAVE_BUDGET){
                Hashtable budgetData = (Hashtable)requester.getDataObject();
                String mode = requester.getId();
                //Get the lock status, whether to release or retain lock
                Boolean releaseLock = (Boolean)budgetData.get(CoeusConstants.IS_RELEASE_LOCK);
                boolean updated = false;
                // Code added for locking bug fixing - BEGIN
                //                BudgetUpdateTxnBean budgetUpdateTxnBean = new BudgetUpdateTxnBean(loggedinUser);
                //                if(budgetData.containsKey(BudgetInfoBean.class)){
                //                    CoeusVector cvBudgetInfoBean = (CoeusVector)budgetData.get(BudgetInfoBean.class);
                //                    BudgetInfoBean budgetBeanTemp = (BudgetInfoBean)cvBudgetInfoBean.get(0);
                ////                    BudgetInfoBean budgetBeanTemp = (BudgetInfoBean)budgetData.get(BudgetInfoBean.class);
                //                    if((budgetBeanTemp.getAcType() != null) && (budgetBeanTemp.getAcType().equals("I"))){
                //                        updated = budgetUpdateTxnBean.addUpdDeleteBudget(budgetData);
                //                    }else{
                //                        boolean lockCheck = budgetDataTxnBean.lockCheck(budgetBeanTemp.getProposalNumber(), loggedinUser);
                //                        if(!lockCheck){
                //                            updated = budgetUpdateTxnBean.addUpdDeleteBudget(budgetData);
                //                        }else{
                //                            throw new LockingException(msg);
                //                        }
                //                    }
                //                }
                // Code added for locking bug fixing - END
                //For Case #1626 start
                BudgetUpdateTxnBean budgetUpdateTxnBean = new BudgetUpdateTxnBean(loggedinUser);
                budgetUpdateTxnBean.setMode(mode);
                updated = budgetUpdateTxnBean.addUpdDeleteBudget(budgetData);
                //For Case #1626 End
                //System.out.println("Updated budget : "+updated);
                
                CoeusVector budgetInfoCoeusVector = (CoeusVector)budgetData.get(BudgetInfoBean.class);
                budgetInfoBean = (BudgetInfoBean) budgetInfoCoeusVector.elementAt(0);
                budgetData = new Hashtable();
                // Getting the budget data from BudgetTxnBean - start
                BudgetTxnBean budgetTxnBean = new BudgetTxnBean();
                budgetData = budgetTxnBean.getBudgetData(budgetInfoBean);
                // Getting the budget data from BudgetTxnBean - End
                //Proposal Hierarchy Enhancment Start
                // gnprh - Commented for Proposal Hierarchy - start
                proposalNumber = budgetInfoBean.getProposalNumber();
                ProposalHierarchyTxnBean propHierarchyTxnBean =
                new ProposalHierarchyTxnBean(loggedinUser);
                int count = propHierarchyTxnBean.updateSyncFlag(proposalNumber , "B");
                //Proposal Hierarchy Enhancment End
                //*End commenting
                
                //Release lock
                //releaseLock = (Boolean)budgetData.get(CoeusConstants.IS_RELEASE_LOCK);
                if(releaseLock != null && releaseLock.booleanValue() == true){
                    // Commented by Shivakumar for locking enhancement
                    //                    budgetDataTxnBean.releaseEdit(budgetInfoBean.getProposalNumber());
                    // Code added by Shivakumar -BEGIN
                    //budgetDataTxnBean.releaseEdit(budgetInfoBean.getProposalNumber(),loggedinUser);
                    // Calling releaseLock method to fix the bug in new locking system
                    LockingBean lockingBean = budgetDataTxnBean.releaseLock(budgetInfoBean.getProposalNumber(),loggedinUser);
                    responder.setLockingBean(lockingBean);
                    // Code added by Shivakumar -END
                    
                }
                
                responder.setDataObject(budgetData);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_VALID_RATE_TYPE_FOR_CE){
                String costElement = (String)requester.getDataObject();
                CoeusVector validRateTypes = null;
                validRateTypes = budgetDataTxnBean.getValidCERateTypes(costElement);
                
                responder.setDataObject(validRateTypes);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_RATES_FOR_NEW_VERSION){
                budgetInfoBean = (BudgetInfoBean)requester.getDataObject();
                proposalNumber = budgetInfoBean.getProposalNumber();
                versionNumber = budgetInfoBean.getVersionNumber();
                unitNumber = budgetInfoBean.getUnitNumber();
                activityTypeCode = budgetInfoBean.getActivityTypeCode();
                dataObjects = new Vector();
                CoeusVector coeusVector = new CoeusVector();
                Hashtable newVersionRates = new Hashtable();
                
                //If this is not the first version
                if(versionNumber !=1){
                    coeusVector = new CoeusVector();
                    coeusVector = budgetDataTxnBean.getProposalInstituteRates(proposalNumber, versionNumber);
                    if(coeusVector==null){
                        coeusVector = budgetDataTxnBean.getProposalInstituteRates(proposalNumber, versionNumber-1);
                    }
                    if(coeusVector==null){
                        coeusVector = new CoeusVector();
                    }
                    newVersionRates.put(ProposalRatesBean.class, coeusVector);
                    
                    coeusVector = new CoeusVector();
                    coeusVector = budgetDataTxnBean.getProposalInstituteLARates(proposalNumber, versionNumber);
                    if(coeusVector==null){
                        coeusVector = budgetDataTxnBean.getProposalInstituteLARates(proposalNumber, versionNumber-1);
                    }
                    if(coeusVector==null){
                        coeusVector = new CoeusVector();
                    }
                    newVersionRates.put(ProposalLARatesBean.class, coeusVector);
                }
                
                /*
                 * Updated by Geo on 16-Sep-2004
                 *  To pass the top level unit to get the institute rates
                 */
                RatesTxnBean ratesTxnBean = new RatesTxnBean();
                String topLevelUnitNum = ratesTxnBean.getTopLevelUnit(unitNumber);
                //System.out.println("Top level unit=>"+topLevelUnitNum);
                
                //Budget Institute Rate
                //                coeusVector = budgetDataTxnBean.getInstituteRates(activityTypeCode);
                //                coeusVector = budgetDataTxnBean.getInstituteRates(activityTypeCode, unitNumber);
                coeusVector = budgetDataTxnBean.getInstituteRates(activityTypeCode, topLevelUnitNum);
                if(coeusVector==null){
                    coeusVector = new CoeusVector();
                }
                newVersionRates.put(InstituteRatesBean.class, coeusVector);
                
                //Budget Institute LA Rate
                coeusVector = new CoeusVector();
                coeusVector = budgetDataTxnBean.getInstituteLARates(unitNumber);
                if(coeusVector==null){
                    coeusVector = new CoeusVector();
                }
                newVersionRates.put(InstituteLARatesBean.class, coeusVector);
                
                responder.setDataObject(newVersionRates);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == CHECK_ACTIVITY_TYPE_CHANGED){
                budgetInfoBean = (BudgetInfoBean)requester.getDataObject();
                proposalNumber = budgetInfoBean.getProposalNumber();
                versionNumber = budgetInfoBean.getVersionNumber();
                activityTypeCode = budgetDataTxnBean.getActivityForBudgetVersion(proposalNumber, versionNumber);
                if(activityTypeCode==0){
                    activityTypeCode = budgetDataTxnBean.getActivityForBudgetVersion(proposalNumber, versionNumber-1);
                }
                Hashtable budgetData = new Hashtable();
                BudgetTxnBean budgetTxnBean = null;
                if(budgetInfoBean.getActivityTypeCode() == activityTypeCode){
                    // Getting the budget data from BudgetTxnBean - start
                    budgetTxnBean = new BudgetTxnBean();
                    budgetData = budgetTxnBean.getBudgetData(budgetInfoBean);
                    // Getting the budget data from BudgetTxnBean - End
                    responder.setDataObject(budgetData);
                }else{
                    budgetInfoBean.setActivityTypeCode(activityTypeCode);
                    dataObjects = new Vector();
                    dataObjects.addElement(budgetInfoBean);
                    responder.setDataObjects(dataObjects);
                }
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_RATE_TYPE_FOR_RATE_CLASS){
                Integer rateClassCode = (Integer)requester.getDataObject();
                int rateClass = rateClassCode.intValue();
                int rateTypeCode = budgetDataTxnBean.getRateTypeForRateClass(rateClass);
                responder.setDataObject(new Integer(rateTypeCode));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
            else if(functionType == CALCULATE_BASE_SALARY_FOR_PERIOD){
                CoeusVector coeusVector = null;
                Vector vecBaseSalaryData = (Vector)requester.getDataObjects();
                //to fetch the inflation rate
                Double inflationRate = (Double)vecBaseSalaryData.get(0);
                //budget persons details
                BudgetPersonsBean budgetPersonsBean = (BudgetPersonsBean)vecBaseSalaryData.get(1);
                //budget periods
                Vector vecBudgetPeriods = (Vector)vecBaseSalaryData.get(2);
                //fetch the budget info bean
                budgetInfoBean = (BudgetInfoBean)vecBaseSalaryData.get(3);
                BudgetCalculator budgetCalculator = new BudgetCalculator(budgetInfoBean);
                //function call to calculate the base salary for all periods
                HashMap hmPeriodBaseSalary = budgetCalculator.calculateBaseSalaryForAllPeriods(budgetPersonsBean, vecBudgetPeriods, inflationRate);
                responder.setDataObject(hmPeriodBaseSalary);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
            // Budget Report - Start
            else if(functionType == 'R') {
                String reportFolder = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH,"Reports");
                String reportPath = getServletContext().getRealPath("/")+reportFolder+"/";
                
                
                BudgetStream budgetStream = new BudgetStream();
                budgetStream.setReportPath(reportPath);
                CoeusVector vecBudgetPeriod = (CoeusVector)requester.getDataObjects();
                
                BudgetPeriodBean budgetPeriodBean;
                budgetPeriodBean = (BudgetPeriodBean)vecBudgetPeriod.get(0);
                String key = budgetPeriodBean.getProposalNumber() + budgetPeriodBean.getVersionNumber();
                QueryEngine.getInstance().addDataCollection(key, (Hashtable)requester.getDataObject());
                
                if(vecBudgetPeriod == null || vecBudgetPeriod.size() == 0) return ;
                
                boolean ediGenerated = budgetDataTxnBean.isGenerateEDIBudgetData(budgetPeriodBean.getProposalNumber(), loggedinUser);
                
                java.io.ByteArrayOutputStream byteArrayOutputStream = null;
                Vector vecPeriodReport = new Vector();
                for(int index = 0; index < vecBudgetPeriod.size(); index++) {
                    budgetPeriodBean = (BudgetPeriodBean)vecBudgetPeriod.get(index);
                    byteArrayOutputStream = budgetStream.getBudgetSummaryReportStream(budgetPeriodBean, ediGenerated, loggedinUser);
                    vecPeriodReport.add(new String(byteArrayOutputStream.toByteArray()));
                }
                CoeusFunctions coeusFunctions = new CoeusFunctions();
                String budgetSummaryOption = coeusFunctions.getParameterValue(CoeusConstants.BUDGET_SUMMARY_DISPLAY_OPTION);
                int summaryReportOption;
                
                if(budgetSummaryOption != null ){
                    summaryReportOption = Integer.parseInt(budgetSummaryOption);
                }else{
                    //Default option will be PDF
                    budgetSummaryOption = ""+CoeusConstants.BUDGET_SUMMARY_PDF_OPTION;
                    summaryReportOption = CoeusConstants.BUDGET_SUMMARY_PDF_OPTION;
                }
                
                String pdfUrl = "";
                //Set PDF/AWT Option
                responder.setId(budgetSummaryOption);
                
                if(summaryReportOption == CoeusConstants.BUDGET_SUMMARY_PDF_OPTION){
                    pdfUrl = generateBudgetSummaryPDF(vecPeriodReport, budgetPeriodBean.getProposalNumber());
                    responder.setDataObject(pdfUrl);
                }else{
                    responder.setDataObject(vecPeriodReport);
                    //Setting Error Messages
                    responder.setDataObjects(budgetStream.getVecMessages());
                }
                responder.setResponseStatus(true);
                responder.setMessage(null);
                //Budget Report - End
            }else if(functionType == GET_VALID_OH_RATE_TYPE_FOR_CE){
                String costElement = (String)requester.getDataObject();
                CoeusVector rateTypes = budgetDataTxnBean.getValidOHRateTypesForCE(costElement);
                responder.setDataObject(rateTypes);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == RELEASE_BUDGET_LOCK){
                proposalNumber = (String)requester.getDataObject();
                // Commented by Shivakumar for locking enhancement
                //                budgetDataTxnBean.releaseEdit(proposalNumber);
                //                Code added by Shivakumar-BEGIN
                //                budgetDataTxnBean.releaseEdit(proposalNumber,loggedinUser);
                // Calling the releaseLock method to fix the bug in new Locking System
                LockingBean lockingBean = budgetDataTxnBean.releaseLock(proposalNumber,loggedinUser);
                if(lockingBean != null){
                    responder.setLockingBean(lockingBean);
                }
                //                Code added by Shivakumar-END
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_COST_ELEMENTS_RATE_TYPES){
                CoeusVector cvValidCERateTypes = budgetDataTxnBean.getValidCERateTypes();
                //CoeusVector cvCostElementList = budgetDataTxnBean.getCostElementList();
                CoeusVector cvCostElements = budgetDataTxnBean.getCostElements();
                CoeusVector cvParameterValues = budgetDataTxnBean.getParameterValues();
                RatesTxnBean ratesTxnBean = new RatesTxnBean();
                CoeusVector cvRateTypeList = ratesTxnBean.getRateTypeList();
                CoeusVector cvRateClassList = ratesTxnBean.getRateClassList();
                
                UserMaintDataTxnBean userMaintTxnBean = new UserMaintDataTxnBean();
                boolean userHasOSPRight = userMaintTxnBean.getUserHasOSPRight(loggedinUser, "MAINTAIN_CODE_TABLES");
                Hashtable htCostElem = new Hashtable();
                // Adding Cost Element Rate types
                if(cvValidCERateTypes != null && cvValidCERateTypes.size() > 0){
                    htCostElem.put(new Integer(0), cvValidCERateTypes);
                }
                // Adding Cost element list
                if(cvCostElements != null && cvCostElements.size() > 0){
                    htCostElem.put(new Integer(1), cvCostElements);
                }
                // Adding Rate type list
                if(cvRateTypeList != null && cvRateTypeList.size() > 0){
                    htCostElem.put(new Integer(2), cvRateTypeList);
                }
                // Adding Rate class list
                if(cvRateClassList != null && cvRateClassList.size() > 0){
                    htCostElem.put(new Integer(3), cvRateClassList);
                }
                htCostElem.put(new Integer(4), new Boolean(userHasOSPRight));
                if(cvParameterValues != null && cvParameterValues.size() > 0){
                    htCostElem.put(new Integer(5), cvParameterValues);
                }
                responder.setDataObject(htCostElem);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == UPDATE_COST_ELEMENT_RATE_TYPES){
                CoeusVector cvCostElementRateData = (CoeusVector)requester.getDataObject();
                BudgetUpdateTxnBean budgetUpdateTxnBean = new BudgetUpdateTxnBean(loggedinUser);
                if(cvCostElementRateData != null && cvCostElementRateData.size() > 0){
                    for(int count=0;count < cvCostElementRateData.size();count++){
                        CERateTypeBean cERateTypeBean = (CERateTypeBean)cvCostElementRateData.elementAt(count);
                        if(cERateTypeBean.getAcType() == null){
                            continue;
                        }
                        boolean success = budgetUpdateTxnBean.updateValidCERateTypes(cERateTypeBean);
                    }
                }
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_VALID_COST_ELEMENT_JOB_CODES){
                CoeusVector cvJobCodesAndTitles = budgetDataTxnBean.getJobCodesAndTitles();
                CoeusVector cvAllvalidJobCodes = budgetDataTxnBean.getAllValidJobCodes();
                CoeusVector cvCostElementList = budgetDataTxnBean.getCostElementList();
                
                UserMaintDataTxnBean userMaintTxnBean = new UserMaintDataTxnBean();
                boolean userHasOSPRight = userMaintTxnBean.getUserHasOSPRight(loggedinUser, "MAINTAIN_CODE_TABLES");
                Hashtable htValidJobCodes = new Hashtable();
                
                htValidJobCodes.put(new Integer(0), new Boolean(userHasOSPRight));
                
                if(cvJobCodesAndTitles != null && cvJobCodesAndTitles.size() > 0){
                    htValidJobCodes.put(new Integer(1), cvJobCodesAndTitles);
                }
                
                if(cvAllvalidJobCodes != null && cvAllvalidJobCodes.size() > 0){
                    htValidJobCodes.put(new Integer(2),cvAllvalidJobCodes);
                }
                
                if(cvCostElementList != null && cvCostElementList.size() > 0){
                    htValidJobCodes.put(new Integer(3),cvCostElementList);
                }
                
                responder.setDataObject(htValidJobCodes);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_COST_ELEMENT_AND_BUDGET_CATEGORY){
                UserMaintDataTxnBean userMaintTxnBean = new UserMaintDataTxnBean();
                Hashtable htBudgetCategory = new Hashtable();
                boolean userHasOSPRight = userMaintTxnBean.getUserHasOSPRight(loggedinUser, "MAINTAIN_CODE_TABLES");
                
                BudgetDataTxnBean budgetTxnBean = new BudgetDataTxnBean();
                CoeusVector cvCostElementList = budgetTxnBean.getCostElementsListAndBudgetCategoryDesc();
                CoeusVector cvBudgetCategoryList = budgetTxnBean.getBudgetCategoryList();
                htBudgetCategory.put(KeyConstants.AUTHORIZATION_CHECK, new Boolean(userHasOSPRight));
                if(cvCostElementList != null && cvCostElementList.size() > 0){
                    htBudgetCategory.put(CostElementsBean.class, cvCostElementList);
                }
                if(cvBudgetCategoryList != null && cvBudgetCategoryList.size() > 0){
                    htBudgetCategory.put(BudgetCategoryBean.class, cvBudgetCategoryList);
                }
                responder.setDataObject(htBudgetCategory);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == UPDATE_COST_ELEMENT){
                CoeusVector cvCostElements = (CoeusVector)requester.getDataObject();
                BudgetUpdateTxnBean budgetUpdateTxnBean = new BudgetUpdateTxnBean(loggedinUser);
                BudgetDataTxnBean budgetTxnBean = new BudgetDataTxnBean();
                Hashtable htBudgetCategory = new Hashtable();
                boolean success = false;
                if(cvCostElements != null && cvCostElements.size() > 0){
                    for(int count=0;count < cvCostElements.size();count++){
                        CostElementsBean CostElementsBean = (CostElementsBean)cvCostElements.elementAt(count);
                        if(CostElementsBean.getAcType() == null){
                            continue;
                        }
                        success = budgetUpdateTxnBean.updateCostElementList(CostElementsBean);
                    }
                }
                CoeusVector cvCostElementList = budgetTxnBean.getCostElementsListAndBudgetCategoryDesc();
                CoeusVector cvBudgetCategoryList = budgetTxnBean.getBudgetCategoryList();
                if(cvCostElementList != null && cvCostElementList.size() > 0){
                    htBudgetCategory.put(CostElementsBean.class, cvCostElementList);
                }
                if(cvBudgetCategoryList != null && cvBudgetCategoryList.size() > 0){
                    htBudgetCategory.put(BudgetCategoryBean.class, cvBudgetCategoryList);
                }
                responder.setDataObject(htBudgetCategory);
                responder.setResponseStatus(success);
                responder.setMessage(null);
            }else if(functionType == UPDATE_VALID_CE_JOB_CODES){
                CoeusVector cvCEJobCodes = (CoeusVector)requester.getDataObject();
                BudgetUpdateTxnBean budgetUpdateTxnBean = new BudgetUpdateTxnBean(loggedinUser);
                boolean success = false;
                if(cvCEJobCodes != null && cvCEJobCodes.size() > 0){
                    for(int count=0;count < cvCEJobCodes.size();count++){
                        ValidCEJobCodesBean validCEJobCodesBean = (ValidCEJobCodesBean)cvCEJobCodes.elementAt(count);
                        if(validCEJobCodesBean.getAcType() == null){
                            continue;
                        }
                        success = budgetUpdateTxnBean.updateValidCECodes(validCEJobCodesBean);
                    }
                }
                responder.setResponseStatus(success);
                responder.setMessage(null);
            }else if(functionType == GET_CUSTOM_DATA_ELEMENTS){
                
                UserMaintDataTxnBean userMaintTxnBean = new UserMaintDataTxnBean();
                boolean userHasOSPRight = userMaintTxnBean.getUserHasOSPRight(loggedinUser, "MAINTAIN_CODE_TABLES");
                
                CustomElementsDataTxnBean customElementsDataTxnBean = new CustomElementsDataTxnBean();
                CoeusVector cvCustomDataElements = customElementsDataTxnBean.getCustomDataElements();
                CoeusVector cvCustomDataElementsDataUsage = customElementsDataTxnBean.getCustomDataElementUsage();
                
                Hashtable htCustomDataElements = new Hashtable();
                
                htCustomDataElements.put(KeyConstants.AUTHORIZATION_CHECK, new Boolean(userHasOSPRight));
                
                //Bug Fix: 1595 Start 1
                htCustomDataElements.put(CustomElementsInfoBean.class,cvCustomDataElements != null ? cvCustomDataElements : new CoeusVector());
                //                if(cvCustomDataElements != null && cvCustomDataElements.size() > 0){
                //                    htCustomDataElements.put(CustomElementsInfoBean.class, cvCustomDataElements);
                //                }
                htCustomDataElements.put(CustomElementsUsageBean.class,cvCustomDataElementsDataUsage != null ? cvCustomDataElementsDataUsage : new CoeusVector());
                
                //                if(cvCustomDataElementsDataUsage != null && cvCustomDataElementsDataUsage.size() > 0){
                //                    htCustomDataElements.put(CustomElementsUsageBean.class,cvCustomDataElementsDataUsage);
                //                }
                //Bug Fix: 1595 End 1
                
                responder.setDataObject(htCustomDataElements);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == DELETE_CUSTOM_ELEMENT){
                CustomElementsInfoBean customElementsInfoBean = (CustomElementsInfoBean)requester.getDataObject();
                CustomElementsDataTxnBean customElementsDataTxnBean = new CustomElementsDataTxnBean(loggedinUser);
                CoeusVector cvDeleteCustRec = customElementsDataTxnBean.getRowCountVarcharToColumn(customElementsInfoBean);
                boolean status = ((Boolean)cvDeleteCustRec.elementAt(cvDeleteCustRec.size() - 1)).booleanValue();
                cvDeleteCustRec.remove(cvDeleteCustRec.size() - 1);
                responder.setDataObjects(cvDeleteCustRec);
                responder.setResponseStatus(status);
                responder.setMessage(null);
            }else if(functionType == GET_MODULE_AND_CODE_TABLE_DATA){
                String columnName = (String)requester.getDataObject();
                Hashtable htModData = new Hashtable();
                CustomElementsDataTxnBean customElementsDataTxnBean = new CustomElementsDataTxnBean();
                // Getting the module names which are not in use
                CoeusVector cvModuleNames = customElementsDataTxnBean.getModuleNameNotInUsage(columnName);
                // Getting the code table data
                CoeusVector cvArgNames = customElementsDataTxnBean.getArgumentNamesForCodeTable();
                // Adding the data to Hashtable
                //                if(cvModuleNames != null && cvModuleNames.size() > 0){
                //                    htModData.put(KeyConstants.MODULE_NAMES, cvModuleNames);
                //                }
                //                if(cvArgNames != null && cvArgNames.size() > 0){
                //                    htModData.put(ComboBoxBean.class, cvArgNames);
                //                }
                htModData.put(KeyConstants.MODULE_NAMES, cvModuleNames==null?new CoeusVector():cvModuleNames);
                htModData.put(ComboBoxBean.class, cvArgNames==null?new CoeusVector():cvArgNames);
                responder.setDataObject(htModData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == UPDATE_CUSTOM_ELEMENTS){
                Hashtable htUpdateCustomElements = (Hashtable)requester.getDataObject();
                boolean success = false;
                if(htUpdateCustomElements != null){
                    CustomElementsDataTxnBean customElementsDataTxnBean = new CustomElementsDataTxnBean(loggedinUser);
                    success = customElementsDataTxnBean.updateCustomDataElementsAndUsageData(htUpdateCustomElements);
                }
                responder.setResponseStatus(success);
                responder.setMessage(null);
            }else if(functionType==GET_BUD_PER_IN_NEW_MODE){
                Vector data = requester.getDataObjects();
                proposalNumber = (String)data.elementAt(0);
                versionNumber = ((Integer)data.elementAt(1)).intValue();
                char mode = ((Character)data.elementAt(2)).charValue();
                
                BudgetDataTxnBean txnBean = new BudgetDataTxnBean();
                HashMap proposalPerons = null;
                proposalPerons = txnBean.getAllPropForBudgetPersons(proposalNumber,versionNumber,mode);
                responder.setDataObject(proposalPerons);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType==GET_PERSON_NAME){
                String personId = (String)requester.getDataObject();
                DepartmentPersonTxnBean deptTxnBean = new DepartmentPersonTxnBean();
                String fullName  = deptTxnBean.getPersonName(personId);
                responder.setDataObject(fullName);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType==UPDATE_PERSONS){
                CoeusVector  budgetPersonData= (CoeusVector)requester.getDataObject();
                BudgetUpdateTxnBean txnBean = new BudgetUpdateTxnBean(loggedinUser);
                boolean success = false;
                if(budgetPersonData!= null && budgetPersonData.size() > 0){
                    for(int count=0;count < budgetPersonData.size();count++){
                        BudgetPersonsBean budgetPersonsBean = (BudgetPersonsBean)budgetPersonData.elementAt(count);
                        if(budgetPersonsBean.getAcType() == null){
                            continue;
                        }
                        success  = txnBean.updateBudgetPersonWithPropPerson(budgetPersonsBean);
                    }
                }
                responder.setDataObject(new Boolean(success));
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType==GET_MODULAR_BUDGET_DATA){// Case Id #1626
                Vector clinetData = requester.getDataObjects();
                HashMap modularData = new HashMap();
                proposalNumber = (String)clinetData.get(0);
                versionNumber = ((Integer)clinetData.get(1)).intValue();
                BudgetDataTxnBean txnBean = new BudgetDataTxnBean();
                CoeusVector cvBudgetPeriods = txnBean.getBudgetPeriods(proposalNumber,versionNumber);
                CoeusVector cvModularBudget = txnBean.getBudgetModularData(proposalNumber,versionNumber);
                modularData.put(BudgetPeriodBean.class,cvBudgetPeriods);
                modularData.put(BudgetModularBean.class,cvModularBudget);
                responder.setDataObject(modularData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }
            else if(functionType==GET_BUD_PARAMETER_VALUE){// Case #1801 :Parameterize Under-recovery and cost-sharing distribution Start 2
                Vector data = new Vector();
                boolean validateCostShr = false;
                CoeusFunctions coeusFunctions = new CoeusFunctions();
                String parmCost = coeusFunctions.getParameterValue(CoeusConstants.FORCE_COST_SHARING_DISTRIBUTION);
                if(parmCost == null || parmCost.trim().equals("0")){
                    validateCostShr = false;
                }else if(parmCost.trim().equals("1")){
                    validateCostShr = true;
                }
                boolean validateUndrRec = false;
                String parmUnder = coeusFunctions.getParameterValue(CoeusConstants.FORCE_UNDER_RECOVERY_DISTRIBUTION);
                if(parmUnder == null || parmUnder.trim().equals("0")){
                    validateUndrRec = false;
                }else if(parmUnder.trim().equals("1")){
                    validateUndrRec = true;
                }
                
                data.addElement(new Boolean(validateCostShr));
                data.addElement(new Boolean(validateUndrRec));
                responder.setDataObjects(data);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }// Case #1801 :Parameterize Under-recovery and cost-sharing distribution End 2
            else if(functionType == GET_COST_SHARING_DISTRIBUTION){
                Vector clinetData = requester.getDataObjects();
                proposalNumber = (String)clinetData.get(0);
                versionNumber = ((Integer)clinetData.get(1)).intValue();
                char data_of_type = ((Character)clinetData.get(2)).charValue();
                BudgetDataTxnBean txnBean = new BudgetDataTxnBean();
                CoeusVector data = null;
                if(data_of_type == 'C'){
                    data = txnBean.getProposalCostSharing(proposalNumber,versionNumber);
                }else if(data_of_type == 'U'){
                    data = txnBean.getProposalIDCRate(proposalNumber,versionNumber);
                }
                responder.setDataObjects(data);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType==GET_BUDGET_PERIOD){
                Vector clinetData = requester.getDataObjects();
                proposalNumber = (String)clinetData.get(0);
                versionNumber = ((Integer)clinetData.get(1)).intValue();
                BudgetDataTxnBean txnBean = new BudgetDataTxnBean();
                CoeusVector data = txnBean.getBudgetPeriods(proposalNumber,versionNumber);
                responder.setDataObjects(data);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == GET_BUDGET_INFO){
                Vector clinetData = requester.getDataObjects();
                proposalNumber = (String)clinetData.get(0);
                versionNumber = ((Integer)clinetData.get(1)).intValue();
                BudgetDataTxnBean txnBean = new BudgetDataTxnBean();
                budgetInfoBean= txnBean.getBudgetForProposal(proposalNumber,versionNumber);
                CoeusVector cvData = new CoeusVector();
                cvData.addElement(budgetInfoBean);
                responder.setDataObjects(cvData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == GET_PROPOSAL_DATES){
                Vector clinetData = requester.getDataObjects();
                proposalNumber = (String)clinetData.get(0);
                versionNumber = ((Integer)clinetData.get(1)).intValue();
                BudgetDataTxnBean txnBean = new BudgetDataTxnBean();
                CoeusVector cvData= txnBean.getProposalIDCRate(proposalNumber,versionNumber);
                responder.setDataObjects(cvData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == GET_BUDGET_RATES){
                Vector clinetData = requester.getDataObjects();
                proposalNumber = (String)clinetData.get(0);
                versionNumber = ((Integer)clinetData.get(1)).intValue();
                unitNumber = (String)clinetData.get(2);
                activityTypeCode = ((Integer)clinetData.get(3)).intValue();
                HashMap hmRateData = new HashMap();
                BudgetDataTxnBean txnBean = new BudgetDataTxnBean();
                // Get the Institute Rates
                CoeusVector cvInstituteRates = txnBean.getProposalInstituteRates(proposalNumber,versionNumber);
                hmRateData.put(ProposalRatesBean.class, cvInstituteRates);
                // Get the Institute LA Rates
                CoeusVector cvInstituteLARates = txnBean.getInstituteLARates(unitNumber);
                hmRateData.put(InstituteLARatesBean.class, cvInstituteLARates);
                responder.setDataObject(hmRateData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == GET_ALL_APPOINTMENTS_FOR_PERSON){
//                CoeusVector coeusVector = null;
                //Commented for case id:3155 unable to create budget for multiple person entries
                //CoeusVector cvPersonId = (CoeusVector)requester.getDataObjects();
                //Added for case id :3155 -unable to create budget for multiple person entries - start
                Vector serverObjects = requester.getDataObjects();
                proposalNumber = (String)serverObjects.get(0);
                CoeusVector cvPersonId = (CoeusVector)serverObjects.get(1);
                //Added for case id :3155 - unable to create budget for multiple person entries - end
                BudgetPersonSyncBean budgetPersonSyncBean = null;
                CoeusVector cvAppointments = new CoeusVector();
                //case 2550 Bug fix start1
                CoeusVector cvData = new CoeusVector();
                CoeusVector cvAllData = new CoeusVector();
                //case 2550 bug fix end1
                if(cvPersonId!= null && cvPersonId.size() > 0){
                    for(int index = 0; index < cvPersonId.size(); index++){
                        //budgetPersonSyncBean = budgetDataTxnBean.getAppointmentsForAllPerson((BudgetPersonSyncBean)cvPersonId.get(index));
                        //cvAppointments.add(budgetPersonSyncBean);
                        //case 2550 bug fix start2
                        //Modified method signature for case id : 3155 - unable to create budget for multiple person entries- start
                        //cvData = budgetDataTxnBean.getAppointmentsForAllPerson((BudgetPersonSyncBean)cvPersonId.get(index));
                        cvData = budgetDataTxnBean.getAppointmentsForAllPerson((BudgetPersonSyncBean)cvPersonId.get(index),proposalNumber );
                        //Modified method signature for case id : 3155 - unable to create budget for multiple person entries - end
                        if(cvData!= null && cvData.size() > 0){
                            cvAllData.addAll(cvData);
                        }
                    }
                }
                if(cvAllData!= null && cvAllData.size() > 0){
                    for (int index = 0; index < cvAllData.size(); index++ ){
                        budgetPersonSyncBean = (BudgetPersonSyncBean)cvAllData.get(index);
                        cvAppointments.addElement(budgetPersonSyncBean);
                    }
                }
                //case 2550 Bug fix end2
                //Added for Case 3869 - Save not working for budget person - start
                CoeusVector cvResults = new CoeusVector();
                cvResults.add(cvAppointments);
                DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
                Vector vecAppointmentTypes = departmentPersonTxnBean.getArgumentValueList("AppointmentTypes");
                cvResults.add(vecAppointmentTypes);
                //Added for Case 3869 - Save not working for budget person - end
                
                responder.setDataObjects(cvResults);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }//For Budget Modular Enhancement case #2087 start 2
            else if(functionType == SYNC_MODULAR_BUDGET){
                Vector cvData = requester.getDataObjects();
                String propNumber = (String)cvData.get(0);
                versionNumber = ((Integer)cvData.get(1)).intValue();
                Hashtable hmModularData = new Hashtable();
                BudgetDataTxnBean txnBean = new BudgetDataTxnBean();
                CoeusVector cvDCData = txnBean.getSyncedModularDCBudget(propNumber, versionNumber);
                CoeusVector cvIDCData = txnBean.getSyncedModularIDCBudget(propNumber, versionNumber);
                if(cvDCData == null){
                    cvDCData = new CoeusVector();
                }
                if(cvIDCData == null){
                    cvIDCData = new CoeusVector();
                }
                hmModularData.put(BudgetModularBean.class, cvDCData);
                hmModularData.put(BudgetModularIDCBean.class, cvIDCData);
                responder.setDataObject(hmModularData);
                responder.setResponseStatus(true);
                responder.setMessage(null);
                
            }
            //For Budget Modular Enhancement case #2087 end 2
            //Added for bug fixed for case #2354 start 2
            else if(functionType == GET_VALID_JOB_CODES_FOR_CE){
                String costElement = (String)requester.getDataObject();
                CoeusVector cvValidCode = null;
                BudgetDataTxnBean txnBean = new BudgetDataTxnBean();
                cvValidCode = txnBean.getValidCodeCE(costElement);
                responder.setDataObject((cvValidCode == null ? new CoeusVector() : cvValidCode));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_JOB_CODE_VALIDATION_ENABLED){
                String parameter = (String)requester.getDataObject();
                CoeusVector cvParam = null;
                BudgetDataTxnBean txnBean = new BudgetDataTxnBean();
                cvParam = txnBean.getParameterValue(parameter);
                responder.setDataObject((cvParam == null ? new CoeusVector() : cvParam));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //Added for bug fixed for case #2354 end 2
            //Added for Case #2372 start 2
            else if(functionType == GET_BUDGET_INFO_FOR_COPY){
                String  strProposalNumber = requester.getId();
                BudgetDataTxnBean txnBean = new BudgetDataTxnBean();
                CoeusVector cvData = txnBean.getBudgetForProposal(strProposalNumber);
                responder.setDataObject(cvData == null ? new CoeusVector() : cvData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == UPDATE_BUDGET_IN_HIERARCHY){
                Hashtable data = (Hashtable)requester.getDataObject();
                BudgetUpdateTxnBean txnBean = new BudgetUpdateTxnBean(loggedinUser);
                CoeusVector cvData = (CoeusVector)data.get(BudgetInfoBean.class);
                BudgetInfoBean infoBean = (BudgetInfoBean)cvData.get(0);
                infoBean.setAcType(TypeConstants.UPDATE_RECORD);
                data.remove(BudgetInfoBean.class);
                data.put(BudgetInfoBean.class,cvData);
                txnBean.addUpdDeleteBudget(data);
                responder.setMessage(null);
                responder.setResponseStatus(true);
                // To get all the Budget TBA persons
            }else if(functionType == GET_TBA_PERSONS){
                CoeusVector cvParam = null;
                BudgetDataTxnBean txnBean = new BudgetDataTxnBean();
                cvParam = txnBean.getBudgetTBAPersons();
                responder.setDataObject((cvParam == null ? new CoeusVector() : cvParam));
                responder.setResponseStatus(true);
                responder.setMessage(null);
                // To get TBA person id
            }else if(functionType == GET_TBA_PERSON_ID){
                String personId = null;
                BudgetDataTxnBean txnBean = new BudgetDataTxnBean();
                personId = txnBean.getTBAPersonId();
                responder.setDataObject((personId == null ? "" : personId));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //Added for Case #2372 end 2
            //Added for Case #3121 - Start
            else if(functionType == GET_COST_ELEMENT_PERIOD) {
                Vector vecData = (Vector) requester.getDataObject();
                BudgetDataTxnBean txnBean = new BudgetDataTxnBean();
                CoeusVector cvData = txnBean.getCostElementPeriod(vecData);
                responder.setDataObject(cvData == null ? new CoeusVector() : cvData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            } else if(functionType == GET_COST_ELEMENT_RATE) {
                Vector vecData = (Vector) requester.getDataObject();
//                BudgetInfoBean dataBean = (BudgetInfoBean) requester.getDataObject();
                BudgetDataTxnBean txnBean = new BudgetDataTxnBean();
                CoeusVector cvData = txnBean.getUnitRate(vecData);
                responder.setDataObject(cvData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }
            //Added for Case #3121 - End
            //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
            else if(functionType == GET_PERIOD_TYPE){
                //to fetch all the period types and description
                HashMap hmData = budgetDataTxnBean.getPeriodTypeValues();
                responder.setDataObject(hmData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }
            else if(functionType == GET_ACTIVE_PERIOD_TYPE){
                //to fetch all the active period types and description
                // JM 1-15-2014
            	//HashMap hmData = budgetDataTxnBean.getActivePeriodTypeValues();
                CoeusVector cvData = budgetDataTxnBean.getActivePeriodTypeValues();
            	//responder.setDataObject(hmData);
            	responder.setDataObject(cvData);
            	// JM END
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }
            else if(functionType == GET_APPOINTMENT_TYPE){
                //to fetch all the appointment types and description
                HashMap hmData = budgetDataTxnBean.getAppointmentTypeValues();
                responder.setDataObject(hmData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }
            else if(functionType == GET_ACTIVE_APPOINTMENT_TYPE){
                //to fetch all the active appointment types and description
                HashMap hmData = budgetDataTxnBean.getActiveAppointmentTypeValues();
                responder.setDataObject(hmData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }
            //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
            ////Added for case 2158: Budgetary Validations : start
            else if(functionType == CHECK_BUDGET_VERSION){
                String propNumber = String.valueOf(requester.getDataObject());
                ProposalDevelopmentTxnBean txnBean = new ProposalDevelopmentTxnBean();
                int budgetCount = txnBean.getProposalHasBudget(propNumber);
                if(budgetCount == 0){
                    responder.setDataObject(new Integer(1000));//no budget
                }else{
                    BudgetSubAwardTxnBean subAward = new BudgetSubAwardTxnBean();
                    budgetCount = subAward.getBudgetFinalVersion(propNumber);
                    if(budgetCount == 0){
                        responder.setDataObject(new Integer(1001));//no final budget
                    }else{
                        responder.setDataObject(new Integer(budgetCount));
                    }
                }
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }
            //2158 end
            //Added for Case 3197 - Allow for the generation of project period greater than 12 months -start
            else if(functionType == GET_NUMBER_OF_MONTHS){
                String personId = null;
                BudgetDataTxnBean txnBean = new BudgetDataTxnBean();
                Vector vecData = requester.getDataObjects();
                Date startDate = new Date();
                Date endDate = new Date();
                if(vecData !=null && vecData.size() > 0){
                    startDate = (Date)vecData.get(0);
                    endDate = ((Date)vecData.get(1));
                }
                double noMonths = txnBean.getNumberOfMonths((java.sql.Date) startDate, (java.sql.Date) endDate);
                responder.setDataObject((new Double(noMonths)));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //3197 - end
            //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
            else if(functionType == GET_INACTIVE_COST_ELEMENTS){
                Vector dataObject = (Vector)requester.getDataObjects();
                Vector inActivecostElements = new Vector();
                //Get all the inactive cost elements form the DB
                if(dataObject!=null && dataObject.size()>0){
                     for(int index = 0 ; index <dataObject.size() ; index++){
                         String costElement = (String) dataObject.get(index);
                         CostElementsBean costElementsBean = budgetDataTxnBean.getCostElementsDetails(costElement);
                         //If status of cost element is 'N' then add to vector inActivecostElements                        
                         if("N".equals(costElementsBean.getActive())){
                             inActivecostElements.addElement(costElement);
                             inActivecostElements.addElement(costElementsBean.getDescription());
                         }
                     }
                }
                responder.setDataObjects(inActivecostElements);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_COST_ELEMENTS){
                Vector dataObject = (Vector)requester.getDataObjects();
                Vector proposalBudgetData = (Vector) dataObject.elementAt(0);
                char flagStatus = (Character)dataObject.elementAt(1);
                Vector budgetData = new Vector();
                Vector vecCostElements = new Vector();
                Vector inActiveCE = new Vector();
                if(proposalBudgetData != null && proposalBudgetData.size() > 0){
                    BudgetInfoBean budgetBean = null;
                    BudgetDetailBean budgetDetailBean = new BudgetDetailBean();
                    boolean isFinalFlag = false;
                    for ( int count = 0; count < proposalBudgetData.size(); count ++ ){
                        budgetBean = ( BudgetInfoBean ) proposalBudgetData.get(count);
                        versionNumber = budgetBean.getVersionNumber();
                        proposalNumber = budgetBean.getProposalNumber();
                        isFinalFlag = budgetBean.isFinalVersion();
                        budgetData = budgetDataTxnBean.getBudgetDetail(proposalNumber, versionNumber);
                        if(budgetData!=null && budgetData.size()>0){
                            for (int index = 0; index < budgetData.size(); index ++ ){
                                budgetDetailBean = (BudgetDetailBean) budgetData.get(index);
                                vecCostElements.add(budgetDetailBean.getCostElement());
                            }
                        }
                        //remove duplicate cost elements from the vector vecCostElements
                        for(int index=0; index<vecCostElements.size(); index++) {
                            //costElementIndex Returns the index of the last occurrence of the specified object from the vector vecCostElements.
                            int costElementIndex = vecCostElements.lastIndexOf(vecCostElements.get(index));
                            //if bothe index and costElementIndex holding the same value then remove the costelement form the vector
                            if(costElementIndex != index) {
                                vecCostElements.remove(costElementIndex);
                                index=index-1;
                            }
                        }
                        //If status of cost element is 'N' then add to vector inActivecostElements
                        if(vecCostElements!=null && vecCostElements.size()>0){
                            for(int index = 0 ; index <vecCostElements.size() ; index++){
                                String costElement = (String) vecCostElements.get(index);
                                CostElementsBean costElementsBean = budgetDataTxnBean.getCostElementsDetails(costElement);
                                if("N".equals(costElementsBean.getActive())){
                                    inActiveCE.addElement(costElementsBean.getActive());
                                    inActiveCE.addElement(costElement);
                                }
                            }
                        }
                        //if final version of budget is copying then break the loop and take cost element details of
                        //final version budget
                        if(flagStatus == 'F' && isFinalFlag){
                            break;
                        }
                    }
                }
                responder.setDataObjects(inActiveCE);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
            //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
            else if(functionType == GET_APPOINTMENT_AND_PERIOD_TYPES){
                Vector dataObject = (Vector)requester.getDataObjects();
                proposalNumber = (String)dataObject.elementAt(0);
                char flagStatus = (Character)dataObject.elementAt(1);
                Vector budgetData = new Vector();
                Vector vecCostElements = new Vector();
                int inactiveType = 0; 
                //get the inactive type details for the proposal
                //inactivetype holds all the inactive cost elements, inactive appointment and period types for the proposal
                inactiveType = budgetDataTxnBean.isBudgetHasInactiveATAndPT(proposalNumber, flagStatus);              
                responder.setDataObject(new Integer(inactiveType));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_INACTIVE_APPOINT_AND_PERIOD_TYPES){
                Vector vecAllData = (Vector)requester.getDataObjects();
                Vector vecBudgetPersons = (Vector)vecAllData.get(0);
                Vector vecBudgetPeriods = (Vector)vecAllData.get(1);
                Vector budgetPersonnelDetail = new Vector();
                
                BudgetDataTxnBean txnBean = new BudgetDataTxnBean();
                BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = null;
                BudgetPersonsBean budgetPersonsBean = new BudgetPersonsBean();
                Set uniquePeriodType = new HashSet();
                Set uniqueAppointmentType = new HashSet();
                Set inActivePeriodType = new HashSet();
                Set inActiveAppointmentType = new HashSet();
                String periodType = null;
                String appointmentType = null;
                
                if(vecBudgetPeriods!=null && vecBudgetPeriods.size()>0){
                    for ( int periodTypeIndex = 0; periodTypeIndex < vecBudgetPeriods.size(); periodTypeIndex ++ ){
                        budgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean)vecBudgetPeriods.get(periodTypeIndex);
                        periodType = budgetPersonnelDetailsBean.getPeriodType();
                        uniquePeriodType.add(periodType);
                    }
                }
                
                //holds inactive period type details
                if(!uniquePeriodType.isEmpty()){
                    Iterator iterator = uniquePeriodType.iterator();
                    while(iterator.hasNext()){
                        periodType = (String)iterator.next();
                        CostElementsBean costElementsBean = budgetDataTxnBean.getPeriodTypeDetails(periodType);
                        if("N".equals(costElementsBean.getActive())){
                            inActivePeriodType.add(costElementsBean.getDescription());
                        }
                    }
                }
                
                //get the budget person details
                if(vecBudgetPersons!=null && vecBudgetPersons.size()>0){
                    for ( int appTypeIndex = 0; appTypeIndex < vecBudgetPersons.size(); appTypeIndex ++ ){
                        budgetPersonsBean = (BudgetPersonsBean)vecBudgetPersons.get(appTypeIndex);
                        appointmentType = budgetPersonsBean.getAppointmentType();
                        uniqueAppointmentType.add(appointmentType);
                    }
                }
                //holds inactive appointment type details
                if(!uniqueAppointmentType.isEmpty()){
                    Iterator iterator = uniqueAppointmentType.iterator();
                    while(iterator.hasNext()){
                        appointmentType = (String)iterator.next();
                        budgetPersonsBean = budgetDataTxnBean.getAppointmentTypeDetails(appointmentType);
                        if("N".equals(budgetPersonsBean.getStatus())){
                            inActiveAppointmentType.add(budgetPersonsBean.getAppointmentType());
                        }
                    }
                }
                
                dataObjects.addElement(inActivePeriodType);
                dataObjects.addElement(inActiveAppointmentType);
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            else if(functionType == GET_APPOINTMENT_AND_PERIOD_DETAILS){
                Vector dataObject = (Vector)requester.getDataObjects();
                proposalNumber = (String)dataObject.elementAt(0);
                versionNumber = (Integer)dataObject.elementAt(1);
                BudgetDataTxnBean txnBean = new BudgetDataTxnBean();
                BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = null;
                BudgetPersonsBean budgetPersonsBean = new BudgetPersonsBean();
                Set uniquePeriodType = new HashSet();
                Set uniqueAppointmentType = new HashSet();
                String periodType = null;
                String appointmentType = null;
                Vector inActivePeriodType = new Vector();
                Vector inActiveAppointmentType = new Vector();
                //get the personnel details for the proposal budget
                CoeusVector cvBudgetPersonnelDetail = budgetDataTxnBean.getBudgetPersonnelDetail(proposalNumber,versionNumber);
                if(cvBudgetPersonnelDetail!=null && cvBudgetPersonnelDetail.size()>0){
                    for ( int periodTypeIndex = 0; periodTypeIndex < cvBudgetPersonnelDetail.size(); periodTypeIndex ++ ){
                        budgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean)cvBudgetPersonnelDetail.get(periodTypeIndex);
                        periodType = budgetPersonnelDetailsBean.getPeriodType();
                        uniquePeriodType.add(periodType);
                    }
                }
                //holds inactive period type details
                if(!uniquePeriodType.isEmpty()){
                    Iterator iterator = uniquePeriodType.iterator();
                    while(iterator.hasNext()){
                        periodType = (String)iterator.next();
                        CostElementsBean costElementsBean = budgetDataTxnBean.getPeriodTypeDetails(periodType);
                        if("N".equals(costElementsBean.getActive())){
                            inActivePeriodType.addElement(costElementsBean.getDescription());
                        }
                    }
                }
                //get the budget persons 
                CoeusVector cvBudgetPersons = budgetDataTxnBean.getBudgetPersons(proposalNumber,versionNumber);
                if(cvBudgetPersons!=null && cvBudgetPersons.size()>0){
                    for ( int appTypeIndex = 0; appTypeIndex < cvBudgetPersons.size(); appTypeIndex ++ ){
                        budgetPersonsBean = (BudgetPersonsBean)cvBudgetPersons.get(appTypeIndex);
                        appointmentType = budgetPersonsBean.getAppointmentType();
                        uniqueAppointmentType.add(appointmentType);
                    }
                }
                //holds inactive appointment type details
                if(!uniqueAppointmentType.isEmpty()){
                    Iterator iterator = uniqueAppointmentType.iterator();
                    while(iterator.hasNext()){
                        appointmentType = (String)iterator.next();
                        budgetPersonsBean = budgetDataTxnBean.getAppointmentTypeDetails(appointmentType);
                        if("N".equals(budgetPersonsBean.getStatus())){
                            inActiveAppointmentType.addElement(budgetPersonsBean.getAppointmentType());
                        }
                    }
                }
                dataObjects.addElement(inActivePeriodType);
                dataObjects.addElement(inActiveAppointmentType);
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_APP_AND_PER_TYPES_FOR_BUDGET_COPY){
                Vector dataObject = (Vector)requester.getDataObjects();
                proposalNumber = (String)dataObject.elementAt(0);
                versionNumber = (Integer)dataObject.elementAt(1);
                int budgetperiodNumber = (Integer)dataObject.elementAt(2); 
                //get the inactive type detils for the proposal copy
                int inactiveType = budgetDataTxnBean.isBudgeCopytHasInactiveATAndPT(proposalNumber, versionNumber , budgetperiodNumber); 
                responder.setDataObject(new Integer(inactiveType));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            
            //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end
            // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
            }else if(functionType == GET_UNIT_FORMUALTED_TYPES){
                proposalNumber = (String)requester.getId();
                responder.setDataObject(budgetDataTxnBean.getUnitFormulatedCostForProposalLeadUnit(proposalNumber));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
        }catch( LockingException lockEx ) {
            //lockEx.printStackTrace();
            LockingBean lockingBean = lockEx.getLockingBean();
            String errMsg = lockEx.getErrorMessage();
            CoeusMessageResourcesBean coeusMessageResourcesBean
            =new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            responder.setResponseStatus(false);
            responder.setException(lockEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, lockEx,
            "BudgetMaintenanceServlet", "perform");
        }
        catch( CoeusException coeusEx ) {
            //coeusEx.printStackTrace();
//            int index=0;
            String errMsg;
            if(coeusEx.getErrorId()==999999){
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
                responder.setLocked(true);
            }else{
                errMsg = coeusEx.getMessage();
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean
            =new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);
            responder.setException(coeusEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx,
            "BudgetMaintenanceServlet", "perform");
            
        }catch( DBException dbEx ) {
            //dbEx.printStackTrace();
//            int index=0;
            String errMsg = dbEx.getUserMessage();
            if (dbEx.getErrorId() == 20102 ) {
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
            }
            if (errMsg.equals("db_exceptionCode.1111")) {
                responder.setCloseRequired(true);
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean
            = new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);
            responder.setException(dbEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx,
            "BudgetMaintenanceServlet", "perform");
            
        }catch(Exception e) {
            //e.printStackTrace();
            responder.setResponseStatus(false);
            responder.setException(e);
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e,
            "BudgetMaintenanceServlet", "perform");
            
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "BudgetMaintenanceServlet", "doPost");
        //Case 3193 - END
            
        } finally {
            try{
                
                outputToApplet
                = new ObjectOutputStream(response.getOutputStream());
                outputToApplet.writeObject(responder);
                // close the streams
                if (inputFromApplet!=null){
                    inputFromApplet.close();
                }
                if (outputToApplet!=null){
                    outputToApplet.flush();
                    outputToApplet.close();
                }
            }catch (IOException ioe){
                UtilFactory.log( ioe.getMessage(), ioe,
                "BudgetMaintenanceServlet", "perform");
            }
        }
    }
    
    private Hashtable getBudgetData(BudgetInfoBean budgetInfoBean) throws DBException, CoeusException{
        Hashtable budget = new Hashtable();
        
        String proposalNumber = budgetInfoBean.getProposalNumber();
        int versionNumber = budgetInfoBean.getVersionNumber();
        String unitNumber = budgetInfoBean.getUnitNumber();
        int activityTypeCode = budgetInfoBean.getActivityTypeCode();
        
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        
        CoeusVector coeusVector = null;
        //Budget Info Bean
        budgetInfoBean = budgetDataTxnBean.getBudgetForProposal(proposalNumber, versionNumber);
        coeusVector = new CoeusVector();
        coeusVector.addElement(budgetInfoBean);
        budget.put(BudgetInfoBean.class,coeusVector);
        //Budget Periods
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetPeriods(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(BudgetPeriodBean.class,coeusVector);
        //Budget Details
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetDetail(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(BudgetDetailBean.class,coeusVector);
        //Budget Personnel Detail
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetPersonnelDetail(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(BudgetPersonnelDetailsBean.class,coeusVector);
        
        // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
        // Budget formualted cost details
        coeusVector = budgetDataTxnBean.getBudgetFormulatedDetail(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(BudgetFormulatedCostDetailsBean.class,coeusVector);
        // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End

        
        //Budget Budget Detail Cal Amounts
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetDetailCalAmounts(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(BudgetDetailCalAmountsBean.class,coeusVector);
        //Budget Budget Personnel Detail Cal Amounts
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetPersonnelCalAmounts(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(BudgetPersonnelCalAmountsBean.class,coeusVector);
        //Budget Budget Persons
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetPersons(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(BudgetPersonsBean.class,coeusVector);
        
        //Budget Proposal Institute Rates
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getProposalInstituteRates(proposalNumber, versionNumber);
        if(coeusVector==null){
            if(versionNumber!=1){
                coeusVector = budgetDataTxnBean.getProposalInstituteRates(proposalNumber, versionNumber-1);
            }
        }
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(ProposalRatesBean.class,coeusVector);
        
        //Budget Proposal Institute LA Rates
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getProposalInstituteLARates(proposalNumber, versionNumber);
        if(coeusVector==null){
            if(versionNumber!=1){
                coeusVector = budgetDataTxnBean.getProposalInstituteLARates(proposalNumber, versionNumber-1);
            }
        }
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        
        budget.put(ProposalLARatesBean.class,coeusVector);
        
        //Budget Justification
        coeusVector = new CoeusVector();
        BudgetJustificationBean budgetJustificationBean = budgetDataTxnBean.getBudgetJustification(proposalNumber, versionNumber);
        if(budgetJustificationBean!=null){
            coeusVector.addElement(budgetJustificationBean);
        }
        budget.put(BudgetJustificationBean.class,coeusVector);
        //Budget Institute Rate
        /*
         * Updated by Geo on 16-Sep-2004
         *  To pass the top level unit to get the institute rates
         */
        RatesTxnBean ratesTxnBean = new RatesTxnBean();
        String topLevelUnitNum = ratesTxnBean.getTopLevelUnit(unitNumber);
        //System.out.println("Top Level Unit=>"+topLevelUnitNum);
        coeusVector = new CoeusVector();
        //        coeusVector = budgetDataTxnBean.getInstituteRates(activityTypeCode);
        coeusVector = budgetDataTxnBean.getInstituteRates(activityTypeCode, topLevelUnitNum);
        //System.out.println("Institute rates=>"+coeusVector.toString());
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(InstituteRatesBean.class,coeusVector);
        //Budget Institute LA Rate
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getInstituteLARates(unitNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(InstituteLARatesBean.class,coeusVector);
        //Budget Category List
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetCategoryList();
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(BudgetCategoryBean.class, coeusVector);
        //Rate Class List
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getOHRateClassList();
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(RateClassBean.class, coeusVector);
        //Valid Calc Types for E and V
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getValidCalcTypesForEV();
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(ValidCalcTypesBean.class, coeusVector);
        
        //Get Proposal Data for Budget
        coeusVector = new CoeusVector();
        ProposalDevelopmentFormBean proposalDevelopmentFormBean = budgetDataTxnBean.getProposalDetailsForBudget(proposalNumber);
        coeusVector.addElement(proposalDevelopmentFormBean);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(ProposalDevelopmentFormBean.class, coeusVector);
        
        //Get Proposal Cost Sharing
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getProposalCostSharing(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(ProposalCostSharingBean.class, coeusVector);
        
        //Get Proposal IDC Rates
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getProposalIDCRate(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(ProposalIDCRateBean.class, coeusVector);
        
        //Get Project Income Details
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getProjectIncomeDetails(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(ProjectIncomeBean.class, coeusVector);
        
        //Get Budget Modular Details
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetModularData(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(BudgetModularBean.class, coeusVector);
        //For Budget Modular Enhancement case #2087 start 3
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetModularIDCData(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(BudgetModularIDCBean.class, coeusVector);
        //For Budget Modular Enhancement case #2087 end 3
        
        
        //Budget Summary Parameter for generating Report in AWT or PDF
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        String budgetSummaryDisplayOption = coeusFunctions.getParameterValue(CoeusConstants.BUDGET_SUMMARY_DISPLAY_OPTION);
        budget.put(CoeusConstants.BUDGET_SUMMARY_DISPLAY_OPTION, budgetSummaryDisplayOption==null ? null : new Integer(budgetSummaryDisplayOption));
        // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - start
        String enableFormualtedCost = coeusFunctions.getParameterValue(CoeusConstants.ENABLE_FORMULATED_COST_CALC);
        budget.put(CoeusConstants.ENABLE_FORMULATED_COST_CALC, enableFormualtedCost==null ? null : new Integer(enableFormualtedCost));
        
        String formulatedCostElements = coeusFunctions.getParameterValue(CoeusConstants.FORMULATED_COST_ELEMENTS);
        if(formulatedCostElements != null){
            formulatedCostElements = formulatedCostElements.trim();
            budget.put(CoeusConstants.FORMULATED_COST_ELEMENTS, formulatedCostElements);
        }else{
            budget.put(CoeusConstants.FORMULATED_COST_ELEMENTS, "");
        }
        
        CoeusVector cvFormualetdTypes = budgetDataTxnBean.getFormulatedTypes();
        if(cvFormualetdTypes != null && !cvFormualetdTypes.isEmpty()){
            budget.put(CoeusConstants.FORMULATED_TYPES, cvFormualetdTypes);
        }
        
        // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
        
        return budget;
    }
    
    public String generateBudgetSummaryPDF(Vector vecPeriodReport, String proposalNumber)
    throws Exception {
        
        Vector reportData = new Vector(3,2);
        
        for(int index = 0; index < vecPeriodReport.size(); index++) {
            String report = (String)vecPeriodReport.get(index);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(report.getBytes());
            reportData.add(byteArrayInputStream);
        }
        String pdfUrl = "";
        
        CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
        
        ReportGenerator reportGenerator = new ReportGenerator();
        ByteArrayInputStream xmlStream;
        ByteArrayOutputStream byteArrayOutputStream;
        ByteArrayOutputStream reports[] = new ByteArrayOutputStream[reportData.size()];
        String bookmarks[] = new String[reportData.size()];
        InputStream xslStream;
        
        for(int index = 0; index < reportData.size(); index++) {
            xmlStream = (ByteArrayInputStream)reportData.get(index);
            xmlStream.close();
            xslStream = getClass().getResourceAsStream("/edu/mit/coeus/budget/report/BudgetReportModified.xsl");
            byteArrayOutputStream = reportGenerator.convertXML2PDF(xmlStream, xslStream);
            byteArrayOutputStream.close();
            reports[index] = byteArrayOutputStream;
            bookmarks[index] = BUDGET_PERIOD + " " + (index + 1);
        }
        
        byteArrayOutputStream = reportGenerator.mergePdfReports(reports, bookmarks);
        
        InputStream is = getClass().getResourceAsStream("/coeus.properties");
        Properties coeusProps = new Properties();
        coeusProps.load(is);
        String reportPath = coeusProps.getProperty("REPORT_GENERATED_PATH"); //get path (to generate PDF) from config
        
        String filePath = CoeusConstants.SERVER_HOME_PATH+File.separator+reportPath;
        File reportDir = new File(filePath);
        if(!reportDir.exists()){
            reportDir.mkdirs();
        }
        SimpleDateFormat dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");
        File file = new File(filePath, "BudgetSummary"+proposalNumber+dateFormat.format(new Date())+".pdf");
        file.deleteOnExit();
        FileOutputStream fos = new FileOutputStream(file);
        
        fos.write(byteArrayOutputStream.toByteArray());
        fos.close();
        byteArrayOutputStream.close();
        
        pdfUrl = "/"+reportPath+"/"+file.getName();
        
        return pdfUrl;
    }
}