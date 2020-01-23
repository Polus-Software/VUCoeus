/*
 * UserAuthAction.java
 *
 * Created on November 1, 2006, 11:41 AM
 */
/*
 * PMD check performed, and commented unused imports and variables on 07-MAR-2011
 * by Maharaja Palanichamy
 */

package edu.mit.coeuslite.utils.auth;

import edu.mit.coeuslite.utils.CoeusBaseAction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 *
 * @author  geot
 */
public class UserAuthAction  extends CoeusBaseAction{

    /** Creates a new instance of UserAuthAction */
    public UserAuthAction() {
    }
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm, 
        HttpServletRequest request, HttpServletResponse response) throws Exception {
       //All authentication done at CoeusBaseAction.
       //This class is used to forward the request to main page
       return actionMapping.findForward("success");
    }
    protected ActionForward authUser(ActionMapping actionMapping,HttpServletRequest req,
            ActionForm actionForm) throws Exception {
       req.getSession().setAttribute("AUTH_ACTION", getAuthenticationActionName());
       //COEUSDEV-657 ARRA - locking error - Start
       //The request parameter is checked for value and if it is set then session attribute "reason"
       //is set to "sessionExpired"
       if(req.getParameter("reason")!=null && "sessionExpired".equals(req.getParameter("reason"))){
            req.getSession().setAttribute("reason", "sessionExpired");
       }
       //COEUSDEV-657 ARRA - locking error - End
       return super.authUser(actionMapping, req, actionForm);
    }
    protected String getAuthenticationActionName() {
        return "/userAuthAction.do";
    }
}
