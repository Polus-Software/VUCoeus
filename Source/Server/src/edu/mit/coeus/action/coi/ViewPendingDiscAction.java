/*
 * @(#)AddNewCOIDisclosureAction.java 1.0 06/10/2002 18:34:19
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
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;

import java.util.Vector;
import java.io.IOException;
import java.sql.Timestamp;

import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.coi.bean.PendingDisclosuresBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.action.common.CoeusActionBase;

/**
 * <code>AddNewCOIDisclosureAction</code> is a struts implemented Action class
 * to create or modify the COI Disclosure.
 * For creating a new or modifying COI Disclosure this component gets user provided information
 * through an action form <code>AddCOIDisclosureActionForm</code> and questions & answers
 * through request object.
 *
 * @version 1.0 June 10,  2002
 * @author RaYaKu
 */

public class ViewPendingDiscAction extends CoeusActionBase{

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an ActionForward instance describing where and how control
     * should be forwarded, or null if the response has already been completed.
     *
     * Retrieve Vector of DisclosureHeaderBean objects.  Contains disclosure
     * information for all disclosures corresponding to temporary (proposal_log)
     * proposals.
     *
     * @param actionMapping The ActionMapping used to select this instance
     * @actionForm The optional ActionForm bean for this request (if any)
     * @request The HTTP request we are processing
     * @response The HTTP response we are creating
     *
     * @throws java.io.IOException if an input/output error occurs
     * @throws javax.servlet.ServletException if a servlet exception occurs.
     */
    public ActionForward perform( ActionMapping actionMapping ,
            ActionForm actionForm , HttpServletRequest request ,
            HttpServletResponse response ) throws IOException , ServletException{

        System.out.println("inside ViewPendingDiscAction");
        String personId = null;
        String userName = null;
        HttpSession session = null;
        ActionForward actionforward = actionMapping.findForward( SUCCESS );
        ActionErrors actionErrors = new ActionErrors();
//        UtilFactory UtilFactory = new UtilFactory();
        boolean errorFlag = false;

        try {
            session = request.getSession( true );

            // look userName attribute in session scope
            userName = ( String ) session.getAttribute( USERNAME );
            /*
             * If user session is  expired then forward the
             * user to session expiration page
             */
            if( userName == null ) {
                actionforward = actionMapping.findForward( EXPIRE );
                return ( actionforward );
            }

            if(session.getAttribute(VIEW_PENDING_DISC) == null){
                throw new CoeusException("exceptionCode.60022");
            }

            PendingDisclosuresBean pendingDisclosuresBean =
                  new PendingDisclosuresBean();
            Vector collPendingDisc = pendingDisclosuresBean.getDiscForTempProp();
            //System.out.println("collPendingDisc.size(): "+collPendingDisc.size());
            request.setAttribute("collPendingDisc", collPendingDisc);

        } catch( CoeusException cEx ) {
            errorFlag = true;
            UtilFactory.log( cEx.getMessage() , cEx , "ViewPendingDiscAction" ,
            "perform()" );
            request.setAttribute( "EXCEPTION" , cEx );
        } catch( DBException dbEx ) {
            System.out.println("caught DBException.  See Error.log");
            errorFlag = true;
            request.setAttribute( "EXCEPTION" , dbEx );
        }catch( Exception ex ) {
            ex.printStackTrace();
            errorFlag = true;
            UtilFactory.log( ex.getMessage() , ex , "ViewPendingDiscAction" ,
                    "perform()" );
            request.setAttribute( "EXCEPTION" ,
                    new CoeusException( "exceptionCode.60023" ) );
        }

        /* if any exceptions were thrown then forward to FAILURE page other wise
         * forward to success
         */
        if( errorFlag ) {
            //System.out.println("TempPropDisclosureAction returning failure");
            actionforward = actionMapping.findForward( FAILURE );
            return actionforward;
        }
        //System.out.println("TempPropDisclosureAciton returning success");
        return actionforward;
    }//end of perform

    /**
     * Validate the properties that have been set for this request,
     * and return an ActionErrors object that encapsulates any validation errors
     * that have been found.
     * If no errors are found, return null or an ActionErrors object with no
     * recorded error messages.
     * @param actionMapping The mapping used to select this instance
     * @param request  The servlet request we are processing
     */
    public ActionErrors validate( ActionMapping actionMapping ,
    HttpServletRequest request ){
        ActionErrors actionErrors = new ActionErrors();
        return actionErrors;

    }//end of validate Method
}//end of ViewTempPropDisclosureAction
