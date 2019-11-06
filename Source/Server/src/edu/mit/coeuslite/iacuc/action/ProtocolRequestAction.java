
/*
 * @(#)ProtocolRequestAction.java 1.0 03/22/07 2:08 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 19-JULY-2010
 * by Johncy M John
 */

package edu.mit.coeuslite.iacuc.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.iacuc.bean.ActionTransaction;
import edu.mit.coeus.iacuc.bean.CorrespondenceTypeFormBean;
import edu.mit.coeus.iacuc.bean.ProtoCorrespRecipientsBean;
import edu.mit.coeus.iacuc.bean.ProtoCorrespTypeTxnBean;
import edu.mit.coeus.iacuc.bean.ProtocolActionDocumentBean;
import edu.mit.coeus.iacuc.bean.ProtocolActionsBean;
import edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean;
import edu.mit.coeus.iacuc.bean.ProtocolSubmissionTxnBean;
import edu.mit.coeus.iacuc.bean.ProtocolSubmissionUpdateTxnBean;
import edu.mit.coeus.iacuc.bean.UploadDocumentBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireUpdateTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.IacucProtocolActionsConstants;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.TypeConstants;
//import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeus.utils.documenttype.DocumentTypeChecker;
import edu.mit.coeus.utils.pdf.generator.XMLtoPDFConvertor;
import edu.mit.coeuslite.iacuc.form.ProtocolActionForm;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.ComboBoxBean;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
//import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.w3c.dom.Document;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.xml.iacuc.generator.XMLStreamGenerator;

/**
 *
 * @author  nandkumarsn
 */
public class ProtocolRequestAction extends ProtocolBaseAction {    
    
    private static final String EMPTY_STRING = "";
    private static final String GET_REQUEST_TO_CLOSE = "/getRequestToClose";
    private static final String GET_REQUEST_TO_CLOSE_ENROLLMENT = "/getRequestToCloseEnrollment";
    private static final String GET_REQUEST_FOR_SUSPRNSION = "/getRequestForSuspension";
    private static final String SAVE_PROTOCOL_ACTION = "/saveIacucActions";
    private static final String GET_LEAD_UNIT_NUMBER = "getIacucLeadUnitNumber";
    private static final String CAN_PERFORM_PROTOCOL_ACTION = "canPerformIacucAction";    
    private static final String GET_MAX_SEQ_NUMBER = "getIacucMaxSequenceNumber";
    private static final String PROTOCOL_NUMBER = "protocolNumber";
    private static final String SEQUENCE_NUMBER = "sequenceNumber";
    private static final String SCHEDULE_ID = "scheduleId";
    private static final String SUBMISSION_NUMBER = "submissionNumber";
    private static final String PROTO_ACTION_TYPE_CODE = "protocolActionTypeCode";
   // private static final String COMMENTS = "comments";
    private static final String IACUC_ACTION_CODE = "IACUC_ACTION_CODE";
    private static final String UNIT_NUMBER = "unitNumber";
   // private static final String LEAD_UNIT_FLAG = "leadUnitFlag";
    private static final String YES = "Y";
    private static final String NO = "N";
    private static final String SUCCESS = "success";
    private static final String ONE = "1";
    private static final String MINUS_ONE = "-1";
    private static final String SUBMIT_IACUC_PROTOCOL = "SUBMIT_IACUC_PROTOCOL";
    private static final String MODIFY_ANY_IACUC_PROTOCOL = "MODIFY_ANY_IACUC_PROTOCOL";
    private static final String ERROR = "IACUC_ERROR";
    private static final String USER ="user";    
    private static final String S = "S";
    private static final String REQUEST_TO_DEACTIVATE = "Request to Deactivate";
    private static final String REQUEST_TO_LIFT_HOLD = "Request to Lift Hold";
    private static final String ACTION_ID = "actionId";
    private static final String MAX_SEQ_NUM = "MAX_SEQ_NUM";
    
    //Added for including new actions - start
    //private static final String REQUEST_TO_DATA_ANALYSIS = "Request to Data Analysis";
    //private static final String REQUEST_TO_REOPEN_ENROLLMENT = "Request to Reopen Enrollment";
    private static final String GET_ALL_ACTIONS = "/getAllIacucActions";
    private static final String GET_REQUEST_TO_REOPEN = "/getRequestToReopenEnrol";
    private static final String GET_REQUEST_TO_LIFT_HOLD = "/getRequestForLiftHold";
    private static final String GET_REQUEST_TO_DEACTIVATE = "/getRequestForDeactivate";
    private static final String GET_NOTIFY_IACUC = "/getNotifyIacuc";    
    private static final String GET_ALL_COMMITTEES = "getCommitteeListForActions";
    private static final String COMMITTEES = "iacucCommittees";
    private static final String COMMITTEE_ID = "committeeId";
    private static final String ZERO = "0";
    private static final String EXCEPTION_CODE = "protocolAction_exceptionCode.4999";
    private static final String NOTIFY_IACUC = "Notify IACUC";
    //Added for including new actions - end
     
    //Added for response and expedited approval actions - start - 1
    private static final String GET_RESPONSE_APPROVAL = "/getResponseApproval";
    private static final String GET_EXPEDITED_APPROVAL = "/getExpeditedApproval";
    private static final String ACTION_RIGHT = "PERFORM_IACUC_ACTIONS_ON_PROTO";
    private static final String APPROVAL_ACTION_ERROR_FLAG = "iacucApprovalActionErrorFlag";
    private static final String IACUC_SUBMISSION_DETAILS = "IACUC_SUBMISSION_DETAILS";
   // private static final String APPROVAL_DATE = "approvalDate";
    //private static final String EXPIRATION_DATE = "expirationDate";
    private static final String ERROR_4999 = "4999";
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";    
    private static final String GET_PROTOCOL_DETAILS = "getIacucInfo";
    private static final String PROTOCOL_STATUS_CODE = "protocolStatusCode";
    private static final String GET_ORG_APPROVAL_DATE = "getIacucOriginalApprovalDate";
    private static final String SUCCESS_FLAG = "iacucSuccessFlag"; 
    private static final String RESPONSE_APPROVAL_ACTION_CODE = "208";
    private static final String EXPEDITED_APPROVAL_ACTION_CODE = "205";
    //Added for response and expedited approval actions - end - 1
    
    //Added for case#2250 - Expire Protocol -Start
    private static final String GET_EXPIRE =  "/getExpire";
    private static final String EXPIRE_PROTOCOL_CODE = "305";    
    private static final String EXPIRE_PROTOCOL = "Expire protocol";
    //Added for case#2250 - Expire Protocol -End
    
    //Added for case#3046 - Notify IRB attachments - start
    
    //private static final String GET_MAX_SUBMISSION_NUMBER = "getIACUCMaxSubNum";
    //Added for case#3046 - Notify IRB attachments - end
    
    //Added for case#3214 - Withdraw Submission - Start
    private static final String GET_WITHDRAW_SUBMISSION =  "/getIacucWithdrawSubmission";
 
    private static final String WITHDRAW_SUBMISSION = "Withdraw Submission";
    //Added for case#3214 - Withdraw Submission - End
    //Code added for Case#3554 - Notify IRB enhancement - starts
    private static final String GET_QUALIFIER_TYPE = "getIacucQualTypeForSubtype";
    private static final String QUALIFIER_TYPE = "IACUC_QUALIFIER_TYPE";
    private static final String MANDATORY = "M";
    //Code added for Case#3554 - Notify IRB enhancement - ends
    
    // COEUSDEV-327 / COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document - Start
    private static final String ADD_PROTOCOL_ACTION_ATTACHMENT= "/addIacucActionAttachment"; 
    private static final String MODIFY_PROTOCOL_ACTION_ATTACHMENT = "/modifyIacucActionAttachment";
    private static final String VIEW_PROTOCOL_ACTION_ATTACHMENT = "/viewIacucActionAttachment";
   
    //private static final String REQUEST_TO_CLOSE_CODE = "105";
    // private static final String REQUEST_FOR_SUSPENSION_CODE = "106";
    //private static final String REQUEST_TO_CLOSE_ENROLLMENT_CODE = "108";   
    //private static final String REQUEST_FOR_DATA_ANALYSIS_ONLY_CODE = "114";
    //private static final String REQUEST_FOR_RE_OPEN_ENROLLMENT_CODE= "115";
    // COEUSDEV-327 / COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document - End    
    // COEUSDEV-86: Questionnaire for a Submission - Start
    private static final String SAVE_TEMP_PROTOCOL_ACTION = "/saveTempIacucActions";
    private static final String COMPLETE_PROTOCOL_SUBMISSION = "/completeIacucSubmission";
    private static final String CANCEL_PROTOCOL_SUBMISSION = "/cancelIacucSubmission";
    private static final String RECORD_ALREADY_LOCKED_ERROR_CODE = "2100";
    // COEUSDEV-86: Questionnaire for a Submission - end
    private static final int FYI_REVIEW = 4;
    // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
    private static final String PERFORM_ABANDON_PROTOCOL =  "/getIacucAbandonProtocol";    
    private static final String ABANDON_PROTOCOL = "Abandon Protocol";
    // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
    /**
     * Creates a new instance of ProtocolRequestAction
     */
    public ProtocolRequestAction() {
    }
    
    /**
     * This method performs the necessary actions by calling the
     * performProtocolActions method
     * @param actionMapping
     * @param actionForm
     * @param request
     * @param response
     * @throws Exception
     * @return actionForward
     */
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm, 
    HttpServletRequest request, HttpServletResponse response) throws Exception {        
        // COEUSDEV-327 / COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document
//        ActionForward actionForward = performProtocolActions(actionMapping, actionForm, request);
        ActionForward actionForward = performProtocolActions(actionMapping, actionForm, request, response);
        return actionForward;
    }  
    
    
    /**
     * This method will identify which request comes from which path and
     * navigates to the respective ActionForward
     * It also check for the lock before performing save action
     * @return ActionForward object
     * @param actionMapping
     * @param actionForm
     * @param request
     * @throws Exception     
     */    
     // COEUSDEV-327 / COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document
