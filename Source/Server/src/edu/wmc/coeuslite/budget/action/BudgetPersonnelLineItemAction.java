/*
 * BudgetLineItemAction.java
 *
 * Created on March 27, 2006, 1:36 PM
 */

/* PMD check performed, and commented unused imports and variables on 14-MAY-2007
 * by Nandkumar
 */

package edu.wmc.coeuslite.budget.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
//import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.budget.bean.BudgetDetailBean;
import edu.mit.coeus.budget.bean.BudgetPersonnelDetailsBean;
import edu.mit.coeus.budget.bean.BudgetDetailCalAmountsBean;
import edu.mit.coeus.budget.bean.BudgetPersonnelCalAmountsBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetPeriodBean;
import edu.mit.coeus.budget.bean.BudgetPersonnelDetailsRateBaseBean;
import edu.mit.coeus.budget.bean.BudgetPersonsBean;
import edu.mit.coeus.budget.bean.BudgetRateBaseBean;
import edu.mit.coeus.budget.bean.BudgetUpdateTxnBean;
import edu.mit.coeus.budget.bean.CostElementsBean;
//import edu.mit.coeus.budget.bean.ValidCERateTypesBean;
//import edu.mit.coeus.budget.calculator.bean.ValidCalcTypesBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeuslite.utils.ComboBoxBean;
//import edu.mit.coeus.utils.RateClassTypeConstants;
import edu.mit.coeus.utils.TypeConstants;
//import edu.mit.coeus.utils.query.And;
//import edu.mit.coeus.utils.query.Equals;
//import edu.mit.coeus.utils.query.NotEquals;
//import edu.mit.coeus.utils.query.Or;
import edu.wmc.coeuslite.budget.bean.CategoryBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.wmc.coeuslite.budget.bean.ProposalBudgetHeaderBean;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
//import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
//import org.apache.struts.action.ActionErrors;
//import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
//import edu.mit.coeus.budget.bean.BudgetPersonsBean;
import java.text.SimpleDateFormat;
//import edu.mit.coeuslite.utils.CoeusDynaBeanList;
//import edu.wmc.coeuslite.utils.CoeusLineItemDynaList;
import edu.mit.coeuslite.utils.CoeusDynaBeansList;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.LockBean;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.action.DynaActionFormClass;
import javax.servlet.ServletContext;
import java.text.DecimalFormat;

/**
 *
 * @author  chandrashekara
 */
public class BudgetPersonnelLineItemAction extends BudgetBaseAction{
    ActionForward actionForward = null;
    //    private HttpServletRequest request = null;;
    //    private HttpSession session = null;
    //    private ActionMapping actionMapping;
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private static final String EMPTY_STRING = "";
    private static final String ADD_LINEITEM = "/AddLineItem";
    private static final String ADD_PERSON_LINEITEM = "/AddPersonLineItem";
    private static final String REMOVE_LINEITEM = "/RemovePersonnelLineItem";
    private static final String LINE_ITEM_DETAILS = "/getPersonnelLineItemDetails";
    private static final String CALCULATE = "/calculatePersonnelBudget";
    private static final String REMOVE_PERSON_LINEITEM = "/RemovePersonLineItem";
    private static final String SAVE_BUDGET_PERSONS = "/setBudgetPersons";
    //private static final String SELECT_BUDGET_PERSONNEL = "/selectBudgetPersonnel";
    
    private static final String  DUPLICATE_BUDGET_PERSONNEL = "budgetPersons_exceptionCode.1000";
  
    private Vector vecMessages;

    private static final String GET_FIELDNAME = "budgetLineItem[";
    private static final String COST_ELEMENT_AS_REQUEST = "].costElement";
    //private static final String LINE_DESC_AS_REQUEST = "].lineItemDescription";
    //private static final String LINE_COST_AS_REQUEST = "].lineItemCost";
    
    private static final String SAVE_AND_APPLY_TO_LATER_PERIODS = "/applyToPersonnelLaterPeriods";
    private static final String SAVE_AND_APPLY_TO_CURRENT_PERIOD = "/applyToPersonnelCurrentPeriods";
    //private static final String RETURN_TO_BUDGET_PERSONNEL = "/returnToBudgetPersonnel";
    
    //Added for TBA Persons - start - 1
    private static final String GET_TBA_DATA = "/tbaSearch";
    //Added for TBA Persons - end - 1
    
    // 4493: While adding a TBA appointment type should be defaulted to 12 Months
    private static final String REG_EMPLOYEE = "REG EMPLOYEE";
    //    private WebTxnBean webTxnBean;
    //    private Timestamp dbTimestamp;
    //    private HttpServletResponse response;
    //    private ActionMessages actionMessages;
    //    private ActionForm actionForm;
    //    private ActionErrors actionErrors;
    
    // Removed instance variables for CASE 2960 - Start
    //private CoeusLineItemDynaList coeusDynaBeanList;
//    private int OPEN_LINE_ITEM_NUMBER = -1;
//    private int OPEN_PERSON_NUMBER = -1;
//    private boolean editLineItemDetails = false;
    
//    private boolean calculateAllPeriods = false;
//    private boolean lineItemWindowOpened = false;
    //    private CoeusDynaBeansList coeusDynaBeanList;
    //    private ProposalBudgetHeaderBean headerBean;
    // removed instance variables CASE 2960- start 
//    private String proposalNumber = null;
//    private int versionNumber  = -1;
    // removed instance variables CASE 2960 - end
//    private boolean isExceeding = false;//Check the total Cost. If amount exceeding above $9,999,999,999.99 then send error message
    /** Creates a new instance of BudgetLineItemAction */
    public BudgetPersonnelLineItemAction() {
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
        // Removed instance variables for CASE 2960  - Start
//        this.lineItemWindowOpened = false;
//        this.OPEN_LINE_ITEM_NUMBER = -1;
//        this.OPEN_PERSON_NUMBER = -1; 
//        this.editLineItemDetails = false;
//        this.calculateAllPeriods = false;
        // Removed instance variables for CASE 2960  -End
        /* Get proposal number and version number */
        HttpSession session = request.getSession();
        ProposalBudgetHeaderBean headerBean = (ProposalBudgetHeaderBean)session.getAttribute("ProposalBudgetHeaderBean");
        // modified for removing instance variables CASE 2960 - start
//        this.proposalNumber = headerBean.getProposalNumber();
        String proposalNumber = headerBean.getProposalNumber();        
//        this.versionNumber = headerBean.getVersionNumber();
        int versionNumber = headerBean.getVersionNumber();
        // modified for removing instance variables CASE 2960 - end
        boolean isLockPresent = isLockPresent(request);
        String navigator = "success";
        actionForward = actionMapping.findForward(navigator);
        CoeusDynaBeansList coeusDynaBeanList = null;
        if(actionMapping.getPath().equals(LINE_ITEM_DETAILS)){
            coeusDynaBeanList = (CoeusDynaBeansList)actionForm;
            navigator = getBudgetLineItemDetails(coeusDynaBeanList, request) ;
            actionForward = actionMapping.findForward(navigator);
            return actionForward;
        }else if(isLockPresent) {

            if(actionMapping.getPath().equals(SAVE_AND_APPLY_TO_CURRENT_PERIOD)){
                coeusDynaBeanList = (CoeusDynaBeansList)actionForm;
                navigator = saveAndApplyToCurrentPeriod(coeusDynaBeanList, request, response) ;
                actionForward = actionMapping.findForward(navigator);
                return actionForward;
            }else if(actionMapping.getPath().equals(SAVE_AND_APPLY_TO_LATER_PERIODS)){
                coeusDynaBeanList = (CoeusDynaBeansList)actionForm;
                navigator = saveAndApplyToLaterPeriod(coeusDynaBeanList, request, response) ;
                actionForward = actionMapping.findForward(navigator);
                return actionForward;
            }
        }
        if(isLockPresent) {
            coeusDynaBeanList = (CoeusDynaBeansList)actionForm;
            // modified for removing instance variables CASE 2960- start
            actionForward = performBudgetDetailsAction(actionMapping, coeusDynaBeanList,request, response, proposalNumber, versionNumber);
            // modified for removing instance variables CASE 2960- end
        }
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
    // Here in this  method have added 2 extra parameters after removing instance variables -CASE 2960
    private ActionForward performBudgetDetailsAction(ActionMapping actionMapping,
    CoeusDynaBeansList coeusDynaBeanList, HttpServletRequest request, HttpServletResponse response,String proposalNumber,int versionNumber)throws Exception{
        String navigator = EMPTY_STRING;
        //WebTxnBean webTxnBean = new WebTxnBean();
        //DynaValidatorForm dynaForm = new DynaValidatorForm();
        if(actionMapping.getPath().equals(ADD_LINEITEM)){
            actionForward = actionMapping.findForward(navigator);
        }else if(actionMapping.getPath().equals(ADD_PERSON_LINEITEM)){
            navigator = addPersonLineItemDetails(coeusDynaBeanList, request);
            request.setAttribute("dataModified", "modified");
            actionForward = actionMapping.findForward(navigator);
        }else if(actionMapping.getPath().equals(SAVE_BUDGET_PERSONS)){
            navigator = saveBudgetPersons(coeusDynaBeanList, request,proposalNumber);
            actionForward = actionMapping.findForward(navigator);
        }else if(actionMapping.getPath().equals(REMOVE_PERSON_LINEITEM)){
            // removal of instance variable ,added the parameters proposalNumber, versionNumber in this method CASE 2960- start
            navigator = removePersonLineItemDetails(coeusDynaBeanList, request,proposalNumber,versionNumber);           
            // removal of instance variable ,added the parameters proposalNumber, versionNumber in this method CASE 2960- end
            actionForward = actionMapping.findForward(navigator);
        }else if(actionMapping.getPath().equals(REMOVE_LINEITEM)){
            navigator = removePersonnelLineItemDetails(coeusDynaBeanList,request, response);
            request.setAttribute("dataModified", "modified");
            actionForward = actionMapping.findForward(navigator);
        }else if(actionMapping.getPath().equals(LINE_ITEM_DETAILS)){
            navigator = getBudgetLineItemDetails(coeusDynaBeanList, request) ;
            actionForward = actionMapping.findForward(navigator);
        }else if(actionMapping.getPath().equals(CALCULATE)){
            navigator=  calculatePersonLineItem(coeusDynaBeanList, request, response);
            request.setAttribute("dataModified", "modified");
            actionForward = actionMapping.findForward(navigator);
        //Added for TBA Persons - start - 2 
        }else if(actionMapping.getPath().equals(GET_TBA_DATA)){
            navigator=  getTBAData(request);            
            actionForward = actionMapping.findForward(navigator);            
        }
        //Added for TBA Persons - end - 2
        return actionForward;
    }
    
    /**
     * This method is used to get last budget Period
     * @param
     * @throws Exception
     * @return int (last budget period)
     */
    private int getLastBudgetPeriod(HttpSession session) throws Exception{
        //System.out.println("--> ENTER getLastBudgetPeriod <--");
        
        Vector vecBudgetPeriods = (Vector)session.getAttribute("BudgetPeriodData");
        int totalBudgetPeriods = vecBudgetPeriods.size();
        DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)vecBudgetPeriods.get(totalBudgetPeriods - 1);
        
        int lastBudgetPeriod = ((Integer)dynaValidatorForm.get("budgetPeriod")).intValue();
        
        //System.out.println("--> EXIT getLastBudgetPeriod <--" + lastBudgetPeriod);
        return lastBudgetPeriod;
    }
    
    
    //    private void printBudgetData(HttpSession session) throws Exception {
    //        /* ------------------------------------------------------ */
    //        Vector vecBDetails = (Vector)session.getAttribute("BudgetDetailsData");
    //        Vector vecBCalAmts = (Vector)session.getAttribute("BudgetDetailCalAmts");
    //        Vector vecBPersonnel = (Vector)session.getAttribute("budgetPersonnelData");
    //        Vector vecBPersonnelCalAmts = (Vector)session.getAttribute("BudgetPersonnelCalAmts");
    //
    //        System.out.println("----------------------------------------------------------------------------");
    //        System.out.println(" >>>>>>>>>>>>>>>*** print budget details <<<<<<<<<<<<<<*** ");
    //        for(int index=0; index<vecBDetails.size(); index++){
    //            DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)vecBDetails.get(index);
    //            System.out.println("budgetPeriod => " + dynaValidatorForm.get("budgetPeriod"));
    //            System.out.println("lineItemNumber => " + dynaValidatorForm.get("lineItemNumber"));
    //            System.out.println("costElement => " + dynaValidatorForm.get("costElement"));
    //        }
    //        System.out.println(" >>>>>>>>>>>>>>>*** print budget cal amounts <<<<<<<<<<<<<<*** ");
    //        for(int index=0; index<vecBCalAmts.size(); index++){
    //            DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)vecBCalAmts.get(index);
    //            System.out.println("budgetPeriod => " + dynaValidatorForm.get("budgetPeriod"));
    //            System.out.println("lineItemNumber => " + dynaValidatorForm.get("lineItemNumber"));
    //            System.out.println("rateTypeDescription => " + dynaValidatorForm.get("rateTypeDescription"));
    //            System.out.println("calculatedCost => " + dynaValidatorForm.get("calculatedCost"));
    //        }
    //        System.out.println(" >>>>>>>>>>>>>>>*** print budget personnel <<<<<<<<<<<<<<*** ");
    //        for(int index=0; index<vecBPersonnel.size(); index++){
    //            DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)vecBPersonnel.get(index);
    //            System.out.println("budgetPeriod => " + dynaValidatorForm.get("budgetPeriod"));
    //            System.out.println("lineItemNumber => " + dynaValidatorForm.get("lineItemNumber"));
    //            System.out.println("fullName => " + dynaValidatorForm.get("fullName"));
    //            System.out.println("salaryRequested => " + dynaValidatorForm.get("salaryRequested"));
    //        }
    //        System.out.println(" >>>>>>>>>>>>>>>*** print budget personnel cal amounts <<<<<<<<<<<<<<*** ");
    //        for(int index=0; index<vecBPersonnelCalAmts.size(); index++){
    //            DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)vecBPersonnelCalAmts.get(index);
    //            System.out.println("budgetPeriod => " + dynaValidatorForm.get("budgetPeriod"));
    //            System.out.println("lineItemNumber => " + dynaValidatorForm.get("lineItemNumber"));
    //            System.out.println("rateTypeDescription => " + dynaValidatorForm.get("rateTypeDescription"));
    //            System.out.println("calculatedCost => " + dynaValidatorForm.get("calculatedCost"));
    //        }
    //        System.out.println("----------------------------------------------------------------------------");
    //    }
    
