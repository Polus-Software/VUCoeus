/*
 * CopyProposalAction.java
 *
 * Created on 22 August 2006, 14:24
 */

/*
 * PMD check performed, and commented unused imports and variables on 27-JAN-2012
 * by Bharati
 */

package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.budget.bean.BudgetDetailBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.CostElementsBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentUpdateTxnBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean;
import edu.mit.coeus.s2s.bean.S2SSubmissionDataTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.dbengine.DBException;
//import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author  mohann
 */
public class CopyProposalAction extends ProposalBaseAction {
    private static final String GET_COPY_PROPOSAL_DATA="/getCopyProposal";
    private static final String COPY_PROPOSAL_DETAILS="/copyProposalDetails";
    private static final String CREATE_RIGHT="CREATE_PROPOSAL";
    private static final String YES = "YES";
    private static final String NO = "NO";
    private static final String GET_UNITS_FOR_USER_RIGHT  = "getUnitsForUserRight"; 
    private static final String GET_BUDGET_FINAL_VERSION_DETAIL  = "getBudgetFinalVersionDetail";
    private static final String PROPOSAL_NUMBER = "proposalNumber";
    private static final String UNIT_NUMBER = "unitNumber";
    private static final String SELECTED_VERSION = "selectedVersion";
    private static final String BUDGET_FINAL_VERSION_EXIST  = "budgetFinalVersionExist";
    private static final String HAS_BUDGET  = "strHasBudget";
    private static final String HAS_NARRATIVE   = "strHasNarrative";
    private static final String HAS_QNR   = "strHasQnr";
    private static final String NARRATIVE = "narrative";
    private static final String RIGHT_ID  = "rightId";
    private static final String USER_ID  = "userId";
    private static final String SUCCESS = "success";
    private static final String USER = "user";
    // COEUSQA-2321: Copy Questionnaires for Proposal Development records
    private static final String QUESTIONNAIRE = "questionnaire";
    private static final String INVALID_QUESTIONNAIRE = "invalidQuestionnaire";
    