//    private ActionForward performProtocolActions(ActionMapping actionMapping, ActionForm actionForm,
//    HttpServletRequest request) throws Exception{ 
    private ActionForward performProtocolActions(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception{        
        String navigator = SUCCESS;    
        ActionForward actionForward = actionMapping.findForward(navigator);
        HttpSession session = request.getSession();
        //Code added for Case#3554 - Notify IRB enhancement
        session.removeAttribute("IACUC_COMM_SELECTION_DURING_SUBMISSION");
        getCommittees(request);
        //Commented for case#3046 - Notify IRB attachments
        //DynaActionForm protocolActionsForm = (DynaActionForm)actionForm;
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.IACUC_MENU_ITEMS);
        //Added for case#3046 - Notify IRB attachments
        ProtocolActionForm protocolActionForm = (ProtocolActionForm)actionForm;        
        if(actionMapping.getPath().equals(GET_REQUEST_TO_CLOSE)){            
           
            // COEUSDEV-327 / COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document
            String actionCode = request.getParameter(IACUC_ACTION_CODE);
            if(actionCode == null){
                actionCode = (String)session.getAttribute(IACUC_ACTION_CODE);
            }
            session.setAttribute(IACUC_ACTION_CODE, actionCode);
            
            String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
            boolean ableToLockProtocol = prepareLockForprotocolSubmission(protocolNumber, request);
            if(!ableToLockProtocol){
                throwMessage(request,RECORD_ALREADY_LOCKED_ERROR_CODE);
            } else {
                navigator = performRequestToCloseOperation(request);
            }
            
            String tempSubmissionSaved = (String) session.getAttribute("iacucTempSubmissionSaved");
            if(CoeusLiteConstants.YES.equalsIgnoreCase(tempSubmissionSaved)){
                
                setSavedTempSubmissionDataToForm(protocolActionForm, request);
                
            } else {
                readProtocolRequestActionMenu(request);
            }
            
            Map mapReqActionMenuList = new HashMap();
            mapReqActionMenuList.put("menuItems",PROTOCOL_REQUEST_ACTION_MENU_ITEMS);
            mapReqActionMenuList.put("menuCode", CoeusliteMenuItems.IACUC_REQUEST_ACTION_SUBMISSION_DETAILS_MENU);
            setSelectedMenuList(request, mapReqActionMenuList);

        }else if(actionMapping.getPath().equals(GET_REQUEST_TO_CLOSE_ENROLLMENT)){            
            
            // COEUSDEV-327 / COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document
            String actionCode = request.getParameter(IACUC_ACTION_CODE);
            if(actionCode == null){
                actionCode = (String)session.getAttribute(IACUC_ACTION_CODE);
            }
            session.setAttribute(IACUC_ACTION_CODE, actionCode);
            String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
            boolean ableToLockProtocol = prepareLockForprotocolSubmission(protocolNumber, request);
            if(!ableToLockProtocol){
                throwMessage(request, RECORD_ALREADY_LOCKED_ERROR_CODE);
            } else {
                navigator = performRequestToCloseEnrollmentOperation(request);
            }
            
            resetFormField(protocolActionForm);
          
           String tempSubmissionSaved = (String) session.getAttribute("iacucTempSubmissionSaved");
           if(CoeusLiteConstants.YES.equalsIgnoreCase(tempSubmissionSaved)){
                setSavedTempSubmissionDataToForm(protocolActionForm, request);
            } else {
               readProtocolRequestActionMenu(request);
            }
           
            Map mapReqActionMenuList = new HashMap();
            mapReqActionMenuList.put("menuItems",PROTOCOL_REQUEST_ACTION_MENU_ITEMS);
            mapReqActionMenuList.put("menuCode", CoeusliteMenuItems.IACUC_REQUEST_ACTION_SUBMISSION_DETAILS_MENU);
            setSelectedMenuList(request, mapReqActionMenuList);
            
            
        }else if(actionMapping.getPath().equals(GET_REQUEST_FOR_SUSPRNSION)){            
            
            // COEUSDEV-327 / COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document
            String actionCode = request.getParameter(IACUC_ACTION_CODE);
            if(actionCode == null){
                actionCode = (String)session.getAttribute(IACUC_ACTION_CODE);
            }
            session.setAttribute(IACUC_ACTION_CODE, actionCode);
            String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
            boolean ableToLockProtocol = prepareLockForprotocolSubmission(protocolNumber, request);
            if(!ableToLockProtocol){
                throwMessage(request, RECORD_ALREADY_LOCKED_ERROR_CODE);
            } else {
            navigator = performRequestForSuspensionOperation(request);
            }
                        
            resetFormField(protocolActionForm);
            
            String tempSubmissionSaved = (String) session.getAttribute("iacucTempSubmissionSaved");
            if(CoeusLiteConstants.YES.equalsIgnoreCase(tempSubmissionSaved)){
                setSavedTempSubmissionDataToForm(protocolActionForm, request);
            } else {
                readProtocolRequestActionMenu(request);
            }
            
            Map mapReqActionMenuList = new HashMap();
            mapReqActionMenuList.put("menuItems",PROTOCOL_REQUEST_ACTION_MENU_ITEMS);
            mapReqActionMenuList.put("menuCode", CoeusliteMenuItems.IACUC_REQUEST_ACTION_SUBMISSION_DETAILS_MENU);
            setSelectedMenuList(request, mapReqActionMenuList);

        }
        // COEUSDEV-86: Questionnaire for a Submission - Start
        else if(actionMapping.getPath().equals(COMPLETE_PROTOCOL_SUBMISSION)){
            
            String fromMenu = request.getParameter("fromMenu");
            if("true".equalsIgnoreCase(fromMenu)){
                checkSubmissionQuestionnaireCompleted(request);
                UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
                LockBean lockBean = getLockingBean(userInfoBean,
                        (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()), request, null);
                LockBean lockData = getLockedData(CoeusLiteConstants.IACUC_PROTO_LOCK_STR+lockBean.getModuleNumber(), request);
                
                boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
                if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
                   
                } else {
                    String errMsg = "release_lock_for";
                    ActionMessages messages = new ActionMessages();
                    messages.add("lockDeleted", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                    saveMessages(request, messages);
                    session.setAttribute(ERROR, YES);
                    
                }

            } 
            
            Map mapReqActionMenuList = new HashMap();
            mapReqActionMenuList.put("menuItems",PROTOCOL_REQUEST_ACTION_MENU_ITEMS);
            mapReqActionMenuList.put("menuCode", CoeusliteMenuItems.IACUC_REQUEST_ACTION_COMPLETE_MENU);
            setSelectedMenuList(request, mapReqActionMenuList);
        }  else if(actionMapping.getPath().equals(CANCEL_PROTOCOL_SUBMISSION)){
            String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());  
            ProtocolSubmissionUpdateTxnBean protocolSubmissionUpdateTxnBean = new ProtocolSubmissionUpdateTxnBean();
            protocolSubmissionUpdateTxnBean.cleanTemporarySubmissionDetails(protocolNumber);
            
            session.removeAttribute("iacucTempSubmissionSaved");
            session.removeAttribute(IACUC_SUBMISSION_DETAILS);
          //  session.removeAttribute("tempSubmissionActionForm");
            request.setAttribute(CoeusLiteConstants.RELEASE_LOCK, CoeusLiteConstants.YES);
            prepareLockRelease(request);
        }
        else if(actionMapping.getPath().equals(SAVE_TEMP_PROTOCOL_ACTION)){
            boolean questionnaireRefreshed = false;
            String actionCode = (String) session.getAttribute(IACUC_ACTION_CODE);
            boolean canSave = true;
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            LockBean lockBean = getLockingBean(userInfoBean,
                    (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()), request, null);
            LockBean lockData = getLockedData(CoeusLiteConstants.IACUC_PROTO_LOCK_STR+lockBean.getModuleNumber(), request);
            String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
            HashMap hmSubmissionData = (HashMap)session.getAttribute(IACUC_SUBMISSION_DETAILS);
            String submissionNumber = (String)hmSubmissionData.get(SUBMISSION_NUMBER);
            if(submissionNumber == null || submissionNumber.equals(EMPTY_STRING)){
                hmSubmissionData.put(SUBMISSION_NUMBER, ONE);
                submissionNumber = (String)hmSubmissionData.get(SUBMISSION_NUMBER);
            }
            
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());           
            if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
                
                if(actionCode != null && actionCode.equals((""+IacucProtocolActionsConstants.NOTIFY_IACUC))) {
                    Map hmQualifiers = new HashMap();
                    hmQualifiers.put("jobCode", "IACUC_COMM_SELECTION_DURING_SUBMISSION");
                    WebTxnBean webTxnBean = new WebTxnBean();
                    hmQualifiers = (Hashtable)webTxnBean.getResults(request, "getDefaultJobCode", hmQualifiers);
                    String value =(String) ((HashMap)hmQualifiers.get("getDefaultJobCode")).get("ls_value");
                    session.setAttribute("IACUC_COMM_SELECTION_DURING_SUBMISSION", value);
                    ActionMessages actionMessages = new ActionMessages();
                    if(protocolActionForm.getNotificationType() == null
                            || protocolActionForm.getNotificationType().equals(EMPTY_STRING)){
                        actionMessages.add("protocolAction_exceptionCode.2033",new ActionMessage("protocolAction_exceptionCode.2033"));
                        saveMessages(request, actionMessages);
                        session.setAttribute(ERROR, NO);
                        session.setAttribute(SUCCESS_FLAG, EMPTY_STRING);
                        session.setAttribute("IACUC_COMM_SELECTION_DURING_SUBMISSION", value);
                        canSave = false;
                    }
                    String committeeId = request.getParameter(COMMITTEE_ID);
                    if(MANDATORY.equals(value) && (protocolActionForm.getCommitteeId() == null
                            || protocolActionForm.getCommitteeId().equals(EMPTY_STRING))){
                        actionMessages.add("protocolAction_exceptionCode.2035",new ActionMessage("protocolAction_exceptionCode.2035"));
                        saveMessages(request, actionMessages);
                        session.setAttribute(ERROR, NO);
                        session.setAttribute(SUCCESS_FLAG, EMPTY_STRING);
                        session.setAttribute("IACUC_COMM_SELECTION_DURING_SUBMISSION", value);
                        canSave = false;
                    }
                    HashMap hmSubmissionDetails = (HashMap)session.getAttribute(IACUC_SUBMISSION_DETAILS);
                    hmSubmissionDetails.put(COMMITTEE_ID, committeeId);
                    
                    // Delete all the temporary questionnaire answers, as the notification type is changed
                    
                    String tempSubmissionCompleted = (String) session.getAttribute("iacucTempSubmissionSaved");
                    String savedNotificationType = "";
                    if(CoeusLiteConstants.YES.equalsIgnoreCase(tempSubmissionCompleted)){
                        savedNotificationType = protocolActionForm.getOldNotificationType();
                        if(savedNotificationType != null && !savedNotificationType.equalsIgnoreCase(protocolActionForm.getNotificationType())
                        && !"".equals(savedNotificationType)){
                           
                            String loggedinUser = userInfoBean.getUserId();
                            QuestionnaireUpdateTxnBean questionnaireUpdateTxnBean = new QuestionnaireUpdateTxnBean(loggedinUser);
                            questionnaireUpdateTxnBean.deleteQuestionnaireAnswers(
                                    ModuleConstants.IACUC_MODULE_CODE,2, protocolNumber+"T", 
                                    String.valueOf(Integer.parseInt(submissionNumber) +1));
                            questionnaireRefreshed = true;
                        }
                        protocolActionForm.setOldNotificationType("");
                    }
                    
                }
                if(canSave){
                    navigator = performTemporarySubmissionAction(request, actionForm, protocolActionForm);
                    Vector menuItemsVector = (Vector)session.getAttribute(PROTOCOL_REQUEST_ACTION_MENU_ITEMS);
                    if(menuItemsVector != null && !menuItemsVector.isEmpty()){
                        int size = menuItemsVector.size();
                        MenuBean menuBean = null;
                        for(int index =0; index < size; index++){
                            menuBean = (MenuBean) menuItemsVector.elementAt(index);
                            if(menuBean != null && CoeusliteMenuItems.IACUC_REQUEST_ACTION_SUBMISSION_DETAILS_MENU.equalsIgnoreCase(menuBean.getMenuId())){
                                menuBean.setDataSaved(true);
                            }
                        }
                    }

                }
                if(questionnaireRefreshed){
                    resetProtocolRequestActionMenu(request, true);
                } else {
                    String newSubmission = (String) request.getAttribute("newSubmission");
                    
                    if(CoeusLiteConstants.YES.equalsIgnoreCase(newSubmission)){
                        resetQuestionnaireMenuData(request, "IACUC_PROTOCOL_SUBMISSION", protocolNumber+"T",String.valueOf(Integer.parseInt(submissionNumber) +1));
                    } else {
                        // resetProtocolRequestActionMenu(request, false);
                    }
                }
                
            } else {
                String errMsg = "release_lock_for";
                ActionMessages messages = new ActionMessages();
                messages.add("lockDeleted", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                saveMessages(request, messages);
                session.setAttribute(ERROR, YES);
            }
        } else if(actionMapping.getPath().equals(SAVE_PROTOCOL_ACTION)){   
            //Code commented for Case#3554 - Notify IRB enhancement
//            navigator = performSaveOperation(request, actionForm, protocolActionForm);
            //Added for email notification implementation - Start
            String actionCode = (String) session.getAttribute(IACUC_ACTION_CODE);
            //Code added for Case#3554 - Notify IRB enhancement - starts
//            boolean canSave = true;
//            if(actionCode != null && actionCode.equals(NOTIFY_IRB_CODE)) {
//                Map hmQualifiers = new HashMap();
//                hmQualifiers.put("jobCode", "IACUC_COMM_SELECTION_DURING_SUBMISSION");        
//                WebTxnBean webTxnBean = new WebTxnBean();
//                hmQualifiers = (Hashtable)webTxnBean.getResults(request, "getDefaultJobCode", hmQualifiers);
//                String value =(String) ((HashMap)hmQualifiers.get("getDefaultJobCode")).get("ls_value");
//                session.setAttribute("IACUC_COMM_SELECTION_DURING_SUBMISSION", value);
//                ActionMessages actionMessages = new ActionMessages(); 
//                if(protocolActionForm.getNotificationType() == null 
//                        || protocolActionForm.getNotificationType().equals(EMPTY_STRING)){
//                    actionMessages.add("protocolAction_exceptionCode.2033",new ActionMessage("protocolAction_exceptionCode.2033"));
//                    saveMessages(request, actionMessages);  
//                    session.setAttribute(ERROR, NO);
//                    session.setAttribute("iacucsuccessFlag", EMPTY_STRING);
//                    session.setAttribute("IACUC_COMM_SELECTION_DURING_SUBMISSION", value);
//                    canSave = false;
//                }
//                String committeeId = request.getParameter(COMMITTEE_ID);
//                if(MANDATORY.equals(value) && (protocolActionForm.getCommitteeId() == null 
//                        || protocolActionForm.getCommitteeId().equals(EMPTY_STRING))){
//                    actionMessages.add("protocolAction_exceptionCode.2035",new ActionMessage("protocolAction_exceptionCode.2035"));
//                    saveMessages(request, actionMessages);  
//                    session.setAttribute(ERROR, NO);
//                    session.setAttribute("iacucsuccessFlag", EMPTY_STRING);
//                    session.setAttribute("IACUC_COMM_SELECTION_DURING_SUBMISSION", value);
//                    canSave = false;
//                }                
//            }
//            if(canSave){
            boolean qnrCompleted = true;
            boolean lockPresent = true;
            if((""+IacucProtocolActionsConstants.NOTIFY_IACUC).equals(actionCode)
            || (""+IacucProtocolActionsConstants.REQUEST_TO_DEACTIVATE).equals(actionCode)
            || (""+IacucProtocolActionsConstants.REQUEST_TO_LIFT_HOLD).equals(actionCode)){
                
                UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
                LockBean lockBean = getLockingBean(userInfoBean,
                        (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()), request, null);
                LockBean lockData = getLockedData(CoeusLiteConstants.IACUC_PROTO_LOCK_STR+lockBean.getModuleNumber(), request);
                
                boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
                if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
                    lockPresent = true;
                } else {
                    String errMsg = "release_lock_for";
                    ActionMessages messages = new ActionMessages();
                    messages.add("lockDeleted", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                    saveMessages(request, messages);
                    session.setAttribute(ERROR, YES);
                    lockPresent = false;
                }
                
                qnrCompleted = checkSubmissionQuestionnaireCompleted(request);
                
                if(!qnrCompleted || ! lockPresent){
                    navigator = "completeSubmissionPage";
                }
             
            } if(qnrCompleted && lockPresent){
                navigator = performSaveOperation(request, actionForm, protocolActionForm);
//            }
                //Code added for Case#3554 - Notify IRB enhancement - ends
                //Modified for COEUSDEV-317 : Notification not working correctly in IRB Module - Start
                //Notification is send for all actions
//            if(actionCode != null && actionCode.equals("114")) {
                String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
                if((IacucProtocolActionsConstants.NOTIFY_IACUC+"").equals(actionCode)
                || (IacucProtocolActionsConstants.REQUEST_TO_DEACTIVATE+"").equals(actionCode)
                || (IacucProtocolActionsConstants.REQUEST_TO_LIFT_HOLD+"").equals(actionCode)){
                    request.setAttribute(CoeusLiteConstants.RELEASE_LOCK, CoeusLiteConstants.YES);
                    prepareLockRelease(request);
                }
                
                if(actionCode != null) {
                    //COEUSDEV -317 : End
                    String success = (String) session.getAttribute(SUCCESS_FLAG);
                    if(success != null && success.equals(YES)) {
                        session.setAttribute("MODULE_CODE", ModuleConstants.IACUC_MODULE_CODE+""); 
                        session.setAttribute("GENERATE_DOC", "true");
                        //COEUSQA-1724: Email Notifications For All Actions In IACUC
                        navigator = "email";
                    }
                }
                //Email notification - end
                session.removeAttribute("iacucTempSubmissionSaved");
                session.removeAttribute(IACUC_SUBMISSION_DETAILS);
                resetFormField(protocolActionForm);
            }
//            session.removeAttribute("tempSubmissionActionForm");
         // COEUSDEV-86: Questionnaire for a Submission - End   
        //Added for including new actions - start    
        }else if(actionMapping.getPath().equals(GET_ALL_ACTIONS)){
            
            String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());  
            ProtocolSubmissionUpdateTxnBean protocolSubmissionUpdateTxnBean = new ProtocolSubmissionUpdateTxnBean();
            protocolSubmissionUpdateTxnBean.cleanTemporarySubmissionDetails(protocolNumber);
            session.removeAttribute("iacucTempSubmissionSaved");
            session.removeAttribute("iacucActionMenuItems");
       //     session.removeAttribute("tempSubmissionActionForm");
            resetFormField(protocolActionForm);
            resetAttachmentFields(protocolActionForm);
            
            initActions(request);                          
        }
        // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start        
        else if(PERFORM_ABANDON_PROTOCOL.equalsIgnoreCase(actionMapping.getPath())){
            String actionCode = request.getParameter(IACUC_ACTION_CODE);
            if(actionCode == null){
                actionCode = (String)session.getAttribute(IACUC_ACTION_CODE);
            }
            session.setAttribute(IACUC_ACTION_CODE, actionCode);

            String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());   
            
            boolean ableToLockProtocol = prepareLockForprotocolSubmission(protocolNumber, request);
            if(!ableToLockProtocol){
                throwMessage(request, RECORD_ALREADY_LOCKED_ERROR_CODE);
            } else {
                navigator = performAbandonProtocol(request);
            }
        }
        // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
        else if(actionMapping.getPath().equals(GET_REQUEST_TO_REOPEN)){            
            // COEUSDEV-86: Questionnaire for a Submission - Start
            String actionCode = request.getParameter(IACUC_ACTION_CODE);
            if(actionCode == null){
                actionCode = (String)session.getAttribute(IACUC_ACTION_CODE);
            }
            session.setAttribute(IACUC_ACTION_CODE, actionCode);
            String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
            
            boolean ableToLockProtocol = prepareLockForprotocolSubmission(protocolNumber, request);
            if(!ableToLockProtocol){
                throwMessage(request, RECORD_ALREADY_LOCKED_ERROR_CODE);
            } else {
                navigator = performRequestToReopenOperation(request);
                
            }
            
            String tempSubmissionSaved = (String) session.getAttribute("iacucTempSubmissionSaved");
            if(CoeusLiteConstants.YES.equalsIgnoreCase(tempSubmissionSaved)){
                setSavedTempSubmissionDataToForm(protocolActionForm, request);
            } else {
                readProtocolRequestActionMenu(request);
            }
            
            
            Map mapReqActionMenuList = new HashMap();
            mapReqActionMenuList.put("menuItems",PROTOCOL_REQUEST_ACTION_MENU_ITEMS);
            mapReqActionMenuList.put("menuCode", CoeusliteMenuItems.IACUC_REQUEST_ACTION_SUBMISSION_DETAILS_MENU);
            setSelectedMenuList(request, mapReqActionMenuList);
            // COEUSDEV-86: Questionnaire for a Submission - End
            
            resetFormField(protocolActionForm);
        }else if(GET_REQUEST_TO_LIFT_HOLD.equals(actionMapping.getPath()) ||
                GET_REQUEST_TO_DEACTIVATE.equals(actionMapping.getPath())){            
            
            // COEUSDEV-327 / COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document
            String actionCode = request.getParameter(IACUC_ACTION_CODE);
            if(actionCode == null){
                actionCode = (String)session.getAttribute(IACUC_ACTION_CODE);
            }
            session.setAttribute(IACUC_ACTION_CODE, actionCode);
            String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
            
            boolean ableToLockProtocol = prepareLockForprotocolSubmission(protocolNumber, request);
            if(!ableToLockProtocol){
                throwMessage(request, RECORD_ALREADY_LOCKED_ERROR_CODE);
            } else {
                navigator = performRequestSubmissionOperation(request);
            }
            // COEUSDEV-86: Questionnaire for a Submission - Start
            String tempSubmissionSaved = (String) session.getAttribute("iacucTempSubmissionSaved");
            if(CoeusLiteConstants.YES.equalsIgnoreCase(tempSubmissionSaved)){
                setSavedTempSubmissionDataToForm(protocolActionForm, request);
            } else {
                readProtocolRequestActionMenu(request);
            }
            Map mapReqActionMenuList = new HashMap();
            mapReqActionMenuList.put("menuItems",PROTOCOL_REQUEST_ACTION_MENU_ITEMS);
            mapReqActionMenuList.put("menuCode", CoeusliteMenuItems.IACUC_REQUEST_ACTION_SUBMISSION_DETAILS_MENU);
            setSelectedMenuList(request, mapReqActionMenuList);
            // COEUSDEV-86: Questionnaire for a Submission - End
            
            resetFormField(protocolActionForm);
        }else if(actionMapping.getPath().equals(GET_NOTIFY_IACUC)){            
            //Code commented and modified for Case#3554 - Notify IRB enhancement
//            navigator = performNotifyIACUCOperation(request);
            // COEUSDEV-327 / COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document 
            String actionCode = request.getParameter(IACUC_ACTION_CODE);
            if(actionCode == null){
                actionCode = (String)session.getAttribute(IACUC_ACTION_CODE);
            }
            session.setAttribute(IACUC_ACTION_CODE, actionCode);

            String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());   
            
            boolean ableToLockProtocol = prepareLockForprotocolSubmission(protocolNumber, request);
            if(!ableToLockProtocol){
                throwMessage(request, RECORD_ALREADY_LOCKED_ERROR_CODE);
            } else {
                resetAttachmentFields(actionForm);
                navigator = performNotifyIACUCOperation(request, protocolActionForm);
            }
          //  }
