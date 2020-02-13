package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeuslite.coiv2.services.GettingRightsCoiV2Service;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireQuestionsBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean;
import edu.mit.coeus.questionnaire.utils.QuestionnaireHandler;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;
import edu.mit.coeuslite.utils.CoeusDynaBeansList;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;
import org.apache.commons.beanutils.BeanUtilsBean;

/**
 *
 * @author Lijo Joy
 */
public class CoiGetAnnualQuestionnaireAction extends COIBaseAction {

    private static final String GET_QUESTIONNAIRE_DATA = "/getCoiAnnualQuestionnaireCoiv2";
    private static final String SAVE_QUESTIONNAIRE_DATA = "/saveCoiAnnualQuestionnaireCoiv2";
    private static final String SUCCESS = "success";
    private static final int MODULE_ITEM_CODE_VALUE = 3;
    private static final String MODULE_ITEM_CODE = "moduleItemCode";
    private static final int MODULE_SUB_ITEM_CODE_VALUE = 0;
    private static final String MODULE_SUB_ITEM_CODE = "moduleSubItemCode";
    private static final String EMPTY_STRING = "";
    private static final String ANSWER_NUMBER_VALUE = "1";
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private static final String NUMBER_DATA_TYPE = "NUMBER";
    private static final String DATE_DATA_TYPE = "DATE";
    // error messages constants
    private static final String NOT_VALID_DATE = "customElements.notValidDate";
    private static final String NUMBER_FORMAT_EXCEPTION = "customElements.numberFormatException";
    //statements
    private static final String GET_QUESTIONNAIRE_QUESTIONS = "getQuestionnaireQuestions";
    private static final String GET_QUESTIONNAIRE_ANSWERS = "getQuestionnaireAnswers";
    private static final String GET_QUESTIONNAIRE_ANS_HEADER = "getQuestionnaireAnsHeader";
    private static final String ADD_UPD_QUESTIONNAIRE_ANSWERS = "addUpdQuestionnaireAnswers";
    private static final String ADD_UPD_QUESTIONNAIRE_ANS_HEADER = "addUpdQuestionnaireAnswerHeader";
    private static final String DATA_OBJECT = "dataObject";
    private static final char RESTART = 'S';
    private static final char MODIFY = 'M';

    /**This Method get/add/update the Questionnaire and Routes to the appropriate JSP
     * @param actionMapping
     * @param actionForm
     * @param request
     * @param response
     * @throws Exception
     * @return ActionForward object
     */
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

                //for annual disclosure menu change
        CoiCommonService coiCommonService1 = CoiCommonService.getInstance();
        request.setAttribute("DiscViewQtnr", true);//to check Questionnaire menu selected
        Integer disclosureAvailable = coiCommonService1.userHasDisclosure(request);
        if (disclosureAvailable > 0) {
            request.setAttribute("disclosureAvailableMessage", true);
            CoiDisclosureBean annualBean = new CoiDisclosureBean();
            annualBean = coiCommonService1.getAnnualDisclosure(request);
            request.setAttribute("annualDisclosureBean", annualBean);
        } else {
            request.setAttribute("disclosureAvailableMessage", false);
        }
        //for annual disclosure menu change end

String projectType=(String)request.getSession().getAttribute("projectType");
if(projectType!=null && projectType.equalsIgnoreCase("Revision")){
 request.getSession().setAttribute("AnnualRevision","Revision");   
}

//added by 10-12-2010
HttpSession session = request.getSession();
PersonInfoBean person1 = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
String personId1=person1.getPersonID();
WebTxnBean txnBean = new WebTxnBean();
 HashMap hmData1 = new HashMap();
        hmData1.put("personId", personId1);
        Hashtable htPersonData = (Hashtable) txnBean.getResults(request, "getPersonDetails", hmData1);
        Vector personDatas = (Vector) htPersonData.get("getPersonDetails");
        if (personDatas != null && personDatas.size() > 0) {
            PersonInfoBean personInfoBean = (PersonInfoBean) personDatas.get(0);

            //added by Vineetha
              request.setAttribute("PersonDetails", personDatas);
            session.setAttribute("person", personInfoBean);
        }
        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId=person.getPersonID();
//added by  10-12-2010
        //HttpSession session = request.getSession();
        GettingRightsCoiV2Service gettingRightsCoiV2Service = GettingRightsCoiV2Service.getInstance();        
        CoeusDynaBeansList coeusDynaBeanList = (CoeusDynaBeansList) actionForm;
        String navigator = EMPTY_STRING;
        WebTxnBean webTxnBean = new WebTxnBean(); 
        HashMap hmQuestionnaireData = null;
        //Code added for coeus4.3 questionnaire enhancements case#2946 - starts
        UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user" + session.getId());
        QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
        QuestionnaireHandler questionnaireHandler = new QuestionnaireHandler(userInfoBean.getUserId());
        String moduleKey = EMPTY_STRING;
        //Code added for coeus4.3 questionnaire enhancements case#2946 - ends
        int questionnaireID = 0;
        String questnnaireId = null;

        int qnrListSize = 0;

        Vector qstnnrIdList = (Vector)session.getAttribute("qstnnrIdList");

        if(qstnnrIdList != null) {
            qnrListSize =   qstnnrIdList.size();
        }
        request.setAttribute("qnrListSize", qnrListSize);

        if(qnrListSize == 0 || qnrListSize > 1) {
            Map mapMenuList = new HashMap();
            session.setAttribute("MenuId", "ANN_DISCL");
            session.setAttribute("questionaireLabel", "Annual Disclosure Certification");
            if (request.getAttribute("actionFrom") != null && !request.getAttribute("actionFrom").equals(EMPTY_STRING)) {
                session.setAttribute("actionFrom", (String) request.getAttribute("actionFrom"));
            }
            return actionMapping.findForward("success");
        } else {
            questnnaireId = qstnnrIdList.get(0).toString();
        }

