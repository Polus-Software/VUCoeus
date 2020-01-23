/*
 * PendingPIListAction.java
 *
 * Created on February 21, 2007, 2:49 PM
 */

package edu.mit.coeuslite.irb.action;

/**
 *
 * @author  divyasusendran
 */

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.search.bean.AttributeBean;
import edu.mit.coeus.search.bean.ColumnBean;
import edu.mit.coeus.search.bean.CriteriaBean;
import edu.mit.coeus.search.bean.DisplayBean;
import edu.mit.coeus.search.bean.ProcessSearchXMLBean;
import edu.mit.coeus.search.bean.SearchExecutionBean;
import edu.mit.coeus.search.bean.SearchInfoHolderBean;
import edu.mit.coeus.search.exception.CoeusSearchException;
import edu.mit.coeus.utils.Utils;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;




public class PendingPIListAction extends ProtocolBaseAction{
    private static final String EMPTY_STRING = "";
    private static final String MM_DD_YYYY = "MM/dd/yyyy";
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String CREATE_TIMESTAMP = "CREATE_TIMESTAMP";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String APPROVAL_DATE = "APPROVAL_DATE";
    private static final String APPLICATION_DATE = "APPLICATION_DATE";
    private static final String PENDING_PI_ACTION_SMR_RQD_PROTO_SEARCH = "PENDING_PI_ACTION_SMR_RQD_PROTO_SEARCH";
    private static final String PENDING_PI_ACTION_SR_RQD_PROTO_SEARCH = "PENDING_PI_ACTION_SR_RQD_PROTO_SEARCH";
    private static final String PENDING_PI_ACTION_EXPIRE_PROTO_SEARCH = "PENDING_PI_ACTION_EXPIRE_PROTO_SEARCH";
    //for customizing the CreateProtocol Option
    private static final String CHECK_PROTOCOL_RIGHT = "protocolRightChecking";
    private static final String PARAMETER_NAME = "PROTOCOL_CREATE_RIGHT_CHECK";
    private static final String GET_PARAMETER_VALUE = "getParameterValue";
    private static final String PROTOCOL_RIGHT = "CREATE_PROTOCOL";
    private static final String SUB_HEADER_PATH = "/edu/mit/coeuslite/irb/xml/ProtocolSubMenu.xml";
    public PendingPIListAction() {
    }
    
    public ActionForward performExecute(ActionMapping mapping, ActionForm form, HttpServletRequest req,
    HttpServletResponse res) throws Exception{
        String protoType = EMPTY_STRING;
        HttpSession session = req.getSession();
        protoType = (req.getParameter("PROTOCOL_TYPE")).trim();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        PersonInfoBean personInfoBean = null;
        if(userInfoBean!=null){
            UserDetailsBean userDetailsBean = new UserDetailsBean();
            //Modified for COEUSDEV-236 : PI can't see protocol he/she created - Start
            //Gets the person details from get_person_for_user with userId as parameter
            personInfoBean  = userDetailsBean.getPersonInfo(userInfoBean.getUserId());
            //COEUSDEV-236 : End
        }
        String coeusHeaderId =  req.getParameter("Menu_Id");
        if(coeusHeaderId!=null) {
            setSelectedCoeusHeaderPath(coeusHeaderId, req);
        }
        String subHeaderId = "";
        /* Coeus4.3 Enhancement ProtocolSearch Customization - Start
         */
          MessageResources irbMessages = MessageResources.getMessageResources("coeus");
        if(protoType.equals("DEFAULT")) {
            subHeaderId = irbMessages.getMessage("irb.protocolDefaultSearch");
        } else
            subHeaderId = req.getParameter("SUBHEADER_ID");
        //Coeus4.3 Enhancement ProtocolSearch Customization - End
            secondLevelHeaderPath(subHeaderId,req);
            String irbValues = irbMessages.getMessage("PROTOCOL_CREATE_RIGHT_CHECK_ENABLED");
            if(irbValues != null && irbValues.equals("1")) {
                int strResult = checkProtocolRight(req);
                if(strResult == 1)
                    session.setAttribute(CHECK_PROTOCOL_RIGHT,"YES");
                else
                    session.setAttribute(CHECK_PROTOCOL_RIGHT,"NO");
            } else {
                if(session.getAttribute(CHECK_PROTOCOL_RIGHT) != null) 
                    session.removeAttribute(CHECK_PROTOCOL_RIGHT);
            }
//        String subHeaderId =  req.getParameter("SUBHEADER_ID");
//        secondLevelHeaderPath(subHeaderId,req);
            String personId = personInfoBean.getPersonID();
            personId = personId != null ?personId : EMPTY_STRING;
            Vector vecColData = new Vector();
            HashMap hmProtoName =  new HashMap();
            session.removeAttribute("protocolColumnNames");
            session.removeAttribute(PENDING_PI_ACTION_SMR_RQD_PROTO_SEARCH);
            session.removeAttribute(PENDING_PI_ACTION_SR_RQD_PROTO_SEARCH);
            session.removeAttribute(PENDING_PI_ACTION_EXPIRE_PROTO_SEARCH);
            protoType = PENDING_PI_ACTION_SMR_RQD_PROTO_SEARCH ;
            getResult(vecColData,hmProtoName,protoType,personId,req);
            protoType = PENDING_PI_ACTION_SR_RQD_PROTO_SEARCH ;
            getResult(vecColData,hmProtoName,protoType,personId,req);
            protoType = PENDING_PI_ACTION_EXPIRE_PROTO_SEARCH ;
            getResult(vecColData,hmProtoName,protoType,personId,req);
            secondLevelHeaderPath(subHeaderId,req);
        return   mapping.findForward("success");
    }
    
