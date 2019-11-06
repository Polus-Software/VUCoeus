/*
 * BudgetLineItemDetailsAction.java
 *
 * Created on March 31, 2006, 11:09 AM
 */

package edu.wmc.coeuslite.budget.action;

import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.budget.bean.BudgetDetailBean;
import edu.mit.coeus.budget.bean.BudgetDetailCalAmountsBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetPeriodBean;
import edu.mit.coeus.budget.bean.BudgetPersonnelCalAmountsBean;
import edu.mit.coeus.budget.bean.BudgetPersonnelDetailsBean;
import edu.mit.coeus.budget.bean.ProposalRatesBean;
import edu.mit.coeus.budget.bean.ValidCERateTypesBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.RateClassTypeConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.GreaterThan;
import edu.mit.coeus.utils.query.LesserThan;
import edu.mit.coeus.utils.query.Or;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.DateUtils;
import java.util.*;
import java.sql.Timestamp;
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
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author  chandrashekara
 */
public class BudgetLineItemDetailsAction extends BudgetBaseAction{
//    private ActionForward actionForward;
    private static final String EMPTY_STRING = "";
    private static final String SAVE_AND_APPLY_TO_LATER_PERIODS = "/applyToLaterPeriods";
    private static final String SAVE_AND_APPLY_TO_CURRENT_PERIOD = "/applyToCurrentPeriods";
    private static final String TOTAL_COST_EXCEEDING = "totalCostExceed";
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
//    private HttpServletRequest request;
//    private HttpSession session;
    private static final char PERSONNEL = 'P';
//    private Timestamp dbTimestamp;
//    private ActionMessages actionMessages;
     
    /** Creates a new instance of BudgetLineItemDetailsAction */
    public BudgetLineItemDetailsAction() {
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
        
        DynaValidatorForm dynaForm  = (DynaValidatorForm)actionForm;
        ActionForward actionForward = performLineItemDetailsActions(actionMapping,dynaForm,request);
        return actionForward;
    }
    /**
     * perform LineItem details action
     * @param actionMapping
     * @param dynaForm
     * @throws Exception
     * @return
     */    
   private ActionForward performLineItemDetailsActions(ActionMapping actionMapping,
    DynaValidatorForm dynaForm, HttpServletRequest request) throws Exception{
        String navigator = EMPTY_STRING;
        ActionForward actionForward = null;
        if(actionMapping.getPath().equals(SAVE_AND_APPLY_TO_LATER_PERIODS ) ) {
            navigator = saveAndApplyToLaterPeriods(dynaForm, request);
            actionForward = actionMapping.findForward(navigator);
        }else if(actionMapping.getPath().equals(SAVE_AND_APPLY_TO_CURRENT_PERIOD)){
            navigator = saveAndApplyToCurrentPeriod(dynaForm, request);
            actionForward = actionMapping.findForward(navigator);
        }
        return actionForward;
    }
    
    /**
     * Perform this action and saveAndApplyToCurrentPeriod action is Performed
     * @param dynaForm
     * @throws Exception
     * @return
     */
    private String saveAndApplyToCurrentPeriod(DynaValidatorForm dynaForm,
        HttpServletRequest request) throws Exception{
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        BeanUtilsBean copyBean = new BeanUtilsBean();
        String budPeriod = request.getParameter("budgetPeriod");
        Vector vecDynaPeriodData = (Vector)session.getAttribute("BudgetPeriodData");
        //Code commented for Budget Bug fix for case# 27438
        CoeusVector vecPeriodData = preparePeriodData(vecDynaPeriodData);
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        //Added for removing instance variable -case # 2960 - start
        String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        //Added for removing instance variable - case # 2960- end                  
        BudgetDetailBean budgetDetailBean = new BudgetDetailBean();
        //Currency format conversion - Start 
        java.text.NumberFormat numberFormat = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US);
        String strLineItemCost = request.getParameter("lineItemCost");
        if(strLineItemCost !=null && !strLineItemCost.equals(EMPTY_STRING)){
            double lineItemCost = 0;
            try{
                lineItemCost = formatStringToDouble(strLineItemCost);
            }catch(NumberFormatException ne){
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("error.lineItemDetails.lineItemCost" , new ActionMessage(
            "error.lineItemDetails.lineItemCost" ) );
            saveMessages(request, actionMessages);
            navigator="success";
            return navigator;
            }
            strLineItemCost = numberFormat.format(lineItemCost);
            dynaForm.set("lineItemCost", strLineItemCost);
        }
        String strCostSharingAmount = request.getParameter("costSharingAmount");
        if(strCostSharingAmount !=null && !strCostSharingAmount.equals(EMPTY_STRING)){
            double costSharingAmount = 0;
            try{
                costSharingAmount = formatStringToDouble(strCostSharingAmount);
            }catch(NumberFormatException ne){
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("error.lineItemDetails.costSharingAmount" , new ActionMessage(
            "error.lineItemDetails.costSharingAmount" ) );
            saveMessages(request, actionMessages);
            navigator="success";
            return navigator;
            }
            strCostSharingAmount = numberFormat.format(costSharingAmount);
            dynaForm.set("costSharingAmount", strCostSharingAmount);
        }
        String strUnderRecoveryAmount = request.getParameter("underRecoveryAmount");
        if(strUnderRecoveryAmount !=null && !strUnderRecoveryAmount.equals(EMPTY_STRING)){
            double underRecoveryAmount = 0 ;
            try{
                underRecoveryAmount = formatStringToDouble(strUnderRecoveryAmount);
            }catch(NumberFormatException ne){
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("error.lineItemDetails.underRecovery" , new ActionMessage(
            "error.lineItemDetails.underRecovery" ) );
            saveMessages(request, actionMessages);
            navigator="success";
            return navigator;
            }
            strUnderRecoveryAmount = numberFormat.format(underRecoveryAmount);
            dynaForm.set("underRecoveryAmount", strUnderRecoveryAmount);
        }
        //Added for Case #3132 - start
        //Changing quantity field from integer to float
        String strQuantity = request.getParameter("quantity");
        if(strQuantity !=null && !strQuantity.equals(EMPTY_STRING)){
            double quantity = 0 ;
            try{
                quantity = formatStringToDouble(strQuantity);
            }catch(NumberFormatException ne){
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("error.lineItemDetails.quantity" , new ActionMessage(
            "error.lineItemDetails.quantity" ) );
            saveMessages(request, actionMessages);
            navigator="success";
            return navigator;
            }
            strQuantity = numberFormat.format(quantity);
            dynaForm.set("quantity", strQuantity);
        }
        //Added for Case #3132 - end

        //Currency format conversion - End
        CoeusVector cvCalAmts = null;
        boolean isValidDate = validationForDate(dynaForm, request);
        if(!isValidDate){
            request.setAttribute("budgetDetails",dynaForm);
            request.setAttribute("period", new Integer(budPeriod));
            cvCalAmts = getCalAmtsData(dynaForm, request);
            cvCalAmts.sort("rateClassCode",true);
            cvCalAmts = prepareDynaBean(cvCalAmts, dynaForm);
            session.setAttribute("budgetDetailCalAmts", cvCalAmts);
          //  request.setAttribute("budgetDetailCalAmts", cvCalAmts);
            navigator="success";
            return navigator;
        }
        DateUtils dateUtils = new DateUtils();
        String tempLineItemEndDate = ((String)dynaForm.get("tempLineItemEndDate")).trim();
        String tempLineItemStartDate = ((String)dynaForm.get("tempLineItemStartDate")).trim();
        
