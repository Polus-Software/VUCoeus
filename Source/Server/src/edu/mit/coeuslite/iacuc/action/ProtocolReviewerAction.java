/*
 * ProtocolReviewerAction.java
 *
 * Created on March 7, 2009, 10:44 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

/* PMD check performed, and commented unused imports and variables on 16-SEPT-2010
 * by Satheesh Kumar K N
 */

package edu.mit.coeuslite.iacuc.action;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.iacuc.bean.MinuteEntryInfoBean;
import edu.mit.coeus.iacuc.bean.ProtocolAuthorizationBean;
import edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean;
import edu.mit.coeus.iacuc.bean.ProtocolInfoBean;
import edu.mit.coeus.iacuc.bean.ProtocolKeyPersonnelBean;
import edu.mit.coeus.iacuc.bean.ProtocolReviewDeterminationBean;
import edu.mit.coeus.iacuc.bean.ProtocolReviewerInfoBean;
import edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean;
import edu.mit.coeus.iacuc.bean.ProtocolSubmissionTxnBean;
import edu.mit.coeus.iacuc.bean.ProtocolSubmissionUpdateTxnBean;
import edu.mit.coeus.iacuc.bean.ReviewAttachmentsBean;
import edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean;
import edu.mit.coeus.iacuc.bean.ScheduleMaintenanceUpdateTxnBean;
import edu.mit.coeus.iacuc.bean.SubmissionDetailsTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeus.utils.documenttype.DocumentTypeChecker;
import edu.mit.coeuslite.iacuc.bean.ProtocolHeaderDetailsBean;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author Sreenath
 */

public class ProtocolReviewerAction extends ProtocolBaseAction{
    
    private static final String INSERT_MODE = "I";
    private static final String GET_REVIEW_COMMENTS = "/getIacucReviewComments";
    private static final String SAVE_REVIEW_COMMENTS = "/saveIacucReviewComments";
    private static final String GET_CONTINGENCY_DESC = "/getIacucContingencyDes";
// Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
    private static final String VIEW_ATTACHMENT = "/viewIacucReviewAttachment";    
// Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end    
    private static final String PROTO_CONTINGENCY_CODE = "protoContingencyCode";
    private static final String FINAL_FLAG = "finalFlag";
    private static final String PRIVATE_COMMENT_FLAG = "privateCommentFlag";
    private static final String MINUTE_ENTRY = "minuteEntry";
    //Added for COEUSDEV-237 : Investigator cannot see review comments - Start
    private static final String MODIFY_COMMENT = "modifyComment";
    private static final String VIEW_COMMENT = "viewComment";
    private static final String CAN_USER_CREATE_REVIEW_COMMNENTS = "CAN_USER_CREATE_REVIEW_COMMNENTS";
//    private static final String CAN_VIEW_PAST_SUBMISSION_COMMENTS = "CAN_VIEW_PAST_SUBMISSION_COMMENTS";
    private static final String DELETE_MODE = "D";
    private static final String USER_PRIVILEGE_IN_REVIEW_COMMENTS = "userRightInReviewComment";
    //COEUSDEV-237 : End
    //COEUSQA-2291 : Start
    private static final String CAN_USER_VIEW_REVIEWER = "canUserViewReviewer";
    //COEUSQA-2291 : End
    //Added for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start
    private static final String CAN_COMPLETE_REVIEW = "canCompleteReview";
    private static final String IS_ALL_REIVEW_COMMENTS_MARKED_FINAL = "isAllReivewCommentsFinal";
    private static final String RECOMMENDED_ACTIONS = "recommendedActionList";
    private static final String CAN_VIEW_UPDATE_DETALS = "canViewUpdateDetails";
    private static final String REVIEW_COMPLETE = "/iacucReviewComplete";
    private static final int SUBMISSION_STATUS_ROUTING_IN_PROGRESS = 100;
    private static final int SUBMISSION_STATUS_SUBMITTED_TO_COMMITTEE = 102;
    private static final int SUBMISSION_STATUS_IN_AGENDA = 103;
    private static final int SUBMISSION_STATUS_PENDING = 101;
    private static final int PROTOCOL_STATUS_PENDING_IN_PROGRESS = 100;
    private static final int PROTOCOL_STATUS_AMENDMENT_IN_PROGRESS = 0;
   //todo: find the status code from iacuc (amendment in progress) and replace hardcoded value 105
    private static final int PROTOCOL_STATUS_RENEWAAL_IN_PROGRESS = 106;
    private static final String REQUEST_SHOW = "show";
    private static final String REQUEST_SET_MODIFY_FLAG = "setModifyFlag";
    private static final String REQUEST_NO_IACUC_REVIEW_COMM_MESSAGE ="noIacucReviewCommentMessage";
    private static final String ACTION_TYPE = "acType";
    private static final String PERSON_ID = "personId";
    private static final String UPDATE_REVIEW_COMMENTES_LITE = "updIacucReviewCommentsFromLite";
    private static final String STRING_VALUE_TRUE = "true";
    private static final String STRING_VALUE_FALSE = "false";
    private static final String IS_IACUC_ADMIN = "isUserIACUCAdmin";
    private static final String IACUC_REVIEW_COMMENTS = "iacucReviewComments";
    private static final String IACUC_ADMIN_REVIEW_COMMENTS = "iacucAdminReviewComments";
    private static final String IACUC_PROTOCOL_SUBMISSION_INFO_BEAN = "iacucProtocolSubmissionInfoBean";
    private static final String SESSION_SUBMISSION_NUMBER = "IACUC_SUBMISSION_NUMBER";
    
    //COEUSQA-2288 : End
    // Modified for COEUSQA-2764 Comments by other reviewer are not showing until the user (viewing reviewer) marks their review complete - Start
    private static final String MY_REVIEW_COMMENTS = "myIacucReviewComments";
    private static final String OTHER_REVIEWER_REVIEW_COMMENTS = "otherIacucReviewerReviewComments";
    private static final String REVIEW_COMPLETE_PROPERTY = "reviewComplete";
// Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
    private static final String IACUC_ADMIN_REVIEW_ATTACHMENTS = "iacucAdminReviewAttachments";
    private static final String ACTION_FROM_COMMENTS = "C";
    private static final String ACTION_FROM_ATTACHMENTS = "A";
    private static final String MY_REVIEW_ATTACHMENTS = "myIacucReviewAttachments";
    private static final String OTHER_REVIEWER_REVIEW_ATTACHMENTS = "otherIacucReviewerReviewAttachments";
    private static final String PRIVATE_ATTACHMENT_FLAG = "privateAttachmentFlag";
    private static final String ATTACHMENT_FINAL_FLAG = "attachmentFinalFlag";
    private static final String ACTION_PERFORMED_FROM = "actionPerformedFrom";    
    private static final String ATTACHMENT_ID = "attachmentId";
    private static final String SUBMISSION_NUMBER = "submissionNumber";
    private static final String SEQUENCE_NUMBER = "sequenceNumber";
    private static final String PROTOCOL_NUMBER = "protocolNumber";
    String actionFrom = EMPTY_STRING;
    private static final String REQUEST_NO_IACUC_REVIEW_ATTACH_MESSAGE ="noIacucReviewAttachmentMessage";
    // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end    // Modified for COEUSQA-2764 Comments by other reviewer are not showing until the user (viewing reviewer) marks their review complete - End
    // Added for COEUSQA-3012:notification for when a reviewer completes their review IRB - start
    private static final int REVIEWER_REVIEW_COMPLETE_NOTIFICATION_CODE = 405;
    // Added for COEUSQA-3012:notification for when a reviewer completes their review IRB - End
    
    // Added for COEUSQA-3025 : Enhanced functionality to address the IACUC designated member review determination process - Start
    private static final String REVIEW_TYPES = "reviewTypeList";
    private final String DATE_FORMAT = "dd-MMM-yyyy";
    // Added for COEUSQA-3025 : Enhanced functionality to address the IACUC designated member review determination process - End
    
    /** Creates a new instance of ProtocolReviewerAction */
    public ProtocolReviewerAction() {
        
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ActionForward actionForward = null;
        // Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
//        actionForward = fetchProtocolReviewDetails(actionMapping, form, request);
        actionForward = fetchProtocolReviewDetails(actionMapping, form, request,response);
        // Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
        readSavedStatus(request);
        return actionForward;
    }
    
    public void cleanUp() {
    }
    
    // Commented and Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
//    private ActionForward fetchProtocolReviewDetails( ActionMapping actionMapping,
//            ActionForm form, HttpServletRequest request) throws CoeusException, DBException, IOException, Exception {
    private ActionForward fetchProtocolReviewDetails( ActionMapping actionMapping,
            ActionForm form, HttpServletRequest request,HttpServletResponse response) throws CoeusException, DBException, IOException, Exception {
      // Commented and Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
        Vector vecPastSubmission = new Vector();
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        ProtocolAuthorizationBean protocolAuthorizationBean = new ProtocolAuthorizationBean();
        ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean;
        // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
        ScheduleMaintenanceUpdateTxnBean scheduleUpdateTxnBean;
        ReviewAttachmentsBean reviewAttachmentBean;
        // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
        MinuteEntryInfoBean minuteEntryInfoBean;
        
       //Added for COEUSDEV-236 : PI can't see protocol he/she created - Start
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());      
        //Gets the person details from get_person_for_user with userId as parameter
        UserDetailsBean  userDetailsBean = new UserDetailsBean();
        PersonInfoBean personInfoBean  = userDetailsBean.getPersonInfo(userInfoBean.getUserId());
        String personId = personInfoBean.getPersonID();
        request.setAttribute("userPersonId",personId);
        //COEUSDEV-236 : END
        
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        Integer intSubNumber  = (Integer)session.getAttribute(SESSION_SUBMISSION_NUMBER+session.getId());
        //Added for case#3282/ 3284 - Reviewer Views and Comments: -start
        ActionMessages messages = new ActionMessages();
//        int submissionNumber = intSubNumber.intValue();
        if(!actionMapping.getPath().equals(REVIEW_COMPLETE) && !actionMapping.getPath().equals(VIEW_ATTACHMENT)){
            // Modified for COEUSQA-2764 Comments by other reviewer are not showing until the user (viewing reviewer) marks their review complete - Start
            //          session.removeAttribute(IACUC_REVIEW_COMMENTS);
            session.removeAttribute(MY_REVIEW_COMMENTS);
            session.removeAttribute(OTHER_REVIEWER_REVIEW_COMMENTS );
            // Modified for COEUSQA-2764 Comments by other reviewer are not showing until the user (viewing reviewer) marks their review complete - End
            // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
            session.removeAttribute(MY_REVIEW_ATTACHMENTS);
            session.removeAttribute(OTHER_REVIEWER_REVIEW_ATTACHMENTS );
            // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
        }
        
        int submissionNumber=0;
        if(intSubNumber!=null){
             submissionNumber = intSubNumber.intValue();
        }
        // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - Start
//        //Checking for user has IRB Admin Role
//        boolean userHasIRBAdminRole = checkUserHasAdminRole(request);
//        if(userHasIRBAdminRole){
//            session.setAttribute("userHasIRBAdminRole","Y");
//        }else{
//            session.setAttribute("userHasIRBAdminRole","N");
//        }
        // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - End
        request.setAttribute(REQUEST_SHOW, REQUEST_SHOW); 
        //Added for When User clicks Modify link, should load the comments to top section Comments window.
        if(request.getParameter(REQUEST_SET_MODIFY_FLAG)!=null && request.getParameter(REQUEST_SET_MODIFY_FLAG).equalsIgnoreCase(REQUEST_SHOW)){
            request.setAttribute(REQUEST_SET_MODIFY_FLAG,REQUEST_SHOW);
            //Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
            DynaValidatorForm reviewForm = (DynaValidatorForm) form;
            if(reviewForm.get(ACTION_PERFORMED_FROM) != null){
                actionFrom = (String) reviewForm.get(ACTION_PERFORMED_FROM);
            }
            if(ACTION_FROM_COMMENTS.equalsIgnoreCase(actionFrom)){
                request.setAttribute("showComment",ACTION_FROM_COMMENTS);
            }else if(ACTION_FROM_ATTACHMENTS.equalsIgnoreCase(actionFrom)){
                request.setAttribute("showComment",ACTION_FROM_ATTACHMENTS);
            }else{
                request.removeAttribute("showComment");
            }
            // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
        }else{
            request.removeAttribute(REQUEST_SET_MODIFY_FLAG);
        }
        //Added for case#3282/ 3284 - Reviewer Views and Comments: -end
        
