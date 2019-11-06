/*
 * RoutingAction.java
 *
 * Created on March 18, 2008, 1:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/* PMD check performed, and commented unused imports and variables on 31-MAY-2011
 * by Maharaja Palanichamy
 */
package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.bean.ActionTransaction;
import edu.mit.coeus.propdev.bean.NotepadBean;
import edu.mit.coeus.propdev.bean.NotepadTxnBean;
import edu.mit.coeus.routing.bean.RoutingAttachmentBean;
import edu.mit.coeus.routing.bean.RoutingBean;
import edu.mit.coeus.routing.bean.RoutingCommentsBean;
import edu.mit.coeus.routing.bean.RoutingDetailsBean;
import edu.mit.coeus.routing.bean.RoutingTxnBean;
import edu.mit.coeus.routing.bean.RoutingUpdateTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.TypeConstants;
//import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeuslite.utils.CoeusDynaFormList;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.SearchModuleBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.utk.coeuslite.propdev.bean.ApprovalRouteDisplayBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.MultipartRequestWrapper;
/**
 *
 * @author noorula
 */
public class RoutingAction extends ProposalBaseAction {
    private static final String PROPOSAL_MESSAGES = "ProposalMessages";
    private static final String BY_PASS = "ByPass";
    private static final String APPROVE = "approve";
    private static final String REJECT = "Reject";
    private static final String ACTION_MODE = "actionMode";
    private static final String SUCCESS ="success";
    private static final String APPROVE_ACTION = "A";
    private static final String REJECT_ACTION = "R";
    private static final String BYPASSS_ACTION = "B";
    private static final String WAITING_FOR_APPROVAL = "W" ;
    private static final String PRIMARY_APPROVER_FLAG = "Y";
    private static final String CREATION_STATUS_CODE = "creationStatusCode" ;
    private static final String BYPASS_COMMENTS = "approve_proposal_bypass_comments" ;


    private static final String GET_ROUTING_ACTIONS = "/getApprovalActions" ;
    private static final String APPROVE_PROPOSAL = "/approveUser" ;
    private static final String SAVE_APPROVAL_DETAILS = "/saveApprovalDetails";
    //Statement IDs
    private static final String APPROVE_PROPOSAL_ALL = "/approveAllUsers";
    private static final String GET_PROP_APPROVAL_FOR_STATUS = "getPropApprovalForStatus" ;
    private static final String UPDATE_PROPOSAL_STATUS = "updateProposalStatus";
    private static final String GET_PROP_APPROVAL_MAPS = "getProposalApprovalMaps" ;
    private static final String GET_PROP_APPROVAL_STATUS = "getProposalApprovalStatus" ;
    private static final String  EDIT_APPROVAL_COMMENTS = "/editApprovalComments";
    private static final String  REMOVE_APPROVAL_DETAILS = "/removeApprovalDetails";
    private static final String  VIEW_ATTACHMENTS = "/viewApprovalAttachments";
    private static final int IRB_SUB_STATUS_SUBMITTED_TO_COMMITTEE = 100;
    private static final int IRB_SUB_STATUS_PENDING = 102;
    private static final int IACUC_SUB_STATUS_SUBMITTED_TO_COMMITTEE = 102;
    private static final int IACUC_SUB_STATUS_PENDING = 101;
    // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - Start
    private static final int IRB_SUBMITTED_TO_IRB_STATUS = 101;
    private static final int IACUC_SUBMITTED_TO_IACUC_STATUS = 101;
    // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - End
    // COEUSQA-1497: No ability to pass routing Authority in IRB Lite - Start
    private static final String MAP_NUMBER_FIELD = "mapNumber";
    private static final String LEVEL_NUMBER_FIELD = "levelNumber";
    private static final String STOP_NUMBER_FIELD = "stopNumber";
    private static final String DESCRIPTION = "Approver added by ";


    private static final String PASS_ACTION = "P";
    private static final String ADD_APPROVER = "/addApprover";
    private static final String REMOVE_APPROVER = "/removeApprover";
    // COEUSQA-1497: No ability to pass routing Authority in IRB Lite - End
    //COEUSQA-3086-"Submit" Button Disappears when User Attempts to Re-Submit Amendment After Rejection - Start
    private static final String GET_IRB_PROTOCOL_INFO = "getProtocolInfo";
    private static final String GET_IACUC_PROTOCOL_INFO = "getIacucInfo";
    //COEUSQA-3086-"Submit" Button Disappears when User Attempts to Re-Submit Amendment After Rejection - End
    //COEUSQA-1433 - Allow Recall from Routing - Start
    private static final String RECALL = "Recall";
    private static final String RECALL_ACTION = "RE";
    private static final String MAIL_NOTIFICATION_ACTION = "512";
     private static final String EMAIL = "email";
    //COEUSQA-1433 - Allow Recall from Routing - End


    /** Creates a new instance of RoutingAction */
    public RoutingAction() {
    }


    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {
        CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList)actionForm ;
        HttpSession session = request.getSession() ;
        String actionMode = EMPTY_STRING ;
        String navigator = SUCCESS ;

        String moduleItemKey=null;
        Integer moduleCode=0;
         String userId=null;
        HashMap hml=new HashMap();
        WebTxnBean webTxnBean=new WebTxnBean();
        actionMode = request.getParameter(ACTION_MODE);
        if(actionMapping.getPath().equals(GET_ROUTING_ACTIONS)){
             UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
                ActionMapping mapping = actionMapping;
                String routingNumber=(String)session.getAttribute("routingNumber"+session.getId());
                moduleItemKey=(String)session.getAttribute("moduleItemKey");
                userId=(String)session.getAttribute("userId");
                moduleCode=(Integer)session.getAttribute("moduleCode");
                hml.put("routingNumber",routingNumber);
                

                // to get the approver count
                 Hashtable levelList = (Hashtable) webTxnBean.getResults(request,"getLevelCount",hml);
                 HashMap levelMap = (HashMap)levelList.get("getLevelCount");
                 String apprCount=(String)levelMap.get("li_ret");
                 // Modified for COEUSQA-3816 : Lite - Proposal routing - Locking issues - Start
//                 if(apprCount!=null && apprCount.equalsIgnoreCase("1")){
                 if("recall".equalsIgnoreCase(actionMode) || 
                         "approve".equalsIgnoreCase(actionMode) ||
                         "reject".equalsIgnoreCase(actionMode)){// Modified for COEUSQA-3816 : End
                    if( moduleCode == ModuleConstants.IACUC_MODULE_CODE){
                        session.setAttribute("mode"+session.getId(),EMPTY_STRING);
                        boolean isvalid = prepareLockProtoIACUC(moduleItemKey, request);
                       if(!isvalid){
                             session.setAttribute("mode"+session.getId(),"display");
                                    }
                       else{
                        session.setAttribute("mode"+session.getId(),"M");

                        }
                      }
                       else if (moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE ){
                        boolean isvalid = prepareLockProto(moduleItemKey, request);
                            if(!isvalid){
                                  session.setAttribute("mode"+session.getId(),"display");
                              }
                            else{
                                 session.setAttribute("mode"+session.getId(),"M");
                               }
                        }
                       else if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                        boolean isvalid = prepareLock(userInfoBean,moduleItemKey,request);
                           if(!isvalid){
                                      session.setAttribute("mode"+session.getId(),"display");
                                  }
                                else{
                                    session.setAttribute("mode"+session.getId(),"M");
                                     }
                        }
                     }
                     else{session.setAttribute("mode"+session.getId(),"M");
                         }
            
