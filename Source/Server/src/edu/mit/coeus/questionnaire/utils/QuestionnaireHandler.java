/*
 * @(#)QuestionnaireHandler.java September 27, 2007, 7:01 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * QuestionnaireHandler.java
 *
 * Created on September 27, 2007, 7:01 PM
 *
 */

package edu.mit.coeus.questionnaire.utils;

import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.questionnaire.bean.QuestionAnswerBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireQuestionsBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireUpdateTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.utils.query.Or;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.xml.generator.QuestionnaireStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author noorula
 */
public class QuestionnaireHandler {

    private QuestionnaireTxnBean questionnaireTxnBean;
    private QuestionnaireQuestionsBuilder questionnaireBuilder;
    private QueryEngine queryEngine;
    private static final String EMPTY_STRING = "";
    private String loggedInUser;
    private static final char RESTART = 'S';
    private static final String PAGE = "page";
    private static final String DATA_OBJECT = "dataObject";
    private static final String COMPLETED = "COMPLETED";
    private static final String PAGES_ANSWERED = "pagesAnswered";
    private static final boolean isLastAnsweredPageToDisplay = true;
    private static final boolean isCheckForNextPage = true;

    /** Creates a new instance of QuestionnaireHandler */
    public QuestionnaireHandler(String loggedInUser) {
        this.loggedInUser = loggedInUser;
        questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);
        queryEngine = QueryEngine.getInstance();
    }

    /**
     * This method is used to get the queestions and answers from DB,
     * then build the questionnaire and give the questions for display
     * @param questionnaireModuleObject
     * @param moduleKey
     * @param mode edit or display mode which questions required
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws java.lang.Exception
     * @return HashMap contains questions to display, page number and message
     */
    public HashMap getQuestionnaireQuestions(QuestionnaireAnswerHeaderBean questionnaireModuleObject,
            String moduleKey, String mode) throws DBException, CoeusException, Exception{

        HashMap hmQuestionnaireData = new HashMap();
        // Get the list of questions
        // 4272: Maintain history of Questionnaires - Start
//        CoeusVector cvQuestionnaireData
//                = questionnaireTxnBean.getQuestionnaireQuestionsDetails(questionnaireModuleObject.getQuestionnaireId());
        CoeusVector cvQuestionnaireData
                = questionnaireTxnBean.getQuestionnaireQuestionsDetails(questionnaireModuleObject);
        // 4272: Maintain history of Questionnaires - End
        String message = EMPTY_STRING;
        // Get the list of answers
        CoeusVector cvAnswersData = questionnaireTxnBean.getQuestionnaireAnswersDetails(questionnaireModuleObject);
        cvQuestionnaireData = getQuestionsData(cvQuestionnaireData, cvAnswersData);
        questionnaireBuilder = new QuestionnaireQuestionsBuilder(cvQuestionnaireData,loggedInUser);
        //building the questionnaire tree for the questions.
        questionnaireBuilder.buildQuestions(0);
        //holds the questionnaire tree
        LinkedHashMap lhmQuestionnaireTree = questionnaireBuilder.getLhmQuestionnaireTree();
        lhmQuestionnaireTree = getAllQuestionnaireQuestions(lhmQuestionnaireTree, questionnaireModuleObject);
        Hashtable htQuestionnaireData = new Hashtable();
        LinkedHashMap lhmPagesAnswered = new LinkedHashMap();
        htQuestionnaireData.put("originalQuestions", cvQuestionnaireData);
        htQuestionnaireData.put("questionnaireTree", lhmQuestionnaireTree);
        htQuestionnaireData.put("questionnaireLevel", questionnaireBuilder.getCvQuestionnaireLevel());
        htQuestionnaireData.put(PAGE, "1");
        lhmPagesAnswered.put(htQuestionnaireData.get(PAGE), null);
        htQuestionnaireData.put(PAGES_ANSWERED, lhmPagesAnswered);
        queryEngine.addDataCollection(moduleKey+questionnaireModuleObject.getQuestionnaireId(),
                htQuestionnaireData);
        //this iteration is for getting the non answered page questions
        CoeusVector cvQuestions = (CoeusVector) lhmQuestionnaireTree.get(htQuestionnaireData.get(PAGE));
        boolean isAnswerPresent = false;
        // Modified for COEUSQA-3475 : Questionnaire Versioning vs. Answer Retention - Start
//        do{
//            
//            Integer questionNumber = null;
//            for(int index = 0; index < cvQuestions.size(); index++){
//                QuestionnaireQuestionsBean questionnaireQuestionsBean =
//                        (QuestionnaireQuestionsBean) cvQuestions.get(index);
//                if((!(questionnaireModuleObject.getModuleItemCode()==3 && questionnaireModuleObject.getModuleSubItemCode()==6))){
//                    if(questionnaireQuestionsBean.getAnswer() != null
//                            && !questionnaireQuestionsBean.getAnswer().equals("")
//                            // Added with CoeusQA2313: Completion of Questionnaire for Submission
//                            && questionnaireQuestionsBean.getAnsweredSubmoduleCode() == questionnaireModuleObject.getApplicableSubmoduleCode()
//                            && questionnaireModuleObject.getApplicableModuleItemKey().equals(questionnaireQuestionsBean.getAnsweredModuleItemKey())
//                            && questionnaireQuestionsBean.getAnsweredModuleSubItemKey() == questionnaireModuleObject.getApplicableModuleSubItemKey()){
//                        // CoeusQA2313: Completion of Questionnaire for Submission - end
//                        isAnswerPresent = true;
//                        questionNumber = questionnaireQuestionsBean.getQuestionNumber();
//                        break;
//                    }
//                } else {
//                    if(questionnaireQuestionsBean.getAnswer() != null
//                            && !questionnaireQuestionsBean.getAnswer().equals("")
//                            // Added with CoeusQA2313: Completion of Questionnaire for Submission
//                            && questionnaireQuestionsBean.getAnsweredSubmoduleCode() == questionnaireModuleObject.getApplicableSubmoduleCode()
//                            && questionnaireModuleObject.getApplicableModuleItemKey().equals(questionnaireQuestionsBean.getAnsweredModuleItemKey())){
//                        // CoeusQA2313: Completion of Questionnaire for Submission - end
//                        isAnswerPresent = true;
//                        questionNumber = questionnaireQuestionsBean.getQuestionNumber();
//                        break;
//                    }
//                }
//            }
//            
//            
//            if(isAnswerPresent){
//                Equals eqQuestion = new Equals("questionNumber", questionNumber);
//                NotEquals eqAnswer = new NotEquals("answer", "");
//                Or andQuesAns = new Or(eqQuestion, eqAnswer);
//                CoeusVector cvFilteredData = cvQuestions.filter(andQuesAns);
//                lhmPagesAnswered.put(htQuestionnaireData.get(PAGE), cvQuestions);
//                cvQuestions = questionnaireBuilder.getNextQuestions(htQuestionnaireData, cvQuestions,questionnaireModuleObject);
//                if(cvQuestions == null || cvQuestions.isEmpty()){
//                    if(!"Y".equals(questionnaireModuleObject.getQuestionnaireCompletionFlag())){
//                        questionnaireModuleObject.setQuestionnaireCompletionFlag("Y");
//                        questionnaireModuleObject.setAcType(TypeConstants.UPDATE_RECORD);
//                        QuestionnaireUpdateTxnBean questionnaireUpdateTxnBean = new QuestionnaireUpdateTxnBean(loggedInUser);
//                        questionnaireUpdateTxnBean.updateQuestionnaireHeader(questionnaireModuleObject);
//                    }
//                    htQuestionnaireData.put(PAGE, "1");
//                    message = COMPLETED;
//                    isAnswerPresent = false;
//                }
//            }
//        } while(isAnswerPresent);
        do{
            HashMap hmQuestionAnswers = new HashMap();
            for(int index = 0; index < cvQuestions.size(); index++){
                QuestionnaireQuestionsBean questionnaireQuestionsBean =
                        (QuestionnaireQuestionsBean) cvQuestions.get(index);
                if(hmQuestionAnswers.get(questionnaireQuestionsBean.getQuestionNumber()) != null){
                    ((CoeusVector)hmQuestionAnswers.get(questionnaireQuestionsBean.getQuestionNumber())).add(questionnaireQuestionsBean);
                }else{
                    CoeusVector cvQuestionsAnswer = new CoeusVector();
                    cvQuestionsAnswer.add(questionnaireQuestionsBean);
                    hmQuestionAnswers.put(questionnaireQuestionsBean.getQuestionNumber(),cvQuestionsAnswer);
                }
            }
            
            Iterator itrQuestionAnswers = hmQuestionAnswers.keySet().iterator();
            while(itrQuestionAnswers.hasNext()){
                CoeusVector cvQuestionAnswer = (CoeusVector)hmQuestionAnswers.get(itrQuestionAnswers.next());
                Integer questionNumber = null;
                isAnswerPresent = false;
                for(int index = 0; index < cvQuestionAnswer.size(); index++){
                    QuestionnaireQuestionsBean questionnaireQuestionsBean =
                            (QuestionnaireQuestionsBean) cvQuestionAnswer.get(index);
                    if((!(questionnaireModuleObject.getModuleItemCode()==3 && questionnaireModuleObject.getModuleSubItemCode()==6))){
                        if(questionnaireQuestionsBean.getAnswer() != null
                                && !questionnaireQuestionsBean.getAnswer().equals("")
                                && questionnaireQuestionsBean.getAnsweredSubmoduleCode() == questionnaireModuleObject.getApplicableSubmoduleCode()
                                && questionnaireModuleObject.getApplicableModuleItemKey().equals(questionnaireQuestionsBean.getAnsweredModuleItemKey())
                                && questionnaireQuestionsBean.getAnsweredModuleSubItemKey() == questionnaireModuleObject.getApplicableModuleSubItemKey()){
                            isAnswerPresent = true;
                            questionNumber = questionnaireQuestionsBean.getQuestionNumber();
                            break;
                        }
                    } else {
                        if(questionnaireQuestionsBean.getAnswer() != null
                                && !questionnaireQuestionsBean.getAnswer().equals("")
                                && questionnaireQuestionsBean.getAnsweredSubmoduleCode() == questionnaireModuleObject.getApplicableSubmoduleCode()
                                && questionnaireModuleObject.getApplicableModuleItemKey().equals(questionnaireQuestionsBean.getAnsweredModuleItemKey())){
                            isAnswerPresent = true;
                            questionNumber = questionnaireQuestionsBean.getQuestionNumber();
                            break;
                        }
                    }
                }
                
                if(!isAnswerPresent){
                    break;
                }
            }
            if(isAnswerPresent){
                lhmPagesAnswered.put(htQuestionnaireData.get(PAGE), cvQuestions);
                cvQuestions = questionnaireBuilder.getNextQuestions(htQuestionnaireData, cvQuestions,questionnaireModuleObject);
                if(cvQuestions == null || cvQuestions.isEmpty()){
                    if(!"Y".equals(questionnaireModuleObject.getQuestionnaireCompletionFlag())){
                        questionnaireModuleObject.setQuestionnaireCompletionFlag("Y");
                        questionnaireModuleObject.setAcType(TypeConstants.UPDATE_RECORD);
                        QuestionnaireUpdateTxnBean questionnaireUpdateTxnBean = new QuestionnaireUpdateTxnBean(loggedInUser);
                        questionnaireUpdateTxnBean.updateQuestionnaireHeader(questionnaireModuleObject);
                    }
                    htQuestionnaireData.put(PAGE, "1");
                    message = COMPLETED;
                    isAnswerPresent = false;
                }
            }
        } while(isAnswerPresent);
        // Modified for COEUSQA-3475 : Questionnaire Versioning vs. Answer Retention - End
        
        CoeusVector cvQuestionnaireDatas = new CoeusVector();
        CoeusVector cvQuestionnaireMenu = getQuestionnaireDetails(questionnaireModuleObject);
        queryEngine.addDataCollection(moduleKey+questionnaireModuleObject.getQuestionnaireId(),
                htQuestionnaireData);
        if(htQuestionnaireData.get(PAGE) == null){
            htQuestionnaireData.put(PAGE, "");
        }
        if((mode != null && mode.equals("D")) || message.equals(COMPLETED)){
            NotEquals eqAnswer = new NotEquals("answer", "");
            Equals eqAnswerNumber = new Equals("answerNumber", new Integer(0));
            Or orAns = new Or(eqAnswer, eqAnswerNumber);
            CoeusVector cvFilteredData =
                    questionnaireBuilder.getQuestionsForDisplayMode(htQuestionnaireData).filter(orAns);
            cvQuestionnaireDatas.add(cvFilteredData);
        } else {
            if(isLastAnsweredPageToDisplay){
                if(htQuestionnaireData.get(PAGE) != null
                        && !htQuestionnaireData.get(PAGE).equals("1")){
                    cvQuestions = questionnaireBuilder.getPreviousQuestions(htQuestionnaireData);
                }
            }
            cvQuestionnaireDatas.add(cvQuestions);
        }
        cvQuestionnaireDatas.add(cvQuestionnaireMenu);
        cvQuestionnaireDatas.add(""+lhmQuestionnaireTree.size());
        hmQuestionnaireData.put(DATA_OBJECT, cvQuestionnaireDatas);
        hmQuestionnaireData.put("message", message);
        hmQuestionnaireData.put(PAGE, htQuestionnaireData.get(PAGE).toString());

        return hmQuestionnaireData;
    }

    /**
     * This method is used to merge the questions with answers
     * @param cvQuestions
     * @param cvAnswers
     * @return CoeusVector contains Questions with Answers
     */
    private CoeusVector getQuestionsData(CoeusVector cvQuestions, CoeusVector cvAnswers) {
        QuestionnaireQuestionsBean bean = null;
        if(cvQuestions != null && cvQuestions.size() > 0
                && cvAnswers != null && cvAnswers.size() > 0){
            try{

            for(int index = 0; index < cvQuestions.size(); index ++) {
                bean = (QuestionnaireQuestionsBean)cvQuestions.get(index);
                for(int jIndex = 0; jIndex < cvAnswers.size(); jIndex ++) {
                    QuestionAnswerBean ansBean = (QuestionAnswerBean)cvAnswers.get(jIndex);
                    if(bean.getQuestionnaireId() == ansBean.getQuestionnaireId()
                        // 4272: Maintain history of Questionnaires
                        && bean.getQuestionnaireVersionNumber() == ansBean.getQuestionnaireVersionNumber()) {
                        if((bean.getQuestionId().intValue() == ansBean.getQuestionId().intValue())
                        &&(bean.getQuestionNumber().intValue() == ansBean.getQuestionNumber())
                        && bean.getAnswerNumber() == ansBean.getAnswerNumber()){
                            bean.setAnswer(ansBean.getAnswer());
                            bean.setAwAnswer(ansBean.getAwAnswer());
                            // Added with CoeusQA2313: Completion of Questionnaire for Submission
                            bean.setAnsweredModuleItemKey(ansBean.getAnsweredModuleItemKey());
                            bean.setAnsweredSubmoduleCode(ansBean.getAnsweredSubmoduleCode());
                            bean.setAnsweredModuleSubItemKey(ansBean.getAnsweredModuleSubItemKey());
                            // CoeusQA2313: Completion of Questionnaire for Submission  - End
                            bean.setAnswerNumber(ansBean.getAnswerNumber());
                            bean.setAwQuestionnaireCompletionId(ansBean.getQuestionnaireCompletionId());
                            bean.setSearchName(ansBean.getDescription());
                            bean.setAcType(TypeConstants.UPDATE_RECORD);
                            bean.setUpdateTimestamp(ansBean.getUpdateTimestamp());
                            bean.setUpdateUser(ansBean.getUpdateUser());
                        }
                    }
                }
            }
            }catch (Exception exception){
            exception.printStackTrace();
            UtilFactory.log(exception.getMessage());
            }
        }
        return cvQuestions;
    }

    /**
     * This method will give all the questions (including questions with multiple answers)
     * @param lhmQuestionnaireTree
     * @param questionnaireModuleObject
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @throws edu.mit.coeus.exception.CoeusException
     * @return LinkedHashMap
     */
    private LinkedHashMap getAllQuestionnaireQuestions(LinkedHashMap lhmQuestionnaireTree,
            QuestionnaireAnswerHeaderBean questionnaireModuleObject) throws DBException, CoeusException{
        if(lhmQuestionnaireTree != null && !lhmQuestionnaireTree.isEmpty()){
            java.util.Set keySet = lhmQuestionnaireTree.keySet();
            Object[] objQuestions = keySet.toArray();
            QuestionnaireTxnBean questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);
            CoeusVector cvAnswersData = questionnaireTxnBean.getQuestionnaireAnswersDetails(questionnaireModuleObject);
            for(int page = 0; page < objQuestions.length; page++){
                CoeusVector cvAllQuestionnaireQuestions = new CoeusVector();
                CoeusVector cvPageQuestions = (CoeusVector) lhmQuestionnaireTree.get(objQuestions[page]);
                for(int index = 0; index < cvPageQuestions.size(); index++){
                    QuestionnaireQuestionsBean questionnaireQuestionsBean =
                            (QuestionnaireQuestionsBean) cvPageQuestions.get(index);
                    if(questionnaireQuestionsBean.getMaxAnswers() > 0){
                        for(int count = 0; count <= questionnaireQuestionsBean.getMaxAnswers(); count++){
                            QuestionnaireQuestionsBean bean = new QuestionnaireQuestionsBean();
                            bean.setAnswerDataType(questionnaireQuestionsBean.getAnswerDataType());
                            bean.setAnswerMaxLength(questionnaireQuestionsBean.getAnswerMaxLength());
                            bean.setAnswerNumber(count);
                            bean.setLookUpGui(questionnaireQuestionsBean.getLookUpGui());
                            bean.setLookUpName(questionnaireQuestionsBean.getLookUpName());
                            bean.setMaxAnswers(questionnaireQuestionsBean.getMaxAnswers());
                            bean.setSearchName(questionnaireQuestionsBean.getSearchName());
                            bean.setValidAnswer(questionnaireQuestionsBean.getValidAnswer());
                            bean.setCompletedFlag(questionnaireQuestionsBean.isCompletedFlag());
                            bean.setCondition(questionnaireQuestionsBean.getCondition());
                            bean.setConditionValue(questionnaireQuestionsBean.getConditionValue());
                            bean.setConditionalFlag(questionnaireQuestionsBean.isConditionalFlag());
                            bean.setDescription(questionnaireQuestionsBean.getDescription());
                            bean.setFinalFlag(questionnaireQuestionsBean.isFinalFlag());
                            bean.setModuleItemCode(questionnaireQuestionsBean.getModuleItemCode());
                            bean.setModuleSubItemCode(questionnaireQuestionsBean.getModuleSubItemCode());
                            bean.setName(questionnaireQuestionsBean.getName());
                            bean.setParentQuestionNumber(questionnaireQuestionsBean.getParentQuestionNumber());
                            bean.setQuestionId(questionnaireQuestionsBean.getQuestionId());
                            // 4272: Maintain history of Questionnaires - Start
                            bean.setQuestionVersionNumber(questionnaireQuestionsBean.getQuestionVersionNumber());
                            bean.setQuestionNumber(questionnaireQuestionsBean.getQuestionNumber());
                            bean.setQuestionSequenceNumber(questionnaireQuestionsBean.getQuestionSequenceNumber());
                            bean.setQuestionnaireId(questionnaireQuestionsBean.getQuestionnaireId());
                            // 4272: Maintain history of Questionnaires - Start
                            bean.setQuestionnaireVersionNumber(questionnaireQuestionsBean.getQuestionnaireVersionNumber());
                            bean.setQuestionnaireNumber(questionnaireQuestionsBean.getQuestionnaireNumber());
                            bean.setUpdateTimestamp(questionnaireQuestionsBean.getUpdateTimestamp());
                            bean.setUpdateUser(questionnaireQuestionsBean.getUpdateUser());
                            // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
                            bean.setConditionRuleId(questionnaireQuestionsBean.getConditionRuleId());
                            // Added for COEUSQA-3287 Questionnaire Maintenance Features - End
                            cvAllQuestionnaireQuestions.add(bean);
                        }
                    } else {
                        questionnaireQuestionsBean.setAnswerNumber(1);
                        cvAllQuestionnaireQuestions.add(questionnaireQuestionsBean);
                    }
                }
                cvAllQuestionnaireQuestions = getQuestionsData(cvAllQuestionnaireQuestions, cvAnswersData);
                lhmQuestionnaireTree.put(objQuestions[page], cvAllQuestionnaireQuestions);
            }
        }
        return lhmQuestionnaireTree;
    }

    /**
     *
     * @param questionnaireModuleObject
     * @param moduleKey
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws java.lang.Exception
     * @return HashMap contains questions to display and page number
     */
    public HashMap getPreviousQuestions(QuestionnaireAnswerHeaderBean questionnaireModuleObject,
            String moduleKey) throws DBException, CoeusException, Exception{
        HashMap hmQuestionnaireData = new HashMap();
        Hashtable htQuestionnaireData = queryEngine.getDataCollection(moduleKey+questionnaireModuleObject.getQuestionnaireId());
        questionnaireBuilder = new QuestionnaireQuestionsBuilder();
        CoeusVector cvQuestionAnswers = questionnaireBuilder.getPreviousQuestions(htQuestionnaireData);
        CoeusVector cvQuestionnaireDatas = new CoeusVector();
        cvQuestionnaireDatas.add(cvQuestionAnswers);
        if(htQuestionnaireData.get(PAGE) == null){
            htQuestionnaireData.put(PAGE, "");
        }
        hmQuestionnaireData.put(PAGE, htQuestionnaireData.get(PAGE).toString());
        hmQuestionnaireData.put(DATA_OBJECT, cvQuestionnaireDatas);
        return hmQuestionnaireData;
    }

    /**
     * To save the answers and to get the next page questions according to the answers
     * @param questionnaireModuleObject
     * @param moduleKey
     * @param cvQuestionAnswers contains answered questions
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws java.lang.Exception
     * @return HashMap contains questions to display, page number and message
     */
    public HashMap saveAndGetNextQuestions(QuestionnaireAnswerHeaderBean questionnaireModuleObject,
            String moduleKey, CoeusVector cvQuestionAnswers) throws DBException, CoeusException, Exception{
        HashMap hmQuestionnaireData = new HashMap();
        String message = EMPTY_STRING;
        String qstnAnsFlag  = EMPTY_STRING;
        QuestionnaireUpdateTxnBean questionnaireUpdateTxnBean = new QuestionnaireUpdateTxnBean(loggedInUser);
        Hashtable htQuestionnaireData = queryEngine.getDataCollection(moduleKey+questionnaireModuleObject.getQuestionnaireId());
        LinkedHashMap lhmPagesAnswered =(LinkedHashMap) htQuestionnaireData.get(PAGES_ANSWERED);
        lhmPagesAnswered.put(htQuestionnaireData.get(PAGE), cvQuestionAnswers);
        questionnaireBuilder = new QuestionnaireQuestionsBuilder();
        //delete all the answered questions which are present in the future pages
        questionnaireBuilder.deleteAnsweredQuestion(htQuestionnaireData);

        //Save the answers to the DB
        if(questionnaireUpdateTxnBean.saveQuestionnaireAnswers(questionnaireModuleObject, lhmPagesAnswered,false)){
            // Added for COEUSQA-3723 : Questionnaire with question rules does not save and complete process - Start
            LinkedHashMap lhmQuestionnaireTree = (LinkedHashMap) htQuestionnaireData.get("questionnaireTree");
            // For Example : when an answer is changed from 'Yes' to 'No', 
            // entire conditional questions answers for the condition 'Yes' will be deleted
            delQnrConditionalAnsForFailedCondition(cvQuestionAnswers,lhmQuestionnaireTree,questionnaireModuleObject);
            CoeusVector cvQuestionnaireAnswerDetails = questionnaireTxnBean.getQuestionnaireAnswersDetails(questionnaireModuleObject);
            // Added for COEUSQA-3723 : Questionnaire with question rules does not save and complete process - End
            qstnAnsFlag = questionnaireUpdateTxnBean.QuestionAnsFlag;
//            htQuestionnaireData = queryEngine.getDataCollection(moduleKey+questionnaireModuleObject.getQuestionnaireId());
            // Get the next page questions
            cvQuestionAnswers = questionnaireBuilder.getNextQuestions(htQuestionnaireData, cvQuestionAnswers,questionnaireModuleObject);
            
            
            if(cvQuestionAnswers == null || cvQuestionAnswers.isEmpty()){
                if(!"Y".equals(questionnaireModuleObject.getQuestionnaireCompletionFlag())){
                    questionnaireModuleObject.setQuestionnaireCompletionFlag("Y");
                    questionnaireModuleObject.setAcType(TypeConstants.UPDATE_RECORD);
                    questionnaireUpdateTxnBean.updateQuestionnaireHeader(questionnaireModuleObject);
                }
                htQuestionnaireData.put(PAGE, "1");
                cvQuestionAnswers =(CoeusVector) lhmPagesAnswered.get("1");
                message = COMPLETED;
            // Added for COEUSQA-3723 : Questionnaire with question rules does not save and complete process - Start
            // Answers will be taken from the database and updated to the beans
            }else if(cvQuestionnaireAnswerDetails != null && !cvQuestionnaireAnswerDetails.isEmpty()){
                 // Added to check answer is exists in the database
                updateStoredAnswerToNewAnswerBean(cvQuestionnaireAnswerDetails,cvQuestionAnswers,questionnaireModuleObject);
            } 
            // Added for COEUSQA-3723 : Questionnaire with question rules does not save and complete process - End
        }
        if(htQuestionnaireData.get(PAGE) == null){
            htQuestionnaireData.put(PAGE, "");
        }

        CoeusVector cvQuestionnaireDatas = new CoeusVector();
        CoeusVector cvQuestionnaireMenu = getQuestionnaireDetails(questionnaireModuleObject);
        //if questionnaire is complete then get all the answered questions to display
        if(message.equals(COMPLETED)){
            NotEquals eqAnswer = new NotEquals("answer", "");
            Equals eqAnswerNumber = new Equals("answerNumber", new Integer(0));
            Or orAns = new Or(eqAnswer, eqAnswerNumber);
            CoeusVector cvFilteredData =
                    questionnaireBuilder.getQuestionsForDisplayMode(htQuestionnaireData).filter(orAns);
            cvQuestionnaireDatas.add(cvFilteredData);
        } else {
            cvQuestionnaireDatas.add(cvQuestionAnswers);
        }
        cvQuestionnaireDatas.add(cvQuestionnaireMenu);
        hmQuestionnaireData.put(PAGE, htQuestionnaireData.get(PAGE).toString());
        hmQuestionnaireData.put(DATA_OBJECT, cvQuestionnaireDatas);
        hmQuestionnaireData.put("message", message);
        hmQuestionnaireData.put("qstnAnsFlag", qstnAnsFlag);
        return hmQuestionnaireData;
    }
    
    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - Start
    /**
     * To save the answers and to get the next page questions according to the answers
     * @param questionnaireModuleObject
     * @param moduleKey
     * @param cvQuestionAnswers contains answered questions
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws java.lang.Exception
     * @return HashMap contains questions to display, page number and message
     */
    public HashMap saveAndCompleteQuestionnaire(QuestionnaireAnswerHeaderBean questionnaireModuleObject,
            String moduleKey, CoeusVector cvQuestionAnswers,QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean) throws DBException, CoeusException, Exception{
        HashMap hmQuestionnaireData = new HashMap();
        String message = EMPTY_STRING;
        String qstnAnsFlag  = EMPTY_STRING;
        QuestionnaireUpdateTxnBean questionnaireUpdateTxnBean = new QuestionnaireUpdateTxnBean(loggedInUser);
        Hashtable htQuestionnaireData = queryEngine.getDataCollection(moduleKey+questionnaireModuleObject.getQuestionnaireId());
        LinkedHashMap lhmPagesAnswered =(LinkedHashMap) htQuestionnaireData.get(PAGES_ANSWERED);
        lhmPagesAnswered.put(htQuestionnaireData.get(PAGE), cvQuestionAnswers);
        questionnaireBuilder = new QuestionnaireQuestionsBuilder();
        //delete all the answered questions which are present in the future pages
        questionnaireBuilder.deleteAnsweredQuestion(htQuestionnaireData);
        
        //Save the answers to the DB
        if(questionnaireUpdateTxnBean.saveQuestionnaireAnswers(questionnaireModuleObject, lhmPagesAnswered,true)){
            // Added for COEUSQA-3723 : Questionnaire with question rules does not save and complete process - Start
            // For Example : when an answer is changed from 'Yes' to 'No',
            // entire conditional questions answers for the condition 'Yes' will be deleted
            LinkedHashMap lhmQuestionnaireTree = (LinkedHashMap) htQuestionnaireData.get("questionnaireTree");
            delQnrConditionalAnsForFailedCondition(cvQuestionAnswers,lhmQuestionnaireTree,questionnaireModuleObject);
            // Added for COEUSQA-3723 : Questionnaire with question rules does not save and complete process - End
            qstnAnsFlag = questionnaireUpdateTxnBean.QuestionAnsFlag;
            // Get the next page conditional
            cvQuestionAnswers = questionnaireBuilder.getNextCondtionalQuestions(htQuestionnaireData, cvQuestionAnswers,questionnaireAnswerHeaderBean);
            if(cvQuestionAnswers == null || cvQuestionAnswers.isEmpty()){
                if(!"Y".equals(questionnaireModuleObject.getQuestionnaireCompletionFlag())){
                    questionnaireModuleObject.setQuestionnaireCompletionFlag("Y");
                    questionnaireModuleObject.setAcType(TypeConstants.UPDATE_RECORD);
                    questionnaireUpdateTxnBean.updateQuestionnaireHeader(questionnaireModuleObject);
                }
                htQuestionnaireData.put(PAGE, "1");
                cvQuestionAnswers =(CoeusVector) lhmPagesAnswered.get("1");
                message = COMPLETED;
            }
        }
        if(htQuestionnaireData.get(PAGE) == null){
            htQuestionnaireData.put(PAGE, "");
        }
        
        CoeusVector cvQuestionnaireDatas = new CoeusVector();
        CoeusVector cvQuestionnaireMenu = getQuestionnaireDetails(questionnaireModuleObject);
        //if questionnaire is complete then get all the answered questions to display
        if(message.equals(COMPLETED)){
//            NotEquals eqAnswer = new NotEquals("answer", "");
//            Equals eqAnswerNumber = new Equals("answerNumber", new Integer(0));
//            Or orAns = new Or(eqAnswer, eqAnswerNumber);
//            CoeusVector cvQnrAnsDetails = questionnaireTxnBean.getQuestionnaireAnswersDetails(questionnaireAnswerHeaderBean);
//            CoeusVector cvQuesionData = questionnaireTxnBean.getQuestionnaireQuestionsData(questionnaireModuleObject.getQuestionnaireId(), questionnaireModuleObject.getQuestionnaireVersionNumber());
//            Vector vecSortedQuestion = QuestionnaireStream.getSortedVector(cvQuesionData);
//            Equals eqCompletion = new Equals("questionnaireCompletionId",questionnaireModuleObject.getQuestionnaireCompletionId());
//            cvQnrAnsDetails = cvQnrAnsDetails.filter(eqCompletion);
            HashMap hmQuestionnaire = getQuestionnaireQuestions(questionnaireModuleObject,questionnaireModuleObject.getModuleItemKey(), "M");
            CoeusVector cvFilteredData = (CoeusVector)hmQuestionnaire.get("dataObject");
            cvQuestionnaireDatas.addAll(cvFilteredData);
        } else {
            CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
            message = coeusMessageResourcesBean.parseMessageKey("questionnaire_exceptionCode.1020");
            cvQuestionnaireDatas.add(cvQuestionAnswers);
        }
        cvQuestionnaireDatas.add(cvQuestionnaireMenu);
        hmQuestionnaireData.put(PAGE, htQuestionnaireData.get(PAGE).toString());
        hmQuestionnaireData.put(DATA_OBJECT, cvQuestionnaireDatas);
        hmQuestionnaireData.put("message", message);
        hmQuestionnaireData.put("qstnAnsFlag", qstnAnsFlag);
        return hmQuestionnaireData;
    }
    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - End
    
    /**
     * To get the Questionnaire menu data with completion flag
     * @param questionnaireModuleObject
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @throws edu.mit.coeus.exception.CoeusException
     * @return CoeusVector
     */
    public CoeusVector getQuestionnaireDetails(QuestionnaireAnswerHeaderBean questionnaireModuleObject)
    throws DBException, CoeusException{
        // COEUSDEV-230: Answered questionnaire says it is not Answered in Approval in Progress Proposal - Start
//        QuestionnaireTxnBean questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);
//        CoeusVector cvQuestionnaireData = questionnaireTxnBean.getQuestionnaireModeDetails(questionnaireModuleObject);
//        CoeusVector cvQuestionnaireHeaderData = questionnaireTxnBean.getQuestionnaireHeaderDetails(questionnaireModuleObject);
//        //Code modified for Case#2785 - Routing Enhancements - starts
//        //To filtered the Questionnaire according to the rules satisfied
//        HashMap questionnaireId = questionnaireTxnBean.getRuleBasedQuestionnaire(questionnaireModuleObject);
//
//        if(cvQuestionnaireData != null){
//            for(int index = 0; index < cvQuestionnaireData.size(); index++){
//                QuestionnaireAnswerHeaderBean questionnaireHeaderBean =
//                        (QuestionnaireAnswerHeaderBean)cvQuestionnaireData.get(index);
//                if(!questionnaireId.containsValue(""+questionnaireHeaderBean.getQuestionnaireId())){
//                    cvQuestionnaireData.remove(index--);
//                }
//            }
//        }
//        if(cvQuestionnaireHeaderData != null && cvQuestionnaireData != null){
//            boolean found = false;
//            for(int index = 0; index < cvQuestionnaireHeaderData.size(); index++){
//                QuestionnaireAnswerHeaderBean questionnaireAnswerBean =
//                        (QuestionnaireAnswerHeaderBean)cvQuestionnaireHeaderData.get(index);
//                found = false;
//                QuestionnaireAnswerHeaderBean questionnaireHeaderBean = null;
//                for(int questionnaireIndex = 0; questionnaireIndex<cvQuestionnaireData.size(); questionnaireIndex++ ){
//                    questionnaireHeaderBean =
//                        (QuestionnaireAnswerHeaderBean)cvQuestionnaireData.get(questionnaireIndex);
//                    if(questionnaireHeaderBean.getQuestionnaireId() == questionnaireAnswerBean.getQuestionnaireId()){
//                        questionnaireHeaderBean.setQuestionnaireCompletionFlag(
//                                questionnaireAnswerBean.getQuestionnaireCompletionFlag());
//                        // 4272: Maintain History of Questionnaires - Start
//                        questionnaireHeaderBean.setQuestionnaireVersionNumber(questionnaireAnswerBean.getQuestionnaireVersionNumber());
//                        found = true;
//                        break;
//                    }
//                }
//                if(!found && questionnaireAnswerBean != null ){
//                    questionnaireAnswerBean.setLabel(questionnaireAnswerBean.getName());
//                    cvQuestionnaireData.add(questionnaireAnswerBean);
//                }
//            }
//        }
//        return cvQuestionnaireData;
        // COEUSDEV-230: Answered questionnaire says it is not Answered in Approval in Progress Proposal - End
        ///////////////////
//        QuestionnaireTxnBean questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);
//        CoeusVector cvQuestionnaireData = questionnaireTxnBean.getQuestionnaireModeDetails(questionnaireModuleObject);
//        CoeusVector cvQuestionnaireHeaderData = questionnaireTxnBean.getQuestionnaireHeaderDetails(questionnaireModuleObject);
//        //Code modified for Case#2785 - Routing Enhancements - starts
//        //To filtered the Questionnaire according to the rules satisfied
//        HashMap questionnaireId = questionnaireTxnBean.getRuleBasedQuestionnaire(questionnaireModuleObject);
//        if(cvQuestionnaireData != null && !cvQuestionnaireData.isEmpty()){
//            for(int index = 0; index < cvQuestionnaireData.size(); index++){
//                QuestionnaireAnswerHeaderBean headerMenuBean =
//                        (QuestionnaireAnswerHeaderBean) cvQuestionnaireData.get(index);
//                boolean isValid = false;
//                if(cvQuestionnaireHeaderData != null && !cvQuestionnaireHeaderData.isEmpty()){
//                    for(int count = 0; count < cvQuestionnaireHeaderData.size(); count++){
//                        QuestionnaireAnswerHeaderBean headerBean =
//                                (QuestionnaireAnswerHeaderBean) cvQuestionnaireHeaderData.get(count);
//                        if(headerMenuBean.getQuestionnaireId() == headerBean.getQuestionnaireId()){
//                            headerMenuBean.setQuestionnaireCompletionFlag(
//                                    headerBean.getQuestionnaireCompletionFlag());
//                            isValid = true;
//                            break;
//                        }
//                    }
//                }
//                String quesId = ""+headerMenuBean.getQuestionnaireId();
//                if(!isValid && questionnaireId.get(quesId) == null){
//                    cvQuestionnaireData.remove(index--);
//                }
//            }
//        }
//        //Code modified for Case#2785 - Routing Enhancements - ends
//        return cvQuestionnaireData;
        // COEUSDEV-230: Answered questionnaire says it is not Answered in Approval in Progress Proposal - Start
        QuestionnaireTxnBean questionnaireTxnBean = new QuestionnaireTxnBean(loggedInUser);

        CoeusVector cvFilteredQuestionnaire = new CoeusVector();
        // Fetch list of questionnaired applicable for a protocol/proposal
        CoeusVector cvApplicabeQuestionnaire = questionnaireTxnBean.fetchApplicableQuestionnairedForModule(questionnaireModuleObject);
        // Fetch all the answered questionnaires for a protocol/Proposal
        CoeusVector cvAnsweredQuestionnaire = questionnaireTxnBean.getQuestionnaireHeaderDetails(questionnaireModuleObject);
        // Compare the answered questionnaire data with applicable questionnaire data.
        // If the same questionnaire data is present in both, set the answered data to the filtered vector
        // If the data is not present in the answered questionnaire data, but present in applicable questionnaire data, add the data from
        // applicable questionnaire data to the filtered vector.
        if(cvApplicabeQuestionnaire != null && !cvApplicabeQuestionnaire.isEmpty()){
            int appilcabeQnrCount = cvApplicabeQuestionnaire.size();
            int answeredQnrCount = 0;
            if(cvAnsweredQuestionnaire != null && !cvAnsweredQuestionnaire.isEmpty()){
                answeredQnrCount = cvAnsweredQuestionnaire.size();
            }
            for(int appilcabeQnrIndex = 0; appilcabeQnrIndex < appilcabeQnrCount; appilcabeQnrIndex++){
                boolean answerBeanPresent = false;
                QuestionnaireAnswerHeaderBean applicableQnrBean =
                        (QuestionnaireAnswerHeaderBean)cvApplicabeQuestionnaire.get(appilcabeQnrIndex);

                if(answeredQnrCount > 0){
                    for(int answeredQnrIndex = 0; answeredQnrIndex < answeredQnrCount; answeredQnrIndex++){

                        QuestionnaireAnswerHeaderBean answeredQnrBean =
                                (QuestionnaireAnswerHeaderBean)cvAnsweredQuestionnaire.get(answeredQnrIndex);

                        if(applicableQnrBean.getQuestionnaireId() == answeredQnrBean.getQuestionnaireId()
                        // Added with CoeusQA2313: Completion of Questionnaire for Submission
                            && applicableQnrBean.getApplicableSubmoduleCode() == answeredQnrBean.getApplicableSubmoduleCode()){
                            // CoeusQA2313: Completion of Questionnaire for Submission - End

                            /*ppc if*/
                            if((!(questionnaireModuleObject.getModuleItemCode()==3 && questionnaireModuleObject.getModuleSubItemCode()==6))){

                            if(questionnaireModuleObject.getQuestionnaireId() != 0){
                                if(applicableQnrBean.getQuestionnaireVersionNumber() == questionnaireModuleObject.getQuestionnaireVersionNumber()
                                    && applicableQnrBean.getQuestionnaireId() == questionnaireModuleObject.getQuestionnaireId()
                                    && applicableQnrBean.getApplicableSubmoduleCode() == questionnaireModuleObject.getApplicableSubmoduleCode()
                                    && questionnaireModuleObject.getApplicableModuleItemKey().equals(applicableQnrBean.getModuleItemKey())
                                    && questionnaireModuleObject.getApplicableModuleSubItemKey() == applicableQnrBean.getApplicableModuleSubItemKey()){
                                    if(applicableQnrBean.getQuestionnaireVersionNumber() == answeredQnrBean.getQuestionnaireVersionNumber()) {
                                        applicableQnrBean.setQuestionnaireCompletionFlag(answeredQnrBean.getQuestionnaireCompletionFlag());
                                        applicableQnrBean.setQuestionnaireCompletionId(answeredQnrBean.getQuestionnaireCompletionId());
                                        applicableQnrBean.setName(answeredQnrBean.getName());
                                    }
                                    cvFilteredQuestionnaire.add(applicableQnrBean);

                                } else {
                                    cvFilteredQuestionnaire.add(answeredQnrBean);
                                }

                            } else {

                                cvFilteredQuestionnaire.add(answeredQnrBean);

                            }
                            }
                            /*ppc else*/
                            else
                            {
                                                         if(questionnaireModuleObject.getQuestionnaireId() != 0){
                                if(applicableQnrBean.getQuestionnaireVersionNumber() == questionnaireModuleObject.getQuestionnaireVersionNumber()
                                    && applicableQnrBean.getQuestionnaireId() == questionnaireModuleObject.getQuestionnaireId()
                                    && applicableQnrBean.getApplicableSubmoduleCode() == questionnaireModuleObject.getApplicableSubmoduleCode()
                                    && questionnaireModuleObject.getApplicableModuleItemKey().equals(applicableQnrBean.getModuleItemKey())
                                    && questionnaireModuleObject.getApplicableModuleSubItemKeyForPpc().equalsIgnoreCase(applicableQnrBean.getApplicableModuleSubItemKeyForPpc())){
                                    if(applicableQnrBean.getQuestionnaireVersionNumber() == answeredQnrBean.getQuestionnaireVersionNumber()) {
                                        applicableQnrBean.setQuestionnaireCompletionFlag(answeredQnrBean.getQuestionnaireCompletionFlag());
                                        applicableQnrBean.setQuestionnaireCompletionId(answeredQnrBean.getQuestionnaireCompletionId());
                                        applicableQnrBean.setName(answeredQnrBean.getName());
                                    }
                                    cvFilteredQuestionnaire.add(applicableQnrBean);

                                } else {
                                    cvFilteredQuestionnaire.add(answeredQnrBean);
                                }

                            } else {

                                cvFilteredQuestionnaire.add(answeredQnrBean);

                            }
                            }


                            answerBeanPresent = true;
                            break;
                        }
                    }
                }
                if(!answerBeanPresent){
                    cvFilteredQuestionnaire.add(applicableQnrBean);
                }

            }

            // COEUSQA-2321: Copy Questionnaires for Proposal Development records - Start
            // Display the questionnaires which was answered for the module, but currently not applicable for module.
            for(int answeredQnrIndex = 0; answeredQnrIndex < answeredQnrCount; answeredQnrIndex++){
                QuestionnaireAnswerHeaderBean answeredQnrBean =
                        (QuestionnaireAnswerHeaderBean)cvAnsweredQuestionnaire.get(answeredQnrIndex);
                boolean found = false;
                for(int appilcabeQnrIndex = 0; appilcabeQnrIndex < appilcabeQnrCount; appilcabeQnrIndex++){
                    QuestionnaireAnswerHeaderBean applicableQnrBean =
                            (QuestionnaireAnswerHeaderBean)cvApplicabeQuestionnaire.get(appilcabeQnrIndex);

                    if(applicableQnrBean.getQuestionnaireId() == answeredQnrBean.getQuestionnaireId()
                        // Added with CoeusQA2313: Completion of Questionnaire for Submission
                            && applicableQnrBean.getApplicableSubmoduleCode() == answeredQnrBean.getApplicableSubmoduleCode()){
                            // CoeusQA2313: Completion of Questionnaire for Submission - End
                        found = true;
                        break;
                    }
                }
                    if(!found){
                        if(answeredQnrBean.getLabel() == null || answeredQnrBean.getLabel().equals("")){
                            answeredQnrBean.setLabel(answeredQnrBean.getName());
                        }

                        cvFilteredQuestionnaire.add(answeredQnrBean);
                    }
            }
            // COEUSQA-2321: Copy Questionnaires for Proposal Development records - eND

        } else {
            cvFilteredQuestionnaire = cvAnsweredQuestionnaire;
        }





        return cvFilteredQuestionnaire;
        // COEUSDEV-230: Answered questionnaire says it is not Answered in Approval in Progress Proposal - End
    }

    /**
     * This method is used to save the questionnaire answeres
     * and retain in the same page
     * @param questionnaireModuleObject
     * @param moduleKey
     * @param cvQuestionAnswers answered datas
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws java.lang.Exception
     * @return HashMap contains questions to display and page number
     */
    public HashMap saveQuestionnaire(QuestionnaireAnswerHeaderBean questionnaireModuleObject,
            String moduleKey, CoeusVector cvQuestionAnswers) throws DBException, CoeusException, Exception{
        HashMap hmQuestionnaireData = new HashMap();
        String message = EMPTY_STRING;
        String qstnAnsFlag;
        QuestionnaireUpdateTxnBean questionnaireUpdateTxnBean = new QuestionnaireUpdateTxnBean(loggedInUser);
        Hashtable htQuestionnaireData = queryEngine.getDataCollection(moduleKey+questionnaireModuleObject.getQuestionnaireId());
        LinkedHashMap lhmPagesAnswered =(LinkedHashMap) htQuestionnaireData.get(PAGES_ANSWERED);
        lhmPagesAnswered.put(htQuestionnaireData.get(PAGE), cvQuestionAnswers);

        //Save the answers to the DB
//
//        int m=questionnaireModuleObject.getModuleSubItemCode();
//          String c=questionnaireModuleObject.getCurrentUser();
//        questionnaireUpdateTxnBean = new QuestionnaireUpdateTxnBean(m,c);


        questionnaireUpdateTxnBean.saveQuestionnaireAnswers(questionnaireModuleObject, lhmPagesAnswered,false);
        // Added for COEUSQA-3723 : Questionnaire with question rules does not save and complete process - Start
        // For Example : when an answer is changed from 'Yes' to 'No',
        // entire conditional questions answers for the condition 'Yes' will be deleted
        LinkedHashMap lhmQuestionnaireTree = (LinkedHashMap) htQuestionnaireData.get("questionnaireTree");
        delQnrConditionalAnsForFailedCondition(cvQuestionAnswers,lhmQuestionnaireTree,questionnaireModuleObject);
                // Added for COEUSQA-3723 : Questionnaire with question rules does not save and complete process - End
        qstnAnsFlag = (String)questionnaireUpdateTxnBean.QuestionAnsFlag;
        if(htQuestionnaireData.get(PAGE) == null){
            htQuestionnaireData.put(PAGE, "");
        }
        if(isCheckForNextPage){
            questionnaireBuilder = new QuestionnaireQuestionsBuilder();
            CoeusVector cvQuestions = questionnaireBuilder.getNextQuestions(htQuestionnaireData, cvQuestionAnswers,questionnaireModuleObject);
            if(cvQuestions == null || cvQuestions.isEmpty()){
                if(!"Y".equals(questionnaireModuleObject.getQuestionnaireCompletionFlag())){
                    questionnaireModuleObject.setQuestionnaireCompletionFlag("Y");
                    questionnaireModuleObject.setAcType(TypeConstants.UPDATE_RECORD);
                    questionnaireUpdateTxnBean.updateQuestionnaireHeader(questionnaireModuleObject);
                }
                htQuestionnaireData.put(PAGE, "1");
                cvQuestions =(CoeusVector) lhmPagesAnswered.get("1");
                message = COMPLETED;
            }
            //if questionnaire is complete then get all the answered questions to display
            if(message.equals(COMPLETED)){
                NotEquals eqAnswer = new NotEquals("answer", "");
                Equals eqAnswerNumber = new Equals("answerNumber", new Integer(0));
                Or orAns = new Or(eqAnswer, eqAnswerNumber);
                CoeusVector cvFilteredData =
                        questionnaireBuilder.getQuestionsForDisplayMode(htQuestionnaireData).filter(orAns);
                cvQuestionAnswers = cvFilteredData;
            } else {
                cvQuestionAnswers = questionnaireBuilder.getPreviousQuestions(htQuestionnaireData);
            }
        }
        CoeusVector cvQuestionnaireDatas = new CoeusVector();
        CoeusVector cvQuestionnaireMenu = getQuestionnaireDetails(questionnaireModuleObject);
        cvQuestionnaireDatas.add(cvQuestionAnswers);
        cvQuestionnaireDatas.add(cvQuestionnaireMenu);
        hmQuestionnaireData.put("message", message);
        hmQuestionnaireData.put(PAGE, htQuestionnaireData.get(PAGE).toString());
        hmQuestionnaireData.put(DATA_OBJECT, cvQuestionnaireDatas);
        return hmQuestionnaireData;
    }

    /**
     * This method is used to restart or modify the questionnaire
     * @param questionnaireModuleObject
     * @param moduleKey
     * @param functionType (MODIFY or RESTART)
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws java.lang.Exception
     * @return HashMap contains questions to display and page number
     */
    public HashMap restartModifyQuestionnaire(QuestionnaireAnswerHeaderBean questionnaireModuleObject,
            String moduleKey, char functionType) throws DBException, CoeusException, Exception{
        HashMap hmQuestionnaireData = new HashMap();
        if(functionType == RESTART){
            // Deleting all the anwers for this questionnaire from DB
            QuestionnaireUpdateTxnBean questionnaireUpdateTxnBean = new QuestionnaireUpdateTxnBean(loggedInUser);
            questionnaireUpdateTxnBean.deleteAnswersForQuestCompletId(questionnaireModuleObject,
                    TypeConstants.DELETE_RECORD);
        }
        // Get the list of questions
        // 4272: Maintain history of Questionnaires - Start
//        CoeusVector cvQuestionnaireData
//                = questionnaireTxnBean.getQuestionnaireQuestionsDetails(questionnaireModuleObject.getQuestionnaireId());
        CoeusVector cvQuestionnaireData
                = questionnaireTxnBean.getQuestionnaireQuestionsDetails(questionnaireModuleObject);
        // 4272: Maintain history of Questionnaires - End

        // Get the list of answers
        CoeusVector cvAnswersData = questionnaireTxnBean.getQuestionnaireAnswersDetails(questionnaireModuleObject);
        //Merge questiona with answeres
        cvQuestionnaireData = getQuestionsData(cvQuestionnaireData, cvAnswersData);
        questionnaireBuilder = new QuestionnaireQuestionsBuilder(cvQuestionnaireData,loggedInUser);
        //building the questionnaire tree for the questions.
        questionnaireBuilder.buildQuestions(0);
        //holds the questionnaire tree
        LinkedHashMap lhmQuestionnaireTree = questionnaireBuilder.getLhmQuestionnaireTree();
        lhmQuestionnaireTree = getAllQuestionnaireQuestions(lhmQuestionnaireTree, questionnaireModuleObject);
        Hashtable htQuestionnaireData = new Hashtable();
        LinkedHashMap lhmPagesAnswered = new LinkedHashMap();
        htQuestionnaireData.put("originalQuestions", cvQuestionnaireData);
        htQuestionnaireData.put("questionnaireTree", lhmQuestionnaireTree);
        htQuestionnaireData.put("questionnaireLevel", questionnaireBuilder.getCvQuestionnaireLevel());
        htQuestionnaireData.put(PAGE, "1");
        lhmPagesAnswered.put(htQuestionnaireData.get(PAGE), null);
        htQuestionnaireData.put(PAGES_ANSWERED, lhmPagesAnswered);
        queryEngine.addDataCollection(moduleKey+questionnaireModuleObject.getQuestionnaireId(),
                htQuestionnaireData);
        if(htQuestionnaireData.get(PAGE) == null){
            htQuestionnaireData.put(PAGE, "");
        }
        CoeusVector cvQuestionnaireDatas = new CoeusVector();
        CoeusVector cvQuestionnaireMenu = getQuestionnaireDetails(questionnaireModuleObject);
        cvQuestionnaireDatas.add(lhmQuestionnaireTree.get(htQuestionnaireData.get(PAGE)));
        cvQuestionnaireDatas.add(cvQuestionnaireMenu);
        cvQuestionnaireDatas.add(""+lhmQuestionnaireTree.size());
        hmQuestionnaireData.put(PAGE, htQuestionnaireData.get(PAGE).toString());
        hmQuestionnaireData.put(DATA_OBJECT, cvQuestionnaireDatas);
        return hmQuestionnaireData;
    }
    
    // Added for COEUSQA-3475 : Questionnaire Versioning vs. Answer Retention - Start
    /**
     * Checks latest version is avaialble and retain the answer based on the canAddAnsInLatestVersion method
     * @param latestVersion 
     * @param versionAnswered 
     * @param mode 
     * @param questionnaireAnswerHeaderBean 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     */
    public void retainAnswersInNewVersion(int latestVersion, int versionAnswered,
            String mode,QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean) throws CoeusException, DBException{
        String displayFunctionType = ""+TypeConstants.DISPLAY_MODE;
        // When new version exists for the questionnaire, based on the condition defined in canAddAnsInLatestVersion method
        // all the answers will be copied to the new version
        if(latestVersion != 0 && !displayFunctionType.equals(mode)
        && versionAnswered != latestVersion && latestVersion != 1
                && latestVersion > versionAnswered && versionAnswered != 0 ){
            questionnaireAnswerHeaderBean.setQuestionnaireVersionNumber(latestVersion);
            // Gets the latest version questionnaire questions details
            CoeusVector cvQnrQuestionsInLatestVersion = questionnaireTxnBean.getQuestionnaireQuestionsDetails(questionnaireAnswerHeaderBean);
            questionnaireAnswerHeaderBean.setQuestionnaireVersionNumber(versionAnswered);
            // Gets the questionnaire questions details in the module questionnaire last answered version
            CoeusVector cvQnrQuestionsInPreviousVersion = questionnaireTxnBean.getQuestionnaireQuestionsDetails(questionnaireAnswerHeaderBean);
            CoeusVector cvAnsweredInPreviousVersion = questionnaireTxnBean.getQuestionnaireAnswersDetails(questionnaireAnswerHeaderBean);
            if(cvQnrQuestionsInLatestVersion != null && !cvQnrQuestionsInLatestVersion.isEmpty() &&
                    cvAnsweredInPreviousVersion != null && !cvAnsweredInPreviousVersion.isEmpty() ){
                QuestionnaireUpdateTxnBean questionnaireUpdateTxnBean = new QuestionnaireUpdateTxnBean(loggedInUser);
                for(Object questions :  cvQnrQuestionsInLatestVersion){
                    QuestionnaireQuestionsBean questionnaireQuestionsBean = (QuestionnaireQuestionsBean)questions;
                    // When the latest version question is a condition question, 
                    //user need to re-answer(the question is not inserted to the new version of the questionnaire)
                    // Commented to store the conditional question to the new version
//                    if(!questionnaireQuestionsBean.isConditionalFlag()){
                        int questionQuestionId = questionnaireQuestionsBean.getQuestionId().intValue();
                        int questionQuestionNumber = questionnaireQuestionsBean.getQuestionNumber().intValue();
                        int questionQuestionVerNum = questionnaireQuestionsBean.getQuestionVersionNumber().intValue();
                        Equals eqQuestionIdLatest = new Equals("questionId",questionQuestionId);
                        Equals eqversionNumberLatest = new Equals("questionnaireVersionNumber",versionAnswered);
                        And andQnsIdNum = new And(eqQuestionIdLatest, new Equals("questionNumber",questionQuestionNumber));
                        And  andQnsVersion = new And(andQnsIdNum,eqversionNumberLatest);
                        // Filter the questionId and QuestionNumber based answers details in the last version answer in the module
                        CoeusVector cvFilteredAnswerInPerVer = cvAnsweredInPreviousVersion.filter(andQnsVersion);
                        if(cvFilteredAnswerInPerVer != null && !cvFilteredAnswerInPerVer.isEmpty()){
                            CoeusVector cvQuestionDetailLatest = cvQnrQuestionsInLatestVersion.filter(eqQuestionIdLatest);
                            boolean canAddAnswerInLatestver = true;
                            if(cvQuestionDetailLatest != null && !cvQuestionDetailLatest.isEmpty()){
                                // checks answer to be insert to the new version
                                canAddAnswerInLatestver = 
                                        canAddAnsInLatestVersion(cvQnrQuestionsInPreviousVersion.filter(eqQuestionIdLatest),
                                        (QuestionnaireQuestionsBean)cvQuestionDetailLatest.get(0));
                            }
                            if(canAddAnswerInLatestver){
                                CoeusVector cvSave = new CoeusVector();
                                for(Object answer : cvFilteredAnswerInPerVer){
                                    QuestionnaireQuestionsBean questionnaireQuestionsDetails = copyQuesAnsDetailsToQnrQues((QuestionAnswerBean)answer);
                                    questionnaireQuestionsDetails.setQuestionVersionNumber(questionnaireQuestionsBean.getQuestionVersionNumber());
                                    cvSave.add(questionnaireQuestionsDetails);
                                }
                                LinkedHashMap lhmAnswers = new LinkedHashMap();
                                lhmAnswers.put("1",cvSave);
                                questionnaireAnswerHeaderBean.setQuestionnaireVersionNumber(latestVersion);
                                questionnaireUpdateTxnBean.saveQuestionnaireAnswers(questionnaireAnswerHeaderBean, lhmAnswers,false);
                            }
                        }
//                    }
                }
            }
        }
    }
    
    /**
     * Method to check whether to add the answer to the new version
     * @param cvQuestionDetailPrev 
     * @param questionsLatest 
     * @return canAddAnswerInLatestver
     */
    private boolean canAddAnsInLatestVersion(CoeusVector cvQuestionDetailPrev,QuestionnaireQuestionsBean questionsLatest){
        boolean canAddAnswerInLatestver = true;
        if(cvQuestionDetailPrev != null && !cvQuestionDetailPrev.isEmpty() ){
            QuestionnaireQuestionsBean questionnaireQuestionsBean = (QuestionnaireQuestionsBean)cvQuestionDetailPrev.get(0);
            if(canAddAnswerInLatestver && questionsLatest.getAnswerMaxLength() < questionnaireQuestionsBean.getAnswerMaxLength()){
                canAddAnswerInLatestver = false;
            }else if(!questionsLatest.getDescription().equals(questionnaireQuestionsBean.getDescription())){
                canAddAnswerInLatestver = false;
            }
        }
        return canAddAnswerInLatestver;
    }
    
    /**
     * Method to copy question answer details qustionnaire questions bean
     * @param questionAnswerBean 
     * @return questionnaireQuestionsDetails
     */
    private QuestionnaireQuestionsBean copyQuesAnsDetailsToQnrQues(QuestionAnswerBean questionAnswerBean){
        QuestionnaireQuestionsBean questionnaireQuestionsDetails = new QuestionnaireQuestionsBean();
        questionnaireQuestionsDetails.setQuestionNumber(new Integer(questionAnswerBean.getQuestionNumber()));
        questionnaireQuestionsDetails.setAnswer(questionAnswerBean.getAnswer());
        questionnaireQuestionsDetails.setAnswerNumber(questionAnswerBean.getAnswerNumber());
        questionnaireQuestionsDetails.setQuestionId(questionAnswerBean.getQuestionId());
        questionnaireQuestionsDetails.setQuestionNumber(new Integer(questionAnswerBean.getQuestionNumber()));
        return questionnaireQuestionsDetails;
    }
    // Added for COEUSQA-3475 : Questionnaire Versioning vs. Answer Retention - End
    
    // Added for COEUSQA-3723 : Questionnaire with question rules does not save and complete process  -Start
    /**
     * Method to update the store answer which is in the database to the collection present in the query engine
     * @param cvQuestionnaireAnswerDetails 
     * @param cvQuestionAnswers 
     * @param questionnaireModuleObject 
     */
    private void updateStoredAnswerToNewAnswerBean(CoeusVector cvQuestionnaireAnswerDetails, CoeusVector cvQuestionAnswers,
            QuestionnaireAnswerHeaderBean questionnaireModuleObject){
        if(cvQuestionAnswers != null && !cvQuestionAnswers.isEmpty()){
            Equals eqQnrCompletionId = null;
            Equals eqQuestionNumber = null;
            Equals eqAnswerNumber = null;
            And andQnrQues = null;
            CoeusVector cvFilteredAnswer = null;
            QuestionAnswerBean questionAnswerBean = null;
            for(Object questionAnswers : cvQuestionAnswers){
                QuestionnaireQuestionsBean qnrQuestionBean = (QuestionnaireQuestionsBean)questionAnswers;
                int questionNumber = qnrQuestionBean.getQuestionNumber().intValue();
                int answerNumber = qnrQuestionBean.getAnswerNumber();
                if(answerNumber > 0){
                    eqQnrCompletionId = new Equals("questionnaireCompletionId",questionnaireModuleObject.getQuestionnaireCompletionId());
                    eqQuestionNumber = new Equals("questionNumber",questionNumber);
                    andQnrQues = new And(eqQnrCompletionId,eqQuestionNumber);
                    eqAnswerNumber = new Equals("answerNumber",answerNumber);
                    cvFilteredAnswer = cvQuestionnaireAnswerDetails.filter(new And(andQnrQues,eqAnswerNumber));
                    if(cvFilteredAnswer == null || cvFilteredAnswer.isEmpty()){
                        qnrQuestionBean.setAnswer("");
                    }else{
                        questionAnswerBean = (QuestionAnswerBean)cvFilteredAnswer.get(0);
                        qnrQuestionBean.setAnswer(questionAnswerBean.getAnswer());
                    }
                }
            }
        }
    }
    
    /**
     * 
     * @param cvQuestionAnswers 
     * @param lhmQuestionnaireTree 
     * @param questionnaireAnswerHeaderBean 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @throws edu.mit.coeus.exception.CoeusException 
     */
    private void delQnrConditionalAnsForFailedCondition(CoeusVector cvQuestionAnswers,
            LinkedHashMap lhmQuestionnaireTree, QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean) throws DBException, CoeusException{
        if(cvQuestionAnswers != null && !cvQuestionAnswers.isEmpty()){
            QuestionnaireQuestionsBuilder questionnaireQuestionsBuilder = new QuestionnaireQuestionsBuilder();
            for(Object questionAnswer :cvQuestionAnswers){
                QuestionnaireQuestionsBean questionnaireQuestionsBean = (QuestionnaireQuestionsBean)questionAnswer;
                if(questionnaireQuestionsBean.getAnswerNumber() == 0){
                    Integer questionNumber = questionnaireQuestionsBean.getQuestionNumber();
                    Iterator itQuestionnaireTree = lhmQuestionnaireTree.keySet().iterator();
                    QuestionnaireUpdateTxnBean questionnaireUpdateTxnBean = new QuestionnaireUpdateTxnBean();
                    while(itQuestionnaireTree.hasNext()){
                        String questionNumberFromTree = (String)itQuestionnaireTree.next();
                        CoeusVector cvQuestionsAnswers = (CoeusVector)lhmQuestionnaireTree.get(questionNumberFromTree);
                        if(cvQuestionsAnswers != null && !cvQuestionsAnswers.isEmpty()){
                            Equals eqQuestionNumber = new Equals("parentQuestionNumber",questionNumber);
                            And andQnsAns = new And(eqQuestionNumber, new NotEquals("answerNumber",0));
                            CoeusVector cvFilteredQuestions = (CoeusVector)cvQuestionsAnswers.filter(andQnsAns);
                            if(cvFilteredQuestions != null && !cvFilteredQuestions.isEmpty()){
                                HashSet hmQuestions = new HashSet();
                                for(Object questionDetails : cvFilteredQuestions){
                                    QuestionnaireQuestionsBean qnrQuestionDetails = (QuestionnaireQuestionsBean)questionDetails;
                                    hmQuestions.add(qnrQuestionDetails.getQuestionNumber());
                                }
                                // Will checks the child condition questions with the parents answer for validation
                                if(!hmQuestions.isEmpty()){
                                    Iterator itAnswers = hmQuestions.iterator();
                                    while(itAnswers.hasNext()){
                                        Integer questionNumberForAnswer = (Integer)itAnswers.next();
                                        Equals eqQnsNumberForAns = new Equals("questionNumber",questionNumberForAnswer);
                                        CoeusVector cvFiltAnswers = cvFilteredQuestions.filter(eqQnsNumberForAns);
                                        boolean isConditionPassed = false;
                                        boolean isConditionQuestion = false;
                                        for(Object questionDetails : cvFiltAnswers){
                                            QuestionnaireQuestionsBean qnrQuestionDetails = (QuestionnaireQuestionsBean)questionDetails;
                                            if(qnrQuestionDetails.isConditionalFlag() &&
                                                    (!"".equals(qnrQuestionDetails.getConditionValue())
                                                    || qnrQuestionDetails.getConditionRuleId() > 0)){
                                                isConditionQuestion = true;
                                                for(Object answersDetails : cvQuestionAnswers){
                                                    QuestionnaireQuestionsBean qnrAnswerDetails = (QuestionnaireQuestionsBean)answersDetails;
                                                    String answer = qnrAnswerDetails.getAnswer();
                                                    if((qnrAnswerDetails.getQuestionNumber().intValue() == questionNumber.intValue()) &&
                                                            answer != null && !"".equals(qnrAnswerDetails.getAnswer())){
                                                        isConditionPassed = questionnaireQuestionsBuilder.isConditionPassed(answer,qnrQuestionDetails,questionnaireAnswerHeaderBean);
                                                        if(isConditionPassed){
                                                            break;
                                                        }
                                                    }
                                                }

                                                
                                            }
                                            
                                            
                                        }
                                        
                                        if(isConditionQuestion && !isConditionPassed){
                                            questionnaireUpdateTxnBean.deleteQuesAnsBranchForCompletionId(
                                                    Integer.parseInt(questionnaireAnswerHeaderBean.getQuestionnaireCompletionId())
                                                    ,questionNumberForAnswer.intValue());
                                        }
                                    }
                                }
                                
                            }
                        }
                    }
                }
            }
        }
        
    }
    // Added for COEUSQA-3723 : Questionnaire with question rules does not save and complete process  - End
}

