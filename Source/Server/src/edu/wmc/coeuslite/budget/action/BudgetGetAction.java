/*
 * BudgetGetAction.java
 *
 * Created on March 6, 2006, 4:09 PM
 *
 */
/* PMD check performed, and commented unused imports and variables on 03-MARCH-2011
 * by MD.Ehtesham Ansari
 */

package edu.wmc.coeuslite.budget.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.budget.bean.BudgetDetailBean;
//import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
//import edu.mit.coeus.budget.bean.BudgetDetailBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetPeriodBean;
import edu.mit.coeus.budget.bean.BudgetUpdateTxnBean;
import edu.mit.coeus.budget.bean.CostElementsBean;
import edu.mit.coeus.budget.bean.ProposalRatesBean;
import edu.mit.coeus.exception.CoeusException;
//import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
//import edu.mit.coeus.utils.CoeusConstants;
//import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
//import edu.mit.coeus.utils.RateClassTypeConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeuslite.utils.CoeusDynaBeansList;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.wmc.coeuslite.budget.bean.CategoryBean;
import edu.wmc.coeuslite.budget.bean.ProposalBudgetHeaderBean;
//import edu.wmc.coeuslite.utils.SyncBudgetPersons;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author  chandrashekara
 */
public class BudgetGetAction extends BudgetBaseAction{
    
    private static final String BUDGET_MESSAGE_FILE_NAME = "BudgetMessages";
    private static final String PLEASE_SELECT_CODE = "generalInfoProposal.pleaseSelectNSF";
    private static final String PROPOSAL_MESSAGE_FILE_NAME = "ProposalMessages";
    private static final String EQUIPMENT_TARGET_CODE = "EQUIPMENT_TARGET_CODE";
    private static final String TRAVEL_TARGET_CODE = "TRAVEL_TARGET_CODE";
    private static final String PARTICIPANT_TRAINEE_TARGET_CODE = "PARTICIPANT_TRAINEE_TARGET_CODE";
    private static final String OTHER_DIRECT_COSTS_TARGET_CODE = "OTHER_DIRECT_COSTS_TARGET_CODE";
    private static final String MAPPING_NAME ="MAPPING_NAME";
    private static final String GET_BUDGET_DATA = "/getBudget";
    private static final String GET_BUDGET_SUMMARY = "/getBudgetSummary";
    private static final String GET_EQUIPMENT_DATA = "/getBudgetEquipment";
    private static final String GENERATE_ALL_PERIODS = "/generateAllPeriods";
    private static final String SELECTION_GENERATE_MENU = "/selectionGenerateAllPeriods";
    private static final String ACTIVITY_TYPE_CHANGED  = "activityTypeChanged";
    private static final String MULTIPLE_APPOINTMENTS  = "multipleAppointments";
    private static final String SYNC_PERSONS ="syncPersons";
    private static final String GET_BUDGET_SYNC_PERSON  ="/getBudgetSyncPerson";   
    
    /** Creates a new instance of BudgetGetAction */
    public BudgetGetAction() {
        
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
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request,HttpServletResponse response) throws Exception {

        HashMap hmRequiredDetails = new HashMap();
        //Added for case#2776 - Allow concurrent Prop dev access in Lite - start
        String proposalNumber = request.getParameter("proposalNumber");
        HttpSession session = request.getSession();
        if(proposalNumber != null && !proposalNumber.equals("")){
            session.setAttribute("proposalNumber"+session.getId(), proposalNumber);
        }
        //Added for case#2776 - Allow concurrent Prop dev access in Lite - end
        if(actionMapping.getPath().equals(GET_BUDGET_DATA)){
            //noting
        }else if(actionMapping.getPath().equals(GET_EQUIPMENT_DATA)){
            //nothing
        }else if(actionMapping.getPath().equals(GET_BUDGET_SYNC_PERSON)){
            //nothing
        }
        //COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-Start
        else if(actionMapping.getPath().equals(GET_BUDGET_SUMMARY)){
            //nothing
        }
        //COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-End
        
        else{
            DynaValidatorForm dynaForm = (DynaValidatorForm)actionForm;
            hmRequiredDetails.put(DynaValidatorForm.class,dynaForm);
        }
        
        hmRequiredDetails.put(ActionMapping.class,actionMapping);
        hmRequiredDetails.put(ActionForm.class, actionForm);
        HashMap hmProposalBudgetData = new HashMap();
                
        //Added for case#2776 - Allow concurrent Prop dev access in Lite - start
        String listPage = request.getParameter("listPage");  
        String redirectUrl = "";
        if(!isBudgetExists(request)){
            if(!canCreateBudget(request)){                
                if("Y".equals(listPage)){
                    redirectUrl = "/proposalList.do?PROPOSAL_TYPE=PROPOSAL_INPROGRESS&Menu_Id=003&SUBHEADER_ID=001";
                }else{
                    redirectUrl = "/getGeneralInfo.do?action=noView&proposalNumber=";
                    redirectUrl += proposalNumber;                
                }
                RequestDispatcher rd = request.getRequestDispatcher(redirectUrl);
                rd.forward(request,response);
                return null;                  
            }else{
                if("Y".equals(listPage)){
                    String createBudgetPopUp = request.getParameter("createBudgetPopUp");
                    if(createBudgetPopUp == null || createBudgetPopUp.equals(EMPTY_STRING)){
                        if(hasCreateBudgetRight(request)){
                            redirectUrl = "/proposalList.do?PROPOSAL_TYPE=PROPOSAL_INPROGRESS&Menu_Id=003&SUBHEADER_ID=001&createBudgetPopUp=Y";
                            RequestDispatcher rd = request.getRequestDispatcher(redirectUrl);
                            rd.forward(request,response);
                            return null;                                  
                        }
                    }
                }
            }            
        }
        //Added for case#2776 - Allow concurrent Prop dev access in Lite - end
        
        
        // Required data to be pushed into the HashMap - End
        //Modified for COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-Start
        ActionForward actionForward = getSelectedProposalBudgetData(actionForm, hmProposalBudgetData,
                                hmRequiredDetails, request, response);
        //Modified for COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-End
        return actionForward; 
    }
    /**
     * This method will identify which request is comes from which path and
     * navigates to the respective ActionForward
     * @returns ActionForward object
     * @param hmProposalBudgetData contain proposal number and version number
     * @param hmRequiredDetails contain dynavalid form and action mapping
     * @throws Exception if any error occurs
     * @return forward to respective navigator
     */
    //Modified for COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-Start
    private ActionForward getSelectedProposalBudgetData(ActionForm actionForm, HashMap hmProposalBudgetData,
    HashMap hmRequiredDetails, HttpServletRequest request,HttpServletResponse response)throws Exception{
    //Modified for COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-End
        String navigator = EMPTY_STRING;
        WebTxnBean webTxnBean = new WebTxnBean();
        ActionMapping actionMapping = (ActionMapping)hmRequiredDetails.get(ActionMapping.class);
        if(actionMapping.getPath().equals(GET_BUDGET_DATA)){
            navigator = getBudgetData(hmProposalBudgetData,hmRequiredDetails,request,response);
            //  getBudgetMenus(request);
        }else if(actionMapping.getPath().equals(GET_EQUIPMENT_DATA)){
            navigator = getBudgetEquipementData(hmProposalBudgetData, hmRequiredDetails, request);
        }else if(actionMapping.getPath().equals(GENERATE_ALL_PERIODS)){
            navigator = generatePeriods(request);
        }else if(actionMapping.getPath().equals(GET_BUDGET_SUMMARY)){
            int versionNumber = -1 ;
            //Modified for COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-Start
            navigator = getBudgetSummaryData(actionForm, hmProposalBudgetData,hmRequiredDetails,
                                                    versionNumber,request);
            //Modified for COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-End
        }else if(actionMapping.getPath().equals(SELECTION_GENERATE_MENU)){
            Map mapMenuList = new HashMap();
            mapMenuList.put("menuItems",CoeusliteMenuItems.BUDGET_MENU_ITEMS);
            mapMenuList.put("menuCode",CoeusliteMenuItems.GENERATE_PERIODS_CODE);
            setSelectedMenuList(request, mapMenuList);
            navigator = "success";
        }else if(actionMapping.getPath().equals(GET_BUDGET_SYNC_PERSON)){
            navigator = "success";
        }   
        //Added/Modified for case#2776 - Allow concurrent Prop dev access in Lite - start
        String displayMode = request.getParameter("displayMode");
        String redirectUrl = "";
        if(displayMode != null && displayMode.equals("Y")){            
            ActionForward actionForward = actionMapping.findForward(navigator);
            return actionForward;            
        }else{  
            if(isBudgetLockedByOther(request)){
                String listPage = request.getParameter("listPage");            
                if("Y".equals(listPage)){
                    redirectUrl = "/proposalList.do?PROPOSAL_TYPE=PROPOSAL_INPROGRESS&Menu_Id=003&SUBHEADER_ID=001";
                    RequestDispatcher rd = request.getRequestDispatcher(redirectUrl);
                    rd.forward(request,response);
                    return null;                     
                }else{
                    getLock(navigator, request, response);
                    ActionForward actionForward = actionMapping.findForward(navigator);
                    return actionForward;                    
                }                       
            }else{
                HttpSession session = request.getSession();
                session.removeAttribute("mode"+session.getId());                
                if(isProposalInHierarchy(request)){
                    session.setAttribute("mode"+session.getId(), "display");                    
                }else{
                    session.setAttribute("mode"+session.getId(), "");
                    String hasModifyValue = (String)session.getAttribute("hasRights"); 
                    if(!hasModifyValue.equals("true")){
                        session.setAttribute("mode"+session.getId(), "display");
                    }
                    // Added for Case COEUSQA-2546 Two users can modify a budget at same time. Locking not working -Start
                    // handled null check.
                    if(!setBudgetInDisplay(request) && navigator != null && !navigator.equals(EMPTY_STRING)){
                        getLock(navigator, request, response);
                    }
                    // Added for Case COEUSQA-2546 -End
                }                
                ActionForward actionForward = actionMapping.findForward(navigator);
                return actionForward;                
            }
        }
        //Added/Modified for case#2776 - Allow concurrent Prop dev access in Lite - end
    }
    