            if(actionMode!=null && !actionMode.equals(EMPTY_STRING)){
   //  levelNumber= (Integer)session.getAttribute("levelNumber");
                DynaValidatorForm dynaForm = (DynaValidatorForm) coeusDynaFormList.getDynaForm(request,"approvalRoute");
                DynaValidatorForm dynaFormData = (DynaValidatorForm) coeusDynaFormList.getDynaForm(request,"approvalRoute");
                List lstData = new ArrayList();
                coeusDynaFormList.setList(lstData);
                lstData = new ArrayList();
                coeusDynaFormList.setBeanList(lstData);
                lstData = new ArrayList();
                coeusDynaFormList.setInfoList(lstData);
                lstData = new ArrayList();
                coeusDynaFormList.setDataList(lstData);
                coeusDynaFormList.getList().add(0, dynaForm);
                coeusDynaFormList.getInfoList().add(0, dynaFormData);
                MessageResources messages = MessageResources.getMessageResources(PROPOSAL_MESSAGES);
                String byPassComments = messages.getMessage(BYPASS_COMMENTS);
                if(actionMode.equalsIgnoreCase(BY_PASS)){
                    dynaForm.set("comments" , byPassComments) ;
                }
                String beanName = request.getParameter("beanName");
                ApprovalRouteDisplayBean byPassBean =
                (ApprovalRouteDisplayBean)session.getAttribute(beanName+session.getId());
                //COEUSQA-1433 - Allow Recall from Routing - Start
                boolean isInLastApproval = checkForLastStageApproval(request, coeusDynaFormList);
                session.setAttribute("isInLastStageApproval"+session.getId(),isInLastApproval);
                //COEUSQA-1433 - Allow Recall from Routing - End
                session.setAttribute("byPassApproval"+session.getId(), byPassBean);
                dynaForm.set(ACTION_MODE ,actionMode.trim());
                String label = request.getParameter(ACTION_MODE);
                session.setAttribute(ACTION_MODE, label);
                session.setAttribute(ACTION_MODE, actionMode.trim());
                session.setAttribute("approvalRoutingList", coeusDynaFormList);
            }
        } else  if(actionMapping.getPath().equals(SAVE_APPROVAL_DETAILS)){
            // COEUSQA-1497: No ability to pass routing Authority in IRB Lite - Start
            ArrayList beanList = (ArrayList) coeusDynaFormList.getList();
            CoeusVector newApprovers = null;
            DynaValidatorForm dynaForm;
            if(beanList != null){
                dynaForm = (DynaValidatorForm) beanList.get(0);
                newApprovers = (CoeusVector) dynaForm.get("newApprovers");
            }
            // COEUSQA-1497: No ability to pass routing Authority in IRB Lite - End


            String addingType = request.getParameter("addingType");
            if(addingType!=null && addingType.equals("comments")){
                navigator = saveComments(request, coeusDynaFormList, true);
            } else if(addingType!=null && addingType.equals("attachments")){
                navigator = saveAttachments(request, coeusDynaFormList, true);
            }
            // COEUSQA-1497: No ability to pass routing Authority in IRB Lite - Start
            beanList = (ArrayList) coeusDynaFormList.getList();
            if(beanList != null){
                dynaForm = (DynaValidatorForm) beanList.get(0);
                dynaForm.set("newApprovers", newApprovers);
            }
            // COEUSQA-1497: No ability to pass routing Authority in IRB Lite - End
        } else if(actionMapping.getPath().equals(APPROVE_PROPOSAL)){
 UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            actionMode = (String) session.getAttribute(ACTION_MODE);
            if(actionMode!=null && !actionMode.equals(EMPTY_STRING)){
                if(actionMode.equalsIgnoreCase(APPROVE)){
                    // COEUSQA-1497: No ability to pass routing Authority in IRB Lite - Start
                    // Get the newly added approvers from the dynaForm
                    CoeusVector newApprovers = null;


                    ArrayList beanList = (ArrayList) coeusDynaFormList.getList();


                    if(beanList != null){
                        DynaValidatorForm dynaForm = (DynaValidatorForm) beanList.get(0);
                        newApprovers = (CoeusVector) dynaForm.get("newApprovers");
                    }



                    navigator = saveAttachments(request, coeusDynaFormList, false);
                    if(navigator != null && navigator.equals("errorPresent")){
                        return actionMapping.findForward(navigator);
                    }

                    //COEUSQA-2457: Administrator cannot view Attachments in Routing in Lite. - end
                    navigator = saveComments(request, coeusDynaFormList, false);

                    String operation = request.getParameter("operation");
                    boolean passOperationPerformed= false;
                    if(PASS_ACTION.equalsIgnoreCase(operation)){
                        passOperationPerformed = true;


                        if(coeusDynaFormList.getBeanList() == null
                                || coeusDynaFormList.getBeanList().size() == 0){
                            ActionMessages actionMessages = new ActionMessages();
                            actionMessages.add("passCommentsReqd",
                                    new ActionMessage("proposal.commentsRequiresForPass"));
                            saveMessages(request, actionMessages);
                            navigator = "errorPresent";
                        }
                    }




                    if(navigator != null && navigator.equals("errorPresent")){
                        return actionMapping.findForward(navigator);
                    }
                    Vector vecPropApprovalStatus = null;


                    if(!passOperationPerformed){
                        vecPropApprovalStatus = getPropApprovalForStatus(request);
                    }

                    if(vecPropApprovalStatus!=null && vecPropApprovalStatus.size()>1){
                        request.setAttribute("approveAll", "approveAll");
                        navigator = "approveAll";
                        SearchModuleBean moduleBean = (SearchModuleBean)session.getAttribute(CoeusLiteConstants.PROPOSAL_SEARCH_ACTION+session.getId());
                        request.setAttribute("newApprovers", newApprovers);
                    }else{

//                        navigator = performApproveAction(request, coeusDynaFormList, 0, response);
                        navigator = performApproveAction(request, coeusDynaFormList, 0, newApprovers, response);
                        //Used for checking final approver in the last seqential stop 
                        boolean isInLastApproval = checkForLastApproval(request, coeusDynaFormList);
                        if (isInLastApproval) {
                            if (navigator != null && "success".equals(navigator)) {
                                RoutingBean routingBean = (RoutingBean) session.getAttribute("routingBean" + session.getId());
                           
                                if (routingBean.getModuleCode() == ModuleConstants.IACUC_MODULE_CODE) {
                                    //to release lock
                                    
                                    String lockId = CoeusLiteConstants.PROTOCOL_LOCK_STR + routingBean.getModuleItemKey();
                                    LockBean lockBean = getApprovalLockingBean(userInfoBean, routingBean.getModuleItemKey(), lockId, routingBean.getModuleCode(), request);
                     
                                    releaseLock(lockBean, request);
                                    session.removeAttribute(CoeusLiteConstants.IACUC_PROTOCOL_LOCK);
                                   // session.setAttribute("IACUC_ACTION_CODE", MAIL_NOTIFICATION_ACTION);
                                  // session.setAttribute("MODULE_CODE", ("" + ModuleConstants.IACUC_MODULE_CODE));
                                   session.setAttribute("MODULE_CODE", "9"); //Module Code for IRB
                                    session.setAttribute("IACUC_ACTION_CODE", "101");   
                                 //   session.setAttribute("MODULE_CODE", ModuleConstants.IACUC_MODULE_CODE+"");   
                                 //   session.setAttribute("IACUC_ACTION_CODE", "101"); // Action Code for Protocol Submission
                                    navigator = "iacucEmail";
                                    return actionMapping.findForward(navigator);
                                } else if (routingBean.getModuleCode() == ModuleConstants.PROTOCOL_MODULE_CODE) {
                                    //to release lock
                                    String lockId = CoeusLiteConstants.IACUC_PROTO_LOCK_STR + routingBean.getModuleItemKey();
                                    LockBean lockBean = getApprovalLockingBean(userInfoBean, routingBean.getModuleItemKey(), lockId, routingBean.getModuleCode(), request);
                                    releaseLock(lockBean, request);
                                    session.removeAttribute(CoeusLiteConstants.PROTOCOL_LOCK);
                                    //    session.setAttribute("ACTION_CODE",MAIL_NOTIFICATION_ACTION);
                                    //     session.setAttribute("MODULE_CODE", (""+ModuleConstants.PROTOCOL_MODULE_CODE));
                                    session.setAttribute("MODULE_CODE", "7"); //Module Code for IRB
                                    session.setAttribute("ACTION_CODE", "101");
                                    navigator = EMAIL;
                                    return actionMapping.findForward(navigator);
                                }
                            }
                        }
                        // COEUSQA-1497: No ability to pass routing Authority in IRB Lite - End
                        //COEUSQA-1433 - Allow Recall from Routing - Start
                        RoutingBean routingBean = (RoutingBean) session.getAttribute("routingBean"+session.getId());
                         userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
                        if(routingBean.getModuleCode() == ModuleConstants.IACUC_MODULE_CODE){
                            //to release lock
                            String lockId = CoeusLiteConstants.IACUC_PROTO_ROUTING_LOCK_STR+routingBean.getModuleItemKey();
                            LockBean lockBean = getApprovalLockingBean(userInfoBean, routingBean.getModuleItemKey(), lockId, routingBean.getModuleCode(), request);
                            releaseLock(lockBean, request);
                            session.removeAttribute(CoeusLiteConstants.IACUC_PROTOCOL_LOCK);
                        }else if(routingBean.getModuleCode() == ModuleConstants.PROTOCOL_MODULE_CODE){
                            //to release lock
                            String lockId = CoeusLiteConstants.PROTO_ROUTING_LOCK_STR+routingBean.getModuleItemKey();
                            LockBean lockBean = getApprovalLockingBean(userInfoBean, routingBean.getModuleItemKey(), lockId, routingBean.getModuleCode(), request);
                            releaseLock(lockBean, request);
                            session.removeAttribute(CoeusLiteConstants.PROTOCOL_LOCK);
                        }else if(routingBean.getModuleCode() == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                            //to release lock
                            LockBean lockBean = getLockingBean(userInfoBean, routingBean.getModuleItemKey(), request);
                            releaseLock(lockBean, request);
                            session.removeAttribute(CoeusLiteConstants.PROPOSAL_LOCK);
                             session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(),
                                    new Boolean(false));
                            }
						}
                        //COEUSQA-1433 - Allow Recall from Routing - End
                    }
                else if(actionMode.equalsIgnoreCase(REJECT)){
                    //COEUSQA-2457: Administrator cannot view Attachments in Routing in Lite. - start
                    navigator = saveAttachments(request, coeusDynaFormList, false);
                    //COEUSQA-2457: Administrator cannot view Attachments in Routing in Lite. - end
                    navigator = saveComments(request, coeusDynaFormList, false);
                    if(navigator != null && navigator.equals("errorPresent")){
                        return actionMapping.findForward(navigator);
                    }
                    if(coeusDynaFormList.getBeanList() == null
                            || coeusDynaFormList.getBeanList().size() == 0){
                        ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("rejectioncomments",
                        new ActionMessage("proposal.commentsrequiresforReject"));
                        saveMessages(request, actionMessages);
                        navigator = "errorPresent";
                    } else {
                        navigator = performRejectAction(request, coeusDynaFormList, response);
                        RoutingBean routingBean = (RoutingBean) session.getAttribute("routingBean"+session.getId());
                        if(routingBean.getModuleCode() == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                            //COEUSQA-3880: Lock on Proposal is not released after a proposal is rejected
                            LockBean lockBean = getLockingBean(userInfoBean, routingBean.getModuleItemKey(), request);
                            releaseLock(lockBean, request);
                            session.removeAttribute(CoeusLiteConstants.PROPOSAL_LOCK);
                            //COEUSQA-3880: Lock on Proposal is not released after a proposal is rejected
                            
                             if(request instanceof MultipartRequestWrapper){
                                request = ((MultipartRequestWrapper)request).getRequest();
                            }
                            RequestDispatcher rd = request.getRequestDispatcher(
                                    "displayProposal.do?proposalNo="+routingBean.getModuleItemKey());
                            rd.forward(request,response);
                            return null;
                        }
                    }
                    //COEUSQA-3086-"Submit" Button Disappears when User Attempts to Re-Submit Amendment After Rejection - Start
                    if(navigator!=null && "success".equals(navigator)){
                        RoutingBean routingBean = (RoutingBean) session.getAttribute("routingBean"+session.getId());
                        webTxnBean = new WebTxnBean();
                         userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
                         userId = userInfoBean.getUserId();
                        //To check if the module is IRB
                        if(routingBean.getModuleCode() == ModuleConstants.PROTOCOL_MODULE_CODE){
                            HashMap hmProtoApproval = new HashMap();
                            hmProtoApproval.put("protocolNumber" , (String)session.getAttribute("PROTOCOL_NUMBER"+session.getId()));
                            Hashtable htProtoApproval =
                                    (Hashtable)webTxnBean.getResults(request, GET_IRB_PROTOCOL_INFO , hmProtoApproval);
                            Vector vecProtoApproval = (Vector)htProtoApproval.get(GET_IRB_PROTOCOL_INFO);
                            Vector vecFilteredData = new Vector();
                            if(vecProtoApproval != null && vecProtoApproval.size() > 0){
                                for(int index = 0; index < vecProtoApproval.size(); index++){
                                    DynaValidatorForm dynaValidatorForm =
                                            (DynaValidatorForm) vecProtoApproval.get(index);
                                    if(dynaValidatorForm != null){
                                        //To set the status code for the IRB protocol
                                        String protocolStatusCode = dynaValidatorForm.get("protocolStatusCode").toString();
                                        session.setAttribute("protocolStatusCode",protocolStatusCode);

                                    }
                                }
                            }
                        }
                        //To check if the module is IACUC
                        else if(routingBean.getModuleCode() == ModuleConstants.IACUC_MODULE_CODE){
                            HashMap hmIacucProtoApproval = new HashMap();
                            hmIacucProtoApproval.put("protocolNumber" , (String)session.getAttribute("IACUC_PROTOCOL_NUMBER"+session.getId()));
                            Hashtable htIacucProtoApproval =
                                    (Hashtable)webTxnBean.getResults(request, GET_IACUC_PROTOCOL_INFO , hmIacucProtoApproval);
                            Vector vecIacucProtoApproval = (Vector)htIacucProtoApproval.get(GET_IACUC_PROTOCOL_INFO);
                            //Vector vecFilteredData = new Vector();
                            if(vecIacucProtoApproval != null && vecIacucProtoApproval.size() > 0){
                                for(int index = 0; index < vecIacucProtoApproval.size(); index++){
                                    DynaValidatorForm dynaValidatorForm =
                                            (DynaValidatorForm) vecIacucProtoApproval.get(index);
                                    if(dynaValidatorForm != null){
                                        //To set the status code for the IACUC protocol
                                        String protocolStatusCode = dynaValidatorForm.get("protocolStatusCode").toString();
                                        session.setAttribute("statusCode",protocolStatusCode);

                                    }
                                }
                            }
                        }
                    }
                    //COEUSQA-1433 - Allow Recall from Routing - Start
                    RoutingBean routingBean = (RoutingBean) session.getAttribute("routingBean"+session.getId());
                     userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
                    if(routingBean.getModuleCode() == ModuleConstants.IACUC_MODULE_CODE){
                        //to release lock
                        String lockId = CoeusLiteConstants.PROTOCOL_LOCK_STR+routingBean.getModuleItemKey();
                        LockBean lockBean = getApprovalLockingBean(userInfoBean, routingBean.getModuleItemKey(), lockId, routingBean.getModuleCode(), request);
                        releaseLock(lockBean, request);
                        session.removeAttribute(CoeusLiteConstants.IACUC_PROTOCOL_LOCK);
                    }else if(routingBean.getModuleCode() == ModuleConstants.PROTOCOL_MODULE_CODE){
                        //to release lock
                        String lockId = CoeusLiteConstants.IACUC_PROTO_LOCK_STR+routingBean.getModuleItemKey();
                        LockBean lockBean = getApprovalLockingBean(userInfoBean, routingBean.getModuleItemKey(), lockId, routingBean.getModuleCode(), request);
                        releaseLock(lockBean, request);
                        session.removeAttribute(CoeusLiteConstants.PROTOCOL_LOCK);



                    }else if(routingBean.getModuleCode() == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                        //to release lock
                        LockBean lockBean = getLockingBean(userInfoBean, routingBean.getModuleItemKey(), request);
                        releaseLock(lockBean, request);
                        session.removeAttribute(CoeusLiteConstants.PROPOSAL_LOCK);
                    }
                    //COEUSQA-1433 - Allow Recall from Routing - End
                    //COEUSQA-3086-"Submit" Button Disappears when User Attempts to Re-Submit Amendment After Rejection - End
                }
                //COEUSQA-1433 - Allow Recall from Routing - Start
                else if(actionMode.equalsIgnoreCase(RECALL)){
                    userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
                    navigator = saveComments(request, coeusDynaFormList, false);
                    if(navigator != null && navigator.equals("errorPresent")){
                        return actionMapping.findForward(navigator);
                    }
                    if(coeusDynaFormList.getBeanList() == null
                            || coeusDynaFormList.getBeanList().size() == 0){
                        ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("rejectioncomments",
                                new ActionMessage("routing.commentsrequiresforRecall"));
                        saveMessages(request, actionMessages);
                        navigator = "errorPresent";
                    } else {
                        navigator = performRecallAction(request, coeusDynaFormList, response);
                        RoutingBean routingBean = (RoutingBean) session.getAttribute("routingBean"+session.getId());
                        if(routingBean.getModuleCode() == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                            //to release lock

                            LockBean lockBean = getLockingBean(userInfoBean, routingBean.getModuleItemKey(), request);
                            releaseLock(lockBean, request);
                            session.removeAttribute(CoeusLiteConstants.PROPOSAL_LOCK);
                            session.setAttribute("ACTION_CODE",MAIL_NOTIFICATION_ACTION);
                            session.setAttribute("MODULE_CODE", (""+ModuleConstants.PROPOSAL_DEV_MODULE_CODE));
                            navigator = "email";
                            return actionMapping.findForward(navigator);
                            //RequestDispatcher rd = request.getRequestDispatcher(
                            //        "getGeneralInfo.do?proposalNumber="+routingBean.getModuleItemKey());
                            //rd.forward(request,response);
                            //return null;
                        // Added for COEUSQA-3816 : Lite - Proposal routing - Locking issues - Start
                        }else if(routingBean.getModuleCode() == ModuleConstants.PROTOCOL_MODULE_CODE){
                            LockBean lockBean = getLockingBeanProto(userInfoBean, routingBean.getModuleItemKey(), request);
                            releaseLock(lockBean, request);
                        }else if(routingBean.getModuleCode() == ModuleConstants.IACUC_MODULE_CODE){
                            LockBean lockBean = getLockingBeanIacucProto(userInfoBean, routingBean.getModuleItemKey(), request);
                            releaseLock(lockBean, request);
                        }                  
                        // Added for COEUSQA-3816 : Lite - Proposal routing - Locking issues - End
                        
                    }
                    if(navigator!=null && "success".equals(navigator)){
                        RoutingBean routingBean = (RoutingBean) session.getAttribute("routingBean"+session.getId());
                        webTxnBean = new WebTxnBean();
                        //To check if the module is IRB
                        if(routingBean.getModuleCode() == ModuleConstants.PROTOCOL_MODULE_CODE){
                            HashMap hmProtoApproval = new HashMap();
                            hmProtoApproval.put("protocolNumber" , (String)session.getAttribute("PROTOCOL_NUMBER"+session.getId()));
                            Hashtable htProtoApproval =
                                    (Hashtable)webTxnBean.getResults(request, GET_IRB_PROTOCOL_INFO , hmProtoApproval);
                            Vector vecProtoApproval = (Vector)htProtoApproval.get(GET_IRB_PROTOCOL_INFO);
                            if(vecProtoApproval != null && vecProtoApproval.size() > 0){
                                for(int index = 0; index < vecProtoApproval.size(); index++){
                                    DynaValidatorForm dynaValidatorForm =
                                            (DynaValidatorForm) vecProtoApproval.get(index);
                                    if(dynaValidatorForm != null){
                                        //To set the status code for the IRB protocol
                                        String protocolStatusCode = dynaValidatorForm.get("protocolStatusCode").toString();
                                        session.setAttribute("protocolStatusCode",protocolStatusCode);
                                    }



                                }
                            }
                        }
                        //To check if the module is IACUC
                        else if(routingBean.getModuleCode() == ModuleConstants.IACUC_MODULE_CODE){
                            HashMap hmIacucProtoApproval = new HashMap();
                            hmIacucProtoApproval.put("protocolNumber" , (String)session.getAttribute("IACUC_PROTOCOL_NUMBER"+session.getId()));
                            Hashtable htIacucProtoApproval =
                                    (Hashtable)webTxnBean.getResults(request, GET_IACUC_PROTOCOL_INFO , hmIacucProtoApproval);
                            Vector vecIacucProtoApproval = (Vector)htIacucProtoApproval.get(GET_IACUC_PROTOCOL_INFO);
                            if(vecIacucProtoApproval != null && vecIacucProtoApproval.size() > 0){
                                for(int index = 0; index < vecIacucProtoApproval.size(); index++){
                                    DynaValidatorForm dynaValidatorForm =
                                            (DynaValidatorForm) vecIacucProtoApproval.get(index);
                                    if(dynaValidatorForm != null){
                                        //To set the status code for the IACUC protocol
                                        String protocolStatusCode = dynaValidatorForm.get("protocolStatusCode").toString();
                                        session.setAttribute("statusCode",protocolStatusCode);
                                    }

                                }
                            }
                        }
                    }
                    //to send mail notification
                        if(navigator!=null && "success".equals(navigator)){
                        RoutingBean routingBean = (RoutingBean) session.getAttribute("routingBean"+session.getId());
                        if(routingBean.getModuleCode() == ModuleConstants.IACUC_MODULE_CODE){
                            //to release lock
                            String lockId = CoeusLiteConstants.PROTOCOL_LOCK_STR+routingBean.getModuleItemKey();
                            LockBean lockBean = getApprovalLockingBean(userInfoBean, routingBean.getModuleItemKey(), lockId, routingBean.getModuleCode(), request);
                            releaseLock(lockBean, request);
                            session.removeAttribute(CoeusLiteConstants.IACUC_PROTOCOL_LOCK);
                            session.setAttribute("IACUC_ACTION_CODE",MAIL_NOTIFICATION_ACTION);
                            session.setAttribute("MODULE_CODE", (""+ModuleConstants.IACUC_MODULE_CODE));
                            navigator = "iacucEmail";
                            return actionMapping.findForward(navigator);
                        }else if(routingBean.getModuleCode() == ModuleConstants.PROTOCOL_MODULE_CODE){
                            //to release lock
                            String lockId = CoeusLiteConstants.IACUC_PROTO_LOCK_STR+routingBean.getModuleItemKey();
                            LockBean lockBean = getApprovalLockingBean(userInfoBean, routingBean.getModuleItemKey(), lockId, routingBean.getModuleCode(), request);
                            releaseLock(lockBean, request);
                            session.removeAttribute(CoeusLiteConstants.PROTOCOL_LOCK);
                           session.setAttribute("ACTION_CODE",MAIL_NOTIFICATION_ACTION);
                           session.setAttribute("MODULE_CODE", (""+ModuleConstants.PROTOCOL_MODULE_CODE));
                            navigator = "email";
                            return actionMapping.findForward(navigator);

                        }
                    }
                }
                //COEUSQA-1433 - Allow Recall from Routing - End

                else if(actionMode.equalsIgnoreCase(BY_PASS)){
                    saveComments(request, coeusDynaFormList, false);
                    navigator = performByPassAction(request, coeusDynaFormList, response);
                }
            }
        } else if(actionMapping.getPath().equals(APPROVE_PROPOSAL_ALL)){
            int approveAll = Integer.parseInt(request.getParameter("approveAll"));
            // COEUSQA-1497: No ability to pass routing Authority in IRB Lite
//            navigator = performApproveAction(request, coeusDynaFormList, approveAll, response);
            ArrayList beanList = (ArrayList) coeusDynaFormList.getList();
            CoeusVector newApprovers = null;
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            if(beanList != null){
                boolean approverAlreadyPresent = false;
                DynaValidatorForm dynaForm = (DynaValidatorForm) beanList.get(0);
                newApprovers = (CoeusVector) dynaForm.get("newApprovers");
            }
            navigator = performApproveAction(request, coeusDynaFormList, approveAll, newApprovers, response);
              //Used for checking final approver in the last seqential stop 
                boolean isInLastApproval = checkForLastApproval(request, coeusDynaFormList);
                if (isInLastApproval) {
                if (navigator != null && "success".equals(navigator)) {
                    RoutingBean routingBean = (RoutingBean) session.getAttribute("routingBean" + session.getId());
                    if (routingBean.getModuleCode() == ModuleConstants.IACUC_MODULE_CODE) {
                        //to release lock
                        String lockId = CoeusLiteConstants.PROTOCOL_LOCK_STR + routingBean.getModuleItemKey();
                        LockBean lockBean = getApprovalLockingBean(userInfoBean, routingBean.getModuleItemKey(), lockId, routingBean.getModuleCode(), request);
                        releaseLock(lockBean, request);
                        session.removeAttribute(CoeusLiteConstants.IACUC_PROTOCOL_LOCK);
                     // session.setAttribute("IACUC_ACTION_CODE", MAIL_NOTIFICATION_ACTION);
                     // session.setAttribute("MODULE_CODE", ("" + ModuleConstants.IACUC_MODULE_CODE));
                         session.setAttribute("IACUC_ACTION_CODE", "101"); // Action Code for Protocol Submission
                         session.setAttribute("MODULE_CODE","9");   
                      // session.setAttribute("MODULE_CODE", ModuleConstants.IACUC_MODULE_CODE+"");   
                     //  session.setAttribute("IACUC_ACTION_CODE", "101"); // Action Code for Protocol Submission
                       navigator = "iacucEmail";
                        return actionMapping.findForward(navigator);
                    } else if (routingBean.getModuleCode() == ModuleConstants.PROTOCOL_MODULE_CODE) {
                        //to release lock
                        String lockId = CoeusLiteConstants.IACUC_PROTO_LOCK_STR + routingBean.getModuleItemKey();
                        //      LockBean lockBean = getApprovalLockingBean(userInfoBean, routingBean.getModuleItemKey(), lockId, routingBean.getModuleCode(), request);
                        //       releaseLock(lockBean, request);
                        session.removeAttribute(CoeusLiteConstants.PROTOCOL_LOCK);
                        //    session.setAttribute("ACTION_CODE",MAIL_NOTIFICATION_ACTION);
                        //     session.setAttribute("MODULE_CODE", (""+ModuleConstants.PROTOCOL_MODULE_CODE));
                        session.setAttribute("MODULE_CODE", "7"); //Module Code for IRB
                        session.setAttribute("ACTION_CODE", "101");
                        navigator = EMAIL;
                        return actionMapping.findForward(navigator);
                    }
                }
            }
                
            RoutingBean routinBean = (RoutingBean) session.getAttribute("routingBean" + session.getId());
            if (routinBean.getModuleCode() == ModuleConstants.PROPOSAL_DEV_MODULE_CODE) {
                //to release lock
                LockBean lockBean = getLockingBean(userInfoBean, routinBean.getModuleItemKey(), request);
                releaseLock(lockBean, request);
                session.removeAttribute(CoeusLiteConstants.PROPOSAL_LOCK);
            }

            String oldProposalNumber = (String)session.getAttribute("proposalNumber"+session.getId());
            userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());

        } else if(actionMapping.getPath().equals(EDIT_APPROVAL_COMMENTS)){
           navigator = performEditCommentsAction(request, coeusDynaFormList);
        } else if(actionMapping.getPath().equals(REMOVE_APPROVAL_DETAILS)){
            // COEUSQA-1497: No ability to pass routing Authority in IRB Lite - Start
            ArrayList beanList = (ArrayList) coeusDynaFormList.getList();
            CoeusVector newApprovers = null;
            DynaValidatorForm dynaForm;
            if(beanList != null){
                dynaForm = (DynaValidatorForm) beanList.get(0);
                newApprovers = (CoeusVector) dynaForm.get("newApprovers");
            }
            // COEUSQA-1497: No ability to pass routing Authority in IRB Lite - End
            String type = request.getParameter("type");
            if(type != null && type.equals("comments")){
                navigator = performRemoveCommentsAction(request, coeusDynaFormList);
            } else if(type != null && type.equals("attachments")){
                navigator = performRemoveAttachmentsAction(request, coeusDynaFormList);
            }
            // COEUSQA-1497: No ability to pass routing Authority in IRB Lite - Start
             beanList = (ArrayList) coeusDynaFormList.getList();
             if(beanList != null){
                 dynaForm = (DynaValidatorForm) beanList.get(0);
                 dynaForm.set("newApprovers", newApprovers);
             }
             // COEUSQA-1497: No ability to pass routing Authority in IRB Lite - End
        } else if(actionMapping.getPath().equals(VIEW_ATTACHMENTS)){
            String index = request.getParameter("attachmentNo");
            String templateURL= viewDocument(request, coeusDynaFormList, index);
            session.setAttribute("url", templateURL);
            response.sendRedirect(request.getContextPath()+templateURL);
            return null;
        // COEUSQA-1497: No ability to pass routing Authority in IRB Lite
        } else if(ADD_APPROVER.equalsIgnoreCase(actionMapping.getPath())){
            navigator = performAddAppoverAction(request, coeusDynaFormList);
        } else if(REMOVE_APPROVER.equalsIgnoreCase(actionMapping.getPath())){
            navigator = performRemoveAppoverAction(request, coeusDynaFormList);
        }
        // COEUSQA-1497: No ability to pass routing Authority in IRB Lite
        if(navigator == null){
            return null;
        }
        return  actionMapping.findForward(navigator);
    }


    /**
     * This method will add the comments to the list
     * @param request
     * @param coeusDynaFormList
     * @param validate
     * @throws java.lang.Exception
     * @return String
     */
    private String saveAttachments(HttpServletRequest request,
            CoeusDynaFormList coeusDynaFormList, boolean validate)throws Exception {
        DynaActionForm dynaForm = coeusDynaFormList.getDynaForm(request,"approvalRoute");
        DynaActionForm dynaFormData = (DynaActionForm) coeusDynaFormList.getList().get(0);
        DynaActionForm dynaFormAttachments = (DynaActionForm) coeusDynaFormList.getInfoList().get(0);
        List lstAttachmentsData = coeusDynaFormList.getDataList();
        String navigator = SUCCESS;
        if(coeusDynaFormList.getDataList()==null){
            lstAttachmentsData = new ArrayList();
        }
//        if(dynaFormAttachments.get("description") == null
//                || ((String)dynaFormAttachments.get("description")).equals("")){
            dynaFormAttachments.set("description", dynaFormData.get("description"));
//        }
        if(dynaFormData.get("attachments") != null
                    && ((org.apache.struts.upload.FormFile) dynaFormData.get("attachments")).getFileData().length > 0){
            dynaFormAttachments.set("attachments", dynaFormData.get("attachments"));
        }
        boolean success = true;
        ActionMessages actionMessages = new ActionMessages();
        if(dynaFormAttachments.get("description") == null
                || dynaFormAttachments.get("description").equals("")){
            actionMessages.add("descriptionRequired",
                    new ActionMessage("routing.descriptionMandatory"));
            saveMessages(request, actionMessages);
            success = false;
            navigator = "errorPresent";
        } else if(((String)dynaFormAttachments.get("description")).length() > 200){
//            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("descriptionSize",
                    new ActionMessage("routing.attachmentDesc"));
            saveMessages(request, actionMessages);
            success = false;
            navigator = "errorPresent";
        }


        if(dynaFormAttachments.get("attachments") == null
                || ((org.apache.struts.upload.FormFile) dynaFormAttachments.get("attachments")).getFileData().length == 0){
        //            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("fileRequired",
                    new ActionMessage("routing.filenameMandatory"));
            saveMessages(request, actionMessages);
            navigator = "errorPresent";
           if(!success && !validate){
                actionMessages.clear();
                saveMessages(request, actionMessages);
                navigator = SUCCESS;
           }
            success = false;
        }
        
        
        if(success){
            lstAttachmentsData.add(dynaFormAttachments);
            coeusDynaFormList.setInfoListIndexed(0, dynaForm);
            coeusDynaFormList.setDataList(lstAttachmentsData);
            request.getSession().setAttribute("approvalRoutingList", coeusDynaFormList);
        }
        return navigator;
    }


    /**
     * This method will add the comments to the list
     * @param request
     * @param coeusDynaFormList
     * @param validate
     * @throws java.lang.Exception
     * @return String
     */
    private String saveComments(HttpServletRequest request,
            CoeusDynaFormList coeusDynaFormList, boolean validate)throws Exception {
        DynaActionForm dynaForm = coeusDynaFormList.getDynaForm(request,"approvalRoute");
        DynaActionForm dynaFormComments = (DynaActionForm) coeusDynaFormList.getList().get(0);
        List lstCommentsData = coeusDynaFormList.getBeanList();
        String editIndex = (String) request.getSession().getAttribute("editIndex");
        if(coeusDynaFormList.getBeanList()==null){
            lstCommentsData = new ArrayList();
        }


        //Check for Edit Comments
        if(editIndex !=null && lstCommentsData.size() > 0 && dynaFormComments.get("comments") !=null && !dynaFormComments.get("comments").equals("") ){
            DynaActionForm dynaComments = (DynaActionForm) lstCommentsData.get(Integer.parseInt(editIndex));
            dynaComments.set("comments", dynaFormComments.get("comments"));
            dynaFormComments.set("comments","");
        }else{
            if(dynaFormComments.get("comments") != null
                    && !dynaFormComments.get("comments").equals("")){
                if(((String)dynaFormComments.get("comments")).length() > 2000){
                    ActionMessages actionMessages = new ActionMessages();
                    actionMessages.add("commentsSize",
                    new ActionMessage("routing.comments"));
                    saveMessages(request, actionMessages);
                    return "errorPresent";
                }

                lstCommentsData.add(dynaFormComments);
                coeusDynaFormList.setListIndexed(0, dynaForm);
            } else if(validate){
                ActionMessages actionMessages = new ActionMessages();
                actionMessages.add("commentsRequired",
                new ActionMessage("routing.commentsRequired"));
                saveMessages(request, actionMessages);
                return "errorPresent";
            }
        }
        request.getSession().removeAttribute("editIndex");
        coeusDynaFormList.setBeanList(lstCommentsData);
        request.getSession().setAttribute("approvalRoutingList", coeusDynaFormList);
        return "success";
    }



    /**
     * This method performs Approve action
     * @param request
     * @param coeusDynaFormList
     * @param approveAll
     * @param newApprovers
     * @param response
     * @throws Exception
     * @return String
     */




