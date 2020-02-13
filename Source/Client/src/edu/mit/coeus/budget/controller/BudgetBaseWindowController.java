/*
 * BudgetBaseWindowController.java
 *
 * Created on October 6, 2003, 11:11 AM
 */

/* PMD check performed, and commented unused imports and variables 
 * on 25-JULY-2011 by Satheesh Kumar K N
 */

package edu.mit.coeus.budget.controller;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author sharathk
 */

import edu.mit.coeus.routing.gui.RoutingValidationChecksForm;
import edu.mit.coeus.unit.bean.UnitFormulatedCostBean;
import edu.mit.coeus.user.gui.UserDelegationForm;
import java.text.MessageFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
//import java.applet.AppletContext;
import java.net.URL;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.budget.gui.*;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.budget.controller.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
//import edu.mit.coeus.propdev.gui.ProposalDetailAdminForm;
import edu.mit.coeus.propdev.gui.InboxDetailForm;
import edu.mit.coeus.user.gui.UserPreferencesForm;

//import edu.mit.coeus.budget.report.BudgetReportBaseWindowController;
import edu.mit.coeus.propdev.bean.ProposalHierarchyBean;
import edu.mit.coeus.propdev.gui.ProposalHierarchyContoller;
import java.net.MalformedURLException;
import edu.mit.coeus.utils.report.ReportController;
//import edu.mit.coeus.budget.report.ReportGenerator;

