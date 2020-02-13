/*
 * QuestionnaireStream.java
 *
 * Created on September 14, 2007, 10:52 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.xml.generator;

//import edu.mit.coeus.award.bean.AwardBean;
//import edu.mit.coeus.award.bean.AwardTxnBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.irb.bean.ProtocolInfoBean;
import edu.mit.coeus.irb.bean.ProtocolInvestigatorsBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean;
import edu.mit.coeus.questionnaire.bean.ModuleDataBean;
import edu.mit.coeus.questionnaire.bean.QuestionAnswerBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireBaseBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean;
import edu.mit.coeus.questionnaire.bean.SubModuleDataBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.xml.bean.questionnaire.AnswerHeaderType;
import edu.mit.coeus.utils.xml.bean.questionnaire.AnswerInfoType;
import edu.mit.coeus.utils.xml.bean.questionnaire.ModuleInfoType;
import edu.mit.coeus.utils.xml.bean.questionnaire.ModuleUsageType;
import edu.mit.coeus.utils.xml.bean.questionnaire.Person;
import edu.mit.coeus.utils.xml.bean.questionnaire.ProposalInfoType;
import edu.mit.coeus.utils.xml.bean.questionnaire.ProtocolInfoType;
import edu.mit.coeus.utils.xml.bean.questionnaire.QuestionInfoType;
import edu.mit.coeus.utils.xml.bean.questionnaire.Questionnaire;
//import edu.mit.coeus.utils.xml.bean.questionnaire.QuestionnaireType;
import edu.mit.coeus.utils.xml.bean.questionnaire.QuestionsType;
import edu.mit.coeus.utils.xml.bean.questionnaire.UserOptions;
import edu.mit.coeus.utils.xml.bean.questionnaire.UserOptionsInfoType;
import edu.mit.coeus.utils.xml.bean.questionnaire.impl.AnswerHeaderTypeImpl;
import edu.mit.coeus.utils.xml.bean.questionnaire.impl.AnswerInfoTypeImpl;
import edu.mit.coeus.utils.xml.bean.questionnaire.impl.ModuleInfoTypeImpl;
import edu.mit.coeus.utils.xml.bean.questionnaire.impl.ModuleUsageTypeImpl;
import edu.mit.coeus.utils.xml.bean.questionnaire.impl.PersonImpl;
import edu.mit.coeus.utils.xml.bean.questionnaire.impl.ProposalInfoTypeImpl;
import edu.mit.coeus.utils.xml.bean.questionnaire.impl.ProtocolInfoTypeImpl;
import edu.mit.coeus.utils.xml.bean.questionnaire.impl.QuestionInfoTypeImpl;
import edu.mit.coeus.utils.xml.bean.questionnaire.impl.QuestionnaireImpl;
//import edu.mit.coeus.utils.xml.bean.questionnaire.impl.QuestionnaireTypeImpl;
import edu.mit.coeus.utils.xml.bean.questionnaire.impl.QuestionsTypeImpl;
import edu.mit.coeus.utils.xml.bean.questionnaire.impl.UserOptionsImpl;
import edu.mit.coeus.utils.xml.bean.questionnaire.impl.UserOptionsInfoTypeImpl;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author talarianand
 */
public class QuestionnaireStream extends ReportBaseStream {

    private static final String packageName = "edu.mit.coeus.utils.xml.bean.questionnaire";
    private static final String EMPTY_STRING = "";
    private static final int PROPOSAL_MODULE_CODE = 3;
    private static final int PROPOSAL_PERSON_QUESTIONNAIRE_SUB_MODULE_CODE = 6;
//    private static final int AWARD = 1;
//    private static final int DEV_PROPOSAL = 3;
//    private static final int INST_PROPOSAL = 2;
//    private static final int IRB = 7;

    /** Creates a new instance of QuestionnaireStream */
    public QuestionnaireStream() {
    }

