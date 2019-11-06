/*
 * ProtocolListAction.java
 *
 * Created on March 10, 2005, 3:03 PM
 */

package edu.mit.coeuslite.irb.action;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.bean.OtherActionInfoBean;
import edu.mit.coeus.irb.bean.ScheduleAttachmentBean;
import edu.mit.coeus.irb.bean.ScheduleDetailsBean;
import edu.mit.coeus.irb.bean.ScheduleMaintenanceTxnBean;
import edu.mit.coeus.irb.bean.ScheduleTxnBean;
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
import edu.mit.coeuslite.irb.bean.ProtocolTxnBean;
import edu.mit.coeuslite.irb.bean.ReadProtocolDetails;
//start--1
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.SubHeaderBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
//end--1
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import edu.mit.coeus.irb.bean.AgendaTxnBean;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;

/**
 *
 * @author  vinayks
 */
public class ProtocolListAction extends ProtocolBaseAction{
    private static final String EMPTY_STRING = "";
    /** Creates a new instance of ProtocolListAction */
    private static final String MM_DD_YYYY = "MM/dd/yyyy";
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String CREATE_TIMESTAMP = "CREATE_TIMESTAMP";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String APPROVAL_DATE = "APPROVAL_DATE";
    private static final String APPLICATION_DATE = "APPLICATION_DATE";
    //Added for Coues4.3 Enhancement
    private static final String CHECK_PROTOCOL_RIGHT = "protocolRightChecking";
    private static final String PARAMETER_NAME = "PROTOCOL_CREATE_RIGHT_CHECK";
    private static final String GET_PARAMETER_VALUE = "getParameterValue";
    private static final String PROTOCOL_RIGHT = "CREATE_PROTOCOL";
    private static final String SUB_HEADER_PATH = "/edu/mit/coeuslite/irb/xml/ProtocolSubMenu.xml";
    // 3282: Reviewer View of Protocol materials - Start
    private static final String REVIEWER_SUB_HEADER_PATH = "/edu/mit/coeuslite/irb/xml/ProtocolReviewerSubMenu.xml";
    //COEUSQA:3333 - IRB and IACUC - Ability to Add Attachments to Minutes and Agenda - Start
    private static final int SCHEDULE_AGENDA_ATTACHMENTS = 1;
    //COEUSQA:3333 - End
    
    public ProtocolListAction() {
    // 3282: Reviewer View of Protocol materials - End
    }
    
