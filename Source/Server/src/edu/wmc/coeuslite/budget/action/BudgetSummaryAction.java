/*
 * BudgetSummaryAction.java
 *
 * Created on May 16, 2006, 4:29 PM
 */
/* PMD check performed, and commented unused imports and variables on 03-MARCH-2011
 * by MD.Ehtesham Ansari
 */

package edu.wmc.coeuslite.budget.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.budget.bean.BudgetDetailBean;
import edu.mit.coeus.budget.bean.BudgetDetailCalAmountsBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetModularBean;
import edu.mit.coeus.budget.bean.BudgetPeriodBean;
import edu.mit.coeus.budget.bean.BudgetPersonnelCalAmountsBean;
import edu.mit.coeus.budget.bean.BudgetPersonnelDetailsBean;
import edu.mit.coeus.budget.bean.BudgetPersonsBean;
import edu.mit.coeus.budget.bean.BudgetUpdateTxnBean;
import edu.mit.coeus.budget.bean.CostElementsBean;
import edu.mit.coeus.budget.bean.ValidCERateTypesBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.RateClassTypeConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeuslite.utils.CoeusDynaBeansList;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.wmc.coeuslite.budget.bean.ProposalBudgetHeaderBean;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * This class is used to update/calculate the budget summary screen.
 * @author vinayks
 */
public class BudgetSummaryAction extends BudgetBaseAction {
    
    /**
     * This is used to compare the actionMapping path
     */
    private static final String BUDGET_SUMMARY = "/budgetSummary" ;
    /**
     * This String holds the acType Value
     */
    private static final String AC_TYPE_UPDATE = "U";
    /**
     * This String holds the Default final version flag
     */
    private static final String DEFAULT_FINAL_VERSION ="N";
    /**
     * This String holds the Default modular budget flag
     */
    private static final String DEFAULT_MODULAR_BUDGET ="N";
    
    // Case id 2924 - start
    /**
     * This String holds the Default OnOffCampus flag on the STRUTS budgetsummary form
     */
    private static final String ON_CAMPUS = "Y";
    /**
     * ActionMappping path for OnOff Campus Flag Change
     */
    private static final String CHANGE_LINE_ITEM_ONOFF = "/changeLineItemOnOff";
    // Case id 2924 - end  
    
     /**
     * This is used to compare the actionMapping path
     */    
    private static final String CHANGE_LINE_ITEM_OHRATE = "/changeLineItemOHRate";
    /**
     * ActionMapping path for UR Rate Type Change
     */    
    private static final String CHANGE_LINE_ITEM_URRATE = "/changeLineItemURRate";    
    /**
     * static variable for Equals operator
     */    
    private static final String LINE_ITEM_NUMBER = "lineItemNumber";
    /**
     * static variable for Equals Operator
     */    
    private static final String BUDGET_PERIOD = "budgetPeriod";
    /**
     * staic variable for Equals Operrator
     */    
    private static final String RATE_CLASS_CODE = "rateClassCode";
    /**
     * static variable for Equals Operator
     */    
    private static final String  RATE_CLASS_TYPE = "rateClassType";
    /**
     * static variable for action messages
     */    
    private static final String INCOMPLETE_MODULAR_BUDGET = "budget_summary_modular_budget_exceptionCode.1121";
    /**
     * static variable for action messages
     */ 
    private static final String NO_MODULAR_BUDGET = "budget_summary_modular_budget_exceptionCode.1120";
    /**
     * static variable for action messages
     */     
    private static final String INVALID_TOTAL_COST_LIMIT ="error.invalidTotalCostLimit";
    
    //COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-Start
    private static final String INVALID_DIRECT_COST ="error.budgetSummary.directCost";
    private static final String INVALID_INDIRECT_COST ="error.budgetSummary.inDirectCost";
    private static final String INVALID_UNDER_RECOVER_COST ="error.budgetSummary.underRecoverCost";
    private static final String INVALID_COST_SHARING_COST ="error.budgetSummary.costSharing";
    private static final String INVALID_TOTAL_COST ="error.budgetSummary.TotalCost";     
    //COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-End
   
    //Added for case#3654 - Third option 'Default' in the campus dropdown
    private static final String DEFAULT_CAMPUS = "D";
    private static final String VALIDATION = "Validation";
    /** Creates a new instance of BudgetSummaryAction */
    public BudgetSummaryAction() {
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
        //Modified for COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-Start      
        DynaValidatorForm dynaBudgetForm = new DynaValidatorForm(); 
        List lstBudgetPeriodSummary = null;
        CoeusDynaBeansList budgetSummaryDynaList = (CoeusDynaBeansList) actionForm;
        List lstBudgetSummary = budgetSummaryDynaList.getList();                          
        lstBudgetPeriodSummary = budgetSummaryDynaList.getBeanList();
        dynaBudgetForm = (DynaValidatorForm) lstBudgetSummary.get(0);                       
        ActionForward actionForward = performBudgetSummaryAction(actionMapping,dynaBudgetForm,request,response,lstBudgetPeriodSummary);
        //Modified for COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-End      
        return actionForward;
    }
    
