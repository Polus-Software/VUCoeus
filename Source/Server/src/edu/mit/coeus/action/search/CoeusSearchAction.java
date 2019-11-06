/*
 * @(#)AddCOIDisclosureAction.java	1.0 06/10/2002 18:34:19
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.action.search;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ForwardingActionForward;

import java.util.Vector;
import java.io.IOException;
import java.sql.SQLException;
import java.io.InputStream;
import java.util.Properties;

import edu.mit.coeus.action.common.CoeusActionBase;
import edu.mit.coeus.search.bean.ProcessSearchXMLBean;
import edu.mit.coeus.search.exception.CoeusSearchException;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;

public class CoeusSearchAction extends CoeusActionBase{

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an ActionForward instance describing where and how control
     * should be forwarded, or null if the response has already been completed.
     *
     * @param actionMapping  The ActionMapping used to select this instance
     * @param actionForm  The optional ActionForm bean for this request (if any)
     * @param request  The HTTP request we are processing
     * @param response  The HTTP response we are creating
     *
     * @throws java.io.IOException  if an input/output error occurs
     * @throws javax.servlet.ServletException  if a servlet exception occurs.
     */
    public ActionForward perform( ActionMapping actionMapping ,
        ActionForm actionForm , HttpServletRequest request ,
            HttpServletResponse response ) throws IOException , ServletException{

        System.out.println("inside  CoeusSearchAction.perform()");

        String personId = null;
        String personFullName = null;
        String userName = null;
        HttpSession session = null;
        ActionForward actionforward = actionMapping.findForward( SUCCESS );
//        UtilFactory UtilFactory = new UtilFactory();
        boolean errorFlag = false;
        boolean validResult = false;	// the result of valid award/proposal
        ActionErrors actionErrors = new ActionErrors(); // to hold any validation errors
        java.net.URL searchFileName = getClass().getResource(SEARCH_XML_FILE_NAME);
        String searchName = request.getParameter(SEARCH_NAME);
        try {
            session = request.getSession( true );

            /*
             *  Looking for the search name in the request object
             */
            ProcessSearchXMLBean processSearchXML = new ProcessSearchXMLBean(
                            "",searchName);
           //System.out.println("searchInfoHolderBean in action class=>"+
                              //  processSearchXML.getSearchInfoHolder().toString());
            UtilFactory.log("searchInfoHolderBean in action class=>"+
                               processSearchXML.getSearchInfoHolder().toString(), null, "", "");
            session.setAttribute("searchinfoholder",processSearchXML.getSearchInfoHolder());
            request.setAttribute("searchName",searchName);
            request.setAttribute("fldName",request.getParameter("fieldName"));
            if(searchName.equalsIgnoreCase("personsearch")){
                String reqType = request.getParameter("reqType");
                String reqPage = request.getParameter("reqPage");
                request.setAttribute("reqType",(reqType==null?"0":reqType));
                request.setAttribute("reqPage",(reqPage==null?"":reqPage));
            }
            //request.setAttribute("fieldlist",processSearchXML.getFieldList());

        } catch( CoeusSearchException coeusSearchEx ) {
            errorFlag = true;
            coeusSearchEx.printStackTrace();
            UtilFactory.log( coeusSearchEx.getMessage() , coeusSearchEx ,
                    "CoeusSearchAction" , "perform()" );
            request.setAttribute( "EXCEPTION" , coeusSearchEx );
        } catch( Exception ex ) {
            ex.printStackTrace();
            errorFlag = true;
            UtilFactory.log( ex.getMessage() , ex , "CoeusSearchAction" ,
                    "perform()" );
            request.setAttribute( "EXCEPTION" ,
                    new CoeusSearchException( ex.getMessage() ) );
        }

        /*
         * if any exceptions were thrown then forward to FAILURE page
         * other wise forward to SUCCESS
         */
        if( errorFlag ) {
            actionforward = actionMapping.findForward( FAILURE );
        } else {
            System.out.println("CoeusSearchAction returns success");
            actionforward = actionMapping.findForward( searchName );
        }
        return actionforward;
    }//end of perform
}
