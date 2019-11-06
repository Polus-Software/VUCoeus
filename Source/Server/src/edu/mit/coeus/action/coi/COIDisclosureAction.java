/*
 * @(#)COIDisclosureAction.java 1.0 05/31/2002 14:21:23
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
import java.util.Vector;
import java.util.ArrayList;
import java.io.IOException;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.coi.bean.SearchBean;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;
/* CASE #734 Comment Begin */
//import edu.mit.coeus.coi.exception.CoeusException;
/* CASE #734 Comment End */
/* CASE #734 Begin */
import edu.mit.coeus.exception.CoeusException;
/* CASE #734 End */
import edu.mit.coeus.action.common.CoeusActionBase;
import edu.mit.coeus.utils.CoeusConstants;

/**
 * <code>COIDisclosureAction</code> is a struts implemented action class
 * to fetch all COI Disclosures on user selected search criteria and all available
 * Disclosures status.
 *
 * <br> <b>Default Search criteria if no Search criteria is provided</b>
 * <li> Disclosure Status= "101".
 *
 * @author RaYaKu
 * @version 1.0 May 31, 2002 14:21:23
 */
public class COIDisclosureAction extends CoeusActionBase{

    /**
     * Process the specified HTTP request, and create the corresponding HTTP response
     * (or forward to another web component that will create it).
     * Return an ActionForward instance describing where and how control should
     * be forwarded, or null if the response has already been completed.
     *
     * <br>Fetches all COIDisclosures for a particular person or selected search criteria
     * and attach these details with the request object as a collection.
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

        String personId = null;
        String personName = null;
        String userName = null;
        String status = null;
        String appliesTo = null;
        String awardProposalNo = null;
        String type = null;
        HttpSession session = request.getSession( true );
        ActionForward actionforward = actionMapping.findForward( SUCCESS );
//        UtilFactory UtilFactory = new UtilFactory();
        boolean errorFlag = false;
        String defaultStatus="All";
        try {
            // look userName attribute in session scope
            userName = ( String ) session.getAttribute( USERNAME );
            // look personInfo in session scope
            PersonInfoBean personInfoBean =
                            ( PersonInfoBean ) session.getAttribute(PERSONINFO );

            /* CASE #1046 Comment Begin */
            /*if( userName == null ) {
                actionforward = actionMapping.findForward( EXPIRE );
                return ( actionforward );
            }
            //process the form information
            COIDisclosureActionForm coiDisclosureActionForm =
                                        ( COIDisclosureActionForm ) actionForm;
            /*
             * If a incoming request is getcoidisclosure then search
             * for all disclosures with default status, currently set to "All".

            if( actionMapping.getPath().equals( "/getcoidisclosure" ) ) {
                coiDisclosureActionForm.setStatus( defaultStatus );
                coiDisclosureActionForm.setAppliesTo( "" );
                coiDisclosureActionForm.setAwardProposalNum( "" );
                coiDisclosureActionForm.setType( "" );
                //Check whether the logged in user has selected any other person
                /*
                 *  If he selected any person, attach the selected person id and
                 *  person name with formbean.

                if(personInfoBean.getOwnInfo()){
                    coiDisclosureActionForm.setPersonId("");
                    coiDisclosureActionForm.setPersonName("");
                }else{
                    coiDisclosureActionForm.setPersonId(personInfoBean.getPersonID());
                    coiDisclosureActionForm.setPersonName(personInfoBean.getFullName());
                }
            }
            personId = coiDisclosureActionForm.getPersonId();
            personName = coiDisclosureActionForm.getPersonName();
            /*
             * Check whether the person name control is there with form or not
             * If yes, set personId value to empty string and perform search
             * only with the entered personId

            if(personName!=null && !personName.equals("")){
                personId = "";
            }else{
                personId = personInfoBean.getPersonID();
            }
            status = coiDisclosureActionForm.getStatus();
            appliesTo = coiDisclosureActionForm.getAppliesTo();
            awardProposalNo = coiDisclosureActionForm.getAwardProposalNum();
            type = coiDisclosureActionForm.getType();

            //search for the personId
            SearchBean searchBean = new SearchBean( personId );

            //get all COIDisclosures
            Vector collCOIDisclosures = searchBean.getCOIDisclosures(
                    personId,personName,status ,
                            appliesTo , awardProposalNo , type );*/
            /* CASE #1046 Comment End */

