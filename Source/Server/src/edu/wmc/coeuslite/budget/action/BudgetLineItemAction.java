/*
 * BudgetLineItemAction.java
 *
 * Created on March 27, 2006, 1:36 PM
 *
 * PMD check performed, and commented unused imports and variables on 15-FEB-2011
 * by Maharaja Palanichamy
 *
 */

package edu.wmc.coeuslite.budget.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
//import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.budget.bean.BudgetDetailBean;
import edu.mit.coeus.budget.bean.BudgetDetailCalAmountsBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetPeriodBean;
import edu.mit.coeus.budget.bean.BudgetPersonnelDetailsRateBaseBean;
import edu.mit.coeus.budget.bean.BudgetRateBaseBean;
import edu.mit.coeus.budget.bean.CostElementsBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
//import edu.mit.coeus.utils.RateClassTypeConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
//import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeuslite.utils.CoeusDynaBeansList;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.LockBean;
import edu.wmc.coeuslite.budget.bean.CategoryBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorActionForm;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author  chandrashekara
 */
public class BudgetLineItemAction extends BudgetBaseAction{
//    ActionForward actionForward = null;
//    private HttpServletRequest request = null;;
//    private HttpSession session = null;
//    private ActionMapping actionMapping;
    private static final String EMPTY_STRING = "";
    private static final String ADD_LINEITEM = "/AddLineItem";
    private static final String REMOVE_LINEITEM = "/RemoveLineItem";
    private static final String LINE_ITEM_DETAILS = "/getLineItemDetails";
    private static final String CALCULATE = "/calculateBudget";
    private static final String GET_FIELDNAME = "dynaFormData[";
    private static final String COST_ELEMENT_AS_REQUEST = "].costElement";
    private static final String LINE_DESC_AS_REQUEST = "].lineItemDescription";
    private static final String LINE_COST_AS_REQUEST = "].tempLineItemCost";    
//    private WebTxnBean webTxnBean;
//    private Timestamp dbTimestamp;
//    private HttpServletResponse response;
//    private ActionMessages actionMessages;
//    private ActionForm actionForm; -- Commented for removing instance variable - case # 2960 
//    private CoeusDynaBeansList coeusDynaBeansList;
    /** Creates a new instance of BudgetLineItemAction */
    public BudgetLineItemAction() {
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
    public ActionForward performExecute(ActionMapping actionMapping,
        ActionForm actionForm, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
//            this.actionForm = actionForm;  -- Commented for removing instance variable - case # 2960
            ActionForward actionForward = null;
            if(actionMapping.getPath().equals(LINE_ITEM_DETAILS)){
                // Modified for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
//                 DynaValidatorForm dynaForm = (DynaValidatorForm)actionForm;
//                 String navigator = getBudgetLineItemDetails(dynaForm, request);
//                 actionForward = actionMapping.findForward(navigator);
//                 return actionForward;
                int budgetPeriod = Integer.parseInt(request.getParameter("budgetPeriod").toString());
                int lineItemNumber = Integer.parseInt(request.getParameter("lineItemNumber").toString());
                int subAwardNumber = 0;
                if(request.getParameter("subAwardNumber") != null){
                    subAwardNumber = Integer.parseInt(request.getParameter("subAwardNumber").toString());
                }
                int budgetCategoryCode = 0;
                if(request.getParameter("budgetCategoryCode") != null){
                    budgetCategoryCode = Integer.parseInt(request.getParameter("budgetCategoryCode"));
                }
                boolean hasFormulatedCost = isLineItemFormulated(request,budgetPeriod, lineItemNumber,subAwardNumber);
                if(hasFormulatedCost){
                    String redirectUrl = "/getFormulatedLineItemDetails.do?action=noView&budgetPeriod=";
                    redirectUrl += budgetPeriod;
                    redirectUrl += "&lineItemNumber="+lineItemNumber;
                    RequestDispatcher rd = request.getRequestDispatcher(redirectUrl);
                    rd.forward(request,response);
                }else{
                    DynaValidatorForm dynaForm = (DynaValidatorForm)actionForm;
                    String navigator = getBudgetLineItemDetails(dynaForm, request);
                    actionForward = actionMapping.findForward(navigator);
                    return actionForward;
                }
                // Modified for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
            }
            CoeusDynaBeansList coeusDynaBeansList = (CoeusDynaBeansList)actionForm;

        //    DynaValidatorForm dynaForm = (DynaValidatorForm)actionForm;
            // added parameter  to this method after removing instance variable - case # 2960
            actionForward = performBudgetDetailsAction(actionMapping,
                                        coeusDynaBeansList, request, response,actionForm);
            return actionForward;
    }
     /**
      * This method will identify which request is comes from which path and
      * navigates to the respective ActionForward
      * @returns ActionForward object
      * @param actionMapping
      * @param dynaForm
      * @throws Exception
      * @return
      */
    // added parameter actionForm to this method after removing instance variable - case # 2960
    private ActionForward performBudgetDetailsAction(ActionMapping actionMapping,
        CoeusDynaBeansList dynaForm, HttpServletRequest request, 
        HttpServletResponse response,ActionForm actionForm)throws Exception{
        String navigator = EMPTY_STRING;
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        if(actionMapping.getPath().equals(ADD_LINEITEM)){
            navigator = addLineItemDetails(dynaForm, request,actionForm);
        }else if(actionMapping.getPath().equals(REMOVE_LINEITEM)){
           navigator = removeLineItemDetails(request,actionForm);
        }
//        else if(actionMapping.getPath().equals(LINE_ITEM_DETAILS)){
//               navigator = getBudgetLineItemDetails(dynaForm) ;
//        }
        else if(actionMapping.getPath().equals(CALCULATE)){
            // Check if lock exists or not
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId()), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.BUDGET_LOCK_STR+lockBean.getModuleNumber(), request);
            if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {            
                navigator=  calculatePeriodLineItem(dynaForm, request, response,actionForm);
            } else {
                String errMsg = "release_lock_for";
                ActionMessages messages = new ActionMessages();                
                messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                saveMessages(request, messages);
                navigator = "success";
            }
        }
        request.setAttribute("dataModified", "modified");        
        ActionForward actionForward = actionMapping.findForward(navigator);
        return actionForward;
    }
    /**
     * This is to calculate the Budget Details when calculate is invoked
     * @param dynaForm
     * @throws Exception
     * @return
     */
    // added parameter actionForm to this method after removing instance variable - case # 2960
    private String calculatePeriodLineItem(CoeusDynaBeansList dynaForm,
        HttpServletRequest request, HttpServletResponse response,ActionForm actionForm) throws Exception{
        HttpSession session = request.getSession();
        int budgetPeriod = Integer.parseInt(request.getParameter("budgetPeriod"));          
        int reqToOpenBudgetPeriod = 0;
        if(request.getParameter("requestBudgetPeriod") != null){
            reqToOpenBudgetPeriod = Integer.parseInt(request.getParameter("requestBudgetPeriod"));
        }
        
        /*If user click on save or Calculate button. Check data is modified or not and get the updated data
         *(Even though If data not modified user will get coeusvector of BudgetPeriodBean). Now get Vector 
         *of Dynavalidator of budgetPeriod from updated data(CoeusVector) and set in request(If user click on 
         *calculate and data is modified). If data is not modified then we are not going to use this coeusvector
         * start 1*/
        HashMap hmDetailsData = getBudgetDetailsForPeriod(budgetPeriod, request);
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
        boolean isDataInActive = ((Boolean)hmDetailsData.get("inActiveData")).booleanValue();
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
        boolean isDataChanged = ((Boolean)hmDetailsData.get("dataChanged")).booleanValue();
        boolean isLineItemAdded = ((Boolean)hmDetailsData.get("RowAdded")).booleanValue();
        boolean isLineItemDeleted = ((Boolean)hmDetailsData.get("RowDeleted")).booleanValue();
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
        //if(!isDataChanged && !isLineItemAdded && !isLineItemDeleted ){
        if(isDataInActive || (!isDataChanged && !isLineItemAdded && !isLineItemDeleted )){
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
            request.setAttribute("dataNotModified", new Boolean(true));
            String page = (String)session.getAttribute("pageConstantValue");
             String url =  "/getBudgetEquipment.do?Period=";
            if(reqToOpenBudgetPeriod > 0){
                url += reqToOpenBudgetPeriod +"&PAGE="+page;
            }else{
                url += budgetPeriod +"&PAGE="+page;
            }
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
            requestDispatcher.forward(request,response);
            return null;
        }
        Vector vecBudgetPeriod = null;
        //So data modified. calculate the budget or save(Based on parameter).
        String navigator = "success";
        BudgetInfoBean budgetInfoBean =  (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        Vector vecBudgetDetailsData = (Vector)session.getAttribute("BudgetDetailData");
        //get the calculated amount. This will be used in calculation
        //Added for removing instance variable -case # 2960 - start
        String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        //Added for removing instance variable - case # 2960- end                
        HashMap hmData = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        hmData.put("proposalNumber",budgetInfoBean.getProposalNumber());
        hmData.put("versionNumber",new Integer(budgetInfoBean.getVersionNumber()));
        Hashtable htBudgetDetails = (Hashtable)webTxnBean.getResults(request,"getBudgetDetailCalAmts",hmData);
        Vector vecBudgetDetailCalAmts = (Vector)htBudgetDetails.get("getBudgetDetailCalAmts");
        vecBudgetDetailCalAmts = getCalculatedCalAmts(vecBudgetDetailsData,budgetPeriod,
                                            dynaForm,vecBudgetDetailCalAmts,request);
        CoeusVector cvBudgetDetailCalamts = prepareBudgetDetailCalAmts(vecBudgetDetailCalAmts);
        
        //start 2 This is vector of all Budget Details including actype EMPTY. This will be used in calculation
        CoeusVector cvFilterBudgetDetails = (CoeusVector)hmDetailsData.get("BudgetDetails");
        
        NotEquals eqAcType = new NotEquals("acType",EMPTY_STRING);
        
        Hashtable htBudgetData = initializeBudgetCalculator(budgetInfoBean);
        htBudgetData.remove(BudgetDetailBean.class);
        htBudgetData.remove(BudgetDetailCalAmountsBean.class);
        htBudgetData.put(BudgetDetailBean.class, cvFilterBudgetDetails);
        htBudgetData.put(BudgetDetailCalAmountsBean.class, cvBudgetDetailCalamts);
        //setting the Period total cost , direct cost  and indirect cost.  Bug fixed start
        vecBudgetPeriod = (Vector)session.getAttribute("BudgetPeriodData");
        double totalDirectCost, totalIndirectCost, costSharingAmount, underrecovery;
        totalDirectCost
                = calculatePeriodCost(cvBudgetDetailCalamts,cvFilterBudgetDetails,
                                            budgetPeriod,TOTAL_DIRECT_COST);
        totalIndirectCost = calculatePeriodCost(cvBudgetDetailCalamts,cvFilterBudgetDetails,
                                            budgetPeriod,TOTAL_INDIRECT_COST);
        costSharingAmount = calculatePeriodCost(cvBudgetDetailCalamts,cvFilterBudgetDetails,
                                            budgetPeriod,COST_SHARING_AMOUNT);
        underrecovery = calculatePeriodCost(cvBudgetDetailCalamts,cvFilterBudgetDetails,
                                            budgetPeriod,UNDERRECOVERY);
        double totCost = totalDirectCost+totalIndirectCost;
        if(vecBudgetPeriod != null && vecBudgetPeriod.size() > 0){
            for(int index = 0; index < vecBudgetPeriod.size(); index++){
                DynaValidatorForm dynaPeriod = (DynaValidatorForm)vecBudgetPeriod.get(index);
                int dynaPeriodnumber = ((Integer)dynaPeriod.get("budgetPeriod")).intValue();
                if(dynaPeriodnumber == budgetPeriod){
                    dynaPeriod.set("totalDirectCost",new Double(totalDirectCost));
                    dynaPeriod.set("totalIndirectCost",new Double(totalIndirectCost));
                    dynaPeriod.set("totalCost", new Double(totCost));
                    dynaPeriod.set("costSharingAmount", new Double(costSharingAmount));
                    dynaPeriod.set("underRecoveryAmount", new Double(underrecovery));
                    dynaPeriod.set("acType", TypeConstants.UPDATE_RECORD);
                }
            }
        }
        CoeusVector cvBudgetPeriods = null;
        if(vecBudgetPeriod != null){
            BudgetPeriodBean budgetPeriodBean = null;
            BeanUtilsBean copyBean = null;
            cvBudgetPeriods = new CoeusVector();
            for(int ind = 0; ind < vecBudgetPeriod.size() ; ind++){
                DynaValidatorForm dynaPeriod = (DynaValidatorForm)vecBudgetPeriod.get(ind);
                int dynaPeriodNumber = ((Integer)dynaPeriod.get("budgetPeriod")).intValue();
                budgetPeriodBean = new BudgetPeriodBean();
                copyBean = new BeanUtilsBean();
                copyBean.copyProperties(budgetPeriodBean,dynaPeriod);
                budgetPeriodBean.setAw_BudgetPeriod(dynaPeriodNumber);
                cvBudgetPeriods.addElement(budgetPeriodBean);
            }
        }
        if(cvBudgetPeriods != null && cvBudgetPeriods.size() > 0){
            htBudgetData.remove(BudgetPeriodBean.class);
            htBudgetData.put(BudgetPeriodBean.class, cvBudgetPeriods);
        }
        //Bug fixed end
        Hashtable htCalculatedData = calculatedBudgetPeriod(htBudgetData,budgetPeriod,budgetInfoBean);
        cvBudgetDetailCalamts = (CoeusVector)htCalculatedData.get(BudgetDetailCalAmountsBean.class);
        CoeusVector cvBudgetPeriod = (CoeusVector)htCalculatedData.get(BudgetPeriodBean.class);
        //start 3 : Here is this coeusvector of BudgetDetailsBean. Take only modified data.
        CoeusVector cvBudgetDetails  = cvFilterBudgetDetails.filter(eqAcType);
        //start 4: Here Getting Dyna vector of budget details which is modified or inserted or deleted.
        //Note: If data is inserted or deleted then update all Line Item for line Item sequence. other wise only modified 
        //data will go for updation.
        HashMap hmBudgetData = prepareBudgetDetailsData(cvBudgetDetails,cvBudgetPeriod,
                                        cvBudgetDetailCalamts,dynaForm,request);
        vecBudgetPeriod = (Vector)hmBudgetData.get(BudgetPeriodBean.class);
        Vector vecBudgetDetails = (Vector)hmBudgetData.get(BudgetDetailBean.class);
        
        //Start 5: Preparing Modified data for Session(Budget Details Data for All Periods)
        Vector vecBudgetEntireDetails = prepareBudgetEntireDetailsData(vecBudgetDetails, request);
        checkTotalCostLimit(request, htCalculatedData);
        //Saving data here otherwise only calculate the data
        if(request.getParameter("Save") != null){
             
                //Check the total Cost. If amount exceeding above $9,999,999,999.99 then send error message
                boolean isExceeding = checkTotalCost(htCalculatedData);
                if(isExceeding){
                    //Modified for removing instance variable -case # 2960 - start
//                    setDataForExceedTotalCost(budgetPeriod,vecBudgetPeriod,vecBudgetEntireDetails,request,actionForm);
                    setDataForExceedTotalCost(budgetPeriod,vecBudgetPeriod,vecBudgetEntireDetails,
                        request,actionForm,queryKey);
                    //Modified for removing instance variable - case # 2960- end                    
                    ActionMessages actionMessages = new ActionMessages();
                    actionMessages.add("budget_common_exceptionCode.1011" , new ActionMessage(
                    "budget_common_exceptionCode.1011" ) );
                    saveMessages(request, actionMessages);
                    return navigator;
                }
                //Modified code web txn bean for on off campus flag start
                CoeusVector cvBudgetInfoData = (CoeusVector)htCalculatedData.get(BudgetInfoBean.class);
                CoeusVector cvBudgetRateBase = (CoeusVector)htCalculatedData.get(BudgetRateBaseBean.class);
                //take only budget info, budget period, budget detail, budget cal amts and rates
                Map hmBudgetDataToSave = new HashMap();
                hmBudgetDataToSave.put(BudgetPeriodBean.class, (Vector)hmBudgetData.get(BudgetPeriodBean.class));
                hmBudgetDataToSave.put(BudgetDetailBean.class, (Vector)hmBudgetData.get(BudgetDetailBean.class));
                hmBudgetDataToSave.put(BudgetDetailCalAmountsBean.class, (Vector)hmBudgetData.get(BudgetDetailCalAmountsBean.class));
                hmBudgetDataToSave.put(BudgetInfoBean.class,cvBudgetInfoData);// This contains the CoeusVector of Budget Info
                              
                saveBudgetPeriodLineItems(hmBudgetDataToSave,budgetPeriod,reqToOpenBudgetPeriod,request,response);
                //Modified code web txn bean for on off campus flag end
                //commented code for web txn bean for on off campus flag start
                /*bug fixed for On off campus flag and for calculation. If flag true set N else F. Note:in webtxnbean if flag is true
                it set to Y and for false it sets N. end 1*/
               // saveBudgetPeriodLineItems(htCalculatedData,budgetPeriod, reqToOpenBudgetPeriod);
                //commented code for web txn bean for on off campus flag end
                session.setAttribute("dataChanges", new Boolean(false));
                return null;
        }
        //start 5 : setting to session and request here(If click on calculate button)
        vecBudgetDetails = filterBudgetPeriod(budgetPeriod, vecBudgetEntireDetails);
        vecBudgetDetails = getExistingCE(vecBudgetDetails, (Vector)session.getAttribute("costElementData"));
        Vector vecAllPeriods = getPeriodForShowTab(vecBudgetPeriod);
        boolean dataExist = false;
        if(vecAllPeriods != null && vecAllPeriods.size() > 0){
            for(int index = 0; index < vecAllPeriods.size(); index++){
                int budPeriod = ((Integer)vecAllPeriods.get(index)).intValue();
                Vector vecFilterTabBudgetPeriod =  filterBudgetPeriod(budPeriod,vecBudgetEntireDetails);
                if(vecFilterTabBudgetPeriod.size() > 0){
                    dataExist = true;
                    break;
                }
            }
        }
        if(!dataExist){
            vecBudgetPeriod = filterBudgetPeriod(1, vecBudgetPeriod);
            budgetPeriod = MAKE_DEFAULT_PERIOD;
            vecBudgetDetails = filterBudgetPeriod(budgetPeriod, vecBudgetEntireDetails);
            vecBudgetDetails = getExistingCE(vecBudgetDetails, (Vector)session.getAttribute("costElementData"));
        }
        if(vecBudgetDetails != null && vecBudgetDetails.size() > 0){
            session.setAttribute("dataChanges", new Boolean(true));
        }
        request.setAttribute("FilterBudgetDetailData",vecBudgetDetails);
        request.setAttribute("SelectedBudgetPeriodNumber", new Integer(budgetPeriod));
        session.setAttribute("BudgetPeriodData",vecBudgetPeriod);
        session.setAttribute("BudgetDetailData", vecBudgetEntireDetails);
        //validation framework start
        //CoeusDynaBeansList coeusLineItemDynaList = (CoeusDynaBeansList)dynaForm;
        dynaForm.setList(vecBudgetDetails);
        List beanList = new ArrayList();
        beanList.add(new Integer(budgetPeriod));
        dynaForm.setBeanList(beanList);
        session.setAttribute("lineItemDynaBeanList",dynaForm);
        //validation framework end
        removeQueryEngineCollection(queryKey);
        return navigator;
    }
    /**
      * Get all the Caclculated Cost Amounts for the period and prepare the data to
      * save the budget
      * @param vecBudgetDetail
      * @param budgetPeriod
      * @param dynaForm
      * @throws Exception
      * @return
      */
    private Vector getCalculatedCalAmts(Vector vecBudgetDetail, int budgetPeriod, 
        CoeusDynaBeansList dynaForm,Vector vecBudgetCalAmts,
        HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        if(vecBudgetCalAmts == null){
            vecBudgetCalAmts = new Vector();
        }
        Vector vecFilterBudgetDetails = filterBudgetPeriod(budgetPeriod, vecBudgetDetail);
        Vector vecCEData = (Vector)session.getAttribute("costElementData");
        Vector vecFilterToShow = getExistingCE(vecFilterBudgetDetails, vecCEData);
        if(vecFilterToShow!= null && vecFilterToShow.size() > 0){
            String acType = null;
            String dynaCE = null;
            String reqCE = null;
            for(int index = 0; index < vecFilterToShow.size(); index++){
                DynaValidatorForm dynaBudgetDetails = (DynaValidatorForm)vecFilterToShow.get(index);
                reqCE = request.getParameter(GET_FIELDNAME+index+LINE_COST_AS_REQUEST);
                if(reqCE != null && !reqCE.equals(EMPTY_STRING)){
                    double dynaLineItemCost = ((Double)dynaBudgetDetails.get("lineItemCost")).doubleValue();
                    dynaBudgetDetails.set("directCost", new Double(dynaLineItemCost));
                    double reqLineItemCost = 0;
                    try{
                        reqLineItemCost = formatStringToDouble(reqCE);
                    }catch(NumberFormatException ne){
                        //set line Cost 0 if not in number format
                        reqLineItemCost = 0;
                    }
                    if(dynaLineItemCost != reqLineItemCost){
                        dynaBudgetDetails.set("lineItemCost", new Double(reqLineItemCost));
                        
                    }
                }
                reqCE = request.getParameter(GET_FIELDNAME+index+COST_ELEMENT_AS_REQUEST);
                if(reqCE != null && !reqCE.equals(EMPTY_STRING)){
                    dynaCE = (String)dynaBudgetDetails.get("costElement");
                    acType = (String)dynaBudgetDetails.get("acType");
                    boolean changeCEFlag = false;
                    if(dynaBudgetDetails.get("changeCostElement") != null){
                        changeCEFlag = ((Boolean)dynaBudgetDetails.get("changeCostElement")).booleanValue();
                    }
                    int budgetPeriodNumber = 0;
                    int lineItemNumber = 0;
                    Vector vecNewCalAmts = null;
                    //modified for Indexed Properties validation start
                    if((dynaCE !=null && !dynaCE.equals(reqCE)) || changeCEFlag 
                        || acType.equals(TypeConstants.INSERT_RECORD) || acType.equals(TypeConstants.DELETE_RECORD)){
                    //modified for Indexed Properties validation end       
                        BudgetInfoBean budgetInfoBean =(BudgetInfoBean)session.getAttribute("budgetInfoBean");
                        
                        budgetPeriodNumber = ((Integer)dynaBudgetDetails.get("budgetPeriod")).intValue();
                        lineItemNumber = ((Integer)dynaBudgetDetails.get("lineItemNumber")).intValue();
                        if(acType!=null && acType.equals(TypeConstants.INSERT_RECORD)){
                            vecNewCalAmts = getCalculatedAmtsData(budgetInfoBean, budgetPeriodNumber,lineItemNumber, reqCE, dynaForm,request,vecBudgetCalAmts);
                            if(vecNewCalAmts!= null && vecNewCalAmts.size() > 0){
                               vecBudgetCalAmts.addAll( vecNewCalAmts );
                            }
                        }else{
                            vecBudgetCalAmts = removeOldCalAmtsData(vecBudgetCalAmts, budgetPeriod, lineItemNumber, request);
                            vecNewCalAmts = getCalculatedAmtsData(budgetInfoBean, budgetPeriodNumber,lineItemNumber, reqCE, dynaForm,request,vecBudgetCalAmts);
                            if(vecNewCalAmts!= null && vecNewCalAmts.size() > 0){
                                vecBudgetCalAmts.addAll( vecNewCalAmts );
                            }
                        }
                        dynaBudgetDetails.set("costElement",reqCE);
                      // Case Id 2924 - start
                       // Added for Case 3956 - On/Off Campus flag is not being set in Lite -Start
                        boolean isCampusFlag = false;
                        if(budgetInfoBean.isDefaultIndicator()){//
                            isCampusFlag = getCampusFlag(request, reqCE);
                        }else{
                            isCampusFlag = budgetInfoBean.isOnOffCampusFlag();
                        }
                        // Added for Case 3956 - On/Off Campus flag is not being set in Lite
                      // Case Id 2924 - end  
                        dynaBudgetDetails.set("onOffCampusFlag",new Boolean(isCampusFlag));
                        
                    }
                    
                }
                
            }
        }
        vecBudgetCalAmts = getDeletedDetailData(vecBudgetCalAmts, request);
        return vecBudgetCalAmts;
    }
    /**
     * Get the DetailCalAmts data prepare the ACTypes for the data updation
     * @param vecBudgetCalAmts
     * @throws Exception
     * @return
     */
    private Vector getDeletedDetailData(Vector vecBudgetCalAmts, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        Vector vecDeletedData = (Vector)session.getAttribute("DeletedBudgetDetailsData");
        if(vecBudgetCalAmts != null){
            if(vecDeletedData != null && vecDeletedData.size() > 0){
                for(int index = 0; index < vecDeletedData.size(); index++ ){
                    DynaValidatorForm dynaForm = (DynaValidatorForm)vecDeletedData.get(index);
                    int budgetPeriod = ((Integer)dynaForm.get("budgetPeriod")).intValue();
                    int lineItemnumber = ((Integer)dynaForm.get("lineItemNumber")).intValue();
                    for(int ind = 0; ind < vecBudgetCalAmts.size(); ind++){
                        DynaValidatorForm dynaValidForm = (DynaValidatorForm)vecBudgetCalAmts.get(ind);
                        int dynaBudgetPeriod = ((Integer)dynaValidForm.get("budgetPeriod")).intValue();
                        int dynaLineItemnumber = ((Integer)dynaValidForm.get("lineItemNumber")).intValue();
                        if(budgetPeriod == dynaBudgetPeriod && lineItemnumber == dynaLineItemnumber){
                            String acType = (String)dynaValidForm.get("acType");
                            if(acType != null ){
                                dynaValidForm.set("acType",TypeConstants.DELETE_RECORD);
                            }
                        }
                    }
                }
            }
        }
        return vecBudgetCalAmts;
    }
    /**
     * Prepares only BudgetDetailCalAmtsBean for the DyanForm
     * @param vecBudgetDetailCalAmts
     * @throws Exception
     * @return
     */
    private CoeusVector prepareBudgetDetailCalAmts(Vector vecBudgetDetailCalAmts) throws Exception{
        BeanUtilsBean copyBean = null;
        BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = null;
        CoeusVector cvCalamts = null;
        if(vecBudgetDetailCalAmts!= null){
            cvCalamts = new CoeusVector();
            for(int index = 0; index <vecBudgetDetailCalAmts.size(); index++){
                budgetDetailCalAmountsBean  = new BudgetDetailCalAmountsBean ();
                DynaValidatorForm dynaForm = (DynaValidatorForm)vecBudgetDetailCalAmts.get(index);
                copyBean = new BeanUtilsBean();
                copyBean.copyProperties(budgetDetailCalAmountsBean ,dynaForm);
                cvCalamts.addElement(budgetDetailCalAmountsBean);
            }
        }
        return cvCalamts;
    }
    /** Uncomment after Modifying Web Txn Bean for On Off Campus Flag
     * Saves the Budget Period Line Item details for the specific period
     * @param hmBudgetData
     * @param budgetPeriod
     * @param reqToOpenBudgetPeriod
     * @param editing
     * @throws Exception
     */
    private void saveBudgetPeriodLineItems(Map hmBudgetData,int budgetPeriod, 
                    int reqToOpenBudgetPeriod, HttpServletRequest request,
                     HttpServletResponse response) throws Exception{
        WebTxnBean webTxnBean = null;
        HttpSession session = request.getSession();
        String proposalNumber = (String) session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
        String page = (String)session.getAttribute("pageConstantValue");
        CoeusVector cvBudgetInfo = (CoeusVector)hmBudgetData.get(BudgetInfoBean.class); 
        Vector vecBudgetPeriod = (Vector)hmBudgetData.get(BudgetPeriodBean.class);
        Vector vecBudgetCalAmts = (Vector)hmBudgetData.get(BudgetDetailCalAmountsBean.class);
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        edu.mit.coeus.budget.bean.BudgetUpdateTxnBean budgetUpdateTxnBean 
                = new edu.mit.coeus.budget.bean.BudgetUpdateTxnBean(userId);
        budgetUpdateTxnBean.addUpdateBudgetInfo(cvBudgetInfo);
        Timestamp dbTimestamp = prepareTimeStamp();
        if(vecBudgetPeriod != null && vecBudgetPeriod.size() > 0){
            vecBudgetPeriod = filterBudgetPeriod(budgetPeriod, vecBudgetPeriod);
            for(int index = 0; index < vecBudgetPeriod.size(); index ++){
                webTxnBean = new WebTxnBean();
                DynaValidatorForm dynaValidForm  = (DynaValidatorForm)vecBudgetPeriod.get(index);
                String acType = (String)dynaValidForm.get("acType");
                if(acType != null && (acType.equals(TypeConstants.UPDATE_RECORD) || acType.equals(TypeConstants.INSERT_RECORD))){
                    dynaValidForm.set("avUpdateTimestamp",dbTimestamp.toString());
                }
                webTxnBean.getResults(request,
                                    "updBudgetPeriod", dynaValidForm);
                // Update the proposal hierarchy sync flag
                updateProposalSyncFlags(request, proposalNumber);                 
            }
        }
        Vector vecTempBudgetDetailData = new Vector();
        Vector vecDeletedBudgetDetails = (Vector)session.getAttribute("DeletedBudgetDetailsData");
        boolean isRowDeleletd = false;
        if(vecDeletedBudgetDetails != null && vecDeletedBudgetDetails.size() > 0){
            vecTempBudgetDetailData.addAll(vecDeletedBudgetDetails);
            isRowDeleletd = true;
        }
	Vector vecBudgetEntireDetails = (Vector)session.getAttribute("BudgetDetailData");        
        Vector vecFilterDetails = filterBudgetPeriod(budgetPeriod, vecBudgetEntireDetails);
        vecFilterDetails = filterBasedOnLineItemNumber(vecFilterDetails);
        boolean isRowAdded = checkRowAdded(vecFilterDetails);
        // If Any row is added or deleted then define line item sequence and set actype 'U'
        // for other line items . This is required if any line item exist whcih is not related to 
        // Equipment/participant/travel and for display proper data in swings application
        if(isRowAdded || isRowDeleletd ){
            if(vecFilterDetails != null && vecFilterDetails.size() > 0){
                for(int index = 0; index < vecFilterDetails.size(); index++ ){
                    DynaValidatorForm dynaForm = (DynaValidatorForm)vecFilterDetails.get(index);
                    String acType = (String)dynaForm.get("acType");
                    //Setting the budget category code based on seleted cost element
                    if(acType != null && acType.equals(TypeConstants.INSERT_RECORD)){
                        String costElement = (String)dynaForm.get("costElement");
                        int budgetCategoryCode = getBudgetCategoryCode(costElement, session);
                        dynaForm.set("budgetCategoryCode", new Integer(budgetCategoryCode));
                    }else if(acType.equals(EMPTY_STRING)){
                        dynaForm.set("acType",TypeConstants.UPDATE_RECORD);
                    }
                    dynaForm.set("lineItemSequence",new Integer(index + 1));
                }
                vecTempBudgetDetailData.addAll(vecFilterDetails);
            }
        }else{
            if(vecFilterDetails != null && vecFilterDetails.size() > 0){
               //COEUSQA-3991 :RR Budget Form is not updating changed line item values
               // category code was not updating while changing CE 
                for(int index = 0; index < vecFilterDetails.size(); index++ ){
                    DynaValidatorForm dynaForm = (DynaValidatorForm)vecFilterDetails.get(index);
                        String costElement = (String)dynaForm.get("costElement");
                        int budgetCategoryCode = getBudgetCategoryCode(costElement, session);
                        dynaForm.set("budgetCategoryCode", new Integer(budgetCategoryCode));
                }
              //COEUSQA-3991 
                vecTempBudgetDetailData.addAll(vecFilterDetails);
            }
        }
        
        if(vecTempBudgetDetailData != null && vecTempBudgetDetailData.size() > 0){
            webTxnBean = new WebTxnBean();
            for(int ind = 0; ind < vecTempBudgetDetailData.size(); ind++){
                
                DynaValidatorForm dynaValidForm = (DynaValidatorForm)vecTempBudgetDetailData.get(ind);
                String acType = (String)dynaValidForm.get("acType");
                if(acType != null && (acType.equals(TypeConstants.UPDATE_RECORD) || acType.equals(TypeConstants.INSERT_RECORD))){
                    dynaValidForm.set("avUpdateTimestamp",dbTimestamp.toString());
                }
                //COEUSQA-1693 - Cost Sharing Submission - start
                String submitCSFlag = (String)dynaValidForm.get("submitCostSharingFlag");
                if("true".equals(submitCSFlag)){
                   dynaValidForm.set("submitCostSharingFlag","Y"); 
                }else{
                    dynaValidForm.set("submitCostSharingFlag","N"); 
                }
                
                
                 HashMap hmLineItemDetails = new HashMap();
                 
                 hmLineItemDetails.put("proposalNumber", dynaValidForm.get("proposalNumber").toString() );
                 hmLineItemDetails.put("versionNumber", ((Integer)dynaValidForm.get("versionNumber")).intValue());  
                 hmLineItemDetails.put("budgetPeriod", ((Integer)dynaValidForm.get("budgetPeriod")).intValue()); 
                 hmLineItemDetails.put("lineItemNumber", ((Integer)dynaValidForm.get("lineItemNumber")).intValue()); 
                //COEUSQA-1693 - Cost Sharing Submission - end
                 
                 // Added for COEUSQA-3626 : Formulated Cost line items cannot be deleted from budget period if expense exists - Start
                 Boolean isFormulatedLineItem = (Boolean)dynaValidForm.get("isFormulatedLineItem");
                 
                 if(isFormulatedLineItem != null && isFormulatedLineItem.booleanValue() &&
                         TypeConstants.DELETE_RECORD.equals(acType)){
                     webTxnBean.getResults(request, "deleteFormulatedCostDetails", hmLineItemDetails );
                 }
                 // Added for COEUSQA-3626 : Formulated Cost line items cannot be deleted from budget period if expense exists - End
                 
                webTxnBean.getResults(request,"updBudgetLineItemDetails", dynaValidForm);
                // Update the proposal hierarchy sync flag
                updateProposalSyncFlags(request, proposalNumber);                 
            }
        }
       
        if(vecBudgetCalAmts != null){
           for(int index = 0; index < vecBudgetCalAmts.size(); index ++){
                webTxnBean = new WebTxnBean();
                DynaValidatorForm dynaValidForm  = (DynaValidatorForm)vecBudgetCalAmts.get(index);
                String acType = (String)dynaValidForm.get("acType");
                if(acType == null || acType.equals(EMPTY_STRING)){
                    dynaValidForm.set("acType",TypeConstants.UPDATE_RECORD);
                }
                if(acType != null && !acType.equals(EMPTY_STRING)&& !acType.equals(TypeConstants.INSERT_RECORD)){
                    if(!acType.equals(TypeConstants.DELETE_RECORD)){
                        dynaValidForm.set("acType",TypeConstants.UPDATE_RECORD);
                    }
                    
                }
                acType = (String)dynaValidForm.get("acType");
                if(acType != null && (acType.equals(TypeConstants.UPDATE_RECORD) || acType.equals(TypeConstants.INSERT_RECORD))){
                    dynaValidForm.set("avUpdateTimestamp",dbTimestamp.toString());
                }
                webTxnBean.getResults(request,"updBudgetDetailCalAmts", dynaValidForm);
                // Update the proposal hierarchy sync flag
                updateProposalSyncFlags(request, proposalNumber);                 
            } 
        }
        /** Since we have to update the Rate and Base for the whole budget not for the selected period
         *Call CalculateAll Periods and get the Budget Rate and Base and then update to the database
         *Start
         */
        Hashtable htBudgetData = initializeBudgetCalculator((BudgetInfoBean)session.getAttribute("budgetInfoBean"));
        htBudgetData = calculateAllPeriods(htBudgetData,(BudgetInfoBean)session.getAttribute("budgetInfoBean"));
        CoeusVector cvBudgetRateBase = (CoeusVector)htBudgetData.get(BudgetRateBaseBean.class);
        /*Save Rate and Base for all peridos End
         */
        
        // COEUSQA-1983 Underrecovery,nonpersonnel LT doesn't post to Edit/detail until log off - start        
        CoeusVector cvBudgetDetail = (CoeusVector)htBudgetData.get(BudgetDetailBean.class);
        budgetUpdateTxnBean.updateBudgetDetails(cvBudgetDetail);        
        // COEUSQA-1983 Underrecovery,nonpersonnel LT doesn't post to Edit/detail until log off - end
        /*Fix #2862
         *by Geo on 05/04/2007
         *If there is no budgetratebase after calculation, it will not delete existing rateandbase data
         *so, it becomes orphan and will come in report
         *Same code change done in BudgetPersonnelLineItemAction as well
         */
        //Begin Fix
        BudgetInfoBean bgtInf = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        budgetUpdateTxnBean.cleanUpRateBase(bgtInf.getProposalNumber(), bgtInf.getVersionNumber());
        //End Fix
        budgetUpdateTxnBean.updateRateAndBase(cvBudgetRateBase);
        //Added for Case 2228 - Print Budget Summary - Start
        CoeusVector cvBudgetPersonnelDetRateBase = (CoeusVector)htBudgetData.get(BudgetPersonnelDetailsRateBaseBean.class);                        
        budgetUpdateTxnBean.cleanUpPersonnelDetRateBase(bgtInf.getProposalNumber(), bgtInf.getVersionNumber());
        budgetUpdateTxnBean.updateBudgetPersonnelDetRateBase(cvBudgetPersonnelDetRateBase);
      // Added for case 2228 - Print Budget Summary - End        
        //Added for removing instance variable -case # 2960 - start
        String queryKey = bgtInf.getProposalNumber()+bgtInf.getVersionNumber();
        //Added for removing instance variable - case # 2960- end                
        removeQueryEngineCollection(queryKey);
        session.removeAttribute("DeletedBudgetDetailsData");
        
        String url =  "/getBudgetEquipment.do?Period=";
        if(reqToOpenBudgetPeriod > 0){
            url += reqToOpenBudgetPeriod +"&PAGE="+page;
        }else{
            url += budgetPeriod +"&PAGE="+page;
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
        requestDispatcher.forward(request,response);

    }
    //Method to check row is added
    /**
     * method to check line item is added
     * @param vecFilterDetails
     * @throws Exception
     * @return
     */    
    private boolean checkRowAdded(Vector vecFilterDetails) throws Exception{
        boolean isRowAdded = false;
        for(int index = 0; index < vecFilterDetails.size(); index++ ){
            DynaValidatorForm dynaForm = (DynaValidatorForm)vecFilterDetails.get(index);
            String acType = (String)dynaForm.get("acType");
            if(acType!= null && acType.equals(TypeConstants.INSERT_RECORD)){
                isRowAdded = true;
                break;
            }
         }
        return isRowAdded;
    }
     /**
      * Prepare the BudgetDetails for the particular period and line item number
      * @param vecBudgetDetails
      * @param dynaForm
      * @throws Exception
      * @return
      */
    private Vector prepareBudgetEntireDetailsData(Vector vecBudgetDetails,
            HttpServletRequest request)throws Exception{
            HttpSession session = request.getSession();
            Vector vecBudgetEntireDetails = (Vector)session.getAttribute("BudgetDetailData");
            if(vecBudgetDetails != null && vecBudgetDetails.size() > 0){               
                for(int index = 0; index < vecBudgetDetails.size() ; index++){
                    DynaValidatorForm dynaBudgetDetails = (DynaValidatorForm)vecBudgetDetails.get(index);
                    int dynaBudgetPeriod = ((Integer)dynaBudgetDetails.get("budgetPeriod")).intValue();
                    int dynaLineItemSequence = ((Integer)dynaBudgetDetails.get("lineItemNumber")).intValue();
                    String costElement = (String)dynaBudgetDetails.get("costElement");
                    Double lineItemCost = (Double)dynaBudgetDetails.get("lineItemCost");
                    String lineItemDescription = (String)dynaBudgetDetails.get("lineItemDescription");
                    String acType = (String)dynaBudgetDetails.get("acType");
                    
                    for(int ind = 0; ind < vecBudgetEntireDetails.size(); ind ++){
                        DynaValidatorForm dynaDetails = (DynaValidatorForm)vecBudgetEntireDetails.get(ind);
                        int dynaPeriod = ((Integer)dynaDetails.get("budgetPeriod")).intValue();
                        int dynaLineSequence = ((Integer)dynaDetails.get("lineItemNumber")).intValue();
                        if(dynaBudgetPeriod == dynaPeriod && dynaLineItemSequence == dynaLineSequence){
                            dynaDetails.set("lineItemCost", lineItemCost);
                            dynaDetails.set("directCost", lineItemCost);
                            dynaDetails.set("costElement",costElement);
                            dynaDetails.set("onOffCampusFlag",dynaBudgetDetails.get("onOffCampusFlag"));
                            dynaDetails.set("lineItemDescription",lineItemDescription);
                            dynaDetails.set("acType",acType);
                            break;
                        }
                    }
                }
            }
            return vecBudgetEntireDetails;
    }
    /**
     * Prepare the DynaBean for the Custom bean. This will extract the CustomBean data
     * and prepares new DynaForm with the values and return hashMap containing the
     * Vector of form data
     * @param cvBudgetDetails
     * @param cvBudgetPeriod
     * @param cvBudgetDetailCalAmts
     * @param dynaForm
     * @throws Exception
     * @return
     */
    private HashMap prepareBudgetDetailsData(CoeusVector cvBudgetDetails,CoeusVector cvBudgetPeriod,
        CoeusVector cvBudgetDetailCalAmts,CoeusDynaBeansList dynaForm,
        HttpServletRequest request) throws Exception{
        BeanUtilsBean copyBean = null;
        HashMap hmBudgetDetail = new HashMap();
        Vector vecBudgetDetails = new Vector();
        Vector vecBudgetPeriod = new Vector();
        Vector vecBudgetDetailCalAmts = new Vector();
        
        if(cvBudgetDetails!= null){
            for(int index = 0; index < cvBudgetDetails.size() ; index++){
                DynaBean budgetDetailDynaForm = null;
               // budgetDetailDynaForm = ((DynaBean)dynaForm).getDynaClass().newInstance();
                DynaActionForm dynaFormData = dynaForm.getDynaForm(request,"budgetEquipmentData");
                budgetDetailDynaForm = ((DynaBean)dynaFormData).getDynaClass().newInstance();
                BudgetDetailBean budgetDetailBean = (BudgetDetailBean)cvBudgetDetails.get(index);
                copyBean = new BeanUtilsBean();
                copyBean.copyProperties(budgetDetailDynaForm,budgetDetailBean);
                vecBudgetDetails.addElement(budgetDetailDynaForm);
            }
        }
        if(cvBudgetPeriod!= null){
            for(int ind = 0; ind < cvBudgetPeriod.size() ; ind++){
                DynaBean budgetPeriodDynaForm = null;
               // budgetPeriodDynaForm = ((DynaBean)dynaForm).getDynaClass().newInstance();
                DynaActionForm dynaFormData = dynaForm.getDynaForm(request,"budgetEquipmentData");
                budgetPeriodDynaForm = ((DynaBean)dynaFormData).getDynaClass().newInstance();
                BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)cvBudgetPeriod.get(ind);
                copyBean = new BeanUtilsBean();
                copyBean.copyProperties(budgetPeriodDynaForm,budgetPeriodBean);
                vecBudgetPeriod.addElement(budgetPeriodDynaForm);
            }
        }
        
        if(cvBudgetDetailCalAmts!= null){
            for(int calInd = 0; calInd < cvBudgetDetailCalAmts.size() ; calInd++){
                DynaBean dynaCalAmtBean = null;
               // dynaCalAmtBean= ((DynaBean)dynaForm).getDynaClass().newInstance();
                DynaActionForm dynaFormData = dynaForm.getDynaForm(request,"budgetEquipmentData");
                dynaCalAmtBean = ((DynaBean)dynaFormData).getDynaClass().newInstance();
                BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)cvBudgetDetailCalAmts.get(calInd);
                copyBean = new BeanUtilsBean();
                copyBean.copyProperties(dynaCalAmtBean,budgetDetailCalAmountsBean);
                vecBudgetDetailCalAmts.addElement(dynaCalAmtBean);
            }
        }
        hmBudgetDetail.put(BudgetDetailBean.class,vecBudgetDetails);
        hmBudgetDetail.put(BudgetPeriodBean.class,vecBudgetPeriod);
        hmBudgetDetail.put(BudgetDetailCalAmountsBean.class,vecBudgetDetailCalAmts);
        return hmBudgetDetail;
    }
    /**
     * Budget Details for selected period
     * @param budgetPeriod
     * @throws Exception
     * @return
     */    
    private HashMap getBudgetDetailsForPeriod(int budgetPeriod, HttpServletRequest request) throws Exception{
        BeanUtilsBean copyBean = null;
        HashMap hmData = new HashMap();
        CoeusVector cvBudgetDetails = new CoeusVector();
        HttpSession session = request.getSession();
        Vector vecBudgetDetails = (Vector)session.getAttribute("BudgetDetailData");
        // Case Id 2924 - start
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        // Case Id 2924 - end
        Vector vecFilterBudgetDetails = filterBudgetPeriod(budgetPeriod, vecBudgetDetails);
        //COEUSQA-1414 Allow schools to indicate if cost element is still active - Start
        Vector vecCEData = (Vector)session.getAttribute("costElementAllData");
        //COEUSQA-1414 Allow schools to indicate if cost element is still active - End
        Vector vecFilterToShow = getExistingCE(vecFilterBudgetDetails, vecCEData);
        Vector vecOldBudgetLIDetail = getOldBudgetLIDetails(request, budgetPeriod);
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
        //get the cost element details. If it returns true means it holds messages for inactive cost elements
        Vector vecCostElementMessages = getCostElementDetails(budgetPeriod,vecFilterToShow);
        
        if(vecCostElementMessages!=null && vecCostElementMessages.size()>0){
            session.setAttribute("inactive_CE_messages",vecCostElementMessages);
            hmData.put("inActiveData", new Boolean(true));
        }else{
            session.setAttribute("inactive_CE_messages",null);
            hmData.put("inActiveData", new Boolean(false));
        }
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
        /** Get the form data and put into the new DynaValidatorForm for the data comparision
         *and for the copying the data
         */
        hmData.put("dataChanged", new Boolean(false));
        //Modified for Indexed List Properties Validation start
        if(vecFilterToShow != null && vecFilterToShow.size() > 0){
            String dynaLineItem = null;
            for(int oIndex = 0; oIndex < vecFilterToShow.size(); oIndex++){
                DynaValidatorForm dynaBeanBudgetDetails = (DynaValidatorForm)vecFilterToShow.get(oIndex);
                double dynaCost = 0;
                double dynaQuantity = 0;//Added for Case #3132 Changing quantity field from integer to float
                try{
//                    dynaCost = Double.parseDouble(((String)dynaBeanBudgetDetails.get("tempLineItemCost")).replaceAll("[$,/,]",EMPTY_STRING));
                    dynaCost = formatStringToDouble((String)dynaBeanBudgetDetails.get("tempLineItemCost"));
                    //Added for Case #3132 - start
                    //Changing quantity field from integer to float
                    String strPage = (String) session.getAttribute("pageConstantValue");
                    if(strPage.trim().equalsIgnoreCase(CoeusLiteConstants.PARTICIPANT_TRAINEE)) {
                        dynaQuantity = formatStringToDouble((String)dynaBeanBudgetDetails.get("tempQuantity"));
                    } else {
                        dynaQuantity = ((Double)dynaBeanBudgetDetails.get("quantity")).doubleValue();
                    }
                    //Added for Case #3132 - end
                }catch(NumberFormatException ne){
                    //set line Cost 0 if not in number format
                    dynaCost = 0;
                    dynaQuantity = 0;//Added for Case #3132 Changing quantity field from integer to float
                }
                String desc = (String)dynaBeanBudgetDetails.get("lineItemDescription");
                int beanOldLineItemNumber = ((Integer)dynaBeanBudgetDetails.get("lineItemNumber")).intValue();
                String dynaCE = (String)dynaBeanBudgetDetails.get("costElement");
                String acType = (String)dynaBeanBudgetDetails.get("acType");
                String lineDesc = (String)dynaBeanBudgetDetails.get("lineItemDescription");
                //COEUSQA-1693 - Cost Sharing Submission - start
                String submitCostSharingDesc = (String)dynaBeanBudgetDetails.get("submitCostSharingFlag");
                if(submitCostSharingDesc == null || EMPTY_STRING.equals(submitCostSharingDesc)){
                    submitCostSharingDesc = "Y";
                    dynaBeanBudgetDetails.set("submitCostSharingFlag", "Y");
                }
                //COEUSQA-1693 - Cost Sharing Submission - end
                if(acType != null && acType.equals(TypeConstants.INSERT_RECORD)){
                    dynaBeanBudgetDetails.set("lineItemCost", new Double(dynaCost));
                    dynaBeanBudgetDetails.set("quantity", new Double(dynaQuantity));//Added for Case #3132 Changing quantity field from integer to float
                    dynaBeanBudgetDetails.set("directCost", new Double(dynaCost));
                 // Case Id 2924 - start
                  // Modified for Case 3956 - On/Off Campus flag is not being set in Lite  -Start
//                    boolean isCampusFlag = budgetInfoBean.isOnOffCampusFlag();
//                  boolean isCampusFlag = getCampusFlag(request, dynaCE);
                    boolean isCampusFlag = false;
                        if(budgetInfoBean.isDefaultIndicator()){//
                            isCampusFlag = getCampusFlag(request, dynaCE);
                        }else{
                            isCampusFlag = budgetInfoBean.isOnOffCampusFlag();
                        }
                   // Modified for Case 3956 - On/Off Campus flag is not being set in Lite  -End 
                 // Case Id 2924 - end
                    dynaBeanBudgetDetails.set("onOffCampusFlag",new Boolean(isCampusFlag));
                }
                if(vecOldBudgetLIDetail!= null && vecOldBudgetLIDetail.size() > 0){
                    for(int index = 0; index < vecOldBudgetLIDetail.size(); index++){
                        boolean modifyData = false;
                        DynaValidatorForm dynaBudgetDetails = (DynaValidatorForm)vecOldBudgetLIDetail.get(index);
                        int beanLineItemNumber = ((Integer)dynaBudgetDetails.get("lineItemNumber")).intValue();
                        double dynaLineItemCost = ((Double)dynaBudgetDetails.get("lineItemCost")).doubleValue();
                        double quantity = ((Double)dynaBudgetDetails.get("quantity")).doubleValue();//Added for Case #3132 Changing quantity field from integer to float
                        if((beanLineItemNumber == beanOldLineItemNumber) &&
                        (acType != null && !acType.equals(TypeConstants.INSERT_RECORD))){
                            if(dynaLineItemCost != dynaCost){
                                dynaBeanBudgetDetails.set("lineItemCost", new Double(dynaCost));
                                dynaBeanBudgetDetails.set("directCost", new Double(dynaCost));
                                modifyData = true;
                            }
                            //Added for Case #3132 - start
                            //Changing quantity field from integer to float
                            if(quantity != dynaQuantity) {
                                dynaBeanBudgetDetails.set("quantity", new Double(dynaQuantity));
                                modifyData = true;
                            }
                            //Added for Case #3132 - end
                            dynaLineItem = (String)dynaBudgetDetails.get("lineItemDescription");
                            if(dynaLineItem == null){
                                dynaLineItem = EMPTY_STRING;
                            }
                            if(!dynaLineItem.equals(EMPTY_STRING) &&!dynaLineItem.trim().equals(lineDesc)){
                                dynaBeanBudgetDetails.set("lineItemDescription",lineDesc);
                                modifyData = true;
                            }
                            //COEUSQA-1693 - Cost Sharing Submission - start
                            dynaLineItem = (String)dynaBudgetDetails.get("submitCostSharingFlag");
                            if(dynaLineItem == null || EMPTY_STRING.equals(dynaLineItem)){
                                dynaLineItem = "Y";
                            }
                            if(!dynaLineItem.trim().equals(submitCostSharingDesc)){
                                dynaBeanBudgetDetails.set("submitCostSharingFlag",submitCostSharingDesc);
                                modifyData = true;
                            }
                            //COEUSQA-1693 - Cost Sharing Submission - start
                            
                            dynaLineItem = (String)dynaBudgetDetails.get("costElement");
                            if(!dynaLineItem.equals(dynaCE)){
                                dynaBeanBudgetDetails.set("costElement",dynaCE);
                            // Case Id 2924 - start
                            // Modified for Case 3956 - On/Off Campus flag is not being set in Lite  -Start    
//                                boolean isCampusFlag = budgetInfoBean.isOnOffCampusFlag();
//                              boolean isCampusFlag = getCampusFlag(request, dynaCE);
                              boolean isCampusFlag = false;
                            if(budgetInfoBean.isDefaultIndicator()){//
                                isCampusFlag = getCampusFlag(request, dynaCE);
                            }else{
                                isCampusFlag = budgetInfoBean.isOnOffCampusFlag();
                            }
                              // Modified for Case 3956 - On/Off Campus flag is not being set in Lite  -End
                            // Case Id 2924 - end
                                dynaBeanBudgetDetails.set("onOffCampusFlag",new Boolean(isCampusFlag));
                                dynaBeanBudgetDetails.set("changeCostElement",new Boolean(true));
                                modifyData = true;
                            }
                            
                            if(modifyData || (acType != null && acType.equals(TypeConstants.UPDATE_RECORD))){
                                hmData.put("dataChanged", new Boolean(true));
                                if(acType != null && !acType.equals(TypeConstants.INSERT_RECORD) && !acType.equals(TypeConstants.DELETE_RECORD)){
                                    dynaBudgetDetails.set("acType", TypeConstants.UPDATE_RECORD);
                                }
                            }
                            break;
                            
                        }
                    }
                }
            }
        }
        if(vecFilterToShow!= null && vecFilterToShow.size() > 0){
            //updating session values
            for(int oIndex = 0; oIndex < vecFilterToShow.size(); oIndex++){
                DynaValidatorForm dynaBeanBudgetDetails = (DynaValidatorForm)vecFilterToShow.get(oIndex);
                int beanOldLineItemNumber = ((Integer)dynaBeanBudgetDetails.get("lineItemNumber")).intValue();
                for(int ind = 0 ; ind < vecFilterBudgetDetails.size(); ind ++){
                    DynaValidatorForm dynaValid = (DynaValidatorForm)vecFilterBudgetDetails.get(ind);
                    int dynaLineItemNumber = ((Integer)dynaValid.get("lineItemNumber")).intValue();
                    if(beanOldLineItemNumber == dynaLineItemNumber){
                        dynaValid.set("lineItemCost", dynaBeanBudgetDetails.get("lineItemCost"));
                        dynaValid.set("directCost",dynaBeanBudgetDetails.get("directCost"));
                        dynaValid.set("costElement" ,dynaBeanBudgetDetails.get("costElement"));
                        dynaValid.set("onOffCampusFlag",dynaBeanBudgetDetails.get("onOffCampusFlag"));
                        dynaValid.set("lineItemDescription",dynaBeanBudgetDetails.get("lineItemDescription"));
                        dynaValid.set("quantity", dynaBeanBudgetDetails.get("quantity"));//Added for Case #3132 Changing quantity field from integer to float
                        dynaValid.set("acType",dynaBeanBudgetDetails.get("acType"));
                        break;
                    }
                }//end for
            }
         }
            
        //Modified for Indexed List Properties Validation end
        if(vecFilterBudgetDetails != null && vecFilterBudgetDetails.size() > 0){
            for(int index = 0; index < vecFilterBudgetDetails.size(); index++){
                DynaValidatorForm dynaBudgetDetails = (DynaValidatorForm)vecFilterBudgetDetails.get(index);
                BudgetDetailBean budgetDetailBean = new BudgetDetailBean();
                copyBean = new BeanUtilsBean();
                copyBean.copyProperties(budgetDetailBean,dynaBudgetDetails);
                cvBudgetDetails.addElement(budgetDetailBean);
            }
        }
        // check if the row is added. that is check for the AcType I
        boolean isRowAdded = checkRowAdded(vecFilterBudgetDetails);
        // Check for the deleted data
        Vector vecDeletedData = (Vector)session.getAttribute("DeletedBudgetDetailsData");
        if(vecDeletedData!= null && vecDeletedData.size() > 0){
            hmData.put("RowDeleted",new Boolean(true));
        }else{
            hmData.put("RowDeleted",new Boolean(false));
        }
        hmData.put("RowAdded",new Boolean(isRowAdded));
        hmData.put("BudgetDetails",cvBudgetDetails == null ? new CoeusVector() : cvBudgetDetails);
        return hmData;
        
    }
    /**
     *
     * Method to Add line Item to budget Details
     * @return String
     * @param dynaForm
     * @throws Exception
     */
    // added parameter actionForm to this method after removing instance variable - case # 2960
    private String addLineItemDetails(CoeusDynaBeansList dynaForm,
        HttpServletRequest request,ActionForm actionForm) throws Exception{
       HttpSession session = request.getSession();
       Vector vecBudgetPeriodData = (Vector)session.getAttribute("BudgetPeriodData");
       Vector vecBudgetEquiDetails = (Vector)session.getAttribute("BudgetDetailData");
       int budgetPeriod = Integer.parseInt(request.getParameter("AddBudgetPeriodRow"));
       Vector vecFilterBudgetPeriod =  filterBudgetPeriod(budgetPeriod,vecBudgetEquiDetails);
       //COEUSQA-1414 Allow schools to indicate if cost element is still active - Start
       Vector vecCEData = (Vector)session.getAttribute("costElementAllData");
       //COEUSQA-1414 Allow schools to indicate if cost element is still active - End
       Vector vecFilterToShow = getExistingCE(vecFilterBudgetPeriod, vecCEData);
       vecFilterToShow = getLineItemModifiedData(vecFilterToShow, vecFilterBudgetPeriod,request);
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
//       newDynaForm.set("quantity",new Integer(0));
       newDynaForm.set("quantity", new Double(0));
       //Modified for Case #3132 - start
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
       newDynaForm.set("updateTimestamp",dbTimestamp.toString());
       newDynaForm.set("acType",TypeConstants.INSERT_RECORD);
       newDynaForm.set("lineItemDescription", EMPTY_STRING);
       newDynaForm.set("isRowAdded", new Boolean(true));
       //COEUSQA-1693 - Cost Sharing Submission - start
       newDynaForm.set("submitCostSharingFlag", "Y");
       //COEUSQA-1693 - Cost Sharing Submission - end
       if(vecBudgetEquiDetails == null){
           vecBudgetEquiDetails = new Vector();
       }
       //commented for default empty row start 1
//       Vector vecCostElementData = (Vector)session.getAttribute("costElementData");
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
      *
      * Method to remove line Item from Budget Details
      * @return String
      * @param dynaForm
      * @throws Exception
      */
    // added parameter actionForm to this method after removing instance variable - case # 2960
    private String removeLineItemDetails(HttpServletRequest request,ActionForm actionForm) throws Exception{
        HttpSession session = request.getSession();
        int budgetPeriod = Integer.parseInt(request.getParameter("budgetPeriod"));
        int lineItemNumber = Integer.parseInt(request.getParameter("lineItemNumber"));
        Vector vecBudgetEquiDetails = (Vector)session.getAttribute("BudgetDetailData");
        Vector vecBudgetDeletedData = new Vector();
        if(session.getAttribute("DeletedBudgetDetailsData") != null ){
            vecBudgetDeletedData = (Vector)session.getAttribute("DeletedBudgetDetailsData");
        }
        Vector vecFilterToshow = null;
        Vector vecFilterBudgetPeriod = null;
        //COEUSQA-1414 Allow schools to indicate if cost element is still active - Start
        Vector vecCEData = (Vector)session.getAttribute("costElementAllData");
        //COEUSQA-1414 Allow schools to indicate if cost element is still active - End
        if(vecBudgetEquiDetails != null && vecBudgetEquiDetails.size() > 0){
            vecFilterBudgetPeriod =  filterBudgetPeriod(budgetPeriod,vecBudgetEquiDetails);
            vecFilterToshow = getExistingCE(vecFilterBudgetPeriod, vecCEData);
            vecFilterToshow = getLineItemModifiedData(vecFilterToshow, vecFilterBudgetPeriod, request);
        }
        
        if(vecBudgetEquiDetails != null && vecBudgetEquiDetails.size() > 0){
            for(int index = 0; index < vecBudgetEquiDetails.size(); index++) {
                boolean flagCEExists = false;
                DynaValidatorForm dynaValidForm = (DynaValidatorForm)vecBudgetEquiDetails.get(index);
                int beanLineItemNumber = ((Integer)dynaValidForm.get("lineItemNumber")).intValue();
                int beanBudgetPeriod = ((Integer)dynaValidForm.get("budgetPeriod")).intValue();
                if(beanBudgetPeriod == budgetPeriod && beanLineItemNumber == lineItemNumber ){
                    String acType = (String)dynaValidForm.get("acType");
                    String dynaCE = (String)dynaValidForm.get("costElement");
                    //If actype EMPTY or UPDATE then DELETE
                    if(acType == null || acType.equals(EMPTY_STRING)
                    || (acType != null && !acType.equals(TypeConstants.INSERT_RECORD))){
                        for(int ind = 0; ind < vecCEData.size();ind++){
                            CategoryBean categoryBean = (CategoryBean)vecCEData.get(ind);
                            if(dynaCE.equals(categoryBean.getCostElement())){
                                flagCEExists = true;
                                break;
                            }
                        }
                        if(flagCEExists){
                            dynaValidForm.set("acType",TypeConstants.DELETE_RECORD);
                            vecBudgetDeletedData.addElement(dynaValidForm);
                            session.setAttribute("dataChanges", new Boolean(true));
                        }
                        
                    }
                    vecBudgetEquiDetails.remove(dynaValidForm);
                    break;
                }
            }
        }
       vecFilterBudgetPeriod =  filterBudgetPeriod(budgetPeriod,vecBudgetEquiDetails);
       vecFilterToshow = getExistingCE(vecFilterBudgetPeriod, vecCEData);
       
       session.setAttribute("DeletedBudgetDetailsData",vecBudgetDeletedData);
       session.setAttribute("BudgetDetailData", vecBudgetEquiDetails );
       request.setAttribute("FilterBudgetDetailData", vecFilterToshow);
       request.setAttribute("SelectedBudgetPeriodNumber", new Integer(budgetPeriod));
       
       //validation framework start
        CoeusDynaBeansList coeusLineItemDynaList = (CoeusDynaBeansList)actionForm;
        coeusLineItemDynaList.setList(vecFilterToshow);
        List beanList = new ArrayList();
        beanList.add(new Integer(budgetPeriod));
        coeusLineItemDynaList.setBeanList(beanList);
        session.setAttribute("lineItemDynaBeanList",coeusLineItemDynaList);
        //validation framework end
       
       return "success";
    }
    /**
     * Get the BudgetLineItemDetails for the selected Period and Line Item Number
     * Save the line item number, calculate, get the BudgetDetailCalAmts data
     * for the specific period and line item number
     * @param dynaForm
     * @throws Exception
     * @return
     */
    private String  getBudgetLineItemDetails(DynaValidatorForm dynaForm,
        HttpServletRequest request) throws Exception{
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        String budgetPeriod = request.getParameter("budgetPeriod");
        String lineItemNumber = request.getParameter("lineItemNumber");
        validateForApplyToLaterPeriods(budgetPeriod, session);
        BeanUtilsBean copyBean = new BeanUtilsBean();
            Vector vecAllBudgetDetails = (Vector)session.getAttribute("BudgetDetailData");
            Vector vecBudgetDetail = filterBudgetPeriod(Integer.parseInt(budgetPeriod), vecAllBudgetDetails);
            if(vecBudgetDetail!= null && vecBudgetDetail.size() > 0){
                for(int index = 0; index < vecBudgetDetail.size(); index++){
                    DynaValidatorForm dynaFormData = (DynaValidatorForm)vecBudgetDetail.get(index);
                    int lineItemNo = ((Integer)dynaFormData.get("lineItemNumber")).intValue();
                    if(Integer.parseInt(lineItemNumber) == lineItemNo){
                        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("MM/dd/yyyy");
                        String tempLineItemStartDate = simpleDateFormat.format(dynaFormData.get("lineItemStartDate"));
                        String tempLineItemEndDate = simpleDateFormat.format(dynaFormData.get("lineItemEndDate"));
                        double lineItemCost = ((Double)dynaFormData.get("lineItemCost")).doubleValue();
                        double costSharingAmount = ((Double)dynaFormData.get("costSharingAmount")).doubleValue();
                        double underRecoveryAmount = ((Double)dynaFormData.get("underRecoveryAmount")).doubleValue();
                        dynaFormData.set("tempLineItemStartDate",tempLineItemStartDate);
                        dynaFormData.set("tempLineItemEndDate",tempLineItemEndDate);
                        copyBean.copyProperties( dynaForm,dynaFormData);
                        java.text.NumberFormat numberFormat = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US);
                        dynaForm.set("lineItemCost",numberFormat.format(lineItemCost));  
                        dynaForm.set("costSharingAmount",numberFormat.format(costSharingAmount)); 
                        dynaForm.set("underRecoveryAmount",numberFormat.format(underRecoveryAmount)); 
                        
                        //Added for Case #3132 - start
                        //Changing quantity field from integer to float
                        double quantity = ((Double)dynaFormData.get("quantity")).doubleValue();
                        numberFormat = java.text.NumberFormat.getNumberInstance(java.util.Locale.US);
                        String strQuantity = numberFormat.format(quantity);
                        if(strQuantity.indexOf(".") == -1) {
                            strQuantity = strQuantity + ".00";
                        }
                        dynaForm.set("quantity", strQuantity);
                        //Added for Case #3132 - end
                        
                        break;
                    }
                }
            }
            request.setAttribute("budgetDetails",dynaForm);
            CoeusVector cvBudgetDetailCalAmts = new CoeusVector();
            Vector vecBudgetDetailCalAmts = (Vector)session.getAttribute("BudgetDetailCalAmts");
            
        if(vecBudgetDetailCalAmts!= null && vecBudgetDetailCalAmts.size() > 0){
            int reqBudgetPeriod = Integer.parseInt(budgetPeriod);
            int reqLineItemNumber = Integer.parseInt(lineItemNumber);
            for(int index = 0; index < vecBudgetDetailCalAmts.size(); index++){
                DynaValidatorForm dynaFormData = (DynaValidatorForm)vecBudgetDetailCalAmts.get(index);
                int dynaLineItemNo = ((Integer)dynaFormData.get("lineItemNumber")).intValue();
                int dynaBudgetNo = ((Integer)dynaFormData.get("budgetPeriod")).intValue();
                if(reqBudgetPeriod == dynaBudgetNo && reqLineItemNumber == dynaLineItemNo){
                    cvBudgetDetailCalAmts.addElement(dynaFormData);
                }
            }
            vecBudgetDetailCalAmts = null;
            CoeusVector cvSortCalAmts = sortCalAmts(cvBudgetDetailCalAmts);
            cvSortCalAmts.sort("rateClassCode",true);
            cvBudgetDetailCalAmts = prepareDynaBean(cvSortCalAmts,dynaForm);
            session.setAttribute("budgetDetailCalAmts", cvBudgetDetailCalAmts);
            //request.setAttribute("budgetDetailCalAmts", cvBudgetDetailCalAmts);
        }
        request.setAttribute("period", new Integer(Integer.parseInt(budgetPeriod)));
        navigator = "success";
        return navigator;
       
    }
    /**
     * method to sort the line item number
     * @param vecFilterData
     * @throws Exception
     * @return Vector object of sorted line item number
     */    
    private Vector filterBasedOnLineItemNumber(Vector vecFilterData) throws Exception{
        // for sorting based on line item number so that every time we'll get sorted 
        //vector based on line item number
        CoeusVector cvFilterData = null;
        BudgetDetailBean budgetDetailBean = null;
        BeanUtilsBean beanUtilsBean = null;
        DynaValidatorForm dynaForm = null;
        if(vecFilterData!= null && vecFilterData.size() > 0){
            dynaForm = (DynaValidatorForm)vecFilterData.get(0);
            cvFilterData = new CoeusVector();
            for(int periodIndex = 0;periodIndex < vecFilterData.size(); periodIndex++){
                beanUtilsBean = new BeanUtilsBean();
                budgetDetailBean = new BudgetDetailBean();
                DynaValidatorForm periodDynaBean = (DynaValidatorForm)vecFilterData.get(periodIndex);
                beanUtilsBean.copyProperties(budgetDetailBean,periodDynaBean);
                cvFilterData.addElement(budgetDetailBean);
            }
        }
        if(cvFilterData != null && cvFilterData.size() > 0){
            cvFilterData.sort("lineItemNumber");
            vecFilterData.removeAllElements();
            for(int periodIndex = 0;periodIndex <cvFilterData.size(); periodIndex++){
                beanUtilsBean = new BeanUtilsBean();
                budgetDetailBean = (BudgetDetailBean)cvFilterData.get(periodIndex);
                DynaBean dynaBean = ((DynaBean)dynaForm).getDynaClass().newInstance();
                beanUtilsBean.copyProperties(dynaBean,budgetDetailBean);
                vecFilterData.addElement(dynaBean);
            }
            
        }
        return vecFilterData;
    }
    /**
     * Method to get budgetCategoryCode based on selectedCostElement
     * @param selectedCostElement
     * @throws Exception
     * @return budgetCategory code
     */    
    private int getBudgetCategoryCode(String selectedCostElement,
        HttpSession session) throws Exception{
        Vector vecCostElements = (Vector)session.getAttribute("costElementData");
        int budgetCategoryCode = 0;
        if(vecCostElements != null){
            for(int index = 0; index < vecCostElements.size() ; index++){
                CategoryBean categoryBean = (CategoryBean)vecCostElements.get(index);
                String beanCostElement = categoryBean.getCostElement();
                if(selectedCostElement.equals(beanCostElement)){
                    budgetCategoryCode = categoryBean.getBudgetCategoryCode();
                    break;
                }
            }
        }
        return budgetCategoryCode;
    }

    
     /**Method to prepare DyanBean from BudgetDetailCalAmountsBean
      * @param cvSortCalAmts
      * @param dynaForm
      * @throws Exception
      * @return vector of cal amt of dynaforms
      */     
     private CoeusVector prepareDynaBean(CoeusVector cvSortCalAmts,DynaValidatorForm dynaForm) throws Exception{
        CoeusVector cvBudgetDetailCalAmts = new CoeusVector();
        BeanUtilsBean copyBean = null;
        if(cvSortCalAmts != null && cvSortCalAmts.size() > 0){
           for(int index = 0; index < cvSortCalAmts.size(); index++){
                BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)cvSortCalAmts.get(index);
                copyBean = new BeanUtilsBean();
                DynaBean dynaBean = ((DynaBean)dynaForm).getDynaClass().newInstance();
                copyBean.copyProperties(dynaBean, budgetDetailCalAmountsBean);
                cvBudgetDetailCalAmts.addElement(dynaBean);
                
            } 
        }
        return cvBudgetDetailCalAmts;
    }
     /**Method to prepare BudgetDetailCalAmountsBean to dynaBean. This method is used for 
      *display rate type in ascending order
      * @param cvBudgetDetailCalAmts
      * @throws Exception
      * @return vector of budget detail cal amounts Bean
      */     
    private CoeusVector sortCalAmts(CoeusVector cvBudgetDetailCalAmts) throws Exception{
        CoeusVector sortCalAmts = new CoeusVector();
        BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = null;
        BeanUtilsBean copyBean = null;
        if(cvBudgetDetailCalAmts != null && cvBudgetDetailCalAmts.size() > 0){
            for(int index = 0; index < cvBudgetDetailCalAmts.size(); index++){
                DynaValidatorForm dynaForm = (DynaValidatorForm)cvBudgetDetailCalAmts.get(index);
                budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
                copyBean = new BeanUtilsBean();
                copyBean.copyProperties(budgetDetailCalAmountsBean, dynaForm);
                sortCalAmts.addElement(budgetDetailCalAmountsBean);
                
            }
        }
        return sortCalAmts;
    }
    /**
     *
     * @param budgetPeriod
     * @param vecBudgetPeriod
     * @param vecBudgetEntireDetails
     * @throws Exception
     */    
    // added parameter actionForm to this method after removing instance variable - case # 2960
    private void setDataForExceedTotalCost(int budgetPeriod,Vector vecBudgetPeriod,Vector
    vecBudgetEntireDetails, HttpServletRequest request,ActionForm actionForm, String queryKey) throws Exception{
        HttpSession session = request.getSession();
        if(vecBudgetEntireDetails != null && vecBudgetEntireDetails.size() > 0){
            Vector vecBudgetDetails = filterBudgetPeriod(budgetPeriod, vecBudgetEntireDetails);
            vecBudgetDetails = getExistingCE(vecBudgetDetails, (Vector)session.getAttribute("costElementData"));
            Vector vecAllPeriods = getPeriodForShowTab(vecBudgetPeriod);
            boolean dataExist = false;
            if(vecAllPeriods != null && vecAllPeriods.size() > 0){
                for(int index = 0; index < vecAllPeriods.size(); index++){
                    int budPeriod = ((Integer)vecAllPeriods.get(index)).intValue();
                    Vector vecFilterTabBudgetPeriod =  filterBudgetPeriod(budPeriod,vecBudgetEntireDetails);
                    if(vecFilterTabBudgetPeriod.size() > 0){
                        dataExist = true;
                        break;
                    }
                }
            }
            if(!dataExist){
                vecBudgetPeriod = filterBudgetPeriod(1, vecBudgetPeriod);
                budgetPeriod = MAKE_DEFAULT_PERIOD;
                vecBudgetDetails = filterBudgetPeriod(budgetPeriod, vecBudgetEntireDetails);
                vecBudgetDetails = getExistingCE(vecBudgetDetails, (Vector)session.getAttribute("costElementData"));
            }
            
            session.setAttribute("dataChanges", new Boolean(true));
            request.setAttribute("FilterBudgetDetailData",vecBudgetDetails);
            request.setAttribute("SelectedBudgetPeriodNumber", new Integer(budgetPeriod));
            session.setAttribute("BudgetPeriodData",vecBudgetPeriod);
            session.setAttribute("BudgetDetailData", vecBudgetEntireDetails);
            //validation framework start
            CoeusDynaBeansList coeusLineItemDynaList = (CoeusDynaBeansList)actionForm;
            coeusLineItemDynaList.setList(vecBudgetDetails);
            List beanList = new ArrayList();
            beanList.add(new Integer(budgetPeriod));
            coeusLineItemDynaList.setBeanList(beanList);
            session.setAttribute("lineItemDynaBeanList",coeusLineItemDynaList);
            //validation framework end
            removeQueryEngineCollection(queryKey);
            
        }
      
    }
    
    /**
     * Validate when applyToLater Periods action is performed
     * Check if the Last period is made to perform the action and
     * Check if the generate all periods is done
     * @param budgetInfoBean
     * @param strBudgetPeriod
     * @throws Exception
     */
     private void validateForApplyToLaterPeriods(String strBudgetPeriod,
         HttpSession session) throws Exception{
         int budgetPeriod = Integer.parseInt(strBudgetPeriod);
         Vector vecBudgetPeriod = (Vector)session.getAttribute("BudgetPeriodData");
         boolean isValidated = false;
         session.setAttribute("firstPeriod", new Boolean(false));
         session.setAttribute("lastPeriod", new Boolean(false));
         if(vecBudgetPeriod.size() > 1){
             if(vecBudgetPeriod.size() == budgetPeriod){
                 //request.setAttribute("lastPeriod", new Boolean(true));
                 session.setAttribute("lastPeriod", new Boolean(true));
                 isValidated = true;
             }
             //Bug fixing case#2738,2742 - starts
//             else{
//                 session.setAttribute("lastPeriod", new Boolean(false));
//                // request.setAttribute("lastPeriod", new Boolean(false));
//             }
             //Bug fixing case#2738,2742 - ends
         }else{
             session.setAttribute("firstPeriod", new Boolean(true));
           //  request.setAttribute("firstPeriod", new Boolean(true));
         }
         

     }
     //commented after modifying web txn bean for on off campus flag start
