/*
 * COIBaseAction.java
 *
 * Created on December 26, 2005, 3:37 PM
 */

package edu.mit.coeuslite.coi.action;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeuslite.irb.bean.ReadProtocolDetails;
import edu.mit.coeuslite.utils.CoeusBaseAction;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
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

/**
 *
 * @author  vinayks
 */
public abstract class COIBaseAction extends CoeusBaseAction{
    private WebTxnBean webTxnBean ;
    public static final String EMPTY_STRING = "";
    public static final String LOGGEDINPERSONID = "loggedinpersonid";
    private static final String SUB_HEADER = "headerVector";
    private static final String XML_PATH = "/edu/mit/coeuslite/coi/xml/COISubMenu.xml";
    public static final String LOGGEDINPERSONNAME = "loggedinpersonname";
    public static final String VIEW_PENDING_DISC = "viewPendingDisc";
    public static final String PRIVILEGE = "userprivilege";

    private static final String COI_SESSION_INITIALIZED = "COI_SESSION_INITIALIZED";

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
        Boolean coiSessionInit = (Boolean)coiSession.getAttribute(COI_SESSION_INITIALIZED);
        if(coiSessionInit==null || !coiSessionInit.booleanValue()){
            checkCOIPrivileges(request);
            getSubheaderDetails();
            coiSession.setAttribute(COI_SESSION_INITIALIZED, Boolean.TRUE);
        }
        if(actionMapping.getPath().equals("/getReviewAnnualDiscl") || actionMapping.getPath().equals("/getApproveDisc") ||
                actionMapping.getPath().equals("/getWip") || actionMapping.getPath().equals("/getWipDisclosures")) {
            if(!hasRightToViewEditWIP(request)){
                CoeusException ex = new CoeusException("Sorry you have no previlege to view/edit.");
                request.setAttribute("Exception", ex);
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

        String userprivilege = (String)session.getAttribute("userprivilege");

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
            (String)session.getAttribute("loggedinpersonid");
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
            //JIRA COEUSDEV-246 - START
            String disclStatCode = (String)dynaValidatorForm.get("coiStatusCode");
            if(disclStatCode == null || disclStatCode.trim().length() == 0) {
                disclStatCode = request.getParameter("disclStatCode");
            }
            if (disclStatCode != null) {
                int intDisclStatCode = Integer.parseInt(disclStatCode);
                if (intDisclStatCode >= 200 && intDisclStatCode < 299) {
                    //Check if all status is >= 200 and < 299
                    int selectedStatus;
                    for (int arrIndex = 0; arrIndex < arrLength; arrIndex++) {
                        arrConflictStatus[arrIndex] = request.getParameter(("sltConflictStatus" + arrIndex));
                        if (arrConflictStatus[arrIndex] != null) {
                            selectedStatus = Integer.parseInt(arrConflictStatus[arrIndex]);
                            if (!(selectedStatus >= 200 && selectedStatus < 299)) {
                                isDisclStatusOK = false;
                                break;
                            }
                        }//end if
                    }//end for
                }
            }
            //JIRA COEUSDEV-246 - END

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

        //Case 3513 - Proposal Title shouldn't be more than 150 chars
        String title = (String)dynaValidatorForm.get("title");
        if(title != null && title.trim().length() > 150) {
            actionMessages.add("proposalTitleLength", new ActionMessage("error.addCOIDisclosureForm.proposalTile.length"));
        }

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
        return hmEntitiesWithYes;
    }

    /*To check whether the person has disclosure  */
    public String isPersonHasDiscl(HttpServletRequest request,
    DynaValidatorForm dynaValidatorForm)throws Exception{
        HashMap hmData = new HashMap();
        String appliesToCode =(String)dynaValidatorForm.get("appliesToCode");
        String disclosureTypeCode =(String)dynaValidatorForm.get("disclosureTypeCode");
        String personId = (String)dynaValidatorForm.get("personId");
        hmData.put("personId",personId);
        hmData.put("appliesToCode", appliesToCode);
        hmData.put("disclosureTypeCode",disclosureTypeCode);
        webTxnBean =  new WebTxnBean();
        Hashtable htResult = (Hashtable)webTxnBean.getResults(request, "checkPersonHasDisc", hmData);
        String disclNum = (String)((HashMap)htResult.get("checkPersonHasDisc")).get("ls_disc_num");
        if(disclNum == null){
            disclNum = EMPTY_STRING;
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
        String loggedinPersonId = (String)session.getAttribute("loggedinpersonid");
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
        HttpSession session = request.getSession();
        hmData.put("questionId",questionId);
        hmData.put("answer",answer);
        hmData.put("personId",loggedinPersonId);
        Hashtable htDisclAnsOk =
        (Hashtable)webTxnBean.getResults(request, "isDisclAnswerOk",hmData);
        String entityNumber = (String)
        ((HashMap)htDisclAnsOk.get("isDisclAnswerOk")).get("entity_number");
        return entityNumber;
    }

    //To check whether the person has Active financial entity
    private boolean checkPersonHasActiveFE(HttpServletRequest request,
    String personId) throws Exception{
        HashMap hmData =  new HashMap();
        hmData.put("personId", personId);
        Hashtable htActiveFE =
        (Hashtable)webTxnBean.getResults(request, "checkPersonHasActiveFE" , hmData);
        HashMap hmActiveFE = (HashMap)htActiveFE.get("checkPersonHasActiveFE");

        int hasActiveFE = Integer.parseInt(hmActiveFE.get("RetVal").toString());

        return (hasActiveFE > 0);
    }

    private Vector getCOIDisclosureInfo(HttpServletRequest request,
                                    String disclosureNum)throws Exception{

        HashMap hmData = new HashMap();
        hmData.put("coiDisclosureNumber", disclosureNum);
        webTxnBean = new WebTxnBean();
        Hashtable htDisclCoiData =
                 (Hashtable)webTxnBean.getResults(request,"getDisclosureInfo",hmData);
        Vector vecDisclData = (Vector)htDisclCoiData.get("getDisclosureInfo");
        return vecDisclData;
    }

    private Vector getCOIDisclInfoForPerson(HttpServletRequest request,
                                    String disclosureNum)throws Exception{

        HashMap hmData = new HashMap();
        HttpSession session = request.getSession();
        String personId =
            (String)session.getAttribute("loggedinpersonid");
        hmData.put("personId",personId);
        Hashtable htResult =(Hashtable)webTxnBean.getResults(request,"getPersonDiscDet", hmData);
        Vector vecDisclData = (Vector)htResult.get("getPersonDiscDet");
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

    protected Vector getCOIDisclosureCreate(HttpServletRequest request, String disclnumber) throws Exception {
        HashMap hmData = new HashMap();
        HttpSession session = request.getSession();
        hmData.put("coiDisclosureNumber", disclnumber);
        if(webTxnBean == null)webTxnBean = new WebTxnBean();
        Hashtable htResult = (Hashtable) webTxnBean.getResults(request, "getInvCoiDiscCreate", hmData);
        Vector vecDisclData = (Vector) htResult.get("getInvCoiDiscCreate");
        return vecDisclData;
    }

    //Case 4321 - START
    //Value already in session from COIAction
   private boolean hasRightToViewEditWIP(HttpServletRequest request) {
        boolean hasRight = false;
        String privilege = (String)request.getSession().getAttribute(COIAction.PRIVILEGE);
        if(privilege != null){
            int val = Integer.parseInt(privilege);
            if(val == 2){
                hasRight = true;
            }//
        }//
        return hasRight;
    }//Case 4321 - END

}