/*
 * @(#) ChangePasswordAction.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeuslite.common.action;

import edu.mit.coeuslite.common.form.ChangePasswordForm;
import edu.mit.coeuslite.utils.CoeusBaseAction;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;

import edu.mit.coeus.bean.ChangePasswordBean;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;
/* CASE #748 Begin */
import edu.mit.coeus.utils.CoeusConstants;
/* CASE #748 End */

/**
 * Extend Struts Action class, to change Coeus user's password.
 * @author Coeus Dev Team
 * @version $Revision:   1.2  $ $Date:   Aug 12 2002 15:13:56  $
 */
/* CASE #748 Extend CoeusActionBase */
public class ChangePasswordAction extends CoeusBaseAction
{	/**
     * Entered newPassword value.
     */
	private String newPassword;

    /**
     * Data from OSP$USER.USER_ID.  For schools using Kerberos, this will be
     * the Kerberos principal.
     */
    private String userId;

	public ChangePasswordAction(){}

    /**
     * Perform Coeus user password change in Coeus database, based on user validation via certificate.
     * @param mapping The ActionMapping object associated with this Action.
     * @param form The ActionForm object associated with this Action.  Value
     * is set in struts-config.xml.
     * @param req The HttpServletRequest we are processing.
     * @param res HttpServletResponse
     * @return ActionForward object. ActionServlet.processActionPerform() will
     * use this to forward to the specified JSP or servlet.
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward performExecute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
	{   System.out.println("begin ChangePasswordAction.perform()");
        HttpSession session = request.getSession(true);
        /* Check for userId in session object.  If it does not exist, forward
            to login page, setting requestedURL as this action class. */
        userId = (String)session.getAttribute("userId");
        System.out.println("UserId: " + userId);
        /* CASE #748 Comment Begin */
        /*if(userId == null)
        {   request.setAttribute("requestedURL", "changePassword.do");
            return mapping.findForward("login");
        }*/
        /* CASE #748 Comment End */
        /* CASE #748 Begin */
        if(userId == null)
        {   request.setAttribute("requestedURL", "changePassword.do");
            request.setAttribute("validationType", CoeusConstants.LOGIN_KEY);
            return mapping.findForward(CoeusConstants.LOGIN_KEY);
        }
        /* CASE #748 End */
//        UtilFactory UtilFactory = new UtilFactory();
        ChangePasswordForm pwdForm = (ChangePasswordForm)form;
        newPassword = pwdForm.getNewPassword();
        userId = (String)session.getAttribute("userId");
        try
        {   ChangePasswordBean changePasswordBean = new ChangePasswordBean();
            changePasswordBean.changePassword(userId, newPassword);
        }
        catch(CoeusException CEx)
        {   UtilFactory.log(CEx.getMessage(), CEx, "ChangePasswordAction", "perform");
            request.setAttribute("EXCEPTION", CEx);
           //So that propdev nav bar will be displayed on Error Page.
           request.setAttribute("usePropDevErrorPage", "true");
            return mapping.findForward("failure");
        }
        catch(DBException DBEx)
        {   UtilFactory.log(DBEx.getMessage(), DBEx, "ChangePasswordActin", "perform");
            request.setAttribute("EXCEPTION", DBEx);
           //So that propdev nav bar will be displayed on Error Page.
           request.setAttribute("usePropDevErrorPage", "true");
            return mapping.findForward("failure");
        }
        return mapping.findForward("success");
    }
}
