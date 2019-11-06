/*
 * @(#)AdjustPeriodBoundaryAction.java May 22, 2006, 2:53 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.wmc.coeuslite.budget.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.budget.bean.BudgetDetailBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetModularBean;
import edu.mit.coeus.budget.bean.BudgetModularIDCBean;
import edu.mit.coeus.budget.bean.BudgetPeriodBean;
import edu.mit.coeus.budget.bean.BudgetPersonnelDetailsBean;
import edu.mit.coeus.budget.bean.BudgetPersonsBean;
import edu.mit.coeus.budget.bean.BudgetUpdateTxnBean;
import edu.mit.coeus.budget.bean.ProjectIncomeBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeuslite.utils.CoeusDynaBeansList;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

/**
 * Class to Adjust Budget Period Dates
 * @author tarique
 */
public class AdjustPeriodBoundaryAction extends BudgetBaseAction{
    
    /** Creates a new instance of AdjustPeriodBoundaryAction */
    private static final String GET_ADJUST_PERIOD_DATA = "/getAdjustPeriodData";
    private static final String ADD_PERIOD = "/addAdjustPeriod";
    private static final String SAVE_ADJUST_PERIOD_DATA = "/saveAdjustPeriodData";
    private static final String DELETE_ADJUST_PERIOD_DATA = "/deleteAdjustPeriod";
    private static final String GET_FIELDNAME = "dynaFormData[";
    private static final String START_DATE_AS_REQUEST_PARAM = "].periodStartDate";
    private static final String END_DATE_AS_REQUEST_PARAM = "].periodEndDate";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private static final String BUDGET_SHOULD_HAVE_ONE_PERIOD = "adjustPeriod_exceptionCode.1451";
    private static final String END_DATE_NOT_LATER_THAN_BUDGET ="adjustPeriod_exceptionCode.1455";
    private static final String  START_DATE_NOT_PRIOR_BUDGET_START_DATE = "adjustPeriod_exceptionCode.1456";
    private static final String  START_DATE_PERIOD_LATER_THAN_NEXT_PERIOD = "adjustPeriod_exceptionCode.1457";
    private static final String  HAS_LINE_ITEM_DETAILS = "adjustPeriod_exceptionCode.1458";
    private static final String TOTAL_COST_EXCEDING = "budget_common_exceptionCode.1011";
    private static final String BUDGET_PERIOD = "budgetPeriod";
    private static final String ADJUST_PERIOD_ATTRIBUTE = "adjustBudgetPeriodData";
    private static final String DATE_SEPARATERS = ":/.,|-";
    //Added for Case#2341 - Recalculate Budget if Project dates change
    private static final String CANNOT_ADJUST_PERIOD_BOUNDARIES = "adjustPeriod_exceptionCode.1466";
//    private ActionMessages actionMessages;
//    private ActionMapping actionMapping;
//    private HttpServletRequest request;
//    private HttpServletResponse response;
//    private HttpSession session;
//    private ActionForward actionForward = null;
    private static final String EMPTY_STRING = "";
//    private String navigator = EMPTY_STRING; 
//    private boolean isSaved = false;
//    private CoeusVector cvDeletedPeriods;
//    private CoeusDynaBeansList coeusDynaBeanList;
    // removing instance variables -case # 2960 - start
//    private ActionForm actionForm;
    // removing instance variables - case # 2960 - end
    //COEUSDEV-786 Budget module produces incorrect message when adding an invalid date to a period - Start
    private static final String ADJUST_END_DATE_ATTRIBUTE = "adjustEndPeriodDate";
    private static final String ADJUST_START_DATE_ATTRIBUTE = "adjustStartPeriodDate";
    //COEUSDEV-786 Budget module produces incorrect message when adding an invalid date to a period - End

    /**
     * Creates a new instance of AdjustPeriodBoundary
     */    
    public AdjustPeriodBoundaryAction() {
    }
    /**
     * Method to perform actions on buttons
     * @param actionMapping instance of ActionMapping
     * @param actionForm instance of ActionForm
     * @param request instance of Request
     * @param response instance of Response
     * @throws Exception if exception occur
     * @return instance of ActionForward
     */    
    public org.apache.struts.action.ActionForward performExecute(org.apache.struts.action.ActionMapping actionMapping, org.apache.struts.action.ActionForm actionForm, javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws Exception {

       //        DynaValidatorForm dynaForm = (DynaValidatorForm)actionForm;
        //modified  for removing instance variables - case # 2960 - start
//        this.actionForm = actionForm;
        //modified  for removing instance variables  - case # 2960 - end
        CoeusDynaBeansList coeusDynaBeanList = (CoeusDynaBeansList)actionForm;
        ActionForward actionForward = getAdjustPeriodAction(coeusDynaBeanList,actionMapping,
                                            request);       
        return actionForward;
    }
    /**
     * This method will identify which request is comes from which path and
     * navigates to the respective ActionForward
     * @returns ActionForward object
     * @return Action Forward
     * @param dynaForm instance of DynaValidator Form
     * @throws Exception If any excpetion occur
     */
    private ActionForward getAdjustPeriodAction(CoeusDynaBeansList dynaForm,
       ActionMapping actionMapping, HttpServletRequest request) throws Exception{
       HttpSession session = request.getSession();
       //Added for removing instance variable -case # 2960 - start
        String navigator = EMPTY_STRING;
       //Added for removing instance variable -case # 2960 - end 
        //COEUSDEV-786 Budget module produces incorrect message when adding an invalid date to a period - Start
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        session.setAttribute(ADJUST_END_DATE_ATTRIBUTE+budgetInfoBean.getProposalNumber(), false);
        session.setAttribute(ADJUST_START_DATE_ATTRIBUTE+budgetInfoBean.getProposalNumber(), false);
        //COEUSDEV-786 Budget module produces incorrect message when adding an invalid date to a period - End
       if(actionMapping.getPath().equals(GET_ADJUST_PERIOD_DATA)){
           navigator = getAdjustPeriodData(dynaForm, request);
       }else if(actionMapping.getPath().equals(ADD_PERIOD)){
           navigator = performAddAction(dynaForm, request);
           request.setAttribute("dataModified", "modified");
       }else if(actionMapping.getPath().equals(SAVE_ADJUST_PERIOD_DATA)){
            // Check if lock exists or not
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            WebTxnBean webTxnBean = new WebTxnBean();
            LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId()), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.BUDGET_LOCK_STR+lockBean.getModuleNumber(), request);
            if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) { 
                navigator = saveAdjustPeriodData(dynaForm, request);
            } else {
                String errMsg = "release_lock_for";
                ActionMessages messages = new ActionMessages();                
                messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                saveMessages(request, messages);
            } 
       } else if(actionMapping.getPath().equals(DELETE_ADJUST_PERIOD_DATA)){
           navigator = performDeleteAction(dynaForm, request);
           request.setAttribute("dataModified", "modified");
       }
       ActionForward actionForward = actionMapping.findForward(navigator);
       return actionForward;
    }
    /**
     * Method to get Budget Periods Data
     * @return String to navigator
     * @param dynaForm of Adjust Period Boundary
     * @throws Exception if exception occur
     */
    private String getAdjustPeriodData(CoeusDynaBeansList dynaForm,
        HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        
        String key = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        // modified for removing instance variable - case # 2960 -start
        QueryEngine queryEngine = QueryEngine.getInstance();
        //modified for removing instance variable - case # 2960 - end                
        //If query contain any collection then remove from query engine.
        queryEngine.removeDataCollection(key);
        session.removeAttribute(ADJUST_PERIOD_ATTRIBUTE);
        session.removeAttribute("dynaBeanList");
        //Bug fixed for Budget Modular start 1
        session.removeAttribute("deletedPeriods");
        //Bug fixed for Budget Modular end 1
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmProposalBudgetData = new HashMap();
        hmProposalBudgetData.put("proposalNumber",budgetInfoBean.getProposalNumber());
        hmProposalBudgetData.put("versionNumber",new Integer(budgetInfoBean.getVersionNumber()));
        
        Hashtable htBudgetData =(Hashtable)webTxnBean.getResults(request,
                                        "getBudgetPeriodData", hmProposalBudgetData);
        Vector vecDBBudgetPeriods = (Vector)htBudgetData.get("getBudgetPeriodData");
        //To get the all the budget period in sorted form. 
        ArrayList arListAdjustPeriodData = prepareSortedDynaForms(vecDBBudgetPeriods, dynaForm, request);
        //Added for Case#2341 - Recalculate Budget if Project dates change - starts
        //To generate the periods as per new dates
        String value = request.getParameter("periodsChangeRequired");
        if("true".equals(value)){
            ArrayList newPeriods = getNewPeriods(request, arListAdjustPeriodData, dynaForm);
            if(arListAdjustPeriodData.size() > newPeriods.size()){
                ActionMessages actionMessages = new ActionMessages();
                actionMessages.add(CANNOT_ADJUST_PERIOD_BOUNDARIES , new ActionMessage(
                            CANNOT_ADJUST_PERIOD_BOUNDARIES ) );
                saveMessages(request, actionMessages);
                arListAdjustPeriodData = prepareSortedDynaForms(vecDBBudgetPeriods, dynaForm, request);
            } else {
                Hashtable htBudgetInfoData = (Hashtable)webTxnBean.getResults(request, "getBudgetInfoData" , hmProposalBudgetData );
                Vector vecBudgetInfo = (Vector)htBudgetInfoData.get("getBudgetInfoData");
                DynaValidatorForm dynaFormData = (DynaValidatorForm)vecBudgetInfo.get(0);
                BeanUtilsBean copyBudgetBean  = new BeanUtilsBean();
                BudgetInfoBean budgetInfo = new BudgetInfoBean();
                copyBudgetBean.copyProperties(budgetInfo, dynaFormData);
                Vector vecProposalHeader = (Vector)request.getSession().getAttribute("getProposalHeaderData");
                if(vecProposalHeader != null && vecProposalHeader.size() > 0){
                    EPSProposalHeaderBean proposalHeaderBean = (EPSProposalHeaderBean)vecProposalHeader.elementAt(0);
                    budgetInfo.setStartDate(proposalHeaderBean.getProposalStartDate());
                    budgetInfo.setEndDate(proposalHeaderBean.getProposalEndDate());
                }
                session.setAttribute("budgetInfo", budgetInfo);
                session.setAttribute("PERIOD_ADJUSTED", "PERIOD_ADJUSTED");
                arListAdjustPeriodData = newPeriods;
            }
        } else {
            session.removeAttribute("budgetInfo");
            session.removeAttribute("PERIOD_ADJUSTED");
        }
        //Added for Case#2341 - Recalculate Budget if Project dates change - ends
        session.setAttribute(ADJUST_PERIOD_ATTRIBUTE, arListAdjustPeriodData);
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.BUDGET_MENU_ITEMS);
        mapMenuList.put("menuCode",CoeusliteMenuItems.ADJUST_PERIOD_CODE); 
        setSelectedMenuList(request, mapMenuList);
        
