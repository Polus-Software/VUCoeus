/*
 * BudgetBaseAction.java
 *
 * Created on March 6, 2006, 3:52 PM
 *
 * PMD check performed, and commented unused imports and variables on 15-FEB-2011
 * by Maharaja Palanichamy
 *
 */
package edu.wmc.coeuslite.budget.action;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.budget.bean.BudgetCategoryBean;
import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.budget.bean.BudgetDetailBean;
import edu.mit.coeus.budget.bean.BudgetDetailCalAmountsBean;
import edu.mit.coeus.budget.bean.BudgetFormulatedCostDetailsBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetJustificationBean;
import edu.mit.coeus.budget.bean.BudgetModularBean;
import edu.mit.coeus.budget.bean.BudgetModularIDCBean;
import edu.mit.coeus.budget.bean.BudgetPeriodBean;
import edu.mit.coeus.budget.bean.BudgetPersonnelCalAmountsBean;
import edu.mit.coeus.budget.bean.BudgetPersonnelDetailsBean;
import edu.mit.coeus.budget.bean.BudgetPersonsBean;
import edu.mit.coeus.budget.bean.BudgetUpdateTxnBean;
import edu.mit.coeus.budget.bean.CostElementsBean;
import edu.mit.coeus.budget.bean.InstituteLARatesBean;
import edu.mit.coeus.budget.bean.InstituteRatesBean;
import edu.mit.coeus.budget.bean.ProjectIncomeBean;
import edu.mit.coeus.budget.bean.ProposalCostSharingBean;
import edu.mit.coeus.budget.bean.ProposalIDCRateBean;
import edu.mit.coeus.budget.bean.ProposalLARatesBean;
import edu.mit.coeus.budget.bean.ProposalRatesBean;
import edu.mit.coeus.budget.bean.RateClassBean;
import edu.mit.coeus.budget.bean.RatesBean;
import edu.mit.coeus.budget.bean.ValidCERateTypesBean;
import edu.mit.coeus.budget.calculator.BudgetCalculator;
import edu.mit.coeus.budget.calculator.bean.ValidCalcTypesBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalHierarchyTxnBean;
import edu.mit.coeus.rates.bean.RatesTxnBean;
import edu.mit.coeus.search.exception.CoeusSearchException;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeuslite.utils.CoeusBaseAction;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import edu.mit.coeus.utils.RateClassTypeConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.utils.query.Or;
import javax.servlet.http.HttpSession;
import edu.mit.coeus.utils.query.GreaterThan;
import edu.mit.coeus.utils.query.LesserThan;
import edu.mit.coeuslite.utils.CoeusDynaBeansList;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.wmc.coeuslite.budget.bean.CategoryBean;
//import edu.wmc.coeuslite.budget.bean.ProposalBudgetHeaderBean;
import edu.wmc.coeuslite.budget.bean.ReadXMLData;
import edu.wmc.coeuslite.utils.SyncBudgetPersons;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;


/**
 *
 * @author  chandrashekara
 */
public abstract class BudgetBaseAction  extends CoeusBaseAction{
    
    private static final String COMPLETE = "C";
    private static final String INCOMPLETE = "I";
    private static final String NONE = "N";
    public static final String EMPTY_STRING = "";
    public static final String VAL_COMPLETE = "Complete";
    public static final String VAL_INCOMPLETE = "Incomplete";
    public static final String VAL_NONE = "None";
    private static final String VIEW_ANY_PROPOSAL = "VIEW_ANY_PROPOSAL";
    private static final String MODIFY_ANY_PROPOSAL = "MODIFY_ANY_PROPOSAL";
    private static final String VIEW_BUDGET = "VIEW_BUDGET";
    private static final String MODIFY_BUDGET  = "MODIFY_BUDGET";
    private static final String BUDGET_MENU_ITEMS ="budgetMenuItemsVector";
    private static final String XML_PATH = "/edu/wmc/coeuslite/budget/xml/BudgetMenu.xml";
    private static final String MULTIPLE_APPOINTMENTS  = "multipleAppointments";
    private static final String SYNC_PERSONS ="syncPersons";
//    private static final String GET_BUDGET_SYNC_PERSON  ="/getBudgetSyncPerson";
    
    //Constants for calculation
    public static final int TOTAL_DIRECT_COST = 1;
    public static final int TOTAL_INDIRECT_COST = 2;
    public static final int COST_SHARING_AMOUNT = 3;
    public static final int UNDERRECOVERY = 4;
    protected static final String ON_CAMPUS_FLAG = "N";
    protected static final String OFF_CAMPUS_FLAG = "F";
  // Case id# 2924 - start (construct combox drop down for on/off Campus flag
    public static final String ON_CAMPUS = "Y";
    public static final String OFF_CAMPUS = "N";
    public static final String VAL_ON_CAMPUS = "On";
    public static final String VAL_OFF_CAMPUS = "Off"; 
  // Case id# 2924 - end
//    private Vector vecBudgetStatus;
//    private ComboBoxBean cmbBudgetStatus;
    //Commented for removing instance variable - start
//    private Vector vecCampusFlag;           // Case id# 2924
//    private ComboBoxBean cmbCampusFlag;     // Case id# 2924
      //Commented for removing instance variable - end
    // Uncommented for Case# 3854: Warning in Lite when salary effective date not in place for a calculation
    private Vector vecMessages;
//    // private ActionMessages actionMessages;
//    private int budgetPeriodNumber;
    private static final String GENERATE_PERIOD_MENU_ID = "B015";
    
    /**
     * instance of Queryengine
     */
    // commented for removing instance variable - case # 2960 - start
//    protected QueryEngine queryEngine;
    // commented for removing instance variable - case # 2960- end
    /**
     * key for getting data from queryEngine
     */
//    protected String queryKey;
    /** set the DEFAULT PERIOD as 1
     */
    protected static final int MAKE_DEFAULT_PERIOD = 1;
    
    //    protected HashMap hmQueryData;
    //    private ActionForward  actionForward = null;
    //
    
    //Commented for case#3654 - Third option 'Default' in the campus dropdown - start
    public static final String DEFAULT_CAMPUS = "D";
    public static final String VAL_DEFAULT_CAMPUS = "Default";  
    //Commented for case#3654 - Third option 'Default' in the campus dropdown - end
    //COEUSQA-1689 Role Restrictions for Budget Rates - Start
    public static final String MODIFY_PROPOSAL_RATES = "MODIFY_PROPOSAL_RATES";
    public static final String MODIFY_ANY_PROPOSAL_RATES = "MODIFY_ANY_PROPOSAL_RATES";
    //COEUSQA-1689 Role Restrictions for Budget Rates - End
    
