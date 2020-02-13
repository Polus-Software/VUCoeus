/*
 * @(#)FinancialEntityDetailsAction.java	1.0	06/06/2002 22:44:23
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
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ForwardingActionForward;
import java.util.Vector;
import java.util.LinkedList;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.sql.Timestamp;

import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;
/* CASE #734 Comment Begin */
//import edu.mit.coeus.coi.exception.CoeusException;
/* CASE #734 Comment End */
/* CASE #734 Begin */
import edu.mit.coeus.exception.CoeusException;
/* CASE #734 End */
import edu.mit.coeus.coi.bean.FinancialEntityDetailsBean;
import edu.mit.coeus.coi.bean.CertificateDetailsBean;
import edu.mit.coeus.coi.bean.EntityDetailsBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.coi.bean.DisclosureValidationBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.action.common.CoeusActionBase;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;

/**
 * <code>FinancialEntityDetailsAction</code> is a struts implemented Action class
 * to Add/Update financial entity details to the <code>coeus</code> database.
 * This class looks for a form bean <code>FinancialEntityDetailsActionForm</code>
 * in request scope for Financial Entity Details and Questions/Answers
 * to add/modify a Financial Entity.
 *
 * @version 1.0 June 6,2002
 * @author RaYaKu
 */
public class FinancialEntityDetailsAction extends CoeusActionBase{

