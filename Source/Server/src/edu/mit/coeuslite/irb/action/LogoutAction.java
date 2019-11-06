/*
 * LogoutAction.java
 *
 * Created on May 26, 2005, 11:41 AM
 */

package edu.mit.coeuslite.irb.action;

import edu.mit.coeus.user.auth.CoeusAuthServiceFactory;
import edu.mit.coeus.user.auth.bean.AuthXMLNodeBean;
import edu.mit.coeus.utils.AuthenticationLogger;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeuslite.utils.CoeusBaseAction;
import javax.servlet.http.*;
import org.apache.struts.action.*;

/**
 *
 * @author  nadhgj
 */
public class LogoutAction extends CoeusBaseAction{
    
    /** Creates a new instance of LogoutAction */
    public LogoutAction() {
    }
    
    public ActionForward performExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        //do nothing
        return null;
    }
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        try{
            prepareLockRelease(request);
            String authAction = (String)session.getAttribute("AUTH_ACTION");
            if(session != null) {
                //JIRA COEUSQA 2527 - START
                AuthenticationLogger authLog = new AuthenticationLogger();
                authLog.logout(session.getId());
                //JIRA COEUSQA 2527 - END
                //invalidates the session for this user
                session.removeAttribute("user"+session.getId());
                session.invalidate();
            }
            AuthXMLNodeBean nodeBean = CoeusAuthServiceFactory.getCoeusAuthDetails(getLoginMode());
            if(nodeBean.hasLoginScreen()){
                ActionForward logonAF = new ActionForward(authAction);//mapping.findForward(authAction.substring(1,authAction.indexOf(".do")));
//                String loginPage = nodeBean.getAuthProps().getProperty("LOGIN_PAGE");
//                ActionForward logonAF = (loginPage==null)?
//                            mapping.findForward("success"):
//                            new ActionForward(loginPage);
                return logonAF;
            }
            ActionForward forward = new ActionForward(CoeusProperties.getProperty(
                        CoeusPropertyKeys.COEUS_HOME_URL),true);
            return forward;
        }catch(Exception ex){
            ex.printStackTrace();
            request.setAttribute("Exception", ex);
            return mapping.findForward("failure");
        }
    }
    
    public void cleanUp() {
    }
    
}

