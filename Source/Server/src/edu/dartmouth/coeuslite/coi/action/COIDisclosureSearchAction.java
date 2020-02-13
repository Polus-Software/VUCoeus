/*
 * @(#)COIDisclosureSearchAction.java 1.0 February 23, 2009, 1:02 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.dartmouth.coeuslite.coi.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.search.bean.AttributeBean;
import edu.mit.coeus.search.bean.ColumnBean;
import edu.mit.coeus.search.bean.ProcessSearchXMLBean;
import edu.mit.coeus.search.bean.SearchExecutionBean;
import edu.mit.coeus.search.bean.SearchInfoHolderBean;
import edu.mit.coeus.search.exception.CoeusSearchException;
import edu.mit.coeus.utils.Utils;
import edu.mit.coeuslite.utils.SearchUtil;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.*;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 *
 * @author satheeshkumarkn
 * @version
 */
public class COIDisclosureSearchAction extends COIBaseAction {
    private static final String SUCCESS = "success";
    /** Creates a new instance of COIDisclosureSearchAction */
    public COIDisclosureSearchAction() {
    }
    
    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an ActionForward instance describing where and how control
     * should be forwarded, or null if the response has already been completed.
     *
     * <br>The method fetches disclosure based on the person HOME_UNIT
     * <br><b>Validation</b>
     * <li> It will check whether the entity has projects using getEntityProjects method
     * @param mapping The ActionMapping used to select this instance
     * @actionForm The optional ActionForm bean for this request (if any)
     * @request The HTTP request we are processing
     * @response The HTTP response we are creating
     *
     * @throws java.io.IOException if an input/output error occurs
     * @throws javax.servlet.ServletException if a servlet exception occurs.
     */
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String strSearchType = request.getParameter("COI_DISCL_TYPE");
        String searchName = request.getParameter("searchName");
        boolean search = new Boolean(request.getParameter("search")).booleanValue();
        
        String rowId = request.getParameter("actionPerformed");
        
        if(rowId != null) {
            Vector resList = (Vector)session.getAttribute("resList");

            request.setAttribute("getRowData", resList.get(Integer.parseInt(rowId)));
            return actionMapping.findForward(SUCCESS);
        }
        if(search){
            
            ProcessSearchXMLBean processSearchXML = new ProcessSearchXMLBean(
                    "",searchName);
            session.setAttribute("searchinfoholder",processSearchXML.getSearchInfoHolder());
            session.setAttribute("displayList",getDisplayList(searchName).getCriteriaList());
            request.setAttribute("search", "true");
            session.setAttribute("COI_DISCL_TYPE", strSearchType);
            ActionForward actionforward = actionMapping.findForward( SUCCESS );
            actionforward = actionMapping.findForward(searchName);
        }
        if(!search) {
            ActionForward actionforward = actionMapping.findForward( SUCCESS );
            boolean errorFlag = false;

            session = request.getSession( true );
            SearchInfoHolderBean searchInfoHolder =
                    (SearchInfoHolderBean)session.getAttribute("searchinfoholder");
            SearchExecutionBean searchExecution = new SearchExecutionBean(searchInfoHolder);

            boolean isSearchCriteriaEntered = SearchUtil.fillCriteria(searchExecution, searchInfoHolder, request);
            String disclosureSearch = "true";//request.getParameter("disclosureSearch");
            if(disclosureSearch != null && disclosureSearch.equals("true")){
                UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
                String fieldValue = " = " + userInfoBean.getPersonId();
                AttributeBean attribute = new AttributeBean(
                        ""+0,fieldValue);
                ColumnBean column = new ColumnBean("OSP$PRESON.PERSON_ID");
                column.addAttribute(attribute);
                searchExecution.addColumn(column);
                
            }
            if(searchName.equals("disclosureSearch")){
                request.setAttribute("reqType",request.getParameter("reqType"));
                request.setAttribute("reqPage",request.getParameter("reqPage"));
            }
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            String userId = userInfoBean.getUserId();
            String query = searchInfoHolder.getQueryString();
            StringBuffer queryString = new StringBuffer(query);
            String newQueryString = Utils.replaceString(queryString.toString(),"COEUS", userId);
            searchInfoHolder.setQueryString(newQueryString);
            
            String clause = searchInfoHolder.getWhereClause();
            StringBuffer whereClause = new StringBuffer(clause);
            String newWhereClause = Utils.replaceString(whereClause.toString(),"COEUS", userId);
            searchInfoHolder.setWhereClause(newWhereClause);
            
            
            Hashtable searchResult = null ;
            if(!isSearchCriteriaEntered){
                ActionMessages actionMessages = new ActionMessages();
                actionMessages.add("noSearchCriteriaEntered", new ActionMessage("noSearchCriteriaEntered_exception"));
                saveMessages(request, actionMessages);
                return actionMapping.findForward("failure");
            }else{
                request.setAttribute("fieldName",request.getParameter("fieldName"));
                searchResult = searchExecution.executeSearchQuery();
          }
            if(searchResult!=null){
                session.setAttribute("searchResult",searchResult);
                session.setAttribute("resList",searchResult.get("reslist"));
                request.setAttribute("searchValue", "false");
                actionforward = actionMapping.findForward("result");
            } else{
                throw new CoeusException("exceptionCode.20005");
            }
            
            
            if( errorFlag ) {
                actionforward = actionMapping.findForward( "failure" );
            }
            return actionforward;
        }
        return actionMapping.findForward(SUCCESS);
    }
    
    /*
     * Method to get display label list
     * @return SearchInfoHolderBean
     */
    private SearchInfoHolderBean getDisplayList(String searchReq) throws CoeusSearchException,CoeusException{
        ProcessSearchXMLBean processSearchXML = new ProcessSearchXMLBean("",searchReq);
        
        return processSearchXML.getSearchInfoHolder();
    }
    
}