//    public String performApproveAction(HttpServletRequest request,
//        CoeusDynaFormList coeusDynaFormList, int approveAll,  HttpServletResponse response)throws Exception{
    public String performApproveAction(HttpServletRequest request,
        CoeusDynaFormList coeusDynaFormList, int approveAll, CoeusVector newApprovers, HttpServletResponse response)throws Exception{
        Vector vecPropApproval = performApproveProposalAction(request);
        Vector vecFilteredApprove = prepareApproveBean(vecPropApproval, request);


        String action  = APPROVE_ACTION;
        String operation = request.getParameter("operation");
        if(PASS_ACTION.equalsIgnoreCase(operation)){
            action = PASS_ACTION;
    }


//        return updateApproveProposal(vecFilteredApprove ,APPROVE_ACTION , coeusDynaFormList, approveAll, request, response);
        return updateApproveProposal(vecFilteredApprove ,action , coeusDynaFormList, approveAll, newApprovers, request, response);
    }


    /**
     * This method is to perform the reject action
     * @param request
     * @param coeusDynaFormList
     * @param response
     * @throws Exception
     * @return String
     */
    public String performRejectAction(HttpServletRequest request,
        CoeusDynaFormList coeusDynaFormList, HttpServletResponse response)throws Exception{
        Vector vecPropApproval = performApproveProposalAction(request);
        Vector vecFilteredApprove = prepareApproveBean(vecPropApproval, request);
//        return updateApproveProposal(vecFilteredApprove ,REJECT_ACTION ,coeusDynaFormList, 0, request, response);
        return updateApproveProposal(vecFilteredApprove ,REJECT_ACTION ,coeusDynaFormList, 0, null, request, response);
    }


    /**
     * This method id to perform the bypass action
     * @param request
     * @param coeusDynaFormList
     * @param response
     * @throws Exception
     * @return String
     */
    public String performByPassAction(HttpServletRequest request,
        CoeusDynaFormList coeusDynaFormList, HttpServletResponse response)throws Exception{
        Vector vecByPassProposal = new Vector();
        HttpSession session = request.getSession();
        DynaActionForm dynaApproveProposalForm = coeusDynaFormList.getDynaForm(request,"approvalRoute");
        ApprovalRouteDisplayBean byPassBean =
            (ApprovalRouteDisplayBean)session.getAttribute("byPassApproval"+session.getId());
        dynaApproveProposalForm.set("proposalNumber",byPassBean.getProposalNumber());
        dynaApproveProposalForm.set("mapId",byPassBean.getMapId());
        dynaApproveProposalForm.set("mapNumber", new Integer(byPassBean.getMapNumber()));
        dynaApproveProposalForm.set("approverNumber", byPassBean.getApproverNumber());
        dynaApproveProposalForm.set("routingNumber",byPassBean.getRoutingNumber());
        dynaApproveProposalForm.set("levelNumber",byPassBean.getLevelNumber());
        dynaApproveProposalForm.set("updateTimeStamp",byPassBean.getUpdateTimeStamp());
        dynaApproveProposalForm.set("stopNumber",byPassBean.getStopNumber());
        dynaApproveProposalForm.set("userId",byPassBean.getUserId());
        dynaApproveProposalForm.set("avUpdateTimestamp",prepareTimeStamp().toString());
        vecByPassProposal.addElement(dynaApproveProposalForm);


//        return updateApproveProposal(vecByPassProposal ,BYPASSS_ACTION, coeusDynaFormList, 0, request, response);
        return updateApproveProposal(vecByPassProposal ,BYPASSS_ACTION, coeusDynaFormList, 0, null, request, response);
    }


    /**
     * This method gets the proposal approval for Approval status TO BE SUBMITTED
     * @param request
     * @throws Exception
     * @return Vector
     */
    public Vector getPropApprovalForStatus(HttpServletRequest request) throws Exception{
        HashMap hmPropApproval = new HashMap();
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        hmPropApproval.put("routingNumber" , (String)session.getAttribute("routingNumber"+session.getId()));
        hmPropApproval.put("userId",userId.toUpperCase());
        hmPropApproval.put("approvalStatus","T");
        hmPropApproval.put("primaryApprover",PRIMARY_APPROVER_FLAG);
        Hashtable htPropApproval =
        (Hashtable)webTxnBean.getResults(request, GET_PROP_APPROVAL_FOR_STATUS , hmPropApproval);
        Vector vecPropApproval = (Vector)htPropApproval.get(GET_PROP_APPROVAL_FOR_STATUS);
        Vector vecFilteredData = new Vector();
        if(vecPropApproval != null && vecPropApproval.size() > 0){
            for(int index = 0; index < vecPropApproval.size(); index++){
                DynaValidatorForm dynaValidatorForm =
                        (DynaValidatorForm) vecPropApproval.get(index);
                if(dynaValidatorForm != null &&
                        (dynaValidatorForm.get("approvalStatus").equals("W")
                            || dynaValidatorForm.get("approvalStatus").equals("T"))){
                    vecFilteredData.add(dynaValidatorForm);
                }


            }
        }
        return vecFilteredData;
    }




    /**
     * This method updates the comments and attachments to the database
     * @param coeusDynaFormList
     * @param request
     * @param response
     * @param vecApproveProposal
     * @param action
     * @param approveAll
     * @param newApprovers
     * @throws Exception
     * @return String
     */
