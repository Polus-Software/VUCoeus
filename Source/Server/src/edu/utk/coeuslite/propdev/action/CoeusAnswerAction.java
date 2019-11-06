package edu.utk.coeuslite.propdev.action;
import org.apache.struts.action.*;
import org.apache.struts.validator.DynaValidatorForm;
import org.apache.commons.beanutils.DynaBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import edu.mit.coeuslite.utils.*;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.utils.TypeConstants;
import java.util.*;
import edu.mit.coeus.questionnaire.utils.QuestionnaireBuilder;

/**
 * This action class handles the request and response for Questionnaire answers.
 */

public class CoeusAnswerAction extends ProposalBaseAction {

    private final String GET_NEXT_QUESTION = "/getNextQuestions";
    private  final String SAVE_QUESTIONNAIRE_DATA = "/saveQuestionnaireData";
    private  final String SUCCESS = "success";
    private  final int MODULE_ITEM_CODE_VALUE = 3 ;
    private  final String MODULE_ITEM_CODE = "moduleItemCode" ;
    private  final int MODULE_SUB_ITEM_CODE_VALUE = 0 ;
    private  final String MODULE_SUB_ITEM_CODE = "moduleSubItemCode" ;
    private  final String EMPTY_STRING = "";
    private  final String ANSWER_NUMBER_VALUE = "1" ;
    private  final String DATE_SEPARATERS = ":/.,|-";
    private  final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private  final String NUMBER_DATA_TYPE = "NUMBER" ;
    private  final String DATE_DATA_TYPE = "DATE" ;
    private  final String NEXT = "Next";
    private  final String PREV = "Prev";
    private  final String IMMEDIATE_CHILD_LEVEL="3";
    private  final String ROOT_LEVEL="2";
    // error messages constants
    private  final String NOT_VALID_DATE = "customElements.notValidDate" ;
    private  final String NUMBER_FORMAT_EXCEPTION = "customElements.numberFormatException" ;
    //statements
    private  final String GET_QUESTIONNAIRE_QUESTIONS       = "getQuestionnaireQuestions";
    private  final String GET_QUESTIONNAIRE_ANSWERS         = "getQuestionnaireAnswers" ;
    private  final String GET_QUESTIONNAIRE_ANS_HEADER      = "getQuestionnaireAnsHeader" ;
    private  final String ADD_UPD_QUESTIONNAIRE_ANSWERS     = "addUpdQuestionnaireAnswers" ;
    private  final String ADD_UPD_QUESTIONNAIRE_ANS_HEADER  = "addUpdQuestionnaireAnswerHeader" ;
    private  final String DEL_QUESTIONNAIRE_ANSWERS         = "deleteQuestionnaireAnswers";
    private  final String UPD_QUESTIONNAIRE_COMPLETED_FLAG  = "updateQuestionnaireCompletedFlag";
    private  String userID                                  = "";


