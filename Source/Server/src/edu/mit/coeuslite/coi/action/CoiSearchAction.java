/*
 * coiSearchAction.java
 *
 * Created on 20 January 2006, 20:18
 */

package edu.mit.coeuslite.coi.action;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.sql.Date;
import java.sql.SQLException;

import javax.naming.*;
import javax.servlet.http.*;
import javax.servlet.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import edu.mit.coeus.bean.*;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.user.bean.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.search.bean.AttributeBean;
import edu.mit.coeus.search.bean.ColumnBean;
import edu.mit.coeus.search.bean.CriteriaBean;
import edu.mit.coeus.search.bean.ProcessSearchXMLBean;
import edu.mit.coeus.search.bean.SearchExecutionBean;
import edu.mit.coeus.search.bean.SearchInfoHolderBean;
import edu.mit.coeus.search.exception.CoeusSearchException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.irb.form.OrganizationSearchForm;
import edu.mit.coeuslite.irb.action.ProtocolBaseAction;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;



public class CoiSearchAction extends ProtocolBaseAction {
    /**
     * No argument constructor.
     */
    
    private Log myLog;
    private String actionToken;
    public CoiSearchAction() {
        //myLog = LogFactory.getLog(OrganizationSearchAction.class);
    }
    
   
    public ActionForward performExecute(ActionMapping mapping, ActionForm form, 
        HttpServletRequest request, HttpServletResponse response) throws Exception {      
        
        String strSearchType = request.getParameter("type");
        String searchName = request.getParameter("searchName");
        boolean search = new Boolean(request.getParameter("search")).booleanValue();
        String rowId = request.getParameter("actionPerformed");
        HttpSession session = request.getSession();
        Vector orgVector = new Vector();
        String propNum = "";
        if(rowId != null) {
            Vector resList = (Vector)session.getAttribute("resList");
            java.util.HashMap hashmap = (java.util.HashMap)resList.get(Integer.parseInt(rowId));
            request.setAttribute("getRowData", resList.get(Integer.parseInt(rowId)));
             String searchType=(String) session.getAttribute("type");
                 if(searchType.equals("personSearch")){
                     return mapping.findForward("personSearch");
                }else
                {    
                    return mapping.findForward("success");
                }    
        }
        if(search) {            
            ProcessSearchXMLBean processSearchXML = new ProcessSearchXMLBean(
            "",searchName);
            session.setAttribute("searchinfoholder",processSearchXML.getSearchInfoHolder());
            session.setAttribute("displayList",getDisplayList(searchName).getCriteriaList());
            request.setAttribute("search", "true");
            session.setAttribute("type", strSearchType);
            actionToken = "success";
            ActionForward actionforward = mapping.findForward( "success" );
            actionforward = mapping.findForward( searchName );
        }
        if(!search) {
            final String SEARCH_NAME = "searchname";            
            ActionForward actionforward = mapping.findForward( "success" );
            boolean errorFlag = false;
            boolean validResult = false;	// the result of valid award/proposal
            ActionErrors actionErrors = new ActionErrors(); // to hold any validation errors
                session = request.getSession( true );
                SearchInfoHolderBean searchInfoHolder =
                (SearchInfoHolderBean)session.getAttribute("searchinfoholder");
                SearchExecutionBean searchExecution = new SearchExecutionBean(searchInfoHolder);
                Vector columns = new Vector(3,2);
                String[] fieldValues = null;
                Vector criteriaList = searchInfoHolder.getCriteriaList();
                int criteriaCount = criteriaList.size();
                boolean noSearchCriteriaEntered =true;
                for(int criteriaIndex=0;criteriaIndex<criteriaCount;criteriaIndex++){
                    CriteriaBean criteria = (CriteriaBean)criteriaList.get(criteriaIndex);
                    String criteriaName = criteria.getName().trim();
                    fieldValues = request.getParameterValues(criteriaName);
                    ColumnBean column = new ColumnBean(criteriaName);
                    for(int fieldIndex=0;fieldIndex<fieldValues.length;fieldIndex++){
                        String fieldValue = fieldValues[fieldIndex];
                        
                        if(fieldValue!=null && !fieldValue.trim().equals("")){
                            fieldValue = fieldValue.replace('*','%');
//                            if(!fieldValue.startsWith("%")) {
//                                fieldValue = "%"+fieldValue;
//                            }
                            fieldValue = "LIKE " + fieldValue;
                            noSearchCriteriaEntered =false;
                            AttributeBean attribute = new AttributeBean(
                            ""+fieldIndex,fieldValue);
                            searchExecution.addAttribute(column,attribute); 
                        }
                    }
                    searchExecution.addColumn(column);
                }
                
                 Hashtable searchResult = null ;
                if(noSearchCriteriaEntered){
                    ActionMessages actionMessages = new ActionMessages();
                    actionMessages.add("noSearchCriteriaEntered", new ActionMessage("noSearchCriteriaEntered_exception"));
                    saveMessages(request, actionMessages);
                    return mapping.findForward("exception"); 

                }else{
                    request.setAttribute("fieldName",request.getParameter("fieldName"));
                    searchResult = searchExecution.executeSearchQuery();
                }                            
                
                if(searchResult!=null){
                    request.setAttribute("searchResult",searchResult);
                    session.setAttribute("resList",searchResult.get("reslist"));
                    request.setAttribute("searchValue", "false");
                    actionforward = mapping.findForward("result");
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
                    actionforward = mapping.findForward( "failure" );
                } 
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