//     private void saveBudgetPeriodLineItems(Hashtable htCalculatedData, int budgetPeriod,
//     int reqToOpenBudgetPeriod) throws Exception{
//         Vector vecTempBudgetDetailData = new Vector();
//         Vector vecDeletedBudgetDetails = (Vector)session.getAttribute("DeletedBudgetDetailsData");
//         boolean isRowDeleletd = false;
//         if(vecDeletedBudgetDetails != null && vecDeletedBudgetDetails.size() > 0){
//             vecTempBudgetDetailData.addAll(vecDeletedBudgetDetails);
//             isRowDeleletd = true;
//         }
//         Vector vecBudgetEntireDetails = (Vector)session.getAttribute("BudgetDetailData");
//         Vector vecFilterDetails = filterBudgetPeriod(budgetPeriod, vecBudgetEntireDetails);
//         vecFilterDetails = filterBasedOnLineItemNumber(vecFilterDetails);
//         boolean isRowAdded = checkRowAdded(vecFilterDetails);
//         // If Any row is added or deleted then define line item sequence and set actype 'U'
//         // for other line items . This is required if any line item exist whcih is not related to
//         // Equipment/participant/travel and for display proper data in swings application
//         if(isRowAdded || isRowDeleletd ){
//             if(vecFilterDetails != null && vecFilterDetails.size() > 0){
//                 for(int index = 0; index < vecFilterDetails.size(); index++ ){
//                     DynaValidatorForm dynaForm = (DynaValidatorForm)vecFilterDetails.get(index);
//                     String acType = (String)dynaForm.get("acType");
//                     //Setting the budget category code based on seleted cost element
//                     if(acType != null && acType.equals(TypeConstants.INSERT_RECORD)){
//                         String costElement = (String)dynaForm.get("costElement");
//                         int budgetCategoryCode = getBudgetCategoryCode(costElement);
//                         dynaForm.set("budgetCategoryCode", new Integer(budgetCategoryCode));
//                     }else if(acType.equals(EMPTY_STRING)){
//                         dynaForm.set("acType",TypeConstants.UPDATE_RECORD);
//                     }
//                     dynaForm.set("lineItemSequence",new Integer(index + 1));
//                 }
//                 vecTempBudgetDetailData.addAll(vecFilterDetails);
//             }
//         }else{
//             if(vecFilterDetails != null && vecFilterDetails.size() > 0){
//                 vecTempBudgetDetailData.addAll(vecFilterDetails);
//             }
//         }
//         CoeusVector cvBudgetLineDetails = null;
//         if(vecTempBudgetDetailData != null && vecTempBudgetDetailData.size() > 0){
//             cvBudgetLineDetails = new CoeusVector();
//             BeanUtilsBean beanUtilsBean = null;
//             BudgetDetailBean budgetDetailBean = null;
//             for(int ind = 0; ind < vecTempBudgetDetailData.size(); ind++){
//                 DynaValidatorForm dynaValidForm = (DynaValidatorForm)vecTempBudgetDetailData.get(ind);
//                 beanUtilsBean = new BeanUtilsBean();
//                 budgetDetailBean = new BudgetDetailBean();
//                 beanUtilsBean.copyProperties(budgetDetailBean,dynaValidForm);
//                 cvBudgetLineDetails.addElement(budgetDetailBean);
//             }
//         }
//         htCalculatedData.remove(BudgetDetailBean.class);
//         htCalculatedData.put(BudgetDetailBean.class, cvBudgetLineDetails);
//         
//         edu.mit.coeus.bean.UserInfoBean userInfoBean = (edu.mit.coeus.bean.UserInfoBean)session.getAttribute("user");
//         edu.mit.coeus.budget.bean.BudgetUpdateTxnBean budgetUpdateTxnBean
//         = new edu.mit.coeus.budget.bean.BudgetUpdateTxnBean(userInfoBean.getUserId());
//         boolean isUpdated = budgetUpdateTxnBean.addUpdDeleteBudget(htCalculatedData);
//         if(isUpdated){
//             CoeusVector cvBudgetRateBase = (CoeusVector)htCalculatedData.get(BudgetRateBaseBean.class);
//             budgetUpdateTxnBean.updateRateAndBase(cvBudgetRateBase);
//         }
//         htCalculatedData = null;
//         removeQueryEngineCollection(queryKey);
//         session.removeAttribute("DeletedBudgetDetailsData");
//         session.setAttribute("dataChanges", new Boolean(false));
//         String page = (String)session.getAttribute("pageConstantValue");
//         String url =  "/getBudgetEquipment.do?Period=";
//         if(reqToOpenBudgetPeriod > 0){
//             url += reqToOpenBudgetPeriod +"&PAGE="+page;
//         }else{
//             url += budgetPeriod +"&PAGE="+page;
//         }
//         RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
//         requestDispatcher.forward(request,response);
//         
//     }
     //commented after modifying web txn bean for on off campus flag end
  /**
     *
     * @param vecExistCE
     * @param vecFilterBudgetPeriod
     * @throws Exception
     * @return
     */    
    private Vector getLineItemModifiedData(Vector vecExistCE, Vector vecFilterBudgetPeriod,
        HttpServletRequest request) throws Exception{
      // Case id 2924 - start
        HttpSession session = request.getSession();
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
      // Case id 2924 - end
        if(vecExistCE != null && vecExistCE.size() > 0 ){
            for(int index = 0; index < vecExistCE.size(); index++){
                boolean modifyData = false;
                DynaValidatorForm dynaBudgetDetails = (DynaValidatorForm)vecExistCE.get(index);
                int beanLineItemNumber = ((Integer)dynaBudgetDetails.get("lineItemNumber")).intValue();
                String strLineItem = request.getParameter(GET_FIELDNAME+index+COST_ELEMENT_AS_REQUEST);
                String dynaLineItem = null;
                if(strLineItem != null && !strLineItem.equals(EMPTY_STRING)){
                    dynaLineItem = (String)dynaBudgetDetails.get("costElement");
                    if(!dynaLineItem.equals(strLineItem)){
                        dynaBudgetDetails.set("costElement",strLineItem);
                    // Case Id 2924 - start
                    // Modified for Case 3956 - On/Off Campus flag is not being set in Lite  -Start    
//                        boolean isCampusFlag = budgetInfoBean.isOnOffCampusFlag();
//                      boolean isCampusFlag = getCampusFlag(request, strLineItem);
                      boolean isCampusFlag = false;
                        if(budgetInfoBean.isDefaultIndicator()){//
                            isCampusFlag = getCampusFlag(request, strLineItem);
                        }else{
                            isCampusFlag = budgetInfoBean.isOnOffCampusFlag();
                        }
                    // Modified for Case 3956 - On/Off Campus flag is not being set in Lite -End  
                    // Case Id 2924 - end
                        dynaBudgetDetails.set("onOffCampusFlag",new Boolean(isCampusFlag));
                        dynaBudgetDetails.set("changeCostElement",new Boolean(true));
                        modifyData = true;
                    }
                }
                strLineItem = request.getParameter(GET_FIELDNAME+index+LINE_DESC_AS_REQUEST).trim();
                if(strLineItem != null){
                    dynaLineItem = (String)dynaBudgetDetails.get("lineItemDescription");
                    if(dynaLineItem == null){
                        dynaLineItem = EMPTY_STRING;
                    }
                    if(!dynaLineItem.equals(strLineItem)){
                        dynaBudgetDetails.set("lineItemDescription",strLineItem);
                        modifyData = true;
                    }
                }
                strLineItem = request.getParameter(GET_FIELDNAME+index+LINE_COST_AS_REQUEST);
                if(strLineItem != null && !strLineItem.equals(EMPTY_STRING)){
                    double dynaLineItemCost = ((Double)dynaBudgetDetails.get("lineItemCost")).doubleValue();
                    double reqLineItemCost = 0;
                    try{
                        reqLineItemCost = formatStringToDouble(strLineItem);
                    }catch(NumberFormatException ne){
                        //set line Cost 0 if not in number format
                        reqLineItemCost = 0;
                    }
                    if(dynaLineItemCost != reqLineItemCost){
                        dynaBudgetDetails.set("lineItemCost", new Double(reqLineItemCost));
                        dynaBudgetDetails.set("directCost", new Double(reqLineItemCost));
                        modifyData = true;
                    }
                }
                if(modifyData){
                    String acType = (String)dynaBudgetDetails.get("acType");
                    if(acType != null && !acType.equals(TypeConstants.INSERT_RECORD) && !acType.equals(TypeConstants.DELETE_RECORD)){
                        dynaBudgetDetails.set("acType", TypeConstants.UPDATE_RECORD);
                    }
                }
                //setting data for session
                for(int ind = 0 ; ind < vecFilterBudgetPeriod.size(); ind ++){
                    DynaValidatorForm dynaValid = (DynaValidatorForm)vecFilterBudgetPeriod.get(ind);
                    int dynaLineItemNumber = ((Integer)dynaValid.get("lineItemNumber")).intValue();
                    if(beanLineItemNumber == dynaLineItemNumber){
                        dynaValid.set("lineItemCost", dynaBudgetDetails.get("lineItemCost"));
                        dynaValid.set("directCost", dynaBudgetDetails.get("directCost"));
                        dynaValid.set("costElement",dynaBudgetDetails.get("costElement"));
                        dynaValid.set("onOffCampusFlag",dynaBudgetDetails.get("onOffCampusFlag"));
                        dynaValid.set("lineItemDescription",dynaBudgetDetails.get("lineItemDescription"));
                        dynaValid.set("acType",dynaBudgetDetails.get("acType"));
                        break;
                    }
                }
                
            }
        }
        return ((vecExistCE == null ) ? new Vector() : vecExistCE);
    }
    
    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
    /**
     * Method to check the line item is formulated line item
     * @param request 
     * @param budgetPeriod 
     * @param lineItemNumber 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return isLineItemFormulated
     */
    private boolean isLineItemFormulated(HttpServletRequest request,int budgetPeriod, int lineItemNumber, int subAwardNumber) throws CoeusException, DBException {
        boolean isLineItemFormulated = false;
        if(subAwardNumber == 0){
            boolean hasFormulatedDetails = false;
            HttpSession session =  request.getSession();
            BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
            CoeusVector cvFormulatedTypes = budgetDataTxnBean.getFormulatedTypes();
            if(cvFormulatedTypes != null && !cvFormulatedTypes.isEmpty()){
                CoeusVector cvFormulatedCostDetails = (CoeusVector)session.getAttribute("FormulatedCostDetails");
                if(cvFormulatedCostDetails != null && !cvFormulatedCostDetails.isEmpty()){
                    Equals eqBudgetPeriod = new Equals("budgetPeriod",budgetPeriod);
                    Equals eqLineItemNumber = new Equals("lineItemNumber",lineItemNumber);
                    And andLineItem = new And(eqBudgetPeriod,eqLineItemNumber);
                    CoeusVector cvLineItemFormulatedCost = cvFormulatedCostDetails.filter(andLineItem);
                    if(cvLineItemFormulatedCost != null && !cvLineItemFormulatedCost.isEmpty()){
                        isLineItemFormulated = true;
                    }else{
                        hasFormulatedDetails = false;
                    }
                }
                if(!hasFormulatedDetails){
                    CoeusFunctions coeusFunctions = new CoeusFunctions();
                    String enableFormualtedCost = coeusFunctions.getParameterValue(CoeusConstants.ENABLE_FORMULATED_COST_CALC);
                    if(enableFormualtedCost != null && "1".equals(enableFormualtedCost.trim())){
                        String formulatedCostElements = coeusFunctions.getParameterValue(CoeusConstants.FORMULATED_COST_ELEMENTS);
                        String[] formulatedCostElem = null;
                        if(formulatedCostElements != null) {
                            formulatedCostElem = formulatedCostElements.split(",");
                        }
                        if(formulatedCostElem != null && formulatedCostElem.length > 0) {
                            DynaValidatorForm lineItemDetailsForm = getBudgetDetailsForLineItem(request,lineItemNumber,budgetPeriod);
                            String costElement = ((String)lineItemDetailsForm.get("costElement"));
                            for(int index = 0; index < formulatedCostElem.length; index++) {
                                if(costElement.equals(formulatedCostElem[index].trim())) {
                                    isLineItemFormulated = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return isLineItemFormulated;
    }
    
    /**
     * Method to get the budget details for the line item 
     * @param request 
     * @param lineItemNumber 
     * @return lineItemDetailsForm
     */
    private DynaValidatorForm getBudgetDetailsForLineItem(HttpServletRequest request, int lineItemNumber, int budgetPeriod){
        DynaValidatorForm lineItemDetailsForm = null;
        Vector vecBudgetDetails = (Vector)request.getSession().getAttribute("BudgetDetailData");
        if(vecBudgetDetails != null && !vecBudgetDetails.isEmpty()){
            for(Object lineItemDetails : vecBudgetDetails){
                lineItemDetailsForm = (DynaValidatorForm)lineItemDetails;
                int formLineItemNumber = ((Integer)lineItemDetailsForm.get("lineItemNumber")).intValue();
                int formBudgetPeriod = ((Integer)lineItemDetailsForm.get("budgetPeriod")).intValue();
                if(budgetPeriod == formBudgetPeriod && formLineItemNumber == lineItemNumber){
                    break;
                }
            }
        }
        return lineItemDetailsForm;
    }
    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
}
