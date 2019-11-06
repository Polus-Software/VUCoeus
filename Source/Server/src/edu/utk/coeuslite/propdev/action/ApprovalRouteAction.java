/*
* @(#) ApprovalRouteAction.java 1.0
*
* Copyright (c) Massachusetts Institute of Technology
* 77 Massachusetts Avenue, Cambridge, MA 02139-4307
* All rights reserved.
*
*/
/* PMD check performed, and commented unused imports and variables on 26-JAN-2011



 * by Bharati
 */
package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.routing.bean.RoutingAttachmentBean;
import edu.mit.coeus.routing.bean.RoutingBean;
import edu.mit.coeus.routing.bean.RoutingTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeuslite.irb.bean.ProtocolHeaderDetailsBean;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.LockBean;
import edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import javax.servlet.RequestDispatcher;

import org.apache.commons.beanutils.*;
import org.apache.commons.collections.comparators.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.wmc.coeuslite.budget.bean.ReadXMLData;
import edu.mit.coeus.bean.UserInfoBean;

import edu.mit.coeus.irb.bean.ProtocolSubmissionInfoBean;
import edu.mit.coeus.irb.bean.ProtocolSubmissionTxnBean;
import edu.mit.coeus.routing.bean.RoutingUpdateTxnBean;

import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.DateUtils;

import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.utk.coeuslite.propdev.bean.ApprovalRouteDisplayBean;
import java.sql.Date;
import java.util.Map;
import edu.mit.coeuslite.iacuc.action.ProtocolBaseAction;



/**
 * @author manjunathm
 *This is the ApprovalRoute Action class where the mapping data is fetched from the Database
 *and displayed on the screen along with the set of Approvers for each map.
 */
public class ApprovalRouteAction extends ProposalBaseAction{

    private static final String EMPTY_STRING = "";
    //Code added for Case#2785 - Protocol Routing
    private static final String XML_MENU_PATH = "/edu/utk/coeuslite/propdev/xml/ApprovalRouteMenu.xml";
    private static final String GENERAL_INFO_CODE = "P001";
    private static final String PROP_APP_MAP_PROCEDURE = "getProposalApprovalMaps";
    private static final String PROP_APP_MAP_STATUS_PROCEDURE = "getProposalApprovalStatus";
 private static final String ACTION_MODE = "actionMode";
    private static final String RECALL = "Recall";
    /*
     * These are the Image name constants
     */
    private static final String PROP_BYPASS_IMAGE 			= "bypass.gif";
    private static final String PROP_ALT_APPR_IMAGE 		= "altappr.gif";
    private static final String PROP_TO_BE_SUBMITTED_IMAGE 	= "tobesub.gif";
    private static final String PROP_WAITING_IMAGE 			= "waiting.gif";
    private static final String PROP_APPROVED_IMAGE 		= "approved.gif";
    private static final String PROP_REJECT_IMAGE 			= "reject.gif";
    private static final String PROP_PASS_IMAGE 			= "pass.gif";
    private static final String PROP_PASS_BY_OTHER                      ="altpassed.gif";
    private static final String PROP_DELEGATE_IMAGE 		= "delegate.gif";
    private static final String PRIMARY_APPROVER			= "primary.gif";
    private static final String ALTERNATE_APPROVER			= "alternate.gif";
    private static final String PROP_RECALL_IMAGE                       ="recalled.gif";
    // Added for 3915 - can't see comments in routing in Approval Routing - Start
    private DateUtils   dateUtils = new DateUtils();
    // Case :#3915 - End
   //Added For case#4262 -  Routing attachments not visible in Lite
     private static final String DATE_FORMAT = "MM/dd/yyyy";
     private static final String MAP_NUMBER = "mapNumber";
     private static final String STOP_NUMBER = "stopNumber";
     private static final String ROUTING_NUMBER = "routingNumber";
     private static final String APPROVER_NUMBER = "approverNumber";
     private static final String LEVEL_NUMBER = "levelNumber";
      private static final String PAGE_NAVIGATION = "pageNav";



     //COEUSQA-1433 - Allow Recall from Routing - Start
     private String RECALL_PROPOSAL_ROUTING = "RECALL_PROPOSAL_ROUTING";
     private String RECALL_IRB_PROTOCOL_ROUTING = "RECALL_IRB_PROTOCOL_ROUTING";
     private String RECALL_IACUC_PROTOCOL_ROUTING = "RECALL_IACUC_PROTOCOL_ROUTING";
     private String HAS_RECALL_RIGHTS = "HAS_RECALL_RIGHTS";
     private String HAS_APPROVER_RECALL_RIGHTS = "HAS_APPROVER_RECALL_RIGHTS";
     private boolean entryFlag = true;
     private int originalSubmissionNumber;
     private int currentSubmissionNumber;

     //COEUSQA-1433 - Allow Recall from Routing - End

     //COEUSQA:1699 - Add Approver Role - Start
     private static final String ADD_APPROVER = "ADD_APPROVER";
    private int protoStatusCode;
     //COEUSQA:1699 - End

