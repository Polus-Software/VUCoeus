/*
 * QuestionnaireMaintenanceServlet.java
 *
 * Created on September 19, 2006, 4:57 PM
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.mapsrules.bean.BusinessRuleBean;
import edu.mit.coeus.mapsrules.bean.RulesTxnBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.questionnaire.bean.ModuleDataBean;
import edu.mit.coeus.questionnaire.bean.QuestionExplanationBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireBaseBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireUpdateTxnBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireUsageBean;
import edu.mit.coeus.questionnaire.bean.QuestionsMaintainanceBean;
import edu.mit.coeus.questionnaire.bean.SubModuleDataBean;
import edu.mit.coeus.questionnaire.utils.QuestionnaireHandler;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.locking.LockingException;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.NotEquals;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author  vinayks
 */
public class QuestionnaireMaintenanceServlet extends CoeusBaseServlet{
    /**
     * This variable holds the function type to process the client request.
     */
    private static final char GET_QUESTIONNAIRE_DATA = 'A' ;
    /**
     * This variable holds the function type to process the client request.
     */
    private static final char GET_QUESTIONS_DATA = 'B';
    /**
     * This variable holds the function type to process the client request.
     */
    private static final char GET_QUESTIONS = 'C';
    /** This variable hold the function type for processing save questionnaire data
     */
    private static final char SAVE_QUESTIONNAIRE_DATA = 'D';
    /** This variable hold the function type for processing save questionnaire data
     */
    private static final char UPDATE_QUESTIONS = 'E';
    /** This variable hold the function type for processing save questionnaire data
     */    
    private static final char NEW_QUESTION_ID = 'F';
    /** To get the new Questionnaire Id
     */
    private static final char NEW_QUESTIONNAIRE_ID = 'G'; 
    /** Creates a new instance of QuestionnaireMaintenanceServlet */
    /** To get the new Questionnaire Id
     */
    private static final char GET_QUESTIONNAIRE_DATAS = 'H';   
    /** To get the questions for the module code */
    private static final char GET_QUESTIONS_MODE = 'I';   
    /** To get the questions from questionnaire */
    private static final char GET_QUESTIONNAIRE_QUESTIONS = 'J';   
    /** To get the answeers from the module  */
    private static final char GET_QUESTIONNAIRE_ANSWERS = 'K';   
    /** To check whether the question from questionnaire can be deleted
     */
    private static final char CAN_DELETE_QUESTION = 'L';
    /** To check whether a module can be deleted from the questionanire
     */
    private static final char CAN_DELETE_MODULE = 'M';
    /** To saving Questions Answer Header and Answer */
    private static final char SAVE_QUESTIONS_ANS_AND_HEADER = 'N';
    
    //Added for question group - start - 1   
    private static final char GET_QUESTION_GROUP = 'Q';
    //Added for question group - end - 1 
    //Code added for coeus4.3 Questionnaire enhancement case#2946 - starts
    private static final char GET_PREVIOUS_QUESTIONS = 'P';
    private static final char NEXT = 'O';    
    private static final char SAVE = 'R';
    private static final char RESTART = 'S';
    private static final char MODIFY = 'T';
    //Code added for coeus4.3 Questionnaire enhancement case#2946 - ends
    
    // Case# 3524: Add Explanation field to Questions - Start
    private static final char GET_QUESTION_EXPLANATION = 'V';
    private static final char UPDATE_QUESTION_EXPLANATION = 'U';
    // Case# 3524: Add Explanation field to Questions - End
   //Added for case 4287:Ability to define Questionnaire Templates
    private static final char GET_TEMPLATE = 'W';
    //4287 end
    // 4272: Maintain history of Questionnaires - Start
    private static final char CHECK_QUESTION_USED_IN_QUESTIONNAIRE = 'X';
    private static final char CHECK_QUESTIONNAIRE_ANSWERED = 'Y';
    private static final char GET_QUESTIONNAIRE_VERSION_DETAILS = 'Z';
    private static final char DELETE_QUESTIONNAIRE_VERSION = 'a';
    private static final char CHECK_QUESTIONNAIRE_COMPLETED = 'b';
    // 4272: Maintain history of Questionnaires - End
    //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
    private static final char CHECK_ANY_QUESTION_ANSWEREN_IN_MODULE = 'c';
    //COEUSDEV-86 : End
    // COEUSQA-2321: Copy Questionnaires for Proposal Development records
    private static final char CHECK_CAN_COPY_QUESTIONNAIRE = 'd';
    
    // Added with CoeusQA2313: Completion of Questionnaire for Submission
    private static final char COPY_QUESTIONNAIRE_FOR_REVISIONS = 'e';
    // CoeusQA2313: Completion of Questionnaire for Submission - End
    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - Start
    private static final char SAVE_AND_COMPLETE = 'z';
    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - End
            
    public QuestionnaireMaintenanceServlet() {
    }
    
    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        RequesterBean requester = null;
        ResponderBean responder = new ResponderBean();
        UserInfoBean infoBean = null;
//        String loggedinUser = null;
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        String loggedInUser = null;
        String unitNumber = null;
        
