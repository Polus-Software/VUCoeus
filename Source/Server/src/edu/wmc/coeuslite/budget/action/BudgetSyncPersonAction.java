/*
 * BudgetSyncPersonAction.java
 *
 * Created on 18 July 2006, 20:51
 */

package edu.wmc.coeuslite.budget.action;

import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetPersonsBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.mit.coeuslite.utils.CoeusDynaBeansList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 *
 * @author  mohann
 */
public class BudgetSyncPersonAction extends BudgetBaseAction {
//    private ActionForward actionForward = null;
//    private HttpServletRequest request;
//    private HttpServletResponse response;
//    private ActionMapping actionMapping;
//    private WebTxnBean webTxnBean ;
//    private ActionForm actionForm;
//    private HttpSession session;
    private static final String UPDATE_BUDGET_SYNC = "/updateBudgetSyncPerson";
    
    /** Creates a new instance of BudgetSyncPersonAction */
    public BudgetSyncPersonAction() {
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
    HttpServletRequest request, HttpServletResponse response) throws Exception {
//        this.actionMapping  = actionMapping;
//        this.request = request;
//        this.response =response;
//        this.session = request.getSession();
//        this.actionForm = actionForm;
        HashMap hmRequiredDetails = new HashMap();
        hmRequiredDetails.put(ActionMapping.class,actionMapping);
        hmRequiredDetails.put(ActionForm.class, actionForm);
        ActionForward actionForward = getSelectedBudgetSyncPerson(hmRequiredDetails, request); 
        return actionForward;
    }
    
    /**
     * This method will identify which request is comes from which path and
     * navigates to the respective ActionForward
     * @returns ActionForward object
     * @param hmRequiredDetails contain action form and action mapping
     * @throws Exception if any error occurs
     * @return forward to respective navigator
     */
    private ActionForward getSelectedBudgetSyncPerson(HashMap hmRequiredDetails,HttpServletRequest request)throws Exception{
        String navigator = EMPTY_STRING;
        //webTxnBean = new WebTxnBean();
        ActionMapping actionMapping = (ActionMapping)hmRequiredDetails.get(ActionMapping.class);
        if(actionMapping.getPath().equals(UPDATE_BUDGET_SYNC)){
            navigator = updateBudgetSyncPerson(hmRequiredDetails, request);
        }
        ActionForward actionForward = actionMapping.findForward(navigator);
        return actionForward;
    }
    
    /**
     * This method is use to update all the budget sync persons details
     * @param HashMap hmRequiredDetails
     * @throws Exception
     * @return navigator
     */
    
    private String updateBudgetSyncPerson(HashMap hmRequiredDetails, HttpServletRequest request) throws Exception{
        DynaActionForm dynaActionForm = null;
        String personId = EMPTY_STRING;
        String personName = EMPTY_STRING;
        CoeusVector cvSyncPersonInfo = null;
        CoeusVector cvSyncPersons = new CoeusVector();
        HttpSession session = request.getSession();
        CoeusDynaBeansList coeusSyncPersonsDynaList = (CoeusDynaBeansList) hmRequiredDetails.get(ActionForm.class);
        List syncList = (List)coeusSyncPersonsDynaList.getList();
        String navigator = (String) request.getParameter("syncNavigator");
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        if(syncList !=null && syncList.size() > 0 ){
            for(int index=0; index < syncList.size() ; index++){
                dynaActionForm = (DynaActionForm) syncList.get(index);
                personId = (String) dynaActionForm.get("personId");
                personName = (String)dynaActionForm.get("fullName");
                // Included Rolodex Budget Persons Enchancement 
                if(personName == null || personName.equals(EMPTY_STRING)){
                    personName = getPersonFullName(personId, request);
                }
                cvSyncPersonInfo = getBudgetPersonsBeanData(personId, personName, budgetInfoBean,dynaActionForm, request);
                cvSyncPersons.addAll(cvSyncPersonInfo);
            }
            session.setAttribute("syncBudgetPersonDetails", cvSyncPersons);
        }
        
        return navigator;
    }
    
    /** This method will set the Proposal Persons with the Budget Persons
     * @ param personId, personName, budgetInfoBean
     * @ param DynaActionForm
     * @ throws Exception
     * @ return CoeusVector
     */
    private CoeusVector getBudgetPersonsBeanData(String personId,
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
            budgetPersonsBean.setEffectiveDate(budgetInfoBean.getStartDate());
            budgetPersonsBean.setAw_EffectiveDate(budgetInfoBean.getStartDate());
            budgetPersonsBean.setCalculationBase(Double.parseDouble(dynaActionForm.get("salary") == null ? "0" : dynaActionForm.get("salary").toString()));
            budgetPersonsBean.setAw_CalculationBase(Double.parseDouble(dynaActionForm.get("salary") == null ? "0" : dynaActionForm.get("salary").toString()));
            budgetPersonsBean.setUpdateTimestamp(prepareTimeStamp());
            budgetPersonsBean.setUpdateUser(userId);
            budgetPersonsBean.setAcType(TypeConstants.INSERT_RECORD);
            String nonEmployeeFlag = (String) dynaActionForm.get("nonEmployeeFlag");
            boolean nonEmployee =  Boolean.valueOf(nonEmployeeFlag).booleanValue();
            //Included Rolodex Budget Persons Enhancement 
            budgetPersonsBean.setNonEmployee(nonEmployee);
          //  budgetPersonsBean.setAw_nonEmployeeFlag()
            //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module -Start
            if(dynaActionForm.get("salaryAnniversaryDate") != null){
                String salAnnivDate = (String) dynaActionForm.get("salaryAnniversaryDate");
                if(salAnnivDate !=null && !salAnnivDate.equals("")){
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");  //yyyy-MM-dd
                    java.util.Date d1 = (java.util.Date) df.parse(salAnnivDate);
                    java.sql.Date d2 = new java.sql.Date(d1.getTime());
                    budgetPersonsBean.setSalaryAnniversaryDate(d2);
                }                
            }            
            //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module -End
            
            cvBudgetPersons.addElement(budgetPersonsBean);
        }
        return cvBudgetPersons;
    }
    
    /**
     * Get the person full name for corresponding person id
     * @param personId
     * @throws Exception
     * @return fullPersonName
     */
    private String getPersonFullName(String personId, HttpServletRequest request) throws Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmPersonData = new HashMap();
        String personName = EMPTY_STRING;
        hmPersonData.put("personId",personId);
        Hashtable htPersonData = (Hashtable) webTxnBean.getResults(request, "getPersonName", hmPersonData);
        if(htPersonData !=null && htPersonData.size()>0){
            personName = (String)((HashMap) htPersonData.get("getPersonName")).get("ls_name");
        }
        return personName;
    }
    
    
}
