/*
 * @(#)AddCOIDisclosureActionForm.java 1.0 06/03/2002 16:30:23
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.action.coi;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import javax.servlet.http.HttpServletRequest;
import java.util.Vector;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Enumeration;
import edu.mit.coeus.coi.bean.DisclosureDetailsBean;
import edu.mit.coeus.coi.bean.CertQuestionDetailsBean;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.coi.bean.CertificateDetailsBean;
import javax.servlet.http.HttpSession;
import edu.mit.coeus.coi.bean.DisclosureInfoBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.coi.bean.DisclosureHeaderBean;
import edu.mit.coeus.coi.bean.FinancialEntityDetailsBean;
import edu.mit.coeus.coi.bean.EntityDetailsBean;
import edu.mit.coeus.action.common.CoeusActionBase;

/**
 * <code>AddCOIDisclosureActionForm</code>  is struts implemented Form class
 * holds a new COIDisclosure information.
 *
 * @author	RaYaKu
 * @version	1.0   June 10,2002 16:30:23
 */

public class AddCOIDisclosureActionForm extends ActionForm{
    
    /* CASE #1374 Begin */
    //Should the page display the Edit Temp Proposal button?
    private boolean showTempProposalEdit;
      
    /**
     * Value of Proposal Type Code if disclosure is for a temporary proposal.
     */
    private String typeCode;
    
    /**
     * Value of proposal type description from OSP$PROPOSAL_TYPE if this is for temp 
     * proposal.
     */
    private String proposalType;

    /**
     * value of Temporary Proposal Title
     */
    private String title;

    /**
     * Value of Sponsor Name.
     */
    private String sponsorName;
    
    /**
     * Value of sponsor id
     */
    private String sponsorId;
    /* CASE #1374 End */

    /**
     * Value of Account type (Insertion/Add,Edit/Modify etc..)
     */
    private String accountType;

    /**
     * Value of Applies To Code  for which user would like to add new disclosure.
     */
    private String appliesToCode;

    /**
     * Value of  Disclosure Number
     */
    private String disclosureNo;

    /* CASE #665 Begin */
    /**
     * Value of disclosure reviewer code.
     */
    private String disclReviewerCode;
    /* CASE #665 End */

    /**
     * Disclosure Type Code (1, 2 or 3 for award, inst proposal or dev proposal)
     */
    private String disclosureTypeCode;

    /**
     * Value of Disclosure Type (I or A for intial or annual)
     */
    private String disclosureType;

    /**
     * Id of the Person
     */
    private String personId;

    /**
     * Name of the Person
     */
    private String personFullName;

    /**
     * Reviewer Code for COI Disclosure or Financial Entity.
     */
    private String reviewer;


    /**
     * Value of User Name
     */
    private String userName;


    /**
     * Disclosure Status Code.
     */
    private String disclStatCode;

    /**
     * Value of Sequence Number
     */
    private String seqNum;

    /**
     * value of Status Sequence Number
     */
    private String statusSeqNum;

    /**
     * Value of Status
     */
    private String status;
    
    /* CASE #1374 Begin */
    /**
     * Get showTempProposalEdit
     * @return showTempProposalEdit
     */
    public boolean getShowTempProposalEdit(){
        return showTempProposalEdit;
    }
    
    /**
     * set showTempProposalEdit
     * @param showTempProposalEdit
     */
    public void setShowTempProposalEdit(boolean showTempProposalEdit){
        this.showTempProposalEdit = showTempProposalEdit;
    }
    
    /**
     * Get title of temp proposal.
     * @return title
     */
    public String getTitle(){
        //System.out.println("getTitle: "+title);
        return title;
    }
    
    /**
     * Set title of temp proposal.
     * @param title.
     */
    public void setTitle(String title){
        //System.out.println("setTitle("+title+")");
        this.title = title;
    }
    
    /**
     * Get Proposal Type Code if disclosure is for a temporary proposal.
     * @return ptypeCode
     */
    public String getTypeCode(){
        //System.out.println("getTypeCode: "+typeCode);
        return typeCode;
    }
    
    /**
     * Set Proposal Type Code if disclosure is for a temporary proposal.
     * @param typeCode
     */
    public void setTypeCode(String typeCode){
        //System.out.println("setTypeCode: "+typeCode);
        this.typeCode = typeCode;
    }
    
    /**
     * Get proposal type description if this is temporary proposal.
     */
    public String getProposalType(){
        return proposalType;
    }
    
    /**
     * Set proposal type description if this is temporary proposal.
     */
    public void setProposalType(String proposalType){
        this.proposalType = proposalType;
    }
    
    public String getSponsorId(){
        return sponsorId;
    }
    
    public void setSponsorId(String sponsorId){
        this.sponsorId = sponsorId;
    }
    
    /**
     * Get sponsorName if disclosure is for a temporary proposal.
     * @return sponsorName.
     */
    public String getSponsorName(){
        return sponsorName;
    }
    
    /**
     * Set sponsorName if disclosure is for a temporary proposal.
     * @param sponsorName.
     */
    public void setSponsorName(String sponsorName){
        this.sponsorName = sponsorName;
    }
    /* CASE #1374 End */

    /**
     * Get theDisclosure Status
     *
     * @return Disclosure Status
     */
    public String getStatus(){
        return this.status;
    }

    /**
     * Set the Disclosure Status
     *
     * @param newStatus  Status of Disclosure.
     */
    public void setStatus( String newStatus ){
        this.status = newStatus;
    }

    /**
     * Get the Disclosure Number
     *
     * @return Disclosure Number
     */
    public String getDisclosureNo(){
        return this.disclosureNo;
    }

    /**
     * Set the Disclosure Number
     *
     * @return newDisclosureNo  Disclosure Number
     */
    public void setDisclosureNo( String newDisclosureNo ){
        this.disclosureNo = newDisclosureNo;
    }

    /**
     * Get the Person FullName
     *
     * @return Fullname of a person
     */
    public String getPersonId(){
        return this.personId;
    }

    /**
     * Set the Person FullName
     *
     * @param newPersonFullName Fullname of a person
     */
    public void setPersonId( String newPersonId ){
        this.personId = newPersonId;
    }

    /**
     * Get the Person FullName
     *
     * @return Fullname of a person
     */
    public String getPersonFullName(){
        return this.personFullName;
    }

    /**
     * Set the Person FullName
     *
     * @param newPersonFullName Fullname of a person
     */
    public void setPersonFullName( String newPersonFullName ){
        this.personFullName = newPersonFullName;
    }

    /**
     * Get the Disclosure Type Code
     *
     * @return Type Code of Disclosure
     */
    public String getDisclosureTypeCode(){
        return this.disclosureTypeCode;
    }

    /**
     * Set the Disclosure Type Code
     *
     * @param newDisclosureTypeCode Type code of Disclosure that user
     *  wish to create Ex. 1- Award, 2- Proposal and  3- Temporay Proposal.
     */
    public void setDisclosureTypeCode( String newDisclosureTypeCode ){
        this.disclosureTypeCode = newDisclosureTypeCode;
    }

    /**
     * Get the value of Applies to code to which user would like to add
     * new disclosure.
     *
     * @return The code of Disclosure that is going to be created
     */
    public String getAppliesToCode(){
        return this.appliesToCode;
    }

    /**
     * Set the value of Applies to code with  which user would like to add new
     * disclosure.
     *
     * @param newAppliesToCode The type code of Disclosure that user wish to
     * create.
     */
    public void setAppliesToCode( String newAppliesToCode ){
        this.appliesToCode = newAppliesToCode;
    }

    /* CASE #665 Begin */
    /**
     * Get disclReviewerCode
     * @return disclReviewerCode
     */
    public String getDisclReviewerCode(){
        return disclReviewerCode;
    }

    /**
     * Set disclReviewerCode
     * @param disclReviewerCode
     */
    public void setDisclReviewerCode( String disclReviewerCode ){
        this.disclReviewerCode = disclReviewerCode;
    }
    /* CASE #665 End */

    /**
     * Get disclStatCode.
     * @return disclStatCode
     */
    public String getDisclStatCode()
    {
      return disclStatCode;
    }

    /**
     * Set disclStatCode.
     * @param disclStatCode
     */
    public void setDisclStatCode(String disclStatCode){
      this.disclStatCode = disclStatCode;
    }

    /**
     * Get the value Disclosure Type
     *
     * @return The Disclosure type(Annual or Initial).
     */
    public String getDisclosureType(){
        return this.disclosureType;
    }

