/*
 * ProjectIncomeAction.java
 *
 * Created on July 17, 2006, 7:15 PM
 */

package edu.wmc.coeuslite.budget.action;

import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetPeriodBean;
import edu.mit.coeus.budget.bean.ProjectIncomeBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeuslite.utils.CoeusDynaBeansList;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author  nandkumarsn
 */
public class BudgetProjectIncomeAction extends BudgetBaseAction {
    
    private static final String GET_PROJECT_INCOME = "/getBudgetProjectIncome";
    private static final String SAVE_PROJECT_INCOME = "/saveBudgetProjectIncome";
    private static final String DELETE_PROJECT_INCOME = "/deleteBudgetProjectIncome";
    private static final String UPDATE_PROJECT_INCOME = "/updateBudgetProjectIncome";
    
    private static final String EMPTY_STRING = "";
    
    //    private HttpServletRequest request;
    //    private HttpSession session ;
    //    private ActionMapping actionMapping;
    //    private ActionForward actionForward;
    
    
    
    /** Creates a new instance of ProjectIncomeAction */
    public BudgetProjectIncomeAction() {
    }
    
    /**
     *
     * @param actionMapping
     * @param actionForm
     * @param request
     * @param response
     * @throws Exception
     * @return
     */
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {
        //        this.request = request ;
        HttpSession session = request.getSession();
        //        this.actionMapping = actionMapping;
        CoeusDynaBeansList coeusDynaBeanList = (CoeusDynaBeansList)actionForm;
        ActionForward actionForward = performBudgetProjectIncomeAction(coeusDynaBeanList, request, actionMapping);
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.BUDGET_MENU_ITEMS);
        mapMenuList.put("menuCode",CoeusliteMenuItems.PROJECT_INCOME_CODE);
        setSelectedMenuList(request, mapMenuList);
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
    
