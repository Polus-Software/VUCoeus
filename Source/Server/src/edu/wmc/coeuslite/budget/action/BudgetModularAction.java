/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * BudgetModularAction.java
 *
 * Created on July 17, 2006, 3:41 PM
 */

package edu.wmc.coeuslite.budget.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetModularBean;
import edu.mit.coeus.budget.bean.BudgetModularIDCBean;
import edu.mit.coeus.budget.bean.BudgetPeriodBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.utils.query.Or;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.mit.coeuslite.utils.CoeusDynaBeansList;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * Class for Budget Modular
 * @author tarique
 */
public class BudgetModularAction extends BudgetBaseAction{
    private static final String BUDGET_MODULAR_LIST = "coeusBudgetModularDynaList";
    private static final String BUDGET_MODULAR_OLD_DATA = "oldBudgetModularData";
    private static final String CUMULATIVE_LIST = "cumulativeData";
    private static final String GET_MODULAR_DATA  = "/getBudgetModularData";
    private static final String DEL_IDC_DATA = "/deleteBudgetModularIDC";
    private static final String SAVE_BUDGET_MOD_DATA = "/saveBudgetModularData";
    private static final String SYNCED_BUDGET_MOD_DATA = "/syncedBudgetModular";
    private static final String ADD_IDC_DATA = "/addBudgetModularIDC";
    private static final String CUMULATIVE_BUDGET_DATA = "/cumulativeBudgetModular";
    private static final String ENTIRE_DC_LESS_FNA = "entireDcLessFNA";
    private static final String ENTIRE_CONS_FNA = "entireConsFNA";
    private static final String ENTIRE_DC = "entireDC";
    private static final String ENTIRE_IDC = "entireIDC";
    private static final String ENTIRE_DC_IDC = "entireDCIDC";
//    private static final String BUDGET_MODULAR_CODE = "B014";
    private static final String BUDGET_PERIOD_DATA = "budgetPeriodCumulativeData";
    private static final String BUDGET_FORM_NAME = "budgetModular";
//    private HttpServletResponse response;
//    private ActionMapping actionMapping;
//    private ActionForm actionForm; -- Commented for removing instance variable -CASE 2960 
//    private HttpServletRequest request;
//    private HttpSession session;
//    private CoeusDynaBeansList coeusBudgetModularDynaList;
    private String navigator = EMPTY_STRING; 
//    private ActionForward actionForward = null;
    /** Creates a new instance of BudgetModularAction */
    public BudgetModularAction() {
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
    public org.apache.struts.action.ActionForward performExecute(ActionMapping actionMapping, 
        ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

//            this.actionForm = actionForm; -- Commented for removing instance variable -CASE 2960
            CoeusDynaBeansList coeusBudgetModularDynaList = (CoeusDynaBeansList)actionForm;
            ActionForward actionForward = getBudgetModularActions(coeusBudgetModularDynaList, 
                                                request, actionMapping, response);       
            return actionForward;
    }
    /**
     * This method will identify which request is comes from which path and
     * navigates to the respective ActionForward
     * @returns ActionForward object
     * @return Action Forward
     * @param coeusBudgetModularDynaList instance of CoeusBudgetModular Form
     * @throws Exception If any excpetion occur
     */
    private ActionForward getBudgetModularActions(CoeusDynaBeansList coeusBudgetModularDynaList,
        HttpServletRequest request, ActionMapping actionMapping,
        HttpServletResponse response) throws Exception{
       //changes for code review
        HttpSession session = request.getSession();
        ActionForward actionForward = null;
       BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
       if(actionMapping.getPath().equals(GET_MODULAR_DATA)){
           navigator = getBudgetModular(coeusBudgetModularDynaList, budgetInfoBean, request);
           request.setAttribute("CLICKED_LINK", session.getAttribute("CLICKED"));
       }else if(actionMapping.getPath().equals(CUMULATIVE_BUDGET_DATA)){
           navigator = getCumulativeBudgetModData(budgetInfoBean, request);
       }else {
            // Check if lock exists or not
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            WebTxnBean webTxnBean = new WebTxnBean();
            String proposalNumber = (String) session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
            LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId()), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.BUDGET_LOCK_STR+lockBean.getModuleNumber(), request);
            if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {            
                if(actionMapping.getPath().equals(SAVE_BUDGET_MOD_DATA)){
                    navigator = saveBudgetModularData(request, response);
                    // Update the proposal hierarchy sync flag
                    updateProposalSyncFlags(request, proposalNumber);                      
                }else if(actionMapping.getPath().equals(ADD_IDC_DATA)){
                    navigator = performAddAction(coeusBudgetModularDynaList, budgetInfoBean, request); 
                    request.setAttribute("dataModified", "modified");
                }else if(actionMapping.getPath().equals(SYNCED_BUDGET_MOD_DATA)){
                    navigator = syncedBudgetModularData(coeusBudgetModularDynaList, budgetInfoBean, request);                    
                }else if(actionMapping.getPath().equals(DEL_IDC_DATA)){
                    navigator = performDeleteAction(budgetInfoBean, request, coeusBudgetModularDynaList, response);           
                }
            } else {
                String errMsg = "release_lock_for";
                ActionMessages messages = new ActionMessages();                
                messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                saveMessages(request, messages);
                return actionForward;
            }
        }
       if(navigator.equals(EMPTY_STRING)){
           return null;
       }
       actionForward = actionMapping.findForward(navigator);
       //setting dynamic request parmater for path 
       ActionForward newActionForward = new ActionForward(actionForward);
       int budgetModPeriod = MAKE_DEFAULT_PERIOD;
       if(request.getParameter("ModularPeriod") != null){
            budgetModPeriod = Integer.parseInt(request.getParameter("ModularPeriod"));
        }
       if(request.getParameter("requestPeriod") != null){
            budgetModPeriod = Integer.parseInt(request.getParameter("requestPeriod"));
        }
        newActionForward.setPath(actionForward.getPath() + "?ModularPeriod=" + budgetModPeriod);
        return newActionForward;
    }
    /**
     * Method to get the Budget Modular IDC data
     * @return String to navigate 
     * @param coeusBudgetModularDynaList instance of CoeusBudgetModular Form
     * @throws Exception if exception occur
     */
    private String getBudgetModular(CoeusDynaBeansList coeusBudgetModularDynaList
        , BudgetInfoBean budgetInfoBean, HttpServletRequest request) throws Exception{
        Map mapMenuList = new HashMap();
        HttpSession session = request.getSession();
        mapMenuList.put("menuItems",CoeusliteMenuItems.BUDGET_MENU_ITEMS);
        mapMenuList.put("menuCode",CoeusliteMenuItems.BUDGET_MODULAR_CODE);
        setSelectedMenuList(request, mapMenuList);
        //setSelectedStatusBudgetMenu(BUDGET_MODULAR_CODE, request);
        
        session.removeAttribute(CUMULATIVE_LIST);
        session.removeAttribute(BUDGET_MODULAR_LIST);
        session.removeAttribute("budgetModPeriodData");
        session.removeAttribute(BUDGET_MODULAR_OLD_DATA);
        session.removeAttribute("budgetModPeriod");
        Map hmModData = getBudgetModularData(budgetInfoBean, request);
        List lstBudgetDCModular = (Vector)hmModData.get(BudgetModularBean.class);
        int budgetModPeriod = MAKE_DEFAULT_PERIOD;
        if(request.getParameter("ModularPeriod") != null){
            budgetModPeriod = Integer.parseInt(request.getParameter("ModularPeriod"));
        }
        if(request.getParameter("requestPeriod") != null){
            budgetModPeriod = Integer.parseInt(request.getParameter("requestPeriod"));
        }
        lstBudgetDCModular = filterBudgetPeriod(budgetModPeriod, (Vector)lstBudgetDCModular);
        List lstBudgetDCMainData = (List)ObjectCloner.deepCopy(lstBudgetDCModular);
        
        if(lstBudgetDCModular == null || lstBudgetDCModular.size() == 0){
            DynaActionForm dynaDCMoudlarForm = coeusBudgetModularDynaList.getDynaForm(request,BUDGET_FORM_NAME);
            dynaDCMoudlarForm.set("proposalNumber", budgetInfoBean.getProposalNumber());
            dynaDCMoudlarForm.set("versionNumber", new Integer(budgetInfoBean.getVersionNumber()));
            dynaDCMoudlarForm.set("budgetPeriod", new Integer(budgetModPeriod));
            dynaDCMoudlarForm.set("directCostFA", new Double(0));
            dynaDCMoudlarForm.set("consortiumFNA", new Double(0));
            dynaDCMoudlarForm.set("totalDirectCost", new Double(0));
            dynaDCMoudlarForm.set("updateTimestamp",prepareTimeStamp().toString());
            dynaDCMoudlarForm.set("acType", TypeConstants.INSERT_RECORD);
            lstBudgetDCModular.add(dynaDCMoudlarForm);
        }
        List lstBudgetMoudlarIDC = (Vector)hmModData.get(BudgetModularIDCBean.class);
        lstBudgetMoudlarIDC = filterBudgetPeriod(budgetModPeriod, (Vector)lstBudgetMoudlarIDC);
        List lstOldIDCData = (List)ObjectCloner.deepCopy(lstBudgetMoudlarIDC);
        if(lstBudgetMoudlarIDC != null && lstBudgetMoudlarIDC.size() > 0){
            for(int idcIndex = 0; idcIndex < lstBudgetMoudlarIDC.size(); idcIndex ++){
                DynaValidatorForm dynaIdcBean = (DynaValidatorForm)lstBudgetMoudlarIDC.get(idcIndex);
                dynaIdcBean.set("awRateNumber",dynaIdcBean.get("rateNumber"));
            }
        }
        List lstPeriod = (Vector)hmModData.get(BudgetPeriodBean.class);
        if(lstPeriod == null){
            lstPeriod = new Vector();
        }
        session.setAttribute("budgetModPeriodData",lstPeriod);
        session.setAttribute("budgetModPeriod", new Integer(budgetModPeriod));
        //Dyna Indexed List start
        coeusBudgetModularDynaList.setList(lstBudgetDCModular);
        coeusBudgetModularDynaList.setBeanList(lstBudgetMoudlarIDC);
        session.setAttribute(BUDGET_MODULAR_LIST, coeusBudgetModularDynaList);
        //Dyna Indexed List end
        //setting old data. This will be compared in JSP for giving save confirmation.
        CoeusDynaBeansList coeusDynaBeanList = new CoeusDynaBeansList();
        coeusDynaBeanList.setList(lstBudgetDCMainData);
        coeusDynaBeanList.setBeanList(lstOldIDCData);
        session.setAttribute(BUDGET_MODULAR_OLD_DATA, coeusDynaBeanList);
        
        return "success";
    }
     /**
     * Method to Add the new Budget Modular IDC data
     * @return String to navigate 
     * @param coeusBudgetModularDynaList instance of CoeusBudgetModular Form
     * @throws Exception if exception occur
     */
    private String performAddAction(CoeusDynaBeansList coeusBudgetModularDynaList 
        , BudgetInfoBean budgetInfoBean, HttpServletRequest request) throws Exception{
      //  setModifiedData();
        List lstIDCData = (Vector)coeusBudgetModularDynaList.getBeanList();
        int budgetPeriod = 0;
        HttpSession session = request.getSession();
        if(request.getParameter("ModularPeriod") != null){
            budgetPeriod = Integer.parseInt(request.getParameter("ModularPeriod"));
        }
        DynaActionForm dynaNewIDCForm 
            = coeusBudgetModularDynaList.getDynaForm(request, BUDGET_FORM_NAME);
        dynaNewIDCForm.set("proposalNumber",budgetInfoBean.getProposalNumber());
        dynaNewIDCForm .set("versionNumber",new Integer(budgetInfoBean.getVersionNumber()));
        dynaNewIDCForm.set("budgetPeriod", new Integer(budgetPeriod));
        dynaNewIDCForm.set("idcRate", new Double(0));
        dynaNewIDCForm.set("idcBase", new Double(0));
        dynaNewIDCForm.set("description", EMPTY_STRING);
        dynaNewIDCForm.set("acType", TypeConstants.INSERT_RECORD);
        dynaNewIDCForm.set("rateNumber",new Integer(MAKE_DEFAULT_PERIOD));
        if(lstIDCData != null && lstIDCData.size() > 0){
            dynaNewIDCForm.set("rateNumber",new Integer(lstIDCData.size() + 1));
        }
        dynaNewIDCForm.set("fundRequested",new Double(0));
        dynaNewIDCForm.set("updateTimestamp",prepareTimeStamp().toString());
        lstIDCData.add(dynaNewIDCForm);
        coeusBudgetModularDynaList.setBeanList(lstIDCData);
        session.setAttribute(BUDGET_MODULAR_LIST, coeusBudgetModularDynaList);
        navigator = "success";
        return navigator;
    }
    /**
     * Method to Delete the Budget Modular IDC data
     * @return String to navigate
     * @throws Exception if exception occur
     */
    private String performDeleteAction(BudgetInfoBean budgetInfoBean,
        HttpServletRequest request, CoeusDynaBeansList coeusBudgetModularDynaList,
        HttpServletResponse response) throws Exception{
        int rateNumber = 0;
        HttpSession session = request.getSession();
        if(request.getParameter("RateNumber") != null ){
            rateNumber = Integer.parseInt(request.getParameter("RateNumber"));
        }
        setModifiedData(session);
        CoeusDynaBeansList coeusDynaBeansList 
            = (CoeusDynaBeansList)session.getAttribute(BUDGET_MODULAR_LIST);
        List lstBudgetModIDCData = coeusDynaBeansList.getBeanList();
        List lstDelIDCData = new Vector();
        WebTxnBean webTxnBean = null;
        String acType = null;
        boolean isDelete = false;
        if(lstBudgetModIDCData != null && lstBudgetModIDCData.size() > 0){
            for(int index = 0; index < lstBudgetModIDCData.size(); index ++){
                DynaValidatorForm dynaValidForm  = (DynaValidatorForm)lstBudgetModIDCData.get(index);
                int dynaRateNumber = ((Integer)dynaValidForm.get("rateNumber")).intValue();
                if(rateNumber == dynaRateNumber){
                    acType = (String)dynaValidForm.get("acType");
                    if(acType == null || acType.equals(EMPTY_STRING)
                                || acType.equals(TypeConstants.UPDATE_RECORD)){
                        dynaValidForm.set("acType", TypeConstants.DELETE_RECORD);
                        lstDelIDCData.add(dynaValidForm);
                        isDelete = true;
                    }
                    lstBudgetModIDCData.remove(index);
                    break;
                }
             }
        }
        if(lstDelIDCData != null && lstDelIDCData.size() > 0){
            webTxnBean = new WebTxnBean();
            DynaValidatorForm dynaDelForm  = (DynaValidatorForm)lstDelIDCData.get(0);
            dynaDelForm.set("avUpdateTimestamp",prepareTimeStamp().toString());
                webTxnBean.getResults(request,"updBudgetModularIDC", dynaDelForm);
             navigator = saveBudgetModularData(request, response);
        }
        //Reset the rateNumber
        if(!isDelete){
            if(lstBudgetModIDCData != null && lstBudgetModIDCData.size() > 0){
                for(int index =0 ; index< lstBudgetModIDCData.size(); index++){
                    DynaActionForm dynaIDC = (DynaActionForm)lstBudgetModIDCData.get(index);
                    dynaIDC.set("rateNumber",new Integer(index + 1));
                }
            }
        }
        
        if(isDelete){
            //data deleted. get the refresh value.
            navigator = getBudgetModular(coeusBudgetModularDynaList, budgetInfoBean, request);
        }
        navigator = "success";
        return navigator;
    }
    /**
     * Method to Save the Budget Modular IDC data
     * @return String to navigate
     * @throws Exception if exception occur
     */
    private String saveBudgetModularData(HttpServletRequest request,
        HttpServletResponse response) throws Exception{
        String url = null;
        HttpSession session = request.getSession();
        RequestDispatcher requestDispatcher = null;
        //set the actypes and modified data
        setModifiedData(session);
        CoeusDynaBeansList coeusBudgetModularDynaList = (CoeusDynaBeansList)session.getAttribute(BUDGET_MODULAR_LIST);
        Map hmCustomData = getCustomBudgetModData(coeusBudgetModularDynaList);
        hmCustomData = validateData(hmCustomData);
        saveFormData(hmCustomData, request);
        if(request.getParameter("Cumulative") != null){
             url = "/cumulativeBudgetModular.do";
             requestDispatcher = request.getRequestDispatcher(url);
             requestDispatcher.forward(request,response);
             navigator = EMPTY_STRING;
        }else{
            System.out.println(request.getParameter("CLICKED_LINK"));
            session.setAttribute("CLICKED", request.getParameter("CLICKED_LINK"));
            navigator = "success";
        }
        return navigator;
    }
    /**
     * Method to Synced the Budget Modular IDC data
     * @return String to navigate 
     * @param coeusBudgetModularDynaList instance of CoeusBudgetModular Form
     * @throws Exception if exception occur
     */
    private String syncedBudgetModularData(CoeusDynaBeansList coeusBudgetModularDynaList
        , BudgetInfoBean budgetInfoBean, HttpServletRequest request) throws Exception{
        Map hmSaveData = getBudgetModularData(budgetInfoBean, request);
        HttpSession session = request.getSession();
        List vecBudgetPeriod = (Vector)session.getAttribute("budgetModPeriodData");
        List lstBudgetModular = (Vector)hmSaveData.get(BudgetModularBean.class);
        List lstBudgetIDCModular = (Vector)hmSaveData.get(BudgetModularIDCBean.class);
        List lstBudgetDCModular = null;
        List lstBudgetMoudlarIDC = null;
        int budgetPeriod = 0;
        if(vecBudgetPeriod != null && vecBudgetPeriod.size() > 0){
            for(int index = 0; index < vecBudgetPeriod.size(); index ++){
                DynaValidatorForm dynaPeriodBean
                    = (DynaValidatorForm)vecBudgetPeriod.get(index);
                budgetPeriod = ((Integer)dynaPeriodBean.get("budgetPeriod")).intValue();
                lstBudgetDCModular = filterBudgetPeriod(budgetPeriod, (Vector)lstBudgetModular);
                if(lstBudgetDCModular != null &&  lstBudgetDCModular.size() > 0){
                    DynaActionForm dynaDCMoudlarForm = (DynaActionForm)lstBudgetDCModular.get(0);
                    dynaDCMoudlarForm.set("acType", TypeConstants.DELETE_RECORD);
                }
                lstBudgetMoudlarIDC = filterBudgetPeriod(budgetPeriod, (Vector)lstBudgetIDCModular);
                if(lstBudgetMoudlarIDC != null && lstBudgetMoudlarIDC.size() > 0){
                    for(int idcIndex = 0; idcIndex < lstBudgetMoudlarIDC.size(); idcIndex ++){
                        DynaValidatorForm dynaIdcBean = (DynaValidatorForm)lstBudgetMoudlarIDC.get(idcIndex);
                        dynaIdcBean.set("awRateNumber",dynaIdcBean.get("rateNumber"));
                        dynaIdcBean.set("acType", TypeConstants.DELETE_RECORD);
                    }//end for
                }//end if
                coeusBudgetModularDynaList.setList(lstBudgetDCModular);
                coeusBudgetModularDynaList.setBeanList(lstBudgetMoudlarIDC);
                Map hmCustomData = getCustomBudgetModData(coeusBudgetModularDynaList);
                saveFormData(hmCustomData, request);
                
            }//end for
        }//end if
        //get the synced data from server
        hmSaveData = getBudgetSyncedModularData(budgetInfoBean, request);
        //setting synced data and saving
        setSyncedModularData(hmSaveData, budgetInfoBean, request, coeusBudgetModularDynaList);
        session.removeAttribute(BUDGET_MODULAR_LIST);
         session.removeAttribute("budgetModPeriodData");
         session.removeAttribute(BUDGET_MODULAR_OLD_DATA);
      //  navigator = getBudgetModular(coeusBudgetModularDynaList);
        return "success";
    }
    /**
     * Method to total of DC and IDC funds
     * @return String to navigate 
     * @throws Exception if exception occur
     */
    private String getCumulativeBudgetModData(BudgetInfoBean budgetInfoBean,
        HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        session.removeAttribute(CUMULATIVE_LIST);
        session.removeAttribute(BUDGET_MODULAR_LIST);
        session.removeAttribute("budgetModPeriodData");
        session.removeAttribute("budgetModPeriod");
        Map hmModData = getBudgetModularData(budgetInfoBean, request);
        List lstBudgetModular = (Vector)hmModData.get(BudgetModularBean.class);
        double dblTotalDCLessFNA = 0;
        double dblTotalConsFNA = 0;
        double dblTotalDC = 0;
        double dblTotalIDC = 0;
        double dblTotalDCNIDC = 0;
        if(lstBudgetModular != null && lstBudgetModular.size() > 0){
            for(int dcIndex = 0; dcIndex < lstBudgetModular.size(); dcIndex++){
                DynaValidatorForm dynaDCForm = (DynaValidatorForm)lstBudgetModular.get(dcIndex);
                dblTotalDCLessFNA = dblTotalDCLessFNA 
                    + ((Double)dynaDCForm.get("directCostFA")).doubleValue();
                dblTotalConsFNA = dblTotalConsFNA 
                    + ((Double)dynaDCForm.get("consortiumFNA")).doubleValue();
               
            }
        }
         dblTotalDC = dblTotalDC + (dblTotalDCLessFNA + dblTotalConsFNA);
        lstBudgetModular = (Vector)hmModData.get(BudgetModularIDCBean.class);
        if(lstBudgetModular != null && lstBudgetModular.size() > 0){
            for(int idcIndex = 0; idcIndex < lstBudgetModular.size(); idcIndex++){
                DynaValidatorForm dynaIDCForm = (DynaValidatorForm)lstBudgetModular.get(idcIndex);
                dblTotalIDC = dblTotalIDC 
                    + ((Double)dynaIDCForm.get("fundRequested")).doubleValue();
            }
        }
        dblTotalDCNIDC = dblTotalDC + dblTotalIDC;
        
        Map hmCumulativeData = new HashMap();
        hmCumulativeData.put(BUDGET_PERIOD_DATA, (Vector)hmModData.get(BudgetPeriodBean.class));
        hmCumulativeData.put(ENTIRE_DC_LESS_FNA, new Double(dblTotalDCLessFNA));
        hmCumulativeData.put(ENTIRE_CONS_FNA, new Double(dblTotalConsFNA));
        hmCumulativeData.put(ENTIRE_DC, new Double(dblTotalDC));
        hmCumulativeData.put(ENTIRE_IDC, new Double(dblTotalIDC));
        hmCumulativeData.put(ENTIRE_DC_IDC, new Double(dblTotalDCNIDC));
        session.setAttribute(CUMULATIVE_LIST, hmCumulativeData);
        return "success";
    }
    /**
     * This method gets Budget Modular data
     * @return map containing budget Modular data
     * @throws Exception If any exception occurs
     */
    private Map getBudgetModularData(BudgetInfoBean budgetInfoBean,
        HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Map hmWebData = new HashMap();
        hmWebData.put("proposalNumber",budgetInfoBean.getProposalNumber());
        hmWebData.put("versionNumber",new Integer(budgetInfoBean.getVersionNumber()));
        
        Map htBudgetData = (java.util.Hashtable)webTxnBean.getResults(request,"getBudgetModular", hmWebData);
        hmWebData = new HashMap();
        hmWebData.put(BudgetPeriodBean.class, 
                    htBudgetData.get("budgetPeriodData") == null ? new Vector(): (Vector)htBudgetData.get("budgetPeriodData"));
        hmWebData.put(BudgetModularBean.class, 
            htBudgetData.get("getBudgetDCModularData") == null ? new Vector(): (Vector)htBudgetData.get("getBudgetDCModularData"));
        hmWebData.put(BudgetModularIDCBean.class, 
            htBudgetData.get("getBudgetModularIDCData") == null ? new Vector(): (Vector)htBudgetData.get("getBudgetModularIDCData"));   
        return hmWebData;
    }
    /**
     * This method gets Budget Synced Modular data
     * @return map containing budget Modular data
     * @throws Exception If any exception occurs
     */
    private Map getBudgetSyncedModularData(BudgetInfoBean budgetInfoBean,
        HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Map hmWebData = new HashMap();
        hmWebData.put("proposalNumber",budgetInfoBean.getProposalNumber());
        hmWebData.put("versionNumber",new Integer(budgetInfoBean.getVersionNumber()));
        
        Map htBudgetData = (java.util.Hashtable)webTxnBean.getResults(request,
                                    "getSyncedBudgetModular", hmWebData);
        hmWebData = new HashMap();
        hmWebData.put(BudgetModularBean.class, 
            htBudgetData.get("getSyncedDCBudgetModularData") == null ? new Vector(): (Vector)htBudgetData.get("getSyncedDCBudgetModularData"));
        hmWebData.put(BudgetModularIDCBean.class, 
            htBudgetData.get("getSyncedBudgetModularIDCData") == null ? new Vector(): (Vector)htBudgetData.get("getSyncedBudgetModularIDCData"));   
        return hmWebData;
    }
    /**
     * Convert dyna bean to custom bean
     * @param coeusDynaBeansList Dyna List class containing the data for DC and IDC
     * @throws Exception if any exception occurs
     * @return a map containing data of Budget Modular
     */    
    private Map getCustomBudgetModData(CoeusDynaBeansList coeusDynaBeansList) throws Exception{
            Map hmBudgetModList = new HashMap();
            List lstBudgetModData = coeusDynaBeansList.getList();
            CoeusVector cvBudgetModular = null;
            CoeusVector cvBudgetIDCModular = null;
            BudgetModularBean budgetModBean = null;
            BudgetModularIDCBean budgetModularIDCBean = null;
            BeanUtilsBean copyBean = null;
            if(lstBudgetModData != null && lstBudgetModData.size() > 0){
                cvBudgetModular = new CoeusVector();
                for(int dcIndex = 0; dcIndex < lstBudgetModData.size(); dcIndex++){
                    DynaValidatorForm dynaDcForm = (DynaValidatorForm)lstBudgetModData.get(dcIndex);
                    copyBean = new BeanUtilsBean();
                    budgetModBean = new BudgetModularBean();
                    copyBean.copyProperties(budgetModBean, dynaDcForm);
                    cvBudgetModular.addElement(budgetModBean);
                }
            }
            lstBudgetModData = coeusDynaBeansList.getBeanList();
            if(lstBudgetModData != null && lstBudgetModData.size() > 0){
                cvBudgetIDCModular = new CoeusVector();
                for(int idcIndex = 0; idcIndex < lstBudgetModData.size(); idcIndex++){
                    DynaValidatorForm dynaIDcForm = (DynaValidatorForm)lstBudgetModData.get(idcIndex);
                    copyBean = new BeanUtilsBean();
                    budgetModularIDCBean = new BudgetModularIDCBean();
                    copyBean.copyProperties(budgetModularIDCBean, dynaIDcForm);
                    cvBudgetIDCModular.addElement(budgetModularIDCBean);
                }
            }
            hmBudgetModList.put(BudgetModularBean.class, 
                cvBudgetModular == null ? new CoeusVector() : cvBudgetModular);
            hmBudgetModList.put(BudgetModularIDCBean.class, 
                cvBudgetIDCModular == null ? new CoeusVector() : cvBudgetIDCModular);
            return hmBudgetModList;
                    
    }
   /**
     * set the Modified values to session
     * @throws Exception if any exception occurs
     */    
    private void setModifiedData(HttpSession session) throws Exception{
        CoeusDynaBeansList coeusDynaBeansList = (CoeusDynaBeansList)session.getAttribute(BUDGET_MODULAR_LIST);
        List lstData = coeusDynaBeansList.getList();
        double dynaCost = 0;
        String desc = null;
        String acType = null;
        if(lstData != null && lstData.size() > 0){
            for(int index = 0; index < lstData.size(); index ++){
                DynaActionForm dynaDCForm = (DynaActionForm)lstData.get(index);
                desc = (String)dynaDCForm.get("description");
                if(desc != null){
                    desc = desc.trim();
                }
                dynaDCForm.set("description",desc);
                try{
//                    dynaCost = Double.parseDouble(((String)dynaDCForm.get("strDirectCostFA")).replaceAll("[$,/,]",EMPTY_STRING));
                    dynaCost = formatStringToDouble((String)dynaDCForm.get("strDirectCostFA"));
                }catch(NumberFormatException ne){
                    dynaCost = 0;
                }
                dynaDCForm.set("directCostFA", new Double(dynaCost));
                try{
//                    dynaCost = Double.parseDouble(((String)dynaDCForm.get("strConsortiumFNA")).replaceAll("[$,/,]",EMPTY_STRING));
                    dynaCost = formatStringToDouble((String)dynaDCForm.get("strConsortiumFNA"));
                }catch(NumberFormatException ne){
                    dynaCost = 0;
                }
                dynaDCForm.set("consortiumFNA", new Double(dynaCost));
                acType = (String)dynaDCForm.get("acType");
                if(acType == null || acType.equals(EMPTY_STRING ) || acType.equals(TypeConstants.UPDATE_RECORD)){
                    dynaDCForm.set("acType", TypeConstants.UPDATE_RECORD);
                }
            }
        }
        lstData = coeusDynaBeansList.getBeanList();
        if(lstData != null && lstData.size() > 0){
            for(int index = 0; index < lstData.size(); index ++){
                DynaActionForm dynaIDCForm = (DynaActionForm)lstData.get(index);
                try{
//                    dynaCost = Double.parseDouble(((String)dynaIDCForm.get("strIdcRate")).replaceAll("[$,/,]",EMPTY_STRING));
                    dynaCost = formatStringToDouble((String)dynaIDCForm.get("strIdcRate"));
                }catch(NumberFormatException ne){
                    dynaCost = 0;
                }
                dynaIDCForm.set("idcRate", new Double(dynaCost));
                try{
//                    dynaCost = Double.parseDouble(((String)dynaIDCForm.get("strIdcBase")).replaceAll("[$,/,]",EMPTY_STRING));
                    dynaCost = formatStringToDouble((String)dynaIDCForm.get("strIdcBase"));
                }catch(NumberFormatException ne){
                    dynaCost = 0;
                }
                dynaIDCForm.set("idcBase", new Double(dynaCost));
                try{
//                    dynaCost = Double.parseDouble(((String)dynaIDCForm.get("strFundRequested")).replaceAll("[$,/,]",EMPTY_STRING));
                    dynaCost = formatStringToDouble((String)dynaIDCForm.get("strFundRequested"));
                }catch(NumberFormatException ne){
                    dynaCost = 0;
                }
                dynaIDCForm.set("fundRequested", new Double(dynaCost));
                acType = (String)dynaIDCForm.get("acType");
                if(acType == null || acType.equals(EMPTY_STRING ) || acType.equals(TypeConstants.UPDATE_RECORD)){
                    dynaIDCForm.set("acType", TypeConstants.UPDATE_RECORD);
                }
            }
        }
    }
    /**
     * Method validate Budget Modular
     * @param hmCustomData Map conatain budget Modualar modified data
     * @throws Exception if any exception occurs
     * @return map conatining Budget Modular Data
     */    
    private Map validateData(Map hmCustomData) throws Exception{
         NotEquals neDesc = new NotEquals("description" , EMPTY_STRING);
         NotEquals neIDCRate = new NotEquals("idcRate" , new Double(0));
         NotEquals neIDCBase = new NotEquals("idcBase" , new Double(0));
         NotEquals neFundReq = new NotEquals("fundRequested" , new Double(0));
         
         Or neDescOrneIDCRate = new Or(neDesc , neIDCRate);
         Or neIDCBaseOrneFundReq = new Or(neIDCBase , neFundReq);
         Or filterOr = new Or(neDescOrneIDCRate , neIDCBaseOrneFundReq);
         List cvIDCData = (CoeusVector)hmCustomData.get(BudgetModularIDCBean.class);
         List cvDCData = (CoeusVector)hmCustomData.get(BudgetModularBean.class);
         double totalDirectCost = 0;
         if(cvDCData != null && cvDCData.size() > 0){
             for(int index = 0; index < cvDCData.size(); index ++){
                 BudgetModularBean budgetModularBean
                    = (BudgetModularBean)cvDCData.get(index);
                 totalDirectCost = budgetModularBean.getDirectCostFA()
                                            + budgetModularBean.getConsortiumFNA();
                 budgetModularBean.setTotalDirectCost(totalDirectCost);
             }
         }
         CoeusVector cvFilterIDC = ((CoeusVector)cvIDCData).filter(filterOr);
         if(cvFilterIDC.size() == 0 && cvDCData.size() > 0 && totalDirectCost == 0){
             BudgetModularBean budgetModularBean = (BudgetModularBean)cvDCData.get(0);
             if(budgetModularBean.getAcType() == null || budgetModularBean.getAcType().equals(EMPTY_STRING) ||
                budgetModularBean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                    budgetModularBean.setAcType(TypeConstants.DELETE_RECORD);
             }else if(budgetModularBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                 budgetModularBean.setAcType(EMPTY_STRING);
             }
         }
         cvIDCData = validateIDCData(cvIDCData);
         int rateNo = 1;
         for(int index =0 ; index< cvIDCData.size(); index++){
             BudgetModularIDCBean budgetModIDCBean =
                    (BudgetModularIDCBean)cvIDCData.get(index);
             if(budgetModIDCBean.getAcType() == null || 
                !budgetModIDCBean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                    budgetModIDCBean.setRateNumber(rateNo);
                    rateNo = rateNo + 1;
             }
             if(budgetModIDCBean.getAcType() == null 
                        || budgetModIDCBean.getAcType().equals(EMPTY_STRING)){
                budgetModIDCBean.setAcType(TypeConstants.UPDATE_RECORD);
             }//End of if
         }//End of for
         hmCustomData.remove(BudgetModularBean.class);
         hmCustomData.remove(BudgetModularIDCBean.class);
         hmCustomData.put(BudgetModularBean.class, cvDCData == null ? new CoeusVector()
            :(CoeusVector)cvDCData);
         hmCustomData.put(BudgetModularIDCBean.class, cvIDCData == null ? new CoeusVector()
            :(CoeusVector)cvIDCData);
         return hmCustomData;
    }
    /**
     * Method to validate IDC Data
     * @param cvIDCData vector containing IDC Data
     * @throws Exception if any exception occurs
     * @return list containing valid IDC
     */    
    private List validateIDCData(List cvIDCData) throws Exception{
        if(cvIDCData != null && cvIDCData.size() > 0){
              Equals eqDesc = new Equals("description" , EMPTY_STRING);
              Equals eqIDCRate = new Equals("idcRate" , new Double(0));
              Equals eqIDCBase = new Equals("idcBase" , new Double(0));
              Equals eqFundReq = new Equals("fundRequested" , new Double(0));
              
              And eqDescAndeqIDCRate = new And(eqDesc , eqIDCRate);
              And eqIDCBaseAndeqFundReq = new And(eqIDCBase , eqFundReq);
              And filterAnd= new And(eqDescAndeqIDCRate , eqIDCBaseAndeqFundReq);
              
              CoeusVector cvBudIDCData = (CoeusVector)cvIDCData;
              CoeusVector cvFilterIDCData = cvBudIDCData.filter(filterAnd);
              if(cvFilterIDCData != null && cvFilterIDCData.size() > 0){
                   for(int index = 0 ; index <cvFilterIDCData.size(); index++){
                       BudgetModularIDCBean budgetModularIDCBean = 
                                        (BudgetModularIDCBean)cvFilterIDCData.get(index);
                       if(budgetModularIDCBean.getAcType() == null || 
                        budgetModularIDCBean.getAcType().equals(EMPTY_STRING) ||
                          budgetModularIDCBean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                              budgetModularIDCBean.setAcType(TypeConstants.DELETE_RECORD);
                       }
                   }//End of for
              }//End of If
              
              CoeusVector cvFilterIDC = new CoeusVector();
              for(int idcIndex =0 ; idcIndex < cvIDCData.size(); idcIndex++){
                   BudgetModularIDCBean budModIDCBean = 
                                        (BudgetModularIDCBean)cvIDCData.get(idcIndex);
                   if(budModIDCBean.getDescription().equals(EMPTY_STRING) &&
                      budModIDCBean.getIdcRate() == 0 &&
                      budModIDCBean.getIdcBase() == 0 && 
                      budModIDCBean.getFundRequested() == 0 && 
                      budModIDCBean.getAcType().equals(TypeConstants.INSERT_RECORD) ){
                          continue;
                   }else{
                       cvFilterIDC.add(budModIDCBean);
                   }
                       
              }
              
              if(cvFilterIDC != null){
                  cvIDCData = cvFilterIDC;
              }
        }
        return cvIDCData;
    }
    /**
     * Method to save Modular Budget Data
     * @param hmDataToSave map conataining data to save
     * @throws Exception if any exception occurs
     */   
    private void saveFormData(Map hmDataToSave, HttpServletRequest request) throws Exception{
        
        CoeusVector cvModBudInsData = null;
        CoeusVector cvModBudDelData = null;
        CoeusVector cvModBudUpdData = null;
        
        CoeusVector cvModBudIDCInsData = null;
        CoeusVector cvModBudIDCDelData = null;
        CoeusVector cvModBudIDCUpdData = null;
        
        CoeusVector modularBudgetVector = (CoeusVector)hmDataToSave.get(BudgetModularBean.class);
        CoeusVector modularBudgetIdcVector = (CoeusVector)hmDataToSave.get(BudgetModularIDCBean.class);
                
        Equals eqInsert = new Equals("acType" , TypeConstants.INSERT_RECORD);
        Equals eqDelete = new Equals("acType" , TypeConstants.DELETE_RECORD);
        Equals eqUpdate = new Equals("acType" , TypeConstants.UPDATE_RECORD);
        
        cvModBudInsData = modularBudgetVector.filter(eqInsert);
        cvModBudDelData = modularBudgetVector.filter(eqDelete);
        cvModBudUpdData = modularBudgetVector.filter(eqUpdate);
        
        cvModBudIDCInsData = modularBudgetIdcVector.filter(eqInsert);
        cvModBudIDCDelData = modularBudgetIdcVector.filter(eqDelete);
        cvModBudIDCUpdData = modularBudgetIdcVector.filter(eqUpdate);
        
            
        //First delete from child table
        updateModularBudgetIdc(cvModBudIDCDelData, request);
        updateModularBudget(cvModBudDelData, request);
        

        //Update the tables
        updateModularBudget(cvModBudUpdData, request);
        updateModularBudgetIdc(cvModBudIDCUpdData, request);
        
         //First insert to the master table
        updateModularBudget(cvModBudInsData, request);
        updateModularBudgetIdc(cvModBudIDCInsData, request);
          
    }
    /**
     * Method to update Modular DC
     * @param cvModBudgetData vector containing data of Budget Modualar DC
     * @throws Exception if any exception occurs
     */    
    private void updateModularBudget(List cvModBudgetData,
        HttpServletRequest request) throws Exception{
        if(cvModBudgetData != null && cvModBudgetData.size() > 0){
            WebTxnBean webTxnBean = null;
            List vecModBudgetData = getBudgetModDynaBean(cvModBudgetData, request);
            for(int index = 0; index < vecModBudgetData.size(); index ++){
                webTxnBean = new WebTxnBean();
                DynaValidatorForm dynaValidForm  = (DynaValidatorForm)vecModBudgetData.get(index);
                dynaValidForm.set("avUpdateTimestamp",prepareTimeStamp().toString());
                webTxnBean.getResults(request,"updBudgetDCModular", dynaValidForm);
            }
        }
    }
    /**
     * Method to save IDC data
     * @param cvModBudgetIDCData vector containing Budget Modualr IDC
     * @throws Exception if any exception occurs
     */    
    private void updateModularBudgetIdc(List cvModBudgetIDCData,
        HttpServletRequest request) throws Exception{
        if(cvModBudgetIDCData != null && cvModBudgetIDCData.size() > 0){
            WebTxnBean webTxnBean = null;
            List vecModBudgetData = getBudgetModIDCDynaBean(cvModBudgetIDCData, request);
            for(int index = 0; index < vecModBudgetData.size(); index ++){
                webTxnBean = new WebTxnBean();
                DynaValidatorForm dynaIDCForm  = (DynaValidatorForm)vecModBudgetData.get(index);
                dynaIDCForm.set("avUpdateTimestamp",prepareTimeStamp().toString());
                webTxnBean.getResults(request,"updBudgetModularIDC", dynaIDCForm);
            }
        }
    }
    /**
     * Method to convert DC custom bean to dyna bean
     * @param lstBudgetModData custom list which has to be converted
     * @throws Exception if any exception occurs
     * @return list containing dyna beans
     */    
    private List getBudgetModDynaBean(List lstBudgetModData,
        HttpServletRequest request) throws Exception{
        List vecBudgetModular = new Vector();
        CoeusDynaBeansList coeusDynaBeansList = null;
        BeanUtilsBean copyBean = null;
        DynaActionForm dynaBean = null;
        if(lstBudgetModData != null && lstBudgetModData.size() > 0){
            coeusDynaBeansList = new CoeusDynaBeansList();
            for(int dcIndex = 0; dcIndex < lstBudgetModData.size(); dcIndex++){
                BudgetModularBean budgetModBean = (BudgetModularBean)lstBudgetModData.get(dcIndex);
                copyBean = new BeanUtilsBean();
                dynaBean = coeusDynaBeansList.getDynaForm(request, BUDGET_FORM_NAME);
                copyBean.copyProperties(dynaBean, budgetModBean);
                vecBudgetModular.add(dynaBean);
            }
        }
        return vecBudgetModular;
    }
    /**
     * Method to convert IDC custom bean to dyna bean
     * @param lstBudgetModIDCData custom list which has to be converted
     * @throws Exception if any exception occurs
     * @return list containing dyna beans
     */    
    private List getBudgetModIDCDynaBean(List lstBudgetModIDCData,
        HttpServletRequest request) throws Exception{
        List vecBudgetIDCModular = new Vector();
        CoeusDynaBeansList coeusDynaBeansList = null;
        BeanUtilsBean copyBean = null;
        DynaActionForm dynaBean = null;
        if(lstBudgetModIDCData != null && lstBudgetModIDCData.size() > 0){
            coeusDynaBeansList = new CoeusDynaBeansList();
            for(int idcIndex = 0; idcIndex < lstBudgetModIDCData.size(); idcIndex++){
                BudgetModularIDCBean budgetModularIDCBean = (BudgetModularIDCBean)lstBudgetModIDCData.get(idcIndex);
                copyBean = new BeanUtilsBean();
                dynaBean = coeusDynaBeansList.getDynaForm(request, BUDGET_FORM_NAME);
                copyBean.copyProperties(dynaBean, budgetModularIDCBean);
                vecBudgetIDCModular.add(dynaBean);
            }
        }
        return vecBudgetIDCModular;
    }
    /**
     * Method to save Synced Modular Budget Data
     * @param hmSaveData map containing Budget Modualar Data for All Budget Periods
     * @throws Exception if any excpetion occurs
     */    
    private void setSyncedModularData(Map hmSaveData, BudgetInfoBean budgetInfoBean,
        HttpServletRequest request, CoeusDynaBeansList coeusBudgetModularDynaList) throws Exception{
        HttpSession session = request.getSession();
        List vecBudgetPeriods = (Vector)session.getAttribute("budgetModPeriodData");
        List lstBudgetModular = (Vector)hmSaveData.get(BudgetModularBean.class);
        List lstBudgetIDCModular = (Vector)hmSaveData.get(BudgetModularIDCBean.class);
        List lstBudgetDCModular = null;
        List lstBudgetMoudlarIDC = null;
        int budgetPeriod = 0;
        CoeusDynaBeansList coeusDynaBeansList ;
        if(vecBudgetPeriods != null && vecBudgetPeriods.size() > 0){
            for(int index = 0; index < vecBudgetPeriods.size(); index ++){
                DynaValidatorForm dynaPeriodBean
                = (DynaValidatorForm)vecBudgetPeriods.get(index);
                budgetPeriod = ((Integer)dynaPeriodBean.get("budgetPeriod")).intValue();
                lstBudgetDCModular = filterBudgetPeriod(budgetPeriod, (Vector)lstBudgetModular);
                if(lstBudgetDCModular == null || lstBudgetDCModular.size() == 0){
                    DynaActionForm dynaDCMoudlarForm = coeusBudgetModularDynaList.getDynaForm(request,BUDGET_FORM_NAME);
                    dynaDCMoudlarForm.set("proposalNumber", budgetInfoBean.getProposalNumber());
                    dynaDCMoudlarForm.set("versionNumber", new Integer(budgetInfoBean.getVersionNumber()));
                    dynaDCMoudlarForm.set("budgetPeriod", new Integer(budgetPeriod));
                    dynaDCMoudlarForm.set("directCostFA", new Double(0));
                    dynaDCMoudlarForm.set("consortiumFNA", new Double(0));
                    dynaDCMoudlarForm.set("totalDirectCost", new Double(0));
                    dynaDCMoudlarForm.set("updateTimestamp",prepareTimeStamp().toString());
                    dynaDCMoudlarForm.set("acType", TypeConstants.INSERT_RECORD);
                    lstBudgetDCModular.add(dynaDCMoudlarForm);
                }else{
                    //set the actype and timestamp
                    DynaActionForm dynaDCMoudlarForm = (DynaActionForm)lstBudgetDCModular.get(0);
                    dynaDCMoudlarForm.set("updateTimestamp",prepareTimeStamp().toString());
                    dynaDCMoudlarForm.set("acType", TypeConstants.INSERT_RECORD);
                    
                }
                lstBudgetMoudlarIDC = filterBudgetPeriod(budgetPeriod, (Vector)lstBudgetIDCModular);
                if(lstBudgetMoudlarIDC != null && lstBudgetMoudlarIDC.size() > 0){
                    for(int idcIndex = 0; idcIndex < lstBudgetMoudlarIDC.size(); idcIndex ++){
                        DynaValidatorForm dynaIdcBean = (DynaValidatorForm)lstBudgetMoudlarIDC.get(idcIndex);
                        dynaIdcBean.set("awRateNumber",dynaIdcBean.get("rateNumber"));
                        dynaIdcBean.set("updateTimestamp",prepareTimeStamp().toString());
                        dynaIdcBean.set("acType", TypeConstants.INSERT_RECORD);
                    }//end for
                }//end if
                coeusDynaBeansList = new CoeusDynaBeansList();
                coeusDynaBeansList.setList(lstBudgetDCModular);
                coeusDynaBeansList.setBeanList(lstBudgetMoudlarIDC);
                Map hmCustomData = getCustomBudgetModData(coeusDynaBeansList);
                hmCustomData = validateData(hmCustomData);
                saveFormData(hmCustomData, request);
                
            }//end for
        }//end if
    }
}
