/*
 * FormulatedCostBudgetLineItemController.java
 *
 * Created on December 2, 2011, 12:39 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.wmc.coeuslite.budget.action;

import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.budget.bean.BudgetDetailBean;
import edu.mit.coeus.budget.bean.BudgetDetailCalAmountsBean;
import edu.mit.coeus.budget.bean.BudgetFormulatedCostDetailsBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetPeriodBean;
import edu.mit.coeus.budget.bean.BudgetPersonnelCalAmountsBean;
import edu.mit.coeus.budget.bean.BudgetPersonnelDetailsBean;
import edu.mit.coeus.budget.bean.BudgetTxnBean;
import edu.mit.coeus.budget.bean.BudgetUpdateTxnBean;
import edu.mit.coeus.budget.bean.ProposalRatesBean;
import edu.mit.coeus.budget.bean.ValidCERateTypesBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.unit.bean.UnitFormulatedCostBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.RateClassTypeConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.DataType;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.GreaterThan;
import edu.mit.coeus.utils.query.LesserThan;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.utils.query.Or;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.*;
import java.sql.Timestamp;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
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
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author satheeshkumarkn
 */
public class BudgetFormulatedLineItemDetailsAction extends BudgetBaseAction{
    
    private final String EMPTY_STRING = "";
    private final String GET_FORMULATED_DETAILS = "/getFormulatedLineItemDetails";
    private final String GET_FORMULATED_DETAILS_AFTER_ADD = "/getFormLineItemDetailsAfterAdd";
    private final String SAVE_AND_APPLY_TO_LATER_PERIODS = "/applyToFormulatedLaterPeriods";
    private final String SAVE_AND_APPLY_TO_CURRENT_PERIOD = "/applyToFormulatedCurrentPeriods";
    private final String SAVE_FORMULATED_COST_DETAILS= "/saveFormulatedLineItemDetail";
    private final String DELETE_FORMULATED_COST = "/deleteFormulatedLineItemDetail";
    private final String SYNC_FORMULATED_COST = "/syncCalculatedLineItemCosts";
    private final String TOTAL_COST_EXCEEDING = "totalCostExceed";
    private final String DATE_SEPARATERS = ":/.,|-";
    private final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private final char PERSONNEL = 'P';
    private final String BUDGET_SUMMARY_PAGE = "summaryPage";
    private final String SUCCESS_NAVIGATION = "success";
    private final String SYNC_MENU_CODE = "B023";
    
