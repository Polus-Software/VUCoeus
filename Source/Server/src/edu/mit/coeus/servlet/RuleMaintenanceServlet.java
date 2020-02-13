/*
 * @(#)RuleMaintainanceServlet.java 1.0 10/17/05
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 17-AUG-2007
 * by Leena
 */
package edu.mit.coeus.servlet;

import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.mapsrules.bean.BusinessRuleBean;
//import edu.mit.coeus.mapsrules.bean.BusinessRuleFunctionBean;
import edu.mit.coeus.mapsrules.bean.MetaRuleBean;
import edu.mit.coeus.mapsrules.bean.MetaRuleDetailBean;
import edu.mit.coeus.mapsrules.bean.RuleUpdateTxnBean;
import edu.mit.coeus.mapsrules.bean.RulesTxnBean;
import edu.mit.coeus.questionnaire.bean.ModuleDataBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireUsageBean;
import edu.mit.coeus.questionnaire.bean.SubModuleDataBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
//import edu.mit.coeus.utils.locking.LockingBean;
//import edu.mit.coeus.utils.locking.LockingException;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.servlet.*;
import javax.servlet.http.*;


/**
 *
 * @author  chandrashekara
 */
public class RuleMaintenanceServlet extends CoeusBaseServlet {
    
    private static final char RULE_DETAILS = 'A';
    private static final char GET_DEFINE_RULE_DATA = 'B';
    private static final char GET_CONDITION_EXPRESSIONS = 'C';
    private static final char UPDATE_META_RULE_DATA = 'D' ;
    private static final char GET_META_RULE_ID = 'F';
    private static final char GET_YNQ_EXPLANATION = 'G';
    private static final char CHECK_FOR_ARGS = 'H';
    private static final char GET_ARG_INFO = 'I';
    private static final char SAVE_RULE_CONDITIONS = 'J';
    private static final char GET_BUSINESS_RULE_DATA = 'K';
    private static final char GET_DATATYPE_FOR_VALIDATION = 'L';
    
    // User Rights constants
    private static final String ADD_BUSINESS_RULE = "ADD_BUSINESS_RULE";
    private static final String MODIFY_BUSINESS_RULE = "MODIFY_BUSINESS_RULE";
    private static final String DELETE_BUSINESS_RULE = "DELETE_BUSINESS_RULE";
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        RequesterBean requester = null;
        ResponderBean responder = new ResponderBean();
        UserInfoBean infoBean = null;
        String loggedinUser = null;
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        String loggedInUser = null;
        String unitNumber = null;
        RulesTxnBean rulesTxnBean = null;
        RuleUpdateTxnBean ruleUpdateTxnBean = null;
        