        if(GET_REVIEW_COMMENTS.equals(actionMapping.getPath())){
            
            ProtocolSubmissionInfoBean protocolSubmissionInfoBean = new ProtocolSubmissionInfoBean();
            ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
            if(session.getAttribute(REQUEST_NO_IACUC_REVIEW_COMM_MESSAGE)!=null){                
                messages.add( "error", new ActionMessage((String) session.getAttribute(REQUEST_NO_IACUC_REVIEW_COMM_MESSAGE)));
                saveErrors( request, messages );                
                session.removeAttribute(REQUEST_NO_IACUC_REVIEW_COMM_MESSAGE);
            }
            // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
            if(session.getAttribute(REQUEST_NO_IACUC_REVIEW_ATTACH_MESSAGE)!=null){                
                messages.add( "error", new ActionMessage((String) session.getAttribute(REQUEST_NO_IACUC_REVIEW_ATTACH_MESSAGE)));
                saveErrors( request, messages );                
                session.removeAttribute(REQUEST_NO_IACUC_REVIEW_ATTACH_MESSAGE);
            }
            // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
            //Added for the case#3282-start
//            ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
            ProtocolInfoBean protocolInfoBean = new ProtocolInfoBean();
            protocolInfoBean = protocolDataTxnBean.getProtocolMaintenanceDetails(protocolNumber);
            //Commented for COEUSDEV-237 : Investigator cannot see review comments  - Start
//            if(intSubNumber==null){
            //COEUSDEV-237 : End
            if(!(protocolInfoBean.getProtocolStatusCode()== PROTOCOL_STATUS_PENDING_IN_PROGRESS)){
                if(protocolNumber!=null){
                    protocolSubmissionInfoBean=protocolSubmissionTxnBean.getProtocolSubmissionDetails(protocolNumber);
                    if(protocolSubmissionInfoBean!=null){
                        submissionNumber=protocolSubmissionInfoBean.getSubmissionNumber();
                        session.setAttribute(SESSION_SUBMISSION_NUMBER+session.getId(),new Integer(submissionNumber));
                    }
                }
            }
            //Commented for COEUSDEV-237 : Investigator cannot see review comments  - Start
//            }else{
//               protocolSubmissionInfoBean = protocolSubmissionTxnBean.getSubmissionForSubmissionNumber(protocolNumber, submissionNumber);
//            }
            //COEUSDEV-2337 : End
            
            //Added for the case#3282-end
            //protocolSubmissionInfoBean = protocolSubmissionTxnBean.getSubmissionForSubmissionNumber(protocolNumber, submissionNumber);
            if(protocolSubmissionInfoBean != null){
                session.setAttribute(IACUC_PROTOCOL_SUBMISSION_INFO_BEAN, protocolSubmissionInfoBean);
             // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - Start   
//            }else{
//                messages.add( "protocolReviewComments.noSubmissionDetials", new ActionMessage("protocolReviewComments.noSubmissionDetials"));
//                saveErrors( request, messages );   
             // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - End   
            }else{
                 session.removeAttribute(IACUC_PROTOCOL_SUBMISSION_INFO_BEAN);
            }
            
            String showContingencCode =(String) request.getParameter("showCodes");
            if("Y".equalsIgnoreCase(showContingencCode)){
                request.setAttribute("showContingencCode", "Y");
            }
            String inEditMode = (String) request.getParameter("editable");
            if("N".equalsIgnoreCase(inEditMode)){
                request.setAttribute("inViewMode", "Y");
            }
            
            String getMinutes =(String) request.getParameter("getMinutes");
            if("Y".equalsIgnoreCase(getMinutes)){
                minuteEntryInfoBean = new MinuteEntryInfoBean();
                DynaValidatorForm reviewForm = (DynaValidatorForm) form;
                String scheduleId =  (String) reviewForm.get("scheduleId");
                Integer entryNumber = (Integer) reviewForm.get("entryNumber");
                scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean();
                minuteEntryInfoBean = scheduleMaintenanceTxnBean.getMinutesInfo(scheduleId, entryNumber.intValue());
                if(minuteEntryInfoBean != null){
                    
                    String minuteEntry = minuteEntryInfoBean.getMinuteEntry();
                    String protoContingencyCode =  minuteEntryInfoBean.getProtocolContingencyCode();
                    String privateCommentFlag =  minuteEntryInfoBean.isPrivateCommentFlag() ? "on" : "N";
                    String finalFlag = minuteEntryInfoBean.isFinalFlag() ? "on" : "N";
                    
                    reviewForm.set(MINUTE_ENTRY, minuteEntry);
                    reviewForm.set(PROTO_CONTINGENCY_CODE, protoContingencyCode);
                    reviewForm.set(PRIVATE_COMMENT_FLAG, privateCommentFlag);
                    reviewForm.set(FINAL_FLAG, finalFlag);
                    if(protoContingencyCode != null && !EMPTY_STRING.equals(protoContingencyCode)){
                        request.setAttribute("contingencyComment", "Y");
                        request.setAttribute("selectedPredefinedComment","Y");
                    }
                }
              
            }
            // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
            String getAttachments =(String) request.getParameter("getAttachments");
            if("Y".equalsIgnoreCase(getAttachments)){
                reviewAttachmentBean = new ReviewAttachmentsBean();
                DynaValidatorForm reviewForm = (DynaValidatorForm) form;
                String editPersonId =  (String)reviewForm.get(PERSON_ID);
                Integer editAttachmentId = (Integer) reviewForm.get(ATTACHMENT_ID);
                Integer editSubmissionNum = (Integer) reviewForm.get(SUBMISSION_NUMBER);
                Integer editSeqNumber = (Integer) reviewForm.get(SEQUENCE_NUMBER);
                scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean();
                reviewAttachmentBean = (ReviewAttachmentsBean)scheduleMaintenanceTxnBean.getReviewAttachmentInfo(
                                        protocolNumber,editSeqNumber,editSubmissionNum,editPersonId,editAttachmentId);
                if(reviewAttachmentBean != null){
                    String reviewDocDescription = reviewAttachmentBean.getDescription();
                    String reviewFileName = reviewAttachmentBean.getFileName();
                    String privateAttachmentFlag = "Y".equalsIgnoreCase(reviewAttachmentBean.getPrivateAttachmentFlag())? "on" : "N";
                    reviewForm.set("reviewDocDescription",reviewDocDescription);
                    reviewForm.set("reviewFileName",reviewFileName);
                    reviewForm.set("reviewDocFileName",reviewFileName);
                    reviewForm.set(PRIVATE_ATTACHMENT_FLAG,privateAttachmentFlag);
                }
            }
            // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
           
            Map mapMenuList = new HashMap();
            mapMenuList.put("menuItems",CoeusliteMenuItems.IACUC_MENU_ITEMS);
            mapMenuList.put("menuCode",CoeusliteMenuItems.IACUC_PROTOCOL_REVIEW_COMMENTS_MENU);
            setSelectedMenuList(request, mapMenuList);
            
            navigator = CoeusConstants.SUCCESS_KEY;
        }
        if(SAVE_REVIEW_COMMENTS.equals(actionMapping.getPath())){
            scheduleMaintenanceTxnBean=new ScheduleMaintenanceTxnBean();
            
            DynaValidatorForm reviewForm = (DynaValidatorForm) form;
            // Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start            
            if(reviewForm.get(ACTION_PERFORMED_FROM) != null){
                actionFrom = (String) reviewForm.get(ACTION_PERFORMED_FROM);
            }
            if(ACTION_FROM_COMMENTS.equalsIgnoreCase(actionFrom)){
                String privateCommentFlag = (String) reviewForm.get(PRIVATE_COMMENT_FLAG);
                if("on".equalsIgnoreCase(privateCommentFlag)){
                    reviewForm.set(PRIVATE_COMMENT_FLAG, "Y");
                }else{
                    reviewForm.set(PRIVATE_COMMENT_FLAG, "N");
                }
                
                String finalFlag = (String) reviewForm.get(FINAL_FLAG);
                if("on".equalsIgnoreCase(finalFlag)){
                    reviewForm.set(FINAL_FLAG, "Y");
                }else{
                    reviewForm.set(FINAL_FLAG, "N");
                }
                
                String contingencyCode = (String) reviewForm.get(PROTO_CONTINGENCY_CODE);
                
                if(contingencyCode == null || EMPTY_STRING.equals(contingencyCode) || "null".equalsIgnoreCase(contingencyCode)){
                    reviewForm.set(PROTO_CONTINGENCY_CODE, null);
                }else{
                    reviewForm.set(PROTO_CONTINGENCY_CODE, contingencyCode);
                }
                
                String acType = (String) reviewForm.get(ACTION_TYPE);
                Timestamp dbTimestamp = prepareTimeStamp();
                if(INSERT_MODE.equalsIgnoreCase(acType)){
                    //Modified for COEUSDEV-236 : PI can't see protocol he/she created - Start
//                reviewForm.set(PERSON_ID, userInfoBean.getPersonId());
                    reviewForm.set(PERSON_ID, personId);
                    //COEUSDEV-236 : End
                    //Added for COEUSQA-2291 : Hide Reviewer Name in Review Comments - Start
                    reviewForm.set("createUser", userInfoBean.getUserId());
                    reviewForm.set("createTimestamp", dbTimestamp.toString());
                    //COEUSQA-2291 : End
                }
                
                reviewForm.set("updateTimestamp",dbTimestamp.toString());
                reviewForm.set("updateUser", userInfoBean.getUserId());
                // Added for the case#3282/3284  reviewer Views/comments-start
                boolean isReviewCommentPresent=true;
                if(acType.equalsIgnoreCase("U")){
                    Integer entryNumber =(Integer) reviewForm.get("entryNumber");
                    String scheduleId = (String)reviewForm.get("scheduleId");
                    isReviewCommentPresent = scheduleMaintenanceTxnBean.checkReviewCommentPresent(entryNumber,scheduleId);
                    //Added for COEUSDEV-237 : Investigator cannot see review comments - Start
                    //When scheduleId is changed, during modification new scheduleid is updated by deleting existing the previous
                    //Scheduleid data and insert the new scheduleid
                    ProtocolSubmissionInfoBean protocolSubInfoBean = (ProtocolSubmissionInfoBean) session.getAttribute(IACUC_PROTOCOL_SUBMISSION_INFO_BEAN);
                    if(protocolSubInfoBean != null){
                        if(protocolSubInfoBean.getScheduleId() != null){
                            String currentScheduleId = protocolSubInfoBean.getScheduleId();
                            if(!currentScheduleId.equals(scheduleId)){
                                reviewForm.set(ACTION_TYPE,DELETE_MODE);
                                Hashtable htReviewCommentsList = (Hashtable)webTxnBean.getResults(request,UPDATE_REVIEW_COMMENTES_LITE,reviewForm);
                                reviewForm.set("scheduleId",currentScheduleId);
                                reviewForm.set(ACTION_TYPE,INSERT_MODE);
                            }
                            
                        }
                    }
                    //COEUSDEV-237 : End
                    
                }
                if(!isReviewCommentPresent){
                    session.setAttribute(REQUEST_NO_IACUC_REVIEW_COMM_MESSAGE,"protcolReviewComments.noReviewCommentFound");
                }else{
                    Hashtable htReviewCommentsList = (Hashtable)webTxnBean.getResults(request,UPDATE_REVIEW_COMMENTES_LITE,reviewForm);
                }
                // Added for the case#3282/3284  reviewer Views/comments-end
            }else if(ACTION_FROM_ATTACHMENTS.equalsIgnoreCase(actionFrom)){
                
                Integer sequenceNumber =(Integer) reviewForm.get(SEQUENCE_NUMBER);
                Integer submsNumber =(Integer) reviewForm.get(SUBMISSION_NUMBER);
                scheduleUpdateTxnBean = new ScheduleMaintenanceUpdateTxnBean();
                String privateCommentFlag = (String) reviewForm.get(PRIVATE_ATTACHMENT_FLAG);
                if("on".equalsIgnoreCase(privateCommentFlag)){
                    reviewForm.set(PRIVATE_ATTACHMENT_FLAG, "Y");
                }else{
                    reviewForm.set(PRIVATE_ATTACHMENT_FLAG, "N");
                }
                
                String acType = (String) reviewForm.get("acType");
                Timestamp dbTimestamp = prepareTimeStamp();
                reviewForm.set("updateTimestamp",dbTimestamp.toString());
                reviewForm.set("updateUser", userInfoBean.getUserId());
                ReviewAttachmentsBean reviewAttachmentsBean = new ReviewAttachmentsBean();
                if(INSERT_MODE.equalsIgnoreCase(acType)){
                    
                    scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean();
                    int attachmentId = scheduleMaintenanceTxnBean.getNextReviewAttachmentID(protocolNumber,sequenceNumber,submsNumber);
                    
                    reviewForm.set(ATTACHMENT_ID, attachmentId);
                    reviewForm.set(PERSON_ID, personId);                                        
                    reviewForm.set("createUser", userInfoBean.getUserId());
                    reviewForm.set("createTimestamp", dbTimestamp.toString());
                    copyValuesToBean(reviewAttachmentsBean,reviewForm);
                    reviewAttachmentsBean.setUpdateTimestamp(dbTimestamp);
                    reviewAttachmentsBean.setCreateTimestamp(dbTimestamp);
                    DocumentTypeChecker typeChecker = new DocumentTypeChecker();
                    reviewAttachmentsBean.setMimeType(typeChecker.getDocumentMimeType(reviewAttachmentsBean));
                }
                boolean isReviewAttachmentPresent=true;
                if(acType.equalsIgnoreCase("U")){
                    isReviewAttachmentPresent = scheduleMaintenanceTxnBean.checkReviewAttachmentPresent(protocolNumber,sequenceNumber,submsNumber);                    
                    reviewForm.set(ATTACHMENT_ID, reviewForm.get(ATTACHMENT_ID));
                    reviewForm.set(PERSON_ID, reviewForm.get(PERSON_ID));                                                            
                    copyValuesToBean(reviewAttachmentsBean,reviewForm);
                    reviewAttachmentsBean.setUpdateTimestamp(dbTimestamp);
                    DocumentTypeChecker typeChecker = new DocumentTypeChecker();
                    reviewAttachmentsBean.setMimeType(typeChecker.getDocumentMimeType(reviewAttachmentsBean));
                }
                if(!isReviewAttachmentPresent){
                    session.setAttribute("noIacucReviewAttachmentMessage","protcolReviewComments.noReviewAttachmentFound");
                }else{
                    
                    Hashtable htReviewCommentsList = (Hashtable)webTxnBean.getResults(request,"updIacucReviewAttachmentsFromLite",reviewForm);
                    
                    if(!acType.equalsIgnoreCase("D") && reviewAttachmentsBean.getAttachment() != null){
                        Vector procedures = new Vector(3,2);
                        DBEngineImpl dbEngine = new DBEngineImpl();
                        procedures.addElement(scheduleUpdateTxnBean.addUpdReviewAttachment(reviewAttachmentsBean));
                        if(dbEngine!=null){
                            java.sql.Connection conn = null;
                            try{
                                if(procedures.size() > 0 ){
                                    conn = dbEngine.beginTxn();
                                    dbEngine.executeStoreProcs(procedures,conn);
                                    dbEngine.commit(conn);
                                }
                            }catch(Exception sqlEx){
                                dbEngine.rollback(conn);
                                throw new CoeusException(sqlEx.getMessage());
                            }finally{
                                dbEngine.endTxn(conn);
                            }
                        }else{
                            throw new CoeusException("db_exceptionCode.1000");
                        }
                    }
                }
            }
            // Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
            
            //Hashtable htReviewCommentsList = (Hashtable)webTxnBean.getResults(request,UPDATE_REVIEW_COMMENTES_LITE,reviewForm);
            navigator = CoeusConstants.SUCCESS_KEY;
        }
        