    /**This Method get/add/update the Questionnaire and Routes to the appropriate JSP
     * @param actionMapping
     * @param actionForm
     * @param request
     * @param response
     * @throws Exception
     * @return ActionForward object
     */
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception {
        String navigator = "success";
        HttpSession session         = request.getSession();
        String proposalNumber       = (String)session.getAttribute("proposalNumber"+session.getId());
        WebTxnBean webTxnBean       = new WebTxnBean();
        HashMap hmQuestionnaireData = null ;
        TreeMap tMapPageNumbers     = new TreeMap();
        Vector vecQuestionnaire     = new Vector();
        String strClickType         = isNull(request.getParameter("click"));
        String strPageNumber        = isNull(request.getParameter("pageno"));
        String strSavePage          = isNull(request.getParameter("savePage"));
        boolean boolSaveFlag        = true;

        TreeMap tmapQuestionnaire   = (TreeMap)session.getAttribute("QuestionnaireTree");

        if (strPageNumber.equals(ROOT_LEVEL) ){
            tMapPageNumbers = tmapQuestionnaire;
        }
        else{
            tMapPageNumbers         = (TreeMap)session.getAttribute("QuestionsMap");
        }
        if(strPageNumber.length() != 0 && strSavePage.length() != 0){
            Integer intPageNo           = new Integer(Integer.parseInt(strPageNumber));
            Integer intSavePage         = new Integer(Integer.parseInt(strSavePage));
            if (strClickType.equals(PREV)){
                Vector vecQuestionnaireQuestion = (Vector)session.getAttribute("QuestionnaireQuestions");
                tMapPageNumbers                 = (TreeMap)session.getAttribute("QuestionsMap");
                QuestionnaireBuilder qBuilder   = new QuestionnaireBuilder();
                tMapPageNumbers = qBuilder.synchronizeQuestionnaireMap(tmapQuestionnaire,tMapPageNumbers,intPageNo,vecQuestionnaireQuestion);
            }
            CoeusDynaBeansList coeusDynaBeanList = (CoeusDynaBeansList)actionForm ;
            if(request.getParameter("actionFrom")!=null && !request.getParameter("actionFrom").equals(EMPTY_STRING)){
                session.setAttribute("actionFrom" , request.getParameter("actionFrom"));
            }
            if(request.getParameter("menuId")!=null && !request.getParameter("menuId").equals(EMPTY_STRING)){
                session.setAttribute("menuCode",request.getParameter("menuId"));
            }

            String actionFrom = (String)session.getAttribute("actionFrom");
            if(actionFrom!=null && !actionFrom.equals(EMPTY_STRING)){
                Map mapMenuList = new HashMap();
                hmQuestionnaireData = new HashMap();
                if(actionFrom.equals("DEV_PROPOSAL")){
                    hmQuestionnaireData.put("moduleItemCode", new Integer(3));
                    hmQuestionnaireData.put("moduleItemKey",(String)session.getAttribute("proposalNumber"+session.getId()));
                    hmQuestionnaireData.put("moduleSubItemCode", new Integer(0));
                    hmQuestionnaireData.put("moduleSubItemKey", (String)session.getAttribute("proposalNumber"+session.getId()));
                    hmQuestionnaireData.put("questionaireCompletionId" ,(String)session.getAttribute("proposalNumber"+session.getId()));
                    mapMenuList.put("menuItems", CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
                    mapMenuList.put("menuCode",session.getAttribute("menuCode"));
                    setSelectedMenuList(request, mapMenuList);
                }else if (actionFrom.equals("PROTOCOL")){
                    hmQuestionnaireData.put("moduleItemCode", new Integer(7));
                    hmQuestionnaireData.put("moduleItemKey",session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()));
                    hmQuestionnaireData.put("moduleSubItemCode", new Integer(0));
                    hmQuestionnaireData.put("moduleSubItemKey", session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId()));
                    hmQuestionnaireData.put("questionaireCompletionId" ,session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()));
                    mapMenuList.put("menuItems",CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
                    mapMenuList.put("menuCode",session.getAttribute("menuCode"));
                    setSelectedMenuList(request, mapMenuList);
                }
            }
            int intTmapSize     = 0;
            QuestionnaireBuilder qBuilder = new QuestionnaireBuilder();
            TreeMap tMapNextQuestions;
            if(strClickType.equals(PREV)){
                tMapNextQuestions = tMapPageNumbers;
                boolSaveFlag = false;
            }
            else{
                String strAnswer    = getAnswerFromUser(coeusDynaBeanList);
                tMapNextQuestions   = qBuilder.getNextQuestions(tMapPageNumbers,strAnswer,strPageNumber);
            }
            session.setAttribute("QuestionsMap", tMapNextQuestions);
            session.setAttribute("pageno", intPageNo);
            if(tMapNextQuestions != null){
                intTmapSize = tMapNextQuestions.size();
            }
            boolean boolPrev = (intPageNo.intValue() == 1)?false:true;
            boolean boolNext = (intPageNo.intValue() == intTmapSize)?false:true;
            session.setAttribute("previous", new Boolean(boolPrev));
            session.setAttribute("Next", new Boolean(boolNext));

            // Save Action Starts from here
            CoeusDynaBeansList coeusDynaBeanFullList = (CoeusDynaBeansList)actionForm ;
            Iterator questionIterator = tMapNextQuestions.keySet().iterator();
            HashMap hmapDynaBean = new HashMap();
            while(questionIterator.hasNext()) {
                Integer intPageNumber = (Integer)questionIterator.next();
                if (intPageNumber.intValue() <= intSavePage.intValue()){
                    hmapDynaBean.put(intPageNumber,tMapNextQuestions.get(intPageNumber));
                }
            }
            Vector vecDynaBean = qBuilder.getListFromMap(new TreeMap(hmapDynaBean));
            coeusDynaBeanFullList.setList(vecDynaBean);
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            userID = userInfoBean.getUserId();
            LockBean lockBean = null ;
            LockBean lockData = null ;
             if(actionFrom.equals("DEV_PROPOSAL")){
                  lockBean = getLockingBean(userInfoBean, (String)session.getAttribute("proposalNumber"+session.getId()), request);
                  lockData = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR+lockBean.getModuleNumber(), request);
             }else if (actionFrom.equals("PROTOCOL")){
                  lockBean = getLockingBean(userInfoBean,
                                (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()), request, null);
                  lockData = getLockedData(CoeusLiteConstants.PROTOCOL_LOCK_STR+lockBean.getModuleNumber(), request);
             }
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId()) ) {
                if(boolSaveFlag){
                    performSaveAction(hmQuestionnaireData, coeusDynaBeanFullList, request);
                }
                coeusDynaBeanList.setList((Vector)tMapNextQuestions.get(intPageNo));
                session.setAttribute("questionsList",coeusDynaBeanList);

            }else{
                String errMsg = "release_lock_for";
                ActionMessages messages = new ActionMessages();
                messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                saveMessages(request, messages);
            }
            if(coeusDynaBeanList.getList() == null){
                updateQuestionnaireCompletedFlag(hmQuestionnaireData,request, "Y");
                session.setAttribute("QuestionnaireCompleted","Y");
                String strMsg       = "questionnaire_completed_message";
                String strMsgCode   = "questionnaire_completed_code";
                ActionMessages messages = new ActionMessages();
                messages.add(strMsgCode, new ActionMessage(strMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                saveMessages(request, messages);
                if (actionFrom.equals("PROTOCOL")){
                    navigator = "protocol";
                }
                else if (actionFrom.equals("DEV_PROPOSAL")){
                    navigator = "proposal";
                }
                session.setAttribute("navigator",navigator);
            }
            else{
                session.setAttribute("QuestionnaireCompleted","N");
            }
        }
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
        mapMenuList.put("menuCode",session.getAttribute("menuCode"));
        setSelectedMenuList(request, mapMenuList);
        readSavedStatus(webTxnBean ,(String)session.getAttribute("actionFrom"), request);
        return actionMapping.findForward("success");
    }

    /**This method gets the Questionnaire Questions for a particular questionnaireId
     * @throws Exception
     * @return Vector of dynabeans
     */
    public Vector getQuestionnaireQuestions(int questionnaireId , HashMap hmQuestionnaireData,
                                            HttpServletRequest request, CoeusDynaBeansList coeusDynaBeanList)throws Exception{
        Map mpQuestionnaireId = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        mpQuestionnaireId.put("questionnaireId" , new Integer(questionnaireId));
        Hashtable htQuestionnaireData =
            (Hashtable)webTxnBean.getResults(request , GET_QUESTIONNAIRE_QUESTIONS , mpQuestionnaireId);
        Vector vecQuestionsData =
            (Vector)htQuestionnaireData.get(GET_QUESTIONNAIRE_QUESTIONS);
        Vector vecAnswersData = getQuestionnaireAnswers(hmQuestionnaireData, request);
        Vector vecQuestionnaireList = (Vector)session.getAttribute("questionnaireList"+session.getId());
        if(vecQuestionsData!=null && vecQuestionsData.size() > 0){
            for(int index = 0 ; index < vecQuestionsData.size() ; index++ ){
                DynaValidatorForm dynaQuestions =
                    (DynaValidatorForm)vecQuestionsData.get(index);
                String questionId = (String)dynaQuestions.get("questionId");
                String questionNumber = (String)dynaQuestions.get("questionNumber");
                if(vecAnswersData!=null && vecAnswersData.size() > 0){
                    coeusDynaBeanList.setBeanList(vecAnswersData);
                    for(int count = 0 ; count < vecAnswersData.size(); count++ ){
                        DynaValidatorForm dynaAnswers =
                            (DynaValidatorForm)vecAnswersData.get(count);
                        String ansQuestionId = (String)dynaAnswers.get("questionId");
                        String ansQuestionNumber = (String)dynaAnswers.get("questionNumber");
                        String ansQuestionnaireId = (String)dynaAnswers.get("questionaireId");
                        if(String.valueOf(questionnaireId).equals(ansQuestionnaireId )){
                            if(ansQuestionId.equals(questionId) &&
                            ansQuestionNumber.equals(questionNumber)){
                                String answer = (String)dynaAnswers.get("answer");
                                dynaQuestions.set("answer" , answer);
                                dynaQuestions.set("answerNumber" ,dynaAnswers.get("answerNumber"));
                                dynaQuestions.set("answerDescription" ,dynaAnswers.get("answerDescription"));
                                dynaQuestions.set("acType" , TypeConstants.UPDATE_RECORD);
                                dynaQuestions.set("awUpdateTimestamp" , dynaAnswers.get("updateTimestamp"));
                            }
                        }
                    }//end inner for
                }else{
                    //dynaQuestions.set("answerNumber",dynaQuestions.get("questionNumber"));
                    dynaQuestions.set("acType" , TypeConstants.INSERT_RECORD);
                }
            }//end for
        }//end If
        return vecQuestionsData ;
    }

    /**This method is to get the Questionnaire Answers
     * @param proposalNumber
     * @throws Exception
     * @return vector of dynaBeans
     */
    private Vector getQuestionnaireAnswers(HashMap hmQuestionnaireData, HttpServletRequest request)throws Exception{
        Map mpQuestionData = new HashMap();
        HttpSession session = request.getSession();
        mpQuestionData.put(MODULE_ITEM_CODE , new Integer(MODULE_ITEM_CODE_VALUE) );
        mpQuestionData.put(MODULE_SUB_ITEM_CODE , new Integer(MODULE_SUB_ITEM_CODE_VALUE) );
        mpQuestionData.put("moduleItemKey" , session.getAttribute("proposalNumber"+session.getId()));
        mpQuestionData.put("moduleSubItemKey" ,session.getAttribute("proposalNumber"+session.getId()));
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htQuestionnaireAnswers =
            (Hashtable)webTxnBean.getResults(request , GET_QUESTIONNAIRE_ANSWERS , hmQuestionnaireData );
        Vector vecQuestionnaireAnswers = (Vector)htQuestionnaireAnswers.get(GET_QUESTIONNAIRE_ANSWERS);
        return vecQuestionnaireAnswers ;
    }

    public void updateQuestionnaireCompletedFlag(HashMap hmQuestionnaireData,HttpServletRequest request, String strFlag)throws Exception{
        HttpSession session        = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        userID = userInfoBean.getUserId();
        WebTxnBean webTxnBean = new WebTxnBean();
        String questionnaireCompletionId  = null;
        Vector vecAnswerHeader = getQuestionnaireAnsHeader(hmQuestionnaireData, request);
        for(int index = 0 ; index < vecAnswerHeader.size() ; index ++ ){
            DynaValidatorForm dynaValidForm = (DynaValidatorForm)vecAnswerHeader.get(index);
            questionnaireCompletionId = (String)dynaValidForm.get("questionaireCompletionId");
        }
        DynaValidatorForm dynaFormData = (DynaValidatorForm)vecAnswerHeader.get(0);
        dynaFormData.set("completedFlag", strFlag);
        dynaFormData.set("awUpdateTimestamp" ,dynaFormData.get("updateTimestamp")) ;
        dynaFormData.set("updateUser" , userID);
        dynaFormData.set("questionaireCompletionId" , questionnaireCompletionId);
        dynaFormData.set("updateTimestamp" , prepareTimeStamp().toString()) ;
        webTxnBean.getResults(request , UPD_QUESTIONNAIRE_COMPLETED_FLAG , dynaFormData );

    }
    /**
     * This method saves the form Data to the osp$questionnaire_ans_header table
        and osp$questionaire_answer table
     * @param hmQuestionnaireData
     * @param coeusDynaBeanList
     * @param request
     * @throws Exception
     */
    public void performSaveAction(HashMap hmQuestionnaireData,
                                  CoeusDynaBeansList coeusDynaBeanList, HttpServletRequest request)throws Exception{
        Vector vecQuestionsList = (Vector)coeusDynaBeanList.getList();
        String questionnaireCompletionId = EMPTY_STRING ;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        boolean boolNewQuestionnaire    = false;
        Vector vecAnswerHeader = getQuestionnaireAnsHeader(hmQuestionnaireData, request);
        if(vecAnswerHeader == null || vecAnswerHeader.size() == 0){
            questionnaireCompletionId = generateQuestionnaireCompletion(request);
            vecAnswerHeader = prepareDynaAnswerHeader(hmQuestionnaireData, coeusDynaBeanList, request);
            boolNewQuestionnaire = true;
        }else{
            for(int index = 0 ; index < vecAnswerHeader.size() ; index ++ ){
                DynaValidatorForm dynaValidForm = (DynaValidatorForm)vecAnswerHeader.get(index);
                questionnaireCompletionId = (String)dynaValidForm.get("questionaireCompletionId");

            }
        }
        Vector vecSaveData = prepareSaveData(hmQuestionnaireData, coeusDynaBeanList, request);
        if(vecSaveData!=null && vecSaveData.size() > 0){
            vecQuestionsList = vecSaveData ;
        }
        if(vecQuestionsList!=null && vecQuestionsList.size() > 0) {
            String acType = EMPTY_STRING ;
            String oldQuestionnaireId = EMPTY_STRING ;
            if(vecAnswerHeader!=null && vecAnswerHeader.size() > 0){
                DynaValidatorForm dynaFormData = (DynaValidatorForm)vecAnswerHeader.get(0);
                String questionnaireId = (String)dynaFormData.get("questionaireId");
                oldQuestionnaireId  = String.valueOf(session.getAttribute("questionnaireId"+session.getId()));
                if(boolNewQuestionnaire){
                    dynaFormData.set("acType" ,TypeConstants.INSERT_RECORD);
                    dynaFormData.set("questionaireId" ,questionnaireId);
                }
                else{
                    dynaFormData.set("acType" ,TypeConstants.UPDATE_RECORD);
                   dynaFormData.set("awUpdateTimestamp" ,dynaFormData.get("updateTimestamp")) ;
                }
                dynaFormData.set("updateTimestamp" , prepareTimeStamp().toString()) ;
                dynaFormData.set("questionaireCompletionId" , questionnaireCompletionId);
                dynaFormData.set("updateUser" , userID);
                dynaFormData.set("completedFlag", "N");
                webTxnBean.getResults(request , ADD_UPD_QUESTIONNAIRE_ANS_HEADER , dynaFormData );
            }
            HashMap hMap = new HashMap();
            hMap.put("questionaireCompletionId",questionnaireCompletionId);
            webTxnBean.getResults(request , DEL_QUESTIONNAIRE_ANSWERS, hMap);
            for(int index = 0 ; index < vecQuestionsList.size() ; index ++ ){
                DynaValidatorForm dynaFormData = (DynaValidatorForm)vecQuestionsList.get(index);
                String answer = (String)dynaFormData.get("answer");
                dynaFormData.set("acType" , TypeConstants.INSERT_RECORD);
                dynaFormData.set("awUpdateTimestamp" ,dynaFormData.get("updateTimestamp")) ;
                dynaFormData.set("updateTimestamp" , prepareTimeStamp().toString()) ;
                dynaFormData.set("answerNumber",ANSWER_NUMBER_VALUE);
                dynaFormData.set("questionaireCompletionId" , questionnaireCompletionId );
                webTxnBean.getResults(request , ADD_UPD_QUESTIONNAIRE_ANSWERS, dynaFormData);

            }
        }
    }

    /**This methods prepares a dynavalidatorform for a new qiuestionnaire
     that has to be inserted in answer header table     *
     * @throws Exception
     * @return Vector containing dynaNewBean
     */
    public Vector prepareDynaAnswerHeader(HashMap hmQuestionnaireData,
                                          CoeusDynaBeansList coeusDynaBeanList, HttpServletRequest request)throws Exception{
        Vector vecQuestionAnswerHeader = new Vector();
        HttpSession session = request.getSession();
        DynaActionForm dynaFormData = coeusDynaBeanList.getDynaForm(request,"questionnaireForm");
        DynaBean dynaNewBean = ((DynaBean)dynaFormData).getDynaClass().newInstance();
        dynaNewBean.set("moduleItemKey" , hmQuestionnaireData.get("moduleItemKey"));
        dynaNewBean.set("moduleSubItemKey" ,hmQuestionnaireData.get("moduleSubItemKey"));
        dynaNewBean.set("moduleItemCode" , String.valueOf((Integer)hmQuestionnaireData.get("moduleItemCode")));
        dynaNewBean.set("moduleSubItemCode" , String.valueOf(hmQuestionnaireData.get("moduleSubItemCode")));
        dynaNewBean.set("acType" , TypeConstants.INSERT_RECORD);
        dynaNewBean.set("questionaireId" , String.valueOf(session.getAttribute("questionnaireId"+session.getId())));
        vecQuestionAnswerHeader.addElement(dynaNewBean);
        return vecQuestionAnswerHeader ;
    }


    /**This method is to get the data from OSP$Questionnaire_answerHeader Table
     * @throws Exception
     * @return
     */
    public Vector getQuestionnaireAnsHeader(HashMap hmQuestionnaireData,HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        String questionnaireId = String.valueOf(session.getAttribute("questionnaireId"+session.getId()));
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htQuestionnaireAnswers =
        (Hashtable)webTxnBean.getResults(request , GET_QUESTIONNAIRE_ANS_HEADER , hmQuestionnaireData );
        Vector vecQuestionnaireAnswers = (Vector)htQuestionnaireAnswers.get(GET_QUESTIONNAIRE_ANS_HEADER);
        Vector vecFilterAnsHeader = null ;
        if(vecQuestionnaireAnswers!=null && vecQuestionnaireAnswers.size() > 0 ){
            vecFilterAnsHeader = new Vector();
            for(int index = 0 ; index < vecQuestionnaireAnswers.size() ; index ++){
                DynaValidatorForm dynaFormData = (DynaValidatorForm)vecQuestionnaireAnswers.get(index);
                String questnaireId = (String)dynaFormData.get("questionaireId");
                if(questnaireId.equals(questionnaireId)){
                    vecFilterAnsHeader.addElement(dynaFormData);
                }
            }
        }
        return vecFilterAnsHeader ;
    }

    /**
     *
     * @param coeusDynaBeanList
     * @return
     */
    public String getAnswerFromUser(CoeusDynaBeansList coeusDynaBeanList){
        String strAnswer = "";
        if(coeusDynaBeanList != null){
            Vector vecQuestionnaireData = (Vector)coeusDynaBeanList.getList();
            if(vecQuestionnaireData!=null  && vecQuestionnaireData.size() > 0){
                for(int index = 0 ;index < vecQuestionnaireData.size() ; index ++ ){
                    DynaBean dynaFormData = (DynaBean)vecQuestionnaireData.get(index);
                    strAnswer = (String)dynaFormData.get("answer");
                }
            }
        }
        return strAnswer;
    }
    /**This method is used to validate the form data
     * @throws Exception
     * @return true if there are no errors.
     */
    private boolean validateAnswers(CoeusDynaBeansList coeusDynaBeanList,
                                    HttpServletRequest request)throws Exception{
        Vector vecQuestionnaireData = (Vector)coeusDynaBeanList.getList();
        String datePattern = "MM/dd/yyyy";
        ActionMessages actionMessages = new ActionMessages();
        if(vecQuestionnaireData!=null  && vecQuestionnaireData.size() > 0){
            for(int index = 0 ;index < vecQuestionnaireData.size() ; index ++ ){
                DynaBean dynaFormData = (DynaBean)vecQuestionnaireData.get(index);
                String answer = (String)dynaFormData.get("answer");
                String dataType = (String)dynaFormData.get("answerDataType");

                String questionId = (String)dynaFormData.get("questionNumber");
                if(dataType != null && answer != null &&
                answer.trim().length() > 0 ){
                    if(dataType.equalsIgnoreCase(NUMBER_DATA_TYPE)){
                        try{
                            Integer.parseInt(answer);
                        }catch(NumberFormatException nfe){
                            actionMessages.add("numberFormatException",
                            new ActionMessage(NUMBER_FORMAT_EXCEPTION,questionId));
                            saveMessages(request, actionMessages);
                            return false;
                        }
                    }else if(dataType.equalsIgnoreCase(DATE_DATA_TYPE)){
                        String   resultDate = EMPTY_STRING ;
                        DateUtils dtUtils = new DateUtils();
                        if (answer != null && !answer.trim().equals(EMPTY_STRING)) {
                            resultDate = dtUtils.formatDate(answer,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
                            if(resultDate == null){
                                actionMessages.add("notValidDate",
                                new ActionMessage(NOT_VALID_DATE,questionId));
                                saveMessages(request, actionMessages);
                                return false;
                            }
                        }
                    }
                }
            }//End For
        }//End If
        return true;
    }

    /**
     * This method returns all the questionnaire associated
     * with a module and its sub module
     * @param questionnaireId
     * @throws Exception
     */
    public void getQuestionnaireList(int questionnaireId , HashMap hmQuestionnaireData,
                                     HttpServletRequest request) throws Exception{
        Map mpQuestionData = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        mpQuestionData.put("moduleItemCode" , hmQuestionnaireData.get("moduleItemCode") );
        mpQuestionData.put("moduleSubItemCode" , hmQuestionnaireData.get("moduleSubItemCode") );
        Hashtable htQuestionnaireList =
            (Hashtable)webTxnBean.getResults(request , "getQuestionnaireListForModule" , mpQuestionData );
        Vector vecQuestionnaireList = (Vector)htQuestionnaireList.get("getQuestionnaireListForModule") ;
        String questionnaireLabel = EMPTY_STRING ;
        if(vecQuestionnaireList!=null && vecQuestionnaireList.size() > 0){
            for(int index = 0 ; index < vecQuestionnaireList.size() ; index++){
                DynaValidatorForm dynaFormData =
                    (DynaValidatorForm)vecQuestionnaireList.get(index);
                String strQuestionnaireId = (String)dynaFormData.get("questionaireId");
                if(String.valueOf(questionnaireId).equals(strQuestionnaireId)){
                   questionnaireLabel =  (String)dynaFormData.get("questionaireLabel");
                }
            }
        }
         session.setAttribute("questionaireLabel" , questionnaireLabel );
    }

    /**This method prepares the form data to be saved or updated
     * @throws Exception
     * @return vecSaveData
     */
    public Vector prepareSaveData(HashMap hmQuestionnaireData,
                                  CoeusDynaBeansList coeusDynaBeanList, HttpServletRequest request)throws Exception {
        HttpSession session = request.getSession();
        int questionnaireId = ((Integer)session.getAttribute("questionnaireId"+session.getId())).intValue();

        Vector vecOldData = getQuestionnaireQuestions(questionnaireId , hmQuestionnaireData,
                                            request, coeusDynaBeanList ) ;

        Vector vecFormData = (Vector)coeusDynaBeanList.getList();
        Vector vecSaveData = null ;
        if(vecFormData!=null && vecFormData.size() > 0){
            vecSaveData = new Vector();
            for(int index = 0 ; index < vecFormData.size() ; index ++){
                DynaValidatorForm dynaFormData =
                    (DynaValidatorForm)vecFormData.get(index);
                /*
                if(isDataChanged(dynaFormData ,questionnaireId ,hmQuestionnaireData,
                        coeusDynaBeanList, request)){
                    vecSaveData.addElement(dynaFormData );
                }
                */
            }
        }
        return vecSaveData ;
    }

    /**This method is to check whether the form data has been changed or not
     * @param dynaForm
     * @param questionnaireId
     * @throws Exception
     * @return
     */
    public boolean isDataChanged(DynaValidatorForm dynaForm , int questionnaireId ,
                                 HashMap hmQuestionnaireData, CoeusDynaBeansList coeusDynaBeanList,
                                 HttpServletRequest request)throws Exception{

        String answer = (String)dynaForm.get("answer");
        String questionNumber = (String)dynaForm.get("questionNumber");
        Vector vecServerData = getQuestionnaireQuestions(questionnaireId , hmQuestionnaireData,
                                        request, coeusDynaBeanList) ;
        boolean isDataChanged = false ;
        if(vecServerData!=null && vecServerData.size() > 0){
            String strAnswer = EMPTY_STRING ;
            String strQuestionNumber = EMPTY_STRING ;
            for(int index = 0 ; index < vecServerData.size() ; index ++){
                DynaValidatorForm dynaOldData = (DynaValidatorForm)vecServerData.get(index) ;
                strAnswer = (String)dynaOldData.get("answer") ;
                strQuestionNumber = (String)dynaOldData.get("questionNumber");
                if(strQuestionNumber.equals(questionNumber)){
                    if(strAnswer!=null && !strAnswer.equals(EMPTY_STRING)){
                        if(!strAnswer.equals(answer)){
                            isDataChanged = true ;
                        }
                    }
                }
            }
        }
        return isDataChanged ;
    }

    /** Manufacture the LockBean based on the parameter passed by the specific module
     *say, Propsoal, Protocol, Budget etc.
     *@param UserInfoBean, Proposal number
     *@returns LockBean
     *@throws Exception
     */
    protected LockBean getLockingBean(UserInfoBean userInfoBean,
                                      String protocolNumber, HttpServletRequest request, String value) throws Exception{
        LockBean lockBean = new LockBean();
        lockBean.setLockId(CoeusLiteConstants.PROTOCOL_LOCK_STR+protocolNumber);
        String mode = (String)request.getSession().getAttribute(CoeusLiteConstants.MODE_DETAILS+request.getSession().getId());
        mode = getMode(mode , null);
        lockBean.setMode(mode);
        lockBean.setModuleKey(CoeusLiteConstants.PROTOCOL_MODULE);
        lockBean.setModuleNumber(protocolNumber);
        lockBean.setModuleUnitNumber(userInfoBean.getUnitNumber());
        lockBean.setUnitNumber(UNIT_NUMBER);
        lockBean.setUserId(userInfoBean.getUserId());
        lockBean.setUserName(userInfoBean.getUserName());
        lockBean.setSessionId(request.getSession().getId());
        return lockBean;
    }

    protected String getMode(String mode, Object obj) throws Exception{
        if(mode!= null && !mode.equals(EMPTY_STRING)){
            if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
                mode = CoeusLiteConstants.DISPLAY_MODE;
            }
        }else{
            mode = CoeusLiteConstants.MODIFY_MODE;
        }
        return mode;
    }

    /** Read the save status for the given proposal number
     *@throws Exception
     */
    protected void readSavedStatus(WebTxnBean webTxnBean ,String actionFrom,
                                   HttpServletRequest request) throws Exception{
        Map hmSavedData =null;
        Hashtable htReqData =null;
        HashMap hmMenuData = new HashMap();
        HashMap hmQuestionnaire = new HashMap();
        HttpSession session = request.getSession();
        String proposalNumber = EMPTY_STRING ;
        String protocolNumber = EMPTY_STRING ;
        String sequenceNumber = EMPTY_STRING ;
        Vector menuData = null ;
        if(actionFrom.equalsIgnoreCase("DEV_PROPOSAL")){
            proposalNumber = (String)request.getSession().getAttribute("proposalNumber"+request.getSession().getId());
            menuData= (Vector)request.getSession().getAttribute(CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
            hmMenuData.put("proposalNumber", proposalNumber);
            hmQuestionnaire.put("proposalNumber" ,proposalNumber);
        }else if(actionFrom.equalsIgnoreCase("PROTOCOL")){
            protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
            sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
            menuData= (Vector)request.getSession().getAttribute(CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
            hmQuestionnaire.put("proposalNumber" ,protocolNumber);
            hmMenuData.put("protocolNumber", protocolNumber);
            hmMenuData.put("sequenceNumber", new Integer(Integer.parseInt(sequenceNumber)));
        }

        if(webTxnBean == null){
            webTxnBean = new WebTxnBean();
        }
        if(menuData!= null && menuData.size() > 0){
            hmSavedData = new HashMap();
            htReqData = new Hashtable();

            MenuBean dataBean = null;
            String menuId = EMPTY_STRING;
            String strValue = EMPTY_STRING;
            boolean isDynamic = false;
            HashMap hmReturnData= null;
            for(int index = 0; index < menuData.size(); index++){
                dataBean = (MenuBean)menuData.get(index);
                /**Checkk for the dynamically created menu's. For example
                 *Questionnaire Menu. the dynamicId specifies the dynamic menu ids
                 *generated. At present it gets the dynamic Id for the questionnaire
                 *Menu and makes server call to show the saved questionnaire menu
                 */
                if(dataBean.getDynamicId()!= null && !dataBean.getDynamicId().equals(EMPTY_STRING)){
                    menuId =dataBean.getDynamicId();
                    isDynamic = true;
                }else{
                    menuId = dataBean.getMenuId();
                    isDynamic = false;
                }
                hmQuestionnaire.put("menuId" , menuId);
                hmMenuData.put("menuId", menuId);
                if(isDynamic){
                    htReqData = (Hashtable)webTxnBean.getResults(request, "getSavedQuestionnaireData", hmQuestionnaire);
                    hmReturnData = (HashMap)htReqData.get("getSavedQuestionnaireData");
                }else{
                    if(actionFrom.equalsIgnoreCase("DEV_PROPOSAL")){
                        htReqData = (Hashtable)webTxnBean.getResults(request, "getSavedProposalMenuData", hmMenuData);
                        hmReturnData = (HashMap)htReqData.get("getSavedProposalMenuData");
                    }else if(actionFrom.equalsIgnoreCase("PROTOCOL")){
                        htReqData = (Hashtable)webTxnBean.getResults(request, "getSavedProtocolData", hmMenuData);
                        hmReturnData = (HashMap)htReqData.get("getSavedProtocolData");
                    }

                }
                if(hmReturnData!=null) {
                    strValue = (String)hmReturnData.get("AV_SAVED_DATA");
                    int value = Integer.parseInt(strValue);
                    if(value == 1){
                        dataBean.setDataSaved(true);
                    }else if(value == 0){
                        dataBean.setDataSaved(false);
                    }
                }
            }
            if(actionFrom.equalsIgnoreCase("DEV_PROPOSAL")){
                request.getSession().removeAttribute(CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
                request.getSession().setAttribute(CoeusliteMenuItems.PROPOSAL_MENU_ITEMS, menuData);
            }else if(actionFrom.equalsIgnoreCase("PROTOCOL")){
                request.getSession().removeAttribute(CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
                request.getSession().setAttribute(CoeusliteMenuItems.PROTOCOL_MENU_ITEMS, menuData);
            }

        }
    }

    /**This method generates a unique Qustionnaire Completion Id
     * @throws Exception
     * @return questionnaireCompId
     */
    public String generateQuestionnaireCompletion(HttpServletRequest request)throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htQuestionnaireCompId = (Hashtable)webTxnBean.getResults(request , "generateCompletionId" , null );
        HashMap hmQuestionnaireCompId = (HashMap)htQuestionnaireCompId.get("generateCompletionId");
        String questionnaireCompId = (String)hmQuestionnaireCompId.get("questionnaireCompletionId");
        return questionnaireCompId ;
    }
    public String isNull (String strInput){
        String strOutput = "";
        if(strInput != null){
            strOutput = strInput.trim();
        }
        return strOutput;
    }
}



