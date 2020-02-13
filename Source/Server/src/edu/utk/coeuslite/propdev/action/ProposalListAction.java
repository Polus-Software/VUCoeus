/*
 * ProposalListAction.java
 *
 * Created on May 3, 2006, 9:23 AM
 */

package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.search.bean.AttributeBean;
import edu.mit.coeus.search.bean.ColumnBean;
import edu.mit.coeus.search.bean.CriteriaBean;
import edu.mit.coeus.search.bean.ProcessSearchXMLBean;
import edu.mit.coeus.search.bean.SearchExecutionBean;
import edu.mit.coeus.search.bean.SearchInfoHolderBean;
import edu.mit.coeus.search.exception.CoeusSearchException;
import edu.mit.coeus.utils.Utils;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 *
 * @author  vinayks
 */
public class ProposalListAction extends ProposalBaseAction {   
    private static final String PROPOSAL_SUB_HEADER="proposalSubHeader";
    
    /** Creates a new instance of ProposalListAction */
    public ProposalListAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm, 
        HttpServletRequest request, HttpServletResponse response) throws Exception {
         Hashtable htProposalList =null ;
        HttpSession session = request.getSession();
        
      
        String proposalType = request.getParameter("PROPOSAL_TYPE");
        //PersonInfoBean personInfoBean = (PersonInfoBean)session.getAttribute("person");
        PersonInfoBean personInfoBean = (PersonInfoBean)session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = personInfoBean.getPersonID();
      
        getProposalMenus(request);
        String coeusHeaderId =  request.getParameter("Menu_Id");
        if(coeusHeaderId!=null) {
            setSelectedCoeusHeaderPath(coeusHeaderId, request);
        }
        String subHeaderId =  request.getParameter("SUBHEADER_ID");
        Vector headerData = (Vector) session.getAttribute(PROPOSAL_SUB_HEADER);
        if(subHeaderId!=null) {
           headerData = readSelectedPath(subHeaderId, headerData);
           session.setAttribute(PROPOSAL_SUB_HEADER, headerData);
        }        
        try{
            htProposalList = (Hashtable)getSearchResult(request,proposalType,personId);
            Vector columnData = (Vector)htProposalList.get("displaylabels");            
            Vector data = (Vector)htProposalList.get("reslist");
           // System.out.println(" PROPOSAL DATA >>>"+data);
            session.setAttribute("proposalColumnNames", columnData);
            session.setAttribute("proposalList", data);
            //String proposalName = (String)htProposalList.get("displayLable");
            //session.setAttribute("proposalName", proposalName);
        }catch(CoeusSearchException searchException){
            session.setAttribute("proposalList",new Vector());
             return actionMapping.findForward("success");
        }          
            return actionMapping.findForward("success");
    }    

    
    private Hashtable getSearchResult(HttpServletRequest request,String searchName,String personId)
            throws CoeusSearchException,CoeusException,DBException,Exception{
            
            HttpSession session = request.getSession();
            UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user"+session.getId());            
            String userId = (String)userInfoBean.getUserId();    
            
            
            Hashtable searchResult = null ;   
            SearchInfoHolderBean searchInfoHolder = null;
            ProcessSearchXMLBean processSearchXML =null;
            
            processSearchXML = new ProcessSearchXMLBean("",searchName);
            
            searchInfoHolder = processSearchXML.getSearchInfoHolder();
            SearchExecutionBean searchExecution = new SearchExecutionBean(searchInfoHolder);                      
            Vector criteriaList = searchInfoHolder.getCriteriaList();
         
            if(criteriaList == null) {
                criteriaList = new Vector();
                searchInfoHolder.setCriteriaList(criteriaList);
            }
             session.setAttribute("AllProposalSearchFlag","");
            if(searchName !=null && (!searchName.equals("ALL_PROPOSALS_SEARCH") && !searchName.equals("PROPOSAL_INPROGRESS"))){
                 CriteriaBean criteria = new CriteriaBean("OSP$EPS_PROP_INVESTIGATORS.PERSON_ID",null,"string",null,null,null, null);
                 criteriaList.addElement(criteria);                 
                 ColumnBean column = new ColumnBean("OSP$EPS_PROP_INVESTIGATORS.PERSON_ID");
                 String fieldValue = " = " + personId;
                 AttributeBean attribute = new AttributeBean("0",fieldValue);
                 column.addAttribute(attribute);
                 searchExecution.addColumn(column);                
            }
            if(searchName !=null && (searchName.equals("ALL_PROPOSALS_SEARCH") || searchName.equals("PROPOSAL_INPROGRESS"))){                
                // replacing hardcoded userid with loggedin user id.                           
                            String clause = searchInfoHolder.getRemClause(); 
                            StringBuffer remClause = new StringBuffer(clause);
                            String tempClause = remClause.toString();
                            String newRemClause = Utils.replaceString( remClause.toString(),"COEUS", userId);
                            searchInfoHolder.setRemClause(newRemClause);                
            }
            
          
            String proposalName = searchInfoHolder.getDisplayLabel();
            session.setAttribute("proposalName", proposalName);
            searchResult = searchExecution.executeSearchQuery();           
            searchResult.put("displayLable", searchInfoHolder.getDisplayLabel()); 
        return searchResult;
    }
}