    /** Creates a new instance of BudgetLineItemDetailsAction */
    public BudgetFormulatedLineItemDetailsAction() {
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
        DynaValidatorForm dynaForm = (DynaValidatorForm)actionForm;
        ActionForward actionForward = performLineItemDetailsActions(actionMapping,dynaForm,request,response);
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
            DynaValidatorForm dynaForm, HttpServletRequest request,HttpServletResponse response) throws Exception{
        String navigator = EMPTY_STRING;
        ActionForward actionForward = null;
        String actionPath = actionMapping.getPath();
        if(GET_FORMULATED_DETAILS.equals(actionPath)) {
            navigator = getBudgetLineItemDetails(actionMapping,dynaForm,request);
            actionForward = actionMapping.findForward(navigator);
        }else if(SAVE_FORMULATED_COST_DETAILS.equals(actionPath)){
            navigator = addUpdFormulatedDetailsAndCalRates(dynaForm,request);
            actionForward = actionMapping.findForward(navigator);
        }else if(DELETE_FORMULATED_COST.equals(actionPath)){
            navigator = deleteFormulatedCost(dynaForm,request);
            actionForward = actionMapping.findForward(navigator);
        }else if(SAVE_AND_APPLY_TO_LATER_PERIODS.equals(actionPath) ) {
            navigator = saveAndApplyToLaterPeriods(dynaForm, request);
            actionForward = actionMapping.findForward(navigator);
        }else if(SAVE_AND_APPLY_TO_CURRENT_PERIOD.equals(actionPath)){
            navigator = saveAndApplyToCurrentPeriod(dynaForm, request);
            actionForward = actionMapping.findForward(navigator);
        }else if(actionPath.equals(SYNC_FORMULATED_COST)){
            setSelectedStatusBudgetMenu(SYNC_MENU_CODE, request);
            String canSync = request.getParameter("canSync");
            // When user clicked 'Yes' button the formulated cost will be synced from the unit hierarchy and navigated to the budget summary page
            if(canSync != null && "YES".equals(canSync)){
                syncCalculatedLineItemcosts(request);
                navigator = BUDGET_SUMMARY_PAGE;
            }else if(canSync != null && "NO".equals(canSync)){
                navigator = BUDGET_SUMMARY_PAGE;
            }else{
                navigator = SUCCESS_NAVIGATION;
            }
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
    private String saveAndApplyToCurrentPeriod(DynaValidatorForm dynaForm,HttpServletRequest request) throws Exception{
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        addUpdFormulatedDetailsAndCalRates(dynaForm,request);
        dynaForm.set("popUp","close");
        session.removeAttribute("budgetDetailCalAmts");
        session.removeAttribute("lastPeriod");
        session.removeAttribute("firstPeriod");
        navigator=SUCCESS_NAVIGATION;
        return navigator;
    }
    
    /**
     * Method to delete the formulated cost
     * @param dynaForm
     * @param request
     * @throws java.lang.Exception
     * @return
     */
    private String deleteFormulatedCost(DynaValidatorForm dynaForm,HttpServletRequest request) throws Exception{
        String navigator = SUCCESS_NAVIGATION;
        HttpSession session = request.getSession();
        DynaValidatorForm formulatedFormToDelete = null;
        dynaForm.set("acType",TypeConstants.DELETE_RECORD);
        CoeusVector cvFormulatedDetails = (CoeusVector)session.getAttribute("formulatedDetails");
        if(cvFormulatedDetails != null && !cvFormulatedDetails.isEmpty()){
            CoeusVector cvNonDeleteFormDetails  = new CoeusVector();
            int formulatedNumber = Integer.parseInt(request.getParameter("formulatedNumber"));
            for(Object formulatedDetails : cvFormulatedDetails){
                DynaActionForm formulatedCostForm = (DynaActionForm)formulatedDetails;
                formulatedCostForm.set("acType",TypeConstants.DELETE_RECORD);
                int formFormulatedNumber = ((Integer)formulatedCostForm.get("formulatedNumber")).intValue();
                if(formulatedNumber == formFormulatedNumber){
                    formulatedFormToDelete = (DynaValidatorForm) formulatedCostForm;
                }else{
                    cvNonDeleteFormDetails.add(formulatedCostForm);
                }
            }
            // Deleted form collection will be updated to the session
            session.setAttribute("formulatedDetails",cvNonDeleteFormDetails);
        }
        
        edu.mit.coeus.bean.UserInfoBean userInfoBean = (edu.mit.coeus.bean.UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        BudgetUpdateTxnBean  budgetUpdateTxnBean = new BudgetUpdateTxnBean(userId);
        BudgetFormulatedCostDetailsBean budgetFormulatedCostDetailsBean = new BudgetFormulatedCostDetailsBean();
        BeanUtilsBean copyBean = new BeanUtilsBean();
        copyBean.copyProperties(budgetFormulatedCostDetailsBean,formulatedFormToDelete);
        Vector vecProcedures = new Vector();
        vecProcedures.add(budgetUpdateTxnBean.addUpdBudgetFormulatedCost(budgetFormulatedCostDetailsBean));
        executeStoreProcs(vecProcedures);
        calculateAndSaveCalAmts(dynaForm,request);
        
        return navigator;
    }
    
    private void calculateAndSaveCalAmts(DynaValidatorForm dynaForm,HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        BeanUtilsBean copyBean = new BeanUtilsBean();
        String budPeriod = (String)session.getAttribute("budgetPeriod"+session.getId());
        String lineItemNumber = (String)session.getAttribute("lineItemNumber"+session.getId());
        Vector vecDynaPeriodData = (Vector)session.getAttribute("BudgetPeriodData");
        
        CoeusVector vecPeriodData = preparePeriodData(vecDynaPeriodData);
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        CoeusVector cvDetails = getDetailsData(dynaForm, session);
        CoeusVector cvCalAmts = getCalAmtsData(dynaForm, request);
        Hashtable htBudgetData = initializeBudgetCalculator(budgetInfoBean);
        htBudgetData.remove(BudgetPeriodBean.class);
        cvDetails = setValueForDetails(vecPeriodData, cvDetails);
        
        htBudgetData.remove(BudgetDetailBean.class);
        htBudgetData.remove(BudgetDetailCalAmountsBean.class);
        
        htBudgetData.put(BudgetPeriodBean.class, vecPeriodData);
        htBudgetData.put(BudgetDetailBean.class, cvDetails);
        htBudgetData.put(BudgetDetailCalAmountsBean.class, cvCalAmts);
        Hashtable htCalculatedData = calculatedBudgetPeriod(htBudgetData,Integer.parseInt(budPeriod), budgetInfoBean);
        //To check total cost not exceeding $9,999,999,999.999
        boolean isTotalCostExceed = checkTotalCost(htCalculatedData);
        if(isTotalCostExceed){
            request.setAttribute("budgetDetails",dynaForm);
            request.setAttribute("period", new Integer(budPeriod));
            cvCalAmts = getCalAmtsData(dynaForm, request);
            cvCalAmts.sort("rateClassCode",true);
            cvCalAmts = prepareDynaBean(cvCalAmts, dynaForm);
            session.setAttribute("budgetDetailCalAmts", cvCalAmts);
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("budget_common_exceptionCode.1011" , new ActionMessage(
                    "budget_common_exceptionCode.1011" ) );
            saveMessages(request, actionMessages);
        }
        
        saveBudget(htCalculatedData, request, queryKey);
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmCalAmtsDetails = new HashMap();
        hmCalAmtsDetails.put("proposalNumber",budgetInfoBean.getProposalNumber());
        hmCalAmtsDetails.put("versionNumber",new Integer(budgetInfoBean.getVersionNumber()));
        Hashtable htCalAmtsDetails =(Hashtable)webTxnBean.getResults(request, "getBudgetDetailCalAmts", hmCalAmtsDetails);
        Vector vecCalAmts = (Vector)htCalAmtsDetails.get("getBudgetDetailCalAmts");
        session.setAttribute("BudgetDetailCalAmts", vecCalAmts);
    }
    
    /**
     * Perform this action and saveAndApplyToLaterPeriods action is Performed
     * @param dynaForm
     *
     * @throws Exception
     * @return
     */
    private String saveAndApplyToLaterPeriods(DynaValidatorForm dynaForm, HttpServletRequest request) throws Exception{
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        String budPeriod = (String)session.getAttribute("budgetPeriod"+session.getId());
        addUpdFormulatedDetailsAndCalRates(dynaForm,request);
        
        BeanUtilsBean copyBean = new BeanUtilsBean();
        String strLineItemCost = (String)session.getAttribute("lineItemCost");
        
        Vector vecDynaPeriodData = (Vector)session.getAttribute("BudgetPeriodData");
        CoeusVector vecPeriodData = preparePeriodData(vecDynaPeriodData);
        boolean value = applyToLaterPeriods(vecPeriodData,dynaForm,request);
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
                navigator=SUCCESS_NAVIGATION;
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
        navigator  = saveBudget(htCalculatedData, request, queryKey);
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
            navigator = SUCCESS_NAVIGATION;
            updateProposalSyncFlags(request, proposalNumber);
            removeQueryEngineCollection(queryKey);
        }
        return navigator;
    }
    
    private boolean  applyToLaterPeriods(CoeusVector vecPeriodData, DynaValidatorForm dynaForm, HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        QueryEngine queryEngine = QueryEngine.getInstance();
        String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        CoeusVector cvBudgetDetailsCalAmts = prepareBudgetDetailsCalAmtsData(dynaForm, request);
        Hashtable htBudgetData = initializeBudgetCalculator(budgetInfoBean);
        htBudgetData.remove(BudgetPeriodBean.class);
        htBudgetData.remove(BudgetDetailCalAmountsBean.class);
        htBudgetData.put(BudgetPeriodBean.class,vecPeriodData);
        htBudgetData.put(BudgetDetailCalAmountsBean.class,cvBudgetDetailsCalAmts == null? new CoeusVector():cvBudgetDetailsCalAmts );
        queryEngine.addDataCollection(queryKey,htBudgetData);
        
        int currentPeriod = Integer.parseInt(session.getAttribute("budgetPeriod"+session.getId()).toString());
        int lineItemNumber = Integer.parseInt(session.getAttribute("lineItemNumber"+session.getId()).toString());
        int totalPeriods = vecPeriodData.size();
        CoeusVector vecBudgetDetails, vecBudgetPersonnelDetails;
        BudgetDetailBean currentBudgetDetailBean = null, nextBudgetDetailBean, newBudgetDetailBean;
        BudgetPeriodBean nextBudgetPeriodBean;
        boolean displayMessage = true;
        int maxLINum = 0;
        int maxSeqNum = 0;
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
            Equals eqPeriod = new Equals("budgetPeriod", new Integer(period));
            Equals eqLINumber = new Equals("basedOnLineItem", new Integer(lineItemNumber));
            And eqPeriodAndEqLINumber = new And(eqPeriod, eqLINumber);
            vecBudgetDetails = queryEngine.getActiveData(queryKey, BudgetDetailBean.class, eqPeriodAndEqLINumber);
            
            CoeusVector vecCurrentPeriodDetail = queryEngine.getActiveData(queryKey, BudgetDetailBean.class,
                    new And(
                    new Equals("budgetPeriod", new Integer(period - 1)),
                    new Equals("lineItemNumber", new Integer(lineItemNumber))
                    ));
            if(vecCurrentPeriodDetail != null || vecCurrentPeriodDetail.size() > 0) {
                currentBudgetDetailBean = (BudgetDetailBean)vecCurrentPeriodDetail.get(0);
            }
            lineItemCost = currentBudgetDetailBean.getLineItemCost();
            if(vecBudgetDetails != null && vecBudgetDetails.size() > 0) {
                nextBudgetDetailBean = (BudgetDetailBean)vecBudgetDetails.get(0);
                BudgetDetailBean copyBudgetDetailBean = (BudgetDetailBean)ObjectCloner.deepCopy(currentBudgetDetailBean);
                copyBudgetDetailBean.setBudgetPeriod(nextBudgetDetailBean.getBudgetPeriod());
                copyBudgetDetailBean.setLineItemNumber(nextBudgetDetailBean.getLineItemNumber());
                copyBudgetDetailBean.setBasedOnLineItem(currentBudgetDetailBean.getLineItemNumber());
                copyBudgetDetailBean.setLineItemSequence(nextBudgetDetailBean.getLineItemSequence());
                copyBudgetDetailBean.setLineItemStartDate(nextBudgetDetailBean.getLineItemStartDate());
                copyBudgetDetailBean.setLineItemEndDate(nextBudgetDetailBean.getLineItemEndDate());
                
                //Check for Cal Amts
                if(! copyBudgetDetailBean.getCostElement().equals(nextBudgetDetailBean.getCostElement())) {
                    //Delete old Cal Amts.
                    Equals eqBgtPeriod = new Equals("budgetPeriod", new Integer(nextBudgetDetailBean.getBudgetPeriod()));
                    Equals eqLINum = new Equals("lineItemNumber", new Integer(nextBudgetDetailBean.getLineItemNumber()));
                    And eqPeriodAndEqLINum = new And(eqBgtPeriod, eqLINum);
                    
                    CoeusVector vecCalAmts = queryEngine.getActiveData(queryKey, BudgetDetailCalAmountsBean.class, eqPeriodAndEqLINum);
                    if(vecCalAmts != null && vecCalAmts.size() > 0) {
                        BudgetDetailCalAmountsBean budgetDetailCalAmountsBean;
                        int size = vecCalAmts.size();
                        for(int index = 0; index < size; index++) {
                            budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)vecCalAmts.get(index);
                            budgetDetailCalAmountsBean.setAcType(TypeConstants.DELETE_RECORD);
                            queryEngine.delete(queryKey, budgetDetailCalAmountsBean);
                        }
                    }
                    
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
                            budgetDetailCalAmountsBean.setLineItemNumber(nextBudgetDetailBean.getLineItemNumber());
                            budgetDetailCalAmountsBean.setAcType(TypeConstants.INSERT_RECORD);
                            queryEngine.insert(queryKey, budgetDetailCalAmountsBean);
                        }
                    }
                    
                }
                nextBudgetDetailBean = copyBudgetDetailBean;
                CoeusVector cvFormualtedDetails  = queryEngine.getActiveData(queryKey, BudgetFormulatedCostDetailsBean.class,
                        new And(
                        new Equals("budgetPeriod", new Integer(period)),
                        new Equals("lineItemNumber", new Integer(copyBudgetDetailBean.getLineItemNumber()))
                        ));
                if(cvFormualtedDetails != null && !cvFormualtedDetails.isEmpty()){
                    for(Object formulatedCostDetails : cvFormualtedDetails){
                        BudgetFormulatedCostDetailsBean budgetFormulatedCostDetailsBean = (BudgetFormulatedCostDetailsBean)formulatedCostDetails;
                        queryEngine.delete(queryKey,budgetFormulatedCostDetailsBean);
                    }
                }
                
                CoeusVector cvFormulatedCostDetails = queryEngine.getActiveData(queryKey, BudgetFormulatedCostDetailsBean.class,
                        new And(
                        new Equals("budgetPeriod", new Integer(period - 1)),
                        new Equals("lineItemNumber", new Integer(lineItemNumber))
                        ));
                if(cvFormulatedCostDetails != null && !cvFormulatedCostDetails.isEmpty()){
                    for(Object formulatedCostDetails : cvFormulatedCostDetails){
                        BudgetFormulatedCostDetailsBean newBudgetFormulatedCostDetailsBean = (BudgetFormulatedCostDetailsBean)formulatedCostDetails;
                        newBudgetFormulatedCostDetailsBean.setProposalNumber(nextBudgetDetailBean.getProposalNumber());
                        newBudgetFormulatedCostDetailsBean.setVersionNumber(nextBudgetDetailBean.getVersionNumber());
                        newBudgetFormulatedCostDetailsBean.setBudgetPeriod(nextBudgetDetailBean.getBudgetPeriod());
                        newBudgetFormulatedCostDetailsBean.setLineItemNumber(nextBudgetDetailBean.getLineItemNumber());
                        newBudgetFormulatedCostDetailsBean.setAcType(TypeConstants.INSERT_RECORD);
                        queryEngine.insert(queryKey, newBudgetFormulatedCostDetailsBean);
                    }
                }
                
                nextBudgetDetailBean.setLineItemCost(lineItemCost);
                queryEngine.update(queryKey, nextBudgetDetailBean);
                lineItemNumber = nextBudgetDetailBean.getLineItemNumber();
                
                continue ;
            }
            
