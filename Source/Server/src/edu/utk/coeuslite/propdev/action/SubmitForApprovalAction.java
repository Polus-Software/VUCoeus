package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean;
import edu.mit.coeus.routing.bean.RoutingUpdateTxnBean;
import edu.mit.coeus.s2s.SubmissionEngine;
import edu.mit.coeus.s2s.bean.S2SHeader;
import edu.mit.coeus.s2s.validator.S2SValidationException;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.mit.coeus.propdev.bean.ProposalActionTxnBean;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.utk.coeuslite.propdev.form.GeneralProposalForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.validator.DynaValidatorForm;
import org.apache.commons.lang.StringUtils;
import java.util.*;
import org.apache.struts.action.DynaActionForm;




/**
 * Created Muralidharan
 * User: muralidharann
 * Date: Jul 13, 2006
 * Time: 12:37:29 PM
 */

/* **************************** SubmitForApprovalAction *******************************
 * This class validates the following function calls
 * 1 - Validates whether the proposal is complete or not.If it is not it then return the messages
 *     to the presentaion layer. If it is valid or if the proposal is complete, it then process the
 *     next validation - Check EDI Validations
 * 2 - Checks for EDI validations. If the validation is successful it then validates for proposal
 *     validation check. If the EDI validation is unsuccessful, the program returns the error messages
 *     to the presentaion layet
 * 3 - Checks for proposal validation. For more info about this please refer the functionality of
 *     ValidateProposalAction
 * 4 - Update Sponsor codes
 * 5 - Update Rolodex IDs
 */
public class SubmitForApprovalAction extends ProposalBaseAction{
    // Entries in Transaction.xml
    private final String IS_PROPOSAL_COMPLETE   = "isProposalComplete";
    private final String IS_PROPOSAL_COMPLETE_PPC   = "isProposalCompletePPC";
    private final String CHECK_MAP_EXISTS       = "checkMapExists";
    private final String BUILD_MAPS_TREE        = "buildMapsTree";
    private final String SUBMIT_NOTIFICATION    = "submitNotification";
    private final String GET_PROPOSAL_APPROVAL  = "getProposalApproval";
    private final String CHECK_USER_PROP_ROLE   = "checkUserProposalRole";
    private final String ADD_UPD_PROP_ROLES     = "updPropUserRoles";
    private final String GET_SPONSORS_FOR_APP   = "getSponsorForApproval";
    private final String UPD_SPONSOR_OWNERSHIP  = "updSponsorOwnership";
    private final String UPD_ROLDOX_ID          = "updRolodexOwnership";
    private final String GET_ROLODEX_FOR_APP    = "getRolodexForApproval";
    private final String RETURN_MESSAGE         = "ls_ret_mesg";

    // Internal constant validation typre variables
    private final String PROP_COMPLETE_CHECK    = "ProposalCompleteCheck";
    private final String EDI_VALIDATE_CHECK     = "EdiValidateCheck";
    private final String PROP_VALIDATE_CHECK    = "ProposalValidateCheck";
    private final String MAP_EXIST_CHECK        = "MapExistCheck";
    private final String MAP_NOT_EXIST          = "proposal.map.noexist";
    private final String SUCCESSFULL_ROUTING    = "proposal.route.map.successfull";
    private final String SUBMIT_FAILED          = "proposal.submit.failed";
    private final String ROUTE_MAP_NOT_EXIST    = "proposal.route.map.noexist";
    private final String PROP_SPONSOR_ROLODEX   = "PropSponsorRolodexCheck";
    private static final String EDI_VALIDATION  = "proposal.submit.ediValidation";
    // Internal constant variables
    private final String PROPOSAL_NUMBER        = "proposalNumber";
    private final String PROPOSAL_MAP           = "PROPOSAL_RULES_MAP";
    private static final String SUBMIT_ACTION    = "SUBMIT_ACTION";
    private final String messagePrefix          = "proposal.validation.message.";
    private final String OPTION                 = "S";
    private final String success                = "0";
    private final Integer APPROVER_ROLE_ID      = new Integer(101);
    private static boolean IS_UPDATE            = false;
     private String flag="";
 private static final String PROPOSAL_INVESTIGATORS_KEYPERSONS = "propdevInvKeyPersons";
 private static final String GET_PROPOSAL_INVESTIGATORS="getProposalInvestigatorList";
 private static final String GET_PROPOSAL_KEYPERSONS = "getProposalKeyPersonList";
  public static final String ENABLE_PROP_PERSON_SELF_CERTIFY = "ENABLE_PROP_PERSON_SELF_CERTIFY";
//    private String proposalValidationType       = null;
    //    private Vector vecResponseObject;
    //    private HttpSession session;
    //    private HttpServletRequest request;
    //    private String userId;
    //    private String unitNumber;
    //    private Properties messagesBundle;
    //    private GeneralProposalForm  proposalForm;