//     public String updateApproveProposal(Vector vecApproveProposal, String action ,
//        CoeusDynaFormList coeusDynaFormList, int approveAll,  HttpServletRequest request,
//        HttpServletResponse response)throws Exception{
     public String updateApproveProposal(Vector vecApproveProposal, String action ,
        CoeusDynaFormList coeusDynaFormList, int approveAll, CoeusVector newApprovers, HttpServletRequest request,
        HttpServletResponse response)throws Exception{
        HttpSession session = request.getSession();
        String navigator = SUCCESS ;
        BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        RoutingUpdateTxnBean routingUpdateTxnBean = new RoutingUpdateTxnBean(userInfoBean.getUserId());
        RoutingBean routingBean = (RoutingBean) session.getAttribute("routingBean"+session.getId());


        if(vecApproveProposal!=null && vecApproveProposal.size() > 0){
                DynaValidatorForm dynaApprStatus =
                (DynaValidatorForm)vecApproveProposal.get(0);

                //server call to update the comments
                RoutingDetailsBean routingDetailsBean = new RoutingDetailsBean();
                routingDetailsBean.setAcType("I");
                routingDetailsBean.setAction(action);
                routingDetailsBean.setApproveAll(approveAll);
                routingDetailsBean.setApproverNumber(((Integer)dynaApprStatus.get("approverNumber")).intValue());
                CoeusVector cvAttachments = new CoeusVector();
                CoeusVector cvComments = new CoeusVector();
                //For comments
                if(coeusDynaFormList.getBeanList() != null
                        && coeusDynaFormList.getBeanList().size() > 0){
                    for(int count = 0; count < coeusDynaFormList.getBeanList().size(); count++){
                        DynaValidatorForm dynacomments =
                                        (DynaValidatorForm)coeusDynaFormList.getBeanList().get(count);
                        RoutingCommentsBean routingCommentsBean = new RoutingCommentsBean();
                        beanUtilsBean.copyProperties(routingCommentsBean, dynacomments);
                        routingCommentsBean.setRoutingNumber(routingBean.getRoutingNumber());
                        routingCommentsBean.setApproverNumber(((Integer)dynaApprStatus.get("approverNumber")).intValue());
                        routingCommentsBean.setLevelNumber(((Integer)dynaApprStatus.get("levelNumber")).intValue());
                        routingCommentsBean.setMapNumber(((Integer)dynaApprStatus.get("mapNumber")).intValue());
                        routingCommentsBean.setStopNumber(((Integer)dynaApprStatus.get("stopNumber")).intValue());
                        routingCommentsBean.setAcType("I");
                        cvComments.add(routingCommentsBean);
                    }
                }
                //For Attachments
                if(coeusDynaFormList.getDataList() != null
                        && coeusDynaFormList.getDataList().size() > 0){
                    for(int count = 0; count < coeusDynaFormList.getDataList().size(); count++){
                        DynaValidatorForm dynaAttachments =
                                        (DynaValidatorForm)coeusDynaFormList.getDataList().get(count);
                        RoutingAttachmentBean routingAttachmentBean = new RoutingAttachmentBean();
                        beanUtilsBean.copyProperties(routingAttachmentBean, dynaAttachments);
                        routingAttachmentBean.setRoutingNumber(routingBean.getRoutingNumber());
                        routingAttachmentBean.setApproverNumber(((Integer)dynaApprStatus.get("approverNumber")).intValue());
                        routingAttachmentBean.setLevelNumber(((Integer)dynaApprStatus.get("levelNumber")).intValue());
                        routingAttachmentBean.setMapNumber(((Integer)dynaApprStatus.get("mapNumber")).intValue());
                        routingAttachmentBean.setStopNumber(((Integer)dynaApprStatus.get("stopNumber")).intValue());
                        routingAttachmentBean.setDescription(((String)dynaAttachments.get("description")));
                        if(dynaAttachments.get("attachments")!=null){
                        routingAttachmentBean.setFileBytes(((org.apache.struts.upload.FormFile) dynaAttachments.get("attachments")).getFileData());
                        }
                        if(dynaAttachments.get("attachments")!=null){
                        routingAttachmentBean.setFileName(((org.apache.struts.upload.FormFile) dynaAttachments.get("attachments")).getFileName());
                        }
                        routingAttachmentBean.setAcType("I");
                        cvAttachments.add(routingAttachmentBean);
                    }
                }

                routingDetailsBean.setAttachments(cvAttachments);
                routingDetailsBean.setComments(cvComments);
                routingDetailsBean.setLevelNumber(((Integer)dynaApprStatus.get("levelNumber")).intValue());
                routingDetailsBean.setMapNumber(((Integer)dynaApprStatus.get("mapNumber")).intValue());
                if(((String)dynaApprStatus.get("primaryApproverFlag")).equals("Y")){
                    routingDetailsBean.setPrimaryApproverFlag(true);
                }
                routingDetailsBean.setRoutingNumber(routingBean.getRoutingNumber());
                routingDetailsBean.setStopNumber(((Integer)dynaApprStatus.get("stopNumber")).intValue());
                routingDetailsBean.setUserId((String)dynaApprStatus.get("userId"));


//                String approveSuccess = routingUpdateTxnBean.updRoutingApprove(null, routingDetailsBean, routingBean).toString();
                 String approveSuccess = routingUpdateTxnBean.updRoutingApprove(newApprovers, routingDetailsBean, routingBean).toString();


//              //server call to update the APPROVE_STATUS
                if(routingBean.getModuleCode() == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                    navigator = updateProposalStatus(approveSuccess, request, response);












                    // Update child proposal status to the DB
                    updateChildStatus(request,routingBean.getModuleItemKey());


                } else if(routingBean.getModuleCode() == ModuleConstants.PROTOCOL_MODULE_CODE){
                    navigator = updateProtocolStatus(approveSuccess, request, response);









                }else if(routingBean.getModuleCode() == ModuleConstants.IACUC_MODULE_CODE){
                    // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - Start
//                    navigator = updateIACUCProtocolStatus(approveSuccess, request);
                    navigator = updateIACUCProtocolStatus(approveSuccess, request, response);




                    // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - End
                }



        }
        return navigator;
    }


    /**
     * This method updates the proposal status in osp$eps_proposal
     * @param request
     * @param response
     * @param approvalSuccess
     * @throws Exception
     * @return String
     */
    public String updateProposalStatus(String approvalSuccess,
        HttpServletRequest request, HttpServletResponse response) throws Exception{
        String navigator = SUCCESS ;
        int  approvalValue = Integer.parseInt(approvalSuccess);
        HttpSession session = request.getSession();
        //Added For the Case # COEUSQA-1679-Modification for final flag indicator -start
        String proposalInhierarchy = (String)session.getAttribute("propInHierarchy");
        String proposalNarrativeStatus = (String)session.getAttribute("proposalNarrativeStatus");
        //Added For the Case # COEUSQA-1679-Modification for final flag indicator -end
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmProposalStatus = new HashMap();
        hmProposalStatus.put("proposalNumber", session.getAttribute("proposalNumber"+session.getId()));
        if(approvalValue == 3 ){//Proposal has been approved at last stop.  User does not have submit right
            hmProposalStatus.put(CREATION_STATUS_CODE, new Integer(4));
            webTxnBean.getResults(request , UPDATE_PROPOSAL_STATUS , hmProposalStatus);
        }
        else if(approvalValue == 2 || approvalValue == 5) {
            //update status to approved, and show message to submit through coeus application
            hmProposalStatus.put(CREATION_STATUS_CODE, new Integer(4));
            webTxnBean.getResults(request , UPDATE_PROPOSAL_STATUS , hmProposalStatus);
            // Perform submit to Sponsor action
            //Added For the Case # COEUSQA-1679-Modification for final flag indicator -start
            if(!"Y".equalsIgnoreCase(proposalInhierarchy)){
                session.setAttribute("statusCode", approvalValue+"");
                //commented and added for COEUSQA-2984 : Statuses in special review - start
                //If status of narrative is InComplete then it should not allow for submit sponsor action
                // I - InComplete
                // C - Complete
                //if(!"C".equalsIgnoreCase(proposalNarrativeStatus)){
                if("I".equalsIgnoreCase(proposalNarrativeStatus)){
                    return "cannotPerformSubmitSponsor";
                }else{
                    return "submitSponsor";
                }
                //added for COEUSQA-2984 : Statuses in special review - end
            }else{
                //Added For the Case # COEUSQA-1679-Modification for final flag indicator -end
                session.setAttribute("statusCode", approvalValue+"");
                return "submitSponsor";
                //Added For the Case # COEUSQA-1679-Modification for final flag indicator -start
            }
            //Added For the Case # COEUSQA-1679-Modification for final flag indicator -end
        }
        else if(approvalValue == 4) {
            //update status to submitted.
            hmProposalStatus.put(CREATION_STATUS_CODE, new Integer(5));
            webTxnBean.getResults(request , UPDATE_PROPOSAL_STATUS , hmProposalStatus);
        }
        return navigator;
    }
     /**
     * This method returns the list of users present in the approval cycle
     * @param request
     * @throws Exception
     * @return Vector
     */
    public Vector performApproveProposalAction(HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        String routingNumber = (String)session.getAttribute("routingNumber"+session.getId());
        Vector vecPropApprovalMaps = getProposalApprovalMaps(routingNumber, request);
        HashMap hmData = new HashMap();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        Vector vecPropApprovalStatus = null ;
        Vector vecMap = null ;
        if(vecPropApprovalMaps!=null && vecPropApprovalMaps.size() > 0){
            vecMap = new Vector();
            for(int index = 0 ; index < vecPropApprovalMaps.size() ; index ++){
                DynaBean dynaPropApprovalMaps = (DynaBean)vecPropApprovalMaps.get(index);
                hmData.put("routingNumber" , routingNumber);
                hmData.put("mapNumber" , dynaPropApprovalMaps.get("mapNumber") );
                hmData.put("userId" , userId );
                vecPropApprovalStatus = getProposalApprovalStatus(hmData, request);
                vecMap.add(vecPropApprovalStatus);
            }
        }
        return vecMap ;
    }




    /**
     * This method is to get the Approval Maps for the particular proposalNumber
     * @param routingNumber
     * @param request
     * @throws Exception
     * @return Vector of dynaBeans
     */
    public Vector getProposalApprovalMaps(String routingNumber, HttpServletRequest request) throws Exception{
        HashMap hmData = new HashMap();
        hmData.put("routingNumber",routingNumber);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htPropApprovalMaps =
        (Hashtable)webTxnBean.getResults(request,GET_PROP_APPROVAL_MAPS,hmData);
        Vector vecPropApprovalMaps =
        (Vector)htPropApprovalMaps.get(GET_PROP_APPROVAL_MAPS);
        return vecPropApprovalMaps ;
    }


    /**
     * This method is to get the Proposal Approval Status
     * @param request
     * @param hmPropStatus
     * @throws Exception
     * @return Vector of dynaBeans
     */
    public Vector getProposalApprovalStatus(HashMap hmPropStatus, HttpServletRequest request)throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htPropApprovalStatus =
        (Hashtable)webTxnBean.getResults(request,GET_PROP_APPROVAL_STATUS,hmPropStatus);
        Vector vecPropApprovalStatus =
        (Vector)htPropApprovalStatus.get(GET_PROP_APPROVAL_STATUS);
        return vecPropApprovalStatus ;
    }


    /**
     * This method returns the list of users undergoes approval cycle
     * @param vecApproveProposal
     * @param request
     * @throws java.lang.Exception
     * @return Vector
     */
    private Vector prepareApproveBean(Vector vecApproveProposal, HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        String dynaUser = null;
        String dynaStatus = null;
        String userId = ((UserInfoBean)session.getAttribute("user"+session.getId())).getUserId();
        Vector vecApproveFilterd= new Vector();
        Vector routedPropsal = new Vector();
        if(vecApproveProposal!=null && vecApproveProposal.size() > 0){
            for(int index = 0 ;index < vecApproveProposal.size();index++ ){
                Vector vecApproveMap = (Vector)vecApproveProposal.get(index);
                if(vecApproveMap != null && vecApproveMap.size() > 0){
                    for(int aIndex = 0; aIndex < vecApproveMap.size(); aIndex ++){
                        DynaValidatorForm dynaForm = (DynaValidatorForm)vecApproveMap.get(aIndex);
                        dynaUser = (String)dynaForm.get("userId");
                        dynaStatus = (String)dynaForm.get("approvalStatus");
                        if(userId.equalsIgnoreCase(dynaUser) && WAITING_FOR_APPROVAL.equalsIgnoreCase(dynaStatus)){
                        vecApproveFilterd.add(dynaForm);
                        }
                    }
                }


            }
        }

        return vecApproveFilterd;
    }


    /**
     * To edit the particular comment from the list
     * @param request
     * @param coeusDynaFormList
     * @throws java.lang.Exception
     * @return String
     */
    private String performEditCommentsAction(HttpServletRequest request,
            CoeusDynaFormList coeusDynaFormList)throws Exception{
        String editIndex = request.getParameter("editIndex");
        DynaActionForm dynaFormComments = (DynaActionForm) coeusDynaFormList.getList().get(0);
        List lstCommentsData = coeusDynaFormList.getBeanList();
        if(coeusDynaFormList.getBeanList()==null){
            lstCommentsData = new ArrayList();
        }
        if(editIndex !=null && lstCommentsData !=null && lstCommentsData.size() >0 ){
                DynaActionForm dynaCommentsForm = (DynaActionForm) lstCommentsData.get(Integer.parseInt(editIndex));
                dynaFormComments.set("comments", dynaCommentsForm.get("comments"));
        }
        request.getSession().setAttribute("editIndex",editIndex);
        return "editComments";
    }


    /**
     * To remove the particular comment from the list
     * @param request
     * @param coeusDynaFormList
     * @throws java.lang.Exception
     * @return String
     */
      private String performRemoveCommentsAction(HttpServletRequest request,
            CoeusDynaFormList coeusDynaFormList)throws Exception{
        String removeIndex = request.getParameter("removeIndex");
        List lstCommentsData = coeusDynaFormList.getBeanList();
        if(coeusDynaFormList.getBeanList()==null){
            lstCommentsData = new ArrayList();
        }
        if(removeIndex !=null && lstCommentsData !=null && lstCommentsData.size() >0 ){
            lstCommentsData.remove(Integer.parseInt(removeIndex));
        }

        return "removeDetails";
    }


    /**
     * To remove the particular attachment from the list
     * @param request
     * @param coeusDynaFormList
     * @throws java.lang.Exception
     * @return String
     */
      private String performRemoveAttachmentsAction(HttpServletRequest request,
            CoeusDynaFormList coeusDynaFormList)throws Exception{
        String removeIndex = request.getParameter("removeIndex");
        List lstAttachmentsData = coeusDynaFormList.getDataList();
        if(coeusDynaFormList.getDataList()==null){
            lstAttachmentsData = new ArrayList();
        }
        if(removeIndex !=null && lstAttachmentsData !=null && lstAttachmentsData.size() >0 ){
            lstAttachmentsData.remove(Integer.parseInt(removeIndex));
        }

        return "removeDetails";
    }


    /**
     * To view the attached document
     * @param request
     * @param coeusDynaFormList
     * @param index index of document to be viewed
     * @throws java.lang.Exception
     * @return String
     */
    private String viewDocument(HttpServletRequest request,
            CoeusDynaFormList coeusDynaFormList, String index) throws Exception{
        DocumentBean documentBean = new DocumentBean();
        Map map = new HashMap();
        RoutingAttachmentBean routingAttachmentBean = new RoutingAttachmentBean();
        if(coeusDynaFormList.getDataList() != null && coeusDynaFormList.getDataList().size() > 0){
            DynaActionForm dynaActionForm = (DynaActionForm)coeusDynaFormList.getDataList().get(Integer.parseInt(index));
            routingAttachmentBean.setFileBytes(((org.apache.struts.upload.FormFile) dynaActionForm.get("attachments")).getFileData());
            routingAttachmentBean.setFileName(((org.apache.struts.upload.FormFile) dynaActionForm.get("attachments")).getFileName());
        }
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.routing.RoutingAttachmentsReader");
        map.put("FUNCTION_TYPE","SHOW_ROUTING_ATTACHMENT");
        map.put("DATA", routingAttachmentBean);
        map.put("ATTACHMENT_SAVED", "N");
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


    /**
     * Update the irb protocol submission status
     * @param approvalSuccess
     * @param request
     * @param response
     * @throws java.lang.Exception
     * @return String
     */
    private String updateProtocolStatus(String approvalSuccess,
            HttpServletRequest request, HttpServletResponse response) throws Exception{
        String navigator = SUCCESS ;
        int  approvalValue = Integer.parseInt(approvalSuccess);
        if(approvalValue != 5 && approvalValue != 3){
            return navigator;
        }
        edu.mit.coeus.irb.bean.SubmissionDetailsTxnBean txnBean = new edu.mit.coeus.irb.bean.SubmissionDetailsTxnBean();
        RoutingBean routingBean = (RoutingBean)
        request.getSession().getAttribute("routingBean"+request.getSession().getId());
        routingBean = (routingBean == null)? new RoutingBean() : routingBean;
        Vector vecSubmissionDetails = txnBean.getDataSubmissionDetails(routingBean.getModuleItemKey());
        edu.mit.coeus.irb.bean.ProtocolSubmissionInfoBean protocolSubmissionInfoBean = null;
        boolean protSubmissionDetailsFound = false;
        int maxSubmissionNo = 0;
        edu.mit.coeus.irb.bean.ProtocolSubmissionInfoBean finalProtocolSubmissionInfoBean = null;
        if(vecSubmissionDetails!=null){
            for(int i=0; i<vecSubmissionDetails.size(); i++){
                protocolSubmissionInfoBean = (edu.mit.coeus.irb.bean.ProtocolSubmissionInfoBean)vecSubmissionDetails.get(i);
                if(protocolSubmissionInfoBean.getProtocolNumber().equals(routingBean.getModuleItemKey()) &&
                        protocolSubmissionInfoBean.getSequenceNumber() == routingBean.getModuleItemKeySequence()){
                    if(protocolSubmissionInfoBean.getSubmissionNumber()>maxSubmissionNo){
                        maxSubmissionNo = protocolSubmissionInfoBean.getSubmissionNumber();
                        finalProtocolSubmissionInfoBean = protocolSubmissionInfoBean;
                    }
                    protSubmissionDetailsFound = true;
                }
            }
        }
        if(protSubmissionDetailsFound){
            UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("user"+request.getSession().getId());
            edu.mit.coeus.irb.bean.ProtocolSubmissionUpdateTxnBean protocolSubmissionUpdateTxnBean =
                    new edu.mit.coeus.irb.bean.ProtocolSubmissionUpdateTxnBean(userInfoBean.getUserId());
            if(finalProtocolSubmissionInfoBean!=null && finalProtocolSubmissionInfoBean.getCommitteeId()!=null
                    && finalProtocolSubmissionInfoBean.getCommitteeId()!=""){
                protocolSubmissionUpdateTxnBean.updateProtoSubmissionStatus(
                        routingBean.getModuleItemKey(), routingBean.getModuleItemKeySequence(),
                        finalProtocolSubmissionInfoBean.getSubmissionNumber(), IRB_SUB_STATUS_SUBMITTED_TO_COMMITTEE);
            }else{
                protocolSubmissionUpdateTxnBean.updateProtoSubmissionStatus(
                        routingBean.getModuleItemKey(), routingBean.getModuleItemKeySequence(),
                        finalProtocolSubmissionInfoBean.getSubmissionNumber(), IRB_SUB_STATUS_PENDING);
            }
            // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - Start
            // Update the protocol status
            edu.mit.coeus.irb.bean.ProtocolUpdateTxnBean protocolUpdateTxnBean = new edu.mit.coeus.irb.bean.ProtocolUpdateTxnBean(userInfoBean.getUserId());
            boolean protocolStatusUpdated =
                    protocolUpdateTxnBean.updateProtocolStatus(routingBean.getModuleItemKey(),  routingBean.getModuleItemKeySequence(),IRB_SUBMITTED_TO_IRB_STATUS);
            request.getSession().setAttribute("MODULE_CODE", ModuleConstants.PROTOCOL_MODULE_CODE+"");
            request.getSession().setAttribute("ACTION_CODE", "101");
            // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - End
        }
        return navigator;
    }


    /**
     * Update the iacuc protocol submission status
     * @param approvalSuccess
     * @param request
     * @param response
     * @throws java.lang.Exception
     * @return String
     */
// Modified for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - Start
//    private String updateIACUCProtocolStatus(String approvalSuccess, HttpServletRequest request) throws Exception{
    private String updateIACUCProtocolStatus(String approvalSuccess, HttpServletRequest request, HttpServletResponse response) throws Exception{ // COEUSQA-2556 : End
        String navigator = SUCCESS ;
        int  approvalValue = Integer.parseInt(approvalSuccess);
        if(approvalValue != 5 && approvalValue != 3){
            return navigator;
        }
        edu.mit.coeus.iacuc.bean.SubmissionDetailsTxnBean txnBean = new edu.mit.coeus.iacuc.bean.SubmissionDetailsTxnBean() ;
        RoutingBean routingBean = (RoutingBean)
        request.getSession().getAttribute("routingBean"+request.getSession().getId());
        routingBean = (routingBean == null)? new RoutingBean() : routingBean;
        Vector vecSubmissionDetails = txnBean.getDataSubmissionDetails(routingBean.getModuleItemKey());
        edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean protocolSubmissionInfoBean = null;
        boolean protSubmissionDetailsFound = false;
        int maxSubmissionNo = 0;
        edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean finalProtocolSubmissionInfoBean = null;
        if(vecSubmissionDetails!=null){
            for(int i=0; i<vecSubmissionDetails.size(); i++){
                protocolSubmissionInfoBean = (edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean)vecSubmissionDetails.get(i);
                if(protocolSubmissionInfoBean.getProtocolNumber().equals(routingBean.getModuleItemKey()) &&
                        protocolSubmissionInfoBean.getSequenceNumber() == routingBean.getModuleItemKeySequence()){
                    if(protocolSubmissionInfoBean.getSubmissionNumber()>maxSubmissionNo){
                        maxSubmissionNo = protocolSubmissionInfoBean.getSubmissionNumber();
                        finalProtocolSubmissionInfoBean = protocolSubmissionInfoBean;
                    }
                    protSubmissionDetailsFound = true;
                }
            }
        }
        if(protSubmissionDetailsFound){
            UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("user"+request.getSession().getId());
            edu.mit.coeus.iacuc.bean.ProtocolSubmissionUpdateTxnBean protocolSubmissionUpdateTxnBean =
                    new edu.mit.coeus.iacuc.bean.ProtocolSubmissionUpdateTxnBean(userInfoBean.getUserId());
            if(finalProtocolSubmissionInfoBean!=null && finalProtocolSubmissionInfoBean.getCommitteeId()!=null
                    && finalProtocolSubmissionInfoBean.getCommitteeId()!=""){
                protocolSubmissionUpdateTxnBean.updateProtoSubmissionStatus(
                        routingBean.getModuleItemKey(), routingBean.getModuleItemKeySequence(),
                        finalProtocolSubmissionInfoBean.getSubmissionNumber(), IACUC_SUB_STATUS_SUBMITTED_TO_COMMITTEE);
            }else{
                protocolSubmissionUpdateTxnBean.updateProtoSubmissionStatus(
                        routingBean.getModuleItemKey(), routingBean.getModuleItemKeySequence(),
                        finalProtocolSubmissionInfoBean.getSubmissionNumber(), IACUC_SUB_STATUS_PENDING);
            }
            // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - Start
            // Update the protocol status
            edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean protocolUpdateTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean(userInfoBean.getUserId());
            boolean protocolStatusUpdated =
                    protocolUpdateTxnBean.updateProtocolStatus(routingBean.getModuleItemKey(),  routingBean.getModuleItemKeySequence(),IACUC_SUBMITTED_TO_IACUC_STATUS);
            request.getSession().setAttribute("MODULE_CODE", ModuleConstants.IACUC_MODULE_CODE+"");
            request.getSession().setAttribute("IACUC_ACTION_CODE", "101");
            // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - End
        }
        return navigator;
    }


    /**
     * This method adds the newApprover to newlyAddedApprovers
     */
    private void addApprover(CoeusVector newlyAddedApprovers, String newApprover, boolean alternateApprover,
            RoutingDetailsBean currentRoutingDetailBean, HttpServletRequest request){


        CoeusVector cvTempData;


        RoutingDetailsBean tempRoutingDetailsBean = new RoutingDetailsBean();


        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user"+session.getId());
        UserMaintDataTxnBean userMaintBean = new UserMaintDataTxnBean();
        String approverName = "";
        try {
            approverName = userMaintBean.getUserName(newApprover);
        } catch (Exception ex) {


        }
        // Prepare the Routing Details Bean for the newly added approver.
        RoutingDetailsBean routingDetailsBean = new RoutingDetailsBean();
        routingDetailsBean.setAcType(TypeConstants.INSERT_RECORD);
        routingDetailsBean.setUserId(newApprover);
        routingDetailsBean.setUserName(approverName);
        routingDetailsBean.setRoutingNumber(currentRoutingDetailBean.getRoutingNumber());
        routingDetailsBean.setMapNumber(currentRoutingDetailBean.getMapNumber());
        routingDetailsBean.setLevelNumber(currentRoutingDetailBean.getLevelNumber());
        routingDetailsBean.setDescription(DESCRIPTION + userInfoBean.getUserName());
        routingDetailsBean.setApprovalStatus("W");




        if( alternateApprover ){
            // If the new approver is an alternate approver


            // Filter all the new approvers for the stop number
            Equals eqMapNumber = new Equals(MAP_NUMBER_FIELD, new Integer(currentRoutingDetailBean.getMapNumber() ));
            Equals eqLevelNumber = new Equals(LEVEL_NUMBER_FIELD, new Integer(currentRoutingDetailBean.getLevelNumber() ));
            And eqMapIdAndEqLevelNumber = new And( eqMapNumber, eqLevelNumber);
            Equals eqStopNumber = new Equals("stopNumber", new Integer(currentRoutingDetailBean.getStopNumber()));
            And eqStopNumAndMapIdAndEqLevelNum = new And( eqStopNumber, eqMapIdAndEqLevelNumber);
            cvTempData = newlyAddedApprovers.filter(eqStopNumAndMapIdAndEqLevelNum);


            if(cvTempData == null){
                routingDetailsBean.setApproverNumber(1);
            } else {
                routingDetailsBean.setApproverNumber(cvTempData.size()+1);
            }


            routingDetailsBean.setPrimaryApproverFlag(false);
            routingDetailsBean.setStopNumber(currentRoutingDetailBean.getStopNumber());
            newlyAddedApprovers.add(routingDetailsBean);
            String[]  fieldNames = {"stopNumber", "approverNumber"};
            newlyAddedApprovers.sort(fieldNames, true);
        }else{
            routingDetailsBean.setStopNumber(currentRoutingDetailBean.getStopNumber()+1);
            routingDetailsBean.setPrimaryApproverFlag(true);
            routingDetailsBean.setApproverNumber(1);
            newlyAddedApprovers.addElement(routingDetailsBean);
        }
    }
    /**
     * This method adds the newApprover the existing list of approvers
     */
    private String performAddAppoverAction(HttpServletRequest request, CoeusDynaFormList coeusDynaFormList) {
        String navigator = SUCCESS;
        HttpSession session = request.getSession();


        String newApprover  = request.getParameter("newApprover");
        String alternateApprover  = request.getParameter("alternateApprover");
        String stopNumber  = request.getParameter("stopNumber");


        boolean isAlternateApprover = false;
        try{
            isAlternateApprover = Boolean.parseBoolean(alternateApprover);
        } catch(Exception ex){


        }
        ArrayList beanList = (ArrayList) coeusDynaFormList.getList();


        if(beanList != null){
            boolean approverAlreadyPresent = false;
            DynaValidatorForm dynaForm = (DynaValidatorForm) beanList.get(0);
            // Get the approvers who are already added.
            CoeusVector newApprovers = (CoeusVector) dynaForm.get("newApprovers");
            if(newApprovers == null){
                newApprovers = new CoeusVector();
            }


            RoutingBean routingBean = (RoutingBean) session.getAttribute("routingBean"+session.getId());
            Vector vecPropApproval;
            Vector vecFilteredApprover = null;
            try {
                vecPropApproval = performApproveProposalAction(request);
                if(vecPropApproval != null){
                    vecFilteredApprover = prepareApproveBean(vecPropApproval, request);
                }
            }catch (Exception ex) {
                ex.printStackTrace();
            }
            RoutingDetailsBean routingDetailsBean = new RoutingDetailsBean();
            CoeusVector cvApproversInLevel = new CoeusVector();
            if(vecFilteredApprover!=null && vecFilteredApprover.size() > 0){
                DynaValidatorForm dynaApprStatus =
                        (DynaValidatorForm)vecFilteredApprover.get(0);


                routingDetailsBean.setApproverNumber(((Integer)dynaApprStatus.get("approverNumber")).intValue());
                routingDetailsBean.setLevelNumber(((Integer)dynaApprStatus.get("levelNumber")).intValue());
                routingDetailsBean.setMapNumber(((Integer)dynaApprStatus.get("mapNumber")).intValue());


                routingDetailsBean.setRoutingNumber(routingBean.getRoutingNumber());




                RoutingTxnBean routingTxnBean = new RoutingTxnBean();
                CoeusVector cvAllApprovers = null;
                try {
                    // Get all the approvers.
                    cvAllApprovers = routingTxnBean.getRoutingDetails(routingBean.getRoutingNumber());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


                // Get all the approvers who are the in same routing level (Sequential stop)
                Equals eqMapNumber = new Equals(MAP_NUMBER_FIELD, new Integer(routingDetailsBean.getMapNumber() ));
                Equals eqLevelNumber = new Equals(LEVEL_NUMBER_FIELD, new Integer(routingDetailsBean.getLevelNumber() ));
                And eqMapIdAndEqLevelNumber = new And( eqMapNumber, eqLevelNumber);
                cvApproversInLevel = cvAllApprovers.filter(eqMapIdAndEqLevelNumber);




                try{
                    int currentMaxStopNumberForLevel = 0;
                    currentMaxStopNumberForLevel = Integer.valueOf(stopNumber);


                    if(currentMaxStopNumberForLevel == 0){
                        // if there are no approvers added for the stop,
                        if(cvApproversInLevel.size() > 0 ){
                            // Get the maximum stop number for the routing level
                            cvApproversInLevel.sort(STOP_NUMBER_FIELD, false);
                            RoutingDetailsBean tempRoutingDetailsBean = (RoutingDetailsBean)cvApproversInLevel.get(0);
                            currentMaxStopNumberForLevel = tempRoutingDetailsBean.getStopNumber();
                        }
                    }


                    routingDetailsBean.setStopNumber(currentMaxStopNumberForLevel == 0 ? 1 :currentMaxStopNumberForLevel);
                } catch(Exception e){


                }


            }


            int totalApproversInLevel = cvApproversInLevel.size();
            // Check if the newly added user is already an approver


            // Check in the approvers in the routing level
            for(int index = 0; index < totalApproversInLevel; index++){
                RoutingDetailsBean apprRoutingDetailsBean = (RoutingDetailsBean) cvApproversInLevel.get(index);
                if(apprRoutingDetailsBean.getUserId().equalsIgnoreCase(newApprover)){
                    approverAlreadyPresent = true;
                    break;
                }


            }


            // Check in the newly added approvers
            if(!approverAlreadyPresent){


                int totalNewApprovers = newApprovers.size();
                for(int index = 0; index < totalNewApprovers; index++){
                    RoutingDetailsBean apprRoutingDetailsBean = (RoutingDetailsBean) newApprovers.get(index);
                    if(apprRoutingDetailsBean.getUserId().equalsIgnoreCase(newApprover)){
                        approverAlreadyPresent = true;
                        break;
                    }
                }


            }




            if(!approverAlreadyPresent){
                // if the newly added user not already added.
                addApprover(newApprovers, newApprover, isAlternateApprover, routingDetailsBean, request);
            } else {


                String approverName = "";
                UserMaintDataTxnBean userMaintBean = new UserMaintDataTxnBean();
                try {
                    approverName = userMaintBean.getUserName(newApprover);
                } catch (Exception ex) {
                    //ex.printStackTrace();
                }
                // Add the messase "User already present as an approver in this stop"
                ActionMessages messages = new ActionMessages();
                messages.add("approverAlreadyPresent",new ActionMessage("routing.approverAlreadyPresent", approverName));
                saveMessages(request, messages);
            }


        }

        return navigator;
    }
    /**
     * This method removes the approver from  existing list of approvers .
     */
    private String performRemoveAppoverAction(HttpServletRequest request, CoeusDynaFormList coeusDynaFormList) {
        String navigator = SUCCESS;


        String stopNumber  = request.getParameter("stopNumber");
        String approverNumber  = request.getParameter("approverNumber");
        String alternateApprover  = request.getParameter("alternateApprover");
        boolean isAlternateApprover = false;
        try{
            isAlternateApprover = Boolean.parseBoolean(alternateApprover);
        } catch(Exception e){


        }
        ArrayList beanList = (ArrayList) coeusDynaFormList.getList();


        if(beanList != null){
            DynaValidatorForm dynaForm = (DynaValidatorForm) beanList.get(0);
            CoeusVector newApprovers = (CoeusVector) dynaForm.get("newApprovers");
            if(newApprovers != null && !newApprovers.isEmpty()){


                boolean canRemoveApprover = true;
                if(!isAlternateApprover){
                    // if the user to be removed is a primary approver
                    // get all the approvers in the stop
                    Equals eqStopNumber = new Equals("stopNumber", new Integer(stopNumber));
                    CoeusVector cvApproversOfStop = newApprovers.filter(eqStopNumber);
                    int size = cvApproversOfStop.size();
                    RoutingDetailsBean  tmpRoutingDetailsBean;
                    // Check if any alternate approvers are present for the user to be removed.
                    for(int index = 0; index < size; index++){
                        tmpRoutingDetailsBean = (RoutingDetailsBean) cvApproversOfStop.elementAt(index);
                        if(!tmpRoutingDetailsBean.isPrimaryApproverFlag()){
                            // if alternate approver present
                            canRemoveApprover = false;
                            break;
                        }
                    }
                }
                if(canRemoveApprover){
                    RoutingDetailsBean currentRoutingDetailBean = new RoutingDetailsBean();


                    int size = newApprovers.size();
                    boolean removed = false;
                    RoutingDetailsBean  tmpRoutingDetailsBean;
                    // Remove the user
                    for(int index = 0; index < size; index++){
                        tmpRoutingDetailsBean = (RoutingDetailsBean) newApprovers.elementAt(index);
                        if(tmpRoutingDetailsBean.getStopNumber() == (Integer.parseInt(stopNumber))
                        && tmpRoutingDetailsBean.getApproverNumber() == (Integer.parseInt(approverNumber))){
                            newApprovers.remove(index);
                            removed = true;
                            break;
                        }




                    }
                    if(removed){
                        if(isAlternateApprover ){
                            // If the removed user was an alternate approver
                            size = newApprovers.size();
                            // Get the other alternate approvers for the stop and reset the approver number
                            for(int index = 0; index < size; index++){
                                tmpRoutingDetailsBean = (RoutingDetailsBean) newApprovers.elementAt(index);


                                if((tmpRoutingDetailsBean.getStopNumber() == (Integer.parseInt(stopNumber))) &&


                                        (tmpRoutingDetailsBean.getApproverNumber() > (Integer.parseInt(approverNumber)))){
                                    tmpRoutingDetailsBean.setApproverNumber(tmpRoutingDetailsBean.getApproverNumber() -1);
                                }
                            }
                        } else {
                            // If the removed user was an primary approver
                            // Get the succeeding stops and reset the stop number
                            for(int index = 0; index < size-1; index++){
                                tmpRoutingDetailsBean = (RoutingDetailsBean) newApprovers.elementAt(index);
                                if(tmpRoutingDetailsBean.getStopNumber() > (Integer.parseInt(stopNumber))
                                ){
                                    tmpRoutingDetailsBean.setStopNumber(tmpRoutingDetailsBean.getStopNumber() -1);
                                }


                            }
                        }
                    }
                } else {
                    // Approver cannot be removed
                    ActionMessages messages = new ActionMessages();
                    messages.add("cannotRemovePrimApprover",new ActionMessage("routing.cannotRemovePrimaryApprover"));
                    saveMessages(request, messages);
                }


            }


        }


        return navigator;
    }

    //COEUSQA-1433 - Allow Recall from Routing - Start
    /**
     * This method is to perform the recall action
     * @param request
     * @param coeusDynaFormList
     * @param response
     * @throws Exception
     * @return String
     */
    public String performRecallAction(HttpServletRequest request,
        CoeusDynaFormList coeusDynaFormList, HttpServletResponse response)throws Exception{
        Vector vecPropApproval = performApproveProposalAction(request);
        Vector vecFilteredApprove = prepareRecallBean(vecPropApproval, request);
        //COEUSQA:3441 - Recalled Proposal notifications, record status changes, and inbox messages - Start
        //return updateRecallProposal(vecPropApproval, RECALL_ACTION ,coeusDynaFormList, 0, null, request, response);
        return updateRecallProposal(vecFilteredApprove, RECALL_ACTION ,coeusDynaFormList, 0, null, request, response);
        //COEUSQA:3441 - End
    }

    /**
     * This method returns the list of users undergoes approval cycle
     * @param vecApproveProposal
     * @param request
     * @throws java.lang.Exception
     * @return Vector
     */
    private Vector prepareRecallBean(Vector vecApproveProposal, HttpServletRequest request)throws Exception{





        HttpSession session = request.getSession();
        String dynaUser = null;
        String dynaStatus = null;

        String userId = ((UserInfoBean)session.getAttribute("user"+session.getId())).getUserId();
        Vector vecApproveFilterd= new Vector();
        if(vecApproveProposal!=null && vecApproveProposal.size() > 0){
            for(int index = 0 ;index < vecApproveProposal.size();index++ ){
                Vector vecApproveMap = (Vector)vecApproveProposal.get(index);
                if(vecApproveMap != null && vecApproveMap.size() > 0){
                    for(int aIndex = 0; aIndex < vecApproveMap.size(); aIndex ++){
                        DynaValidatorForm dynaForm = (DynaValidatorForm)vecApproveMap.get(aIndex);
                        dynaUser = (String)dynaForm.get("userId");
                        //COEUSQA:3441 - Recalled Proposal notifications, record status changes, and inbox messages - Start
                        dynaStatus = (String)dynaForm.get("approvalStatus"); 
                        //if(userId.equalsIgnoreCase(dynaUser)){
                        if(userId.equalsIgnoreCase(dynaUser) && WAITING_FOR_APPROVAL.equalsIgnoreCase(dynaStatus)){
                        //COEUSQA:3441 - End                        
                            vecApproveFilterd.add(dynaForm);
                        }
                    }
                }
            }
            //COEUSQA:3441 - Recalled Proposal notifications, record status changes, and inbox messages - Start
            if(vecApproveFilterd == null || vecApproveFilterd.isEmpty()){
                for(Object vecapprovalProposal : vecApproveProposal){
                    Vector vecApproveMap = (Vector)vecapprovalProposal;
                    if(vecApproveMap != null && vecApproveMap.size() > 0){
                        for(Object vecapprovalMap : vecApproveMap){
                            DynaValidatorForm dynaForm = (DynaValidatorForm)vecapprovalMap;
                            dynaStatus = (String)dynaForm.get("approvalStatus");
                            if(WAITING_FOR_APPROVAL.equalsIgnoreCase(dynaStatus)){
                                vecApproveFilterd.add(dynaForm);
                                break;
                            }
                        }
                    }
                }
            }
            //COEUSQA:3441 - End
        }
        return vecApproveFilterd;



    }

     /**
     * This method updates the comments to the database
     * @param coeusDynaFormList
     * @param request
     * @param response
     * @param vecApproveProposal
     * @param action
     * @param approveAll
     * @param newApprovers
     * @throws Exception
     * @return String
     */

















































     public String updateRecallProposal(Vector vecApproveProposal, String action ,
        CoeusDynaFormList coeusDynaFormList, int approveAll, CoeusVector newApprovers, HttpServletRequest request,
        HttpServletResponse response)throws Exception{


        HttpSession session = request.getSession();
        String navigator = SUCCESS ;
        BeanUtilsBean beanUtilsBean = new BeanUtilsBean();



        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        RoutingUpdateTxnBean routingUpdateTxnBean = new RoutingUpdateTxnBean(userInfoBean.getUserId());
        RoutingBean routingBean = (RoutingBean) session.getAttribute("routingBean"+session.getId());

        if(vecApproveProposal!=null && vecApproveProposal.size() > 0){
                //COEUSQA:3441 - Recalled Proposal notifications, record status changes, and inbox messages - Start
                //Vector vecApprovalData = (Vector)vecApproveProposal.get(0);
                //DynaValidatorForm dynaApprStatus = (DynaValidatorForm)vecApprovalData.get(0);
                DynaValidatorForm dynaApprStatus = (DynaValidatorForm)vecApproveProposal.get(0);
                //COEUSQA:3441 - End



                //server call to update the comments
                RoutingDetailsBean routingDetailsBean = new RoutingDetailsBean();
                routingDetailsBean.setAcType("I");
                routingDetailsBean.setAction(action);
                routingDetailsBean.setApproveAll(approveAll);
                routingDetailsBean.setApproverNumber(((Integer)dynaApprStatus.get("approverNumber")).intValue());
                CoeusVector cvAttachments = new CoeusVector();
                CoeusVector cvComments = new CoeusVector();
                //For comments
                if(coeusDynaFormList.getBeanList() != null
                        && coeusDynaFormList.getBeanList().size() > 0){
                    for(int count = 0; count < coeusDynaFormList.getBeanList().size(); count++){
                        DynaValidatorForm dynacomments =
                                        (DynaValidatorForm)coeusDynaFormList.getBeanList().get(count);
                        RoutingCommentsBean routingCommentsBean = new RoutingCommentsBean();
                        beanUtilsBean.copyProperties(routingCommentsBean, dynacomments);
                        routingCommentsBean.setRoutingNumber(routingBean.getRoutingNumber());
                        routingCommentsBean.setApproverNumber(((Integer)dynaApprStatus.get("approverNumber")).intValue());
                        routingCommentsBean.setLevelNumber(((Integer)dynaApprStatus.get("levelNumber")).intValue());
                        routingCommentsBean.setMapNumber(((Integer)dynaApprStatus.get("mapNumber")).intValue());
                        routingCommentsBean.setStopNumber(((Integer)dynaApprStatus.get("stopNumber")).intValue());
                        routingCommentsBean.setAcType("I");
                        cvComments.add(routingCommentsBean);












                    }
                }

                routingDetailsBean.setAttachments(cvAttachments);
                routingDetailsBean.setComments(cvComments);
                routingBean.setRoutingEndUser(userInfoBean.getUserId());
                routingDetailsBean.setLevelNumber(((Integer)dynaApprStatus.get("levelNumber")).intValue());
                routingDetailsBean.setMapNumber(((Integer)dynaApprStatus.get("mapNumber")).intValue());
                if(((String)dynaApprStatus.get("primaryApproverFlag")).equals("Y")){
                    routingDetailsBean.setPrimaryApproverFlag(true);
































                }
                routingDetailsBean.setRoutingNumber(routingBean.getRoutingNumber());
                routingDetailsBean.setStopNumber(((Integer)dynaApprStatus.get("stopNumber")).intValue());
                routingDetailsBean.setUserId(userInfoBean.getUserId());
                String approveSuccess = routingUpdateTxnBean.updRoutingRecall(newApprovers, routingDetailsBean, routingBean).toString();

                //server call to update the APPROVE_STATUS
                if(routingBean.getModuleCode() == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                    navigator = updateProposalStatus(approveSuccess, request, response);
                    // Update child proposal status to the DB
                    updateChildStatus(request,routingBean.getModuleItemKey());
                } else if(routingBean.getModuleCode() == ModuleConstants.PROTOCOL_MODULE_CODE){
                    navigator = updateProtocolStatus(approveSuccess, request, response);
                    //To update the action
                    saveIRBProtocolActions(routingBean, request);
                }else if(routingBean.getModuleCode() == ModuleConstants.IACUC_MODULE_CODE){
                    navigator = updateIACUCProtocolStatus(approveSuccess, request, response);
                    //To update the action
                    saveIACUCProtocolActions(routingBean, request);









                }
                
                //COEUSQA:3644 - Routing recall action should populate the notepad with comments - Start
                if(routingBean!=null){
                    if(routingBean.getModuleCode() == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                        if(routingDetailsBean.getComments()!=null){
                            RoutingCommentsBean routingCommentsBean = null;
                            for(Object routingComments : routingDetailsBean.getComments()){
                                routingCommentsBean = (RoutingCommentsBean)routingComments;
                                CoeusVector notepads = new CoeusVector();
                                NotepadTxnBean notepadTxnBean = new NotepadTxnBean(userInfoBean.getUserId());
                                NotepadBean notePadBean = new NotepadBean();
                                
                                notePadBean.setAcType(TypeConstants.INSERT_RECORD);
                                notePadBean.setComments(routingCommentsBean.getComments());
                                notePadBean.setProposalAwardNumber(routingBean.getModuleItemKey());
                                notePadBean.setRestrictedView(false);
                                notepads.add(notePadBean);
                                
                                notepadTxnBean.addUpdProposalDevelopmentNotepad(notepads);
                            }
                        }
                    } else if(routingBean.getModuleCode() == ModuleConstants.PROTOCOL_MODULE_CODE){
                        routingUpdateTxnBean.addProtocolNotepad(routingDetailsBean, routingBean);
                        
                    } else if(routingBean.getModuleCode() == ModuleConstants.IACUC_MODULE_CODE){
                        routingUpdateTxnBean.addIacucProtocolNotepad(routingDetailsBean, routingBean);
                    }
                }
                //COEUSQA:3644 - End

        }
        return navigator;
    }


    /**
     * This method is used to update the data for Protocol Actions
     * @param routingBean
     * @param request
     * @throws Exception
     * @return boolean success
     */
    private boolean saveIRBProtocolActions(RoutingBean routingBean , HttpServletRequest request)throws Exception{
        boolean success = false;
        int actionCode =  123; //Recalled Submission
        int submissionNo = 0;
        ActionTransaction actionTxn = new ActionTransaction(actionCode) ;
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());

        //To get submission number
        edu.mit.coeus.irb.bean.ProtocolSubmissionInfoBean protocolSubmissionInfoBean = null;
        edu.mit.coeus.irb.bean.SubmissionDetailsTxnBean txnBean = new edu.mit.coeus.irb.bean.SubmissionDetailsTxnBean();
        Vector vecSubmissionDetails = txnBean.getDataSubmissionDetails(routingBean.getModuleItemKey());
        if(vecSubmissionDetails!=null){
            for(int i=0; i<vecSubmissionDetails.size(); i++){
                protocolSubmissionInfoBean = (edu.mit.coeus.irb.bean.ProtocolSubmissionInfoBean)vecSubmissionDetails.get(i);
                if(protocolSubmissionInfoBean.getProtocolNumber().equals(routingBean.getModuleItemKey()) &&
                        protocolSubmissionInfoBean.getSequenceNumber() == routingBean.getModuleItemKeySequence()){
                    submissionNo = protocolSubmissionInfoBean.getSubmissionNumber();















                }
            }
        }
        if (actionTxn.logStatusChangeToProtocolAction(routingBean.getModuleItemKey(),routingBean.getModuleItemKeySequence(),
                submissionNo , userInfoBean.getUserId()) != -1) {// status is Recalled Submission
            success = true ;









































        }
        return  success;
    }

    /**
     * This method is used to update the data for Protocol Actions
     * @param routingBean
     * @param request
     * @throws Exception
     * @return boolean success
     */
    private boolean saveIACUCProtocolActions(RoutingBean routingBean , HttpServletRequest request)throws Exception{
        boolean success = false;
        int actionCode =  123; //Recalled Submission
        int submissionNo = 0;
        edu.mit.coeus.iacuc.bean.ActionTransaction actionTxn = new edu.mit.coeus.iacuc.bean.ActionTransaction(actionCode) ;


























        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());

        //To get submission number
        edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean protocolSubmissionInfoBean = null;
        edu.mit.coeus.iacuc.bean.SubmissionDetailsTxnBean txnBean = new edu.mit.coeus.iacuc.bean.SubmissionDetailsTxnBean();
        Vector vecSubmissionDetails = txnBean.getDataSubmissionDetails(routingBean.getModuleItemKey());
        if(vecSubmissionDetails!=null){
            for(int i=0; i<vecSubmissionDetails.size(); i++){
                protocolSubmissionInfoBean = (edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean)vecSubmissionDetails.get(i);
                if(protocolSubmissionInfoBean.getProtocolNumber().equals(routingBean.getModuleItemKey()) &&
                        protocolSubmissionInfoBean.getSequenceNumber() == routingBean.getModuleItemKeySequence()){
                    submissionNo = protocolSubmissionInfoBean.getSubmissionNumber();




















                }
            }
        }
        if (actionTxn.logStatusChangeToProtocolAction(routingBean.getModuleItemKey(),routingBean.getModuleItemKeySequence(),
                submissionNo , userInfoBean.getUserId()) != -1) {// status is Recalled Submission
            success = true ;





























































        }
        return  success;



    }
















    /**
     * This method is used to validate whether the submission in in last stage of Approval
     * @param routingBean
     * @param request
     * @throws Exception
     * @return boolean success




     */
     public boolean checkForLastStageApproval(HttpServletRequest request,
             CoeusDynaFormList coeusDynaFormList) throws Exception{
        boolean returnFlag = false;
        //counter to match the vector size

        HttpSession session = request.getSession();
        String routingNumber = (String)session.getAttribute("routingNumber"+session.getId());


        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        HashMap hmData = new HashMap();
        //counter to check for last map in case of multiple maps
        int stopDelimiter = 0;
        Vector vctApprovalMaps = getProposalApprovalMaps(routingNumber, request);
        if(vctApprovalMaps == null){
            vctApprovalMaps = new Vector();
        }
        //to check whether the proposal/protocol is in the last approval stage in last sequential stop
        for(Object routingData:vctApprovalMaps){
            DynaBean dynaPropApprovalMaps = (DynaBean)routingData;
            //fetch the routing map details
            int counter = 0;
            stopDelimiter++;
            hmData.put("routingNumber" , routingNumber);
            hmData.put("mapNumber" , dynaPropApprovalMaps.get("mapNumber") );
            hmData.put("userId" , userId );
            Vector cvRoutingMap = getProposalApprovalStatus(hmData, request);
            for(Object routingMapData:cvRoutingMap){
                ++counter;
                DynaBean dynaRoutingDetailsBean = (DynaBean)routingMapData;
                //to check for last map in case of multiple maps
                if(vctApprovalMaps.size() == stopDelimiter){
                    if(cvRoutingMap.size()>1){
                        if((counter+1) == cvRoutingMap.size()){
                            //fetch the approval status for the approval stage just above the last approval
                            String approvalStatus = (String)dynaRoutingDetailsBean.get("approvalStatus");
                            if(approvalStatus.equals("W") || approvalStatus.equals("T")){
                                returnFlag = false;
                            }else{
                                returnFlag = true;
                            }
                        }
                    }else{
                        //fetch the approval status for the approval stage just above the last approval
                        String approvalStatus = (String)dynaRoutingDetailsBean.get("approvalStatus");
                        if(approvalStatus.equals("W") || approvalStatus.equals("T")){
                            returnFlag = true;
                        }else{
                            returnFlag = false;
                        }
                    }
                }
            }

                            }
        return returnFlag;
    }

     /** Manufacture the LockBean based on the parameter passed by the specific module
      *say, Propsoal, Protocol, Budget etc.
      *@param UserInfoBean, Proposal number
      *@returns LockBean
      *@throws Exception
      */
     protected LockBean getApprovalLockingBean(UserInfoBean userInfoBean, String protocolNumber, String lockId, int moduleCode, HttpServletRequest request) throws Exception{
         LockBean lockBean = new LockBean();
         lockBean.setLockId(lockId);
         String mode = (String)request.getSession().getAttribute(CoeusLiteConstants.MODE_DETAILS+request.getSession().getId());
         mode = getMode(mode);
         lockBean.setMode(mode);
         lockBean.setModuleKey(""+moduleCode);
         lockBean.setModuleNumber(protocolNumber);
         lockBean.setModuleUnitNumber(userInfoBean.getUnitNumber());
         lockBean.setUnitNumber(UNIT_NUMBER);
         lockBean.setUserId(userInfoBean.getUserId());
         lockBean.setUserName(userInfoBean.getUserName());
         lockBean.setSessionId(request.getSession().getId());
         return lockBean;
     }
    //COEUSQA-1433 - Allow Recall from Routing - End