    /** Creates a new instance of BudgetBaseAction */
    public BudgetBaseAction() {
        // modified for removing instance variable - case # 2960- start
//        vecBudgetStatus = new Vector();
//        cmbBudgetStatus = new ComboBoxBean(INCOMPLETE,VAL_INCOMPLETE);
//        vecBudgetStatus.addElement(cmbBudgetStatus);
//        cmbBudgetStatus = new ComboBoxBean(COMPLETE,VAL_COMPLETE);
//        vecBudgetStatus.addElement(cmbBudgetStatus);
//        cmbBudgetStatus = new ComboBoxBean(NONE,VAL_NONE);
//        vecBudgetStatus.addElement(cmbBudgetStatus);
//        queryEngine = QueryEngine.getInstance();
        //modified for removing instance variable - case # 2960- end
        //Commented for removing instance variable - start
     // Case id# 2924 - start
//        vecCampusFlag = new Vector();
//        cmbCampusFlag = new ComboBoxBean(ON_CAMPUS, VAL_ON_CAMPUS);
//        vecCampusFlag.addElement(cmbCampusFlag);
//        cmbCampusFlag = new ComboBoxBean(OFF_CAMPUS, VAL_OFF_CAMPUS);
//        vecCampusFlag.addElement(cmbCampusFlag);
     // Case id# 2924 - end
        //Commented for removing instance variable - end

    }
    
    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response){
        ActionForward  actionForward = null;
        try{
            prepareLockRelease(request);
            actionForward = performExecute(actionMapping,actionForm,request,response);
            if(actionForward != null) {
                getBudgetMenus(request);
                readSavedStatus(new WebTxnBean(),request);
            }
        }catch(CoeusSearchException coeusSearchException){
            coeusSearchException.printStackTrace();
            UtilFactory.log("FROM COEUS LITE : "+coeusSearchException.getMessage(), coeusSearchException, "BudgetBaseAction", "execute()");
            actionForward =actionMapping.findForward("failure");
            request.setAttribute("Exception", coeusSearchException);
        }
        catch (Exception exception){
            UtilFactory.log("FROM COEUS LITE : "+exception.getMessage(), exception, "BudgetBaseAction", "execute()");
            exception.printStackTrace();
            request.setAttribute("Exception", exception);
            actionForward =actionMapping.findForward("failure");
            //            LockBean lockBean = (LockBean)request.getSession().getAttribute(
            //                CoeusLiteConstants.LOCK_BEAN+request.getSession().getId());
            //            try{
            //                releaseLock(lockBean,request);
            //            }catch (Exception lockException){
            //                lockException.printStackTrace();
            //                request.setAttribute("Exception", lockException);
            //                actionForward =actionMapping.findForward("failure");
            //            }
        }
        return actionForward;
    }
    
    /**
     * Method to perform action
     * @param actionMapping instance of ActionMapping
     * @param actionForm instance of ActionForm
     * @param request instance of Request
     * @param response instance of Response
     * @throws Exception if exception occur
     * @return instance of ActionForward
     */
    public abstract ActionForward performExecute(ActionMapping actionMapping,
    ActionForm actionForm, HttpServletRequest request,
    HttpServletResponse response) throws Exception;
    
    
    /**
     * This method is used in Personnel Budget Line Item  .This method will be used for the Copying the DynaValidatorForm to the
     * BaseBean
     * @param dynaForm instance of DynaValidator
     * @param baseBean instance of base bean
     * @throws Exception if exception occur
     * @return Basebean
     */
    public BaseBean prepareCopyBean(DynaValidatorForm dynaForm, BaseBean baseBean) throws Exception{
        BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
        beanUtilsBean.copyProperties(baseBean, dynaForm);
        return baseBean;
    }
    
    /**
     * Getter for property vecBudgetStatus.
     * @return Value of property vecBudgetStatus.
     */
    public java.util.Vector getVecBudgetStatus() {
        // modified for removing instance variable - case # 2960- start
        Vector vecBudgetStatus = new Vector();
        ComboBoxBean cmbBudgetStatus = new ComboBoxBean(INCOMPLETE,VAL_INCOMPLETE);
        vecBudgetStatus.addElement(cmbBudgetStatus);
        cmbBudgetStatus = new ComboBoxBean(COMPLETE,VAL_COMPLETE);
        vecBudgetStatus.addElement(cmbBudgetStatus);
        cmbBudgetStatus = new ComboBoxBean(NONE,VAL_NONE);
        vecBudgetStatus.addElement(cmbBudgetStatus);
        // modified for removing instance variable - case # 2960- start        
        return vecBudgetStatus;
    }
   // Case id# 2924 - start
    /**
     * Getter for property vecCampusFlag.
     * @return Value of property vecCampusFlag.
     */
    //modified for removing instance variable - start
    public java.util.Vector getVecCampusFlag() {                
        Vector vecCampusFlag = new Vector();
        //Commented for case#3654 - Third option 'Default' in the campus dropdown - start
        ComboBoxBean cmbCampusFlag = new ComboBoxBean(DEFAULT_CAMPUS, VAL_DEFAULT_CAMPUS);    
        vecCampusFlag.addElement(cmbCampusFlag);    
        //Commented for case#3654 - Third option 'Default' in the campus dropdown - end        
        cmbCampusFlag = new ComboBoxBean(ON_CAMPUS, VAL_ON_CAMPUS);
        vecCampusFlag.addElement(cmbCampusFlag);
        cmbCampusFlag = new ComboBoxBean(OFF_CAMPUS, VAL_OFF_CAMPUS);
        vecCampusFlag.addElement(cmbCampusFlag);    
        return vecCampusFlag;
    }
    //modified for removing instance variable - end    
   // Case id# 2924 - end 
    /**
     * Initialize the datas for the Budget Calculator. Get all the data which
     * is required for the calculation. Get all the Budget Reletaed data and then
     * pass it as a Hashtable to the calling calculator
     * @param budgetInfoBean instance of BudgetInfoBean
     * @throws Exception if exception occur
     * @return instance of hashtable
     */
    protected Hashtable initializeBudgetCalculator(BudgetInfoBean budgetInfoBean) throws Exception{
        //queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        Hashtable htBudgetData = getBudgetData(budgetInfoBean);
        return htBudgetData;
    }
    /**
     * Initialize the Budget calculator and set the latest data from the hashtable
     * passed by the calling action. Calculate for the specific period
     * @return hashtable
     * @param data data for calculation
     * @param budgetPeriod budget period
     * @param budgetInfoBean budgetinfobean
     * @throws Exception if exception occur
     */
    protected Hashtable calculatedBudgetPeriod(Hashtable data, int budgetPeriod,
    BudgetInfoBean budgetInfoBean) throws Exception{
        BudgetCalculator budgetCalculator = null;
        //Added for removing instance variable - case # 2960- start
        QueryEngine queryEngine = QueryEngine.getInstance();
        String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        //Added for removing instance variable - case # 2960 - end        
        queryEngine.addDataCollection(queryKey,data);
        //Initialize budget calculator
        budgetCalculator = new BudgetCalculator(budgetInfoBean);
        budgetCalculator.setKey(queryKey);
        // Added for COEUSQA-3044 Added Personnel type Cost Element does not calculate in Lite - start
        //This method is used for to calculate each line item salary for the period.
        budgetCalculator.calculateSalaryForBudgetPeriod(budgetPeriod);
        // Added for COEUSQA-3044 - end
        budgetCalculator.calculatePeriod(budgetPeriod);
        
        
              
        // Uncommented for Case# 3854: Warning in Lite when salary effective date not in place for a calculation
        setVecMessages(budgetCalculator.getVecSalaryMessages());
        Hashtable htData = queryEngine.getDataCollection(queryKey);
        return htData;
    }
    /**
     * Initialize the Budget calculator and set the latest data from the hashtable
     * passed by the calling action. Calculate for all periods.
     * @param data To be calculated
     * @param budgetInfoBean for getting key
     * @throws Exception if any error occurs
     * @return hashtable
     */
    protected Hashtable calculateAllPeriods(Hashtable data,
    BudgetInfoBean budgetInfoBean) throws Exception{
        BudgetCalculator budgetCalculator = null;
        //Added for removing instance variable -case # 2960- start
        QueryEngine queryEngine = QueryEngine.getInstance();
        String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        //Added for removing instance variable - case # 2960- end        
        queryEngine.addDataCollection(queryKey,data);
        //Initialize budget calculator
        budgetCalculator = new BudgetCalculator(budgetInfoBean);
        budgetCalculator.setKey(queryKey);
        budgetCalculator.calculate();
//        setVecMessages(budgetCalculator.getVecMessages());
        Hashtable htData = queryEngine.getDataCollection(queryKey);
        return htData;
    }
    /**
     * Remove the data from QueryEngine and once Calculation is done per
     * request
     * @param queryKey key to delete data from queryengine
     * @throws Exception if exception occur
     */
    protected void removeQueryEngineCollection(String queryKey) throws Exception{
        //Added for removing instance variable - case # 2960 -start
        QueryEngine queryEngine = QueryEngine.getInstance();
        //Added for removing instance variable - case # 2960 - end        
        queryEngine.removeDataCollection(queryKey);
    }
    
    /**
     * Get the budget info details for the selected ProposalNumber and
     * Get the BudgetInfoBean and ProposalDevelopmentFormBean for the
     * given ProposalNumber
     * @return vector containing all budget info
     * @param request request for txn bean
     * @param proposalNumber proposalNumber
     * @throws Exception if exception
     */
    protected CoeusVector getBudgetInfo( HttpServletRequest request ,String proposalNumber ) throws Exception{
        HashMap hmBudgetData = new HashMap();
        hmBudgetData.put("proposalNumber" , proposalNumber );
        WebTxnBean webTxnBean = new WebTxnBean();
        Vector vecBudgetInfo = new Vector();
        CoeusVector cvBudgetForProposal = new CoeusVector();

        try {
        Hashtable htBudgetInfo = (Hashtable)webTxnBean.getResults(request, "getBudgetForProposal" , hmBudgetData );
        Vector vecBudgetForProposal = (Vector)htBudgetInfo.get("getBudgetForProposal");
        if(vecBudgetForProposal!= null && vecBudgetForProposal.size()> 0){
            for(int index = 0 ; index < vecBudgetForProposal.size();index++){
                DynaValidatorForm dynaForm = (DynaValidatorForm)vecBudgetForProposal.elementAt(index);
                BudgetInfoBean budgetInfoBean = new BudgetInfoBean();
                BeanUtilsBean budgetInfoCopyBean = new BeanUtilsBean();
               //   Added for case#3654 - Third option 'Default' in the campus dropdown -Start
                if(dynaForm.get("bsOnOffCampusFlag") !=null){
                    String bsOnOffCampusFlag = (String) dynaForm.get("bsOnOffCampusFlag");
                    if(bsOnOffCampusFlag != null && bsOnOffCampusFlag.equalsIgnoreCase("D")){
                        budgetInfoBean.setDefaultIndicator(true);
                    }
                }
                //   Added for case#3654 - Third option 'Default' in the campus dropdown -End
                budgetInfoCopyBean.copyProperties(budgetInfoBean, dynaForm );
                cvBudgetForProposal.addElement(budgetInfoBean);
            }
            vecBudgetInfo.addElement(cvBudgetForProposal);
        }
        }catch(Exception ex) {
            UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(), ex, "BudgetBaseAction", "getBudgetInfo()");
        }
        return cvBudgetForProposal;
    }
    /**
     *
     * Get the getProposalDetailsForBudget for the
     * given ProposalNumber
     * check user has modify Budger Right
     * check user has view budget right
     * @return vector containing element for rights
     * @param request for txnBean
     * @param dynaProposalDetailForm instance of dynaform
     * @throws Exception if exception
     */
    protected Vector checkBudgetRights(HttpServletRequest request ,
    DynaActionForm dynaProposalDetailForm)throws Exception{
        HttpSession session = request.getSession();
        boolean hasRight= false;
        boolean hasViewRightOnly=false;
        Vector vecBudgetRights = new Vector();
        String proposalNumber = (String)dynaProposalDetailForm.get("proposalNumber");
        String unitNumber = (String)dynaProposalDetailForm.get("unitNumber");
        int statusCode = Integer.parseInt(dynaProposalDetailForm.get("creationStatusCode").toString());
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String loggedinUser = userInfoBean.getUserId();
        hasRight=userMaintDataTxnBean.getUserHasAnyOSPRight(loggedinUser);
        if(hasRight) {
            //COEUSQA-1433 - Allow Recall from Routing - Start
            //if(statusCode != 1 && statusCode != 3) {
            if(statusCode != 1 && statusCode != 3 && statusCode != 8) {
            //COEUSQA-1433 - Allow Recall from Routing - End
                hasViewRightOnly = true;
            }
        }
        hasRight=userMaintDataTxnBean.getUserHasRight(loggedinUser,MODIFY_ANY_PROPOSAL,unitNumber);
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
                hasViewRightOnly =userMaintDataTxnBean.getUserHasRight(loggedinUser,VIEW_ANY_PROPOSAL,unitNumber);
                
                //If not check user has VIEW_BUDGET right
                if(!hasViewRightOnly) {
                    hasViewRightOnly = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, VIEW_BUDGET);
                    //                    if(hasRight) {
                    //                        hasViewRightOnly=true;
                    //                    }
                    //IF user has any OSP right, and the proposal status is (2:Approval In Progress,4: Approved,5: Submitted,
                    //6. Post-Submission Approval or 7. Post-Submission Rejection).
                    if(!hasViewRightOnly) {
                        if(statusCode == 2 || statusCode== 4 || statusCode == 5 || statusCode == 6 || statusCode == 7) {
                            hasViewRightOnly =  userMaintDataTxnBean.getUserHasAnyOSPRight(loggedinUser);
                            //                            if(hasRight) {
                            //                                hasViewRightOnly=true;
                            //                            }
                        }
                    }//End else
                }
            }//End else
        }//End else
        //Added for case#2776 - Allow concurrent Prop dev access in Lite - start        
        if(isProposalInHierarchy(request)){
            if(hasRight){
                hasRight = false;
                hasViewRightOnly = true;
            }            
        }
        //Added for case#2776 - Allow concurrent Prop dev access in Lite - end
        vecBudgetRights.addElement(new Boolean(hasRight));
        vecBudgetRights.addElement(new Boolean(hasViewRightOnly));
        return vecBudgetRights ;
    }
    /**
     * Get all the budget related data from the database while loading the Budget Summary
     * data and keep these values for the calculation itself.
     * @param budgetInfoBean instance of budgetinfobean
     * @throws DBException if exception
     * @throws CoeusException if exception occur
     * @return instance of hashtable containing data
     */
    protected  Hashtable getBudgetData(BudgetInfoBean budgetInfoBean) throws DBException, CoeusException{
        Hashtable budget = new Hashtable();
        
        String proposalNumber = budgetInfoBean.getProposalNumber();
        int versionNumber = budgetInfoBean.getVersionNumber();
        String unitNumber = budgetInfoBean.getUnitNumber();
        int activityTypeCode = budgetInfoBean.getActivityTypeCode();
        
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        
        CoeusVector coeusVector = null;
        try {
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
            //Budget Formulated Cost Detail
            coeusVector = new CoeusVector();
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

            RatesTxnBean ratesTxnBean = new RatesTxnBean();
            String topLevelUnitNum = ratesTxnBean.getTopLevelUnit(unitNumber);
            coeusVector = new CoeusVector();
            coeusVector = budgetDataTxnBean.getInstituteRates(activityTypeCode, topLevelUnitNum);
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
         }catch(Exception ex) {
            UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(), ex, "BudgetBaseAction", "getBudgetData()");
        }
        
        return budget;

    }
    /**
     * Get the Budget Period for the selected tab in the JSP. Get the period and
     * filter for the corresponding details associated with the period
     * @param budgetPeriod period number
     * @param vecEquipmentData vector data contain all data
     * @return instance of vector containing data based on buddget period
     */
    protected Vector filterBudgetPeriod(int budgetPeriod, Vector vecEquipmentData){
        Vector budgetPrData = new Vector();
        if(vecEquipmentData!= null){
            for(int index = 0; index < vecEquipmentData.size() ; index++){
                DynaValidatorForm dynaForm = (DynaValidatorForm)vecEquipmentData.get(index);
                int dynaBudgetPeriod = ((Integer)dynaForm.get("budgetPeriod")).intValue();
                if(dynaBudgetPeriod == budgetPeriod){
                    budgetPrData.addElement(dynaForm);
                }
            }
        }
        return budgetPrData;
    }
    /**
     * Method to get current timestamp
     * @throws Exception if exception occur
     * @return current timestamp
     */
    public Timestamp prepareTimeStamp() throws Exception{
        Timestamp dbTimestamp = null;
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        return dbTimestamp;
    }
    /**This method is used in Personnel Budget Line Item .
     * This helper method will extract the BudgetDetailBean and
     * BudgetDetailCalAmtsBean from the DynaValidatorForm
     * @param cvCalAmts instance of CoeusVector
     * @param budgetPeriod budget period
     * @param lineItemNumber line item number
     * @throws Exception if exception
     * @return hashtable containing data
     */
    protected Hashtable extractItemDetailsDynaToCustomBean(CoeusVector cvCalAmts, int budgetPeriod, int lineItemNumber) throws Exception{
        // Extract the BudgetDetailsBean and BudgetDetailCalAmtsBean
        Hashtable htLineItemDetails = new Hashtable();
        BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = null;
        CoeusVector cvDetailCalAmtsData = new CoeusVector();
        DynaValidatorForm dynaCalAmtsData = null;
        // Extract the BudgetDetailCalAmtsBEan
        if(cvCalAmts!= null && cvCalAmts.size() > 0){
            for(int index=0; index < cvCalAmts.size(); index++){
                dynaCalAmtsData = (DynaValidatorForm)cvCalAmts.get(index);
                budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
                budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)prepareCopyBean(dynaCalAmtsData,budgetDetailCalAmountsBean);
                cvDetailCalAmtsData.addElement(budgetDetailCalAmountsBean);
            }
        }else{
            cvDetailCalAmtsData =  new CoeusVector();
        }
        htLineItemDetails.put(BudgetDetailCalAmountsBean.class,cvDetailCalAmtsData);
        htLineItemDetails = filterLineItemsDetailsForDetails(htLineItemDetails,dynaCalAmtsData,budgetPeriod,lineItemNumber);
        return htLineItemDetails;
    }
    
    /**
     * This method is used in Personnel Budget Line Item .
     * Filter the BudgetDetail and BudgetDetailCalAmts
     * @param htLineItem
     * @param dynaForm
     * @param budgetPeriod
     * @param lineItemNumber
     * @throws Exception
     * @return
     */
    private Hashtable filterLineItemsDetailsForDetails(Hashtable htLineItem,DynaValidatorForm dynaForm, int budgetPeriod, int lineItemNumber) throws Exception{
        Hashtable htLineItemDetails = new Hashtable();
        CoeusVector cvDetailCalAmts = (CoeusVector)htLineItem.get(BudgetDetailCalAmountsBean.class);
        Equals eqBudgetPeriod = null;
        Equals eqLineItem = null;
        CoeusVector cvFilterCalAmtsData = null;
        And eqBudgetPeriodAndeqLineItem = null;
        eqBudgetPeriod = new Equals("budgetPeriod", new Integer(budgetPeriod));
        eqLineItem = new Equals("lineItemNumber", new Integer(lineItemNumber));
        eqBudgetPeriodAndeqLineItem = new And(eqBudgetPeriod,eqLineItem);
        cvFilterCalAmtsData = cvDetailCalAmts.filter(eqBudgetPeriodAndeqLineItem);
        
        htLineItemDetails.put(BudgetDetailCalAmountsBean.class,cvFilterCalAmtsData == null? new CoeusVector():cvFilterCalAmtsData );
        htLineItemDetails = extractItemDetailsCustomToDynaBean(htLineItemDetails, dynaForm);
        return htLineItemDetails;
    }
    /**
     *This method is used in Personnel Budget Line Item .
     * @param htLineItem
     * @param dynaForm
     * @throws Exception
     * @return
     */
    private Hashtable extractItemDetailsCustomToDynaBean(Hashtable htLineItem,DynaValidatorForm dynaForm) throws Exception{
        Hashtable htLineItemDetails = new Hashtable();
        CoeusVector cvCalAmtsData = (CoeusVector)htLineItem.get(BudgetDetailCalAmountsBean.class);
        CoeusVector cvDynaDetailCalAmtsData = new CoeusVector();
        if(cvCalAmtsData!= null){
            for(int calAmtIndex =0; calAmtIndex < cvCalAmtsData.size() ; calAmtIndex++){
                BudgetDetailCalAmountsBean calAmtsBean = (BudgetDetailCalAmountsBean)cvCalAmtsData.get(calAmtIndex);
                BeanUtilsBean calAmtsCopyBean = new BeanUtilsBean();
                DynaBean calAmtDynaForm = null;
                calAmtDynaForm = ((DynaBean)dynaForm).getDynaClass().newInstance();
                calAmtsCopyBean.copyProperties(calAmtDynaForm ,calAmtsBean);
                cvDynaDetailCalAmtsData.addElement(calAmtDynaForm);
            }
        }
        htLineItemDetails.put(BudgetDetailCalAmountsBean.class,cvDynaDetailCalAmtsData == null? new CoeusVector():cvDynaDetailCalAmtsData);
        
        return htLineItemDetails;
    }
    
    /**
     * remove the old BudgetDetailCalAmts data from the passed data object and
     * delete the data for the specific period and line item number
     * @param vecCalAmtsData vector object contain all the cal Amts
     * @param budgetPeriod budget period
     * @param lineItemNumber line item number
     * @param request request
     * @throws Exception if exception
     * @return vector containing newly inserted data
     */
    protected Vector removeOldCalAmtsData(Vector vecCalAmtsData,int budgetPeriod, int lineItemNumber, HttpServletRequest request) throws Exception{
        Vector vecTemp = null;
        if(vecCalAmtsData != null && vecCalAmtsData.size()>0){
            vecTemp = new Vector();
            for(int index = 0 ; index < vecCalAmtsData.size() ; index++){
                DynaValidatorForm dynaCalAmountForm = (DynaValidatorForm)vecCalAmtsData.get(index);
                int dynaBudgetPeriod = ((Integer)dynaCalAmountForm.get("budgetPeriod")).intValue();
                int dynaLineItemNumber = ((Integer)dynaCalAmountForm.get("lineItemNumber")).intValue();
                
                if(dynaBudgetPeriod == budgetPeriod &&
                dynaLineItemNumber != lineItemNumber){
                    vecTemp.add(dynaCalAmountForm);
                }
                //Case 2743 -Start
                //check budgetPeriod, lineItemNumber if both are equals then allow for remove calamounts data 
                else if(dynaBudgetPeriod == budgetPeriod && dynaLineItemNumber == lineItemNumber){
                    try {
                        WebTxnBean webTxnBean = new WebTxnBean();
                        dynaCalAmountForm.set("acType", TypeConstants.DELETE_RECORD);
                        webTxnBean.getResults(request, "updBudgetDetailCalAmts", dynaCalAmountForm);
                    }catch(Exception ex) {
                        UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(), ex, "BudgetBaseAction", "removeOldCalAmtsData()");
                    }
                    
                }//Case 2743 -End
            }//End of for
        }
        return vecTemp == null? new Vector():vecTemp ;
    }//End of removeOldCalAmtsData0
    
    /**
     * Make server call and get the BudgetDetailCalAmts data and then compare with the
     * modified data and then set the approriate acType to the bean
     * @param serverData vector containing data
     * @param budgetPeriod for budget Period
     * @param lineItemNumber line Item number
     * @throws Exception if any error occurs
     * @return boolean for update data
     */
    private boolean checkForServerData(Vector serverData, int budgetPeriod, int lineItemNumber) throws Exception{
        if(serverData!= null && serverData.size() > 0){
            for(int index = 0; index < serverData.size() ; index++){
                DynaValidatorForm dynaForm  = (DynaValidatorForm)serverData.get(index);
                int periodNumber = ((Integer)dynaForm.get("budgetPeriod")).intValue();
                int lineNumber = ((Integer)dynaForm.get("lineItemNumber")).intValue();
                if(budgetPeriod == periodNumber && lineItemNumber ==lineNumber ){
                    return false;//U
                }
            }
            return true;//I
        }else{
            return true;// I
        }
    }
    
    /**This method is used in Personnel Line Item. Later we have to change this method to getCalculatedAmtsData().
     * Get the Calculated BudgetDetailCalAmts data and set the values for the particular line Item
     * @dynaForm instance of DynaValidator form
     * @ returns Vector containing the Dynavalues of the BudgetDetailCalAmts data
     * @param dynaForm instance of DynaValidator form
     * @param request request for txnbean
     * @param budgetInfoBean Bean for Budget Information
     * @param budgetPeriod Budget Period
     * @param lineItemNumber Line Item Number
     * @param costElement Cost Element for Amount
     * @throws Exception if any error occurs
     * @return vector of calculated amount
     */
    protected Vector getCalulatedAmtsData(BudgetInfoBean budgetInfoBean, int budgetPeriod,
    int lineItemNumber, String costElement, DynaValidatorForm dynaForm, HttpServletRequest request) throws Exception{
        CoeusVector cvCalAmts = new CoeusVector();
        //Set Valid CE Rates
        HashMap hmBudgetData = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        try {
            hmBudgetData.put("proposalNumber",budgetInfoBean.getProposalNumber());
            hmBudgetData.put("versionNumber",new Integer(budgetInfoBean.getVersionNumber()));
            Hashtable htBudgetDetails = (Hashtable)webTxnBean.getResults(request,"getBudgetDetailCalAmts",hmBudgetData);
            Vector vecBudgetDetailCalAmts = (Vector)htBudgetDetails.get("getBudgetDetailCalAmts");
            String acType = (String)dynaForm.get("acType");
            boolean status = checkForServerData(vecBudgetDetailCalAmts,budgetPeriod,lineItemNumber);
            if(status){
                acType = TypeConstants.INSERT_RECORD;
            }else{
                acType = TypeConstants.UPDATE_RECORD;
            }
            CoeusVector vecCalCTypes = null;
            BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
            BudgetDataTxnBean budgetDataTxnBean  = new BudgetDataTxnBean();
            CoeusVector vecValidRateTypes= budgetDataTxnBean.getValidCERateTypes(costElement);
            if(vecValidRateTypes == null){
                vecValidRateTypes = new CoeusVector();
            }
            //Check wheather it contains Inflation Rate
            NotEquals nEqInflation = new NotEquals("rateClassType",RateClassTypeConstants.INFLATION);
            Equals eqOH = new Equals("rateClassType", RateClassTypeConstants.OVERHEAD);
            NotEquals nEqOH = new NotEquals("rateClassType", RateClassTypeConstants.OVERHEAD);
            Equals eqBudgetRateClass = new Equals("rateClassCode",new Integer(budgetInfoBean.getOhRateClassCode()));

            And eqOHAndEqBudgetRateClass = new And(eqOH, eqBudgetRateClass);
            Or eqOHAndEqBudgetRateClassOrNEqOH = new Or(eqOHAndEqBudgetRateClass, nEqOH);
            And nEqInflationAndeqOHAndEqBudgetRateClassOrNEqOH = new And(nEqInflation, eqOHAndEqBudgetRateClassOrNEqOH);

            vecValidRateTypes = vecValidRateTypes.filter(nEqInflationAndeqOHAndEqBudgetRateClassOrNEqOH);
            /**
             * Check whether any LA rates are applicable for the home unit, if not
             * filter out all the LA rates
             */
            CoeusVector cvLARates = budgetDataTxnBean.getProposalInstituteLARates(budgetInfoBean.getProposalNumber(),budgetInfoBean.getVersionNumber());
            if (cvLARates == null || cvLARates.size() == 0) {
                NotEquals neqLA = new NotEquals("rateClassType",RateClassTypeConstants.LAB_ALLOCATION);
                NotEquals neqLASal = new NotEquals("rateClassType", RateClassTypeConstants.LA_WITH_EB_VA);
                And laAndLaSal = new And(neqLA, neqLASal);

                vecValidRateTypes = vecValidRateTypes.filter(laAndLaSal);
            }
            //insert to Query Engine
            if(vecValidRateTypes != null && vecValidRateTypes.size() > 0) {
                for(int index = 0; index < vecValidRateTypes.size(); index++) {
                    ValidCERateTypesBean validCERateTypesBean;

                    budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
                    validCERateTypesBean = (ValidCERateTypesBean)vecValidRateTypes.get(index);

                    budgetDetailCalAmountsBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                    budgetDetailCalAmountsBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                    budgetDetailCalAmountsBean.setBudgetPeriod(budgetPeriod);
                    budgetDetailCalAmountsBean.setLineItemNumber(lineItemNumber);
                    budgetDetailCalAmountsBean.setRateClassType(validCERateTypesBean.getRateClassType());
                    budgetDetailCalAmountsBean.setRateClassCode(validCERateTypesBean.getRateClassCode());
                    budgetDetailCalAmountsBean.setRateTypeCode(validCERateTypesBean.getRateTypeCode());
                    budgetDetailCalAmountsBean.setRateClassDescription(validCERateTypesBean.getRateClassDescription());
                    budgetDetailCalAmountsBean.setRateTypeDescription(validCERateTypesBean.getRateTypeDescription());
                    budgetDetailCalAmountsBean.setApplyRateFlag(true);
                    budgetDetailCalAmountsBean.setUpdateTimestamp(prepareTimeStamp());
                    budgetDetailCalAmountsBean.setAcType(acType);
                    cvCalAmts.addElement(budgetDetailCalAmountsBean);
                }
            }

            Equals eqLabAllocSal = new Equals("rateClassType",RateClassTypeConstants.LA_WITH_EB_VA);
            CoeusVector vecLabAllocSal = vecValidRateTypes.filter(eqLabAllocSal);

            if(vecLabAllocSal != null && vecLabAllocSal.size() > 0) {
                //Has Lab allocation and Salaries Entry (i.e Rate Class Type = Y)
                Equals eqE = new Equals("rateClassType",RateClassTypeConstants.EMPLOYEE_BENEFITS);
                Equals eqV = new Equals("rateClassType",RateClassTypeConstants.VACATION);
                vecCalCTypes = budgetDataTxnBean.getValidCalcTypesForEV();
                vecCalCTypes = vecCalCTypes.filter(eqE);
                if (vecCalCTypes.size() > 0) {
                    ValidCalcTypesBean validCalcTypesBean = (ValidCalcTypesBean) vecCalCTypes.get(0);
                    if (validCalcTypesBean.getDependentRateClassType().equals(RateClassTypeConstants.LA_WITH_EB_VA)) {
                        budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
                        budgetDetailCalAmountsBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                        budgetDetailCalAmountsBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                        budgetDetailCalAmountsBean.setBudgetPeriod(budgetPeriod);
                        budgetDetailCalAmountsBean.setLineItemNumber(lineItemNumber);
                        budgetDetailCalAmountsBean.setRateClassType(validCalcTypesBean.getRateClassType());
                        budgetDetailCalAmountsBean.setRateClassCode(validCalcTypesBean.getRateClassCode());
                        budgetDetailCalAmountsBean.setRateTypeCode(validCalcTypesBean.getRateTypeCode());
                        budgetDetailCalAmountsBean.setRateClassDescription(validCalcTypesBean.getRateClassDescription());
                        budgetDetailCalAmountsBean.setRateTypeDescription(validCalcTypesBean.getRateTypeDescription());
                        budgetDetailCalAmountsBean.setApplyRateFlag(true);
                        budgetDetailCalAmountsBean.setUpdateTimestamp(prepareTimeStamp());
                        budgetDetailCalAmountsBean.setAcType(acType);
                        cvCalAmts.addElement(budgetDetailCalAmountsBean);
                    }
                }//End IF Size > 0
                vecCalCTypes = budgetDataTxnBean.getValidCalcTypesForEV();
                vecCalCTypes = vecCalCTypes.filter(eqV);
                if (vecCalCTypes.size() > 0) {
                    ValidCalcTypesBean validCalcTypesBean = (ValidCalcTypesBean) vecCalCTypes.get(0);
                    if (validCalcTypesBean.getDependentRateClassType().equals(RateClassTypeConstants.LA_WITH_EB_VA)) {
                        budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
                        budgetDetailCalAmountsBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                        budgetDetailCalAmountsBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                        budgetDetailCalAmountsBean.setBudgetPeriod(budgetPeriod);
                        budgetDetailCalAmountsBean.setLineItemNumber(lineItemNumber);
                        budgetDetailCalAmountsBean.setRateClassType(validCalcTypesBean.getRateClassType());
                        budgetDetailCalAmountsBean.setRateClassCode(validCalcTypesBean.getRateClassCode());
                        budgetDetailCalAmountsBean.setRateTypeCode(validCalcTypesBean.getRateTypeCode());
                        budgetDetailCalAmountsBean.setRateClassDescription(validCalcTypesBean.getRateClassDescription());
                        budgetDetailCalAmountsBean.setRateTypeDescription(validCalcTypesBean.getRateTypeDescription());
                        budgetDetailCalAmountsBean.setApplyRateFlag(true);
                        budgetDetailCalAmountsBean.setUpdateTimestamp(prepareTimeStamp());
                        budgetDetailCalAmountsBean.setAcType(acType);
                        cvCalAmts.addElement(budgetDetailCalAmountsBean);
                    }
                }//End IF Size > 0

            }//End IF Lab Allocation Salaries
        } catch(Exception ex) {
            UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(), ex, "BudgetBaseAction", "getCalulatedAmtsData()");
        }
        Vector vecCalAmts = getDynaCalAmtsdData(cvCalAmts,dynaForm);
        return vecCalAmts;
    }
    /**
     * Prepare the DynaBean for the BudgetDetailCalAmts data
     * @param cvCalAmts vector conatining Cal Amounts
     * @param dynaBean instance of Dyna Bean
     * @throws Exception If any error occurs
     * @return vector containing DynaCal Amounts
     */
    private Vector getDynaCalAmtsdData(CoeusVector cvCalAmts,
    DynaValidatorForm dynaBean) throws Exception{
        BeanUtilsBean copyBean = null;
        Vector vecCalAmts = null;
        if(cvCalAmts!= null && cvCalAmts.size() > 0){
            vecCalAmts  = new Vector();
            for(int index = 0; index < cvCalAmts.size() ; index++){
                BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)cvCalAmts.get(index);
                copyBean = new BeanUtilsBean();
                DynaBean dynaCalAmtsBean = ((DynaBean)dynaBean).getDynaClass().newInstance();
                
                copyBean.copyProperties(dynaCalAmtsBean,budgetDetailCalAmountsBean);
                vecCalAmts.addElement(dynaCalAmtsBean);
            }
        }
        return vecCalAmts;
    }
    /**
     * Prepare the DynaBean for the BudgetDetailCalAmts data
     * @param cvCalAmts vector conatining Cal Amounts
     * @param dynaBean instance of Dyna Bean
     * @throws Exception If any error occurs
     * @return vector containing DynaCal Amounts
     */
    private Vector getDynaCalAmtsdData(CoeusVector cvCalAmts,
    CoeusDynaBeansList dynaBean, HttpServletRequest request) throws Exception{
        BeanUtilsBean copyBean = null;
        Vector vecCalAmts = null;
        if(cvCalAmts!= null && cvCalAmts.size() > 0){
            vecCalAmts  = new Vector();
            for(int index = 0; index < cvCalAmts.size() ; index++){
                BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)cvCalAmts.get(index);
                copyBean = new BeanUtilsBean();
                // DynaBean dynaCalAmtsBean = ((DynaBean)dynaBean).getDynaClass().newInstance();
                DynaActionForm dynaFormData = dynaBean.getDynaForm(request,"budgetEquipmentData");
                DynaBean dynaCalAmtsBean = ((DynaBean)dynaFormData).getDynaClass().newInstance();
                copyBean.copyProperties(dynaCalAmtsBean,budgetDetailCalAmountsBean);
                vecCalAmts.addElement(dynaCalAmtsBean);
            }
        }
        return vecCalAmts;
    }
    /**
     * Check for Cost Element exist based on Category type selected by the
     * page
     * @param vecFilterData data has to filter
     * @param vecCEData cost element data
     * @throws Exception if exception occur
     * @return vector containing data related to equipment/participant/travel/other debt
     */
    public Vector getExistingCE(Vector vecFilterData, Vector vecCEData) throws Exception{
        //Vector vecCEData = (Vector)session.getAttribute("costElementData");
        Vector vecFilterCE = new Vector();
        DynaValidatorForm dynaForm = null;
        
        if(vecCEData != null){
            if(vecFilterData != null && vecFilterData.size() > 0){
                dynaForm = (DynaValidatorForm)vecFilterData.get(0);
                for(int index = 0; index < vecFilterData.size(); index++){
                    DynaValidatorForm dynaFilterForm = (DynaValidatorForm)vecFilterData.get(index);
                    String dynaCE = (String)dynaFilterForm.get("costElement");
                    for(int ind = 0; ind < vecCEData.size() ; ind++){
                        CategoryBean categoryBean = (CategoryBean)vecCEData.get(ind);
                        String costElement = categoryBean.getCostElement();
                        if(dynaCE.equals(costElement)){
                            vecFilterCE.addElement(dynaFilterForm);
                            break;
                        }
                    }
                }
            }
        }
        //         // for sorting based on line item number so that every time we'll get sorted
        //         //vector based on line item number
        //         CoeusVector cvFilterCostElement = null;
        //         BudgetDetailBean budgetDetailBean = null;
        //         BeanUtilsBean beanUtilsBean = null;
        //         if(vecFilterCE!= null && vecFilterCE.size() > 0){
        //             cvFilterCostElement = new CoeusVector();
        //             for(int periodIndex = 0;periodIndex <vecFilterCE.size(); periodIndex++){
        //                 beanUtilsBean = new BeanUtilsBean();
        //                 budgetDetailBean = new BudgetDetailBean();
        //                 DynaValidatorForm periodDynaBean = (DynaValidatorForm)vecFilterCE.get(periodIndex);
        //                 beanUtilsBean.copyProperties(budgetDetailBean,periodDynaBean);
        //                 cvFilterCostElement.addElement(budgetDetailBean);
        //             }
        //         }
        //         if(cvFilterCostElement != null && cvFilterCostElement.size() > 0){
        //             cvFilterCostElement.sort("lineItemNumber");
        //             vecFilterCE.removeAllElements();
        //             for(int periodIndex = 0;periodIndex <cvFilterCostElement.size(); periodIndex++){
        //                 beanUtilsBean = new BeanUtilsBean();
        //                 budgetDetailBean = (BudgetDetailBean)cvFilterCostElement.get(periodIndex);
        //                 DynaBean dynaBean = ((DynaBean)dynaForm).getDynaClass().newInstance();
        //                 beanUtilsBean.copyProperties(dynaBean,budgetDetailBean);
        //                 vecFilterCE.addElement(dynaBean);
        //             }
        //
        //         }
        return vecFilterCE;
    }
    
    private boolean isPeriodsGenerated(HttpServletRequest request) throws Exception{
        boolean isPeriodsGenerated = false;
        
        HashMap hmBudgetData = new HashMap();
        HttpSession session = request.getSession();
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        hmBudgetData.put("proposalNumber",budgetInfoBean.getProposalNumber());
        hmBudgetData.put("versionNumber",new Integer(budgetInfoBean.getVersionNumber()));
        int value = checkPeriodsGenerated(request,hmBudgetData);
        if(value==0){// Periods are not generated
            session.setAttribute("Generated", new Boolean(false));
        }else if(value==1){// Periods are generated
            isPeriodsGenerated = true;
            session.setAttribute("Generated", new Boolean(true));
        }
        return isPeriodsGenerated;
    }
    
    /** This method will return whether the periods are generated or not.
     *@param request
     *@param HashMap containing the Proposal Number and Version Number
     *@throws Exception
     *If the value is 1, the periods are generated
     *If the value is 0, the periods are not generated
     */
    private int checkPeriodsGenerated(HttpServletRequest request, HashMap hmBudgetData) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
         int value = -1;
        try {
            Hashtable htUpdBudgetStatus =
            (Hashtable)webTxnBean.getResults(request, "isPeriodsGenerated", hmBudgetData);
            HashMap hmData = (HashMap)htUpdBudgetStatus.get("isPeriodsGenerated");
            String strValue = EMPTY_STRING;

            if(hmData!= null){
                strValue = (String)hmData.get("IS_GENERATED");
                value = ((Integer)Integer.valueOf(strValue)).intValue();
            }
        }catch(Exception ex) {
             UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(), ex, "BudgetBaseAction", "checkPeriodsGenerated()");
        }
        return value;
    }
    /**
     * Performs Generate All Periods
     * @param request request for selected menu
     * @throws Exception if any error occurs
     * @return navigator
     */
    protected HashMap generateAllPeriods(HttpServletRequest request)  throws Exception{
        /** Currently validate method is not invoked. In the later release
         *this has to be validated against the budget and then saves to the
         *data base
         */
        //   if(! validate()) return ;
        String navigator = "";
        //Added for removing instance variable -case # 2960 - starts
        HashMap hmNavigator = new HashMap();
        //Added for removing instance variable -case # 2960 - ends        
        HttpSession session = request.getSession();
        String proposalNumber = (String) session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        //Added for removing instance variable -case # 2960 - start
        QueryEngine queryEngine = QueryEngine.getInstance();
        String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        //Added for removing instance variable - case # 2960- end                
        Hashtable htCalBudgetData = initializeBudgetCalculator(budgetInfoBean);
        CoeusVector cvBudgetPeriod = (CoeusVector)htCalBudgetData.get(BudgetPeriodBean.class);
        CoeusVector cvBudgetDetails = (CoeusVector)htCalBudgetData.get(BudgetDetailBean.class);
        CoeusVector vecPeriodData = getActivePeriodData(cvBudgetPeriod,cvBudgetDetails);
        queryEngine.addDataCollection(queryKey, htCalBudgetData);
        CoeusVector vecPeriods = null;
        
        Equals eqNull = new Equals("acType", null);
        
        vecPeriods = queryEngine.getDetails(queryKey, BudgetPeriodBean.class);
        
        
        // Check if the periods are generated or not
        HashMap hmPeriodsData = new HashMap();
        hmPeriodsData.put("proposalNumber", budgetInfoBean.getProposalNumber());
        hmPeriodsData.put("versionNumber",new Integer(budgetInfoBean.getVersionNumber()));
        int value = checkPeriodsGenerated(request,hmPeriodsData);
        if(value ==1){
            navigator = "generated";
            hmNavigator.put("navigator", navigator);
            return hmNavigator;                        
        }
        
        //Checking for Generate Periods
        BudgetPeriodBean lastBudgetPeriodBean;
        
        lastBudgetPeriodBean = new BudgetPeriodBean();
        //        lastBudgetPeriodBean.setBudgetPeriod(vecPeriodData.size());
        lastBudgetPeriodBean.setBudgetPeriod(MAKE_DEFAULT_PERIOD);
        lastBudgetPeriodBean = (BudgetPeriodBean)queryEngine.executeQuery(queryKey, lastBudgetPeriodBean).get(0);
        
        Equals propNo = new Equals("proposalNumber", lastBudgetPeriodBean.getProposalNumber());
        Equals versionNo = new Equals("versionNumber", new Integer(lastBudgetPeriodBean.getVersionNumber()));
        Equals periodNo = new Equals("budgetPeriod", new Integer(lastBudgetPeriodBean.getBudgetPeriod()));
        
        NotEquals notDelete = new NotEquals("acType", TypeConstants.DELETE_RECORD);
        And propVersion = new And(propNo, versionNo);
        Or notDeleteOrEqNull = new Or(notDelete, eqNull);
        And periodNotDeleteOrEqNull = new And(periodNo, notDeleteOrEqNull);
        
        And condition = new And(propVersion, periodNotDeleteOrEqNull);
        
        CoeusVector vecBudgetDetailBean = null;
        
        vecBudgetDetailBean = queryEngine.executeQuery(queryKey, BudgetDetailBean.class, condition);
        if(vecBudgetDetailBean == null || vecBudgetDetailBean.size() == 0) {
            navigator = "lineItemRequired";
            //Modified for removing instance variable -case # 2960 - starts
//            setBudgetPeriodNumber(lastBudgetPeriodBean.getBudgetPeriod());
            hmNavigator.put("navigator", navigator);
            hmNavigator.put("budgetPeriodNumber", new Integer(lastBudgetPeriodBean.getBudgetPeriod()));
            return hmNavigator;
            //Modified for removing instance variable -case # 2960 - ends
//             return navigator;            
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
                newBudgetDetailBean.setLineItemStartDate(newBudgetPeriodBean.getStartDate());
                newBudgetDetailBean.setLineItemEndDate(newBudgetPeriodBean.getEndDate());
                //Setting Based on LIne Item - Start
                newBudgetDetailBean.setBasedOnLineItem(newBudgetDetailBean.getLineItemNumber());
                //Setting Based on LIne Item - End
                
                newBudgetDetailBean.setAcType(TypeConstants.INSERT_RECORD);
                
                BudgetDetailBean prevBudgetDetailBean;
                
                prevBudgetDetailBean = new BudgetDetailBean();
                prevBudgetDetailBean.setBudgetPeriod(newBudgetDetailBean.getBudgetPeriod() - 1);
                prevBudgetDetailBean.setLineItemNumber(newBudgetDetailBean.getLineItemNumber());
                prevBudgetDetailBean = (BudgetDetailBean)queryEngine.executeQuery(queryKey, prevBudgetDetailBean).get(0);
                double lineItemCost = prevBudgetDetailBean.getLineItemCost();
                
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
                
                //Apply inflation only if line item does not contain personnel line item
                if (newBudgetDetailBean.isApplyInRateFlag() && (vecPersonnelLI == null || vecPersonnelLI.size() == 0)) {
                    //Cost Calculation
                    Equals eqCe  = new Equals("costElement", newBudgetDetailBean.getCostElement());
                    Equals eqInflation = new Equals("rateClassType","I");
                    And ceAndInflation = new And(eqCe, eqInflation);
                    //Check for inflation for the Cost Element.Get ValidCERateTypesBean From Server Side.
                    BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
                    CoeusVector vecValidCERateTypes = budgetDataTxnBean.getValidCERateTypes(newBudgetDetailBean.getCostElement());
                    CoeusVector vecCE = null ;
                    if(vecValidCERateTypes!=null && vecValidCERateTypes.size() >0 ){
                        vecCE = vecValidCERateTypes.filter(ceAndInflation);
                    }
                    if(vecCE != null && vecCE.size() > 0) {
                        Date startDate, endDate;
                        startDate = prevBudgetDetailBean.getLineItemStartDate();//newBudgetDetailBean.getLineItemStartDate();
                        endDate = newBudgetDetailBean.getLineItemStartDate();
                        
                        ValidCERateTypesBean validCERateTypesBean  = (ValidCERateTypesBean)vecCE.get(0);
                        
                        Equals eqRC = new Equals("rateClassCode", new Integer(validCERateTypesBean.getRateClassCode()));
                        Equals eqRT = new Equals("rateTypeCode", new Integer(validCERateTypesBean.getRateTypeCode()));
                        
                        GreaterThan gtSD = new GreaterThan("startDate", startDate);
                        LesserThan ltED = new LesserThan("startDate", endDate);
                        Equals eqED = new Equals("startDate", endDate);
                        Or ltEDOrEqED = new Or(ltED, eqED);
                        
                        And ltOrEqEDAndGtSD = new And(ltEDOrEqED, gtSD);
                        And rcAndRt = new And(eqRC, eqRT);
                        And rcAndRtAndLtOrEqEDAndGtSD = new And(rcAndRt, ltOrEqEDAndGtSD);
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
                                                        
                            //ProposalRatesBean proposalRatesBean = (ProposalRatesBean)vecPropInflationRates.get(0);
                            double applicableRate = proposalRatesBean.getApplicableRate();
                            lineItemCost = lineItemCost * (100 + applicableRate) / 100;
                            lineItemCost = (double)Math.round(lineItemCost*Math.pow(10.0, 2) ) / 100;
                        }//End For vecPropInflationRates != null ...
                    }//End If vecCE != null ...
                }//Apply Inflation check ends here
                newBudgetDetailBean.setLineItemCost(lineItemCost);
                queryEngine.insert(queryKey, newBudgetDetailBean);
                
            }//End For Copy Budget Detail Beans
            //Copy Budget Detail Cal Amts Beans
            vecCalAmts = queryEngine.executeQuery(queryKey, BudgetDetailCalAmountsBean.class, condition);
            if(vecCalAmts == null){
                vecCalAmts = new CoeusVector();
            }
            for(int calAmtsIndex = 0; calAmtsIndex < vecCalAmts.size(); calAmtsIndex++) {
                newBudgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)vecCalAmts.get(calAmtsIndex);
                newBudgetDetailCalAmountsBean.setBudgetPeriod(newBudgetPeriodBean.getBudgetPeriod());
                newBudgetDetailCalAmountsBean.setAcType(TypeConstants.INSERT_RECORD);
                queryEngine.insert(queryKey, newBudgetDetailCalAmountsBean);
            }//End For Copy Budget Detail Cal Amts Beans
            //Copy Personnel Detail Beans
            vecBudgetPersonnelDetailsBean = queryEngine.executeQuery(queryKey, BudgetPersonnelDetailsBean.class, condition);
            if(vecBudgetPersonnelDetailsBean == null){
                vecBudgetPersonnelDetailsBean = new CoeusVector();
            }
            // Added for Case 3197 - Allow for the generation of project period greater than 12 months -start                
               BudgetPersonnelDetailsBean personnelDetailsBean = null; 
               Date personOldStartDate = null;
               Date personOldEndDate = null;                      
               // 3197 - end
                // Added for COEUSDEV-419 / COEUSQA-2402 Prop dev - generate all periods not copying personnel lines when periods > 12 months
               Date previousPeriodStartDate = null;
               Date previousPeriodEndDate = null;  
               // COESUDEV-419 / COEUSQA-2402  -End
              
            for(int personelIndex = 0; personelIndex < vecBudgetPersonnelDetailsBean.size(); personelIndex++) {
                newBudgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean)vecBudgetPersonnelDetailsBean.get(personelIndex);
                newBudgetPersonnelDetailsBean.setBudgetPeriod(newBudgetPeriodBean.getBudgetPeriod());
                // Case# 2767:Leap Year, Personnel Budget Details Window - Start
                // Modified for Calculating new Personal Line Item Start DAte using dateAdd() of Date Utils