    /** Creates a new instance of SubmitForApprovalAction */
    public SubmitForApprovalAction() {
    }
    /** Filled up the necessary details in performExecute() */
    public ActionForward performExecute(ActionMapping actionMapping,
    ActionForm actionForm,
    HttpServletRequest request,
    HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();


       flag = fetchParameterValue(request,ENABLE_PROP_PERSON_SELF_CERTIFY);
       if(flag==null)
       {
        flag="";
       }

        String userId = getValuesFromUserBean(session,USER);
        String unitNumber = getValuesFromUserBean(session,"unitNumber");
        Vector vecResponseObject = null;
        session.removeAttribute("proposalValidationType");
        String proposalNumber = (String) session.getAttribute(PROPOSAL_NUMBER+session.getId());
        HashMap hmProposalNumber = new HashMap();
        GeneralProposalForm proposalForm = (GeneralProposalForm)actionForm;
        //setSelectedStatusMenu(MENU_ID);
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
        mapMenuList.put("menuCode",CoeusliteMenuItems.SUBMIT_FOR_APPROVAL_CODE);
        setSelectedMenuList(request, mapMenuList);
        Properties messagesBundle = (Properties)getObjectsFromBundle();
        hmProposalNumber.put(PROPOSAL_NUMBER,proposalNumber);
        String actionType = request.getParameter("SUBMIT_FOR_APPROVE");
        boolean isSubmitAction = true;;
        if(actionType!= null && !actionType.equals(EMPTY_STRING)){
            isSubmitAction = false;
        }
        // Call business methods to proposal validations
        HashMap hmValue = isProposalComplete(hmProposalNumber, request, messagesBundle,userId);
        if(!((Boolean)hmValue.get("isProposalCompleteFlag")).booleanValue()) {
            vecResponseObject = (Vector) hmValue.get("vecResponseObject");
        }
        HashMap hmData = checkEDIValidations(request,proposalNumber, messagesBundle);
        if(!((Boolean)hmData.get(Boolean.class)).booleanValue()) {
            vecResponseObject = (Vector) hmData.get("vecResponseObject");
        }
        if (((Boolean)hmValue.get("isProposalCompleteFlag")).booleanValue() &&
        ((Boolean)hmData.get(Boolean.class)).booleanValue()){
            if(isSubmitAction){
                hmData = checkProposalValidationRules(proposalNumber,actionForm,session,request);
                if(!((Boolean)hmData.get("propValidationFlag")).booleanValue()) {
                    vecResponseObject = (Vector) hmData.get("vecResponseObject");
                }
                return actionMapping.findForward(returnResponse(actionMapping,proposalNumber,
                request,vecResponseObject));
            }

            if(vecResponseObject == null || vecResponseObject.size() == 0) {
                //No Validation Errors. Do grants Gov validation
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
                        //Fix #4513
                       // return actionMapping.findForward(returnResponse(actionMapping,proposalNumber  ,request,vecResponseObject));
                        return actionMapping.findForward(PROP_VALIDATE_CHECK);
                       //END Fix
                    }
                }//End if S2S Candidate
            }

            IS_UPDATE = true;
            //code modified for Case#2785 - Coeuslite Routing enehnacement - starts
            int mapExists = 0;
//            int mapExists = checkMapsExists(proposalNumber, request, unitNumber, userId);
//            vecResponseObject = isMapExists(request,mapExists, messagesBundle);
//            if (mapExists > 0){
            //code modified for Case#2785 - Coeuslite Routing enehnacement - ends
                HashMap hmMapExists = buildMapsForRouting(proposalNumber, request, messagesBundle, unitNumber, userId);
                mapExists = Integer.parseInt(hmMapExists.get("buildMaps").toString());
                vecResponseObject = isMapExists(request,mapExists, messagesBundle);
                if(mapExists > 0){
                    Vector vecOtherChecks = new Vector();
                    //Case 4508: Routing Issue - Start
//                    int recordsUpdated = submitNotification(proposalNumber, request, unitNumber, userId);
                    submitNotification(proposalNumber, unitNumber, userId);
                    //4508 End
                    IS_UPDATE = false;
                    getProposalApprovalForProposalNumber(hmProposalNumber, request, proposalForm);
                    getSponsorCodes(hmProposalNumber, request);
                    getRolodexId(hmProposalNumber, request);
                    vecOtherChecks.addElement("Submission is successfull");
                    request.setAttribute("submissionSuccess", "submissionSuccess");
                    setProposalMap(request,vecOtherChecks);
                    updateChildStatus(request,proposalNumber);
                    //Added for Case 2785 - Coeus Lite Routing - Release lock after the proposal is submitted for routing - Start
                     UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
                     LockBean lockBean = getLockingBean(userInfoBean, proposalNumber,request);
                     releaseLock(lockBean, request);
                     session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(), new Boolean(false));
                    //Added for Case 2785 - Coeus Lite Routing - Release lock after the proposal is submitted for routing - End
                 //code modified for Case#2785 - Coeuslite Routing enehnacement - starts
                } else if(mapExists == -999) {
                    String proposalValidationType = MAP_EXIST_CHECK;
                    session.setAttribute("proposalValidationType",proposalValidationType);
                    vecResponseObject.addAll((Vector)hmMapExists.get("vecResponseObject"));
                    session.setAttribute(PROPOSAL_MAP,vecResponseObject);
                    return actionMapping.findForward("ProposalCompleteCheck");
                }
                //code modified for Case#2785 - Coeuslite Routing enehnacement - ends