//to lock proposal
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
                    LockBean serverDataBean = getLockedData(CoeusLiteConstants.PROP_ROUTING_LOCK_STR+lockBean.getModuleNumber(), request);
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
            LockBean serverDataBean = getLockedData(CoeusLiteConstants.PROP_ROUTING_LOCK_STR+lockBean.getModuleNumber(), request);
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
    protected LockBean getLockingBean(UserInfoBean userInfoBean, String proposalNumber, HttpServletRequest request) throws Exception{
        LockBean lockBean = new LockBean();
        lockBean.setLockId(CoeusLiteConstants.PROP_ROUTING_LOCK_STR+proposalNumber);
        String mode = (String)request.getSession().getAttribute("mode"+request.getSession().getId());
        mode =getMode(mode);
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
    private void showLockingMessage(String moduleNumber, HttpServletRequest request) throws Exception{
        String lockId = CoeusLiteConstants.PROP_ROUTING_LOCK_STR+moduleNumber;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        LockBean serverLockedBean = getLockedData(lockId,request);
        if(serverLockedBean!= null){
            serverLockedBean.setModuleKey("Proposal Routing");
            serverLockedBean.setModuleNumber(moduleNumber);
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

// to lock IACUC protocol
 protected boolean prepareLockProtoIACUC(String protocolNumber, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        boolean isSuccess = true;
//        WebTxnBean webTxnBean = new WebTxnBean();
        String mode = (String)session.getAttribute("mode"+session.getId());
        SearchModuleBean moduleBean = (SearchModuleBean)session.getAttribute(CoeusLiteConstants.PROTOCOL_SEARCH_ACTION+session.getId());
        LockBean lockBean = null;
        LockBean sessionLockBean = (LockBean)session.getAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());

        // If the action is from search window
        if(moduleBean!= null && !moduleBean.getOldModuleNumber().equals(EMPTY_STRING)){
            if(!moduleBean.getModuleNumber().equals(moduleBean.getOldModuleNumber())){
                // If the existing protocol number is not in DISPLAY MODE, release the lcok
                if(!moduleBean.getOldMode().equals(CoeusLiteConstants.DISPLAY_MODE)){
                    lockBean = getLockingBeanIacucProto(userInfoBean, moduleBean.getOldModuleNumber(), request);
                    LockBean serverDataBean = getLockedData(CoeusLiteConstants.IACUC_PROTO_ROUTING_LOCK_STR+lockBean.getModuleNumber(), request);
                    if(serverDataBean!=null && lockBean.getSessionId().equals(serverDataBean.getSessionId())) {
                        releaseLock(lockBean,request);
                        session.removeAttribute(CoeusLiteConstants.PROTOCOL_SEARCH_ACTION+session.getId());
                        session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(),
                            new Boolean(false));
                    }
                }
            }
            moduleBean.setMode(getMode(mode));
            // If the current Protocol number is in MODIFY MODE then lock it
                if(!moduleBean.getMode().equals(CoeusLiteConstants.DISPLAY_MODE)){
                    lockBean = getLockingBeanIacucProto(userInfoBean,moduleBean.getModuleNumber(),request);
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
                    LockBean serverDataBean = getLockedData(CoeusLiteConstants.IACUC_PROTO_ROUTING_LOCK_STR+lockBean.getModuleNumber(), request);
                    // If the current protocol is locked by other user, then show the message
                    // else lock it
                    if(!isLocked ){
                        /** Check if the same record is locked or not. If not
                         *then only show the message else discard it
                         */
                        if(!isSessionRowLocked || (serverDataBean!= null && !serverDataBean.getSessionId().equals(lockBean.getSessionId()))){
                            showLockingMessage(lockBean.getModuleNumber(), request);
                            isSuccess = false;
                        }// End if for lockeed record in the session
                    }else{// If the record is not locked then go ahead and lock it
                        lockModule(lockBean,request);
                        session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(),
                            new Boolean(true));
                    }
                }

        }else{
            // Protocol opened from list
            lockBean = getLockingBeanIacucProto(userInfoBean, protocolNumber,request);
	    LockBean serverDataBean = getLockedData(CoeusLiteConstants.IACUC_PROTO_ROUTING_LOCK_STR+lockBean.getModuleNumber(), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            if(isLockExists) {
                if(serverDataBean!=null && !lockBean.getSessionId().equals(serverDataBean.getSessionId())) {
                    isLockExists = false;
                }
            }
            /** check whether lock exists or not. If not and the mode of the
             *protocol is not display then lock the protocol else show the message
             */
            if(isLockExists && !lockBean.getMode().equals(CoeusLiteConstants.DISPLAY_MODE)){
                lockModule(lockBean,request);
                session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(),
                            new Boolean(true));
            }else{
                if(sessionLockBean == null && !lockBean.getMode().equals(CoeusLiteConstants.DISPLAY_MODE)){
                    showLockingMessageIacucProto(lockBean.getModuleNumber(), request);
                    isSuccess = false;
                } else if(sessionLockBean!=null && serverDataBean!=null) {
                    if(!lockBean.getSessionId().equals(serverDataBean.getSessionId())) {
                        showLockingMessageIacucProto(lockBean.getModuleNumber(),request);
                        isSuccess = false;
                    }
                }
            }
        }
        session.removeAttribute(CoeusLiteConstants.PROTOCOL_SEARCH_ACTION+session.getId());
        return isSuccess;
    }
   protected LockBean getLockingBeanIacucProto(UserInfoBean userInfoBean, String protocolNumber, HttpServletRequest request) throws Exception{
        LockBean lockBean = new LockBean();
        lockBean.setLockId(CoeusLiteConstants.IACUC_PROTO_ROUTING_LOCK_STR +protocolNumber);
        String mode = (String)request.getSession().getAttribute(CoeusLiteConstants.MODE_DETAILS+request.getSession().getId());
        mode = getMode(mode);
        lockBean.setMode(mode);
        lockBean.setModuleKey(CoeusLiteConstants.IACUC_PROTOCOL_ROUTING_MODULE);
        lockBean.setModuleNumber(protocolNumber);
        lockBean.setModuleUnitNumber(userInfoBean.getUnitNumber());
        lockBean.setUnitNumber(UNIT_NUMBER);
        lockBean.setUserId(userInfoBean.getUserId());
        lockBean.setUserName(userInfoBean.getUserName());
        lockBean.setSessionId(request.getSession().getId());
        return lockBean;
    }
 protected void showLockingMessageIacucProto(String moduleNumber, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        String lockId = CoeusLiteConstants.IACUC_PROTO_ROUTING_LOCK_STR+moduleNumber;
        LockBean serverLockedBean = getLockedData(lockId,request);

        if(serverLockedBean!= null){
            serverLockedBean.setModuleKey(CoeusLiteConstants.IACUC_PROTOCOL_ROUTING_MODULE);
            serverLockedBean.setModuleNumber(moduleNumber);
            //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-start
            UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("user"+request.getSession().getId());
            String loggedInUserId = userInfoBean.getUserId();
            String lockUserId = serverLockedBean.getUserId();
            String lockUserName = EMPTY_STRING;
            String acqLock = "acquired_lock";
            ActionMessages messages = new ActionMessages();
            lockUserName =  viewRestrictionOfUser(loggedInUserId,lockUserId);
            //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-end
            messages.add("acqLock", new ActionMessage(acqLock,
                lockUserName,serverLockedBean.getModuleKey(),
                    serverLockedBean.getModuleNumber()));
            //End
            saveMessages(request, messages);
            session.removeAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());
        }
    }
     protected String viewRestrictionOfUser(String loggedInUserId, String lockUserId) throws DBException, CoeusException {
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        UserMaintDataTxnBean userTxnBean = new UserMaintDataTxnBean();
        String currenatLockUserName = userTxnBean.getUserName(lockUserId);
        //DISPLAY_LOCKNAME_IRB is Taking from the parameter table
        String displayLockName = coeusFunctions.getParameterValue(CoeusConstants.DISPLAY_LOCKNAME_IACUC);
        // If the value for DISPLAY_LOCKNAME_PROP_BUDGET is set 'Y' or if loginned user has lock,ot will display the lock user name
            if(displayLockName != null && "Y".equalsIgnoreCase(displayLockName.trim()) || lockUserId.equalsIgnoreCase(loggedInUserId)){
                currenatLockUserName = currenatLockUserName;
            }else{
                currenatLockUserName = CoeusConstants.lockedUsername;
            }
        return currenatLockUserName;
    }
  protected String getMode(String mode) throws Exception{
        if(mode!= null && !mode.equals(EMPTY_STRING)){
            if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
                mode = CoeusLiteConstants.DISPLAY_MODE;
            }
        }else{
            mode = CoeusLiteConstants.MODIFY_MODE;
        }
        return mode;
    }

  protected boolean prepareLockProto(String protocolNumber, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        boolean isSuccess = true;
        WebTxnBean webTxnBean = new WebTxnBean();
        String mode = (String)session.getAttribute("mode"+session.getId());
        SearchModuleBean moduleBean = (SearchModuleBean)session.getAttribute(CoeusLiteConstants.PROTOCOL_SEARCH_ACTION+session.getId());
        LockBean lockBean = null;
        LockBean sessionLockBean = (LockBean)session.getAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());

        // If the action is from search window
        if(moduleBean!= null && !moduleBean.getOldModuleNumber().equals(EMPTY_STRING)){
            if(!moduleBean.getModuleNumber().equals(moduleBean.getOldModuleNumber())){
                // If the existing protocol number is not in DISPLAY MODE, release the lcok
                if(!moduleBean.getOldMode().equals(CoeusLiteConstants.DISPLAY_MODE)){
                    lockBean = getLockingBeanProto(userInfoBean, moduleBean.getOldModuleNumber(), request);
                    LockBean serverDataBean = getLockedData(CoeusLiteConstants.PROTO_ROUTING_LOCK_STR+lockBean.getModuleNumber(), request);
                    if(serverDataBean!=null && lockBean.getSessionId().equals(serverDataBean.getSessionId())) {
                        releaseLock(lockBean,request);
                        session.removeAttribute(CoeusLiteConstants.PROTOCOL_SEARCH_ACTION+session.getId());
                        session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(),
                            new Boolean(false));
                    }
                }
            }
            moduleBean.setMode(getMode(mode));
            // If the current Protocol number is in MODIFY MODE then lock it
                if(!moduleBean.getMode().equals(CoeusLiteConstants.DISPLAY_MODE)){
                    lockBean = getLockingBeanProto(userInfoBean,moduleBean.getModuleNumber(),request);
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
                    LockBean serverDataBean = getLockedData(CoeusLiteConstants.PROTO_ROUTING_LOCK_STR+lockBean.getModuleNumber(), request);
                    // If the current protocol is locked by other user, then show the message
                    // else lock it
                    if(!isLocked ){
                        /** Check if the same record is locked or not. If not
                         *then only show the message else discard it
                         */
                        if(!isSessionRowLocked || (serverDataBean!= null && !serverDataBean.getSessionId().equals(lockBean.getSessionId()))){
                            showLockingMessageProto(lockBean.getModuleNumber(), request);
                            isSuccess = false;
                        }// End if for lockeed record in the session
                    }else{// If the record is not locked then go ahead and lock it
                        lockModule(lockBean,request);
                        session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(),
                            new Boolean(true));
                    }
                }

        }else{
            // Protocol opened from list
            lockBean = getLockingBeanProto(userInfoBean, protocolNumber,request);
	    LockBean serverDataBean = getLockedData(CoeusLiteConstants.PROTO_ROUTING_LOCK_STR+lockBean.getModuleNumber(), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            if(isLockExists) {
                if(serverDataBean!=null && !lockBean.getSessionId().equals(serverDataBean.getSessionId())) {
                    isLockExists = false;
                }
            }
            /** check whether lock exists or not. If not and the mode of the
             *protocol is not display then lock the protocol else show the message
             */
            if(isLockExists && !lockBean.getMode().equals(CoeusLiteConstants.DISPLAY_MODE)){
                lockModule(lockBean,request);
                session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(),
                            new Boolean(true));
            }else{
                if(sessionLockBean == null && !lockBean.getMode().equals(CoeusLiteConstants.DISPLAY_MODE)){
                    showLockingMessageProto(lockBean.getModuleNumber(), request);
                    isSuccess = false;
                } else if(sessionLockBean!=null && serverDataBean!=null) {
                    if(!lockBean.getSessionId().equals(serverDataBean.getSessionId())) {
                        showLockingMessageProto(lockBean.getModuleNumber(),request);
                        isSuccess = false;
                    }
                }
            }
        }
        session.removeAttribute(CoeusLiteConstants.PROTOCOL_SEARCH_ACTION+session.getId());
        return isSuccess;

    }
 protected LockBean getLockingBeanProto(UserInfoBean userInfoBean, String protocolNumber, HttpServletRequest request) throws Exception{
        LockBean lockBean = new LockBean();
        lockBean.setLockId(CoeusLiteConstants.PROTO_ROUTING_LOCK_STR+protocolNumber);
        String mode = (String)request.getSession().getAttribute(CoeusLiteConstants.MODE_DETAILS+request.getSession().getId());
        mode = getMode(mode);
        lockBean.setMode(mode);
        lockBean.setModuleKey(CoeusLiteConstants.PROTOCOL_ROUTING_MODULE);
        lockBean.setModuleNumber(protocolNumber);
        lockBean.setModuleUnitNumber(userInfoBean.getUnitNumber());
        lockBean.setUnitNumber(UNIT_NUMBER);
        lockBean.setUserId(userInfoBean.getUserId());
        lockBean.setUserName(userInfoBean.getUserName());
        lockBean.setSessionId(request.getSession().getId());
        return lockBean;
    }
    /** Prepare the Locking messages when other or same user locked
     *the same module number. Make server call to get the message for the
     *locked user
     *@param String moduleNumber
     *@throws Exception
     */
    protected void showLockingMessageProto(String moduleNumber, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        String lockId = CoeusLiteConstants.PROTO_ROUTING_LOCK_STR+moduleNumber;
        WebTxnBean webTxnBean = new WebTxnBean();
        UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("user"+request.getSession().getId());
        String loggedInUserId = userInfoBean.getUserId();
        LockBean serverLockedBean = getLockedData(lockId,request);
        if(serverLockedBean!= null){
            serverLockedBean.setModuleKey(CoeusLiteConstants.PROTOCOL_ROUTING_MODULE);
            serverLockedBean.setModuleNumber(moduleNumber);
            String lockUserId = serverLockedBean.getUserId();
            String lockUserName = EMPTY_STRING;
            String acqLock = "acquired_lock";
            ActionMessages messages = new ActionMessages();
            lockUserName =  viewRestrictionOfUser(loggedInUserId,lockUserId);

            messages.add("acqLock", new ActionMessage(acqLock,
                    lockUserName,serverLockedBean.getModuleKey(),
                    serverLockedBean.getModuleNumber()));
            //End
            saveMessages(request, messages);
            session.removeAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());
        }
    }
         public boolean checkForLastApproval(HttpServletRequest request,
             CoeusDynaFormList coeusDynaFormList) throws Exception{
        boolean returnFlag = false;
        //counter to match the vector size

        HttpSession session = request.getSession();
        String routingNumber = (String)session.getAttribute("routingNumber"+session.getId());


        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        HashMap hmData = new HashMap();
        //counter to check for last map in case of multiple maps
        int stopDelimiter = 0;
        Vector vctApprovalMaps = getProposalApprovalMaps(routingNumber, request);
        if(vctApprovalMaps == null){
            vctApprovalMaps = new Vector();
        }
        //to check whether the proposal/protocol is in the last approval stage in last sequential stop
        for(Object routingData:vctApprovalMaps){
            DynaBean dynaPropApprovalMaps = (DynaBean)routingData;
            //fetch the routing map details
            int counter = 0;
            stopDelimiter++;
            hmData.put("routingNumber" , routingNumber);
            hmData.put("mapNumber" , dynaPropApprovalMaps.get("mapNumber") );
            hmData.put("userId" , userId );
            Vector cvRoutingMap = getProposalApprovalStatus(hmData, request);
        returnFlag = true;    
        for(Object routingMapData:cvRoutingMap){
                DynaBean dynaRoutingDetailsBean = (DynaBean)routingMapData;
                    if(cvRoutingMap.size()>0){
                            //fetch the approval status for the approval stage just above the last approval
                            String approvalStatus = (String)dynaRoutingDetailsBean.get("approvalStatus");
                            if(approvalStatus.equals("W") || approvalStatus.equals("T")){
                            returnFlag = false;   
                            break;
                            }
        }}
        }
        
        return returnFlag;
    }
}
