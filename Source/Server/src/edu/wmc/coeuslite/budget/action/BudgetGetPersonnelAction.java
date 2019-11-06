/*
 * BudgetGetAction.java
 *
 * Created on March 6, 2006, 4:09 PM
 *
 * PMD check performed, and commented unused imports and variables on 15-FEB-2011
 * by Maharaja Palanichamy
 *
 */

package edu.wmc.coeuslite.budget.action;

//import org.apache.struts.action.ActionMessage;
//import org.apache.struts.action.ActionMessages;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import edu.wmc.coeuslite.budget.bean.ReadXMLData;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

//import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.FormBeanConfig;
//import java.util.Date;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.validator.DynaValidatorForm;

import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetPeriodBean;
import edu.mit.coeus.budget.bean.BudgetPersonsBean;
import edu.mit.coeus.budget.calculator.BudgetCalculator;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
//import java.text.SimpleDateFormat;
//import edu.wmc.coeuslite.utils.CoeusLineItemDynaList;
import edu.mit.coeuslite.utils.CoeusDynaBeansList;
import edu.mit.coeuslite.utils.ComboBoxBean;
//import edu.mit.coeus.utils.query.And;
//import edu.mit.coeus.utils.query.Equals;
//import edu.mit.coeuslite.irb.bean.ReadProtocolDetails;
//import edu.mit.coeuslite.utils.ComboBoxBean;
import edu.mit.coeuslite.utils.DateUtils;
//import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.wmc.coeuslite.budget.bean.CategoryBean;
//import edu.mit.coeus.utils.RateClassTypeConstants;
//import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
//import edu.mit.coeus.utils.UtilFactory;
import edu.wmc.coeuslite.budget.bean.ProposalBudgetHeaderBean;

/**
 *
 * @author  chandrashekara
 */
public class BudgetGetPersonnelAction extends BudgetBaseAction{
    //Menu code for personnel
    private static final String PERSONNEL = "B005";
    //Menu code for personnel budget
    private static final String PERSONNEL_BUDGET = "B009";   
        //commented to remove instance variable - start
//    private ActionForward actionForward = null;
    // commented to remove instance variable - end
    private HashMap hmProposalBudgetData = null;
    private static final String EMPTY_STRING = "";
   
   
   
    private static final String BUDGET_MENU_ITEMS ="budgetMenuItemsVector";
    private static final String XML_PATH = "/edu/wmc/coeuslite/budget/xml/BudgetMenu.xml";
    // commented to remove instance variable - start
//    private DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
    // commented to remove instance variable - end
    private static final int MAKE_DEFAULT_PERIOD = 1;
    private static final String GET_BUDGET_PERSONS = "/getBudgetPersons";
    private static final String GET_BUDGET_PERSONNEL = "/getBudgetPersonnel";
    private static final String ADD_BUDGET_PERSONNEL = "/AddBudgetPersonnel";
    private static final String TEST_BUDGET_PERSONS = "/getBudgetTestPersons";
    private static final String SELECT_BUDGET_PERSONNEL = "/selectBudgetPersonnel";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
   
    /* Added for Budget Personnel Cost Element without Persons Enhancement -Start     */
    private static final String NON_DETAILS_FOR_PERSONNEL = "/nonDetailsForPersonnel";
    private static final String PARAMETER_NAME = "ENABLE_SALARY_INFLATION_ANNIV_DATE";
    private static final String GET_PARAMETER_VALUE = "getParameterValue";
    /* Added for Budget Personnel Cost Element without Persons Enhancement -End     */
    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
    private static final String CALCULATE_BASE_SALARY = "/calculateBaseSalary";
    private static final String CALCULATE_ALL_BASE_SALARY = "/calculateAllBaseSalary";
    private static final String DEFAULT_INFLATION_RATE_FOR_SALARY = "DEFAULT_INFLATION_RATE_FOR_SALARY";
    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
    // commented to remove instance variable - case # 2960 - start
//    private String proposalNumber = null;
//    private int versionNumber  = -1;
    // commented to remove instance variables - case # 2960- end
    /** Creates a new instance of BudgetGetPersonnelAction */
    public BudgetGetPersonnelAction() {
    
    }
    
	/**
	 * Malini:11/15/2015- Method to find the budget person status
	 * 
	 * @param proposalNumber
	 * @param integer
	 * @return
	 */
	private boolean isBudgetPersonActive(String personId, String proposalNumber, Integer versionNumber) {
		BudgetPersonsBean budgetpersonBean = null;
		boolean isActive = true;
		BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
		try {
			CoeusVector budgetPersonsVec = budgetDataTxnBean.getBudgetPersons(proposalNumber, versionNumber);
			for (int i = 0; i < budgetPersonsVec.size(); i++) {
				budgetpersonBean = (BudgetPersonsBean) budgetPersonsVec.get(i);
				if (budgetpersonBean.getPersonId().equals(personId) && budgetpersonBean.getPersonStatus().equals("I")) {
					UtilFactory.log("BudgetGetPersonnelAction: Inactive person found " + budgetpersonBean.getFullName()
							+ budgetpersonBean.getPersonStatus());
					isActive = false;
				} else if (budgetpersonBean.getPersonId().equals(personId)
						&& budgetpersonBean.getPersonStatus().equals("A")) {
					UtilFactory.log("BudgetGetPersonnelAction: Active " + budgetpersonBean.getFullName()
							+ budgetpersonBean.getPersonStatus());
					isActive = true;
				} else if (budgetpersonBean.getPersonId().equals(personId)
						&& (budgetpersonBean.getPersonStatus().trim().equals("")
								|| budgetpersonBean.getPersonStatus() == null)) {
					// Returns a default value of true for external persons for
					// whom status data
					// does not exist on the DB
					isActive = true;
				}

			}

		} catch (DBException e) {

			e.printStackTrace();


		} catch (CoeusException e) {
			e.printStackTrace();


		} catch (NullPointerException ne) {
			ne.printStackTrace();


		}

		return isActive;
	}
    /* END Malini */
    
	private String getBudgetPersonStatus(String personId, String proposalNumber, Integer versionNumber) {
		BudgetPersonsBean budgetpersonBean = null;
		String pStatus = null;
		BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
		try {
			CoeusVector budgetPersonsVec = budgetDataTxnBean.getBudgetPersons(proposalNumber, versionNumber);
			for (int i = 0; i < budgetPersonsVec.size(); i++) {
				budgetpersonBean = (BudgetPersonsBean) budgetPersonsVec.get(i);
				if (budgetpersonBean.getPersonId().equals(personId) && budgetpersonBean.getPersonStatus().equals("I")
) {
					pStatus = "I";
					UtilFactory.log("BudgetGetPersonnelAction: Inactive person found " + budgetpersonBean.getFullName()
							+ budgetpersonBean.getPersonStatus() + budgetpersonBean.getIsExternalPerson());
				} else if (budgetpersonBean.getPersonId().equals(personId)
						&& budgetpersonBean.getPersonStatus().equals("A")
) {
					pStatus = "A";
					UtilFactory.log("BudgetGetPersonnelAction: Active " + budgetpersonBean.getFullName()
							+ budgetpersonBean.getPersonStatus() + budgetpersonBean.getIsExternalPerson());
				} else if (budgetpersonBean.getPersonId().equals(personId)
						&& ((budgetpersonBean.getPersonStatus().trim().equals("")
								|| budgetpersonBean.getPersonStatus() == null))
) {

					pStatus = "E";
					UtilFactory.log(
"external" + budgetpersonBean.getFullName() + budgetpersonBean.getIsExternalPerson());
				}

			}

		} catch (NullPointerException e) {
			UtilFactory.log(e.getMessage());
			e.printStackTrace();
		} catch (DBException de) {
			de.printStackTrace();
		} catch (CoeusException ce) {
			ce.printStackTrace();
		}
		return pStatus;
	}
	/* END Malini */
	private boolean isBudgetPersonExternal(String personId, String proposalNumber, Integer versionNumber) {
		BudgetPersonsBean budgetpersonBean = null;
		boolean pExternal = true;
		BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
		try {
			CoeusVector budgetPersonsVec = budgetDataTxnBean.getBudgetPersons(proposalNumber, versionNumber);
			for (int i = 0; i < budgetPersonsVec.size(); i++) {
				budgetpersonBean = (BudgetPersonsBean) budgetPersonsVec.get(i);
				if (budgetpersonBean.getPersonId().equals(personId)
						&& budgetpersonBean.getIsExternalPerson().equals("N")) {
					pExternal = false;
					UtilFactory.log("BudgetGetPersonnelAction: not External person" + budgetpersonBean.getFullName()
							+ budgetpersonBean.getIsExternalPerson());

				} else if (budgetpersonBean.getPersonId().equals(personId)
						&& budgetpersonBean.getIsExternalPerson().equals("Y")) {
					pExternal = true;

					UtilFactory.log("BudgetGetPersonnelAction: is External person " + budgetpersonBean.getFullName()
							+ budgetpersonBean.getIsExternalPerson());
				}

			}

		} catch (NullPointerException e) {
			UtilFactory.log(e.getMessage());
			e.printStackTrace();
		} catch (DBException de) {
			de.printStackTrace();
		} catch (CoeusException ce) {
			ce.printStackTrace();
		}
		return pExternal;
	}
	/* END Malini */

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
        /* Get proposal number and version number */
        HttpSession session = request.getSession();
        ProposalBudgetHeaderBean  headerBean = (ProposalBudgetHeaderBean)session.getAttribute("ProposalBudgetHeaderBean");        
        // added after removing instance variable - start
        ActionForward actionForward = null;
        // added after removing instance variable - end
        
