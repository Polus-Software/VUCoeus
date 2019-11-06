/*
 * BudgetVersionsAction.java
 *
 * Created on 19 May 2006, 14:57
 */

/**
 * PMD check performed, and commented unused imports and variables on 24-AUG-2011
 * by Bharati Umarani
 */
package edu.wmc.coeuslite.budget.action;

import edu.mit.coeus.bean.UserInfoBean;
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
import edu.mit.coeus.budget.bean.BudgetPersonnelDetailsRateBaseBean;
import edu.mit.coeus.budget.bean.BudgetPersonsBean;
import edu.mit.coeus.budget.bean.BudgetRateBaseBean;
import edu.mit.coeus.budget.bean.BudgetUpdateTxnBean;
import edu.mit.coeus.budget.bean.CostElementsBean;
import edu.mit.coeus.budget.bean.ProjectIncomeBean;
import edu.mit.coeus.budget.bean.ProposalLARatesBean;
import edu.mit.coeus.budget.bean.ProposalRatesBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.DataType;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.GreaterThan;
import edu.mit.coeus.utils.query.QueryEngine;
//import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.wmc.coeuslite.budget.bean.ProposalBudgetHeaderBean;
import edu.wmc.coeuslite.budget.bean.ReadXMLData;
import edu.mit.coeuslite.utils.CoeusDynaBeansList;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
//import org.apache.struts.action.ActionMessage;
//import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author  mohann
 */
public class BudgetVersionsAction extends BudgetBaseAction  {
    public static final String EMPTY_STRING = "";
    private static final String GET_BUDGET_VERSIONS = "/getBudgetVersions";
    private static final String YES = "Y";
    private static final String NO = "N";
    private static final String SAVE = "Save";
    private static final String COPY = "Copy";
    private static final String ADD_NEW_VERSION = "/addNewVersion" ;
    private static final String COPY_BUDGET_VERSION = "/copyBudgetVersion";
    private static final String OPEN_BUDGET_VERSION = "/openBudgetVersion";
    public static final int COPY_ONE_PERIOD_ONLY = 1;
    public static final int COPY_ALL_PERIODS = 2;
    private static final String BUDGET_MESSAGE_FILE_NAME = "BudgetMessages";
    private static final String BUDGET_VERSIONS_XML_PATH = "BUDGET_VERSIONS_XML_PATH";
    private static final String ACTIVITY_TYPE_CHANGED  = "activityTypeChanged";
    private static final String GET_BUDGET_SYNC_PERSON  ="/getSyncPersons";
    private static final String VALIDATION = "BudgetValidation";
    private static final String SAVE_VALIDATE = "SaveAfterValidate";
    //    private ActionForward actionForward = null;
    //    private WebTxnBean webTxnBean ;
    //    private HttpServletRequest request;
    //    private HttpServletResponse response;
    //    private ActionMapping actionMapping;
    //    private HttpSession session;
    //    private Timestamp dbTimestamp;
    //    private UserInfoBean userInfoBean;
    //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
    private boolean isBudgetSaved = false;
    //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end
  