//                Date pliStartDate = newBudgetPersonnelDetailsBean.getStartDate();
//                Calendar calendar= Calendar.getInstance();
//                calendar.setTime(pliStartDate);
//                calendar.add(Calendar.YEAR, 1);
//                pliStartDate = calendar.getTime();
                 // Added for Case 3197 - Allow for the generation of project period greater than 12 months -start
                Vector vecBudPersonnelDetailsBean = queryEngine.executeQuery(queryKey, BudgetPersonnelDetailsBean.class, condition);
                if(vecBudPersonnelDetailsBean !=null && vecBudPersonnelDetailsBean.size() > personelIndex){
                    personnelDetailsBean = (BudgetPersonnelDetailsBean)vecBudPersonnelDetailsBean.get(personelIndex);
                    personOldStartDate = personnelDetailsBean.getStartDate();
                    personOldEndDate = personnelDetailsBean.getEndDate();
                }
//                Date pliStartDate = DateUtils.dateAdd(Calendar.YEAR, newBudgetPersonnelDetailsBean.getStartDate(), 1);
//                Date pliStartDate =  getValidPersonDateforPeriod(newBudgetPeriodBean.getStartDate(), newBudgetPeriodBean.getEndDate(), newBudgetPersonnelDetailsBean.getStartDate(), null, personOldStartDate, personOldEndDate);
//                Date pliEndDate =  getValidPersonDateforPeriod(newBudgetPeriodBean.getStartDate(), newBudgetPeriodBean.getEndDate(), null ,newBudgetPersonnelDetailsBean.getEndDate(), personOldStartDate, personOldEndDate);
                //3197 - end
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
//                            pliStartDate =  getValidPersonDateforPeriod(newBudgetPeriodBean.getStartDate(), newBudgetPeriodBean.getEndDate(), newBudgetPersonnelDetailsBean.getStartDate(), null, personOldStartDate, personOldEndDate);
//                            pliEndDate =  getValidPersonDateforPeriod(newBudgetPeriodBean.getStartDate(), newBudgetPeriodBean.getEndDate(), null ,newBudgetPersonnelDetailsBean.getEndDate(), personOldStartDate, personOldEndDate);
//
                           //Modified for COEUSQA-3422 Budget Persons Line Item Details END DATES match Budget Period END DATES in 
                           //Leap Years using February start -Start
                           // If Person Start and Period Date does not match, using existing logic.
                           pliStartDate =  getValidPersonDateforPeriod(newBudgetPeriodBean.getStartDate(), newBudgetPeriodBean.getEndDate(), newBudgetPersonnelDetailsBean.getStartDate(), null
                                   , personOldStartDate, personOldEndDate, previousPeriodStartDate , previousPeriodEndDate, null );
                           
                           pliEndDate =  getValidPersonDateforPeriod(newBudgetPeriodBean.getStartDate(), newBudgetPeriodBean.getEndDate(), null ,newBudgetPersonnelDetailsBean.getEndDate()
                           , personOldStartDate, personOldEndDate, previousPeriodStartDate , previousPeriodEndDate, pliStartDate );
                           //Modified for COEUSQA-3422 -End
                           
                           // Modified for COEUSQA-3038 -CLONE - Leap Year Personnel Budget Details Window -end
                            
                         //  pliStartDate =  getValidPersonDateforPeriod(newBudgetPeriodBean.getStartDate(), newBudgetPeriodBean.getEndDate(), personnelDetailsBean.getStartDate(), null, personOldStartDate, personOldEndDate);
                           // Date pliEndDate = dtUtils.dateAdd(Calendar.YEAR, newBudgetPersonnelDetailsBean.getEndDate(), 1);
                          // pliEndDate =  getValidPersonDateforPeriod(newBudgetPeriodBean.getStartDate(), newBudgetPeriodBean.getEndDate(), null ,personnelDetailsBean.getEndDate(), personOldStartDate, personOldEndDate);
                       }// end of inner condition else
                   }// end of outer condition else
                    // COEUSDEV-419 / COEUSQA-2402  Prop dev - generate all periods not copying personnel lines when periods > 12 months -End
                    
                // Case# 2767:Leap Year, Personnel Budget Details Window - End
                //since start date for line item will be same as start date of period.
                //while generating periods. we can take period startt date.
                if(newBudgetPeriodBean.getStartDate().compareTo(pliStartDate) <= 0 && newBudgetPeriodBean.getEndDate().compareTo(pliStartDate) >= 0 && pliEndDate.compareTo(newBudgetPeriodBean.getStartDate()) > 0) {
                    newBudgetPersonnelDetailsBean.setStartDate(new java.sql.Date(pliStartDate.getTime()));
                    //set End Date
                     // Case# 2767:Leap Year, Personnel Budget Details Window - Start
//                    Date pliEndDate = newBudgetPersonnelDetailsBean.getEndDate();
//                    calendar.setTime(pliEndDate);
//                    calendar.add(Calendar.YEAR, 1);
//                    pliEndDate = calendar.getTime();
                    // Added for Case 3197 - Allow for the generation of project period greater than 12 months -start
//                       Date pliEndDate = DateUtils.dateAdd(Calendar.YEAR, newBudgetPersonnelDetailsBean.getEndDate(), 1, true);
//                    Date pliEndDate =  getValidPersonDateforPeriod(newBudgetPeriodBean.getStartDate(), newBudgetPeriodBean.getEndDate(), null ,newBudgetPersonnelDetailsBean.getEndDate(), personOldStartDate, personOldEndDate);
                    // 3197 -end

                    
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
//                    vecBudgetPersonnelCalAmountsBean = queryEngine.executeQuery(queryKey, BudgetPersonnelCalAmountsBean.class, conditionAndLINo);
                        // If same person is added multiple times (for eg. 3 times) in one lineitem, 
                        // During generate all periods out of 3 it has match only 2 items. Cal amount has to update only to 2 items. 
                        // Problem Person Number is not taken care in PersonnelCalAmount updation.
                        And condandLINoAndPersonNum = new And (conditionAndLINo,  
                          new Equals("personNumber", new Integer(newBudgetPersonnelDetailsBean.getPersonNumber())));
                        vecBudgetPersonnelCalAmountsBean = queryEngine.executeQuery(queryKey, BudgetPersonnelCalAmountsBean.class, condandLINoAndPersonNum);
                        // Modified for case COEUSDEV 302 - End
                    if(vecBudgetPersonnelCalAmountsBean == null){
                        vecBudgetPersonnelCalAmountsBean = new CoeusVector();
                    }
                    for(int personnelCalAmtsIndex = 0; personnelCalAmtsIndex < vecBudgetPersonnelCalAmountsBean.size(); personnelCalAmtsIndex++) {
                        newBudgetPersonnelCalAmountsBean = (BudgetPersonnelCalAmountsBean)vecBudgetPersonnelCalAmountsBean.get(personnelCalAmtsIndex);
                        newBudgetPersonnelCalAmountsBean.setBudgetPeriod(newBudgetPeriodBean.getBudgetPeriod());
                        newBudgetPersonnelCalAmountsBean.setAcType(TypeConstants.INSERT_RECORD);
                        queryEngine.insert(queryKey, newBudgetPersonnelCalAmountsBean);
                    }
                }
            }//End For Copy Personnel Detail Beans
               
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

               
        }//End For New Budget Periods
        navigator =  calulateAndSaveBudget(budgetInfoBean,session);
        // Update the proposal hierarchy sync flag
        updateProposalSyncFlags(request, proposalNumber);
        //Modified for removing instance variable -case # 2960 - starts