        // commented to remove instance variable - start
//        this.proposalNumber = headerBean.getProposalNumber();
//        this.versionNumber = headerBean.getVersionNumber();
        // commented to remove instance variable - end
        // Added for removing instance variables - case # 2960-start
        String proposalNumber = headerBean.getProposalNumber();
        int versionNumber = headerBean.getVersionNumber();
        // Added for removing instance variables - case # 2960-end
        /* modified for Budget Personnel Cost Element without Persons Enhancement -Start     */
        HashMap hmRequiredDetails = new HashMap();
        if(actionMapping.getPath().equals(NON_DETAILS_FOR_PERSONNEL)){
            DynaValidatorForm dynaForm = (DynaValidatorForm)actionForm;
            hmRequiredDetails.put(DynaValidatorForm.class,dynaForm);
        }
        hmRequiredDetails.put(ActionMapping.class,actionMapping);
        hmRequiredDetails.put(ActionForm.class, actionForm);
        
        // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item -Start
        session.removeAttribute("hasMultiplePersons");
        // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item -End
        
        // modified to remove instance variables - case # 2960-start
        actionForward = getSelectedProposalBudgetData(actionForward,request,actionMapping, hmRequiredDetails,proposalNumber,versionNumber);
        // modified to remove instance variables - case # 2960- end
        //END
        return actionForward;
    }
    
    /**
     * This method will identify which request is comes from which path and
     * navigates to the respective ActionForward
     * @returns ActionForward object
     * @param hmProposalBudgetData
     * @param hmRequiredDetails
     * @throws Exception
     * @return
     */
    /* modified for Budget Personnel Cost Element without Persons Enhancement -Start     */
    // added actionForward,proposalNumber,versionNumber parameters to this method 
     private ActionForward getSelectedProposalBudgetData(ActionForward actionForward,HttpServletRequest request , 
        ActionMapping actionMapping, HashMap hmRequiredDetails,String proposalNumber,int versionNumber)throws Exception{
        String navigator = EMPTY_STRING;
        if(actionMapping.getPath().equals(GET_BUDGET_PERSONNEL)){
            //set the selected menu
            setSelectedStatusBudgetMenu(PERSONNEL_BUDGET, request);
            CoeusDynaBeansList coeusDynaBeanList = (CoeusDynaBeansList) hmRequiredDetails.get(ActionForm.class);            
            navigator = getBudgetPersonnel(request, coeusDynaBeanList,proposalNumber,versionNumber);
            actionForward = actionMapping.findForward(navigator);
        }else if(actionMapping.getPath().equals(GET_BUDGET_PERSONS)){
            //set the selected menu
            setSelectedStatusBudgetMenu(PERSONNEL, request);
            //Added for Case#2341 - Recalculate Budget if Project dates change - starts
            String value = (String) request.getSession().getAttribute("CHECK_BUDGET_DATES");
            request.getSession().removeAttribute("CHECK_BUDGET_DATES");
            if("CHECK_BUDGET_DATES".equals(value)){
                if(validateForProjectDates(request)){
                    request.setAttribute("CHANGE_BUDGET_DATES", "CHANGE_BUDGET_DATES");
                }
            }
            //Added for Case#2341 - Recalculate Budget if Project dates change - ends
            CoeusDynaBeansList coeusDynaBeanList = (CoeusDynaBeansList) hmRequiredDetails.get(ActionForm.class);            
            navigator = getBudgetPersons(request, coeusDynaBeanList,proposalNumber,versionNumber);
            actionForward = actionMapping.findForward(navigator);
        }else if(actionMapping.getPath().equals(ADD_BUDGET_PERSONNEL)){
            //set the selected menu
            setSelectedStatusBudgetMenu(PERSONNEL_BUDGET, request);
            CoeusDynaBeansList coeusDynaBeanList = (CoeusDynaBeansList) hmRequiredDetails.get(ActionForm.class);            
            navigator = addBudgetPersonnel(request, coeusDynaBeanList,proposalNumber,versionNumber);
            actionForward = actionMapping.findForward(navigator);
        }else if(actionMapping.getPath().equals(SELECT_BUDGET_PERSONNEL)){
            setSelectedStatusBudgetMenu(PERSONNEL_BUDGET, request);
            CoeusDynaBeansList coeusDynaBeanList = (CoeusDynaBeansList) hmRequiredDetails.get(ActionForm.class);            
            navigator =  selectBudgetPersonnel(request, coeusDynaBeanList);
            request.setAttribute("dataModified", "modified");
            actionForward = actionMapping.findForward(navigator);
        }
        else if(actionMapping.getPath().equals(NON_DETAILS_FOR_PERSONNEL)){
            DynaValidatorForm dynaForm = (DynaValidatorForm)hmRequiredDetails.get(DynaValidatorForm.class);
            navigator = getNonDetailsForPersonnel(request, dynaForm,proposalNumber,versionNumber);
            actionForward = actionMapping.findForward(navigator);
        }
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
        else if(actionMapping.getPath().equals(CALCULATE_BASE_SALARY)){
            CoeusDynaBeansList coeusDynaBeanList = (CoeusDynaBeansList) hmRequiredDetails.get(ActionForm.class);
            navigator = calculateSalaryForPeriods(request, coeusDynaBeanList,proposalNumber,versionNumber);
            actionForward = actionMapping.findForward(navigator);
        }
        else if(actionMapping.getPath().equals(CALCULATE_ALL_BASE_SALARY)){
            CoeusDynaBeansList coeusDynaBeanList = (CoeusDynaBeansList) hmRequiredDetails.get(ActionForm.class);
            navigator = calculateAllBaseSalaryForPeriods(request, coeusDynaBeanList,proposalNumber,versionNumber);
            actionForward = actionMapping.findForward(navigator);
        }
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
        return actionForward;
    }
