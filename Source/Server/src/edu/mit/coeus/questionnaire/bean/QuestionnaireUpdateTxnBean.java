/*
 * @(#)QuestionnaireUpdateTxnBean.java September 25, 2006, 12:10 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * QuestionnaireUpdateTxnBean.java
 *
 * Created on September 25, 2006, 12:10 PM
 */

package edu.mit.coeus.questionnaire.bean;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.NotEquals;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Vector;
import org.omg.PortableServer.REQUEST_PROCESSING_POLICY_ID;

/**
 *
 * @author  chandrashekara
 */
public class QuestionnaireUpdateTxnBean {
     private DBEngineImpl dbEngine;

    private static final String DSN = "Coeus";

    private Timestamp dbTimestamp;

    // holds the userId for the logged in user
    private String userId;

    private TransactionMonitor transMon;

    //Code added for coeus4.3 Questionnaire enhancement case#2946
    private QuestionnaireTxnBean questionnaireTxnBean;

    public String QuestionAnsFlag = "false";



    /** Creates a new instance of QuestionnaireUpdateTxnBean */
   /** Creates a new instance of QuestionnaireTxnBean */
    public QuestionnaireUpdateTxnBean() {
        dbEngine = new DBEngineImpl();
    }

    public QuestionnaireUpdateTxnBean(String userId) throws DBException {
        this();
        this.userId = userId;
        transMon = TransactionMonitor.getInstance();
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
    }



    /** This method will update the Business Rules, Meta Rules and Metarule Details
      *together.
      *@param HashMap contains the vector of BusinessRulesBean, Vector of MetaRuleBean
      *Vector of MataRuleDetailBean.
      *@throws CoeusException,DBException
      */
     public boolean addUpdDelQuestionnaireDetails(HashMap hmClinetData) throws
     CoeusException, DBException{
         boolean success = false;
         Vector procedures = new Vector(5,3);
         CoeusVector cvQuestionnaireUsageData = null;
         CoeusVector cvQuestionnaireQuestionsData = null;
         CoeusVector cvQuestionnaireData = null;
         //4287
         Vector vctUpdTemplateProcedures = new Vector();
         //4287 End
         cvQuestionnaireUsageData = (CoeusVector)hmClinetData.get(QuestionnaireUsageBean.class);
         cvQuestionnaireQuestionsData = (CoeusVector)hmClinetData.get(QuestionnaireBean.class);
         cvQuestionnaireData = (CoeusVector)hmClinetData.get(QuestionnaireBaseBean.class);

         // Update QuestionnaireData data
         if(cvQuestionnaireData!= null && cvQuestionnaireData.size() > 0){
                 QuestionnaireBaseBean questionnaireBaseBean = (QuestionnaireBaseBean)cvQuestionnaireData.elementAt(0);
                 if(questionnaireBaseBean!= null && questionnaireBaseBean.getAcType() != null){
                     // // 4272: Maintain history of Questionnaires
                     int questionnaireVersionNumber = questionnaireBaseBean.getQuestionnaireVersionNumber();
                     procedures.add(addUpdQuestionnaire(questionnaireBaseBean));
                     //Added with case 4287 : Questionnaire Templates - Start
                     QuestionnaireTemplateBean templateBean = questionnaireBaseBean.getQuestionnaireTemplateBean();
                     if(templateBean!=null && (TypeConstants.UPDATE_RECORD).equals(templateBean.getAcType())){
                         templateBean.setQuestionnaireNumber(questionnaireBaseBean.getQuestionnaireId());
                         // 4272: Maintain history of Questionnaires - Start
//                         vctUpdTemplateProcedures.add(updateQuestionnaireTemplate(templateBean));
                         vctUpdTemplateProcedures.add(updateQuestionnaireTemplate(templateBean, questionnaireVersionNumber));
                         // 4272: Maintain history of Questionnaires - End
                     }
                     //4287 end
                 }
         }
         //Modified for COEUSDEV-86 - Questionnaire for Submission - Start
         //Deletes all the data and insert the same datas
//         if(cvQuestionnaireUsageData!= null && cvQuestionnaireUsageData.size() > 0){
//             for(int count=0;count < cvQuestionnaireUsageData.size();count++){
//                 QuestionnaireUsageBean questionnaireUsageBean = (QuestionnaireUsageBean)cvQuestionnaireUsageData.elementAt(count);
//                 if(questionnaireUsageBean.getAcType() == null){
//                     continue;
//                 }
//                 if(questionnaireUsageBean!= null && questionnaireUsageBean.getAcType() != null){
//                     procedures.add(addUpdQuestionnaireUsage(questionnaireUsageBean));
//                 }
//             }
//         }
         if(cvQuestionnaireUsageData!= null && cvQuestionnaireUsageData.size() > 0){
             Vector vcIndexToDeleteUsages = new Vector();
             for(int usageIndex=0;usageIndex < cvQuestionnaireUsageData.size();usageIndex++){
                 boolean deleteUsagePermenantly = false;
                 QuestionnaireUsageBean questionnaireUsageBean =
                         (QuestionnaireUsageBean)cvQuestionnaireUsageData.elementAt(usageIndex);
                 String acType = questionnaireUsageBean.getAcType();
                 //Adding the delete bean to a new vector
                 if(acType!= null && acType.equals(TypeConstants.DELETE_RECORD)){
                     vcIndexToDeleteUsages.add(questionnaireUsageBean);
                 }
                 //Setting actype to 'D' to all the beans except insert bean
                 if(acType == null || (acType != null && !acType.equals(TypeConstants.INSERT_RECORD))){
                     questionnaireUsageBean.setAcType(TypeConstants.DELETE_RECORD);
                 }
                 if(questionnaireUsageBean!= null && questionnaireUsageBean.getAcType() != null &&
                         !questionnaireUsageBean.getAcType().equals( TypeConstants.INSERT_RECORD)){
                     addUpdQuestionnaireUsage(questionnaireUsageBean);
                 }
             }
             //Removes the actuall delete bean from the original collection
             if(vcIndexToDeleteUsages != null && vcIndexToDeleteUsages.size() > 0){
                 for(int usageDeleteIndex=0;usageDeleteIndex<vcIndexToDeleteUsages.size();usageDeleteIndex++){
                     QuestionnaireUsageBean questionnaireUsageBeanToDelete =
                             (QuestionnaireUsageBean)vcIndexToDeleteUsages.elementAt(usageDeleteIndex);
                     for(int usageIndex=cvQuestionnaireUsageData.size()-1;usageIndex>=0;usageIndex--){
                        QuestionnaireUsageBean questionnaireUsageBean =
                             (QuestionnaireUsageBean)cvQuestionnaireUsageData.elementAt(usageIndex);
                        if(questionnaireUsageBeanToDelete.equals(questionnaireUsageBean) &&
                                (questionnaireUsageBeanToDelete.getAcType().equals(questionnaireUsageBean.getAcType()))){
                            cvQuestionnaireUsageData.remove(usageIndex);
                        }
                     }
                 }

             }
         }

         //COEUSDEV-86 : End

         // Update QuestionnaireQuestions Data
         if(cvQuestionnaireQuestionsData!= null && cvQuestionnaireQuestionsData.size() > 0){
             for(int count=0;count < cvQuestionnaireQuestionsData.size();count++){
                 QuestionnaireBean questionnaireBean = (QuestionnaireBean)cvQuestionnaireQuestionsData.elementAt(count);
                 // 4272: Maintain history of Questionnaires - End
//                 if(questionnaireBean.getAcType() == null){
                 if(questionnaireBean.getAcType() == null && "N".equalsIgnoreCase(questionnaireBean.getAcType())){
                 // 4272: Maintain history of Questionnaires - End
                     continue;
                 }
                 if(questionnaireBean!= null && questionnaireBean.getAcType() != null){
                     procedures.add(addUpdQuestionnaireQuestions(questionnaireBean));
                 }
             }
         }

         if(dbEngine!=null){
             java.sql.Connection conn = null;
             try{
                 conn = dbEngine.beginTxn();
                 if((procedures != null) && (procedures.size() > 0)){
                     dbEngine.executeStoreProcs(procedures,conn);
                     //Update Template CLOB data:case 4287
                     if(vctUpdTemplateProcedures!=null && vctUpdTemplateProcedures.size() > 0){
                         dbEngine.batchSQLUpdate(vctUpdTemplateProcedures, conn);
                     }
                     dbEngine.commit(conn);
                     success = true;
                     //Modified COEUSDEV-86 : Questionnaire for Submission - Start
                     //Inserting the usage information, once all the details are commited
                     //Setting actype to 'I' to all the beans
                     if(cvQuestionnaireUsageData!= null && cvQuestionnaireUsageData.size() > 0){
                         for(int usageIndex=0;usageIndex < cvQuestionnaireUsageData.size();usageIndex++){
                             QuestionnaireUsageBean questionnaireUsageBean =
                                     (QuestionnaireUsageBean)cvQuestionnaireUsageData.elementAt(usageIndex);
                             questionnaireUsageBean.setAcType(TypeConstants.INSERT_RECORD);
                             if(questionnaireUsageBean!= null && questionnaireUsageBean.getAcType() != null){
                                 addUpdQuestionnaireUsage(questionnaireUsageBean);
                             }
                         }
                     }
                     //COEUSDEV-86 : End
                 }
             }catch(Exception sqlEx){
                 success = false;
                 dbEngine.rollback(conn);
                 throw new CoeusException(sqlEx.getMessage());
             }finally{
                 dbEngine.endTxn(conn);
             }
         }else{
             success = false;
             throw new CoeusException("db_exceptionCode.1000");
         }

         return success;
     }


    /** Method used to update/insert/delete all the details of Questionnaire data
     * <li>To fetch the data, it uses UPDATE_QUESTIONNAIRE procedure.
     *
     * @return boolean this holds true for successfull insert/modify or
     * false if fails.
     * @param QuestionnaireUsageBean questionnaireUsageBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */

    public ProcReqParameter addUpdQuestionnaire(QuestionnaireBaseBean questionnaireBaseBean)
    throws CoeusException , DBException{
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;
        param = new Vector();

        param.addElement(new Parameter("AV_QUESTIONNAIRE_ID",
             DBEngineConstants.TYPE_INT, ""+questionnaireBaseBean.getQuestionnaireId()));

        // 4272: Maintain history of Questionnaires - Start
        param.addElement(new Parameter("AV_VERSION_NUMBER",
                DBEngineConstants.TYPE_INT, ""+questionnaireBaseBean.getQuestionnaireVersionNumber()));
        // 4272: Maintain history of Questionnaires - End

        param.addElement(new Parameter("AV_NAME",
             DBEngineConstants.TYPE_STRING, questionnaireBaseBean.getName()));

        param.addElement(new Parameter("AV_DESCRIPTION",
             DBEngineConstants.TYPE_STRING, questionnaireBaseBean.getDescription()));

        param.addElement(new Parameter("AV_UPDATE_TIMESTAMP",
             DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));

        param.addElement(new Parameter("AV_UPDATE_USER",
             DBEngineConstants.TYPE_STRING, userId));

        //Added for coeus4.3 Questionnaire enhancement case#2946 - starts
        param.addElement(new Parameter("AV_IS_FINAL",
             DBEngineConstants.TYPE_STRING,
                questionnaireBaseBean.isFinalFlag()? "Y" : "N"));
        //Added for coeus4.3 Questionnaire enhancement case#2946 - ends
        //Added for case 4287:Questionnaire Templates
        param.addElement(new Parameter("AV_FILE_NAME",
             DBEngineConstants.TYPE_STRING, questionnaireBaseBean.getTemplateName()));
        //4287:End
        param.addElement(new Parameter("AW_QUESTIONNAIRE_ID",
             DBEngineConstants.TYPE_INT, ""+questionnaireBaseBean.getQuestionnaireId()));
        // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
        param.addElement(new Parameter("AV_GROUP_TYPE_CODE",
                DBEngineConstants.TYPE_INT, ""+questionnaireBaseBean.getGroupTypeCode()));
        // Added for COEUSQA-3287 Questionnaire Maintenance Features - End
         param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
             DBEngineConstants.TYPE_TIMESTAMP, questionnaireBaseBean.getUpdateTimestamp()));

        param.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING, questionnaireBaseBean.getAcType()));

        StringBuffer sql = new StringBuffer("call UPDATE_QUESTIONNAIRE(");
        sql.append(" <<AV_QUESTIONNAIRE_ID>> , ");
        // 4272: Maintain history of Questionnaires
        sql.append(" <<AV_VERSION_NUMBER>> , ");
        sql.append(" <<AV_NAME>> , ");
        sql.append(" <<AV_DESCRIPTION>> , ");
        sql.append(" <<AV_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AV_UPDATE_USER>> , ");
        //Added for coeus4.3 Questionnaire enhancement case#2946 - starts
        sql.append(" <<AV_IS_FINAL>> , ");
        //Added for coeus4.3 Questionnaire enhancement case#2946 - ends
        //Added for case 4287:Questionnaire Templates
        sql.append(" <<AV_FILE_NAME>> , ");
        //4287 - End
        sql.append(" <<AW_QUESTIONNAIRE_ID>> , ");
        // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
        sql.append(" <<AV_GROUP_TYPE_CODE>> , ");
        // Added for COEUSQA-3287 Questionnaire Maintenance Features - End
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> )");
        procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        return procReqParameter;
    }


    /** Method used to update/insert/delete all the details of QuestionnaireUsage data
     * <li>To fetch the data, it uses UPDATE_QUESTIONNAIRE_USAGE procedure.
     *
     * @return ProcParameter this holds formatted ProcRequiredParameter
     * @param QuestionnaireUsageBean questionnaireUsageBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    //Modified for COEUSDEV-86: Questionnaire for a Submission - start
//    public ProcReqParameter addUpdQuestionnaireUsage(QuestionnaireUsageBean questionnaireUsageBean)
    public boolean addUpdQuestionnaireUsage(QuestionnaireUsageBean questionnaireUsageBean)//COEUSDEV-86 : End
    throws CoeusException , DBException{
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;
        param = new Vector();
        Vector procedures = new Vector(5,3);
        boolean success = false;
        param.addElement(new Parameter("AV_MODULE_ITEM_CODE",
        DBEngineConstants.TYPE_INT, ""+questionnaireUsageBean.getModuleItemCode()));
        param.addElement(new Parameter("AV_MODULE_SUB_ITEM_CODE",
        DBEngineConstants.TYPE_INT, ""+questionnaireUsageBean.getModuleSubItemCode()));
        param.addElement(new Parameter("AV_QUESTIONNAIRE_ID",
        DBEngineConstants.TYPE_INT, ""+questionnaireUsageBean.getQuestionnaireId()));
        // 4272: Maintain history of Questionnaires - Start
        param.addElement(new Parameter("AV_VERSION_NUMBER",
                DBEngineConstants.TYPE_INT, ""+questionnaireUsageBean.getQuestionnaireVersionNumber()));
        param.addElement(new Parameter("AV_IS_MANDATORY",
                DBEngineConstants.TYPE_STRING, questionnaireUsageBean.isMandatory()? "Y" : "N"));
        // 4272: Maintain history of Questionnaires - End
        param.addElement(new Parameter("AV_RULE_ID",
        DBEngineConstants.TYPE_INT, ""+questionnaireUsageBean.getRuleId()));
        param.addElement(new Parameter("AV_QUESTIONNAIRE_LABEL",
        DBEngineConstants.TYPE_STRING, questionnaireUsageBean.getLabel()));
        param.addElement(new Parameter("AV_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
        param.addElement(new Parameter("AV_UPDATE_USER",
        DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("AW_MODULE_ITEM_CODE",
        DBEngineConstants.TYPE_INT, ""+questionnaireUsageBean.getAwModuleItemCode()));
        param.addElement(new Parameter("AW_MODULE_SUB_ITEM_CODE",
        DBEngineConstants.TYPE_INT, ""+questionnaireUsageBean.getAwModuleSubItemCode()));
        param.addElement(new Parameter("AW_QUESTIONNAIRE_ID",
        DBEngineConstants.TYPE_INT, ""+questionnaireUsageBean.getQuestionnaireId()));

        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP, questionnaireUsageBean.getUsageTimestamp()));
        param.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING, questionnaireUsageBean.getAcType()));
        StringBuffer sql = new StringBuffer("call UPDATE_QUESTIONNAIRE_USAGE(");
        sql.append(" <<AV_MODULE_ITEM_CODE>> , ");
        sql.append(" <<AV_MODULE_SUB_ITEM_CODE>> , ");
        sql.append(" <<AV_QUESTIONNAIRE_ID>> , ");
        // 4272: Maintain history of Questionnaires
        sql.append(" <<AV_VERSION_NUMBER>> , ");
        sql.append(" <<AV_RULE_ID>> , ");
        sql.append(" <<AV_QUESTIONNAIRE_LABEL>> , ");
        // 4272: Maintain history of Questionnaires
        sql.append(" <<AV_IS_MANDATORY>> , ");
        sql.append(" <<AV_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AV_UPDATE_USER>> , ");
        sql.append(" <<AW_MODULE_ITEM_CODE>> , ");
        sql.append(" <<AW_MODULE_SUB_ITEM_CODE>> , ");
        sql.append(" <<AW_QUESTIONNAIRE_ID>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> )");

        procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        //Modified for COEUSDEV-86: Questionnaire for a Submission - start
//        procReqParameter = new ProcReqParameter();
//        procReqParameter.setDSN(DSN);
//        procReqParameter.setParameterInfo(param);
//        procReqParameter.setSqlCommand(sql.toString());
//        return procReqParameter;
        procedures.add(procReqParameter);
         if(dbEngine!=null){
             java.sql.Connection conn = null;
             try{
                 conn = dbEngine.beginTxn();
                 if((procedures != null) && (procedures.size() > 0)){
                     dbEngine.executeStoreProcs(procedures,conn);
                     dbEngine.commit(conn);
                     success = true;
                 }
             }catch(Exception sqlEx){
                 success = false;
                 dbEngine.rollback(conn);
                 throw new CoeusException(sqlEx.getMessage());
             }finally{
                 dbEngine.endTxn(conn);
             }
         }else{
             success = false;
             throw new CoeusException("db_exceptionCode.1000");
         }
         return success;
        //COEUSDEV-86 : End
    }


    /** Method used to update/insert/delete all the details of QuestionnaireQuestions data
     * <li>To fetch the data, it uses UPDATE_QUESTIONNAIRE_QUESTIONS procedure.
     *
     * @return ProcParameter this holds formatted ProcRequiredParameter
     * @param QuestionnaireBean questionnaireBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */

    public ProcReqParameter addUpdQuestionnaireQuestions(QuestionnaireBean questionnaireBean)
    throws CoeusException , DBException{
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;
        param = new Vector();


        param.addElement(new Parameter("AV_QUESTIONNAIRE_ID",
        DBEngineConstants.TYPE_INT, ""+questionnaireBean.getQuestionnaireId()));
        // 4272: Maintain history of Questionnaires - Start
        param.addElement(new Parameter("AV_QUESTIONNAIRE_VERSION",
                DBEngineConstants.TYPE_INT, String.valueOf(questionnaireBean.getQuestionnaireVersionNumber())));
        // 4272: Maintain history of Questionnaires - End
        param.addElement(new Parameter("AV_QUESTION_ID",
        DBEngineConstants.TYPE_INTEGER, questionnaireBean.getQuestionId()));
        param.addElement(new Parameter("AV_QUESTION_NUMBER",
        DBEngineConstants.TYPE_INTEGER, questionnaireBean.getQuestionNumber()));
        // 4272: Maintain history of Questionnaires - Start
        param.addElement(new Parameter("AV_QUESTION_VERSION_NUMBER",
                DBEngineConstants.TYPE_INTEGER, questionnaireBean.getQuestionVersionNumber()));
        // 4272: Maintain history of Questionnaires - End
        param.addElement(new Parameter("AV_PARENT_QUESTION_NUMBER",
        DBEngineConstants.TYPE_INTEGER, questionnaireBean.getParentQuestionNumber()));
        param.addElement(new Parameter("AV_CONDITION_FLAG",
        DBEngineConstants.TYPE_STRING, questionnaireBean.isConditionalFlag()== true?"Y":"N"));
        param.addElement(new Parameter("AV_CONDITION",
        DBEngineConstants.TYPE_STRING, questionnaireBean.getCondition()));
        param.addElement(new Parameter("AV_CONDITION_VALUE",
        DBEngineConstants.TYPE_STRING, questionnaireBean.getConditionValue()));
        param.addElement(new Parameter("AV_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
        param.addElement(new Parameter("AV_UPDATE_USER",
        DBEngineConstants.TYPE_STRING, userId));
        //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946 - starts
        param.addElement(new Parameter("AV_QUESTION_SEQ_NUMBER",
        DBEngineConstants.TYPE_INTEGER, new Integer(questionnaireBean.getQuestionSequenceNumber())));
        //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946 - ends
        // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
        param.addElement(new Parameter("AV_RULE_ID",
        DBEngineConstants.TYPE_INTEGER, new Integer(questionnaireBean.getConditionRuleId())));
        // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
        param.addElement(new Parameter("AW_QUESTIONNAIRE_ID", DBEngineConstants.TYPE_INT, ""+questionnaireBean.getQuestionnaireId()));
        // Added for COEUSQA-3287 Questionnaire Maintenance Features - End
        param.addElement(new Parameter("AW_QUESTION_NUMBER",
        DBEngineConstants.TYPE_INTEGER, questionnaireBean.getQuestionNumber()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP, questionnaireBean.getUpdateTimestamp()));
         param.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING, questionnaireBean.getAcType()));
        StringBuffer sql = new StringBuffer("call UPDATE_QUESTIONNAIRE_QUESTIONS(");
        sql.append(" <<AV_QUESTIONNAIRE_ID>> , ");
        // 4272: Maintain history of Questionnaires
        sql.append(" <<AV_QUESTIONNAIRE_VERSION>> , ");
        sql.append(" <<AV_QUESTION_ID>> , ");
        // 4272: Maintain history of Questionnaires
        sql.append(" <<AV_QUESTION_VERSION_NUMBER>> , ");
        sql.append(" <<AV_QUESTION_NUMBER>> , ");
        sql.append(" <<AV_PARENT_QUESTION_NUMBER>> , ");
        sql.append(" <<AV_CONDITION_FLAG>> , ");
        sql.append(" <<AV_CONDITION>> , ");
        sql.append(" <<AV_CONDITION_VALUE>> , ");
        sql.append(" <<AV_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AV_UPDATE_USER>> , ");
        //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
        sql.append(" <<AV_QUESTION_SEQ_NUMBER>> , ");
        // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
        sql.append(" <<AV_RULE_ID>> , ");
        // Added for COEUSQA-3287 Questionnaire Maintenance Features - End
        sql.append(" <<AW_QUESTIONNAIRE_ID>> , ");
        sql.append(" <<AW_QUESTION_NUMBER>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> )");
        procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        return procReqParameter;
    }


    /** Method used to update/insert/delete all the details of Question data
     * <li>To fetch the data, it uses UPDATE_QUESTION procedure.
     *
     * @return boolean this holds true for successfull insert/modify or
     * false if fails.
     * @param QuestionnaireUsageBean questionnaireUsageBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */

    public boolean addUpdQuestion(QuestionsMaintainanceBean questionsMaintainanceBean)
    throws CoeusException , DBException{
        Vector param = new Vector();
        Vector procedures = new Vector(5,3);
        ProcReqParameter procReqParameter = null;
        param = new Vector();
        boolean success = false;
        dbEngine = new DBEngineImpl();
        param.addElement(new Parameter("AV_QUESTION_ID",
             DBEngineConstants.TYPE_INT, ""+questionsMaintainanceBean.getQuestionId()));
        // 4272: Maintain history of Questionnaires - Start
        param.addElement(new Parameter("AV_VERSION_NUMBER",
             DBEngineConstants.TYPE_INT, ""+questionsMaintainanceBean.getVersionNumber()));
        param.addElement(new Parameter("AV_STATUS",
             DBEngineConstants.TYPE_STRING, questionsMaintainanceBean.getStatus()));
        // 4272: Maintain history of Questionnaires - End
        param.addElement(new Parameter("AV_QUESTION",
             DBEngineConstants.TYPE_STRING, questionsMaintainanceBean.getDescription()));

        param.addElement(new Parameter("AV_MAX_ANSWERS",
             DBEngineConstants.TYPE_INT, ""+questionsMaintainanceBean.getMaxAnswers()));

        param.addElement(new Parameter("AV_VALID_ANSWER",
             DBEngineConstants.TYPE_STRING, questionsMaintainanceBean.getValidAnswers()));

        param.addElement(new Parameter("AV_LOOKUP_NAME",
             DBEngineConstants.TYPE_STRING, questionsMaintainanceBean.getLookupName()));

         param.addElement(new Parameter("AV_ANSWER_DATA_TYPE",
             DBEngineConstants.TYPE_STRING, questionsMaintainanceBean.getAnswerDataType()));

         param.addElement(new Parameter("AV_ANSWER_MAX_LENGTH",
             DBEngineConstants.TYPE_INT, ""+questionsMaintainanceBean.getAnswerMaxLength()));

        param.addElement(new Parameter("AV_LOOKUP_GUI",
            DBEngineConstants.TYPE_STRING, questionsMaintainanceBean.getLookupGui()));

        param.addElement(new Parameter("AV_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));

        param.addElement(new Parameter("AV_UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));

        //Added for question group - start - 1
        param.addElement(new Parameter("AV_GROUP_TYPE_CODE",
            DBEngineConstants.TYPE_INT, ""+questionsMaintainanceBean.getGroupTypeCode()));
        //Added for question group - end - 1

        param.addElement(new Parameter("AW_QUESTION_ID",
            DBEngineConstants.TYPE_INT, questionsMaintainanceBean.getQuestionId()));

        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP, questionsMaintainanceBean.getUpdateTimestamp()));

        param.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING, questionsMaintainanceBean.getAcType()));

        StringBuffer sql = new StringBuffer("call UPDATE_QUESTIONS(");
        sql.append(" <<AV_QUESTION_ID>> , ");
        // 4272: Maintain history of Questionnaires
        sql.append(" <<AV_VERSION_NUMBER>> , ");
        sql.append(" <<AV_QUESTION>> , ");
        sql.append(" <<AV_MAX_ANSWERS>> , ");
        sql.append(" <<AV_VALID_ANSWER>> , ");
        sql.append(" <<AV_LOOKUP_NAME>> , ");
        sql.append(" <<AV_ANSWER_DATA_TYPE>> , ");
        sql.append(" <<AV_ANSWER_MAX_LENGTH>> , ");
        sql.append(" <<AV_LOOKUP_GUI>> , ");
        // 4272: Maintain history of Questionnaires
        sql.append(" <<AV_STATUS>> , ");
        sql.append(" <<AV_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AV_UPDATE_USER>> , ");
        //Added for question group - start - 2
        sql.append(" <<AV_GROUP_TYPE_CODE>> , ");
        //Added for question group - end - 2
        sql.append(" <<AW_QUESTION_ID>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> )");

        procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        procedures.add(procReqParameter);
         if(dbEngine!=null){
             java.sql.Connection conn = null;
             try{
                 conn = dbEngine.beginTxn();
                 if((procedures != null) && (procedures.size() > 0)){
                     dbEngine.executeStoreProcs(procedures,conn);
                     dbEngine.commit(conn);
                     success = true;
                 }
             }catch(Exception sqlEx){
                 success = false;
                 dbEngine.rollback(conn);
                 throw new CoeusException(sqlEx.getMessage());
             }finally{
                 dbEngine.endTxn(conn);
             }
         }else{
             success = false;
             throw new CoeusException("db_exceptionCode.1000");
         }
         return success;

    }
     /** This method will update the Questionnaire Answer header and Answer
      *@param Vector contains the vector of Answer Header and Vector of Answer
      *@throws CoeusException,DBException
      */
    public boolean addUpdQuestionnaireAnsHeaderDetails(Vector vecData) throws
    CoeusException, DBException{
        boolean success = false;
        Vector procedures = new Vector(5,3);
        CoeusVector cvAnsHeaderUpdateData = new CoeusVector();
        CoeusVector cvAnsHeaderInsertData = new CoeusVector();

        CoeusVector cvAnswerUpdateData = new CoeusVector();
        CoeusVector cvAnswerInsertData = new CoeusVector();

        CoeusVector cvAnsHeaderData = (CoeusVector)vecData.get(0);
        CoeusVector cvAnsData = (CoeusVector)vecData.get(1);

        Equals eqInsert = new Equals("acType" , TypeConstants.INSERT_RECORD);
        Equals eqUpdate = new Equals("acType" , TypeConstants.UPDATE_RECORD);

        cvAnsHeaderUpdateData = cvAnsHeaderData.filter(eqUpdate);
        cvAnsHeaderInsertData = cvAnsHeaderData.filter(eqInsert);

        cvAnswerUpdateData = cvAnsData.filter(eqUpdate);
        cvAnswerInsertData = cvAnsData.filter(eqInsert);

        // First Update Questionnaire Answer Header Data and Answers
        //don't update header
//        if(cvAnsHeaderUpdateData!= null && cvAnsHeaderUpdateData.size() > 0){
//            for(Iterator index = cvAnsHeaderUpdateData.iterator(); index.hasNext();) {
//                QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean
//                = (QuestionnaireAnswerHeaderBean)index.next();
//                if(questionnaireAnswerHeaderBean.getAcType() != null){
//                    procedures.add(addUpdQuestionAnsHeader(questionnaireAnswerHeaderBean));
//                }
//            }
//        }

        if(cvAnswerUpdateData!= null && cvAnswerUpdateData.size() > 0){
            for(Iterator index = cvAnswerUpdateData.iterator(); index.hasNext();) {
                QuestionAnswerBean questionAnswerBean
                = (QuestionAnswerBean)index.next();
                if(questionAnswerBean.getAcType() != null){
                    procedures.add(addUpdQuestionAnswer(questionAnswerBean));
                }
            }
        }
        //Modified for auto generation completion id start 1
        if(cvAnsHeaderInsertData!= null && cvAnsHeaderInsertData.size() > 0){
            String strCompletionId = null;
            for(Iterator index = cvAnsHeaderInsertData.iterator(); index.hasNext();) {
                QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean
                = (QuestionnaireAnswerHeaderBean)index.next();
                strCompletionId = generateQuestionnaireCompletionID();
                questionnaireAnswerHeaderBean.setQuestionnaireCompletionId(strCompletionId);
                if(questionnaireAnswerHeaderBean.getAcType() != null){
                    procedures.add(addUpdQuestionAnsHeader(questionnaireAnswerHeaderBean));
                }
                CoeusVector cvFilterData =
                                cvAnswerInsertData.filter(new Equals("questionnaireId",
                                    new Integer(questionnaireAnswerHeaderBean.getQuestionnaireId())));
                cvAnswerInsertData = cvAnswerInsertData.filter(new NotEquals("questionnaireId",
                                    new Integer(questionnaireAnswerHeaderBean.getQuestionnaireId())));
                if(cvFilterData!= null && cvFilterData.size() > 0){
                    for(Iterator cIndex = cvFilterData.iterator(); cIndex.hasNext();) {
                        QuestionAnswerBean questionAnswerBean = (QuestionAnswerBean)cIndex.next();
                        questionAnswerBean.setQuestionnaireCompletionId(strCompletionId);
                        procedures.add(addUpdQuestionAnswer(questionAnswerBean));
                    }

                }
            }
        }
        //Modified for auto generation completion id end 1
        //Added for auto generation completion id start 2
        //If any new answer is exist in existing header
        if(cvAnswerInsertData!= null && cvAnswerInsertData.size() > 0){
            for(Iterator index = cvAnswerInsertData.iterator(); index.hasNext();) {
                QuestionAnswerBean questionAnswerBean
                            = (QuestionAnswerBean)index.next();
                if(cvAnsHeaderUpdateData != null && !cvAnsHeaderUpdateData.isEmpty() ){
                    for(Iterator cIndex = cvAnsHeaderUpdateData.iterator(); cIndex.hasNext();) {
                        QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean
                                = (QuestionnaireAnswerHeaderBean)cIndex.next();
                        if( questionAnswerBean.getQuestionnaireId() == questionnaireAnswerHeaderBean.getQuestionnaireId()){
                            questionAnswerBean.setQuestionnaireCompletionId(questionnaireAnswerHeaderBean.getQuestionnaireCompletionId());
                            break;
                        }

                    }
                }
                if(questionAnswerBean.getAcType() != null && questionAnswerBean.getQuestionnaireCompletionId() != null){
                    procedures.add(addUpdQuestionAnswer(questionAnswerBean));
                }
            }
        }
        //Modified for auto generation completion id end 2
        if(dbEngine!=null){
            java.sql.Connection conn = null;
            try{
                conn = dbEngine.beginTxn();
                if((procedures != null) && (procedures.size() > 0)){
                    dbEngine.executeStoreProcs(procedures,conn);
                    dbEngine.commit(conn);
                    success = true;
                }
            }catch(Exception sqlEx){
                success = false;
                dbEngine.rollback(conn);
                throw new CoeusException(sqlEx.getMessage());
            }finally{
                dbEngine.endTxn(conn);
            }
        }else{
            success = false;
            throw new CoeusException("db_exceptionCode.1000");
        }
        return success;
    }
    /**This method generates the Questionnaire Completion Id
     * @throws CoeusException
     * @throws DBException
     * @return String Completion Id
     */
    public String generateQuestionnaireCompletionID()
                            throws CoeusException , DBException {
        String completionId = null;
        Vector param= new Vector();
        Vector result = new Vector();
        dbEngine = new DBEngineImpl();
        /* calling stored function */
        if(dbEngine!=null){
             result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING QUESTIONNAIRE_COMP_ID>> = call FN_GENERATE_QNR_COMPLETION_ID()}",
            param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            completionId = (String)rowParameter.get("QUESTIONNAIRE_COMP_ID");
        }
        return completionId;
    }
     /** Method used to update/insert the details of Questionnaire Answer Header
     * <li>To fetch the data, it uses UPD_QUESTIONNAIRE_ANS_HEADER procedure.
     *
     * @return ProcParameter this holds formatted ProcRequiredParameter
     * @param QuestionnaireAnswerHeaderBean questionnaireBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */

    public ProcReqParameter addUpdQuestionAnsHeader(QuestionnaireAnswerHeaderBean questionnaireBean)
    throws CoeusException , DBException{
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;
        param = new Vector();


        param.addElement(new Parameter("AV_MODULE_ITEM_CODE",
        DBEngineConstants.TYPE_INT, ""+questionnaireBean.getModuleItemCode()));
        param.addElement(new Parameter("AV_MODULE_ITEM_KEY",
        DBEngineConstants.TYPE_STRING, questionnaireBean.getModuleItemKey()));
        param.addElement(new Parameter("AV_MODULE_SUB_ITEM_CODE",
        // Modified with CoeusQA2313: Completion of Questionnaire for Submission
        // applicableSubmoduleCode will be same as submoduleCode except for amendments
//        DBEngineConstants.TYPE_INT, ""+questionnaireBean.getModuleSubItemCode()));
                DBEngineConstants.TYPE_INT, ""+questionnaireBean.getApplicableSubmoduleCode()));
        // CoeusQA2313: Completion of Questionnaire for Submission - End

                if(questionnaireBean.getModuleItemCode()==3 && questionnaireBean.getModuleSubItemCode()==6)
                {
                 param.addElement(new Parameter("AV_MODULE_SUB_ITEM_KEY",
        DBEngineConstants.TYPE_STRING, questionnaireBean.getModuleSubItemKey()));
                 param.addElement(new Parameter("AW_MODULE_SUB_ITEM_KEY",
        DBEngineConstants.TYPE_STRING, questionnaireBean.getModuleSubItemKey()));

                }
                else
                {
            param.addElement(new Parameter("AV_MODULE_SUB_ITEM_KEY",
          DBEngineConstants.TYPE_INT, questionnaireBean.getModuleSubItemKey()));
            param.addElement(new Parameter("AW_MODULE_SUB_ITEM_KEY",
        DBEngineConstants.TYPE_INT, questionnaireBean.getModuleSubItemKey()));
                }



        param.addElement(new Parameter("AV_QUESTIONNAIRE_ID",
        DBEngineConstants.TYPE_INT, ""+questionnaireBean.getQuestionnaireId()));
        // 4272: Maintain history of Questionnaires - Start
        param.addElement(new Parameter("AV_QUESTIONNAIRE_VERSION",
        DBEngineConstants.TYPE_INT, ""+questionnaireBean.getQuestionnaireVersionNumber()));
        // 4272: Maintain history of Questionnaires - End
        param.addElement(new Parameter("AV_QUESTIONNAIRE_COMPLETION_ID",
        DBEngineConstants.TYPE_STRING, questionnaireBean.getQuestionnaireCompletionId()));
        param.addElement(new Parameter("AV_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));

     questionnaireBean.setCUser(questionnaireBean.getCurrentUser());
          int m=questionnaireBean.getModSubCode();
          String c=questionnaireBean.getCUser();
                  c=questionnaireBean.getCurrentUser();


//              if(questionnaireBean.getModuleItemCode()==3 && questionnaireBean.getModuleSubItemCode()==6)
//              {
//        if(userId.equalsIgnoreCase(c))
//        {
//
//        param.addElement(new Parameter("AV_UPDATE_USER",
//        DBEngineConstants.TYPE_STRING, userId));
//        }else
//        {
//         param.addElement(new Parameter("AV_UPDATE_USER",
//        DBEngineConstants.TYPE_STRING, c));
//        }
//              }
//              else
//              {
                 param.addElement(new Parameter("AV_UPDATE_USER",
                      DBEngineConstants.TYPE_STRING, userId));
//              }

        //Added for coeus4.3 Questionnaire enhancement - starts
        //To be modified while implementing questionnaire
        param.addElement(new Parameter("AV_QNR_COMPLETED_FLAG",
        DBEngineConstants.TYPE_STRING, questionnaireBean.getQuestionnaireCompletionFlag()));
        //Added for coeus4.3 Questionnaire enhancement - ends
        param.addElement(new Parameter("AW_MODULE_ITEM_CODE",
        DBEngineConstants.TYPE_INT, ""+questionnaireBean.getModuleItemCode()));
        param.addElement(new Parameter("AW_MODULE_ITEM_KEY",
        DBEngineConstants.TYPE_STRING, questionnaireBean.getModuleItemKey()));






        // CoeusQA2313: Completion of Questionnaire for Submission - End
                param.addElement(new Parameter("AW_MODULE_SUB_ITEM_CODE",
        // Modified with CoeusQA2313: Completion of Questionnaire for Submission
        // applicableSubmoduleCode will be same as submoduleCode except for amendments
//        DBEngineConstants.TYPE_INT, ""+questionnaireBean.getModuleSubItemCode()));
                DBEngineConstants.TYPE_INT, ""+questionnaireBean.getApplicableSubmoduleCode()));

         param.addElement(new Parameter("AW_QUESTIONNAIRE_ID",
        DBEngineConstants.TYPE_INT, ""+questionnaireBean.getQuestionnaireId()));
         param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP, questionnaireBean.getAwUpdateTimestamp()));


  //~*~**~*~*~*~**~*~*~*~*~**~*~*~*~**~*~*~*~*~*~**~*~*~*~*
//          int m=questionnaireBean.getModSubCode();
//          String c=questionnaireBean.getCUser();

         /* Comment for ppc with coeus engin
          *
          *
          */

      if(questionnaireBean.getModuleItemCode()==3 && questionnaireBean.getModuleSubItemCode()==6){

   Vector updateUser=null;
  Vector vecParam=new Vector();
  String updUser="";


            vecParam.add(new Parameter("AV_MODULE_ITEM_KEY",DBEngineConstants.TYPE_STRING,questionnaireBean.getModuleItemKey()));
        vecParam.addElement(new Parameter("AW_MODULE_ITEM_CODE",DBEngineConstants.TYPE_INT, ""+questionnaireBean.getModuleItemCode()));
        vecParam.addElement(new Parameter("AW_MODULE_SUB_ITEM_CODE",DBEngineConstants.TYPE_INT, ""+questionnaireBean.getApplicableSubmoduleCode()));
          //  vecParam.add(new Parameter("AV_UPDATE_USER",DBEngineConstants.TYPE_STRING,c));
             vecParam.addElement(new Parameter("AW_MODULE_SUB_ITEM_KEY",DBEngineConstants.TYPE_STRING, questionnaireBean.getModuleSubItemKey()));

 if(dbEngine != null) {
              updateUser = dbEngine.executeFunctions(DSN,"{<<OUT INTEGER COUNT>> = call FN_GET_UPDATEUSER_FOR_PPC(<<AV_MODULE_ITEM_KEY>>,<<AW_MODULE_ITEM_CODE>>,<<AW_MODULE_SUB_ITEM_CODE>>,<<AW_MODULE_SUB_ITEM_KEY>>)}",vecParam);

                     } else{
             throw new CoeusException("db_exceptionCode.1000");
                            }

              if(!updateUser.isEmpty())
              {
            HashMap rowParameter = (HashMap)updateUser.elementAt(0);
           int count = Integer.parseInt(rowParameter.get("COUNT").toString());
           // updUser = rowParameter.get("UPDATEUSER").toString();
                    if(count == 1){
               param.addElement(new Parameter("AC_TYPE",DBEngineConstants.TYPE_STRING, questionnaireBean.getAcType()));
            }else if(count == 0){
               param.addElement(new Parameter("AC_TYPE",DBEngineConstants.TYPE_STRING, "I"));
            }

              }

              else
              {
                param.addElement(new Parameter("AC_TYPE",DBEngineConstants.TYPE_STRING, "I"));
              }
        StringBuffer sql = new StringBuffer("call UPD_QUEST_ANS_HEADER_PPC(");
        sql.append(" <<AV_MODULE_ITEM_CODE>> , ");
        sql.append(" <<AV_MODULE_ITEM_KEY>> , ");
        sql.append(" <<AV_MODULE_SUB_ITEM_CODE>> , ");
        sql.append(" <<AV_MODULE_SUB_ITEM_KEY>> , ");
        sql.append(" <<AV_QUESTIONNAIRE_ID>> , ");
        // 4272: Maintain history of Questionnaires
        sql.append(" <<AV_QUESTIONNAIRE_VERSION>> , ");
        sql.append(" <<AV_QUESTIONNAIRE_COMPLETION_ID>> , ");
        sql.append(" <<AV_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AV_UPDATE_USER>> , ");
        //Added for coeus4.3 Questionnaire enhancement - starts
        sql.append(" <<AV_QNR_COMPLETED_FLAG>> , ");
        //Added for coeus4.3 Questionnaire enhancement - ends
        sql.append(" <<AW_MODULE_ITEM_CODE>> , ");
        sql.append(" <<AW_MODULE_ITEM_KEY>> , ");
        sql.append(" <<AW_MODULE_SUB_ITEM_CODE>> , ");
        sql.append(" <<AW_MODULE_SUB_ITEM_KEY>> , ");
        sql.append(" <<AW_QUESTIONNAIRE_ID>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");

        sql.append(" <<AC_TYPE>> )");
        procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        return procReqParameter;

      }

      else
      {


             param.addElement(new Parameter("AC_TYPE",DBEngineConstants.TYPE_STRING, questionnaireBean.getAcType()));
        StringBuffer sql = new StringBuffer("call UPD_QUESTIONNAIRE_ANS_HEADER(");
        sql.append(" <<AV_MODULE_ITEM_CODE>> , ");
        sql.append(" <<AV_MODULE_ITEM_KEY>> , ");
        sql.append(" <<AV_MODULE_SUB_ITEM_CODE>> , ");
        sql.append(" <<AV_MODULE_SUB_ITEM_KEY>> , ");
        sql.append(" <<AV_QUESTIONNAIRE_ID>> , ");
        // 4272: Maintain history of Questionnaires
        sql.append(" <<AV_QUESTIONNAIRE_VERSION>> , ");
        sql.append(" <<AV_QUESTIONNAIRE_COMPLETION_ID>> , ");
        sql.append(" <<AV_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AV_UPDATE_USER>> , ");
        //Added for coeus4.3 Questionnaire enhancement - starts
        sql.append(" <<AV_QNR_COMPLETED_FLAG>> , ");
        //Added for coeus4.3 Questionnaire enhancement - ends
        sql.append(" <<AW_MODULE_ITEM_CODE>> , ");
        sql.append(" <<AW_MODULE_ITEM_KEY>> , ");
        sql.append(" <<AW_MODULE_SUB_ITEM_CODE>> , ");
        sql.append(" <<AW_MODULE_SUB_ITEM_KEY>> , ");
        sql.append(" <<AW_QUESTIONNAIRE_ID>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");

        sql.append(" <<AC_TYPE>> )");
        procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        return procReqParameter;

      }


  //~*~**~*~*~*~**~*~*~*~*~**~*~*~*~*~**~*~*~*~*~*~**~*~*~*~*~**~*~*~*~*~*~*~*~*~**~*~*~*



    }
    /** Method used to update/insert the details of Questionnaire Answer
     * <li>To fetch the data, it uses UPDATE_QUESTIONNAIRE_ANSWERS procedure.
     *
     * @return ProcParameter this holds formatted ProcRequiredParameter
     * @param QuestionnaireAnswerHeaderBean questionnaireBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */

    public ProcReqParameter addUpdQuestionAnswer(QuestionAnswerBean questionAnsBean)
    throws CoeusException , DBException{
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;
        param = new Vector();


        param.addElement(new Parameter("AV_QUESTIONNAIRE_COMPLETION_ID",
        DBEngineConstants.TYPE_STRING, questionAnsBean.getQuestionnaireCompletionId()));
        param.addElement(new Parameter("AV_QUESTION_ID",
        DBEngineConstants.TYPE_INT, questionAnsBean.getQuestionId()));
        // 4272: Maintain history of Questionnaires - Start
        param.addElement(new Parameter("AV_QUESTION_VERSION_NUMBER",
        DBEngineConstants.TYPE_INT, ""+questionAnsBean.getVersionNumber()));
        // 4272: Maintain history of Questionnaires - End
        param.addElement(new Parameter("AV_QUESTION_NUMBER",
        DBEngineConstants.TYPE_INT, ""+questionAnsBean.getQuestionNumber()));
        param.addElement(new Parameter("AV_ANSWER_NUMBER",
        DBEngineConstants.TYPE_INT, ""+questionAnsBean.getAnswerNumber()));
        param.addElement(new Parameter("AV_ANSWER",
        DBEngineConstants.TYPE_STRING, questionAnsBean.getAnswer()));
        param.addElement(new Parameter("AV_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
        param.addElement(new Parameter("AV_UPDATE_USER",
        DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("AW_QUESTIONNAIRE_COMPLETION_ID",
        DBEngineConstants.TYPE_STRING, questionAnsBean.getAwQuestionnaireCompletionId()));
        param.addElement(new Parameter("AW_QUESTION_NUMBER",
        DBEngineConstants.TYPE_INT, ""+questionAnsBean.getAwQuestionNumber()));
        param.addElement(new Parameter("AW_ANSWER_NUMBER",
        DBEngineConstants.TYPE_INT, ""+questionAnsBean.getAwAnswerNumber()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP, questionAnsBean.getAwUpdateTimestamp()));
         param.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING, questionAnsBean.getAcType()));
        StringBuffer sql = new StringBuffer("call UPDATE_QUESTIONNAIRE_ANSWERS(");
        sql.append(" <<AV_QUESTIONNAIRE_COMPLETION_ID>> , ");
        sql.append(" <<AV_QUESTION_ID>> , ");
        // 4272: Maintain history of Questionnaires
        sql.append(" <<AV_QUESTION_VERSION_NUMBER>> , ");
        sql.append(" <<AV_QUESTION_NUMBER>> , ");
        sql.append(" <<AV_ANSWER_NUMBER>> , ");
        sql.append(" <<AV_ANSWER>> , ");
        sql.append(" <<AV_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AV_UPDATE_USER>> , ");
        sql.append(" <<AW_QUESTIONNAIRE_COMPLETION_ID>> , ");
        sql.append(" <<AW_QUESTION_NUMBER>> , ");
        sql.append(" <<AW_ANSWER_NUMBER>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> )");
        procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        return procReqParameter;
    }

    /**
     * Code added for coeus4.3 Questionnaire enhancement case#2946
     * Delete all the answers for given questionnaireCompletionId
     * <li>To do this operation, it uses FN_DEL_ANS_FOR_QUEST_COMPLEID function.
     *
     * @return ProcParameter this holds formatted ProcRequiredParameter
     * @param int questionnaireCompletionId
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter deleteAnswersForQuestCompletId(int questionnaireCompletionId)
    throws CoeusException,DBException {
        Vector param = new Vector();
        Vector result = null;
        ProcReqParameter procReqParameter = null;
        param.add(new Parameter("AV_QUESTIONNAIRE_COMPLETION_ID",DBEngineConstants.TYPE_INT,
                new Integer(questionnaireCompletionId)));
        StringBuffer sql = new StringBuffer("{ <<OUT INTEGER RESULT>> =");
        sql.append(" call FN_DEL_ANS_FOR_QUEST_COMPLEID( ");
        sql.append(" << AV_QUESTIONNAIRE_COMPLETION_ID >>) }");
        procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        return procReqParameter;
    }

    /**
     * Code added for coeus4.3 Questionnaire enhancement case#2946
     * To get the questionnaire completion id for the given datas
     * @param questionnaireModuleObject QuestionnaireAnswerHeaderBean
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return int questionnaireCompletionId
     */
     public int getQuestionnaireCompletId(QuestionnaireAnswerHeaderBean questionnaireModuleObject)
     throws CoeusException,DBException {
         Vector param = new Vector();
         Vector result = null;
         int completionId = -1;
         param.add(new Parameter("AV_MODULE_ITEM_CODE",DBEngineConstants.TYPE_INT,
                 new Integer(questionnaireModuleObject.getModuleItemCode())));
         param.add(new Parameter("AV_MODULE_ITEM_KEY",DBEngineConstants.TYPE_STRING,
                 questionnaireModuleObject.getModuleItemKey()));
         param.add(new Parameter("AV_MODULE_SUB_ITEM_CODE",DBEngineConstants.TYPE_INT,
         // Modified with CoeusQA2313: Completion of Questionnaire for Submission
         // applicableSubmoduleCode will be same as submoduleCode except for amendments
//         new Integer(questionnaireModuleObject.getModuleSubItemCode())));
                 new Integer(questionnaireModuleObject.getApplicableSubmoduleCode())));
         // CoeusQA2313: Completion of Questionnaire for Submission - End

        if(questionnaireModuleObject.getModuleItemCode()==3&&questionnaireModuleObject.getModuleSubItemCode()==6)
        {
                 param.add(new Parameter("AV_MODULE_SUB_ITEM_KEY",DBEngineConstants.TYPE_STRING,
                 questionnaireModuleObject.getModuleSubItemKey()));
        }
        else
        {
                 param.add(new Parameter("AV_MODULE_SUB_ITEM_KEY",DBEngineConstants.TYPE_INT,
                 questionnaireModuleObject.getModuleSubItemKey()));
        }





         param.add(new Parameter("AV_QUESTIONNAIRE_ID",DBEngineConstants.TYPE_INT,
                 new Integer(questionnaireModuleObject.getQuestionnaireId())));
         // 4272: Maintain History of Questionnaires - Start
          param.add(new Parameter("AV_QNR_VERSION_NUMBER",DBEngineConstants.TYPE_INT,
                 new Integer(questionnaireModuleObject.getQuestionnaireVersionNumber())));
         // 4272: Maintain History of Questionnaires - End

//           if(questionnaireModuleObject.getModuleItemCode()==3 && questionnaireModuleObject.getModuleSubItemCode()==6)
//           {
//         param.add(new Parameter("AV_UPDATE_USER",DBEngineConstants.TYPE_STRING,
//                 questionnaireModuleObject.getCurrentUser()));
//
//
//         if(dbEngine != null) {
//             result = dbEngine.executeFunctions(DSN,
//             "{ <<OUT INTEGER COMPLETION_ID>> = "
//             + " call FN_GET_QUEST_COMPLETID_PPC(<< AV_MODULE_ITEM_CODE >>, <<AV_MODULE_ITEM_KEY>>,"
//             + "<<AV_MODULE_SUB_ITEM_CODE>>, <<AV_MODULE_SUB_ITEM_KEY>>, <<AV_QUESTIONNAIRE_ID>>, <<AV_QNR_VERSION_NUMBER>>,<<AV_UPDATE_USER>>) }", param);
//         }else{
//             throw new CoeusException("db_exceptionCode.1000");
//         }
//
//
//         if(!result.isEmpty()){
//             HashMap rowParameter = (HashMap)result.elementAt(0);
//             completionId = Integer.parseInt(rowParameter.get("COMPLETION_ID").toString());
//         }
//           }
//
//           else{
              if(dbEngine != null) {
             result = dbEngine.executeFunctions(DSN,
             "{ <<OUT INTEGER COMPLETION_ID>> = "
             + " call FN_GET_QUESTIONNAIRE_COMPLETID(<<AV_MODULE_ITEM_CODE >>, <<AV_MODULE_ITEM_KEY>>,"
             + "<<AV_MODULE_SUB_ITEM_CODE>>, <<AV_MODULE_SUB_ITEM_KEY>>, <<AV_QUESTIONNAIRE_ID>>, <<AV_QNR_VERSION_NUMBER>>) }", param);
         }else{
             throw new CoeusException("db_exceptionCode.1000");
         }
         if(!result.isEmpty()){
             HashMap rowParameter = (HashMap)result.elementAt(0);
             completionId = Integer.parseInt(rowParameter.get("COMPLETION_ID").toString());
         }
          // }

         return completionId;
     }

    /**
     * Code added for coeus4.3 Questionnaire enhancement case#2946
     * To save the Questionnaire Answers datas,
     * This method deletes all the answers, and insert the new answered datas
     * @param questionnaireModuleObject QuestionnaireAnswerHeaderBean
     * @param lhmPagesAnswered LinkedHashMap
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return boolean success (To indicate if saving is completed or not)
     */
    public boolean saveQuestionnaireAnswers(QuestionnaireAnswerHeaderBean questionnaireModuleObject,
            LinkedHashMap lhmPagesAnswered, boolean isSaveAndComplete)throws CoeusException,DBException{
        QuestionAnswerBean questionAnsBean = null;
        Vector procedures = new Vector(5,3);
        HashMap hmAnswerData = new HashMap();
        boolean success = false;

        int m=questionnaireModuleObject.getModuleSubItemCode();
        String c=questionnaireModuleObject.getCurrentUser();

     questionAnsBean = new QuestionAnswerBean();


     questionAnsBean.setModSubCode(m);
     questionAnsBean.setCUser(c);

        int questionnaireCompletionId = getQuestionnaireCompletId(questionnaireModuleObject);


           //~*~**~*~*~*~**~*~*~*~*~**~*~*~*~**~*~*~*~*~*~**~*~*~*~*
         // int m=questionnaireBean.getModSubCode();
        //  String c=questionnaireBean.getCUser();
      if(questionnaireModuleObject.getModuleItemCode()==3 && questionnaireModuleObject.getModuleSubItemCode()==6)
      {
   Vector updateUser=null;
  Vector vecParam=new Vector();
  String updUser="";


            vecParam.add(new Parameter("AV_MODULE_ITEM_KEY",DBEngineConstants.TYPE_STRING,questionnaireModuleObject.getModuleItemKey()));
        vecParam.addElement(new Parameter("AW_MODULE_ITEM_CODE",DBEngineConstants.TYPE_INT, ""+questionnaireModuleObject.getModuleItemCode()));
        vecParam.addElement(new Parameter("AW_MODULE_SUB_ITEM_CODE",DBEngineConstants.TYPE_INT, ""+questionnaireModuleObject.getApplicableSubmoduleCode()));
            vecParam.add(new Parameter("AW_MODULE_SUB_ITEM_KEY",DBEngineConstants.TYPE_STRING, questionnaireModuleObject.getModuleSubItemKey()));

 if(dbEngine != null) {
              updateUser = dbEngine.executeFunctions(DSN,"{<<OUT INTEGER COUNT>> = call FN_GET_UPDATEUSER_FOR_PPC(<<AV_MODULE_ITEM_KEY>>,<<AW_MODULE_ITEM_CODE>>,<<AW_MODULE_SUB_ITEM_CODE>>,<<AW_MODULE_SUB_ITEM_KEY>>)}",vecParam);

                     } else{
             throw new CoeusException("db_exceptionCode.1000");
                            }

              if(!updateUser.isEmpty())
              {
            HashMap rowParameter = (HashMap)updateUser.elementAt(0);
           int count = Integer.parseInt(rowParameter.get("COUNT").toString());
           // updUser = rowParameter.get("UPDATEUSER").toString();
                    if(count == 0){
              questionnaireCompletionId = -1;
                                }

              }

              else
              {

              }

      }
  //~*~**~*~*~*~**~*~*~*~*~**~*~*~*~*~**~*~*~*~*~*~**~*~*~*~*~**~*~*~*~*~*~*~*~*~**~*~*~*


        if(questionnaireCompletionId == -1){
            String questionnaireCompId = generateQuestionnaireCompletionID();
            questionnaireCompletionId = Integer.parseInt(questionnaireCompId);
            questionnaireModuleObject.setAcType(TypeConstants.INSERT_RECORD);
            questionnaireModuleObject.setQuestionnaireCompletionId(questionnaireCompId);
        } else {
            questionnaireModuleObject.setAcType(TypeConstants.UPDATE_RECORD);
            questionnaireModuleObject.setQuestionnaireCompletionId(""+questionnaireCompletionId);
            // Commented for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - Start
//                procedures.add(deleteAnswersForQuestCompletId(questionnaireCompletionId));
            // Commented for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - End

        }
        questionnaireModuleObject.setQuestionnaireCompletionFlag("N");
        procedures.add(addUpdQuestionAnsHeader(questionnaireModuleObject));


        Set keySet = lhmPagesAnswered.keySet();
        Object[] objQuestions = keySet.toArray();
        
        for(int index = 0 ; index < objQuestions.length ; index++){
            String page = (String) objQuestions[index];
            CoeusVector cvQuestionAnswers = (CoeusVector)lhmPagesAnswered.get(page);
            if(cvQuestionAnswers != null && cvQuestionAnswers.size() > 0){
                for(int count = 0 ; count < cvQuestionAnswers.size() ; count++){
                    QuestionnaireQuestionsBean questionnaireQuestionsBean =
                            (QuestionnaireQuestionsBean) cvQuestionAnswers.get(count);
                    if(!isSaveAndComplete){
                        String complId = questionnaireModuleObject.getQuestionnaireCompletionId();
                        if(complId != null && !"".equals(complId)){
                            int qnrCompletionId = Integer.parseInt(complId);
                            deleteQuesAnsForCompletionId(qnrCompletionId,questionnaireQuestionsBean.getQuestionNumber());
                        }
                    }
                    questionAnsBean = new QuestionAnswerBean();
                    if(questionnaireQuestionsBean.getAnswer() == null
                            || questionnaireQuestionsBean.getAnswer().equals("")
                            || questionnaireQuestionsBean.getAnswerNumber() == 0){
                        continue;
                    }
                    String answerKey = ""+questionnaireQuestionsBean.getQuestionId().intValue()
                    +questionnaireQuestionsBean.getQuestionNumber().intValue();
                    if(hmAnswerData.get(answerKey) == null){
                        questionnaireQuestionsBean.setAnswerNumber(1);
                        hmAnswerData.put(answerKey, new Integer(questionnaireQuestionsBean.getAnswerNumber()));
                    } else {
                        int ansNumber = ((Integer)hmAnswerData.get(answerKey)).intValue();
                        questionnaireQuestionsBean.setAnswerNumber(++ansNumber);
                        hmAnswerData.put(answerKey, new Integer(questionnaireQuestionsBean.getAnswerNumber()));
                    }
                    questionAnsBean.setAnswer(questionnaireQuestionsBean.getAnswer());
                    questionAnsBean.setQuestionnaireCompletionId(""+questionnaireCompletionId);
                    questionAnsBean.setQuestionId(questionnaireQuestionsBean.getQuestionId());
                    // 4272: Maintain history of Questionnaires
                    questionAnsBean.setVersionNumber(questionnaireQuestionsBean.getQuestionVersionNumber().intValue());
                    
                    questionAnsBean.setQuestionNumber(questionnaireQuestionsBean.getQuestionNumber().intValue());
                    questionAnsBean.setAnswerNumber(questionnaireQuestionsBean.getAnswerNumber());
                    questionAnsBean.setAnswerNumber(questionnaireQuestionsBean.getAnswerNumber());
                    questionAnsBean.setAwQuestionnaireCompletionId(""+questionnaireCompletionId);
                    questionAnsBean.setAwQuestionNumber(questionnaireQuestionsBean.getQuestionNumber().intValue());
                    questionAnsBean.setAwAnswerNumber(questionnaireQuestionsBean.getAnswerNumber());
                    questionAnsBean.setAcType(TypeConstants.INSERT_RECORD);
                    
                    String answer = questionAnsBean.getAnswer().toString();
                    
                    if(answer.equals("Y")) {
                        QuestionAnsFlag = "true";
                    }
                    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - Start
                    // Question answers will be deleted and then inserted
                    if(isSaveAndComplete){
                        questionAnsBean.setAcType(TypeConstants.DELETE_RECORD);
                        Vector vecProcedures = new Vector();
                        vecProcedures.add(addUpdQuestionAnswer(questionAnsBean));
                        if(dbEngine!=null){
                            java.sql.Connection conn = null;
                            try{
                                conn = dbEngine.beginTxn();
                                if((vecProcedures != null) && (vecProcedures.size() > 0)){
                                    dbEngine.executeStoreProcs(vecProcedures,conn);
                                    dbEngine.commit(conn);
                                    success = true;
                                }
                            }catch(Exception sqlEx){
                                success = false;
                                dbEngine.rollback(conn);
                                throw new CoeusException(sqlEx.getMessage());
                            }finally{
                                dbEngine.endTxn(conn);
                            }
                        }else{
                            success = false;
                            throw new CoeusException("db_exceptionCode.1000");
                        }
                        
                        questionAnsBean.setAcType(TypeConstants.INSERT_RECORD);
                    }
                    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - End
                    procedures.add(addUpdQuestionAnswer(questionAnsBean));
                }
            }
            
        }

        if(dbEngine!=null){
            java.sql.Connection conn = null;
            try{
                conn = dbEngine.beginTxn();
                if((procedures != null) && (procedures.size() > 0)){
                    dbEngine.executeStoreProcs(procedures,conn);
                    dbEngine.commit(conn);
                    success = true;
                }
            }catch(Exception sqlEx){
                success = false;
                dbEngine.rollback(conn);
                throw new CoeusException(sqlEx.getMessage());
            }finally{
                dbEngine.endTxn(conn);
            }
        }else{
            success = false;
            throw new CoeusException("db_exceptionCode.1000");
        }
       // Commented for COEUSQA-3723 : Questionnaire with question rules does not save and complete process    - Start     
       // There is no business logic here
//        for(int index = 0 ; index < objQuestions.length ; index++){
//            String page = (String) objQuestions[index];
//            CoeusVector cvQuestionAnswers = (CoeusVector)lhmPagesAnswered.get(page);
//            if(cvQuestionAnswers != null && cvQuestionAnswers.size() > 0){
//                for(int count = 0 ; count < cvQuestionAnswers.size() ; count++){
//                    QuestionnaireQuestionsBean questionnaireQuestionsBean =
//                            (QuestionnaireQuestionsBean) cvQuestionAnswers.get(count);
//                    questionAnsBean = new QuestionAnswerBean();
//                    if( (questionnaireQuestionsBean.getAnswer() != null
//                            && !questionnaireQuestionsBean.getAnswer().equals(""))
//                            || questionnaireQuestionsBean.getAnswerNumber() == 0){
//                        continue;
//                    }
//                    String answerKey = ""+questionnaireQuestionsBean.getQuestionId().intValue()
//                        +questionnaireQuestionsBean.getQuestionNumber().intValue();
//                    if(hmAnswerData.get(answerKey) == null){
//                        questionnaireQuestionsBean.setAnswerNumber(1);
//                        hmAnswerData.put(answerKey, new Integer(questionnaireQuestionsBean.getAnswerNumber()));
//                    } else {
//                        int ansNumber = ((Integer)hmAnswerData.get(answerKey)).intValue();
//                        questionnaireQuestionsBean.setAnswerNumber(++ansNumber);
//                        hmAnswerData.put(answerKey, new Integer(questionnaireQuestionsBean.getAnswerNumber()));
//                    }
//                }
//            }
//        }    
        // Commented for COEUSQA-3723 : Questionnaire with question rules does not save and complete process    - Start
        return success;
    }
    

    /**
     * Code added for coeus4.3 Questionnaire enhancement case#2946
     * This method is used to update the Questionnaire Header datas
     * @param questionnaireModuleObject QuestionnaireAnswerHeaderBean
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    public void updateQuestionnaireHeader(QuestionnaireAnswerHeaderBean questionnaireModuleObject)
    throws CoeusException,DBException{
        Vector procedures = new Vector(5,3);
        int questionnaireCompletionId = getQuestionnaireCompletId(questionnaireModuleObject);
        questionnaireModuleObject.setQuestionnaireCompletionId(""+questionnaireCompletionId);
        procedures.add(addUpdQuestionAnsHeader(questionnaireModuleObject));
        if(dbEngine!=null){
            java.sql.Connection conn = null;
            try{
                conn = dbEngine.beginTxn();
                if((procedures != null) && (procedures.size() > 0)){
                    dbEngine.executeStoreProcs(procedures,conn);
                    dbEngine.commit(conn);
                }
            }catch(Exception sqlEx){
                dbEngine.rollback(conn);
                throw new CoeusException(sqlEx.getMessage());
            }finally{
                dbEngine.endTxn(conn);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
    }

    /**
     * Code added for coeus4.3 Questionnaire enhancement case#2946
     * Deletes all the answers for the questionnaireCompletionId and
     * updates the Questionnaire Header Questionnaire completion data to N
     * @param questionnaireModuleObject QuestionnaireAnswerHeaderBean
     * @param acType String acType value
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    public void deleteAnswersForQuestCompletId(QuestionnaireAnswerHeaderBean questionnaireModuleObject,
            String acType)throws CoeusException,DBException{
        Vector procedures = new Vector(5,3);
        int questionnaireCompletionId = getQuestionnaireCompletId(questionnaireModuleObject);
        if(questionnaireCompletionId == -1){
            return;
        }
        questionnaireModuleObject.setAcType(acType);
        questionnaireModuleObject.setQuestionnaireCompletionId(""+questionnaireCompletionId);
        questionnaireModuleObject.setQuestionnaireCompletionFlag("N");
        procedures.add(deleteAnswersForQuestCompletId(questionnaireCompletionId));
        procedures.add(addUpdQuestionAnsHeader(questionnaireModuleObject));
        if(dbEngine!=null){
            java.sql.Connection conn = null;
            try{
                conn = dbEngine.beginTxn();
                if((procedures != null) && (procedures.size() > 0)){
                    dbEngine.executeStoreProcs(procedures,conn);
                    dbEngine.commit(conn);
                }
            }catch(Exception sqlEx){
                dbEngine.rollback(conn);
                throw new CoeusException(sqlEx.getMessage());
            }finally{
                dbEngine.endTxn(conn);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
    }

    /**
     * Code added for coeus4.3 enhancements
     * To update the Questionnaire details
     * @param cvQuestionnaireData CoeusVector questionnaire data
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    public void updateQuestionnaireFlag(CoeusVector cvQuestionnaireData) throws CoeusException, DBException{
        Vector procedures = new Vector(5,3);
        // Update QuestionnaireData data
        if(cvQuestionnaireData!= null && cvQuestionnaireData.size() > 0){
            for(int index = 0; index < cvQuestionnaireData.size(); index++){
                QuestionnaireBaseBean questionnaireBaseBean = (QuestionnaireBaseBean)cvQuestionnaireData.elementAt(index);
                if(questionnaireBaseBean!= null && questionnaireBaseBean.getAcType() != null){
                    procedures.add(addUpdQuestionnaire(questionnaireBaseBean));
                }
            }
        }
        if(dbEngine!=null){
            java.sql.Connection conn = null;
            try{
                conn = dbEngine.beginTxn();
                if((procedures != null) && (procedures.size() > 0)){
                    dbEngine.executeStoreProcs(procedures,conn);
                    dbEngine.commit(conn);
                }
            }catch(Exception sqlEx){
                dbEngine.rollback(conn);
                throw new CoeusException(sqlEx.getMessage());
            }finally{
                dbEngine.endTxn(conn);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
    }

    // Case# 3524: Add Explanation field to Questions - Start

    /**
     * This method updates data in OSP$QUESTION_EXPLANATION table
     * @throws CoeusException
     * @throws DBException
     * @param questionExplanationBean QuestionExplanationBean
     */

    public boolean addUpdQuestionExplanation(QuestionExplanationBean questionExplanationBean)
    throws CoeusException , DBException{
        Vector param = new Vector();
        Vector procedures = new Vector(5,3);
        ProcReqParameter procReqParameter = null;
        param = new Vector();
        boolean success = false;
        dbEngine = new DBEngineImpl();
        param.addElement(new Parameter("AV_QUESTION_ID",
                DBEngineConstants.TYPE_INT, ""+questionExplanationBean.getQuestionId()));

        param.addElement(new Parameter("AV_EXPLANATION_TYPE",
                DBEngineConstants.TYPE_STRING, questionExplanationBean.getExplanationType()));

        param.addElement(new Parameter("AV_EXPLANATION",
                DBEngineConstants.TYPE_STRING, questionExplanationBean.getExplanation()));

        param.addElement(new Parameter("AV_UPDATE_TIMESTAMP",
             DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));

        param.addElement(new Parameter("AV_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));


        param.addElement(new Parameter("AW_QUESTION_ID",
                DBEngineConstants.TYPE_INT, questionExplanationBean.getQuestionId()));
        param.addElement(new Parameter("AW_EXPLANATION_TYPE",
                DBEngineConstants.TYPE_STRING, questionExplanationBean.getExplanationType()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, questionExplanationBean.getUpdateTimestamp()));

        param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING, questionExplanationBean.getAcType()));

        StringBuffer sql = new StringBuffer("call UPDATE_QUESTION_EXPLANATION(");
        sql.append(" <<AV_QUESTION_ID>> , ");
        sql.append(" <<AV_EXPLANATION_TYPE>> , ");
        sql.append(" <<AV_EXPLANATION>> , ");
        sql.append(" <<AV_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AV_UPDATE_USER>> , ");
        sql.append(" <<AW_QUESTION_ID>> , ");
        sql.append(" <<AW_EXPLANATION_TYPE>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> )");

        procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        procedures.add(procReqParameter);
        if(dbEngine!=null){
            java.sql.Connection conn = null;
            try{
                conn = dbEngine.beginTxn();
                if((procedures != null) && (procedures.size() > 0)){
                    dbEngine.executeStoreProcs(procedures,conn);
                    dbEngine.commit(conn);
                    success = true;
                }
            }catch(Exception sqlEx){
                success = false;
                dbEngine.rollback(conn);
                throw new CoeusException(sqlEx.getMessage());
            }finally{
                dbEngine.endTxn(conn);
            }
        }else{
            success = false;
            throw new CoeusException("db_exceptionCode.1000");
        }
        return success;

    }
    // Case# 3524: Add Explanation field to Questions - End

    //New method Added with case 4287 : Questionnaire Templates - Start
    /**
     *  The method used to get the insert query for the Questionnaire Template CLOB
     *  @param questionnaireTemplateBean QuestionnaireTemplateBean
     *  @param questionnaireVersionNumber int
     *  @return procReqParameter ProcReqParameter
     *  @exception DBException if parameters are not correctly set.
     */
    // 4272: Maintain history of Questionnaires
//    public ProcReqParameter updateQuestionnaireTemplate(QuestionnaireTemplateBean questionnaireTemplateBean)
    public ProcReqParameter updateQuestionnaireTemplate(QuestionnaireTemplateBean questionnaireTemplateBean, int questionnaireVersionNumber)
    throws  DBException {

        ProcReqParameter procReqParameter = null;
        StringBuffer sqlUploadDocument    = new StringBuffer("") ;
        Vector param    = new Vector();
        byte[] fileData = questionnaireTemplateBean.getTemplateFileBytes();

        param.addElement(new Parameter("QUESTIONNAIRE_ID",
                DBEngineConstants.TYPE_INT,new Integer(questionnaireTemplateBean.getQuestionnaireNumber())));
       // 4272: Maintain history of Questionnaires - Start
       param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,String.valueOf(questionnaireVersionNumber)));
        // 4272: Maintain history of Questionnaires - End
        if(fileData==null){//deleting a template
            param.addElement(new Parameter("TEMPLATE",
                    DBEngineConstants.TYPE_CLOB,null));
        }else{//updating a template
            param.addElement(new Parameter("TEMPLATE",
                    DBEngineConstants.TYPE_CLOB,new String(fileData)));
        }
        sqlUploadDocument.append("update OSP$QUESTIONNAIRE set");
        sqlUploadDocument.append(" TEMPLATE = ");
        sqlUploadDocument.append(" <<TEMPLATE>> ");
        sqlUploadDocument.append(" where ");
        sqlUploadDocument.append(" QUESTIONNAIRE_ID = ");
        sqlUploadDocument.append(" <<QUESTIONNAIRE_ID>> ");
        // 4272: Maintain history of Questionnaires - Start
        sqlUploadDocument.append(" and ");
        sqlUploadDocument.append(" VERSION_NUMBER = ");
        sqlUploadDocument.append(" <<VERSION_NUMBER>> ");
        // 4272: Maintain history of Questionnaires - End

        procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlUploadDocument.toString());
        return procReqParameter;
    }
    //   Case 4287 : End

    public boolean deleteQuestionnaireAnswers(int moduleCode, int subModulecode, String moduleItemKey, String moduleSubItemKey) throws DBException, CoeusException{
        boolean deleted = true;

        Vector param = new Vector();
        Vector result = null;
        int count = -1;
        param.add(new Parameter("AV_MODULE_ITEM_CODE",DBEngineConstants.TYPE_INT,moduleCode));
        param.add(new Parameter("AV_MODULE_ITEM_KEY",DBEngineConstants.TYPE_STRING,moduleItemKey));
        param.add(new Parameter("AV_MODULE_SUB_ITEM_CODE",DBEngineConstants.TYPE_INT,subModulecode));
        param.add(new Parameter("AV_MODULE_SUB_ITEM_KEY",DBEngineConstants.TYPE_STRING,moduleSubItemKey));
        if(dbEngine != null) {
            result = dbEngine.executeFunctions(DSN,
                    "{ <<OUT INTEGER COUNT>> = "
                    +" call FN_DELETE_QNR_ANSWERS(<<AV_MODULE_ITEM_CODE >>, <<AV_MODULE_ITEM_KEY>>, <<AV_MODULE_SUB_ITEM_CODE>>, <<AV_MODULE_SUB_ITEM_KEY>>) }", param);
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
            deleted = false;
        }

        return deleted;
    }

    // Added with CoeusQA2313: Completion of Questionnaire for Submission
    /**
     * This function copies the questionnaire answers in the previous sequence
     * to the latest sequence in case of a revision.
     * To achieve this, it calls FN_COPY_QNR_FOR_REVISION
     * @param questionnaireModuleObject - The QuestionnaireAnswerHeaderBean object
     * @return boolean whether any records are copied or not.
     */
    public boolean copyQuestionnairesForRevisions(QuestionnaireAnswerHeaderBean questionnaireModuleObject)
    throws CoeusException,DBException{

        boolean updated = false;
        if(dbEngine != null) {
            Vector result = null;
            Vector param = new Vector();
            param.addElement(new Parameter("MODULE_CODE",
                    DBEngineConstants.TYPE_INT,
                    questionnaireModuleObject.getModuleItemCode()));
            param.addElement(new Parameter("PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                    questionnaireModuleObject.getModuleItemKey()));
            param.addElement(new Parameter("SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                    questionnaireModuleObject.getModuleSubItemKey()));
            param.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));

            StringBuffer sqlProtoAmendRen = new StringBuffer(
                    "{<<OUT INTEGER IS_UPDATE>> = call "
                    +"FN_COPY_QNR_FOR_REVISION(");
            sqlProtoAmendRen.append(" <<MODULE_CODE>> , ");
            sqlProtoAmendRen.append(" <<PROTOCOL_NUMBER>> , ");
            sqlProtoAmendRen.append(" <<SEQUENCE_NUMBER>> , ");
            sqlProtoAmendRen.append(" <<UPDATE_USER>> ) }");
            result = dbEngine.executeFunctions(DSN,sqlProtoAmendRen.toString(),param);
            if(!result.isEmpty()){
                HashMap rowParameter = (HashMap)result.elementAt(0);
                if(rowParameter.get("IS_UPDATE") != null){
                    rowParameter = (HashMap)result.elementAt(0);
                    int count = Integer.parseInt(rowParameter.get("IS_UPDATE").toString());
                    if(count == 1){
                        updated = true;
                    }
                }
            }

        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return updated;
    }
    
    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - Start
    /**
     * Method to delete question answer for the completion id
     * @param qnrCompletionId 
     * @param questionNumber 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return isDeleted
     */
    public int deleteQuesAnsForCompletionId(int qnrCompletionId, int questionNumber) throws CoeusException, DBException {
        int isDeleted = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("AS_QNR_COMPLETION_ID", DBEngineConstants.TYPE_INT,new Integer(qnrCompletionId).toString()));
        param.addElement(new Parameter("AS_QUESTION_NUMBER", DBEngineConstants.TYPE_INT,new Integer(questionNumber).toString()));
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER IS_DELETED>> = "
                    +" call FN_DEL_QUES_ANS_FOR_COMPLEID(<< AS_QNR_COMPLETION_ID >> ,<< AS_QUESTION_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            isDeleted = Integer.parseInt(rowParameter.get("IS_DELETED").toString());
        }
        return isDeleted;
    }
    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - End
    
    
    // Added for COEUSQA-3723 : Questionnaire with question rules does not save and complete process    - Start
    /**
     * Method to delete all the child and parent answered answers for the completion id
     * @param qnrCompletionId
     * @param questionNumber
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return isDeleted
     */
    public int deleteQuesAnsBranchForCompletionId(int qnrCompletionId, int questionNumber) throws CoeusException, DBException {
        int isDeleted = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("AS_QNR_COMPLETION_ID", DBEngineConstants.TYPE_INT,new Integer(qnrCompletionId).toString()));
        param.addElement(new Parameter("AS_QUESTION_NUMBER", DBEngineConstants.TYPE_INT,new Integer(questionNumber).toString()));
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER IS_DELETED>> = "
                    +" call FN_DEL_COMPL_QNR_ANS_BRANCH(<< AS_QNR_COMPLETION_ID >> ,<< AS_QUESTION_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            isDeleted = Integer.parseInt(rowParameter.get("IS_DELETED").toString());
        }
        return isDeleted;
    }
    // Added for COEUSQA-3723 : Questionnaire with question rules does not save and complete process    - Start
    
}