            //Get Max Squence Number if Adding
            Equals eqperiod = new Equals("budgetPeriod", new Integer(period));
            CoeusVector vecMaxSeqNum = queryEngine.getActiveData(queryKey, BudgetDetailBean.class, eqperiod);
            if(vecMaxSeqNum != null && vecMaxSeqNum.size() > 0) {
                vecMaxSeqNum.sort("lineItemSequence", false);
                maxSeqNum =  ((BudgetDetailBean)vecMaxSeqNum.get(0)).getLineItemSequence() + 1;
            }else {
                maxSeqNum = 1;
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
            newBudgetDetailBean.setLineItemCost(lineItemCost);
            queryEngine.insert(queryKey, newBudgetDetailBean);
            
            //Copy Budget Detail Cal Amts Beans
            CoeusVector vecCalAmts = queryEngine.getActiveData(queryKey, BudgetDetailCalAmountsBean.class, eqBudgetPeriodAndEqLineItemNo);
            if(vecCalAmts == null || vecCalAmts.size() == 0) vecCalAmts = new CoeusVector();
            
            BudgetDetailCalAmountsBean newBudgetDetailCalAmountsBean = null;
            
            for(int calAmtsIndex = 0; calAmtsIndex < vecCalAmts.size(); calAmtsIndex++) {
                newBudgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)vecCalAmts.get(calAmtsIndex);
                newBudgetDetailCalAmountsBean.setBudgetPeriod(nextBudgetPeriodBean.getBudgetPeriod());
                newBudgetDetailCalAmountsBean.setLineItemNumber(newBudgetDetailBean.getLineItemNumber());
                newBudgetDetailCalAmountsBean.setAcType(TypeConstants.INSERT_RECORD);
                queryEngine.insert(queryKey, newBudgetDetailCalAmountsBean);
            }//End For Copy Budget Detail Cal Amts Beans
            
