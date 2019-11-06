/*
 * @(#)NewDisclosureAction.java 1.0 06/10/2002 18:34:19
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
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;
/* CASE #734 Comment Begin */
//import edu.mit.coeus.coi.exception.CoeusException;
/* CASE #734 Comment End */
/* CASE #734 Begin */
import edu.mit.coeus.exception.CoeusException;
/* CASE #734 End */
import edu.mit.coeus.coi.bean.DisclosureValidationBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.coi.bean.DisclosureDetailsBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.action.common.CoeusActionBase;

/**
 * <code>NewDisclosureAction</code> is a struts implemented Action class
 * to create a new disclosure on existing award, proposal
 * by validating Award Number or Proposal Number respectively or on a Temporary
 * Proposal.
 *
 * This component shows a view component with default(Award)
 * disclosure type to the user and process the same view component back with
 * valid award/proposal number or Temporary Proposal.
 *
 * Default disclosure type is award and forwards to a view component and the same
 * view component is processed back which is with user desired information.
 *
 * @version 1.0 June 10, 2002
 * @author RaYaKu
 */

public class NewDisclosureAction extends CoeusActionBase{

    /**
     * Process the specified HTTP request, and create the corresponding HTTP response
     * (or forward to another web component that will create it). Return an ActionForward instance
     * describing where and how control should be forwarded, or null if the response
     * has already been completed.
     * <br>The method used to validate the selected disclosure type.
     * it uses the following objects and methods for performing the validations
     * <li>To validate Proposal, it uses <code>isProposalNumValid</code> method
     * of <code>DisclosureValidationBean</code> class.
     * <li>To validate award, it uses <code>isAwardNumValid</code> method
     * of <code>DisclosureValidationBean</code> class.
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
            ActionForm actionForm , HttpServletRequest request ,
            HttpServletResponse response ) throws IOException , ServletException{

        System.out.println("Inside NewDisclosureAction ");
        System.out.println("actionMapping.getPath(): "+actionMapping.getPath());

        /* CASE #1374 Comment Begin */
        //final String TO_ADD_COIDISCL_PAGE = "success1";
        //final String TO_TEMP_PROPOSAL_PAGE = "success2";
        //final String TO_ERROR_PAGE = "failure1";
        //final String TO_NEW_DISCLOSURE_PAGE = "failure2";
        /* CASE #1374 End */

        String personId = null;
        String personFullName = null;
        String userName = null;
        PersonInfoBean personInfoBean = null;
        HttpSession session = null;
        /* CASE #1374 Comment Begin */
        //ActionForward actionforward = actionMapping.findForward( TO_ADD_COIDISCL_PAGE );
        /* CASE #1374 Comment End */
        ActionForward actionforward = actionMapping.findForward ( SUCCESS );
//        UtilFactory UtilFactory = new UtilFactory();
        boolean errorFlag = false;
        boolean validResult = false;	// the result of valid award/proposal
        ActionErrors actionErrors = new ActionErrors(); // to hold any validation errors
        String loggedinpersonid = null;
        String userprivilege = null;
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
            // process form information
            NewCOIDisclosureActionForm newCOIDisclosureActionForm
            = ( NewCOIDisclosureActionForm ) actionForm;
            //Check for the person id in the form bean.
            personId = newCOIDisclosureActionForm.getPersonId();
            personFullName = newCOIDisclosureActionForm.getPersonName();

            // look personInfo in session scope
            personInfoBean = ( PersonInfoBean ) session.getAttribute(PERSONINFO);
            loggedinpersonid = (String)session.getAttribute(LOGGEDINPERSONID);
            userprivilege = (String)session.getAttribute("userprivilege");

           /*CASE #212 COMMENT BEGIN

            /* check whether the personId element is the renedered on the form.
             * if yes, check he has selected any person to do the operations. The validation
             * is done by checking the isOwnFlag attached with the PersonInfoBean class.

            if( personId != null && personInfoBean.getOwnInfo() ) {
                actionErrors.add( "Invalid Person Name" ,
                    new ActionError( "error.invalidPersonName" ) );
                saveErrors( request , actionErrors );
                actionforward = actionMapping.findForward( TO_NEW_DISCLOSURE_PAGE );
                return actionforward;
            } else {
                if( personInfoBean != null ) {
                    personId = personInfoBean.getPersonID();
                    personFullName = personInfoBean.getFullName();
                }
            }
            CASE #212 COMMENT END */
            /*  CASE #212 BEGIN
            Check whether personId from form is null.  If so, get personID from
            PersonInfoBean.  Check whether loggedinpersonid is the same as personID.
            If yes, create a new disclosure for personID.  Else,
            check whether user has Maintain COI.  If yes, create a new disclosure for
            personID.  Else, create a new disclosure for loggedinpersonid.
            */
            if(personId == null){
                personId = personInfoBean.getPersonID();
            }
            if(loggedinpersonid.equals(personId)){
                personFullName = personInfoBean.getFullName();
            }
            else{
                if(userprivilege.equals("2")){
                    personFullName = personInfoBean.getFullName();
                }
                else{
                    personId = loggedinpersonid;
                    personFullName = (String)session.getAttribute(LOGGEDINPERSONNAME);
                }
            }
            /* CASE #212 End */
            newCOIDisclosureActionForm.setUserName( personFullName );
            String disclType = newCOIDisclosureActionForm.getDisclType();
            System.out.println("disclsoureType: "+disclType);
            if(disclType.equals("midyear") ){
                actionforward = actionMapping.findForward ( "success2" );
            }

            DisclosureDetailsBean disclosureDetailsBean
                        = new DisclosureDetailsBean( personId.trim() );
            LinkedList collCOIStatus = disclosureDetailsBean.getAllCOIStatus();
            request.setAttribute("personId",personId);
            request.setAttribute( "collCOIStatus" , collCOIStatus );
            
        } catch( DBException dbEx ) {
            errorFlag = true;
            /* CASE #735 Comment Begin */
            //DBEngine prints the exception to log file.
            //UtilFactory.log( dbEx.getMessage() , dbEx , "NewDisclosureAction" ,
                   // "perform()" );
            /* CASE #735 Comment End */
            request.setAttribute( "EXCEPTION" , dbEx );
        }catch( Exception ex ) {
            errorFlag = true;
            ex.printStackTrace();
            UtilFactory.log( ex.getMessage() , ex ,
                    "NewDisclosureAction" , "perform()" );
            request.setAttribute( "EXCEPTION" ,
                    new CoeusException( "exceptionCode.60011" ) );
        }
        //if any exceptions were thrown then forward to FAILURE page other wise forward to success1
        if( errorFlag ) {
            /* CASE #1374 Comment Begin */
            //actionforward = actionMapping.findForward( TO_ERROR_PAGE );
            /* CASE #1374 Comment End */
            /* CASE #1374 Begin */
            actionforward = actionMapping.findForward( FAILURE );
            /* cASE #1374 End */
        }
        /* CASE #1374 Comment Begin */
        /*else {
            actionforward = actionMapping.findForward( TO_ADD_COIDISCL_PAGE );
        }*/
        /* CASE #1374 Comment End */
        System.out.println("NewDisclosureAction returning success");
        return actionforward;
    }//end of perform
}