/*
 * @(#) OrganizationSearchForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

/* PMD check performed, and commented unused imports and variables on 07-MAY-2007
 * by Noorul
 */
package edu.mit.coeuslite.irb.action;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

//import java.io.IOException;
import edu.mit.coeus.routing.bean.RoutingDetailsBean;
import edu.mit.coeus.routing.bean.RoutingUpdateTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.Utils;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.LockBean;
import edu.utk.coeuslite.propdev.bean.ApprovalRouteDisplayBean;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.Hashtable;
//import java.util.Enumeration;
//import java.sql.Date;
//import java.sql.SQLException;

import javax.naming.*;
import javax.servlet.http.*;
import javax.servlet.*;
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//import javax.servlet.http.HttpServletResponse;

//import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
//import org.apache.struts.action.ActionError;
//import org.apache.struts.action.ActionErrors;

import edu.mit.coeus.bean.*;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.user.bean.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.routing.bean.RoutingBean;
import edu.mit.coeus.search.bean.AttributeBean;
import edu.mit.coeus.search.bean.ColumnBean;
//import edu.mit.coeus.search.bean.CriteriaBean;
import edu.mit.coeus.search.bean.ProcessSearchXMLBean;
import edu.mit.coeus.search.bean.SearchExecutionBean;
import edu.mit.coeus.search.bean.SearchInfoHolderBean;
import edu.mit.coeus.search.exception.CoeusSearchException;
//import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.*;
//import edu.mit.coeus.utils.dbengine.DBException;
//import edu.mit.coeuslite.irb.form.OrganizationSearchForm;
import edu.mit.coeuslite.utils.SearchUtil;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;


/**
 * This class is associated with a particular action through an
 * action mapping in <code>struts-config.xml</code>.
 * @author nadhgj
 * @Created on March 3, 2005, 11:20 AM
 */

public class IrbSearchAction extends ProtocolBaseAction {
    /**
     * No argument constructor.
     */
    
    //COEUSQA:1699 - Add Approver Role - Start
    private static final String MAP_NUMBER_FIELD = "mapNumber";
    private static final String LEVEL_NUMBER_FIELD = "levelNumber";
    private static final String STOP_NUMBER_FIELD = "stopNumber";
    private static final String DESCRIPTION = "Approver added by ";
    //COEUSQA:1699 - End
    
    //Removing instance variable case# 2960
//    private Log myLog;
    public IrbSearchAction() {
        //myLog = LogFactory.getLog(OrganizationSearchAction.class);
    }
    
    
    public ActionForward performExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String strSearchType = request.getParameter("type");
        String searchName = request.getParameter("searchName");
        boolean search = new Boolean(request.getParameter("search")).booleanValue();
        String rowId = request.getParameter("actionPerformed");
        HttpSession session = request.getSession();
//        Vector orgVector = new Vector();
        //COEUSQA:1699 - Add Approver Role - Start
        String strRoutingNumber = request.getParameter("routingNumber");
        String strMapNumber = request.getParameter("mapNumber");
        String strLevelNumber = request.getParameter("levelNumber");
        String strStopNumber = request.getParameter("stopNumber");
        String strApproverNumber = request.getParameter("approverNumber");
        //COEUSQA:1699 - End
        