    /**
     * Set the value fo Disclosure Type
     *
     * @param newDisclosureType The disclosure type that user wish to create
     *  A - Annual or I - Initial.
     */
    public void setDisclosureType( String newDisclosureType ){
        this.disclosureType = newDisclosureType;
    }

    /**
     * Get the Name of User
     *
     * @return name of the user
     */
    public String getUserName(){
        return this.userName;
    }

    /**
     * Set the User name
     *
     * @param newUserName Name of the User
     */
    public void setUserName( String newUserName ){
        this.userName = newUserName;
    }

    /**
     * Get the Status Sequence Number
     *
     * @return Status Sequence Number of Disclosure.
     */
    public String getStatusSeqNum(){
        return this.statusSeqNum;
    }

    /**
     * Set the Status Sequence Number
     *
     * @param newStatusSeqNum Status Sequence number of disclosure.
     */
    public void setStatusSeqNum( String newStatusSeqNum ){
        this.statusSeqNum = newStatusSeqNum;
    }

    /**
     * Get the Account type that user would like to perform on this disclosure
     *
     * @return Account type( Insert/Add or Modify/Edit).
     */
    public String getAccountType(){
        return this.accountType;
    }

    /**
     * Set the Account type ( kind of action that is going to be performed on this disclosure).
     *
     * @param newAccountType The Action (Insert/Add or Edit/Modify) on disclosure.
     */
    public void setAccountType( String newAccountType ){
        this.accountType = newAccountType;
    }

        /**
     * Get reviewer.
     * @return reviewer
     */
    public String getReviewer()
    {
      return reviewer;
    }

    /**
     * Set reviewer.
     * @param reviewer
     */
    public void setReviewer(String reviewer)
    {
      this.reviewer = reviewer;
    }

    /**
     * Get the Sequence Number of Disclosure
     *
     * @return The Sequence Number of Disclosure
     */
    public String getSeqNum(){
        return this.seqNum;
    }

    /**
     * Set the Sequence Number of Disclosure.
     *
     * @param newSeqNum - The Sequence Number of Disclosure.
     */
    public void setSeqNum( String newSeqNum ){
        this.seqNum = newSeqNum;
    }

