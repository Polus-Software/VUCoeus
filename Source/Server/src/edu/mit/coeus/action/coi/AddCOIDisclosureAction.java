/*
 * @(#)AddCOIDisclosureAction.java	1.0 06/10/2002 18:34:19
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
import java.util.LinkedList;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;

import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;
/* CASE #734 Comment Begin */
//import edu.mit.coeus.coi.exception.CoeusException;
/* CASE #734 Comment End */
/* CASE #734 Begin */
import edu.mit.coeus.exception.CoeusException;
/* CASE #734 End */
import edu.mit.coeus.action.common.CoeusActionBase;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.coi.bean.CertQuestionDetailsBean;
import edu.mit.coeus.coi.bean.DisclosureDetailsBean;
import edu.mit.coeus.coi.bean.DisclosureHeaderBean;
import edu.mit.coeus.coi.bean.DisclosureInfoBean;
import edu.mit.coeus.coi.bean.FinancialEntityDetailsBean;
import edu.mit.coeus.coi.bean.EntityDetailsBean;
import edu.mit.coeus.coi.bean.ProposalSearchBean;
import edu.mit.coeus.coi.bean.TempProposalTransBean;


/**
 * <code>AddCOIDisclosureAction</code> is a struts implemented Action class
 * to fetch the details from <code>coeus</code> database and perform the necessary
 * validations to create a new disclosure.
 *
 * @version 1.0 June 10,2002
 * @author RaYaKu
 */
public class AddCOIDisclosureAction extends CoeusActionBase{

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an ActionForward instance describing where and how control
     * should be forwarded, or null if the response has already been completed.
     * <br> The method will fetch the required information from the <coed> Coeus</code>
     * database and attach these details with <code>AddCOIDisclosureActionForm</code>
     * bean which has the request scope. And forward the request to
     * <code>addCOIDisclosure.do</code>
     * <br><b>Validations</b>
     * <br><li> Check whether the person has any disclosure or not.
     * If the person does not have any disclosure, the disclosure type set as
     * <code>Initial</code>.
     * <li> Check the selected option, If it is <code>award</code> fetch the award details by
     * using <code>getCOIAwardInfo</code> of <code>DisclosureDetailsBean</code> class.
     * If it is <code>proposal</code>, fetch the proposal details by using
     * <code>getCOIProposalInfo</code>
     * of <code>DisclosureDetailsBean</code> class.
     * If the user chooses to create a temporary proposal, forward the request to a page where
     * user can create a temporary proposal and come back to create
     * a new COIDisclosure for that proposal.
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

        System.out.println("begin AddCOIDisclosureAction");

        //CASE #1374 Comment next line
        //final String kDefaulDisclosureTypeCode =  "2";
        final String kDisclNumberFmtr = "0000000000";
        final String kAnnualDisclType = "A";
        final String kInitialDisclType = "I";
        final int kAppliesToAward = 1;
        /* CASE #1374 Begin */
        final String DEV_PROPOSAL_MODULE = "3";
        /* CASE #1374 End */

        String personId = null;
        String personFullName = null;
        String userName = null;
        PersonInfoBean personInfoBean = null;
        HttpSession session = null;
        ActionForward actionforward = actionMapping.findForward( SUCCESS );
//        UtilFactory UtilFactory = new UtilFactory();
        boolean errorFlag = false;
        boolean validResult = false;	// the result of valid award/proposal
        ActionErrors actionErrors = new ActionErrors(); // to hold any validation errors
        