        if(GET_CONTINGENCY_DESC.equals(actionMapping.getPath())){
            
            scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean();
            DynaValidatorForm reviewForm = (DynaValidatorForm) form;
            String contingencyCode = (String) reviewForm.get(PROTO_CONTINGENCY_CODE);
            String contingencyDesc = EMPTY_STRING;
            if(contingencyCode != null && !EMPTY_STRING.equals(contingencyCode)){
                contingencyDesc = scheduleMaintenanceTxnBean.getContingencyDesc(contingencyCode);
            }
            if(contingencyDesc != null && !EMPTY_STRING.equals(contingencyDesc)){
                reviewForm.set(MINUTE_ENTRY, contingencyDesc);
            }
            request.setAttribute("contingencyComment", "Y");
            request.setAttribute("selectedPredefinedComment","Y");
            navigator = CoeusConstants.SUCCESS_KEY;
        }
        //Added for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start
        if(actionMapping.getPath().equals(REVIEW_COMPLETE)){
            
            //Modified for COEUSQA-2591 : Ability to mark review comments complete is not always possible for reviewers - Start
//            Timestamp dbTimestamp = prepareTimeStamp();
//             Vector vecReviewComments = (Vector)session.getAttribute("reviewComments");
//            if(vecReviewComments != null && vecReviewComments.size()>0){
//                for(int index=0;index<vecReviewComments.size();index++){
//                    DynaValidatorForm reviewCommentsForm = (DynaValidatorForm)vecReviewComments.get(index);
//                    String userPrivilegeInComment = (String)reviewCommentsForm.get(USER_PRIVILEGE_IN_REVIEW_COMMENTS);
//                    if((reviewCommentsForm.get(FINAL_FLAG) != null && !reviewCommentsForm.get(FINAL_FLAG).equals("Y")) 
//                            && userPrivilegeInComment != null && (userPrivilegeInComment.equals(VIEW_COMMENT) 
//                             || userPrivilegeInComment.equals(MODIFY_COMMENT))){
//                             reviewCommentsForm.set(FINAL_FLAG, "Y");
//                             reviewCommentsForm.set(ACTION_TYPE,TypeConstants.UPDATE_RECORD);
//                             reviewCommentsForm.set("updateTimestamp",dbTimestamp.toString());
//                             reviewCommentsForm.set("updateUser",userInfoBean.getUserId());
//                             Hashtable htReviewCommentsList = 
//                                     (Hashtable)webTxnBean.getResults(request,UPDATE_REVIEW_COMMENTES_LITE,reviewCommentsForm);
//                    }
//                   
//                }
//            }
            String isUserIACUCAdmin = (String)session.getAttribute(IS_IACUC_ADMIN);
            Vector vecAllReviewComments = null;
            // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
            Vector vecAllReviewAttachments = null;
            // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
            if("Y".equals(isUserIACUCAdmin)){
                vecAllReviewComments = (Vector)session.getAttribute(IACUC_ADMIN_REVIEW_COMMENTS);
                vecAllReviewAttachments = (Vector)session.getAttribute("iacucAdminReviewAttachments");     
            }else{
                // Modified for COEUSQA-2764 Comments by other reviewer are not showing until the user (viewing reviewer) marks their review complete - Start
//                vecReviewComments = (Vector)session.getAttribute(IACUC_REVIEW_COMMENTS);
                vecAllReviewComments =  new Vector();
                vecAllReviewComments.addAll((Vector)session.getAttribute(MY_REVIEW_COMMENTS));
                vecAllReviewComments.addAll((Vector)session.getAttribute(OTHER_REVIEWER_REVIEW_COMMENTS));
                // Modified for COEUSQA-2764 Comments by other reviewer are not showing until the user (viewing reviewer) marks their review complete - End
                // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
                vecAllReviewAttachments = new Vector();
                vecAllReviewAttachments.addAll((Vector)session.getAttribute(MY_REVIEW_ATTACHMENTS));
                vecAllReviewAttachments.addAll((Vector)session.getAttribute(OTHER_REVIEWER_REVIEW_ATTACHMENTS));
                // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
            }
            // Commented for COEUSQA-3232 : Reviewer View IRB and IACUC: Coeus should not mark non-final comments final when completing the review - Start
//            updateFinalFlagComments(vecAllReviewComments, isUserIACUCAdmin, userInfoBean, request);
            // Commented for COEUSQA-3232 : Reviewer View IRB and IACUC: Coeus should not mark non-final comments final when completing the review - End
            //COEUSQA-2591 : End
            ProtocolSubmissionTxnBean submissionTxnBean = new ProtocolSubmissionTxnBean();
            ProtocolSubmissionUpdateTxnBean submissionUpdateTxnBean = new ProtocolSubmissionUpdateTxnBean(userInfoBean.getUserId());
            String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
            int seqNumber = 0;
            if(sequenceNumber != null){
                seqNumber = Integer.parseInt(sequenceNumber);
            }
            Vector vecReviewers = submissionTxnBean.getProtocolReviewers(protocolNumber,seqNumber,submissionNumber);
            if(vecReviewers !=null && vecReviewers.size() > 0){
                for(int index=0;index<vecReviewers.size();index++){
                    ProtocolReviewerInfoBean reviewerDetails = (ProtocolReviewerInfoBean)vecReviewers.get(index);
                    if(reviewerDetails != null && reviewerDetails.getPersonId().equals(personId)){
                        reviewerDetails.setReviewComplete(true);
                        DynaValidatorForm reviewCommentsForm = (DynaValidatorForm)form;
                        reviewerDetails.setRecommendedActionCode((String)reviewCommentsForm.get("recommendedAction"));
                        reviewerDetails.setAcType(TypeConstants.UPDATE_RECORD);
                        reviewerDetails.setUpdateUser(userInfoBean.getUserId());
                        submissionUpdateTxnBean.addUpdProtocolReviewerDetails(reviewerDetails);
                        break;
                    }
                }
            }
            //Added for COEUSQA-3012 notification for when a reviewer completes their review IACUC - start
            session.setAttribute("MODULE_CODE", ModuleConstants.IACUC_MODULE_CODE+"");
            session.setAttribute("IACUC_ACTION_CODE", REVIEWER_REVIEW_COMPLETE_NOTIFICATION_CODE+""); 
            navigator = "email";
            // COEUSQA-3012 - end
            ActionForward actionForward = actionMapping.findForward(navigator);
            return actionForward;
        }
        //COEUSQA-2288 : End
        // Added for COEUSQA-3025 : Enhanced functionality to address the IACUC designated member review determination process - Start
        if("/iacucUpdateReviewDetermination".equals(actionMapping.getPath())){
            ProtocolSubmissionTxnBean submissionTxnBean = new ProtocolSubmissionTxnBean();
            String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
            Vector vecReviewDetermination = submissionTxnBean.getReviewDeterminations(
                    protocolNumber,Integer.parseInt(sequenceNumber),submissionNumber);
            ProtocolReviewDeterminationBean reviewDeterminationBean = null;
            String userPersonId = userDetailsBean.getPersonID(userInfoBean.getUserId());
            if(vecReviewDetermination != null && !vecReviewDetermination.isEmpty()){
                for(Object determinationDetails : vecReviewDetermination){
                    reviewDeterminationBean = (ProtocolReviewDeterminationBean)determinationDetails;
                    if(userPersonId.equals(reviewDeterminationBean.getPersonId())){
                        ProtocolSubmissionUpdateTxnBean submissionUpdateTxnBean = new ProtocolSubmissionUpdateTxnBean(userInfoBean.getUserId());
                        DynaValidatorForm reviewCommentsForm = (DynaValidatorForm)form;
                        // Modified for COEUSQA-3822 : Input string error on "Save" without entering a Determination Type in IACUC and
                        // COEUSQA-3821 : Coeus 4.5.1: Review Type Determination Save Error - Start
//                        reviewDeterminationBean.setRecommendedReviewTypeCode(Integer.parseInt((String)reviewCommentsForm.get("determinationReviewType")));                        
                        String reviewTypeDetermination =  (String)reviewCommentsForm.get("determinationReviewType");
                        if(reviewTypeDetermination != null && !"".equals(reviewTypeDetermination)){
                            reviewDeterminationBean.setRecommendedReviewTypeCode(Integer.parseInt(reviewTypeDetermination));
                        }else{
                            reviewDeterminationBean.setRecommendedReviewTypeCode(0);
                        }
                        // Modified for COEUSQA-3822 : Input string error on "Save" without entering a Determination Type in IACUC and
                        // COEUSQA-3821 : Coeus 4.5.1: Review Type Determination Save Error - Start
                        reviewDeterminationBean.setAcType(TypeConstants.UPDATE_RECORD);
                        submissionUpdateTxnBean.updReviewDetermination(reviewDeterminationBean);
                        break;
                    }
                    
                }
            }
            navigator = "success";
            ActionForward actionForward = actionMapping.findForward(navigator);
            return actionForward;
        }
        // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start        
         if(VIEW_ATTACHMENT.equals(actionMapping.getPath())){
            
            DynaValidatorForm reviewForm = (DynaValidatorForm) form;
            Integer attachmentId = (Integer)reviewForm.get(ATTACHMENT_ID);
            String templateURL= viewDocument(request,attachmentId);
            request.getSession().setAttribute("url", templateURL);
            response.sendRedirect(request.getContextPath()+templateURL);
            return null;    
         }
        // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
        // Added for COEUSQA-3025 : Enhanced functionality to address the IACUC designated member review determination process - End
        Vector vecReviewComments = new Vector();
        Vector vecContingencyList = new Vector();
        // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
        Vector vecReviewAttachments = new Vector();
        // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
        
        HashMap hmpProtocolData = new HashMap();
        hmpProtocolData.put("protocolNumber", protocolNumber);
        hmpProtocolData.put("submissionNumber", new Integer(submissionNumber));
        
        Hashtable htReviewCommentsList = (Hashtable)webTxnBean.getResults(request,"getIacucReviewCommentsList",hmpProtocolData);
        if(htReviewCommentsList != null){
            vecReviewComments = (Vector) htReviewCommentsList.get("getReviewComments");
            vecContingencyList = (Vector) htReviewCommentsList.get("getIacucContingencyList");
            // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
            vecReviewAttachments = (Vector)htReviewCommentsList.get("getIacucReviewAttachmentsForLite");
            // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
        }
                        
        request.setAttribute("contingencyList", vecContingencyList);
        //Added for COEUSQA-2290 : New Minute entry type for Review Comments - Start
        //Commented for COEUSQA-2591 : Ability to mark review comments complete is not always possible for reviewers - Start
//        CoeusFunctions coeusFunctions = new CoeusFunctions(userInfoBean.getUserId());
//        String minuteTypeCode = coeusFunctions.getParameterValue("IRB_MINUTE_TYPE_REVIEWER_COMMENT");
//        request.setAttribute("minuteTypeCode", minuteTypeCode);
//        ProtocolSubmissionInfoBean protoSubInfoBean = (ProtocolSubmissionInfoBean) session.getAttribute("protocolSubmissionInfoBean");
        //COEUSQA-2591 : End
        //COEUSQA-2290 : End
        //Added for the case#3282/ 3284 - Reviewer Views and Comments-start
        //Commented for COEUSDEV-237 : Investigator cannot see review comments - Start
        //"reviewComments" attribute is set to request in assignUserPrivilegeForComments method
//        session.setAttribute("reviewComments", vecAllReviewComments);
        //COEUSDEV-237 : End
        //Added for the case#3282/3284 Reviewer Views and comments-start
        // Review Comments from Past Submissions.
         if(intSubNumber!=null){
             submissionNumber = intSubNumber.intValue();
        }
        SubmissionDetailsTxnBean submissionDetailsTxnBean = new SubmissionDetailsTxnBean();       
        ProtocolSubmissionInfoBean protocolSubmissionInfoBeanForPastSub = new ProtocolSubmissionInfoBean();
        MinuteEntryInfoBean minuteEntryInfoBeanForPastSub = new MinuteEntryInfoBean();
        // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
        ReviewAttachmentsBean reviewAttachmentsBeanForPastSub = new ReviewAttachmentsBean();
        // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
        //UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
      