    /**
     * Reset all bean properties to their default state.
     * This method is called before the properties are repopulated by the controller servlet.
     * The default implementation does nothing.
     *
     * @param actionMapping The actionMapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset( ActionMapping actionMapping , HttpServletRequest request ){
    }

    /* CASE #864 Begin */
    /**
     * Validate the properties that have been set for this request,
     * and return an ActionErrors object that encapsulates any validation errors
     * that have been found.
     * If no errors are found, return null or an ActionErrors object with
     * no recorded error messages
     * <br> <b>Validations</b> </br>
     * <li> All Questions should be answered and if any question is left
     * unanswered a message will be supplied to
     * to the user in a page from which the request is generated.</li>
     * <li> If Disclosure status is 200 or 201 (No conflict or Resolved), then
     * Conflict status for all disclosures musts be 200 or 201 or 202 (No Conflict
     * or Resolved or Managed).
     *
     * @param actionMapping The mapping used to select this instance
     * @param request  The servlet request we are processing
     */
    public ActionErrors validate( ActionMapping actionMapping , HttpServletRequest request ){
      System.out.println("Begin AddCOIDisclosureActionForm.validate()");

      ActionErrors actionErrors = new ActionErrors();
      HttpSession session = request.getSession( true );
//      UtilFactory UtilFactory = new UtilFactory();
      try{
            String userName = (String)session.getAttribute(CoeusActionBase.USERNAME);
            /*
             * If userName information is not available in session scope, return. 
             * action class will forward to expiration page.
             */
            if( userName == null ) {
                return actionErrors;
            }          
          
        /* If user has selected "Sync" submit button, don't perform the 
         * validation check.  Disclosure edits
         * will not be processed until the user selects submit button.
         * User a workaround to determine which button the user has pressed. 
         */
          Enumeration attr = request.getParameterNames();
          String att = null;
          while(attr.hasMoreElements()){
              att = (String)attr.nextElement();
              /* CASE #1374 Comment Begin */
              /*if(att.equals("Edit Temporary Proposal.x")){
                  session.setAttribute("tempProposalAction", "edit");
                  return actionErrors;
              }*/
              if(att.equals("Synchronize Financial Entities.x")){
                  request.setAttribute("synchronize", "true");
                  return actionErrors;
              }
          }
          
          
          /* CASE #1374 Begin */
          if(personId == null){
              return actionErrors;
          }
          session.removeAttribute("backToDiscl");
          
            DisclosureDetailsBean disclosureDetails =
                  new DisclosureDetailsBean(personId);          
          //If actionMapping is addCOIDisclosure, then process the validate method
          //only if user has come from editing financial entity.
          System.out.println("*****actionMapping: "+actionMapping.getPath());
          String actionFrom = (String)request.getAttribute("actionFrom");
          if(actionFrom == null){
              actionFrom = request.getParameter("actionFrom");
          }
          System.out.println("actionFrom: "+request.getAttribute("actionFrom"));
          System.out.println("title: "+title);
          System.out.println("sponsorName: "+sponsorName);
          if( actionMapping.getPath().equals("/addCOIDisclosure")  ||
            actionMapping.getPath().equals("/viewCOIDisclosureDetails") ){
              if(actionFrom == null || !actionFrom.equals("editFinEnt") ){
                    System.out.println("**skip validation**");
                    return actionErrors;
              }
          }
          System.out.println("**continue with validation**");
          //System.out.println("****typeCode: "+typeCode);
          //System.out.println("request.getParameter: "+request.getParameter("typeCode"));
          /* CASE #1374 End */
          String userprivilege = (String)session.getAttribute("userprivilege");
          /* CASE #1374 Comment Begin */
          /*String personId = null;
          DisclosureDetailsBean disclosureDetailsBean = null;
          PersonInfoBean personInfoBean = (PersonInfoBean)session.getAttribute("personInfo");
          if( personInfoBean != null ) {
                personId = personInfoBean.getPersonID();
                disclosureDetailsBean= new DisclosureDetailsBean(personId);
                //page will get collections from session
                LinkedList collCOIStatus = disclosureDetailsBean.getAllCOIStatus();
                request.setAttribute( "collCOIStatus" , collCOIStatus );
                LinkedList collCOIReviewer = disclosureDetailsBean.getAllReviewers();
                request.setAttribute("collCOIReviewer", collCOIReviewer);
                LinkedList collCOIDisclStatus = disclosureDetailsBean.getAllDiscStatus();
                request.setAttribute("collCOIDisclStatus", collCOIDisclStatus);
          }
          //If personInfoBean from session is null, then session has expired.  Return
          //control to action classs which will forward to session expired page.
          else if(personInfoBean == null){
              return actionErrors;
          }*/
          /* CASE #1374 Comment End */
          /* CASE #1374 Begin */
          if(disclosureTypeCode != null && disclosureTypeCode.equals("3") ){
            if( ( title == null ) || ( title.equals( "" ) ) ) {
                actionErrors.add( "title" ,
                new ActionError( "error.tempProposalForm.title.required" ) );
            }
            if ( (title != null) && (150 < title.length()) ) {
                actionErrors.add( "title" ,
                new ActionError( "error.tempProposalForm.title.toolong" ) );
            }
            if ( typeCode == null ){
                actionErrors.add( "proposalType" ,
                    new ActionError ( "error.tempProposalForm.proposalType.required" ) );
            }
            if ( sponsorName == null || sponsorName.trim().equals( "" ) ){
                actionErrors.add("sponsor", 
                    new ActionError( "error.tempPrposal.sponsor.required" ) );
            }
            else if( sponsorName.trim().equalsIgnoreCase( "NIH" ) || 
                sponsorName.trim().equalsIgnoreCase( "National Institute of Health" ) ){
                    sponsorName = "NIH";
                    sponsorId = "000340";
            }
            else if( sponsorName.trim().equalsIgnoreCase( "NSF" ) ||
                sponsorName.trim().equalsIgnoreCase( "National Science Foundation" ) ){
                    sponsorName = "NSF";
                    sponsorId = "000500";
            }               
          }
          String[] arrQuestionIDs = null;
          String[] arrQuestionDescs = null;
          String[] arrAnswers = null;
          String[] arrConflictStatus = null;
          String[] arrDescription = null;
          boolean isAnswerAvailable = true;
          if(actionMapping.getPath().equals("/addCOIDisclosure")  ||
            actionMapping.getPath().equals("/viewCOIDisclosureDetails")  ){
                arrQuestionIDs = (String[])session.getAttribute("selectedQuestions");
                arrAnswers = (String[])session.getAttribute("selectedAnswers");
                arrConflictStatus = (String[])session.getAttribute("selectedConflictStatus");
                arrDescription = (String[])session.getAttribute("selectedDescription");
                typeCode = (String)session.getAttribute("typeCode");                 
          }
          else {
                arrQuestionIDs = request.getParameterValues( "hdnQuestionID" ); //all question ids
                arrQuestionDescs = request.getParameterValues( "hdnQuestionDesc" ); //all question desc
                String[] arrLastUpdate = request.getParameterValues( "hdnLastUpdate" );
              if( arrQuestionIDs != null ) {
                  arrAnswers = new String[arrQuestionIDs.length];
                  for( int questionIDIndex = 0 ; questionIDIndex < arrQuestionIDs.length ;
                                                                  questionIDIndex++ ) {
                      String answer = request.getParameter( arrQuestionIDs[ questionIDIndex ] );//request.getParameter("C3");
                      if( answer == null ) {
                          // set the flag to false
                          isAnswerAvailable=false;
                      }
                      //collect each answer
                      arrAnswers[questionIDIndex] = answer;
                  }
              }
          }
          /* CASE #1374 End */
          Vector certDetails = (Vector)session.getAttribute("collCOICertDetails");
          /* CASE #1374 Comment Begin */
          /*String[] arrQuestionIDs = request.getParameterValues( "hdnQuestionID" ); //all question ids
          String[] arrQuestionDescs = request.getParameterValues( "hdnQuestionDesc" ); //all question desc
          String[] arrLastUpdate = request.getParameterValues( "hdnLastUpdate" ); //last updated time
          String[] arrAnswers = null;
          String[] arrConflictStatus = null;
          String[] arrDescription = null;
          boolean isAnswerAvailable=true;
          if( arrQuestionIDs != null ) {
              arrAnswers = new String[arrQuestionIDs.length];
              for( int questionIDIndex = 0 ; questionIDIndex < arrQuestionIDs.length ;
                                                              questionIDIndex++ ) {
                  String answer = request.getParameter( arrQuestionIDs[ questionIDIndex ] );
                  if( answer == null ) {
                      // set the flag to false
                      isAnswerAvailable=false;
                  }
                  //collect each answer
                  arrAnswers[questionIDIndex] = answer;
              }
          }*/
          if(!isAnswerAvailable){
              actionErrors.add( "answerRequired" ,
                  new ActionError( "error.addCOIDisclsoureForm.answers.requried" ) );
          }
          /* CASE #864 Comment Begin
          /* Check size of collection of financial entities stored in session for this
          disclosure.  If size is 0, then check that user has not answered any of
          the certification questions in a way that requires an explanation.  Then,
          skip the rest of the validation. */
          /*if(disclosureInfo != null && disclosureInfo.size() == 0){
                  if(certDetails == null){
                      certDetails = disclosureDetailsBean.getCOICertificateDetails
                                                                    (disclosureNo);
                  }
              for( int questionIDIndex = 0 ; questionIDIndex < arrQuestionIDs.length ;
                                                              questionIDIndex++ ) {
                  String answer = request.getParameter( arrQuestionIDs[ questionIDIndex ] );
                  CertificateDetailsBean certificateDetailsBean =
                              (CertificateDetailsBean)certDetails.get(questionIDIndex);
                  if(certificateDetailsBean.getExplReqFor() != null){
                      String explReqFor = certificateDetailsBean.getExplReqFor();
                      //System.out.println("explReqFor: "+explReqFor);
                      //System.out.println("answer: "+answer);
                      if(answer != null && explReqFor.indexOf(answer) != -1){
                         actionErrors.add("No Fin Ent",
                            new ActionError("error.addCOIDisclosure.noFinEntAnsReqExpl"));
                      }//end if
                  }
              }//end for
          }// end if*/
          /* CASE #864 Comment End */
          else if(isAnswerAvailable){
              String personIDForCertValidation = personId;
              /* If user has done a select person, so personinfo is not his/her
              own, but user does not have maintain COI rights, then user is adding
              a disclosure for themself.  Use loggedinpersonid instead of personid.*/
              String loggedinpersonid =
                  (String)session.getAttribute(CoeusActionBase.LOGGEDINPERSONID);
              if((!loggedinpersonid.equals(personId)) &&
                    (Integer.parseInt(userprivilege) != 2)){
                    personIDForCertValidation = loggedinpersonid;
              }
              /* CASE #1393 Begin */
              CertQuestionDetailsBean certQuestionDetails = 
                                        new CertQuestionDetailsBean(personId);
              /* CASE #1393 End */
              
              /* CASE #1393 Begin */
              //Modify so that info regarding ID of question that failed is
              //available to the JSP.
              HashMap certQuestionErrors = new HashMap();
              if(arrQuestionIDs != null){
                  for(int questCnt=0; questCnt<arrQuestionIDs.length; questCnt++){
                      boolean foundNoFinEnt = false;
                      boolean foundEntNoAnsYes = false;
                      boolean foundEntYesAnsNo = false;  
                      String questionID = arrQuestionIDs[questCnt];                  
                      System.out.println("****call fn_check_discl_answer for question id: "+questionID);
                      System.out.println("with answer: "+arrAnswers[questCnt]);
                      System.out.println("and personId: "+personId);
                      String entityNumber = disclosureDetails.isDisclAnswerOk(
                          questionID, arrAnswers[questCnt]);
                      System.out.println("entityNumber returned by check_discl_answer: "+entityNumber);
                      if(!entityNumber.equals("1")){
                          if(entityNumber.equals("-1") && !foundNoFinEnt){
                                  actionErrors.add ("answersIncorrect",
                                      new ActionError( "error.addCOIDisclosure.noFinEntAnsReqExpl"));
                                  /* CASE #1393 Begin */
                                  certQuestionErrors.put(questionID, entityNumber);
                                  /* CASE #1393 End */
                                  foundNoFinEnt = true;
                          }
                          else if(entityNumber.equals("-2") && !foundEntNoAnsYes){
                                  //System.out.println("adding entNoAnsYes");
                                  actionErrors.add ("answersIncorrect",
                                      new ActionError( "error.addCOIDisclosure.entNoAnsYes"));
                                  /* CASE #1393 Begin */
                                  certQuestionErrors.put(questionID, entityNumber);
                                  /* CASE #1393 End */
                                  foundEntNoAnsYes = true;
                          }
                          else if(!foundEntYesAnsNo){
                              /* CASE #1393 Comemnt Begin */ 
                              /*FinancialEntityDetailsBean financialEntityDetails =
                                  new FinancialEntityDetailsBean(personId);
                              EntityDetailsBean entityWithYes =
                                  financialEntityDetails.getFinancialEntityDetails(entityNumber);
                              String entityName = entityWithYes.getName();
                              String seqNo = entityWithYes.getSeqNumber();
                              actionErrors.add ("answersIncorrect",
                                  new ActionError( "error.addCOIDisclosure.entYesAnsNo"));
                              request.setAttribute("errorEntityNumber", entityNumber);
                              request.setAttribute("errorEntityName", entityName);
                              request.setAttribute("errorEntitySeqNo", seqNo);
                              /* CASE #1393 Comment End */
                              foundEntYesAnsNo = true;
                              /* CASE #1393 Begin */
                              //System.out.println("adding actionError entYesAnsNo");
                              actionErrors.add("answersIncorrect",  
                                    new ActionError( "error.addCOIDisclosure.entYesAnsNo"));
                              HashMap entitiesWithYes = 
                                certQuestionDetails.getEntitiesCertQuestionYes(questionID);
                              certQuestionErrors.put(questionID, entitiesWithYes);
                              /* CASE #1393 End */
                          }
                          request.setAttribute("certQuestionErrors", certQuestionErrors);
                      }
                  }
               }
          }//end if arrQuestionIDs != null
          //Process entity info 
          Vector disclosureInfo = (Vector)session.getAttribute
                                              ("collCOIDisclosureInfo");
            if(session.getAttribute("addFinEnt") != null ){
                if(actionMapping.getPath().equals("/viewCOIDisclosureDetails")  ) {
                   disclosureInfo =  disclosureDetails.getCOIDisclosureInfo(
                                                        disclosureNo.trim() );
                }
                else if(actionMapping.getPath().equals("/addCOIDisclosure") ){
                    disclosureInfo = disclosureDetails.getCOIDisclInfoForPerson();
                }
                session.setAttribute("collCOIDisclosureInfo", disclosureInfo);
            }          
          if(disclosureInfo != null && disclosureInfo.size() != 0){
            /* to get all the selcted conflictStatus and Description  information to retain the values
             * when form is invalidated
            */
            boolean isConflictStatusOk = true;
            boolean isConflictStatusOkHasMaintain = true;
            int arrLength = disclosureInfo.size();
            /* CASE #1374 Begin */
            if (actionMapping.getPath().equals("/addNewCOIDisclosure")  ||
                actionMapping.getPath().equals("/editCOIDisclosure") ){
            /* CASE #1374 End */
                arrConflictStatus = new String[arrLength];
                arrDescription = new String[arrLength];
                  /* Check for valid status for fin entities disclosed for this award/proposal.
                  For all new disclosures, valid status is either 200 - "No Conflict" or
                  301 - "PI Identified Conflict".  For edit disclosure, if user does not
                  have Maintain COI, then valid status is either 200 or 301.  Else if user
                  has Maintain COI, then any staus is valid EXCEPT 100 - "Not Previously Reported"
                   or 101 - Financial Entity Changed.*/
                   for(int arrIndex=0;arrIndex<arrLength;arrIndex++){
                      arrConflictStatus[arrIndex]=request.getParameter(("sltConflictStat"+arrIndex));
                      arrDescription[arrIndex] = request.getParameter(("description"+arrIndex));
                   }
           /* CASE #1374 Begin */
            }
          if(arrConflictStatus == null){
              System.out.println("arrConflictStatus is null");
              return actionErrors;
          }            
             if(actionMapping.getPath().equals("/editCOIDisclosure")  ||
                actionMapping.getPath().equals("/viewCOIDisclosureDetails")  ) {
                for(int i=0; i<disclosureInfo.size(); i++) {
                  DisclosureInfoBean disclInfo = 
                        (DisclosureInfoBean) disclosureInfo.elementAt(i);
                  disclInfo.setConflictStatusCode(arrConflictStatus[i]);
                  disclInfo.setDesc(arrDescription[i]);
                }                            
             }
            //if user has edited a fin ent, then get updated description
            //info from database. overwrite any description added on discl form
            if(session.getAttribute("updatePersonReDesc") != null &&
                arrDescription != null && 
                request.getAttribute("entityNumber") != null){
                    System.out.println("updatePersonReDesc");
                String changedEntity = (String)request.getAttribute("entityNumber");
                FinancialEntityDetailsBean financialEntityDetails =
                    new FinancialEntityDetailsBean(personId);
                EntityDetailsBean entityDetails =
                    financialEntityDetails.getFinancialEntityDetails(changedEntity);
                String updatedDesc = entityDetails.getPersonReDesc();
                for(int entCnt=0; entCnt<disclosureInfo.size(); entCnt++){
                    DisclosureInfoBean disclInfo = 
                        (DisclosureInfoBean)disclosureInfo.elementAt(entCnt);
                    if(disclInfo.getEntityNumber().equals(changedEntity)){
                         if(actionMapping.getPath().equals("/addCOIDisclosure")  ){
                            arrDescription[entCnt] = updatedDesc;
                            session.setAttribute("selectedDescription", arrDescription);
                         }
                         else{//viewCOIDisclosureDetails
                            disclInfo.setDesc(updatedDesc);
                            session.setAttribute( "collCOIDisclosureInfo" , disclosureInfo);
                         }
                    }
                }
                session.removeAttribute("updatePersonReDesc");
            }
            for(int arrIndex=0;arrIndex<arrLength;arrIndex++){
            /* CASE #1374 End */
                  if(arrConflictStatus[arrIndex] != null) {
                      System.out.println("arrConflictStatus: "+arrConflictStatus[arrIndex]);
                    if( actionMapping.getPath().equals("/addNewCOIDisclosure")
                        || actionMapping.getPath().equals("/addCOIDisclosure") ) {
                        if(!arrConflictStatus[arrIndex].equals("200") &&
                          !arrConflictStatus[arrIndex].equals("301")) {
                              isConflictStatusOk = false;
                        }
                    }
                    else if(actionMapping.getPath().equals("/editCOIDisclosure") ||
                        actionMapping.getPath().equals("/viewCOIDisclosureDetails")  ) {
                        if(Integer.parseInt(userprivilege) < 2){//User does not have Maintain COI
                            if(!arrConflictStatus[arrIndex].equals("200") &&
                              !arrConflictStatus[arrIndex].equals("301")) {
                                  isConflictStatusOk = false;
                            }
                        }
                        else{//User has Maintain COI
                            if(arrConflictStatus[arrIndex].equals("100") ||
                            arrConflictStatus[arrIndex].equals("101") ||
                            arrConflictStatus[arrIndex].equals("") ){
                                isConflictStatusOkHasMaintain = false;
                            }
                        }
                    }
                  }
                  /* CASE #1374 Move next line up, inside if condition*/
                  //arrDescription[arrIndex] = request.getParameter(("description"+arrIndex));
                }
                if(!isConflictStatusOk){
                      //System.out.println("adding ActionError invalidConflictStatus");
                      actionErrors.add( "invalidConflictStatus" ,
                      new ActionError( "error.addCOIDisclosure.invalidConflictStatus" ) );
                }
                else if(!isConflictStatusOkHasMaintain){
                      //System.out.println("adding ActionError invalidConflictStatusHasMaintain");                    
                      actionErrors.add( "invalidConflictStatusHasMaintain" ,
                      new ActionError( "error.addCOIDisclosure.invalidConflictStatusHasMaintain" ) );
                }
                String explanation = request.getParameter( "txtExplanation" ); //explanation(future use)
                /* CASE #864 Comment Begin */
              // check answers to questions
              /*if(isAnswerAvailable){
                boolean isAnswerCorrect=true;
                String personIDNumber = null; //session.getAttribute("PERSON_ID_NO").toString();
                if( personInfoBean != null ) {
                    personIDNumber = personInfoBean.getPersonID();
                    /* If user has done a select person, so personinfo is not his/her
                    own, but user does not have maintain COI rights, then user is adding
                    a disclosure for themself.  Use loggedinpersonid instead of personid.
                    String loggedinpersonid =
                        (String)session.getAttribute(CoeusActionBase.LOGGEDINPERSONID);
                    if((!loggedinpersonid.equals(personIDNumber)) &&
                          (Integer.parseInt(userprivilege) != 2)){
                          personIDNumber = loggedinpersonid;
                    }
                  }
                  try{
                    disclosureDetailsBean
                                    = new DisclosureDetailsBean(personIDNumber);
                    for(int i=0; i<arrAnswers.length; i++) {
                      if(!disclosureDetailsBean.isDisclAnswerOk(arrQuestionIDs[i], arrAnswers[i])) {
                        isAnswerCorrect=false;
                        break;
                      }
                    }
                  }catch( DBException dbEx ) {
                      request.setAttribute( "EXCEPTION" , dbEx );
                  }
                  // check answers to questions - if incorrect throw error
                  if(!isAnswerCorrect){
                      actionErrors.add( "answerIncorrect" ,
                      new ActionError( "error.addCOIDisclsoureForm.answers.incorrect" ) );
                  }
                }*/
              /*  Check that Disclosure status is valid.
                * If Disclosure status is 200 or 201 (No conflict or Resolved), then
                * Conflict status for all financial entities be 200 or 201 or 202
                * (No Conflict or Resolved or Managed).*/
                boolean isDisclStatusOK = true;
                /* If the request is from the edit page, disclStatCode will be non-null. */
                if(disclStatCode != null){
                  if(disclStatCode.equals("200") || disclStatCode.equals("201")){
                    for(int arrIndex=0;arrIndex<arrLength;arrIndex++){
                        arrConflictStatus[arrIndex]=request.getParameter(("sltConflictStat"+arrIndex));
                        if(arrConflictStatus[arrIndex] != null) {
                          if(!arrConflictStatus[arrIndex].equals("200") &&
                              !arrConflictStatus[arrIndex].equals("201") &&
                              !arrConflictStatus[arrIndex].equals("202")){
                            //System.out.println("*******invalid COI Disclosure Status*******");
                            isDisclStatusOK = false;
                          }//end if
                        }//end if
                      }//end for
                    }//end if
                  }//end if
                  //Check that Disclosure Status is valid.  If invalid, throw error.  Put
                  //entered information into request to be redisplayed.
                  if(!isDisclStatusOK){
                    actionErrors.add("invalidDisclosureStatus",
                       new ActionError( "error.editCOIDisclosure.disclStatus.invalid") );
                  }
                /* If user is editing a disclosure, check whether user has another
                disclosure, of disclosure type Initial, for this particular award or
                proposal.  If yes, then Initial is not a valid disclosure type for this
                disclosure.*/
                  if(actionMapping.getPath().equals("/editCOIDisclosure") ||
                     actionMapping.getPath().equals("/viewCOIDisclosureDetails")  ) {
                      String disclosureNo = (String)session.getAttribute("disclosureNo");
                      String moduleCode = (String)session.getAttribute("moduleCode");
                      String keyNumber = (String)session.getAttribute("keyNumber");
                      String initialDisclNo = disclosureDetails.isPersonHasDisclosure
                                                        (moduleCode, keyNumber, true);
                      if((initialDisclNo != null &&
                        !initialDisclNo.equals(" ")) &&
                          (!initialDisclNo.equals(disclosureNo))){
                          if(disclosureType.equals("I")){
                            actionErrors.add("initial disclosure type invalid",
                                new ActionError("error.addCOIDisclosureForm.invalid.initial"));
                          }
                      }                   
                }
          }//end if disclosureInfo != null && disclosureInfo.size != 0
          
          /* CASE #1374 If validation errors exist, put preselected questions, 
           * answers, conflict status, and 
           * description in session, instead of request, so they
           * can be reloaded in case user edits a fin entity and comes back to page.*/
          if(!actionErrors.isEmpty()){
              session.setAttribute("selectedQuestions",arrQuestionIDs);
              session.setAttribute("selectedAnswers",arrAnswers);
              session.setAttribute("selectedConflictStatus",arrConflictStatus);
              session.setAttribute("selectedDescription",arrDescription);
              /* CASE #1374 Begin */
              session.setAttribute("typeCode", typeCode);
              System.out.println("set typeCode in session: "+typeCode);
              /* CASE #1374 End */
              System.out.println("put selected questoins, answers, conflict status in session");
              /* CASE #1374 Begin */
              //Put actionform in session, to be accessed by JSP 
              //session.setAttribute("frmAddCOIDisclosure", this );
             // System.out.println("put frmAddCOIDisclosure in session");
              /* CASE #1374 End */
              if(actionMapping.getPath().equals("/editCOIDisclosure") ||
                   actionMapping.getPath().equals("/viewCOIDisclosureDetails")  ) {
                /* CASE #1374 Comment Begin */
                //request.setAttribute( "disclosureNo" , session.getAttribute("disclosureNo") );
                //request.setAttribute("disclosureHeader", disclosureHeader);
                //request.setAttribute( "discHeaderAwPrInfo" ,
                  //session.getAttribute("discHeaderAwPrInfo") );
                //request.setAttribute( "moduleCode" , session.getAttribute
                 // ("moduleCode") );   
                /* CASE #1374 Comment End */
                DisclosureHeaderBean disclosureHeader =
                  (DisclosureHeaderBean)session.getAttribute("disclosureHeader");
                disclosureHeader.setDisclStatCode(disclStatCode);
                disclosureHeader.setReviewerCode(disclReviewerCode);
                /* CASE #1374 Begin */
                if(disclosureTypeCode.equals("3") ){
                    session.setAttribute("disclosureHeader", disclosureHeader );
                    DisclosureHeaderBean discHeaderAwPrInfo = 
                        (DisclosureHeaderBean)session.getAttribute("discHeaderAwPrInfo");
                    discHeaderAwPrInfo.setTitle(title);
                    discHeaderAwPrInfo.setSponsor(sponsorName);
                    session.setAttribute("discHeaderAwPrInfo", discHeaderAwPrInfo);
                }
                /* CASE #1374 End */
                if(arrAnswers != null){
                    for(int i=0; i<arrAnswers.length; i++) {
                        if(certDetails != null){
                            CertificateDetailsBean objCertDet = (CertificateDetailsBean)
                                                            certDetails.elementAt(i);
                            objCertDet.setAnswer(arrAnswers[i]);
                        }
                    }
                }
                /* CASE #1374 Comment next line
                //request.setAttribute( "collCOICertDetails" , certDetails );
                /* CASE #1374 Begin */
                session.setAttribute( "collCOICertDetails" , certDetails );
                /* CASE #1374 End */
                /* CASE #1374 Move this up to before if actionErrors not empty
                for(int i=0; i<disclosureInfo.size(); i++) {
                  DisclosureInfoBean disclInfo = 
                        (DisclosureInfoBean) disclosureInfo.elementAt(i);
                  disclInfo.setConflictStatusCode(arrConflictStatus[i]);
                  disclInfo.setDesc(arrDescription[i]);
                }
                /* CASE #1374 Comment End */
                /* CASE #1374 Comment next line*/
                //request.setAttribute( "collCOIDisclosureInfo" , disclosureInfo);
                /* CASE #1374 Begin */
                session.setAttribute( "collCOIDisclosureInfo" , disclosureInfo);
                /* CASE #1374 End */
                /* CASE #1374 Comment Begin */
                /*if(this.showTempProposalEdit){
                    System.out.println("showTempProposalEdit is true");
                    request.setAttribute("showTempProposalEdit", "showTempProposalEdit");
                }*/
                //else    System.out.println("showTempProposalEdit is false");
              }// end if action mapping is editCOIDIsclosure
          }
      }
      catch(DBException DBEx){
          request.setAttribute("EXCEPTION", DBEx);
      }
      catch(CoeusException CEx){
          UtilFactory.log(CEx.getMessage(), CEx, "AddCOIDisclosureActionForm", "perform");
          request.setAttribute("EXCEPTION", CEx);
      } 
      return actionErrors;
    }
    /* CASE #864 End */

