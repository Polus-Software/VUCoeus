/*
 * ProtocolSubmissionAction.java
 *
 * Created on 31 August 2006, 10:37
 */

/* PMD check performed, and commented unused imports and variables on 14-MARCH-2011
 * by Maharaja Palanichamy
 */

package edu.mit.coeuslite.iacuc.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.iacuc.bean.ActionTransaction;
import edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean;
import edu.mit.coeus.iacuc.bean.ProtocolReviewerInfoBean;
import edu.mit.coeus.iacuc.bean.ProtocolSubmissionUpdateTxnBean;
import edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.iacuc.bean.ProtocolHeaderDetailsBean;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.ProtocolSubmissionDynaBeanList;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
//import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.validator.DynaValidatorForm;
import edu.mit.coeus.utils.ModuleConstants;
/**
 *
 * @author  mohann
 */
public class ProtocolSubmissionAction extends ProtocolBaseAction{
//    private ActionForward actionForward = null;
//    private WebTxnBean webTxnBean ;
//    private HttpServletRequest request;
//    private HttpServletResponse response;
//    private ActionMapping actionMapping;
//    private HttpSession session;
//    private ActionMessages actionMessages ;
    private static final String GET_PROTOCOL_SUBMISSION_DATA="/getSubmitForIacuc";
    private static final String GET_SCHEDULES_DETAILS = "/getIacucScheduleDetails";
    private static final String GET_REVIEWER_DETAILS ="/getIacucReviewerDetails";
    private static final String GET_COMMITTEE_DETAILS = "/getIacucCommitteeDetails";
    private static final String GET_ALL_COMM_LIST = "/getIacucAllCommitteeList";
    private static final String GET_CHECK_LIST = "/getCheckList";
    private static final String SAVE_PROTOCOL_SUBMISSION = "/iacucProtocolSubmission";
    // Added for case# 3063 Notice Page to the PI that a Protocol Submission to the IRB is successful
    private static final String GET_SUBMISSION_CONFIRMATION = "/getIacucSubmissionConfirmation";
    private static final String SUCCESS = "success";
    private static final String EMPTY_STRING ="";
    private static final String PROTOCOL_NUMBER = "protocolNumber";
    private static final String SEQUENCE_NUMBER ="sequenceNumber";
    private static final String IACUC_COMM_SELECTION_DURING_SUBMISSION ="IACUC_COMM_SELECTION_DURING_SUBMISSION";
    private static final String GET_PROTOCOL_SUBMISSION_MODE = "getIacucProtocolSubmissionMode";
    private static final String PARAMETER_NAME = "parameterName";
    private static final String GET_PARAMETER_VALUE = "getParameterValue";
    private static final String GET_SUBMIT_FOR_REVIEW_DETAILS = "getIacucSubmitForReviewDetails";
    private static final String GET_SUBMISSION_TYPES = "getIacucSubmissionTypes";
    private static final String GET_SUBMISSION_TYPE_QUALIFIERS = "getIacucSubTypeQualifiers";
    private static final String GET_PROTOCOL_REVIEW_TYPES = "getIacucReviewTypes";
    private static final String GET_EXPEDITED_REVIEW_CHECK_LIST = "getExpeditedReviewCheckList";
    private static final String GET_EXEMPT_STUDIES_CHECK_LIST = "getExemptStudiesCheckList";
    private static final String GET_PROTO_SUBMISSION_COMMITTEE = "getIacucSubmissionCommittee";
    private static final String GET_MATCHING_COMM_FOR_PROTO = "getMatchingCommForIacuc";
    private static final String GET_VALID_PROTO_SUB_REVIEW_TYPES = "getValidIacucSubReviewTypes";
    private static final String GET_PROTOCOL_REVIEWER_LIST = "getIacucReviewerList";
    private static final String SUBMIT_FOR_REVIEW_FORM = "submitForIacucReviewForm";
    private static final String SUBMIT_FOR_REVIEW_LIST = "submitForReviewList";
    private static final String GET_SCHEDULE_LIST_FOR_COMM = "getScheduleListForComm";
    private static final String COMMITTEE_ID = "committeeId";
    private static final String COMMITTEE_NAME = "committeeName";
    private static final String SELECT_COMMITTEE = "selectCommittee";
    private static final String SCHEDULE_ID = "scheduleId";
    private static final String SCHEDULE_EXIST = "scheduleExist";
    private static final String GET_COMM_COMMITTEE_REVIEWERS = "getIacucCommCommitteeReviewers";
    private static final String SELECT_SCHEDULE = "selectSchedule";
    private static final String YES = "YES";
    private static final String REVIEWER_EXIST = "reviewerExist";
    private static final String GET_COMMITTEE_REVIEWERS = "getIacucCommitteeReviewers";
    private static final String GET_PROTO_SUBMISSION_COUNT = "getIacucSubmissionCount";
    private static final String MAX_PROTOCOLS_EXCEEDED = "MaxProtocolsExceeded";
    private static final String GET_COMMITTEE_LIST = "getIacucCommitteeList";
    private static final String MAX_PROTOCOLS = "maxProtocols";
    private static final String PARAMETER_VALUE = "parameterValue";
    private static final String MATCHING_COMMITTEE_ID = "matchingCommitteeId";
    private static final String PLEASE_SLECT = "----Please Select----";
    private static final String VALID_SUBMISSION_REVIEW_TYPES = "validSubmissionReviewTypes";
    private static final String CHECK_LIST_EXSITS = "checkListExist";
    private static final String GET_REVIEW_TYPE = "getReviewType";
    private static final String REVIEW_TYPE = "reviewType";
    private static final String DYNA_SUBMISSION_TYPE = "dynaFormData[0].submissionType";
    private static final String PROTOCOL_SUBMIT_WITHOUT_COMMITTEE = "protocolSubmitWithoutSelectingCommittee";
    private static final String PROTOCOL_SUBMIT_WITHOUT_SCHEDULE = "protocolSubmitWithoutSelectingSchedule";
    private static final String PROTOCOL_SUBMIT_WITHOUT_REVIEWER = "protocolSubmitWithoutSelectingReviewer";
    private static final String GET_MAX_SUBMISSION_NUMBER = "getIacucMaxSubmissionNumber";
    private static final String GET_SUBMISSION_DETAILS = "getIacucSubmissionDetails";
    private static final String GET_PROTOCOL_INFO = "getIacucInfo";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private static final String VALID_PROTO_SUBMISSION_TYPE_CODE = "validProtoSubmissionTypeCode";
    private static final String VALID_PROTOCOL_REVIEW_TYPE_CODE = "validProtocolReviewTypeCode";
    private static final String CHECK_IS_PROTOCOL_COMPLETE = "checkIsIacucCompletes";
    private static final String PROTOCOL_INCOMPLETE = "protocolInComplete";
    private static final String VALIDATE_SUBMISSION = "validateSubmission";
    private static final String VALID_PROTO_REVIEW_DESCRIPTION = "validProtoReviewDescription";
    private static final String IS_COMPLETE = "isComplete";
    private static final String PARAMETER_MAINTANANCE_VALUE = "parameterMaintananceValue";
    private static final String NO_PRINICIPAL_INVESTIGATOR_CODE = "1002";
    private static final String NO_RESEARCH_AREA_CODE = "1004";
    private static final String NO_LOCATION_CODE = "1001";
    private static final String NO_LEAD_UNIT_CODE = "1003";
    private static final String UPD_PROTOCOL = "updIacuc";
    private static final String UPD_PROTOCOL_SUBMISSION = "updIacucSubmission";
    private static final String UPD_PROTOCOL_REVIEWER = "updIacucReviewer";
    private static final String UPD_PROTOCOL_EXPIDITED_CHKLST  = "updIacucExpiditedCheckList";
    private static final String UPD_PROTOCOL_EXEMPT_CHKLST = "updIacucExemptCheckList";
    private static final String SAVE_SUBMISSION_DETAILS = "saveIacucSubmissionDetails";
    private static final String EXPEDITED = "Expedited";
    private static final String EXEMPT = "Exempt";
    private static final String PROTOCOLEDIT_EXCEPTIONCODE = "protocoledit_exceptionCode.2501";
    //private static final String PROTOCOL_LIST = "protocolList";
    private static final String NEW_UPDATE_TIMESTAMP = "newUpdateTimestamp";
    private static final String AC_TYPE = "acType";
    private static final String SUBMISSION_NUMBER = "submissionNumber";
    private static final String UPDATE_TIMESTAMP = "updateTimestamp";
    private static final String ON = "on";
    private static final String PLEASE_SELECT_COMMITTEE = "pleaseSelectCommittee";
    private static final String PLEASE_SELECT_COMMITTEE_MSG = "protocolSubmission.pleaseSelectCommittee";
    private static final String NO_SCHEDULE_LIST_ERR_CODE = "noScheduleList";
    private static final String NO_SCHEDULE_LIST_ERR_MSG = "protocolSubmission.noScheduleList";
    private static final String USER = "user";
    // private UserInfoBean userInfoBean;
    
    //Added for protocol custom elements validation - start - 1
    private static final String PROTOCOL_CUSTOM_ELEMENT_CODE = "1005";
    private static final String PROTOCOL_CUSTOM_ELEMENT_ERR_CODE = "mandatoryCustomData";
    private static final String PROTOCOL_CUSTOM_ELEMENT_ERR_MSG = "protocolSubmission.customData";
    private static final String GET_PROTO_AM_MODULES = "getIacucAmendRenewModules";
    //Added for protocol custom elements validation - end - 1
    
    
    //Added for Case#3053 - Submission Details Type Qualifier Filter - Start
    private static final String GET_VALID_PROTO_SUB_TYPE_QUALS= "getValidIacucSubTypeQuals";
    private static final String VALID_PROTO_SUB_TYPE_QUALS = "validProtoSubTypeQuals";
    private static final String GET_TYPE_QUALIFIERS = "getTypeQualifiers";
    //Added for Case#3053 - Submission Details Type Qualifier Filter - End
    
    // 3282: Reviewer view of Protocol materials - Start
    private boolean validReviewerDetails = true;
    private static String GET_REVIEWERS = "getReviewers";
    // 3282: Reviewer view of Protocol materials - End
    
    // 4272: Maintain History of Questionnaires - Start
    private static final String MANDATORY_QUESTIONNAIRE_CODE = "1006";
    private static final String MANDATORY_QUESTIONNAIRE_ERR_CODE = "mandatoryQuestionnaire";
    private static final String MANDATORY_QUESTIONNAIRE_ERR_MSG = "protocolSubmission.mandatoryQuestionnaire";
    
    private static final String NEW_QUESTIONNAIRE_VERSION_AVAILABLE_CODE = "1007";
    private static final String NEW_QUESTIONNAIRE_VERSION_AVAILABLE_ERR_CODE = "newQnrVersionAvailable";
    private static final String NEW_QUESTIONNAIRE_VERSION_AVAILABLE_ERR_MSG = "protocolSubmission.newQnrVersionAvailable";
    private static final int SUBMITTED_TO_COMMITTEE = 102;
    private static final int PENDING = 101;
    // 4272: Maintain History of Questionnaires - End
    
    // COEUSQA-1724_ Implement validation based on rules in protocols_Start
    private final String PROTOCOL_MAP           = "IACUC_PROTOCOL_RULES_MAP";
    private final String VALIDATE_CHECK_FORM = "iacucProtocolValidationCheck";
    private static final String PROTOCOL_VALIDATION_FLAG = "protocolValidationFlag";
    private static final String RESPONSE_OBJECT = "vecResponseObject";
    private static final String IACUC_VALIDATION = "iacucProtocolValidationType";
    // COEUSQA-1724_ Implement validation based on rules in protocols_End
    private static final int SUB_STATUS_ROUTING_IN_PROGRESS = 100;
    //Added for COEUSQA-2444 -Change wording on mandatory questionnaire message in IRB 
    private static final String MESSAGES_FILE_NAME = "IACUCProtocolMessages";
    // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - Start
    private static final String SUBMITTED_TO_IACUC_STATUS = "101";
    private static final String ROUTING_IN_PROGRESS_STATUS = "108";
    // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - End
    //COEUSQA-3082 IACUC - Protocol does not appear under Pending/In Progress Reviews - Start
    private static final String IS_REVIEW_COMPLETE = "reviewComplete";
    //COEUSQA-3082 IACUC - Protocol does not appear under Pending/In Progress Reviews - End
    
    /** Creates a new instance of ProtocolSubmissionAction */
    public ProtocolSubmissionAction() {
    }
    
