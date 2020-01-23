/*
 * @(#) SelectPersonAction.java	1.0    2002/06/08   14:59:29
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
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

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.utils.dbengine.DBException;
/* CASE #734 Comment Begin */
//import edu.mit.coeus.coi.exception.CoeusException;
/* CASE #734 Comment End */
/* CASE #734 Begin */
import edu.mit.coeus.exception.CoeusException;
/* CASE #734 End */
import edu.mit.coeus.action.common.CoeusActionBase;

/**
 * <code>SelectPersonAction</code> is an struts implemented Action class
 *  to select a person if the logged in user has <code>OSP right</code>.
 *
 * @author  Geo Thomas
 * @version 1.0
 */
public class SelectPersonAction extends CoeusActionBase{

    private final String NEWDISCLOSURE = "newdisclosure";

    /**
     * Process the specified HTTP request, and create the corresponding HTTP response
     * (or forward to another web component that will create it). Return an ActionForward instance
     * describing where and how control should be forwarded, or null if the response
     * has already been completed.
     *
     * <br>The method will take the person id from the request object and fetch
     * corresponding person's details from the <code>coeus</code> database.
     * The details will be attached to the request object as an instance of
     * <code>PersonInfo</code> class.
     *
     * @param actionMapping - The ActionMapping used to select this instance
     * @actionForm - The optional ActionForm bean for this request (if any)
     * @request - The HTTP request we are processing
     * @response - The HTTP response we are creating
     *
     * @throws java.io.IOException - if an input/output error occurs
     * @throws javax.servlet.ServletException - if a servlet exception occurs.
     */

    public ActionForward perform( ActionMapping actionMapping ,
            ActionForm actionForm , HttpServletRequest request ,
            HttpServletResponse response ) throws IOException , ServletException{

        System.out.println("inside SelectPersonAction.java");
        HttpSession session = null;
        ActionForward actionforward = actionMapping.findForward( SUCCESS );
//        UtilFactory UtilFactory = new UtilFactory();
        CoeusException coeusExce = new CoeusException();
        String personID = null;
        String personFullName = null;
        String userName = null;
        //To hold the information about the page from which the request had come for selecting person
        String reqPage = null;
        boolean errorFlag = true;
        try {
            session = request.getSession();
            //look user name attribute in session scope
            userName = ( String ) session.getAttribute( USERNAME );
            /*
             * If user name is not available in session scope then
             * supply him a session expiration page
             */
            if( userName == null ) {
                actionforward = actionMapping.findForward( EXPIRE ); //EXPIRE is defined in CoeusActionBase
                return ( actionforward );
            }

            //instance to get the db details of a particular user.
            UserDetailsBean userDetails = new UserDetailsBean();
            //To store the person information of the selected person
            PersonInfoBean sltdPersonInfo = null;
            String sltdPersonID = request.getParameter( "personID" );
            System.out.println("sltdPersonID: "+sltdPersonID);
            if( sltdPersonID != null ) {
                //get the details of the selected person after the search
                sltdPersonInfo = userDetails.getPersonInfo( sltdPersonID, true );
                if( sltdPersonInfo != null ) {
                    //making the flag to check the status of the details to false.
                    sltdPersonInfo.setOwnInfo( false );
                } else {
                    coeusExce.setMessage( "exceptionCode.40003" );
                    throw coeusExce;
                }
            } else {
                System.out.println("sltdPersonID was null");
                coeusExce.setMessage( "exceptionCode.40004" );
                throw coeusExce;
            }
            session.setAttribute( "personInfo" , sltdPersonInfo );
                        /* Fetch the requested page information from the request scope.
                         * if the request for selecting a person comes from NewDisclosureContent,
                         * redirect to the same page.
                         * else show the selected person's details
                         */
            reqPage = request.getParameter( "reqPage" );

            if( reqPage != null && reqPage.trim().equals( "newdisc" ) ) {
                actionforward = actionMapping.findForward( NEWDISCLOSURE );
            }
            System.out.println("SelectPersonAction returning success");
            errorFlag = false;
        } catch( CoeusException cEx ) {
            UtilFactory.log( cEx.getMessage() , cEx , "SelectPersonAction" , "perform()" );
            request.setAttribute( "EXCEPTION" , cEx );
        } catch( DBException dbEx ) {
            /* CASE #735 Comment Begin */
            //DBEngine prints the exception to log file.
            //UtilFactory.log( dbEx.getMessage() , dbEx , "SelectPersonAction" , "perform()" );
            /* CASE #735 Comment End */
            request.setAttribute( "EXCEPTION" , dbEx );
        }catch( Exception ex ) {
            coeusExce.setMessage( "exceptionCode.40002" );
            UtilFactory.log( ex.getMessage() , ex , "SelectPersonAction" , "perform()" );
            request.setAttribute( "EXCEPTION" , coeusExce );
        }
        //forward the output to failure/success page depends on errorFlag state
        if( errorFlag ) {
            actionforward = actionMapping.findForward( FAILURE );
        }
        return actionforward;
    }
}