    /**
     * Is used to get the protocol object in the form of org.w3c.dom.Document
     * @param Hashtable
     * @return Document
     * @throws DBException, CoeusException
     */
    public org.w3c.dom.Document getStream(Hashtable params) throws DBException, CoeusException {
        Questionnaire questionnaireType = new QuestionnaireImpl();
        CoeusXMLGenrator xmlGenerator = new CoeusXMLGenrator();
        questionnaireType = getQuestionnaireData(questionnaireType, params);
        return xmlGenerator.marshelObject(questionnaireType, packageName);
    }

    /**
     * Is used to get the QuestionnaireType object after populating all the
     * required data.
     * @param params Hashtable consists of details like Questionnaireid, modulekey
     * @return Object
     * @throws DBException
     * @throws CoeusException
     */
    public Object getObjectStream(Hashtable params) throws DBException, CoeusException{
        Questionnaire questionnaireType = new QuestionnaireImpl();
        questionnaireType = getQuestionnaireData(questionnaireType, params);
        return questionnaireType;
    }

    /**
     * Is used to populate the questionnaire data to Questionnaire object.
     * @param params Hashtable consists of details like Questionnaireid, modulekey
     * @return Questionnaire object
     * @param questionnaireType Questionnaire object
     * @throws DBException
     * @throws CoeusException
     */
    private Questionnaire getQuestionnaireData(Questionnaire questionnaireType, Hashtable params) throws DBException, CoeusException {
        QuestionnaireTxnBean txnBean = new QuestionnaireTxnBean();
        QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean = (QuestionnaireAnswerHeaderBean)params.get(QuestionnaireAnswerHeaderBean.class);
        int questionnaireId = questionnaireAnswerHeaderBean.getQuestionnaireId();
        CoeusVector cvData = txnBean.getQuestionnaireData(questionnaireId);
        QuestionnaireBaseBean dataBean = null;
        if(cvData != null && cvData.size() > 0) {
            dataBean = (QuestionnaireBaseBean)cvData.get(0);
        }
        if(dataBean != null) {
            questionnaireType.setQuestionnaireId(dataBean.getQuestionnaireId());
            questionnaireType.setQuestionnaireName(dataBean.getName());
            questionnaireType.setQuestionnaireDesc(dataBean.getDescription());

            questionnaireType.getQuestions().addAll(getQuestionInfoData(questionnaireId, params));
            questionnaireType.setUserOption(getUserOptions(params));
            questionnaireType.setModuleUsage(getModuleUsage(params));
            questionnaireType.getAnswerHeader().addAll(getAnswerInfo(params));
            if(questionnaireAnswerHeaderBean.getModuleItemCode() != 0) {
                questionnaireType.setModuleUsage(getModuleUsage(params));
            }
            String moduleItemKey = questionnaireAnswerHeaderBean.getModuleItemKey();
            int subModuleItemKey = 0;
            if(questionnaireAnswerHeaderBean.getModuleSubItemKey() != null &&
                    questionnaireAnswerHeaderBean.getModuleSubItemKey() != "") {
                subModuleItemKey = Integer.parseInt(questionnaireAnswerHeaderBean.getModuleSubItemKey());
            }
            int moduleCode = questionnaireAnswerHeaderBean.getModuleItemCode();
            switch(moduleCode) {
                case ModuleConstants.PROPOSAL_DEV_MODULE_CODE:
                    questionnaireType.getProposalInfo().addAll(getDevProposalInfo(moduleItemKey,questionnaireAnswerHeaderBean));
                    break;
                case ModuleConstants.PROTOCOL_MODULE_CODE:
                    questionnaireType.getProtocolInfo().addAll(getProtocolInfo(moduleItemKey, subModuleItemKey));
                    break;
                case ModuleConstants.IACUC_MODULE_CODE:
                    questionnaireType.getProtocolInfo().addAll(getIacucProtocolInfo(moduleItemKey, subModuleItemKey));
                default:
                    break;
            }
        }
        return questionnaireType;
    }

