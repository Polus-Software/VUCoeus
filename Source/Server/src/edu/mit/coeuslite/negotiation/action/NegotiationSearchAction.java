/*
 * NegotiationSearchAction.java
 *
 * Created on December 30, 2009, 10:27 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeuslite.negotiation.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.search.bean.ProcessSearchXMLBean;
import edu.mit.coeus.search.bean.SearchExecutionBean;
import edu.mit.coeus.search.bean.SearchInfoHolderBean;
import edu.mit.coeus.search.exception.CoeusSearchException;
import edu.mit.coeus.utils.Utils;
import edu.mit.coeuslite.utils.SearchUtil;
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

/**
 *
 * @author suganyadevipv
 */
public class NegotiationSearchAction extends NegotiationBaseAction{
    private static final String SUCCESS = "success";
    
    /** Creates a new instance of NegotiationSearchAction */
    public NegotiationSearchAction() {
    }

    public ActionForward performExecuteNegotiation(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        
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
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            String userId = userInfoBean.getUserId();
            
            String newRemClause = Utils.replaceString(
                    searchInfoHolder.getRemClause().toString(),"COEUS",userId.toUpperCase().trim());
            searchInfoHolder.setRemClause(newRemClause);
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