        if (questnnaireId != null && !questnnaireId.equals(EMPTY_STRING)) {
            questionnaireID = Integer.parseInt(questnnaireId);
            session.setAttribute("questionnaireId" + session.getId(), new Integer(questionnaireID));
            //Code added for coeus4.3 questionnaire enhancements case#2946
            questionnaireModuleObject.setQuestionnaireId(Integer.parseInt(questnnaireId));
        }
        if (request.getAttribute("actionFrom") != null && !request.getAttribute("actionFrom").equals(EMPTY_STRING)) {
            session.setAttribute("actionFrom", (String) request.getAttribute("actionFrom"));
            //Code added for coeus4.3 questionnaire enhancements case#2946
            questionnaireModuleObject.setModuleItemDescription(request.getParameter("actionFrom"));
        }
        if (request.getAttribute("menuId") != null && !request.getAttribute("menuId").equals(EMPTY_STRING)) {
            session.setAttribute("menuCode", (String) request.getAttribute("menuId"));
        }
        //Code added for coeus4.3 questionnaire enhancements case#2946 - starts
        if (request.getAttribute("questionaireLabel") != null && !request.getAttribute("questionaireLabel").equals(EMPTY_STRING)) {
            session.setAttribute("questionaireLabel", (String) request.getAttribute("questionaireLabel"));
        }
        //Code added for coeus4.3 questionnaire enhancements case#2946 - ends
        Vector vecQuestionnaireQuestions = null;
        Vector vecQuestionnaireQuestionsextcode = null;
        String actionFrom = (String) session.getAttribute("actionFrom");
        if (actionFrom != null && !actionFrom.equals(EMPTY_STRING)) {
            hmQuestionnaireData = new HashMap();
            questionnaireModuleObject.setModuleItemCode(8);
            questionnaireModuleObject.setModuleSubItemCode(0);
            String operationType = request.getParameter("operationType");
            if (operationType == null || operationType.equals("")) {
                operationType = (String) request.getAttribute("operationType");
            }
            Integer seqNum = null;
            Integer seqNumAnnual = null;
            if (operationType != null && operationType.equals("MODIFY")) {
                moduleKey = (String) session.getAttribute("DisclosureNumberInUpdateSession");
                questionnaireModuleObject.setModuleItemKey((String) session.getAttribute("DisclosureNumberInUpdateSession"));
                questionnaireModuleObject.setApplicableModuleItemKey((String) session.getAttribute("DisclosureNumberInUpdateSession"));
//                String seq = (String) session.getAttribute("SequenceNumberInUpdateSession");
//                seqNum = Integer.parseInt(seq);
                seqNum = (Integer) session.getAttribute("SequenceNumberInUpdateSession");             
                if(seqNum!=null){
                questionnaireModuleObject.setModuleSubItemKey(seqNum.toString());
                questionnaireModuleObject.setApplicableModuleSubItemKey(seqNum);
                }
            } else {
                moduleKey = (String) session.getAttribute("DisclNumber");
                questionnaireModuleObject.setModuleItemKey((String) session.getAttribute("DisclNumber"));
                 questionnaireModuleObject.setApplicableModuleItemKey((String) session.getAttribute("DisclNumber"));
                seqNum = (Integer) session.getAttribute("param2");
                if(seqNum==null)
                {
                    seqNum = (Integer) session.getAttribute("DisclSeqNumber");
                }
                seqNumAnnual = (Integer)session.getAttribute("currentSequence");
                session.setAttribute("DisclNumber", moduleKey);                
                if(seqNumAnnual!=null){
                    setApprovedDisclosureDetails(moduleKey,seqNumAnnual,personId,request);
                    String StrSeqNumber = Integer.toString(seqNumAnnual.intValue());
                    questionnaireModuleObject.setModuleSubItemKey(StrSeqNumber);
                    questionnaireModuleObject.setApplicableModuleSubItemKey(seqNumAnnual);
                }else if(seqNum!=null){
                     setApprovedDisclosureDetails(moduleKey,seqNum,personId,request);
                     String StrSeqNumber = Integer.toString(seqNum.intValue());
                    questionnaireModuleObject.setModuleSubItemKey(StrSeqNumber);
                    questionnaireModuleObject.setApplicableModuleSubItemKey(seqNum);
                }

            }
           String fromQn="fromQuestionnaire";
           session.setAttribute("fromQuestionnaire", fromQn);
        }
           