        Vector vecReviewCommentsForPastSubmission = new Vector();
        // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
        Vector vecReviewAttachmentsForPastSubmission = new Vector();
        HashMap mapAttachPersonName = new HashMap();
        // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
        HashMap mapPersonName = new HashMap();
        vecPastSubmission = submissionDetailsTxnBean.getDataSubmissionDetails(protocolNumber);
        if(vecPastSubmission!=null && vecPastSubmission.size()>0){
            // Added for COEUSQA-3170 : IRB and IACUC - Private attachments are being displayed to the investigator if he/she also has a reviewer role and they should not - Start
            String seqNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
            int sequenceNumber = -1;
            if(seqNumber!=null){
                sequenceNumber = Integer.parseInt(seqNumber);
            }
            boolean isProtocolInvOrKeyPerson = false;
            boolean isUserProtocolInvestigator =
                    protocolDataTxnBean.isUserProtocolInvestigator(protocolNumber,sequenceNumber,userInfoBean.getUserId());
            boolean isProtocolKeyPerson = isUserProtocolKeyPerson(protocolNumber,sequenceNumber,personId,userInfoBean.getUserId());
            // Added for COEUSQA-3170 : IRB and IACUC - Private attachments are being displayed to the investigator if he/she also has a reviewer role and they should not - End
            for(int i=0;i<vecPastSubmission.size();i++){
                protocolSubmissionInfoBeanForPastSub=(ProtocolSubmissionInfoBean)vecPastSubmission.get(i);
                if(protocolSubmissionInfoBeanForPastSub.getSubmissionNumber()==submissionNumber){
                    vecPastSubmission.remove(protocolSubmissionInfoBeanForPastSub);
                }else{  
                    vecReviewCommentsForPastSubmission = protocolSubmissionInfoBeanForPastSub.getProtocolReviewComments();
                    if(vecReviewCommentsForPastSubmission!=null && vecReviewCommentsForPastSubmission.size()>0){
                        //Modified for COEUSDEV-303 : Review View menu items are not enabled if the user has reviewer role - Start
//                        for(int indx=0;indx<reviewCommentsSize;indx++){
                        //Gets the leadunit of the submission protocol sequence
                        String protocolLeadUnit = protocolDataTxnBean.getLeadUnitForProtocol(protocolNumber, protocolSubmissionInfoBeanForPastSub.getSequenceNumber());
                        String userPrivilegeOnPastReviewComments =  EMPTY_STRING;
                        int reviewCommentsSize = vecReviewCommentsForPastSubmission.size();
                        //When comments are not available,rights are not checked
                        if(reviewCommentsSize > 0){
                            userPrivilegeOnPastReviewComments = protocolAuthorizationBean.getUserPrivilegeForReviewComments(protocolNumber,
                                    protocolSubmissionInfoBeanForPastSub.getSequenceNumber(),protocolLeadUnit,userInfoBean.getUserId(),protocolSubmissionInfoBeanForPastSub);
                        }
                        for(int indx=0;indx<reviewCommentsSize;indx++){//COEUSDEV-303 : End
                            minuteEntryInfoBeanForPastSub = (MinuteEntryInfoBean)vecReviewCommentsForPastSubmission.get(indx);
                            // Modified for COEUSQA-3170 : IRB and IACUC - Private attachments are being displayed to the investigator if he/she also has a reviewer role and they should not - Start
//                            if(minuteEntryInfoBeanForPastSub != null && minuteEntryInfoBeanForPastSub.getPersonId()!=null &&
//                                    !"".equalsIgnoreCase(minuteEntryInfoBeanForPastSub.getPersonId().trim()) &&  minuteEntryInfoBeanForPastSub.getPersonId().length()>0){
                          if(minuteEntryInfoBeanForPastSub != null){
                                //Modified for COEUSDEV-236 : PI can't see protocol he/she created - Start
                                //Getting the person details using userid. UpdateUser is userid
//                            Vector personInfo = userMaintDataTxnBean.getPersonInfo(minuteEntryInfoBeanForPastSub.getPersonId());
//                            if(personInfo!=null && personInfo.size()>0){
//                            mapPersonName.put(minuteEntryInfoBeanForPastSub.getPersonId(),(String)personInfo.elementAt(0));
//                            }
                                personInfoBean = userDetailsBean.getPersonInfo(minuteEntryInfoBeanForPastSub.getUpdateUser());
                                if(personInfoBean != null){
                                    mapPersonName.put(minuteEntryInfoBeanForPastSub.getPersonId(),(String)personInfoBean.getFullName());
                                }
                                //COEUSDEV-236 : End
                                //Added for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start
                                //Sets whether logged-in user is the IACUC Admin the protocol submission
                                if(CoeusConstants.IACUC_ADMIN.equals(userPrivilegeOnPastReviewComments)){
                                    minuteEntryInfoBeanForPastSub.setIsUserAdmin(true);
                                }else{
                                        minuteEntryInfoBeanForPastSub.setIsUserAdmin(false);
                                }
                                //COEUSQA-2288 : End
                                //Added for COEUSDEV-303 : Review View menu items are not enabled if the user has reviewer role - Start
                                //Checks user can view the comment otherwise comment is removed from collection
                                // Modified for COEUSQA-3170 : IRB and IACUC - Private attachments are being displayed to the investigator if he/she also has a reviewer role and they should not - Start
//                                boolean canViewReviewComment = checkuserCanViewComment(userPrivilegeOnPastReviewComments,minuteEntryInfoBeanForPastSub,personId);
                                if(isUserProtocolInvestigator || isProtocolKeyPerson){
                                    isProtocolInvOrKeyPerson = true;
                                }
                                boolean canViewReviewComment = checkuserCanViewComment(
                                        userPrivilegeOnPastReviewComments,minuteEntryInfoBeanForPastSub,personId,isProtocolInvOrKeyPerson,userInfoBean);
                                // Modified for COEUSQA-3170 : IRB and IACUC - Private attachments are being displayed to the investigator if he/she also has a reviewer role and they should not - End

                                if(!canViewReviewComment){
                                    //comments is removed from collection and collection size and index is decremented
                                    vecReviewCommentsForPastSubmission.remove(indx);
                                    reviewCommentsSize--;
                                    indx--;
                                }
                               //COEUSDEV-303 : End
                            }
                        }
                    }
                    
                    // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
                    //vecReviewAttachmentsForPastSubmission get the data of all attachments for past submissions
                    vecReviewAttachmentsForPastSubmission = protocolSubmissionInfoBeanForPastSub.getProtocolReviewAttachments();                     
                    if(vecReviewAttachmentsForPastSubmission!=null && vecReviewAttachmentsForPastSubmission.size()>0){
                        String protocolLeadUnit = protocolDataTxnBean.getLeadUnitForProtocol(protocolNumber, protocolSubmissionInfoBeanForPastSub.getSequenceNumber());                        
                        int reviewAttachmentsSize = vecReviewAttachmentsForPastSubmission.size();
                        String userPrivilegeOnPastReviewComments =  EMPTY_STRING;
                        //When attachments are not available,rights are not checked
                        if(reviewAttachmentsSize > 0){
                            userPrivilegeOnPastReviewComments = protocolAuthorizationBean.getUserPrivilegeForReviewAttachments(protocolNumber,
                                    protocolSubmissionInfoBeanForPastSub.getSequenceNumber(),protocolLeadUnit,userInfoBean.getUserId(),protocolSubmissionInfoBeanForPastSub);
                        }
                        for(int attachIndx=0;attachIndx<reviewAttachmentsSize;attachIndx++){//COEUSDEV-303 : End
                            reviewAttachmentsBeanForPastSub = (ReviewAttachmentsBean)vecReviewAttachmentsForPastSubmission.get(attachIndx);
                            if(reviewAttachmentsBeanForPastSub != null && reviewAttachmentsBeanForPastSub.getPersonId()!=null &&
                                    !"".equalsIgnoreCase(reviewAttachmentsBeanForPastSub.getPersonId().trim()) &&  reviewAttachmentsBeanForPastSub.getPersonId().length()>0){
                                
                                personInfoBean = userDetailsBean.getPersonInfo(reviewAttachmentsBeanForPastSub.getUpdateUser());
                                if(personInfoBean != null){
                                    mapAttachPersonName.put(reviewAttachmentsBeanForPastSub.getPersonId(),(String)personInfoBean.getFullName());
                                }
                                
                                //Sets whether logged-in user is the IRB Admin the protocol submission
                                if(CoeusConstants.IACUC_ADMIN.equals(userPrivilegeOnPastReviewComments)){
                                    reviewAttachmentsBeanForPastSub.setUserAdmin(true);
                                }else{
                                        reviewAttachmentsBeanForPastSub.setUserAdmin(false);
                                }
                                
                                //Checks user can view the comment otherwise comment is removed from collection
                                // Modified for COEUSQA-3170 : IRB and IACUC - Private attachments are being displayed to the investigator if he/she also has a reviewer role and they should not - Start
//                                boolean canViewReviewComment = checkUserCanViewAttachment(userPrivilegeOnPastReviewComments,reviewAttachmentsBeanForPastSub,personId);
                                boolean canViewReviewComment = checkUserCanViewAttachment(
                                        userPrivilegeOnPastReviewComments,reviewAttachmentsBeanForPastSub,personId,isProtocolInvOrKeyPerson,userInfoBean);
                                // Modified for COEUSQA-3170 : IRB and IACUC - Private attachments are being displayed to the investigator if he/she also has a reviewer role and they should not - End

                                if(!canViewReviewComment){
                                    //comments is removed from collection and collection size and index is decremented
                                    vecReviewAttachmentsForPastSubmission.remove(attachIndx);
                                    reviewAttachmentsSize--;
                                    attachIndx--;
                                }
                               
                            }
                        }
                    }
                    // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
                }
            }
            request.setAttribute("pastSubmissionComments",vecPastSubmission);
            // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
            request.setAttribute("mapAttachPersonName",mapAttachPersonName);
            // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
            request.setAttribute("mapPersonName",mapPersonName);
        }

    
         //Added for Past Submission 
     
        //Added for the case#3282/3284 Reviewer Views and comments-end

        
        // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - Start
        // Check if user has IACUC Admin Role
        String protocolLeadUnit = "";
        ProtocolHeaderDetailsBean protocolHeaderBean = (ProtocolHeaderDetailsBean) session.getAttribute("iacucHeaderBean");
        // Added for COEUSQA-3025 : Enhanced functionality to address the IACUC designated member review determination process - Start
        ProtocolSubmissionTxnBean submissionTxnBean = new ProtocolSubmissionTxnBean();
        Vector vecReviewDetermination = submissionTxnBean.getReviewDeterminations(
                                                            protocolHeaderBean.getProtocolNumber(),
                                                            Integer.parseInt(protocolHeaderBean.getSequenceNumber()),
                                                            submissionNumber);
        ProtocolReviewDeterminationBean reviewDeterminationBean = null;
        String userPersonId = userDetailsBean.getPersonID(userInfoBean.getUserId());
        if(vecReviewDetermination != null && !vecReviewDetermination.isEmpty()){
            for(Object determinationDetails : vecReviewDetermination){
                reviewDeterminationBean = (ProtocolReviewDeterminationBean)determinationDetails;
                if(userPersonId.equals(reviewDeterminationBean.getPersonId())){
                    request.setAttribute("enableRecommendReviewTypeLink",new Boolean(true));
                    DynaValidatorForm reviewCommentsForm = (DynaValidatorForm)form;
                    reviewCommentsForm.set("determinationReviewType",reviewDeterminationBean.getRecommendedReviewTypeCode()+"");
                    break;
                }
                request.setAttribute("enableRecommendReviewTypeLink",new Boolean(false));
            }
        }
        if(reviewDeterminationBean != null){
            java.sql.Date reviewDeterminationDate = reviewDeterminationBean.getDeterminationDueDate();
            String determinationDueDate =  reviewDeterminationDate.toString();
            DateUtils dateUtils = new DateUtils();
            String convertedDate = dateUtils.formatDate(determinationDueDate.toString(),DATE_FORMAT);
            CoeusFunctions coeusFunctions = new CoeusFunctions();
            java.sql.Date currentDate = new java.sql.Date(coeusFunctions.getDBTimestamp().getTime());
            if(reviewDeterminationDate.toString().equals(currentDate.toString()) || reviewDeterminationDate.after(currentDate)){
                request.setAttribute("enableReviewTypeDetermination",new Boolean(true));
            }else{
                request.setAttribute("enableReviewTypeDetermination",new Boolean(false));
            }
            request.setAttribute("determinationDueDate",convertedDate);
            // MOdified for COEUSQA-3471 : IACUC CoeusLite Review Type Determination Values   - Start
//            Hashtable hmRviewTypes =   (Hashtable)webTxnBean.getResults(request,"getIacucReviewTypes",null);
//            if(hmRviewTypes != null){
//                request.setAttribute(REVIEW_TYPES,(Vector)hmRviewTypes.get("getIacucReviewTypes"));
//            }
            Hashtable hmRviewTypes =   (Hashtable)webTxnBean.getResults(request,"getIacucReviewTypesDetermination",null);
            if(hmRviewTypes != null){
                request.setAttribute(REVIEW_TYPES,(Vector)hmRviewTypes.get("getIacucReviewTypesDetermination"));
            }
            // MOdified for COEUSQA-3471 : IACUC CoeusLite Review Type Determination Values   - End
        }
        // Added for COEUSQA-3025 : Enhanced functionality to address the IACUC designated member review determination process - End
        
