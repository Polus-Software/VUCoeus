/*
 * @(#) TempProposalAction.java	1.0 06/11/2002 01:48:19 AM
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
import org.apache.struts.action.ForwardingActionForward;
import java.util.Vector;
import java.util.Hashtable;
import java.io.IOException;
import java.sql.SQLException;

import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;
/* CASE #734 Comment Begin */
//import edu.mit.coeus.coi.exception.CoeusException;
/* CASE #734 Comment End */
/* CASE #734 Begin */
import edu.mit.coeus.exception.CoeusException;
/* CASE #734 End */
import edu.mit.coeus.coi.bean.ProposalSearchBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.action.common.CoeusActionBase;

/**
 * <code>TempProposalAction</code> is a struts implemented Action class which
 * enables the user to create a temporary disclosure when he wishes to create a
 * new disclsoure.
 * Provides default information to create a new Temporary Proposal.
 * <br> <b> Default information provided to create a new Temporary proposal</b>
 * <li> Primary Investigater Id
 * <li> Primary Investigater Name.
 * <li> All Proposal Types.
 *
 * @version 1.0 June 11,2002
 * @author RaYaKu
 */

public class TempProposalAction extends CoeusActionBase{

    /**
     * Process the specified HTTP request, and create the corresponding HTTP response
     * (or forward to another web component that will create it). Return an ActionForward instance
     * describing where and how control should be forwarded, or null if the response
     * has already been completed.
     * <br>The method fetchs the details from <code>coeus</code> database
     * and attach these details with request object.It uses
     * <code>getProposalTypes</code> method of <code>ProposalSearchBean</code>
     * to get all proposal types.
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

        String personId = null;
        String personFullName = null;
        String userName = null;
        String homeUnit = null;
        String unitName = null;
        PersonInfoBean personInfoBean = null;
        HttpSession session = null;
        ActionForward actionforward = actionMapping.findForward(SUCCESS);
//        UtilFactory UtilFactory = new UtilFactory();
        boolean errorFlag = false;
        ActionErrors actionErrors = new ActionErrors(); // to hold any validation errors
        System.out.println("begin TempProposalAction");
        try {
            session = request.getSession( true );
            // look userName attribute in session scope
            userName = ( String ) session.getAttribute( USERNAME );
            /* If user session is really expired then forward the
             * user to session exipiration page
             */
            if( userName == null ) {
                actionforward = actionMapping.findForward( EXPIRE );
                return ( actionforward );
            }
            // look personInfo in session scope
            personInfoBean = ( PersonInfoBean ) session.getAttribute(PERSONINFO);

            if( personInfoBean != null ) {
                personId = personInfoBean.getPersonID();
                /* CASE #231 Begin */
                /* If user has selected to view another user's disclosures, and
                user does not have maintain coi right, then create new temp
                proposal for logged in user, not selected user. */
                String userprivilege = (String)session.getAttribute("userprivilege");
                String loggedinpersonid = (String)session.getAttribute(LOGGEDINPERSONID);
                if(!loggedinpersonid.equals(personId)  &&
                                  Integer.parseInt(userprivilege) != 2){
                    UserDetailsBean userDetailsBean = new UserDetailsBean();
                    PersonInfoBean loggedInPersonInfo =
                            userDetailsBean.getPersonInfo(loggedinpersonid, true);
                    personFullName = loggedInPersonInfo.getFullName();
                    homeUnit = loggedInPersonInfo.getHomeUnit();
                    unitName = loggedInPersonInfo.getUnitName();
                }
                else{
                /* CASE #231 End */
                  personFullName = personInfoBean.getFullName();
                  homeUnit = personInfoBean.getHomeUnit();
                  unitName = personInfoBean.getUnitName();
                }
            }
            //get the form
            TempProposalActionForm tempProposalActionForm
                                        = ( TempProposalActionForm ) actionForm;

            if( tempProposalActionForm != null ) {
                tempProposalActionForm.setPrimInvestigaterId( personId );
                tempProposalActionForm.setPrimInvestigaterName( personFullName );
                tempProposalActionForm.setLeadUnit(homeUnit);
                tempProposalActionForm.setLeadUnitName(unitName);
            }
            // search for proposals for this person.
            ProposalSearchBean proposalSearchBean = new ProposalSearchBean( personId.trim() );

            // get all proposal codes and their respective types
            Vector collProposalTypes = proposalSearchBean.getProposalTypes();

            session.setAttribute( "collProposalTypes" , collProposalTypes );

        } catch( DBException dbEx ) {
            errorFlag = true;
            /* CASE #735 Comment Begin */
            //DBEngine prints the exception to log file.
            //UtilFactory.log( dbEx.getMessage() , dbEx , "TempProposalAction" ,
                   // "perform()" );
            /* CASE #735 Comment End */
            request.setAttribute( "EXCEPTION" , dbEx );
        }catch( Exception ex ) {
            errorFlag = true;
            UtilFactory.log( ex.getMessage() , ex ,
                    "TempProposalAction" , "perform()" );
            request.setAttribute( "EXCEPTION" ,
                    new CoeusException( "exceptionCode.60015" ) );
        }
        //if any exceptions were thrown then forward to FAILURE page other wise forward to success
        if( errorFlag ) {
            actionforward = actionMapping.findForward( FAILURE );
        } else {
            session.setAttribute("tempProposalAction", "insert");
            actionforward = actionMapping.findForward( SUCCESS );
        }
        return actionforward;
    }//end of perform
}