        /* CASE #1374 Begin */
        if(request.getAttribute("EXCEPTION") != null){
            return actionMapping.findForward( FAILURE );
        }
        /* CASE #1374 End */

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
                return ( actionforward );
            }
            // look personInfo in session scope
            personInfoBean = ( PersonInfoBean ) session.getAttribute( PERSONINFO );
            if( personInfoBean != null ) {
                personId = personInfoBean.getPersonID();
                /* CASE #212 Begin */
                /* Check if loggedinpersonid is same as personid.  If not, check
                if user has maintain coi.  If not, then set personid as
                LOGGEDINPERSONID, and personFullName as LOGGEDINPERSONNAME. */
                String loggedinpersonid = (String)session.getAttribute(LOGGEDINPERSONID);
                String userprivilege = (String)session.getAttribute("userprivilege");
                if((!loggedinpersonid.equals(personId)) &&
                      (Integer.parseInt(userprivilege) != 2)){
                      personId = loggedinpersonid;
                      personFullName = (String)session.getAttribute(LOGGEDINPERSONNAME);
                }
                else{
                /* CASE #212 End */
                    personFullName = personInfoBean.getFullName();
                }
            }
            /* CASE #1374 Begin*/
            AddCOIDisclosureActionForm addCOIDisclosureActionForm = null;
            String actionFrom = (String)request.getAttribute("actionFrom");
            if(actionFrom == null){
                   actionFrom = request.getParameter("actionFrom");
            }
            System.out.println("actionFrom: "+request.getAttribute("actionFrom"));
            if( ( actionFrom == null || !actionFrom.equals("editFinEnt")  ) ){
                    System.out.println("construct new AddCOIDisclosureActionForm");
                    addCOIDisclosureActionForm = new AddCOIDisclosureActionForm();
                    session.removeAttribute("certQuestionErrors");
                    session.removeAttribute("selectedQuestions");
                    session.removeAttribute("selectedAnswers");
                    session.removeAttribute("selectedConflictStatus");
                    session.removeAttribute("selectedDescription");
                    session.removeAttribute("typeCode");                    
            }
            else{
                System.out.println("use actionform from session");
                return actionforward;
            }
            /* CASE #1374 End */
            /* CASE #1374 Comment Begin */
            //AddCOIDisclosureActionForm addCOIDisclosureActionForm = 
                //(AddCOIDisclosureActionForm)actionForm;
            /* CASE #1374 Comment End */

            // get the attributes from request scope by which a new disclosure can be created.
            String appliesToCode
                    = ( String ) request.getAttribute( "appliesToCode" );            
            String disclosureTypeCode
                    = ( String ) request.getAttribute( "disclosureTypeCode" );
            System.out.println("$$$$appliesToCode: "+appliesToCode);
            System.out.println("$$$$disclosureTypeCode: "+disclosureTypeCode);
            /* CASE #1374 Comment Begin */
            /*if( disclosureTypeCode == null ) {
                disclosureTypeCode = kDefaulDisclosureTypeCode; //set to dev proposal
            }*/
            /* CASE #1374 Begin */
            if (disclosureTypeCode == null ) {
                disclosureTypeCode = DEV_PROPOSAL_MODULE;
            }
            if(disclosureTypeCode.equals(DEV_PROPOSAL_MODULE)){//dev proposal
                TempProposalTransBean tempProposalTrans = 
                                        new TempProposalTransBean();
                appliesToCode = tempProposalTrans.getNextTempLogNumDisc();
            }           
            /* CASE #1374 End */
            DisclosureDetailsBean disclosureDetailsBean
                    = new DisclosureDetailsBean( personId.trim() );
            //initialize the formatter
            DecimalFormat decimalFormat = new DecimalFormat( kDisclNumberFmtr );

            //get all COI Disclosure Information of a person
            Vector collCOIDisclosureInfo = disclosureDetailsBean.getCOIDisclInfoForPerson();
            /* CASE #236 Begin */
            /* For each financial entity, check whether explanation of person's
            relationship to entity exists in EntityDetailsBean.  If yes, populate
            description field of DisclosureInfoBean with this information, so
            that it will show up on Add Disclosure Form.*/
            for(int i=0; i<collCOIDisclosureInfo.size(); i++){
                DisclosureInfoBean disclosureInfo =
                    (DisclosureInfoBean)collCOIDisclosureInfo.elementAt(i);
                String entityNumber = disclosureInfo.getEntityNumber();
                FinancialEntityDetailsBean financialEntityDetails =
                    new FinancialEntityDetailsBean(personId);
                EntityDetailsBean entityDetails =
                    financialEntityDetails.getFinancialEntityDetails(entityNumber);
                String personReDesc = entityDetails.getPersonReDesc();
                if(personReDesc != null){
                  disclosureInfo.setDesc(personReDesc);
                }
                //collCOIDisclosureInfo.remove(i);
                //collCOIDisclosureInfo.add(i, disclosureInfo);
            }
            /* CASE #236 End */


            String disclosureNum = decimalFormat.format( Integer.parseInt(
                    disclosureDetailsBean.getNextSeqNum() ) );
            /* CASE #1393 Comment Begin */
            //get all COI Disclosure Certificate details
            /*Vector collCOIDiscCertDetails
                    = disclosureDetailsBean.getCOICertificateDetails( disclosureNum );*
            /* CASE #1393 Begin */
            //Get correct answers to cert questions, based on user answers to 
            //fin ent cert questions.
            CertQuestionDetailsBean certQuestionDetails = 
                                new CertQuestionDetailsBean(personId);
            System.out.println("call getAnnDiscCertQuestions");
            Vector collCOIDiscCertDetails = 
                certQuestionDetails.getAnnDiscCertQuestions();
            /* CASE #1393 End */
            /* CASE #1374 Comment Begin */
            /*boolean isPersonHasDiscl = true;
            // check if person has got any disclosures,
            try {
                isPersonHasDiscl
                        = disclosureDetailsBean.isPersonHasDisclosure( 
                                disclosureTypeCode , appliesToCode );
            } catch( Exception ex ) {
                UtilFactory.log(ex.getMessage(), ex, "AddCOIDisclosureAction", "perform");
            }
            //Move to AddNewCOIDisclosureAction.java 
            //Dislclosure type
            /*String disclosureType
                    = isPersonHasDiscl ? kAnnualDisclType : kInitialDisclType;*/
            /* CASE #1374 Comment End */

            DisclosureHeaderBean disclosureHeaderBean = null;
            if( kAppliesToAward == Integer.parseInt( disclosureTypeCode ) ) {
                disclosureHeaderBean = disclosureDetailsBean.getCOIAwardInfo(
                        appliesToCode );
            } else {
                //System.out.println("call disclosureDetailsBean.getCOIProposalInfo() with proposalNo: "+appliesToCode);
                disclosureHeaderBean = disclosureDetailsBean.getCOIProposalInfo(
                        appliesToCode );
            }

            //keep necessary information in form that display these in jsp page
            addCOIDisclosureActionForm.setDisclosureNo( disclosureNum );
            System.out.println("set disclosure no in actionform: "+disclosureNum);
            addCOIDisclosureActionForm.setPersonFullName( personFullName );
            System.out.println("set personFullName in actionform: "+personFullName);
            addCOIDisclosureActionForm.setDisclosureTypeCode( disclosureTypeCode );
            addCOIDisclosureActionForm.setAppliesToCode( appliesToCode );
            /* CASE #1374 Comment next line */
            //addCOIDisclosureActionForm.setDisclosureType( disclosureType );
            addCOIDisclosureActionForm.setUserName( userName );
            /* CASE #1374 Begin */
            addCOIDisclosureActionForm.setPersonId( personId );
            addCOIDisclosureActionForm.setTitle(disclosureHeaderBean.getTitle());
            addCOIDisclosureActionForm.setSponsorName(disclosureHeaderBean.getSponsor());         
            /* CASE #1374 Comment Begin */
            // title, sponsor, and proposal type have been added to AddCOIDisclosureActionForm
            // User will populate on add coi discl page.
            /*if( (disclosureHeaderBean.getTitle() == null )||
                        (disclosureHeaderBean.getTitle().trim().equals("") )  ){
                disclosureHeaderBean.setTitle(
                        (String)session.getAttribute("temporaryDisclosureTitle") );
            }*/

            //keep information in session scope.
            /* CASE #1374 Begin*/
            //Construct ProposalSearchBean in order to get all proposal types for drop down menu. 
            ProposalSearchBean proposalSearchBean = new ProposalSearchBean( personId.trim() );
            Vector collProposalTypes = proposalSearchBean.getProposalTypes();
            //get Conflict Status information and set in the request scope
            LinkedList collCOIStatus = disclosureDetailsBean.getAllCOIStatus();
            session.setAttribute( "collProposalTypes" , collProposalTypes );            
            session.setAttribute( "collCOIStatus" , collCOIStatus );
            //Put actionform in session to be accessed by JSP
            session.setAttribute("frmAddCOIDisclosure", addCOIDisclosureActionForm);            
            /* CASE #1374 End */
            System.out.println("disclosureheaderbean.getAccount: "+disclosureHeaderBean.getAccount());  
            /* CASE #1374 Put disclosureHeaderBean in session only for award or inst prop.*/
            //session.setAttribute( "disclosureHeaderBean" , disclosureHeaderBean );
            if(!disclosureTypeCode.equals(DEV_PROPOSAL_MODULE)){
                session.setAttribute( "disclosureHeaderBean" , disclosureHeaderBean );
            }
            session.setAttribute( "collCOIDiscCertDetails" , collCOIDiscCertDetails );
            session.setAttribute( "collCOIDisclosureInfo" , collCOIDisclosureInfo );
            /* CASE #665 Begin */
            //Put token in session, to be
            //checked before processing the form to avoid duplicate submission.
            saveToken(request);
            /* CASE #665 End */

        } catch( CoeusException coeusEx ) {
            errorFlag = true;
            UtilFactory.log( coeusEx.getMessage() , coeusEx ,
                    "AddCOIDisclosureAction" , "perform()" );
            request.setAttribute( "EXCEPTION" , coeusEx );
        } catch( DBException dbEx ) {
            errorFlag = true;
            /* CASE #735 Comment Begin */
            //DBEngine prints the exception to log file.
            //UtilFactory.log( dbEx.getMessage() , dbEx , "AddCOIDisclosureAction" ,
                   // "perform()" );
            /* CASE #735 Comment End */
            request.setAttribute( "EXCEPTION" , dbEx );
        }/* catch( SQLException sqlEx ) {
            errorFlag = true;
            UtilFactory.log( sqlEx.getMessage() , sqlEx , "AddCOIDisclosureAction" ,
                    "perform()" );
            request.setAttribute( "EXCEPTION" ,
                    new CoeusException( "exceptionCode.60004" ) );
        }*/ catch( Exception ex ) {
            ex.printStackTrace();
            errorFlag = true;
            UtilFactory.log( ex.getMessage() , ex , "AddCOIDisclosureAction" ,
                    "perform()" );
            request.setAttribute( "EXCEPTION" ,
                    new CoeusException( "exceptionCode.60002" ) );
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