    public ActionForward performExecute(ActionMapping mapping, ActionForm form, HttpServletRequest req,
    HttpServletResponse res) throws Exception{
        String protoType = EMPTY_STRING;
        String protoName = EMPTY_STRING;
        String navigator = EMPTY_STRING;
        String scheduleIdForAgenda = EMPTY_STRING;
        String agendaNumber = EMPTY_STRING;
        boolean isUserProtocolReviewer = false;
        HttpSession session = req.getSession();
        //modified for COEUSQA-2292 as req.getParameter("PROTOCOL_TYPE") was null
        if(req.getParameter("PROTOCOL_TYPE")!=null) {
            protoType = (req.getParameter("PROTOCOL_TYPE")).trim();
        }
        //protoName = req.getParameter("PROTOCOL_NAME");
        
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        //COEUSQA-2292 START 
        AgendaTxnBean agendaTxnBean = new AgendaTxnBean((String)userInfoBean.getUserId());
        //COEUSQA-2292 end
        PersonInfoBean personInfoBean = null;
        if(userInfoBean!=null){
            UserDetailsBean userDetailsBean = new UserDetailsBean();
            //Modified for COEUSDEV-236 : PI can't see protocol he/she created - Start
            //Gets the person details from get_person_for_user with userId as parameter
//            personInfoBean  = userDetailsBean.getPersonInfo(userInfoBean.getPersonId(),false);
            personInfoBean  = userDetailsBean.getPersonInfo(userInfoBean.getUserId());
            //COEUSDEV-236 : END
            // 3282: Reviewer View of Protocol materials - Start            
            String hasReviewerRight = (String) session.getAttribute(CoeusLiteConstants.USER_IS_REVIEWER+session.getId());
            if(CoeusLiteConstants.YES.equalsIgnoreCase(hasReviewerRight)){
                isUserProtocolReviewer = true;
            }else if(CoeusLiteConstants.NO.equalsIgnoreCase(hasReviewerRight)){
                isUserProtocolReviewer = false;
            }else{
                //Modified for COEUSDEV-303 : Review View menu items are not enabled if the user has reviewer role - Start
                //Checked for user has RIEVEW_PROTOCOL right in any unit 
//                isUserProtocolReviewer = isUserProtocolReviewer(req, userInfoBean.getUserId(), userInfoBean.getUnitNumber());
                  isUserProtocolReviewer = isUserProtocolReviewer(req, userInfoBean.getUserId());
               //COEUSDEV-303 : End
            }
            // 3282: Reviewer View of Protocol materials - End
        }
        //session.setAttribute("person", personInfoBean );
        //session.setAttribute(SessionConstants.LOGGED_IN_PERSON, personInfoBean );
       
        //COEUSQA-2292 START 
        if(req.getParameter("isAgenda")!=null && req.getParameter("isAgenda").equals("true")) {
            if(req.getParameter("scheduleId") != null){
                scheduleIdForAgenda = req.getParameter("scheduleId");
            }
            agendaNumber = agendaTxnBean.getMaxAgendaID(scheduleIdForAgenda);
            if(Integer.parseInt(agendaNumber)==0) {
                session.setAttribute("AGENDA_NUMBER"+session.getId(),agendaNumber);                
                ActionMessages actionMessages = new ActionMessages();
                actionMessages.add("noAgendaDetails",new ActionMessage("schedule_no_agenda_details"));
                saveMessages(req, actionMessages);
            }
             if(Integer.parseInt(agendaNumber)==1) {
                session.setAttribute("AGENDA_NUMBER"+session.getId(),agendaNumber);
             }
            if(req.getParameter("openAgenda")!=null && req.getParameter("openAgenda").equals("true")) {
                if(Integer.parseInt(agendaNumber)>0){                   
                    String templateURL= viewDocument(scheduleIdForAgenda,agendaNumber,req);
                    session.setAttribute("url", templateURL);
                    res.sendRedirect(req.getContextPath()+templateURL);
                    return null;
                }
            }
        }
        //COEUSQA:3333 - IRB and IACUC - Ability to Add Attachments to Minutes and Agenda - Start
         else if(req.getParameter("isAttach")!=null && req.getParameter("isAttach").equals("true")) {
            if(req.getParameter("scheduleId") != null){
                scheduleIdForAgenda = req.getParameter("scheduleId");
            }
            String attachmentId = req.getParameter("attachmentId");
            if(req.getParameter("openAttach")!=null && req.getParameter("openAttach").equals("true")) {
                if(Integer.parseInt(attachmentId)>0){
                    String templateURL= viewScheduleAttach(session, scheduleIdForAgenda, attachmentId);
                    session.setAttribute("url", templateURL);
                    res.sendRedirect(req.getContextPath()+templateURL);
                    return null;
                }
            }
        }
        //COEUSQA:3333 - End        
        else{
            session.removeAttribute("AGENDA_NUMBER"+session.getId());
       }
       
        //COEUSQA-2292 END
        ServletContext application = getServlet().getServletContext();
        Vector headerDetailsVec= null;
        String coeusHeaderId =  req.getParameter("Menu_Id");
        if(coeusHeaderId!=null) {
            setSelectedCoeusHeaderPath(coeusHeaderId, req);
        }
        // 4361: Rights checking issue in protocol in Lite - Start
        String hasViewRights =  req.getParameter("View_Right");
        if("N".equals(hasViewRights)){
            session.setAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId(),"noRights");
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("noDisplayAndEditRights",
                    new ActionMessage("protocol_DtlForm_no_displayEditRights"));
            saveMessages(req, actionMessages);
        }
        // 4361: Rights checking issue in protocol in Lite - End
        /*
         * Modified By Anand.T
         * Coeus4.3 Enhancement ProtocolSearch Customization - Start
         */
          MessageResources irbMessages = MessageResources.getMessageResources("coeus");
//        String subHeaderId =  req.getParameter("SUBHEADER_ID");
        String subHeaderId = "";
        if(protoType.equals("DEFAULT")) {
            // 3282: Reviewer view of Protocols - Start
            //Commented for COEUSDEV-300 : In IRB Lite, clicking My IRB protocols provides user with the wrong starting point - Start
            //To select Pending/In progress link when user clicked 'My IRB Protocol'
//            if(isUserProtocolReviewer){
//                // COEUSDEV-156: Investigator can add and modify review comments in IRB Lite 
////                subHeaderId = "SH001";
//                subHeaderId = "SH007";
//            // 3282: Reviewer view of Protocols - End
//            }else{
            //COEUSDEV-200 : End
                subHeaderId = irbMessages.getMessage("irb.protocolDefaultSearch");
//            }
        } else
            subHeaderId = req.getParameter("SUBHEADER_ID");
        //Coeus4.3 Enhancement ProtocolSearch Customization - End
//Commented for Protocol SubHeader 7/2/2007 - Start         
//        if(subHeaderId!=null) {
            readNavigationPath(subHeaderId,req);
            