/** Budget Internal frame which displays all budget related tabs. */
public class BudgetBaseWindowController extends Controller
implements ActionListener, ChangeListener ,VetoableChangeListener,BeanDeletedListener,
BeanUpdatedListener {
    
    //private ProposalDetailAdminForm proposalDetailAdminForm;
    
    private CoeusAppletMDIForm mdiForm;
    private CoeusMessageResources coeusMessageResources;
    private  StatusWindow statusWindow;
    
    private JPanel pnlBasePanel;
    
    //private BudgetBean budgetBean;
    private BudgetInfoBean budgetInfoBean, oldActivityTypeCodeBudgetInfoBean;
    private BudgetBaseWindow budgetBaseWindow;
    private JTabbedPane tbdPnBudgetTabbedPane;
    
    //Controllers
    private BudgetSummaryController budgetSummaryController;
    private Vector vecBudgetPeriodController;
    private BudgetTotalController budgetTotalController;
    
    private BudgetPersonController budgetPersonController;
    
    private AdjustPeriodBoundaryController adjustPeriodBoundaryController;
    
    private BudgetPersonSalariesController budgetPersonSalariesController;
    private UnderRecoveryDistributionController underRecoveryController;
    
    private boolean copyMode;
    
    // To check whether the budget info bean is changed or not.
    private boolean budgetInfoBeanModified = false;
    
    //Holds true if budget lock has to be released, false otherwise
    private boolean releaseLock = false;
    
    //will be true if budget data already available.
    //will be used in Copy mode and in display since both will have a call to fetchData.
    private boolean dataFetched = false;
    
    //Holds true if save is invoked while closing budget window
    private boolean closeAfterSave = false;
    
    private ProposalDevelopmentFormBean proposalDevelopmentFormBean;
    
    private QueryEngine queryEngine;
    private String queryKey;
    
    private String title;
    private String unitNumber;
    private int activityTypeCode;
    private boolean activityTypeCodeModified;
    private int selectedTabIndex;
    
    public  static final String SERVLET = "/BudgetMaintenanceServlet";
    private char functionType;
    
    private static char GET_BUDGET_DETAILS = 'B';
    private static char VALIDATE = 'L';
    private static char SAVE_BUDGET = 'S';
    private final char RELEASE_BUDGET_LOCK = 'O';
    //private static char GET_INSTITUTE_RATES = 'K';
    
    private boolean closed = false;
    
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
    private static final char GET_INACTIVE_COST_ELEMENTS = '3';
    private static final String BUDGET_HAVE_INACTIVE_COST_ELEMENTS = "budgetSelect_exceptionCode.1061";
    private static final String INACTIVE_COST_ELEMENT_MSG = "budgetSelect_exceptionCode.1064 ";
    private boolean allow_save;
    private Vector vecCEMessages;
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
    
    //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
    private static final char GET_INACTIVE_APPOINT_AND_PERIOD_TYPES = '7';
    private static final char GET_APP_AND_PER_TYPES_FOR_BUDGET_COPY = '8';
    //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end

    // Added for Case COEUSDEV-423 / COEUSQA-2404 : Prop Dev - Error saving budget after adjust period boundaries -Start
    private CoeusVector vecDeletedPeriods;
    // Added for Case COEUSDEV-423 / COEUSQA-2404 : Prop Dev - Error saving budget after adjust period boundaries -End
//    private static final int WIDTH = 400;
//    private static final int HEIGHT = 300;
    private static final String BUDGET_SUMMARY = "Summary";
    private static final String BUDGET_PERIOD = "Period ";
    private static final String BUDGET_TOTAL = "Total";
    
    private static final String BUDGET_SUMMARY_DISPLAY_OPTION = "BUDGET_SUMMARY_DISPLAY_OPTION";
    //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module
    private static final String ENABLE_SALARY_INFLATION_ANNIV_DATE = "ENABLE_SALARY_INFLATION_ANNIV_DATE";
//    private static final int BUDGET_SUMMARY_PDF = 1;
    private static final int BUDGET_SUMMARY_AWT = 2;
    private boolean otherCalculation;
    
    
    private static final String THE_BUDGET_PERIOD = "The Budget period ";
    /**
     *  does not have any detail line items. Cannot generate period
     */
    private static final String DOES_NOT_HAVE_DETAIL_LINE_ITEMS = "budget_baseWindow_exceptionCode.1401";
    
    /**
     * Do you want to save changes ?
     */
    private static final String SAVE_CHANGES = "budget_baseWindow_exceptionCode.1402";
    
//    private static final String TOTAL_COST_EXCEED_COST_LIMIT = "budgetSummary_exceptionCode.1107";
    
    /**
     * Pending changes in the budget should be saved before \n opening budget summary report. \n Do you want to save now?
     */
    private static final String SAVE_PENDING_CHANGES = "budget_baseWindow_exceptionCode.1404";
    
    /**
     * The proposal must contain a budget with atleast one line item
     */
    private static final String NO_LINE_ITEM = "budget_baseWindow_exceptionCode.1405";
    
    /**
     * The proposal's Activity Type has changed.
     * Do you want to change the references to old activity type in the budget to the current activity type.
     * If you decide to change the activity type budget should be recalculated
     */
    private static final String ACTIVITY_TYPE_HAS_CHANGED = "budget_baseWindow_exceptionCode.1403";
    // 3681: Message about proposal's activity type changing - Start
    private static final String ACTIVITY_TYPE_NOT_AVAILABLE = "budget_baseWindow_exceptionCode.1406";
    // 3681: Message about proposal's activity type changing - End
    /**
     * Save Budget to enter subaward data
     */
    private static final String SAVE_BUDGET_FIRST = "budgetSubAward_exceptionCode.1551";
    
    private static final String CAN_APPLY_AFTER_GENERATING_PERIODS = "This operation can be performed only after generating all the budget periods.";
    
    private static final String BUDGET_SUMMARY_REPORT = "Budget Summary Report for Proposal ";
    private static final String CUM_INDSTRL_BUDGET = "Cumulative Industrial Budget";
    private static final String BUDGET_SUMMARY_TOTAL = "Budget Summary Total";

    public static final int COPY_ONE_PERIOD_ONLY = 1;
    public static final int COPY_ALL_PERIODS = 2;
    private static final int CALCULATE_ALL_PERIODS = -1;
    private UserPreferencesForm userPreferencesForm;
    //Added for Case#3682 - Enhancements related to Delegations - Start
    private UserDelegationForm userDelegationForm;
    //Added for Case#3682 - Enhancements related to Delegations - End
    private char mode;
    private boolean visited;
    /** Case #1801 :Parameterize Under-recovery and cost-sharing distribution Start 1
     */
    private boolean forceUnderRecovery;
    private boolean forceCostSharing;
    private boolean hierarcy;
    private boolean parentProposal;
    private ProposalHierarchyBean proposalHierarchyBean;
    private static final char UPDATE_BUDGET_RATE_AND_BASE = 'p';
    private CoeusVector budgetPersonData;
    /** Case #1801: Parameterize Under-recovery and cost-sharing distribution End 1
     */
    //add for printSummary
    private static final char PRINT_BUDGET_SUMMARY_TOTAL_PAGE = 'B';
    //private static final String connect = CoeusGuiConstants.CONNECTION_URL+"/printServlet";\
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + "/ReportConfigServlet";
    
    //Proposal Hierarchy Enhancment Start
    private ProposalHierarchyContoller hierarchyController;
    private static final String HIERARCHY_SERVLET = "/ProposalHierarchyServlet";
    private static final char SYNC_BUDGET = 'K';
//    private static final char REFRESH_HIERARCHY = 'P';
    private String parentPropNo = "";
//    private Vector vecEntireModBudController = null;
    private DateUtils dtUtils = new DateUtils();
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    private boolean hierarchyMode;
    //Proposal Hierarchy Enhancment End
    //Added for Case#2341 - Recalculate Budget if Project dates change - starts
    //private static final String DATE_SEPARATERS = ":/.,|-";
    private boolean isAdjusted;
    private final char GET_MASTER_DATA_TO_SYNC = 'G';
    private static final String COULD_NOT_CONTACT_SERVER = "Could not contact server";
    private final String SYNC_MESSAGE = "budget_proposalRates_exceptionCode.1253"; //"Do you want to Sync Proposal rates with the currrent Institute rates ?";
    // Added for Case#2341 - Recalculate Budget if Project dates change - ends
    //Added for Case#3404-Outstanding proposal hierarchy changes - Start
    private static final String VAL_COMPLETE = "Complete";
    //Added for Case#3404-Outstanding proposal hierarchy changes - End
    //Added with case 2158: Budget Validations - Start
    private static final char VALIDATION_CHECKS  = 'N';
    private static final char VALIDATION_ERROR   = 'E';
    private static final char VALIDATION_WARNING = 'W';
    private static final String CONFIRM_SYNCING = "validationChecks_exceptionCode.1909";
    private static final String MSGKEY_VALIDATION_WARNINGS = "budgetSelect_exceptionCode.1060";
    private boolean validatingBudgetSummary = false;

   
    //2158 End
    //Added with COEUSDEV 197,198 - Validation Checks for syncing Proposals
    private static final char HIERARCHY_VALIDATION_CHECKS = 'S';
    //COEUSDEV 197,198 End
    /** Creates a new instance of BudgetBaseWindowController
     * @param title title of the budget base window.
     * @param budgetInfoBean budget Info Bean.
     * @param functionType functionType
     * @param proposalDevelopmentFormBean proposalDevelopmentFormBean
     */
    public BudgetBaseWindowController(String title, BudgetInfoBean budgetInfoBean,char functionType, ProposalDevelopmentFormBean proposalDevelopmentFormBean){
        super(budgetInfoBean);
        this.budgetInfoBean = budgetInfoBean;
        this.functionType = functionType;
        this.mode = functionType;
        this.proposalDevelopmentFormBean = proposalDevelopmentFormBean;
        this.title = title;
        budgetBaseWindow = new BudgetBaseWindow(title, CoeusGuiConstants.getMDIForm());
        budgetBaseWindow.setMinimumSize(budgetBaseWindow.getSize());
        
        setFunctionType(functionType);
        
        mdiForm = CoeusGuiConstants.getMDIForm();
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        vecBudgetPeriodController = new Vector();
        
        //Used to display status when calcualting / Saving / Refreshing.
        statusWindow = new StatusWindow(mdiForm, true);
        
        queryEngine = QueryEngine.getInstance();
        queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        
    }
    
    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
    /*
     * Metthod to fetch the budget details from server
     */
    private void fetchBudgetDetails(){
        RequesterBean requesterBean = new RequesterBean();
        Hashtable htDataForBudget = new Hashtable();
        htDataForBudget.put(BudgetInfoBean.class, budgetInfoBean);
        htDataForBudget.put(CoeusConstants.IS_RELEASE_LOCK, new Boolean( isReleaseLock() ));
        requesterBean.setDataObject(htDataForBudget);
        requesterBean.setFunctionType(GET_BUDGET_DETAILS);
        activityTypeCode = budgetInfoBean.getActivityTypeCode();
        unitNumber = budgetInfoBean.getUnitNumber();
        requesterBean.setUserName(mdiForm.getUserId());
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL+SERVLET, requesterBean);
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(responderBean.isSuccessfulResponse()) {
            activityTypeCode = budgetInfoBean.getActivityTypeCode();
            unitNumber = budgetInfoBean.getUnitNumber();
            Hashtable budgetData = (Hashtable)responderBean.getDataObject();
            CoeusVector cvDisplayOption = new CoeusVector();
            cvDisplayOption.add(budgetData.get(BUDGET_SUMMARY_DISPLAY_OPTION));
            budgetData.put(BUDGET_SUMMARY_DISPLAY_OPTION, cvDisplayOption);
            queryEngine.addDataCollection(queryKey, budgetData);
        }
    }
    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
    
    private void fetchDataFromServer() {
        if(dataFetched)return ;
        
        RequesterBean requesterBean = new RequesterBean();
        //requesterBean.setDataObject(budgetInfoBean);
        Hashtable htDataForBudget = new Hashtable();
        htDataForBudget.put(BudgetInfoBean.class, budgetInfoBean);
        htDataForBudget.put(CoeusConstants.IS_RELEASE_LOCK, new Boolean( isReleaseLock() ));
        
        if( (getFunctionType() == TypeConstants.ADD_MODE && budgetInfoBean.getVersionNumber() == 1)
        || getFunctionType() == TypeConstants.DISPLAY_MODE ){
            //If first Veriosn no need to check for activity type change.Get Details.
            requesterBean.setDataObject(htDataForBudget);
            requesterBean.setFunctionType(GET_BUDGET_DETAILS);
            activityTypeCode = budgetInfoBean.getActivityTypeCode();
            unitNumber = budgetInfoBean.getUnitNumber();
        }else {
            requesterBean.setDataObject(budgetInfoBean);
            requesterBean.setFunctionType(VALIDATE);
        }
        requesterBean.setUserName(mdiForm.getUserId());
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL+SERVLET, requesterBean);
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(responderBean.isSuccessfulResponse()) {
            //Check for activity type code modified.
            
            if(responderBean.getDataObject() == null) {
                BudgetInfoBean serverBudgetInfoBean = (BudgetInfoBean)responderBean.getDataObjects().get(0);
                int selection;
                //Display msg only if in modify. Add Mode. else display
                //data as it is in Database.
                // 3681: Message about proposal's activity type changing - Start
//                //Code commented and added for bug fix case#3183 - starts
////                if(getFunctionType() != TypeConstants.DISPLAY_MODE) {
//                if(getFunctionType() != TypeConstants.DISPLAY_MODE && !isParentProposal()) {
//                //Code commented and added for bug fix case#3183 - ends
//                    selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(ACTIVITY_TYPE_HAS_CHANGED), CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
//                }else {
//                    selection = CoeusOptionPane.SELECTION_NO;
//                }
                
                if(getFunctionType() != TypeConstants.DISPLAY_MODE && !isParentProposal()) {
                    if(serverBudgetInfoBean.getActivityTypeCode() == 0){
                        // No Rate information Available in the Table
                        selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(ACTIVITY_TYPE_NOT_AVAILABLE), CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                    } else {
                        selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(ACTIVITY_TYPE_HAS_CHANGED), CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                    }
                }else {
                    selection = CoeusOptionPane.SELECTION_NO;
                }
                // 3681: Message about proposal's activity type changing - End
                htDataForBudget = new Hashtable();
                if(selection ==CoeusOptionPane.SELECTION_YES) {
                    //Set Old Budget info Bean and New One
                    oldActivityTypeCodeBudgetInfoBean = serverBudgetInfoBean;
                    htDataForBudget.put(BudgetInfoBean.class, budgetInfoBean);
                    //requesterBean.setDataObject(budgetInfoBean);
                    activityTypeCodeModified = true;
                }else if(selection == CoeusOptionPane.SELECTION_NO) {
                    budgetInfoBean.setActivityTypeCode(serverBudgetInfoBean.getActivityTypeCode());
                    //budgetInfoBean = serverBudgetInfoBean;
                    htDataForBudget.put(BudgetInfoBean.class, serverBudgetInfoBean);
                    //requesterBean.setDataObject(serverBudgetInfoBean);
                }
                htDataForBudget.put(CoeusConstants.IS_RELEASE_LOCK, new Boolean( isReleaseLock() ));
                requesterBean.setDataObject(htDataForBudget);
                activityTypeCode = budgetInfoBean.getActivityTypeCode();
                unitNumber = budgetInfoBean.getUnitNumber();
                requesterBean.setFunctionType(GET_BUDGET_DETAILS);
                appletServletCommunicator.setRequest(requesterBean);
                appletServletCommunicator.send();
                responderBean = appletServletCommunicator.getResponse();
                if(responderBean.isSuccessfulResponse()) {
                    Hashtable budgetData = (Hashtable)responderBean.getDataObject();
                    CoeusVector cvDisplayOption = new CoeusVector();
//                    CoeusVector cvAnnivData = new CoeusVector();
                    cvDisplayOption.add(budgetData.get(BUDGET_SUMMARY_DISPLAY_OPTION));
                    budgetData.put(BUDGET_SUMMARY_DISPLAY_OPTION, cvDisplayOption);
                     //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module -Start
//                    cvAnnivData.add(budgetData.get(ENABLE_SALARY_INFLATION_ANNIV_DATE));
//                    budgetData.put(ENABLE_SALARY_INFLATION_ANNIV_DATE, cvAnnivData); 
                     //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module -End
                    queryEngine.addDataCollection(queryKey, budgetData);
                    
                    //queryEngine.addDataCollection(queryKey , (Hashtable)responderBean.getDataObject());
                }
            }
            else {
                activityTypeCode = budgetInfoBean.getActivityTypeCode();
                unitNumber = budgetInfoBean.getUnitNumber();
                Hashtable budgetData = (Hashtable)responderBean.getDataObject();
                CoeusVector cvDisplayOption = new CoeusVector();
//                CoeusVector cvAnnivData = new CoeusVector();
                cvDisplayOption.add(budgetData.get(BUDGET_SUMMARY_DISPLAY_OPTION));
                budgetData.put(BUDGET_SUMMARY_DISPLAY_OPTION, cvDisplayOption);
                 //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module -Start
//                cvAnnivData.add(budgetData.get(ENABLE_SALARY_INFLATION_ANNIV_DATE));
//                budgetData.put(ENABLE_SALARY_INFLATION_ANNIV_DATE, cvAnnivData);
                 //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module -End
                queryEngine.addDataCollection(queryKey, budgetData);
            }
            
            dataFetched = true;
            
        }else {
            //System.out.println("Could Not Fetch Data From Server");
        }
    }
    
    /** Creates a new instance of BudgetBaseWindowController */
    private BudgetBaseWindowController() {
        initComponents();
    }
    
    /** instantiates all required Controllers for tabs and
     * sets up the budget related tabbed panes.
     */
    private void initComponents() {
        budgetInfoBean.setActivityTypeCode(activityTypeCode);
        budgetInfoBean.setUnitNumber(unitNumber);
        
        if(oldActivityTypeCodeBudgetInfoBean == null) oldActivityTypeCodeBudgetInfoBean = new BudgetInfoBean();
        
        //if in Add Mode do the initialization of creating budget Periods
        if(getFunctionType() == TypeConstants.ADD_MODE) {
            
            budgetInfoBean.setStartDate(proposalDevelopmentFormBean.getRequestStartDateInitial());
            budgetInfoBean.setEndDate(proposalDevelopmentFormBean.getRequestEndDateInitial());
            //--- Case id #2924 -start
            budgetInfoBean.setOnOffCampusFlag(true);     // default to On campus when adding a new budget version
            //--- Case id #2924 - end            
            budgetInfoBean.setAcType(TypeConstants.INSERT_RECORD);
            queryEngine.insert(queryKey, budgetInfoBean);
            
            
            if(budgetPersonData!= null && budgetPersonData.size() >0){
                for(int index = 0; index < budgetPersonData.size(); index++){
                    BudgetPersonsBean budgetPersonsBean = (BudgetPersonsBean)budgetPersonData.get(index);
                    budgetPersonsBean.setRowId(index);
                    queryEngine.insert(queryKey,budgetPersonsBean);
                }
            }
            
            
            //Set Rates
            /*RequesterBean requesterBean = new RequesterBean();
            requesterBean.setDataObject(budgetInfoBean);
            requesterBean.setFunctionType(GET_INSTITUTE_RATES);
            requesterBean.setUserName(mdiForm.getUserId());
             
            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL+SERVLET, requesterBean);
            appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + SERVLET);
            appletServletCommunicator.setRequest(requesterBean);
            appletServletCommunicator.send();
            ResponderBean responderBean = appletServletCommunicator.getResponse();
             
            queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
             
            if(responderBean.isSuccessfulResponse()) {
                Hashtable data = (Hashtable)responderBean.getDataObject();
                Enumeration enum = data.keys();
                Object key;
                while(enum.hasMoreElements()) {
                    key = enum.nextElement();
                    queryEngine.addCollection(queryKey, (Class)key, (CoeusVector)data.get(key));
                }
            }
             */
            
            //Setting Proposal rates ans Proposal LA Rates
            try{
                if(activityTypeCodeModified && budgetInfoBean.getVersionNumber() > 1) {
                    setProposalRatesBean();
                    setProposalLARatesBean();
                    //Clean up unwanted Beans.
                    //Since in Add Mode All proposal Beans will be in insert mode.
                    //Remove beans not in insert mode.
                    //NotEquals neInsert = new NotEquals("acType", TypeConstants.INSERT_RECORD);
                    Equals eqNull = new Equals("acType", null);
                    queryEngine.removeData(queryKey, ProposalRatesBean.class, eqNull);
                    queryEngine.removeData(queryKey, ProposalLARatesBean.class, eqNull);
                }else if(budgetInfoBean.getVersionNumber() == 1) {
                    setProposalRatesBean();
                    setProposalLARatesBean();
                    //Clean up unwanted Beans.
                    //Since in Add Mode All proposal Beans will be in insert mode.
                    //Remove beans not in insert mode.
                    //NotEquals neInsert = new NotEquals("acType", TypeConstants.INSERT_RECORD);
                    //queryEngine.removeData(queryKey, ProposalRatesBean.class, neInsert);
                    //queryEngine.removeData(queryKey, ProposalLARatesBean.class, neInsert);
                }else if(budgetInfoBean.getVersionNumber() > 1) {
                    Equals eqVersion = new Equals("versionNumber", new Integer(budgetInfoBean.getVersionNumber() - 1));
                    
                    queryEngine.setUpdate(queryKey, ProposalRatesBean.class, "acType", String.class, TypeConstants.INSERT_RECORD, eqVersion);
                    queryEngine.setUpdate(queryKey, ProposalRatesBean.class, "versionNumber", DataType.getClass(DataType.INT), new Integer(budgetInfoBean.getVersionNumber()), eqVersion);
                    
                    queryEngine.setUpdate(queryKey, ProposalLARatesBean.class, "acType", String.class, TypeConstants.INSERT_RECORD, eqVersion);
                    queryEngine.setUpdate(queryKey, ProposalLARatesBean.class, "versionNumber", DataType.getClass(DataType.INT), new Integer(budgetInfoBean.getVersionNumber()), eqVersion);
                    
                    
                }
            }catch (CoeusException coeusException) {
                coeusException.printStackTrace();
            }
            
            Calendar calStart, calEnd, calPeriodStart, calPeriodEnd;
            int startYear, endYear;
            
            calStart = Calendar.getInstance();
            calStart.setTime(proposalDevelopmentFormBean.getRequestStartDateInitial());
            
            calEnd = Calendar.getInstance();
            calEnd.setTime(proposalDevelopmentFormBean.getRequestEndDateInitial());
            
            startYear = calStart.get(Calendar.YEAR);
            endYear = calEnd.get(Calendar.YEAR);
            if(startYear < endYear) {
                //Proposal spans more thrn a year. Break up required.
                calPeriodStart = calStart;
                calPeriodEnd = Calendar.getInstance();
                int budgetPeriod = 0;
                while(true) {
                    budgetPeriod = budgetPeriod + 1;
                    BudgetPeriodBean budgetPeriodBean = new BudgetPeriodBean();
                    budgetPeriodBean.setBudgetPeriod(budgetPeriod);
                    budgetPeriodBean.setAw_BudgetPeriod(budgetPeriod);
                    budgetPeriodBean.setAcType(TypeConstants.INSERT_RECORD);
                    budgetPeriodBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                    budgetPeriodBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                    budgetPeriodBean.setStartDate(new java.sql.Date(calPeriodStart.getTimeInMillis()));
                    
                    calPeriodStart.add(Calendar.YEAR, 1);
                    calPeriodStart.add(Calendar.DATE, -1);
                    calPeriodEnd.setTimeInMillis(calPeriodStart.getTimeInMillis());
                    calPeriodStart.add(Calendar.DATE, 1);
                    
                    if(calPeriodEnd.after(calEnd) || calPeriodEnd.equals(calEnd)) {
                        budgetPeriodBean.setEndDate(proposalDevelopmentFormBean.getRequestEndDateInitial());
                        //Added for Case 3197 - Allow for the generation of project period greater than 12 months -start
                        budgetPeriodBean.setNoOfPeriodMonths(getNumberOfMonths(budgetPeriodBean.getStartDate(),budgetPeriodBean.getEndDate()));
                        //3197 - End
                        queryEngine.insert(queryKey, budgetPeriodBean);
                        break;
                    }
                    
                    budgetPeriodBean.setEndDate(new java.sql.Date(calPeriodEnd.getTimeInMillis()));
                     //Added for Case 3197 - Allow for the generation of project period greater than 12 months -start
                    budgetPeriodBean.setNoOfPeriodMonths(getNumberOfMonths(budgetPeriodBean.getStartDate(),budgetPeriodBean.getEndDate()));
                    //3197 - End
                    queryEngine.insert(queryKey, budgetPeriodBean);
                    
                }
            }else {
                //Generate 1st Period.
                BudgetPeriodBean budgetPeriodBean = new BudgetPeriodBean();
                budgetPeriodBean.setBudgetPeriod(1);
                budgetPeriodBean.setAcType(TypeConstants.INSERT_RECORD);
                budgetPeriodBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                budgetPeriodBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                budgetPeriodBean.setStartDate(proposalDevelopmentFormBean.getRequestStartDateInitial());
                budgetPeriodBean.setEndDate(proposalDevelopmentFormBean.getRequestEndDateInitial());
                queryEngine.insert(queryKey, budgetPeriodBean);
            }
        }//initialization if in Add Mode.
        else if(getFunctionType() == TypeConstants.MODIFY_MODE) {
            //Set Budget Info Bean
            try{
                BudgetInfoBean qeBudgetInfoBean = (BudgetInfoBean)queryEngine.executeQuery(queryKey, budgetInfoBean).get(0);
                qeBudgetInfoBean.setActivityTypeCode(activityTypeCode);
                qeBudgetInfoBean.setUnitNumber(unitNumber);
                //qeBudgetInfoBean.setUnitNumber(budgetInfoBean.getUnitNumber());
                
                /**Commented since this has to be set only when the BudgetInfoBean data
                 * has been modified.
                 */
                //qeBudgetInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
                //queryEngine.update(queryKey, qeBudgetInfoBean);
                
                if(activityTypeCodeModified) {
                    budgetInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
                    queryEngine.update(queryKey, budgetInfoBean);
                }
                
            }catch (CoeusException coeusException) {
                coeusException.printStackTrace();
            }
            
            //Check if Activity type Code had Been Modified And User Selected to
            //get latest Activity type Code Information
            if(activityTypeCodeModified) {
                try{
                    if(copyMode) {
                        //If in copy mode remove all proposal rates beans.
                        queryEngine.removeData(queryKey, ProposalRatesBean.class, new Equals("acType", TypeConstants.INSERT_RECORD));
                        
                    }else{
                        Equals eqOldATCode = new Equals("activityCode", new Integer(oldActivityTypeCodeBudgetInfoBean.getActivityTypeCode()));
                        queryEngine.setUpdate(queryKey, ProposalRatesBean.class, "acType", String.class, TypeConstants.DELETE_RECORD, eqOldATCode);
                    }
                    setProposalRatesBean();
                    
                }catch (CoeusException coeusException) {
                    coeusException.printStackTrace();
                }
            }
            
        }
        tbdPnBudgetTabbedPane = new CoeusTabbedPane(CoeusTabbedPane.CTRL_T);
        budgetSummaryController = new BudgetSummaryController(budgetInfoBean, proposalDevelopmentFormBean,this);
        budgetSummaryController.setFunctionType(getFunctionType());
        //Code added for bug fix case#3183 - starts
        budgetSummaryController.setParentProposal(isParentProposal());
        if(mode != TypeConstants.DISPLAY_MODE){
            budgetSummaryController.budgetSummaryForm.chkBudgetModular.setEnabled(true);
            //COEUSQA-1693 - Cost Sharing Submission - start
            budgetSummaryController.budgetSummaryForm.chkSubmitCostSharing.setEnabled(true);
            //COEUSQA-1693 - Cost Sharing Submission - end
            //Code added for case#2938 - proposal hierarchy - starts
            //For Parent proposal the following fields are enabled.
            budgetSummaryController.budgetSummaryForm.txtResidualFunds.setEditable(true);
            budgetSummaryController.budgetSummaryForm.txtTotalCostLimit.setEditable(true);
            budgetSummaryController.budgetSummaryForm.cmbURRateType.setEnabled(true);
            //Added for Case#3404-Outstanding proposal hierarchy changes - Start
            budgetSummaryController.budgetSummaryForm.cmbOHRateType.setEnabled(true);
            budgetSummaryController.budgetSummaryForm.cmbBudgetStatus.setEnabled(true);
            if(budgetSummaryController.budgetSummaryForm.chkFinal.isSelected() 
                && budgetSummaryController.budgetSummaryForm.cmbBudgetStatus.getSelectedItem()==VAL_COMPLETE){
                        budgetSummaryController.budgetSummaryForm.cmbBudgetStatus.setEnabled(true);
                        budgetSummaryController.budgetSummaryForm.chkFinal.setEnabled(false);
            }else if(!budgetSummaryController.budgetSummaryForm.chkFinal.isSelected()){ 
                budgetSummaryController.budgetSummaryForm.cmbBudgetStatus.setEnabled(false);
            }
            //Added for Case#3404-Outstanding proposal hierarchy changes - End
            budgetSummaryController.budgetSummaryForm.txtArComments.setEditable(true);
            budgetSummaryController.budgetSummaryForm.txtArComments.setBackground(
                java.awt.Color.WHITE);
            //Code added for case#2938 - proposal hierarchy - ends
            
            //COEUSQA-1693 - Cost Sharing Submission - start
            if(mode == TypeConstants.ADD_MODE){
//              budgetSummaryController.budgetSummaryForm.chkSubmitCostSharing.setEnabled(true);  
//JM              budgetSummaryController.budgetSummaryForm.chkSubmitCostSharing.setSelected(true);
              budgetSummaryController.budgetSummaryForm.chkSubmitCostSharing.setSelected(false); //JM 5-25-2011 VU wants default to be unchecked per 4.4.2
            }
            
            //COEUSQA-1693 - Cost Sharing Submission - End
        }
        //Code added for bug fix case#3183 - ends
        /** Case #1801 :Parameterize Under-recovery and cost-sharing distribution Start2
         */
        budgetSummaryController.setForceUnderRecovery(isForceUnderRecovery());
        budgetSummaryController.setForceCostSharing(isForceCostSharing());
        /**Case #1801: Parameterize Under-recovery and cost-sharing distribution End 2
         */
        //budgetSummaryController.proposalDates(proposalDevelopmentFormBean);
          
        //Modified for Case#3893 - java1.5 issues - Start
//        tbdPnBudgetTabbedPane.addTab(BUDGET_SUMMARY,budgetSummaryController.getControlledUI());
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        JPanel pnlDetail = new JPanel(layout);
        pnlDetail.add(budgetSummaryController.getControlledUI());
        JScrollPane jscrPnDetail = new JScrollPane(pnlDetail);
        //Added for COEUSDEV-241 : Funky screens in PD Budget tab for Total screen when save is pressed multiple times - Start
        //Not to display Horizotal and vertical scroll bar
        jscrPnDetail.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jscrPnDetail.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        //COEUSDEV-241 - END
        tbdPnBudgetTabbedPane.addTab(BUDGET_SUMMARY,jscrPnDetail);
        //Case#2445 - End
        
        if(copyMode) {
            budgetSummaryController.saveFormData();
            budgetSummaryController.setRefreshRequired(true);
            budgetSummaryController.refresh();
        }
        
        
        //initialize periods
        BudgetPeriodBean budgetPeriodBean = new BudgetPeriodBean();
        budgetPeriodBean.setProposalNumber(budgetInfoBean.getProposalNumber());
        budgetPeriodBean .setVersionNumber(budgetInfoBean.getVersionNumber());
        CoeusVector vecBudgetPeriodBean = null;
        try{
            vecBudgetPeriodBean = queryEngine.executeQuery(queryKey, budgetPeriodBean);
            
            if(vecBudgetPeriodBean != null && vecBudgetPeriodBean.size() > 0) {
                
                BudgetPeriodController budgetPeriodController = new BudgetPeriodController(budgetInfoBean);
                if(isParentProposal()){
                    budgetPeriodController.setParentProposal(isParentProposal());
                    budgetPeriodController.setProposalHierarchyBean(getProposalHierarchyBean());
                }
                budgetPeriodController.setFunctionType(getFunctionType());
                budgetPeriodBean = (BudgetPeriodBean)vecBudgetPeriodBean.get(0);
                budgetPeriodController.setFormData(budgetPeriodBean);
                vecBudgetPeriodController.add(budgetPeriodController);
                tbdPnBudgetTabbedPane.addTab(BUDGET_PERIOD + budgetPeriodBean.getBudgetPeriod(), budgetPeriodController.getControlledUI());
                
                //Check wheather other tabs has to be displayed.
                GreaterThan gt2 = new GreaterThan("budgetPeriod",new Integer(1));
                CoeusVector vecBudgetDetails = queryEngine.executeQuery(queryKey, BudgetDetailBean.class, gt2);
                if(vecBudgetDetails != null && vecBudgetDetails.size() > 0) {
                    //Do this loop only if other tabs has to be displayed.
                    for(int index = 1; index < vecBudgetPeriodBean.size(); index++) {
                        budgetPeriodController = new BudgetPeriodController(budgetInfoBean);
                        if(isParentProposal()){
                            budgetPeriodController.setParentProposal(isParentProposal());
                            budgetPeriodController.setProposalHierarchyBean(getProposalHierarchyBean());
                        }
                        budgetPeriodController.setFunctionType(getFunctionType());
                        budgetPeriodBean = (BudgetPeriodBean)vecBudgetPeriodBean.get(index);
                        budgetPeriodController.setFormData(budgetPeriodBean);
                        vecBudgetPeriodController.add(budgetPeriodController);
                        tbdPnBudgetTabbedPane.addTab(BUDGET_PERIOD + budgetPeriodBean.getBudgetPeriod(), budgetPeriodController.getControlledUI());
                    }
                }
            }
            else{
                BudgetPeriodController budgetPeriodController = new BudgetPeriodController(budgetInfoBean);
                if(isParentProposal()){
                    budgetPeriodController.setParentProposal(isParentProposal());
                    budgetPeriodController.setProposalHierarchyBean(getProposalHierarchyBean());
                }
                budgetPeriodController.setFunctionType(getFunctionType());
                vecBudgetPeriodController.add(budgetPeriodController);
                tbdPnBudgetTabbedPane.addTab(BUDGET_PERIOD + "1", budgetPeriodController.getControlledUI());
            }
            
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
        budgetTotalController = new BudgetTotalController(budgetInfoBean);
        budgetTotalController.setFunctionType(getFunctionType());
        tbdPnBudgetTabbedPane.addTab(BUDGET_TOTAL,  budgetTotalController.getControlledUI());
        //tbdPnBudgetTabbedPane.addTab("Proposal Hierarchy",)     
        try{
            if(isHierarcy()){
                if(isParentProposal()){
                    budgetBaseWindow.ratesMenuItem.setEnabled(false);
                    budgetBaseWindow.btnMaintainRatesForProposal.setEnabled(false);
                    //Code commented for case#2938 - proposal hierarchy
                    //For Parent proposal the following fields are enabled.
//                    budgetBaseWindow.costSharingDistributionMenuItem.setEnabled(false);
//                    budgetBaseWindow.underRecoveryDistibutionMenuItem.setEnabled(false);
                    if(hierarchyController == null){
                        hierarchyController = new ProposalHierarchyContoller(getProposalHierarchyBean());
                    }
                    hierarchyController.setFormData(unitNumber);
                    tbdPnBudgetTabbedPane.addTab("Proposal Hierarchy",  hierarchyController.getControlledUI());
                }
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        //Added for Proposal hierarchy to set Icon start by Tarique 1
        setPropHierarchyStatus();
        //Added for Proposal hierarchy to set Icon end by Tarique 1
        pnlBasePanel = new JPanel();
        pnlBasePanel.setLayout(new BorderLayout());
        pnlBasePanel.add(tbdPnBudgetTabbedPane);
        
        budgetBaseWindow.getContentPane().add(pnlBasePanel);
    }
    /** Method to set Proposal Hiearchy Icon as well as Parent Proposal Icon
     * Added By Tarique start 2
     */
    public void setPropHierarchyStatus(){
        
        if(isHierarcy()){
            ((BudgetSummaryForm)budgetSummaryController.getControlledUI())
            .lblPropHierIcon.setIcon(null);
            ((BudgetSummaryForm)budgetSummaryController.getControlledUI())
            .lblPropHieParentIcon.setIcon(new ImageIcon(getClass().getClassLoader()
            .getResource(CoeusGuiConstants.CHILD_PROP_HIE_ICON)));
            budgetBaseWindow.btnSync.setVisible(true);
            budgetBaseWindow.btnSync.setToolTipText("Sync To Parent");
            if(isParentProposal()){
                ((BudgetSummaryForm)budgetSummaryController.getControlledUI())
                .lblPropHieParentIcon.setIcon(new ImageIcon(getClass().getClassLoader()
                .getResource(CoeusGuiConstants.PARENT_PROP_HIE_ICON)));
                budgetBaseWindow.btnSync.setToolTipText("Sync All");
                budgetBaseWindow.syncToParentMenuItem.setVisible(true);
                budgetBaseWindow.budgetJustificationMenuItem.setEnabled(false);
                budgetBaseWindow.mnItmValidChk.setEnabled(true);//case 2158
//                budgetBaseWindow.budgetModularMenuItem.setEnabled(false);
                //Code commented for case#2938 - proposal hierarchy
                //For Parent proposal the following fields are enabled.
//                budgetBaseWindow.projectIncomeMenuItem.setEnabled(false);
            }else{
                budgetBaseWindow.syncToParentMenuItem.setText("Sync To Parent");
                if(getFunctionType() == TypeConstants.DISPLAY_MODE){
                    budgetBaseWindow.btnSync.setEnabled(false);
                    budgetBaseWindow.syncToParentMenuItem.setVisible(false);
                }
                budgetBaseWindow.mnItmValidChk.setEnabled(false);//case 2158
            }
            
        }else{
            ((BudgetSummaryForm)budgetSummaryController.getControlledUI())
            .lblPropHierIcon.setIcon(new ImageIcon(getClass().getClassLoader()
            .getResource(CoeusGuiConstants.NONE_ICON)));
            ((BudgetSummaryForm)budgetSummaryController.getControlledUI())
            .lblPropHieParentIcon.setIcon(null);
            budgetBaseWindow.btnSync.setVisible(false);
            //Added for Case# 2917 - Disable Sync All menu item for proposal NOT in hierarchy
            budgetBaseWindow.syncToParentMenuItem.setVisible(false);            
        }
    }
    //Added by tarique end here 2
    /** perform field formatting.
     * enabling, disabling components depending on the
     * function type.
     */
    public void formatFields() {
        if(getFunctionType() == TypeConstants.DISPLAY_MODE) {
            budgetBaseWindow.btnAddLineItem.setEnabled(false);
            budgetBaseWindow.addLineItemMenuItem.setEnabled(false);
            
            budgetBaseWindow.btnEditLineItem.setEnabled(false);
            budgetBaseWindow.editDetailsMenuItem.setEnabled(false);
            
            budgetBaseWindow.btnDeleteLineItem.setEnabled(false);
            budgetBaseWindow.deleteLineItemMenuItem.setEnabled(false);
            
            budgetBaseWindow.insertLineItemMenuItem.setEnabled(false);
            
            budgetBaseWindow.btnCalculateBudget.setEnabled(false);
            budgetBaseWindow.calculateAllPeridosMenuItem.setEnabled(false);
            budgetBaseWindow.calculateCurrentPeriodMenuItem.setEnabled(false);
            
            budgetBaseWindow.btnGenerateallPeriods.setEnabled(false);
            budgetBaseWindow.generateAllPeriodMenuItem.setEnabled(false);
            
            budgetBaseWindow.addMenuItem.setEnabled(false);
            budgetBaseWindow.deleteMenuItem.setEnabled(false);
            
            budgetBaseWindow.btnSave.setEnabled(false);
            // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
            budgetBaseWindow.mnuItmSyncCalLineItemCost.setEnabled(false);
            // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
            budgetBaseWindow.selectCostElementMenuItem.setEnabled(false);
            budgetBaseWindow.syncToPeriodCostLimitMenuItem.setEnabled(false);
            budgetBaseWindow.applytoLaterPeriodsMenuItem.setEnabled(false);
            budgetBaseWindow.adjustPeriodBoundariesMenuItem.setEnabled(false);
            //Code added for Case#3472 - Sync to Direct Cost Limit
            //For adding total direct cost limit
            budgetBaseWindow.syncToDirectCostLimitMenuItem.setEnabled(false);
            
            //bug Fix : Save Menu Item enabled when Budget Opened in display Mode - START
            budgetBaseWindow.saveMenuItem.setEnabled(false);
            //bug Fix : Save Menu Item enabled when Budget Opened in display Mode - END
            
            if(isParentProposal()){
                budgetBaseWindow.ratesMenuItem.setEanable(false);
            }
            
        }
    }
    
    /** returns controlled Componenet.
     * @return controlled Componenet.
     */
    public Component getControlledUI() {
        return budgetBaseWindow;
    }
    
    /** returns the form data
     * @return returns the form data
     */
    public Object getFormData() {
        return null;
    }
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form
     */
    public void setFormData(Object data) {
        
    }
 
    /** validate the form data/Form and returns true if
     *  validation is through else returns false.
     *  @throws CoeusUIException if some exception occurs or some validation fails.
     *  @return true if
     *  validation is through else returns false.
     */
    public boolean validate() throws CoeusUIException {
        return validate(true);
    }
    
    /** validate the form data/Form and returns true if 
     *  validation is through else returns false.
     *  @param boolean checkTotalCostLimit, if this is true, method will check 
     *  if the Total Cost of the Budget Exceeded the Total Cost Limit. 
     *  @throws CoeusUIException if some exception occurs or some validation fails.
     *  @return true if validation is through else returns false.
     */
    
    // Modified for Case# 3760:Error Message when Total Cost limit is exceeded
//    public boolean validate() throws CoeusUIException {
    private boolean validate(boolean checkTotalCostLimit) throws CoeusUIException {
        if(! budgetSummaryController.validate()){
            tbdPnBudgetTabbedPane.setSelectedIndex(0);
            return false;
        }
        
        Controller controller;
        for(int index = 0; index < vecBudgetPeriodController.size(); index++) {
            controller = (Controller)vecBudgetPeriodController.get(index);
            //Code added for Case#3472 - Sync to Direct Cost Limit
            controller.saveFormData();
            if(! controller.validate()) {
                tbdPnBudgetTabbedPane.setSelectedIndex(index + 1);
                return false;
            }
        }
       // displays the validate Messages 
        
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
        //get the budget details
        GreaterThan gtBudgetPeriod = new GreaterThan("budgetPeriod", new Integer(0));
        CoeusVector cvBudgetPeriods = new CoeusVector();
        try {
            cvBudgetPeriods = queryEngine.executeQuery(queryKey, BudgetPeriodBean.class, gtBudgetPeriod);
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
        CoeusVector cvUpdatedBudgetDetails = new CoeusVector();
        if(cvBudgetPeriods != null && !cvBudgetPeriods.isEmpty()){
            for(Object budgetPeriod : cvBudgetPeriods){
                BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)budgetPeriod;
                Equals eqBudgetPeriod = new Equals("budgetPeriod", new Integer(budgetPeriodBean.getBudgetPeriod()));
                CoeusVector cvBudgetDetails = new CoeusVector();
                try {
                    cvBudgetDetails = queryEngine.executeQuery(queryKey, BudgetDetailBean.class, eqBudgetPeriod);
                } catch (CoeusException ex) {
                    ex.printStackTrace();
                }
                cvBudgetDetails = resetOrderOfLineItemSequence(cvBudgetDetails);
                cvUpdatedBudgetDetails.addAll(cvBudgetDetails);
            }
        }
        
        //Add all the cost elements to vector vecCostElements from the budget details
        Vector vecCostElements = getCostElementsInUI(cvUpdatedBudgetDetails);
        //if it returns true then it allows to create new version of budget
        allow_save = getCostElementDetails(vecCostElements);
        //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
        String proposalNumber = budgetInfoBean.getProposalNumber();
        CoeusVector cvData = new CoeusVector();
        try {
            //If budget holds inactive types then it returns the true value
            cvData = queryEngine.getDetails(budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber(), BudgetPersonsBean.class);
        } catch (CoeusException ex) {
            
        }
        CoeusVector vecBudgetPeriodType = new CoeusVector();
        try {
            vecBudgetPeriodType = queryEngine.getDetails(budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber(), BudgetPersonnelDetailsBean.class);
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
    
        //get appointmentType data
        BudgetPersonsBean budgetPersonsBean = new BudgetPersonsBean();
        Vector appTypeData = new Vector();
        if(cvData!=null && cvData.size()>0){
            for(int index = 0; index < cvData.size(); index++) {
                budgetPersonsBean = (BudgetPersonsBean) cvData.get(index);
                if(!TypeConstants.DELETE_RECORD.equals(budgetPersonsBean.getAcType())){
                    appTypeData.add(budgetPersonsBean);
                }
            }
        }
        
        //Get periodType data
        BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = new BudgetPersonnelDetailsBean();
        Vector periodTypeData = new Vector();
        if(vecBudgetPeriodType!=null && vecBudgetPeriodType.size()>0){
            for(int indexPeriodType = 0; indexPeriodType < vecBudgetPeriodType.size(); indexPeriodType++) {
                budgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean) vecBudgetPeriodType.get(indexPeriodType);
                if(!TypeConstants.DELETE_RECORD.equals(budgetPersonnelDetailsBean.getAcType())){
                    periodTypeData.add(budgetPersonnelDetailsBean);
                }
            }
        }
        boolean allow_budget_save = getBudgetInfoForProp(appTypeData , periodTypeData );
        //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end
        if(allow_save || allow_budget_save){
            if (vecCEMessages != null && vecCEMessages.size() > 0) {
                displayCENotAvailableMessages();
            }
            allow_save = false;
        }else{
            allow_save = true;
        }
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
        // displays the validate Messages
        //Validation for Budget Total Cost Limit
        
        // if allow_save retutns true then allow for other validation
        if(allow_save){
            try {
                if(checkTotalCostLimit){
                    CoeusVector vecBudgetInfo = queryEngine.getDetails(queryKey, BudgetInfoBean.class);
                    vecBudgetInfo.remove(null);
                    BudgetInfoBean budgetInfoBean = (BudgetInfoBean)vecBudgetInfo.get(0);
                    if(budgetInfoBean.getTotalCostLimit() > 0
                            && budgetInfoBean.getTotalCostLimit() < budgetInfoBean.getTotalCost()) {
                        
                        // Case# 2975:Updating cost limit exceeded dialog box
                        // Changed the Message and Gettin Message from properties file
//                int selectedOption = CoeusOptionPane.showQuestionDialog(
//                "Total Cost of the Budget Exceeded the Total Cost Limit",
//                CoeusOptionPane.OPTION_YES_NO,
//                CoeusOptionPane.DEFAULT_YES);
                        // Case# 3760:Error Message when Total Cost limit is exceeded - Start
//                int selectedOption = CoeusOptionPane.showQuestionDialog(
//                "budgetSummary_exceptionCode.1107",
//                CoeusOptionPane.OPTION_YES_NO,
//                CoeusOptionPane.DEFAULT_YES);
                        int selectedOption = CoeusOptionPane.showQuestionDialog(
                                coeusMessageResources.parseMessageKey("budgetSummary_exceptionCode.1107"),
                                CoeusOptionPane.OPTION_YES_NO,
                                CoeusOptionPane.DEFAULT_YES);
                        // Case# 3760:Error Message when Total Cost limit is exceeded - End
                        if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                            statusWindow.setVisible(false);
                            return true;
                        }else if(selectedOption == CoeusOptionPane.SELECTION_NO) {
                            statusWindow.setVisible(false);
                            return false;
                        }
                        
                        
                        //                CoeusOptionPane.showInfoDialog(
                        //                coeusMessageResources.parseMessageKey(TOTAL_COST_EXCEED_COST_LIMIT));
                        //                statusWindow.setVisible(false);
                        //if refresh is called residual funds which is not yet set to
                        //budget info bean will be set to zero.
                        //refresh();
                        // Show the info message and then save to the data base. - Its a enhancement
                        //                return true;
                    }
                    //Code added for Case#3472 - Sync to Direct Cost Limit - starts
                    //For adding total direct cost limit
                    if(budgetInfoBean.getTotalDirectCostLimit() > 0
                            && budgetInfoBean.getTotalDirectCostLimit() < budgetInfoBean.getTotalDirectCost()) {
                        int selectedOption = CoeusOptionPane.showQuestionDialog(
                                coeusMessageResources.parseMessageKey("budgetSummary_exceptionCode.1118"),
                                CoeusOptionPane.OPTION_YES_NO,
                                CoeusOptionPane.DEFAULT_YES);
                        if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                            statusWindow.setVisible(false);
                            return true;
                        }else if(selectedOption == CoeusOptionPane.SELECTION_NO) {
                            statusWindow.setVisible(false);
                            return false;
                        }
                    }
                    //Code added for Case#3472 - Sync to Direct Cost Limit
                }
                //validation for Budget Peroid Cost limit
                CoeusVector vecBudgetPeriod = queryEngine.executeQuery(queryKey, BudgetPeriodBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
                BudgetPeriodBean budgetPeriodBean;
                int size = vecBudgetPeriod.size();
                for(int index = 0; index < size; index++) {
                    budgetPeriodBean = (BudgetPeriodBean)vecBudgetPeriod.get(index);
                    
                    //Code added for Case#3472 - Sync to Direct Cost Limit - starts
                    //For adding total direct cost limit
                    //If Budget Period Cost Limit and direct cost exceeds then show a warning message and continue saving
                    if(budgetPeriodBean.getTotalCostLimit() > 0 &&
                            budgetPeriodBean.getTotalCostLimit() < budgetPeriodBean.getTotalCost() &&
                            budgetPeriodBean.getTotalDirectCostLimit() > 0 &&
                            budgetPeriodBean.getTotalDirectCostLimit() < budgetPeriodBean.getTotalDirectCost()) {
                        CoeusOptionPane.showWarningDialog(
                                "Total Cost of the Period " + budgetPeriodBean.getBudgetPeriod() + " has exceeded the Cost Limit of the period.\n" +
                                "Direct Cost of the Period " + budgetPeriodBean.getBudgetPeriod() + " has exceeded the Direct Cost Limit of the period.");
                    }
                    //If Budget Period Cost Limit exceeds then show a warning message and continue saving
                    else if(budgetPeriodBean.getTotalCostLimit() > 0 &&
                            budgetPeriodBean.getTotalCostLimit() < budgetPeriodBean.getTotalCost()) {
                        CoeusOptionPane.showWarningDialog(
                                "Total Cost of the Period " + budgetPeriodBean.getBudgetPeriod() + " has exceeded the Cost Limit of the period.");
                    }
                    //If Budget Period Direct Cost exceeds then show a warning message and continue saving
                    else if(budgetPeriodBean.getTotalDirectCostLimit() > 0 &&
                            budgetPeriodBean.getTotalDirectCostLimit() < budgetPeriodBean.getTotalDirectCost()) {
                        CoeusOptionPane.showWarningDialog(
                                "Direct Cost of the Period " + budgetPeriodBean.getBudgetPeriod() + " has exceeded the Direct Cost Limit of the period.");
                    }
                    //Code added for Case#3472 - Sync to Direct Cost Limit - ends
                }
            } catch (CoeusException coeusException) {
                coeusException.printStackTrace();
                //            throw new CoeusUIException();
                throw new CoeusUIException(coeusException.getMessage());
            }         
        }
         return true;
    }

    /** registers components with listeners. */
    public void registerComponents() {
        
        //listen to budget bean info.
        //For Adjust Period Boundaries
        addBeanUpdatedListener(this, BudgetInfoBean.class);
        addBeanDeletedListener(this, BudgetPeriodBean.class);
        
        //Tab Listeners
        tbdPnBudgetTabbedPane.addChangeListener(this);
        //budgetBaseWindow.setDefaultCloseOperation(BudgetBaseWindow.EXIT_ON_CLOSE);
        
        // This will catch the window closing event and
        //  checks any data is modified.If any changes are done by
        // the user the system will ask for confirmation of Saving the info.
        budgetBaseWindow.addVetoableChangeListener(this);
        /*new VetoableChangeListener(){
            public void vetoableChange(PropertyChangeEvent pce)
            throws PropertyVetoException {
                if (pce.getPropertyName().equals(
                JInternalFrame.IS_CLOSED_PROPERTY)) {
                    CoeusInternalFrame frame = null;
                    boolean changed = (
                    (Boolean) pce.getNewValue()).booleanValue();
                    if (isSaveRequired() ) {
                        String msg = coeusMessageResources.parseMessageKey(
                        "saveConfirmCode.1002");
                        int confirm = CoeusOptionPane.showQuestionDialog(msg,
                        CoeusOptionPane.OPTION_YES_NO_CANCEL,
                        CoeusOptionPane.DEFAULT_YES);
                    }
                }
            }
        });*/
        
        //Set Menus
        //budgetBaseWindow.setMenu(budgetBaseWindow.getBudgetFileMenu(),0);
        //budgetBaseWindow.setMenu(budgetBaseWindow.getBudgetEditMenu(),1);
        //budgetBaseWindow.setMenu(budgetBaseWindow.getBudgetViewMenu(),2);
        //budgetBaseWindow.setMenu(budgetBaseWindow.getBudgetPeriodsMenu(),3);
        //budgetBaseWindow.setMenu(budgetBaseWindow.getBudgetItemMenu(),4);
        //budgetBaseWindow.setFrameIcon(budgetBaseWindow.mdiForm.getCoeusIcon());
        //budgetBaseWindow.setFrameToolBar(budgetBaseWindow.getBudgetToolBar());
        
        //Budget File Menu
        budgetBaseWindow.inboxMenuItem.addActionListener(this);
        budgetBaseWindow.closeMenuItem.addActionListener(this);
        budgetBaseWindow.saveMenuItem.addActionListener(this);
        
        //Commented since we are not using it in Coeus 4.0
        //budgetBaseWindow.printSetupMenuItem.addActionListener(this);
        
        budgetBaseWindow.printSummaryMenuItem.addActionListener(this);
        budgetBaseWindow.budgetSummaryMenuItem.addActionListener(this);
        budgetBaseWindow.changePasswordMenuItem.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - Start
        budgetBaseWindow.delegationsMenuItem.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - End
        budgetBaseWindow.preferencesMenuItem.addActionListener(this);
        budgetBaseWindow.exitMenuItem.addActionListener(this);
        
        budgetBaseWindow.saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_MASK));
        //Budget Edity Menu
        budgetBaseWindow.ratesMenuItem.addActionListener(this);
        budgetBaseWindow.personsMenuItem.addActionListener(this);
        budgetBaseWindow.costSharingDistributionMenuItem.addActionListener(this);
        budgetBaseWindow.underRecoveryDistibutionMenuItem.addActionListener(this);
        budgetBaseWindow.budgetJustificationMenuItem.addActionListener(this);
        budgetBaseWindow.subAwardMenuItem.addActionListener(this);
        budgetBaseWindow.syncToParentMenuItem.addActionListener(this);
        
        //Case #1625 Start 1
        budgetBaseWindow.projectIncomeMenuItem.addActionListener(this);
        //Case #1625 End 1
        //Case #1626 Start 1
        budgetBaseWindow.budgetModularMenuItem.addActionListener(this);
        //Case #1626 End 1
        //Budget View Menu
        budgetBaseWindow.customizeMenuItem.addActionListener(this);
        budgetBaseWindow.salarymenuItem.addActionListener(this);
        budgetBaseWindow.mnItmValidChk.addActionListener(this);//Case 2158
        // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
        budgetBaseWindow.mnuItmSyncCalLineItemCost.addActionListener(this);//Case 2158
        // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
        //Budget Periods Bean
        budgetBaseWindow.addMenuItem.addActionListener(this);
        budgetBaseWindow.deleteMenuItem.addActionListener(this);
        budgetBaseWindow.calculateAllPeridosMenuItem.addActionListener(this);
        budgetBaseWindow.calculateCurrentPeriodMenuItem.addActionListener(this);
        budgetBaseWindow.generateAllPeriodMenuItem.addActionListener(this);
        budgetBaseWindow.adjustPeriodBoundariesMenuItem.addActionListener(this);
        
        //Budget Item Menu
        budgetBaseWindow.addLineItemMenuItem.addActionListener(this);
        budgetBaseWindow.insertLineItemMenuItem.addActionListener(this);
        budgetBaseWindow.deleteLineItemMenuItem.addActionListener(this);
        budgetBaseWindow.editDetailsMenuItem.addActionListener(this);
        budgetBaseWindow.selectCostElementMenuItem.addActionListener(this);
        budgetBaseWindow.personnelBudgetMenuItem.addActionListener(this);
        budgetBaseWindow.syncToPeriodCostLimitMenuItem.addActionListener(this);
        budgetBaseWindow.applytoLaterPeriodsMenuItem.addActionListener(this);
        //Code added for Case#3472 - Sync to Direct Cost Limit
        //For adding total direct cost limit
        budgetBaseWindow.syncToDirectCostLimitMenuItem.addActionListener(this);
        
        //Budget ToolBar
        budgetBaseWindow.btnAddLineItem.addActionListener(this);
        budgetBaseWindow.btnEditLineItem.addActionListener(this);
        budgetBaseWindow.btnDeleteLineItem.addActionListener(this);
        budgetBaseWindow.btnPersonnelBudgetForLineItem.addActionListener(this);
        budgetBaseWindow.btnCalculateBudget.addActionListener(this);
        budgetBaseWindow.btnGenerateallPeriods.addActionListener(this);
        budgetBaseWindow.btnMaintainRatesForProposal.addActionListener(this);
        budgetBaseWindow.btnBudgetPersons.addActionListener(this);
        budgetBaseWindow.btnSave.addActionListener(this);
        budgetBaseWindow.btnClose.addActionListener(this);
        budgetBaseWindow.btnCustomizeView.addActionListener(this);
        
        //Proposal Hierarchy Enhancment Start
        budgetBaseWindow.btnSync.addActionListener(this);
        //Proposal Hierarchy Enhancment End
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        
        //set wait cursor while doing some operation and block events.
        try{
            mdiForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            blockEvents(true);
            
            if(source.equals(budgetBaseWindow.addLineItemMenuItem) || source.equals(budgetBaseWindow.btnAddLineItem)) {
                addLineItem();
            }else if(source.equals(budgetBaseWindow.insertLineItemMenuItem)) {
                insertLineItem();
            }else if(source.equals(budgetBaseWindow.deleteLineItemMenuItem) || source.equals(budgetBaseWindow.btnDeleteLineItem)) {
                deleteLineItem();
            }else if(source.equals(budgetBaseWindow.editDetailsMenuItem) || source.equals(budgetBaseWindow.btnEditLineItem)) {
                try {
                    editLineItemDetails();
                } catch (CoeusException ex) {
                    ex.printStackTrace();
                }
            }
            
            //=================added for Proposal Rate Dialog window ========
            else if(source.equals(budgetBaseWindow.ratesMenuItem) || source.equals(budgetBaseWindow.btnMaintainRatesForProposal)){
                saveFormData();
                showRates();
                
            }else if(source.equals(budgetBaseWindow.budgetJustificationMenuItem)){
                showBudgetJustification();
                
            }else if(source.equals(budgetBaseWindow.costSharingDistributionMenuItem)){
                showCostSharingDistribution();
                
            }else if(source.equals(budgetBaseWindow.personsMenuItem) || (source.equals(budgetBaseWindow.btnBudgetPersons))){
                showPersons();
            }
            
            //Case #1625 Start 2
            else if(source.equals(budgetBaseWindow.projectIncomeMenuItem) ){
                showProjectIncome();
            }//Case #1625 End 2
            //Case #1626 Start 2
            else if(source.equals(budgetBaseWindow.budgetModularMenuItem)){
                showBudgetModular();
            }//Case #1626 End 2
            
            else if(source.equals(budgetBaseWindow.saveMenuItem) || source.equals(budgetBaseWindow.btnSave)) {
                
                blockEvents(true);
                //Commented for COEUSDEV-241 : Funky screens in PD Budget tab for Total screen when save is pressed multiple times - Start
//                Thread thread = new Thread(new Runnable() {
//                    public void run() {
                //COEUSDEV-241 : End
                try{
                    if(! validate()) {
                        statusWindow.setVisible(false);
                        blockEvents(false);
                        return ;
                    }
                    //Code commented and added for bugfix case#3183 - starts
                    //If it is parent proposal, then skip the calculation
//                            statusWindow.setHeader("Calculating Budget");
                    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
                    //If allow_save returns true then only allow to save
                    if(allow_save){
                        if(!isParentProposal()){
                            statusWindow.setHeader("Calculating Budget");
                        }
                        //Code commented and added for bugfix case#3183 - ends
                        statusWindow.setFooter("Please Wait...");
                        statusWindow.display();
                        
                        budgetSummaryController.refresh();
                        saveFormData(); //Save to Query Engine
                        //Code commented and added for bugfix case#3183
                        //If it is parent proposal, then skip the calculation
//                            if(! calculate(queryKey, CALCULATE_ALL_PERIODS)) {
                        if(!isParentProposal() && ! calculate(queryKey, CALCULATE_ALL_PERIODS)) {
                            CoeusOptionPane.showErrorDialog("Server Error : Calculation failed");
                            statusWindow.setVisible(false);
                            blockEvents(false);
                            return ;
                        }
                        
                        //                        //Validation for Budget Total Cost Limit
                        //                        CoeusVector vecBudgetInfo = queryEngine.getDetails(queryKey, BudgetInfoBean.class);
                        //                        vecBudgetInfo.remove(null);
                        //                        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)vecBudgetInfo.get(0);
                        //                        if(budgetInfoBean.getTotalCostLimit() > 0 &&
                        //                        budgetInfoBean.getTotalCostLimit() < budgetInfoBean.getTotalCost()) {
                        //                            CoeusOptionPane.showErrorDialog(
                        //                            coeusMessageResources.parseMessageKey(TOTAL_COST_EXCEED_COST_LIMIT));
                        //                            statusWindow.setVisible(false);
                        //                            refresh();
                        //                            return ;
                        //                        }
                        //
                        //                        //validation for Budget Peroid Cost limit
                        //                        CoeusVector vecBudgetPeriod = queryEngine.executeQuery(queryKey, BudgetPeriodBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
                        //                        BudgetPeriodBean budgetPeriodBean;
                        //                        int size = vecBudgetPeriod.size();
                        //                        for(int index = 0; index < size; index++) {
                        //                            budgetPeriodBean = (BudgetPeriodBean)vecBudgetPeriod.get(index);
                        //                            if(budgetPeriodBean.getTotalCostLimit() > 0 &&
                        //                            budgetPeriodBean.getTotalCostLimit() < budgetPeriodBean.getTotalCost()) {
                        //                                CoeusOptionPane.showErrorDialog(
                        //                                "Total Cost of the Period " + budgetPeriodBean.getBudgetPeriod() + " has exceeded the Cost Limit of the period.");
                        //                            }
                        //                        }
                        
                        statusWindow.setHeader("Saving Budget");
                        otherCalculation = false;
                        //Update the Rate and Base calculation
                        /** gnprh - commented for Proposal hierarchy
                         * if(!otherCalculation){
                         * updateBudgetRateAndBase();
                         * }
                         * otherCalculation = false;
                         */
                        saveBudget(); //Save to Database
                        statusWindow.setHeader("Refreshing Budget Information");
                        refresh();
                    }
                    //Added for COEUSQA-3273 - end
                    statusWindow.setVisible(false);
                    blockEvents(false);
                }catch (CoeusUIException coeusUIException) {
                    coeusUIException.printStackTrace();
                    statusWindow.setVisible(false);
                    blockEvents(false);
                }catch (Exception exception) {
                    statusWindow.setVisible(false);
                    blockEvents(false);
                    //CoeusOptionPane.showErrorDialog(mdiForm, exception.getMessage());
                }
                    /*catch (CoeusException coeusException) {
                        coeusException.printStackTrace();
                    }*/
                //Commented for COEUSDEV-241 : Funky screens in PD Budget tab for Total screen when save is pressed multiple times - Start
//                    }
//                });
//                thread.start();
                //COEUSDEV-241 : End
            }else if(source.equals(budgetBaseWindow.personnelBudgetMenuItem) || source.equals(budgetBaseWindow.btnPersonnelBudgetForLineItem)) {
                personnelBudget();
            }else if(source.equals(budgetBaseWindow.applytoLaterPeriodsMenuItem)) {
                try{
                    applyToLaterPeriods();
                }catch (CoeusClientException coeusClientException){
                    CoeusOptionPane.showDialog(coeusClientException);
                }
            }else if(source.equals(budgetBaseWindow.addMenuItem)){
                blockEvents(false);
                addBudgetPeriod();
            }else if(source.equals(budgetBaseWindow.deleteMenuItem)){
                blockEvents(false);
                deleteBudgetPeriod();
            }else if(source.equals(budgetBaseWindow.generateAllPeriodMenuItem) || source.equals(budgetBaseWindow.btnGenerateallPeriods)) {
                // bug Fix 1852 - start 25-Nov-2005
                // saveFormData();
                // bug Fix 1852 - End 25-Nov-2005
                generateAllPeriods();
            }else if(source.equals(budgetBaseWindow.selectCostElementMenuItem)) {
                try{
                    selectCostElement();
                }catch (CoeusClientException coeusClientException){
                    CoeusOptionPane.showDialog(coeusClientException);
                }
            }else if(source.equals(budgetBaseWindow.btnCalculateBudget) || source.equals(budgetBaseWindow.calculateAllPeridosMenuItem)) {
                // Case# 3760:Error Message when Total Cost limit is exceeded -Start
//                calculateBudget();
                calculateBudget(true);
                // Case# 3760:Error Message when Total Cost limit is exceeded -End
            }else if(source.equals(budgetBaseWindow.calculateCurrentPeriodMenuItem)) {
                calculateCurrentPeriod();
            }else if(source.equals(budgetBaseWindow.btnClose) || source.equals(budgetBaseWindow.closeMenuItem)) {
                try{
                    close();
                }catch (PropertyVetoException propertyVetoException) {
                    //Throws Veto Exception if user doesn't want to save changes.
                    //and Clicks Cancel So no need to print or do some action.
                }
            }else if(source.equals(budgetBaseWindow.customizeMenuItem) || source.equals(budgetBaseWindow.btnCustomizeView)) {
                customizeView();
            }else if(source.equals(budgetBaseWindow.adjustPeriodBoundariesMenuItem)){
                // Bug fix - Save the form datas and then open the menu item so that
                // It will get the new / fresh data.
                saveFormData();
                adjustPeriodBoundaries();
            }else if(source.equals(budgetBaseWindow.syncToPeriodCostLimitMenuItem)) {
                syncToPeriodCostLimit();
            //Code added for Case#3472 - Sync to Direct Cost Limit - starts
            //For adding total direct cost limit
            }else if(source.equals(budgetBaseWindow.syncToDirectCostLimitMenuItem)) {
                syncToDirectCostLimit();
            //Code added for Case#3472 - Sync to Direct Cost Limit - ends
            }else if(source.equals(budgetBaseWindow.salarymenuItem)) {
                viewSalaries();
            }else if(source.equals(budgetBaseWindow.underRecoveryDistibutionMenuItem)) {
                editUnderrecovery();
            }else if(source.equals(budgetBaseWindow.exitMenuItem)) {
                exit();
            }else if(source.equals(budgetBaseWindow.budgetSummaryMenuItem)) {
                showBudgetSummary();
            }else if(source.equals(budgetBaseWindow.inboxMenuItem)) {
                showInboxDetails();
            //Added for Case#3682 - Enhancements related to Delegations - Start
            }else if(source.equals(budgetBaseWindow.delegationsMenuItem)){
                displayUserDelegation();
            //Added for Case#3682 - Enhancements related to Delegations - End
            }else if(source.equals(budgetBaseWindow.preferencesMenuItem)){
                showPreference();
                //add for print summary total page
            }else if(source.equals(budgetBaseWindow.printSummaryMenuItem)){
                try{
                    printSummaryTotalPage();
                }catch(CoeusException coeusException){
                    CoeusOptionPane.showErrorDialog(coeusException.getMessage());
                    coeusException.printStackTrace();
                }
            }
            //Proposal Hierarchy Enhancment Start
            else if(source.equals(budgetBaseWindow.btnSync) || 
                    source.equals(budgetBaseWindow.syncToParentMenuItem)){
                        
                String message = "Are you sure, you want to sync the Budget";
                int answer = CoeusOptionPane.showQuestionDialog(message,
                CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
                if (answer == JOptionPane.YES_OPTION){
                    blockEvents(true);
                    Thread thread = new Thread(new Runnable() {
                        public void run() {
                            try{
                                if(! validate()) {
                                    statusWindow.setVisible(false);
                                    blockEvents(false);
                                    return ;
                                }
                                //Code commented and added for bug fix case#3183 - starts
                                //while saving parent proposal data calculation to be skipped                                
//                                statusWindow.setHeader("Calculating Budget");
                                if(!isParentProposal()){
                                    statusWindow.setHeader("Calculating Budget");
                                }
                                //Code commented and added for bug fix case#3183 - ends
                                statusWindow.setFooter("Please Wait...");
                                statusWindow.display();
                                
                                budgetSummaryController.refresh();
                                saveFormData(); //Save to Query Engine
                                if(! calculate(queryKey, CALCULATE_ALL_PERIODS)) {
                                    CoeusOptionPane.showErrorDialog("Server Error : Calculation failed");
                                    statusWindow.setVisible(false);
                                    blockEvents(false);
                                    return ;
                                }
                                statusWindow.setHeader("Saving Budget");
                                saveBudget(); //Save to Database
                                statusWindow.setHeader("Refreshing Budget Information");
                                refresh();
                                statusWindow.setHeader("Validating Budget...");
                                //Modified with case 2158: Budget Validations
                                char errType = validateForHierarchy(budgetInfoBean.getProposalNumber(),isParentProposal());
                                statusWindow.setVisible(false);
                                if(errType == VALIDATION_ERROR){
                                    blockEvents(false);
                                    return;
                                }else if(errType == VALIDATION_WARNING){
                                    int option = CoeusOptionPane.showQuestionDialog(
                                            coeusMessageResources.parseMessageKey(CONFIRM_SYNCING),
                                            CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
                                    if(option == JOptionPane.NO_OPTION){
                                        blockEvents(false);
                                        return;
                                    }
                                }
                                //2158 End
                                syncPropHierarchyBudget();
                                statusWindow.setVisible(false);
                                blockEvents(false);
                            }catch (CoeusUIException coeusUIException) {
                                coeusUIException.printStackTrace();
                                statusWindow.setVisible(false);
                                blockEvents(false);
                            }catch (Exception exception) {
                                statusWindow.setVisible(false);
                                blockEvents(false);
                                //CoeusOptionPane.showErrorDialog(mdiForm, exception.getMessage());
                            }
                        }
                    });
                    if(!isParentProposal()){
                        thread.start();
                    }
                    if(isParentProposal()){
                        //Added with COEUSDEV 197,198: Validation Checks for Syncing
                        char errType = validateForHierarchy(budgetInfoBean.getProposalNumber(),isParentProposal());
                        if(errType == VALIDATION_ERROR){
                            blockEvents(false);
                            return;
                        }else if(errType == VALIDATION_WARNING){
                            int option = CoeusOptionPane.showQuestionDialog(
                                    coeusMessageResources.parseMessageKey(CONFIRM_SYNCING),
                                    CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
                            if(option == JOptionPane.NO_OPTION){
                                blockEvents(false);
                                return;
                            }
                        }
                        //COEUSDEV 197,198 End
                        calculate(queryKey, CALCULATE_ALL_PERIODS);
                        syncPropHierarchyBudget();
                    }
                    
                }
            }
            //Proposal Hierarchy Enhancment End
            else if(source.equals(budgetBaseWindow.subAwardMenuItem)) {
                try{
                    if(canDisplayBudgetSubAward()) {  
                        // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
                        // Before opening the sub award window, non-saved datas will be saved and budget will be refreshed
                        blockEvents(true);
                        try{
                            if(! validate()) {
                                statusWindow.setVisible(false);
                                blockEvents(false);
                                return ;
                            }
                            if(!isParentProposal()){
                                statusWindow.setHeader("Calculating Budget");
                            }
                            statusWindow.setFooter("Please Wait...");
                            statusWindow.display();
                            
                            budgetSummaryController.refresh();
                            saveFormData(); //Save to Query Engine
                            if(!isParentProposal() && ! calculate(queryKey, CALCULATE_ALL_PERIODS)) {
                                CoeusOptionPane.showErrorDialog("Server Error : Calculation failed");
                                statusWindow.setVisible(false);
                                blockEvents(false);
                                return ;
                            }
                            statusWindow.setHeader("Saving Budget");
                            otherCalculation = false;
                            saveBudget();
                            statusWindow.setHeader("Refreshing Budget Information");
                            refresh();
                            statusWindow.setVisible(false);
                            blockEvents(false);
                            
                        }catch (CoeusUIException coeusUIException) {
                            coeusUIException.printStackTrace();
                            statusWindow.setVisible(false);
                            blockEvents(false);
                        }catch (Exception exception) {
                            statusWindow.setVisible(false);
                            blockEvents(false);
                        }
                        // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
                        // JM 6-26-2014 updated to pass in proposalNumber
                        BudgetSubAwardController budgetSubAwardController = new BudgetSubAwardController(getFunctionType(),budgetInfoBean.getProposalNumber());
                        // JM END
                        //Added for Case#3404-Outstanding proposal hierarchy changes - Start
                         if(isParentProposal()){
                             budgetSubAwardController.setParentProposal(isParentProposal());
                         }
                        //Added for Case#3404-Outstanding proposal hierarchy changes - End
                        // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
                        // Sets when budget period is generated. 
                        // Once period generated, only allow to enter the sub award details for a sub award
                        if(vecBudgetPeriodController != null && vecBudgetPeriodController.size()>1){
                            budgetSubAwardController.setPeriodsGenerated(true);
                        }
                        // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
                        budgetSubAwardController.setFormData(budgetInfoBean);
                        budgetSubAwardController.display();
                        // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
                        // Budget details will refreshed only when the flag is true
                        if(budgetSubAwardController.isRefreshBudgetDetails()){
                            fetchBudgetDetails();
                            refresh();
                            calculate(queryKey,-1);
                            // Added for COEUSQA-3420 : Lite does not recognized Cost Sharing from Sub Award Details screen - Start
                            // Will trigger the save action and save all the budget details and refresh the scree
                            budgetBaseWindow.btnSave.doClick();
                            // Added for COEUSQA-3420 : Lite does not recognized Cost Sharing from Sub Award Details screen - End
                        }
                        // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
                    }else {
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SAVE_BUDGET_FIRST));
                    }
                }catch (CoeusException coeusException) {
                    CoeusOptionPane.showErrorDialog(coeusException.getMessage());
                }
            }
            //Added with case 2158: Budget Validations - Start
            else if (source.equals(budgetBaseWindow.mnItmValidChk)){
                //saving the budget prior to validation
                blockEvents(true);
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        try{
                            
                            if(! validate()) {
                                statusWindow.setVisible(false);
                                blockEvents(false);
                                return ;
                            }
                            if(!isParentProposal()){
                                statusWindow.setHeader("Calculating Budget");
                            }
                            statusWindow.setFooter("Please Wait...");
                            statusWindow.display();
                            
                            budgetSummaryController.refresh();
                            saveFormData(); //Save to Query Engine
                            if(!isParentProposal() && ! calculate(queryKey, CALCULATE_ALL_PERIODS)) {
                                CoeusOptionPane.showErrorDialog("Server Error : Calculation failed");
                                statusWindow.setVisible(false);
                                blockEvents(false);
                                return ;
                            }
                            statusWindow.setHeader("Saving Budget");
                            otherCalculation = false;
                            saveBudget(); //Save to Database
                            statusWindow.setHeader("Refreshing Budget Information");
                            refresh();
                            statusWindow.setHeader("Validating Budget...");
                            char errType = performValidationChecks();
                            statusWindow.setVisible(false);
                            blockEvents(false);
                            if(errType==0){
                                CoeusOptionPane.showInfoDialog(
                                        coeusMessageResources.parseMessageKey("validationChecks_exceptionCode.1904"));
                            }
                        }catch (Exception exception) {
                            exception.printStackTrace();
                            statusWindow.setVisible(false);
                            blockEvents(false);
                        }
                    }
                });
                thread.start();
            }
            //2158 end
            // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
            else if (source.equals(budgetBaseWindow.mnuItmSyncCalLineItemCost)){
                try{
                    syncCalculatedLineItemcosts();
                }catch (CoeusException coeusExp){
                    coeusExp.printStackTrace();;
                }
            }
            // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
            
            //remove wait cursor - set default cursor and allow events.
        }finally {
            mdiForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            blockEvents(false);
        }
    }
    
    private boolean canDisplayBudgetSubAward()throws CoeusException{
        boolean canDisplay = false;
        CoeusVector cv = queryEngine.getDetails(queryKey, edu.mit.coeus.budget.bean.BudgetInfoBean.class);
        if(cv != null && cv.size() > 0) {
            cv.remove(null);
            BudgetInfoBean budgetInfoBean =  (BudgetInfoBean)cv.get(0); //there would be only one instance of BudgetInfoBean
            if(budgetInfoBean.getAcType() == null || budgetInfoBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                canDisplay = true;
            }
        }
        return canDisplay;
    }
    
    
    //Added for Case#3682 - Enhancements related to Delegations - Start
    /*
     * Display Delegations window
     */
    private void displayUserDelegation() {
        userDelegationForm = new UserDelegationForm(mdiForm,true);
        userDelegationForm.display();
    }
    //Added for Case#3682 - Enhancements related to Delegations - End
    
    // Added by surekha to implement the User Preference details
    private void showPreference(){
        if(userPreferencesForm == null) {
            userPreferencesForm = new UserPreferencesForm(mdiForm,true);
        }
        userPreferencesForm.loadUserPreferences(mdiForm.getUserId());
        userPreferencesForm.setUserName(mdiForm.getUserName());
        userPreferencesForm.display();
    }// End surekha
    
    //add for print summary total page
    private void printSummaryTotalPage()throws CoeusException{
        budgetTotalController.refresh();
        if (budgetTotalController.cvTotalPageTableData == null || budgetTotalController.cvTotalPageTableData.size() == 0 ) return;
        //        if (budgetTotalController.cvTotalPageTableData == null ) return;
        Hashtable htPrintParams = new Hashtable();
        htPrintParams.put("PROPOSAL_NUM",budgetInfoBean.getProposalNumber());
        htPrintParams.put("VERSION_NUM",""+budgetInfoBean.getVersionNumber());
        htPrintParams.put("START_DATE",budgetInfoBean.getStartDate());
        htPrintParams.put("END_DATE",budgetInfoBean.getEndDate());
        htPrintParams.put("DATA",budgetTotalController.cvTotalPageTableData);
        // Added for COEUSQA-3769 : Coeus4.5: Proposal Development Budget Report Issue - Print Total - Start
        htPrintParams.put("BUDGET_DATA", queryEngine.getDataCollection(queryKey));
        // Added for COEUSQA-3769 : Coeus4.5: Proposal Development Budget Report Issue - Print Total - End
        htPrintParams.put("HEADER_TITLE","Budget Summary Total");
        // Added for COEUSQA-1683 : Print option to display Version Comments on Budget Reports - Start        
        htPrintParams.put("SUMMARY_COMMENTS",(budgetInfoBean.getComments() != null ? budgetInfoBean.getComments() : CoeusGuiConstants.EMPTY_STRING));
        // Added for COEUSQA-1683 : Print option to display Version Comments on Budget Reports - End
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(PRINT_BUDGET_SUMMARY_TOTAL_PAGE);
        requester.setDataObject(htPrintParams);
        
        //For Streaming
        requester.setId("Budget/SummaryTotal");
        requester.setFunctionType('R');
        //For Streaming
        
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connect, requester);
        
        comm.send();
        ResponderBean responder = comm.getResponse();
        String fileName = "";
        if(responder.isSuccessfulResponse()){
//            AppletContext coeusContxt = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
            
            
            fileName = (String)responder.getDataObject();
//            System.out.println("Report Filename is=>"+fileName);
//            
//            fileName.replace('\\', '/') ; // this is fix for Mac
//            URL reportUrl = null;
//            try{
//                reportUrl = new URL( CoeusGuiConstants.CONNECTION_URL + fileName );
//                
//                
//                if (coeusContxt != null) {
//                    coeusContxt.showDocument( reportUrl, "_blank" );
//                }else {
//                    javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
//                    bs.showDocument( reportUrl );
//                }
//            }catch(MalformedURLException muEx){
//                throw new CoeusException(muEx.getMessage());
//            }catch(Exception uaEx){
//                throw new CoeusException(uaEx.getMessage());
//            }
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
    /** Releases the budget lock
     */
    private void releaseBudgetLock() throws CoeusClientException {
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType( RELEASE_BUDGET_LOCK );
        requester.setDataObject(budgetInfoBean.getProposalNumber());
        AppletServletCommunicator comm = new AppletServletCommunicator(
        CoeusGuiConstants.CONNECTION_URL+SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( !response.isSuccessfulResponse() ){
            throw new CoeusClientException(response.getMessage());
        }
    }
    
    //included by raghuSV to show the inbox details form.
    private void showInboxDetails(){
        InboxDetailForm inboxDtlForm = null;
        try{
            if( ( inboxDtlForm = (InboxDetailForm)mdiForm.getFrame(
            "Inbox" ))!= null ){
                if( inboxDtlForm.isIcon() ){
                    inboxDtlForm.setIcon(false);
                }
                inboxDtlForm.setSelected( true );
                return;
            }
            inboxDtlForm = new InboxDetailForm(mdiForm);
            inboxDtlForm.setVisible(true);
        }catch(Exception exception){
            CoeusOptionPane.showInfoDialog(exception.getMessage());
        }
    }
    
    
    public void adjustPeriodBoundaries(){
        otherCalculation = true;
        if(adjustPeriodBoundaryController == null) {
            adjustPeriodBoundaryController = new AdjustPeriodBoundaryController(
            mdiForm,true, budgetInfoBean);
        }
        
        adjustPeriodBoundaryController.setFormData(budgetInfoBean);
        adjustPeriodBoundaryController.setFunctionType(getFunctionType());
        adjustPeriodBoundaryController.display();
        // Added for Case COEUSDEV-423 / COEUSQA-2404 : Prop Dev - Error saving budget after adjust period boundaries -Start
        // Fetch the delete periods data from AdjustPeriodBoundaryController and set it to local vector.
        setVecDeletedPeriods(adjustPeriodBoundaryController.getDeletedPeriods());
        // Added for Case COEUSDEV-423 / COEUSQA-2404 : Prop Dev - Error saving budget after adjust period boundaries -End

        if(adjustPeriodBoundaryController.isPeriodAdjusted()) {
            //            BeanEvent beanEvent = new BeanEvent();
            //            beanEvent.setSource(this);
            //            beanEvent.setBean(budgetInfoBean);
            //            fireBeanUpdated(beanEvent);
            
            adjustPeriodTabPages();
            
            refresh();
            //Added for Case#2341 - Recalculate Budget if Project dates change - starts
            //To sync the budget rates
            if(CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(SYNC_MESSAGE),
                    CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES) == JOptionPane.YES_OPTION){
                try {
                    RequesterBean requesterBean  = new RequesterBean();
                    requesterBean.setFunctionType(GET_MASTER_DATA_TO_SYNC);
                    requesterBean.setDataObject(budgetInfoBean);
                    
                    AppletServletCommunicator comm = new AppletServletCommunicator(
                            CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean );
                    comm.send();
                    ResponderBean responderBean = comm.getResponse();
                    if(responderBean==null){
                        throw new CoeusClientException(COULD_NOT_CONTACT_SERVER, CoeusClientException.ERROR_MESSAGE);
                    }
                    if(responderBean.isSuccessfulResponse()) {
                        if ( responderBean !=null ){
                            Vector masterRateVector = (Vector) responderBean.getDataObjects();
                            CoeusVector vecInstituteRateBean = (CoeusVector) masterRateVector.elementAt(0);
                            CoeusVector vecInstituteLARateBean = (CoeusVector) masterRateVector.elementAt(1);
                            vecInstituteRateBean = getFilteredVector(vecInstituteRateBean);
                            vecInstituteRateBean = getFilteredVector(vecInstituteRateBean);
                            saveInstituteRates(vecInstituteRateBean);
                            saveLARates(vecInstituteLARateBean);
                        }
                    }
                } catch (CoeusClientException ex) {
                    ex.printStackTrace();
                    CoeusOptionPane.showErrorDialog(ex.getMessage());
                }
            }
            //Added for Case#2341 - Recalculate Budget if Project dates change - ends
            // Case# 3760:Error Message when Total Cost limit is exceeded - Start
//            calculateBudget();
            calculateBudget(true);
            // Case# 3760:Error Message when Total Cost limit is exceeded - End         
            isAdjusted = true;
        }
        
    }
    
    
    /** calls budget summary controllers deleteBudgetPeriod(). */
    public void deleteBudgetPeriod(){
        budgetSummaryController.deleteBudgetPeriod();
    }
    
    /** calls budget Summary Controllers addBudgetPeriod() */
    public void addBudgetPeriod(){
        int index = tbdPnBudgetTabbedPane.getSelectedIndex();
        if(index == 0){
            budgetSummaryController.addBudgetPeriod();
        }
    }
    
    /** displays proposal rates dialog. */
    public void showRates() {
        ProposalRateController proposalRateController = null;
        if(isParentProposal()){
            if(tbdPnBudgetTabbedPane.getSelectedComponent() instanceof edu.mit.coeus.propdev.gui.ProposalHierarchyForm){
                CoeusVector nodeData  = hierarchyController.getDataObject();
                if(nodeData!= null && nodeData.size() > 0){
                    Integer versionNumber = (Integer)nodeData.elementAt(0);
                    String propNumber = (String)nodeData.elementAt(1);
                    Vector dataObject = new Vector();
                    dataObject.add(0,propNumber);
                    dataObject.add(1,versionNumber);
                    proposalRateController =  new ProposalRateController(mdiForm,true,budgetInfoBean,getFunctionType(),'D',isParentProposal());
                    proposalRateController.setDataObject(dataObject);
                    proposalRateController.setUnitNumber(getUnitNumber());
                    proposalRateController.setActivityTypeCode(getActivityTypeCode());
                    proposalRateController.setFormDataForHiearchy();                    
                    
                }else{
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.
                    parseMessageKey("propHierarchy_exceptionCode.1017"));
                    return ;
                }
            }
        }else{
            // Added by chandra. setting function type as a parameter
            //COEUSQA-1689 Role Restrictions for Budget Rates - Start
            //The additional parameter unitNumber is passed as an argument to the construtor
            proposalRateController =  new ProposalRateController(mdiForm,true,budgetInfoBean,getFunctionType(),mode,unitNumber);
            proposalRateController.setUnitNumber(getUnitNumber());
            //COEUSQA-1689 Role Restrictions for Budget Rates - Start
            // Added by chandra 17-Sept-start
            // proposalRateController.setFunctionType(getFunctionType());
            // Added by chandra 17-Sept-end
            proposalRateController.setActivityTypeCode(getActivityTypeCode());  //1);
        }
        
        
        int clicked = proposalRateController.displayRates();
        if(clicked == 0 && mode == TypeConstants.ADD_MODE && !visited) {
            visited = true;
            mode = TypeConstants.MODIFY_MODE;
        }
        //Case 2453 - start
        if(proposalRateController.isCalculationChanged()){
            // Case# 3760:Error Message when Total Cost limit is exceeded -Start
//            calculateBudget();
            calculateBudget(true);
            // Case# 3760:Error Message when Total Cost limit is exceeded -End
        }//Case 2453 - End
    }
    
    /** displays budget persons dialog. */
    public void showPersons() {
        try{
            if(!isParentProposal()){
                if(budgetPersonController == null) {
                    budgetPersonController = new BudgetPersonController(mdiForm,budgetInfoBean);
                }
                budgetPersonController.setFunctionType(getFunctionType());
                budgetPersonController.display();
            }else{
                //ProposalHierarchyContoller hierarchyController  = new ProposalHierarchyContoller(getProposalHierarchyBean(),CoeusGuiConstants.BUDGET_MODULE);
                //if(hierarchyController == null){
                hierarchyController  = new ProposalHierarchyContoller(getProposalHierarchyBean());
                //}
                hierarchyController.setModule(CoeusGuiConstants.BUDGET_MODULE);
                hierarchyController.setFormData(unitNumber);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        
    }
    
    /** displays BudgetJustification dialog. */
    public void showBudgetJustification() {
        BudgetJustificationController budgetJustificationController =  new BudgetJustificationController(mdiForm,true,budgetInfoBean);
        budgetJustificationController.setFunctionType(getFunctionType());
        budgetJustificationController.display();
    }
    
    /** displays CostSharingDistribution dialog. */
    public void showCostSharingDistribution() {
        boolean fromHierarchy = false;
        Vector dataObject = new Vector();
        if(tbdPnBudgetTabbedPane.getSelectedComponent() instanceof edu.mit.coeus.propdev.gui.ProposalHierarchyForm){
            CoeusVector cvData  = hierarchyController.getDataObject();
            if(cvData!= null && cvData.size() > 0){
                int versionNum = ((Integer)cvData.elementAt(0)).intValue();
                String propNum = (String)cvData.elementAt(1);
                
                if(budgetInfoBean.getProposalNumber().equals(propNum) &&
                versionNum==budgetInfoBean.getVersionNumber()){
                    fromHierarchy = false;
                }else{
                    fromHierarchy = true;
                    dataObject.addElement(propNum);
                    dataObject.addElement(new Integer(versionNum));
                }
            }else{
                CoeusOptionPane.showInfoDialog(coeusMessageResources.
                parseMessageKey("propHierarchy_exceptionCode.1017"));
                return ;
            }
        }
        
        if(!fromHierarchy && getFunctionType() != TypeConstants.DISPLAY_MODE) {
            
            //Code commented and added for bug fix case#3183 - starts
            //while saving parent proposal data calculation to be skipped
//            statusWindow.setHeader("Calculating Budget");
            if(!isParentProposal()){
                statusWindow.setHeader("Calculating Budget");
            }
            //Code commented and added for bug fix case#3183 - ends
            statusWindow.setFooter("Please Wait...");
            statusWindow.display();
            
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    setFromCostShare(true);
//                    Hashtable data  = queryEngine.getDataCollection(queryKey);
                    if(! calculate(queryKey,-1)) {
                        setFromCostShare(false);
                        CoeusOptionPane.showErrorDialog("Server Error : Calculation failed");
                        statusWindow.setVisible(false);
                        return ;
                    }
                    setFromCostShare(false);
                    refresh();
                    
                    CostSharingDistributionController costSharingDistributionController =  new CostSharingDistributionController(mdiForm,true,budgetInfoBean);
                    costSharingDistributionController.setFunctionType(getFunctionType());
                    statusWindow.setVisible(false);
                    costSharingDistributionController.display();
                }
            });
            thread.start();
            return ;
        }
        CostSharingDistributionController costSharingDistributionController =  new CostSharingDistributionController(mdiForm,true,budgetInfoBean);
        costSharingDistributionController.setFromHierarchy(fromHierarchy);
        costSharingDistributionController.setDataObject(dataObject);
        if(!fromHierarchy){
            //Code commented and added for case#2938 - proposal hierarchy - starts
            costSharingDistributionController.setFunctionType(getFunctionType());
            //costSharingDistributionController.setFunctionType(mode);
            //Code commented and added for case#2938 - proposal hierarchy - ends
        }else{
            costSharingDistributionController.setFunctionType('D');
            
        }
        costSharingDistributionController.display();
        costSharingDistributionController = null;
    }
    
    //Case #1625 Start 3
    public void showProjectIncome(){
        ProjectIncomeController projectIncomeController = new ProjectIncomeController(mdiForm,budgetInfoBean);
        //Code commented and added for case#2938 - proposal hierarchy - starts
//        projectIncomeController.setFunctionType(getFunctionType());
        projectIncomeController.setFunctionType(mode);
        //Code commented and added for case#2938 - proposal hierarchy - ends
        projectIncomeController.display();
    }//Case #1625 End 3
    //Case #1626 Start 3
    public void showBudgetModular(){
        // commented For Budget Modular Enhancement case #2087 start 1
        //        try{
        //            BudgetModularController budgetModularController
        //                                            =new BudgetModularController(budgetInfoBean);
        //            budgetModularController.setFunctionType(getFunctionType());
        //            budgetModularController.setFormData(null);
        //            budgetModularController.display();
        //        }catch(CoeusException ce){
        //            ce.printStackTrace();
        //        }
        //Commented For Budget Modular Enhancement case #2087 end 1
        //For Budget Modular Enhancement case #2087 start 2
        BudgetPeriodBean budgetPeriodBean = new BudgetPeriodBean();
        budgetPeriodBean.setProposalNumber(budgetInfoBean.getProposalNumber());
        budgetPeriodBean .setVersionNumber(budgetInfoBean.getVersionNumber());
        CoeusVector cvPeriodBeans = null;
        try{
            cvPeriodBeans = queryEngine.executeQuery(queryKey, budgetPeriodBean);
            if(cvPeriodBeans!=null && cvPeriodBeans.size()>0){
                cvPeriodBeans = cvPeriodBeans.filter(CoeusVector.FILTER_ACTIVE_BEANS);
            }
            
            Vector vecEntireModBudController = new Vector();
            BudgetModularFormsController budgetModularFormsController;
            CumulativeBudgetInformationForm cumulativeBudgetInformationForm
            = new CumulativeBudgetInformationForm();
            Hashtable data = syncModularBudget(budgetInfoBean.getProposalNumber(),budgetInfoBean.getVersionNumber());
            BudgetModularBaseController budgetModularBaseController
            = new BudgetModularBaseController(cumulativeBudgetInformationForm,budgetInfoBean, data);
            if(isHierarchyMode()){
                budgetModularBaseController.setHierarchyMode(true);
                budgetModularBaseController.setFunctionType('M');
            }else{
                budgetModularBaseController.setHierarchyMode(false);
                budgetModularBaseController.setFunctionType(getFunctionType());
            }
            for(int index = 0; index < cvPeriodBeans.size(); index++) {
                budgetPeriodBean =(BudgetPeriodBean)cvPeriodBeans.get(index);
                budgetModularFormsController
                = new BudgetModularFormsController(budgetPeriodBean.getBudgetPeriod(), budgetInfoBean, data);
               if(isHierarchyMode()){
                   budgetModularFormsController.setFunctionType('M');
               }else{
                    budgetModularFormsController.setFunctionType(getFunctionType());
               }
                budgetModularFormsController.setFormData(null);
                ((BudgetModularForms)budgetModularFormsController.getControlledUI())
                .lblStartDateValue.setText(dtUtils.formatDate(budgetPeriodBean.getStartDate().toString(), REQUIRED_DATEFORMAT));
                ((BudgetModularForms)budgetModularFormsController.getControlledUI())
                .lblEnDateValue.setText(dtUtils.formatDate(budgetPeriodBean.getEndDate().toString(), REQUIRED_DATEFORMAT));
                
                ((BudgetModularBaseForm)budgetModularBaseController.getControlledUI()).
                tbdPnBudgetModular.addTab(BUDGET_PERIOD + budgetPeriodBean.getBudgetPeriod()
                , budgetModularFormsController.getControlledUI());
                
                vecEntireModBudController.add(budgetModularFormsController);
                budgetPeriodBean = null;
                budgetModularFormsController.setSaveRequired(true);
            }
            
            ((BudgetModularBaseForm)budgetModularBaseController.getControlledUI()).
            tbdPnBudgetModular.addTab("Cumulative", cumulativeBudgetInformationForm);
            budgetModularBaseController.setFormData(vecEntireModBudController);
            budgetModularBaseController.display();
            budgetModularBaseController.cleanUp();
            //  vecEntireModBudController = null;
            
        }catch(CoeusException exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
        //For Budget Modular Enhancement case #2087 end 2
    }
    //Case #1626 End 3
    
    public void display() {
        fetchDataFromServer();
        
        //Code added for bug fix case#3183 - starts
        //If it is parent proposal then budget should be opened in display mode.
        if(isParentProposal()){
            this.functionType = TypeConstants.DISPLAY_MODE;
        }
        setFunctionType(functionType);        
        //Code added for bug fix case#3183 - ends
        
        initComponents();
        registerComponents();
        
        budgetBaseWindow.setMenus();
        
        //Code added for bug fix case#3183 - starts
        //To save the modular budget enable the save button for parent proposal
        if(mode != TypeConstants.DISPLAY_MODE && isParentProposal()){
            budgetBaseWindow.btnSave.setEnabled(true);
            budgetBaseWindow.saveMenuItem.setEanable(true);
        }
        //Code added for bug fix case#3183 - ends
        
        //budgetBaseWindow.setSize(WIDTH ,HEIGHT);
        //budgetBaseWindow.setVisible(true);
        
        mdiForm.putFrame(CoeusGuiConstants.BUDGET_FRAME_TITLE , budgetInfoBean.getProposalNumber(), getFunctionType(), (CoeusInternalFrame)getControlledUI() );
        mdiForm.getDeskTopPane().add(getControlledUI());
        try{
            ((CoeusInternalFrame)getControlledUI()).setVisible(true);
            ((CoeusInternalFrame)getControlledUI()).setSelected(true);
            //Commented since internal frames should not be opened in maximised mode.
            //((CoeusInternalFrame)getControlledUI()).setMaximum(true);
        }catch (PropertyVetoException propertyVetoException) {
            propertyVetoException.printStackTrace();
        }
    }
    
    /** This method is used to create the tabbedpane along with the components
     * which are used in <CODE>Budget</CODE>.
     *
     */
   /* public JTabbedPane createForm() throws Exception{
    
        JTabbedPane tbdPnBudgetSummary = new JTabbedPane();
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        JPanel pnlDetail = new JPanel(layout);
        budgetSummaryForm = new BudgetSummaryForm();
        pnlDetail.add(budgetSummaryForm);
        JScrollPane jscrPn = new JScrollPane();
        jscrPn.setViewportView(pnlDetail);
        tbdPnBudgetSummary.setFont( CoeusFontFactory.getNormalFont() );
        tbdPnBudgetSummary.addTab("Summary", jscrPn );
         return tbdPnBudgetSummary;
    }
    
     public JComponent createBudgetSummaryPanel() throws Exception{
        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BorderLayout());
        pnlForm.add( createForm(), BorderLayout.CENTER );
        //pnlForm.setSize( 650, 300 );
        pnlForm.setSize( 760, 490 );
        pnlForm.setLocation(200, 200);
        return pnlForm;
    }
    
      public void showBudgetDialogForm() throws Exception{
       getContentPane().add( createBudgetSummaryPanel());
       mdiForm.putFrame( "Budget Summary "," Test", this.functionType, this );
       mdiForm.getDeskTopPane().add(this);
        setVisible(true);
        setSelected(true);
      }
    */
    
    //For Testing Purpose Only
    /*
     public static void main(String s[]){
        JFrame frame = new JFrame();
        BudgetInfoBean budgetInfoBean = new BudgetInfoBean();
        budgetInfoBean.setProposalNumber("01100580");
        budgetInfoBean.setVersionNumber(1);
        //frame.getContentPane().add(new BudgetBaseWindowController("", budgetBean).tbdPnBudgetTabbedPane);
        //frame.getContentPane().add(new BudgetBaseWindowController().tbdPnBudgetTabbedPane);
        //frame.setSize(WIDTH, HEIGHT);
        //frame.setVisible(true);
     
        QueryEngine queryEngine = QueryEngine.getInstance();
     
        int period[] = {3,2,4,1,5};
        for(int i=0; i< period.length; i++) {
            BudgetPeriodBean budgetPeriodBean = new BudgetPeriodBean();
            budgetPeriodBean.setProposalNumber("01100580");
            budgetPeriodBean.setVersionNumber(1);
            budgetPeriodBean.setBudgetPeriod(period[i]);
            budgetPeriodBean.setUpdateTimestamp(new java.sql.Timestamp(period[i]));
            queryEngine.addData(budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber(), budgetPeriodBean);
        }
     
        BudgetPeriodBean budgetPeriodBean = new BudgetPeriodBean();
        budgetPeriodBean.setProposalNumber("01100580");
        budgetPeriodBean.setVersionNumber(1);
        //budgetPeriodBean.setBudgetPeriod(1);
     
        try{
     
            CoeusVector data = queryEngine.executeQuery(budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber(),budgetPeriodBean);
            Class beanClass = budgetPeriodBean.getClass();
            beanClass.getDeclaredField("budgetPeriod");
     
            data.sort("budgetPeriod",false);
            //data.sort("updateTimestamp", true);
     
            //System.out.println("Search Complete "+data.size());
     
        }catch (Exception ex) {
            ex.printStackTrace();
        }
     
    }
     */
    
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
    
    /** Getter for property activityTypeCode.
     * @return Value of property activityTypeCode.
     */
    public int getActivityTypeCode() {
        return activityTypeCode;
    }
    
    /** Setter for property activityTypeCode.
     * @param activityTypeCode New value of property activityTypeCode.
     */
    public void setActivityTypeCode(int activityTypeCode) {
        this.activityTypeCode = activityTypeCode;
    }
    
    /** saves the Form Data.
     */
    public void saveFormData() {
        budgetSummaryController.saveFormData();
        
        Controller controller;
        for(int index = 0; index < vecBudgetPeriodController.size(); index++) {
            controller = (Controller)vecBudgetPeriodController.get(index);
            controller.saveFormData();
        }
        
    }
    
    
    /** adds a line item to the selcted period. */
    private void addLineItem() {
        int index = tbdPnBudgetTabbedPane.getSelectedIndex();
        if(index == 0 || index > vecBudgetPeriodController.size()) return;
        
        BudgetPeriodController budgetPeriodController = (BudgetPeriodController)vecBudgetPeriodController.get(index - 1);
        budgetPeriodController.addLineItem();
    }
    
    /** inserts a line item to the selcted period. */
    private void insertLineItem() {
        int index = tbdPnBudgetTabbedPane.getSelectedIndex();
        if(index == 0 || index > vecBudgetPeriodController.size()) return;
        
        BudgetPeriodController budgetPeriodController = (BudgetPeriodController)vecBudgetPeriodController.get(index - 1);
        budgetPeriodController.insertLineItem();
    }
    
    /** deletes a line item from the selcted period. */
    private void deleteLineItem() {
        int index = tbdPnBudgetTabbedPane.getSelectedIndex();
        if(index == 0 || index > vecBudgetPeriodController.size()) return;
        
        BudgetPeriodController budgetPeriodController = (BudgetPeriodController)vecBudgetPeriodController.get(index - 1);
        budgetPeriodController.deleteLineItem();
    }
    
    /** displays line item details dialog for the selected period and selected line
     * item.
     */
    private void editLineItemDetails() throws CoeusException {
        int index = tbdPnBudgetTabbedPane.getSelectedIndex();
        if(index == 0 || index > vecBudgetPeriodController.size()) return;
        
        BudgetPeriodController budgetPeriodController = (BudgetPeriodController)vecBudgetPeriodController.get(index - 1);
        budgetPeriodController.editLineItemDetails();
    }
    
    /** displays personnel information if the selected line item in the selected period
     * is of personnel category.
     */
    private void personnelBudget() {
        int index = tbdPnBudgetTabbedPane.getSelectedIndex();
        if(index == 0 || index > vecBudgetPeriodController.size()) return;
        
        BudgetPeriodController budgetPeriodController = (BudgetPeriodController)vecBudgetPeriodController.get(index - 1);
        budgetPeriodController.personnelBudget();
        
        
    }
    
    private void applyToLaterPeriods() throws CoeusClientException{
        otherCalculation = true;
        //Check if all periods are generated
        if(vecBudgetPeriodController.size() != budgetSummaryController.getPeriods()) {
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
            CAN_APPLY_AFTER_GENERATING_PERIODS
            ));
            return ;
        }
        
        int index = tbdPnBudgetTabbedPane.getSelectedIndex();
        if(index == 0 || index > vecBudgetPeriodController.size()) return;
        
        BudgetPeriodController budgetPeriodController = (BudgetPeriodController)vecBudgetPeriodController.get(index - 1);
        if(budgetPeriodController.applyToLaterPeriods()) {
            // Case# 3760:Error Message when Total Cost limit is exceeded -Start
//            calculateBudget();
            calculateBudget(true);
            // Case# 3760:Error Message when Total Cost limit is exceeded -End
        }
 
    }
    
    /** saves the budget to the Databse. */
    private void saveBudget() {
        try{
            //For testing
            /*CoeusVector vecToModify = queryEngine.executeQuery(queryKey, BudgetPersonsBean.class, new Equals("acType", null));
            BudgetPersonsBean budgetPersonsBean;
            String acTypes[] = {TypeConstants.INSERT_RECORD, TypeConstants.UPDATE_RECORD, TypeConstants.DELETE_RECORD};
            for(int index = 0; index < vecToModify.size(); index++) {
                budgetPersonsBean = (BudgetPersonsBean)vecToModify.get(index);
                budgetPersonsBean.setAcType(TypeConstants.UPDATE_RECORD);
                budgetPersonsBean.setFullName("Modified Full Name");
                queryEngine.set(queryKey, budgetPersonsBean);
            }
             */
            //end test Data
            
            //Code commented and added for bug fix case#3183 - starts
//            if(getFunctionType() == TypeConstants.DISPLAY_MODE) return ;
            if(getFunctionType() == TypeConstants.DISPLAY_MODE
                    && !isParentProposal()){
                return ;
            }
            //Code commented and added for bug fix case#3183 - ends
            /*try{
                if(! budgetSummaryController.validate()){
                    tbdPnBudgetTabbedPane.setSelectedIndex(0);
                    return ;
                }
                budgetSummaryController.saveFormData();
             
                Controller controller;
                for(int index = 0; index < vecBudgetPeriodController.size(); index++) {
                    controller = (Controller)vecBudgetPeriodController.get(index);
                    if(! controller.validate()) {
                        tbdPnBudgetTabbedPane.setSelectedIndex(index + 1);
                        return ;
                    }
                    controller.saveFormData();
                }
            }catch (CoeusUIException coeusUIException) {
                coeusUIException.printStackTrace();
                return ;
            }*/
           
            //when boolean value of allow_save is true it allows to create new version of budget       
                Hashtable budgetDetails = new Hashtable();
                Enumeration enumeration =  queryEngine.getKeyEnumeration(queryKey);
                
                Equals eqInsert = new Equals("acType", TypeConstants.INSERT_RECORD);
                Equals eqUpdate = new Equals("acType", TypeConstants.UPDATE_RECORD);
                Equals eqDelete = new Equals("acType", TypeConstants.DELETE_RECORD);
                
                Or insertOrUpdate = new Or(eqInsert, eqUpdate);
                Or insertOrUpdateOrDelete = new Or(insertOrUpdate, eqDelete);
                //System.out.println(insertOrUpdateOrDelete);
                
                Object key;
                CoeusVector data;
                boolean anythingToSave = false;
                while(enumeration.hasMoreElements()) {
                    key = enumeration.nextElement();
                    //Modified for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - starts
                    if(key.equals(BUDGET_SUMMARY_DISPLAY_OPTION)
                    || key.equals(ENABLE_SALARY_INFLATION_ANNIV_DATE)) {
                        continue;
                    }
                    //Modified for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - ends
                    // 4493: While adding a TBA appointment type should be defaulted to 12 Months - Start
                    // Modified for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
//                    if(key.equals(CoeusConstants.DEFAULT_TBA_APPOINTMENT_TYPE_CODE)) {
                    if(key.equals(CoeusConstants.DEFAULT_TBA_APPOINTMENT_TYPE_CODE) || 
                            key.equals(CoeusConstants.ENABLE_FORMULATED_COST_CALC) ||
                            key.equals(CoeusConstants.FORMULATED_COST_ELEMENTS) || 
                            key.equals(CoeusConstants.FORMULATED_TYPES) ) { // Modified for COEUSQA-1725 : End
                        continue;
                    }
                    // 4493: While adding a TBA appointment type should be defaulted to 12 Months - End
                    data = queryEngine.executeQuery(queryKey, (Class)key, insertOrUpdateOrDelete);
                    
                    if(! anythingToSave) {
                        if(data != null && data.size() > 0) anythingToSave = true;
                    }
                    
                    //Sort beans with AW Parameter - Start
                    if(key.equals(ProposalCostSharingBean.class) ||
                            key.equals(ProposalIDCRateBean.class) ||
                            key.equals(BudgetPersonsBean.class)) {
                        data.sort("acType");
                    }
                    //Sort beans with AW Parameter - End
                    
                    budgetDetails.put(key, data);
                }
                
                if(! anythingToSave) {
                    //CoeusOptionPane.showInfoDialog("Nothing To Save");
                    return ;
                }
                
                if(getFunctionType() == TypeConstants.MODIFY_MODE) {
                    budgetInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
                    
                    CoeusVector vec = queryEngine.executeQuery(queryKey, budgetInfoBean);
                    budgetInfoBean = (BudgetInfoBean)vec.get(0);
                    budgetInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
                    queryEngine.update(queryKey, budgetInfoBean);
                }
                
                CoeusVector vecBudgetInfo = queryEngine.executeQuery(queryKey, budgetInfoBean);
                BudgetInfoBean budgetInfoBean = (BudgetInfoBean)vecBudgetInfo.get(0);
                //Code added for bug fix case#3183 - starts
                if(isParentProposal()){
                    budgetInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
                //Code added for bug fix case#3183 - ends
                //String type = (getFunctionType() == TypeConstants.ADD_MODE) ? TypeConstants.INSERT_RECORD : TypeConstants.UPDATE_RECORD ;
                //budgetInfoBean.setAcType(type);
                
                vecBudgetInfo.removeAllElements();
                vecBudgetInfo.add(budgetInfoBean);
                budgetDetails.put(BudgetInfoBean.class, vecBudgetInfo);
                //System.out.println("Saving Budget ...");
                
                // Added for Case COEUSDEV-423 / COEUSQA-2404 : Prop Dev - Error saving budget after adjust period boundaries -Start
                // While saving the budget, get the deleted period data from vector, set it to hashmap and send it to the serverside.
                if(getVecDeletedPeriods() !=null && getVecDeletedPeriods().size() > 0){
                    budgetDetails.put("deletedPeriods", getVecDeletedPeriods());
                }
                // Added for Case COEUSDEV-423 / COEUSQA-2404 : Prop Dev - Error saving budget after adjust period boundaries -End
                
                //Saving
                RequesterBean requesterBean = new RequesterBean();
                budgetDetails.put(CoeusConstants.IS_RELEASE_LOCK, new Boolean(closeAfterSave));
                requesterBean.setDataObject(budgetDetails);
                //Case #1626 Start 6
                if(functionType==TypeConstants.ADD_MODE || copyMode){
                    requesterBean.setId("ADD_MODE");
                }else{
                    requesterBean.setId("");
                }
                //Case #1626 End 6
                requesterBean.setFunctionType(SAVE_BUDGET);
                requesterBean.setUserName(mdiForm.getUserId());
                
                AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL+SERVLET, requesterBean);
                appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + SERVLET);
                appletServletCommunicator.setRequest(requesterBean);
                appletServletCommunicator.send();
                ResponderBean responderBean = appletServletCommunicator.getResponse();
                if(responderBean.isSuccessfulResponse()) {
                    //CoeusOptionPane.showInfoDialog("budget Saved Successfully");
                    queryEngine.removeDataCollection(queryKey);
                    
                    Hashtable budgetData = (Hashtable)responderBean.getDataObject();
                    CoeusVector cvDisplayOption = new CoeusVector();
                    cvDisplayOption.add(budgetData.get(BUDGET_SUMMARY_DISPLAY_OPTION));
                    budgetData.put(BUDGET_SUMMARY_DISPLAY_OPTION, cvDisplayOption);
                    queryEngine.addDataCollection(queryKey, budgetData);
                    //queryEngine.addDataCollection(queryKey , (Hashtable)responderBean.getDataObject());
                    
                    CoeusVector cv = queryEngine.executeQuery(queryKey, budgetInfoBean);
                    budgetInfoBean = (BudgetInfoBean)cv.get(0);
                    //System.out.println(budgetInfoBean);
                    //System.out.println(budgetInfoBean.getUpdateTimestamp());
                    
                    //setting back the data to the Controllers. i.e refreshing Data
                /*budgetSummaryController.setRefreshRequired(true);
                budgetSummaryController.refresh();
                for(int index = 0; index < vecBudgetPeriodController.size(); index++) {
                    ((Controller)vecBudgetPeriodController.get(index)).setRefreshRequired(true);
                    ((Controller)vecBudgetPeriodController.get(index)).refresh();
                }
                budgetTotalController.setRefreshRequired(true);
                budgetTotalController.refresh();
                 */
                    //Cleaning up Old Components.
                    //budgetBaseWindow.remove(pnlBasePanel);
                    //initComponents();
                    //budgetBaseWindow.updateUI();
                    // Added on 21/10/2003 -  To save proposal details in the Proposal Details form.
                    
                    // Added for Case COEUSDEV-423 / COEUSQA-2404 : Prop Dev - Error saving budget after adjust period boundaries -Start
                    // If Budget saves successful, remove the deleted budget period data vector.
                    if(getVecDeletedPeriods() !=null && getVecDeletedPeriods().size() > 0){
                        getVecDeletedPeriods().removeAllElements();
                    }
                    // Added for Case COEUSDEV-423 / COEUSQA-2404 : Prop Dev - Error saving budget after adjust period boundaries -End
                    
                    CoeusVector cvProposalDetails;
                    try{
                        cvProposalDetails = queryEngine.getDetails(queryKey,ProposalDevelopmentFormBean.class);
                        if (cvProposalDetails != null && cvProposalDetails.size() > 0) {
                            proposalDevelopmentFormBean = (ProposalDevelopmentFormBean)
                            cvProposalDetails.get(0);
                            //proposalDetailAdminForm.setProposalDevelopmentFormBean(proposalDevelopmentFormBean);
                        }
                    }catch(CoeusException coeusException){
                        coeusException.printStackTrace();
                    }
                    
                    budgetInfoBeanModified = false;
                    
                }else {
                    CoeusOptionPane.showErrorDialog("Server Error : Could Not Save budget Details");
                    throw new RuntimeException();
                }
                //End Test Saving
          
            
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog("Could Not Save budget Details");
            throw new RuntimeException();
        }
    }
    
    /** Generates All Periods. */
    private void generateAllPeriods() {
        try{
            if(! validate()) return ;
            saveFormData();
            
            CoeusVector vecPeriods = null;
            
            Equals eqNull = new Equals("acType", null);
                    
            vecPeriods = queryEngine.getDetails(queryKey, BudgetPeriodBean.class);
            
            if(vecPeriods == null || vecPeriods.size() == 0 || (budgetSummaryController.getPeriods() == vecBudgetPeriodController.size())) return ;
            
            //Checking for Generate Periods
            BudgetPeriodBean lastBudgetPeriodBean;
            
            lastBudgetPeriodBean = new BudgetPeriodBean();
            lastBudgetPeriodBean.setBudgetPeriod(vecBudgetPeriodController.size());
            lastBudgetPeriodBean = (BudgetPeriodBean)queryEngine.executeQuery(queryKey, lastBudgetPeriodBean).get(0);
            
            Equals propNo = new Equals("proposalNumber", lastBudgetPeriodBean.getProposalNumber());
            Equals versionNo = new Equals("versionNumber", new Integer(lastBudgetPeriodBean.getVersionNumber()));
            Equals periodNo = new Equals("budgetPeriod", new Integer(lastBudgetPeriodBean.getBudgetPeriod()));
            
            NotEquals notDelete = new NotEquals("acType", TypeConstants.DELETE_RECORD);
            //Equals eqNull = new Equals("acType", null);
            
            And propVersion = new And(propNo, versionNo);
            Or notDeleteOrEqNull = new Or(notDelete, eqNull);
            And periodNotDeleteOrEqNull = new And(periodNo, notDeleteOrEqNull);
            
            And condition = new And(propVersion, periodNotDeleteOrEqNull);
            
            CoeusVector vecBudgetDetailBean = null;
            
            vecBudgetDetailBean = queryEngine.executeQuery(queryKey, BudgetDetailBean.class, condition);
            if(vecBudgetDetailBean == null || vecBudgetDetailBean.size() == 0) {
                CoeusOptionPane.showInfoDialog(THE_BUDGET_PERIOD + lastBudgetPeriodBean.getBudgetPeriod() + coeusMessageResources.parseMessageKey(DOES_NOT_HAVE_DETAIL_LINE_ITEMS));
                return ;
            }
            
            //Generate Periods
            GreaterThan newBP = new GreaterThan("budgetPeriod", new Integer(lastBudgetPeriodBean.getBudgetPeriod()));
            NotEquals notDel = new NotEquals("acType", TypeConstants.DELETE_RECORD);
            Equals eqlNull = new Equals("acType", null);
            Or notDelOrNull = new Or(notDel, eqlNull);
            
            And gtAndNotEqDel = new And(newBP, notDelOrNull);
            
            CoeusVector vecNewBudgetPeriods = queryEngine.executeQuery(queryKey, BudgetPeriodBean.class, gtAndNotEqDel);
            
            //Copy BudgetDetails to these Periods
            BudgetDetailBean newBudgetDetailBean = null;
            BudgetDetailCalAmountsBean newBudgetDetailCalAmountsBean = null;
            
            BudgetPersonnelDetailsBean newBudgetPersonnelDetailsBean = null;
            BudgetPersonnelCalAmountsBean newBudgetPersonnelCalAmountsBean = null;
            
            CoeusVector vecCalAmts, vecBudgetPersonnelDetailsBean, vecBudgetPersonnelCalAmountsBean;
            
            //Added for Case # 3185 Generating Future Periods - Start
            Date srcStrDate = lastBudgetPeriodBean.getStartDate();
            Date srcEndDate = lastBudgetPeriodBean.getEndDate();
            //Added for Case # 3185 Generating Future Periods - end
            
            for(int index = 0; index < vecNewBudgetPeriods.size(); index++) {
                BudgetPeriodBean newBudgetPeriodBean = (BudgetPeriodBean)vecNewBudgetPeriods.get(index);
                
                //Condition change since we need to copy from the prev period only
                periodNo = new Equals("budgetPeriod", new Integer(newBudgetPeriodBean.getBudgetPeriod() - 1));
                periodNotDeleteOrEqNull = new And(periodNo, notDeleteOrEqNull);
                condition = new And(propVersion, periodNotDeleteOrEqNull);
                
                //Copy BudgetDetails to this Period
                for(int detailIndex = 0; detailIndex < vecBudgetDetailBean.size(); detailIndex++) {
                    newBudgetDetailBean = (BudgetDetailBean)vecBudgetDetailBean.get(detailIndex);
                    newBudgetDetailBean.setBudgetPeriod(newBudgetPeriodBean.getBudgetPeriod());
                    //Added for Case #3185 Generate Future Periods - Start
                    BudgetDetailBean firstBudgetDetailBean = new BudgetDetailBean();
                    firstBudgetDetailBean.setBudgetPeriod(1);
                    firstBudgetDetailBean.setLineItemNumber(newBudgetDetailBean.getLineItemNumber());
                    firstBudgetDetailBean = (BudgetDetailBean)queryEngine.executeQuery(queryKey, firstBudgetDetailBean).get(0);
                    // Case# 3772: Generate all periods on persons not referencing correct period end date - Start
                    if(srcStrDate.equals(firstBudgetDetailBean.getLineItemStartDate())
                            && srcEndDate.equals(firstBudgetDetailBean.getLineItemEndDate())){
                        // If the Line Item Start Date and End Date are equal to the Start Date and End Date of the 
                        // first period of the Budget,set the Start Date and End Date of the Budget Period to the Line
                        // Item Start and End Date, for the future periods.
                        newBudgetDetailBean.setLineItemStartDate(newBudgetPeriodBean.getStartDate());
                        newBudgetDetailBean.setLineItemEndDate(newBudgetPeriodBean.getEndDate());
                    } else {
                        // Case# 3772: Generate all periods on persons not referencing correct period end date - End
                        int diffMonths = DateUtils.calculateDateDiff(Calendar.MONTH, srcStrDate, firstBudgetDetailBean.getLineItemStartDate());
                        Date newLiStartDate = calculateStartDate(newBudgetPeriodBean.getStartDate(), firstBudgetDetailBean.getLineItemStartDate(), diffMonths);
                        diffMonths = DateUtils.calculateDateDiff(Calendar.MONTH, firstBudgetDetailBean.getLineItemStartDate(), firstBudgetDetailBean.getLineItemEndDate());
                        Date newLiEndDate = calculateEndDate(newLiStartDate, firstBudgetDetailBean.getLineItemEndDate(), diffMonths);
                        if(newLiStartDate != null && newLiEndDate != null) {
                            if(newLiStartDate.after(newBudgetPeriodBean.getEndDate()) || newLiEndDate.before(newBudgetPeriodBean.getStartDate())) {
                                continue;
                            }
                            if(newLiStartDate.before(newBudgetPeriodBean.getStartDate())) {
                                newLiStartDate = newBudgetPeriodBean.getStartDate();
                            }
                            if(newLiEndDate.after(newBudgetPeriodBean.getEndDate())) {
                                newLiEndDate = newBudgetPeriodBean.getEndDate();
                            }
                            newBudgetDetailBean.setLineItemStartDate(new java.sql.Date(newLiStartDate.getTime()));
                            newBudgetDetailBean.setLineItemEndDate(new java.sql.Date(newLiEndDate.getTime()));
                        } else {
                            //Added for Case #3185 - End
                            newBudgetDetailBean.setLineItemStartDate(newBudgetPeriodBean.getStartDate());
                            newBudgetDetailBean.setLineItemEndDate(newBudgetPeriodBean.getEndDate());
                        }
                    }
                    //Setting Based on LIne Item - Start
                    newBudgetDetailBean.setBasedOnLineItem(newBudgetDetailBean.getLineItemNumber());
                    //Setting Based on LIne Item - End
                    
                    newBudgetDetailBean.setAcType(TypeConstants.INSERT_RECORD);
                    
                    BudgetDetailBean prevBudgetDetailBean;
                    
                    prevBudgetDetailBean = new BudgetDetailBean();
                    prevBudgetDetailBean.setBudgetPeriod(newBudgetDetailBean.getBudgetPeriod() - 1);
                    prevBudgetDetailBean.setLineItemNumber(newBudgetDetailBean.getLineItemNumber());
                    CoeusVector cvData = (CoeusVector)queryEngine.executeQuery(queryKey, prevBudgetDetailBean);
                    double lineItemCost = 0.0;
                    if(cvData != null && cvData.size() > 0) {
                        prevBudgetDetailBean = (BudgetDetailBean)queryEngine.executeQuery(queryKey, prevBudgetDetailBean).get(0);
                        lineItemCost = prevBudgetDetailBean.getLineItemCost();
                    }
                    
                    Equals eqBudgetPeriod = new Equals("budgetPeriod", new Integer(newBudgetDetailBean.getBudgetPeriod() - 1));
                    Equals eqLineItemNo = new Equals("lineItemNumber", new Integer(newBudgetDetailBean.getLineItemNumber()));
                    And eqBudgetPeriodAndEqLineItemNo = new And(eqBudgetPeriod, eqLineItemNo);
                    CoeusVector vecPersonnelLI = queryEngine.executeQuery(queryKey, BudgetPersonnelDetailsBean.class, eqBudgetPeriodAndEqLineItemNo);
                    
                    /**
                     *if line item contains personnel line items.
                     *then line item cost will be set to 0.
                     *correct cost will be set during calculation.
                     */
                    if(vecPersonnelLI != null && vecPersonnelLI.size() > 0) {
                        lineItemCost = 0;
                    }
                    BudgetPeriodController periodController = new BudgetPeriodController();
                    boolean tuitionFeeCalculation = false;
                    if(periodController.getAutoCalculation(newBudgetDetailBean) && periodController.getTuitionFeeAutoCalculation()){
                        double rate = periodController.calculateUnitRates(newBudgetDetailBean, budgetInfoBean);
                        if(rate != 0.0) {
                            tuitionFeeCalculation = true;
                            lineItemCost = rate;
                        }
                    } 
                    if(!tuitionFeeCalculation) {
                    //Apply inflation only if line item does not contain personnel line item
                    if (newBudgetDetailBean.isApplyInRateFlag() && (vecPersonnelLI == null || vecPersonnelLI.size() == 0)) {
                        //Cost Calculation
                        Equals eqCe  = new Equals("costElement", newBudgetDetailBean.getCostElement());
                        Equals eqInflation = new Equals("rateClassType","I");
                        And ceAndInflation = new And(eqCe, eqInflation);
                        //Check for inflation for the Cost Element.
                        //Get ValidCERateTypesBean From Server Side.
                        CoeusVector vecValidCERateTypes = ((BudgetPeriodController)vecBudgetPeriodController.get(0)).getValidCERateTypes(newBudgetDetailBean.getCostElement());
                        CoeusVector vecCE = vecValidCERateTypes.filter(ceAndInflation);//queryEngine.executeQuery(queryKey, ValidCERateTypesBean.class, ceAndInflation);
                        
                        if(vecCE != null && vecCE.size() > 0) {
                            Date startDate, endDate;
                            
                            
                            startDate = prevBudgetDetailBean.getLineItemStartDate();//newBudgetDetailBean.getLineItemStartDate();
                            //endDate = prevBudgetDetailBean.getLineItemEndDate();//newBudgetDetailBean.getLineItemEndDate();
                            endDate = newBudgetDetailBean.getLineItemStartDate();
                            
                            //double perDayCost = prevBudgetDetailBean.getLineItemCost() / ((endDate.getTime() - startDate.getTime())/86400000 + 1) ;
                            
                            ValidCERateTypesBean validCERateTypesBean  = (ValidCERateTypesBean)vecCE.get(0);
                            
                            Equals eqRC = new Equals("rateClassCode", new Integer(validCERateTypesBean.getRateClassCode()));
                            Equals eqRT = new Equals("rateTypeCode", new Integer(validCERateTypesBean.getRateTypeCode()));
                            
                            GreaterThan gtSD = new GreaterThan("startDate", startDate);
                            LesserThan ltED = new LesserThan("startDate", endDate);
                            Equals eqED = new Equals("startDate", endDate);
                            Or ltEDOrEqED = new Or(ltED, eqED);
                            
                            And ltOrEqEDAndGtSD = new And(ltEDOrEqED, gtSD);
                            
                            //Equals eqED = new Equals("startDate", endDate);
                            //--------------------------------------------------------
                            And rcAndRt = new And(eqRC, eqRT);
                            
                            //Or ltEdOrEqED = new Or(ltED,eqED);
                            //And gtSdAndltEdOrEqED = new And(gtSD, ltEdOrEqED);
                            
                            And rcAndRtAndLtOrEqEDAndGtSD = new And(rcAndRt, ltOrEqEDAndGtSD);
                            
                            //And propRatesCondition = new And(rcAndRt, gtSdAndltEdOrEqED);
                            
                            CoeusVector vecPropInflationRates =  queryEngine.executeQuery(queryKey, ProposalRatesBean.class, rcAndRtAndLtOrEqEDAndGtSD);
                            
                            if(vecPropInflationRates != null && vecPropInflationRates.size() > 0) {
                                //Sort so that the recent date comes first
                                vecPropInflationRates.sort("startDate", false);
                                 
                                //Added for COEUSQA-2377 Inflation off campus rate applied no matter the flag setting -start
                                boolean defaultIndicator = budgetInfoBean.isDefaultIndicator();                                
                                boolean validOnOffCampusFlag = false;
                                if(defaultIndicator){
                                   validOnOffCampusFlag = newBudgetDetailBean.isOnOffCampusFlag();
                                }else{
                                    validOnOffCampusFlag = budgetInfoBean.isOnOffCampusFlag();
                                }
                                 Equals eqOnOffCampus = new Equals("onOffCampusFlag", validOnOffCampusFlag);
                                 CoeusVector cvProposalRates = vecPropInflationRates.filter(eqOnOffCampus);//queryEngine.executeQuery(queryKey, ProposalRatesBean.class, eqBreakUpSD);
                                 ProposalRatesBean proposalRatesBean = (ProposalRatesBean)cvProposalRates.get(0);
                                //Added for COEUSQA-2377 - end
                                
                              //  ProposalRatesBean proposalRatesBean = (ProposalRatesBean)vecPropInflationRates.get(0);
                                double applicableRate = proposalRatesBean.getApplicableRate();
                                lineItemCost = lineItemCost * (100 + applicableRate) / 100;
                                // bug Fix 1852 - start 25-Nov-2005
                                lineItemCost = (double)Math.round(lineItemCost*Math.pow(10.0, 2) ) / 100;
                                // bug Fix 1852 - End 25-Nov-2005
                                /*Vector vecBreakUpBoundaries = LineItemCalculator.createBreakupBoundaries(vecPropInflationRates, startDate, endDate);
                                for(int boundaryIndex = 0; boundaryIndex < vecBreakUpBoundaries.size(); boundaryIndex++) {
                                    Boundary boundary = (Boundary)vecBreakUpBoundaries.get(boundaryIndex);
                                    Equals eqBreakUpSD = new Equals("startDate", boundary.getStartDate());
                                    CoeusVector vec = vecPropInflationRates.filter(eqBreakUpSD);//queryEngine.executeQuery(queryKey, ProposalRatesBean.class, eqBreakUpSD);
                                    if(vec != null && vec.size() > 0) {
                                        double inflation = (boundary.getDateDifference() * perDayCost * proposalRatesBean.getApplicableRate()) / 100;
                                 
                                        newBudgetDetailBean.setLineItemCost(prevBudgetDetailBean.getLineItemCost() + inflation);
                                    }//End If vec != null
                                 
                                }//End For
                                 */
                            }//End For vecPropInflationRates != null ...
                        }//End If vecCE != null ...
                    }//Apply Inflation check ends here
                    }
                    newBudgetDetailBean.setLineItemCost(lineItemCost);
                    queryEngine.insert(queryKey, newBudgetDetailBean);
                    
                }//End For Copy Budget Detail Beans
                
                //Modified for COEUSQA-3080 Could not save budget details" error -start
                // Problem is when some lineitem does not fit into the particular period, 
                // calculated amounts for all the line item (including does not fit) were copied to new period 
                //and getting OSPA.FK_BUDGET_DETAILS_CAL_AMTS violated - parent key not found Error
                // Fix is extracting all the line item for the current period and getting corresponding cal amount and update to query engine.                
                
                Equals newBudgetPrd = new Equals("budgetPeriod", new Integer(newBudgetPeriodBean.getBudgetPeriod()));
//                CoeusVector cvBudgetDetails = queryEngine.getDetails(queryKey, BudgetDetailBean.class);
                //Copy Budget Detail Cal Amts Beans
//                vecCalAmts = queryEngine.executeQuery(queryKey, BudgetDetailCalAmountsBean.class, condition);
//                if(vecCalAmts == null || vecCalAmts.size() == 0) vecCalAmts = new CoeusVector();
                
               CoeusVector cvBudgetDetails = queryEngine.executeQuery(queryKey, BudgetDetailBean.class, newBudgetPrd);
                 
               for(Object obj : cvBudgetDetails){
                  BudgetDetailBean budDetailBean = (BudgetDetailBean) obj;
                  Equals lineItemNo = new Equals("LineItemNumber", new Integer(budDetailBean.getLineItemNumber()));
                  And conditionWithLineItemNo = new And(condition, lineItemNo);
                                    
                  CoeusVector vecCalAmts2 = queryEngine.executeQuery(queryKey, BudgetDetailCalAmountsBean.class, conditionWithLineItemNo);
                     if(vecCalAmts2 == null || vecCalAmts2.size() == 0) vecCalAmts2 = new CoeusVector();
                  for(int calAmtsIndex = 0; calAmtsIndex < vecCalAmts2.size(); calAmtsIndex++) {
                    newBudgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)vecCalAmts2.get(calAmtsIndex);
                    newBudgetDetailCalAmountsBean.setBudgetPeriod(newBudgetPeriodBean.getBudgetPeriod());
                    newBudgetDetailCalAmountsBean.setAcType(TypeConstants.INSERT_RECORD);
                    queryEngine.insert(queryKey, newBudgetDetailCalAmountsBean);
                }//End For Copy Budget Detail Cal Amts Beans
                                    
               }
                              
//                for(int calAmtsIndex = 0; calAmtsIndex < vecCalAmts.size(); calAmtsIndex++) {
//                    newBudgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)vecCalAmts.get(calAmtsIndex);
//                    newBudgetDetailCalAmountsBean.setBudgetPeriod(newBudgetPeriodBean.getBudgetPeriod());
//                    newBudgetDetailCalAmountsBean.setAcType(TypeConstants.INSERT_RECORD);
//                    queryEngine.insert(queryKey, newBudgetDetailCalAmountsBean);
//                }//End For Copy Budget Detail Cal Amts Beans
                
                //Modified for COEUSQA-3080 -End
               
               // Added for Case 3197 - Allow for the generation of project period greater than 12 months -start                
               BudgetPersonnelDetailsBean personnelDetailsBean = null; 
               Date personOldStartDate = null;
               Date personOldEndDate = null;     
               // Added for COEUSDEV-419 / COEUSQA-2402 Prop dev - generate all periods not copying personnel lines when periods > 12 months
               Date previousPeriodStartDate = null;
               Date previousPeriodEndDate = null;  
               // COESUDEV-419 / COEUSQA-2402  -End
               // 3197 - end
               
               //Copy Personnel Detail Beans
                vecBudgetPersonnelDetailsBean = queryEngine.executeQuery(queryKey, BudgetPersonnelDetailsBean.class, condition);
                if(vecBudgetPersonnelDetailsBean == null || vecBudgetPersonnelDetailsBean.size() == 0) vecBudgetPersonnelDetailsBean = new CoeusVector();
                for(int personelIndex = 0; personelIndex < vecBudgetPersonnelDetailsBean.size(); personelIndex++) {
                    newBudgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean)vecBudgetPersonnelDetailsBean.get(personelIndex);
                    newBudgetPersonnelDetailsBean.setBudgetPeriod(newBudgetPeriodBean.getBudgetPeriod());
                    // Modified for Calculating new Personal Line Item Start DAte using dateAdd() of Date Utils
//                    Date pliStartDate = newBudgetPersonnelDetailsBean.getStartDate();
//                    Calendar calendar= Calendar.getInstance();
//                    calendar.setTime(pliStartDate);
//                    calendar.add(Calendar.YEAR, 1);
//                    pliStartDate = calendar.getTime();
                    
                // Added for Case 3197 - Allow for the generation of project period greater than 12 months -start    
                    
                    Vector vecBudPersonnelDetailsBean = queryEngine.executeQuery(queryKey, BudgetPersonnelDetailsBean.class, condition);
                    if(vecBudPersonnelDetailsBean !=null && vecBudPersonnelDetailsBean.size() > personelIndex){
                        personnelDetailsBean = (BudgetPersonnelDetailsBean)vecBudPersonnelDetailsBean.get(personelIndex);
                        personOldStartDate = personnelDetailsBean.getStartDate();
                        personOldEndDate = personnelDetailsBean.getEndDate();
                    }                    
//                  Date pliStartDate = dtUtils.dateAdd(Calendar.YEAR, newBudgetPersonnelDetailsBean.getStartDate(), 1);
                    // COEUSDEV-419 / COEUSQA-2402  Prop dev - generate all periods not copying personnel lines when periods > 12 months -Start
                    //If Person StartDate and End Date is same as Period Start and End Date then Create the person in new period with same start and end data as new period.
                    Date pliStartDate = null;
                    Date pliEndDate = null;
                   Vector  vecPreviousBudgetPeriodBean = queryEngine.executeQuery(queryKey, BudgetPeriodBean.class, condition);
                   if(vecPreviousBudgetPeriodBean !=null && vecPreviousBudgetPeriodBean.size() >0 ){
                       BudgetPeriodBean previousBudgetPeriodBean = (BudgetPeriodBean)vecPreviousBudgetPeriodBean.get(0);
                       previousPeriodStartDate = previousBudgetPeriodBean.getStartDate();
                       previousPeriodEndDate = previousBudgetPeriodBean.getEndDate();
                   }
                   if(previousPeriodStartDate.equals(personOldStartDate) && previousPeriodEndDate.equals(personOldEndDate) ){
                       pliStartDate = newBudgetPeriodBean.getStartDate();
                       pliEndDate = newBudgetPeriodBean.getEndDate();
                   }else{
                       // If Person StartDate is same as Period Start Date and End is not then create the person in new period with same date of new period and
                       // end date = StartDate + no. of months
                       if(previousPeriodStartDate.equals(personOldStartDate) && !previousPeriodEndDate.equals(personOldEndDate) ){
                           pliStartDate = newBudgetPeriodBean.getStartDate();
                           pliEndDate =  getPersonEndDateforPeriod(newBudgetPeriodBean.getStartDate(), personOldStartDate, personOldEndDate, pliStartDate);
                       }else{
                           // Modified for COEUSQA-3038 -CLONE - Leap Year Personnel Budget Details Window -Start
                           // If Person Start and Period Date does not match, using existing logic.
                           //Modified for COEUSQA-3422 Budget Persons Line Item Details END DATES match Budget Period END DATES in 
                           //Leap Years using February start -Start
                           pliStartDate =  getValidPersonDateforPeriod(newBudgetPeriodBean.getStartDate(), newBudgetPeriodBean.getEndDate(), personnelDetailsBean.getStartDate(), null
                                   , personOldStartDate, personOldEndDate, previousPeriodStartDate , previousPeriodEndDate, null );
                           // Date pliEndDate = dtUtils.dateAdd(Calendar.YEAR, newBudgetPersonnelDetailsBean.getEndDate(), 1);
                           pliEndDate =  getValidPersonDateforPeriod(newBudgetPeriodBean.getStartDate(), newBudgetPeriodBean.getEndDate(), null ,personnelDetailsBean.getEndDate()
                           , personOldStartDate, personOldEndDate, previousPeriodStartDate , previousPeriodEndDate, pliStartDate );
                           //Modified for COEUSQA-3422  -End  
                           // Modified for COEUSQA-3038 -CLONE - Leap Year Personnel Budget Details Window -end
                       }// end of inner condition else
                   }// end of outer condition else
                    // COEUSDEV-419 / COEUSQA-2402  Prop dev - generate all periods not copying personnel lines when periods > 12 months -End
                    // 3197 -end
                    
                    //since start date for line item will be same as start date of period.
                    //while generating periods. we can take period startt date.
                   
                 if(newBudgetPeriodBean.getStartDate().compareTo(pliStartDate) <= 0 && newBudgetPeriodBean.getEndDate().compareTo(pliStartDate) >= 0 && pliEndDate.compareTo(newBudgetPeriodBean.getStartDate()) > 0) {
                        newBudgetPersonnelDetailsBean.setStartDate(new java.sql.Date(pliStartDate.getTime()));
                        //set End Date
                        // Case# 2767:Leap Year, Personnel Budget Details Window - Start
//                        Date pliEndDate = newBudgetPersonnelDetailsBean.getEndDate();
//                        calendar.setTime(pliEndDate);
//                        calendar.add(Calendar.YEAR, 1);
//                        pliEndDate = calendar.getTime();
                        
                        // Added for Case 3197 - Allow for the generation of project period greater than 12 months -start           
//                       // Date pliEndDate = dtUtils.dateAdd(Calendar.YEAR, newBudgetPersonnelDetailsBean.getEndDate(), 1);
//                         Date pliEndDate =  getValidPersonDateforPeriod(newBudgetPeriodBean.getStartDate(), newBudgetPeriodBean.getEndDate(), null ,personnelDetailsBean.getEndDate(), personOldStartDate, personOldEndDate);
//                        // 3197 -end     
                         
                        // Case# 2767:Leap Year, Personnel Budget Details Window - End
                        if(newBudgetPeriodBean.getEndDate().compareTo(pliEndDate) < 0) {
                            pliEndDate = newBudgetPeriodBean.getEndDate();
                        }
                       
                        newBudgetPersonnelDetailsBean.setEndDate(new java.sql.Date(pliEndDate.getTime()));
                        
                        newBudgetPersonnelDetailsBean.setAcType(TypeConstants.INSERT_RECORD);
                        queryEngine.insert(queryKey, newBudgetPersonnelDetailsBean);
                        
                        //Copy Budget Personned Details Cal Amts
                        And conditionAndLINo = new And(condition,
                        new Equals("lineItemNumber", new Integer(newBudgetPersonnelDetailsBean.getLineItemNumber())));
                        // Modified for case COEUSDEV 302 - ERROR saving budget: > 12 Month budget generation - Start
                        // If same person is added multiple times (for eg. 3 times) in one lineitem, 
                        // During generate all periods out of 3 it has match only 2 items. Cal amount has to update only to 2 items. 
                        // Problem Person Number is not taken care in PersonnelCalAmount updation.
                        And condandLINoAndPersonNum = new And (conditionAndLINo,  
                          new Equals("personNumber", new Integer(newBudgetPersonnelDetailsBean.getPersonNumber())));
                        vecBudgetPersonnelCalAmountsBean = queryEngine.executeQuery(queryKey, BudgetPersonnelCalAmountsBean.class, condandLINoAndPersonNum);
                        // Modified for case COEUSDEV 302 - End
                        if(vecBudgetPersonnelCalAmountsBean == null || vecBudgetPersonnelCalAmountsBean.size() == 0) vecBudgetPersonnelCalAmountsBean = new CoeusVector();
                        for(int personnelCalAmtsIndex = 0; personnelCalAmtsIndex < vecBudgetPersonnelCalAmountsBean.size(); personnelCalAmtsIndex++) {
                            newBudgetPersonnelCalAmountsBean = (BudgetPersonnelCalAmountsBean)vecBudgetPersonnelCalAmountsBean.get(personnelCalAmtsIndex);
                            newBudgetPersonnelCalAmountsBean.setBudgetPeriod(newBudgetPeriodBean.getBudgetPeriod());
                            newBudgetPersonnelCalAmountsBean.setAcType(TypeConstants.INSERT_RECORD);
                            queryEngine.insert(queryKey, newBudgetPersonnelCalAmountsBean);
                        }
                        
                    }
                    
                    //newBudgetPersonnelDetailsBean.setLineItemStartDate(newBudgetPeriodBean.getStartDate());
                    //newBudgetPersonnelDetailsBean.setLineItemEndDate(newBudgetPeriodBean.getEndDate());
                    
                }//End For Copy Personnel Detail Beans
                
                //Copy Budget Personned Details Cal Amts
                /*vecBudgetPersonnelCalAmountsBean = queryEngine.executeQuery(queryKey, BudgetPersonnelCalAmountsBean.class, condition);
                if(vecBudgetPersonnelCalAmountsBean == null || vecBudgetPersonnelCalAmountsBean.size() == 0) vecBudgetPersonnelCalAmountsBean = new CoeusVector();
                for(int personnelCalAmtsIndex = 0; personnelCalAmtsIndex < vecBudgetPersonnelCalAmountsBean.size(); personnelCalAmtsIndex++) {
                    newBudgetPersonnelCalAmountsBean = (BudgetPersonnelCalAmountsBean)vecBudgetPersonnelCalAmountsBean.get(personnelCalAmtsIndex);
                    newBudgetPersonnelCalAmountsBean.setBudgetPeriod(newBudgetPeriodBean.getBudgetPeriod());
                    newBudgetPersonnelCalAmountsBean.setAcType(TypeConstants.INSERT_RECORD);
                    queryEngine.insert(queryKey, newBudgetPersonnelCalAmountsBean);
                }*/
                // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
                CoeusVector cvFormualtedCost = queryEngine.executeQuery(queryKey, BudgetFormulatedCostDetailsBean.class, condition);
                if(cvFormualtedCost != null && !cvFormualtedCost.isEmpty()){
                    for(Object formualtedDetails : cvFormualtedCost){
                        BudgetFormulatedCostDetailsBean newBudgetFormulatedCostDetailsBean = (BudgetFormulatedCostDetailsBean)formualtedDetails;
                        newBudgetFormulatedCostDetailsBean.setBudgetPeriod(newBudgetPeriodBean.getBudgetPeriod());
                        newBudgetFormulatedCostDetailsBean.setAcType(TypeConstants.INSERT_RECORD);
                        queryEngine.insert(queryKey, newBudgetFormulatedCostDetailsBean);
                    }
                }
                // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
                
                
                //Add New Budget Periods to Tab Pane
                BudgetPeriodController newBudgetPeriodController = new BudgetPeriodController(budgetInfoBean);
                if(isParentProposal()){
                    newBudgetPeriodController.setParentProposal(isParentProposal());
                    newBudgetPeriodController.setProposalHierarchyBean(getProposalHierarchyBean());
                }
                newBudgetPeriodController.setFunctionType(getFunctionType());
                newBudgetPeriodController.setFormData(newBudgetPeriodBean);
                newBudgetPeriodController.calculate();
                vecBudgetPeriodController.add(newBudgetPeriodController);
                tbdPnBudgetTabbedPane.insertTab(BUDGET_PERIOD + newBudgetPeriodBean.getBudgetPeriod(), null, newBudgetPeriodController.getControlledUI(), null, newBudgetPeriodBean.getBudgetPeriod());
                //addTab(BUDGET_PERIOD + newBudgetPeriodBean.getBudgetPeriod(), newBudgetPeriodController.getControlledUI());
                
            }//End For New Budget Periods
            otherCalculation = true;
            // Case# 3760:Error Message when Total Cost limit is exceeded -Start