//        return navigator;
        hmNavigator.put("navigator", navigator);
        return hmNavigator;
        //Modified for removing instance variable -case # 2960 - ends
    }
    /**
     * Get the active period data based on the budgetDetails
     * If there are no line items present then don't consider it as an active period data
     * @returns the active budgetPeriod data
     * @param cvBudgetPeriods vector of budget Periods
     * @param cvBudgetDetails vector of Budget Details
     * @throws Exception if any error occurs
     * @return vector containing active periods
     */
    private CoeusVector getActivePeriodData(CoeusVector cvBudgetPeriods, CoeusVector cvBudgetDetails)
    throws Exception{
        //        GreaterThan gtPeriod = new GreaterThan("budgetPeriod", new Integer(MAKE_DEFAULT_PERIOD));
        boolean dataExists = false;
        Equals eqBudgetPeriod = null;
        //        CoeusVector cvBudgetPeriod = cvBudgetPeriods.filter(gtPeriod);
        //CoeusVector budgetPeriodData = new CoeusVector();
        CoeusVector cvPeriodsData = new CoeusVector();
        if(cvBudgetPeriods != null && cvBudgetPeriods .size() > 0){
            BudgetPeriodBean budgetPeriodBean = null;
            for(int periodIndex = 0; periodIndex < cvBudgetPeriods .size(); periodIndex++){
                budgetPeriodBean = (BudgetPeriodBean)cvBudgetPeriods.get(periodIndex);
                if(cvBudgetDetails!= null && cvBudgetDetails.size() > 0){
                    for(int detailIndex = 0; detailIndex < cvBudgetDetails.size(); detailIndex++){
                        // Filter for the  active period with the budget details(line item details)
                        eqBudgetPeriod = new Equals("budgetPeriod", new Integer(budgetPeriodBean.getBudgetPeriod()));
                        CoeusVector cvBudgetDetail = cvBudgetDetails.filter(eqBudgetPeriod);
                        if(cvBudgetDetail == null || cvBudgetDetail.size() == 0){
                            cvPeriodsData.addElement(budgetPeriodBean);
                            //                            dataExists = true;
                            break;
                        }
                    }
                }
            }
            //            if(!dataExists){
            //                cvBudgetPeriods = cvBudgetPeriods.filter(new Equals("budgetPeriod", new Integer(MAKE_DEFAULT_PERIOD)));
            //            }
        }
        return cvPeriodsData;
    }// End for filtering the active budget Periods
    /**
     * This will Calculate and save the budget for the whole budget
     * Now it is using TxnBean to update the data to the database. Later we have
     * to change this. We have to update these data through Transaction.XML
     * way
     * @return navigator
     * @param budgetInfoBean Budget Information Bean
     * @param session session Object
     * @throws Exception If any error occurs
     */
    protected String calulateAndSaveBudget(BudgetInfoBean budgetInfoBean, HttpSession session) throws Exception{
        String navigator = "";
        //Added for removing instance variable -case # 2960 - start
        QueryEngine queryEngine = QueryEngine.getInstance();
        String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        //Added for removing instance variable - case # 2960- end        
        Hashtable htBudgetData = queryEngine.getDataCollection(queryKey);
        Hashtable htCalculatedData = null;
        htCalculatedData = calculateAllPeriods(htBudgetData, budgetInfoBean);
        edu.mit.coeus.bean.UserInfoBean userInfoBean = (edu.mit.coeus.bean.UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        edu.mit.coeus.budget.bean.BudgetUpdateTxnBean budgetUpdateTxnBean = new edu.mit.coeus.budget.bean.BudgetUpdateTxnBean(userId);
        
        boolean isUpdated = budgetUpdateTxnBean.addUpdDeleteBudget(htBudgetData);
        if(isUpdated){
            navigator = "success";
            removeQueryEngineCollection(queryKey);
        }
        return navigator;
    }
    /**
     * Method to used for getting all the periods except first period
     * @param vecBudgetPeriod vector containg budget periods
     * @throws Exception if any error occurs
     * @return vector containing period for show tab.
     */
    protected Vector getPeriodForShowTab(Vector vecBudgetPeriod) throws Exception{
        Vector vecAllPeriodsExceptDefault = new Vector();
        if(vecBudgetPeriod != null && vecBudgetPeriod.size() > 0){
            vecAllPeriodsExceptDefault = new Vector();
            for(int index = 0; index < vecBudgetPeriod.size(); index ++){
                DynaValidatorForm dynaValidForm = (DynaValidatorForm)vecBudgetPeriod.get(index);
                int budPeriod = ((Integer)dynaValidForm.get("budgetPeriod")).intValue();
                if(budPeriod != MAKE_DEFAULT_PERIOD){
                    vecAllPeriodsExceptDefault.addElement(new Integer(budPeriod));
                }
            }
        }
        return vecAllPeriodsExceptDefault;
    }
    // Method uncommented for Case# 3854: Warning in Lite when salary effective date not in place for a calculation
    /**
     * Getter for property vecMessages.
     * @return Value of property vecMessages.
     */
    public java.util.Vector getVecMessages() {
        return vecMessages;
    }
    
    // Method uncommentd for Case# 3854: Warning in Lite when salary effective date not in place for a calculation
    /**
     * Setter for property vecMessages.
     * @param vecMessages New value of property vecMessages.
     */
    public void setVecMessages(java.util.Vector vecMessages) {
        this.vecMessages = vecMessages;
    }
    /**
     * Method to check total cost of the budget not exceeding $9,999,999,999.99
     * @param htCalculatedData data for getting budget information
     * @throws Exception if any error occurs
     * @return boolean true if total cost exceeding
     */
    protected boolean checkTotalCost(Hashtable htCalculatedData) throws Exception{
        boolean isExceed = false;
        CoeusVector cvBudgetInfoData = (CoeusVector)htCalculatedData.get(BudgetInfoBean.class);
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)cvBudgetInfoData.elementAt(0);
        double budgetTotalCost = budgetInfoBean.getTotalCost();
        budgetTotalCost = ((double)Math.round(budgetTotalCost*Math.pow(10.0, 2) )) / 100;
        if(budgetTotalCost > 9999999999.99){
            isExceed = true;
        }
        return isExceed;
    }
    /**
     * Method to Update Budget_Status in osp$eps_proposal
     * @param request request for budget status
     * @param dynaValidatorForm instance of dyna validator form
     * @throws Exception if any error occurs
     */
    protected void updateBudgetStatus( HttpServletRequest request ,
    DynaActionForm dynaActionForm) throws Exception{
        HttpSession session =  request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        String budgetStatusCode = (String)dynaActionForm.get("budgetStatusCode");
        String proposalNumber = (String)session.getAttribute("proposalNumber"+session.getId());
        HashMap hmUpdBudgetStatus = new HashMap();

        try {
            hmUpdBudgetStatus.put("proposalNumber",proposalNumber);
            hmUpdBudgetStatus.put("budgetStatusCode",budgetStatusCode);
            Hashtable htUpdBudgetStatus =
            (Hashtable)webTxnBean.getResults(request, "updProposalBudgetStatus", hmUpdBudgetStatus);
            if(budgetStatusCode.equals("C")) {
                session.setAttribute("budgetStatusMode", "complete");
            } else if(budgetStatusCode.equals("I")) {
                session.setAttribute("mode"+session.getId(),"incomplete");
                session.setAttribute("budgetStatusMode", "incomplete");
            }
            // Update the proposal hierarchy sync flag
            updateProposalSyncFlags(request, proposalNumber);
        }catch(Exception ex){
            UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(), ex, "BudgetBaseAction", "updateBudgetStatus()");
        }
    }
    /**
     * To read the Budget Menus from the XML file speciofied for the
     * Budget
     * @param request request for selected menu.
     */
    protected void getBudgetMenus(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        Vector budgetMenuItemsVector  = null;
        ReadXMLData readXMLData = new ReadXMLData();
        budgetMenuItemsVector = (Vector) session.getAttribute(BUDGET_MENU_ITEMS);
        if (budgetMenuItemsVector == null || budgetMenuItemsVector.size()==0) {
            budgetMenuItemsVector = readXMLData.readXMLDataForMenu(XML_PATH);
            budgetMenuItemsVector = getVisibleMenus(budgetMenuItemsVector,request);
            session.setAttribute(BUDGET_MENU_ITEMS, budgetMenuItemsVector);
        }else if(budgetMenuItemsVector!= null && budgetMenuItemsVector .size() > 0){
            budgetMenuItemsVector = getVisibleMenus(budgetMenuItemsVector,request);
            session.setAttribute(BUDGET_MENU_ITEMS, budgetMenuItemsVector);
        }
    }
    
    public Vector getVisibleMenus(Vector vecBudgetMenus,HttpServletRequest request) throws Exception{
        boolean value = isPeriodsGenerated(request);
        if(vecBudgetMenus!= null && vecBudgetMenus.size() > 0){
            for(int index = 0; index <vecBudgetMenus.size(); index++){
                MenuBean menuBean = (MenuBean)vecBudgetMenus.get(index);
                if(menuBean.getMenuId().equals(GENERATE_PERIOD_MENU_ID)){
                    if(value){
                        menuBean.setVisible(false);
                        continue;
                    }else{
                        menuBean.setVisible(true);
                        continue;
                    }
                }
            }
        }
        return vecBudgetMenus;
    }
    /**
     *
     * To set the selected status for the Budget Menus
     * @param menuCode menuCode for selection
     * @param request request for selection
     */
    protected void setSelectedStatusBudgetMenu(String menuCode , HttpServletRequest request){
        HttpSession session = request.getSession();
        Vector menuItemsVector  = null;
        menuItemsVector=(Vector)session.getAttribute(BUDGET_MENU_ITEMS);
        Vector modifiedVector = new Vector();
        if(menuItemsVector!= null && menuItemsVector.size() > 0){
            for (int index=0; index<menuItemsVector.size();index++) {
                MenuBean meanuBean = (MenuBean)menuItemsVector.get(index);
                String menuId = meanuBean.getMenuId();
                meanuBean.setSelected(false);
                if (menuId.equals(menuCode)) {
                    meanuBean.setSelected(true);
                }
                modifiedVector.add(meanuBean);
            }
        }
        session.setAttribute(BUDGET_MENU_ITEMS, modifiedVector);
    }
    /**
     * To get Proposal Details For Budget
     * @return Vector containing proposal detail
     * @param request request for txn bean
     * @param proposalNumber proposal Number
     * @throws Exception if any error occurs
     */
    protected Vector getProposalDetailsForBudget(HttpServletRequest request, String proposalNumber)throws Exception{
        HashMap hmProposalDetails = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        hmProposalDetails.put("proposalNumber",proposalNumber);
        Vector vecProposalDetails = new Vector();
        try {
            Hashtable htProposalDetails =
            (Hashtable)webTxnBean.getResults(request, "getProposalDetailsForBudget",hmProposalDetails );
             vecProposalDetails = (Vector)htProposalDetails.get("getProposalDetailsForBudget");
        }catch(Exception ex) {
            UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(), ex, "BudgetBaseAction", "getProposalDetailsForBudget()");
        }
        return vecProposalDetails;
    }
    /**
     * Filter the CoeusVector containing the Budget for the proposal
     * for the selected Version Number
     * @return Budget Information Bean for filter Budget Version
     * @param request request
     * @param cvBudgetInfo vector containing all budget information
     * @param versionNumber version number for filter budget information
     * @throws Exception if any error occcurs
     */
    protected BudgetInfoBean filterBudgetInfo(HttpServletRequest request , CoeusVector cvBudgetInfo, int versionNumber) throws Exception{
        BudgetInfoBean budgetInfoBean = null;
        CoeusVector cvFiletrData = null;
        if(cvBudgetInfo!= null && cvBudgetInfo.size() > 0){
            cvFiletrData = cvBudgetInfo.filter( new Equals("versionNumber",new Integer(versionNumber)));
            if(cvFiletrData!=null && cvFiletrData.size() > 0){
                budgetInfoBean = (BudgetInfoBean)cvFiletrData.get(0);
            }
        }
        return budgetInfoBean;
    }
    /**
     * Getter for property budgetPeriodNumber.
     * @return Value of property budgetPeriodNumber.
     */