            /* 
             * Modified By Anand.T
             * Coeus4.3 Enhancement - Start
             * Added for coes 4.3 enhancement for customizing the CreateProtocol Option
             */
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
            //Coeus4.3 Enhancement - End
//        }
//Commented for Protocol SubHeader 7/2/2007 - End  
        if(personInfoBean == null || personInfoBean.getPersonID()== null ||
        userInfoBean == null || userInfoBean.getUserId()== null || protoType==null){
            
        }else{
            String userId = userInfoBean.getUserId();
            String personId = personInfoBean.getPersonID();
            ProtocolTxnBean protocolTxnBean = new ProtocolTxnBean();
            
            //                Vector data = protocolTxnBean.getProtocolList(personId, protoType);
            try{
                /*
                 * Modified By Anand.T
                 * Coeus4.3 Enhancement - Start
                 * Added to customize the protocol default search
                 * Based on the prototype the default Protocol Search is defined.
                 */
                if( protoType.equals("DEFAULT")) {
                    String strSubHeaderId = irbMessages.getMessage("irb.protocolDefaultSearch");
                    // 3282: Reviewer view of Protocols - Start
                    //Commented for COEUSDEV-300 : In IRB Lite, clicking My IRB protocols provides user with the wrong starting point - Start
                    //To select Pending/In progress link when user clicked 'My IRB Protocol'
//                    if(isUserProtocolReviewer){
//                        // COEUSDEV-156: Investigator can add and modify review comments in IRB Lite 
////                        strSubHeaderId = "SH001";
//                        strSubHeaderId = "SH007";
//                    }
                    //COEUSDEV-300 : End
                    // 3282: Reviewer view of Protocols - End
                    if(strSubHeaderId.equals("NONE") ) 
                            protoType = strSubHeaderId;
                        else 
//                            protoType = getProtoTypeName(strSubHeaderId);
                            protoType = getProtoTypeName(strSubHeaderId, req);
                }
                if(protoType.equals("NONE")) {
                    return mapping.findForward("protocolsearch");
                } 
                //Coeus4.3 Enhancement - End
                //Modified for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start
                //Section is moved to setProtocolListToSession method
//                 Hashtable searchResult = getSearchResult(protoType,personId,req);
//                Vector columnData = (Vector)searchResult.get("displaylabels");
//                Vector vecProtocolList = (Vector)searchResult.get("reslist");
//                Vector vecNewProtocolList = new Vector();
//                Vector vecScheduleProtocols = new Vector();
//                HashMap hmSubmittedProtocolInfo = new HashMap();
//                ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean();
//                if(vecProtocolList != null && vecProtocolList.size()>0){
//                    for(int count=0;count<vecProtocolList.size();count++){
//                        HashMap HmdisplayMap =(HashMap) vecProtocolList.get(count);
//                        if("REVIEWER_SCHEDULE_SEARCH".equals(protoType)){
//                            vecScheduleProtocols = new Vector();
//                            String scheduleId = (String) HmdisplayMap.get("SCHEDULE_ID");
//                            //Added for case#3282/3284reviewer Views and comments -start
//                            //vecScheduleProtocols = scheduleMaintenanceTxnBean.fetchSubmittedProtocolsForSchedule(scheduleId);
//                            vecScheduleProtocols = scheduleMaintenanceTxnBean.fetchSubmittedProtocolsForReviewer(scheduleId,personId);
//                            //Added for case#3282/3284reviewer Views and comments -end
//                            hmSubmittedProtocolInfo.put(scheduleId, vecScheduleProtocols);
//                        }
//                        
//                        if(columnData != null && columnData.size()>0){
//                            for(int index=0;index<columnData.size();index++){
//                                DisplayBean displayBean = (DisplayBean)columnData.elementAt(index);
//                                if(!displayBean.isVisible())
//                                    continue;
//                                String key = displayBean.getName();
//                                if(key != null){
//                                    String value = HmdisplayMap.get(key) == null ? "" : HmdisplayMap.get(key).toString();
//                                    if(key.equals(CREATE_TIMESTAMP) || key.equals(EXPIRATION_DATE) || key.equals(APPROVAL_DATE)
//                                    || key.equals(APPLICATION_DATE)){
//                                        DateUtils date=new DateUtils();
//                                        value=date.restoreDate(value,DATE_SEPARATERS);
//                                        //Code commented for PT ID#2932 - date in yyyy/MM/dd format
//                                        //value = date.formatDate(value,DATE_SEPARATERS,MM_DD_YYYY);
//                                        HmdisplayMap.put(key, value);
//                                    }
//                                }
//                            }
//                        }
//                        vecNewProtocolList.add(HmdisplayMap);
//                    }
//                    req.setAttribute("schSubmittedProtocols", hmSubmittedProtocolInfo);
//                }
//                //Vector data = new Vector();
//                //Vector columnData = readProtocolDetails.readXMLData(protoType, "/edu/mitweb/coeus/irb/xml/data/ProtocolDetails.xml");
//                //req.setAttribute("protocolName", protoName);
//                session.setAttribute("protocolColumnNames", columnData);
//                session.setAttribute("protocolList", vecNewProtocolList);
//                protoName = (String)searchResult.get("displayLable");
//                
//                //start--2
//                session.setAttribute(CoeusLiteConstants.PROTOCOL_TYPE, protoType);
//                session.setAttribute(CoeusLiteConstants.PROTOCOL_NAME, protoName);
//                //end--2

                //REVIEWER_PROTOCOL_SEARCH search will do the following two searchs
                //REVIEWER_REVIEW_INCOMPLETED_PROTOCOL_SEARCH and REVIEWER_REVIEW_COMPLETED_PROTOCOL_SEARCH
                Hashtable searchResult = null;
                if("REVIEWER_PROTOCOL_SEARCH".equals(protoType)){
                    String reviewerReviewType[] = new String[2];
                    reviewerReviewType[0] = "REVIEWER_REVIEW_INCOMPLETED_PROTOCOL_SEARCH";
                    reviewerReviewType[1] = "REVIEWER_REVIEW_COMPLETED_PROTOCOL_SEARCH";
                    for(int index=0;index<reviewerReviewType.length;index++){
                        protoType = reviewerReviewType[index];
                        try{
                            searchResult = getSearchResult(protoType,personId,req);
                            setProtocolListToSession(searchResult,protoType,personId,req);
                        }catch(Exception ex){
                            session.removeAttribute(reviewerReviewType[index]);
                            session.removeAttribute(reviewerReviewType[index]+"_COLUMNS");
                        }
                    }
                }else{
                    searchResult = getSearchResult(protoType,personId,req);
                    setProtocolListToSession(searchResult,protoType,personId,req);
                }
                //COEUSQA-2288 : End
            }catch(Exception ListExp){
                session.removeAttribute("protocolList");
                session.removeAttribute("protocolColumnNames");
                //start--3
                // 3282: Reviewer View of Protocol materials - Start
                //Modified for COEUSDEV-300 : In IRB Lite, clicking My IRB protocols provides user with the wrong starting point - Start
//                 if(isUserProtocolReviewer){
//                    session.setAttribute(CoeusLiteConstants.PROTOCOL_NAME,"results for the selecion criteria ");
//                } else {
//                    session.setAttribute(CoeusLiteConstants.PROTOCOL_NAME,"Protocols No rows found with current selection criteria ");
//                }
//                // 3282: Reviewer View of Protocol materials - End
//                //end--3
//                if(isUserProtocolReviewer){
//                    if("REVIEWER_SCHEDULE_AGENDA_SEARCH".equals(protoType)){                        
//                        navigator = "agenda";
//                    } else if("REVIEWER_SCHEDULE_SEARCH".equals(protoType)){                        
//                        navigator = "scheduleList";                
//                    }else if("REVIEWER_PROTOCOL_SEARCH".equals(protoType)){                        
//                        navigator = "reviewerList";
//                    }else{
//                        navigator ="success";
//                    }
//                 }else{
//                    navigator ="success";
//                }   
                    if("REVIEWER_SCHEDULE_AGENDA_SEARCH".equals(protoType) && isUserProtocolReviewer ){    
                        session.setAttribute(CoeusLiteConstants.PROTOCOL_NAME,"results for the selecion criteria ");
                        navigator = "agenda";
                    } else if("REVIEWER_SCHEDULE_SEARCH".equals(protoType)&& isUserProtocolReviewer){                        
                        session.setAttribute(CoeusLiteConstants.PROTOCOL_NAME,"results for the selecion criteria ");
                        navigator = "scheduleList";                
                   //MODIFIED for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start
                    }else if(("REVIEWER_REVIEW_COMPLETED_PROTOCOL_SEARCH".equals(protoType)||
                        "REVIEWER_REVIEW_INCOMPLETED_PROTOCOL_SEARCH".equals(protoType)) && isUserProtocolReviewer){                        
                        session.setAttribute(CoeusLiteConstants.PROTOCOL_NAME,"results for the selecion criteria ");
                        navigator = "reviewerList";
                    } else if("SCHEDULE_ATTACHMENTS".equals(protoType)){                        
                        session.setAttribute(CoeusLiteConstants.PROTOCOL_NAME,"results for the selecion criteria ");
                        navigator = "schdAttachList";                                   
                    }else{
                        //session.setAttribute(CoeusLiteConstants.PROTOCOL_NAME,"Protocols No rows found with current selection criteria ");
                        navigator ="success";
                    }
                //COEUSDEV-300 : END
//                return mapping.findForward("success");
                return mapping.findForward(navigator);
                
            }
            
        }
            
//        return   mapping.findForward("success");
             //Modified for COEUSDEV-300 : In IRB Lite, clicking My IRB protocols provides user with the wrong starting point - Start
//            if(isUserProtocolReviewer){
                if("REVIEWER_SCHEDULE_AGENDA_SEARCH".equals(protoType) && isUserProtocolReviewer){
                    navigator = "agenda";
                } else if("REVIEWER_SCHEDULE_SEARCH".equals(protoType) && isUserProtocolReviewer){
                    String canViewSchedule = (String) req.getAttribute("canViewSchedule");
                    if("N".equals(canViewSchedule)){
                        ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("noViewRight",
                                new ActionMessage("scheduleAction_exceptionCode.4000"));
                        saveMessages(req, actionMessages);
                    }
                    
//                    if("true".equalsIgnoreCase((String)req.getParameter("agenda"))) {
//                         navigator ="isAgenda";
//                    }else 
                    navigator ="scheduleList";
                  //Added for the case#3282/3284-Reviewer Views and Comments: start  
//                }else{
//                    navigator = "reviewerList";
//                }
                    //Modified for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start
//                   }else if("REVIEWER_PROTOCOL_SEARCH".equals(protoType) && isUserProtocolReviewer){
//                    navigator = "reviewerList";
                }else if(("REVIEWER_REVIEW_COMPLETED_PROTOCOL_SEARCH".equals(protoType) ||
                    "REVIEWER_REVIEW_INCOMPLETED_PROTOCOL_SEARCH".equals(protoType)) && isUserProtocolReviewer){
                navigator = "reviewerList";
                //COEUSQA-2288 : End
                
                }
                //COEUSQA:3333 - IRB and IACUC - Ability to Add Attachments to Minutes and Agenda - Start
                else if("SCHEDULE_ATTACHMENTS".equals(protoType)){
                navigator = "schdAttachList";                
                }
                //COEUSQA:3333 - End
                else {
                navigator ="success";
                }
                  //Added for the case#3282/3284-Reviewer Views and Comments: -end
//            }else{
//                navigator ="success";
//            }
            //COEUSDEV-300 : End
            return mapping.findForward(navigator);
    }
    
    public void cleanUp() {
    }
    
    private Hashtable getSearchResult(String searchName,String personId,HttpServletRequest req) throws CoeusSearchException,CoeusException,DBException,Exception{
        ProcessSearchXMLBean processSearchXML = new ProcessSearchXMLBean("",searchName);
        SearchInfoHolderBean searchInfoHolder = processSearchXML.getSearchInfoHolder();
        SearchExecutionBean searchExecution = new SearchExecutionBean(searchInfoHolder);
        Vector columns = new Vector(3,2);
        String[] fieldValues = null;
        HttpSession session = req.getSession();
        UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user"+session.getId());
        String userId = (String)userInfoBean.getUserId();
        Vector criteriaList = searchInfoHolder.getCriteriaList();
        if(criteriaList == null) {
            criteriaList = new Vector();
            searchInfoHolder.setCriteriaList(criteriaList);
        }
        if(searchName !=null && (!searchName.equals("ACTIVEPROTOCOLSEARCH") && !searchName.equals("ALL_PROTO_SEARCH") && !searchName.equals("PENDINGPROTOCOLSEARCH")
        // 3282: Reviewer View of Protocol materials - Start
//        && !searchName.equals("ACTIVE_SUBMISSION_PROTO_SEARCH")  && !searchName.equals("AMENDS_RENEWALS_PROTO_SEARCH")  )){
         && !searchName.equals("ACTIVE_SUBMISSION_PROTO_SEARCH")  && !searchName.equals("AMENDS_RENEWALS_PROTO_SEARCH") &&
         //Modified for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start
//         && !searchName.equals("REVIEWER_PROTOCOL_SEARCH") && !searchName.equals("REVIEWER_SCHEDULE_SEARCH")  && !searchName.equals("REVIEWER_SCHEDULE_AGENDA_SEARCH"))){
                !searchName.equals("REVIEWER_REVIEW_COMPLETED_PROTOCOL_SEARCH") &&
                !searchName.equals("REVIEWER_REVIEW_INCOMPLETED_PROTOCOL_SEARCH") &&
                !searchName.equals("REVIEWER_SCHEDULE_SEARCH")  &&
                !searchName.equals("REVIEWER_SCHEDULE_AGENDA_SEARCH") &&
                //COEUSQA:3333 - IRB and IACUC - Ability to Add Attachments to Minutes and Agenda - Start
                !searchName.equals("SCHEDULE_ATTACHMENTS"))){
                //COEUSQA:3333 - End
            //COEUSQA-2288 : End
         // 3282: Reviewer View of Protocol materials - End   
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
        searchName.equals("PENDINGPROTOCOLSEARCH") || searchName.equals("ACTIVE_SUBMISSION_PROTO_SEARCH") || searchName.equals("AMENDS_RENEWALS_PROTO_SEARCH") )){
            // replacing hardcoded userid with loggedin user id.
            // searchBean = searchInfoHolder.getSearchInfoHolder();
            String clause = searchInfoHolder.getRemClause();
            StringBuffer remClause = new StringBuffer(clause);
            String tempClause = remClause.toString();
            String newRemClause = Utils.replaceString( remClause.toString(),"COEUS", userId);
            searchInfoHolder.setRemClause(newRemClause);
        }
        // 3282: Reviewer View of Protocol materials - Start
        //Modified for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start
