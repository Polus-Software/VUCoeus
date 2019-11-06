/*
 * @(#) DisplayHumanSubjAction.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.action.humansubj;

import edu.mit.coeuslite.utils.CoeusBaseAction;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Hashtable;
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

import java.util.Vector;


import edu.mit.coeus.humansubj.bean.HumanSubjBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;


/**
 * Extend Struts Action class, to initiate response to a user request to
 * display human subject trainees.
 *
 * @author Coeus Dev Team
 * @version 1.0  $ $Date:   Aug 12 2002 16:13:42  $
 */
public class DisplayHumanSubjAction extends CoeusBaseAction
{	/**
     * no parameters need to be passed
     * by the calling JSP.
     */

 	/**
     * No argument constructor.
     */
    public DisplayHumanSubjAction(){}

    /**
     * Initiate a response to user request to display data about human subject
     * trainees.
     * Get data, store in bean objects, and set the beans
     * as attributes of the request object.  Forward to JSP to display the data.
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
    public ActionForward performExecute(ActionMapping mapping, ActionForm actionForm,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
	{   HttpSession session = request.getSession();

        if (request.isSecure() == false) {
        CoeusException Ex = new CoeusException("exceptionCode.error.no.https");

        request.setAttribute("EXCEPTION", Ex);
         return mapping.findForward("failure");
        }
//        UtilFactory UtilFactory = new UtilFactory();
        System.out.println("After UtlFactory");
        HumanSubjBean humanSubjBean = new HumanSubjBean();

        System.out.println(" after new HumanSubjBean");
        Vector results = null;
        try
        {   humanSubjBean.getHumanSubjInfo();
           System.out.println(" after getHumanSubjInfo");
            request.setAttribute("humansubj", humanSubjBean);
        }
        catch(DBException DBEx)
        {   request.setAttribute("EXCEPTION", DBEx);
            return mapping.findForward("failure");
        }
        catch(SQLException SQLEx)
        {   request.setAttribute("EXCEPTION", SQLEx);
            return mapping.findForward("failure");
        }
        catch(Exception ex)
        {   request.setAttribute("EXCEPTION", ex);
            return mapping.findForward("failure");
        }
        return mapping.findForward("displayHumanSubj");
    }
}