        try {
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            requester = (RequesterBean)inputFromApplet.readObject();
            isValidRequest(requester);
            loggedInUser = requester.getUserName();
            UserDetailsBean userDetailsBean = new UserDetailsBean();
            infoBean = userDetailsBean.getUserInfo(loggedInUser);
            unitNumber = infoBean.getUnitNumber();
            char functionType = requester.getFunctionType();
            QuestionnaireTxnBean questionnaireTxnBean = null;
            QuestionnaireUpdateTxnBean questionnaireUpdateTxnBean = null;
            if(functionType == GET_QUESTIONNAIRE_DATA) {
                Vector dataObjects = requester.getDataObjects();
                Integer dataValue = (Integer)dataObjects.get(0);
                int questionniareId = dataValue.intValue();
                unitNumber = (String)dataObjects.get(1);
                // 4272: Maintain history of Questionnaires - Start
                Integer qnrVerssionNumber = (Integer) dataObjects.get(2);
                Boolean latestQuestionsRequired =  (Boolean) dataObjects.get(3);
                // 4272: Maintain history of Questionnaires - End
                Hashtable htData = new Hashtable();
                questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);
                ////Code commented and modified for Case#3875 - Need to add Annual COI as a Module for Questionnaire maintenance
                //CoeusVector cvData = questionnaireTxnBean.getModuleData();
                CoeusVector cvData = questionnaireTxnBean.getModuleData(true);
                htData.put(ModuleDataBean.class,cvData == null?new CoeusVector():cvData);
                // 4272: Maintain history of Questionnaires - Start
//                cvData = questionnaireTxnBean.getQuestionnaireQuestionsData(questionniareId);
                if(latestQuestionsRequired != null && latestQuestionsRequired.booleanValue()){
                    cvData = questionnaireTxnBean.getLatestQuestionsForQuestionnaire(questionniareId);
                } else{
//                    cvData = questionnaireTxnBean.getQuestionnaireQuestionsData(questionniareId);
                    cvData = questionnaireTxnBean.getQuestionnaireQuestionsData(questionniareId, qnrVerssionNumber.intValue());
                }
                // 4272: Maintain history of Questionnaires - End
                htData.put(QuestionnaireBean.class,cvData == null?new CoeusVector():cvData);
                cvData = questionnaireTxnBean.getQuestionnaireUsageData(questionniareId);
                htData.put(QuestionnaireUsageBean.class,cvData == null?new CoeusVector():cvData);
                cvData = questionnaireTxnBean.getSubModuleData();
                htData.put(SubModuleDataBean.class,cvData == null?new CoeusVector():cvData);
                cvData = questionnaireTxnBean.getQuestionnaireData(questionniareId) ;
                htData.put(QuestionnaireBaseBean.class,cvData == null?new CoeusVector():cvData);
                // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
                cvData = questionnaireTxnBean.getQuestionnaireGroups() ;
                htData.put("QUESTIONNAIRE_GROUP_TYPES",cvData == null?new CoeusVector():cvData);
                // Added for COEUSQA-3287 Questionnaire Maintenance Features - End
                //Code added for Case#2785 - Routing Enhancement - starts
                //To filter the Business rules based on the rule type Question ("Q")
                RulesTxnBean rulesTxnBean = new RulesTxnBean(loggedInUser);
                CoeusVector cvRulesData = rulesTxnBean.getAllBusinessRules();
                if(cvRulesData != null && !cvRulesData.isEmpty()){
                    Equals equals = new Equals("ruleType", "Q");
                    cvRulesData = cvRulesData.filter(equals);
                }
                htData.put(BusinessRuleBean.class,cvRulesData == null?new CoeusVector():cvRulesData);                
                //Code added for Case#2785 - Routing Enhancement - ends
                responder.setDataObject(htData);
                responder.setResponseStatus(true);
            }else if (functionType == GET_QUESTIONS_DATA){
                 Vector dataObjects = requester.getDataObjects();
                 Integer dataValue = (Integer)dataObjects.get(0);
                 Integer questionVersionNumber = null;
                 // 4272: Maintain history of Questionnaires - Start
                 if(dataObjects.size() >1){
                     questionVersionNumber = (Integer)dataObjects.get(1);
                 }
                 int versionNumber = 0;
                 // 4272: Maintain history of Questionnaires - End
                 int questionId = dataValue.intValue();
                 questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);
                 // 4272: Maintain history of Questionnaires - Start
//                 CoeusVector cvQuestionsData = questionnaireTxnBean.getQuestionsData(questionId);
                 if(questionVersionNumber == null || questionVersionNumber.equals(new Integer(0))){
                     versionNumber = questionnaireTxnBean.fetchMaxVersionNumberOfQuestion(questionId);
                 } else {
                     versionNumber = questionVersionNumber.intValue();
                 }
                 CoeusVector cvQuestionsData = questionnaireTxnBean.getQuestionsData(questionId, versionNumber);
                 // 4272: Maintain history of Questionnaires - End
                 responder.setDataObject(cvQuestionsData);
                 responder.setResponseStatus(true);
            } else if (functionType == GET_QUESTIONS){
                questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);
                CoeusVector cvQuestionsData = questionnaireTxnBean.getQuestions();
                responder.setDataObject(cvQuestionsData);
                responder.setResponseStatus(true);
                // Case# 3524: Add Explanation field to Questions - Start
            }else if(functionType == GET_QUESTION_EXPLANATION){
                QuestionExplanationBean questionExplanationBean = 
                        (QuestionExplanationBean) requester.getDataObject();
                questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);
                CoeusVector cvQuestionExplanation = questionnaireTxnBean.getQuestionExplanation(questionExplanationBean);
                responder.setDataObject(cvQuestionExplanation);
                responder.setResponseStatus(true);
                // Case# 3524: Add Explanation field to Questions - End
            }else if(functionType == SAVE_QUESTIONNAIRE_DATA){
                boolean success = false;
                questionnaireUpdateTxnBean = new QuestionnaireUpdateTxnBean(loggedInUser);
                HashMap hmClinetData = (HashMap)requester.getDataObject();
                success = questionnaireUpdateTxnBean.addUpdDelQuestionnaireDetails(hmClinetData);
                if(success){
                    responder.setMessage(null);
                    responder.setResponseStatus(true);
                    responder.setDataObject(new Boolean(true));
                }
            } else if (functionType == UPDATE_QUESTIONS){
                 questionnaireUpdateTxnBean = new QuestionnaireUpdateTxnBean(loggedInUser);
                 QuestionsMaintainanceBean questionsMaintainanceBean = 
                    (QuestionsMaintainanceBean) requester.getDataObject();
                 boolean success = questionnaireUpdateTxnBean.addUpdQuestion(questionsMaintainanceBean);
                 questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);
                 CoeusVector cvQuestionsData = questionnaireTxnBean.getQuestions();
                 responder.setDataObject(cvQuestionsData);
                 responder.setResponseStatus(true);
                 // Case# 3524: Add Explanation field to Questions - Start
            }else if(functionType == UPDATE_QUESTION_EXPLANATION){
                boolean success = true;
                questionnaireUpdateTxnBean = new QuestionnaireUpdateTxnBean(loggedInUser);
                CoeusVector cvExplanation  =
                        (CoeusVector) requester.getDataObject();
                if(cvExplanation != null && cvExplanation.size() >0){
                    for(int index = 0; index < cvExplanation.size(); index++){
                        QuestionExplanationBean questionExplanationBean = (QuestionExplanationBean) cvExplanation.get(index);
                        success = questionnaireUpdateTxnBean.addUpdQuestionExplanation(questionExplanationBean);
                    }
                }
                responder.setResponseStatus(success);
                // Case# 3524: Add Explanation field to Questions - End
            } else if (functionType == NEW_QUESTION_ID){
                questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);
                int questionId = questionnaireTxnBean.generateQuestionId();
                responder.setDataObject(new Integer(questionId));
                responder.setResponseStatus(true);
            }else if(functionType == NEW_QUESTIONNAIRE_ID){
                questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);
                int questionnaireId = questionnaireTxnBean.getGeneratedQuestionnaireId();
                responder.setDataObject(new Integer(questionnaireId));
                responder.setResponseStatus(true);
            }else if(functionType == GET_QUESTIONNAIRE_DATAS){
                questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);
                CoeusVector cvQuestionnaireData = questionnaireTxnBean.getQuestionnaireDetails();
                //Code added for coeus4.3 enhancement - starts
                //If any of the questionnaire is answered in any of the module and the 
                //questionnaire is not final, then make that questionnaire final.
                boolean isPresent = false;
                CoeusVector cvDataToUpdate = new CoeusVector();
                if(cvQuestionnaireData != null && !cvQuestionnaireData.isEmpty()){
                    for(int index =0; index < cvQuestionnaireData.size(); index++){
                        QuestionnaireBaseBean baseBean = 
                                (QuestionnaireBaseBean)cvQuestionnaireData.get(index);
                        if(baseBean.isCompletedFlag() && !baseBean.isFinalFlag()){
                            isPresent = true;
                            baseBean.setFinalFlag(true);
                            baseBean.setAcType("U");
                            cvDataToUpdate.add(baseBean);
                        } 
                    }
                }
                if(isPresent){
                    questionnaireUpdateTxnBean = new QuestionnaireUpdateTxnBean(loggedInUser);
                    questionnaireUpdateTxnBean.updateQuestionnaireFlag(cvDataToUpdate);
                    cvQuestionnaireData = questionnaireTxnBean.getQuestionnaireDetails();
                }
                //Code added for coeus4.3 enhancement - ends
                // Modified for COEUSQA-3287 Questionnaire Maintenance Features - Start