//        if(searchName !=null && (searchName.equals("REVIEWER_PROTOCOL_SEARCH") || searchName.equals("REVIEWER_SCHEDULE_SEARCH"))){
        if(searchName !=null && (searchName.equals("REVIEWER_REVIEW_COMPLETED_PROTOCOL_SEARCH") ||
                searchName.equals("REVIEWER_REVIEW_INCOMPLETED_PROTOCOL_SEARCH") ||
                searchName.equals("REVIEWER_SCHEDULE_SEARCH"))){
        //COEUSQA-2288 : end
            String clause = searchInfoHolder.getRemClause();
            StringBuffer remClause = new StringBuffer(clause);
            String tempClause = remClause.toString();
            String newRemClause = Utils.replaceString( remClause.toString(),"COEUS", userId);
            searchInfoHolder.setRemClause(newRemClause);
        }
        if(searchName !=null&&  searchName.equals("REVIEWER_SCHEDULE_AGENDA_SEARCH")){
           
            String scheduleId = req.getParameter("scheduleId");
            if(scheduleId !=  null && !"".equals(scheduleId)){
                session.setAttribute("SCHEDULE_ID"+session.getId(), scheduleId);
            }
            String clause = searchInfoHolder.getRemClause();
            StringBuffer remClause = new StringBuffer(clause);
            String tempClause = remClause.toString();
            String newRemClause = Utils.replaceString( remClause.toString(),"PARAM_SCHEDULE_ID", scheduleId);
            newRemClause =  Utils.replaceString( newRemClause,"COEUS", userId);
            searchInfoHolder.setRemClause(newRemClause);
        }
        // 3282: Reviewer View of Protocol materials - End
        //COEUSQA:3333 - IRB and IACUC - Ability to Add Attachments to Minutes and Agenda - Start
        if(searchName !=null&&  searchName.equals("SCHEDULE_ATTACHMENTS")){
            String scheduleId = req.getParameter("scheduleId");
            if(scheduleId !=  null && !"".equals(scheduleId)){
                session.setAttribute("SCHEDULE_ID"+session.getId(), scheduleId);
            }
            String clause = searchInfoHolder.getRemClause();
            StringBuffer remClause = new StringBuffer(clause);
            String tempClause = remClause.toString();
            String newRemClause = Utils.replaceString( remClause.toString(),"PARAM_SCHEDULE_ID", scheduleId);
            searchInfoHolder.setRemClause(newRemClause);
        }
        //COEUSQA:3333 - End
        String protocolName = searchInfoHolder.getDisplayLabel();
        session.setAttribute("protocolName", protocolName);
        Hashtable searchResult = searchExecution.executeSearchQuery();        
        searchResult.put("displayLable", searchInfoHolder.getDisplayLabel());        
        return searchResult;
    }
    
    /* 
     * Modified By Anand.T
     * Coeus4.3 Enhancement - Start
     * Added for coes 4.3 enhancement for customizing the CreateProtocol Option
     */
    /**
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

    /**
     * This method is to get the Parameter Value for a particular Parameter
     * @throws Exception
     * @return String Parameter Value
     */
    private String getParameterValue(HttpServletRequest request)throws Exception{
        Map mpParameterName = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        mpParameterName.put("parameterName",PARAMETER_NAME);
        Hashtable htParameterValue = 
            (Hashtable)webTxnBean.getResults(request, GET_PARAMETER_VALUE, mpParameterName );
        HashMap hmParameterValue = (HashMap)htParameterValue.get(GET_PARAMETER_VALUE);
        String moduleId = (String)hmParameterValue.get("parameterValue");
        return moduleId ;
    }
    
    /**
     * This method is to get the Prototype Name for a particular SubheaderId
     * @throws Exception
     * @return String Prototyoe Name
     */
    // 3282: Reviewer view of Protocols