        try {
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            requester = (RequesterBean)inputFromApplet.readObject();
            isValidRequest(requester);
            loggedInUser = requester.getUserName();
            UserDetailsBean userDetailsBean = new UserDetailsBean();
            infoBean = userDetailsBean.getUserInfo(loggedInUser);
            unitNumber = infoBean.getUnitNumber();
            char functionType = requester.getFunctionType();
            
            if(functionType == RULE_DETAILS) {
                String unitNo = (String)requester.getDataObject();
                Hashtable hmRuleData = getRuleData(unitNo,loggedInUser);
                responder.setDataObject(hmRuleData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == GET_DEFINE_RULE_DATA){
                rulesTxnBean = new RulesTxnBean(loggedInUser);
                CoeusVector cvRuleData = new CoeusVector();
                
                CoeusVector cvData = (CoeusVector)requester.getDataObject();
                char mode = ((Character)cvData.get(0)).charValue();
                
                if(mode == TypeConstants.ADD_MODE){
                    //Get the new Rule Id
                    Integer ruleID = rulesTxnBean.getNextRuleID();
                    cvRuleData.add(ruleID);
                }else{
                    int ruleId = ((Integer)cvData.get(1)).intValue();
                    
                    //Get the Rule Details data
                    CoeusVector cvRuleDetails = rulesTxnBean.getRuleDetails(ruleId);
                    cvRuleData.add(cvRuleDetails == null ? new CoeusVector() : cvRuleDetails);
                    
                    //Get the Rule Conditions data
                    CoeusVector cvRuleCond = rulesTxnBean.getRuleConditions(ruleId);
                    cvRuleData.add(cvRuleCond == null ? new CoeusVector() : cvRuleCond);
                    
                    //Get the Rule Expressions data
                    CoeusVector cvRuleExp = rulesTxnBean.getRuleExpression(ruleId);
                    cvRuleData.add(cvRuleExp == null ? new CoeusVector() : cvRuleExp);
                    
                    //Get the Rule Function Arguments data
                    CoeusVector cvFuncArgs = rulesTxnBean.getRuleFuncArgs(ruleId);
                    cvRuleData.add(cvFuncArgs == null ? new CoeusVector() : cvFuncArgs);
                    
                }
                //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                //Get all the questions
                QuestionnaireTxnBean questionnaireTxnBean = new QuestionnaireTxnBean();
                CoeusVector cvQuestionnaireQuestions = questionnaireTxnBean.getQuestions();
                cvRuleData.add(cvQuestionnaireQuestions);
                //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                responder.setDataObject(cvRuleData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == GET_CONDITION_EXPRESSIONS){
                //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                Vector serverObjects = requester.getDataObjects();
                String moduleCode = (String)serverObjects.get(0);
                String subModuleCode = (String)serverObjects.get(1);
                //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                rulesTxnBean = new RulesTxnBean(loggedInUser);
                CoeusVector cvData = new CoeusVector();
                
                //Get the BusinessRuleVariables data
                //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                //rule variables are fetched based on the module code
                CoeusVector cvBusinessRuleVariables = (CoeusVector)rulesTxnBean.
                        getBusinessRuleVariables(moduleCode, subModuleCode);
                //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                cvData.addElement(cvBusinessRuleVariables == null ? new CoeusVector() :cvBusinessRuleVariables);//0
                
                //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                //Get the BusinessRule Questionnaire data
                CoeusVector cvYNQData =(CoeusVector)rulesTxnBean.getYNQ();
//                CoeusVector cvYNQData = rulesTxnBean.getYNQ("P");
                cvData.addElement(cvYNQData == null ? new CoeusVector() : cvYNQData);//1
                QuestionnaireTxnBean questionnaireTxnBean = new QuestionnaireTxnBean();
                Vector vecQuestionnaireUsage = questionnaireTxnBean.getUsageForModule(
                        Integer.parseInt(moduleCode), Integer.parseInt(subModuleCode));
                QuestionnaireUsageBean questionnaireUsageBean = null;
                CoeusVector cvQuestionnaire = new CoeusVector();
                Map hmQuestionnaireQuestions = new HashMap();
                if(vecQuestionnaireUsage!=null){
                    for(int i=0; i<vecQuestionnaireUsage.size(); i++){
                        questionnaireUsageBean = (QuestionnaireUsageBean)vecQuestionnaireUsage.get(i);
                        cvQuestionnaire.add(questionnaireTxnBean.getQuestionnaireData(questionnaireUsageBean.getQuestionnaireId()));
                        hmQuestionnaireQuestions.put(Integer.toString(questionnaireUsageBean.getQuestionnaireId()),
                                // COEUSDEV-252: User message field should be disabled in condition editor for Question rules - Start
//                                questionnaireTxnBean.getDistinctQuestionareQuestions(questionnaireUsageBean.getQuestionnaireId()));
                                questionnaireTxnBean.getDistinctQuestionareQuestions(questionnaireUsageBean.getQuestionnaireId(),
                                    questionnaireUsageBean.getQuestionnaireVersionNumber()));
                                // COEUSDEV-252: User message field should be disabled in condition editor for Question rules - End
                    }
                }
                cvData.addElement(cvQuestionnaire == null ? new CoeusVector() : cvQuestionnaire);//2
                cvData.addElement(hmQuestionnaireQuestions);//3
                //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                
                //Get the BusinessRuleFunctionList
                //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                //rule functions are fetched based on the module code and subModuleCode
                CoeusVector cvFunctionName = (CoeusVector)rulesTxnBean.getBusinessRuleFunctionList(moduleCode, subModuleCode);
                //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                cvData.addElement(cvFunctionName == null ? new CoeusVector():cvFunctionName);//4
                
                //Get the BusinessRule FunctionDescription by passing the FunctionName
                CoeusVector cvFuncDesc = new CoeusVector();
                if(cvFunctionName != null && cvFunctionName.size() > 0){
                    cvFuncDesc = (CoeusVector)rulesTxnBean.getRuleFuncDesc(cvFunctionName);
                    cvData.addElement(cvFuncDesc);//5
                }
                //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                else{
                    cvData.addElement(null);//5
                }
                //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                responder.setDataObject(cvData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType==UPDATE_META_RULE_DATA){
                boolean success = false;
                ruleUpdateTxnBean = new RuleUpdateTxnBean(loggedInUser);
                HashMap hmClinetData = (HashMap)requester.getDataObject();
                unitNumber = (String)requester.getId();
                success = ruleUpdateTxnBean.addUpdDelBusinessRules(hmClinetData);
                if(success){
                    Hashtable hmRuleData = getRuleData(unitNumber,loggedInUser);
                    responder.setDataObject(hmRuleData);
                    responder.setMessage(null);
                    responder.setResponseStatus(true);
                }
            }else if(functionType==GET_META_RULE_ID){
                int metaRuleId = 0;
                rulesTxnBean = new RulesTxnBean(loggedInUser);
                metaRuleId = rulesTxnBean.getMetaRuleId();
                responder.setDataObject(new Integer(metaRuleId));
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == GET_YNQ_EXPLANATION){
                String questionId = (String)requester.getDataObject();
                
                rulesTxnBean = new RulesTxnBean(loggedInUser);
                CoeusVector cvYNQExp = rulesTxnBean.getYNQExplanation(questionId);
                
                responder.setDataObject(cvYNQExp == null ? new CoeusVector() : cvYNQExp);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == CHECK_FOR_ARGS){
                String functionName = (String)requester.getDataObject();
                rulesTxnBean = new RulesTxnBean(loggedInUser);
                Integer argPresent = rulesTxnBean.checkForArgs(functionName);
                
                responder.setDataObject(argPresent);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == GET_ARG_INFO){
                String functionName = (String)requester.getDataObject();
                rulesTxnBean = new RulesTxnBean(loggedInUser);
                CoeusVector cvData = rulesTxnBean.getArgumentInfo(functionName);
                if(cvData == null){
                    cvData = new CoeusVector();
                }
                responder.setDataObject(cvData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == SAVE_RULE_CONDITIONS){
                HashMap hmData  = (HashMap)requester.getDataObject();
                ruleUpdateTxnBean = new RuleUpdateTxnBean(loggedInUser);
                boolean sucess = ruleUpdateTxnBean.addUpdDelCondtions(hmData);
                
                responder.setDataObject(null);
                responder.setMessage(null);
                responder.setResponseStatus(sucess);
            }else if(functionType == GET_BUSINESS_RULE_DATA){
                unitNumber = (String)requester.getDataObject();
                rulesTxnBean = new RulesTxnBean(loggedInUser);
                CoeusVector cvBusinessRule = rulesTxnBean.getAllRules(unitNumber);
                responder.setDataObjects(cvBusinessRule);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == GET_DATATYPE_FOR_VALIDATION){
                String varName = (String)requester.getDataObject();
                rulesTxnBean = new RulesTxnBean(loggedInUser);
                String dataType = rulesTxnBean.getDataType(varName);
                
                responder.setDataObject(dataType);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }
        } catch( CoeusException coeusEx ) {
            //coeusEx.printStackTrace();
//            int index=0;
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
                    "RulesMaintenenceServlet", "doPost");
            
        } catch( DBException dbEx ) {
            //dbEx.printStackTrace();
//            int index=0;
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
                    "RulesMaintenenceServlet", "doPost");
            
        } catch(Exception e) {
            //e.printStackTrace();
            responder.setResponseStatus(false);
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e,
                    "RulesMaintenenceServlet", "doPost");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "RulesMaintenenceServlet", "doPost");
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
                        "RulesMaintenenceServlet", "doPost");
            }
        }
    }
    
    
    private Hashtable getRuleData(String unitNumber,String loggedInUser)
    throws  DBException, CoeusException,Exception{
        Hashtable hmRuleData = new Hashtable();
        RulesTxnBean rulesTxnBean = null;
        rulesTxnBean = new RulesTxnBean(loggedInUser);
        // Get the Meta Rule data which contains description
        CoeusVector cvMetaRuleData = rulesTxnBean.getMetaRuleIdForUnit(unitNumber);
        hmRuleData.put(MetaRuleBean.class,cvMetaRuleData==null ? new CoeusVector() : cvMetaRuleData);
        // Get all the Business Rules data
        CoeusVector cvBusinessRule = rulesTxnBean.getAllRules(unitNumber);
        hmRuleData.put(BusinessRuleBean.class,cvBusinessRule==null ? new CoeusVector() : cvBusinessRule);
        // Get all the meta rules details - tree data for the individual meta rule Id
        CoeusVector cvMetaRuleDetail = null;
        CoeusVector cvData = new CoeusVector();
        if(cvMetaRuleData!= null && cvMetaRuleData.size() > 0){
            for(int index = 0; index < cvMetaRuleData.size(); index++){
                MetaRuleBean metaRuleBean = (MetaRuleBean)cvMetaRuleData.get(index);
                cvMetaRuleDetail = rulesTxnBean.getMetaRuleDetails(metaRuleBean.getMetaRuleId());
                if(cvMetaRuleDetail!= null && cvMetaRuleDetail.size() > 0){
                    cvData.addAll(cvMetaRuleDetail);
                }
            }
        }
        // Get the rights data - start
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        boolean  addBusinessRule = userMaintDataTxnBean.getUserHasRight(loggedInUser, ADD_BUSINESS_RULE,unitNumber);
        boolean  modifyBusinessRule = userMaintDataTxnBean.getUserHasRight(loggedInUser, MODIFY_BUSINESS_RULE,unitNumber);
        boolean  deleteBusinessRule = userMaintDataTxnBean.getUserHasRight(loggedInUser,DELETE_BUSINESS_RULE, unitNumber);
        // Get the rights data - End
        //Update the Rights to the Hashtable - start
        hmRuleData.put(CoeusConstants.ADD_BUSINESS_RULE,new Boolean(addBusinessRule));
        hmRuleData.put(CoeusConstants.MODIFY_BUSINESS_RULE,new Boolean(modifyBusinessRule));
        hmRuleData.put(CoeusConstants.DELETE_BUSINESS_RULE,new Boolean(deleteBusinessRule));
        //Update the Rights to the Hashtable - End
        
        // Put the tree data.
        hmRuleData.put(MetaRuleDetailBean.class,cvData==null ? new CoeusVector() : cvData);
        //Added for Coeus4.3 enhancement PT ID:2785 - start
        QuestionnaireTxnBean questionaireTxnBean = new QuestionnaireTxnBean();
        //Code commented and modified for Case#3875 - Need to add Annual COI as a Module for Questionnaire maintenance
        //CoeusVector cvModules = questionaireTxnBean.getModuleData();
        CoeusVector cvModules = questionaireTxnBean.getModuleData(false);
        hmRuleData.put(ModuleDataBean.class, cvModules==null ? new CoeusVector() : cvModules);
        CoeusVector cvSubModules = questionaireTxnBean.getSubModuleData();
        hmRuleData.put(SubModuleDataBean.class, cvSubModules==null ? new CoeusVector() : cvSubModules);
        //Added for Coeus4.3 enhancement PT ID:2785 - end
        return hmRuleData;
    }//End getRuleData
}
