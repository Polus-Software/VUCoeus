/*
 * @(#)AddNewCOIDisclosureAction.java 1.0 06/10/2002 18:34:19
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.action.coi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
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
import java.util.Enumeration;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;
/* CASE #665 Comment Begin */
//import edu.mit.coeus.coi.exception.CoeusException;
/* CASE #665 Comment End*/
/* CASE #665 Begin */
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.coi.bean.TempProposalTransBean;
import edu.mit.coeus.coi.bean.TempProposalDetailsBean;
/* CASE #665 End */
import edu.mit.coeus.coi.bean.CertificateDetailsBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.coi.bean.DisclosureDetailsBean;
import edu.mit.coeus.coi.bean.DisclosureInfoBean;
import edu.mit.coeus.coi.bean.DisclosureHeaderBean;
import edu.mit.coeus.action.common.CoeusActionBase;

/**
 * <code>AddNewCOIDisclosureAction</code> is a struts implemented Action class
 * to create or modify the COI Disclosure.
 * For creating a new or modifying COI Disclosure this component gets user provided information
 * through an action form <code>AddCOIDisclosureActionForm</code> and questions & answers
 * through request object.
 *
 * @version 1.0 June 10,  2002
 * @author RaYaKu
 */

public class AddNewCOIDisclosureAction extends CoeusActionBase{

    private final String defaultReviewerCode = "1";  //PI Code

