/*
 * @(#) DisplayNSFProgAnnDetsAction.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.action.nsfProgAnn;

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

import edu.mit.coeus.nsfProgAnn.bean.NSFProgAnnDetsBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;


/**
 * Extend Struts Action class, to initiate response to a user request to
 * display NSF Program announcements
 *
 * @author Coeus Dev Team
 */
public class DisplayNSFProgAnnDetsAction extends Action
{	/**
     * pne parameters needs to be passed
     * by the calling JSP.
     */
    private String progAnnounceID;
 	/**
     * No argument constructor.
     */
    public DisplayNSFProgAnnDetsAction(){}

    /**
     * Initiate a response to user request to display data 
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
    public ActionForward perform(ActionMapping mapping, ActionForm actionForm,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
	{   HttpSession session = request.getSession();
            
          progAnnounceID = request.getParameter("progAnnounceID");
          
          System.out.println("start of display nsf progAnnDetsAction, progannounceID is " + progAnnounceID);
          
        if (request.isSecure() == false) {
        CoeusException Ex = new CoeusException("exceptionCode.error.no.https");

        request.setAttribute("EXCEPTION", Ex);
         return mapping.findForward("failure");
        }
//        UtilFactory UtilFactory = new UtilFactory();
        System.out.println("After UtlFactory");
        NSFProgAnnDetsBean nsfProgAnnDetsBean = new NSFProgAnnDetsBean();

       
        Vector results = null;
        try
        {   nsfProgAnnDetsBean.init(progAnnounceID);
           System.out.println(" after init");
            request.setAttribute("nsfProgAnnDets", nsfProgAnnDetsBean);
            System.out.println(" after setting request attribute with bean");
            
             
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
        return mapping.findForward("displayNSFProgAnnDets");
    }
}