        tempLineItemEndDate = dateUtils.formatDate(tempLineItemEndDate,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
        tempLineItemStartDate = dateUtils.formatDate(tempLineItemStartDate,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
        java.sql.Date tempStartDate = dateUtils.getSQLDate(tempLineItemStartDate);
        java.sql.Date tempEndDate = dateUtils.getSQLDate(tempLineItemEndDate);

        Timestamp dbTimestamp = (Timestamp)prepareTimeStamp();
        dynaForm.set("updateTimestamp",dbTimestamp.toString());
        dynaForm.set("lineItemStartDate",tempStartDate);
        dynaForm.set("lineItemEndDate",tempEndDate);
        String lineItemDesc = (String)dynaForm.get("lineItemDescription");
        if(lineItemDesc != null){
            lineItemDesc = lineItemDesc.trim();
            dynaForm.set("lineItemDescription",lineItemDesc);
        }
        copyBean.copyProperties(budgetDetailBean,dynaForm);
        CoeusVector cvDetails = getDetailsData(dynaForm, session);
        cvCalAmts = getCalAmtsData(dynaForm, request);
        Hashtable htBudgetData = initializeBudgetCalculator(budgetInfoBean);
        //Code commented for Budget Bug fix for case# 2738
        htBudgetData.remove(BudgetPeriodBean.class);
        cvDetails = setValueForDetails(vecPeriodData, cvDetails);
        //Code added for Budget Bug fix for case# 2742,2738 - ends
        htBudgetData.remove(BudgetDetailBean.class);
        htBudgetData.remove(BudgetDetailCalAmountsBean.class);
        //Code commented for Budget Bug fix for case# 2738
        htBudgetData.put(BudgetPeriodBean.class, vecPeriodData);
        htBudgetData.put(BudgetDetailBean.class, cvDetails);
        htBudgetData.put(BudgetDetailCalAmountsBean.class, cvCalAmts);
        Hashtable htCalculatedData = calculatedBudgetPeriod(htBudgetData,budgetDetailBean.getBudgetPeriod(), budgetInfoBean);
        //To check total cost not exceeding $9,999,999,999.999
        boolean isTotalCostExceed = checkTotalCost(htCalculatedData);
        if(isTotalCostExceed){
            request.setAttribute("budgetDetails",dynaForm);
            request.setAttribute("period", new Integer(budPeriod));
            cvCalAmts = getCalAmtsData(dynaForm, request);
            cvCalAmts.sort("rateClassCode",true);
            cvCalAmts = prepareDynaBean(cvCalAmts, dynaForm);
            session.setAttribute("budgetDetailCalAmts", cvCalAmts);
           // request.setAttribute("budgetDetailCalAmts", cvCalAmts);
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("budget_common_exceptionCode.1011" , new ActionMessage(
            "budget_common_exceptionCode.1011" ) );
            saveMessages(request, actionMessages);
            navigator="success";
            return navigator;
        }
        //Modified for removing instance variable -case # 2960 - start
//        saveBudget(htCalculatedData, request);
        saveBudget(htCalculatedData, request, queryKey);
        //Modified for removing instance variable -case # 2960 - end
        //refreshBuddgetLineItemData(budgetInfoBean, dynaForm);
        dynaForm.set("popUp","close");
        request.setAttribute("budgetDetails",dynaForm);
        request.setAttribute("period", new Integer(budPeriod));
        session.removeAttribute("budgetDetailCalAmts");
        session.removeAttribute("lastPeriod");
        session.removeAttribute("firstPeriod");
        navigator="success";
        return navigator;
    }
    /**
     * Perform this action and saveAndApplyToLaterPeriods action is Performed
     * @param dynaForm
     
     * @throws Exception
     * @return
     */
    private String saveAndApplyToLaterPeriods(DynaValidatorForm dynaForm,
        HttpServletRequest request) throws Exception{
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        String budPeriod = request.getParameter("budgetPeriod");
        BeanUtilsBean copyBean = new BeanUtilsBean();
        BudgetDetailBean budgetDetailBean = new BudgetDetailBean();
        boolean isValidDate = validationForDate(dynaForm, request);
        //COEUSQA-1693 - Cost Sharing Submission - start
        String paramCostSharingFlag = request.getParameter("submitCostSharingFlag");
              
        if(paramCostSharingFlag !=null && "Y".equalsIgnoreCase(paramCostSharingFlag)){
            dynaForm.set("submitCostSharingFlag","Y");
        } else{
            dynaForm.set("submitCostSharingFlag","N");
            //dynaValidatorForm.set("tempSubmitCostSharingFlag", new Boolean(false));
        }
       //COEUSQA-1693 - Cost Sharing Submission - end
        //Currency format conversion - Start 
        java.text.NumberFormat numberFormat = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US);
        String strLineItemCost = request.getParameter("lineItemCost");
        if(strLineItemCost !=null && !strLineItemCost.equals(EMPTY_STRING)){
            double lineItemCost = 0;
            try{
                lineItemCost = formatStringToDouble(strLineItemCost);
            }catch(NumberFormatException ne){
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("error.lineItemDetails.lineItemCost" , new ActionMessage(
            "error.lineItemDetails.lineItemCost" ) );
            saveMessages(request, actionMessages);
            navigator="success";
            return navigator;
            }
            strLineItemCost = numberFormat.format(lineItemCost);
            dynaForm.set("lineItemCost", strLineItemCost);
        }
        String strCostSharingAmount = request.getParameter("costSharingAmount");
        if(strCostSharingAmount !=null && !strCostSharingAmount.equals(EMPTY_STRING)){
            double costSharingAmount = 0;
            try{
                costSharingAmount = formatStringToDouble(strCostSharingAmount);
            }catch(NumberFormatException ne){
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("error.lineItemDetails.costSharingAmount" , new ActionMessage(
            "error.lineItemDetails.costSharingAmount" ) );
            saveMessages(request, actionMessages);
            navigator="success";
            return navigator;
            }
            strCostSharingAmount = numberFormat.format(costSharingAmount);
            dynaForm.set("costSharingAmount", strCostSharingAmount);
        }
        String strUnderRecoveryAmount = request.getParameter("underRecoveryAmount");
        if(strUnderRecoveryAmount !=null && !strUnderRecoveryAmount.equals(EMPTY_STRING)){
            double underRecoveryAmount = 0 ;
            try{
                underRecoveryAmount = formatStringToDouble(strUnderRecoveryAmount);
            }catch(NumberFormatException ne){
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("error.lineItemDetails.underRecovery" , new ActionMessage(
            "error.lineItemDetails.underRecovery" ) );
            saveMessages(request, actionMessages);
            navigator="success";
            return navigator;
            }
            strUnderRecoveryAmount = numberFormat.format(underRecoveryAmount);
            dynaForm.set("underRecoveryAmount", strUnderRecoveryAmount);
        }
        //Added for Case #3132 - start
        //Changing quantity field from integer to float
        String strQuantity = request.getParameter("quantity");
        if(strQuantity !=null && !strQuantity.equals(EMPTY_STRING)){
            double quantity = 0 ;
            try{
                quantity = formatStringToDouble(strQuantity);
            }catch(NumberFormatException ne){
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("error.lineItemDetails.quantity" , new ActionMessage(
            "error.lineItemDetails.quantity" ) );
            saveMessages(request, actionMessages);
            navigator="success";
            return navigator;
            }
            strQuantity = numberFormat.format(quantity);
            dynaForm.set("quantity", strQuantity);
        }
        //Added for Case #3132 - end

        //Currency format conversion - End
        if(!isValidDate){
            validateForApplyToLaterPeriods(budPeriod, request);
            request.setAttribute("budgetDetails",dynaForm);
            request.setAttribute("period", new Integer(budPeriod));
            CoeusVector cvCalAmts = getCalAmtsData(dynaForm, request);
            cvCalAmts.sort("rateClassCode",true);
            cvCalAmts = prepareDynaBean(cvCalAmts, dynaForm);
            session.setAttribute("budgetDetailCalAmts", cvCalAmts);
            //request.setAttribute("budgetDetailCalAmts", cvCalAmts);
            navigator="success";
            return navigator;
        }
        DateUtils dateUtils = new DateUtils();
        String tempLineItemEndDate = ((String)dynaForm.get("tempLineItemEndDate")).trim();
        String tempLineItemStartDate = ((String)dynaForm.get("tempLineItemStartDate")).trim();
        
        tempLineItemEndDate = dateUtils.formatDate(tempLineItemEndDate,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
        tempLineItemStartDate = dateUtils.formatDate(tempLineItemStartDate,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
        java.sql.Date tempStartDate = dateUtils.getSQLDate(tempLineItemStartDate);
        java.sql.Date tempEndDate = dateUtils.getSQLDate(tempLineItemEndDate);
      
        dynaForm.set("lineItemStartDate",tempStartDate);
        dynaForm.set("lineItemEndDate",tempEndDate);
        String lineItemDesc = (String)dynaForm.get("lineItemDescription");
        if(lineItemDesc != null){
            lineItemDesc = lineItemDesc.trim();
            dynaForm.set("lineItemDescription",lineItemDesc);
        }
        copyBean.copyProperties(budgetDetailBean,dynaForm);
        Vector vecDynaPeriodData = (Vector)session.getAttribute("BudgetPeriodData");
        CoeusVector vecPeriodData = preparePeriodData(vecDynaPeriodData);
        boolean value = applyToLaterPeriods(budgetDetailBean,vecPeriodData,dynaForm,request);
        if(value){
            navigator = calulateAndSaveBudget(request);
            if(navigator.equals(TOTAL_COST_EXCEEDING)){
                ActionMessages actionMessages = new ActionMessages();
                actionMessages.add("budget_common_exceptionCode.1011" , new ActionMessage(
                "budget_common_exceptionCode.1011" ) );
                saveMessages(request, actionMessages);
                request.setAttribute("budgetDetails",dynaForm);
                request.setAttribute("period", new Integer(budPeriod));
                CoeusVector cvCalAmts = getCalAmtsData(dynaForm, request);
                cvCalAmts.sort("rateClassCode",true);
                cvCalAmts = prepareDynaBean(cvCalAmts, dynaForm);
                session.setAttribute("budgetDetailCalAmts", cvCalAmts);
                //request.setAttribute("budgetDetailCalAmts", cvCalAmts);
                navigator="success";
                return navigator;
            }
            dynaForm.set("popUp","close");
            request.setAttribute("budgetDetails",dynaForm);
            request.setAttribute("period", new Integer(budPeriod));
        }
        session.removeAttribute("budgetDetailCalAmts");
        session.removeAttribute("lastPeriod");
        session.removeAttribute("firstPeriod");
        return navigator;
    }
    // calculate and save the budget
    /**
     * Method to calculte and save the budget
     * @param budgetDetailBean BudgetDetailBean
     * @param actionMapping instance of ActionMapping
     * @param dynaForm instance of DynaValidatorForm
     * @throws Exception if exception occur
     * @return String for opening the appropriate page
     */    
    private String calulateAndSaveBudget(HttpServletRequest request) throws Exception{
        String navigator = EMPTY_STRING;
        HttpSession session  = request.getSession();
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        //Added for removing instance variable -case # 2960 - start
        QueryEngine queryEngine = QueryEngine.getInstance();
        String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        //Added for removing instance variable - case # 2960- end                  
        Hashtable htBudgetData = queryEngine.getDataCollection(queryKey);
        Hashtable htCalculatedData = null;
        htCalculatedData = calculateAllPeriods(htBudgetData, budgetInfoBean);
        boolean isTotalCostExceed = checkTotalCost(htCalculatedData);
        if(isTotalCostExceed){
            navigator = TOTAL_COST_EXCEEDING;
            return navigator;
        }
        //Modified for removing instance variable -case # 2960 - start
//        navigator  = saveBudget(htCalculatedData, request);        
        navigator  = saveBudget(htCalculatedData, request, queryKey);
        //Modified for removing instance variable -case # 2960 - end
        //refreshBuddgetLineItemData(budgetInfoBean,dynaForm);
        return navigator;
    }
    
    /**
     * This will save the budget for the particular line item
     * Now it is using TxnBean to update the data to the database. Later we have
     * to change this. We have to update these data through Transaction.XML
     * way
     * @param htBudgetData
     * @throws Exception
     * @return
     */
     private String saveBudget(Hashtable htBudgetData, HttpServletRequest request,
            String queryKey) throws Exception{
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        String proposalNumber = (String) session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
        edu.mit.coeus.bean.UserInfoBean userInfoBean = (edu.mit.coeus.bean.UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        edu.mit.coeus.budget.bean.BudgetUpdateTxnBean budgetUpdateTxnBean = new edu.mit.coeus.budget.bean.BudgetUpdateTxnBean(userId);
        boolean isUpdated = budgetUpdateTxnBean.addUpdDeleteBudget(htBudgetData);  
        if(isUpdated){
            navigator = "success";
            // Update the proposal hierarchy sync flag
            updateProposalSyncFlags(request, proposalNumber);            
            removeQueryEngineCollection(queryKey);
        }
        return navigator;
    }
    /**
     * Calculate and prepare for applyLatePeriods action
     * @param budgetDetailBean
     * @param vecPeriodData
     * @param dynaForm
     * @throws Exception
     * @return
     */
    private boolean  applyToLaterPeriods(BudgetDetailBean budgetDetailBean, CoeusVector vecPeriodData,
        DynaValidatorForm dynaForm, HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        //Added for removing instance variable -case # 2960 - start
        QueryEngine queryEngine = QueryEngine.getInstance();
        String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        //Added for removing instance variable - case # 2960- end                  
        CoeusVector cvBudgetDetailsCalAmts = prepareBudgetDetailsCalAmtsData(dynaForm, request);
        CoeusVector cvBudgetDetails = prepareBudgetDetailsData(dynaForm, session);
        
        Hashtable htBudgetData = initializeBudgetCalculator(budgetInfoBean);
        htBudgetData.remove(BudgetPeriodBean.class);
        htBudgetData.remove(BudgetDetailBean.class);
        htBudgetData.remove(BudgetDetailCalAmountsBean.class);
        
        htBudgetData.put(BudgetPeriodBean.class,vecPeriodData);
        htBudgetData.put(BudgetDetailBean.class,cvBudgetDetails == null? new CoeusVector():cvBudgetDetails);
        htBudgetData.put(BudgetDetailCalAmountsBean.class,cvBudgetDetailsCalAmts == null? new CoeusVector():cvBudgetDetailsCalAmts );
        queryEngine.addDataCollection(queryKey,htBudgetData);
        
        int currentPeriod = budgetDetailBean.getBudgetPeriod();
        int lineItemNumber = budgetDetailBean.getLineItemNumber();
        String costElement = budgetDetailBean.getCostElement();
        CoeusVector vecBudgetDetails, vecBudgetPersonnelDetails;
        BudgetDetailBean currentBudgetDetailBean = null, nextBudgetDetailBean, newBudgetDetailBean;
        BudgetPeriodBean nextBudgetPeriodBean;
        
        int maxLINum = 0;
        int maxSeqNum = 0;
        int totalPeriods = vecPeriodData.size();
        //get the max line item number > current period
        GreaterThan gtPeriod = new GreaterThan("budgetPeriod", new Integer(currentPeriod));
        CoeusVector vecMaxLI = queryEngine.getActiveData(queryKey, BudgetDetailBean.class, gtPeriod);
        if(vecMaxLI != null && vecMaxLI.size() > 0) {
            vecMaxLI.sort("lineItemNumber", false);
            maxLINum = ((BudgetDetailBean)vecMaxLI.get(0)).getLineItemNumber();
        }
        
        maxLINum = maxLINum + 1;
        
        for(int period = currentPeriod + 1; period <= totalPeriods; period++) {
            double lineItemCost = 0;
            //check if selected line item is present for this period
            Equals eqPeriod = new Equals("budgetPeriod", new Integer(period));
            Equals eqLINumber = new Equals("basedOnLineItem", new Integer(lineItemNumber));
            Equals eqCostElement = new Equals("costElement", costElement);
//            And eqPeriodAndEqLINumber = new And(eqPeriod, eqLINumber);
            And eqPeriodAndEqLINumber = new And(eqPeriod, eqCostElement);
            vecBudgetDetails = queryEngine.getActiveData(queryKey, BudgetDetailBean.class, eqPeriodAndEqLINumber);
            
            CoeusVector vecCurrentPeriodDetail = queryEngine.getActiveData(queryKey, BudgetDetailBean.class,
            new And(
            new Equals("budgetPeriod", new Integer(period - 1)),
            new Equals("lineItemNumber", new Integer(lineItemNumber))
            ));
            if(vecCurrentPeriodDetail != null){
                if(vecCurrentPeriodDetail.size() > 0){
                    currentBudgetDetailBean = (BudgetDetailBean)vecCurrentPeriodDetail.get(0);
                    lineItemCost = currentBudgetDetailBean.getLineItemCost();
                }
            }
            //if next period line item is based on same line item then continue with next period.
            if(vecBudgetDetails != null && vecBudgetDetails.size() > 0) {
                nextBudgetDetailBean = (BudgetDetailBean)vecBudgetDetails.get(0);
                Timestamp detailTimestamp = nextBudgetDetailBean.getUpdateTimestamp();
                vecBudgetPersonnelDetails = queryEngine.getActiveData(queryKey, BudgetPersonnelDetailsBean.class,
                new And(
                new Equals("budgetPeriod", new Integer(period)),
                new Equals("lineItemNumber", new Integer(lineItemNumber))
                ));
                if(nextBudgetDetailBean.getCategoryType() == PERSONNEL ||
                (vecBudgetPersonnelDetails != null && vecBudgetPersonnelDetails.size() > 0)) {
                 return false;
                }
                
                //Update line item cost after applying inflation.
                if(currentBudgetDetailBean.isApplyInRateFlag()){
                    //Modified for removing instance variable -case # 2960 - start
//                    lineItemCost = calculateInflation(currentBudgetDetailBean, nextBudgetDetailBean.getLineItemStartDate());
                    lineItemCost = calculateInflation(currentBudgetDetailBean, 
                        nextBudgetDetailBean.getLineItemStartDate(), queryKey);
                    //Modified for removing instance variable -case # 2960 - end
                }
                BudgetDetailBean copyBudgetDetailBean = (BudgetDetailBean)ObjectCloner.deepCopy(currentBudgetDetailBean);
                copyBudgetDetailBean.setBudgetPeriod(nextBudgetDetailBean.getBudgetPeriod());
                //Commented for Case 3245- Commented to get the new period lineItemNumber  - Start
                // copyBudgetDetailBean.setLineItemNumber(nextBudgetDetailBean.getLineItemNumber());
                //Added for Case 3245 - Set the currently selected period lineItemNumber
                copyBudgetDetailBean.setLineItemNumber(currentBudgetDetailBean.getLineItemNumber());
                //Commented for Case 3245  - End
                copyBudgetDetailBean.setBasedOnLineItem(currentBudgetDetailBean.getLineItemNumber());
                //Commented for Case 3245- Commented to get the new period lineItemSequence  - Start
                // copyBudgetDetailBean.setLineItemSequence(nextBudgetDetailBean.getLineItemSequence());
                //Added for Case 3245- Set the currently selected period lineItemSequence
                copyBudgetDetailBean.setLineItemSequence(currentBudgetDetailBean.getLineItemSequence());
                // Case 3245 - End
                copyBudgetDetailBean.setLineItemStartDate(nextBudgetDetailBean.getLineItemStartDate());
                copyBudgetDetailBean.setLineItemEndDate(nextBudgetDetailBean.getLineItemEndDate());
                
                //Check for Cal Amts
                //Code commented for Budget Bug fix for case# 2742
//                if(! copyBudgetDetailBean.getCostElement().equals(nextBudgetDetailBean.getCostElement())) {
                    //Delete old Cal Amts.
                    Equals eqBgtPeriod = new Equals("budgetPeriod", new Integer(nextBudgetDetailBean.getBudgetPeriod()));
                    //Commented for Case 3245- Commented to get the new period lineItemNumber  - Start
                    // Equals eqLINum = new Equals("lineItemNumber", new Integer(nextBudgetDetailBean.getLineItemNumber()));
                    //Added for Case 3245 - Set the currently selected period lineItemNumber
                    Equals eqLINum = new Equals("lineItemNumber", new Integer(currentBudgetDetailBean.getLineItemNumber()));
                    And eqPeriodAndEqLINum = new And(eqBgtPeriod, eqLINum);
                    // Case 3245 - End
                                      
                    CoeusVector vecCalAmts = queryEngine.getActiveData(queryKey, BudgetDetailCalAmountsBean.class, eqPeriodAndEqLINum);
                    if(vecCalAmts != null && vecCalAmts.size() > 0) {
                        BudgetDetailCalAmountsBean budgetDetailCalAmountsBean;
                        int size = vecCalAmts.size();
                        for(int index = 0; index < size; index++) {
                            budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)vecCalAmts.get(index);
                            budgetDetailCalAmountsBean.setAcType(TypeConstants.DELETE_RECORD);
                            queryEngine.delete(queryKey, budgetDetailCalAmountsBean);
                        }//End For - Delete Budget Detail Cal Amts.
                    }//End IF - vecCalAmts != null
                    //Add new Cal Amts
                    eqBgtPeriod = new Equals("budgetPeriod", new Integer(currentBudgetDetailBean.getBudgetPeriod()));
                    eqLINum = new Equals("lineItemNumber", new Integer(currentBudgetDetailBean.getLineItemNumber()));
                    eqPeriodAndEqLINum = new And(eqBgtPeriod, eqLINum);
                    CoeusVector vecBudgetDetailCalAmts = queryEngine.getActiveData(queryKey, BudgetDetailCalAmountsBean.class, eqPeriodAndEqLINum);
                    
                    if(vecBudgetDetailCalAmts != null && vecBudgetDetailCalAmts.size() > 0) {
                        BudgetDetailCalAmountsBean budgetDetailCalAmountsBean;
                        int size = vecBudgetDetailCalAmts.size();
                        for(int index = 0; index < size; index++) {
                            budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)vecBudgetDetailCalAmts.get(index);
                            budgetDetailCalAmountsBean.setBudgetPeriod(nextBudgetDetailBean.getBudgetPeriod());
                            //Commented for Case 3245- Commented to get the new period lineItemNumber  - Start
                            // bdgetDetailCalAmountsBean.setLineItemNumber(nextBudgetDetailBean.getLineItemNumber());
                            //Added for Case 3245 - Set the currently selected period lineItemNumber
                            budgetDetailCalAmountsBean.setLineItemNumber(currentBudgetDetailBean.getLineItemNumber());
                            //Case 3245 -End
                            budgetDetailCalAmountsBean.setAcType(TypeConstants.INSERT_RECORD);
                            queryEngine.insert(queryKey, budgetDetailCalAmountsBean);
                        }//End For - BudgetDetail Cal Amts Inserting
                    }//End IF - vecBudgetDetailCalAmts !=null
                  //Code commented for Budget Bug fix for case# 2742 
//                }
                
                nextBudgetDetailBean = copyBudgetDetailBean;
                if(detailTimestamp!= null){
                    nextBudgetDetailBean.setUpdateTimestamp(detailTimestamp);
                }
                nextBudgetDetailBean.setLineItemCost(lineItemCost);
                queryEngine.update(queryKey, nextBudgetDetailBean);
                //Commented for Case 3245- Commented to get the new period lineItemNumber  - Start
                // lineItemNumber = nextBudgetDetailBean.getLineItemNumber();
                //Added for Case 3245 - Set the currently selected period lineItemNumber
                lineItemNumber = currentBudgetDetailBean.getLineItemNumber();
                //Case 3245 -End
                continue ;
            }//End if - check for next period line item is based on same line item
            
            //Get Max Squence Number if Adding
            Equals eqperiod = new Equals("budgetPeriod", new Integer(period));
            CoeusVector vecMaxSeqNum = queryEngine.getActiveData(queryKey, BudgetDetailBean.class, eqperiod);
            if(vecMaxSeqNum != null && vecMaxSeqNum.size() > 0) {
                vecMaxSeqNum.sort("lineItemSequence", false);
                maxSeqNum =  ((BudgetDetailBean)vecMaxSeqNum.get(0)).getLineItemSequence() + 1;
            }else {
                maxSeqNum = 1; //First Entry
            }
            nextBudgetPeriodBean = (BudgetPeriodBean)vecPeriodData.get(period - 1);
            
            newBudgetDetailBean = (BudgetDetailBean)ObjectCloner.deepCopy(currentBudgetDetailBean);
            newBudgetDetailBean.setBudgetPeriod(period);
            newBudgetDetailBean.setLineItemSequence(maxSeqNum);
            newBudgetDetailBean.setLineItemNumber(maxLINum);
            
            lineItemNumber = maxLINum;
            newBudgetDetailBean.setBasedOnLineItem(currentBudgetDetailBean.getLineItemNumber());
            
            
            newBudgetDetailBean.setLineItemStartDate(nextBudgetPeriodBean.getStartDate());
            newBudgetDetailBean.setLineItemEndDate(nextBudgetPeriodBean.getEndDate());
            
            newBudgetDetailBean.setAcType(TypeConstants.INSERT_RECORD);
            
            Equals eqBudgetPeriod = new Equals("budgetPeriod", new Integer(period - 1));
            Equals eqLineItemNo = new Equals("lineItemNumber", new Integer(currentBudgetDetailBean.getLineItemNumber()));
            And eqBudgetPeriodAndEqLineItemNo = new And(eqBudgetPeriod, eqLineItemNo);
            CoeusVector vecBudgetPersonnelDetailsBean = queryEngine.getActiveData(queryKey, BudgetPersonnelDetailsBean.class, eqBudgetPeriodAndEqLineItemNo);
            
            /**
             *if line item contains personnel line items.
             *then line item cost will be set to 0.
             *correct cost will be set during calculation.
             */
            if(vecBudgetPersonnelDetailsBean != null && vecBudgetPersonnelDetailsBean.size() > 0) {
                lineItemCost = 0;
            }
            //Apply inflation only if line item does not contain personnel line item
            if (newBudgetDetailBean.isApplyInRateFlag() && (vecBudgetPersonnelDetailsBean == null || vecBudgetPersonnelDetailsBean.size() == 0)) {
                //Modified for removing instance variable -case # 2960 - start
//                lineItemCost = calculateInflation(currentBudgetDetailBean, newBudgetDetailBean.getLineItemStartDate());
                lineItemCost = calculateInflation(currentBudgetDetailBean, 
                    newBudgetDetailBean.getLineItemStartDate(), queryKey);
                //Modified for removing instance variable -case # 2960 - end
            }//Apply Inflation check ends here
            newBudgetDetailBean.setLineItemCost(lineItemCost);
            queryEngine.insert(queryKey, newBudgetDetailBean);
            
            //Copy Budget Detail Cal Amts Beans
            CoeusVector vecCalAmts = queryEngine.getActiveData(queryKey, BudgetDetailCalAmountsBean.class, eqBudgetPeriodAndEqLineItemNo);
            if(vecCalAmts == null){
                vecCalAmts = new CoeusVector();
            }
            
            BudgetDetailCalAmountsBean newBudgetDetailCalAmountsBean = null;
            
            for(int calAmtsIndex = 0; calAmtsIndex < vecCalAmts.size(); calAmtsIndex++) {
                newBudgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)vecCalAmts.get(calAmtsIndex);
                newBudgetDetailCalAmountsBean.setBudgetPeriod(nextBudgetPeriodBean.getBudgetPeriod());
                newBudgetDetailCalAmountsBean.setLineItemNumber(newBudgetDetailBean.getLineItemNumber());
                newBudgetDetailCalAmountsBean.setAcType(TypeConstants.INSERT_RECORD);
                queryEngine.insert(queryKey, newBudgetDetailCalAmountsBean);
            }//End For Copy Budget Detail Cal Amts Beans
            
            //Copy Personnel Detail Beans
            if(vecBudgetPersonnelDetailsBean == null){
                vecBudgetPersonnelDetailsBean = new CoeusVector();
            }
            BudgetPersonnelDetailsBean newBudgetPersonnelDetailsBean = null;
            for(int personelIndex = 0; personelIndex < vecBudgetPersonnelDetailsBean.size(); personelIndex++) {
                newBudgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean)vecBudgetPersonnelDetailsBean.get(personelIndex);
                newBudgetPersonnelDetailsBean.setBudgetPeriod(nextBudgetPeriodBean.getBudgetPeriod());
                newBudgetPersonnelDetailsBean.setLineItemNumber(newBudgetDetailBean.getLineItemNumber());
                Date pliStartDate = newBudgetPersonnelDetailsBean.getStartDate();
                Calendar calendar= Calendar.getInstance();
                calendar.setTime(pliStartDate);
                calendar.add(Calendar.YEAR, 1);
                pliStartDate = calendar.getTime();
                
                //since start date for line item will be same as start date of period.
                //while generating periods. we can take period start date.
                if(nextBudgetPeriodBean.getStartDate().compareTo(pliStartDate) <= 0) {
                    newBudgetPersonnelDetailsBean.setStartDate(new java.sql.Date(pliStartDate.getTime()));
                    //set End Date
                    Date pliEndDate = newBudgetPersonnelDetailsBean.getEndDate();
                    calendar.setTime(pliEndDate);
                    calendar.add(Calendar.YEAR, 1);
                    pliEndDate = calendar.getTime();
                    if(nextBudgetPeriodBean.getEndDate().compareTo(pliEndDate) < 0) {
                        pliEndDate = nextBudgetPeriodBean.getEndDate();
                    }
                    newBudgetPersonnelDetailsBean.setEndDate(new java.sql.Date(pliEndDate.getTime()));
                    
                    newBudgetPersonnelDetailsBean.setAcType(TypeConstants.INSERT_RECORD);
                    queryEngine.insert(queryKey, newBudgetPersonnelDetailsBean);
                    
                    //Copy Budget Personned Details Cal Amts
                    And conditionAndLINo = new And(eqBudgetPeriodAndEqLineItemNo,
                    new Equals("personNumber", new Integer(newBudgetPersonnelDetailsBean.getPersonNumber())));
                    
                    CoeusVector vecBudgetPersonnelCalAmountsBean = queryEngine.getActiveData(queryKey, BudgetPersonnelCalAmountsBean.class, conditionAndLINo);
                    if(vecBudgetPersonnelCalAmountsBean == null){
                        vecBudgetPersonnelCalAmountsBean = new CoeusVector();
                    }
                    BudgetPersonnelCalAmountsBean newBudgetPersonnelCalAmountsBean = null;
                    for(int personnelCalAmtsIndex = 0; personnelCalAmtsIndex < vecBudgetPersonnelCalAmountsBean.size(); personnelCalAmtsIndex++) {
                        newBudgetPersonnelCalAmountsBean = (BudgetPersonnelCalAmountsBean)vecBudgetPersonnelCalAmountsBean.get(personnelCalAmtsIndex);
                        newBudgetPersonnelCalAmountsBean.setBudgetPeriod(nextBudgetPeriodBean.getBudgetPeriod());
                        newBudgetPersonnelCalAmountsBean.setLineItemNumber(newBudgetDetailBean.getLineItemNumber());
                        
                        newBudgetPersonnelCalAmountsBean.setAcType(TypeConstants.INSERT_RECORD);
                        queryEngine.insert(queryKey, newBudgetPersonnelCalAmountsBean);
                        
                    }
                    
                }//End For Cmparision
                
            }//End For Copy Personnel Detail Beans
            
        }// End for Periods
        return true;
    }
    
    /**
     *
     * @param budgetDetailBean
     * @param endDate
     * @throws Exception
     * @return
     */    
    private double calculateInflation(BudgetDetailBean budgetDetailBean, 
        Date endDate, String queryKey) throws Exception{
        //CASE 2960: Added queryEngine for to removing instance level variable 
        QueryEngine queryEngine = QueryEngine.getInstance();
        String costElement = budgetDetailBean.getCostElement();
        double lineItemCost = budgetDetailBean.getLineItemCost();
        Date startDate = budgetDetailBean.getLineItemStartDate();
        CoeusVector vecCE = null ;
          
        //Cost Calculation
        Equals eqCe  = new Equals("costElement", costElement);
        Equals eqInflation = new Equals("rateClassType",RateClassTypeConstants.INFLATION);
        And ceAndInflation = new And(eqCe, eqInflation);
        //Check for inflation for the Cost Element.
        //Get ValidCERateTypesBean From Server Side.
        BudgetDataTxnBean budgetDataTxnBean  = new BudgetDataTxnBean();
        CoeusVector vecValidCERateTypes = budgetDataTxnBean.getValidCERateTypes(costElement);
        if(vecValidCERateTypes!=null && vecValidCERateTypes.size() >0){
            vecCE = vecValidCERateTypes.filter(ceAndInflation);
            if(vecCE != null && vecCE.size() > 0) {
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
                 // We should always consider the budget detail line item OnOffCampus flag.
//                boolean defaultIndicator = budgetInfoBean.isDefaultIndicator();
                    boolean validOnOffCampusFlag = false;
//                if(defaultIndicator){
//                    validOnOffCampusFlag = budgetDetailBean.isOnOffCampusFlag();
//                }else{
//                    validOnOffCampusFlag = budgetInfoBean.isOnOffCampusFlag();
//                }
                    validOnOffCampusFlag = budgetDetailBean.isOnOffCampusFlag();
                    Equals eqOnOffCampus = new Equals("onOffCampusFlag", validOnOffCampusFlag);
                    CoeusVector cvProposalRates = vecPropInflationRates.filter(eqOnOffCampus);//queryEngine.executeQuery(queryKey, ProposalRatesBean.class, eqBreakUpSD);
                    ProposalRatesBean proposalRatesBean = (ProposalRatesBean)cvProposalRates.get(0);
                    //Added for COEUSQA-2377 - end
                    
//                    ProposalRatesBean proposalRatesBean = (ProposalRatesBean)vecPropInflationRates.get(0);
                    double applicableRate = proposalRatesBean.getApplicableRate();
                    lineItemCost = lineItemCost * (100 + applicableRate) / 100;
                    
                }//End For vecPropInflationRates != null ...
            }//End If vecCE != null ...
        }
        return lineItemCost;
    }
    /**
     * Prepare the Budget Period data and return CoeusVector containing
     *      BudgetPeriodBean
     * @param vecBudgetPeriod
     * @throws Exception
     * @return
     */
    private CoeusVector preparePeriodData(Vector vecBudgetPeriod) throws Exception{
        BudgetPeriodBean budgetPeriodBean = null;
        BeanUtilsBean copyBean = null;
        CoeusVector cvBudgetPeriods = new CoeusVector();
        if(vecBudgetPeriod!= null){
            for(int index=0; index < vecBudgetPeriod.size(); index++){
                DynaValidatorForm dynaPeriodData = (DynaValidatorForm)vecBudgetPeriod.get(index);
                budgetPeriodBean = new BudgetPeriodBean();
                copyBean = new BeanUtilsBean();
                copyBean.copyProperties(budgetPeriodBean,dynaPeriodData);
                budgetPeriodBean.setAw_BudgetPeriod(budgetPeriodBean.getBudgetPeriod());
                cvBudgetPeriods.addElement(budgetPeriodBean);
            }
        }
        return cvBudgetPeriods;
    }
    
    /**
     * prepare Cal Amts DynaForms Vector
     * @param dynaForm
     * @param budgetInfoBean
     * @throws Exception
     * @return
     */    
    private CoeusVector prepareBudgetDetailsCalAmtsData(DynaValidatorForm dynaForm,
        HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        Vector vecBudgetDetailCalAmts = (Vector)session.getAttribute("BudgetDetailCalAmts");
        BeanUtilsBean copyBean = null;
        BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = null;
        CoeusVector cvBudgetDetailsCalAmts = null;
        int budgetPeriod = ((Integer)dynaForm.get("budgetPeriod")).intValue();
        int lineItemNumber = ((Integer)dynaForm.get("lineItemNumber")).intValue();
        CoeusVector cvFilterData = null;
        CoeusVector cvFilterCalAmtsData = new CoeusVector();
        //Code added for Budget Bug fix for case# 2742
        CoeusVector cvMydata = new CoeusVector();
        if(vecBudgetDetailCalAmts!= null && vecBudgetDetailCalAmts.size() > 0){
            cvBudgetDetailsCalAmts =  new CoeusVector();
            for(int index=0;index<vecBudgetDetailCalAmts.size();index++){
                DynaValidatorForm dynaCalAmts = (DynaValidatorForm)vecBudgetDetailCalAmts.get(index);
                copyBean = new BeanUtilsBean();
                budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
                copyBean.copyProperties(budgetDetailCalAmountsBean,dynaCalAmts);
                cvBudgetDetailsCalAmts.addElement(budgetDetailCalAmountsBean);
                //Code added for Budget Bug fix for case# 2742
                cvMydata.addElement(budgetDetailCalAmountsBean);
            }
            cvFilterData = filterBudgetDetailCalAmts(cvBudgetDetailsCalAmts,budgetPeriod,lineItemNumber,dynaForm);
            if(cvFilterData != null && cvFilterData.size() > 0){
                    // cvCalAmts.sort("rateClasCode",true);
                     CoeusVector cvCalAmounts = sortCalAmts(cvFilterData);
                     cvCalAmounts.sort("rateClassCode", true);
                     CoeusVector cvOnlySelectedData = new CoeusVector();
                     String[] checkedMessages = (String[])request.getParameterValues("applyRateFlag");
                     if(checkedMessages!= null ){
                         for(int ind = 0; ind < checkedMessages.length ; ind++){
                             int mesgIndex = Integer.parseInt(checkedMessages[ind]);
                             for(int in =0 ; in < cvFilterData.size(); in++){
                                 BudgetDetailCalAmountsBean calBean = (BudgetDetailCalAmountsBean)cvCalAmounts.get(in);
                                 if(mesgIndex == in){
                                     calBean.setApplyRateFlag(true);
                                     cvOnlySelectedData.addElement(calBean);
                                 }
                             }
                         }
                     }
                     if(cvOnlySelectedData.size() > 0){
                         for(int selIn = 0 ; selIn < cvOnlySelectedData.size();selIn++){
                             BudgetDetailCalAmountsBean dynaSelForm = (BudgetDetailCalAmountsBean)cvOnlySelectedData.get(selIn);
                             int rateClassCode = dynaSelForm.getRateClassCode();
                             for(int in =0 ; in < cvCalAmounts.size(); in++){
                                 BudgetDetailCalAmountsBean dynaInForm = (BudgetDetailCalAmountsBean)cvCalAmounts.get(in);
                                 int dynaRateClassCode = dynaInForm.getRateClassCode();
                                 if(rateClassCode == dynaRateClassCode){
                                     cvCalAmounts.removeElement(dynaSelForm);
                                     break;
                                 }
                             }
                         }
                     }
                     for(int fIndex = 0; fIndex < cvCalAmounts.size(); fIndex++){
                         BudgetDetailCalAmountsBean dynaFForm = (BudgetDetailCalAmountsBean)cvCalAmounts.get(fIndex);
                         dynaFForm.setApplyRateFlag(false);
                         dynaFForm.setCalculatedCost(0.0);
                     }
                     cvFilterCalAmtsData.removeAllElements();
                     cvFilterCalAmtsData.addAll(cvOnlySelectedData);
                     cvFilterCalAmtsData.addAll(cvCalAmounts);
                 }
        }
        //Code added for Budget Bug fix for case# 2742 - starts
        if(cvMydata!=null && cvFilterCalAmtsData!=null){
            for(int count=0 ; count<cvFilterCalAmtsData.size() ; count++){
                BudgetDetailCalAmountsBean calBean = (BudgetDetailCalAmountsBean) cvFilterCalAmtsData.get(count);
                for(int index=0 ; index<cvMydata.size() ; index++){
                    BudgetDetailCalAmountsBean bean = (BudgetDetailCalAmountsBean) cvMydata.get(index);
                    if(bean.getBudgetPeriod() == calBean.getBudgetPeriod() &&
                    bean.getLineItemNumber() == calBean.getLineItemNumber() &&
                    bean.getRateTypeCode() == calBean.getRateTypeCode() &&
                    bean.getRateClassCode() == calBean.getRateClassCode()){
                        cvMydata.remove(index);
                        cvMydata.add(calBean);
                        break;
                    }
                }
            }
        }        
        return cvMydata;
        //Code added for Budget Bug fix for case# 2742- ends
        //Code commented for Budget Bug fix for case# 2742- ends
//        return cvFilterCalAmtsData;
    }
    /**
     * Filter the BudgetDetailcalAmts fir the budget period and line item number
     * @retruns CoeusVector containing the BudgetDetailCalAmt
     * @param cvBudgetDetailsCalAmts
     * @param budgetPeriod
     * @param lineItemNumber
     * @param calAmtsDynaForm
     * @throws Exception
     * @return
     */
    private CoeusVector filterBudgetDetailCalAmts(CoeusVector cvBudgetDetailsCalAmts,
    int budgetPeriod,int lineItemNumber,DynaValidatorForm calAmtsDynaForm) throws Exception{
        Equals eqPeriod = null;
        Equals eqLineItem = null;
        And eqPeriodAndeqLineItem = null;
        CoeusVector cvFilterCalAmts = null;
        BeanUtilsBean beanUtilsBean = null;
        CoeusVector cvCalAmtsData = null;
        if(cvBudgetDetailsCalAmts!= null && cvBudgetDetailsCalAmts.size() > 0){
            cvFilterCalAmts = new CoeusVector();
            eqPeriod = new Equals("budgetPeriod",new Integer(budgetPeriod));
            eqLineItem = new Equals("lineItemNumber",new Integer(lineItemNumber));
            eqPeriodAndeqLineItem = new And(eqPeriod,eqLineItem);
            cvFilterCalAmts = cvBudgetDetailsCalAmts.filter(eqPeriodAndeqLineItem);
            cvFilterCalAmts.sort("rateClassCode",true);
            if(cvFilterCalAmts!= null && cvFilterCalAmts.size() > 0){
                cvCalAmtsData = new CoeusVector();
                for(int ind = 0; ind < cvFilterCalAmts.size() ; ind++){
                    BudgetDetailCalAmountsBean calAmtsBean = (BudgetDetailCalAmountsBean)cvFilterCalAmts.get(ind);
                    DynaBean calAmtDynaForm = null;
                    calAmtDynaForm = ((DynaBean)calAmtsDynaForm).getDynaClass().newInstance();
                    beanUtilsBean = new BeanUtilsBean();
                    beanUtilsBean.copyProperties(calAmtDynaForm,calAmtsBean);
                    cvCalAmtsData.addElement(calAmtDynaForm);
                }
            }
        }
        return cvCalAmtsData;
    }

    
    

    
    
    /**
     * prepare Budget Detail Data
     * @param dynaForm
     * @param budgetInfoBean
     * @throws Exception
     * @return
     */    
     private CoeusVector prepareBudgetDetailsData(DynaValidatorForm dynaForm,
        HttpSession session) throws Exception{
        
        int budgetPeriod = ((Integer)dynaForm.get("budgetPeriod")).intValue();
        int lineItemNumber = ((Integer)dynaForm.get("lineItemNumber")).intValue();
        Vector vecBudgetDetails = (Vector)session.getAttribute("BudgetDetailData");
        BeanUtilsBean copyBean = null;
        BudgetDetailBean budgetDetailBean = null;
        CoeusVector cvBudgetDetails = null;
        if(vecBudgetDetails!= null && vecBudgetDetails.size() > 0){
            cvBudgetDetails =  new CoeusVector();
            for(int index=0;index<vecBudgetDetails.size();index++){
                DynaValidatorForm dynaDetails = (DynaValidatorForm)vecBudgetDetails.get(index);
                int period = ((Integer)dynaDetails.get("budgetPeriod")).intValue();
                int lineItem = ((Integer)dynaDetails.get("lineItemNumber")).intValue();
                /**If the Changed data is equals to the budget Period and LineItem
                 *number of the data got from the form data then only
                 *set the modified data to the dynaForm else ignore it
                 */
               
                if(budgetPeriod == period && lineItemNumber == lineItem){
                    String lineItemDescription = (String)dynaForm.get("lineItemDescription");
                    java.sql.Date lineItemStartDate = (java.sql.Date)dynaForm.get("lineItemStartDate");
                    java.sql.Date lineItemEndDate = (java.sql.Date)dynaForm.get("lineItemEndDate");
                    double lineItemCost = (formatStringToDouble((String)dynaForm.get("lineItemCost")));                    
                    double costSharingAmount = (formatStringToDouble((String)dynaForm.get("costSharingAmount")));
                    Boolean applyFlag = (Boolean)dynaForm.get("applyInRateFlag");
                    if(applyFlag == null){
                        applyFlag = new Boolean(false);
                    }else{
                        applyFlag = new Boolean(true);
                    }
                    //Code added for Enhancement - starts
                    Boolean onOffFlag = (Boolean)dynaForm.get("onOffCampusFlag");
                    if(onOffFlag == null){
                        onOffFlag = new Boolean(false);
                    }else{
                        onOffFlag = new Boolean(true);
                    }
                    //Code added for enhancement - ends
                    
                    //COEUSQA-1693 - Cost Sharing Submission - start
                    String submitCostSharingFlag = (String)dynaForm.get("submitCostSharingFlag");                    
                    if(submitCostSharingFlag !=null && "Y".equalsIgnoreCase(submitCostSharingFlag)){
                        dynaDetails.set("submitCostSharingFlag","Y");
                    } else{
                        dynaDetails.set("submitCostSharingFlag","N");
                        //dynaValidatorForm.set("tempSubmitCostSharingFlag", new Boolean(false));
                    } 
                    //COEUSQA-1693 - Cost Sharing Submission - end

                    boolean applyInRateFlag = applyFlag.booleanValue();
                    //Modified for Case #3132 - start
                    //Changing quantity field from integer to float
//                    int quantity = ((Integer)dynaForm.get("quantity")).intValue();
                    String strQuantity = (String) dynaForm.get("quantity");
                    strQuantity = strQuantity.replaceAll(",","");
                    double quantity = Double.parseDouble(strQuantity.replaceAll("[$]",""));
                    //Modified for Case #3132 - end
                    String costElement = (String)dynaForm.get("costElement");
                    dynaDetails.set("lineItemDescription",lineItemDescription);
                    dynaDetails.set("lineItemStartDate", lineItemStartDate);
                    dynaDetails.set("lineItemEndDate",lineItemEndDate);
                    dynaDetails.set("lineItemCost", new Double(lineItemCost));
                    dynaDetails.set("costSharingAmount",new Double(costSharingAmount));
                    dynaDetails.set("applyInRateFlag",new Boolean(applyInRateFlag));
                    //Code added for enhancement
                    dynaDetails.set("onOffCampusFlag", onOffFlag);
                    //Modified for Case #3132 - start
                    //Changing quantity field from integer to float
//                    dynaDetails.set("quantity", new Integer(quantity));
                    dynaDetails.set("quantity",new Double(quantity));
                    //Modified for Case #3132 - end
                    dynaDetails.set("costElement",costElement);

                    copyBean = new BeanUtilsBean();
                    budgetDetailBean = new BudgetDetailBean();
                    copyBean.copyProperties(budgetDetailBean,dynaDetails);
                    cvBudgetDetails.addElement(budgetDetailBean);
                }else{
                    copyBean = new BeanUtilsBean();
                    budgetDetailBean = new BudgetDetailBean();
                    copyBean.copyProperties(budgetDetailBean,dynaDetails);
                    cvBudgetDetails.addElement(budgetDetailBean);
                }
            }
        }
        return cvBudgetDetails;   
    }
     /**
      * Get the modified BudgetDetailCalAmts data for the selected line item and
      * get the updated Form data in the form BudgetDetailcalAmtsBean
      * @ return CoeusVector containing the BudgetDetailcalAmtsBean
      * @param dynaForm
      * @param budgetInfoBean
      * @throws Exception
      * @return
      */
     private CoeusVector getCalAmtsData(DynaValidatorForm dynaForm,
        HttpServletRequest request) throws Exception{
         CoeusVector cvCalAmts = null;
        int budgetPeriod = ((Integer)dynaForm.get("budgetPeriod")).intValue();
        int lineItemNumber = ((Integer)dynaForm.get("lineItemNumber")).intValue();
        HttpSession session = request.getSession();
         Vector vecBudgetDetailCalAmts = (Vector)session.getAttribute("BudgetDetailCalAmts");
         BeanUtilsBean copyBean = null;
         BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = null;
         CoeusVector cvFilterCalAmtsData = null;
         cvFilterCalAmtsData = new CoeusVector();
         cvCalAmts = new CoeusVector();
         //Code added for Budget Bug fix for case# 2738
         CoeusVector cvMydata = new CoeusVector();
         if(vecBudgetDetailCalAmts!= null){
             for(int index =0; index < vecBudgetDetailCalAmts.size() ; index++){
                 DynaValidatorForm dynaCalAmts = (DynaValidatorForm)vecBudgetDetailCalAmts.get(index);
                 copyBean = new BeanUtilsBean();
                 budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
                 copyBean.copyProperties(budgetDetailCalAmountsBean,dynaCalAmts);
                 cvCalAmts.addElement(budgetDetailCalAmountsBean);
                 //Code added for Budget Bug fix for case# 2738
                 cvMydata.addElement(budgetDetailCalAmountsBean);
                }
                 cvCalAmts = filterBudgetDetailCalAmts(cvCalAmts,budgetPeriod,lineItemNumber,dynaForm);
                 if(cvCalAmts != null && cvCalAmts.size() > 0){
                    // cvCalAmts.sort("rateClasCode",true);

                     CoeusVector cvCalAmounts = sortCalAmts(cvCalAmts);
                     cvCalAmounts.sort("rateClassCode", true);
                     CoeusVector cvOnlySelectedData = new CoeusVector();
                     String[] checkedMessages = (String[])request.getParameterValues("applyRateFlag");
                     if(checkedMessages!= null){
                         for(int ind = 0; ind < checkedMessages.length ; ind++){
                             int mesgIndex = Integer.parseInt(checkedMessages[ind]);
                             for(int in =0 ; in < cvCalAmts.size(); in++){
                                 BudgetDetailCalAmountsBean calBean = (BudgetDetailCalAmountsBean)cvCalAmounts.get(in);
                                 if(mesgIndex == in){
                                     calBean.setApplyRateFlag(true);
                                     cvOnlySelectedData.addElement(calBean);
                                 }
                             }
                         }
                     }
                     if(cvOnlySelectedData.size() > 0){
                         for(int selIn = 0 ; selIn < cvOnlySelectedData.size();selIn++){
                             BudgetDetailCalAmountsBean dynaSelForm = (BudgetDetailCalAmountsBean)cvOnlySelectedData.get(selIn);
                             int rateClassCode = dynaSelForm.getRateClassCode();
                             for(int in =0 ; in < cvCalAmounts.size(); in++){
                                 BudgetDetailCalAmountsBean dynaInForm = (BudgetDetailCalAmountsBean)cvCalAmounts.get(in);
                                 int dynaRateClassCode = dynaInForm.getRateClassCode();
                                 if(rateClassCode == dynaRateClassCode){
                                     cvCalAmounts.removeElement(dynaSelForm);
                                     break;
                                 }
                             }
                         }
                     }
                     for(int fIndex = 0; fIndex < cvCalAmounts.size(); fIndex++){
                         BudgetDetailCalAmountsBean dynaFForm = (BudgetDetailCalAmountsBean)cvCalAmounts.get(fIndex);
                         dynaFForm.setApplyRateFlag(false);
                         dynaFForm.setCalculatedCost(0.0);
                         //Added for COEUSQA-2393 Revamp Coeus Budget Engine -Start
                         // If we uncheck the apply Rate flag need to set the zero for CalculatedCostSharing value.
                         dynaFForm.setCalculatedCostSharing(0.0);
                         //Added for COEUSQA-2393 - End
                     }
                     cvFilterCalAmtsData.removeAllElements();
                     cvFilterCalAmtsData.addAll(cvOnlySelectedData);
                     cvFilterCalAmtsData.addAll(cvCalAmounts);
                 }
                 
                 
       }
        //Code added for Budget Bug fix for case# 2738 - starts
        if(cvMydata!=null && cvFilterCalAmtsData!=null){
            for(int count=0 ; count<cvFilterCalAmtsData.size() ; count++){
                BudgetDetailCalAmountsBean calBean = (BudgetDetailCalAmountsBean) cvFilterCalAmtsData.get(count);
                for(int index=0 ; index<cvMydata.size() ; index++){
                    BudgetDetailCalAmountsBean bean = (BudgetDetailCalAmountsBean) cvMydata.get(index);
                    if(bean.getBudgetPeriod() == calBean.getBudgetPeriod() &&
                    bean.getLineItemNumber() == calBean.getLineItemNumber() &&
                    bean.getRateTypeCode() == calBean.getRateTypeCode() &&
                    bean.getRateClassCode() == calBean.getRateClassCode()){
                        cvMydata.remove(index);
                        cvMydata.add(calBean);
                        break;
                    }
                }
            }
        }
        return cvMydata;
        //Code added for Budget Bug fix for case# 2738 - ends
        
        //Code commented for Budget Bug fix for case# 2738
//        return cvFilterCalAmtsData;
     }
     
     /**
      * method to sort rate class code
      * @param cvBudgetDetailCalAmts
      * @throws Exception
      * @return
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
      * Get the BudgetLine Item details data. Compare with the line item selected and
      * populated thedata accordingy
      * @param dynaForm
      * @param budgetInfoBean
      * @throws Exception
      * @return
      */
     private CoeusVector getDetailsData(DynaValidatorForm dynaForm,
        HttpSession session) throws Exception{
        int budgetPeriod = ((Integer)dynaForm.get("budgetPeriod")).intValue();
        int lineItemNumber = ((Integer)dynaForm.get("lineItemNumber")).intValue();
        Vector vecBudgetDetails = (Vector)session.getAttribute("BudgetDetailData");
        BeanUtilsBean copyBean = null;
        BudgetDetailBean budgetDetailBean = null;
        CoeusVector cvBudgetDetails = null;
        if(vecBudgetDetails!= null && vecBudgetDetails.size() > 0){
            cvBudgetDetails =  new CoeusVector();
            for(int index=0;index<vecBudgetDetails.size();index++){
                DynaValidatorForm dynaDetails = (DynaValidatorForm)vecBudgetDetails.get(index);
                int period = ((Integer)dynaDetails.get("budgetPeriod")).intValue();
                int lineItem = ((Integer)dynaDetails.get("lineItemNumber")).intValue();
                /**If the Changed data is equals to the budget Period and LineItem
                 *number of the data got from the form data then only
                 *set the modified data to the dynaForm else ignore it
                 */
               
                if(budgetPeriod == period && lineItemNumber == lineItem){
                    String lineItemDescription = (String)dynaForm.get("lineItemDescription");
                    java.sql.Date lineItemStartDate = (java.sql.Date)dynaForm.get("lineItemStartDate");
                    java.sql.Date lineItemEndDate = (java.sql.Date)dynaForm.get("lineItemEndDate");
                    double lineItemCost = (formatStringToDouble((String)dynaForm.get("lineItemCost")));                    
                    double costSharingAmount = (formatStringToDouble((String)dynaForm.get("costSharingAmount")));
                    Boolean applyFlag = (Boolean)dynaForm.get("applyInRateFlag");
                    if(applyFlag == null){
                        applyFlag = new Boolean(false);
                    }else{
                        applyFlag = new Boolean(true);
                    }
                    //code added for enhancement - starts
                    Boolean onOffFlag = (Boolean)dynaForm.get("onOffCampusFlag");
                    if(onOffFlag == null){
                        onOffFlag = new Boolean(false);
                    }else{
                        onOffFlag = new Boolean(true);
                    }
                    //COEUSQA-1693 - Cost Sharing Submission - start
                    dynaDetails.set("submitCostSharingFlag",dynaForm.get("submitCostSharingFlag"));
                    //COEUSQA-1693 - Cost Sharing Submission - end
                    //code added for enhancement - ends
                    boolean applyInRateFlag = applyFlag.booleanValue();
                    //Modified for Case #3132 - start
                    //Changing quantity field from integer to float
//                    int quantity = ((Integer)dynaForm.get("quantity")).intValue();
                    double quantity = (formatStringToDouble((String)dynaForm.get("quantity")));
                    //Modified for Case #3132 - end
                    String costElement = (String)dynaForm.get("costElement");
                    dynaDetails.set("lineItemDescription",lineItemDescription);
                    dynaDetails.set("lineItemStartDate", lineItemStartDate);
                    dynaDetails.set("lineItemEndDate",lineItemEndDate);
                    dynaDetails.set("lineItemCost", new Double(lineItemCost));
                    dynaDetails.set("costSharingAmount",new Double(costSharingAmount));
                    dynaDetails.set("applyInRateFlag",new Boolean(applyInRateFlag));
                    //code added for enhancement 
                    dynaDetails.set("onOffCampusFlag", onOffFlag);
                    //Modified for Case #3132 - start
                    //Changing quantity field from integer to float
//                    dynaDetails.set("quantity",new Integer(quantity));
                    dynaDetails.set("quantity",new Double(quantity));
                    //Modified for Case #3132 - end
                    dynaDetails.set("costElement",costElement);
                }
                    copyBean = new BeanUtilsBean();
                    budgetDetailBean = new BudgetDetailBean();
                    copyBean.copyProperties(budgetDetailBean,dynaDetails);
                    cvBudgetDetails.addElement(budgetDetailBean);
            }
        }
         return cvBudgetDetails;
     }
     private boolean validateEnteredDate(java.sql.Date enteredStartDate, java.sql.Date enteredEndDate,
        DynaValidatorForm dynaForm, HttpServletRequest request) throws Exception{
            ActionMessages actionMessages = new ActionMessages();
            HttpSession session = request.getSession();
            int budgetPeriod = ((Integer)dynaForm.get("budgetPeriod")).intValue();
            Vector vecBudgetPeriod = (Vector)session.getAttribute("BudgetPeriodData");
            vecBudgetPeriod = filterBudgetPeriod(budgetPeriod, vecBudgetPeriod);
            if(vecBudgetPeriod.size() > 0){
                DynaValidatorForm dynaValidForm = (DynaValidatorForm)vecBudgetPeriod.get(0);
                if(enteredEndDate.compareTo(enteredStartDate) < 0){
                    actionMessages.add("budget_period_exceptionCode.1153" , new ActionMessage(
                    "budget_period_exceptionCode.1153" ) );
                    saveMessages(request, actionMessages);
                    return false;
                }
                if(enteredStartDate.before((java.sql.Date)dynaValidForm.get("startDate"))){
                    actionMessages.add("budget_period_exceptionCode.1159" , new ActionMessage(
                    "budget_period_exceptionCode.1159" ) );
                    saveMessages(request, actionMessages);
                    return false;
                }
                if(enteredStartDate.compareTo(enteredEndDate) > 0){
                    actionMessages.add("budget_period_exceptionCode.1156" , new ActionMessage(
                    "budget_period_exceptionCode.1156" ) );
                    saveMessages(request, actionMessages);
                    return false;
                }
                if(enteredEndDate.after((java.sql.Date)dynaValidForm.get("endDate"))){
                    actionMessages.add("budget_period_exceptionCode.1160" , new ActionMessage(
                    "budget_period_exceptionCode.1160" ) );
                    saveMessages(request, actionMessages);
                    return false;
                }
                
            }
            return true;
     }
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
    private boolean validationForDate(DynaValidatorForm dynaForm,
        HttpServletRequest request) throws Exception{
        String tempLineItemEndDate = ((String)dynaForm.get("tempLineItemEndDate")).trim();
        String tempLineItemStartDate = ((String)dynaForm.get("tempLineItemStartDate")).trim();
        DateUtils dateUtils = new DateUtils();
        boolean isValid = true;
        if(tempLineItemStartDate.equals(EMPTY_STRING)){
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("adjustPeriod_exceptionCode.1459" , new ActionMessage(
             "adjustPeriod_exceptionCode.1459" ) );
             saveMessages(request, actionMessages);
             return false;
            
        }else if(tempLineItemEndDate.equals(EMPTY_STRING)){
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("adjustPeriod_exceptionCode.1460" , new ActionMessage(
             "adjustPeriod_exceptionCode.1460" ) );
             saveMessages(request, actionMessages);
             return false;
        }
        isValid = dateUtils.validateDate(tempLineItemStartDate, ":/.,|-");
        if(!isValid){
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("budget_period_exceptionCode.1157" , new ActionMessage(
             "budget_period_exceptionCode.1157" ) );
             saveMessages(request, actionMessages);
             return isValid;

        }
        isValid = dateUtils.validateDate(tempLineItemEndDate, ":/.,|-");
        if(!isValid){
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("budget_period_exceptionCode.1158" , new ActionMessage(
             "budget_period_exceptionCode.1158" ) );
             saveMessages(request, actionMessages);
             return isValid;

        }
        //for two digit year
        tempLineItemEndDate = dateUtils.formatDate(tempLineItemEndDate,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
        tempLineItemStartDate = dateUtils.formatDate(tempLineItemStartDate,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
        java.sql.Date tempStartDate = dateUtils.getSQLDate(tempLineItemStartDate);
        java.sql.Date tempEndDate = dateUtils.getSQLDate(tempLineItemEndDate);
        isValid = validateEnteredDate(tempStartDate,tempEndDate,dynaForm,request);
        return isValid;
        
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
         HttpServletRequest request) throws Exception{
         int budgetPeriod = Integer.parseInt(strBudgetPeriod);
         HttpSession session = request.getSession();
         Vector vecBudgetPeriod = (Vector)session.getAttribute("BudgetPeriodData");
         boolean isValidated = false;
         if(vecBudgetPeriod.size() > 1){
             if(vecBudgetPeriod.size() == budgetPeriod){
                 request.setAttribute("lastPeriod", new Boolean(true));
                 isValidated = true;
             }else{
                 request.setAttribute("lastPeriod", new Boolean(false));
             }
         }else{
             request.setAttribute("firstPeriod", new Boolean(true));
         }
         

     }
     
     /**
      * To set the Period values to the first line item of each periods.
      * @param vecPeriodData
      * @param cvDetailData
      * @throws Exception
      * @return Coeusvector
      */     
     private CoeusVector setValueForDetails(CoeusVector vecPeriodData,
        CoeusVector cvDetailData)throws Exception{
         if(vecPeriodData!=null && cvDetailData!=null){
             for(int index=0 ; index<vecPeriodData.size() ; index++){
                BudgetPeriodBean periodBean = (BudgetPeriodBean) vecPeriodData.get(index);
                boolean isChanged = false;
                for(int count = 0 ; count<cvDetailData.size() ; count++){
                    BudgetDetailBean detailBean = (BudgetDetailBean) cvDetailData.get(count);
                    if(periodBean.getBudgetPeriod() == detailBean.getBudgetPeriod() && !isChanged){
                        detailBean.setDirectCost(periodBean.getTotalDirectCost());
                        detailBean.setIndirectCost(periodBean.getTotalIndirectCost());
                        detailBean.setTotalCostSharing(periodBean.getCostSharingAmount());
                        detailBean.setUnderRecoveryAmount(periodBean.getUnderRecoveryAmount());
                        isChanged = true;
                    }
                }
             }
         }
         return cvDetailData;
     }
     
}