    /**
     * Is used to get the module information.
     * @param htData Hashtable consists of details like Questionnaireid, modulekey
     * @return ModuleUsageType object
     * @throws DBException
     * @throws CoeusException
     */
    private ModuleUsageType getModuleUsage(Hashtable htData) throws DBException, CoeusException {
        ModuleUsageType moduleUsage = new ModuleUsageTypeImpl();
        ModuleInfoType moduleInfo = new ModuleInfoTypeImpl();
        QuestionnaireTxnBean txnBean = new QuestionnaireTxnBean();
        //Code commented and modified for Case#3875 - Need to add Annual COI as a Module for Questionnaire maintenance
        //CoeusVector cvModuleData = txnBean.getModuleData();
        CoeusVector cvModuleData = txnBean.getModuleData(true);
        CoeusVector cvSubModuleData = txnBean.getSubModuleData();
        String moduleCode = EMPTY_STRING;
        String subModuleCode = EMPTY_STRING;
        QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean = (QuestionnaireAnswerHeaderBean)htData.get(QuestionnaireAnswerHeaderBean.class);
        if(htData != null && htData.size() > 0) {
            moduleCode = new Integer(questionnaireAnswerHeaderBean.getModuleItemCode()).toString();
            subModuleCode = new Integer(questionnaireAnswerHeaderBean.getModuleSubItemCode()).toString();
            String module = EMPTY_STRING;
            String subModule = EMPTY_STRING;
            Equals eqModule = new Equals("code", moduleCode);
            cvModuleData = cvModuleData.filter(eqModule);

            if(cvModuleData != null && cvModuleData.size() > 0) {
                ModuleDataBean dataBean = (ModuleDataBean) cvModuleData.get(0);
                module = dataBean.getDescription();
            }

            eqModule = new Equals("moduleCode", moduleCode);
            Equals eqSubModule = new Equals("code", subModuleCode);
            And moduleAndSubModule = new And(eqModule, eqSubModule);

            if(cvSubModuleData != null && cvSubModuleData.size() > 0) {
                cvSubModuleData = cvSubModuleData.filter(moduleAndSubModule);
                if(cvSubModuleData != null && cvSubModuleData.size() > 0) {
                    SubModuleDataBean dataBean = (SubModuleDataBean) cvSubModuleData.get(0);
                    subModule = dataBean.getDescription();
                }
            }
            moduleInfo.setModuleCode(Integer.parseInt(moduleCode));
            moduleInfo.setSubModuleCode(Integer.parseInt(subModuleCode));
            moduleInfo.setModuleDesc(module);
            moduleInfo.setSubModuleDesc(subModule);
            moduleUsage.setModuleInfo(moduleInfo);
        }
        return moduleUsage;
    }

