/*
 * ProposalGetAction.java
 *
 * Created on April 17, 2006, 12:27 PM
 */

package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.validator.OpportunitySchemaParser;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.SearchModuleBean;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
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
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean;
import java.util.Map;

/**
 *
 * @author  vinayks
 */
public class ProposalGetAction extends ProposalBaseAction{
 
    private static final String GET_GENERAL_INFO_DATA="/getGeneralInfo";
    private static final String GET_UNIT_DETAILS ="/getUnitDetails";
    //Stores the RIGHT ID which is required for checking user rights to create a new proposal
    private static final String CREATE_RIGHT="CREATE_PROPOSAL";
    //Removing instance variable case# 2960
//    private DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
    private static final String AC_TYPE_UPDATE = "U";
    private static final String DEFAULT_CREATION_STATUS_DESC = "In Progress";
    private static final String MODIFY_ANY_PROPOSAL_RIGHT = "MODIFY_ANY_PROPOSAL";
     //Stores the RIGHT ID which is required for checking user rights to modify proposal
    private static final String MODIFY_PROPOSAL_RIGHT="MODIFY_PROPOSAL";
     //Stores the RIGHT ID which is required for checking user rights to modify narrative
    private static final String MODIFY_NARRATIVE_RIGHT="MODIFY_NARRATIVE";
      //Stores the RIGHT ID which is required for checking user rights to modify budget
    private static final String MODIFY_BUDGET_RIGHT="MODIFY_BUDGET";
    
    private static final String VIEW_ANY_PROPOSAL_RIGHT="VIEW_ANY_PROPOSAL";
    //Stores the RIGHT ID which is required for checking user rights to view proposal
    private final String VIEW_RIGHT="VIEW_PROPOSAL";
     // Menu codes
    private static final String GENERAL_INFO_CODE = "P001";
    // Submit for Approval Mode
    //Removing instance variable case# 2960
//    private static String strMode;
    
    private static final String PROPOSAL_SUB_HEADER="proposalSubHeader";
     
    //COEUSQA:3446 - Disable YNQ icon & menu-path in Proposal Development if all Proposal YNQ's are marked Inactive - Start
    private static final String QUESTION_TYPE = "questionType";
    private static final String GROUP_NAME = "groupName";
    private static final String PROPOSAL = "P";
    //COEUSQA:3446 - End 
    
    
    
    /** Creates a new instance of ProposalGetAction */
    public ProposalGetAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm, 
        HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        //Code addded for Case#2785 - Protocol Routing
        session.setAttribute("moduleCode"+session.getId(), "3");
        //releaseBudgetLock(request);
        DynaValidatorForm dynaForm = (DynaValidatorForm)actionForm;       
        HashMap hmRequiredDetails = new HashMap();
        hmRequiredDetails.put(ActionMapping.class,actionMapping);
        hmRequiredDetails.put(DynaValidatorForm.class,dynaForm);
        