    /**
     * This method will generate the periods and then saves to the database
     * @throws Exception if any error occurs
     * @return navigator
     */
    private String generatePeriods(HttpServletRequest request) throws Exception{
        //Modified for removing instance variable -case # 2960 - starts
//        String navigator = generateAllPeriods(request);
        HashMap hmNavigator = generateAllPeriods(request);
        String navigator = (String) hmNavigator.get("navigator");
        //Modified for removing instance variable -case # 2960 - ends
        HttpSession session = request.getSession();
        if(navigator.equals("lineItemRequired")){
            ActionMessages actionMessages = new ActionMessages();
            //Modified for removing instance variable -case # 2960 - starts
//            actionMessages.add("generate.period_generated" ,
//            new ActionMessage("generate.lineItemRequired",new Integer(getBudgetPeriodNumber())));
            actionMessages.add("generate.period_generated" ,
            new ActionMessage("generate.lineItemRequired", hmNavigator.get("budgetPeriodNumber")));            
            //Modified for removing instance variable -case # 2960 - ends
            saveMessages(request, actionMessages);
            session.setAttribute("Generated", new Boolean(false));
            navigator = "success";
        }else if(navigator.equals("generated")){
            session.setAttribute("Generated", new Boolean(true));
            navigator = "success";
        }
        return navigator;
    }
    /**
     * This method will return the Propsoal Budget header data and the Budget summary
     * data for the selected Propsoal number
     * @param hmProposalBudgetData contain proposal and version number
     * @param hmRequiredDetails contain dyna form and action mapping object
     * @throws Exception if any error occurs
     * @return navigator to forward
     */
    