    /* CASE #864 Comment Begin */
    /**
     * Validate the properties that have been set for this request,
     * and return an ActionErrors object that encapsulates any validation errors
     * that have been found.
     * If no errors are found, return null or an ActionErrors object with
     * no recorded error messages
     * <br> <b>Validations</b> </br>
     * <li> All Questions should be answered and if any question is left
     * unanswered a message will be supplied to
     * to the user in a page from which the request is generated.</li>
     * <li> If Disclosure status is 200 or 201 (No conflict or Resolved), then
     * Conflict status for all disclosures musts be 200 or 201 or 202 (No Conflict
     * or Resolved or Managed).
     *
     * @param actionMapping The mapping used to select this instance
     * @param request  The servlet request we are processing

    public ActionErrors validate( ActionMapping actionMapping , HttpServletRequest request ){
      System.out.println("Begin AddCOIDisclosureActionForm.validate()");

      ActionErrors actionErrors = new ActionErrors();
      HttpSession session = request.getSession( true );

      /* CASE #231 Begin. */
      /* CASE #357 Begin. */
      /* If user has selected the "Edit Proposal Information" submit button or
       * "Sync" submit button, don't perform the validation check.  Disclosure edits
       * will not be processed until the user selects submit button.
       * User a workaround to determine which button the user has pressed.
      Enumeration attr = request.getParameterNames();
      String att = null;
      while(attr.hasMoreElements()){
          att = (String)attr.nextElement();
          if(att.equals("Edit Temporary Proposal.x") ||
                att.equals("Synchronize Financial Entities.x")){
              return actionErrors;
          }
      }
      /* CASE #231 End. */
      /* CASE #357 End */

