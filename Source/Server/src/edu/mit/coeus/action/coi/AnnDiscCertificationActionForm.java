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

import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.coi.bean.AnnualDiscFinEntitiesBean;
import edu.mit.coeus.coi.bean.CertificateDetailsBean;
import edu.mit.coeus.coi.bean.DisclosureDetailsBean;
import edu.mit.coeus.coi.bean.CertQuestionDetailsBean;
import edu.mit.coeus.coi.bean.EntityDetailsBean;
import edu.mit.coeus.coi.bean.FinancialEntityDetailsBean;
import javax.servlet.http.HttpSession;
import edu.mit.coeus.bean.PersonInfoBean;

/**
 * <code>AddCOIDisclosureActionForm</code>  is struts implemented Form class
 * holds a new COIDisclosure information.
 *
 * @author	RaYaKu
 * @version	1.0   June 10,2002 16:30:23
 */

public class AnnDiscCertificationActionForm extends ActionForm{

    /**
     * Id of the Person
     */
    private String personId;

    /**
     * Name of the Person
     */
    private String personFullName;

    /**
     * Value of User Name
     */
    private String userName;
    
    /* CASE #1374 Begin */
    private boolean hasActiveEntities;
    
    public boolean getHasActiveEntities(){
        return hasActiveEntities;
    }
    
    public void setHasActiveEntities(boolean hasActiveEntities){
        this.hasActiveEntities = hasActiveEntities;
    }
    /* CASE #1374 End */



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

    private Vector annDiscCertQuestions;

    public Vector getAnnDiscCertQuestions(){
        return annDiscCertQuestions;
    }