    /**
     * This method is used to perform saveAndApplyToLaterPeriod
     * @param dynaForm
     * @throws Exception
     * @return
     */
    private String saveAndApplyToLaterPeriod(CoeusDynaBeansList dynaForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception{
        HttpSession session = request.getSession();
        System.out.println("--> ENTER saveAndApplyToLaterPeriod <--");
        
        // Modified for COEUSQA-3038 -CLONE - Leap Year Personnel Budget Details Window -Start
        session.removeAttribute("budgetPersonPreviousStartDate");
        session.removeAttribute("budgetPersonPreviousEndDate");
        // Modified for COEUSQA-3038 -End
        
        /* get budget personnel data */
        Vector vecBudgetPersonnel = (Vector)session.getAttribute("budgetPersonnelData");
        /* get budget details data */
        Vector vecBudgetDetails = (Vector)session.getAttribute("BudgetDetailsData");
        
        String navigator = EMPTY_STRING;
        List budgetPersonnelList = dynaForm.getList();
        List newBudgetPersonnelList = new ArrayList();
        //List budgetLineItemList = dynaForm.getBeanList();
        
        List budgetPersonnelListReceived = dynaForm.getList();
        List budgetLineItemlListReceived = dynaForm.getBeanList();
        
        DateUtils dateUtils = new DateUtils();
        
        DynaValidatorForm dynaValidForm = (DynaValidatorForm)budgetPersonnelList.get(0);
        int budgetPeriod = ((Integer)dynaValidForm.get("budgetPeriod")).intValue(); //Integer.parseInt(request.getParameter("budgetPeriod"));
        
        //COEUSQA-1693 - Cost Sharing Submission - start
        String paramCostSharingFlag = request.getParameter("list[0].submitCostSharingFlag");
        if(paramCostSharingFlag !=null && "Y".equalsIgnoreCase(paramCostSharingFlag)){
            dynaValidForm.set("submitCostSharingFlag","Y");
        } else{
            dynaValidForm.set("submitCostSharingFlag","N");
            //dynaValidatorForm.set("tempSubmitCostSharingFlag", new Boolean(false));
        }
        //COEUSQA-1693 - Cost Sharing Submission - end

        //int budgetPeriod = Integer.parseInt(request.getParameter("budgetPeriod"));
        //BudgetDetailBean budgetDetailBean = new BudgetDetailBean();
        boolean isValidDate = validationForDate(dynaForm, request);
        request.setAttribute("period", new Integer(budgetPeriod));
        if(!isValidDate){
            validateForApplyToLaterPeriods(budgetPeriod, request);
            navigator="success";
            return navigator;
        }
        
        /* reset old cost element field so that the record is removed and inserted.
         * this process is performed to keep record in sync - record created through
         * swing application (in swing line item may have more than one person linked).
         */
        for(int index=0; index<budgetPersonnelList.size(); index++){
            DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)budgetPersonnelList.get(index);
            String tempLineItemEndDate = ((String)dynaValidatorForm.get("tempLineItemEndDate")).trim();
            String tempLineItemStartDate = ((String)dynaValidatorForm.get("tempLineItemStartDate")).trim();
            tempLineItemEndDate = dateUtils.formatDate(tempLineItemEndDate,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
            tempLineItemStartDate = dateUtils.formatDate(tempLineItemStartDate,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
            java.sql.Date tempStartDate = dateUtils.getSQLDate(tempLineItemStartDate);
            java.sql.Date tempEndDate = dateUtils.getSQLDate(tempLineItemEndDate);
            dynaValidatorForm.set("awCostElement", EMPTY_STRING);
            dynaValidatorForm.set("startDate", tempStartDate);
            dynaValidatorForm.set("endDate", tempEndDate);
            /* get new dyna form for later periods */
            int lastBudgetPeriod = getLastBudgetPeriod(session);
            for(int newPeriod=budgetPeriod+1; newPeriod<lastBudgetPeriod+1; newPeriod++){
                HashMap newDynalForms = createNewPersonnelDynaForm(newPeriod, dynaValidatorForm ,request);
                DynaBean newDynaValidatorForm= (DynaBean) newDynalForms.get(BudgetPersonnelDetailsBean.class);
                DynaBean newBudgetDetailForm = (DynaBean) newDynalForms.get(BudgetDetailBean.class);
                //Modified for COEUSDEV-209:Save and apply to later periods sets line item dates same as period dates - Start
//                java.sql.Date newPeriodStartDate = (java.sql.Date)newDynaValidatorForm.get("startDate");
//                java.sql.Date newPeriodEndDate = (java.sql.Date)newDynaValidatorForm.get("endDate");
                Date newPeriodStartDate = (java.sql.Date)newBudgetDetailForm.get("lineItemStartDate");
                Date newPeriodEndDate = (java.sql.Date)newBudgetDetailForm.get("lineItemEndDate");
                Date personStartDate = (java.sql.Date)newDynaValidatorForm.get("startDate");
                Date personEndDate = (java.sql.Date)newDynaValidatorForm.get("endDate");
                
                // Modified for COEUSQA-3038 -CLONE - Leap Year Personnel Budget Details Window -Start
                 // get previous period start date and end date from budget period to set for new personnel line item.
                Vector vecBudgetPeriod = (Vector)session.getAttribute("BudgetPeriodData");
                DynaValidatorForm previousPeriodDynaForm = getFileteredBudgetPeriod(vecBudgetPeriod,newPeriod-1);
                Date previousPeriodStartDate = (java.sql.Date)previousPeriodDynaForm.get("startDate");
                Date previousPeriodEndDate = (java.sql.Date)previousPeriodDynaForm.get("endDate");
                
                java.util.Date  personOldStartDate = (java.util.Date) session.getAttribute("budgetPersonPreviousStartDate");
                java.util.Date  personOldEndDate = (java.util.Date) session.getAttribute("budgetPersonPreviousEndDate");
                if(personOldStartDate != null){                    
                    personStartDate = (Date) personOldStartDate;
                }
                if(personOldEndDate != null){
                    personEndDate = (Date) personOldEndDate;
                }
                
                //Modified for COEUSQA-3422 Budget Persons Line Item Details END DATES match Budget Period END DATES in 
                //Leap Years using February - Start
                java.util.Date pliStartDate =  getValidPersonDateforPeriod(newPeriodStartDate, newPeriodEndDate, personStartDate, null, personStartDate, personEndDate,previousPeriodStartDate,previousPeriodEndDate, null);
                java.util.Date pliEndDate =  getValidPersonDateforPeriod(newPeriodStartDate, newPeriodEndDate, null ,personEndDate, personStartDate, personEndDate,previousPeriodStartDate,previousPeriodEndDate, pliStartDate);
               //Modified for COEUSQA-3422 -End  
                
                // Modified for COEUSQA-3038 -End 
                
                if(newPeriodStartDate.compareTo(pliStartDate) <= 0 && newPeriodEndDate.compareTo(pliStartDate) >= 0 && pliEndDate.compareTo(newPeriodStartDate) > 0) {
                    Integer newLineItemNumber = (Integer)newDynaValidatorForm.get("lineItemNumber");
                    BeanUtilsBean copyBean = new BeanUtilsBean();
                    copyBean.copyProperties(newDynaValidatorForm, dynaValidatorForm);
                    newDynaValidatorForm.set("budgetPeriod", new Integer(newPeriod));
                    newDynaValidatorForm.set("startDate", new Date(pliStartDate.getTime()));
                    if(newPeriodEndDate.compareTo(pliEndDate) < 0) {
                        pliEndDate = newPeriodEndDate;
                    }
                    newDynaValidatorForm.set("endDate", new Date(pliEndDate.getTime()));
                    newDynaValidatorForm.set("lineItemNumber", newLineItemNumber);
                    
                    newBudgetDetailForm.set("budgetPeriod", new Integer(newPeriod));
                    newBudgetDetailForm.set("lineItemStartDate", newPeriodStartDate);
                    newBudgetDetailForm.set("lineItemEndDate", newPeriodEndDate);
                    newBudgetDetailForm.set("costElement", dynaValidatorForm.get("costElement"));
                    newBudgetDetailForm.set("costElementDescription", dynaValidatorForm.get("costElementDescription"));
                    
                    //COEUSDEV-126 : Lite - Off campus rate applied to on campus cost element  - start
                    newDynaValidatorForm.set("onOffCampusFlag", dynaValidatorForm.get("onOffCampusFlag"));
                    newBudgetDetailForm.set("onOffCampusFlag", dynaValidatorForm.get("onOffCampusFlag"));
                    //COEUSDEV-126 -end
                    //COEUSQA-1693 - Cost Sharing Submission - start
                    newDynaValidatorForm.set("submitCostSharingFlag", dynaValidatorForm.get("submitCostSharingFlag"));
                    newBudgetDetailForm.set("submitCostSharingFlag", dynaValidatorForm.get("submitCostSharingFlag"));
                    //COEUSQA-1693 - Cost Sharing Submission - end
                    //System.out.println("cost element for other periods ===> " + dynaValidatorForm.get("costElement"));
                    newDynaValidatorForm.set("costElement", dynaValidatorForm.get("costElement"));
                    newDynaValidatorForm.set("costElementDescription", dynaValidatorForm.get("costElementDescription"));
                    newDynaValidatorForm.set("projectRole", dynaValidatorForm.get("projectRole"));
                    newDynaValidatorForm.set("periodType", dynaValidatorForm.get("periodType"));
                    newDynaValidatorForm.set("percentCharged", dynaValidatorForm.get("percentCharged"));
                    newDynaValidatorForm.set("percentEffort", dynaValidatorForm.get("percentEffort"));
                    newDynaValidatorForm.set("acType",TypeConstants.INSERT_RECORD);
                    
                    // Modified for COEUSQA-3038 -CLONE - Leap Year Personnel Budget Details Window -Start
                    session.setAttribute("budgetPersonPreviousStartDate",new Date(pliStartDate.getTime()));
                    session.setAttribute("budgetPersonPreviousEndDate",new Date(pliEndDate.getTime()));
                    // Modified for COEUSQA-3038 -End
                    
                    newBudgetPersonnelList.add(newDynaValidatorForm);
                    vecBudgetPersonnel.add(newDynaValidatorForm);
                    vecBudgetDetails.add(newBudgetDetailForm);
                }else{
                    break;
                }
                //COEUSDEV 209 End
            }
        }
        
        /* add the new forms created for later periods */
        budgetPersonnelList.addAll(newBudgetPersonnelList);
        dynaForm.setList(budgetPersonnelList);
        HashMap hmOpenList = new HashMap();
        //Case 2960: Commented for to remove the Instance level variable and add these variable in to dynaValidatorForm -Start
        //        lineItemWindowOpened = true;
        //        calculateAllPeriods = true;
        
        boolean dataChanged = checkPersonnelDataChanged(dynaForm, request, hmOpenList);


        
        List budgetPerson = dynaForm.getList();
        if(budgetPerson !=null && budgetPerson.size() > 0){
            dynaValidForm = (DynaValidatorForm)budgetPerson.get(0);
            dynaValidForm.set("calculateAllPeriodsFlag", new Boolean(true));
            dynaValidForm.set("lineItemWindowOpenedFlag", new Boolean(true));
        }
        // Removed instance variables for CASE 2960  - End
        if(dataChanged){
            navigator = calculatePeriodLineItem(dynaForm , budgetPeriod ,request, response, hmOpenList);
        }
        
        request.setAttribute("popUp","close");
        session.removeAttribute("lastPeriod");
        session.removeAttribute("firstPeriod");
        
        // CoeusDynaBeansList coeusLineItemDynaList = (CoeusDynaBeansList)actionForm;
        dynaForm.setList(budgetPersonnelListReceived);
        dynaForm.setBeanList(budgetLineItemlListReceived);
        session.setAttribute("budgetPersonnelLineItemDynaBean",dynaForm);
        
        navigator="success";
        //System.out.println("--> EXIT saveAndApplyToLaterPeriod <--");
        return navigator;
    }
    
    /**
     * This method is used to perform saveAndApplyToCurrentPeriod
     * @param dynaForm
     * @throws Exception
     * @return
     */
    private String saveAndApplyToCurrentPeriod(CoeusDynaBeansList dynaForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception{
        //System.out.println("--> ENTER saveAndApplyToCurrentPeriod <--");
        HttpSession session = request.getSession();
        List budgetPersonnelList = dynaForm.getList();
        //List budgetLineItemList = dynaForm.getBeanList();
        DateUtils dateUtils = new DateUtils();
        HashMap hmOpenList = new HashMap();        
        List budgetPersonnelListReceived = dynaForm.getList();
        List budgetLineItemlListReceived = dynaForm.getBeanList();
        
        String navigator = EMPTY_STRING;
        DynaValidatorForm dynaValidForm = (DynaValidatorForm)budgetPersonnelList.get(0);
        int budgetPeriod = ((Integer)dynaValidForm.get("budgetPeriod")).intValue(); //Integer.parseInt(request.getParameter("budgetPeriod"));
        boolean isValidDate = validationForDate(dynaForm, request);
        request.setAttribute("period", new Integer(budgetPeriod));
        if(!isValidDate){
            navigator="success";
            return navigator;
        }
         // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item -start
        Vector vecBudgetDetails = (Vector)session.getAttribute("BudgetDetailsData");
        // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item -end
        /* reset old cost element field so that the record is removed and inserted.
         * this process is performed to keep record in sync - record created through
         * swing application (in swing line item may have more than one person linked).
         */
        for(int index=0; index<budgetPersonnelList.size(); index++){
            DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)budgetPersonnelList.get(index);
            String tempLineItemEndDate = ((String)dynaValidatorForm.get("tempLineItemEndDate")).trim();
            String tempLineItemStartDate = ((String)dynaValidatorForm.get("tempLineItemStartDate")).trim();
            tempLineItemEndDate = dateUtils.formatDate(tempLineItemEndDate,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
            tempLineItemStartDate = dateUtils.formatDate(tempLineItemStartDate,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
            java.sql.Date tempStartDate = dateUtils.getSQLDate(tempLineItemStartDate);
            java.sql.Date tempEndDate = dateUtils.getSQLDate(tempLineItemEndDate);
         //   dynaValidatorForm.set("awCostElement", EMPTY_STRING);
            dynaValidatorForm.set("startDate", tempStartDate);
            dynaValidatorForm.set("endDate", tempEndDate);
            // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item -start
            String paramOnOffCampusFlag = request.getParameter("list[0].onOffCampusFlag");
            if(paramOnOffCampusFlag !=null && "on".equalsIgnoreCase(paramOnOffCampusFlag)){
                   dynaValidatorForm.set("onOffCampusFlag",new Boolean(true));
             } else{
                dynaValidatorForm.set("onOffCampusFlag",new Boolean(false));
                //dynaValidatorForm.set("tempSubmitCostSharingFlag", new Boolean(false));
            } 
            dynaValidatorForm.set("acType", "U");
            // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item - end
            
            //COEUSQA-1693 - Cost Sharing Submission - start
            String paramCostSharingFlag = request.getParameter("list[0].submitCostSharingFlag");
            if(paramCostSharingFlag !=null && "Y".equalsIgnoreCase(paramCostSharingFlag)){
                   dynaValidatorForm.set("submitCostSharingFlag","Y");
             } else{
                dynaValidatorForm.set("submitCostSharingFlag","N");
                //dynaValidatorForm.set("tempSubmitCostSharingFlag", new Boolean(false));
            }  
           
            //COEUSQA-1693 - Cost Sharing Submission - end
            // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item - Start
            int personnelLineItemNumber = ((Integer)dynaValidatorForm.get("lineItemNumber")).intValue();
            int personnelBudgetPeriod = ((Integer)dynaValidatorForm.get("budgetPeriod")).intValue();
            
            if ( vecBudgetDetails != null && vecBudgetDetails.size() > 0 ){
                for(int budgetIndex=0; budgetIndex < vecBudgetDetails.size(); budgetIndex++){
                    DynaValidatorForm dynaBudgetDataForm = (DynaValidatorForm)vecBudgetDetails.get(budgetIndex);
                    int detailLineItemNumber = ((Integer)dynaBudgetDataForm.get("lineItemNumber")).intValue();
                    int detailBudgetPeriod = ((Integer)dynaBudgetDataForm.get("budgetPeriod")).intValue();
                    
                    if((detailLineItemNumber == personnelLineItemNumber) && (detailBudgetPeriod == personnelBudgetPeriod)){                        
                        dynaBudgetDataForm.set("submitCostSharingFlag", dynaValidatorForm.get("submitCostSharingFlag"));
                        dynaBudgetDataForm.set("onOffCampusFlag", dynaValidatorForm.get("onOffCampusFlag"));
                    }
                }
            }
            
            
            
        }
         
         /* store updated records - budget details  */
         session.setAttribute("BudgetDetailsData", vecBudgetDetails);
        // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item -End
        
        //Case 2960: Commented for to remove the Instance level variable and add these variable in to dynaValidatorForm -Start
        //        lineItemWindowOpened = true;
        dynaValidForm.set("lineItemWindowOpenedFlag", new Boolean(true));
        
        //Case 2960 End
       
        
        boolean dataChanged = checkPersonnelDataChanged(dynaForm, request, hmOpenList);
        if(dataChanged){
            navigator = calculatePeriodLineItem(dynaForm , budgetPeriod , request, response, hmOpenList);
        }
        
        request.setAttribute("popUp","close");
        session.removeAttribute("lastPeriod");
        session.removeAttribute("firstPeriod");
        
        navigator="success";
        
        //CoeusDynaBeansList coeusLineItemDynaList = (CoeusDynaBeansList)actionForm;
        dynaForm.setList(budgetPersonnelListReceived);
        dynaForm.setBeanList(budgetLineItemlListReceived);
        session.setAttribute("budgetPersonnelLineItemDynaBean",dynaForm);
        
        //System.out.println("--> EXIT saveAndApplyToCurrentPeriod <--");
        return navigator;
    }
      
    
    /**
     * Method to reset budget data when a personnel item is removed
     * Swing and Lite application should be in sync.
     *@return
     */
    private void resetPersonnelBudgetData(DynaValidatorForm personnelDynaForm,
    HttpServletRequest request) throws Exception{
        //System.out.println("*** ENTER resetPersonnelBudgetData ***");
        /* get budget detail data from session */
        HttpSession session = request.getSession();
        Vector vecBudgetDetails = (Vector)session.getAttribute("BudgetDetailsData");
        /* get the calculated Amts from session */
        Vector vecBudgetDetailCalAmts = (Vector)session.getAttribute("BudgetDetailCalAmts");
        /* get personnel budget data from session */
        Vector vecBudgetPersonnel = (Vector)session.getAttribute("budgetPersonnelData");
        /* get the budget personnel calculated Amts from session */
        Vector vecBudgetPersonnelCalAmts = (Vector)session.getAttribute("BudgetPersonnelCalAmts");
        
        boolean newRowInserted = false;
        String acTypeForDeletedRow = (String)personnelDynaForm.get("acType");
        /* check whether the current row deleted is a new row added and not recorded
         * in database yet. If yes, do not mark the record for deletion (to remove
         * from database as such record does not exist in relevant tables.
         */
        if(acTypeForDeletedRow.equalsIgnoreCase(TypeConstants.INSERT_RECORD)){
            newRowInserted = true;
        }
        
        /* check deleted records - budget details */
        Vector vecDeletedBudgetDetails = (Vector)session.getAttribute("DeletedBudgetDetailsData");
        /* check deleted records - budget cal amts */
        Vector vecDeletedBudgetCalAmts = (Vector)session.getAttribute("DeletedBudgetDetailCalAmts");
        /* check deleted records - budget personnel details */
        Vector vecDeletedBudgetPersonnel = (Vector)session.getAttribute("DeletedBudgetPersonnelData");
        /* check deleted records - budget personnel details */
        Vector vecDeletedBudgetPersonnelCalAmts = (Vector)session.getAttribute("DeletedBudgetPersonnelCalAmts");
        
        if(!newRowInserted){
            if(vecDeletedBudgetDetails == null){
                vecDeletedBudgetDetails = new Vector();
            }
            if(vecDeletedBudgetCalAmts == null){
                vecDeletedBudgetCalAmts = new Vector();
            }
            if(vecDeletedBudgetPersonnel == null){
                vecDeletedBudgetPersonnel = new Vector();
            }
            if(vecDeletedBudgetPersonnelCalAmts == null){
                vecDeletedBudgetPersonnelCalAmts = new Vector();
            }
        }
        
        /* get removed line item number and person number - set this as key to verify existing data */
        int deletedBudgetPeriod = ((Integer)personnelDynaForm.get("budgetPeriod")).intValue();
        int deletedLineItemNumber = ((Integer)personnelDynaForm.get("lineItemNumber")).intValue();
        int deletedPersonNumber = ((Integer)personnelDynaForm.get("personNumber")).intValue();
        boolean removedBudgetLineItem = false;
        /* check budget details */
        int totalLineItems = vecBudgetDetails.size();
        for(int index=0; index<totalLineItems; index++){
            DynaValidatorForm dynaForm = (DynaValidatorForm)vecBudgetDetails.get(index);
            //dynaForm.set("lineItemSequence",new Integer(index + 1));
            int lineItemNumber = ((Integer)dynaForm.get("lineItemNumber")).intValue();
            int budgetPeriod = ((Integer)dynaForm.get("budgetPeriod")).intValue();
            if((lineItemNumber == deletedLineItemNumber) && (budgetPeriod == deletedBudgetPeriod)){
                double quantity = ((Double)dynaForm.get("quantity")).doubleValue();
                quantity = quantity - 1;
                /* Quantity will be greater than 0 for personnel budget created through swing application
                 * (i.e. budget personnel added for a cost element. So reduce the quantity when a person is
                 * removed and if quantity equal 0, remove the cost element.
                 */
                if(quantity == 0) {
                    if(!newRowInserted){
                        dynaForm.set("acType", TypeConstants.DELETE_RECORD);
                        vecDeletedBudgetDetails.add(dynaForm);
                    }
                    vecBudgetDetails.remove(index);
                    removedBudgetLineItem = true;
                }else{
                    dynaForm.set("quantity", new Double(quantity));
                    String acType = (String)dynaForm.get("acType");
                    if(!acType.equalsIgnoreCase(TypeConstants.INSERT_RECORD)){
                        dynaForm.set("acType", TypeConstants.UPDATE_RECORD);
                    }
                }
                break;
            }
        }
        /* check budget details cal amounts. Remove budget cal amounts data only if budget details
         * line item is removed. If not appropriate fields are updated while recalculating budget.
         */
        Vector vecTempBudgetDetailCalAmts = new Vector();
        totalLineItems = vecBudgetDetailCalAmts.size();
        for(int index=0; index<totalLineItems; index++){
            DynaValidatorForm dynaForm = (DynaValidatorForm)vecBudgetDetailCalAmts.get(index);
            int lineItemNumber = ((Integer)dynaForm.get("lineItemNumber")).intValue();
            int budgetPeriod = ((Integer)dynaForm.get("budgetPeriod")).intValue();
            /* if there is a matching key (line item and budget period) and if line item was removed
             * from budget details, remove matching line item from budget cal amounts else set update
             * flag and recalculate will set new values to appropriate fields.
             */
            if((lineItemNumber == deletedLineItemNumber) && (budgetPeriod == deletedBudgetPeriod) &&
            (removedBudgetLineItem)){
                if(!newRowInserted){
                    dynaForm.set("acType", TypeConstants.DELETE_RECORD);
                    vecDeletedBudgetCalAmts.add(dynaForm);
                }
            }else{
                String acType = (String)dynaForm.get("acType");
                if(!acType.equalsIgnoreCase(TypeConstants.INSERT_RECORD)){
                    dynaForm.set("acType", TypeConstants.UPDATE_RECORD);
                }
                vecTempBudgetDetailCalAmts.add(dynaForm);
            }
        }
        /* Remove person from budget personnel details. */
        totalLineItems = vecBudgetPersonnel.size();
        for(int index=0; index<totalLineItems; index++){
            DynaValidatorForm dynaForm = (DynaValidatorForm)vecBudgetPersonnel.get(index);
            int lineItemNumber = ((Integer)dynaForm.get("lineItemNumber")).intValue();
            int budgetPeriod = ((Integer)dynaForm.get("budgetPeriod")).intValue();
            int personNumber = ((Integer)dynaForm.get("personNumber")).intValue();
            if((lineItemNumber == deletedLineItemNumber) && (budgetPeriod == deletedBudgetPeriod) &&
            (personNumber == deletedPersonNumber)){
                if(!newRowInserted){
                    dynaForm.set("acType", TypeConstants.DELETE_RECORD);
                    vecDeletedBudgetPersonnel.add(dynaForm);
                }
                vecBudgetPersonnel.remove(index);
                break;
            }
        }
        /* Remove person from budget personnel cal amounts details. */
        Vector vecTempBudgetPersonnelCalAmts = new Vector();
        totalLineItems = vecBudgetPersonnelCalAmts.size();
        for(int index=0; index<totalLineItems; index++){
            DynaValidatorForm dynaForm = (DynaValidatorForm)vecBudgetPersonnelCalAmts.get(index);
            int budgetPeriod = ((Integer)dynaForm.get("budgetPeriod")).intValue();
            int lineItemNumber = ((Integer)dynaForm.get("lineItemNumber")).intValue();
            //int personNumber = ((Integer)dynaForm.get("personNumber")).intValue();
            //code modified for bug fix case#2759
            if((lineItemNumber == deletedLineItemNumber) && (budgetPeriod == deletedBudgetPeriod)){
                if(!newRowInserted){
                    dynaForm.set("acType", TypeConstants.DELETE_RECORD);
                    vecDeletedBudgetPersonnelCalAmts.add(dynaForm);
                }
            }else{
                vecTempBudgetPersonnelCalAmts.add(dynaForm);
            }
        }
        
        /* store updated records - budget details  */
        session.setAttribute("BudgetDetailsData", vecBudgetDetails);
        /* store updated records - budget cal amts */
        session.setAttribute("BudgetDetailCalAmts", vecTempBudgetDetailCalAmts);
        /* store updated records - budget personnel */
        session.setAttribute("budgetPersonnelData", vecBudgetPersonnel);
        /* store updated records - budget personnel cal amts */
        session.setAttribute("BudgetPersonnelCalAmts", vecTempBudgetPersonnelCalAmts);
        
        /* if the row is new and not recorded in database, do not mark it for
         * deletion.
         */
        if(!newRowInserted){
            /* store deleted records - budget cal amts */
            session.setAttribute("DeletedBudgetDetailsData", vecDeletedBudgetDetails);
            /* store deleted records - budget cal amts */
            session.setAttribute("DeletedBudgetDetailCalAmts", vecDeletedBudgetCalAmts);
            /* store deleted records - budget personnel details */
            session.setAttribute("DeletedBudgetPersonnelData", vecDeletedBudgetPersonnel);
            /* store deleted records - budget personnel details */
            session.setAttribute("DeletedBudgetPersonnelCalAmts", vecDeletedBudgetPersonnelCalAmts);
        }
        
        //System.out.println("*** EXIT resetPersonnelBudgetData ***");
    }
    
    /**
     * Method to remove personnel line Item from personnel budget
     *@return String
     */
    private String removePersonnelLineItemDetails(CoeusDynaBeansList dynaForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception{
        HttpSession session = request.getSession();
        HashMap hmOpenList = new HashMap();
        //System.out.println("*** ENTER removePersonnelLineItemDetails ***");
        String navigator = "success";
        int rowIndex = Integer.parseInt(request.getParameter("rowIndex"));
        //CoeusDynaBeansList coeusLineItemDynaList = (CoeusDynaBeansList)actionForm;
        List budgetPersonnelList = dynaForm.getList();
        
        DynaValidatorForm personnelDynaForm = (DynaValidatorForm)budgetPersonnelList.get(rowIndex);
        int budgetPeriod = ((Integer)personnelDynaForm.get("budgetPeriod")).intValue();
        
        resetPersonnelBudgetData((DynaValidatorForm)budgetPersonnelList.get(rowIndex), request);
        budgetPersonnelList.remove(rowIndex);
        
        List beanList = new ArrayList();
        beanList.add(new Integer(budgetPeriod));
        dynaForm.setList(budgetPersonnelList);
        dynaForm.setBeanList(beanList);
        // Added hashmap parameter for CASE 2960  - Start
        session.setAttribute("budgetPersonnelDynaBean",dynaForm);        
        navigator = calculatePeriodLineItem(dynaForm , budgetPeriod ,request, response , hmOpenList);
        // Added hashmap parameter for CASE 2960  - Start

        request.setAttribute("SelectedBudgetPeriodNumber", new Integer(budgetPeriod));
        
        //System.out.println("*** EXIT removePersonnelLineItemDetails ***");
        return navigator;
    }
    
    /**
     * Method to remove person line Item from Budget persons
     *@return String
     */
    private String removePersonLineItemDetails(CoeusDynaBeansList coeusDynaBeanList,
    HttpServletRequest request,String proposalNumber,int versionNumber) throws Exception{
        //System.out.println("*** ENTER removePersonLineItemDetails ***");
        String navigator = "";
        HttpSession session = request.getSession();
        int rowIndex = Integer.parseInt(request.getParameter("rowIndex"));
        List budgetPersonsList = coeusDynaBeanList.getList();
        Vector vecBudgetDeletedData = new Vector();
        
        //Enhancement: Added to Restricted the person for Remove functionality. If the person is associated with Personnel Line Item. -Start
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmProposalBudgetParam = new HashMap();
        hmProposalBudgetParam.put("proposalNumber",proposalNumber);
        hmProposalBudgetParam.put("versionNumber", new Integer(versionNumber));        
        Hashtable htBudgetPersonnelData = (Hashtable)webTxnBean.getResults(request,"fetchBudgetPersonnelData",hmProposalBudgetParam);
        Vector vecBudgetPersonnel = (Vector)htBudgetPersonnelData.get("fetchBudgetPersonnelData");
        boolean isValidateSuccess = true;
        if(vecBudgetPersonnel !=null && vecBudgetPersonnel.size() >0 && budgetPersonsList !=null && budgetPersonsList.size()>0){
            for(int index=0; index<vecBudgetPersonnel.size(); index++){
                DynaValidatorForm dynaForm = (DynaValidatorForm)  vecBudgetPersonnel.get(index);
                String personId = (String) dynaForm.get("personId");
                DynaValidatorForm dynaPersonsForm = (DynaValidatorForm) budgetPersonsList.get(rowIndex);
                String budPersonId = (String) dynaPersonsForm.get("personId");
                String budPersonName = (String) dynaPersonsForm.get("personName");
                if(personId.equals(budPersonId)){
                    isValidateSuccess = false;
                    ActionMessages actionMessages = new ActionMessages();
                    actionMessages.add("persons.remove.invalid", new ActionMessage(
                    "persons.remove.invalid", budPersonName) );
                    saveMessages(request, actionMessages);
                    navigator="success";
                    return navigator;
                }
            }
        }
        if(session.getAttribute("DeletedBudgetPersonData") != null ){
            vecBudgetDeletedData = (Vector)session.getAttribute("DeletedBudgetPersonData");
        }
        if(isValidateSuccess){
            vecBudgetDeletedData.addElement(budgetPersonsList.get(rowIndex));
            budgetPersonsList.remove(rowIndex);
            request.setAttribute("dataModified", "modified");
        }
        //Enhancement: Added to Restricted the person for remove functionality. If the person is associated with Personnel Line Item. -End
        
        session.setAttribute("DeletedBudgetPersonData",vecBudgetDeletedData);
        session.setAttribute("budgetPersonsDynaBean" ,coeusDynaBeanList);
        navigator = "success";
        //System.out.println("*** EXIT removePersonLineItemDetails ***");
        return navigator;
    }
    
    /**
     * Method to validate Duplicate Persons
     * @return boolean
     * @throws Exception if exception occur
     */
    private boolean validateDuplicatePerson(CoeusDynaBeansList coeusDynaBeanList,
    HttpServletRequest request) throws Exception{
        boolean duplicatePersonExists = false;
        List budgetPersonsList = coeusDynaBeanList.getList();
        int numberOfPersons = budgetPersonsList.size();
        Set uniquePersonData = new HashSet(numberOfPersons, 1.0f);
        for(int index=0; index < numberOfPersons; index++){
            DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)budgetPersonsList.get(index);
            String personId = (String)dynaValidatorForm.get("personId");
            String jobCode = (String)dynaValidatorForm.get("jobCode");
            String effectiveDate = (String)dynaValidatorForm.get("effectiveDate");
            String personKey = personId + jobCode + effectiveDate;
            if(!uniquePersonData.add(personKey)){
                ActionMessages actionMessages = new ActionMessages();
                actionMessages.add(DUPLICATE_BUDGET_PERSONNEL, new ActionMessage(
                DUPLICATE_BUDGET_PERSONNEL ) );
                saveMessages(request, actionMessages);
                duplicatePersonExists = true;
                break;
            }
        }
        return duplicatePersonExists;
    }
    
    
    /**
     * Saves the Budget Persons details
     * @param dynaForm
     * @throws Exception
     */
    // Added parameter proposalNumber in this method,after removing instance variables -CASE 2960
    private String saveBudgetPersons(CoeusDynaBeansList coeusDynaBeanList,
    HttpServletRequest request,String proposalNumber) throws Exception{
        //System.out.println("*** ENTER saveBudgetPersons ***");
        String navigator = "success";
        HttpSession session = request.getSession();
        boolean save_budget_person = false;
        /* check for duplicate persons. if duplicate found, do not send data to database */
        boolean duplicatePersonExists = validateDuplicatePerson(coeusDynaBeanList, request);
        if(duplicatePersonExists){
            return navigator;
        }
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
        double newBaseSalaryP1 = 0.00;
        double oldBaseSalaryP1 = 0.00;
        double newBaseSalaryP2 = 0.00;
        double oldBaseSalaryP2 = 0.00;
        double newBaseSalaryP3 = 0.00;
        double oldBaseSalaryP3 = 0.00;
        double newBaseSalaryP4 = 0.00;
        double oldBaseSalaryP4 = 0.00;
        double newBaseSalaryP5 = 0.00;
        double oldBaseSalaryP5 = 0.00;
        double newBaseSalaryP6 = 0.00;
        double oldBaseSalaryP6 = 0.00;
        double newBaseSalaryP7 = 0.00;
        double oldBaseSalaryP7 = 0.00;
        double newBaseSalaryP8 = 0.00;
        double oldBaseSalaryP8 = 0.00;
        double newBaseSalaryP9 = 0.00;
        double oldBaseSalaryP9 = 0.00;
        double newBaseSalaryP10 = 0.00;
        double oldBaseSalaryP10 = 0.00;
        //Budget Periods details
        CoeusVector cvBudgetPeriods = (CoeusVector)session.getAttribute("budgetPeriodData");
        if(cvBudgetPeriods==null){
            cvBudgetPeriods = new CoeusVector();
        }
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End 
        DateUtils dateUtils = new DateUtils();
        WebTxnBean webTxnBean = new WebTxnBean();
        Timestamp dbTimestamp = prepareTimeStamp();
        edu.mit.coeus.bean.UserInfoBean userInfoBean = (edu.mit.coeus.bean.UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        List budgetPersonsList = coeusDynaBeanList.getList();
        Set inActiveAppointmentType = new HashSet();
        StringBuffer errMsg;
        Vector vecAppointmentTypeMsg = new Vector();
        int numberOfPersons = budgetPersonsList.size(); 
        //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
        HashSet hmAppointmentTYpesTypes = new HashSet();
         BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
            BudgetPersonsBean budgetPersonsBean = new BudgetPersonsBean();
        if(numberOfPersons>0){
            for(int index=0; index < numberOfPersons; index++){
                DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)budgetPersonsList.get(index);
                String newAppointmentType =  (String)dynaValidatorForm.get("appointmentType");
                hmAppointmentTYpesTypes.add(newAppointmentType);
            }
            if(!hmAppointmentTYpesTypes.isEmpty()){
                Iterator finalAppIterator = hmAppointmentTYpesTypes.iterator();
                while(finalAppIterator.hasNext()){
                    String appointmentType = (String)finalAppIterator.next();                   
                    budgetPersonsBean = budgetDataTxnBean.getAppointmentTypeDetails(appointmentType);
                    if("N".equals(budgetPersonsBean.getStatus())){
                        inActiveAppointmentType.add(budgetPersonsBean.getAppointmentType());
                        errMsg = new StringBuffer();
                        //String costElementDesc = costElementsBean.getDescription();
                        errMsg.append("Appointment Type ");
                        errMsg.append(appointmentType);
                        errMsg.append(" is no longer active. Please select an alternative");
                        vecAppointmentTypeMsg.add(errMsg);
                    }
                }
            }
            //If vecAppointmentTypeMsg holds inactive messages then dont allow to save the budget persons so
            //set save_budget_person as false
            if(vecAppointmentTypeMsg.size()>0){
                session.setAttribute("appointmentType_inactive_messages",vecAppointmentTypeMsg);
                save_budget_person = false;
            }else{
                //If there are no inactive appointmentTypes then set save_budget_person as true
                //allow to save the budgetpersons
                session.setAttribute("appointmentType_inactive_messages",null);
                save_budget_person = true;
            }
            
        }     
        //save_budget_person is true then allow to save the budget perasons
        //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end
        if(save_budget_person){
            for(int index=0; index < numberOfPersons; index++){
                DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)budgetPersonsList.get(index);
                String acType = (String)dynaValidatorForm.get("acType");
                /* effectiveDate is of String datatype - convert this to date */
                String effectiveDate = (String) dynaValidatorForm.get("effectiveDate");
                
                // Case# 3912: Lite budget personnel effective date format - Start
                effectiveDate  = dateUtils.formatDate(effectiveDate,":/.,|-","MM/dd/yyyy");
                dynaValidatorForm.set("effectiveDate",effectiveDate);
                // Case# 3912: Lite budget personnel effective date format - End
                
                java.sql.Date newEffectiveDate = dateUtils.getSQLDate(effectiveDate);
                java.sql.Date oldEffectiveDate = (java.sql.Date)dynaValidatorForm.get("awEffectiveDate");
                //Added for Case 2918 – Use of Salary Anniversary Date for calculating inflation in budget development module-Start
                String salAnnivDate = (String) dynaValidatorForm.get("strSalaryAnniversaryDate");
                Date newSalAnnivDate = null;
                salAnnivDate  = dateUtils.formatDate(salAnnivDate,":/.,|-","MM/dd/yyyy");
                dynaValidatorForm.set("strSalaryAnniversaryDate",salAnnivDate);
                java.sql.Date oldSalAnnivDate = (java.sql.Date)dynaValidatorForm.get("awSalaryAnniversaryDate");
                if(salAnnivDate != null){
                    newSalAnnivDate = dateUtils.getSQLDate(salAnnivDate);
                    dynaValidatorForm.set("salaryAnniversaryDate",newSalAnnivDate);
                }else if(salAnnivDate == null){
                    dynaValidatorForm.set("salaryAnniversaryDate", null);
                }
                boolean isAnnivDateChanged = false;
                if((oldSalAnnivDate == null && newSalAnnivDate != null) || (oldSalAnnivDate != null && newSalAnnivDate == null) ){
                    isAnnivDateChanged = true;
                }else if((oldSalAnnivDate !=null && newSalAnnivDate !=null) &&
                        (oldSalAnnivDate.compareTo(newSalAnnivDate) < 0 || oldSalAnnivDate.compareTo(newSalAnnivDate) > 0)){
                    isAnnivDateChanged = true;
                }
                //Added for Case 2918 – Use of Salary Anniversary Date for calculating inflation in budget development module-Start
                
//            String strCalBase = request.getParameter("dynaFormData["+index+"].strCalculationBase");
//            if(strCalBase !=null && !strCalBase.equals(EMPTY_STRING)){
//                strCalBase = strCalBase.replaceAll("[$,/,]","");
//                dynaValidatorForm.set("calculationBase", new Double(strCalBase));
//            }
                
                String strCalBase = request.getParameter("dynaFormData["+index+"].strCalculationBase");
                if(strCalBase !=null && !strCalBase.equals(EMPTY_STRING)){
                    double calBase = 0;
                    try{
                        calBase = formatStringToDouble(strCalBase);
                    }catch(NumberFormatException ne){
                        ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("persons.baseSalary.invalid", new ActionMessage(
                                "persons.baseSalary.invalid") );
                        saveMessages(request, actionMessages);
                        navigator="success";
                        return navigator;
                    }
                    dynaValidatorForm.set("calculationBase", new Double(calBase));
                }
                
                double newCalculationBase = ((Double)dynaValidatorForm.get("calculationBase")).doubleValue();
                double oldCalculationBase = ((Double)dynaValidatorForm.get("awCalculationBase")).doubleValue();
                String newJobCode =  (dynaValidatorForm.get("jobCode").toString().trim());
                String oldJobCode =  (dynaValidatorForm.get("awJobCode").toString().trim());
                String newAppointmentType =  (String)dynaValidatorForm.get("appointmentType");
                String oldAppointmentType =  (String)dynaValidatorForm.get("awAppointmentType");
                
                //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
                int counter = 1;
                navigator="success";
                for(Object data : cvBudgetPeriods){
                    BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean) data;
                    switch(counter){
                        case 1:
                            String strBaseSalary1 = (String)dynaValidatorForm.get("strBasesalaryp1");
                            if(strBaseSalary1 !=null && !strBaseSalary1.equals(EMPTY_STRING)){
                                double calBase = 0;
                                try{
                                    calBase = formatStringToDouble(strBaseSalary1);
                                }catch(NumberFormatException ne){
                                    saveActionMessage(request);
                                    return navigator;
                                }
                                dynaValidatorForm.set("basesalaryp1", new Double(calBase));
                                newBaseSalaryP1 = calBase;
                            }                            
                            if(dynaValidatorForm.get("awBasesalaryp1") != null){
                                oldBaseSalaryP1 = ((Double)dynaValidatorForm.get("awBasesalaryp1")).doubleValue();
                            }
                            break;
                        case 2:
                            String strBaseSalary2 = (String)dynaValidatorForm.get("strBasesalaryp2");
                            if(strBaseSalary2 !=null && !strBaseSalary2.equals(EMPTY_STRING)){
                                double calBase = 0;
                                try{
                                    calBase = formatStringToDouble(strBaseSalary2);
                                }catch(NumberFormatException ne){
                                    saveActionMessage(request);
                                    return navigator;
                                }
                                dynaValidatorForm.set("basesalaryp2", new Double(calBase));
                                newBaseSalaryP2 = calBase;
                            }
                            if(dynaValidatorForm.get("awBasesalaryp2") != null){
                                oldBaseSalaryP2 = ((Double)dynaValidatorForm.get("awBasesalaryp2")).doubleValue();
                            }
                            break;
                        case 3:
                            String strBaseSalary3 = (String)dynaValidatorForm.get("strBasesalaryp3");
                            if(strBaseSalary3 !=null && !strBaseSalary3.equals(EMPTY_STRING)){
                                double calBase = 0;
                                try{
                                    calBase = formatStringToDouble(strBaseSalary3);
                                }catch(NumberFormatException ne){
                                    saveActionMessage(request);
                                    return navigator;
                                }
                                dynaValidatorForm.set("basesalaryp3", new Double(calBase));
                                newBaseSalaryP3 = calBase;
                            }
                            if(dynaValidatorForm.get("awBasesalaryp3") != null){
                                oldBaseSalaryP3 = ((Double)dynaValidatorForm.get("awBasesalaryp3")).doubleValue();
                            }
                            break;
                        case 4:
                            String strBaseSalary4 = (String)dynaValidatorForm.get("strBasesalaryp4");
                            if(strBaseSalary4 !=null && !strBaseSalary4.equals(EMPTY_STRING)){
                                double calBase = 0;
                                try{
                                    calBase = formatStringToDouble(strBaseSalary4);
                                }catch(NumberFormatException ne){
                                    saveActionMessage(request);
                                    return navigator;
                                }
                                dynaValidatorForm.set("basesalaryp4", new Double(calBase));
                                newBaseSalaryP4 = calBase;
                            }
                            if(dynaValidatorForm.get("awBasesalaryp4") != null){
                                oldBaseSalaryP4 = ((Double)dynaValidatorForm.get("awBasesalaryp4")).doubleValue();
                            }
                            break;
                        case 5:
                            String strBaseSalary5 = (String)dynaValidatorForm.get("strBasesalaryp5");
                            if(strBaseSalary5 !=null && !strBaseSalary5.equals(EMPTY_STRING)){
                                double calBase = 0;
                                try{
                                    calBase = formatStringToDouble(strBaseSalary5);
                                }catch(NumberFormatException ne){
                                    saveActionMessage(request);
                                    return navigator;
                                }
                                dynaValidatorForm.set("basesalaryp5", new Double(calBase));
                                newBaseSalaryP5 = calBase;
                            }
                            if(dynaValidatorForm.get("awBasesalaryp5") != null){
                                oldBaseSalaryP5 = ((Double)dynaValidatorForm.get("awBasesalaryp5")).doubleValue();
                            }
                            break;
                        case 6:
                            String strBaseSalary6 = (String)dynaValidatorForm.get("strBasesalaryp6");
                            if(strBaseSalary6 !=null && !strBaseSalary6.equals(EMPTY_STRING)){
                                double calBase = 0;
                                try{
                                    calBase = formatStringToDouble(strBaseSalary6);
                                }catch(NumberFormatException ne){
                                    saveActionMessage(request);
                                    return navigator;
                                }
                                dynaValidatorForm.set("basesalaryp6", new Double(calBase));
                                newBaseSalaryP6 = calBase;
                            }
                            if(dynaValidatorForm.get("awBasesalaryp6") != null){
                                oldBaseSalaryP6 = ((Double)dynaValidatorForm.get("awBasesalaryp6")).doubleValue();
                            }
                            break;
                        case 7:
                            String strBaseSalary7 = (String)dynaValidatorForm.get("strBasesalaryp7");
                            if(strBaseSalary7 !=null && !strBaseSalary7.equals(EMPTY_STRING)){
                                double calBase = 0;
                                try{
                                    calBase = formatStringToDouble(strBaseSalary7);
                                }catch(NumberFormatException ne){
                                    saveActionMessage(request);
                                    return navigator;
                                }
                                dynaValidatorForm.set("basesalaryp7", new Double(calBase));
                                newBaseSalaryP7 = calBase;
                            }
                            if(dynaValidatorForm.get("awBasesalaryp7") != null){
                                oldBaseSalaryP7 = ((Double)dynaValidatorForm.get("awBasesalaryp7")).doubleValue();
                            }
                            break;
                        case 8:
                            String strBaseSalary8 = (String)dynaValidatorForm.get("strBasesalaryp8");
                            if(strBaseSalary8 !=null && !strBaseSalary8.equals(EMPTY_STRING)){
                                double calBase = 0;
                                try{
                                    calBase = formatStringToDouble(strBaseSalary8);
                                }catch(NumberFormatException ne){
                                    saveActionMessage(request);
                                    return navigator;
                                }
                                dynaValidatorForm.set("basesalaryp8", new Double(calBase));
                                newBaseSalaryP8 = calBase;
                            }
                            if(dynaValidatorForm.get("awBasesalaryp8") != null){
                                oldBaseSalaryP8 = ((Double)dynaValidatorForm.get("awBasesalaryp8")).doubleValue();
                            }
                            break;
                        case 9:
                            String strBaseSalary9 = (String)dynaValidatorForm.get("strBasesalaryp9");
                            if(strBaseSalary9 !=null && !strBaseSalary9.equals(EMPTY_STRING)){
                                double calBase = 0;
                                try{
                                    calBase = formatStringToDouble(strBaseSalary9);
                                }catch(NumberFormatException ne){
                                    saveActionMessage(request);
                                    return navigator;
                                }
                                dynaValidatorForm.set("basesalaryp9", new Double(calBase));
                                newBaseSalaryP9 = calBase;
                            }
                            if(dynaValidatorForm.get("awBasesalaryp9") != null){
                                oldBaseSalaryP9 = ((Double)dynaValidatorForm.get("awBasesalaryp9")).doubleValue();
                            }
                            break;
                        case 10:
                            String strBaseSalary10 = (String)dynaValidatorForm.get("strBasesalaryp10");
                            if(strBaseSalary10 !=null && !strBaseSalary10.equals(EMPTY_STRING)){
                                double calBase = 0;
                                try{
                                    calBase = formatStringToDouble(strBaseSalary10);
                                }catch(NumberFormatException ne){
                                    saveActionMessage(request);
                                    return navigator;
                                }
                                dynaValidatorForm.set("basesalaryp10", new Double(calBase));
                                newBaseSalaryP10 = calBase;
                            }
                            if(dynaValidatorForm.get("awBasesalaryp10") != null){
                                oldBaseSalaryP10 = ((Double)dynaValidatorForm.get("awBasesalaryp10")).doubleValue();
                            }
                            break;
                    }
                    counter++;
                }
                //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
                
                /* check for new/insert record. if not compare data and set update flag */
                if(!acType.equalsIgnoreCase(TypeConstants.INSERT_RECORD)){
                    if(((newCalculationBase > oldCalculationBase) || (newCalculationBase < oldCalculationBase)) || ((!newJobCode.equalsIgnoreCase(oldJobCode))) ||
                            ((!newAppointmentType.equalsIgnoreCase(oldAppointmentType))) || ((oldEffectiveDate.compareTo(newEffectiveDate) < 0 || oldEffectiveDate.compareTo(newEffectiveDate) > 0))
                            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
                            || ((newBaseSalaryP1 > oldBaseSalaryP1) || (newBaseSalaryP1 < oldBaseSalaryP1))
                            || ((newBaseSalaryP2 > oldBaseSalaryP2) || (newBaseSalaryP2 < oldBaseSalaryP2))
                            || ((newBaseSalaryP3 > oldBaseSalaryP3) || (newBaseSalaryP3 < oldBaseSalaryP3))
                            || ((newBaseSalaryP4 > oldBaseSalaryP4) || (newBaseSalaryP4 < oldBaseSalaryP4))
                            || ((newBaseSalaryP5 > oldBaseSalaryP5) || (newBaseSalaryP5 < oldBaseSalaryP5))
                            || ((newBaseSalaryP6 > oldBaseSalaryP6) || (newBaseSalaryP6 < oldBaseSalaryP6))
                            || ((newBaseSalaryP7 > oldBaseSalaryP7) || (newBaseSalaryP7 < oldBaseSalaryP7))
                            || ((newBaseSalaryP8 > oldBaseSalaryP8) || (newBaseSalaryP8 < oldBaseSalaryP8))
                            || ((newBaseSalaryP9 > oldBaseSalaryP9) || (newBaseSalaryP9 < oldBaseSalaryP9))
                            || ((newBaseSalaryP10 > oldBaseSalaryP10) || (newBaseSalaryP10 < oldBaseSalaryP10))
                            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
                            || isAnnivDateChanged ) {
                        dynaValidatorForm.set("acType", TypeConstants.UPDATE_RECORD);
                        acType = TypeConstants.UPDATE_RECORD;
                        dynaValidatorForm.set("acType", acType);
                    }
                }
                
                //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
                if(acType.equalsIgnoreCase(TypeConstants.INSERT_RECORD)){
                    //insert default value as 0.00 when inserted for first time
                    dynaValidatorForm.set("basesalaryp1",0.00);
                    dynaValidatorForm.set("basesalaryp2",0.00);
                    dynaValidatorForm.set("basesalaryp3",0.00);
                    dynaValidatorForm.set("basesalaryp4",0.00);
                    dynaValidatorForm.set("basesalaryp5",0.00);
                    dynaValidatorForm.set("basesalaryp6",0.00);
                    dynaValidatorForm.set("basesalaryp7",0.00);
                    dynaValidatorForm.set("basesalaryp8",0.00);
                    dynaValidatorForm.set("basesalaryp9",0.00);
                    dynaValidatorForm.set("basesalaryp10",0.00);
                }
                //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
                
                if(acType.equalsIgnoreCase(TypeConstants.INSERT_RECORD) || acType.equalsIgnoreCase(TypeConstants.UPDATE_RECORD)){
                    dynaValidatorForm.set("updateTimestamp",dbTimestamp.toString());
                    dynaValidatorForm.set("updateUser",userId);
                    /* database call using webtransaction */
                    webTxnBean.getResults(request,"updBudgetPersons", dynaValidatorForm);
                    // Update the proposal hierarchy sync flag
                    updateProposalSyncFlags(request, proposalNumber);
                }
                
                
                
                /* check for DELETED records if any */
                Vector deletedBudgetPersons = (Vector)session.getAttribute("DeletedBudgetPersonData");
                if(deletedBudgetPersons != null){
                    for(index=0; index<deletedBudgetPersons.size(); index++){
                        dynaValidatorForm = (DynaValidatorForm)deletedBudgetPersons.get(index);
                        dynaValidatorForm.set("acType",TypeConstants.DELETE_RECORD);
                        /* database call using webtransaction */
                        webTxnBean.getResults(request,"updBudgetPersons", dynaValidatorForm);
                        // Update the proposal hierarchy sync flag
                        updateProposalSyncFlags(request, proposalNumber);
                    }
                }
                
                //Added for COEUSQA-2553 Lite: Changing base salary in a budget doesn't update requested salary in all budget periods -start
                BudgetInfoBean budgetInfoBean =(BudgetInfoBean)session.getAttribute("budgetInfoBean");
                calulateAllPeriodsAndSaveBudget(budgetInfoBean, session);
                // COEUSQA-2553 -End
                
                /* reset keyfields and actype flag */
                //Commented and added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
                //resetBudgetPersonKeys(budgetPersonsList, numberOfPersons);
                //Above line commented because for every person save it is reseting the values, which was not updating properly in the DB
                resetBudgetPersonKeys(budgetPersonsList, index+1);
                //Commented and added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end
                session.setAttribute("budgetPersonsDynaBean" ,coeusDynaBeanList);
                session.removeAttribute("DeletedBudgetPersonData");
                //System.out.println("*** EXIT saveBudgetPersons ***");
            }
        }
        return navigator;
        
    }
    
    /**
     * reset keys required for update/delete when done with save
     * need to analyze this method - remove if not required
     * @param budgetPersonsList
     * @param numberOfPersons
     * @throws Exception
     */
    private void resetBudgetPersonKeys(List budgetPersonsList, int numberOfPersons) throws Exception{
        //System.out.println("*** ENTER resetBudgetPersonKeys ***");
        DateUtils dateUtils = new DateUtils();
        for(int index = 0; index < numberOfPersons; index++){
            DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)budgetPersonsList.get(index);
            java.sql.Date effectiveDate  = dateUtils.getSQLDate((String)dynaValidatorForm.get("effectiveDate").toString());
            //java.sql.Date effectiveDate = new java.sql.Date(dateFormat.parse(dynaValidatorForm.get("effectiveDate").toString()).getTime());
            dynaValidatorForm.set("awJobCode", dynaValidatorForm.get("jobCode"));
            dynaValidatorForm.set("awNonEmployeeFlag" ,dynaValidatorForm.get("nonEmployeeFlag"));
            dynaValidatorForm.set("awCalculationBase", dynaValidatorForm.get("calculationBase"));
            dynaValidatorForm.set("awAppointmentType", dynaValidatorForm.get("appointmentType"));
            dynaValidatorForm.set("awEffectiveDate", effectiveDate);
            dynaValidatorForm.set("acType", EMPTY_STRING);
        }
        //System.out.println("*** EXIT resetBudgetPersonKeys ***");
    }
    
    /**
     *
     * Method to Add Person line Item
     * @return String
     * @param dynaForm
     * @throws Exception
     */
    private String addPersonLineItemDetails(CoeusDynaBeansList coeusDynaBeanList,
    HttpServletRequest request) throws Exception{
        //System.out.println("*** ENTER addPersonLineItemDetails ***");
        WebTxnBean webTxnBean = new WebTxnBean();
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        List budgetPersonsList = coeusDynaBeanList.getList();
        int numberOfPersons = budgetPersonsList.size();
        for(int index=0; index < numberOfPersons; index++){
            DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)budgetPersonsList.get(index);
            //take the calculationBase value from getParameter and remove all the values and set to Dynavalidator form
//            String strCalBase = request.getParameter("dynaFormData["+index+"].strCalculationBase");
//            if(strCalBase !=null && !strCalBase.equals(EMPTY_STRING)){
//                strCalBase = strCalBase.replaceAll("[$,/,]","");
//                dynaValidatorForm.set("calculationBase", new Double(strCalBase));
//            }
            String strCalBase = request.getParameter("dynaFormData["+index+"].strCalculationBase");
            if(strCalBase !=null && !strCalBase.equals(EMPTY_STRING)){
                double calBase = 0;
                try{
                    calBase = formatStringToDouble(strCalBase); 
                }catch(NumberFormatException ne){
                    ActionMessages actionMessages = new ActionMessages();
                    actionMessages.add("persons.baseSalary.invalid", new ActionMessage(
                    "persons.baseSalary.invalid") );
                    saveMessages(request, actionMessages);
                    navigator="success";
                    return navigator;
                }
                dynaValidatorForm.set("calculationBase", new Double(calBase));
            }
           // Added for Case 4145 - Lite budget personnel save problem when adding more rows w/out saving in between -Start
            String strSalAnnivDate = request.getParameter("dynaFormData["+index+"].strSalaryAnniversaryDate");
            if(strSalAnnivDate !=null && !strSalAnnivDate.equals(EMPTY_STRING)){
                DateUtils dateUtils = new DateUtils();
                try {
                    strSalAnnivDate = dateUtils.formatDate(strSalAnnivDate,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
                    if (strSalAnnivDate==null) {
                        ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("persons.salaryAnnivDate.required", new ActionMessage(
                                "persons.salaryAnnivDate.required") );
                        saveMessages(request, actionMessages);
                        navigator="success";
                        return navigator;
                    }
                } catch (Exception e) {
                    ActionMessages actionMessages = new ActionMessages();
                    actionMessages.add("persons.salaryAnnivDate.required", new ActionMessage(
                            "persons.salaryAnnivDate.required") );
                    saveMessages(request, actionMessages);
                    navigator="success";
                    return navigator;
                }
                Date formatedAnnivDate = dateUtils.getSQLDate(strSalAnnivDate);
                dynaValidatorForm.set("salaryAnniversaryDate",formatedAnnivDate);
                dynaValidatorForm.set("awSalaryAnniversaryDate",formatedAnnivDate);
            }
            // Added for Case 4145 - Lite budget personnel save problem when adding more rows w/out saving in between -End
        }
        String  jobCode = EMPTY_STRING;
        String appointmentType = EMPTY_STRING;
        double salary = 0.00;
        ServletContext servletContext = session.getServletContext();
        ModuleConfig moduleConfig = RequestUtils.getModuleConfig(request, servletContext);
        FormBeanConfig formConfig = moduleConfig.findFormBeanConfig("budgetPersonsData");
        DynaActionFormClass dynaClass = DynaActionFormClass.createDynaActionFormClass(formConfig);
        DynaActionForm dynaActionForm = (DynaActionForm)dynaClass.newInstance();
        DynaBean newDynaForm = ((DynaBean)dynaActionForm).getDynaClass().newInstance();
        
        String fullName = request.getParameter("fullName");
        String personId =  request.getParameter("personId");
        //Added for Case 2918 – Use of Salary Anniversary Date for calculating inflation in budget development module-Start        
        String salaryAnnivDate =  (String)session.getAttribute("srcSalaryAnnivDate");
        //Added for Case 2918 – Use of Salary Anniversary Date for calculating inflation in budget development module-End
        String nonEmpFlag = (String)session.getAttribute("type");
        
        ProposalBudgetHeaderBean proposalBudgetHeaderBean
        = (ProposalBudgetHeaderBean)session.getAttribute("ProposalBudgetHeaderBean");
        String proposalNumber = proposalBudgetHeaderBean.getProposalNumber();
        int versionNumber = proposalBudgetHeaderBean.getVersionNumber();
        Date startDate = proposalBudgetHeaderBean.getProposalStartDate();
        
        //Added for TBA Persons - start - 3
        String tbaFlag = "false";
        tbaFlag = request.getParameter("tbaFlag");
        
        if(tbaFlag != null && tbaFlag.equals("true")){
            personId = generatePersonIdForTBA(request);                     
            fullName = request.getParameter("personName");
            jobCode = request.getParameter("jobCode");
            nonEmpFlag = "tbaSearch";
            // 4493: While adding a TBA appointment type should be defaulted to 12 Months - Start
            String defaultTbaApnmntTypeCode = fetchDefaultTBAAppointmentTypeCode(request);
            if(defaultTbaApnmntTypeCode != null && !EMPTY_STRING.equals(defaultTbaApnmntTypeCode)){
                String defaultTbaApntmntTypeDesc = fetchAppointmentTypeDescription(defaultTbaApnmntTypeCode, request);
                if(defaultTbaApntmntTypeDesc != null && !EMPTY_STRING.equals(defaultTbaApntmntTypeDesc)){
                    appointmentType  = defaultTbaApntmntTypeDesc;
                } else {
                    appointmentType = REG_EMPLOYEE;
                }
            } else {
                appointmentType = REG_EMPLOYEE;
            }
            // 4493: While adding a TBA appointment type should be defaulted to 12 Months - End
        }
        //Added for TBA Persons - end - 3
            
        //Case 2729 : Persons JobCode is not coming forward to budget -Start
        HashMap hmPersonListParam = new HashMap();
        hmPersonListParam.put("personId",personId);
        /* use webtransaction and get data from database */
        Hashtable htPersonsListData =
        (Hashtable)webTxnBean.getResults(request, "getAppointmentsPerson", hmPersonListParam);
        Vector vecBudgetPersons = (Vector)htPersonsListData.get("getAppointmentsPerson");

        if(vecBudgetPersons !=null && vecBudgetPersons.size() >0){

            //            if(vecBudgetPersons.size()>1){
            //                //Call Multiple Appointments Screen
            //            }else{
            DynaActionForm dynaForm = (DynaActionForm) vecBudgetPersons.get(0);
            jobCode =(String) dynaForm.get("jobCode");
            appointmentType = (String)dynaForm.get("appointmentType");

            if(dynaForm.get("calculationBase") !=null){
                salary =  ((Double)dynaForm.get("calculationBase")).doubleValue();

            }

        }
        //Default Job Code - Start
        if(jobCode.equals("null") || jobCode.equals(EMPTY_STRING)){
            Map hmDefaultJobCode  = new HashMap();
            hmDefaultJobCode.put("jobCode", "DEFAULT_JOB_CODE");
            hmDefaultJobCode =(Hashtable)webTxnBean.getResults(request, "getDefaultJobCode", hmDefaultJobCode);
            hmDefaultJobCode = (HashMap)hmDefaultJobCode.get("getDefaultJobCode");   
            jobCode = (String)hmDefaultJobCode.get("ls_value");
        }
        //Default Job Code - End        
        //Case 2729 : Persons JobCode is not coming forward to budget -End            
       
        String strEffectiveDate = dateFormat.format(startDate);
        
        /* add new person - set unknown fields with default data */
        newDynaForm.set("proposalNumber",proposalNumber);
        newDynaForm.set("versionNumber", new Integer(versionNumber));
        newDynaForm.set("personId",personId);
        newDynaForm.set("jobCode",jobCode);
        newDynaForm.set("effectiveDate",strEffectiveDate);
        //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
        //newDynaForm.set("calculationBase",new Double(salary));
        if(checkViewInstitutionalSalariesRight(request, proposalNumber, personId)){
            newDynaForm.set("calculationBase",new Double(salary));
        }else{
            newDynaForm.set("calculationBase",new Double(0.00));
        }
        //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
        newDynaForm.set("appointmentType",appointmentType);
        newDynaForm.set("updateTimestamp",EMPTY_STRING);
        newDynaForm.set("updateUser",EMPTY_STRING);
        newDynaForm.set("personName",fullName);
        newDynaForm.set("awJobCode",jobCode);
        newDynaForm.set("awEffectiveDate",startDate);
        newDynaForm.set("awAppointmentType",appointmentType);
        newDynaForm.set("awCalculationBase",new Double(salary));
        newDynaForm.set("awAppointmentType",appointmentType);
        //Added for Case 2918 – Use of Salary Anniversary Date for calculating inflation in budget development module-Start   
        if(salaryAnnivDate != null){
            DateUtils dtUtils = new DateUtils();
            String strAnnivDate = dtUtils.formatDate(salaryAnnivDate.toString(),SIMPLE_DATE_FORMAT);
            Date formatedAnnivDate = dtUtils.getSQLDate(strAnnivDate);
            newDynaForm.set("salaryAnniversaryDate",formatedAnnivDate);
            newDynaForm.set("awSalaryAnniversaryDate",formatedAnnivDate);
        }
        session.removeAttribute("srcSalaryAnnivDate");
        //Added for Case 2918 – Use of Salary Anniversary Date for calculating inflation in budget development module-End          
        
        if(nonEmpFlag!=null && nonEmpFlag.equals("budgetPersonSearch")  || nonEmpFlag.equals("tbaSearch")) {
            newDynaForm.set("nonEmployeeFlag","N");
        } else if(nonEmpFlag!=null && nonEmpFlag.equals("budgetRolodexSearch")) {
            newDynaForm.set("nonEmployeeFlag","Y");
        }
        newDynaForm.set("acType",TypeConstants.INSERT_RECORD);
        
        budgetPersonsList.add(newDynaForm);
        coeusDynaBeanList.setList(budgetPersonsList);
        session.setAttribute("budgetPersonsDynaBean" ,coeusDynaBeanList);
        navigator = "success";
        //System.out.println("*** EXIT addPersonLineItemDetails ***");
        return navigator;
    }
    
   /*
    */
    private void setOnOffCampusFlag(Vector vecBudgetData, HttpServletRequest request)  throws Exception{
        //System.out.println("*** ENTER setOnOffCampusFlag ***");
     // Case id 2924 - start
        HttpSession session = request.getSession();
        BudgetInfoBean budgetInfoBean =(BudgetInfoBean)session.getAttribute("budgetInfoBean");
     // Case id 2924 - end
        for(int index = 0; index <vecBudgetData.size(); index++){
            DynaValidatorForm dynaForm = (DynaValidatorForm)vecBudgetData.get(index);
         // case id 2924 - start
         //    String costElementCode = (String)dynaForm.get("costElement");
            boolean isCampusFlag = budgetInfoBean.isOnOffCampusFlag();
         //    boolean isCampusFlag = getCampusFlag(request, costElementCode);
         // case id 2924 - end
            dynaForm.set("onOffCampusFlag",new Boolean(isCampusFlag));
        }
        //System.out.println("*** EXIT setOnOffCampusFlag ***");
    }
    
    /**
     * This method is to check data inserted in budget cal amounts
     * for all new records (personnel line item)
     * @param vecBudgetCalAmts
     * @throws Exception
     * @return boolean
     */
    private boolean checkDataInsertedInBudgetCalAmts(int budgetPeriod, int lineItemNumber, int personNumber, Vector vecBudgetCalAmts) throws Exception{
        boolean dataInserted = false;
        int calRecords = vecBudgetCalAmts.size();
        for(int index=0; index<calRecords; index++){
            DynaValidatorForm dynaForm = (DynaValidatorForm)vecBudgetCalAmts.get(index);
            int dynaBudgetPeriod = ((Integer)dynaForm.get("budgetPeriod")).intValue();
            int dynaLineItemNumber = ((Integer)dynaForm.get("lineItemNumber")).intValue();
            int dynaPersonNumber = ((Integer)dynaForm.get("personNumber")).intValue();
            if((budgetPeriod == dynaBudgetPeriod) && (lineItemNumber == dynaLineItemNumber) &&
            (personNumber == dynaPersonNumber)){
                dataInserted = true;
            }
        }
        return dataInserted;
    }
    
    /**
     * This method is to create new personnel cal amounts dyna form
     *
     * @param dynaValidatorForm
     * @throws Exception
     * @return boolean
     */
    private Vector getNewPersonnelCalAmts(Vector vecNewCalAmts, Integer personNumber, Vector vecBudgetPersonnelCalAmts,
    HttpServletRequest request)throws Exception{
        //System.out.println("*** ENTER getNewPersonnelCalAmts ***");
        DynaBean budgetPersonnelCalAmtsDynaForm = null;
        BeanUtilsBean copyBean = null;
        Timestamp dbTimestamp = prepareTimeStamp();
        for(int i=0; i<vecNewCalAmts.size(); i++){
            copyBean = new BeanUtilsBean();
            DynaValidatorForm dynaForm = (DynaValidatorForm)vecNewCalAmts.get(i);
            budgetPersonnelCalAmtsDynaForm = getDynaForm("budgetPersonnelCalAmtsData", request);
            copyBean.copyProperties(budgetPersonnelCalAmtsDynaForm, dynaForm);
            budgetPersonnelCalAmtsDynaForm.set("personNumber", personNumber);
            budgetPersonnelCalAmtsDynaForm.set("updateTimestamp", dbTimestamp.toString());
            budgetPersonnelCalAmtsDynaForm.set("avUpdateTimestamp", dbTimestamp.toString());
            vecBudgetPersonnelCalAmts.add(budgetPersonnelCalAmtsDynaForm);
        }
        //System.out.println("*** EXIT getNewPersonnelCalAmts ***");
        return vecBudgetPersonnelCalAmts;
    }
    
    /**
     * This method is to update fields for new personnel line item
     *
     * @param dynaValidatorForm
     * @throws Exception
     * @return boolean
     */
    private void updateNewPersonnelLineItem(DynaValidatorForm dynaValidatorForm, CoeusDynaBeansList dynaForm,
    HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        boolean lineItemWindowOpened = false;
        System.out.println("*** ENTER updateNewPersonnelLineItem ***" + dynaValidatorForm.get("fullName"));
        Vector vecBudgetDetails = (Vector)session.getAttribute("BudgetDetailsData");
        Vector vecBudgetCalAmts = (Vector)session.getAttribute("BudgetDetailCalAmts");
        Vector vecBudgetPersonnelCalAmts = (Vector)session.getAttribute("BudgetPersonnelCalAmts");
        BudgetInfoBean budgetInfoBean =(BudgetInfoBean)session.getAttribute("budgetInfoBean");
        
        Integer personNumber = (Integer)dynaValidatorForm.get("personNumber");
        String newCostElement =  (dynaValidatorForm.get("costElement").toString().trim());
        String newCostElementDescr =  (dynaValidatorForm.get("costElementDescription").toString().trim());
        double newPercentCharged =  ((Double)dynaValidatorForm.get("percentCharged")).doubleValue();
        double newPercentEffort =  ((Double)dynaValidatorForm.get("percentEffort")).doubleValue();
        int budgetCategoryCode = getBudgetCategoryCode(newCostElement ,request);
        /* get personnel data line item number */
        int personnelLineItemNumber = ((Integer)dynaValidatorForm.get("lineItemNumber")).intValue();
        /* get personnel data budget period */
        int personnelBudgetPeriod = ((Integer)dynaValidatorForm.get("budgetPeriod")).intValue();
   
   // Case id 2924 - start
        // Modified for Case 3956 - On/Off Campus flag is not being set in Lite  -Start
//        boolean isCampusFlag = budgetInfoBean.isOnOffCampusFlag();
//        boolean isCampusFlag = getCampusFlag(request, newCostElement); 
         boolean isCampusFlag = false;
         if(budgetInfoBean.isDefaultIndicator()){
             isCampusFlag = getCampusFlag(request, newCostElement);
         }else{
             isCampusFlag = budgetInfoBean.isOnOffCampusFlag();
         }
                   // Modified for Case 3956 - On/Off Campus flag is not being set in Lite  -End 
        // Modified for Case 3956 - On/Off Campus flag is not being set in Lite  -End
   // Case id 2924 - end
        
        /* set cost sharing percent */
        dynaValidatorForm.set("costSharingPercent", new Double(newPercentEffort - newPercentCharged));
        /* set campus flag for new cost element */
       // Commented for Case #3507 -  F&A for Personnel line items - CoeusLite (on campus vs. off campus) - Start
        //OnOffCampusFlag() value should take from what user has entered. It should not take OnOffCampusflag from CostElement table
//        dynaValidatorForm.set("onOffCampusFlag", new Boolean(isCampusFlag));
        // Commented for Case #3507 -  F&A for Personnel line items - CoeusLite (on campus vs. off campus) - End
        //Modified for Case #3956 -  On/off campus flag is not being set in Lite -Start
        String reCalculate = request.getParameter("reCalculate");
        if(reCalculate == null ){
            dynaValidatorForm.set("onOffCampusFlag", new Boolean(isCampusFlag));
        }        
        //Modified for Case #3956 -  On/off campus flag is not being set in Lite -End
        int budgetSize = vecBudgetDetails.size();
        for(int indexBudget=0; indexBudget<budgetSize; indexBudget++){
            DynaValidatorForm dynaBudgetDataForm = (DynaValidatorForm)vecBudgetDetails.get(indexBudget);
            /* get budget detail data line item number */
            int detailLineItemNumber = ((Integer)dynaBudgetDataForm.get("lineItemNumber")).intValue();
            /* get budget detail data budget period */
            int detailBudgetPeriod = ((Integer)dynaBudgetDataForm.get("budgetPeriod")).intValue();
            //System.out.println("Line item numbers  ----------------DL" + detailLineItemNumber + "PL" + personnelLineItemNumber);
            //System.out.println("budget Periods  ----------------DL" + detailBudgetPeriod + "PL" + personnelBudgetPeriod);
            if((detailLineItemNumber == personnelLineItemNumber) && (detailBudgetPeriod == personnelBudgetPeriod)){
                /* set the new cost element to budget details form - if exsting cost element changed for the new record
                 * each time caculation is performed - to make sure that we have the latest cost element for calculation.
                 */
                //System.out.println("<--------------- matching found L" + detailLineItemNumber + "P" + +detailBudgetPeriod + "C" + newCostElement);
                dynaBudgetDataForm.set("costElement", newCostElement);
                dynaBudgetDataForm.set("costElementDescription", newCostElementDescr);
                dynaBudgetDataForm.set("budgetCategoryCode", new Integer(budgetCategoryCode));
                // Commented for Case #3507 -  F&A for Personnel line items - CoeusLite (on campus vs. off campus) - Start
                // Commented OnOffCampusFlag() value should take from what user has entered. It should not take OnOffCampusflag from CostElement table
//                dynaBudgetDataForm.set("onOffCampusFlag", new Boolean(isCampusFlag));
                // Commented for Case #3507 -  F&A for Personnel line items - CoeusLite (on campus vs. off campus) - End
                //Modified for Case #3956 -  On/off campus flag is not being set in Lite -Start
                if(reCalculate == null){
                    dynaBudgetDataForm.set("onOffCampusFlag", new Boolean(isCampusFlag));
                }
                //Modified for Case #3956 -  On/off campus flag is not being set in Lite -End
                Vector vecNewCalAmts = null;
                
                /* remove calculation form data stored earlier - if any */
                vecBudgetCalAmts = removeOldCalAmtsData(vecBudgetCalAmts, detailBudgetPeriod, detailLineItemNumber, 0 , request);
                vecBudgetPersonnelCalAmts = removeOldCalAmtsData(vecBudgetPersonnelCalAmts, detailBudgetPeriod, detailLineItemNumber, 1 ,request);
                
                //System.out.println("calling getCalculatedAmtsData detailBudgetPeriod ====> " + detailBudgetPeriod);
                vecNewCalAmts = getCalculatedAmtsData(budgetInfoBean, detailBudgetPeriod, detailLineItemNumber, newCostElement, dynaForm , request, vecBudgetCalAmts);
                
                // Check for the lineItemwindowOpenedFlag from the list and check the condition for Case 2960 - Start               
                List budgetPersonnelList = dynaForm.getList();
                if (budgetPersonnelList !=null && budgetPersonnelList.size() > 0){
                    DynaValidatorForm calAllPeriodsForm = (DynaValidatorForm) budgetPersonnelList.get(0);
                    Boolean lineItemwinOpened = (Boolean) calAllPeriodsForm.get("lineItemWindowOpenedFlag");
                    
                    if(lineItemwinOpened !=null){
                        lineItemWindowOpened = lineItemwinOpened.booleanValue();
                    }
                }
                // Added for Case 2960 - End
                
                if(vecNewCalAmts!= null && vecNewCalAmts.size() > 0){
                    /* check whether this routine is invoke when line item details window is opened.
                     * if yes, check for applyRateFlag and set the value for new cal amounts data.
                     */
                    //System.out.println("new cal amounts size ====> " + vecNewCalAmts.size());
                    if(lineItemWindowOpened){
                        List budgetLineItemList = dynaForm.getBeanList();
                        for(int index=0; index<vecNewCalAmts.size(); index++){
                            DynaValidatorForm dynaValidForm = (DynaValidatorForm)vecNewCalAmts.get(index);
                            //int newBudgetPeriod = ((Integer)dynaValidForm.get("budgetPeriod")).intValue();
                            //int newLineItemNumber = ((Integer)dynaValidForm.get("lineItemNumber")).intValue();
                            int newRateClassCode = ((Integer)dynaValidForm.get("rateClassCode")).intValue();
                            for(int frmIndex=0; frmIndex<budgetLineItemList.size(); frmIndex++){
                                DynaValidatorForm dynaLineItemForm = (DynaValidatorForm)budgetLineItemList.get(frmIndex);
                                //int dynaBudgetPeriod = ((Integer)dynaLineItemForm.get("budgetPeriod")).intValue();
                                //int dynaLineItemNumber = ((Integer)dynaLineItemForm.get("lineItemNumber")).intValue();
                                int dynaRateClassCode = ((Integer)dynaLineItemForm.get("rateClassCode")).intValue();
                                boolean applyRateFlag = ((Boolean)dynaLineItemForm.get("applyRateFlag")).booleanValue();
                                if(newRateClassCode == dynaRateClassCode){
                                    //(newBudgetPeriod == dynaBudgetPeriod) && (
                                    dynaValidForm.set("applyRateFlag", new Boolean(applyRateFlag));
                                    vecNewCalAmts.set(index, dynaValidForm);
                                }
                            }
                        }
                    }
                    //System.out.println("new cal amounts size ====> " + vecNewCalAmts.size());
                    vecBudgetCalAmts.addAll( vecNewCalAmts );
                    vecBudgetPersonnelCalAmts = getNewPersonnelCalAmts(vecNewCalAmts, personNumber,
                    vecBudgetPersonnelCalAmts, request);
                }
            }
        }
        session.setAttribute("BudgetDetailCalAmts", vecBudgetCalAmts);
        session.setAttribute("BudgetPersonnelCalAmts", vecBudgetPersonnelCalAmts);
        //System.out.println("*** EXIT updateNewPersonnelLineItem ***");
    }
    
    /**
     * This method is to check for any cost element changes in each line item.
     * if user modified cost element for a row, we need to update budget details and sync it so that
     * it doesn't affect the record created through swing application. Delete that line personnel line item
     * and create a new one (personnel line item created through swing might have more than one person linked
     * to one cost element.
     * @param dynaForm
     * @throws Exception
     * @return
     */
    private HashMap verifyAndUpdateCostElementChanges(CoeusDynaBeansList dynaForm,
    HttpServletRequest request, HashMap hmOpenList) throws Exception{
        //System.out.println("*** ENTER verifyAndUpdateCostElementChanges ***");
        /* get entire budget personnel data */
        HttpSession session = request.getSession();
        Vector vecBudgetPersonnel = (Vector)session.getAttribute("budgetPersonnelData");
        /* get budget details data */
        Vector vecBudgetDetails = (Vector)session.getAttribute("BudgetDetailsData");
         // Remove the Instance level variable and Take for the value from the list and check the condition for Case 2960 - Start   
         int openLineItemNumber = -1;
         int openPersonNumber = -1;
     //    int OPEN_LINE_ITEM_NUMBER =-1;
         Integer openLineItemNo ;
         Integer openPersonNo;
        if( hmOpenList !=null && hmOpenList.size() > 0){
           openLineItemNo = (Integer) hmOpenList.get("OPEN_LINE_ITEM_NUMBER");
           openPersonNo  = (Integer) hmOpenList.get("OPEN_PERSON_NUMBER");
            if(openLineItemNo != null){
                //convert to int ans set it to OPEN_LINE_ITEM_NUMBER
	            openLineItemNumber = openLineItemNo.intValue();
            }
           if(openPersonNo != null){
                openPersonNumber = openPersonNo.intValue();
           }
        }
        //Case 2960 - End        
        
        /* get budget personnel data for the period */
        List budgetPersonnelList = dynaForm.getList();
        List newBudgetPersonnelList = new ArrayList();
        int numberOfItems = budgetPersonnelList.size();
        for(int index=0; index < numberOfItems; index++){
            DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)budgetPersonnelList.get(index);
            //System.out.println("-------------> " + dynaValidatorForm.get("fullName"));
            //printBudgetData();
            String newCostElement =  (dynaValidatorForm.get("costElement").toString().trim());
            String newCostElementDescr =  (dynaValidatorForm.get("costElementDescription").toString().trim());
            String oldCostElement =  (dynaValidatorForm.get("awCostElement").toString().trim());
            String newProjectRole = ((String)dynaValidatorForm.get("projectRole").toString().trim());
            String newPeriodType =  (dynaValidatorForm.get("periodType").toString().trim());
            Double newPercentCharged =  (Double)dynaValidatorForm.get("percentCharged");
            Double newPercentEffort =  (Double)dynaValidatorForm.get("percentEffort");
            java.sql.Date newStartDate = (java.sql.Date)dynaValidatorForm.get("startDate");
            java.sql.Date newEndDate = (java.sql.Date)dynaValidatorForm.get("endDate");
            String acType = (String)dynaValidatorForm.get("acType");
            // Added for Case #3507 -  F&A for Personnel line items - CoeusLite (on campus vs. off campus) - Start
            // Added  OnOffCampusFlag() value should take from what user has entered. It should not take OnOffCampusflag from CostElement table
            Boolean onOffCampusFlag = (Boolean) dynaValidatorForm.get("onOffCampusFlag");
            // Added for Case #3507 -  F&A for Personnel line items - CoeusLite (on campus vs. off campus) - End
            //Code added for bug fix case#2759
            //Added to save description
            String lineItemDescription =(String) dynaValidatorForm.get("lineItemDescription");
            lineItemDescription = (lineItemDescription==null)?EMPTY_STRING:lineItemDescription.trim();
            int lineItemNumber = ((Integer)dynaValidatorForm.get("lineItemNumber")).intValue();
             //COEUSQA-1693 - Cost Sharing Submission - start
            String submitCostSharingFlag = (String) dynaValidatorForm.get("submitCostSharingFlag");
            submitCostSharingFlag =  (submitCostSharingFlag==null)?"Y":submitCostSharingFlag;
             //COEUSQA-1693 - Cost Sharing Submission - end
            //System.out.println("lineItemNumber ---------------> " + lineItemNumber);
            /* get personnel data budget period */
            int personnelBudgetPeriod = ((Integer)dynaValidatorForm.get("budgetPeriod")).intValue();
            if((!newCostElement.equalsIgnoreCase(oldCostElement)) && (!acType.equalsIgnoreCase(TypeConstants.INSERT_RECORD))){
                //System.out.println("*** cost element changed - get new form and update data *** ");
                HashMap newDynalForms = createNewPersonnelDynaForm(personnelBudgetPeriod, dynaValidatorForm ,request);
                DynaBean newPersonnelDynaForm = (DynaBean) newDynalForms.get(BudgetPersonnelDetailsBean.class);
                DynaBean newBudgetDetailForm = (DynaBean) newDynalForms.get(BudgetDetailBean.class);
                newPersonnelDynaForm.set("costElement", newCostElement);
                newPersonnelDynaForm.set("costElementDescription", newCostElementDescr);
                newPersonnelDynaForm.set("projectRole", newProjectRole);
                newPersonnelDynaForm.set("periodType", newPeriodType);
                newPersonnelDynaForm.set("percentCharged", newPercentCharged);
                newPersonnelDynaForm.set("percentEffort", newPercentEffort);
                newPersonnelDynaForm.set("startDate", newStartDate);
                newPersonnelDynaForm.set("endDate", newEndDate);
                // Added for Case #3507 -  F&A for Personnel line items - CoeusLite (on campus vs. off campus) - Start
                // Added OnOffCampusFlag() value should take from what user has entered. It should not take OnOffCampusflag from CostElement table
                newPersonnelDynaForm.set("onOffCampusFlag",onOffCampusFlag);
                // Added for Case #3507 -  F&A for Personnel line items - CoeusLite (on campus vs. off campus) - End
                 //Added for Case #3956 -  On/off campus flag is not being set in Lite -Start
                newBudgetDetailForm.set("onOffCampusFlag",onOffCampusFlag);
                 //Added for Case #3956 -  On/off campus flag is not being set in Lite -End
                //COEUSQA-1693 - Cost Sharing Submission - start
                newBudgetDetailForm.set("submitCostSharingFlag",submitCostSharingFlag);
                newPersonnelDynaForm.set("submitCostSharingFlag",submitCostSharingFlag);
                //COEUSQA-1693 - Cost Sharing Submission - end
                //Code added for bug fix case#2759
                //Added to save description
                newPersonnelDynaForm.set("lineItemDescription", lineItemDescription);
                //System.out.println("<------------------getting new form--------------->");
                /* check whether user clicked on edit and there is a change in line item
                 * i.e. whether line item is removed and a new one is created */
                //System.out.println("OPEN_LINE_ITEM_NUMBER--------------->" + OPEN_LINE_ITEM_NUMBER);
                if((openLineItemNumber >=0 ) && (openLineItemNumber == lineItemNumber)) {
                    openLineItemNumber = ((Integer)newPersonnelDynaForm.get("lineItemNumber")).intValue();
                    //System.out.println("<------------------matching found and the new number is --------------->"+OPEN_LINE_ITEM_NUMBER);
                } 
                resetPersonnelBudgetData(dynaValidatorForm, request);
                newBudgetPersonnelList.add(newPersonnelDynaForm);
                vecBudgetPersonnel.add(newPersonnelDynaForm);
                vecBudgetDetails.add(newBudgetDetailForm);
            }else{
                newBudgetPersonnelList.add(dynaValidatorForm);
            }
        }
        dynaForm.setList(newBudgetPersonnelList);
         // Remove the Instance level variable for Case 2960 - Start   
        hmOpenList.clear();
        hmOpenList.put("OPEN_LINE_ITEM_NUMBER", new Integer(openLineItemNumber));
        hmOpenList.put("OPEN_PERSON_NUMBER", new Integer(openPersonNumber));
        //Case 2960 - End
        //System.out.println("*** EXIT verifyAndUpdateCostElementChanges ***");
        return hmOpenList;
    }
    