    public void cleanUp() {
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
//        this.actionMapping = actionMapping;
//        webTxnBean = new WebTxnBean();
//        actionMessages = new ActionMessages();
        HttpSession session = request.getSession();
        String MENU_ITEMS = "menuItems";
        String MENU_CODE = "menuCode";
//        userInfoBean = (UserInfoBean)session.getAttribute(USER);
        ProtocolSubmissionDynaBeanList protocolSubmissionDynaBeanList = (ProtocolSubmissionDynaBeanList)actionForm;
        //Added for Case#3053 -Start
        session.removeAttribute(GET_TYPE_QUALIFIERS);
        //Added for Case#3053 -End
        session.removeAttribute(GET_REVIEW_TYPE);
        session.removeAttribute(PROTOCOL_SUBMIT_WITHOUT_COMMITTEE);
        session.removeAttribute(PROTOCOL_SUBMIT_WITHOUT_SCHEDULE);
        session.removeAttribute(PROTOCOL_SUBMIT_WITHOUT_REVIEWER);
        session.removeAttribute("submissionConfirmation");
        
        // COEUSQA-1724_ Implement validation based on rules in protocols_Start
        session.removeAttribute(PROTOCOL_MAP);
        // COEUSQA-1724_ Implement validation based on rules in protocols_End
        
        //System.out.println("Request >>>>>"+request);
        ActionForward actionForward = getProtocolSubmissionData(protocolSubmissionDynaBeanList, request, response, actionMapping);
        
        Map mapMenuList = new HashMap();
        mapMenuList.put(MENU_ITEMS,CoeusliteMenuItems.IACUC_MENU_ITEMS);
        mapMenuList.put(MENU_CODE,CoeusliteMenuItems.SUBMIT_FOR_REVIEW);
        setSelectedMenuList(request, mapMenuList);
        //Added for Case#3053
        setSelectedTypeQualifier(protocolSubmissionDynaBeanList, request);
        //Added for Case#3053
        setSelectedReviewType(protocolSubmissionDynaBeanList, request);
        //Case# 3018 - Delete Pending Study 
        readSavedStatus(request);
        return actionForward;
    }
    /** This method will identify which request is comes from which path and
     *  navigates to the respective ActionForward
     *  @returns ActionForward object
     */
    private ActionForward getProtocolSubmissionData(ProtocolSubmissionDynaBeanList protocolSubmissionDynaBeanList,
            HttpServletRequest request, HttpServletResponse response, ActionMapping actionMapping)throws Exception{
        String navigator = EMPTY_STRING;
        if(actionMapping.getPath().equals(GET_PROTOCOL_SUBMISSION_DATA)){
            navigator = getSubmitForReveiwDetails(protocolSubmissionDynaBeanList, request);
        }else if(actionMapping.getPath().equals(GET_SCHEDULES_DETAILS)){
            navigator = getScheduleDetails(protocolSubmissionDynaBeanList, request);
        }else if(actionMapping.getPath().equals(GET_REVIEWER_DETAILS)){
            navigator = getReviewerDetails(protocolSubmissionDynaBeanList, request);
        }else if(actionMapping.getPath().equals(GET_COMMITTEE_DETAILS)){
            navigator = getCommitteeDetails(protocolSubmissionDynaBeanList,request);
        }else if(actionMapping.getPath().equals(GET_ALL_COMM_LIST)){
            navigator = getAllCommitteeList(protocolSubmissionDynaBeanList, request);
        }else if(actionMapping.getPath().equals(GET_CHECK_LIST)){
            navigator = getCheckList(protocolSubmissionDynaBeanList, request);
        }else if(actionMapping.getPath().equals(SAVE_PROTOCOL_SUBMISSION)){
            navigator = saveProtocolSubmission(protocolSubmissionDynaBeanList, request, response);
        }
        //Case# 3063 - Notice Page to the PI that a Protocol Submission to the IRB is successful 
        else if(actionMapping.getPath().equals(GET_SUBMISSION_CONFIRMATION)){
            navigator = getConfirmationMessage(request);
        }
        ActionForward actionForward = actionMapping.findForward(navigator);
        return actionForward;
    }
    
    /**
     * This method is used to get all the details of protocol submission.
     * @throws Exception
     * @return navigator
     */
    private String getSubmitForReveiwDetails(ProtocolSubmissionDynaBeanList protocolSubmissionDynaBeanList, HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        ActionMessages actionMessages = new ActionMessages();
        
        
        String navigator = EMPTY_STRING;
        String NO_PRINCIPAL_INVESTIGATOR_ERR_CODE = "noPrincipalInvestigator";
        String NO_PRINCIPAL_INVESTIGATOR_ERR_MSG = "protocolSubmission.noPrincipalInvestigator";
        String NO_RESEARCH_AREA_ERR_CODE = "noResearchArea";
        String NO_RESEARCH_AREA_ERR_MSG = "protocolSubmission.noResearchArea";
        String NO_LOCATION_ERR_CODE = "noLocation";
        String NO_LOCATION_ERR_MSG = "protocolSubmission.noLocation";
        String NO_LEAD_UNIT_ERR_CODE = "noLeadUnit";
        String NO_LEAD_UNIT_ERR_MSG = "protocolSubmission.noLeadUnit";
        String NO_SUBMISSION_RIGHTS_CODE = "noSubmissionRights";
        String NO_SUBMISSION_RIGHTS_MSG = "protocolSubmission.noSubmissionRights";
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        HashMap hmProtocolData = new HashMap();
        List lstSubmissionData = new ArrayList();
        DynaActionForm dynaForm = protocolSubmissionDynaBeanList.getDynaForm(request,SUBMIT_FOR_REVIEW_FORM);
        dynaForm.set(PROTOCOL_NUMBER,protocolNumber);
        hmProtocolData.put(PROTOCOL_NUMBER,protocolNumber);
        hmProtocolData.put(PARAMETER_NAME, IACUC_COMM_SELECTION_DURING_SUBMISSION);
        Vector vecMatchingCommForProto  = new Vector();
        String committeeId = EMPTY_STRING;
        String paramValue =EMPTY_STRING;
        String isProtocolComplete = EMPTY_STRING;
        //check for protocol is complete or Incomplete
        hmProtocolData.put(SEQUENCE_NUMBER, new Integer(Integer.parseInt(sequenceNumber)));
        Hashtable htProtocolDetail = (Hashtable)webTxnBean.getResults(request,CHECK_IS_PROTOCOL_COMPLETE, hmProtocolData);
        HashMap hmProtocolDetail = (HashMap) htProtocolDetail.get(CHECK_IS_PROTOCOL_COMPLETE);
        if(hmProtocolDetail != null && hmProtocolDetail.size() > 0){
            isProtocolComplete = (String)hmProtocolDetail.get(IS_COMPLETE);
        }
        //Added for protocol custom elements validation - start - 2
        //If its an amendment protocol, we have to validate mandatory custom data ONLY if Others is checked        
        if(protocolNumber != null && protocolNumber.length() > 10){
            boolean othersChecked = isOthersChecked(request, protocolNumber);
            if(!othersChecked){
                //Skip validation if Others is not checked
                StringTokenizer stokenMessage = new StringTokenizer(isProtocolComplete,",");
                while (stokenMessage.hasMoreTokens()){
                    String message = stokenMessage.nextToken();
                    if(message != null && !message.equals(EMPTY_STRING) && message.equals("1005")){
                        //Indicates the protocol has lead unit and PI, but does not have custom data                        
                        isProtocolComplete = "0";                            
                        break;
                    }else{
                        //Indicates the protocol may not have lead unit and PI
                        //Retain those error codes and take off custom data error code
                        int strLength = isProtocolComplete.length();                        
                        int value = isProtocolComplete.indexOf("1005");
                        if(value != -1){
                            isProtocolComplete = isProtocolComplete.substring(0, strLength-5);
                        }                        
                        break;
                    }            
                }
              
            }
        }            
        //Added for protocol custom elements validation - end - 2   
        
        // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - Start
        //COEUSDEV-325:Ability to route a protocol to PI if the person submitting the protocol IS NOT the PI
        //Fetching user id to initialize QuestionnaireTxnBean
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String userId = userInfoBean.getUserId();
        QuestionnaireTxnBean questionnaireTxnBean = new QuestionnaireTxnBean(userId);
        // COEUSDEV-86: Questionnaire for a Submission - Start
//        Vector vecUnfilledQnr = questionnaireTxnBean.fetchQuestionnaireCompletedForModule(protocolNumber, 7, "0", Integer.parseInt(sequenceNumber));
        int subModuleCode = 0;
        if(protocolNumber.length() > 10 && isIacucProtoAmendmentRenewal(protocolNumber.charAt(10))){
            subModuleCode = CoeusLiteConstants.IRB_SUB_MODULE_CODE_FOR_AMENDMENT_RENEWAL;
        } else {
            subModuleCode = 0;
        }
        Vector vecUnfilledQnr =
                questionnaireTxnBean.fetchQuestionnaireCompletedForModule(
                protocolNumber, ModuleConstants.IACUC_MODULE_CODE, String.valueOf(subModuleCode),
                Integer.parseInt(sequenceNumber));
        // COEUSDEV-86: Questionnaire for a Submission - End
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
                // Added for COEUSQA-2444 -Change wording on mandatory questionnaire message in IRB(IACUC)_Start
                //String validationMessage = "Fill the following mandatory forms before submitting the protocol to IACUC";
                MessageResources messages = MessageResources.getMessageResources(MESSAGES_FILE_NAME);
                String validationMessage = messages.getMessage(MANDATORY_QUESTIONNAIRE_ERR_MSG);
                // Added for COEUSQA-2444 -Change wording on mandatory questionnaire message in IRB(IACUC)_End
                
               StringTokenizer stokenMessage = new StringTokenizer(mandatoryQnr,"~");
                while (stokenMessage.hasMoreTokens()){
                    String message = stokenMessage.nextToken();
                    validationMessage = validationMessage + "<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + message;
                }
                request.setAttribute("mandatoryQnrMessage", validationMessage);
            }
            
            if(incompleteQnrPresent){
                
                String validationMessage = "Complete the following forms before submitting the protocol to IACUC";
                
                StringTokenizer stokenMessage = new StringTokenizer(incompleteQnr,"~");
                while (stokenMessage.hasMoreTokens()){
                    String message = stokenMessage.nextToken();
                    validationMessage = validationMessage + "<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + message;
                }
                request.setAttribute("incompleteQnrMessage", validationMessage);
            }
            