//            calculateBudget();
            calculateBudget(false);
            // Case# 3760:Error Message when Total Cost limit is exceeded - End
            
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }catch (CoeusUIException coeusUIException) {
            CoeusOptionPane.showDialog(coeusUIException);
        }catch (CoeusClientException coeusClientException){
            CoeusOptionPane.showDialog(coeusClientException);
        }
        
    }
    
    /** Copies Budget Information.
     * @param type valid types are
     * COPY_ONE_PERIOD_ONLY
     * COPY_ALL_PERIODS
     * @param newVersionNumber new version of budget.
     */
    public void copyBudget(int type, int newVersionNumber)  {
        
        fetchDataFromServer();
        
        copyMode = true;
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
         boolean allow_copy = false;
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
        
        GreaterThan gt1 = new GreaterThan("budgetPeriod", new Integer(1));
        Equals eqVersion = new Equals("versionNumber", new Integer(budgetInfoBean.getVersionNumber()));
        
        Class copyClass[] = {
            BudgetInfoBean.class,
            BudgetDetailBean.class,
            BudgetDetailCalAmountsBean.class,
            BudgetPersonnelDetailsBean.class,
            ProposalRatesBean.class,
            BudgetJustificationBean.class,
            BudgetPersonsBean.class,
            ProposalLARatesBean.class,
            BudgetPeriodBean.class,
            BudgetPersonnelCalAmountsBean.class,
            BudgetFormulatedCostDetailsBean.class,
            //Case 1625 Start 4
            ProjectIncomeBean.class,
            //Case 1625 End 4
            //Case #1626 Start 4
            BudgetModularBean.class,
            //for moudlar enhancement by tarique
            BudgetModularIDCBean.class
            //Case #1626 End 5
        };
        
        Class removeClass[] = {
            BudgetDetailBean.class,
            BudgetDetailCalAmountsBean.class,
            BudgetPersonnelDetailsBean.class,
            BudgetFormulatedCostDetailsBean.class,
            //BudgetPeriodBean.class,
            BudgetPersonnelCalAmountsBean.class
            
            //Case 1625 Start 5
            ,ProjectIncomeBean.class
            //Case 1625 End 5
            //Commented with COEUSDEV-298:Do not copy modular flag and modular budget while only copying period 1
            //Case #1626 Start 5
//            ,BudgetModularBean.class
            //for moudlar enhancement by tarique
//            ,BudgetModularIDCBean.class
            //Case #1626 End 5
            //COEUSDEV-298:End
        };
        
        if(type == COPY_ONE_PERIOD_ONLY) {
            //Delete beans having period > 1
            for(int index = 0; index < removeClass.length; index++) {
                queryEngine.removeData(queryKey, removeClass[index], gt1);
            }
            try{
                queryEngine.setUpdate(queryKey, BudgetPeriodBean.class, "totalCost", DataType.getClass(DataType.DOUBLE), new Double(0), gt1);
                queryEngine.setUpdate(queryKey, BudgetPeriodBean.class, "totalDirectCost", DataType.getClass(DataType.DOUBLE), new Double(0), gt1);
                queryEngine.setUpdate(queryKey, BudgetPeriodBean.class, "totalIndirectCost", DataType.getClass(DataType.DOUBLE), new Double(0), gt1);
                queryEngine.setUpdate(queryKey, BudgetPeriodBean.class, "costSharingAmount", DataType.getClass(DataType.DOUBLE), new Double(0), gt1);
                queryEngine.setUpdate(queryKey, BudgetPeriodBean.class, "underRecoveryAmount", DataType.getClass(DataType.DOUBLE), new Double(0), gt1);
                queryEngine.setUpdate(queryKey, BudgetPeriodBean.class, "totalCostLimit", DataType.getClass(DataType.DOUBLE), new Double(0), gt1);
                
                double totalCost, totalDirectCost, totalIndirectCost;
                totalCost = queryEngine.getDetails(queryKey, BudgetPeriodBean.class).sum("totalCost");
                totalDirectCost = queryEngine.getDetails(queryKey, BudgetPeriodBean.class).sum("totalDirectCost");
                totalIndirectCost = queryEngine.getDetails(queryKey, BudgetPeriodBean.class).sum("totalIndirectCost");
                
                Equals eqPrpNo = new Equals("proposalNumber", budgetInfoBean.getProposalNumber());
                Equals eqVerNo = new Equals("versionNumber", new Integer(budgetInfoBean.getVersionNumber()));
                And eqPrpNoAndEqVerNo = new And(eqPrpNo, eqVerNo);
                
                queryEngine.setUpdate(queryKey, BudgetInfoBean.class, "totalCost", DataType.getClass(DataType.DOUBLE), new Double(totalCost), eqPrpNoAndEqVerNo);
                queryEngine.setUpdate(queryKey, BudgetInfoBean.class, "totalDirectCost", DataType.getClass(DataType.DOUBLE), new Double(totalDirectCost), eqPrpNoAndEqVerNo);
                queryEngine.setUpdate(queryKey, BudgetInfoBean.class, "totalIndirectCost", DataType.getClass(DataType.DOUBLE), new Double(totalIndirectCost), eqPrpNoAndEqVerNo);
                //COEUSDEV-298:Do not copy modular flag and modular budget while only copying period 1
                budgetInfoBean.setBudgetModularFlag(false);
                queryEngine.removeData(queryKey, BudgetModularBean.class,eqPrpNo);
                queryEngine.removeData(queryKey, BudgetModularIDCBean.class,eqPrpNo);
                //COEUSDEV-298:End
                
            }catch (CoeusException coeusException) {
                coeusException.printStackTrace();
            }
        }
        
        Object key;
        
        for(int index = 0; index < copyClass.length; index++) {
            key = copyClass[index];
            try{
                queryEngine.setUpdate(queryKey, (Class)key, "acType", String.class, TypeConstants.INSERT_RECORD, eqVersion);
                queryEngine.setUpdate(queryKey, (Class)key, "versionNumber", DataType.getClass(DataType.INT), new Integer(newVersionNumber) , eqVersion);
            }catch (CoeusException coeusException) {
                System.out.println(coeusException.getMessage());
            }
        }
       
        // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
        // Sub award line items are removed from the queryEngine
        // reorder the lineitem sequence for budget details in all the periods
        try{
            GreaterThan subAwardGreaterThanOne = new GreaterThan("subAwardNumber",new Integer(0));
            CoeusVector cvSubAwardLineItems = queryEngine.executeQuery(queryKey, BudgetDetailBean.class,subAwardGreaterThanOne);
            if(cvSubAwardLineItems != null && !cvSubAwardLineItems.isEmpty()){
                Class lineItemBeans[] = {
                    BudgetDetailBean.class,
                    BudgetDetailCalAmountsBean.class,
                    BudgetRateBaseBean.class,
                    BudgetPersonnelDetailsRateBaseBean.class,
                };
                Vector vecSubAwardLinItems = new Vector();
                for(Object budgetDetails : cvSubAwardLineItems){
                    BudgetDetailBean budgetDetailBean = (BudgetDetailBean)budgetDetails;
                    Equals eqLineItemNum = new Equals("lineItemNumber", budgetDetailBean.getLineItemNumber());
                    Equals eqBudgetPeriod = new Equals("budgetPeriod", budgetDetailBean.getBudgetPeriod());
                    And periodAndLineItem = new And(eqBudgetPeriod,eqLineItemNum);
                    for(int index = 0; index < lineItemBeans.length; index++) {
                        key = lineItemBeans[index];
                        queryEngine.removeData(queryKey, (Class)key, periodAndLineItem);
                    }
                }

            }
            if(type == COPY_ONE_PERIOD_ONLY) {
                Equals eqBudgetPeriod = new Equals("budgetPeriod", new Integer(1));
                CoeusVector cvBudgetDetails = queryEngine.executeQuery(queryKey, BudgetDetailBean.class,eqBudgetPeriod);
                cvBudgetDetails = resetOrderOfLineItemSequence(cvBudgetDetails);
                //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
                //Add all the cost elements to vector vecCostElements from the budget details
                //Commented and Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
                //Vector vecCostElements = getCostElementsInUI(cvBudgetDetails);
                String proposalNumber =  budgetInfoBean.getProposalNumber();
                int versionNumber = budgetInfoBean.getVersionNumber();
                //type of copy budget is COPY_ONE_PERIOD_ONLY so set budgetperiodNumber as 1
                int budgetperiodNumber  =1; 
                //it returns the value if budget holda any inactive types
                int inactive_Type  = getInactiveAppPerTypForPeriod(proposalNumber,versionNumber,budgetperiodNumber);
                allow_copy = getWarningMsg(inactive_Type);
                //if it returns true then it allows to create new version of budget
                /*allow_copy = getCostElementDetails(vecCostElements);
                int selection;
                if(allow_copy){
                    selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(BUDGET_HAVE_INACTIVE_COST_ELEMENTS), CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                    if(selection ==CoeusOptionPane.SELECTION_YES){
                        allow_copy = true;
                    } else if(selection ==CoeusOptionPane.SELECTION_NO){
                        allow_copy = false;
                    }
                }else{
                    allow_copy = true;
                }*/
                //Commented and Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end
                //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
                queryEngine.removeData(queryKey, BudgetDetailBean.class,eqBudgetPeriod);
                queryEngine.addCollection(queryKey, BudgetDetailBean.class,cvBudgetDetails);
            }else{
                GreaterThan gtBudgetPeriod = new GreaterThan("budgetPeriod", new Integer(0));
                CoeusVector cvBudgetPeriods = queryEngine.executeQuery(queryKey,BudgetPeriodBean.class,gtBudgetPeriod) ;
                CoeusVector cvUpdatedBudgetDetails = new CoeusVector();
                if(cvBudgetPeriods != null && !cvBudgetPeriods.isEmpty()){
                    for(Object budgetPeriod : cvBudgetPeriods){
                        BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)budgetPeriod;
                        Equals eqBudgetPeriod = new Equals("budgetPeriod", new Integer(budgetPeriodBean.getBudgetPeriod()));
                        CoeusVector cvBudgetDetails = queryEngine.executeQuery(queryKey, BudgetDetailBean.class,eqBudgetPeriod);
                        cvBudgetDetails = resetOrderOfLineItemSequence(cvBudgetDetails);
                        cvUpdatedBudgetDetails.addAll(cvBudgetDetails); 
                    }
                    //Commented and Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
                    String proposalNumber =  budgetInfoBean.getProposalNumber();
                    int versionNumber = budgetInfoBean.getVersionNumber();
                    //type of copy budget is COPY_ALL_PERIOD_ONLY so set budgetperiodNumber as 2
                    int budgetperiodNumber  = 2;
                    //it returns the value if budget holda any inactive types
                    int inactive_Type  = getInactiveAppPerTypForPeriod(proposalNumber,versionNumber,budgetperiodNumber);
                    allow_copy = getWarningMsg(inactive_Type);                
                    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
                    //Add all the cost elements to vector vecCostElements from the budget details
                    //Vector vecCostElements = getCostElementsInUI(cvUpdatedBudgetDetails);
                    //if it returns true then it allows to create new version of budget
                    /*allow_copy = getCostElementDetails(vecCostElements);
                    int selection;
                    if(allow_copy){
                        selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(BUDGET_HAVE_INACTIVE_COST_ELEMENTS), CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                        if(selection ==CoeusOptionPane.SELECTION_YES){
                            allow_copy = true;
                        } else if(selection ==CoeusOptionPane.SELECTION_NO){
                            allow_copy = false;
                        }
                    }else{
                        allow_copy = true;
                    }*/
                    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
                    //Commented and Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end
                    queryEngine.removeData(queryKey, BudgetDetailBean.class,gtBudgetPeriod);
                    queryEngine.addCollection(queryKey, BudgetDetailBean.class,cvUpdatedBudgetDetails);
                }

                
            }
        }catch (CoeusException coeusException) {
            System.out.println(coeusException.getMessage());
        }
        
        // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
        //if it returns true then it allows to create new version of budget
        if(allow_copy){
            budgetInfoBean.setVersionNumber(newVersionNumber);
            budgetInfoBean.setAcType(TypeConstants.INSERT_RECORD);
            //Added for Case - 3714 : Copying Budget marked final - Set always false to final version flag. - Start
            budgetInfoBean.setFinalVersion(false);
            //Added for Case - 3714 : Copying Budget marked final - Set always false to final version flag. - End
            String newQueryKey = budgetInfoBean.getProposalNumber() + budgetInfoBean.getVersionNumber();
            queryEngine.addDataCollection(newQueryKey, queryEngine.getDataCollection(queryKey));
            queryEngine.removeDataCollection(queryKey);
            
            queryKey = newQueryKey;
            
            queryEngine.insert(queryKey, budgetInfoBean);
            
            budgetBaseWindow.setTitle("Modify Budget for Proposal "+budgetInfoBean.getProposalNumber() + ", Version " + budgetInfoBean.getVersionNumber());
            calculate(queryKey,-1);
        }
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
    }
    
    /** displays cost element look up dialog. */
    private void selectCostElement() throws CoeusClientException {
        int index = tbdPnBudgetTabbedPane.getSelectedIndex();
        if(index == 0 || index > vecBudgetPeriodController.size()) return;
        
        BudgetPeriodController budgetPeriodController = (BudgetPeriodController)vecBudgetPeriodController.get(index - 1);
        budgetPeriodController.costElementLookup();
    }
    
    /** Setter for property proposalDevelopmentFormBean.
     * @param proposalDevelopmentFormBean New value of property proposalDevelopmentFormBean.
     */
    public void setProposalDevelopmentFormBean(ProposalDevelopmentFormBean proposalDevelopmentFormBean) {
        this.proposalDevelopmentFormBean = proposalDevelopmentFormBean;
        //budgetSummaryController.proposalDates(proposalDevelopmentFormBean);
    }
    
    public void stateChanged(ChangeEvent changeEvent) {
        try{
            //save FormData before Calling Refresh.
            if(selectedTabIndex == 0) {
                if(isParentProposal()){
                    budgetBaseWindow.ratesMenuItem.setEnabled(false);
                    budgetBaseWindow.btnMaintainRatesForProposal.setEnabled(false);
                    //Code commented for case#2938 - proposal hierarchy
                    //For Parent proposal the following fields are enabled.
//                    budgetBaseWindow.costSharingDistributionMenuItem.setEnabled(false);
//                    budgetBaseWindow.underRecoveryDistibutionMenuItem.setEnabled(false);
                }
                budgetSummaryController.saveFormData();
            } else if(selectedTabIndex <= vecBudgetPeriodController.size()) {
                ((Controller)vecBudgetPeriodController.get(selectedTabIndex - 1)).saveFormData();
                if(isParentProposal()){
                    budgetBaseWindow.ratesMenuItem.setEnabled(false);
                    budgetBaseWindow.btnMaintainRatesForProposal.setEnabled(false);
                    //Code commented for case#2938 - proposal hierarchy
                    //For Parent proposal the following fields are enabled.
//                    budgetBaseWindow.costSharingDistributionMenuItem.setEnabled(false);
//                    budgetBaseWindow.underRecoveryDistibutionMenuItem.setEnabled(false);
                }
            }
            
            //Refresh Data
            selectedTabIndex = tbdPnBudgetTabbedPane.getSelectedIndex();
            if(selectedTabIndex == 0) {
                budgetSummaryController.refresh();
                if(isParentProposal()){
                    budgetBaseWindow.ratesMenuItem.setEnabled(false);
                    budgetBaseWindow.btnMaintainRatesForProposal.setEnabled(false);
                    //Code commented for case#2938 - proposal hierarchy
//                    budgetBaseWindow.costSharingDistributionMenuItem.setEnabled(false);
//                    budgetBaseWindow.underRecoveryDistibutionMenuItem.setEnabled(false);
                }
            } else if(selectedTabIndex <= vecBudgetPeriodController.size()) {
                ((Controller)vecBudgetPeriodController.get(selectedTabIndex - 1)).refresh();
                if(isParentProposal()){
                    budgetBaseWindow.ratesMenuItem.setEnabled(false);
                    budgetBaseWindow.btnMaintainRatesForProposal.setEnabled(false);
                    //Code commented for case#2938 - proposal hierarchy
//                    budgetBaseWindow.costSharingDistributionMenuItem.setEnabled(false);
//                    budgetBaseWindow.underRecoveryDistibutionMenuItem.setEnabled(false);
                }
            }else if(tbdPnBudgetTabbedPane.getSelectedComponent() instanceof edu.mit.coeus.propdev.gui.ProposalHierarchyForm){
                if(isParentProposal()){
                    budgetBaseWindow.ratesMenuItem.setEnabled(true);
                    budgetBaseWindow.btnMaintainRatesForProposal.setEnabled(true);
                    budgetBaseWindow.costSharingDistributionMenuItem.setEnabled(true);
                    budgetBaseWindow.underRecoveryDistibutionMenuItem.setEnabled(true);
                }
            }else{
                if(isParentProposal()){
                    budgetBaseWindow.ratesMenuItem.setEnabled(false);
                    budgetBaseWindow.btnMaintainRatesForProposal.setEnabled(false);
                    //Code commented for case#2938 - proposal hierarchy
//                    budgetBaseWindow.costSharingDistributionMenuItem.setEnabled(false);
//                    budgetBaseWindow.underRecoveryDistibutionMenuItem.setEnabled(false);
                }
                budgetTotalController.refresh();
            }
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }
    }
    
    /** Calculates Budget. */
    // Made the method private for Case# 3760:Error Message when Total Cost limit is exceeded