    /**
     * Is used to get the question and corresponding answers information data
     * @param questionnaireId int Questionnaire id
     * @param htData consists of details like Questionnaireid, modulekey
     * @return Vector
     * @throws DBException
     * @throws CoeusException
     */
    private Vector getQuestionInfoData(int questionnaireId, Hashtable htData) throws DBException, CoeusException {
        QuestionsType questionsType = new QuestionsTypeImpl();
        QuestionInfoType questionInfo = new QuestionInfoTypeImpl();
        QuestionnaireTxnBean txnBean = new QuestionnaireTxnBean();
        //QuestionnaireBean dataBean = null;
        AnswerInfoType answerInfo = null;
        CoeusVector cvAnswerData = new CoeusVector();
        QuestionnaireAnswerHeaderBean answerBean = new QuestionnaireAnswerHeaderBean();
        QuestionAnswerBean quesAnswerBean = null;
        //Added for the case# coeusdev-135-Problem printing a questionnaire-start
        int qnrVersion=0;
        QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean=new QuestionnaireAnswerHeaderBean();
        String flagAdminQuestionnaire = "";
        //Added for the case# coeusdev-135-Problem printing a questionnaire-end
        // 4272: Maintain History of Questionnaires
//        CoeusVector cvQuesionData = txnBean.getQuestionnaireQuestionsData(questionnaireId);
        //Getting the Answer details for the Specified Questionnaire

        if(htData != null && htData.size() > 0) {
            //Added for the case# coeusdev-135-Problem printing a questionnaire-start
            if(htData.get("ADMIN_QUESTIONNAIRE") != null && htData.get("ADMIN_QUESTIONNAIRE").toString() .equalsIgnoreCase("Y")){
            flagAdminQuestionnaire = htData.get("ADMIN_QUESTIONNAIRE").toString();
            }
            //Added for the case# coeusdev-135-Problem printing a questionnaire-end
            questionnaireAnswerHeaderBean =
                    (QuestionnaireAnswerHeaderBean)htData.get(QuestionnaireAnswerHeaderBean.class);
            answerBean.setModuleItemCode(questionnaireAnswerHeaderBean.getModuleItemCode());
            // Modified with CoeusQA2313: Completion of Questionnaire for Submission
//            answerBean.setModuleItemKey(questionnaireAnswerHeaderBean.getModuleItemKey());
//            answerBean.setModuleSubItemCode(questionnaireAnswerHeaderBean.getModuleSubItemCode());
//            answerBean.setModuleSubItemKey(questionnaireAnswerHeaderBean.getModuleSubItemKey());
            answerBean.setApplicableSubmoduleCode(questionnaireAnswerHeaderBean.getApplicableSubmoduleCode());
            answerBean.setQuestionnaireCompletionFlag(questionnaireAnswerHeaderBean.getQuestionnaireCompletionFlag());
            if("Y".equals(questionnaireAnswerHeaderBean.getQuestionnaireCompletionFlag())){
                answerBean.setApplicableModuleItemKey(questionnaireAnswerHeaderBean.getApplicableModuleItemKey());
                answerBean.setApplicableModuleSubItemKey(questionnaireAnswerHeaderBean.getApplicableModuleSubItemKey());
     // checking for whether it is comming from ppc print function
                if(questionnaireAnswerHeaderBean.getModuleItemCode()==3 && questionnaireAnswerHeaderBean.getModuleSubItemCode()==6)
                {
                   answerBean.setCurrentUser(questionnaireAnswerHeaderBean.getCurrentUser());

                   answerBean.setApplicableModuleSubItemKeyForPpc(questionnaireAnswerHeaderBean.getCurrentPersonId());
                   answerBean.setCurrentPersonId(questionnaireAnswerHeaderBean.getCurrentPersonId());
                   answerBean.setModuleSubItemCode(questionnaireAnswerHeaderBean.getModuleSubItemCode());
                }
            }else{
                answerBean.setApplicableModuleItemKey(questionnaireAnswerHeaderBean.getModuleItemKey());
                int moduleSubItemKey = questionnaireAnswerHeaderBean.getModuleSubItemKey() == null? 0:
                    Integer.parseInt(questionnaireAnswerHeaderBean.getModuleSubItemKey());
                answerBean.setApplicableModuleSubItemKey(moduleSubItemKey);
            }
            // CoeusQA2313: Completion of Questionnaire for Submission - End
            // 4272: Maintain History of Questionnaires
            answerBean.setQuestionnaireId(questionnaireId);
            cvAnswerData = txnBean.getQuestionnaireAnswersDetails(answerBean);
        }
        //Added for the case# coeusdev-135-Problem printing a questionnaire-start
        //  if(answerBean.getModuleItemCode()==0 && answerBean.getModuleSubItemCode()==0 ){

        qnrVersion = questionnaireAnswerHeaderBean.getQuestionnaireVersionNumber();
        String questionnaireCompletionFlag = questionnaireAnswerHeaderBean.getQuestionnaireCompletionFlag();
        // }else{
        // 4272: Maintain History of Questionnaires - Start
        //  qnrVersion = txnBean.fetchAnsweredVersionNumberOfQuestionnaire(answerBean);
        //  }

        //Added for the case# coeusdev-135-Problem printing a questionnaire-end
        CoeusVector cvQuesionData = txnBean.getQuestionnaireQuestionsData(questionnaireId, qnrVersion);
        // 4272: Maintain History of Questionnaires - End
         // 4272: Maintain History of Questionnaires - End
        //Added for the case# coeusdev-135-Problem printing a questionnaire-start
        //for (QuestionnaireBean questionaireBean : getSortedVector(cvQuesionData)) {

        Vector vecQuestionInfo = new Vector();
        if(cvQuesionData != null && cvQuesionData.size() > 0) {
            Vector cvSorted= new Vector();
            // The getSortedVector method is called to sort according to the hierarchy level
            if(flagAdminQuestionnaire != null && flagAdminQuestionnaire.equalsIgnoreCase("Y")){
                vecQuestionInfo = getAdminQuestionnaire(cvQuesionData);
            }else{
                cvSorted = getSortedVector(cvQuesionData);
                if(cvSorted!=null && cvSorted.size()>0){
                    for(int index = 0; index < cvSorted.size(); index++) {
//           for (QuestionnaireBean dataBean : getSortedVector(cvQuesionData)) {
                        QuestionnaireBean dataBean = (QuestionnaireBean) cvSorted.get(index);
                        questionInfo = new QuestionInfoTypeImpl();
                        int questionId = dataBean.getQuestionId().intValue();
                        int questionNumber = dataBean.getQuestionNumber().intValue();
                        //Setting the question information
                        questionInfo.setQuestionId(questionId);
                        questionInfo.setQuestionNumber(questionNumber);
                        questionInfo.setQuestion(dataBean.getDescription());
                        //Added for the case# coeusdev-135-Problem printing a questionnaire-start
                        boolean isAnswerPresent = false;
                        //Added for the case# coeusdev-135-Problem printing a questionnaire-end
                        if(cvAnswerData != null && cvAnswerData.size() > 0) {
                            for(int i = 0; i < cvAnswerData.size(); i++) {
                                //Setting the answer information
                                quesAnswerBean = (QuestionAnswerBean)cvAnswerData.get(i);
                                if(questionnaireId == quesAnswerBean.getQuestionnaireId() &&
                                        questionId == quesAnswerBean.getQuestionId().intValue()
                                        && questionNumber == quesAnswerBean.getQuestionNumber()
                                        // 4272: Maintain History of Questionnaires
                                        && qnrVersion == quesAnswerBean.getQuestionnaireVersionNumber()) {
                                    answerInfo = new AnswerInfoTypeImpl();
                                    answerInfo.setAnswerNumber(quesAnswerBean.getAnswerNumber());
                                    if(quesAnswerBean.getAnswer() != null) {
                                        //Added for the case# coeusdev-135-Problem printing a questionnaire-start
                                        isAnswerPresent = true;
                                         //Added for the case# coeusdev-135-Problem printing a questionnaire-end
                                        //Setting the description in case of search fields
                                        //For exapmle if person search exists in questionnaire, then
                                        //instead of person id, person name will be printed
                                        String name = quesAnswerBean.getAnswer().trim();
                                        String description = quesAnswerBean.getDescription();
                                        if(description != null && description != "") {
                                            // Modified for COEUSQA-2513:Questionnaire should print the description not the code value for arg value lookup responses
//                                            answerInfo.setAnswer(name + " - "+ description);
                                            answerInfo.setAnswer(description);
                                            // COEUSQA-2513: End
                                        } else if(name != null){
                                            //Setting the Yes/No/None in case of radio buttons
                                            if(name.trim().equalsIgnoreCase("Y")) {
                                                answerInfo.setAnswer("Yes");
                                            } else if(name.trim().equalsIgnoreCase("N")) {
                                                answerInfo.setAnswer("No");
                                            } else if(name.trim().equalsIgnoreCase("X")) {
                                                answerInfo.setAnswer("None");
                                            } else {
                                                answerInfo.setAnswer(name);
                                            }
                                        }

                                    }

                                    questionInfo.getAnswerInfo().add(answerInfo);

                                }

                            }

                        }
                        //Added for the case# coeusdev-135-Problem printing a questionnaire-start
                        if(isAnswerPresent || "N".equalsIgnoreCase(questionnaireCompletionFlag) ||  questionnaireCompletionFlag== null){
                            questionsType.getQuestionInfo().add(questionInfo);
                        }
                       //Added for the case# coeusdev-135-Problem printing a questionnaire-end
                    }
                    vecQuestionInfo.add(questionsType);
                }
            }
        }
        return vecQuestionInfo;
    }
    //Added for the case# coeusdev-135-Problem printing a questionnaire-start
    private Vector getAdminQuestionnaire(CoeusVector cvQuesionData){
        Vector vecQuestionInfo = new Vector();
        QuestionsType questionsType = new QuestionsTypeImpl();
        QuestionInfoType questionInfo = new QuestionInfoTypeImpl();
        Vector cvSorted = getSortedVector(cvQuesionData);
        if(cvSorted != null  && cvSorted.size()>0){
            for(int index = 0; index < cvSorted.size(); index++) {
//           for (QuestionnaireBean dataBean : getSortedVector(cvQuesionData)) {
                QuestionnaireBean dataBean = (QuestionnaireBean) cvSorted.get(index);
                questionInfo = new QuestionInfoTypeImpl();
                int questionId = dataBean.getQuestionId().intValue();
                int questionNumber = dataBean.getQuestionNumber().intValue();
                //Setting the question information
                questionInfo.setQuestionId(questionId);
                questionInfo.setQuestionNumber(questionNumber);
                questionInfo.setQuestion(dataBean.getDescription());
                questionsType.getQuestionInfo().add(questionInfo);
            }
           vecQuestionInfo.add(questionsType);
        }

        return vecQuestionInfo;
    }

