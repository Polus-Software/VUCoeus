/*
 * @(#)ViewCOIDisclosureAction.java 1.0 05/16/2002 16:59:29
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
import org.apache.struts.action.ForwardingActionForward;
import java.util.Vector;
import java.util.LinkedList;
import java.sql.SQLException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.coi.bean.SearchBean;
import edu.mit.coeus.coi.bean.DisclosureDetailsBean;
import edu.mit.coeus.coi.bean.DisclosureHeaderBean;
import edu.mit.coeus.coi.bean.CertificateDetailsBean;
import edu.mit.coeus.coi.bean.ProposalSearchBean;
import edu.mit.coeus.coi.bean.TempProposalDetailsBean;
import edu.mit.coeus.coi.bean.TempProposalTransBean;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.action.common.CoeusActionBase;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;

/**
 * <code>ViewCOIDisclosureAction</code> is a struts implemented action
 * component to view the details(Information,CertificateDetails and Award/Proposal)
 * of a COI Disclsoure.
 *
 * This component displays details of COI Disclosure in both editable/noneditable
 * mode depends on parameter and  requested page.
 *
 * Note: If querystring has got "action" parameter value "edit" then the details
 * of this disclosure will be shown in Editbale mode in a different view component,
 * otherwise details will be shown in Noneditable/label mode.
 *
 * @author RaYaKu
 * @version 1.0 May 16,2002 16:59:29
 */
public class ViewCOIDisclosureDetailsAction extends CoeusActionBase{

