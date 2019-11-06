/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.award.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author vineetha
 */
import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.utils.Utils;
import java.util.Vector;
import java.util.Hashtable;
import javax.servlet.http.*;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import edu.mit.coeus.bean.*;

import edu.mit.coeus.exception.*;
import edu.mit.coeus.search.bean.AttributeBean;
import edu.mit.coeus.search.bean.ColumnBean;
import edu.mit.coeus.search.bean.ProcessSearchXMLBean;
import edu.mit.coeus.search.bean.SearchExecutionBean;
import edu.mit.coeus.search.bean.SearchInfoHolderBean;
import edu.mit.coeus.search.exception.CoeusSearchException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeuslite.award.formbeans.AllAwardRecordFormBean;
import edu.mit.coeuslite.utils.SearchUtil;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class awardSearchAction extends  COIBaseAction{
    public awardSearchAction() {
        //myLog = LogFactory.getLog(OrganizationSearchAction.class);
    }


    public ActionForward performExecuteCOI(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
       String strSearchType = request.getParameter("type");
        String searchName = request.getParameter("searchName");
        boolean search = new Boolean(request.getParameter("search")).booleanValue();
        String rowId = request.getParameter("actionPerformed");
        HttpSession session = request.getSession();

        if(rowId != null) {
            Vector resList = (Vector)session.getAttribute("resList");
            request.setAttribute("getRowData", resList.get(Integer.parseInt(rowId)));
            return mapping.findForward("success");
        }
        if(search){

            ProcessSearchXMLBean processSearchXML = new ProcessSearchXMLBean(
                    "",searchName);
            session.setAttribute("searchinfoholder",processSearchXML.getSearchInfoHolder());
            session.setAttribute("displayList",getDisplayList(searchName).getCriteriaList());
            request.setAttribute("search", "true");
            session.setAttribute("type", strSearchType);
            ActionForward actionforward = mapping.findForward( "success" );
            actionforward = mapping.findForward( searchName );
        }
        if(!search) {
            ActionForward actionforward = mapping.findForward( "success" );
            boolean errorFlag = false;
            session = request.getSession( true );
            SearchInfoHolderBean searchInfoHolder =(SearchInfoHolderBean)session.getAttribute("searchinfoholder");            
            SearchExecutionBean searchExecution = new SearchExecutionBean(searchInfoHolder);
            boolean isSearchCriteriaEntered = SearchUtil.fillCriteria(searchExecution, searchInfoHolder, request);
            


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