    /**
     * Process the specified HTTP request, and create the corresponding
     * HTTP response(or forward to another web component that will create it).
     * Return an ActionForward instance describing where and how control
     * should be forwarded, or null if the response has already been completed.
     *
     * <br> The method used to perform Add/Update a particular Financial Entity
     * to the <code>coeus</code> database after all doing all necessary validations.
     * It will extract all the required parameters from the actionForm object and
     * set it to <code>EntityDetailsBean</code> and <code> CertificateDetailsBean</code>.
     * According to the type of the request, the bean instance will be passed to
     * the <code>addUpdateEntityDetails</code> method of
     * <code>FinancialEntityDetailsBean</code> class.
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
            ActionForm actionForm ,  HttpServletRequest request ,
            HttpServletResponse response ) throws IOException , ServletException{
        /* CASE #665 Comment Begin */
        //final String TO_ANNUAL_DISCL_PAGE = "success1";
        //final String TO_FIN_ENT_PAGE = "success2";
        /* CASE #665 Comment End */
        String personId = null;
        String personName = null;
        String userName = null;
        PersonInfoBean personInfoBean = null;
        HttpSession session = null;
        ActionForward actionforward = actionMapping.findForward( SUCCESS );
//        UtilFactory UtilFactory = new UtilFactory();
        boolean errorFlag = false;
        boolean sponsorValid = true;
        ActionErrors actionErrors = new ActionErrors();
        String actionFrom = null;  // request coming from
        /* CASE #1402/1374 Begin */
        String defaultActionType = CoeusProperties.getProperty(CoeusPropertyKeys.DEFAULT_ACTION_TYPE);
        final String DEFAULT_ORG_RELATIONSHIP = "X";
        final String ACTIVE_STATUS = "1";
        final String INACTIVE_STATUS = "2";
        EntityDetailsBean entityDetailsBean = null;
        /* CASE #1402/1374 End */
        try {
            System.out.println("inside FinancialEntityDetailsAction.perform()");
            session = request.getSession( true );
            //look username attribute in session scope
            userName = ( String ) session.getAttribute( USERNAME );
            /*
             * If userName information is not available in session scope then
             * supply him a session expiration page
             */
            if( userName == null ) {
                actionforward = actionMapping.findForward( EXPIRE );
                return ( actionforward );
            }
            // look personInfo attribute in session scope
            personInfoBean = ( PersonInfoBean ) session.getAttribute( PERSONINFO );
            if( personInfoBean != null ) {
                personId = personInfoBean.getPersonID();
                /*CASE #212 Begin */
                String loggedinpersonid =
                    (String)session.getAttribute(LOGGEDINPERSONID);
                String userprivilege = (String)session.getAttribute("userprivilege");
                if((!loggedinpersonid.equals(personId)) &&
                    (Integer.parseInt(userprivilege) != 2)){
                      personId = loggedinpersonid;
                      personName = (String)session.getAttribute(LOGGEDINPERSONNAME);
                }
                else{
                /* CASE #212 End */
                  personName = personInfoBean.getFullName();
                }
            }
            FinancialEntityDetailsActionForm financialEntityDetailsActionForm =
                                ( FinancialEntityDetailsActionForm ) actionForm;

            //get the current date
            FinancialEntityDetailsBean financialEntityDetailsBean
                        = new FinancialEntityDetailsBean( "Coeus" , "Coeus" ,
                                        personId.trim() );
            Timestamp currentDate = financialEntityDetailsBean.getTimestamp();
            //get the lastUpdateTimestamp info from form bean and convert it back to Timestamp format.
            String lastUpdateTimestamp =
                        financialEntityDetailsActionForm.getLastUpdateTimestamp();
            Timestamp convertLastUpdate = null;
            if( lastUpdateTimestamp != null ) {
                convertLastUpdate = Timestamp.valueOf( lastUpdateTimestamp );
            }
            /* Populate Form values */
            //  Get form information to process(Update/Add/Delete) the Financial Entity.
            String actionType = financialEntityDetailsActionForm.getActionType();
            String entityNumber = financialEntityDetailsActionForm.getNumber();
            String entityType = financialEntityDetailsActionForm.getTypeCode();
            /*CASE #1374 Comment entityStatus. no longer included on form. */
            //String entityStatus = financialEntityDetailsActionForm.getStatus();
            String entityName = financialEntityDetailsActionForm.getName();
            String shareOwnership = financialEntityDetailsActionForm.getShareOwnership();
            //entityDescription collected only for deactivate actionMapping
            String entityDescription = financialEntityDetailsActionForm.getDescription();
            String personRelationType = financialEntityDetailsActionForm.getPersonRelationType();
            String personRelationTypeCode = financialEntityDetailsActionForm.getPersonRelationTypeCode();
            String personRelationDesc = financialEntityDetailsActionForm.getPersonRelationDesc();
            /* CASE #1374 Comment Always use default value for org relationship type.*/
            //String orgRelationType = financialEntityDetailsActionForm.getOrgRelationType();
            //will be null, since we are not currently asking for org relationship explantion
            //String orgRelationDesc = financialEntityDetailsActionForm.getOrgRelationDesc();
            String updateUser = financialEntityDetailsActionForm.getUserName();
            String sequenceNum = financialEntityDetailsActionForm.getSequenceNum();
            actionFrom = financialEntityDetailsActionForm.getActionFrom();

            /* CASE #1374 Begin */
            System.out.println("actionmapping: "+actionMapping.getPath());
            System.out.println("actionFrom: "+actionFrom);
            
            if(actionMapping.getPath().equals("/deactivateFinEnt") ){
                    //System.out.println("actionmapping is deactivate");
                    entityNumber = request.getParameter("entityNo");
                    System.out.println("entityNumber: "+entityNumber);
                    entityDetailsBean =
                            financialEntityDetailsBean.
                            getFinancialEntityDetails(entityNumber);
                    entityDescription = entityDetailsBean.getEntityDescription();
                    financialEntityDetailsActionForm.setNumber(entityNumber);
                    financialEntityDetailsActionForm.setName(
                                    entityDetailsBean.getName());
                    if(actionFrom != null && actionFrom.equals("AnnualFE") ){
                        financialEntityDetailsActionForm.setActionFrom("AnnualFE");
                    }
                    session.setAttribute("entityDetails", entityDetailsBean);
                    return actionforward;
            }
            else if( actionMapping.getPath().equals("/deactivateFinEnt2") ||
                actionMapping.getPath().equals("/activateFinEnt") ){
                    System.out.println("actionmapping: "+actionMapping.getPath());
                    if(actionMapping.getPath().equals("/deactivateFinEnt2")){
                        entityDetailsBean = 
                            (EntityDetailsBean)session.getAttribute("entityDetails"); 
                        entityDetailsBean.setStatusCode(INACTIVE_STATUS);
                        entityDetailsBean.setEntityDescription(
                            financialEntityDetailsActionForm.getDescription());
                        request.setAttribute("actionType", "deactivate");
                    }
                    else{//activate
                        entityNumber = request.getParameter("entityNo");
                        entityDetailsBean =
                            financialEntityDetailsBean.
                            getFinancialEntityDetails(entityNumber);
                        entityDetailsBean.setStatusCode(ACTIVE_STATUS);
                        /* CASE #1619 Begin */
                        entityDetailsBean.setEntityDescription("");
                        /* CASE #1619 End */
                        actionFrom = request.getParameter("actionFrom");
                        request.setAttribute("actionType", "activate");
                        //System.out.println("entityNumber: "+entityNumber);
                    }
                    int seqNumber = 
                        Integer.parseInt(entityDetailsBean.getSeqNumber()) +1;
                    entityDetailsBean.setSeqNumber(String.valueOf(seqNumber) );
                    Vector collCertificates = new Vector(); //empty vector
                    financialEntityDetailsBean.addUpdateEntityDetails
                        (entityDetailsBean, collCertificates);
                    request.setAttribute("entityNumber", entityNumber);
                    request.setAttribute("entityName", entityDetailsBean.getName());                    
                    request.setAttribute("FESubmitSuccess", "FESubmitSuccess"); 
                    if(actionFrom != null && actionFrom.equals("AnnualFE") ){
                        actionforward = actionMapping.findForward("toAnnDiscFE");
                    }                     
                    return actionforward;
            }
            /* CASE #1374 Begin */
            if(actionFrom != null){                
                if(actionFrom.equals("addDiscl" ) ){
                    System.out.println("set addDiscl as actionFrom in request");
                    request.setAttribute("actionFrom", "editFinEnt");
                    actionforward = actionMapping.findForward("toAddDiscl");
                }
                else if(actionFrom.equals("editDiscl") ){
                    System.out.println("Set actionFrom to editFinEnt");
                    request.setAttribute("actionFrom", "editFinEnt");
                    actionforward = actionMapping.findForward( "toEditDiscl" );
                }
                else if(actionFrom.equals("annDiscCert") ){
                    request.setAttribute("actionFrom", "editFinEnt");
                    actionforward = actionMapping.findForward("toAnnDiscCert");
                }
                else if(actionFrom.equals("AnnualFE") ){
                    actionforward = actionMapping.findForward("toAnnDiscFE");
                }                
            }            
            /* CASE #1374 End */

            /* CASE #1400 Begin*/
            //if this is an update, increment the sequence number
            if(actionType != null && !actionType.equals(defaultActionType)){
                int seqNo = Integer.parseInt(sequenceNum);
                sequenceNum = String.valueOf(seqNo +1);
            }
            /* CASE #1400 End */
            /*End of population form values */

            /* CASE #1374 Comment Begin */
            //Not collecting sponsor info.
            /* Validate the sponsor */
            /*String sponsorId = financialEntityDetailsActionForm.getSponsorId();
            if( sponsorId == null ) {
                sponsorId = "";
            }
            String sponsorName = financialEntityDetailsActionForm.getSponsorName();
            if( sponsorName == null ) {
                sponsorName = "";
            }*/
            /* CASE #1374 Comment End */

            /* CASE #1402 Begin */
            // If form is expired, check if seq no for this entity number already exists.
            //If not, process the update. Make changes below to check for this boolean.
            boolean entSeqNoExists = false;
            /* CASE #1402 End */


            /*CASE #1400 Begin */
            //Compare values from actionForm with values from original entityDetailsBean.
            //If there are no changes, don't do an update.
            entityDetailsBean =
                (EntityDetailsBean)session.getAttribute("originalEntityDetails");
            boolean entityChanged = false;
            if(!actionType.equals(defaultActionType)){
                if(entityDetailsBean == null){
                    entityDetailsBean =
                        financialEntityDetailsBean.getFinancialEntityDetails(entityNumber);
                }
                if( !entityType.equals(entityDetailsBean.getTypeCode()) ){
                    entityChanged = true;
                    entityDetailsBean.setTypeCode( entityType );                    
                    System.out.println("entityType changed");
                }
                if( !entityName.equals(entityDetailsBean.getName()) ){
                    entityChanged = true;
                    entityDetailsBean.setName( entityName );                  
                    System.out.println("entity name changed");
                }
                if( !(shareOwnership.equals(entityDetailsBean.getShareOwnship())
                || (shareOwnership.equals("")
                       && entityDetailsBean.getShareOwnship() == null) ))  {
                    entityChanged = true;
                    entityDetailsBean.setShareOwnship( shareOwnership );                    
                    System.out.println("shareOwnership:**"+shareOwnership);
                    System.out.println("entityDetailsBean: "+entityDetailsBean.getShareOwnship());
                    System.out.println("share ownership changed");
                }
                if( !personRelationTypeCode.equals(entityDetailsBean.getPersonReTypeCode()) ){
                    entityChanged = true;
                    entityDetailsBean.setPersonReTypeCode( personRelationTypeCode );                   
                    System.out.println("person relationship type code changed");
                }
                if( !(personRelationDesc.equals(entityDetailsBean.getPersonReDesc()) ||
                    (personRelationDesc.equals("") 
                        && entityDetailsBean.getPersonReDesc() == null) ) ){
                    entityDetailsBean.setPersonReDesc( personRelationDesc );                        
                    entityChanged = true;
                    System.out.println("person relation desc changed");
                    if(actionFrom != null && ( actionFrom.equals("addDiscl") ||
                        actionFrom.equals("editDiscl") ) ){
                            session.setAttribute("updatePersonReDesc", "updatePersonReDesc");
                        System.out.println("put updatePersonReDesc in session");
                    }
                }
            }//end if !actionType.equals(defaultActionType)
            else{ //actionType is defaultActionType
                entityDetailsBean = new EntityDetailsBean();
                entityDetailsBean.setNumber( entityNumber );
                entityDetailsBean.setPersonReDesc( personRelationDesc );
                entityDetailsBean.setPersonReTypeCode( personRelationTypeCode );
                entityDetailsBean.setName( entityName );
                entityDetailsBean.setTypeCode( entityType );
                entityDetailsBean.setStatusCode( ACTIVE_STATUS );
                entityDetailsBean.setShareOwnship( shareOwnership );
                if(actionFrom != null && ( actionFrom.equals("addDiscl") ||
                actionFrom.equals("editDiscl") ) ){
                    session.setAttribute("addFinEnt", "addFinEnt");
                    System.out.println("put addFinEnt in session");
                }
            }
            /* CASE #1400 End */

            /* CASE #665 Begin */
            //Check for valid token associated with form.  If invalid, then this
            //is a duplicate submission.  Take user to error page, with appropriate
            //error msg based on whether this is an add or edit.
            if(!isTokenValid(request)){
                entSeqNoExists = financialEntityDetailsBean.checkEntSeqNoExists
                        (entityNumber, sequenceNum);
                if(entSeqNoExists){
                    //CASE #1400 display dup submission error only  if entity has changed. 
                    //else, just skip the update
                    if(entityChanged){
                        /* CASE #1221 Comment Begin */
                        // seq no no longer needed in url for display entity
                        //int newSequenceNum = Integer.parseInt(sequenceNum)+1;
                        //request.setAttribute("seqNo", newSequenceNum+"");
                        /* CASE #1221 Comment End */
                        request.setAttribute("actionFrom", actionFrom);
                        if(actionType.equalsIgnoreCase(defaultActionType)){
                            request.setAttribute("actionType", actionType);
                            throw new CoeusException("exceptionCode.80010");
                        }
                        else{
                            request.setAttribute("actionType", actionType);
                            throw new CoeusException("exceptionCode.80011");
                        }
                    }
                    else{
                        return actionforward;
                    }
                }
            }
            if(isTokenValid(request) || !entSeqNoExists || !entityChanged){
                resetToken(request);
            }
            /* CASE #665 End */            
            //DisclosureValidationBean disclosureValidationBean = new DisclosureValidationBean();
            /* For this release, don't check for organizational relationship to entity.
            if(orgRelationType.trim().equals("Y") || !"".equals(sponsorId.trim())){
                sponsorValid = disclosureValidationBean.isSponsorNumValid( sponsorId , sponsorName );
            }
            */
            /* In this release, sponsor information is not requested on financial entity
            details page. */
            /*
            if(!sponsorId.trim().equals("")){
                System.out.println("sponsor code " + sponsorId);
                sponsorValid = disclosureValidationBean.isSponsorNumValid( sponsorId , null ); // sending sponsor name as null - sponsor name not required here
                System.out.println("sponsor valid " + sponsorValid);
            }else {
                if(orgRelationType.trim().equals("Y") && sponsorId.trim().equals("")){
                  sponsorValid = false;
                }
            }

            if( !sponsorValid ) {
                //throw error to user
                actionErrors.add( "Invalid sponsor" ,
                        new ActionError( "error.financialEntity.invalidSponsor" ) );
                saveErrors( request , actionErrors );
                //forward to invalidsponsor
                actionforward = actionMapping.findForward( "invalidSponsor" );
                return actionforward;
            }*/
            /* end of sponsor validation */
            /* CASE #1374 Comment Begin */
            /*entityDetailsBean = new EntityDetailsBean();
            entityDetailsBean.setSeqNumber( sequenceNum );
            entityDetailsBean.setNumber( entityNumber );
            entityDetailsBean.setUpdateUser( updateUser );
            entityDetailsBean.setLastUpdate( convertLastUpdate );
            entityDetailsBean.setSponsor( sponsorId );
            entityDetailsBean.setOrgRelationship( orgRelationType );
            entityDetailsBean.setPersonReDesc( personRelationDesc );
            entityDetailsBean.setPersonReTypeCode( personRelationTypeCode );
            entityDetailsBean.setEntityDescription( entityDescription );
            entityDetailsBean.setName( entityName );
            entityDetailsBean.setTypeCode( entityType );
            entityDetailsBean.setStatusCode( entityStatus );
            entityDetailsBean.setShareOwnship( shareOwnership );*/
            /* CASE #1374 Comment End */

            /* Recieve all question id,description their answers from request scope */

            /*
             * Taking the number of questions from the request.
             * Taking all the question ids and answers by loopin thru this number.
             * For each question, perform updation by using addUpdateCert()
             *
             * NOTE: Struts1.0.2 do not support dynamic form properties
             */
            /* CASE 1400 Begin */
            //Get original answers to the questions.
            Vector originalEntityCertDetails =
                (Vector)session.getAttribute("originalEntityCertDetails");
            if(!actionType.equals(defaultActionType) && originalEntityCertDetails == null){
                new CoeusException( "exceptionCode.80005" );
            }
            boolean certAnswersChanged = false;
            boolean persFinIntDiscUpdated = false;
            /* CASE #1400 End */
            Vector collCertificates = new Vector( 2 , 1 );
            String[] collQuestionIDs = request.getParameterValues( "hdnQuestionID" );
            String[] collQuestionDescs = request.getParameterValues( "hdnQuestionDesc" );
            String[] collQSeqNumbers = request.getParameterValues( "hdnQSeqNum" );
            String explanation = request.getParameter( "explanation" );
            if( collQuestionIDs != null ) {
                for( int questionIndex = 0 ; questionIndex < collQuestionIDs.length ; questionIndex++ ) {
                    String answer = request.getParameter( collQuestionIDs[ questionIndex ] );
                    /* Validation done in AcitonForm class */
                    //if answer is null throw exception
                    /*if( answer == null ) {
                        throw new CoeusException( "Please answer all the questions" );
                    }*/
                    /* CASE #1400 Begin */
                    //Compare with original answer.
                    if(!actionType.equals(defaultActionType)){
                        CertificateDetailsBean originalCertDetails =
                            (CertificateDetailsBean)originalEntityCertDetails.get(questionIndex);
                        String origAnswer = originalCertDetails.getAnswer();
                        if( !answer.equals(origAnswer)){
                            certAnswersChanged = true;
                            System.out.println("certAnswerChanged for: "+originalCertDetails.getCode());
                        }
                    }
                        /* CASE #1400 End */
                    CertificateDetailsBean certificateDetailsBean = new CertificateDetailsBean();
                    certificateDetailsBean.setPersonId( personId );
                    certificateDetailsBean.setNumber( entityNumber );
                    //seq no. from before any edits, to be passed as aw_sequence_number.
                    certificateDetailsBean.setSeqNumber( collQSeqNumbers[ questionIndex ] );
                    certificateDetailsBean.setCode( collQuestionIDs[ questionIndex ] );
                    certificateDetailsBean.setQuestion( collQuestionDescs[ questionIndex ] );
                    certificateDetailsBean.setAnswer( answer );
                    certificateDetailsBean.setExplanation( explanation );
                    certificateDetailsBean.setUpdateUser( updateUser );
                    certificateDetailsBean.setLastUpdate( convertLastUpdate );

                    collCertificates.add(certificateDetailsBean);


                }//end for loop
            }//end if collQuestionIDs != null
             /* CASE 1400 Begin */
            //If cert answers changed, set Vector size to 0 so no update to 
            // cert answers will be made.
            if(!actionType.equals(defaultActionType) && !certAnswersChanged){
                collCertificates.removeAllElements();
            }
            //If this is new entity or entity details changed or cert answers changed,
            //need to insert row into osp$fin_int_entity_ynq
            //to keep sequence_number there in sync with seq number
            //in osp$person_fin_int_disclosure
            if(actionType.equals(defaultActionType) || 
                                        entityChanged || certAnswersChanged ) {
                entityDetailsBean.setSeqNumber(sequenceNum);
                entityDetailsBean.setOrgRelationship( DEFAULT_ORG_RELATIONSHIP );
                entityDetailsBean.setUpdateUser( updateUser );
                entityDetailsBean.setLastUpdate( convertLastUpdate );                
                financialEntityDetailsBean.addUpdateEntityDetails
                                (entityDetailsBean, collCertificates);
                UtilFactory.log( "Financial Entity has been successfully added/updated" ,
                    null , "" , "" );
                session.removeAttribute("originalEntityDetails");
                session.removeAttribute("originalEntityCertDetails");
                request.setAttribute("FESubmitSuccess", "FESubmitSuccess");
                request.setAttribute("actionType", actionType);
                request.setAttribute("entityName", entityName);  
                request.setAttribute("entityNumber", entityNumber);
            }
            /* CASE #1400 End */            
            /* CASE #1400 Comment Begin */
            /* Take the appropriate action on this Financial Entity ( Update/Add/Delete) */
            /*if( financialEntityDetailsBean.addUpdateEntityDetails( entityDetailsBean ,
            collCertificates , actionType.trim().toUpperCase() ) ) {
                UtilFactory.log( "Financial Entity has been successfully added/updated" ,
                            null , "" , "" );*/

                /* Unbind the  data of this financial entity from session object */
                /* CASE #819 Comment Begin */
                //Leave info in session for case of back button use after
                //add/edit has been completed.
                /*session.removeAttribute( "collEntityCertDetails" );
                session.removeAttribute( "collOrgTypes" );
                session.removeAttribute( "collEntityStatus" );
                session.removeAttribute( "collRelations" );*/
                /* CASE #819 Comment End */
            //}
            /* CASE #1400 Comment End */
        } catch( CoeusException coeusEx ) {
            errorFlag = true;
            UtilFactory.log( coeusEx.getMessage() , coeusEx ,
                    "FinancialEntityDetailsAction" ,"perform()" );
            request.setAttribute( "EXCEPTION" , coeusEx );
        } catch( DBException dbEx ) {
            errorFlag = true;
            /*CASE #735 Comment Begin */
            //DBEngine will print exception to log file.
            //UtilFactory.log( dbEx.getMessage() , dbEx ,
                    //"FinancialEntityDetailsAction" , "perform()" );
            /* CASE #735 Comment End */
            request.setAttribute( "EXCEPTION" , dbEx );
        } catch( Exception ex ) {
            errorFlag = true;
            UtilFactory.log( ex.getMessage() , ex ,
                    "FinancialEntityDetailsAction" , "perform()" );
            request.setAttribute( "EXCEPTION" ,
                    new CoeusException( "exceptionCode.80005" ) );
        }
        /* CASE #665 Comment Begin */
        //forward the output to failure/success page depends on errorFlag state
        /*if( errorFlag ) {
            actionforward = actionMapping.findForward( FAILURE );
        } else {
            /*
             * if actual request has come from AnnualFE then forward to same(success1)
             * otherwise success2
             */
            /* CASE #427 Comment Begin */
            //if( ( actionFrom != null ) && ( actionFrom.equals( "AnnualFE" ) ) ) {
            /* CASE #427 Comment End */
            /* CASE #427 Begin */
            /* CASE #665 Comment Begin */
           // if( ( actionFrom != null ) && ( actionFrom.equalsIgnoreCase( "annualFE" ) ) ) {
            /* CASE #427 End */
                /*actionforward = actionMapping.findForward( TO_ANNUAL_DISCL_PAGE );
            } else {
                actionforward = actionMapping.findForward( TO_FIN_ENT_PAGE );
            }*/
        //}
        /* CASE #665 Comment End */
        /* CASE #665 Begin */
        //If errors, forward to error page.
        if( errorFlag ){
            actionforward = actionMapping.findForward( FAILURE );
        }
        /* CASE #665 End */
        return actionforward;
    }
}