//            }
        }


        return actionMapping.findForward(returnResponse(actionMapping,proposalNumber
        ,request,vecResponseObject));
    }
    private String returnResponse(ActionMapping actionMapping, String proposalNumber,
    HttpServletRequest request, Vector vecResponseObject) throws Exception{
        HttpSession session = request.getSession();
        String navigator = EMPTY_STRING;
        session.removeAttribute(SUBMIT_ACTION);
        session.setAttribute(PROPOSAL_NUMBER+session.getId(), proposalNumber);
        session.setAttribute(PROPOSAL_MAP,vecResponseObject);
        session.setAttribute(SUBMIT_ACTION,SUBMIT_ACTION);
        //2960
        String proposalValidationType  = (String)session.getAttribute("proposalValidationType");
//        if(getProposalValidationType() != null && !getProposalValidationType().equals(EMPTY_STRING)){
//            navigator = getProposalValidationType();
//        }else{
//            navigator = PROP_SPONSOR_ROLODEX;
//        }
        if(proposalValidationType != null && !proposalValidationType.equals(EMPTY_STRING)){
            navigator = proposalValidationType;
        }else{
            navigator = PROP_SPONSOR_ROLODEX;
        }
        return navigator;

    }
    private Vector isMapExists(HttpServletRequest request,int mapExists,Properties messagesBundle) throws Exception{
        Vector vecMapCheck = new Vector();
        HttpSession session = request.getSession();
        if(mapExists == 0){
            //2960
            String proposalValidationType = MAP_EXIST_CHECK;
            session.setAttribute("proposalValidationType",proposalValidationType);
//            setProposalValidationType(MAP_EXIST_CHECK); -- 2960
            //commented and added for case # 2886 start
            vecMapCheck.addElement(messagesBundle.get("proposal.route.map.noMap"));
//            vecMapCheck.addElement(messagesBundle.get(SUCCESSFULL_ROUTING));
            //case # 2886 end
        }else if(mapExists == -1){
            //2960
            String proposalValidationType = MAP_EXIST_CHECK;
            session.setAttribute("proposalValidationType",proposalValidationType);
//            setProposalValidationType(MAP_EXIST_CHECK);
            //2960
            vecMapCheck.addElement(messagesBundle.get(SUBMIT_FAILED));
          // Case #2886 - start
        }else if(mapExists == 1){
            String proposalValidationType = MAP_EXIST_CHECK;
            session.setAttribute("proposalValidationType",proposalValidationType);
            vecMapCheck.addElement(messagesBundle.get(SUCCESSFULL_ROUTING));
        }
        // Case #2886 - end

        return vecMapCheck;
    }

    //COEUSDEV-325:Ability to route a protocol to PI if the person submitting the protocol IS NOT the PI
    //Added user Id parameter to initialize QuestionnaireTxnBean
    private HashMap isProposalComplete(HashMap hmProposalNumber,
            HttpServletRequest request,Properties messagesBundle,String userId) throws Exception{


        WebTxnBean webTxnBean = new WebTxnBean();
        //S T A R T
     HashMap hmPropCompleteMsg = new HashMap();
      request.getSession().setAttribute("isPpcCompleteFlag", false);
        if(flag!=null&& flag.equalsIgnoreCase("1"))
        {
        Hashtable htIsProposalComplete  = (Hashtable)webTxnBean.getResults(request,IS_PROPOSAL_COMPLETE_PPC,hmProposalNumber);
        hmPropCompleteMsg = (HashMap)htIsProposalComplete.get(IS_PROPOSAL_COMPLETE_PPC);
         HttpSession session = request.getSession();
         HashMap hmpProposalData = new HashMap();

          hmpProposalData.put(PROPOSAL_NUMBER,session.getAttribute(PROPOSAL_NUMBER+session.getId()));
   Hashtable htInvestigator =
                (Hashtable)webTxnBean.getResults(request,PROPOSAL_INVESTIGATORS_KEYPERSONS,hmpProposalData);

     // get the investigators list and save it in 'invData' vector
        Vector invData=(Vector)htInvestigator.get(GET_PROPOSAL_INVESTIGATORS);

     // get the KeyStudyPerson Data and save it in 'keyPersData' vector
        Vector keyPersData=(Vector)htInvestigator.get(GET_PROPOSAL_KEYPERSONS);

     // merge Vector invData and Vector keyPersData into one Vector invKeyData
         Vector invKeyData = new Vector();
        if(invData !=null && invData.size() >0){
            for (int i=0;i<invData.size();i++) {
                // added Certify flag for Case Id 2579
                DynaValidatorForm dynaInvestigator = (DynaValidatorForm) invData.get(i);
                String personId=(String)dynaInvestigator.get("personId");
                request.setAttribute("pi_id", personId);
                HashMap hmpInvData = new HashMap();
                hmpInvData.put("proposalNumber",(String)hmpProposalData.get("proposalNumber"));
                hmpInvData.put("personId",personId);
                //Added for COEUSQA-2037 : Software allows you to delete an investigator who is assigned credit in the credit split window

//
//                //COEUSQA-2037 : End
//
//                    Hashtable htCertInv = (Hashtable)webTxnBean.getResults(request, "isProposalInvCertified", hmpInvData);
//
//                    HashMap hmCertInv = (HashMap)htCertInv.get("isProposalInvCertified");

                Hashtable htCertInv=new Hashtable();
                HashMap  hmCertInv=new HashMap();
  //********************************
//                    if(flag.equalsIgnoreCase("1")){  
                        HashMap map1=new HashMap();
                        map1.put("AV_MODULE_ITEM_CODE",3);
                        map1.put("AV_MODULE_SUB_ITEM_CODE",6);
                        map1.put("AV_MODULE_ITEM_KEY",(String)hmpProposalData.get("proposalNumber"));
                        map1.put("AV_MODULE_SUB_ITEM_KEY",personId);
                      //  map1.put("AV_USER",ActualUser);
                        map1.put("AV_PERSONID",personId);

          htCertInv= (Hashtable)webTxnBean.getResults(request, "fnGetPpcCompleteFlag", map1);
           hmCertInv= (HashMap)htCertInv.get("fnGetPpcCompleteFlag");
//                    }

  //***************************************

                dynaInvestigator.set("certifyFlag",hmCertInv.get("isCertified").toString());
                int x=invKeyData.size();
                 int y=invData.size();
                invKeyData.addElement(invData.get(i));
            }
        }

        if(keyPersData !=null && keyPersData.size()>0){
            HashMap hmpInvData=new HashMap();

            for (int i=0;i<keyPersData.size();i++) {


                 DynaValidatorForm dynaKeyPerson = (DynaValidatorForm) keyPersData.get(i);
                 String personId=(String)dynaKeyPerson.get("personId");

                 int    j=invData.size()+1;
                 DynaValidatorForm dynaInvestigator = (DynaValidatorForm) keyPersData.get(i);
                hmpInvData.put("proposalNumber",(String)hmpProposalData.get("proposalNumber"));                                                         
                        HashMap map1=new HashMap();
                        map1.put("AV_MODULE_ITEM_CODE",3);
                        map1.put("AV_MODULE_SUB_ITEM_CODE",6);
                        map1.put("AV_MODULE_ITEM_KEY",(String)hmpProposalData.get("proposalNumber"));
                        map1.put("AV_MODULE_SUB_ITEM_KEY",personId);
                    //    map1.put("AV_USER",ActualUser);
                        map1.put("AV_PERSONID",personId);

            Hashtable htCertkey= (Hashtable)webTxnBean.getResults(request, "fnGetPpcCompleteFlag", map1);
            HashMap hmCertkey= (HashMap)htCertkey.get("fnGetPpcCompleteFlag");

              dynaInvestigator.set("certifyFlag",hmCertkey.get("isCertified").toString());
              invKeyData.addElement(keyPersData.get(i));

            }
        }
                 for (int i=0;i<invKeyData.size();i++) {
                 request.setAttribute("message", false);
                 DynaValidatorForm dynaKeyPerson = (DynaValidatorForm) invKeyData.get(i);
                 String certify = (String) dynaKeyPerson.get("certifyFlag");
                 if(certify.equalsIgnoreCase("0"))
                 {
                 request.setAttribute("message", true);
                 request.getSession().setAttribute("isPpcCompleteFlag", true);
                 break;
                 }
                 }
        }
     //E N D
        else{
        Hashtable htIsProposalComplete  = (Hashtable)webTxnBean.getResults(request,IS_PROPOSAL_COMPLETE,hmProposalNumber);
        hmPropCompleteMsg = (HashMap)htIsProposalComplete.get(IS_PROPOSAL_COMPLETE);
        // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - Start
        // Check required Questionnaires are filled.
        }
        QuestionnaireTxnBean questionnaireTxnBean = new QuestionnaireTxnBean();
        String proposalNumber = (String) request.getSession().getAttribute(PROPOSAL_NUMBER + request.getSession().getId());
        Vector vecUnfilledQnr = questionnaireTxnBean.fetchQuestionnaireCompletedForModule(proposalNumber,3,"0",0);
//        return processResponseMessage(request,hmPropCompleteMsg, messagesBundle);
        return processResponseMessage(request,hmPropCompleteMsg, messagesBundle, vecUnfilledQnr);

    }
