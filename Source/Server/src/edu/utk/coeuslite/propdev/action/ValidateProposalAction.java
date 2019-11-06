package edu.utk.coeuslite.propdev.action;


import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.SubmissionEngine;
import edu.mit.coeus.s2s.bean.S2SHeader;
import edu.mit.coeus.s2s.validator.S2SValidationException;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.wmc.coeuslite.budget.bean.ProposalBudgetHeaderBean;
import edu.wmc.coeuslite.budget.bean.ReadXMLData;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * Created Muralidharan
 * User: muralidharann
 * Date: Jul 13, 2006
 * Time: 12:37:29 PM
 */
/*
* ValidateProposalAction.java does the following actions
* 1- Gets the proposalNumber from the session and calls the function checkValidationProposal()
* 2- checkValidationProposal() accepts two parameters DynaValidatorForm and proposal Number
* 3- In checkValidationProposal(), UserId and Unit number are obtained from UserBean
* 4- checkValidationProposal calls ProposalTxnBean's proposalValidation() function with input parameters
*    as proposalNumber, userId and unit number
* 5- ProposalTxnBean's proposalValidation() calls a PLSQL function which validates the proposal number and response
*    is sent back as a Vector object.
* 6- The response Vector object is agin processed in getDynaData() function which returns a dynaVector that is ready
*    for process in presentation layer in jsp
*/
public class ValidateProposalAction extends ProposalBaseAction{

    private static final String PROPOSAL_NUMBER                 = "proposalNumber";
    private static final String PROPOSAL_RULES_MAP                 = "PROPOSAL_RULES_MAP";
    private static final String SUCCESS                         = "0000";
    private static final String USER                            = "user";
    private static final String BUDGET                          = "B";
    private static final String BUDGET_VERSIONS                 = "BV";
    private static final String BUDGET_SUMMARY                  = "BS";
    //Added for COEUSDEV-858 Add a validation button to proposal summary in lite (approval screens) for business rules validations - start
    private static final String VALIDATION_CHECKS               = "VC";
    public static final String PROPOSAL_MENU_ITEMS              = "proposalMenuItemsVector";
    private static final String XML_MENU_PATH                   = "/edu/utk/coeuslite/propdev/xml/ProposalApprovalMenu.xml";
    public static final String ENABLE_PROP_PERSON_SELF_CERTIFY = "ENABLE_PROP_PERSON_SELF_CERTIFY";
    //Added for COEUSDEV-858 Add a validation button to proposal summary in lite (approval screens) for business rules validations - end
    /** Creates a new instance of ValidateProposalAction */
    public ValidateProposalAction() {
    }