         String check=(String) session.getAttribute("extcodeModuleKey");
        if (actionMapping.getPath().equals(GET_QUESTIONNAIRE_DATA)) {

            session.setAttribute("extcodeModuleKey", moduleKey);

            String mode = (String) session.getAttribute("mode" + session.getId());
            String functionType = "M";
            if (mode != null && !mode.equals("")) {
                if (mode.equalsIgnoreCase("display") || mode.equalsIgnoreCase("D")) {
                    functionType = "D";
                }
            }
            //For getting the questions to display
            // 4272: Maintain History of Questionnaire - Start
            if (questionnaireModuleObject.getQuestionnaireVersionNumber() == 0) {
                QuestionnaireTxnBean questionnaireTxnBean = new QuestionnaireTxnBean();
                int latestVersion = questionnaireTxnBean.fetchMaxVersionNumberOfQuestionnaire(
                        questionnaireModuleObject.getQuestionnaireId());
                questionnaireModuleObject.setQuestionnaireVersionNumber(latestVersion);
            }

            // 4272: Maintain History of Questionnaire - End
            if(check!=null){
                 String operation = "";
            if (request.getAttribute("operation") != null) {
                operation = (String) request.getAttribute("operation");
            } else if (request.getParameter("operation") != null || (request.getAttribute("operation")!=null && ((String) request.getAttribute("operation")).equals(""))) {
                operation = request.getParameter("operation");
            }
            operation = (operation == null) ? EMPTY_STRING : operation;
//             if(questionnaireModuleObject.getQuestionnaireId()==0){
//             QuestionnaireAnswerHeaderBean questionnaireQuestion = new QuestionnaireAnswerHeaderBean();
//             questionnaireQuestion= (QuestionnaireAnswerHeaderBean) session.getAttribute("questionnaireModuleObject");
//             int questionnaireId=questionnaireQuestion.getQuestionnaireId();
//             questionnaireModuleObject.setQuestionnaireId(questionnaireId);}
            String moduleSubItemKey="";
            if(questionnaireModuleObject.getModuleSubItemKey()!=null){
               moduleSubItemKey=questionnaireModuleObject.getModuleSubItemKey();
             }
         questionnaireModuleObject = (QuestionnaireAnswerHeaderBean) session.getAttribute("questionnaireModuleObject");
           questionnaireModuleObject.setModuleSubItemKey(moduleSubItemKey);
            hmQuestionnaireData = (HashMap) session.getAttribute("questionnaireInfo");
            hmQuestionnaireData.put("message", null);
            CoeusVector cvData = (CoeusVector) convertDynaBeanToBean((Vector) coeusDynaBeanList.getList());
            //Validate and save the answers, then get the nquestions to display in next page
            if (operation.equals("SAVE") && validateAnswers(coeusDynaBeanList, request)) {
               if(hmQuestionnaireData!=null){
                hmQuestionnaireData = questionnaireHandler.saveAndGetNextQuestions(questionnaireModuleObject,
                        moduleKey, cvData);
               }
                String mess = (String) hmQuestionnaireData.get("message");
                if (mess != null && mess.equals("COMPLETED")) {
                             int ansCount=0;
                     HashMap hmData2 = new HashMap();
          hmData2.put("moduleItemKey",moduleKey);
         hmData2.put("questionnaireId", questionnaireModuleObject.getQuestionnaireId());
        hmData2.put("moduleItemCode", questionnaireModuleObject.getModuleItemCode());
        hmData2.put("moduleSubItemCode",questionnaireModuleObject.getModSubCode());
         hmData2.put("moduleSubItemKey", questionnaireModuleObject.getModuleSubItemKey());
        Hashtable htanswerYes = (Hashtable) txnBean.getResults(request, "getQuestionAnswerYes", hmData2);
        HashMap resultMap=(HashMap)htanswerYes.get("getQuestionAnswerYes");
        if (resultMap!=null)
        {
        ansCount=Integer.parseInt((resultMap.get("ll_count")).toString());
        }
        if (ansCount>0){
              String annualQstnFlag="true";
        request.getSession().setAttribute("annualQstnFlag", annualQstnFlag);
                }else
                {
            String annualQstnFlag="false";
        request.getSession().setAttribute("annualQstnFlag", annualQstnFlag);
                }  
//        // this fix is to InProgress menu Start
//        if(!(request.getSession().getAttribute("frmPendingInPrg")!=null && request.getAttribute("DiscViewQtnr")!=null && projectType.equalsIgnoreCase("Annual"))){
           return actionMapping.findForward("continueUpdate");
//        }// this fix is to InProgress menu  ends        
            }
            }
            session.setAttribute("questionnaireInfo", hmQuestionnaireData);
            Vector vecQuestionnaireData = (Vector) hmQuestionnaireData.get(DATA_OBJECT);
            vecQuestionnaireQuestions = (Vector) vecQuestionnaireData.get(0);
            vecQuestionnaireQuestions = convertBeanToDynaBean(vecQuestionnaireQuestions,
                    request, coeusDynaBeanList);
            //Code commented and added for coeus4.3 questionnaire enhancements case#2946 - ends
            coeusDynaBeanList.setList(vecQuestionnaireQuestions);
//                }
            }else if(check==null){

            hmQuestionnaireData = questionnaireHandler.getQuestionnaireQuestions(questionnaireModuleObject,
                    moduleKey, functionType);
            Vector vecQuestionnaireData = (Vector) hmQuestionnaireData.get(DATA_OBJECT);
            vecQuestionnaireQuestions = (Vector) vecQuestionnaireData.get(0);
            vecQuestionnaireQuestionsextcode = (Vector) vecQuestionnaireData.get(0);
            vecQuestionnaireQuestionsextcode = convertBeanToDynaBean(vecQuestionnaireQuestionsextcode,
                    request, coeusDynaBeanList);
            vecQuestionnaireQuestions = convertBeanToDynaBean(vecQuestionnaireQuestions,
                    request, coeusDynaBeanList);
             session.setAttribute("extcodequestionnaire", vecQuestionnaireQuestionsextcode);
            String mess = (String) hmQuestionnaireData.get("message");
            if (mess != null && mess.equals("COMPLETED")) {
                request.setAttribute("COMPLETED", "COMPLETED");
                // NEW MENU CHANGE  
               request.removeAttribute("QnrNotCompleted");             
               // NEW MENU CHANGE  
                              //  return actionMapping.findForward("continue");
            }
            session.setAttribute("questionnaireInfo", hmQuestionnaireData);
            session.setAttribute("questionnaireModuleObject", questionnaireModuleObject);
            session.setAttribute("questionnaireModuleObjectextcode", questionnaireModuleObject);
            //Code commented and added for coeus4.3 questionnaire enhancements case#2946 - ends
            coeusDynaBeanList.setList(vecQuestionnaireQuestions);
            session.setAttribute("questionsList", coeusDynaBeanList);
            //        session.setAttribute("proposalNumber"+session.getId(),proposalNumber);
            if (request.getAttribute("acType") != null) {
                if (((String) request.getAttribute("acType")).equals("review")) {
                    if (request.getAttribute("reviewType") != null) {
                        request.setAttribute("reviewType", (String) request.getAttribute("reviewType"));
                    }
                    if (request.getAttribute("reviewType").equals("history")) {
                        return actionMapping.findForward("history");
                    }
                    return actionMapping.findForward("review");
                }
            }
            if (request.getAttribute("mode") != null) {
                if (((String) request.getAttribute("mode")).equals("edit")) {
                    return actionMapping.findForward("modify");
                } else if (((String) request.getAttribute("mode")).equals("saveUpdate")) {
                    request.setAttribute("operation", "SAVE");
                    return actionMapping.findForward("saveUpdate");
                }
            }
            navigator = SUCCESS;}
        } else if (actionMapping.getPath().equals(SAVE_QUESTIONNAIRE_DATA)) {
            String operation = "";
            if (request.getAttribute("operation") != null) {
                operation = (String) request.getAttribute("operation");
            } else if (request.getParameter("operation") != null || ((String) request.getAttribute("operation")).equals("")) {
                operation = request.getParameter("operation");
            }
            operation = (operation == null) ? EMPTY_STRING : operation;
            String moduleSubItemKey= "";
             if(questionnaireModuleObject.getModuleSubItemKey()!=null){
               moduleSubItemKey=questionnaireModuleObject.getModuleSubItemKey();
             }
         questionnaireModuleObject = (QuestionnaireAnswerHeaderBean) session.getAttribute("questionnaireModuleObject");
           questionnaireModuleObject.setModuleSubItemKey(moduleSubItemKey);
         hmQuestionnaireData = (HashMap) session.getAttribute("questionnaireInfo");
            hmQuestionnaireData.put("message", null);
            CoeusVector cvData = (CoeusVector) convertDynaBeanToBean((Vector) coeusDynaBeanList.getList());
            //Validate and save the answers, then get the nquestions to display in next page
            if (operation.equals("SAVE") && validateAnswers(coeusDynaBeanList, request)) {
                hmQuestionnaireData = questionnaireHandler.saveAndGetNextQuestions(questionnaireModuleObject,
                        moduleKey, cvData);
                String mess = (String) hmQuestionnaireData.get("message");
                if (mess != null && mess.equals("COMPLETED")) {
                   //start
        // NEW MENU CHANGE  
               request.removeAttribute("QnrNotCompleted");             
        // NEW MENU CHANGE     
                    int ansCount=0;
                     HashMap hmData2 = new HashMap();
          hmData2.put("moduleItemKey",moduleKey);
         hmData2.put("questionnaireId", questionnaireModuleObject.getQuestionnaireId());
        hmData2.put("moduleItemCode", questionnaireModuleObject.getModuleItemCode());
        hmData2.put("moduleSubItemCode",questionnaireModuleObject.getModSubCode());
        hmData2.put("moduleSubItemKey", questionnaireModuleObject.getModuleSubItemKey());
        Hashtable htanswerYes = (Hashtable) txnBean.getResults(request, "getQuestionAnswerYes", hmData2);      
        HashMap resultMap=(HashMap)htanswerYes.get("getQuestionAnswerYes");
        if (resultMap!=null)
        {
        ansCount=Integer.parseInt((resultMap.get("ll_count")).toString());
        }
        if (ansCount>0){           
              String annualQstnFlag="true";
        request.getSession().setAttribute("annualQstnFlag", annualQstnFlag);
                }else{
                 String annualQstnFlag="false";
        request.getSession().setAttribute("annualQstnFlag", annualQstnFlag);
                }
    // NEW MENU CHANGE  
        String COIDisclNumber = (String)session.getAttribute("DisclNumber");
        Integer COISeqNumber = (Integer)session.getAttribute("DisclSeqNumber");      
        setApprovedDisclosureDetails(COIDisclNumber,COISeqNumber,personId,request);
        coiMenuDataSaved(COIDisclNumber,COISeqNumber,personId,request);// saved menu
   // NEW MENU CHANGE 
                    //end
                    //  request.setAttribute("COMPLETED", "COMPLETED");
                    return actionMapping.findForward("continue");
                }

            } //For getting the previous page questions
            else if (operation.equals("PREVIOUS")) {
                hmQuestionnaireData = questionnaireHandler.getPreviousQuestions(questionnaireModuleObject,
                        moduleKey);
            } //Modify the completed questionnaire and get the first page with answers to edit
            else if (operation.equals("MODIFY")) {
                hmQuestionnaireData = questionnaireHandler.restartModifyQuestionnaire(questionnaireModuleObject,
                        moduleKey, MODIFY);
            } // delete all the answers for the questionnaire and start from the first page.
            else if (operation.equals("START_OVER")) {
                hmQuestionnaireData = questionnaireHandler.restartModifyQuestionnaire(questionnaireModuleObject,
                        moduleKey, RESTART);
            } else {
                return actionMapping.findForward("success");
            }
            session.setAttribute("questionnaireInfo", hmQuestionnaireData);
            Vector vecQuestionnaireData = (Vector) hmQuestionnaireData.get(DATA_OBJECT);
            vecQuestionnaireQuestions = (Vector) vecQuestionnaireData.get(0);
            vecQuestionnaireQuestions = convertBeanToDynaBean(vecQuestionnaireQuestions,
                    request, coeusDynaBeanList);
            //Code commented and added for coeus4.3 questionnaire enhancements case#2946 - ends
            coeusDynaBeanList.setList(vecQuestionnaireQuestions);
//                }

        }
  //   PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
//        String personId=person.getPersonID();
                String disclosureNumber=null;
        if (session.getAttribute("param1")!=null)
        {
         disclosureNumber=(String)session.getAttribute("param1");
        }
        else
        {
        CoiDisclosureBean disclosureBean=getApprovedDisclosureBean(personId,request);
        disclosureNumber=disclosureBean.getCoiDisclosureNumber();
        }
        Integer sequenceNumber=null;
        if(request.getParameter("currentSequence")!=null){
        sequenceNumber=Integer.parseInt(request.getParameter("currentSequence"));
        session.setAttribute("currentSequence", sequenceNumber);
        setApprovedDisclosureDetails(disclosureNumber,sequenceNumber,personId,request);
        }else if(session.getAttribute("currentSequence")!=null){
        sequenceNumber=(Integer) session.getAttribute("currentSequence");
        setApprovedDisclosureDetails(disclosureNumber,sequenceNumber,personId,request);
        }
        
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems", CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
        mapMenuList.put("menuCode", session.getAttribute("menuCode"));
        readSavedStatus(webTxnBean, (String) session.getAttribute("actionFrom"), request);
        return actionMapping.findForward("success");
    }

    /**This method gets the Questionnaire Questions for a particular questionnaireId
     * @throws Exception
     * @return Vector of dynabeans
     */
    public Vector getQuestionnaireQuestions(int questionnaireId, HashMap hmQuestionnaireData,
            HttpServletRequest request, CoeusDynaBeansList coeusDynaBeanList) throws Exception {
        Map mpQuestionnaireId = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        mpQuestionnaireId.put("questionnaireId", new Integer(questionnaireId));
        Hashtable htQuestionnaireData =
                (Hashtable) webTxnBean.getResults(request, GET_QUESTIONNAIRE_QUESTIONS, mpQuestionnaireId);
        Vector vecQuestionsData =
                (Vector) htQuestionnaireData.get(GET_QUESTIONNAIRE_QUESTIONS);
        Vector vecAnswersData = getQuestionnaireAnswers(hmQuestionnaireData, request);
        Vector vecQuestionnaireList = (Vector) session.getAttribute("questionnaireList" + session.getId());
        if (vecQuestionsData != null && vecQuestionsData.size() > 0) {
            for (int index = 0; index < vecQuestionsData.size(); index++) {
                DynaValidatorForm dynaQuestions =
                        (DynaValidatorForm) vecQuestionsData.get(index);
                String questionId = (String) dynaQuestions.get("questionId");
                String questionNumber = (String) dynaQuestions.get("questionNumber");
                if (vecAnswersData != null && vecAnswersData.size() > 0) {
                    coeusDynaBeanList.setBeanList(vecAnswersData);
                    for (int count = 0; count < vecAnswersData.size(); count++) {
                        DynaValidatorForm dynaAnswers =
                                (DynaValidatorForm) vecAnswersData.get(count);
                        String ansQuestionId = (String) dynaAnswers.get("questionId");
                        String ansQuestionNumber = (String) dynaAnswers.get("questionNumber");
                        String ansQuestionnaireId = (String) dynaAnswers.get("questionaireId");
                        if (String.valueOf(questionnaireId).equals(ansQuestionnaireId)) {
                            if (ansQuestionId.equals(questionId)
                                    && ansQuestionNumber.equals(questionNumber)) {
                                String answer = (String) dynaAnswers.get("answer");
                                dynaQuestions.set("answer", answer);
                                dynaQuestions.set("answerNumber", dynaAnswers.get("answerNumber"));
                                dynaQuestions.set("answerDescription", dynaAnswers.get("answerDescription"));
                                dynaQuestions.set("acType", TypeConstants.UPDATE_RECORD);
                                dynaQuestions.set("awUpdateTimestamp", dynaAnswers.get("updateTimestamp"));
                            }
                        }
                    }//end inner for
                } else {
                    //dynaQuestions.set("answerNumber",dynaQuestions.get("questionNumber"));
                    dynaQuestions.set("acType", TypeConstants.INSERT_RECORD);
                }
            }//end for
        }//end If
        return vecQuestionsData;
    }

    /**This method is to get the Questionnaire Answers
     * @param proposalNumber
     * @throws Exception
     * @return vector of dynaBeans
     */
    private Vector getQuestionnaireAnswers(HashMap hmQuestionnaireData, HttpServletRequest request) throws Exception {
        Map mpQuestionData = new HashMap();
        HttpSession session = request.getSession();
        mpQuestionData.put(MODULE_ITEM_CODE, new Integer(MODULE_ITEM_CODE_VALUE));
        mpQuestionData.put(MODULE_SUB_ITEM_CODE, new Integer(MODULE_SUB_ITEM_CODE_VALUE));
        mpQuestionData.put("moduleItemKey", session.getAttribute("proposalNumber" + session.getId()));
        mpQuestionData.put("moduleSubItemKey", session.getAttribute("proposalNumber" + session.getId()));
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htQuestionnaireAnswers =
                (Hashtable) webTxnBean.getResults(request, GET_QUESTIONNAIRE_ANSWERS, hmQuestionnaireData);
        Vector vecQuestionnaireAnswers = (Vector) htQuestionnaireAnswers.get(GET_QUESTIONNAIRE_ANSWERS);
        return vecQuestionnaireAnswers;
    }

    /**
     * This method saves the form Data to the osp$questionnaire_ans_header table
    and osp$questionaire_answer table
     * @param proposalNumber
     * @throws Exception
     */
    public void performSaveAction(HashMap hmQuestionnaireData,
            CoeusDynaBeansList coeusDynaBeanList, HttpServletRequest request) throws Exception {
        Vector vecQuestionsList = (Vector) coeusDynaBeanList.getList();
        String questionnaireCompletionId = EMPTY_STRING;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        Vector vecAnswerHeader = getQuestionnaireAnsHeader(hmQuestionnaireData, request);
        if (vecAnswerHeader == null || vecAnswerHeader.size() == 0) {
            questionnaireCompletionId = generateQuestionnaireCompletion(request);
            vecAnswerHeader = prepareDynaAnswerHeader(hmQuestionnaireData, coeusDynaBeanList, request);
        } else {
            for (int index = 0; index < vecAnswerHeader.size(); index++) {
                DynaValidatorForm dynaValidForm = (DynaValidatorForm) vecAnswerHeader.get(index);
                questionnaireCompletionId = (String) dynaValidForm.get("questionaireCompletionId");
            }
        }
        Vector vecSaveData = prepareSaveData(hmQuestionnaireData, coeusDynaBeanList, request);
        if (vecSaveData != null && vecSaveData.size() > 0) {
            vecQuestionsList = vecSaveData;
        }
        if (vecQuestionsList != null && vecQuestionsList.size() > 0) {
            String acType = EMPTY_STRING;
            String oldQuestionnaireId = EMPTY_STRING;
            if (vecAnswerHeader != null && vecAnswerHeader.size() > 0) {
                DynaValidatorForm dynaFormData = (DynaValidatorForm) vecAnswerHeader.get(0);
                String questionnaireId = (String) dynaFormData.get("questionaireId");
                oldQuestionnaireId = String.valueOf(session.getAttribute("questionnaireId" + session.getId()));
                if (!questionnaireId.equals(oldQuestionnaireId)) {
                    dynaFormData.set("acType", TypeConstants.INSERT_RECORD);
                    dynaFormData.set("questionaireId", oldQuestionnaireId);
                }
                dynaFormData.set("awUpdateTimestamp", dynaFormData.get("updateTimestamp"));
                dynaFormData.set("updateTimestamp", prepareTimeStamp().toString());
                dynaFormData.set("questionaireCompletionId", questionnaireCompletionId);
                webTxnBean.getResults(request, ADD_UPD_QUESTIONNAIRE_ANS_HEADER, dynaFormData);
            }
            for (int index = 0; index < vecQuestionsList.size(); index++) {
                DynaValidatorForm dynaFormData = (DynaValidatorForm) vecQuestionsList.get(index);
                String answer = (String) dynaFormData.get("answer");
                acType = (String) dynaFormData.get("acType");
                if (acType == null || acType.equals(EMPTY_STRING)) {
                    dynaFormData.set("acType", TypeConstants.INSERT_RECORD);
                }
                dynaFormData.set("answerNumber", ANSWER_NUMBER_VALUE);
                dynaFormData.set("questionaireCompletionId", questionnaireCompletionId);
                dynaFormData.set("updateTimestamp", prepareTimeStamp().toString());
                if (answer != null && !answer.equals(EMPTY_STRING)) {
                    webTxnBean.getResults(request, ADD_UPD_QUESTIONNAIRE_ANSWERS, dynaFormData);
                }
            }
        }
    }

    /**This methods prepares a dynavalidatorform for a new qiuestionnaire
    that has to be inserted in answer header table     *
     * @throws Exception
     * @return Vector containing dynaNewBean
     */
    public Vector prepareDynaAnswerHeader(HashMap hmQuestionnaireData,
            CoeusDynaBeansList coeusDynaBeanList, HttpServletRequest request) throws Exception {
        Vector vecQuestionAnswerHeader = new Vector();
        HttpSession session = request.getSession();
        DynaActionForm dynaFormData = coeusDynaBeanList.getDynaForm(request, "questionnaireForm");
        DynaBean dynaNewBean = ((DynaBean) dynaFormData).getDynaClass().newInstance();
        dynaNewBean.set("moduleItemKey", hmQuestionnaireData.get("moduleItemKey"));
        dynaNewBean.set("moduleSubItemKey", hmQuestionnaireData.get("moduleSubItemKey"));
        dynaNewBean.set("moduleItemCode", String.valueOf((Integer) hmQuestionnaireData.get("moduleItemCode")));
        dynaNewBean.set("moduleSubItemCode", String.valueOf(hmQuestionnaireData.get("moduleSubItemCode")));
        dynaNewBean.set("acType", TypeConstants.INSERT_RECORD);
        dynaNewBean.set("questionaireId", String.valueOf(session.getAttribute("questionnaireId" + session.getId())));
        vecQuestionAnswerHeader.addElement(dynaNewBean);
        return vecQuestionAnswerHeader;
    }

    /**This method is to get the data from OSP$Questionnaire_answerHeader Table
     * @throws Exception
     * @return
     */
    public Vector getQuestionnaireAnsHeader(HashMap hmQuestionnaireData, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        String questionnaireId = String.valueOf(session.getAttribute("questionnaireId" + session.getId()));
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htQuestionnaireAnswers =
                (Hashtable) webTxnBean.getResults(request, GET_QUESTIONNAIRE_ANS_HEADER, hmQuestionnaireData);
        Vector vecQuestionnaireAnswers = (Vector) htQuestionnaireAnswers.get(GET_QUESTIONNAIRE_ANS_HEADER);
        Vector vecFilterAnsHeader = null;
        if (vecQuestionnaireAnswers != null && vecQuestionnaireAnswers.size() > 0) {
            vecFilterAnsHeader = new Vector();
            for (int index = 0; index < vecQuestionnaireAnswers.size(); index++) {
                DynaValidatorForm dynaFormData = (DynaValidatorForm) vecQuestionnaireAnswers.get(index);
                String questnaireId = (String) dynaFormData.get("questionaireId");
                if (questnaireId.equals(questionnaireId)) {
                    vecFilterAnsHeader.addElement(dynaFormData);
                }
            }
        }
        return vecFilterAnsHeader;
    }

    /**This method is used to validate the form data
     * @throws Exception
     * @return true if there are no errors.
     */
    private boolean validateAnswers(CoeusDynaBeansList coeusDynaBeanList,
            HttpServletRequest request) throws Exception {
        Vector vecQuestionnaireData = (Vector) coeusDynaBeanList.getList();
        String datePattern = "MM/dd/yyyy";
        //String annualQstnFlag = "false";
        //Code added for coeus4.3 questionnaire enhancements case#2946 - starts
        ActionMessages actionMessages = new ActionMessages();
        LinkedHashMap lhmAnsweredQuestions = new LinkedHashMap();
        LinkedHashMap lhmQuestions = new LinkedHashMap();
        int questionNumber = 0;
        //Code added for coeus4.3 questionnaire enhancements case#2946 - ends
        if (vecQuestionnaireData != null && vecQuestionnaireData.size() > 0) {
            for (int index = 0; index < vecQuestionnaireData.size(); index++) {
                DynaBean dynaFormData = (DynaBean) vecQuestionnaireData.get(index);
                //Code added for coeus4.3 questionnaire enhancements case#2946 - starts
                String answer = (String) dynaFormData.get("answer");
                String dataType = (String) dynaFormData.get("answerDataType");

               // if(answer != null && answer.toLowerCase().equals("y")) {
                //    annualQstnFlag = "true";
               // }
                String key = "" + questionNumber;
                if (((Integer) dynaFormData.get("answerNumber")).intValue() == 0) {
                    questionNumber++;
                    key = "" + questionNumber;
                    if (lhmQuestions.get(key) == null) {
                        lhmQuestions.put(key, dynaFormData.get("description"));
                    }
                }
                //Code added for coeus4.3 questionnaire enhancements case#2946 - ends
                String questionId = ((Integer) dynaFormData.get("questionNumber")).toString();
                if (dataType != null && answer != null
                        && answer.trim().length() > 0) {
                    //Code added for coeus4.3 questionnaire enhancements case#2946
                    lhmAnsweredQuestions.put(key, "Answered");
                    if (dataType.equalsIgnoreCase(NUMBER_DATA_TYPE)) {
                        try {
                            Integer.parseInt(answer);
                        } catch (NumberFormatException nfe) {
                            actionMessages.add("numberFormatException",
                                    new ActionMessage(NUMBER_FORMAT_EXCEPTION, questionId));
                            saveMessages(request, actionMessages);
                            return false;
                        }
                    } else if (dataType.equalsIgnoreCase(DATE_DATA_TYPE)) {
                        String resultDate = EMPTY_STRING;
                        DateUtils dtUtils = new DateUtils();
                        if (answer != null && !answer.trim().equals(EMPTY_STRING)) {
                            resultDate = dtUtils.formatDate(answer, DATE_SEPARATERS, SIMPLE_DATE_FORMAT);
                            if (resultDate == null) {
                                actionMessages.add("notValidDate",
                                        new ActionMessage(NOT_VALID_DATE, questionId));
                                saveMessages(request, actionMessages);
                                return false;
                            }
                        }
                    }
                } else {
                    if (lhmAnsweredQuestions.get(key) == null) {
                        lhmAnsweredQuestions.put(key, null);
                    }
                }
            }//End For
            //Code added for coeus4.3 questionnaire enhancements case#2946 - starts
            //To check the non answered questions
            if (lhmAnsweredQuestions.size() > 0) {
                Set keySet = lhmAnsweredQuestions.keySet();
                Object[] objQuestions = keySet.toArray();
                for (int index = 0; index < objQuestions.length; index++) {
                    if (lhmAnsweredQuestions.get(objQuestions[index]) == null) {
                        actionMessages.add("answerMandatory",
                                new ActionMessage("questionnaire.answerMandatory",
                                objQuestions[index] + " : ", lhmQuestions.get(objQuestions[index])));
                        saveMessages(request, actionMessages);
                        return false;
                    }
                }
            }
            //Code added for coeus4.3 questionnaire enhancements case#2946 - ends
        }//End If
// if(request.getAttribute("questionAnswerYes")!=null && request.getAttribute("questionAnswerYes").equals("true")){
//       String annualQstnFlag="true";
//        request.getSession().setAttribute("annualQstnFlag", annualQstnFlag);}
        return true;
    }

    /**
     * This method returns all the questionnaire associated
     * with a module and its sub module
     * @param questionnaireId
     * @throws Exception
     */
    public void getQuestionnaireList(int questionnaireId, HashMap hmQuestionnaireData,
            HttpServletRequest request) throws Exception {
        Map mpQuestionData = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        mpQuestionData.put("moduleItemCode", hmQuestionnaireData.get("moduleItemCode"));
        mpQuestionData.put("moduleSubItemCode", hmQuestionnaireData.get("moduleSubItemCode"));
        Hashtable htQuestionnaireList =
                (Hashtable) webTxnBean.getResults(request, "getQuestionnaireListForModule", mpQuestionData);
        Vector vecQuestionnaireList = (Vector) htQuestionnaireList.get("getQuestionnaireListForModule");
        String questionnaireLabel = EMPTY_STRING;
        if (vecQuestionnaireList != null && vecQuestionnaireList.size() > 0) {
            for (int index = 0; index < vecQuestionnaireList.size(); index++) {
                DynaValidatorForm dynaFormData =
                        (DynaValidatorForm) vecQuestionnaireList.get(index);
                String strQuestionnaireId = (String) dynaFormData.get("questionaireId");
                if (String.valueOf(questionnaireId).equals(strQuestionnaireId)) {
                    questionnaireLabel = (String) dynaFormData.get("questionaireLabel");
                }
            }
        }
        session.setAttribute("questionaireLabel", questionnaireLabel);
    }

    /**This method prepares the form data to be saved or updated
     * @throws Exception
     * @return vecSaveData
     */
    public Vector prepareSaveData(HashMap hmQuestionnaireData,
            CoeusDynaBeansList coeusDynaBeanList, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        int questionnaireId = ((Integer) session.getAttribute("questionnaireId" + session.getId())).intValue();
        Vector vecOldData = getQuestionnaireQuestions(questionnaireId, hmQuestionnaireData,
                request, coeusDynaBeanList);
        Vector vecFormData = (Vector) coeusDynaBeanList.getList();
        Vector vecSaveData = null;
        if (vecFormData != null && vecFormData.size() > 0) {
            vecSaveData = new Vector();
            for (int index = 0; index < vecFormData.size(); index++) {
                DynaValidatorForm dynaFormData =
                        (DynaValidatorForm) vecFormData.get(index);
                if (isDataChanged(dynaFormData, questionnaireId, hmQuestionnaireData,
                        coeusDynaBeanList, request)) {
                    vecSaveData.addElement(dynaFormData);
                }
            }
        }
        return vecSaveData;
    }

    /**This method is to check whether the form data has been changed or not
     * @param dynaForm
     * @param questionnaireId
     * @throws Exception
     * @return
     */
    public boolean isDataChanged(DynaValidatorForm dynaForm, int questionnaireId,
            HashMap hmQuestionnaireData, CoeusDynaBeansList coeusDynaBeanList,
            HttpServletRequest request) throws Exception {

        String answer = (String) dynaForm.get("answer");
        String questionNumber = (String) dynaForm.get("questionNumber");
        Vector vecServerData = getQuestionnaireQuestions(questionnaireId, hmQuestionnaireData,
                request, coeusDynaBeanList);
        boolean isDataChanged = false;
        if (vecServerData != null && vecServerData.size() > 0) {
            String strAnswer = EMPTY_STRING;
            String strQuestionNumber = EMPTY_STRING;
            for (int index = 0; index < vecServerData.size(); index++) {
                DynaValidatorForm dynaOldData = (DynaValidatorForm) vecServerData.get(index);
                strAnswer = (String) dynaOldData.get("answer");
                strQuestionNumber = (String) dynaOldData.get("questionNumber");
                if (strQuestionNumber.equals(questionNumber)) {
                    if (strAnswer != null && !strAnswer.equals(EMPTY_STRING)) {
                        if (!strAnswer.equals(answer)) {
                            isDataChanged = true;
                        }
                    }
                }
            }
        }
        return isDataChanged;
    }

    /** Manufacture the LockBean based on the parameter passed by the specific module
     *say, Propsoal, Protocol, Budget etc.
     *@param UserInfoBean, Proposal number
     *@returns LockBean
     *@throws Exception
     */
    protected String getMode(String mode, Object obj) throws Exception {
        if (mode != null && !mode.equals(EMPTY_STRING)) {
            if (mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)) {
                mode = CoeusLiteConstants.DISPLAY_MODE;
            }
        } else {
            mode = CoeusLiteConstants.MODIFY_MODE;
        }
        return mode;
    }

    /** Read the save status for the given proposal number
     *@throws Exception
     */
    protected void readSavedStatus(WebTxnBean webTxnBean, String actionFrom,
            HttpServletRequest request) throws Exception {
        Map hmSavedData = null;
        Hashtable htReqData = null;
        HashMap hmMenuData = new HashMap();
        HashMap hmQuestionnaire = new HashMap();
        HttpSession session = request.getSession();
        String proposalNumber = EMPTY_STRING;
        String protocolNumber = EMPTY_STRING;
        String sequenceNumber = EMPTY_STRING;
        Vector menuData = null;
        if (actionFrom.equalsIgnoreCase("DEV_PROPOSAL")) {
            proposalNumber = (String) request.getSession().getAttribute("proposalNumber" + request.getSession().getId());
            menuData = (Vector) request.getSession().getAttribute(CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
            hmMenuData.put("proposalNumber", proposalNumber);
            hmQuestionnaire.put("proposalNumber", proposalNumber);
        } else if (actionFrom.equalsIgnoreCase("PROTOCOL")) {
            protocolNumber = (String) session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER + session.getId());
            sequenceNumber = (String) session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER + session.getId());
            menuData = (Vector) request.getSession().getAttribute(CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
            hmQuestionnaire.put("proposalNumber", protocolNumber);
            hmMenuData.put("protocolNumber", protocolNumber);
            hmMenuData.put("sequenceNumber", new Integer(Integer.parseInt(sequenceNumber)));
        }

        if (webTxnBean == null) {
            webTxnBean = new WebTxnBean();
        }
        if (menuData != null && menuData.size() > 0) {
            hmSavedData = new HashMap();
            htReqData = new Hashtable();

            MenuBean dataBean = null;
            String menuId = EMPTY_STRING;
            String strValue = EMPTY_STRING;
            boolean isDynamic = false;
            HashMap hmReturnData = null;
            for (int index = 0; index < menuData.size(); index++) {
                dataBean = (MenuBean) menuData.get(index);
                /**Checkk for the dynamically created menu's. For example
                 *Questionnaire Menu. the dynamicId specifies the dynamic menu ids
                 *generated. At present it gets the dynamic Id for the questionnaire
                 *Menu and makes server call to show the saved questionnaire menu
                 */
                if (dataBean.getDynamicId() != null && !dataBean.getDynamicId().equals(EMPTY_STRING)) {
                    menuId = dataBean.getDynamicId();
                    isDynamic = true;
                } else {
                    menuId = dataBean.getMenuId();
                    isDynamic = false;
                }
                hmQuestionnaire.put("menuId", menuId);
                hmMenuData.put("menuId", menuId);
                if (isDynamic) {
                    htReqData = (Hashtable) webTxnBean.getResults(request, "getSavedQuestionnaireData", hmQuestionnaire);
                    hmReturnData = (HashMap) htReqData.get("getSavedQuestionnaireData");
                } else {
                    if (actionFrom.equalsIgnoreCase("DEV_PROPOSAL")) {
                        htReqData = (Hashtable) webTxnBean.getResults(request, "getSavedProposalMenuData", hmMenuData);
                        hmReturnData = (HashMap) htReqData.get("getSavedProposalMenuData");
                    } else if (actionFrom.equalsIgnoreCase("PROTOCOL")) {
                        htReqData = (Hashtable) webTxnBean.getResults(request, "getSavedProtocolData", hmMenuData);
                        hmReturnData = (HashMap) htReqData.get("getSavedProtocolData");
                    }

                }
                if (hmReturnData != null) {
                    strValue = (String) hmReturnData.get("AV_SAVED_DATA");
                    int value = Integer.parseInt(strValue);
                    if (value == 1) {
                        dataBean.setDataSaved(true);
                    } else if (value == 0) {
                        dataBean.setDataSaved(false);
                    }
                }
            }
            if (actionFrom.equalsIgnoreCase("DEV_PROPOSAL")) {
                request.getSession().removeAttribute(CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
                request.getSession().setAttribute(CoeusliteMenuItems.PROPOSAL_MENU_ITEMS, menuData);
            } else if (actionFrom.equalsIgnoreCase("PROTOCOL")) {
                request.getSession().removeAttribute(CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
                request.getSession().setAttribute(CoeusliteMenuItems.PROTOCOL_MENU_ITEMS, menuData);
            }

        }
    }

    /**This method generates a unique Qustionnaire Completion Id
     * @throws Exception
     * @return questionnaireCompId
     */
    public String generateQuestionnaireCompletion(HttpServletRequest request) throws Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htQuestionnaireCompId = (Hashtable) webTxnBean.getResults(request, "generateCompletionId", null);
        HashMap hmQuestionnaireCompId = (HashMap) htQuestionnaireCompId.get("generateCompletionId");
        String questionnaireCompId = (String) hmQuestionnaireCompId.get("questionnaireCompletionId");
        return questionnaireCompId;
    }

    /**
     * Code added for coeus4.3 questionnaire enhancements case#2946
     * Convert the QuestionnaireQuestionsBean to DynaForm
     * @param vecQuestionnaireQuestions Vector Question datas
     * @param request HttpServletRequest
     * @param coeusDynaBeanList CoeusDynaBeansList
     * @throws java.lang.Exception
     * @return Vector dynabean datas
     */
    private Vector convertBeanToDynaBean(Vector vecQuestionnaireQuestions,
            HttpServletRequest request, CoeusDynaBeansList coeusDynaBeanList) throws Exception {
        Vector vecQuestionsFormData = new Vector();
        BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
        if (vecQuestionnaireQuestions != null && vecQuestionnaireQuestions.size() > 0) {
            for (int index = 0; index < vecQuestionnaireQuestions.size(); index++) {
                QuestionnaireQuestionsBean questionnaireQuestionsBean =
                        (QuestionnaireQuestionsBean) vecQuestionnaireQuestions.get(index);
                DynaActionForm dynaFormData = coeusDynaBeanList.getDynaForm(request, "questionnaireForm");
                beanUtilsBean.copyProperties(dynaFormData, questionnaireQuestionsBean);
                vecQuestionsFormData.add(dynaFormData);
            }
        }
        return vecQuestionsFormData;
    }

    /**
     * Code added for coeus4.3 questionnaire enhancements case#2946
     * Convert the DynaForm to QuestionnaireQuestionsBean
     * @param vecQuestionnaireQuestions Vector
     * @throws java.lang.Exception
     * @return CoeusVector
     */
    private CoeusVector convertDynaBeanToBean(Vector vecQuestionnaireQuestions) throws Exception {
        CoeusVector cvQuestionsFormData = new CoeusVector();
        BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
        if (vecQuestionnaireQuestions != null && vecQuestionnaireQuestions.size() > 0) {
            for (int index = 0; index < vecQuestionnaireQuestions.size(); index++) {
                DynaActionForm dynaActionForm =
                        (DynaActionForm) vecQuestionnaireQuestions.get(index);
                QuestionnaireQuestionsBean questionnaireQuestionsBean =
                        new QuestionnaireQuestionsBean();
                beanUtilsBean.copyProperties(questionnaireQuestionsBean, dynaActionForm);
                cvQuestionsFormData.add(questionnaireQuestionsBean);
            }
        }
        return cvQuestionsFormData;
    }
    
     private void setApprovedDisclosureDetails(String coiDisclosureNumber,Integer sequenceNumber,String personId,HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();  //added by Vineetha
        HashMap hmData = new HashMap();
        hmData.put("coiDisclosureNumber", coiDisclosureNumber);
         if(coiDisclosureNumber==null)
        { hmData.put("sequenceNumber",0);}
        else
        {hmData.put("sequenceNumber", sequenceNumber);}
        hmData.put("personId", personId);
        WebTxnBean webTxn = new WebTxnBean();
 /* **
        Vector statusDispDet = new Vector();
        Hashtable statusData = (Hashtable) webTxn.getResults(request, "getDisclDispositionStatus", hmData);
        statusDispDet = (Vector) statusData.get("getDisclDispositionStatus");
        if (statusDispDet != null && statusDispDet.size() > 0) {
            request.setAttribute("statusDispDetView", statusDispDet);
        }
  ** */
        Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData);
        Vector DisclDet = (Vector) DisclData.get("getDisclBySequnce");
        if (DisclDet != null && DisclDet.size() > 0) {
            request.setAttribute("ApprovedDisclDetView", DisclDet);
/* **            for (Iterator it = DisclDet.iterator(); it.hasNext();) {
                CoiDisclosureBean object = (CoiDisclosureBean) it.next();
                if(object.getCertificationTimestamp()!=null){
                     request.setAttribute("isCertified", true);
                }else{
                    request.setAttribute("isCertified", false);
                }
            }
 ** */
        }
/* **
        DisclData = (Hashtable) webTxn.getResults(request, "getDisclStatus", hmData);
        Vector statusDet = (Vector) DisclData.get("getDisclStatus");
        if (DisclDet != null && DisclDet.size() > 0) {
            request.setAttribute("statusDetView", statusDet);
        }
** */
             //added by Vineetha
      hmData = new HashMap();
        hmData.put("personId", personId);
        Hashtable htPersonData = (Hashtable) webTxn.getResults(request, "getPersonDetails", hmData);
        Vector personDatas = (Vector) htPersonData.get("getPersonDetails");
        if (personDatas != null && personDatas.size() > 0) {
            PersonInfoBean personInfoBean = (PersonInfoBean) personDatas.get(0);

            //added by Vineetha
              request.setAttribute("PersonDetails", personDatas);
            session.setAttribute("person", personInfoBean);
    }

    }

        //get Approved DisclosureBean
    public CoiDisclosureBean getApprovedDisclosureBean(String personId,HttpServletRequest request)throws Exception{
        HashMap hmData = new HashMap();
        hmData.put("personId", personId);
        WebTxnBean webTxn = new WebTxnBean();
        Vector apprvdDiscl = null;
        CoiDisclosureBean apprvdDisclosureBean = new CoiDisclosureBean();
        Hashtable apprvdDisclDet = (Hashtable) webTxn.getResults(request, "getApprovedDisclosure", hmData);
        apprvdDiscl = (Vector) apprvdDisclDet.get("getApprovedDisclosure");
        if (apprvdDiscl != null && apprvdDiscl.size() > 0) {
            apprvdDisclosureBean = (CoiDisclosureBean) apprvdDiscl.get(0);
            request.setAttribute("apprvdDisclosureBean", apprvdDisclosureBean);
            request.getSession().setAttribute("disclosureBeanSession", apprvdDisclosureBean);
        }
        return apprvdDisclosureBean;
}
}




