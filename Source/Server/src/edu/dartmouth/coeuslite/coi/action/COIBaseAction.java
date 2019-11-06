/*
 * COIBaseAction.java
 *
 * Created on December 26, 2005, 3:37 PM
 */

package edu.dartmouth.coeuslite.coi.action;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.beans.CoiFinancialEntityBean;
import edu.mit.coeuslite.coiv2.beans.CoiInfoBean;
import edu.mit.coeuslite.coiv2.beans.CoiMenuBean;
import edu.mit.coeuslite.coiv2.services.CoiAttachmentService;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;
import edu.mit.coeuslite.coiv2.services.GettingRightsCoiV2Service;
import edu.mit.coeuslite.coiv2.utilities.CoiConstants;
import edu.mit.coeuslite.irb.bean.ReadProtocolDetails;
import edu.mit.coeuslite.utils.CoeusBaseAction;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * @author  vinayks
 */
public abstract class COIBaseAction extends CoeusBaseAction{
    private WebTxnBean webTxnBean ;
    //base class have the same variable
    //public static final String EMPTY_STRING = "";
    public static final String LOGGEDINPERSONID = "loggedinpersonid";
    private static final String SUB_HEADER = "dartmouthHeaderVector";
    private static final String XML_PATH = "/edu/dartmouth/coeuslite/coi/xml/COISubMenu.xml";
    public static final String LOGGEDINPERSONNAME = "loggedinpersonname";
    public static final String VIEW_PENDING_DISC = "viewPendingDisc";
    public static final String PRIVILEGE = "userprivilege";
    public static final String RIGHT="rights";
    public static final String COI_ADMIN="COI_ADMIN";
    private static final String COI_DART_SESSION_INITIALIZED = "COI_DART_SESSION_INITIALIZED";
    private static final String USER ="user";    
    private static String ANNUAL ="Annual";
    private static String REVISION ="Revision";
    private static String PROPOSAL ="Proposal";
    private static String PROTOCOL ="Protocol";
    private static String IACUCPROTOCOL ="IACUCProtocol"; 
    private static String AWARD ="Award";
    private static String TRAVEL ="Travel";    
    /** Creates a new instance of COIBaseAction */
    public COIBaseAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping,
    ActionForm actionForm, HttpServletRequest request,
    HttpServletResponse response) throws Exception{
        /*
         *Fix # 2866
         *Moved checkCOIPrivileges(request) and getSubheaderDetails() method to base class and 
         *create another abstract method performExecuteCOI(), which needs to be overridden by 
         *all other concrete COI action classes
         *Initialize COI session only once
         */
        //Begin
        HttpSession coiSession = request.getSession();
        Boolean coiSessionInit = (Boolean)coiSession.getAttribute(COI_DART_SESSION_INITIALIZED);
        if(coiSessionInit==null || !coiSessionInit.booleanValue()){
            checkCOIPrivileges(request);
            getSubheaderDetails();
            coiSession.setAttribute(COI_DART_SESSION_INITIALIZED, Boolean.TRUE);
        }
        LoadHeaderDetails(request);
        getApprovedDisclosure(request);
        setCoiInfoDetails(request);
        checkIsApprvdAnn(request);
        checkAnnualReviewDue(request);
        checkAssignedDisclosurePresent(request);

        if(actionMapping.getPath().equals("/showAllDiscl") || actionMapping.getPath().equals("/showAllAnnualDiscl") ||
                actionMapping.getPath().equals("/showAllWipDiscl")) {
                GettingRightsCoiV2Service gettingRightsCoiV2Service = GettingRightsCoiV2Service.getInstance();
                boolean isAdmin = gettingRightsCoiV2Service.isAdmin(request);
            if(!isAdmin){
//                CoeusException ex = new CoeusException("Sorry you have no previlege to view/edit.");
//                request.setAttribute("Exception", ex);
                request.setAttribute("Message", "Sorry, you have no view or edit rights.");
                return actionMapping.findForward("error");
            }
        }

        return performExecuteCOI(actionMapping,actionForm,request,response);
        //End
    }
    
    public abstract ActionForward performExecuteCOI(ActionMapping actionMapping,
    ActionForm actionForm, HttpServletRequest request,
    HttpServletResponse response) throws Exception;
    
    public Timestamp prepareTimeStamp() throws Exception{
        Timestamp dbTimestamp = null;
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        return dbTimestamp;
    }
    
    /**
     *Method to validate a disclosure while editing a disclosure
     */
    public ActionMessages validateDisclosure(HttpServletRequest request,
    ActionMapping actionMapping,DynaValidatorForm dynaValidatorForm)
    throws Exception{
        ActionMessages actionMessages = new ActionMessages();
        HttpSession session = request.getSession();        
        String actionFrom = (String)request.getAttribute("actionFrom");       
        
        /* If user has selected "Sync" submit button, don't perform the 
         * validation check.  Disclosure edits
         * will not be processed until the user selects submit button.
         * User a workaround to determine which button the user has pressed. 
         */
//          Enumeration attr = request.getParameterNames();
//          String att = null;
//          while(attr.hasMoreElements()){
//              att = (String)attr.nextElement();             
//              if(att.equals("Synchronize Financial Entities.x")){
//                  request.setAttribute("synchronize", "true");
//                  return actionMessages;
//              }
//          }
        
        
        if(actionMapping.getPath().equals("/viewCOIDisclosureDetails") ){
            if(actionFrom == null || !actionFrom.equals("editFinEnt") ){               
                return actionMessages;
            }
        }
        
        String userprivilege = (String)session.getAttribute(PRIVILEGE);
        
        webTxnBean = new WebTxnBean();
        
        String[] arrQuestionIDs = null;
        String[] arrQuestionDescs = null;
        String[] arrAnswers = null;
        String[] arrConflictStatus = null;
        String[] arrDescription = null;
        boolean isAnswerAvailable = true;
        
        if( actionMapping.getPath().equals("/viewCOIDisclosureDetails")||
        actionMapping.getPath().equals("/getAddDisclosure")){
            arrQuestionIDs = (String[])session.getAttribute("selectedQuestions");
            arrAnswers = (String[])session.getAttribute("selectedAnswers");
            arrConflictStatus = (String[])session.getAttribute("selectedConflictStatus");
            arrDescription = (String[])session.getAttribute("selectedDescription");
        }else{
            arrQuestionIDs = request.getParameterValues("hdnQuestionId");
            arrQuestionDescs = request.getParameterValues("hdnQuestionDesc"); //all question desc
            String[] arrLastUpdate = request.getParameterValues("hdnLastUpdate");
            if(arrQuestionIDs!= null){
                arrAnswers = new String[arrQuestionIDs.length];
                for(int questionIDIndex = 0 ; questionIDIndex < arrQuestionIDs.length ;
                questionIDIndex++){
                    String answer = request.getParameter( arrQuestionIDs[ questionIDIndex ] );
                    if( answer == null || answer.equals("")) {
                        isAnswerAvailable=false;
                    }//end if
                    //collect each answer
                    arrAnswers[questionIDIndex] = answer;
                }//end for
            }//end if
        }
        
        Vector vecCertDetails = (Vector)session.getAttribute("questionsData");
        if(!isAnswerAvailable){
            actionMessages.add("answerRequired",
            new ActionMessage("error.addCOIDisclsoureForm.answers.requried"));
            saveMessages(session, actionMessages);            
        }else if(isAnswerAvailable){
            String personId = (String)dynaValidatorForm.get("personId");
            String personIdForCerValidation = personId;
             /* If user has done a select person, so personinfo is not his/her
              own, but user does not have maintain COI rights, then user is adding
              a disclosure for themself.  Use loggedinpersonid instead of personid.*/
            String loggedinpersonid =
            (String)session.getAttribute(LOGGEDINPERSONID);
            if((!loggedinpersonid.equals(personId)) &&
            (Integer.parseInt(userprivilege) != 2)){
                personIdForCerValidation = loggedinpersonid;
            }
            if(arrQuestionIDs!= null){
                HashMap certQuestionErrors = new HashMap();
                for(int index = 0;index < arrQuestionIDs.length ; index++){
                    boolean foundNoFinEnt = false;
                    boolean foundEntNoAnsYes = false;
                    boolean foundEntYesAnsNo = false;
                    
                    HashMap hmData = new HashMap();
                    String questionID = arrQuestionIDs[index];
                    hmData.put("questionId",questionID);
                    hmData.put("personId",personIdForCerValidation);
                    hmData.put("answer",arrAnswers[index]);
                    try {
                        Hashtable htDisclAnsOk =
                        (Hashtable)webTxnBean.getResults(request, "isDisclAnswerOk",hmData);
                        String entityNumber = (String)
                        ((HashMap)htDisclAnsOk.get("isDisclAnswerOk")).get("entity_number");
                        if(entityNumber!=null || !entityNumber.equals(EMPTY_STRING)){
                            if(!entityNumber.equals("1")){
                                if(entityNumber.equals("-1") && !foundNoFinEnt){
                                    actionMessages.add(ActionMessages.GLOBAL_MESSAGE,
                                    new ActionMessage("error.addCOIDisclosure.noFinEntAnsReqExpl"));
                                    saveMessages(request, actionMessages);
                                    certQuestionErrors.put(questionID, entityNumber);
                                    foundNoFinEnt = true;
                                } else if(entityNumber.equals("-2") && !foundEntNoAnsYes){
                                    actionMessages.add(ActionMessages.GLOBAL_MESSAGE,
                                    new ActionMessage("error.addCOIDisclosure.entNoAnsYes"));
                                    saveMessages(request, actionMessages);
                                    foundEntNoAnsYes = true;
                                    certQuestionErrors.put(questionID, entityNumber);
                                } else if(!foundEntYesAnsNo){
                                    actionMessages.add("answersIncorrect",
                                    new ActionMessage("error.addCOIDisclosure.entYesAnsNo"));
                                    saveMessages(request, actionMessages);
                                    foundEntYesAnsNo = true;

                                    HashMap hmQuestId = new HashMap();
                                    hmQuestId.put("personId",personIdForCerValidation);
                                    hmQuestId.put("questionId",questionID );

                                    HashMap entitiesWithYes =
                                    getEntitiesCertQuestionYes(request,hmQuestId);

                                    certQuestionErrors.put(questionID, entitiesWithYes);
                                }
                                request.setAttribute("certQuestionErrors", certQuestionErrors);
                            }//End if
                        }
                    } catch(Exception ex) {
                         UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(),ex,"dartmouth.coeuslite.coi.action.COIBaseAction","validateDisclosure");
                    }
                }//End for
            }//End if
        }//End else
        
        Vector vecDisclosureInfo =(Vector)session.getAttribute("personDescDet");
        String disclosureNum = (String)dynaValidatorForm.get("coiDisclosureNumber");
         if(session.getAttribute("addFinEnt") != null ){
                if(actionMapping.getPath().equals("/viewCOIDisclosureDetails")  ) {
                   vecDisclosureInfo =  getCOIDisclosureInfo(request,disclosureNum);
                }
                else if(actionMapping.getPath().equals("/addCoiDisclosure") ){
                    vecDisclosureInfo = getCOIDisclInfoForPerson(request,disclosureNum);
                }
                session.setAttribute("personDescDet", vecDisclosureInfo);
            }     
        if(vecDisclosureInfo!=null && vecDisclosureInfo.size() > 0){
            /* to get all the selcted conflictStatus and Description
               information to retain the values
             * when form is invalidated
             */
            boolean isConflictStatusOk = true;
            boolean isConflictStatusOkHasMaintain = true;
            int arrLength = vecDisclosureInfo.size();
            if (actionMapping.getPath().equals("/addCoiDisclosure")  ||
            actionMapping.getPath().equals("/updCoiDisclosure") ){
                arrConflictStatus = new String[arrLength];
                arrDescription = new String[arrLength];
                  /* Check for valid status for fin entities disclosed for this award/proposal.
                  For all new disclosures, valid status is either 200 - "No Conflict" or
                  301 - "PI Identified Conflict".  For edit disclosure, if user does not
                  have Maintain COI, then valid status is either 200 or 301.  Else if user
                  has Maintain COI, then any staus is valid EXCEPT 100 - "Not Previously Reported"
                   or 101 - Financial Entity Changed.
                   */
                for(int arrIndex=0;arrIndex<arrLength;arrIndex++){
                    arrConflictStatus[arrIndex]=request.getParameter(("sltConflictStatus"+arrIndex));
                    arrDescription[arrIndex] = request.getParameter(("description"+arrIndex));
                }//End for
            }//End if            
         
            for(int arrIndex=0;arrIndex<arrLength;arrIndex++){
                if(arrConflictStatus!=null){
                    if(arrConflictStatus[arrIndex] != null) {
                        if(actionMapping.getPath().equals("/addCoiDisclosure")) {
                            if(!arrConflictStatus[arrIndex].equals("200") &&
                            !arrConflictStatus[arrIndex].equals("301")) {
                                isConflictStatusOk = false;
                            }
                        }
                        else if(actionMapping.getPath().equals("/updCoiDisclosure") ||
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
                        }//End else if
                    }//End if
                }//End if
            }//End For
            
            if(!isConflictStatusOk){
                actionMessages.add("invalidConflictStatus",
                new ActionMessage("error.addCOIDisclosure.invalidConflictStatus"));
                saveMessages(request, actionMessages);                
            } else if(!isConflictStatusOkHasMaintain){
                actionMessages.add("invalidConflictStatusHasMaintain",
                new ActionMessage("error.addCOIDisclosure.invalidConflictStatusHasMaintain"));
                saveMessages(request, actionMessages);               
            }
            
         /*  Check that Disclosure status is valid.
          * If Disclosure status is 200 or 201 (No conflict or Resolved), then
          * Conflict status for all financial entities be 200 or 201 or 202
          * (No Conflict or Resolved or Managed).*/
            boolean isDisclStatusOK = true;
            String disclStatCode = (String)dynaValidatorForm.get("coiStatusCode");
            if(disclStatCode != null){
                if(disclStatCode.equals("200") || disclStatCode.equals("201")){
                    for(int arrIndex=0;arrIndex<arrLength;arrIndex++){
                        arrConflictStatus[arrIndex] =
                        request.getParameter(("sltConflictStatus"+arrIndex));
                        if(arrConflictStatus[arrIndex] != null) {
                            if(!arrConflictStatus[arrIndex].equals("200") &&
                            !arrConflictStatus[arrIndex].equals("201") &&
                            !arrConflictStatus[arrIndex].equals("202")){
                                isDisclStatusOK = false;
                            }//end if
                        }//end if
                    }//end for
                }//end if
            }//end if
            
            //Check that Disclosure Status is valid.  If invalid, throw error.  Put
            //entered information into request to be redisplayed.
            if(!isDisclStatusOK){
                actionMessages.add("invalidDisclosureStatus",
                new ActionMessage("error.editCOIDisclosure.disclStatus.invalid"));
                saveMessages(request, actionMessages);               
            }
            /* If user is editing a disclosure, check whether user has another
                disclosure, of disclosure type Initial, for this particular award or
                proposal.  If yes, then Initial is not a valid disclosure type for this
                disclosure.*/
            if(actionMapping.getPath().equals("/updCoiDisclosure")) {
                String disclosureNo = (String)dynaValidatorForm.get("coiDisclosureNumber");
                String moduleCode = (String)dynaValidatorForm.get("moduleCode");
                String keyNumber = (String)dynaValidatorForm.get("moduleItemKey");
                String initialDisclNo = isPersonHasDiscl(request, dynaValidatorForm);
                if((initialDisclNo != null &&
                !initialDisclNo.equals(EMPTY_STRING)) &&
                (!initialDisclNo.equals(disclosureNo))){
                    if(dynaValidatorForm.get("disclosureType").equals("I")){
                        actionMessages.add("initialdDisclosureTypeInvalid",
                        new ActionMessage("error.addCOIDisclosureForm.invalid.initial"));
                    }
                }
            }
        }//End if  vecDisclosureInfo!=null && vecDisclosureInfo.size() > 0
        
        /* If validation errors exist, put preselected questions,
         * answers, conflict status, and
         * description in session, instead of request, so they
         * can be reloaded in case user edits a fin entity and comes back to page.*/
        if(!actionMessages.isEmpty()){
            session.setAttribute("selectedQuestions",arrQuestionIDs);
            session.setAttribute("selectedAnswers",arrAnswers);
            session.setAttribute("selectedConflictStatus",arrConflictStatus);
            session.setAttribute("selectedDescription",arrDescription);
        }
        return actionMessages;
    }
    
    public HashMap getEntitiesCertQuestionYes(HttpServletRequest request,HashMap hmQuestId)
    throws Exception{
        HashMap hmEntitiesWithYes = new HashMap();
        webTxnBean = new WebTxnBean();
        try {
            Hashtable htEntcerQuestYes =
            (Hashtable)webTxnBean.getResults(request, "getEntitiesCertQuestionYes", hmQuestId);
            Vector vecEntitiesWithYes =
            (Vector)htEntcerQuestYes.get("getEntitiesCertQuestionYes");
            if(vecEntitiesWithYes!=null && vecEntitiesWithYes.size()>0){
                for(int index = 0 ; index < vecEntitiesWithYes.size(); index++){
                    DynaValidatorForm dynaForm =
                    (DynaValidatorForm)vecEntitiesWithYes.elementAt(index);
                    String entityNum = (String)dynaForm.get("entityNumber");
                    String entityName = (String)dynaForm.get("entityName");
                    hmEntitiesWithYes.put(entityNum,entityName);
                    //                  entities.put((String)entityRow.get("ENTITY_NUMBER"),
                    //                  (String)entityRow.get("ENTITY_NAME"));

                }
            }
        } catch(Exception ex) {

            UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(),ex,"dartmouth.coeuslite.coi.action.COIBaseAction","getEntitiesCertQuestionYes");
        }
        return hmEntitiesWithYes;
    }
    
    /*To check whether the person has disclosure  */
    public String isPersonHasDiscl(HttpServletRequest request,
    DynaValidatorForm dynaValidatorForm)throws Exception{
        HashMap hmData = new HashMap();
        String appliesToCode =(String)dynaValidatorForm.get("appliesToCode");
        String disclosureTypeCode =(String)dynaValidatorForm.get("disclosureTypeCode");
        String personId = (String)dynaValidatorForm.get("personId");
        String disclNum = "";
        hmData.put("personId",personId);
        hmData.put("appliesToCode", appliesToCode);
        hmData.put("disclosureTypeCode",disclosureTypeCode);
        webTxnBean =  new WebTxnBean();
        try {
            Hashtable htResult = (Hashtable)webTxnBean.getResults(request, "checkPersonHasDisc", hmData);
            disclNum = (String)((HashMap)htResult.get("checkPersonHasDisc")).get("ls_disc_num");
            if(disclNum == null){
                disclNum = EMPTY_STRING;
            }
        }catch(Exception ex) {
            UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(),ex,"dartmouth.coeuslite.coi.action.COIBaseAction","isPersonHasDiscl");
        }
        return disclNum;
    }
    
    //Method to validate the Annual Disclosure Certification questions
    public ActionMessages validateAnnualDisclosure(HttpServletRequest request,
    ActionMapping actionMapping,DynaValidatorForm dynaValidatorForm)throws Exception{
        HttpSession session = request.getSession();        
        String[] arrQuestionIDs = null;
        String[] arrAnswers = null;
        ActionMessages actionMessages = new ActionMessages();
        if(actionMapping.getPath().equals("/annualDisclosure")){
            arrQuestionIDs = request.getParameterValues( "hdnQuestionId" );
        }else{
            arrQuestionIDs = (String[])session.getAttribute("selectedQuestions");
            arrAnswers = (String[])session.getAttribute("selectedAnswers");
        }
        String loggedinPersonId = (String)session.getAttribute(LOGGEDINPERSONID);
        boolean isAnswerAvailable=true;
        if(actionMapping.getPath().equals("/annualDisclosure")){
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
            }//End If
        }else{
            isAnswerAvailable = true;
        }
        if(!isAnswerAvailable){
            actionMessages.add("answerRequired",
            new ActionMessage("error.addCOIDisclsoureForm.answers.requried"));
            saveMessages(session, actionMessages);
            
        }
        boolean foundNoFinEnt;
        boolean foundEntNoAnsYes;
        boolean foundEntYesAnsNo;
        HashMap certQuestionErrors = new HashMap();
        
        for(int questCnt=0; questCnt<arrQuestionIDs.length; questCnt++){
            
            String questionID = arrQuestionIDs[questCnt];
            foundNoFinEnt = false;
            foundEntNoAnsYes = false;
            foundEntYesAnsNo = false;
            String entityNumber = isDisclosureAnswerOk(request,arrQuestionIDs[questCnt],
            arrAnswers[questCnt],loggedinPersonId);            
          
            if(!entityNumber.equals("1")){
                if(entityNumber.equals("-1") && !foundNoFinEnt){
                    actionMessages.add(ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage("error.annDiscCertification.noFinEntAnsReq"));
                    saveMessages(request, actionMessages);
                    certQuestionErrors.put(questionID, entityNumber);
                    foundNoFinEnt = true;
                }
                else if(entityNumber.equals("-2")&& !foundEntNoAnsYes){
                    actionMessages.add(ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage("error.annDiscCertification.entNoAnsYes"));
                    saveMessages(request, actionMessages);
                    foundEntNoAnsYes = true;
                    certQuestionErrors.put(questionID, entityNumber);
                }
                else if(!foundEntYesAnsNo){
                    foundEntYesAnsNo = true;
                    actionMessages.add("answersIncorrect",
                    new ActionMessage("error.annDiscCertification.entYesAnsNo"));
                    saveMessages(request, actionMessages);
                    
                    HashMap hmData = new HashMap();
                    hmData.put("personId",loggedinPersonId);
                    hmData.put("questionId",questionID);
                    HashMap entitiesWithYes =
                        getEntitiesCertQuestionYes(request,hmData);
                    
                    certQuestionErrors.put(questionID, entitiesWithYes);
                }
                session.setAttribute("certQuestionErrors", certQuestionErrors);
            }
        }
        
        boolean hasActiveEntities = checkPersonHasActiveFE(request,loggedinPersonId);
        dynaValidatorForm.set("hasActiveEntities",new Boolean(hasActiveEntities));
        
        Vector annDiscCertQuestions = (Vector)session.getAttribute("ynqList");
        if(!actionMessages.isEmpty()){
            if(arrAnswers != null){
                for(int i=0; i<arrAnswers.length; i++) {
                    if(annDiscCertQuestions != null){
                        dynaValidatorForm =
                        (DynaValidatorForm)annDiscCertQuestions.elementAt(i);
                        dynaValidatorForm.set("answer",arrAnswers[i]);
                    }
                }
            }
            session.setAttribute("selectedQuestions", arrQuestionIDs);
            session.setAttribute("selectedAnswers", arrAnswers);
        }
        else {
            session.removeAttribute("certQuestionErrors");
        }
        return actionMessages;
        
    }
    
    //To check whether answer to the disclosure questions is Correct or not
    private String isDisclosureAnswerOk(HttpServletRequest request,String questionId,
    String answer,String loggedinPersonId)throws Exception{
        HashMap hmData = new HashMap();
        webTxnBean = new WebTxnBean();
        String entityNumber = "";
        HttpSession session = request.getSession();
        hmData.put("questionId",questionId);
        hmData.put("answer",answer);
        hmData.put("personId",loggedinPersonId);

        try {
            Hashtable htDisclAnsOk =
            (Hashtable)webTxnBean.getResults(request, "isDisclAnswerOk",hmData);
             entityNumber = (String)
            ((HashMap)htDisclAnsOk.get("isDisclAnswerOk")).get("entity_number");
        }catch(Exception ex) {

            UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(),ex,"dartmouth.coeuslite.coi.action.COIBaseAction","isDisclosureAnswerOk");
        }
        return entityNumber;
    }
    
    //To check whether the person has Active financial entity
    private boolean checkPersonHasActiveFE(HttpServletRequest request,
    String personId) throws Exception{
        HashMap hmData =  new HashMap();
        int hasActiveFE = 0;
        hmData.put("personId", personId);
        try {
            Hashtable htActiveFE =
            (Hashtable)webTxnBean.getResults(request, "checkPersonHasActiveFE" , hmData);
            HashMap hmActiveFE = (HashMap)htActiveFE.get("checkPersonHasActiveFE");

            hasActiveFE = Integer.parseInt(hmActiveFE.get("RetVal").toString());

        }catch(Exception ex){
            UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(),ex,"dartmouth.coeuslite.coi.action.COIBaseAction","checkPersonHasActiveFE");
        }
        return (hasActiveFE > 0);
    }
    
    private Vector getCOIDisclosureInfo(HttpServletRequest request,
                                    String disclosureNum)throws Exception{
                                        
        HashMap hmData = new HashMap();
        Vector vecDisclData = new Vector();
        hmData.put("coiDisclosureNumber", disclosureNum);
        webTxnBean = new WebTxnBean();

        try {
            Hashtable htDisclCoiData =
                     (Hashtable)webTxnBean.getResults(request,"getDisclosureInfo",hmData);
             vecDisclData = (Vector)htDisclCoiData.get("getDisclosureInfo");
        } catch(Exception ex) {
            UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(),ex,"dartmouth.coeuslite.coi.action.COIBaseAction","getCOIDisclosureInfo");
        }
        return vecDisclData;
    }
    
    private Vector getCOIDisclInfoForPerson(HttpServletRequest request,
                                    String disclosureNum)throws Exception{  
                                        
        HashMap hmData = new HashMap();
        HttpSession session = request.getSession();
        Vector vecDisclData = new Vector();

        try {
            String personId =
                (String)session.getAttribute(LOGGEDINPERSONID);
            hmData.put("personId",personId);
            Hashtable htResult =(Hashtable)webTxnBean.getResults(request,"getPersonDiscDet", hmData);
             vecDisclData = (Vector)htResult.get("getPersonDiscDet");
        } catch(Exception ex) {
            UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(),ex,"dartmouth.coeuslite.coi.action.COIBaseAction","getCOIDisclInfoForPerson");
        }
        return vecDisclData;
    }

    
    /** This method reads the xml file and gets the subheader data 
     **/
    private void getSubheaderDetails()throws Exception{
         javax.servlet.ServletContext application = getServlet().getServletConfig().getServletContext();
         Vector vecCOISubHeader ;
         ReadProtocolDetails readProtocolDetails = new ReadProtocolDetails();
         vecCOISubHeader = (Vector)application.getAttribute(SUB_HEADER);
         if(vecCOISubHeader == null || vecCOISubHeader.size()==0){
             vecCOISubHeader = readProtocolDetails.readXMLDataForSubHeader(XML_PATH);             
             application.setAttribute(SUB_HEADER,vecCOISubHeader);
         }
    }
    
    private void checkCOIPrivileges(HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        /*
         *  Commented by Geo
         *  take person and user from session
         */
        //BEGIN Block
//        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
//        String userName = userInfoBean.getUserId();        
//        PersonInfoBean personInfoBean = null;
//        UserDetailsBean userDetailsBean = new UserDetailsBean();
//        if(userInfoBean!=null){            
////            personInfoBean  = userDetailsBean.getPersonInfo(userInfoBean.getPersonId(),false);
//            personInfoBean  = userDetailsBean.getPersonInfo(userInfoBean.getUserId());
//        }
        
        UserDetailsBean userDetailsBean = new UserDetailsBean();
        PersonInfoBean personInfoBean = (PersonInfoBean)session.getAttribute(
                        SessionConstants.LOGGED_IN_PERSON);
        if(personInfoBean!=null && personInfoBean.getPersonID() != null && personInfoBean.getUserName() != null){
            //setting personal details with the session object
            // session.setAttribute(PERSON_DETAILS_REF,personInfo);
            //setting privilege of a logged in user with the session
            String userName = personInfoBean.getUserName();
            int value = userDetailsBean.getCOIPrivilege(userName);
            session.setAttribute(PRIVILEGE,""+userDetailsBean.getCOIPrivilege(userName));
           
            //setting logged in user's person id with the session
            session.setAttribute(LOGGEDINPERSONID, personInfoBean.getPersonID());
            //setting logged in user's person name with the session
            String personName = personInfoBean.getFullName();
            session.setAttribute(LOGGEDINPERSONNAME, personName);           
            //Check whether to show link for View Pending Disclosures
            if(userDetailsBean.canViewPendingDisc(userName)){
              session.setAttribute(VIEW_PENDING_DISC, VIEW_PENDING_DISC);
            }
        } 
        session.setAttribute("person", personInfoBean);
    }
    //header details starts
  private void LoadHeaderDetails(HttpServletRequest request)throws Exception{
      HttpSession session = request.getSession();
      PersonInfoBean personInfoBean = (PersonInfoBean)session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
      webTxnBean=new WebTxnBean();
      HashMap hmData = new HashMap();
      hmData.put("personId", personInfoBean.getPersonID());
      Hashtable apprvdDisclDet = (Hashtable) webTxnBean.getResults(request, "getCoiHeaderDetails", hmData);
      Vector apprvdDiscl = (Vector) apprvdDisclDet.get("getCoiHeaderDetails");
      if((apprvdDiscl!=null)&&(apprvdDiscl.size()>0)){request.setAttribute("ApprovedDisclDetViewHeader",apprvdDiscl);}
      else{request.setAttribute("ApprovedDisclDetViewHeader",new Vector());}
  }  
     //header details for selected person
  protected void LoadHeaderDetails(String personId,HttpServletRequest request)throws Exception{
      webTxnBean=new WebTxnBean();
      HashMap hmData = new HashMap();
      hmData.put("personId", personId);
      Hashtable apprvdDisclDet = (Hashtable) webTxnBean.getResults(request, "getCoiHeaderDetails", hmData);
      Vector apprvdDiscl = (Vector) apprvdDisclDet.get("getCoiHeaderDetails");
      if((apprvdDiscl!=null)&&(apprvdDiscl.size()>0)){request.setAttribute("ApprovedDisclDetViewHeader",apprvdDiscl);}
      else{request.setAttribute("ApprovedDisclDetViewHeader",new Vector());}
  }
    //header details ends
  private void getApprovedDisclosure(HttpServletRequest request) throws Exception{
       PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);       
        Vector apprvdDiscl;
        /** Gets Latest Version of the disclosure for the logged in Reporter **/
        String personId = person.getPersonID();
        WebTxnBean webTxn = new WebTxnBean();
        CoiDisclosureBean apprvdDisclosureBean=null ;
        HashMap hmData = new HashMap();
        hmData.put("personId", personId);
        Hashtable apprvdDisclDet = (Hashtable) webTxn.getResults(request, "getApprovedDisclosure", hmData);
        apprvdDiscl = (Vector) apprvdDisclDet.get("getApprovedDisclosure");
        if (apprvdDiscl != null && apprvdDiscl.size() > 0) {
            apprvdDisclosureBean = (CoiDisclosureBean) apprvdDiscl.get(0); 
            request.getSession().setAttribute("showReviewUpdate", apprvdDisclosureBean.getCoiDisclosureNumber());
        }
      
  }
  private void checkIsApprvdAnn(HttpServletRequest request) throws Exception{
        PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);  
        String personId = person.getPersonID();
        WebTxnBean webTxn = new WebTxnBean();       
        HashMap hmData = new HashMap();
        hmData.put("personId", personId);
        Hashtable projectDetailsList = (Hashtable) webTxn.getResults(request, "fnCheckCoiApprvdAnn", hmData);
        HashMap hmfinEntityList = (HashMap) projectDetailsList.get("fnCheckCoiApprvdAnn");
        if(hmfinEntityList !=null && hmfinEntityList.size()>0){
        int count = Integer.parseInt(hmfinEntityList.get("returnVal").toString());
        if(count >0){
          request.setAttribute("ApprvedAnnual","true");
        } 
        }     
}
  public int isRightAtPersonHomeUnit(String personId,String logInPersonId,HttpServletRequest request) throws IOException, Exception{
      int returnVal = 0;      
      HashMap hmData = new HashMap();
      HttpSession session = request.getSession(true);
      WebTxnBean webTxn = new WebTxnBean();
        hmData.put("personId", personId);        
        String unitNumber = null;        
        Hashtable htPersonData = (Hashtable) webTxn.getResults(request, "getPersonDetails", hmData);
        Vector personDatas = (Vector) htPersonData.get("getPersonDetails");
        if (personDatas != null && personDatas.size() > 0) {
            PersonInfoBean personInfoBean = (PersonInfoBean) personDatas.get(0);             
              unitNumber = personInfoBean.getHomeUnit();
        }  
        UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute(USER+session.getId());
        String logInUserId = userInfoBean.getUserId();       
        if(unitNumber!=null && logInUserId!=null){
            if(isUserHasRight(request,unitNumber,logInUserId)){
            returnVal = 1;
            }       
        }
      return returnVal;
  }
    private boolean isUserHasRight(HttpServletRequest request, String ownedByUnit, String createUser)throws Exception{
        boolean isRight = false;       
        HashMap hmData = new HashMap();
        hmData.put("userId" , createUser);
        hmData.put("unitNumber", ownedByUnit);
        hmData.put("rightId" ,COI_ADMIN);
        Hashtable htAddRolodexPerson =
        (Hashtable)webTxnBean.getResults(request, "isUserHasRight", hmData);
        HashMap hmAddRolodexPerson = (HashMap)htAddRolodexPerson.get("isUserHasRight");
        if(hmAddRolodexPerson !=null && hmAddRolodexPerson.size()>0){
            int canView = Integer.parseInt(hmAddRolodexPerson.get("retValue").toString());
            if(canView == 1){
                isRight = true ;
            }
        }
        return isRight;
    }
    // new update for mit COIBaseAction
    
        private void setCoiInfoDetails(HttpServletRequest request)throws IOException, CoeusException, DBException, Exception {
        CoiInfoBean coiInfoBean = (CoiInfoBean)request.getSession().getAttribute("CoiInfoBean");
        if(coiInfoBean==null){
            coiInfoBean =new CoiInfoBean();
        } 
        String disclosureNumber =  coiInfoBean.getDisclosureNumber();
        CoiDisclosureBean apprvdDisclosureBean=null ;
        WebTxnBean webTxn = new WebTxnBean();
        Vector apprvdDiscl;
        HashMap hmData = new HashMap();
        hmData.put("personId", coiInfoBean.getPersonId());
        Hashtable apprvdDisclDet = (Hashtable) webTxn.getResults(request, "getApprvdDisclForCoihome", hmData);
        apprvdDiscl = (Vector) apprvdDisclDet.get("getApprvdDisclForCoihome");
        if (apprvdDiscl != null && apprvdDiscl.size() > 0) {
            apprvdDisclosureBean = (CoiDisclosureBean) apprvdDiscl.get(0);
            coiInfoBean.setApprovedSequence(apprvdDisclosureBean.getSequenceNumber());
            request.setAttribute("apprvdDisclosureBean", apprvdDisclosureBean);
            request.getSession().setAttribute("disclosureBeanSession", apprvdDisclosureBean);
        }
        if(disclosureNumber!=null){
        int maxSeq =  getMaxCOISeqNumber(request,disclosureNumber);
     //   coiInfoBean.setSequenceNumber(maxSeq);
        }  
        
        }
        public int getMaxCOISeqNumber(HttpServletRequest request, String disclosureNumber) throws Exception {
        int seq = 0;
        HashMap hmData = new HashMap();
        hmData.put("disclosureNumber", disclosureNumber);
        WebTxnBean webTxn = new WebTxnBean();        
        Hashtable htSyncData = (Hashtable) webTxn.getResults(request, "getMaxDisclSeqNumberCoiv2", hmData);
        HashMap hmSeq = (HashMap) htSyncData.get("getMaxDisclSeqNumberCoiv2");
        if(hmSeq.get("ll_Max") != null){
            seq = Integer.parseInt(hmSeq.get("ll_Max").toString());
        }
        return seq;
    }
        public void getCoiPersonDetails(String personId,HttpServletRequest request) throws Exception{
            HashMap hmData1 = new HashMap();
            hmData1.put("personId", personId);
            WebTxnBean webTxn1 = new WebTxnBean();
            Hashtable htPersonData = (Hashtable) webTxn1.getResults(request, "getPersonDetails", hmData1);
            Vector personDatas = (Vector) htPersonData.get("getPersonDetails");
                if (personDatas != null && personDatas.size() > 0) {
                    PersonInfoBean personInfoBean = (PersonInfoBean) personDatas.get(0);           
                      request.setAttribute("PersonDetails", personDatas);
                    request.getSession().setAttribute("person", personInfoBean);
                }
            }
        public String getCoiQuestionnaire(CoiInfoBean coiInfoBean, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        PersonInfoBean personInfoBean = (PersonInfoBean) session.getAttribute(
                SessionConstants.LOGGED_IN_PERSON);        
        QuestionnaireTxnBean questionnaireTxnBean = new QuestionnaireTxnBean(personInfoBean.getUserId());
        session.setAttribute("actionFrom", "ANN_DISCL");
        request.setAttribute("actionFrom", "ANN_DISCL");
        String valid=null;
        Map hmQuestData = new HashMap();
        String projType = coiInfoBean.getProjectType();
        Integer seqNum = coiInfoBean.getSequenceNumber();
        hmQuestData.put("module_sub_item_key", seqNum);
         if (projType != null && !projType.equals("")) {
             if (projType.equalsIgnoreCase(PROPOSAL)) {
               hmQuestData.put("as_module_item_code",ModuleConstants.ANNUAL_COI_DISCLOSURE);
               hmQuestData.put("as_module_sub_item_code",CoiConstants.COISUBMODULE_PROPOSAL_ITEMCODE);
               String proposalNo=(String)session.getAttribute("checkedproposal");
               hmQuestData.put("module_item_key", proposalNo);
         }
             if (projType.equalsIgnoreCase(PROTOCOL)) {
               hmQuestData.put("as_module_item_code",ModuleConstants.ANNUAL_COI_DISCLOSURE);
               hmQuestData.put("as_module_sub_item_code",CoiConstants.COISUBMODULE_PROTOCOL_ITEMCODE);
               String protocolNo=(String)session.getAttribute("checkedprotocolno");
               hmQuestData.put("module_item_key", protocolNo);
         }
              if (projType.equalsIgnoreCase(IACUCPROTOCOL)) {
               hmQuestData.put("as_module_item_code",ModuleConstants.ANNUAL_COI_DISCLOSURE);
               hmQuestData.put("as_module_sub_item_code",CoiConstants.COISUBMODULE_IACUC_PROTOCOL_ITEMCODE);
               String iacucprotocolNo=(String)session.getAttribute("checkediacucprotocolno");
               hmQuestData.put("module_item_key", iacucprotocolNo);
         }
             if (projType.equalsIgnoreCase(ANNUAL)) {
               hmQuestData.put("as_module_item_code",ModuleConstants.ANNUAL_COI_DISCLOSURE);
               hmQuestData.put("as_module_sub_item_code",CoiConstants.COISUBMODULE_ANNUAL_ITEMCODE);
         }
             if (projType.equalsIgnoreCase(AWARD)) {
             hmQuestData.put("as_module_item_code",ModuleConstants.ANNUAL_COI_DISCLOSURE);
               hmQuestData.put("as_module_sub_item_code",CoiConstants.COISUBMODULE_AWARD_ITEMCODE);
               String awardNo=(String)session.getAttribute("checkedawardno");
               hmQuestData.put("module_item_key", awardNo);
             }
             if (projType.equalsIgnoreCase(REVISION)) {
               hmQuestData.put("as_module_item_code",ModuleConstants.ANNUAL_COI_DISCLOSURE);
               hmQuestData.put("as_module_sub_item_code",CoiConstants.COISUBMODULE_REVISION_ITEMCODE);              
             }
               if (projType.equalsIgnoreCase(TRAVEL)) {
               hmQuestData.put("as_module_item_code",ModuleConstants.ANNUAL_COI_DISCLOSURE);
               hmQuestData.put("as_module_sub_item_code",CoiConstants.COISUBMODULE_TRAVEL_ITEMCODE);
               String travelNo=(String)session.getAttribute("checkedtravelno");
               hmQuestData.put("module_item_key", travelNo);
             }                             
         }

         QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
         questionnaireModuleObject.setModuleItemCode(Integer.parseInt(hmQuestData.get("as_module_item_code").toString()));
         questionnaireModuleObject.setModuleSubItemCode(Integer.parseInt(hmQuestData.get("as_module_sub_item_code").toString()));
         if(hmQuestData.get("module_item_key") != null) {
            questionnaireModuleObject.setModuleItemKey(hmQuestData.get("module_item_key").toString());
         }
         questionnaireModuleObject.setModuleSubItemKey(hmQuestData.get("module_sub_item_key").toString());
         questionnaireModuleObject.setCurrentPersonId(personInfoBean.getPersonID());
         
         CoeusVector qstnVector =  questionnaireTxnBean.fetchApplicableQuestionnairedForModule(questionnaireModuleObject);
         Vector qstnnrIdList = new Vector();
         if(qstnVector != null && qstnVector.size() > 0) {
             valid = "valid";
             for(int i=0; i < qstnVector.size(); i++){
                 QuestionnaireAnswerHeaderBean qnrAnsHeadBean = (QuestionnaireAnswerHeaderBean)qstnVector.get(i);
                 int qstnnrId = qnrAnsHeadBean.getQuestionnaireId();
                 request.setAttribute("questionnaireId",qstnnrId);
                 qstnnrIdList.add(qstnnrId);
             }
         }           
         session.setAttribute("qstnnrIdList", qstnnrIdList);
        request.setAttribute("MenuId", "ANN_DISCL");
        request.setAttribute("questionaireLabel", "Annual Disclosure Certification");
       return valid;
    }      
  protected void coiMenuDataSaved(String disclosureNumber,Integer sequenceNumber,String personId,HttpServletRequest request) throws DBException, IOException, CoeusException, Exception{
//coi menu data saved start
       CoiInfoBean coiInfoBean = (CoiInfoBean)request.getSession().getAttribute("CoiInfoBean");
        if(coiInfoBean==null){
            coiInfoBean =new CoiInfoBean();
        }
      try {
                    CoiMenuBean coiMenuBean = new CoiMenuBean();
                    HashMap hmData = new HashMap();
                    WebTxnBean webTxn = new WebTxnBean();
                    Vector questionDet = null;
                    HashMap hasRightMap = new HashMap();
                    Integer hasDisclosure = 0;
                    Vector finEntityListNew=new Vector();
      //travel Menu
                    coiMenuBean.setTravelDataSaved(true);
     //questionnaire Menu
            hmData.put("coiDisclosureNumber", disclosureNumber);
            hmData.put("sequenceNumber", sequenceNumber);
            hmData.put("personId", personId);
            Hashtable questionData = (Hashtable) webTxn.getResults(request, "getQnsAns", hmData);
            questionDet = (Vector) questionData.get("getQnsAns");
                    if (questionDet != null && questionDet.size() > 0) {
                        coiMenuBean.setQuestDataSaved(true);
                    }
                    else {
                        coiMenuBean.setQuestDataSaved(false);
                    }
    //financial Entity Menu
             hmData = new HashMap();
             hmData.put("coiDisclosureNumber", disclosureNumber);
             hmData.put("sequenceNumber", sequenceNumber);
             Hashtable projectDetailsList = (Hashtable) webTxn.getResults(request, "fnGetFinEntDiscSeq", hmData);
             HashMap hmfinEntityList = (HashMap) projectDetailsList.get("fnGetFinEntDiscSeq");
            if(hmfinEntityList !=null && hmfinEntityList.size()>0){
            int count = Integer.parseInt(hmfinEntityList.get("count").toString());
            if(count >0){
                 coiMenuBean.setFinEntDataSaved(true);
            }else{
                coiMenuBean.setFinEntDataSaved(false);
            }
        }                
             
    // By Project
          if(request.getAttribute("byProjectMenu")!=null){
                int projectCount= 1;               
                if(coiInfoBean.getEventType()==5 ||coiInfoBean.getEventType()==6){
                   projectCount=coiInfoBean.getProjectCount();
                 }                          
                hmData.put("disclosureNumber", disclosureNumber);
                hmData.put("sequenceNumber", sequenceNumber);   
                hmData.put("personId", personId);  
                hmData.put("projectCount", projectCount);
                projectDetailsList = (Hashtable) webTxn.getResults(request, "fnIsSavedCoiDiscDet", hmData);
                hmfinEntityList = (HashMap) projectDetailsList.get("fnIsSavedCoiDiscDet");
                if(hmfinEntityList !=null && hmfinEntityList.size()>0){
                int count = Integer.parseInt(hmfinEntityList.get("li_return").toString());
                if(count ==0){
                  coiMenuBean.setPrjDataSaved(false);
                }
                else{
                  coiMenuBean.setPrjDataSaved(true);   
                }
                }
                  
                   }else{    
            
                     hmData = new HashMap();
                    hmData.put("userId", personId);
                    Hashtable hasRightHashtable = (Hashtable) webTxn.getResults(request, "checkDisclosureAvailable", hmData);
                    hasRightMap = (HashMap) hasRightHashtable.get("checkDisclosureAvailable");
                    if (hasRightMap != null && hasRightMap.size() > 0) {
                        hasDisclosure = Integer.parseInt((String) hasRightMap.get("disclosureAvailable"));
                    }
                    if (hasDisclosure > 0) {
                     coiMenuBean.setPrjDataSaved(true);
                    }
                    else{
                      coiMenuBean.setPrjDataSaved(false);
                    }
                    
          }             
    // notes
         if(coiInfoBean!=null &&  coiInfoBean.getMenuType()!=null && coiInfoBean.getMenuType().equalsIgnoreCase("ViewCurrent")){
             hmData.put("coiDisclosureNumber",disclosureNumber);
             Hashtable disclNotes = (Hashtable) webTxnBean.getResults(request, "getApprvdDisclNotesCoiv2", hmData);
             Vector notes = (Vector) disclNotes.get("getApprvdDisclNotesCoiv2");
              if (notes!=null && notes.size()>0 ){
                        coiMenuBean.setNoteDataSaved(true);
                    }
                    else{
                         coiMenuBean.setNoteDataSaved(false);
                    }
         }else{
            hmData = new HashMap();
            hmData.put("coiDisclosureNumber", disclosureNumber);
            hmData.put("coiSequenceNumber", sequenceNumber);
            Hashtable discNotesList = (Hashtable) webTxn.getResults(request, "getDisclosureNotesCoiv2", hmData);
            Vector notes = (Vector) discNotesList.get("getDisclosureNotesCoiv2");
                    if ( notes!=null && notes.size()>0 ){
                        coiMenuBean.setNoteDataSaved(true);
                    }
                    else{
                         coiMenuBean.setNoteDataSaved(false);
                    }
         }
                  
                    
    //attachemnt
         if(coiInfoBean!=null && coiInfoBean.getMenuType()!=null && coiInfoBean.getMenuType().equalsIgnoreCase("ViewCurrent")){
               Vector approvedAttDet=new Vector();            
               HashMap hmp=new HashMap();
               hmp.put("disclosureNumber",disclosureNumber);
               hmp.put("sequenceNumber",sequenceNumber);
               Hashtable approvedDisclosureAttachments = (Hashtable) webTxn.getResults(request, "getApprovedDisclDocument", hmp);
               approvedAttDet = (Vector) approvedDisclosureAttachments.get("getApprovedDisclDocument");            
               if(approvedAttDet!=null && !approvedAttDet.isEmpty()){
                  coiMenuBean.setAttachDataSaved(true);
               }else{
                  coiMenuBean.setAttachDataSaved(false); 
               } 
         }else{
             CoiAttachmentService coiAttachmentService = CoiAttachmentService.getInstance();
             Vector attDet = coiAttachmentService.getUploadDocumentForPerson(disclosureNumber, sequenceNumber, personId);
                   if ( attDet!=null && attDet.size()>0 ){
                        coiMenuBean.setAttachDataSaved(true);
                    }
                    else{
                         coiMenuBean.setAttachDataSaved(false);
                    }
         }
         
        
              
                   
     // certification
        HttpSession session=request.getSession();       
        hmData = new HashMap();        
        hmData.put("coiDisclosureNumber", disclosureNumber);
        hmData.put("sequenceNumber", sequenceNumber);
        hmData.put("personId", personId);
        Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData);
        Vector DisclDet = (Vector) DisclData.get("getDisclBySequnce");
                    if (DisclDet != null && DisclDet.size() > 0) {
                       // request.setAttribute("ApprovedDisclDetView", DisclDet);
                        for (Iterator it = DisclDet.iterator(); it.hasNext();) {
                            CoiDisclosureBean object = (CoiDisclosureBean) it.next();
                            if(object.getCertificationTimestamp()!=null){
                                 coiMenuBean.setCertDataSaved(true);
                            }else{
                                coiMenuBean.setCertDataSaved(false);
                            }
                        }
                    }
        
                    else{
                    coiMenuBean.setCertDataSaved(false);
                    }
   // Assign Viewer
             hmData = new HashMap();
             Vector userDet = new Vector();
             hmData.put("coiDisclNum", disclosureNumber);
             hmData.put("seqNumber", sequenceNumber);
            Hashtable users = (Hashtable) webTxn.getResults(request, "getCoiUserRoles", hmData);
            userDet = (Vector) users.get("getCoiUserRoles");
            if(userDet!=null && userDet.size()>0)
            {
             coiMenuBean.setAssignViewerDataSaved(true);
            }
            else{
             coiMenuBean.setAssignViewerDataSaved(false);
            }
            
    // review Complete
              PersonInfoBean personInfoBean = (PersonInfoBean)session.getAttribute(SessionConstants.LOGGED_IN_PERSON);            
              HashMap hmMap = new HashMap();
              hmMap.put("disclosureNumber", disclosureNumber);
              hmMap.put("seqNumber", sequenceNumber);
              hmMap.put("personId", personInfoBean.getPersonID());
            Hashtable htResult =(Hashtable)webTxn.getResults(request,"isCOIPersonReviewComplete",hmMap);
            HashMap  hmResult = (HashMap)htResult.get("isCOIPersonReviewComplete");
            int isReviewCompleted = Integer.parseInt(hmResult.get("isComplete").toString());
            if(isReviewCompleted == 1) {  
                coiMenuBean.setReviewCompleteSaved(true);
            }else{
                coiMenuBean.setReviewCompleteSaved(false);
            }
     request.getSession().setAttribute("coiMenuDatasaved",coiMenuBean);
     }
     catch (Exception ex) {
         UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(),ex,"dartmouth.coeuslite.coi.action.COIBaseAction","coiMenuDataSaved");
         request.removeAttribute("coiMenuDatasaved");
     }
 //coi menu data saved ends
  }
     private void checkAnnualReviewDue(HttpServletRequest request){
        try {
            String parameterName = "COI_ANNUAL_REVIEW_DUE";
            Hashtable param_value = new Hashtable();
            HashMap hmCertInv = new HashMap();
            HashMap hmMap = new HashMap();
            WebTxnBean webTxnBean = new WebTxnBean();
            Date expirationDate = null;
            hmMap.put("parameterName", parameterName);
            param_value = (Hashtable) webTxnBean.getResults(request, "getParameterValue", hmMap);
            hmCertInv = (HashMap) param_value.get("getParameterValue");
            String parameterValue = (String) hmCertInv.get(("parameterValue").toString());
            boolean isValid = false;
            if(parameterValue != null && parameterValue.trim().length() != 0 && !parameterValue.equals("0")) {
                Matcher matcher;
                Pattern pattrn = Pattern.compile("\\d{1,2}\\-\\d{1,2}\\-\\d{2,4}");
                Pattern pattern = Pattern.compile("\\d{1,2}\\-\\d{1,2}");
                matcher = pattrn.matcher(parameterValue);
                isValid = matcher.matches();

                if(!isValid) {
                   matcher = pattern.matcher(parameterValue);
                   isValid = matcher.matches();
                   
                   if(isValid) {
                       Calendar cal=Calendar.getInstance();
                       Date currentDate =  cal.getTime();
                       String currentYear = "";

                       String[] splitList = currentDate.toString().split("\\s");

                       if(splitList != null && splitList.length > 0) {
                           currentYear = splitList[splitList.length - 1];
                           parameterValue = parameterValue + "-" + currentYear;
                       }
                   }
                }

                if(!isValid) {
                  parameterValue = null;
                }
            }
            if(isValid){
            request.getSession().setAttribute("AnnualDueDate", parameterValue);
            }
            CoiCommonService coiCommonService = CoiCommonService.getInstance();
             CoiDisclosureBean discl = coiCommonService.getLatestApprvdDisclPerson(request);
              if (parameterValue != null && isValid) {
                DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
                Date date1 = new Date();
                try{
                    date1 = (Date) formatter.parse(parameterValue);
                     Calendar cal=Calendar.getInstance();
                    cal.setTime(date1);
                    cal.add(Calendar.MONTH, 12);
                    expirationDate = cal.getTime();
                    //Date date1 =new Date(parameterValue);
                } catch (ParseException ex)
                {
                    Logger.getLogger(COIBaseAction.class.getName()).log(Level.SEVERE, null, ex);
                }
                //Date date1 =new Date(parameterValue);
                //Date date1 =new Date(parameterValue);
                Calendar currentDate = Calendar.getInstance();
                SimpleDateFormat formatter1 = new SimpleDateFormat("MM-dd-yyyy");
                String dateNow = formatter1.format(currentDate.getTime());
                Date date2 = new Date();
                try
                {
                    date2 = (Date) formatter1.parse(dateNow);
                } catch (ParseException ex)
                {
                    Logger.getLogger(COIBaseAction.class.getName()).log(Level.SEVERE, null, ex);
                }
                boolean results = date1.before(date2);

                if(results == false) {
                    results = date1.equals(date2);
                }
                // boolean results1 = date1.equals(date2);
                if (results == true){
                     if(discl != null){
                        String expDate = discl.getExpDateFormated();
                        if(expDate != null) {
                            SimpleDateFormat smplformatter = new SimpleDateFormat("MM-dd-yyyy");
                            Date frmtDate = smplformatter.parse(expDate.toString());
                            boolean result1 = frmtDate.equals(expirationDate);

                            if(!result1) {
                                result1 = frmtDate.after(expirationDate);
                            }
                            if(!result1){
                                 request.getSession().setAttribute("isReviewDue", true);
                            }else {
                                 String expiredateFormated = formatter1.format(expirationDate);
                             request.getSession().setAttribute("AnnualDueDate", expiredateFormated);
                                 request.getSession().setAttribute("isReviewDue", false);
                            }
                        }else {
                             request.getSession().setAttribute("isReviewDue", true);
                             String expiredateFormated = formatter1.format(expirationDate);
                             request.getSession().setAttribute("AnnualDueDate", expiredateFormated);
                        }
                     } else {
                         request.getSession().setAttribute("isReviewDue", true);
                     }
                    } else
                {
                    request.getSession().setAttribute("isReviewDue", false);
                    String expiredateFormated = formatter1.format(expirationDate);
                    request.getSession().setAttribute("AnnualDueDate", expiredateFormated);
                }
              }
              else{                
                if(discl != null){
                    boolean results = false;
                    String expDate = discl.getExpDateFormated();
                    Calendar currentDate = Calendar.getInstance();
                    SimpleDateFormat formatter1 = new SimpleDateFormat("MM-dd-yyyy");
                    String dateNow = formatter1.format(currentDate.getTime());
                    Date date2 = new Date();
                    date2 = (Date) formatter1.parse(dateNow);
                    if(expDate!=null){
                    Date frmtDate = formatter1.parse(expDate.toString());                              
                    results = frmtDate.before(date2);
                    }
                    if (results == true){
                        request.getSession().setAttribute("isReviewDue", true);
                        request.getSession().setAttribute("AnnualDueDate", dateNow);
                    } else{
                        request.getSession().setAttribute("isReviewDue", false);
                        request.getSession().setAttribute("AnnualDueDate", expDate);
                    }
                }else{
                      request.getSession().setAttribute("isReviewDue", false);
                      request.getSession().setAttribute("AnnualDueDate", null);
                    }
              }
         } catch (IOException ex) {
            Logger.getLogger(COIBaseAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CoeusException ex) {
            Logger.getLogger(COIBaseAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DBException ex) {
            Logger.getLogger(COIBaseAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(COIBaseAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        }

        private void checkAssignedDisclosurePresent(HttpServletRequest request) {
            WebTxnBean webTxn = new WebTxnBean();
            Vector historyDet =new Vector();
            HashMap hmData = new HashMap();
            HttpSession session = request.getSession();
            PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
            String personId = person.getUserName();
            hmData.put("personId", personId);
            Vector entityName = new Vector();
            try {
                Hashtable historyData = (Hashtable) webTxn.getResults(request, "getAssignedDisc", hmData);
                historyDet = (Vector) historyData.get("getAssignedDisc");

                if(historyDet != null && historyDet.size() > 0) {
                    session.setAttribute("isAssignedDisclPresent", true);
                }
                else {
                    session.setAttribute("isAssignedDisclPresent", false);
                }
            } catch (Exception e) {
                UtilFactory.log(e.getMessage(), e, "CoiV2ViewerAction", "performExecuteCOI()");

            }
        }    
    // new update for mit COIBaseAction
}