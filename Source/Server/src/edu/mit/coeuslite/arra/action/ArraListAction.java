/*
 * ArraListAction.java
 *
 * Created on August 11, 2009, 10:45 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeuslite.arra.action;

import edu.mit.coeus.arra.bean.ArraReportBean;
import edu.mit.coeus.arra.bean.ArraReportTxnBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.search.bean.ProcessSearchXMLBean;
import edu.mit.coeus.search.bean.SearchExecutionBean;
import edu.mit.coeus.search.bean.SearchInfoHolderBean;
import edu.mit.coeus.search.exception.CoeusSearchException;
import edu.mit.coeus.utils.Utils;
import edu.mit.coeus.utils.dbengine.DBException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.batik.dom.util.HashTable;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
/**
 *
 * @author keerthyjayaraj
 */
public class ArraListAction extends ArraBaseAction{
    
    private static final String GET_CLOSED_REPORTS = "/getClosedReports";
    public static final String EMPTY_STRING = "";
    /** Creates a new instance of ArraListAction */
    public ArraListAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
//        getArraMenus(request);
        String navigator = EMPTY_STRING;
        String coeusHeaderId =  request.getParameter("Menu_Id");
        if(coeusHeaderId!=null) {
            setSelectedCoeusHeaderPath(coeusHeaderId, request);
        }
        if(actionMapping.getPath().equals(GET_CLOSED_REPORTS)){
            navigator = getClosedreports(request);
        }else{
            try{
                ArraReportBean startAndEndDateForArra = new ArraReportBean();
                startAndEndDateForArra = getPeriodsForArra();
                request.setAttribute("startAndEndDate",startAndEndDateForArra);
                Hashtable htArraList = (Hashtable)getSearchResult(request,"ARRA_AWARD_SEARCH");
                Vector columnData = (Vector)htArraList.get("displaylabels");
                Vector data = (Vector)htArraList.get("reslist");
                request.setAttribute("arraColumnNames", columnData);
                request.setAttribute("arraList", data);
                request.setAttribute("displayLabel",htArraList.get("displayLabel"));
                navigator = "success";
            }catch(CoeusSearchException searchException){
                request.setAttribute("arraList",new Vector());
                return actionMapping.findForward("success");
            }
        }
        return actionMapping.findForward(navigator);
    }
    
    private Hashtable getSearchResult(HttpServletRequest request,String searchName)
            throws CoeusSearchException,CoeusException,DBException,Exception{
            
            HttpSession session = request.getSession();
            UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user"+session.getId());            
            String userId = (String)userInfoBean.getUserId();    
            
            
            Hashtable searchResult = null ;   
            SearchInfoHolderBean searchInfoHolder = null;
            ProcessSearchXMLBean processSearchXML =null;
            
            processSearchXML = new ProcessSearchXMLBean("",searchName);
            
            searchInfoHolder = processSearchXML.getSearchInfoHolder();
            String clause = searchInfoHolder.getRemClause();
            StringBuffer sbClause = new StringBuffer(clause);
            String newRemClause = Utils.replaceString( sbClause.toString(),"COEUS", userId);
            searchInfoHolder.setRemClause(newRemClause);
            
            clause = searchInfoHolder.getQueryString();
            sbClause = new StringBuffer(clause);
            String newSelect = Utils.replaceString( sbClause.toString(),"COEUS", userId);
            searchInfoHolder.setQueryString(newSelect);
            
            SearchExecutionBean searchExecution = new SearchExecutionBean(searchInfoHolder);                      
            Vector criteriaList = searchInfoHolder.getCriteriaList();
         
            if(criteriaList == null) {
                criteriaList = new Vector();
                searchInfoHolder.setCriteriaList(criteriaList);
            }
            
            searchResult = searchExecution.executeSearchQuery();           
            searchResult.put("displayLabel", searchInfoHolder.getDisplayLabel()); 
        return searchResult;
    }
    private ArraReportBean getPeriodsForArra() throws CoeusException, DBException{
        ArraReportBean periodForArra = new ArraReportBean();
        ArraReportTxnBean arraReportTxnBean = new ArraReportTxnBean();
        periodForArra = arraReportTxnBean.getStartAndEndDatePeriodOfArra();
        return periodForArra;
    }
    
    private String getClosedreports(HttpServletRequest request) throws CoeusException, DBException, Exception{
        String navigator = "success";
          try{
                Hashtable htArraList = (Hashtable)getSearchResult(request,"ARRA_CLOSED_REPORT_SEARCH");
                Vector columnData = (Vector)htArraList.get("displaylabels");
                Vector data = (Vector)htArraList.get("reslist");
                request.setAttribute("arraColumnNames", columnData);
                request.setAttribute("arraList", data);
                request.setAttribute("displayLabel",htArraList.get("displayLabel"));
                navigator = "success";
            }catch(CoeusSearchException searchException){
                request.setAttribute("arraList",new Vector());
                navigator = "success";;
            }
        return navigator;
    }
    
}
