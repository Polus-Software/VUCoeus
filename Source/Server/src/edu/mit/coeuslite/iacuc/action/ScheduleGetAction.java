/*
 * ScheduleGetAction.java
 *
 * Created on March 17, 2009, 11:49 AM
 */

package edu.mit.coeuslite.iacuc.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.iacuc.bean.AgendaTxnBean;
import edu.mit.coeus.iacuc.bean.OtherActionInfoBean;
import edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import java.util.HashMap;
import java.util.Map;
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
 * @author Sreenath
 */

/* PMD check performed, and commented unused imports and variables on 16-JULY-2010
 * by George J Nirappeal
 */
public class ScheduleGetAction extends ScheduleBaseAction{
       
    
    private static final String FORWARD_IACUC_SCHEDULE_OTHER_ACTIONS = "/getIacucScheduleOtherActions";
    private static final String FORWARD_GET_IACUC_SUBMITTED = "/getIacucSubmitted";
    private static final String SCHEDULE_DOCUMENT_READER_CLASS = "edu.mit.coeus.iacuc.ScheduleDocumentReader";
    private static final String SESSION_SCHEDULE_ID= "IACUC_SCHEDULE_ID";
    private static final String SESSION_AGENDA_NUMBER = "IACUC_AGENDA_NUMBER";
    
    public ScheduleGetAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ActionForward actionForward = null;
        actionForward = fetchScheduleDetails(actionMapping, form, request,response);
        return actionForward;
    }
    
    public void cleanUp() {
    }
    
    private ActionForward fetchScheduleDetails( ActionMapping actionMapping,
            ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        String navigator ="success";
        //COEUSQA-2292 START SHABARISH.V
        String scheduleIdForAgenda = EMPTY_STRING;
        String agendaNumber = EMPTY_STRING;
        //COEUSQA-2292 END SHABARISH.V
        HttpSession session = request.getSession();
         UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        //COEUSQA-2292 START 
        AgendaTxnBean agendaTxnBean = new AgendaTxnBean((String)userInfoBean.getUserId());
        //COEUSQA-2292 END SHABARISH.V
        String scheduleId = (String) session.getAttribute(SESSION_SCHEDULE_ID+session.getId());
        ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean();
        HashMap mapMenuList = null;
        
        if(actionMapping.getPath().equalsIgnoreCase(FORWARD_IACUC_SCHEDULE_OTHER_ACTIONS)){
            
            mapMenuList = new HashMap();
            mapMenuList.put("menuItems",CoeusliteMenuItems.IACUC_SCHEDULE_MENU_ITEMS);
            mapMenuList.put("menuCode",CoeusliteMenuItems.SCHEDULE_OTHER_ACTIONS);
            setSelectedMenuList(request, mapMenuList);
            
            
            Vector vecOtherActions = scheduleMaintenanceTxnBean.getOtherActions(scheduleId);
            Vector vecActionsTypes = scheduleMaintenanceTxnBean.getScheduleActionTypes();
            HashMap hmOtherActions = new HashMap();
            if(vecOtherActions != null && vecOtherActions.size() >0){
                ComboBoxBean comboBoxBean;
                String actionTypeDesc = "";
                Vector vecOthAction = null;
                OtherActionInfoBean otherActionInfoBean;
                for(int indx = 0; indx < vecActionsTypes.size(); indx ++){
                    comboBoxBean = (ComboBoxBean) vecActionsTypes.elementAt(indx);
                    actionTypeDesc = comboBoxBean.getDescription();
                    vecOthAction = new Vector();
                    for(int index =0; index < vecOtherActions.size() ; index++){
                        
                        otherActionInfoBean = (OtherActionInfoBean)vecOtherActions.elementAt(index);
                        if(actionTypeDesc.equalsIgnoreCase(otherActionInfoBean.getScheduleActTypeDesc())){
                            vecOthAction.add(otherActionInfoBean);
                        }
                        hmOtherActions.put(actionTypeDesc,vecOthAction);
                    }
                    
                }
                
            }
            
            request.setAttribute("otherActions", hmOtherActions);
            request.setAttribute("actionTypes", vecActionsTypes);
        } else if(actionMapping.getPath().equalsIgnoreCase(FORWARD_GET_IACUC_SUBMITTED)){
            
            mapMenuList = new HashMap();
            mapMenuList.put("menuItems",CoeusliteMenuItems.IACUC_SCHEDULE_MENU_ITEMS);
            mapMenuList.put("menuCode",CoeusliteMenuItems.SCHEDULE_PROTOCOLS_SUBMITTED);
            setSelectedMenuList(request, mapMenuList);
        }
        /*
         *This part of the code will open agenda or displays alert message 
         *based on the schedule,whether it has agenda or not 
         */
        //COEUSQA-2292 START Agenda attachment available to reviewers from Lite
       if(request.getParameter("isAgenda")!=null && request.getParameter("isAgenda").equals("true")) {
            agendaNumber = agendaTxnBean.getMaxAgendaID(scheduleId);
            if(Integer.parseInt(agendaNumber)==0) {
                session.setAttribute(SESSION_AGENDA_NUMBER+session.getId(),agendaNumber);
                ActionMessages actionMessages = new ActionMessages();
                actionMessages.add("noAgendaDetails",new ActionMessage("schedule_no_agenda_details"));
                saveMessages(request, actionMessages);
            }
             if(Integer.parseInt(agendaNumber)==1) {
                session.setAttribute(SESSION_AGENDA_NUMBER+session.getId(),agendaNumber);
             }
            if(request.getParameter("openAgenda")!=null && request.getParameter("openAgenda").equals("true")) {
                
                if(Integer.parseInt(agendaNumber)>0){
                    String templateURL= viewDocument(scheduleIdForAgenda,agendaNumber,request);
                    session.setAttribute("url", templateURL);
                    response.sendRedirect(request.getContextPath()+templateURL);
                    return null;
                }
            }
       }else{
            session.removeAttribute(SESSION_AGENDA_NUMBER+session.getId());
       }
        
        ActionForward actionForward = actionMapping.findForward(navigator);
        return actionForward;
    }
    /*
     *This method is called if 
     *agenda number is 1
     */
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
        map.put(DocumentConstants.READER_CLASS, SCHEDULE_DOCUMENT_READER_CLASS);
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
     //COEUSQA-2292 END Agenda attachment available to reviewers from Lite
}