     /**
     * Is used to sort
     * @param vector consists of details like QuestionnaireId,ParentQuestionNumber..
     */
       public static Vector getSortedVector(
               Vector vector) {
           return sort(vector);
       }

     /**
     * Is used to sort
     * @param vector consists of details like QuestionnaireId,ParentQuestionNumber..
     */
       private static Vector sort(Vector vector) {
           Vector temp = vector;
           Vector elements = new Vector();
           QuestionnaireBean questionaireBean = null;

           for (int i = 0; i < temp.size(); i++) {
               questionaireBean = (QuestionnaireBean)vector.get(i);
               if ( !elements.contains(questionaireBean) ) {
                   //calls findChildrenAndUpdate method recursively in order to find children
                   findChildrenAndUpdate(elements, temp, questionaireBean);
                   i--;
               }
           }
           return elements;
       }
     /**
     * Is used to find Children
     * @param vector ,elements, parent
     * once parent is added in elemnts, it is removed from vector,so that it wont check for same QuestionId
     *
     */
       private static void findChildrenAndUpdate(
               Vector elements, Vector vector,
               QuestionnaireBean parent) {
           elements.add(parent);
           vector.remove(parent);
           // FInd the children for parent
           List children = findChildren(vector, parent
                   .getQuestionNumber());
           if(children != null && children.size() > 0){
               for(int index=0;index<children.size();index++){
                   QuestionnaireBean childBean = (QuestionnaireBean)children.get(index);
                   findChildrenAndUpdate(elements, vector, childBean);
               }
           }
       }
     /**
     * Is used to find sequence number for the childrens to display in order
     * @param vector , parent
     * once parent is added in elemnts, it is removed from vector,so that it wont check for same QuestionId
     *
     */
       private static List findChildren(
               Vector vector, Integer parent) {
           List list = new ArrayList();
           if(vector != null && vector.size() > 0){
               for(int index=0;index<vector.size();index++){
                   QuestionnaireBean bean = (QuestionnaireBean)vector.get(index);
                   if(bean.getParentQuestionNumber() != null && parent != null){
                       if (bean.getParentQuestionNumber().intValue() == parent.intValue()) {
                           list.add(bean);
                       }
                   }
               }
           }

           QuestionnaireBean firstChild = null;
           QuestionnaireBean secondChild = null;
           QuestionnaireBean temp = null;
           // To the the questions according to the questionSequenceNumber
           for (int i = 0; i < list.size(); i++) {
               for (int j = 1; j <= (list.size() - 1); j++) {
                   firstChild = (QuestionnaireBean)list.get(j - 1);
                   secondChild = (QuestionnaireBean)list.get(j);
                   if (secondChild.getQuestionSequenceNumber() < firstChild
                           .getQuestionSequenceNumber() ) {
                       temp = firstChild;
                       list.set((j - 1), secondChild);
                       list.set(j, temp);
                   }
               }
           }
           return list;
       }
       //Added for the case# coeusdev-135-Problem printing a questionnaire-end