      /* CASE #653 Comment Begin */
      /*Check for presence of "hdnEntityNum".  Parameter is set is request by
      JSP when the user has one or more financial entities.  If null, user has
      no financial entities.  Do not perform validation check.  */
      //if(request.getParameterValues("hdnEntityNum") == null){
        //return actionErrors;
      //}
      // set person id in session
      /*
      if(this.personId != null && this.personId.trim().length() > 0 ) {
        session.setAttribute("PERSON_ID_NO", this.personId);
      }

      String[] arrQuestionIDs = request.getParameterValues( "hdnQuestionID" ); //all question ids
      String[] arrQuestionDescs = request.getParameterValues( "hdnQuestionDesc" ); //all question desc
      String[] arrLastUpdate = request.getParameterValues( "hdnLastUpdate" ); //last updated time
      String[] arrAnswers= new String[arrQuestionIDs.length];*/
      /* CASE #653 Comment End */
      /* CASE #653 Begin
      String personId = null;
      DisclosureDetailsBean disclosureDetailsBean = null;
      PersonInfoBean personInfoBean = (PersonInfoBean)session.getAttribute("personInfo");
      if( personInfoBean != null ) {
        try{
            personId = personInfoBean.getPersonID();
            disclosureDetailsBean= new DisclosureDetailsBean(personId);
            LinkedList collCOIStatus = disclosureDetailsBean.getAllCOIStatus();
            request.setAttribute( "collCOIStatus" , collCOIStatus );
            LinkedList collCOIReviewer = disclosureDetailsBean.getAllReviewers();
            request.setAttribute("collCOIReviewer", collCOIReviewer);
            LinkedList collCOIDisclStatus = disclosureDetailsBean.getAllDiscStatus();
            request.setAttribute("collCOIDisclStatus", collCOIDisclStatus);
        }catch( DBException dbEx ) {
            request.setAttribute( "EXCEPTION" , dbEx );
        }
      }
      //If personInfoBean from session is null, then session has expired.  Return
      //control to action classs which will forward to session expired page.
      else if(personInfoBean == null){
          return actionErrors;
      }
      Vector certDetails = (Vector)session.getAttribute("collCOICertDetails");
      String[] arrQuestionIDs = request.getParameterValues( "hdnQuestionID" ); //all question ids
      String[] arrQuestionDescs = request.getParameterValues( "hdnQuestionDesc" ); //all question desc
      String[] arrLastUpdate = request.getParameterValues( "hdnLastUpdate" ); //last updated time
      String[] arrAnswers = null;
      String[] arrConflictStatus = null;
      String[] arrDescription = null;
      boolean isAnswerAvailable=true;
      if( arrQuestionIDs != null ) {
          arrAnswers = new String[arrQuestionIDs.length];
          for( int questionIDIndex = 0 ; questionIDIndex < arrQuestionIDs.length ;
                                                          questionIDIndex++ ) {
              String answer = request.getParameter( arrQuestionIDs[ questionIDIndex ] );
              if( answer == null ) {
                  // set the flag to false
                  isAnswerAvailable=false;
              }
              //collect each answer
              arrAnswers[questionIDIndex] = answer;
          }
      }
      if(!isAnswerAvailable){
          actionErrors.add( "answerRequired" ,
              new ActionError( "error.addCOIDisclsoureForm.answers.requried" ) );
      }
      /* Check size of collection of financial entities stored in session for this
      disclosure.  If size is 0, then check that user has not answered any of
      the certification questions in a way that requires an explanation.  Then,
      skip the rest of the validation.
      Vector disclosureInfo = (Vector)session.getAttribute
                                          ("collCOIDisclosureInfo");
      if(disclosureInfo != null && disclosureInfo.size() == 0){
          try{
              if(certDetails == null){
                  //System.out.println("disclosureNo: "+disclosureNo);
                  certDetails = disclosureDetailsBean.getCOICertificateDetails
                                                                (disclosureNo);
              }
          }
          catch( DBException dbEx ) {
              request.setAttribute( "EXCEPTION" , dbEx );
          }
          catch( CoeusException cEx ){
              request.setAttribute( "EXCEPTION" , cEx );
          }
          for( int questionIDIndex = 0 ; questionIDIndex < arrQuestionIDs.length ;
                                                          questionIDIndex++ ) {
              String answer = request.getParameter( arrQuestionIDs[ questionIDIndex ] );
              CertificateDetailsBean certificateDetailsBean =
                          (CertificateDetailsBean)certDetails.get(questionIDIndex);
              if(certificateDetailsBean.getExplReqFor() != null){
                  String explReqFor = certificateDetailsBean.getExplReqFor();
                  //System.out.println("explReqFor: "+explReqFor);
                  //System.out.println("answer: "+answer);
                  if(answer != null && explReqFor.indexOf(answer) != -1){
                     actionErrors.add("No Fin Ent",
                        new ActionError("error.addCOIDisclosure.noFinEntAnsReqExpl"));
                  }//end if
              }
          }//end for
      }// end if
      else if(disclosureInfo != null && disclosureInfo.size() != 0){
      /* CASE #653 End */

