/*
 * ScheduleBaseAction.java
 *
 * Created on March 18, 2009, 9:50 AM
 *
 */

package edu.mit.coeuslite.irb.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.irb.bean.ScheduleDetailsBean;
import edu.mit.coeus.irb.bean.ScheduleTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeuslite.irb.bean.ReadProtocolDetails;
import edu.mit.coeuslite.utils.CoeusBaseAction;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
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
public abstract class ScheduleBaseAction extends CoeusBaseAction{
    
    /** Creates a new instance of ScheduleBaseAction */
    public ScheduleBaseAction() {
    }
    
    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response){
        ActionForward forward = null;
        ActionForward fwd;
        boolean isAuthorizedToViewSchedule= false;
        try {
            fwd = authUser(actionMapping, request, actionForm);
            if(fwd!=null){
                return fwd;
            }
            HttpSession session = request.getSession();
            saveMessages(request,null);
            readScheduleMenu(request);
            readNavigationPath(request);
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            
            if(userInfoBean == null) {
                ActionForward actionForward = new ActionForward("/coeuslite/mit/irb/cwLogon.jsp");
                request.setAttribute(CoeusLiteConstants.SESSION_EXPIRED, CoeusLiteConstants.INVALID_SESSION);
                ActionMessages actionMessages = new ActionMessages();
                String errMsg = "session_ended_exceptionCode";
                actionMessages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(errMsg));
                saveMessages(request,actionMessages);
                return actionForward;
            }
            
            secondLevelHeaderPath(request);
            String scheduleId = EMPTY_STRING;
            ScheduleTxnBean scheduleTxnBean;;
            
            scheduleId = request.getParameter("scheduleId");
            if(scheduleId == null || "".equals(scheduleId)){
                scheduleId = (String) session.getAttribute("SCHEDULE_ID"+session.getId());
            }
            if(scheduleId != null && !"".equals(scheduleId)){
                session.removeAttribute("scheduleHeader"+session.getId());
                session.setAttribute("SCHEDULE_ID"+session.getId(),scheduleId);
                scheduleTxnBean = new ScheduleTxnBean();
                ScheduleDetailsBean scheduleDetailsBean = null ;
                try{
                    scheduleDetailsBean = scheduleTxnBean.getScheduleDetails(scheduleId);
                } catch(Exception ex){
                    ex.printStackTrace();
                    request.setAttribute("canViewSchedule","N");
                    return actionMapping.findForward("noRight");
                }
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                if(scheduleDetailsBean != null){
                    isAuthorizedToViewSchedule =
                            userMaintDataTxnBean.getUserHasRight(userInfoBean.getUserId(),"VIEW_SCHEDULE", scheduleDetailsBean.getHomeUnitNumber());;
                            if(isAuthorizedToViewSchedule){
                                session.setAttribute("scheduleHeader"+session.getId(), scheduleDetailsBean);
                            }
                }
            }
            if(isAuthorizedToViewSchedule){
                forward = performExecute(actionMapping,actionForm,request,response);
            } else{
                request.setAttribute("canViewSchedule","N");
                forward = actionMapping.findForward("noRight");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            request.setAttribute("Exception", exception);
            forward = actionMapping.findForward("failure");
        }
        
        return forward;
    }
    
    public abstract ActionForward performExecute(ActionMapping mapping, ActionForm form, HttpServletRequest req,
            HttpServletResponse res) throws Exception;
    
    private void readScheduleMenu(HttpServletRequest request) {
        
        Vector menuItemsVector  = null;
        HttpSession session = request.getSession();
        ReadProtocolDetails readProtocolDetails = new ReadProtocolDetails();
        menuItemsVector = (Vector) session.getAttribute("scheduleMenuItemsVector");
        
        if (menuItemsVector == null || menuItemsVector.size()==0) {
            menuItemsVector = readProtocolDetails.readXMLDataForMenu("/edu/mit/coeuslite/irb/xml/ScheduleMenu.xml");
            session.setAttribute("scheduleMenuItemsVector", menuItemsVector);
        }else{
            session.setAttribute("scheduleMenuItemsVector", menuItemsVector);
        }
    }

   
        protected void readNavigationPath(HttpServletRequest request) throws Exception{
            readNavigationPath(null,request);
    }
        protected void readNavigationPath(String subHeaderId, HttpServletRequest request) throws Exception{
            
            HttpSession session = request.getSession();
            Vector headerDetailsVec = (Vector)session.getAttribute("subHeaderVector"+session.getId());
            if (headerDetailsVec == null || headerDetailsVec.size()==0 ) {
                headerDetailsVec = readSelectedPath(subHeaderId,headerDetailsVec);
            }
            session.setAttribute("subHeaderVector"+session.getId(),headerDetailsVec);
            
        }
        
        protected void secondLevelHeaderPath(HttpServletRequest request) throws Exception{
            secondLevelHeaderPath(null,request);
        }
    
        protected void secondLevelHeaderPath(String subHeaderId, HttpServletRequest request) throws Exception{

            ReadProtocolDetails readProtocolDetails = new ReadProtocolDetails();
            HttpSession session = request.getSession();
            Vector vecSecondHeader = (Vector)session.getAttribute("headerItemsVector"+session.getId());
            if(vecSecondHeader ==  null || vecSecondHeader.size() == 0){
                // 3282: Reviewer View of Protocol materials - Start
              vecSecondHeader  = readProtocolDetails.readXMLDataForSubHeader("/edu/mit/coeuslite/irb/xml/ProtocolReviewerSubMenu.xml" );
//                vecSecondHeader  = readProtocolDetails.readXMLDataForSubHeader(subHeaderPath);
                // 3282: Reviewer View of Protocol materials - End
            }
            vecSecondHeader = readSelectedPath(subHeaderId,vecSecondHeader);
            session.setAttribute("slSubHeaderVector"+session.getId(), vecSecondHeader);
        }
    
}