//        setSelectedStatusBudgetMenu(ADJUST_PERIOD_CODE, request);
        dynaForm.setList(arListAdjustPeriodData);
        request.getSession().setAttribute("dynaBeanList" ,dynaForm);
        budgetInfoBean = null;
        arListAdjustPeriodData = null;
        vecDBBudgetPeriods = null;
        htBudgetData = null;
        hmProposalBudgetData = null;
        //Modified for removing instance variable -case # 2960 - start
//        navigator = "success";
        String navigator = "success";
        //Modified for removing instance variable -case # 2960 - end
        return navigator;
    }
    /**
     * Method to Add the new Budget Period
     * @return String
     * @param dynaForm instance of DynaValidator form
     * @throws Exception if exception occur
     */
    private String performAddAction(CoeusDynaBeansList coeusDynaBeanList, HttpServletRequest request) throws Exception{
        ArrayList arLstAdjustPeriodData = getModifiedData(request);
        HttpSession session = request.getSession();
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");        
        DynaActionForm dynaFormData = coeusDynaBeanList.getDynaForm(request,"adjustPeriodBoundary");
        DynaBean dynaNewBean = ((DynaBean)dynaFormData).getDynaClass().newInstance();
        dynaNewBean.set("proposalNumber", budgetInfoBean.getProposalNumber());
        dynaNewBean.set("versionNumber", new Integer(budgetInfoBean.getVersionNumber()));
        dynaNewBean.set(BUDGET_PERIOD, new Integer(arLstAdjustPeriodData.size() + 1));
        dynaNewBean.set("acType", TypeConstants.INSERT_RECORD);
        Timestamp prepareTimestamp = prepareTimeStamp();
        dynaNewBean.set("updateTimestamp",prepareTimestamp.toString());
        arLstAdjustPeriodData.add(dynaNewBean);
        session.setAttribute(ADJUST_PERIOD_ATTRIBUTE, arLstAdjustPeriodData);
        coeusDynaBeanList.setList(arLstAdjustPeriodData);
        request.getSession().setAttribute("dynaBeanList" ,coeusDynaBeanList);
        arLstAdjustPeriodData = null;
        prepareTimestamp = null;
        budgetInfoBean = null;
        //Modified for removing instance variable -case # 2960 - start
//        navigator = "success";
        String navigator = "success";
        //Modified for removing instance variable -case # 2960 - end
        return navigator;
    }
    /**
     * Method to Save the Adjust Period Data
     * @return String
     * @param dynaForm instance of Validator Form
     * @throws Exception if exception occur
     */
     private String saveAdjustPeriodData(CoeusDynaBeansList dynaForm,
        HttpServletRequest request) throws Exception{
        ArrayList arListBudgetPeriodData = null;
        //Added for removing instance variable -case # 2960 - start
        String navigator = "success";
        boolean isSaved = false;
        //Added for removing instance variable -case # 2960 - end                
        HttpSession session = request.getSession();
        String proposalNumber = (String) session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
        if(validateAdjustPeriodBoundary(request)){
            BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
            //Added for removing instance variable -case # 2960 - start
            QueryEngine queryEngine = QueryEngine.getInstance();
            String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
            //Added for removing instance variable - case # 2960- end                          
            boolean dataModified = false;
            Hashtable htBudgetData = initializeBudgetCalculator(budgetInfoBean);
            CoeusVector cvOldBudgetPeriod = (CoeusVector)htBudgetData.get(BudgetPeriodBean.class);
            arListBudgetPeriodData = (ArrayList)session.getAttribute(ADJUST_PERIOD_ATTRIBUTE);
            int oldPeriodSize = cvOldBudgetPeriod.size();
            int newPeriodSize = arListBudgetPeriodData.size();
            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
            CoeusVector cvPreviousBudgetPeriodData = cvOldBudgetPeriod;
            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
            //Check Only Deleted Periods
            if(oldPeriodSize > newPeriodSize){
                dataModified = true;
                cvOldBudgetPeriod = null;
            }
            //Check Dates is Modified or Inserted
            if(!dataModified){
                dataModified = checkDataModified(request);
            }
            if(dataModified){
                //Check deleted period has line items(If Budget period deleted).
                isSaved = hasLineItems(htBudgetData, request);
                
                if(isSaved){
                    //Period has LI . Show Message
                    arListBudgetPeriodData = getModifiedData(request);
                    session.setAttribute(ADJUST_PERIOD_ATTRIBUTE, arListBudgetPeriodData);
                    dynaForm.setList(arListBudgetPeriodData );
                    session.setAttribute("dynaBeanList", dynaForm);
                    navigator = "success";
                    return navigator;
                }
                checkTotalCostLimit(request, htBudgetData);
                queryEngine.addDataCollection(queryKey, htBudgetData);
                //Saving part will start from here
                //Modified for removing instance variable - case # 2960- start                
//                isSaved = adjustBudgetDates(htBudgetData, request);
                //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
                //insert, update the records from the base window
                CoeusVector cvBudgetPersons = queryEngine.executeQuery(queryKey, BudgetPersonsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
                BudgetPeriodBean budgetPeriodBean;
                int budgetPeriod;
                int rowID = 0;
                int DEFAULT_PERIODS = 10;
                //Set the max rowID
                rowID = setMaxRowID(queryKey, rowID);
                if (oldPeriodSize > newPeriodSize) {
                    for(int index = oldPeriodSize -1; index >= newPeriodSize; index--) {
                        budgetPeriodBean = (BudgetPeriodBean) cvPreviousBudgetPeriodData.get(index);
                        if(budgetPeriodBean.getBudgetPeriod() > DEFAULT_PERIODS){
                            continue;
                        }
                        if (cvBudgetPersons != null && cvBudgetPersons.size() > 0) {
                            int size = cvBudgetPersons.size();
                            int personIndex = 0;
                            for(personIndex = 0; personIndex < size; personIndex++) {
                                BudgetPersonsBean persBean = (BudgetPersonsBean) cvBudgetPersons.get(personIndex);
                                // Check whether the last period has line items. If it has show the message.
                                budgetPeriod = budgetPeriodBean.getBudgetPeriod();
                                //to remove the period base salary of the person
                                persBean = updatePersonPeriodBaseSalary(budgetPeriod, persBean);
                                
                                if(persBean.getAcType()!=null){
                                    if (persBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                                        //First delete the existing person and then insert the same. This is
                                        //required since primary keys can be modified
                                        persBean.setAcType(TypeConstants.DELETE_RECORD);
                                        queryEngine.delete(queryKey, persBean);
                                        
                                        persBean.setAcType(TypeConstants.INSERT_RECORD);
                                        persBean.setRowId(rowID++);
                                        queryEngine.insert(queryKey, persBean);
                                    }
                                }
                            }
                        }
                    }
                }
                //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
                isSaved = adjustBudgetDates(htBudgetData, request, queryKey);
                //Modified for removing instance variable - case # 2960- end
                if(isSaved){
                    // Update the proposal hierarchy sync flag
                    updateProposalSyncFlags(request, proposalNumber);                     
                    removeQueryEngineCollection(queryKey);
                    //Refresh the window.
                    navigator = getAdjustPeriodData(dynaForm, request);
                }
            }
        }else{
            //Validation failed. So set the modified data
            arListBudgetPeriodData = getModifiedData(request);
            session.setAttribute(ADJUST_PERIOD_ATTRIBUTE, arListBudgetPeriodData);
            navigator = "success";
        }
        if(!isSaved){
            dynaForm.setList(arListBudgetPeriodData );
            session.setAttribute("dynaBeanList", dynaForm);
        }
        arListBudgetPeriodData = null;
        return navigator;
        
    }
    /**
     * Method to delete Budget Period
     * @throws Exception if exception occur
     * @return String to naviagate
     */
    private String performDeleteAction(CoeusDynaBeansList coeusDynaBeanList, 
        HttpServletRequest request) throws Exception{
        ArrayList arListBudgetPeriodData = getModifiedData(request);
        HttpSession session = request.getSession();
        int budgetPeriod = Integer.parseInt(request.getParameter(BUDGET_PERIOD));
        List lstDelPeriod = (List)session.getAttribute("deletedPeriods");
        String dynaAcType = null;
        if(arListBudgetPeriodData != null && arListBudgetPeriodData.size() > 0){
            //Bug fixed for Budget Modular start 2
            DynaValidatorForm dynaDelForm = (DynaValidatorForm)arListBudgetPeriodData.get(budgetPeriod - 1);
            dynaAcType = (String)dynaDelForm.get("acType");
            if(dynaAcType == null || dynaAcType.equals(EMPTY_STRING)
                    ||dynaAcType.equals(TypeConstants.UPDATE_RECORD)){
                        if(lstDelPeriod != null && lstDelPeriod.size() > 0){
                            lstDelPeriod.add(new Integer(budgetPeriod));
                        }else{
                            lstDelPeriod = new ArrayList();
                            lstDelPeriod.add(new Integer(budgetPeriod));
                        }
                        session.setAttribute("deletedPeriods", lstDelPeriod);
            }
            //Bug fixed for Budget Modular end 2
            arListBudgetPeriodData.remove(budgetPeriod - 1);
            // This logic is used to take n-1 period numbers in the budget period(To Adjust Periods).
            for(int index = budgetPeriod -1; index < arListBudgetPeriodData.size(); index++){
                DynaValidatorForm dynaPeriodBean = (DynaValidatorForm)arListBudgetPeriodData.get(index);
                int dynaBudgetPeriod = ((Integer)dynaPeriodBean.get(BUDGET_PERIOD)).intValue();
                dynaPeriodBean.set(BUDGET_PERIOD,new Integer(dynaBudgetPeriod - 1));
                dynaAcType = (String)dynaPeriodBean.get("acType");
                if(dynaAcType == null || dynaAcType.equals(EMPTY_STRING)
                    || dynaAcType.equals(TypeConstants.UPDATE_RECORD)){
                    dynaPeriodBean.set("acType",TypeConstants.UPDATE_RECORD);
                }
            }
        }
        session.setAttribute(ADJUST_PERIOD_ATTRIBUTE, arListBudgetPeriodData);
        coeusDynaBeanList.setList(arListBudgetPeriodData);
        session.setAttribute("dynaBeanList" ,coeusDynaBeanList);
        arListBudgetPeriodData = null;
        //Modified for removing instance variable -case # 2960 - start
//        navigator = "success";
        String navigator = "success";
        //Modified for removing instance variable -case # 2960 - end
        return navigator;
    }
   /**
     * Method to prepare Sorted dyna Bean of Adjust period Boundary. 
     * @return ArrayList of sorted AdjustPeriodBoundary Dyna Bean 
     * @param cvBudgetPeriod instance of CoeusVector
     * @param dynaForm instance of Validator form
     * @throws Exception if exception occur
     */
    private ArrayList prepareSortedDynaForms(Vector vecBudgetPeriod, CoeusDynaBeansList dynaForm,
        HttpServletRequest request) throws Exception{
        ArrayList arLstAdjustBudgetPeriods = new ArrayList();
        CoeusVector cvBudgetPeriod = new CoeusVector();
        BudgetPeriodBean  budgetPeriodBean = null;
        BeanUtilsBean copyBean = null;
        if(vecBudgetPeriod != null && vecBudgetPeriod.size() > 0){
            for(int index = 0; index < vecBudgetPeriod.size(); index++){
                DynaValidatorForm dynaPeriodForm = (DynaValidatorForm)vecBudgetPeriod.get(index);
                budgetPeriodBean = new BudgetPeriodBean();
                copyBean = new BeanUtilsBean();
                copyBean.copyProperties(budgetPeriodBean, dynaPeriodForm);
                cvBudgetPeriod.addElement(budgetPeriodBean);
                
            }
        }
        vecBudgetPeriod = null;
        budgetPeriodBean = null;
        DateUtils dateUtils = new DateUtils();
        DynaBean newDynaBean = null;
        BeanUtilsBean beanUtils = null;
        String periodStartDate = null;
        String periodEndDate = null;
        if(cvBudgetPeriod != null && cvBudgetPeriod.size() > 0){
            cvBudgetPeriod.sort(BUDGET_PERIOD,true);
            for(int index = 0; index < cvBudgetPeriod.size(); index++){
                budgetPeriodBean = (BudgetPeriodBean)cvBudgetPeriod.get(index);
                periodStartDate = dateUtils.formatDate(budgetPeriodBean.getStartDate().toString(),SIMPLE_DATE_FORMAT);
                periodEndDate = dateUtils.formatDate(budgetPeriodBean.getEndDate().toString(), SIMPLE_DATE_FORMAT); 
                DynaActionForm formData = (DynaActionForm)dynaForm.getDynaForm(request,"adjustPeriodBoundary");
                newDynaBean = ((DynaBean)formData).getDynaClass().newInstance();
                newDynaBean.set("periodStartDate",periodStartDate);
                newDynaBean.set("periodEndDate",periodEndDate);
                beanUtils = new BeanUtilsBean();
                beanUtils.copyProperties(newDynaBean, budgetPeriodBean);
                arLstAdjustBudgetPeriods.add(newDynaBean);
                
            }
            
        }
        cvBudgetPeriod = null;
        dateUtils = null;
        return arLstAdjustBudgetPeriods;
        
    }
    /**
     * Method to get Modified Data 
     * @return Vector of Modified data
     * @throws Exception if exception occur
     */
    private ArrayList getModifiedData(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        ArrayList arListBudgetPeriodData = (ArrayList)session.getAttribute(ADJUST_PERIOD_ATTRIBUTE);
        String periodDate = null;
        String requestDate = null;
        //COEUSDEV-786 Budget module produces incorrect message when adding an invalid date to a period - Start
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        boolean isEndDateAdjusted = (Boolean)session.getAttribute(ADJUST_END_DATE_ATTRIBUTE+budgetInfoBean.getProposalNumber());
        boolean isStartDateAdjusted = (Boolean)session.getAttribute(ADJUST_START_DATE_ATTRIBUTE+budgetInfoBean.getProposalNumber());
        //COEUSDEV-786 Budget module produces incorrect message when adding an invalid date to a period - End
        if(arListBudgetPeriodData != null && arListBudgetPeriodData.size() > 0){
            for(int index = 0; index < arListBudgetPeriodData.size(); index++){
                DynaValidatorForm lstDynaForm = (DynaValidatorForm)arListBudgetPeriodData.get(index);
                periodDate = (String)lstDynaForm.get("periodStartDate");
                requestDate = request.getParameter(GET_FIELDNAME+index+START_DATE_AS_REQUEST_PARAM);
                if(requestDate != null && !periodDate.equals(requestDate)){
                    //lstDynaForm.set("periodStartDate", requestDate);                    
                    if(isStartDateAdjusted){
                       lstDynaForm.set("periodStartDate", periodDate);
                    }else{
                        lstDynaForm.set("periodStartDate", requestDate);
                    }
                }
                periodDate = (String)lstDynaForm.get("periodEndDate");
                requestDate = request.getParameter(GET_FIELDNAME+index+END_DATE_AS_REQUEST_PARAM);
                if(requestDate !=null && !periodDate.equals(requestDate)){
                    //COEUSDEV-786 Budget module produces incorrect message when adding an invalid date to a period - Start
                    //lstDynaForm.set("periodEndDate", requestDate);
                    if(isEndDateAdjusted){
                       lstDynaForm.set("periodEndDate", periodDate);
                    }else{
                        lstDynaForm.set("periodEndDate", requestDate);
                    }
                    //COEUSDEV-786 Budget module produces incorrect message when adding an invalid date to a period - End
                }
            }
            //COEUSDEV-786 Budget module produces incorrect message when adding an invalid date to a period - Start
            session.setAttribute(ADJUST_END_DATE_ATTRIBUTE+budgetInfoBean.getProposalNumber(), false);
            session.setAttribute(ADJUST_START_DATE_ATTRIBUTE+budgetInfoBean.getProposalNumber(), false);
            //COEUSDEV-786 Budget module produces incorrect message when adding an invalid date to a period - Start
        }
        return (arListBudgetPeriodData == null ? new ArrayList():arListBudgetPeriodData);
    }
    /**
     *Method to validate Budget Period Dates
     *@return boolean value to Valid Date\
     *@throws Exception if exception occur
     */
    private boolean validateAdjustPeriodBoundary(HttpServletRequest request) throws Exception{
        ArrayList listBudgetPeriodData = (ArrayList)request.getSession().getAttribute(ADJUST_PERIOD_ATTRIBUTE);
        boolean isValidate = true;
        if(listBudgetPeriodData != null){
            if(listBudgetPeriodData.size() == 0){
                ActionMessages actionMessages = new ActionMessages();
                actionMessages.add(BUDGET_SHOULD_HAVE_ONE_PERIOD , new ActionMessage(
                            BUDGET_SHOULD_HAVE_ONE_PERIOD ) );
                saveMessages(request, actionMessages);
                isValidate = false;
            }else if(!validateAdjustBudgetDate(request)){
                 isValidate = false;
            }
        }
        return isValidate;
    }
    /**
     * Method to validate Budget Period Date and check 
     * When a two Digit year is Entered. This method calculates the year to be as four digit.
     * @return boolean true value to Valid Date
     * @throws Exception if exception occur
     */
    private boolean validateAdjustBudgetDate(HttpServletRequest request) throws Exception{
        boolean isValidate = true;
        DateUtils dateUtils = null;
        ArrayList arLstBudgetPeriod = getModifiedData(request);
        HttpSession session = request.getSession();
        if(arLstBudgetPeriod != null){
            //Modified for Case#2341 - Recalculate Budget if Project dates change - starts
            BudgetInfoBean budgetInfoBean  = null;
            if("PERIOD_ADJUSTED".equals(session.getAttribute("PERIOD_ADJUSTED"))){
                budgetInfoBean  = (BudgetInfoBean)session.getAttribute("budgetInfo");
            } else {
                budgetInfoBean  = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
            }
            //Modified for Case#2341 - Recalculate Budget if Project dates change - ends
            java.sql.Date budgetDate = budgetInfoBean.getStartDate();
            String strRequestDate = request.getParameter(GET_FIELDNAME+0+START_DATE_AS_REQUEST_PARAM);
            
            dateUtils = new DateUtils();
            strRequestDate = dateUtils.formatDate(strRequestDate,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
            java.sql.Date dtStartDate = dateUtils.getSQLDate(strRequestDate);
            if(dtStartDate.before(budgetDate)){
                ActionMessages actionMessages = new ActionMessages();
                actionMessages.add(START_DATE_NOT_PRIOR_BUDGET_START_DATE , new ActionMessage(
                START_DATE_NOT_PRIOR_BUDGET_START_DATE ) );
                saveMessages(request, actionMessages);
                isValidate = false;
                return isValidate;
            }
            budgetDate = budgetInfoBean.getEndDate();
            strRequestDate = request.getParameter(GET_FIELDNAME+0+END_DATE_AS_REQUEST_PARAM);
            strRequestDate = dateUtils.formatDate(strRequestDate,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
            java.sql.Date dtEndDate = dateUtils.getSQLDate(strRequestDate);
            if(dtEndDate.after(budgetDate)){
                ActionMessages actionMessages = new ActionMessages();
                actionMessages.add(END_DATE_NOT_LATER_THAN_BUDGET , new ActionMessage(
                END_DATE_NOT_LATER_THAN_BUDGET ) );
                saveMessages(request, actionMessages);
                isValidate = false;
                return isValidate;
            }
            
            dtStartDate = null;
            dtEndDate = null;
            dateUtils = new DateUtils();
            DynaValidatorForm dynaBudgetPeriodBean , nextDynaBudgetPeriodBean;
            java.sql.Date nextStartDate, nextEndDate;
            //COEUSDEV-786 Budget module produces incorrect message when adding an invalid date to a period - Start
            session.setAttribute(ADJUST_END_DATE_ATTRIBUTE+budgetInfoBean.getProposalNumber(), false);
            session.setAttribute(ADJUST_START_DATE_ATTRIBUTE+budgetInfoBean.getProposalNumber(), false);
            //COEUSDEV-786 Budget module produces incorrect message when adding an invalid date to a period - End
            for(int index = 0; index < arLstBudgetPeriod.size() - 1; index++){
                dynaBudgetPeriodBean = (DynaValidatorForm)arLstBudgetPeriod.get(index);
                strRequestDate = (String)dynaBudgetPeriodBean.get("periodEndDate");
                //COEUSDEV-786 Budget module produces incorrect message when adding an invalid date to a period - Start
                String strInitialReqDate = strRequestDate;
                //COEUSDEV-786 Budget module produces incorrect message when adding an invalid date to a period - End
                strRequestDate = dateUtils.formatDate(strRequestDate, DATE_SEPARATERS, SIMPLE_DATE_FORMAT);                
                //COEUSDEV-786 Budget module produces incorrect message when adding an invalid date to a period - Start
                if(!strRequestDate.equals(strInitialReqDate)){
                    dynaBudgetPeriodBean.set("periodEndDate", strRequestDate);
                    java.sql.Date changeDate = dateUtils.getSQLDate(strRequestDate);
                    dynaBudgetPeriodBean.set("endDate", changeDate);
                    arLstBudgetPeriod.remove(index);
                    arLstBudgetPeriod.add(index, dynaBudgetPeriodBean);
                    session.setAttribute(ADJUST_PERIOD_ATTRIBUTE, arLstBudgetPeriod);
                    session.setAttribute(ADJUST_END_DATE_ATTRIBUTE+budgetInfoBean.getProposalNumber(), true);
                }
                //COEUSDEV-786 Budget module produces incorrect message when adding an invalid date to a period - End
                dtEndDate = dateUtils.getSQLDate(strRequestDate);
                
                nextDynaBudgetPeriodBean = (DynaValidatorForm)arLstBudgetPeriod.get(index + 1);
                
                strRequestDate = (String)nextDynaBudgetPeriodBean.get("periodEndDate");
                strRequestDate = dateUtils.formatDate(strRequestDate, DATE_SEPARATERS, SIMPLE_DATE_FORMAT);
                nextEndDate = dateUtils.getSQLDate(strRequestDate);
                
                strRequestDate = (String)nextDynaBudgetPeriodBean.get("periodStartDate");
                //COEUSDEV-786 Budget module produces incorrect message when adding an invalid date to a period - Start
                String strStartReqDate = strRequestDate;
                //COEUSDEV-786 Budget module produces incorrect message when adding an invalid date to a period - End
                strRequestDate = dateUtils.formatDate(strRequestDate, DATE_SEPARATERS, SIMPLE_DATE_FORMAT);
                //COEUSDEV-786 Budget module produces incorrect message when adding an invalid date to a period - Start
                if(!strRequestDate.equals(strStartReqDate)){
                    nextDynaBudgetPeriodBean.set("periodStartDate", strRequestDate);
                    java.sql.Date changeDate = dateUtils.getSQLDate(strRequestDate);
                    nextDynaBudgetPeriodBean.set("startDate", changeDate);
                    arLstBudgetPeriod.remove(index+1);
                    arLstBudgetPeriod.add(index+1, nextDynaBudgetPeriodBean);
                    session.setAttribute(ADJUST_PERIOD_ATTRIBUTE, arLstBudgetPeriod);
                    session.setAttribute(ADJUST_START_DATE_ATTRIBUTE+budgetInfoBean.getProposalNumber(), true);
                }
                //COEUSDEV-786 Budget module produces incorrect message when adding an invalid date to a period - End
                nextStartDate = dateUtils.getSQLDate(strRequestDate);
                
                if(nextStartDate.compareTo(dtEndDate) <= 0){
                    ActionMessages actionMessages = new ActionMessages();
                    ActionMessage actionMsg = new ActionMessage(START_DATE_PERIOD_LATER_THAN_NEXT_PERIOD,  
                            nextDynaBudgetPeriodBean.get(BUDGET_PERIOD), dynaBudgetPeriodBean.get(BUDGET_PERIOD));
                    actionMessages.add(START_DATE_PERIOD_LATER_THAN_NEXT_PERIOD , actionMsg );
                    saveMessages(request, actionMessages);
                    isValidate = false;
                    break;
                }
                if(index == arLstBudgetPeriod.size() - 2 && nextEndDate.after(budgetDate)){
                    ActionMessages actionMessages = new ActionMessages();
                    actionMessages.add(END_DATE_NOT_LATER_THAN_BUDGET , new ActionMessage(
                    END_DATE_NOT_LATER_THAN_BUDGET ) );
                    saveMessages(request, actionMessages);
                    isValidate = false;
                    break;
                  
                }
            }
            dateUtils = null;
            nextStartDate = null;
            nextEndDate = null;
            dynaBudgetPeriodBean = null;
            nextDynaBudgetPeriodBean = null;
         }
        
        arLstBudgetPeriod = null;
        return isValidate;
    }
    /**
     * Method to check deleted period has any line items.
     * @return boolean value
     * @param htBudgetData instance of hashtable containing Whole Budget Details
     * @throws Exception if any error occur
     */
    private boolean hasLineItems(Hashtable htBudgetData, HttpServletRequest request) throws Exception{
        boolean isDeletedPeriod = false;
        CoeusVector cvDeletedPeriods = new CoeusVector();
        HttpSession session = request.getSession();
        CoeusVector cvBudgetDetails = (CoeusVector)htBudgetData.get(BudgetDetailBean.class);
        CoeusVector cvOldBudgetPeriod = (CoeusVector)htBudgetData.get(BudgetPeriodBean.class);
        ArrayList arLstPeriod = (ArrayList)session.getAttribute(ADJUST_PERIOD_ATTRIBUTE);
        int oldPeriodSize = cvOldBudgetPeriod.size();
        int newPeriodSize = arLstPeriod.size();
        int budgetPeriod = 0;
        BudgetPeriodBean budgetPeriodBean;
        if (oldPeriodSize > newPeriodSize) {
           for(int index = oldPeriodSize -1; index >= newPeriodSize; index--) {
                budgetPeriodBean = (BudgetPeriodBean) cvOldBudgetPeriod.get(index);
                // Check whether the last period has line items. If it has show the message.
                budgetPeriod = budgetPeriodBean.getBudgetPeriod();
                CoeusVector cvFilterLIDetails
                = cvBudgetDetails.filter(new Equals(BUDGET_PERIOD, new Integer(budgetPeriod)));
                if(cvFilterLIDetails.size() > 0){
                    ActionMessages actionMessages = new ActionMessages();
                    ActionMessage actionMsg
                    = new ActionMessage(HAS_LINE_ITEM_DETAILS, new Integer(budgetPeriod),
                    new Integer(budgetPeriod));
                    actionMessages.add(HAS_LINE_ITEM_DETAILS , actionMsg );
                    saveMessages(request, actionMessages);
                    isDeletedPeriod = true;
                    cvFilterLIDetails = null;
                    break;
                }
                budgetPeriodBean.setAcType(TypeConstants.DELETE_RECORD);
                cvDeletedPeriods.add(budgetPeriodBean);
            }
        }
        htBudgetData.put("deletedPeriods", cvDeletedPeriods);
        return isDeletedPeriod;
}
    /** Adjust the dates in BudgetPeriod, Budget Line Item, Personnel Budget.
     *If the old period changed to new Period, adjust the dates in Line item dates,
     *Personnel Line Iten dates.Delete, Update or insert the dates as dates are
     *changed in the adjust period.
     *@param htBudgetData of Budget Information
     *@return boolean to tell date is adjusted and saved to DB
     *@throws Exception if error occurs
     */
    public boolean adjustBudgetDates(Hashtable htBudgetData, 
        HttpServletRequest request, String queryKey) throws Exception{
            CoeusVector cvModifiedBudgetPeriods = getModifiedBudgetPeriods(request);
            
            CoeusVector cvLineItemDetails = new CoeusVector();
            CoeusVector cvPersonnelDetails = new CoeusVector();
            CoeusVector cvTotalLineItemDetails = new CoeusVector();
            CoeusVector cvTotalPersonnelDetails = new CoeusVector();
            // modified for removing instance variable - case # 2960- start
            QueryEngine queryEngine = QueryEngine.getInstance();
            boolean isSaved = false;
            //modified for removing instance variable - case # 2960 - end            
            CoeusVector cvOldPeriodDetails = (CoeusVector)htBudgetData.get(BudgetPeriodBean.class);
            Calendar calendar = Calendar.getInstance();
            
            long periodAndLineItemStDateDifference;
            long oldLiEndDateDifference;
            long liAndPersonnelStDateDifference;
            long personnelStDateEndDateDiff;
            
            int periodSize = cvModifiedBudgetPeriods.size();
            BudgetPeriodBean newBudgetPeriodBean;
            BudgetPeriodBean oldBudgetPeriodBean;
            BudgetDetailBean budgetDetailBean;
            BudgetPersonnelDetailsBean personnelDetailsBean;
          
            int budgetPeriod = 0;
            Date oldPeriodStDate, oldPeriodEndDate, newPeriodStDate, newPeriodEndDate;
            Date oldLiStDate, oldLiEndDate;
            java.util.Date newLiStDate = null;
            java.util.Date newLiEndDate = null;
            Date oldPersonnelStDate, oldPersonnelEndDate;
            java.util.Date newPersonnelStDate = null;
            java.util.Date newPersonnelEndDate = null;
            for (int periodIndex = 0; periodIndex < periodSize; periodIndex++) {
                
                newBudgetPeriodBean = (BudgetPeriodBean) cvModifiedBudgetPeriods.get(periodIndex);
                newPeriodStDate = newBudgetPeriodBean.getStartDate();
                newPeriodEndDate = newBudgetPeriodBean.getEndDate();
                
                budgetPeriod = newBudgetPeriodBean.getBudgetPeriod();
                //Check whether the period has line items
                cvLineItemDetails = queryEngine.getActiveData(queryKey, 
                    BudgetDetailBean.class, new Equals(BUDGET_PERIOD, new Integer(budgetPeriod)));
                if (cvLineItemDetails != null && cvLineItemDetails.size() > 0) {
                    //Get the old budget period
                    oldBudgetPeriodBean = (BudgetPeriodBean) cvOldPeriodDetails.get(periodIndex);
                    oldPeriodStDate = oldBudgetPeriodBean.getStartDate();
                    oldPeriodEndDate = oldBudgetPeriodBean.getEndDate();
                    int liSize = cvLineItemDetails.size();
                    for (int liIndex = 0; liIndex < liSize; liIndex++) {
                        budgetDetailBean = (BudgetDetailBean) cvLineItemDetails.get(liIndex);
                        oldLiStDate = budgetDetailBean.getLineItemStartDate();
                        oldLiEndDate = budgetDetailBean.getLineItemEndDate();
                        //set New period start and End Date
                        if (oldPeriodStDate.compareTo(oldLiStDate) == 0 &&
                            oldPeriodEndDate.compareTo(oldLiEndDate) == 0) {
                            newLiStDate = newPeriodStDate;
                            newLiEndDate = newPeriodEndDate;
                        } else {
                            /** get the Period and Line item start dates difference and 
                             *add this to the new Period start date and set the new start date
                             *to the new line item start date */
                            periodAndLineItemStDateDifference = (oldLiStDate.getTime() - oldPeriodStDate.getTime()) / 86400000;
                            calendar.setTime(newPeriodStDate);
                            calendar.add(Calendar.DATE, (int)periodAndLineItemStDateDifference);
                            newLiStDate = calendar.getTime();
                            if (newLiStDate.compareTo(newPeriodEndDate) > 0) {
                                newLiStDate = newPeriodStDate;
                            }
                            //Set Line Item End Date
                            oldLiEndDateDifference = (oldLiEndDate.getTime() - oldLiStDate.getTime()) / 86400000;
                            calendar.setTime(newLiStDate);
                            calendar.add(Calendar.DATE, (int) oldLiEndDateDifference);
                            newLiEndDate = calendar.getTime();
                            if (newLiEndDate.compareTo(newPeriodEndDate) > 0) {
                                newLiEndDate = newPeriodEndDate;
                            }
                            
                        }
                        //Set the line item with new Start Date & End Dates
                        budgetDetailBean.setLineItemStartDate(new java.sql.Date(newLiStDate.getTime()));
                        budgetDetailBean.setLineItemEndDate(new java.sql.Date(newLiEndDate.getTime()));
                        
                        //Check whether the line item has personnel details
                        //Added for Case#2341 - Recalculate Budget if Project dates change - starts
                         And andPeriodAndLineItem = new And(new Equals("lineItemNumber", 
                                 new Integer(budgetDetailBean.getLineItemNumber())), new Equals("budgetPeriod", 
                                 new Integer(budgetDetailBean.getBudgetPeriod())));
//                        cvPersonnelDetails = queryEngine.getActiveData(queryKey, 
//                            BudgetPersonnelDetailsBean.class, new Equals("lineItemNumber",
//                                            new Integer(budgetDetailBean.getLineItemNumber())));
                        cvPersonnelDetails = queryEngine.getActiveData(queryKey, 
                            BudgetPersonnelDetailsBean.class, andPeriodAndLineItem);
                        //Added for Case#2341 - Recalculate Budget if Project dates change - ends
                        if (cvPersonnelDetails != null && cvPersonnelDetails.size() > 0) {
                            int personnelSize = cvPersonnelDetails.size();
                            for (int personnelIndex = 0; personnelIndex < personnelSize; personnelIndex++) {
                                personnelDetailsBean = (BudgetPersonnelDetailsBean) cvPersonnelDetails.get(personnelIndex);
                                oldPersonnelStDate = personnelDetailsBean.getStartDate();
                                oldPersonnelEndDate = personnelDetailsBean.getEndDate();
                                
                                if(oldLiStDate.compareTo(oldPersonnelStDate) == 0 &&
                                        oldLiEndDate.compareTo(oldPersonnelEndDate) == 0) {
                                            newPersonnelStDate = newLiStDate;
                                            newPersonnelEndDate = newLiEndDate;
                                } else {
                                    //Set Personnel Line Item Start Date
                                    liAndPersonnelStDateDifference = (oldPersonnelStDate.getTime() - oldLiStDate.getTime()) / 86400000;
                                    calendar.setTime(newLiStDate);
                                    calendar.add(Calendar.DATE, (int)liAndPersonnelStDateDifference);
                                    newPersonnelStDate = calendar.getTime();

                                    if(newPersonnelStDate.compareTo(newLiEndDate) > 0) {
                                        newPersonnelStDate = newLiStDate;
                                    }
                                    //Set Personnel Line Item End Date
                                    personnelStDateEndDateDiff = (oldPersonnelEndDate.getTime() - oldPersonnelStDate.getTime()) / 86400000;
                                    calendar.setTime(newPersonnelStDate);
                                    calendar.add(Calendar.DATE, (int) personnelStDateEndDateDiff);
                                    newPersonnelEndDate = calendar.getTime();
                                    if(newPersonnelEndDate.compareTo(newLiEndDate) > 0) {
                                        newPersonnelEndDate = newLiEndDate;
                                    }
                                }
                                //Set the Personnel line item with new Start Date & End Dates
                                personnelDetailsBean.setStartDate(new java.sql.Date(newPersonnelStDate.getTime()));
                                personnelDetailsBean.setEndDate(new java.sql.Date(newPersonnelEndDate.getTime()));
                            }
                            if(cvPersonnelDetails.size() > 0) {
                                cvTotalPersonnelDetails.addAll(cvPersonnelDetails);
                            }
                        }
                    }
                    if (cvLineItemDetails.size() > 0) {
                        cvTotalLineItemDetails.addAll(cvLineItemDetails);
                    }
                }
            }
            //Modified for removing instance variable - case # 2960- start                
//           isSaved = updateBudgetDates(cvTotalLineItemDetails,cvTotalPersonnelDetails, 
//                                    cvModifiedBudgetPeriods, request);
            CoeusVector cvDeletedPeriods = (CoeusVector) htBudgetData.get("deletedPeriods");
           isSaved = updateBudgetDates(cvTotalLineItemDetails,cvTotalPersonnelDetails, 
                                    cvModifiedBudgetPeriods, request, queryKey, cvDeletedPeriods);
            //Modified for removing instance variable - case # 2960- end             
           cvLineItemDetails = null;
           cvPersonnelDetails = null;
           cvTotalLineItemDetails = null;
           cvTotalPersonnelDetails = null;
           return isSaved;
       
    }
    /**
     * Method to Update All Budget Dates and Save the Budget
     * @param cvLineItemDetails contain all LineItem details
     * @param cvPersonnelDetails contain all Personnel LineItem details
     * @param cvModifiedBudgetPeriods contain all modified budget Periods
     * throws exception if any error occur
     * @throws Exception If any excpetion occur
     * @return boolean value if dates is updated
     */
    private boolean updateBudgetDates(CoeusVector cvLineItemDetails,CoeusVector cvPersonnelDetails,
            CoeusVector cvModifiedBudgetPeriods, HttpServletRequest request, 
            String queryKey, CoeusVector cvDeletedPeriods) throws Exception{        
        // modified for removing instance variable - case # 2960 - start
        QueryEngine queryEngine = QueryEngine.getInstance();
        boolean isSaved = false;
        //modified for removing instance variable - case # 2960- end
        //Combine deleted vector with updated vector
        if(cvDeletedPeriods != null && cvDeletedPeriods.size() > 0){
            cvModifiedBudgetPeriods.addAll(cvDeletedPeriods);
        }
        /*setting the acTypes. acType come from dyna bean is empty and queryEngine check only actype == null
        or actype is Update. So setting actype as Update. */
        if(cvModifiedBudgetPeriods != null && cvModifiedBudgetPeriods.size() > 0) {
            for (int index = 0; index < cvModifiedBudgetPeriods.size(); index++) {
                BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean) cvModifiedBudgetPeriods.get(index);
                String acType = budgetPeriodBean.getAcType();
                if (acType == null || acType.equals(EMPTY_STRING)) {
                    budgetPeriodBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
            }
        }
        updateProjectIncomeDetails(cvModifiedBudgetPeriods);
        //bug fixed for budget modular start 3
        //Modified for removing instance variable -case # 2960 - start
//        updateBudgetModular(request);
        updateBudgetModular(request, queryKey);
        //Modified for removing instance variable - case # 2960- end          
        //bug fixed for budget modular end 3
        updateBudgetModularDetails(cvModifiedBudgetPeriods);
        //Now Update the budget period bean in query engine
        String acType = null;
        if (cvModifiedBudgetPeriods != null && cvModifiedBudgetPeriods.size() > 0) {
            BudgetPeriodBean budgetPeriodBean;
            for (int index = 0; index < cvModifiedBudgetPeriods.size(); index++) {
                budgetPeriodBean = (BudgetPeriodBean) cvModifiedBudgetPeriods.get(index);
                acType = budgetPeriodBean.getAcType();
                if (acType == null || acType.equals(TypeConstants.UPDATE_RECORD)) {
                    queryEngine.update(queryKey,budgetPeriodBean);
                }else if (acType.equals(TypeConstants.INSERT_RECORD)) {
                    queryEngine.insert(queryKey,budgetPeriodBean);
                } else if (acType.equals(TypeConstants.DELETE_RECORD)) {
                    queryEngine.delete(queryKey,budgetPeriodBean);
                }
            }
        }
        if(cvLineItemDetails!=null && cvLineItemDetails.size() > 0){
            for(int liIndex = 0 ; liIndex < cvLineItemDetails.size(); liIndex++){
                BudgetDetailBean budgetDetailBean = (BudgetDetailBean)cvLineItemDetails.get(liIndex);
                acType = budgetDetailBean.getAcType();
                if(acType == null || acType.equals(TypeConstants.UPDATE_RECORD)){
                    queryEngine.update(queryKey, budgetDetailBean);
                } else if(acType.equals(TypeConstants.INSERT_RECORD)){
                    queryEngine.insert(queryKey, budgetDetailBean);
                }
            }
        }
        if(cvPersonnelDetails!=null || cvPersonnelDetails.size() > 0){
            for(int pIndex = 0 ; pIndex < cvPersonnelDetails.size(); pIndex++){
                BudgetPersonnelDetailsBean budgetPersonnelDetailsBean =
                (BudgetPersonnelDetailsBean)cvPersonnelDetails.get(pIndex);
                acType = budgetPersonnelDetailsBean.getAcType();
                if(acType == null || acType.equals(TypeConstants.UPDATE_RECORD)){
                    queryEngine.update(queryKey, budgetPersonnelDetailsBean);
                }else if(acType.equals(TypeConstants.INSERT_RECORD)){
                    queryEngine.insert(queryKey, budgetPersonnelDetailsBean);
                }
            }
        }
        isSaved = saveBudget(request);
        return isSaved;
    }
    /**
     * Saving budget to Database
     * @throws Exception if any exception occur
     * @return boolean value Budget is saved or not.
     */    
    private boolean saveBudget(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        //Added for removing instance variable -case # 2960 - start
        QueryEngine queryEngine = QueryEngine.getInstance();
        String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        boolean isSaved = false;
        //Added for removing instance variable - case # 2960- end   
        //Added for Case#2341 - Recalculate Budget if Project dates change - starts
        if("PERIOD_ADJUSTED".equals(session.getAttribute("PERIOD_ADJUSTED"))){
            budgetInfoBean  = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
            budgetInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
            queryEngine.addData(queryKey, budgetInfoBean);
        } else {
            budgetInfoBean  = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        }        
        //Added for Case#2341 - Recalculate Budget if Project dates change - ends
        Hashtable htBudgetData = queryEngine.getDataCollection(queryKey);        
        htBudgetData = calculateAllPeriods(htBudgetData, budgetInfoBean);
        boolean isExceeding = checkTotalCost(htBudgetData);
        if(isExceeding){
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(TOTAL_COST_EXCEDING , new ActionMessage(
                                                    TOTAL_COST_EXCEDING ) );
            saveMessages(request, actionMessages);
            isSaved = false;
            return isSaved;
        }
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        BudgetUpdateTxnBean budgetUpdateTxnBean = new BudgetUpdateTxnBean(userId);
        isSaved = budgetUpdateTxnBean.addUpdDeleteBudget(htBudgetData);
        budgetUpdateTxnBean = null;
        budgetInfoBean = null;
        htBudgetData = null;
        userInfoBean = null;
        userId = null;
        return isSaved;
    }
    /** Method to Update Project Income Details If any Period is added or Deleted.
     *@param cvModifiedBudgetPeriods vector of all Periods
     *@throws Exception if Exception occur
     */
    private void updateProjectIncomeDetails(CoeusVector cvModifiedBudgetPeriods) throws Exception{
        CoeusVector cvProjectIncome = new CoeusVector();
        BudgetPeriodBean budgetPeriodBean;
        String acType = null;        
        // modified for removing instance variable - case # 2960 - start
        QueryEngine queryEngine = QueryEngine.getInstance();
        //modified for removing instance variable - case # 2960 - end        
        if (cvModifiedBudgetPeriods != null && cvModifiedBudgetPeriods.size() > 0) {
            for (int index = 0; index < cvModifiedBudgetPeriods.size(); index++) {
                budgetPeriodBean = (BudgetPeriodBean) cvModifiedBudgetPeriods.get(index);
                acType = budgetPeriodBean.getAcType();
                //Added for removing instance variable -case # 2960 - start
                String queryKey = budgetPeriodBean.getProposalNumber()+budgetPeriodBean.getVersionNumber();
                //Added for removing instance variable - case # 2960- end                                   
                cvProjectIncome = queryEngine.getActiveData(queryKey,
                    ProjectIncomeBean.class,new Equals(BUDGET_PERIOD, new Integer(budgetPeriodBean.getBudgetPeriod())));
                if (cvProjectIncome != null && cvProjectIncome.size() > 0) {
                    for (int incomeIndex = 0; incomeIndex  < cvProjectIncome.size(); incomeIndex ++) {
                        ProjectIncomeBean projectIncomeBean 
                            = (ProjectIncomeBean) cvProjectIncome.get(incomeIndex);
                        if (acType == null || acType.equals(EMPTY_STRING) || acType.equals(TypeConstants.UPDATE_RECORD)) {
                            queryEngine.update(queryKey,projectIncomeBean);
                        }else if (acType.equals(TypeConstants.DELETE_RECORD)) {
                            queryEngine.delete(queryKey,projectIncomeBean);
                        }
                    }
                }
            }
        }
        cvProjectIncome = null;
    }
    /** Method to Update Budget Modular Details If any Period is added or Deleted.
     *@param cvModifiedBudgetPeriods vector of all Periods
     *@throws Exception if Exception occur
     */
    private void updateBudgetModularDetails(CoeusVector cvModifiedBudgetPeriods) throws Exception{        
        // modified for removing instance variable - case # 2960 - start
        QueryEngine queryEngine = QueryEngine.getInstance();
        //modified for removing instance variable - case # 2960 - end        
        CoeusVector cvBudgetModular = new CoeusVector();
        Equals eqBudgetPeriod = null;
        BudgetPeriodBean budgetPeriodBean;
        String acType = null;
        if(cvModifiedBudgetPeriods != null && cvModifiedBudgetPeriods.size() > 0) {
            for (int index = 0; index < cvModifiedBudgetPeriods.size(); index++) {
                budgetPeriodBean = (BudgetPeriodBean)cvModifiedBudgetPeriods.get(index);
                acType = budgetPeriodBean.getAcType();
                //Added for removing instance variable -case # 2960 - start
                String queryKey = budgetPeriodBean.getProposalNumber()+budgetPeriodBean.getVersionNumber();
                //Added for removing instance variable - case # 2960- end                 
                eqBudgetPeriod = new Equals(BUDGET_PERIOD, new Integer(budgetPeriodBean.getBudgetPeriod()));
                cvBudgetModular = queryEngine.getActiveData(queryKey, BudgetModularBean.class,eqBudgetPeriod);
                if (cvBudgetModular != null && cvBudgetModular.size() > 0) {
                    for (int modularIndex = 0; modularIndex  < cvBudgetModular.size(); modularIndex ++) {
                        BudgetModularBean budgetModularBean = (BudgetModularBean) cvBudgetModular.get(modularIndex);
                        if (acType == null || acType.equals(EMPTY_STRING) || acType.equals(TypeConstants.UPDATE_RECORD)) {
                            queryEngine.update(queryKey,budgetModularBean);
                        }else if (acType.equals(TypeConstants.DELETE_RECORD)) {
                            queryEngine.delete(queryKey,budgetModularBean);
                        }
                    }
                }
                cvBudgetModular = queryEngine.getActiveData(queryKey,BudgetModularIDCBean.class,eqBudgetPeriod);
                if (cvBudgetModular != null && cvBudgetModular.size() > 0) {
                    for (int modularIndex = 0; modularIndex  < cvBudgetModular.size(); modularIndex ++) {
                        BudgetModularIDCBean budgetModularIDCBean = (BudgetModularIDCBean) cvBudgetModular.get(modularIndex);
                        if (acType == null || acType.equals(TypeConstants.UPDATE_RECORD)) {
                            queryEngine.update(queryKey,budgetModularIDCBean);
                        }else if (acType.equals(TypeConstants.DELETE_RECORD)) {
                            queryEngine.delete(queryKey,budgetModularIDCBean);
                        }
                    }
                }
            }
            
        }
        eqBudgetPeriod = null;
        cvBudgetModular = null;
    }
    //bug fixed for budget modular start 4
    /** Method to Update Budget Modular Details If any Period is Deleted.
     * Note: If any period is deleted in b/w periods, not from last.
     *@throws Exception if Exception occur
     */
    private void updateBudgetModular(HttpServletRequest request, String queryKey) throws Exception{        
        // modified for removing instance variable - case # 2960- start
        QueryEngine queryEngine = QueryEngine.getInstance();
        //modified for removing instance variable - case # 2960- end        
        HttpSession session = request.getSession();
        List lstDelData = (List)session.getAttribute("deletedPeriods");
        CoeusVector cvBudgetModular = null;
        Equals eqBudgetPeriod = null;
        BudgetPeriodBean budgetPeriodBean;
        String acType = null;
        if(lstDelData != null && lstDelData.size() > 0) {
            cvBudgetModular = new CoeusVector();
            for (int index = 0; index < lstDelData.size(); index++) {
                eqBudgetPeriod = new Equals(BUDGET_PERIOD, (Integer)lstDelData.get(index));
                cvBudgetModular = queryEngine.getActiveData(queryKey, BudgetModularBean.class,eqBudgetPeriod);
                if (cvBudgetModular != null && cvBudgetModular.size() > 0) {
                    for (int modularIndex = 0; modularIndex  < cvBudgetModular.size(); modularIndex ++) {
                        BudgetModularBean budgetModularBean = (BudgetModularBean) cvBudgetModular.get(modularIndex);
                        if (acType == null || acType.equals(EMPTY_STRING) 
                            || acType.equals(TypeConstants.UPDATE_RECORD) || acType.equals(TypeConstants.DELETE_RECORD)) {
                            queryEngine.delete(queryKey,budgetModularBean);
                        }
                    }
                }
                cvBudgetModular = queryEngine.getActiveData(queryKey,BudgetModularIDCBean.class,eqBudgetPeriod);
                if (cvBudgetModular != null && cvBudgetModular.size() > 0) {
                    for (int modularIndex = 0; modularIndex  < cvBudgetModular.size(); modularIndex ++) {
                        BudgetModularIDCBean budgetModularIDCBean = (BudgetModularIDCBean) cvBudgetModular.get(modularIndex);
                        if (acType == null || acType.equals(EMPTY_STRING) || 
                                acType.equals(TypeConstants.UPDATE_RECORD) || acType.equals(TypeConstants.DELETE_RECORD)) {
                            queryEngine.delete(queryKey,budgetModularIDCBean);
                        }
                    }
                }
            }
            
        }
        eqBudgetPeriod = null;
        cvBudgetModular = null;
    }
    //bug fixed for budget modular end 4
    /**
     *Method to prepare Budget Period bean with Updated Data
     *@return vector of Modified Budget Periods
     *@throws Exception if any error occur
     */
    private CoeusVector getModifiedBudgetPeriods(HttpServletRequest request) throws Exception{
        ArrayList arLstModifiedData = getModifiedData(request);
        CoeusVector cvModifiedPeriods = new CoeusVector();
        DateUtils dateUtils = null;
        if(arLstModifiedData != null){
            dateUtils = new DateUtils();
            for(int index = 0; index < arLstModifiedData.size(); index++){
                DynaValidatorForm dynaPeriodBean = (DynaValidatorForm)arLstModifiedData.get(index);
                String strStartDate = (String)dynaPeriodBean.get("periodStartDate");
                String strEndDate = (String)dynaPeriodBean.get("periodEndDate");
                strStartDate = dateUtils.formatDate(strStartDate,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
                strEndDate = dateUtils.formatDate(strEndDate,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
                dynaPeriodBean.set("startDate",dateUtils.getSQLDate(strStartDate));
                dynaPeriodBean.set("endDate",dateUtils.getSQLDate(strEndDate));
                BudgetPeriodBean budgetPeriodBean = new BudgetPeriodBean();
                BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
                beanUtilsBean.copyProperties(budgetPeriodBean, dynaPeriodBean);
                budgetPeriodBean.setAw_BudgetPeriod(budgetPeriodBean.getBudgetPeriod());
                cvModifiedPeriods.addElement(budgetPeriodBean);
            }
            dateUtils = null;
        }
        return cvModifiedPeriods;
    }
    /**
     *Method to check Data is updated or Inserted
     *@return boolean to check data is modified or not
     *@throws Exception if any error occurs
     */
    private boolean checkDataModified(HttpServletRequest request) throws Exception{
        ArrayList arLstModifiedData = getModifiedData(request);
        boolean isModified = false;
        DateUtils dateUtils = new DateUtils();
        if(arLstModifiedData != null){
            for(int index = 0; index < arLstModifiedData.size(); index++){
                DynaValidatorForm dynaPeriodBean = (DynaValidatorForm)arLstModifiedData.get(index);
                String acType = (String)dynaPeriodBean.get("acType");
                //Modified for Case#2341 - Recalculate Budget if Project dates change
                if(acType != null && (acType.equals(TypeConstants.INSERT_RECORD)
                    || acType.equals(TypeConstants.UPDATE_RECORD))){
                    isModified = true;
                    acType = null;
                    break;
                }
                Date dtDate = (Date)dynaPeriodBean.get("startDate");
                String strDate = (String)dynaPeriodBean.get("periodStartDate");
                strDate = dateUtils.formatDate(strDate,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
                Date dtReqDate = dateUtils.getSQLDate(strDate);
                if(dtDate.compareTo(dtReqDate) < 0 || dtDate.compareTo(dtReqDate) > 0){
                    isModified = true;
                    strDate = null;
                    dtDate = null;
                    dtReqDate = null;
                    break;
                }
                dtDate = (Date)dynaPeriodBean.get("endDate");
                strDate = (String)dynaPeriodBean.get("periodEndDate");
                strDate = dateUtils.formatDate(strDate,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
                dtReqDate = dateUtils.getSQLDate(strDate);
                if(dtDate.compareTo(dtReqDate) < 0 || dtDate.compareTo(dtReqDate) > 0){
                    isModified = true;
                    strDate = null;
                    dtDate = null;
                    dtReqDate = null;
                    break;
                }
            }
        }
        dateUtils = null;
        arLstModifiedData = null;
        return isModified;
    }
    
    /**
     * Added for Case#2341 - Recalculate Budget if Project dates change
     * To generate periods as per the new project dates
     * @param request HttpServletRequest
     * @param arLstAdjustBudgetPeriods List
     * @param dynaForm CoeusDynaBeansList
     * @throws java.lang.Exception 
     * @return Added for Case#2341 - Recalculate Budget if Project dates change
     */
    private ArrayList getNewPeriods(HttpServletRequest request, List arLstAdjustBudgetPeriods,
            CoeusDynaBeansList dynaForm) throws Exception{
        Calendar calStart, calEnd, calPeriodStart, calPeriodEnd;
        int startYear, endYear;
        DateUtils dateUtils = new DateUtils();
        Vector vecProposalHeader = (Vector)request.getSession().getAttribute("getProposalHeaderData");
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean) request.getSession().getAttribute("budgetInfoBean");
        budgetInfoBean = (budgetInfoBean == null) ? new BudgetInfoBean() : budgetInfoBean;
        ArrayList newPeriods = new ArrayList();
        calStart = Calendar.getInstance();
        calEnd = Calendar.getInstance();
        EPSProposalHeaderBean proposalHeaderBean = null;
        if(vecProposalHeader != null && vecProposalHeader.size() > 0){
            proposalHeaderBean = (EPSProposalHeaderBean)vecProposalHeader.elementAt(0);
            java.sql.Date proposalStartDate =  proposalHeaderBean.getProposalStartDate();
            java.sql.Date proposalEndDate = proposalHeaderBean.getProposalEndDate();
            calStart.setTime(proposalHeaderBean.getProposalStartDate());
            calEnd.setTime(proposalHeaderBean.getProposalEndDate());
        }
        startYear = calStart.get(Calendar.YEAR);
        endYear = calEnd.get(Calendar.YEAR);
        arLstAdjustBudgetPeriods = (arLstAdjustBudgetPeriods == null)? new ArrayList() : arLstAdjustBudgetPeriods;
        if(startYear <= endYear) {
            //Proposal spans more thrn a year. Break up required.
            calPeriodStart = calStart;
            calPeriodEnd = Calendar.getInstance();
            int budgetPeriod = 0;
            BudgetPeriodBean budgetPeriodBean = null;
            while(true) {
                budgetPeriod = budgetPeriod + 1;
                if(arLstAdjustBudgetPeriods.size() >= budgetPeriod){
                    DynaValidatorForm dynaPeriodForm = (DynaValidatorForm) arLstAdjustBudgetPeriods.get(budgetPeriod-1);
                    budgetPeriodBean = new BudgetPeriodBean();
                    BeanUtilsBean copyBean = new BeanUtilsBean();
                    copyBean.copyProperties(budgetPeriodBean, dynaPeriodForm);
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
                
                budgetPeriodBean.setEndDate(new java.sql.Date(calPeriodEnd.getTimeInMillis()));
                String periodStartDate = dateUtils.formatDate(budgetPeriodBean.getStartDate().toString(),SIMPLE_DATE_FORMAT);
                String periodEndDate = dateUtils.formatDate(budgetPeriodBean.getEndDate().toString(), SIMPLE_DATE_FORMAT); 
                DynaActionForm formData = (DynaActionForm)dynaForm.getDynaForm(request,"adjustPeriodBoundary");
                DynaBean newDynaBean = ((DynaBean)formData).getDynaClass().newInstance();
                newDynaBean.set("periodStartDate",periodStartDate);
                newDynaBean.set("periodEndDate",periodEndDate);
                BeanUtilsBean beanUtils = new BeanUtilsBean();
                beanUtils.copyProperties(newDynaBean, budgetPeriodBean);
                if(TypeConstants.INSERT_RECORD.equals(budgetPeriodBean.getAcType())){
                    Timestamp prepareTimestamp = prepareTimeStamp();
                    newDynaBean.set("updateTimestamp",prepareTimestamp.toString());
                }
                if(calPeriodEnd.after(calEnd) || calPeriodEnd.equals(calEnd)) {
                    budgetPeriodBean.setEndDate(proposalHeaderBean.getProposalEndDate());
                    periodEndDate = dateUtils.formatDate(budgetPeriodBean.getEndDate().toString(), SIMPLE_DATE_FORMAT); 
                    newDynaBean.set("periodEndDate",periodEndDate);
                    newPeriods.add(newDynaBean);
                    break;
                }                
                newPeriods.add(newDynaBean);
            }
        }
        return newPeriods;
    }
    
    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
    /**
     *     
     * To reset the period base salary of the person for the particular period.
     * @param int
     * @param BudgetPersonsBean
     * @return BudgetPersonsBean     
     */
    private BudgetPersonsBean updatePersonPeriodBaseSalary(int budgetPeriod, BudgetPersonsBean persBean){
        double baseSalary = 0.00;
        boolean modified = false;
        switch(budgetPeriod){
            case 1:
                persBean.setBaseSalaryP1(baseSalary);
                modified = true;
                break;
            case 2:
                persBean.setBaseSalaryP2(baseSalary);
                modified = true;
                break;
            case 3:
                persBean.setBaseSalaryP3(baseSalary);
                modified = true;
                break;
            case 4:
                persBean.setBaseSalaryP4(baseSalary);
                modified = true;
                break;
            case 5:
                persBean.setBaseSalaryP5(baseSalary);
                modified = true;
                break;
            case 6:
                persBean.setBaseSalaryP6(baseSalary);
                modified = true;
                break;
            case 7:
                persBean.setBaseSalaryP7(baseSalary);
                modified = true;
                break;
            case 8:
                persBean.setBaseSalaryP8(baseSalary);
                modified = true;
                break;
            case 9:
                persBean.setBaseSalaryP9(baseSalary);
                modified = true;
                break;
            case 10:
                persBean.setBaseSalaryP10(baseSalary);
                modified = true;
                break;
        }
        //check if value is modified
        if(modified){
            persBean.setAcType(TypeConstants.UPDATE_RECORD);
        }
        return persBean;
    }
    
    /* This method sets the maximum Row ID from the vector of Budget Persons beans
     *that is present in queryEngine
     */
    private int setMaxRowID(String queryKey, int rowID) {
        CoeusVector cvBudgetPersons = new CoeusVector();
        BudgetPersonsBean personsBean;
        try {
            cvBudgetPersons = QueryEngine.getInstance().getDetails(queryKey,BudgetPersonsBean.class);
            if (cvBudgetPersons != null && cvBudgetPersons.size() > 0) {
                cvBudgetPersons.sort("rowId", false);
                personsBean = (BudgetPersonsBean) cvBudgetPersons.get(0);
                rowID = personsBean.getRowId() + 1;
            }
        } catch(Exception exception) {
            exception.printStackTrace();
        }
        return rowID;
    }
    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
 }