        /* to get all the selcted conflictStatus and Description  information to retain the values
         * when form is invalidated

        boolean isConflictStatusOk = true;
        /* CASE #666 Begin
        boolean isConflictStatusOkHasMaintain = true;
        String userprivilege = (String)session.getAttribute("userprivilege");
        //System.out.println("userprivilege: "+userprivilege);
        /* CASE #666 End
        int arrLength = (request.getParameterValues("hdnEntityNum")).length;
        /* CASE #653 Comment Begin */
        //String[] arrConflictStatus = new String[arrLength];
        //String[] arrDescription = new String[arrLength];
        /* CASE #653 Comment End
        arrConflictStatus = new String[arrLength];
        arrDescription = new String[arrLength];
        /* CASE #653 Begin */
        /* CASE #653 End */
        /* CASE #666 Comment Begin */
        /*for(int arrIndex=0;arrIndex<arrLength;arrIndex++){
            arrConflictStatus[arrIndex]=request.getParameter(("sltConflictStat"+arrIndex));
            //System.out.println("conflict status >>>> " + arrConflictStatus[arrIndex]);

            /* check for disclosure status - either 200 - "No conflict" OR
               301 - "PI Identified Conflict" */
            /*if(arrConflictStatus[arrIndex] != null) {
              //System.out.println("conflict status out of if >>>> " + arrConflictStatus[arrIndex]);
              if(!arrConflictStatus[arrIndex].equals("200") &&
                !arrConflictStatus[arrIndex].equals("301")) {
                //System.out.println("conflict status inside if >>>> " + arrConflictStatus[arrIndex]);
                isConflictStatusOk = false;
              }
            }
            arrDescription[arrIndex] = request.getParameter(("description"+arrIndex));
          }
            */
            /* CASE #666 Comment End */