            CoeusVector cvFormulatedCostDetails = queryEngine.getActiveData(queryKey, BudgetFormulatedCostDetailsBean.class, eqBudgetPeriodAndEqLineItemNo);
            if(cvFormulatedCostDetails != null && !cvFormulatedCostDetails.isEmpty()){
                for(Object formulatedCostDetails : cvFormulatedCostDetails){
                    BudgetFormulatedCostDetailsBean newBudgetFormulatedCostDetailsBean = (BudgetFormulatedCostDetailsBean)formulatedCostDetails;
                    newBudgetFormulatedCostDetailsBean.setProposalNumber(nextBudgetPeriodBean.getProposalNumber());
                    newBudgetFormulatedCostDetailsBean.setVersionNumber(nextBudgetPeriodBean.getVersionNumber());
                    newBudgetFormulatedCostDetailsBean.setBudgetPeriod(nextBudgetPeriodBean.getBudgetPeriod());
                    newBudgetFormulatedCostDetailsBean.setLineItemNumber(newBudgetDetailBean.getLineItemNumber());
                    newBudgetFormulatedCostDetailsBean.setAcType(TypeConstants.INSERT_RECORD);
                    queryEngine.insert(queryKey, newBudgetFormulatedCostDetailsBean);
                }
            }
            
        }
        return true;
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
    private CoeusVector prepareBudgetDetailsCalAmtsData(DynaValidatorForm dynaForm, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        Vector vecBudgetDetailCalAmts = (Vector)session.getAttribute("BudgetDetailCalAmts");
        BeanUtilsBean copyBean = null;
        BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = null;
        CoeusVector cvBudgetDetailsCalAmts = null;
        int budgetPeriod = Integer.parseInt(session.getAttribute("budgetPeriod"+session.getId()).toString());
        int lineItemNumber = Integer.parseInt(session.getAttribute("lineItemNumber"+session.getId()).toString());
        CoeusVector cvFilterData = null;
        CoeusVector cvFilterCalAmtsData = new CoeusVector();
        CoeusVector cvMydata = new CoeusVector();
        if(vecBudgetDetailCalAmts!= null && vecBudgetDetailCalAmts.size() > 0){
            cvBudgetDetailsCalAmts =  new CoeusVector();
            for(int index=0;index<vecBudgetDetailCalAmts.size();index++){
                DynaValidatorForm dynaCalAmts = (DynaValidatorForm)vecBudgetDetailCalAmts.get(index);
                copyBean = new BeanUtilsBean();
                budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
                copyBean.copyProperties(budgetDetailCalAmountsBean,dynaCalAmts);
                cvBudgetDetailsCalAmts.addElement(budgetDetailCalAmountsBean);
                cvMydata.addElement(budgetDetailCalAmountsBean);
            }
            cvFilterData = filterBudgetDetailCalAmts(cvBudgetDetailsCalAmts,budgetPeriod,lineItemNumber,dynaForm);
            if(cvFilterData != null && cvFilterData.size() > 0){
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
        HttpSession session = request.getSession();
        int budgetPeriod = Integer.parseInt((String)session.getAttribute("budgetPeriod"+session.getId()));
        int lineItemNumber = Integer.parseInt((String)session.getAttribute("lineItemNumber"+session.getId()));
        
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
        int budgetPeriod = Integer.parseInt((String)session.getAttribute("budgetPeriod"+session.getId()));
        int lineItemNumber = Integer.parseInt((String)session.getAttribute("lineItemNumber"+session.getId()));
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
                    dynaDetails.set("submitCostSharingFlag","N");
                    double lineItemCost = getLineItemCost(session,dynaDetails,dynaForm);
                    dynaDetails.set("lineItemCost", new Double(lineItemCost));
                    dynaDetails.set("costSharingAmount",new Double(0.0));
                    dynaDetails.set("underRecoveryAmount",new Double(0.0));
                    dynaDetails.set("applyInRateFlag",new Boolean(false));
                    CoeusVector cvFormulatedCostDetails = (CoeusVector)session.getAttribute("formulatedDetails");
                    if(cvFormulatedCostDetails != null && !cvFormulatedCostDetails.isEmpty()){
                        dynaDetails.set("quantity",new Double(cvFormulatedCostDetails.size()+""));
                    }else{
                        
                    }
                    
                }
                copyBean = new BeanUtilsBean();
                budgetDetailBean = new BudgetDetailBean();
                copyBean.copyProperties(budgetDetailBean,dynaDetails);
                cvBudgetDetails.addElement(budgetDetailBean);
            }
        }
        return cvBudgetDetails;
    }
    
    /**
     * Method to prepare dyna beans 
     * @param cvSortCalAmts 
     * @param dynaForm 
     * @throws java.lang.Exception 
     * @return cvBudgetDetailCalAmts
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
        if(vecBudgetPeriod.size() > 1){
            if(vecBudgetPeriod.size() == budgetPeriod){
                request.setAttribute("lastPeriod", new Boolean(true));
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
    
    /**
     * Method to get budget line item details
     * @param actionMapping 
     * @param dynaForm 
     * @param request 
     * @throws java.lang.Exception 
     * @return success
     */
    private String getBudgetLineItemDetails(ActionMapping actionMapping,DynaValidatorForm dynaForm,HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        String budgetPeriod = request.getParameter("budgetPeriod");
        String lineItemNumber = request.getParameter("lineItemNumber");
        if(budgetPeriod != null ){
            session.setAttribute("budgetPeriod"+session.getId(),budgetPeriod);
        }
        if(lineItemNumber != null){
            session.setAttribute("lineItemNumber"+session.getId(),lineItemNumber);
        }
        budgetPeriod = (String)session.getAttribute("budgetPeriod"+session.getId());
        lineItemNumber = (String)session.getAttribute("lineItemNumber"+session.getId());
        
        BudgetInfoBean budgetInfoBean =  (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        String proposalNumber = budgetInfoBean.getProposalNumber();
        int versionNumber = budgetInfoBean.getVersionNumber();
        
        BudgetDataTxnBean budgetDataTxnBean  = new BudgetDataTxnBean();
        CoeusVector cvFormulatedDetails = null;
        if(GET_FORMULATED_DETAILS_AFTER_ADD.equals(actionMapping.getPath())){
            cvFormulatedDetails = (CoeusVector)session.getAttribute("formulatedDetails");
        }else {
            CoeusVector cvAllFormulatedDetails = budgetDataTxnBean.getBudgetFormulatedDetail(proposalNumber,versionNumber);
            if(cvAllFormulatedDetails != null && !cvAllFormulatedDetails.isEmpty()){
                Equals eqBudgetPeriod = new Equals("budgetPeriod",Integer.parseInt(budgetPeriod));
                Equals eqlineItemNumber = new Equals("lineItemNumber",Integer.parseInt(lineItemNumber));
                And andPeriodLineItem = new And(eqBudgetPeriod,eqlineItemNumber);
                CoeusVector cvFilteredFormulatedDetails = cvAllFormulatedDetails.filter(andPeriodLineItem);
                if(cvFilteredFormulatedDetails != null && !cvFilteredFormulatedDetails.isEmpty()){
                    cvFormulatedDetails = new CoeusVector();
                    BeanUtilsBean copyBean = new BeanUtilsBean();
                    for(Object formulatedDetails : cvFilteredFormulatedDetails){
                        BudgetFormulatedCostDetailsBean formulatedDetailBean = (BudgetFormulatedCostDetailsBean)formulatedDetails;
                        DynaActionForm dynaActionForm = getDynaForm(request,"formulatedLineItemDetails");
                        copyBean.copyProperties(dynaActionForm,formulatedDetailBean);
                        cvFormulatedDetails.add(dynaActionForm);
                    }
                    
                }
                
            }
        }
        
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
                    double quantity = ((Double)dynaFormData.get("quantity")).doubleValue();
                    numberFormat = java.text.NumberFormat.getNumberInstance(java.util.Locale.US);
                    String strQuantity = numberFormat.format(quantity);
                    if(strQuantity.indexOf(".") == -1) {
                        strQuantity = strQuantity + ".00";
                    }
                    break;
                }
            }
        }
        session.setAttribute("formulatedDetails",cvFormulatedDetails);
        session.setAttribute("formulatedTypes",budgetDataTxnBean.getFormulatedTypes());
        session.setAttribute("unitFormulatedCostDetails",budgetDataTxnBean.getUnitFormulatedCostForProposalLeadUnit(proposalNumber));
        
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
            session.setAttribute("budgetDetailCalAmts", cvBudgetDetailCalAmts);
        }
        request.setAttribute("period", new Integer(Integer.parseInt(budgetPeriod)));
        return SUCCESS_NAVIGATION;
    }
    
    /**
     * Method to validate for apply to later periods
     * @param strBudgetPeriod 
     * @param session 
     * @throws java.lang.Exception 
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
                session.setAttribute("lastPeriod", new Boolean(true));
                isValidated = true;
            }
        }else{
            session.setAttribute("firstPeriod", new Boolean(true));
        }
    }
    
    /**
     * Method to get the dyna form
     * @param request 
     * @param formInstance 
     * @throws java.lang.Exception 
     * @return dynaActionForm
     */
    private DynaActionForm getDynaForm(HttpServletRequest request, String formInstance) throws Exception{
        ServletContext servletContext = request.getSession().getServletContext();
        ModuleConfig moduleConfig = RequestUtils.getModuleConfig(request, servletContext);
        FormBeanConfig formConfig = moduleConfig.findFormBeanConfig(formInstance);
        DynaActionFormClass dynaClass = DynaActionFormClass.createDynaActionFormClass(formConfig);
        DynaActionForm dynaActionForm = (DynaActionForm)dynaClass.newInstance();
        return dynaActionForm;
    }
    
    /**
     * Method to inset, update formulated cost details
     * @param dynaForm 
     * @param request 
     * @throws java.lang.Exception 
     * @return success
     */
    private String addUpdFormulatedDetailsAndCalRates(DynaValidatorForm dynaForm, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        edu.mit.coeus.bean.UserInfoBean userInfoBean = (edu.mit.coeus.bean.UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        BudgetUpdateTxnBean budgetUpdateTxnBean = new BudgetUpdateTxnBean(userId);
        String acType = (String)dynaForm.get("acType");
        if(TypeConstants.INSERT_RECORD.equals(acType) || TypeConstants.UPDATE_RECORD.equals(acType)){
            BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
            int budgetPeriod = Integer.parseInt((String)session.getAttribute("budgetPeriod"+session.getId()));
            int lineItemNumber = Integer.parseInt((String)session.getAttribute("lineItemNumber"+session.getId()));
            int formulatedCode = ((Integer)dynaForm.get("formulatedCode")).intValue();
            double unitCost = formatStringToDouble((String)dynaForm.get("unitCost"));
            int count = ((Integer)dynaForm.get("count")).intValue();
            int frequency = ((Integer)dynaForm.get("frequency")).intValue();
            double calculatedExpenses = formatStringToDouble((String)dynaForm.get("calculatedExpenses"));
            BudgetFormulatedCostDetailsBean budgetFormulatedCostDetailsBean = new BudgetFormulatedCostDetailsBean();
            budgetFormulatedCostDetailsBean.setAcType(acType);
            budgetFormulatedCostDetailsBean.setProposalNumber(budgetInfoBean.getProposalNumber());
            budgetFormulatedCostDetailsBean.setVersionNumber(budgetInfoBean.getVersionNumber());
            budgetFormulatedCostDetailsBean.setBudgetPeriod(budgetPeriod);
            budgetFormulatedCostDetailsBean.setLineItemNumber(lineItemNumber);
            if(TypeConstants.INSERT_RECORD.equals(acType)){
                budgetFormulatedCostDetailsBean.setFormulatedNumber(getMaxFormulatedNumber(session)+1);
            }else if(TypeConstants.UPDATE_RECORD.equals(acType)){
                int currentFormFormulatedNumber = ((Integer)dynaForm.get("formulatedNumber")).intValue();
                budgetFormulatedCostDetailsBean.setFormulatedNumber(currentFormFormulatedNumber);
                budgetFormulatedCostDetailsBean.setAwFormulatedNumber(currentFormFormulatedNumber);
                CoeusVector cvFormulatedCostDetails = (CoeusVector)session.getAttribute("formulatedDetails");
                if(cvFormulatedCostDetails != null && !cvFormulatedCostDetails.isEmpty()){
                    for(Object formulatedCostDetails : cvFormulatedCostDetails){
                        DynaActionForm formulatedCostForm = (DynaActionForm)formulatedCostDetails;
                        int formulatedNumber = ((Integer)formulatedCostForm.get("formulatedNumber")).intValue();
                        if(currentFormFormulatedNumber == formulatedNumber){
                            formulatedCostForm.set("formulatedCode",dynaForm.get("formulatedCode"));
                            formulatedCostForm.set("unitCost",dynaForm.get("unitCost"));
                            formulatedCostForm.set("count",dynaForm.get("count"));
                            formulatedCostForm.set("frequency",dynaForm.get("frequency"));
                            formulatedCostForm.set("calculatedExpenses",dynaForm.get("calculatedExpenses"));
                        }
                    }
                }
                session.setAttribute("formulatedDetails",cvFormulatedCostDetails);
            }
            budgetFormulatedCostDetailsBean.setFormulatedCode(formulatedCode);
            budgetFormulatedCostDetailsBean.setUnitCost(unitCost);
            budgetFormulatedCostDetailsBean.setCount(count);
            budgetFormulatedCostDetailsBean.setFrequency(frequency);
            budgetFormulatedCostDetailsBean.setCalculatedExpenses(calculatedExpenses);
            Vector vecProcedures = new Vector();
            vecProcedures.add(budgetUpdateTxnBean.addUpdBudgetFormulatedCost(budgetFormulatedCostDetailsBean));
            executeStoreProcs(vecProcedures);
        }
        calculateAndSaveCalAmts(dynaForm,request);
        return SUCCESS_NAVIGATION;
    }
    
    /**
     * Method to execute the store procedures
     * @param vecProcedures 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @throws edu.mit.coeus.exception.CoeusException 
     */
    private void executeStoreProcs(Vector vecProcedures) throws DBException, CoeusException{
        DBEngineImpl dbEngine = new DBEngineImpl();
        if(dbEngine!=null){
            java.sql.Connection conn = null;
            try{
                conn = dbEngine.beginTxn();
                dbEngine.executeStoreProcs(vecProcedures,conn);
                dbEngine.commit(conn);
            }catch(Exception sqlEx){
                dbEngine.rollback(conn);
                throw new CoeusException(sqlEx.getMessage());
            }finally{
                dbEngine.endTxn(conn);
            }
        }
    }
    
    /**
     * Method to get the max formulated number
     * @param session 
     * @return maxFormulatedNumber
     */
    private int getMaxFormulatedNumber(HttpSession session){
        CoeusVector cvFormulatedCostDetails = (CoeusVector)session.getAttribute("formulatedDetails");
        int maxFormulatedNumber = 1;
        if(cvFormulatedCostDetails != null && !cvFormulatedCostDetails.isEmpty()){
            DynaActionForm formulatedCostForm = (DynaActionForm)cvFormulatedCostDetails.get(cvFormulatedCostDetails.size()-1);
            maxFormulatedNumber = ((Integer)formulatedCostForm.get("formulatedNumber")).intValue();
        }
        return maxFormulatedNumber;
    }
    
    /**
     * Method to get the line item cost 
     * @param session 
     * @param dynaDetailsForm 
     * @param dynaForm 
     * @throws java.lang.Exception 
     * @return lineItemCost
     */
    private double getLineItemCost(HttpSession session,DynaValidatorForm dynaDetailsForm, DynaValidatorForm dynaForm) throws Exception {
        double lineItemCost = 0.00;
        CoeusVector cvFormulatedCostDetails = (CoeusVector)session.getAttribute("formulatedDetails");
        String acType = "";
        if(dynaForm.get("acType") != null){
            acType = (String)dynaForm.get("acType");
        }
        if(cvFormulatedCostDetails != null && !cvFormulatedCostDetails.isEmpty()){
            for(Object fomulatedCostDetails : cvFormulatedCostDetails){
                DynaActionForm formulatedCostForm = (DynaActionForm)fomulatedCostDetails;
                double calculatedExpenses = formatStringToDouble((String)formulatedCostForm.get("calculatedExpenses"));
                lineItemCost = lineItemCost + calculatedExpenses;
            }
        }
        if(TypeConstants.INSERT_RECORD.equals(acType)){
            double calculatedExpenses = formatStringToDouble((String)dynaForm.get("calculatedExpenses"));
            lineItemCost = lineItemCost + calculatedExpenses;
        }else if(acType == null || "".equals(acType)){
            lineItemCost = ((Double)dynaDetailsForm.get("lineItemCost")).doubleValue();
            
        }
        return lineItemCost;
    }
    
    /**
     * Method to sync the formulated line item formulated cost in all periods with the unit formulated cost based on the formulated types
     * Calculate line item cost for the formualted cost based on the new cost and calculate the budget for all periods
     * budget will be saved
     *
     * @param request
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws java.lang.Exception
     */
    private void syncCalculatedLineItemcosts(HttpServletRequest request) throws DBException, CoeusException, Exception{
        HttpSession session = request.getSession();
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        BudgetTxnBean budgetTxnBean = new BudgetTxnBean();
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        Hashtable htBudgetData = budgetTxnBean.getBudgetData(budgetInfoBean);
        String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        QueryEngine queryEngine = QueryEngine.getInstance();
        queryEngine.addDataCollection(queryKey,htBudgetData);
        // When formulated cost exists in the unit hierarchy based on the proposa lead unit,
        // if not travers to the hierarchy and get the first available formualted cost, sync will start only when formulated exists in the unit hierarchy
        CoeusVector cvUnitFormulatedCost = budgetDataTxnBean.getUnitFormulatedCostForProposalLeadUnit(budgetInfoBean.getProposalNumber());
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
                CoeusVector cvBudgetPeriod = queryEngine.getDetails(queryKey, BudgetPeriodBean.class);
                if(cvBudgetPeriod != null && !cvBudgetPeriod.isEmpty()){
                    int periodCount = cvBudgetPeriod.size();
                    for(Object budgetPeriodDetails : cvBudgetPeriod){
                        BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)budgetPeriodDetails;
                        Equals eqBudgetPeriod = new Equals("budgetPeriod", budgetPeriodBean.getBudgetPeriod());
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
                                    double unitCostInMap = ((Double)hmLineItem.get(lineItemNumber)).doubleValue();
                                    double sumOfUnitCost = unitCostInMap + budgetFormulatedCostDetailsBean.getCalculatedExpenses();
                                    // Sum up the Calculated cost for the line item number
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
                // Once sync is performed all the periods will be calculated and budget will be saved
                Hashtable htBudgetDetails = calculateAllPeriods(queryEngine.getDataCollection(queryKey),budgetInfoBean);
                saveBudget(htBudgetDetails,request,queryKey);
            }
        }
    }
}
