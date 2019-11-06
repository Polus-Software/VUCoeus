/*
 * @(#)RulesTxnBean.java 1.0 10/10/05 5:03 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 20-AUG-2007
 * by Leena
 */

package edu.mit.coeus.mapsrules.bean;

import edu.mit.coeus.admin.bean.YNQBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.question.bean.YNQExplanationBean;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author  chandrashekar
 */
public class RulesTxnBean {
    
    private DBEngineImpl dbEngine;
    
    private static final String DSN = "Coeus";
    
//    private Timestamp dbTimestamp;
    
    
    // holds the userId for the logged in user
    private String userId;
    
    /** Creates a new instance of RulesTxnBean */
    public RulesTxnBean() {
        dbEngine = new DBEngineImpl();
    }
    
    public RulesTxnBean(String userId) {
        this();
        this.userId = userId;
    }
    
    public CoeusVector getMetaRuleIdForUnit(String unitNumber) throws CoeusException,DBException{
        Vector param = new Vector();
        Vector result = null;
        //String ruleId = null;
        param.addElement(new Parameter("UNIT_NO",DBEngineConstants.TYPE_STRING,unitNumber));
        if(dbEngine != null) {
            result = dbEngine.executeRequest(DSN,"call DW_GET_METARULES_UNIT(<< UNIT_NO >>, "+
                    "<< OUT RESULTSET rset >>) ", DSN,param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        MetaRuleBean metaRuleBean = null;
        HashMap rowData = null;
        CoeusVector cvMetaRuleData= new CoeusVector();
        if(result != null && result.size() > 0) {
            for(int index = 0; index < result.size(); index++){
                rowData = (HashMap)result.get(index);
                metaRuleBean = new MetaRuleBean();
                metaRuleBean.setMetaRuleId(rowData.get("META_RULE_ID")== null ? null : rowData.get("META_RULE_ID").toString());
                metaRuleBean.setMetaRuleType((String)rowData.get("META_RULE_TYPE"));
                metaRuleBean.setDescription(rowData.get("DESCRIPTION")== null ? null : rowData.get("DESCRIPTION").toString());
                metaRuleBean.setUnitNumber(rowData.get("UNIT_NUMBER")== null ? null : rowData.get("UNIT_NUMBER").toString());
                metaRuleBean.setUpdateTimestamp(((Timestamp)rowData.get("UPDATE_TIMESTAMP")));
                metaRuleBean.setUpdateUser((String)rowData.get("UPDATE_USER"));
                //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                //Added a new column in the db for module_code
                metaRuleBean.setModuleCode(rowData.get("MODULE_CODE").toString());
                //metaRuleBean.setSubmoduleCode(rowData.get("SUB_MODULE_CODE").toString());
                metaRuleBean.setSubmoduleCode(rowData.get("SUB_MODULE_CODE")==null?null:rowData.get("SUB_MODULE_CODE").toString() );
                //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                cvMetaRuleData.addElement(metaRuleBean);
            }
        }
        return cvMetaRuleData;
    }
    
    public CoeusVector getMetaRuleDetails(String metaRuleId) throws CoeusException,DBException {
        
        Vector param = new Vector();
        Vector result = null;
        CoeusVector metaRuleDetails = null;
        param.add(new Parameter("META_RULE_ID",DBEngineConstants.TYPE_INT,metaRuleId));
        if(dbEngine != null) {
            result = dbEngine.executeRequest(DSN,"call DW_GET_METARULE_DETAILS(<< META_RULE_ID >>, "+
                    "<< OUT RESULTSET rset >>)",DSN,param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result != null && result.size() > 0) {
            HashMap rowData = null;
            metaRuleDetails = new CoeusVector();
            MetaRuleDetailBean metaRuleDetailBean = null;
            for(int index=0; index< result.size(); index++) {
                rowData = (HashMap)result.get(index);
                metaRuleDetailBean = new MetaRuleDetailBean();
                metaRuleDetailBean.setDescription((String)rowData.get("DESCRIPTION"));
                metaRuleDetailBean.setMetaRuleId(rowData.get("META_RULE_ID")== null ? null : rowData.get("META_RULE_ID").toString());
                metaRuleDetailBean.setNodeId(rowData.get("NODE_NUMBER")== null ? null : rowData.get("NODE_NUMBER").toString());
                metaRuleDetailBean.setRuleId(rowData.get("RULE_ID")== null ? null : rowData.get("RULE_ID").toString());
                metaRuleDetailBean.setParentNodeId(rowData.get("PARENT_NODE")== null ? null : rowData.get("PARENT_NODE").toString());
                metaRuleDetailBean.setNextNode(rowData.get("NEXT_NODE")== null ? null : rowData.get("NEXT_NODE").toString());
                metaRuleDetailBean.setNodeIfTrue(rowData.get("NODE_IF_TRUE")== null ? null : rowData.get("NODE_IF_TRUE").toString());
                metaRuleDetailBean.setNodeIfFalse(rowData.get("NODE_IF_FALSE")== null ? null : rowData.get("NODE_IF_FALSE").toString());
                metaRuleDetailBean.setUpdateTimeStamp((Timestamp)rowData.get("UPDATE_TIMESTAMP"));
                metaRuleDetailBean.setUpdateUser((String)rowData.get("UPDATE_USER"));
                metaRuleDetails.addElement(metaRuleDetailBean);
            }
        }
        return metaRuleDetails;
    }
    
    
//        Vector param = new Vector();
//        Vector result = null;
//        CoeusVector metaRuleDetails = null;
//        int metaRuleID  = Integer.parseInt(metaRuleId);
//        param.add(new Parameter("META_RULE_ID",DBEngineConstants.TYPE_INT,""+metaRuleID));
//        if(dbEngine != null) {
//            result = dbEngine.executeRequest(DSN,"call DW_GET_METARULE_DETAILS(<< META_RULE_ID >>, "+
//                        "<< OUT RESULTSET rset >>)",DSN,param);
//        } else {
//            throw new CoeusException("db_exceptionCode.1000");
//        }
//        if(result != null && result.size() > 0) {
//            HashMap rowData = null;
//            metaRuleDetails = new CoeusVector();
//            //MetaRuleDetailBean metaRuleDetailBean = null;
//            for(int index=0; index< result.size(); index++) {
//                rowData = (HashMap)result.get(index);
//                MetaRuleDetailBean metaRuleDetailBean = new MetaRuleDetailBean();
//                metaRuleDetailBean.setMetaRuleId(rowData.get("META_RULE_ID")== null ? null : rowData.get("META_RULE_ID").toString());
//                metaRuleDetailBean.setNodeId(rowData.get("NODE_NUMBER")== null ? null : rowData.get("NODE_NUMBER").toString());
//                metaRuleDetailBean.setRuleId(rowData.get("RULE_ID")== null ? null : rowData.get("RULE_ID").toString());
//                metaRuleDetailBean.setParentNodeId(rowData.get("PARENT_NODE")== null ? null : rowData.get("PARENT_NODE").toString());
//                metaRuleDetailBean.setNextNode(rowData.get("NEXT_NODE")== null ? null : rowData.get("NEXT_NODE").toString());
//                metaRuleDetailBean.setNodeIfTrue(rowData.get("NODE_IF_TRUE")== null ? null : rowData.get("NODE_IF_TRUE").toString());
//                metaRuleDetailBean.setNodeIfFalse(rowData.get("NODE_IF_FALSE")== null ? null : rowData.get("NODE_IF_FALSE").toString());
//                metaRuleDetailBean.setUpdateTimestamp((Timestamp)rowData.get("UPDATE_TIMESTAMP"));
//                metaRuleDetailBean.setUpdateUser((String)rowData.get("UPDATE_USER"));
//                metaRuleDetailBean.setDiscription((String)rowData.get("DESCRIPTION"));
//            }
//        }
//        return metaRuleDetails;
//    }
    
    public CoeusVector getAllRules(String unitNumber)throws CoeusException,DBException {
        Vector param = new Vector();
        Vector result = null;
        CoeusVector cvBusinessRule = null;
        param.addElement(new Parameter("UNIT_NO",DBEngineConstants.TYPE_STRING,unitNumber));
        if(dbEngine != null) {
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
//            result = dbEngine.executeRequest(DSN,"call dw_get_allrules(<< UNIT_NO >>, "+
//                                "<< OUT RESULTSET rset >>) ", DSN,param);
            result = dbEngine.executeRequest(DSN,"call get_all_rules(<< UNIT_NO >>, "+
                    "<< OUT RESULTSET rset >>) ", DSN,param);
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(result!= null && result.size() > 0){
            HashMap rowData = null;
            cvBusinessRule = new CoeusVector();
            BusinessRuleBean businessRuleBean = null;
            for(int index=0; index< result.size(); index++) {
                rowData = (HashMap)result.get(index);
                businessRuleBean = new BusinessRuleBean();
                businessRuleBean.setRuleId(rowData.get("RULE_ID")== null ? null : rowData.get("RULE_ID").toString());
                businessRuleBean.setRuleType((String)rowData.get("RULE_TYPE"));
                businessRuleBean.setUnitNumber(rowData.get("UNIT_NUMBER")== null ? null : rowData.get("UNIT_NUMBER").toString());
                businessRuleBean.setDescription(rowData.get("DESCRIPTION")== null ? null : rowData.get("DESCRIPTION").toString());
                businessRuleBean.setUpdateTimestamp((Timestamp)rowData.get("UPDATE_TIMESTAMP"));
                businessRuleBean.setUpdateUser((String)rowData.get("UPDATE_USER"));
                //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                businessRuleBean.setModuleCode(rowData.get("MODULE_CODE").toString());
                businessRuleBean.setSubmoduleCode(rowData.get("SUB_MODULE_CODE")==null? null:rowData.get("SUB_MODULE_CODE").toString());
                businessRuleBean.setRuleCategory(rowData.get("RULE_CATEGORY")==null? null: rowData.get("RULE_CATEGORY").toString());
                //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                cvBusinessRule.addElement(businessRuleBean);
            }
        }
        return cvBusinessRule;
    }
    
    /**
     *  This method used get next Rule ID
     *  <li>To fetch the data, it uses the function FN_GET_RULE_ID.
     *
     *  @return int next Rule ID
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Integer getNextRuleID()throws CoeusException, DBException {
        Integer ruleId = null;
        Vector param = new Vector();
        Vector result = new Vector();
        
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT STRING RULE_ID>> = "
                    +" call FN_GET_RULE_ID() }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            ruleId = new Integer(rowParameter.get("RULE_ID").toString());
        }
        if(ruleId == null){
            ruleId = new Integer(0);
        }
        return ruleId;
    }//End getNextRuleID
    
    /** Method used to get Bussiness Rule Details for the rule ID.
     * <li>To fetch the data, it uses DW_GET_RULE_DETAILS.
     *
     * @return CoeusVector of BusinessRuleBean
     * @param ruleId is used to get BusinessRuleBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getRuleDetails(int ruleId)
    throws CoeusException,DBException {
        Vector param = new Vector();
        Vector result = null;
        CoeusVector cvRuleDetails = null;
        param.addElement(new Parameter("RULE_ID",
                DBEngineConstants.TYPE_INT,""+ruleId));
        
        if(dbEngine != null) {
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
//            result = dbEngine.executeRequest(DSN,"call DW_GET_RULE_DETAILS(<< RULE_ID >>, "+
//                    "<< OUT RESULTSET rset >>) ", DSN,param);
            result = dbEngine.executeRequest(DSN,"call GET_RULE_DETAILS(<< RULE_ID >>, "+
                    "<< OUT RESULTSET rset >>) ", DSN,param);
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(result!= null && result.size() > 0){
            HashMap rowData = null;
            cvRuleDetails = new CoeusVector();
            BusinessRuleBean businessRuleBean = null;
            for(int index=0; index< result.size(); index++) {
                rowData = (HashMap)result.get(index);
                businessRuleBean = new BusinessRuleBean();
                businessRuleBean.setRuleId(
                        rowData.get("RULE_ID")== null ? null : rowData.get("RULE_ID").toString());
                businessRuleBean.setRuleType((String)
                rowData.get("RULE_TYPE"));
                //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                businessRuleBean.setModuleCode(rowData.get("MODULE_CODE").toString());
                businessRuleBean.setSubmoduleCode(rowData.get("SUB_MODULE_CODE")==null? null:rowData.get("SUB_MODULE_CODE").toString());
                businessRuleBean.setRuleCategory(rowData.get("RULE_CATEGORY")==null? null:rowData.get("RULE_CATEGORY").toString());
                //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                businessRuleBean.setUnitNumber(
                        rowData.get("UNIT_NUMBER")== null ? null : rowData.get("UNIT_NUMBER").toString());
                businessRuleBean.setDescription(
                        rowData.get("DESCRIPTION")== null ? null : rowData.get("DESCRIPTION").toString());
                businessRuleBean.setUpdateTimestamp(
                        (Timestamp)rowData.get("UPDATE_TIMESTAMP"));
                businessRuleBean.setUpdateUser(
                        (String)rowData.get("UPDATE_USER"));
                cvRuleDetails.addElement(businessRuleBean);
            }
        }
        return cvRuleDetails;
    }//End getRuleDetails
    
    /** Method used to get Bussiness Rule Conditions for the rule ID.
     * <li>To fetch the data, it uses GET_RULE_CONDITIONS.
     *
     * @return CoeusVector of BusinessRuleConditionsBean
     * @param ruleId is used to get BusinessRuleConditionsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getRuleConditions(int ruleId)
    throws CoeusException,DBException {
        Vector param = new Vector();
        Vector result = null;
        CoeusVector cvRuleConditions = null;
        param.addElement(new Parameter("RULE_ID",
                DBEngineConstants.TYPE_INT,""+ruleId));
        
        if(dbEngine != null) {
            result = dbEngine.executeRequest(DSN,"call GET_RULE_CONDITIONS(<< RULE_ID >>, "+
                    "<< OUT RESULTSET rset >>) ", DSN,param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(result!= null && result.size() > 0){
            HashMap rowData = null;
            cvRuleConditions = new CoeusVector();
            BusinessRuleConditionsBean businessRuleConditionsBean = null;
            for(int index=0; index< result.size(); index++) {
                rowData = (HashMap)result.get(index);
                businessRuleConditionsBean = new BusinessRuleConditionsBean();
                
                businessRuleConditionsBean.setRuleId(
                        Integer.parseInt(rowData.get("RULE_ID").toString()));
                
                businessRuleConditionsBean.setConditionNumber(
                        Integer.parseInt(rowData.get("CONDITION_NUMBER").toString()));
                
                businessRuleConditionsBean.setAwConditionNumber(
                        Integer.parseInt(rowData.get("CONDITION_NUMBER").toString()));
                
                businessRuleConditionsBean.setConditionSequence(
                        Integer.parseInt(rowData.get("CONDITION_SEQUENCE").toString()));
                
                businessRuleConditionsBean.setAction(
                        Integer.parseInt(rowData.get("ACTION").toString()));
                
                businessRuleConditionsBean.setRuleDescription(
                        rowData.get("RULE_DESCRIPTION")== null ? "" : rowData.get("RULE_DESCRIPTION").toString());
                
                businessRuleConditionsBean.setMapDescription(
                        rowData.get("MAP_DESCRIPTION")== null ? "" : rowData.get("MAP_DESCRIPTION").toString());
                
                businessRuleConditionsBean.setConditionExp(
                        rowData.get("CONDITION_EXP")== null ? "" : rowData.get("CONDITION_EXP").toString());
                
                businessRuleConditionsBean.setUpdateTimestamp(
                        (Timestamp)rowData.get("UPDATE_TIMESTAMP"));
                
                businessRuleConditionsBean.setUpdateUser(
                        (String)rowData.get("UPDATE_USER"));
                //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
                businessRuleConditionsBean.setUserMessage(
                        (String)rowData.get("USER_MESSAGE"));
                //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
                cvRuleConditions.addElement(businessRuleConditionsBean);
            }
        }
        return cvRuleConditions;
    }//End getRuleConditions
    
    
    /** Method used to get Bussiness Rule Expression for the rule ID.
     * <li>To fetch the data, it uses GET_RULE_EXPRESSION.
     *
     * @return CoeusVector of BusinessRuleExpBean
     * @param ruleId is used to get BusinessRuleExpBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getRuleExpression(int ruleId)
    throws CoeusException,DBException {
        Vector param = new Vector();
        Vector result = null;
        CoeusVector cvRuleExp = null;
        param.addElement(new Parameter("RULE_ID",
                DBEngineConstants.TYPE_INT,""+ruleId));
        
        if(dbEngine != null) {
            //Modified for case 2418 - Rule Evaluation bug - start
//            result = dbEngine.executeRequest(DSN,"call DW_GET_RULE_EXPRESSION(<< RULE_ID >>, "+
//                    "<< OUT RESULTSET rset >>) ", DSN,param);
            result = dbEngine.executeRequest(DSN,"call GET_RULE_EXPRESSIONS(<< RULE_ID >>, "+
                    "<< OUT RESULTSET rset >>) ", DSN,param);
            //Modified for case 2418 - Rule Evaluation bug - end
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(result!= null && result.size() > 0){
            HashMap rowData = null;
            cvRuleExp = new CoeusVector();
            BusinessRuleExpBean businessRuleExpBean= null;
            for(int index=0; index< result.size(); index++) {
                rowData = (HashMap)result.get(index);
                businessRuleExpBean = new BusinessRuleExpBean();
                
                businessRuleExpBean.setRuleId(
                        Integer.parseInt(rowData.get("RULE_ID").toString()));
                
                businessRuleExpBean.setLvalue(
                        rowData.get("LVALUE")== null ? "" : rowData.get("LVALUE").toString());
                
                businessRuleExpBean.setOperator(
                        rowData.get("OPERATOR")== null ? "" : rowData.get("OPERATOR").toString());
                
                businessRuleExpBean.setRvalue(
                        rowData.get("RVALUE")== null ? "" : rowData.get("RVALUE").toString());
                
                businessRuleExpBean.setLogicalOperator(
                        rowData.get("LOGICAL_OPERATOR")== null ? "" : rowData.get("LOGICAL_OPERATOR").toString());
                
                // Added with COEUSQA-2458-Make 'and' and 'or' work in business rules
                businessRuleExpBean.setExpressionPrefix((String)rowData.get("EXPRESSION_PREFIX"));
                
                businessRuleExpBean.setExpressionSuffix((String)rowData.get("EXPRESSION_SUFFIX"));
                // COEUSQA-2458-End
                
                businessRuleExpBean.setExpressionNumber(
                        Integer.parseInt(rowData.get("EXPRESSION_NUMBER").toString()));
                
                businessRuleExpBean.setAwExpressionNumber(
                        Integer.parseInt(rowData.get("EXPRESSION_NUMBER").toString()));
                
                businessRuleExpBean.setConditionNumber(
                        Integer.parseInt(rowData.get("CONDITION_NUMBER").toString()));
                
                businessRuleExpBean.setAwConditionNumber(
                        Integer.parseInt(rowData.get("CONDITION_NUMBER").toString()));
                
                businessRuleExpBean.setExpressionType(
                        rowData.get("EXPRESSION_TYPE")== null ? null : rowData.get("EXPRESSION_TYPE").toString());
                
                businessRuleExpBean.setNoOfAnswers(
                        rowData.get("NO_OF_ANSWERS") == null ? 0 : Integer.parseInt(rowData.get("NO_OF_ANSWERS").toString()));
                
                businessRuleExpBean.setUpdateTimestamp(
                        (Timestamp)rowData.get("UPDATE_TIMESTAMP"));
                
                businessRuleExpBean.setUpdateUser(
                        (String)rowData.get("UPDATE_USER"));
                
                cvRuleExp.addElement(businessRuleExpBean);
            }
        }
        return cvRuleExp;
    }//End getRuleExpression
    
    
    /** Method used to get Bussiness Rule Expression for the rule ID.
     * <li>To fetch the data, it uses GET_FUNCTION_ARGS.
     *
     * @return CoeusVector of BusinessRuleFuncArgsBean
     * @param ruleId is used to get BusinessRuleFuncArgsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getRuleFuncArgs(int ruleId)
    throws CoeusException,DBException {
        Vector param = new Vector();
        Vector result = null;
        CoeusVector cvRuleFuncArgs = null;
        
        param.addElement(new Parameter("RULE_ID",
                DBEngineConstants.TYPE_INT,""+ruleId));
        
        if(dbEngine != null) {
            result = dbEngine.executeRequest(DSN,"call GET_FUNCTION_ARGS(<< RULE_ID >>, "+
                    "<< OUT RESULTSET rset >>) ", DSN,param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(result!= null && result.size() > 0){
            HashMap rowData = null;
            cvRuleFuncArgs = new CoeusVector();
            BusinessRuleFuncArgsBean businessRuleFuncArgsBean = null;
            for(int index=0; index< result.size(); index++) {
                rowData = (HashMap)result.get(index);
                businessRuleFuncArgsBean = new BusinessRuleFuncArgsBean();
                
                businessRuleFuncArgsBean.setRuleFuncDescription(
                        rowData.get("RULE_FUNC_DESCRIPTION")== null ? "" : rowData.get("RULE_FUNC_DESCRIPTION").toString());
                
                businessRuleFuncArgsBean.setArgumentType(
                        rowData.get("ARGUMENT_TYPE")== null ? "" : rowData.get("ARGUMENT_TYPE").toString());
                
                businessRuleFuncArgsBean.setDefaultValue(
                        rowData.get("DEFAULT_VALUE")== null ? "" : rowData.get("DEFAULT_VALUE").toString());
                
                businessRuleFuncArgsBean.setWindowName(
                        rowData.get("WINDOW_NAME")== null ? "" : rowData.get("WINDOW_NAME").toString());
                
                businessRuleFuncArgsBean.setRuleId(
                        Integer.parseInt(rowData.get("SEQUENCE_NUMBER").toString()));
                
                businessRuleFuncArgsBean.setArgumentName(
                        rowData.get("ARGUMENT_NAME")== null ? "" : rowData.get("ARGUMENT_NAME").toString());
                
                businessRuleFuncArgsBean.setAwArgumentName(
                        rowData.get("ARGUMENT_NAME")== null ? "" : rowData.get("ARGUMENT_NAME").toString());
                
                businessRuleFuncArgsBean.setValue(
                        rowData.get("VALUE")== null ? "" : rowData.get("VALUE").toString());
                
                businessRuleFuncArgsBean.setRuleExpDescription(
                        rowData.get("RULE_EXP_DESCRIPTION")== null ? "" : rowData.get("RULE_EXP_DESCRIPTION").toString());
                
                businessRuleFuncArgsBean.setRuleId(
                        Integer.parseInt(rowData.get("RULE_ID").toString()));
                
                businessRuleFuncArgsBean.setConditionNumber(
                        Integer.parseInt(rowData.get("CONDITION_NUMBER").toString()));
                
                businessRuleFuncArgsBean.setAwConditionNumber(
                        Integer.parseInt(rowData.get("CONDITION_NUMBER").toString()));
                
                businessRuleFuncArgsBean.setExpressionNumber(
                        Integer.parseInt(rowData.get("EXPRESSION_NUMBER").toString()));
                
                businessRuleFuncArgsBean.setAwExpressionNumber(
                        Integer.parseInt(rowData.get("EXPRESSION_NUMBER").toString()));
                
                businessRuleFuncArgsBean.setFunctionName(
                        rowData.get("FUNCTION_NAME")== null ? "" : rowData.get("FUNCTION_NAME").toString());
                
                businessRuleFuncArgsBean.setAwFunctionName(
                        rowData.get("FUNCTION_NAME")== null ? "" : rowData.get("FUNCTION_NAME").toString());
                
                
                businessRuleFuncArgsBean.setUpdateTimestamp(
                        (Timestamp)rowData.get("UPDATE_TIMESTAMP"));
                
                businessRuleFuncArgsBean.setUpdateUser(
                        (String)rowData.get("UPDATE_USER"));
                
                cvRuleFuncArgs.addElement(businessRuleFuncArgsBean);
            }
        }
        return cvRuleFuncArgs;
    }//End getRuleFuncArgs
    
    
    /** Method is used to get Rule Variables Data
     * <li>To fetch the data, it uses dw_get_rule_variables
     *
     * @return CoeusVector of BusinessRuleVariableBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    
    public CoeusVector getBusinessRuleVariables(String moduleCode, String subModuleCode)throws CoeusException,DBException {
        Vector result = null;
        Vector param= new Vector();
        CoeusVector cvRuleVariable = null;
        if(dbEngine != null) {
            // result = dbEngine.executeRequest(DSN,"call dw_get_rule_variables( << OUT RESULTSET rset >>) ");
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
//            result = dbEngine.executeRequest(DSN,
//                    "call dw_get_rule_variables( <<OUT RESULTSET rset>> )", "Coeus", param);
            param.addElement(new Parameter("MODULE_CODE",
                    DBEngineConstants.TYPE_INT,moduleCode));
            param.addElement(new Parameter("SUB_MODULE_CODE",
                    DBEngineConstants.TYPE_INT,subModuleCode));
            result = dbEngine.executeRequest(DSN,
                    "call get_rule_variables_for_module(<<MODULE_CODE>>, <<SUB_MODULE_CODE>>, <<OUT RESULTSET rset>> )", "Coeus", param);
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(result!= null && result.size() > 0){
            HashMap rowData = null;
            cvRuleVariable = new CoeusVector();
            BusinessRuleVariableBean  businessRuleVariableBean= null;
            for(int index=0; index< result.size(); index++) {
                rowData = (HashMap)result.get(index);
                businessRuleVariableBean = new BusinessRuleVariableBean();
                businessRuleVariableBean.setVariableName((String)
                rowData.get("VARIABLE_NAME"));
                businessRuleVariableBean.setVariableDescription((String)
                rowData.get("DESCRIPTION"));
                businessRuleVariableBean.setDataType((String)
                rowData.get("DATA_TYPE"));
                businessRuleVariableBean.setColumnName((String)
                rowData.get("COLUMN_NAME"));
                businessRuleVariableBean.setUpdateUser((String)
                rowData.get("UPDATE_USER"));
                businessRuleVariableBean.setUpdateTimestamp((Timestamp)
                rowData.get("UPDATE_TIMESTAMP"));
                cvRuleVariable.addElement(businessRuleVariableBean);
            }
        }
        return cvRuleVariable;
    }// End getBusinessRuleVariables
    
    
    /** Method is used to get YNQ Data
     * <li>To fetch the data, it uses DW_GET_YNQ_LIST
     * @param questionType question type of the question
     * @return CoeusVector of ynqBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    
    public CoeusVector getYNQ()throws CoeusException,DBException {
        Vector result = null;
        Vector param= new Vector();
        CoeusVector cvYNQData = null;
         
        if(dbEngine != null) {
            result = dbEngine.executeRequest(DSN,
                    "call dw_get_all_ynq( <<OUT RESULTSET rset>> )", DSN, param);     
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(result!= null && result.size() > 0){
            HashMap rowData = null;
            cvYNQData = new CoeusVector();
            YNQBean ynqBean = null;
            for(int index=0; index< result.size(); index++) {
                rowData = (HashMap)result.get(index);
                ynqBean = new YNQBean();
                ynqBean.setQuestionId((String)
                rowData.get("QUESTION_ID"));
                ynqBean.setDescription((String)
                rowData.get("DESCRIPTION"));
                ynqBean.setQuestionType((String)
                rowData.get("QUESTION_TYPE"));
                ynqBean.setNoOfAnswers(
                        rowData.get("NO_OF_ANSWERS") == null ? 0 : Integer.parseInt(rowData.get("NO_OF_ANSWERS").toString()));
                ynqBean.setExplanationRequiredFor((String)
                rowData.get("EXPLANATION_REQUIRED_FOR"));
                ynqBean.setDateRequiredFor((String)
                rowData.get("DATE_REQUIRED_FOR"));
                ynqBean.setStatus((String)
                rowData.get("STATUS"));
                ynqBean.setEffectiveDate(
                        rowData.get("EFFECTIVE_DATE") == null ?
                            null :new Date(((Timestamp)rowData.get("EFFECTIVE_DATE")).getTime()));
                ynqBean.setUpdateTimestamp((Timestamp)
                rowData.get("UPDATE_TIMESTAMP"));
                ynqBean.setUpdateUser((String)
                rowData.get("UPDATE_USER"));
                cvYNQData.addElement(ynqBean);
            }
        }
        return cvYNQData;
    }// End getYNQ
    
    /** Method  used to get FunctionList
     * <li>To fetch the data, it uses dw_get_function_list
     *@return CoeusVector of businessRuleFunctionBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getBusinessRuleFunctionList(String moduleCode, String subModuleCode)throws CoeusException,DBException{
        Vector result = null;
        Vector param = new Vector();
        CoeusVector cvFunctionList = null;
        if(dbEngine != null) {
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
//            result = dbEngine.executeRequest(DSN,
//                    "call dw_get_function_list( <<OUT RESULTSET rset>> )", DSN, param);
            param.addElement(new Parameter("MODULE_CODE",
                    DBEngineConstants.TYPE_INT, moduleCode));
            param.addElement(new Parameter("SUB_MODULE_CODE",
                    DBEngineConstants.TYPE_INT, subModuleCode));
            result = dbEngine.executeRequest(DSN,
                    "call get_function_list_for_module(<<MODULE_CODE>>, <<SUB_MODULE_CODE>>, <<OUT RESULTSET rset>> )", DSN, param);
            //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(result!= null && result.size() > 0){
            HashMap rowData = null;
            cvFunctionList= new CoeusVector();
            BusinessRuleFunctionBean businessRuleFunctionBean = null;
            for(int index=0; index< result.size(); index++) {
                rowData = (HashMap)result.get(index);
                businessRuleFunctionBean = new BusinessRuleFunctionBean();
                businessRuleFunctionBean.setFunctionName((String)
                rowData.get("FUNCTION_NAME"));
                businessRuleFunctionBean.setDescription((String)
                rowData.get("DESCRIPTION"));
                cvFunctionList.addElement(businessRuleFunctionBean);
            }
        }
        return cvFunctionList;
    }//End of getBusinessRuleFunctionList
    
    
    /*Method to get the FucntionDescription
     *@param CoeusVector of businessRuleFunctionBean
     *@return CoeusVector of businessRuleFunctionBean
     * @exception DBException if any error during database transaction.
     *@exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getRuleFuncDesc(CoeusVector cvData)
    throws CoeusException,DBException{
        CoeusVector cvFunctionDescription = new CoeusVector();
        for(int index = 0;index<cvData.size();index++ ){
            BusinessRuleFunctionBean businessRuleFunctionBean = (BusinessRuleFunctionBean)cvData.get(index);
            String funcName = businessRuleFunctionBean.getFunctionName();
            businessRuleFunctionBean = (BusinessRuleFunctionBean)getBusinessRuleFunctionDescription(funcName);
            cvFunctionDescription.addElement(businessRuleFunctionBean);
        }
        return cvFunctionDescription;
    }
    
    /** Method  used to get FunctionDescription
     * <li>To fetch the data, it uses dw_get_function_list
     *@return BusinessRuleFunctionBean
     *@param String functionName
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public BusinessRuleFunctionBean getBusinessRuleFunctionDescription(String functionName)
    throws CoeusException,DBException{
        Vector param =new Vector();
        Vector result = null;
        CoeusVector cvFunctionDesc = null;
        BusinessRuleFunctionBean businessRuleFunctionBean = null;
        param.addElement(new Parameter("FUNCTION_NAME",DBEngineConstants.TYPE_STRING,functionName));
        if(dbEngine != null) {
//            result = dbEngine.executeRequest(DSN,"dw_get_function_desc( << FUNCTION_NAME >>, "+
//            "<< OUT RESULTSET rset >>) ", DSN,param);
            result=dbEngine.executeRequest("Coeus",
                    "call dw_get_function_desc(<< FUNCTION_NAME >>,<< OUT RESULTSET rset >>)", "Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result!= null && result.size() > 0){
            HashMap rowData = null;
            cvFunctionDesc = new CoeusVector();
            for(int index=0; index< result.size(); index++) {
                rowData = (HashMap)result.get(index);
                businessRuleFunctionBean = new BusinessRuleFunctionBean();
                businessRuleFunctionBean.setFunctionName((String)
                rowData.get("FUNCTION_NAME"));
                businessRuleFunctionBean.setDescription((String)
                rowData.get("DESCRIPTION"));
            }
        }
        return businessRuleFunctionBean;
    }//End of getBusinessRuleFunctionDescription
    
    
    /** Method  used to get FunctionList
     * <li>To fetch the data, it uses fn_check_for_args
     *@return CoeusVector of businessRuleFunctionArgsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    
    public Integer checkForArgs(String functionName)throws CoeusException,DBException{
        Integer number =null;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("FUNCTION_NAME",
                DBEngineConstants.TYPE_STRING,functionName));
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER NUMBER>> = call fn_check_for_args(<< FUNCTION_NAME >>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowData = (HashMap)result.elementAt(0);
            number = new Integer(rowData.get("NUMBER").toString());
        }
        return number;
    }
    
    public int getMetaRuleId() throws CoeusException, DBException{
        int ruleId = 0;
        String metaRuleId = "";
        Vector param= new Vector();
        Vector result = new Vector();
//        param.add(new Parameter("PARAMATER",DBEngineConstants.TYPE_STRING,param));
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER META_RULE_ID>> = "
                    +" call fn_get_meta_rule_id( ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowData = (HashMap)result.elementAt(0);
            ruleId = Integer.parseInt(rowData.get("META_RULE_ID").toString());
        }
        return ruleId;
    }
    
    /** Method  used to get FunctionList
     * <li>To fetch the data, it uses DW_GET_YNQ_EXPLANATION
     *@return CoeusVector of CoeusVector
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getYNQExplanation(String questionId)throws CoeusException,DBException{
        Vector result = null;
        Vector param = new Vector();
        CoeusVector cvYNQExp = null;
        
        param.addElement(new Parameter("QUESTION_ID",
                DBEngineConstants.TYPE_STRING, questionId));
        
        if(dbEngine != null) {
            result = dbEngine.executeRequest(DSN,"call DW_GET_YNQ_EXPLANATION(<< QUESTION_ID >>, "+
                    "<< OUT RESULTSET rset >>) ", DSN,param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(result!= null && result.size() > 0){
            HashMap rowData = null;
            cvYNQExp= new CoeusVector();
            YNQExplanationBean ynqExplanationBean = null;
            for(int index=0; index< result.size(); index++) {
                rowData = (HashMap)result.get(index);
                ynqExplanationBean = new YNQExplanationBean();
                
                ynqExplanationBean.setQuestionId(
                        rowData.get("QUESTION_ID").toString());
                
                ynqExplanationBean.setExplanationType(
                        rowData.get("EXPLANATION_TYPE") == null ? "" : rowData.get("EXPLANATION_TYPE").toString());
                
                ynqExplanationBean.setExplanation(
                        rowData.get("EXPLANATION") == null ? "" : rowData.get("EXPLANATION").toString());
                
                cvYNQExp.addElement(ynqExplanationBean);
            }//End of for
        }//End of if
        return cvYNQExp;
    }//End of getYNQExplanation
    
    
    /** Method  used to get Argument Info
     * <li>To fetch the data, it uses DW_GET_ARG_INFO
     *@return CoeusVector of CoeusVector
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getArgumentInfo(String functionName)throws CoeusException,DBException{
        Vector result = null;
        Vector param = new Vector();
        CoeusVector cvArgInfo = null;
        
        param.addElement(new Parameter("FUNCTION_NAME",
                DBEngineConstants.TYPE_STRING, functionName));
        
        if(dbEngine != null) {
            result = dbEngine.executeRequest(DSN,"call DW_GET_ARG_INFO(<< FUNCTION_NAME >>, "+
                    "<< OUT RESULTSET rset >>) ", DSN,param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(result!= null && result.size() > 0){
            HashMap rowData = null;
            cvArgInfo= new CoeusVector();
            BusinessRuleFuncArgsBean businessRuleFuncArgsBean = null;
            for(int index=0; index< result.size(); index++) {
                
                rowData = (HashMap)result.get(index);
                businessRuleFuncArgsBean = new BusinessRuleFuncArgsBean();
                
                businessRuleFuncArgsBean.setFunctionName(
                        rowData.get("FUNCTION_NAME")== null ? "" : rowData.get("FUNCTION_NAME").toString());
                
                businessRuleFuncArgsBean.setArgumentName(
                        rowData.get("ARGUMENT_NAME")== null ? "" : rowData.get("ARGUMENT_NAME").toString());
                
                businessRuleFuncArgsBean.setRuleFuncDescription(
                        rowData.get("DESCRIPTION")== null ? "" : rowData.get("DESCRIPTION").toString());
                
                businessRuleFuncArgsBean.setArgumentType(
                        rowData.get("ARGUMENT_TYPE")== null ? "" : rowData.get("ARGUMENT_TYPE").toString());
                
                businessRuleFuncArgsBean.setDefaultValue(
                        rowData.get("DEFAULT_VALUE")== null ? "" : rowData.get("DEFAULT_VALUE").toString());
                
                businessRuleFuncArgsBean.setWindowName(
                        rowData.get("WINDOW_NAME")== null ? "" : rowData.get("WINDOW_NAME").toString());
                
                businessRuleFuncArgsBean.setSequenceNumber(
                        Integer.parseInt(rowData.get("SEQUENCE_NUMBER").toString()));
                
                businessRuleFuncArgsBean.setUpdateTimestamp(
                        (Timestamp)rowData.get("UPDATE_TIMESTAMP"));
                
                businessRuleFuncArgsBean.setUpdateUser(
                        (String)rowData.get("UPDATE_USER"));
                
                cvArgInfo.addElement(businessRuleFuncArgsBean);
            }//End of for
        }//End of if
        return cvArgInfo;
    }//End of getArgumentInfo
    
    
    /**
     * This method is used to get the data type for the variable name
     * @param String variable name
     * @return String datatype
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getDataType(String variableName)
    throws DBException, CoeusException {
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap dataTypeRow = null;
        String dataType = "";
        
        param.add(new Parameter("VARIABLE_NAME",
                DBEngineConstants.TYPE_STRING, variableName));
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT STRING DATATYPE>> = call FN_GET_RULE_DATATYPE (<<VARIABLE_NAME>>)}",param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(!result.isEmpty()) {
            dataTypeRow = (HashMap)result.elementAt(0);
            dataType = dataTypeRow.get("DATATYPE").toString();
        }
        return dataType;
    }//End of getDataType
    
    
    /**
     *  This method is used check the rights to enable/disable menu items
     *  <li>To fetch the data, it uses fn_user_has_right function.
     *
     *  @param user String
     *  @param unitNumber String
     *  @param Business Rule Right as String
     *  @return boolean true if user has rights
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean isUserHasRight(String user,String unitNumber,
            String ruleRight) throws CoeusException, DBException {
        int userRight = 0;
        boolean hasRight =false;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("USER",DBEngineConstants.TYPE_STRING,user));
        param.add(new Parameter("UNITNUMBER",
                DBEngineConstants.TYPE_STRING,unitNumber));
        param.add(new Parameter("RULERIHGT",
                DBEngineConstants.TYPE_STRING,ruleRight));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER RIGHTEXISTS>> = call fn_user_has_right(<< USER >>,"+
                    "<< UNITNUMBER >> , << RULERIHGT >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap ruleId = (HashMap)result.elementAt(0);
            userRight = Integer.parseInt(
                    ruleId.get("RIGHTEXISTS").toString());
        }
        if ( userRight ==1 )  {
            hasRight = true;
        }else if ( userRight == 0 ){
            hasRight =false;
        }
        return hasRight;
    }// End of isUserHasRight
    
    public CoeusVector getAllBusinessRules()throws CoeusException,DBException {
        Vector param = new Vector();
        Vector result = null;
        CoeusVector cvBusinessRule = null;
        if(dbEngine != null) {
            result = dbEngine.executeRequest(DSN,"call GET_ALL_BUSSINESS_RULES("+
                    "<< OUT RESULTSET rset >>) ", DSN,param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(result!= null && result.size() > 0){
            HashMap rowData = null;
            cvBusinessRule = new CoeusVector();
            BusinessRuleBean businessRuleBean = null;
            for(int index=0; index< result.size(); index++) {
                rowData = (HashMap)result.get(index);
                businessRuleBean = new BusinessRuleBean();
                businessRuleBean.setRuleId(rowData.get("RULE_ID")== null ? null : rowData.get("RULE_ID").toString());
                businessRuleBean.setRuleType((String)rowData.get("RULE_TYPE"));
                businessRuleBean.setUnitNumber(rowData.get("UNIT_NUMBER")== null ? null : rowData.get("UNIT_NUMBER").toString());
                businessRuleBean.setDescription(rowData.get("DESCRIPTION")== null ? null : rowData.get("DESCRIPTION").toString());
                businessRuleBean.setUpdateTimestamp((Timestamp)rowData.get("UPDATE_TIMESTAMP"));
                businessRuleBean.setUpdateUser((String)rowData.get("UPDATE_USER"));
                businessRuleBean.setModuleCode(rowData.get("MODULE_CODE").toString());
                businessRuleBean.setSubmoduleCode(rowData.get("SUB_MODULE_CODE")==null? null:rowData.get("SUB_MODULE_CODE").toString());
                businessRuleBean.setRuleCategory(rowData.get("RULE_CATEGORY")==null? null: rowData.get("RULE_CATEGORY").toString());
                cvBusinessRule.addElement(businessRuleBean);
            }
        }
        return cvBusinessRule;
    }    
    
    // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
    /**
     * Method to evaluate rules
     * @param moduleItemCode 
     * @param subModuleItemCode 
     * @param moduleItemKey 
     * @param moduleSubItemKey 
     * @param ruleId 
     * @param ruleType 
     * @param userId 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return rulePassed
     */
    public boolean evaluateRule(int moduleItemCode, int subModuleItemCode, 
            String moduleItemKey, String moduleSubItemKey, int ruleId, String ruleType, String userId) throws DBException{
        boolean rulePassed = false;
        Vector param= new Vector();
        param.add(new Parameter("AS_MODULE_CODE",
                DBEngineConstants.TYPE_INT, new Integer(moduleItemCode).toString())) ;
        param.add(new Parameter("AS_SUB_MODULE_CODE",
                DBEngineConstants.TYPE_INT, new Integer(subModuleItemCode).toString())) ;        
        param.add(new Parameter("AS_MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING, moduleItemKey )) ;
        param.add(new Parameter("AS_MODULE_ITEM_KEY_SEQUENCE",
                    DBEngineConstants.TYPE_INT, new Integer(moduleSubItemKey).toString())) ;
        param.add(new Parameter("AI_RULE_ID",
                    DBEngineConstants.TYPE_INT, new Integer(ruleId).toString())) ;
        param.add(new Parameter("AS_RULE_TYPE", DBEngineConstants.TYPE_STRING, ruleType )) ;
        param.add(new Parameter("AS_USER",DBEngineConstants.TYPE_STRING, userId )) ;		
        
        Vector result = dbEngine.executeFunctions("Coeus",
                "{<<OUT INTEGER SUCCESS>> = call pkg_routing_evaluation.fn_evaluate_rule ( "
                +" << AS_MODULE_CODE >> , << AS_SUB_MODULE_CODE >> ,<< AS_MODULE_ITEM_KEY >>, << AS_MODULE_ITEM_KEY_SEQUENCE >>, << AI_RULE_ID >>, << AS_RULE_TYPE >>, << AS_USER >>)}", param);
        if(!result.isEmpty()){
            HashMap nextNumRow = (HashMap)result.elementAt(0);
            int success = Integer.parseInt(nextNumRow.get("SUCCESS").toString());
            if(success == 1){
                rulePassed = true;
            }
        }
       return rulePassed; 
    }
    // Added for COEUSQA-3287 Questionnaire Maintenance Features - end
    
}