//    private String getProtoTypeName(String subHeaderId) throws Exception {
    private String getProtoTypeName(String subHeaderId, HttpServletRequest request) throws Exception {
        String protoTypeLink = EMPTY_STRING;
        // 3282: Reviewer View of Protocol materials - Start
        String subHeaderPath = EMPTY_STRING;
        UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("user"+request.getSession().getId());        
        boolean isUserProtocolReviewer = checkReviewerRight(request, userInfoBean);
        if(isUserProtocolReviewer){
            subHeaderPath = REVIEWER_SUB_HEADER_PATH;
        }else{
            subHeaderPath = SUB_HEADER_PATH;
        }
        // 3282: Reviewer View of Protocol materials - End
        ReadProtocolDetails objReadProtocolDetails = new ReadProtocolDetails();
        SubHeaderBean subHeaderBean = new SubHeaderBean();
        // 3282: Reviewer View of Protocol materials - Start
//        Vector subHeaderDetailsVec = objReadProtocolDetails.readXMLDataForSubHeader(SUB_HEADER_PATH);
        Vector subHeaderDetailsVec = objReadProtocolDetails.readXMLDataForSubHeader(subHeaderPath);
        // 3282: Reviewer View of Protocol materials - End
        for(int index = 0; index < subHeaderDetailsVec.size(); index++) {
            subHeaderBean = (SubHeaderBean)subHeaderDetailsVec.get(index);
            if(subHeaderId.equals(subHeaderBean.getSubHeaderId()))
                protoTypeLink = subHeaderBean.getSubHeaderLink();
        }
        String protoTypeName = protoTypeLink.substring(protoTypeLink.indexOf("=")+1, protoTypeLink.indexOf("&"));
        return protoTypeName;
    }
    //Coeus4.3 Enhancement - End
    
    //COEUSQA-2292 START SHABARISH.V
     private String viewDocument(String scheduleIdForAgenda,String agendaID,HttpServletRequest request) throws Exception{
         
        DocumentBean documentBean = new DocumentBean();
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user"+session.getId());
        StringBuffer stringBuffer = new StringBuffer();
        
        String userId = (String)userInfoBean.getUserId();
        Map map = new HashMap();
        map.put("DOCUMENT_TYPE", "VIEW_AGENDA");
        map.put("USER_ID", userId);
        map.put("SCHEDULE_ID", scheduleIdForAgenda);
        map.put("AGENDA_ID", agendaID);
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.irb.ScheduleDocumentReader");
        //COEUSQA:3333 - IRB and IACUC - Ability to Add Attachments to Minutes and Agenda - Start
        map.put("addBookmark",request.getParameter("addBookmark"));
        //COEUSQA:3333 - End
        documentBean.setParameterMap(map);       
        String docId = DocumentIdGenerator.generateDocumentId();
        
        stringBuffer.append("/StreamingServlet");
        stringBuffer.append("?");
        stringBuffer.append(DocumentConstants.DOC_ID);
        stringBuffer.append("=");
        stringBuffer.append(docId);
        
        request.getSession().setAttribute(docId, documentBean);
        
        return stringBuffer.toString();
    }
     //COEUSQA-2292 END SHABARISH.V
     //Added for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start
     /*
      * Method to exacute the search in CoeusSearch.xml file and result to the session
      * @param searchResult - Hashtable
      * @param protoType - String
      * @param personId - String
      * @param request - HttpServletRequest
      */
     public void setProtocolListToSession(Hashtable searchResult,String protoType,String personId,
             HttpServletRequest request)throws CoeusException,DBException{
         HttpSession session = request.getSession();
         Vector columnData = (Vector)searchResult.get("displaylabels");
         Vector vecProtocolList = (Vector)searchResult.get("reslist");
         Vector vecNewProtocolList = new Vector();
         Vector vecScheduleProtocols = new Vector();
         HashMap hmSubmittedProtocolInfo = new HashMap();
         ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean();
         if(vecProtocolList != null && vecProtocolList.size()>0){
             for(int count=0;count<vecProtocolList.size();count++){
                 HashMap HmdisplayMap =(HashMap) vecProtocolList.get(count);
                 if("REVIEWER_SCHEDULE_SEARCH".equals(protoType)){
                     vecScheduleProtocols = new Vector();
                     String scheduleId = (String) HmdisplayMap.get("SCHEDULE_ID");
                     vecScheduleProtocols = scheduleMaintenanceTxnBean.fetchSubmittedProtocolsForReviewer(scheduleId,personId);
                     hmSubmittedProtocolInfo.put(scheduleId, vecScheduleProtocols);
                 }
                 
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
                                 HmdisplayMap.put(key, value);
                             }
                         }
                     }
                 }
                 vecNewProtocolList.add(HmdisplayMap);
             }
             request.setAttribute("schSubmittedProtocols", hmSubmittedProtocolInfo);
         }
         if("REVIEWER_REVIEW_COMPLETED_PROTOCOL_SEARCH".equals(protoType) ||
                 "REVIEWER_REVIEW_INCOMPLETED_PROTOCOL_SEARCH".equals(protoType)){
             session.setAttribute(protoType, vecNewProtocolList);
             session.setAttribute(protoType+"_COLUMNS", columnData);
             String protoName = (String)searchResult.get("displayLable");
             session.setAttribute(CoeusLiteConstants.PROTOCOL_TYPE, protoType);
             session.setAttribute(CoeusLiteConstants.PROTOCOL_NAME, protoName);
               
         }else{
             session.setAttribute("protocolColumnNames", columnData);
             session.setAttribute("protocolList", vecNewProtocolList);
             String protoName = (String)searchResult.get("displayLable");
             session.setAttribute(CoeusLiteConstants.PROTOCOL_TYPE, protoType);
             session.setAttribute(CoeusLiteConstants.PROTOCOL_NAME, protoName);
         }
     }
     //COEUSQA-2288 : end
     
     //COEUSQA:3333 - IRB and IACUC - Ability to Add Attachments to Minutes and Agenda - Start
     /**
      *  Method to view the Schedule Attachment
      *
      * @param session
      * @param scheId
      * @param attachmentId
      * @throws java.lang.Exception
      * @return
      */
     private String viewScheduleAttach(final HttpSession session, String scheduleId, String attachmentId)
     throws Exception{
         
         Vector vecUpldData = null;
         ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean
                 = new ScheduleMaintenanceTxnBean();
         vecUpldData = scheduleMaintenanceTxnBean.getScheduleAttachments(scheduleId);
         
         ScheduleAttachmentBean scheduleAttachmentBean = null;
         if(vecUpldData != null && !vecUpldData.isEmpty()){
             for(Object attach : vecUpldData) {
                 scheduleAttachmentBean = (ScheduleAttachmentBean)attach;
                 if( (scheduleId.equals(scheduleAttachmentBean.getScheduleId())) && (attachmentId.equals(String.valueOf(scheduleAttachmentBean.getAttachmentId()))) ){
                     break;
                 }
             }
         }
         DocumentBean documentBean = new DocumentBean();
         Map map = new HashMap();
         map.put("SCHEDELE_DETAILS_BEAN", scheduleAttachmentBean);
         map.put("DOCUMENT_TYPE", "SCHEDULE_ATTACHMENT_DOC");
         map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.irb.ProtocolDocumentReader");
         documentBean.setParameterMap(map);
         String docId = DocumentIdGenerator.generateDocumentId();
         StringBuffer stringBuffer = new StringBuffer();
         stringBuffer.append("/StreamingServlet");
         stringBuffer.append("?");
         stringBuffer.append(DocumentConstants.DOC_ID);
         stringBuffer.append("=");
         stringBuffer.append(docId);
         session.setAttribute(docId, documentBean);
         return stringBuffer.toString();
     }
     //COEUSQA:3333 - End
}