    /** Creates a new instance of CopyProposalAction */
    public CopyProposalAction() {
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
        HttpSession session = request.getSession();
        DynaValidatorForm dynaForm = (DynaValidatorForm)actionForm;
        HashMap hmRequiredDetails = new HashMap();
        dynaForm.set("budgetVersionFlag","A");
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
        request.setAttribute("budgetVersionFlag","A");
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
        hmRequiredDetails.put(ActionMapping.class,actionMapping);
        hmRequiredDetails.put(DynaValidatorForm.class,dynaForm);
        ActionForward actionForward = getCopyProposalData(response,request,actionMapping,hmRequiredDetails);
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
        mapMenuList.put("menuCode",CoeusliteMenuItems.COPY_PROPOSAL_CODE);
        setSelectedMenuList(request, mapMenuList);
        return actionForward;
    }
    /** This method will identify which request is comes from which path and
     *  navigates to the respective ActionForward
     *  @returns ActionForward object
     */
    private ActionForward getCopyProposalData(HttpServletResponse response,HttpServletRequest request,ActionMapping actionMapping,HashMap hmRequiredDetails)throws Exception{
        
        String navigator = EMPTY_STRING;
        if(actionMapping.getPath().equals(GET_COPY_PROPOSAL_DATA)){
            navigator = getCopyProposalDetails(request);
           
        } else if(actionMapping.getPath().equals(COPY_PROPOSAL_DETAILS)){
            // COEUSQA-2321: Copy Questionnaires for Proposal Development records - Start
//            copyProposalDetails(response,request);
//            navigator = SUCCESS; 
            navigator = copyProposalDetails(response,request);
            // COEUSQA-2321: Copy Questionnaires for Proposal Development records - End
        }
        ActionForward actionForward = actionMapping.findForward(navigator);
        return actionForward;
    }
    /**
     * This method is used to check whether the Budget and Narrative information of the Proposal can be copied.
     * @throws Exception
     * @return navigator
     */
    private String getCopyProposalDetails(HttpServletRequest request)throws Exception{
        String navigator = SUCCESS;
        HttpSession session = request.getSession();
        String proposalNumber = (String)session.getAttribute(PROPOSAL_NUMBER+session.getId());
        String loggedinUser = ((UserInfoBean)session.getAttribute(USER+session.getId())).getUserId();
         //Modified for case#2903 - Modify all dev proposals should allow you to copy proposal - start
        String unitNumber = (String)session.getAttribute("unitNumber");
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        boolean rightExist = userMaintDataTxnBean.getUserHasRight(loggedinUser,"MODIFY_ANY_PROPOSAL",unitNumber);        
        ProposalDevelopmentTxnBean  proposalDataTxnBean = new ProposalDevelopmentTxnBean();
        String strHasBudget = NO;
        String strHasNarrative = NO;
        if(proposalDataTxnBean.isPropBudgetCopyAllowed(proposalNumber,loggedinUser, rightExist)) {
            strHasBudget = YES;
        }
        if(proposalDataTxnBean.isPropNarrativeCopyAllowed(proposalNumber,loggedinUser, rightExist)) {
            strHasNarrative = YES;
        }  //Modified for case#2903 - Modify all dev proposals should allow you to copy proposal - End
        // COEUSQA-2321: Copy Questionnaires for Proposal Development records - Start
        String strHasQnr = NO;
        QuestionnaireTxnBean questionnaireTxnBean = new QuestionnaireTxnBean();
        if(questionnaireTxnBean.checkAnyQuestionIsAnsweredInModule(3, proposalNumber, 0, "0")){
            strHasQnr =  YES;
        }
        boolean canCopyQnr = questionnaireTxnBean.checkCanCopyQuestionnaireFromModule(3, 0, proposalNumber, "0");
        request.setAttribute("canCopyQnr", Boolean.valueOf(canCopyQnr));
        // COEUSQA-2321: Copy Questionnaires for Proposal Development records - End
        
        //Added for COEUSQA-3509 : Add warning message to Copy proposal window - start
        //if proposal is grants gov. proposal then set the ggproposal value as true.
        S2SSubmissionDataTxnBean s2SSubmissionDataTxnBean = new S2SSubmissionDataTxnBean();
        boolean strGGProposal = false;
        if(s2SSubmissionDataTxnBean.isS2SCandidate(proposalNumber)){
            strGGProposal = true;
        }
        request.setAttribute("gg_Proposal", Boolean.valueOf(strGGProposal));
        //Added for COEUSQA-3509 : Add warning message to Copy proposal window - end
        
        //Commented and added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
        //BudgetDataTxnBean txnBean = new BudgetDataTxnBean();
        String budgetFlag = (String) request.getAttribute("budgetVersionFlag");
        //get the budget data for proposal and get the cost element details for 
        //CoeusVector cvData = txnBean.getBudgetForProposal(proposalNumber);
        //if it returns true then allow to save         
        //boolean  allow_to_copy = getProposalData(cvData,budgetFlag);
        
        //Get the inactive type details for the proposal budget
        String budgetFinalFlag = "F";
        String budgetAllVersionFlag = "A";
        int inactivTypeForBudFInalFlag = isInactiveATAndPTExist(proposalNumber,budgetFinalFlag);
        String msgKeyForFinalFlag = Integer.toString(inactivTypeForBudFInalFlag);
        request.setAttribute("inactiveType_For_Final_Flag", msgKeyForFinalFlag);
        
        int inactivTypeForBudFAllFlag = isInactiveATAndPTExist(proposalNumber,budgetAllVersionFlag);
        String msgKeyForAllFlag = Integer.toString(inactivTypeForBudFAllFlag);
        request.setAttribute("inactiveType_For_All_version_Flag", msgKeyForAllFlag);
        
        //request.setAttribute("allow_to_copy", new Boolean(allow_to_copy));
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
        //Commented and added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget -  end
        getUserUnitDetails(request);
        String finalVersion = getBudgetFinalVersion(request,proposalNumber);
        request.setAttribute(BUDGET_FINAL_VERSION_EXIST,finalVersion);
        request.setAttribute(HAS_BUDGET, strHasBudget);
        request.setAttribute(HAS_NARRATIVE, strHasNarrative);
        // COEUSQA-2321: Copy Questionnaires for Proposal Development records
        request.setAttribute(HAS_QNR, strHasQnr);
        return navigator;
    }
    /**
     * This method is used to copy all the details of the given Proposal data.
     * @throws Exception
     * @return navigator
     */
    private String copyProposalDetails(HttpServletResponse response,HttpServletRequest request)throws Exception{        
        HttpSession session = request.getSession();
        String proposalNumber = (String)session.getAttribute(PROPOSAL_NUMBER+session.getId());        
        String unitNumber =  request.getParameter(UNIT_NUMBER);
        String selectedVersion =  request.getParameter(SELECTED_VERSION);
        String narrative =  request.getParameter(NARRATIVE);
        String loggedinUser 	= ((UserInfoBean)session.getAttribute(USER+session.getId())).getUserId();
        // COEUSQA-2321: Copy Questionnaires for Proposal Development records - Start
        String questionnaire =  request.getParameter(QUESTIONNAIRE);
        //String invalidQuestionnaire =  request.getParameter(INVALID_QUESTIONNAIRE);
        if(questionnaire != null && 'Y' == questionnaire.charAt(0)){
            QuestionnaireTxnBean questionnaireTxnBean = new QuestionnaireTxnBean();
            boolean canCopy = questionnaireTxnBean.checkCanCopyQuestionnaireFromModule(3, 0, proposalNumber, "0");
             if(!canCopy){
                questionnaire = "N";
            }
        } 
        // COEUSQA-2321: Copy Questionnaires for Proposal Development records - End
        
        ProposalDevelopmentTxnBean proposalDataTxnBean = new ProposalDevelopmentTxnBean();
        String targetProposalNumber =  proposalDataTxnBean.getNextProposalNumber();       
        char budgetFlag='N';   
        char narrativeFlag='N';
        char questionnaireFlag='N';
        // COEUSQA-2321: Copy Questionnaires for Proposal Development records
        if(selectedVersion !=null && !selectedVersion.equals(EMPTY_STRING)){
            budgetFlag = selectedVersion.charAt(0);
        }
        if(questionnaire !=null && !questionnaire.equals(EMPTY_STRING)){
            questionnaireFlag = questionnaire.charAt(0);
        }
        
        if(narrative !=null && !narrative.equals(EMPTY_STRING)){
            narrativeFlag = narrative.charAt(0);
        }
        if((proposalNumber!=null && !proposalNumber.equals(EMPTY_STRING)) &&
        (targetProposalNumber!=null && !targetProposalNumber.equals(EMPTY_STRING)) &&
        (unitNumber!=null && !unitNumber.equals(EMPTY_STRING))){
            ProposalDevelopmentUpdateTxnBean  proposalUpdTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
            // COEUSQA-2321: Copy Questionnaires for Proposal Development records
//            proposalUpdTxnBean.copyProposal(proposalNumber, targetProposalNumber, budgetFlag, narrativeFlag, unitNumber, loggedinUser);
            proposalUpdTxnBean.copyProposal(proposalNumber, targetProposalNumber, budgetFlag, narrativeFlag, questionnaireFlag, unitNumber, loggedinUser);
            
        }
        String url =  "/getGeneralInfo.do?action=notExist&proposalNumber=";
        url += targetProposalNumber+"&Menu_Id=003";// modified by Noor
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request,response);
        return null ;
    }
    
    
    /** Get the user units for the logged in user and get the department names for the user
     *  @throws Exception
     */
    private void getUserUnitDetails(HttpServletRequest request)throws Exception{
        HashMap hmData = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        hmData.put(USER_ID,userInfoBean.getUserId());
        hmData.put(RIGHT_ID,CREATE_RIGHT);
        boolean isCreateProposalRightExists = hasCreateProposalRights(hmData);
        if(isCreateProposalRightExists){
            Hashtable htUnitsData = (Hashtable)webTxnBean.getResults(request, GET_UNITS_FOR_USER_RIGHT, hmData);
            Vector vecUserUnits = (Vector)htUnitsData.get(GET_UNITS_FOR_USER_RIGHT);
            request.setAttribute("vecUserUnits", vecUserUnits);
        }
    }
    /**
     * This method is invoked when the user clicks create new proposal
     * Checks whether the User has rights to Create Proposal
     * @return boolean. true indicates has rights and false indicates no rights.
     */
    private boolean hasCreateProposalRights(HashMap hmData) throws Exception{
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        boolean isRightExists = userMaintDataTxnBean.getUserHasRightInAnyUnit(
        (String)hmData.get(USER_ID), (String)hmData.get(RIGHT_ID));
        userMaintDataTxnBean = null;
        return isRightExists;
    }
    
    /**
     * This method get the Budget Status details for a particular proposal number
     * @param proposalNumber
     * @throws Exception
     */
    private String getBudgetFinalVersion(HttpServletRequest request,String proposalNumber) throws Exception{    
        HashMap hmPropData = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        hmPropData.put(PROPOSAL_NUMBER,proposalNumber);
        Hashtable htBudgetStatusDetail =
        (Hashtable)webTxnBean.getResults(request, GET_BUDGET_FINAL_VERSION_DETAIL,hmPropData);
        String finalVersion =EMPTY_STRING;
        if(htBudgetStatusDetail !=null && htBudgetStatusDetail.size()>0){
            HashMap hmFinalVersionExists = (HashMap)htBudgetStatusDetail.get(GET_BUDGET_FINAL_VERSION_DETAIL);
            finalVersion = (String) hmFinalVersionExists.get("ll_version");
        }
        
        return finalVersion;
    }
    
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
    /**
     * This method get the cost element details for the proposal budget
     * @param cvData
     * @param budgetFlag
     * @returns boolean value
     */
    private boolean getProposalData(CoeusVector cvData, String budgetFlag){
        Vector inActiveCE = new Vector();
        if(cvData != null && cvData.size() > 0){
            BudgetInfoBean budgetBean = null;
            BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
            BudgetDetailBean budgetDetailBean = new BudgetDetailBean();
            CostElementsBean costElementsBean = new CostElementsBean();
            boolean isFinalFlag = false;
            Vector budgetData = new Vector();
            Vector vecCostElements = new Vector();
            //Iterate the vector cvData to get the all versions budget data
            for ( int count = 0; count < cvData.size(); count ++ ){
                budgetBean = ( BudgetInfoBean ) cvData.get(count);
                int versionNumber = budgetBean.getVersionNumber();
                String proposalNumber = budgetBean.getProposalNumber();
                isFinalFlag = budgetBean.isFinalVersion();
                try {
                    budgetData = budgetDataTxnBean.getBudgetDetail(proposalNumber, versionNumber);
                } catch (CoeusException ex) {
                    ex.printStackTrace();
                } catch (DBException ex) {
                    ex.printStackTrace();
                }
                if(budgetData!=null && budgetData.size()>0){
                    for (int index = 0; index < budgetData.size(); index ++ ){
                        budgetDetailBean = (BudgetDetailBean) budgetData.get(index);
                        vecCostElements.add(budgetDetailBean.getCostElement());
                    }
                }
                //remove duplicate cost elements from the vector vecCostElements
                for(int index=0; index<vecCostElements.size(); index++) {
                    //costElementIndex Returns the index of the last occurrence of the specified object from the vector vecCostElements.
                    int costElementIndex = vecCostElements.lastIndexOf(vecCostElements.get(index));
                    //if bothe index and costElementIndex holding the same value then remove the costelement form the vector
                    if(costElementIndex != index) {
                        vecCostElements.remove(costElementIndex);
                        index=index-1;
                    }
                }
                //If status of cost element is 'N' then add to vector inActivecostElements
                if(vecCostElements!=null && vecCostElements.size()>0){
                    for(int index = 0 ; index <vecCostElements.size() ; index++){
                        String costElement = (String) vecCostElements.get(index);
                        try {
                            costElementsBean = budgetDataTxnBean.getCostElementsDetails(costElement);
                        } catch (CoeusException ex) {
                            ex.printStackTrace();
                        } catch (DBException ex) {
                            ex.printStackTrace();
                        }
                        if("N".equals(costElementsBean.getActive())){
                            inActiveCE.addElement(costElementsBean.getActive());
                            inActiveCE.addElement(costElement);
                        }
                    }
                }
                //Iteration of vector cvData gives all the versions of budget
                //If selected budget version is final then break it
                if("F".equals(budgetFlag) && isFinalFlag){
                    break;
                }
            }
        }
        //If vector inActiveCE is null means there is no inactive cost elements for the budget
        if(inActiveCE!=null && inActiveCE.size()>0){
            return true;
        }
        return false;
    }
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
    
    //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
    /**
     * This method gives the inactiveType details for the proposal budget
     * @param proposalNumber
     * @param budgetFlag
     */
    private int isInactiveATAndPTExist(String proposalNumber , String budgetFlag){
        Vector budgetData = new Vector();
        Vector vecCostElements = new Vector();
        int inactiveType = 0;
        boolean proposalCopy = true;
        char finalFlag = '\0';
        if(budgetFlag != null && (budgetFlag.length() >0) ){
            finalFlag = budgetFlag.charAt(0);
        }
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        try {
            inactiveType = budgetDataTxnBean.isBudgetHasInactiveATAndPT(proposalNumber, finalFlag);
        } catch (DBException ex) {
            ex.printStackTrace();
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
        return inactiveType;
    }
    //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end
    
}
