/*
 * @(#)CreateTempProposalAction.java 1.0 06/11/2002 12:45:19 PM
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
import javax.servlet.RequestDispatcher;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ForwardingActionForward;
import java.util.Vector;
import java.util.Hashtable;
import java.util.LinkedList;
import java.io.IOException;
import java.sql.SQLException;

import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.coi.bean.*;
import edu.mit.coeus.action.common.CoeusActionBase;
import edu.mit.coeus.bean.PersonInfoBean;


/**
 * <code>CreateTempProposalAction</code> is a struts implemented Action class
 * to create a new Temporary Proposal as part of creating new COI Disclosure
 * and validates Sponsor and Lead Unit codes.
 *
 * @version 1.0 June 10, 2002
 * @author RaYaKu
 */
public class CreateTempProposalAction extends CoeusActionBase{

    /**
     * Process the specified HTTP request, and create the corresponding HTTP response
     * (or forward to another web component that will create it).
     * Return an ActionForward instance describing where and how control
     * should be forwarded, or null if the response has already been completed.
     * <br> The method extracts all the required parameters from the form object
     * and do the following valiadtions
     * <li><code>LeadUnitNumber</code> validation
     * <li><code>SponsorNumber</code> validation
     * <br>If all validations are through, pass these information to <code>add</code> method
     * of <code>TempProposalTransBean</code> to create a temporary proposal.
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

        System.out.println("begin CreateTempProposalAction");

        String personId = null;
        String userName = null;
        PersonInfoBean personInfoBean = null;
        HttpSession session = null;
        ActionForward actionforward = actionMapping.findForward( SUCCESS );
//        UtilFactory UtilFactory = new UtilFactory();
        boolean errorFlag = false;
        boolean validResult = false;	// the result of valid award/proposal
        ActionErrors actionErrors = new ActionErrors(); // to hold any validation errors
        String tempProposalNo = null;
        String disclosureNo = null;
        /* CASE #231 Begin */
        TempProposalDetailsBean tempProposalDetails = null;
        String tempProposalAction = null;
        TempProposalActionForm tempProposalActionForm =
                                        ( TempProposalActionForm ) actionForm;
        TempProposalTransBean tempProposalTransBean = new TempProposalTransBean();
        /* CASE #231 End */