//            resetFormField(protocolActionForm);  
        //Added for including new actions - end
            // COEUSDEV-86: Questionnaire for a Submission - Start
           String tempSubmissionSaved = (String) session.getAttribute("iacucTempSubmissionSaved");
           if(CoeusLiteConstants.YES.equalsIgnoreCase(tempSubmissionSaved)){
                //resetQuestionnaireMenuData(request, "PROTOCOL_SUBMISSION", protocolNumber,sequenceNumber);
            } else {
               readProtocolRequestActionMenu(request);
            }
            Map mapReqActionMenuList = new HashMap();
            mapReqActionMenuList.put("menuItems",PROTOCOL_REQUEST_ACTION_MENU_ITEMS);
            mapReqActionMenuList.put("menuCode", CoeusliteMenuItems.IACUC_REQUEST_ACTION_SUBMISSION_DETAILS_MENU);
            setSelectedMenuList(request, mapReqActionMenuList);
            // COEUSDEV-86: Questionnaire for a Submission - End
        //Added for response and expedited approval actions - start - 2 
        }else if(actionMapping.getPath().equals(GET_RESPONSE_APPROVAL)){
            navigator = checkApprovalAction(request);
            resetFormField(protocolActionForm);
            setDates(request, protocolActionForm);
        }else if(actionMapping.getPath().equals(GET_EXPEDITED_APPROVAL)){
            navigator = checkApprovalAction(request);
            resetFormField(protocolActionForm); 
            setDates(request, protocolActionForm);
        //Added for response and expedited approval actions - end - 2  
        }
        
        // Added for case # 2250 - Expire protocol - Start 
        else if(actionMapping.getPath().equals(GET_EXPIRE)){
            navigator = performExpireProtocol(request);
            resetFormField(protocolActionForm); 
        }
        // Added for case # 2250 - Expire protocol - End
        //Added for case#3214 - Withdraw Submission - Start
        else if(actionMapping.getPath().equals(GET_WITHDRAW_SUBMISSION)){
            String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
            boolean ableToLockProtocol = prepareLockForprotocolSubmission(protocolNumber, request);
            if(!ableToLockProtocol){
                throwMessage(request, RECORD_ALREADY_LOCKED_ERROR_CODE);
            } else {
                navigator = checkWithdrawSubmission(request);
            }
            resetFormField(protocolActionForm);             
        }
        //Added for case#3214 - Withdraw Submission - End
        // COEUSDEV-327 / COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document -Start
        else if(ADD_PROTOCOL_ACTION_ATTACHMENT.equalsIgnoreCase(actionMapping.getPath())){
            navigator = addAttachment(request,actionForm);
        } else if(MODIFY_PROTOCOL_ACTION_ATTACHMENT.equalsIgnoreCase(actionMapping.getPath())){
            navigator = modifyAttachment(request,actionForm);
        } else if(VIEW_PROTOCOL_ACTION_ATTACHMENT.equalsIgnoreCase(actionMapping.getPath())){
            navigator = viewAttachment(request, response, actionForm);
        }
        // COEUSDEV-327 / COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document - End   
        mapMenuList.put("menuCode",CoeusliteMenuItems.IACUC_PROTOCOL_ACTION_MENU);
        setSelectedMenuList(request, mapMenuList);   
        readSavedStatus(request);
        actionForward = actionMapping.findForward(navigator);
        return actionForward;                
    }
        
    /**
     * This method checks whether request to close action can be performed
     * @param request
     * @return navigator
     * @throws Exception
     */
    private String performRequestToCloseOperation(HttpServletRequest request) throws Exception{        
        String navigator = SUCCESS;        
        String responseCode = isAuthorizedToPerformAction(request);
        throwMessage(request, responseCode);
        return navigator;          
    }
 
    /**
     * This method checks whether request to close enrollment action can be performed
     * @param request
     * @return navigator
     * @throws Exception
     */    
    private String performRequestToCloseEnrollmentOperation(HttpServletRequest request) throws Exception{        
        String navigator = SUCCESS;        
        String responseCode = isAuthorizedToPerformAction(request);
        throwMessage(request, responseCode);
        return navigator;        
    }
       
    /**
     * This method checks whether request for suspension action can be performed
     * @param request
     * @return navigator
     * @throws Exception
     */    
    private String performRequestForSuspensionOperation(HttpServletRequest request) throws Exception{        
        String navigator = SUCCESS;        
        String responseCode = isAuthorizedToPerformAction(request);
        throwMessage(request, responseCode);
        return navigator;        
    }   
    
     /**
     * This method performs save action by calling saveProtocolActions method
     * @param request
     * @param actionForm
     * @return navigator
     * @throws Exception
     */   
    private String performSaveOperation(HttpServletRequest request, ActionForm actionForm, 
            ProtocolActionForm protocolActionForm) throws Exception{        
        String navigator = SUCCESS;        
        String responseCode = MINUS_ONE;  
        boolean saveSuccess = true;
        //Added/Modified for response and expedited approval actions - start
        HttpSession session = request.getSession();
        session.removeAttribute(APPROVAL_ACTION_ERROR_FLAG);
        String actionCode = request.getParameter(IACUC_ACTION_CODE);
        if(actionCode == null){
            actionCode = (String)session.getAttribute(IACUC_ACTION_CODE);
        }                
//        if(actionCode.equals(EXPEDITED_APPROVAL_ACTION_CODE) || actionCode.equals(RESPONSE_APPROVAL_ACTION_CODE) 
//            || actionCode.equals(EXPIRE_PROTOCOL_CODE)){// Modified for case#2250 - Expire Protocol 
//            responseCode = isAuthorizedToPerformApprovalAction(request);  
//            //Indicates that an approval action is being performed,
//            //Set the APPROVAL_ACTION_ERROR_FLAG to YES, indicating
//            //errors are present while performing the approval actions
//            session.setAttribute(APPROVAL_ACTION_ERROR_FLAG, YES);
//        }else{
            responseCode = isAuthorizedToPerformAction(request);
            //Indicates its not an approval action, so set the APPROVAL_ACTION_ERROR_FLAG to NO
            session.setAttribute(APPROVAL_ACTION_ERROR_FLAG, NO);
//        }        
        if(responseCode.equals(ONE)){            
            saveSuccess = saveProtocolActions(request, actionForm, protocolActionForm);
            if(saveSuccess){
                //Even a success messagee is considered as part of error message
                throwMessage(request, S);
                //Indicates an action was performed successfully
                //so set the SUCCESS_FLAG to YES and APPROVAL_ACTION_ERROR_FLAG to NO
                session.removeAttribute(SUCCESS_FLAG);
                session.setAttribute(SUCCESS_FLAG, YES);                 
                session.removeAttribute(APPROVAL_ACTION_ERROR_FLAG);
                session.setAttribute(APPROVAL_ACTION_ERROR_FLAG, NO);
            }
            else{
                //Indicates server side validations failed for approval actions
                //So set the ERROR flag to NO, so that the error message
                //and all the controls on the view (JSP) are visible.
                session.removeAttribute(ERROR);
                session.setAttribute(ERROR, NO);                
            }                
        } else{
           throwMessage(request, responseCode); 
        }  
        //Added/Modified for response and expedited approval actions - end    
        return navigator;  
    }    
    
    
     /**
     * This method checks whether the protocl and user have rights.
     * Also calls the canPerformProtocolAction method which specifies 
     * whether action can be performed or not.
     * @param request     
     * @return navigator
     * @throws Exception
     */     
    private String isAuthorizedToPerformAction(HttpServletRequest request) throws Exception{
        
        String responseCode = MINUS_ONE;
        HttpSession session = request.getSession();        
        session.removeAttribute(ERROR);
        String actionCode = request.getParameter(IACUC_ACTION_CODE);
        if(actionCode == null){
            actionCode = (String)session.getAttribute(IACUC_ACTION_CODE);
        }
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());     
        String leadUnitNumber = getLeadUnitNumber(request, protocolNumber);
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String loggedinUser = userInfoBean.getUserId();
        UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
        
        if (txnData.getUserHasIACUCProtocolRight(loggedinUser, SUBMIT_IACUC_PROTOCOL, protocolNumber) 
        || txnData.getUserHasRight(loggedinUser, MODIFY_ANY_IACUC_PROTOCOL, leadUnitNumber)){            
            responseCode = canPerformProtocolAction(request, protocolNumber);            
        }    
        else{
            responseCode = ERROR_4999;             
        }
        
        session.setAttribute(IACUC_ACTION_CODE, actionCode);
        return responseCode;
        
    }
    
    
     /**     
     * This method checks wheteher the specified action can be performed
     * by calling the function FN_CAN_PERFORM_PROTOCOL_ACTION     
     * @param request     
     * @param protocolNumber
     * @return navigator
     * @throws Exception
     */     
     private String canPerformProtocolAction(HttpServletRequest request, String protocolNumber) throws Exception{
         
        HashMap hmInputData = new HashMap();
        HashMap hmSubmissionData = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();  
        HttpSession session = request.getSession();
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());    
        String responseCode = MINUS_ONE;
        String actionCode = request.getParameter(IACUC_ACTION_CODE);
        if(actionCode == null){
            actionCode = (String)session.getAttribute(IACUC_ACTION_CODE);
        }
        //Get Submission Details        
        hmSubmissionData = (HashMap)session.getAttribute(IACUC_SUBMISSION_DETAILS);
         
        //Execute FN_CAN_PERFORM_PROTOCOL_ACTION
        hmInputData.put(PROTOCOL_NUMBER,protocolNumber);
        hmInputData.put(SEQUENCE_NUMBER,new Integer(sequenceNumber));
        hmInputData.put(SCHEDULE_ID,hmSubmissionData.get(SCHEDULE_ID));
        hmInputData.put(PROTO_ACTION_TYPE_CODE,new Integer(actionCode));  
        if(actionCode.equals(EXPEDITED_APPROVAL_ACTION_CODE) 
            || actionCode.equals(RESPONSE_APPROVAL_ACTION_CODE)){
            hmInputData.put(SUBMISSION_NUMBER, new Integer((String)hmSubmissionData.get(SUBMISSION_NUMBER)));
        }else{
            hmInputData.put(SUBMISSION_NUMBER, new Integer(0));
        }
        Hashtable htCanPerform =(Hashtable)webTxnBean.getResults(request,CAN_PERFORM_PROTOCOL_ACTION, hmInputData);
        if(htCanPerform != null && htCanPerform.size() > 0){
            hmInputData = (HashMap)htCanPerform.get(CAN_PERFORM_PROTOCOL_ACTION);
            responseCode = hmInputData.get("ACTION_CODE").toString();
        }
        
        return responseCode;
     }
    
    
     /**     
     * This method gets the lead unit number for a given protocol  
     * @param request     
     * @param protocolNumber
     * @return navigator
     * @throws Exception
     */      
    private String getLeadUnitNumber(HttpServletRequest request, String protocolNumber) throws Exception{
        
        HashMap hmInputData = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        String unitNumber = EMPTY_STRING;
        String leadUnitFlag = EMPTY_STRING;        
        
        hmInputData.put(PROTOCOL_NUMBER,protocolNumber);
        Hashtable htOutputData =(Hashtable)webTxnBean.getResults(request,GET_LEAD_UNIT_NUMBER, hmInputData);
        Vector vecOutputData = (Vector) htOutputData.get(GET_LEAD_UNIT_NUMBER);         
        if(vecOutputData != null && vecOutputData.size() > 0){
            for(int index = 0; index < vecOutputData.size(); index++){
                //Commented for case#3046 - Notify IRB attachments - start
                //DynaActionForm dynaActionForm = (DynaActionForm)vecOutputData.get(index);                
                //unitNumber = (String)dynaActionForm.get(UNIT_NUMBER);
                //leadUnitFlag = (String)dynaActionForm.get(LEAD_UNIT_FLAG);
                //Commented for case#3046 - Notify IRB attachments - end
                //Added for case#3046 - Notify IRB attachments - start
                ProtocolActionForm protocolActionForm = (ProtocolActionForm)vecOutputData.get(index);
                unitNumber = protocolActionForm.getUnitNumber();
                leadUnitFlag = protocolActionForm.getLeadUnitFlag();            
                //Added for case#3046 - Notify IRB attachments - end
                if(leadUnitFlag.equals(YES)){
                    break;
                }
            }
        }
        return unitNumber;
    }
    
    
     /**     
     * This method sets the appropriate error messages 
     * if the action cannot be performed
     * @param request     
     * @param responseCode        
     */      
    private void throwMessage(HttpServletRequest request, String responseCode){        
        ActionMessages actionMessages = new ActionMessages();  
        HttpSession session = request.getSession();
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());      
        //This boolean variable is set to true when an action cannot be performed
        //due to some reasons. Based on this, the seesion variable ERROR is set
        //to YES or NO which helps in hiding/showing the controls at the view (JSP)
        boolean error = false;
        String actionCode = (String)session.getAttribute(IACUC_ACTION_CODE);
        if(responseCode.equals(ERROR_4999)){            
           if(actionCode.equals(""+IacucProtocolActionsConstants.REQUEST_TO_DEACTIVATE)){
                actionMessages.add(EXCEPTION_CODE, new ActionMessage(EXCEPTION_CODE, REQUEST_TO_DEACTIVATE));
           }else if(actionCode.equals(""+IacucProtocolActionsConstants.REQUEST_TO_LIFT_HOLD)){
                actionMessages.add(EXCEPTION_CODE, new ActionMessage(EXCEPTION_CODE, REQUEST_TO_LIFT_HOLD));
           }else if(actionCode.equals(""+IacucProtocolActionsConstants.NOTIFY_IACUC)){
               actionMessages.add(EXCEPTION_CODE, new ActionMessage(EXCEPTION_CODE, NOTIFY_IACUC));
           }else if(actionCode.equals(EXPIRE_PROTOCOL_CODE)){
               actionMessages.add(EXCEPTION_CODE, new ActionMessage(EXCEPTION_CODE, EXPIRE_PROTOCOL));
           }else if(actionCode.equals(IacucProtocolActionsConstants.WITHDRAWN+"")){
               actionMessages.add(EXCEPTION_CODE, new ActionMessage(EXCEPTION_CODE,WITHDRAW_SUBMISSION));
//               actionMessages.add("iacucProtocolAction_exceptionCode.4025", new ActionMessage("iacucProtocolAction_exceptionCode.4025", WITHDRAW_SUBMISSION));
           // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
           }else if(actionCode.equals(IacucProtocolActionsConstants.PROTOCOL_ABANDON+"")){
               actionMessages.add(EXCEPTION_CODE, new ActionMessage(EXCEPTION_CODE,ABANDON_PROTOCOL));
           }
           // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
           //Added for case#3214 - Withdraw Submission - end
           saveMessages(request, actionMessages);           
           error = true;
        }else if(responseCode.equals("4000")){
           actionMessages.add("iacucProtocolAction_exceptionCode.4000",new ActionMessage("iacucProtocolAction_exceptionCode.4000"));
           saveMessages(request, actionMessages);  
           error = true;
        }else if(responseCode.equals("2008")){
           actionMessages.add("protocolAction_exceptionCode.2008",new ActionMessage("protocolAction_exceptionCode.2008"));
           saveMessages(request, actionMessages);  
           error = true;
        }else if(responseCode.equals("2018")){
           actionMessages.add("protocolAction_exceptionCode.2018",new ActionMessage("protocolAction_exceptionCode.2018"));
           saveMessages(request, actionMessages);  
           error = true;
        }else if(responseCode.equals("2024")){
           actionMessages.add("protocolAction_exceptionCode.2024",new ActionMessage("protocolAction_exceptionCode.2024"));
           saveMessages(request, actionMessages); 
           error = true;
        }else if(responseCode.equals("2031")){
           actionMessages.add("protocolAction_exceptionCode.2031",new ActionMessage("protocolAction_exceptionCode.2031"));
           saveMessages(request, actionMessages); 
           error = true;
        }else if(responseCode.equals("2032")){
           actionMessages.add("protocolAction_exceptionCode.2032",new ActionMessage("protocolAction_exceptionCode.2032"));
           saveMessages(request, actionMessages);   
           error = true;
        }else if(responseCode.equals("S")){
           actionMessages.add("protocolAction.msg.success",new ActionMessage("protocolAction.msg.success",protocolNumber));
           saveMessages(request, actionMessages);  
           error = true;
        //Added for including new actions - start   
        }else if(responseCode.equals("2047")){
           actionMessages.add("protocolAction_exceptionCode.2047",new ActionMessage("protocolAction_exceptionCode.2047"));
           saveMessages(request, actionMessages);   
           error = true;            
        }else if(responseCode.equals("2048")){
           actionMessages.add("protocolAction_exceptionCode.2048",new ActionMessage("protocolAction_exceptionCode.2048"));
           saveMessages(request, actionMessages);   
           error = true;            
        }else if(responseCode.equals("2049")){
           actionMessages.add("protocolAction_exceptionCode.2049",new ActionMessage("protocolAction_exceptionCode.2049"));
           saveMessages(request, actionMessages);   
           error = true;            
        }else if(responseCode.equals("2050")){
           actionMessages.add("protocolAction_exceptionCode.2050",new ActionMessage("protocolAction_exceptionCode.2050"));
           saveMessages(request, actionMessages);   
           error = true;            
        //Added for including new actions - end           
        //Added for response and expedited approval actions - start - 3
        }else if(responseCode.equals("2044")){
           actionMessages.add("protocolAction_exceptionCode.2044",new ActionMessage("protocolAction_exceptionCode.2044"));
           saveMessages(request, actionMessages);   
           error = true;            
        }else if(responseCode.equals("2014")){
           actionMessages.add("protocolAction_exceptionCode.2014",new ActionMessage("protocolAction_exceptionCode.2014"));
           saveMessages(request, actionMessages);   
           error = true;            
        }else if(responseCode.equals("2027")){
           actionMessages.add("protocolAction_exceptionCode.2027",new ActionMessage("protocolAction_exceptionCode.2027"));
           saveMessages(request, actionMessages);   
           error = true;            
        }else if(responseCode.equals("2045")){
           actionMessages.add("protocolAction_exceptionCode.2045",new ActionMessage("protocolAction_exceptionCode.2045"));
           saveMessages(request, actionMessages);   
           error = true;            
        }else if(responseCode.equals("2028")){
           actionMessages.add("protocolAction_exceptionCode.2028",new ActionMessage("protocolAction_exceptionCode.2028"));
           saveMessages(request, actionMessages);   
           error = true;            
        }else if(responseCode.equals("2046")){
           actionMessages.add("protocolAction_exceptionCode.2046",new ActionMessage("protocolAction_exceptionCode.2046"));
           saveMessages(request, actionMessages);   
           error = true;            
        }else if(responseCode.equals("4010")){
           actionMessages.add("iacucProtocolAction_exceptionCode.4010",new ActionMessage("iacucProtocolAction_exceptionCode.4010"));
           saveMessages(request, actionMessages);   
           error = true;            
        }else if(responseCode.equals("4006")){
           actionMessages.add("iacucProtocolAction_exceptionCode.4006",new ActionMessage("iacucProtocolAction_exceptionCode.4006"));
           saveMessages(request, actionMessages);   
           error = true;            
        }else if(responseCode.equals("4008")){
           actionMessages.add("iacucProtocolAction_exceptionCode.4008",new ActionMessage("iacucProtocolAction_exceptionCode.4008"));
           saveMessages(request, actionMessages);   
           error = true;   
        }else if(responseCode.equals("4007")){
           actionMessages.add("iacucProtocolAction_exceptionCode.4007",new ActionMessage("iacucProtocolAction_exceptionCode.4007"));
           saveMessages(request, actionMessages);   
           error = true;   
        }else if(responseCode.equals("4009")){
           actionMessages.add("iacucProtocolAction_exceptionCode.4009",new ActionMessage("iacucProtocolAction_exceptionCode.4009"));
           saveMessages(request, actionMessages);   
           error = true;   
        }else if(responseCode.equals("4011")){
           actionMessages.add("iacucProtocolAction_exceptionCode.4011",new ActionMessage("iacucProtocolAction_exceptionCode.4011"));
           saveMessages(request, actionMessages);   
           error = true;   
        }
        //Added for response and expedited approval actions - end - 3
        // Added for case #2250 - Expire Protocol - start 
        if(responseCode.equals("2065")){
            actionMessages.add("protocolAction_exceptionCode.2065",new ActionMessage("protocolAction_exceptionCode.2065"));
            saveMessages(request, actionMessages);   
            error = true; 
        }else if(responseCode.equals("2034")){
            actionMessages.add("protocolAction_exceptionCode.2034",new ActionMessage("protocolAction_exceptionCode.2034"));
            saveMessages(request, actionMessages);   
            error = true; 
        }
        // Added for case #2250 - Expire Protocol - End
        //Added for case#3214 - Withdraw Submission - Start
        else if(responseCode.equals("4025")){
            actionMessages.add("iacucProtocolAction_exceptionCode.4025",new ActionMessage("iacucProtocolAction_exceptionCode.4025"));
            saveMessages(request, actionMessages);   
            error = true;        
        }else if(responseCode.equals("4029")){
            actionMessages.add("iacucProtocolAction_exceptionCode.4029",new ActionMessage("iacucProtocolAction_exceptionCode.4029"));
            saveMessages(request, actionMessages);   
            error = true; 
        }else if(responseCode.equals("2012")){
            actionMessages.add("protocolAction_exceptionCode.2012",new ActionMessage("protocolAction_exceptionCode.2012"));
            saveMessages(request, actionMessages);   
            error = true;                        
        } else if(responseCode.equals(RECORD_ALREADY_LOCKED_ERROR_CODE)){
            actionMessages.add("protocolAction_exceptionCode."+RECORD_ALREADY_LOCKED_ERROR_CODE,new ActionMessage("protocolAction_exceptionCode."+RECORD_ALREADY_LOCKED_ERROR_CODE));
            saveMessages(request, actionMessages);   
            error = true;  
        }else if(responseCode.equals("2015")){
            actionMessages.add("protocolAction_exceptionCode.2015",new ActionMessage("protocolAction_exceptionCode.2015"));
            saveMessages(request, actionMessages);   
            error = true;  
        // Modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
        }else if(responseCode.equals("4061")){
            actionMessages.add("iacucProtocolAction_exceptionCode.4061",new ActionMessage("iacucProtocolAction_exceptionCode.4061"));
            saveMessages(request, actionMessages);   
            error = true;  
        }
        // Modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
        //Added for case#3214 - Withdraw Submission - end
        if(error){
            //Indicates an action cannot be performed for some reason so
            //set the ERROR flag to YES, so that ONLY the error message
            //is visible AND all the controls on the view (JSP) are hidden.          
            session.setAttribute(ERROR, YES);
        }else{
            //Indicates that an action can be performed
            //so, set the ERROR flag to NO
            session.setAttribute(ERROR, NO);
        }
    }
    
    
     /**     
     * This method saves the protocol action to database tables
     * OSP$PROTOCOL_ACTIONS, OSP$PROTOCOL_SUBMISSION
     * and correspondences, if any to OSP$PROTOCOL_CORRESPONDENCE table
     * @param request     
     * @param actionForm
     * @return navigator     
     * @throws Exception
     */     
    private boolean saveProtocolActions(HttpServletRequest request, ActionForm actionForm, 
            ProtocolActionForm protocolActionForm) throws Exception{
        HashMap hmSubmissionData = new HashMap();      
        HttpSession session = request.getSession();
        HashMap hmSubmissionDetails = (HashMap)session.getAttribute(IACUC_SUBMISSION_DETAILS);
        String committeeId = (String)hmSubmissionDetails.get(COMMITTEE_ID);
        //Added to include committeeId - start - 1
        //String committeeId = request.getParameter(COMMITTEE_ID);
        //Added to include committeeId - end - 1
        String actionCode = (String)session.getAttribute(IACUC_ACTION_CODE);
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());   
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String loggedinUser = userInfoBean.getUserId();                           
        //Commented for case#3046 - ability to upload an attachment
        //DynaActionForm dynaForm = (DynaActionForm)actionForm;        
        SimpleDateFormat dtFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);    
        //To indicate approval action status, if an approval actions has errors, it is set to false.
        boolean saveSuccess = true;
        ActionMessages actionMessages = new ActionMessages();
        
        //Get Submission Details        
        hmSubmissionData = (HashMap)session.getAttribute(IACUC_SUBMISSION_DETAILS);
        String submissionNumber = (String)hmSubmissionData.get(SUBMISSION_NUMBER);
        if(submissionNumber == null || submissionNumber.equals(EMPTY_STRING)){
            hmSubmissionData.put(SUBMISSION_NUMBER, ONE);
            submissionNumber = (String)hmSubmissionData.get(SUBMISSION_NUMBER);
        }

        //Set actionBean properties
        ProtocolActionsBean actionBean = new ProtocolActionsBean();
        actionBean.setProtocolNumber(protocolNumber);
        actionBean.setSequenceNumber(Integer.parseInt(sequenceNumber));
        actionBean.setScheduleId((String)hmSubmissionData.get(SCHEDULE_ID));
        // Modified and Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start    
        if((IacucProtocolActionsConstants.WITHDRAWN+"").equals(actionCode)){
//            actionBean.setSubmissionNumber(Integer.parseInt(submissionNumber));
            //When with draw action performed from lite the protocol status will be set to 'Withdrawn'
            actionBean.setSubmissionNumber(0);        
        }else if((IacucProtocolActionsConstants.PROTOCOL_ABANDON+"").equals(actionCode)){
            actionBean.setSubmissionNumber(Integer.parseInt(submissionNumber));
        }else  {
            actionBean.setSubmissionNumber(Integer.parseInt(submissionNumber) +1);
        }
        // Modified and Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
        //Commented for case#3046 - Notify IRB attachments - start
        //actionBean.setComments((String)dynaForm.get(COMMENTS));
        //actionBean.setActionDate(new java.sql.Date(dtFormat.parse(getFormattedDate(dynaForm.get("actionDate").toString())).getTime()));
        //Commented for case#3046 - Notify IRB attachments - end
        //Added for case#3046 - Notify IRB attachments - start
        actionBean.setComments(protocolActionForm.getComments());
        actionBean.setActionDate(new java.sql.Date(dtFormat.parse(getFormattedDate(protocolActionForm.getActionDate())).getTime()));
        //Added for case#3046 - Notify IRB attachments - start
        actionBean.setActionTypeCode(Integer.parseInt(actionCode));
        actionBean.setUpdateUser(loggedinUser);        
        //Added to include committeeId - start - 2
        // modified for case # 2250 - Expire Protocol - Start
        if(committeeId != null && !committeeId.equals("")){
            actionBean.setCommitteeId(committeeId);
        }else{
            //Modified for case#2250 - Expire Protocol
            if(hmSubmissionData.get(COMMITTEE_ID) != null){
                actionBean.setCommitteeId(hmSubmissionData.get(COMMITTEE_ID).toString());
            }
        }
        //modified for case # 2250 - Expire protocol - End
        //Added to include committeeId - end - 2
        //Code added for Case#3554 - Notify IRB enhancement - starts
        //To set the review type as FYI for Notify IRB
        if(actionCode != null && actionCode.equals(""+IacucProtocolActionsConstants.NOTIFY_IACUC)){
            actionBean.setSubmissionTypeCode(protocolActionForm.getNotificationType());
            actionBean.setReviwerTypeCode(FYI_REVIEW+"");
            actionBean.setCommitteeId(committeeId);
        }
        //Code added for Case#3554 - Notify IRB enhancement - ends
        //Added for response and expedited approval actions - start - 4
        if(actionCode.equals(EXPEDITED_APPROVAL_ACTION_CODE) || actionCode.equals(RESPONSE_APPROVAL_ACTION_CODE)){            
            //Commented for case#3046 - Notify IRB attachments - start
            //String approvalDate = (String)dynaForm.get(APPROVAL_DATE);
            //String expirationDate = (String)dynaForm.get(EXPIRATION_DATE);
            //Commented for case#3046 - Notify IRB attachments - end
            //Added for case#3046 - Notify IRB attachments - start
            String approvalDate = protocolActionForm.getApprovalDate();
            String expirationDate = protocolActionForm.getExpirationDate();          
            //Added for case#3046 - Notify IRB attachments - end
            if((approvalDate == null || approvalDate.equals(EMPTY_STRING)) && (expirationDate != null && !expirationDate.equals(EMPTY_STRING))){
                saveSuccess = false;
                actionMessages.add("protocolAction_exceptionCode.date1",new ActionMessage("protocolAction_exceptionCode.date1"));
                saveMessages(request, actionMessages);                
            }
            if((expirationDate == null || expirationDate.equals(EMPTY_STRING)) &&  (approvalDate != null && !approvalDate.equals(EMPTY_STRING))){
                saveSuccess = false;
                actionMessages.add("protocolAction_exceptionCode.date2",new ActionMessage("protocolAction_exceptionCode.date2"));
                saveMessages(request, actionMessages); 
            }
            if((approvalDate == null || approvalDate.equals(EMPTY_STRING)) && (expirationDate == null || expirationDate.equals(EMPTY_STRING))){
                saveSuccess = false;
                actionMessages.add("protocolAction_exceptionCode.date3",new ActionMessage("protocolAction_exceptionCode.date3"));
                saveMessages(request, actionMessages); 
            }  
            if((approvalDate != null && !approvalDate.equals(EMPTY_STRING)) && (expirationDate != null && !expirationDate.equals(EMPTY_STRING))){
                if(compareDates(approvalDate, expirationDate)){
                    saveSuccess = false;
                    actionMessages.add("protocolAction_exceptionCode.date4",new ActionMessage("protocolAction_exceptionCode.date4"));
                    saveMessages(request, actionMessages);                     
                }                
            }
            if(saveSuccess){
                //Commented for case#3046 - Notify IRB attachments - start
                //actionBean.setApprovalDate(new java.sql.Date(dtFormat.parse(getFormattedDate(dynaForm.get(APPROVAL_DATE).toString())).getTime()));
                //actionBean.setExpirationDate(new java.sql.Date(dtFormat.parse(getFormattedDate(dynaForm.get(EXPIRATION_DATE).toString())).getTime()));                            
                //Commented for case#3046 - Notify IRB attachments - end
                //Added for case#3046 - Notify IRB attachments - start
                actionBean.setApprovalDate(new java.sql.Date(dtFormat.parse(getFormattedDate(protocolActionForm.getApprovalDate())).getTime()));
                actionBean.setExpirationDate(new java.sql.Date(dtFormat.parse(getFormattedDate(protocolActionForm.getExpirationDate())).getTime()));                                            
                //Added for case#3046 - Notify IRB attachments - end
            //    HashMap hmSubmissionDetails = (HashMap)session.getAttribute(IACUC_SUBMISSION_DETAILS);
                actionBean.setCommitteeId((String)hmSubmissionDetails.get(COMMITTEE_ID));                
            }
        }
        //Added for response and expedited approval actions - end - 4 
        
        if(saveSuccess){
            //Save action to database and get actionId
            ActionTransaction actionTransaction = new ActionTransaction(Integer.parseInt(actionCode));
            int actionId = actionTransaction.performAction(actionBean, loggedinUser);
            actionBean.setActionId(actionId);
            String maxSeqNum = getMaxSequenceNumber(request,protocolNumber,sequenceNumber,actionCode);            
            actionBean.setSequenceNumber(Integer.parseInt(maxSeqNum));
            
            //For an amendment/renewal protocol, set the 14 digit protocol number
            //to session scope with protoAmendRenewNumber as identifier
            if(protocolNumber != null && protocolNumber.length() > 10){
                session.removeAttribute("protoAmendRenewNumber");
                session.setAttribute("protoAmendRenewNumber", protocolNumber);            
            }else{
                session.removeAttribute("protoAmendRenewNumber");
            }
            
            //After an approval action is performed, for an amendment/renewal protocol
            //truncate the protocol number to original protocol number,
            //and set the same to session scope
            if(actionCode.equals(EXPEDITED_APPROVAL_ACTION_CODE) || actionCode.equals(RESPONSE_APPROVAL_ACTION_CODE) || actionCode.equals(EXPIRE_PROTOCOL_CODE)){//Modified for Case#2250 - Expire Protocol
                if(protocolNumber != null && protocolNumber.length() > 10){
                    protocolNumber = protocolNumber.substring(0,10);   
                    session.removeAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER);
                    session.setAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId(), protocolNumber);                    
                }    
                //Set the latest sequence number to session scope after an action is performed                
                HashMap hmProtocolDetails = getProtocolDetails(request, protocolNumber);            
                session.removeAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER);
                session.setAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId(), hmProtocolDetails.get(SEQUENCE_NUMBER).toString()); 
                session.setAttribute(CoeusLiteConstants.IACUC_VERSION_NUMBER+session.getId(), hmProtocolDetails.get("versionNumber").toString()); 
            }                
            //Added for case#3046 - Notify IRB attachments - start
            // COEUSDEV-327 / COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document -Start
            // Allow uploading of the attachments for the following actions