    public void cleanUp() {
    }
    
    /** This method is used to parse the respective XML file for a particular
     *search,build the query,execute it and  set and return the result in a HashTable
     */
    private Hashtable getSearchResults(String searchName,String personId,HttpServletRequest req) throws CoeusSearchException,CoeusException,DBException,Exception{
        ProcessSearchXMLBean processSearchXML = new ProcessSearchXMLBean("",searchName);
        // searchInfoHolderBean is used to hold all the information for a particular search
        SearchInfoHolderBean searchInfoHolder = processSearchXML.getSearchInfoHolder();
        /*SearchExecutionBean is  used to build the query,execute it and parse the result set into 
         a hashtable searchResult*/
        SearchExecutionBean searchExecution = new SearchExecutionBean(searchInfoHolder);
        //Vector columns = new Vector(3,2);
        //String[] fieldValues = null;
        HttpSession session = req.getSession();
        UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user"+session.getId());
        String userId = (String)userInfoBean.getUserId();
        Vector criteriaList = searchInfoHolder.getCriteriaList();
        if(criteriaList == null) {
            criteriaList = new Vector();
            searchInfoHolder.setCriteriaList(criteriaList);
        }
        //here the query is built according to the searchName, executed and the result is returned
        if(searchName !=null && (!searchName.equals("ACTIVEPROTOCOLSEARCH") && !searchName.equals("ALL_PROTO_SEARCH") && !searchName.equals("PENDINGPROTOCOLSEARCH")
        && !searchName.equals("ACTIVE_SUBMISSION_PROTO_SEARCH")  && !searchName.equals("AMENDS_RENEWALS_PROTO_SEARCH") && !searchName.equals("PENDING_PI_ACTION_SMR_RQD_PROTO_SEARCH")
        && !searchName.equals("PENDING_PI_ACTION_SR_RQD_PROTO_SEARCH") && !searchName.equals("PENDING_PI_ACTION_EXPIRE_PROTO_SEARCH"))){
            CriteriaBean criteria = new CriteriaBean("OSP$PROTOCOL_INVESTIGATORS.PERSON_ID",null,"string",null,null,null, null);
            criteriaList.addElement(criteria);
            ColumnBean column = new ColumnBean("OSP$PROTOCOL_INVESTIGATORS.PERSON_ID");
            String fieldValue = " = " + personId;
            AttributeBean attribute = new AttributeBean(
            "0",fieldValue);
            column.addAttribute(attribute);
            searchExecution.addColumn(column);
        }
        if(searchName !=null && (searchName.equals("ACTIVEPROTOCOLSEARCH") || searchName.equals("ALL_PROTO_SEARCH") ||
        searchName.equals("PENDINGPROTOCOLSEARCH") || searchName.equals("ACTIVE_SUBMISSION_PROTO_SEARCH") || searchName.equals("AMENDS_RENEWALS_PROTO_SEARCH")
        || searchName.equals("PENDING_PI_ACTION_SMR_RQD_PROTO_SEARCH")
        || searchName.equals("PENDING_PI_ACTION_SR_RQD_PROTO_SEARCH")
        || searchName.equals("PENDING_PI_ACTION_EXPIRE_PROTO_SEARCH") )){
            String clause = searchInfoHolder.getRemClause();
            StringBuffer remClause = new StringBuffer(clause);
            String newRemClause = Utils.replaceString( remClause.toString(),"COEUS", userId);
            searchInfoHolder.setRemClause(newRemClause);
        }
        
        Hashtable searchResult = searchExecution.executeSearchQuery();
        searchResult.put("displayLable", searchInfoHolder.getDisplayLabel());
        return searchResult;
    }
    
    