        try {
            session = request.getSession( true );
            // look userName attribute in session scope
            userName = ( String ) session.getAttribute( USERNAME );
            /* If user session is expired then forward the
             * user to session expiration page
             */
            if( userName == null ) {
                actionforward = actionMapping.findForward( EXPIRE );
                return ( actionforward );
            }
            // look personInfo in session scope
            personInfoBean = ( PersonInfoBean ) session.getAttribute( "personInfo" );
            if( personInfoBean != null ) {
                personId = personInfoBean.getPersonID();
            }
            
            /* CASE #1374 Begin */
            //Set lead unit for all temp proposals as default lead unit.
            String defaultLeadUnit = tempProposalTransBean.getDefaultLeadUnit();
            if(defaultLeadUnit == null){
                throw new CoeusException ("exceptionCode.60024");
            }
            /* CASE #1374 End */            

            /* CASE #231 Begin */
            /* Check value of "tempProposalAction" in session.  If value is edit,
            retrieve info for this temp proposal.  Set TempProposalDetailsBean
            and other necessary attributes in session.  Then forward to
            TempProposal.jsp.  Before forwarding, change "tempProposalAction" attribute
            to "update".  */
            tempProposalAction = (String)session.getAttribute("tempProposalAction");
            /* CASE #665 Check for null value of tempProposalAction attribute.   */
            if(tempProposalAction == null){
                request.setAttribute("dupSubmission", "dupSubmission");
                request.setAttribute("dupSubmissionTempProp", "yes");
                throw new CoeusException("exceptionCode.60021");
            }
            /* CASE #665 End */
            if (tempProposalAction.equals("edit")){
                tempProposalNo = request.getParameter("proposalNo");
                disclosureNo = (String)session.getAttribute("disclosureNo");
                TempProposalTransBean tempProposalTrans = new TempProposalTransBean();
                /* Get information for this temp proposal and put in form bean. */
                tempProposalDetails = tempProposalTrans.getTempProposalInfo(tempProposalNo);
                if(tempProposalDetails != null){
                    tempProposalDetails.setProposalNumber(tempProposalNo);
                    tempProposalActionForm.setTitle(tempProposalDetails.getTitle());
                    tempProposalActionForm.setTypeCode
                        (tempProposalDetails.getProposalTypeCode());
                    tempProposalActionForm.setPrimInvestigaterId(tempProposalDetails.getPiId());
                    tempProposalActionForm.setPrimInvestigaterName(tempProposalDetails.getPiName());
                    tempProposalActionForm.setLeadUnit(tempProposalDetails.getLeadUnit());
                    tempProposalActionForm.setSponsorId(tempProposalDetails.getSponsorCode());
                    tempProposalActionForm.setSponsorName(tempProposalDetails.getSponsorName());
                    tempProposalActionForm.setComments(tempProposalDetails.getComments());
                }
                /* Construct ProposalSearchBean in order to get all proposal types for drop down menu. */
                ProposalSearchBean proposalSearchBean = new ProposalSearchBean( personId.trim() );
                Vector collProposalTypes = proposalSearchBean.getProposalTypes();
                session.setAttribute( "collProposalTypes" , collProposalTypes );
                /* Set information in session to be used for update and for returning ro edit disclosure page. */
                session.setAttribute("tempProposalDetails", tempProposalDetails);
                /* Set action in session */
                session.setAttribute("tempProposalAction", "update");
                request.setAttribute( "appliesToCode" , tempProposalNo );
                actionforward = actionMapping.findForward("tempProposalPage");
                return actionforward;
            }
            /* If "tempProposalAction" attribute in session is "update", get
            temp proposal details stored in session, update with values from
            the form, and make database update. If the update is successful,
            set "tempProposalAction" attribute to "complete" and forward
            control to edit disclosure page. */
            else if(tempProposalAction.equals("update")){
                /* Get TempProposalDetailsBean from session. */
                tempProposalDetails = (TempProposalDetailsBean)
                      session.getAttribute("tempProposalDetails");
                /* CASE #1374 Comment Begin */
                //String leadUnit = tempProposalActionForm.getLeadUnit();
                //tempProposalDetails.setLeadUnit(leadUnit);                
                /* CASE #1374 Comment End */
                /* CASE #665 Comment Begin */
                /* Lead unit validation done in ActionForm.
                /* Validate lead unit info from form. */
                /*boolean isLeadUnitValid = false;
                boolean isSponsorValid = false;
                DisclosureValidationBean disclosureValidationBean =
                                                    new DisclosureValidationBean();
                if( ( leadUnit != null ) && ( !leadUnit.equals( "" ) ) ) {
                    isLeadUnitValid = disclosureValidationBean.isLeadUnitNumValid(
                                                                    leadUnit.trim() );
                    //if lead is invalid then collect error info
                    if( !isLeadUnitValid ) {
                        actionErrors.add( "invalidLead" ,
                            new ActionError( "error.tempPrposal.invalidLeadNumber" ) );
                    }
                }
                // check if any errors are caught in actionErrors,
                if( !actionErrors.empty() ) {
                    saveErrors( request , actionErrors );
                    actionforward = actionMapping.findForward( "tempProposalPage" );
                    return actionforward;
                }
                */
                /* CASE #665 Comment End */
                /* Update TempProposalDetailsBean with values from form. */
                /* CASE #1374 Comment Next Line*/
                //tempProposalDetails.setLeadUnit(leadUnit);
                tempProposalDetails.setTitle(tempProposalActionForm.getTitle());
                tempProposalDetails.setPiId
                          (tempProposalActionForm.getPrimInvestigaterId());
                tempProposalDetails.setPiName
                          (tempProposalActionForm.getPrimInvestigaterName());
                tempProposalDetails.setSponsorCode(tempProposalActionForm.getSponsorId());
                tempProposalDetails.setSponsorName
                          (tempProposalActionForm.getSponsorName());
                tempProposalDetails.setComments(tempProposalActionForm.getComments());
                tempProposalDetails.setUpdateUser(userName.trim());
                request.setAttribute( "appliesToCode" , tempProposalNo );
                /* Make the database update. */
                boolean success=tempProposalTransBean.
                                updTempProposalInfo(tempProposalDetails);
                if(success == true){
                  session.setAttribute("tempProposalAction", "complete");
                  disclosureNo = (String)session.getAttribute("disclosureNo");
                  String url = "viewCOIDisclosureDetails.do?action=edit&disclNo=";
                  url+=disclosureNo;
                  RequestDispatcher rd = request.getRequestDispatcher(url);
                  rd.forward(request, response);
                  return null;
                }
            }
            /* CASE #665 Comment Begin */
            /* If "tempProposalAction" attribute in session is "insert", get
            values from form bean stored in session and insert row in database. */
            /*else if(tempProposalAction.equals("insert")){
           /* CASE #231 End */
                // populate the form values
                /*String sponsorNumber = tempProposalActionForm.getSponsorId();
                String sponsorName = tempProposalActionForm.getSponsorName();
                String leadUnit = tempProposalActionForm.getLeadUnit();
                boolean isLeadUnitValid = false;
                boolean isSponsorValid = false;

                DisclosureValidationBean disclosureValidationBean =
                                                    new DisclosureValidationBean();

                // validate the Lead Unit number
                /* Validation done in TempProposalActionForm
                if( ( leadUnit != null ) && ( !leadUnit.equals( "" ) ) ) {
                    isLeadUnitValid = disclosureValidationBean.isLeadUnitNumValid(
                                                                    leadUnit.trim() );
                    //if lead is invalid then collect error info
                    if( !isLeadUnitValid ) {
                        System.out.println("caught invalid unit inside action class");
                        actionErrors.add( "invalidLead" ,
                            new ActionError( "error.tempPrposal.invalidLeadNumber" ) );
                    }
                }
                // validate the Sponsor Number and Name
                /* validation done in TempProposalActionForm
                if( ( sponsorNumber != null && !sponsorNumber.trim().equals( "" ) )
                ||( sponsorName != null && !sponsorName.trim().equals( "" ) ) ) {
                    isSponsorValid = disclosureValidationBean.isSponsorNumValid(
                                    sponsorNumber.trim() , sponsorName.trim() );
                    // if sponsor is invalid then collect error info
                    if( !isSponsorValid ) {
                        actionErrors.add( "invalidSponsor" ,
                        new ActionError( "error.tempPrposal.invalidSponsor" ) );
                    }
                } else {
                    isSponsorValid = true; //default   if no sponsor info is available
                }
                */


                // check if any errors are caught in actionErrors,
                /*if( !actionErrors.empty() ) {
                    saveErrors( request , actionErrors );
                    actionforward = actionMapping.findForward( "tempProposalPage" );
                    return actionforward;
                }
                TempProposalDetailsBean tempProposalDetailsBean = new TempProposalDetailsBean();

                // fill the form bean values into TempProposalDetails

                /*
                 * As the form bean and TempProposalDetailsBean have almost same behaviour
                 * the TempProposalDetailsBean can really be used as a form bean.
                 *
                 * the populate method of RequestUtils is real handy to use (see the description below).
                 * Populate the properties of the specified JavaBean from the specified HTTP request,
                 * based on matching each parameter name (plus an optional prefix and/or suffix)
                 * against the corresponding JavaBeans "property setter" methods in the bean's class.
                 *
                 * but some of the method names are different in these beans,this can't be achieved.
                 */

                /*tempProposalDetailsBean.setProposalTypeCode( tempProposalActionForm.getTypeCode().trim() );
                tempProposalDetailsBean.setTitle( tempProposalActionForm.getTitle().trim() );
                //keep the title in session, PENDING and to be added to db directly See the COEUS-TODO.txt for more description,
                session.setAttribute("temporaryDisclosureTitle",tempProposalActionForm.getTitle().trim());
                tempProposalDetailsBean.setPiId( tempProposalActionForm.getPrimInvestigaterId().trim() );
                tempProposalDetailsBean.setPiName( tempProposalActionForm.getPrimInvestigaterName().trim() );
                tempProposalDetailsBean.setLeadUnit( tempProposalActionForm.getLeadUnit().trim() );
                tempProposalDetailsBean.setSponsorCode( tempProposalActionForm.getSponsorId().trim() );
                tempProposalDetailsBean.setSponsorName( tempProposalActionForm.getSponsorName().trim() );
                tempProposalDetailsBean.setLogStatus( tempProposalActionForm.getLogStatus().trim() );
                tempProposalDetailsBean.setComments( tempProposalActionForm.getComments().trim() );

                // set the username as update user
                tempProposalDetailsBean.setUpdateUser( userName.trim() );

                // create a temporary proposal number
                tempProposalTransBean.add( tempProposalDetailsBean );
                //Get values for drop down list on add disclosure page.
                DisclosureDetailsBean disclosureDetailsBean
                            = new DisclosureDetailsBean( personId.trim() );
                LinkedList collCOIStatus = disclosureDetailsBean.getAllCOIStatus();
                request.setAttribute("personId",personId);
                request.setAttribute( "collCOIStatus" , collCOIStatus );

                // keep the user selected disclosureType and its related information in request scope
                request.setAttribute( "appliesToCode" , tempProposalNo );
            }*/
            /* CASE #665 Comment End */
            /* CASE #665 Begin */
            /* Instead of creating the temp proposal log, then showing the user the
            form to create the disclosure, get the proposal log number, and get
            all the information to create the temp proposal log,  hold the info
            in session, when user successfully submits the disclosure, then create
            the temp proposal log and the disclosure. Use
            tempProposalAction session attribute to control the flow. */
            else if(tempProposalAction.equalsIgnoreCase("insert")){

                TempProposalDetailsBean tempProposalDetailsBean = new TempProposalDetailsBean();
                //Get next temp log number.
                tempProposalNo = tempProposalTransBean.getNextTempLogNumDisc();
                tempProposalDetailsBean.setProposalNumber(tempProposalNo);

                // fill the form bean values into TempProposalDetails
                tempProposalDetailsBean.setProposalTypeCode( tempProposalActionForm.getTypeCode().trim() );
                tempProposalDetailsBean.setTitle( tempProposalActionForm.getTitle().trim() );
                //keep the title in session, PENDING and to be added to db directly See the COEUS-TODO.txt for more description,
                session.setAttribute("temporaryDisclosureTitle",tempProposalActionForm.getTitle().trim());
                /* CASE #1374 Begin */
                session.setAttribute("temporaryDisclosureSpName", tempProposalActionForm.getSponsorName().trim() );
                /* CASE #1374 End */
                tempProposalDetailsBean.setPiId( tempProposalActionForm.getPrimInvestigaterId().trim() );
                tempProposalDetailsBean.setPiName( tempProposalActionForm.getPrimInvestigaterName().trim() );
                /* CASE #1374 Comment Begin */
                //tempProposalDetailsBean.setLeadUnit( tempProposalActionForm.getLeadUnit().trim() );
                /* CASE #1374 Comment End */
                /* CASE #1374 Begin */
                //Set lead unit for all new temp proposals to default lead unit.
                tempProposalDetailsBean.setLeadUnit( defaultLeadUnit );
                System.out.println("lead unit: "+defaultLeadUnit);
                /* CASE #1374 End */
                tempProposalDetailsBean.setSponsorCode( tempProposalActionForm.getSponsorId().trim() );
                tempProposalDetailsBean.setSponsorName( tempProposalActionForm.getSponsorName().trim() );
                tempProposalDetailsBean.setLogStatus( tempProposalActionForm.getLogStatus().trim() );
                tempProposalDetailsBean.setComments( tempProposalActionForm.getComments().trim() );

                // set the username as update user
                tempProposalDetailsBean.setUpdateUser( userName.trim() );
                session.setAttribute("tempProposalDetails", tempProposalDetailsBean);

                DisclosureDetailsBean disclosureDetailsBean
                            = new DisclosureDetailsBean( personId.trim() );
                LinkedList collCOIStatus = disclosureDetailsBean.getAllCOIStatus();
                request.setAttribute("personId",personId);
                request.setAttribute( "collCOIStatus" , collCOIStatus );
                request.setAttribute( "appliesToCode" , tempProposalNo );
                /* CASE #1374 Begin */
                request.setAttribute("disclosureTypeCode", "2");
                /* CASE #1374 End */
            }

        } catch( CoeusException coeusEx ) {
            errorFlag = true;
            UtilFactory.log( coeusEx.getMessage() , coeusEx , "CreateTempProposalAction" ,
            "perform()" );
            request.setAttribute( "EXCEPTION" , coeusEx );
        } catch( DBException dbEx ) {
            errorFlag = true;
            /* CASE #735 Comment Begin */
            //DBEngine prints the exception to log file.
            //UtilFactory.log( dbEx.getMessage() , dbEx , "CreateTempProposalAction" ,
            //"perform()" );
            /* cASE #735 Comment End */
            request.setAttribute( "EXCEPTION" , dbEx );
        }catch( Exception ex ) {
            ex.printStackTrace();
            errorFlag = true;
            UtilFactory.log( ex.getMessage() , ex , "CreateTempProposalAction" ,
            "perform()" );
            request.setAttribute( "EXCEPTION" , new CoeusException( "exceptionCode.60009" ) );
        }
        //if any exceptions were thrown then forward to failure page other wise forward to
        //insert success
        if( errorFlag ) {
            actionforward = actionMapping.findForward( "failure" );
        }
        /* CASE #665 Comment Begin */
       /* else {
            actionforward = actionMapping.findForward( "insertSuccess" );
        }*/
        /* CASE #665 Comment End */
        return actionforward;
    }//end of perform
}