    private ActionForward performBudgetProjectIncomeAction(CoeusDynaBeansList coeusDynaBeansList,
    HttpServletRequest request, ActionMapping actionMapping) throws Exception{
        HttpSession session = request.getSession();
        String navigator = EMPTY_STRING;
        edu.mit.coeus.bean.UserInfoBean userInfoBean = (edu.mit.coeus.bean.UserInfoBean)session.getAttribute("user"+session.getId());
        WebTxnBean webTxnBean = new WebTxnBean();
        if(actionMapping.getPath().equals(GET_PROJECT_INCOME)){
            navigator = getProjectIncomeData(coeusDynaBeansList, userInfoBean, request);
        }else {
            LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId()), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.BUDGET_LOCK_STR+lockBean.getModuleNumber(), request);
            if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
                if(actionMapping.getPath().equals(SAVE_PROJECT_INCOME)){
                    navigator = performSaveAction(coeusDynaBeansList, userInfoBean, request);
                }else if(actionMapping.getPath().equals(UPDATE_PROJECT_INCOME)){
                    navigator = performUpdateAction(coeusDynaBeansList, userInfoBean, request);
                }else if(actionMapping.getPath().equals(DELETE_PROJECT_INCOME)){
                    navigator = performDeleteAction(coeusDynaBeansList, userInfoBean, request);
                }
            } else {
                String errMsg = "release_lock_for";
                ActionMessages messages = new ActionMessages();
                messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                saveMessages(request, messages);
                navigator = "success";
            }
        }
        ActionForward actionForward = actionMapping.findForward(navigator);
        return actionForward;
        
    }
    
    /**
     * This method gets the project income data by calling the getBudgetProjectIncomeData method
     * and calculates the summary data based on budget periods.
     * @param coeusDynaBeansList
     * @throws Exception
     * @return String to navigator
     */
    private String getProjectIncomeData(CoeusDynaBeansList coeusDynaBeansList,
    edu.mit.coeus.bean.UserInfoBean userInfoBean, HttpServletRequest request) throws Exception{
        Map hmProjectData = getBudgetProjectIncomeData(coeusDynaBeansList, request);
        DynaActionForm dynaSummaryForm = null;
        Vector vecBudgetPeriodData = (Vector)hmProjectData.get(BudgetPeriodBean.class);
        List lstProjectIncomeData = (Vector)hmProjectData.get(ProjectIncomeBean.class);
        List lstSummaryData = new ArrayList();
        List lstBudgetPeriods = new ArrayList();
        String userId = userInfoBean.getUserId();
        HttpSession session = request.getSession();
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        
        if(vecBudgetPeriodData !=null && vecBudgetPeriodData.size()>0){
            for(int index=0; index < vecBudgetPeriodData.size(); index++){
                double dblSummaryAmount = 0;
                DynaValidatorForm dynaForm = (DynaValidatorForm)vecBudgetPeriodData.get(index);
                int budgetPeriod = ((Integer)dynaForm.get("budgetPeriod")).intValue();
                if(lstProjectIncomeData != null && lstProjectIncomeData.size()>0){
                    for(int pIndex = 0; pIndex < lstProjectIncomeData.size(); pIndex++){
                        dynaForm = (DynaValidatorForm)lstProjectIncomeData.get(pIndex);
                        int projectIncomePeriod  = ((Integer)dynaForm.get("budgetPeriod")).intValue();
                        if(budgetPeriod == projectIncomePeriod){
                            dblSummaryAmount = dblSummaryAmount + ((Double)dynaForm.get("amount")).doubleValue();
                        }
                    }
                }
                dynaSummaryForm = coeusDynaBeansList.getDynaForm(request,"budgetProjectIncome");
                dynaSummaryForm.set("budgetPeriod",new Integer(budgetPeriod));
                dynaSummaryForm.set("amount",new Double(dblSummaryAmount));
                
                if(dblSummaryAmount > 0){
                    String stramount = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(dblSummaryAmount);
                    dynaSummaryForm.set("strAmount",stramount);
                    lstSummaryData.add(dynaSummaryForm);
                }
            }
        }
        DynaActionForm dynaBudgetPeriodForm = coeusDynaBeansList.getDynaForm(request,"budgetProjectIncome");
        dynaBudgetPeriodForm.set("proposalNumber", budgetInfoBean.getProposalNumber());
        dynaBudgetPeriodForm.set("versionNumber", new Integer(budgetInfoBean.getVersionNumber()));
        dynaBudgetPeriodForm.set("budgetPeriod",new Integer(MAKE_DEFAULT_PERIOD));
        dynaBudgetPeriodForm.set("incomeNumber",new Integer(0));
        dynaBudgetPeriodForm.set("updateTimestamp", prepareTimeStamp().toString());
        dynaBudgetPeriodForm.set("description",EMPTY_STRING);
        dynaBudgetPeriodForm.set("strAmount","0");
        dynaBudgetPeriodForm.set("amount",new Double(0));
        dynaBudgetPeriodForm.set("updateUser",userId);
        dynaBudgetPeriodForm.set("acType",TypeConstants.INSERT_RECORD);
        lstBudgetPeriods.add(dynaBudgetPeriodForm);
        
        coeusDynaBeansList.setList(lstProjectIncomeData);
        String actionFrom = request.getParameter("actionFrom");
        if(actionFrom!=null && !actionFrom.equals(EMPTY_STRING)){
            DynaActionForm dynaPrjIncome = prepareNewDynaValidatorForm(coeusDynaBeansList , dynaBudgetPeriodForm , request);
            request.setAttribute("dataModified", "modified");
            lstProjectIncomeData.add(dynaPrjIncome);
            coeusDynaBeansList.setList(lstProjectIncomeData);
        }
        coeusDynaBeansList.setBeanList(lstBudgetPeriods);
        session.setAttribute("budgetProjectIncomeList", coeusDynaBeansList);
        session.setAttribute("budgetPeriods", vecBudgetPeriodData);
        session.setAttribute("summary", lstSummaryData);
        return "success";
    }
    
    /**
     * This method deletes the seleted row
     * @param coeusDynaBeansList
     * @throws Exception
     * @return String to navigator
     */
    private String performDeleteAction(CoeusDynaBeansList coeusDynaBeansList,
    edu.mit.coeus.bean.UserInfoBean userInfoBean, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        Vector vecProjectIncome = (Vector) coeusDynaBeansList.getList();
        String rowId = request.getParameter("rowId");
        String userId = userInfoBean.getUserId();
        DynaActionForm dynaActionForm =null;
        HashMap hmProjectIncome = new HashMap();
        String proposalNumber = (String) session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
        if(vecProjectIncome !=null && vecProjectIncome.size()>0 && (rowId !=null && !rowId.equals(EMPTY_STRING)) ){
            
            dynaActionForm = (DynaActionForm) vecProjectIncome.get(Integer.parseInt(rowId));
            hmProjectIncome.put("proposalNumber",dynaActionForm.get("proposalNumber"));
            hmProjectIncome.put("description",dynaActionForm.get("description"));
            hmProjectIncome.put("versionNumber",dynaActionForm.get("versionNumber"));
            hmProjectIncome.put("budgetPeriod",dynaActionForm.get("budgetPeriod"));
            String strAmount = ((String)dynaActionForm.get("strAmount")).replaceAll("[$,/,]","");
            hmProjectIncome.put("amount", new Double(strAmount));
            hmProjectIncome.put("incomeNumber",dynaActionForm.get("incomeNumber"));
            hmProjectIncome.put("updateTimestamp",prepareTimeStamp().toString());
            hmProjectIncome.put("awUpdateTimestamp",dynaActionForm.get("updateTimestamp"));
            hmProjectIncome.put("updateUser",userId);
            hmProjectIncome.put("acType", TypeConstants.DELETE_RECORD);
            Hashtable htProjectIncome = (Hashtable) webTxnBean.getResults(request, "updateProjectIncome", hmProjectIncome);
            // Update the proposal hierarchy sync flag
            updateProposalSyncFlags(request, proposalNumber);              
            navigator = "success";
        }
        return navigator;
    }
    
    /**
     * This method saves the newly added row to the database
     * @param coeusDynaBeansList
     * @throws Exception
     * @return String to navigator
     */
    private String performSaveAction(CoeusDynaBeansList coeusDynaBeansList,
    edu.mit.coeus.bean.UserInfoBean userInfoBean, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        boolean value = false;
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        List periodList = (ArrayList)coeusDynaBeansList.getBeanList();
        List projectIncome = (Vector)coeusDynaBeansList.getList();
        String userId = userInfoBean.getUserId();
        DynaActionForm dynaActionForm =null;
        HashMap hmProjectIncome = new HashMap();
        String proposalNumber = (String) session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
        if(periodList !=null && periodList.size()>0){
            dynaActionForm = (DynaActionForm) periodList.get(0);
            if(projectIncome!=null && projectIncome.size() > 0 ){
                String acType = EMPTY_STRING ;
                for(int index = 0 ; index  < projectIncome.size() ; index++){
                    DynaActionForm dynaProjectForm = (DynaActionForm)projectIncome.get(index);
                    value =  validateData(dynaProjectForm, request);
                    if(value){
                        acType = (String)dynaProjectForm.get("acType");
                        if(acType!=null && !acType.equals(EMPTY_STRING)){
                            if(acType.equals(TypeConstants.INSERT_RECORD)){
                                dynaProjectForm.set("updateTimestamp" , prepareTimeStamp().toString());
                                dynaProjectForm.set("awUpdateTimestamp",prepareTimeStamp().toString());
                            }
                        }else{
                            dynaProjectForm.set("awUpdateTimestamp",dynaProjectForm.get("updateTimestamp"));
                            dynaProjectForm.set("acType", TypeConstants.UPDATE_RECORD);
                        }
                        String strAmount = ((String)dynaProjectForm.get("strAmount")).replaceAll("[$,/,]","");
                        dynaProjectForm.set("amount", new Double(strAmount));
                        webTxnBean.getResults(request, "updateProjectIncome", dynaProjectForm);
                        // Update the proposal hierarchy sync flag
                        updateProposalSyncFlags(request, proposalNumber);  
                    }
                }
            }
        }
        navigator = "success";
        if(!value){
            navigator = "Unsuccess";
        }
        return navigator;
    }
    
    /**
     *  This method validates the input data for a new row
     * @param dynaForm
     * @throws Exception
     * @return boolean
     */
    private boolean validateData(DynaActionForm dynaForm, HttpServletRequest request) throws Exception{
        boolean value = true;
        
        String description = (String)dynaForm.get("description");
        if(description != null){
            description = description.trim();
        }
        String amount = (String)dynaForm.get("strAmount");
        ActionMessages actionMessages = new ActionMessages();
        amount = amount.replaceAll("[$,/,]","");
        Object result = GenericTypeValidator.formatDouble(amount);
        
        
        if(result == null ){
            // check for the valid amount
            actionMessages.add("project.income.error.invalidIncome",new ActionMessage("project.income.error.invalidIncome"));
            saveMessages(request, actionMessages);
            value = false;
        }else{
            double dblAmount = Double.parseDouble(result.toString());
            if(dblAmount<=0.0){
                actionMessages.add("project.income.error.invalidIncome",new ActionMessage("project.income.error.invalidIncome"));
                saveMessages(request, actionMessages);
                value = false;
            }
            int pos = amount.indexOf(".");
            String strTempAmt = "";
            pos = amount.indexOf(".");
            if(pos!=-1){
                strTempAmt =  amount.substring(0,pos);
            }else{
                strTempAmt = amount;
            }
            if(strTempAmt.length() > 10 ){
                actionMessages.add("projectIncome.error.maxAmount",new ActionMessage("projectIncome.error.maxAmount"));
                saveMessages(request, actionMessages);
                value = false;
            }
        }
        // check for the empty description
        if(description == null || description.equals(EMPTY_STRING)){
            actionMessages.add("project.income.error.descrequired",new ActionMessage("project.income.error.descrequired"));
            saveMessages(request, actionMessages);
            value = false;
        }
        return value;
    }
    
    /**
     * This method retrives project income data corresponding to a selected proposal number and version number
     * @throws Exception
     * @return
     */
    private Map getBudgetProjectIncomeData(CoeusDynaBeansList coeusDynaBeansList, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        Map hmBudgetProject = new HashMap();
        hmBudgetProject.put("proposalNumber", budgetInfoBean.getProposalNumber());
        hmBudgetProject.put("versionNumber", new Integer(budgetInfoBean.getVersionNumber()));
        
        hmBudgetProject =(Hashtable)webTxnBean.getResults(request,"budgetProjectIncomeData", hmBudgetProject);
        
        if(hmBudgetProject != null && hmBudgetProject.size() > 0){
            
            List lstBudgetPeriodData = (hmBudgetProject.get("budgetPeriodData") == null?new Vector():(Vector)hmBudgetProject.get("budgetPeriodData"));
            List lstProjectIncomeData = (hmBudgetProject.get("getProjectIncome") == null?new Vector():(Vector)hmBudgetProject.get("getProjectIncome"));
            hmBudgetProject = new HashMap();
            hmBudgetProject.put(ProjectIncomeBean.class, lstProjectIncomeData);
            hmBudgetProject.put(BudgetPeriodBean.class, lstBudgetPeriodData);
        }
        return hmBudgetProject;
    }
    
    /**
     * This method updates the modified row.
     * @param coeusDynaBeansList
     * @throws Exception
     * @return String to navigator
     */
    private String performUpdateAction(CoeusDynaBeansList coeusDynaBeansList, edu.mit.coeus.bean.UserInfoBean userInfoBean, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        String navigator = EMPTY_STRING;
        Vector vecProjectIncome = (Vector) coeusDynaBeansList.getList();
        String userId = userInfoBean.getUserId();
        DynaActionForm dynaActionForm =null;
        HashMap hmProjectIncome = new HashMap();
        
        if(vecProjectIncome !=null && vecProjectIncome.size()>0){
            for(int index=0; index<vecProjectIncome.size();index++){
                dynaActionForm = (DynaActionForm) vecProjectIncome.get(index);
                hmProjectIncome.put("proposalNumber",dynaActionForm.get("proposalNumber"));
                hmProjectIncome.put("description",dynaActionForm.get("description"));
                hmProjectIncome.put("versionNumber",dynaActionForm.get("versionNumber"));
                hmProjectIncome.put("budgetPeriod",dynaActionForm.get("budgetPeriod"));
                String strAmount = ((String)dynaActionForm.get("strAmount")).replaceAll("[$,/,]","");
                hmProjectIncome.put("amount", new Double(strAmount));
                hmProjectIncome.put("incomeNumber",dynaActionForm.get("incomeNumber"));
                hmProjectIncome.put("updateTimestamp",prepareTimeStamp().toString());
                hmProjectIncome.put("awUpdateTimestamp",dynaActionForm.get("updateTimestamp"));
                hmProjectIncome.put("updateUser",userId);
                hmProjectIncome.put("acType", TypeConstants.UPDATE_RECORD);
                Hashtable htProjectIncome = (Hashtable) webTxnBean.getResults(request, "updateProjectIncome", hmProjectIncome);
                
            }
            navigator = "success";
        }
        return navigator;
    }
    
    /**
     * This method gets the max income number for a particular proposal number and version number
     * @param proposalNumber
     * @param versionNumber
     * @throws Exception
     * @return int
     */
    private int getMaxIncomeNumber(String proposalNumber, int versionNumber, HttpServletRequest request)throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmMaxIncomeNumber = new HashMap();
        int maxIncomeNumber=0;
        hmMaxIncomeNumber.put("proposalNumber",proposalNumber);
        hmMaxIncomeNumber.put("versionNumber",new Integer(versionNumber));
        Hashtable htProjectIncome = (Hashtable) webTxnBean.getResults(request, "getMaxIncomeNumber", hmMaxIncomeNumber);
        hmMaxIncomeNumber  = (HashMap)htProjectIncome.get("getMaxIncomeNumber");
        if(hmMaxIncomeNumber != null && hmMaxIncomeNumber.size()>0) {
            String maxNumber=(String) hmMaxIncomeNumber.get("max_number");
            maxIncomeNumber = Integer.parseInt(maxNumber);
        }
        return maxIncomeNumber;
    }
    
    /**
     *
     * @param coeusDynaBeansList
     * @param dynaBudgetPeriodForm
     * @throws Exception
     * @return
     */
    private DynaActionForm prepareNewDynaValidatorForm(CoeusDynaBeansList coeusDynaBeansList ,
    DynaActionForm dynaBudgetPeriodForm, HttpServletRequest request)throws Exception{
        
        DynaActionForm dynaProjectIncome = coeusDynaBeansList.getDynaForm(request,"budgetProjectIncome");
        int maxIncomeNumber = getMaxIncomeNumber((String)dynaBudgetPeriodForm.get("proposalNumber"),((Integer)dynaBudgetPeriodForm.get("versionNumber")).intValue(), request);
        dynaProjectIncome.set("proposalNumber",dynaBudgetPeriodForm.get("proposalNumber"));
        dynaProjectIncome.set("description",dynaBudgetPeriodForm.get("description").toString().trim());
        dynaProjectIncome.set("versionNumber",dynaBudgetPeriodForm.get("versionNumber"));
        dynaProjectIncome.set("budgetPeriod",dynaBudgetPeriodForm.get("budgetPeriod"));
        String strAmount = ((String)dynaBudgetPeriodForm.get("strAmount")).replaceAll("[$,/,]","");
        dynaProjectIncome.set("amount", new Double(strAmount.trim()));
        dynaProjectIncome.set("incomeNumber",new Integer(maxIncomeNumber));
        dynaProjectIncome.set("updateTimestamp",prepareTimeStamp().toString());
        dynaProjectIncome.set("awUpdateTimestamp",prepareTimeStamp().toString());
        dynaProjectIncome.set("acType", TypeConstants.INSERT_RECORD);
        return dynaProjectIncome ;
    }
    
    
}
