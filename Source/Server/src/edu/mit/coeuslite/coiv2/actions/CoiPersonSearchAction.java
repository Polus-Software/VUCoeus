/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
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
 * @author indhulekha
 */
public class CoiPersonSearchAction extends COIBaseAction {
    
    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    
    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */

    public ActionForward performExecuteCOI(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)  throws Exception {
        
         String strSearchType = request.getParameter("type");
        String searchName = request.getParameter("searchName");
        boolean search = new Boolean(request.getParameter("search")).booleanValue();
        String rowId = request.getParameter("actionPerformed");
        HttpSession session = request.getSession();
        request.getSession().removeAttribute("noQuestionnaireForModule");

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
            SearchInfoHolderBean searchInfoHolder =
                    (SearchInfoHolderBean)session.getAttribute("searchinfoholder");
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            String remClause = searchInfoHolder.getRemClause();
            if(remClause != null){
                String newRemClause = Utils.replaceString(
                                remClause,"COEUS",userInfoBean.getUserId().toUpperCase().trim());
                searchInfoHolder.setRemClause(newRemClause);
            }
            SearchExecutionBean searchExecution = new SearchExecutionBean(searchInfoHolder);
            boolean isSearchCriteriaEntered = SearchUtil.fillCriteria(searchExecution, searchInfoHolder, request);
            String disclosureSearch = request.getParameter("disclosureSearch");


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
                actionforward = mapping.findForward("error");
                //throw new CoeusException("exceptionCode.20005");
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
}
