/*
 * ProposalSearchAction.java
 *
 * Created on April 19, 2006, 12:18 PM
 */

package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.routing.bean.RoutingBean;
import edu.mit.coeus.routing.bean.RoutingDetailsBean;
import edu.mit.coeus.routing.bean.RoutingUpdateTxnBean;
import edu.mit.coeus.search.bean.AttributeBean;
import edu.mit.coeus.search.bean.ColumnBean;
import edu.mit.coeus.search.bean.CriteriaBean;
import edu.mit.coeus.search.bean.ProcessSearchXMLBean;
import edu.mit.coeus.search.bean.SearchExecutionBean;
import edu.mit.coeus.search.bean.SearchInfoHolderBean;
import edu.mit.coeus.search.exception.CoeusSearchException;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.SearchModuleBean;
import edu.mit.coeuslite.utils.SearchUtil;
import edu.utk.coeuslite.propdev.bean.ApprovalRouteDisplayBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import edu.mit.coeus.utils.Utils;

/**
 *
 * @author  vinayks
 */
public class ProposalSearchAction extends ProposalBaseAction {        
    //    private String actionToken; - commented for removing instance variable - case# 2960 

    //COEUSQA:1699 - Add Approver Role - Start
    private static final String MAP_NUMBER_FIELD = "mapNumber";
    private static final String LEVEL_NUMBER_FIELD = "levelNumber";
    private static final String STOP_NUMBER_FIELD = "stopNumber";
    private static final String DESCRIPTION = "Approver added by ";
    //COEUSQA:1699 - End
    