//                responder.setDataObject(cvQuestionnaireData);
                HashMap hmQuestionnaireData = new HashMap();
                
                Boolean isDuringUpdate = (Boolean)requester.getDataObject();
//                if(isDuringUpdate.booleanValue()){
                    hmQuestionnaireData.put("QUESTIONNAIRE_GROUP_TYPES",questionnaireTxnBean.getQuestionnaireGroups());
//                }
                hmQuestionnaireData.put("QUESTIONNAIRE_DATA",cvQuestionnaireData);
                responder.setDataObject(hmQuestionnaireData);
                // Modified for COEUSQA-3287 Questionnaire Maintenance Features - End
                
                responder.setResponseStatus(true);
            }else if(functionType == GET_QUESTIONS_MODE) {
                //Code commented and modified for coeus4.3 Questionnaire enhancement case#2946 - starts
                //To get the Questionnaire list data with completed flag
//                QuestionnaireAnswerHeaderBean questionnaireModuleObject
//                    = (QuestionnaireAnswerHeaderBean)requester.getDataObject();                
//                questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);
//                CoeusVector cvQuestionnaireData = questionnaireTxnBean.getQuestionnaireModeDetails(questionnaireModuleObject);
                CoeusVector cvQuestionnaireDatas = (CoeusVector) requester.getDataObject();
                QuestionnaireAnswerHeaderBean questionnaireModuleObject
                    = (QuestionnaireAnswerHeaderBean) cvQuestionnaireDatas.get(0);                
                QuestionnaireHandler handler = new QuestionnaireHandler(loggedInUser);
                CoeusVector cvQuestionnaireData = handler.getQuestionnaireDetails(questionnaireModuleObject);
                //Code commented and modified for coeus4.3 Questionnaire enhancement case#2946 - ends
                responder.setDataObject(cvQuestionnaireData);
                responder.setResponseStatus(true);
                
            }else if(functionType == GET_QUESTIONNAIRE_QUESTIONS) {
                QuestionnaireAnswerHeaderBean questionnaireModuleObject
                    = (QuestionnaireAnswerHeaderBean)requester.getDataObject();
                String moduleKey = requester.getId();
                String mode = requester.getRequestedForm();
                //Code commented and modified for coeus4.3 Questionnaire enhancement case#2946 - starts
                //To show the questions according to its brach and conditions
//                questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);
//                CoeusVector cvQuestionnaireData 
//                    = questionnaireTxnBean.getQuestionnaireQuestionsDetails(questionnaireModuleObject.getQuestionnaireId());
//                responder.setDataObject(cvQuestionnaireData == null ? new CoeusVector() : cvQuestionnaireData);
//                responder.setResponseStatus(true);
//                
//            }else if(functionType == GET_QUESTIONNAIRE_ANSWERS) {
//                QuestionnaireAnswerHeaderBean questionnaireModuleObject
//                    = (QuestionnaireAnswerHeaderBean)requester.getDataObject();
//                questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);
//                CoeusVector cvQuestionnaireData = questionnaireTxnBean.getQuestionnaireAnswersDetails(questionnaireModuleObject);
//                responder.setDataObject(cvQuestionnaireData == null ? new CoeusVector() : cvQuestionnaireData);
                QuestionnaireHandler handler = new QuestionnaireHandler(loggedInUser);
                HashMap hmQuestionnaireData = handler.getQuestionnaireQuestions(questionnaireModuleObject,
                        moduleKey, mode);
                responder.setMessage((String) hmQuestionnaireData.get("message"));
                responder.setId((String) hmQuestionnaireData.get("page"));
                responder.setDataObject(hmQuestionnaireData.get("dataObject"));
                //Code commented and modified for coeus4.3 Questionnaire enhancement case#2946 - ends
                responder.setResponseStatus(true);
                
            }else if(functionType == CAN_DELETE_QUESTION){
                Vector dataObjects = requester.getDataObjects();
                int questionId = ((Integer)dataObjects.get(0)).intValue();
                int questionnaireId = ((Integer)dataObjects.get(1)).intValue();
                questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);
                boolean canDeleteQuestion = 
                    questionnaireTxnBean.canDeleteQuestionFromquestionanire(questionId,questionnaireId);
                responder.setDataObject(new Boolean(canDeleteQuestion ));
                responder.setResponseStatus(true);
            }else if(functionType == CAN_DELETE_MODULE){
                Vector dataObjects = requester.getDataObjects();
                int moduleItemCode = ((Integer)dataObjects.get(0)).intValue();
                int moduleSubItemCode = ((Integer)dataObjects.get(1)).intValue();
                int questionnaireId = ((Integer)dataObjects.get(2)).intValue();
                questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);
                boolean canDeleteUsage = 
                    questionnaireTxnBean.canDeleteQuestionnaireUsage(moduleItemCode,moduleSubItemCode,questionnaireId);
                responder.setDataObject(new Boolean(canDeleteUsage ));
                responder.setResponseStatus(true);
            }else if(functionType == SAVE_QUESTIONS_ANS_AND_HEADER ) {


                    //~*~*~**~*~**~*~* M and Current User
              CoeusVector cvQuestionnaireDatas = (CoeusVector) requester.getDataObject();
                QuestionnaireAnswerHeaderBean questionnaireModuleObject
                    = (QuestionnaireAnswerHeaderBean) cvQuestionnaireDatas.get(0);
                int m=questionnaireModuleObject.getModuleSubItemCode();
                 String c=questionnaireModuleObject.getCurrentUser();
//                questionnaireUpdateTxnBean = new QuestionnaireUpdateTxnBean(m,c);
              
   //~*~*~**~*~**~*~* M and Current User

                Vector dataObjects = requester.getDataObjects();
                questionnaireUpdateTxnBean = new QuestionnaireUpdateTxnBean(loggedInUser);
              //  questionnaireUpdateTxnBean.addUpdQuestionnaireAnsHeaderDetails(dataObjects,m,c);
                responder.setResponseStatus(true);
            //Added for question group - start - 2
            }else if(functionType == GET_QUESTION_GROUP){  
                questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);
                Vector cvQuestionGroups = questionnaireTxnBean.getQuestionGroups();
                responder.setDataObject(cvQuestionGroups == null ? new Vector() : cvQuestionGroups);
                responder.setResponseStatus(true);
            }
            //Added for question group - end - 2
            //Code added for coeus4.3 Questionnaire enhancement case#2946 - starts
            //for different modes of operations
            else if(functionType == GET_PREVIOUS_QUESTIONS){
                CoeusVector cvQuestionnaireDatas = (CoeusVector) requester.getDataObject();
                QuestionnaireAnswerHeaderBean questionnaireModuleObject
                    = (QuestionnaireAnswerHeaderBean) cvQuestionnaireDatas.get(0); 
                String moduleKey = requester.getId();
                QuestionnaireHandler handler = new QuestionnaireHandler(loggedInUser);
                HashMap hmQuestionnaireData = handler.getPreviousQuestions(questionnaireModuleObject, moduleKey);
                responder.setId((String) hmQuestionnaireData.get("page"));
                responder.setDataObject(hmQuestionnaireData.get("dataObject"));                
                responder.setResponseStatus(true);
            }else if(functionType == NEXT){
                CoeusVector cvQuestionnaireDatas = (CoeusVector) requester.getDataObject();
                QuestionnaireAnswerHeaderBean questionnaireModuleObject
                    = (QuestionnaireAnswerHeaderBean) cvQuestionnaireDatas.get(0);                
                CoeusVector cvQuestionAnswers  = (CoeusVector) cvQuestionnaireDatas.get(1);
                String moduleKey = requester.getId();
                boolean lockCheck = false;
                CoeusMessageResourcesBean coeusMessageResourcesBean =new CoeusMessageResourcesBean();                
                String msg = "";
                // Modified for IACUC questionnaire implementation - Start
//                if(questionnaireModuleObject.getModuleItemCode() == 7){
//                    ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
//                    lockCheck = protocolDataTxnBean.lockCheck(moduleKey, loggedInUser);
//                    msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1008");
//                } else if(questionnaireModuleObject.getModuleItemCode() == 3){
//                    ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
//                    lockCheck = proposalDevelopmentTxnBean.lockCheck(moduleKey, loggedInUser);
//                    msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1006");
//                }
                if(questionnaireModuleObject.getModuleItemCode() == ModuleConstants.PROTOCOL_MODULE_CODE){
                    ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
                    lockCheck = protocolDataTxnBean.lockCheck(moduleKey, loggedInUser);
                    msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1008");
                } else if(questionnaireModuleObject.getModuleItemCode() == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                    ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                    lockCheck = proposalDevelopmentTxnBean.lockCheck(moduleKey, loggedInUser);
                    msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1006");
                } else if(questionnaireModuleObject.getModuleItemCode() == ModuleConstants.IACUC_MODULE_CODE){
                    edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean protocolDataTxnBean =
                            new edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean();
                    lockCheck = protocolDataTxnBean.lockCheck(moduleKey, loggedInUser);
                    msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1008");
                }
                // Modified for IACUC questionnaire implementation - End

                if(lockCheck){
                    msg = msg+" "+moduleKey+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1002");
                    throw new LockingException(msg);
                }                
                QuestionnaireHandler handler = new QuestionnaireHandler(loggedInUser);
                HashMap hmQuestionnaireData = handler.saveAndGetNextQuestions(questionnaireModuleObject,moduleKey, 
                        cvQuestionAnswers);
                responder.setMessage((String) hmQuestionnaireData.get("message"));
                responder.setId((String) hmQuestionnaireData.get("page"));
                responder.setDataObject(hmQuestionnaireData.get("dataObject"));                
                responder.setResponseStatus(true);
                
            }else if(functionType == SAVE_AND_COMPLETE){
                CoeusVector cvQuestionnaireDatas = (CoeusVector) requester.getDataObject();
                QuestionnaireAnswerHeaderBean questionnaireModuleObject
                        = (QuestionnaireAnswerHeaderBean) cvQuestionnaireDatas.get(0);
                CoeusVector cvQuestionAnswers  = (CoeusVector) cvQuestionnaireDatas.get(1);
                String moduleKey = requester.getId();
                boolean lockCheck = false;
                CoeusMessageResourcesBean coeusMessageResourcesBean =new CoeusMessageResourcesBean();
                String msg = "";
                if(questionnaireModuleObject.getModuleItemCode() == ModuleConstants.PROTOCOL_MODULE_CODE){
                    ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
                    lockCheck = protocolDataTxnBean.lockCheck(moduleKey, loggedInUser);
                    msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1008");
                } else if(questionnaireModuleObject.getModuleItemCode() == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                    ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                    lockCheck = proposalDevelopmentTxnBean.lockCheck(moduleKey, loggedInUser);
                    msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1006");
                } else if(questionnaireModuleObject.getModuleItemCode() == ModuleConstants.IACUC_MODULE_CODE){
                    edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean protocolDataTxnBean =
                            new edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean();
                    lockCheck = protocolDataTxnBean.lockCheck(moduleKey, loggedInUser);
                    msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1008");
                }
                if(lockCheck){
                    msg = msg+" "+moduleKey+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1002");
                    throw new LockingException(msg);
                }
                QuestionnaireHandler handler = new QuestionnaireHandler(loggedInUser);
                HashMap hmQuestionnaireData = handler.saveAndCompleteQuestionnaire(questionnaireModuleObject,moduleKey,cvQuestionAnswers,questionnaireModuleObject);
                responder.setMessage((String) hmQuestionnaireData.get("message"));
                responder.setId((String) hmQuestionnaireData.get("page"));
                responder.setDataObject(hmQuestionnaireData.get("dataObject"));
                responder.setResponseStatus(true);
            }else if(functionType == SAVE){
                CoeusVector cvQuestionnaireDatas = (CoeusVector) requester.getDataObject();
                QuestionnaireAnswerHeaderBean questionnaireModuleObject
                    = (QuestionnaireAnswerHeaderBean) cvQuestionnaireDatas.get(0);                
                CoeusVector cvQuestionAnswers  = (CoeusVector) cvQuestionnaireDatas.get(1);
                String moduleKey = requester.getId();
                boolean lockCheck = false;
                CoeusMessageResourcesBean coeusMessageResourcesBean =new CoeusMessageResourcesBean();                
                String msg = "";
                // Modified for IACUC questionnaire implementation - Start
//                if(questionnaireModuleObject.getModuleItemCode() == 7){
//                    ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
//                    lockCheck = protocolDataTxnBean.lockCheck(moduleKey, loggedInUser);
//                    msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1008");
//                } else if(questionnaireModuleObject.getModuleItemCode() == 3){
//                    ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
//                    lockCheck = proposalDevelopmentTxnBean.lockCheck(moduleKey, loggedInUser);
//                    msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1006");
//                }
                if(questionnaireModuleObject.getModuleItemCode() == ModuleConstants.PROTOCOL_MODULE_CODE){
                    ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
                    lockCheck = protocolDataTxnBean.lockCheck(moduleKey, loggedInUser);
                    msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1008");
                } else if(questionnaireModuleObject.getModuleItemCode() == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                    ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                    lockCheck = proposalDevelopmentTxnBean.lockCheck(moduleKey, loggedInUser);
                    msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1006");
                } else if(questionnaireModuleObject.getModuleItemCode() == ModuleConstants.IACUC_MODULE_CODE){
                    edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean protocolDataTxnBean = 
                            new edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean();
                    lockCheck = protocolDataTxnBean.lockCheck(moduleKey, loggedInUser);
                    msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1008");
                }
                // Modified for IACUC questionnaire implementation - Start
                
                if(lockCheck){
                    msg = msg+" "+moduleKey+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1002");
                    throw new LockingException(msg);
                }               
                QuestionnaireHandler handler = new QuestionnaireHandler(loggedInUser);
                HashMap hmQuestionnaireData = handler.saveQuestionnaire(questionnaireModuleObject,moduleKey, 
                        cvQuestionAnswers);
                responder.setId((String) hmQuestionnaireData.get("page"));
                responder.setMessage((String) hmQuestionnaireData.get("message"));
                responder.setDataObject(hmQuestionnaireData.get("dataObject"));                
                responder.setResponseStatus(true);                
            }else if(functionType == RESTART || functionType == MODIFY){
                CoeusVector cvQuestionnaireDatas = (CoeusVector) requester.getDataObject();
                QuestionnaireAnswerHeaderBean questionnaireModuleObject
                    = (QuestionnaireAnswerHeaderBean) cvQuestionnaireDatas.get(0);
                //Code added for Case#2785 - Routing Enhancement
                //To get the Questionnaire menu items
                CoeusVector cvQuestionnaireMenu = (CoeusVector) cvQuestionnaireDatas.get(2);
                String moduleKey = requester.getId();
                boolean lockCheck = false;
                CoeusMessageResourcesBean coeusMessageResourcesBean =new CoeusMessageResourcesBean();                
                String msg = "";
                // Modified for IACUC questionnaire implementation - Start
//                if(questionnaireModuleObject.getModuleItemCode() == 7){
//                    ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
//                    lockCheck = protocolDataTxnBean.lockCheck(moduleKey, loggedInUser);
//                    msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1008");
//                } else if(questionnaireModuleObject.getModuleItemCode() == 3){
//                    ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
//                    lockCheck = proposalDevelopmentTxnBean.lockCheck(moduleKey, loggedInUser);
//                    msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1006");
//                }
                if(questionnaireModuleObject.getModuleItemCode() == ModuleConstants.PROTOCOL_MODULE_CODE){
                    ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
                    lockCheck = protocolDataTxnBean.lockCheck(moduleKey, loggedInUser);
                    msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1008");
                } else if(questionnaireModuleObject.getModuleItemCode() == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                    ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                    lockCheck = proposalDevelopmentTxnBean.lockCheck(moduleKey, loggedInUser);
                    msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1006");
                } else if(questionnaireModuleObject.getModuleItemCode() == ModuleConstants.IACUC_MODULE_CODE){
                    edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean protocolDataTxnBean =
                            new edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean();
                    lockCheck = protocolDataTxnBean.lockCheck(moduleKey, loggedInUser);
                    msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1008");
                }
                // Modified for IACUC questionnaire implementation - end
                if(lockCheck){
                    msg = msg+" "+moduleKey+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1002");
                    throw new LockingException(msg);
                }               
                QuestionnaireHandler handler = new QuestionnaireHandler(loggedInUser);
                HashMap hmQuestionnaireData = handler.restartModifyQuestionnaire(questionnaireModuleObject,moduleKey, 
                        functionType);
                //Code added for Case#2785 - Routing Enhancement - starts
                //If the functionType is restart then dont get the questionnaire menu items from database.
                CoeusVector cvDataObjects = (CoeusVector) hmQuestionnaireData.get("dataObject");
                if(functionType == RESTART){
                    if(cvQuestionnaireMenu != null && cvQuestionnaireMenu.size() > 0){
                        for(int index = 0; index < cvQuestionnaireMenu.size(); index++){
                            QuestionnaireAnswerHeaderBean headerMenuBean =
                                    (QuestionnaireAnswerHeaderBean) cvQuestionnaireMenu.get(index);
                            if(questionnaireModuleObject.getQuestionnaireId() == headerMenuBean.getQuestionnaireId()){
                                headerMenuBean.setCompletedFlag(false);
                            }
                        }
                    }
                    cvDataObjects.remove(1);
                    cvDataObjects.add(1, cvQuestionnaireMenu);
                }
                responder.setId((String) hmQuestionnaireData.get("page"));
                responder.setDataObject(cvDataObjects);                 
                //Code added for Case#2785 - Routing Enhancement - ends
                responder.setResponseStatus(true);                
            }
            //Code added for coeus4.3 Questionnaire enhancement case#2946 - end
            //Added for case 4287:Ability to define Questionnaire Templates - Start
            else if(functionType == GET_TEMPLATE){
                // 4272: Maintain history of Questionnaires - Start
//                Integer dataValue = (Integer)requester.getDataObject();
//                int questionniareId = dataValue.intValue();
                CoeusVector dataObjects = (CoeusVector)requester.getDataObjects();
                Integer questionniareId = (Integer) dataObjects.elementAt(0);
                Integer qnrVersionNumber = (Integer) dataObjects.elementAt(1);
                questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);