    /**
     * This method is to check data changed in personnel line item
     * @param dynaForm
     * @throws Exception
     * @return
     */
    private boolean checkPersonnelDataChanged(CoeusDynaBeansList dynaForm,
    HttpServletRequest request, HashMap hmOpenList) throws Exception{
        //System.out.println("*** ENTER checkPersonnelDataChanged ***");
        boolean dataChanged = false;
        HttpSession session = request.getSession();       
        /* check for updated cost elements */
        HashMap hmValueList = verifyAndUpdateCostElementChanges(dynaForm, request, hmOpenList);
        
       if( hmValueList !=null && hmValueList.size() >0){
           request.setAttribute("hmValueList", hmValueList);
       }
        
        /* get budget personnel data */
        List budgetPersonnelList = dynaForm.getList();
        int numberOfItems = budgetPersonnelList.size();
        for(int index=0; index < numberOfItems; index++){
            DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)budgetPersonnelList.get(index);
            String acType = (String)dynaValidatorForm.get("acType");
            /* user can modify only fields (in main interface) listed below.
             * cost element change is handled in above routine (verifyAndUpdateCostElementChanges)
             * compare these fields to look for data change
             */
            String newProjectRole = ((String)dynaValidatorForm.get("projectRole").toString().trim());
            String oldProjectRole = ((String)dynaValidatorForm.get("awProjectRole").toString().trim());
            String newPeriodType =  (dynaValidatorForm.get("periodType").toString().trim());
            String oldPeriodType =  (dynaValidatorForm.get("awPeriodType").toString().trim());
            double newPercentCharged = ((Double) dynaValidatorForm.get("percentCharged")).doubleValue();
            double oldPercentCharged =  ((Double)dynaValidatorForm.get("awPercentCharged")).doubleValue();
            double newPercentEffort =  ((Double)dynaValidatorForm.get("percentEffort")).doubleValue();
            double oldPercentEffort =  ((Double)dynaValidatorForm.get("awPercentEffort")).doubleValue();
            
            // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item -Start
            Boolean oldOnOffCampusFlag = (Boolean) dynaValidatorForm.get("awOnOffCampusFlag");
            Boolean newOnOffCampusFlag = (Boolean) dynaValidatorForm.get("onOffCampusFlag");  
            String oldSubmitCostSharingFlag = (String) dynaValidatorForm.get("awSubmitCostSharingFlag");
            String newSubmitCostSharingFlag = (String) dynaValidatorForm.get("submitCostSharingFlag");
            // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item -End
                    
            /* check for new/insert record. if not compare data and set update flag */
            if(acType.equalsIgnoreCase(TypeConstants.INSERT_RECORD)){
                updateNewPersonnelLineItem(dynaValidatorForm, dynaForm, request);
                dataChanged = true;
            }else if(((newPercentCharged > oldPercentCharged) || (newPercentCharged < oldPercentCharged)) ||
            ((newPercentEffort > oldPercentEffort) || (newPercentEffort < oldPercentEffort)) ||
            ((!newPeriodType.equalsIgnoreCase(oldPeriodType))) ||
            ((!newProjectRole.equalsIgnoreCase(oldProjectRole)))) {
                dynaValidatorForm.set("costSharingPercent", new Double(newPercentEffort - newPercentCharged));
                dynaValidatorForm.set("acType", TypeConstants.UPDATE_RECORD);
                dataChanged = true;
            }else if( !(newSubmitCostSharingFlag.equalsIgnoreCase(oldSubmitCostSharingFlag)) || (newOnOffCampusFlag.compareTo(oldOnOffCampusFlag)!=0 ) ){
            dataChanged = true;
        }  // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item 
        }
        