    /**
     * This method will identify which request is comes from which path and
     * navigates to the respective ActionForward
     * @returns ActionForward object
     * @return
     * @param dynaBudgetForm
     * @param actionMapping
     * @throws Exception
     */
    //Modified for COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-Start      
    private ActionForward performBudgetSummaryAction(ActionMapping actionMapping,
    DynaValidatorForm dynaBudgetForm, HttpServletRequest request,HttpServletResponse response,List lstBudgetSummary)throws Exception{
        //Modified for COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-End
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        String proposalNumber = (String) session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());        
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());        
        WebTxnBean webTxnBean = new WebTxnBean();
         BudgetInfoBean budgetInfoBean =  getBudgetForProposal(dynaBudgetForm, request);
         Hashtable htBudgetData = initializeBudgetCalculator(budgetInfoBean);
         // Check if lock exists or not
        LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId()), request);
        boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
        LockBean lockData = getLockedData(CoeusLiteConstants.BUDGET_LOCK_STR+lockBean.getModuleNumber(), request);
        if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {         
             if(actionMapping.getPath().equals(BUDGET_SUMMARY)){                
                 //Modified with case 2158:Budget Validations                             
                 String statusChange = (String)request.getParameter("statusChange");
                 String forwardTo    = (String)request.getParameter("forwardTo");
                 if(statusChange!=null && "C".equals(statusChange.trim())){
                     dynaBudgetForm.set("budgetStatusCode",statusChange);
                 }else if(forwardTo!=null && VALIDATION.equals(forwardTo.trim())){
                     dynaBudgetForm.set("budgetStatusCode","I");
                 }
                 //Added for Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
                 ProposalBudgetHeaderBean proposalBudgetHeaderBean = (ProposalBudgetHeaderBean)session.getAttribute("ProposalBudgetHeaderBean");
                 proposalNumber = proposalBudgetHeaderBean.getProposalNumber();
                 String budgetStatusCode = (String)dynaBudgetForm.get("budgetStatusCode");
                 String finalVersionFlag = (String)dynaBudgetForm.get("finalVersionFlag");
                 if("C".equals(budgetStatusCode) && "N".equals(finalVersionFlag)){
                     dynaBudgetForm.set("finalVersionFlag","Y");
                 }
                 int versionNumber = proposalBudgetHeaderBean.getVersionNumber();
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
                 boolean update_budget = false;
                 Vector vecAppAndPeriodTypeMessages = new Vector();
                 finalVersionFlag = (String)dynaBudgetForm.get("finalVersionFlag");
                 if("Y".equals(finalVersionFlag) && forwardTo == null){
                     //get periodType details for proposal budget
                     CoeusVector cvBudgetPersonnelDetail = budgetDataTxnBean.getBudgetPersonnelDetail(proposalNumber,versionNumber);
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
                     CoeusVector cvBudgetPersons = budgetDataTxnBean.getBudgetPersons(proposalNumber,versionNumber);
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
                     }
                 }
                 
                 //vector vecAppAndPeriodTypeMessages holds inactivetype(Both appointment type and period type) details
                 if(vecAppAndPeriodTypeMessages!=null && vecAppAndPeriodTypeMessages.size()>0){
                     session.setAttribute("inactive_App_per_type_messages",vecAppAndPeriodTypeMessages);
                 }else{
                     session.setAttribute("inactive_App_per_type_messages",null);
                 }
                 //if vector vecAppAndPeriodTypeMessages holds inactive Type details then dont allow to save it so set update_budget as false
                 if(vecAppAndPeriodTypeMessages!=null && vecAppAndPeriodTypeMessages.size()>0){
                     update_budget = false;
                 }else{
                     //if vector vecAppAndPeriodTypeMessages is null means it doesnt hold any inactive types then set update_budget as true
                     update_budget = true;
                 }
                //COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-Start
                 //budget is getting saved during validation.
                 //forwardTo == null is checking here because to avoid saving od budget during validation.
                 //While validating value for forwardTo will be "validation" and while saving value will be null for forwardTo
                 if(update_budget && forwardTo == null){
                     if(validateBudgetSummary(htBudgetData ,dynaBudgetForm, request)
                     && validateBudgetPeriod(lstBudgetSummary, request)){
                         //Updating the budget period bean.
                         htBudgetData = updateBudgetPeriodBean(htBudgetData, lstBudgetSummary);
                         navigator = addUpdBudget(htBudgetData, dynaBudgetForm, request);
                         // Update the proposal hierarchy sync flag
                         updateProposalSyncFlags(request, proposalNumber);
                     }
                 }
                //COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-End

                  if(forwardTo!=null && "Validation".equals(forwardTo.trim())){
                     String url =  "/validateProposal.do?PAGE=BS";
                    RequestDispatcher rd = request.getRequestDispatcher(url);
                    rd.forward(request,response);
                    return null;
                 }
                 //Added for COEUSQA-2546 Two users can modify a budget at same time. Locking not working -Start
                 //if inactive types are exist in the budget then its going to give false for the update_budget
                 //need to release the lock when update_budget will be true
                 if(update_budget){
                     if(forwardTo == null && dynaBudgetForm.get("budgetStatusCode")!=null && dynaBudgetForm.get("budgetStatusCode").equals("C")){
                         releaseLock(lockBean, request);
                         request.setAttribute(CoeusLiteConstants.RELEASE_LOCK, EMPTY_STRING);
                     }
                 }
                 //Added for Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end
                 //Added for COEUSQA-2546 -End
                 //2158 End
            }else if(actionMapping.getPath().equals(CHANGE_LINE_ITEM_OHRATE)){
                int newOhRateClass = ((Integer)dynaBudgetForm.get("ohRateClassCode")).intValue();
                navigator = changeLineItemOHRate(htBudgetData ,newOhRateClass,dynaBudgetForm,request);
                // Update the proposal hierarchy sync flag
                updateProposalSyncFlags(request, proposalNumber);                 
                //actionForward = actionMapping.findForward(navigator);
            }else if(actionMapping.getPath().equals(CHANGE_LINE_ITEM_URRATE)){           
                if(validateBudgetSummary(htBudgetData ,dynaBudgetForm, request)){
                    navigator = addUpdBudget(htBudgetData , dynaBudgetForm, request);
                    // Update the proposal hierarchy sync flag
                    updateProposalSyncFlags(request, proposalNumber);                     
                }
            // Case Id# 2924 - start
            }else if(actionMapping.getPath().equals(CHANGE_LINE_ITEM_ONOFF)){
                String OnOff = (String)dynaBudgetForm.get("onOffCampusFlag");
                boolean isOnOff = false;
                //Added/Modified for case#3654 - Third option 'Default' in the campus dropdown - start
                boolean defaultIndicator = false;
                if (OnOff.equals(ON_CAMPUS)) {
                   isOnOff = true;
                }else if(OnOff.equals(DEFAULT_CAMPUS)){
                    defaultIndicator = true;
                }
                navigator = changeLineItemOnOff(htBudgetData,isOnOff,dynaBudgetForm,request,defaultIndicator);
                //Added/Modified for case#3654 - Third option 'Default' in the campus dropdown - start
                // Update the proposal hierarchy sync flag
                updateProposalSyncFlags(request, proposalNumber);  
            // Case Id# 2924 - end
            }
        } else {
            String errMsg = "release_lock_for";
            ActionMessages messages = new ActionMessages();                
            messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
            saveMessages(request, messages);
        } 
        getBudgetPeriodData(dynaBudgetForm, request);
        BudgetInfoBean updatedBudgetInfoBean =  getBudgetForProposal(dynaBudgetForm, request);
        session.setAttribute("budgetInfoBean",updatedBudgetInfoBean);
        Map mapMenuList = new HashMap(); 
        mapMenuList.put("menuItems",CoeusliteMenuItems.BUDGET_MENU_ITEMS); 
        mapMenuList.put("menuCode",CoeusliteMenuItems.SUMMARY_CODE );
        setSelectedMenuList(request, mapMenuList);
        
        return  actionMapping.findForward("success");
}
    
    
    /**
     * Method to get the Budget Period Data and set it to request scope so that 
     *the data is available at the jsp;
     * @param dynaValidatorForm
     * @throws Exception
     */
    private void getBudgetPeriodData(DynaValidatorForm dynaValidatorForm,
        HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
//        int versionNumber = Integer.parseInt(dynaValidatorForm.get("versionNumber").toString());
//        String proposalNumber = (String)dynaValidatorForm.get("proposalNumber");       
          ProposalBudgetHeaderBean proposalBudgetHeaderBean = (ProposalBudgetHeaderBean)session.getAttribute("ProposalBudgetHeaderBean");
          String proposalNumber = proposalBudgetHeaderBean.getProposalNumber();
          int versionNumber = proposalBudgetHeaderBean.getVersionNumber(); 
          
          HashMap hmBudgetPeriodData = new HashMap();
          WebTxnBean webTxnBean = new WebTxnBean();
          hmBudgetPeriodData.put("proposalNumber",proposalNumber);
          hmBudgetPeriodData.put("versionNumber",new Integer(versionNumber));
          Hashtable htBudgetPeriod =
        (Hashtable)webTxnBean.getResults(request, "getBudgetPeriodData", hmBudgetPeriodData);
        Vector vecBudgetPeriod = (Vector)htBudgetPeriod.get("getBudgetPeriodData");
//         //Added for Case 3197 - Allow for the generation of project period greater than 12 months -start
        vecBudgetPeriod = calculatePeriodNumberOfMonths(vecBudgetPeriod);            
//        //3197 - end
        //Modified/Added for COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-Start
        HashMap hmParamValue = new HashMap();             
        hmParamValue.put("proposalNumber",proposalNumber);
        hmParamValue.put("versionNumber",versionNumber);
        Hashtable htDCModularData = (Hashtable)webTxnBean.getResults(request,"fetchBudgetDetail",hmParamValue);
        Vector vecDIPeriodData  =(Vector) htDCModularData.get("fetchBudgetDetail"); 
        if(vecBudgetPeriod != null && !vecBudgetPeriod.isEmpty() 
            && vecDIPeriodData != null && !vecDIPeriodData.isEmpty()){
            DynaValidatorForm newDynaForm = new DynaValidatorForm();
            DynaValidatorForm dynaFormPeriodData = new DynaValidatorForm();
            for(Object obj:vecBudgetPeriod){
            newDynaForm = (DynaValidatorForm)obj;
                for(Object objDIData:vecDIPeriodData){
                    dynaFormPeriodData = (DynaValidatorForm)objDIData;
                    if(newDynaForm.get("budgetPeriod").toString().equals(dynaFormPeriodData.get("budgetPeriod").toString())){
                        newDynaForm.set("modularBudgetFlag","Y");
                    }
                }            
            }
        }        
        CoeusDynaBeansList budgetSummaryDynaList = new CoeusDynaBeansList();         
        Vector vecBudgetSummaryDetailedData = new Vector();
        vecBudgetSummaryDetailedData.add(dynaValidatorForm);         
        budgetSummaryDynaList.setList(vecBudgetSummaryDetailedData);         
        budgetSummaryDynaList.setBeanList(vecBudgetPeriod);
        session.setAttribute("budgetSummaryDynaList", budgetSummaryDynaList);        
        session.setAttribute("budgetPeriodData",vecBudgetPeriod );
        //Modified/Added for COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-End
    }
    
    
    /**
     * This method will update, delete the bean values when the OH Rate class is changed
     * @return
     * @param htBudgetData
     * @param newOhRateClass
     * @param dynaBudgetForm
     * @throws Exception
     */
    private String changeLineItemOHRate(Hashtable htBudgetData , int newOhRateClass,
        DynaValidatorForm dynaBudgetForm, HttpServletRequest request) throws Exception{
        String navigator = "success" ;
        BudgetDetailBean budgetDetailBean = new BudgetDetailBean();
        BudgetPersonnelCalAmountsBean personnelCalAmts = new BudgetPersonnelCalAmountsBean();
        BudgetDetailCalAmountsBean detailCalAmts = new BudgetDetailCalAmountsBean();
        BudgetPersonnelDetailsBean personnelDetails = new BudgetPersonnelDetailsBean();
        //Modified for instance variable case#2960.
        String queryKey = dynaBudgetForm.get("proposalNumber").toString()+dynaBudgetForm.get("versionNumber").toString();
        QueryEngine queryEngine = QueryEngine.getInstance();                
        queryEngine.addDataCollection(queryKey, htBudgetData);
        String costElement = EMPTY_STRING;
        Equals ohEquals = new Equals(RATE_CLASS_TYPE, RateClassTypeConstants.OVERHEAD);
        CoeusVector cvBudgetDetails = queryEngine.executeQuery(queryKey, BudgetDetailBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
        CoeusVector cvDetailCalAmts = queryEngine.getActiveData(queryKey, BudgetDetailCalAmountsBean.class, ohEquals);
        CoeusVector cvPersonnelCalAmts = queryEngine.getActiveData(queryKey, BudgetPersonnelCalAmountsBean.class, ohEquals);
        CoeusVector cvChangedCalAmts = new CoeusVector();
        CoeusVector cvChangedPersonnelCalAmts = new CoeusVector();
        CoeusVector cvOhRates = new CoeusVector();
        
        int lineItemNo = 0;
        int budgetPeriod = 0;
        Equals liEquals = new Equals(LINE_ITEM_NUMBER, new Integer(lineItemNo));
        Equals periodEquals = new Equals(BUDGET_PERIOD, new Integer(budgetPeriod));
        Equals newOhRcEquals = new Equals(RATE_CLASS_CODE, new Integer(newOhRateClass));
        And liAndPeriod;
        And liAndOh;
        And liAndPeriodAndOh;
        
        Vector vecCostElements = new Vector();
        Hashtable htCeOhRates = null;
        
        //get all the cost elements in a vector
        if (cvBudgetDetails != null && cvBudgetDetails.size() > 0) {
            int size = cvBudgetDetails.size();
            for(int index = 0; index < size; index++) {
                budgetDetailBean = (BudgetDetailBean) cvBudgetDetails.get(index);
                vecCostElements.add(budgetDetailBean.getCostElement());
            }
            
            //now get the OH rates for these cost elements by calling the database
            htCeOhRates = (Hashtable) getCostElementOHRates(vecCostElements);            
            
            /**
             *Loop thru all the line items. Remove old OH Rate. Then check
             *whether the new OH Rate is present in the hashtable. If present then
             *add it as a new BudgetCalAmt bean.
             *If the line item has Personnel line items, then change the OH for
             *each personnel line item.
             */
            
            for(int index = 0; index < size; index++) {
                budgetDetailBean = (BudgetDetailBean) cvBudgetDetails.get(index);
                budgetPeriod = budgetDetailBean.getBudgetPeriod();
                lineItemNo = budgetDetailBean.getLineItemNumber();
                costElement = budgetDetailBean.getCostElement();
                liEquals = new Equals("lineItemNumber", new Integer(lineItemNo));
                periodEquals = new Equals(BUDGET_PERIOD, new Integer(budgetPeriod));
                liAndPeriod = new And(periodEquals, liEquals);
                liAndOh = new And(liEquals, ohEquals);
                liAndPeriodAndOh = new And(liAndPeriod, ohEquals);
                
                CoeusVector cvCalAmtsOHRate = cvDetailCalAmts.filter(liAndPeriodAndOh);
                CoeusVector cvPersonnelCalAmtsOHRate = cvPersonnelCalAmts.filter(liAndPeriodAndOh);
                
                //check whether old OH rate is present, if present delete it
                if (cvCalAmtsOHRate != null && cvCalAmtsOHRate.size() > 0) {
                    detailCalAmts = (BudgetDetailCalAmountsBean) cvCalAmtsOHRate.get(0);
                    detailCalAmts.setAcType(TypeConstants.DELETE_RECORD);
                    cvChangedCalAmts.add(detailCalAmts);
                    
                    //check whether personnel line item has details, then delete that too
                    if (cvPersonnelCalAmtsOHRate != null && cvPersonnelCalAmtsOHRate.size() > 0) {
                        int pSize = cvPersonnelCalAmtsOHRate.size();
                        for (int ohIndex = 0; ohIndex < pSize; ohIndex++) {
                            personnelCalAmts = (BudgetPersonnelCalAmountsBean)
                            cvPersonnelCalAmtsOHRate.get(ohIndex);
                            personnelCalAmts.setAcType(TypeConstants.DELETE_RECORD);
                            cvChangedPersonnelCalAmts.add(personnelCalAmts);
                        }
                    }
                    
                }
                
                //check whether the new OH rate is present for the Cost Element
                if (htCeOhRates != null && costElement!=null) {
                    cvOhRates = (CoeusVector) htCeOhRates.get(costElement);
                    //Bug Fix -  Start
                    if(cvOhRates == null || cvOhRates.size() == 0) {
                        cvOhRates = new CoeusVector();
                    }
                    //Bug Fix -  End
                    cvOhRates = cvOhRates.filter(newOhRcEquals);
                    
                    //if present create a new detail cal amt bean and add to cvChangedCalAmts
                    if (cvOhRates != null && cvOhRates.size() > 0) {
                        ValidCERateTypesBean validCERateTypes =
                        (ValidCERateTypesBean) cvOhRates.get(0);
                        int rateTypeCode = validCERateTypes.getRateTypeCode();
                        detailCalAmts = new BudgetDetailCalAmountsBean();
                        detailCalAmts.setProposalNumber(budgetDetailBean.getProposalNumber());
                        detailCalAmts.setVersionNumber(budgetDetailBean.getVersionNumber());
                        detailCalAmts.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
                        detailCalAmts.setLineItemNumber(budgetDetailBean.getLineItemNumber());
                        detailCalAmts.setRateClassType(RateClassTypeConstants.OVERHEAD);
                        detailCalAmts.setRateClassCode(newOhRateClass);
                        detailCalAmts.setRateClassDescription(validCERateTypes.getRateClassDescription());
                        detailCalAmts.setRateTypeCode(rateTypeCode);
                        detailCalAmts.setRateTypeDescription(validCERateTypes.getRateTypeDescription());
                        detailCalAmts.setAcType(TypeConstants.INSERT_RECORD);
                        detailCalAmts.setApplyRateFlag(true);
                        cvChangedCalAmts.add(detailCalAmts);
                        
                        /**
                         * check whether personnel line item details are present
                         * if present add the new OH Rate for each personnel
                         * line item
                         */
                        CoeusVector cvPersonnelDetails = queryEngine.getActiveData(queryKey, BudgetPersonnelDetailsBean.class, liAndPeriod);
                        if (cvPersonnelDetails != null && cvPersonnelDetails.size() > 0) {
                            //if (cvPersonnelDetails != null && cvPersonnelDetails.size() > 0) {
                            int pSize = cvPersonnelDetails.size();
                            int personNo = 0;
                            for (int pIndex = 0; pIndex < pSize;  pIndex++) {
                                personnelDetails = (BudgetPersonnelDetailsBean)
                                cvPersonnelDetails.get(pIndex);
                                personNo = personnelDetails.getPersonNumber();
                                personnelCalAmts = new BudgetPersonnelCalAmountsBean();
                                personnelCalAmts.setProposalNumber(budgetDetailBean.getProposalNumber());
                                personnelCalAmts.setVersionNumber(budgetDetailBean.getVersionNumber());
                                personnelCalAmts.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
                                personnelCalAmts.setLineItemNumber(budgetDetailBean.getLineItemNumber());
                                personnelCalAmts.setPersonNumber(personNo);
                                personnelCalAmts.setRateClassType(RateClassTypeConstants.OVERHEAD);
                                personnelCalAmts.setRateClassCode(newOhRateClass);
                                personnelCalAmts.setRateClassDescription(validCERateTypes.getRateClassDescription());
                                personnelCalAmts.setRateTypeCode(rateTypeCode);
                                personnelCalAmts.setRateTypeDescription(validCERateTypes.getRateTypeDescription());
                                personnelCalAmts.setApplyRateFlag(true);
                                personnelCalAmts.setAcType(TypeConstants.INSERT_RECORD);
                                cvChangedPersonnelCalAmts.add(personnelCalAmts);
                            }//End For
                        }
                    }//End if                    
                }//End if 
            }//End For
            
            updatOhRateChange(cvChangedCalAmts,cvChangedPersonnelCalAmts);            
        }
        //This will be changed
        if(validateBudgetSummary(htBudgetData ,dynaBudgetForm, request)){
            navigator = addUpdBudget(htBudgetData,dynaBudgetForm,request);
        }       
        return navigator;
    }
 
    // Case id# 2924 - start
     /**
     * This method will update, delete the bean values when the OH Rate class is changed
     * @return
     * @param htBudgetData
     * @param newOhRateClass
     * @param dynaBudgetForm
     * @throws Exception
     */
    private String changeLineItemOnOff(Hashtable htBudgetData , boolean isOnOff,
        DynaValidatorForm dynaBudgetForm, HttpServletRequest request, boolean defaultIndicator) throws Exception{
        String navigator = "success" ;
        BudgetDetailBean budgetDetailBean = new BudgetDetailBean();
        BudgetPersonnelDetailsBean personnelDetails = new BudgetPersonnelDetailsBean();     
        QueryEngine queryEngine = QueryEngine.getInstance();
        String queryKey = dynaBudgetForm.get("proposalNumber").toString()+dynaBudgetForm.get("versionNumber").toString();
        queryEngine.addDataCollection(queryKey, htBudgetData);
        CoeusVector cvBudgetDetails = queryEngine.executeQuery(queryKey, BudgetDetailBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
        // loop thru budgetDetails and set the onOff Campus flag
        
        //Added/Modified for case#3654 - Third option 'Default' in the campus dropdown - start
        Vector vecCostElements = null;
        if(defaultIndicator){
            vecCostElements = getCostElements();
        }        
        //Added for COEUSDEV-124 - CoeusLite - Budget summary On/off campus flag gets reset - start
        HashMap hmOnOffCampus = new HashMap();        
        //Added for COEUSDEV-124 -start
        
        if (cvBudgetDetails != null && cvBudgetDetails.size() > 0) {
            int size = cvBudgetDetails.size();
            for(int index = 0; index < size; index++) {
                budgetDetailBean = (BudgetDetailBean) cvBudgetDetails.get(index);
                if(defaultIndicator){
                    budgetDetailBean.setOnOffCampusFlag(getDefaultOnOffFlag(vecCostElements, budgetDetailBean.getCostElement()));
                    //Added for COEUSDEV-124 - CoeusLite - Budget summary On/off campus flag gets reset -  -start
                    //Saving default onOffCampus value to the HashMap.
                    hmOnOffCampus.put(new Integer(budgetDetailBean.getLineItemNumber()), new Boolean(budgetDetailBean.isOnOffCampusFlag()));
                    //Added for COEUSDEV-124 -end
//                    isOnOff = budgetDetailBean.isOnOffCampusFlag(); 
                }else{
                    budgetDetailBean.setOnOffCampusFlag(isOnOff);
                }                                                    
                queryEngine.update(queryKey, budgetDetailBean);
            }       
        }     
        //Added/Modified for case#3654 - Third option 'Default' in the campus dropdown - end
        
        // check whether personnel line item details are present
        // if present change onOff campus flag for each personnel line item
        CoeusVector cvPersonnelDetails = queryEngine.executeQuery(queryKey, BudgetPersonnelDetailsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
        if (cvPersonnelDetails != null && cvPersonnelDetails.size() > 0) {
            int pSize = cvPersonnelDetails.size();
            for (int pIndex = 0; pIndex < pSize;  pIndex++) {
                 personnelDetails = (BudgetPersonnelDetailsBean)cvPersonnelDetails.get(pIndex);
                 //Added for COEUSDEV-124 - CoeusLite - Budget summary On/off campus flag gets reset -  -start
                 //Check if select one is default, get the default onOffCampus value from the HashMap, set it to all the personnel.
                 if(defaultIndicator){
                     if (hmOnOffCampus.get(new Integer(personnelDetails.getLineItemNumber())) !=null){
                         Boolean bnOnOffCampus = (Boolean) hmOnOffCampus.get(new Integer(personnelDetails.getLineItemNumber()));
                         personnelDetails.setOnOffCampusFlag(bnOnOffCampus.booleanValue());
                     }
                 }else{
                     personnelDetails.setOnOffCampusFlag(isOnOff);
                 }
                 //Added for COEUSDEV-124 -end
                 queryEngine.update(queryKey, personnelDetails);
             }
        }
        if(validateBudgetSummary(htBudgetData ,dynaBudgetForm, request)){  
            //   Added for case#3654 - Third option 'Default' in the campus dropdown -Start
            request.getSession().setAttribute("bsOnOffCampusFlag", new Boolean(defaultIndicator));
            //   Added for case#3654 - Third option 'Default' in the campus dropdown -End
            navigator = addUpdBudget(htBudgetData,dynaBudgetForm,request);
        }       
        return navigator;
    }
    // Case id# 2924 - end
    
    /**Get the Cost Element OHRates
     * @param vecCostElements
     * @throws Exception
     * @return Hashtable of validCERateTypesBean
     */
    private Hashtable getCostElementOHRates(Vector vecCostElements)throws Exception{
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        Hashtable validOHRateTypes = (Hashtable)budgetDataTxnBean.getValidCERateTypes(vecCostElements);
        return validOHRateTypes;
    }
    
            
     /* Update the changed values to the respective bean for Line Item and Personnel details
      * @param cvChangedCalAmts
      * @param cvChangedPersonnelCalAmts
      * @throws Exception
      */
    private void updatOhRateChange(CoeusVector cvChangedCalAmts,
    CoeusVector cvChangedPersonnelCalAmts) throws Exception{                
        int size = 0;
        int index = 0;
        String acType=EMPTY_STRING;
        //Modified for instance variable case#2960.
        QueryEngine queryEngine = QueryEngine.getInstance();                 
        if (cvChangedCalAmts != null && cvChangedCalAmts.size() > 0) {
            size = cvChangedCalAmts.size();
            BudgetDetailCalAmountsBean detailCalAmtsBean;
            for (index = 0; index < size; index++) {
                detailCalAmtsBean = (BudgetDetailCalAmountsBean)
                cvChangedCalAmts.get(index);
                acType = detailCalAmtsBean.getAcType();
                String queryKey = detailCalAmtsBean.getProposalNumber()+
                    detailCalAmtsBean.getVersionNumber();                
                if (acType.equals(TypeConstants.INSERT_RECORD)) {
                    queryEngine.insert(queryKey,detailCalAmtsBean);
                } else if (acType.equals(TypeConstants.DELETE_RECORD)) {
                    queryEngine.delete(queryKey,detailCalAmtsBean);
                } else if (acType.equals(TypeConstants.UPDATE_RECORD)) {
                    queryEngine.update(queryKey,detailCalAmtsBean);
                }
            }
        }
        
        //Update Budget Personnel Detail Cal Amts to the base window
        if (cvChangedPersonnelCalAmts != null && cvChangedPersonnelCalAmts.size() > 0) {
            size = cvChangedPersonnelCalAmts.size();
            BudgetPersonnelCalAmountsBean personnelCalAmtsBean;
            for (index = 0; index < size; index++) {
                personnelCalAmtsBean = (BudgetPersonnelCalAmountsBean)
                cvChangedPersonnelCalAmts.get(index);
                acType = personnelCalAmtsBean.getAcType();
                String queryKey = personnelCalAmtsBean.getProposalNumber()+
                    personnelCalAmtsBean.getVersionNumber();                
                if (acType.equals(TypeConstants.INSERT_RECORD)) {
                    queryEngine.insert(queryKey,personnelCalAmtsBean);
                } else if (acType.equals(TypeConstants.DELETE_RECORD)) {
                    queryEngine.delete(queryKey,personnelCalAmtsBean);
                } else if (acType.equals(TypeConstants.UPDATE_RECORD)) {
                    queryEngine.update(queryKey,personnelCalAmtsBean);
                    
                }
            }
        }
    }
    
    /**This method updates the budgetInfoBean with form data.
     * @param htBudgetData
     * @param dynaBudgetForm
     * @throws Exception
     * @return CoeusVector containg budgetInfoBean.
     */  
    private CoeusVector getUpdatedBudgetInfo(Hashtable htBudgetData , DynaValidatorForm dynaBudgetForm, HttpServletRequest request)throws Exception{
        String finalVersionFlag = (String)dynaBudgetForm.get("finalVersionFlag");
        String modularBudgetFlag = (String)dynaBudgetForm.get("modularBudgetFlag");
        CoeusVector cvUpdatedBudgetInfo  = new CoeusVector();
        
        CoeusVector cvBudgetInfoVector = (CoeusVector)htBudgetData.get(BudgetInfoBean.class);
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)cvBudgetInfoVector.get(0);
        Integer ohRateClassCode = (Integer) dynaBudgetForm.get("ohRateClassCode");
        Integer urRateClassCode = (Integer) dynaBudgetForm.get("urRateClassCode");
        if( ohRateClassCode !=null && !ohRateClassCode.equals(EMPTY_STRING) && urRateClassCode !=null && !urRateClassCode.equals(EMPTY_STRING)){
            budgetInfoBean.setOhRateClassCode(((Integer)dynaBudgetForm.get("ohRateClassCode")).intValue());
            budgetInfoBean.setUrRateClassCode(((Integer)dynaBudgetForm.get("urRateClassCode")).intValue());
        }else{
            BudgetInfoBean updatedBudgetInfoBean =  getBudgetForProposal(dynaBudgetForm, request);
            budgetInfoBean.setOhRateClassCode(updatedBudgetInfoBean.getOhRateClassCode()); 
            budgetInfoBean.setUrRateClassCode(updatedBudgetInfoBean.getUrRateClassCode());
            
        }
        
        // Case Id 2924 - start
        String onOffCampusFlag = (String)dynaBudgetForm.get("onOffCampusFlag");
        if(onOffCampusFlag!=null && !onOffCampusFlag.equals(EMPTY_STRING)){
            //Modified for case#3654 - Third option 'Default' in the campus dropdown - start
            if(onOffCampusFlag.equals("D")){
                budgetInfoBean.setDefaultIndicator(true);
            }else{
                budgetInfoBean.setDefaultIndicator(false);
                if(onOffCampusFlag.equals(ON_CAMPUS)){
                    budgetInfoBean.setOnOffCampusFlag(true);
                }else {
                    budgetInfoBean.setOnOffCampusFlag(false);
                }
            }
            //Modified for case#3654 - Third option 'Default' in the campus dropdown - end
        } else {
            BudgetInfoBean updatedBudgetInfoBean =  getBudgetForProposal(dynaBudgetForm, request);
            budgetInfoBean.setOnOffCampusFlag(updatedBudgetInfoBean.isOnOffCampusFlag());
        }
        // Case Id 2924 - end     
        
        budgetInfoBean.setComments((String)dynaBudgetForm.get("comments"));
        budgetInfoBean.setAcType(AC_TYPE_UPDATE);
        
        if(finalVersionFlag!=null){
            if(finalVersionFlag.equals(EMPTY_STRING) || finalVersionFlag.equals("N")){//COEUSDEV-848
                budgetInfoBean.setFinalVersion(false);
            }else {
                budgetInfoBean.setFinalVersion(true);
            }
        }
        
        if(modularBudgetFlag!=null){
            if(modularBudgetFlag.equals(EMPTY_STRING) || modularBudgetFlag.equals("N")){//COEUSDEV-848
                budgetInfoBean.setBudgetModularFlag(false);
            }else {
                budgetInfoBean.setBudgetModularFlag(true);
            }
        }
        
        //COEUSQA-1693 - Cost Sharing Submission - start
        String submitCostSharingFlag = (String)dynaBudgetForm.get("submitCostSharingFlag");
        if(submitCostSharingFlag!=null){
            if(submitCostSharingFlag.equals(EMPTY_STRING) || submitCostSharingFlag.equals("N")){//COEUSDEV-848
                budgetInfoBean.setSubmitCostSharingFlag(false);
            }else {
                budgetInfoBean.setSubmitCostSharingFlag(true);
            }
        }
        //COEUSQA-1693 - Cost Sharing Submission - end
        
//        budgetInfoBean.setResidualFunds(formatStringToDouble((String)dynaBudgetForm.get("residualFunds")));
        ActionMessages actionMessages = new ActionMessages();
        double residualFunds = 0;
        try{
            residualFunds = formatStringToDouble((String)dynaBudgetForm.get("residualFunds"));
        }catch(NumberFormatException ne){
            actionMessages.add("error.budgetSummary.residualFunds" , new ActionMessage(
            "error.budgetSummary.residualFunds" ) );
            saveMessages(request, actionMessages);
            residualFunds = 0;
        }
        budgetInfoBean.setResidualFunds(residualFunds);
//        budgetInfoBean.setTotalCostLimit(formatStringToDouble((String)dynaBudgetForm.get("totalCostLimit")));
        double totCostLimit = 0;
        double totalDirectCost = 0;
        try{
            totCostLimit = formatStringToDouble((String)dynaBudgetForm.get("totalCostLimit"));
        }catch(NumberFormatException ne){
            actionMessages.add("error.budgetSummary.totalCostLimit" , new ActionMessage(
            "error.budgetSummary.totalCostLimit" ) );
            saveMessages(request, actionMessages);
            totCostLimit = 0;
        }
        try{
            totalDirectCost = formatStringToDouble((String)dynaBudgetForm.get("totalDirectCostLimit"));
        }catch(NumberFormatException ne){
            actionMessages.add("error.budgetSummary.totalDirectCost" , new ActionMessage(
            "error.budgetSummary.totalDirectCost" ) );
            saveMessages(request, actionMessages);
            totalDirectCost = 0;
        }        
        budgetInfoBean.setTotalCostLimit(totCostLimit);
        budgetInfoBean.setTotalDirectCostLimit(totalDirectCost);
        budgetInfoBean.setUnitNumber((String)dynaBudgetForm.get("unitNumber"));
        cvUpdatedBudgetInfo.addElement((budgetInfoBean));
        return cvUpdatedBudgetInfo ;
    }    
    
    
    /**
     * Method to Calculate and Add/Update Budget Data
     * @return String
     * @param htBudgetData
     * @param dynaBudgetForm
     * @throws Exception
     */
    private String addUpdBudget(Hashtable htBudgetData ,DynaValidatorForm dynaBudgetForm,
        HttpServletRequest request) throws Exception{
        String navigator = EMPTY_STRING ;
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user"+session.getId());
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean"); 
        //Added for removing instance variable -case # 2960 - start
        QueryEngine queryEngine = QueryEngine.getInstance();
        String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        //Added for removing instance variable - case # 2960- end        
        queryEngine.addDataCollection(queryKey, htBudgetData);
        CoeusVector cvBudgetInfoVector = getUpdatedBudgetInfo(htBudgetData,dynaBudgetForm,request); 
        htBudgetData.remove(BudgetInfoBean.class);
        htBudgetData.put(BudgetInfoBean.class,cvBudgetInfoVector);
        BudgetUpdateTxnBean budgetUpdateTxnBean = new BudgetUpdateTxnBean(userInfoBean.getUserId());
        BudgetInfoBean updatedBudgetInfoBean = (BudgetInfoBean)cvBudgetInfoVector.get(0);        
        htBudgetData = calculateAllPeriods(htBudgetData, updatedBudgetInfoBean);
        CoeusVector proposalDevelopmentVector = (CoeusVector)htBudgetData.get(ProposalDevelopmentFormBean.class);
        ProposalDevelopmentFormBean proposalDevelopmentFormBean = (ProposalDevelopmentFormBean)proposalDevelopmentVector.get(0);
        String budgetStatus = (String)dynaBudgetForm.get("budgetStatusCode");
        // Modified for Bug fix 
        if( budgetStatus !=null && !budgetStatus.equals(EMPTY_STRING) ){
            proposalDevelopmentFormBean.setBudgetStatus((String)dynaBudgetForm.get("budgetStatusCode"));
        }else{
            budgetStatus = (String) session.getAttribute("budgetStatusMode");
            if(budgetStatus !=null && !budgetStatus.equals(EMPTY_STRING) && budgetStatus.equals("complete")){
                 proposalDevelopmentFormBean.setBudgetStatus("C");
            }else if(budgetStatus !=null && !budgetStatus.equals(EMPTY_STRING) && budgetStatus.equals("Incomplete")){
                 proposalDevelopmentFormBean.setBudgetStatus("I");
            }
        }
        proposalDevelopmentFormBean.setAcType(AC_TYPE_UPDATE);
        queryEngine.update(queryKey , proposalDevelopmentFormBean );
        // this has to be updated using Transactions.xml way
        budgetUpdateTxnBean.addUpdDeleteBudget(htBudgetData);
        //updateBudgetStatus(request,dynaBudgetForm);
        removeQueryEngineCollection(queryKey);  
        getBudgetPeriodData(dynaBudgetForm, request);
        request.setAttribute("budgetInfoData", dynaBudgetForm);
        navigator = "success" ;
        return navigator ;
    }
    
    
    /**
     * Method to Validate Budget Summary
     * @return boolean
     * @param htBudgetData
     * @param dynaBudgetForm
     * @throws Exception
     */
    private boolean validateBudgetSummary(Hashtable htBudgetData , 
        DynaValidatorForm dynaBudgetForm, HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        ActionMessages actionMessages = new ActionMessages();
        String budgetStatusCode = (String)dynaBudgetForm.get("budgetStatusCode");
        String isModularFlagSelected = (String)dynaBudgetForm.get("modularBudgetFlag");
        String isFinalVersionSelected = (String)dynaBudgetForm.get("finalVersionFlag");
        //Added for removing instance variable -case # 2960 - start
        QueryEngine queryEngine = QueryEngine.getInstance();
        String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        //Added for removing instance variable - case # 2960- end                
        queryEngine.addDataCollection(queryKey, htBudgetData);
        
        if(isModularFlagSelected == null || isModularFlagSelected.equals(EMPTY_STRING)){
            isModularFlagSelected = DEFAULT_MODULAR_BUDGET ;
        }
        
        if(isFinalVersionSelected == null || isFinalVersionSelected.equals(EMPTY_STRING)){
            isFinalVersionSelected = DEFAULT_FINAL_VERSION ;
        }
        
        boolean isSaveRequired = true;
//        double totalCostLimit = formatStringToDouble(((String)dynaBudgetForm.get("totalCostLimit")));
        double totalCostLimit = 0;
        try{
            totalCostLimit = formatStringToDouble((String)dynaBudgetForm.get("totalCostLimit"));
        }catch(NumberFormatException ne){
            totalCostLimit = 0;
        }
        
        if(totalCostLimit > 0 && totalCostLimit < budgetInfoBean.getTotalCost() ){
            actionMessages.add(INVALID_TOTAL_COST_LIMIT, new ActionMessage(
            INVALID_TOTAL_COST_LIMIT));
            saveMessages(request, actionMessages);           
        }
        
        if(isModularFlagSelected.equals("Y") && budgetStatusCode.equals("C")){
            CoeusVector cvBudgetPeriod = queryEngine.executeQuery(queryKey,BudgetPeriodBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            CoeusVector cvBudgetModular  = queryEngine.executeQuery(queryKey,BudgetModularBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            if(cvBudgetModular!= null && cvBudgetModular.size() > 0){
                if(cvBudgetModular.size()!= cvBudgetPeriod.size()){
                    dynaBudgetForm.set("budgetStatusCode" ,"I");
                    actionMessages.add(INCOMPLETE_MODULAR_BUDGET, new ActionMessage(
                    INCOMPLETE_MODULAR_BUDGET ) );
                    saveMessages(request, actionMessages);
                    isSaveRequired = false;
                }
            }else{
                dynaBudgetForm.set("budgetStatusCode" ,"I");
                actionMessages.add(NO_MODULAR_BUDGET, new ActionMessage(
                NO_MODULAR_BUDGET) );
                saveMessages(request, actionMessages);
                isSaveRequired = false;
            }           
        }
        if(budgetStatusCode.equals("C") && isSaveRequired){
            session.setAttribute("budgetStatusMode", "complete");
        }
        request.setAttribute("budgetInfoData", dynaBudgetForm);
        return isSaveRequired;
    }
    
    
    /**
     * Method to get the BudgetInfoBean
     * @return BudgetInfoBean
     * @param dynaBudgetForm
     * @throws Exception
     */
    private BudgetInfoBean getBudgetForProposal(DynaValidatorForm dynaBudgetForm,
        HttpServletRequest request)throws Exception{
        HashMap hmBudgetInfoData = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        ProposalBudgetHeaderBean proposalBudgetHeaderBean = (ProposalBudgetHeaderBean)session.getAttribute("ProposalBudgetHeaderBean"); 
        String proposalNumber = proposalBudgetHeaderBean.getProposalNumber();
        int versionNumber = proposalBudgetHeaderBean.getVersionNumber();
    
    
        hmBudgetInfoData.put("proposalNumber",proposalNumber);
        hmBudgetInfoData.put("versionNumber", new Integer(versionNumber));
        Hashtable htBudgetInfoData = (Hashtable)webTxnBean.getResults(request, "getBudgetInfoData" , hmBudgetInfoData );
        Vector vecBudgetInfo = (Vector)htBudgetInfoData.get("getBudgetInfoData");
        BeanUtilsBean copyBudgetBean  = new BeanUtilsBean();
        BudgetInfoBean budgetInfoBean = new BudgetInfoBean();        
        if(vecBudgetInfo!=null && vecBudgetInfo.size()>0){
            DynaValidatorForm dynaForm = (DynaValidatorForm)vecBudgetInfo.get(0);
            copyBudgetBean.copyProperties(budgetInfoBean, dynaForm);
            //Added for COEUSDEV-124 - CoeusLite - Budget summary On/off campus flag gets reset
            if(dynaForm.get("bsOnOffCampusFlag") !=null && (dynaForm.get("bsOnOffCampusFlag")).toString().equals("D")){
                budgetInfoBean.setDefaultIndicator(true);
            }
            //COEUSDEV-124 End
        }
        budgetInfoBean.setUnitNumber((String)dynaBudgetForm.get("unitNumber"));       
        return budgetInfoBean;
    }
    
    
    //Added/Modified for case#3654 - Third option 'Default' in the campus dropdown - start
    /**
     * This method gets the cost elements data from table OSP$COST_ELEMENT
     * @return vecCostElements Vector
     * @throws Exception CoeusException
     */
    private Vector getCostElements() throws Exception{
        Vector vecCostElements = null;
        BudgetDataTxnBean budgetTxnBean = new BudgetDataTxnBean();
        vecCostElements = budgetTxnBean.getCostElementsListAndBudgetCategoryDesc();        
        return vecCostElements;
    }  
    
    /**
     * This method gets the default onOff flag for a given cost element
     * @param vecCostElement Vector
     * @param requiredCostElement String
     * @return defaultOnOffFlag boolean
     */
    private boolean getDefaultOnOffFlag(Vector vecCostElement, String requiredCostElement){
        boolean defaultOnOffFlag = false;
        CostElementsBean costElementsBean = null;
        if(vecCostElement != null && vecCostElement.size() > 0){
            for(int index = 0; index < vecCostElement.size(); index++){
                costElementsBean = (CostElementsBean)vecCostElement.get(index);
                String costElement = costElementsBean.getCostElement();
                if(requiredCostElement.equals(costElement)){
                    defaultOnOffFlag = costElementsBean.isOnOffCampusFlag();
                    break;
                }
            }
        }
        return defaultOnOffFlag;
    }
    //Added/Modified for case#3654 - Third option 'Default' in the campus dropdown - end    
    
    //COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-Start            
    /**
     * Method to Validate Budget Summary Period Data.
     * @return boolean
     * @param lstBudgetSummary
     * @param request
     * @throws Exception
     */
    private boolean validateBudgetPeriod(List lstBudgetSummary, 
                HttpServletRequest request)throws Exception{                               
        boolean isValidate = true;          
        ActionMessages actionMessages = new ActionMessages();                          
        DynaValidatorForm dynaBudgetForm = null;             
            for(Object obj:lstBudgetSummary){ 
                dynaBudgetForm = (DynaValidatorForm)obj;
                if(!"Y".equals(dynaBudgetForm.get("modularBudgetFlag"))){
                     try{
                            formatStringToDouble((String)dynaBudgetForm.get("strTotalDirectCost"));
                        }catch(NumberFormatException ne){
                             actionMessages.add(INVALID_DIRECT_COST, new ActionMessage(
                                INVALID_DIRECT_COST));
                                saveMessages(request, actionMessages);
                                isValidate = false;
                        }
                     try{
                            formatStringToDouble((String)dynaBudgetForm.get("strTotalIndirectCost"));
                        }catch(NumberFormatException ne){
                             actionMessages.add(INVALID_INDIRECT_COST, new ActionMessage(
                                INVALID_INDIRECT_COST));
                                saveMessages(request, actionMessages);
                                isValidate = false;
                        }
                     try{
                            formatStringToDouble((String)dynaBudgetForm.get("strCostSharingAmount"));
                        }catch(NumberFormatException ne){
                             actionMessages.add(INVALID_COST_SHARING_COST, new ActionMessage(
                                INVALID_COST_SHARING_COST));
                                saveMessages(request, actionMessages);
                                isValidate = false;
                        }
                     try{
                            formatStringToDouble((String)dynaBudgetForm.get("strUnderRecoveryAmount"));
                        }catch(NumberFormatException ne){
                             actionMessages.add(INVALID_UNDER_RECOVER_COST, new ActionMessage(
                                INVALID_UNDER_RECOVER_COST));
                                saveMessages(request, actionMessages);
                                isValidate = false;
                        }
                     try{
                            formatStringToDouble((String)dynaBudgetForm.get("strTotalCost"));
                        }catch(NumberFormatException ne){
                             actionMessages.add(INVALID_TOTAL_COST, new ActionMessage(
                                INVALID_TOTAL_COST));
                                saveMessages(request, actionMessages);
                                isValidate = false;
                        }
                }
                if(!isValidate){
                    break;
                }
            }                
        return isValidate;
    }//End
    //Start
    /**
     * Method to Update Budget Period Dollar Amount in budget summary page.   
     * @param Hashtable htBudgetData
     * @param List lstBudgetPeriodSummary
     * @return Hashtable htBudgetData      
     * @throws Exception
     */
    private Hashtable updateBudgetPeriodBean(Hashtable htBudgetData, List lstBudgetPeriodData) throws Exception{
        CoeusVector budgetPeriodVector = (CoeusVector)htBudgetData.get(BudgetPeriodBean.class);
        double totalDirectCost, totalIndirectCost, costSharingAmount, underRecovery, totalCost;
        BudgetPeriodBean budgetPeriodBean = null;
        DynaValidatorForm dynaForm = null;
        if(budgetPeriodVector != null && !budgetPeriodVector.isEmpty()){
            for(int index=0;index<lstBudgetPeriodData.size();index++){
                budgetPeriodBean = (BudgetPeriodBean)budgetPeriodVector.get(index);
                dynaForm = (DynaValidatorForm) lstBudgetPeriodData.get(index);
                totalCost = formatStringToDouble((String) dynaForm.get("strTotalCost"));
                totalDirectCost= formatStringToDouble((String) dynaForm.get("strTotalDirectCost"));
                totalIndirectCost= formatStringToDouble((String) dynaForm.get("strTotalIndirectCost"));
                costSharingAmount = formatStringToDouble((String) dynaForm.get("strCostSharingAmount"));
                underRecovery= formatStringToDouble((String) dynaForm.get("strUnderRecoveryAmount"));
               // Modified for COEUSQA-3654 CLONE - Total cost disappears after saving budget -start
               // strTotalCost property is set as disabled in jsp, because of that data is not getting in form
               // so directly sum of direct cost & indirect cost and setting to TotalCost
//                budgetPeriodBean.setTotalCost(new Double(totalCost));
                budgetPeriodBean.setTotalCost(new Double(totalDirectCost+totalIndirectCost));
               // COEUSQA-3654 - end
                budgetPeriodBean.setTotalDirectCost(new Double(totalDirectCost));
                budgetPeriodBean.setTotalIndirectCost(new Double(totalIndirectCost));
                budgetPeriodBean.setCostSharingAmount(new Double(costSharingAmount));
                budgetPeriodBean.setUnderRecoveryAmount(new Double(underRecovery));
            }
        }
        
        return htBudgetData;
    }
    //End
    //COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-End
}