//    public void calculateBudget() {
      private void calculateBudget(boolean checkTotalCostExceeded) {
        blockEvents(true);
        
        try{
            // Case# 3760:Error Message when Total Cost limit is exceeded -Start
//            if(! validate()){
            if(! validate(checkTotalCostExceeded)){
                // Case# 3760:Error Message when Total Cost limit is exceeded -End
                blockEvents(false);
                return ;
            }
        }catch (CoeusUIException coeusUIException) {
            coeusUIException.printStackTrace();
            blockEvents(false);
            return ;
        }
        saveFormData();
        statusWindow.setHeader("Calculating Budget");
        statusWindow.setFooter("Please Wait...");
        statusWindow.display();
        
        //Dispath calculate in a separate Thread.
        
        Thread thread = new Thread(new Runnable() {
            public void run() {
                // the processing done here.
                if(! calculate(queryKey, CALCULATE_ALL_PERIODS)) {
                    CoeusOptionPane.showErrorDialog("Server Error : Calculation failed");
                    statusWindow.setVisible(false);
                    blockEvents(false);
                    return ;
                }
               /*gnprh - commented for proposal hierarchy
                try{
                    if(!otherCalculation){
                        updateBudgetRateAndBase();
                    }
                    otherCalculation = false;
                }catch (CoeusException exception){
                    exception.printStackTrace();
                    CoeusOptionPane.showErrorDialog(exception.getMessage());
                }*/
                statusWindow.setHeader("Refreshing Budget Information");
                refresh();
                statusWindow.setVisible(false);
                blockEvents(false);
            }
        });
        thread.start();
        
    }
    
    /** Contact server to update budget rate and base values in OSP$BUDGET_RATE_AND_BASE
     *First clean the table and then update the values for the corresponding
     *proposal number and version number
     *Case Id 1811
     */
    private void updateBudgetRateAndBase() throws CoeusException{
        String connectTo = CoeusGuiConstants.CONNECTION_URL + SERVLET;
//        CoeusVector cvData  = queryEngine.getDetails(queryKey,BudgetRateBaseBean.class);
        RequesterBean requesterBean = new RequesterBean();
        CoeusVector clinetData = new CoeusVector();
        clinetData.add(0,queryEngine.getDetails(queryKey,BudgetRateBaseBean.class));
        clinetData.add(1,budgetInfoBean.getProposalNumber());
        clinetData.add(2,new Integer(budgetInfoBean.getVersionNumber()));
        requesterBean.setDataObjects(clinetData);
        requesterBean.setFunctionType(UPDATE_BUDGET_RATE_AND_BASE);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean);
        comm.send();
        ResponderBean responderBean = comm.getResponse();
        if(responderBean!= null){
            if(responderBean.isSuccessfulResponse()) {
            }else{
                throw new CoeusException(responderBean.getMessage());
            }
        }
    }
    
    /** called when budget Base Window is Closed.
     * @throws PropertyVetoException PropertyVetoException
     */
    public void close() throws PropertyVetoException{
        //System.out.println("Closing");
        //Code commented and added for bug fix case#3183 - starts
//        if(getFunctionType() == TypeConstants.DISPLAY_MODE) {
        if(getFunctionType() == TypeConstants.DISPLAY_MODE
            && !isParentProposal()) {
        //Code commented and added for bug fix case#3183 - ends
            //mdiForm.removeFrame(title, title );
            mdiForm.removeFrame(CoeusGuiConstants.BUDGET_FRAME_TITLE, budgetInfoBean.getProposalNumber());
            //budgetBaseWindow.dispose();
            
            /*CoeusInternalFrame frame = mdiForm.getFrame(
            CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE, budgetInfoBean.getProposalNumber());
            if(frame != null){
                frame.setSelected(true);
                frame.setVisible(true);
            }*/
            
            closed = true;
            budgetBaseWindow.doDefaultCloseAction();
            
            queryEngine.removeDataCollection(queryKey);
            cleanup();
            
            return ;
        }
        
        try{
            
            /*if(! validate()) {
                throw new PropertyVetoException("Cancel",null);
            }*/
            
            //Save and check for modification
            saveFormData();
            
            Enumeration enumeration =  queryEngine.getKeyEnumeration(queryKey);
            
            Equals eqInsert = new Equals("acType", TypeConstants.INSERT_RECORD);
            Equals eqUpdate = new Equals("acType", TypeConstants.UPDATE_RECORD);
            Equals eqDelete = new Equals("acType", TypeConstants.DELETE_RECORD);
            
            Or insertOrUpdate = new Or(eqInsert, eqUpdate);
            Or insertOrUpdateOrDelete = new Or(insertOrUpdate, eqDelete);
            
            Object key;
            CoeusVector data;
            boolean anythingToSave = false;
            
            if(budgetInfoBeanModified) {
                anythingToSave = true;
            }
            else{
                while(enumeration.hasMoreElements()) {
                    key = enumeration.nextElement();
                    
                    //Commented since BudgetInfoBean data can also be changed and needs to be saved
                    //Modified for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - starts
                    if(key.equals(BudgetInfoBean.class) || key.equals(BUDGET_SUMMARY_DISPLAY_OPTION)
                    || key.equals(ENABLE_SALARY_INFLATION_ANNIV_DATE)) continue;
                    //Modified for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - ends
                    // 4493: While adding a TBA appointment type should be defaulted to 12 Months - Start
                    if(key.equals(CoeusConstants.DEFAULT_TBA_APPOINTMENT_TYPE_CODE) || 
                            key.equals(CoeusConstants.ENABLE_FORMULATED_COST_CALC) || 
                            key.equals(CoeusConstants.FORMULATED_COST_ELEMENTS) || key.equals(CoeusConstants.FORMULATED_TYPES)) {
                        continue;
                    }
                    // 4493: While adding a TBA appointment type should be defaulted to 12 Months - End
                    data = queryEngine.executeQuery(queryKey, (Class)key, insertOrUpdateOrDelete);
                    if(! anythingToSave) {
                        if(data != null && data.size() > 0) {
                            anythingToSave = true;
                            break;
                        }
                    }
                }
            }
            
            
            if(anythingToSave) {
                int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(SAVE_CHANGES), CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
                if(selection == CoeusOptionPane.SELECTION_YES) {
                    
                    try{
                        if(! validate()) {
                            statusWindow.setVisible(false);
                            throw new PropertyVetoException("Cancel",null);
                        }
                    }catch (CoeusUIException coeusUIException) {
                        statusWindow.setVisible(false);
                        coeusUIException.printStackTrace();
                    }
                    
                    //Code commented and added for bug fix case#3183 - starts
                    //if it is parent proposal, then calculation to be skipped
//                    statusWindow.setHeader("Calculating Budget");
                    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
                    //if allow_save returns true then allow save the budget
                    if(allow_save){                      
                        if(!isParentProposal()){
                            statusWindow.setHeader("Calculating Budget");
                        }
                        //Code commented and added for bug fix case#3183 - ends
                        statusWindow.setFooter("Please Wait...");
                        statusWindow.display();
                        
                        Thread thread = new Thread(new Runnable() {
                            public void run() {
                                try{
                                    budgetSummaryController.refresh();
                                    saveFormData();
                                    //Code commented and added for bug fix case#3183 - starts
                                    //if it is parent proposal, then calculation to be skipped
//                                if(!calculate(queryKey, CALCULATE_ALL_PERIODS)) {
                                    if(!isParentProposal() && !calculate(queryKey, CALCULATE_ALL_PERIODS)) {
                                        //Code commented and added for bug fix case#3183 - ends
                                        CoeusOptionPane.showErrorDialog("Server Error : Calculation failed");
                                        statusWindow.setVisible(false);
                                        return ;
                                    }
                                    statusWindow.setHeader("Saving Budget");
                                    closeAfterSave = true;
                                    //Update the Rate and Base calculation
                              /*gnprh - commented for Proposal Hierarchy
                               if(!otherCalculation){
                                    updateBudgetRateAndBase();
                                }
                                otherCalculation = false;
                               **/
                                    saveBudget();
                                    statusWindow.setVisible(false);
                                    
                                    //mdiForm.removeFrame(title, title);
                                    mdiForm.removeFrame(CoeusGuiConstants.BUDGET_FRAME_TITLE, budgetInfoBean.getProposalNumber());
                                    //budgetBaseWindow.dispose();
                                /*CoeusInternalFrame frame = mdiForm.getFrame(
                                CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE, budgetInfoBean.getProposalNumber());
                                if(frame != null){
                                    frame.setSelected(true);
                                    frame.setVisible(true);
                                }*/
                                    
                                    closed = true;
                                    budgetBaseWindow.doDefaultCloseAction();
                                    
                                    queryEngine.removeDataCollection(queryKey);
                                    cleanup();
                                }catch (Exception exception) {
                                    statusWindow.setVisible(false);
                                    exception.printStackTrace();
                                }
                            }
                            
                        });
                        thread.start();
                    }//Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
                    //to stop window from closing before saving.
                    throw new PropertyVetoException("Cancel",null);
                    
                }else if(selection == CoeusOptionPane.SELECTION_NO) {
                    //budgetBaseWindow.doDefaultCloseAction();
                    //mdiForm.removeFrame(title, title );
                    mdiForm.removeFrame(CoeusGuiConstants.BUDGET_FRAME_TITLE, budgetInfoBean.getProposalNumber());
                    
                    //Release the lock before closing budget
                    try{
                        releaseBudgetLock();
                    }catch (CoeusClientException coeusClientException){
                        coeusClientException.printStackTrace();
                    }
                    
                    //budgetBaseWindow.dispose();
                    /*
                    CoeusInternalFrame frame = mdiForm.getFrame(
                    CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE, budgetInfoBean.getProposalNumber());
                    if(frame != null){
                        frame.setSelected(true);
                        frame.setVisible(true);
                    }*/
                    
                    closed = true;
                    budgetBaseWindow.doDefaultCloseAction();
                    
                    queryEngine.removeDataCollection(queryKey);
                    try{
                        finalize();
                    }catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }else if(selection == CoeusOptionPane.SELECTION_CANCEL) {
                    throw new PropertyVetoException("Cancel",null);
                }
            }else {
                //budgetBaseWindow.doDefaultCloseAction();
                //mdiForm.removeFrame(title, title );
                mdiForm.removeFrame(CoeusGuiConstants.BUDGET_FRAME_TITLE, budgetInfoBean.getProposalNumber());
                
                //Release the lock before closing budget
                try{
                    releaseBudgetLock();
                }catch (CoeusClientException coeusClientException){
                    coeusClientException.printStackTrace();
                }
                
                /*budgetBaseWindow.dispose();
                 
                CoeusInternalFrame frame = mdiForm.getFrame(
                CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE, budgetInfoBean.getProposalNumber());
                if(frame != null){
                    frame.setSelected(true);
                    frame.setVisible(true);
                }*/
                
                closed = true;
                budgetBaseWindow.doDefaultCloseAction();
                
                queryEngine.removeDataCollection(queryKey);
                try{
                    finalize();
                }catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }/*catch (CoeusUIException coeusUIException) {
            coeusUIException.printStackTrace();
            throw new PropertyVetoException("Cancel",null);
        }*/
    }
    
    private void cleanup() {
        //Bug Fix - Remove Listeners  - Start
        try{
            finalize();
        }catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        //Bug Fix - Remove Listeners  - End
    }
    
    public void vetoableChange(PropertyChangeEvent pce) throws PropertyVetoException {
        if(closed) return ;
        boolean changed = ((Boolean) pce.getNewValue()).booleanValue();
        if(pce.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY) && changed) {
            close();
        }
    }
    
    /** refreshes budget information. */
    public void refresh() {
        try{
            BudgetInfoBean qeBudgetInfoBean = (BudgetInfoBean)queryEngine.executeQuery(queryKey, budgetInfoBean).get(0);
            qeBudgetInfoBean.setActivityTypeCode(budgetInfoBean.getActivityTypeCode());
            qeBudgetInfoBean.setUnitNumber(budgetInfoBean.getUnitNumber());
            //Added for Case#2341 - Recalculate Budget if Project dates change - starts
            qeBudgetInfoBean.setStartDate(budgetInfoBean.getStartDate());
            qeBudgetInfoBean.setEndDate(budgetInfoBean.getEndDate());
            //Added for Case#2341 - Recalculate Budget if Project dates change - ends
            queryEngine.update(queryKey, qeBudgetInfoBean);
            budgetInfoBean = qeBudgetInfoBean;
            //Remove Tab Listener, so that when refresh removes and adds tab, tab selection changes,
            //but shouldn't set the data as updated. so add Change listener once update is done
            tbdPnBudgetTabbedPane.removeChangeListener(this);
            int selectedTabIndex = tbdPnBudgetTabbedPane.getSelectedIndex();
            //setting back the data to the Controllers. i.e refreshing Data
            //Modified for COEUSDEV-241 : Funky screens in PD Budget tab for Total screen when save is pressed multiple times - Start
//            tbdPnBudgetTabbedPane.remove(0);
//            budgetSummaryController = new BudgetSummaryController(budgetInfoBean, proposalDevelopmentFormBean,this);
            budgetSummaryController.setFunctionType(getFunctionType());
//            budgetSummaryController.registerComponents();
            budgetSummaryController.refreshBudgetInfo(budgetInfoBean,proposalDevelopmentFormBean);
            budgetSummaryController.refresh();
            //COEUSDEV-241 : end
            //Code added for bug fix case#3183 - starts
            //for enableing budget modular check box for parent proposal
            if(mode != TypeConstants.DISPLAY_MODE){
                budgetSummaryController.budgetSummaryForm.chkBudgetModular.setEnabled(true);
            }
            //Code added for case#2938 - Proposal Hierarchy enhancement - starts
            budgetSummaryController.setParentProposal(isParentProposal()); 
            if(mode != TypeConstants.DISPLAY_MODE){
                budgetSummaryController.budgetSummaryForm.chkBudgetModular.setEnabled(true);
                //COEUSQA-1693 - Cost Sharing Submission - start
                budgetSummaryController.budgetSummaryForm.chkSubmitCostSharing.setEnabled(true);
                //COEUSQA-1693 - Cost Sharing Submission - end
                budgetSummaryController.budgetSummaryForm.txtResidualFunds.setEditable(true);
                budgetSummaryController.budgetSummaryForm.txtTotalCostLimit.setEditable(true);
                budgetSummaryController.budgetSummaryForm.cmbURRateType.setEnabled(true);
                //Added for Case#3404-Outstanding proposal hierarchy changes - Start
                budgetSummaryController.budgetSummaryForm.cmbOHRateType.setEnabled(true);
                budgetSummaryController.budgetSummaryForm.cmbBudgetStatus.setEnabled(true);
                if(budgetSummaryController.budgetSummaryForm.chkFinal.isSelected() 
                    && budgetSummaryController.budgetSummaryForm.cmbBudgetStatus.getSelectedItem()==VAL_COMPLETE){
                            budgetSummaryController.budgetSummaryForm.cmbBudgetStatus.setEnabled(true);
                            budgetSummaryController.budgetSummaryForm.chkFinal.setEnabled(false);
                }else if(!budgetSummaryController.budgetSummaryForm.chkFinal.isSelected()){ 
                    budgetSummaryController.budgetSummaryForm.cmbBudgetStatus.setEnabled(false);
                }
                //Added for Case#3404-Outstanding proposal hierarchy changes - End
                budgetSummaryController.budgetSummaryForm.txtArComments.setEditable(true);
                budgetSummaryController.budgetSummaryForm.txtArComments.setBackground(
                    java.awt.Color.WHITE);
            }
            //Code added for case#2938 - Proposal Hierarchy enhancement - ends
            //Code added for bug fix case#3183 - ends            
            /** Case #1801:Parameterize Under-recovery and cost-sharing distribution Start3
             */
            budgetSummaryController.setForceUnderRecovery(isForceUnderRecovery());
            budgetSummaryController.setForceCostSharing(isForceCostSharing());
            /** Case #1801:Parameterize Under-recovery and cost-sharing distribution End3
             */
            //budgetSummaryController.proposalDates(proposalDevelopmentFormBean);
            //Commented for COEUSDEV-241 : Funky screens in PD Budget tab for Total screen when save is pressed multiple times - Start
//            tbdPnBudgetTabbedPane.insertTab(BUDGET_SUMMARY,null,budgetSummaryController.getControlledUI(), null, 0);
            //COEUSDEV-241 : End
            //Added for Proposal Hierarchy Icon by Tarique start
            setPropHierarchyStatus();
            //Added for Proposal Hierarchy Icon by Tarique end
            //budgetSummaryController.setRefreshRequired(true);
            //budgetSummaryController.refresh();
            for(int index = 0; index < vecBudgetPeriodController.size(); index++) {
                ((Controller)vecBudgetPeriodController.get(index)).setRefreshRequired(true);
                ((Controller)vecBudgetPeriodController.get(index)).refresh();
            }
            budgetTotalController.setRefreshRequired(true);
            budgetTotalController.refresh();
            if(selectedTabIndex != -1) {//Something Selected
                tbdPnBudgetTabbedPane.setSelectedIndex(selectedTabIndex);
            }else {
                //Select the first tab
                tbdPnBudgetTabbedPane.setSelectedIndex(0);
            }
            tbdPnBudgetTabbedPane.addChangeListener(this);
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }
    }
    
    /** Calculates Current Period. */
    public void calculateCurrentPeriod() {
        int index = tbdPnBudgetTabbedPane.getSelectedIndex();
        if(index == 0 || index > vecBudgetPeriodController.size()) return;
        
        final BudgetPeriodController budgetPeriodController = (BudgetPeriodController)vecBudgetPeriodController.get(index - 1);
        if(! budgetPeriodController.validate()) return ;
        budgetPeriodController.saveFormData();
        final BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)budgetPeriodController.getFormData();
        
        statusWindow.setHeader("Calculating Budget Period " + budgetPeriodBean.getBudgetPeriod());
        statusWindow.setFooter("Please Wait...");
        statusWindow.display();
        blockEvents(true);
        //Dispath Saving in a separate Thread.
        Thread thread = new Thread(new Runnable() {
            public void run() {
                // the processing done here.
                if(! calculate(queryKey, budgetPeriodBean.getBudgetPeriod())) {
                    CoeusOptionPane.showErrorDialog("Server Error : Calculation failed");
                    statusWindow.setVisible(false);
                    blockEvents(false);
                    return ;
                }
                statusWindow.setHeader("Refreshing Budget Information");
                budgetPeriodController.setRefreshRequired(true);
                budgetPeriodController.refresh();
                statusWindow.setVisible(false);
                blockEvents(false);
            }
        });
        thread.start();
    }
    
     /*
      *   Method to filter the Vector for each Rates and finding all applicable
      *  for each Rates during the Proposal Period
      */
    public CoeusVector getFilteredVector(CoeusVector vecRateBean) {
        Hashtable rateCode = new Hashtable();
        CoeusVector completeVector = new CoeusVector();
        CoeusVector vecEachRateBean;
        
        for(int filterIndex=0; filterIndex< vecRateBean.size();filterIndex ++) {
            
            int rateClassCode = ((RatesBean ) vecRateBean.get(filterIndex)).getRateClassCode();
            int rateTypeCode = ((RatesBean ) vecRateBean.get(filterIndex)).getRateTypeCode();
            
            if(!rateCode.contains((Object) new String(rateClassCode+"-"+rateTypeCode))) {
                
                Equals equalRateClassCodeObj  =  new Equals("rateClassCode",new Integer(rateClassCode));
                Equals equalRateTypeCodeObj  =  new Equals("rateTypeCode",new Integer(rateTypeCode));
                And equalRateClassAndType = new And(equalRateClassCodeObj,equalRateTypeCodeObj);
                
                if(vecRateBean!= null && vecRateBean.size()> 0){
                    vecEachRateBean = vecRateBean.filter(equalRateClassAndType);
                    //Case 3447 - START
                    boolean inflation = false;
                    if(rateClassCode == 7) inflation = true; 
                    CoeusVector vecRateBeanOnThePeriods = getRatesOnThePeriod(vecEachRateBean,budgetInfoBean, inflation);
                    completeVector.addAll(vecRateBeanOnThePeriods);
                    
                    CoeusVector vecRateBeanWithInPeriod = getRatesWithInPeriod(vecEachRateBean,budgetInfoBean, inflation);
                    //Case 3447 - END
                    completeVector.addAll(vecRateBeanWithInPeriod);
                    
                    rateCode.put((Object) new String(rateClassCode+"-"+rateTypeCode),(Object) new String(rateClassCode+"-"+rateTypeCode));
                }
            }
            
        }
        return completeVector;
    }
    
    
    
    private void setProposalRatesBean() throws CoeusException{
        //Set Proposal Rates from Institute Rates.
        CoeusVector vecFilter = getFilteredVector(queryEngine.getDetails(queryKey, InstituteRatesBean.class));
        InstituteRatesBean instituteRatesBean;
        ProposalRatesBean proposalRatesBean;
        for(int index = 0; index < vecFilter.size(); index++) {
            instituteRatesBean = (InstituteRatesBean)vecFilter.get(index);
            proposalRatesBean = new ProposalRatesBean();
            
            proposalRatesBean.setAcType(TypeConstants.INSERT_RECORD);
            proposalRatesBean.setProposalNumber(budgetInfoBean.getProposalNumber());
            proposalRatesBean.setVersionNumber(budgetInfoBean.getVersionNumber());
            
            proposalRatesBean.setActivityCode(activityTypeCode);
            
            proposalRatesBean.setOnOffCampusFlag(instituteRatesBean.isOnOffCampusFlag());
            proposalRatesBean.setActivityTypeDescription(instituteRatesBean.getActivityTypeDescription());
            proposalRatesBean.setFiscalYear(instituteRatesBean.getFiscalYear());
            proposalRatesBean.setInstituteRate(instituteRatesBean.getInstituteRate());
            proposalRatesBean.setRateClassCode(instituteRatesBean.getRateClassCode());
            proposalRatesBean.setRateClassDescription(instituteRatesBean.getRateClassDescription());
            proposalRatesBean.setRateTypeCode(instituteRatesBean.getRateTypeCode());
            proposalRatesBean.setRateTypeDescription(instituteRatesBean.getRateTypeDescription());
            proposalRatesBean.setStartDate(instituteRatesBean.getStartDate());
            proposalRatesBean.setApplicableRate(instituteRatesBean.getInstituteRate());
            
            proposalRatesBean.setAw_ActivityTypeCode(oldActivityTypeCodeBudgetInfoBean.getActivityTypeCode());
            
            queryEngine.insert(queryKey, proposalRatesBean);
            
        }
    }
    
    private void setProposalLARatesBean() throws CoeusException{
        //Set Proposal Rates from Institute Rates.
        CoeusVector vecFilter = getFilteredVector(queryEngine.getDetails(queryKey, InstituteLARatesBean.class));
        InstituteLARatesBean instituteLARatesBean;
        ProposalLARatesBean proposalLARatesBean;
        for(int index = 0; index < vecFilter.size(); index++) {
            instituteLARatesBean = (InstituteLARatesBean)vecFilter.get(index);
            proposalLARatesBean = new ProposalLARatesBean();
            
            proposalLARatesBean.setAcType(TypeConstants.INSERT_RECORD);
            proposalLARatesBean.setProposalNumber(budgetInfoBean.getProposalNumber());
            proposalLARatesBean.setVersionNumber(budgetInfoBean.getVersionNumber());
            
            proposalLARatesBean.setOnOffCampusFlag(instituteLARatesBean.isOnOffCampusFlag());
            proposalLARatesBean.setFiscalYear(instituteLARatesBean.getFiscalYear());
            proposalLARatesBean.setInstituteRate(instituteLARatesBean.getInstituteRate());
            proposalLARatesBean.setRateClassCode(instituteLARatesBean.getRateClassCode());
            proposalLARatesBean.setRateClassDescription(instituteLARatesBean.getRateClassDescription());
            proposalLARatesBean.setRateTypeCode(instituteLARatesBean.getRateTypeCode());
            proposalLARatesBean.setRateTypeDescription(instituteLARatesBean.getRateTypeDescription());
            proposalLARatesBean.setStartDate(instituteLARatesBean.getStartDate());
            proposalLARatesBean.setApplicableRate(instituteLARatesBean.getInstituteRate());
            
            queryEngine.insert(queryKey, proposalLARatesBean);
            
        }
    }
    
    /*
    public void setProposalDetailAdminForm( ProposalDetailAdminForm proposalDetailAdminForm){
        this.proposalDetailAdminForm = proposalDetailAdminForm;
     
    }
     */
    
    private void customizeView() {
        int index = tbdPnBudgetTabbedPane.getSelectedIndex();
        if(index == 0 || index > vecBudgetPeriodController.size()) return;
        
        BudgetPeriodController budgetPeriodController = (BudgetPeriodController)vecBudgetPeriodController.get(index - 1);
        budgetPeriodController.customizeView();
    }
    
    protected void finalize() throws Throwable {
        //budgetSummaryController.finalize();
        if(statusWindow != null) {
            statusWindow.setVisible(false);
        }
        
        for(int index = 0; index < vecBudgetPeriodController.size(); index++) {
            ((BudgetPeriodController)vecBudgetPeriodController.get(index)).finalize();
        }
        //budgetTotalController.finalize();
        vecBudgetPeriodController.removeAllElements();
        //budgetBaseWindow = null;
        
        removeBeanUpdatedListener(this, BudgetInfoBean.class);
        removeBeanDeletedListener(this, BudgetPeriodBean.class);
        
        budgetSummaryController = null;
        budgetTotalController.finalize();
        budgetTotalController = null;
        //System.gc();
        
        super.finalize();
    }
    
    /** This method will delete Budget Periods with no Line Items - Start from Last Period
     * Added by Chandra
     */
    public void deleteTabPages(){
        //remove Budget Periods with no line items - start from Last Period
//        int size = vecBudgetPeriodController.size();
        BudgetPeriodBean budgetPeriodBean;
        CoeusVector vecPeriodDetails;
        try{
            for(int index = vecBudgetPeriodController.size() - 1; index > 0; index--) {
                BudgetPeriodController budgetPeriodController =
                (BudgetPeriodController)vecBudgetPeriodController.get(index);
                budgetPeriodBean = (BudgetPeriodBean)budgetPeriodController.getFormData();
                Equals eqPeriod = new Equals("budgetPeriod", new Integer(budgetPeriodBean.getBudgetPeriod()));
                
                vecPeriodDetails = queryEngine.getActiveData(queryKey, BudgetDetailBean.class, eqPeriod);
                if(vecPeriodDetails == null || vecPeriodDetails.size() == 0) {
                    //This Budget Period has No Line Items. Remove Tab
                    tbdPnBudgetTabbedPane.remove(index + 1);
                    vecBudgetPeriodController.remove(index);
                }//End If
                else {
                    //Don't Delete Any Further Tab Pages.
                    break;
                }
                
            }//End For
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }// End of deleteTabPages() - chandra
    
    public void adjustPeriodTabPages() {
        //remove Budget Periods with no line items - start from Last Period
        int size = vecBudgetPeriodController.size();
        BudgetPeriodBean budgetPeriodBean;
        CoeusVector vecPeriodDetails;
        try{
            for(int index = vecBudgetPeriodController.size() - 1; index > 0; index--) {
                BudgetPeriodController budgetPeriodController =
                (BudgetPeriodController)vecBudgetPeriodController.get(index);
                budgetPeriodBean = (BudgetPeriodBean)budgetPeriodController.getFormData();
                Equals eqPeriod = new Equals("budgetPeriod", new Integer(budgetPeriodBean.getBudgetPeriod()));
                
                vecPeriodDetails = queryEngine.getActiveData(queryKey, BudgetDetailBean.class, eqPeriod);
                if(vecPeriodDetails == null || vecPeriodDetails.size() == 0) {
                    //This Budget Period has no Line Items. Remove Tab
                    tbdPnBudgetTabbedPane.remove(index + 1);
                    vecBudgetPeriodController.remove(index);
                }//End If
                else {
                    //Don't Delete Any Further Tab Pages.
                    break;
                }
                
            }//End For
            //Tab Pages with no line items Removed.
            //Continue Adding Tab Pages if applicable.
            if(vecBudgetPeriodController.size() > 1) {
                CoeusVector vecPeriods = queryEngine.getDetails(queryKey, BudgetPeriodBean.class).filter(CoeusVector.FILTER_ACTIVE_BEANS);
                vecPeriods.sort("budgetPeriod", true); //Sort in Ascending Order.
                size = vecPeriods.size();
                int startSize = vecBudgetPeriodController.size();
                for(int index = startSize; index < size; index++) {
                    BudgetPeriodController budgetPeriodController = new BudgetPeriodController(budgetInfoBean);
                    if(isParentProposal()){
                        budgetPeriodController.setParentProposal(isParentProposal());
                        budgetPeriodController.setProposalHierarchyBean(getProposalHierarchyBean());
                    }
                    budgetPeriodController.setFunctionType(getFunctionType());
                    budgetPeriodBean = (BudgetPeriodBean)vecPeriods.get(index);
                    budgetPeriodController.setFormData(budgetPeriodBean);
                    vecBudgetPeriodController.add(budgetPeriodController);
                    tbdPnBudgetTabbedPane.insertTab(BUDGET_PERIOD + budgetPeriodBean.getBudgetPeriod(), null, budgetPeriodController.getControlledUI(), null, index + 1);
                }
                
            }
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }
    
    //    public void beanUpdated(BeanEvent beanEvent) {
    //    }
    
    //End adjustPeriodTabPages
    
    
    //    public void beanUpdated(BeanEvent beanEvent) {
    //        if(beanEvent.getSource().getClass().equals(BudgetSummaryController.class)) {
    //            adjustPeriodTabPages();
    //        }//End beanUpdated
    //    }
    
    private void syncToPeriodCostLimit() {
        int index = tbdPnBudgetTabbedPane.getSelectedIndex();
        if(index == 0 || index > vecBudgetPeriodController.size()) return;
        
        BudgetPeriodController budgetPeriodController = (BudgetPeriodController)vecBudgetPeriodController.get(index - 1);
        budgetPeriodController.syncToPeriodCostLimit();
    }
    
    /**
     * Code added for Case#3472 - Sync to Direct Cost Limit
     * For adding total direct cost limit
     * Sync the line item cost to make periods direct cost is approximately equal to total direct cost limit.
     */
    private void syncToDirectCostLimit() {
        int index = tbdPnBudgetTabbedPane.getSelectedIndex();
        if(index == 0 || index > vecBudgetPeriodController.size()) return;
        
        BudgetPeriodController budgetPeriodController = (BudgetPeriodController)vecBudgetPeriodController.get(index - 1);
        budgetPeriodController.syncToDirectCostLimit();
    }
    /* To listen the BudgetSummaryController. whenever there is deletion of Periods
     *in budget Summary it has to delete the line item if it doesn't contain any line
     *item details
     * Added by chandra
     */
    public void beanDeleted(BeanEvent beanEvent) {
        if(beanEvent.getSource().getClass().equals(BudgetSummaryController.class)) {
            deleteTabPages();
        }
    }
    
    private void viewSalaries() {
        budgetPersonSalariesController = new BudgetPersonSalariesController();
        budgetPersonSalariesController.setFunctionType(getFunctionType());
        budgetPersonSalariesController.setFormData(budgetInfoBean);
        budgetPersonSalariesController.display();
    }
    
    // Shows the Under Recovery details
    private void editUnderrecovery() {
        boolean fromHierarchy = false;
        Vector dataObject = new Vector();
        if(tbdPnBudgetTabbedPane.getSelectedComponent() instanceof edu.mit.coeus.propdev.gui.ProposalHierarchyForm){
            CoeusVector cvData  = hierarchyController.getDataObject();
            if(cvData!= null && cvData.size() > 0){
                int versionNum = ((Integer)cvData.elementAt(0)).intValue();
                String propNum = (String)cvData.elementAt(1);
                
                if(budgetInfoBean.getProposalNumber().equals(propNum) &&
                versionNum==budgetInfoBean.getVersionNumber()){
                    fromHierarchy = false;
                }else{
                    fromHierarchy = true;
                    dataObject.addElement(propNum);
                    dataObject.addElement(new Integer(versionNum));
                }
            }else{
                CoeusOptionPane.showInfoDialog(coeusMessageResources.
                parseMessageKey("propHierarchy_exceptionCode.1017"));
                return ;
            }
        }
        if(!fromHierarchy && getFunctionType() != TypeConstants.DISPLAY_MODE) {
            statusWindow.setHeader("Calculating Budget");
            statusWindow.setFooter("Please Wait...");
            statusWindow.display();
            
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    
                    setFromUnderRecovery(true);
                    if(!calculate(queryKey,-1)) {
                        CoeusOptionPane.showErrorDialog("Server Error : Calculation failed");
                        statusWindow.setVisible(false);
                        setFromUnderRecovery(false);
                        return ;
                    }
                    setFromUnderRecovery(false);
                    // refresh();
                    underRecoveryController = new UnderRecoveryDistributionController();
                    underRecoveryController.setFunctionType(getFunctionType());
                    underRecoveryController.setFormData(budgetInfoBean);
                    statusWindow.setVisible(false);
                    underRecoveryController.display();
                }
            });
            thread.start();
            return ;
        }
        
        underRecoveryController = new UnderRecoveryDistributionController();
        
        if(!fromHierarchy){
            //Code commented and added for case#2938 - starts
            underRecoveryController.setFunctionType(getFunctionType());
            //underRecoveryController.setFunctionType(mode);
            //Code commented and added for case#2938 - ends
            underRecoveryController.setFormData(budgetInfoBean);
        }else{
            underRecoveryController.setFunctionType('D');
            underRecoveryController.setDataObject(dataObject);
            underRecoveryController.setDataForHierarcy();
        }
        
        underRecoveryController.display();
        tbdPnBudgetTabbedPane.repaint();
    }
    
    //listen to budget info bean updated in budget summary controller.
    public void beanUpdated(BeanEvent beanEvent) {
        if(beanEvent.getBean().getClass().equals(BudgetInfoBean.class)) {
            budgetInfoBean = (BudgetInfoBean)beanEvent.getBean();
            budgetInfoBeanModified = true;
        }
    }
    
    private void exit() {
        //start of bug fix id 1651
        String message = coeusMessageResources.parseMessageKey(
        "toolBarFactory_exitConfirmCode.1149");
        int answer = CoeusOptionPane.showQuestionDialog(message,
        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
        if (answer == JOptionPane.YES_OPTION) {
            if( mdiForm.closeInternalFrames() ) {
                mdiForm.dispose();
            }
        }
        // End of Bug fix id 1651
    }
    
    private void showBudgetSummary() {
        
        //Validate if not in display mode.(i.e Add Mode / Modify Mode)
        try{
            boolean anythingToSave = false;
            if(getFunctionType() != TypeConstants.DISPLAY_MODE) {
                if(! validate()) {
                    return ;
                }
                
                //Save and check for modification
                saveFormData();
                
                //Check if anything changed.
//                Hashtable budgetDetails = new Hashtable();
                Enumeration enumeration =  queryEngine.getKeyEnumeration(queryKey);
                
                Equals eqInsert = new Equals("acType", TypeConstants.INSERT_RECORD);
                Equals eqUpdate = new Equals("acType", TypeConstants.UPDATE_RECORD);
                Equals eqDelete = new Equals("acType", TypeConstants.DELETE_RECORD);
                
                Or insertOrUpdate = new Or(eqInsert, eqUpdate);
                Or insertOrUpdateOrDelete = new Or(insertOrUpdate, eqDelete);
                
                Object key;
                CoeusVector data;
                while(enumeration.hasMoreElements()) {
                    key = enumeration.nextElement();
                    //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module- Start                    
                    if(key.equals(BudgetInfoBean.class) || key.equals(BUDGET_SUMMARY_DISPLAY_OPTION)|| key.equals(ENABLE_SALARY_INFLATION_ANNIV_DATE) ) continue;
                    //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module -End
                    // 4493: While adding a TBA appointment type should be defaulted to 12 Months - Start
                    if(key.equals(CoeusConstants.DEFAULT_TBA_APPOINTMENT_TYPE_CODE) 
                    || key.equals(CoeusConstants.FORMULATED_COST_ELEMENTS) ||
                            key.equals(CoeusConstants.FORMULATED_TYPES) ||
                            key.equals(CoeusConstants.ENABLE_FORMULATED_COST_CALC)
                            ) {
                        continue;
                    }
                    // 4493: While adding a TBA appointment type should be defaulted to 12 Months - End
                    data = queryEngine.executeQuery(queryKey, (Class)key, insertOrUpdateOrDelete);
                    if(data != null && data.size() > 0) {
                        anythingToSave = true;
                        break;
                    }
                }
                
                if(anythingToSave) {
                    int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(SAVE_PENDING_CHANGES), CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                    if(selection == CoeusOptionPane.SELECTION_NO) {
                        return ;
                    }
                }//End IF anything to save
                
            }//End IF Display Mode
            
            //Check report mode.
            CoeusVector cvDisplayOption = queryEngine.getDetails(queryKey, BUDGET_SUMMARY_DISPLAY_OPTION);
            final int displayOption = ((Integer)cvDisplayOption.get(0)).intValue();
            
            if(displayOption == BUDGET_SUMMARY_AWT) {
                //Report String for AWT.
                final String title = BUDGET_SUMMARY_REPORT + budgetInfoBean.getProposalNumber() + ", Version " + budgetInfoBean.getVersionNumber();
                
                //if in AWT check if reporyt already opened.
                CoeusInternalFrame budgetReportFrame = mdiForm.getFrame(CoeusGuiConstants.BUDGET_FRAME_TITLE, title);
                if(budgetReportFrame != null) {
                    //Frame Exists
                    try{
                        budgetReportFrame.setSelected(true);
                        budgetReportFrame.setMaximum(true);
                    }catch (PropertyVetoException propertyVetoException) {
                        propertyVetoException.printStackTrace();
                    }
                    return ;
                }
            }
            
            //new final boolean variable to be acessed within the inner class.
            final boolean saveRequired = anythingToSave;
            //BUG FIX # 1575 Typo
            //Geo on 30-Jun-2005
            statusWindow.setHeader("Processing Budget");
            //End FIX
            statusWindow.setFooter("Please Wait...");
            statusWindow.display();
/*
 *Commented by Geo to implement Generic report engine on  21-Feb-2006
 */
            //BEGIN BLOCK
            //            //Dispatch Processing in a separate Thread.
            //            Thread thread = new Thread(new Runnable() {
            //                public void run() {
            //
            //                    blockEvents(true);
            //                    mdiForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            //
            //
            //                    if(getFunctionType() != TypeConstants.DISPLAY_MODE && saveRequired) {
            //                        budgetSummaryController.refresh();
            //                        if(! calculate(queryKey, CALCULATE_ALL_PERIODS)) {
            //                            CoeusOptionPane.showErrorDialog("Server Error : Calculation failed");
            //                            statusWindow.setVisible(false);
            //                            return ;
            //                        }
            //                        statusWindow.setHeader("Saving Budget");
            //                        //Update the Rate and Base calculation
            //                       /*gnprh - Commented for Proposal Hierarchy
            //                        try{
            //
            //                            if(!otherCalculation){
            //                                updateBudgetRateAndBase();
            //                            }
            //                        }catch (CoeusException ex){
            //                            ex.printStackTrace();
            //                            CoeusOptionPane.showErrorDialog(ex.getMessage());
            //                        }
            //                        otherCalculation = false;
            //                        **/
            //                        saveBudget();
            //                    }
            //
            //                    // the processing done here.
            //                   processSummaryReport(title);
            //                   statusWindow.setVisible(false);
            //
            //                   blockEvents(false);
            //                   mdiForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            //
            //                }
            //            });
            //            thread.start();
            
            try{
                blockEvents(true);
                mdiForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                
                
                if(getFunctionType() != TypeConstants.DISPLAY_MODE && saveRequired) {
                    budgetSummaryController.refresh();
                    if(! calculate(queryKey, CALCULATE_ALL_PERIODS)) {
                        CoeusOptionPane.showErrorDialog("Server Error : Calculation failed");
                        statusWindow.setVisible(false);
                        return ;
                    }
                    statusWindow.setHeader("Saving Budget");
                    saveBudget();
                }
                
                // the processing done here.
                //                   processSummaryReport(title);
                statusWindow.setVisible(false);
            }finally{
                blockEvents(false);
                mdiForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            
            ReportController repCntrl = new ReportController("ProposalBudget"){
                //Modified for COEUSQA-1683 Print option to display Version Comments on Budget Reports
                public Hashtable getPrintData(boolean isCommentSelected) throws CoeusException{
                    CoeusVector vecBudgetPeriods = new CoeusVector();
                    //get Only those periods which has line items
                    //start from Last Period
//                    int size = vecBudgetPeriodController.size();
                    CoeusVector vecPeriodDetails;
                    boolean periodAdded = false;
                    BudgetPeriodBean budgetPeriodBean = null;
                    for(int index = vecBudgetPeriodController.size() - 1; index >= 0; index--) {
                        BudgetPeriodController budgetPeriodController =
                        (BudgetPeriodController)vecBudgetPeriodController.get(index);
                        budgetPeriodBean = (BudgetPeriodBean)budgetPeriodController.getFormData();
                        if (periodAdded) {
                            vecBudgetPeriods.add(budgetPeriodBean);
                        } else {
                            Equals eqPeriod = new Equals("budgetPeriod", new Integer(budgetPeriodBean.getBudgetPeriod()));
                            vecPeriodDetails = queryEngine.getActiveData(queryKey, BudgetDetailBean.class, eqPeriod);
                            if(vecPeriodDetails != null && vecPeriodDetails.size() > 0) {
                                //This Budget Period has Line Items.                                
                                vecBudgetPeriods.add(budgetPeriodBean);                                
                                periodAdded = true;
                            }
                        }
                        
                    }//End For
                    //Tab Pages with no line items ignored.
                    //                    }catch (CoeusException coeusException) {
                    //                        coeusException.printStackTrace();
                    //                    }
                    
                    //requesterBean.setDataObject(budgetPeriodBean);
                    if(vecBudgetPeriods.size() == 0) {
                        //No periods have line items.
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_LINE_ITEM));
                        return null;
                    }
                    vecBudgetPeriods.sort("budgetPeriod");
                    Hashtable params = new Hashtable();
                    params.put("USER_ID", CoeusGuiConstants.getMDIForm().getUserName());
                    params.put("BUDGET_DATA", queryEngine.getDataCollection(queryKey));
                    params.put("BUDGET_PERIODS", vecBudgetPeriods);
                    // Added for COEUSQA-1683 Print option to display Version Comments on Budget Reports - Start
                    params.put("PRINT_BUDGET_COMMENT", isCommentSelected);
                    params.put("SUMMARY_COMMENTS",(budgetInfoBean.getComments() != null ? budgetInfoBean.getComments() : CoeusGuiConstants.EMPTY_STRING));
                    // Added for COEUSQA-1683 Print option to display Version Comments on Budget Reports - End
                    
                    /////////////for total page
                    String repId = getSltdReportId();
                    
                    if(repId.equalsIgnoreCase("ProposalBudget/indsrlcumbudget") ||
                           repId.equalsIgnoreCase("ProposalBudget/budgettotalbyperiod")){
                          boolean indsrlCumFlag = getSltdReportId().equalsIgnoreCase("ProposalBudget/indsrlcumbudget");
//                        budgetTotalController.refresh(repId.equalsIgnoreCase("ProposalBudget/indsrlcumbudget"));
                        
//                        if (budgetTotalController.cvTotalPageTableData != null &&
//                                budgetTotalController.cvTotalPageTableData.size() > 0 ){
                        CoeusVector data;
                        String headerTitle;
                        if(indsrlCumFlag){
                            data = budgetTotalController.getIndsrlCumulativeData();
                            headerTitle = CUM_INDSTRL_BUDGET;
                        }else{
                            budgetTotalController.refresh();
                            data = budgetTotalController.cvTotalPageTableData;
                            headerTitle = BUDGET_SUMMARY_TOTAL;
                        }
                        if (data != null &&
                                data.size() > 0 ){
                            params.put("PROPOSAL_NUM",budgetInfoBean.getProposalNumber());
                            params.put("VERSION_NUM",""+budgetInfoBean.getVersionNumber());
                            params.put("START_DATE",budgetInfoBean.getStartDate());
                            params.put("END_DATE",budgetInfoBean.getEndDate());
                            params.put("DATA",data);
//                            String headerTitle = indsrlCumFlag?CUM_INDSTRL_BUDGET:BUDGET_SUMMARY_TOTAL;
                            params.put("HEADER_TITLE",headerTitle);
                        }
                    }
                    /////////////////////
                    
                    return params;
                }
            };
            repCntrl.display();
            
            //END BLOCK
        }catch (CoeusUIException coeusUIException) {
            coeusUIException.printStackTrace();
            statusWindow.setVisible(false);
            blockEvents(false);
            mdiForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
            statusWindow.setVisible(false);
            blockEvents(false);
            mdiForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }catch (Exception ex) {
            ex.printStackTrace();
            statusWindow.setVisible(false);
            blockEvents(false);
            mdiForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        
    }//End Show Budget Summary
    
    /*
    private Vector getReportData() {
        RequesterBean requesterBean = new RequesterBean();
        BudgetPeriodBean budgetPeriodBean = new BudgetPeriodBean();
     
        CoeusVector vecBudgetPeriods = new CoeusVector();
        try{
            //get Only those periods which has line items
            //start from Last Period
            int size = vecBudgetPeriodController.size();
            CoeusVector vecPeriodDetails;
            boolean periodAdded = false;
     
            for(int index = vecBudgetPeriodController.size() - 1; index >= 0; index--) {
                BudgetPeriodController budgetPeriodController =
                (BudgetPeriodController)vecBudgetPeriodController.get(index);
                budgetPeriodBean = (BudgetPeriodBean)budgetPeriodController.getFormData();
     
                if (periodAdded) {
                    vecBudgetPeriods.add(budgetPeriodBean);
                } else {
                    Equals eqPeriod = new Equals("budgetPeriod", new Integer(budgetPeriodBean.getBudgetPeriod()));
     
                    vecPeriodDetails = queryEngine.getActiveData(queryKey, BudgetDetailBean.class, eqPeriod);
                    if(vecPeriodDetails != null && vecPeriodDetails.size() > 0) {
                        //This Budget Period has Line Items.
                        vecBudgetPeriods.add(budgetPeriodBean);
                        periodAdded = true;
                    }
                }
     
            }//End For
            //Tab Pages with no line items ignored.
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
     
        //requesterBean.setDataObject(budgetPeriodBean);
        if(vecBudgetPeriods.size() == 0) {
            //No periods have line items.
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_LINE_ITEM));
            return null;
        }
        vecBudgetPeriods.sort("budgetPeriod");
        requesterBean.setDataObject(queryEngine.getDataCollection(queryKey)); //Budget Details
        requesterBean.setDataObjects(vecBudgetPeriods); //Period Details
        requesterBean.setFunctionType('R');
     
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
     
        Vector data = new Vector();
     
        if(responderBean.isSuccessfulResponse()) {
     
            Vector vecPeriodReport;
            if(responderBean.getDataObject() instanceof Vector) {
                vecPeriodReport = (Vector)responderBean.getDataObject();
                for(int index = 0; index < vecPeriodReport.size(); index++) {
                    String report = (String)vecPeriodReport.get(index);
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(report.getBytes());
                    data.add(byteArrayInputStream);
                }
            }else if(responderBean.getDataObject() instanceof String){
                //report is generated as PDF. url is returned as string.
                data.add(responderBean.getDataObject());
            }
        }
     
        Vector vecMessages = responderBean.getDataObjects();
        //If rates not available messages are there then display the messages.
        if (vecMessages != null && vecMessages.size() > 0) {
            BudgetMessageForm budgetMessageForm = new BudgetMessageForm();
            budgetMessageForm.setFormData(vecMessages);
            budgetMessageForm.display();
        }
     
        return data;
    }
     */
    
    /*private void processSummaryReport(String reportId) {
        RequesterBean requesterBean = new RequesterBean();
        BudgetPeriodBean budgetPeriodBean = new BudgetPeriodBean();
        
        CoeusVector vecBudgetPeriods = new CoeusVector();
        try{
            //get Only those periods which has line items
            //start from Last Period
            int size = vecBudgetPeriodController.size();
            CoeusVector vecPeriodDetails;
            boolean periodAdded = false;
            
            for(int index = vecBudgetPeriodController.size() - 1; index >= 0; index--) {
                BudgetPeriodController budgetPeriodController =
                (BudgetPeriodController)vecBudgetPeriodController.get(index);
                budgetPeriodBean = (BudgetPeriodBean)budgetPeriodController.getFormData();
                
                if (periodAdded) {
                    vecBudgetPeriods.add(budgetPeriodBean);
                } else {
                    Equals eqPeriod = new Equals("budgetPeriod", new Integer(budgetPeriodBean.getBudgetPeriod()));
                    
                    vecPeriodDetails = queryEngine.getActiveData(queryKey, BudgetDetailBean.class, eqPeriod);
                    if(vecPeriodDetails != null && vecPeriodDetails.size() > 0) {
                        //This Budget Period has Line Items.
                        vecBudgetPeriods.add(budgetPeriodBean);
                        periodAdded = true;
                    }
                }
                
            }//End For
            //Tab Pages with no line items ignored.
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
        
        //requesterBean.setDataObject(budgetPeriodBean);
        if(vecBudgetPeriods.size() == 0) {
            //No periods have line items.
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_LINE_ITEM));
            return ;
        }
        vecBudgetPeriods.sort("budgetPeriod");
        requesterBean.setDataObject(queryEngine.getDataCollection(queryKey)); //Budget Details
        requesterBean.setDataObjects(vecBudgetPeriods); //Period Details
        requesterBean.setFunctionType('R');
        requesterBean.setId(reportId);
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        Vector data = new Vector();
        
        if(responderBean.isSuccessfulResponse()) {
            
            Vector vecMessages = responderBean.getDataObjects();
            //If rates not available messages are there then display the messages.
            if (vecMessages != null && vecMessages.size() > 0) {
                BudgetMessageForm budgetMessageForm = new BudgetMessageForm();
                budgetMessageForm.setFormData(vecMessages);
                budgetMessageForm.display();
            }
            
            Vector vecPeriodReport;
            if(responderBean.getDataObject() instanceof Vector) {
                vecPeriodReport = (Vector)responderBean.getDataObject();
                for(int index = 0; index < vecPeriodReport.size(); index++) {
                    String report = (String)vecPeriodReport.get(index);
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(report.getBytes());
                    data.add(byteArrayInputStream);
                }
                BudgetReportBaseWindowController budgetReportBaseWindowController = new BudgetReportBaseWindowController(reportId);
                budgetReportBaseWindowController.setFormData(data);
                budgetReportBaseWindowController.display();
                
            }else if(responderBean.getDataObject() instanceof String) {
                //Report is generated as PDF.
                //Open report in browser.
                AppletContext coeusContext = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
                String url = (String)responderBean.getDataObject();
                url = url.replace('\\', '/');
                try{
                    URL templateURL = new URL(CoeusGuiConstants.CONNECTION_URL + url);
                    if(coeusContext != null){
                        coeusContext.showDocument( templateURL, "_blank" );
                    }else{
                        javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
                        bs.showDocument(templateURL);
                    }
                }catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            
        }else {
            //Unsuccessful response.
            Exception exception = responderBean.getException();
            if(exception != null) {
                CoeusOptionPane.showErrorDialog(exception.getMessage());
            }else {
                CoeusOptionPane.showErrorDialog("No response from server");
            }
        }
        
    }//End Process Budget Summary Report
    */
    /*
    private void processSummaryReportAsPdf() {
        //PDF generation.
        Vector reportData = getReportData();
        ReportGenerator reportGenerator = new ReportGenerator();
        ByteArrayInputStream xmlStream;
        ByteArrayOutputStream byteArrayOutputStream;
        ByteArrayOutputStream reports[] = new ByteArrayOutputStream[reportData.size()];
        String bookmarks[] = new String[reportData.size()];
        InputStream xslStream;
     
        try{
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
            //Save Budget Summary
            String tmpFilePath = System.getProperty("java.io.tmpdir");
     
            File file = new File(tmpFilePath, "BudgetSummary"+budgetInfoBean.getProposalNumber()+".pdf");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(byteArrayOutputStream.toByteArray());
            fos.close();
            byteArrayOutputStream.close();
     
            //Open report in browser.
            AppletContext coeusContext = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
            String url = file.getAbsolutePath();
            url = url.replace('\\', '/');
            URL templateURL = new URL("file://"+url);
            if(coeusContext != null){
                coeusContext.showDocument( templateURL, "_blank" );
            }else{
                javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
                bs.showDocument(templateURL);
            }
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }*/
    
    /**
     * Getter for property releaseLock.
     * @return Value of property releaseLock.
     */
    public boolean isReleaseLock() {
        return releaseLock;
    }
    
    /**
     * Setter for property releaseLock.
     * @param releaseLock New value of property releaseLock.
     */
    public void setReleaseLock(boolean releaseLock) {
        this.releaseLock = releaseLock;
    }
    /** Case #1801:Parameterize Under-recovery and cost-sharing distribution Start 4
     */
    
    /**
     * Getter for property forceUnderRecovery.
     * @return Value of property forceUnderRecovery.
     */
    public boolean isForceUnderRecovery() {
        return forceUnderRecovery;
    }
    
    /**
     * Setter for property forceUnderRecovery.
     * @param forceUnderRecovery New value of property forceUnderRecovery.
     */
    public void setForceUnderRecovery(boolean forceUnderRecovery) {
        this.forceUnderRecovery = forceUnderRecovery;
    }
    
    /**
     * Getter for property forceCostSharing.
     * @return Value of property forceCostSharing.
     */
    public boolean isForceCostSharing() {
        return forceCostSharing;
    }
    
    /**
     * Setter for property forceCostSharing.
     * @param forceCostSharing New value of property forceCostSharing.
     */
    public void setForceCostSharing(boolean forceCostSharing) {
        this.forceCostSharing = forceCostSharing;
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
     * Getter for property hierarcy.
     * @return Value of property hierarcy.
     */
    public boolean isHierarcy() {
        return hierarcy;
    }
    
    /**
     * Setter for property hierarcy.
     * @param hierarcy New value of property hierarcy.
     */
    public void setHierarcy(boolean hierarcy) {
        this.hierarcy = hierarcy;
    }
    
    /**
     * Getter for property budgetPersonData.
     * @return Value of property budgetPersonData.
     */
    public edu.mit.coeus.utils.CoeusVector getBudgetPersonData() {
        return budgetPersonData;
    }
    
    /**
     * Setter for property budgetPersonData.
     * @param budgetPersonData New value of property budgetPersonData.
     */
    public void setBudgetPersonData(edu.mit.coeus.utils.CoeusVector budgetPersonData) {
        this.budgetPersonData = budgetPersonData;
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
    
    /** Case #1801:Parameterize Under-recovery and cost-sharing distribution End 4
     */
    
    //Proposal Hierarchy Enhancment Start
    private void syncPropHierarchyBudget(){
        int syncFlag = 0;
        /*String message = "Are you sure, you want to sync the Budget";
        int answer = CoeusOptionPane.showQuestionDialog(message,
        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
        if (answer == JOptionPane.YES_OPTION) {*/
        try{
            CoeusVector syncData =  connectToSyncChild();
            
            if(syncData!= null && syncData.size()>0) {
                syncFlag = Integer.parseInt(syncData.elementAt(0).toString());
                if(syncFlag == 1){
                    queryEngine.removeDataCollection(queryKey);
                    
                    Hashtable budgetData = (Hashtable)syncData.get(1);
                    
                    // Get the Propsoal Hierarchy data
                    HashMap hierarchyMap = (HashMap)syncData.elementAt(2);
                    
                    // Get the Proposal Hierarchy Tree data
                    proposalHierarchyBean = (ProposalHierarchyBean)syncData.elementAt(3);
                    
                    CoeusVector cvDisplayOption = new CoeusVector();
                    cvDisplayOption.add(budgetData.get(BUDGET_SUMMARY_DISPLAY_OPTION));
                    budgetData.put(BUDGET_SUMMARY_DISPLAY_OPTION, cvDisplayOption);
                    
                    queryEngine.addDataCollection(queryKey, budgetData);
                    
                    
                    CoeusVector cv = queryEngine.executeQuery(queryKey, budgetInfoBean);
                    budgetInfoBean = (BudgetInfoBean)cv.get(0);
                    
                    CoeusVector cvProposalDetails;
                    try{
                        cvProposalDetails = queryEngine.getDetails(queryKey,ProposalDevelopmentFormBean.class);
                        if (cvProposalDetails != null && cvProposalDetails.size() > 0) {
                            proposalDevelopmentFormBean = (ProposalDevelopmentFormBean)
                            cvProposalDetails.get(0);
                        }
                    }catch(CoeusException coeusException){
                        coeusException.printStackTrace();
                    }
                    
                    budgetInfoBeanModified = false;
                    refresh();
                    
                    /** Check if the selected proposal is in Hierarchy. If it is in Hierarchy
                     *then check for the parent and then set the values
                     */
                    if(hierarchyMap!= null){
                        boolean inHierarchy = ((Boolean)hierarchyMap.get("IN_HIERARCHY")).booleanValue();
                        boolean isParent = false;
                        if(inHierarchy) {
                            setHierarcy(inHierarchy);
                            setParentPropNo((String)hierarchyMap.get("PARENT_PROPOSAL"));
                            isParent = ((Boolean)hierarchyMap.get("IS_PARENT")).booleanValue();
                            if(isParent){
                                setParentProposal(isParent);
                            }
                        }
                        
                        if(isParentProposal()){
                            hierarchyController.setProposalHierarchyBean(proposalHierarchyBean);
                            hierarchyController.setModule("");
                            hierarchyController.setFormData(unitNumber);
                            budgetBaseWindow.budgetModularMenuItem.setEnabled(true);
                            
                        }
                    }//End of inner if
                }//End of outer if
            }
        }catch (CoeusException cEx){
            cEx.printStackTrace();
            CoeusOptionPane.showErrorDialog(cEx.getMessage());
        }catch (Exception ex){
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(ex.getMessage());
        }
        
        if(syncFlag< 0) {
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
            "Error while Syncing all proposals"));
            return;
        }
        //}
    }//End syncPorpHierarchyBudget
    
    private CoeusVector connectToSyncChild() throws CoeusException,Exception{
        String connectTo = CoeusGuiConstants.CONNECTION_URL + HIERARCHY_SERVLET;
        RequesterBean request = new RequesterBean();
        CoeusVector cvData = null;
        
        CoeusVector cvDataToServer = new CoeusVector();
        
        cvDataToServer.add(getParentPropNo());// Parent proposal Number
        cvDataToServer.add(budgetInfoBean.getProposalNumber());
        
        cvDataToServer.add(new Boolean(isParentProposal()));
        cvDataToServer.add(budgetInfoBean);
        
        request.setDataObject(cvDataToServer);
        request.setFunctionType(SYNC_BUDGET);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                cvData = (CoeusVector)response.getDataObject();
            }else {
                throw new CoeusException(response.getMessage());
            }
        }
        return cvData;
    }
    
    /**
     * Getter for property parentPropNo.
     * @return Value of property parentPropNo.
     */
    public java.lang.String getParentPropNo() {
        return parentPropNo;
    }
    
    /**
     * Setter for property parentPropNo.
     * @param parentPropNo New value of property parentPropNo.
     */
    public void setParentPropNo(java.lang.String parentPropNo) {
        this.parentPropNo = parentPropNo;
    }
    
    /**
     * Getter for property hierarchyMode.
     * @return Value of property hierarchyMode.
     */
    public boolean isHierarchyMode() {
        return hierarchyMode;
    }
    
    /**
     * Setter for property hierarchyMode.
     * @param hierarchyMode New value of property hierarchyMode.
     */
    public void setHierarchyMode(boolean hierarchyMode) {
        this.hierarchyMode = hierarchyMode;
    }
    
    //Proposal Hierarchy Enhancment End
    //Added for Case #3121 Tuition Fee Calculation Enhancement - Start
    /**
     * Is used to calculate the start date of the line item
     * @param PeriodStartDate
     * @param LineItemStartDate
     * @param Difference in months
     */
    private static Date calculateStartDate(Date periodStrDate, Date liStrDate, int diffMonths) {
        GregorianCalendar fromCal = new GregorianCalendar(TimeZone.getDefault());
        GregorianCalendar toCal = new GregorianCalendar(TimeZone.getDefault());
        
        //Calculation of new Start date
        fromCal.setTime(liStrDate);
        String strDay = new Integer(fromCal.get(Calendar.DATE)).toString();
        toCal.setTime(periodStrDate);
        toCal.add(Calendar.MONTH, diffMonths);
        toCal.set(Calendar.DATE, Integer.parseInt(strDay));
        Date newStrtDate = toCal.getTime();
        return newStrtDate;
    }
    
    /**
     * Is used to calculate the end date of the line item
     * @param LineItemStartDate
     * @param Ole LineItemEndDate
     * @param Difference in months
     */
    private static Date calculateEndDate(Date liStrDate, Date oldLIEndDate, int diffMonths) {
//        GregorianCalendar fromCal = new GregorianCalendar(TimeZone.getDefault());
        GregorianCalendar toCal = new GregorianCalendar(TimeZone.getDefault());
        
        toCal.setTime(oldLIEndDate);
        int endDay = toCal.get(Calendar.DATE);
        int days = toCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        boolean lastDay = true;
        
        //Calculation of end date
        toCal.setTime(liStrDate);
        toCal.add(Calendar.MONTH, diffMonths);
        if(endDay == days) {
            days = toCal.getActualMaximum(Calendar.DAY_OF_MONTH);
            toCal.set(Calendar.DATE, days);
        } else {
            toCal.set(Calendar.DATE, endDay);
            lastDay = false;
        }
        Date newEndDate = toCal.getTime();
        return newEndDate;
    }
    //Added for Case #3121 Tuition Fee Calculation Enhancement - End
    
    /**
     * Added for Case#2341 - Recalculate Budget if Project dates change
     * Method to Sync the budget start and end dates with the project dates
     */
    public void syncToProposalDates(){
        try{
            java.sql.Date proposalStartDate =  proposalDevelopmentFormBean.getRequestStartDateInitial();
            java.sql.Date proposalEndDate = proposalDevelopmentFormBean.getRequestEndDateInitial();
            java.sql.Date budgetStartDate =  budgetInfoBean.getStartDate();
            java.sql.Date budgetEndDate =  budgetInfoBean.getEndDate();
            ObjectCloner.deepCopy(budgetStartDate);
            ObjectCloner.deepCopy(budgetEndDate);
            boolean changeNeeded = false;
            //Commented for case 2341:Recalculate Budget if Project dates change - start
            // Check if budget start date with in the proposal start date else show the error message
//            if(budgetStartDate != null &&
//                    budgetStartDate.before(proposalStartDate)){
//                changeNeeded = true;
//                // Check if budget start date with in the proposal end date else show the error message
//            }else if(budgetEndDate != null &&
//                    budgetEndDate.after(proposalEndDate)){
//                changeNeeded = true;
//            }
            //Commented for case 2341:Recalculate Budget if Project dates change - end
            //Added for case 2341:Recalculate Budget if Project dates change - start
             if(budgetStartDate != null &&
                    !budgetStartDate.equals(proposalStartDate)){
                changeNeeded = true;
            }else if(budgetEndDate != null &&
                    !budgetEndDate.equals(proposalEndDate)){
                changeNeeded = true;
            }
            //Added for case 2341:Recalculate Budget if Project dates change - end
            if(changeNeeded && CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey("budgetSummary_exceptionCode.1119"), 
                    CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES) == JOptionPane.YES_OPTION){
                // Added for COEUSDEV-752 Could not save budget details error  - Start
                saveBudget(); //Save to Database
                // Added for COEUSDEV-752 - End
                budgetInfoBean.setStartDate(proposalDevelopmentFormBean.getRequestStartDateInitial());
                budgetInfoBean.setEndDate(proposalDevelopmentFormBean.getRequestEndDateInitial());
                budgetSummaryController.budgetSummaryForm.txtStartDate.setText(
                        dtUtils.formatDate(budgetInfoBean.getStartDate().toString(), REQUIRED_DATEFORMAT));
                budgetSummaryController.budgetSummaryForm.txtEndDate.setText(
                        dtUtils.formatDate(budgetInfoBean.getEndDate().toString(), REQUIRED_DATEFORMAT));
                Equals eqPropNo = new Equals("proposalNumber", budgetInfoBean.getProposalNumber());
                Equals eqVersionNo = new Equals("versionNumber", new Integer(budgetInfoBean.getVersionNumber()));
                And eqPropNoAndEqVersionNo = new And(eqPropNo, eqVersionNo);
                And eqPropNoAndEqVersionNoAndActivePeriods = new And(eqPropNoAndEqVersionNo, CoeusVector.FILTER_ACTIVE_BEANS);
                CoeusVector vecOldPeriodDetails = queryEngine.executeQuery(queryKey,BudgetPeriodBean.class, eqPropNoAndEqVersionNoAndActivePeriods);
                vecOldPeriodDetails = (vecOldPeriodDetails == null)? new CoeusVector() : vecOldPeriodDetails;
                ObjectCloner.deepCopy(vecOldPeriodDetails);
                CoeusVector vecPeriodDetails = queryEngine.executeQuery(queryKey,BudgetPeriodBean.class, eqPropNoAndEqVersionNoAndActivePeriods);
                vecPeriodDetails = (vecPeriodDetails == null)? new CoeusVector() : vecPeriodDetails;
                BudgetPeriodBean budgetPeriodBean = null;
                CoeusVector newPeriods = new CoeusVector();
                Calendar calStart, calEnd, calPeriodStart, calPeriodEnd;
                int startYear, endYear;

                calStart = Calendar.getInstance();
                calStart.setTime(proposalDevelopmentFormBean.getRequestStartDateInitial());

                calEnd = Calendar.getInstance();
                calEnd.setTime(proposalDevelopmentFormBean.getRequestEndDateInitial());

                startYear = calStart.get(Calendar.YEAR);
                endYear = calEnd.get(Calendar.YEAR);
                if(startYear <= endYear) {
                    //Proposal spans more thrn a year. Break up required.
                    calPeriodStart = calStart;
                    calPeriodEnd = Calendar.getInstance();
                    int budgetPeriod = 0;
                    while(true) {
                        budgetPeriod = budgetPeriod + 1;
                        if(vecPeriodDetails.size() >= budgetPeriod){
                            budgetPeriodBean = (BudgetPeriodBean) vecPeriodDetails.get(budgetPeriod-1);
                            budgetPeriodBean.setAcType(TypeConstants.UPDATE_RECORD);
                        } else {
                            budgetPeriodBean = new BudgetPeriodBean();
                            budgetPeriodBean.setAcType(TypeConstants.INSERT_RECORD);
                        }
                        budgetPeriodBean.setBudgetPeriod(budgetPeriod);
                        budgetPeriodBean.setAw_BudgetPeriod(budgetPeriod);
                        budgetPeriodBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                        budgetPeriodBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                        budgetPeriodBean.setStartDate(new java.sql.Date(calPeriodStart.getTimeInMillis()));

                        calPeriodStart.add(Calendar.YEAR, 1);
                        calPeriodStart.add(Calendar.DATE, -1);
                        calPeriodEnd.setTimeInMillis(calPeriodStart.getTimeInMillis());
                        calPeriodStart.add(Calendar.DATE, 1);

                        if(calPeriodEnd.after(calEnd) || calPeriodEnd.equals(calEnd)) {
                            budgetPeriodBean.setEndDate(proposalDevelopmentFormBean.getRequestEndDateInitial());
                            newPeriods.add(budgetPeriodBean);
                            break;
                        }
                        
                        budgetPeriodBean.setEndDate(new java.sql.Date(calPeriodEnd.getTimeInMillis()));
                        newPeriods.add(budgetPeriodBean);
                    }
                    queryEngine.addCollection(queryKey, BudgetPeriodBean.class, newPeriods);
                }
                
               // Check if budget start date with in the proposal end date else show the error message
               if(vecOldPeriodDetails.size() <= newPeriods.size()){
                    adjustPeriodBoundaryController = new AdjustPeriodBoundaryController(mdiForm,true, budgetInfoBean);
                    adjustPeriodBoundaryController.setSaveNeeded(true);
                    adjustPeriodBoundaryController.setCvOldPeriodDetails(vecOldPeriodDetails);
                    adjustPeriodBoundaries();
                    adjustPeriodBoundaryController.setSaveNeeded(false);
                } else {
                     CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey("budgetSummary_exceptionCode.1120"));//Cannot adjust the budget period boundaries as per the project dates
                }                
                if(!isAdjusted){
                    budgetInfoBean.setStartDate(budgetStartDate);
                    budgetInfoBean.setEndDate(budgetEndDate);
                    budgetSummaryController.budgetSummaryForm.txtStartDate.setText(
                        dtUtils.formatDate(budgetInfoBean.getStartDate().toString(), REQUIRED_DATEFORMAT));
                    budgetSummaryController.budgetSummaryForm.txtEndDate.setText(
                        dtUtils.formatDate(budgetInfoBean.getEndDate().toString(), REQUIRED_DATEFORMAT));
                    queryEngine.addCollection(queryKey, BudgetPeriodBean.class, vecOldPeriodDetails);
                }
            }
        }catch(Exception e){
            CoeusOptionPane.showErrorDialog(e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Added for Case#2341 - Recalculate Budget if Project dates change
     * Method to save the institute proposal rates details
     */
    public CoeusVector saveInstituteRates(CoeusVector vecInstRateBeans) {
        CoeusVector newRates = new CoeusVector();
        ProposalRatesBean newProposalRatesBean = null;
//        ProposalRatesBean proposalRatesBean = new ProposalRatesBean();
        if(vecInstRateBeans != null && vecInstRateBeans.size() >0) {
            //===============SAVE WITH NEW INSITITUTE RATES ON SYNC OPERATION ============
            try {
                if(vecInstRateBeans.get(0).getClass() == InstituteRatesBean.class) {
                    deleteInstRateFromBaseWindow();
                    for(int insideIndex=0;insideIndex< vecInstRateBeans.size();insideIndex++ ) {
                        InstituteRatesBean instituteRatesBean = (InstituteRatesBean) vecInstRateBeans.get(insideIndex);                        
                        newProposalRatesBean = new ProposalRatesBean();
                        newProposalRatesBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                        newProposalRatesBean.setVersionNumber(budgetInfoBean.getVersionNumber());
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
                        newProposalRatesBean.setApplicableRate(instituteRatesBean.getInstituteRate());
                        newProposalRatesBean.setAw_ActivityTypeCode(instituteRatesBean.getActivityCode());
                        CoeusVector  timeStampCoeusVector = queryEngine.executeQuery(
                                (budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber()), newProposalRatesBean);
                        if(timeStampCoeusVector != null && timeStampCoeusVector.size() > 0) {
                            newProposalRatesBean.setUpdateTimestamp(((ProposalRatesBean) timeStampCoeusVector.get(0)).getUpdateTimestamp());
                        }
                        newRates.add(newProposalRatesBean);
                    }
                }
                
                // Inserting the New Rates to base window
                try{
                    for(int j=0;j< newRates.size();j++ ) {
                        ProposalRatesBean insertProposalRatesBean = (ProposalRatesBean) newRates.get(j);
                        queryEngine.insert(queryKey, insertProposalRatesBean);
                    }
                    newProposalRatesBean = new ProposalRatesBean();
                    newProposalRatesBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                    newProposalRatesBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                    CoeusVector  coeusVect = queryEngine.executeQuery(queryKey, newProposalRatesBean);
                    coeusVect.sort("acType",true);
                    
                }catch(Exception exception){
                    exception.getMessage();
                }
            } catch(Exception exception){
                exception.getMessage();
            }
        }
        return newRates;
    }
    
    /**
     * Added for Case#2341 - Recalculate Budget if Project dates change
     * Delete the existing ProposalRates beans in Base window details on SYNC operation
     */
    public void deleteInstRateFromBaseWindow() {
        
        try {
            ProposalRatesBean proposalRatesBean = new ProposalRatesBean();
            proposalRatesBean.setProposalNumber(budgetInfoBean.getProposalNumber());
            proposalRatesBean.setVersionNumber(budgetInfoBean.getVersionNumber());
            Equals equalsActype = new Equals("acType",null);
            NotEquals notequalsActype = new NotEquals("acType",TypeConstants.DELETE_RECORD);
            Or actypeBoth = new Or(equalsActype,notequalsActype);
            CoeusVector existingInstBeans = queryEngine.executeQuery(queryKey, ProposalRatesBean.class,actypeBoth);
            for(int i=0;i < existingInstBeans.size();i++ ) {
                ProposalRatesBean baseProposalRatesBean  = (ProposalRatesBean) existingInstBeans.get(i);
                baseProposalRatesBean.setAcType(TypeConstants.DELETE_RECORD);
                queryEngine.delete(queryKey, baseProposalRatesBean);
            }
            
        }catch(Exception exception){
            exception.getMessage();
        }
    }
    
    /**
     * Added for Case#2341 - Recalculate Budget if Project dates change
     * Method to sync LA rates details
     */
    public CoeusVector saveLARates(CoeusVector vecLARateBeans) {
        CoeusVector newLARates = new CoeusVector();
        ProposalLARatesBean newProposalLARatesBean = null;
        InstituteLARatesBean instituteLARatesBean = new InstituteLARatesBean();
//        ProposalLARatesBean proposalLARatesBean = new ProposalLARatesBean();
        if(vecLARateBeans != null && vecLARateBeans.size() >0) {
            try {
                if(vecLARateBeans.get(0).getClass() == instituteLARatesBean.getClass()) {
                    deleteLARatesFromBaseWindow();
                    
                    for(int insideIndex=0;insideIndex< vecLARateBeans.size();insideIndex++ ) {
                        instituteLARatesBean = (InstituteLARatesBean) vecLARateBeans.get(insideIndex);
                        newProposalLARatesBean = new ProposalLARatesBean();
                        newProposalLARatesBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                        newProposalLARatesBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                        newProposalLARatesBean.setAcType(TypeConstants.INSERT_RECORD);
                        newProposalLARatesBean.setRateClassCode(instituteLARatesBean.getRateClassCode());
                        newProposalLARatesBean.setRateClassDescription(instituteLARatesBean.getRateClassDescription());
                        newProposalLARatesBean.setRateTypeCode(instituteLARatesBean.getRateTypeCode());
                        newProposalLARatesBean.setRateTypeDescription(instituteLARatesBean.getRateTypeDescription());
                        newProposalLARatesBean.setFiscalYear(instituteLARatesBean.getFiscalYear());
                        newProposalLARatesBean.setOnOffCampusFlag(instituteLARatesBean.isOnOffCampusFlag());
                        newProposalLARatesBean.setStartDate(instituteLARatesBean.getStartDate());
                        newProposalLARatesBean.setInstituteRate(instituteLARatesBean.getInstituteRate());
                        newProposalLARatesBean.setApplicableRate(instituteLARatesBean.getInstituteRate());
                        
                        CoeusVector  timeStampCoeusVector = queryEngine.executeQuery(
                                (budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber()), newProposalLARatesBean);
                        
                        if(timeStampCoeusVector != null && timeStampCoeusVector.size() > 0) {
                            newProposalLARatesBean.setUpdateTimestamp(((ProposalLARatesBean) timeStampCoeusVector.get(0)).getUpdateTimestamp());
                        }
                        newLARates.add(newProposalLARatesBean);
                    }
                }
                // Inserting the New LARates to base window
                try{
                    
                    for(int j=0;j< newLARates.size();j++ ) {
                        ProposalLARatesBean insertProposalLARatesBean = (ProposalLARatesBean) newLARates.get(j);
                        queryEngine.insert(queryKey, insertProposalLARatesBean);
                    }
                    newProposalLARatesBean = new ProposalLARatesBean();
                    newProposalLARatesBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                    newProposalLARatesBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                    CoeusVector  coeusVect = queryEngine.executeQuery(queryKey, newProposalLARatesBean);
                    coeusVect.sort("acType",true);
                }catch(Exception exception){
                    exception.getMessage();
                }
            }catch(Exception exception){
                exception.getMessage();
            }
        }
        return newLARates;
    }
    
    /**
     * Added for Case#2341 - Recalculate Budget if Project dates change
     * Delete the existing ProposalLARates beans in base window on SYNC operation
     */    
    public void deleteLARatesFromBaseWindow() {
        try {
            ProposalLARatesBean newProposalLARatesBean = new ProposalLARatesBean();
            newProposalLARatesBean.setProposalNumber(budgetInfoBean.getProposalNumber());
            newProposalLARatesBean.setVersionNumber(budgetInfoBean.getVersionNumber());
            Equals equalsActype = new Equals("acType",null);
            NotEquals notequalsActype = new NotEquals("acType",TypeConstants.DELETE_RECORD);
            Or actypeBoth = new Or(equalsActype,notequalsActype);
            
            CoeusVector existingLABeans = queryEngine.executeQuery(queryKey, ProposalLARatesBean.class,actypeBoth);
            for(int i=0;i < existingLABeans.size();i++ ) {
                ProposalLARatesBean baseProposalLARatesBean = (ProposalLARatesBean) existingLABeans.get(i);
                baseProposalLARatesBean.setAcType(TypeConstants.DELETE_RECORD);
                queryEngine.delete(queryKey, baseProposalLARatesBean);
            }
        }catch(Exception exception) {
            exception.getMessage();
        }
    }
    //Methods added with case 2158: Budget Validations - Start
    //This method is called from budget summary to validate status change  from budget summary.
   // This method saves the entire budget and then validates.
    public boolean validateSummaryStatusChange(){
        
        blockEvents(true);
        try{
            if(! validate()) {
                statusWindow.setVisible(false);
                blockEvents(false);
                return false;
            }
            if(!isParentProposal()){
                statusWindow.setHeader("Calculating Budget");
            }
            statusWindow.setFooter("Please Wait...");
            statusWindow.display();
            setValidatingBudgetSummary(true);
            saveFormData(); //Save to Query Engine
            if(!isParentProposal() && ! calculate(queryKey, CALCULATE_ALL_PERIODS)) {
                CoeusOptionPane.showErrorDialog("Server Error : Calculation failed");
                statusWindow.setVisible(false);
                blockEvents(false);
                setValidatingBudgetSummary(false);
                return false;
            }
            statusWindow.setHeader("Saving Budget");
            otherCalculation = false;
            saveBudget(); //Save to Database
            statusWindow.setHeader("Refreshing Budget Information");
            refresh();
            statusWindow.setHeader("Validating Budget...");
            char errType = performValidationChecks();
            if(errType == VALIDATION_ERROR){
                statusWindow.setVisible(false);
                blockEvents(false);
                setValidatingBudgetSummary(false);
                budgetSummaryController.budgetSummaryForm.cmbBudgetStatus.setSelectedItem("Incomplete");
                budgetSummaryController.budgetSummaryForm.chkFinal.setEnabled(true);
                return false;
            }else if(errType == VALIDATION_WARNING){
                int option = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(MSGKEY_VALIDATION_WARNINGS),
                        CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
                if(option == JOptionPane.NO_OPTION){
                    statusWindow.setVisible(false);
                    blockEvents(false);
                    
                    setValidatingBudgetSummary(false);
                    budgetSummaryController.budgetSummaryForm.cmbBudgetStatus.setSelectedItem("Incomplete");
                    budgetSummaryController.budgetSummaryForm.chkFinal.setEnabled(true);
                    return false;
                }
            }
            statusWindow.setVisible(false);
            blockEvents(false);
            setValidatingBudgetSummary(false);
        }catch (Exception exception) {
            exception.printStackTrace();
            statusWindow.setVisible(false);
            blockEvents(false);
            setValidatingBudgetSummary(false);
        }
        return true;
    }

    public boolean isValidatingBudgetSummary() {
        return validatingBudgetSummary;
    }

    public void setValidatingBudgetSummary(boolean validatingBudgetSummary) {
        this.validatingBudgetSummary = validatingBudgetSummary;
    }
    /**
     * This method performs validation checks added for budget sub module
     * This method performs checks and displays any errors/warnings encountered
     *
     */
    private char performValidationChecks(){
        
        char errType = 0;
        Vector vecBrokenRules=null;
        Vector inputVector= new Vector();
//        Vector dataObjects = null;
        String leadUnitNumber = proposalDevelopmentFormBean.getOwnedBy();
        inputVector.add(new Integer(ModuleConstants.PROPOSAL_DEV_MODULE_CODE));//module code
        inputVector.add(budgetInfoBean.getProposalNumber());//moduleitem key
        inputVector.add(new Integer(budgetInfoBean.getVersionNumber()));//moduleitemkeysequence
        inputVector.add(leadUnitNumber);
        inputVector.add(new Integer(1));//approval sequence
        inputVector.add(new Integer(ModuleConstants.PROPOSAL_DEV_BUDGET_SUB_MODULE));//sub module code
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/RoutingServlet";
        
        RequesterBean request = new RequesterBean();
        request.setFunctionType(VALIDATION_CHECKS);
        request.setDataObjects(inputVector);
        AppletServletCommunicator comm
                = new AppletServletCommunicator( connectTo, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
        }
        if (response.isSuccessfulResponse()) {
            vecBrokenRules = response.getDataObjects();
            if (vecBrokenRules != null && vecBrokenRules.size() > 0){
                RoutingValidationChecksForm valChecks = new RoutingValidationChecksForm(CoeusGuiConstants.getMDIForm(),vecBrokenRules,3, budgetInfoBean.getProposalNumber());
                valChecks.display();
                if(valChecks.isError()){
                    errType = VALIDATION_ERROR;
                }else{
                    errType = VALIDATION_WARNING;
                }
            }
        } else{
            CoeusOptionPane.showErrorDialog(response.getMessage());
        }
        return errType;
    }
    //2158 end
    
    // Added for Case 3197 - Allow for the generation of project period greater than 12 months -start
    /**
     * This method is used to get the Year value on the Date.
     * @param Date StartDate
     * @return int YEAR value
     */
//    private int getPeriodYear(Date startDate) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(startDate);
//        return calendar.get(Calendar.YEAR);
//    }
     // Modified for COEUSQA-3038 -CLONE - Leap Year Personnel Budget Details Window 
    /**
     * This method is to get valid Person start date or end date based on Period start and end date.
     * @param periodStartDate
     * @param periodEndDate
     * @param startDate
     * @param endDate
     * @param personOldStartDate
     * @param personOldEndDate
     * @param previousPeriodStartDate
     * @param previousPeriodEndDate
     * @return Date (valid start / end Date)
     */
    private Date getValidPersonDateforPeriod(Date periodStartDate, Date periodEndDate, Date startDate, Date endDate,
            Date personOldStartDate,Date personOldEndDate,Date previousPeriodStartDate,
            Date previousPeriodEndDate, Date currentPersonStartDate){
        Vector vecPossibleYears = new Vector();
        Vector vecPersonYear = new Vector();
        Date finalDate = new Date();
        // get period start date month and year
        Calendar startDatecalendar = Calendar.getInstance();
        startDatecalendar.setTime(periodStartDate);
        int periodStartDateMonth = startDatecalendar.get(Calendar.MONTH);
        int periodStartDateYear = startDatecalendar.get(Calendar.YEAR);
        
        // get period end date month and year
        Calendar endDatecalendar = Calendar.getInstance();
        endDatecalendar.setTime(periodEndDate);
//        int periodEndDateMonth = endDatecalendar.get(Calendar.MONTH);
        int periodEndDateYear = endDatecalendar.get(Calendar.YEAR);
        
        //Modified for COEUSQA-3422 COEUSQA-3422 Budget Persons Line Item Details END DATES match Budget Period END DATES in
        //Leap Years using February -Start
        Calendar personStrDatecalendar = Calendar.getInstance();
        personStrDatecalendar.setTime(personOldStartDate);
        int personOldStartDateYear = personStrDatecalendar.get(Calendar.YEAR);
        
        Calendar personEndDatecalendar = Calendar.getInstance();
        personEndDatecalendar.setTime(personOldEndDate);
        int personOldEndDateYear = personEndDatecalendar.get(Calendar.YEAR);
        //Modified for COEUSQA-3422 -End
        
        int personStartDateMonth = 0;
//        int personStartDateYear = 0;
        if(startDate != null){
            Calendar personStartDatecalendar = Calendar.getInstance();
            personStartDatecalendar.setTime(startDate);
            personStartDateMonth = personStartDatecalendar.get(Calendar.MONTH);
//            personStartDateYear = personStartDatecalendar.get(Calendar.YEAR);
            finalDate = startDate;
        }
        
//        int personEndDateMonth = 0;
//        int personEndDateYear = 0;
         // Added for COEUSQA-3038 -CLONE - Leap Year Personnel Budget Details Window -Start
        // Compare previous period end data and previous period person date are same, if both are same assign period end date as person end date.
        if(endDate != null){
            if(previousPeriodEndDate.compareTo(personOldEndDate) == 0){
                endDate = periodEndDate;
            }
            // Added for COEUSQA-3038 - End  
//            Calendar personEndDatecalendar = Calendar.getInstance();
//            personEndDatecalendar.setTime(endDate);
//            personEndDateMonth = personEndDatecalendar.get(Calendar.MONTH);
//            personEndDateYear = personEndDatecalendar.get(Calendar.YEAR);
            finalDate = endDate;
        }
        
        for(int endYear = periodStartDateYear+1; endYear<= periodEndDateYear; endYear++){
            vecPossibleYears.addElement(new Integer(endYear));
        }
        if(vecPossibleYears !=null && vecPossibleYears.size() > 0){
            //If Person Starting Month is with in that period staring year, apply period start year as Person year.
            if( startDate != null && personStartDateMonth >= periodStartDateMonth && personStartDateMonth < 12 ){
                vecPersonYear.add(new Integer(periodStartDateYear));
            }else if( startDate != null){
                vecPersonYear.add(vecPossibleYears.get(0));
            }
            //Modified for COEUSQA-3422 Budget Persons Line Item Details END DATES match Budget Period END DATES in
            //Leap Years using February - Start
            //To find valid EndDate, if person pervious Start Date and End Date are falls on the same year,
            // future endDate also has to fall on furture person startDate year.
            if(endDate != null &&  personOldStartDateYear == personOldEndDateYear){
                Calendar personCurrentStartDatecalendar = Calendar.getInstance();
                personCurrentStartDatecalendar.setTime(currentPersonStartDate);
//        int periodEndDateMonth = personCurrentStartDatecalendar.get(Calendar.MONTH);
                int personCurrentStartDateYear = personCurrentStartDatecalendar.get(Calendar.YEAR);
                vecPersonYear.add(new Integer(personCurrentStartDateYear));
                
            }else if (endDate != null){
                vecPersonYear.add(new Integer(getValidPeriodEndYear(vecPossibleYears, periodStartDate, periodEndDate, personOldStartDate, personOldEndDate)));
            }
            ////Modified for COEUSQA-3422 -End
        }// If it is a single year period.
        else{
            vecPersonYear.add(new Integer(periodStartDateYear));
        }
        
        Calendar calendar = Calendar.getInstance();
        int year = 0;
//        String DATE_FORMAT = "yyyy-MM-dd";
//        java.text.SimpleDateFormat sdf =
//                new java.text.SimpleDateFormat(DATE_FORMAT);
        if(vecPersonYear !=null && vecPersonYear.size() > 0){
            year = ((Integer) vecPersonYear.get(0)).intValue();
        }
      //Modified for COEUSQA-3422 Budget Persons Line Item Details END DATES match Budget Period END DATES in 
        //Leap Years using February - Start
        calendar.setTime(finalDate);
        calendar.set(Calendar.YEAR, year);
        
        // Added for COEUSDEV-309  Adjust period boundaries and generate all periods is not copiying personnel items correctly -start
        // If it is a single year period, check if person end is beyond the period end date, Apply Period End Date as Person end date.
         finalDate = calendar.getTime();
         //Modified for COEUSQA-3422 -End
         
          // Added for COEUSDEV-794 : End of the month period date not recognized when the first period is on a Leap year -Start
       // Check whether the person Date is come under leap year, if it is leap year reduce one day.
        if(isDateLeapYear(finalDate)){
            finalDate = adjustDateForLeapYear(finalDate);
         }         
         // Added for COEUSDEV-794 -End
         
        if(vecPossibleYears == null || vecPossibleYears.isEmpty() && endDate != null){
            
            if(periodStartDate.compareTo(finalDate) > 0 && periodEndDate.compareTo(finalDate) > 0) {
                 calendar.setTime(periodEndDate);
                 calendar.set(Calendar.YEAR, year);
                 finalDate = calendar.getTime();
            }
        }
        // Added for case COEUSDEV-309 -end
//        return calendar.getTime();
        return finalDate;
    }
    
    /*
     * This method is used to check the Valid Period year based on period end date.
     */
    
    private int getValidPeriodEndYear(Vector vecPossibleYears, Date periodStartDate, Date periodEndDate, Date personOldStartDate, Date personOldEndDate){
        int validPersonStartYear = 0;
        int validPersonEndYear = 0;
        HashMap hmValidEndYear = new HashMap();
        
        Calendar startDateCalendar = Calendar.getInstance();
        startDateCalendar.setTime(periodStartDate);
        int periodStartDateMonth = startDateCalendar.get(Calendar.MONTH);
        int periodStartDateYear = startDateCalendar.get(Calendar.YEAR);
        
        //Get Period End Date
        Calendar endDatecalendar = Calendar.getInstance();
        endDatecalendar.setTime(periodEndDate);
        int periodEndDateMonth = endDatecalendar.get(Calendar.MONTH);
        int periodEndDateYear = endDatecalendar.get(Calendar.YEAR);
        
        Calendar personStartDtCalendar = Calendar.getInstance();
        personStartDtCalendar.setTime(personOldStartDate);
        int personOldStartDateMonth = personStartDtCalendar.get(Calendar.MONTH);
//        int personOldStartDateYear = personStartDtCalendar.get(Calendar.YEAR);
        
        Calendar personEndDtCalendar = Calendar.getInstance();
        personEndDtCalendar.setTime(personOldEndDate);
//        int personOldEndDateMonth = personEndDtCalendar.get(Calendar.MONTH);
//        int personOldEndDateYear = personEndDtCalendar.get(Calendar.YEAR);
        
        
        //If Person Starting Month is with in that period staring year, apply period start year as Person year.
        if( periodStartDate != null && personOldStartDateMonth >= periodStartDateMonth &&  periodStartDateMonth < 12 ){
            validPersonStartYear = periodStartDateYear;
        }else{    // Person Starting Month is not with Period Start year, need to check in the next year.
            if (vecPossibleYears != null && vecPossibleYears.size() > 0) {
                validPersonStartYear = ((Integer)vecPossibleYears.get(0)).intValue();
            }
        }
        // Calculate the Number of Days difference in all the Possible years.
        if (vecPossibleYears != null && vecPossibleYears.size() > 0) {
            for(int index =0; index < vecPossibleYears.size(); index++){
                int endYear = ((Integer) vecPossibleYears.get(index)).intValue();
                int daysDiff = dtUtils.calculateDateDiff(2, mergePeriodDate(personOldStartDate, validPersonStartYear) , mergePeriodDate(personOldEndDate, endYear));
                hmValidEndYear.put(new Integer(daysDiff), new Integer(endYear));
            }
        }
        //Get the Number of days difference for Orginal Person Start and End date.
        int personNoOfDaysDiff = dtUtils.calculateDateDiff(2, personOldStartDate,personOldEndDate ) ;
        
        // Based on the number of days in the first period, get the exact value in the possible year hashmap.
        if(hmValidEndYear !=null && hmValidEndYear.size() > 0){
            if (hmValidEndYear.get(new Integer(personNoOfDaysDiff)) != null){
                validPersonEndYear = ((Integer)  hmValidEndYear.get(new Integer(personNoOfDaysDiff))).intValue();
            }else{
//                validPersonEndYear = periodStartDateYear;
                // Added for COEUSQA-3038 -CLONE - Leap Year Personnel Budget Details Window -Start
                validPersonEndYear = periodEndDateYear;
                // Added for COEUSQA-3038 - End
            }
        }else{
            validPersonEndYear = periodStartDateYear;
        }
        
        
        
        return validPersonEndYear;
    }
    
    /**
     * This method is to merge the Year with source date.
     * This is used to merge person date with specified year.
     * @param StartDate
     * @param Year
     * @return Date
     */
    private Date mergePeriodDate(Date startDate, int year){
        Calendar calendar = Calendar.getInstance();
//        String DATE_FORMAT = "yyyy-MM-dd";
//        java.text.SimpleDateFormat sdf =
//                new java.text.SimpleDateFormat(DATE_FORMAT);
        calendar.setTime(startDate);
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }
    
    //Added for Case 3197 - Allow for the generation of project period greater than 12 months -start
    /**
     * This method gets the cost elements data from table OSP$COST_ELEMENT
     * @return vecCostElements Vector
     * @throws Exception CoeusException
     */
    private double getNumberOfMonths(Date startDate, Date endDate){
        RequesterBean requesterBean = new RequesterBean();
        double noOfMonths = 0;
         try{
        
//        Vector vecCostElements = null;
        Vector vecDates = new Vector();
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + SERVLET);
        requesterBean.setFunctionType('s');
        vecDates.add(startDate);
        vecDates.add(endDate);
        requesterBean.setDataObjects(vecDates);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean!= null){
            if(!responderBean.hasResponse()){
                throw new CoeusException(responderBean.getMessage(), 1);
            }else{                 
                noOfMonths = ((Double)responderBean.getDataObject()).doubleValue();
              
            }
        }
        }catch (CoeusException exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
        return noOfMonths;
    }  
    
    //3197 - End
    //3197 - End
    //COEUSDEV-197,198:Budget Validations during Syncing
    private char validateForHierarchy(String proposalNumber,boolean isParent) {
        
        char errType = 0;
        Vector inputVector= new Vector();
        String leadUnitNumber = proposalDevelopmentFormBean.getOwnedBy();
        inputVector.add(proposalNumber);//module item key
        inputVector.add(leadUnitNumber);//unit number
        inputVector.add(new Boolean(isParent));
        String connectTo = CoeusGuiConstants.CONNECTION_URL + HIERARCHY_SERVLET;
        
        RequesterBean request = new RequesterBean();
        request.setFunctionType(HIERARCHY_VALIDATION_CHECKS);
        request.setDataObjects(inputVector);
        AppletServletCommunicator comm = new AppletServletCommunicator( connectTo, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response!=null && response.isSuccessfulResponse()) {
            HashMap hmResult = (HashMap)response.getDataObject();
            if (hmResult != null && !hmResult.isEmpty()){
                edu.mit.coeus.routing.gui.RoutingValidationChecksForm valChecks =
                        new edu.mit.coeus.routing.gui.RoutingValidationChecksForm(mdiForm,hmResult,3, proposalNumber );
                valChecks.display();
                if(valChecks.isError()){
                    errType = VALIDATION_ERROR;
                }else{
                    errType = VALIDATION_WARNING;
                }
            }
        }
        return errType;
    }
    //COEUSDEV-197,198: End
// Added for Case COEUSDEV-423 / COEUSQA-2404 : Prop Dev - Error saving budget after adjust period boundaries -Start
    public Vector getVecDeletedPeriods() {
        return vecDeletedPeriods;
    }

   public void setVecDeletedPeriods(CoeusVector vecDeletedPeriods) {
        this.vecDeletedPeriods = vecDeletedPeriods;
    }
// Added for Case COEUSDEV-423 / COEUSQA-2404 : Prop Dev - Error saving budget after adjust period boundaries   -End
 //Added for COEUSDEV-419 / COEUSQA-2402  Prop dev - generate all periods not copying personnel lines when periods > 12 months -Start
    /**
     * This method is to used to calculate the End Date of the person. 
     * If Person Start Date is same as Period Start Date and End Date is different.
     * Then End Date should be calculate End Date = (Start Date + No.of Months ( If less than or equal to period 2 enddate Else apply Period 2 End Date))
     * @param periodStartDate
     * @param personOldStartDate
     * @param personOldEndDate
     * @return Date (valid end Date)
     */
    private Date getPersonEndDateforPeriod(Date periodStartDate, Date personOldStartDate, Date personOldEndDate, Date personNewStartDate){
        
          //Get the Number of days difference for Orginal Person Start and End date.
        int personNoOfDaysDiff = dtUtils.calculateDateDiff(5, personOldStartDate,personOldEndDate ) ;
        
        // Added for COEUSQA-3038 -CLONE - Leap Year Personnel Budget Details Window -Start
        // Check the period starting year has next to the Leap Year, if so then reduce one day less.
        if(isPeriodYearNextToLeapYear(periodStartDate, personNoOfDaysDiff, personOldEndDate, personOldStartDate)){
            personNoOfDaysDiff = personNoOfDaysDiff-1;
        }
        //Check the Period Year is Leap Year, If it is Leap Year add one more day to the total person days.
        if(isLeapYearTobeConsider(periodStartDate, personNoOfDaysDiff, personNewStartDate )){
            personNoOfDaysDiff = personNoOfDaysDiff+1;
        }
        
        // Added for COEUSQA-3038 - End
         //Adding the Start Date and No.of person days to the person end date.
         Date perEndDate = dtUtils.dateAdd(Calendar.DATE, periodStartDate, personNoOfDaysDiff);
        return perEndDate;
    }
    //Added for COEUSDEV-419 / COEUSQA-2402  Prop dev - generate all periods not copying personnel lines when periods > 12 months -End
   
    // Added for COEUSQA-3038 -CLONE - Leap Year Personnel Budget Details Window -Start
    /**
     * This method to check whether it is a leap year or not.
     * @param periodStartDate
     * @return boolean (if it is leap year, return true)
     */
    private boolean isLeapYear(Date periodStartDate){
        Calendar prdCalendar = Calendar.getInstance();
        GregorianCalendar cal = new GregorianCalendar();
        boolean isLeapYear = false;
        prdCalendar.setTime(periodStartDate);
        int periodYear = prdCalendar.get(Calendar.YEAR);
        if (cal.isLeapYear(periodYear)){
            isLeapYear = true;
        }
        return isLeapYear;
    }
    
    /**
     * This method is to used to check whether the PersonEndDate has to be consider for the leap year or not.
     * @param periodStartDate
     * @param personNoOfDaysDiff
     * @return boolean (if leap year has to be consider, return true)
     */
    private boolean isLeapYearTobeConsider(Date periodStartDate, int personNoOfDaysDiff, Date personNewStartDate){
        Calendar stDateCalendar = Calendar.getInstance();
        Calendar personStartDateCalendar = Calendar.getInstance();
        Date perEndDate = dtUtils.dateAdd(Calendar.DATE, periodStartDate, personNoOfDaysDiff);
        boolean isLeepYearTobeConsider = false;
        //check is period start date come under the leap year or not
        
        stDateCalendar.setTime(perEndDate);
        int perEndDateDay = stDateCalendar.get(Calendar.DAY_OF_MONTH);
        int perEndDateMonth = stDateCalendar.get(Calendar.MONTH);
        
        personStartDateCalendar.setTime(personNewStartDate);
        int perStartDateMonth = personStartDateCalendar.get(Calendar.MONTH);
        
        if(isLeapYear(personNewStartDate)){
            //If PersonStartDate Month is greater than March(2), Leep year should not be consider.
            if(perStartDateMonth <= 1  ){
                isLeepYearTobeConsider = true;
            }else{
              isLeepYearTobeConsider = false;  
            }
            if(isLeapYear(perEndDate)){
                if(perEndDateMonth >= 2 && perStartDateMonth <= 1 ){
                    isLeepYearTobeConsider = true;
                }//If PersonEndDate Month is February(1) and Day is more than 28,then Leep year has to be consider.
                else if(perEndDateMonth == 1 & perEndDateDay > 28){
                    isLeepYearTobeConsider = true;
                }else{
                isLeepYearTobeConsider = false;
            } 
            }           
        }else if(isLeapYear(perEndDate)){
            // If PersonEndDate Month is greater than March(2), Leep year has to be consider.
            if(perEndDateMonth >= 2){
                isLeepYearTobeConsider = true;
            }//If PersonEndDate Month is February(1) and Day is more than 28,then Leep year has to be consider.
            else if(perEndDateMonth == 1 & perEndDateDay > 28){
                isLeepYearTobeConsider = true;
            }
        }
                   

        return isLeepYearTobeConsider;
    }
    
    /**
     * This method to check whether this period year is next year to the leap year.
     * @param periodStartDate
     * @param personNoOfDaysDiff
     * @return boolean (if it is next year to leap year, return true)
     */
    private boolean isPeriodYearNextToLeapYear(Date periodStartDate , int personNoOfDaysDiff, Date personOldEndDate, Date personOldStartDate){
        
        Calendar personEndDateCalendar = Calendar.getInstance();
        Calendar personStartDateCalendar = Calendar.getInstance();
        GregorianCalendar cal = new GregorianCalendar();
        Calendar stDateCalendar = Calendar.getInstance();
        boolean isLeapYear = false;
        
        Date perEndDate = dtUtils.dateAdd(Calendar.DATE, periodStartDate, personNoOfDaysDiff);
        
        personEndDateCalendar.setTime(personOldEndDate);
        personStartDateCalendar.setTime(personOldStartDate);
        
        int personPreviousEndYear = personEndDateCalendar.get(Calendar.YEAR);
        int personPreviousEndMonth = personEndDateCalendar.get(Calendar.MONTH);
        int personPreviousEndDay = personEndDateCalendar.get(Calendar.DAY_OF_MONTH);
        
        int personPreviousStartYear = personStartDateCalendar.get(Calendar.YEAR);
        int personPreviousStartMonth = personStartDateCalendar.get(Calendar.MONTH);
        int personPreviousStartDay = personStartDateCalendar.get(Calendar.DAY_OF_MONTH);
        
        stDateCalendar.setTime(perEndDate);
        int perEndDateDay = stDateCalendar.get(Calendar.DAY_OF_MONTH);
        int perEndDateMonth = stDateCalendar.get(Calendar.MONTH);
        
        
        if(isLeapYear(personOldStartDate)){
            //If PersonStartDate Month is greater than March(2), Leep year should not be consider.
            if(personPreviousStartMonth <= 1  ){
                isLeapYear = true;
            }else{
              isLeapYear = false;  
            }
            if(isLeapYear(personOldEndDate)){
                if(personPreviousEndMonth >= 2 && personPreviousStartMonth <= 1){
                    isLeapYear = true;
                }//If PersonEndDate Month is February(1) and Day is more than 28,then Leep year has to be consider.
                else if(personPreviousEndMonth == 1 & personPreviousEndDay > 28){
                    isLeapYear = true;
                }else{
                isLeapYear = false;
            } 
            }           
        }else if(isLeapYear(personOldEndDate)){
            // If PersonEndDate Month is greater than March(2), Leep year has to be consider.
            if(personPreviousEndMonth >= 2){
                isLeapYear = true;
            }//If PersonEndDate Month is February(1) and Day is more than 28,then Leep year has to be consider.
            else if(personPreviousEndMonth == 1 & personPreviousEndDay > 28){
                isLeapYear = true;
            }
        }        
        
  
        return isLeapYear;
    }
    // Added for COEUSQA-3038 - End
    
    // Added for COEUSDEV-794 : End of the month period date not recognized when the first period is on a Leap year -Start
    
    /**
     * This method will check, The person date has to be considered for leap year or not.
     * @param personDate
     * @return boolean 
     */
    private boolean isDateLeapYear(Date personDate){
        Calendar stDateCalendar = Calendar.getInstance();
        boolean isLeepYearTobeConsider = false;
        stDateCalendar.setTime(personDate);
        int personDateDay = stDateCalendar.get(Calendar.DAY_OF_MONTH);
        int personDateMonth = stDateCalendar.get(Calendar.MONTH);
        if(isLeapYear(personDate)){
         //If PersonEndDate Month is February(1) and Day is more than 28,then Leep year has to be consider.
            if(personDateMonth == 1 & personDateDay > 28){
                isLeepYearTobeConsider = true;
            }
        }
        return isLeepYearTobeConsider;
    }
    /**
     * This method will reduce one day from the person date
     * @param personDate
     * @return Date 
     */
    private Date adjustDateForLeapYear(Date personDate){
        personDate = dtUtils.dateAdd(Calendar.DATE, personDate, -1);
        return personDate;
    }

    //Added for COEUSDEV-794 - End
    
    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
    /**
     * Method to re-order the line item sequence
     * @param cvPeriodLineItemDetails
     * @return cvPeriodLineItemDetails
     */
    private CoeusVector resetOrderOfLineItemSequence(CoeusVector cvPeriodLineItemDetails){
        if(cvPeriodLineItemDetails != null && !cvPeriodLineItemDetails.isEmpty()){
            for(int lineItemIndex = 0;lineItemIndex<cvPeriodLineItemDetails.size();lineItemIndex++){
                BudgetDetailBean budgetDetailBean = (BudgetDetailBean)cvPeriodLineItemDetails.get(lineItemIndex);
                budgetDetailBean.setLineItemSequence(lineItemIndex+1);
            }
    }
        return cvPeriodLineItemDetails;
    }
    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start

    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
    /**
     * This method displays the list of Inactive cost elements
     */
    public void displayCENotAvailableMessages() {
        CostElementMessageForm costElementMessageForm = new CostElementMessageForm();
        costElementMessageForm.setFormData(vecCEMessages);
        costElementMessageForm.display();
    }
    
    /**
     * This method gives coset element details
     * @param cvBudgetDetails
     * @return vecCostElements
     */
    private Vector getCostElementsInUI(CoeusVector cvBudgetDetails){
        Vector vecCostElements = new Vector();
        BudgetDetailBean budgetDetailBean = new BudgetDetailBean();
        if (cvBudgetDetails != null && cvBudgetDetails.size() > 0) {
            int size = cvBudgetDetails.size();
            for(int index = 0; index < size; index++) {
                budgetDetailBean = (BudgetDetailBean) cvBudgetDetails.get(index);
                if(!TypeConstants.DELETE_RECORD.equals(budgetDetailBean.getAcType())){
                    vecCostElements.add(budgetDetailBean.getCostElement());
                }
            }
        }
        //remove duplicate cost elements from the vector vecCostElements
        for(int index=0; index<vecCostElements.size(); index++) {
            //costElementIndex Returns the index of the last occurrence of the specified object from the vector vecCostElements.
            int costElementIndex = vecCostElements.lastIndexOf(vecCostElements.get(index));
            //if both index and costElementIndex holding the same value then remove the costelement form the vector
            if(costElementIndex != index) {
                vecCostElements.remove(costElementIndex);
                index=index-1;
            }
        }
        return   vecCostElements;
    }
    
    /**
     * Method to fetch the cost element status from server
     * @param vecCostElements
     * @ returns boolean value
     */
    private boolean getCostElementDetails(Vector vecCostElements) {
        final String connectTo = CoeusGuiConstants.CONNECTION_URL+ SERVLET;
        RequesterBean requester = new RequesterBean();
        ResponderBean responder = null;
        int selection;
        String message;
        MessageFormat formatter = new MessageFormat("");
        vecCEMessages = new Vector();
           
        requester.setFunctionType(GET_INACTIVE_COST_ELEMENTS);
        requester.setDataObjects(vecCostElements);
        Vector inActive = new Vector();
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            inActive =(Vector) responder.getDataObjects();
        }
        //vector inActive holds inactive elements.
        if(inActive!= null && inActive.size() >0){
            for(int index=0; index<inActive.size();index++){
                String costElement = (String) inActive.get(index);
                String costElementDesc = (String) inActive.get(++index);
                message = formatter.format(coeusMessageResources.parseMessageKey("budgetSelect_exceptionCode.1062"),costElement,costElementDesc);
                vecCEMessages.add(message);
            }
            return true;
        }
        return false;
    }
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
    
    
    //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
    /**
     * Method to fetch the proposal details and Inactive Types
     * @param proposalNumber
     * @ returns boolean value
     */
    private boolean getBudgetInfoForProp(Vector appTypeData,Vector periodTypeData){
        Set inactivePeriodType = new HashSet();
        Set inactiveAppointmentType = new HashSet();
        String message;
        MessageFormat formatter = new MessageFormat("");
        Vector dataObject = new Vector();
        boolean finalFlag = true;
        boolean periodFlag = false;
        boolean appointmentTypeFlag = false;
        final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL + "/BudgetMaintenanceServlet";
        RequesterBean requester = new RequesterBean();
        dataObject.addElement(appTypeData);
        dataObject.addElement(periodTypeData);
        requester.setFunctionType(GET_INACTIVE_APPOINT_AND_PERIOD_TYPES);
        requester.setDataObjects(dataObject);
        edu.mit.coeus.utils.AppletServletCommunicator comm
                = new edu.mit.coeus.utils.AppletServletCommunicator(CONNECTION_STRING, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!= null && response.isSuccessfulResponse() ){
            inactivePeriodType = (Set)response.getDataObjects().get(0);
            inactiveAppointmentType = (Set)response.getDataObjects().get(1);
        }
        
        //It holds the inactive periodtypes 
        if(!inactivePeriodType.isEmpty()){
            Iterator iterator = inactivePeriodType.iterator();
            while(iterator.hasNext()){
                String periodType = (String)iterator.next();
                message = formatter.format(coeusMessageResources.parseMessageKey("budgetSelect_exceptionCode.1071"),periodType);
                vecCEMessages.add(message);
            }
            //if there are any inactive Period types then set the flag periodFlag as true
            periodFlag = true;
        }
        //It holds the inactive appointtype types
        if(!inactiveAppointmentType.isEmpty()){
            Iterator iterator = inactiveAppointmentType.iterator();
            while(iterator.hasNext()){
                String appointmentType = (String)iterator.next();
                message = formatter.format(coeusMessageResources.parseMessageKey("budgetSelect_exceptionCode.1072"),appointmentType);
                vecCEMessages.add(message);
            }
            //if there are any inactive Appointment types then set the flag appointmentTypeFlag as true
            appointmentTypeFlag = true;
        }
        //if there are inactive elements then it returns the the value as true
        if(periodFlag || appointmentTypeFlag ){
            return true;
        }
        return  false;
    }
    /**
     * Method to fetch the Inactive Appointment Type and Period Type from the server
     * @param proposalNumber
     * @ returns boolean value
     */
    private int getInactiveAppPerTypForPeriod(String proposalNumber,int VersionNumber,int budgetperiodNumber){
        Vector dataObject = new Vector();
        int inactiveType = 0;
        final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL + "/BudgetMaintenanceServlet";
        RequesterBean requester = new RequesterBean();
        dataObject.addElement(proposalNumber);
        dataObject.addElement(VersionNumber);
        dataObject.addElement(budgetperiodNumber);
        requester.setFunctionType(GET_APP_AND_PER_TYPES_FOR_BUDGET_COPY);
        requester.setDataObjects(dataObject);
        edu.mit.coeus.utils.AppletServletCommunicator comm
                = new edu.mit.coeus.utils.AppletServletCommunicator(CONNECTION_STRING, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!= null && response.isSuccessfulResponse() ){
            inactiveType = (Integer)response.getDataObject();
        }
        return inactiveType;
    }
    
    /**
     * Method to display the warning messages for inactive types
     * @param proposalNumber
     * @ returns boolean value
     */
    private boolean getWarningMsg(int inactive_Type){
        boolean allow_copy = false;
        if(inactive_Type == 0){
            allow_copy = true;
        }else{
            String msgKey = "budgetSelect_exceptionCode."+inactive_Type;
            int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(msgKey),
                    CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
            //if user selects yes then allows to copy the budget
            if(selection ==CoeusOptionPane.SELECTION_YES){
                allow_copy = true;
            } else if(selection ==CoeusOptionPane.SELECTION_NO){
                allow_copy = false;
            }
        }
        return allow_copy;
    }          
    //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end
    
    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
    /**
     * Method to get unit formulated types from unit hierarchy based on the proposal lead unit
     * @return cvUnitFormulatedType - CoeusVector
     */
    private CoeusVector getUnitFormulatedTypesFromServer() {
        CoeusVector cvUnitFormulatedType = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + BudgetBaseWindowController.SERVLET;
        RequesterBean request = new RequesterBean();
        request.setId(budgetInfoBean.getProposalNumber());
        request.setFunctionType(FormulatedCostBudgetLineItemController.GET_UNIT_FORMUALTED_TYPES);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()) {
            cvUnitFormulatedType =  (CoeusVector)response.getDataObject();
        }
        return cvUnitFormulatedType;
    }
    
    /**
     * Method to sync the formulated line item formulated cost in all periods with the unit formulated cost based on the formulated types 
     * Calculate line item cost for the formualted cost based on the new cost and calculated the budget for all periods
     * @throws edu.mit.coeus.exception.CoeusException 
     */
    private void syncCalculatedLineItemcosts() throws CoeusException{
       CoeusVector cvUnitFormulatedCost = getUnitFormulatedTypesFromServer();
        // When formulated cost exists in the unit hierarchy based on the proposa lead unit, 
        // if not travers to the hierarchy and get the first available formualted cost, sync will start only when formulated exists in the unit hierarchy
        if(cvUnitFormulatedCost != null && !cvUnitFormulatedCost.isEmpty()){
            CoeusVector cvLineItemFormulatedCost = queryEngine.getDetails(queryKey, BudgetFormulatedCostDetailsBean.class);
            Equals eqProposalNumber = new Equals("proposalNumber",budgetInfoBean.getProposalNumber());
            Equals eqBudgetVersion = new Equals("versionNumber",budgetInfoBean.getVersionNumber());
            And andBudget = new And(eqProposalNumber,eqBudgetVersion);
            // sync will start further only when formulated cost details available for the budget version
            if(cvLineItemFormulatedCost != null && !cvLineItemFormulatedCost.isEmpty()){
                for(Object formulatedCostDetails : cvUnitFormulatedCost){
                    UnitFormulatedCostBean unitFormulatedCostBean = (UnitFormulatedCostBean)formulatedCostDetails;
                    int formualtedCode = unitFormulatedCostBean.getFormulatedCode();
                    // Formulated unit cost will be updated only to the active formulated cost in the budget version for all the periods
                    Equals eqFormualtedCode = new Equals("formulatedCode",formualtedCode);
                    NotEquals ntDelete = new NotEquals("acType",TypeConstants.DELETE_RECORD);
                    And andActiveFormualteCost = new And(eqFormualtedCode,CoeusVector.FILTER_ACTIVE_BEANS);
                    And andFilterCondition = new And(andBudget,andActiveFormualteCost);
                    double unitCost = unitFormulatedCostBean.getUnitCost();
                    CoeusVector cvFilterFormCost = cvLineItemFormulatedCost.filter(andFilterCondition);
                    // will upadte the unit cost from the unit formulated cost to all the formulated types in the budget version and update to the queryEngine
                    // Set the unitCost and recalculate the Calculated Expenses for each and every budget formulated cost
                    if(cvFilterFormCost != null && !cvFilterFormCost.isEmpty()){
                        for(Object formulCostDetails : cvFilterFormCost){
                            BudgetFormulatedCostDetailsBean budgetFormulatedCostDetailsBean = (BudgetFormulatedCostDetailsBean)formulCostDetails;
                            budgetFormulatedCostDetailsBean.setUnitCost(unitCost);
                            double calculatedExpenses = budgetFormulatedCostDetailsBean.getUnitCost() *
                                    budgetFormulatedCostDetailsBean.getCount() * budgetFormulatedCostDetailsBean.getFrequency();
                            budgetFormulatedCostDetailsBean.setCalculatedExpenses(calculatedExpenses);
                            queryEngine.update(queryKey,budgetFormulatedCostDetailsBean);
                        }
                    }
                }
                if(vecBudgetPeriodController != null && !vecBudgetPeriodController.isEmpty()){
                    int periodCount = vecBudgetPeriodController.size();
                    for(int period = 1; period < periodCount+1;period++){
                        Equals eqBudgetPeriod = new Equals("budgetPeriod", period);
                        And andBudgetPeriod = new And(andBudget,eqBudgetPeriod);
                        CoeusVector cvFormCostDetails = queryEngine.executeQuery(queryKey,BudgetFormulatedCostDetailsBean.class,andBudgetPeriod);
                        if(cvFormCostDetails != null && !cvFormCostDetails.isEmpty()){
                            // HashMap object key will contain line item number for the specific period
                            // and value will be the BudgetFormulatedCostDetailsBean for the period
                            HashMap hmLineItem = new HashMap();
                            for(Object formCostDetails : cvFormCostDetails){
                                BudgetFormulatedCostDetailsBean budgetFormulatedCostDetailsBean = (BudgetFormulatedCostDetailsBean)formCostDetails;
                                Integer lineItemNumber = new Integer(budgetFormulatedCostDetailsBean.getLineItemNumber());
                                double unitCost = budgetFormulatedCostDetailsBean.getUnitCost();
                                if(hmLineItem.get(lineItemNumber) != null){
                                    // Sum up the Calculated cost for the line item number
                                    double unitCostInMap = ((Double)hmLineItem.get(lineItemNumber)).doubleValue();
                                    double sumOfUnitCost = unitCostInMap + budgetFormulatedCostDetailsBean.getCalculatedExpenses();
                                    hmLineItem.put(lineItemNumber,sumOfUnitCost);
                                }else{
                                    hmLineItem.put(lineItemNumber,budgetFormulatedCostDetailsBean.getCalculatedExpenses());
                                }
                            }
                            Iterator itLineItem = hmLineItem.keySet().iterator();
                            // Iterate line item and apply the sum of the calculated cost to the line item cost and update the BudgetDetailBean to the query engine
                            while(itLineItem.hasNext()){
                                Integer lineItem = (Integer)itLineItem.next();
                                double unitCost = ((Double)hmLineItem.get(lineItem)).doubleValue();
                                Equals eqLineItem = new Equals("lineItemNumber", lineItem.intValue());
                                And andBudgetDetailCondition = new And(andBudgetPeriod,eqLineItem);
                                queryEngine.setUpdate(queryKey,BudgetDetailBean.class,"lineItemCost",DataType.getClass(DataType.DOUBLE),unitCost,andBudgetDetailCondition);
                            }
                        }
                    }
                }
                // Will calculated the entire budget, only when formulated cost details are synced
                calculateBudget(true);
            }
        }
    }
    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
}