    /** Creates a new instance of ProposalSearchAction */
    public ProposalSearchAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String strSearchType = request.getParameter("type");
        String searchName = request.getParameter("searchName");
        boolean search = new Boolean(request.getParameter("search")).booleanValue();
        String rowId = request.getParameter("actionPerformed");
        session = request.getSession();
        //Added for COEUSQA-2428 : LIte - Prop Dev - Application does not return any rows while searching for Original proposal - Start
        UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user"+session.getId());            
        String userId = (String)userInfoBean.getUserId();   
        //COEUSQA-2428 : End
        //COEUSQA:1699 - Add Approver Role - Start
        String strRoutingNumber = request.getParameter("routingNumber");
        String strMapNumber = request.getParameter("mapNumber");
        String strLevelNumber = request.getParameter("levelNumber");
        String strStopNumber = request.getParameter("stopNumber");
        String strApproverNumber = request.getParameter("approverNumber");
        //COEUSQA:1699 - End
        if(rowId != null) {
            Vector vecResultList = (Vector)session.getAttribute("resList");
            java.util.HashMap hashmap = (java.util.HashMap)vecResultList.get(Integer.parseInt(rowId));
            request.setAttribute("getRowData", vecResultList.get(Integer.parseInt(rowId)));
            String searchType=(String) session.getAttribute("type");             
            if(searchType.equals("AwardSearch")){
                return actionMapping.findForward("awardSearch");
            }else if(searchType.equals("PersonSearch")){
                return actionMapping.findForward("personSearch");
            }else if(searchType.equals("SponsorSearch")){               
                session.setAttribute("sponsorName",hashmap.get("SPONSOR_NAME"));
                return actionMapping.findForward("sponsorSearch");                
            }else if(searchType.equals("PrimeSponsorSearch")){
               return actionMapping.findForward("primeSponsorSearch");
            }else if(searchType.equals("proposalSearch")){
                setSearchData(request,hashmap);
                request.setAttribute("proposalNumber", hashmap.get("PROPOSAL_NUMBER"));
                return actionMapping.findForward("proposalSearch"); 
            
            //COEUSQA:1699 - Add Approver Role - Start
            }else if("AddApprover".equals(searchType)){                
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
                        return actionMapping.findForward("exception");                        
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
                
                return actionMapping.findForward("success");
                
            }else if("AddAlternateApprover".equals(searchType)){
                String routingNumber = (String) session.getAttribute("routingNumber");
                String mapNumber = (String) session.getAttribute("mapNumber");
                String levelNumber = (String) session.getAttribute("levelNumber");
                String stopNumber = (String) session.getAttribute("stopNumber");
                String approverNumber = (String) session.getAttribute("approverNumber");
                
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
                        return actionMapping.findForward("exception");
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
                
                return actionMapping.findForward("success");                
                //COEUSQA:1699 - End
            }else {
                return actionMapping.findForward("success");
            }
        }
        if(search) {
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
            //            actionToken = "success";- commented for removing instance variable - case# 2960 
            ActionForward actionforward = actionMapping.findForward( "success" );
            actionforward = actionMapping.findForward( searchName );
        }
        if(!search) {            
            ActionForward actionforward = actionMapping.findForward( "success" );
            boolean errorFlag = false;            
            session = request.getSession( true );
            SearchInfoHolderBean searchInfoHolder =
            (SearchInfoHolderBean)session.getAttribute("searchinfoholder");
            SearchExecutionBean searchExecution = new SearchExecutionBean(searchInfoHolder);
            // Code commented for coeus 4.3 enhancements - starts
            // The following codes are added in SearchUtil.setSearchCriterias method            
//            String[] fieldValues = null;
//            Vector criteriaList = searchInfoHolder.getCriteriaList();
//            int criteriaCount = criteriaList.size();
//            boolean noSearchCriteriaEntered =true;
//            for(int criteriaIndex=0;criteriaIndex<criteriaCount;criteriaIndex++){
//                CriteriaBean criteria = (CriteriaBean)criteriaList.get(criteriaIndex);
//                String criteriaName = criteria.getName().trim();
//                fieldValues = request.getParameterValues(criteriaName);
//                ColumnBean column = new ColumnBean(criteriaName);
//                for(int fieldIndex=0;fieldIndex<fieldValues.length;fieldIndex++){
//                    String fieldValue = fieldValues[fieldIndex];
//                    
//                    if(fieldValue!=null && !fieldValue.trim().equals("")){
//                        fieldValue = fieldValue.replace('*','%');
//                        //                            if(!fieldValue.startsWith("%")) {
//                        //                                fieldValue = "%"+fieldValue;
//                        //                            }
//                        fieldValue = "LIKE " + fieldValue;
//                        noSearchCriteriaEntered =false;
//                        AttributeBean attribute = new AttributeBean(
//                        ""+fieldIndex,fieldValue);
//                        searchExecution.addAttribute(column,attribute);
//                    }
//                }
//                searchExecution.addColumn(column);
//            }
            // Code commented for coeus 4.3 enhancements - ends
            // Code added for coeus 4.3 enhancements            
            boolean searchCriteriaEntered = SearchUtil.fillCriteria(searchExecution, searchInfoHolder, request);
            Hashtable searchResult = null ;
            if(!searchCriteriaEntered){
                ActionMessages actionMessages = new ActionMessages();
                actionMessages.add("noSearchCriteriaEntered", new ActionMessage("noSearchCriteriaEntered_exception"));
                saveMessages(request, actionMessages);
                return actionMapping.findForward("exception");
                 
            }else{
                request.setAttribute("fieldName",request.getParameter("fieldName"));
                //Added for COEUSQA-2428 : LIte - Prop Dev - Application does not return any rows while searching for Original proposal - Start
                //Replacing the 'COEUS' to the logged-in user id in the query 
                if(searchName !=null && searchName.equals("PROPOSALSEARCH")){
                    String clause = searchInfoHolder.getRemClause();
                    StringBuffer remClause = new StringBuffer(clause);
                    String tempClause = remClause.toString();
                    String newRemClause = Utils.replaceString( remClause.toString(),"COEUS", userId);
                    searchInfoHolder.setRemClause(newRemClause);
                }
                //Added for COEUSQA-2428_CLONE -LIte - Prop Dev - Application does not return any rows while searching for Original proposal_Start
                else if(searchName !=null && searchName.equals("INSTITUTEPROPOSALSEARCH")){
                    String clause = searchInfoHolder.getRemClause();
                    StringBuffer remClause = new StringBuffer(clause);
                    String tempClause = remClause.toString();
                    String newRemClause = Utils.replaceString( remClause.toString(),"COEUS", userId);
                    searchInfoHolder.setRemClause(newRemClause);
                }
                //Added for COEUSQA-2428_CLONE -LIte - Prop Dev - Application does not return any rows while searching for Original proposal_End
                searchResult = searchExecution.executeSearchQuery();
                //COEUSQA-2428 : End
            }
            if(searchResult!=null){
                request.setAttribute("searchResult",searchResult);
                session.setAttribute("resList",searchResult.get("reslist"));
                request.setAttribute("searchValue", "false");
                actionforward = actionMapping.findForward("result");
            }
          
            else{
                throw new CoeusException("exceptionCode.20005");
            }
            //String searchType = request.getParameter("type");
            String searchType=(String) session.getAttribute("type");
            if(searchType.equals("personSearch")){
                request.setAttribute("reqType",request.getParameter("reqType"));
                request.setAttribute("reqPage",request.getParameter("reqPage"));
                // actionforward = mapping.findForward("personSearch");
            }
         /*
          * if any exceptions were thrown then forward to FAILURE page
          * other wise forward to SUCCESS
          */
            if( errorFlag ) {
                actionforward = actionMapping.findForward( "failure" );
            }
            return actionforward;
            
        }
        return actionMapping.findForward("success");
    }
    
    private SearchInfoHolderBean getDisplayList(String searchReq) throws CoeusSearchException,CoeusException{
        ProcessSearchXMLBean processSearchXML = new ProcessSearchXMLBean("",searchReq);
        
        return processSearchXML.getSearchInfoHolder();
    }
    
    public void cleanUp() {
    }
    
    private void setSearchData(HttpServletRequest request,HashMap hmProposalData) throws Exception{
        HttpSession session = request.getSession();
        SearchModuleBean moduleBean = new SearchModuleBean();
        String mode = EMPTY_STRING;
        String proposalNumber = (String)hmProposalData.get("PROPOSAL_NUMBER");
        String unitNumber = (String)hmProposalData.get("UNIT_NUMBER");
        java.math.BigDecimal proposalStatus = (java.math.BigDecimal)hmProposalData.get("CREATION_CODE");
        String oldMode = (String)session.getAttribute("mode"+session.getId());
        oldMode = getMode(oldMode);
        moduleBean.setOldMode(oldMode);
        int value =  proposalStatus.intValue();
        // 1 -> InProgress, 2-> Rejected(creation status code)
        if(value!= 1 && value!= 3){
            mode = CoeusLiteConstants.DISPLAY_MODE;
//            session.setAttribute("mode", "display");
        }
        String unitName = (String)hmProposalData.get("UNIT_NAME");
        moduleBean.setModuleKey(CoeusLiteConstants.PROPOSAL_MODULE);
        moduleBean.setModuleNumber(proposalNumber);
        mode = getMode(mode);
        moduleBean.setMode(mode);
        String oldProposalNumber = (String)session.getAttribute("proposalNumber"+session.getId());
        if(oldProposalNumber!= null){
            moduleBean.setOldModuleNumber(oldProposalNumber);
        }else{
            moduleBean.setOldModuleNumber(EMPTY_STRING);
        }
        
        moduleBean.setUnitNumber(unitNumber);
        session.setAttribute(CoeusLiteConstants.PROPOSAL_SEARCH_ACTION+session.getId(),moduleBean);
    }    
    
}
              
    