          /* CASE #666 Begin */
          /* Check for valid status for fin entities disclosed for this award/proposal.
          For all new disclosures, valid status is either 200 - "No Conflict" or
          301 - "PI Identified Conflict".  For edit disclosure, if user does not
          have Maintain COI, then valid status is either 200 or 301.  Else if user
          has Maintain COI, then any staus is valid EXCEPT 100 - "Not Previously Reported".
           for(int arrIndex=0;arrIndex<arrLength;arrIndex++){
              arrConflictStatus[arrIndex]=request.getParameter(("sltConflictStat"+arrIndex));

              if(arrConflictStatus[arrIndex] != null) {
                if(actionMapping.getPath().equals("/addNewCOIDisclosure")) {
                    if(!arrConflictStatus[arrIndex].equals("200") &&
                      !arrConflictStatus[arrIndex].equals("301")) {
                          isConflictStatusOk = false;
                    }
                }
                else if(actionMapping.getPath().equals("/editCOIDisclosure")) {
                    if(Integer.parseInt(userprivilege) < 2){//User does not have Maintain COI
                        if(!arrConflictStatus[arrIndex].equals("200") &&
                          !arrConflictStatus[arrIndex].equals("301")) {
                              isConflictStatusOk = false;
                        }
                    }
                    else{//User has Maintain COI
                        if(arrConflictStatus[arrIndex].equals("100")){
                            isConflictStatusOkHasMaintain = false;
                        }
                    }
                }
              }
              arrDescription[arrIndex] = request.getParameter(("description"+arrIndex));
            }
            if(!isConflictStatusOk){
                  actionErrors.add( "Invalid Conflict Status" ,
                  new ActionError( "error.addCOIDisclosure.invalidConflictStatus" ) );
            }
            else if(!isConflictStatusOkHasMaintain){
                  actionErrors.add( "Invalid Conflict Status Has Maintain" ,
                  new ActionError( "error.addCOIDisclosure.invalidConflictStatusHasMaintain" ) );
            }
            /* CASE #666 End
            //PersonInfoBean personInfoBean = ( PersonInfoBean ) session.getAttribute(PERSONINFO);
            String explanation = request.getParameter( "txtExplanation" ); //explanation(future use)
            /* CASE #653 Comment Begin */
            //Code moved higher up
            /*try{
                //DisclosureDetailsBean disclosureDetailsBean
                                //= new DisclosureDetailsBean( personId);
                LinkedList collCOIStatus = disclosureDetailsBean.getAllCOIStatus();
                request.setAttribute( "collCOIStatus" , collCOIStatus );
                LinkedList collCOIReviewer = disclosureDetailsBean.getAllReviewers();
                request.setAttribute("collCOIReviewer", collCOIReviewer);
                LinkedList collCOIDisclStatus = disclosureDetailsBean.getAllDiscStatus();
                request.setAttribute("collCOIDisclStatus", collCOIDisclStatus);
            }catch( DBException dbEx ) {
                request.setAttribute( "EXCEPTION" , dbEx );
            }*/
            /* CASE #653 Comment Begin */
            //Move this code higher up
            /*boolean isAnswerAvailable=true;
            if( arrQuestionIDs != null ) {
                for( int questionIDIndex = 0 ; questionIDIndex < arrQuestionIDs.length ;
                questionIDIndex++ ) {
                    String answer = request.getParameter( arrQuestionIDs[ questionIDIndex ] );
                    if( answer == null ) {
                        // set the flag to false
                        isAnswerAvailable=false;
                    }
                    //collect each answer
                    arrAnswers[questionIDIndex] = answer;
                }
            }
            /* CASE #666 Begin */
            /*if(!isAnswerAvailable){
                actionErrors.add( "answerRequired" ,
                    new ActionError( "error.addCOIDisclsoureForm.answers.requried" ) );
            }*/
            /* CASE #653 Comment End */
            /* CASE #666 End */
            /* CASE #666 Comment Begin */
            // incorrect conflict status - check for new disclosures
            /*if(actionMapping.getPath().equals("/addNewCOIDisclosure")) {
              if(!isConflictStatusOk){
                  actionErrors.add( "Invalid ConflictStatus" ,
                  new ActionError( "error.addCOIDisclosure.invalidConflictStatus" ) );
                  request.setAttribute("selectedQuestions",arrQuestionIDs);
                  request.setAttribute("selectedAnswers",arrAnswers);
                  request.setAttribute("selectedConflictStatus",arrConflictStatus);
                  request.setAttribute("selectedDescription",arrDescription);
              }
            }

            // if all answers are not available for all questions then show error messages to user.
            if(!isAnswerAvailable){
                actionErrors.add( "answerRequired" ,
                    new ActionError( "error.addCOIDisclsoureForm.answers.requried" ) );
                request.setAttribute("selectedQuestions",arrQuestionIDs);
                request.setAttribute("selectedAnswers",arrAnswers);
                request.setAttribute("selectedConflictStatus",arrConflictStatus);
                request.setAttribute("selectedDescription",arrDescription);
                if(actionMapping.getPath().equals("/editCOIDisclosure")) {
                  request.setAttribute( "action" , session.getAttribute("action"));
                  request.setAttribute( "disclosureNo" ,
                    session.getAttribute("disclosureNo") );
                  request.setAttribute( "disclosureHeader" ,
                    session.getAttribute("disclosureHeader") );
                  request.setAttribute( "discHeaderAwPrInfo" ,
                    session.getAttribute("discHeaderAwPrInfo") );
                  request.setAttribute( "moduleCode" , session.getAttribute("moduleCode") );
                  Vector certDetails = (Vector)session.getAttribute("collCOICertDetails");
                  for(int i=0; i<arrAnswers.length; i++) {
                    CertificateDetailsBean objCertDet = (CertificateDetailsBean) certDetails.elementAt(i);
                    objCertDet.setAnswer(arrAnswers[i]);
                  }
                  request.setAttribute( "collCOICertDetails" , certDetails );
                  Vector disclosureInfo = (Vector)session.getAttribute
                    ("collCOIDisclosureInfo");
                  for(int i=0; i<arrConflictStatus.length; i++) {
                    DisclosureInfoBean disclInfo = (DisclosureInfoBean) disclosureInfo.elementAt(i);
                    disclInfo.setConflictStatus(arrConflictStatus[i]);
                    disclInfo.setDesc(arrDescription[i]);
                  }
                  request.setAttribute( "collCOIDisclosureInfo" , disclosureInfo);
                  request.setAttribute("selectedConflictStatus",arrConflictStatus);
                }
            }*/
            /* CASE #666 Comment End

          // check answers to questions
          if(isAnswerAvailable){
            boolean isAnswerCorrect=true;
            String personIDNumber = null; //session.getAttribute("PERSON_ID_NO").toString();
            /* CASE #653 Comment Begin */
            //Code moved further up
            /*PersonInfoBean personInfoBean = null;
            personInfoBean = ( PersonInfoBean ) session.getAttribute( "personInfo" );*/
            /* CASE #653 Comment End
            if( personInfoBean != null ) {
                personIDNumber = personInfoBean.getPersonID();
                /*Begin CASE #212 */
                /* If user has done a select person, so personinfo is not his/her
                own, but user does not have maintain COI rights, then user is adding
                a disclosure for themself.  Use loggedinpersonid instead of personid.
                String loggedinpersonid =
                    (String)session.getAttribute(CoeusActionBase.LOGGEDINPERSONID);
                /* CASE #666 Comment Begin */
                //String userprivilege = (String)session.getAttribute("userprivilege");
                /* CASE #666 Comment End
                if((!loggedinpersonid.equals(personIDNumber)) &&
                      (Integer.parseInt(userprivilege) != 2)){
                      personIDNumber = loggedinpersonid;
                }

                /* End CASE #212
              }
              try{
                disclosureDetailsBean
                                = new DisclosureDetailsBean(personIDNumber);
                for(int i=0; i<arrAnswers.length; i++) {
                  if(!disclosureDetailsBean.isDisclAnswerOk(arrQuestionIDs[i], arrAnswers[i])) {
                    isAnswerCorrect=false;
                    break;
                  }
                }
              }catch( DBException dbEx ) {
                  request.setAttribute( "EXCEPTION" , dbEx );
              }
              // check answers to questions - if incorrect throw error
              if(!isAnswerCorrect){
                  actionErrors.add( "answerIncorrect" ,
                  new ActionError( "error.addCOIDisclsoureForm.answers.incorrect" ) );
              }
            }
            /* CASE #666 Comment Begin
                request.setAttribute("selectedQuestions",arrQuestionIDs);
                request.setAttribute("selectedAnswers",arrAnswers);
                request.setAttribute("selectedConflictStatus",arrConflictStatus);
                request.setAttribute("selectedDescription",arrDescription);

                if(actionMapping.getPath().equals("/editCOIDisclosure")) {
                  request.setAttribute( "action" , session.getAttribute("action"));
                  request.setAttribute( "disclosureNo" ,
                    session.getAttribute("disclosureNo") );
                  request.setAttribute( "disclosureHeader" ,
                    session.getAttribute("disclosureHeader") );
                  request.setAttribute( "discHeaderAwPrInfo" ,
                    session.getAttribute("discHeaderAwPrInfo") );
                  //request.setAttribute( "collCOIDisclosureInfo" , session.getAttribute("collCOIDisclosureInfo") );
                  //request.setAttribute( "collCOICertDetails" , session.getAttribute("collCOICertDetails") );
                  request.setAttribute( "moduleCode" ,
                    session.getAttribute("moduleCode") );

                  Vector certDetails = (Vector)session.getAttribute("collCOICertDetails");
                  for(int i=0; i<arrAnswers.length; i++) {
                    CertificateDetailsBean objCertDet = (CertificateDetailsBean)
                      certDetails.elementAt(i);
                    objCertDet.setAnswer(arrAnswers[i]);
                  }
                  request.setAttribute( "collCOICertDetails" , certDetails );

                  Vector disclosureInfo = (Vector)session.getAttribute("collCOIDisclosureInfo");
                  for(int i=0; i<arrConflictStatus.length; i++) {
                    DisclosureInfoBean disclInfo = (DisclosureInfoBean) disclosureInfo.elementAt(i);
                    disclInfo.setConflictStatus(arrConflictStatus[i]);
                    disclInfo.setDesc(arrDescription[i]);
                  }
                  request.setAttribute( "collCOIDisclosureInfo" , disclosureInfo);
                  request.setAttribute("selectedConflictStatus",arrConflictStatus);
                }
            }
          }*/
          /*  Check that Disclosure status is valid.
            * If Disclosure status is 200 or 201 (No conflict or Resolved), then
            * Conflict status for all financial entities be 200 or 201 or 202
            * (No Conflict or Resolved or Managed).
            boolean isDisclStatusOK = true;
            /* If the request is from the edit page, disclStatCode will be non-null.
            if(disclStatCode != null){
              if(disclStatCode.equals("200") || disclStatCode.equals("201")){
                for(int arrIndex=0;arrIndex<arrLength;arrIndex++){
                    arrConflictStatus[arrIndex]=request.getParameter(("sltConflictStat"+arrIndex));
                    if(arrConflictStatus[arrIndex] != null) {
                      if(!arrConflictStatus[arrIndex].equals("200") &&
                          !arrConflictStatus[arrIndex].equals("201") &&
                          !arrConflictStatus[arrIndex].equals("202")){
                        //System.out.println("*******invalid COI Disclosure Status*******");
                        isDisclStatusOK = false;
                      }//end if
                    }//end if
                  }//end for
                }//end if
              }//end if
              //Check that Disclosure Status is valid.  If invalid, throw error.  Put
              //entered information into request to be redisplayed.
              if(!isDisclStatusOK){
                actionErrors.add("invalidDisclosureStatus",
                   new ActionError( "error.editCOIDisclosure.disclStatus.invalid") );
              }
         // }
          /* CASE #666 Comment Begin */
          /*if(isAnswerAvailable){
                 request.setAttribute("selectedQuestions",arrQuestionIDs);
                  request.setAttribute("selectedAnswers",arrAnswers);
                  request.setAttribute("selectedConflictStatus",arrConflictStatus);
                  request.setAttribute("selectedDescription",arrDescription);
                  if(actionMapping.getPath().equals("/editCOIDisclosure")) {
                    request.setAttribute( "action" , session.getAttribute("action"));
                    request.setAttribute( "disclosureNo" ,
                      session.getAttribute("disclosureNo") );
                    request.setAttribute( "disclosureHeader" ,
                      session.getAttribute("disclosureHeader") );
                    request.setAttribute( "discHeaderAwPrInfo" ,
                      session.getAttribute("discHeaderAwPrInfo") );
                    request.setAttribute( "moduleCode" , session.getAttribute
                      ("moduleCode") );
                    Vector certDetails = (Vector)session.getAttribute
                      ("collCOICertDetails");
                    for(int i=0; i<arrAnswers.length; i++) {
                      CertificateDetailsBean objCertDet = (CertificateDetailsBean)
                        certDetails.elementAt(i);
                      objCertDet.setAnswer(arrAnswers[i]);
                    }
                    request.setAttribute( "collCOICertDetails" , certDetails );
                    Vector disclosureInfo = (Vector)session.getAttribute
                      ("collCOIDisclosureInfo");
                    for(int i=0; i<arrConflictStatus.length; i++) {
                      DisclosureInfoBean disclInfo = (DisclosureInfoBean) disclosureInfo.elementAt(i);
                      disclInfo.setConflictStatus(arrConflictStatus[i]);
                      disclInfo.setDesc(arrDescription[i]);
                    }
                    request.setAttribute( "collCOIDisclosureInfo" , disclosureInfo);
                    request.setAttribute("selectedConflictStatus",arrConflictStatus);
                  }
              }
            }*/
            /* CASE #666 Comment End */