    private final String defaultStatus = "200";

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an ActionForward instance describing where and how control
     * should be forwarded, or null if the response has already been completed.
     *
     * <br> The method will take all the parameters that required for
     * adding/modifying a COI disclosure from the request object, and
     * attach these details to the following objects.
     * <li><code>DisclosureHeaderBean</code>
     * <li><code>DisclosureInfoBean</code>
     * <li><code>CertificateDetails</code>
     * <br> The objects will be passed to <code>addUpdateCOIDisc</code>
     * method of <code>DisclosureDetailsBean</code> class to perform the add/update
     * a new COI disclosure.
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
        System.out.println("inside AddNewCOIDisclosureAction");
        String personId = null;
        String userName = null;
        final String kdefaultAccountType="I";
        PersonInfoBean personInfoBean = null;
        HttpSession session = null;
        ActionForward actionforward = actionMapping.findForward( SUCCESS );
        ActionErrors actionErrors = new ActionErrors();
//        UtilFactory UtilFactory = new UtilFactory();
        boolean errorFlag = false;
        DisclosureHeaderBean disclosureHeaderBean = null;
        /* CASE #212 Begin */
        String userprivilege = null;
        String loggedinpersonid = null;
        /* CASE #212 End */
        /* CASE #307 Begin */
        Vector allCertDetails = new Vector( 3, 2 );
        Vector allDisclosureInfo = new Vector( 3, 2 );
        /* CASE #307 End */
        /* CASE #357 Begin */
        boolean synchronize = false;
        /* CASE #357 End */
        /* CASE #1374 Begin */
        final String AWARD_MODULE = "1";        
        final String INST_PROPOSAL_MODULE = "2";        
        final String DEV_PROPOSAL_MODULE = "3";        
        final String DISCL_TYPE_INITIAL = "I";
        final String DISCL_TYPE_ANNUAL = "A";
        final String DISCL_STAT_PI_REV = "101";
        final String REVIEWED_BY_PI = "1";        
        final String DEFAULT_SPONSOR_ID = "000001";
        final String DEFAULT_LOG_STATUS = "T";
        final String DEFAULT_STATUS_SEQ_NUMBER = "1";
        TempProposalTransBean tempProposalTrans = null;
        TempProposalDetailsBean tempProposalDetails = null;
        boolean disclStatusChanged = false;
        /* CASE #1374 End */
        try {

            /* CASE #1374 Begin */
            if(request.getAttribute("EXCEPTION") != null){
                return actionMapping.findForward( FAILURE );
            }
            /* CASE #1374 End */            
            
            session = request.getSession( true );

            // look userName attribute in session scope
            userName = ( String ) session.getAttribute( USERNAME );
            /*
             * If user session is  expired then forward the
             * user to session expiration page
             */
            if( userName == null ) {
                actionforward = actionMapping.findForward( EXPIRE );
                return ( actionforward );
            }
            // look personInfo in session scope
            personInfoBean = ( PersonInfoBean ) session.getAttribute( PERSONINFO );
            if( personInfoBean != null ) {
                personId = personInfoBean.getPersonID();
            }

            /*  CASE #212 BEGIN
            If user has done a select person to view another user's disclosures,
            and user does not have maintain coi right, then add a new disclosure
            for loggedinpersonid.
            */
            loggedinpersonid = (String)session.getAttribute(LOGGEDINPERSONID);
            userprivilege = (String)session.getAttribute("userprivilege");
            if((!loggedinpersonid.equals(personId)) &&
                (Integer.parseInt(userprivilege) != 2)){
                    personId = loggedinpersonid;
            }
            /* CASE #212 End */
            //get the form
            AddCOIDisclosureActionForm addCOIDisclosureActionForm = 
                            (AddCOIDisclosureActionForm)actionForm;
            //TempProposalDetailsBean tempProposalDetails =
              //    (TempProposalDetailsBean)session.getAttribute ("tempProposalDetails");
            /* CASE #1374 Comment End */
            /* CASE #852 Begin */
            if(request.getAttribute("synchronize") != null){
                synchronize = true;
            }
            /*case #1374 comment next line*/
            //String tempProposalAction = (String)session.getAttribute("tempProposalAction");
            /* CASE #852 End */
            DisclosureDetailsBean disclosureDetailsBean =
                                        new DisclosureDetailsBean( personId.trim() );
            //get the current timestamp
            Timestamp currentTimeStamp = disclosureDetailsBean.getTimestamp();

            /*  Checking whether the request for update/insert */
            String accountType = addCOIDisclosureActionForm.getAccountType();
            System.out.println("accountType: "+accountType);
            System.out.println("title: "+addCOIDisclosureActionForm.getTitle());
            System.out.println("sponsorName: "+addCOIDisclosureActionForm.getSponsorName());
            String discosureNum = addCOIDisclosureActionForm.getDisclosureNo();
            System.out.println("disclosureNum: "+discosureNum);
            String updateUser = addCOIDisclosureActionForm.getUserName();
            String disclosureType = addCOIDisclosureActionForm.getDisclosureType();
            /* CASE #1374 Begin */
            String disclosureTypeCode = 
                    addCOIDisclosureActionForm.getDisclosureTypeCode();
            System.out.println("disclosureTypeCode: "+disclosureTypeCode);
            String appliesToCode = addCOIDisclosureActionForm.getAppliesToCode();
            /* CASE #1374 End */
            // if request is for adding a new COI Disclosure
            if( accountType.trim().equals("I") ) {

                /* CASE #665 Begin */
                //Check validity of token that was set in request when the form was displayed to
                //the user, to avoid duplicate submission.
                if(!isTokenValid(request)){
                    /* CASE #1402 Begin*/
                    boolean disclosureExists = 
                        disclosureDetailsBean.checkDiscNoExists(discosureNum);
                    if(disclosureExists){
                    /* CASE #1402 End */
                        request.setAttribute("dupSubmission", "dupSubmission");
                        request.setAttribute("disclosureNumEP", discosureNum);
                        throw new CoeusException("exceptionCode.60020");                        
                    }                        
                }
                else{
                    resetToken(request);
                }
                /* CASE #665 End */
                /* CASE #1374 Comment next line */
                //String appliesToCode = addCOIDisclosureActionForm.getAppliesToCode();
                System.out.println("appliesToCode: "+appliesToCode);
                //CASE #1374 commment: String disclosureTypeCode = addCOIDisclosureActionForm.
                    //getDisclosureTypeCode();
                String seqNum = addCOIDisclosureActionForm.getSeqNum();
                String reviewer = addCOIDisclosureActionForm.getReviewer();
                String status = addCOIDisclosureActionForm.getStatus();
                System.out.println("addNewCOIDisclosureAction, status: "+status);
                String statusSeqNum = addCOIDisclosureActionForm.getStatusSeqNum();
                String lastUpdate = null;
                Timestamp lastUpdateTimestamp = null;
                if( lastUpdate == null ) {
                    lastUpdateTimestamp = currentTimeStamp;
                } else {
                    lastUpdateTimestamp = Timestamp.valueOf( lastUpdate );
                }
                /* CASE #1374 Begin */
                //overwrite value in actionform for disclosureType
                boolean isPersonHasDiscl = true;
                // check if person has got any disclosures,
                isPersonHasDiscl
                        = disclosureDetailsBean.isPersonHasDisclosure( 
                                disclosureTypeCode , appliesToCode );                
                disclosureType
                    = isPersonHasDiscl ? DISCL_TYPE_ANNUAL : DISCL_TYPE_INITIAL;               
                /* CASE #1374 End */
                disclosureHeaderBean = new DisclosureHeaderBean();
                disclosureHeaderBean.setDisclosureNo( discosureNum );
                disclosureHeaderBean.setKeyNumber( appliesToCode );
                disclosureHeaderBean.setUpdateUser( updateUser );
                disclosureHeaderBean.setDisclType( disclosureType );//Initial or Annual
                disclosureHeaderBean.setUpdatedDate( lastUpdateTimestamp );
                disclosureHeaderBean.setDisclStatCode( DISCL_STAT_PI_REV );
                disclosureHeaderBean.setReviewerCode ( REVIEWED_BY_PI );
                /* CASE #1374 Comment next line. */
                //disclosureHeaderBean.setStatSeqNumber( statusSeqNum );                
                /* CASE #1374  Begin */
                disclosureHeaderBean.setStatSeqNumber( DEFAULT_STATUS_SEQ_NUMBER );
                /* CASE #1374 End */                
                /* CASE #1374 Comment Begin */
                // disclosureHeaderBean.setModuleCode( disclosureTypeCode );
                /* CASE #1374 Comment End */
                /* CASE #1374 Begin */
                if(disclosureTypeCode.equals(AWARD_MODULE)){
                    disclosureHeaderBean.setModuleCode(AWARD_MODULE);
                }
                else {
                    disclosureHeaderBean.setModuleCode(INST_PROPOSAL_MODULE);
                }
                /* CASE #1374 End */
                /* CASE #1374 Comment Begin */
                /* CASE #665 Begin */
                //Put title and sponsor in DisclosureHeaderBean to be displayed
                //by submit confirmation page.
                /*if(tempProposalDetails != null){
                    disclosureHeaderBean.setSponsor(tempProposalDetails.getSponsorName());
                    disclosureHeaderBean.setTitle(tempProposalDetails.getTitle());
                }
                else{
                    DisclosureHeaderBean disclHeaderSession =
                      (DisclosureHeaderBean)session.getAttribute("disclosureHeaderBean");
                    if(disclHeaderSession != null){
                        disclosureHeaderBean.setTitle(disclHeaderSession.getTitle());
                        disclosureHeaderBean.setSponsor(disclHeaderSession.getSponsor());
                    }
                }*/
                /* CASE #665 End */
                /* CASE #1374 Comment End */

            } else { //Request is for editing a disclosure.

                /* CASE #212 Begin. */
                /* If user does not have maintain COI right, then disclosure
                    status after edits should be PI Reviewed (101).*/
                String disclStatCode = "101";  //PI Reviewed
                if( Integer.parseInt(userprivilege) == 2){
                  disclStatCode = request.getParameter("disclStatCode");
                }
                /* Whenever user is editing his or her own disclosure, after
                edits, Reviewed By should be set to PI. */
                String disclReviewerCode = "1"; //PI
                if(!loggedinpersonid.equals(personId)){
                  disclReviewerCode = request.getParameter("disclReviewerCode");
                }
                /* CASE #212 End. */
                /* CASE #212 Comment Begin. */
                /* String disclStatCode = request.getParameter("disclStatCode");
                String disclReviewerCode = request.getParameter("disclReviewerCode");*/
                /* CASE #212 Comment End. */
                disclosureHeaderBean = disclosureDetailsBean.getCOIDisclosureHeader
                  ( discosureNum );
                /* CASE #1374 Begin */
                //Check if disclsoure status has changed
                if(!disclosureHeaderBean.getDisclStatCode().equals(disclStatCode) ){
                    disclosureHeaderBean.setDisclStatCode( disclStatCode );
                    disclStatusChanged = true;
                }
                /* CASE #1374 End */
                disclosureHeaderBean.setDisclType( disclosureType );
                disclosureHeaderBean.setUpdateUser( updateUser );
                /* CASE #1374 Comment next line */
                //disclosureHeaderBean.setDisclStatCode( disclStatCode );
                disclosureHeaderBean.setReviewerCode(disclReviewerCode );
                
                /* CASE #1374 Comment Begin /* CASE #665 Begin */
                //Put sponsor and title info in DisclosureHeaderBean to be
                //accessed by disclosure submit confirmation page.  Put personName
                //in addCOIDisclosureActionForm
                /*DisclosureHeaderBean discHeaderAwPrInfo=
                    (DisclosureHeaderBean)session.getAttribute("discHeaderAwPrInfo");
                disclosureHeaderBean.setTitle(discHeaderAwPrInfo.getTitle());
                disclosureHeaderBean.setSponsor(discHeaderAwPrInfo.getSponsor());
                System.out.println("AddNewCOIDisclosureActionForm, edit mode, title: "+
                discHeaderAwPrInfo.getTitle());
                addCOIDisclosureActionForm.setPersonFullName
                                              (disclosureHeaderBean.getName());*/
                /* CASE #665 End *//* CASE #1374 Comment End */
            }
            
            /* Collect the Disclosure Information */
            // vector to hold all the disclosure entity details
            Vector collDisclosureInfo = new Vector( 3 , 2 );

            /*
             * Taking all the parameters for editing/adding disclosure details
             * only editable field is sltDisclosure status.
             * Take the array of all edited disclosure status fields
             * If the request comes from the Add page, there will not be any entity
             * details for editing
             * so, get the entity details for a disclosure set the default values,
             * and put it back to the vector.
             * If the request comes from the edit page,
             * Take all the parameters and set to the disclosure info bean
             * and keep it in vector to send to the procedure.
             */
            // will be availabe when modifying the disclosure
            String[] arrConflictStatus = request.getParameterValues( "sltConflictStat" );
            String[] arrEntityNum = request.getParameterValues( "hdnEntityNum" );
            String[] arrEntSeqNum = request.getParameterValues( "hdnEntSeqNum" );
            String[] arrSeqNum = request.getParameterValues( "hdnSeqNum" );
            String[] arrDesc = request.getParameterValues("hdnDescription");
            String[] arrReviewerCode = request.getParameterValues( "sltReviewerCode" );

            if( accountType.trim().equals( "I" ) ) {
            //request is for a new disclosure
                collDisclosureInfo = disclosureDetailsBean.getCOIDisclInfoForPerson();
                int discInfoSize = collDisclosureInfo.size();
                for( int disclosureIndex = 0 ; disclosureIndex < discInfoSize ;
                  disclosureIndex++ )
                {
                  String description = request.getParameter
                    (("description"+disclosureIndex));
                  /* CASE #310 Begin. */
                  String conflictStatusCode = request.getParameter
                    (("sltConflictStat"+disclosureIndex));
                  /* CASE # 310 End. */
                  DisclosureInfoBean disclosureInfoBean =
                  ( DisclosureInfoBean )collDisclosureInfo.elementAt( disclosureIndex );
                  disclosureInfoBean.setReviewerCode( defaultReviewerCode );
                  disclosureInfoBean.setUpdatedBy( updateUser );
                  //disclosureInfoBean.setConflictStatus( arrDisclStatus[ disclosureIndex  ] );
                  /* CASE # 310 Comment Begin. */
                  /*disclosureInfoBean.setConflictStatusCode( defaultStatus );*/
                  /* CASE #310 Comment End. */
                  /* CASE # 310 Begin. */
                  disclosureInfoBean.setConflictStatusCode( conflictStatusCode );
                  /* CASE # 310 End. */
                  //disclosureInfoBean.setDesc(arrDesc[disclosureIndex ]);
                  disclosureInfoBean.setDesc(description);
                //  disclosureInfoBean.setConflictStatus( defaultStatus );
                  collDisclosureInfo.setElementAt( disclosureInfoBean , disclosureIndex );
                }

              //request from the edit page
            } else {
            /* CASE #852 Comment Begin */
            /* CASE #357 Begin */
            /* Use a workaround to determine if user has selected the "Sync"
                submit button.  If yes, skip this update of financial entity
                information.  Call stored procedure to synchronize financial
                entities and then reload the page.
                Enumeration attr = request.getParameterNames();
                String att = null;
                while(attr.hasMoreElements()){
                    att = (String)attr.nextElement();
                    if(att.equals("Synchronize Financial Entities.x")){
                        synchronize = true;
                    }
                }
                /* CASE #852 Comment End */
                /*Check for null value for arrEntityNum.  If this variable is null, then user
                has no financial entities.  Skip the update of DisclosureInfoBeans. */
                if(!synchronize && arrEntityNum != null){
                /* CASE #357 End */
                /* CASE #357 Comment Begin */
                //if(arrEntityNum != null){
                /* CASE #357 Comment End */
                    /* CASE #1374 Begin */
                    //Put any entities for which conflict status has changed
                    //into Vector to be updated.
                    /*Vector sessionDisclInfo = 
                        (Vector)session.getAttribute("collCOIDisclosureInfo");*/
                    Vector DBDisclInfo = disclosureDetailsBean.getCOIDisclosureInfo
                                                            ( discosureNum );
                    DisclosureInfoBean disclosureInfoBean = null;
                    boolean entDetailsChanged = false;
                    for( int discIndex = 0 ; discIndex < DBDisclInfo.size() ;
                                                                discIndex++ ) {
                      entDetailsChanged = false;
                      disclosureInfoBean = ( DisclosureInfoBean )
                                            DBDisclInfo.elementAt( discIndex );
                      String description = request.getParameter
                                        (("description"+discIndex));
                      String finEntRevCode = request.getParameter
                                     (("sltReviewerCode"+discIndex));
                      String conflictStatusCode = request.getParameter
                                    (("sltConflictStat"+discIndex));
                      if(disclosureInfoBean.getConflictStatusCode() != null &&
                        !disclosureInfoBean.getConflictStatusCode().equals
                          (conflictStatusCode)){
                        entDetailsChanged = true;
                        disclosureInfoBean.setConflictStatusCode( conflictStatusCode );
                      }
                      if(description != null && description.equals("") &&
                          disclosureInfoBean.getDesc() == null){
                          //Don't set entDetailsChanged to true.
                      }
                      else if((description != null) && (!description.equals
                            (disclosureInfoBean.getDesc()))){
                        entDetailsChanged = true;
                        disclosureInfoBean.setDesc(description);
                      }
                      if(Integer.parseInt(userprivilege) == 2){
                        if(disclosureInfoBean.getReviewerCode() !=null &&
                        !disclosureInfoBean.getReviewerCode().equals
                            (finEntRevCode)){
                            entDetailsChanged = true;
                            disclosureInfoBean.setReviewerCode(finEntRevCode);
                        }
                      }
                      else{
                        if(disclosureInfoBean.getReviewerCode() != null &&
                        !disclosureInfoBean.getReviewerCode().equals
                            (defaultReviewerCode)){
                          entDetailsChanged = true;
                          disclosureInfoBean.setReviewerCode(defaultReviewerCode);
                        }
                      }
                        if( disclosureInfoBean != null && entDetailsChanged){
                            collDisclosureInfo.addElement( disclosureInfoBean );  
                        }                      
                    }//end for loop
                }//end if synchronize != null
            }//end else if request is for edit disclosure                
                    
                    /* CASE #1374 End */
                    /* CASE #1374 Comment Begin */
                     /*for( int disclStatusIndex = 0; disclStatusIndex<arrEntityNum.length ; 
                                                        disclStatusIndex++ ) {
                          Vector collDiscInfo = disclosureDetailsBean.getCOIDisclosureInfo
                            ( discosureNum );
                          DisclosureInfoBean disclosureInfoBean = null;
                          /* CASE #307 begin */
                          /* Check whether user-entered DisclosureInfoBean fields
                          are different from existing DisclosureInfoBean fields.
                          If any are different, add that DisclosureInfoBean to
                          Vector, so that a row will be entered in
                          inv_coi_disc_details.
                          boolean entDetailsChanged = false;
                          /* CASE #307 end 
                          for( int discIndex = 0 ; discIndex < collDiscInfo.size() ;
                            discIndex++ ) {
                              /* CASE #307 begin. 
                              entDetailsChanged = false;
                              /* CASE #307 end. 
                              disclosureInfoBean = ( DisclosureInfoBean )
                                collDiscInfo.elementAt( discIndex );
                              String description = request.getParameter
                                                (("description"+disclStatusIndex));
                              String finEntRevCode = request.getParameter
                                             (("sltReviewerCode"+disclStatusIndex));
                              String conflictStatusCode = request.getParameter
                                            (("sltConflictStat"+disclStatusIndex));
                              if( disclosureInfoBean.getEntityNumber().equals
                                ( arrEntityNum[ disclStatusIndex ] )
                                  && disclosureInfoBean.getEntSeqNumber().equals
                                  ( arrEntSeqNum[ disclStatusIndex ] )
                                          && disclosureInfoBean.getSeqNumber().equals
                                          ( arrSeqNum[ disclStatusIndex ] ) ) {
                                  //disclosureInfoBean.setConflictStatus( arrDisclStatus[ disclStatusIndex ] );
                                  /*CASE #307 begin. 
                                  if(!disclosureInfoBean.getConflictStatusCode().equals
                                          (conflictStatusCode)){
                                    entDetailsChanged = true;
                                  }
                                  //Check whether user has edited a fin entity, which will
                                  //have incremented the seq no of the fin ent.
                                  else if(!disclosureInfoBean.get
                                  /*CASE #307 end. 
                                  disclosureInfoBean.setConflictStatusCode( conflictStatusCode );
                                  //disclosureInfoBean.setDesc(arrDesc[disclStatusIndex]);
                                  /*CASE #307 begin. 
                                  if(description.equals("") &&
                                      disclosureInfoBean.getDesc() == null){
                                      //Do nothing.  Don't set entDetailsChanged to true.
                                  }
                                  else if((description != null) && (!description.equals
                                        (disclosureInfoBean.getDesc()))){
                                    entDetailsChanged = true;
                                  }
                                  /*CASE #307 end. 
                                  disclosureInfoBean.setDesc(description);
                                  /* CASE #212 If reviewer has maintain coi role, set reviewer code for
                                  financial entity to the user selection.  Otherwise, set
                                  it to the default reviewer code. */
                                  /* CASE #212 Comment begin */
                                  /*
                                  String strUserprivilege =
                                    session.getAttribute("userprivilege").toString();
                                  int userprivilege = Integer.parseInt(strUserprivilege);
                                  if(userprivilege == 2){*/
                                  /* CASE #212 Comment End */
                                  /* CASE #212 Begin 
                                  if(Integer.parseInt(userprivilege) == 2){
                                    /* CASE #307 begin. 
                                    if(!disclosureInfoBean.getReviewerCode().equals
                                        (finEntRevCode)){
                                      entDetailsChanged = true;
                                    }
                                    /* CASE #307 end. 
                                    disclosureInfoBean.setReviewerCode(finEntRevCode);
                                  }
                                  else{
                                    /* CASE #307 begin. 
                                    if(!disclosureInfoBean.getReviewerCode().equals
                                        (defaultReviewerCode)){
                                      entDetailsChanged = true;
                                    }
                                    /* CASE #307 end. 
                                    disclosureInfoBean.setReviewerCode(defaultReviewerCode);
                                  }
                                  /* CASE #212 End 
                                  break;
                              }
                          }//end for loop

                          /*CASE #307 Begin. */
                          /* If any changes have been made for a given financial
                          entity with regard to this coi discl, insert a row in
                          inv_coi_disc_details. 
                          if( disclosureInfoBean != null && entDetailsChanged){
                            collDisclosureInfo.addElement( disclosureInfoBean );  //orig code before CASE # 307 edits
                          }
                          /* Put discl info for all fin entities in collDisclosureInfo
                          Vector, which will be set in session to be accessed if
                          user edits temp proposal info and returns to the page.
                          allDisclosureInfo.addElement( disclosureInfoBean );
                          /* CASE # 307 End. 
                      }//end if synchronize != null
              }
            }//end if request is for edit disclosure
            /* CASE #1374 Comment End */
            /*
             * Get the information of questions, number of questions,
             * all question ids and their answers.
             * For each question, perform update by using addUpdateCert().
             */
            String[] arrQuestionIDs = request.getParameterValues( "hdnQuestionID" ); //all question ids
            String[] arrQuestionDescs = request.getParameterValues( "hdnQuestionDesc" ); //all question desc
            String[] arrLastUpdate = request.getParameterValues( "hdnLastUpdate" ); //last updated time
            String explanation = request.getParameter( "txtExplanation" ); //explanation(future use)
            String accType = null;
            Vector collCertDetails = null;
            if( arrQuestionIDs != null ) {
              collCertDetails = new Vector( 3 , 2 );
             /* CASE #307 Begin. */
              if(accountType.equals("I")){
             /* CASE #307 End. */
                for( int questionIDIndex = 0 ; questionIDIndex < arrQuestionIDs.length ; questionIDIndex++ ) {
                    String answer = request.getParameter
                      ( arrQuestionIDs[ questionIDIndex ] );
                    if( answer == null ) {
                        throw new CoeusException( "Please answer all the questions" );
                    }
                    Timestamp certLastUpdate = null;
                    if( arrLastUpdate != null ) {
                        if( arrLastUpdate[ questionIDIndex ].equalsIgnoreCase("null")) {
                          certLastUpdate = currentTimeStamp;
                          accType = kdefaultAccountType;
                        }else {
                          certLastUpdate = Timestamp.valueOf
                            ( arrLastUpdate[ questionIDIndex ] );
                          accType = accountType.trim();
                        }
                        //certLastUpdate = Timestamp.valueOf( arrLastUpdate[ questionIDIndex ] );
                    } else {
                        certLastUpdate = null;
                        accType = accountType.trim();
                    }
                    CertificateDetailsBean certificateDetailsBean =
                      new CertificateDetailsBean();
                    certificateDetailsBean.setPersonId( personId );
                    certificateDetailsBean.setNumber( discosureNum );
                    certificateDetailsBean.setCode( arrQuestionIDs[ questionIDIndex ] );
                    certificateDetailsBean.setQuestion( arrQuestionDescs[ questionIDIndex ] );
                    certificateDetailsBean.setAnswer( answer );
                    certificateDetailsBean.setExplanation( explanation );
                    certificateDetailsBean.setUpdateUser( updateUser );
                    certificateDetailsBean.setLastUpdate( certLastUpdate );
                    certificateDetailsBean.setAccountType(accType);
                    collCertDetails.addElement( certificateDetailsBean );
                }//for ends
              }//end if account type I
              /* CASE # 307 Begin. */
              /* For user editing disclosure, OSP$INV_COI_CERTIFICATION should be
              updated only when the answers to the questions have changed. */
              else if(accountType.equals("U")){
                  Vector collCOICertDetails =
                    disclosureDetailsBean.getCOICertificateDetails(discosureNum);
                  for(int questionIDIndex = 0; questionIDIndex <
                        collCOICertDetails.size(); questionIDIndex++){
                      CertificateDetailsBean certDetailsBean = (CertificateDetailsBean)
                            collCOICertDetails.get(questionIDIndex);
                      String answer = request.getParameter
                                ( arrQuestionIDs[ questionIDIndex ] );
                      if( answer == null ) {
                          throw new CoeusException( "Please answer all the questions" );
                      }
                      boolean certInfoChanged = false;
                      if(!answer.equals(certDetailsBean.getAnswer())){
                          certInfoChanged = true;
                          certDetailsBean.setAnswer(answer);
                      }
                      if((explanation != null) &&
                        !(explanation.equals(certDetailsBean.getExplanation()))){
                            certInfoChanged = true;
                            certDetailsBean.setExplanation(explanation); //future use
                      }
                      certDetailsBean.setAccountType(accountType);
                      certDetailsBean.setUpdateUser(updateUser);
                      if(certInfoChanged){
                        collCertDetails.add(certDetailsBean);
                      }
                      /* Add all cert info to Vector to be set in session
                      to be accessed if user edits temp proposal info
                      and returns to the page. */
                      allCertDetails.add(certDetailsBean);
                  }//end for loop
             }//end if
          }// end if arrQuestionIDs not null
            /* CASE # 307 End. */
            /* CASE #357 Begin */
            if(synchronize){
              /* Set any edits already made to disclosure header info and cert
                 info in request.  Set attribute in request so that
                 ViewCOIDisclosureDetailsAction will take this info from session.
                 Synchronize entities, then reload the page. */
                  if(disclosureDetailsBean.syncDisclosureWithFE(discosureNum, updateUser)){
                      request.setAttribute("synchronize", "synchronize");
                      request.setAttribute("collCOICertDetails", allCertDetails);
                      request.setAttribute("disclosureHeader", disclosureHeaderBean);
                      String url = "/viewCOIDisclosureDetails.do?action=edit&disclNo=";
                      url += discosureNum;
                      RequestDispatcher rd = request.getRequestDispatcher(url);
                      rd.forward(request, response);
                      return null;
                  }
                  else{
                      String errorMsg = "Synchronization of disclosure with ";
                      errorMsg += "current financial entities failed.";
                      throw new CoeusException(errorMsg);
                  }
            }// end if synchronize
            /* CASE #357 End */
            /* CASE #231 Begin */
            /* CASE #852 Comment Begin */
            /* Check if action is to edit Temp Proposal information.  Use a workaround
            to determine which submit button was pressed.  If submit button for edit Temp
            Proposal info was pressed, set information in session to be redisplayed
            when user returns to edit disclosure page.  Then forward to
            createTempProposal.do
            Enumeration attr2 = request.getParameterNames();
            String att2 = null;
            while(attr2.hasMoreElements()){
                att2 = (String)attr2.nextElement();
                if(att2.equals("Edit Temporary Proposal.x")){
                    /* Set form bean in session with any edits already made to
                    disclosure.
                    session.setAttribute("addCOIDisclosureActionForm",
                                addCOIDisclosureActionForm);
                    session.setAttribute("tempProposalAction", "edit");
                    session.setAttribute("collCOICertDetails", allCertDetails);
                    session.setAttribute("disclosureHeader", disclosureHeaderBean);
                    session.setAttribute("collDisclosureInfo", allDisclosureInfo);
                    String proposalNo = (String)request.getParameter("keyNumber");
                    String url = "createTempProposal.do?proposalNo="+proposalNo;
                    RequestDispatcher rd = request.getRequestDispatcher(url);
                    rd.forward(request, response);
                    /*Need to return actionforward, so that the rest of the class
                    is not executed.  Control will not be forwarded to this actionforward,
                    however.
                    return actionforward;
                }
            }
            /* End CASE #231 */
            /* CASE #852 Comment End */
            /* CASE #1374 Comment Begin */
            //temp proposal info included on coi form.
           /* if(tempProposalAction != null &&
                          tempProposalAction.equalsIgnoreCase("edit")){
                /* Set form bean in session with any edits already made to
                disclosure. 
                session.setAttribute("addCOIDisclosureActionForm",
                            addCOIDisclosureActionForm);
                session.setAttribute("collCOICertDetails", allCertDetails);
                session.setAttribute("disclosureHeader", disclosureHeaderBean);
                session.setAttribute("collDisclosureInfo", allDisclosureInfo);
                String proposalNo = (String)request.getParameter("keyNumber");
                String url = "createTempProposal.do?proposalNo="+proposalNo;
                RequestDispatcher rd = request.getRequestDispatcher(url);
                rd.forward(request, response);
                return null;
            }*/
            /* CASE #852 End */
            /* CASE #1374 Begin */
            if(disclosureTypeCode == null){
                throw new CoeusException("exceptionCode.30003");                
            }
            if(disclosureTypeCode.equals(DEV_PROPOSAL_MODULE)){
                //String title = addCOIDisclosureActionForm.getTitle();
                System.out.println("*****dev proposal, title: "+addCOIDisclosureActionForm.getTitle());
                //String sponsorName = addCOIDisclosureActionForm.getSponsorName();
                String sponsorId = addCOIDisclosureActionForm.getSponsorId();
                if(sponsorId == null){
                    sponsorId = DEFAULT_SPONSOR_ID;
                }
                tempProposalTrans = new TempProposalTransBean();
                if( accountType.trim().equals( "I" ) ){
                    tempProposalDetails = new TempProposalDetailsBean();
                }
                else{
                    tempProposalDetails = 
                        tempProposalTrans.getTempProposalInfo
                            (disclosureHeaderBean.getKeyNumber());
                }
                tempProposalDetails.setSponsorName
                        ( addCOIDisclosureActionForm.getSponsorName() );
                tempProposalDetails.setSponsorCode( sponsorId );
                tempProposalDetails.setTitle
                        ( addCOIDisclosureActionForm.getTitle() );
                tempProposalDetails.setProposalTypeCode( 
                        addCOIDisclosureActionForm.getTypeCode().trim() );
                System.out.println("addCOIDisclosureActionForm.getTypeCode(): "+addCOIDisclosureActionForm.getTypeCode());
                tempProposalDetails.setUpdateUser( userName );
                    System.out.println("userName: "+userName);                 
                if( accountType.trim().equals("I") ) {
                    System.out.println("accountType equals I");
                    tempProposalDetails.setProposalNumber( appliesToCode );  
                    //Set lead unit for all temp proposals as default lead unit.
                    String defaultLeadUnit = tempProposalTrans.getDefaultLeadUnit();
                    if(defaultLeadUnit == null){
                        throw new CoeusException ("exceptionCode.60024");
                    }                    
                    tempProposalDetails.setLeadUnit( defaultLeadUnit );
                    tempProposalDetails.setPiId
                              (addCOIDisclosureActionForm.getPersonId());
                    tempProposalDetails.setPiName
                              (addCOIDisclosureActionForm.getPersonFullName()); 

                    System.out.println("tempProposalDetails.getProposalTypeCode: "+tempProposalDetails.getProposalTypeCode());
                    tempProposalDetails.setLogStatus( DEFAULT_LOG_STATUS );
                    tempProposalTrans.add(tempProposalDetails); 
                }
                else {
                    System.out.println("call updTempProposalInfo");
                    tempProposalTrans.updTempProposalInfo(tempProposalDetails); 
                }
            } 
        
            /* CASE #852 Comment Begin */
                //String tempProposalAction = (String)session.getAttribute("tempProposalAction");
                /* CASE #852 Comment End */
                /* CASE #1374 Comment Begin */
               /* if(tempProposalAction != null
                    && tempProposalAction.equalsIgnoreCase("insert")){
                    TempProposalTransBean tempProposalTrans = new TempProposalTransBean();
                    if(tempProposalDetails != null){
                        tempProposalTrans.add(tempProposalDetails);
                    }*/
                    /* CASE #852.  If tempProposalAction is not null, but
                    tempProposalDetails is null, then tempProposalAction has
                    been left in session. Don't throw exception.  Remove it.*/
                    /* CASE #852 Comment Begin */
                    //else{
                        //throw new CoeusException("exceptionCode.60005");
                   // }
                    /* CASE #852 Comment End */
                //}
               // session.removeAttribute("tempProposalAction");
               // session.removeAttribute("tempProposalDetails");               
            //}
            /* CASE #1374 Comment End */ 
            /* CASE #665 End */
            /* CASE #1374 Begin */
            //Remove from session, if present: certQuestionErrors, user preselected 
            //answers and action form.
            session.removeAttribute("certQuestionErrors");
            session.removeAttribute("selectedQuestions");
            session.removeAttribute("selectedAnswers");
            session.removeAttribute("selectedConflictStatus");
            session.removeAttribute("selectedDescription");
            session.removeAttribute("typeCode");
            session.removeAttribute("fromEditFinEnt");
            /* CASE #1374 Begin */
            if( disclosureDetailsBean.addUpdateCOIDisc( disclosureHeaderBean ,  
                disclStatusChanged,  collDisclosureInfo , collCertDetails , 
                accountType.trim().toUpperCase() ) ) {
                UtilFactory.log( "COI Disclosure has been successfully added/updated" ,
                        null , "" , "" );
            }//if ends  
            /* CASE #1374 End */
            /* CASE #1374 Comment Begin */
            /*if( disclosureDetailsBean.addUpdateCOIDisc( disclosureHeaderBean ,                 
              collDisclosureInfo , collCertDetails , accountType.trim().toUpperCase() ) ) {
                UtilFactory.log( "COI Disclosure has been successfully added/updated" ,
                        null , "" , "" );
            }//if ends            
            /* CASE #665 Begin */
            //Put disclosure header bean and actionform bean in request to be
            //accessed by Disclosure Submit confirmation page.
            //request.setAttribute("addCOIDisclosureActionForm", addCOIDisclosureActionForm);
            //request.setAttribute("disclosureHeader", disclosureHeaderBean);
            /* CASE #665 End *//* CASE #1374 Comment End */

        } catch( CoeusException cEx ) {
            errorFlag = true;
            UtilFactory.log( cEx.getMessage() , cEx , "AddNewCOIDisclosureAction" ,
            "perform()" );
            request.setAttribute( "EXCEPTION" , cEx );
        } catch( DBException dbEx ) {
            errorFlag = true;
            /* CASE #735 Comment Begin */
            //DBEngine prints the exception to log file.
            //UtilFactory.log( dbEx.getMessage() , dbEx , "AddNewCOIDisclosureAction" ,
            //"perform()" );
            /* CASE #735 Comment End */
            request.setAttribute( "EXCEPTION" , dbEx );
        }catch( Exception ex ) {
            ex.printStackTrace();
            errorFlag = true;
            UtilFactory.log( ex.getMessage() , ex , "AddNewCOIDisclosureAction" , "perform()" );
            request.setAttribute( "EXCEPTION" , new CoeusException( "exceptionCode.30003" ) );
        }

        /* if any exceptions were thrown then forward to FAILURE page other wise
         * forward to success
         */
        if( errorFlag ) {
            actionforward = actionMapping.findForward( FAILURE );
        } else {
            actionforward = actionMapping.findForward( SUCCESS );
        }
        return actionforward;
    }//end of perform

}//end of AddNewCOIDisclosureAction