    public void setAnnDiscCertQuestions(Vector annDiscCertQuestions){
        this.annDiscCertQuestions = annDiscCertQuestions;
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
      System.out.println("Inside AnnDiscCertificationForm.validate()");

      ActionErrors actionErrors = new ActionErrors();
      HttpSession session = request.getSession( true );
//      UtilFactory UtilFactory = new UtilFactory();

      PersonInfoBean personInfoBean = (PersonInfoBean)session.getAttribute("personInfo");
      if( personInfoBean != null ) {
          this.personId = personInfoBean.getPersonID();
      }
      //If personInfoBean from session is null, then session has expired.  Return
      //control to action class which will forward to session expired page.
      else if(personInfoBean == null){
          return actionErrors;
      }
      try{
          String[] arrQuestionIDs = request.getParameterValues( "hdnQuestionID" ); //all question ids
          String[] arrAnswers = null;
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
          /* CASE #1374 Begin */
          session.removeAttribute("backToDiscl");
          String actionFrom = (String)request.getAttribute("actionFrom");
          if(actionFrom == null){
              actionFrom = request.getParameter("actionFrom");
          }
          System.out.println("actionFrom: "+actionFrom);
          if( actionFrom != null && actionFrom.equals("editFinEnt") ) {
                arrQuestionIDs = (String[])session.getAttribute("selectedQuestions");
                arrAnswers = (String[])session.getAttribute("selectedAnswers");               
          }
          if(arrQuestionIDs == null){
              System.out.println("skip validation");
              session.removeAttribute("certQuestionErrors");
              return actionErrors;
          }
          /* CASE #1374 End */
          /* CASE #1374 Comment next line */
          //else return actionErrors;
          if(!isAnswerAvailable){
              actionErrors.add( "answerRequired" ,
                  new ActionError( "error.addCOIDisclsoureForm.answers.requried" ) );
          }
          //Check validity of user selected answers.
          DisclosureDetailsBean disclosureDetails = 
                                    new DisclosureDetailsBean(personId);
          CertQuestionDetailsBean certQuestionDetails = 
                                    new CertQuestionDetailsBean(personId);
          
          boolean foundNoFinEnt;
          boolean foundEntNoAnsYes;
          boolean foundEntYesAnsNo; 
          /* CASE #1393 Begin */
          HashMap certQuestionErrors = new HashMap();
          /* CASE #1393 End */
          

          for(int questCnt=0; questCnt<arrQuestionIDs.length; questCnt++){
              
              /* CASE #1393 Begin */
              String questionID = arrQuestionIDs[questCnt];              
              foundNoFinEnt = false;
              foundEntNoAnsYes = false;
              foundEntYesAnsNo = false; 
              /* CASE #1393 End */              
              
              String entityNumber = disclosureDetails.isDisclAnswerOk(
                  arrQuestionIDs[questCnt], arrAnswers[questCnt]);

              System.out.println("****entityNumber: "+entityNumber+", returned for answer: "+arrAnswers[questCnt]);
              
              if(!entityNumber.equals("1")){
                  if(entityNumber.equals("-1") && !foundNoFinEnt){
                          actionErrors.add ("answersIncorrect",
                              new ActionError( "error.annDiscCertification.noFinEntAnsReq"));
                          foundNoFinEnt = true;
                          /* CASE #1393 Begin */
                          certQuestionErrors.put(questionID, entityNumber);
                          /* CASE #1393 End */
                         
                  }
                  else if(entityNumber.equals("-2")&& !foundEntNoAnsYes){
                          actionErrors.add ("answersIncorrect",
                              new ActionError( "error.annDiscCertification.entNoAnsYes"));
                          foundEntNoAnsYes = true;
                          /* CASE #1393 Begin */
                          certQuestionErrors.put(questionID, entityNumber);
                          /* CASE #1393 End */                          
                  }
                  else if(!foundEntYesAnsNo){
                          /* CASE #1393 Comment Begin */
                          /*                        
                          FinancialEntityDetailsBean financialEntityDetails =
                              new FinancialEntityDetailsBean(personId);
                          EntityDetailsBean entityWithYes =
                              financialEntityDetails.getFinancialEntityDetails(entityNumber);
                          String entityName = entityWithYes.getName();
                          String seqNo = entityWithYes.getSeqNumber();
                          actionErrors.add ("answersIncorrect",
                              new ActionError( "error.annDiscCertification.entYesAnsNo"));
                          request.setAttribute("errorEntityNumber", entityNumber);
                          request.setAttribute("errorEntityName", entityName);
                          request.setAttribute("errorEntitySeqNo", seqNo);*/
                          /* CASE #1393 Comment End */
                          foundEntYesAnsNo = true;
                          /* CASE #1393 Begin */
                          actionErrors.add ("answersIncorrect",
                              new ActionError( "error.annDiscCertification.entYesAnsNo"));  
                          HashMap entitiesWithYes = 
                            certQuestionDetails.getEntitiesCertQuestionYes(questionID);
                          certQuestionErrors.put(questionID, entitiesWithYes);                          
                          /* CASE #1393 End */
                  }
                  /* CASE #1393 Begin */
                  session.setAttribute("certQuestionErrors", certQuestionErrors);
                  /* CASE #1393 End */
              }
          }
            AnnualDiscFinEntitiesBean annualDiscFinEntities =
                new AnnualDiscFinEntitiesBean( personId.trim() );            
            boolean hasActiveEntities = annualDiscFinEntities.checkPersonHasActiveFE();
            this.setHasActiveEntities(hasActiveEntities);          
          Vector annDiscCertQuestions = (Vector)session.getAttribute("annDiscCertQuestions");
          if(annDiscCertQuestions == null){
                return actionErrors;
          }          
          if(!actionErrors.isEmpty()){
              //Set user selected answers in cert details collection to be redisplayed
              if(arrAnswers != null){
                  for(int i=0; i<arrAnswers.length; i++) {
                      if(annDiscCertQuestions != null){
                          CertificateDetailsBean certDetails =
                              (CertificateDetailsBean)annDiscCertQuestions.elementAt(i);
                          certDetails.setAnswer(arrAnswers[i]);
                      }
                  }
              }
              session.setAttribute( "annDiscCertQuestions", annDiscCertQuestions );
              /* CASE #1374 Begin */
              //Put user selected answers in session, to redisplay validation errors
              //in case user edits fin ent
              session.setAttribute("selectedQuestions", arrQuestionIDs);
              session.setAttribute("selectedAnswers", arrAnswers);
              /* CASE #1374 End */
          }
          else {
            session.removeAttribute("certQuestionErrors");
          }
      }catch(DBException DBEx){
          request.setAttribute("EXCEPTION", DBEx);
      }/* CASE #1393 Comment Begin */
      /*catch(CoeusException CEx){
          UtilFactory.log(CEx.getMessage(), CEx, "AnnDiscCertificationActionForm",
              "perform");
          request.setAttribute("EXCEPTION", CEx);
      }*/
      /* CASE #1393 Comment End */
      return actionErrors;
    }

}