//    public int getBudgetPeriodNumber() {
//        return budgetPeriodNumber;
//    }
    /**
     * Setter for property budgetPeriodNumber.
     * @param budgetPeriodNumber New value of property budgetPeriodNumber.
     */
//    public void setBudgetPeriodNumber(int budgetPeriodNumber) {
//        this.budgetPeriodNumber = budgetPeriodNumber;
//    }
    /**
     * Method to check Total Cost Limit is exceeding in budget and Periods.
     * @param request request
     * @param htBudgetData containing all budget Information
     * @throws Exception if any exception occur
     */
    protected void checkTotalCostLimit(HttpServletRequest request ,Hashtable htBudgetData) throws Exception{
        ActionMessages actionMessages = new ActionMessages();
        CoeusVector cvBudgetInfo = (CoeusVector)htBudgetData.get(BudgetInfoBean.class);
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)cvBudgetInfo.elementAt(0);
        if(budgetInfoBean.getTotalCostLimit() > 0 &&
        budgetInfoBean.getTotalCostLimit() < budgetInfoBean.getTotalCost()) {
            actionMessages = new ActionMessages();
            actionMessages.add("budget_common_exceptionCode.101" , new ActionMessage(
            "budget_common_exceptionCode.101" ) );
            saveMessages(request, actionMessages);
            return;
        }
        CoeusVector cvBudgetPeriods = (CoeusVector)htBudgetData.get(BudgetPeriodBean.class);
        if(cvBudgetPeriods != null && cvBudgetPeriods.size() >0){
            if(cvBudgetPeriods.size() > 0){
                for(int index = 0; index < cvBudgetPeriods.size(); index++){
                    BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)cvBudgetPeriods.get(index);
                    if(budgetPeriodBean.getTotalCostLimit() > 0 &&
                    budgetPeriodBean.getTotalCostLimit() < budgetPeriodBean.getTotalCost()) {
                        actionMessages = new ActionMessages();
                        ActionMessage actionMsg = new ActionMessage("budget_common_exceptionCode.102"
                        ,new Integer(budgetPeriodBean.getBudgetPeriod()));
                        actionMessages.add("budget_common_exceptionCode.102" , actionMsg );
                        saveMessages(request, actionMessages);
                        break;
                    }
                }
            }
        }
        
    }
    /**
     * Method to get budget information
     * @param request request
     * @param dynaBudgetForm instance of dyna validator form
     * @throws Exception if any error occurs
     * @return Budget Information Bean for selected Proposal
     */
    protected BudgetInfoBean getBudgetForProposal(HttpServletRequest request ,
    DynaValidatorForm dynaBudgetForm)throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmBudgetInfoData = new HashMap();
        hmBudgetInfoData.put("proposalNumber",(String)dynaBudgetForm.get("proposalNumber"));
        hmBudgetInfoData.put("versionNumber",(Integer)dynaBudgetForm.get("versionNumber"));
        BudgetInfoBean budgetInfoBean = new BudgetInfoBean();
        try {
            Hashtable htBudgetInfoData = (Hashtable)webTxnBean.getResults(request, "getBudgetInfoData" , hmBudgetInfoData );
            Vector vecBudgetInfo = (Vector)htBudgetInfoData.get("getBudgetInfoData");
            DynaValidatorForm dynaForm = (DynaValidatorForm)vecBudgetInfo.get(0);
            BeanUtilsBean copyBudgetBean  = new BeanUtilsBean();
            
            copyBudgetBean.copyProperties(budgetInfoBean, dynaForm);
            budgetInfoBean.setUnitNumber((String)dynaBudgetForm.get("unitNumber"));
        }catch(Exception ex) {
             UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(), ex, "BudgetBaseAction", "getBudgetForProposal()");
        }
        return budgetInfoBean;
    }
    
    /**
     * Method to Insert/Update Budget Data
     * @param cvBudgetInfo containing budgetInfoBeans
     * @param cvBudgetPeriods containing budgetPeriodBeans
     * @throws Exception if any error occurs
     */
    protected void updateBudget(HttpServletRequest request ,CoeusVector cvBudgetInfo,CoeusVector cvBudgetPeriods)  throws Exception {
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)cvBudgetInfo.get(0);
        //Added for removing instance variable -case # 2960 - start
        QueryEngine queryEngine = QueryEngine.getInstance();
        String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        //Added for removing instance variable - case # 2960- end        
        Hashtable htBudgetData = initializeBudgetCalculator(budgetInfoBean);
        queryEngine.addDataCollection(queryKey, htBudgetData);
        HttpSession session = request.getSession();
        
        //get the Proposal Rates Vector and Update it into the htBudgetData
        CoeusVector cvProposalRates = getProposalRatesBean(budgetInfoBean);
        //get the ProposalLARates Vector and Update into the htBudgetData
        CoeusVector cvProposalLARates = getProposalLARatesBean(budgetInfoBean);
        
        htBudgetData.remove(BudgetInfoBean.class);
        htBudgetData.put(BudgetInfoBean.class, cvBudgetInfo);
        
        htBudgetData.remove(BudgetPeriodBean.class);
        htBudgetData.put(BudgetPeriodBean.class, cvBudgetPeriods);
        
        htBudgetData.remove(ProposalRatesBean.class);
        htBudgetData.put(ProposalRatesBean.class,cvProposalRates);
        
        htBudgetData.remove(ProposalLARatesBean.class);
        htBudgetData.put(ProposalLARatesBean.class, cvProposalLARates);
        
        htBudgetData = calculateAllPeriods(htBudgetData, budgetInfoBean);
        
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        BudgetUpdateTxnBean budgetUpdateTxnBean = new BudgetUpdateTxnBean(userInfoBean.getUserId());
        // this has to be updated using Transactions.xml way
        budgetUpdateTxnBean.addUpdDeleteBudget(htBudgetData);
        removeQueryEngineCollection(queryKey);
    }
    /**
     * Method to get the Proposal Rates Bean
     * @param budgetInfoBean Budget Info Bean instance
     * @throws Exception if any error occurs
     * @return CoeusVector of proposalRatesBean
     */
    public CoeusVector getProposalRatesBean(BudgetInfoBean budgetInfoBean) throws Exception{
        //Set Proposal Rates from Institute Rates.
        //Added for removing instance variable -case # 2960 - start
        QueryEngine queryEngine = QueryEngine.getInstance();
        String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        //Added for removing instance variable - case # 2960- end        
        CoeusVector vecFilter = getFilteredVector(queryEngine.getDetails(queryKey, InstituteRatesBean.class),budgetInfoBean);
        InstituteRatesBean instituteRatesBean;
        ProposalRatesBean proposalRatesBean;
        CoeusVector cvProposalRates =  new CoeusVector();
        for(int index = 0; index < vecFilter.size(); index++) {
            instituteRatesBean = (InstituteRatesBean)vecFilter.get(index);
            proposalRatesBean = new ProposalRatesBean();
            
            proposalRatesBean.setAcType(TypeConstants.INSERT_RECORD);
            proposalRatesBean.setProposalNumber(budgetInfoBean.getProposalNumber());
            proposalRatesBean.setVersionNumber(budgetInfoBean.getVersionNumber());
            proposalRatesBean.setActivityCode(budgetInfoBean.getActivityTypeCode());
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
            proposalRatesBean.setAw_ActivityTypeCode(budgetInfoBean.getActivityTypeCode());
            
            cvProposalRates.addElement(proposalRatesBean);
            
        }
        return cvProposalRates;
    }
    
    /*Method to get the Proposal LA Rates Bean.
     * @param budgetInfoBean
     * @throws Exception
     * @return
     */
    /**
     * Method to set Proposal LA rates and get in vector
     * @param budgetInfoBean Budget Info Bean instance
     * @throws Exception if any error occurs
     * @return vector of Proposal LA Rates Beans
     */
    private CoeusVector getProposalLARatesBean(BudgetInfoBean budgetInfoBean) throws Exception{
        //Set Proposal Rates from Institute Rates.
        //Added for removing instance variable -case # 2960 - start
        QueryEngine queryEngine = QueryEngine.getInstance();
        String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        //Added for removing instance variable - case # 2960- end        
        CoeusVector vecFilter = getFilteredVector(queryEngine.getDetails(queryKey, InstituteLARatesBean.class),budgetInfoBean);
        InstituteLARatesBean instituteLARatesBean;
        ProposalLARatesBean proposalLARatesBean;
        CoeusVector cvProposalLARates = new CoeusVector();
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
            
            cvProposalLARates.addElement(proposalLARatesBean);
        }
        return cvProposalLARates;
    }
    
    
    /**
     * Method to filter the Vector for each Rates and finding all applicable
     * for each Rates during the Proposal Period
     * @param vecRateBean vector of Rates Bean
     * @param budgetInfoBean Budget Info Bean instance
     * @return vector containing each Rates and finding all applicable
     */
    public CoeusVector getFilteredVector(CoeusVector vecRateBean,BudgetInfoBean budgetInfoBean) {
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
                    CoeusVector vecRateBeanOnThePeriods = getRatesOnThePeriod(vecEachRateBean,budgetInfoBean);
                    completeVector.addAll(vecRateBeanOnThePeriods);
                    
                    
                    CoeusVector vecRateBeanWithInPeriod = getRatesWithInPeriod(vecEachRateBean,budgetInfoBean);
                    completeVector.addAll(vecRateBeanWithInPeriod);
                    
                    rateCode.put((Object) new String(rateClassCode+"-"+rateTypeCode),(Object) new String(rateClassCode+"-"+rateTypeCode));
                }
            }
            
        }
        return completeVector;
    }
    
    /**
     *Method to find all the Rates just before the Start Date of Proposal
     * @param vecRateBean CoeusVector containing all the Rates from the Master Table
     * @param budgetInfoBean BudgetInfoBean for getting Start Date and End Date of Proposal
     * @return vector containing rates
     */
    public CoeusVector getRatesOnThePeriod(CoeusVector vecRateBean,BudgetInfoBean budgetInfoBean) {
        
        long longTime = budgetInfoBean.getStartDate().getTime();
        Equals equalsStartDate = new Equals("startDate", new java.sql.Date(longTime));
        LesserThan lesserThanStartDate =  new LesserThan("startDate", new java.sql.Date(longTime));
        Or equalsOrlesserThanStartDate = new Or(equalsStartDate, lesserThanStartDate);
        Equals equalsOnCampus = new Equals("onOffCampusFlag", true);
        Equals equalsOffCampus = new Equals("onOffCampusFlag", false);
        And equalsOrlesserThanStartDateAndEqualsOnCampus = new And(equalsOrlesserThanStartDate, equalsOnCampus);
        And equalsOrlesserThanStartDateAndEqualsOffCampus = new And(equalsOrlesserThanStartDate, equalsOffCampus);
        CoeusVector lessThanOrEqOffCampusVector;
        CoeusVector lessThanOrEqOnCampusVector;
        CoeusVector combinedRates = new CoeusVector();
        
        lessThanOrEqOffCampusVector = vecRateBean.filter(equalsOrlesserThanStartDateAndEqualsOffCampus);
        if(lessThanOrEqOffCampusVector != null && lessThanOrEqOffCampusVector.size() > 0) {
            lessThanOrEqOffCampusVector.sort("startDate",false);
            combinedRates.add(lessThanOrEqOffCampusVector.get(0));
        }
        
        lessThanOrEqOnCampusVector = vecRateBean.filter(equalsOrlesserThanStartDateAndEqualsOnCampus);
        if(lessThanOrEqOnCampusVector != null && lessThanOrEqOnCampusVector.size() > 0) {
            lessThanOrEqOnCampusVector.sort("startDate",false);
            combinedRates.add(lessThanOrEqOnCampusVector.get(0));
        }
        combinedRates.sort("startDate", true);
        return combinedRates;
    }
    
    
    /**
     * Method to find all the Rates with in the a Proposal Period
     * @param vecRateBean CoeusVector containing all the Rates from the Master Table
     * @param budgetInfoBean BudgetInfoBean for getting Start Date and End Date of Proposal
     * @return vector containing rate b/w start and end date of budget
     */
    public CoeusVector getRatesWithInPeriod(CoeusVector vecRateBean,BudgetInfoBean budgetInfoBean) {
        long longTime = budgetInfoBean.getStartDate().getTime();
        GreaterThan greaterThanStartDateObj =  new GreaterThan("startDate",new java.sql.Date(longTime));
        longTime = budgetInfoBean.getEndDate().getTime();
        LesserThan lesserThanEndDateObj =  new LesserThan("startDate",new java.sql.Date(longTime));
        Equals equalsEndDateObj = new Equals("startDate",new java.sql.Date(longTime));
        Or lsEqualEndDate = new Or(equalsEndDateObj,lesserThanEndDateObj);
        And dateGreaterAndEquals = new And(greaterThanStartDateObj,lsEqualEndDate);
        CoeusVector vecFiltered = vecRateBean.filter(dateGreaterAndEquals);
        return vecFiltered;
    }
    
    /**
     * Setter for property vecBudgetStatus.
     * @param vecBudgetStatus New value of property vecBudgetStatus.
     */
//    public void setVecBudgetStatus(java.util.Vector vecBudgetStatus) {
//        this.vecBudgetStatus = vecBudgetStatus;
//    }

  // Case id# 2924 - start
   /**
     * Setter for property vecCampusFlag.
     * @param vecCampusFlag New value of property vecCampusFlag.
     */
    //Commented for removing instance variable - start
