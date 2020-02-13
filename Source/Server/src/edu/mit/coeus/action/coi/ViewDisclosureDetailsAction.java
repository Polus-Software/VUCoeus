/*
 * @(#)ViewDisclosureDetailsAction.java 1.0 05/17/2002 16:59:43
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.action.coi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ForwardingActionForward;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;
/* CASE #734 Comment Begin */
//import edu.mit.coeus.coi.exception.CoeusException;
/* CASE #734 Comment End */
/* CASE #734 Begin */
import edu.mit.coeus.exception.CoeusException;
/* CASE #734 End */
import edu.mit.coeus.coi.bean.DisclosureInfoDetailBean;
import edu.mit.coeus.coi.bean.DisclosureDetailViewBean;
import edu.mit.coeus.action.common.CoeusActionBase;

/**
 * <code>ViewDisclosureDetailsAction</code> is a struts implemented action
 * component to get the History and current details of a Disclsoure.
 * <br>The Disclosure details are extracted from <code>coeus</code> database
 * using Disclosure Number and Entity Number that are supplied by user through a request.
 *
 * @version 1.0 May 17, 2002 16:59:43
 * @author RaYaKu
 */
public class ViewDisclosureDetailsAction extends CoeusActionBase{

    /**
     * Process the specified HTTP request, and create the corresponding HTTP response
     * (or forward to another web component that will create it). Return an ActionForward instance
     * describing where and how control should be forwarded, or null if the response
     * has already been completed.
     * <br>Fetch all the details for a disclosure from the coeus database by using
     * <code>DisclosureInfoDetailBean</code> class.
     *
     * @param actionMapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @throws java.io.IOException if an input/output error occurs
     * @throws javax.servlet.ServletException if a servlet exception occurs.
     */

    public ActionForward perform( ActionMapping actionMapping ,
        ActionForm form ,  HttpServletRequest request ,
            HttpServletResponse response ) throws IOException , ServletException{

        String personId = null;
        String personName = null;
        String userName = null;
        HttpSession session = request.getSession( true );
        ActionForward actionforward = actionMapping.findForward( SUCCESS );
        PersonInfoBean personInfoBean = null;
//        UtilFactory UtilFactory = new UtilFactory();
        boolean errorFlag = false;
        try {
            // look username attribute in session scope
            userName = ( String ) session.getAttribute( USERNAME );

            /*
             * If userName information is not available in session scope then
             * supply him a session expiration page
             */
            if( userName == null ) {
                actionforward = actionMapping.findForward( EXPIRE );
                return ( actionforward );
            }
            // look personInfo attribute in session scope
            personInfoBean = ( PersonInfoBean ) session.getAttribute(PERSONINFO);
            if( personInfoBean != null ) {
                personId = personInfoBean.getPersonID();
                personName = personInfoBean.getFullName();
            }

            String disclosureNum = request.getParameter( "disclNo" );
            String entityNum = request.getParameter( "entNo" );
            //get the disclosure information Details
            DisclosureDetailViewBean disclosureDetailViewBean = new DisclosureDetailViewBean();
            DisclosureInfoDetailBean disclosureInfoDetailBean
                    = disclosureDetailViewBean.getDisclosureInfoDetail(
                            disclosureNum.trim() , entityNum.trim() );

            //get the disclosure History
            Vector discInfoHistory
                    = disclosureDetailViewBean.getDisclosureInfoHistory(
                            disclosureNum.trim() , entityNum.trim() );

            /*
             * bind the disclosure information including history to request object and forward to a view component where
             * the information is present
             */
            request.setAttribute( "disclosureInfo" , disclosureInfoDetailBean );
            request.setAttribute( "disclosureHistory" , discInfoHistory );
            request.setAttribute( "personName" , personName );

            //get title information from session
            request.setAttribute( "discHeaderAwPrInfo", session.getAttribute("discHeaderAwPrInfo"));
            request.setAttribute( "disclosureNo", session.getAttribute("disclosureNo"));



        /*} catch( CoeusException coeusEx ) {
            errorFlag = true;
            UtilFactory.log( coeusEx.getMessage() , coeusEx ,
            "ViewDisclosureDetailsAction" , "perform()" );
            request.setAttribute( "EXCEPTION" , coeusEx );*/
        } catch( DBException dbEx ) {
            errorFlag = true;
            /* CASE #735 Comment Begin */
            //DBEngine prints the exception to log file.
            //UtilFactory.log( dbEx.getMessage() , dbEx ,
                              //"ViewDisclosureDetailsAction" , "perform()" );
            /* CASE #735 Comment End */
            request.setAttribute( "EXCEPTION" , dbEx );
        }catch( Exception ex ) {
            errorFlag = true;
            UtilFactory.log( ex.getMessage() , ex ,
            "ViewDisclosureDetailsAction" , "perform()" );
            request.setAttribute( "EXCEPTION" , new CoeusException( "exceptionCode.60017" ) );
        }
        /*
         * if errorFlag is true then an exception is occured in this component, so that forward to page that
         * is available in struts-config.xml for failure attribute value for this component.
         */
        if( errorFlag ) {
            actionforward = actionMapping.findForward( FAILURE );
        } else {
            actionforward = actionMapping.findForward( SUCCESS );
        }
        return actionforward;
    }
}