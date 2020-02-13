/*
 * @(#)QuestionnaireTxnBean.java September 19, 2006, 5:02 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * QuestionnaireTxnBean.java
 *
 * Created on September 19, 2006, 5:02 PM
 */

package edu.mit.coeus.questionnaire.bean;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.mapsrules.bean.BusinessRuleBean;
import edu.mit.coeus.mapsrules.bean.RulesTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.Utils;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author  vinayks
 */
public class QuestionnaireTxnBean {
    private DBEngineImpl dbEngine;

    private static final String DSN = "Coeus";

    private Timestamp dbTimestamp;

    // holds the userId for the logged in user
    private String userId;

    /** Creates a new instance of QuestionnaireTxnBean */
    public QuestionnaireTxnBean() {
        dbEngine = new DBEngineImpl();
    }

    public QuestionnaireTxnBean(String userId) {
        this();
        this.userId = userId;
    }

    /**
     * This method retrieves the data from OSP$QUESTION table
     * @throws CoeusException
     * @throws DBException
     * @return
     */
    // 4272: Maintain history of Questionnaires
//    public CoeusVector getQuestionsData(int questionId) throws CoeusException,DBException {
    public CoeusVector getQuestionsData(int questionId, int questionVersionNumber) throws CoeusException,DBException {

        Vector param = new Vector();
        Vector result = null;
        CoeusVector questionsData = null;
        QuestionsMaintainanceBean questMaintenanceBean = null ;
        param.add(new Parameter("AV_QUESTION_ID",DBEngineConstants.TYPE_INT,new Integer(questionId)));
        // 4272: Maintain history of Questionnaires - Start
        param.add(new Parameter("AV_VERSION_NUMBER",DBEngineConstants.TYPE_INT,new Integer(questionVersionNumber)));
        if(dbEngine != null) {
//            result = dbEngine.executeRequest(DSN,"call GET_QUESTION_DETAIL_FOR_QN_ID(<< AV_QUESTION_ID >>," +
//                    "<< OUT RESULTSET rset >>)",DSN,param);
            result = dbEngine.executeRequest(DSN,"call GET_QUESTION_DETAIL_FOR_QN_ID(<< AV_QUESTION_ID >>,<<AV_VERSION_NUMBER>>," +
                    "<< OUT RESULTSET rset >>)",DSN,param);
        // 4272: Maintain history of Questionnaires - End
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result != null && result.size() > 0) {
            questionsData = new CoeusVector();
            HashMap rowData = null;
            for(int index = 0; index < result.size(); index++){
                rowData = (HashMap)result.get(index);
                questMaintenanceBean = new QuestionsMaintainanceBean();
                questMaintenanceBean.setQuestionId(rowData.get("QUESTION_ID") == null ? null :
                    new Integer(rowData.get("QUESTION_ID").toString()));
                // 4272: Maintain history of Questionnaires - Start
                questMaintenanceBean.setVersionNumber(rowData.get("VERSION_NUMBER") == null ? 0 :
                    new Integer(rowData.get("VERSION_NUMBER").toString()).intValue());
                questMaintenanceBean.setStatus((String)rowData.get("STATUS"));
                // 4272: Maintain history of Questionnaires - End
                questMaintenanceBean.setDescription((String)rowData.get("QUESTION"));
                questMaintenanceBean.setMaxAnswers(rowData.get("MAX_ANSWERS") == null ? 0 :
                    Integer.parseInt(rowData.get("MAX_ANSWERS").toString()));
                questMaintenanceBean.setValidAnswers((String)rowData.get("VALID_ANSWER"));
                questMaintenanceBean.setLookupName(rowData.get("LOOKUP_GUI")== null ?
                    null : (String)rowData.get("LOOKUP_NAME"));
                questMaintenanceBean.setAnswerDataType(rowData.get("ANSWER_DATA_TYPE")== null ?
                    null : (String)rowData.get("ANSWER_DATA_TYPE"));
                questMaintenanceBean.setAnswerMaxLength(rowData.get("ANSWER_MAX_LENGTH")== null ? 0 :
                    Integer.parseInt(rowData.get("ANSWER_MAX_LENGTH").toString()));
                questMaintenanceBean.setLookupGui(rowData.get("LOOKUP_GUI")== null ?
                    null : (String)rowData.get("LOOKUP_GUI"));
                // COEUSDEV-183: Display Question from Questionnaire does not display Question Group data - Start
                questMaintenanceBean.setGroupTypeCode(rowData.get("GROUP_TYPE_CODE")== null ? 0 :
                    Integer.parseInt(rowData.get("GROUP_TYPE_CODE").toString()));
                // COEUSDEV-183: Display Question from Questionnaire does not display Question Group data - End
                questMaintenanceBean.setUpdateTimestamp(((Timestamp)rowData.get("UPDATE_TIMESTAMP")));
                questMaintenanceBean.setUpdateUser((String)rowData.get("UPDATE_USER"));
                questionsData.addElement(questMaintenanceBean);
            }
        }
        return questionsData;
    }