    /**
     * Process the specified HTTP request, and create the corresponding HTTP response
     * (or forward to another web component that will create it).
     * Return an ActionForward instance describing where and how control
     * should be forwarded, or null if the response has already been completed.
     *
     * <b>The method uses <code>DisclosureDetailsBean</code> to get the COIDisclosure
     * details from <code>coeus</code> database.
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
        System.out.println("Inside ViewCOIDisclosureDetailsAction");
        String personId = null;
        String personFullName = null;
        String userName = null;
        HttpSession session = request.getSession( true );
        ActionForward actionforward = actionMapping.findForward( "viewDisclosure" );
//        UtilFactory UtilFactory = new UtilFactory();
        PersonInfoBean personInfoBean = null;
        boolean errorFlag = false;
        AddCOIDisclosureActionForm addCOIDisclosureActionForm = null;
        /* CASE #1374 Begin */
        String disclosureTypeCode = null;
        final String DEV_PROPOSAL_MODULE = "3";
        /* CASE #1374 End */
        try {
            String disclosureNo = request.getParameter( "disclNo" );          
            /* CASE #1374 Comment Begin */
            //get the disclosure number from request
            //String disclosureNo = request.getParameter( "disclNo" );
            /* CASE #1374 Comment End */
            //look username attribute in session scope
            userName = ( String ) session.getAttribute( USERNAME );

            //get the action from request
            // the action that user wish to do with this component
            String action = request.getParameter( "action" );

            /* CASE # 352 Comment Begin */
            /*
             * If userName information is not available in session scope then
             * supply him a session expiration page
             */
           /* if( userName == null ) {
                actionforward = actionMapping.findForward( EXPIRE );
                return ( actionforward );
            }*/
            /* CASE #352 Comment End */
            /* CASE #352 Begin */
            /* If userName is null, forward to ValidateUserAction to attempt login
            for this user. */
            if(userName == null){
                String requestedURL = "viewCOIDisclosureDetails.do?action="+action;
                requestedURL += "&disclNo="+disclosureNo;
                request.setAttribute("requestedURL", requestedURL);
                actionforward = actionMapping.findForward(CoeusConstants.LOGIN_COI_KEY );
                return actionforward;
            }
            /* CASE #352 End */
            
            /* CASE #1374 Begin */
            String actionFrom = (String)request.getAttribute("actionFrom");
            if(actionFrom == null){
                actionFrom = request.getParameter("actionFrom");
            }
            System.out.println("actionFrom: "+actionFrom);
            if(actionFrom != null && actionFrom.equals("editFinEnt") ){
                    //disclosureNo = (String)session.getAttribute("disclosureNo");
                    System.out.println("***actionFrom is fromEditFinEnt***");
                    actionforward = actionMapping.findForward("editDisclosure");
                    return actionforward;
            }
            else{
                addCOIDisclosureActionForm = new AddCOIDisclosureActionForm();
                session.removeAttribute("certQuestionErrors");
                session.removeAttribute("typeCode");                
            }
            /* CASE #1374 End */  
            
            // look personInfo attribute in session scope
            personInfoBean = ( PersonInfoBean ) session.getAttribute( "personInfo" );
            if( personInfoBean != null ) {
                personId = personInfoBean.getPersonID();
                personFullName = personInfoBean.getFullName();
            }
            DisclosureDetailsBean disclosureDetailsBean =
                new DisclosureDetailsBean( personId.trim() );
            /* Begin CASE #231 */
            /* Check if user is returning from user making edits to temp
            proposal info. If yes, get previous edits to the page stored in
            session. Else, retrieve information from database. */
            Vector collCOIDisclosureInfo = null;
            Vector collCOICertDetails = null;
            DisclosureHeaderBean discHeaderBean = null;
            /* CASE #1374 Coment Begin */
            //temp proposal info moved to coi form.
            /*String tempProposalAction = (String)session.getAttribute("tempProposalAction");
            if(tempProposalAction != null && tempProposalAction.equals("complete")){
                /* For user returning from edit temp proposal, replace
                DisclosureHeaderBean, as well as collections
                of CertificateDetailsBeans and DisclosureInfoBeans with
                collections stored in session. 
                if(session.getAttribute("collCOICertDetails") != null){
                    collCOICertDetails = (Vector)session.getAttribute("collCOICertDetails");
                }
                if(session.getAttribute("collDisclosureInfo") != null){
                    collCOIDisclosureInfo = (Vector)session.getAttribute("collDisclosureInfo");
                }
                if(session.getAttribute("disclosureHeader") != null){
                    discHeaderBean = (DisclosureHeaderBean)
                            session.getAttribute("disclosureHeader");
                }
                /* For user returning from edit temp proposal, get
                addCOIDisclosureActionForm from session instead of from passed action
                form parameter and DisclosureHeaderBean. 
                addCOIDisclosureActionForm = (AddCOIDisclosureActionForm)
                            session.getAttribute("addCOIDisclosureActionForm");
                /* Set form bean in request so that values will be displayed on the page. 
                request.setAttribute("frmAddCOIDisclosure", addCOIDisclosureActionForm);
                session.removeAttribute("tempProposalAction");
             }*/
             /* CASE #357 Begin */
             /* Check if user is reloading page after synchronizing financial entities.
                If yes, get previous edits to the page stored in request.  Else,
                retrieve information from database. */
              if(request.getAttribute("synchronize") != null){
                  //addCOIDisclosureActionForm = ( AddCOIDisclosureActionForm )actionForm;
                  collCOICertDetails = (Vector)request.getAttribute("collCOICertDetails");
                  discHeaderBean = (DisclosureHeaderBean)
                      request.getAttribute("disclosureHeader");
                  //Retrieve financial entity info for this disclosure.
                  collCOIDisclosureInfo = disclosureDetailsBean.getCOIDisclosureInfo(
                                    disclosureNo.trim() );
              }
             /* CASE #357 End */
             else{ //request is not for synchronize
             /* End Case #231 */
                 /* CASE #1374 Comment next line.*/
                //addCOIDisclosureActionForm = ( AddCOIDisclosureActionForm )actionForm;

                discHeaderBean = disclosureDetailsBean.getCOIDisclosureHeader(
                                    disclosureNo.trim() );
                //get  all disclosure information
                collCOIDisclosureInfo = disclosureDetailsBean.getCOIDisclosureInfo(
                                    disclosureNo.trim() );
                //get  all COICertificateDetails
                collCOICertDetails = disclosureDetailsBean.getCOICertificateDetails(
                                   disclosureNo.trim() );
            }
            DisclosureHeaderBean discHeaderAwPrInfoBean = null;
            String moduleCode = discHeaderBean.getModuleCode();
            String keyNumber = discHeaderBean.getKeyNumber();
            // get Award/proposal information.
            if( Integer.parseInt( moduleCode ) == 1 ) {
                discHeaderAwPrInfoBean = disclosureDetailsBean.getCOIAwardInfo(
                        keyNumber );
            } else {
                discHeaderAwPrInfoBean = disclosureDetailsBean.getCOIProposalInfo(
                        keyNumber );
            }
            request.setAttribute( "action" , action );
            request.setAttribute( "disclosureNo" , disclosureNo );
            request.setAttribute( "disclosureHeader" , discHeaderBean );
            request.setAttribute( "discHeaderAwPrInfo" , discHeaderAwPrInfoBean );
            request.setAttribute( "collCOIDisclosureInfo" , collCOIDisclosureInfo );
            request.setAttribute( "collCOICertDetails" , collCOICertDetails );
            request.setAttribute( "moduleCode" , moduleCode );

            //set above information in session - need to access it during form validation
            session.setAttribute( "action" , action );
            session.setAttribute( "disclosureNo" , disclosureNo );
            session.setAttribute( "disclosureHeader" , discHeaderBean );
            session.setAttribute( "discHeaderAwPrInfo" , discHeaderAwPrInfoBean );
            session.setAttribute( "collCOIDisclosureInfo" , collCOIDisclosureInfo );
            session.setAttribute( "collCOICertDetails" , collCOICertDetails );
            session.setAttribute( "moduleCode" , moduleCode );
            /* CASE # 250 Begin. */
            session.setAttribute( "keyNumber", keyNumber);
            /* CASE # 250 End. */

            /* CASE #352 Begin */
            /* Check user privileges for view and edit. */
            boolean hasRightToView = false;
            boolean hasRightToEdit = false;
            String loggedinpersonid =
                    (String)session.getAttribute(LOGGEDINPERSONID);            
            String strUserprivilege =
              session.getAttribute("userprivilege").toString();
            int userprivilege = Integer.parseInt(strUserprivilege);
            if(userprivilege > 1){
                hasRightToEdit = true;
                hasRightToView = true;
            }
            else if(userprivilege > 0){
                hasRightToView = true;
                /* Is user editing his or her own disclosure? */
                if(discHeaderBean.getPersonId().equals(loggedinpersonid)){
                    /* Check for appropriate disclosure status.*/
                    String disclStatCode = discHeaderBean.getDisclStatCode();
                    if(disclStatCode.equals("100") || disclStatCode.equals("101")
                                            || disclStatCode.equals("104")){
                        hasRightToEdit = true;
                    }
                }
            }
            else if(userprivilege == 0){
                /* Is user editing his or her own disclosure? */
                if(discHeaderBean.getPersonId().equals(loggedinpersonid)){
                    hasRightToView = true;
                    /* Check for appropriate disclosure status.*/
                    String disclStatCode = discHeaderBean.getDisclStatCode();
                    if(disclStatCode.equals("100") || disclStatCode.equals("101")
                                            || disclStatCode.equals("104")){
                        hasRightToEdit = true;
                    }
                }
            }

            /* CASE #653 Begin */
            //If user has no financial entities, then check if user has answered
            //any of the certification questions in such a way that an explanation
            //is required (which would mean that a fin entity is required).  If
            //yes, display a warning.
            if(collCOIDisclosureInfo.size() == 0){
                for(int questionCnt=0; questionCnt<collCOICertDetails.size(); questionCnt++){
                    CertificateDetailsBean certificateDetailsBean =
                        (CertificateDetailsBean)collCOICertDetails.get(questionCnt);
                    if(certificateDetailsBean.getExplReqFor() != null){
                        String explReqFor = certificateDetailsBean.getExplReqFor();
                        //System.out.println("explReqFor: "+explReqFor);
                        String answer = certificateDetailsBean.getAnswer();
                        //System.out.println("answer: "+answer);
                        if(answer != null && explReqFor.indexOf(answer) != -1){
                            request.setAttribute("noFinEntAnsReqExpl", "noFinEntAnsReqExpl");
                        }
                    }
                }

            }
            /* CASE #653 End */

            // Find out the type of action that user supplied/ wants through querystring
            if( ( action != null ) && ( action.equals( "edit" ) ) ) {                
                if(!hasRightToEdit){
                    /*CASE #653 Comment Begin */
                    /*String errorMessage = "You do not have the right to edit this disclosure.  ";
                    errorMessage += "If you believe that you are seeing this message in error, ";
                    errorMessage += "please contact the Office of Sponsored Programs.";
                    throw new CoeusException(errorMessage);*/
                    /* CASE #653 Comment End */
                    /* CASE #653 Begin */
                    throw new CoeusException("exceptionCode.60018");
                    /* CASE #653 End */
                }
                /* Put code table data in request, so that drop down lists can be populated. */
                LinkedList collCOIStatus = disclosureDetailsBean.getAllCOIStatus();
                LinkedList collCOIReviewer = disclosureDetailsBean.getAllReviewers();
                LinkedList collCOIDisclStatus = disclosureDetailsBean.getAllDiscStatus();
                /* CASE #1374 Begin */
                ProposalSearchBean proposalSearchBean = new ProposalSearchBean( personId.trim() );
                Vector collProposalTypes = proposalSearchBean.getProposalTypes();
                session.setAttribute( "collProposalTypes" , collProposalTypes );  
                session.setAttribute( "collCOIStatus" , collCOIStatus );
                session.setAttribute("collCOIReviewer", collCOIReviewer);
                session.setAttribute("collCOIDisclStatus", collCOIDisclStatus);                
                /* CASE #1374 End */
                request.setAttribute( "collCOIStatus" , collCOIStatus );
                request.setAttribute("collCOIReviewer", collCOIReviewer);
                request.setAttribute("collCOIDisclStatus", collCOIDisclStatus);
                
                //keep required information in action form that may be required to show in a page
                addCOIDisclosureActionForm.setAccountType( "U" );
                addCOIDisclosureActionForm.setDisclosureNo( disclosureNo );
                addCOIDisclosureActionForm.setUserName( userName );
                /* CASE #287 Begin */
                addCOIDisclosureActionForm.setDisclosureType( discHeaderBean.getDisclType() );
                /* CASE #287 End */
                /* CASE #1374 Begin */
                addCOIDisclosureActionForm.setTitle(discHeaderAwPrInfoBean.getTitle());
                addCOIDisclosureActionForm.setSponsorName(discHeaderAwPrInfoBean.getSponsor());
                addCOIDisclosureActionForm.setPersonFullName( personFullName );
                /* CASE #1374 End */

                /* CASE #231 Begin */
                /* If the disclosure is for a temporary proposal, show a small edit
                button next to the proposal title, for editing proposal information.
                Read properties file to get letter with which temporary proposal numbers begin.*/
//                InputStream is = getClass().getResourceAsStream("/coeus.properties");
//                Properties properties = new Properties();
//                try {
//                    properties.load(is);
//                }catch (Exception e) {
//                    System.err.println("Can't read the properties file. " +
//                    "Make sure coeus.properties is in the CLASSPATH");
//                    return null;
//                }
               /* String tempProposalBegin = CoeusProperties.getProperty(CoeusPropertyKeys.TEMP_PROPOSAL_BEGIN);
                if(tempProposalBegin != null){
                    boolean showTempProposalEdit = keyNumber.startsWith(tempProposalBegin);
                    if(showTempProposalEdit){
                    request.setAttribute("showTempProposalEdit", "showTempProposalEdit");
                    }
                }
                else{
                    String errorMessage = "Value for TEMP_PROPOSAL_BEGIN not set in ";
                    errorMessage +="coeus.properties.";
                    throw new CoeusException(errorMessage);
                }*/
                /* CASE #231 End */
                /* CASE #357 Begin */
                /* If user's financial entities have changed since the time the
                 * disclosure was created, then give user option to synchronize. */
                boolean requiresSync = disclosureDetailsBean.checkDisclosureRequiresSync(disclosureNo);
                if(requiresSync){
                    request.setAttribute("showSyncButton", "showSyncButton");
                }
                /* CASE #357 End */
                actionforward = actionMapping.findForward( "editDisclosure" );
            } else { // Action is for view.
                /* CASE #352 Begin */
                if(!hasRightToView){
                    /* CASE #653 Comment Begin */
                    /*String errorMessage = "You do not have rights to view this disclosure.  ";
                    errorMessage += "If you believe that you are seeing this message in error, ";
                    errorMessage += "please contact the Office of Sponsored Programs.";
                    throw new CoeusException(errorMessage);*/
                    /* CASE #653 Comment End */
                    /*CASE #653 Begin */
                    throw new CoeusException("exceptionCode.60019");
                    /*CASE #653 End */
                }
                if(hasRightToEdit){
                    request.setAttribute( "showEditButton", "showEditButton" );
                }
                /* CASE #352 End */
                /* CASE #352 Comment Begin */
                /* Change logic for whether user should see edit button. */
                /* CASE #212 Begin. */
                /* Logic for whether to show edit button, based on user's COI rights.
                    If user has Maintain COI, user should see edit button.  If user
                    does not have Maintain COI, user can edit his or her own
                    disclosures with certain specified status codes.
                boolean showEditButton = false;
                /*String strUserprivilege =
                  session.getAttribute("userprivilege").toString();
                int userprivilege = Integer.parseInt(strUserprivilege);
                /* If user has Maintain COI role, always show edit button.
                if(userprivilege == 2){
                  showEditButton = true;
                }
                /* If user doesn't have Maintain COI
                else if(userprivilege < 2){
                  /* Is the user editing his or her own disclosure?
                    String loggedInPersonId =
                        (String)session.getAttribute(LOGGEDINPERSONID);
                    if(loggedInPersonId != null && personId != null
                      && loggedInPersonId.equals(personId)){
                      /* Check for appropriate disclosure status code.
                        String disclStatCode = discHeaderBean.getDisclStatCode();
                        if(disclStatCode.equals("100") || disclStatCode.equals("101")
                                            || disclStatCode.equals("104")){
                          showEditButton = true;
                        }
                    }
                }
                if(showEditButton){
                  request.setAttribute( "showEditButton", new String("showEditButton") );
                }*/
                /* CASE #352 Comment End */
                /* CASE #212 End. */
                /* CASE #1374 Comment next line.*/
                //actionforward = actionMapping.findForward( "viewDisclosure" );
            }
            /* CASE #1374 Begin */
            String tempProposalBegin = CoeusProperties.getProperty(CoeusPropertyKeys.TEMP_PROPOSAL_BEGIN);
            if(tempProposalBegin != null){                
                if(keyNumber.startsWith(tempProposalBegin)){
                    disclosureTypeCode = DEV_PROPOSAL_MODULE;
                }
                else disclosureTypeCode = moduleCode;
                addCOIDisclosureActionForm.setDisclosureTypeCode(disclosureTypeCode);
                System.out.println("set disclosureTypeCode in actionform: "+disclosureTypeCode);
            }
            else{
                String errorMessage = "Value for TEMP_PROPOSAL_BEGIN not set in ";
                errorMessage +="coeus.properties.";
                throw new CoeusException(errorMessage);
            } 
            if(disclosureTypeCode.equals(DEV_PROPOSAL_MODULE) ){
                TempProposalTransBean tempProposalTrans = new TempProposalTransBean();
                TempProposalDetailsBean tempProposalDetails = 
                        tempProposalTrans.getTempProposalInfo(keyNumber);   
                addCOIDisclosureActionForm.setTypeCode
                                (tempProposalDetails.getProposalTypeCode());
                addCOIDisclosureActionForm.setProposalType
                                (tempProposalDetails.getProposalType());
            }                
            addCOIDisclosureActionForm.setPersonId(personId);
            session.setAttribute("frmAddCOIDisclosure", 
                                                addCOIDisclosureActionForm);
            /* CASE #1374 End */            

        } catch( CoeusException coeusEx ) {
            coeusEx.printStackTrace();
            errorFlag = true;
            UtilFactory.log( coeusEx.getMessage() , coeusEx ,
                        "ViewCOIDisclosureDetailsAction" , "perform()" );
            request.setAttribute( "EXCEPTION" , coeusEx );
        } catch( DBException dbEx ) {
            errorFlag = true;
            /* CASE #735 Comment Begin */
            //DBEngine prints the exception to log file.
            //UtilFactory.log( dbEx.getMessage() , dbEx ,
                       // "ViewCOIDisclosureDetailsAction" , "perform()" );
            /* CASE #735 Comment End */
            request.setAttribute( "EXCEPTION" , dbEx );
        }catch( Exception ex ) {
            ex.printStackTrace();
            errorFlag = true;
            UtilFactory.log( ex.getMessage() , ex ,
                        "ViewCOIDisclosureDetailsAction" , "perform()" );
            request.setAttribute( "EXCEPTION" ,
                        new CoeusException( "exceptionCode.60001" ) );
        }

        if( errorFlag ) {
            actionforward = actionMapping.findForward( FAILURE );
        }
        return actionforward;
    }

}