            if(newVersionQnrPresent){
                String validationMessage = "Fill the latest version of the following forms before submitting the protocol to IACUC" ;
                
                StringTokenizer stokenMessage = new StringTokenizer(newVersionQnr,"~");
                while (stokenMessage.hasMoreTokens()){
                    String message = stokenMessage.nextToken();
                    validationMessage = validationMessage + "<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + message;
                }
                request.setAttribute("newVersionQnrMessage", validationMessage);
            }
            if(!requiredQnrFilled){
                navigator =  PROTOCOL_INCOMPLETE;
            }
        }
        
        // COEUSQA-1724_ Implement validation based on rules in protocols_Start
        Vector vecResponseObject = null;
        HashMap  hmData = checkProtocolValidationRules(protocolNumber,sequenceNumber,session,request);
        if(!((Boolean)hmData.get(PROTOCOL_VALIDATION_FLAG)).booleanValue()) {
            vecResponseObject = (Vector) hmData.get(RESPONSE_OBJECT);
        }
        // COEUSQA-1724_ Implement validation based on rules in protocols_End
        
        //check protcol submission rights
        boolean canSubmit =  checkSubmitProtocolRights(request);
        // If a Protocol is Complete
        if(isProtocolComplete!=null &&  !isProtocolComplete.equals(EMPTY_STRING) && isProtocolComplete.equalsIgnoreCase("0") && canSubmit 
                && requiredQnrFilled){
        // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - End    
            //Added for IACUC Changes - Start
            hmProtocolData.put("matchingCommitteeTypeCode", new Integer(CoeusConstants.IACUC_COMMITTEE_TYPE_CODE));
            //Added for IACUC Changes - End
            Hashtable htSubmissionDetail =
                    (Hashtable)webTxnBean.getResults(request,GET_SUBMIT_FOR_REVIEW_DETAILS ,hmProtocolData);
            
            if(htSubmissionDetail !=null && htSubmissionDetail.size()>0){
                Vector vecSubmissionTypes = (Vector)htSubmissionDetail.get(GET_SUBMISSION_TYPES);
                Vector vecSubTypeQualifiers = (Vector)htSubmissionDetail.get(GET_SUBMISSION_TYPE_QUALIFIERS);
                Vector vecProtocolReveiwTypes = (Vector)htSubmissionDetail.get(GET_PROTOCOL_REVIEW_TYPES);
                Vector vecExpeditedReviewCheckList = (Vector)htSubmissionDetail.get(GET_EXPEDITED_REVIEW_CHECK_LIST);
                Vector vecExemptStudiesCheckList = (Vector) htSubmissionDetail.get(GET_EXEMPT_STUDIES_CHECK_LIST);
                Vector vecProtoSubmissionCommittee = (Vector)htSubmissionDetail.get(GET_PROTO_SUBMISSION_COMMITTEE);
                vecMatchingCommForProto = (Vector)htSubmissionDetail.get(GET_MATCHING_COMM_FOR_PROTO);
                Vector vecValidProtoSubReviewTypes = (Vector)htSubmissionDetail.get(GET_VALID_PROTO_SUB_REVIEW_TYPES);
                //Added for Case#3053 - Submission Details Type Qualifier Filter - Start
                Vector vecValidProtoSubTypeQualifiers = (Vector)htSubmissionDetail.get(GET_VALID_PROTO_SUB_TYPE_QUALS);
                //Added for Case#3053 - Submission Details Type Qualifier Filter - End
                Vector vecProtocolReviewerList = (Vector)htSubmissionDetail.get(GET_PROTOCOL_REVIEWER_LIST);
                
                HashMap hmParamValueExists = (HashMap)htSubmissionDetail.get(GET_PARAMETER_VALUE);
                if( hmParamValueExists !=null && hmParamValueExists.size() > 0){
                    paramValue = (String) hmParamValueExists.get(PARAMETER_VALUE);
                }
                
                if(vecProtoSubmissionCommittee !=null && vecProtoSubmissionCommittee.size()>0){
                    DynaValidatorForm DynaValidatorForm = (DynaValidatorForm)  vecProtoSubmissionCommittee.elementAt(0);
                    committeeId = (String)DynaValidatorForm.get(COMMITTEE_ID);
                    String committeeName = (String)DynaValidatorForm.get(COMMITTEE_NAME);
                    dynaForm.set(COMMITTEE_ID,committeeId);
                    dynaForm.set(COMMITTEE_NAME,committeeName);
                }
                
                //get the valid protocol sub review types
                HashMap hmCombinedReviewTypes = new HashMap();
                if(vecValidProtoSubReviewTypes !=null && vecValidProtoSubReviewTypes.size()>0){
                    for(int index=0; index<vecValidProtoSubReviewTypes.size();index++){
                        DynaActionForm dynaActionForm = (DynaActionForm)vecValidProtoSubReviewTypes.get(index);
                        Integer submissionTypeCode = (Integer)dynaActionForm.get(VALID_PROTO_SUBMISSION_TYPE_CODE);
                        HashMap hmReviewTypeLevels = new HashMap();
                        for(int revIndex=0; revIndex < vecValidProtoSubReviewTypes.size(); revIndex++){
                            DynaActionForm revDynaForm = (DynaActionForm)vecValidProtoSubReviewTypes.get(revIndex);
                            Integer subTypeCode = (Integer)revDynaForm.get(VALID_PROTO_SUBMISSION_TYPE_CODE);
                            Integer revTypeCode = (Integer)revDynaForm.get(VALID_PROTOCOL_REVIEW_TYPE_CODE);
                            String revTypeDesc = (String)revDynaForm.get(VALID_PROTO_REVIEW_DESCRIPTION);
                            if(submissionTypeCode.toString().equals(subTypeCode.toString())){
                                //Modified for Case# 3038 - Filtering Submission Review Type - Start
                                if(revDynaForm.get("submissionReviewTypeFlag")!= null && revDynaForm.get("submissionReviewTypeFlag").equals("Y")){
                                hmReviewTypeLevels.put(revTypeCode, revTypeDesc);
                                hmCombinedReviewTypes.put(submissionTypeCode,hmReviewTypeLevels);
                                }//Modified for Case# 3038 - Filtering Submission Review Type - End
                            }
                        }
                    }
                    session.setAttribute(VALID_SUBMISSION_REVIEW_TYPES, hmCombinedReviewTypes);
                //Added for Case# 3038 - Filtering Submission Review Type - Start
                }else{
                    session.setAttribute(VALID_SUBMISSION_REVIEW_TYPES, hmCombinedReviewTypes);
                }
                //Added for Case# 3038 - Filtering Submission Review Type - End
                
                //Added for Case#3053 - Submission Details Type Qualifier Filter - Start
                HashMap hmValidTypeQualifiers = new HashMap();
                if(vecValidProtoSubTypeQualifiers !=null && vecValidProtoSubTypeQualifiers.size()>0){
                    for(int index=0; index<vecValidProtoSubTypeQualifiers.size();index++){
                        DynaActionForm dynaActionForm = (DynaActionForm)vecValidProtoSubTypeQualifiers.get(index);
                        Integer submissionTypeCode = (Integer)dynaActionForm.get(VALID_PROTO_SUBMISSION_TYPE_CODE);
                        HashMap hmTypeQualLevels = new HashMap();
                        for(int revIndex=0; revIndex < vecValidProtoSubTypeQualifiers.size(); revIndex++){
                            DynaActionForm revDynaForm = (DynaActionForm)vecValidProtoSubTypeQualifiers.get(revIndex);
                            Integer subTypeCode = (Integer)revDynaForm.get(VALID_PROTO_SUBMISSION_TYPE_CODE);
                            Integer revTypeCode = (Integer)revDynaForm.get("validProtocolTypeQualCode");
                            String revTypeDesc = (String)revDynaForm.get("validProtoTypeQualDescription");
                            if(submissionTypeCode.toString().equals(subTypeCode.toString())){
                                hmTypeQualLevels.put(revTypeCode, revTypeDesc);
                                hmValidTypeQualifiers.put(submissionTypeCode,hmTypeQualLevels);
                            }
                        }
                    }
                    session.setAttribute("validProtoSubTypeQuals", hmValidTypeQualifiers);
                }else{
                    session.setAttribute("validProtoSubTypeQuals", hmValidTypeQualifiers);
                }
                //Added for Case#3053 - Submission Details Type Qualifier Filter - End
                
                session.removeAttribute(GET_REVIEW_TYPE);
                session.removeAttribute(PROTOCOL_SUBMIT_WITHOUT_COMMITTEE);
                session.setAttribute(GET_SUBMISSION_TYPES, vecSubmissionTypes);
                session.setAttribute(GET_SUBMISSION_TYPE_QUALIFIERS, vecSubTypeQualifiers);
                session.setAttribute(GET_PROTOCOL_REVIEW_TYPES, vecProtocolReveiwTypes);
                session.setAttribute(GET_EXPEDITED_REVIEW_CHECK_LIST, vecExpeditedReviewCheckList);
                session.setAttribute(GET_EXEMPT_STUDIES_CHECK_LIST, vecExemptStudiesCheckList);
                session.setAttribute(GET_PROTO_SUBMISSION_COMMITTEE, vecProtoSubmissionCommittee);
                session.setAttribute(GET_MATCHING_COMM_FOR_PROTO, vecMatchingCommForProto);
                //Modified for Case# 3038 - Filtering Submission Review Type - Start
                if(vecValidProtoSubReviewTypes != null && vecValidProtoSubReviewTypes.size()>0){
                    session.setAttribute(GET_VALID_PROTO_SUB_REVIEW_TYPES, vecValidProtoSubReviewTypes);
                }else{
                    session.setAttribute(GET_VALID_PROTO_SUB_REVIEW_TYPES, new Vector());
                }
                //Modified for Case# 3038 - Filtering Submission Review Type - End
                //Commented for Case# 3038 - Filtering Submission Review Type 
//                session.setAttribute(GET_VALID_PROTO_SUB_REVIEW_TYPES, vecValidProtoSubReviewTypes);
                
                //Added for Case#3053 - Submission Details Type Qualifier Filter - Start
                if(vecValidProtoSubTypeQualifiers != null && vecValidProtoSubTypeQualifiers.size()>0){
                    session.setAttribute(GET_VALID_PROTO_SUB_TYPE_QUALS, vecValidProtoSubTypeQualifiers);
                }else{
                    session.setAttribute(GET_VALID_PROTO_SUB_TYPE_QUALS, new Vector());
                }
                //Added for Case#3053 - Submission Details Type Qualifier Filter - End
                
                session.setAttribute(GET_PROTOCOL_REVIEWER_LIST, vecProtocolReviewerList);
                session.setAttribute(GET_PROTOCOL_SUBMISSION_MODE, paramValue);
            }
            
            if(request.getParameter("PAGE") !=null && request.getParameter("PAGE").equals("Committee")){
                committeeId = request.getParameter(COMMITTEE_ID);
            }
            setSelectedCommittee(vecMatchingCommForProto, committeeId);
            lstSubmissionData.add(dynaForm);
            protocolSubmissionDynaBeanList.setList(lstSubmissionData);
            protocolSubmissionDynaBeanList.setBeanList(vecMatchingCommForProto);
            request.getSession().setAttribute(SUBMIT_FOR_REVIEW_LIST, protocolSubmissionDynaBeanList);
            request.setAttribute(PARAMETER_MAINTANANCE_VALUE, paramValue);
            
            // COEUSQA-1724_ Implement validation based on rules in protocols_Start
//                        navigator = SUCCESS; 
            String onlyWarning = (String)request.getParameter("warnPresent");
            if(vecResponseObject != null && vecResponseObject.size()>0  ){
                session.setAttribute(PROTOCOL_MAP,vecResponseObject);                
                if("Y".equals(onlyWarning) ){
                    navigator = SUCCESS;
                }else{
                    navigator =  IACUC_VALIDATION;
                }
            }else{
                if("Y".equals(onlyWarning) ){
                    navigator = SUCCESS;
                }else{
                    navigator =  IACUC_VALIDATION;
                }
            }
            // COEUSQA-1724_ Implement validation based on rules in protocols_End
            
        }//end of protocol complete
        else if(isProtocolComplete!=null &&  !isProtocolComplete.equals(EMPTY_STRING) && !isProtocolComplete.equalsIgnoreCase("0") && canSubmit ){
            // Else if protocol is not complete return the error codes..
            Vector vecProtoCompleteMsg = new Vector();
            StringTokenizer stokenMessage = new StringTokenizer(isProtocolComplete,",");
            while (stokenMessage.hasMoreTokens()){
                String message = stokenMessage.nextToken();
                vecProtoCompleteMsg.addElement(message);
            }
            if(vecProtoCompleteMsg !=null && vecProtoCompleteMsg.size()>0){
                for(int index=0 ; index < vecProtoCompleteMsg.size() ; index++){
                    String errorMsg = (String)vecProtoCompleteMsg.elementAt(index);
                    if(errorMsg !=null && errorMsg.equals(NO_PRINICIPAL_INVESTIGATOR_CODE)){
                        actionMessages.add(NO_PRINCIPAL_INVESTIGATOR_ERR_CODE,
                                new ActionMessage(NO_PRINCIPAL_INVESTIGATOR_ERR_MSG));
                        saveMessages(request, actionMessages);
                    }else if(errorMsg !=null && errorMsg.equals(NO_RESEARCH_AREA_CODE)){
                        actionMessages.add(NO_RESEARCH_AREA_ERR_CODE,
                                new ActionMessage(NO_RESEARCH_AREA_ERR_MSG));
                        saveMessages(request, actionMessages);
                    }else if(errorMsg !=null && errorMsg.equals(NO_LOCATION_CODE)){
                        actionMessages.add(NO_LOCATION_ERR_CODE,
                                new ActionMessage(NO_LOCATION_ERR_MSG));
                        saveMessages(request, actionMessages);
                    }else if(errorMsg !=null && errorMsg.equals(NO_LEAD_UNIT_CODE)){
                        actionMessages.add(NO_LEAD_UNIT_ERR_CODE,
                                new ActionMessage(NO_LEAD_UNIT_ERR_MSG));
                        saveMessages(request, actionMessages);
                        //Added for protocol custom elements validation - start - 3
                    }else if(errorMsg != null && errorMsg.equals(PROTOCOL_CUSTOM_ELEMENT_CODE)){
                        actionMessages.add(PROTOCOL_CUSTOM_ELEMENT_ERR_CODE,
                                new ActionMessage(PROTOCOL_CUSTOM_ELEMENT_ERR_MSG));
                        saveMessages(request, actionMessages);
                    }
                    // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - Start
                    //Added for protocol custom elements validation - end - 3
//                    // 4272: Maintain History of Questionnaires - Start
//                    else if(errorMsg != null && errorMsg.equals(MANDATORY_QUESTIONNAIRE_CODE)){
//                        actionMessages.add(MANDATORY_QUESTIONNAIRE_ERR_CODE,
//                                new ActionMessage(MANDATORY_QUESTIONNAIRE_ERR_MSG));
//                        saveMessages(request, actionMessages);
//                    } else if(errorMsg != null && errorMsg.equals(NEW_QUESTIONNAIRE_VERSION_AVAILABLE_CODE)){
//                        actionMessages.add(NEW_QUESTIONNAIRE_VERSION_AVAILABLE_ERR_CODE,
//                                new ActionMessage(NEW_QUESTIONNAIRE_VERSION_AVAILABLE_ERR_MSG));
//                        saveMessages(request, actionMessages);
//                    }
//                    // 4272: Maintain History of Questionnaires - End
                    // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - End
                }
            }
            navigator =  PROTOCOL_INCOMPLETE;
        }else if(!canSubmit){
            actionMessages.add(NO_SUBMISSION_RIGHTS_CODE,
                    new ActionMessage(NO_SUBMISSION_RIGHTS_MSG));
            saveMessages(request, actionMessages);
            navigator =  PROTOCOL_INCOMPLETE;
        }
        
        return navigator;
    }
    
    /**
     * This method is used to save a protocol for review
     * @param protocolSubmissionDynaBeanList
     * @throws Exception
     * @return navigator
     */
    private String saveProtocolSubmission(ProtocolSubmissionDynaBeanList protocolSubmissionDynaBeanList,
            HttpServletRequest request, HttpServletResponse response)throws Exception{
        
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        ActionMessages actionMessages  = new ActionMessages();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String navigator = SUCCESS;
        String submissionNumber = EMPTY_STRING;
        String SUB_NUM = "sub_num";
        boolean isValidateSuccess = false;
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        String reviewType = request.getParameter(REVIEW_TYPE);
        // Check if lock exists or not
        LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()), request);
        boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
        LockBean lockData = getLockedData(CoeusLiteConstants.IACUC_PROTO_LOCK_STR+lockBean.getModuleNumber(), request);
        if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
            session.removeAttribute(PROTOCOL_SUBMIT_WITHOUT_COMMITTEE);
            session.removeAttribute(PROTOCOL_SUBMIT_WITHOUT_SCHEDULE);
            isValidateSuccess = validateSubmission(protocolSubmissionDynaBeanList, request);
            // 3282: Reviewer view of Protocol materials  - Start
            if(!validReviewerDetails){
                return GET_REVIEWERS;
            }
            // 3282: Reviewer view of Protocol materials  -End
            if(isValidateSuccess){
                HashMap hmMaxSubDetails = new HashMap();
                HashMap hmSaveProtocolSubmission = new HashMap();
                hmMaxSubDetails.put(PROTOCOL_NUMBER,protocolNumber);
                Hashtable htSubmissionDetail = (Hashtable)webTxnBean.getResults(request, GET_SUBMISSION_DETAILS,hmMaxSubDetails);
                Vector vecProtocolInfo = (Vector) htSubmissionDetail.get(GET_PROTOCOL_INFO);
                HashMap hmMaxSubmissionNumber = (HashMap)htSubmissionDetail.get(GET_MAX_SUBMISSION_NUMBER);
                if( hmMaxSubmissionNumber !=null && hmMaxSubmissionNumber.size() > 0){
                    submissionNumber = (String) hmMaxSubmissionNumber.get(SUB_NUM);
                }
                hmSaveProtocolSubmission.put(UPD_PROTOCOL, prepareProtocolInfo(vecProtocolInfo));
                hmSaveProtocolSubmission.put(UPD_PROTOCOL_SUBMISSION, prepareProtocolSubmission(protocolSubmissionDynaBeanList, vecProtocolInfo, submissionNumber, request));
                
                // Check for reviewer present for updProtocolReviewer
                Vector vecCheckReviwerExist = getProtocolReviewer(protocolSubmissionDynaBeanList, request);
                // COEUSQA-2045: Coeus 4.4 QA - IRB Online Review - Review Notification - Start
                Vector reviewerDetails = null;
                if(vecCheckReviwerExist !=null && vecCheckReviwerExist.size()>0){
                    reviewerDetails = prepareProtocolReviewer(protocolSubmissionDynaBeanList,vecProtocolInfo,submissionNumber, request);
                   // hmSaveProtocolSubmission.put(UPD_PROTOCOL_REVIEWER, prepareProtocolReviewer(protocolSubmissionDynaBeanList,vecProtocolInfo,submissionNumber, request));
                     hmSaveProtocolSubmission.put(UPD_PROTOCOL_REVIEWER, reviewerDetails);
                // COEUSQA-2045: Coeus 4.4 QA - IRB Online Review - Review Notification - End     
                }
                // Check for CheckList exits or not
//                Vector vecCheckListExist = getProtocolCheckList(protocolSubmissionDynaBeanList, request);
//                if(vecCheckListExist !=null && vecCheckListExist.size()>0){
//                    String expiditedType = UPD_PROTOCOL_EXPIDITED_CHKLST;
//                    String exemptType = UPD_PROTOCOL_EXEMPT_CHKLST;
//                    String checkListType = EMPTY_STRING;
//                    if((reviewType !=null && !reviewType.equals(EMPTY_STRING))&& (reviewType.equals(EXPEDITED) || reviewType.equals("2") )){
//                        checkListType = expiditedType;
//                    }else if((reviewType !=null && !reviewType.equals(EMPTY_STRING))&& (reviewType.equals(EXEMPT) || reviewType.equals("3"))){
//                        checkListType = exemptType ;
//                    }
//                    if(checkListType!=null && !checkListType.equals(EMPTY_STRING)){
//                        hmSaveProtocolSubmission.put(checkListType, prepareProtocolCheckList(protocolSubmissionDynaBeanList,vecProtocolInfo,submissionNumber, request));
//                    }
//                    
//                }
                //Code added for Case#2785 - Protocol Routing - starts
                // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - Start
                String protocolStatusCode = SUBMITTED_TO_IACUC_STATUS;
                // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - End
                DynaActionForm dynaUploadActionForm = (DynaActionForm) vecProtocolInfo.get(0);
                Integer seqNo = new Integer(dynaUploadActionForm.get(SEQUENCE_NUMBER).toString());
                ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
                String protocolLeadUnit = protocolDataTxnBean.getLeadUnitForProtocol(protocolNumber, seqNo.intValue());
                String userId = userInfoBean.getUserId();
                ProtocolSubmissionUpdateTxnBean  protocolSubmissionUpdateTxnBean = new ProtocolSubmissionUpdateTxnBean(userId);                
                webTxnBean.getResultsData(request, SAVE_SUBMISSION_DETAILS, hmSaveProtocolSubmission);
                Vector vecMaps = new Vector();
                try{
                    vecMaps = protocolSubmissionUpdateTxnBean.submitToApprove(protocolNumber, seqNo.intValue(), protocolLeadUnit, "S");
                } catch(DBException e){
                    actionMessages.add("errorMessage",
                            new ActionMessage("protocolSubmission.errorMessage", e.getUserMessage()));
                    saveMessages(request, actionMessages);                    
                    return navigator;
                }

                if(vecMaps != null && vecMaps.size() > 0 && !vecMaps.get(0).equals(new Integer(0))){
                    if(hmSaveProtocolSubmission.get(UPD_PROTOCOL_SUBMISSION) != null){
                        DynaActionForm protcolSubmissiondynaForm =(DynaActionForm) 
                            ((Vector)hmSaveProtocolSubmission.get(UPD_PROTOCOL_SUBMISSION)).get(0);
                        protcolSubmissiondynaForm.set("submissionStatusCode",new Integer(SUB_STATUS_ROUTING_IN_PROGRESS));
                        int protoSubmissionNumber = ((Integer)protcolSubmissiondynaForm.get(SUBMISSION_NUMBER)).intValue();
                        protocolSubmissionUpdateTxnBean.updateProtoSubmissionStatus(protocolNumber,seqNo,protoSubmissionNumber,SUB_STATUS_ROUTING_IN_PROGRESS);

                    }
                    session.setAttribute("mapsFound"+session.getId(), vecMaps.get(0).toString());
                    // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - Start
                    if(hmSaveProtocolSubmission.get(UPD_PROTOCOL) != null){
                        DynaActionForm protcolSubmissiondynaForm =(DynaActionForm)
                        ((Vector)hmSaveProtocolSubmission.get(UPD_PROTOCOL)).get(0);
                        protocolStatusCode = ROUTING_IN_PROGRESS_STATUS;
                        protcolSubmissiondynaForm.set("protocolStatusCode",protocolStatusCode);
                        ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(userId);
                        protocolUpdateTxnBean.updateProtocolStatus(protocolNumber,seqNo,Integer.parseInt(ROUTING_IN_PROGRESS_STATUS));
                        
                    }
                    // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - End

                }
                //Code modified for Case#2785 - Protocol Routing - ends                
                // Save all protocol submission details
                
                
                //Update the protocol document status for Coeus 4.3 Upload Documents Enhancement -Start
//                String userId = userInfoBean.getUserId();
//                ProtocolSubmissionUpdateTxnBean  protocolSubmissionUpdateTxnBean = new ProtocolSubmissionUpdateTxnBean(userId);
                //List lstBeanList = (List) protocolSubmissionDynaBeanList.getList();
                //DynaActionForm protcolSubmissiondynaForm = (DynaActionForm)lstBeanList.get(0);
//                Integer seqNo = (Integer) dynaUploadActionForm.get(IACUC_SEQUENCE_NUMBER);
                protocolSubmissionUpdateTxnBean.protocolUploadStatusChanged(protocolNumber,seqNo.intValue());
                // Coeus 4.3 Upload Documents Enhancement -End
                //sava protocol actions details
                saveProtocolActions(vecProtocolInfo, submissionNumber, request);
                
                ActionMessage message = new ActionMessage(PROTOCOLEDIT_EXCEPTIONCODE);
                actionMessages.add(ActionMessages.GLOBAL_MESSAGE,message);
                saveMessages(request,actionMessages);
                session.setAttribute(CoeusLiteConstants.MODE+session.getId(), CoeusLiteConstants.DISPLAY_MODE);
                session.removeAttribute(VALIDATE_SUBMISSION);
                
                //Added for Amendment/Renewal protocols - start - 1
                //Set the protocol status code to 101 (Submitted to IRB)
                session.removeAttribute("statusCode");
//                session.setAttribute("statusCode", "101");
                session.setAttribute("statusCode", protocolStatusCode);
                //Added for Amendment/Renewal protocols - end - 1
                
                // navigator = PROTOCOL_LIST;
//                String url =  "/getProtocolData.do?PAGE=G&protocolNumber=";
//                String url =  "/getProtocolData.do?PAGE=G&SEARCH_ACTION=SEARCH_ACTION&protocolNumber=";
//                url += protocolNumber;
//                String url =  "/getProtocolData.do?SEARCH_ACTION=SEARCH_ACTION&PAGE=G&protocolNumber=";
//                url += protocolNumber;
//                url +="&sequenceNumber=";
//                url += seqNo;
                //Commented Email Notification for to bring the Submission confirmation and implemented Email Notification in jsp part.
                /* Coeus4.3 Enhancement modified for Email Notification Implementation - Start */
//                String url = "/mailAction.do";
         //       session.setAttribute("MODULE_CODE", ModuleConstants.IACUC_MODULE_CODE+"");   
          //      session.setAttribute("IACUC_ACTION_CODE", "101"); // Action Code for Protocol Submission
                //Email Notification Implementation - End
//                RequestDispatcher rd = request.getRequestDispatcher(url);
//                rd.forward(request,response);
                session.setAttribute("submissionConfirmation",YES);
                //Case# 3018 - Delete Pending Study 
                //Code modified for Case#2785 - Protocol Routing
//                 setMenuForAmendRenew(protocolNumber, request);
                setMenuForAmendRenew(protocolNumber, ""+seqNo.intValue(), request);
                // COEUSQA-2045: Coeus 4.4 QA - IRB Online Review - Review Notification 
                if(reviewerDetails != null ){
                    sendMailToReviewers(reviewerDetails, protocolNumber, seqNo);
                }
                return SUCCESS ;
                
                
            } //end of validate
        } else {
            String errMsg = "release_lock_for";
            ActionMessages messages = new ActionMessages();
            messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
            saveMessages(request, messages);
        }
        return navigator;
    }
    
    /**
     * This method is to prepare a Protocol Info datas for Update
     * @param vecProtocolInfo
     * @throws Exception
     * @return updated vecProtocolInfo
     */
    private Vector prepareProtocolInfo(Vector vecProtocolInfo)throws Exception{
        String APPLICATION_DATE = "applicationDate";
        String PROTOCOL_STATUS_CODE = "protocolStatusCode";
        Vector vecProtoDetails = new Vector();
        DateUtils dateUtils = new DateUtils();
        if(vecProtocolInfo !=null && vecProtocolInfo.size()>0){
            DynaActionForm dynaActionForm = (DynaActionForm) vecProtocolInfo.get(0);
            String  applicationDate = dateUtils.formatDate(dynaActionForm.get(APPLICATION_DATE).toString(),SIMPLE_DATE_FORMAT);
            //Updating expiration and approval date bug fix - start
            String  expirationDate = (String)dynaActionForm.get("expirationDate");
            String  approvalDate = (String)dynaActionForm.get("approvalDate");             
            if(expirationDate != null && approvalDate != null){
                expirationDate = dateUtils.formatDate(expirationDate, SIMPLE_DATE_FORMAT);
                approvalDate = dateUtils.formatDate(approvalDate, SIMPLE_DATE_FORMAT);
            }
            //Updating expiration and approval date bug fix - end
            //Set Submitted to IRB code '101'
            dynaActionForm.set(APPLICATION_DATE,applicationDate);
            dynaActionForm.set("approvalDate", approvalDate);             
            dynaActionForm.set("expirationDate", expirationDate);
            dynaActionForm.set("versionNumber", (String)dynaActionForm.get("versionNumber"));
            dynaActionForm.set(PROTOCOL_STATUS_CODE, new String("101"));
            dynaActionForm.set(NEW_UPDATE_TIMESTAMP, prepareTimeStamp().toString() );
            dynaActionForm.set(AC_TYPE,TypeConstants.UPDATE_RECORD);
            vecProtoDetails.add(dynaActionForm);
        }
        return vecProtoDetails;
    }
    /**
     * This method is used to prepare a Protocol Submission details for save functinality.
     * @param protocolSubmissionDynaBeanList
     * @param vecProtocolInfo
     * @param submissionNumber
     * @throws Exception
     * @return updated vecProtocolSubmission
     */
    private Vector prepareProtocolSubmission(ProtocolSubmissionDynaBeanList protocolSubmissionDynaBeanList,
            Vector vecProtocolInfo, String submissionNumber, HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        Vector vecProtocolSubmission = new Vector();
        String SUBMISSION_STATUS_CODE = "submissionStatusCode";
        String TYPE_QUALIFIER  = "typeQualifier";
        //String SUBMISSION_TYPE_QUAL_CODE  = "submissionTypeQualCode";
        String PROTOCOL_REVIEW_TYPE_CODE = "protocolReviewTypeCode";
        String SUBMISSION_TYPE_CODE = "submissionTypeCode";
        String SUBMISSION_DATE = "submissionDate";
        String YES_VOTE_COUNT = "yesVoteCount";
        String NO_VOTE_COUNT = "noVoteCount";
        String ABSTAINER_COUNT = "abstainerCount";
        String STR_SUBMISSION_TYPE_QUAL_CODE = "strSubmissionTypeQualCode";
        // String selectedCommitteeId = request.getParameter(SELECT_COMMITTEE);
        String committeParamId = request.getParameter(COMMITTEE_ID);
        String scheduleId = request.getParameter(SCHEDULE_ID);
        //Combo boxes values
        String submissionTypeCode = request.getParameter(DYNA_SUBMISSION_TYPE);
        String reviewType = request.getParameter(REVIEW_TYPE);
        // Added for COEUSQA-3123_Type qualifier doesn't stick when selected in Lite "submit to IRB" screen_start
        String typeQualifier  = request.getParameter("typeQualifier");
        String typeQualCode = EMPTY_STRING;
        // Added for COEUSQA-3123_Type qualifier doesn't stick when selected in Lite "submit to IRB" screen_end
        // String committeeId = EMPTY_STRING;
        String reviewTypeCode = EMPTY_STRING;
        List lstBeanList = (List) protocolSubmissionDynaBeanList.getList();
        DynaActionForm protcolSubmissiondynaForm = (DynaActionForm)lstBeanList.get(0);
        DynaActionForm dynaActionForm = (DynaActionForm) vecProtocolInfo.get(0);
         // If Only Committe is selected and with out selecting any schedule and set Submission Status Code as 100
        if(committeParamId == null || committeParamId.equals(EMPTY_STRING)){ //|| scheduleId == null || scheduleId.equals(EMPTY_STRING)){
            protcolSubmissiondynaForm.set(SUBMISSION_STATUS_CODE,new Integer(PENDING));
        }else{
            // protcolSubmissiondynaForm.set(SUBMISSION_STATUS_CODE,new Integer(100));
            // modified for first phase
            protcolSubmissiondynaForm.set(SUBMISSION_STATUS_CODE,new Integer(SUBMITTED_TO_COMMITTEE));
        }
        if(reviewType !=null &&  reviewType.length()==1){
            reviewTypeCode = reviewType;
        }
        
        // based of Review Type Description take the Review Type Code.
        HashMap hmCombinedReviewTypes = (HashMap) session.getAttribute(VALID_SUBMISSION_REVIEW_TYPES);
        HashMap hmReviewTypes = new HashMap();
        if(submissionTypeCode !=null && (hmCombinedReviewTypes !=null && hmCombinedReviewTypes.size() > 0)){
            hmReviewTypes   = (HashMap) hmCombinedReviewTypes.get(new Integer(Integer.parseInt(submissionTypeCode)));
            if(hmReviewTypes !=null && hmReviewTypes.size()>0){
                Set setRevTypes =  hmReviewTypes.keySet(); ;
                Iterator iterator = setRevTypes.iterator();
                while(iterator.hasNext()){
                    Object key = iterator.next();
                    String value = (String)hmReviewTypes.get(key);
                    if(reviewType.equals(value)){
                        reviewTypeCode = key.toString();
                        break;
                    }
                }
            }
        }
        // Modified for COEUSQA-3123_Type qualifier doesn't stick when selected in Lite "submit to IRB" screen_start
//        String typeQualifier = (String) protcolSubmissiondynaForm.get(TYPE_QUALIFIER);        
        if(typeQualifier !=null &&  typeQualifier.length()==1){
            typeQualCode = typeQualifier;
        }
        HashMap hmTypeQualifier = (HashMap) session.getAttribute(VALID_PROTO_SUB_TYPE_QUALS);
        HashMap hmTypeQual = new HashMap();
        if(submissionTypeCode !=null && (hmTypeQualifier !=null && hmTypeQualifier.size() > 0 && typeQualifier !=null)){
            hmTypeQual   = (HashMap) hmTypeQualifier.get(new Integer(Integer.parseInt(submissionTypeCode)));
            if(hmTypeQual !=null && hmTypeQual.size()>0){
                    Set setTypeQuals =  hmTypeQual.keySet(); ;
                    Iterator iterator = setTypeQuals.iterator();
                    while(iterator.hasNext()){
                        Object key = iterator.next();
                        String value = (String)hmTypeQual.get(key);
                        if(typeQualifier.equals(value)){
                            typeQualCode = key.toString();
                            break;
                        }
                    }
                }
        }
        // Modified for COEUSQA-3123_Type qualifier doesn't stick when selected in Lite "submit to IRB" screen_end
        //Integer data = (Integer)protcolSubmissiondynaForm.get(SUBMISSION_TYPE_QUAL_CODE);
        // Commented for COEUSQA-3123_Type qualifier doesn't stick when selected in Lite "submit to IRB" screen_start
//        if(typeQualifier == null || typeQualifier.equals(EMPTY_STRING)){
//            typeQualifier = null;
//        }else{
//            // data = new Integer(Integer.parseInt(typeQualifier));
//        }
        // Commented for COEUSQA-3123_Type qualifier doesn't stick when selected in Lite "submit to IRB" screen_end
        // Add all protocol submission data to dynaform
        protcolSubmissiondynaForm.set(PROTOCOL_NUMBER,dynaActionForm.get(PROTOCOL_NUMBER));
        protcolSubmissiondynaForm.set(SEQUENCE_NUMBER,new Integer((String) dynaActionForm.get(SEQUENCE_NUMBER)));
        protcolSubmissiondynaForm.set(SUBMISSION_NUMBER, new Integer((submissionNumber !=null ? Integer.parseInt(submissionNumber)+1:0)));
        protcolSubmissiondynaForm.set(PROTOCOL_REVIEW_TYPE_CODE, new Integer((reviewTypeCode !=null ? Integer.parseInt(reviewTypeCode):0)));
        protcolSubmissiondynaForm.set(SUBMISSION_TYPE_CODE,  new Integer((submissionTypeCode !=null ? Integer.parseInt(submissionTypeCode):0)));
        // protcolSubmissiondynaForm.set(SUBMISSION_TYPE_QUAL_CODE,   new Integer((typeQualifier !=null && !typeQualifier.equals(EMPTY_STRING)) ? Integer.parseInt(typeQualifier):0));
        // Changed Type Qualifier as String type for Bug fix - 2511
        // Modified for COEUSQA-3123_Type qualifier doesn't stick when selected in Lite "submit to IRB" screen_start
//        protcolSubmissiondynaForm.set(STR_SUBMISSION_TYPE_QUAL_CODE, typeQualifier);
        protcolSubmissiondynaForm.set(STR_SUBMISSION_TYPE_QUAL_CODE, typeQualCode);
        // Modified for COEUSQA-3123_Type qualifier doesn't stick when selected in Lite "submit to IRB" screen_start
        protcolSubmissiondynaForm.set(SCHEDULE_ID, scheduleId);
        protcolSubmissiondynaForm.set(COMMITTEE_ID, committeParamId);
        protcolSubmissiondynaForm.set(SUBMISSION_DATE, prepareTimeStamp().toString());
        protcolSubmissiondynaForm.set(YES_VOTE_COUNT, new Integer(0));
        protcolSubmissiondynaForm.set(NO_VOTE_COUNT, new Integer(0));
        protcolSubmissiondynaForm.set(NEW_UPDATE_TIMESTAMP, prepareTimeStamp().toString());
        protcolSubmissiondynaForm.set(UPDATE_TIMESTAMP, prepareTimeStamp().toString());
        protcolSubmissiondynaForm.set(ABSTAINER_COUNT, new Integer(0));
        protcolSubmissiondynaForm.set(AC_TYPE, TypeConstants.INSERT_RECORD);
        vecProtocolSubmission.add(protcolSubmissiondynaForm);
        return vecProtocolSubmission;
    }
    
    /**
     * This method is used to prepare the data for Protocol Actions
     * @param vecProtocolInfo
     * @param submissionNumber
     * @throws Exception
     * @return updated vecProtocolInfo
     */
    private boolean saveProtocolActions(Vector vecProtocolInfo, String submissionNumber, HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        boolean success = false;
        if(vecProtocolInfo !=null && vecProtocolInfo.size()>0){
            DynaActionForm dynaActionForm = (DynaActionForm) vecProtocolInfo.get(0);
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
            String userId = userInfoBean.getUserId();
            int actionCode =  101; //Normal Submission
            ActionTransaction actionTxn = new ActionTransaction(actionCode) ;
            String protocolNumber = (String)dynaActionForm.get(PROTOCOL_NUMBER);
            int sequenceNumber = Integer.parseInt(dynaActionForm.get(SEQUENCE_NUMBER).toString());
            int subNum = (submissionNumber !=null ? Integer.parseInt(submissionNumber)+1:0);
            if (actionTxn.logStatusChangeToProtocolAction(protocolNumber,sequenceNumber, new Integer(subNum) , userId) != -1) {// status is submit to IRB
                success = true ;
            }
        }
        return  success;
    }
    /**
     * This method is used to prepare a Protocol Reviewer details for save functinality.
     * @param protocolSubmissionDynaBeanList
     * @param vecProtocolInfo
     * @param submissionNumber
     * @throws Exception
     * @return updated vecProtocolInfo
     */
    
    private Vector prepareProtocolReviewer(ProtocolSubmissionDynaBeanList protocolSubmissionDynaBeanList,
            Vector vecProtocolInfo, String submissionNumber, HttpServletRequest request)throws Exception{
        Vector vecProtocolReviewer = new Vector();
        // 3282: Reviewer view of Protocol materials - Start
        DateUtils dateUtils = new DateUtils();
        boolean isValidAssignedDate = false;
        boolean isValidDueDate = false;
        // 3282: Reviewer view of Protocol materials - End
        String SELECT_REVIEWER_TYPE = "selectReviewerType";
        String REVIEW_TYPE_CODE = "reviewerTypeCode";
        List lstReviewerInfo = (List) protocolSubmissionDynaBeanList.getReviewerList();
        Vector vecGetReviewers  = new Vector();
        vecGetReviewers = getProtocolReviewer(protocolSubmissionDynaBeanList, request);
        DynaActionForm dynaActionForm = (DynaActionForm) vecProtocolInfo.get(0);
        if((lstReviewerInfo !=null && lstReviewerInfo.size()>0) && (vecGetReviewers !=null && vecGetReviewers.size()>0)){
            for(int vecIndex =0; vecIndex < vecGetReviewers.size(); vecIndex++ ){
                Integer vecKey = (Integer) vecGetReviewers.elementAt(vecIndex);
                int key = vecKey.intValue();
                if(key <= lstReviewerInfo.size()){
                    DynaActionForm dynaReviewerActionForm = (DynaActionForm) lstReviewerInfo.get(key);
                    dynaReviewerActionForm.set(PROTOCOL_NUMBER,dynaActionForm.get(PROTOCOL_NUMBER));
                    dynaReviewerActionForm.set(SEQUENCE_NUMBER,new Integer((String)dynaActionForm.get(SEQUENCE_NUMBER)));
                    dynaReviewerActionForm.set(SUBMISSION_NUMBER, new Integer((submissionNumber !=null ? Integer.parseInt(submissionNumber)+1:0)));
                    String reviwerType =(String) dynaReviewerActionForm.get(SELECT_REVIEWER_TYPE);
                    dynaReviewerActionForm.set(REVIEW_TYPE_CODE, new Integer((reviwerType !=null ? Integer.parseInt(reviwerType):0)));
                    // 3282: Reviewer view of Protocol materials - Start
                    String assignedDate = (String) dynaReviewerActionForm.get("assignedDate");
                    String dueDate = (String) dynaReviewerActionForm.get("dueDate");
                    
                    isValidAssignedDate = dateUtils.validateDate(assignedDate,"/");
                    isValidDueDate = dateUtils.validateDate(dueDate,"/");
                    if(isValidAssignedDate && !"".equals(assignedDate)){
                        dynaReviewerActionForm.set("assignedDate", dynaReviewerActionForm.get("assignedDate"));
                    } else{
                        dynaReviewerActionForm.set("assignedDate", null);
                    }
                    if(isValidDueDate && !"".equals(dueDate)){
                        dynaReviewerActionForm.set("dueDate", dynaReviewerActionForm.get("dueDate"));
                    }else{
                        dynaReviewerActionForm.set("dueDate", null);
                    }
                    // 3282: Reviewer view of Protocol materials - End
                    
                    dynaReviewerActionForm.set(NEW_UPDATE_TIMESTAMP, prepareTimeStamp().toString());
                    dynaReviewerActionForm.set(UPDATE_TIMESTAMP, prepareTimeStamp().toString());
                    dynaReviewerActionForm.set(AC_TYPE, TypeConstants.INSERT_RECORD);
                    //COEUSQA-3082 IACUC - Protocol does not appear under Pending/In Progress Reviews - Start
                    dynaReviewerActionForm.set(IS_REVIEW_COMPLETE, "N");
                    //COEUSQA-3082 IACUC - Protocol does not appear under Pending/In Progress Reviews - End
                    vecProtocolReviewer.addElement(dynaReviewerActionForm);
                }
            }//end of for loop
        }
        return vecProtocolReviewer;
    }
    
    
    /**
     * This method is used to get the selected Reviewers List
     * @param protocolSubmissionDynaBeanList
     * @throws Exception
     * @return vecReviewerInfo
     */
    private Vector getProtocolReviewer(ProtocolSubmissionDynaBeanList protocolSubmissionDynaBeanList, HttpServletRequest request)throws Exception{
        List lstReviewerInfo = (List) protocolSubmissionDynaBeanList.getReviewerList();
        Vector vecReviewInfo = new Vector();
        if(lstReviewerInfo !=null && lstReviewerInfo.size()>0){
            for(int index=0; index < lstReviewerInfo.size(); index++){
                String selectReviewer = request.getParameter("dynaFormReviewer["+index+"].selectReviewer");
                // String selectReviewerType = request.getParameter("dynaFormReviewer["+index+"].selectReviewerType");
                if(selectReviewer !=null && !selectReviewer.equals(EMPTY_STRING) && selectReviewer.equals(ON)){
                    vecReviewInfo.addElement(new Integer(index));
                }
            }
        }
        return vecReviewInfo;
    }
    /**
     * This method is used to prepare a checklist details for save functinality.
     * @param protocolSubmissionDynaBeanList
     * @param vecProtocolInfo
     * @param submissionNumber
     * @throws Exception
     * @return updated vecProtocolCheckList
     */
    
    private Vector prepareProtocolCheckList(ProtocolSubmissionDynaBeanList protocolSubmissionDynaBeanList,
            Vector vecProtocolInfo, String submissionNumber, HttpServletRequest request)throws Exception{
        Vector vecProtocolCheckList = new Vector();
        List lstCheckListInfo = (List) protocolSubmissionDynaBeanList.getCheckList();
        Vector vecGetCheckList  = new Vector();
        vecGetCheckList = getProtocolCheckList(protocolSubmissionDynaBeanList,request);
        DynaActionForm dynaActionForm = (DynaActionForm) vecProtocolInfo.get(0);
        if((lstCheckListInfo !=null && lstCheckListInfo.size()>0) && (vecGetCheckList !=null && vecGetCheckList.size()>0)){
            for(int vecIndex =0; vecIndex < vecGetCheckList.size(); vecIndex++ ){
                Integer vecKey = (Integer) vecGetCheckList.elementAt(vecIndex);
                int key = vecKey.intValue();
                if(key <= lstCheckListInfo.size()){
                    DynaActionForm dynaCheckListActionForm = (DynaActionForm) lstCheckListInfo.get(key);
                    dynaCheckListActionForm.set(PROTOCOL_NUMBER,dynaActionForm.get(PROTOCOL_NUMBER));
                    dynaCheckListActionForm.set(SEQUENCE_NUMBER,dynaActionForm.get(SEQUENCE_NUMBER));
                    dynaCheckListActionForm.set(SUBMISSION_NUMBER, new Integer((submissionNumber !=null ? Integer.parseInt(submissionNumber)+1:0)));
                    dynaCheckListActionForm.set(NEW_UPDATE_TIMESTAMP, prepareTimeStamp().toString());
                    dynaCheckListActionForm.set(UPDATE_TIMESTAMP, prepareTimeStamp().toString());
                    dynaCheckListActionForm.set(AC_TYPE, TypeConstants.INSERT_RECORD);
                    vecProtocolCheckList.addElement(dynaCheckListActionForm);
                }
            }//end of for loop
        }
        return vecProtocolCheckList;
    }
    
    
    /**
     * This method is used to get the selected CheckList based on Review Type
     * @param protocolSubmissionDynaBeanList
     * @throws Exception
     * @return vecReviewerInfo
     */
    
    private Vector getProtocolCheckList(ProtocolSubmissionDynaBeanList protocolSubmissionDynaBeanList,
            HttpServletRequest request)throws Exception{
        List lstCheckListInfo = (List) protocolSubmissionDynaBeanList.getCheckList();
        Vector vecCheckLstInfo = new Vector();
        String SELECT_CHECK_LIST = "selectCheckList";
        if(lstCheckListInfo !=null && lstCheckListInfo.size()>0){
            for(int index=0; index < lstCheckListInfo.size(); index++){
                String selectCheckList = request.getParameter("dynaFormCheck["+index+"].selectCheckList");
                DynaActionForm dynaForm = (DynaActionForm) lstCheckListInfo.get(index);
                String selectChkList =(String) dynaForm.get(SELECT_CHECK_LIST);
                if(selectCheckList ==null || selectCheckList.equals(EMPTY_STRING)){
                    selectCheckList = selectChkList;
                }
                if(selectCheckList !=null && !selectCheckList.equals(EMPTY_STRING) && selectCheckList.equals(ON)){
                    vecCheckLstInfo.addElement(new Integer(index));
                }
                
            }
            
        }
        return vecCheckLstInfo;
    }
    
    
    /**
     * This method vallidates submission type, protocol Review Type, check Committee and Schedule during protocol submission.
     * @param protocolSubmissionDynaBeanList
     * @throws Exception
     * @return navigator
     */
    private boolean validateSubmission(ProtocolSubmissionDynaBeanList protocolSubmissionDynaBeanList, HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        ActionMessages actionMessages  = new ActionMessages();
        String SUBMIT_WITHOUT_COMM = "submitWithoutComm";
        String SUBMIT_WITHOUT_SCHEDULE = "submitWithoutSchedule";
        String SUBMIT_WITHOUT_REVIEWER = "submitWithoutReviewer";
        String NO_SUBMISSION_REVIEW_TYPE_ERR_CODE = "noSubmissionAndReviewType";
        String NO_SUBMISSION_REVIEW_TYPE_ERR_MSG = "protocolSubmission.noSubmissionAndReviewType";
        String NO_REVIEW_TYPE_ERR_CODE = "noReviewType";
        String NO_REVIEW_TYPE_ERR_MSG = "protocolSubmission.noReviewType";
        String NO_CHECKLIST_EXIST_ERR_CODE = "noCheckListExistForReviewType";
        String NO_CHECKLIST_EXIST_ERR_MSG = "protocolSubmission.noCheckListExistForReviewType";
        String submissionMode = (String)  session.getAttribute(GET_PROTOCOL_SUBMISSION_MODE);
        //List Values
        String committeParamId = request.getParameter(COMMITTEE_ID);
        String scheduleId = request.getParameter(SCHEDULE_ID);
        //Combo boxes values
        String submissionTypeCode = request.getParameter(DYNA_SUBMISSION_TYPE);
        String reviewType = request.getParameter(REVIEW_TYPE);
        String submitWithoutCommittee = request.getParameter(SUBMIT_WITHOUT_COMM);
        String submitWithoutSchedule = request.getParameter(SUBMIT_WITHOUT_SCHEDULE);
        String submitWithoutReviewer = request.getParameter(SUBMIT_WITHOUT_REVIEWER);
        
        boolean validate = true;
        //Validate a Submission and Review Type
        if((submissionTypeCode == null || submissionTypeCode.equals(EMPTY_STRING)) &&
                (reviewType == null || reviewType.equals(EMPTY_STRING)) ){
            actionMessages.add(NO_SUBMISSION_REVIEW_TYPE_ERR_CODE,
                    new ActionMessage(NO_SUBMISSION_REVIEW_TYPE_ERR_MSG));
            saveMessages(request, actionMessages);
            validate = false;
        }//Validate a Review Type
        else if(submissionTypeCode != null && (reviewType ==null ||
                reviewType.equals(EMPTY_STRING) || reviewType.equals(PLEASE_SLECT))){
            actionMessages.add(NO_REVIEW_TYPE_ERR_CODE,
                    new ActionMessage(NO_REVIEW_TYPE_ERR_MSG));
            saveMessages(request, actionMessages);
            validate = false;
        }//Validate a Submission mode option 'M' and Committee Id
        if(submissionMode !=null && submissionMode.equals("M")){
            if((scheduleId == null || scheduleId.equals(EMPTY_STRING)) &&
                    (committeParamId == null || committeParamId.equals(EMPTY_STRING))){
                actionMessages.add(PLEASE_SELECT_COMMITTEE,
                        new ActionMessage(PLEASE_SELECT_COMMITTEE_MSG));
                saveMessages(request, actionMessages);
                validate = false;
            }
            //Validate Schedule Id
            if((scheduleId == null || scheduleId.equals(EMPTY_STRING))  &&
                    (committeParamId !=null && !committeParamId.equals(EMPTY_STRING))){
                actionMessages.add(NO_SCHEDULE_LIST_ERR_CODE,
                        new ActionMessage(NO_SCHEDULE_LIST_ERR_MSG));
                saveMessages(request, actionMessages);
                validate = false;
            }
        }
        //validate the checkList
        if((submissionTypeCode != null && !submissionTypeCode.equals(EMPTY_STRING)) &&
                (reviewType != null && !reviewType.equals(EMPTY_STRING)) &&
                ((reviewType.equals(EXPEDITED) || reviewType.equals("2")) ||
                ( reviewType.equals(EXEMPT) || reviewType.equals("3")))){
            
            //If review type is Expedited / Exempt then check for CheckList exist if not show Error
            // Check for CheckList exits or not
//            Vector vecCheckListExist = getProtocolCheckList(protocolSubmissionDynaBeanList, request);
//            if(vecCheckListExist ==null || vecCheckListExist.isEmpty()){
//                actionMessages.add(NO_CHECKLIST_EXIST_ERR_CODE,
//                        new ActionMessage(NO_CHECKLIST_EXIST_ERR_MSG));
//                saveMessages(request, actionMessages);
//                validate = false;
//            }
        }
        //Validate and Getting Confirmation Msg in 'O' - Optional Mode without selecting the Committee / Schedule Id.
        if(submissionMode !=null && submissionMode.equals("O") && validate){
            if( (scheduleId == null || scheduleId.equals(EMPTY_STRING)) &&
                    (committeParamId == null || committeParamId.equals(EMPTY_STRING))
                    && (submitWithoutCommittee ==null || submitWithoutCommittee.equals(EMPTY_STRING)) ){
                session.setAttribute(PROTOCOL_SUBMIT_WITHOUT_COMMITTEE,YES);
                validate = false;
            }
            if( (scheduleId == null || scheduleId.equals(EMPTY_STRING)) &&
                    (committeParamId != null && !committeParamId.equals(EMPTY_STRING)) && validate &&
                    (submitWithoutSchedule ==null || submitWithoutSchedule.equals(EMPTY_STRING)) ){
                session.setAttribute(PROTOCOL_SUBMIT_WITHOUT_SCHEDULE,YES);
                validate = false;
            }
            
        }
        // if(submissionMode !=null && submissionMode.equals("O") && validate){
        //check for Review present or not
        if( (scheduleId != null && !scheduleId.equals(EMPTY_STRING)) &&
                (committeParamId != null && !committeParamId.equals(EMPTY_STRING)) && validate &&
                (submitWithoutReviewer ==null || submitWithoutReviewer.equals(EMPTY_STRING) ) ){
            Vector vecCheckReviwerExist = getProtocolReviewer(protocolSubmissionDynaBeanList, request);
            if(vecCheckReviwerExist ==null || vecCheckReviwerExist.isEmpty()){
                session.setAttribute(PROTOCOL_SUBMIT_WITHOUT_REVIEWER,YES);
                validate = false;
            }
        }
        
        // 3282: Reviewer view of Protocol materials - Start
        if(!validateReviewerDetails(protocolSubmissionDynaBeanList, request)){
            actionMessages.add("protocolSubmission.invalidDate",
                    new ActionMessage("protocolSubmission.invalidDate"));
            saveMessages(request, actionMessages);
            validReviewerDetails = false;
            validate = false;
        }
        // 3282: Reviewer view of Protocol materials - End
        
        // }
        //set the already selected Committee Id and Schedule Id
        if(validate == false){
            Vector vecMatchingCommForProto = (Vector)protocolSubmissionDynaBeanList.getBeanList();
            setSelectedCommittee(vecMatchingCommForProto, committeParamId);
            protocolSubmissionDynaBeanList.setBeanList(vecMatchingCommForProto);
            if(scheduleId != null && !scheduleId.equals(EMPTY_STRING)){
                Vector vecScheduleDetails = (Vector)protocolSubmissionDynaBeanList.getScheduleList();
                setSelectedSchedule(vecScheduleDetails, scheduleId);
                protocolSubmissionDynaBeanList.setScheduleList(vecScheduleDetails);
            }
            session.setAttribute(SUBMIT_FOR_REVIEW_LIST, protocolSubmissionDynaBeanList);
            
        }
        return validate;
    }
    
    /**
     * This method is used to get a Schedule Details using procedure getScheduleListForComm
     * @param protocolSubmissionDynaBeanList
     * @throws Exception
     * @return navigator
     */
    private String getScheduleDetails(ProtocolSubmissionDynaBeanList protocolSubmissionDynaBeanList,
            HttpServletRequest request)throws Exception{
        //HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        ActionMessages actionMessages  = new ActionMessages();
        String navigator = SUCCESS;
        String INVALID_SCHEDULE_ERR_CODE = "inValidSchedule";
        String INVALID_SCHEDULE_ERR_MSG = "protocolSubmission.inValidSchedule";
        List lstSubmissionDetails = (List)protocolSubmissionDynaBeanList.getList();
        String selectedCommitteeId = request.getParameter(SELECT_COMMITTEE);
        String committeParamId = request.getParameter(COMMITTEE_ID);
        HashMap hmCommDetails = new HashMap();
        if(lstSubmissionDetails !=null && lstSubmissionDetails.size()>0){
            for(int index=0; index<lstSubmissionDetails.size() ; index++){
                DynaActionForm lstDynaActionForm = (DynaActionForm)lstSubmissionDetails.get(index);
                String committeId = (String) lstDynaActionForm.get(COMMITTEE_ID);
                if(committeId !=null && !committeId.equals(EMPTY_STRING)  && committeParamId == null && selectedCommitteeId ==null){
                    selectedCommitteeId = committeId;
                }else if(committeParamId != null && selectedCommitteeId ==null ){
                    selectedCommitteeId = committeParamId;
                }
            }
        }
        if(selectedCommitteeId !=null && !selectedCommitteeId.equals(EMPTY_STRING)){
            hmCommDetails.put(COMMITTEE_ID, selectedCommitteeId);
            Hashtable htScheduleDetail =
                    (Hashtable)webTxnBean.getResults(request,GET_SCHEDULE_LIST_FOR_COMM ,hmCommDetails);
            if(htScheduleDetail !=null && htScheduleDetail.size()>0){
                Vector vecScheduleDetails = (Vector)htScheduleDetail.get(GET_SCHEDULE_LIST_FOR_COMM);
                if(vecScheduleDetails !=null && vecScheduleDetails.size()>0){
                    //check for scheduleId is not null then set as a selected in radio button
                    String scheduleId = request.getParameter(SCHEDULE_ID);
                    if(scheduleId !=null && !scheduleId.equals(EMPTY_STRING)){
                        setSelectedSchedule(vecScheduleDetails, scheduleId);
                    }
                    protocolSubmissionDynaBeanList.setScheduleList(vecScheduleDetails);
                    request.getSession().setAttribute(SUBMIT_FOR_REVIEW_LIST, protocolSubmissionDynaBeanList);
                    request.setAttribute(SCHEDULE_EXIST,YES);
                }else{
                    // this is for Selecting CommitteeId radio button when there is error.
                    Vector vecMatchingCommForProto = (Vector)protocolSubmissionDynaBeanList.getBeanList();
                    setSelectedCommittee(vecMatchingCommForProto, selectedCommitteeId);
                    protocolSubmissionDynaBeanList.setBeanList(vecMatchingCommForProto);
                    request.getSession().setAttribute(SUBMIT_FOR_REVIEW_LIST, protocolSubmissionDynaBeanList);
                    actionMessages.add(INVALID_SCHEDULE_ERR_CODE,
                            new ActionMessage(INVALID_SCHEDULE_ERR_MSG));
                    saveMessages(request, actionMessages);
                }
            }
        }else{
            actionMessages.add(PLEASE_SELECT_COMMITTEE,
                    new ActionMessage(PLEASE_SELECT_COMMITTEE_MSG));
            saveMessages(request, actionMessages);
        }
        return navigator;
    }
    /**
     * This method is used to get the Committee Details based up on the Committee ID
     * @param protocolSubmissionDynaBeanList
     * @throws Exception
     * @return navigator
     *
     */
    private String getCommitteeDetails(ProtocolSubmissionDynaBeanList protocolSubmissionDynaBeanList, HttpServletRequest request)throws Exception{
        String navigator = SUCCESS;
        String committeeId = request.getParameter(COMMITTEE_ID);
        Vector vecMatchingCommForProto = (Vector)protocolSubmissionDynaBeanList.getBeanList();
        setSelectedCommittee(vecMatchingCommForProto, committeeId);
        protocolSubmissionDynaBeanList.setBeanList(vecMatchingCommForProto);
        return navigator;
    }
    
    /**
     * This method is used to bring the checkList details based on ReviewType Expedited / Exempt
     * @param protocolSubmissionDynaBeanList
     * @throws Exception
     * @return navigator
     */
    private String getCheckList(ProtocolSubmissionDynaBeanList protocolSubmissionDynaBeanList, HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        String navigator = SUCCESS;
        String SELECTED_REVIEW_TYPE = "selectedReviewType";
        Vector vecExpeditedReviewCheckList = (Vector) session.getAttribute(GET_EXPEDITED_REVIEW_CHECK_LIST);
        Vector vecExemptStudiesCheckList =   (Vector) session.getAttribute(GET_EXEMPT_STUDIES_CHECK_LIST);
        String selectedReviewType = request.getParameter(SELECTED_REVIEW_TYPE);
        List lstReviewType = (List) protocolSubmissionDynaBeanList.getList();
        DynaActionForm dynaForm = (DynaActionForm)lstReviewType.get(0);
        if(selectedReviewType !=null && selectedReviewType.equals(EXPEDITED)){
            dynaForm.set(REVIEW_TYPE,"2");
            protocolSubmissionDynaBeanList.setCheckList(vecExpeditedReviewCheckList);
            protocolSubmissionDynaBeanList.setList(lstReviewType);
            session.setAttribute(SUBMIT_FOR_REVIEW_LIST, protocolSubmissionDynaBeanList);
            request.setAttribute(CHECK_LIST_EXSITS,YES);
            //Added for case#3529 - Coeus Lite - Expedited Review Checklist Display- Start 
            request.setAttribute("reviewListChosen","Expedited");
            //Added for case#3529 - Coeus Lite - Expedited Review Checklist Display- End
        }else if(selectedReviewType !=null && selectedReviewType.equals(EXEMPT)){
            dynaForm.set(REVIEW_TYPE,"3");
            protocolSubmissionDynaBeanList.setCheckList(vecExemptStudiesCheckList);
            protocolSubmissionDynaBeanList.setList(lstReviewType);
            session.setAttribute(SUBMIT_FOR_REVIEW_LIST, protocolSubmissionDynaBeanList);
            request.setAttribute(CHECK_LIST_EXSITS,YES);
            //Added for case#3529 - Coeus Lite - Expedited Review Checklist Display- Start 
            request.setAttribute("reviewListChosen","Exempt");
            //Added for case#3529 - Coeus Lite - Expedited Review Checklist Display- End
        }
        
        //Added for case#3529 - Coeus Lite - Expedited Review Checklist Display- Start 
        request.setAttribute("showCheckList",new Boolean(true));
        //Added for case#3529 - Coeus Lite - Expedited Review Checklist Display- End
        return navigator;
    }
    
    /**
     * This method is used to get the reviewer details based on Committee Id and Schedule Id.
     * @param protocolSubmissionDynaBeanList
     * @throws Exception
     * @return navigator
     */
    private String getReviewerDetails(ProtocolSubmissionDynaBeanList protocolSubmissionDynaBeanList, HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        ActionMessages actionMessages = new ActionMessages();
        WebTxnBean webTxnBean = new WebTxnBean();
        String navigator = SUCCESS;
        String NO_ACTIVE_MEMBERS_ERR_CODE = "noActiveMembers";
        String NO_ACTIVE_MEMBERS_ERR_MSG = "protocolSubmission.noActiveMembers";
        String NO_ACTIVE_MEMBERS_SCHEDULE_ERR_CODE = "noActiveMembersforSchedule";
        String NO_ACTIVE_MEMBERS_SCHEDULE_ERR_MSG = "protocolSubmission.noActiveMembersforSchedule";
        String COUNT = "count";
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        List lstSubmissionDetails = (List)protocolSubmissionDynaBeanList.getList();
        String scheduleId = request.getParameter(SCHEDULE_ID);
        String submissionMode = (String)  session.getAttribute(GET_PROTOCOL_SUBMISSION_MODE);
        String selectedCommitteeId = request.getParameter(SELECT_COMMITTEE);
        
        String committeParamId = request.getParameter(COMMITTEE_ID);
        HashMap hmScheduleDetails = new HashMap();
        if(lstSubmissionDetails !=null && lstSubmissionDetails.size()>0){
            for(int index=0; index<lstSubmissionDetails.size() ; index++){
                DynaActionForm lstDynaActionForm = (DynaActionForm)lstSubmissionDetails.get(index);
                String committeId = (String) lstDynaActionForm.get(COMMITTEE_ID);
                if((committeId !=null && !committeId.equals(EMPTY_STRING)) && committeParamId == null && selectedCommitteeId ==null){
                    selectedCommitteeId = committeId;
                }
            }
        }
        // If Submission mode is 'M' ; selected a committee Id and try to select Reviewer without selecting a schedule show Error Msg
        if((scheduleId == null || scheduleId.equals(EMPTY_STRING)) && ((selectedCommitteeId !=null && !selectedCommitteeId.equals(EMPTY_STRING))
        || (committeParamId !=null && !committeParamId.equals(EMPTY_STRING))) &&
                (submissionMode !=null && submissionMode.equalsIgnoreCase("M")) ){
            Vector vecMatchingCommForProto = (Vector)protocolSubmissionDynaBeanList.getBeanList();
            setSelectedCommittee(vecMatchingCommForProto, selectedCommitteeId);
            protocolSubmissionDynaBeanList.setBeanList(vecMatchingCommForProto);
            request.getSession().setAttribute(SUBMIT_FOR_REVIEW_LIST, protocolSubmissionDynaBeanList);
            actionMessages.add(NO_SCHEDULE_LIST_ERR_CODE,
                    new ActionMessage(NO_SCHEDULE_LIST_ERR_MSG));
            saveMessages(request, actionMessages);
            
        }
        
        // If Submission mode is 'O'  Select the reviewer , without scheduled id then execute getCommCommitteeReviewers OR
        //  From schedule List without selecting any scheduleId and select the Reviewer
        if(((scheduleId == null || scheduleId.equals(EMPTY_STRING)) && (selectedCommitteeId !=null && !selectedCommitteeId.equals(EMPTY_STRING)))
        && (submissionMode !=null && submissionMode.equalsIgnoreCase("O")) ||
                (((scheduleId == null || scheduleId.equals(EMPTY_STRING)) && selectedCommitteeId == null) && (submissionMode !=null && submissionMode.equalsIgnoreCase("O")))){
            if(selectedCommitteeId ==null && committeParamId !=null){
                selectedCommitteeId  = committeParamId;
            }
            if(selectedCommitteeId !=null && !selectedCommitteeId.equals(EMPTY_STRING)){
                hmScheduleDetails.put(COMMITTEE_ID, selectedCommitteeId);
                hmScheduleDetails.put(PROTOCOL_NUMBER,protocolNumber);
                Hashtable htReviewerDetail =
                        (Hashtable)webTxnBean.getResults(request,GET_COMM_COMMITTEE_REVIEWERS ,hmScheduleDetails);
                
                if(htReviewerDetail !=null && htReviewerDetail.size()>0){
                    Vector vecReviewerDetails = (Vector)htReviewerDetail.get(GET_COMM_COMMITTEE_REVIEWERS);
                    if(vecReviewerDetails !=null && vecReviewerDetails.size()>0){
                        protocolSubmissionDynaBeanList.setReviewerList(vecReviewerDetails);
                        request.getSession().setAttribute(SUBMIT_FOR_REVIEW_LIST, protocolSubmissionDynaBeanList);
                        request.setAttribute(REVIEWER_EXIST,YES);
                    }else{
                        // this is for Selecting CommitteeId radio button when there is error.
                        Vector vecMatchingCommForProto = (Vector)protocolSubmissionDynaBeanList.getBeanList();
                        setSelectedCommittee(vecMatchingCommForProto, selectedCommitteeId);
                        protocolSubmissionDynaBeanList.setBeanList(vecMatchingCommForProto);
                        request.getSession().setAttribute(SUBMIT_FOR_REVIEW_LIST, protocolSubmissionDynaBeanList);
                        actionMessages.add(NO_ACTIVE_MEMBERS_ERR_CODE,
                                new ActionMessage(NO_ACTIVE_MEMBERS_ERR_MSG));
                        saveMessages(request, actionMessages);
                    }
                }
            }
        }//Select a ScheduleId and the reviewer execute the getCommitteeReviewers
        else if(scheduleId !=null && !scheduleId.equals(EMPTY_STRING)){
            hmScheduleDetails.put(PROTOCOL_NUMBER,protocolNumber);
            hmScheduleDetails.put(SCHEDULE_ID,scheduleId);
            Hashtable htReviewerScheduleDetail =
                    (Hashtable)webTxnBean.getResults(request,GET_COMMITTEE_REVIEWERS ,hmScheduleDetails);
            if(htReviewerScheduleDetail !=null && htReviewerScheduleDetail.size()>0){
                Vector vecReviewerScheduleDetails = (Vector)htReviewerScheduleDetail.get(GET_COMMITTEE_REVIEWERS);
                if(vecReviewerScheduleDetails !=null && vecReviewerScheduleDetails.size()>0){
                    //get the Protocol Submission Count and Check for protoSubmissionCount is greaterthan maxProtocol show the message
                    Hashtable htProtoCount =
                            (Hashtable)webTxnBean.getResults(request, GET_PROTO_SUBMISSION_COUNT,hmScheduleDetails);
                    HashMap hmProtoCount = (HashMap)htProtoCount.get(GET_PROTO_SUBMISSION_COUNT);
                    String count = (String) hmProtoCount.get(COUNT);
                    String commId = (String) request.getParameter(COMMITTEE_ID);
                    String maxProtocol = getMaximumProtocolsForSchedule(commId,scheduleId,request);
                    if ( (maxProtocol !=null && !maxProtocol.equals(EMPTY_STRING)) && (count !=null && !count.equals(EMPTY_STRING)) && Integer.parseInt(count) >= Integer.parseInt(maxProtocol) ) {
                        request.setAttribute(MAX_PROTOCOLS_EXCEEDED,YES);
                    }
                    protocolSubmissionDynaBeanList.setReviewerList(vecReviewerScheduleDetails);
                    request.getSession().setAttribute(SUBMIT_FOR_REVIEW_LIST, protocolSubmissionDynaBeanList);
                    request.setAttribute(REVIEWER_EXIST,YES);
                }else{ // selecting ScheduleId radio button when there is error
                    Vector vecScheduleDetails = (Vector)protocolSubmissionDynaBeanList.getScheduleList();
                    setSelectedSchedule(vecScheduleDetails, scheduleId);
                    protocolSubmissionDynaBeanList.setScheduleList(vecScheduleDetails);
                    request.getSession().setAttribute(SUBMIT_FOR_REVIEW_LIST, protocolSubmissionDynaBeanList);
                    actionMessages.add(NO_ACTIVE_MEMBERS_SCHEDULE_ERR_CODE,
                            new ActionMessage(NO_ACTIVE_MEMBERS_SCHEDULE_ERR_MSG));
                    saveMessages(request, actionMessages);
                    request.setAttribute(SCHEDULE_EXIST,YES);
                }
            }
        } if((scheduleId == null || scheduleId.equals(EMPTY_STRING)) && ( committeParamId == null || committeParamId.equals(EMPTY_STRING))){
            actionMessages.add(PLEASE_SELECT_COMMITTEE,
                    new ActionMessage(PLEASE_SELECT_COMMITTEE_MSG));
            saveMessages(request, actionMessages);
        }
        
        // 3282: Reviewer view of Protocol materials - Start
        String invalidReviewerDate = (String) request.getAttribute("invalidReviewerDate");
        if("Y".equalsIgnoreCase(invalidReviewerDate)){
            actionMessages.add("protocolSubmission.invalidDate",
                    new ActionMessage("protocolSubmission.invalidDate"));
            saveMessages(request, actionMessages);
        }
        // 3282: Reviewer view of Protocol materials - End
        
        return navigator;
    }
    
    /**
     * This method is used to get the Committee Details based up on the Committee ID
     * @param protocolSubmissionDynaBeanList
     * @throws Exception
     * @return navigator
     *
     */
    private String getAllCommitteeList(ProtocolSubmissionDynaBeanList protocolSubmissionDynaBeanList, HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        String navigator = SUCCESS;
        //Modified for IACUC Changes - Start
//        Hashtable htCommitteeDetail =
//                (Hashtable)webTxnBean.getResults(request,GET_COMMITTEE_LIST ,null);
        Map hmCommitteeType = new HashMap();
        hmCommitteeType.put("matchingCommitteeTypeCode", new Integer(CoeusConstants.IACUC_COMMITTEE_TYPE_CODE));
        Hashtable htCommitteeDetail =
                (Hashtable)webTxnBean.getResults(request,GET_COMMITTEE_LIST ,hmCommitteeType);
        //IACUC Changes - End

        if(htCommitteeDetail !=null && htCommitteeDetail.size()>0){
            Vector vecCommitteeDet = (Vector)htCommitteeDetail.get(GET_COMMITTEE_LIST);
//            Vector vecIacucCommittee = new Vector();
//            for(int i=0; i< vecCommitteeDet.size();i++){
//                DynaValidatorForm dynaForm = (DynaValidatorForm)vecCommitteeDet.get(i);
//                Integer matchingCommitteeTypeCode = (Integer)dynaForm.get("matchingCommitteeTypeCode");
//                if(matchingCommitteeTypeCode.equals(2)){
//                    vecIacucCommittee.add(dynaForm);
//                }
//            }
            protocolSubmissionDynaBeanList.setBeanList(vecCommitteeDet);
            session.setAttribute(SUBMIT_FOR_REVIEW_LIST, protocolSubmissionDynaBeanList);
        }
        return navigator;
    }
    
    
    
    
    /***
     * This method is used to get the maximumProtocolNumber for corresponding Schedule Id
     * @param Schedule Id
     * @throws Exception
     * @return maxProtocolNumber
     */
    private String getMaximumProtocolsForSchedule(String committeeId, String scheduleId, HttpServletRequest request)throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        String maxProtocolNumber = EMPTY_STRING;
        HashMap hmCommDetails = new HashMap();
        //Integer maxProtocols = null;
        hmCommDetails.put(COMMITTEE_ID, committeeId);
        Hashtable htScheduleDetail =
                (Hashtable)webTxnBean.getResults(request,GET_SCHEDULE_LIST_FOR_COMM ,hmCommDetails);
        if(htScheduleDetail !=null && htScheduleDetail.size()>0){
            Vector vecScheduleDetails = (Vector)htScheduleDetail.get(GET_SCHEDULE_LIST_FOR_COMM);
            if(vecScheduleDetails !=null && vecScheduleDetails.size()>0){
                for(int index=0; index < vecScheduleDetails.size() ; index++){
                    DynaActionForm dynaFormSchedule = (DynaActionForm)vecScheduleDetails.get(index);
                    String schdId = (String)dynaFormSchedule.get(SCHEDULE_ID);
                    if(schdId.equals(scheduleId)){
                        maxProtocolNumber =(String) dynaFormSchedule.get(MAX_PROTOCOLS).toString();
                        break;
                    }
                }
            }
        }
        
        return maxProtocolNumber;
    }
    /**
     * This method is for to select the committee List radio button for corresponding committeeId
     * @ param vecCommitteeDetails
     * @ param CommitteeId
     * @ throws Exception
     */
    private void setSelectedCommittee(Vector vecCommitteeDetails, String commId)throws Exception{
        if(vecCommitteeDetails!=null && vecCommitteeDetails.size()>0 && commId !=null){
            for(int index = 0 ; index < vecCommitteeDetails.size(); index++ ){
                DynaActionForm dynaForm = (DynaActionForm)vecCommitteeDetails.get(index);
                dynaForm.set(SELECT_COMMITTEE,"");
                String committeeId = (String) dynaForm.get(MATCHING_COMMITTEE_ID);
                if(committeeId.equals(commId)){
                    dynaForm.set(SELECT_COMMITTEE,committeeId);
                }
            }
        }
    }
    /**
     * This method is for to select the Schedule List radio button for corresponding ScheduleId
     * @ param vecScheduleDetail
     * @ param scheduleId
     * @ throws Exception
     */
    private void setSelectedSchedule(Vector vecScheduleDetail,String scheduleId)throws Exception{
        if(vecScheduleDetail!=null && vecScheduleDetail.size()>0 && scheduleId !=null){
            for(int index = 0 ; index < vecScheduleDetail.size(); index++ ){
                DynaActionForm dynaForm = (DynaActionForm)vecScheduleDetail.get(index);
                dynaForm.set(SELECT_SCHEDULE,"");
                String schedId = (String) dynaForm.get(SCHEDULE_ID);
                if(schedId.equals(scheduleId)){
                    dynaForm.set(SELECT_SCHEDULE,schedId);
                }
            }
        }
    }
    
    /**
     * This method is used to set the Review Type list based on Submission type
     * @param protocolSubmissionDynaBeanList
     * @throws Exception
     */
    private void setSelectedReviewType(ProtocolSubmissionDynaBeanList protocolSubmissionDynaBeanList, HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        String submissionTypeCode = request.getParameter(DYNA_SUBMISSION_TYPE);
        String reviewTypeCode = EMPTY_STRING;
        String reviewType = request.getParameter(REVIEW_TYPE);
        List lstReviewType = (List) protocolSubmissionDynaBeanList.getList();
        if(reviewType !=null &&  reviewType.length()==1){
            reviewTypeCode = reviewType;
        }
        if(lstReviewType !=null && lstReviewType.size()>0){
            // based of Review Type Description take the Review Type Code and set it the form.
            DynaActionForm dynaForm = (DynaActionForm)lstReviewType.get(0);
            HashMap hmCombinedReviewTypes = (HashMap) session.getAttribute(VALID_SUBMISSION_REVIEW_TYPES);
            HashMap hmReviewTypes = new HashMap();
            //Modified for COEUSQA-1388 - Protocol Submission in IRB Lite - Start
            //if((submissionTypeCode !=null && !submissionTypeCode.equals(EMPTY_STRING)) && (hmCombinedReviewTypes !=null && hmCombinedReviewTypes.size() > 0) && (reviewType !=null && !reviewType.equals(EMPTY_STRING))){
                if((submissionTypeCode !=null && !submissionTypeCode.equals(EMPTY_STRING)) && 
                    (hmCombinedReviewTypes !=null && hmCombinedReviewTypes.size() > 0) &&
                    reviewType !=null){
                //Modified for COEUSQA-1388 - Protocol Submission in IRB Lite - End
                hmReviewTypes   = (HashMap) hmCombinedReviewTypes.get(new Integer(Integer.parseInt(submissionTypeCode)));
                if(hmReviewTypes !=null && hmReviewTypes.size()>0){
                    Set setRevTypes =  hmReviewTypes.keySet(); ;
                    Iterator iterator = setRevTypes.iterator();
                    while(iterator.hasNext()){
                        Object key = iterator.next();
                        String value = (String)hmReviewTypes.get(key);
                        if(reviewType.equals(value)){
                            reviewTypeCode = key.toString();
                            break;
                        }
                    }
                }
                dynaForm.set(REVIEW_TYPE, reviewTypeCode);
                session.setAttribute(GET_REVIEW_TYPE, hmReviewTypes);
                protocolSubmissionDynaBeanList.setList(lstReviewType);
                session.setAttribute(SUBMIT_FOR_REVIEW_LIST, protocolSubmissionDynaBeanList);
            }
            
        }//end of if loop
    }
    
    /**
     * This Method to highlight the selected menu
     * @param request
     * @throws Exception
     */
    public void setSubmitForReviewMenu(HttpServletRequest request)throws Exception {
        HttpSession session = request.getSession();
        Vector menuItemsVector=(Vector)session.getAttribute("iacucmenuItemsVector");
        String protocolStatusCode = (String) session.getAttribute("statusCode");
        boolean isPresent = true;
        if(protocolStatusCode !=null && !protocolStatusCode.equals(EMPTY_STRING)){
            if(!( protocolStatusCode.equals("100") || protocolStatusCode.equals("102") || protocolStatusCode.equals("103")
            || protocolStatusCode.equals("104") || protocolStatusCode.equals("105") || protocolStatusCode.equals("106")
            || protocolStatusCode.equals("304"))) {
                isPresent = false;
            }
            Vector modifiedVector = new Vector();
            for (int index=0; index<menuItemsVector.size();index++) {
                MenuBean meanuBean = (MenuBean)menuItemsVector.get(index);
                String menuId = meanuBean.getMenuId();
                if(CoeusliteMenuItems.SUBMIT_FOR_REVIEW.equals(menuId) && !isPresent) {
                    meanuBean.setVisible(false);
                }
                modifiedVector.add(meanuBean);
            }
            session.setAttribute("iacucmenuItemsVector", modifiedVector);
        }
        
    }
    
    /**
     * Check Protocol Submission rights
     * @throws Exception
     */
    private boolean checkSubmitProtocolRights(HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
        String SUBMIT_IACUC_PROTOCOL = "SUBMIT_IACUC_PROTOCOL";
        String SUBMIT_ANY_IACUC_PROTOCOL = "SUBMIT_ANY_IACUC_PROTOCOL";
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        ProtocolHeaderDetailsBean protocolHeaderDetailsBean = (ProtocolHeaderDetailsBean)session.getAttribute("iacucHeaderBean");
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String loggedinUser = userInfoBean.getUserId();
        String unitNumber = protocolHeaderDetailsBean.getUnitNumber();
        boolean canSubmit =  txnData.getUserHasIACUCProtocolRight(loggedinUser, SUBMIT_IACUC_PROTOCOL, protocolNumber);
        //If no rights check at Unit level right
        if(unitNumber !=null && !canSubmit){
            canSubmit = txnData.getUserHasRight(loggedinUser, SUBMIT_ANY_IACUC_PROTOCOL, unitNumber);
        }
        return canSubmit;
    }
    
    /**
     * Added for protocol custom elements validation - start - 4
     * This method check whether 'Others' is checked for an amendment protocol
     * if its checked, it returns true, otherwise false
     * @param request
     * @param protocolNumber
     * @throws Exception
     * @return othersChecked
     */
    private boolean isOthersChecked(HttpServletRequest request, String protocolNumber) throws Exception{
        boolean othersChecked = false;
        HashMap hmData = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        hmData.put(PROTOCOL_NUMBER, protocolNumber);
        Hashtable htOutputData =(Hashtable)webTxnBean.getResults(request, GET_PROTO_AM_MODULES, hmData);
        Vector vecProtocolDetails = (Vector)htOutputData.get(GET_PROTO_AM_MODULES);
        if(vecProtocolDetails != null && vecProtocolDetails.size() > 0){
            for(int index = 0; index < vecProtocolDetails.size(); index++){
                DynaActionForm dynaForm = (DynaActionForm)vecProtocolDetails.get(index);
                String protocolAmendRenNumber = (String)dynaForm.get(PROTOCOL_NUMBER);
                String protocolModuleCode = (String)dynaForm.get("protocolModuleCode");
                if(protocolAmendRenNumber.equals(protocolNumber) && protocolModuleCode.equals("023")){
                    othersChecked = true;
                    break;
                }
            }
        }
        return othersChecked;
    }
    //Added for protocol custom elements validation - end - 4
    
    //Added for Case#3053 - Submission Details Type Qualifier Filter - Start
     /**
     * This method is used to set the Type Qualifier list based on Submission type
     * @param protocolSubmissionDynaBeanList
     * @throws Exception
     */
    private void setSelectedTypeQualifier(ProtocolSubmissionDynaBeanList protocolSubmissionDynaBeanList, HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        String submissionTypeCode = request.getParameter(DYNA_SUBMISSION_TYPE);
        String TypeQualCode = EMPTY_STRING;
        String TypeQual = request.getParameter("typeQualifier");
        List lstTypeQualifiers = (List) protocolSubmissionDynaBeanList.getList();
        if(TypeQual !=null &&  TypeQual.length()==1){
            TypeQualCode = TypeQual;
        }
        if(lstTypeQualifiers !=null && lstTypeQualifiers.size()>0){
            // based of Type Qualifier Description take the Type qualifier Code and set it the form.
            DynaActionForm dynaForm = (DynaActionForm)lstTypeQualifiers.get(0);
            HashMap hmTypeQualifier = (HashMap) session.getAttribute(VALID_PROTO_SUB_TYPE_QUALS);
            HashMap hmTypeQual = new HashMap();
            //coeusqa-1388 - begin Type Qualifier dropdown in protocal submission page not populating properly
            //if((submissionTypeCode !=null && !submissionTypeCode.equals(EMPTY_STRING)) && (hmTypeQualifier !=null && hmTypeQualifier.size() > 0) && (TypeQual !=null && !TypeQual.equals(EMPTY_STRING))){
            //coeusqa-1388 - End
            // Modified for COEUSQA-3123_Type qualifier doesn't stick when selected in Lite "submit to IRB" screen_start
            if((submissionTypeCode !=null && !submissionTypeCode.equals(EMPTY_STRING)) && 
                    (hmTypeQualifier !=null && hmTypeQualifier.size() > 0)&&
                    TypeQual !=null){
            // Modified for COEUSQA-3123_Type qualifier doesn't stick when selected in Lite "submit to IRB" screen_end    
                hmTypeQual   = (HashMap) hmTypeQualifier.get(new Integer(Integer.parseInt(submissionTypeCode)));
                if(hmTypeQual !=null && hmTypeQual.size()>0){
                    Set setTypeQuals =  hmTypeQual.keySet(); ;
                    Iterator iterator = setTypeQuals.iterator();
                    while(iterator.hasNext()){
                        Object key = iterator.next();
                        String value = (String)hmTypeQual.get(key);
                        if(TypeQual.equals(value)){
                            TypeQualCode = key.toString();
                            break;
                        }
                    }
                }
                dynaForm.set("typeQualifier", TypeQualCode);
                session.setAttribute(GET_TYPE_QUALIFIERS, hmTypeQual);
                protocolSubmissionDynaBeanList.setList(lstTypeQualifiers);
                session.setAttribute(SUBMIT_FOR_REVIEW_LIST, protocolSubmissionDynaBeanList);
            }
            
        }//end of if loop
    }
    //Added for Case#3053 - Submission Details Type Qualifier Filter - End
    //Added for Case#3063 - Notice Page to the PI that a Protocol Submission to the IRB is successful-Start
    private String getConfirmationMessage(HttpServletRequest request)throws Exception {
        HttpSession session = request.getSession();
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        //Code modified for Case#2785 - Protocol Routing - starts
        String mapsFound = (String) request.getSession().getAttribute("mapsFound"+request.getSession().getId());
        request.getSession().setAttribute("mapsFound"+request.getSession().getId(), null);
        ActionMessages actionMessages = new ActionMessages();
        if(mapsFound != null && !mapsFound.equals("")
            && Integer.parseInt(mapsFound) > 0){
            actionMessages.add("confirmationMessage3",
                    new ActionMessage("protocolIacucSubmission.submissionConfirmationMessage3",protocolNumber));
            saveMessages(request, actionMessages);            
        } else {
            actionMessages.add("confirmationMessage1",
                    new ActionMessage("protocolIacucSubmission.submissionConfirmationMessage1",protocolNumber));
            actionMessages.add("confirmationMessage2",
                    new ActionMessage("protocolIacucSubmission.submissionConfirmationMessage2",protocolNumber));
            saveMessages(request, actionMessages);            
        }
        //Code modified for Case#2785 - Protocol Routing - ends
         setMenuForAmendRenew(protocolNumber, ""+sequenceNumber, request);
        return SUCCESS;
    }
    //Added for Case#3063 - Notice Page to the PI that a Protocol Submission to the IRB is successful- End

    // 3282: Reviewer view of Protocol materials - Start
    /**
     * Method to validate the Reviewer Details entered by the user
     * 
     * @param ProtocolSubmissionDynaBeanList protocolSubmissionDynaBeanList
     * @param HttpServletRequest request
     * @return boolean if the Reviewer details are valid or not
     */
    private boolean validateReviewerDetails(ProtocolSubmissionDynaBeanList protocolSubmissionDynaBeanList,
            HttpServletRequest request) throws Exception {
        List lstReviewerInfo = (List) protocolSubmissionDynaBeanList.getReviewerList();
        Vector vecGetReviewers  = new Vector();
        boolean isValidAssignedDate = true;
        boolean isValidDueDate = true;        
        DateUtils dateUtils = new DateUtils();
        
        validReviewerDetails = true;
        vecGetReviewers = getProtocolReviewer(protocolSubmissionDynaBeanList, request);
        
        if((lstReviewerInfo !=null && lstReviewerInfo.size()>0) && (vecGetReviewers !=null && vecGetReviewers.size()>0)){
            for(int vecIndex =0; vecIndex < vecGetReviewers.size(); vecIndex++ ){
                Integer vecKey = (Integer) vecGetReviewers.elementAt(vecIndex);
                int key = vecKey.intValue();
                if(key <= lstReviewerInfo.size()){
                    DynaActionForm dynaReviewerActionForm = (DynaActionForm) lstReviewerInfo.get(key);
                    
                    String assignedDate = (String) dynaReviewerActionForm.get("assignedDate");
                    String dueDate = (String) dynaReviewerActionForm.get("dueDate");
                    
                    if(assignedDate != null && !"".equals(assignedDate)){
                        isValidAssignedDate = dateUtils.validateDate(assignedDate,"/");
                    }
                    
                    if(dueDate != null && !"".equals(dueDate)){
                        isValidDueDate = dateUtils.validateDate(dueDate,"/");
                    }
                    
                    if(!(isValidAssignedDate && isValidDueDate)){
                        validReviewerDetails = false;
                        request.setAttribute("invalidReviewerDate","Y");
                        return false;
                    }
                   
                }
            }
        }
 
        return true;
    }
    // 3282: Reviewer view of Protocol materials - End
    // COEUSQA-2045: Coeus 4.4 QA - IRB Online Review - Review Notification - Start
    private void sendMailToReviewers(Vector reviewerDetails, String protocolNumber, int sequenceNumber) {
        ProtocolSubmissionUpdateTxnBean protocolSubmissionUpdateTxnBean = new ProtocolSubmissionUpdateTxnBean();
        Vector vecReviewers = new Vector();
        int totalReviewers = reviewerDetails.size();
        for(int index = 0; index < totalReviewers; index++){
            ProtocolReviewerInfoBean protocolReviewerInfoBean = new ProtocolReviewerInfoBean();
            protocolReviewerInfoBean.setAcType("I");
            protocolReviewerInfoBean.setProtocolNumber(protocolNumber);
            protocolReviewerInfoBean.setSequenceNumber(sequenceNumber);
            
            DynaActionForm reviewersFromForm = (DynaActionForm) reviewerDetails.get(index);
            
            protocolReviewerInfoBean.setPersonId((String) reviewersFromForm.get("personId"));
            vecReviewers.add(protocolReviewerInfoBean);
           
        }
        
        protocolSubmissionUpdateTxnBean.sendMailsForReviewerChange(vecReviewers);
    }
    // COEUSQA-2045: Coeus 4.4 QA - IRB Online Review - Review Notification - End
    // COEUSQA-1724_ Implement validation based on rules in protocols_Start
    /* Get all the validation rules present
      * @param protocolNumber
      * @param sequenceNumber
      * @param HttpSession
      * @param HttpServletRequest
      * @return hmMsg map containing validation rules
      * @throws Exception 
      */
    private HashMap checkProtocolValidationRules(String protocolNumber,String sequenceNumber,
            HttpSession session,HttpServletRequest request) throws Exception{
        boolean protocolValidationFlag = true;
        ServletContext servletContext = request.getSession().getServletContext();
        ModuleConfig moduleConfig = RequestUtils.getModuleConfig(request, servletContext);
        FormBeanConfig formConfig = moduleConfig.findFormBeanConfig(VALIDATE_CHECK_FORM);
        DynaActionFormClass dynaClass = DynaActionFormClass.createDynaActionFormClass(formConfig);
        DynaActionForm dynaForm = (DynaActionForm)dynaClass.newInstance();
        Vector protocolValidationRules = checkValidationProtocol(protocolNumber,
                                                                Integer.parseInt(sequenceNumber), 
                                                                dynaForm, 
                                                                session);
        if(protocolValidationRules.size() > 0){
            protocolValidationFlag = false;
        }
        HashMap hmMsg = new HashMap();
        hmMsg.put(PROTOCOL_VALIDATION_FLAG, new Boolean(protocolValidationFlag));
        hmMsg.put(RESPONSE_OBJECT, protocolValidationRules);
        return hmMsg;
    }
}