//            if(actionCode.equals(NOTIFY_IRB_CODE)){
            //  if(protocolActionForm.getDocument().getFileData().length != 0){
            if((""+IacucProtocolActionsConstants.NOTIFY_IACUC).equals(actionCode)
            || (""+IacucProtocolActionsConstants.REQUEST_TO_DEACTIVATE).equals(actionCode)
            || (""+IacucProtocolActionsConstants.REQUEST_TO_LIFT_HOLD).equals(actionCode)){
            // COEUSDEV-327 / COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document - End
//                protocolActionForm.setProtocolNumber(protocolNumber);
//                protocolActionForm.setSequenceNumber(Integer.parseInt(sequenceNumber));
//                protocolActionForm.setUpdateUser(loggedinUser);
                
                ProtocolActionForm formWithAttachments = (ProtocolActionForm) session.getAttribute("formWithAttchmentsForSubmission");
                if(formWithAttachments != null && formWithAttachments.getVecAttachments() != null){
                    saveAttachments(request, formWithAttachments);
                }
                // }
            }
            //Added for case#3046 - Notify IRB attachments - end
            
            //Generate correspondences        
            generateCorrespondence(request, actionBean);
        }
        return saveSuccess;
    }
       
     /**     
     * This method gets the submission details for a given protocol  
     * @param request     
     * @param protocolNumber
     * @return HashMap hmData
     * @throws Exception
     */
    /* Commented no longer used
    private HashMap getSubmissionDetails(HttpServletRequest request, String protocolNumber) throws Exception{
        
        HashMap hmData = new HashMap();        
        WebTxnBean webTxnBean = new WebTxnBean();  
        String scheduleId = EMPTY_STRING;
        String protocolActionTypeCode = EMPTY_STRING; 
        String submissionNumber = EMPTY_STRING;   
        String comments = EMPTY_STRING; 
        
        hmData.put(PROTOCOL_NUMBER,protocolNumber);
        Hashtable htOutputData =(Hashtable)webTxnBean.getResults(request,GET_PROTO_SUBMISSION_DETAIL, hmData);
        Vector vecOutputData = (Vector) htOutputData.get(GET_PROTO_SUBMISSION_DETAIL);         
        
        if(vecOutputData != null && vecOutputData.size() > 0){            
            for(int index = 0; index < vecOutputData.size(); index++){
                DynaActionForm dynaActionForm = (DynaActionForm)vecOutputData.get(index);
                scheduleId = (String)dynaActionForm.get(SCHEDULE_ID);
                comments = (String)dynaActionForm.get(COMMENTS);  
                submissionNumber = dynaActionForm.get(SUBMISSION_NUMBER).toString();                  
                protocolActionTypeCode = dynaActionForm.get(PROTO_ACTION_TYPE_CODE).toString();                  
            }
        } 
        
        hmData.clear();
        hmData.put(SCHEDULE_ID, scheduleId);
        hmData.put(COMMENTS, comments);
        hmData.put(SUBMISSION_NUMBER, submissionNumber);
        hmData.put(PROTO_ACTION_TYPE_CODE, protocolActionTypeCode);
        
        return hmData;        
    }
    */
    
    /**
     * This method generates Correspondence for the action
     * @param request
     * @param actionBean
     * @throws Exception
     * @return boolean
     */
    private boolean generateCorrespondence(HttpServletRequest request, ProtocolActionsBean actionBean)                        
        throws Exception, CoeusException, 
        javax.xml.bind.JAXBException, javax.xml.parsers.ParserConfigurationException, 
        javax.xml.transform.TransformerConfigurationException, 
        javax.xml.transform.TransformerException, IOException, 
        org.apache.fop.apps.FOPException{
        
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String loggedinUser = userInfoBean.getUserId();        
        boolean blnCorrGenerated = false;
        
        CorrespondenceTypeFormBean correpTypeFormBean = null;
        ProtoCorrespTypeTxnBean protoCorrespTypeTxnBean = new ProtoCorrespTypeTxnBean(loggedinUser);
        ProtoCorrespRecipientsBean protoCorrespRecipientsBean = null;
        Vector validActionCorrespTypes = null;
        try{
            validActionCorrespTypes = protoCorrespTypeTxnBean.getValidProtoActionCorrespTypes(actionBean);
            byte[] templateFileBytes;

            Vector vctReceipients = null;
            Vector vctRecp = null;        

            if(validActionCorrespTypes != null && validActionCorrespTypes.size() > 0){
                for(int row = 0; row < validActionCorrespTypes.size(); row++ ){
                    correpTypeFormBean = (CorrespondenceTypeFormBean)validActionCorrespTypes.elementAt(row);
                    //Modified for the case# COEUSDEV_229-generate correspondence
                    templateFileBytes = protoCorrespTypeTxnBean.getCorrespTemplate(correpTypeFormBean.getProtoCorrespTypeCode(), actionBean.getScheduleId(),actionBean.getCommitteeId());
                    if(templateFileBytes != null){                    
                        XMLStreamGenerator xmlStreamGenerator = new XMLStreamGenerator();
                        Document xmlDoc = xmlStreamGenerator.correspondenceXMLStreamGenerator(actionBean);
                        UtilFactory.log("Xml doc generating complete!");
                        XMLtoPDFConvertor conv = new XMLtoPDFConvertor();
                        boolean fileGenerated = conv.generatePDF(xmlDoc, templateFileBytes);
                        if (fileGenerated){    
                           templateFileBytes = conv.getGeneratedPdfFileBytes();
                        }                    
                        blnCorrGenerated = protoCorrespTypeTxnBean.addUpdProtocolCorrespondence(actionBean, correpTypeFormBean.getProtoCorrespTypeCode(), templateFileBytes);
                        vctReceipients = protoCorrespTypeTxnBean.getCorrespondenceReceipients(actionBean.getProtocolNumber(),actionBean.getSequenceNumber(),correpTypeFormBean.getProtoCorrespTypeCode());
                        String strMailIds = "";
                        vctRecp = new Vector();
                        if(vctReceipients != null && vctReceipients.size() > 0){
                            for(int intRecepRow = 0; intRecepRow < vctReceipients.size(); intRecepRow++){
                                protoCorrespRecipientsBean = (ProtoCorrespRecipientsBean)vctReceipients.elementAt(intRecepRow);
                                strMailIds =  strMailIds + protoCorrespRecipientsBean.getMailId()+",";
                                protoCorrespRecipientsBean.setProtocolNumber(actionBean.getProtocolNumber());
                                protoCorrespRecipientsBean.setProtoCorrespTypeCode(correpTypeFormBean.getProtoCorrespTypeCode());
                                protoCorrespRecipientsBean.setActionId(actionBean.getActionId());
                                protoCorrespRecipientsBean.setAcType("I");
                                protoCorrespRecipientsBean.setNumberOfCopies(1);
                                vctRecp.addElement(protoCorrespRecipientsBean);
                            }
                            if(vctRecp.size()>0){
                                protoCorrespTypeTxnBean.addUpdCorrespRecipients(vctRecp);
                            }
                        }
                    }
                } 
            }else{
                blnCorrGenerated = false;
            }
        }catch(Exception e){
            UtilFactory.log(e.getMessage(), e,"ProtocolRequestAction","generateCorrespondence");
            blnCorrGenerated = false;
            return blnCorrGenerated;
        }
        return blnCorrGenerated; 
    }

    /**
     * 
     * This method resets the form field
     * 
     * 
     * @param protocolActionForm ProtocolActionForm
     */
      //Commented for case#3046 - Notify IRB attachments