    /** Creates a new instance of BudgetVersionsAction */
    public BudgetVersionsAction() {
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
    
    public org.apache.struts.action.ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {
        //        this.actionMapping  = actionMapping;
        //        this.request = request;
        //        this.response =response;
        //        this.session = request.getSession();
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        HashMap hmRequiredDetails = new HashMap();
        request.setAttribute("PAGE","V");
        if(actionMapping.getPath().equals(ADD_NEW_VERSION)){
            //noting
        }else if(actionMapping.getPath().equals(OPEN_BUDGET_VERSION)){
            //nothing
        }else if(actionMapping.getPath().equals(GET_BUDGET_SYNC_PERSON)){
            //nothing
        }else{
            DynaValidatorForm dynaForm = (DynaValidatorForm)actionForm;
            hmRequiredDetails.put(DynaValidatorForm.class,dynaForm);
        }
        
        
        
        hmRequiredDetails.put(ActionMapping.class,actionMapping);
        hmRequiredDetails.put(ActionForm.class, actionForm);
        
        getBudgetVersionMenus(request);
        ActionForward actionForward = performBudgetVersionsAction(actionMapping,hmRequiredDetails,request, response);
        return actionForward;
    }
    
    /**
     * This method will identify which request is comes from which path and
     * navigates to the respective ActionForward
     * @return ActionForward object
     * @param hmRequiredDetails
     * @param actionMapping
     * @throws Exception
     */
    private ActionForward performBudgetVersionsAction(ActionMapping actionMapping,
    HashMap hmRequiredDetails, HttpServletRequest request, HttpServletResponse response) throws Exception{
        String navigator = "lockPage";
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        ActionForward actionForward = null;
        String mode = (String)session.getAttribute(CoeusLiteConstants.MODE+request.getSession().getId());
        mode = mode!=null ? mode : EMPTY_STRING;
        ProposalBudgetHeaderBean headerBean = (ProposalBudgetHeaderBean)session.getAttribute("ProposalBudgetHeaderBean");
        String proposalNumber = headerBean.getProposalNumber();
        String budgetStatus =(String) session.getAttribute("budgetStatusCode");
        if( budgetStatus == null || budgetStatus.equals(EMPTY_STRING)){
            budgetStatus = request.getParameter("BudgetStatus");
        }

        String calculate = (String)request.getParameter("calculate");
        String versionNumber = (String)request.getParameter("versionNumber");
        int oldAcivityType =0;
        int activityTypeChange =0;
        
        //Commeneted and added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
        
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
        /*boolean allowCopy = getCopyDetails(proposalNumber, versionNumber,request);
        request.setAttribute("allowCopy", new Boolean(allowCopy));*/
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
     
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        int budgetPeriodOne = 1;
        int budgetPeriodAll = 2;
        int inactivTypeForBudPeriodOne = 0;
        int inactivTypeForBudPeriodAll = 0;
        if(versionNumber!=null){
            inactivTypeForBudPeriodOne = budgetDataTxnBean.isBudgeCopytHasInactiveATAndPT(proposalNumber, Integer.parseInt(versionNumber) , budgetPeriodOne);
            inactivTypeForBudPeriodAll = budgetDataTxnBean.isBudgeCopytHasInactiveATAndPT(proposalNumber, Integer.parseInt(versionNumber) , budgetPeriodAll);
        }       
        String messageKeyForPerOne = Integer.toString(inactivTypeForBudPeriodOne);
        request.setAttribute("inactiv_type_for_budget_one", messageKeyForPerOne);
        
        String messageKeyForPerAll = Integer.toString(inactivTypeForBudPeriodAll);
        request.setAttribute("inactive_type_for_budget_all", messageKeyForPerAll);
        //Commented and added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end
        if( (proposalNumber !=null && !proposalNumber.equals(EMPTY_STRING)) && (versionNumber !=null && !versionNumber.equals(EMPTY_STRING))){
            
            // Added for Cost Sharing Distribution Validation - start
            // for a particular version of budget is chosen
            boolean forceCostSharing = false;
            String forceValidation = "notForce";
            String validationMsg = " ";
            HashMap hmGetParamValue = new HashMap();
            hmGetParamValue.put("forceCostSharingDistribution","FORCE_COST_SHARING_DISTRIBUTION");
            Hashtable htCostSharing = (Hashtable)webTxnBean.getResults(request,"getForceCostSharing",hmGetParamValue);
            hmGetParamValue =(HashMap) htCostSharing.get("getForceCostSharing");
            String forceCSD = (String)hmGetParamValue.get("ls_value");

            LinkedHashMap lhmVersionFlag = new LinkedHashMap();
            HashMap hmCostSharing = new HashMap();
            Vector vecCostSharingDistriData = null;
            HashMap hmPropData = new HashMap();
            hmPropData.put("proposalNumber",proposalNumber);
            Hashtable htBudgetVersionsDetail = (Hashtable)webTxnBean.getResults(request, "getBudgetVersionsDetails",hmPropData);
            Vector vecBudgetVersions = (Vector)htBudgetVersionsDetail.get("getBudgetVersionsForProposal");
            if(vecBudgetVersions != null && vecBudgetVersions.size()>0 ){
                for(int count = 0; count < vecBudgetVersions.size(); count++){
                    DynaValidatorForm dynaCostSharing = (DynaValidatorForm)vecBudgetVersions.get(count);
                    double totalCostSharing = ((Double)dynaCostSharing.get("costSharingAmount")).doubleValue();
                    vecCostSharingDistriData =  getCostSharing(request,hmCostSharing,proposalNumber,((Integer)dynaCostSharing.get("versionNumber")).intValue());
                    if(vecCostSharingDistriData != null){
                        if(vecCostSharingDistriData.size() <= 0){
                                if(!forceCSD.equals(EMPTY_STRING) && forceCSD != null && forceCSD.equals("1")){
                                    // Modified for case COEUSQA-2784_Lite is forcing users to distribute underrecovery when the proposal has negative underrecovery_start
                                    if(totalCostSharing <= 0){
                                    // Modified for case COEUSQA-2784_Lite is forcing users to distribute underrecovery when the proposal has negative underrecovery_end    
                                        forceCostSharing = false;
                                        forceValidation = "notForce"; 
                                    }else{
                                        forceCostSharing = true;
                                        forceValidation = "force";
                                        validationMsg = "noCSD";
                                    }
                                }else{
                                    forceCostSharing = false;
                                    forceValidation = "notForce";
                                }
                        }
                        else{
                            double costSharingTotal = 0.0;
                            for(int ind = 0; ind < vecCostSharingDistriData.size(); ind++){
                                DynaValidatorForm dynaCstSharing = (DynaValidatorForm)vecCostSharingDistriData.get(ind);
                                costSharingTotal = costSharingTotal + ((Double)dynaCstSharing.get("costSharingAmount")).doubleValue();
                            }
                            //COEUSDEV-159 Lite - cost share warning -won't allow complete status in lite
                            costSharingTotal = ((double)Math.round(costSharingTotal*Math.pow(10.0, 2) )) / 100;
                            //COEUSDEV-159 End
                            if(costSharingTotal != totalCostSharing){
                                if(!forceCSD.equals(EMPTY_STRING) && forceCSD != null && forceCSD.equals("1")){
                                forceCostSharing = true;
                                forceValidation = "force";
                                validationMsg = "amtUnequal";
                                } 
                               else{
                                    forceCostSharing = false;
                                    forceValidation = "notForce";
                               }

                            }else{
                                    forceCostSharing = false;
                                    forceValidation = "notForce";
                            }
                        
                        }
                    }//vecCostSharingDistriData not null
                    lhmVersionFlag.put(((Integer)dynaCostSharing.get("versionNumber")),forceValidation);
                }//count for loop
            }//  vecBudgetVersions size  if
            
            session.setAttribute("forceCSDInVersions",lhmVersionFlag);
            session.setAttribute("CSDVersionsValidationMsg",validationMsg);
        // Added for Cost Sharing Distribution Validation - end
            
        // Added for Under Recovery Distribution - start
            
                boolean forceUnderRec = false;
                String validationURDMsg = " ";
                LinkedHashMap lhmURDVersionFlag = new LinkedHashMap();
                //if  the value for the parameter "FORCE_UNDER_RECOVERY_DISTRIBUTION" is zero then no validation 
                //is fired when the selected version(which does not have Under Recovery Distribution or when the 
                //total cost sharing across the periods does not match) is set as "complete" and "final"
                //if the value is "1" then a validation should be fired when the selected version(which does not have Under Recovery Distribution or when the 
                //total cost sharing across the periods does not match)is selected as "complete" and "final"
                
                hmGetParamValue.put("forceUnderRecoveryDistribution","FORCE_UNDER_RECOVERY_DISTRIBUTION");
                Hashtable htUnderRec = (Hashtable)webTxnBean.getResults(request,"getForceUnderRecovery",hmGetParamValue);
                hmGetParamValue =(HashMap) htUnderRec.get("getForceUnderRecovery");
                String forceURD = (String)hmGetParamValue.get("ls_value");
                            

                Vector vecUnderRecoveryData = null;
                if(vecBudgetVersions != null && vecBudgetVersions.size()>0 ){
                for(int count = 0; count < vecBudgetVersions.size(); count++){
                    DynaValidatorForm dynaUnderRec = (DynaValidatorForm)vecBudgetVersions.get(count);
                    double totalUnderRec = ((Double)dynaUnderRec.get("underRecoveryAmount")).doubleValue();
                    vecUnderRecoveryData =  (Vector)getUnderRecovery(request,proposalNumber,((Integer)dynaUnderRec.get("versionNumber")).intValue());
                    if(vecUnderRecoveryData != null){
                        if(vecUnderRecoveryData.size() <= 0){
                                if(!forceURD.equals(EMPTY_STRING) && forceURD != null && forceURD.equals("1")){
                                    // Modified for case COEUSQA-2784_Lite is forcing users to distribute underrecovery when the proposal has negative underrecovery_start
                                    if(totalUnderRec <= 0){
                                    // Modified for case COEUSQA-2784_Lite is forcing users to distribute underrecovery when the proposal has negative underrecovery_end    
                                        forceUnderRec = false;
                                        forceValidation = "notForce";
                                    }else{
                                        forceUnderRec = true;
                                        forceValidation = "force";
                                        validationURDMsg = "noURD";
                                    }
                                }else{
                                    forceUnderRec = false;
                                    forceValidation = "notForce";
                                }
                        }
                        else{
                            double underRecoveryTotal = 0.0;
                            for(int ind = 0; ind < vecUnderRecoveryData.size(); ind++){
                                DynaValidatorForm dynaUnderRecovery = (DynaValidatorForm)vecUnderRecoveryData.get(ind);
                                underRecoveryTotal = underRecoveryTotal + ((Double)dynaUnderRecovery.get("underRecoveryAmt_wmc")).doubleValue();
                            }
                            //COEUSDEV-159 Lite - cost share warning -won't allow complete status in lite
                            underRecoveryTotal = ((double)Math.round(underRecoveryTotal*Math.pow(10.0, 2) )) / 100;
                            //COEUSDEV-159 End
                            if(underRecoveryTotal != totalUnderRec){
                                if(!forceURD.equals(EMPTY_STRING) && forceURD != null && forceURD.equals("1")){
                                forceUnderRec = true;
                                forceValidation = "force";
                                validationURDMsg = "amtUnequal";
                                } 
                               else{
                                    forceUnderRec = false;
                                    forceValidation= "notForce";
                               }
                                
                            }
                            else{
                                    forceUnderRec = false;
                                    forceValidation= "notForce";
                            }
                        }
                    }//vecUnderRecoveryData not null
                    lhmURDVersionFlag.put(((Integer)dynaUnderRec.get("versionNumber")),forceValidation);
                    session.setAttribute("URDVersionsValidationMsg",validationURDMsg);
                }//count for loop
            }//  vecBudgetVersions size  if
            
            session.setAttribute("forceURDInVersions",lhmURDVersionFlag);
        // Added for Under Recovery Distribution - End
            
            CoeusVector cvActivityType = checkActivityTypeChanged(request, proposalNumber, Integer.parseInt(versionNumber));
            
            if(cvActivityType !=null && cvActivityType.size() >0){
                oldAcivityType = Integer.parseInt((String)cvActivityType.get(0).toString());
                activityTypeChange = Integer.parseInt((String)cvActivityType.get(1).toString());
                //3681: Message about proposal's activity type changing - Start
                request.setAttribute("oldActivityType", String.valueOf(oldAcivityType));
                // 3681: Message about proposal's activity type changing - End
            }
        }
        
        if(actionMapping.getPath().equals(GET_BUDGET_VERSIONS)){
            DynaValidatorForm dynaForm = (DynaValidatorForm)hmRequiredDetails.get(DynaValidatorForm.class);
            
            /*******  Added for New Selected Menu List  Start **********/
            
            Map mapMenuList = new HashMap();
            mapMenuList.put("menuItems",CoeusliteMenuItems.BUDGET_VERSION_MENU_ITEMS);
            mapMenuList.put("menuCode",CoeusliteMenuItems.BUDGET_VERSION_CODE);
            setSelectedMenuList(request, mapMenuList);
            
            /*******  Added for New Selected Menu List  End **********/
            
            String page = request.getParameter("actionFrom");
            if( page == null){
                navigator = getBudgetVersionsDetails(proposalNumber,dynaForm,request);
                dynaForm.set("budgetStatusCode",session.getAttribute("budgetStatusCode"));
                //Added for COEUSQA-2546  Two users can modify a budget at same time. Locking not working -Start
                // If Lock exist and budget status is complete, set the mode as Display
                boolean isBudgetLocked = isLockExist(proposalNumber,request);
                //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
                //If budget is complete and its saved then set budgetStatusMode for display
                if(isBudgetLocked && dynaForm.get("budgetStatusCode")!=null && dynaForm.get("budgetStatusCode").equals("C") && isBudgetSaved){
                    session.setAttribute(CoeusLiteConstants.MODE+session.getId(),"display");
                }
                //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end
                //Added for COEUSQA-2546 -End
            } else if(page.trim().equals(SAVE)){
                if(dynaForm.get("budgetStatusCode")!=null && dynaForm.get("budgetStatusCode").equals("I")){
                    String budgetStatusMode = (String) session.getAttribute("budgetStatusMode");
                    session.setAttribute("budgetStatusMode", "incomplete");
                    if(getLock(request)) {
                        updateBudgetFinalVersion(dynaForm,request);
                        navigator = getBudgetVersionsDetails(proposalNumber,dynaForm,request);                 
                    } else {
                        session.setAttribute("budgetStatusMode", budgetStatusMode);
                    }
                } else if(dynaForm.get("budgetStatusCode")!=null && dynaForm.get("budgetStatusCode").equals("C")){
                    updateBudgetFinalVersion(dynaForm,request);
                    navigator = getBudgetVersionsDetails(proposalNumber,dynaForm,request);
                    LockBean lockBean =  (LockBean)request.getSession().getAttribute(CoeusLiteConstants.LOCK_BEAN+request.getSession().getId());
                    if(lockBean!= null) {
                        LockBean serverDataBean = getLockedData(CoeusLiteConstants.PROTOCOL_LOCK_STR+lockBean.getModuleNumber(), request);
                        if(!isLockExists(lockBean, lockBean.getModuleKey()) &&
                        (serverDataBean==null || lockBean.getSessionId().equals(serverDataBean.getSessionId()))){
                            releaseLock(lockBean, request);
                            request.setAttribute(CoeusLiteConstants.RELEASE_LOCK, EMPTY_STRING);
                        }
                    }
                }
            } else if(page.trim().equals(COPY)){
                navigator = "copyBudgetVersion";
                dynaForm.set("copyPeriodCode","1");
                //Added with case 2158 Budgetary Validations Start
            }else if(page.trim().equals(VALIDATION)){
                //update status in versions screen
                String statusChange = (String)request.getParameter("statusChange");
                if(statusChange!=null && "C".equals(statusChange.trim())){
                    dynaForm.set("budgetStatusCode",statusChange);
                    request.setAttribute("newBudgetStatus",statusChange);
                }
                //update final version in version screen
                String newVersionNo   = (String)request.getParameter("versionNumber");
                String currentVersion = (String)session.getAttribute("budgetFinalVersionValue");
                if(newVersionNo!=null && !EMPTY_STRING.equals(newVersionNo.trim())){
                    dynaForm.set("finalVersionValue",newVersionNo);
                    int finalIndex = Integer.parseInt(newVersionNo)-1;
                    Vector vecBudgetVersions =(Vector) session.getAttribute("budgetVersionsData");
                    if(vecBudgetVersions!=null){       
                        DynaActionForm dynaFinalVersionForm = null;
                        for(int i =0; i<vecBudgetVersions.size(); i++){
                            dynaFinalVersionForm = (DynaActionForm)vecBudgetVersions.elementAt(i);
                            if(i==finalIndex){
                                dynaFinalVersionForm.set("finalVersionFlag",YES);
                            }else{
                                dynaFinalVersionForm.set("finalVersionFlag",NO);
                            }
                        }            
                    }
                    if(!newVersionNo.equals(currentVersion)){
                        request.setAttribute("versionUpdated","Y");
                    }
                }
            }else if(page.trim().equals(SAVE_VALIDATE)){
                
                dynaForm.set("proposalNumber", proposalNumber);
                String statusChange = (String)request.getParameter("statusChange");
                dynaForm.set("budgetStatusCode",statusChange);
                String newVersionNo = (String)request.getParameter("versionNumber");
                dynaForm.set("finalVersionValue",newVersionNo);
                updateBudgetFinalVersion(dynaForm,request);
            }  
            //Budget Validation End
        } else if(actionMapping.getPath().equals(GET_BUDGET_SYNC_PERSON)){
            navigator = "success";
        } else if(actionMapping.getPath().equals(OPEN_BUDGET_VERSION)){
            //check for activityTypeChange if equal to 1 it doesnt modified
            CoeusDynaBeansList coeusSyncPersonsDynaList = (CoeusDynaBeansList) hmRequiredDetails.get(ActionForm.class);
            DynaActionForm budgetStatusDynaForm = coeusSyncPersonsDynaList.getDynaForm(request,"budgetSummary");
            if (budgetStatus == null || budgetStatus.equals(EMPTY_STRING) ){
                budgetStatus =(String) budgetStatusDynaForm.get("budgetStatusCode");
            }
            String multipleAppointmentExist = request.getParameter("multipleAppointmentExist");
            String syncPersonsExist = request.getParameter("syncPersonsExist");
            if( activityTypeChange ==1 || (calculate !=null && !calculate.equals(EMPTY_STRING)) && !mode.equals("display") ){
                navigator =  performOpenBudgetVersion(calculate,oldAcivityType,hmRequiredDetails,request,response);
                //Added for Case#2341 - Recalculate Budget if Project dates change - starts
                session.setAttribute("CHECK_BUDGET_DATES", "CHECK_BUDGET_DATES");
                if(validateForProjectDates(request)){
                    request.setAttribute("CHANGE_BUDGET_DATES", "CHANGE_BUDGET_DATES");
                }
                //Added for Case#2341 - Recalculate Budget if Project dates change - ends
            }else if( activityTypeChange == -1 && budgetStatus.equals("I") && !mode.equals("display") &&
            multipleAppointmentExist == null && syncPersonsExist == null ) {
                navigator = ACTIVITY_TYPE_CHANGED;
                request.setAttribute("pageNavigate", "OpenBudgetVersion");
            }else if(budgetStatus.equals("C") || mode.equals("display")) {
                navigator =  performOpenBudgetVersion(calculate,oldAcivityType,hmRequiredDetails,request,response);
            }else if(activityTypeChange == -1 && (multipleAppointmentExist !=null || syncPersonsExist !=null)){
                navigator =  performOpenBudgetVersion(calculate,oldAcivityType,hmRequiredDetails,request,response);
            }
        } else if(actionMapping.getPath().equals(ADD_NEW_VERSION)){
            
            CoeusDynaBeansList coeusSyncPersonsDynaList = (CoeusDynaBeansList) hmRequiredDetails.get(ActionForm.class);
            DynaActionForm budgetStatusDynaForm = coeusSyncPersonsDynaList.getDynaForm(request,"budgetSummary");
            if (budgetStatus == null || budgetStatus.equals(EMPTY_STRING) ){
                budgetStatus =(String) budgetStatusDynaForm.get("budgetStatusCode");
            }
            if(budgetStatusDynaForm.get("budgetStatusCode").equals(EMPTY_STRING)){
                budgetStatusDynaForm.set("budgetStatusCode",budgetStatus);
            }
                       
            // update the budget status before create a new version
            updateBudgetStatus(request,budgetStatusDynaForm);
            String multipleAppointmentExist = request.getParameter("multipleAppointmentExist");
            String syncPersonsExist = request.getParameter("syncPersonsExist");
            //check for activityTypeChange if equal to 1 it doesnt modified
            if( activityTypeChange ==1 ||(calculate !=null && !calculate.equals(EMPTY_STRING)) ){
                
                // check for sync persons if no multiple appointments or not valid jobcode create a new version
                if( multipleAppointmentExist == null  && syncPersonsExist == null ){
                    navigator =  addNewVersion(hmRequiredDetails,calculate,oldAcivityType,request);
                }
            } // if equal to -1 it is modified
            else if( activityTypeChange == -1 && budgetStatus.equals("I") &&
            (multipleAppointmentExist == null  && syncPersonsExist == null) ){
                navigator = ACTIVITY_TYPE_CHANGED;
                request.setAttribute("pageNavigate", "NewBudgetVersion");
                return actionForward = actionMapping.findForward(navigator);
            }
            
            /************* Check Sync Budget Persons - Start  ******************/
            if( (mode !=null && !mode.equals("display")) && (session.getAttribute("budgetStatusMode")!=null &&
            !session.getAttribute("budgetStatusMode").equals("complete" ))) {
                session.setAttribute("syncPageNavigate", "addNewVersion");
                String functionType ="MODIFY";
                BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
                navigator =   checkBudgetSyncPersons(request,budgetInfoBean,hmRequiredDetails,functionType);
                if(navigator !=null && !navigator.equals(EMPTY_STRING)){
                    return actionForward = actionMapping.findForward(navigator);
                }
                saveBudgetSyncPersons(request);
                session.removeAttribute("syncPageNavigate");
            }
            //Check salary for persons if 0 navigate to personnel page or 1 navigate to budget summary page
            int salaryCount = checkSalaryBudgetPerson(request, proposalNumber, Integer.parseInt(versionNumber));
            if(salaryCount ==0){
                //Added for Case#2341 - Recalculate Budget if Project dates change
                session.setAttribute("CHECK_BUDGET_DATES", "CHECK_BUDGET_DATES");
                navigator = "budgetPersons";
            }else{
                //Added for Case#2341 - Recalculate Budget if Project dates change
                session.setAttribute("CHECK_BUDGET_DATES", "CHECK_BUDGET_DATES");
                String url =  "/getBudgetSummary.do?versionNumber=";
                url += versionNumber;
                RequestDispatcher rd = request.getRequestDispatcher(url);
                rd.forward(request,response);
                return null ;
            }
            /* *************** Check Sync Budget Persons - End  ***************/
            
        }else if(actionMapping.getPath().equals(COPY_BUDGET_VERSION)){
            String budgetStatusMode = (String) session.getAttribute("budgetStatusMode");
            session.setAttribute("budgetStatusMode", "incomplete");
            //int budgetPeriodNumber = 1;
            if(getLock(request)) {
                if(hmRequiredDetails!=null && hmRequiredDetails.size() > 0) {
                    DynaValidatorForm dynaValidatorForm =(DynaValidatorForm) hmRequiredDetails.get(ActionForm.class);
                    dynaValidatorForm.set("budgetStatusCode", "I");
                    dynaValidatorForm.set("proposalNumber", proposalNumber);            
                    updateBudgetStatus(dynaValidatorForm,request);
                }
                
                //check for activityTypeChange if equal to 1 it doesnt modified
                if(activityTypeChange ==1 ||(calculate !=null && !calculate.equals(EMPTY_STRING)) ){
                    navigator =  performCopyBudgetVersion(calculate,oldAcivityType,request);
                                 
                }  // if equal to -1 it is modified
                else if(activityTypeChange == -1 ){
                    navigator = ACTIVITY_TYPE_CHANGED;
                    request.setAttribute("pageNavigate", "CopyBudgetVersion");
                    request.setAttribute("copyPeriod", (String)request.getParameter("copyPeriod"));
                }
            } else {
                session.setAttribute("budgetStatusMode", budgetStatusMode);
                navigator = "lockPage";
            }
        }
        
        return actionForward = actionMapping.findForward(navigator);
    }
    /**
     * This method will perform if budget persons exist open the budget
     * navigates to the respective ActionForward
     * @return ActionForward object
     * @throws Exception
     */
    private String performOpenBudgetVersion(String calculate,int oldAcivityType,HashMap hmRequiredDetails, HttpServletRequest request, HttpServletResponse response)throws Exception {
        String navigator = EMPTY_STRING;
        boolean isSuccessFulModified = false;
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        ProposalBudgetHeaderBean headerBean = (ProposalBudgetHeaderBean)session.getAttribute("ProposalBudgetHeaderBean");
        String proposalNumber = headerBean.getProposalNumber();
        int versionNumber = Integer.parseInt(request.getParameter("versionNumber"));
        String unitNumber = EMPTY_STRING;
        Vector vecProposalDetails = getProposalDetailsForBudget( request , proposalNumber);
        
        if(vecProposalDetails!=null && vecProposalDetails.size() > 0){
            DynaActionForm dynaProposalDetailForm = (DynaActionForm)vecProposalDetails.elementAt(0);
            unitNumber = (String)dynaProposalDetailForm.get("unitNumber");
        }
        
        BudgetInfoBean budgetInfoBean = new BudgetInfoBean();
        budgetInfoBean = getBudgetInfoBeanforProposal(proposalNumber, versionNumber, request);
        budgetInfoBean.setActivityTypeCode(oldAcivityType);
        budgetInfoBean.setUnitNumber(unitNumber);        
        //Added for removing instance variable -case # 2960 - start
        QueryEngine queryEngine = QueryEngine.getInstance();
        String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        //Added for removing instance variable - case # 2960- end
        //get the budget Final version value and set it to budgetInfoBean
        Vector vecBudgetVersionData = (Vector) session.getAttribute("budgetVersionsData");
        if(vecBudgetVersionData!=null && vecBudgetVersionData.size()>0 && versionNumber<=vecBudgetVersionData.size()){
            DynaActionForm dynaFinalVersionForm = (DynaActionForm)vecBudgetVersionData.elementAt(versionNumber-1);
            String finalVersionValue = (String)dynaFinalVersionForm.get("finalVersionFlag");
            if(finalVersionValue != null && finalVersionValue.equals("Y")){
                budgetInfoBean.setFinalVersion(true);
            }
        }
        
        //if calculate is Yes set the new activity type to budgetInfoBean
        if( (calculate !=null && !calculate.equals(EMPTY_STRING)) &&  calculate.equals("Y")){
            int proposalActivityType = getActivityTypeForProposal(request,proposalNumber);
            int oldActivityTypeCode = budgetInfoBean.getActivityTypeCode();
            budgetInfoBean.setActivityTypeCode(proposalActivityType);
            Hashtable htBudgetData = initializeBudgetCalculator(budgetInfoBean);
            queryEngine.addDataCollection(queryKey, htBudgetData);
            Equals eqOldATCode = new Equals("activityCode", new Integer(oldActivityTypeCode));
            CoeusVector cvPropRates = (CoeusVector)htBudgetData.get(ProposalRatesBean.class);
            //System.out.println("ProposalRatesBean Size >>"+cvPropRates.size());
            queryEngine.setUpdate(queryKey, ProposalRatesBean.class, "acType", String.class, TypeConstants.DELETE_RECORD, eqOldATCode);
            
            CoeusVector cvNewProposalRates = getProposalRatesBean(budgetInfoBean);
            //System.out.println("New ProposalRatesBean Size >>"+cvNewProposalRates.size());
            if(cvNewProposalRates != null){
                for(int index = 0 ; index < cvNewProposalRates.size(); index++){
                    ProposalRatesBean proposalRatesBean = (ProposalRatesBean)cvNewProposalRates.get(index);
                    proposalRatesBean.setAw_ActivityTypeCode(oldActivityTypeCode);
                    queryEngine.insert(queryKey,proposalRatesBean);
                }
            }
            htBudgetData.remove(BudgetInfoBean.class);
            CoeusVector cvBudgetInfo = new CoeusVector();
            cvBudgetInfo.addElement(budgetInfoBean);
            htBudgetData.put(BudgetInfoBean.class, cvBudgetInfo);
            htBudgetData = calculateAllPeriods(htBudgetData, budgetInfoBean);
            BudgetUpdateTxnBean budgetUpdateTxnBean = new BudgetUpdateTxnBean(userInfoBean.getUserId());
            isSuccessFulModified = budgetUpdateTxnBean.addUpdDeleteBudget(htBudgetData);
        }
        session.setAttribute("budgetInfoBean", budgetInfoBean );
        //version number added to header bean - start
        headerBean.setVersionNumber(versionNumber);
        session.setAttribute("ProposalBudgetHeaderBean", headerBean);
        //added to display version number in the header other than versions page- start
        request.setAttribute("PAGE","");
        //added to display version number in the header other than versions page - end
        // version number added to header bean -  end
        
        /************* Check Sync Budget Persons - Start  ******************/
        String mode =(String) session.getAttribute("mode"+session.getId());
        if( (mode !=null && !mode.equals("display")) && (session.getAttribute("budgetStatusMode")!=null &&
        !session.getAttribute("budgetStatusMode").equals("complete" ))) {
            session.setAttribute("syncPageNavigate", "OpenBudgetVersion");
            String functionType ="MODIFY";
            navigator =   checkBudgetSyncPersons(request,budgetInfoBean,hmRequiredDetails,functionType);
            if(navigator !=null && !navigator.equals(EMPTY_STRING)){
                return navigator;
            }
            
            saveBudgetSyncPersons(request);
            session.removeAttribute("syncPageNavigate");
        }
        //Check salary for persons if 0 navigate to personnel page or 1 navigate to budget summary page
        int salaryCount = checkSalaryBudgetPerson(request, proposalNumber, versionNumber);
        if(salaryCount ==0){
            //Added for Case#2341 - Recalculate Budget if Project dates change
            session.setAttribute("CHECK_BUDGET_DATES", "CHECK_BUDGET_DATES");
            navigator = "budgetPersons";
        }else{
            //Added for Case#2341 - Recalculate Budget if Project dates change
            session.setAttribute("CHECK_BUDGET_DATES", "CHECK_BUDGET_DATES");
            String url =  "/getBudgetSummary.do?versionNumber=";
            url += versionNumber;
            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request,response);
            return null ;
        }
        /* *************** Check Sync Budget Persons - End  ***************/
        
        return navigator;
    }
    