        //Commented for COEUSDEV-237 : Investigator cannot see review comments - Start
        //Checked for Protocol status is in 'Amendment In progress' or ' Renewal In Progress'
//        if(protocolHeaderBean != null && protocolHeaderBean.getProtocolStatusCode() != 100) 
        if(protocolHeaderBean != null && protocolHeaderBean.getProtocolStatusCode() != PROTOCOL_STATUS_PENDING_IN_PROGRESS &&
               // protocolHeaderBean.getProtocolStatusCode() != 105 &&
                protocolHeaderBean.getProtocolStatusCode() != PROTOCOL_STATUS_RENEWAAL_IN_PROGRESS ){//COEUSDEV-237 : End
            ProtocolSubmissionInfoBean protoSubInfoBean = (ProtocolSubmissionInfoBean) session.getAttribute(IACUC_PROTOCOL_SUBMISSION_INFO_BEAN);
            if(protoSubInfoBean != null){
                protocolLeadUnit = protocolDataTxnBean.getLeadUnitForProtocol(protocolNumber, protoSubInfoBean.getSequenceNumber());
                //Modified for COEUSDEV-237 : Investigator cannot see review comments - Start
                // To Check if the user can enter Review Comments, Modify and view based on the rights(IRBAdministrator,Reviewer,Investigator)
//                boolean userHasIRBAdminRole = protocolAuthorizationBean.hasIRBAdminRights(userInfoBean.getUserId(), leadUnit);
//                if(userHasIRBAdminRole){
//                    request.setAttribute("userHasIRBAdminRole",CoeusLiteConstants.YES);
//                    
//                }else{
//                    request.setAttribute("userHasIRBAdminRole",CoeusLiteConstants.NO);
//                }
                
//                boolean canModifyReviewComments = protocolAuthorizationBean.canModifyReviewComments(protocolNumber, protoSubInfoBean.getSequenceNumber(),
//                        leadUnit, userInfoBean.getUserId());
//                if(canModifyReviewComments){
//                    request.setAttribute("canModifyComment",CoeusLiteConstants.YES);
//                } else {
//                    request.setAttribute("canModifyComment",CoeusLiteConstants.NO);
//                } 
                //Added for COEUSDEV-237 : Investigator cannot see review comments
                String seqNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
                int sequenceNumber = -1;
                if(seqNumber!=null){
                    sequenceNumber = Integer.parseInt(seqNumber);
                }
                //Gets the user privilege for review comments
                 String userPrivilegeOnReviewComments = protocolAuthorizationBean.getUserPrivilegeForReviewComments(protocolNumber,
                        sequenceNumber,protocolLeadUnit,userInfoBean.getUserId(),protoSubInfoBean);
     
                 //Modified COEUSQA-2242 : Investigator can add and modify review comments in IRB Lite - Start
//                 assignUserPrivilegeForComments(request,userPrivilegeOnReviewComments,vecReviewComments,personId);
                 //Logged-in user can add new review comments only if the user is IRBAdminRights/reviewer(unless review complete flag is set)
//                 if(userPrivilegeOnReviewComments.equals(CoeusConstants.IACUC_ADMIN) ||
//                         userPrivilegeOnReviewComments.equals(CoeusConstants.CREATE_VIEW_FINAL_MODIFY_OWN_COMMNENTS)){
//                     request.setAttribute(CAN_USER_CREATE_REVIEW_COMMNENTS,new Boolean(true));
//                 }
                 boolean isProtocolInvOrKeyPerson = false;
                 boolean isUserProtocolInvestigator = 
                         protocolDataTxnBean.isUserProtocolInvestigator(protocolNumber,sequenceNumber,userInfoBean.getUserId());
                 boolean isProtocolKeyPerson = isUserProtocolKeyPerson(protocolNumber,sequenceNumber,personId,userInfoBean.getUserId());
                 if(isUserProtocolInvestigator || isProtocolKeyPerson){
                     isProtocolInvOrKeyPerson = true; 
                 }
                 //COEUSQA-2242 : End
                 //Added for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start
                 boolean canUserCompleteReview = false;
                 boolean isReviewCompleted  = false;
                 boolean isAllReviewCommentsFinal = false;
                 boolean isProtocolReviewer = false;
                 boolean hasReviewerRight = false;
                 Vector vecRecommendedActions = null;
                 boolean userCanCreateComments = false;
                 boolean canViewUpdateDetails = false;
                 Vector vecProtocolReviewers = null;
                 //'Review Complete' link will be enabled on the following scenarios
                 //Logged-in user doesn't have IACUC-admin rights and is not the protocol investigator/study key persons
                 //only reviewer can perform 'Review Complete' for whom review is not completed
                 isProtocolReviewer =
                             protocolAuthorizationBean.isProtocolReviewer(protocolNumber,sequenceNumber,submissionNumber,personId);
                 //Added for COEUSQA-2591 : Ability to mark review comments complete is not always possible for reviewers - Start
                 if(CoeusConstants.IACUC_ADMIN.equals(userPrivilegeOnReviewComments) && isProtocolReviewer){
                     session.setAttribute(IS_IACUC_ADMIN,"Y");
                     Vector iacucAddedReviewComments =  getIACUCReviewComments(vecReviewComments,personId);
                     session.setAttribute(IACUC_ADMIN_REVIEW_COMMENTS,iacucAddedReviewComments);
                     // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
                     Vector iacucAddedReviewAttachments = getIACUCReviewAttachments(vecReviewAttachments,personId);
                     session.setAttribute(IACUC_ADMIN_REVIEW_ATTACHMENTS,iacucAddedReviewAttachments);
                     // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
                     
                 }else{
                     session.setAttribute(IS_IACUC_ADMIN,"N");
                     session.setAttribute(IACUC_ADMIN_REVIEW_COMMENTS,new Vector());
                     // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
                     session.setAttribute(IACUC_ADMIN_REVIEW_ATTACHMENTS,new Vector());
                     // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
                 }
                // UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                //"REVIEW_IACUC_PROTOCOL" right need to complete the review coment.
                // hasReviewerRight = userMaintDataTxnBean.getUserHasRight(userInfoBean.getUserId(),"REVIEW_IACUC_PROTOCOL",userInfoBean.getUnitNumber());
                 
 		 request.setAttribute("minuteTypeCode", getParameteMinuteTypeCode(userInfoBean.getUserId()));    
                 //IACUC admin can able to complete his/her review
//                  if(!CoeusConstants.IACUC_ADMIN.equals(userPrivilegeOnReviewComments) && !isProtocolInvOrKeyPerson){
//                     if(isProtocolReviewer || hasReviewerRight){
                 if(!isProtocolInvOrKeyPerson && (CoeusConstants.IACUC_ADMIN.equals(userPrivilegeOnReviewComments) &&
		    (isProtocolReviewer || hasReviewerRight))
                    || isProtocolReviewer || hasReviewerRight){
                     isReviewCompleted = protocolDataTxnBean.isProtocolReviwerReviewComplete(protocolNumber,
                             sequenceNumber,submissionNumber,personId);
                     if(CoeusConstants.IACUC_ADMIN.equals(userPrivilegeOnReviewComments) && isReviewCompleted){
                         request.setAttribute("minuteTypeCode", "3");
                     }else if(!isReviewCompleted){
                         canUserCompleteReview = true;
                         isAllReviewCommentsFinal = isAllReviewCommentsFinal(vecReviewComments, personId, session);
//                         ProtocolSubmissionTxnBean submissionTxnBean = new ProtocolSubmissionTxnBean();
                         // Modified for COEUSQA-2707 : Available Recommended Actions for IACUC Reviewers - Start
//                         vecRecommendedActions = submissionTxnBean.getRecommendedActionsForReviewer();
                         HashMap hmRecommendedActions = submissionTxnBean.getRecommendedActionsForAllReview();
                         String reviewTypeCode = protoSubInfoBean.getProtocolReviewTypeCode()+"";
                         if(!hmRecommendedActions.containsKey(reviewTypeCode)){
                             reviewTypeCode = "0";
                         }
                         vecRecommendedActions = (Vector)hmRecommendedActions.get(reviewTypeCode);
                         // Modified for COEUSQA-2707 : Available Recommended Actions for IACUC Reviewers - Start
                     }
                 }
                 //COEUSQA-2591 : End
                 //Modified for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start
                 //Assigning user privilege for individual review comments
//                 assignUserPrivilegeForComments(request,userPrivilegeOnReviewComments,
//                         vecAllReviewComments,personId,isProtocolInvOrKeyPerson);
                   //Logged-in user can add new review comments only if the user is not the protocol investigator(PI,CO_PI and keypersons) with
//                 //IRBAdminRights/reviewer(unless review complete flag is set)
//                 if(!isProtocolInvOrKeyPerson  && (userPrivilegeOnReviewComments.equals(CoeusConstants.IACUC_ADMIN) ||
//                         userPrivilegeOnReviewComments.equals(CoeusConstants.CREATE_VIEW_FINAL_MODIFY_OWN_COMMNENTS)) ){
//                     userCanCreateComments  = true;
//                 }
                 
                 //Logged-in user can add/modify review comments only if the user is not the protocol investigator(PI,CO_PI and keypersons) with
                 //IRBAdminRights/reviewer(unless review complete flag is set)
                 //User can't add review comments when the protocol submission is in 'Routing in progress'
                 boolean canModifyReviewComments = false;
                 //Modified for COEUSQA-2591 : Ability to mark review comments complete is not always possible for reviewers - Start
//                 if(!isReviewCompleted && !isProtocolInvOrKeyPerson  && (userPrivilegeOnReviewComments.equals(CoeusConstants.IACUC_ADMIN) ||//COEUSQA-2591
                    
                 if(!isProtocolInvOrKeyPerson  &&
                         (CoeusConstants.IACUC_ADMIN.equals(userPrivilegeOnReviewComments) || 
                         (CoeusConstants.CREATE_VIEW_FINAL_MODIFY_OWN_COMMNENTS.equals(userPrivilegeOnReviewComments))) &&
                         (protoSubInfoBean.getSubmissionStatusCode() == SUBMISSION_STATUS_SUBMITTED_TO_COMMITTEE ||
                         protoSubInfoBean.getSubmissionStatusCode() == SUBMISSION_STATUS_IN_AGENDA ||
                         protoSubInfoBean.getSubmissionStatusCode() == SUBMISSION_STATUS_PENDING)){
                     canModifyReviewComments = true;
                     userCanCreateComments  = true;
                 }
                 
                 if(CoeusConstants.IACUC_ADMIN.equals(userPrivilegeOnReviewComments)){
                     canViewUpdateDetails = true;
                 }else{
                     vecProtocolReviewers = getReviewers(request,protocolNumber,sequenceNumber,submissionNumber);
                 }
                 
                 // Method calling modified for internal issue fix #85
                 // Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
//                 assignUserPrivilegeForComments(request,userPrivilegeOnReviewComments,
//                         vecReviewComments,vecProtocolReviewers,personId,
//                         canModifyReviewComments,isReviewCompleted, isUserProtocolInvestigator);                 
                   assignUserPrivilegeForComments(request,userPrivilegeOnReviewComments,
                         vecReviewComments,vecReviewAttachments,vecProtocolReviewers,personId,
                         canModifyReviewComments,isReviewCompleted, isUserProtocolInvestigator);
                 
                 // Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
             
                 if(protoSubInfoBean.getSubmissionStatusCode() == SUBMISSION_STATUS_ROUTING_IN_PROGRESS){
                     String errMsg = "protocolReviewComments.submissionInRouting";
                     messages.add("errMsg", new ActionMessage(errMsg));
                     saveMessages(request, messages);
                 }
                 // Modified for COEUSQA-2764 Comments by other reviewer are not showing until the user (viewing reviewer) marks their review complete - Start
//                 Vector vecFilteredComments = (Vector)session.getAttribute("reviewComments");
//                 if(canUserCompleteReview && !isCommentsExistsForUser(vecFilteredComments, personId)){
//                     canUserCompleteReview = false;
//                 }
                   
                   // Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
//                 Vector vecMyReviewComments = (Vector)session.getAttribute(MY_REVIEW_COMMENTS);
//                 if(canUserCompleteReview && vecMyReviewComments != null && vecMyReviewComments.isEmpty()){
//                     canUserCompleteReview = false;
//                 }
                   Vector vecMyReviewComments = (Vector)session.getAttribute(MY_REVIEW_COMMENTS);
                   Vector vecMyReviewAttachments = (Vector)session.getAttribute(MY_REVIEW_ATTACHMENTS);
                   if(canUserCompleteReview && vecMyReviewComments != null && vecMyReviewAttachments != null && (vecMyReviewComments.isEmpty() && vecMyReviewAttachments.isEmpty())){
                       canUserCompleteReview = false;
                   }
                 // Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
                 // Modified for COEUSQA-2764 Comments by other reviewer are not showing until the user (viewing reviewer) marks their review complete - End
                 request.setAttribute(RECOMMENDED_ACTIONS,vecRecommendedActions);
                 request.setAttribute(IS_ALL_REIVEW_COMMENTS_MARKED_FINAL,isAllReviewCommentsFinal);
                 request.setAttribute(CAN_COMPLETE_REVIEW,canUserCompleteReview);
                 request.setAttribute(CAN_USER_CREATE_REVIEW_COMMNENTS,userCanCreateComments);
                 request.setAttribute(CAN_VIEW_UPDATE_DETALS,canViewUpdateDetails);
                 //COEUSQA-2288 : End
                 //Commented for COEUSDEV-303 : Review View menu items are not enabled if the user has reviewer role - Start
//                boolean canViewPastSubReviewComments = protocolAuthorizationBean.canViewPastSubmissionReviewComments(userInfoBean.getUserId(),protocolLeadUnit);
//                request.setAttribute(CAN_VIEW_PAST_SUBMISSION_COMMENTS,new Boolean(canViewPastSubReviewComments));
                 //COEUSDEV-303 : End
                //COEUSDEV-237 : END
                 
            }
        }
        // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - End
        ActionForward actionForward = actionMapping.findForward(navigator);
        return actionForward;
    }
    // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - Start
    // Moved the method to ProtocolAuthorizationBean.java with new method name hasIRBAdminRole()
//    //Added for the case#3282/3284 - Reviewer Views and Comments-start
//    private boolean checkUserHasAdminRole(HttpServletRequest request) throws DBException, CoeusException, org.okip.service.shared.api.Exception{
//        HttpSession session = request.getSession();
//        boolean userHasIRBAdminRole = false;
//        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
//        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
//        if(userInfoBean.getUserId()!=null){
//            userHasIRBAdminRole = userMaintDataTxnBean.getUserHasRole(userInfoBean.getUserId(),40);
//        }
//        return userHasIRBAdminRole;
//    }
//   //Added for the case#3282/3284 - Reviewer Views and Comments-end
   // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - End 
    
    
    //Added for COEUSDEV-237 : Investigator cannot see review comments - Start
    /*
     * Method to assign user privilege(Modify,View) for individual review comment
     * @param userPrivilegeOnReviewComments -  IACUC_ADMIN/CREATE_VIEW_FINAL_MODIFY_OWN_COMMNENTS/VIEW_FINAL_CANNOT_MODIFY_COMMENTS/VIEW_FINAL_NON_PRIVATE_COMMENTS
     * @param vecReviewComments
     * @param loggedInUserPersonId
     * @param isProtocolInvOrKeyPerson - boolean (User can't modify/ delete the review comment)
     */
      //Modified COEUSQA-2242 : Investigator can add and modify review comments in IRB Lite - Start
//    private void assignUserPrivilegeForComments(HttpServletRequest request,String userPrivilegeOnReviewComments,Vector vecReviewComments,
//     String loggedInUserPersonId)throws Exception{
//    private void assignUserPrivilegeForComments(HttpServletRequest request,String userPrivilegeOnReviewComments,Vector vecReviewComments,
//            String loggedInUserPersonId, boolean isProtocolInvOrKeyPerson)throws Exception{//COEUSQA-2242 : End
//    private void assignUserPrivilegeForComments(HttpServletRequest request,String userPrivilegeOnReviewComments,Vector vecReviewComments,
//            String loggedInUserPersonId, boolean canModify)throws Exception{//COEUSQA-2242 : End
    
    // Method signature modified for internal issue fix #85 passing the parameter boolean isUserProtocolInvestigator.
    // Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start    