//                responder.setDataObject(questionnaireTxnBean.getQuestionnaireTemplate(questionniareId));
                responder.setDataObject(questionnaireTxnBean.getQuestionnaireTemplate(questionniareId.intValue(), 
                        qnrVersionNumber.intValue()));
                // 4272: Maintain history of Questionnaires - End
                responder.setResponseStatus(true);
            }
            //4287 End
            // 4272: Maintain history of Questionnaires - Start
            else if(functionType == CHECK_QUESTION_USED_IN_QUESTIONNAIRE){
                boolean isQuestionUsed;
                QuestionsMaintainanceBean questionMaintBean = (QuestionsMaintainanceBean)requester.getDataObject();
                questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);
                if(questionMaintBean != null && questionMaintBean.getQuestionId() != null){
                    if(questionMaintBean.getVersionNumber() == 0){
                        isQuestionUsed = false;
                    } else {
                        isQuestionUsed= questionnaireTxnBean.checkQuestionUsedInQuestionnaire(questionMaintBean.getQuestionId().intValue(),
                                questionMaintBean.getVersionNumber());
                    }
                } else{
                    isQuestionUsed = false;
                }
                responder.setDataObject(new Boolean(isQuestionUsed));
                responder.setResponseStatus(true);
            } else if(functionType == CHECK_QUESTIONNAIRE_ANSWERED){
                boolean isQuestionnaireAnswered = true;
                Integer questionnaireId = (Integer) requester.getDataObject();
                questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);
                if(questionnaireId != null){
                    isQuestionnaireAnswered = questionnaireTxnBean.checkQuesionnaireAnswered(questionnaireId.intValue());
                }
                responder.setDataObject(new Boolean(isQuestionnaireAnswered));
                responder.setResponseStatus(true);
            } else if(functionType == GET_QUESTIONNAIRE_VERSION_DETAILS){
                int versionAnswered = 0;
                int latestVersion = 0;
                CoeusVector cvDataObjects = new CoeusVector();
                QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean = (QuestionnaireAnswerHeaderBean) requester.getDataObject();
                questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);
                if(questionnaireAnswerHeaderBean != null){
                    versionAnswered = questionnaireTxnBean.fetchAnsweredVersionNumberOfQuestionnaire(questionnaireAnswerHeaderBean);
                    latestVersion = questionnaireTxnBean.fetchMaxVersionNumberOfQuestionnaire(questionnaireAnswerHeaderBean.getQuestionnaireId());
                }
                // Added for COEUSQA-3475 : Questionnaire Versioning vs. Answer Retention - Start
                questionnaireTxnBean = null;
                QuestionnaireHandler questionnaireHandler = new QuestionnaireHandler(loggedInUser);
                questionnaireHandler.retainAnswersInNewVersion(latestVersion,versionAnswered,requester.getId(),questionnaireAnswerHeaderBean);
                // Added for COEUSQA-3475 : Questionnaire Versioning vs. Answer Retention - End
                cvDataObjects.add(0, new Integer(versionAnswered));
                cvDataObjects.add(1, new Integer(latestVersion));
                responder.setDataObjects(cvDataObjects);
                responder.setResponseStatus(true);
            } else if(functionType == DELETE_QUESTIONNAIRE_VERSION){
                QuestionnaireBaseBean questionnaireBaseBean = (QuestionnaireBaseBean) requester.getDataObject();
                questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);
                boolean deleted = questionnaireTxnBean.deleteQuestionnaireVersion(questionnaireBaseBean);
                responder.setDataObject(new Boolean(deleted));
                responder.setResponseStatus(true);
            }else if(functionType == CHECK_QUESTIONNAIRE_COMPLETED){
                CoeusVector cvModuleData = (CoeusVector) requester.getDataObjects();
                String moduleItemKey = (String) cvModuleData.elementAt(0);
                Integer moduleCode =  (Integer) cvModuleData.elementAt(1);
                String subModuleCode = (String) cvModuleData.elementAt(2);
                Integer moduleItemSeqNumber =  (Integer) cvModuleData.elementAt(3);
                
                questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);
                // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - Start
