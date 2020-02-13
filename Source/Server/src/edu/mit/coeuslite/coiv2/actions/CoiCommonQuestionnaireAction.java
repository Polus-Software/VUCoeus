/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;
import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireQuestionsBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean;
import edu.mit.coeus.questionnaire.utils.QuestionnaireHandler;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.utils.CoeusDynaBeansList;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.mit.coeuslite.coiv2.beans.CoiInfoBean;
import edu.mit.coeuslite.utils.SessionConstants;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
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
 * @author UnNamed
 */
public class CoiCommonQuestionnaireAction  extends COIBaseAction{
    private static final String GET_QUESTIONNAIRE_DATA = "/getCoiCommonQnr";
    private static final String SAVE_QUESTIONNAIRE_DATA = "/saveCoiCommonQnr";
    private static final String SUCCESS = "success";
    private static final int MODULE_ITEM_CODE_VALUE = 3 ;
    private static final String MODULE_ITEM_CODE = "moduleItemCode" ;
    private static final int MODULE_SUB_ITEM_CODE_VALUE = 0 ;
    private static final String MODULE_SUB_ITEM_CODE = "moduleSubItemCode" ;
    private static final String EMPTY_STRING = "";
    private static final String ANSWER_NUMBER_VALUE = "1" ;
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private static final String NUMBER_DATA_TYPE = "NUMBER" ;
    private static final String DATE_DATA_TYPE = "DATE" ;
    private static final String NOT_VALID_DATE = "error.questionnaire_inValidDateFormat" ;
    private static final String NUMBER_FORMAT_EXCEPTION = "error.questionnaire_inValidNumberFormat" ;
    private static final String GET_QUESTIONNAIRE_QUESTIONS = "getQuestionnaireQuestions";
    private static final String GET_QUESTIONNAIRE_ANSWERS = "getQuestionnaireAnswers" ;
    private static final String GET_QUESTIONNAIRE_ANS_HEADER = "getQuestionnaireAnsHeader" ;
    private static final String ADD_UPD_QUESTIONNAIRE_ANSWERS = "addUpdQuestionnaireAnswers" ;
    private static final String ADD_UPD_QUESTIONNAIRE_ANS_HEADER = "addUpdQuestionnaireAnswerHeader" ;   
    private static final String DATA_OBJECT = "dataObject";
    private static final char RESTART = 'S';
    private static final char MODIFY = 'M';
    private static String ANNUAL ="Annual";
    private static String REVISION ="Revision";
    private static String PROPOSAL ="Proposal";
    private static String PROTOCOL ="Protocol";
    private static String IACUCPROTOCOL ="IACUCProtocol"; 
    private static String AWARD ="Award";
    private static String TRAVEL ="Travel";

    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        CoeusDynaBeansList coeusDynaBeanList = (CoeusDynaBeansList)actionForm ;       
        String navigator = SUCCESS;
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmQuestionnaireData = new HashMap() ; 
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
        QuestionnaireHandler questionnaireHandler = new QuestionnaireHandler(userInfoBean.getUserId());
        int questionnaireID = 0 ;
        Integer questnnaireId = (Integer)request.getAttribute("questionnaireId");
        CoiInfoBean coiInfoBean = (CoiInfoBean)session.getAttribute("CoiInfoBean");
        if(coiInfoBean==null){
          coiInfoBean =new CoiInfoBean();
        } 
        if(questnnaireId!=null && !questnnaireId.equals(EMPTY_STRING)){
            questionnaireID  = questnnaireId;
            session.setAttribute("questionnaireId"+session.getId() , new Integer(questionnaireID));
            session.setAttribute("coiQuestionnaireId",questionnaireID);
            questionnaireModuleObject.setQuestionnaireId(questnnaireId);
        }            
        if(request.getAttribute("questionaireLabel")!=null && !request.getAttribute("questionaireLabel").equals(EMPTY_STRING)){
            session.setAttribute("questionaireLabel" , request.getAttribute("questionaireLabel"));
        }
       int qnrListSize = 0;

        Vector qstnnrIdList = (Vector)session.getAttribute("qstnnrIdList");