    /**
     * Is used to set the module key, submodule key information
     * @param htData consists of details like Questionnaireid, modulekey
     * @return Vector
     * @throws DBException
     * @throws CoeusException
     */
    private Vector getAnswerInfo(Hashtable htData) throws DBException, CoeusException {
        AnswerHeaderType answerHeader = new AnswerHeaderTypeImpl();
        Vector vecData = new Vector();
        QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean = (QuestionnaireAnswerHeaderBean)htData.get(QuestionnaireAnswerHeaderBean.class);
        AnswerInfoType answerInfo = new AnswerInfoTypeImpl();
        answerHeader.setModuleKey(questionnaireAnswerHeaderBean.getModuleItemKey());
        answerHeader.setSubModuleKey(questionnaireAnswerHeaderBean.getModuleSubItemKey());
        answerHeader.getAnswerInfo().add(answerInfo);
        vecData.add(answerHeader);
        return vecData;
    }

    /**
     * Is used to the user options
     * @param htData consists of details like Questionnaireid, modulekey
     * @return UserOptions object
     */
    private UserOptions getUserOptions(Hashtable htData) {
        UserOptions userOptions = new UserOptionsImpl();
        UserOptionsInfoType userOptionsInfo = new UserOptionsInfoTypeImpl();
        QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean = (QuestionnaireAnswerHeaderBean)htData.get(QuestionnaireAnswerHeaderBean.class);
        if(!questionnaireAnswerHeaderBean.getPrintAnswers()) {
            userOptionsInfo.setPrintAnswers("No");
        } else if(questionnaireAnswerHeaderBean.getPrintAll()) {
            userOptionsInfo.setPrintAnsweredQuestionsOnly("No");
        } else if(questionnaireAnswerHeaderBean.getPrintOnlyAnswered()) {
            userOptionsInfo.setPrintAnsweredQuestionsOnly("Yes");
        }
        userOptions.setUserOptionsInfo(userOptionsInfo);
        return userOptions;
    }