    /* (non-Javadoc)
     * @see edu.mit.coeuslite.utils.CoeusBaseAction#performExecute(org.apache.struts.action.ActionMapping,
     * org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        //Code added for Case#4262 - Lite - Routing attachments not visible in Lite - starts
         HttpSession session = request.getSession() ;
         DynaValidatorForm dynaForm = (DynaValidatorForm)actionForm ;
		//COEUSQA-1433 - Allow Recall for Routing
         entryFlag = true;
         //COEUSQA-1433 - Allow Recall for Routing
         //Code added for Case#4262 - Lite - Routing attachments not visible in Lite - ends
            //Code added for Case#3915 - Lite - can’t see comments in routing in Approval Routing - starts
            //To get the comments data for the given routingnumber, mapnumber, levelnumber, stopnumber, approvernumber
            if(actionMapping.getPath().equals("/approvalRoutComments")){
                Vector vecComments = getComments(request);
                request.getSession().setAttribute("ApprovalRoutingComments", vecComments);


                // Added for 3915 - can't see comments in routing in Approval Routing - Start
                //Getting the Approval Date from ApprovalRouteDisplayBean and passing it in request
                if(request.getSession().getAttribute("hmDisplayCollection") != null){
                    request.setAttribute("getCommentsModuleItemKey",request.getSession().getAttribute("commentsModuleItemKey"));
                    HashMap displayCollection = (HashMap)request.getSession().getAttribute("hmDisplayCollection");
                    Integer mapNumber = new Integer(request.getParameter(MAP_NUMBER));
                    Integer stopNumber = new Integer(request.getParameter(STOP_NUMBER));
                    if(displayCollection != null && displayCollection.size() >0){
                        /*In the hmDisplayCollection, the fifth value has
                         * ApprovalRouteDisplayBean, so in order to get the ApprovalRouteDisplayBean,
                         * the get(5) is used.
                         */
                        ArrayList approvalRouting = (ArrayList)((ArrayList)displayCollection.get(mapNumber)).get(5);
                        for(int i=0;i<approvalRouting.size();i++){
                            ApprovalRouteDisplayBean displayBean = (ApprovalRouteDisplayBean)approvalRouting.get(i);
                            if(displayBean != null){
                                if(displayBean.getStopNumber().equals(stopNumber)){
                                    if(displayBean.getApprovalDate() != null){
                                        java.sql.Date sqlDate2 = displayBean.getApprovalDate();
                                        String strAppovalDate ="";
                                        if(sqlDate2 != null && sqlDate2.toString().length() > 0 ){
                                                strAppovalDate = dateUtils.formatDate(sqlDate2.toString(),DATE_FORMAT);

                                        }
                                        request.setAttribute("ApprovalDate", strAppovalDate);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                //Case :#3195 - End


                return actionMapping.findForward("success");
            }
            //Code added for Case#3915 - Lite - can’t see comments in routing in Approval Routing - ends


            //Code added for Case#4262 - Lite - Routing attachments not visible in Lite - starts
            if(actionMapping.getPath().equals("/approvalRoutingAttachments")){
                Vector vecAttachments = getAttachments(request);
                request.getSession().setAttribute("ApprovalRoutingAttachments", vecAttachments);
               //Getting the Approval Date from ApprovalRouteDisplayBean and passing it in request
                if(request.getSession().getAttribute("hmDisplayCollection") != null){
                    request.setAttribute("getCommentsModuleItemKey",request.getSession().getAttribute("commentsModuleItemKey"));
                    HashMap displayCollection = (HashMap)request.getSession().getAttribute("hmDisplayCollection");
                    Integer mapNumber = new Integer(request.getParameter(MAP_NUMBER));
                    Integer stopNumber = new Integer(request.getParameter(STOP_NUMBER));
                    if(displayCollection != null && displayCollection.size() >0){
                        ArrayList approvalRouting = (ArrayList)((ArrayList)displayCollection.get(mapNumber)).get(5);
                        for(int i=0;i<approvalRouting.size();i++){
                            ApprovalRouteDisplayBean displayBean = (ApprovalRouteDisplayBean)approvalRouting.get(i);
                            if(displayBean != null){
                                if(displayBean.getStopNumber().equals(stopNumber)){
                                    if(displayBean.getApprovalDate() != null){
                                        java.sql.Date sqlDate2 = displayBean.getApprovalDate();
                                        String strAppovalDate =EMPTY_STRING;
                                        if(sqlDate2 != null && sqlDate2.toString().length() > 0 ){
                                                strAppovalDate = dateUtils.formatDate(sqlDate2.toString(),DATE_FORMAT);

                                        }
                                        request.setAttribute("ApprovalDate", strAppovalDate);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }




                return actionMapping.findForward("success");
            }
            if(actionMapping.getPath().equals("/viewApprovalRoutingAttachments")){
              //vecAttachments = (Vector) request.getSession().getAttribute("ApprovalRoutingAttachments");
//              String index = request.getParameter("attachmentNumber");
//              String fileName = request.getParameter("fileName");
              String[] attachment = request.getParameterValues("attachment");
            //Modified for the caseid COEUSQA-2574: Routing attachments for protocols not appearing in Lite begin .
//              String attachmentNum = request.getParameter("attachmentNumbers");
//              String routingNum=request.getParameter("routingNumbers");
//              int levelNumber = Integer.parseInt(request.getParameter("levelNumber"));
               int selectedIndex = Integer.parseInt(request.getParameter("index"));
              //String templateURL= viewDocument(request, fileName,attachment,routingNum,index, levelNumber);


              String templateURL= viewDocument(session, selectedIndex);
                //Modified for the caseid COEUSQA-2574: Routing attachments for protocols not appearing in Lite end .
              session.setAttribute("url", templateURL);
              response.sendRedirect(request.getContextPath()+templateURL);
              return null;
            }
            //Code added for Case#4262 - Lite - Routing attachments not visible in Lite - ends
            getApprovalMenu(GENERAL_INFO_CODE, request);
            HashMap hmRequiredDetails = new HashMap();
            hmRequiredDetails.put(ActionMapping.class,actionMapping);
            hmRequiredDetails.put(DynaValidatorForm.class,dynaForm);
            //Code commented for Case#2785 - Protocol Routing
            //getProposalHeader(request);
            session.removeAttribute("HAS_APPROVER_RECALL_RIGHTS");
            session.removeAttribute("HAS_RECALL_RIGHTS");
            ActionForward actionForward = getApprovalRoutingData(hmRequiredDetails,
                                                    request, actionMapping,response);
            return actionForward;
        }


    /**
     * This method gets the approvalRoute Data for the given proposal number
     * @param hmRequiredDetails - This is the HashMap which carries the data for querying.
     * @return Returns the ActionForward path for this class.
     * @throws Exception
     */
    private ActionForward getApprovalRoutingData(HashMap hmRequiredDetails,
        HttpServletRequest request, ActionMapping actionMapping,HttpServletResponse response)throws Exception
    {
        HttpSession session = request.getSession();
//COEUSQA-1433 - Allow Recall from Routing - Start
       String actionMode = request.getParameter(ACTION_MODE);
        session.setAttribute(CoeusLiteConstants.PROPOSAL_LOCK,"");
        session.setAttribute(CoeusLiteConstants.PROTOCOL_LOCK,"");
        session.setAttribute(CoeusLiteConstants.IACUC_PROTOCOL_LOCK,"");
         //COEUSQA-3909
        String routingPreviousNext=(String)request.getParameter(PAGE_NAVIGATION);

        //COEUSQA-1433 - Allow Recall from Routing - End
        //Code modified for Case#2785 - Protocol Routing - starts
        String moduleCode = (String)session.getAttribute("moduleCode"+session.getId());
        String moduleItemKey = EMPTY_STRING;
        String moduleItemSeq = "0";
        int iModuleCode = (moduleCode == null)? 0 : Integer.parseInt(moduleCode);
        session.setAttribute("moduleCode", iModuleCode);
        String navigator = "success";
        //COEUSQA:1699 - Add Approver Role - Start
        boolean hasAddApproverRight = false;
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        String userId = ((UserInfoBean)session.getAttribute("user"+session.getId())).getUserId();
        String statusCode = null;
        //COEUSQA:1699 - End
        if(iModuleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
            String oldProposalNumber = (String)session.getAttribute("proposalNumber"+session.getId());
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            moduleItemKey = (String)session.getAttribute("proposalNumber"+session.getId());
// Added for 3915 - can't see comments in routing in Approval Routing - Start
            request.getSession().setAttribute("CommentsModuleCode",moduleCode);
            //Case:#3915 - End
            session.setAttribute("moduleItemKey", moduleItemKey);
            session.setAttribute("mode"+session.getId(),EMPTY_STRING);
           if(oldProposalNumber!= null){
                    LockBean lockBean = getLockingBean(userInfoBean, oldProposalNumber,request);
                    releaseLock(lockBean, request);
                    session.removeAttribute(CoeusLiteConstants.PROPOSAL_SEARCH_ACTION+session.getId());
                    session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(),
                            new Boolean(false));
                     EPSProposalHeaderBean headerBean = (EPSProposalHeaderBean)session.getAttribute("epsProposalHeaderBean");
                    if(headerBean!= null){
                    statusCode = headerBean.getProposalStatusCode();
                    }
                   // session.removeAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());
                }
            //COEUSQA:1699 - Add Approver Role - Start
            ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
            String leadUnitNumber = proposalDevelopmentTxnBean.getProposalLeadUnit(moduleItemKey);
            hasAddApproverRight = userMaintDataTxnBean.getUserHasRight(userId, ADD_APPROVER,leadUnitNumber);
            //COEUSQA:1699 - End
        }
        else if(iModuleCode == ModuleConstants.PROTOCOL_MODULE_CODE){
            moduleItemKey = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
            moduleItemSeq = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
             session.setAttribute("moduleItemKey", moduleItemKey);
            session.setAttribute("mode"+session.getId(),EMPTY_STRING);
            navigator = "displayProtocol";
            // Added for 3915 - can't see comments in routing in Approval Routing - Start
            request.getSession().setAttribute("CommentsModuleCode",moduleCode);
            //Case:#3915 - End
             UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
              // LockBean lockBean = getLockingBeanProto(userInfoBean, moduleItemKey,request);
             //  releaseLock(lockBean,request);
               // Added for COEUSQA-3816 : Lite - Proposal routing - Locking issues - Start
               LockBean lockBean = getLockingBeanIRBProto(userInfoBean, moduleItemKey,request);
               releaseLock(lockBean,request);
               // Added for COEUSQA-3816 : Lite - Proposal routing - Locking issues - End
               session.removeAttribute(CoeusLiteConstants.PROTOCOL_SEARCH_ACTION+session.getId());
               session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(),
                new Boolean(false));
               //COEUSQA:1699 - Add Approver Role - Start
               ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
               String leadUnitNumber = protocolDataTxnBean.getLeadUnitForProtocol(moduleItemKey,Integer.parseInt(moduleItemSeq));
               hasAddApproverRight = userMaintDataTxnBean.getUserHasRight(userId, ADD_APPROVER,leadUnitNumber);
                 ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
                        ProtocolSubmissionInfoBean submissionInformation =
                                protocolSubmissionTxnBean.getProtocolSubmissionDetails(moduleItemKey);
                        if(submissionInformation!=null){
                        statusCode=Integer.toString(submissionInformation.getSubmissionStatusCode());
                        }

               //COEUSQA:1699 - End
        }else if(iModuleCode == ModuleConstants.IACUC_MODULE_CODE){
            moduleItemKey = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
            moduleItemSeq = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
            session.setAttribute("moduleItemKey", moduleItemKey);
            session.setAttribute("mode"+session.getId(),EMPTY_STRING);
            navigator = "displayIacuc";
            request.getSession().setAttribute("CommentsModuleCode",moduleCode);
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            LockBean lockBean = getLockingBeanIacucProto(userInfoBean,
                                (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()), request);
                        releaseLock(lockBean, request);
             //COEUSQA:1699 - Add Approver Role - Start
            edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean iacucProtocolDataTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean();
            String leadUnitNumber = iacucProtocolDataTxnBean.getLeadUnitForProtocol(moduleItemKey,Integer.parseInt(moduleItemSeq));
            hasAddApproverRight = userMaintDataTxnBean.getUserHasRight(userId, ADD_APPROVER,leadUnitNumber);
            //COEUSQA:1699 - End
            edu.mit.coeus.iacuc.bean.ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolSubmissionTxnBean();
                        edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean submissionInformation =
                                protocolSubmissionTxnBean.getProtocolSubmissionDetails(moduleItemKey);
                         if(submissionInformation!=null){
                         statusCode=Integer.toString(submissionInformation.getSubmissionStatusCode());
                         }
        }
        //COEUSQA:1699 - Add Approver Role - Start
        session.setAttribute("hasAddApproverRight",hasAddApproverRight);
        //COEUSQA:1699 - End
//    	String proposalNumber 		= (String)session.getAttribute("proposalNumber"+session.getId());
//        System.out.println("ProposalNumber"+proposalNumber);
        //Code modified for Case#2785 - Protocol Routing - ends
        //COEUSQA:1699 - Add Approver Role - Start
    	//String userId 		  		= ((UserInfoBean)session.getAttribute("user"+session.getId())).getUserId();
        //COEUSQA:1699 - End
        session.setAttribute("userId", userId);
    	String displayType			= request.getParameter("DisplayType");
    	Integer mapId				= new Integer(-1);
    	String mapIDsPresent 		= EMPTY_STRING;
    	WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmMap=new HashMap();
    	DynaValidatorForm dynApprovalRouteMapValues = null;
     	HashMap hmpData = new HashMap();
    	HashMap hmProposalApprovalMap = new HashMap();
    	HashMap hmApprovalRouteMaps = new HashMap();
    	Vector vecApprovalStatusValues = null;
//         boolean hasLock=false;
    	// This gets the maps data fro any given proposalNumber.
//        Vector vecApprovalRouteMaps = null;
        RoutingTxnBean routingTxnBean = new RoutingTxnBean();
        RoutingBean routingBean = routingTxnBean.getRoutingHeader(moduleCode, moduleItemKey, moduleItemSeq, 0);
        session.setAttribute("routingstartdate", routingBean.getRoutingStartDate());
        session.setAttribute("routingenddate", routingBean.getRoutingEndDate());
        session.setAttribute("submissionNumber", routingBean.getApprovalSequence());



//        String routingNumber = (String) session.getAttribute("routingNumber"+session.getId());
        String routingNumber = EMPTY_STRING;
        //COEUSQA-1433 - Allow Recall from Routing - Start
        String moduleStatusCode = EMPTY_STRING;
        //COEUSQA-1433 - Allow Recall from Routing - End
        if(routingBean!=null){
//            vecApprovalRouteMaps = routingTxnBean.getRoutingMaps(routingBean.getRoutingNumber());
            routingNumber = routingBean.getRoutingNumber();
            session.setAttribute(ROUTING_NUMBER+session.getId(), routingNumber);
            session.setAttribute("routingBean"+session.getId(), routingBean);

        }
    	hmpData.put(ROUTING_NUMBER, routingNumber);
    	Hashtable hmMapResults = (Hashtable)webTxnBean.getResults(request,PROP_APP_MAP_PROCEDURE,hmpData);
    	Vector vecApprovalRouteMaps = (Vector)hmMapResults.get(PROP_APP_MAP_PROCEDURE);

        Vector procedures = new Vector(5,3);
                RoutingUpdateTxnBean routingUpdateTxnBean = new RoutingUpdateTxnBean(userId);
                int approvalSeq = routingUpdateTxnBean.getApprovalSequenceNumber(moduleItemKey, Integer.parseInt(moduleCode), Integer.parseInt(moduleItemSeq));
                session.setAttribute("approvalSequenceNumber",approvalSeq);
                if(statusCode.equals("214") || statusCode.equals("211"))
                {
                    UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
                     String unitNumber = userInfoBean.getUnitNumber();
                     webTxnBean = new WebTxnBean();
                     HashMap hmBuildMap = new HashMap();
                     hmBuildMap.put("modulecode", Integer.parseInt(moduleCode));
                     hmBuildMap.put("moduleitemkey", moduleItemKey);
                     hmBuildMap.put("moduleitemkeysequence", moduleItemSeq);
                     hmBuildMap.put("approvalsequence", String.valueOf(approvalSeq));
                     hmBuildMap.put("unitnumber", unitNumber);
                     hmBuildMap.put("updateuser", userId);
                     hmBuildMap.put("option", "D");
                    Hashtable htResult =(Hashtable)webTxnBean.getResults(request,"fnApprovalRoutingBuildMaps",hmBuildMap);
                    HashMap  hmResult = (HashMap)htResult.get("fnApprovalRoutingBuildMaps");
                    int isMapBuildCompleted = Integer.parseInt(hmResult.get("returnValue").toString());
                    routingBean = routingTxnBean.getRoutingHeader(moduleCode, moduleItemKey, moduleItemSeq, 0);
                }

            if(approvalSeq>2 || statusCode.equals("214") || statusCode.equals("211") || statusCode.equals("3")){
                session.setAttribute("statusCodeForMap",statusCode);
               String previousRouting=(String) session.getAttribute("showPreviousRouting");
                if(previousRouting==null){
                     session.setAttribute("showPreviousRouting","true");
                }
                if(session.getAttribute("currentSubmissionNumber")!=null){
                 currentSubmissionNumber=Integer.parseInt(session.getAttribute("currentSubmissionNumber").toString());
                }
                 if(routingPreviousNext!=null){
                   session.removeAttribute("showPreviousRouting");
                   if(routingPreviousNext.equals("previous")){
                        currentSubmissionNumber--;
                   }
                   else if(routingPreviousNext.equals("p")){
                    currentSubmissionNumber--;
                   }
                   else if(routingPreviousNext.equals("n")){
                    currentSubmissionNumber++;
                   }
                 }
                 else{
                   if(statusCode.equals("3") || statusCode.equals("211") || statusCode.equals("214")){
                     originalSubmissionNumber=approvalSeq;
                     currentSubmissionNumber=approvalSeq;
                   }
                   else{
                     originalSubmissionNumber=approvalSeq-1;
                     currentSubmissionNumber=approvalSeq-1;
                   }

                     session.setAttribute("originalSubmissionNumber", originalSubmissionNumber);
                     session.setAttribute("currentSubmissionNumber", currentSubmissionNumber);
                 }
                 session.setAttribute("currentSubmissionNumber", currentSubmissionNumber);
                 
                     UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
                     String unitNumber = userInfoBean.getUnitNumber();
                     webTxnBean = new WebTxnBean();
                     HashMap hmBuildMap = new HashMap();
                     hmBuildMap.put("modulecode", Integer.parseInt(moduleCode));
                     hmBuildMap.put("moduleitemkey", moduleItemKey);
                     hmBuildMap.put("moduleitemkeysequence", moduleItemSeq);
                     hmBuildMap.put("approvalsequence", String.valueOf(approvalSeq));
                     hmBuildMap.put("unitnumber", unitNumber);
                     hmBuildMap.put("updateuser", userId);
                     hmBuildMap.put("option", "D");
                 if(vecApprovalRouteMaps == null){
                    Hashtable htResult =(Hashtable)webTxnBean.getResults(request,"fnApprovalRoutingBuildMaps",hmBuildMap);
                    HashMap  hmResult = (HashMap)htResult.get("fnApprovalRoutingBuildMaps");
                    int isMapBuildCompleted = Integer.parseInt(hmResult.get("returnValue").toString());
                    routingBean = routingTxnBean.getRoutingHeader(moduleCode, moduleItemKey, moduleItemSeq, 0);
                 }
                 else{
                  routingBean = routingTxnBean.getRoutingHeader(moduleCode, moduleItemKey, moduleItemSeq, currentSubmissionNumber);
                 }
                     session.setAttribute("routingstartdate", routingBean.getRoutingStartDate());
                     session.setAttribute("routingenddate", routingBean.getRoutingEndDate());
                     session.setAttribute("submissionNumber", routingBean.getApprovalSequence());
            }

                if(routingBean!=null){
                    routingNumber = routingBean.getRoutingNumber();
                    session.setAttribute(ROUTING_NUMBER+session.getId(), routingNumber);
                    session.setAttribute("routingBean"+session.getId(), routingBean);
                }
                    hmpData.put(ROUTING_NUMBER, routingNumber);
                    hmMapResults = (Hashtable)webTxnBean.getResults(request,PROP_APP_MAP_PROCEDURE,hmpData);
                    vecApprovalRouteMaps = (Vector)hmMapResults.get(PROP_APP_MAP_PROCEDURE);



    	if(vecApprovalRouteMaps != null && vecApprovalRouteMaps.size()>0)
    	{
                request.setAttribute("vecApproval", vecApprovalRouteMaps);
    		Iterator mapsIterator = vecApprovalRouteMaps.iterator();
    		//		This gets the ApprovalStatus for the givgen userId , mapId and proposalNumber.
    		while(mapsIterator.hasNext())
    		{
    			dynApprovalRouteMapValues = (DynaValidatorForm)mapsIterator.next();
    			mapId = (Integer)dynApprovalRouteMapValues.get(MAP_NUMBER);
    			hmpData.clear();
//    			hmpData.put("proposalNumber",proposalNumber);
//    			hmpData.put("userId",userId);
//    			hmpData.put("mapId",mapId);
    			hmpData.put(ROUTING_NUMBER, routingNumber);
                        hmpData.put(MAP_NUMBER,dynApprovalRouteMapValues.get(MAP_NUMBER));
    			hmpData.put("userId",userId);
    			Hashtable hmApprovalStatusResults = (Hashtable)webTxnBean.getResults(request,PROP_APP_MAP_STATUS_PROCEDURE,hmpData);
    			vecApprovalStatusValues = (Vector)hmApprovalStatusResults.get(PROP_APP_MAP_STATUS_PROCEDURE);
    			hmProposalApprovalMap.put(mapId,vecApprovalStatusValues);
    			hmApprovalRouteMaps.put(mapId,dynApprovalRouteMapValues);
    		}
                //Code modified for Case#2785 - Protocol Routing
//                HashMap hmDisplayCollection = formDisplayCollection(proposalNumber ,hmApprovalRouteMaps,hmProposalApprovalMap  );
    		HashMap hmDisplayCollection = formDisplayCollection(moduleItemKey ,hmApprovalRouteMaps,hmProposalApprovalMap  );
    //COEUSQA-1433 - Allow Recall from Routing - Start
                //To disable the recall menuitem
                moduleStatusCode = setModuleStatusCode(iModuleCode,moduleItemKey,request);
                if(entryFlag){
                    checkApprovalStage(request, hmApprovalRouteMaps, hmProposalApprovalMap, moduleStatusCode);
                }
                //COEUSQA-1433 - Allow Recall from Routing - End
    		if(hmDisplayCollection != null)
    		{
                        //Modified for Case :#3915 - can't see comments in routing in Approval Routing - Start
                        request.getSession().setAttribute("commentsModuleItemKey",moduleItemKey);
    			request.getSession().setAttribute("hmDisplayCollection",hmDisplayCollection);
                        //Case :#3915 - End
    		}


    		mapIDsPresent = "Yes";
    	}
    	else
    	{
    		mapIDsPresent = "No";
    	}
        request.setAttribute("statusCode",statusCode);
        request.setAttribute("DisplayType",displayType);
    	request.setAttribute("mapIDsPresent",mapIDsPresent);
	//COEUSQA:1445 - User Not able to View Routing Comments through Show Routing - Start
        request.setAttribute("RoutingNumber",routingNumber);
        //COEUSQA:1445 - End
        //Code modified for Case#2785 - Protocol Routing
//         HashMap hmMap = getPropApprovalRights(request);
        //COEUSQA-1433 - Allow Recall from Routing - Start
        //HashMap hmMap = getApprovalRoutingMenus(request, moduleCode, moduleItemKey, moduleItemSeq);
         hmMap = getApprovalRoutingMenus(request, moduleCode, moduleItemKey, moduleItemSeq, moduleStatusCode);
        //COEUSQA-1433 - Allow Recall from Routing - End
        setRoutingHeader(iModuleCode,moduleItemKey,request);
        Vector vecData = new Vector();
        int parentNumber = 0;
        getTree(vecApprovalRouteMaps, vecData, parentNumber);
        request.setAttribute("vecApproval", vecData);

       // Commented for COEUSQA-3816 : Lite - Proposal routing - Locking issues - Start
       // Locking for routing is created only when user goes for 'Approve','Reject' or 'Recall'
       //COEUSQA-1433 - Allow Recall from Routing - Start
        //To check the lock
//        hasLock = lockSubmissionRecall(request, moduleCode, moduleItemKey);
        //boolean hasLock = true;


//        Integer moduleKeyCode = Integer.parseInt(moduleCode);
//        //to check whether the proposal/protocol is in routing
//        if(!hasLock && canDisplayApprovalMenus(moduleStatusCode)){
//            setRoutingMessageToSession(moduleKeyCode,routingBean.getModuleItemKey(),request);
//            if(moduleKeyCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
//                RequestDispatcher rd = request.getRequestDispatcher(
//                        "getGeneralInfo.do?proposalNumber="+routingBean.getModuleItemKey());
//                rd.forward(request,response);
//                return null;
//            }else if(moduleKeyCode == ModuleConstants.PROTOCOL_MODULE_CODE){
//                session.setAttribute(CoeusLiteConstants.PROTOCOL_LOCK,"true");
//                navigator = "displayProtocolInfo";
//            }else if(moduleKeyCode == ModuleConstants.IACUC_MODULE_CODE){
//                session.setAttribute(CoeusLiteConstants.IACUC_PROTOCOL_LOCK,"true");
//                navigator = "displayIacucProtocolInfo";
//            }
//        }else{
//            if(moduleKeyCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
//                session.setAttribute(CoeusLiteConstants.PROPOSAL_LOCK,"");
//            }else if(moduleKeyCode == ModuleConstants.PROTOCOL_MODULE_CODE){
//                session.setAttribute(CoeusLiteConstants.PROTOCOL_LOCK,"");
//            }else if(moduleKeyCode == ModuleConstants.IACUC_MODULE_CODE){
//                session.setAttribute(CoeusLiteConstants.IACUC_PROTOCOL_LOCK,"");
//            }
//        }
        // Commented for COEUSQA-3816 : Lite - Proposal routing - Locking issues - End

        //COEUSQA-1433 - Allow Recall from Routing - End
    	return actionMapping.findForward(navigator);
    }




    /* Method to refresh the routing header.
     * @param moduleCode: the module Code
     * @param moduleItemKey: the module Item Key
     * @param request: the request attribute
     */
    public void setRoutingHeader(int moduleCode, String moduleItemKey, HttpServletRequest request) throws Exception{
        HashMap hmDetails= new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();


        if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
            hmDetails.put("proposalNumber", moduleItemKey );
            Hashtable htPropData = (Hashtable)webTxnBean.getResults(request,"getProposalHeaderData",hmDetails);
            Vector vecProposalHeader = (Vector)htPropData.get("getProposalHeaderData");
            request.getSession().setAttribute("epsProposalHeaderBean",vecProposalHeader.get(0));


        }else if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE){
            hmDetails.put("protocolNumber",moduleItemKey);
            Hashtable htProtocolHeaderDetails=(Hashtable)webTxnBean.getResults(request, "getProtocolHeaderDetails",hmDetails );
            Vector vecProtocolHeader = (Vector)htProtocolHeaderDetails.get("getProtocolHeaderDetails");
            request.getSession().setAttribute("protocolHeaderBean", vecProtocolHeader.get(0));


        }else if(moduleCode == ModuleConstants.IACUC_MODULE_CODE){
            hmDetails.put("protocolNumber",moduleItemKey);
            Hashtable htProtocolHeaderDetails=(Hashtable)webTxnBean.getResults(request, "getIacucHeaderDetails",hmDetails );
            Vector vecProtocolHeader = (Vector)htProtocolHeaderDetails.get("getIacucHeaderDetails");
            request.getSession().setAttribute("iacucHeaderBean", vecProtocolHeader.get(0));
        }
    }


    private void getTree(Vector vecApprovalRouteMaps, Vector vecData, int parentNumber){
        if(vecApprovalRouteMaps != null){
            Vector vecFilteredData = new Vector();
            for(int index = 0; index < vecApprovalRouteMaps.size(); index++){
                DynaValidatorForm dynaRoutingMaps = (DynaValidatorForm) vecApprovalRouteMaps.get(index);
                if(dynaRoutingMaps != null && dynaRoutingMaps.get("parentMapNumber") != null
                        && ((Integer)dynaRoutingMaps.get("parentMapNumber")).intValue() == parentNumber){
                    vecFilteredData.add(dynaRoutingMaps);
                }
            }


            for(int index = 0; index < vecFilteredData.size(); index++){
                DynaValidatorForm dynaRoutingMaps = (DynaValidatorForm) vecFilteredData.get(index);
                vecData.add(dynaRoutingMaps);
                getTree(vecApprovalRouteMaps, vecData, ((Integer)dynaRoutingMaps.get(MAP_NUMBER)).intValue());
            }
        }
    }

    /**

     *
     * This method gets the map data to be displayed on the screen for the given proposal number
     * @param proposalNumber - This is the proposalNumber which is used for querying.
     * @param hmApprovalRouteMaps - This contains the result from the stored procedure 'getProposalApprovalStatus'
     * @param hmProposalApprovalMap - This is the HashMap containing the approvalStatus for the given proposalnumber
     * and MapID
     * @return - the Hash Table required to display on the target page.
     * @throws Exception
     */
    /**
     * @param proposalNumber
     * @param hmApprovalRouteMaps
     * @param hmProposalApprovalMap
     * @return
     * @throws Exception
     */
    public HashMap formDisplayCollection(String proposalNumber , HashMap hmApprovalRouteMaps,HashMap hmProposalApprovalMap )
    throws Exception
    {


		String description 		= 	null;
		String userId 			=	null;
		Integer mapNumber		=   new Integer(-1);
		String  mapStatusImage	=   EMPTY_STRING;
		String  mapStatus		=   EMPTY_STRING;
		DynaValidatorForm dynProposalApprovalMapBean = null;
                DynaValidatorForm dynProposalApprovalRouteValues = null;


		HashMap hmDisplayHash 	=  new HashMap();
		Set mapIds = hmProposalApprovalMap.keySet();
		Iterator mapIdIterator = mapIds.iterator();
		while(mapIdIterator.hasNext()) {
                    ArrayList 	displayList	=  new ArrayList();
                    mapNumber 						= (Integer)mapIdIterator.next();
                    dynProposalApprovalMapBean 	= (DynaValidatorForm)hmApprovalRouteMaps.get(mapNumber);
                    description      			= (String)dynProposalApprovalMapBean.get("description");
                    Vector approvalStatusValues = (Vector)hmProposalApprovalMap.get(mapNumber);
                    ArrayList displaydata		= 	new ArrayList();
                    if(approvalStatusValues!=null){
                        Iterator proposalApprovalValuesIterator = approvalStatusValues.iterator();
//                        String approverUserId = null;
                        String approverTypeImage = null;
                        String approverType 	 = null;
                        String approverDescription 		 = null;
                        String approvalStatusText = null;
                        String approvalStatus = null;
                        String approvalStatusImage = null;
                        //Case :#3915 - can't see comments in routing in Approval Routing - Start
                        String approvalDate = null;
                        //Case :#3915 - End
                        while(proposalApprovalValuesIterator.hasNext()) {
                            dynProposalApprovalRouteValues = (DynaValidatorForm)proposalApprovalValuesIterator.next();


                                /*
                                 * This is the bean for every map entry for the proposal
                                 */
                            ApprovalRouteDisplayBean rowDisplayBean = new ApprovalRouteDisplayBean();


                            rowDisplayBean.setRoutingNumber((String)dynProposalApprovalRouteValues.get(ROUTING_NUMBER));
                            rowDisplayBean.setMapNumber(mapNumber.intValue());
                            rowDisplayBean.setApproverNumber((Integer)dynProposalApprovalRouteValues.get("approverNumber"));
                            rowDisplayBean.setProposalNumber((String)dynProposalApprovalRouteValues.get("proposalNumber"));
                            rowDisplayBean.setLevelNumber((Integer)dynProposalApprovalRouteValues.get("levelNumber"));
                            rowDisplayBean.setStopNumber((Integer)dynProposalApprovalRouteValues.get("stopNumber"));
                            rowDisplayBean.setMapId((Integer)dynProposalApprovalRouteValues.get("mapId"));
                            approverDescription = (String)dynProposalApprovalRouteValues.getString("description");
                            //Case :#3915 - can't see comments in routing in Approval Routing - Start
                            approvalDate = (String)dynProposalApprovalRouteValues.get("approvaldate");
                            if(approvalDate != null){
                                //To spilt Date and Time StringTokenizer is used
                                StringTokenizer token = new StringTokenizer(approvalDate," ");
                                if(token.hasMoreTokens()){
                                   //Getting the first token which is Date
                                   rowDisplayBean.setApprovalDate(Date.valueOf(token.nextToken()));
                                }
                            }
                            //Case :#3915 - End
                            //COEUSQA:1445 - User Not able to View Routing Comments through Show Routing - Start
                            RoutingTxnBean routingTxnBean = new RoutingTxnBean();
                            Vector vecComments = routingTxnBean.getRoutingComments((String)dynProposalApprovalRouteValues.get(ROUTING_NUMBER), mapNumber.intValue(), (Integer)dynProposalApprovalRouteValues.get("levelNumber"),
                                    (Integer)dynProposalApprovalRouteValues.get("stopNumber"), (Integer)dynProposalApprovalRouteValues.get("approverNumber"));
                            if(vecComments != null && vecComments.size() > 0) {
                                rowDisplayBean.setIsCommentsPresent(true);
                            } else {
                                rowDisplayBean.setIsCommentsPresent(false);
                            }
                            Vector vecAttachments = routingTxnBean.getRoutingAttachments((String)dynProposalApprovalRouteValues.get(ROUTING_NUMBER), mapNumber.intValue(), (Integer)dynProposalApprovalRouteValues.get("levelNumber"),
                                    (Integer)dynProposalApprovalRouteValues.get("stopNumber"), (Integer)dynProposalApprovalRouteValues.get("approverNumber"));
                            if(vecAttachments != null && vecAttachments.size() > 0) {
                                rowDisplayBean.setIsAttachmentsPresent(true);
                            } else {
                                rowDisplayBean.setIsAttachmentsPresent(false);
                            }
                            //COEUSQA:1445 - End
                            approverDescription = approverDescription == null || approverDescription.equalsIgnoreCase("null")?"":approverDescription;
                            rowDisplayBean.setDescription(approverDescription);
                            approverType = (String)dynProposalApprovalRouteValues.get("primaryApproverFlag");
                            rowDisplayBean.setPrimaryApproverFlag(approverType);
                            rowDisplayBean.setUpdateTimeStamp((String)dynProposalApprovalRouteValues.get("updateTimeStamp"));
                            if(approverType.equalsIgnoreCase("Y")) {
                                approverTypeImage = PRIMARY_APPROVER;
                            }
                            else {
                                approverTypeImage = ALTERNATE_APPROVER;
                            }
                            rowDisplayBean.setUserId((String)dynProposalApprovalRouteValues.get("userId"));
                            //Added for displaying user name for  user Id
                            String approvalUserName = !((String)dynProposalApprovalRouteValues.get("approvalUserName")).equals(EMPTY_STRING) &&
                            (String)dynProposalApprovalRouteValues.get("approvalUserName") != null
                            ?(String)dynProposalApprovalRouteValues.get("approvalUserName"):
                                (String)dynProposalApprovalRouteValues.get("userId");
                                rowDisplayBean.setApprovalUserName(approvalUserName);


                                //End
                                rowDisplayBean.setUserName((String)dynProposalApprovalRouteValues.get("userName"));
                                rowDisplayBean.setApproverType(approverTypeImage);
                                approvalStatus = (String)dynProposalApprovalRouteValues.getString("approvalStatus");
                                approvalStatusImage = this.getStatusImageName(approvalStatus);
                                approvalStatusText = (String)dynProposalApprovalRouteValues.get("approvalStatusText");
                                mapStatus	   = approvalStatus;
                                mapStatusImage = approvalStatusImage;
                                rowDisplayBean.setApprovalStatus(approvalStatus);
                                rowDisplayBean.setApprovalStatusText(approvalStatusText);
                                rowDisplayBean.setApprovalStatusImage(approvalStatusImage);
                                rowDisplayBean.setBypassFlag((String)dynProposalApprovalRouteValues.get("byPassFlag"));


                                displaydata.add(rowDisplayBean);
                        }
                    }
                    displaydata = arSortedBeanList(displaydata);
                    displayList.add(mapNumber);
                    displayList.add(description);
                    displayList.add(userId);
                    displayList.add(mapStatus);
                    displayList.add(mapStatusImage);
                    displayList.add(displaydata);




                    hmDisplayHash.put(mapNumber,displayList);


                }
		hmDisplayHash = sortDisplayByMapId(hmDisplayHash);
		return hmDisplayHash;


    }


    /**
     * This method sorts the bean collection in the ArrayList on three
     * keys - LevelNumber, StopNumber and primaryApproverFlag
     * @param arUnsortedBeanList - This is the unSorted ArrayList containig the
     * collection of beans clApprovalRouteDisplayBean.

     *
     * @return returns the sorted ArrayList
     */
    public ArrayList arSortedBeanList(ArrayList arUnsortedBeanList)
    {
    	/*
    	 * This reverses the ArrayList
    	 */
    	java.util.Collections.reverse(arUnsortedBeanList);
		/*
		 * This is the array:ist containg the keys required for sorting
		 */
    	ArrayList sortFields = new ArrayList();
		sortFields.add(new BeanComparator("levelNumber"));
		sortFields.add(new BeanComparator("stopNumber"));
		sortFields.add(new BeanComparator("primaryApproverFlag"));
		/*
		 * This is the comparator chain which uses the above listed keys
		 * for multilevel sorting.
		 */
		ComparatorChain multiSort = new
		ComparatorChain(sortFields);
		java.util.Collections.sort(arUnsortedBeanList,multiSort);
		java.util.Collections.reverse(arUnsortedBeanList);
		return arUnsortedBeanList;
     }


    /**
     * This method is for sorting the contents of the hashmap based on the key
     * @param displayHash - This is the main HashTable
     * @return returns the sorted hashTable.
     */
    public HashMap sortDisplayByMapId(HashMap displayHash)
    {
    	Vector vecKeySet = new Vector(displayHash.keySet());
    	HashMap hmSortedDisplayHash = new HashMap();
    	Collections.sort(vecKeySet);


		Iterator sortedIterator = vecKeySet.iterator();


		while(sortedIterator.hasNext())
		{
			Integer newMapId = (Integer)sortedIterator.next();


			ArrayList displayList =(ArrayList)displayHash.get(newMapId);


			hmSortedDisplayHash.put(newMapId,displayList);
		}


		//This is for displaying the contents of the collection
		//that will be sent to the server.
		//Uncomment for displaying the collection.
		//displayValues(sortedDisplayHash);


		return hmSortedDisplayHash;
    }




    /**
     * This is a debug statement which can be used for displaying the contents of the main hashmap.
     * To use it, the calling point has to be uncommented
     * @param sortedDisplayHash - This method is used for displaying the data and is optional
     */


    public void displayValues(Hashtable  sortedDisplayHash)
    {
    	Set mapIds = sortedDisplayHash.keySet();
    	Iterator keySetIter = mapIds.iterator();
    	while(keySetIter.hasNext())
    	{
    		Integer mapId = (Integer)keySetIter.next();
    		ArrayList displayList = (ArrayList)(sortedDisplayHash.get(mapId));
    		ArrayList displayData = (ArrayList)displayList.get(3);
    		//System.out.println("mapId=="+mapId);
    		//System.out.println("description==="+displayList.get(1));
    		//System.out.println("userId==="+displayList.get(2));
    		Iterator beanIterator = displayData.iterator();
			while(beanIterator.hasNext())
			{
						ApprovalRouteDisplayBean appBeanSorted = (ApprovalRouteDisplayBean)beanIterator.next();
				//System.out.println("level=="+appBeanSorted.getLevelNumber());
				//System.out.println("stopNumber=="+appBeanSorted.getStopNumber());
				//System.out.println("primaryApproverFlag=="+appBeanSorted.getPrimaryApproverFlag());
				//System.out.println("userId=="+appBeanSorted.getUserId());

			}
			//System.out.println("  ");
    	}
    }


    /**
     * This method is for getting an populating the left side menu.
     * @param menuCode - This is the menu for the leftside panel
     */
    private void getApprovalMenu(String menuCode, HttpServletRequest request)
    {
        Vector ApprovalRouteMenuItemsVector  = null;
        ReadXMLData readXMLData = new ReadXMLData();
        HttpSession session = request.getSession();
        ApprovalRouteMenuItemsVector = (Vector) session.getAttribute(APPROVAL_ROUTE_MENU_ITEMS);
//        if (ApprovalRouteMenuItemsVector == null || ApprovalRouteMenuItemsVector.size()==0) {
        	ApprovalRouteMenuItemsVector = readXMLData.readXMLDataForMenu(XML_MENU_PATH);
            session.setAttribute(APPROVAL_ROUTE_MENU_ITEMS, ApprovalRouteMenuItemsVector);


//        }

    }


    /**
     *
     * This method gives the Proposal Approval rights values
     * @return the hashmap of the approval rights data
     * @throws Exception
     */
    public HashMap getApprovalRoutingMenus(HttpServletRequest request, String moduleCode,
          String moduleItemKey, String moduleItemSeq, String moduleStatus)throws Exception{
        HttpSession session = request.getSession();
//        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
//        String userId = userInfoBean.getUserId();
        //Code commented for Case#2785 - Protocol Routing
//        String moduleCode = (String)session.getAttribute("moduleCode"+session.getId());
//        moduleCode = (moduleCode == null)? EMPTY_STRING : moduleCode;
//        String moduleItemKey = EMPTY_STRING;
//        String moduleItemSeq = EMPTY_STRING;
//        if(moduleCode.equals("3")){
//            moduleItemKey = (String)session.getAttribute("proposalNumber"+session.getId());
//            moduleItemSeq = "0";
//        } else if(moduleCode.equals("7")){
//            moduleItemKey = (String)session.getAttribute("protocolNumber"+session.getId());
//            moduleItemSeq = (String)session.getAttribute("sequenceNumber"+session.getId());
//        }
        HashMap hmApprovalRights = getApprovalRights(request, moduleCode, moduleItemKey, moduleItemSeq);
//        String avViewRouting =(String) hmApprovalRights.get(VIEW_ROUTING);
        String avPropWait =(String) hmApprovalRights.get(PROPWAIT);
        getApprovalMenu(EMPTY_STRING, request);
        Vector approvalMenuItemsVector = (Vector) session.getAttribute(APPROVAL_ROUTE_MENU_ITEMS);

        //COEUSQA-1433 - Allow Recall from Routing - Start
        //To check whether the user has rights to recall the submission
        boolean hasRight = checkUserHasRecallRights(request, moduleCode, moduleItemKey, moduleItemSeq);
        //to restrict the approval menu items
        boolean canDisplayApproveReject = canDisplayApprovalMenus(moduleStatus);
        //COEUSQA-1433 - Allow Recall from Routing - End


        Vector modifiedVector = new Vector();
        if(approvalMenuItemsVector!= null && approvalMenuItemsVector.size() > 0){
            for (int index=0; index < approvalMenuItemsVector.size();index++) {
                MenuBean meanuBean = (MenuBean)approvalMenuItemsVector.get(index);
                String menuId = meanuBean.getMenuId();
                //Code modified for Case#2785 - Protocol Routing - starts
                meanuBean.setVisible(true);
//                if((menuId.equals(CoeusliteMenuItems.APPROVAL_ROUTING_PROTOCOL) || menuId.equals(CoeusliteMenuItems.APPROVAL_ROUTING_PROPOSAL))
//                && (avViewRouting!= null && avViewRouting.equals(CoeusliteMenuItems.ROUTING_STATUS))) {
//                    meanuBean.setVisible(false);
//                }
 //COEUSQA-1433 - Allow Recall from Routing - Start
                //if(menuId.equals(CoeusliteMenuItems.APPROVE) && (avPropWait!= null && avPropWait.equals(CoeusliteMenuItems.ROUTING_STATUS))) {
                if(menuId.equals(CoeusliteMenuItems.APPROVE) && (avPropWait!= null && avPropWait.equals(CoeusliteMenuItems.ROUTING_STATUS))) {
                    meanuBean.setVisible(false);
				}else if(menuId.equals(CoeusliteMenuItems.APPROVE) && !canDisplayApproveReject){
                    meanuBean.setVisible(false);
                }
                //if(avPropWait!= null && avPropWait.equals(CoeusliteMenuItems.ROUTING_STATUS) && (menuId.equals(CoeusliteMenuItems.REJECT))) {
                if(avPropWait!= null && avPropWait.equals(CoeusliteMenuItems.ROUTING_STATUS) && (menuId.equals(CoeusliteMenuItems.REJECT))) {
                    meanuBean.setVisible(false);
				}else if(menuId.equals(CoeusliteMenuItems.REJECT) && !canDisplayApproveReject){
                    meanuBean.setVisible(false);
                }
               //COEUSQA-1433 - Allow Recall from Routing - End
                if((menuId.equals(CoeusliteMenuItems.ROUTING_PROTOCOL_DETAILS) || menuId.equals(CoeusliteMenuItems.ROUTING_PROTOCOL_SUMMARY))
                    && !String.valueOf(ModuleConstants.PROTOCOL_MODULE_CODE).equals(moduleCode)){
                    meanuBean.setVisible(false);
                } else if((menuId.equals(CoeusliteMenuItems.ROUTING_PROPOSAL_DETAILS) || menuId.equals(CoeusliteMenuItems.ROUTING_PROPOSAL_SUMMARY))
                    && !String.valueOf(ModuleConstants.PROPOSAL_DEV_MODULE_CODE).equals(moduleCode)){
                    meanuBean.setVisible(false);
                }else if((menuId.equals(CoeusliteMenuItems.IACUC_ROUTING_PROTOCOL_DETAILS) || menuId.equals(CoeusliteMenuItems.IACUC_ROUTING_PROTOCOL_SUMMARY))
                    && !String.valueOf(ModuleConstants.IACUC_MODULE_CODE).equals(moduleCode)){
                    meanuBean.setVisible(false);
                }
                if(menuId.equals(CoeusliteMenuItems.ROUTING_PROTOCOL_DETAILS) ||
                        menuId.equals(CoeusliteMenuItems.ROUTING_PROTOCOL_SUMMARY) ||
                        menuId.equals(CoeusliteMenuItems.IACUC_ROUTING_PROTOCOL_DETAILS) ||
                        menuId.equals(CoeusliteMenuItems.IACUC_ROUTING_PROTOCOL_SUMMARY)){
                    StringBuffer menuLink = new StringBuffer(meanuBean.getMenuLink());
                    menuLink.append("&protocolNumber=");
                    menuLink.append(moduleItemKey);
                    menuLink.append("&sequenceNumber=");
                    menuLink.append(moduleItemSeq);
                    meanuBean.setMenuLink(menuLink.toString());
                }
                //Code modified for Case#2785 - Protocol Routing - ends
                modifiedVector.add(meanuBean);
            }
        }
        //COEUSQA-1433 - Allow Recall from Routing - Start
        session.setAttribute(HAS_RECALL_RIGHTS, hasRight);
        //COEUSQA-1433 - Allow Recall from Routing - End
        session.setAttribute(APPROVAL_ROUTE_MENU_ITEMS, modifiedVector);
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",APPROVAL_ROUTE_MENU_ITEMS);
        //Code added for Case#2785 - Protocol Routing - starts
        if(String.valueOf(ModuleConstants.PROPOSAL_DEV_MODULE_CODE).equals(moduleCode)){
            mapMenuList.put("menuCode","P002");
        } else if(String.valueOf(ModuleConstants.PROTOCOL_MODULE_CODE).equals(moduleCode)){
            mapMenuList.put("menuCode","02");
            session.setAttribute("menuItemsVector", modifiedVector);
            request.setAttribute("ROUTING", "ROUTING");
        }
        //Code added for Case#2785 - Protocol Routing - ends
        else if(String.valueOf(ModuleConstants.IACUC_MODULE_CODE).equals(moduleCode)){
            mapMenuList.put("menuCode","02");
            session.setAttribute("iacucmenuItemsVector", modifiedVector);
            request.setAttribute("ROUTING", "ROUTING");
        }
        setSelectedMenuList(request, mapMenuList);
        return hmApprovalRights;
    }


    /**
     * This method gets the ApprovalStatusImage for any approvalStatus
     * @param approvalStatus
     * @return the approvalStatusImage
     */
    private String getStatusImageName(String approvalStatus)
     {
    	 	String approvalStatusImage = null;
    	 	char approvalStatusCode = approvalStatus.charAt(0);
		   	switch(approvalStatusCode)
	    	{
	            case 'B':
	            	approvalStatusImage	= PROP_BYPASS_IMAGE;
	              break;

	            case 'O':
	            	approvalStatusImage	= PROP_ALT_APPR_IMAGE;
	              break;

	            case 'T':
	            	approvalStatusImage	= PROP_TO_BE_SUBMITTED_IMAGE;
	              break;

	            case 'W':
	            	approvalStatusImage	= PROP_WAITING_IMAGE;
	              break;

	            case 'A':
	            	approvalStatusImage	= PROP_APPROVED_IMAGE;
	              break;

	            case 'R':
	            	approvalStatusImage	= PROP_REJECT_IMAGE;
	              break;

	            case 'J':
	            	approvalStatusImage	= PROP_REJECT_IMAGE;
	              break;

	            case 'P':
	            	approvalStatusImage	= PROP_PASS_IMAGE;
	              break;

	            case 'L':
	            	approvalStatusImage	= PROP_PASS_BY_OTHER;
	              break;

	            case 'D':
	            	approvalStatusImage	= PROP_DELEGATE_IMAGE;
	              break;

                    case 'C':
	             approvalStatusImage	= PROP_RECALL_IMAGE;
	              break;

                     case 'E':
	             approvalStatusImage	= PROP_RECALL_IMAGE;
	              break;
	        }
		   	return approvalStatusImage;
     }


    /**
     * Added for Case#3915 - Lite - can’t see comments in routing in Approval Routing
     * To get the list of comments for the given routingnumber, mapnumber, levelnumber, stopnumber, approvernumber
     * @param request HttpServletRequest
     * @throws java.lang.Exception
     * @return Vector list of comments
     */
    public Vector getComments(HttpServletRequest request) throws Exception{
        RoutingTxnBean routingTxnBean = new RoutingTxnBean();
        String routingNumber = request.getParameter(ROUTING_NUMBER);
        int mapNumber = Integer.parseInt(request.getParameter(MAP_NUMBER));
        int levelNumber = Integer.parseInt(request.getParameter(LEVEL_NUMBER));
        int stopNumber = Integer.parseInt(request.getParameter(STOP_NUMBER));
        int approverNumber = Integer.parseInt(request.getParameter(APPROVER_NUMBER));
        Vector vecComments = routingTxnBean.getRoutingComments(
                                routingNumber, mapNumber, levelNumber, stopNumber, approverNumber);
        return vecComments;
    }
    /**
     * Added for Case#4262 - Lite - Routing attachments not visible in Lite
     * To get the list of attachments for the given routingnumber, mapnumber, levelnumber, stopnumber, approvernumber
     * @param request HttpServletRequest
     * @throws java.lang.Exception
     * @return Vector list of attachments
     */
    public Vector getAttachments(HttpServletRequest request) throws Exception{
        RoutingTxnBean routingTxnBean = new RoutingTxnBean();
        String routingNumber = request.getParameter(ROUTING_NUMBER);
        int mapNumber = Integer.parseInt(request.getParameter(MAP_NUMBER));
        int levelNumber = Integer.parseInt(request.getParameter(LEVEL_NUMBER));
        int stopNumber = Integer.parseInt(request.getParameter(STOP_NUMBER));
        int approverNumber = Integer.parseInt(request.getParameter(APPROVER_NUMBER));
        Vector vecComments = routingTxnBean.getRoutingAttachments(
                                routingNumber, mapNumber, levelNumber, stopNumber, approverNumber);
        return vecComments;
    }
    /**
     * Added for Case#4262 - Lite - Routing attachments not visible in Lite
     * To View the Attachment documnet for the given fileName, attachment, attachNum, routingNum
     * @param request HttpServletRequest
     * @throws java.lang.Exception
     * @return Vector list of comments
     */


     //Modified for the caseid COEUSQA-2574: Routing attachments for protocols not appearing in Lite begin .
//      private String viewDocument(HttpServletRequest request,
//             String fileName,String[] attachment,String attachNum,String routingNum, String index, int levelNumber) throws Exception{


//

        private String viewDocument(final HttpSession session, final int selectedIndex)throws Exception{


          final List routingAttachments = (List)session.getAttribute("ApprovalRoutingAttachments");
          final DocumentBean documentBean = new DocumentBean();
          final Map map = new HashMap();
          final RoutingAttachmentBean routingAttachmentBean = (RoutingAttachmentBean)routingAttachments.get(selectedIndex);


//          byte[] attachments =attachment.toString().getBytes();
//          routingAttachmentBean.setFileBytes(attachments);
//          routingAttachmentBean.setFileName(fileName);

//
//          routingAttachmentBean.setRoutingNumber(routingNumber);
//          routingAttachmentBean.setMapNumber(mapNumber);
//          routingAttachmentBean.setLevelNumber(levelNumber);
//          routingAttachmentBean.setStopNumber(stopNumber);
//          routingAttachmentBean.setApproverNumber(approverNumber);
//          routingAttachmentBean.setAttachmentNumber(attachmentNumber);
//          routingAttachmentBean.setAttachmentNumber(Integer.parseInt(attachNum));
//          routingAttachmentBean.setRoutingNumber(routingNum);
        //Modified for the caseid COEUSQA-2574: Routing attachments for protocols not appearing in Lite end .
          map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.routing.RoutingAttachmentsReader");
          map.put("FUNCTION_TYPE","GET_ROUTING_ATTACHMENT");
          map.put("DATA", routingAttachmentBean);
          documentBean.setParameterMap(map);
          String docId = DocumentIdGenerator.generateDocumentId();
          StringBuffer stringBuffer = new StringBuffer();
          stringBuffer.append("/StreamingServlet");
          stringBuffer.append("?");
          stringBuffer.append(DocumentConstants.DOC_ID);
          stringBuffer.append("=");
          stringBuffer.append(docId);
          session.setAttribute(docId, documentBean);
          return stringBuffer.toString();
      }

        //COEUSQA-1433 - Allow Recall from Routing - Start
        /**
         * To check whether the user has the rights to recall the proposal/protocol
         * @param request HttpServletRequest
         * @param String moduleCodeKey
         * @param String moduleItemKey
         * @param String moduleItemSeq
         * @throws java.lang.Exception
         * @return boolean hasRight
         */
        private boolean checkUserHasRecallRights(HttpServletRequest request, String moduleCodeKey,
            String moduleItemKey, String moduleItemSeq) throws Exception{
            boolean hasRight = false;
            HttpSession session = request.getSession();
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            String userId = userInfoBean.getUserId();
            //To fetch the userInfoBean
            UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
            if(moduleCodeKey != null && moduleCodeKey.length() > 0){
                Integer moduleCode = Integer.parseInt(moduleCodeKey);
                if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                    //Check user has RECALL_PROPOSAL_ROUTING right at proposal
                    hasRight = userMaintDataTxnBean.getUserHasProposalRight(userId,moduleItemKey,RECALL_PROPOSAL_ROUTING);
                }else if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE){
                    //Check user has RECALL_IRB_PROTOCOL_ROUTING right at proposal
                    hasRight = userMaintDataTxnBean.getUserHasProtocolRight(userId,RECALL_IRB_PROTOCOL_ROUTING,moduleItemKey);
                }else if(moduleCode == ModuleConstants.IACUC_MODULE_CODE){
                    //Check user has RECALL_IACUC_PROTOCOL_ROUTING right at proposal
                    hasRight = userMaintDataTxnBean.getUserHasIACUCProtocolRight(userId,RECALL_IACUC_PROTOCOL_ROUTING,moduleItemKey);
                }
            }
            return hasRight;
        }


         /**
         * To check whether the lock is there and obtain lock the proposal/protocol
         * @param request HttpServletRequest
         * @param String moduleCodeKey
         * @param String moduleItemKey
         * @throws java.lang.Exception
         * @return boolean hasRight
         */
        private boolean lockSubmissionRecall(HttpServletRequest request, String moduleCodeKey,
                String moduleItemKey) throws Exception{
            boolean lockCheck = false;
            String errMsg = EMPTY_STRING;
            HttpSession session = request.getSession();
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            String userId = userInfoBean.getUserId();
            String unitNumber = userInfoBean.getUnitNumber();
            if(moduleCodeKey != null && moduleCodeKey.length() > 0){
                Integer moduleCode = Integer.parseInt(moduleCodeKey);
                String lockId = "";
                if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                    lockId = CoeusLiteConstants.PROP_ROUTING_LOCK_STR+moduleItemKey;
                }else if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE){
                    lockId = CoeusLiteConstants.PROTO_ROUTING_LOCK_STR+moduleItemKey;
                }else if(moduleCode == ModuleConstants.IACUC_MODULE_CODE){
                    lockId = CoeusLiteConstants.IACUC_PROTO_ROUTING_LOCK_STR+moduleItemKey;
                }
                TransactionMonitor transMon = TransactionMonitor.getInstance();
                lockCheck = transMon.lockAvailabilityCheck(lockId, userInfoBean.getUserId());
                if(lockCheck ){
                    LockBean lockBean = getApprovalLockingBean(userInfoBean, moduleItemKey,lockId,moduleCode, request);
                    boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
                    if(isLockExists){
                        lockingRoutingModule(moduleItemKey,lockId,moduleCode,request);
                        return lockCheck;
                    }else{
                        lockCheck = false;
                    }
                }

            }
            return lockCheck;
        }

        /** Manufacture the LockBean based on the parameter passed by the specific module
         *say, Propsoal, Protocol, Budget etc.
         *@param UserInfoBean, Proposal number
         *@returns LockBean
         *@throws Exception
         */

       protected LockBean getLockingBean(UserInfoBean userInfoBean, String proposalNumber, HttpServletRequest request) throws Exception{
           HttpSession session=request.getSession();
           String moduleCode="";
           if(session.getAttribute("moduleCode"+session.getId())!=null)
           {
                moduleCode = (String)session.getAttribute("moduleCode"+session.getId());
           }
        LockBean lockBean = new LockBean();
        if(!moduleCode.equals(null) && !moduleCode.equals(""))
        {
          if(moduleCode.equals("7"))
          {
              lockBean.setLockId(CoeusLiteConstants.PROTO_ROUTING_LOCK_STR+proposalNumber);
          }
        }else{
             lockBean.setLockId(CoeusLiteConstants.PROP_ROUTING_LOCK_STR+proposalNumber);
        }

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
       protected LockBean getApprovalLockingBean(UserInfoBean userInfoBean, String moduleItemKey, String lockId, int moduleCode, HttpServletRequest request) throws Exception{
           LockBean lockBean = new LockBean();
           lockBean.setLockId(lockId);
           String mode = (String)request.getSession().getAttribute(CoeusLiteConstants.MODE_DETAILS+request.getSession().getId());
           mode = getMode(mode);
           lockBean.setMode(mode);
           if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
               lockBean.setModuleKey(CoeusLiteConstants.PROPOSAL_ROUTING_MODULE);
           }else if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE){
               lockBean.setModuleKey(CoeusLiteConstants.PROTOCOL_ROUTING_MODULE);
           }else if(moduleCode == ModuleConstants.IACUC_MODULE_CODE){
               lockBean.setModuleKey(CoeusLiteConstants.IACUC_PROTOCOL_ROUTING_MODULE);
           }
           lockBean.setModuleNumber(moduleItemKey);
           lockBean.setModuleUnitNumber(userInfoBean.getUnitNumber());
           lockBean.setUnitNumber(UNIT_NUMBER);
           lockBean.setUserId(userInfoBean.getUserId());
           lockBean.setUserName(userInfoBean.getUserName());
           lockBean.setSessionId(request.getSession().getId());
           return lockBean;
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
        /**
         * To obtain the lock for the proposal/protocol
         * @param request HttpServletRequest
         * @param String moduleCodeKey
         * @param String moduleItemKey
         * @throws java.lang.Exception
         */
        protected void lockingRoutingModule(String moduleItemKey,String lockId, int moduleCode, HttpServletRequest request) throws Exception {
            HttpSession session = request.getSession();
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            LockBean lockBean = getApprovalLockingBean(userInfoBean, moduleItemKey,lockId,moduleCode,request);
            lockBean.setMode("M");
            lockModule(lockBean,request);
            session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(),new Boolean(true));
        }

        /**
         * To obtain the lock for the proposal/protocol
         * @param request HttpServletRequest
         * @param String moduleCodeKey
         * @param String moduleItemKey
         * @throws java.lang.Exception
         */
        protected void checkApprovalStage(HttpServletRequest request, HashMap hmApprovalRouteMaps, HashMap hmProposalApprovalMap, String moduleStatusCode){
            boolean hasRight = false;
            HttpSession session = request.getSession();
            if("2".equals(moduleStatusCode) || "107".equals(moduleStatusCode) || "108".equals(moduleStatusCode) || "6".equals(moduleStatusCode)){
		Integer mapNumber =   new Integer(-1);
		DynaValidatorForm dynProposalApprovalMapBean = null;
                DynaValidatorForm dynProposalApprovalRouteValues = null;
		Set mapIds = hmProposalApprovalMap.keySet();
		Iterator mapIdIterator = mapIds.iterator();
		while(mapIdIterator.hasNext()) {
                    mapNumber = (Integer)mapIdIterator.next();
                    dynProposalApprovalMapBean 	= (DynaValidatorForm)hmApprovalRouteMaps.get(mapNumber);
                    Vector approvalStatusValues = (Vector)hmProposalApprovalMap.get(mapNumber);
                    if(approvalStatusValues!=null){
                        Iterator proposalApprovalValuesIterator = approvalStatusValues.iterator();
                        String approvalStatus = null;
                        while(proposalApprovalValuesIterator.hasNext()) {
                            dynProposalApprovalRouteValues = (DynaValidatorForm)proposalApprovalValuesIterator.next();
                            approvalStatus = (String)dynProposalApprovalRouteValues.getString("approvalStatus");
                            if(approvalStatus!=null && (approvalStatus.equals("W"))){
                                hasRight = true;
                                session.setAttribute(HAS_APPROVER_RECALL_RIGHTS, hasRight);
                                entryFlag = false;
                                break;
                            }else{
                                session.setAttribute(HAS_APPROVER_RECALL_RIGHTS, hasRight);
                            }
                        }
                    }
                }
            }else{
                session.setAttribute(HAS_APPROVER_RECALL_RIGHTS, hasRight);
            }
        }

        /* Method to refresh the routing header.
         * @param moduleCode: the module Code
         * @param moduleItemKey: the module Item Key
         * @param request: the request attribute
         */
        private String setModuleStatusCode(int moduleCode, String moduleItemKey, HttpServletRequest request) throws Exception{
            HashMap hmDetails= new HashMap();
            WebTxnBean webTxnBean = new WebTxnBean();
            String moduleStatusCode = EMPTY_STRING;
            if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                if((request.getSession().getAttribute("proposalStatus"))!=null){
                    moduleStatusCode = ""+(Integer)request.getSession().getAttribute("proposalStatus");
                }else{
                    hmDetails.put("proposalNumber", moduleItemKey );
                    Hashtable htPropData = (Hashtable)webTxnBean.getResults(request,"getProposalHeaderData",hmDetails);
                    Vector vecProposalHeader = (Vector)htPropData.get("getProposalHeaderData");
                    EPSProposalHeaderBean epsProposalHeaderBean = (EPSProposalHeaderBean)(vecProposalHeader.get(0));
                    moduleStatusCode = epsProposalHeaderBean.getProposalStatusCode();
                }

            }else if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE){
                if((request.getSession().getAttribute("protocolStatusCode"))!=null){
                    moduleStatusCode = (String)request.getSession().getAttribute("protocolStatusCode");
                }else{
                    hmDetails.put("protocolNumber",moduleItemKey);
                    Hashtable htProtocolHeaderDetails=(Hashtable)webTxnBean.getResults(request, "getProtocolHeaderDetails",hmDetails );
                    Vector vecProtocolHeader = (Vector)htProtocolHeaderDetails.get("getProtocolHeaderDetails");
                    ProtocolHeaderDetailsBean protocolHeaderDetailsBean = (ProtocolHeaderDetailsBean)(vecProtocolHeader.get(0));
                    moduleStatusCode = ""+protocolHeaderDetailsBean.getProtocolStatusCode();
                }

            }else if(moduleCode == ModuleConstants.IACUC_MODULE_CODE){
                if((request.getSession().getAttribute("statusCode"))!=null){
                    moduleStatusCode = (String)request.getSession().getAttribute("statusCode");
                }else{
                    hmDetails.put("protocolNumber",moduleItemKey);
                    Hashtable htProtocolHeaderDetails=(Hashtable)webTxnBean.getResults(request, "getIacucHeaderDetails",hmDetails );
                    Vector vecProtocolHeader = (Vector)htProtocolHeaderDetails.get("getIacucHeaderDetails");
                    edu.mit.coeuslite.iacuc.bean.ProtocolHeaderDetailsBean iacucHeaderDetailsBean =
                            (edu.mit.coeuslite.iacuc.bean.ProtocolHeaderDetailsBean)(vecProtocolHeader.get(0));
                    moduleStatusCode = ""+iacucHeaderDetailsBean.getProtocolStatusCode();
                }
            }
            return moduleStatusCode;
        }

        /* Method to restrict the approval menu items.
         * @param moduleStatus: the module status
         */
        private boolean canDisplayApprovalMenus(String moduleStatus){
            boolean canDisplay = false;
            if("2".equals(moduleStatus) || "107".equals(moduleStatus) || "108".equals(moduleStatus) || "6".equals(moduleStatus)){
                canDisplay = true;
            }
            return canDisplay;
        }
        //COEUSQA-1433 - Allow Recall from Routing - End

        private void setRoutingMessageToSession(int moduleCode ,String moduleItemKey,HttpServletRequest request) throws CoeusException, DBException, Exception{
            HttpSession session = request.getSession();
            String moduleDescription = "";
            String lockStr = "";
            if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                moduleDescription = CoeusLiteConstants.PROPOSAL_ROUTING_MODULE;
                lockStr = CoeusLiteConstants.PROP_ROUTING_LOCK_STR+moduleItemKey;
            }else if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE){
                moduleDescription = CoeusLiteConstants.PROTOCOL_ROUTING_MODULE;
                lockStr = CoeusLiteConstants.PROTO_ROUTING_LOCK_STR+moduleItemKey;
            }else if(moduleCode == ModuleConstants.IACUC_MODULE_CODE){
                moduleDescription = CoeusLiteConstants.IACUC_PROTOCOL_ROUTING_MODULE;
                lockStr = CoeusLiteConstants.IACUC_PROTO_ROUTING_LOCK_STR+moduleItemKey;
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            CoeusFunctions coeusFunction = new CoeusFunctions();
            String paramValue = coeusFunction.getParameterValue("DISPLAY_LOCKNAME_FOR_ROUTING");
            String lockMsg = "";
             UserMaintDataTxnBean userTxnBean = new UserMaintDataTxnBean();
               LockBean lockBean = getLockedData(lockStr,request);
            if(lockBean!=null)
            {
                if("Y".equalsIgnoreCase(paramValue)){
                String currenatLockUserName = userTxnBean.getUserName(lockBean.getUserId());
               lockMsg = currenatLockUserName+" "+
                       coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1000")+" "+
                       moduleDescription+" "+ moduleItemKey;
            }else{
                lockMsg = "Another User"+" "+
                        coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1000")+" "+
                        moduleDescription+" "+ moduleItemKey;
            }
            session.setAttribute("routingLockMessage",lockMsg);
        }
        }

        // Added for COEUSQA-3816 : Lite - Proposal routing - Locking issues - Start
        /**
         * Method to get locking details for IRB protocol
         * @param userInfoBean
         * @param protocolNumber
         * @param request
         * @throws java.lang.Exception
         * @return lockBean - LockBean
         */
        protected LockBean getLockingBeanIRBProto(UserInfoBean userInfoBean, String protocolNumber, HttpServletRequest request) throws Exception{
            LockBean lockBean = new LockBean();
            lockBean.setLockId(CoeusLiteConstants.PROTO_ROUTING_LOCK_STR +protocolNumber);
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
        // Added for COEUSQA-3816 : Lite - Proposal routing - Locking issues - End

}