    /*
     *This method is used to display the List of protocols
     */
    private void getResult(Vector vecColData,HashMap hmProtoName,
    String protoType,String personId,HttpServletRequest req)throws
    CoeusSearchException,CoeusException,DBException,Exception{
        String protoName = EMPTY_STRING;
        Vector vecNewProtocolList = new Vector();
        boolean isSuccessful = false;
        HttpSession session = req.getSession();
        try{
            Hashtable searchResult = getSearchResults(protoType,personId,req);
            Vector columnData = (Vector)searchResult.get("displaylabels");
            Vector vecProtocolList = (Vector)searchResult.get("reslist");
            if(vecProtocolList != null && vecProtocolList.size()>0){
                for(int count=0;count<vecProtocolList.size();count++){
                    HashMap HmdisplayMap =(HashMap) vecProtocolList.get(count);
                    if(columnData != null && columnData.size()>0){
                        for(int index=0;index<columnData.size();index++){
                            DisplayBean displayBean = (DisplayBean)columnData.elementAt(index);
                            if(!displayBean.isVisible())
                                continue;
                            String key = displayBean.getName();
                            if(key != null){
                                String value = HmdisplayMap.get(key) == null ? "" : HmdisplayMap.get(key).toString();
                                if(key.equals(CREATE_TIMESTAMP) || key.equals(EXPIRATION_DATE) || key.equals(APPROVAL_DATE)
                                || key.equals(APPLICATION_DATE)){
                                    DateUtils date=new DateUtils();
                                    //Commented for COEUSQA-1477 Dates in Search Results
                                    //value=date.restoreDate(value,DATE_SEPARATERS);
                                    //Code commented for PT ID#2932 - date in yyyy/MM/dd format
                                    //value = date.formatDate(value,DATE_SEPARATERS,MM_DD_YYYY);
                                    HmdisplayMap.put(key, value);
                                }
                            }
                        }
                    }
                    vecColData.add(columnData);
                    vecNewProtocolList.add(HmdisplayMap);
                }
            }
            protoName = (String)searchResult.get("displayLable");
            hmProtoName.put(protoType,protoName);
            isSuccessful = true;
        }catch(Exception ListExp){
            isSuccessful = false;
            session.removeAttribute(protoType);
        }
        if(isSuccessful){
            session.setAttribute("protocolColumnNames", vecColData);
            session.setAttribute(protoType, vecNewProtocolList);
            session.setAttribute(CoeusLiteConstants.PROTOCOL_NAME, hmProtoName);
        }
    }   
    
    /*  Added for customizing the CreateProtocol Option
     * Checks whether the loggedin user has the right to create a new protocol or not
     */
    public int checkProtocolRight(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String loggedinUser = userInfoBean.getUserId();
        WebTxnBean webTxnBean = new WebTxnBean(); 
        HashMap hmDetails = new HashMap();
        hmDetails.put("userid", loggedinUser);
        hmDetails.put("rightid", PROTOCOL_RIGHT);
        Hashtable htProtocolRightChecking=(Hashtable)webTxnBean.getResults(request, "checkProtocolRight",hmDetails );
        HashMap hmProtocolRight = (HashMap)htProtocolRightChecking.get("checkProtocolRight");
        int intResult = Integer.parseInt(hmProtocolRight.get("protocolResult").toString());
        return intResult;
    }
    
}