    /**
     * Is used to get the header information for Development proposal
     * @param proposalNumber String Proposal Number
     * @return Vector
     * @throws DBException
     * @throws CoeusException
     */
    private Vector getDevProposalInfo(String proposalNumber,QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean) throws DBException, CoeusException {
        Vector vecInvestigator = new Vector();
        ProposalDevelopmentTxnBean proposalTxnBean = new ProposalDevelopmentTxnBean();
        ProposalDevelopmentFormBean proposalBean = proposalTxnBean.getProposalDevelopmentDetails(proposalNumber);
        ProposalInfoType proposalInfo = new ProposalInfoTypeImpl();
        Person personInfo = null;
        if(proposalBean != null) {
            personInfo = new PersonImpl();
            Vector vecData = proposalBean.getInvestigators();
            if(vecData != null && vecData.size() > 0) {
                for(int index = 0; index < vecData.size(); index++) {
                    ProposalInvestigatorFormBean ivestigatorBean = (ProposalInvestigatorFormBean) vecData.get(index);
                    if(ivestigatorBean.isPrincipleInvestigatorFlag()) {
                        personInfo.setFullname(ivestigatorBean.getPersonName());
                    }
                    /* Add Name of Person Certifying to Printout - START*/
                    if (questionnaireAnswerHeaderBean.getModuleItemCode() == PROPOSAL_MODULE_CODE && questionnaireAnswerHeaderBean.getModuleSubItemCode() == PROPOSAL_PERSON_QUESTIONNAIRE_SUB_MODULE_CODE) {
                        QuestionnaireTxnBean txnBean = new QuestionnaireTxnBean();                        
                        String certUserId = questionnaireAnswerHeaderBean.getCurrentPersonId();                        
                        String certUserName = "";
                        certUserName = txnBean.getPersonFullName(certUserId);
                        personInfo.setDirectoryTitle(certUserName);
                    }
                    /* Add Name of Person Certifying to Printout - END*/
                }
            }
            proposalInfo.setTitle(proposalBean.getTitle());
            proposalInfo.setInvestigator(personInfo);
        }
        vecInvestigator.add(proposalInfo);
        return vecInvestigator;
    }