//                int result = questionnaireTxnBean.fetchQuestionnaireCompletedForModule
//                        (moduleItemKey, moduleCode.intValue(), subModuleCode, moduleItemSeqNumber.intValue());
//                responder.setDataObject(new Integer(result));
                Vector vecUnFilledQnr = questionnaireTxnBean.fetchQuestionnaireCompletedForModule
                        (moduleItemKey, moduleCode.intValue(), subModuleCode, moduleItemSeqNumber.intValue());
                responder.setDataObject(vecUnFilledQnr);
                // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - End
                responder.setResponseStatus(true);
            }

            // 4272: Maintain history of Questionnaires - End
            //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
            else if(functionType == CHECK_ANY_QUESTION_ANSWEREN_IN_MODULE){
                CoeusVector cvModuleData = (CoeusVector) requester.getDataObjects();
                Integer moduleItemCode =  (Integer) cvModuleData.elementAt(0);
                String moduleItemKey = (String) cvModuleData.elementAt(1);
                Integer subModuleItemCode = (Integer) cvModuleData.elementAt(2);
                String moduleItemItemKey =  (String) cvModuleData.elementAt(3);
                questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);
                boolean questionAnswered = questionnaireTxnBean.checkAnyQuestionIsAnsweredInModule(moduleItemCode.intValue(),moduleItemKey,
                        subModuleItemCode.intValue(),moduleItemItemKey);
                responder.setDataObject(new Boolean(questionAnswered));
                responder.setResponseStatus(true);
            }
            //COEUSDEV-86 : End
            // COEUSQA-2321: Copy Questionnaires for Proposal Development records - Start
            else if(functionType == CHECK_CAN_COPY_QUESTIONNAIRE){
                Vector cvModuleData = (Vector) requester.getDataObjects();
                
                Integer moduleItemCode =  (Integer) cvModuleData.elementAt(0);
                Integer subModuleItemCode = (Integer) cvModuleData.elementAt(1);
                String moduleItemKey = (String) cvModuleData.elementAt(2);
                String moduleItemItemKey =  (String) cvModuleData.elementAt(3);
                
                questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);
                boolean canCopy = questionnaireTxnBean.checkCanCopyQuestionnaireFromModule(moduleItemCode, subModuleItemCode, moduleItemKey, moduleItemItemKey);
                responder.setDataObject(Boolean.valueOf(canCopy));
                responder.setResponseStatus(true);
            }
            // COEUSQA-2321: Copy Questionnaires for Proposal Development records - End
            // Added with CoeusQA2313: Completion of Questionnaire for Submission
            else if(functionType == COPY_QUESTIONNAIRE_FOR_REVISIONS){
                QuestionnaireAnswerHeaderBean headerBean = (QuestionnaireAnswerHeaderBean) requester.getDataObject();
                questionnaireUpdateTxnBean = new QuestionnaireUpdateTxnBean(loggedInUser);
                boolean dataCopied = questionnaireUpdateTxnBean.copyQuestionnairesForRevisions(headerBean);
                responder.setDataObject(Boolean.valueOf(dataCopied));
                responder.setResponseStatus(true);
            }
            // CoeusQA2313: Completion of Questionnaire for Submission - End
        }catch( CoeusException coeusEx ) {
            //coeusEx.printStackTrace();
            int index=0;
            String errMsg;
            if(coeusEx.getErrorId()==999999) {
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
                responder.setLocked(true);
            }else{
                errMsg = coeusEx.getMessage();
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean
            =new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);
            
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx,
            "QuestionnaireMaintenanceServlet", "doPost");
            
        } catch( DBException dbEx ) {
            //dbEx.printStackTrace();
            int index=0;
            String errMsg = dbEx.getUserMessage();
            if (dbEx.getErrorId() == 20102 ) {
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
            }
            if (errMsg.equals("db_exceptionCode.1111")) {
                responder.setCloseRequired(true);
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean
            = new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);
            
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx,
            "QuestionnaireMaintenanceServlet", "doPost");
            
        } catch(Exception e) {
            //e.printStackTrace();
            responder.setResponseStatus(false);
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e,
            "QuestionnaireMaintenanceServlet", "doPost");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "QuestionnaireMaintenanceServlet", "doPost");
        //Case 3193 - END
        } finally {
            try{
                outputToApplet
                = new ObjectOutputStream(response.getOutputStream());
                outputToApplet.writeObject(responder);
                // close the streams
                if (inputFromApplet!=null){
                    inputFromApplet.close();
                }
                if (outputToApplet!=null){
                    outputToApplet.flush();
                    outputToApplet.close();
                }
            } catch (IOException ioe){
                UtilFactory.log( ioe.getMessage(), ioe,
                "QuestionnaireMaintenanceServlet", "doPost");
            }
        }
    }
}