     /** Filled up the necessary details in performExecute() */
    public ActionForward performExecute(ActionMapping actionMapping,
                                        ActionForm actionForm,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {

        HttpSession session            = request.getSession();
        String proposalNumber   = null;
        DynaValidatorForm  dynaValidatorForm =(DynaValidatorForm)actionForm;
        proposalNumber  =(String) session.getAttribute(PROPOSAL_NUMBER+session.getId());
        session.removeAttribute("SUBMIT_ACTION");
        //Added with Case 2158: Budgetary Validations
        try{
            String page = request.getParameter("PAGE");
            int budgetVersion = 0;
            //Resolve the budget version and validate
            //Budget version will be passed as the module item key sequence.
            
            if(BUDGET_VERSIONS.equalsIgnoreCase(page)){
                //This is called from budget vesions screen on status change to complete:Version no currently selected as final comes as a paramter
                String versionParam = (String)request.getParameter("versionNumber");
                if(versionParam!=null && !EMPTY_STRING.equals(versionParam.trim())){
                    budgetVersion = Integer.parseInt(versionParam);
                }
                //COEUSDEV-159 Lite - cost share warning -won't allow complete status in lite
//                Vector validationChecks = checkValidationBudget(proposalNumber, budgetVersion,dynaValidatorForm, session);
                Vector validationChecks = checkValidationBudget(proposalNumber, budgetVersion,dynaValidatorForm, request);
                //COEUSDEV-159: End
                session.setAttribute(PROPOSAL_RULES_MAP, validationChecks);
                if(validationChecks==null || validationChecks.size()==0){
                    //If there are no errors/warnings do not show the validation checks screen.
                    String url =  "/getBudgetVersions.do?PAGE=V&actionFrom=BudgetValidation&statusChange=C&versionNumber="+versionParam;
                    RequestDispatcher rd = request.getRequestDispatcher(url);
                    rd.forward(request,response);
                    return null;
                }
            }else if(BUDGET_SUMMARY.equalsIgnoreCase(page)){
                //This is called from budget summary screen on status change to complete:Validate on the budget this is called from.
                ProposalBudgetHeaderBean  headerBean = (ProposalBudgetHeaderBean)session.getAttribute("ProposalBudgetHeaderBean");
                budgetVersion = headerBean.getVersionNumber();
                //COEUSDEV-159 Lite - cost share warning -won't allow complete status in lite
//                Vector validationChecks = checkValidationBudget(proposalNumber, budgetVersion,dynaValidatorForm, session);
                Vector validationChecks = checkValidationBudget(proposalNumber, budgetVersion,dynaValidatorForm, request);
                //COEUSDEV-159: End
                session.setAttribute(PROPOSAL_RULES_MAP, validationChecks);
                if(validationChecks==null || validationChecks.size()==0){
                    //If there are no errors/warnings do not show the validation checks screen.
                    String url =  "/getBudgetSummary.do?proposalNumber="+proposalNumber+"&statusChange=C&versionNumber="+budgetVersion;
                    RequestDispatcher rd = request.getRequestDispatcher(url);
                    rd.forward(request,response);
                    return null;
                }
            }else if(BUDGET.equalsIgnoreCase(page)){
                //This is called from individual budget details screen- Validate on the budget this is called from.
                ProposalBudgetHeaderBean  headerBean = (ProposalBudgetHeaderBean)session.getAttribute("ProposalBudgetHeaderBean");        
                budgetVersion = headerBean.getVersionNumber();
                //COEUSDEV-159 Lite - cost share warning -won't allow complete status in lite
//                session.setAttribute(PROPOSAL_RULES_MAP, checkValidationBudget(proposalNumber, budgetVersion,dynaValidatorForm, session));
                session.setAttribute(PROPOSAL_RULES_MAP, checkValidationBudget(proposalNumber, budgetVersion,dynaValidatorForm, request));
                //COEUSDEV-159:End
            }else{
                //This is called from proposal - Validate on Final Version
                budgetVersion = getBudgetVersionToValidate(proposalNumber);
                if(budgetVersion== 1000){//no budget to validate
                    CoeusMessageResourcesBean messageResource = new CoeusMessageResourcesBean();
                    request.setAttribute("WarningMessage",messageResource.parseMessageKey(
                            "validationChecks_exceptionCode.1901",new String[] {proposalNumber}));
                    budgetVersion = 0;
                }else if(budgetVersion== 1001){//no budget selected final
                    CoeusMessageResourcesBean messageResource = new CoeusMessageResourcesBean();
                    session.setAttribute(PROPOSAL_RULES_MAP,new Vector());
                    request.setAttribute("NewFinalVersion","0");
                    throw new CoeusException(messageResource.parseMessageKey("validationChecks_exceptionCode.1900"));
                }
                session.setAttribute(PROPOSAL_RULES_MAP, checkValidationProposal(proposalNumber, budgetVersion,dynaValidatorForm, session));
                /* Moved Grants gov validations inside proposal validations with
                    COEUSQA-2695: 4.4.2 Error when running Budget Validations - Start */
                //Grants Gov validation - START
                Object ggExist = request.getSession().getAttribute("grantsGovExist");
                boolean s2sCandidate = false;
                if(ggExist != null && ggExist.toString().trim().equals("1")) {
                    s2sCandidate = true;
                }
                if(s2sCandidate) {
                    SubmissionEngine submissionEngine = SubmissionEngine.getInstance();
                    S2SHeader s2SHeader = new S2SHeader();
                    HashMap hashMap = new HashMap();
                    hashMap.put("PROPOSAL_NUMBER", proposalNumber);
                    s2SHeader.setStreamParams(hashMap);
                    s2SHeader.setSubmissionTitle(proposalNumber);
                    
                    WebTxnBean webTxnBean = new WebTxnBean();
                    Map map = new HashMap();
                    map.put("proposalNumber", proposalNumber);
                    Hashtable result = (Hashtable)webTxnBean.getResults(request, "getProposalSummaryDetails", map);
                    List lstPropSummary = (List)result.get("getProposalSummaryDetails");
                    DynaActionForm propSummaryForm  = (DynaActionForm)lstPropSummary.get(0);
                    String cdfa = (String)propSummaryForm.get("cfdaCode");
                    if(cdfa != null && cdfa.length() == 5){
                        //Put a period after 2nd character
                        cdfa = cdfa.substring(0, 2) + "." + cdfa.substring(2);
                    }
                    s2SHeader.setCfdaNumber(cdfa);
                    
                    String oppId = (String)propSummaryForm.get("programAnnouncementNumber");
                    s2SHeader.setOpportunityId(oppId);
                    
                    try{
                        submissionEngine.validateData(s2SHeader);
                    }catch (S2SValidationException s2SValidationException) {
                        request.setAttribute("Exception", s2SValidationException);
                    }
                }//End if S2S Candidate
                //Grants Gov validation - END
                // COEUSQA-2695 : 4.4.2 Error when running Budget Validations - End
            }
            //budget version resolution end
            request.setAttribute("VALIDATION_FROM",page);
            request.setAttribute("NewFinalVersion",String.valueOf(budgetVersion));
            //2158 End
        //Added with Case 2158: Budgetary Validations
        }catch(CoeusException coeusException){
            request.setAttribute("Exception",coeusException);
        }
        //2158 End
        //Added for COEUSDEV-858 Add a validation button to proposal summary in lite (approval screens) for business rules validations - start
        //if selected page is validation checks then get the menu details for it
        String page = request.getParameter("PAGE");
        if(VALIDATION_CHECKS.equalsIgnoreCase(page)){
            //checking for "certification" menu in left nav  
               String pPCFlag = null;
               pPCFlag = fetchParameterValue(request,ENABLE_PROP_PERSON_SELF_CERTIFY);
               if(pPCFlag!=null && pPCFlag.equalsIgnoreCase("1")){
                   if(getProposalPersonCertifyrightsForUser(proposalNumber,request)){
                         request.setAttribute("enableSelfCertication",true);
                    }                
               }
            //checking for "certification" menu in left nav 
            HashMap hmMap = getApprovalRights(request, Integer.toString(ModuleConstants.PROPOSAL_DEV_MODULE_CODE), proposalNumber, "0");
            getApproveProposalMenuDetails(request,hmMap);
        }else{
            Map mapMenuList = new HashMap();
            mapMenuList.put("menuItems",CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
            mapMenuList.put("menuCode",CoeusliteMenuItems.VALIDATE_CODE);
            setSelectedMenuList(request, mapMenuList);
        }
        //Added for COEUSDEV-858 Add a validation button to proposal summary in lite (approval screens) for business rules validations - end
//         setSelectedStatusMenu(VALIDATE_CODE);
        return actionMapping.findForward("success");
    }
    
    
    //Added for COEUSDEV-858 Add a validation button to proposal summary in lite (approval screens) for business rules validations - start
    /**
     * This method set the selected menu list for VALIDATION_CHECKS
     * @param hmApprovalRights
     * @throws Exception
     */
    public void getApproveProposalMenuDetails(HttpServletRequest request,HashMap hmApprovalRights)throws Exception {
        HttpSession session = request.getSession();
        String avViewRouting =(String) hmApprovalRights.get(VIEW_ROUTING);
        String avPropWait =(String) hmApprovalRights.get(PROPWAIT);
        ReadXMLData readXMLData = new ReadXMLData();
        Vector proposalMenuItemsVector = readXMLData.readXMLDataForMenu(XML_MENU_PATH);
        Vector modifiedVector = new Vector();
        if(proposalMenuItemsVector!= null && proposalMenuItemsVector.size() > 0){
            for (int index=0; index<proposalMenuItemsVector.size();index++) {
                MenuBean meanuBean = (MenuBean)proposalMenuItemsVector.get(index);
                String menuId = meanuBean.getMenuId();
                meanuBean.setVisible(true);
                if(CoeusliteMenuItems.APPROVAL_ROUTING_PROPOSAL.equals(menuId)
                && (avViewRouting!= null && (CoeusliteMenuItems.ROUTING_STATUS).equals(avViewRouting))) {
                    meanuBean.setVisible(false);
                }
                if((CoeusliteMenuItems.APPROVE).equals(menuId) && (avPropWait!= null && (CoeusliteMenuItems.ROUTING_STATUS).equals(avPropWait))) {
                    meanuBean.setVisible(false);
                }
                if(avPropWait!= null && (CoeusliteMenuItems.ROUTING_STATUS).equals(avPropWait) && (CoeusliteMenuItems.REJECT).equals(menuId)) {
                    meanuBean.setVisible(false);
                }
                modifiedVector.add(meanuBean);
            }
        }
        session.setAttribute(PROPOSAL_MENU_ITEMS, modifiedVector);
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",PROPOSAL_MENU_ITEMS);
        mapMenuList.put("menuCode",CoeusliteMenuItems.VALIDATION_CHECKS);
        setSelectedMenuList(request, mapMenuList);
    }
    //Added for COEUSDEV-858 Add a validation button to proposal summary in lite (approval screens) for business rules validations - end
}

