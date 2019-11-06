/*
 * @(#)MidYearDisclosureAction.java 1.0 06/10/2002 18:34:19
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
import edu.mit.coeus.exception.CoeusException;
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

public class MidYearDisclosureAction extends CoeusActionBase{

    /**
     * Process the specified HTTP request, and create the corresponding HTTP response
     * (or forward to another web component that will create it). Return an ActionForward instance
     * describing where and how control should be forwarded, or null if the response
     * has already been completed.
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

        System.out.println("Inside MidYearDisclosureAction ");
        System.out.println("actionMapping.getPath(): "+actionMapping.getPath());

        String personId = null;
        String personFullName = null;
        String userName = null;
        PersonInfoBean personInfoBean = null;
        HttpSession session = null;
        ActionForward actionforward = actionMapping.findForward ( SUCCESS );
        boolean errorFlag = false;
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
            
            if(actionMapping.getPath().equals("/midyeardisclosure") ){
                return actionforward;
            }
            // process form information
            MidYearDisclosureActionForm midYearDisclosureActionForm
            = ( MidYearDisclosureActionForm ) actionForm;
            //Check for the person id in the form bean.
            personId = midYearDisclosureActionForm.getPersonId();
            personFullName = midYearDisclosureActionForm.getPersonName();

            // look personInfo in session scope
            personInfoBean = ( PersonInfoBean ) session.getAttribute(PERSONINFO);
            loggedinpersonid = (String)session.getAttribute(LOGGEDINPERSONID);
            userprivilege = (String)session.getAttribute("userprivilege");

            /* Check whether loggedinpersonid is the same as personID.
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
            String disclosureType = midYearDisclosureActionForm.getDisclosureType();
            //System.out.println("disclosureType: "+disclosureType);
            if(disclosureType.equals("1")){
                String awardNum = midYearDisclosureActionForm.getAwardNum();
                request.setAttribute("appliesToCode", awardNum );
            }
            else{
                String proposalNum = midYearDisclosureActionForm.getProposalNum();
                request.setAttribute( "appliesToCode" , proposalNum );
            }
            
            //keep the user selected disclosureType in  request scope
            request.setAttribute( "disclosureTypeCode" , disclosureType );
            request.setAttribute("personId",personId);
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
            actionforward = actionMapping.findForward( FAILURE );
        }
        //System.out.println("MidYearDisclosureAction returning success");
        return actionforward;
    }//end of perform
}