        if(!dataChanged){
            /* check deleted records - budget personnel details */
            Vector vecDeletedBudgetPersonnel = (Vector)session.getAttribute("DeletedBudgetPersonnelData");
            if((vecDeletedBudgetPersonnel != null)){
                dataChanged = true;
            }
        }
        
        
        //System.out.println("*** EXIT checkPersonnelDataChanged ***" +  dataChanged);
        return dataChanged;
    }
    
    
    //    /**
    //     * This method is to calculate and save the Peronnel Budget Details when save is invoked
    //     * @param dynaForm
    //     * @throws Exception
    //     * @return
    //     */
    //    private String savePersonLineItem(CoeusDynaBeansList dynaForm,
    //        HttpServletRequest request) throws Exception{
    //        //System.out.println("*** ENTER savePersonLineItem ***");
    //        String navigator = "success";
    //        HttpSession session = request.getSession();
    //        List budgetPersonnelList = dynaForm.getList();
    //        DynaValidatorForm dynaValidForm = (DynaValidatorForm)budgetPersonnelList.get(0);
    //        int budgetPeriod = ((Integer)dynaValidForm.get("budgetPeriod")).intValue(); //Integer.parseInt(request.getParameter("budgetPeriod"));
    //
    //        boolean dataChanged = checkPersonnelDataChanged(dynaForm, request);
    //        if(dataChanged){
    //            /* calculate line items */
    //            navigator = calculatePeriodLineItem(dynaForm , budgetPeriod);
    //            /* Check the total Cost. isExceeding flag is set in above calculation routine
    //             * If amount exceeding above $9,999,999,999.99 then send error message
    //             */
    //            if(isExceeding){
    //                /* get budget period data from session */
    //                Vector vecBudgetPeriod = (Vector)session.getAttribute("BudgetPeriodData");
    //                /* get budget details data from session */
    //                Vector vecBudgetDetails = (Vector)session.getAttribute("BudgetDetailsData");
    //                setDataForExceedTotalCost(budgetPeriod,vecBudgetPeriod,vecBudgetDetails);
    //                ActionMessages actionMessages = new ActionMessages();
    //                actionMessages.add("budget_common_exceptionCode.1011" , new ActionMessage(
    //                "budget_common_exceptionCode.1011" ) );
    //                saveMessages(request, actionMessages);
    //                return navigator;
    //            }
    //        }
    //        request.setAttribute("SelectedBudgetPeriodNumber", new Integer(budgetPeriod));
    //        //System.out.println("*** EXIT savePersonLineItem ***");
    //        return navigator;
    //    }
    
    /**
     * This method is to calculate the Peronnel Budget Details when calculate is invoked
     * @param dynaForm
     * @throws Exception
     * @return
     */
    private String calculatePersonLineItem(CoeusDynaBeansList dynaForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception{
        System.out.println("*** ENTER calculatePersonLineItem ***");
        String navigator = "success";
        List budgetPersonnelList = dynaForm.getList();
        List budgetList = dynaForm.getBeanList();
        //Added the local variable to avoid the Instance level variable for Case 2960 -Start
        int openLineItemNumber = -1;
        int openPersonNumber = -1;
        HashMap hmOpenList = new HashMap();
        //Added the local variable to avoid the Instance level variable for Case 2960 -End        
        if(budgetList !=null && budgetList.size() >0){
            //Code added for bug fix case#2758 - starts
            //Get budgetPeriod from requestParameter.
            String period = request.getParameter("budgetPeriod");
            period = (period==null)? "1" : period;
            int budgetPeriod = Integer.parseInt(period);
            if(budgetList.get(0) instanceof Integer){
                budgetPeriod = ((Integer)budgetList.get(0)).intValue();
            }
            //Code added for bug fix case#2758 - ends
            boolean isValid = validateBudgetPersonnelDate(dynaForm, request);
            // DynaValidatorForm dynaValidForm = (DynaValidatorForm)budgetPersonnelList.get(0);
            // int budgetPeriod = ((Integer)dynaValidForm.get("budgetPeriod")).intValue(); //Integer.parseInt(request.getParameter("budgetPeriod"));
            if(isValid){
                //System.out.println("*** request.getParameter - EDIT ======> " + request.getParameter("Edit"));
                if(request.getParameter("Edit") != null){
                    int rowIndex = Integer.parseInt(request.getParameter("rowIndex"));
                    DynaValidatorForm personnelDynaForm = (DynaValidatorForm)budgetPersonnelList.get(rowIndex);
                    int personNumber = ((Integer)personnelDynaForm.get("personNumber")).intValue();
                    int lineItemNo = ((Integer)personnelDynaForm.get("lineItemNumber")).intValue();
                    //System.out.println("************ edit mode ********** edit => " + lineItemNo);
                    openLineItemNumber = lineItemNo;
                    openPersonNumber = personNumber;
                    request.setAttribute("personNumber", new Integer(personNumber));
                    request.setAttribute("popUp","fromLocation");
                    //Added the values to hashmap, pass as a paramter and check those valuse in another method for to remove the Instance level variable for Case 2960 -Start
                    //  editLineItemDetails = true;
                    hmOpenList.put("OPEN_LINE_ITEM_NUMBER", new Integer(lineItemNo));
                    hmOpenList.put("OPEN_PERSON_NUMBER", new Integer(personNumber));
                    //Case 2960 -End
                }
                // added for start and End date validation
                
                /* calculate and save data if required */
                boolean dataChanged = checkPersonnelDataChanged(dynaForm, request, hmOpenList);
                
              
                
                HashMap hmValueList = (HashMap) request.getAttribute("hmValueList");
                if( hmValueList ==null){
                    hmValueList = new HashMap();
                }
                //case 2554 fix start
                // if(dataChanged){
                navigator = calculatePeriodLineItem(dynaForm , budgetPeriod ,request, response, hmValueList);
                // } case 2554 fix end
            }
        /* line item number might have changed during calculation. if so the new line item
         * number will be assigned to the variable below in calculation routine and this
         * number is used to open line item details.
         */
            if(openLineItemNumber >=0){
                request.setAttribute("lineItemNumber", new Integer(openLineItemNumber));
            }
            request.setAttribute("SelectedBudgetPeriodNumber", new Integer(budgetPeriod));
        }
        //System.out.println("*** EXIT calculatePersonLineItem ***");
        return navigator;
    }
    
    /**
     * Return a new dynaform
     * @param formName
     */
    private DynaBean getDynaForm(String formName, HttpServletRequest request) throws Exception{
        //System.out.println("*** ENTER getDynaForm ***");
        HttpSession session = request.getSession();
        ServletContext servletContext = session.getServletContext();
        ModuleConfig moduleConfig = RequestUtils.getModuleConfig(request, servletContext);
        FormBeanConfig formConfig = moduleConfig.findFormBeanConfig(formName);
        DynaActionFormClass dynaClass = DynaActionFormClass.createDynaActionFormClass(formConfig);
        DynaActionForm dynaActionForm = (DynaActionForm)dynaClass.newInstance();
        DynaBean newDynaForm = ((DynaBean)dynaActionForm).getDynaClass().newInstance();
        //System.out.println("*** EXIT getDynaForm ***");
        return newDynaForm;
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
    private HashMap prepareBudgetPersonnelData(CoeusVector cvBudgetDetails,CoeusVector cvBudgetPeriod,
    CoeusVector cvBudgetDetailCalAmts, CoeusVector cvBudgetPersonnelData,
    CoeusVector cvBudgetPersonnelCalamts, HttpServletRequest request) throws Exception{
        //System.out.println("*** ENTER prepareBudgetPersonnelData ***");
        BeanUtilsBean copyBean = null;
        HashMap hmBudgetDetail = new HashMap();
        Vector vecBudgetDetails = new Vector();
        Vector vecBudgetPeriod = new Vector();
        Vector vecBudgetDetailCalAmts = new Vector();
        Vector vecBudgetPersonnelData = new Vector();
        Vector vecBudgetPersonnelCalamts = new Vector();
        HashMap hmCostElementData = new HashMap();
        CoeusVector vecCEdetails = new CoeusVector();
        
        /* prepare budget details data */
        if(cvBudgetDetails!= null){
            for(int index = 0; index < cvBudgetDetails.size() ; index++){
                DynaBean budgetDetailDynaForm = null;
                budgetDetailDynaForm = getDynaForm("budgetDetailData", request);
                BudgetDetailBean budgetDetailBean = (BudgetDetailBean)cvBudgetDetails.get(index);
                copyBean = new BeanUtilsBean();
                copyBean.copyProperties(budgetDetailDynaForm,budgetDetailBean);
                vecBudgetDetails.addElement(budgetDetailDynaForm);
                
                /* Set budgetDetail bean with key lineItemNumber to get cost element code
                 * and description later in personnel budget form data.
                 * These values are required for display in personnel budget pages
                 * This additional step is introduced to perform a key fetch using lineItemNumber
                 * rather than looping through this vector again.
                 */
                String lineItemNumber = "L" + budgetDetailBean.getLineItemNumber()+ "P" +budgetDetailBean.getBudgetPeriod();
                System.out.println("Details Bean data : "+lineItemNumber);
                //System.out.println("budget details - key to get cost element ===> " + lineItemNumber + "***" + budgetDetailBean.getCostElement());
                hmCostElementData.put(lineItemNumber, budgetDetailBean);
            }
        }
               
        /* prepare budget periods data */
        if(cvBudgetPeriod!= null){
            for(int ind = 0; ind < cvBudgetPeriod.size() ; ind++){
                DynaBean budgetPeriodDynaForm = null;
                budgetPeriodDynaForm = getDynaForm("budgetPeriodData", request);
                BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)cvBudgetPeriod.get(ind);
                copyBean = new BeanUtilsBean();
                copyBean.copyProperties(budgetPeriodDynaForm,budgetPeriodBean);
                vecBudgetPeriod.addElement(budgetPeriodDynaForm);
            }
        }
        /* prepare budget details calculation data */
        if(cvBudgetDetailCalAmts!= null){
            for(int calInd = 0; calInd < cvBudgetDetailCalAmts.size() ; calInd++){
                DynaBean dynaCalAmtBean = null;
                dynaCalAmtBean = getDynaForm("budgetDetailCalAmtsData", request);
                BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)cvBudgetDetailCalAmts.get(calInd);
                copyBean = new BeanUtilsBean();
                copyBean.copyProperties(dynaCalAmtBean,budgetDetailCalAmountsBean);
                vecBudgetDetailCalAmts.addElement(dynaCalAmtBean);
            }
        }
        /* prepare budget personnel calculation data */
        if(cvBudgetPersonnelCalamts!= null){
            for(int calInd = 0; calInd < cvBudgetPersonnelCalamts.size() ; calInd++){
                DynaBean dynaPersonnelCalAmtsBean = null;
                dynaPersonnelCalAmtsBean = getDynaForm("budgetPersonnelCalAmtsData", request);
                BudgetPersonnelCalAmountsBean budgetPersonnelCalAmountsBean = (BudgetPersonnelCalAmountsBean)cvBudgetPersonnelCalamts.get(calInd);
                copyBean = new BeanUtilsBean();
                copyBean.copyProperties(dynaPersonnelCalAmtsBean,budgetPersonnelCalAmountsBean);
                vecBudgetPersonnelCalamts.addElement(dynaPersonnelCalAmtsBean);
            }
        }
        /* prepare budget personnel data */
        if(cvBudgetPersonnelData!= null){
            for(int calInd = 0; calInd < cvBudgetPersonnelData.size() ; calInd++){
                DynaBean dynaPersonnelDataBean = null;
                dynaPersonnelDataBean = getDynaForm("budgetPersonnelData", request);
                BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean)cvBudgetPersonnelData.get(calInd);
                copyBean = new BeanUtilsBean();
                copyBean.copyProperties(dynaPersonnelDataBean,budgetPersonnelDetailsBean);
                
                /* Use lineItemNumber as key to get cost element code
                 * and description.
                 */
                String lineItemNumber = "L" + budgetPersonnelDetailsBean.getLineItemNumber() + "P" + budgetPersonnelDetailsBean.getBudgetPeriod();
                System.out.println("Personnel Details Data  : "+lineItemNumber);
                BudgetDetailBean budgetDetailBean = (BudgetDetailBean) hmCostElementData.get(lineItemNumber);
                
                //System.out.println("personnel budget - key to get cost element ===> " + lineItemNumber + "***" + budgetDetailBean.getCostElement());
                dynaPersonnelDataBean.set("costElement", (String)budgetDetailBean.getCostElement());
                dynaPersonnelDataBean.set("costElementDescription", (String)budgetDetailBean.getCostElementDescription());
                dynaPersonnelDataBean.set("personMonths",new Double(getPersonMonths(dynaPersonnelDataBean, request)));
                
                /* get fringe benefit for the person */
                double fringeBenefit = getPersonFringeBenefit(budgetPersonnelDetailsBean.getBudgetPeriod(), budgetPersonnelDetailsBean.getLineItemNumber(),
                budgetPersonnelDetailsBean.getPersonNumber(), cvBudgetPersonnelCalamts);
                dynaPersonnelDataBean.set("fringeBenefit",new Double(fringeBenefit));
                /* calculate funds requested */
                double fundsRequested = budgetPersonnelDetailsBean.getSalaryRequested() + fringeBenefit;
                dynaPersonnelDataBean.set("fundsRequested",new Double(fundsRequested));
                
                HashMap initialData = getInitialValues(budgetPersonnelDetailsBean.getBudgetPeriod(),
                budgetPersonnelDetailsBean.getLineItemNumber(), budgetPersonnelDetailsBean.getPersonNumber(), request);
                String oldProjectRole = (String)initialData.get("oldProjectRole");
                String oldCostElement =  (String)initialData.get("oldCostElement");
                String oldPeriodType =  (String)initialData.get("oldPeriodType");
                Double oldPercentCharged =  (Double)initialData.get("oldPercentCharged");
                Double oldPercentEffort =  (Double)initialData.get("oldPercentEffort");
                
                /* set intial values in dyna form */
                dynaPersonnelDataBean.set("awProjectRole",oldProjectRole);
                dynaPersonnelDataBean.set("awCostElement",oldCostElement);
                dynaPersonnelDataBean.set("awPeriodType",oldPeriodType);
                dynaPersonnelDataBean.set("awPercentCharged",oldPercentCharged);
                dynaPersonnelDataBean.set("awPercentEffort",oldPercentEffort);
                
                vecBudgetPersonnelData.addElement(dynaPersonnelDataBean);
            }
        }
        hmBudgetDetail.put(BudgetDetailBean.class,vecBudgetDetails);
        hmBudgetDetail.put(BudgetPeriodBean.class,vecBudgetPeriod);
        hmBudgetDetail.put(BudgetDetailCalAmountsBean.class,vecBudgetDetailCalAmts);
        hmBudgetDetail.put(BudgetPersonnelDetailsBean.class,vecBudgetPersonnelData);
        hmBudgetDetail.put(BudgetPersonnelCalAmountsBean.class,vecBudgetPersonnelCalamts);
        
        //System.out.println("*** EXIT prepareBudgetPersonnelData ***");
        return hmBudgetDetail;
    }
    
    /* This method is to get initial values - for user editable fields
     * These values are used for comparison - to identify whether data changed
     * @param - budgetPeriod
     * @param - lineItemNumber
     * @param - personNumber
     * @return - HashMap - (projectRole, costElement, periodType, percentCharged, percentEffort
     */
    private HashMap getInitialValues(int budgetPeriod, int lineItemNumber,
    int personNumber, HttpServletRequest request) throws Exception{
        //System.out.println("*** ENTER getInitialValues ***");
        HttpSession session = request.getSession();
        Vector vecBudgetPersonnel = (Vector)session.getAttribute("budgetPersonnelData");
        HashMap initialData = new HashMap();
        if(vecBudgetPersonnel!= null){
            for(int index = 0; index <vecBudgetPersonnel.size(); index++){
                DynaValidatorForm dynaForm = (DynaValidatorForm)vecBudgetPersonnel.get(index);
                int dynaBudgetPeriod = ((Integer)dynaForm.get("budgetPeriod")).intValue();
                int dynaLineItemNumber = ((Integer)dynaForm.get("lineItemNumber")).intValue();
                int dynaPersonNumber = ((Integer)dynaForm.get("personNumber")).intValue();
                if(budgetPeriod == dynaBudgetPeriod && lineItemNumber == dynaLineItemNumber &&
                personNumber == dynaPersonNumber) {
                    initialData.put("oldProjectRole", dynaForm.get("awProjectRole").toString());
                    initialData.put("oldCostElement", dynaForm.get("awCostElement").toString());
                    initialData.put("oldPeriodType", dynaForm.get("awPeriodType").toString());
                    initialData.put("oldPercentCharged", (Double)dynaForm.get("awPercentCharged"));
                    initialData.put("oldPercentEffort", (Double)dynaForm.get("awPercentEffort"));
                    break;
                }
            }
        }
        //System.out.println("*** EXIT getInitialValues ***");
        return initialData;
        
    }
    
    /* This method is to get person months
     * @param - dynaPersonnelData
     * @return - int - person months
     */
    private double getPersonMonths(DynaBean dynaPersonnelData, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmData = new HashMap();
        DateUtils dateUtils = new DateUtils();
        double personMonths = 0;
        double percentEffort = ((Double)dynaPersonnelData.get("percentEffort")).doubleValue();
        //String periodType = (String)dynaPersonnelData.get("periodType");
        //Commented for Case 2676 included StartDate , End Date differences and percentEffort
        
        //        if(periodType.equalsIgnoreCase("CY")){ // Calendar Year
        //            personMonths = percentEffort/100 * 12;
        //        }else if(periodType.equalsIgnoreCase("SP")){ // Summer Period
        //            personMonths = percentEffort/100 * 3;
        //        }else if(periodType.equalsIgnoreCase("AP")){ // Academic Period
        //            personMonths = percentEffort/100 * 9;
        //        }
        
        String personStartDate = (String) dynaPersonnelData.get("personStartDate");
        String personEndDate = (String) dynaPersonnelData.get("personEndDate");
        if(personStartDate !=null && !personStartDate.equals(EMPTY_STRING) && personEndDate !=null && !personEndDate.equals(EMPTY_STRING) ){
            
            Date startDate = dateUtils.getSQLDate(personStartDate);
            Date endDate = dateUtils.getSQLDate(personEndDate);
            hmData.put("startDate", startDate);
            hmData.put("endDate", endDate);
            double totalMonths = 0;

            // Case# 3803: Lite Personnel Budget months not calculating correctly -Start
            // Use FN_MONTHS_BETWEEN to calculate PersonMonths
//            HashMap hmViewMonths = (HashMap)htViewMonths.get("getNumberOfMonths");
//            if(hmViewMonths !=null && hmViewMonths.size() >0){
//                totalMonths = Integer.parseInt(hmViewMonths.get("months").toString());
//            }
            
            BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
            totalMonths = budgetDataTxnBean.getMonthsBetween(startDate, endDate);
            // Case# 3803: Lite Personnel Budget months not calculating correctly - End
            personMonths = totalMonths/100 * percentEffort;
            
        }
        
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setMinimumIntegerDigits(1);
        String formattedPersonMonths = decimalFormat.format(personMonths);
        return Double.parseDouble(formattedPersonMonths);
    }
    
    /* This method is to get fringe benefit for a given line item
     * @param - budgetPeriod
     * @param - lineItemNumber
     * @param - personNumber
     * @param - budgetPersonnelCalAmts
     * @return - double - fringeBenefit
     */
    private double getPersonFringeBenefit(int budgetPeriod, int lineItemNumber,
    int personNumber, CoeusVector cvBudgetPersonnelCalamts) throws Exception{
        //System.out.println("*** ENTER getPersonFringeBenefit ***");
        double fringeBenefit = 0;
        for(int calInd = 0; calInd < cvBudgetPersonnelCalamts.size() ; calInd++){
            BudgetPersonnelCalAmountsBean budgetPersonnelCalAmountsBean = (BudgetPersonnelCalAmountsBean)cvBudgetPersonnelCalamts.get(calInd);
            // Added for Case 4032 - Budget Display LA Problems in 4.3.1 - Start
            // To Calculate fringe benefit sum up all the Rate Class code is Employee Benefit (5) , Rate Type Code is not in EB on LA (3) and 
            // Rate Class code is Vacation (8) , Rate Type Code is not in Vacation on LA (2)
            if((budgetPersonnelCalAmountsBean.getBudgetPeriod() == budgetPeriod) && (budgetPersonnelCalAmountsBean.getLineItemNumber() == lineItemNumber) &&
            (budgetPersonnelCalAmountsBean.getPersonNumber() == personNumber) && ((budgetPersonnelCalAmountsBean.getRateClassCode() == 5 && budgetPersonnelCalAmountsBean.getRateTypeCode() != 3) 
                || (budgetPersonnelCalAmountsBean.getRateClassCode() == 8 && budgetPersonnelCalAmountsBean.getRateTypeCode() != 2)) ){
                fringeBenefit = fringeBenefit + budgetPersonnelCalAmountsBean.getCalculatedCost();
            }
            // Added for Case 4032 - Budget Display LA Problems in 4.3.1 - End
        }
        //System.out.println("*** EXIT getPersonFringeBenefit ***");
        return fringeBenefit;
    }
    
    /**
     * Budget Details for selected period
     * @param budgetPeriod
     * @throws Exception
     * @return
     */
    private HashMap getBudgetDetailsForPeriod(int budgetPeriod, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        //System.out.println("*** ENTER getBudgetDetailsForPeriod ***");
        BeanUtilsBean copyBean = null;
        HashMap hmData = new HashMap();
        CoeusVector cvBudgetDetails = new CoeusVector();
        Vector vecBudgetDetails = (Vector)session.getAttribute("BudgetDetailData");
        Vector vecFilterBudgetDetails = filterBudgetPeriod(budgetPeriod, vecBudgetDetails);
        Vector vecCEData = (Vector)session.getAttribute("costElementData");
        Vector vecFilterToShow = getExistingCE(vecFilterBudgetDetails, vecCEData);
        
     // Case id 2924 - start
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
     // Case id 2924 - end
        
        /* Get the form data and put into the new DynaValidatorForm for the data comparision
         * and for the copying the data
         */
        hmData.put("dataChanged", new Boolean(false));
        
        if(vecFilterToShow!= null && vecFilterToShow.size() > 0){
            String reqLineItem = null;
            String dynaLineItem = null;
            for(int index = 0; index < vecFilterToShow.size(); index++){
                boolean modifyData = false;
                DynaValidatorForm dynaBudgetDetails = (DynaValidatorForm)vecFilterToShow.get(index);
                int beanLineItemNumber = ((Integer)dynaBudgetDetails.get("lineItemNumber")).intValue();
                reqLineItem = request.getParameter(GET_FIELDNAME+index+COST_ELEMENT_AS_REQUEST);
                if(reqLineItem != null && !reqLineItem.equals(EMPTY_STRING)){
                    dynaLineItem = (String)dynaBudgetDetails.get("costElement");
                    if(!dynaLineItem.equals(reqLineItem)){
                        dynaBudgetDetails.set("costElement",reqLineItem);
                     // Case id 2924 - start
//                        boolean isCampusFlag = budgetInfoBean.isOnOffCampusFlag();                        
                       //  boolean isCampusFlag = getCampusFlag(request, reqLineItem);
                        boolean isCampusFlag = false;
                        if(budgetInfoBean.isDefaultIndicator()){
                            isCampusFlag = getCampusFlag(request, reqLineItem);
                        }else{
                            isCampusFlag = budgetInfoBean.isOnOffCampusFlag();
                        }
                     // Case id 2924 - end
                        dynaBudgetDetails.set("onOffCampusFlag",new Boolean(isCampusFlag));
                        dynaBudgetDetails.set("changeCostElement",new Boolean(true));
                        modifyData = true;
                    }
                }
                String acType = (String)dynaBudgetDetails.get("acType");
                if(modifyData || (acType != null && acType.equals(TypeConstants.UPDATE_RECORD))){
                    hmData.put("dataChanged", new Boolean(true));
                    if(acType != null && !acType.equals(TypeConstants.INSERT_RECORD) && !acType.equals(TypeConstants.DELETE_RECORD)){
                        dynaBudgetDetails.set("acType", TypeConstants.UPDATE_RECORD);
                    }
                }
                for(int ind = 0 ; ind < vecFilterBudgetDetails.size(); ind ++){
                    DynaValidatorForm dynaValid = (DynaValidatorForm)vecFilterBudgetDetails.get(ind);
                    int dynaLineItemNumber = ((Integer)dynaValid.get("lineItemNumber")).intValue();
                    if(beanLineItemNumber == dynaLineItemNumber){
                        dynaValid.set("lineItemCost", dynaBudgetDetails.get("lineItemCost"));
                        dynaValid.set("directCost",dynaBudgetDetails.get("directCost"));
                        dynaValid.set("costElement" ,dynaBudgetDetails.get("costElement"));
                        dynaValid.set("onOffCampusFlag",dynaBudgetDetails.get("onOffCampusFlag"));
                        dynaValid.set("lineItemDescription",dynaBudgetDetails.get("lineItemDescription"));
                        dynaValid.set("acType",dynaBudgetDetails.get("acType"));
                        break;
                    }
                }
            }
        }
        if(vecFilterBudgetDetails != null && vecFilterBudgetDetails.size() > 0){
            for(int index = 0; index < vecFilterBudgetDetails.size(); index++){
                DynaValidatorForm dynaBudgetDetails = (DynaValidatorForm)vecFilterBudgetDetails.get(index);
                BudgetPersonnelDetailsBean budgetDetailBean = new BudgetPersonnelDetailsBean();
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
        //System.out.println("*** EXIT getBudgetDetailsForPeriod ***");
        return hmData;
        
    }
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
    //    /**
    //     * Get the DetailCalAmts data prepare the ACTypes for the data updation
    //     * @param vecBudgetCalAmts
    //     * @throws Exception
    //     * @return
    //     */
    //    private Vector getDeletedDetailData(Vector vecBudgetCalAmts, HttpServletRequest request) throws Exception{
    //        //System.out.println("*** ENTER getDeletedDetailData ***");
    //        HttpSession session = request.getSession();
    //        Vector vecDeletedData = (Vector)session.getAttribute("DeletedBudgetDetailsData");
    //        if(vecBudgetCalAmts != null){
    //            if(vecDeletedData != null && vecDeletedData.size() > 0){
    //                for(int index = 0; index < vecDeletedData.size(); index++ ){
    //                    DynaValidatorForm dynaForm = (DynaValidatorForm)vecDeletedData.get(index);
    //                    int budgetPeriod = ((Integer)dynaForm.get("budgetPeriod")).intValue();
    //                    int lineItemnumber = ((Integer)dynaForm.get("lineItemNumber")).intValue();
    //                    for(int ind = 0; ind < vecBudgetCalAmts.size(); ind++){
    //                        DynaValidatorForm dynaValidForm = (DynaValidatorForm)vecBudgetCalAmts.get(ind);
    //                        int dynaBudgetPeriod = ((Integer)dynaValidForm.get("budgetPeriod")).intValue();
    //                        int dynaLineItemnumber = ((Integer)dynaValidForm.get("lineItemNumber")).intValue();
    //                        if(budgetPeriod == dynaBudgetPeriod && lineItemnumber == dynaLineItemnumber){
    //                            String acType = (String)dynaValidForm.get("acType");
    //                            if(acType != null ){
    //                                dynaValidForm.set("acType",TypeConstants.DELETE_RECORD);
    //                            }
    //                        }
    //                    }
    //                }
    //            }
    //        }
    //        //System.out.println("*** EXIT getDeletedDetailData ***");
    //        return vecBudgetCalAmts;
    //    }
    /**
     * Prepares only BudgetDetailCalAmtsBean for the DyanForm
     * @param vecBudgetDetailCalAmts
     * @throws Exception
     * @return
     */
    //    private CoeusVector prepareBudgetDetailCalAmts(Vector vecBudgetDetailCalAmts) throws Exception{
    //        //System.out.println("*** ENTER prepareBudgetDetailCalAmts ***");
    //        BeanUtilsBean copyBean = null;
    //        BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = null;
    //        CoeusVector cvCalamts = null;
    //        if(vecBudgetDetailCalAmts!= null){
    //            cvCalamts = new CoeusVector();
    //            for(int index = 0; index <vecBudgetDetailCalAmts.size(); index++){
    //                budgetDetailCalAmountsBean  = new BudgetDetailCalAmountsBean ();
    //                DynaValidatorForm dynaForm = (DynaValidatorForm)vecBudgetDetailCalAmts.get(index);
    //                copyBean = new BeanUtilsBean();
    //                copyBean.copyProperties(budgetDetailCalAmountsBean ,dynaForm);
    //                cvCalamts.addElement(budgetDetailCalAmountsBean);
    //            }
    //        }
    //        //System.out.println("*** EXIT prepareBudgetDetailCalAmts ***");
    //        return cvCalamts;
    //    }
    
    /**
     * Prepare Custom bean for DynaBean bean. This will extract the dynabean
     * and prepares new CustomBean with the values and return hashMap containing the
     * Vector of form data
     * @param vecBudgetDetails
     * @param vecBudgetDetailCalAmts
     * @param vecBudgetPersonnel
     * @param vecBudgetPersonnelCalAmts
     * @param vecBudgetPeriod
     * @param dynaForm
     * @throws Exception
     * @return
     */
    private HashMap prepareBudgetCustomData(Vector vecBudgetDetails, Vector vecBudgetCalAmts,
    Vector vecBudgetPersonnel, Vector vecBudgetPersonnelCalAmts, Vector vecBudgetPeriod) throws Exception{
        //System.out.println("*** ENTER prepareBudgetCustomData ***");
        
        BeanUtilsBean copyBean = null;
        HashMap hmBudgetDetail = new HashMap();
        BudgetDetailBean budgetDetailBean = null;
        BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = null;
        BudgetPersonnelCalAmountsBean budgetPersonnelCalAmountsBean = null;
        BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = null;
        BudgetPeriodBean budgetPeriodBean = null;
        
        CoeusVector cvBudgetDetails = null;
        CoeusVector cvBudgetCalamts = null;
        CoeusVector cvBudgetPersonnelData = null;
        CoeusVector cvBudgetPersonnelCalamts = null;
        CoeusVector cvBudgetPeriods = null;
        
        /* prepare budget period */
        if(vecBudgetPeriod != null){
            cvBudgetPeriods = new CoeusVector();
            for(int ind = 0; ind < vecBudgetPeriod.size() ; ind++){
                DynaValidatorForm dynaForm = (DynaValidatorForm)vecBudgetPeriod.get(ind);
                int dynaPeriodNumber = ((Integer)dynaForm.get("budgetPeriod")).intValue();
                budgetPeriodBean = new BudgetPeriodBean();
                copyBean = new BeanUtilsBean();
                copyBean.copyProperties(budgetPeriodBean,dynaForm);
                budgetPeriodBean.setAw_BudgetPeriod(dynaPeriodNumber);
                cvBudgetPeriods.addElement(budgetPeriodBean);
            }
        }
        
        /* prepare budget details */
        if(vecBudgetDetails!= null){
            cvBudgetDetails = new CoeusVector();
            for(int index = 0; index <vecBudgetDetails.size(); index++){
                budgetDetailBean  = new BudgetDetailBean();
                DynaValidatorForm dynaForm = (DynaValidatorForm)vecBudgetDetails.get(index);
                copyBean = new BeanUtilsBean();
                copyBean.copyProperties(budgetDetailBean ,dynaForm);
                cvBudgetDetails.addElement(budgetDetailBean);
            }
        }
        
        /* prepare budget Calamts */
        if(vecBudgetCalAmts!= null){
            cvBudgetCalamts = new CoeusVector();
            for(int index = 0; index <vecBudgetCalAmts.size(); index++){
                budgetDetailCalAmountsBean  = new BudgetDetailCalAmountsBean();
                DynaValidatorForm dynaForm = (DynaValidatorForm)vecBudgetCalAmts.get(index);
                copyBean = new BeanUtilsBean();
                copyBean.copyProperties(budgetDetailCalAmountsBean ,dynaForm);
                cvBudgetCalamts.addElement(budgetDetailCalAmountsBean);
            }
        }
        
        /* prepare personnel budgetCalamts */
        if(vecBudgetPersonnelCalAmts!= null){
            cvBudgetPersonnelCalamts = new CoeusVector();
            for(int index = 0; index <vecBudgetPersonnelCalAmts.size(); index++){
                budgetPersonnelCalAmountsBean  = new BudgetPersonnelCalAmountsBean();
                DynaValidatorForm dynaForm = (DynaValidatorForm)vecBudgetPersonnelCalAmts.get(index);
                copyBean = new BeanUtilsBean();
                copyBean.copyProperties(budgetPersonnelCalAmountsBean ,dynaForm);
                cvBudgetPersonnelCalamts.addElement(budgetPersonnelCalAmountsBean);
            }
        }
        
        /* prepare personnel budget data */
        if(vecBudgetPersonnel!= null){
            cvBudgetPersonnelData = new CoeusVector();
            for(int index = 0; index <vecBudgetPersonnel.size(); index++){
                budgetPersonnelDetailsBean  = new BudgetPersonnelDetailsBean();
                DynaValidatorForm dynaForm = (DynaValidatorForm)vecBudgetPersonnel.get(index);
                copyBean = new BeanUtilsBean();
                copyBean.copyProperties(budgetPersonnelDetailsBean ,dynaForm);
                cvBudgetPersonnelData.addElement(budgetPersonnelDetailsBean);
            }
        }
        
        
        
        hmBudgetDetail.put(BudgetDetailBean.class,cvBudgetDetails);
        hmBudgetDetail.put(BudgetDetailCalAmountsBean.class,cvBudgetCalamts);
        hmBudgetDetail.put(BudgetPersonnelDetailsBean.class,cvBudgetPersonnelData);
        hmBudgetDetail.put(BudgetPersonnelCalAmountsBean.class,cvBudgetPersonnelCalamts);
        hmBudgetDetail.put(BudgetPeriodBean.class,cvBudgetPeriods);
        
        //System.out.println("*** EXIT prepareBudgetCustomData ***");
        return hmBudgetDetail;
    }
    
    //     /**
    //      * Prepare the BudgetDetails for the particular period and line item number
    //      * @param vecBudgetDetails
    //      * @param dynaForm
    //      * @throws Exception
    //      * @return
    //      */
    //    private Vector prepareBudgetEntireDetailsData(Vector vecBudgetDetails, HttpServletRequest request)
    //        throws Exception{
    //        HttpSession session = request.getSession();
    //        //System.out.println("*** ENTER prepareBudgetEntireDetailsData ***");
    //        Vector vecBudgetEntireDetails = (Vector)session.getAttribute("BudgetDetailData");
    //        if(vecBudgetDetails != null && vecBudgetDetails.size() > 0){
    //            for(int index = 0; index < vecBudgetDetails.size() ; index++){
    //                DynaValidatorForm dynaBudgetDetails = (DynaValidatorForm)vecBudgetDetails.get(index);
    //                int dynaBudgetPeriod = ((Integer)dynaBudgetDetails.get("budgetPeriod")).intValue();
    //                int dynaLineItemSequence = ((Integer)dynaBudgetDetails.get("lineItemNumber")).intValue();
    //                String costElement = (String)dynaBudgetDetails.get("costElement");
    //                Double lineItemCost = (Double)dynaBudgetDetails.get("lineItemCost");
    //                String lineItemDescription = (String)dynaBudgetDetails.get("lineItemDescription");
    //                String acType = (String)dynaBudgetDetails.get("acType");
    //
    //                for(int ind = 0; ind < vecBudgetEntireDetails.size(); ind ++){
    //                    DynaValidatorForm dynaDetails = (DynaValidatorForm)vecBudgetEntireDetails.get(ind);
    //                    int dynaPeriod = ((Integer)dynaDetails.get("budgetPeriod")).intValue();
    //                    int dynaLineSequence = ((Integer)dynaDetails.get("lineItemNumber")).intValue();
    //                    if(dynaBudgetPeriod == dynaPeriod && dynaLineItemSequence == dynaLineSequence){
    //                        dynaDetails.set("lineItemCost", lineItemCost);
    //                        dynaDetails.set("directCost", lineItemCost);
    //                        dynaDetails.set("costElement",costElement);
    //                        dynaDetails.set("onOffCampusFlag",dynaBudgetDetails.get("onOffCampusFlag"));
    //                        dynaDetails.set("lineItemDescription",lineItemDescription);
    //                        dynaDetails.set("acType",acType);
    //                        break;
    //                    }
    //                }
    //            }
    //        }
    //        //System.out.println("*** EXIT prepareBudgetEntireDetailsData ***");
    //        return vecBudgetEntireDetails;
    //    }
    /**
     *
     * @param budgetPeriod
     * @param vecBudgetPeriod
     * @param vecBudgetEntireDetails
     * @throws Exception
     */
    private void setDataForExceedTotalCost(int budgetPeriod,Vector vecBudgetPeriod,Vector
    vecBudgetEntireDetails , HttpServletRequest request, CoeusDynaBeansList coeusLineItemDynaList) throws Exception{
        HttpSession session = request.getSession();
        //System.out.println("*** ENTER setDataForExceedTotalCost ***");
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
            //CoeusDynaBeansList coeusLineItemDynaList = (CoeusDynaBeansList)actionForm;
            coeusLineItemDynaList.setList(vecBudgetDetails);
            List beanList = new ArrayList();
            beanList.add(new Integer(budgetPeriod));
            coeusLineItemDynaList.setBeanList(beanList);
            session.setAttribute("budgetPersonnelDynaBean",coeusLineItemDynaList);
            //validation framework end
            //Added for removing instance variable -case # 2960 - start
            BudgetInfoBean bgtInf = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
            String queryKey = bgtInf.getProposalNumber()+bgtInf.getVersionNumber();
            //Added for removing instance variable - case # 2960- end  	                 
            removeQueryEngineCollection(queryKey);
        }
        
        //System.out.println("*** EXIT setDataForExceedTotalCost ***");
    }
    
    /** bug fixed for On off campus flag and for calculation. If flag true set N else F.
     * Note:in webtxnbean if flag is true
     * it set to Y and for false it sets N. This method will be used later after implementing Txn Bean
     * for multiple procedures start 2.
     * Saves the Budget Period Line Item details for the specific period
     * @param hmBudgetData
     * @param budgetPeriod
     * @param reqToOpenBudgetPeriod
     * @param editing
     * @throws Exception
     */
    private String saveBudgetPeriodLineItems(HashMap hmBudgetData,int budgetPeriod,
    int reqToOpenBudgetPeriod ,HttpServletRequest request, CoeusDynaBeansList dynaForm) throws Exception{
        
        WebTxnBean webTxnBean = null;
        String navigator = "success";
        HttpSession session = request.getSession();
        String proposalNumber = (String) session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
        CoeusVector cvBudgetInfo = (CoeusVector)hmBudgetData.get(BudgetInfoBean.class);
        Vector vecBudgetPeriod = (Vector)hmBudgetData.get(BudgetPeriodBean.class);
        Vector vecBudgetCalAmts = (Vector)hmBudgetData.get(BudgetDetailCalAmountsBean.class);
        Vector vecBudgetPersonnel = (Vector)hmBudgetData.get(BudgetPersonnelDetailsBean.class);
        Vector vecBudgetPersonnelCalAmts = (Vector)hmBudgetData.get(BudgetPersonnelCalAmountsBean.class);
        CoeusVector cvBudgetRateBase = (CoeusVector)hmBudgetData.get(BudgetRateBaseBean.class);
        edu.mit.coeus.bean.UserInfoBean userInfoBean = (edu.mit.coeus.bean.UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        edu.mit.coeus.budget.bean.BudgetUpdateTxnBean budgetUpdateTxnBean
        = new edu.mit.coeus.budget.bean.BudgetUpdateTxnBean(userId);
        budgetUpdateTxnBean.addUpdateBudgetInfo(cvBudgetInfo);
        Timestamp dbTimestamp = prepareTimeStamp();
        HashMap hmLineItemSequenceHolder = new HashMap();
        /* set budget period */
         //Take the value from the List and check the flag to remove the Instance level variable for Case 2960 -Start
        boolean calculateAllPeriods = false;
        List budgetPersonnelList = dynaForm.getList();
        if(budgetPersonnelList !=null && budgetPersonnelList.size() > 0){
            DynaValidatorForm calAllPeriodsForm = (DynaValidatorForm)budgetPersonnelList.get(0);
            Boolean calAllPeriods = (Boolean) calAllPeriodsForm.get("calculateAllPeriodsFlag");
            
            if(calAllPeriods !=null ){
                calculateAllPeriods = calAllPeriods.booleanValue();
            }
        }
          //Case : 2960 -End
                 
        if(vecBudgetPeriod != null && vecBudgetPeriod.size() > 0){
            if(!calculateAllPeriods){
                vecBudgetPeriod = filterBudgetPeriod(budgetPeriod, vecBudgetPeriod);
            }
            for(int index = 0; index < vecBudgetPeriod.size(); index ++){
                webTxnBean = new WebTxnBean();
                DynaValidatorForm dynaValidForm  = (DynaValidatorForm)vecBudgetPeriod.get(index);
                String acType = (String)dynaValidForm.get("acType");
                if(acType != null && (acType.equals(TypeConstants.UPDATE_RECORD) || acType.equals(TypeConstants.INSERT_RECORD))){
                    dynaValidForm.set("avUpdateTimestamp",dbTimestamp.toString());
                }
                hmLineItemSequenceHolder.put(dynaValidForm.get("budgetPeriod"), new Integer(1));
                webTxnBean.getResults(request,
                "updBudgetPeriod", dynaValidForm);
                // Update the proposal hierarchy sync flag
                updateProposalSyncFlags(request, proposalNumber);
            }
        }
        
        /* check deleted records - budget details */
        Vector vecDeletedBudgetDetails = (Vector)session.getAttribute("DeletedBudgetDetailsData");
        /* check deleted records - budget cal amts */
        Vector vecDeletedBudgetCalAmts = (Vector)session.getAttribute("DeletedBudgetDetailCalAmts");
        /* check deleted records - budget personnel details */
        Vector vecDeletedBudgetPersonnel = (Vector)session.getAttribute("DeletedBudgetPersonnelData");
        /* check deleted records - budget personnel details */
        Vector vecDeletedBudgetPersonnelCalAmts = (Vector)session.getAttribute("DeletedBudgetPersonnelCalAmts");
        
        Vector vecTempBudgetDetailData = new Vector();
        Vector vecTempBudgetCalAmts = new Vector();
        Vector vecTempBudgetPersonnel = new Vector();
        Vector vecTempBudgetPersonnelCalAmts = new Vector();
        
        Vector vecBudgetEntireDetails = (Vector)hmBudgetData.get(BudgetDetailBean.class);
        Vector vecFilterDetails = null;
        
        /* apply to later periods does a calculate all periods and need to save data */
        if(calculateAllPeriods){
            vecFilterDetails = vecBudgetEntireDetails;
        }else {
            vecFilterDetails = filterBudgetPeriod(budgetPeriod, vecBudgetEntireDetails);
        }
        
        vecFilterDetails = filterBasedOnLineItemNumber(vecFilterDetails);
        
        /* Add all budget details  data marked for deletion */
        if(vecDeletedBudgetDetails != null){
            vecTempBudgetDetailData.addAll(vecDeletedBudgetDetails);
        }
        vecTempBudgetDetailData.addAll(vecFilterDetails);
        if(vecTempBudgetDetailData != null && vecTempBudgetDetailData.size() > 0){
            webTxnBean = new WebTxnBean();
            for(int ind = 0; ind < vecTempBudgetDetailData.size(); ind++){
                DynaValidatorForm dynaValidForm = (DynaValidatorForm)vecTempBudgetDetailData.get(ind);
                String acType = (String)dynaValidForm.get("acType");
                if(acType.equalsIgnoreCase(TypeConstants.INSERT_RECORD) || acType.equalsIgnoreCase(TypeConstants.UPDATE_RECORD) ||
                acType.equalsIgnoreCase(TypeConstants.DELETE_RECORD) ){
                    if((acType.equalsIgnoreCase(TypeConstants.INSERT_RECORD) || acType.equalsIgnoreCase(TypeConstants.UPDATE_RECORD))){
                        int newLineItemSequence = ((Integer)hmLineItemSequenceHolder.get(dynaValidForm.get("budgetPeriod"))).intValue();
                        dynaValidForm.set("lineItemSequence",new Integer(newLineItemSequence));
                        newLineItemSequence++;
                        hmLineItemSequenceHolder.put(dynaValidForm.get("budgetPeriod"), new Integer(newLineItemSequence));
                    }
                    dynaValidForm.set("avUpdateTimestamp",dbTimestamp.toString());
                    
                    //COEUSQA-1693 - Cost Sharing Submission - start
                    String submitCSFlag = (String)dynaValidForm.get("submitCostSharingFlag");
                    if("true".equals(submitCSFlag)){
                        dynaValidForm.set("submitCostSharingFlag","Y");
                    }else{
                        dynaValidForm.set("submitCostSharingFlag","N");
                    }
                    //COEUSQA-1693 - Cost Sharing Submission - end
                    //System.out.println("budgetPeriod ===> " + dynaValidForm.get("budgetPeriod"));
                    //System.out.println("costElement ===> " + dynaValidForm.get("costElement"));
                    
                    webTxnBean.getResults(request,"updBudgetLineItemDetails", dynaValidForm);
                    // Update the proposal hierarchy sync flag
                    updateProposalSyncFlags(request, proposalNumber);
                }
                
            }
        }
        /* Add all budget details cal amts data  marked for deletion */
        if(vecDeletedBudgetCalAmts != null){
            vecTempBudgetCalAmts.addAll(vecDeletedBudgetCalAmts);
        }
        vecTempBudgetCalAmts.addAll(vecBudgetCalAmts);
        if(vecTempBudgetCalAmts != null){
            for(int index = 0; index < vecTempBudgetCalAmts.size(); index ++){
                webTxnBean = new WebTxnBean();
                DynaValidatorForm dynaValidForm  = (DynaValidatorForm)vecTempBudgetCalAmts.get(index);
                String acType = (String)dynaValidForm.get("acType");
                if(acType.equalsIgnoreCase(TypeConstants.INSERT_RECORD) || acType.equalsIgnoreCase(TypeConstants.UPDATE_RECORD) ||
                acType.equalsIgnoreCase(TypeConstants.DELETE_RECORD) ){
                    dynaValidForm.set("avUpdateTimestamp",dbTimestamp.toString());
                    webTxnBean.getResults(request,"updBudgetDetailCalAmts", dynaValidForm);
                    // Update the proposal hierarchy sync flag
                    updateProposalSyncFlags(request, proposalNumber);
                }
            }
        }
        /* Add all budget personnel details data  marked for deletion */
        if(vecDeletedBudgetPersonnel != null) {
            vecTempBudgetPersonnel.addAll(vecDeletedBudgetPersonnel);
        }
        
        Vector vecFilterPersonnelDetails = null;
        /* apply to later periods does a calculate all periods and need to save data */
        if(calculateAllPeriods){
            vecFilterPersonnelDetails  = vecBudgetPersonnel;
        }else {
            vecFilterPersonnelDetails = filterBudgetPeriod(budgetPeriod, vecBudgetPersonnel);
        }
        
        
        vecTempBudgetPersonnel.addAll(vecFilterPersonnelDetails);
        if(vecTempBudgetPersonnel != null){
            for(int index = 0; index < vecTempBudgetPersonnel.size(); index ++){
                webTxnBean = new WebTxnBean();
                DynaValidatorForm dynaValidForm  = (DynaValidatorForm)vecTempBudgetPersonnel.get(index);
                String acType = (String)dynaValidForm.get("acType");
                if(acType.equalsIgnoreCase(TypeConstants.INSERT_RECORD) || acType.equalsIgnoreCase(TypeConstants.UPDATE_RECORD) ||
                acType.equalsIgnoreCase(TypeConstants.DELETE_RECORD) ){
                    dynaValidForm.set("avUpdateTimestamp",dbTimestamp.toString());
                    webTxnBean.getResults(request,"updBudgetPersonnelDetails", dynaValidForm);
                    // Update the proposal hierarchy sync flag
                    updateProposalSyncFlags(request, proposalNumber);
                }
            }
        }
        /* Add all budget personnel details cal amts data  marked for deletion */
        if(vecDeletedBudgetPersonnelCalAmts != null){
            vecTempBudgetPersonnelCalAmts.addAll(vecDeletedBudgetPersonnelCalAmts);
        }
        vecTempBudgetPersonnelCalAmts.addAll(vecBudgetPersonnelCalAmts);
        if(vecTempBudgetPersonnelCalAmts != null){
            for(int index = 0; index < vecTempBudgetPersonnelCalAmts.size(); index ++){
                webTxnBean = new WebTxnBean();
                DynaValidatorForm dynaValidForm  = (DynaValidatorForm)vecTempBudgetPersonnelCalAmts.get(index);
                String acType = (String)dynaValidForm.get("acType");
                if(acType.equalsIgnoreCase(TypeConstants.INSERT_RECORD) || acType.equalsIgnoreCase(TypeConstants.UPDATE_RECORD) ||
                acType.equalsIgnoreCase(TypeConstants.DELETE_RECORD) ){
                    dynaValidForm.set("avUpdateTimestamp",dbTimestamp.toString());
                    webTxnBean.getResults(request,"updBudgetPersonnelCalAmts", dynaValidForm);
                    // Update the proposal hierarchy sync flag
                    updateProposalSyncFlags(request, proposalNumber);
                }
            }
        }
        
        /** Since we have to update the Rate and Base for the whole budget not for the selected period
         *Call CalculateAll Periods and get the Budget Rate and Base and then update to the database
         *Start
         */
        Hashtable htBudgetData = initializeBudgetCalculator((BudgetInfoBean)session.getAttribute("budgetInfoBean"));
        htBudgetData = calculateAllPeriods(htBudgetData,(BudgetInfoBean)session.getAttribute("budgetInfoBean"));
        cvBudgetRateBase = (CoeusVector)htBudgetData.get(BudgetRateBaseBean.class);
        
        /* update rate and base */
         /*Fix #2862
         *by Geo on 05/04/2007
         *If there is no budgetratebase after calculation, it will not delete existing rateandbase data
         *so, it becomes orphan and will come in report
         *Same code change done in BudgetLineItemAction as well
         */
        //Begin Fix
        BudgetInfoBean bgtInf = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        //Added for removing instance variable -case # 2960 - start
        String queryKey = bgtInf.getProposalNumber()+bgtInf.getVersionNumber();
        //Added for removing instance variable - case # 2960- end         
        budgetUpdateTxnBean.cleanUpRateBase(bgtInf.getProposalNumber(), bgtInf.getVersionNumber());
        //End Fix
        budgetUpdateTxnBean.updateRateAndBase(cvBudgetRateBase);
         //Added for Case 2228 - Print Budget Summary - Start
        CoeusVector cvBudgetPersonnelDetRateBase = (CoeusVector)htBudgetData.get(BudgetPersonnelDetailsRateBaseBean.class);                        
        budgetUpdateTxnBean.cleanUpPersonnelDetRateBase(bgtInf.getProposalNumber(), bgtInf.getVersionNumber());
        budgetUpdateTxnBean.updateBudgetPersonnelDetRateBase(cvBudgetPersonnelDetRateBase);
      // Added for case 2228 - Print Budget Summary - End      
        // Added for COEUSDEV-1212 :  Proposal Versions $ and Budget summary $ totals show a single year rather than multi-year total - Start
        cvBudgetInfo = (CoeusVector)htBudgetData.get(BudgetInfoBean.class);
        if(cvBudgetInfo != null && !cvBudgetInfo.isEmpty()){
            budgetUpdateTxnBean.addUpdateBudgetInfo(cvBudgetInfo);
        }
        // Added for COEUSDEV-1212 :  Proposal Versions $ and Budget summary $ totals show a single year rather than multi-year total - End
        removeQueryEngineCollection(queryKey);
        session.removeAttribute("DeletedBudgetDetailsData");
        session.removeAttribute("DeletedBudgetDetailCalAmts");
        session.removeAttribute("DeletedBudgetPersonnelData");
        session.removeAttribute("DeletedBudgetPersonnelCalAmts");
        
        //System.out.println("*** EXIT saveBudgetPeriodLineItems ***");
        return navigator;
        
    }
    
    /**
     * method to sort the line item number
     * @param vecFilterData
     * @throws Exception
     * @return Vector object of sorted line item number
     */
    private Vector filterBasedOnLineItemNumber(Vector vecFilterData) throws Exception{
        /* for sorting based on line item number so that every time we'll get sorted
         * vector based on line item number */
        //System.out.println("*** ENTER filterBasedOnLineItemNumber ***");
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
        //System.out.println("*** EXIT filterBasedOnLineItemNumber ***");
        return vecFilterData;
    }
    /**
     * Method to get budgetCategoryCode based on selectedCostElement
     * @param selectedCostElement
     * @throws Exception
     * @return budgetCategory code
     */
    private int getBudgetCategoryCode(String selectedCostElement ,HttpServletRequest request) throws Exception{
        //System.out.println("*** ENTER getBudgetCategoryCode ***");
        HttpSession session = request.getSession();
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
        //System.out.println("*** EXIT getBudgetCategoryCode ***");
        return budgetCategoryCode;
    }
    
    /**
     * Method to create new personnel dyna form
     * This method will create a new personnel budget data and corresponding budget detail data
     * required for calculation.
     * @param budgetPeriod
     * @param personName
     * @param personId
     * @param jobCode
     * @throws Exception
     * @return HashMap
     */
    private HashMap createNewPersonnelDynaForm(int budgetPeriod, DynaValidatorForm dynaValidatorForm ,
    HttpServletRequest request)throws Exception {
        //System.out.println("*** ENTER createNewPersonnelDynaForm ***");
        HttpSession session = request.getSession();
        ServletContext servletContext = session.getServletContext();
        ModuleConfig moduleConfig = RequestUtils.getModuleConfig(request, servletContext);
        FormBeanConfig formConfig = moduleConfig.findFormBeanConfig("budgetPersonnelData");
        DynaActionFormClass dynaClass = DynaActionFormClass.createDynaActionFormClass(formConfig);
        DynaActionForm dynaActionForm = (DynaActionForm)dynaClass.newInstance();
        DynaBean newPersonnelDynaForm = ((DynaBean)dynaActionForm).getDynaClass().newInstance();
        
        formConfig = moduleConfig.findFormBeanConfig("budgetDetailData");
        dynaClass = DynaActionFormClass.createDynaActionFormClass(formConfig);
        dynaActionForm = (DynaActionForm)dynaClass.newInstance();
        DynaBean newBudgetDynaForm = ((DynaBean)dynaActionForm).getDynaClass().newInstance();
        
        String personName = (dynaValidatorForm.get("fullName")==null)?"" : ((String)dynaValidatorForm.get("fullName")).trim();
        String personId = ((String)dynaValidatorForm.get("personId")).trim();
        String jobCode = ((String)dynaValidatorForm.get("jobCode")).trim();
        
        HashMap newDynaForms = new HashMap();
        
        /* get period start date and end date from budget period to set for new personnel
         * line item.
         */
        Vector vecBudgetPeriod = (Vector)session.getAttribute("BudgetPeriodData");
        DynaValidatorForm periodDynaForm = getFileteredBudgetPeriod(vecBudgetPeriod,budgetPeriod);
        
        /* get budget details data */
        //Vector vecBudgetDetailCalAmts = (Vector)session.getAttribute("BudgetDetailCalAmts");
        Vector vecBudgetDetails = (Vector)session.getAttribute("BudgetDetailsData");
        Vector vecFilterBudgetDetails =  filterBudgetPeriod(budgetPeriod,vecBudgetDetails);
        
        /* get max line item number */
        int nextLineItemNumber = getMaxLineItemNumberForBudget(vecFilterBudgetDetails) + 1;
        
        //Vector vecCostElementData = (Vector)session.getAttribute("costElementData");
        String costElementCode = EMPTY_STRING;
        String costElementDescription = EMPTY_STRING;
        
    // Case id 2924 - start
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        boolean isCampusFlag = budgetInfoBean.isOnOffCampusFlag();
    // Case id 2924 - end
        
        Timestamp dbTimestamp = prepareTimeStamp();
        /* create new budget details dyna form - cost element set in budget details */
        newBudgetDynaForm.set("proposalNumber",periodDynaForm.get("proposalNumber"));
        newBudgetDynaForm.set("versionNumber", periodDynaForm.get("versionNumber"));
        newBudgetDynaForm.set("budgetPeriod",periodDynaForm.get("budgetPeriod"));
        newBudgetDynaForm.set("lineItemNumber",new Integer(nextLineItemNumber));
        newBudgetDynaForm.set("basedOnLineItem",new Integer(0));
        newBudgetDynaForm.set("lineItemSequence",new Integer(nextLineItemNumber));
        newBudgetDynaForm.set("lineItemStartDate",periodDynaForm.get("startDate"));
        newBudgetDynaForm.set("lineItemEndDate",periodDynaForm.get("endDate"));
        newBudgetDynaForm.set("lineItemCost",new Double(0));
        newBudgetDynaForm.set("costSharingAmount",new Double(0));
        newBudgetDynaForm.set("underRecoveryAmount",new Double(0));
        newBudgetDynaForm.set("applyInRateFlag",new Boolean(true));
        newBudgetDynaForm.set("budgetJustification",null);
        newBudgetDynaForm.set("quantity",new Double(1.0));
        newBudgetDynaForm.set("directCost",new Double(0));
        newBudgetDynaForm.set("indirectCost",new Double(0));
        newBudgetDynaForm.set("totalCostSharing",new Double(0));
        newBudgetDynaForm.set("lineItemStartDate",periodDynaForm.get("startDate"));
        newBudgetDynaForm.set("lineItemEndDate",periodDynaForm.get("endDate"));
        newBudgetDynaForm.set("lineItemCost",new Double(0));
        newBudgetDynaForm.set("directCost",new Double(0));
        newBudgetDynaForm.set("indirectCost",new Double(0));
        newBudgetDynaForm.set("totalCostSharing",new Double(0));
        newBudgetDynaForm.set("costElement",costElementCode);
        newBudgetDynaForm.set("costElementDescription",costElementDescription);
        newBudgetDynaForm.set("budgetCategoryCode",new Integer(0));
        
     // Case id 2924 -start
        newBudgetDynaForm.set("onOffCampusFlag", new Boolean(isCampusFlag));
       // newBudgetDynaForm.set("onOffCampusFlag",new Boolean(true));
     // Case id 2924 - end
        //COEUSQA-1693 - Cost Sharing Submission - start
        newBudgetDynaForm.set("submitCostSharingFlag",dynaValidatorForm.get("submitCostSharingFlag"));
        //COEUSQA-1693 - Cost Sharing Submission - end
        //newBudgetDynaForm.set("applyInRateFlag",new Boolean(true));
        newBudgetDynaForm.set("updateTimestamp",dbTimestamp.toString());
        newBudgetDynaForm.set("avUpdateTimestamp",dbTimestamp.toString());
        newBudgetDynaForm.set("acType",TypeConstants.INSERT_RECORD);
        
        /* add the new dyna form */
        //vecBudgetDetails.add(newBudgetDynaForm);
        //session.setAttribute("BudgetDetailsData", vecBudgetDetails);
        newDynaForms.put(BudgetDetailBean.class, newBudgetDynaForm);
        
        
        /* create new budget personnel details dyna form */
        newPersonnelDynaForm.set("proposalNumber",periodDynaForm.get("proposalNumber"));
        newPersonnelDynaForm.set("versionNumber", periodDynaForm.get("versionNumber"));
        newPersonnelDynaForm.set("budgetPeriod", periodDynaForm.get("budgetPeriod"));
        newPersonnelDynaForm.set("lineItemNumber", new Integer(nextLineItemNumber));
        newPersonnelDynaForm.set("sequenceNumber", new Integer(-1));
        newPersonnelDynaForm.set("fullName",personName);
        newPersonnelDynaForm.set("personId",personId);
        newPersonnelDynaForm.set("personNumber",new Integer(1));
        newPersonnelDynaForm.set("jobCode",jobCode);
        newPersonnelDynaForm.set("projectRole",EMPTY_STRING);
        newPersonnelDynaForm.set("costElement",costElementCode);
        newPersonnelDynaForm.set("costElementDescription",costElementDescription);
        newPersonnelDynaForm.set("periodType",EMPTY_STRING);
        newPersonnelDynaForm.set("percentCharged",new Double(0));
        newPersonnelDynaForm.set("percentEffort",new Double(0));
        newPersonnelDynaForm.set("personMonths",new Double(0));
        newPersonnelDynaForm.set("salaryRequested",new Double(0));
        newPersonnelDynaForm.set("fringeBenefit",new Double(0));
        newPersonnelDynaForm.set("fundsRequested",new Double(0));
        newPersonnelDynaForm.set("costSharingAmount",new Double(0));
        
      // Case id 2924 -start
        newPersonnelDynaForm.set("onOffCampusFlag", new Boolean(isCampusFlag));
       // newPersonnelDynaForm.set("onOffCampusFlag",new Boolean(true));
     // Case id 2924 - end   
        
        newPersonnelDynaForm.set("applyInRateFlag",new Boolean(true));
        newPersonnelDynaForm.set("updateTimestamp",dbTimestamp.toString());
        //Added for Case COEUSDEV-209:Save and apply to later periods sets line item dates same as period dates - Start
//        newPersonnelDynaForm.set("startDate",periodDynaForm.get("startDate"));
//        newPersonnelDynaForm.set("endDate",periodDynaForm.get("endDate"));
        newPersonnelDynaForm.set("startDate",dynaValidatorForm.get("startDate"));
        newPersonnelDynaForm.set("endDate",dynaValidatorForm.get("endDate"));
        //COEUSDEV 209: End
        newPersonnelDynaForm.set("acType",TypeConstants.INSERT_RECORD);
        newPersonnelDynaForm.set("awProjectRole",EMPTY_STRING);
        newPersonnelDynaForm.set("awCostElement",EMPTY_STRING);
        newPersonnelDynaForm.set("awPeriodType",EMPTY_STRING);
        newPersonnelDynaForm.set("awPercentCharged",new Double(0));
        newPersonnelDynaForm.set("awPercentEffort",new Double(0));
        newPersonnelDynaForm.set("avUpdateTimestamp",prepareTimeStamp().toString());
        
        //System.out.println("*** EXIT createNewPersonnelDynaForm ***");
        /* return new dyna form - budget personnel details */
        newDynaForms.put(BudgetPersonnelDetailsBean.class, newPersonnelDynaForm);
        return newDynaForms;
    }
    /**
     * Get the BudgetLineItemDetails for the selected Period, Line Item Number and Person Number
     * Save the line item number, calculate, get the BudgetDetailCalAmts data
     * for the specific period and line item number
     * @param dynaForm
     * @throws Exception
     * @return
     */
    private String  getBudgetLineItemDetails(CoeusDynaBeansList dynaForm, HttpServletRequest request) throws Exception{
        //System.out.println("ENTER getBudgetLineItemDetails");
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        int budgetPeriod = Integer.parseInt(request.getParameter("budgetPeriod"));
        int lineItemNumber = Integer.parseInt(request.getParameter("lineItemNumber"));
        int personNumber = Integer.parseInt(request.getParameter("personNumber"));
        
        validateForApplyToLaterPeriods(budgetPeriod, request);
        List personnelDataList = new ArrayList();
        List lineItemDataList = new ArrayList();
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("MM/dd/yyyy");
        //java.text.NumberFormat numberFormat = java.text.NumberFormat.getCurrencyInstance();
        
        BeanUtilsBean copyBean = new BeanUtilsBean();
        Vector vecAllBudgetDetails = (Vector)session.getAttribute("budgetPersonnelData");
        Vector vecBudgetDetail = filterBudgetPeriod(budgetPeriod, vecAllBudgetDetails);
        if(vecBudgetDetail!= null && vecBudgetDetail.size() > 0){
            for(int index = 0; index < vecBudgetDetail.size(); index++){
                DynaValidatorForm dynaFormData = (DynaValidatorForm)vecBudgetDetail.get(index);
                int lineItemNo = ((Integer)dynaFormData.get("lineItemNumber")).intValue();
                int personNo = ((Integer)dynaFormData.get("personNumber")).intValue();
                if((lineItemNumber == lineItemNo)&&(personNumber == personNo)){
                    String tempLineItemStartDate = simpleDateFormat.format(dynaFormData.get("startDate"));
                    String tempLineItemEndDate = simpleDateFormat.format(dynaFormData.get("endDate"));
                    dynaFormData.set("tempLineItemStartDate",tempLineItemStartDate);
                    dynaFormData.set("tempLineItemEndDate",tempLineItemEndDate);
                    personnelDataList.add(dynaFormData);
                    break;
                }
            }
        }
        /* set personnel data */
        dynaForm.setList(personnelDataList);
        
        //CoeusVector cvBudgetDetailCalAmts = new CoeusVector();
        CoeusVector cvBudgetPersonnelCalAmts = new CoeusVector();
        //Vector vecBudgetDetailCalAmts = (Vector)session.getAttribute("BudgetDetailCalAmts");
        Vector vecBudgetPersonnelCalAmts = (Vector)session.getAttribute("BudgetPersonnelCalAmts");
        if(vecBudgetPersonnelCalAmts!= null && vecBudgetPersonnelCalAmts.size() > 0){
            for(int index = 0; index < vecBudgetPersonnelCalAmts.size(); index++){
                DynaValidatorForm dynaFormData = (DynaValidatorForm)vecBudgetPersonnelCalAmts.get(index);
                int dynaLineItemNo = ((Integer)dynaFormData.get("lineItemNumber")).intValue();
                int dynaBudgetNo = ((Integer)dynaFormData.get("budgetPeriod")).intValue();
                int personNo = ((Integer)dynaFormData.get("personNumber")).intValue();
                if((budgetPeriod == dynaBudgetNo) && (lineItemNumber == dynaLineItemNo) &&(personNumber == personNo)){
                    cvBudgetPersonnelCalAmts.addElement(dynaFormData);
                    lineItemDataList.add(dynaFormData);
                }
            }
            /* sort line item detail cal amts */
            CoeusVector cvSortCalAmts = sortCalAmts(cvBudgetPersonnelCalAmts);
            cvSortCalAmts.sort("rateClassCode",true);
            for(int index = 0; index < cvSortCalAmts.size(); index++){
                BudgetPersonnelCalAmountsBean budgetPersonnelCalAmountsBean = (BudgetPersonnelCalAmountsBean)cvSortCalAmts.get(index);
                copyBean = new BeanUtilsBean();
                DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)lineItemDataList.get(index);
                DynaBean dynaBean = ((DynaBean)dynaValidatorForm).getDynaClass().newInstance();
                copyBean.copyProperties(dynaBean, budgetPersonnelCalAmountsBean);
                lineItemDataList.set(index, dynaBean);
            }
            //Code commented for bug fix case#2758
//            dynaForm.setBeanList(lineItemDataList);
        }
        //Code added for bug fix case#2758
        dynaForm.setBeanList(lineItemDataList);
        request.setAttribute("period", new Integer(budgetPeriod));
        session.setAttribute("budgetPersonnelLineItemDynaBean" ,dynaForm);
        navigator = "success";
        //System.out.println("EXIT getBudgetLineItemDetails");
        return navigator;
    }
    
    /**
     * Validate when applyToLater Periods action is performed
     * Check if the Last period is made to perform the action and
     * Check if the generate all periods is done
     * @param budgetInfoBean
     * @param strBudgetPeriod
     * @throws Exception
     */
    private void validateForApplyToLaterPeriods(int budgetPeriod, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        Vector vecBudgetPeriod = (Vector)session.getAttribute("BudgetPeriodData");
        Vector vecBudgetDetail = (Vector)session.getAttribute("BudgetDetailsData");
        int lastBudgetRecord = vecBudgetPeriod.size() - 1;
        DynaValidatorForm dynaForm = (DynaValidatorForm)vecBudgetPeriod.get(lastBudgetRecord);
        int lastBudgetPeriod = ((Integer)dynaForm.get("budgetPeriod")).intValue();
        
        if(!checkPeriodGenerated(vecBudgetDetail)){
            session.setAttribute("firstPeriod", new Boolean(true));
        }else if(budgetPeriod == lastBudgetPeriod){
            session.setAttribute("lastPeriod", new Boolean(true));
        }else{
            session.setAttribute("firstPeriod", new Boolean(false));
            session.setAttribute("lastPeriod", new Boolean(false));
        }
    }
    /**Method to prepare BudgetPersonnelCalAmountsBean to dynaBean. This method is used for
     *display rate type in ascending order
     * @param cvBudgetDetailCalAmts
     * @throws Exception
     * @return vector of budget personnel cal amounts Bean
     */
    private CoeusVector sortCalAmts(CoeusVector cvBudgetDetailCalAmts) throws Exception{
        CoeusVector sortCalAmts = new CoeusVector();
        BudgetPersonnelCalAmountsBean budgetPersonnelCalAmountsBean = null;
        BeanUtilsBean copyBean = null;
        if(cvBudgetDetailCalAmts != null && cvBudgetDetailCalAmts.size() > 0){
            for(int index = 0; index < cvBudgetDetailCalAmts.size(); index++){
                DynaValidatorForm dynaForm = (DynaValidatorForm)cvBudgetDetailCalAmts.get(index);
                budgetPersonnelCalAmountsBean = new BudgetPersonnelCalAmountsBean();
                copyBean = new BeanUtilsBean();
                copyBean.copyProperties(budgetPersonnelCalAmountsBean, dynaForm);
                sortCalAmts.addElement(budgetPersonnelCalAmountsBean);
                
            }
        }
        return sortCalAmts;
    }
    
    //     /**Method to prepare DyanBean from BudgetDetailCalAmountsBean
    //      * @param cvSortCalAmts
    //      * @param dynaForm
    //      * @throws Exception
    //      * @return vector of cal amt of dynaforms
    //      */
    //     private CoeusVector prepareDynaBean(CoeusVector cvSortCalAmts,DynaValidatorForm dynaForm) throws Exception{
    //        CoeusVector cvBudgetDetailCalAmts = new CoeusVector();
    //        BeanUtilsBean copyBean = null;
    //        if(cvSortCalAmts != null && cvSortCalAmts.size() > 0){
    //           for(int index = 0; index < cvSortCalAmts.size(); index++){
    //                BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)cvSortCalAmts.get(index);
    //                copyBean = new BeanUtilsBean();
    //                DynaBean dynaBean = ((DynaBean)dynaForm).getDynaClass().newInstance();
    //                copyBean.copyProperties(dynaBean, budgetDetailCalAmountsBean);
    //                cvBudgetDetailCalAmts.addElement(dynaBean);
    //
    //            }
    //        }
    //        return cvBudgetDetailCalAmts;
    //    }
    
    private boolean validationForDate(CoeusDynaBeansList coeusDynaBeansList, HttpServletRequest request) throws Exception{
        List budgetPersonnelList = coeusDynaBeansList.getList();
        /* Above list is set when get line item details action is invoked. There will be only
         * one record in budgetPersonnelList - header information
         */
        DynaValidatorForm dynaForm = (DynaValidatorForm)budgetPersonnelList.get(0);
        String tempLineItemEndDate = ((String)dynaForm.get("tempLineItemEndDate")).trim();
        String tempLineItemStartDate = ((String)dynaForm.get("tempLineItemStartDate")).trim();
        DateUtils dateUtils = new DateUtils();
        boolean isValid = true;
        ActionMessages actionMessages = null ;
        if(tempLineItemStartDate.equals(EMPTY_STRING)){
            actionMessages = new ActionMessages();
            actionMessages.add("adjustPeriod_exceptionCode.1459" , new ActionMessage(
            "adjustPeriod_exceptionCode.1459" ) );
            saveMessages(request, actionMessages);
            return false;
            
        }else if(tempLineItemEndDate.equals(EMPTY_STRING)){
            actionMessages = new ActionMessages();
            actionMessages.add("adjustPeriod_exceptionCode.1460" , new ActionMessage(
            "adjustPeriod_exceptionCode.1460" ) );
            saveMessages(request, actionMessages);
            return false;
        }
        isValid = dateUtils.validateDate(tempLineItemStartDate, ":/.,|-");
        if(!isValid){
            actionMessages = new ActionMessages();
            actionMessages.add("budget_period_exceptionCode.1157" , new ActionMessage(
            "budget_period_exceptionCode.1157" ) );
            saveMessages(request, actionMessages);
            return isValid;
            
        }
        isValid = dateUtils.validateDate(tempLineItemEndDate, ":/.,|-");
        if(!isValid){
            actionMessages = new ActionMessages();
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
    
    private boolean validateEnteredDate(java.sql.Date enteredStartDate, java.sql.Date enteredEndDate,
    DynaValidatorForm dynaForm ,HttpServletRequest request) throws Exception{
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
    
    /**
     * This method is to calculate Budget when calculate is invoked
     * @param dynaForm
     * @throws Exception
     * @return
     */
    private String calculatePeriodLineItem(CoeusDynaBeansList dynaForm , int budgetPeriod ,
    HttpServletRequest request, HttpServletResponse response, HashMap hmOpenList) throws Exception{
        //System.out.println("*** ENTER calculatePeriodLineItem ($$$new routine$$$) ***");
        String navigator = "success";
        HttpSession session = request.getSession();
        //CoeusDynaBeansList coeusList = (CoeusDynaBeansList)session.getAttribute("budgetPersonnelDynaBean");
        List budgetPersonnelList = dynaForm.getList();
        //List budgetList = dynaForm.getBeanList();
        //int budgetPeriod = ((Integer)budgetList.get(0)).intValue();
         //Added the values to hashmap the pass as a parameter and check those values in another method for 
        // to remove the Instance level variable for Case 2960 -Start
        boolean calculateAllPeriods = false;
        boolean lineItemWindowOpened = false;
        int openLineItemNumber = -1;
        int openPersonNumber = -1;
        int OPEN_LINE_ITEM_NUMBER =-1;
        Integer openLineItemNo;
        Integer openPersonNo;
        if( hmOpenList !=null && hmOpenList.size() > 0){
            openLineItemNo = (Integer) hmOpenList.get("OPEN_LINE_ITEM_NUMBER");
            openPersonNo = (Integer) hmOpenList.get("OPEN_PERSON_NUMBER");
            if(openLineItemNo !=null){
                //convert to int ans set it to OPEN_LINE_ITEM_NUMBER
                openLineItemNumber = openLineItemNo.intValue();
            }
            if(openPersonNo !=null){
                openPersonNumber = openPersonNo.intValue();
            }
            
        }
        
          // Case 2960 -End
        
        // DynaValidatorForm dynaValidForm = (DynaValidatorForm)budgetPersonnelList.get(0);
        //int budgetPeriod =  ((Integer)dynaValidForm.get("budgetPeriod")).intValue(); //Integer.parseInt(request.getParameter("budgetPeriod"));
        
        //int budgetPeriod = Integer.parseInt(request.getParameter("budgetPeriod"));
        
        
        int reqToOpenBudgetPeriod = 0;
        if(request.getParameter("requestBudgetPeriod") != null && !request.getParameter("requestBudgetPeriod").equals(EMPTY_STRING ) && !request.getParameter("requestBudgetPeriod").equals("null")){
            reqToOpenBudgetPeriod = Integer.parseInt(request.getParameter("requestBudgetPeriod"));
        }
        
        /* get budget period data from session */
        Vector vecBudgetPeriod = (Vector)session.getAttribute("BudgetPeriodData");
        /* get budget info bean from session */
        BudgetInfoBean budgetInfoBean =  (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        /* get budget personnel details data from session */
        Vector vecBudgetPersonnel = (Vector)session.getAttribute("budgetPersonnelData");
        /* get the budget personnel calculated Amts from session */
        Vector vecBudgetPersonnelCalAmts = (Vector)session.getAttribute("BudgetPersonnelCalAmts");
        /* get budget details data from session */
        Vector vecBudgetDetails = (Vector)session.getAttribute("BudgetDetailsData");
        /* get budget calculated Amts from session */
        Vector vecBudgetCalAmts = (Vector)session.getAttribute("BudgetDetailCalAmts");
        //   Added for case#3654 - Third option 'Default' in the campus dropdown -Start
        if( request.getSession().getAttribute("bsOnOffCampusFlag") !=null ){
            Boolean onOffflag = (Boolean) request.getSession().getAttribute("bsOnOffCampusFlag"); 
            budgetInfoBean.setDefaultIndicator(onOffflag.booleanValue());
        }
        //  Added for case#3654 - Third option 'Default' in the campus dropdown -END
        
        /* set on/off campus flag for budget details and personnel details */
//        setOnOffCampusFlag(vecBudgetDetails, request);
        // Commented for Case #3507 -  F&A for Personnel line items - CoeusLite (on campus vs. off campus) - Start
        //Commented setOnOffCampusFlag() method because always take OnOffCampusflag from each CostElement, 
        //Problem is after adding Cost element if any change in Campus flag on Premium, that change is not reflecting. 
//        setOnOffCampusFlag(vecBudgetPersonnel, request);
        // Commented for Case #3507 -  F&A for Personnel line items - CoeusLite (on campus vs. off campus) - End
        /* get custom bean for dyna bean */
        HashMap hmBudgetDetail = prepareBudgetCustomData(vecBudgetDetails, vecBudgetCalAmts,
        vecBudgetPersonnel, vecBudgetPersonnelCalAmts, vecBudgetPeriod);
        CoeusVector cvBudgetDetails = (CoeusVector)hmBudgetDetail.get(BudgetDetailBean.class);
        CoeusVector cvBudgetCalamts = (CoeusVector)hmBudgetDetail.get(BudgetDetailCalAmountsBean.class);
        CoeusVector cvBudgetPersonnelData = (CoeusVector)hmBudgetDetail.get(BudgetPersonnelDetailsBean.class);
        CoeusVector cvBudgetPersonnelCalamts = (CoeusVector)hmBudgetDetail.get(BudgetPersonnelCalAmountsBean.class);
        CoeusVector cvBudgetPeriods = (CoeusVector)hmBudgetDetail.get(BudgetPeriodBean.class);
        //Commented - In the method setValueForDetails, the cvBudgetPeriods details is setting to the first line item of the cvBudgetDetails based on Budget Period.
        //cvBudgetDetails = setValueForDetails(cvBudgetPeriods, cvBudgetDetails);
        Hashtable htBudgetData = initializeBudgetCalculator(budgetInfoBean);
        
        htBudgetData.remove(BudgetDetailBean.class);
        htBudgetData.remove(BudgetDetailCalAmountsBean.class);
        htBudgetData.remove(BudgetPersonnelDetailsBean.class);
        htBudgetData.remove(BudgetPersonnelCalAmountsBean.class);
        htBudgetData.remove(BudgetPeriodBean.class);
        
        /* need to filter before sending IMPORTANT */
        htBudgetData.put(BudgetDetailBean.class, cvBudgetDetails);
        htBudgetData.put(BudgetDetailCalAmountsBean.class, cvBudgetCalamts);
        htBudgetData.put(BudgetPersonnelDetailsBean.class, cvBudgetPersonnelData);
        htBudgetData.put(BudgetPersonnelCalAmountsBean.class, cvBudgetPersonnelCalamts);
        htBudgetData.put(BudgetPeriodBean.class, cvBudgetPeriods);
        
        //Added to set the TotalDirectCost, TotalIndirectCost, UnderRecoveryAmount, CostSharingAmount and TotalCost to
        // zero, if there is no line items for any budget period - Start
        Equals equals;
        if(cvBudgetPeriods != null && !cvBudgetPeriods.isEmpty()) {
            BudgetPeriodBean budgetPeriodBean;
            for(Object budgetPeriods : cvBudgetPeriods) {
                budgetPeriodBean = (BudgetPeriodBean) budgetPeriods;
                equals = new Equals("budgetPeriod", new Integer(budgetPeriodBean.getBudgetPeriod()));
                And operator = new And(equals, CoeusVector.FILTER_ACTIVE_BEANS);
                CoeusVector cvLineItemDetails = cvBudgetDetails.filter(operator);
                if (cvLineItemDetails == null || cvLineItemDetails.isEmpty()) {
                    budgetPeriodBean.setTotalDirectCost(0);
                    budgetPeriodBean.setTotalIndirectCost(0);
                    budgetPeriodBean.setCostSharingAmount(0);
                    budgetPeriodBean.setUnderRecoveryAmount(0);
                    budgetPeriodBean.setTotalCost(0);
                }
            }
        }
        //End

        Hashtable htCalculatedData = null;
        //Take the value from the List and check the flag to remove the Instance level variable for Case 2960 -Start
        if(budgetPersonnelList !=null && budgetPersonnelList.size() >0 ){
            DynaValidatorForm calAllPeriodsForm = (DynaValidatorForm)budgetPersonnelList.get(0);
            Boolean calAllPeriods = (Boolean) calAllPeriodsForm.get("calculateAllPeriodsFlag");
            Boolean lineItemwinOpened = (Boolean) calAllPeriodsForm.get("lineItemWindowOpenedFlag");
            
            if(calAllPeriods !=null ){
                calculateAllPeriods = calAllPeriods.booleanValue();
            }
            if(lineItemwinOpened !=null){
                lineItemWindowOpened = lineItemwinOpened.booleanValue();
            }
        }
        // Added for Case 2960 - End
        if(calculateAllPeriods){
           
           // Added for COEUSQA-3044 Added Personnel type Cost Element does not calculate in Lite - start
           // This method is used for to calculate salary for given period for all line items.
            htCalculatedData = calculateSalary(htBudgetData,budgetPeriod,budgetInfoBean);
            // Added for COEUSQA-3044 -end
            htCalculatedData = calculateAllPeriods(htBudgetData, budgetInfoBean);
            
        }else {
            htCalculatedData = calculatedBudgetPeriod(htBudgetData,budgetPeriod,budgetInfoBean);
            // Case# 3854: Warning in Lite when salary effective date not in place for a calculation - Start
            if(! "Y".equalsIgnoreCase(request.getParameter("WarningDisplayed"))){
                // If the Warnig Page is NOT displayed 
                Vector vecMessages = getVecMessages();
                if(vecMessages != null && vecMessages.size() > 0 ){
                    request.setAttribute("budgetWarning", vecMessages);
                    return "showWarning";
                }
            }
            // Case# 3854: Warning in Lite when salary effective date not in place for a calculation - End
        }
        
        checkTotalCostLimit(request, htCalculatedData);
        
        CoeusVector cvBudgetPeriod = (CoeusVector)htCalculatedData.get(BudgetPeriodBean.class);
        cvBudgetPersonnelData = (CoeusVector)htCalculatedData.get(BudgetPersonnelDetailsBean.class);
        cvBudgetCalamts = (CoeusVector)htCalculatedData.get(BudgetDetailCalAmountsBean.class);
        cvBudgetPersonnelCalamts = (CoeusVector)htCalculatedData.get(BudgetPersonnelCalAmountsBean.class);
        cvBudgetDetails = (CoeusVector)htCalculatedData.get(BudgetDetailBean.class);
        
        /* prepare and get dyna bean from custom bean */
        HashMap hmBudgetData = prepareBudgetPersonnelData(cvBudgetDetails,cvBudgetPeriod,
        cvBudgetCalamts, cvBudgetPersonnelData,
        cvBudgetPersonnelCalamts, request);
        
        vecBudgetPeriod = (Vector)hmBudgetData.get(BudgetPeriodBean.class);
        vecBudgetDetails = (Vector)hmBudgetData.get(BudgetDetailBean.class);
        vecBudgetPersonnel = (Vector)hmBudgetData.get(BudgetPersonnelDetailsBean.class);
        vecBudgetPersonnelCalAmts = (Vector)hmBudgetData.get(BudgetPersonnelCalAmountsBean.class);
        vecBudgetCalAmts = (Vector)hmBudgetData.get(BudgetDetailCalAmountsBean.class);
        hmBudgetData.put(BudgetInfoBean.class, htCalculatedData.get(BudgetInfoBean.class));
        hmBudgetData.put(BudgetRateBaseBean.class, htCalculatedData.get(BudgetRateBaseBean.class));
        ActionMessages actionMessages = null ;
        //Check save flag
        if(request.getParameter("Save") != null){
            //Check the total Cost. If amount exceeding above $9,999,999,999.99 then send error message
            boolean isExceeding = checkTotalCost(htCalculatedData);
            if(isExceeding){
                setDataForExceedTotalCost(budgetPeriod,vecBudgetPeriod,vecBudgetDetails ,request, dynaForm);
                actionMessages = new ActionMessages();
                actionMessages.add("budget_common_exceptionCode.1011" , new ActionMessage(
                        "budget_common_exceptionCode.1011" ) );
                saveMessages(request, actionMessages);
                return navigator;
            }
            
            //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
            Vector vecCostElements = (Vector)session.getAttribute("budgetPersonnelData");
            
            //Commented and added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
            //get cost element details. If it returns true means it holds messages for inactive cost elements
            /*Vector vecCostElementMessages = getCostElementDetails(budgetPeriod,vecCostElements);
             if(vecCostElementMessages.size()>0){
                session.setAttribute("personnel_inactive_CE_messages",vecCostElementMessages);
            }else{
                session.setAttribute("personnel_inactive_CE_messages",null);
                saveBudgetPeriodLineItems(hmBudgetData,budgetPeriod, reqToOpenBudgetPeriod ,request, dynaForm);
            }*/
            
            //get inactiveType details. If it returns true means it holds messages for inactive types(Cost elements,AppointmentTypes,Period Types)
            Vector vecInactiveTypeMessages = getInactivetypeDetails(budgetPeriod,vecCostElements);
            
            if(vecInactiveTypeMessages.size()>0){
                session.setAttribute("personnel_inactive_CE_messages",vecInactiveTypeMessages);
            }else{
                session.setAttribute("personnel_inactive_CE_messages",null);
                saveBudgetPeriodLineItems(hmBudgetData,budgetPeriod, reqToOpenBudgetPeriod ,request, dynaForm);
            }
            //Commented and added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end
            
            //saveBudgetPeriodLineItems(hmBudgetData,budgetPeriod, reqToOpenBudgetPeriod ,request, dynaForm);
            //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
            if(!lineItemWindowOpened) {
                //Code modified for bug fix case#2758 - starts
                HashMap personnelMap = new HashMap();
//                String url =  "/getBudgetPersonnel.do?lineItemNumber=" + OPEN_LINE_ITEM_NUMBER + "&personNumber=" + OPEN_PERSON_NUMBER + "&budgetPeriod=";
                if(reqToOpenBudgetPeriod > 0){
                    personnelMap.put("budgetPeriod", new Integer(reqToOpenBudgetPeriod));
//                    url += reqToOpenBudgetPeriod + "&OldPeriod=" + budgetPeriod + "&reloadData=TRUE";
                }else{
                    personnelMap.put("budgetPeriod", new Integer(budgetPeriod));
//                    url += budgetPeriod + "&OldPeriod=" + budgetPeriod + "&reloadData=TRUE";
                }
                personnelMap.put("lineItemNumber", new Integer(openLineItemNumber));
                personnelMap.put("personNumber", new Integer(openPersonNumber));
                personnelMap.put("OldPeriod", new Integer(budgetPeriod));
                personnelMap.put("reloadData", "TRUE");
                personnelMap.put("Edit", request.getParameter("Edit"));
//                RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
//                requestDispatcher.forward(request,response);
                session.setAttribute("personnelMap", personnelMap);
                //Code added for bug fix case#2758 - ends
                navigator = "getBudgetPersonnel";
                return navigator;
            }
        }
        
        Vector vecFilterBudgetPersonnel = filterBudgetPeriod(budgetPeriod, vecBudgetPersonnel);
        
        session.setAttribute("budgetPersonnelData", vecBudgetPersonnel);
        /* set budget detail data to session */
        session.setAttribute("BudgetDetailsData", vecBudgetDetails);
        /* set the calculated Amts to session */
        session.setAttribute("BudgetDetailCalAmts", vecBudgetCalAmts);
        /* set budget period data to session */
        session.setAttribute("BudgetPeriodData", vecBudgetPeriod);
        /* set the budget personnel calculated Amts to session */
        session.setAttribute("BudgetPersonnelCalAmts", vecBudgetPersonnelCalAmts);
        session.setAttribute("budgetPersonnelData", vecBudgetPersonnel);
        
        //CoeusDynaBeansList coeusLineItemDynaList = (CoeusDynaBeansList)actionForm;
        List list = new ArrayList();
        for(int index = 0; index < vecFilterBudgetPersonnel.size(); index ++){
            DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)vecFilterBudgetPersonnel.get(index);
            list.add(dynaValidatorForm);
        }
        dynaForm.setList(list);
        
        List beanList = new ArrayList();
        beanList.add(new Integer(budgetPeriod));
        dynaForm.setBeanList(beanList);
        session.setAttribute("budgetPersonnelDynaBean",dynaForm);
        //validation framework end
        //Added for removing instance variable -case # 2960 - start
        BudgetInfoBean bgtInf = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        String queryKey = bgtInf.getProposalNumber()+bgtInf.getVersionNumber();
        //Added for removing instance variable - case # 2960- end         
        removeQueryEngineCollection(queryKey);
        //System.out.println("*** EXIT calculatePeriodLineItem ***");
        return navigator;
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
    protected Vector removeOldCalAmtsData(Vector vecCalAmtsData,int budgetPeriod, int lineItemNumber, int budgetCalType ,HttpServletRequest request) throws Exception{
        Vector vecTemp = null;
        HttpSession session  = request.getSession();
        if(vecCalAmtsData != null && vecCalAmtsData.size()>0){
            vecTemp = new Vector();
            for(int index = 0 ; index < vecCalAmtsData.size() ; index++){
                DynaValidatorForm dynaCalAmountForm = (DynaValidatorForm)vecCalAmtsData.get(index);
                int dynaBudgetPeriod = ((Integer)dynaCalAmountForm.get("budgetPeriod")).intValue();
                int dynaLineItemNumber = ((Integer)dynaCalAmountForm.get("lineItemNumber")).intValue();
                if(dynaBudgetPeriod == budgetPeriod && dynaLineItemNumber == lineItemNumber){
                    // System.out.println("delete budget period -> " + dynaBudgetPeriod + " and line item no -> " + dynaLineItemNumber);
                    dynaCalAmountForm.set("acType", TypeConstants.DELETE_RECORD);
                    switch(budgetCalType){
                        case 0: // budget detail cal amts
                            /* check deleted records - budget cal amts */
                            Vector vecDeletedBudgetCalAmts = (Vector)session.getAttribute("DeletedBudgetDetailCalAmts");
                            if(vecDeletedBudgetCalAmts == null){
                                vecDeletedBudgetCalAmts = new Vector();
                            }
                            vecDeletedBudgetCalAmts.add(dynaCalAmountForm);
                            session.setAttribute("DeletedBudgetDetailCalAmts", vecDeletedBudgetCalAmts);
                            break;
                        case 1: // budget personnel cal amts
                            /* check deleted records - budget personnel details */
                            Vector vecDeletedBudgetPersonnelCalAmts = (Vector)session.getAttribute("DeletedBudgetPersonnelCalAmts");
                            if(vecDeletedBudgetPersonnelCalAmts == null){
                                vecDeletedBudgetPersonnelCalAmts = new Vector();
                            }
                            vecDeletedBudgetPersonnelCalAmts.add(dynaCalAmountForm);
                            session.setAttribute("DeletedBudgetPersonnelCalAmts", vecDeletedBudgetPersonnelCalAmts);
                            break;
                    }
                    /*
                    WebTxnBean webTxnBean = new WebTxnBean();
                    dynaCalAmountForm.set("acType", TypeConstants.DELETE_RECORD);
                    webTxnBean.getResults(request, "updBudgetDetailCalAmts", dynaCalAmountForm);
                     */
                }
                else{
                    vecTemp.add(dynaCalAmountForm);
                }
            }//End of for
        }
        return vecTemp == null? new Vector():vecTemp ;
    }
    /** This method is to verify whether all periods generated. APPLY in BASE
     * @param Vector
     * @return boolean
     */
    private boolean checkPeriodGenerated(Vector vecBudgetData)throws Exception {
        boolean isPeriodGenerated = false;
        for(int index = 0; index < vecBudgetData.size(); index ++){
            DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)vecBudgetData.get(index);
            int budgetPeriod = ((Integer)dynaValidatorForm.get("budgetPeriod")).intValue();
            if(budgetPeriod > MAKE_DEFAULT_PERIOD) {
                isPeriodGenerated = true;
                break;
            }
        }
        return isPeriodGenerated;
    }
    
    private boolean isLockPresent(HttpServletRequest request)throws Exception {
        boolean isPresent = true;
        HttpSession session = request.getSession() ;
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        //WebTxnBean webTxnBean = new WebTxnBean();
        LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId()), request);
        boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
        LockBean lockData = getLockedData(CoeusLiteConstants.BUDGET_LOCK_STR+lockBean.getModuleNumber(), request);
        if(isLockExists || !lockBean.getSessionId().equals(lockData.getSessionId())) {
            String errMsg = "release_lock_for";
            ActionMessages messages = new ActionMessages();
            messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
            saveMessages(request, messages);
            isPresent = false;
        }
        return isPresent;
    }
    
    /**
     * Method to validate Budget Personnel Start Date and End Date
     * @return boolean true value to Valid Date
     * @throws Exception if exception occur
     */
    private boolean validateBudgetPersonnelDate(CoeusDynaBeansList coeusDynaBeansList,HttpServletRequest request) throws Exception{
        boolean isValidate = true;
        DateUtils dateUtils = new DateUtils();
        HttpSession session = request.getSession();
        List budgetList = coeusDynaBeansList.getList();
        List lstBudgetPeriod = coeusDynaBeansList.getBeanList();
        int selectedBudgetPeriod ;
        Vector vecBudgetPersonnel = (Vector) session.getAttribute("BudgetPeriodData");
        String bugetPeriod = request.getParameter("budgetPeriod");
        //Vector vecBudgetPeriodData = new Vector();
        Date periodStartDate = null;
        Date periodEndDate = null;
        if(bugetPeriod ==null){
            bugetPeriod = (String) lstBudgetPeriod.get(0);
        }
        if(bugetPeriod !=null && vecBudgetPersonnel !=null && vecBudgetPersonnel.size() > 0){
            selectedBudgetPeriod = Integer.parseInt(bugetPeriod);
            DynaActionForm dynaActionForm = (DynaActionForm) vecBudgetPersonnel.get(selectedBudgetPeriod-1);
            periodStartDate = (Date) dynaActionForm.get("startDate");
            periodEndDate = (Date) dynaActionForm.get("endDate");
            
        }
        
        if(budgetList != null){
            for(int index=0; index < budgetList.size() ; index++){
                DynaActionForm dynaForm = (DynaActionForm) budgetList.get(index);
                
                String date=dynaForm.get("personStartDate").toString();
                date = dateUtils.formatDate(date,":/.,|-","MM/dd/yyyy");
                dynaForm.set("personStartDate",date);
                date=dynaForm.get("personEndDate").toString();
                date = dateUtils.formatDate(date,":/.,|-","MM/dd/yyyy");
                dynaForm.set("personEndDate",date);
                
                
                Date startDate = dateUtils.getSQLDate(dynaForm.get("personStartDate").toString());
                Date endDate = dateUtils.getSQLDate(dynaForm.get("personEndDate").toString());
                dynaForm.set("startDate", startDate);
                dynaForm.set("endDate", endDate);
                
                if(startDate.before(periodStartDate) || startDate.after(periodEndDate)){
                    ActionMessages actionMessages = new ActionMessages();
                    actionMessages.add("budgetPersonnel_exceptionCode.StartDateNotPerior" , new ActionMessage(
                    "budgetPersonnel_exceptionCode.StartDateNotPerior",startDate) );
                    saveMessages(request, actionMessages);
                    isValidate = false;
                    return isValidate;
                }
                
                if(endDate.after(periodEndDate) || endDate.before(periodStartDate)){
                    ActionMessages actionMessages = new ActionMessages();
                    actionMessages.add("budgetPersonnel_exceptionCode.EndDateNotLater" , new ActionMessage(
                    "budgetPersonnel_exceptionCode.EndDateNotLater",endDate) );
                    saveMessages(request, actionMessages);
                    isValidate = false;
                    return isValidate;
                }
                double percentCharged =  ((Double)dynaForm.get("percentCharged")).doubleValue();
                if(percentCharged > 100.0){
                    ActionMessages actionMessages = new ActionMessages();
                    actionMessages.add("budgetPersonnel_exceptionCode.inValidPercentCharged" , new ActionMessage(
                    "budgetPersonnel_exceptionCode.inValidPercentCharged",endDate) );
                    saveMessages(request, actionMessages);
                    isValidate = false;
                    return isValidate;
                }
                double percentEffort =  ((Double)dynaForm.get("percentEffort")).doubleValue();
                if(percentEffort > 100.0){
                    ActionMessages actionMessages = new ActionMessages();
                    actionMessages.add("budgetPersonnel_exceptionCode.inValidPercentEffort" , new ActionMessage(
                    "budgetPersonnel_exceptionCode.inValidPercentEffort",endDate) );
                    saveMessages(request, actionMessages);
                    isValidate = false;
                    return isValidate;
                }
                
            }
        }
        
        return isValidate;
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
     
     //Added for TBA Persons - start - 4
     /**
      * This method gets the TBA data from the table OSP$TBA
      * and sets the collection to session scope
      * @param request
      * @throws Exception
      * @return String
      */
     private String getTBAData(HttpServletRequest request) throws Exception{
         
        String navigator = "success"; 
        Vector vecTBAData = new Vector();        
        WebTxnBean webTxnBean = new WebTxnBean();  
        HttpSession session = request.getSession();
        session.removeAttribute("tbaPersonsData"); 
        
        //Execute GET_TBA
        Hashtable htTBAData =(Hashtable)webTxnBean.getResults(request, "getTBA", null);
        vecTBAData = (Vector)htTBAData.get("getTBA");
        
        session.setAttribute("tbaPersonsData",vecTBAData);
        
        return navigator;
     }               
     
     /**
      * This method generates person Id for TBA person
      * using the function FN_GENERATE_PERSON_ID
      * @param request
      * @throws Exception
      * @return String
      */
     private String generatePersonIdForTBA(HttpServletRequest request) throws Exception{
      
         String personId = EMPTY_STRING;
         //HttpSession session = request.getSession();
         HashMap hmOutputData = new HashMap();
         WebTxnBean webTxnBean = new WebTxnBean();
         
         Hashtable htOutputData =(Hashtable)webTxnBean.getResults(request, "getPersonIdForTBA", null);
         hmOutputData = (HashMap)htOutputData.get("getPersonIdForTBA");         
         personId = (String)hmOutputData.get("ls_PersonID");
         
         return personId;
         
     }
     //Added for TBA Persons - end - 4
     
     // 4493: While adding a TBA appointment type should be defaulted to 12 Months - Start
     /**
      * Method used to get the Description for an Appointment Type Code.
      * Method searches for a matching Appointment type code in the vecAppointmentTypes.
      * If any matching entry found, returns the corresponding description. Else the method returns
      * Empty String.
      *
      * @param String Appointment Type Code
      * @param HttpServletRequest request
      * @return String Appointmnt Type Description
      */
     private String fetchAppointmentTypeDescription(String appointmentTypeCode, HttpServletRequest request) {
         String apntmntTypeDesc = EMPTY_STRING;
         HttpSession session = request.getSession();
         Vector vecAppointmentTypes = (Vector)session.getAttribute("appointmentTypesData");
         if(vecAppointmentTypes != null && vecAppointmentTypes.size() > 0){
             int appointmentCount = vecAppointmentTypes.size();
             ComboBoxBean cmbBean;
             for(int index = 0; index < appointmentCount; index++){
                 cmbBean = (ComboBoxBean) vecAppointmentTypes.get(index);
                 if(cmbBean.getCode() != null){
                     if(cmbBean.getCode().equalsIgnoreCase(appointmentTypeCode)){
                         apntmntTypeDesc = cmbBean.getDescription();
                     }
                 }
             }
         }
         return apntmntTypeDesc;
     }
     
     /**
      * This method is used to get the vale for the Parameter DEFAULT_TBA_APPOINTMENT_TYPE_CODE
      * from the Parameter Table.
      *
      * @param HttpServletRequest request
      * @return String DEFAULT_TBA_APPOINTMENT_TYPE_CODE value from the Parameter Table
      */
     private String fetchDefaultTBAAppointmentTypeCode(HttpServletRequest request) {
         WebTxnBean webTxnBean = new WebTxnBean();
         String defaultTbaApnmntTypeCode = EMPTY_STRING;
         HashMap hmParameterName = new HashMap();
         hmParameterName.put("parameterName",CoeusLiteConstants.DEFAULT_TBA_APPOINTMENT_TYPE_CODE);
         Hashtable htParameterValue = null;
         try {
             htParameterValue = (Hashtable) webTxnBean.getResults(request, "getParameterValue", hmParameterName);
         } catch (Exception ex) {
             ex.printStackTrace();
             return EMPTY_STRING;
         }
         if(htParameterValue != null){
             HashMap hmParameterValue = (HashMap)htParameterValue.get("getParameterValue");
             if(hmParameterValue != null){
                 defaultTbaApnmntTypeCode = (String)hmParameterValue.get("parameterValue");
             }
         }
         return defaultTbaApnmntTypeCode;
     }
     //4493: While adding a TBA appointment type should be defaulted to 12 Months - End
   
     //Added for COEUSQA-2553 Lite: Changing base salary in a budget doesn't update requested salary in all budget periods -start
     /**
     * This will Calculate and save the budget for the whole budget
     * Now it is using TxnBean to update the data to the database. 
     * @param budgetInfoBean Budget Information Bean
     * @param session session Object
     * @throws Exception If any error occurs
     */
    private void calulateAllPeriodsAndSaveBudget(BudgetInfoBean budgetInfoBean, HttpSession session) throws Exception{
        String navigator = "";
         boolean isSuccessFulModified = false;
        
        QueryEngine queryEngine = QueryEngine.getInstance();
        String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
                     
        Hashtable htBudgetData = initializeBudgetCalculator(budgetInfoBean);
        queryEngine.addDataCollection(queryKey, htBudgetData);
        CoeusVector cvBudgetInfo = new CoeusVector();
        cvBudgetInfo = (CoeusVector)htBudgetData.get(BudgetInfoBean.class);
        budgetInfoBean = (BudgetInfoBean)cvBudgetInfo.get(0);
        htBudgetData = calculateAllPeriods(htBudgetData, budgetInfoBean);
        edu.mit.coeus.bean.UserInfoBean userInfoBean = (edu.mit.coeus.bean.UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        BudgetUpdateTxnBean budgetUpdateTxnBean = new BudgetUpdateTxnBean(userInfoBean.getUserId());
        isSuccessFulModified = budgetUpdateTxnBean.addUpdDeleteBudget(htBudgetData);
         
    }
     //Added for COEUSQA-2553 - end
    
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
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
            //if both index and costElementIndex holding the same value then remove the costelement form the vector
            if(costElementIndex != index) {
                vecCostElements.remove(costElementIndex);
                index=index-1;
            }
        }
        return   vecCostElements;
    }
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
    
    //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
    /**
     * Method to fetch the Inactive type status
     * @param vecCostElements
     * @ returns boolean value
     */
     protected Vector getInactivetypeDetails(int budgetPeriod,Vector vecFilterBudgetDetailToShow) throws Exception{
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        Vector inActivecostElements = new Vector();
        Vector inActivePeriodTypes = new Vector();
        Vector vecInactiveTypeMessages = new Vector();
        StringBuffer errMsg;
        //remove duplicate cost elements from the vector vecCostElements
        if(vecFilterBudgetDetailToShow != null && !vecFilterBudgetDetailToShow.isEmpty()){
            HashSet hmCostElements = new HashSet();
            HashSet hmPeriodTypes = new HashSet();
            for(Object costElementDetails : vecFilterBudgetDetailToShow){
                DynaActionForm costElementForm = (DynaActionForm)costElementDetails;
                String budgetPeriodCE = costElementForm.get("budgetPeriod").toString();
                String costElement = costElementForm.get("costElement").toString();
                String   periodType = costElementForm.get("periodType").toString();
                if(budgetPeriodCE.equals(budgetPeriod+"")){
                    hmCostElements.add(costElement);
                    hmPeriodTypes.add(periodType);
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
                        vecInactiveTypeMessages.add(errMsg);
                    }
                }
            }
            //It gives err messages for the inactive period types
            if(!hmPeriodTypes.isEmpty()){
                Iterator finalPeriodIterator = hmPeriodTypes.iterator();
                while(finalPeriodIterator.hasNext()){
                    String periodType = (String)finalPeriodIterator.next();
                    CostElementsBean costElementsBean = budgetDataTxnBean.getPeriodTypeDetails(periodType);
                    if("N".equals(costElementsBean.getActive())){
                        inActivePeriodTypes.add(costElementsBean.getDescription());
                        errMsg = new StringBuffer();
                        String periodTypeDesc = costElementsBean.getDescription();
                        errMsg.append("Period Type ");
                        errMsg.append(periodTypeDesc);
                        errMsg.append(" is no longer active. Please select an alternative");
                        vecInactiveTypeMessages.add(errMsg);
                    }
                }
            }         
        }
        return vecInactiveTypeMessages;
    }    
    //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end
     
     //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
     /**
      * This method formats the string to double and returns the amount
      * @param String
      * @param HttpServletRequest
      * @return Double
      */
     private Double fetchBaseSalaryForPeriod(String strCalBase, HttpServletRequest request) throws Exception{
         double calBase = 0;
         if(strCalBase !=null && !strCalBase.equals(EMPTY_STRING)){
             try{
                 //format the string to double
                 calBase = formatStringToDouble(strCalBase);
             }catch(NumberFormatException ne){
                 ActionMessages actionMessages = new ActionMessages();
                 actionMessages.add("persons.baseSalary.invalid", new ActionMessage(
                         "persons.baseSalary.invalid") );
             }
         }
         return new Double(calBase);
     }
     
     /**
      * This method saves the error message for invalid base salary      
      * @param HttpServletRequest
      * @return void
      */
     private void saveActionMessage(HttpServletRequest request){
         ActionMessages actionMessages = new ActionMessages();
         actionMessages.add("persons.baseSalary.invalid", new ActionMessage(
                 "persons.baseSalary.invalid") );
         saveMessages(request, actionMessages);
     }
     //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
}