    private String getBudgetData(HashMap hmProposalBudgetData,
        HashMap hmRequiredDetails,HttpServletRequest request,HttpServletResponse response)throws Exception{
        String navigator = EMPTY_STRING;
        ActionMessages actionMessages = new ActionMessages();
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        boolean hasRight = false;
        boolean hasViewRightOnly = false;
        Vector vecBudgetRights = null ;
        //        DynaValidatorForm dynaProposalDetailForm = null ;
        
        DynaActionForm dynaProposalDetailForm = null ;
        String unitNumber =EMPTY_STRING;
        
        //        DynaValidatorForm dynaForm = (DynaValidatorForm)hmRequiredDetails.get(DynaValidatorForm.class);
        CoeusDynaBeansList coeusSyncPersonsDynaList = (CoeusDynaBeansList) hmRequiredDetails.get(ActionForm.class);
        DynaActionForm dynaForm = coeusSyncPersonsDynaList.getDynaForm(request,"budgetSummary");
        
        
        String proposalNumber = request.getParameter("proposalNumber");
        //Added for case#2776 - allow concurrent Prop dev access in Lite - start
        if(proposalNumber == null || proposalNumber.equals("")){
            proposalNumber = (String)session.getAttribute("proposalNumber");
        }
        //Added for case#2776 - allow concurrent Prop dev access in Lite - end
        hmProposalBudgetData.put("proposalNumber",proposalNumber);
        
        Hashtable htPropData = (Hashtable)webTxnBean.getResults(request,"isValidPropDevNoNBudget",hmProposalBudgetData);
        HashMap validData = (HashMap)htPropData.get("isValidPropDevNoNBudget");
        int valid = Integer.parseInt((String)validData.get("isValid"));
        
        CoeusVector cvBudgetForProposal = getBudgetInfo(request , proposalNumber);
        Vector vecProposalDetails = getProposalDetailsForBudget( request , proposalNumber);
        
        if(vecProposalDetails!=null && vecProposalDetails.size() > 0){
            dynaProposalDetailForm = (DynaActionForm)vecProposalDetails.elementAt(0);
            String budgetStatusCode = (String)dynaProposalDetailForm.get("budgetStatusCode");
            session.setAttribute("budgetStatusMode", "Incomplete");
            unitNumber = (String)dynaProposalDetailForm.get("unitNumber");
            if(budgetStatusCode!=null && !budgetStatusCode.equals(EMPTY_STRING)){
                
                if(budgetStatusCode.equals("C")){
                    //session.setAttribute("mode","display");
                    session.setAttribute("budgetStatusMode", "complete");
                }else{
                    //session.setAttribute("mode"+session.getId(), "modify");
                    session.setAttribute("versionMode"+session.getId(), "modify");
                }
            }
            vecBudgetRights = checkBudgetRights(request , dynaProposalDetailForm);
        }
        //Added/Modified for case#2776 - Allow concurrent Prop dev access in Lite - start
        String displayMode = request.getParameter("displayMode");
        if(displayMode != null && displayMode.equals("Y")){
            hasRight = false;
            hasViewRightOnly = true;
        }else{        
            hasRight = ((Boolean)vecBudgetRights.elementAt(0)).booleanValue();
            hasViewRightOnly = ((Boolean)vecBudgetRights.elementAt(1)).booleanValue();
        }   
        if(!hasRight && isUserPI(request)){
            hasViewRightOnly = true;
        }
        //Added/Modified for case#2776 - Allow concurrent Prop dev access in Lite - end
        session.setAttribute("hasRights",String.valueOf(hasRight));
        session.setAttribute("hasViewRightOnly",String.valueOf(hasViewRightOnly));
        
        // If Valid == 200 Budget Does not exist for the proposal
        if(valid == 200){
            /**
             * 1. Verify the rights for creating the budget
             * 2. If the user has the right to create budget then create  NEW BUDGET And Display Budget Personnel PAge
             * 3. If the user does not have right to create budget and budget doesnt exist Display a Validation Message
             **/
            if(hasRight){
                if(cvBudgetForProposal == null || cvBudgetForProposal.size() == 0){
                    String multipleAppointmentExist = request.getParameter("multipleAppointmentExist");
                    String syncPersonsExist = request.getParameter("syncPersonsExist");
                    // check for sync persons if no multiple appointments or not valid jobcode create a new version
                    if( multipleAppointmentExist == null  && syncPersonsExist == null ){
                        CoeusVector cvBudgetInfo = getNewBudgetInfoData(hmRequiredDetails,
                                                    dynaProposalDetailForm,request);
                        ////   Added for case#3654 - Third option 'Default' in the campus dropdown -Start
                        if(cvBudgetInfo !=null && cvBudgetInfo.size() > 0){
                            BudgetInfoBean budgetInfobean = (BudgetInfoBean)cvBudgetInfo.get(0);
                            budgetInfobean.setDefaultIndicator(true);
                        }
                        //   Added for case#3654 - Third option 'Default' in the campus dropdown -End
                        dynaProposalDetailForm.set("versionNumber",new Integer(1));
                        CoeusVector cvBudgetPeriods = performDefaultVersionAction(dynaProposalDetailForm);
                        updateBudget(request ,cvBudgetInfo , cvBudgetPeriods );
                        dynaForm.set("budgetStatusCode","I");
                        updateBudgetStatus(request,dynaForm);
                        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)getBudgetForProposal(dynaProposalDetailForm,request);
                        session.setAttribute("budgetInfoBean", budgetInfoBean );
                        
                        Hashtable htPropInvHeaderData = (Hashtable)webTxnBean.getResults(request,"getProposalInv",hmProposalBudgetData);
                        Vector propInvHeader = (Vector)htPropInvHeaderData.get("getProposalInv");
                        if(propInvHeader!= null && propInvHeader.size() > 0){
                            ProposalBudgetHeaderBean headerBean = (ProposalBudgetHeaderBean)propInvHeader.get(0);
                            session.setAttribute("ProposalBudgetHeaderBean",headerBean);
                        }
                        
                    }
                    /************* Check Sync Budget Persons - Start  ******************/
                    String mode =(String) session.getAttribute("mode"+session.getId());
                    if((hasRight && !hasViewRightOnly) && ( mode !=null && !mode.equals("display")) &&
                    (session.getAttribute("budgetStatusMode")!=null && !session.getAttribute("budgetStatusMode").equals("complete" ))) {
                        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
                        session.setAttribute("syncPageNavigate", "openGetBudget");
                        String  functionType = "NEW_MODE";
                        navigator =   checkBudgetSyncPersons(request,budgetInfoBean,hmRequiredDetails,functionType);
                        if(navigator !=null && !navigator.equals(EMPTY_STRING)){
                            return navigator;
                        }
                        saveBudgetSyncPersons(request);
                        session.removeAttribute("syncPageNavigate");
                    }
                    /* *************** Check Sync Budget Persons - End  ***************/
                    Map mapMenuList = new HashMap();
                    mapMenuList.put("menuItems",CoeusliteMenuItems.BUDGET_MENU_ITEMS);
                    mapMenuList.put("menuCode",CoeusliteMenuItems.BUDGET_PERSONS);
                    setSelectedMenuList(request, mapMenuList);                    
                    navigator = "budgetPersons";
                }
            }else{
                actionMessages.add( "budgetDoesnotExist" , new ActionMessage(
                "error.budgetDoesnotExist") );
                saveMessages(request, actionMessages);
                //Added/Modified for case#2776 - Allow concurrent Prop dev access in Lite - start
                String listPage = request.getParameter("listPage");
                String redirectUrl = "";
                if("Y".equals(listPage)){
                    redirectUrl = "/proposalList.do?PROPOSAL_TYPE=PROPOSAL_INPROGRESS&Menu_Id=003&SUBHEADER_ID=001";
                }else{
                    redirectUrl = "/getGeneralInfo.do?action=noView&proposalNumber=";
                    redirectUrl += proposalNumber;                    
                }
                //Added/Modified for case#2776 - Allow concurrent Prop dev access in Lite - end
                RequestDispatcher rd = request.getRequestDispatcher(redirectUrl);
                rd.forward(request,response);
                return navigator;
            }
        }else{ //Budget Exists
            /* 1. If Budget Exist and there is only one version
             *     a.Verify if Budget has any personnel if no personnel then display Budget Personnel page
             *     b.Else display Budget Summary page
             * 2. If Budget Exist and there are multiple versions then display Budget Version page
             */
            if(hasRight || hasViewRightOnly ){
                Hashtable htPropInvHeaderData = (Hashtable)webTxnBean.getResults(request,"getProposalInv",hmProposalBudgetData);
                Vector propInvHeader = (Vector)htPropInvHeaderData.get("getProposalInv");
                if(propInvHeader!= null && propInvHeader.size() > 0){
                    ProposalBudgetHeaderBean headerBean = (ProposalBudgetHeaderBean)propInvHeader.get(0);
                    session.setAttribute("ProposalBudgetHeaderBean",headerBean);
                }
                if(cvBudgetForProposal!=null && cvBudgetForProposal.size()>0 ){
                    if(cvBudgetForProposal.size() == 1){
                        String mode =(String) session.getAttribute("mode"+session.getId());
                        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)cvBudgetForProposal.get(0);
                        session.setAttribute("budgetInfoBean",budgetInfoBean);
                        int versionNumber = budgetInfoBean.getVersionNumber() ;
                        
                        /************* Check Sync Budget Persons - Start  ******************/
                        
                        if((hasRight && !hasViewRightOnly) && ( mode !=null && !mode.equals("display")) &&
                        (session.getAttribute("budgetStatusMode")!=null && !session.getAttribute("budgetStatusMode").equals("complete" ))) {
                            session.setAttribute("syncPageNavigate", "openGetBudget");
                            String functionType ="MODIFY";
                            navigator =   checkBudgetSyncPersons(request,budgetInfoBean,hmRequiredDetails,functionType);
                            if(navigator !=null && !navigator.equals(EMPTY_STRING)){
                                return navigator;
                            }
                            saveBudgetSyncPersons(request);
                            session.removeAttribute("syncPageNavigate");
                        }
                        /* *************** Check Sync Budget Persons - End  ***************/
                        
                        // Check for Activity Type Change
        
                        String calculate = request.getParameter("calculate");
                        int oldAcivityType =0;
                        int activityTypeChange =0;
                        if( (proposalNumber !=null && !proposalNumber.equals(EMPTY_STRING)) && (cvBudgetForProposal!=null && cvBudgetForProposal.size()>0)){
                            CoeusVector cvActivityType = checkActivityTypeChanged(request, proposalNumber, 1);
                            if(cvActivityType !=null && cvActivityType.size() >0){
                                oldAcivityType = Integer.parseInt((String)cvActivityType.get(0).toString());
                                activityTypeChange = Integer.parseInt((String)cvActivityType.get(1).toString());
                            }
                            if( (calculate !=null && !calculate.equals(EMPTY_STRING)) && !mode.equals("display")){
                                // hmRequiredDetails.put("unitNumber", unitNumber);
                                performBudgetRecalculation(hmRequiredDetails,calculate,oldAcivityType,unitNumber,request);
                            }else if( activityTypeChange == -1  && mode !=null && !mode.equals("display") &&
                            (session.getAttribute("budgetStatusMode")!=null && !session.getAttribute("budgetStatusMode").equals("complete" ))) {
                                if(hasRight && !hasViewRightOnly){
                                    navigator = ACTIVITY_TYPE_CHANGED;
                                    request.setAttribute("pageNavigate", "GetBudgetVersion");
                                    // 3681: Message about proposal's activity type changing - Start
                                    request.setAttribute("oldActivityType", String.valueOf(oldAcivityType));
                                    // 3681: Message about proposal's activity type changing - End
                                    return navigator;
                                }
                            }
                        }
                        
                        //Check salary for persons if 0 navigate to personnel page or 1 navigate to budget summary page
                        
                        int salaryCount = checkSalaryBudgetPerson(request, proposalNumber, versionNumber);
                        //Added for Case#2341 - Recalculate Budget if Project dates change
                        session.setAttribute("CHECK_BUDGET_DATES", "CHECK_BUDGET_DATES");
                        if(salaryCount ==0){
                            navigator = "budgetPersons";
                            //forwarding request through requestDispatcher
//                            String url =  "/getBudgetPersons.do?versionNumber=";
//                            url += versionNumber;
//                            RequestDispatcher rd = request.getRequestDispatcher(url);
//                            rd.forward(request,response);
//                            return null ;
                        }else{
                            String redirectUrl = "";
                            redirectUrl = "/getBudgetSummary.do?versionNumber=";
                            redirectUrl += versionNumber;
                            RequestDispatcher rd = request.getRequestDispatcher(redirectUrl);
                            rd.forward(request,response);
                            return null ;
                        }
                        
                        //Check personnel exists or not
                        
                        
                        //                        Vector vecBudgetPersonnel = getBudgetPersonsData(proposalNumber, versionNumber,request);
                        //                        if(vecBudgetPersonnel == null || vecBudgetPersonnel.size()== 0){
                        //                            //setSelectedStatusBudgetMenu(BUDGET_PERSONS, request);
                        //                            navigator = "budgetPersons";
                        //                        }else{
                        //                            // navigator = getBudgetSummaryData(hmProposalBudgetData,hmRequiredDetails,versionNumber);
                        //                            //  navigator ="getBudgetSummary"
                        //                            String url =  "/getBudgetSummary.do?versionNumber=";
                        //                            url += versionNumber;
                        //                            RequestDispatcher rd = request.getRequestDispatcher(url);
                        //                            rd.forward(request,response);
                        //                            return null ;
                        //                        }
                    }else{
                        ProposalBudgetHeaderBean headerBean =
                        (ProposalBudgetHeaderBean)session.getAttribute("ProposalBudgetHeaderBean");
                        int versionNumber = headerBean.getVersionNumber();
                        /* Get the Budget Info.Get the CoeusVector containing BudgetInfoBeans
                         *Set the BudgetInfoBean for the selected Proposal Number and Version Number
                         *to the session.
                         */
                        BudgetInfoBean budgetInfoBean  = null;
                        budgetInfoBean = filterBudgetInfo(request,cvBudgetForProposal,versionNumber);
                        session.setAttribute("budgetInfoBean",budgetInfoBean);
                        request.setAttribute("PAGE","V");
                        navigator = "budgetVersions";
                    }
                }
                if(hasViewRightOnly){
//                    actionMessages.add( "onlyBudgetViewRight" , new ActionMessage(
//                    "error.onlyBudgetViewRight") );
//                    saveMessages(request, actionMessages);
                    session.setAttribute("mode"+session.getId(), "display");
                    session.setAttribute("versionMode"+session.getId(), "display");
                }
            }else{
                if(!hasViewRightOnly){
                    actionMessages.add( "noModifyAndViewRights" , new ActionMessage(
                    "error.noModifyAndViewRights") );
                    saveMessages(request, actionMessages);
                    //Added/Modified for case#2776 - Allow concurrent Prop dev access in Lite - start
                    String listPage = request.getParameter("listPage");
                    String redirectUrl = "";
                    if("Y".equals(listPage)){
                        redirectUrl = "/proposalList.do?PROPOSAL_TYPE=PROPOSAL_INPROGRESS&Menu_Id=003&SUBHEADER_ID=001";
                    }else{
                        redirectUrl = "/getGeneralInfo.do?action=noView&proposalNumber=";
                        redirectUrl += proposalNumber;                        
                    }     
                    //Added/Modified for case#2776 - Allow concurrent Prop dev access in Lite - end
                    RequestDispatcher rd = request.getRequestDispatcher(redirectUrl);
                    rd.forward(request,response);
                    return null ;
                    
                }
            }
        }//End Else
        return navigator;
    }
    
    /**
     * For the Budget Equipment/Trainee/Travel Line Item Details
     * @return String navigator to forward
     * @param hmDynaFormDetails map contain DynaForm instance
     * @param hmProposalBudgetData contain proposal and version number
     * @throws Exception if any error occurs
     */
    
    private String getBudgetEquipementData(HashMap hmProposalBudgetData
    , HashMap hmDynaFormDetails, HttpServletRequest request) throws Exception{
        Vector vecFilterBudgetPeriod = null;
        Vector vecBudgetEquiDetails = null;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        // DynaValidatorForm dynaForm = (DynaValidatorForm)hmDynaFormDetails.get(DynaValidatorForm.class);
        CoeusDynaBeansList coeusLineItemDynaList
        = (CoeusDynaBeansList)hmDynaFormDetails.get(ActionForm.class);
        
        
        int prevBudgetPeriod = 0;
        int budgetPeriod = 0;
        String navigator = EMPTY_STRING;
        if(session.getAttribute("BudgetDetailData") != null){
            vecBudgetEquiDetails = (Vector)session.getAttribute("BudgetDetailData");
        }
        if(request.getParameter("OldPeriod") != null){
            prevBudgetPeriod = Integer.parseInt(request.getParameter("OldPeriod"));
        }
        if(request.getParameter("Period") != null){
            budgetPeriod = Integer.parseInt(request.getParameter("Period"));
        }
        if(request.getAttribute("dataNotModified") != null ){
            prevBudgetPeriod = budgetPeriod;
        }
        vecFilterBudgetPeriod = filterBudgetPeriod(prevBudgetPeriod, vecBudgetEquiDetails);

        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
        //Vector vecCEData = (Vector)session.getAttribute("costElementData");
        Vector vecCEData = (Vector)session.getAttribute("costElementAllData");
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
        Vector vecFilterBudgetDetailToShow = getExistingCE(vecFilterBudgetPeriod, vecCEData);
        //If user want to see only line items details without modify the data
        if(request.getParameter("Edit") != null){
            int lineItemNo = Integer.parseInt(request.getParameter("lineItemNumber"));
            request.setAttribute("lineItemNumber", new Integer(lineItemNo));
            request.setAttribute("popUp","fromLocation");
        }
        //To check data is modified. If not then don't make server call
        if(request.getAttribute("dataNotModified") != null){
            request.setAttribute("FilterBudgetDetailData", vecFilterBudgetDetailToShow);
            request.setAttribute("SelectedBudgetPeriodNumber", new Integer(prevBudgetPeriod));
            //validation framework start
            coeusLineItemDynaList.setList(vecFilterBudgetDetailToShow);
            List beanList = new ArrayList();
            beanList.add(new Integer(budgetPeriod));
            coeusLineItemDynaList.setBeanList(beanList);
            session.setAttribute("lineItemDynaBeanList",coeusLineItemDynaList);
            //validation framework end            
            navigator = "success";
            return navigator;
        }
        
        /*Commented for Budget Flow. Now Proposal Number and version will be taken from
         *selected Budget Info Bean(From versions window). Note : For any versions, budgetInfobean will be set in session */
        //ProposalBudgetHeaderBean headerBean = (ProposalBudgetHeaderBean)session.getAttribute("ProposalBudgetHeaderBean");
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        String page = null;
        // newly added code Get the IRBSession Page Constant
        if(session.getAttribute("pageConstantValue")!= null){
            page = (String)session.getAttribute("pageConstantValue");
        }
        if(request.getParameter(CoeusLiteConstants.PAGE) != null){
            page  = request.getParameter(CoeusLiteConstants.PAGE);
        }
        //Now get the proposal and version number from budget info bean.
        String propNum = budgetInfoBean.getProposalNumber();
        int versionNumber = budgetInfoBean.getVersionNumber();
        hmProposalBudgetData.put("proposalNumber",propNum);
        hmProposalBudgetData.put("versionNumber",new Integer(versionNumber));
        Hashtable htBudgetData =(Hashtable)webTxnBean.getResults(request,
        "getBudgetLineItem", hmProposalBudgetData);
        
        vecBudgetEquiDetails = (Vector)htBudgetData.get("getBudgetDetail");
        //modified web txn bean for On off Campus flag start
        //        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        //        CoeusVector cvBudgetDetails = budgetDataTxnBean.getBudgetDetail(propNum, versionNumber);
        //
        //        if(cvBudgetDetails != null){
        //            vecBudgetEquiDetails = new Vector();
        //            BeanUtilsBean copyBean = null;
        //            BudgetDetailBean budgetDetailBean = null;
        //            DynaBean dynaBean = null;
        //            for(int index = 0; index < cvBudgetDetails.size(); index++){
        //                budgetDetailBean = (BudgetDetailBean)cvBudgetDetails.get(index);
        //                if(budgetDetailBean.getAcType() == null){
        //                    budgetDetailBean.setAcType(EMPTY_STRING);
        //                }
        //                DynaActionForm dynaFormData = coeusLineItemDynaList.getDynaForm(request);
        //                dynaBean = ((DynaBean)dynaFormData).getDynaClass().newInstance();
        //                copyBean = new BeanUtilsBean();
        //                copyBean.copyProperties(dynaBean,budgetDetailBean);
        //                vecBudgetEquiDetails.addElement(dynaBean);
        //            }
        //        }
        //modified web txn bean for On off Campus flag end
        Vector vecBudgetPeriod = (Vector)htBudgetData.get("budgetPeriodData");
        Vector vecBudgetCalAmts = (Vector)htBudgetData.get("getBudgetDetailCalAmts");
    
        //remove the session for line items, if user click on equipment link
        session.removeAttribute("DeletedBudgetDetailsData");
        session.removeAttribute("BudgetDetailData");
        session.removeAttribute("BudgetDetailCalAmts");
        session.removeAttribute("BudgetPeriodData");
        //To get the Cost Elements  Data
        String categoryCode = EMPTY_STRING;
        HashMap hmCostElementData = new HashMap();
        MessageResources messages = MessageResources.getMessageResources(BUDGET_MESSAGE_FILE_NAME);
        
        //Added for Case 3197 - Allow for the generation of project period greater than 12 months -start
            vecBudgetPeriod = calculatePeriodNumberOfMonths(vecBudgetPeriod);            
        //3197 - end
        
        if(page !=null) {
            // added for new setSelectedMenuList from coeusBaseAction
            Map mapMenuList = new HashMap();
            mapMenuList.put("menuItems",CoeusliteMenuItems.BUDGET_MENU_ITEMS);
            
            if( page.trim().equalsIgnoreCase(CoeusLiteConstants.EQUIPMENT)) {
                categoryCode = messages.getMessage(EQUIPMENT_TARGET_CODE);
                // set the selected status                
                mapMenuList.put("menuCode",CoeusliteMenuItems.EQUIPMENT_CODE);
                setSelectedMenuList(request, mapMenuList);
                
            }
            else if( page.trim().equalsIgnoreCase(CoeusLiteConstants.TRAVEL)) {
                categoryCode = messages.getMessage(TRAVEL_TARGET_CODE);   // Travel - Domestic
                // set the selected status                
                mapMenuList.put("menuCode",CoeusliteMenuItems.TRAVEL_CODE);
                setSelectedMenuList(request, mapMenuList);
                
                
            }
            else if( page.trim().equalsIgnoreCase(CoeusLiteConstants.PARTICIPANT_TRAINEE)) {
                categoryCode = messages.getMessage(PARTICIPANT_TRAINEE_TARGET_CODE);  // Trainee/Participant Costs - Stipends
                // set the selected status
                mapMenuList.put("menuCode",CoeusliteMenuItems.PARTICIPANT_TRAINEE_CODE);
                setSelectedMenuList(request, mapMenuList);
                
            }else if(page.trim().equalsIgnoreCase(CoeusLiteConstants.OTHER_DIRECT_COSTS)){
                
                categoryCode = messages.getMessage(OTHER_DIRECT_COSTS_TARGET_CODE); //Other Direct Cost category code
                // set the selected status
                mapMenuList.put("menuCode",CoeusliteMenuItems.OTHER_DIRECT_COSTS_CODE);
                setSelectedMenuList(request, mapMenuList);
            }
            String mappingName = messages.getMessage(MAPPING_NAME);
            hmCostElementData.put("categoryCode",categoryCode);
            hmCostElementData.put("mappingName",mappingName);
        } // end of if(page!=null)
        
        
        Hashtable htCostData =
        (Hashtable)webTxnBean.getResults(request, "getBudgetCostElementData", hmCostElementData);
        Vector vecCostElements = (Vector)htCostData.get("getBudgetCostElementData");
        //COEUSQA-1414 Allow schools to indicate if cost element is still active - Start
        session.setAttribute("costElementAllData" , vecCostElements);
        if(vecCostElements != null && vecCostElements.size() > 0){            
            messages = MessageResources.getMessageResources(PROPOSAL_MESSAGE_FILE_NAME);
            CategoryBean defaultBean = new CategoryBean();
            defaultBean.setCostElement(EMPTY_STRING);
            defaultBean.setDescription(messages.getMessage(PLEASE_SELECT_CODE));
            defaultBean.setBudgetCategoryCode(0);
            defaultBean.setCampusFlag(OFF_CAMPUS_FLAG);
            defaultBean.setActiveFlag("");
            vecCostElements.insertElementAt(defaultBean, 0);
            Vector vecFilteredCostElement = new Vector();
            for(Object obj:vecCostElements){
                defaultBean = (CategoryBean)obj;
                if("Y".equals(defaultBean.getActiveFlag())
                   || "".equals(defaultBean.getActiveFlag())){
                    vecFilteredCostElement.add(defaultBean);
                }
            }
            session.setAttribute("costElementData" , vecFilteredCostElement);
        }else{
            navigator = "noCostElements";
            return navigator;
        }
        //COEUSQA-1414 Allow schools to indicate if cost element is still active - End
        budgetPeriod = MAKE_DEFAULT_PERIOD;
        if(request.getParameter("Period") != null){
            budgetPeriod = Integer.parseInt(request.getParameter("Period"));
        }
        // Check for periods is generated or not. If not show only default period. start
        Vector vecAllPeriods = getPeriodForShowTab(vecBudgetPeriod);
        boolean dataExist = false;
        if(vecAllPeriods != null && vecAllPeriods.size() > 0){
            for(int index = 0; index < vecAllPeriods.size(); index++){
                int budPeriod = ((Integer)vecAllPeriods.get(index)).intValue();
                Vector vecFilterTabBudgetPeriod =  filterBudgetPeriod(budPeriod,vecBudgetEquiDetails);
                if(vecFilterTabBudgetPeriod.size() > 0){
                    dataExist = true;
                    break;
                }
            }
        }
        if(!dataExist){
            vecBudgetPeriod = filterBudgetPeriod(MAKE_DEFAULT_PERIOD, vecBudgetPeriod);
            budgetPeriod = MAKE_DEFAULT_PERIOD;
        }
        vecFilterBudgetPeriod =  filterBudgetPeriod(budgetPeriod,vecBudgetEquiDetails);
        vecFilterBudgetDetailToShow = getExistingCE(vecFilterBudgetPeriod, vecCostElements);
        // Check for periods is generated or not. If not show only default period. end
        // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
        setIsFormulatedCostLineItem(vecFilterBudgetDetailToShow,session);
        // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
        // set the budget Periods for the extarction and identification of the budget period
        session.setAttribute("BudgetPeriodData", vecBudgetPeriod );
        //validation framework start
        coeusLineItemDynaList.setList(vecFilterBudgetDetailToShow);
        List beanList = new ArrayList();
        beanList.add(new Integer(budgetPeriod));
        coeusLineItemDynaList.setBeanList(beanList);
        session.setAttribute("lineItemDynaBeanList",coeusLineItemDynaList);
        
        //validation framework end
        session.setAttribute("dataChanges", new Boolean(false));
        request.setAttribute("FilterBudgetDetailData", vecFilterBudgetDetailToShow);
        //Set the selected period
        request.setAttribute("SelectedBudgetPeriodNumber", new Integer(budgetPeriod));
        // Set the page Value
        session.setAttribute("pageConstantValue", page);
        // Set the Budget details data
//        Vector vecTemp = (Vector) session.getAttribute("costElementData");
//        if(vecBudgetEquiDetails != null && !vecBudgetEquiDetails.isEmpty()){
//            DynaActionForm dynaFormData = new DynaActionForm();                   
//            CategoryBean defaultBean = new CategoryBean();
//            Vector vecNewCostElement = new Vector();
//            for(Object obj:vecBudgetEquiDetails){
//                dynaFormData = (DynaActionForm)obj;
//                defaultBean.setBudgetCategoryCode(Integer.parseInt(dynaFormData.get("budgetCategoryCode").toString()));
//                defaultBean.setCostElement(dynaFormData.get("costElement").toString());
//                defaultBean.setDescription(dynaFormData.get("costElementDescription").toString());
//                defaultBean.setCampusFlag(dynaFormData.get("onOffCampusFlag").toString());
//                defaultBean.setActiveFlag("");
//                vecNewCostElement.add(defaultBean);
//            }
//        }
        session.setAttribute("BudgetDetailData", vecBudgetEquiDetails);
        //set the calculated Amts to session
        session.setAttribute("BudgetDetailCalAmts", vecBudgetCalAmts);
        // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        CoeusVector cvFormulatedCostDetails = budgetDataTxnBean.getBudgetFormulatedDetail(propNum,versionNumber);
        session.setAttribute("FormulatedCostDetails", cvFormulatedCostDetails);
        // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
        if(request.getParameter("Edit") != null){
            int lineItemNo = Integer.parseInt(request.getParameter("lineItemNumber"));
            request.setAttribute("lineItemNumber", new Integer(lineItemNo));
            request.setAttribute("popUp","fromLocation");
        }
        //checking for display mode. If Display mode then don't add Default line Item
        String mode = (String)session.getAttribute("mode"+session.getId());
        String budgetStatusMode = (String)session.getAttribute("budgetStatusMode");
        if((mode !=null && mode.equals("display")) ||
        (budgetStatusMode != null && budgetStatusMode.equals("complete"))){
            navigator = "success";
            return navigator;
        }
        navigator = "success";
        if(vecFilterBudgetDetailToShow == null || vecFilterBudgetDetailToShow.size() == 0){
            //            String url =  "/AddLineItem.do?AddBudgetPeriodRow=";
            //            url += budgetPeriod;
            //            RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
            //            requestDispatcher.forward(request,response);
            //            return null;
            navigator = addDefaultLineItem(coeusLineItemDynaList, budgetPeriod,
                    request, (ActionForm)hmDynaFormDetails.get(ActionForm.class));
        }
        return navigator;
    }
    
    /**
     * To get the Budget Persons Data
     * @param proposalNumber proposal Number
     * @param versionNumber version number
     * @throws Exception if any error occurs
     * @return Vector of budget Persons
     */
    private Vector getBudgetPersonsData(String proposalNumber,int versionNumber,
        HttpServletRequest request)throws Exception{
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
     * This method is to create a new budget version
     * @param hmRequiredDetails contain dyna form and action mapping
     * @param dynaProposalDetailForm instance of dyna Proposal Form
     * @throws Exception if any error occurs
     * @return vector contain Budget Info Data
     */
    private CoeusVector getNewBudgetInfoData( HashMap hmRequiredDetails ,
    DynaActionForm dynaProposalDetailForm, HttpServletRequest request)throws Exception{
        Timestamp dbTimestamp = prepareTimeStamp();
        CoeusDynaBeansList coeusSyncPersonsDynaList = (CoeusDynaBeansList) hmRequiredDetails.get(ActionForm.class);
        DynaActionForm budgetDynaForm = coeusSyncPersonsDynaList.getDynaForm(request,"budgetSummary");
        // DynaActionForm budgetDynaForm = (DynaActionForm)hmRequiredDetails.get(DynaActionForm.class);
        budgetDynaForm.set("proposalNumber",dynaProposalDetailForm.get("proposalNumber"));
        budgetDynaForm.set("versionNumber",new Integer(1)) ;
        budgetDynaForm.set("startDate",dynaProposalDetailForm.get("requestedStartDateInitial"));
        budgetDynaForm.set("endDate",dynaProposalDetailForm.get("requestedEndDateInitial"));
        budgetDynaForm.set("ohRateClassCode",new Integer(1));
        budgetDynaForm.set("acType","I");
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
     // Case Id 2924 - start
        budgetDynaForm.set("onOffCampusFlag", "D");
        
     // Case Id 2924 - end
        budgetDynaForm.set("ohRateTypeCode",new Integer(1));
        budgetDynaForm.set("urRateClassCode",new Integer(1));
        //COEUSQA-1693 - Cost Sharing Submission - start
// JM 5-27-2011 updated checkbox to default to "N" 
        budgetDynaForm.set("submitCostSharingFlag","N");
// JM END
        //COEUSQA-1693 - Cost Sharing Submission - end
        //Make directly budgetinfobean
        CoeusVector cvBudgetInfo = getBudgetInfoBean(budgetDynaForm);
        return cvBudgetInfo ;
    }
    
    
    /**
     * Default will create the periods based on Budget start date and budget end
     * date.It spanns the start date and end date by one year and break up of year
     * month and days are generated
     * @param dynaProposalDetailForm instance of Proposal Dyna Form
     * @throws Exception if any error occurs
     * @return vector of budget periods
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
     * This method is to get the budget summary data.
     * @param hmProposalBudgetData containg proposalNumber and VersionNumber
     * @param hmRequiredDetails contain dyna form and action mapping object
     * @param versionNumber version number for budget information
     * @throws Exception if any error occurs
     * @return navigator to summary page
     */
    //Modified for COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-Start
    private String getBudgetSummaryData(ActionForm actionForm, HashMap hmProposalBudgetData,
    HashMap hmRequiredDetails ,int versionNumber, HttpServletRequest request)throws Exception{
    //Modified for COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-End
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        // Added for Cost Sharing Distribution Start
        boolean forceCostSharing = false;
        String forceValidation = "notForce";
        String validationMsg = " ";
        // Added for Cost Sharing Distribution End
        //Added for Under Recovery Distribution Start
        boolean forceUR = false;
        String validationURMsg = " ";
        //Added for Under Recovery Distribution End
        WebTxnBean webTxnBean = new WebTxnBean();
        DynaValidatorForm dynaProposalDetailForm = null;
        //COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-Start        
        CoeusDynaBeansList budgetSummaryDynaList = (CoeusDynaBeansList) actionForm;
        DynaActionForm dynaForm = budgetSummaryDynaList.getDynaForm(request,"budgetSummary");//
        //DynaValidatorForm dynaForm = (DynaValidatorForm)hmRequiredDetails.get(DynaValidatorForm.class);
        //COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-End    
        String versionIndex = request.getParameter("versionNumber");
        BudgetInfoBean budgetInfo = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        String proposalNumber = budgetInfo.getProposalNumber();
        //Added for Case COEUSDEV-134 - Lite proposal -- EMPTY fields on Budget Summary screen - Start
        if(versionNumber == -1 && versionIndex != null){
            versionNumber = Integer.parseInt(versionIndex);
        }
        
        if(request.getAttribute("versionNumber")!=null){
            versionNumber = ((Integer)request.getAttribute("versionNumber")).intValue();
        }
        if(versionNumber == -1){
            versionNumber = budgetInfo.getVersionNumber();
        }
         //Added for Case COEUSDEV-134 - Lite proposal -- EMPTY fields on Budget Summary screen - End
        /* Get the Budget Info.Get the CoeusVector containing BudgetInfoBeans
         *Set the BudgetInfoBean for the selected Proposal Number and Version Number
         *to the session.
         */
        CoeusVector cvBudgetForProposal = getBudgetInfo(request , proposalNumber);
        Vector vecProposalDetails =  getProposalDetailsForBudget( request , proposalNumber);
        
        if(vecProposalDetails!=null && vecProposalDetails.size() > 0){
            dynaProposalDetailForm = (DynaValidatorForm)vecProposalDetails.get(0);
        }

        hmProposalBudgetData.put("proposalNumber",proposalNumber);
        hmProposalBudgetData.put("versionNumber",new Integer(versionNumber));
        Hashtable htBudgetSummaryData =
        (Hashtable)webTxnBean.getResults(request,"getPropBudgetData",hmProposalBudgetData);
        
        Vector vecBudgetInfo = (Vector)htBudgetSummaryData.get("getBudgetInfoData");
        if(vecBudgetInfo!= null && vecBudgetInfo.size() > 0){
            DynaValidatorForm serverDynaData = (DynaValidatorForm)vecBudgetInfo.get(0);
            serverDynaData.set("requestedStartDateInitial",serverDynaData.get("startDate"));
            serverDynaData.set("requestedEndDateInitial",serverDynaData.get("endDate"));
            serverDynaData.set("appointmentStartDate",serverDynaData.get("startDate"));
            serverDynaData.set("appointmentEndDate",serverDynaData.get("endDate"));
            serverDynaData.set("effectiveDate",serverDynaData.get("endDate"));
              //COEUSQA-3965   
             if(request.getParameter("statusChange")!=null && request.getParameter("statusChange").equalsIgnoreCase("C")){
                List lstBudgetSummary = budgetSummaryDynaList.getList();
                dynaForm = (DynaValidatorForm) lstBudgetSummary.get(0);
                }else{
                BeanUtilsBean beanCopy = new BeanUtilsBean();
                beanCopy.copyProperties(dynaForm,serverDynaData);
                     }
            //Added for case#3654 - Third option 'Default' in the campus dropdown - start
            String bsOnOffCampusFlag = (String)serverDynaData.get("bsOnOffCampusFlag");
            String onOffCampusFlag = (String)serverDynaData.get("onOffCampusFlag");
            if(bsOnOffCampusFlag.equals("D")){
                onOffCampusFlag = bsOnOffCampusFlag;
            }            
            dynaForm.set("onOffCampusFlag", onOffCampusFlag);
            //Added for case#3654 - Third option 'Default' in the campus dropdown - end
        }
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.BUDGET_MENU_ITEMS);
        Vector vecBudgetPeriodData=new Vector();
        if(request.getParameter("statusChange")==null){
        vecBudgetPeriodData=(Vector)htBudgetSummaryData.get("getBudgetPeriodData");
        }
        Vector vecRateClass = (Vector)htBudgetSummaryData.get("getRateClass");
        BudgetInfoBean budgetInfoBean = filterBudgetInfo(request,cvBudgetForProposal,versionNumber);
        //Added for Case#2341 - Recalculate Budget if Project dates change - starts
        String value = (String) request.getSession().getAttribute("CHECK_BUDGET_DATES");
        request.getSession().removeAttribute("CHECK_BUDGET_DATES");
        if("CHECK_BUDGET_DATES".equals(value)){
            if(validateForProjectDates(request)){
                request.setAttribute("CHANGE_BUDGET_DATES", "CHANGE_BUDGET_DATES");
            }
        }
        //Added for Case#2341 - Recalculate Budget if Project dates change - ends
        // Added for Cost Sharing Distribution Start
            double totCostSharingAmt = budgetInfoBean.getCostSharingAmount();
            HashMap hmGetParamValue = new HashMap();
            hmGetParamValue.put("forceCostSharingDistribution","FORCE_COST_SHARING_DISTRIBUTION");
            Hashtable htCostSharing = (Hashtable)webTxnBean.getResults(request,"getForceCostSharing",hmGetParamValue);
            hmGetParamValue =(HashMap) htCostSharing.get("getForceCostSharing");
            String forceCSD = (String)hmGetParamValue.get("ls_value");
            
            HashMap hmCostSharing = new HashMap();
            Vector vecCostSharingDistriData =  getCostSharing(request,hmCostSharing,proposalNumber,versionNumber);
            //Added for Case 3197 - Allow for the generation of project period greater than 12 months -start
            //3197 - end
            
            //if the vector is empty and the parameter value is zero,
            //allow the user to set the Budget Version as Final and Budget Status as Complete
            //without firing validation
            if(vecCostSharingDistriData != null){
            if(vecCostSharingDistriData.size() <= 0){
                
                if(!forceCSD.equals(EMPTY_STRING) && forceCSD != null && forceCSD.equals("1")){
                    // Modified for case COEUSQA-2784_Lite is forcing users to distribute underrecovery when the proposal has negative underrecovery_start
                    if(totCostSharingAmt <= 0){
                    // Modified for case COEUSQA-2784_Lite is forcing users to distribute underrecovery when the proposal has negative underrecovery_end    
                        forceCostSharing = false;
                        forceValidation = "notForce";
                    }else{
                    forceCostSharing = true;
                    forceValidation = "force";
                    validationMsg = "noCSD";
                    }
                }
                else{
                    forceCostSharing = false;
                    forceValidation = "notForce";
                }
            }else{
                    // else allow the user to set the Budget Version as Final and Budget Status as Complete
                    //and fire a validation 
                double formCostSharingAmt = 0.0;
                    for(int index=0; index < vecCostSharingDistriData.size();index++){
                        DynaValidatorForm dynaCostForm = (DynaValidatorForm)vecCostSharingDistriData.get(index);
                        formCostSharingAmt = formCostSharingAmt + ((Double)dynaCostForm.get("costSharingAmount")).doubleValue();
                    }
                    //COEUSDEV-159 Lite - cost share warning -won't allow complete status in lite
                    formCostSharingAmt = ((double)Math.round(formCostSharingAmt * Math.pow(10.0, 2) )) / 100;
                    //COEUSDEV-159 End
                    if(formCostSharingAmt != totCostSharingAmt){
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
                }// end of else
            }
            
            session.setAttribute("ForceCSDInBudgetSummary",forceValidation);
            session.setAttribute("CSDSummaryValidationMsg",validationMsg);
        //Added for Cost Sharing distribution End
        
        // Added for Under Recovery Distribution Start
            double totUnderRecAmt = budgetInfoBean.getUnderRecoveryAmount();
            hmGetParamValue.put("forceUnderRecoveryDistribution","FORCE_UNDER_RECOVERY_DISTRIBUTION");
            Hashtable htUnderRecovery = (Hashtable)webTxnBean.getResults(request,"getForceUnderRecovery",hmGetParamValue);
            hmGetParamValue =(HashMap) htUnderRecovery.get("getForceUnderRecovery");
            String forceURD = (String)hmGetParamValue.get("ls_value");
            System.out.println("forceURD in summary action : "+forceURD);
            
            Vector vecUnderRecData =  (Vector)getUnderRecovery(request,proposalNumber,versionNumber);
            //if the vector is empty and the parameter value is zero,
            //allow the user to set the Budget Version as Final and Budget Status as Complete
            //without firing validation
            if(vecUnderRecData != null){
            if(vecUnderRecData.size() <= 0){
                
                if(!forceURD.equals(EMPTY_STRING) && forceURD != null && forceURD.equals("1")){
                    // Modified for case COEUSQA-2784_Lite is forcing users to distribute underrecovery when the proposal has negative underrecovery_start
                    if(totUnderRecAmt <= 0){
                    //Modified for case COEUSQA-2784_Lite is forcing users to distribute underrecovery when the proposal has negative underrecovery_end
                        forceUR = false;
                        forceValidation = "notForce";
                    }else{
                    forceUR = true;
                    forceValidation = "force";
                    validationURMsg = "noUnderRec";
                    }
                }
                else{
                    forceUR = false;
                    forceValidation = "notForce";
                }
            }else{
                    // else allow the user to set the Budget Version as Final and Budget Status as Complete
                    //and fire a validation 
                double formUnderRecAmt = 0.0;
                    for(int index=0; index < vecUnderRecData.size();index++){
                        DynaValidatorForm dynaCostForm = (DynaValidatorForm)vecUnderRecData.get(index);
                        formUnderRecAmt = formUnderRecAmt + ((Double)dynaCostForm.get("underRecoveryAmt_wmc")).doubleValue();
                    }
                    //COEUSDEV-159 Lite - cost share warning -won't allow complete status in lite
                    formUnderRecAmt = ((double)Math.round(formUnderRecAmt * Math.pow(10.0, 2) )) / 100;
                    //COEUSDEV-159 end
                    if(formUnderRecAmt != totUnderRecAmt){
                           if(!forceURD.equals(EMPTY_STRING) && forceURD != null && forceURD.equals("1")){
                                forceUR = true;
                                forceValidation = "force";
                                validationURMsg = "amtUnequal";
                            } 
                           else{
                                forceUR = false;
                                forceValidation = "notForce";
                           }
                    }else{
                               forceUR = false;
                                forceValidation = "notForce";
                    }
                }// end of else
            }
            
            session.setAttribute("forceURDInBudgetSummary",forceValidation);
            session.setAttribute("URDSummaryValidationMsg",validationURMsg);
        //Added for Under Recovery Distribution End
             //COEUSQA-3965
        if(request.getParameter("statusChange")!=null && request.getParameter("statusChange").equalsIgnoreCase("C")){
        List lstBudgetPeriodSummary = budgetSummaryDynaList.getBeanList();
        for(int i=0;i<lstBudgetPeriodSummary.size();i++){
        DynaValidatorForm dynaBudgetPeriod = (DynaValidatorForm) lstBudgetPeriodSummary.get(i);
        Double totalDirectCost = formatStringToDouble((String) dynaBudgetPeriod.get("strTotalDirectCost"));
        Double totalIndirectCost  = formatStringToDouble((String) dynaBudgetPeriod.get("strTotalIndirectCost"));
        Double totalCost = totalDirectCost+totalIndirectCost;
        dynaBudgetPeriod.set("totalDirectCost", formatStringToDouble((String) dynaBudgetPeriod.get("strTotalDirectCost")));
        dynaBudgetPeriod.set("totalIndirectCost", formatStringToDouble((String) dynaBudgetPeriod.get("strTotalIndirectCost")));
        dynaBudgetPeriod.set("underRecoveryAmount", formatStringToDouble((String) dynaBudgetPeriod.get("strUnderRecoveryAmount")));
        dynaBudgetPeriod.set("costSharingAmount", formatStringToDouble((String) dynaBudgetPeriod.get("strCostSharingAmount")));
        dynaBudgetPeriod.set("totalCost", totalCost);
        vecBudgetPeriodData.add(dynaBudgetPeriod);
        }}else{
          
          vecBudgetPeriodData = calculatePeriodNumberOfMonths(vecBudgetPeriodData);
        //COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-Start
        HashMap hmParamValue = new HashMap();             
        hmParamValue.put("proposalNumber",proposalNumber);
        hmParamValue.put("versionNumber",versionNumber);
        Hashtable htDCModularData = (Hashtable)webTxnBean.getResults(request,"fetchBudgetDetail",hmParamValue);
        Vector vecDIPeriodData  =(Vector) htDCModularData.get("fetchBudgetDetail"); 
        if(vecBudgetPeriodData != null && !vecBudgetPeriodData.isEmpty() 
            && vecDIPeriodData != null && !vecDIPeriodData.isEmpty()){
            DynaValidatorForm newDynaForm = new DynaValidatorForm();
            DynaValidatorForm dynaFormPeriodData = new DynaValidatorForm();
            for(Object obj:vecBudgetPeriodData){
                newDynaForm = (DynaValidatorForm)obj;
                    for(Object objDIData:vecDIPeriodData){
                        dynaFormPeriodData = (DynaValidatorForm)objDIData;
                        if(newDynaForm.get("budgetPeriod").toString().equals(dynaFormPeriodData.get("budgetPeriod").toString())){
                            newDynaForm.set("modularBudgetFlag","Y");
                    }
                }            
            }
        }       
     }
        //COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-End
        session.setAttribute("budgetInfoBean",budgetInfoBean);
        session.setAttribute("budgetStatus", getVecBudgetStatus());
        session.setAttribute("rateClass", vecRateClass);
        session.setAttribute("campusFlag", getVecCampusFlag());  // Case id# 2924
        //COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-Start
        //request.setAttribute("budgetPeriodData", vecBudgetPeriodData);
        session.setAttribute("budgetPeriodData", vecBudgetPeriodData);
        //COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-End
        request.setAttribute("budgetInfoData", dynaForm);
        //Added with Case 2158: Budget Validations
        //This happens when the user wants to change the budget status to complete inspite of validation warnings.
        String statusCode = (String)request.getParameter("statusChange");
        if(statusCode==null || EMPTY_STRING.equals(statusCode.trim())){
            statusCode = (String)dynaProposalDetailForm.get("budgetStatusCode");
        }else{
            request.setAttribute("statusUpdated","Y");
        }
        dynaForm.set("budgetStatusCode",statusCode);
        //2158 End
        dynaForm.set("unitNumber",dynaProposalDetailForm.get("unitNumber"));
        dynaForm.set("activityTypeCode",dynaProposalDetailForm.get("activityTypeCode"));
        //Commented for case#3654 - Third option 'Default' in the campus dropdown - start
//      // Case id# 2924 - start
//        if (budgetInfoBean.isOnOffCampusFlag()){
//            dynaForm.set("onOffCampusFlag","Y");
//        }else{
//            dynaForm.set("onOffCampusFlag","N");
//        }
//      // Case id# 2924 - end            
        //Commented for case#3654 - Third option 'Default' in the campus dropdown - end
        //COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-Start     
        Vector vecBudgetSummaryDetailedData = new Vector();
        vecBudgetSummaryDetailedData.add(dynaForm);
        budgetSummaryDynaList.setList(vecBudgetSummaryDetailedData);  
        budgetSummaryDynaList.setBeanList(vecBudgetPeriodData);
        session.setAttribute("budgetSummaryDynaList", budgetSummaryDynaList);
        //COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-End
        navigator = "budgetSummary";
        mapMenuList.put("menuCode",CoeusliteMenuItems.SUMMARY_CODE );
        setSelectedMenuList(request, mapMenuList);
        return navigator;
    }
    
    /**
     * Convert the DynaForm to BudgetInfoBean
     * @param dynaProposalForm Dyna Form of proposal
     * @throws Exception if any error occurs
     * @return CoeusVector of BudgetInfoBean
     */
    private CoeusVector getBudgetInfoBean(DynaActionForm dynaProposalForm) throws Exception{
        CoeusVector cvBudgetInfo = new CoeusVector();
        BudgetInfoBean budgetInfoBean = new BudgetInfoBean();
        BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
        beanUtilsBean.copyProperties(budgetInfoBean, dynaProposalForm);
        cvBudgetInfo.add(budgetInfoBean);
        return cvBudgetInfo;
    }
    
    /**
     * This method will perform budget recalculation
     * navigates to the respective ActionForward
     * @param unitNumber Proposal Unit Number
     * @param hmRequiredDetails map contain action form details
     * @param calculate activity change action
     * @param oldAcivityType old Activity Code
     * @throws Exception if any error occurs
     */
    private void performBudgetRecalculation(HashMap hmRequiredDetails,String calculate,
        int oldAcivityType,String unitNumber, HttpServletRequest request)throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user"+session.getId());
        boolean isSuccessFulModified = false;
        // DynaValidatorForm dynaForm = (DynaValidatorForm)hmRequiredDetails.get(DynaValidatorForm.class);
        CoeusDynaBeansList coeusSyncPersonsDynaList = (CoeusDynaBeansList) hmRequiredDetails.get(ActionForm.class);
        DynaActionForm dynaForm = coeusSyncPersonsDynaList.getDynaForm(request,"budgetSummary");
        
        ProposalBudgetHeaderBean headerBean = (ProposalBudgetHeaderBean)session.getAttribute("ProposalBudgetHeaderBean");
        String proposalNumber = headerBean.getProposalNumber();
        dynaForm.set("unitNumber",unitNumber);
        dynaForm.set("proposalNumber",proposalNumber);
        dynaForm.set("versionNumber",new Integer(1));
        
        BudgetInfoBean budgetInfoBean = new BudgetInfoBean();
        
        budgetInfoBean = getBudgetForProposal(dynaForm,request);
        //Added for removing instance variable -case # 2960 - start
        QueryEngine queryEngine = QueryEngine.getInstance();
        String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        //Added for removing instance variable - case # 2960- end                
        budgetInfoBean.setActivityTypeCode(oldAcivityType);
        //if calculate is Yes set the new activity type to budgetInfoBean
        if( (calculate !=null && !calculate.equals(EMPTY_STRING)) &&  calculate.equals("Y")){
            int proposalActivityType = getActivityTypeForProposal(request,proposalNumber);
            int oldActivityTypeCode = budgetInfoBean.getActivityTypeCode();
            budgetInfoBean.setActivityTypeCode(proposalActivityType);
            Hashtable htBudgetData = initializeBudgetCalculator(budgetInfoBean);
            queryEngine.addDataCollection(queryKey, htBudgetData);
            Equals eqOldATCode = new Equals("activityCode", new Integer(oldActivityTypeCode));
            queryEngine.setUpdate(queryKey, ProposalRatesBean.class, "acType", String.class, TypeConstants.DELETE_RECORD, eqOldATCode);
            
            CoeusVector cvNewProposalRates = getProposalRatesBean(budgetInfoBean);
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
        
    }
    /**
     *
     * Method to Add Default line Item to budget Details
     * @return String to naviagate page
     * @param budgetPeriod selected Period
     * @param dynaForm instance of action form
     * @throws Exception if any error occurs
     */
    private String addDefaultLineItem(CoeusDynaBeansList dynaForm, int budgetPeriod,
        HttpServletRequest request, ActionForm actionForm) throws Exception{
        HttpSession session = request.getSession();
        Vector vecBudgetPeriodData = (Vector)session.getAttribute("BudgetPeriodData");
        Vector vecBudgetEquiDetails = (Vector)session.getAttribute("BudgetDetailData");
        BudgetInfoBean budgetInfo = (BudgetInfoBean)session.getAttribute("budgetInfoBean");  // Case Id 2924
        // int budgetPeriod = Integer.parseInt(request.getParameter("AddBudgetPeriodRow"));
        Vector vecFilterBudgetPeriod =  filterBudgetPeriod(budgetPeriod,vecBudgetEquiDetails);
        Vector vecCEData = (Vector)session.getAttribute("costElementData");
        Vector vecFilterToShow = getExistingCE(vecFilterBudgetPeriod, vecCEData);
        // vecFilterToShow = getLineItemModifiedData(vecFilterToShow, vecFilterBudgetPeriod);
        DynaValidatorForm periodDynaForm = getFileteredBudgetPeriod(vecBudgetPeriodData,budgetPeriod);
        
        DynaActionForm dynaFormData = dynaForm.getDynaForm(request,"budgetEquipmentData");
        DynaBean newDynaForm = ((DynaBean)dynaFormData).getDynaClass().newInstance();
        // DynaBean newDynaForm = ((DynaBean)dynaForm).getDynaClass().newInstance();
        
        Timestamp dbTimestamp = prepareTimeStamp();
        newDynaForm.set("proposalNumber",periodDynaForm.get("proposalNumber"));
        newDynaForm.set("versionNumber", periodDynaForm.get("versionNumber"));
        newDynaForm.set("budgetPeriod",periodDynaForm.get("budgetPeriod"));
        newDynaForm.set("lineItemNumber",new Integer(getMaxLineItemNumberForBudget(vecFilterBudgetPeriod) + 1));
        newDynaForm.set("basedOnLineItem",new Integer(0));
        newDynaForm.set("lineItemSequence",new Integer(getMaxLineItemNumberForBudget(vecFilterBudgetPeriod) + 1));
        newDynaForm.set("lineItemStartDate",periodDynaForm.get("startDate"));
        newDynaForm.set("lineItemEndDate",periodDynaForm.get("endDate"));
        newDynaForm.set("lineItemCost",new Double(0));
        newDynaForm.set("costSharingAmount",new Double(0));
        newDynaForm.set("underRecoveryAmount",new Double(0));
        newDynaForm.set("applyInRateFlag",new Boolean(true));
        newDynaForm.set("budgetJustification",null);
        //Modified for Case #3132 - start
        //Changing quantity field from integer to float
//        newDynaForm.set("quantity",new Integer(0));
        newDynaForm.set("quantity", new Double(0));
        //Modified for Case #3132 - end
        newDynaForm.set("directCost",new Double(0));
        newDynaForm.set("indirectCost",new Double(0));
        newDynaForm.set("totalCostSharing",new Double(0));
        newDynaForm.set("startDate",periodDynaForm.get("startDate"));
        newDynaForm.set("endDate",periodDynaForm.get("endDate"));
        newDynaForm.set("totalCost",new Double(0));
        newDynaForm.set("totalDirectCost",new Double(0));
        newDynaForm.set("totalIndirectCost",new Double(0));
        newDynaForm.set("totalCostLimit",new Double(0));
        newDynaForm.set("comments",null);
        newDynaForm.set("description",EMPTY_STRING);
        //COEUSQA-1693 - Cost Sharing Submission - start
        newDynaForm.set("submitCostSharingFlag","Y");
        //COEUSQA-1693 - Cost Sharing Submission - end
        newDynaForm.set("updateTimestamp",dbTimestamp.toString());
        newDynaForm.set("acType",TypeConstants.INSERT_RECORD);
        newDynaForm.set("isRowAdded", new Boolean(true));
        if(vecBudgetEquiDetails == null){
            vecBudgetEquiDetails = new Vector();
        }
        //commented for default empty row start 1
        //      Vector vecCostElementData = (Vector)session.getAttribute("costElementData");
        //
        //       String costElementCode = EMPTY_STRING;
        //       String costElementDescription = EMPTY_STRING;
        
        //       String campusFlag = null;
        //       if(vecCostElementData != null && vecCostElementData.size() > 0){
        //           CategoryBean categoryBean = (CategoryBean)vecCostElementData.get(0);
        //           costElementCode = categoryBean.getCostElement();
        //           costElementDescription = categoryBean.getDescription();
        //           campusFlag = categoryBean.getCampusFlag();
        //       }
        //       if(campusFlag != null && !campusFlag.equals(EMPTY_STRING)){
        //           newDynaForm.set("onOffCampusFlag",new Boolean(false));
        //           if(campusFlag.equals(ON_CAMPUS_FLAG)){
        //               newDynaForm.set("onOffCampusFlag",new Boolean(true));
        //           }
        //       }
        //commented for default empty row end 1
        newDynaForm.set("costElement",EMPTY_STRING);
        newDynaForm.set("costElementDescription",EMPTY_STRING);
     //  Case Id 2924 - start
        boolean isCampusFlag = budgetInfo.isOnOffCampusFlag();
        if (isCampusFlag) {
            newDynaForm.set("onOffCampusFlag", new Boolean(true));
        }else {
            newDynaForm.set("onOffCampusFlag", new Boolean(false));
        }
     // Case Id 2924 - end

        newDynaForm.set("applyInRateFlag", new Boolean(true));
        //commented for default empty row start 2
        //       BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        //       CoeusVector vecValidRateTypes= budgetDataTxnBean.getValidCERateTypes((String)newDynaForm.get("costElement"));
        //        if(vecValidRateTypes == null){
        //            vecValidRateTypes = new CoeusVector();
        //        }
        //            //Check wheather it contains Inflation Rate
        //        Equals eqInflation = new Equals("rateClassType", RateClassTypeConstants.INFLATION);
        //        CoeusVector vecInflation = vecValidRateTypes.filter(eqInflation);
        //        if(vecInflation !=null && vecInflation.size() > 0) {
        //            newDynaForm.set("applyInRateFlag",new Boolean(true));
        //        }else {
        //            newDynaForm.set("applyInRateFlag",new Boolean(true));
        //        }
        //commented for default empty row end 2
        vecBudgetEquiDetails.addElement(newDynaForm);
        vecFilterBudgetPeriod.addElement(newDynaForm);
        vecFilterToShow = getExistingCE(vecFilterBudgetPeriod, vecCEData);
        session.setAttribute("BudgetDetailData", vecBudgetEquiDetails );
        //validation framework start
        CoeusDynaBeansList coeusLineItemDynaList = (CoeusDynaBeansList)actionForm;
        coeusLineItemDynaList.setList(vecFilterToShow);
        List beanList = new ArrayList();
        beanList.add(new Integer(budgetPeriod));
        coeusLineItemDynaList.setBeanList(beanList);
        session.setAttribute("lineItemDynaBeanList",coeusLineItemDynaList);
        //validation framework end
        request.setAttribute("FilterBudgetDetailData", vecFilterToShow);
        request.setAttribute("SelectedBudgetPeriodNumber", new Integer(budgetPeriod));
        return "success";
        
    }
    /**
     * Method to get budget information
     * @param dynaBudgetForm instance of dyna validator form
     * @throws Exception if any error occurs
     * @return Budget Information Bean for selected Proposal
     */
    protected BudgetInfoBean getBudgetForProposal(DynaActionForm dynaBudgetForm,
        HttpServletRequest request)throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmBudgetInfoData = new HashMap();
        hmBudgetInfoData.put("proposalNumber",(String)dynaBudgetForm.get("proposalNumber"));
        hmBudgetInfoData.put("versionNumber",(Integer)dynaBudgetForm.get("versionNumber"));
        Hashtable htBudgetInfoData = (Hashtable)webTxnBean.getResults(request, "getBudgetInfoData" , hmBudgetInfoData );
        Vector vecBudgetInfo = (Vector)htBudgetInfoData.get("getBudgetInfoData");
        DynaValidatorForm dynaForm = (DynaValidatorForm)vecBudgetInfo.get(0);
        BeanUtilsBean copyBudgetBean  = new BeanUtilsBean();
        BudgetInfoBean budgetInfoBean = new BudgetInfoBean();
        copyBudgetBean.copyProperties(budgetInfoBean, dynaForm);
        //Added for Case 3956 - On/Off Campus flag is not being set in Lite - start
        if(dynaForm.get("bsOnOffCampusFlag") !=null && (dynaForm.get("bsOnOffCampusFlag")).toString().equals("D")){
          budgetInfoBean.setDefaultIndicator(true);  
        }
        //Added for Case 3956 - On/Off Campus flag is not being set in Lite - End
        //Added for COEUSDEV-275 : Copy proposal: Modular Budget check/flag not saved in copied proposal = Premium - Start
        if(dynaForm.get("modularBudgetFlag") !=null && (dynaForm.get("modularBudgetFlag")).toString().equals("Y")){
          budgetInfoBean.setBudgetModularFlag(true);  
        }
        
        //COEUSDEV-275 : End
        budgetInfoBean.setUnitNumber((String)dynaBudgetForm.get("unitNumber"));
        return budgetInfoBean;
    }
    
    private void getLock(String navigator, HttpServletRequest request, HttpServletResponse response)throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user"+session.getId());
        // Added for Case COEUSQA-2546 Two users can modify a budget at same time. Locking not working -Start
        //For the page- Other Direct Cost, Equipment, Participant/Trainee and Travel getting navigator type as "success".
        // added another condition: navigator.equals("success") to the if condition
        if(navigator!=null && (navigator.equals("budgetSummary") || navigator.equals("budgetPersons")
        || navigator.equals("budgetPersonnel") || navigator.equals("budgetVersions")
        || navigator.equals("activityTypeChanged") || navigator.equals("multipleAppointments")
        || navigator.equals("syncPersons") || navigator.equals("success"))) {
            // Addedfor Case COEUSQA-2546 -End
            String proposalNumber = (String) session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
            //Added/Modified for case#2776 - Allow concurrent Prop dev access in Lite - start
            if(proposalNumber == null || proposalNumber.equals("")){
                //If proposalNumber is null, indicates that budget is invoked from a list page
                //so proposalNumber is available as a request parameter, not as a session attribute
                proposalNumber = (String)request.getParameter("proposalNumber");
            }            
            String mode =(String) session.getAttribute("mode"+session.getId());   
            if(mode != null && !mode.equals("display")){
                boolean isValid = prepareLock(userInfoBean, proposalNumber, request);
                if(!isValid){
                    session.setAttribute(CoeusLiteConstants.MODE+session.getId(),"display");
                }                
            } 
            //Added/Modified for case#2776 - Allow concurrent Prop dev access in Lite - end
        }
        //Case 2458 Bug fix ..
        if(navigator == null ){            
            String proposalNumber = (String) session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
            //Added for case#2776 - Allow concurrent Prop dev access in Lite - start
            if(proposalNumber == null || proposalNumber.equals("")){
                //If proposalNumber is null, indicates that budget is invoked from a list page
                //so proposalNumber is available as a request parameter, not as a session attribute
                proposalNumber = (String)request.getParameter("proposalNumber");
            }     
            //Added for case#2776 - Allow concurrent Prop dev access in Lite - end
            boolean isValid = prepareLock(userInfoBean, proposalNumber, request);
            if(!isValid){
                session.setAttribute(CoeusLiteConstants.MODE+session.getId(),"display");
            } 
        }
    } 
    
    /**
     * This method checks whether any other user is having lock for the budget
     * @param request HttpServletRequest
     * @return islockOwnedByOther boolean, indicating whether lock is acquired by another user or not
     * @throws Exception
     */
    private boolean isBudgetLockedByOther(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        boolean islockOwnedByOther = false; 
        String listPage = request.getParameter("listPage");                                    
        String proposalNumber = (String) session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());        
        if(proposalNumber == null || proposalNumber.equals("")){
            proposalNumber = (String)request.getParameter("proposalNumber");
        }            
        String lockId = CoeusLiteConstants.BUDGET_LOCK_STR +proposalNumber;        
        LockBean serverLockedBean = getLockedData(lockId, request);
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String loggedinUser = userInfoBean.getUserId();        
        if(serverLockedBean != null){
            serverLockedBean.setModuleKey(CoeusLiteConstants.BUDGET_MODULE);
            serverLockedBean.setModuleNumber(proposalNumber);            
            String lockUserId = serverLockedBean.getUserId();
//            UserMaintDataTxnBean userTxnBean = new UserMaintDataTxnBean();
//            String lockUserName = userTxnBean.getUserName(lockUserId);    
            String lockUserName = EMPTY_STRING;
            String budgetLock = "";
            if("Y".equals(listPage)){
                budgetLock = "budget_lock";
            }else{
                budgetLock = "acquired_lock";
            }
            // I the logged in user and the locked user is same, the acquired lock message is not displaying so commented
            // while doing case # COEUQA -1697
           if(!loggedinUser.equalsIgnoreCase(lockUserId)){
                ActionMessages messages = new ActionMessages();                               
                String hasModifyValue = (String)session.getAttribute("hasRights");                    
                String msg = "";
                //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-start               
//                CoeusFunctions coeusFunctions = new CoeusFunctions();                 
//                String displayLockName = coeusFunctions.getParameterValue(CoeusConstants.DISPLAY_LOCKNAME_PROP_BUDGET);
//                if("Y".equalsIgnoreCase(displayLockName.trim()) || lockUserId.equalsIgnoreCase(loggedinUser)){
//                    lockUserName=lockUserName;
//                }else{
//                    lockUserName = CoeusConstants.lockedUsername;
//                }                
                lockUserName =  viewRestrictionOfUser(loggedinUser,lockUserId);
                //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-end
                if("Y".equals(listPage)){
                    if(hasModifyValue.equals("true")){
                        msg = ". Do you want to open it in display mode ?";
                        messages.add("budgetLock", new ActionMessage(budgetLock, lockUserName, serverLockedBean.getModuleKey(), serverLockedBean.getModuleNumber(), msg));            
                        saveMessages(request, messages);
                        islockOwnedByOther = true;
                    }
                }else{                   
                    messages.add("acqLock", new ActionMessage(budgetLock, lockUserName, serverLockedBean.getModuleKey(), serverLockedBean.getModuleNumber()));            
                    saveMessages(request, messages);
                    islockOwnedByOther = true;
                }                
                session.removeAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());                 
            }
        }   
        return islockOwnedByOther;
    }   
    
    /**
     * This method checks whether a budget can be created for a given proposal
     * @param request HttpServletRequest
     * @return canCreateBudget boolean, indicating whether budget can be created or not
     * @throws Exception
     */
    private boolean canCreateBudget(HttpServletRequest request) throws Exception{
        boolean canCreateBudget = false;
        String propStatusCode = "";
        String propStatusDesc = "";
        HashMap hmPropStatus = getProposalStatus(request);
        propStatusCode = (String)hmPropStatus.get("propStatusCode");
        propStatusDesc = (String)hmPropStatus.get("propStatusDesc");
        //COEUSQA-1433 Allow Recall for Routing - Start
        //if(propStatusCode.equals("1") || propStatusCode.equals("3")){
        if(propStatusCode.equals("1") || propStatusCode.equals("3") || propStatusCode.equals("8")){
        //COEUSQA-1433 Allow Recall for Routing - End
            canCreateBudget = true;
        }else{
            ActionMessages messages = new ActionMessages();
            messages.add("cannotCreateBudget", new ActionMessage("error.budgetCreate", propStatusDesc));   
            saveMessages(request, messages);
        }        
        return canCreateBudget;
    }
    
    
    /**
     * This method checks whether budget exists for a given proposal
     * @param request HttpServletRequest
     * @return budgetExists boolean, indicating whether budget exits or not
     * @throws Exception
     */
    private boolean isBudgetExists(HttpServletRequest request) throws Exception{
        boolean budgetExists = true;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        String proposalNumber = (String)session.getAttribute("proposalNumber"+session.getId());
        Map hmInput = new HashMap();
        hmInput.put("proposalNumber", proposalNumber);        
        Hashtable htOutput = (Hashtable)webTxnBean.getResults(request, "isValidPropDevNoNBudget", hmInput);
        hmInput = (HashMap)htOutput.get("isValidPropDevNoNBudget");
        int valid = Integer.parseInt((String)hmInput.get("isValid"));        
        if(valid == 200){
            budgetExists = false;
        }
        return budgetExists;
    }
    
    /**
     * This method sets the budget mode based on proposal status
     * @param request HttpServletRequest
     * @throws Exception
     */
    private boolean setBudgetInDisplay(HttpServletRequest request) throws Exception{
        String propStatusCode = "";  
        boolean budgetInDisplay = false;
        ProposalBudgetHeaderBean headerBean = (ProposalBudgetHeaderBean)request.getSession().getAttribute("ProposalBudgetHeaderBean");
        String proposalNumber = headerBean.getProposalNumber();
        HashMap hmPropStatus = null;
        if(proposalNumber == null || proposalNumber.trim().length() == 0) {
            hmPropStatus = getProposalStatus(request);
        }else {
            hmPropStatus = getProposalStatus(proposalNumber, request);
        }
        propStatusCode = (String)hmPropStatus.get("propStatusCode");
        //COEUSQA-1433 Allow Recall for Routing - Start
        if(!propStatusCode.equals("1") && !propStatusCode.equals("3") && !propStatusCode.equals("8")){
        //if(!propStatusCode.equals("1") && !propStatusCode.equals("3")){
        //COEUSQA-1433 Allow Recall for Routing - End
            HttpSession session = request.getSession();
            session.removeAttribute("mode"+session.getId());
            session.setAttribute("mode"+session.getId(), "display");
            budgetInDisplay = true;
        }
        return budgetInDisplay;
    }
    
    /**
     * This method checks whether the logged in user is the PI for a given proposal
     * @param request HttpServletRequest
     * @return isLoggedInUserPI boolean, indicating whether logged in user is the PI
     * @throws Exception
     */
    private boolean isUserPI(HttpServletRequest request) throws Exception{
        boolean isLoggedInUserPI = false;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        String proposalNumber = (String)session.getAttribute("proposalNumber"+session.getId());
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String loggedinUser = userInfoBean.getUserId();         
        HashMap hmInput = new HashMap();
        hmInput.put("moduleItemKey", proposalNumber);
        hmInput.put("userId", loggedinUser);
        hmInput.put("moduleCode", "3");
        Hashtable htOutput = (Hashtable)webTxnBean.getResults(request, "isUserPI", hmInput);
        hmInput = (HashMap)htOutput.get("isUserPI");
        String count = (String)hmInput.get("ll_count");
        if(count.equals("1")){
            isLoggedInUserPI = true;
        }
        return isLoggedInUserPI;                
    }
    
    /**
     * This method checks whether the user has the create budget right
     * @param request HttpServletRequest
     * @return hasRight boolean, indicating whether user has right
     * @throws Exception
     */
    private boolean hasCreateBudgetRight(HttpServletRequest request) throws Exception{
        boolean hasRight = false;
        HttpSession session = request.getSession();
        String proposalNumber = (String)session.getAttribute("proposalNumber"+session.getId());
        Vector vecProposalDetails = getProposalDetailsForBudget(request ,proposalNumber);  
        DynaActionForm dynaProposalDetailForm = null;
        if(vecProposalDetails !=null && vecProposalDetails.size() > 0){
            dynaProposalDetailForm = (DynaActionForm)vecProposalDetails.elementAt(0);   
            Vector vecBudgetRights = checkBudgetRights(request ,dynaProposalDetailForm);
            hasRight = ((Boolean)vecBudgetRights.elementAt(0)).booleanValue();
        }
        return hasRight;
    }    

    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
    /**
     * Method to set each line item is formulated line item or not
     * @param vecFilterBudgetDetail 
     * @param session 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @throws edu.mit.coeus.exception.CoeusException 
     */
    private void setIsFormulatedCostLineItem(Vector vecFilterBudgetDetail, HttpSession session) throws DBException, CoeusException {
        if(vecFilterBudgetDetail != null && !vecFilterBudgetDetail.isEmpty()){
            BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
            BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
            CoeusVector cvFormulatedDetails = budgetDataTxnBean.getBudgetFormulatedDetail(budgetInfoBean.getProposalNumber(),budgetInfoBean.getVersionNumber());
            if(cvFormulatedDetails != null && !cvFormulatedDetails.isEmpty()){
                for(Object lineItemForm : vecFilterBudgetDetail){
                    DynaActionForm lineItemDynaForm = (DynaActionForm)lineItemForm;
                    int subAwardNumber = 0;
                    if(lineItemDynaForm.get("subAwardNumber") != null) {
                       subAwardNumber = ((Integer)lineItemDynaForm.get("subAwardNumber")).intValue();
                    }
                    if(subAwardNumber == 0){
                        int budgetPeriod = ((Integer)lineItemDynaForm.get("budgetPeriod")).intValue();
                        int lineItemNumber = ((Integer)lineItemDynaForm.get("lineItemNumber")).intValue();
                        Equals eqBudgetPeriod = new Equals("budgetPeriod", budgetPeriod);
                        Equals eqLineItemNum = new Equals("lineItemNumber",lineItemNumber);
                        And andLineItem = new And(eqBudgetPeriod,eqLineItemNum);
                        CoeusVector cvLineItemFormDetails = cvFormulatedDetails.filter(andLineItem);
                        if(cvLineItemFormDetails != null && !cvLineItemFormDetails.isEmpty()){
                            lineItemDynaForm.set("isFormulatedLineItem", new Boolean(true));
                        }else{
                            lineItemDynaForm.set("isFormulatedLineItem", new Boolean(false));
                        }
                    }
                }
            }
        }
    }
    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
}