//    private void resetFormField(DynaActionForm protocolActionsForm){
//        protocolActionsForm.set(COMMENTS, EMPTY_STRING);
//        protocolActionsForm.set(COMMITTEE_ID, ZERO);
//    }
    //Added for case#3046 - Notify IRB attachments
    private void resetFormField(ProtocolActionForm protocolActionForm){
        protocolActionForm.setComments(EMPTY_STRING);
        protocolActionForm.setCommitteeId(ZERO);
        // COEUSDEV-327 / COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document -Start
        protocolActionForm.setVecAttachments(null);
        protocolActionForm.setDescription("");
        protocolActionForm.setFileName("");
        protocolActionForm.setAcType("");
        protocolActionForm.setDocumentId(0);
        // COEUSDEV-327 / COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document - End
        protocolActionForm.setActionDate(EMPTY_STRING);
    }    
    
    /**
     * This method gets the max sequence number for a given protocol number
     * @param request
     * @param protocolNumber
     * @param sequenceNumber
     * @param actionId
     * @return maxSeqNum
     */
    private String getMaxSequenceNumber(HttpServletRequest request, String protocolNumber,
        String sequenceNumber, String actionId) throws Exception{
        
        String maxSeqNum = "1";                
        HashMap hmData = new HashMap();        
        WebTxnBean webTxnBean = new WebTxnBean();           
        hmData.put(PROTOCOL_NUMBER,protocolNumber);
        hmData.put(SEQUENCE_NUMBER,new Integer(sequenceNumber));
        hmData.put(ACTION_ID,new Integer(actionId));        
        Hashtable htOutputData =(Hashtable)webTxnBean.getResults(request,GET_MAX_SEQ_NUMBER, hmData);
        if(htOutputData != null && htOutputData.size() > 0){
            hmData = (HashMap)htOutputData.get(GET_MAX_SEQ_NUMBER);
            maxSeqNum = hmData.get(MAX_SEQ_NUM).toString();
        }
        return maxSeqNum;
    }
    
    /**
     * This method gets all the committes
     * and sets the collection to session scope
     * @param request
     * @throws Exception
     */
    private void getCommittees(HttpServletRequest request) throws Exception{
        
        Map hmCommittees = new HashMap();
        Vector vecCommittees = null;        
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        session.removeAttribute(COMMITTEES);
        //Modified for IACUC Changes - Start
//        hmCommittees = (Hashtable)webTxnBean.getResults(request, GET_ALL_COMMITTEES, null);
        Map hmCommitteeType = new HashMap();
        hmCommitteeType.put("matchingCommitteeTypeCode", new Integer(CoeusConstants.IACUC_COMMITTEE_TYPE_CODE));
        hmCommittees = (Hashtable)webTxnBean.getResults(request, GET_ALL_COMMITTEES, hmCommitteeType);
        //IACUC Changes - End
        vecCommittees = (Vector) hmCommittees.get(GET_ALL_COMMITTEES);           
        if(vecCommittees == null){
            vecCommittees = new Vector();
        } else {
            for(int index = 0; index < vecCommittees.size(); index++){
                ComboBoxBean comboBoxBean = (ComboBoxBean)vecCommittees.get(index);
                comboBoxBean.setDescription(comboBoxBean.getCode()+" : "+comboBoxBean.getDescription());
            }
        }            
        session.setAttribute(COMMITTEES, vecCommittees);        
    }
    
    /**
     * This method sets ERROR, IACUC_ACTION_CODE, IACUC_SUBMISSION_DETAILS and APPROVAL_ACTION_ERROR_FLAG
     * values to session scope
     * 
     * 
     * @param request
     * @throws Exception
     */
    private void initActions(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession(); 
        //Added for response and expedited approval actions - start - 5
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()); 
        //Get the submission details of last non notify irb
        //and set the collection to session scope
        HashMap hmSubmissionDetails = new HashMap();
        hmSubmissionDetails = getNonNotifyIACUCCommitteeForProtocol(request, protocolNumber);        
        session.removeAttribute(IACUC_SUBMISSION_DETAILS);
        session.setAttribute(IACUC_SUBMISSION_DETAILS, hmSubmissionDetails);
        //Initially set the APPROVAL_ACTION_ERROR_FLAG flag to NO
        //to indicate that no errors occured on approval action
        session.removeAttribute(APPROVAL_ACTION_ERROR_FLAG);
        session.setAttribute(APPROVAL_ACTION_ERROR_FLAG, NO);
        //Initially set the SUCCESS_FLAG to NO
        //indicating that the action performed is not success
        session.removeAttribute(SUCCESS_FLAG);
        session.setAttribute(SUCCESS_FLAG, NO);        
        //Added for response and expedited approval actions - end - 5        
        //Initially, we assume that there are no errors, so the ERROR
        //flag is set to NO, so that all the controls are visible on the view (JSP)
        session.removeAttribute(ERROR); 
        session.setAttribute(ERROR, NO);
        //Initial, IACUC_ACTION_CODE is set to A
        session.removeAttribute(IACUC_ACTION_CODE);
        session.setAttribute(IACUC_ACTION_CODE, request.getParameter(IACUC_ACTION_CODE));        
    }
    
    /**
     * This method checks whether request to data analysis action can be performed
     * @param request
     * @return navigator
     * @throws Exception
     */
    private String performRequestSubmissionOperation(HttpServletRequest request) throws Exception{        
        String navigator = SUCCESS;        
        String responseCode = isAuthorizedToPerformAction(request);
        throwMessage(request, responseCode);
        return navigator;          
    }    
  
    /**
     * This method checks whether request to re open enrollment action can be performed
     * @param request
     * @return navigator
     * @throws Exception
     */
    private String performRequestToReopenOperation(HttpServletRequest request) throws Exception{        
        String navigator = SUCCESS;        
        String responseCode = isAuthorizedToPerformAction(request);
        throwMessage(request, responseCode);
        return navigator;          
    } 
    
    /**
     * Code modified for Case#3554 - Notify IRB enhancement
     * This method checks the whether user has rights
     * to perform Notify IRB action, if he has then
     * displays the page, otherwise throws error meaage
     * @param request
     * @param protocolActionForm
     * @return navigator     
     */
    private String performNotifyIACUCOperation(HttpServletRequest request,
            ProtocolActionForm protocolActionForm) throws Exception{  
        
        String navigator = SUCCESS;          
        String responseCode = MINUS_ONE;
        HttpSession session = request.getSession();  
        
        session.removeAttribute(ERROR);
        // COEUSDEV-327 / COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document 
//        String actionCode = request.getParameter(IACUC_ACTION_CODE);
//        if(actionCode == null){
//            actionCode = (String)session.getAttribute(IACUC_ACTION_CODE);
//        }
//        session.setAttribute(IACUC_ACTION_CODE, actionCode);
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());     
        String leadUnitNumber = getLeadUnitNumber(request, protocolNumber);
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String loggedinUser = userInfoBean.getUserId();
        UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();        
        if (txnData.getUserHasIACUCProtocolRight(loggedinUser, SUBMIT_IACUC_PROTOCOL, protocolNumber) 
        || txnData.getUserHasRight(loggedinUser, MODIFY_ANY_IACUC_PROTOCOL, leadUnitNumber)){                        
            session.setAttribute(ERROR, NO);
        }    
        else{
            responseCode = ERROR_4999; 
            throwMessage(request, responseCode);
        }        
        //Code added for Case#3554 - Notify IRB enhancement - starts
        //set the values for the Notify submission
        Map hmQualifiers = new HashMap();
        hmQualifiers.put("submissionTypeCode", "104");
        hmQualifiers.put("protocolNumber", protocolNumber);
        hmQualifiers.put("jobCode", "IACUC_COMM_SELECTION_DURING_SUBMISSION");        
        WebTxnBean webTxnBean = new WebTxnBean();
        session.removeAttribute(QUALIFIER_TYPE);
        hmQualifiers = (Hashtable)webTxnBean.getResults(request, "getNotifyIacucData", hmQualifiers);
        Vector vecQualifiers = (Vector) hmQualifiers.get(GET_QUALIFIER_TYPE);
        vecQualifiers = (vecQualifiers == null)? new Vector() : vecQualifiers;
        Vector vecSubCommittee = (Vector) hmQualifiers.get("getIacucSubmissionCommittee");
        if(vecSubCommittee != null && vecSubCommittee.size() > 0){
            DynaActionForm dynaForm = (DynaActionForm) vecSubCommittee.get(0);
            protocolActionForm.setCommitteeId((String)dynaForm.get("committeeId")); 
        } else {
            protocolActionForm.setCommitteeId(ZERO);
        }
        String value =(String) ((HashMap)hmQualifiers.get("getDefaultJobCode")).get("ls_value");
        protocolActionForm.setComments(EMPTY_STRING);
        protocolActionForm.setNotificationType(EMPTY_STRING);
        session.setAttribute("IACUC_COMM_SELECTION_DURING_SUBMISSION", value);
        session.setAttribute(QUALIFIER_TYPE, vecQualifiers);
        //Code added for Case#3554 - Notify IRB enhancement - ends
        // COEUSDEV-327 / COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document -Start
        protocolActionForm.setVecAttachments(null);
        protocolActionForm.setDescription("");
        protocolActionForm.setFileName("");
        setSavedTempSubmissionDataToForm(protocolActionForm, request);
        // COEUSDEV-327 / COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document - End
        
        return navigator;          
    } 
    
    /**
     * //Added for response and expedited approval actions - start - 6
     * This method checks whether approval actions can be performed
     * @param request
     * @return navigator
     * @throws Exception
     */
    private String checkApprovalAction(HttpServletRequest request) throws Exception{        
        String navigator = SUCCESS;        
        String responseCode = isAuthorizedToPerformApprovalAction(request);
        throwMessage(request, responseCode);
        return navigator;          
    }    
        
     /**
     * This method checks whether the protocol and user have rights
     * for performing approval actions.
     * Also calls the canPerformProtocolAction method which specifies 
     * whether action can be performed or not.
     * @param request     
     * @return navigator
     * @throws Exception
     */     
    private String isAuthorizedToPerformApprovalAction(HttpServletRequest request) throws Exception{
        
        String responseCode = MINUS_ONE;
        HttpSession session = request.getSession();        
        session.removeAttribute(ERROR);
        String actionCode = request.getParameter(IACUC_ACTION_CODE);
        if(actionCode == null){
            actionCode = (String)session.getAttribute(IACUC_ACTION_CODE);
        }
        session.setAttribute(IACUC_ACTION_CODE, actionCode);
        
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());   
        HashMap hmSubmissionDetails = new HashMap();
        hmSubmissionDetails = (HashMap)session.getAttribute(IACUC_SUBMISSION_DETAILS);        
        String committeeId = (String)hmSubmissionDetails.get(COMMITTEE_ID);
        String scheduleId = (String)hmSubmissionDetails.get(SCHEDULE_ID);
          
        responseCode = canPerformProtocolAction(request, protocolNumber);            
        if (responseCode != ONE){   
            return responseCode ;
        }          
        if(!committeeId.equals(EMPTY_STRING)){
            responseCode = checkProtocolCommitteeForRight(request, ACTION_RIGHT, committeeId, protocolNumber, false);
            return responseCode ;    
        }else if(!scheduleId.equals(EMPTY_STRING)){
            responseCode = checkProtocolScheduleForRight(request, ACTION_RIGHT, scheduleId, protocolNumber, false); 
            return responseCode;    
        }            
        return responseCode;
        
    }
    
    /**
     * This method checks whether a given protocol committee has rights
     * @param request
     * @param userRight
     * @param committeeId
     * @param protocolNumber
     * @param checkCanPerform
     * @return responseCode     
     */
    private String checkProtocolCommitteeForRight(HttpServletRequest request, String userRight, 
            String committeeId, String protocolNumber, boolean checkCanPerform) throws Exception{
        
        String responseCode = MINUS_ONE;
        HttpSession session = request.getSession();
        UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        
        if(txnData.getUserHasRight(userInfoBean.getUserId(), userRight, getCommitteeHomeUnitForCommittee(request, committeeId))){
            if (checkCanPerform){                    
                responseCode = canPerformProtocolAction(request, protocolNumber);
            }
            else{       
                responseCode = ONE;
            }    
        }
        else{
            responseCode = ERROR_4999;
        }         
        return responseCode ;
    } 
    
    /**
     * This method gets the home unit number for a given committee ID
     * @param request
     * @param committeeId
     * @return unitNumber
     */
    private String getCommitteeHomeUnitForCommittee(HttpServletRequest request, String committeeId)  throws Exception{
        
        String unitNumber = EMPTY_STRING;  
        HashMap hmData = new HashMap();        
        WebTxnBean webTxnBean = new WebTxnBean();           
        //hmData.put("AW_COMMITTEE_ID", committeeId);
        hmData.put("committeeId", committeeId);
        //Vector vecOutputData =(Vector)webTxnBean.getResults(request, "getCommitteeInfo", hmData);
        Hashtable htOutputData = (Hashtable)webTxnBean.getResults(request, "getCommitteeInfo", hmData);
        Vector vecOutputData = (Vector)htOutputData.get("getCommitteeInfo");
        if(vecOutputData != null && vecOutputData.size() > 0){   
            //Commented/Added for case#3046 - Notify IRB attachments - start
            //DynaActionForm dynaForm = (DynaActionForm)vecOutputData.get(0);
            //unitNumber = (String)dynaForm.get("HOME_UNIT_NUMBER");
            //unitNumber = (String)dynaForm.get("unitNumber");
            ProtocolActionForm protocolActionForm = (ProtocolActionForm)vecOutputData.get(0);
            unitNumber = protocolActionForm.getUnitNumber();
            //Commented/Added for case#3046 - Notify IRB attachments - end
        }
        return unitNumber;       
    }    
    
    /**
     * This method checks whether a given protocol schedule has rights
     * @param request
     * @param userRight
     * @param scheduleId
     * @param protocolNumber
     * @param checkCanPerform
     * @return responseCode     
     */
    private String checkProtocolScheduleForRight(HttpServletRequest request, String userRight, 
            String scheduleId, String protocolNumber, boolean checkCanPerform) throws Exception{
        
        String responseCode = MINUS_ONE;
        HttpSession session = request.getSession();
        UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        
        if(txnData.getUserHasRight(userInfoBean.getUserId(), userRight, getCommitteeHomeUnitForSchedule(request, scheduleId))){
            if (checkCanPerform){                    
                responseCode = canPerformProtocolAction(request, protocolNumber);
            }
            else{       
                responseCode = ONE;
            }    
        }
        else{
            responseCode = ERROR_4999;
        }         
        return responseCode ;
    }    
    
    /**
     * This method gets the home unit number for a given schedule ID
     * @param request
     * @param scheduleId
     * @return unitNumber
     */    
    private String getCommitteeHomeUnitForSchedule(HttpServletRequest request, String scheduleId)  throws Exception{
        
        String unitNumber = EMPTY_STRING;  
        HashMap hmData = new HashMap();        
        WebTxnBean webTxnBean = new WebTxnBean();           
        hmData.put("AW_SCHEDULE_ID", scheduleId);
        Vector vecOutputData =(Vector)webTxnBean.getResults(request, "getIacucScheduleInfo", hmData);
        if(vecOutputData != null && vecOutputData.size() > 0){
            //Commented/Added for case#3046 - Notify IRB attachments - start
            //DynaActionForm dynaForm = (DynaActionForm)vecOutputData.get(0);
            //unitNumber = (String)dynaForm.get("HOME_UNIT_NUMBER");
            ProtocolActionForm protocolActionForm = (ProtocolActionForm)vecOutputData.get(0);
            unitNumber = protocolActionForm.getUnitNumber();
            //Commented/Added for case#3046 - Notify IRB attachments - end
        }
        return unitNumber;       
    }      
    
    /**     
     * This method gets the submission details of last non notify irb action
     * @param request
     * @param protocolNumber
     * @return hmSubDetails
     * @throws Exception
     */
    private HashMap getNonNotifyIACUCCommitteeForProtocol(HttpServletRequest request, String protocolNumber) throws Exception {        
        
        int submissionStatusCode = 0;
        Vector submissionDetails = new Vector();
        HashMap hmSubDetails = new HashMap();
        HttpSession session = request.getSession();        
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());        
        ActionTransaction actionTxn = new ActionTransaction(userInfoBean.getUserId());        
        submissionDetails = actionTxn.getSubmissionDetails(protocolNumber);                
        if(submissionDetails != null && submissionDetails.size() > 0){            
            for(int index = submissionDetails.size() - 1; index >= 0; index--){
                Map hmSubmissionDetails = (HashMap)submissionDetails.get(index);
                submissionStatusCode = Integer.parseInt(hmSubmissionDetails.get("SUBMISSION_STATUS_CODE").toString());                
                if (submissionStatusCode == 103){
                    //Indicates a Notify IRB was the last action performed
                    //ignore it and continue.
                    continue;
                }else{
                    //Indicated a first non Notify IRB submission was found here
                    //Take its details                  
                    hmSubDetails.put(COMMITTEE_ID, hmSubmissionDetails.get("COMMITTEE_ID"));
                    hmSubDetails.put(SCHEDULE_ID, hmSubmissionDetails.get("SCHEDULE_ID"));
                    hmSubDetails.put(SUBMISSION_NUMBER, hmSubmissionDetails.get("SUBMISSION_NUMBER").toString());
                    hmSubDetails.put(PROTOCOL_NUMBER, hmSubmissionDetails.get("PROTOCOL_NUMBER"));
                    hmSubDetails.put(PROTO_ACTION_TYPE_CODE, hmSubmissionDetails.get("PROTOCOL_ACTION_TYPE_CODE"));
                    break;                  
                }
            }
        }        
        session.setAttribute("iacucSubmissionStatusCode",""+submissionStatusCode);
        return hmSubDetails;         
    }
    
    /**
     * This method formats the actionDate
     * @param actionDate
     * @return actionDate     
     */
    private String getFormattedDate(String strDate){
        DateUtils dtUtils = new DateUtils();                
        String tmp1 = dtUtils.formatDate(strDate, "/:-,","yyyy/MM/dd");
        String tmp2 = dtUtils.restoreDate(dtUtils.formatDate(tmp1, "dd-MMM-yyyy"), "/:-,");     
        return tmp2;
    }  
    
    
    /**
     * 
     * This method sets the approvalDate and expirationDate to the form field
     * It uses the procedure Get_Original_Approval_Date to get approval date
     * 
     * 
     * @param request HttpServletRequest
     * @param protocolActionForm ProtocolActionForm
     */
    private void setDates(HttpServletRequest request, ProtocolActionForm protocolActionForm) throws Exception{
        
        HashMap hmData = new HashMap();        
        WebTxnBean webTxnBean = new WebTxnBean();  
        HttpSession session = request.getSession(); 
        DateUtils dtUtils = new DateUtils();
        HashMap hmSubmissionDetails = (HashMap)session.getAttribute(IACUC_SUBMISSION_DETAILS);
        hmData.put(PROTOCOL_NUMBER, (String)hmSubmissionDetails.get(PROTOCOL_NUMBER));
        hmData.put(SUBMISSION_NUMBER, new Integer(hmSubmissionDetails.get(SUBMISSION_NUMBER).toString()));
        Hashtable htOutputData =(Hashtable)webTxnBean.getResults(request, GET_ORG_APPROVAL_DATE, hmData);
        Vector vecData = (Vector)htOutputData.get(GET_ORG_APPROVAL_DATE);        
        if(vecData != null && vecData.size() > 0){
            //Commented/Added for case#3046 - Notify IRB attachments
            //DynaActionForm dynaForm = (DynaActionForm)vecData.get(0);             
            //String approvalDate = (String)dynaForm.get(APPROVAL_DATE); 
            ProtocolActionForm protoActionForm = (ProtocolActionForm)vecData.get(0);
            String approvalDate = protoActionForm.getApprovalDate();
            if(approvalDate != null && !approvalDate.equals(EMPTY_STRING)){  
                //Commented/Added for case#3046 - Notify IRB attachments
                //protocolActionsForm.set(APPROVAL_DATE, dtUtils.formatDate(approvalDate, SIMPLE_DATE_FORMAT));                
                protocolActionForm.setApprovalDate(dtUtils.formatDate(approvalDate, SIMPLE_DATE_FORMAT));
                String expirationDate = getExpirationDate(approvalDate);    
                //Commented/Added for case#3046 - Notify IRB attachments
                //protocolActionsForm.set(EXPIRATION_DATE, expirationDate);
                protocolActionForm.setExpirationDate(expirationDate);
            }
        }
    } 
    
    /**
     * This method gets the expiration date based on approval date
     * ie 1 year ahead of approval date
     * @param approvalDate
     * @return expirationDate   
     * @throws Exception
     */
    private String getExpirationDate(String approvalDate) throws Exception{        
        DateUtils dtUtils = new DateUtils();
        SimpleDateFormat dtFormat = new java.text.SimpleDateFormat(SIMPLE_DATE_FORMAT);
        String expirationDate = dtUtils.formatDate(approvalDate, "dd/MM/yyyy");
        java.util.GregorianCalendar cal = new java.util.GregorianCalendar();
        cal.setTimeInMillis(dtFormat.parse(expirationDate).getTime());
        cal.roll(Calendar.YEAR, 1);
        cal.set(Calendar.DATE,cal.get(Calendar.DATE)-1);
        expirationDate = (cal.get(Calendar.MONTH)+1)+"/" +cal.get(Calendar.DATE)+"/"+(cal.get(Calendar.YEAR));                
        return expirationDate;
    }
    
    /**
     * This method compares the approval and expiration dates
     * if expiration date < approval date return true
     * @param approvalDate
     * @param expirationDate
     * @return dateFlag
     * @throws Exception
     */
    private boolean compareDates(String approvalDate, String expirationDate) throws Exception{
        
        boolean dateFlag = false;
        DateUtils dtUtils = new DateUtils();        
        String appDate = dtUtils.formatDate(approvalDate, DATE_SEPARATERS, SIMPLE_DATE_FORMAT);
        String expDate = dtUtils.formatDate(expirationDate, DATE_SEPARATERS, SIMPLE_DATE_FORMAT);
        java.sql.Date startDate = dtUtils.getSQLDate(appDate);
        java.sql.Date endDate = dtUtils.getSQLDate(expDate);
        if (endDate.before(startDate)) {
            dateFlag = true;            
        }   
        return dateFlag;
    }  
    
    /**
     * This method get protocol details, viz sequence number and protocol status code
     * for a given protocol
     * @param request
     * @param protocolNumber
     * @throws Exception
     * @return hmData
     */
    private HashMap getProtocolDetails(HttpServletRequest request, String protocolNumber) throws Exception{         
        
        HashMap hmData = new HashMap();        
        WebTxnBean webTxnBean = new WebTxnBean();           
        hmData.put(PROTOCOL_NUMBER, protocolNumber);
        Hashtable htOutputData =(Hashtable)webTxnBean.getResults(request, GET_PROTOCOL_DETAILS, hmData);    
        Vector vecProtocolDetails = (Vector)htOutputData.get(GET_PROTOCOL_DETAILS);
        if(vecProtocolDetails != null && vecProtocolDetails.size() > 0){
            DynaActionForm dynaForm = (DynaActionForm)vecProtocolDetails.get(0);
            hmData.clear();
            hmData.put(SEQUENCE_NUMBER, dynaForm.get(SEQUENCE_NUMBER));
            hmData.put(PROTOCOL_STATUS_CODE, dynaForm.get(PROTOCOL_STATUS_CODE));            
        }
        return hmData;
    }        
    //Added for response and expedited approval actions - end - 6
    
    public void cleanUp() {
    }    
   
    
    // Added for case # 2250 - Expire protocol - Start
    /**
     * This method checks whether 'Expire' protocol can be performed
     * @param request
     * @return navigator
     * @throws Exception
     */
    private String performExpireProtocol(HttpServletRequest request) throws Exception{
        String navigator = SUCCESS;        
        String responseCode = isAuthorizedToPerformExpireAction(request);
        throwMessage(request, responseCode);
        return navigator;
    }
    
    /**
     * This method checks whether user has rights
     * for  Expire protocols.
     * Calls the canPerformProtocolAction method which specifies 
     * whether action can be performed or not.
     * @param request     
     * @return responseCode
     * @throws Exception
     */     
    private String isAuthorizedToPerformExpireAction(HttpServletRequest request) throws Exception{
        
        String responseCode = "-1";
        HttpSession session = request.getSession();        
        session.removeAttribute(ERROR);
        String actionCode = request.getParameter(IACUC_ACTION_CODE);
        if(actionCode == null){
            actionCode = (String)session.getAttribute(IACUC_ACTION_CODE);
        }
        session.setAttribute(IACUC_ACTION_CODE, actionCode);
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());   
        HashMap hmSubmissionDetails = new HashMap();
        hmSubmissionDetails = (HashMap)session.getAttribute(IACUC_SUBMISSION_DETAILS);        
        String committeeId = (String)hmSubmissionDetails.get(COMMITTEE_ID);
        String scheduleId = (String)hmSubmissionDetails.get(SCHEDULE_ID);
        responseCode = canPerformProtocolAction(request, protocolNumber);            
        if (!responseCode.equals("1")){   
            // cannot perform this function on this protocol
            return responseCode ;
        }          
        if(!committeeId.equals(EMPTY_STRING)){
            responseCode = checkProtocolCommitteeForRight(request, ACTION_RIGHT, committeeId, protocolNumber, false);
            return responseCode ;    
            // if committeeid is not available then try using schedule
        }else if(!scheduleId.equals(EMPTY_STRING)){
            responseCode = checkProtocolScheduleForRight(request, ACTION_RIGHT, scheduleId, protocolNumber, false); 
            return responseCode;    
        }            
        return responseCode;
    }
    //Added for case#2250 - Expire protocol - End
    
    //Added for case#3046 - Notify IRB attachments - start
    /**
     * This method is used insert the attchment as blob data after
     * Notify IRB action is performed. 
     * @param request HttpServletRequest
     * @param protocolActionForm ProtocolActionForm
     * @throws Exception
     */    
    private void saveAttachments(HttpServletRequest request, ProtocolActionForm protocolActionForm) throws Exception {
        
        HttpSession session = request.getSession();
        HashMap hmSubmissionData = (HashMap)session.getAttribute(IACUC_SUBMISSION_DETAILS);
        String submissionNumber = (String)hmSubmissionData.get(SUBMISSION_NUMBER);
        if(submissionNumber == null || submissionNumber.equals(EMPTY_STRING)){
            hmSubmissionData.put(SUBMISSION_NUMBER, ONE);
            submissionNumber = (String)hmSubmissionData.get(SUBMISSION_NUMBER);
        }
        // COEUSDEV-327 / COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document -Start
        Vector vecAttachments = protocolActionForm.getVecAttachments();
        if(vecAttachments != null && !vecAttachments.isEmpty()){
            ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
            Timestamp dbTimestamp = prepareTimeStamp();
            int size = vecAttachments.size();
            for(int index = 0; index < size; index++){
                ProtocolActionDocumentBean protocolActionDocumentBean = (ProtocolActionDocumentBean) vecAttachments.get(index);
                
//                Timestamp dbTimestamp = prepareTimeStamp();
//                ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
//                ProtocolActionDocumentBean protocolActionDocumentBean = new ProtocolActionDocumentBean();
                protocolActionDocumentBean.setProtocolNumber(protocolActionForm.getProtocolNumber());
                protocolActionDocumentBean.setSequenceNumber(protocolActionForm.getSequenceNumber());
//                protocolActionDocumentBean.setSubmissionNumber(
//                        protocolSubmissionTxnBean.getMaxSubmissionNumber(protocolActionForm.getProtocolNumber()));
//                protocolActionDocumentBean.setFileName(protocolActionForm.getDocument().getFileName());
//                protocolActionDocumentBean.setFileBytes(protocolActionForm.getDocument().getFileData());
                protocolActionDocumentBean.setSubmissionNumber(Integer.parseInt(submissionNumber) +1);
		//Added with case 4007: icon based on mime type - Start
        	DocumentTypeChecker typeChecker = new DocumentTypeChecker();
        	protocolActionDocumentBean.setMimeType(typeChecker.getDocumentMimeType(protocolActionDocumentBean));
        	//4007 End
                protocolActionDocumentBean.setUpdateUser(protocolActionForm.getUpdateUser());
                protocolActionDocumentBean.setAcType(TypeConstants.INSERT_RECORD);
                protocolSubmissionTxnBean.addUpdProtocolActionDocument(protocolActionDocumentBean);
            }
        }
        // COEUSDEV-327 / COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document - End
    } 
    //Added for case#3046 - Notify IRB attachments - end
    
    //Added for case#3214 - Withdraw Submission - Start
    /**
     * This method checks whether withdraw submission can be performed
     * @param request HttpServletRequest
     * @return navigator String
     * @throws Exception
     */
    private String checkWithdrawSubmission(HttpServletRequest request) throws Exception{        
        String navigator = SUCCESS;        
        String responseCode = isAuthorizedToPerformAction(request);
        throwMessage(request, responseCode);
        return navigator;          
    } 
    //Added for case#3214 - Withdraw Submission - end
    // COEUSDEV-327 / COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document -Start
    private String addAttachment(HttpServletRequest request, ActionForm actionForm) throws FileNotFoundException, IOException, DBException, CoeusException, Exception {
        HttpSession session = request.getSession();
        String navigator = SUCCESS;
        ProtocolActionForm form = (ProtocolActionForm) actionForm;
        Vector vecAttachments = form.getVecAttachments();
        
        Map hmQualifiers = new HashMap();
        hmQualifiers.put("jobCode", "IACUC_COMM_SELECTION_DURING_SUBMISSION");        
        WebTxnBean webTxnBean = new WebTxnBean();
        hmQualifiers = (Hashtable)webTxnBean.getResults(request, "getDefaultJobCode", hmQualifiers);
        String value =(String) ((HashMap)hmQualifiers.get("getDefaultJobCode")).get("ls_value");
        session.setAttribute("IACUC_COMM_SELECTION_DURING_SUBMISSION", value);
        
        if(form.getDescription() == null || form.getDescription().trim().equals("")){
            ActionMessages actionMessages = new ActionMessages(); 
            actionMessages.add("protocolAction_exceptionCode.2036",new ActionMessage("protocolAction_exceptionCode.2036"));
            saveMessages(request, actionMessages);  
            session.setAttribute(ERROR, NO);
            session.setAttribute(SUCCESS_FLAG, EMPTY_STRING);
        } else if ((form.getDocument() == null || 
                form.getDocument().getFileName() == null || 
                form.getDocument().getFileName().equals("") ||
                form.getDocument().getFileSize() == 0 )&& 
                (form.getFileName() == null ||
                form.getFileName().trim().equals(""))){
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("protocolAction_exceptionCode.2037",new ActionMessage("protocolAction_exceptionCode.2037"));
            saveMessages(request, actionMessages);
            session.setAttribute(ERROR, NO);
            session.setAttribute(SUCCESS_FLAG, EMPTY_STRING);
        } else {
            
            if(vecAttachments == null){
                vecAttachments = new Vector();
            }
            if("U".equalsIgnoreCase(form.getAcType())){
                if(vecAttachments != null && !vecAttachments.isEmpty()){
                    ProtocolActionDocumentBean protocolActionDocumentBean;
                    int size = vecAttachments.size();
                    for(int index = 0; index < size; index++){
                        protocolActionDocumentBean = (ProtocolActionDocumentBean) vecAttachments.get(index);
                        if(form.getDocumentId() == protocolActionDocumentBean.getDocumentId()){
                            
                            protocolActionDocumentBean.setDescription(form.getDescription());
                            
                            if(form.getDocument().getFileData().length !=0){
                                protocolActionDocumentBean.setFileName(form.getDocument().getFileName());
                                protocolActionDocumentBean.setFileBytes(form.getDocument().getFileData());
                            }     
                            protocolActionDocumentBean.setUpdateTimestamp( new CoeusFunctions().getDBTimestamp());
                            break;
                        }
                    }
                }
            } else {
                
                UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
                String loggedinUser = userInfoBean.getUserId();
                String loggedInUserName = userInfoBean.getUserName();
                ProtocolActionDocumentBean protocolActionDocumentBean = new ProtocolActionDocumentBean();
                protocolActionDocumentBean.setFileName(form.getDocument().getFileName());
                protocolActionDocumentBean.setFileBytes(form.getDocument().getFileData());
                protocolActionDocumentBean.setDescription(form.getDescription());
                protocolActionDocumentBean.setUpdateTimestamp( new CoeusFunctions().getDBTimestamp());
                protocolActionDocumentBean.setUpdateUser(loggedinUser);
                protocolActionDocumentBean.setUpdateUserName(loggedInUserName);
                vecAttachments.add(protocolActionDocumentBean);
                form.setVecAttachments(vecAttachments);
            }
            resetAttachmentFields(actionForm);
            form.setAcType("");
            assignDocumentIds(actionForm);
        }
        return navigator;
    }
    /**
     * This method gets all the attachments in the passed ProtocolActionForm,
     * traverses throgh each attachment and assigns document id for each document.
     * The document id starts with 1 and increments by 1 for each attachment. 
     */
    private void assignDocumentIds(ActionForm actionForm) {
        ProtocolActionForm form = (ProtocolActionForm) actionForm;
        Vector vecAttachments = form.getVecAttachments();
        if(vecAttachments != null && !vecAttachments.isEmpty()){
            ProtocolActionDocumentBean protocolActionDocumentBean;
            int size = vecAttachments.size();
            for(int index = 0; index < size; index++){
                protocolActionDocumentBean = (ProtocolActionDocumentBean) vecAttachments.get(index);
                protocolActionDocumentBean.setDocumentId(index +1);
            }
        }
    }

    private String modifyAttachment(HttpServletRequest request, ActionForm actionForm) throws Exception {
        String navigator = SUCCESS;
        String strDocumentId = request.getParameter("docId");
        String mode = request.getParameter("mode");
        
        HttpSession session = request.getSession();
        Map hmQualifiers = new HashMap();
        hmQualifiers.put("jobCode", "IACUC_COMM_SELECTION_DURING_SUBMISSION");
        WebTxnBean webTxnBean = new WebTxnBean();
        hmQualifiers = (Hashtable)webTxnBean.getResults(request, "getDefaultJobCode", hmQualifiers);
        String value =(String) ((HashMap)hmQualifiers.get("getDefaultJobCode")).get("ls_value");
        session.setAttribute("IACUC_COMM_SELECTION_DURING_SUBMISSION", value);

        int documentId = 0;
        if(strDocumentId != null){
            documentId = Integer.parseInt(strDocumentId);
        }
        
        ProtocolActionForm form = (ProtocolActionForm) actionForm;
        Vector vecAttachments = form.getVecAttachments();
        if(vecAttachments != null && !vecAttachments.isEmpty()){
            ProtocolActionDocumentBean protocolActionDocumentBean;
            int size = vecAttachments.size();
            for(int index = size-1; index >= 0 ; index--){
                protocolActionDocumentBean = (ProtocolActionDocumentBean) vecAttachments.get(index);
                if(documentId == protocolActionDocumentBean.getDocumentId()){
                    
                    if("M".equalsIgnoreCase(mode)){
                        form.setFileName(protocolActionDocumentBean.getFileName());
                        form.setDescription(protocolActionDocumentBean.getDescription());
                        form.setDocumentId(protocolActionDocumentBean.getDocumentId());
                    } else if("D".equalsIgnoreCase(mode)){
                        vecAttachments.remove(index);
                        form.setAcType("");
                        assignDocumentIds(actionForm);
                    }
                    break;
                }
            }
        }
        
        
        return navigator;
    }

    private void resetAttachmentFields(ActionForm actionForm) {
        ProtocolActionForm form = (ProtocolActionForm) actionForm;
        form.setDescription("");
        form.setFileName("");
        form.setDocumentId(0);
        form.setAcType("");
    }

    private String viewAttachment(HttpServletRequest request, HttpServletResponse response, ActionForm actionForm) throws IOException {
        String navigator = SUCCESS;
        String strDoumentId = request.getParameter("docId");
        if(strDoumentId != null && !"".equals(strDoumentId)){
            int documentId = Integer.parseInt(strDoumentId);
           
            boolean present = false;
            ProtocolActionDocumentBean protocolActionDocumentBean = null;
            ProtocolActionForm form = (ProtocolActionForm) actionForm;
            Vector vecAttachments = form.getVecAttachments();
            if(vecAttachments != null && !vecAttachments.isEmpty()){
               int size = vecAttachments.size();
                for(int index = size-1; index >= 0 ; index--){
                    protocolActionDocumentBean = (ProtocolActionDocumentBean) vecAttachments.get(index);
                    if(documentId == protocolActionDocumentBean.getDocumentId()){
                        present = true;
                        break;
                    }
                }
            }
            
            if(present && protocolActionDocumentBean != null){
                UploadDocumentBean uploadDocumentBean = new UploadDocumentBean();
                
                DocumentBean documentBean = new DocumentBean();
                Map map = new HashMap();
                map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.iacuc.ProtocolDocumentReader");
                map.put("DOCUMENT_TYPE", "SUBMISSION_DOC");
                map.put("FILE_BYTES", protocolActionDocumentBean.getFileBytes());
                map.put("FILE_NAME", protocolActionDocumentBean.getFileName());
                documentBean.setParameterMap(map);
                String docId = DocumentIdGenerator.generateDocumentId();
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("/StreamingServlet");
                stringBuffer.append("?");
                stringBuffer.append(DocumentConstants.DOC_ID);
                stringBuffer.append("=");
                stringBuffer.append(docId);
                request.getSession().setAttribute(docId, documentBean);
                
                String templateURL= stringBuffer.toString();
                request.getSession().setAttribute("url", templateURL);
                response.sendRedirect(request.getContextPath()+templateURL);
                return null;
                
            }
        }
        return navigator;
    }
    // COEUSDEV-327 / COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document - End
    // COEUSDEV-86: Questionnaire for a Submission  -Start
    private String performTemporarySubmissionAction(HttpServletRequest request, ActionForm actionForm, ProtocolActionForm protocolActionForm) throws Exception {
        
        
        String navigator = SUCCESS;
        String responseCode = MINUS_ONE;
        boolean saveSuccess = true;
        
        HttpSession session = request.getSession();
        session.removeAttribute(APPROVAL_ACTION_ERROR_FLAG);
        String actionCode = request.getParameter(IACUC_ACTION_CODE);
        if(actionCode == null){
            actionCode = (String)session.getAttribute(IACUC_ACTION_CODE);
        }
        if(actionCode.equals(EXPEDITED_APPROVAL_ACTION_CODE) || actionCode.equals(RESPONSE_APPROVAL_ACTION_CODE)
        || actionCode.equals(EXPIRE_PROTOCOL_CODE)){// Modified for case#2250 - Expire Protocol
            responseCode = isAuthorizedToPerformApprovalAction(request);
            
            session.setAttribute(APPROVAL_ACTION_ERROR_FLAG, YES);
        }else{
            responseCode = isAuthorizedToPerformAction(request);
            
            session.setAttribute(APPROVAL_ACTION_ERROR_FLAG, NO);
        }
        if(responseCode.equals(ONE)){

            saveSuccess = saveTemporarySubmissionDetails(request, actionForm, protocolActionForm);
            if(saveSuccess){

//                throwMessage(request, S);
//
//                session.removeAttribute(SUCCESS_FLAG);
//                session.setAttribute(SUCCESS_FLAG, YES);
//                session.removeAttribute(APPROVAL_ACTION_ERROR_FLAG);
//                session.setAttribute(APPROVAL_ACTION_ERROR_FLAG, NO);
           // } else{

                session.removeAttribute(ERROR);
                session.setAttribute(ERROR, NO);
            }
        } else{
            throwMessage(request, responseCode);
        }

        return navigator;
        
    }

    private boolean saveTemporarySubmissionDetails(HttpServletRequest request, ActionForm actionForm, ProtocolActionForm protocolActionForm) throws Exception {
       
        
        HashMap hmSubmissionData = new HashMap();      
        HttpSession session = request.getSession();
        
        String committeeId = request.getParameter(COMMITTEE_ID);
        
        String actionCode = (String)session.getAttribute(IACUC_ACTION_CODE);
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()) ;
        String tempProtocolNumber = protocolNumber + "T";
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());   
       
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String loggedinUser = userInfoBean.getUserId();                           
        
        SimpleDateFormat dtFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);    
        
        boolean saveSuccess = true;
        ActionMessages actionMessages = new ActionMessages();
        
        //Get Submission Details        
        hmSubmissionData = (HashMap)session.getAttribute(IACUC_SUBMISSION_DETAILS);
        String submissionNumber = (String)hmSubmissionData.get(SUBMISSION_NUMBER);
        if(submissionNumber == null || submissionNumber.equals(EMPTY_STRING)){
            hmSubmissionData.put(SUBMISSION_NUMBER, ONE);
            submissionNumber = (String)hmSubmissionData.get(SUBMISSION_NUMBER);
        }

        //Set actionBean properties
        ProtocolActionsBean actionBean = new ProtocolActionsBean();
        actionBean.setProtocolNumber(tempProtocolNumber);
        actionBean.setSequenceNumber(Integer.parseInt(sequenceNumber));
        actionBean.setScheduleId((String)hmSubmissionData.get(SCHEDULE_ID));
        actionBean.setSubmissionNumber(Integer.parseInt(submissionNumber) +1);
        
        
        actionBean.setComments(protocolActionForm.getComments());
        actionBean.setActionDate(new java.sql.Date(dtFormat.parse(getFormattedDate(protocolActionForm.getActionDate())).getTime()));
        
        actionBean.setActionTypeCode(Integer.parseInt(actionCode));
        actionBean.setUpdateUser(loggedinUser);        
        
        if(committeeId != null && !committeeId.equals("")){
            actionBean.setCommitteeId(committeeId);
        }else{
            
            if(hmSubmissionData.get(COMMITTEE_ID) != null){
                actionBean.setCommitteeId(hmSubmissionData.get(COMMITTEE_ID).toString());
            }
        }
        
        if(actionCode != null && actionCode.equals(""+IacucProtocolActionsConstants.NOTIFY_IACUC)){
            actionBean.setSubmissionTypeCode(protocolActionForm.getNotificationType());
            actionBean.setReviwerTypeCode(FYI_REVIEW+"");
            actionBean.setCommitteeId(committeeId);
        }
        
        if(actionCode.equals(EXPEDITED_APPROVAL_ACTION_CODE) || actionCode.equals(RESPONSE_APPROVAL_ACTION_CODE)){            
        
            String approvalDate = protocolActionForm.getApprovalDate();
            String expirationDate = protocolActionForm.getExpirationDate();          
        
            if((approvalDate == null || approvalDate.equals(EMPTY_STRING)) && (expirationDate != null && !expirationDate.equals(EMPTY_STRING))){
                saveSuccess = false;
                actionMessages.add("protocolAction_exceptionCode.date1",new ActionMessage("protocolAction_exceptionCode.date1"));
                saveMessages(request, actionMessages);                
            }
            if((expirationDate == null || expirationDate.equals(EMPTY_STRING)) &&  (approvalDate != null && !approvalDate.equals(EMPTY_STRING))){
                saveSuccess = false;
                actionMessages.add("protocolAction_exceptionCode.date2",new ActionMessage("protocolAction_exceptionCode.date2"));
                saveMessages(request, actionMessages); 
            }
            if((approvalDate == null || approvalDate.equals(EMPTY_STRING)) && (expirationDate == null || expirationDate.equals(EMPTY_STRING))){
                saveSuccess = false;
                actionMessages.add("protocolAction_exceptionCode.date3",new ActionMessage("protocolAction_exceptionCode.date3"));
                saveMessages(request, actionMessages); 
            }  
            if((approvalDate != null && !approvalDate.equals(EMPTY_STRING)) && (expirationDate != null && !expirationDate.equals(EMPTY_STRING))){
                if(compareDates(approvalDate, expirationDate)){
                    saveSuccess = false;
                    actionMessages.add("protocolAction_exceptionCode.date4",new ActionMessage("protocolAction_exceptionCode.date4"));
                    saveMessages(request, actionMessages);                     
                }                
            }
            if(saveSuccess){
                
                actionBean.setApprovalDate(new java.sql.Date(dtFormat.parse(getFormattedDate(protocolActionForm.getApprovalDate())).getTime()));
                actionBean.setExpirationDate(new java.sql.Date(dtFormat.parse(getFormattedDate(protocolActionForm.getExpirationDate())).getTime()));                                            
                
                HashMap hmSubmissionDetails = (HashMap)session.getAttribute(IACUC_SUBMISSION_DETAILS);
                actionBean.setCommitteeId((String)hmSubmissionDetails.get(COMMITTEE_ID));                
            }
        }
       
        
        if(saveSuccess){
       
            ActionTransaction actionTransaction = new ActionTransaction(Integer.parseInt(actionCode));
//            int actionId = actionTransaction.performAction(actionBean, loggedinUser);
//            actionBean.setActionId(actionId);
//            String maxSeqNum = getMaxSequenceNumber(request,protocolNumber,sequenceNumber,actionCode);            
//            actionBean.setSequenceNumber(Integer.parseInt(maxSeqNum));
//            
//       
//            if(protocolNumber != null && protocolNumber.length() > 10){
//                session.removeAttribute("protoAmendRenewNumber");
//                session.setAttribute("protoAmendRenewNumber", protocolNumber);            
//            }else{
//                session.removeAttribute("protoAmendRenewNumber");
//            }
//            
//       
//            if(actionCode.equals(EXPEDITED_APPROVAL_ACTION_CODE) || actionCode.equals(RESPONSE_APPROVAL_ACTION_CODE) || actionCode.equals(EXPIRE_PROTOCOL_CODE)){//Modified for Case#2250 - Expire Protocol
//                if(protocolNumber != null && protocolNumber.length() > 10){
//                    protocolNumber = protocolNumber.substring(0,10);   
//                    session.removeAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER);
//                    session.setAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId(), protocolNumber);                    
//                }    
//       
//                HashMap hmProtocolDetails = getProtocolDetails(request, protocolNumber);            
//                session.removeAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER);
//                session.setAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId(), hmProtocolDetails.get(IACUC_SEQUENCE_NUMBER).toString()); 
//            }                
       
            ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
            ProtocolSubmissionUpdateTxnBean protocolSubmissionUpdateTxnBean = new ProtocolSubmissionUpdateTxnBean(loggedinUser);
            
            ProtocolSubmissionInfoBean protocolSubmissionInfoBean = new ProtocolSubmissionInfoBean();
            protocolSubmissionInfoBean.setProtocolNumber(tempProtocolNumber);
            protocolSubmissionInfoBean.setSequenceNumber(Integer.parseInt(sequenceNumber));
            protocolSubmissionInfoBean.setScheduleId((String)hmSubmissionData.get(SCHEDULE_ID));
            protocolSubmissionInfoBean.setComments(protocolActionForm.getComments());
            
            protocolSubmissionInfoBean.setSubmissionTypeCode(protocolSubmissionTxnBean.getSubmissionTypeCodeForAction(Integer.parseInt(actionCode)));
            if(protocolActionForm.getNotificationType() != null && !"".equals(protocolActionForm.getNotificationType())){
                protocolSubmissionInfoBean.setSubmissionQualTypeCode(Integer.parseInt(protocolActionForm.getNotificationType()));
            }
            protocolSubmissionInfoBean.setCommitteeId(committeeId);
            protocolSubmissionInfoBean.setSubmissionStatusCode(105);
            protocolSubmissionInfoBean.setSubmissionNumber(Integer.parseInt(submissionNumber) +1);
            
            protocolSubmissionInfoBean.setUpdateTimestamp(protocolActionForm.getUpdateTimestamp());
            protocolSubmissionInfoBean.setAWSequenceNumber(Integer.parseInt(sequenceNumber));
                
            String tempSubmissionSaved = (String) session.getAttribute("iacucTempSubmissionSaved");
            if(CoeusLiteConstants.YES.equalsIgnoreCase(tempSubmissionSaved)){
                
                HashMap hmSubmissionDetails = (HashMap)session.getAttribute(IACUC_SUBMISSION_DETAILS);
                String savedSubmissionNumber = (String)hmSubmissionDetails.get(SUBMISSION_NUMBER);
                protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
                ProtocolSubmissionInfoBean savedProtocolSubmissionInfoBean = protocolSubmissionTxnBean.getSubmissionForSubmissionNumber(protocolNumber + "T", new Integer(savedSubmissionNumber) +1);
                if(protocolSubmissionInfoBean != null){
                    protocolSubmissionInfoBean.setUpdateTimestamp(savedProtocolSubmissionInfoBean.getUpdateTimestamp());
                }
                
                protocolSubmissionInfoBean.setAcType("U");
                actionBean.setAcType("U");
            } else {
                protocolSubmissionInfoBean.setAcType("I");
                actionBean.setAcType("I");
                request.setAttribute("newSubmission", CoeusLiteConstants.YES);
            }
           // protocolSubmissionInfoBean.setProtocolReviewTypeCode(112);
            if(actionCode != null && actionCode.equals(""+IacucProtocolActionsConstants.NOTIFY_IACUC)){
                protocolSubmissionInfoBean.setProtocolReviewTypeCode(FYI_REVIEW);
            } else {
                protocolSubmissionInfoBean.setProtocolReviewTypeCode(1);
            }
            protocolSubmissionInfoBean.setUpdateUser(loggedinUser);
        
            protocolSubmissionUpdateTxnBean.updateProtocolSubmission(protocolSubmissionInfoBean);
        
        
        
//            if(actionCode != null && actionCode.equals(NOTIFY_IRB_CODE)){
//                protocolSubmissionInfoBean.setSubmissionTypeCode(protocolActionForm.getNotificationType());
//                protocolSubmissionInfoBean.setProtocolReviewTypeCode("7");
//                
//            }
            
          //  if(!CoeusLiteConstants.YES.equalsIgnoreCase(tempSubmissionSaved)){
                protocolSubmissionUpdateTxnBean.updateProtocolAction(actionBean);
       //     }
                    
            
            
            
            
            if((""+IacucProtocolActionsConstants.NOTIFY_IACUC).equals(actionCode)
            || (""+IacucProtocolActionsConstants.REQUEST_TO_DEACTIVATE).equals(actionCode)
            || (""+IacucProtocolActionsConstants.REQUEST_TO_LIFT_HOLD).equals(actionCode) ){
       
//                protocolActionForm.setProtocolNumber(protocolNumber);
//                protocolActionForm.setSequenceNumber(Integer.parseInt(sequenceNumber));
//                protocolActionForm.setUpdateUser(loggedinUser);
//                saveAttachments(request, protocolActionForm);
                
                ProtocolActionForm formWithAttachment = new ProtocolActionForm();
                formWithAttachment.setProtocolNumber(protocolNumber);
                formWithAttachment.setSequenceNumber(Integer.parseInt(sequenceNumber));
                formWithAttachment.setUpdateUser(loggedinUser);
                if(protocolActionForm.getVecAttachments() != null){
                    formWithAttachment.setVecAttachments((Vector) ObjectCloner.deepCopy(protocolActionForm.getVecAttachments()));
                }
                session.setAttribute("formWithAttchmentsForSubmission", formWithAttachment);
            }
            
//            generateCorrespondence(request, actionBean);
        }
        
        if(saveSuccess){
            session.setAttribute("iacucTempSubmissionSaved", CoeusLiteConstants.YES);
     //       session.setAttribute("tempSubmissionActionForm", actionForm);
            if((""+IacucProtocolActionsConstants.NOTIFY_IACUC).equals(actionCode)){
                String savedNotifType = protocolActionForm.getNotificationType();
                request.setAttribute("savedNotificationType", savedNotifType);
            }
        }
        
        return saveSuccess;
        
    }

    private void setUnfilledQuestionnaireData(Vector vecUnfilledQnr, HttpServletRequest request) {
        boolean requiredQnrFilled = true;
        HttpSession session = request.getSession();
        String actionCode = (String) session.getAttribute(IACUC_ACTION_CODE);
        String actionName;
        if((""+IacucProtocolActionsConstants.NOTIFY_IACUC).equals(actionCode)){
           actionName = NOTIFY_IACUC; 
        } else if ((""+IacucProtocolActionsConstants.REQUEST_TO_DEACTIVATE).equals(actionCode)){
           actionName = REQUEST_TO_DEACTIVATE; 
        } else  if((""+IacucProtocolActionsConstants.REQUEST_TO_LIFT_HOLD).equals(actionCode)){
            actionName = REQUEST_TO_LIFT_HOLD;
        } else {
            actionName = "the action";
        }
        
        
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
                String validationMessage = "Complete the following mandatory forms before performing "+actionName+":";
                
                StringTokenizer stokenMessage = new StringTokenizer(mandatoryQnr,"~");
                while (stokenMessage.hasMoreTokens()){
                    String message = stokenMessage.nextToken();
                    validationMessage = validationMessage + "<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + message;
                }
                request.setAttribute("mandatoryQnrMessage", validationMessage);
            }
            
            if(incompleteQnrPresent){
                
                String validationMessage = "Complete the following forms before performing "+actionName+".";
                
                StringTokenizer stokenMessage = new StringTokenizer(incompleteQnr,"~");
                while (stokenMessage.hasMoreTokens()){
                    String message = stokenMessage.nextToken();
                    validationMessage = validationMessage + "<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + message;
                }
                request.setAttribute("incompleteQnrMessage", validationMessage);
            }
            
            if(newVersionQnrPresent){
                String validationMessage = "Fill the latest version of the following forms before performing "+actionName+"." ;
                
                StringTokenizer stokenMessage = new StringTokenizer(newVersionQnr,"~");
                while (stokenMessage.hasMoreTokens()){
                    String message = stokenMessage.nextToken();
                    validationMessage = validationMessage + "<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + message;
                }
                request.setAttribute("newVersionQnrMessage", validationMessage);
            }
        }
    }
    
    protected LockBean getLockingBean(UserInfoBean userInfoBean,
            String protocolNumber, HttpServletRequest request, String value) throws Exception{
        LockBean lockBean = new LockBean();
        lockBean.setLockId(CoeusLiteConstants.IACUC_PROTO_LOCK_STR+protocolNumber);
        String mode = (String)request.getSession().getAttribute(CoeusLiteConstants.MODE_DETAILS+request.getSession().getId());
        mode = getMode(mode , null);
        lockBean.setMode(mode);
        lockBean.setModuleKey(CoeusLiteConstants.PROTOCOL_MODULE);
        lockBean.setModuleNumber(protocolNumber);
        lockBean.setModuleUnitNumber(userInfoBean.getUnitNumber());
        lockBean.setUnitNumber(UNIT_NUMBER);
        lockBean.setUserId(userInfoBean.getUserId());
        lockBean.setUserName(userInfoBean.getUserName());
        lockBean.setSessionId(request.getSession().getId());
        return lockBean;
    }
    
    
    protected String getMode(String mode, Object obj) throws Exception{
        if(mode!= null && !mode.equals(EMPTY_STRING)){
            if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
                mode = CoeusLiteConstants.DISPLAY_MODE;
            }
        }else{
            mode = CoeusLiteConstants.MODIFY_MODE;
        }
        return mode;
    }
    // COEUSDEV-86: Questionnaire for a Submission - End
    
    private void setSavedTempSubmissionDataToForm(ProtocolActionForm protocolActionForm, HttpServletRequest request) throws CoeusException, DBException{
        
        HttpSession session = request.getSession();
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()) ;    
        HashMap hmSubmissionDetails = (HashMap)session.getAttribute(IACUC_SUBMISSION_DETAILS);
        String submissionNumber = (String)hmSubmissionDetails.get(SUBMISSION_NUMBER);
        ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
        ProtocolSubmissionInfoBean protocolSubmissionInfoBean = protocolSubmissionTxnBean.getSubmissionForSubmissionNumber(protocolNumber + "T", new Integer(submissionNumber) +1);
        if(protocolSubmissionInfoBean != null){
            protocolActionForm.setNotificationType(String.valueOf(protocolSubmissionInfoBean.getSubmissionQualTypeCode()));
            protocolActionForm.setCommitteeId(protocolSubmissionInfoBean.getCommitteeId());
            protocolActionForm.setComments(protocolSubmissionInfoBean.getComments());
            
            String strActionDate = "";
            if(protocolSubmissionInfoBean.getActionDate() != null){
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                strActionDate = dateFormat.format(protocolSubmissionInfoBean.getActionDate());
            }
            
            protocolActionForm.setActionDate(strActionDate);
            protocolActionForm.setUpdateTimestamp(protocolSubmissionInfoBean.getUpdateTimestamp());
            
            request.setAttribute("savedNotificationType",String.valueOf(protocolSubmissionInfoBean.getSubmissionQualTypeCode()));
        }
    }
    
    private boolean checkSubmissionQuestionnaireCompleted(HttpServletRequest request) throws DBException, CoeusException {
        HttpSession session = request.getSession();
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());  
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String loggedinUser = userInfoBean.getUserId();
        HashMap hmSubmissionData = (HashMap)session.getAttribute(IACUC_SUBMISSION_DETAILS);
        String submissionNumber = (String)hmSubmissionData.get(SUBMISSION_NUMBER);
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        boolean submissionQnrCompleted = false;
        String tempSubmissionCompleted = (String) session.getAttribute("iacucTempSubmissionSaved");
        if(CoeusLiteConstants.YES.equalsIgnoreCase(tempSubmissionCompleted)){
            
            QuestionnaireTxnBean questionnaireTxnBean = new QuestionnaireTxnBean(loggedinUser);
            Vector vecUnfilledQnr = 
                    questionnaireTxnBean.fetchQuestionnaireCompletedForModule(
                    protocolNumber+"T", ModuleConstants.IACUC_MODULE_CODE, "2", Integer.parseInt(submissionNumber) +1);
            
            
            if(vecUnfilledQnr != null && vecUnfilledQnr.size() > 2 && vecUnfilledQnr.elementAt(0) == null
                    && vecUnfilledQnr.elementAt(1) == null
                    && vecUnfilledQnr.elementAt(2) == null){
                submissionQnrCompleted = true;
            }
            
            if(submissionQnrCompleted){
                request.setAttribute("submissionQuestionnaireCompleted",CoeusLiteConstants.YES);
            } else {
                request.setAttribute("submissionQuestionnaireCompleted",CoeusLiteConstants.NO);
                setUnfilledQuestionnaireData(vecUnfilledQnr, request);
            }
        } else {
            throwMessage(request,"2015");
        }
        return submissionQnrCompleted;
    }
  
    // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
    /**
     * Perform Abandon action on the chosen protocol
     * @param HttpServletRequest object
     * @return navigator
     * @exception throws Exception
     */
    private String performAbandonProtocol(HttpServletRequest request) throws Exception{        
        String navigator = SUCCESS;        
        String responseCode = isAuthorizedToPerformAction(request);
        throwMessage(request, responseCode);
        return navigator;          
    }
    // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
      
}
