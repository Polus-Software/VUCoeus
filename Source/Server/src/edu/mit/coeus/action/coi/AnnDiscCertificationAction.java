/*
 * @(#)AnnDiscCertificationAction.java	1.0 5/17/2004
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

import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.coi.bean.CertQuestionDetailsBean;
import edu.mit.coeus.coi.bean.AnnualDiscFinEntitiesBean;
import edu.mit.coeus.action.common.CoeusActionBase;

/**
 * <code>AnnDiscCertificationAction</code> is a struts implemented Action class
 * to collect and validate user answers to certification questions for any
 * user disclosed financial entities.
 *
 * @version 1.0 May 17, 2004
 * @author coeus-dev-team
 */
public class AnnDiscCertificationAction extends CoeusActionBase{

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an ActionForward instance describing where and how control
     * should be forwarded, or null if the response has already been completed.
     * <br> The method will fetch the required information from the <code> Coeus</code>
     * database and attach these details with <code>AnnDiscCertificationActionForm</code>
     * bean which has the request scope.
     * <br><b>Validations</b>
     * <br><li> Stored procedure will check status and answers to certification
     * questions for any user disclosed financial
     * entities and set certification questions to the appropriate answers.
     * When the form is processed, with user disclosed answers, a stored procedure
     * will check the validity of the answers.
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

        System.out.println("begin AnnDiscCertificationAction");

        String personId = null;
        String personFullName = null;
        String userName = null;
        PersonInfoBean personInfoBean = null;
        HttpSession session = null;
        ActionForward actionforward = actionMapping.findForward( SUCCESS );
//        UtilFactory UtilFactory = new UtilFactory();
        boolean errorFlag = true;
        //final String QUESTION_TYPE = "C";

        try {
            session = request.getSession( true );

            // look userName attribute in session scope
            userName = ( String ) session.getAttribute( USERNAME );

            /*
             * If user session is really expired then forward the
             * user to session exipiration page
             */
            if( userName == null ) {
                actionforward = actionMapping.findForward( EXPIRE );
                return actionforward;
            }

            //If user has answered the questions, and form has been submitted,
            //if got here then validation has succeeded.  Forward to main
            //annual disc page.
            if(actionMapping.getPath().equals("/annDiscCertificationValidation")){
                System.out.println("forwarding to main ann disc page");
                return actionforward;
            }
            
            /* CASE #1374 Begin */
            //if user is coming from edit fin ent, don't get cert questions
            //from DB.
            String actionFrom = (String)request.getAttribute("actionFrom");
            if(actionFrom == null){
                actionFrom = request.getParameter("actionFrom");
            }
            if(actionFrom != null && actionFrom.equals("editFinEnt") ){
                return actionforward;
            }
            /* CASE #1374 End */ 

            // look personInfo in session scope
            personInfoBean = ( PersonInfoBean ) session.getAttribute( PERSONINFO );
            if( personInfoBean != null ) {
                personId = personInfoBean.getPersonID();
            } 
                    
            CertQuestionDetailsBean certQuestionDetails =
                    new CertQuestionDetailsBean(personId);

            //get all COI Disclosure Certificate details
            Vector annDiscCertQuestions
                    = certQuestionDetails.getAnnDiscCertQuestions();

            /* CASE #1374 Begin */
            AnnualDiscFinEntitiesBean annualDiscFinEntities =
                    new AnnualDiscFinEntitiesBean( personId.trim() );            
            boolean hasActiveEntities = annualDiscFinEntities.checkPersonHasActiveFE();
            AnnDiscCertificationActionForm frmAnnDiscCertification = new AnnDiscCertificationActionForm();
            frmAnnDiscCertification.setHasActiveEntities(hasActiveEntities);
            session.setAttribute("frmAnnDiscCertification", frmAnnDiscCertification);
            /* CASE #1374 End */
            
            //Put certification answers from stored procedure call in request.
            session.setAttribute( "annDiscCertQuestions" , annDiscCertQuestions );
            errorFlag = false;
        } catch( CoeusException coeusEx ) {
            UtilFactory.log( coeusEx.getMessage() , coeusEx ,
                    "AddCOIDisclosureAction" , "perform()" );
            request.setAttribute( "EXCEPTION" , coeusEx );
        } catch( DBException dbEx ) {
            request.setAttribute( "EXCEPTION" , dbEx );
        }catch( Exception ex ) {
            ex.printStackTrace();
            UtilFactory.log( ex.getMessage() , ex , "AddCOIDisclosureAction" ,
                    "perform()" );
            request.setAttribute( "EXCEPTION" ,
                    new CoeusException( "exceptionCode.70014" ) );
        }

        /*
         * if any exceptions were thrown then forward to FAILURE page
         * other wise forward to SUCCESS
         */
        if( errorFlag ) {
            actionforward = actionMapping.findForward( FAILURE );
        }
        return actionforward;
    }//end of perform
}