        if(qstnnrIdList != null) {
            qnrListSize =   qstnnrIdList.size();
        }
        request.setAttribute("qnrListSize", qnrListSize);

        if(qnrListSize == 0 || qnrListSize > 1) {            
            session.setAttribute("MenuId", "ANN_DISCL");
            session.setAttribute("questionaireLabel", "Annual Disclosure Certification");
            if (request.getAttribute("actionFrom") != null && !request.getAttribute("actionFrom").equals(EMPTY_STRING)) {
                session.setAttribute("actionFrom", (String) request.getAttribute("actionFrom"));
            }
            return actionMapping.findForward("success");
        }         
         String moduleKey = EMPTY_STRING;
         Integer moduleSubItemKey = null;
         Integer moduleSubItemCode = null;
         String projectType = coiInfoBean.getProjectType();
         moduleKey = coiInfoBean.getDisclosureNumber();
         moduleSubItemKey = coiInfoBean.getSequenceNumber();
         questionnaireModuleObject.setModuleItemKey(moduleKey);
         questionnaireModuleObject.setModuleItemCode(ModuleConstants.ANNUAL_COI_DISCLOSURE);
         if(moduleSubItemKey!=null){
         questionnaireModuleObject.setModuleSubItemKey(moduleSubItemKey.toString());
         }
       if (projectType!=null && projectType.equalsIgnoreCase(ANNUAL)){
           moduleSubItemCode = ModuleConstants.COI_EVENT_ANNUAL;
           questionnaireModuleObject.setModuleSubItemCode(ModuleConstants.COI_EVENT_ANNUAL);
       }
       if (projectType!=null && projectType.equalsIgnoreCase(REVISION)){
           moduleSubItemCode = ModuleConstants.COI_EVENT_REVISION;
           questionnaireModuleObject.setModuleSubItemCode(ModuleConstants.COI_EVENT_REVISION);
       } 
       if (projectType!=null && projectType.equalsIgnoreCase(PROPOSAL)){ 
           moduleSubItemCode = ModuleConstants.COI_EVENT_PROPOSAL;
           questionnaireModuleObject.setModuleSubItemCode(ModuleConstants.COI_EVENT_PROPOSAL);
       } 
       if (projectType!=null && projectType.equalsIgnoreCase(PROTOCOL)){
           moduleSubItemCode = ModuleConstants.COI_EVENT_PROTOCOL;
           questionnaireModuleObject.setModuleSubItemCode(ModuleConstants.COI_EVENT_PROTOCOL);
       } 
       if (projectType!=null && projectType.equalsIgnoreCase(IACUCPROTOCOL)){
           moduleSubItemCode = ModuleConstants.COI_EVENT_IACUC;
           questionnaireModuleObject.setModuleSubItemCode(ModuleConstants.COI_EVENT_IACUC);
       } 
       if (projectType!=null && projectType.equalsIgnoreCase(AWARD)){
           moduleSubItemCode = ModuleConstants.COI_EVENT_AWARD;
           questionnaireModuleObject.setModuleSubItemCode(ModuleConstants.COI_EVENT_AWARD);
       } 
       if (projectType!=null && projectType.equalsIgnoreCase(TRAVEL)){
           moduleSubItemCode = ModuleConstants.COI_EVENT_TRAVEL;
           questionnaireModuleObject.setModuleSubItemCode(ModuleConstants.COI_EVENT_TRAVEL);
       }
        Vector vecQuestionnaireQuestions = null ;
        String newVersionMsgDisplayed = request.getParameter("newVersionMsgDisplayed");
        // sub header details S T A R T S      
       PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = person.getPersonID();
        getCoiPersonDetails(personId,request);
        String disclosureNumber=null;
        Integer sequenceNumber=null;
        disclosureNumber = coiInfoBean.getDisclosureNumber();
        sequenceNumber =coiInfoBean.getSequenceNumber();
        if(sequenceNumber == null){
            sequenceNumber =coiInfoBean.getApprovedSequence();
        }
        setApprovedDisclosureDetails(disclosureNumber,sequenceNumber,personId,request);     
       // sub header details E N D S
        if(actionMapping.getPath().equals(GET_QUESTIONNAIRE_DATA)){
            session.removeAttribute("modifyCompletedQuestionnaire");            
            String completionFlag = request.getParameter("completed");            
            String applicableModuleItemKey = moduleKey;            
            questionnaireModuleObject.setApplicableSubmoduleCode(moduleSubItemCode);
            questionnaireModuleObject.setQuestionnaireCompletionFlag(completionFlag);
            if("Y".equals(completionFlag)){
                questionnaireModuleObject.setApplicableModuleItemKey(applicableModuleItemKey);
                questionnaireModuleObject.setApplicableModuleSubItemKey(moduleSubItemKey);
            }else{
                questionnaireModuleObject.setApplicableModuleItemKey(questionnaireModuleObject.getModuleItemKey());
                questionnaireModuleObject.setApplicableModuleSubItemKey(moduleSubItemKey);
            }
            String mode=(String)session.getAttribute("mode"+session.getId());
            String functionType = "M";            
            QuestionnaireTxnBean questionnaireTxnBean=new QuestionnaireTxnBean();
            int versionAnswered = questionnaireTxnBean.fetchAnsweredVersionNumberOfQuestionnaire(questionnaireModuleObject);
            int latestVersion = questionnaireTxnBean.fetchMaxVersionNumberOfQuestionnaire(questionnaireModuleObject.getQuestionnaireId());                  
            if(versionAnswered != 0 && latestVersion != 1 && !"true".equalsIgnoreCase(newVersionMsgDisplayed)
                    && !(CoeusLiteConstants.DISPLAY_MODE.equalsIgnoreCase(mode) || ("display").equalsIgnoreCase(mode))){
                if(versionAnswered != latestVersion && latestVersion > versionAnswered){
                    request.setAttribute("newQuestionnaireVersion",CoeusLiteConstants.YES);  
                }
            }
            //apply questionnaire version change only for review status is in "In Progress" 
            int reviewInProgressCode = 1 ;
            int reviewCode = -1;
            Vector DisclDetView = (Vector)request.getAttribute("ApprovedDisclDetView");
            Vector DisclDet = (Vector) request.getAttribute("ApprovedDisclDet");
            if(DisclDetView != null){
                reviewCode = getReviewStatus(DisclDetView);
            }else if(DisclDet!=null){
                reviewCode = getReviewStatus(DisclDet);
            }   
            //apply questionnaire version change only for review status is in "In Progress"
            if(CoeusLiteConstants.DISPLAY_MODE.equalsIgnoreCase(mode) || ("display").equalsIgnoreCase(mode) || reviewCode != reviewInProgressCode){
                questionnaireModuleObject.setQuestionnaireVersionNumber(versionAnswered);
                session.setAttribute("questionnaireVersion", new Integer(versionAnswered));
            } else {
                questionnaireModuleObject.setQuestionnaireVersionNumber(latestVersion);
                session.setAttribute("questionnaireVersion", new Integer(latestVersion));
            }
            String moduleSubItemkey = EMPTY_STRING;
            if(coiInfoBean.getSequenceNumber()!=null){
                 moduleSubItemkey =coiInfoBean.getSequenceNumber().toString();
            
              }            
            if(versionAnswered != 0 || !(CoeusLiteConstants.DISPLAY_MODE.equalsIgnoreCase(mode) || ("display").equalsIgnoreCase(mode))){
                hmQuestionnaireData = questionnaireHandler.getQuestionnaireQuestions(questionnaireModuleObject,
                        moduleKey, functionType);
                Vector vecQuestionnaireData =(Vector) hmQuestionnaireData.get(DATA_OBJECT);
                vecQuestionnaireQuestions = (Vector) vecQuestionnaireData.get(0);               
                vecQuestionnaireQuestions = convertBeanToDynaBean(vecQuestionnaireQuestions,request, coeusDynaBeanList);
                String mess = (String) hmQuestionnaireData.get("message");
                if(mess != null && mess.equals("COMPLETED")){
                    request.setAttribute("COMPLETED", "COMPLETED");
                }
            }
            session.setAttribute("questionnaireInfo",hmQuestionnaireData);
            session.setAttribute("questionnaireModuleObject",questionnaireModuleObject);
            coeusDynaBeanList.setList(vecQuestionnaireQuestions);
            session.setAttribute("questionsList",coeusDynaBeanList);                     
            navigator = SUCCESS;
        }else if(actionMapping.getPath().equals(SAVE_QUESTIONNAIRE_DATA)){
            String operation = request.getParameter("operation");
            operation = (operation == null)? EMPTY_STRING : operation;
            request.setAttribute("operation",operation);
            questionnaireModuleObject =(QuestionnaireAnswerHeaderBean) session.getAttribute("questionnaireModuleObject");            
            Integer qnrVersion = (Integer) session.getAttribute("questionnaireVersion");
            if(qnrVersion != null){
                questionnaireModuleObject.setQuestionnaireVersionNumber(qnrVersion.intValue());
            }
            hmQuestionnaireData = (HashMap) session.getAttribute("questionnaireInfo");
            hmQuestionnaireData.put("message", null);          
          
                    if(operation.equals("SAVE") && validateAnswers(coeusDynaBeanList, request)){ 
                        CoeusVector cvData =(CoeusVector) convertDynaBeanToBean((Vector)coeusDynaBeanList.getList());
                        hmQuestionnaireData = questionnaireHandler.saveAndGetNextQuestions(questionnaireModuleObject,
                                moduleKey, cvData);
                    }
                    else if("SAVE_COMPLETE".equals(operation) && validateAnswers(coeusDynaBeanList, request)){                      
                        CoeusVector cvData =(CoeusVector) convertDynaBeanToBean((Vector)coeusDynaBeanList.getList());
                        QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean =(QuestionnaireAnswerHeaderBean) session.getAttribute("questionnaireModuleObject");
                        hmQuestionnaireData = questionnaireHandler.saveAndCompleteQuestionnaire(questionnaireModuleObject,moduleKey, cvData,questionnaireAnswerHeaderBean);
                     }           
                    else if(operation.equals("PREVIOUS")){
                        hmQuestionnaireData = questionnaireHandler.getPreviousQuestions(questionnaireModuleObject,
                                moduleKey);
                    }
                    else if(operation.equals("MODIFY")){
                        hmQuestionnaireData = questionnaireHandler.restartModifyQuestionnaire(questionnaireModuleObject,
                                moduleKey, MODIFY);
                        session.setAttribute("modifyCompletedQuestionnaire", "YES");
                    }
                    else if(operation.equals("START_OVER")){
                        hmQuestionnaireData = questionnaireHandler.restartModifyQuestionnaire(questionnaireModuleObject,
                                moduleKey, RESTART);                        
                    }
                    else {
                        return actionMapping.findForward(navigator);
                    }
                Vector vecQuestionnaireData =(Vector) hmQuestionnaireData.get(DATA_OBJECT);
                vecQuestionnaireQuestions = (Vector) vecQuestionnaireData.get(0);               
                vecQuestionnaireQuestions = convertBeanToDynaBean(vecQuestionnaireQuestions,request, coeusDynaBeanList);
                String mess = (String) hmQuestionnaireData.get("message");
                if(mess != null && mess.equals("COMPLETED")){
                    request.setAttribute("COMPLETED", "COMPLETED");
                    request.getSession().removeAttribute("disableCoiMenu");
                    navigator = "continue";
                }
                 session.setAttribute("questionnaireInfo",hmQuestionnaireData);
                 coeusDynaBeanList.setList(vecQuestionnaireQuestions); 
                 return actionMapping.findForward(navigator);
                 }  
if("true".equalsIgnoreCase(newVersionMsgDisplayed)){
            String moduleSubItemkey = EMPTY_STRING;
            if(coiInfoBean.getSequenceNumber()!=null){
                 moduleSubItemkey =coiInfoBean.getSequenceNumber().toString();            
              }     
                
        }
        return actionMapping.findForward(navigator);
    }
    
    /**This method gets the Questionnaire Questions for a particular questionnaireId
     * @throws Exception
     * @return Vector of dynabeans
     */
    public Vector getQuestionnaireQuestions(int questionnaireId , HashMap hmQuestionnaireData,
        HttpServletRequest request, CoeusDynaBeansList coeusDynaBeanList)throws Exception{
        Map mpQuestionnaireId = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
//        HttpSession session = request.getSession();
        mpQuestionnaireId.put("questionnaireId" , new Integer(questionnaireId));
        Hashtable htQuestionnaireData =
            (Hashtable)webTxnBean.getResults(request , GET_QUESTIONNAIRE_QUESTIONS , mpQuestionnaireId);
        Vector vecQuestionsData = 
            (Vector)htQuestionnaireData.get(GET_QUESTIONNAIRE_QUESTIONS);
        Vector vecAnswersData = getQuestionnaireAnswers(hmQuestionnaireData, request);
//        Vector vecQuestionnaireList = (Vector)session.getAttribute("questionnaireList"+session.getId());        
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
         CoiInfoBean coiInfoBean = (CoiInfoBean)request.getSession().getAttribute("CoiInfoBean");
        if(coiInfoBean==null){
          coiInfoBean =new CoiInfoBean();
        } 
        String projectType = coiInfoBean.getProjectType();   
        mpQuestionData.put(MODULE_ITEM_CODE , ModuleConstants.ANNUAL_COI_DISCLOSURE);
       if (projectType!=null && projectType.equalsIgnoreCase(ANNUAL)){
           mpQuestionData.put(MODULE_SUB_ITEM_CODE , ModuleConstants.COI_EVENT_ANNUAL);
       }
       if (projectType!=null && projectType.equalsIgnoreCase(REVISION)){
           mpQuestionData.put(MODULE_SUB_ITEM_CODE , ModuleConstants.COI_EVENT_REVISION);
       } 
       if (projectType!=null && projectType.equalsIgnoreCase(PROPOSAL)){ 
           mpQuestionData.put(MODULE_SUB_ITEM_CODE , ModuleConstants.COI_EVENT_PROPOSAL);
       } 
       if (projectType!=null && projectType.equalsIgnoreCase(PROTOCOL)){
           mpQuestionData.put(MODULE_SUB_ITEM_CODE , ModuleConstants.COI_EVENT_PROTOCOL);
       } 
       if (projectType!=null && projectType.equalsIgnoreCase(IACUCPROTOCOL)){
           mpQuestionData.put(MODULE_SUB_ITEM_CODE , ModuleConstants.COI_EVENT_IACUC);
       } 
       if (projectType!=null && projectType.equalsIgnoreCase(AWARD)){
           mpQuestionData.put(MODULE_SUB_ITEM_CODE , ModuleConstants.COI_EVENT_AWARD);
       } 
       if (projectType!=null && projectType.equalsIgnoreCase(TRAVEL)){
           mpQuestionData.put(MODULE_SUB_ITEM_CODE , ModuleConstants.COI_EVENT_TRAVEL);
       } 
        mpQuestionData.put("moduleItemKey" , coiInfoBean.getDisclosureNumber());
        mpQuestionData.put("moduleSubItemKey" ,coiInfoBean.getSequenceNumber());
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htQuestionnaireAnswers =
            (Hashtable)webTxnBean.getResults(request , GET_QUESTIONNAIRE_ANSWERS , hmQuestionnaireData );
        Vector vecQuestionnaireAnswers = (Vector)htQuestionnaireAnswers.get(GET_QUESTIONNAIRE_ANSWERS);
        return vecQuestionnaireAnswers ;
    }
    
    
    /**
     * This method saves the form Data to the osp$questionnaire_ans_header table 
        and osp$questionaire_answer table
     * @param proposalNumber
     * @throws Exception
     */
    public void performSaveAction(HashMap hmQuestionnaireData,
        CoeusDynaBeansList coeusDynaBeanList, HttpServletRequest request)throws Exception{
        Vector vecQuestionsList = (Vector)coeusDynaBeanList.getList();
        String questionnaireCompletionId = EMPTY_STRING ;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        Vector vecAnswerHeader = getQuestionnaireAnsHeader(hmQuestionnaireData, request);
        if(vecAnswerHeader == null || vecAnswerHeader.size() == 0){
            questionnaireCompletionId = generateQuestionnaireCompletion(request);
            vecAnswerHeader = prepareDynaAnswerHeader(hmQuestionnaireData, coeusDynaBeanList, request);
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
                if(!questionnaireId.equals(oldQuestionnaireId)){
                    dynaFormData.set("acType" ,TypeConstants.INSERT_RECORD);
                    dynaFormData.set("questionaireId" ,oldQuestionnaireId);
                }
                dynaFormData.set("awUpdateTimestamp" ,dynaFormData.get("updateTimestamp")) ;
                dynaFormData.set("updateTimestamp" , prepareTimeStamp().toString()) ;
                dynaFormData.set("questionaireCompletionId" , questionnaireCompletionId);                
                webTxnBean.getResults(request , ADD_UPD_QUESTIONNAIRE_ANS_HEADER , dynaFormData );
            }
            for(int index = 0 ; index < vecQuestionsList.size() ; index ++ ){
                DynaValidatorForm dynaFormData = (DynaValidatorForm)vecQuestionsList.get(index);
                String answer = (String)dynaFormData.get("answer");
                acType = (String)dynaFormData.get("acType") ;
                if(acType == null || acType.equals(EMPTY_STRING)){
                    dynaFormData.set("acType" , TypeConstants.INSERT_RECORD);
                }
                dynaFormData.set("answerNumber",ANSWER_NUMBER_VALUE);
                dynaFormData.set("questionaireCompletionId" , questionnaireCompletionId );                
                dynaFormData.set("updateTimestamp" ,prepareTimeStamp().toString());
                if(answer!=null && !answer.equals(EMPTY_STRING)){
                    webTxnBean.getResults(request , ADD_UPD_QUESTIONNAIRE_ANSWERS, dynaFormData);
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
    
    
    /**This method is used to validate the form data
     * @throws Exception
     * @return true if there are no errors.
     */
    private boolean validateAnswers(CoeusDynaBeansList coeusDynaBeanList,
        HttpServletRequest request)throws Exception{
        Vector vecQuestionnaireData = (Vector)coeusDynaBeanList.getList();
        ActionMessages actionMessages = new ActionMessages();
        LinkedHashMap lhmAnsweredQuestions = new LinkedHashMap();
        LinkedHashMap lhmQuestions = new LinkedHashMap();
        int questionNumber = 0;
        if(vecQuestionnaireData!=null  && vecQuestionnaireData.size() > 0){
            for(int index = 0 ;index < vecQuestionnaireData.size() ; index ++ ){
                DynaBean dynaFormData = (DynaBean)vecQuestionnaireData.get(index);
                String answer = (String)dynaFormData.get("answer");
                String dataType = (String)dynaFormData.get("answerDataType");
                String key = ""+questionNumber;
                if(((Integer)dynaFormData.get("answerNumber")).intValue() == 0){
                    questionNumber++;
                    key = ""+questionNumber;
                    if(lhmQuestions.get(key) == null){
                        lhmQuestions.put(key, dynaFormData.get("description"));
                    }
                }
                if(dataType != null && answer != null &&
                answer.trim().length() > 0 ){           
                    lhmAnsweredQuestions.put(key, "Answered");
                    if(dataType.equalsIgnoreCase(NUMBER_DATA_TYPE)){
                        try{
                            Integer.parseInt(answer);
                        }catch(NumberFormatException nfe){
                            actionMessages.add("numberFormatException",
                            new ActionMessage(NUMBER_FORMAT_EXCEPTION,key));             
                            saveMessages(request, actionMessages);
                            return false;
                        }
                    }else if(dataType.equalsIgnoreCase(DATE_DATA_TYPE)){
                        String   resultDate = EMPTY_STRING ;
                        DateUtils dtUtils = new DateUtils();
                        if (answer != null && !answer.trim().equals(EMPTY_STRING)) {
                            resultDate = dtUtils.formatDate(answer,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
                            if(resultDate == null || (answer != null && answer.trim().length() < 10 )){                                
                                actionMessages.add("notValidDate",new ActionMessage(NOT_VALID_DATE,key));
                                saveMessages(request, actionMessages);
                                return false;
                            }else{
                                dynaFormData.set("answer",resultDate);
                            }
                        }
                    }
                } else {
                    if(lhmAnsweredQuestions.get(key) == null){
                        lhmAnsweredQuestions.put(key, null);
                    }                    
                }
            }
            if(lhmAnsweredQuestions.size() > 0){
                java.util.Set keySet = lhmAnsweredQuestions.keySet();
                Object[] objQuestions = keySet.toArray();
                for(int index = 0; index < objQuestions.length; index++){
                    if(lhmAnsweredQuestions.get(objQuestions[index]) == null){
                        actionMessages.add("answerMandatory",
                            new ActionMessage("questionnaire.answerMandatory", 
                                objQuestions[index]+" : ", lhmQuestions.get(objQuestions[index])));
                        saveMessages(request, actionMessages);
                        return false;                         
                    }
                }
            }           
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
    public Vector prepareSaveData(HashMap hmQuestionnaireData, 
        CoeusDynaBeansList coeusDynaBeanList, HttpServletRequest request)throws Exception {
        HttpSession session = request.getSession();
        int questionnaireId = ((Integer)session.getAttribute("questionnaireId"+session.getId())).intValue();
        Vector vecFormData = (Vector)coeusDynaBeanList.getList();
        Vector vecSaveData = null ;
        if(vecFormData!=null && vecFormData.size() > 0){
            vecSaveData = new Vector();
            for(int index = 0 ; index < vecFormData.size() ; index ++){
                DynaValidatorForm dynaFormData = 
                    (DynaValidatorForm)vecFormData.get(index);
                if(isDataChanged(dynaFormData ,questionnaireId ,hmQuestionnaireData, 
                        coeusDynaBeanList, request)){
                    vecSaveData.addElement(dynaFormData );
                }
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
            HttpServletRequest request, CoeusDynaBeansList coeusDynaBeanList)throws Exception{
        Vector vecQuestionsFormData = new Vector();
        BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
        if(vecQuestionnaireQuestions != null && vecQuestionnaireQuestions.size() > 0){
            for(int index = 0; index < vecQuestionnaireQuestions.size(); index++){
                QuestionnaireQuestionsBean questionnaireQuestionsBean =
                        (QuestionnaireQuestionsBean) vecQuestionnaireQuestions.get(index);
                DynaActionForm dynaFormData = coeusDynaBeanList.getDynaForm(request,"questionnaireForm");
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
    private CoeusVector convertDynaBeanToBean(Vector vecQuestionnaireQuestions)throws Exception{
        CoeusVector cvQuestionsFormData = new CoeusVector();
        BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
        if(vecQuestionnaireQuestions != null && vecQuestionnaireQuestions.size() > 0){
            for(int index = 0; index < vecQuestionnaireQuestions.size(); index++){
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
        HashMap hmData = new HashMap();
        hmData.put("coiDisclosureNumber", coiDisclosureNumber);
            if(coiDisclosureNumber==null)
            { hmData.put("sequenceNumber",0);
            }
            else
            {hmData.put("sequenceNumber", sequenceNumber);
            }
        hmData.put("personId", personId);
        WebTxnBean webTxn = new WebTxnBean();            
            Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData);
            Vector DisclDet = (Vector) DisclData.get("getDisclBySequnce");
            if (DisclDet != null && DisclDet.size() > 0) {
                request.setAttribute("ApprovedDisclDetView", DisclDet);
            }
      
    }
    //apply questionnaire version change only for review status is in "In Progress"
     private int getReviewStatus(Vector vecDisclDet){         
         int returnValue = -1;
         try{
         if(vecDisclDet!=null && !vecDisclDet.isEmpty()){
             CoiDisclosureBean coiDisclosureBean = (CoiDisclosureBean) vecDisclDet.get(0);
                 if(coiDisclosureBean!=null){
                 returnValue = coiDisclosureBean.getReviewStatusCode();
                 }             
         }
         }catch(Exception e){}
         return  returnValue;
     }
   //apply questionnaire version change only for review status is in "In Progress"  
}
