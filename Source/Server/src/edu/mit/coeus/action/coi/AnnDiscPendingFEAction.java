/*
 * @(#)AnnDiscPendingFEAction.java 1.0 6/10/02
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
import java.util.Vector;
import java.io.IOException;
import java.sql.SQLException;

import edu.mit.coeus.coi.bean.SearchBean;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;
/* CASE #734 Comment Begin */
//import edu.mit.coeus.coi.exception.CoeusException;
/* CASE #734 Comment End */
/* CASE #734 Begin */
import edu.mit.coeus.exception.CoeusException;
/* CASE #734 End */
import edu.mit.coeus.coi.bean.AnnualDiscFinEntitiesBean;
import edu.mit.coeus.coi.bean.FinancialEntitiesBean;
import edu.mit.coeus.action.common.CoeusActionBase;

/**
 * <code>AnnDiscPendingFEAction</code> is a struts implemented Action class
 * to get all the Financial Entites for the review.
 *
 * Note: <code>GetFinancialEntitiesAction</code> is slightly different from
 * this class which checks the user privilege management.
 *
 * @version 1.0 June 10,2002
 * @author Phaneendra Kumar.
 */
public class AnnDiscPendingFEAction extends CoeusActionBase{

    /**
     * Process the specified HTTP request, and create the corresponding HTTP response
     * (or forward to another web component that will create it). Return an ActionForward instance
     * describing where and how control should be forwarded, or null if the response
     * has already been completed.
     *
     * <br>The method used to fetch the details of all Financial Entity disclosures
     * for a person. <code<getFinancialEntities</code> method of
     * <code<FinancialEntitiesBean</code> class will be used to fetch the details.
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
            ActionForm actionForm ,HttpServletRequest request ,
            HttpServletResponse response ) throws IOException , ServletException{
        String personId = null;
        String userName = null;
        boolean errorFlag = false;
        HttpSession session = request.getSession();
        ActionForward actionforward = actionMapping.findForward( SUCCESS );
//        UtilFactory UtilFactory = new UtilFactory();
        try {
            session = request.getSession( true );

            /* look username attribute in session scope */
            userName = ( String ) session.getAttribute( USERNAME );

            /*
             * If userName information is not available in session scope then
             * supply him a session expiration page
             */
            if( userName == null ) {
                actionforward = actionMapping.findForward( EXPIRE );
                return ( actionforward );
            }

            /* look personId in session scope */
            personId = ( String ) session.getAttribute( LOGGEDINPERSONID );

            /* If user session is really expired then forward the
             * user to session exipiration page
             */
            if( personId == null ) {
                actionforward = actionMapping.findForward( EXPIRE );
                return ( actionforward );
            }

            /*
             * Get the AnnualDisclFinEntitiesBean instance to get all the pending Financial Entities
             * for a person for the review.
             */
            /*AnnualDiscFinEntitiesBean pendingFinEntities =
                                    new AnnualDiscFinEntitiesBean( personId.trim() );
            Vector finEntities = pendingFinEntities.getAnnualDiscEntities();
             */
            // get all financial entities
            FinancialEntitiesBean financialEntitiesBean
                    = new FinancialEntitiesBean( personId.trim() );
            Vector finEntities = financialEntitiesBean.getFinancialEntities();

            /* Set the collection of pending Financial Entities which has pending
             * disclosures to be reviewed in the session scope.These will be collected
             * in an included AnnualDisclLeftPane.jsp to display for the user in the
             * left frame of the AnnualDisclosures page.
             */
             /* CASE #409 Comment Begin */
            //request.setAttribute( "allAnnualDiscEntities" , finEntities );
            /* CASE #409 Comment End */
            /* CASE #912 Comment Begin*/
            /*AnnualDiscFinEntitiesBean annualDisclFinEntities =
                    new AnnualDiscFinEntitiesBean( personId.trim() );
            Vector allAnnualDiscEntities = annualDisclFinEntities.getAnnualDiscEntities();
            request.setAttribute( "allAnnualDiscEntities" , allAnnualDiscEntities );*/
            /* CASE #912 Comment End */
            /* CASE #912 Begin */
            //For better performance, instead of calling get_annual_disc_entities,
            // call stored function to check if person has active entities.
            AnnualDiscFinEntitiesBean annualDiscFinEntities =
                    new AnnualDiscFinEntitiesBean( personId.trim() );
            boolean hasActiveEntities = annualDiscFinEntities.checkPersonHasActiveFE();
            request.setAttribute("hasActiveEntities", new Boolean(hasActiveEntities));
            /* CASE #912 End */
            request.setAttribute( "finEntities", finEntities );
            /* CASE #409 End */
        } catch( CoeusException coeusEx ) {
            errorFlag = true;
            UtilFactory.log( coeusEx.getMessage() , coeusEx , "AnnDiscPendingFEAction" , "perform()" );
            request.setAttribute( "EXCEPTION" , coeusEx);
        } catch( DBException dbEx ) {
            errorFlag = true;
            /* CASE #735 Comment Begin */
            //DBEngine prints the exception to log file.
            //UtilFactory.log( dbEx.getMessage() , dbEx , "AnnDiscPendingFEAction" , "perform()" );
            /* CASE #735 Comment End */
            request.setAttribute( "EXCEPTION" , dbEx );
        } catch( Exception ex ) {
            errorFlag = true;
            UtilFactory.log( ex.getMessage() , ex , "AnnDiscPendingFEAction" , "perform()" );
            request.setAttribute( "EXCEPTION" , new CoeusException( "exceptionCode.70004" ) );
        }

        if( errorFlag ) {
            actionforward = actionMapping.findForward( FAILURE );
        } else {
            actionforward = actionMapping.findForward( SUCCESS );
        }
        return actionforward;
    }
}