//    private void assignUserPrivilegeForComments(HttpServletRequest request,String userPrivilegeOnReviewComments,Vector vecReviewComments,
//            Vector vecProtocolReviewers, String loggedInUserPersonId, boolean canModify, boolean isLoggedUserReviewCompleted, boolean isUserProtocolInvestigator)throws Exception{//COEUSQA-2242 : End
    private void assignUserPrivilegeForComments(HttpServletRequest request,String userPrivilegeOnReviewComments,Vector vecReviewComments,Vector vecReviewAttachments,
            Vector vecProtocolReviewers,String loggedInUserPersonId, boolean canModify, boolean isLoggedUserReviewCompleted, boolean isUserProtocolInvestigator)throws Exception{//COEUSQA-2242 : End
        // Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
        Vector vecOtherReviewerComments = new Vector();
        Vector vecMyReviewComments = new Vector();
        // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
        Vector vecOtherReviewerAttachments = new Vector();
        Vector vecMyReviewAttachments = new Vector();
        // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
        boolean userHasPrivilegeOnAtleastOneComment = false;
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        ProtocolAuthorizationBean authorizationBean = new ProtocolAuthorizationBean();
        if(vecReviewComments != null && vecReviewComments.size() > 0){
            for(int index=0;index<vecReviewComments.size();index++){
                DynaValidatorForm reviewCommentsForm = (DynaValidatorForm)vecReviewComments.get(index);
                 boolean userHasPrivilege = false;
                String finalFlag = (String)reviewCommentsForm.get(FINAL_FLAG);
                String privateCommentFlag = (String)reviewCommentsForm.get(PRIVATE_COMMENT_FLAG);
                //User can modify,view and remove the review comments
                if(userPrivilegeOnReviewComments.equals(CoeusConstants.IACUC_ADMIN)){
                    // Modified COEUSQA-2242 : Investigator can add and modify review comments in IRB Lite - Start
                    // IACUC admin can modify,remove,view review comments, unless user is not the investigator/key person
//                    reviewCommentsForm.set(USER_PRIVILEGE_IN_REVIEW_COMMENTS,MODIFY_COMMENT);
                    if(canModify){
                        reviewCommentsForm.set(USER_PRIVILEGE_IN_REVIEW_COMMENTS,MODIFY_COMMENT);
                    }else{
                        reviewCommentsForm.set(USER_PRIVILEGE_IN_REVIEW_COMMENTS,VIEW_COMMENT);
                    }
                    userHasPrivilege = true;
                    //COEUSQA-2242 : End
                    //User(Reviewer) can view only final reviewcomments of other users and modify his own review comments
                    //can view all the review comments added by the user
                }else if(userPrivilegeOnReviewComments.equals(CoeusConstants.CREATE_VIEW_FINAL_MODIFY_OWN_COMMNENTS)){
                    if(loggedInUserPersonId.equals(reviewCommentsForm.get(PERSON_ID))){
                        //Modified COEUSQA-2242 : Investigator can add and modify review comments in IRB Lite - Start
                        //User can modify,remove,view own review comments, unless user is not the investigator/key person
//                        reviewCommentsForm.set(USER_PRIVILEGE_IN_REVIEW_COMMENTS,MODIFY_COMMENT);
                        if(canModify){
                            reviewCommentsForm.set(USER_PRIVILEGE_IN_REVIEW_COMMENTS,MODIFY_COMMENT);
                        }else { 
                            reviewCommentsForm.set(USER_PRIVILEGE_IN_REVIEW_COMMENTS,VIEW_COMMENT);
                        }
                        userHasPrivilege = true;
                        //COEUSQA-2242 : End
                    }else if(finalFlag != null && finalFlag.equalsIgnoreCase("Y")){
                        //Modified for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start
                        //reviewCommentsForm.set(USER_PRIVILEGE_IN_REVIEW_COMMENTS,VIEW_COMMENT);
                        //Logged in user can view only the final comments of other reviewer, when the reviewer review completed
                        //can view final review comments entered by users other than reviewers
                        String protocolNumber = (String)reviewCommentsForm.get("protocolNumber");
                        int sequenceNumber = ((Integer)reviewCommentsForm.get("sequenceNumber")).intValue();
                        int submissionNumber = ((Integer)reviewCommentsForm.get("submissionNumber")).intValue();
                        String personId = (String)reviewCommentsForm.get(PERSON_ID);
                        boolean isProtocolReviewer = isProtocolReviewer(vecProtocolReviewers,personId);
                        if(isProtocolReviewer){
//                            boolean isReviewCompleted = protocolDataTxnBean.isProtocolReviwerReviewComplete(
//                                    protocolNumber, sequenceNumber,submissionNumber,personId);
                            boolean isReviewCompleted = isReviewerReviewCompleted(vecProtocolReviewers,personId);
                            if(isReviewCompleted){
                                reviewCommentsForm.set(USER_PRIVILEGE_IN_REVIEW_COMMENTS,VIEW_COMMENT);
                            }
                            userHasPrivilege= true;   
                        }else{
                            reviewCommentsForm.set(USER_PRIVILEGE_IN_REVIEW_COMMENTS,VIEW_COMMENT);
                            userHasPrivilege= true;  
                        }
                        //COEUSQA-2288 : End
                    }
                    //User(Reviewer) can view only final reviewcomments of other users and cannot modify his own review comments
                    //can view all the review comments added by the user
                }else if(userPrivilegeOnReviewComments.equals(CoeusConstants.VIEW_FINAL_CANNOT_MODIFY_COMMENTS)){
                    if(loggedInUserPersonId.equals(reviewCommentsForm.get(PERSON_ID))){
                        //can view all the review comments added by the user
                        reviewCommentsForm.set(USER_PRIVILEGE_IN_REVIEW_COMMENTS,VIEW_COMMENT);
                         userHasPrivilege = true;
                    }else if(finalFlag != null && finalFlag.equalsIgnoreCase("Y")){
                        //User can view other user comments if it is final
                        reviewCommentsForm.set(USER_PRIVILEGE_IN_REVIEW_COMMENTS,VIEW_COMMENT);
                         userHasPrivilege = true;
                    }
                    //User(Investigator) can only view final non-private comments
                }else if(userPrivilegeOnReviewComments.equals(CoeusConstants.VIEW_FINAL_NON_PRIVATE_COMMENTS)){
                    if(finalFlag != null && finalFlag.equalsIgnoreCase("Y") &&
                            privateCommentFlag != null && (privateCommentFlag.equals(EMPTY_STRING) || privateCommentFlag.equalsIgnoreCase("N"))){
                        reviewCommentsForm.set(USER_PRIVILEGE_IN_REVIEW_COMMENTS,VIEW_COMMENT);
                         userHasPrivilege = true;
                    }
                }
                // Commmented for COEUSQA-2764 Comments by other reviewer are not showing until the user (viewing reviewer) marks their review complete - Start
//                if(userHasPrivilege){
//                    vecOtherReviewerComments.add(reviewCommentsForm);
//                }
                // Commmented for COEUSQA-2764 Comments by other reviewer are not showing until the user (viewing reviewer) marks their review complete - End
                //COEUSQA-2291 : Start Shabarish.V
                CoeusFunctions coeusFunctions = new CoeusFunctions();
                String paramValue =  coeusFunctions.getParameterValue(CoeusConstants.IACUC_DISPLAY_REVIEWER_NAME);
                if("0".equals(paramValue)) {
                    if(userPrivilegeOnReviewComments.equals(CoeusConstants.IACUC_ADMIN)) {
                        reviewCommentsForm.set(CAN_USER_VIEW_REVIEWER,STRING_VALUE_TRUE);
                    } else if(loggedInUserPersonId.equals(reviewCommentsForm.get(PERSON_ID))) {
                        reviewCommentsForm.set(CAN_USER_VIEW_REVIEWER,STRING_VALUE_TRUE);
                    }else {
                        reviewCommentsForm.set(CAN_USER_VIEW_REVIEWER,STRING_VALUE_FALSE);
                    }
                }else {//When parameter value is '1'
                    reviewCommentsForm.set(CAN_USER_VIEW_REVIEWER,STRING_VALUE_TRUE);
                }//COEUSQA-2291 : End
                // Modified for COEUSQA-2764 Comments by other reviewer are not showing until the user (viewing reviewer) marks their review complete - Start
                if(userHasPrivilege){
                    String personId = (String)reviewCommentsForm.get(PERSON_ID);
                    // Comments created by logged-in user
                    if(loggedInUserPersonId.equals(personId)){
                        vecMyReviewComments.add(reviewCommentsForm);
                    }else{
                        boolean isOtherReviewer = isProtocolReviewer(vecProtocolReviewers,personId);
                        // IACUC Admin can view,modify,delete all review comments
                        if(CoeusConstants.IACUC_ADMIN.equals(userPrivilegeOnReviewComments)){
                            vecOtherReviewerComments.add(reviewCommentsForm);
                        // Other reviewer review comments can be seen by logged-in user(Reviewer) only if he/she completed the review and  Other reviewer complete his/her review
                            //Added for internal issue fix issue #85 start
                        }else if(isUserProtocolInvestigator){
                            
                            if(finalFlag != null && finalFlag.equalsIgnoreCase("Y") &&
                                privateCommentFlag != null && (privateCommentFlag.equals(EMPTY_STRING) || privateCommentFlag.equalsIgnoreCase("N"))){
                                vecOtherReviewerComments.add(reviewCommentsForm);
                            }
                         
                            //Added for internal issue fix issue #85 end
                        }else if(isOtherReviewer){
                            boolean isOtherReviewCompleted = isReviewerReviewCompleted(vecProtocolReviewers,personId);
                            if(isLoggedUserReviewCompleted && isOtherReviewCompleted){
                                String userPrivilegeOnComments = (String)reviewCommentsForm.get(USER_PRIVILEGE_IN_REVIEW_COMMENTS);
                                if(userPrivilegeOnComments != null && !EMPTY_STRING.equals(userPrivilegeOnComments)){
                                    vecOtherReviewerComments.add(reviewCommentsForm);
                                }
                            }
                        }else{
                            vecOtherReviewerComments.add(reviewCommentsForm);
                        }
                        
                    }
                }
                // Modified for COEUSQA-2764 Comments by other reviewer are not showing until the user (viewing reviewer) marks their review complete - End
            }
        }
        // Modified for COEUSQA-2764 Comments by other reviewer are not showing until the user (viewing reviewer) marks their review complete - Start
        // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
        if(vecReviewAttachments != null && vecReviewAttachments.size() > 0){
            for(int index=0;index<vecReviewAttachments.size();index++){
                DynaValidatorForm reviewCommentsForm = (DynaValidatorForm)vecReviewAttachments.get(index);
                boolean userHasPrivilege = false;
                String finalFlag = (String)reviewCommentsForm.get(ATTACHMENT_FINAL_FLAG);
                String privateCommentFlag = (String)reviewCommentsForm.get(PRIVATE_ATTACHMENT_FLAG);
                //User can modify,view and remove the review comments
                if(userPrivilegeOnReviewComments.equals(CoeusConstants.IACUC_ADMIN)){
                    //IRB admin can modify,remove,view review comments, unless user is not the investigator/key person
                    if(canModify){
                        reviewCommentsForm.set(USER_PRIVILEGE_IN_REVIEW_COMMENTS,MODIFY_COMMENT);
                    }else{
                        reviewCommentsForm.set(USER_PRIVILEGE_IN_REVIEW_COMMENTS,VIEW_COMMENT);
                    }
                    userHasPrivilege = true;
                    //User(Reviewer) can view only final reviewcomments of other users and modify his own review comments
                    //can view all the review comments added by the user
                }else if(userPrivilegeOnReviewComments.equals(CoeusConstants.CREATE_VIEW_FINAL_MODIFY_OWN_COMMNENTS)){
                    if(loggedInUserPersonId.equals(reviewCommentsForm.get(PERSON_ID))){
                        //User can modify,remove,view own review comments, unless user is not the investigator/key person
                        if(canModify){
                            reviewCommentsForm.set(USER_PRIVILEGE_IN_REVIEW_COMMENTS,MODIFY_COMMENT);
                        }else {
                            reviewCommentsForm.set(USER_PRIVILEGE_IN_REVIEW_COMMENTS,VIEW_COMMENT);
                        }
                        userHasPrivilege = true;
                    }else if(finalFlag != null && finalFlag.equalsIgnoreCase("Y")){
                        //Logged in user can view only the final comments of other reviewer, when the reviewer review completed
                        //can view final review comments entered by users other than reviewers
//                        String protocolNumber = (String)reviewCommentsForm.get(PROTOCOL_NUMBER);
//                        int sequenceNumber = ((Integer)reviewCommentsForm.get(SEQUENCE_NUMBER)).intValue();
//                        int submissionNumber = ((Integer)reviewCommentsForm.get(SUBMISSION_NUMBER)).intValue();
                        String personId = (String)reviewCommentsForm.get(PERSON_ID);
                        boolean isProtocolReviewer = isProtocolReviewer(vecProtocolReviewers,personId);
                        if(isProtocolReviewer){
                            boolean isReviewCompleted = isReviewerReviewCompleted(vecProtocolReviewers,personId);
                            if(isReviewCompleted){
                                reviewCommentsForm.set(USER_PRIVILEGE_IN_REVIEW_COMMENTS,VIEW_COMMENT);
                            }
                            userHasPrivilege = true;
                        }else{
                            reviewCommentsForm.set(USER_PRIVILEGE_IN_REVIEW_COMMENTS,VIEW_COMMENT);
                            userHasPrivilege = true;
                        }
                    }
                    //User(Reviewer) can view only final reviewcomments of other users and cannot modify his own review comments
                    //can view all the review comments added by the user
                }else if(userPrivilegeOnReviewComments.equals(CoeusConstants.VIEW_FINAL_CANNOT_MODIFY_COMMENTS)){
                    if(loggedInUserPersonId.equals(reviewCommentsForm.get(PERSON_ID))){
                        //can view all the review comments added by the user
                        reviewCommentsForm.set(USER_PRIVILEGE_IN_REVIEW_COMMENTS,VIEW_COMMENT);
                        userHasPrivilege = true;
                    }else if(finalFlag != null && finalFlag.equalsIgnoreCase("Y")){
                        //User can view other user comments if it is final
                        reviewCommentsForm.set(USER_PRIVILEGE_IN_REVIEW_COMMENTS,VIEW_COMMENT);
                        userHasPrivilege = true;
                    }
                    //User(Investigator) can only view final non-private comments
                }else if(userPrivilegeOnReviewComments.equals(CoeusConstants.VIEW_FINAL_NON_PRIVATE_COMMENTS)){
                    if(finalFlag != null && finalFlag.equalsIgnoreCase("Y") &&
                            privateCommentFlag != null && (privateCommentFlag.equals(EMPTY_STRING) || privateCommentFlag.equalsIgnoreCase("N"))){
                        reviewCommentsForm.set(USER_PRIVILEGE_IN_REVIEW_COMMENTS,VIEW_COMMENT);
                        userHasPrivilege = true;
                    }
                }
                CoeusFunctions coeusFunctions = new CoeusFunctions();
                String paramValue =  coeusFunctions.getParameterValue(CoeusConstants.IACUC_DISPLAY_REVIEWER_NAME);
                if(paramValue.equals("0")) {
                    if(userPrivilegeOnReviewComments.equals(CoeusConstants.IACUC_ADMIN)) {
                        reviewCommentsForm.set(CAN_USER_VIEW_REVIEWER,"true");
                    } else if(loggedInUserPersonId.equals(reviewCommentsForm.get(PERSON_ID))) {
                        reviewCommentsForm.set(CAN_USER_VIEW_REVIEWER,"true");
                    }else {
                        reviewCommentsForm.set(CAN_USER_VIEW_REVIEWER,"false");
                    }
                }else {//When parameter value is '1'
                    reviewCommentsForm.set(CAN_USER_VIEW_REVIEWER,"true");
                }
                if(userHasPrivilege){
                    String personId = (String)reviewCommentsForm.get(PERSON_ID);
                    // Comments created by logged-in user
                    if(loggedInUserPersonId.equals(personId)){
                        vecMyReviewAttachments.add(reviewCommentsForm);
                    }else{
                        boolean isOtherReviewer = isProtocolReviewer(vecProtocolReviewers,personId);
                        // IRB Admin can view,modify,delete all review comments
                        if(CoeusConstants.IACUC_ADMIN.equals(userPrivilegeOnReviewComments)){
                            vecOtherReviewerAttachments.add(reviewCommentsForm);
                            // Other reviewer review comments can be seen by logged-in user(Reviewer) only if he/she completed the review and  Other reviewer complete his/her review
                        }else if(isUserProtocolInvestigator){
                            if(finalFlag != null && finalFlag.equalsIgnoreCase("Y") &&
                                    privateCommentFlag != null && (privateCommentFlag.equals(EMPTY_STRING) || privateCommentFlag.equalsIgnoreCase("N"))){
                                vecOtherReviewerAttachments.add(reviewCommentsForm);
                            }
                        }else if(isOtherReviewer){
                            boolean isOtherReviewCompleted = isReviewerReviewCompleted(vecProtocolReviewers,personId);
                            if(isLoggedUserReviewCompleted && isOtherReviewCompleted){
                                String userPrivilegeOnComments = (String)reviewCommentsForm.get(USER_PRIVILEGE_IN_REVIEW_COMMENTS);
                                if(userPrivilegeOnComments != null && !EMPTY_STRING.equals(userPrivilegeOnComments)){
                                    vecOtherReviewerAttachments.add(reviewCommentsForm);
                                }
                            }
                        }else{
                            vecOtherReviewerAttachments.add(reviewCommentsForm);
                        }
                    }
                }
            }
        }
        request.getSession().setAttribute(MY_REVIEW_ATTACHMENTS, vecMyReviewAttachments);
        if(vecOtherReviewerAttachments != null || !vecOtherReviewerAttachments.isEmpty()){
            request.getSession().setAttribute(OTHER_REVIEWER_REVIEW_ATTACHMENTS, vecOtherReviewerAttachments);
        }else{
            request.getSession().setAttribute(OTHER_REVIEWER_REVIEW_ATTACHMENTS, new Vector());
        }
         // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
//        request.getSession().setAttribute("reviewComments", vecOtherReviewerComments);
        request.getSession().setAttribute(MY_REVIEW_COMMENTS, vecMyReviewComments);
        if(vecOtherReviewerComments != null || !vecOtherReviewerComments.isEmpty()){
            request.getSession().setAttribute(OTHER_REVIEWER_REVIEW_COMMENTS, vecOtherReviewerComments);
        }else{
            request.getSession().setAttribute(OTHER_REVIEWER_REVIEW_COMMENTS, new Vector());
        }
        // Modified for COEUSQA-2764 Comments by other reviewer are not showing until the user (viewing reviewer) marks their review complete - End
    }
    //COEUSDEV-237 : End
    
    //Added for COEUSDEV-303 : Review View menu items are not enabled if the user has reviewer role - Start
     /**
     * Method to check user can view the past submission review comment
     * 
     * @param userPrivilegeOnPastReviewComments
     * @param vecReviewComments
     * @param loggedInUserPersonId
     * @param isProtocolInvOrKeyPerson
     * @return canViewPastComment
     */