            /* CASE #250 Begin. */
            /* If user is editing a disclosure, check whether user has another
            disclosure, of disclosure type Initial, for this particular award or
            proposal.  If yes, then Initial is not a valid disclosure type for this
            disclosure.
              if(actionMapping.getPath().equals("/editCOIDisclosure")) {
                try{
                  /* CASE #653 Comment Begin */
                  //Code moved higher up
                  /*String personId = null;
                  PersonInfoBean personInfoBean = (PersonInfoBean)session.getAttribute("personInfo");
                  if( personInfoBean != null ) {
                    personId = personInfoBean.getPersonID();
                  }*/
                  /* CASE #653 Comment End
                  DisclosureDetailsBean disclosureDetails =
                          new DisclosureDetailsBean(personId);
                  String disclosureNo = (String)session.getAttribute("disclosureNo");
                  String moduleCode = (String)session.getAttribute("moduleCode");
                  String keyNumber = (String)session.getAttribute("keyNumber");
                  String initialDisclNo = disclosureDetails.isPersonHasDisclosure
                                                    (moduleCode, keyNumber, true);
                  if((!initialDisclNo.equals(" ")) &&
                      (!initialDisclNo.equals(disclosureNo))){
                      if(disclosureType.equals("I")){
                        actionErrors.add("initial disclosure type invalid",
                            new ActionError("error.addCOIDisclosureForm.invalid.initial"));
                      }
                  }

              /* CASE #666 Comment Begin */
                        /*request.setAttribute( "action" , session.getAttribute("action"));
                        request.setAttribute( "disclosureNo" ,
                          session.getAttribute("disclosureNo") );
                        request.setAttribute( "disclosureHeader" ,
                          session.getAttribute("disclosureHeader") );
                        request.setAttribute( "discHeaderAwPrInfo" ,
                          session.getAttribute("discHeaderAwPrInfo") );
                        request.setAttribute( "moduleCode" ,
                          session.getAttribute("moduleCode") );
                        Vector certDetails = (Vector)session.getAttribute
                          ("collCOICertDetails");
                        for(int i=0; i<arrAnswers.length; i++) {
                          CertificateDetailsBean objCertDet = (CertificateDetailsBean)
                            certDetails.elementAt(i);
                          objCertDet.setAnswer(arrAnswers[i]);
                        }
                        request.setAttribute( "collCOICertDetails" , certDetails );
                        Vector disclosureInfo = (Vector)session.getAttribute
                          ("collCOIDisclosureInfo");
                        for(int i=0; i<arrConflictStatus.length; i++) {
                          DisclosureInfoBean disclInfo = (DisclosureInfoBean) disclosureInfo.elementAt(i);
                          disclInfo.setConflictStatus(arrConflictStatus[i]);
                          disclInfo.setDesc(arrDescription[i]);
                        }
                        request.setAttribute( "collCOIDisclosureInfo" , disclosureInfo);
                        request.setAttribute("selectedConflictStatus",arrConflictStatus);
                      }
                  }*/
              /* CASE #666 Comment End
              }
              catch(DBException DBEx){
                request.setAttribute("EXCEPTION", DBEx);
              }
            }
            /* CASE # 250 End.
      }//end else if disclosureInfo != null && disclosureInfo.size != 0
      /* CASE #666 Begin
      //If there are validation errors, put in request info to redisplay
      //page with user's selected answers.
      if(!actionErrors.empty()){
          request.setAttribute("selectedQuestions",arrQuestionIDs);
          request.setAttribute("selectedAnswers",arrAnswers);
          request.setAttribute("selectedConflictStatus",arrConflictStatus);
          request.setAttribute("selectedDescription",arrDescription);

          if(actionMapping.getPath().equals("/editCOIDisclosure")) {
            request.setAttribute( "action" , session.getAttribute("action"));
            request.setAttribute( "disclosureNo" ,
              session.getAttribute("disclosureNo") );
            DisclosureHeaderBean disclosureHeader =
              (DisclosureHeaderBean)session.getAttribute("disclosureHeader");
            disclosureHeader.setDisclStatCode(disclStatCode);
            disclosureHeader.setReviewerCode(disclReviewerCode);
            request.setAttribute("disclosureHeader", disclosureHeader);
            request.setAttribute( "discHeaderAwPrInfo" ,
              session.getAttribute("discHeaderAwPrInfo") );
            request.setAttribute( "moduleCode" , session.getAttribute
              ("moduleCode") );
            if(arrAnswers != null){
                for(int i=0; i<arrAnswers.length; i++) {
                    if(certDetails != null){
                        CertificateDetailsBean objCertDet = (CertificateDetailsBean)
                                                        certDetails.elementAt(i);
                        objCertDet.setAnswer(arrAnswers[i]);
                    }
                }
            }
            request.setAttribute( "collCOICertDetails" , certDetails );
            for(int i=0; i<disclosureInfo.size(); i++) {
              DisclosureInfoBean disclInfo = (DisclosureInfoBean) disclosureInfo.elementAt(i);
              disclInfo.setConflictStatusCode(arrConflictStatus[i]);
              disclInfo.setDesc(arrDescription[i]);
            }
            request.setAttribute( "collCOIDisclosureInfo" , disclosureInfo);
          }
      }
      /* CASE #666 End

      return actionErrors;
    }*/
    /* CASE #864 Comment End */

}