    /**
     * This method retrieves the data from OSP$QUESTIONNAIRE table
     * @throws CoeusException
     * @throws DBException
     * @return
     */
    public CoeusVector getQuestionnaireData(int questionnaireId) throws CoeusException,DBException {
        Vector param = new Vector();
        Vector result = null;
        CoeusVector questionnaireData = null;
        QuestionnaireBaseBean questionnaireBaseBean = null ;
        boolean hasTemplate = false;//4287
        param.add(new Parameter("AV_QUESTIONNAIRE_ID",DBEngineConstants.TYPE_INT,new Integer(questionnaireId)));
        if(dbEngine != null) {
            result = dbEngine.executeRequest(DSN,"call GET_QUESTIONNAIRE(<< AV_QUESTIONNAIRE_ID >>," +
                    "<< OUT RESULTSET rset >>)",DSN,param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result != null && result.size() > 0) {
            HashMap rowData = null ;
            questionnaireData = new CoeusVector();
            for(int index = 0 ; index < result.size() ; index ++){
                rowData = (HashMap)result.get(index);
                questionnaireBaseBean = new QuestionnaireBaseBean();
                questionnaireBaseBean.setQuestionnaireId(rowData.get("QUESTIONNAIRE_ID") == null ? 0 :
                    Integer.parseInt(rowData.get("QUESTIONNAIRE_ID").toString()));
                // 4272: Maintain history of Questionnaires  - Start
                questionnaireBaseBean.setQuestionnaireVersionNumber(rowData.get("VERSION_NUMBER") == null ? 0 :
                    Integer.parseInt(rowData.get("VERSION_NUMBER").toString()));
                // 4272: Maintain history of Questionnaires - End
                questionnaireBaseBean.setName((String)rowData.get("NAME"));
                questionnaireBaseBean.setDescription((String)rowData.get("DESCRIPTION"));
                questionnaireBaseBean.setUpdateTimestamp((Timestamp)rowData.get("UPDATE_TIMESTAMP"));
                questionnaireBaseBean.setUpdateUser((String)rowData.get("UPDATE_USER"));
                //Added for coeus4.3 Questionnaire enhancement case#2946 - starts
                String flag = (String)rowData.get("IS_FINAL");
                flag = (flag == null)? "" : flag;
                questionnaireBaseBean.setFinalFlag((flag.equals("Y"))?true : false);
                flag = (String)rowData.get("QUESTIONNAIRE_COMPLETED_FLAG");
                flag = (flag == null)? "" : flag;
                questionnaireBaseBean.setCompletedFlag((flag.equals("Y"))?true : false);
                //Added for coeus4.3 Questionnaire enhancement case#2946 - ends
                //Added with case 4287 : Questionnaire Templates - Start
                questionnaireBaseBean.setTemplateName((String)rowData.get("FILE_NAME"));
                hasTemplate = (String)rowData.get("FILE_NAME")==null?false:true;
                questionnaireBaseBean.setHasTemplate(hasTemplate);
                questionnaireBaseBean.setQuestionnaireTemplateBean(hasTemplate?new QuestionnaireTemplateBean():null);
                //4287 : End
                // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
                questionnaireBaseBean.setGroupTypeCode(rowData.get("GROUP_TYPE_CODE")== null ? 0 :
                    (Integer.parseInt(rowData.get("GROUP_TYPE_CODE").toString())));
                questionnaireBaseBean.setUpdateUserName((String)rowData.get("UPDATE_USER_NAME"));

                // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
                questionnaireData.addElement(questionnaireBaseBean );
            }
        }
        return questionnaireData;
    }

    /**
     * This method retrieves the data from OSP$QUESTIONNAIRE_QUESTIONS table
     * @throws CoeusException
     * @throws DBException
     * @return
     */
    // 4272: Maintain history of Questionnaires
//    public CoeusVector getQuestionnaireQuestionsData(int questionnaireId) throws CoeusException,DBException {
    public CoeusVector getQuestionnaireQuestionsData(int questionnaireId, int qnrVersionNumber) throws CoeusException,DBException {
        Vector param = new Vector();
        Vector result = null;
        CoeusVector questionnaireQuestionsData = null;
        QuestionnaireBean questionnaireBean = null ;
        param.add(new Parameter("AV_QUESTIONNAIRE_ID",DBEngineConstants.TYPE_INT,new Integer(questionnaireId)));
        // 4272: Maintain history of Questionnaires - Start
        param.add(new Parameter("AV_QNR_VERSION_NUMBER",DBEngineConstants.TYPE_INT,new Integer(qnrVersionNumber)));
        if(dbEngine != null) {
//            result = dbEngine.executeRequest(DSN,"call GET_QUESTIONNAIRE_QUESTIONS(<< AV_QUESTIONNAIRE_ID >>," +
            result = dbEngine.executeRequest(DSN,"call GET_QUESTIONNAIRE_QUESTIONS(<< AV_QUESTIONNAIRE_ID >>, << AV_QNR_VERSION_NUMBER >>, " +
                    "<< OUT RESULTSET rset >>)",DSN,param);
        // 4272: Maintain history of Questionnaires - End
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result != null && result.size() > 0) {
            HashMap rowData = null ;
            HashMap hmSeqQuestions = new HashMap();
            questionnaireQuestionsData = new CoeusVector();
            RulesTxnBean rulesTxnBean = new RulesTxnBean();
            for(int index = 0 ; index < result.size() ; index++){
                questionnaireBean = new QuestionnaireBean();
                rowData = (HashMap)result.get(index);
                questionnaireBean.setQuestionnaireId(rowData.get("QUESTIONNAIRE_ID")== null ? 0 :
                    (Integer.parseInt(rowData.get("QUESTIONNAIRE_ID").toString())));
                // 4272: Maintain history of Questionnaires - Start
                questionnaireBean.setQuestionnaireVersionNumber(rowData.get("QUESTIONNAIRE_VERSION_NUMBER")== null ? 0 :
                    (Integer.parseInt(rowData.get("QUESTIONNAIRE_VERSION_NUMBER").toString())));
                questionnaireBean.setQuestionVersionNumber(rowData.get("QUESTION_VERSION_NUMBER")== null ? new Integer(0) :
                    (new Integer(rowData.get("QUESTION_VERSION_NUMBER").toString())));
                questionnaireBean.setQuestionStatus(rowData.get("STATUS") == null ?
                    false : rowData.get("STATUS").toString().equalsIgnoreCase("A") ? true : false);
                // 4272: Maintain history of Questionnaires - End
                questionnaireBean.setQuestionId(rowData.get("QUESTION_ID")== null ? null :
                    new Integer(rowData.get("QUESTION_ID").toString()));
                questionnaireBean.setDescription((String)rowData.get("QUESTION_DESC"));
                questionnaireBean.setQuestionNumber(rowData.get("QUESTION_NUMBER") == null ? null :
                    new Integer(rowData.get("QUESTION_NUMBER").toString()));
                questionnaireBean.setParentQuestionNumber(rowData.get("PARENT_QUESTION_NUMBER") == null ? null :
                    new Integer(rowData.get("PARENT_QUESTION_NUMBER").toString()));
                questionnaireBean.setConditionalFlag(rowData.get("CONDITION_FLAG") == null ?
                    false : rowData.get("CONDITION_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
                questionnaireBean.setConditionValue(rowData.get("CONDITION_VALUE")
                == null ? "" :(String)rowData.get("CONDITION_VALUE"));
                if(rowData.get("CONDITION_VALUE") != null && "".equals(rowData.get("CONDITION_VALUE").toString())){
                    questionnaireBean.setPreviousQuestionFlag(true);
                }
                questionnaireBean.setCondition(rowData.get("CONDITION") == null
                        ? "" : (String)rowData.get("CONDITION"));
                questionnaireBean.setUpdateTimestamp((Timestamp)rowData.get("UPDATE_TIMESTAMP"));
                questionnaireBean.setUpdateUser((String)rowData.get("UPDATE_USER"));
                //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
                if(rowData.get("QUESTION_SEQ_NUMBER") == null){
                    if(!hmSeqQuestions.containsKey(questionnaireBean.getParentQuestionNumber())){
                        hmSeqQuestions.put(questionnaireBean.getParentQuestionNumber(), new BigDecimal(0));
                    }
                    int seqNum = ((BigDecimal) hmSeqQuestions.get(questionnaireBean.getParentQuestionNumber())).intValue();
                    questionnaireBean.setQuestionSequenceNumber(++seqNum);
                    hmSeqQuestions.put(questionnaireBean.getParentQuestionNumber(), new BigDecimal(seqNum));
                    questionnaireBean.setAcType("U");
                } else {
                    int seqNum = ((BigDecimal)rowData.get("QUESTION_SEQ_NUMBER")).intValue();
                    hmSeqQuestions.put(questionnaireBean.getParentQuestionNumber(), new BigDecimal(seqNum));
                    questionnaireBean.setQuestionSequenceNumber(seqNum);
                }
                // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
                if(rowData.get("RULE_ID") != null){
                    questionnaireBean.setConditionRuleId(Integer.parseInt(rowData.get("RULE_ID").toString()));
                    questionnaireBean.setRuleSelectionFlag(true);
                    if(questionnaireBean.getConditionRuleId() != 0){
                        CoeusVector cvRuleDetails = rulesTxnBean.getRuleDetails(questionnaireBean.getConditionRuleId());
                        if(cvRuleDetails != null && !cvRuleDetails.isEmpty()){
                            BusinessRuleBean businessRuleBean = (BusinessRuleBean)cvRuleDetails.get(0);
                            questionnaireBean.setConditionRuleDesc(businessRuleBean.getDescription());
                        }
                    }
                }
                // Added for COEUSQA-3287 Questionnaire Maintenance Features - End
                questionnaireQuestionsData.addElement(questionnaireBean);
            }
        }
        return questionnaireQuestionsData;
    }

    /**This method returns the sata from osp$questionnaire_usage table for a given questionaire id
     * @throws CoeusException
     * @throws DBException
     * @return
     */
    public CoeusVector getQuestionnaireUsageData(int questionnaireId) throws CoeusException,DBException {
        Vector param = new Vector();
        Vector result = null;
        // 4272: Maintain history of Questionnaires
        String flag = "";
        CoeusVector questionnaireUsageData = null;
        QuestionnaireUsageBean questionnaireUsageBean = null ;
        param.add(new Parameter("AV_QUESTIONNAIRE_ID",DBEngineConstants.TYPE_INT,new Integer(questionnaireId)));
        if(dbEngine != null) {
            result = dbEngine.executeRequest(DSN,"call GET_QUESTIONNAIRE_USAGE(<< AV_QUESTIONNAIRE_ID >>," +
                    "<< OUT RESULTSET rset >>)",DSN,param);

        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result != null && result.size() > 0) {
            questionnaireUsageData = new CoeusVector();
            HashMap rowData = null ;
            for(int index = 0 ; index < result.size() ; index ++){
                rowData = (HashMap)result.get(index);
                questionnaireUsageBean = new QuestionnaireUsageBean();
                questionnaireUsageBean.setQuestionnaireId(rowData.get("QUESTIONNAIRE_ID") == null ? 0 :
                    Integer.parseInt(rowData.get("QUESTIONNAIRE_ID").toString()));
                // 4272: Maintain history of Questionnaires - Start
                questionnaireUsageBean.setQuestionnaireVersionNumber(rowData.get("VERSION_NUMBER") == null ? 0 :
                    Integer.parseInt(rowData.get("VERSION_NUMBER").toString()));
                // 4272: Maintain history of Questionnaires - End
                questionnaireUsageBean.setName((String)rowData.get("NAME"));
                questionnaireUsageBean.setModuleItemCode(rowData.get("MODULE_ITEM_CODE") == null ? 0 :
                    Integer.parseInt(rowData.get("MODULE_ITEM_CODE").toString()));
                questionnaireUsageBean.setModuleSubItemCode(rowData.get("MODULE_SUB_ITEM_CODE") == null ? 0 :
                    Integer.parseInt(rowData.get("MODULE_SUB_ITEM_CODE").toString()));
                questionnaireUsageBean.setRuleId(rowData.get("RULE_ID") == null ? 0 :
                    Integer.parseInt(rowData.get("RULE_ID").toString()));
                questionnaireUsageBean.setDescription((String)rowData.get("DESCRIPTION"));
                questionnaireUsageBean.setLabel((String)rowData.get("QUESTIONNAIRE_LABEL"));
                // 4272: Maintain history of Questionnaires - Start
                flag = (String)rowData.get("IS_MANDATORY");
                flag = (flag == null)? "" : flag;
                questionnaireUsageBean.setMandatory((flag.equalsIgnoreCase("Y"))?true : false);
                // 4272: Maintain history of Questionnaires - End
                questionnaireUsageBean.setUpdateTimestamp((Timestamp)rowData.get("UPDATE_TIMESTAMP"));
                questionnaireUsageBean.setUpdateUser((String)rowData.get("UPDATE_USER"));
                questionnaireUsageBean.setUsageTimestamp((Timestamp)rowData.get("USAGE_UPDATE_TIMESTAMP"));
                questionnaireUsageBean.setUsageUser((String)rowData.get("USAGE_UPDATE_USER"));
                questionnaireUsageBean.setAwModuleItemCode(rowData.get("MODULE_ITEM_CODE") == null ? 0 :
                    Integer.parseInt(rowData.get("MODULE_ITEM_CODE").toString()));
                questionnaireUsageBean.setAwModuleSubItemCode(rowData.get("MODULE_SUB_ITEM_CODE") == null ? 0 :
                    Integer.parseInt(rowData.get("MODULE_SUB_ITEM_CODE").toString()));
                questionnaireUsageData.addElement(questionnaireUsageBean);
            }
        }
        return questionnaireUsageData;
    }


    /**
     * This method retrieves the data from OSP$QUESTION table
     * @param isQuestionnaire boolean
     * @throws CoeusException
     * @throws DBException
     * @return CoeusVector
     */
    public CoeusVector getModuleData(boolean isQuestionnaire) throws CoeusException,DBException {

        Vector param = new Vector();
        Vector result = null;
        CoeusVector moduleData = null;
        QuestionsMaintainanceBean questMaintenanceBean = null ;
        ModuleDataBean moduleDataBean = null ;
        //Code added for Case#3875 - Need to add Annual COI as a Module for Questionnaire maintenance
        param.add(new Parameter("AS_QUESTIONNAIRE_ID",DBEngineConstants.TYPE_INT,
                new Integer((isQuestionnaire ? 1 : 0))));
        if(dbEngine != null) {
            //Code commented and modified for Case#3875 - Need to add Annual COI as a Module for Questionnaire maintenance
//             result = dbEngine.executeRequest("Coeus",
//            "call GET_COEUS_MODULE ( <<OUT RESULTSET rset>> )",
//            "Coeus", param);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_COEUS_MODULE (<< AS_QUESTIONNAIRE_ID >>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result != null && result.size() > 0) {
            moduleData = new CoeusVector();
            HashMap rowData = null;
            for(int index = 0; index < result.size(); index++){
                rowData = (HashMap)result.get(index);
                moduleDataBean = new ModuleDataBean();
                moduleDataBean.setCode(rowData.get("MODULE_CODE").toString());
                moduleDataBean.setDescription((String)rowData.get("DESCRIPTION"));
                moduleData.addElement(moduleDataBean);

            }
        }
        return moduleData;
    }

    /**
     * This method retrieves the data from OSP$QUESTION table
     * @throws CoeusException
     * @throws DBException
     * @return
     */
    public CoeusVector getSubModuleData() throws CoeusException,DBException {
        Vector param = new Vector();
        Vector result = null;
        CoeusVector subModuleData = null;
        SubModuleDataBean subModuleDataBean = null ;

        if(dbEngine != null) {
            result = dbEngine.executeRequest("Coeus",
                    "call GET_COEUS_SUB_MODULE ( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result != null && result.size() > 0) {
            subModuleData = new CoeusVector();
            HashMap rowData = null;
            for(int index = 0; index < result.size(); index++){
                subModuleDataBean = new SubModuleDataBean();
                rowData = (HashMap)result.get(index);
                subModuleDataBean.setModuleCode(rowData.get("MODULE_CODE")== null ? 0 :
                    Integer.parseInt(rowData.get("MODULE_CODE").toString()));
                subModuleDataBean.setCode(rowData.get("SUB_MODULE_CODE").toString());
                subModuleDataBean.setDescription((String)rowData.get("DESCRIPTION"));
                subModuleData.addElement(subModuleDataBean);
            }
        }
        return subModuleData;
    }

    /**
     * To get all the datas from the OSP$QUESTION table
     * @throws CoeusException
     * @throws DBException
     * @return CoeusVector
     */
    public CoeusVector getQuestions() throws CoeusException,DBException {
        Vector param = new Vector();
        Vector result = null;
        CoeusVector questionsMaintainanceData = null;
        QuestionsMaintainanceBean questionsMaintainanceBean = null;

        if(dbEngine != null) {
            result = dbEngine.executeRequest("Coeus",
                    "call GET_QUESTIONS ( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result != null && result.size() > 0) {
            questionsMaintainanceData = new CoeusVector();
            HashMap rowData = null;
            for(int index = 0; index < result.size(); index++){
                questionsMaintainanceBean = new QuestionsMaintainanceBean();
                rowData = (HashMap)result.get(index);
                //Added for question group - start - 1
                questionsMaintainanceBean.setGroupTypeCode(rowData.get("GROUP_TYPE_CODE") == null ? 0 :
                    Integer.parseInt(rowData.get("GROUP_TYPE_CODE").toString()));
                //Added for question group - end - 1
                questionsMaintainanceBean.setQuestionId(rowData.get("QUESTION_ID") == null ? null :
                    new Integer(rowData.get("QUESTION_ID").toString()));
                // 4272: Maintain history of Questionnaires - Start
                questionsMaintainanceBean.setVersionNumber(rowData.get("VERSION_NUMBER") == null ? 0 :
                    Integer.parseInt(rowData.get("VERSION_NUMBER").toString()));
                questionsMaintainanceBean.setStatus((String) rowData.get("STATUS"));
                // 4272: Maintain history of Questionnaires  - End
                questionsMaintainanceBean.setDescription((String)rowData.get("QUESTION"));
                questionsMaintainanceBean.setMaxAnswers(rowData.get("MAX_ANSWERS") == null ? 0 :
                    Integer.parseInt(rowData.get("MAX_ANSWERS").toString()));
                questionsMaintainanceBean.setValidAnswers((String)rowData.get("VALID_ANSWER"));
                questionsMaintainanceBean.setLookupName((String)rowData.get("LOOKUP_NAME"));
                questionsMaintainanceBean.setAnswerDataType((String)rowData.get("ANSWER_DATA_TYPE"));
                questionsMaintainanceBean.setAnswerMaxLength(rowData.get("ANSWER_MAX_LENGTH") == null ? 0 :
                    Integer.parseInt(rowData.get("ANSWER_MAX_LENGTH").toString()));
                questionsMaintainanceBean.setLookupGui((String)rowData.get("LOOKUP_GUI"));
                questionsMaintainanceBean.setUpdateTimestamp(((Timestamp)rowData.get("UPDATE_TIMESTAMP")));
                questionsMaintainanceBean.setUpdateUser((String)rowData.get("UPDATE_USER"));
                questionsMaintainanceData.addElement(questionsMaintainanceBean);
            }
        }
        return questionsMaintainanceData;
    }

    /**This method generates the Question Id
     * @throws CoeusException
     * @throws DBException
     * @return int questionID
     */
    public int generateQuestionId()throws CoeusException , DBException {
        int count = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        dbEngine = new DBEngineImpl();
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER QUESTION_ID>> = call FN_GENERATE_QUESTION_ID()}",
                    param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            count = Integer.parseInt(rowParameter.get("QUESTION_ID").toString());
        }
        return count;
    }


    /**This method generates the Question Id
     * @throws CoeusException
     * @throws DBException
     * @return int questionID
     */
    public int getGeneratedQuestionnaireId()throws CoeusException , DBException {
        int count = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        dbEngine = new DBEngineImpl();
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER QUESTIONNAIRE_ID>> = call FN_GENERATE_QUESTIONNAIRE_ID()}",
                    param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            count = Integer.parseInt(rowParameter.get("QUESTIONNAIRE_ID").toString());
        }
        return count;
    }

    /**
     *
     * @throws DBException
     * @throws CoeusException
     * @return
     */
    public CoeusVector getQuestionnaireDetails() throws DBException,CoeusException{
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        CoeusVector questionnaireData = null;
        QuestionnaireBaseBean questionnaireBaseBean;
        if(dbEngine != null) {
            result = dbEngine.executeRequest(DSN,"call GET_QUESTIONNAIRE_DATA(<< OUT RESULTSET rset >>)",DSN,param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result != null && result.size() > 0) {
            HashMap rowData = null ;
            questionnaireData = new CoeusVector();
            for(int index = 0 ; index < result.size() ; index ++){
                rowData = (HashMap)result.get(index);
                questionnaireBaseBean = new QuestionnaireBaseBean();
                questionnaireBaseBean.setQuestionnaireId(rowData.get("QUESTIONNAIRE_ID") == null ? 0 :
                    Integer.parseInt(rowData.get("QUESTIONNAIRE_ID").toString()));
                // 4272: Maintain history of Questionnaires - Start
                questionnaireBaseBean.setQuestionnaireVersionNumber(rowData.get("VERSION_NUMBER") == null ? 0 :
                    Integer.parseInt(rowData.get("VERSION_NUMBER").toString()));
                // 4272: Maintain history of Questionnaires - End
                questionnaireBaseBean.setName((String)rowData.get("NAME"));
                questionnaireBaseBean.setDescription((String)rowData.get("DESCRIPTION"));
                questionnaireBaseBean.setUpdateTimestamp((Timestamp)rowData.get("UPDATE_TIMESTAMP"));
                questionnaireBaseBean.setUpdateUser((String)rowData.get("UPDATE_USER"));
                //Added for coeus4.3 Questionnaire enhancement case#2946 - starts
                String flag = (String)rowData.get("IS_FINAL");
                flag = (flag == null)? "" : flag;
                questionnaireBaseBean.setFinalFlag((flag.equals("Y"))?true : false);
                flag = (String)rowData.get("QUESTIONNAIRE_COMPLETED_FLAG");
                flag = (flag == null)? "" : flag;
                questionnaireBaseBean.setCompletedFlag((flag.equals("Y"))?true : false);
                //Added for coeus4.3 Questionnaire enhancement case#2946 - ends
                // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
                questionnaireBaseBean.setGroupTypeCode(rowData.get("GROUP_TYPE_CODE")== null ? 0 :
                    (Integer.parseInt(rowData.get("GROUP_TYPE_CODE").toString())));
                // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
                questionnaireData.addElement(questionnaireBaseBean );
            }
        }
        return questionnaireData;
    }

    /**
     *  This method used to Questionnaire detail based on questionnaire module
     *  <li>To fetch the data, it uses the function GET_QUESTIONNAIRE_LIST_FOR_MOD.
     *
     *  @return vector containing data
     *  @param QuestionnaireAnswerHeaderBean containing module detail
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getQuestionnaireModeDetails(QuestionnaireAnswerHeaderBean
            questionnaireAnswerHeaderBean) throws DBException,CoeusException{
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        CoeusVector questionnaireModeData = null;
        QuestionnaireAnswerHeaderBean answerHeaderBean = null ;
        param.add(new Parameter("AV_MODULE_ITEM_CODE",DBEngineConstants.TYPE_INT,
                new Integer(questionnaireAnswerHeaderBean.getModuleItemCode())));
        param.add(new Parameter("AV_MODULE_SUB_ITEM_CODE",DBEngineConstants.TYPE_INT,
                new Integer(questionnaireAnswerHeaderBean.getModuleSubItemCode())));
        if(dbEngine != null) {
            result = dbEngine.executeRequest(DSN,"call GET_QUESTIONNAIRE_LIST_FOR_MOD" +
                    "(<< AV_MODULE_ITEM_CODE >>,<< AV_MODULE_SUB_ITEM_CODE >>,<< OUT RESULTSET rset >>)",DSN,param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result != null && result.size() > 0) {
            HashMap rowData = null ;
            questionnaireModeData = new CoeusVector();
            for(int index = 0 ; index < result.size() ; index ++){
                rowData = (HashMap)result.get(index);
                answerHeaderBean = new QuestionnaireAnswerHeaderBean();
                answerHeaderBean.setQuestionnaireId(rowData.get("QUESTIONNAIRE_ID") == null ? 0 :
                    Integer.parseInt(rowData.get("QUESTIONNAIRE_ID").toString()));
                // 4272: Maintain History of Questionnaires - Start
                answerHeaderBean.setQuestionnaireVersionNumber(rowData.get("VERSION_NUMBER") == null ? 0 :
                    Integer.parseInt(rowData.get("VERSION_NUMBER").toString()));
                // 4272: Maintain History of Questionnaires - End
                answerHeaderBean.setLabel(((String)rowData.get("QUESTIONNAIRE_LABEL")));
                questionnaireModeData.addElement(answerHeaderBean );
            }
        }
        return questionnaireModeData;
    }
    /**
     *  This method used to Questionnaire detail based on questionnaire module
     *  <li>To fetch the data, it uses the function GET_QUESTIONNAIRE_ANSWERS.
     *
     *  @return vector containing data
     *  @param QuestionnaireAnswerHeaderBean containing module detail
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getQuestionnaireAnswersDetails(QuestionnaireAnswerHeaderBean
            questionnaireAnswerHeaderBean) throws DBException,CoeusException{
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        CoeusVector questionnaireAnswerData = null;
        QuestionAnswerBean questionnaireBean = null ;
        param.add(new Parameter("AV_MODULE_ITEM_CODE",DBEngineConstants.TYPE_INT,
                new Integer(questionnaireAnswerHeaderBean.getModuleItemCode())));
        // Modified with CoeusQA2313: Completion of Questionnaire for Submission
        // applicable modulecode/itemkey/subitemkey will be same as actual modulecode/itemkey/subitemkey in normal scenarios.
        // For amendment/renewals, this can vary based on whether is answer is present for amendment or original protocol
        param.add(new Parameter("AV_MODULE_ITEM_KEY",DBEngineConstants.TYPE_STRING,
                questionnaireAnswerHeaderBean.getApplicableModuleItemKey()));
        param.add(new Parameter("AV_MODULE_SUB_ITEM_CODE",DBEngineConstants.TYPE_INT,
                new Integer(questionnaireAnswerHeaderBean.getApplicableSubmoduleCode())));


       if(questionnaireAnswerHeaderBean.getModuleItemCode()==3 && questionnaireAnswerHeaderBean.getModuleSubItemCode()==6)
         {

        param.add(new Parameter("AV_MODULE_SUB_ITEM_KEY",DBEngineConstants.TYPE_STRING,
                questionnaireAnswerHeaderBean.getApplicableModuleSubItemKeyForPpc()));
         }
       else
       {
          param.add(new Parameter("AV_MODULE_SUB_ITEM_KEY",DBEngineConstants.TYPE_INT,
                questionnaireAnswerHeaderBean.getApplicableModuleSubItemKey()));
       }


//        param.add(new Parameter("AV_UPDATE_USER",DBEngineConstants.TYPE_STRING,
//                questionnaireAnswerHeaderBean.getCurrentUser()));
//        // CoeusQA2313: Completion of Questionnaire for Submission - End
//
//        if(dbEngine != null) {
//            result = dbEngine.executeRequest(DSN,"call GET_QUESTIONNAIRE_ANSWER_PPC" +
//                    "(<< AV_MODULE_ITEM_CODE >>,<< AV_MODULE_ITEM_KEY >>,<< AV_MODULE_SUB_ITEM_CODE >>" +
//                    ",<< AV_MODULE_SUB_ITEM_KEY >>,<<AV_UPDATE_USER>>,<< OUT RESULTSET rset >>)",DSN,param);
//        } else {
//            throw new CoeusException("db_exceptionCode.1000");
//        }
//
//
//         }
//         else
//         {
           if(dbEngine != null) {
            result = dbEngine.executeRequest(DSN,"call GET_QUESTIONNAIRE_ANSWERS" +
                    "(<< AV_MODULE_ITEM_CODE >>,<< AV_MODULE_ITEM_KEY >>,<< AV_MODULE_SUB_ITEM_CODE >>" +
                    ",<< AV_MODULE_SUB_ITEM_KEY >>,<< OUT RESULTSET rset >>)",DSN,param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }

//         }





        if(result != null && result.size() > 0) {
            HashMap rowData = null ;
            questionnaireAnswerData = new CoeusVector();
            for(int index = 0 ; index < result.size() ; index++){
                questionnaireBean = new QuestionAnswerBean();
                rowData = (HashMap)result.get(index);
                questionnaireBean.setQuestionnaireId(Integer.parseInt(rowData.get("QUESTIONNAIRE_ID").toString()));
                // 4272: Maintain history of Questionnaires
                questionnaireBean.setQuestionnaireVersionNumber(Integer.parseInt(rowData.get("QUESTIONNAIRE_VERSION_NUMBER").toString()));
                questionnaireBean.setQuestionnaireCompletionId((String)rowData.get("QUESTIONNAIRE_COMPLETION_ID").toString());
                questionnaireBean.setQuestionId(new Integer(rowData.get("QUESTION_ID").toString()));
                // 4272: Maintain history of Questionnaires
                // Added for CoeusQA2313: Completion of Questionnaire for Submission
                questionnaireBean.setAnsweredModuleItemKey((String)rowData.get("MODULE_ITEM_KEY"));
                questionnaireBean.setAnsweredSubmoduleCode(Integer.parseInt(rowData.get("MODULE_SUB_ITEM_CODE").toString()));

if(questionnaireAnswerHeaderBean.getModuleItemCode()==3 && questionnaireAnswerHeaderBean.getModuleSubItemCode()==6)
         {
                questionnaireBean.setAnsweredModuleSubItemKeyForppc(rowData.get("MODULE_SUB_ITEM_KEY").toString());
                // CoeusQA2313: Completion of Questionnaire for Submission - End

       }
    else
       {
        questionnaireBean.setAnsweredModuleSubItemKey(Integer.parseInt(rowData.get("MODULE_SUB_ITEM_KEY").toString()));
       }


                questionnaireBean.setVersionNumber(new Integer(rowData.get("QUESTION_VERSION_NUMBER").toString()).intValue());
                questionnaireBean.setQuestionNumber(Integer.parseInt(rowData.get("QUESTION_NUMBER").toString()));
                questionnaireBean.setAnswerNumber(Integer.parseInt(rowData.get("ANSWER_NUMBER").toString()));
                questionnaireBean.setAnswer(rowData.get("ANSWER") == null ? "" : (String)rowData.get("ANSWER")) ;
                questionnaireBean.setAwAnswer(rowData.get("ANSWER") == null ? "" : (String)rowData.get("ANSWER")) ;
                questionnaireBean.setDescription(rowData.get("DESCRIPTION") == null ? "" : (String)rowData.get("DESCRIPTION"));
                questionnaireBean.setUpdateTimestamp((Timestamp)rowData.get("UPDATE_TIMESTAMP"));
                questionnaireBean.setUpdateUser((String)rowData.get("UPDATE_USER"));
                questionnaireAnswerData.addElement(questionnaireBean);
            }
        }
        return questionnaireAnswerData;
    }
    /**
     * This method retrieves the data from OSP$QUESTIONNAIRE_QUESTIONS table
     * @throws CoeusException
     * @throws DBException
     * @return
     */
    // 4272: Maintain history of Questionnaires
//    public CoeusVector getQuestionnaireQuestionsDetails(int questionnaireId) throws CoeusException,DBException {
    public CoeusVector getQuestionnaireQuestionsDetails(QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean) throws CoeusException,DBException {
        Vector param = new Vector();
        Vector result = null;
        CoeusVector questionnaireQuestionsData = null;
        QuestionnaireQuestionsBean questionnaireBean = null ;
        // 4272: Maintain history of Questionnaires - Start
//        param.add(new Parameter("AV_QUESTIONNAIRE_ID",DBEngineConstants.TYPE_INT,new Integer(questionnaireId)));
        param.add(new Parameter("AV_QUESTIONNAIRE_ID",DBEngineConstants.TYPE_INT,""+questionnaireAnswerHeaderBean.getQuestionnaireId()));
        param.add(new Parameter("AV_QNR_VERSION_NUMBER",DBEngineConstants.TYPE_INT,""+questionnaireAnswerHeaderBean.getQuestionnaireVersionNumber()));
        if(dbEngine != null) {
//            result = dbEngine.executeRequest(DSN,"call GET_QUESTIONNAIRE_QUESTIONS(<< AV_QUESTIONNAIRE_ID >>," +
//                    "<< OUT RESULTSET rset >>)",DSN,param);
            result = dbEngine.executeRequest(DSN,"call GET_QUESTIONNAIRE_QUESTIONS(<< AV_QUESTIONNAIRE_ID >>, << AV_QNR_VERSION_NUMBER >>," +
                    "<< OUT RESULTSET rset >>)",DSN,param);
         // 4272: Maintain history of Questionnaires - End
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result != null && result.size() > 0) {
            HashMap rowData = null ;
            questionnaireQuestionsData = new CoeusVector();
            for(int index = 0 ; index < result.size() ; index++){
                questionnaireBean = new QuestionnaireQuestionsBean();
                rowData = (HashMap)result.get(index);
                questionnaireBean.setQuestionnaireId(rowData.get("QUESTIONNAIRE_ID")== null ? 0 :
                    (Integer.parseInt(rowData.get("QUESTIONNAIRE_ID").toString())));
                // 4272: Maintain history of Questionnaires - Start
                questionnaireBean.setQuestionnaireVersionNumber(rowData.get("QUESTIONNAIRE_VERSION_NUMBER")== null ? 0 :
                    (Integer.parseInt(rowData.get("QUESTIONNAIRE_VERSION_NUMBER").toString())));
                // 4272: Maintain history of Questionnaires - End
                questionnaireBean.setQuestionId(new Integer(rowData.get("QUESTION_ID").toString()));
                // 4272: Maintain history of Questionnaires - Start
                questionnaireBean.setQuestionVersionNumber(new Integer(rowData.get("QUESTION_VERSION_NUMBER").toString()));
                questionnaireBean.setQuestionStatus(rowData.get("STATUS") == null ?
                    false : rowData.get("STATUS").toString().equalsIgnoreCase("A") ? true : false);
                // 4272: Maintain history of Questionnaires - End
                questionnaireBean.setMaxAnswers(rowData.get("MAX_ANSWERS") == null ? 0 :
                    Integer.parseInt(rowData.get("MAX_ANSWERS").toString()));
                questionnaireBean.setValidAnswer((String)rowData.get("VALID_ANSWER"));
                questionnaireBean.setLookUpName(rowData.get("LOOKUP_GUI")== null ?
                    "" : (String)rowData.get("LOOKUP_NAME"));
                questionnaireBean.setAnswerDataType((String)rowData.get("ANSWER_DATA_TYPE"));
                questionnaireBean.setAnswerMaxLength(rowData.get("ANSWER_MAX_LENGTH")== null ? 0 :
                    Integer.parseInt(rowData.get("ANSWER_MAX_LENGTH").toString()));
                questionnaireBean.setLookUpGui(rowData.get("LOOKUP_GUI")== null ?
                    "" : (String)rowData.get("LOOKUP_GUI"));
                questionnaireBean.setDescription((String)rowData.get("QUESTION_DESC"));
                questionnaireBean.setQuestionNumber(new Integer(rowData.get("QUESTION_NUMBER").toString()));
                questionnaireBean.setParentQuestionNumber(new Integer(rowData.get("PARENT_QUESTION_NUMBER").toString()));
                questionnaireBean.setConditionalFlag(rowData.get("CONDITION_FLAG") == null ?
                    false : rowData.get("CONDITION_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
                questionnaireBean.setConditionValue(rowData.get("CONDITION_VALUE")
                == null ? "" :(String)rowData.get("CONDITION_VALUE"));
                questionnaireBean.setCondition(rowData.get("CONDITION") == null
                        ? "" : (String)rowData.get("CONDITION"));
                questionnaireBean.setUpdateTimestamp((Timestamp)rowData.get("UPDATE_TIMESTAMP"));
                questionnaireBean.setUpdateUser((String)rowData.get("UPDATE_USER"));
                //if(questionnaireBean.get)
                // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
                if(rowData.get("RULE_ID") != null){
                    questionnaireBean.setConditionRuleId(Integer.parseInt(rowData.get("RULE_ID").toString()));
                    questionnaireBean.setRuleSelectionFlag(true);
                }
                // Added for COEUSQA-3287 Questionnaire Maintenance Features - End
                questionnaireQuestionsData.addElement(questionnaireBean);
            }
        }
        return questionnaireQuestionsData;
    }


    /** This will get the questionnaire usage for the given moduleCode and
     *Module Sub Item Code
     */
    public  Vector getUsageForModule(int moduleCode, int moduleSubItemCode)throws CoeusException,DBException {
        Vector param = new Vector();
        Vector result = null;
        Vector questionnaireUsageData = null;
        QuestionnaireUsageBean questionnaireUsageBean = null ;
        param.add(new Parameter("AV_MODULE_ITEM_CODE",DBEngineConstants.TYPE_INT,new Integer(moduleCode)));
        param.add(new Parameter("AV_MODULE_SUB_ITEM_CODE",DBEngineConstants.TYPE_INT,new Integer(moduleSubItemCode)));
        if(dbEngine != null) {

            result = dbEngine.executeRequest(DSN,
                    "call GET_QUESTIONNAIRE_LIST_FOR_MOD( <<AV_MODULE_ITEM_CODE>> , <<AV_MODULE_SUB_ITEM_CODE>>,"+
                    "<<OUT RESULTSET rset>> )", DSN, param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result != null && result.size() > 0) {
            questionnaireUsageData = new Vector();
            HashMap rowData = null ;
            for(int index = 0 ; index < result.size() ; index ++){
                rowData = (HashMap)result.get(index);
                questionnaireUsageBean = new QuestionnaireUsageBean();
                questionnaireUsageBean.setQuestionnaireId(rowData.get("QUESTIONNAIRE_ID") == null ? 0 :
                    Integer.parseInt(rowData.get("QUESTIONNAIRE_ID").toString()));
                // COEUSDEV-252: User message field should be disabled in condition editor for Question rules - Start
                questionnaireUsageBean.setQuestionnaireVersionNumber(rowData.get("VERSION_NUMBER") == null ? 0 :
                    Integer.parseInt(rowData.get("VERSION_NUMBER").toString()));
                // COEUSDEV-252: User message field should be disabled in condition editor for Question rules - End
                questionnaireUsageBean.setLabel((String)rowData.get("QUESTIONNAIRE_LABEL"));
                questionnaireUsageData.addElement(questionnaireUsageBean);
            }
        }
        return questionnaireUsageData;
    }

    /** to check whether a question can be deleted from the questionnaire, if it
     *is answered in any module.
     *@param question Id and questionnaire ID
     *@returns boolean says a question can be deleted or not
     */
    public boolean canDeleteQuestionFromquestionanire(int questionId, int questionnaireId)
    throws CoeusException,DBException {
        boolean canDelete = false;
        Vector param = new Vector();
        Vector result = null;
        Vector questionnaireUsageData = null;
        QuestionnaireUsageBean questionnaireUsageBean = null ;
        param.add(new Parameter("AV_QUESTION_ID",DBEngineConstants.TYPE_INT,new Integer(questionId)));
        param.add(new Parameter("AV_QUESTIONNAIRE_ID",DBEngineConstants.TYPE_INT,new Integer(questionnaireId)));
        if(dbEngine != null) {
            result = dbEngine.executeFunctions(DSN,
                    "{ <<OUT INTEGER CAN_DELETE_QUESTION>> = "
                    +" call FN_CAN_DEL_QUESTION_FROM_QNR(<< AV_QUESTION_ID >>, <<AV_QUESTIONNAIRE_ID>>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            int count = Integer.parseInt(rowParameter.get("CAN_DELETE_QUESTION").toString());
            if(count == 1){
                canDelete = true;
            }else if(count == 0){
                canDelete = false;
            }
        }
        return canDelete;
    }


    /**To check whether the given module, submodule for the given questionnaire
     *can be deleted or not. If it used in the modules and answered then the module
     *can't be deleted
     *@param moduleCode, subModuleItemCode and questionanire Id
     *@param boolean says can be deleted or not
     */
    public boolean canDeleteQuestionnaireUsage(int moduleCode, int moduleSubItemCode, int questionnaireId)
    throws CoeusException,DBException {
        boolean canDelete = false;
        Vector param = new Vector();
        Vector result = null;
        Vector questionnaireUsageData = null;
        QuestionnaireUsageBean questionnaireUsageBean = null ;
        param.add(new Parameter("AV_MODULE_ITEM_CODE",DBEngineConstants.TYPE_INT,new Integer(moduleCode)));
        param.add(new Parameter("AV_MODULE_SUB_ITEM_CODE",DBEngineConstants.TYPE_INT,new Integer(moduleSubItemCode)));
        param.add(new Parameter("AV_QUESTIONNAIRE_ID",DBEngineConstants.TYPE_INT,new Integer(questionnaireId)));
        if(dbEngine != null) {
            result = dbEngine.executeFunctions(DSN,
                    "{ <<OUT INTEGER CAN_DELTE_USAGE>> = "
                    +" call FN_CAN_DEL_QUESTIONNAIRE_USAGE(<< AV_MODULE_ITEM_CODE >>, <<AV_MODULE_SUB_ITEM_CODE>>,<<AV_QUESTIONNAIRE_ID>>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            int count = Integer.parseInt(rowParameter.get("CAN_DELTE_USAGE").toString());
            if(count == 1){
                canDelete = true;
            }else if(count == 0){
                canDelete = false;
            }
        }
        return canDelete;
    }

    /**
     * Added for question group - start - 2
     * This method get question groups from the database table OSP$QUESTION_GROUP
     * using the stored procedure GET_QUESTION_GROUP
     * @return CoeusVector
     * @throws CoeusException
     * @throws DBException
     */
    public Vector getQuestionGroups() throws DBException{

        Vector result = new Vector(3,2);
        Vector vecQuestionGroups = null;
        HashMap hmQuestionGroups = null;
        Vector param = new Vector();

        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_GROUP_TYPES ( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int count = result.size();
        if (count >0){
            vecQuestionGroups = new CoeusVector();
            ComboBoxBean comboBoxBean = new ComboBoxBean();
            comboBoxBean.setCode("");
            comboBoxBean.setDescription("");
            vecQuestionGroups.addElement(comboBoxBean);
            for(int index = 0; index < count; index++){
                hmQuestionGroups = (HashMap)result.elementAt(index);
                vecQuestionGroups.addElement(new ComboBoxBean(
                        hmQuestionGroups.get("GROUP_TYPE_CODE").toString(),
                        hmQuestionGroups.get("GROUP_NAME").toString()));
            }
        }
        return vecQuestionGroups;
    }
    //Added for question group - end - 2

    /**
     *  Code added for coeus4.3 Questionnaire enhancement case#2946
     *  This method used to get Questionnaire detail based on questionnaire module
     *  and questionnaire module key
     *  <li>To fetch the data, it uses the function GET_QUESTIONNAIRE_ANS_HEADER.
     *
     *  @return vector containing data
     *  @param QuestionnaireAnswerHeaderBean containing module detail
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getQuestionnaireHeaderDetails(QuestionnaireAnswerHeaderBean
            questionnaireAnswerHeaderBean) throws DBException,CoeusException{

        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        CoeusVector questionnaireModeData = null;
        QuestionnaireAnswerHeaderBean answerHeaderBean = null ;
        param.add(new Parameter("AV_MODULE_ITEM_CODE",DBEngineConstants.TYPE_INT,
                new Integer(questionnaireAnswerHeaderBean.getModuleItemCode())));
        param.addElement(new Parameter("AV_MODULE_ITEM_KEY",DBEngineConstants.TYPE_STRING,
                questionnaireAnswerHeaderBean.getModuleItemKey()));
        param.add(new Parameter("AV_MODULE_SUB_ITEM_CODE",DBEngineConstants.TYPE_INT,
                new Integer(questionnaireAnswerHeaderBean.getModuleSubItemCode())));




         if(questionnaireAnswerHeaderBean.getModuleItemCode()==3 && questionnaireAnswerHeaderBean.getModuleSubItemCode()==6)
         {
      param.addElement(new Parameter("AV_MODULE_SUB_ITEM_KEY",DBEngineConstants.TYPE_STRING,
                ""+questionnaireAnswerHeaderBean.getModuleSubItemKey()));
             param.addElement(new Parameter("AV_UPDATE_USER",DBEngineConstants.TYPE_STRING,
                ""+questionnaireAnswerHeaderBean.getCurrentUser()));

           if(dbEngine != null) {
            result = dbEngine.executeRequest(DSN,"call GET_QUEST_ANS_HEADER_PPC" +
                    "(<< AV_MODULE_ITEM_CODE >>,<< AV_MODULE_ITEM_KEY >>,<< AV_MODULE_SUB_ITEM_CODE >>,"
                    + "<< AV_MODULE_SUB_ITEM_KEY >>,<< AV_UPDATE_USER >>,<< OUT RESULTSET rset >>)",DSN,param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }

         }
         else
         {
      param.addElement(new Parameter("AV_MODULE_SUB_ITEM_KEY",DBEngineConstants.TYPE_INT,
                ""+questionnaireAnswerHeaderBean.getModuleSubItemKey()));
        if(dbEngine != null) {
            result = dbEngine.executeRequest(DSN,"call GET_QUESTIONNAIRE_ANS_HEADER" +
                    "(<< AV_MODULE_ITEM_CODE >>,<< AV_MODULE_ITEM_KEY >>,<< AV_MODULE_SUB_ITEM_CODE >>,"
                    + "<< AV_MODULE_SUB_ITEM_KEY >>,<< OUT RESULTSET rset >>)",DSN,param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }

         }

        if(result != null && result.size() > 0) {
            HashMap rowData = null ;
            questionnaireModeData = new CoeusVector();
            for(int index = 0 ; index < result.size() ; index ++){
                rowData = (HashMap)result.get(index);
                answerHeaderBean = new QuestionnaireAnswerHeaderBean();
                answerHeaderBean.setQuestionnaireId(rowData.get("QUESTIONNAIRE_ID") == null ? 0 :
                    Integer.parseInt(rowData.get("QUESTIONNAIRE_ID").toString()));
                // 4272: Maintain history of Questionnaires - Start
                answerHeaderBean.setQuestionnaireVersionNumber(rowData.get("QUESTIONNAIRE_VERSION_NUMBER") == null ? 0 :
                    Integer.parseInt(rowData.get("QUESTIONNAIRE_VERSION_NUMBER").toString()));
                answerHeaderBean.setLabel(((String)rowData.get("QUESTIONNAIRE_LABEL")));
                // 4272: Maintain history of Questionnaires - End
                // Added with CoeusQA2313: Completion of Questionnaire for Submission
                answerHeaderBean.setModuleItemCode(rowData.get("MODULE_ITEM_CODE") == null ? 0 :
                    Integer.parseInt(rowData.get("MODULE_ITEM_CODE").toString()));
                answerHeaderBean.setModuleSubItemCode(questionnaireAnswerHeaderBean.getModuleSubItemCode());
                answerHeaderBean.setApplicableSubmoduleCode(rowData.get("MODULE_SUB_ITEM_CODE") == null ? 0 :
                    Integer.parseInt(rowData.get("MODULE_SUB_ITEM_CODE").toString()));
                answerHeaderBean.setApplicableModuleItemKey((String)rowData.get("MODULE_ITEM_KEY"));

                if(questionnaireAnswerHeaderBean.getModuleItemCode()==3 && questionnaireAnswerHeaderBean.getModuleSubItemCode()==6)
                {
               answerHeaderBean.setApplicableModuleSubItemKeyForPpc((String)rowData.get("MODULE_SUB_ITEM_KEY"));


                }
                else
                {
                  answerHeaderBean.setApplicableModuleSubItemKey(rowData.get("MODULE_SUB_ITEM_KEY") == null ? 0 :
                    Integer.parseInt(rowData.get("MODULE_SUB_ITEM_KEY").toString()));
                // CoeusQA2313: Completion of Questionnaire for Submission - End
                }

                answerHeaderBean.setQuestionnaireCompletionId(((String)rowData.get("QUESTIONNAIRE_COMPLETION_ID")));
                answerHeaderBean.setQuestionnaireCompletionFlag(((String)rowData.get("QUESTIONNAIRE_COMPLETED_FLAG")));
                answerHeaderBean.setName(((String)rowData.get("NAME")));
                questionnaireModeData.addElement(answerHeaderBean );
            }
        }
        return questionnaireModeData;
    }

    /**
     * Code added for Case#2785 - Routing Enhancements
     * To get the Questionnaire List according to the rules satisfied
     * @param questionnaireAnswerHeaderBean QuestionnaireAnswerHeaderBean
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return HashMap
     */
    public HashMap getRuleBasedQuestionnaire(QuestionnaireAnswerHeaderBean
            questionnaireAnswerHeaderBean)throws CoeusException,DBException {
        HashMap hmQuestionId = new HashMap();
        Vector param = new Vector();
        Vector result = null;
        Vector questionnaireUsageData = null;
        QuestionnaireUsageBean questionnaireUsageBean = null ;

        param.add(new Parameter("AS_MODULE_CODE",DBEngineConstants.TYPE_INT,
                new Integer(questionnaireAnswerHeaderBean.getModuleItemCode())));
        param.addElement(new Parameter("AS_MODULE_ITEM_KEY",DBEngineConstants.TYPE_STRING,
                questionnaireAnswerHeaderBean.getModuleItemKey()));
        param.addElement(new Parameter("AS_MODULE_ITEM_KEY_SEQUENCE",DBEngineConstants.TYPE_STRING,
                ""+questionnaireAnswerHeaderBean.getModuleSubItemKey()));
        //Added userId param with COEUSDEV 325-Ability to route a protocol to PI if the person submitting the protocol IS NOT the PI
        param.addElement(new Parameter("AS_USER",DBEngineConstants.TYPE_STRING,userId));

        if(dbEngine != null) {
            result = dbEngine.executeFunctions(DSN,
                    "{ <<OUT STRING QUEST_IDS>> = "
                    +" call FN_GET_QUESTIONNAIRE_IDS(<<AS_MODULE_CODE>>, <<AS_MODULE_ITEM_KEY>>,<<AS_MODULE_ITEM_KEY_SEQUENCE >>,<<AS_USER>>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            if(rowParameter.get("QUEST_IDS") != null){
                String ids = rowParameter.get("QUEST_IDS").toString()+",";
                while(ids.indexOf(",") != -1){
                    String questionnaireId = ids.substring(0, ids.indexOf(",")).trim();
                    hmQuestionId.put(questionnaireId, questionnaireId);
                    ids = ids.substring(ids.indexOf(",")+1).trim();
                }
            }
        }
        return hmQuestionId;
    }
    //Added for case 2785 - Routing enhancement start
    /**
     * This method retrieves all the distinct questions for the given questionnaire id
     *
     * @param questionnaireId
     * @param questionnaireVersionNumber
     * @throws CoeusException
     * @throws DBException
     * @return
     */
    // COEUSDEV-252: User message field should be disabled in condition editor for Question rules
//    public CoeusVector getDistinctQuestionareQuestions(int questionnaireId) throws CoeusException,DBException {
    public CoeusVector getDistinctQuestionareQuestions(int questionnaireId, int questionnaireVersionNumber) throws CoeusException,DBException {
        Vector param = new Vector();
        Vector result = null;
        CoeusVector questionnaireQuestionsData = null;
        QuestionnaireQuestionsBean questionnaireBean = null ;
        param.add(new Parameter("AV_QUESTIONNAIRE_ID",DBEngineConstants.TYPE_INT,new Integer(questionnaireId)));
        param.add(new Parameter("AV_QUESTIONNAIRE_VERSION",DBEngineConstants.TYPE_INT,new Integer(questionnaireVersionNumber)));
        if(dbEngine != null) {
//            result = dbEngine.executeRequest(DSN,"call GET_DISTINCT_QUESTONARE_QUESTS(<< AV_QUESTIONNAIRE_ID >>," +
            result = dbEngine.executeRequest(DSN,"call GET_DISTINCT_QUESTONARE_QUESTS(<< AV_QUESTIONNAIRE_ID >>, << AV_QUESTIONNAIRE_VERSION >>," +
                    "<< OUT RESULTSET rset >>)",DSN,param);

        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result != null && result.size() > 0) {
            HashMap rowData = null ;
            questionnaireQuestionsData = new CoeusVector();
            RulesTxnBean rulesTxnBean = new RulesTxnBean();
            for(int index = 0 ; index < result.size() ; index++){
                questionnaireBean = new QuestionnaireQuestionsBean();
                rowData = (HashMap)result.get(index);
                questionnaireBean.setQuestionnaireId(questionnaireId);
                // COEUSDEV-252: User message field should be disabled in condition editor for Question rules- Start
                questionnaireBean.setQuestionnaireVersionNumber(questionnaireVersionNumber);
                questionnaireBean.setQuestionVersionNumber(rowData.get("QUESTION_VERSION_NUMBER") == null ? 0 :
                    Integer.valueOf(rowData.get("QUESTION_VERSION_NUMBER").toString()));
                // COEUSDEV-252: User message field should be disabled in condition editor for Question rules - End
                questionnaireBean.setQuestionId(new Integer(rowData.get("QUESTION_ID").toString()));
                questionnaireBean.setMaxAnswers(rowData.get("MAX_ANSWERS") == null ? 0 :
                    Integer.parseInt(rowData.get("MAX_ANSWERS").toString()));
                questionnaireBean.setValidAnswer((String)rowData.get("VALID_ANSWER"));
                questionnaireBean.setLookUpName(rowData.get("LOOKUP_NAME")== null ?
                    "" : (String)rowData.get("LOOKUP_NAME"));
                questionnaireBean.setAnswerDataType((String)rowData.get("ANSWER_DATA_TYPE"));
                questionnaireBean.setAnswerMaxLength(rowData.get("ANSWER_MAX_LENGTH")== null ? 0 :
                    Integer.parseInt(rowData.get("ANSWER_MAX_LENGTH").toString()));
                questionnaireBean.setLookUpGui(rowData.get("LOOKUP_GUI")== null ?
                    "" : (String)rowData.get("LOOKUP_GUI"));
                questionnaireBean.setDescription((String)rowData.get("QUESTION_DESC"));
                questionnaireBean.setConditionalFlag(rowData.get("CONDITION_FLAG") == null ?
                    false : rowData.get("CONDITION_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
                questionnaireBean.setConditionValue(rowData.get("CONDITION_VALUE")
                == null ? "" :(String)rowData.get("CONDITION_VALUE"));
                questionnaireBean.setCondition(rowData.get("CONDITION") == null
                        ? "" : (String)rowData.get("CONDITION"));
                // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
                if(rowData.get("RULE_ID") != null){
                    questionnaireBean.setConditionRuleId(Integer.parseInt(rowData.get("RULE_ID").toString()));
                    questionnaireBean.setRuleSelectionFlag(true);
                    if(questionnaireBean.getConditionRuleId() != 0){
                        CoeusVector cvRuleDetails = rulesTxnBean.getRuleDetails(questionnaireBean.getConditionRuleId());
                        if(cvRuleDetails != null && !cvRuleDetails.isEmpty()){
                            BusinessRuleBean businessRuleBean = (BusinessRuleBean)cvRuleDetails.get(0);
                            questionnaireBean.setConditionRuleDesc(businessRuleBean.getDescription());
                        }
                    }

                }
                // Added for COEUSQA-3287 Questionnaire Maintenance Features - End

                questionnaireQuestionsData.addElement(questionnaireBean);
            }
        }
        return questionnaireQuestionsData;
    }
    //Added for case 2785 - Routing enhancement - end

    // Case# 3524: Add Explanation field to Questions - Start

    /**
     * This method retrieves the data from OSP$QUESTION_EXPLANATION table
     * @throws CoeusException
     * @throws DBException
     * @return cvQuestionExplanation CoeusVector
     */

    public CoeusVector getQuestionExplanation( QuestionExplanationBean questionExplanationBean
            ) throws DBException,CoeusException{
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        CoeusVector cvQuestionExplanation = null;

        param.add(new Parameter("AS_QUESTION_ID",DBEngineConstants.TYPE_INT,
                questionExplanationBean.getQuestionId()));
        if(dbEngine != null) {
            result = dbEngine.executeRequest(DSN,"call GET_QUESTION_EXPLANATION" +
                    "(<< AS_QUESTION_ID >>,<< OUT RESULTSET CUR_RES >>)",DSN,param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result != null && result.size() > 0) {
            HashMap rowData = null ;
            cvQuestionExplanation = new CoeusVector();
            for(int index = 0 ; index < result.size() ; index ++){
                rowData = (HashMap)result.get(index);
                questionExplanationBean = new QuestionExplanationBean();
                questionExplanationBean.setQuestionId( new Integer(Integer.parseInt(rowData.get("QUESTION_ID").toString())));
                questionExplanationBean.setExplanationType(((String)rowData.get("EXPLANATION_TYPE")));
                questionExplanationBean.setExplanation(((String)rowData.get("EXPLANATION")));
                questionExplanationBean.setUpdateTimestamp(((Timestamp)rowData.get("UPDATE_TIMESTAMP")));
                cvQuestionExplanation.addElement(questionExplanationBean);
            }
        }
        return cvQuestionExplanation;
    }
    // Case# 3524: Add Explanation field to Questions - End

    //Added with case 4287 : Method to fetch the template data  - Start
    /**
     *  This method is used to fetch the questionnaire template
     *  from OSP$QUESTIONNAIRE.
     *  @param questionnaireId : The questionnaire Id.
     *  @param qnrVersionNumber : The questionnaire Version number.
     *  @return Questionnaire template as btye[].
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    // 4272: Maintain history of Questionnaires
//    public byte[] getQuestionnaireTemplate(int questionnaireId) throws CoeusException,DBException {
    public byte[] getQuestionnaireTemplate(int questionnaireId, int qnrVersionNumber) throws CoeusException,DBException {
        Vector result     = null;
        Vector param      = null;
        HashMap resultRow = null;

        if(dbEngine != null) {
            param = new Vector();
            param.add(new Parameter("AV_QUESTIONNAIRE_ID",DBEngineConstants.TYPE_INT,new Integer(questionnaireId)));
            // 4272: Maintain history of Questionnaires  - Start
            param.add(new Parameter("AV_VERSION_NUMBER",DBEngineConstants.TYPE_INT,new Integer(qnrVersionNumber)));

//            String selectQuery = "SELECT TEMPLATE FROM OSP$QUESTIONNAIRE "
//                    + " WHERE QUESTIONNAIRE_ID =  <<AV_QUESTIONNAIRE_ID>> ";
            String selectQuery = "SELECT TEMPLATE FROM OSP$QUESTIONNAIRE "
                    + " WHERE QUESTIONNAIRE_ID =  <<AV_QUESTIONNAIRE_ID>> AND VERSION_NUMBER = <<AV_VERSION_NUMBER>> ";
            // 4272: Maintain history of Questionnaires  - End
            result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",param);
            if( !result.isEmpty()) {
                resultRow = (HashMap)result.get(0);
                String template =Utils.convertNull((String)resultRow.get("TEMPLATE"));//clob returns string
                return template.getBytes();
            }

        }else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        return null;
    }
    //Case 4287 : Method to fetch the template data  - End
    // 4272: Maintain history of Questionnaires - Start
    public boolean checkQuestionUsedInQuestionnaire(int questionId, int versionNumber) throws DBException, CoeusException {

        boolean isQuestionUsed = true;
        Vector param = new Vector();
        Vector result = null;
        int count = -1;
        param.add(new Parameter("AS_QUESTION_ID",DBEngineConstants.TYPE_INT,
                new Integer(questionId)));
        param.addElement(new Parameter("AS_VERSION_NUMBER",DBEngineConstants.TYPE_INT,
                 new Integer(versionNumber)));

        if(dbEngine != null) {
            result = dbEngine.executeFunctions(DSN,
                    "{ <<OUT INTEGER COUNT>> = "
                    +" call FN_IS_QUESTION_USED_IN_QNR (<<AS_QUESTION_ID>>, <<AS_VERSION_NUMBER>> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            if(rowParameter.get("COUNT") != null){
                 rowParameter = (HashMap)result.elementAt(0);
                 count = Integer.parseInt(rowParameter.get("COUNT").toString());
            }
        }

        if(count == 0){
            isQuestionUsed = false;
        }

        return isQuestionUsed;
    }


    public boolean checkQuesionnaireAnswered(int questionnaireId) throws DBException, CoeusException {

        boolean isQuestionnaireAnswered = true;
        Vector param = new Vector();
        Vector result = null;
        int count = -1;
        param.add(new Parameter("AS_QUESTIONNAIRE_ID",DBEngineConstants.TYPE_INT,
                new Integer(questionnaireId)));

        if(dbEngine != null) {
            result = dbEngine.executeFunctions(DSN,
                    "{ <<OUT INTEGER COUNT>> = "
                    +" call FN_IS_QUESTIONNAIRE_ANSWERED(<<AS_QUESTIONNAIRE_ID>> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            if(rowParameter.get("COUNT") != null){
                 rowParameter = (HashMap)result.elementAt(0);
                 count = Integer.parseInt(rowParameter.get("COUNT").toString());
            }
        }

        if(count == 0){
            isQuestionnaireAnswered = false;
        }

        return isQuestionnaireAnswered;
    }


    public CoeusVector getLatestQuestionsForQuestionnaire(int questionnaireId) throws DBException, CoeusException {
        Vector param = new Vector();
        Vector result = null;
        CoeusVector questionnaireQuestionsData = null;
        QuestionnaireBean questionnaireBean = null ;
        param.add(new Parameter("AV_QUESTIONNAIRE_ID",DBEngineConstants.TYPE_INT,new Integer(questionnaireId)));

        if(dbEngine != null) {
            result = dbEngine.executeRequest(DSN,"call GET_LATEST_QUESTIONS_FOR_QNR (<< AV_QUESTIONNAIRE_ID >>, " +
                    "<< OUT RESULTSET rset >>)",DSN,param);

        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result != null && result.size() > 0) {
            HashMap rowData = null ;
            HashMap hmSeqQuestions = new HashMap();
            questionnaireQuestionsData = new CoeusVector();
            RulesTxnBean rulesTxnBean = new RulesTxnBean();
            for(int index = 0 ; index < result.size() ; index++){
                questionnaireBean = new QuestionnaireBean();
                rowData = (HashMap)result.get(index);
                questionnaireBean.setQuestionnaireId(rowData.get("QUESTIONNAIRE_ID")== null ? 0 :
                    (Integer.parseInt(rowData.get("QUESTIONNAIRE_ID").toString())));

                questionnaireBean.setQuestionnaireVersionNumber(rowData.get("QUESTIONNAIRE_VERSION_NUMBER")== null ? 0 :
                    (Integer.parseInt(rowData.get("QUESTIONNAIRE_VERSION_NUMBER").toString())));
                questionnaireBean.setQuestionVersionNumber(rowData.get("VERSION_NUMBER")== null ? new Integer(0) :
                    (new Integer(rowData.get("VERSION_NUMBER").toString())));

                questionnaireBean.setQuestionId(rowData.get("QUESTION_ID")== null ? null :
                    new Integer(rowData.get("QUESTION_ID").toString()));
                // 4272: Maintain History of Questionnaires - Start
                questionnaireBean.setQuestionStatus(rowData.get("STATUS") == null ?
                    false : rowData.get("STATUS").toString().equalsIgnoreCase("A") ? true : false);
                // 4272: Maintain History of Questionnaires - End
                questionnaireBean.setDescription((String)rowData.get("QUESTION_DESC"));
                questionnaireBean.setQuestionNumber(rowData.get("QUESTION_NUMBER") == null ? null :
                    new Integer(rowData.get("QUESTION_NUMBER").toString()));
                questionnaireBean.setParentQuestionNumber(rowData.get("PARENT_QUESTION_NUMBER") == null ? null :
                    new Integer(rowData.get("PARENT_QUESTION_NUMBER").toString()));
                questionnaireBean.setConditionalFlag(rowData.get("CONDITION_FLAG") == null ?
                    false : rowData.get("CONDITION_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
                questionnaireBean.setConditionValue(rowData.get("CONDITION_VALUE")
                == null ? "" :(String)rowData.get("CONDITION_VALUE"));
                questionnaireBean.setCondition(rowData.get("CONDITION") == null
                        ? "" : (String)rowData.get("CONDITION"));
                questionnaireBean.setUpdateTimestamp((Timestamp)rowData.get("UPDATE_TIMESTAMP"));
                questionnaireBean.setUpdateUser((String)rowData.get("UPDATE_USER"));

                if(rowData.get("QUESTION_SEQ_NUMBER") == null){
                    if(!hmSeqQuestions.containsKey(questionnaireBean.getParentQuestionNumber())){
                        hmSeqQuestions.put(questionnaireBean.getParentQuestionNumber(), new BigDecimal(0));
                    }
                    int seqNum = ((BigDecimal) hmSeqQuestions.get(questionnaireBean.getParentQuestionNumber())).intValue();
                    questionnaireBean.setQuestionSequenceNumber(++seqNum);
                    hmSeqQuestions.put(questionnaireBean.getParentQuestionNumber(), new BigDecimal(seqNum));
                    questionnaireBean.setAcType("U");
                } else {
                    int seqNum = ((BigDecimal)rowData.get("QUESTION_SEQ_NUMBER")).intValue();
                    hmSeqQuestions.put(questionnaireBean.getParentQuestionNumber(), new BigDecimal(seqNum));
                    questionnaireBean.setQuestionSequenceNumber(seqNum);
                }
                // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
                if(rowData.get("RULE_ID") != null){
                    questionnaireBean.setConditionRuleId(Integer.parseInt(rowData.get("RULE_ID").toString()));
                    questionnaireBean.setRuleSelectionFlag(true);
                    if(questionnaireBean.getConditionRuleId() != 0){
                        CoeusVector cvRuleDetails = rulesTxnBean.getRuleDetails(questionnaireBean.getConditionRuleId());
                        if(cvRuleDetails != null && !cvRuleDetails.isEmpty()){
                            BusinessRuleBean businessRuleBean = (BusinessRuleBean)cvRuleDetails.get(0);
                            questionnaireBean.setConditionRuleDesc(businessRuleBean.getDescription());
                        }
                        
                    }
                }
                // Added for COEUSQA-3287 Questionnaire Maintenance Features - End
                questionnaireQuestionsData.addElement(questionnaireBean);
            }
        }
        return questionnaireQuestionsData;
    }


    public int fetchAnsweredVersionNumberOfQuestionnaire(QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean) throws DBException, CoeusException {
        int answeredVersion = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        dbEngine = new DBEngineImpl();

        param.add(new Parameter("AV_QUESTIONNAIRE_ID",DBEngineConstants.TYPE_INT,""+questionnaireAnswerHeaderBean.getQuestionnaireId()));
        param.add(new Parameter("AV_MODULE_ITEM_CODE",DBEngineConstants.TYPE_STRING,""+questionnaireAnswerHeaderBean.getModuleItemCode()));
        // Modified with CoeusQA2313: Completion of Questionnaire for Submission
        // applicable modulecode/itemkey/subitemkey will be same as actual modulecode/itemkey/subitemkey in normal scenarios.
        // For amendment/renewals, this can vary based on whether is answer is present for amendment or original protocol
        param.add(new Parameter("AV_MODULE_ITEM_KEY",DBEngineConstants.TYPE_STRING,""+questionnaireAnswerHeaderBean.getApplicableModuleItemKey()));


        /*ppc */
     if(questionnaireAnswerHeaderBean.getModuleItemCode()==3 && questionnaireAnswerHeaderBean.getModuleSubItemCode()==6)
         {

      param.add(new Parameter("AV_MODULE_SUB_ITEM_KEY",DBEngineConstants.TYPE_STRING,""+questionnaireAnswerHeaderBean.getApplicableModuleSubItemKeyForPpc()));
      param.add(new Parameter("AV_MODULE_SUB_ITEM_CODE",DBEngineConstants.TYPE_INT,""+6));
     }
       else
       {
        param.add(new Parameter("AV_MODULE_SUB_ITEM_KEY",DBEngineConstants.TYPE_INT,""+questionnaireAnswerHeaderBean.getApplicableModuleSubItemKey()));
        param.add(new Parameter("AV_MODULE_SUB_ITEM_CODE",DBEngineConstants.TYPE_INT,""+questionnaireAnswerHeaderBean.getApplicableSubmoduleCode()));
       }


        // CoeusQA2313: Completion of Questionnaire for Submission - End
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER VERSION_NUMBER>> = call FN_GET_VER_NO_OF_ANSWERED_QNR (<< AV_MODULE_ITEM_CODE >>,<< AV_MODULE_ITEM_KEY >>," +
                        "<< AV_MODULE_SUB_ITEM_CODE >>,<< AV_MODULE_SUB_ITEM_KEY >>,<< AV_QUESTIONNAIRE_ID >>)}",
                    param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            answeredVersion = Integer.parseInt(rowParameter.get("VERSION_NUMBER").toString());
        }
        return answeredVersion;
    }
///////////////////////ppc new //////////////////////////////////////////////////
       public int fetchAnsweredVersionNumber(int qId,String itemKey,String subItemKey) throws DBException, CoeusException {
        int answeredVersion = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        dbEngine = new DBEngineImpl();

        param.add(new Parameter("AV_QUESTIONNAIRE_ID",DBEngineConstants.TYPE_INT,""+qId));
        param.add(new Parameter("AV_MODULE_ITEM_CODE",DBEngineConstants.TYPE_STRING,""+3));
        param.add(new Parameter("AV_MODULE_SUB_ITEM_CODE",DBEngineConstants.TYPE_INT,""+6));
        // Modified with CoeusQA2313: Completion of Questionnaire for Submission
        // applicable modulecode/itemkey/subitemkey will be same as actual modulecode/itemkey/subitemkey in normal scenarios.
        // For amendment/renewals, this can vary based on whether is answer is present for amendment or original protocol
        param.add(new Parameter("AV_MODULE_ITEM_KEY",DBEngineConstants.TYPE_STRING,""+itemKey));

        param.add(new Parameter("AV_MODULE_SUB_ITEM_KEY",DBEngineConstants.TYPE_STRING,""+subItemKey));




        // CoeusQA2313: Completion of Questionnaire for Submission - End
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER VERSION_NUMBER>> = call FN_GET_VER_NO_OF_ANSWERED_QNR (<< AV_MODULE_ITEM_CODE >>,<< AV_MODULE_ITEM_KEY >>," +
                        "<< AV_MODULE_SUB_ITEM_CODE >>,<< AV_MODULE_SUB_ITEM_KEY >>,<< AV_QUESTIONNAIRE_ID >>)}",
                    param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            answeredVersion = Integer.parseInt(rowParameter.get("VERSION_NUMBER").toString());
        }
        return answeredVersion;
    }





    public int fetchMaxVersionNumberOfQuestionnaire(int questionnaireId) throws DBException, CoeusException {
        int version = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        dbEngine = new DBEngineImpl();

        param.add(new Parameter("AV_QUESTIONNAIRE_ID",DBEngineConstants.TYPE_INT,new Integer(questionnaireId)));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER VERSION_NUMBER>> = call FN_GET_VER_NO_OF_FINAL_QNR (<< AV_QUESTIONNAIRE_ID >>)}",
                    param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            version = Integer.parseInt(rowParameter.get("VERSION_NUMBER").toString());
        }
        return version;
    }

    public int fetchMaxVersionNumberOfQuestion(int questionId) throws DBException, CoeusException {
        int version = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        dbEngine = new DBEngineImpl();

        param.add(new Parameter("AV_QUESTION_ID",DBEngineConstants.TYPE_INT,new Integer(questionId)));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER VERSION_NUMBER>> = call FN_GET_MAX_QUESTION_VERSION_NO (<< AV_QUESTION_ID >>)}",
                    param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            version = Integer.parseInt(rowParameter.get("VERSION_NUMBER").toString());
        }
        return version;
    }

    public boolean deleteQuestionnaireVersion(QuestionnaireBaseBean questionnaireBaseBean) throws DBException, CoeusException {
        boolean deleted = false;
        int success = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        dbEngine = new DBEngineImpl();

        param.add(new Parameter("AV_QUESTIONNAIRE_ID",DBEngineConstants.TYPE_INT,""+questionnaireBaseBean.getQuestionnaireNumber()));
        param.add(new Parameter("AV_VERSION_NUMBER",DBEngineConstants.TYPE_INT,""+questionnaireBaseBean.getQuestionnaireVersionNumber()));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER DELETED>> = call FN_DELETE_QUESTIONNAIRE (<< AV_QUESTIONNAIRE_ID >> , << AV_VERSION_NUMBER >>)}",
                    param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            success = Integer.parseInt(rowParameter.get("DELETED").toString());
        }

        if(success == 1){
           deleted = true;
        }
        return deleted;
    }

     // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire
//    public int fetchQuestionnaireCompletedForModule(String moduleItemKey, int moduleCode, String subModuleCode, int moduleItemSeqNumber) throws DBException, CoeusException {
     public Vector fetchQuestionnaireCompletedForModule(String moduleItemKey, int moduleCode, String subModuleCode, int moduleItemSeqNumber) throws DBException, CoeusException {
        int success = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        dbEngine = new DBEngineImpl();
        String mandatoryQnr = "";
        String incompleteQnr = "";
        String newVersionQnr = "";
        Vector vecQnrList = new Vector();

        param.add(new Parameter("AS_MODULE_ITEM_CODE",DBEngineConstants.TYPE_INT,""+moduleCode));
        param.add(new Parameter("AS_MODULE_ITEM_KEY",DBEngineConstants.TYPE_STRING,moduleItemKey));
        param.add(new Parameter("AS_MODULE_SUB_ITEM_CODE",DBEngineConstants.TYPE_INT,subModuleCode));
        param.add(new Parameter("AS_MODULE_SUB_ITEM_KEY",DBEngineConstants.TYPE_INT,""+moduleItemSeqNumber));
        //Added userId param with COEUSDEV 325-Ability to route a protocol to PI if the person submitting the protocol IS NOT the PI
        param.add(new Parameter("AS_USER",DBEngineConstants.TYPE_STRING,userId));
        // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - Start
        param.addElement(new Parameter("MANDATORY_QNR_LIST",
                DBEngineConstants.TYPE_STRING, null, "out"));
        param.addElement(new Parameter("INCOMPLETE_QNR_LIST",
                DBEngineConstants.TYPE_STRING, null, "out"));
        param.addElement(new Parameter("NEW_VERSION_QNR_LIST",
                DBEngineConstants.TYPE_STRING, null, "out"));
        // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - End
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER SUCCESS>> = call FN_IS_QNR_COMPLETE_IN_MODULE(<< AS_MODULE_ITEM_CODE >> , << AS_MODULE_ITEM_KEY >>," +
                    // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - Start
//                    "<< AS_MODULE_SUB_ITEM_CODE >>, << AS_MODULE_SUB_ITEM_KEY >>)}",
                    "<< AS_MODULE_SUB_ITEM_CODE >>, << AS_MODULE_SUB_ITEM_KEY >>, <<AS_USER>> , << MANDATORY_QNR_LIST >>, << INCOMPLETE_QNR_LIST >>, << NEW_VERSION_QNR_LIST >>)}",
                    // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - End
                    param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            success = Integer.parseInt(rowParameter.get("SUCCESS").toString());
            // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - Start
            mandatoryQnr = (String)rowParameter.get("MANDATORY_QNR_LIST");;
            incompleteQnr = (String)rowParameter.get("INCOMPLETE_QNR_LIST");;
            newVersionQnr = (String)rowParameter.get("NEW_VERSION_QNR_LIST");
            vecQnrList.addElement(mandatoryQnr);
            vecQnrList.addElement(incompleteQnr);
            vecQnrList.addElement(newVersionQnr);
        }
//        return success;
        return vecQnrList;
        // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - End
    }
    // 4272: Maintain history of Questionnaires  - End

     // COEUSDEV-230: Answered questionnaire says it is not Answered in Approval in Progress Proposal
     /**
      * This method fetches all the questionnaires applicable for a module.
      * This fetches both rule based questionnaires and questionnaires which doesnt have any
      * rules applicable for a particular module.
      */

     public CoeusVector fetchApplicableQuestionnairedForModule(QuestionnaireAnswerHeaderBean questionnaireModuleObject) throws DBException, CoeusException {
         Vector param = new Vector();
         Vector result = null;
         CoeusVector cvQnrList = new CoeusVector();

         if(questionnaireModuleObject.getModuleItemKey() == null || "".equals(questionnaireModuleObject.getModuleItemKey())){
             questionnaireModuleObject.setModuleItemKey("0");
         }

         if(questionnaireModuleObject.getModuleSubItemKey() == null || "".equals(questionnaireModuleObject.getModuleSubItemKey())){
             questionnaireModuleObject.setModuleSubItemKey("0");
         }

         param.add(new Parameter("AV_MODULE_ITEM_CODE",DBEngineConstants.TYPE_INT,questionnaireModuleObject.getModuleItemCode()));
         param.add(new Parameter("AV_MODULE_SUB_ITEM_CODE",DBEngineConstants.TYPE_INT,questionnaireModuleObject.getModuleSubItemCode()));
         param.add(new Parameter("AV_MODULE_ITEM_KEY",DBEngineConstants.TYPE_STRING, questionnaireModuleObject.getModuleItemKey()));

         //Added userId param with COEUSDEV 325-Ability to route a protocol to PI if the person submitting the protocol IS NOT the PI
          if(questionnaireModuleObject.getModuleItemCode()==3 && questionnaireModuleObject.getModuleSubItemCode()==6)
         {

               param.add(new Parameter("AV_MODULE_SUB_ITEM_KEY",DBEngineConstants.TYPE_STRING,questionnaireModuleObject.getModuleSubItemKey()));
              param.add(new Parameter("AV_USER",DBEngineConstants.TYPE_STRING,questionnaireModuleObject.getCurrentUser()));
              param.add(new Parameter("AV_PERSONID",DBEngineConstants.TYPE_STRING,questionnaireModuleObject.getCurrentPersonId()));
         if(dbEngine != null) {
             result = dbEngine.executeRequest(DSN,"call GET_QNIR_FOR_PROP_PERSON(<< AV_MODULE_ITEM_CODE >>,<< AV_MODULE_SUB_ITEM_CODE >>,<< AV_MODULE_ITEM_KEY >>,<< AV_MODULE_SUB_ITEM_KEY >>, <<AV_USER>>,<<AV_PERSONID>>, " +
                     "<< OUT RESULTSET rset >>)",DSN,param);
         }

         }
          else if(questionnaireModuleObject.getModuleItemCode()==8) {
              param.add(new Parameter("AV_MODULE_SUB_ITEM_KEY",DBEngineConstants.TYPE_INT,questionnaireModuleObject.getModuleSubItemKey()));
               param.add(new Parameter("AV_USER",DBEngineConstants.TYPE_STRING,questionnaireModuleObject.getCurrentPersonId()));
               result = dbEngine.executeRequest(DSN,"call GET_QUESTIONNAIRE_APPLICABLE(<< AV_MODULE_ITEM_CODE >>,<< AV_MODULE_SUB_ITEM_CODE >>,<< AV_MODULE_ITEM_KEY >>,<< AV_MODULE_SUB_ITEM_KEY >>, <<AV_USER>>, " +
                     "<< OUT RESULTSET rset >>)",DSN,param);
          }
         else {
              param.add(new Parameter("AV_MODULE_SUB_ITEM_KEY",DBEngineConstants.TYPE_INT,questionnaireModuleObject.getModuleSubItemKey()));
               param.add(new Parameter("AV_USER",DBEngineConstants.TYPE_STRING,userId));

              result = dbEngine.executeRequest(DSN,"call GET_QUESTIONNAIRE_APPLICABLE(<< AV_MODULE_ITEM_CODE >>,<< AV_MODULE_SUB_ITEM_CODE >>,<< AV_MODULE_ITEM_KEY >>,<< AV_MODULE_SUB_ITEM_KEY >>, <<AV_USER>>, " +
                     "<< OUT RESULTSET rset >>)",DSN,param);
           //  throw new CoeusException("db_exceptionCode.1000");
         }




         if(result != null && result.size() > 0) {
             HashMap rowData = null ;

             for(int index = 0 ; index < result.size() ; index++){
                 QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean = new QuestionnaireAnswerHeaderBean();
                 rowData = (HashMap)result.get(index);
                 questionnaireAnswerHeaderBean.setQuestionnaireId(rowData.get("QUESTIONNAIRE_ID")== null ? 0 :
                     (Integer.parseInt(rowData.get("QUESTIONNAIRE_ID").toString())));
                 questionnaireAnswerHeaderBean.setQuestionnaireVersionNumber(rowData.get("VERSION_NUMBER")== null ? 0 :
                     (Integer.parseInt(rowData.get("VERSION_NUMBER").toString())));
                 questionnaireAnswerHeaderBean.setLabel((String) rowData.get("QUESTIONNAIRE_LABEL"));
                 // Added with CoeusQA2313: Completion of Questionnaire for Submission
                 questionnaireAnswerHeaderBean.setModuleItemCode(questionnaireModuleObject.getModuleItemCode());
                 questionnaireAnswerHeaderBean.setModuleSubItemCode(questionnaireModuleObject.getModuleSubItemCode());


                 questionnaireAnswerHeaderBean.setApplicableSubmoduleCode(rowData.get("MODULE_SUB_ITEM_CODE")== null ? 0 :
                     (Integer.parseInt(rowData.get("MODULE_SUB_ITEM_CODE").toString())));

                 // CoeusQA2313: Completion of Questionnaire for Submission - End
                 cvQnrList.add(questionnaireAnswerHeaderBean);
             }

         }
         return cvQnrList;
     }
     // COEUSDEV-230: Answered questionnaire says it is not Answered in Approval in Progress Proposal - End

     //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
     /*
      * Method to check any question is answered in the module
      * @param moduleItemCode
      * @param moduleItemKey
      * @param moduleSubItemCode
      * @param moduleSubItemKey
      * @return isAnyQuestionsAnswered
      */
     public boolean checkAnyQuestionIsAnsweredInModule(int moduleItemCode,
             String moduleItemKey, int moduleSubItemCode, String moduleSubItemKey) throws DBException, CoeusException {
         boolean isAnyQuestionsAnswered = true;
         Vector param = new Vector();
         Vector result = null;
         int count = -1;
         param.add(new Parameter("AV_MODULE_ITEM_CODE",DBEngineConstants.TYPE_INT,moduleItemCode));
         param.add(new Parameter("AV_MODULE_ITEM_KEY",DBEngineConstants.TYPE_STRING,moduleItemKey));
         param.add(new Parameter("AV_MODULE_SUB_ITEM_CODE",DBEngineConstants.TYPE_INT,moduleSubItemCode));
         param.add(new Parameter("AV_MODULE_SUB_ITEM_KEY",DBEngineConstants.TYPE_STRING,moduleSubItemKey));
                 if(dbEngine != null) {
             result = dbEngine.executeFunctions(DSN,
                      "{ <<OUT INTEGER COUNT>> = "
                     +" call FN_IS_ANY_QNR_ANSWERED_FOR_MOD(<<AV_MODULE_ITEM_CODE >>, <<AV_MODULE_ITEM_KEY>>, <<AV_MODULE_SUB_ITEM_CODE>>, <<AV_MODULE_SUB_ITEM_KEY>>) }", param);
         }else{
             throw new CoeusException("db_exceptionCode.1000");
         }
         if(!result.isEmpty()){
             HashMap rowParameter = (HashMap)result.elementAt(0);
             if(rowParameter.get("COUNT") != null){
                 rowParameter = (HashMap)result.elementAt(0);
                 count = Integer.parseInt(rowParameter.get("COUNT").toString());
             }
         }

         if(count == 0){
             isAnyQuestionsAnswered = false;
         }

         return isAnyQuestionsAnswered;
     }
     //COEUSDEV-86 : End

     // COEUSQA-2321: Copy Questionnaires for Proposal Development records - Start
     public boolean checkCanCopyQuestionnaireFromModule(int moduleCode, int subModuleCode, String moduleItemKey, String moduleSubItemKey ){
         boolean canCopy = false;
         if(moduleCode == 3){
             canCopy = checkCanCopyQuestionnaireFromproposal(moduleItemKey);
         }
         //COEUSQA:3503 - Protocol Copying - Option to Copy Attachments and Questionnaires - Start
         else if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE){
             canCopy = checkCanCopyQuestionnaireFromprotocol(moduleCode, moduleItemKey, moduleSubItemKey);
         }
         else if(moduleCode == ModuleConstants.IACUC_MODULE_CODE){
             canCopy = checkCanCopyQuestionnaireFromprotocol(moduleCode, moduleItemKey, moduleSubItemKey);
         }
         //COEUSQA:3503 - End
         return canCopy;
     }

     private boolean checkCanCopyQuestionnaireFromproposal(String proposalNumber) {
         boolean canCopy = false;

         Vector param = new Vector();
         Vector result = null;
         Vector vecQnrList = new Vector();
         int copy = -1;
         try {
             param.add(new Parameter("AS_MODULE_CODE",DBEngineConstants.TYPE_INT, 3));
             param.add(new Parameter("AS_MODULE_ITEM_KEY",DBEngineConstants.TYPE_STRING, proposalNumber));
             param.add(new Parameter("AS_MODULE_SUB_ITEM_CODE",DBEngineConstants.TYPE_INT, 0));
             param.add(new Parameter("AS_MODULE_SUB_ITEM_KEY",DBEngineConstants.TYPE_STRING, "0"));
             param.add(new Parameter("AS_UPDATE_USER",DBEngineConstants.TYPE_STRING, userId));

             param.addElement(new Parameter("AS_INCOMPLETE_QNR_LIST",
                     DBEngineConstants.TYPE_STRING, null, "out"));
             param.addElement(new Parameter("AS_NEW_VERSION_QNR_LIST",
                     DBEngineConstants.TYPE_STRING, null, "out"));

             if(dbEngine != null) {
                 result = dbEngine.executeFunctions(DSN,
                         "{ <<OUT INTEGER COUNT>> = "
                         +" call FN_CAN_COPY_MODULE_QNR_ANS (<< AS_MODULE_CODE >> , << AS_MODULE_ITEM_KEY >>, << AS_MODULE_SUB_ITEM_CODE >>, " +
                         "<< AS_MODULE_SUB_ITEM_KEY >> , << AS_UPDATE_USER >>, << AS_INCOMPLETE_QNR_LIST >>, << AS_NEW_VERSION_QNR_LIST >>) }",
                         param);
             }else{
                 throw new CoeusException("db_exceptionCode.1000");
             }
             if(!result.isEmpty()){
                 HashMap rowParameter = (HashMap)result.elementAt(0);
                 if(rowParameter.get("COUNT") != null){
                     rowParameter = (HashMap)result.elementAt(0);
                     copy = Integer.parseInt(rowParameter.get("COUNT").toString());

                     String incompleteQnrList = (String)rowParameter.get("AS_INCOMPLETE_QNR_LIST");;
                     String newVersionQnrList = (String)rowParameter.get("AS_NEW_VERSION_QNR_LIST");

                 }
             }
             //COEUSQA-2321: Copy Questionnaires for Proposal Development records.
             // Return "5" if business rules return false.
             if(copy != 4 && copy != 3 && copy != 5){
                 canCopy = true;
             }
         } catch (Exception ex) {
             UtilFactory.log(ex.getMessage());
         }
         return canCopy;
     }
     // COEUSQA-2321: Copy Questionnaires for Proposal Development records - End
      //Added for COEUSQA-1682_FS_Proposal Development Schema Infrastructure start.
     public List getQuestionnaireAnswers(String proposalNumber, int questionnaireID, int module_code)
     throws CoeusException , DBException{

         Vector param = new Vector();

         param.addElement( new Parameter("MODULE_ITEM_KEY",
                 DBEngineConstants.TYPE_STRING, proposalNumber));
         param.addElement(new Parameter("QUESTIONNAIRE_ID",
                 DBEngineConstants.TYPE_INT, ( Integer.toString(questionnaireID))));
         param.addElement(new Parameter("MODULE_ITEM_CODE",
                 DBEngineConstants.TYPE_INT, ( Integer.toString(module_code))));
         //HashMap row = null;
         Vector result = new Vector();

         if(dbEngine !=null){
             result = dbEngine.executeRequest("Coeus",
                     "call  get_ans_for_a_questionnaire" +
                     " ( <<MODULE_ITEM_KEY>> , <<QUESTIONNAIRE_ID>> , <<MODULE_ITEM_CODE>>, " +
                     " <<OUT RESULTSET rset>> )",  "Coeus", param);

         }else{
         throw new CoeusException("db_exceptionCode.1000");
         }

         Vector questionnaireAnswerList = new Vector();

         int vecSize = result.size();
         if (vecSize >0){
             for(int index=0; index<vecSize; index++){

                 HashMap rowData = (HashMap)result.get(index);

                 QuestionAnswerBean answerBean = new QuestionAnswerBean();
                 answerBean.setQuestionId(new Integer(rowData.get("QUESTION_ID").toString()));
                 answerBean.setQuestionNumber(Integer.parseInt(rowData.get("QUESTION_NUMBER").toString()));
                 answerBean.setAnswerNumber(Integer.parseInt(rowData.get("ANSWER_NUMBER").toString()));
                 answerBean.setAnswer(rowData.get("ANSWER") == null ? "" : (String)rowData.get("ANSWER")) ;
                 answerBean.setAwAnswer(rowData.get("ANSWER") == null ? "" : (String)rowData.get("ANSWER")) ;
                 questionnaireAnswerList.addElement(answerBean);
             }
         }

         return questionnaireAnswerList;
     }
     //Added for COEUSQA-1682_FS_Proposal Development Schema Infrastructure end.
     
 //Added COEUSQA-3287 Questionnaire Maintenance Features - End
    /**
     * This method get question groups from the database table OSP$QUESTIONNAIRE_GROUP_TYPES
     * using the stored procedure get_questionnaire_group_types
     * @return CoeusVector
     * @throws CoeusException
     * @throws DBException
     */
     public CoeusVector getQuestionnaireGroups() throws DBException{
         
         Vector result = new Vector();
         CoeusVector vecQuestionnaireGroups = null;
         HashMap hmQuestionGroups = null;
         Vector param = new Vector();
         
         if(dbEngine!=null){
             result = dbEngine.executeRequest("Coeus",
                     "call GET_QUESTIONNAIRE_GROUP_TYPES ( <<OUT RESULTSET rset>> )",
                     "Coeus", param);
         }else{
             throw new DBException("DB instance is not available");
         }
         int count = result.size();
         if (count >0){
             vecQuestionnaireGroups = new CoeusVector();
             ComboBoxBean comboBoxBean = new ComboBoxBean();
             comboBoxBean.setCode("");
             //Commented and Added for COEUSQA-3511 : Questionnaire Group List will not return to show all once a group list is selected - start
             //instead of blank need to name it as 'All' and which should list out all the questionarries.
             //comboBoxBean.setDescription("");
             comboBoxBean.setDescription("ALL");
             //Added for COEUSQA-3511 : Questionnaire Group List will not return to show all once a group list is selected - end
             vecQuestionnaireGroups.addElement(comboBoxBean);
             for(int index = 0; index < count; index++){
                 hmQuestionGroups = (HashMap)result.elementAt(index);
                 vecQuestionnaireGroups.addElement(new ComboBoxBean(
                         hmQuestionGroups.get("GROUP_TYPE_CODE").toString(),
                         hmQuestionGroups.get("GROUP_NAME").toString()));
             }
         }
         return vecQuestionnaireGroups;
     }
    //Added COEUSQA-3287 Questionnaire Maintenance Features - End
     
      //COEUSQA:3503 - Protocol Copying - Option to Copy Attachments and Questionnaires - Start
    /**
     * Method to check whether the questionnaires can be copied
     *
     * @param moduleCode 
     * @param protocolNumber 
     * @param sequenceNumber 
     * @return 
     */
    public boolean checkCanCopyQuestionnaireFromprotocol(int moduleCode, String protocolNumber, String sequenceNumber) {
         boolean canCopy = false;

         Vector param = new Vector();
         Vector result = null;
         Vector vecQnrList = new Vector();
         int copy = -1;
         try {
             param.add(new Parameter("AS_MODULE_CODE",DBEngineConstants.TYPE_INT, moduleCode));
             param.add(new Parameter("AS_MODULE_ITEM_KEY",DBEngineConstants.TYPE_STRING, protocolNumber));
             param.add(new Parameter("AS_MODULE_SUB_ITEM_CODE",DBEngineConstants.TYPE_INT, 0));
             param.add(new Parameter("AS_MODULE_SUB_ITEM_KEY",DBEngineConstants.TYPE_STRING, sequenceNumber));
             param.add(new Parameter("AS_UPDATE_USER",DBEngineConstants.TYPE_STRING, userId));

             param.addElement(new Parameter("AS_INCOMPLETE_QNR_LIST",
                     DBEngineConstants.TYPE_STRING, null, "out"));
             param.addElement(new Parameter("AS_NEW_VERSION_QNR_LIST",
                     DBEngineConstants.TYPE_STRING, null, "out"));

             if(dbEngine != null) {
                 result = dbEngine.executeFunctions(DSN,
                         "{ <<OUT INTEGER COUNT>> = "
                         +" call FN_CAN_COPY_MODULE_QNR_ANS (<< AS_MODULE_CODE >> , << AS_MODULE_ITEM_KEY >>, << AS_MODULE_SUB_ITEM_CODE >>, " +
                         "<< AS_MODULE_SUB_ITEM_KEY >> , << AS_UPDATE_USER >>, << AS_INCOMPLETE_QNR_LIST >>, << AS_NEW_VERSION_QNR_LIST >>) }",
                         param);
             }else{
                 throw new CoeusException("db_exceptionCode.1000");
             }
             if(!result.isEmpty()){
                 HashMap rowParameter = (HashMap)result.elementAt(0);
                 if(rowParameter.get("COUNT") != null){
                     rowParameter = (HashMap)result.elementAt(0);
                     copy = Integer.parseInt(rowParameter.get("COUNT").toString());

                         String incompleteQnrList = (String)rowParameter.get("AS_INCOMPLETE_QNR_LIST");;
                     String newVersionQnrList = (String)rowParameter.get("AS_NEW_VERSION_QNR_LIST");

                 }
             }
             if(copy != 4 && copy != 3 && copy != 5){
                 canCopy = true;
             }
         } catch (Exception ex) {
             UtilFactory.log(ex.getMessage());
         }
         return canCopy;
     }
    //COEUSQA:3503 - End
    
    /*Add Name of Person Certifying to Printout - START*/    

     /**
     * Method to get the Person's fullname
     *
     * @param personId
     * @return String
     **/
    public String getPersonFullName(String personId) {
        String personName ="";
        Vector param = new Vector();
        Vector result = null;
        try{
            param.add(new Parameter("PERSON_ID",DBEngineConstants.TYPE_STRING, personId));
            if (dbEngine != null) {
                result = dbEngine.executeRequest("Coeus",
                        "call dw_get_person ( <<PERSON_ID>> , <<OUT RESULTSET rset>> ) ",
                        "Coeus", param);
            } else {
                throw new CoeusException("db_exceptionCode.1000");
            }

            if(result != null && !result.isEmpty()) {
                HashMap rowParameter = (HashMap) result.elementAt(0);
                if(rowParameter.get("FULL_NAME") != null) {
                    personName = (String) rowParameter.get("FULL_NAME");
                }
            }
        } catch (Exception ex) {
             UtilFactory.log(ex.getMessage());
        }
        return personName;
    }

    /* Add Name of Person Certifying to Printout - END*/
}