        if(request.getParameter("Menu_Id") != null) {
            String coeusHeaderId =  request.getParameter("Menu_Id");
            if(coeusHeaderId!=null) {
                setSelectedCoeusHeaderPath(coeusHeaderId, request);
            }
        }
        //JIRA COEUSDEV 61 - START
        //Check if User has Create Proposal Right and check if this schema exists in coeus.
        String schemaUrl = request.getParameter("schemaUrl");
        String proposalNumber = request.getParameter("proposalNumber");  
        if (schemaUrl != null) {
            String user = null;
            UserInfoBean userInfoBean = (UserInfoBean) request.getSession().getAttribute("user" + request.getSession().getId());
            user = userInfoBean.getUserId();
            UserMaintDataTxnBean usrTxn = new UserMaintDataTxnBean();
            boolean isRightExists = usrTxn.getUserHasRightInAnyUnit(user, "CREATE_PROPOSAL");
            request.setAttribute("CREATE_PROPOSAL_RIGHT", new Boolean(isRightExists));
            if (isRightExists) {
                OpportunitySchemaParser schemeParser = new OpportunitySchemaParser();
//                try{
                schemeParser.checkFormsAvailable(proposalNumber,schemaUrl);
                request.getSession().setAttribute("oppNum", request.getParameter("oppNum"));
                request.getSession().setAttribute("competitionId", request.getParameter("compId"));
//                }catch(CoeusException ce){
//                    if (ce.getMessage() != null && ce.getMessage().equals("exceptionCode.90013")) {
//                        //This proposal is not eligible for Coeus transmission
//                        CoeusMessageResourcesBean resourceBean = new CoeusMessageResourcesBean();
//                        String message = resourceBean.parseMessageKey(ce.getMessage());
//                        request.setAttribute("Exception", message);
//                        ActionForward actionForward = new ActionForward("/opportunitySearchAction.do?action=searchPage");
//                        return actionForward;
//                    } else {
//                        throw ce;
//                    }
//                }
            }
        }else {
            //Set Session values to dyna Form
            dynaForm.set("programAnnouncementNumber", request.getSession().getAttribute("oppNum"));
            dynaForm.set("cfdaCode", request.getSession().getAttribute("cfdaNumber"));
            request.getSession().removeAttribute("oppNum");
            request.getSession().removeAttribute("cfdaNumber");
        }
        //JIRA COEUSDEV 61 - END
        ActionForward actionForward = getProposalData(hmRequiredDetails, request);
        if(proposalNumber!= null){
            UserInfoBean userInfoBean = (UserInfoBean) request.getSession().getAttribute("user" + request.getSession().getId());
            LockBean lockBean = getRoutingLockingBean(userInfoBean,proposalNumber, request);
            releaseLock(lockBean, request);
        }
        getProposalMenus(request);
        String subHeaderId =  request.getParameter("SUBHEADER_ID");
        Vector headerData = (Vector) session.getAttribute(PROPOSAL_SUB_HEADER);
        if(subHeaderId!=null) {
           headerData = readSelectedPath(subHeaderId, headerData);
           session.setAttribute(PROPOSAL_SUB_HEADER, headerData);
        }         
        enableDisableMenus(request);
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
        mapMenuList.put("menuCode",CoeusliteMenuItems.GENERAL_INFO_CODE); 
        setSelectedMenuList(request, mapMenuList);
        readSavedStatus(request);
//        setSelectedStatusMenu(GENERAL_INFO_CODE);           
        if(request.getParameter("page")!=null && !request.getParameter("page").equals(EMPTY_STRING)){
          actionForward = actionMapping.findForward("success"); 
        }
        //COEUSQA-1433 - Allow Recall for Routing - Start
        //to release lock if the proposal is in approval in progress
        EPSProposalHeaderBean proposalHeaderBean=(EPSProposalHeaderBean)session.getAttribute("epsProposalHeaderBean");
        String statusCode = EMPTY_STRING;
        if(proposalHeaderBean!=null){
             statusCode = proposalHeaderBean.getProposalStatusCode();
        }
        if("2".equals(statusCode)){
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            proposalNumber = request.getAttribute("proposalNumber").toString();
            LockBean lockBean = getLockingBean(userInfoBean, proposalNumber, request);
            releaseLock(lockBean, request);
//            session.removeAttribute("proposalLock");
        }
        //COEUSQA-1433 - Allow Recall for Routing - End
        return actionForward;
    }
   
    
      
    /** This method will identify which request is comes from which path and 
     *navigates to the respective ActionForward
     *@returns ActionForward object
     */
    private ActionForward getProposalData(HashMap hmRequiredDetails,
        HttpServletRequest request)throws Exception{
        
        String navigator = EMPTY_STRING;
        WebTxnBean webTxnBean = new WebTxnBean();
        ActionForward actionForward = null;
        ActionMapping actionMapping = (ActionMapping)hmRequiredDetails.get(ActionMapping.class);
         if(actionMapping.getPath().equals(GET_GENERAL_INFO_DATA)){
           navigator = getGeneralInfoProposal(hmRequiredDetails,null,null,request);
            actionForward = actionMapping.findForward(navigator);
        }else if(actionMapping.getPath().equals(GET_UNIT_DETAILS)){
             navigator = getUserUnitDetails(hmRequiredDetails, request);
             actionForward = actionMapping.findForward(navigator);
        }
        return actionForward;
    }
    /** Get the user units for the logged in user and get the department names
     *for the user
     */
    private String getUserUnitDetails(HashMap hmRequiredDetails, HttpServletRequest request)throws Exception{
        ActionMessages actionMessages = new ActionMessages();
        HashMap hmData = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        String navigator = EMPTY_STRING;
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        hmData.put("userId",userInfoBean.getUserId());
        hmData.put("rightId",CREATE_RIGHT);
        session.setAttribute("grantsGovExist", "0");
        boolean isCreateProposalRightExists = hasCreateProposalRights(hmData);
        if(isCreateProposalRightExists){
            Hashtable htUnitsData = (Hashtable)webTxnBean.getResults(request, "getUnitsForUserRight", hmData);
            Vector vecUserUnits = (Vector)htUnitsData.get("getUnitsForUserRight");
            if(vecUserUnits!=null && vecUserUnits.size() > 0){
                //Release the old lock if it exists  - Start
                String oldProposalNumber = (String)session.getAttribute("proposalNumber"+session.getId());
                if(oldProposalNumber!= null){
                    LockBean lockBean = getLockingBean(userInfoBean, oldProposalNumber,request);
                    releaseLock(lockBean, request);
                    session.removeAttribute(CoeusLiteConstants.PROPOSAL_SEARCH_ACTION+session.getId());
                    session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(), 
                            new Boolean(false));
                   // session.removeAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());
                    //JIRA COEUSQA-3705 - START
                    session.removeAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
                    //JIRA COEUSQA-3705 - END
                }
//                //Release the old lock if it exists  - End
                if(vecUserUnits.size() == 1){
                    DynaValidatorForm userUnitDynaForm = (DynaValidatorForm)vecUserUnits.get(0);
                    String unitNumber = (String)userUnitDynaForm.get("unitNumber");
                    String unitName = (String)userUnitDynaForm.get("unitName");
                    DynaValidatorForm dynaForm = (DynaValidatorForm)hmRequiredDetails.get(DynaValidatorForm.class);
                    dynaForm.set("unitNumber",unitNumber);
                    dynaForm.set("unitName",unitName);
                    getGeneralInfoProposal(hmRequiredDetails,unitNumber,unitName,request);
                    navigator = "generalInfo";
                    return navigator;
                }else{
                    request.setAttribute("userUnits", vecUserUnits);
                    navigator = "unitDetails";
                    return navigator;
                }
            }
            
            
        }else{
            actionMessages.add( "creationProposalRightRequired" , new ActionMessage(
            "proposal.creationProposalRightRequired") );
            saveMessages(request, actionMessages);
            navigator = "success";
            return navigator;
        }
        
        return navigator;
    }
    
    private String getGeneralInfoProposal(HashMap hmRequiredDetails,
        String unitNumber, String unitName, HttpServletRequest request)throws Exception{  
            
            String navigator = "success";
            ActionMessages actionMessages = new ActionMessages();
            PersonInfoBean personInfoBean = null;
            UserDetailsBean userDetailsBean = new UserDetailsBean();
            HttpSession session = request.getSession();
            WebTxnBean webTxnBean = new WebTxnBean();
            // Added for Case 2162  - adding Award Type - Start 
            session.getAttribute("proposalLock");
            session.removeAttribute("proposalAwardTypes");
            // Added for Case 2162  - adding Award Type - End
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            String userName = EMPTY_STRING ;
            if (userInfoBean != null && userInfoBean.getUserId()!=null){
                userName = userInfoBean.getUserId(); 
                 userInfoBean    = userDetailsBean.getUserInfo(userName.trim());
                //String strFullName = userInfoBean.getUserName();
                //String strUserId = userInfoBean.getUserId();
                //Case 3742 - START
                personInfoBean = (PersonInfoBean)session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
                //personInfoBean  = userDetailsBean.getPersonInfo(userInfoBean.getPersonId(),false);
                //Case 3742 - END
                String loggedinUser = personInfoBean.getFullName();
                session.setAttribute("createUser",loggedinUser);   
                session.setAttribute("InvUser",loggedinUser);   
            }
                String proposalNumber = request.getParameter("proposalNumber");  
                if(proposalNumber == null || "".equals(proposalNumber)){
                    proposalNumber = (String)session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
                }
                /*
                 * commented because, currently there is no need for S2SSubmissionType checking 
                //COEUSQA-3951
                        if(checkS2SSubmissionType(proposalNumber,request)){
                            request.setAttribute("isS2SSubTypIs_3","true");
                        }
                //COEUSQA-3951         
                 */
                DynaValidatorForm dynaForm =
                (DynaValidatorForm)hmRequiredDetails.get(DynaValidatorForm.class);
                HashMap hmHeaderData = new HashMap();
                hmHeaderData.put("proposalNumber" , proposalNumber);
                hmRequiredDetails.put("proposalNumber",proposalNumber);            
                //This transactions populates combobox values.
                Hashtable htProposalDetail =
                (Hashtable)webTxnBean.getResults(request, "getProposalDetails",null);
                Vector vecProposalTypes =
                (Vector)htProposalDetail.get("getProposalTypeList");
                Vector vecActivityTypes =
                (Vector)htProposalDetail.get("getActivityType");
                Vector vecNop =
                (Vector)htProposalDetail.get("getNoticeOfOpportunity");
                
                // Added for Case 2162  - adding Award Type - Start 
                Hashtable htAwardTypes = (Hashtable)webTxnBean.getResults(request , "getPropAwardTypes" , null );
                Vector vecAwrdType =(Vector)htAwardTypes.get("getPropAwardTypes");
                // Added for Case 2162  - adding Award Type - Start 
            
                /** To  get the header details for  a existing proposal
                 */
                Hashtable htPropHeader = (Hashtable)webTxnBean.getResults(request , "getProposalHeaderData" , hmHeaderData );
                Vector vecProposalHeader = (Vector)htPropHeader.get("getProposalHeaderData");
                if(proposalNumber!= null && !proposalNumber.equals(EMPTY_STRING)){
                    if(vecProposalHeader!=null && vecProposalHeader.size()>0) {
                        session.setAttribute("epsProposalHeaderBean",(EPSProposalHeaderBean)vecProposalHeader.get(0));
                    }
                }else{
                    /**while creating a new proposal,the bean is removed from session
                     */
                    session.removeAttribute("epsProposalHeaderBean");
                    session.removeAttribute("proposalBudgetStatus");
                    session.removeAttribute("proposalNarrativeStatus");
                }

                // Get the NSF Codes.
                HashMap hmNsfCode = new HashMap();
                Hashtable htNSFCodes =
                (Hashtable)webTxnBean.getResults(request, "getNSFCodes",hmNsfCode);
                Vector vecNSFCodes= (Vector)htNSFCodes.get("getNSFCodes");
                session.setAttribute("optionsNSFCodes",vecNSFCodes);

                 //Check S2SAttrMatch
                if(proposalNumber !=null && !proposalNumber.equals(EMPTY_STRING)) {
                    String grants= getS2SAttrMatch(proposalNumber, request);
                    session.setAttribute("grantsGovExist", "0");
                    if(grants !=null && grants.equals("1")) {
                        session.setAttribute("grantsGovExist", grants);
                    }                    
                }

                session.setAttribute("proposalType", vecProposalTypes);
                session.setAttribute("activityType", vecActivityTypes);
                session.setAttribute("noticeOfOppr", vecNop);
                // Added for Case 2162  - adding Award Type - Start 
                session.setAttribute("proposalAwardTypes",vecAwrdType);
                // Added for Case 2162  - adding Award Type - End
    //            session.setAttribute("proposalNumber"+session.getId(), proposalNumber);
                session.setAttribute("proposalNumber"+session.getId(), proposalNumber);
                String unitNumParam = EMPTY_STRING;
                String unitNameParam = EMPTY_STRING;
                                // By default set the value as "" for the selected Proposal
                session.setAttribute("mode"+session.getId(),EMPTY_STRING);
                String creationStatDesc =EMPTY_STRING ;
                
                //COEUSQA:3446 - Disable YNQ icon & menu-path in Proposal Development if all Proposal YNQ's are marked Inactive - Start
                HashMap hmProposalQuestions = new HashMap();
                hmProposalQuestions.put(QUESTION_TYPE, PROPOSAL);
                hmProposalQuestions.put(CoeusLiteConstants.PROPOSAL_NUMBER, proposalNumber);
                Hashtable htProposalQuestions = (Hashtable) webTxnBean.getResults(request,
                        "getDevPropYnqQuestions", hmProposalQuestions);
                HashMap hmDevPropHasYnqData = (HashMap)htProposalQuestions.get("getDevPropYnqQuestions");
                int propYnqCount = Integer.parseInt((String)hmDevPropHasYnqData.get("hasYnqData"));

                if(propYnqCount == 0) {
                    HashMap hmGroupQuestions = new HashMap();                    
                    hmGroupQuestions.put("proposalNumber", proposalNumber);
                    Hashtable htGroupQuestions = (Hashtable) webTxnBean.getResults(request,
                                                        "getTabAnswers", hmGroupQuestions);
                    Vector ynqAns = (Vector)htGroupQuestions.get("getTabAnswers");

                    if(ynqAns != null) {
                        propYnqCount = ynqAns.size();
                    }
                }
                session.setAttribute("propYnqCount", propYnqCount);
                //COEUSQA:3446 - End

                //While getting a proposal details for a particular proposal
                if(proposalNumber!=null && !proposalNumber.equals(EMPTY_STRING)){
                    //start - 28/12/2006
                    // added for proposals which are "in Hierarchy" to be displayed in "Display Mode"
                    EPSProposalHeaderBean proposalHeaderBean=(EPSProposalHeaderBean)session.getAttribute("epsProposalHeaderBean");
                    String propInHierarchy = proposalHeaderBean.getIsHierarchy();
                    if(propInHierarchy.equals("Y")){
                        session.setAttribute("mode"+session.getId(),"display");
                        //Added for dispalying proposal hierarchy image
                        session.setAttribute("propInHierarchy", "Y");
                        String isParent = proposalHeaderBean.getIsParent();
                        if(isParent.equals("Y")){
                            session.setAttribute("proposalIsParent", "Y");
                        }else{
                            session.setAttribute("proposalIsParent", "N");
                        }
                    }else{
                        session.removeAttribute("propInHierarchy");
                        session.removeAttribute("proposalIsParent");
                    }
                    //End - 30/01/2007
                    //end - 28/12/2006
                    Vector vecProposalSummary = getProposalSummaryDetails(proposalNumber, request);
                    if(vecProposalSummary!=null && vecProposalSummary.size() >0 ){
                        DynaValidatorForm dynaProposalForm = (DynaValidatorForm)vecProposalSummary.get(0); 
                          String createUser = (String)dynaProposalForm.get("createUser");
                          String createUserFullName = getUserName(request,createUser);
                          String budgetStatusCode = (String)dynaProposalForm.get("budgetStatus");
                          //Added/Modified for case#2776 - Allow concurrent Prop dev access in Lite - start
                          session.removeAttribute("createBudget"+session.getId());
                          if(budgetStatusCode == null || budgetStatusCode.equals(EMPTY_STRING)){
//                              request.setAttribute("createBudget", "createBudget");
                              session.setAttribute("createBudget"+session.getId(), "createBudget");
                          }
                          //Added/Modified for case#2776 - Allow concurrent Prop dev access in Lite - end
                          session.setAttribute("proposalBudgetStatus", budgetStatusCode);
                          String narrativeStatusCode = (String)dynaProposalForm.get("narrativeStatus");
                          session.setAttribute("proposalNarrativeStatus", narrativeStatusCode);
                          session.setAttribute("createUser",createUserFullName);

                          String updUser = (String)dynaProposalForm.get("updateUser");
                          String updUserFullName = getUserName(request,updUser);
                          session.setAttribute("updUser",updUserFullName);     
                          // Check whether proposal can be editable or not based on creation status code
                          String creationStatCode = (String)dynaProposalForm.get("creationStatusCode");
                          int creationStatusCode =Integer.parseInt(creationStatCode);
                          //Added for COEUSQA-2697 Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-Start
                          session.setAttribute("proposalStatus",creationStatusCode);
                          //Added for COEUSQA-2697 Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-End
                        if(hasModifyProposalRights(dynaProposalForm,session)){
                            dynaProposalForm = prepareDynaProposalForm(dynaProposalForm, hmRequiredDetails, request);
//                            // Check whether proposal can be editable or not based on creation status code
//                            String creationStatCode = (String)dynaProposalForm.get("creationStatusCode");
//                            int creationStatusCode =Integer.parseInt(creationStatCode);
//                            //Added for COEUSQA-2697 Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-Start
//                            session.setAttribute("proposalStatus",creationStatusCode);
//                            //Added for COEUSQA-2697 Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-End
                            switch (creationStatusCode) {

                                case PROPOSAL_APPROVAL_IN_PROGRESS:
//                                    actionMessages.add("proposalApprovalinProgress",
//                                    new ActionMessage("proposal_DtlForm_exceptionCode.7105"));
//                                    saveMessages(request, actionMessages);                                    
                                    session.setAttribute("mode"+session.getId(),"display");
                                    navigator ="success";
                                    break;
                                case PROPOSAL_APPROVED:
//                                    actionMessages.add("proposalApproved",
//                                    new ActionMessage("proposal_DtlForm_exceptionCode.7106"));
//                                    saveMessages(request, actionMessages);                                    
                                    session.setAttribute("mode"+session.getId(),"display");
                                    navigator ="success";
                                    break;
                                case PROPOSAL_SUBMITTED:
//                                    actionMessages.add("proposalSubmitted",
//                                    new ActionMessage("proposal_DtlForm_exceptionCode.7107"));
//                                    saveMessages(request, actionMessages);                                   
                                    session.setAttribute("mode"+session.getId(),"display");
                                    navigator ="success";
                                    break;

                                case PROPOSAL_POST_SUB_APPROVAL:
//                                    actionMessages.add("proposalSubPostApproval",
//                                    new ActionMessage("proposal_DtlForm_exceptionCode.7114"));
//                                    saveMessages(request, actionMessages);                                   
                                    session.setAttribute("mode"+session.getId(),"display");
                                    navigator ="success";
                                    break;

                                case PROPOSAL_POST_SUB_REJECTION:
//                                    actionMessages.add("proposalRejection",
//                                    new ActionMessage("proposal_DtlForm_exceptionCode.7115"));
//                                    saveMessages(request, actionMessages);                                    
                                    session.setAttribute("mode"+session.getId(),"display");
                                    navigator ="success";
                                    break;
                            }
                            // session.setAttribute("mode","editable");
                        }//End if hasModifyProposalRights
                        else{
                            //Check for display proposal rights
                            dynaProposalForm = prepareDynaProposalForm(dynaProposalForm, hmRequiredDetails, request);
                            boolean hasDisplayRights = hasDisplayProposalRights(dynaProposalForm,session);
                            //Checking the loggedinUser is the Principal Investigator of the proposal
                            //If the loggedinUser doesnot have modify and Display rights, If he is the PI of the proposal, open the proposal in Display mode.
                            String principalInvName = proposalHeaderBean.getPersonName();
                            String loggedUser = (String) session.getAttribute("InvUser");
                            /*
                             *Bug fix by Geo on 09-06-2007
                             */
                            //BEGIN 
//                            if(!hasDisplayRights  && ( principalInvName !=null && loggedUser !=null && !principalInvName.equals(loggedUser))){
                            if(!hasDisplayRights  && ( loggedUser !=null && !loggedUser.equals(principalInvName))){
                                //END
                                actionMessages.add("noDisplayAndEditRights",
                                new ActionMessage("proposal_DtlForm_no_displayEditRights"));
                                saveMessages(request, actionMessages);
                                navigator ="statusException";
                                session.setAttribute("mode"+session.getId(),"noRights");
                                //return navigator;

                            }else{
//                                actionMessages.add("noDisplayRights",
//                                new ActionMessage("proposal_DtlForm_no_displayRights"));
//                                saveMessages(request, actionMessages);
                                navigator ="success";
                                session.setAttribute("mode"+session.getId(),"display");
                                //return navigator;

                            }
                        }//End Else

                    }//End if vecProposalSummary!=null
                    session.removeAttribute("InvUser");
                    String isEditable = request.getParameter("isEditable");
                    String action = request.getParameter("action");
                    if(isEditable!=null && !isEditable.equals(EMPTY_STRING)){
                        
                        if(isEditable.equals("display")){
                            if(action!=null ){
                                if(action.equals("noView")){
                                    actionMessages.add( "noModifyAndViewRights" , new ActionMessage(
                                    "error.noModifyAndViewRights") );
                                    saveMessages(request, actionMessages);
                                }else if(action.equals("notExist")) {
                                    actionMessages.add( "budgetDoesnotExist" , new ActionMessage(
                                    "error.budgetDoesnotExist") );
                                    saveMessages(request, actionMessages);
                                }
                            }
                            navigator = "success" ;
                        }
                    }
                    String mode = (String) session.getAttribute("mode"+session.getId());
                    mode = (mode==null)?EMPTY_STRING : mode;
                    if(!mode.equals("noRights")){
                        boolean isValid = prepareLock(userInfoBean,proposalNumber,request);
                        if(!isValid){
                            session.setAttribute("mode"+session.getId(),"display");
                        }                        
                    } else {
                        SearchModuleBean moduleBean = (SearchModuleBean)session.getAttribute(CoeusLiteConstants.PROPOSAL_SEARCH_ACTION+session.getId());
                        // If the action is from search window
                        if(moduleBean!= null && !moduleBean.getOldModuleNumber().equals(EMPTY_STRING)){
                            if(!moduleBean.getModuleNumber().equals(moduleBean.getOldModuleNumber())){
                                // If the existing proposal number is not in DISPLAY MODE, release the lcok
                                if(!moduleBean.getOldMode().equals(CoeusLiteConstants.DISPLAY_MODE)){
                                    LockBean lockBean = getLockingBean(userInfoBean, moduleBean.getOldModuleNumber(), request);
                                    releaseLock(lockBean,request);
                                    session.removeAttribute(CoeusLiteConstants.PROPOSAL_SEARCH_ACTION+session.getId());
                                    session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(), 
                                            new Boolean(false));
                                }
                            }
                        }
                    }         
                    
                    return navigator;
                }//End if

           // this block of code is while creating a new proposal. 
           dynaForm.set("acType","I");
           creationStatDesc = DEFAULT_CREATION_STATUS_DESC;
           session.setAttribute("creationStatDesc",creationStatDesc);
           webTxnBean = new WebTxnBean();
         //  UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user");
           unitNumParam = request.getParameter("unitNumber");
           unitNameParam = request.getParameter("unitName");     

           // Get the values from the sever.
           if((unitNumParam!=null && !unitNumParam.equals(EMPTY_STRING)) 
                    &&(unitNameParam!=null && !unitNameParam.equals(EMPTY_STRING))){
                   session.setAttribute("unitNumber", unitNumParam);
                   dynaForm.set("unitNumber",unitNumParam);
                   dynaForm.set("unitName",unitNameParam);               
                   session.setAttribute("unitName", unitNameParam);

           }else{
               dynaForm.set("unitNumber",unitNumber);
               dynaForm.set("unitName",unitName);
               session.setAttribute("unitNumber", unitNumber);
               session.setAttribute("unitName", unitName);
           }
           //set general Info selected status code for proposal menu.
           navigator = "success"; 
           session.removeAttribute("createTimestamp");
           session.removeAttribute("mode"+session.getId());
           
                    
       return navigator;
    }   
    
    /**
     * This method is invoked when the user clicks create new proposal 
     * Checks whether the User has rights to Create Proposal
     * @return boolean. true indicates has rights and false indicates no rights.
     */
    private boolean hasCreateProposalRights(HashMap hmData) throws Exception{
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        boolean isRightExists = userMaintDataTxnBean.getUserHasRightInAnyUnit(
                            (String)hmData.get("userId"), (String)hmData.get("rightId"));
        userMaintDataTxnBean = null;
        return isRightExists;
    }
    
    /**
     *This method get the proposal details for a particular proposal number 
     */
    private Vector getProposalSummaryDetails(String proposalNumber, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean(); 
        HashMap hmPropData = new HashMap();
        hmPropData.put("proposalNumber",proposalNumber);
        Hashtable htProposalDetail = 
            (Hashtable)webTxnBean.getResults(request, "getProposalSummaryDetails",hmPropData);
        Vector vecProposalSummary = 
            (Vector)htProposalDetail.get("getProposalSummaryDetails");
        return vecProposalSummary;
    }
    
     /**
     *This method returns unit name for particular unit number
     */
    private String getUnitName(String unitNumber, HttpServletRequest request) throws Exception{
        HashMap hmUnitNumber = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        String unitDesc = "";
        hmUnitNumber.put("ownedUnit",unitNumber);
        Hashtable htUnitDesc = 
            (Hashtable)webTxnBean.getResults(request,"getUnitDesc",hmUnitNumber);
        HashMap hmUnitDesc = (HashMap)htUnitDesc.get("getUnitDesc");
        if(hmUnitDesc!= null && hmUnitDesc.size() > 0){
            unitDesc = (String)hmUnitDesc.get("RetVal");
        }
        return unitDesc;
    }
    
  
   /**
    *This method checks whether the user has rights to modify a particular proposal
    *returns true if the user has modify rights
    */
  private boolean hasModifyProposalRights(DynaValidatorForm dynaValidatorForm,
        HttpSession session)throws Exception{
        boolean hasRights = false ;
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId().toUpperCase();
        String unitNumber = (String)dynaValidatorForm.get("unitNumber");
        String proposalNumber = (String)dynaValidatorForm.get("proposalNumber");
        
        //check if user has MODIFY_ANY_PROPOSAL right at the lead unit of the Proposal.      
        hasRights = userMaintDataTxnBean.getUserHasRight(userId,MODIFY_ANY_PROPOSAL_RIGHT,unitNumber);
        
        //Check if the User has MODIFY_PROPOSAL right for the particular proposal. 
        if(!hasRights){            
            hasRights =  userMaintDataTxnBean.getUserHasProposalRight
                                    (userId, proposalNumber, MODIFY_PROPOSAL_RIGHT);
        }
        
         // check if the User has MODIFY_NARRATIVE for this particular proposal 
        if(!hasRights){
           hasRights =  userMaintDataTxnBean.getUserHasProposalRight
                                (userId, proposalNumber, MODIFY_NARRATIVE_RIGHT);  
        }
        
         // check if the User has MODIFY_BUDGET for this particular proposal 
        if(!hasRights){
           hasRights =  userMaintDataTxnBean.getUserHasProposalRight
                                    (userId, proposalNumber, MODIFY_BUDGET_RIGHT);  
        }
        return hasRights ;
      
  }
  
  
   /**
    *This method checks whether the user has rights to view a particular proposal
    *returns true if the user has view rights
    */
  private boolean hasDisplayProposalRights(DynaValidatorForm dynaValidatorForm,
        HttpSession session)throws Exception{
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId().toUpperCase();
        String unitNumber = (String)dynaValidatorForm.get("unitNumber");
        String proposalNumber = (String)dynaValidatorForm.get("proposalNumber");
        String statusCode = (String)dynaValidatorForm.get("creationStatusCode");
        boolean hasRights = false ;
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        UserDetailsBean userDetailsBean = new UserDetailsBean();
        int rightExists  = userDetailsBean.getUserHasAnyOSPRights(userId);        
        if (rightExists == 1) {
            if(statusCode.equals("2") || statusCode.equals("4") || statusCode.equals("5")
            || statusCode.equals("6") || statusCode.equals("7")) {
                hasRights = true;
            }
        }
          //Check if user has VIEW_ANY_PROPOSAL_RIGHT at the lead unit of the Proposal.  
         if(!hasRights){
            hasRights = userMaintDataTxnBean.getUserHasRight(userId,VIEW_ANY_PROPOSAL_RIGHT,unitNumber);  
         }
          //Check if the User has VIEW_RIGHT for the particular proposal. 
        if(!hasRights){
            hasRights =  userMaintDataTxnBean.getUserHasProposalRight(userId, proposalNumber, VIEW_RIGHT);
        }
        return hasRights;
  }
  
   /**
    *This method sets the values to the form dynafield properties    
    */
  private DynaValidatorForm prepareDynaProposalForm(DynaValidatorForm dynaValidatorForm ,
  HashMap hmRequiredDetails, HttpServletRequest request)throws Exception{
      DynaValidatorForm dynaForm = (DynaValidatorForm)hmRequiredDetails.get(DynaValidatorForm.class);
      BeanUtilsBean copyBean = new BeanUtilsBean();
      HttpSession session = request.getSession();
      String startDate = (String)dynaValidatorForm.get("startDate");
      String endDate = (String)dynaValidatorForm.get("endDate");
      String deadLineDate = (String)dynaValidatorForm.get("deadLineDate");
      String updateTimestamp = (String)dynaValidatorForm.get("updateTimestamp");
      String createTimestamp = (String)dynaValidatorForm.get("createTimestamp");
      String creationStatDesc = (String)dynaValidatorForm.get("creationStatusDesc");      
      //Modified for instance variable case#2960.
      DateUtils dateUtils = new DateUtils();     
      
      if(startDate != null){
          String value = 
            dateUtils.formatDate(startDate, DateUtils.MM_DD_YYYY);
            dynaValidatorForm.set("startDate",value);
      }
      if(endDate!=null){
          String value = 
            dateUtils.formatDate(endDate, DateUtils.MM_DD_YYYY);
            dynaValidatorForm.set("endDate",value);
      }
      
      if(deadLineDate!=null){
          String value = 
            dateUtils.formatDate(deadLineDate, DateUtils.MM_DD_YYYY);
            dynaValidatorForm.set("deadLineDate",value);
      }else{
          dynaValidatorForm.set("deadLineDate",EMPTY_STRING);
      }
      String unitNumParam = (String)dynaValidatorForm.get("unitNumber");
      String unitNameParam = getUnitName(unitNumParam, request);
      dynaValidatorForm.set("unitName",unitNameParam);
      dynaValidatorForm.set("acType",AC_TYPE_UPDATE);
      
      String sponsorCode = (String)dynaValidatorForm.get("sponsorCode");
      String sponsorName = getSponsorDetails(request , sponsorCode);
      dynaValidatorForm.set("sponsorName",sponsorName);
      
      String primeSponsorCode = (String)dynaValidatorForm.get("primeSponsorCode");
      String primeSponsorName = getSponsorDetails(request , primeSponsorCode);
      dynaValidatorForm.set("primeSponsorName",primeSponsorName);
      
      String proposalNumber = (String)dynaValidatorForm.get("proposalNumber");
      
      copyBean.copyProperties(dynaForm,dynaValidatorForm);
      session.setAttribute("unitNumber", unitNumParam);
      session.setAttribute("unitName", unitNameParam);
      request.setAttribute("updateTimestamp",updateTimestamp);
      session.setAttribute("createTimestamp",createTimestamp);
      session.setAttribute("creationStatDesc",creationStatDesc);
      request.setAttribute("proposalNumber", proposalNumber);
      return dynaForm ;
  }
  
   /** 
     * To set the selected status for the Proposal Menus
     */
    private void setSelectedStatusMenu(String menuCode, HttpServletRequest request){
        String submitForApproval = "P012";
        Vector menuItemsVector  = null;
        HttpSession session = request.getSession();
        menuItemsVector=(Vector)session.getAttribute("proposalMenuItemsVector");
        Vector modifiedVector = new Vector();
        for (int index=0; index<menuItemsVector.size();index++) {
            MenuBean meanuBean = (MenuBean)menuItemsVector.get(index);
            String menuId = meanuBean.getMenuId();
            if (menuId.equals(menuCode)) {
                meanuBean.setSelected(true);
            } else {
                if(getMode(session).equalsIgnoreCase("display") && menuId.equals(submitForApproval)){
                    meanuBean.setVisible(false);
                } else if(menuId.equals(submitForApproval) && (!meanuBean.isVisible())){
                        meanuBean.setVisible(true);    
                }
                meanuBean.setSelected(false);
            }
            modifiedVector.add(meanuBean);
        }
        setMode("");
        session.setAttribute("proposalMenuItemsVector", modifiedVector);
    } 
    
    /* Getter and setter methods for mode
     *
     */
    private String getMode (HttpSession session){
        //Modified for instance variable case#2960.
        String strMode;        
        strMode = (String)session.getAttribute("mode"+session.getId());
        strMode = (strMode == null)?"":strMode;
        return strMode;
    }

    private void  setMode (String strMode){
        strMode = strMode;
    }

    /** This method will notify for the acquiring and releasing the Lock
     *based on the way the locks are opened.
     *It will check whether the proposal is opened through search or list
     *Based on the conditions it will acquire the lock and release the lock
     *If it locked then it will prepare the locking messages
     *@param UserInfoBean, ProposalNumber(Current)
     *@throws Exception
     *@returns boolean is locked or not
     */
    private boolean prepareLock(UserInfoBean userInfoBean, String proposalNumber, 
        HttpServletRequest request) throws Exception{
        boolean isSuccess = true;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        String mode = (String)session.getAttribute("mode"+session.getId());
        SearchModuleBean moduleBean = (SearchModuleBean)session.getAttribute(CoeusLiteConstants.PROPOSAL_SEARCH_ACTION+session.getId());
        LockBean lockBean = null;
        LockBean sessionLockBean = (LockBean)session.getAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());
    
        // If the action is from search window
        if(moduleBean!= null && !moduleBean.getOldModuleNumber().equals(EMPTY_STRING)){
            if(!moduleBean.getModuleNumber().equals(moduleBean.getOldModuleNumber())){
                // If the existing proposal number is not in DISPLAY MODE, release the lcok
                if(!moduleBean.getOldMode().equals(CoeusLiteConstants.DISPLAY_MODE)){
                    lockBean = getLockingBean(userInfoBean, moduleBean.getOldModuleNumber(), request);
                    releaseLock(lockBean,request);
                    session.removeAttribute(CoeusLiteConstants.PROPOSAL_SEARCH_ACTION+session.getId());
                    session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(), 
                            new Boolean(false));
                }
                
            }
                moduleBean.setMode(getMode(mode));
            // If the current Proposal number is in MODIFY MODE then lock it
                if(!moduleBean.getMode().equals(CoeusLiteConstants.DISPLAY_MODE)){
                    lockBean = getLockingBean(userInfoBean,moduleBean.getModuleNumber(),request);
                    boolean isLocked = isLockExists(lockBean, lockBean.getModuleKey());
                    boolean isSessionRowLocked = false;
                    Object  isRowLocked = session.getAttribute(
                        CoeusLiteConstants.RECORD_LOCKED+session.getId());
                    if(isRowLocked!= null){
                        isSessionRowLocked = ((Boolean)isRowLocked).booleanValue();
                    }
                    /** Make server call and get the locked data. Check for the unit
                     *number. If the unit number!= 00000000 then assume that
                     *it is lokced by the coeus premium and show the message
                     */
                    LockBean serverDataBean = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR+lockBean.getModuleNumber(), request);
                    // If the current proposal is locked by other user, then show the message
                    // else lock it
                    if(!isLocked ){
                        /** Check if the same record is locked or not. If not 
                         *then only show the message else discard it
                         */
                        if(!isSessionRowLocked || !serverDataBean.getSessionId().equals(lockBean.getSessionId())){
                            showLockingMessage(lockBean.getModuleNumber(), request);
                            isSuccess = false;
                        }// End if for lockeed record in the session
                    }else{// If the record is not locked then go ahead and lock it
                        lockModule(lockBean, request);
                        session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(), 
                            new Boolean(true));
                    }
                }
            
        }else{
            // Proposal opened from list
            lockBean = getLockingBean(userInfoBean, proposalNumber,request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean serverDataBean = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR+lockBean.getModuleNumber(), request);
            if(isLockExists) {
                if(serverDataBean!=null && !lockBean.getSessionId().equals(serverDataBean.getSessionId())) {
                    isLockExists = false;
                }
            }
            /** check whether lock exists or not. If not and the mode of the 
             *propsoal is not disaply then lock the proposal else show the message
             */
            if(isLockExists && !lockBean.getMode().equals(CoeusLiteConstants.DISPLAY_MODE)) {
                lockModule(lockBean, request);
                session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(), 
                            new Boolean(true));
            }else{
                if(sessionLockBean == null && !lockBean.getMode().equals(CoeusLiteConstants.DISPLAY_MODE)){
                    showLockingMessage(lockBean.getModuleNumber(), request);
                    isSuccess = false;
                } else if(sessionLockBean!=null && serverDataBean!=null) {
                    if(!lockBean.getSessionId().equals(serverDataBean.getSessionId())) {
                        showLockingMessage(lockBean.getModuleNumber(), request);
                        isSuccess = false;                        
                    }
                }
            }
        }
        session.removeAttribute(CoeusLiteConstants.PROPOSAL_SEARCH_ACTION+session.getId());
        return isSuccess;
        
    }

    
    
    
    
    /** Prepare the Locking messages when other or same user locked
     *the same module number. Make server call to get the message for the
     *locked user
     *@param String moduleNumber
     *@throws Exception
     */
    private void showLockingMessage(String moduleNumber, HttpServletRequest request) throws Exception{
        String lockId = CoeusLiteConstants.PROP_DEV_LOCK_STR+moduleNumber;
        HttpSession session = request.getSession();        
        WebTxnBean webTxnBean = new WebTxnBean();
        LockBean serverLockedBean = getLockedData(lockId,request);
        if(serverLockedBean!= null){
            serverLockedBean.setModuleKey("Development Proposal");
            serverLockedBean.setModuleNumber(moduleNumber);
//            String acqLock = "acquired_lock";
//            ActionMessages messages = new ActionMessages();
//            messages.add("acqLock", new ActionMessage(acqLock,
//                serverLockedBean.getUserName(),serverLockedBean.getModuleKey(),
//                    serverLockedBean.getModuleNumber()));
            // Added for displaying user name - user id start
            //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-start
            UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("user"+request.getSession().getId());
            String loggedInUserId = userInfoBean.getUserId();
            CoeusFunctions coeusFunctions = new CoeusFunctions();
            //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-end
            String lockUserId = serverLockedBean.getUserId();
            UserMaintDataTxnBean userTxnBean = new UserMaintDataTxnBean();
            String lockUserName = userTxnBean.getUserName(lockUserId);
            String acqLock = "acquired_lock";
            ActionMessages messages = new ActionMessages();
            //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-start
            String displayLockName = coeusFunctions.getParameterValue(CoeusConstants.DISPLAY_LOCKNAME_PROP);
            if(displayLockName != null && "Y".equalsIgnoreCase(displayLockName.trim()) || lockUserId.equalsIgnoreCase(loggedInUserId)){
                lockUserName=lockUserName;
            }else{
                lockUserName = CoeusConstants.lockedUsername;
            }
            //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-end
            messages.add("acqLock", new ActionMessage(acqLock,
                lockUserName,serverLockedBean.getModuleKey(),
                    serverLockedBean.getModuleNumber()));
            //End
            saveMessages(request, messages);
            session.removeAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());
        }
    }
    
    /**
     * To delete the Budget lock for that particular proposalNumber
     * and for that particular browsers session id.
     * @throws Exception
     */    
    private void releaseBudgetLock(HttpServletRequest request) throws Exception{
        LockBean lockBean =  (LockBean)request.getSession().getAttribute(CoeusLiteConstants.LOCK_BEAN+request.getSession().getId());
        WebTxnBean webTxnBean = new WebTxnBean();
        if(lockBean!= null) {
            LockBean serverDataBean = getLockedData(CoeusLiteConstants.BUDGET_LOCK_STR+lockBean.getModuleNumber(), request);
            if(!isLockExists(lockBean, lockBean.getModuleKey()) &&
            (serverDataBean==null || lockBean.getSessionId().equals(serverDataBean.getSessionId()))){
                releaseLock(lockBean, request);
                request.setAttribute(CoeusLiteConstants.RELEASE_LOCK, EMPTY_STRING);
            }
        }
    }
    
    private LockBean getRoutingLockingBean(UserInfoBean userInfoBean, String proposalNumber, HttpServletRequest request) throws Exception{
        LockBean lockBean = new LockBean();
        lockBean.setLockId(CoeusLiteConstants.PROP_ROUTING_LOCK_STR +proposalNumber);
        String mode = (String)request.getSession().getAttribute(CoeusLiteConstants.MODE_DETAILS+request.getSession().getId());
        mode = getMode(mode);
        lockBean.setMode(mode);
        lockBean.setModuleKey(CoeusLiteConstants.PROPOSAL_ROUTING_MODULE);
        lockBean.setModuleNumber(proposalNumber);
        lockBean.setModuleUnitNumber(userInfoBean.getUnitNumber());
        lockBean.setUnitNumber(UNIT_NUMBER);
        lockBean.setUserId(userInfoBean.getUserId());
        lockBean.setUserName(userInfoBean.getUserName());
        lockBean.setSessionId(request.getSession().getId());
        return lockBean;
    }

    
}
 