//    public void setVecCampusFlag(java.util.Vector vecCampusFlag) {
//        this.vecCampusFlag = vecCampusFlag;
//    }    
    //Commented for removing instance variable - end
  // Case id# 2924 - end  

    /**
     * Get the Calculated BudgetDetailCalAmts data and set the values for the particular line Item
     * @dynaForm instance of DynaValidator form
     * @ returns Vector containing the Dynavalues of the BudgetDetailCalAmts data
     * @param dynaForm instance of DynaValidator form
     * @param request request for txnbean
     * @param budgetInfoBean Bean for Budget Information
     * @param budgetPeriod Budget Period
     * @param lineItemNumber Line Item Number
     * @param costElement Cost Element for Amount
     * @throws Exception if any error occurs
     * @return vector of calculated amount
     */
    protected Vector getCalculatedAmtsData(BudgetInfoBean budgetInfoBean, int budgetPeriod,
    int lineItemNumber, String costElement, CoeusDynaBeansList dynaForm, HttpServletRequest request,
    Vector vecBudgetDetailCalAmts) throws Exception{
        CoeusVector cvCalAmts = new CoeusVector();
        //Set Valid CE Rates
        //        HashMap hmBudgetData = new HashMap();
        //        WebTxnBean webTxnBean = new WebTxnBean();
        //        hmBudgetData.put("proposalNumber",budgetInfoBean.getProposalNumber());
        //        hmBudgetData.put("versionNumber",new Integer(budgetInfoBean.getVersionNumber()));
        //        Hashtable htBudgetDetails = (Hashtable)webTxnBean.getResults(request,"getBudgetDetailCalAmts",hmBudgetData);
        //        Vector vecBudgetDetailCalAmts = (Vector)htBudgetDetails.get("getBudgetDetailCalAmts");
        // String acType = (String)dynaForm.get("acType");
        String acType = null;
        boolean status = checkForServerData(vecBudgetDetailCalAmts,budgetPeriod,lineItemNumber);
        if(status){
            acType = TypeConstants.INSERT_RECORD;
        }else{
            acType = TypeConstants.UPDATE_RECORD;
        }
        CoeusVector vecCalCTypes = null;
        BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
        BudgetDataTxnBean budgetDataTxnBean  = new BudgetDataTxnBean();
        CoeusVector vecValidRateTypes= budgetDataTxnBean.getValidCERateTypes(costElement);
        if(vecValidRateTypes == null){
            vecValidRateTypes = new CoeusVector();
        }
        //Check wheather it contains Inflation Rate
        NotEquals nEqInflation = new NotEquals("rateClassType",RateClassTypeConstants.INFLATION);
        Equals eqOH = new Equals("rateClassType", RateClassTypeConstants.OVERHEAD);
        NotEquals nEqOH = new NotEquals("rateClassType", RateClassTypeConstants.OVERHEAD);
        Equals eqBudgetRateClass = new Equals("rateClassCode",new Integer(budgetInfoBean.getOhRateClassCode()));
        
        And eqOHAndEqBudgetRateClass = new And(eqOH, eqBudgetRateClass);
        Or eqOHAndEqBudgetRateClassOrNEqOH = new Or(eqOHAndEqBudgetRateClass, nEqOH);
        And nEqInflationAndeqOHAndEqBudgetRateClassOrNEqOH = new And(nEqInflation, eqOHAndEqBudgetRateClassOrNEqOH);
        
        vecValidRateTypes = vecValidRateTypes.filter(nEqInflationAndeqOHAndEqBudgetRateClassOrNEqOH);
        /**
         * Check whether any LA rates are applicable for the home unit, if not
         * filter out all the LA rates
         */
        CoeusVector cvLARates = budgetDataTxnBean.getProposalInstituteLARates(budgetInfoBean.getProposalNumber(),budgetInfoBean.getVersionNumber());
        if (cvLARates == null || cvLARates.size() == 0) {
            NotEquals neqLA = new NotEquals("rateClassType",RateClassTypeConstants.LAB_ALLOCATION);
            NotEquals neqLASal = new NotEquals("rateClassType", RateClassTypeConstants.LA_WITH_EB_VA);
            And laAndLaSal = new And(neqLA, neqLASal);
            
            vecValidRateTypes = vecValidRateTypes.filter(laAndLaSal);
        }
        //insert to Query Engine
        if(vecValidRateTypes != null && vecValidRateTypes.size() > 0) {
            for(int index = 0; index < vecValidRateTypes.size(); index++) {
                ValidCERateTypesBean validCERateTypesBean;
                
                budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
                validCERateTypesBean = (ValidCERateTypesBean)vecValidRateTypes.get(index);
                
                budgetDetailCalAmountsBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                budgetDetailCalAmountsBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                budgetDetailCalAmountsBean.setBudgetPeriod(budgetPeriod);
                budgetDetailCalAmountsBean.setLineItemNumber(lineItemNumber);
                budgetDetailCalAmountsBean.setRateClassType(validCERateTypesBean.getRateClassType());
                budgetDetailCalAmountsBean.setRateClassCode(validCERateTypesBean.getRateClassCode());
                budgetDetailCalAmountsBean.setRateTypeCode(validCERateTypesBean.getRateTypeCode());
                budgetDetailCalAmountsBean.setRateClassDescription(validCERateTypesBean.getRateClassDescription());
                budgetDetailCalAmountsBean.setRateTypeDescription(validCERateTypesBean.getRateTypeDescription());
                budgetDetailCalAmountsBean.setApplyRateFlag(true);
                budgetDetailCalAmountsBean.setUpdateTimestamp(prepareTimeStamp());
                budgetDetailCalAmountsBean.setAcType(acType);
                cvCalAmts.addElement(budgetDetailCalAmountsBean);
            }
        }
        
        Equals eqLabAllocSal = new Equals("rateClassType",RateClassTypeConstants.LA_WITH_EB_VA);
        CoeusVector vecLabAllocSal = vecValidRateTypes.filter(eqLabAllocSal);
        
        if(vecLabAllocSal != null && vecLabAllocSal.size() > 0) {
            //Has Lab allocation and Salaries Entry (i.e Rate Class Type = Y)
            Equals eqE = new Equals("rateClassType",RateClassTypeConstants.EMPLOYEE_BENEFITS);
            Equals eqV = new Equals("rateClassType",RateClassTypeConstants.VACATION);
            vecCalCTypes = budgetDataTxnBean.getValidCalcTypesForEV();
            vecCalCTypes = vecCalCTypes.filter(eqE);
            if (vecCalCTypes.size() > 0) {
                ValidCalcTypesBean validCalcTypesBean = (ValidCalcTypesBean) vecCalCTypes.get(0);
                if (validCalcTypesBean.getDependentRateClassType().equals(RateClassTypeConstants.LA_WITH_EB_VA)) {
                    budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
                    budgetDetailCalAmountsBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                    budgetDetailCalAmountsBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                    budgetDetailCalAmountsBean.setBudgetPeriod(budgetPeriod);
                    budgetDetailCalAmountsBean.setLineItemNumber(lineItemNumber);
                    budgetDetailCalAmountsBean.setRateClassType(validCalcTypesBean.getRateClassType());
                    budgetDetailCalAmountsBean.setRateClassCode(validCalcTypesBean.getRateClassCode());
                    budgetDetailCalAmountsBean.setRateTypeCode(validCalcTypesBean.getRateTypeCode());
                    budgetDetailCalAmountsBean.setRateClassDescription(validCalcTypesBean.getRateClassDescription());
                    budgetDetailCalAmountsBean.setRateTypeDescription(validCalcTypesBean.getRateTypeDescription());
                    budgetDetailCalAmountsBean.setApplyRateFlag(true);
                    budgetDetailCalAmountsBean.setUpdateTimestamp(prepareTimeStamp());
                    budgetDetailCalAmountsBean.setAcType(acType);
                    if(!cvCalAmts.contains(budgetDetailCalAmountsBean)){
                        cvCalAmts.addElement(budgetDetailCalAmountsBean);
                    }
                }
            }//End IF Size > 0
            vecCalCTypes = budgetDataTxnBean.getValidCalcTypesForEV();
            vecCalCTypes = vecCalCTypes.filter(eqV);
            if (vecCalCTypes.size() > 0) {
                ValidCalcTypesBean validCalcTypesBean = (ValidCalcTypesBean) vecCalCTypes.get(0);
                if (validCalcTypesBean.getDependentRateClassType().equals(RateClassTypeConstants.LA_WITH_EB_VA)) {
                    budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
                    budgetDetailCalAmountsBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                    budgetDetailCalAmountsBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                    budgetDetailCalAmountsBean.setBudgetPeriod(budgetPeriod);
                    budgetDetailCalAmountsBean.setLineItemNumber(lineItemNumber);
                    budgetDetailCalAmountsBean.setRateClassType(validCalcTypesBean.getRateClassType());
                    budgetDetailCalAmountsBean.setRateClassCode(validCalcTypesBean.getRateClassCode());
                    budgetDetailCalAmountsBean.setRateTypeCode(validCalcTypesBean.getRateTypeCode());
                    budgetDetailCalAmountsBean.setRateClassDescription(validCalcTypesBean.getRateClassDescription());
                    budgetDetailCalAmountsBean.setRateTypeDescription(validCalcTypesBean.getRateTypeDescription());
                    budgetDetailCalAmountsBean.setApplyRateFlag(true);
                    budgetDetailCalAmountsBean.setUpdateTimestamp(prepareTimeStamp());
                    budgetDetailCalAmountsBean.setAcType(acType);
                    if(!cvCalAmts.contains(budgetDetailCalAmountsBean)){
                        cvCalAmts.addElement(budgetDetailCalAmountsBean);
                    }
                }
            }//End IF Size > 0
            
        }//End IF Lab Allocation Salaries
        Vector vecCalAmts = getDynaCalAmtsdData(cvCalAmts,dynaForm, request);
        return vecCalAmts;
    }
    /** Calculates period cost.
     * @param type valid Cost types are :
     * TOTAL_DIRECT_COST
     * TOTAL_INDIRECT_COST
     * COST_SHARING_AMOUNT
     * @return calculatedAmount
     */
    public double calculatePeriodCost(CoeusVector cvBudgetDetailsCalAmts, CoeusVector cvBudgetDetails,
    int budgetPeriod, int type) throws Exception{
        
        double calculatedAmount = 0;
        
        if(cvBudgetDetailsCalAmts == null){
            cvBudgetDetailsCalAmts = new CoeusVector();
        }
        if(cvBudgetDetails == null){
            cvBudgetDetails = new CoeusVector();
        }
        CoeusVector cvBudgetDetailsCalAmt = cvBudgetDetailsCalAmts.filter(new Equals("budgetPeriod", new Integer(budgetPeriod)));
        NotEquals notDelete = new NotEquals("acType",TypeConstants.DELETE_RECORD);
        Equals eqNull = new Equals("acType", null);
        Equals eqEmpty = new Equals("acType", EMPTY_STRING);
        Or eqNullOrEmpty = new Or(eqNull,eqEmpty);
        Or notDeleteOrEqNull = new Or(notDelete, eqNullOrEmpty);
        cvBudgetDetailsCalAmt = cvBudgetDetailsCalAmt.filter(notDeleteOrEqNull);
        
        switch (type) {
            case TOTAL_DIRECT_COST:
                calculatedAmount = cvBudgetDetailsCalAmt.sum("calculatedCost", new NotEquals("rateClassType",RateClassTypeConstants.OVERHEAD)) +
                cvBudgetDetails.sum("lineItemCost");
                calculatedAmount =((double)Math.round(calculatedAmount*Math.pow(10.0, 2) )) / 100;
                break;
            case TOTAL_INDIRECT_COST:
                calculatedAmount = cvBudgetDetailsCalAmt.sum("calculatedCost", new Equals("rateClassType",RateClassTypeConstants.OVERHEAD));
                calculatedAmount =((double)Math.round(calculatedAmount*Math.pow(10.0, 2) )) / 100;
                break;
            case COST_SHARING_AMOUNT:
                calculatedAmount = cvBudgetDetailsCalAmts.sum("calculatedCostSharing") +
                cvBudgetDetails.sum("costSharingAmount");
                break;
            case UNDERRECOVERY:
                calculatedAmount = cvBudgetDetails.sum("underRecoveryAmount");
                break;
        }
        return calculatedAmount;
    }
    
    /**
     * This method get the activity type details for a particular proposal number and version number
     * returns ActionForward object
     * @param request for txnbean
     * @param versionNumber
     * @param proposalNumber
     * @throws Exception
     * @return activityTypeCode
     */
    public CoeusVector checkActivityTypeChanged(HttpServletRequest request,String proposalNumber, int versionNumber) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmPropData = new HashMap();
        CoeusVector cvActivityType = new CoeusVector();
        hmPropData.put("proposalNumber",proposalNumber);
        hmPropData.put("versionNumber",new Integer(versionNumber));
        try {
            Hashtable htActivityTypeStatus =
            (Hashtable)webTxnBean.getResults(request, "getActivityTypeForVersion",hmPropData);
            hmPropData = (HashMap)htActivityTypeStatus.get("getActivityTypeForVersion");
            String activityType  = (String)hmPropData.get("li_ActivityType");
            if(activityType !=null && !activityType.equals(EMPTY_STRING) ){
                int activityTypeCode = Integer.parseInt(activityType);
                hmPropData.put("activityTypeCode",new Integer(activityTypeCode));
                hmPropData.put("proposalNumber",proposalNumber);
                htActivityTypeStatus = (Hashtable)webTxnBean.getResults(request, "checkActivityTypeModified", hmPropData);
                hmPropData = (HashMap)htActivityTypeStatus.get("checkActivityTypeModified");
                String activityTypeModified = (String)hmPropData.get("li_type");
                cvActivityType.insertElementAt(activityType, 0);
                cvActivityType.insertElementAt(activityTypeModified, 1);
            }
            hmPropData = null;
            htActivityTypeStatus = null;
            activityType = null;
            webTxnBean = null;
         }catch(Exception ex) {
             UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(), ex, "BudgetBaseAction", "checkActivityTypeChanged()");
        }
        return cvActivityType;
    }
    /**
     * To get the ActivityType from the proposal
     * @param request request for txnbean
     * @param proposalNumber
     * @throws Exception
     * @ return activityType
     */
    protected int getActivityTypeForProposal(HttpServletRequest request,String proposalNumber) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmActivityType = new HashMap();
        int activityType=0 ;
        
        hmActivityType.put("proposalNumber",proposalNumber);
        try{
            Hashtable htActivityTypeCode = (Hashtable)webTxnBean.getResults(request, "getActivityTypeForProposal", hmActivityType);
            Vector vecActivityType = (Vector)htActivityTypeCode.get("getActivityTypeForProposal");

            if(vecActivityType != null && vecActivityType.size()>0) {
                DynaValidatorForm dynaForm = (DynaValidatorForm)vecActivityType.get(0);
                String activityTypeCode=(String)dynaForm.get("activityTypeCode");
                activityType = Integer.parseInt(activityTypeCode);
            }
        }catch(Exception ex) {
             UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(), ex, "BudgetBaseAction", "getActivityTypeForProposal()");
        }
        return activityType;
    }
    /** To get on off Campus */
    protected boolean getCampusFlag(HttpServletRequest request, String costElementCode) throws Exception{
        HttpSession session = request.getSession();
        boolean isCampusFlag = false;
        Vector vecCostElements = (Vector)session.getAttribute("costElementData");
        String campusFlag = null;
        if(vecCostElements != null && vecCostElements.size() > 0){
            for(int index = 0; index < vecCostElements.size(); index++){
                CategoryBean categoryBean = (CategoryBean)vecCostElements.get(index);
                if(categoryBean.getCostElement().equals(costElementCode)){
                    campusFlag = categoryBean.getCampusFlag();
                    if(campusFlag.equals(ON_CAMPUS_FLAG)){
                        isCampusFlag = true;
                    }
                    break;
                }
            }
        }
        return isCampusFlag;
    }
    /**
     * Method to get filter Budget Period
     * @return DynaValidatorForm
     * @param vecBudgetPeriod
     * @param budgetPeriod
     * @param dynaForm
     * @throws Exception
     */
    protected DynaValidatorForm getFileteredBudgetPeriod(Vector vecBudgetPeriod,
    int budgetPeriod) throws Exception{
        DynaValidatorForm dynaForm = null;
        if(vecBudgetPeriod!= null){
            for(int index = 0; index < vecBudgetPeriod.size() ; index++){
                dynaForm = (DynaValidatorForm)vecBudgetPeriod.get(index);
                int dynaBudgetPeriod = ((Integer)dynaForm.get("budgetPeriod")).intValue();
                if(dynaBudgetPeriod == budgetPeriod){
                    return dynaForm;
                }
            }
        }
        return dynaForm;
    }
    /** this method has to be changed in Budget Personnel Line Item Action
     * Method to get Maximum line item number
     * @return max line item number
     * @param vecFilterBudgetPeriod
     * @throws Exception
     */
    protected int getMaxLineItemNumberForBudget(Vector vecFilterBudgetPeriod) throws Exception{
        int lineItemNumber = 0;
        if(vecFilterBudgetPeriod == null || vecFilterBudgetPeriod.size() == 0){
            lineItemNumber = 0;
            return lineItemNumber;
        }
        for(int index = 0 ; index < vecFilterBudgetPeriod.size() ; index ++){
            DynaValidatorForm dynaForm = (DynaValidatorForm)vecFilterBudgetPeriod.get(index);
            int beanLineItemNumber = ((Integer)dynaForm.get("lineItemNumber")).intValue();
            if(lineItemNumber < beanLineItemNumber){
                lineItemNumber = beanLineItemNumber;
            }
        }
        return lineItemNumber;
    }
    /**
     *Get old budget Line Item Detail for Indexed Validation framework
     */
    protected Vector getOldBudgetLIDetails(HttpServletRequest request, int budgetPeriod) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)request.getSession().getAttribute("budgetInfoBean");
        Map hmLIDetail = new HashMap();
        hmLIDetail.put("proposalNumber",budgetInfoBean.getProposalNumber());
        hmLIDetail.put("versionNumber",new Integer(budgetInfoBean.getVersionNumber()));
        Vector vecBudgetLIDetails = new Vector();
        try {
            Hashtable htProposalDetails =
            (Hashtable)webTxnBean.getResults(request, "getBudgetDetail",hmLIDetail );
             vecBudgetLIDetails = (Vector)htProposalDetails.get("getBudgetDetail");
            vecBudgetLIDetails = filterBudgetPeriod(budgetPeriod, vecBudgetLIDetails);
            //Vector vecCEData = (Vector)request.getSession().getAttribute("costElementData");
            //COEUSQA-1414 Allow schools to indicate if cost element is still active - Start
            Vector vecCEData = (Vector)request.getSession().getAttribute("costElementAllData");
            //COEUSQA-1414 Allow schools to indicate if cost element is still active - End
            vecBudgetLIDetails = getExistingCE(vecBudgetLIDetails, vecCEData);
         }catch(Exception ex) {
             UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(), ex, "BudgetBaseAction", "getOldBudgetLIDetails()");
        }
        return vecBudgetLIDetails;
    }
    protected double formatStringToDouble(String strValue) throws Exception{
        double value = 0 ;
        if(strValue!=null && !strValue.equals(EMPTY_STRING)){
//            String replacement = strValue.replaceAll("[$,/,]",EMPTY_STRING); 
//            value = Double.parseDouble(replacement);
            String regExp = "[$,/,@,#,$,%,^,&,*,(,),-,_,+,%,!]";
            String replacement = strValue.replaceAll(regExp,EMPTY_STRING);
            if(replacement != null && !replacement.equals(EMPTY_STRING)){
            value = Double.parseDouble(replacement);
            }
        }
        return value;
    }
     /*
      * Check for Budget Sync and Multiple Appointments
      * @param HttpServletRequest
      * @param budgetInfoBean
      * @param hmRequiredDetails
      * @throws Exception
      * @return navigator
      */
    public String checkBudgetSyncPersons(HttpServletRequest request, BudgetInfoBean budgetInfoBean , HashMap hmRequiredDetails , String functionType)throws Exception{
        String navigator = EMPTY_STRING;
        HttpSession session =  request.getSession();
        CoeusDynaBeansList coeusSyncPersonsDynaList = (CoeusDynaBeansList) hmRequiredDetails.get(ActionForm.class);
        // DynaValidatorForm dynaForm = (DynaValidatorForm)hmRequiredDetails.get(DynaValidatorForm.class);
        DynaActionForm dynaForm = coeusSyncPersonsDynaList.getDynaForm(request,"budgetSummary");
        //Check Sync Budget Persons
        SyncBudgetPersons syncBudgetPersons = new SyncBudgetPersons();
        String multipleAppointmentExist = request.getParameter("multipleAppointmentExist");
        String syncPersonsExist = request.getParameter("syncPersonsExist");
        String mode =(String) session.getAttribute("mode"+session.getId());
        // char functionType = 'N';
        
        HashMap hmSyncBudgetPersons = syncBudgetPersons.getAllPropPersons(budgetInfoBean, functionType, request,dynaForm);
        CoeusVector cvMultipleAppInfo = (CoeusVector) hmSyncBudgetPersons.get("multipleAppointmentsInfo");
        CoeusVector cvSyncPersonsInfo = (CoeusVector) hmSyncBudgetPersons.get("syncPersonsInfo");
        CoeusVector cvValidPersonsInfo = (CoeusVector) hmSyncBudgetPersons.get("validPersonsSyncDetail");
        HashMap hmMultipleAppointmentsPerson = (HashMap) hmSyncBudgetPersons.get("hmMultipleAppointments");
        CoeusVector cvMultipleAppointmentNames = (CoeusVector) hmSyncBudgetPersons.get("multipleAppointmentsNames");
        
        if(cvMultipleAppInfo !=null && cvMultipleAppInfo.size()>0 && !mode.equals("display") &&
        (multipleAppointmentExist ==null || multipleAppointmentExist.equals(EMPTY_STRING)) &&
        ( syncPersonsExist == null || syncPersonsExist.equals(EMPTY_STRING) )){
            if(cvMultipleAppInfo.size()>0){
                navigator= MULTIPLE_APPOINTMENTS;
                request.setAttribute("multipleAppointments", cvMultipleAppInfo);
                session.setAttribute("multipleAppointmentsPersons", hmMultipleAppointmentsPerson);
                return navigator;
            }
        }
        
        // get the selected data for multiple appointments
        CoeusVector cvSelectedInfo = new CoeusVector();
        if(multipleAppointmentExist !=null && multipleAppointmentExist.equals("Y")){
            cvSelectedInfo = syncBudgetPersons.getSelectedAppointments(request,budgetInfoBean);
            session.setAttribute("selectedMulitpleAppsInfo", cvSelectedInfo);
        }
        
        if( cvSyncPersonsInfo !=null && cvSyncPersonsInfo.size() >0 && !mode.equals("display") &&
        ( syncPersonsExist == null || syncPersonsExist.equals(EMPTY_STRING)) ){
            navigator = SYNC_PERSONS;
            //validation framework start
            coeusSyncPersonsDynaList.setList(cvSyncPersonsInfo);
            session.setAttribute("syncPersonsDynaList",coeusSyncPersonsDynaList);
            //            //validation framework end
            return navigator;
        }
        session.setAttribute("validPersonsSyncDetail", cvValidPersonsInfo);
        return navigator;
    }
    
    /**
     * Save the budget sync persons details in to osp$budget_persons
     * @throws Exception
     */
    
    public void saveBudgetSyncPersons(HttpServletRequest request)throws Exception {
        HttpSession session =  request.getSession();
        SyncBudgetPersons syncBudgetPersons = new SyncBudgetPersons();
        CoeusVector cvValidPersons = (CoeusVector) session.getAttribute("validPersonsSyncDetail");
        CoeusVector cvSelectedMultipleAppointments = (CoeusVector) session.getAttribute("selectedMulitpleAppsInfo");
        CoeusVector cvSyncPersons = (CoeusVector) session.getAttribute("syncBudgetPersonDetails");
        CoeusVector cvAllSyncPersons = syncBudgetPersons.getAllBudgetSyncPersons(cvValidPersons,cvSelectedMultipleAppointments,cvSyncPersons);
        session.removeAttribute("selectedMulitpleAppsInfo");
        session.removeAttribute("syncBudgetPersonDetails");
        session.removeAttribute("validPersonsSyncDetail");
        // update the budget persons
        if(cvAllSyncPersons !=null && cvAllSyncPersons.size()>0){
            syncBudgetPersons.updateBudgetPersons(request, cvAllSyncPersons);
        }
    }
    
    /**
     * Check salary for person if 0 navigate to Budget Personnel Page or 1 navigate to budget summary page.
     * @param request request for txnbean
     * @param proposalNumber
     *@param versionNumber
     * @throws Exception
     * @ return salaryCount
     */
    protected int checkSalaryBudgetPerson(HttpServletRequest request,String proposalNumber, int versionNumber) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmPersonInfo = new HashMap();
        int salaryCount=0 ;
        hmPersonInfo.put("proposalNumber",proposalNumber);
        hmPersonInfo.put("versionNumber",new Integer(versionNumber));

        try {
            Hashtable htPersonsCode = (Hashtable)webTxnBean.getResults(request, "checkSalaryBudgetPerson", hmPersonInfo);
            hmPersonInfo  = (HashMap)htPersonsCode.get("checkSalaryBudgetPerson");
            if(hmPersonInfo != null && hmPersonInfo.size()>0) {
                String salCount=(String) hmPersonInfo.get("sal_count");
                salaryCount = Integer.parseInt(salCount);
            }
         }catch(Exception ex) {
             UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(), ex, "BudgetBaseAction", "checkSalaryBudgetPerson()");
        }
        return salaryCount;
    }
    
    
    /** Read the save status for the given proposal number and sequence number
     *@throws Exception
     */
    protected void readSavedStatus(WebTxnBean webTxnBean, HttpServletRequest request) throws Exception{
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)request.getSession().getAttribute("budgetInfoBean");
        Map hmSavedData =null;
        Hashtable htReqData =null;
        HashMap hmMenuData = null;
        String proposalNumber = budgetInfoBean.getProposalNumber();
        int versionNumber = budgetInfoBean.getVersionNumber();

        try {
            Vector menuData= (Vector)request.getSession().getAttribute(CoeusliteMenuItems.BUDGET_MENU_ITEMS);
            if(webTxnBean == null){
                webTxnBean = new WebTxnBean();
            }
            if(menuData!= null && menuData.size() > 0){
                hmSavedData = new HashMap();
                htReqData = new Hashtable();
                hmMenuData =new HashMap();
                MenuBean dataBean = null;
                for(int index = 0; index < menuData.size(); index++){
                    dataBean = (MenuBean)menuData.get(index);
                    String menuId =dataBean.getMenuId();
                    hmMenuData.put("proposalNumber", proposalNumber);
                    hmMenuData.put("versionNumber", new Integer(versionNumber));
                    hmMenuData.put("menuId", menuId);
                    htReqData = (Hashtable)webTxnBean.getResults(request, "getSavedBudgetMenuData", hmMenuData);
                    HashMap hmReturnData = (HashMap)htReqData.get("getSavedBudgetMenuData");
                    if(hmReturnData!=null) {
                        String strValue = (String)hmReturnData.get("AV_SAVED_DATA");
                        int value = Integer.parseInt(strValue);
                        if(value == 1){
                            dataBean.setDataSaved(true);
                        }else if(value == 0){
                            dataBean.setDataSaved(false);
                        }
                    }
                }
                request.getSession().removeAttribute(CoeusliteMenuItems.BUDGET_MENU_ITEMS);
                request.getSession().setAttribute(CoeusliteMenuItems.BUDGET_MENU_ITEMS, menuData);

            }
        }catch(Exception ex) {
             UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(), ex, "BudgetBaseAction", "readSavedStatus()");
        }
    }
    
    /** Manufacture the LockBean based on the parameter passed by the specific module
     *say, Propsoal, Protocol, Budget etc.
     *@param UserInfoBean, Proposal number
     *@returns LockBean
     *@throws Exception
     */
    protected LockBean getLockingBean(UserInfoBean userInfoBean, String proposalNumber, HttpServletRequest request) throws Exception{
        LockBean lockBean = new LockBean();
        lockBean.setLockId(CoeusLiteConstants.BUDGET_LOCK_STR+proposalNumber);
        String mode = (String)request.getSession().getAttribute(CoeusLiteConstants.MODE+request.getSession().getId());
        String budgetStatusMode = (String)request.getSession().getAttribute("budgetStatusMode");
        mode = getMode(mode, budgetStatusMode);
        lockBean.setMode(mode);
        lockBean.setModuleKey(CoeusLiteConstants.BUDGET_MODULE);
        lockBean.setModuleNumber(proposalNumber);
        lockBean.setModuleUnitNumber(userInfoBean.getUnitNumber());
        lockBean.setUnitNumber(UNIT_NUMBER);
        lockBean.setUserId(userInfoBean.getUserId());
        lockBean.setUserName(userInfoBean.getUserName());
        lockBean.setSessionId(request.getSession().getId());
        return lockBean;
    }
    
    protected String getMode(String mode, String budgetStatusMode) throws Exception{
        
        if(mode!=null && mode.equalsIgnoreCase("display")){
            mode = CoeusLiteConstants.DISPLAY_MODE;
        } else if(budgetStatusMode!=null && budgetStatusMode.equals("complete")) {
            mode = CoeusLiteConstants.DISPLAY_MODE;
        } else{
            mode = CoeusLiteConstants.MODIFY_MODE;
        }
        return mode;
    }
    
    /** This method will notify for the acquiring and releasing the Lock
     *based on the way the locks are opened.
     *It will check whether the proposal is opened through search or list
     *Based on the conditions it will acquire the lock and release the lock
     *If it locked then it will prepare the locking messages
     *@param UserInfoBean, proposalNumber(Current)
     *@throws Exception
     *@returns boolean is locked or not
     */
    protected boolean prepareLock(UserInfoBean userInfoBean, String proposalNumber, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        boolean isSuccess = true;
        WebTxnBean webTxnBean = new WebTxnBean();
        LockBean lockBean = null;
        LockBean sessionLockBean = (LockBean)session.getAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());
        
        // proposal opened from list
        lockBean = getLockingBean(userInfoBean, proposalNumber,request);
        LockBean serverDataBean = getLockedData(CoeusLiteConstants.BUDGET_LOCK_STR+lockBean.getModuleNumber(), request);
        boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
        if(isLockExists) {
            if(serverDataBean!=null && !lockBean.getSessionId().equals(serverDataBean.getSessionId())) {
                isLockExists = false;
            }
        }
        /** check whether lock exists or not. If not and the mode of the
         *proposal is not display then lock the proposal else show the message
         */
        if(isLockExists && !lockBean.getMode().equals(CoeusLiteConstants.DISPLAY_MODE)){
            lockModule(lockBean, request);
            session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(),
            new Boolean(true));
        }else{
            if(sessionLockBean == null && !lockBean.getMode().equals(CoeusLiteConstants.DISPLAY_MODE)){
                showLockingMessage(lockBean.getModuleNumber(), request);
                isSuccess = false;
            } else if(sessionLockBean!=null && serverDataBean!=null) {
                if(!lockBean.getSessionId().equals(serverDataBean.getSessionId())) {
                    showLockingMessage(lockBean.getModuleNumber(), request);
                    isSuccess = false;
                }
            }
        }
        return isSuccess;
        
    }
    
    /** Prepare the Locking messages when other or same user locked
     *the same module number. Make server call to get the message for the
     *locked user
     *@param String moduleNumber
     *@throws Exception
     */
    protected void showLockingMessage(String moduleNumber, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        String lockId = CoeusLiteConstants.BUDGET_LOCK_STR+moduleNumber;
        WebTxnBean webTxnBean = new WebTxnBean();
        LockBean serverLockedBean = getLockedData(lockId,request);
        if(serverLockedBean!= null){
            serverLockedBean.setModuleKey(CoeusLiteConstants.BUDGET_MODULE);
            serverLockedBean.setModuleNumber(moduleNumber);
            //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-start
            UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("user"+request.getSession().getId());
            String loggedInUserId = userInfoBean.getUserId();
//            CoeusFunctions coeusFunctions = new CoeusFunctions();
            //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-end
//            String acqLock = "acquired_lock";
//            ActionMessages messages = new ActionMessages();
//            messages.add("acqLock", new ActionMessage(acqLock,
//            serverLockedBean.getUserName(),serverLockedBean.getModuleKey(),
//            serverLockedBean.getModuleNumber()));
             // Added for displaying user name - user id start
            String lockUserId = serverLockedBean.getUserId();
            //UserMaintDataTxnBean userTxnBean = new UserMaintDataTxnBean();
            //String lockUserName = userTxnBean.getUserName(lockUserId);
            String lockUserName = EMPTY_STRING;
            String acqLock = "acquired_lock";
            ActionMessages messages = new ActionMessages();
            //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-start
            lockUserName =  viewRestrictionOfUser(loggedInUserId,lockUserId);
//            String displayLockName = coeusFunctions.getParameterValue(CoeusConstants.DISPLAY_LOCKNAME_PROP_BUDGET);
//            if("Y".equalsIgnoreCase(displayLockName.trim()) || lockUserId.equalsIgnoreCase(loggedInUserId)){
//                lockUserName=lockUserName;
//            }else{
//                lockUserName = CoeusConstants.lockedUsername;
//            }
            //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-end
            messages.add("acqLock", new ActionMessage(acqLock,
                lockUserName,serverLockedBean.getModuleKey(),
                    serverLockedBean.getModuleNumber()));
            //End
            saveMessages(request, messages);
            session.removeAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());
        }
    }
    
    /** Update the sync flag if and only if the proposal is in hierarchy
     *@param request and proposalNumber
     *@thorws Exception
     */
    protected void updateProposalSyncFlags(HttpServletRequest request, String proposalNumber)
    throws Exception{
        Map hmHierarchyData = getParentProposalData(request,proposalNumber);
        boolean isHierarchy = ((Boolean)hmHierarchyData.get("IN_HIERARCHY")).booleanValue();
        if(isHierarchy){
            updateSyncFlag(request,proposalNumber);
        }
    }
    
    /** This method will be invoked when the proposal is trying to open in 
     *any given mode except new mode. This method will say whether the given
     *proposal number is in hierarchy or not and also, whether the proposal is
     *parent or child if and only if it is in hierarchy
     *@param request, proposalNumber
     *@throws Exception
     */
    protected Map getParentProposalData(HttpServletRequest request, String proposalNumber) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap mapData = new HashMap();
        mapData.put("proposalNumber", proposalNumber);
        ProposalHierarchyTxnBean txnBean = new ProposalHierarchyTxnBean();
        HashMap hmData = txnBean.getParentProposalData(proposalNumber);
        return hmData;
    } 
    
    /** This method is used to update the child or parent proposal sync flag
     *in OSP$EPS_PROPOSAL_HIERARCHY. This will be called only when the 
     *proposal is in hierarchy which is modified and saved
     *@param, Request, proposalNumber
     *throws Exception
     */
    protected void updateSyncFlag(HttpServletRequest request, String proposalNumber) throws Exception{
        HttpSession session = request.getSession();
        final String flag = "B";
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        WebTxnBean webTxnBean = new WebTxnBean();
        Map mapInput = new HashMap();
        mapInput.put("proposalNumber",proposalNumber);
        mapInput.put("userId",userInfoBean.getUserId());
        mapInput.put("flag", flag);
        try {
        webTxnBean.getResults(request,"updateSyncFlag", mapInput);
         }catch(Exception ex) {
             UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(), ex, "BudgetBaseAction", "updateSyncFlag()");
        }
    }    
    
    /**
     * Method to get the cost sharing distribution from the procedure DW_GET_EPS_PROP_COST_SHARING
     * @param hmdetails instance of HashMap
     * @param proposalNumber instance of String
     * @param request instance of Request
     * @param versionNumber instance of int
     * @throws Exception if exception occur
     * @return instance of Vector containing all cost sharing distribution 
     */
    protected Vector getCostSharing(HttpServletRequest request,
            HashMap hmdetails,String proposalNumber,int versionNumber) throws Exception{
        WebTxnBean  webTxnBean = new WebTxnBean();
        hmdetails.put("proposalNumber",proposalNumber);
        hmdetails.put("versionNumber",new Integer(versionNumber));
        Vector vecCostSharing = new Vector();

        try {
            Hashtable htGetDetails = (Hashtable)webTxnBean.getResults(request,"getCostSharingDistribution",hmdetails);
             vecCostSharing =  (Vector)htGetDetails.get("getCostSharingDistribution");
         }catch(Exception ex) {
             UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(), ex, "BudgetBaseAction", "getCostSharing()");
        }
        return  vecCostSharing =  vecCostSharing !=null && vecCostSharing.size() >0 ? vecCostSharing : new Vector();
    }
    
    /**
     * Method for getting under recovery distribution from the procedure
     * @param proposal Number 
     * @param versionNumber
     * @param request instance of Request
     * @throws Exception if exception occur
     * @return vecUnderRec instance of List containing the UR distribution
     */
    protected List getUnderRecovery(HttpServletRequest request,String proposalNumber,int versionNumber) throws Exception{
        HashMap hmRecoveryDet = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        hmRecoveryDet.put("proposalNumber",proposalNumber);
        hmRecoveryDet.put("versionNumber",new Integer(versionNumber));
        Vector vecUnderRec = new Vector();

        try {
            Hashtable htGetDetails = (Hashtable)webTxnBean.getResults(request,"getUnderRecoveryDistribution",hmRecoveryDet);
             vecUnderRec  = (Vector)htGetDetails.get("getUnderRecoveryDistribution");
         }catch(Exception ex) {
             UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(), ex, "BudgetBaseAction", "getUnderRecovery()");
        }
        return  vecUnderRec =  vecUnderRec !=null && vecUnderRec.size() >0 ? vecUnderRec : new Vector();
        
    }
    
    //Added for case#2776 - Allow concurrent Prop dev access in Lite - start
    /**
     * This method checks whether a given proposal is in hierarchy or not
     * @param request HttpServletRequest
     * @return isPropInHierarchy boolean
     * @throws Exception   
     */
    protected boolean isProposalInHierarchy(HttpServletRequest request) throws Exception{
        boolean isPropInHierarchy = false;        
        HashMap hmProposalHeader = new HashMap(); 
        EPSProposalHeaderBean proposalHeaderBean = null;
        WebTxnBean webTxnBean = new WebTxnBean();  
        HttpSession session = request.getSession();
        String proposalNumber = request.getParameter("proposalNumber");
        if(proposalNumber == null || proposalNumber.equals("")){
            proposalNumber = (String)session.getAttribute("proposalNumber");
        }
        hmProposalHeader.put("proposalNumber", proposalNumber);

        try {
        Hashtable htProposalHeader = (Hashtable)webTxnBean.getResults(request, "getProposalHeaderData", hmProposalHeader);
        Vector vecProposalHeader = (Vector)htProposalHeader.get("getProposalHeaderData");
        if(vecProposalHeader != null && vecProposalHeader.size() > 0){
            proposalHeaderBean = (EPSProposalHeaderBean)vecProposalHeader.elementAt(0);
            if(proposalHeaderBean.getIsHierarchy().equals("Y")){
                isPropInHierarchy = true;
            }            
        }
         }catch(Exception ex) {
             UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(), ex, "BudgetBaseAction", "isProposalInHierarchy()");
        }
        return isPropInHierarchy;        
    }
    
    /**
     * This method gets proposal status code and description for a given proposal number
     * @param request HttpServletRequest
     * @return hmProposalStatus HashMap
     * @throws Exception
     */
    protected HashMap getProposalStatus(HttpServletRequest request) throws Exception{
        HashMap hmProposalHeader = new HashMap(); 
        HashMap hmProposalStatus = new HashMap();
        EPSProposalHeaderBean proposalHeaderBean = null;
        WebTxnBean webTxnBean = new WebTxnBean();  
        HttpSession session = request.getSession();
        String proposalNumber = request.getParameter("proposalNumber");
        if(proposalNumber == null || proposalNumber.equals("")){
            proposalNumber = (String)session.getAttribute("proposalNumber");
        }
        hmProposalHeader.put("proposalNumber", proposalNumber);

        try {
            Hashtable htProposalHeader = (Hashtable)webTxnBean.getResults(request, "getProposalHeaderData", hmProposalHeader);
            Vector vecProposalHeader = (Vector)htProposalHeader.get("getProposalHeaderData");
            if(vecProposalHeader != null && vecProposalHeader.size() > 0){
                proposalHeaderBean = (EPSProposalHeaderBean)vecProposalHeader.elementAt(0);
                hmProposalStatus.put("propStatusCode", proposalHeaderBean.getProposalStatusCode());
                hmProposalStatus.put("propStatusDesc", proposalHeaderBean.getProposalStatusDescription());
            }
         }catch(Exception ex) {
             UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(), ex, "BudgetBaseAction", "getProposalStatus()");
        }
        return hmProposalStatus;
    }
    //Added for case#2776 - Allow concurrent Prop dev access in Lite - end
    
    /**
     * This method gets proposal status code and description for a given proposal number
     * @param request HttpServletRequest
     * @return hmProposalStatus HashMap
     * @throws Exception
     */
    protected HashMap getProposalStatus(String proposalNumber, HttpServletRequest request) throws Exception{
        HashMap hmProposalHeader = new HashMap(); 
        HashMap hmProposalStatus = new HashMap();
        EPSProposalHeaderBean proposalHeaderBean = null;
        WebTxnBean webTxnBean = new WebTxnBean();  
        HttpSession session = request.getSession();
        if(proposalNumber == null || proposalNumber.equals("")){
            proposalNumber = (String)session.getAttribute("proposalNumber");
        }
        hmProposalHeader.put("proposalNumber", proposalNumber);

        try {
            Hashtable htProposalHeader = (Hashtable)webTxnBean.getResults(request, "getProposalHeaderData", hmProposalHeader);
            Vector vecProposalHeader = (Vector)htProposalHeader.get("getProposalHeaderData");
            if(vecProposalHeader != null && vecProposalHeader.size() > 0){
                proposalHeaderBean = (EPSProposalHeaderBean)vecProposalHeader.elementAt(0);
                hmProposalStatus.put("propStatusCode", proposalHeaderBean.getProposalStatusCode());
                hmProposalStatus.put("propStatusDesc", proposalHeaderBean.getProposalStatusDescription());
            }
         }catch(Exception ex) {
             UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(), ex, "BudgetBaseAction", "getProposalStatus()");
        }
        return hmProposalStatus;
    }

    /**
     * Added for Case#2341 - Recalculate Budget if Project dates change
     * To validate that the budget dates are equal to project dates
     * @param request HttpServletRequest
     * @throws java.lang.Exception 
     * @return boolean
     */
    protected boolean validateForProjectDates(HttpServletRequest request) throws Exception{
        boolean changeNeeded = false;
        HttpSession session = request.getSession();
        HashMap hmProposalHeader = new HashMap();
        EPSProposalHeaderBean proposalHeaderBean = null;
        WebTxnBean webTxnBean = new WebTxnBean();
        String proposalNumber = (String) request.getSession().getAttribute("proposalNumber"+session.getId());
        hmProposalHeader.put("proposalNumber", proposalNumber);

        try {
            Hashtable htProposalHeader = (Hashtable)webTxnBean.getResults(request, "getProposalHeaderData", hmProposalHeader);
            Vector vecProposalHeader = (Vector)htProposalHeader.get("getProposalHeaderData");
            session.setAttribute("getProposalHeaderData", vecProposalHeader);
            BudgetInfoBean budgetInfoBean = (BudgetInfoBean) session.getAttribute("budgetInfoBean");
            if(vecProposalHeader != null && vecProposalHeader.size() > 0){
                proposalHeaderBean = (EPSProposalHeaderBean)vecProposalHeader.elementAt(0);
                java.sql.Date proposalStartDate =  proposalHeaderBean.getProposalStartDate();
                java.sql.Date proposalEndDate = proposalHeaderBean.getProposalEndDate();
                java.sql.Date budgetStartDate =  budgetInfoBean.getStartDate();
                java.sql.Date budgetEndDate =  budgetInfoBean.getEndDate();
                //Modified for Case#2341-Recalculate Budget if Project dates change	 - Start
    //            // Check if budget start date with in the proposal start date else show the error message
    //            if(budgetStartDate != null &&
    //                    budgetStartDate.before(proposalStartDate)){
                if(budgetStartDate != null &&
                        ( budgetStartDate.compareTo(proposalStartDate) < 0
                            || budgetStartDate.compareTo(proposalStartDate) > 0 )){
                    changeNeeded = true;
                    // Check if budget start date with in the proposal end date else show the error message
    //            }else if(budgetEndDate != null &&
    //                    budgetEndDate.after(proposalEndDate)){
                   }else if(budgetEndDate != null &&
                        ( budgetEndDate.compareTo(proposalEndDate) < 0
                            || budgetEndDate.compareTo(proposalEndDate) > 0 )){
                    changeNeeded = true;
                }
                //Modified for Case#2341-Recalculate Budget if Project dates change	 - End
            }
         }catch(Exception ex) {
             UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(), ex, "BudgetBaseAction", "validateForProjectDates()");
        }
        return changeNeeded;
    }
    
    
    //Added for Case 3197 - Allow for the generation of project period greater than 12 months -start
    /**
     * Calculate the number of months between the periods start date and end date.     *
     */
    protected Vector calculatePeriodNumberOfMonths(Vector  cvBudgetPeriods) throws Exception {
        DynaValidatorForm dynaProposalDetailForm  = null;
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        if(cvBudgetPeriods !=null && cvBudgetPeriods.size() > 0){
            for (int index = 0; index < cvBudgetPeriods.size(); index++) {
                dynaProposalDetailForm = (DynaValidatorForm) cvBudgetPeriods.get(index);
                double noOfMonths = budgetDataTxnBean.getNumberOfMonths( (java.sql.Date) dynaProposalDetailForm.get("startDate"), (java.sql.Date) dynaProposalDetailForm.get("endDate"));
                dynaProposalDetailForm.set("noOfPeriodMonths", new Double(noOfMonths));
            }
        }
        return cvBudgetPeriods;
    }
    
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
    //Access modifier changed from private to protected with COEUSDEV 209-Issue with save and apply to later period.
    protected Date getValidPersonDateforPeriod(Date periodStartDate, Date periodEndDate, Date startDate, Date endDate, 
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
            
            
            
        } // If it is a single year period.
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
         finalDate = calendar.getTime();
        //Modified for COEUSQA-3422 -End         
         
         // Added for COEUSDEV-794 : End of the month period date not recognized when the first period is on a Leap year -Start
        // Check whether the person Date is come under leap year, if it is leap year reduce one day.
        if(isDateLeapYear(finalDate)){
            finalDate = adjustDateForLeapYear(finalDate);
        }
        // Added for COEUSDEV-794 -End
         
        // Added for COEUSDEV-309  Adjust period boundaries and generate all periods is not copiying personnel items correctly -start 
        // If it is a single year period, check if person end is beyond the period end date, Apply Period End Date as Person end date. 
        if(vecPossibleYears == null || vecPossibleYears.isEmpty() && endDate != null){
            
            if(periodStartDate.compareTo(finalDate) > 0 && periodEndDate.compareTo(finalDate) > 0) {
                 calendar.setTime(periodEndDate);
                 calendar.set(Calendar.YEAR, year);
                 finalDate = calendar.getTime();
            }
        }
       