/* modified for Budget Personnel Cost Element without Persons Enhancement -end     */    
    
    
    
     /**
      * This method gets the details of the LineItem
      * for the given budget period and line item number
      * @param request
      * @param dynaForm
      * @throws Exception
      * @return
      */
     // added parameters proposalNumber,versionNumber to this method after removing instance variables case # 2960
     private String getNonDetailsForPersonnel(HttpServletRequest request,
     DynaValidatorForm dynaForm,String proposalNumber, int versionNumber)throws Exception{
         String navigator = "success";
         WebTxnBean webTxnBean  = new WebTxnBean();
         HttpSession session = request.getSession();
         Vector vecBudgetCalAmts = new Vector();
         String budgetPeriod = request.getParameter("noPersonBudgetPeriod");
         String lineItemNumber = request.getParameter("noPersonLineItemNum");
         String noPersonVersion = request.getParameter("noPersonVersion");
                  Vector vecNoPersons = new Vector();
         String startDate = null;
         String endDate = null;
         String description = null;
         int quantity = 0;
         double lineItemCost = 0.00;
         double costSharingAmt = 0.00;
         double underRecoveryAmt = 0.00;
         boolean  applyInflation = false;
         boolean onOffCampus = false;
         Vector vecPersonnelNoPersons =
         (Vector)session.getAttribute("personnelCEwithoutPersons");
         if(vecPersonnelNoPersons != null && vecPersonnelNoPersons.size()>0){
             for(int index =0 ; index < vecPersonnelNoPersons.size();index++){
                 DynaValidatorForm dynaPersonnelNoPersons =
                 (DynaValidatorForm)vecPersonnelNoPersons.get(index);
                 if((budgetPeriod.equals(dynaPersonnelNoPersons.get("budgetPeriod").toString()))&&(
                 lineItemNumber.equals(dynaPersonnelNoPersons.get("lineItemNumber").toString()))&&
                 noPersonVersion.equals(dynaPersonnelNoPersons.get("versionNumber").toString())){
                 vecNoPersons.add(dynaPersonnelNoPersons);
                 break;
                 }
                 
             }
             
         }
         
         HashMap hmDetails = new HashMap();
         hmDetails.put("proposalNumber",proposalNumber);
         hmDetails.put("versionNumber", new Integer(versionNumber));
         Hashtable htData =
         (Hashtable)webTxnBean.getResults(request, "getBudgetDetCalAmts", hmDetails);
         vecBudgetCalAmts = (Vector)htData.get("getBudgetDetCalAmts");
         Vector vecBudgetDetCalAmts = new Vector();
         if(vecBudgetCalAmts != null && vecBudgetCalAmts.size()>0){
             for(int index = 0; index < vecBudgetCalAmts.size();index++){
                 DynaValidatorForm nonDetailsPersonnelForm =
                 (DynaValidatorForm)vecBudgetCalAmts.get(index);
                 if((budgetPeriod.equals(nonDetailsPersonnelForm.get("budgetPeriod").toString()))&&(
                 lineItemNumber.equals(nonDetailsPersonnelForm.get("lineItemNumber").toString()))&&
                 noPersonVersion.equals(nonDetailsPersonnelForm.get("versionNumber").toString())){
                 vecBudgetDetCalAmts.add(nonDetailsPersonnelForm);
                 }
             }
             session.setAttribute("noDetailsForPersonnel", vecBudgetDetCalAmts);
             session.setAttribute("detailsForPersonnel", vecNoPersons);
         }
         return navigator;
     }
     
    /** This method will get BudgetPersons  
     *  for personnel budget page
     */
     // added parameters proposalNumber,versionNumber to this method after removing instance variables -case # 2960
    private String addBudgetPersonnel(HttpServletRequest request,
        CoeusDynaBeansList coeusDynaBeanList,String proposalNumber, int versionNumber)throws Exception {
        String navigator = EMPTY_STRING;
        /* This method is invoked from personnel budget to select budget persons.
         * Get existing personnel budget dynabean list and store that in session
         * for later use
         */
        WebTxnBean webTxnBean  = new WebTxnBean();
        HttpSession session = request.getSession();
        CoeusDynaBeansList personnelDynaBeanList = (CoeusDynaBeansList)session.getAttribute("budgetPersonnelDynaBean");
        List personnelList = personnelDynaBeanList.getList();
        Vector vecBudgetPersonnel = new Vector();
        int totalPersons = personnelList.size();
        
        for(int index=0; index<totalPersons; index++){           
            vecBudgetPersonnel.add(personnelList.get(index));
        }
        
        /**DynaValidatorForm dynaValidForm = (DynaValidatorForm)personnelList.get(0);
        int budgetPeriod = ((Integer)dynaValidForm.get("budgetPeriod")).intValue(); */
        int budgetPeriod = Integer.parseInt(request.getParameter("budgetPeriod"));
        
        /* get budget person information */
        Vector vecBudgetPersons = new Vector();
        /* set parameter to get person information */
        HashMap hmPersonListParam = new HashMap();
        hmPersonListParam.put("proposalNumber",proposalNumber);
        hmPersonListParam.put("versionNumber", new Integer(versionNumber));
        /* use webtransaction and get data from database */
        Hashtable htPersonsListData = 
            (Hashtable)webTxnBean.getResults(request, "getBudgetPersons", hmPersonListParam);
        vecBudgetPersons = (Vector)htPersonsListData.get("getPersonList");

        /* add data to dynabean list */
        List list = new ArrayList();
        if(vecBudgetPersons!=null && vecBudgetPersons.size() > 0){
            for(int index = 0; index < vecBudgetPersons.size(); index ++){
                DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)vecBudgetPersons.get(index);                
                list.add(dynaValidatorForm);
            }
        }        

        coeusDynaBeanList.setList(list);        
        
        session.setAttribute("budgetPersonnelDynaBean" ,coeusDynaBeanList);
        session.setAttribute("personnelDynaBeanList" ,vecBudgetPersonnel);
        
        request.setAttribute("SelectedBudgetPeriodNumber", new Integer(budgetPeriod));

        navigator = "success";
        return navigator;
    }
    
    /** This method will get the data for the BudgetPersons and set to
     *  the respective form
     */
    // added parameters proposalNumber,versionNumber to this method after removing instance variables.-case # 2960
    private String getBudgetPersons(HttpServletRequest request, CoeusDynaBeansList coeusDynaBeanList,String proposalNumber, int versionNumber)throws Exception {
        
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        WebTxnBean  webTxnBean = new WebTxnBean();
        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
        /* Set argument name to get data from lookup table */
        //String argumentName = "AppointmentTypes"; 
        /* Get appointment types from argument lookup table */
        //HashMap hmlookupArgument = new HashMap();
        //hmlookupArgument.put("argumentName",argumentName);
        //Hashtable htAppointmentTypesData = 
        //    (Hashtable)webTxnBean.getResults(request, "getArgumentValuesData", hmlookupArgument);
        //Vector vecAppointmentTypes = (Vector)htAppointmentTypesData.get("getArgValueList");
        Vector vecAppointmentTypes = fetchAppointmentTypes();
        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
        
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
        //to fetch the period data
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        CoeusVector cvBudgetPeriods = budgetDataTxnBean.getBudgetPeriods(proposalNumber, versionNumber);
        if(cvBudgetPeriods==null){
            cvBudgetPeriods = new CoeusVector();
        }
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
        
        /* get budget persons */
        HashMap hmProposalBudgetParam = new HashMap();
        hmProposalBudgetParam.put("proposalNumber",proposalNumber);
        hmProposalBudgetParam.put("versionNumber", new Integer(versionNumber));
        Hashtable htBudgetPersonsData = (Hashtable)webTxnBean.getResults(request,"getPropBudgetPersonData",hmProposalBudgetParam);
        Vector vecBudgetPersons = (Vector)htBudgetPersonsData.get("getBudgetPersonsData");
        
        /* add data to dynabean list */
        List list = new ArrayList();
        if(vecBudgetPersons!= null && vecBudgetPersons.size() > 0){
            for(int index = 0; index < vecBudgetPersons.size(); index ++){
                DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)vecBudgetPersons.get(index);
                //Modified for case#3869-Save not working for budget person - Start                
                 if(vecAppointmentTypes != null && vecAppointmentTypes.size() >0){
                    for(int appCnt = 0 ;appCnt <vecAppointmentTypes.size();appCnt++){
                        edu.mit.coeuslite.utils.ComboBoxBean comboBoxBean = (edu.mit.coeuslite.utils.ComboBoxBean)vecAppointmentTypes.get(appCnt);
                        if(dynaValidatorForm.get("appointmentType") != null && dynaValidatorForm.get("appointmentType").equals(comboBoxBean.getCode())){
                            dynaValidatorForm.set("appointmentType",comboBoxBean.getDescription());
                            dynaValidatorForm.set("awAppointmentType",comboBoxBean.getDescription());
                            break;
                        }
                    }
                 }
                //Modified for case#3869-Save not working for budget person - End
                 
 				// Malini:11/25/15:added to highlight inactive budgetPersons
 				String personId = (String) dynaValidatorForm.get("personId");
 				try {
					
					UtilFactory.log(
							"Active:" + isBudgetPersonActive(personId, proposalNumber, new Integer(versionNumber)));

					if (isBudgetPersonActive(personId, proposalNumber, new Integer(versionNumber))) {
						dynaValidatorForm.set("isBudgetPersonActive", "Y");
					} else {
						dynaValidatorForm.set("isBudgetPersonActive", "N");
					}

					if (isBudgetPersonExternal(personId, proposalNumber, new Integer(versionNumber))) {
						dynaValidatorForm.set("isExternal", "Y");
					} else {
						dynaValidatorForm.set("isExternal", "N");
					}

					UtilFactory.log(
							"External:" + isBudgetPersonExternal(personId, proposalNumber, new Integer(versionNumber)));
				


					
 				/* END Malini */                 
				} catch (NullPointerException ne) {
					ne.printStackTrace();

				}
                list.add(dynaValidatorForm);
            }
        }
        coeusDynaBeanList.setList(list);  
        //Added for Case 2918 – Use of Salary Anniversary Date for calculating inflation in budget development module-Start
        String parameterValue = getParameterValue(request);
        session.setAttribute("ENABLE_SALARY_INFLATION_ANNIV_DATE",parameterValue);
        //Added for Case 2918 – Use of Salary Anniversary Date for calculating inflation in budget development module-Start
        session.setAttribute("budgetPersonsDynaBean" ,coeusDynaBeanList);
        session.setAttribute("appointmentTypesData" , vecAppointmentTypes);
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start        
        session.setAttribute("budgetPeriodData" , cvBudgetPeriods);
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End        
        session.removeAttribute("DeletedBudgetPersonData");
            
        navigator = "success";
        return navigator;
    }
       
    /** Create a new dynaform  
     *  for personnel budget 
     */
    private DynaBean createNewDynaForm(int budgetPeriod, String personName, String personId,
                                        String jobCode ,HttpServletRequest request)throws Exception {
                HttpSession session = request.getSession();                      
                // added to remove instance variables - case # 2960-start
                DateUtils dateUtils = new DateUtils();
                // added to remove instance variables - case # 2960-end
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

               String costElementCode = EMPTY_STRING;
               String costElementDescription = EMPTY_STRING;
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
               newBudgetDynaForm.set("quantity",new Double(1));
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
               newBudgetDynaForm.set("onOffCampusFlag",new Boolean(true));
               //newBudgetDynaForm.set("applyInRateFlag",new Boolean(true));
               newBudgetDynaForm.set("updateTimestamp",dbTimestamp.toString());
               newBudgetDynaForm.set("avUpdateTimestamp",prepareTimeStamp().toString());
               newBudgetDynaForm.set("acType",TypeConstants.INSERT_RECORD);
               //COEUSQA-1693 - Cost Sharing Submission - start
               newBudgetDynaForm.set("submitCostSharingFlag","Y");
               //newBudgetDynaForm.set("tempSubmitCostSharingFlag",new Boolean(true));               
               //COEUSQA-1693 - Cost Sharing Submission - start
               
               /* add the new dyna form */
               vecBudgetDetails.add(newBudgetDynaForm);
               session.setAttribute("BudgetDetailsData", vecBudgetDetails);
               
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
                newPersonnelDynaForm.set("onOffCampusFlag",new Boolean(true));
                newPersonnelDynaForm.set("applyInRateFlag",new Boolean(true));
                newPersonnelDynaForm.set("updateTimestamp",dbTimestamp.toString());
                newPersonnelDynaForm.set("startDate",periodDynaForm.get("startDate"));
                newPersonnelDynaForm.set("endDate",periodDynaForm.get("endDate"));
                newPersonnelDynaForm.set("acType",TypeConstants.INSERT_RECORD);
                newPersonnelDynaForm.set("awProjectRole",EMPTY_STRING);
                newPersonnelDynaForm.set("awCostElement",EMPTY_STRING);
                newPersonnelDynaForm.set("awPeriodType",EMPTY_STRING);
                newPersonnelDynaForm.set("awPercentCharged",new Double(0));
                newPersonnelDynaForm.set("awPercentEffort",new Double(0));
               newBudgetDynaForm.set("avUpdateTimestamp",dbTimestamp.toString());
         
               newPersonnelDynaForm.set("personStartDate",dateUtils.formatDate(periodDynaForm.get("startDate").toString(),SIMPLE_DATE_FORMAT) );
               newPersonnelDynaForm.set("personEndDate",dateUtils.formatDate(periodDynaForm.get("endDate").toString(),SIMPLE_DATE_FORMAT) );
                /* return new dyna form - budget personnel details */
                return newPersonnelDynaForm;
    }
    
    /** This method will get BudgetPersons  
     *  for personnel budget page
     */
    private String selectBudgetPersonnel(HttpServletRequest request,
        CoeusDynaBeansList coeusDynaBeanList)throws Exception {
        System.out.println("***ENTER selectBudgetPersonnel***");
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        /* Add selected budget persons to existing personnel budget list.
         * Get existing personnel budget dynabean list from session and add
         * selected persons.
         */
        Vector vecDynaBudgetPersonnel = (Vector)session.getAttribute("personnelDynaBeanList");
        /* get budget personnel for all budget periods from session */
        Vector vecBudgetPersonnel = (Vector)session.getAttribute("budgetPersonnelData");
        
        List budgetPersonsList = coeusDynaBeanList.getList();
        List budgetPersonnelList = new ArrayList();
        
        for(int index=0; index<vecDynaBudgetPersonnel.size(); index++){
            budgetPersonnelList.add(vecDynaBudgetPersonnel.get(index));
        }

        int budgetPeriod = Integer.parseInt(request.getParameter("budgetPeriod"));
        int rowIndex = Integer.parseInt(request.getParameter("rowIndex"));
        
        /* if single selection (with name hypertext) then rowIndex is set to -1 */
        if(rowIndex >= 0){
            DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)budgetPersonsList.get(rowIndex);
            DynaBean newDynaForm = createNewDynaForm(budgetPeriod, (String)dynaValidatorForm.get("personName"), 
                                                dynaValidatorForm.get("personId").toString(), 
                                                dynaValidatorForm.get("jobCode").toString(),
                                                request);
            budgetPersonnelList.add(newDynaForm);
            vecBudgetPersonnel.add(newDynaForm);
        }else{
            int numberOfPersons = budgetPersonsList.size();
            for(int index=0; index < numberOfPersons; index++){
                DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)budgetPersonsList.get(index);
                boolean personSelected = ((Boolean)dynaValidatorForm.get("personSelected")).booleanValue(); 
                if(personSelected){
                    DynaBean newDynaForm = createNewDynaForm(budgetPeriod, (String)dynaValidatorForm.get("personName"), 
                                                    dynaValidatorForm.get("personId").toString(), 
                                                    dynaValidatorForm.get("jobCode").toString(),
                                                    request);
                    budgetPersonnelList.add(newDynaForm);
                    vecBudgetPersonnel.add(newDynaForm);
                }
        }
            
        }
        coeusDynaBeanList.setList(budgetPersonnelList);        
        
        Vector vecFilterBudgetPersonnel = filterBudgetPeriod(budgetPeriod, vecBudgetPersonnel);
        /* data for requested period */
        request.setAttribute("FilterBudgetPersonnelData", vecFilterBudgetPersonnel);
        request.setAttribute("SelectedBudgetPeriodNumber", new Integer(budgetPeriod));

        
        session.setAttribute("budgetPersonnelData", vecBudgetPersonnel);
        session.setAttribute("budgetPersonnelDynaBean" ,coeusDynaBeanList);
        session.removeAttribute("personnelDynaBeanList");
        
        navigator = "success";
        return navigator;
    }
    
    
    /** This method will gets the data for the BudgetPersonnel and sets to
     *the respective form
     */
    // Added the parameters proposalNumber,versionNumber to this method after removng instance variables - case # 2960
    private String getBudgetPersonnel(HttpServletRequest request,
        CoeusDynaBeansList coeusDynaBeanList,String proposalNumber, int versionNumber)throws Exception {        
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        
        //System.out.println("*** ENTER getBudgetPersonnel ***");
        /* Set budgetPeriod default to 1. When this method is invoked from left menu (personnel budget) link
         * display page with period 1 data else get appropriate period to filter data.
         */
        int budgetPeriod = 1;
        int prevBudgetPeriod = 1;
        boolean reloadDataAfterSave = false;
                
        boolean dataExist = false;
         //Code modified for bug fix case#2758
        HashMap personnelMap = (HashMap)request.getSession().getAttribute("personnelMap");
        if(personnelMap!=null && personnelMap.get("budgetPeriod")!= null){
            budgetPeriod = Integer.parseInt(personnelMap.get("budgetPeriod").toString());
            prevBudgetPeriod = Integer.parseInt(personnelMap.get("OldPeriod").toString());
            dataExist = true;
        } else if(request.getParameter("budgetPeriod")!=null){
            budgetPeriod = Integer.parseInt(request.getParameter("budgetPeriod"));
            prevBudgetPeriod = Integer.parseInt(request.getParameter("OldPeriod"));
            dataExist = true;            
        }
        /* force to reload data from database after save */
        //Code added for bug fix case#2758
        if(personnelMap!=null && personnelMap.get("reloadData")!= null){
            dataExist = false;
        } else if(request.getParameter("reloadData")!=null){
            dataExist = false;
        }
        
        /* if data fetched during previous run (LHS Personnel budget menu link) then get data from session 
         * else get data from database.
         */
        // commented to filter datas based on budget periods
//        if(!dataExist){        
        // parameters proposalNumber and versionNumber are added after removing instance variables -case # 2960
            getBudgetPersonnelFromDB(request, budgetPeriod,proposalNumber,versionNumber);
//        }
        /* get recent budget personnel data stored in session */
        Vector vecBudgetPersonnel = (Vector)session.getAttribute("budgetPersonnelData");
        
        /* filter data for appropriate budget period */
        Vector vecFilterBudgetPersonnel = filterBudgetPeriod(budgetPeriod, vecBudgetPersonnel);

        /* add data to dynabean list */
        List list = new ArrayList();
        List personList = new ArrayList();
        for(int index = 0; index < vecFilterBudgetPersonnel.size(); index ++){
            DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)vecFilterBudgetPersonnel.get(index);
            list.add(dynaValidatorForm);
            
            // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item -Start
            if(personnelMap!=null && personnelMap.get("Edit")!= null){
                int dynaLineItemNumber = Integer.parseInt(dynaValidatorForm.get("lineItemNumber").toString());
                int lineItemNumber = Integer.parseInt(personnelMap.get("lineItemNumber").toString());
                if(lineItemNumber == dynaLineItemNumber){
                    personList.add((Integer)dynaValidatorForm.get("personNumber"));
                }                
            }            
                      
        }
        if(personList !=null && personList.size() >1){
            session.setAttribute("hasMultiplePersons","Yes");
        }
        // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item -End  
        coeusDynaBeanList.setList(list);        
        
        List beanList = new ArrayList();
        beanList.add(new Integer(budgetPeriod));
        coeusDynaBeanList.setBeanList(beanList);

        /* view/edit personnel line item details */
        //Code added for bug fix case#2758
        if(personnelMap!=null && personnelMap.get("Edit")!= null){
            int lineItemNumber = Integer.parseInt(personnelMap.get("lineItemNumber").toString());
            int personNumber = Integer.parseInt(personnelMap.get("personNumber").toString());
            request.setAttribute("lineItemNumber", new Integer(lineItemNumber));
            request.setAttribute("personNumber", new Integer(personNumber));
            request.setAttribute("popUp","fromLocation");
        } else if(request.getParameter("Edit")!=null){
            int lineItemNumber = Integer.parseInt(request.getParameter("lineItemNumber"));
            int personNumber = Integer.parseInt(request.getParameter("personNumber"));
            request.setAttribute("lineItemNumber", new Integer(lineItemNumber));
            request.setAttribute("personNumber", new Integer(personNumber));
            request.setAttribute("popUp","fromLocation");            
        }
        request.getSession().removeAttribute("personnelMap");
        /* data for requested period */
        request.setAttribute("FilterBudgetPersonnelData", vecFilterBudgetPersonnel);
        request.setAttribute("SelectedBudgetPeriodNumber", new Integer(budgetPeriod));
        session.setAttribute("budgetPersonnelDynaBean",coeusDynaBeanList);
        
        navigator = "success";
        //System.out.println("*** EXIT getBudgetPersonnel ***");
            
        return navigator;
    }
   
    /** This method is to verify whether all periods generated
     * @param Vector
     * @return boolean
     */
    private boolean checkPeriodGenerated(Vector vecBudgetData)throws Exception {
        boolean isPeriodGenerated = false;
        if(vecBudgetData!=null && vecBudgetData.size()>0){
            for(int index = 0; index < vecBudgetData.size(); index ++){
                DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)vecBudgetData.get(index);
                int budgetPeriod = ((Integer)dynaValidatorForm.get("budgetPeriod")).intValue();
                if(budgetPeriod > MAKE_DEFAULT_PERIOD) {
                    isPeriodGenerated = true;
                    break;
                }
            }
        }
        return isPeriodGenerated;
    }
    
    /** This method will get appropriate data from database
     * and set it to session
     */
    // Added parameters proposalNumber,versionNumber to this method after removing instance variables -case # 2960
    private void getBudgetPersonnelFromDB(HttpServletRequest request, 
        int budgetPeriod,String proposalNumber, int versionNumber)throws Exception {
        //System.out.println("*** ENTER getBudgetPersonnelFromDB ***");
        
        /* input parameters required to get personnel budget data */
        HttpSession session = request.getSession();        
        // added to remove instance variables - case # 2960-start
        DateUtils dateUtils = new DateUtils();
        // added to remove instance variables - case # 2960- end        
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmProposalBudgetParam = new HashMap();
        hmProposalBudgetParam.put("proposalNumber",proposalNumber);
        hmProposalBudgetParam.put("versionNumber", new Integer(versionNumber));
        
        /* use webtransaction and get data from database */
        Hashtable htBudgetPersonnelData = (Hashtable)webTxnBean.getResults(request,"getPropBudgetPersonnelData",hmProposalBudgetParam);
        Vector vecBudgetPersonnel = (Vector)htBudgetPersonnelData.get("fetchBudgetPersonnelData");
        Vector vecBudgetPeriod = (Vector)htBudgetPersonnelData.get("fetchBudgetPeriod");
        Vector vecBudgetPersonnelCalAmts = (Vector)htBudgetPersonnelData.get("fetchBudgetPersonnelCalAmts");
        Vector vecBudgetDetails = (Vector)htBudgetPersonnelData.get("fetchBudgetDetail");
        Vector vecBudgetDetailCalAmts = (Vector)htBudgetPersonnelData.get("fetchBudgetDetailCalAmts");

	//Added for Case 3197 - Allow for the generation of project period greater than 12 months -start
        vecBudgetPeriod = calculatePeriodNumberOfMonths(vecBudgetPeriod);
        //3197 - end
        
		 /* Added for Budget Personnel Cost Element without Persons Enhancement -Start     */
        
        Vector vecPersonnelNoPersons = (Vector)htBudgetPersonnelData.get("getBudgetPersonnelCostElemNoPersons");
        
        /* Added for Budget Personnel Cost Element without Persons Enhancement -End     */

        /* get data for dropdown - Project Roles */
        Vector vecProjectRoles = new Vector();
        /* set parameter to get project roles from argument lookup table */
        String argumentName = "ProjectRoles"; 
        HashMap hmlookupArgument = new HashMap();
        hmlookupArgument.put("argumentName",argumentName);
        /* use webtransaction and get data from database */
        Hashtable htProjectRolesData = 
            (Hashtable)webTxnBean.getResults(request, "getArgumentValuesData", hmlookupArgument);
        vecProjectRoles = (Vector)htProjectRolesData.get("getArgValueList");

        /* get data for dropdown - Budget periods */
        Vector vecPeriodTypes = new Vector();
        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
        /* set parameter to get periods from argument lookup table */
        //argumentName = "PeriodTypes"; 
        //hmlookupArgument = new HashMap();
        //hmlookupArgument.put("argumentName",argumentName);
        /* use webtransaction and get data from database */
        //htProjectRolesData = 
        //    (Hashtable)webTxnBean.getResults(request, "getArgumentValuesData", hmlookupArgument);
        //vecPeriodTypes = (Vector)htProjectRolesData.get("getArgValueList");
        Vector vecAllPeriodTypes = fetchPeriodTypes();
        Vector vecBothPeriodTypes = (Vector)vecAllPeriodTypes.get(0);
        vecPeriodTypes = (Vector)vecAllPeriodTypes.get(1);
        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
        
        /* get data for dropdown - Person names */
        Vector vecBudgetPersons = new Vector();
        /* set parameter to get person names */
        HashMap hmPersonListParam = new HashMap();
        hmPersonListParam.put("proposalNumber",proposalNumber);
        hmPersonListParam.put("versionNumber", new Integer(versionNumber));
        /* use webtransaction and get data from database */
        Hashtable htPersonsListData = 
            (Hashtable)webTxnBean.getResults(request, "getBudgetPersons", hmPersonListParam);
        vecBudgetPersons = (Vector)htPersonsListData.get("getPersonList");

        /* set parameter to get personnel cost elements */
        HashMap hmCostElementData = new HashMap();
        String categoryCode = "01"; //personnel category code
        hmCostElementData.put("categoryCode",categoryCode);
        hmCostElementData.put("mappingName","S2S");
        /* use webtransaction and get data from database */
        Hashtable htCostData = 
            (Hashtable)webTxnBean.getResults(request, "getBudgetCostElementData", hmCostElementData);
        Vector vecCostElements = (Vector)htCostData.get("getBudgetCostElementData");
        
        //COEUSQA-1414 Allow schools to indicate if cost element is still active - Start
        Vector vecFilteredCostElement = new Vector();
        session.setAttribute("costElementData" , vecCostElements);
        if(vecCostElements != null && vecCostElements.size() > 0){            
            CategoryBean defaultBean = new CategoryBean();
            defaultBean.setCostElement(EMPTY_STRING);
            defaultBean.setBudgetCategoryCode(0);
            defaultBean.setDescription(EMPTY_STRING);
            defaultBean.setCampusFlag(OFF_CAMPUS_FLAG);
            defaultBean.setActiveFlag("");
            vecCostElements.insertElementAt(defaultBean, 0);
            for(Object obj:vecCostElements){
                defaultBean = (CategoryBean)obj;
                if("Y".equals(defaultBean.getActiveFlag())
                   || "".equals(defaultBean.getActiveFlag())){
                    vecFilteredCostElement.add(defaultBean);
                }
            }
        }
        //COEUSQA-1414 Allow schools to indicate if cost element is still active - End
        
//        if(vecCostElements != null && vecCostElements.size() > 0){
//            CategoryBean defaultBean = new CategoryBean();
//            defaultBean.setCostElement(EMPTY_STRING);
//            defaultBean.setDescription(EMPTY_STRING);
//            defaultBean.setBudgetCategoryCode(0);
//            defaultBean.setCampusFlag(OFF_CAMPUS_FLAG);
//            vecCostElements.insertElementAt(defaultBean, 0);
//        }

        /* check whether period generated, if not filter budget periods for
         * default period.
         */
        boolean isPeriodGenerated = checkPeriodGenerated(vecBudgetDetails);
        if(!isPeriodGenerated) {
            vecBudgetPeriod = filterBudgetPeriod(MAKE_DEFAULT_PERIOD, vecBudgetPeriod);
        }
        String personStartDate = EMPTY_STRING;
        String personEndDate = EMPTY_STRING;
        if(vecBudgetPersonnel != null && vecBudgetPersonnel.size() > 0){           
            for(int index = 0; index < vecBudgetPersonnel.size(); index++){
                DynaActionForm dynaActionForm = (DynaActionForm)vecBudgetPersonnel.get(index);
                personStartDate = dateUtils.formatDate(dynaActionForm.get("startDate").toString(),SIMPLE_DATE_FORMAT);
                personEndDate = dateUtils.formatDate(dynaActionForm.get("endDate").toString(), SIMPLE_DATE_FORMAT); 

                dynaActionForm.set("personStartDate",personStartDate);
                dynaActionForm.set("personEndDate",personEndDate);
                // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item -start
                dynaActionForm.set("awOnOffCampusFlag", dynaActionForm.get("onOffCampusFlag"));
                dynaActionForm.set("awSubmitCostSharingFlag", dynaActionForm.get("submitCostSharingFlag"));
                // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item -end
                
                //COEUSQA-1693 - Cost Sharing Submission - start
//                String submitCSFlag = (String)dynaActionForm.get("submitCostSharingFlag");
//                if(submitCSFlag !=null && submitCSFlag.equalsIgnoreCase("Y")){
//                    dynaActionForm.set("tempSubmitCostSharingFlag",new Boolean(true));
//                }else{
//                    dynaActionForm.set("tempSubmitCostSharingFlag",new Boolean(false));
//                }
                //COEUSQA-1693 - Cost Sharing Submission - end
            }
        }
        
        Vector filteredData = new Vector();
		/* Added for Budget Personnel Cost Element without Persons Enhancement -Start     */
         if(vecPersonnelNoPersons != null && vecPersonnelNoPersons.size() > 0){           
            for(int index = 0; index < vecPersonnelNoPersons.size(); index++){
                DynaActionForm dynaPeronnelActionForm = (DynaActionForm)vecPersonnelNoPersons.get(index);
                // code added to filter datas based on budget periods
                if(dynaPeronnelActionForm.get("budgetPeriod").equals(new Integer(budgetPeriod))){
                    personStartDate = dateUtils.formatDate(dynaPeronnelActionForm.get("startDate").toString(),SIMPLE_DATE_FORMAT);
                    personEndDate = dateUtils.formatDate(dynaPeronnelActionForm.get("endDate").toString(), SIMPLE_DATE_FORMAT); 
                    dynaPeronnelActionForm.set("personStartDate",personStartDate);
                    dynaPeronnelActionForm.set("personEndDate",personEndDate);
                    filteredData.add(dynaPeronnelActionForm);
                }
            }
        }
         /* Set the Budget details data */
        session.setAttribute("personnelCEwithoutPersons", filteredData);
        
       /*  Budget Personnel Cost Element without Persons Enhancement -End   */
        
        /* set data in request/session */
        session.setAttribute("costElementData" , vecFilteredCostElement);
        session.setAttribute("BudgetPeriodData", vecBudgetPeriod );
        String page = "";
        session.setAttribute("pageConstantValue", page);

        /* Set the Budget details data */
        session.setAttribute("budgetPersonnelData", vecBudgetPersonnel);
        /* set budget detail data to session */
        session.setAttribute("BudgetDetailsData", vecBudgetDetails);
        /* set the calculated Amts to session */
        session.setAttribute("BudgetDetailCalAmts", vecBudgetDetailCalAmts);
        /* set budget period data to session */
        session.setAttribute("BudgetPeriodData", vecBudgetPeriod);
        
        /* set the budget personnel calculated Amts to session */
        session.setAttribute("BudgetPersonnelCalAmts", vecBudgetPersonnelCalAmts);
        
        /* set all dropdown values */
        session.setAttribute("budgetPersons", vecBudgetPersons);
        session.setAttribute("projectRoles", vecProjectRoles);
        session.setAttribute("periodTypes", vecPeriodTypes);
        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
        session.setAttribute("allPeriodTypes", vecBothPeriodTypes);
        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
        
        //System.out.println("*** EXIT getBudgetPersonnelFromDB ***");
    }
    //Added for Case 2918 – Use of Salary Anniversary Date for calculating inflation in budget development module-Start
    /**
     * This method is to get the Parameter Value for a particular Parameter
     * @throws Exception
     * @return String Parameter Value
     */
    private String getParameterValue(HttpServletRequest request)throws Exception{
        Map mpParameterName = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();        
        mpParameterName.put("parameterName",PARAMETER_NAME);
        Hashtable htParameterValue = 
            (Hashtable)webTxnBean.getResults(request, GET_PARAMETER_VALUE, mpParameterName );
        HashMap hmParameterValue = (HashMap)htParameterValue.get(GET_PARAMETER_VALUE);
        String strParameterValue = (String)hmParameterValue.get("parameterValue");
        return strParameterValue ;
    }
     //Added for Case 2918 – Use of Salary Anniversary Date for calculating inflation in budget development module-End
    
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
    /**
     * Fetch all the appointment types from the database
     * @return Vector vecAppointmentTypes
     */
    public Vector fetchAppointmentTypes(){
        Vector vecApointmentTypeData = new Vector();
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        HashMap hmApptData=null;
        try {
            hmApptData = budgetDataTxnBean.getActiveAppointmentTypeValues();
        } catch (CoeusException ex) {
            ex.printStackTrace();
        } catch (DBException ex) {
            ex.printStackTrace();
        }
        if(hmApptData != null && hmApptData.size()>0){
            vecApointmentTypeData = constructAppointmentTypeVector(hmApptData);
        }
        return vecApointmentTypeData;
    }
    
    /**
     * Fetch all the period types from the database
     * @return Vector vecPeriodTypeData
     */
    public Vector fetchPeriodTypes(){
        Vector vecPeriodTypeData = new Vector();
        Vector vecActivePeriodTypeData = new Vector();
        Vector vecAllPeriodTypeData = new Vector();
        CoeusVector cvActivePeriodData = new CoeusVector(); // JM 12-16-2013
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        HashMap hmActivePeriodData=null;
        HashMap hmAllPeriodData=null;
        try {
            hmAllPeriodData = budgetDataTxnBean.getPeriodTypeValues();
            // JM 12-16-2013 changing type to vector so we can order
            //hmActivePeriodData = budgetDataTxnBean.getActivePeriodTypeValues();
            cvActivePeriodData = budgetDataTxnBean.getActivePeriodTypeValues();
            // JM END
        } catch (CoeusException ex) {
            ex.printStackTrace();
        } catch (DBException ex) {
            ex.printStackTrace();
        }
        if(hmAllPeriodData != null && hmAllPeriodData.size()>0){
            vecAllPeriodTypeData = constructPeriodTypeVector(hmAllPeriodData);
            vecPeriodTypeData.add(vecAllPeriodTypeData);
        }
        /* JM 12-16-2013 changing type to vector so we can order
        if(hmActivePeriodData != null && hmActivePeriodData.size()>0){
            vecActivePeriodTypeData = constructPeriodTypeVector(hmActivePeriodData);
            vecPeriodTypeData.add(vecActivePeriodTypeData);
        }
        */
        if(cvActivePeriodData != null && cvActivePeriodData.size() > 0){
            vecActivePeriodTypeData = constructPeriodTypeVector(cvActivePeriodData);
        	vecPeriodTypeData.add(vecActivePeriodTypeData);
        }
        // JM END
        return vecPeriodTypeData;
    }
    
    /**
     * To create the appointment types in to ComboBox values
     * @return Vector vecApptData
     * @param HashMap hmData
     */
    public Vector constructAppointmentTypeVector(HashMap hmData){
        Vector vecApptData = new Vector();
        Set<Map.Entry<String, String>> setData = hmData.entrySet();
        for(Map.Entry<String,String> mapData : setData){
            ComboBoxBean cmbBean = new ComboBoxBean();
            cmbBean.setCode(mapData.getKey());
            cmbBean.setDescription(mapData.getKey());
            vecApptData.add(cmbBean);
        }
        return vecApptData;
    }
    
    // JM 12-18-2013 new method to handle sorted period types
    /**
     * To create the period types in to ComboBox values
     * @return Vector vecPeriodData
     * @param CoeusVector cvPeriodData
     */
    public Vector constructPeriodTypeVector(CoeusVector cvPeriodData){
        Vector vecPeriodData = new Vector();
        String[] periodType = new String[2];
        for(int i=0; i < cvPeriodData.size(); i++) {
        	periodType = (String[]) cvPeriodData.get(i);
            ComboBoxBean cmbBean = new ComboBoxBean();
            cmbBean.setCode(periodType[0]);
            cmbBean.setDescription(periodType[1]);
            vecPeriodData.add(cmbBean);
        }
        return vecPeriodData;
    }
    // JM END
    
    /**
     * To create the period types in to ComboBox values
     * @return Vector vecPeriodData
     * @param HashMap hmData
     */
    public Vector constructPeriodTypeVector(HashMap hmPeriodData){
        Vector vecPeriodData = new Vector();
        Set<Map.Entry<String, String>> setData = hmPeriodData.entrySet();
        for(Map.Entry<String,String> mapData : setData){
            ComboBoxBean cmbBean = new ComboBoxBean();
            cmbBean.setCode(mapData.getKey());
            cmbBean.setDescription(mapData.getValue());
            vecPeriodData.add(cmbBean);
        }
        return vecPeriodData;
    }
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End

    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
    /**
     * Method to calculate base salary for all periods
     * @param HttpServletRequest
     * @param CoeusDynaBeansList
     * @param String
     * @param int
     * @return String
     * @throws Exception if exception occur
     */
    private String calculateSalaryForPeriods(HttpServletRequest request, CoeusDynaBeansList coeusDynaBeanList,String proposalNumber, int versionNumber)throws Exception {
        String navigator = EMPTY_STRING;
        HashMap hmBaseSalForAllPeriods = new HashMap(10);
        HttpSession session = request.getSession();
        WebTxnBean  webTxnBean = new WebTxnBean();        
        Vector vecAppointmentTypes = fetchAppointmentTypes();
        //to fetch the period data
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        CoeusVector cvBudgetPeriods = budgetDataTxnBean.getBudgetPeriods(proposalNumber, versionNumber);
        if(cvBudgetPeriods==null){
            cvBudgetPeriods = new CoeusVector();
        }
        /* get budget persons */
        HashMap hmProposalBudgetParam = new HashMap();
        hmProposalBudgetParam.put("proposalNumber",proposalNumber);
        hmProposalBudgetParam.put("versionNumber", new Integer(versionNumber));
        Hashtable htBudgetPersonsData = (Hashtable)webTxnBean.getResults(request,"getPropBudgetPersonData",hmProposalBudgetParam);
        Vector vecBudgetPersons = (Vector)htBudgetPersonsData.get("getBudgetPersonsData");
        
        /* add data to dynabean list */
        List list = new ArrayList();
        if(vecBudgetPersons!= null && vecBudgetPersons.size() > 0){
            for(int index = 0; index < vecBudgetPersons.size(); index ++){
                DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)vecBudgetPersons.get(index);
                 if(vecAppointmentTypes != null && vecAppointmentTypes.size() >0){
                    for(int appCnt = 0 ;appCnt <vecAppointmentTypes.size();appCnt++){
                        edu.mit.coeuslite.utils.ComboBoxBean comboBoxBean = (edu.mit.coeuslite.utils.ComboBoxBean)vecAppointmentTypes.get(appCnt);
                        if(dynaValidatorForm.get("appointmentType") != null && dynaValidatorForm.get("appointmentType").equals(comboBoxBean.getCode())){
                            dynaValidatorForm.set("appointmentType",comboBoxBean.getDescription());
                            dynaValidatorForm.set("awAppointmentType",comboBoxBean.getDescription());
                            break;
                        }
                    }
                 }
                if(dynaValidatorForm.get("personId") != null && request.getParameter("id")!= null){
                    if(request.getParameter("id").equals(dynaValidatorForm.get("personId"))){
                        BudgetInfoBean bgtInf = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
                        BudgetCalculator budgetCalculator = new BudgetCalculator(bgtInf);
                        CoeusFunctions coeusFunctions = new CoeusFunctions();
                        String personName = (String)dynaValidatorForm.get("personName");
                        Double inflationRate = new Double(coeusFunctions.getParameterValue(DEFAULT_INFLATION_RATE_FOR_SALARY));
                        BudgetPersonsBean budgetPersonsBean = getBudgetPersonsBeanData(request.getParameter("personId"), personName, bgtInf, 
                                                dynaValidatorForm, request);
                        //copy values from CoeusVector to Vector
                        Vector vecBudgetPeriods = copyVector(cvBudgetPeriods);
                        //to calculate the base salary for all periods
                        hmBaseSalForAllPeriods = budgetCalculator.calculateBaseSalaryForAllPeriods(budgetPersonsBean, vecBudgetPeriods, inflationRate);
                        double currentPeriodSalary = 0.00;
                        for(int periodCount = 0;periodCount<hmBaseSalForAllPeriods.size();periodCount++){
                            int periodKey = periodCount+1;
                            switch(periodKey){
                                case 1:
                                    currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                                    dynaValidatorForm.set("basesalaryp1",currentPeriodSalary);
                                    break;
                                case 2:
                                    currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                                    dynaValidatorForm.set("basesalaryp2",currentPeriodSalary);
                                    break;
                                case 3:
                                    currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                                    dynaValidatorForm.set("basesalaryp3",currentPeriodSalary);
                                    break;
                                case 4:
                                    currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                                    dynaValidatorForm.set("basesalaryp4",currentPeriodSalary);
                                    break;
                                case 5:
                                    currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                                    dynaValidatorForm.set("basesalaryp5",currentPeriodSalary);
                                    break;
                                case 6:
                                    currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                                    dynaValidatorForm.set("basesalaryp6",currentPeriodSalary);
                                    break;
                                case 7:
                                    currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                                    dynaValidatorForm.set("basesalaryp7",currentPeriodSalary);
                                    break;
                                case 8:
                                    currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                                    dynaValidatorForm.set("basesalaryp8",currentPeriodSalary);
                                    break;
                                case 9:
                                    currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                                    dynaValidatorForm.set("basesalaryp9",currentPeriodSalary);
                                    break;
                                case 10:
                                    currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                                    dynaValidatorForm.set("basesalaryp10",currentPeriodSalary);
                                    break;
                            }
                        }
                    }
                }
                list.add(dynaValidatorForm);
            }
        }
        coeusDynaBeanList.setList(list);  
        String parameterValue = getParameterValue(request);
        session.setAttribute("ENABLE_SALARY_INFLATION_ANNIV_DATE",parameterValue);
        session.setAttribute("budgetPersonsDynaBean" ,coeusDynaBeanList);
        session.setAttribute("appointmentTypesData" , vecAppointmentTypes);
        session.setAttribute("budgetPeriodData" , cvBudgetPeriods);
        session.removeAttribute("DeletedBudgetPersonData");
            
        navigator = "success";
        return navigator;
    }
    
    /**
     * Method to calculate base salary for all periods
     * @param HttpServletRequest
     * @param CoeusDynaBeansList
     * @param String
     * @param int
     * @return String
     * @throws Exception if exception occur
     */
    private String calculateAllBaseSalaryForPeriods(HttpServletRequest request, CoeusDynaBeansList coeusDynaBeanList,String proposalNumber, int versionNumber)throws Exception {
        String navigator = EMPTY_STRING;
        HashMap hmBaseSalForAllPeriods = new HashMap(10);
        HttpSession session = request.getSession();
        WebTxnBean  webTxnBean = new WebTxnBean();        
        Vector vecAppointmentTypes = fetchAppointmentTypes();
        //to fetch the period data
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        CoeusVector cvBudgetPeriods = budgetDataTxnBean.getBudgetPeriods(proposalNumber, versionNumber);
        if(cvBudgetPeriods==null){
            cvBudgetPeriods = new CoeusVector();
        }
        
             
        /* get budget persons */
        HashMap hmProposalBudgetParam = new HashMap();
        hmProposalBudgetParam.put("proposalNumber",proposalNumber);
        hmProposalBudgetParam.put("versionNumber", new Integer(versionNumber));
        Hashtable htBudgetPersonsData = (Hashtable)webTxnBean.getResults(request,"getPropBudgetPersonData",hmProposalBudgetParam);
        Vector vecBudgetPersons = (Vector)htBudgetPersonsData.get("getBudgetPersonsData");
        
        /* add data to dynabean list */
        List list = new ArrayList();
        if(vecBudgetPersons!= null && vecBudgetPersons.size() > 0){
            for(int index = 0; index < vecBudgetPersons.size(); index ++){
                DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)vecBudgetPersons.get(index);
                 if(vecAppointmentTypes != null && vecAppointmentTypes.size() >0){
                    for(int appCnt = 0 ;appCnt <vecAppointmentTypes.size();appCnt++){
                        edu.mit.coeuslite.utils.ComboBoxBean comboBoxBean = (edu.mit.coeuslite.utils.ComboBoxBean)vecAppointmentTypes.get(appCnt);
                        if(dynaValidatorForm.get("appointmentType") != null && dynaValidatorForm.get("appointmentType").equals(comboBoxBean.getCode())){
                            dynaValidatorForm.set("appointmentType",comboBoxBean.getDescription());
                            dynaValidatorForm.set("awAppointmentType",comboBoxBean.getDescription());
                            break;
                        }
                    }
                 }
               // if(dynaValidatorForm.get("personId") != null && request.getParameter("id")!= null){
//                    if(request.getParameter("id").equals(dynaValidatorForm.get("personId"))){
                        BudgetInfoBean bgtInf = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
                        BudgetCalculator budgetCalculator = new BudgetCalculator(bgtInf);
                        CoeusFunctions coeusFunctions = new CoeusFunctions();
                        String personName = (String)dynaValidatorForm.get("personName");
                        Double inflationRate = new Double(coeusFunctions.getParameterValue(DEFAULT_INFLATION_RATE_FOR_SALARY));
                        BudgetPersonsBean budgetPersonsBean = getBudgetPersonsBeanData(request.getParameter("personId"), personName, bgtInf, 
                                                dynaValidatorForm, request);
                        //copy values from CoeusVector to Vector
                        Vector vecBudgetPeriods = copyVector(cvBudgetPeriods);
                        //to calculate the base salary for all periods
                        hmBaseSalForAllPeriods = budgetCalculator.calculateBaseSalaryForAllPeriods(budgetPersonsBean, vecBudgetPeriods, inflationRate);
                        double currentPeriodSalary = 0.00;
                        for(int periodCount = 0;periodCount<hmBaseSalForAllPeriods.size();periodCount++){
                            int periodKey = periodCount+1;
                            switch(periodKey){
                                case 1:
                                    currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                                    dynaValidatorForm.set("basesalaryp1",currentPeriodSalary);
                                    break;
                                case 2:
                                    currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                                    dynaValidatorForm.set("basesalaryp2",currentPeriodSalary);
                                    break;
                                case 3:
                                    currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                                    dynaValidatorForm.set("basesalaryp3",currentPeriodSalary);
                                    break;
                                case 4:
                                    currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                                    dynaValidatorForm.set("basesalaryp4",currentPeriodSalary);
                                    break;
                                case 5:
                                    currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                                    dynaValidatorForm.set("basesalaryp5",currentPeriodSalary);
                                    break;
                                case 6:
                                    currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                                    dynaValidatorForm.set("basesalaryp6",currentPeriodSalary);
                                    break;
                                case 7:
                                    currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                                    dynaValidatorForm.set("basesalaryp7",currentPeriodSalary);
                                    break;
                                case 8:
                                    currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                                    dynaValidatorForm.set("basesalaryp8",currentPeriodSalary);
                                    break;
                                case 9:
                                    currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                                    dynaValidatorForm.set("basesalaryp9",currentPeriodSalary);
                                    break;
                                case 10:
                                    currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                                    dynaValidatorForm.set("basesalaryp10",currentPeriodSalary);
                                    break;
                            }
                        }
               //     }
              //  }
                list.add(dynaValidatorForm);
            }
        }
        coeusDynaBeanList.setList(list);  
        String parameterValue = getParameterValue(request);
        session.setAttribute("ENABLE_SALARY_INFLATION_ANNIV_DATE",parameterValue);
        session.setAttribute("budgetPersonsDynaBean" ,coeusDynaBeanList);
        session.setAttribute("appointmentTypesData" , vecAppointmentTypes);
        session.setAttribute("budgetPeriodData" , cvBudgetPeriods);
        session.removeAttribute("DeletedBudgetPersonData");
            
        navigator = "success";
        return navigator;
    }
    
    /** This method will set the Proposal Persons with the Budget Persons
     * @ param personId, personName, budgetInfoBean
     * @ param DynaActionForm
     * @ throws Exception
     * @ return BudgetPersonsBean
     */
    private BudgetPersonsBean getBudgetPersonsBeanData(String personId,
    String personName,BudgetInfoBean budgetInfoBean, DynaActionForm dynaActionForm, HttpServletRequest request) throws Exception{
        CoeusVector cvBudgetPersons = new CoeusVector();
        HttpSession session = request.getSession();
        BudgetPersonsBean budgetPersonsBean = null;
        edu.mit.coeus.bean.UserInfoBean userInfoBean
        = (edu.mit.coeus.bean.UserInfoBean)session.getAttribute("user"+session.getId());
        String jobCode = (String) dynaActionForm.get("jobCode");
        String userId = userInfoBean.getUserId();
        if(budgetInfoBean != null){
            budgetPersonsBean = new BudgetPersonsBean();
            budgetPersonsBean.setProposalNumber(budgetInfoBean.getProposalNumber());
            budgetPersonsBean.setAw_ProposalNumber(budgetInfoBean.getProposalNumber());
            budgetPersonsBean.setVersionNumber(budgetInfoBean.getVersionNumber());
            budgetPersonsBean.setAw_VersionNumber(budgetInfoBean.getVersionNumber());
            budgetPersonsBean.setPersonId(personId);
            budgetPersonsBean.setAw_PersonId(personId);
            budgetPersonsBean.setFullName(personName);
            budgetPersonsBean.setJobCode(jobCode.trim());
            budgetPersonsBean.setAw_JobCode(jobCode.trim());
            budgetPersonsBean.setAppointmentType((String) dynaActionForm.get("appointmentType"));
            budgetPersonsBean.setAw_AppointmentType((String) dynaActionForm.get("appointmentType"));
            if(dynaActionForm.get("effectiveDate") != null){
                String effectiveDate = (String) dynaActionForm.get("effectiveDate");
                effectiveDate = effectiveDate.substring(0,10);
                if(effectiveDate !=null && !effectiveDate.equals("")){
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//                     SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                    java.util.Date dt = formatter.parse(effectiveDate);
                    java.sql.Date dte = new java.sql.Date(dt.getTime());
                    budgetPersonsBean.setEffectiveDate(dte);
                    budgetPersonsBean.setAw_EffectiveDate(dte);
                }                
            }
            budgetPersonsBean.setCalculationBase(Double.parseDouble(dynaActionForm.get("calculationBase") == null ? "0" : dynaActionForm.get("calculationBase").toString()));
            budgetPersonsBean.setAw_CalculationBase(Double.parseDouble(dynaActionForm.get("calculationBase") == null ? "0" : dynaActionForm.get("calculationBase").toString()));
            budgetPersonsBean.setUpdateTimestamp(prepareTimeStamp());
            budgetPersonsBean.setUpdateUser(userId);
            budgetPersonsBean.setAcType(TypeConstants.INSERT_RECORD);
            String nonEmployeeFlag = (String) dynaActionForm.get("nonEmployeeFlag");
            boolean nonEmployee =  Boolean.valueOf(nonEmployeeFlag).booleanValue();          
            budgetPersonsBean.setNonEmployee(nonEmployee);
        }
        return budgetPersonsBean;
    }
    
    /** This method will copy the budget periods from CoeusVector to Vector
     * @ param CoeusVector
     * @ throws Exception
     * @ return Vector
     */
    private Vector copyVector(CoeusVector cvBudgetPeriods){
     Vector vecBudgetPeriods = new Vector(10);
     for(Object data : cvBudgetPeriods){
         BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean) data;
         vecBudgetPeriods.add(budgetPeriodBean);
     }
     return vecBudgetPeriods;
    }
    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
}