//    private boolean checkuserCanViewComment(String userPrivilegeOnPastReviewComments,MinuteEntryInfoBean minuteEntryInfoBean,
//             String loggedInUserPersonId)throws Exception{
    private boolean checkuserCanViewComment(String userPrivilegeOnPastReviewComments,MinuteEntryInfoBean minuteEntryInfoBean,
             String loggedInUserPersonId, boolean isProtocolInvOrKeyPerson, UserInfoBean userInfoBean)throws Exception{
        boolean canViewPastComment = false;
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        ProtocolAuthorizationBean authorizationBean = new ProtocolAuthorizationBean();
        if(minuteEntryInfoBean != null){
            boolean isFinalFlag = minuteEntryInfoBean.isFinalFlag();
            boolean isPrivateCommentFlag = minuteEntryInfoBean.isPrivateCommentFlag();
            String personId = minuteEntryInfoBean.getPersonId();
            //User can modify,view and remove the review comments
            if(userPrivilegeOnPastReviewComments.equals(CoeusConstants.IACUC_ADMIN)){
                canViewPastComment = true;
                //User(Reviewer) can view only final reviewcomments of other users and modify his own review comments
                //can view all the review comments added by the user
            }else if(userPrivilegeOnPastReviewComments.equals(CoeusConstants.CREATE_VIEW_FINAL_MODIFY_OWN_COMMNENTS)){
                if(loggedInUserPersonId.equals(personId) || userInfoBean.getUserId().equals(minuteEntryInfoBean.getCreateUser())){
                    //User can view own review comments
                    canViewPastComment = true;
                }else if(isFinalFlag){
                    //User can view other user comments if it is final and 
                    //if the logged-in user is not the investigator in the current sequence with the comments are non-private
                    if(!isProtocolInvOrKeyPerson || (isProtocolInvOrKeyPerson && !isPrivateCommentFlag)){
                        canViewPastComment = true;
                    }
                }
                //User(Reviewer) can view only final reviewcomments of other users and cannot modify his own review comments
                //can view all the review comments added by the user
            }else if(userPrivilegeOnPastReviewComments.equals(CoeusConstants.VIEW_FINAL_CANNOT_MODIFY_COMMENTS)){
                if(loggedInUserPersonId.equals(personId) || userInfoBean.getUserId().equals(minuteEntryInfoBean.getCreateUser())){
                    //can view all the review comments added by the user
                    canViewPastComment = true;
                }else if(isFinalFlag){
                    // User can view other user comments if it is final
                    // Only non private comments can be view by the logged-in user is investigator or key person in the current sequence
                    if(!isProtocolInvOrKeyPerson || (isProtocolInvOrKeyPerson && !isPrivateCommentFlag)){
                        canViewPastComment = true;
                        //Modified for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start
                        //reviewCommentsForm.set(USER_PRIVILEGE_IN_REVIEW_COMMENTS,VIEW_COMMENT);
                        //Logged in user can view only the final comments of other reviewer, when the reviewer review completed
                        //can view final review comments entered by users other than reviewers
                        String protocolNumber = minuteEntryInfoBean.getProtocolNumber();
                        int sequenceNumber = minuteEntryInfoBean.getSequenceNumber();
                        int submissionNumber = minuteEntryInfoBean.getSubmissionNumber();
                        boolean isProtocolReviewer = authorizationBean.isProtocolReviewer(
                                protocolNumber, sequenceNumber,submissionNumber,personId);
                        if(isProtocolReviewer){
                            boolean isReviewCompleted = protocolDataTxnBean.isProtocolReviwerReviewComplete(
                                    protocolNumber, sequenceNumber,submissionNumber,personId);
                            if(isReviewCompleted){
                                canViewPastComment = true;
                            }
                        }else{
                            canViewPastComment = true;
                        }
                    }
                     //COEUSQA-2288 : End
                }
                //User(Investigator) can only view final non-private comments
            }else if(userPrivilegeOnPastReviewComments.equals(CoeusConstants.VIEW_FINAL_NON_PRIVATE_COMMENTS)){
                if(isFinalFlag && !isPrivateCommentFlag){
                    canViewPastComment = true;
                }
            }
            //COEUSQA-2291 : Start Shabarish.V
            CoeusFunctions coeusFunctions = new CoeusFunctions();
            String paramValue =  coeusFunctions.getParameterValue(CoeusConstants.IACUC_DISPLAY_REVIEWER_NAME);
           if(paramValue!=null && paramValue!="" && "0".equals(paramValue)) {
                if(userPrivilegeOnPastReviewComments.equals(CoeusConstants.IACUC_ADMIN)){
                    minuteEntryInfoBean.setCanUserViewRewierName(STRING_VALUE_TRUE);
                }else if(loggedInUserPersonId.equals(personId)){
                    minuteEntryInfoBean.setCanUserViewRewierName(STRING_VALUE_TRUE);
                }else{
                    minuteEntryInfoBean.setCanUserViewRewierName(STRING_VALUE_FALSE);
                }
            } else {//When parameter value is '1'
                minuteEntryInfoBean.setCanUserViewRewierName(STRING_VALUE_TRUE);
            }//COEUSQA-2291 : End


        }
        return canViewPastComment;
    }
    // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
    /**
     * Method to check user can view the past submission review comment
     * 
     * @param userPrivilegeOnPastReviewComments
     * @param vecReviewComments
     * @param loggedInUserPersonId
     * @return canViewPastComment
     */
//    private boolean checkUserCanViewAttachment(String userPrivilegeOnPastReviewComments,ReviewAttachmentsBean reviewAttachmentsBeanForPastSub,
//             String loggedInUserPersonId)throws Exception{
      private boolean checkUserCanViewAttachment(String userPrivilegeOnPastReviewComments,ReviewAttachmentsBean reviewAttachmentsBeanForPastSub,
                 String loggedInUserPersonId, boolean isProtocolInvOrKeyPerson, UserInfoBean userInfoBean)throws Exception{
        boolean canViewPastComment = false;
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        ProtocolAuthorizationBean authorizationBean = new ProtocolAuthorizationBean();
        if(reviewAttachmentsBeanForPastSub != null){
            String isFinalFlag = reviewAttachmentsBeanForPastSub.getFinalAttachmentFlag();
            String isPrivateCommentFlag = reviewAttachmentsBeanForPastSub.getPrivateAttachmentFlag();
            String personId = reviewAttachmentsBeanForPastSub.getPersonId();
            //User can modify,view and remove the review comments
            if(userPrivilegeOnPastReviewComments.equals(CoeusConstants.IACUC_ADMIN)){
                canViewPastComment = true;
                //User(Reviewer) can view only final reviewcomments of other users and modify his own review comments
                //can view all the review comments added by the user
            }else if(userPrivilegeOnPastReviewComments.equals(CoeusConstants.CREATE_VIEW_FINAL_MODIFY_OWN_COMMNENTS)){
                if(loggedInUserPersonId.equals(personId) || userInfoBean.getUserId().equals(reviewAttachmentsBeanForPastSub.getCreateUser())){
                    //User can view own review comments
                    canViewPastComment = true;
                }else if("Y".equalsIgnoreCase(isFinalFlag)){
                    //User can view other user comments if it is final
                     //if the logged-in user is not the investigator in the current sequence with the comments are non-private
                    if(!isProtocolInvOrKeyPerson || (isProtocolInvOrKeyPerson && "N".equals(isPrivateCommentFlag))){
                        canViewPastComment = true;
                    }
                }
                //User(Reviewer) can view only final reviewcomments of other users and cannot modify his own review comments
                //can view all the review comments added by the user
            }else if(userPrivilegeOnPastReviewComments.equals(CoeusConstants.VIEW_FINAL_CANNOT_MODIFY_COMMENTS)){
                if(loggedInUserPersonId.equals(personId) || userInfoBean.getUserId().equals(reviewAttachmentsBeanForPastSub.getCreateUser())){
                    //can view all the review comments added by the user
                    canViewPastComment = true;
                }else if("Y".equalsIgnoreCase(isFinalFlag)){
                    //User can view other user comments if it is final
                    //if the logged-in user is not the investigator in the current sequence with the comments are non-private
                    if(!isProtocolInvOrKeyPerson || (isProtocolInvOrKeyPerson && "N".equals(isPrivateCommentFlag))){
                        canViewPastComment = true;
                        //Logged in user can view only the final comments of other reviewer, when the reviewer review completed
                        //can view final review comments entered by users other than reviewers
                        String protocolNumber = reviewAttachmentsBeanForPastSub.getProtocolNumber();
                        int sequenceNumber = reviewAttachmentsBeanForPastSub.getSequenceNumber();
                        int submissionNumber = reviewAttachmentsBeanForPastSub.getSubmissionNumber();
                        boolean isProtocolReviewer = authorizationBean.isProtocolReviewer(
                                protocolNumber, sequenceNumber,submissionNumber,personId);
                        if(isProtocolReviewer){
                            boolean isReviewCompleted = protocolDataTxnBean.isProtocolReviwerReviewComplete(
                                    protocolNumber, sequenceNumber,submissionNumber,personId);
                            if(isReviewCompleted){
                                canViewPastComment = true;
                            }
                        }else{
                            canViewPastComment = true;
                        }
                    }
                }
                //User(Investigator) can only view final non-private comments
            }else if(userPrivilegeOnPastReviewComments.equals(CoeusConstants.VIEW_FINAL_NON_PRIVATE_COMMENTS)){
                if("Y".equalsIgnoreCase(isFinalFlag) && !"Y".equalsIgnoreCase(isPrivateCommentFlag)){
                    canViewPastComment = true;
                }
            }            
            CoeusFunctions coeusFunctions = new CoeusFunctions();
            String paramValue =  coeusFunctions.getParameterValue(CoeusConstants.IACUC_DISPLAY_REVIEWER_NAME);
           if(paramValue!=null && paramValue!="" && paramValue.equals("0")) {
                if(userPrivilegeOnPastReviewComments.equals(CoeusConstants.IACUC_ADMIN)){
                    reviewAttachmentsBeanForPastSub.setCanUserViewReviewerName("true");
                }else if(loggedInUserPersonId.equals(personId)){
                    reviewAttachmentsBeanForPastSub.setCanUserViewReviewerName("true");
                }else{
                    reviewAttachmentsBeanForPastSub.setCanUserViewReviewerName("false");
                }
            } else {//When parameter value is '1'
                reviewAttachmentsBeanForPastSub.setCanUserViewReviewerName("true");
            }
        }
        return canViewPastComment;
    }

   // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
    /*
     * Method to check the logged_in user is the study key person
     * @param protocolNumber - String
     * @param sequenceNumber - String
     * @param userPersonId  - String
     * @param userId - String
     * @return isProtocolKeyPerson - boolean
     */
    public boolean isUserProtocolKeyPerson(String protocolNumber, int sequenceNumber,String userPersonId,
            String userId)throws DBException{
        boolean isProtocolKeyPerson = false;
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
        Vector vecStudyKeyPersons = protocolDataTxnBean.getProtocolKeyPersonList(protocolNumber,sequenceNumber);
        if(vecStudyKeyPersons != null && vecStudyKeyPersons.size() > 0){
            for(int index=0;index<vecStudyKeyPersons.size();index++){
                ProtocolKeyPersonnelBean keyPersonDetails = (ProtocolKeyPersonnelBean)vecStudyKeyPersons.get(index);
                String keyPersonId = keyPersonDetails.getPersonId();
                if(keyPersonId != null && keyPersonId.equals(userPersonId)){
                    isProtocolKeyPerson = true;
                }
            }
        }
        return isProtocolKeyPerson;
    }
    //COEUSQA-2242 : End
    
    //Added for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start
    /*
     * Method to check all the visible review comments are final
     * @param vecReviewComments - Vector
     * @return isAllReviewCommentsFinal - boolean
     */
    //Modified for COEUSQA-2591 : Ability to mark review comments complete is not always possible for reviewers - Start