    /**
     * This method get the Budget Status and Versions details for a particular proposal number
     * returns ActionForward object
     * @param dynaForm
     * @param proposalNumber
     * @throws Exception
     * @return navigator
     */
    private String getBudgetVersionsDetails(String proposalNumber,DynaValidatorForm dynaForm, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmPropData = new HashMap();
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        hmPropData.put("proposalNumber",proposalNumber);
        if(proposalNumber!=null && !proposalNumber.equals(EMPTY_STRING)){
            Hashtable htBudgetVersionsDetail =
            (Hashtable)webTxnBean.getResults(request, "getBudgetVersionsDetails",hmPropData);
            Vector vecBudgetVersions =
            (Vector)htBudgetVersionsDetail.get("getBudgetVersionsForProposal");
            // Added for Cost Sharing Distribution Validation - Start
            // for budget which has more than one version
                boolean forceCostSharing = false;
                String forceValidation = "notForce";
                String validationMsg = "noCSD";
                LinkedHashMap lhmVersionFlag = new LinkedHashMap();
                //if  the value for the parameter "FORCE_COST_SHARING_DISTRIBUTION" is zero then no validation 
                //is fired when the selected version(which does not have cost sharing distribution or when the 
                //total cost sharing across the periods does not match) is set as "complete" and "final"
                //if the value is "1" then a validation should be fired when the selected version(which does not have cost sharing distribution or when the 
                //total cost sharing across the periods does not match)is selected as "complete" and "final"
                HashMap hmGetParamValue = new HashMap();
                hmGetParamValue.put("forceCostSharingDistribution","FORCE_COST_SHARING_DISTRIBUTION");
                Hashtable htCostSharing = (Hashtable)webTxnBean.getResults(request,"getForceCostSharing",hmGetParamValue);
                hmGetParamValue =(HashMap) htCostSharing.get("getForceCostSharing");
                String forceCSD = (String)hmGetParamValue.get("ls_value");
                String finalVersionFlag = EMPTY_STRING;
                
                HashMap hmCostSharing = new HashMap();
                Vector vecCostSharingDistriData = null;
                if(vecBudgetVersions != null && vecBudgetVersions.size()>0 ){
                for(int count = 0; count < vecBudgetVersions.size(); count++){
                    DynaValidatorForm dynaCostSharing = (DynaValidatorForm)vecBudgetVersions.get(count);
                    double totalCostSharing = ((Double)dynaCostSharing.get("costSharingAmount")).doubleValue();
                    vecCostSharingDistriData =  getCostSharing(request,hmCostSharing,proposalNumber,((Integer)dynaCostSharing.get("versionNumber")).intValue());
                    if(vecCostSharingDistriData != null){
                        if(vecCostSharingDistriData.size() <= 0){
                                if(!forceCSD.equals(EMPTY_STRING) && forceCSD != null && forceCSD.equals("1")){
                                    // Modified for case COEUSQA-2784_Lite is forcing users to distribute underrecovery when the proposal has negative underrecovery_start
                                    if(totalCostSharing <= 0){
                                    // Modified for case COEUSQA-2784_Lite is forcing users to distribute underrecovery when the proposal has negative underrecovery_End    
                                        forceCostSharing = false;
                                        forceValidation = "notForce";
                                    }else{
                                        forceCostSharing = true;
                                        forceValidation = "force";
                                        validationMsg = "noCSD";
                                    }
                                }else{
                                    forceCostSharing = false;
                                    forceValidation = "notForce";
                                }
                        }
                        else{
                            double costSharingTotal = 0.0;
                            for(int ind = 0; ind < vecCostSharingDistriData.size(); ind++){
                                DynaValidatorForm dynaCstSharing = (DynaValidatorForm)vecCostSharingDistriData.get(ind);
                                costSharingTotal = costSharingTotal + ((Double)dynaCstSharing.get("costSharingAmount")).doubleValue();
                            }
                            //COEUSDEV-159 Lite - cost share warning -won't allow complete status in lite
                            costSharingTotal = ((double)Math.round(costSharingTotal*Math.pow(10.0, 2) )) / 100;
                            //COEUSDEV-159 End
                            if(costSharingTotal != totalCostSharing){
                                if(!forceCSD.equals(EMPTY_STRING) && forceCSD != null && forceCSD.equals("1")){
                                forceCostSharing = true;
                                forceValidation = "force";
                                validationMsg = "amtUnequal";
                                } 
                               else{
                                    forceCostSharing = false;
                                    forceValidation= "notForce";
                               }
                                
                            }
                            else{
                                    forceCostSharing = false;
                                    forceValidation= "notForce";
                            }
                        }
                    }//vecCostSharingDistriData not null
                    lhmVersionFlag.put(((Integer)dynaCostSharing.get("versionNumber")),forceValidation);
                    session.setAttribute("CSDVersionsValidationMsg",validationMsg);
                }//count for loop
            }//  vecBudgetVersions size  if
            
            session.setAttribute("forceCSDInVersions",lhmVersionFlag);
            // Added for Cost Sharing Distribution Validation - end
            
            // Under Recovery Distribution - Start
                boolean forceUnderRec = false;
                //String forceValidation = "notForce";
                String validationURDMsg = "noURD";
                LinkedHashMap lhmURDVersionFlag = new LinkedHashMap();
                //if  the value for the parameter "FORCE_UNDER_RECOVERY_DISTRIBUTION" is zero then no validation 
                //is fired when the selected version(which does not have Under Recovery Distribution or when the 
                //total cost sharing across the periods does not match) is set as "complete" and "final"
                //if the value is "1" then a validation should be fired when the selected version(which does not have Under Recovery Distribution or when the 
                //total cost sharing across the periods does not match)is selected as "complete" and "final"
                
                hmGetParamValue.put("forceUnderRecoveryDistribution","FORCE_UNDER_RECOVERY_DISTRIBUTION");
                Hashtable htUnderRec = (Hashtable)webTxnBean.getResults(request,"getForceUnderRecovery",hmGetParamValue);
                hmGetParamValue =(HashMap) htUnderRec.get("getForceUnderRecovery");
                String forceURD = (String)hmGetParamValue.get("ls_value");
                            

                Vector vecUnderRecoveryData = null;
                if(vecBudgetVersions != null && vecBudgetVersions.size()>0 ){
                for(int count = 0; count < vecBudgetVersions.size(); count++){
                    DynaValidatorForm dynaUnderRec = (DynaValidatorForm)vecBudgetVersions.get(count);
                    double totalUnderRec = ((Double)dynaUnderRec.get("underRecoveryAmount")).doubleValue();
                    vecUnderRecoveryData =  (Vector)getUnderRecovery(request,proposalNumber,((Integer)dynaUnderRec.get("versionNumber")).intValue());
                    if(vecUnderRecoveryData != null){
                        if(vecUnderRecoveryData.size() <= 0){
                                if(!forceURD.equals(EMPTY_STRING) && forceURD != null && forceURD.equals("1")){
                                    // Modified for case COEUSQA-2784_Lite is forcing users to distribute underrecovery when the proposal has negative underrecovery_start
                                    if(totalUnderRec <= 0){
                                    // Modified for case COEUSQA-2784_Lite is forcing users to distribute underrecovery when the proposal has negative underrecovery_end    
                                        forceUnderRec = false;
                                        forceValidation = "notForce";
                                    }else{
                                        forceUnderRec = true;
                                        forceValidation = "force";
                                        validationURDMsg = "noURD";
                                    }
                                }else{
                                    forceUnderRec = false;
                                    forceValidation = "notForce";
                                }
                        }
                        else{
                            double underRecoveryTotal = 0.0;
                            for(int ind = 0; ind < vecUnderRecoveryData.size(); ind++){
                                DynaValidatorForm dynaUnderRecovery = (DynaValidatorForm)vecUnderRecoveryData.get(ind);
                                underRecoveryTotal = underRecoveryTotal + ((Double)dynaUnderRecovery.get("underRecoveryAmt_wmc")).doubleValue();
                            }
                            //COEUSDEV-159 Lite - cost share warning -won't allow complete status in lite
                            underRecoveryTotal = ((double)Math.round(underRecoveryTotal*Math.pow(10.0, 2) )) / 100;
                            //COEUSDEV-159 End
                            if(underRecoveryTotal != totalUnderRec){
                                if(!forceURD.equals(EMPTY_STRING) && forceURD != null && forceURD.equals("1")){
                                forceUnderRec = true;
                                forceValidation = "force";
                                validationURDMsg = "amtUnequal";
                                } 
                               else{
                                    forceUnderRec = false;
                                    forceValidation= "notForce";
                               }
                                
                            }
                            else{
                                    forceUnderRec = false;
                                    forceValidation= "notForce";
                            }
                        }
                    }//vecUnderRecoveryData not null
                    lhmURDVersionFlag.put(((Integer)dynaUnderRec.get("versionNumber")),forceValidation);
                    session.setAttribute("URDVersionsValidationMsg",validationURDMsg);
                }//count for loop
            }//  vecBudgetVersions size  if
            
            session.setAttribute("forceURDInVersions",lhmURDVersionFlag);
             // Under Recovery Distribution - End
            
            Vector vecBudgetStatus =
            (Vector)htBudgetVersionsDetail.get("getBudgetStatusDetails");
            if(vecBudgetStatus!=null && vecBudgetStatus.size() >0 ){
                dynaForm = (DynaValidatorForm)vecBudgetStatus.get(0);
                String budgetStatus = (String)dynaForm.get("budgetStatusCode");
                dynaForm.set("budgetStatusCode",budgetStatus);
                session.setAttribute("budgetStatusCode",budgetStatus);
            }
            if(vecBudgetVersions !=null && vecBudgetVersions.size()>0) {
                session.setAttribute("budgetVersionsData", vecBudgetVersions);
            }
            // get the Budget Status using getBudgetFinalVersion
            getBudgetFinalVersion(proposalNumber,request);
            session.setAttribute("optionsBudgetStatus", getVecBudgetStatus());
            navigator = "success";
        }
        return navigator;
    }
    /**
     * This method get the Budget Status details for a particular proposal number
     * @param proposalNumber
     * @throws Exception
     */
    private void getBudgetFinalVersion(String proposalNumber, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmPropData = new HashMap();
        HttpSession session = request.getSession();
        hmPropData.put("proposalNumber",proposalNumber);
        Hashtable htBudgetStatusDetail =
        (Hashtable)webTxnBean.getResults(request, "getBudgetFinalVesionAndSponsorCode",hmPropData);
        HashMap hmFinalVersionExists = (HashMap)htBudgetStatusDetail.get("getBudgetFinalVersionDetail");
        String finalVersion = (String) hmFinalVersionExists.get("ll_version");
        Vector vecSponsorCode = ((Vector)htBudgetStatusDetail.get("getProposalSponsorCode"));
        if(vecSponsorCode!=null && vecSponsorCode.size()>0){
            DynaValidatorForm dynaForm =(DynaValidatorForm)vecSponsorCode.get(0);
            String sponsorCode=(String)dynaForm.get("sponsorCode");
            if(sponsorCode !=null && !sponsorCode.equals(EMPTY_STRING)) {
                HashMap hmSponsorCode = new HashMap();
                hmSponsorCode.put("sponsorCode",sponsorCode);
                Hashtable htSponsorName =
                (Hashtable)webTxnBean.getResults(request, "getSponsorName",hmSponsorCode);
                HashMap hmSponsorName = (HashMap)htSponsorName.get("getSponsorName");
                String sponsorName = (String) hmSponsorName.get("ls_name");
                request.setAttribute("sponsorCode",sponsorCode );
                request.setAttribute("sponsorName",sponsorName );
            }
        }
        session.setAttribute("budgetFinalVersionValue", finalVersion);
    }
    
    
    /**
     *This method is used to update the final version flag, version number and budget status
     * @param dynaForm
     * @throws Exception
     */
    private void updateBudgetFinalVersion(DynaValidatorForm dynaForm, HttpServletRequest request)throws Exception{
        Timestamp dbTimestamp = prepareTimeStamp();
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        DynaValidatorForm dynaValidatorForm=null;
        String uncheckedVersionNumber = (String)session.getAttribute("budgetFinalVersionValue");
        String versionNumber = (String) dynaForm.get("finalVersionValue");
        versionNumber=versionNumber.trim();
        //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = null;
        BudgetPersonsBean budgetPersonsBean = new BudgetPersonsBean();
        Set uniquePeriodType = new HashSet();
        Set uniqueAppointmentType = new HashSet();
        String periodType = null;
        String appointmentType = null;
        Vector inActivePeriodType = new Vector();
        Vector inActiveAppointmentType = new Vector();
        StringBuffer errMsg;
        Vector vecAppAndPeriodTypeMessages = new Vector();
        //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end
        Vector vecBudgetVersions =(Vector) session.getAttribute("budgetVersionsData");
        if(uncheckedVersionNumber!=null && !uncheckedVersionNumber.equals(EMPTY_STRING)) {
            if( uncheckedVersionNumber!=null && versionNumber.equals(EMPTY_STRING)) {
                for(int index = 0 ; index < vecBudgetVersions.size();index ++ ){
                    if(Integer.parseInt(uncheckedVersionNumber)-1==index) {
                        dynaValidatorForm =(DynaValidatorForm) vecBudgetVersions.elementAt(index);
                        dynaValidatorForm.set("finalVersionFlag",NO);
                        session.setAttribute("budgetFinalVersionValue", uncheckedVersionNumber);
                        dynaValidatorForm.set("newUpdateTimestamp",dbTimestamp.toString());
                        webTxnBean.getResults(request, "updBudgetFinalVersion" , dynaValidatorForm);
                    }
                }
            } else if(uncheckedVersionNumber.equals("0") && versionNumber !=null) {
                for(int index = 0 ; index < vecBudgetVersions.size();index ++ ){
                    if(Integer.parseInt(versionNumber)-1==index) {
                        boolean update_budget_version = false;
                        String proposalNumber = (String)dynaForm.get("proposalNumber");
                        dynaValidatorForm =(DynaValidatorForm) vecBudgetVersions.elementAt(index);
                        dynaValidatorForm.set("finalVersionFlag",YES);
                        session.setAttribute("budgetFinalVersionValue", versionNumber);
                        dynaValidatorForm.set("newUpdateTimestamp",dbTimestamp.toString());
                        if(versionNumber!=null){
                            update_budget_version = getInactiveTypeDetails(proposalNumber,versionNumber,request);
                            if(update_budget_version){
                                webTxnBean.getResults(request, "updBudgetFinalVersion" , dynaValidatorForm);
                            }
                        }else{
                            webTxnBean.getResults(request, "updBudgetFinalVersion" , dynaValidatorForm);
                        }
                    }
                }
            } else if(vecBudgetVersions !=null && vecBudgetVersions.size()>0 &&
                    !(uncheckedVersionNumber.equalsIgnoreCase(versionNumber))
                    && !(versionNumber.equals(EMPTY_STRING)) && !(uncheckedVersionNumber.equals("0")) ) {
                //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
                boolean update_budget_flag = false;
                String proposalNumber = (String)dynaForm.get("proposalNumber");
                ProposalBudgetHeaderBean headerBean = (ProposalBudgetHeaderBean)session.getAttribute("ProposalBudgetHeaderBean");
                proposalNumber = headerBean.getProposalNumber();
                int newVersionNumbert  = Integer.parseInt(versionNumber);
                //get periodType details for proposal budget
                CoeusVector cvBudgetPersonnelDetail = budgetDataTxnBean.getBudgetPersonnelDetail(proposalNumber,newVersionNumbert);
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
                        //Checking for the periodType status
                        if("N".equals(costElementsBean.getActive())){
                            inActivePeriodType.addElement(costElementsBean.getDescription());
                            errMsg = new StringBuffer();
                            String costElementDesc = costElementsBean.getDescription();
                            errMsg.append("Period type");
                            errMsg.append(costElementDesc);
                            errMsg.append(" is no longer active. Please select an alternative");
                            vecAppAndPeriodTypeMessages.add(errMsg);
                        }
                    }
                }
                //get the budget persons
                CoeusVector cvBudgetPersons = budgetDataTxnBean.getBudgetPersons(proposalNumber,newVersionNumbert);
                if(cvBudgetPersons!=null && cvBudgetPersons.size()>0){
                    for ( int appTypeIndex = 0; appTypeIndex < cvBudgetPersons.size(); appTypeIndex ++ ){
                        budgetPersonsBean = (BudgetPersonsBean)cvBudgetPersons.get(appTypeIndex);
                        appointmentType = budgetPersonsBean.getAppointmentType();
                        uniqueAppointmentType.add(appointmentType);
                    }
                }
                //holds inactive period type details
                if(!uniqueAppointmentType.isEmpty()){
                    Iterator iterator = uniqueAppointmentType.iterator();
                    while(iterator.hasNext()){
                        appointmentType = (String)iterator.next();
                        budgetPersonsBean = budgetDataTxnBean.getAppointmentTypeDetails(appointmentType);
                        //Checking for the periodType status
                        if("N".equals(budgetPersonsBean.getStatus())){
                            inActiveAppointmentType.addElement(budgetPersonsBean.getAppointmentType());
                            errMsg = new StringBuffer();
                            String newAppointmentType = (String)budgetPersonsBean.getAppointmentType();
                            errMsg.append("Appointment type");
                            errMsg.append(newAppointmentType);
                            errMsg.append(" is no longer active. Please select an alternative");
                            vecAppAndPeriodTypeMessages.add(errMsg);
                        }
                    }
                    //vector vecAppAndPeriodTypeMessages holds inactivetype(Both appointment type and period type) details
                    if(vecAppAndPeriodTypeMessages!=null && vecAppAndPeriodTypeMessages.size()>0){
                        session.setAttribute("inactiv_App_Per_Type_Messages",vecAppAndPeriodTypeMessages);
                    }else{
                        session.setAttribute("inactiv_App_Per_Type_Messages",null);
                        update_budget_flag = true;
                    }
                }
                //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end
                for(int index = 0 ; index < vecBudgetVersions.size();index ++ ){
                    boolean isModified = false;
                    boolean update_flag = true;
                    if(Integer.parseInt(versionNumber)-1==index) {
                        dynaValidatorForm =(DynaValidatorForm) vecBudgetVersions.elementAt(index);
                        dynaValidatorForm.set("finalVersionFlag",YES);
                        session.setAttribute("budgetFinalVersionValue", versionNumber);
                        isModified = true;
                    }
                    if(Integer.parseInt(uncheckedVersionNumber)-1==index) {
                        dynaValidatorForm =(DynaValidatorForm) vecBudgetVersions.elementAt(index);
                        dynaValidatorForm.set("finalVersionFlag",NO);
                        isModified = true;
                    }
                    if(isModified && update_budget_flag){
                        dynaValidatorForm.set("newUpdateTimestamp",dbTimestamp.toString());
                        webTxnBean.getResults(request, "updBudgetFinalVersion" , dynaValidatorForm);
                    }
                }
                //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
                //For existing data if user trying to save it wil give hard stop err msg if it holds inactive types.
            }else if(uncheckedVersionNumber!=null && versionNumber!=null){
                if((uncheckedVersionNumber.equalsIgnoreCase(versionNumber))){
                    boolean update_budget_status = false;
                    String proposalNumber = (String)dynaForm.get("proposalNumber");
                    update_budget_status = getInactiveTypeDetails(proposalNumber,versionNumber,request);
                }             
            }
            //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end
        }
        
       
        
        String budgetstatus =(String) session.getAttribute("budgetStatusCode");
        String selectedBudgetStatus = (String) dynaForm.get("budgetStatusCode");
        //        if( (budgetstatus !=null && !budgetstatus.equals(EMPTY_STRING)) &&
        //        (selectedBudgetStatus !=null && !selectedBudgetStatus.equals(EMPTY_STRING))) {
        if(!(budgetstatus.equalsIgnoreCase(selectedBudgetStatus))/* && update_budget_flag == false*/) {
            updateBudgetStatus(dynaForm,request);
        }
        
        //        }
    }
    
    /**
     * This method will add new version and check for budget persons
     * if exist open budget summary otherwise open budgetPersons
     * @param hmRequiredDetails
     * @throws Exception
     * @return navigator
     */
    private String addNewVersion(HashMap hmRequiredDetails, String calculate,int oldAcivityType, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        ProposalBudgetHeaderBean headerBean = (ProposalBudgetHeaderBean)session.getAttribute("ProposalBudgetHeaderBean");
        String proposalNumber = headerBean.getProposalNumber();
        int versionNumber = Integer.parseInt(request.getParameter("versionNumber"))+1;
        //CASE 2960: Added queryEngine instance after removing instance level variable 
        QueryEngine queryEngine = QueryEngine.getInstance();        
        // DynaValidatorForm dynaProposalDetailForm = null;
        DynaActionForm dynaProposalDetailForm = null;
        String navigator = EMPTY_STRING;
        Vector vecBudgetRights = null ;
        boolean hasRight = false;
        boolean isActivityChanged = false;
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        
        /* Get the Budget Info.Get the CoeusVector containing BudgetInfoBeans
         *Set the BudgetInfoBean for the selected Proposal Number and Version Number
         *to the session.
         */
        CoeusVector cvBudgetForProposal = getBudgetInfo(request , proposalNumber);
        Vector vecProposalDetails =  getProposalDetailsForBudget( request , proposalNumber);
        if(vecProposalDetails!=null && vecProposalDetails.size() > 0){
            dynaProposalDetailForm = (DynaActionForm)vecProposalDetails.get(0);
            dynaProposalDetailForm.set("activityTypeCode",new Integer(oldAcivityType).toString());
            dynaProposalDetailForm.set("proposalNumber",proposalNumber);
            
            // Check for activityType change if calculate is Yes then set the new activity type
            if( (calculate !=null && !calculate.equals(EMPTY_STRING)) &&  calculate.equals("Y")){
                int proposalActivityType = getActivityTypeForProposal(request,proposalNumber);
                dynaProposalDetailForm.set("activityTypeCode", EMPTY_STRING+proposalActivityType);
                isActivityChanged = true;
            }
            
            vecBudgetRights = checkBudgetRights(request , dynaProposalDetailForm);
        }
        hasRight = ((Boolean)vecBudgetRights.elementAt(0)).booleanValue();
        if(hasRight){
            if(cvBudgetForProposal != null || cvBudgetForProposal.size() > 0){
                CoeusVector cvBudgetInfo = getNewBudgetInfoData(hmRequiredDetails,dynaProposalDetailForm,versionNumber,request);
                dynaProposalDetailForm.set("versionNumber",new Integer(versionNumber));
                CoeusVector cvBudgetPeriods = performDefaultVersionAction(dynaProposalDetailForm);
                if(isActivityChanged){
                    //Get the new Rates and LA Rates based on activity type and unit number. Saved to database
                    updateBudget(request ,cvBudgetInfo , cvBudgetPeriods );
                }else{
                    //Get Rates and LA Rates from previous version and saved to based , open summary window
                    BudgetInfoBean budgetInfoBean = (BudgetInfoBean)cvBudgetInfo.get(0);
                      // Added for case#3654 - Third option 'Default' in the campus dropdown -Start
                    budgetInfoBean.setDefaultIndicator(true);
                    // Added for case#3654 - Third option 'Default' in the campus dropdown -End
                    Hashtable htBudgetData = initializeBudgetCalculator(budgetInfoBean);
                    //Added for removing instance variable -case # 2960 - start
                    String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
                    //Added for removing instance variable - case # 2960- end                                           
                    queryEngine.addDataCollection(queryKey, htBudgetData);
                    
                    Equals eqVersion = new Equals("versionNumber", new Integer(budgetInfoBean.getVersionNumber() - 1));
                    queryEngine.setUpdate(queryKey, ProposalRatesBean.class,
                    "acType", String.class, TypeConstants.INSERT_RECORD, eqVersion);
                    queryEngine.setUpdate(queryKey, ProposalRatesBean.class,
                    "versionNumber", DataType.getClass(DataType.INT), new Integer(budgetInfoBean.getVersionNumber()), eqVersion);
                    queryEngine.setUpdate(queryKey, ProposalLARatesBean.class,
                    "acType", String.class, TypeConstants.INSERT_RECORD, eqVersion);
                    queryEngine.setUpdate(queryKey, ProposalLARatesBean.class,
                    "versionNumber", DataType.getClass(DataType.INT), new Integer(budgetInfoBean.getVersionNumber()), eqVersion);
                    
                    htBudgetData.remove(BudgetInfoBean.class);
                    htBudgetData.put(BudgetInfoBean.class, cvBudgetInfo);
                    
                    htBudgetData.remove(BudgetPeriodBean.class);
                    htBudgetData.put(BudgetPeriodBean.class, cvBudgetPeriods);
                    htBudgetData = calculateAllPeriods(htBudgetData, budgetInfoBean);
                    
                    BudgetUpdateTxnBean budgetUpdateTxnBean = new BudgetUpdateTxnBean(userInfoBean.getUserId());
                    // this has to be updated using Transactions.xml way
                    budgetUpdateTxnBean.addUpdDeleteBudget(htBudgetData);
                    removeQueryEngineCollection(queryKey);
                }
                
            }
        }
        
        BudgetInfoBean budgetInfoBean = new BudgetInfoBean();
        budgetInfoBean = getBudgetInfoBeanforProposal(proposalNumber, versionNumber, request);
        session.setAttribute("budgetInfoBean", budgetInfoBean );
        // version number added to header bean - start
        headerBean.setVersionNumber(versionNumber);
        session.setAttribute("ProposalBudgetHeaderBean", headerBean);
        //added to display version number in the header other than versions page - start
        request.setAttribute("PAGE","");
        //added to display version number in the header other than versions page - end
        //version number added to header bean -  end
        return navigator ;
    }
    
    /**
     * This method will copy the selected version and check for budget persons
     * if exist open budget summary otherwise open budgetPersons
     * @throws Exception
     * @return navigator
     */
    private String performCopyBudgetVersion(String calculate,int oldAcivityType,HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        ProposalBudgetHeaderBean headerBean = (ProposalBudgetHeaderBean)session.getAttribute("ProposalBudgetHeaderBean");
        String proposalNumber = headerBean.getProposalNumber();
        int versionNumber = Integer.parseInt(request.getParameter("versionNumber"));
        String navigator = EMPTY_STRING;
        String unitNumber = EMPTY_STRING;
        Vector vecProposalDetails = getProposalDetailsForBudget( request , proposalNumber);
        if(vecProposalDetails!=null && vecProposalDetails.size() > 0){
            DynaValidatorForm dynaProposalDetailForm = (DynaValidatorForm)vecProposalDetails.elementAt(0);
            unitNumber = (String)dynaProposalDetailForm.get("unitNumber");
        }
        
        /* Get the Budget Info.Get the CoeusVector containing BudgetInfoBeans
         *Set the BudgetInfoBean for the selected Proposal Number and Version Number
         *to the session.
         */
        CoeusVector cvBudgetForProposal = getBudgetInfo(request , proposalNumber);
        BudgetInfoBean budgetInfoBean  = null;
        budgetInfoBean = filterBudgetInfo(request,cvBudgetForProposal,versionNumber);
        budgetInfoBean.setActivityTypeCode(oldAcivityType);
        budgetInfoBean.setUnitNumber(unitNumber);
        // //if calculate is Yes set the new activity type to budgetInfoBean
        boolean isActivityChanged = false;
        if( (calculate !=null && !calculate.equals(EMPTY_STRING)) &&  calculate.equals("Y")){
            int proposalActivityType = getActivityTypeForProposal(request,proposalNumber);
            budgetInfoBean.setActivityTypeCode(proposalActivityType);
            isActivityChanged = true;
        }
        int newVersionNumber = 1;
        if(cvBudgetForProposal != null && cvBudgetForProposal.size() >= 1){
            newVersionNumber = cvBudgetForProposal.size() + 1;
        }
        CoeusVector cvBudgetInfo = copyBudget(newVersionNumber, budgetInfoBean,isActivityChanged, request);
        boolean isCopied = false;
        if(cvBudgetInfo != null && cvBudgetInfo.size() > 0){
            isCopied = ((Boolean)cvBudgetInfo.get(0)).booleanValue();
            budgetInfoBean = (BudgetInfoBean)cvBudgetInfo.get(1);
        }
        if(isCopied){
            
            Vector vecBudgetPersonnel = getBudgetPersonsData(proposalNumber, newVersionNumber, request);
            if(vecBudgetPersonnel == null || vecBudgetPersonnel.size()== 0){
                navigator = "budgetPersons";
            }else{
                navigator = "budgetSummary";
            }
            request.setAttribute("versionNumber", new Integer(newVersionNumber));
            session.setAttribute("budgetInfoBean", budgetInfoBean);
            //version number added to header bean - start
            headerBean.setVersionNumber(newVersionNumber);
            session.setAttribute("ProposalBudgetHeaderBean", headerBean);
            //added to display version number in the header  other than versions page- start
            request.setAttribute("PAGE","");
            //added to display version number in the header other than versions page- end
            //version number added to header bean - end
        }            
        return navigator ;
    }
    
    /**
     *To get the Budget Persons Data
     * @param proposalNumber
     * @param versionNumber
     * @throws Exception
     * @return Vector
     */
    private Vector getBudgetPersonsData(String proposalNumber,int versionNumber, HttpServletRequest request)throws Exception{
        HashMap hmBudgetPersonnel = new HashMap();
        hmBudgetPersonnel.put("proposalNumber",proposalNumber);
        hmBudgetPersonnel.put("versionNumber",new Integer(versionNumber));
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htBudgetPersonnel =
        (Hashtable)webTxnBean.getResults(request, "getBudgetPersonsData", hmBudgetPersonnel);
        Vector vecBudgetPersonnel = (Vector)htBudgetPersonnel.get("getBudgetPersonsData");
        return vecBudgetPersonnel;
    }
    /**
     * Default will create the periods based on Budget start date and budget end
     * date.It spanns the start date and end date by one year and break up of year
     * month and days are generated
     * @return CoeusVector
     * @param dynaProposalDetailForm
     * @throws Exception
     */
    private CoeusVector performDefaultVersionAction(DynaActionForm dynaProposalDetailForm)throws Exception{
        Calendar calStart, calEnd, calPeriodStart, calPeriodEnd;
        CoeusVector cvBudgetPeriods = new CoeusVector();
        int startYear, endYear;
        calStart = Calendar.getInstance();
        calStart.setTime((Date)dynaProposalDetailForm.get("requestedStartDateInitial"));
        calEnd = Calendar.getInstance();
        calEnd.setTime((Date)dynaProposalDetailForm.get("requestedEndDateInitial"));
        startYear = calStart.get(Calendar.YEAR);
        endYear = calEnd.get(Calendar.YEAR);
        int versionNumber = ((Integer)dynaProposalDetailForm.get("versionNumber")).intValue();
        if(startYear < endYear) {
            //Period spans more thrn a year. Break up required.
            calPeriodStart = calStart;
            calPeriodEnd = Calendar.getInstance();
            int budgetPeriod = 0;
            while(true) {
                budgetPeriod = budgetPeriod + 1;
                BudgetPeriodBean budgetPeriodBean = new BudgetPeriodBean();
                budgetPeriodBean.setBudgetPeriod(budgetPeriod);
                budgetPeriodBean.setAw_BudgetPeriod(budgetPeriod);
                budgetPeriodBean.setAcType(TypeConstants.INSERT_RECORD);
                budgetPeriodBean.setProposalNumber((String)dynaProposalDetailForm.get("proposalNumber"));
                budgetPeriodBean.setVersionNumber(versionNumber);
                budgetPeriodBean.setTotalCost(0.0);
                budgetPeriodBean.setUnderRecoveryAmount(0.0);
                budgetPeriodBean.setCostSharingAmount(0.0);
                budgetPeriodBean.setStartDate(new java.sql.Date(calPeriodStart.getTimeInMillis()));
                calPeriodStart.add(Calendar.YEAR, 1);
                calPeriodStart.add(Calendar.DATE, -1);
                calPeriodEnd.setTimeInMillis(calPeriodStart.getTimeInMillis());
                calPeriodStart.add(Calendar.DATE, 1);
                if(calPeriodEnd.after(calEnd) || calPeriodEnd.equals(calEnd)) {
                    budgetPeriodBean.setEndDate((Date)dynaProposalDetailForm.get("requestedEndDateInitial"));
                    budgetPeriodBean.setAcType(TypeConstants.INSERT_RECORD);
                    cvBudgetPeriods.add(budgetPeriodBean);
                    break;
                }
                budgetPeriodBean.setEndDate(new java.sql.Date(calPeriodEnd.getTimeInMillis()));
                budgetPeriodBean.setAcType(TypeConstants.INSERT_RECORD);
                cvBudgetPeriods.add(budgetPeriodBean);
            }
        }else{
            //Generate 1st Period.
            BudgetPeriodBean budgetPeriodBean = new BudgetPeriodBean();
            budgetPeriodBean.setBudgetPeriod(MAKE_DEFAULT_PERIOD);
            budgetPeriodBean.setAw_BudgetPeriod(MAKE_DEFAULT_PERIOD);
            budgetPeriodBean.setAcType(TypeConstants.INSERT_RECORD);
            budgetPeriodBean.setProposalNumber((String)dynaProposalDetailForm.get("proposalNumber"));
            budgetPeriodBean.setVersionNumber(versionNumber);
            budgetPeriodBean.setStartDate((Date)dynaProposalDetailForm.get("requestedStartDateInitial"));
            budgetPeriodBean.setEndDate((Date)dynaProposalDetailForm.get("requestedEndDateInitial"));
            budgetPeriodBean.setTotalCost(0.0);
            budgetPeriodBean.setUnderRecoveryAmount(0.0);
            budgetPeriodBean.setCostSharingAmount(0.0);
            cvBudgetPeriods.add(budgetPeriodBean);
        }//End Else
        return cvBudgetPeriods;
    }
    
    /**
     *This method will copy the budget dyna form to BudgetInfoBean
     * @param budgetDynaForm
     * returns CoeusVector
     * @throws Exception
     */
    private CoeusVector getBudgetInfoBean(DynaActionForm budgetDynaForm) throws Exception{
        CoeusVector cvBudgetInfo = new CoeusVector();
        BudgetInfoBean budgetInfoBean = new BudgetInfoBean();
        BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
        beanUtilsBean.copyProperties(budgetInfoBean, budgetDynaForm);
        //Added for Case 3956 - On/Off Campus flag is not being set in Lite - start
        if(budgetDynaForm.get("bsOnOffCampusFlag") !=null && (budgetDynaForm.get("bsOnOffCampusFlag")).toString().equals("D")){
          budgetInfoBean.setDefaultIndicator(true);  
        }
        //Added for Case 3956 - ON/Off Campus flag is not being set in Lite - End
        cvBudgetInfo.add(budgetInfoBean);
        return cvBudgetInfo;
    }
    
    
    /**
     * This method is to create a new budget version
     * @param versionNumber
     * @param hmRequiredDetails
     * @param dynaProposalDetailForm
     * @throws Exception
     * @return CoeusVector
     */
    private CoeusVector getNewBudgetInfoData( HashMap hmRequiredDetails ,
    DynaActionForm dynaProposalDetailForm, int versionNumber, HttpServletRequest request)throws Exception{
        Timestamp dbTimestamp = prepareTimeStamp();
        // DynaValidatorForm budgetDynaForm = (DynaValidatorForm)hmRequiredDetails.get(DynaValidatorForm.class);
        CoeusDynaBeansList coeusSyncPersonsDynaList = (CoeusDynaBeansList) hmRequiredDetails.get(ActionForm.class);
        DynaActionForm budgetDynaForm = coeusSyncPersonsDynaList.getDynaForm(request,"budgetSummary");
        
        budgetDynaForm.set("proposalNumber",dynaProposalDetailForm.get("proposalNumber"));
        budgetDynaForm.set("versionNumber",new Integer(versionNumber)) ;
        budgetDynaForm.set("startDate",dynaProposalDetailForm.get("requestedStartDateInitial"));
        budgetDynaForm.set("endDate",dynaProposalDetailForm.get("requestedEndDateInitial"));
        budgetDynaForm.set("ohRateClassCode",new Integer(1));
        budgetDynaForm.set("acType",TypeConstants.INSERT_RECORD);
        budgetDynaForm.set("updateTimestamp",dbTimestamp.toString());
        budgetDynaForm.set("budgetStatusCode","I");
        budgetDynaForm.set("unitNumber",dynaProposalDetailForm.get("unitNumber"));
        budgetDynaForm.set("activityTypeCode",dynaProposalDetailForm.get("activityTypeCode"));
        budgetDynaForm.set("totalCost",new Double(0.0));
        budgetDynaForm.set("totalDirectCost",new Double(0.0));
        budgetDynaForm.set("totalIndirectCost",new Double(0.0));
        budgetDynaForm.set("costSharingAmount",new Double(0.0));
        budgetDynaForm.set("underRecoveryAmount",new Double(0.0));
        budgetDynaForm.set("residualFunds","0.0");
        budgetDynaForm.set("totalCostLimit","0.0");
        budgetDynaForm.set("finalVersionFlag","N");
        budgetDynaForm.set("modularBudgetFlag","N");
        //COEUSQA-1693 - Cost Sharing Submission - start
// JM 5-27-2011 updated checkbox to default to "N"
        budgetDynaForm.set("submitCostSharingFlag", "N");
// JM END        
        //COEUSQA-1693 - Cost Sharing Submission - end
        budgetDynaForm.set("ohRateTypeCode",new Integer(1));
        budgetDynaForm.set("urRateClassCode",new Integer(1));
        budgetDynaForm.set("onOffCampusFlag","D");   // Case id# 2924 - set default
        budgetDynaForm.set("bsOnOffCampusFlag","D"); // Added for case#3654 - Third option 'Default' in the campus dropdown  
        //Make directly budgetinfobean
        CoeusVector cvBudgetInfo = getBudgetInfoBean(budgetDynaForm);
        return cvBudgetInfo ;
    }
    
    /** To read the Budget Versions Menus from the XML file speciofied for the
     *Budget
     */
    private void getBudgetVersionMenus(HttpServletRequest request){
        HttpSession session = request.getSession();
        Vector budgetVersionMenuItemsVector  = null;
        ReadXMLData readXMLData = new ReadXMLData();
        budgetVersionMenuItemsVector = (Vector) session.getAttribute(CoeusliteMenuItems.BUDGET_VERSION_MENU_ITEMS);
        if (budgetVersionMenuItemsVector == null || budgetVersionMenuItemsVector.size()==0) {
            MessageResources messages = MessageResources.getMessageResources(BUDGET_MESSAGE_FILE_NAME);
            String budgetVersionsXmlPath = messages.getMessage(BUDGET_VERSIONS_XML_PATH);
            budgetVersionMenuItemsVector = readXMLData.readXMLDataForMenu(budgetVersionsXmlPath);
            session.setAttribute(CoeusliteMenuItems.BUDGET_VERSION_MENU_ITEMS, budgetVersionMenuItemsVector);
        }
    }
    
    
    
    /*
     * This method is used for to copy budget versions
     * @param newVersionNumber
     * @param BudgetInfoBean
     * @return CoeusVector
     */
    
    private CoeusVector copyBudget(int newVersionNumber, BudgetInfoBean budgetInfoBean, boolean isActivityChanged, HttpServletRequest request) throws Exception{
        CoeusVector cvBudgetInfo = new CoeusVector();
        HttpSession session = request.getSession();
        //Added for removing instance variable -case # 2960 - start
        QueryEngine queryEngine = QueryEngine.getInstance();
        String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        //Added for removing instance variable - case # 2960- end           
        boolean isSuccessFulCopied = false;
        Hashtable htBudgetData = initializeBudgetCalculator(budgetInfoBean);
        queryEngine.addDataCollection(queryKey, htBudgetData);
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
         boolean allow_copy = false;
        //COEUSQA-3273  - end
        GreaterThan greaterThan = new GreaterThan("budgetPeriod", new Integer(MAKE_DEFAULT_PERIOD));
        Equals eqVersion = new Equals("versionNumber", new Integer(budgetInfoBean.getVersionNumber()));
        
        Class copyClass[] = {
            BudgetInfoBean.class,
            BudgetDetailBean.class,
            BudgetDetailCalAmountsBean.class,
            BudgetPersonnelDetailsBean.class,
            BudgetFormulatedCostDetailsBean.class,
            ProposalRatesBean.class,
            BudgetJustificationBean.class,
            BudgetPersonsBean.class,
            ProposalLARatesBean.class,
            BudgetPeriodBean.class,
            BudgetPersonnelCalAmountsBean.class,
            ProjectIncomeBean.class,
            BudgetModularBean.class,
            BudgetModularIDCBean.class
        };
        
        Class removeClass[] = {
            BudgetDetailBean.class,
            BudgetDetailCalAmountsBean.class,
            BudgetPersonnelDetailsBean.class,
            BudgetFormulatedCostDetailsBean.class,
            BudgetPersonnelCalAmountsBean.class,
            ProjectIncomeBean.class,
            //Commented with COEUSDEV-298:Do not copy modular flag and modular budget while only copying period 1
//            BudgetModularBean.class,
//            BudgetModularIDCBean.class
            //COEUSDEV-298:End
        };
        
        int copyType = Integer.parseInt(request.getParameter("copyPeriod"));
        if(copyType == COPY_ONE_PERIOD_ONLY) {
            //Delete beans having period > 1
            for(int index = 0; index < removeClass.length; index++) {
                queryEngine.removeData(queryKey, removeClass[index], greaterThan);
            }
            
            queryEngine.setUpdate(queryKey, BudgetPeriodBean.class, "totalCost", DataType.getClass(DataType.DOUBLE), new Double(0), greaterThan);
            queryEngine.setUpdate(queryKey, BudgetPeriodBean.class, "totalDirectCost", DataType.getClass(DataType.DOUBLE), new Double(0), greaterThan);
            queryEngine.setUpdate(queryKey, BudgetPeriodBean.class, "totalIndirectCost", DataType.getClass(DataType.DOUBLE), new Double(0), greaterThan);
            queryEngine.setUpdate(queryKey, BudgetPeriodBean.class, "costSharingAmount", DataType.getClass(DataType.DOUBLE), new Double(0), greaterThan);
            queryEngine.setUpdate(queryKey, BudgetPeriodBean.class, "underRecoveryAmount", DataType.getClass(DataType.DOUBLE), new Double(0), greaterThan);
            queryEngine.setUpdate(queryKey, BudgetPeriodBean.class, "totalCostLimit", DataType.getClass(DataType.DOUBLE), new Double(0), greaterThan);
            
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
        }
        
        Object key;
        
        for(int index = 0; index < copyClass.length; index++) {
            key = copyClass[index];
            
            queryEngine.setUpdate(queryKey, (Class)key, "acType", String.class, TypeConstants.INSERT_RECORD, eqVersion);
            queryEngine.setUpdate(queryKey, (Class)key, "versionNumber", DataType.getClass(DataType.INT), new Integer(newVersionNumber) , eqVersion);
            
        }
        
        // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
        // Sub award line items are removed from the queryEngine
        // reorder the lineitem sequence for budget details in all the periods
        
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
        if(copyType == COPY_ONE_PERIOD_ONLY) {
            Equals eqBudgetPeriod = new Equals("budgetPeriod", new Integer(1));
            CoeusVector cvBudgetDetails = queryEngine.executeQuery(queryKey, BudgetDetailBean.class,eqBudgetPeriod);
            cvBudgetDetails = resetOrderOfLineItemSequence(cvBudgetDetails);
            
            //Commented for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
            //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
            //Add all the cost elements to vector vecCostElements from the budget details
            //Vector vecCostElements = getCostElements(cvBudgetDetails)
            //get the Inactive cost elements
            //if it returns true then throw the warning message
            //if user clicks yes then allow to save the budget
            //allow_copy = getCostElementDetails(vecCostElements);
            // if  allow_copy returns false means there are no ianactive cost elements
            //if allow_copy is false then set it as true to allow for copying the new version of budget
//            if(allow_copy == false){
//                allow_copy = true;
//            }
            //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
            //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end      
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
                //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
                //Add all the cost elements to vector vecCostElements from the budget details
                /*Vector vecCostElements = getCostElements(cvUpdatedBudgetDetails);
                //if it returns true and user clicks yes then it allows to create new version of budget
                allow_copy = getCostElementDetails(vecCostElements);   
                // if  allow_copy returns false means there are no ianactive cost elements
                //if allow_copy is false then set it as true to allow for copying the new version of budget
                if(allow_copy == false){
                    allow_copy = true;
                }
                //added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end*/
                
                queryEngine.removeData(queryKey, BudgetDetailBean.class,gtBudgetPeriod);
                queryEngine.addCollection(queryKey, BudgetDetailBean.class,cvUpdatedBudgetDetails);
            }
          
        }
        // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
        
        
        //if it returns true then it allows to create new version of budget
       // if(allow_copy){            
            //Added for Case - 3714 : Copying Budget marked final - Set always false to final version flag. - Start
            budgetInfoBean.setFinalVersion(false);
            //Added for Case - 3714 : Copying Budget marked final - Set always false to final version flag. - End
            budgetInfoBean.setVersionNumber(newVersionNumber);
            budgetInfoBean.setAcType(TypeConstants.INSERT_RECORD);
            
            
            CoeusVector cvProposalRates = null;
            if(isActivityChanged){
                queryEngine.removeData(queryKey, ProposalRatesBean.class, new Equals("acType", TypeConstants.INSERT_RECORD));
                cvProposalRates = getProposalRatesBean(budgetInfoBean);
                queryEngine.addCollection(queryKey, ProposalRatesBean.class,cvProposalRates);
            }
            
            String newQueryKey = budgetInfoBean.getProposalNumber() + budgetInfoBean.getVersionNumber();
            queryEngine.addDataCollection(newQueryKey, queryEngine.getDataCollection(queryKey));
            queryEngine.removeDataCollection(queryKey);
            
            queryKey = newQueryKey;
            
            queryEngine.insert(queryKey, budgetInfoBean);
            htBudgetData = queryEngine.getDataCollection(queryKey);
            htBudgetData = calculateAllPeriods(htBudgetData, budgetInfoBean);
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            BudgetUpdateTxnBean budgetUpdateTxnBean = new BudgetUpdateTxnBean(userInfoBean.getUserId());
            isSuccessFulCopied = budgetUpdateTxnBean.addUpdDeleteBudget(htBudgetData);
            cvBudgetInfo.insertElementAt(new Boolean(isSuccessFulCopied), 0);
            cvBudgetInfo.insertElementAt(budgetInfoBean, 1);
            removeQueryEngineCollection(queryKey);
        //}
        return cvBudgetInfo;
        
    }
    
    
    
    /**
     * To get the BudgetInfoBean for particular proposal number and version number
     * @param proposalNumber
     * @param versionNumber
     * @throws Exception
     * @return BudgetInfoBean
     */
    private BudgetInfoBean getBudgetInfoBeanforProposal(String proposalNumber , int versionNumber, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmBudgetInfoData = new HashMap();
        hmBudgetInfoData.put("proposalNumber",proposalNumber);
        hmBudgetInfoData.put("versionNumber",new Integer(versionNumber));
        Hashtable htBudgetInfoData = (Hashtable)webTxnBean.getResults(request, "getBudgetInfoData" , hmBudgetInfoData );
        Vector vecBudgetInfo = (Vector)htBudgetInfoData.get("getBudgetInfoData");
        DynaValidatorForm dynaBudgetForm = (DynaValidatorForm)vecBudgetInfo.get(0);
        BeanUtilsBean copyBudgetBean  = new BeanUtilsBean();
        BudgetInfoBean budgetInfoBean = new BudgetInfoBean();
        copyBudgetBean.copyProperties(budgetInfoBean, dynaBudgetForm);
        //Added for Case 3956 - On/Off Campus flag is not being set in Lite - start
        if(dynaBudgetForm.get("bsOnOffCampusFlag") !=null && (dynaBudgetForm.get("bsOnOffCampusFlag")).toString().equals("D")){
          budgetInfoBean.setDefaultIndicator(true);  
        }
        //Added for Case 3956 - On/Off Campus flag is not being set in Lite - End
        return budgetInfoBean;
    }
    
    
    /**
     * Method to Update Budget_Status in osp$eps_proposal
     * @param request request for budget status
     * @param dynaValidatorForm instance of dyna validator form
     * @throws Exception if any error occurs
     */
    private void updateBudgetStatus( DynaValidatorForm dynaValidatorForm, HttpServletRequest request) throws Exception{
        HttpSession session =  request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        String budgetStatusCode = (String)dynaValidatorForm.get("budgetStatusCode");
        String proposalNumber = (String)dynaValidatorForm.get("proposalNumber");
        String copyPeriodCode = (String)dynaValidatorForm.get("copyPeriodCode");
        //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
        String finalVersionNumber = (String)dynaValidatorForm.get("finalVersionValue");
        finalVersionNumber=finalVersionNumber.trim();
        boolean update_budget_status = false;
        if(finalVersionNumber!=null && (copyPeriodCode.equals(EMPTY_STRING)) && !(finalVersionNumber.equals(EMPTY_STRING))){
            //if update_budget_flag is true then allow to save the budget version
            update_budget_status = getInactiveTypeDetails(proposalNumber,finalVersionNumber,request);
            if(update_budget_status){
                HashMap hmUpdBudgetStatus = new HashMap();
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
                //If budget saved then set isBudgetSaved flag as true
                isBudgetSaved = true;
            } 
            else{
                //If update_budget_flag is false and budget has status as complete then set the budgetStatusMode as complete
                isBudgetSaved = false;
                String budgetstatus =(String) session.getAttribute("budgetStatusCode");
                if(budgetstatus.equals("C")) {
                    session.setAttribute("budgetStatusMode", "complete");
                }
            }
        }else{
            HashMap hmUpdBudgetStatus = new HashMap();
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
        }
        //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end        
    }
    
    private boolean getLock(HttpServletRequest request)throws Exception {
        boolean isLocked = true;
        HttpSession session =  request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String proposalNumber = (String) session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
        boolean isValid = prepareLock(userInfoBean, proposalNumber, request);
        if(!isValid){
            session.setAttribute(CoeusLiteConstants.MODE+session.getId(),"display");
            isLocked = false;
        }
        return isLocked;
    }
    //Added for COEUSQA-2546  Two users can modify a budget at same time. Locking not working -Start
    private boolean isLockExist(String proposalNumber, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        boolean isLocked= false;
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        LockBean lockBean = getLockingBean(userInfoBean, proposalNumber,request);
        if(lockBean != null){
            LockBean serverDataBean = getLockedData(CoeusLiteConstants.BUDGET_LOCK_STR+lockBean.getModuleNumber(), request);
            boolean isNotLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            if(!isNotLockExists) {
                isLocked = true;
            }
        }        
        return isLocked;
    }
    //Added for COEUSQA-2546  - End
    
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
     *This method gets the inactive cost elements
     * It returns boolean value. If it is true then it has inactive cost elements
     *@param proposalNumber
     *@param versionNumber
     *@returns allow_copy
     */
    private boolean getCopyDetails(String proposalNumber, String versionNumber,HttpServletRequest request){ 
        QueryEngine queryEngine = QueryEngine.getInstance();
        HttpSession session = request.getSession();
        int newVersionNumber = 1;
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        if(versionNumber!=null){
            newVersionNumber = Integer.parseInt(versionNumber);
        }
        String queryKey = budgetInfoBean.getProposalNumber()+ newVersionNumber;
        Hashtable htBudgetData = new Hashtable();
        try {
            htBudgetData = initializeBudgetCalculator(budgetInfoBean);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        queryEngine.addDataCollection(queryKey, htBudgetData);
        Equals eqBudgetPeriod = new Equals("budgetPeriod", new Integer(1));
        CoeusVector cvBudgetDetails = new CoeusVector();
        try {
            cvBudgetDetails = queryEngine.executeQuery(queryKey, BudgetDetailBean.class, eqBudgetPeriod);
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
        cvBudgetDetails = resetOrderOfLineItemSequence(cvBudgetDetails);
        
        //Add all the cost elements to vector vecCostElements from the budget details
        Vector vecCostElements = getCostElements(cvBudgetDetails);
        //get the Inactive cost elements
        boolean allow_copy = getCostElementDetails(vecCostElements);
        
        return allow_copy;
        
    }
    
    /**
     * This method gives coset element details
     * @param cvBudgetDetails
     * @return vecCostElements
     */
    private Vector getCostElements(CoeusVector cvBudgetDetails){
        Vector vecCostElements = new Vector();
        BudgetDetailBean budgetDetailBean = new BudgetDetailBean();
        if (cvBudgetDetails != null && cvBudgetDetails.size() > 0) {
            int size = cvBudgetDetails.size();
            for(int index = 0; index < size; index++) {
                budgetDetailBean = (BudgetDetailBean) cvBudgetDetails.get(index);
                vecCostElements.add(budgetDetailBean.getCostElement());
            }
        }
        //remove duplicate cost elements from the vector vecCostElements
        for(int index=0; index<vecCostElements.size(); index++) {
            //costElementIndex Returns the index of the last occurrence of the specified object from the vector vecCostElements.
            int costElementIndex = vecCostElements.lastIndexOf(vecCostElements.get(index));
            if(costElementIndex != index) {
                vecCostElements.remove(costElementIndex);
                index=index-1;
            }
        }
        return   vecCostElements;
    }
    
    /**
     * Method to get the Inactive cost elements
     * @param vecCostElements
     * @return boolean value
     */
    private boolean getCostElementDetails(Vector vecCostElements) {
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        Vector inActivecostElements = new Vector();
        if(vecCostElements!=null && vecCostElements.size()>0){
            for(int index = 0 ; index <vecCostElements.size() ; index++){
                String costElement = (String) vecCostElements.get(index);
                CostElementsBean costElementsBean = new CostElementsBean();
                try {
                    costElementsBean = budgetDataTxnBean.getCostElementsDetails(costElement);
                } catch (CoeusException ex) {
                    ex.printStackTrace();
                } catch (DBException ex) {
                    ex.printStackTrace();
                }
                //If status of cost element is 'N' then add to vector inActivecostElements
                if("N".equals(costElementsBean.getActive())){
                    inActivecostElements.addElement(costElement);
                }
            }
        }
        if(inActivecostElements!= null && inActivecostElements.size() >0){
            return true;
        }
        return false;
    }
    
    //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
    private boolean getInactiveTypeDetails(String proposalNumber,String VersionNumber, HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = null;
        BudgetPersonsBean budgetPersonsBean = new BudgetPersonsBean();
        Set uniquePeriodType = new HashSet();
        Set uniqueAppointmentType = new HashSet();
        String periodType = null;
        String appointmentType = null;
        Vector inActivePeriodType = new Vector();
        Vector inActiveAppointmentType = new Vector();
        StringBuffer errMsg;
        Vector vecAppAndPeriodTypeMessages = new Vector();
         boolean update_budget = false;
        int newVersionNumbert  = Integer.parseInt(VersionNumber);
        //get periodType details for proposal budget
        CoeusVector cvBudgetPersonnelDetail = budgetDataTxnBean.getBudgetPersonnelDetail(proposalNumber,newVersionNumbert);
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
                //Checking for the periodType status
                if("N".equals(costElementsBean.getActive())){
                    inActivePeriodType.addElement(costElementsBean.getDescription());
                    errMsg = new StringBuffer();
                    String costElementDesc = costElementsBean.getDescription();
                    errMsg.append("Period type ");
                    errMsg.append(costElementDesc);
                    errMsg.append(" is no longer active. Please select an alternative");
                    vecAppAndPeriodTypeMessages.add(errMsg);
                }
            }
        }
        //get the budget persons
        CoeusVector cvBudgetPersons = budgetDataTxnBean.getBudgetPersons(proposalNumber,newVersionNumbert);
        if(cvBudgetPersons!=null && cvBudgetPersons.size()>0){
            for ( int appTypeIndex = 0; appTypeIndex < cvBudgetPersons.size(); appTypeIndex ++ ){
                budgetPersonsBean = (BudgetPersonsBean)cvBudgetPersons.get(appTypeIndex);
                appointmentType = budgetPersonsBean.getAppointmentType();
                uniqueAppointmentType.add(appointmentType);
            }
        }
        //holds inactive period type details
        if(!uniqueAppointmentType.isEmpty()){
            Iterator iterator = uniqueAppointmentType.iterator();
            while(iterator.hasNext()){
                appointmentType = (String)iterator.next();
                budgetPersonsBean = budgetDataTxnBean.getAppointmentTypeDetails(appointmentType);
                //Checking for the periodType status
                if("N".equals(budgetPersonsBean.getStatus())){
                    inActiveAppointmentType.addElement(budgetPersonsBean.getAppointmentType());
                    errMsg = new StringBuffer();
                    String newAppointmentType = (String)budgetPersonsBean.getAppointmentType();
                    errMsg.append("Appointment type ");
                    errMsg.append(newAppointmentType);
                    errMsg.append(" is no longer active. Please select an alternative");
                    vecAppAndPeriodTypeMessages.add(errMsg);
                }
            }
        }
        
        
        //vector vecAppAndPeriodTypeMessages holds inactivetype(Both appointment type and period type) details
        if(vecAppAndPeriodTypeMessages!=null && vecAppAndPeriodTypeMessages.size()>0){
            session.setAttribute("inactiv_App_Per_Type_Messages",vecAppAndPeriodTypeMessages);
        }else{
            session.setAttribute("inactiv_App_Per_Type_Messages",null);
        }
        //if vector vecAppAndPeriodTypeMessages holds inactive Type details then dont allow to save it so set update_budget as false
        if(vecAppAndPeriodTypeMessages!=null && vecAppAndPeriodTypeMessages.size()>0){
            update_budget = false;
        }else{
            //if vector vecAppAndPeriodTypeMessages is null means it doesnt hold any inactive types then set update_budget as true
            update_budget = true;
        }
        return update_budget;
    }
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end 
}
