/*
 * @(#)NegotiationListAction.java 1.0 July 2, 2009, 2:55 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeuslite.negotiation.action;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.search.bean.ProcessSearchXMLBean;
import edu.mit.coeus.search.bean.SearchExecutionBean;
import edu.mit.coeus.search.bean.SearchInfoHolderBean;
import edu.mit.coeus.search.exception.CoeusSearchException;
import edu.mit.coeus.utils.Utils;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.*;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author satheeshkumarkn
 * @version
 */
public class NegotiationListAction extends NegotiationBaseAction {
        private static final String SUB_HEADER = "negotiationSubHeaderVector";
        private static final String PARAMETER_NAME = "NEGOTIATION_SEARCH_ACTIVE_STATUS";
        private static final String GET_PARAMETER_VALUE = "getParameterValue";
        
    
    public NegotiationListAction() {
    }
    
    public ActionForward performExecuteNegotiation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
    HttpServletResponse response) throws Exception{
        javax.servlet.ServletContext application = getServlet().getServletConfig().getServletContext();
        String coeusHeaderId =  request.getParameter("Menu_Id");
        if(coeusHeaderId!=null) {
            setSelectedCoeusHeaderPath(coeusHeaderId, request);
        }
        String subHeaderId =  request.getParameter("SUBHEADER_ID");
        Vector headerData = (Vector) application.getAttribute(SUB_HEADER);
        if(subHeaderId!=null) {
            headerData = readSelectedPath(subHeaderId, headerData);
            application.setAttribute(SUB_HEADER, headerData);
        }
        //COEUSQA-119 : View negotiations in lite - Start
        // String negotiationType = "NEGOTIATION_LIST_SEARCH";
        String negotiationType = request.getParameter("NEGOTIATION_TYPE");
        //COEUSQA-119 : End
        try{
        Hashtable searchResult = getSearchResult(negotiationType,request); // Search Data
        Vector columnData = (Vector)searchResult.get("displaylabels");
        Vector vecNegotiationList = (Vector)searchResult.get("reslist");
        request.setAttribute("negotiationColumnNames", columnData);
        request.setAttribute("negotiationList", vecNegotiationList);
        }catch(CoeusSearchException searchException){
            request.setAttribute("negotiationList",new Vector());
            return mapping.findForward("success");
        }
      return mapping.findForward("success");
    }
    /*To get the search result for negotiation*/
     private Hashtable getSearchResult(String searchName,HttpServletRequest request) throws CoeusSearchException,CoeusException,DBException,Exception{
        ProcessSearchXMLBean processSearchXML = new ProcessSearchXMLBean("",searchName);
        SearchInfoHolderBean searchInfoHolder = processSearchXML.getSearchInfoHolder();
        SearchExecutionBean searchExecution = new SearchExecutionBean(searchInfoHolder);
        String[] fieldValues = null;
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user"+session.getId());
        String userId = (String)userInfoBean.getUserId();
        Vector criteriaList = searchInfoHolder.getCriteriaList();
        PersonInfoBean personInfoBean = new PersonInfoBean();
        if(userInfoBean!=null){
            UserDetailsBean userDetailsBean = new UserDetailsBean();
            personInfoBean  = userDetailsBean.getPersonInfo(userId);
        }
        if(criteriaList == null) {
            criteriaList = new Vector();
            searchInfoHolder.setCriteriaList(criteriaList);
        }
        if(searchName !=null && (searchName.equals("NEGOTIATION_LIST_SEARCH") || searchName.equals("NEGOTIATION_ACTIVE_SEARCH"))){
            String clause = searchInfoHolder.getRemClause();
            StringBuffer remClause = new StringBuffer(clause);
            String newRemClause = null;
            newRemClause = Utils.replaceString( remClause.toString(),"COEUS_PERSON", personInfoBean.getPersonID());
            searchInfoHolder.setRemClause(newRemClause);
            
            //COEUSQA-119 : View negotiations in lite - Added to replace NEGOTIATION_SEARCH_ACTIVE_STATUS value to the query - Start
            String negotiationActiveStatusCode = null;
            Map mpParameterName = new HashMap();
            WebTxnBean webTxnBean = new WebTxnBean();
            mpParameterName.put("parameterName",PARAMETER_NAME);
            Hashtable htParameterValue =
                    (Hashtable)webTxnBean.getResults(request, GET_PARAMETER_VALUE, mpParameterName );
            HashMap hmParameterValue = (HashMap)htParameterValue.get("getParameterValue");
            negotiationActiveStatusCode = (String)hmParameterValue.get("parameterValue");
            
            newRemClause = Utils.replaceString(
                    searchInfoHolder.getRemClause(),"NEGOTIATION_SEARCH_ACTIVE_STATUS",negotiationActiveStatusCode);
            searchInfoHolder.setRemClause(newRemClause);
            //COEUSQA-119 : End
        }
        Hashtable searchResult = searchExecution.executeSearchQuery();
        searchResult.put("displayLables", searchInfoHolder.getDisplayLabel());
        return searchResult;
    }
    
    
}