        if(rowId != null) {
            Vector resList = (Vector)session.getAttribute("resList");
//            java.util.HashMap hashmap = (java.util.HashMap)resList.get(Integer.parseInt(rowId));            
            request.setAttribute("getRowData", resList.get(Integer.parseInt(rowId)));
            //COEUSQA:1699 - Add Approver Role - Start
            java.util.HashMap hashmap = (java.util.HashMap)resList.get(Integer.parseInt(rowId));
            UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user"+session.getId());
            String userId = (String)userInfoBean.getUserId();
            String searchType=(String) session.getAttribute("type");
            if("AddApprover".equals(searchType)){
                LockBean lockBean = getLockingBean(userInfoBean, (String)request.getSession().getAttribute(
                                                   CoeusLiteConstants.PROTOCOL_NUMBER+request.getSession().getId()), request);
                String protocolNumber = (String)request.getSession().getAttribute(
                                                   CoeusLiteConstants.PROTOCOL_NUMBER+request.getSession().getId());
                lockBean.setLockId(CoeusLiteConstants.PROTO_ROUTING_LOCK_STR+protocolNumber);                
                releaseLock(lockBean, request);                
                HashMap hmApprovalRouteDetails = (HashMap)request.getSession().getAttribute("hmDisplayCollection");
                String mapNumber = (String) session.getAttribute("mapNumber");
                ArrayList approversDetailList = (ArrayList)hmApprovalRouteDetails.get(Integer.parseInt(mapNumber));
                ArrayList approversList = (ArrayList)approversDetailList.get(5);                
                ApprovalRouteDisplayBean approvalRouteDisplayBean = null;
                for(Object approvers : approversList){
                    approvalRouteDisplayBean = (ApprovalRouteDisplayBean)approvers;
                    if("W".equals(approvalRouteDisplayBean.getApprovalStatus())){                        
                        break;
                    }
                }
                CoeusVector cvExistingApprovers = convertArrayListToCoeusVector(approversList);
                
                Equals eqLevelNumber = new Equals(LEVEL_NUMBER_FIELD,new Integer(approvalRouteDisplayBean.getLevelNumber()));
                CoeusVector cvCurrenLevelApprovers = cvExistingApprovers.filter(eqLevelNumber);
                
                for(Object currenLevelApprovers : cvCurrenLevelApprovers) {
                    ApprovalRouteDisplayBean approvalRouteBean = (ApprovalRouteDisplayBean)currenLevelApprovers;
                    if(((String) hashmap.get("USER_NAME")).equalsIgnoreCase(approvalRouteBean.getUserId())) {
                        ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("noSearchCriteriaEntered", new ActionMessage("routing.approverPresent_exception"));
                        saveMessages(request, actionMessages);
                        return mapping.findForward("exception");                        
                    }
                }
                if( cvCurrenLevelApprovers != null && cvCurrenLevelApprovers.size() > 0 ){
                    cvCurrenLevelApprovers.sort(STOP_NUMBER_FIELD, false);
                    approvalRouteDisplayBean = (ApprovalRouteDisplayBean)cvCurrenLevelApprovers.get(0);                    
                }
                
                CoeusVector cvApprover = new CoeusVector();
                RoutingDetailsBean routingDetailsBean = new RoutingDetailsBean();
                routingDetailsBean.setMapNumber(approvalRouteDisplayBean.getMapNumber());
                routingDetailsBean.setLevelNumber(approvalRouteDisplayBean.getLevelNumber());
                routingDetailsBean.setStopNumber(approvalRouteDisplayBean.getStopNumber()+1);
                routingDetailsBean.setRoutingNumber(approvalRouteDisplayBean.getRoutingNumber());
                routingDetailsBean.setDescription(DESCRIPTION + userInfoBean.getUserName());
                routingDetailsBean.setApprovalStatus("W");
                routingDetailsBean.setAcType(TypeConstants.INSERT_RECORD);
                routingDetailsBean.setUserId((String) hashmap.get("USER_NAME"));
                routingDetailsBean.setPrimaryApproverFlag(true);
                routingDetailsBean.setApproverNumber(1);
                routingDetailsBean.setUpdateUser(userInfoBean.getUserId());
                cvApprover.addElement(routingDetailsBean);
                
                RoutingUpdateTxnBean routingUpdateTxnBean = new RoutingUpdateTxnBean(userId);
                RoutingBean routingBean = (RoutingBean) session.getAttribute("routingBean"+session.getId());
                Integer returnValue = routingUpdateTxnBean.addRoutingNewApprovers(routingDetailsBean,routingBean,cvApprover);
                
                return mapping.findForward("success");
                
            } else if("AddAlternateApprover".equals(searchType)){
                String routingNumber = (String) session.getAttribute("routingNumber");
                String mapNumber = (String) session.getAttribute("mapNumber");
                String levelNumber = (String) session.getAttribute("levelNumber");
                String stopNumber = (String) session.getAttribute("stopNumber");
                String approverNumber = (String) session.getAttribute("approverNumber");
                
                LockBean lockBean = getLockingBean(userInfoBean, (String)request.getSession().getAttribute(
                                                   CoeusLiteConstants.PROTOCOL_NUMBER+request.getSession().getId()), request);
                String protocolNumber = (String)request.getSession().getAttribute(
                                                   CoeusLiteConstants.PROTOCOL_NUMBER+request.getSession().getId());
                lockBean.setLockId(CoeusLiteConstants.PROTO_ROUTING_LOCK_STR+protocolNumber);  
                releaseLock(lockBean, request);
                
                HashMap hmApprovalRouteDetails = (HashMap)request.getSession().getAttribute("hmDisplayCollection");
                ArrayList approversDetailList = (ArrayList)hmApprovalRouteDetails.get(Integer.parseInt(mapNumber));
                ArrayList approversList = (ArrayList)approversDetailList.get(5);
                
                CoeusVector cvApprovers = convertArrayListToCoeusVector(approversList);
                CoeusVector cvFilteredApprovers;
                
                Equals eqMapNumber = new Equals(MAP_NUMBER_FIELD, new Integer(mapNumber ));
                Equals eqLevelNumber = new Equals(LEVEL_NUMBER_FIELD, new Integer(levelNumber));
                And eqMapIdAndEqLevelNumber = new And( eqMapNumber, eqLevelNumber);
                
                cvFilteredApprovers = cvApprovers.filter(eqMapIdAndEqLevelNumber);
                
                CoeusVector cvCurrenLevelApprovers = cvFilteredApprovers.filter(eqLevelNumber);                
                for(Object currenLevelApprovers : cvCurrenLevelApprovers) {
                    ApprovalRouteDisplayBean approvalRouteBean = (ApprovalRouteDisplayBean)currenLevelApprovers;
                    if(((String) hashmap.get("USER_NAME")).equalsIgnoreCase(approvalRouteBean.getUserId())) {
                        ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("noSearchCriteriaEntered", new ActionMessage("routing.approverPresent_exception"));
                        saveMessages(request, actionMessages);
                        return mapping.findForward("exception");
                    }
                }
                
                Equals eqStopNumber = new Equals("stopNumber", new Integer(stopNumber));
                And eqStopNumAndMapIdAndEqLevelNum = new And( eqStopNumber, eqMapIdAndEqLevelNumber);
                cvFilteredApprovers = cvApprovers.filter(eqStopNumAndMapIdAndEqLevelNum);
                
                CoeusVector cvApprover = new CoeusVector();
                RoutingDetailsBean routingDetailsBean = new RoutingDetailsBean();
                routingDetailsBean.setMapNumber(Integer.parseInt(mapNumber));
                routingDetailsBean.setLevelNumber(Integer.parseInt(levelNumber));
                routingDetailsBean.setStopNumber(Integer.parseInt(stopNumber));
                routingDetailsBean.setRoutingNumber(routingNumber);
                routingDetailsBean.setDescription(DESCRIPTION + userInfoBean.getUserName());
                routingDetailsBean.setApprovalStatus("W");
                routingDetailsBean.setAcType(TypeConstants.INSERT_RECORD);
                routingDetailsBean.setUserId((String) hashmap.get("USER_NAME"));
                routingDetailsBean.setPrimaryApproverFlag(false);
                if(cvFilteredApprovers == null){
                    routingDetailsBean.setApproverNumber(1);
                } else {
                    routingDetailsBean.setApproverNumber(cvFilteredApprovers.size()+1);
                }
                routingDetailsBean.setUpdateUser(userInfoBean.getUserId());
                cvApprover.addElement(routingDetailsBean);
                
                RoutingUpdateTxnBean routingUpdateTxnBean = new RoutingUpdateTxnBean(userId);
                RoutingBean routingBean = (RoutingBean) session.getAttribute("routingBean"+session.getId());
                Integer returnValue = routingUpdateTxnBean.addRoutingNewApprovers(routingDetailsBean,routingBean,cvApprover);
                
                return mapping.findForward("success");                
                //COEUSQA:1699 - End
            }
            else {
            return mapping.findForward("success");
            }
        }
        if(search){
            
            ProcessSearchXMLBean processSearchXML = new ProcessSearchXMLBean(
                    "",searchName);
            session.setAttribute("searchinfoholder",processSearchXML.getSearchInfoHolder());
            session.setAttribute("displayList",getDisplayList(searchName).getCriteriaList());
            request.setAttribute("search", "true");
            session.setAttribute("type", strSearchType);
            //COEUSQA:1699 - Add Approver Role - Start 
            session.setAttribute("routingNumber", strRoutingNumber);
            session.setAttribute("mapNumber", strMapNumber);
            session.setAttribute("levelNumber", strLevelNumber);
            session.setAttribute("stopNumber", strStopNumber);
            session.setAttribute("approverNumber", strApproverNumber);        
            //COEUSQA:1699 - End 
            ActionForward actionforward = mapping.findForward( "success" );
            actionforward = mapping.findForward( searchName );
        }
        if(!search) {
//            final String SEARCH_NAME = "searchname";
            
            ActionForward actionforward = mapping.findForward( "success" );
            boolean errorFlag = false;
//            boolean validResult = false;	// the result of valid award/proposal
//            ActionErrors actionErrors = new ActionErrors(); // to hold any validation errors
            session = request.getSession( true );
            SearchInfoHolderBean searchInfoHolder =
                    (SearchInfoHolderBean)session.getAttribute("searchinfoholder");
            // COEUSDEV-326: Lite - Protocol Funding source - application should validate the sponsor code entered by the user - Start
            // Replace the hard coded COEUS in Remclause with logged in User id.
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            String remClause = searchInfoHolder.getRemClause();
            if(remClause != null){
                String newRemClause = Utils.replaceString(
                                remClause,"COEUS",userInfoBean.getUserId().toUpperCase().trim());
                searchInfoHolder.setRemClause(newRemClause);
            }
            // COEUSDEV-326: Lite - Protocol Funding source - application should validate the sponsor code entered by the user - End
            SearchExecutionBean searchExecution = new SearchExecutionBean(searchInfoHolder);
//                Vector columns = new Vector(3,2);
            // Code commented for coeus 4.3 search between two dates enhancements -starts
            // The following codes are added in SearchUtil.setSearchCriterias method
//                String[] fieldValues = null;
//                Vector criteriaList = searchInfoHolder.getCriteriaList();
//                int criteriaCount = criteriaList.size();
//                boolean noSearchCriteriaEntered =true;
//                for(int criteriaIndex=0;criteriaIndex<criteriaCount;criteriaIndex++){
//                    CriteriaBean criteria = (CriteriaBean)criteriaList.get(criteriaIndex);
//                    String criteriaName = criteria.getName().trim();
//                    fieldValues = request.getParameterValues(criteriaName);
//                    ColumnBean column = new ColumnBean(criteriaName);
//                    if(fieldValues!=null){
//                    for(int fieldIndex=0;fieldIndex<fieldValues.length;fieldIndex++){
//                        String fieldValue = fieldValues[fieldIndex];
//                        if(fieldValue!=null && !fieldValue.trim().equals("")){
//                            if(fieldValue.indexOf("*")!=-1){
//                                fieldValue = fieldValue.replace('*','%');
//                                fieldValue = "LIKE " + fieldValue;
//                            }
//                            //fieldValue = fieldValue.replace('*','%');
////                            if(!fieldValue.startsWith("%")) {
////                                fieldValue = "%"+fieldValue;
////                            }
//                            //fieldValue = "LIKE " + fieldValue;
//                            noSearchCriteriaEntered =false;
//                            AttributeBean attribute = new AttributeBean(
//                            ""+fieldIndex,fieldValue);
//                            searchExecution.addAttribute(column,attribute);
//                        }
//                    }
//                }
//                    searchExecution.addColumn(column);
//                }
            // Code commented for coeus4.3 search between two dates enhancements - ends
            // Code added for coeus4.3 search between two dates enhancements
            boolean isSearchCriteriaEntered = SearchUtil.fillCriteria(searchExecution, searchInfoHolder, request);
            String protocolSearch = request.getParameter("protocolSearch");
            if(protocolSearch != null){
                if(protocolSearch.equals("true")){
//                    UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
                    String fieldValue = " = " + userInfoBean.getPersonId();
                    AttributeBean attribute = new AttributeBean(
                            ""+0,fieldValue);
                    ColumnBean column = new ColumnBean("OSP$PROTOCOL_INVESTIGATORS.PERSON_ID");
                    column.addAttribute(attribute);
                    searchExecution.addColumn(column);
                }
            }
            if(searchName.equals("personSearch")){
                request.setAttribute("reqType",request.getParameter("reqType"));
                request.setAttribute("reqPage",request.getParameter("reqPage"));
            }
            
            Hashtable searchResult = null ;
            if(!isSearchCriteriaEntered){
                ActionMessages actionMessages = new ActionMessages();
                actionMessages.add("noSearchCriteriaEntered", new ActionMessage("noSearchCriteriaEntered_exception"));
                saveMessages(request, actionMessages);
                return mapping.findForward("exception");
            }else{
                request.setAttribute("fieldName",request.getParameter("fieldName"));
                searchResult = searchExecution.executeSearchQuery();
            }
            
            
            // request.setAttribute("fieldName",request.getParameter("fieldName"));
            // Hashtable searchResult = searchExecution.executeSearchQuery();
            if(searchResult!=null){
                request.setAttribute("searchResult",searchResult);
                session.setAttribute("resList",searchResult.get("reslist"));
                request.setAttribute("searchValue", "false");
                actionforward = mapping.findForward("result");
            } else{
                throw new CoeusException("exceptionCode.20005");
            }
            
            
            
            if( errorFlag ) {
                actionforward = mapping.findForward( "failure" );
            }
//                else {
//                    //actionforward = mapping.findForward( searchName );
//                }
            return actionforward;
            
        }
        return mapping.findForward("success");
    }
    
    private SearchInfoHolderBean getDisplayList(String searchReq) throws CoeusSearchException,CoeusException{
        ProcessSearchXMLBean processSearchXML = new ProcessSearchXMLBean("",searchReq);
        
        return processSearchXML.getSearchInfoHolder();
    }
    
    public void cleanUp() {
    }
    
    
}