    /**
     * Is used to get the Header information for Protocol.
     * @param protocolNumber String Protocol Number
     * @param sequenceNumber int Sequence Number
     * @return Vector
     * @throws DBException
     * @throws CoeusException
     */
    private Vector getProtocolInfo(String protocolNumber, int sequenceNumber) throws DBException, CoeusException {
        Vector vecInvestigator = new Vector();
        ProtocolDataTxnBean protocolTxnBean = new ProtocolDataTxnBean();
        // ProtocolInfoBean protocolInfoBean = protocolTxnBean.getProtocolInfo(protocolNumber, sequenceNumber);
        ProtocolInfoBean protocolInfoBean = protocolTxnBean.getProtocolMaintenanceDetails(protocolNumber, sequenceNumber);
        ProtocolInfoType protocolInfo = new ProtocolInfoTypeImpl();
        Person personInfo = null;
        if(protocolInfoBean != null) {
            personInfo = new PersonImpl();
//            Vector vecData = protocolInfoBean.getInvestigators();
            Vector vecData = protocolTxnBean.getProtocolInvestigators(protocolNumber, sequenceNumber);
            if(vecData != null && vecData.size() > 0) {
                for(int index = 0; index < vecData.size(); index++) {
                    ProtocolInvestigatorsBean ivestigatorBean = (ProtocolInvestigatorsBean) vecData.get(index);
                    if(ivestigatorBean.isPrincipalInvestigatorFlag()) {
                        personInfo.setFullname(ivestigatorBean.getPersonName());
                    }
                }
            }

            protocolInfo.setTitle(protocolInfoBean.getTitle());
            protocolInfo.setInvestigator(personInfo);
        }
        vecInvestigator.add(protocolInfo);
        return vecInvestigator;
    }

    // Added for IACUC Questionnaire implementation - Start
    /**
     * Is used to get the Header information for IACUC Protocol.
     * @param protocolNumber String Protocol Number
     * @param sequenceNumber int Sequence Number
     * @return Vector
     * @throws DBException
     * @throws CoeusException
     */
    private Vector getIacucProtocolInfo(String protocolNumber, int sequenceNumber) throws DBException, CoeusException {
        Vector vecInvestigator = new Vector();
        edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean protocolTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean();
        edu.mit.coeus.iacuc.bean.ProtocolInfoBean protocolInfoBean = protocolTxnBean.getProtocolMaintenanceDetails(protocolNumber, sequenceNumber);
        ProtocolInfoType protocolInfo = new ProtocolInfoTypeImpl();
        Person personInfo = null;
        if(protocolInfoBean != null) {
            personInfo = new PersonImpl();
            Vector vecData = protocolTxnBean.getProtocolInvestigators(protocolNumber, sequenceNumber);
            if(vecData != null && vecData.size() > 0) {
                for(int index = 0; index < vecData.size(); index++) {
                    edu.mit.coeus.iacuc.bean.ProtocolInvestigatorsBean ivestigatorBean = (edu.mit.coeus.iacuc.bean.ProtocolInvestigatorsBean) vecData.get(index);
                    if(ivestigatorBean.isPrincipalInvestigatorFlag()) {
                        personInfo.setFullname(ivestigatorBean.getPersonName());
                    }
                }
            }

            protocolInfo.setTitle(protocolInfoBean.getTitle());
            protocolInfo.setInvestigator(personInfo);
        }
        vecInvestigator.add(protocolInfo);
        return vecInvestigator;
    }
    // Added for IACUC Questionnaire implementation - End
}