            /* CASE #1046 Begin */
            /* If user session is really expired then forward the
             * user to session exipiration page
             */
            if( userName == null || personInfoBean == null) {
                actionforward = actionMapping.findForward( EXPIRE );
                return ( actionforward );
            }
            else {
                personId = personInfoBean.getPersonID();
            }
            Vector collCOIDisclosures = new Vector();
            SearchBean searchBean = null;
            COIDisclosureActionForm coiDisclosureActionForm =
                ( COIDisclosureActionForm ) actionForm;
            /*
             * If a incoming request is getcoidisclosure then user is taking
             * list, not doing a customization.
             */
            if( actionMapping.getPath().equals( "/getcoidisclosure" ) ) {
                coiDisclosureActionForm.setPersonId(personInfoBean.getPersonID());
                coiDisclosureActionForm.setPersonName(personInfoBean.getFullName());
                request.removeAttribute("customizedListIntroduction");
                searchBean = new SearchBean(personId);
                //get all COIDisclosures
                collCOIDisclosures =
                    searchBean.getInitialCOIDisclosures( personId );
            }
            //request is coming from user-entered customized list criteria
            else if( actionMapping.getPath().equals("/coidisclosure") ) {
                //change the text for the introduction
                request.setAttribute("customizedListIntroduction",
                    "customizedListIntroduction");
                //process the form information
                personId = coiDisclosureActionForm.getPersonId();
                personName = coiDisclosureActionForm.getPersonName();

                /*
                 * Check whether the person name control is there with form or not
                 * If yes, set personId value to empty string and perform search
                 * only with the entered personId
                 */
                if(personName!=null && !personName.equals("")){
                    personId = "";
                }else{
                    personId = personInfoBean.getPersonID();
                }
                status = coiDisclosureActionForm.getStatus();
                appliesTo = coiDisclosureActionForm.getAppliesTo();
                awardProposalNo = coiDisclosureActionForm.getAwardProposalNum();
                type = coiDisclosureActionForm.getType();

                //search for the personId
                searchBean = new SearchBean( personId );

                //System.out.println("call getCOIDisclosures with: "+personId+", "+personName);

                //get all COIDisclosures
                collCOIDisclosures = searchBean.getCOIDisclosures(
                        personId,personName,status ,
                                appliesTo , awardProposalNo , type );

            }
            /* CASE #1046 End */
            //get All coi disclosures Status
            ArrayList collStatus = searchBean.getDisclosureStatus();

            /* With STRUTS 1.0.2  it is not possible for  a jsp page to retain the data
             * which is in request scope once user come back to the same page
             * after form validation errors occur, as the form validations
             * are done at server side and request becomes new in which only
             * formbean information is held.
             *
             * So store the information in session
             */
            session.setAttribute( "collCOIDisclosures" , collCOIDisclosures );
            session.setAttribute( "collCOIDisclosuresStatus" , collStatus );

        } catch( CoeusException coeusEx ) {
            errorFlag = true;
            UtilFactory.log( coeusEx.getMessage() , coeusEx , "COIDisclosureAction" ,
                    "perform()" );
            request.setAttribute( "EXCEPTION" , coeusEx );
        } catch( DBException dbEx ) {
            errorFlag = true;
            /* CASE #735 Comment Begin */
            //DBEngine prints the exception to log file.
           // UtilFactory.log( dbEx.getMessage() , dbEx , "COIDisclosureAction" ,
                   // "perform()" );
            /* CASE #735 Comment End */
            request.setAttribute( "EXCEPTION" , dbEx );
        }catch( Exception ex ) {
            errorFlag = true;
            UtilFactory.log( ex.getMessage() , ex ,
                                "COIDisclosureAction" , "perform()" );
            request.setAttribute( "EXCEPTION" ,
                                new CoeusException( "exceptionCode.60007" ) );
        }
        // forward to FAILURE/SUCCESS page based on errorFlag status
        if( errorFlag ) {
            actionforward = actionMapping.findForward( FAILURE );
        } else {
            actionforward = actionMapping.findForward( SUCCESS );
        }
        return actionforward;
    }//end of perform method
}