//    public boolean isAllReviewCommentsFinal(Vector vecReviewComments)
    public boolean isAllReviewCommentsFinal(Vector vecReviewComments ,String loggedInPersonId, HttpSession session){
        boolean isAllReviewCommentsFinal = true;
        String isUserIACUCAdmin = (String)session.getAttribute(IS_IACUC_ADMIN);
        if(vecReviewComments != null && vecReviewComments.size() > 0){
            for(int index=0;index<vecReviewComments.size();index++){
                DynaValidatorForm reviewCommentsForm = (DynaValidatorForm)vecReviewComments.get(index);
                String finalFlag = (String)reviewCommentsForm.get(FINAL_FLAG);
                //Modified for COEUSQA-2591 : Ability to mark review comments complete is not always possible for reviewers - Start
                //If the logged-in user is IACUC Administrator,
                //Final flag check is done only for the review comments created by IRB Administrator
//                if(finalFlag != null && "Y".equalsIgnoreCase(finalFlag)){
//                    isAllReviewCommentsFinal = true;
//                    break;
                // Modified for COEUSQA-3232 : Reviewer View IRB and IACUC: Coeus should not mark non-final comments final when completing the review - Start
//                String creatorPersonId = (String)reviewCommentsForm.get("personId");
//                if("Y".equals(isUserIACUCAdmin)){
//                    if(loggedInPersonId.equals(creatorPersonId) && finalFlag != null && "Y".equalsIgnoreCase(finalFlag)){
//                        isAllReviewCommentsFinal = true;
//                        break;
//                    }
//                }else if(finalFlag != null && "Y".equalsIgnoreCase(finalFlag)){
//                        isAllReviewCommentsFinal = true;
//                        break;
//                }
                String creatorPersonId = (String)reviewCommentsForm.get("personId");
                if(loggedInPersonId.equals(creatorPersonId) && finalFlag != null && "N".equalsIgnoreCase(finalFlag)){
                    isAllReviewCommentsFinal = false;
                }
                // Modified for COEUSQA-3232 : Reviewer View IRB and IACUC: Coeus should not mark non-final comments final when completing the review - End
            }
        }
        return isAllReviewCommentsFinal;
    }
    //COEUSQA-2288 : End
    
    //Added for COEUSQA-2591 : Ability to mark review comments complete is not always possible for reviewers - Start
    /*
     * Method to get the Review comments entered by the logged in user whose is IACUC Administrator 
     * @param Vector - vecReviewComments
     * @param String - loggedInUserPersonId
     */
    private Vector getIACUCReviewComments(Vector vecReviewComments, String loggedInUserPersonId){
        Vector vecIacucComments = new Vector();
        if(vecReviewComments != null && vecReviewComments.size() > 0){
            for(int index=0;index<vecReviewComments.size();index++){
                 DynaValidatorForm reviewCommentsForm = (DynaValidatorForm)vecReviewComments.get(index);
                 String creatorPersonId = (String)reviewCommentsForm.get(PERSON_ID);
                 if(loggedInUserPersonId.equals(creatorPersonId)){
                     vecIacucComments.add(reviewCommentsForm);
                 }
            }
        }
        return vecIacucComments;
    }
    
    // Commented for COEUSQA-3232 : Reviewer View IRB and IACUC: Coeus should not mark non-final comments final when completing the review - Start
    /*
     * Method to set final flag for review comments which are non-final
     * @param Vector - vecReviewComments
     * @param String - isUserIACUCAdmin
     * @param UserInfoBean - userInfoBean
     * @param HttpServletRequest - request
     */
//    private void updateFinalFlagComments(Vector vecReviewComments, String isUserIACUCAdmin, 
//            UserInfoBean userInfoBean, HttpServletRequest request) throws Exception{
//        Timestamp dbTimestamp = prepareTimeStamp();
//        WebTxnBean webTxnBean = new WebTxnBean();
//        if(vecReviewComments != null && vecReviewComments.size()>0){
//            for(int index=0;index<vecReviewComments.size();index++){
//                DynaValidatorForm reviewCommentsForm = (DynaValidatorForm)vecReviewComments.get(index);
//                //If the user is the IACUC Admin, then all the comments created by the user with non-final comments will
//                //be updated to final 
//                if("Y".equals(isUserIACUCAdmin) && !"Y".equals(reviewCommentsForm.get(FINAL_FLAG))){
//                    reviewCommentsForm.set(FINAL_FLAG, "Y");
//                    reviewCommentsForm.set(ACTION_TYPE,TypeConstants.UPDATE_RECORD);
//                    reviewCommentsForm.set("updateTimestamp",dbTimestamp.toString());
//                    reviewCommentsForm.set("updateUser",userInfoBean.getUserId());
//                    Hashtable htReviewCommentsList =
//                            (Hashtable)webTxnBean.getResults(request,UPDATE_REVIEW_COMMENTES_LITE,reviewCommentsForm);
//                }else{
//                    String userPrivilegeInComment = (String)reviewCommentsForm.get(USER_PRIVILEGE_IN_REVIEW_COMMENTS);
//                    if((reviewCommentsForm.get(FINAL_FLAG) != null && !"Y".equals(reviewCommentsForm.get(FINAL_FLAG)))
//                    && userPrivilegeInComment != null && (userPrivilegeInComment.equals(VIEW_COMMENT)
//                    || userPrivilegeInComment.equals(MODIFY_COMMENT))){
//                        reviewCommentsForm.set(FINAL_FLAG, "Y");
//                        reviewCommentsForm.set(ACTION_TYPE,TypeConstants.UPDATE_RECORD);
//                        reviewCommentsForm.set("updateTimestamp",dbTimestamp.toString());
//                        reviewCommentsForm.set("updateUser",userInfoBean.getUserId());
//                        Hashtable htReviewCommentsList =
//                                (Hashtable)webTxnBean.getResults(request,UPDATE_REVIEW_COMMENTES_LITE,reviewCommentsForm);
//                    }
//                }
//            }
//        }
//    }
    // Commented for COEUSQA-3232 : Reviewer View IRB and IACUC: Coeus should not mark non-final comments final when completing the review - End
    
    /*
     * Method to get the 'IACUC_MINUTE_TYPE_REVIEWER_COMMENT' paramemte value
     * @param String - userId
     * @return String - minuteTypeCode
     */
    private String getParameteMinuteTypeCode(String userId)throws CoeusException, DBException{
        CoeusFunctions coeusFunctions = new CoeusFunctions(userId);
        String minuteTypeCode = coeusFunctions.getParameterValue(CoeusConstants.IACUC_MINUTE_TYPE_REVIEWER_COMMENT);
        return minuteTypeCode;
    }
    
    // Commented for COEUSQA-2764 Comments by other reviewer are not showing until the user (viewing reviewer) marks their review complete - Start
    /**
     *  This method will identifies the logged in user added any review comments or not.
     *  @param vecReviewComments java.lang.Vector
     *  @param loggedInUserPersonId String
     *  @return isCommentsExistsForUser boolean
     */
//    private boolean isCommentsExistsForUser(Vector vecReviewComments, String loggedInUserPersonId){
//        boolean isCommentsExistsForUser = false;
//        
//        for(Object obj : vecReviewComments){
//            DynaValidatorForm reviewCommentsForm = (DynaValidatorForm)obj;
//            
//            if(loggedInUserPersonId.equals(reviewCommentsForm.get("personId"))){
//                isCommentsExistsForUser = true;
//                break;
//            }
//        }
//        return isCommentsExistsForUser;
//    }
    // Commented for COEUSQA-2764 Comments by other reviewer are not showing until the user (viewing reviewer) marks their review complete - End
    //COEUSQA-2591 : End
    
    // Added for COEUSQA-2764 Comments by other reviewer are not showing until the user (viewing reviewer) marks their review complete - Start
    /*
     * Method to check whether the reviewer completed his/her review
     * @param vecProtocolReviewers - Vector
     * @param personId - String
     * @return isReviewerReviewCompleted - boolean
     */
    private boolean isReviewerReviewCompleted(Vector vecProtocolReviewers, String personId){
        boolean isReviewerReviewCompleted = false;
        if(vecProtocolReviewers != null && !vecProtocolReviewers.isEmpty()){
            for(Object reviewersFormObject : vecProtocolReviewers){
               DynaValidatorForm reviewersForm =  (DynaValidatorForm)reviewersFormObject;
                String reviewerPersonId = (String)reviewersForm.get(PERSON_ID);
                String reviewComplete = (String)reviewersForm.get(REVIEW_COMPLETE_PROPERTY);
                if(personId.equals(reviewerPersonId) && "Y".equals(reviewComplete)){
                    isReviewerReviewCompleted = true;
                    break;
                }
            }
        }
        return isReviewerReviewCompleted;
    }
    
        /*
         * Method to check whether the person is protocol reviewer
         * @param vecProtocolReviewers - Vector
         * @param personId - String
         * @return isProtocolReviewer - boolean
         */
    private boolean isProtocolReviewer(Vector vecProtocolReviewers, String personId){
        boolean isProtocolReviewer = false;
        if(vecProtocolReviewers != null && !vecProtocolReviewers.isEmpty()){
            for(Object reviewersFormObject : vecProtocolReviewers){
                DynaValidatorForm reviewersForm =  (DynaValidatorForm)reviewersFormObject;
                String reviewerPersonId = (String)reviewersForm.get("personId");
                if(personId.equals(reviewerPersonId) ){
                    isProtocolReviewer = true;
                    break;
                }
            }
        }
        return isProtocolReviewer;
    }

    // Added for COEUSQA-2764 Comments by other reviewer are not showing until the user (viewing reviewer) marks their review complete - End
    // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
    /*
     * Method to get the Review Attachments included by the logged in user who is IRB Administrator 
     * @param Vector - vecReviewAttachments
     * @param String - loggedInUserPersonId
     * @return Vector of attachments added by logged in User
     */
    private Vector getIACUCReviewAttachments(Vector vecReviewAttachments, String loggedInUserPersonId){
        Vector vecIACUCAttachments = new Vector();
        
        if(vecReviewAttachments != null && !vecReviewAttachments.isEmpty()){
            for(int index=0;index<vecReviewAttachments.size();index++){
                 DynaValidatorForm reviewCommentsForm = (DynaValidatorForm)vecReviewAttachments.get(index);
                 String creatorPersonId = (String)reviewCommentsForm.get(PERSON_ID);
                 if(loggedInUserPersonId.equals(creatorPersonId)){
                     vecIACUCAttachments.add(reviewCommentsForm);
                 }
            }
        }
        return vecIACUCAttachments;
    }
    
    /**
     * Method to copy values from DynaValidatorForm to ReviewAttachmentsBean
     * @param ReviewAttachmentsBean reviewAttachmentsBean
     * @param DynaValidatorForm reviewersForm
     */
    private void copyValuesToBean(ReviewAttachmentsBean reviewAttachmentsBean,DynaValidatorForm reviewersForm){        
        reviewAttachmentsBean.setProtocolNumber((String)reviewersForm.get(PROTOCOL_NUMBER));
        reviewAttachmentsBean.setSequenceNumber(Integer.parseInt(reviewersForm.get(SEQUENCE_NUMBER).toString()));
        reviewAttachmentsBean.setSubmissionNumber(Integer.parseInt(reviewersForm.get(SUBMISSION_NUMBER).toString()));
        reviewAttachmentsBean.setPersonId((String)reviewersForm.get(PERSON_ID));
        reviewAttachmentsBean.setAttachmentNumber(Integer.parseInt(reviewersForm.get(ATTACHMENT_ID).toString()));
        reviewAttachmentsBean.setDescription((String)reviewersForm.get("reviewDocDescription"));
        reviewAttachmentsBean.setFileName((String)reviewersForm.get("reviewDocFileName"));        
        FormFile myFile = (FormFile)reviewersForm.get("reviewDocument");
        if(myFile != null){
            try {
                byte[] fileData = myFile.getFileData();
                if(fileData.length >0){
                    reviewAttachmentsBean.setAttachment(fileData);// BLOB data
                }else{
                    reviewAttachmentsBean.setAttachment(null);
                }
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        reviewAttachmentsBean.setPrivateAttachmentFlag((String)reviewersForm.get(PRIVATE_ATTACHMENT_FLAG));
        reviewAttachmentsBean.setUpdateUser((String)reviewersForm.get("updateUser"));        
        reviewAttachmentsBean.setCreateUser((String)reviewersForm.get("createUser"));
    }
    
    
    /*
     * Get the attachment ,for the given attachment id, for viewing.
     * @param HttpServletRequest request is the request to view the attachment
     * @param int attachmentId 
     * @return String the url to view the attachment  
     * @exception throws Exception
     */
    private String viewDocument(HttpServletRequest request , int attachmentId) throws Exception{        
            String ProtocolNumber = (String)request.getSession().getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+ 
                                            request.getSession().getId());
            
            ReviewAttachmentsBean reviewAttachmentsBean = new ReviewAttachmentsBean();
            reviewAttachmentsBean.setProtocolNumber(ProtocolNumber);
            reviewAttachmentsBean.setAttachmentNumber(attachmentId);
            DocumentBean documentBean = new DocumentBean();
            Map map = new HashMap();
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.iacuc.ProtocolDocumentReader");
            map.put("DOCUMENT_TYPE","IACUC_REVIEW_ATTACHMENT_DOC");
            map.put("PROTO_REVIEW_ATTACH_BEAN", reviewAttachmentsBean);
            documentBean.setParameterMap(map);
            String docId = DocumentIdGenerator.generateDocumentId();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("/StreamingServlet");
            stringBuffer.append("?");
            stringBuffer.append(DocumentConstants.DOC_ID);
            stringBuffer.append("=");
            stringBuffer.append(docId);
            request.getSession().setAttribute(docId, documentBean);
            return stringBuffer.toString();
    }
    // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
    
}