//    private HashMap processResponseMessage(HttpServletRequest request,HashMap hmPropCompleteMsg,Properties messagesBundle) throws Exception {
    private HashMap processResponseMessage(HttpServletRequest request,HashMap hmPropCompleteMsg,Properties messagesBundle, Vector vecUnfilledQnr) throws Exception {
        // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - End
        boolean isProposalCompleteFlag = false;
        Vector vecPropCompleteMsg = new Vector();
        String respMessage = (String) hmPropCompleteMsg.get(RETURN_MESSAGE);
        StringTokenizer stokenMessage = new StringTokenizer(respMessage,",");
        while (stokenMessage.hasMoreTokens()){
            String message = stokenMessage.nextToken();
            vecPropCompleteMsg.addElement(messagesBundle.get(messagePrefix+message));
        }
        // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - Start
        boolean requiredQnrFilled = true;
        if(vecUnfilledQnr != null && vecUnfilledQnr.size() > 0){
            String mandatoryQnr = (String) vecUnfilledQnr.get(0);
            String incompleteQnr = (String) vecUnfilledQnr.get(1);
            String newVersionQnr = (String) vecUnfilledQnr.get(2);

            boolean mandatoryQnrPresent = false;
            boolean incompleteQnrPresent = false;
            boolean newVersionQnrPresent = false;

            if(mandatoryQnr != null && !"".equals(mandatoryQnr.trim())){
                mandatoryQnrPresent = true;
                requiredQnrFilled = false;
            }

            if(incompleteQnr != null && !"".equals(incompleteQnr.trim())){
                incompleteQnrPresent = true;
                requiredQnrFilled = false;
            }

            if(newVersionQnr != null && !"".equals(newVersionQnr.trim())){
                newVersionQnrPresent = true;
                requiredQnrFilled = false;
            }

            if(mandatoryQnrPresent){
                String validationMessage = (String) messagesBundle.get(messagePrefix+"1012");

                stokenMessage = new StringTokenizer(mandatoryQnr,"~");
                while (stokenMessage.hasMoreTokens()){
                    String message = stokenMessage.nextToken();
                    validationMessage = validationMessage + "<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + message;
                }
                vecPropCompleteMsg.addElement(validationMessage);
            }

            if(incompleteQnrPresent){

                String validationMessage = (String) messagesBundle.get(messagePrefix+"1014");

                stokenMessage = new StringTokenizer(incompleteQnr,"~");
                while (stokenMessage.hasMoreTokens()){
                    String message = stokenMessage.nextToken();
                    validationMessage = validationMessage + "<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + message;
                }
                vecPropCompleteMsg.addElement(validationMessage);
            }

            if(newVersionQnrPresent){
                String validationMessage = (String) messagesBundle.get(messagePrefix+"1013");

                stokenMessage = new StringTokenizer(newVersionQnr,"~");
                while (stokenMessage.hasMoreTokens()){
                    String message = stokenMessage.nextToken();
                    validationMessage = validationMessage + "<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + message;
                }
                vecPropCompleteMsg.addElement(validationMessage);
            }
        }

        if(vecPropCompleteMsg.size() > 0 && vecPropCompleteMsg.elementAt(0).equals(messagesBundle.get(messagePrefix+success))
            && requiredQnrFilled){
        // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - End
                isProposalCompleteFlag = true;
        }
        else{
            //2960
            HttpSession session = request.getSession();
            String proposalValidationType = PROP_COMPLETE_CHECK;
            session.setAttribute("proposalValidationType",proposalValidationType);

//            setProposalValidationType(PROP_COMPLETE_CHECK);
            //2960
            //emptyAndSetResponseObject(vecPropCompleteMsg);

        }
        // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire  - Start
        if(vecPropCompleteMsg.size() > 0 && vecPropCompleteMsg.elementAt(0).equals(messagesBundle.get(messagePrefix+success))
            && !requiredQnrFilled){
            vecPropCompleteMsg.remove(0);
        }
        if(request.getSession().getAttribute("isPpcCompleteFlag")!=null && (Boolean)request.getSession().getAttribute("isPpcCompleteFlag")){
             isProposalCompleteFlag = false;
             if(vecPropCompleteMsg.size() > 0 && vecPropCompleteMsg.elementAt(0).equals(messagesBundle.get(messagePrefix+success))){
            vecPropCompleteMsg.remove(0);

        }
            HttpSession session = request.getSession();
            String proposalValidationType = PROP_COMPLETE_CHECK;
            session.setAttribute("proposalValidationType",proposalValidationType);
        }
        // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - End
        HashMap hmMsg = new HashMap();
        hmMsg.put("isProposalCompleteFlag", new Boolean(isProposalCompleteFlag));
        hmMsg.put("vecResponseObject", vecPropCompleteMsg);
        return hmMsg;
    }
    private HashMap checkEDIValidations(HttpServletRequest request,String proposalNumber,Properties messagesBundle) throws Exception{
        boolean ediValidationsFlag = true;
        Vector vecCheckEDI = new Vector();
        Integer failure = new Integer(-1);
        // Since the scope is not allowed in Transaction.xml, calling ProposalActionTxnBean checkEDIValidation()
        ProposalActionTxnBean objProposalActionTxnBean = new ProposalActionTxnBean();
        String message = (String)messagesBundle.get(EDI_VALIDATION);
        vecCheckEDI = objProposalActionTxnBean.checkEDIValidation(proposalNumber);
        // Vector will contain the success/failure message at the first element (0 or -1)
        // If the operation is failure (-1) get the message at the next element and set in session
        if(vecCheckEDI != null && vecCheckEDI.elementAt(0).equals(failure)){
            message = message+(String)vecCheckEDI.elementAt(1);
            vecCheckEDI.setElementAt(message,1);
            ediValidationsFlag = false;
            vecCheckEDI.removeElementAt(0);
            //2960
            HttpSession session = request.getSession();
            String proposalValidationType = EDI_VALIDATE_CHECK;
            session.setAttribute("proposalValidationType",proposalValidationType);
//            setProposalValidationType(EDI_VALIDATE_CHECK);
            //2960
            //emptyAndSetResponseObject(vecCheckEDI);
        }
        HashMap hmMsg = new HashMap();
        hmMsg.put(Boolean.class, new Boolean(ediValidationsFlag));
        hmMsg.put("vecResponseObject", vecCheckEDI);
        return hmMsg;
    }
    private HashMap checkProposalValidationRules(String proposalNumber,
    ActionForm  actionForm,HttpSession session,HttpServletRequest request) throws Exception{
        boolean propValidationFlag = true;
        DynaValidatorForm  dynaValidatorForm =(DynaValidatorForm)actionForm;
         //Added with Case 2158: Budgetary Validations
        int budgetVersion = getBudgetVersionToValidate(proposalNumber);
        if(budgetVersion== 1000){
            CoeusMessageResourcesBean messageResource = new CoeusMessageResourcesBean();
            request.setAttribute("WarningMessage",messageResource.parseMessageKey(
                    "validationChecks_exceptionCode.1901",new String[] {proposalNumber}));
            budgetVersion = 0;
        }else if(budgetVersion== 1001){
            throw new CoeusException("Please select a final Version");
        }
        //2158 End
        request.setAttribute("NewFinalVersion",String.valueOf(budgetVersion));
        Vector propValidationRules = checkValidationProposal(proposalNumber,budgetVersion, dynaValidatorForm, session);
        if(propValidationRules.size() > 0){
            propValidationFlag = false;
            // emptyAndSetResponseObject(propValidationRules);
        }else{
            //this.vecResponseObject = propValidationRules;
        }
        //2960

        String proposalValidationType = PROP_VALIDATE_CHECK;
        session.setAttribute("proposalValidationType",proposalValidationType);

//        setProposalValidationType(PROP_VALIDATE_CHECK);
        //2960
        HashMap hmMsg = new HashMap();
        hmMsg.put("propValidationFlag", new Boolean(propValidationFlag));
        hmMsg.put("vecResponseObject", propValidationRules);
        return hmMsg;
    }
    private int checkMapsExists(String proposalNumber, HttpServletRequest request,
    String unitNumber,String userId) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htRowParameter = (Hashtable)webTxnBean.getResults(request,CHECK_MAP_EXISTS,
        getHashMapObjects(proposalNumber, true, unitNumber, userId));
        HashMap hmMapExists = (HashMap)htRowParameter.get(CHECK_MAP_EXISTS);
        int mapExists        = Integer.parseInt(hmMapExists.get("IS_EXIST").toString());
        return mapExists;
    }
    private HashMap buildMapsForRouting(String proposalNumber, HttpServletRequest request,
    Properties messagesBundle,String unitNumber,String userId) throws Exception{
        //code modified for Case#2785 - Coeuslite Routing enehnacement - starts
//        WebTxnBean webTxnBean = new WebTxnBean();
//        Hashtable htRowParameter = (Hashtable)webTxnBean.getResults(request,BUILD_MAPS_TREE,
//        getHashMapObjects(proposalNumber, true, unitNumber, userId));
//        HashMap hmBuildMaps = (HashMap)htRowParameter.get(BUILD_MAPS_TREE);
        Vector vecMapCheck = new Vector();
//        int buildMaps       = Integer.parseInt(hmBuildMaps.get("IS_UPDATE").toString());
        RoutingUpdateTxnBean routingUpdateTxnBean = new RoutingUpdateTxnBean(userId);
        int buildMaps = 0;
        try{
            buildMaps = routingUpdateTxnBean.buildMapsForRouting(proposalNumber, unitNumber, 3, 0, "S");
        } catch(DBException e){
            vecMapCheck.addElement(e.getUserMessage());
            buildMaps = -999;
        }
        //code modified for Case#2785 - Coeuslite Routing enehnacement - ends
        if(buildMaps == 0){
            //2960
            HttpSession session = request.getSession();
            String proposalValidationType = MAP_EXIST_CHECK;
            session.setAttribute("proposalValidationType",proposalValidationType);

//            setProposalValidationType(MAP_EXIST_CHECK);
            //2960
            vecMapCheck.addElement(messagesBundle.get(ROUTE_MAP_NOT_EXIST));
            //emptyAndSetResponseObject(vecMapCheck);
        }
        HashMap hmMsg = new HashMap();
        hmMsg.put("buildMaps", new Integer(buildMaps));
        hmMsg.put("vecResponseObject", vecMapCheck);
        return hmMsg;
    }

    private void submitNotification(String proposalNumber,String unitNumber,String userId) throws Exception{
        //Commented for case 4508: Routing issue
//        WebTxnBean webTxnBean = new WebTxnBean();
        //code modified for Case#2785 - Coeuslite Routing enehnacement - starts
//        Hashtable htRowParameter = (Hashtable)webTxnBean.getResults(request,SUBMIT_NOTIFICATION,
//        getHashMapObjects(proposalNumber, false, unitNumber, userId));
//        HashMap hmModuleDetails = getHashMapObjects(proposalNumber, false, unitNumber, userId);
//        hmModuleDetails.put("proposalNumber", proposalNumber);
//        hmModuleDetails.put("moduleCode", "3");
//        hmModuleDetails.put("moduleItemKeySeq", "0");
//        Hashtable htRowParameter = (Hashtable)webTxnBean.getResults(request,SUBMIT_NOTIFICATION,hmModuleDetails);
        //code modified for Case#2785 - Coeuslite Routing enehnacement - ends
//        HashMap submitNotification = (HashMap)htRowParameter.get(SUBMIT_NOTIFICATION);
//        int submitNotify = Integer.parseInt(submitNotification.get("IS_UPDATE").toString());
//        return submitNotify;
        RoutingUpdateTxnBean routingTxnBean = new RoutingUpdateTxnBean(userId);
        //COEUSDEV-75:Rework email engine so the email body is picked up from one place
        routingTxnBean.sendNotification(ModuleConstants.PROPOSAL_DEV_MODULE_CODE, proposalNumber, 0, unitNumber);
        //COEUSDEV-75:End
        //4508:End
    }

    private void getProposalApprovalForProposalNumber(HashMap hmProposalNumber,
    HttpServletRequest request,GeneralProposalForm proposalForm) throws Exception{
        Vector vecProposalApproval = new Vector();
        WebTxnBean webTxnBean = new WebTxnBean();
        hmProposalNumber.put("moduleCode", "3");
        hmProposalNumber.put("moduleItemKeySeq", "0");
        Hashtable htProposalApproval = (Hashtable)webTxnBean.getResults(request,GET_PROPOSAL_APPROVAL,hmProposalNumber);
        vecProposalApproval =(Vector) htProposalApproval.get(GET_PROPOSAL_APPROVAL);
        checkUserProposalRole(hmProposalNumber.get(PROPOSAL_NUMBER).toString(),vecProposalApproval,
        request, proposalForm);
    }
    private void checkUserProposalRole(String proposalNumber, Vector vecProposalApproval,
    HttpServletRequest request,GeneralProposalForm proposalForm) throws Exception{
        int hasRole = 0;
        //Check If no Approver Role Exist for this User
        //If not insert Approver Role for  this User
        int sizeOfVect = vecProposalApproval==null?0:vecProposalApproval.size();
        if (sizeOfVect > 0){
            HashMap hmUserPropoRole = new HashMap();
            WebTxnBean webTxnBean = new WebTxnBean();
            for(int row = 0 ; row < sizeOfVect; row++){
                proposalForm = (GeneralProposalForm)vecProposalApproval.elementAt(row);
                hmUserPropoRole.put("userId", proposalForm.getUserId());
                hmUserPropoRole.put(PROPOSAL_NUMBER, proposalNumber);
                hmUserPropoRole.put("roleId", APPROVER_ROLE_ID);
                Hashtable htHasRole   =  (Hashtable)webTxnBean.getResults(request,CHECK_USER_PROP_ROLE,hmUserPropoRole);
                HashMap   hmHasRole   =  (HashMap)htHasRole.get(CHECK_USER_PROP_ROLE);
                hasRole = Integer.parseInt(hmHasRole.get("HAS_RIGHT").toString());
                if(hasRole == 0){
                    proposalForm.setProposalNumber(proposalNumber);
                    proposalForm.setRoleId(APPROVER_ROLE_ID.intValue());
                    proposalForm.setUserId(proposalForm.getUserId());
                    proposalForm.setAcType("I");
                    updateProposalUserRoles(proposalNumber, request, proposalForm);
                }
            }
        }
    }
    private void updateProposalUserRoles(String proposalNumber, HttpServletRequest request,
    GeneralProposalForm proposalForm)throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        proposalForm.setUpdateUser(getValuesFromUserBean(request.getSession(),USER));
        proposalForm.setUpdateTimeStamp(prepareTimeStamp());
        HashMap hmUpdateUserRoles = new HashMap();
        hmUpdateUserRoles.put("proposalNumber", proposalNumber);
        hmUpdateUserRoles.put("userId", proposalForm.getUserId());
        hmUpdateUserRoles.put("roleId", new Integer(proposalForm.getRoleId()));
        hmUpdateUserRoles.put("updateUser",proposalForm.getUpdateUser());
        hmUpdateUserRoles.put("updateTimeStamp",proposalForm.getUpdateTimeStamp());
        hmUpdateUserRoles.put("acType",proposalForm.getAcType());
        webTxnBean.getResults(request, ADD_UPD_PROP_ROLES, hmUpdateUserRoles);
        IS_UPDATE = true;
    }
    private void getSponsorCodes(HashMap hmProposalNumber, HttpServletRequest request) throws Exception{
        Vector vecSponsor = new Vector();
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htSponsor = (Hashtable)webTxnBean.getResults(request,GET_SPONSORS_FOR_APP,hmProposalNumber);
        vecSponsor =(Vector) htSponsor.get(GET_SPONSORS_FOR_APP);
        updateSponsorOwnership(vecSponsor, request);
    }
    private void updateSponsorOwnership(Vector vecSponsor, HttpServletRequest request) throws Exception{
        int vecSponsorSize = vecSponsor==null?0:vecSponsor.size();
        HashMap hmSponsorCode = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        for(int row = 0 ; row < vecSponsorSize; row++){
            GeneralProposalForm proposalForm = (GeneralProposalForm)vecSponsor.elementAt(row);
            String sponsorCode = proposalForm.getSponsorCode();
            String primeSponsorCode = proposalForm.getPrimeSponsorCode();
            if(StringUtils.isNotEmpty(sponsorCode)){
                hmSponsorCode.put("sponsorCode",sponsorCode );
                hmSponsorCode.put("primeSponsorCode",primeSponsorCode );
                Hashtable htUpdateSponsor   =  (Hashtable)webTxnBean.getResults(request,
                UPD_SPONSOR_OWNERSHIP,hmSponsorCode);
                IS_UPDATE = true;
            }

        }

    }
    private void getRolodexId(HashMap hmProposalNumber, HttpServletRequest request) throws Exception{
        Vector vecRolodex = new Vector();
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htSponsor = (Hashtable)webTxnBean.getResults(request,GET_ROLODEX_FOR_APP,hmProposalNumber);
        vecRolodex=(Vector) htSponsor.get(GET_ROLODEX_FOR_APP);
        updateRolodexOwnership(vecRolodex, request);

    }
    private void updateRolodexOwnership(Vector vecRolodex, HttpServletRequest request) throws Exception{
        int vecRolodexSize = vecRolodex==null?0:vecRolodex.size();
        HashMap hmRolodexId = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        for(int row = 0 ; row < vecRolodexSize; row++){
            GeneralProposalForm proposalForm = (GeneralProposalForm)vecRolodex.elementAt(row);
            String rolodexId = proposalForm.getRolodexId();
            if(StringUtils.isNotEmpty(rolodexId)){
                hmRolodexId.put("rolodexId",rolodexId);
                Hashtable htUpdateRolodexId   =  (Hashtable)webTxnBean.getResults(request,
                UPD_ROLDOX_ID,hmRolodexId);
                IS_UPDATE = true;
            }
        }
    }
    private void setProposalMap(HttpServletRequest request,Vector vecProposalMap) throws Exception{
        //2960
        HttpSession session = request.getSession();
        String proposalValidationType = PROP_SPONSOR_ROLODEX;
        session.setAttribute("proposalValidationType",proposalValidationType);

//        setProposalValidationType(PROP_SPONSOR_ROLODEX);
        //2960
        //emptyAndSetResponseObject(vecProposalMap);
    }

    /*
     * UTILITY METHODS
     */
    private HashMap getHashMapObjects(String proposalNumber, boolean optionFlag,
    String unitNumber,String userId) throws Exception{
        HashMap hmMapExists = new HashMap();
        hmMapExists.put(PROPOSAL_NUMBER, proposalNumber);
        hmMapExists.put("unitNumber", unitNumber);
        hmMapExists.put("userId", userId.toUpperCase());
        if(optionFlag){
            hmMapExists.put("OPTION", OPTION);
        }
        return hmMapExists;
    }
   /* private Vector emptyAndSetResponseObject (Vector vecMessageObject ){
        if(this.vecResponseObject!= null && (!this.vecResponseObject.isEmpty())){
            vecResponseObject.removeAllElements();
        }
        vecResponseObject = vecMessageObject;
    }*/
    //2960
//    private void setProposalValidationType(String proposalValidationType){
//        this.proposalValidationType = proposalValidationType;
//    }
//    private String getProposalValidationType(){
//        return this.proposalValidationType;
//    }
    //2960




}