//        return calendar.getTime();
        return finalDate;
    // Added for case COEUSDEV-309 -end
    }
    
    /*
     * This method is used to check the Valid Period year based on period end date.
     */
    
    private int getValidPeriodEndYear(Vector vecPossibleYears, Date periodStartDate, Date periodEndDate, Date personOldStartDate, Date personOldEndDate){
//        DateUtils dtUtils = new DateUtils();
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
                int daysDiff = DateUtils.calculateDateDiff(2, mergePeriodDate(personOldStartDate, validPersonStartYear) , mergePeriodDate(personOldEndDate, endYear));
                hmValidEndYear.put(new Integer(daysDiff), new Integer(endYear));
            }
        }
        //Get the Number of days difference for Orginal Person Start and End date.
        int personNoOfDaysDiff = DateUtils.calculateDateDiff(2, personOldStartDate,personOldEndDate ) ;
        
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
    //Added for Case 3197 - Allow for the generation of project period greater than 12 months -End
    //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-start
    /**
     * This method is to username of the locked user, if the DISPLAY_LOCKNAME_PROP_BUDGET
     * is set to 'Y' then current lock user naem will be displayed, else it will disply 
     * 'Another user'.
     * @param loggedInUserId
     * @param lockUserId
     * @return currenatLockUserName
     */
     protected String viewRestrictionOfUser(String loggedInUserId, String lockUserId) throws DBException, CoeusException {
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        UserMaintDataTxnBean userTxnBean = new UserMaintDataTxnBean();
        String currenatLockUserName = userTxnBean.getUserName(lockUserId);
        //DISPLAY_LOCKNAME_PROP_BUDGET is Taking from the parameter table
        String displayLockName = coeusFunctions.getParameterValue(CoeusConstants.DISPLAY_LOCKNAME_PROP_BUDGET);
        // If the value for DISPLAY_LOCKNAME_PROP_BUDGET is set 'Y' or if loginned user has lock,ot will display the lock user name
            if(displayLockName != null && "Y".equalsIgnoreCase(displayLockName.trim()) || lockUserId.equalsIgnoreCase(loggedInUserId)){
                currenatLockUserName = currenatLockUserName;
            }else{
                currenatLockUserName = CoeusConstants.lockedUsername;
            }
        return currenatLockUserName;
    }
    //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-end

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
    protected Date getPersonEndDateforPeriod(Date periodStartDate, Date personOldStartDate, Date personOldEndDate, Date personNewStartDate){
        
        //Get the Number of days difference for Orginal Person Start and End date.
        int personNoOfDaysDiff = DateUtils.calculateDateDiff(5, personOldStartDate,personOldEndDate ) ;
        
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
        Date perEndDate = DateUtils.dateAdd(Calendar.DATE, periodStartDate, personNoOfDaysDiff);
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
        Date perEndDate = DateUtils.dateAdd(Calendar.DATE, periodStartDate, personNoOfDaysDiff);
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
    
//   
    
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
        
        Date perEndDate = DateUtils.dateAdd(Calendar.DATE, periodStartDate, personNoOfDaysDiff);
        
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
    
    // Added for COEUSQA-3044 Added Personnel type Cost Element does not calculate in Lite - start
    /**
     * This method is used for to calculate the line item salary for given period 
     * Initialize the Budget calculator and set the latest data from the hashtable
     * passed by the calling action. Calculate for the specific period
     * @return hashtable
     * @param data data for calculation
     * @param budgetPeriod budget period
     * @param budgetInfoBean budgetinfobean
     * @throws Exception if exception occur
     */
    protected Hashtable calculateSalary(Hashtable data, int budgetPeriod,
    BudgetInfoBean budgetInfoBean) throws Exception{
        BudgetCalculator budgetCalculator = null;
        
        QueryEngine queryEngine = QueryEngine.getInstance();
        String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        
        queryEngine.addDataCollection(queryKey,data);
        //Initialize budget calculator
        budgetCalculator = new BudgetCalculator(budgetInfoBean);
        budgetCalculator.setKey(queryKey);
             
        //This method is used for to calculate each line item salary for the period.
        budgetCalculator.calculateSalaryForBudgetPeriod(budgetPeriod);
                
        Hashtable htData = queryEngine.getDataCollection(queryKey);
        return htData;
    }
    // Added for COEUSQA-3044 -end

    //COEUSQA-1689 Role Restrictions for Budget Rates - Start
    /**
     *
     * For the given ProposalNumber     
     * check user has proposal rates
     * @return boolean value for right
     * @param request for txnBean
     * @param proposalNumber for bean
     * @throws Exception if exception
     */
    protected boolean checkPropsalRatesRight(HttpServletRequest request ,
    String proposalNumber)throws Exception{
        HttpSession session = request.getSession();
        boolean hasRight= false;
        HashMap hmProposalHeader = new HashMap(); 
        WebTxnBean webTxnBean = new WebTxnBean();
        EPSProposalHeaderBean proposalHeaderBean = null;
        
        hmProposalHeader.put("proposalNumber", proposalNumber);
        Hashtable htProposalHeader = (Hashtable)webTxnBean.getResults(request, "getProposalHeaderData", hmProposalHeader);
        Vector vecProposalHeader = (Vector)htProposalHeader.get("getProposalHeaderData");
        if(vecProposalHeader != null && vecProposalHeader.size() > 0){
            proposalHeaderBean = (EPSProposalHeaderBean)vecProposalHeader.elementAt(0);
        }
        
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String loggedinUser = userInfoBean.getUserId();            
        String unitNumber = proposalHeaderBean.getLeadUnitNumber();
                
        //Check user has MODIFY_PROPOSAL_RATES right at proposal
        hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, MODIFY_PROPOSAL_RATES);
        if(!hasRight){
            //Check user has MODIFY_PROPOSAL_RATES right at unit level
            hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_ANY_PROPOSAL_RATES, unitNumber);
        }
        
        return hasRight ;
    }
    //COEUSQA-1689 Role Restrictions for Budget Rates - End
    
    //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
    /**
     *
     * For the given ProposalNumber     
     * check user has rights to view institutional salaries
     * @return boolean value for right
     * @param request for txnBean
     * @param proposalNumber for bean
     * @throws Exception if exception
     */
    protected boolean checkViewInstitutionalSalariesRight(HttpServletRequest request ,
    String proposalNumber, String appointmentPersonId)throws Exception{
        HttpSession session = request.getSession();
        boolean hasRight= false;
        String unitNumber = null;
        HashMap hmProposalHeader = new HashMap(); 
        WebTxnBean webTxnBean = new WebTxnBean();
        /*EPSProposalHeaderBean proposalHeaderBean = null;
        
        hmProposalHeader.put("proposalNumber", proposalNumber);
        Hashtable htProposalHeader = (Hashtable)webTxnBean.getResults(request, "getProposalHeaderData", hmProposalHeader);
        Vector vecProposalHeader = (Vector)htProposalHeader.get("getProposalHeaderData");
        if(vecProposalHeader != null && vecProposalHeader.size() > 0){
            proposalHeaderBean = (EPSProposalHeaderBean)vecProposalHeader.elementAt(0);
        } 
        
        String unitNumber = proposalHeaderBean.getUnitNumber();*/
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String loggedinUser = userInfoBean.getUserId();
        Vector vecPersonInfo = new Vector();
        //If the userInfoBean is not null then fetch the person details
        if(appointmentPersonId!=null && appointmentPersonId.trim().length()>0){
            //If the person id is not empty then fetch the person details
            vecPersonInfo = userMaintDataTxnBean.getPersonInfo(appointmentPersonId);
        }
        //To fetch the last value which is home unit
        int decrementor = 1;
        int lastIndex = vecPersonInfo.size()-decrementor;
        //To fetch the home unit
        if(vecPersonInfo!=null && vecPersonInfo.size()>0){
            unitNumber = (String)vecPersonInfo.get(lastIndex);
        }else{
            unitNumber = EMPTY_STRING;
        }
        //Check user has VIEW_PROP_PERSON_INST_SALARIES right at proposal
        hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser,proposalNumber,"VIEW_PROP_PERSON_INST_SALARIES");
        if(!hasRight){
            //Check user has VIEW_INSTITUTIONAL_SALARIES right at unit level
            hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser,"VIEW_INSTITUTIONAL_SALARIES",unitNumber);
        }

        return hasRight ;
    }
    //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
    
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
        personDate = DateUtils.dateAdd(Calendar.DATE, personDate, -1);
        return personDate;
    }

    //Added for COEUSDEV-794 - End
    
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
    /**
     * Method to fetch the cost element status from server
     * @param vecCostElements
     * @ returns boolean value
     */
     protected Vector getCostElementDetails(int budgetPeriod,Vector vecFilterBudgetDetailToShow) throws DBException, CoeusException {
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        Vector inActivecostElements = new Vector();
        Vector vecCostElementMessages = new Vector();
        StringBuffer errMsg;
        //remove duplicate cost elements from the vector vecCostElements
        if(vecFilterBudgetDetailToShow != null && !vecFilterBudgetDetailToShow.isEmpty()){
            HashSet hmCostElements = new HashSet();
            for(Object costElementDetails : vecFilterBudgetDetailToShow){
                DynaActionForm costElementForm = (DynaActionForm)costElementDetails;
                String budgetPeriodCE = costElementForm.get("budgetPeriod").toString();
                String costElement = costElementForm.get("costElement").toString();
                if(budgetPeriodCE.equals(budgetPeriod+"")){
                    hmCostElements.add(costElement);
                }
            }
            if(!hmCostElements.isEmpty()){
                Iterator finalCostIterator = hmCostElements.iterator();
                while(finalCostIterator.hasNext()){
                    String costElement = (String)finalCostIterator.next();
                    CostElementsBean  costElementsBean = budgetDataTxnBean.getCostElementsDetails(costElement);
                    if("N".equals(costElementsBean.getActive())){
                        inActivecostElements.addElement(costElement);
                        inActivecostElements.addElement(costElementsBean.getDescription());
                        errMsg = new StringBuffer();
                        String costElementDesc = costElementsBean.getDescription();
                        errMsg.append("Cost Element ");
                        errMsg.append(costElement);
                        errMsg.append(":");
                        errMsg.append(costElementDesc);
                        errMsg.append(" is no longer active. Please select an alternative");
                        vecCostElementMessages.add(errMsg);
                    }
                }
            }
        }
        return vecCostElementMessages;
    }    
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
}


