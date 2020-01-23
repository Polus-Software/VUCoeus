/*
 * @(#)RuleUpdateTxnBean.java 1.0 10/25/05 4:45 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 20-AUG-2007
 * by Leena
 */
package edu.mit.coeus.mapsrules.bean;

import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
//import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.CoeusFunctions;
//import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.exception.CoeusException;

import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.query.Equals;

import java.util.Vector;
//import java.util.Hashtable;
import java.util.HashMap;
import java.sql.Timestamp;
//import java.sql.Date;
//import java.sql.SQLException;


/**
 *
 * @author  chandrashekara
 */
public class RuleUpdateTxnBean {
    
    // instance of a dbEngine
    private DBEngineImpl dbEngine;
    // holds the dataset name
    private static final String DSN = "Coeus";
    // holds the userId for the logged in user
    private String userId;
    private TransactionMonitor transMon;
    private Timestamp dbTimestamp;
//    private String unitNumber;
    /** Creates a new instance of RuleUpdateTxnBean */
    public RuleUpdateTxnBean() {
    }
    
    public RuleUpdateTxnBean(String userId) throws DBException{
        this.userId = userId;
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
    }
    
    
    /** Method used to update/insert/delete all the details of MetaRule
     * <li>To fetch the data, it uses dw_update_metarules procedure.
     *
     * @return boolean this holds true for successfull insert/modify or
     * false if fails.
     * @param MetaRuleBean metaRuleBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    
    public ProcReqParameter addUpdMetaRule(MetaRuleBean metaRuleBean)
    throws CoeusException , DBException{
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;
        param = new Vector();
        param.addElement(new Parameter("AV_META_RULE_ID",
                DBEngineConstants.TYPE_STRING, metaRuleBean.getMetaRuleId()));
        param.addElement(new Parameter("AV_DESCRIPTION",
                DBEngineConstants.TYPE_STRING, metaRuleBean.getDescription()));
        param.addElement(new Parameter("AV_UNIT_NO",
                DBEngineConstants.TYPE_STRING, metaRuleBean.getUnitNumber()));
        param.addElement(new Parameter("AV_META_RULE_TYPE",
                DBEngineConstants.TYPE_STRING, metaRuleBean.getMetaRuleType()));
        param.addElement(new Parameter("AV_USER",
                DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("AV_UPD_DATESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
        param.addElement(new Parameter("AW_ORG_USER",
                DBEngineConstants.TYPE_STRING, metaRuleBean.getUpdateUser()));
        if(metaRuleBean.getUpdateTimestamp()!= null){
            param.addElement(new Parameter("AW_ORG_UPD_DATESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,metaRuleBean.getUpdateTimestamp()));
        }else{
            param.addElement(new Parameter("AW_ORG_UPD_DATESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        }
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //added the column module_code
        param.addElement(new Parameter("AV_MODULE_CODE",
                DBEngineConstants.TYPE_STRING, metaRuleBean.getModuleCode()));
        param.addElement(new Parameter("AV_SUB_MODULE_CODE",
                DBEngineConstants.TYPE_STRING, metaRuleBean.getSubmoduleCode()));
        param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING, metaRuleBean.getAcType()));
//        param.addElement(new Parameter("CHOICE",
//                DBEngineConstants.TYPE_STRING, metaRuleBean.getAcType()));
        //Created a new procedure update_metarules to include the column module_code
        //StringBuffer sql = new StringBuffer("call dw_update_metarules(");
        StringBuffer sql = new StringBuffer("call update_metarules(");
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        sql.append(" <<AV_META_RULE_ID>> , ");
        sql.append(" <<AV_DESCRIPTION>> , ");
        sql.append(" <<AV_UNIT_NO>> , ");
        sql.append(" <<AV_META_RULE_TYPE>> , ");
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        sql.append(" <<AV_MODULE_CODE>> , ");
        sql.append(" <<AV_SUB_MODULE_CODE>>, ");
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        sql.append(" <<AV_USER>> , ");
        sql.append(" <<AV_UPD_DATESTAMP>> , ");
        sql.append(" <<AW_ORG_USER>> , ");
        sql.append(" <<AW_ORG_UPD_DATESTAMP>> , ");
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //sql.append(" <<CHOICE>> )");
        sql.append(" <<AC_TYPE>> )");
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        return procReqParameter;
    }
    
    /** Method used to update/insert/delete all the details of MetaRuleDetail
     * <li>To fetch the data, it uses dw_update_metarule_detail procedure.
     *
     * @return boolean this holds true for successfull insert/modify or
     * false if fails.
     * @param MetaRuleDetailBean metaRuleDetailBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    
    public ProcReqParameter addUpdMetaRuleDetails(MetaRuleDetailBean metaRuleDetailBean)
    throws CoeusException , DBException{
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;
        param = new Vector();
        param.addElement(new Parameter("AV_META_RULE_ID",
                DBEngineConstants.TYPE_STRING, metaRuleDetailBean.getMetaRuleId()));
        param.addElement(new Parameter("AV_NODE_NUMBER",
                DBEngineConstants.TYPE_STRING, metaRuleDetailBean.getNodeId()));
        param.addElement(new Parameter("AV_RULE_ID",
                DBEngineConstants.TYPE_STRING, metaRuleDetailBean.getRuleId()));
        param.addElement(new Parameter("AV_PARENT_NODE",
                DBEngineConstants.TYPE_STRING, metaRuleDetailBean.getParentNodeId()));
        param.addElement(new Parameter("AV_NEXT_NODE",
                DBEngineConstants.TYPE_STRING, metaRuleDetailBean.getNextNode()));
        param.addElement(new Parameter("AV_NODE_IF_TRUE",
                DBEngineConstants.TYPE_STRING, metaRuleDetailBean.getNodeIfTrue()));
        param.addElement(new Parameter("AV_NODE_IF_FALSE",
                DBEngineConstants.TYPE_STRING, metaRuleDetailBean.getNodeIfFalse()));
        param.addElement(new Parameter("AV_USER",
                DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("AV_UPD_DATESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("AW_ORG_USER",
                DBEngineConstants.TYPE_STRING, metaRuleDetailBean.getUpdateUser()));
        param.addElement(new Parameter("AW_ORG_UPD_DATESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,metaRuleDetailBean.getUpdateTimeStamp()));
        param.addElement(new Parameter("CHOICE",
                DBEngineConstants.TYPE_STRING, metaRuleDetailBean.getAcType()));
        
        StringBuffer sql = new StringBuffer("call dw_update_metarule_detail(");
        sql.append(" <<AV_META_RULE_ID>> , ");
        sql.append(" <<AV_NODE_NUMBER>> , ");
        sql.append(" <<AV_RULE_ID>> , ");
        sql.append(" <<AV_PARENT_NODE>> , ");
        sql.append(" <<AV_NEXT_NODE>> , ");
        sql.append(" <<AV_NODE_IF_TRUE>> , ");
        sql.append(" <<AV_NODE_IF_FALSE>> , ");
        sql.append(" <<AV_USER>> , ");
        sql.append(" <<AV_UPD_DATESTAMP>> , ");
        sql.append(" <<AW_ORG_USER>> , ");
        sql.append(" <<AW_ORG_UPD_DATESTAMP>> , ");
        sql.append(" <<CHOICE>> )");
        procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        return procReqParameter;
    }
    
    /** Method used to update/insert/delete all the details of Business Rule detail
     * <li>To fetch the data, it uses dw_update_bussinessrules procedure.
     *
     * @return ProcReqParameter this holds true for successfull insert/modify or
     * false if fails.
     * @param BusinessRuleBean businessRuleBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public ProcReqParameter addUpdBusinessRules(BusinessRuleBean businessRuleBean)
    throws CoeusException , DBException{
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;
        param = new Vector();
        param.addElement(new Parameter("AV_RULE_ID",
                DBEngineConstants.TYPE_STRING, businessRuleBean.getRuleId()));
        param.addElement(new Parameter("AV_DESCRIPTION",
                DBEngineConstants.TYPE_STRING, businessRuleBean.getDescription()));
        param.addElement(new Parameter("AV_UNIT_NO",
                DBEngineConstants.TYPE_STRING, businessRuleBean.getUnitNumber()));
        param.addElement(new Parameter("AV_RULE_TYPE",
                DBEngineConstants.TYPE_STRING, businessRuleBean.getRuleType()));
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        param.addElement(new Parameter("AV_MODULE_CODE",
                DBEngineConstants.TYPE_STRING, businessRuleBean.getModuleCode()));
        param.addElement(new Parameter("AV_SUB_MODULE_CODE",
                DBEngineConstants.TYPE_STRING, businessRuleBean.getSubmoduleCode()));
        param.addElement(new Parameter("AV_RULE_CATEGORY",
                DBEngineConstants.TYPE_STRING, businessRuleBean.getRuleCategory()));
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        param.addElement(new Parameter("AV_USER",
                DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("AW_ORG_USER",
                DBEngineConstants.TYPE_STRING, businessRuleBean.getUpdateUser()));
        if(businessRuleBean.getUpdateTimestamp()!= null){
            param.addElement(new Parameter("AW_ORG_UPD_DATESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,businessRuleBean.getUpdateTimestamp()));
        }else{
            param.addElement(new Parameter("AW_ORG_UPD_DATESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        }
        param.addElement(new Parameter("CHOICE",
                DBEngineConstants.TYPE_STRING, businessRuleBean.getAcType()));
        
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //StringBuffer sql = new StringBuffer("call dw_update_bussinessrules(");
        StringBuffer sql = new StringBuffer("call update_bussinessrules(");
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        sql.append(" <<AV_RULE_ID>> , ");
        sql.append(" <<AV_DESCRIPTION>> , ");
        sql.append(" <<AV_UNIT_NO>> , ");
        sql.append(" <<AV_RULE_TYPE>> , ");
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        sql.append(" <<AV_MODULE_CODE>> , ");
        sql.append(" <<AV_SUB_MODULE_CODE>> , ");
        sql.append(" <<AV_RULE_CATEGORY>> , ");
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        sql.append(" <<AV_USER>> , ");
        sql.append(" <<AW_ORG_USER>> , ");
        sql.append(" <<AW_ORG_UPD_DATESTAMP>> , ");
        sql.append(" <<CHOICE>> )");
        procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        return procReqParameter;
    }
    
    
    /** This method will update the Business Rules, Meta Rules and Metarule Details
     *together.
     *@param HashMap contains the vector of BusinessRulesBean, Vector of MetaRuleBean
     *Vector of MataRuleDetailBean.
     *@throws CoeusException,DBException
     */
    public boolean addUpdDelBusinessRules(HashMap hmClinetData) throws
            CoeusException, DBException{
        boolean success = false;
        Vector procedures = new Vector(5,3);
        CoeusVector cvMetaRuleData = null;
        CoeusVector cvMetaRuleDetail = null;
        CoeusVector cvBusinessRule = null;
        cvMetaRuleData = (CoeusVector)hmClinetData.get(MetaRuleBean.class);
        cvMetaRuleDetail = (CoeusVector)hmClinetData.get(MetaRuleDetailBean.class);
        cvBusinessRule = (CoeusVector)hmClinetData.get(BusinessRuleBean.class);
        // Update MetaRule data
        if(cvMetaRuleData!= null && cvMetaRuleData.size() > 0){
            for(int count=0;count < cvMetaRuleData.size();count++){
                MetaRuleBean metaRuleBean = (MetaRuleBean)cvMetaRuleData.elementAt(count);
                if(metaRuleBean.getAcType() == null){
                    continue;
                }
                if(metaRuleBean!= null && metaRuleBean.getAcType() != null){
                    procedures.add(addUpdMetaRule(metaRuleBean));
                }
            }
        }
        // Update MetaRuleDetails data
        if(cvMetaRuleDetail!= null && cvMetaRuleDetail.size() > 0){
            for(int count=0;count < cvMetaRuleDetail.size();count++){
                MetaRuleDetailBean metaRuleDetailBean = (MetaRuleDetailBean)cvMetaRuleDetail.elementAt(count);
                if(metaRuleDetailBean.getAcType() == null){
                    continue;
                }
                if(metaRuleDetailBean!= null && metaRuleDetailBean.getAcType() != null){
                    procedures.add(addUpdMetaRuleDetails(metaRuleDetailBean));
                }
            }
        }
        
        // Update Business Rule Data
        if(cvBusinessRule!= null && cvBusinessRule.size() > 0){
            for(int count=0;count < cvBusinessRule.size();count++){
                BusinessRuleBean businessRuleBean = (BusinessRuleBean)cvBusinessRule.elementAt(count);
                if(businessRuleBean.getAcType() == null){
                    continue;
                }
                if(businessRuleBean!= null && businessRuleBean.getAcType() != null){
                    procedures.add(addUpdBusinessRules(businessRuleBean));
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
        return success;
    }
    
    
    public boolean addUpdDelCondtions(HashMap hmData) throws
            CoeusException , DBException{
        boolean success = false;
        
        Vector procedures = new Vector(5,3);
        
        BusinessRuleBean businessRuleBean = (BusinessRuleBean)hmData.get(BusinessRuleBean.class);
        
        procedures.add(addUpdBusinessRules(businessRuleBean));
        
        CoeusVector cvConditionData = (CoeusVector)hmData.get(BusinessRuleConditionsBean.class);
        
        if(cvConditionData != null && cvConditionData.size()>0){
            for (int index = 0 ; index < cvConditionData.size(); index++){
                BusinessRuleConditionsBean businessRuleConditionsBean =
                        (BusinessRuleConditionsBean)cvConditionData.get(index);
                
                if(businessRuleConditionsBean.getAcType() != null){
                    procedures.add(addUpdRuleCond(businessRuleConditionsBean));
                }
                //Added for case 2418 - Business Rule Evaluation Bug - start
                /*Check whether any conditions are modified, by checking the acType
                If there is any modification done in any expressions for this condition,
                 delete all the expression in this condition. This will do a cascade delete
                 in the arguments table. Set the acType of all the records to 'I' and insert 
                 the records*/
                CoeusVector cvExprData = (CoeusVector)hmData.get(BusinessRuleExpBean.class);
                CoeusVector cvModifiedExprData = new CoeusVector();
                Equals eqUpdate = new Equals("acType" , TypeConstants.UPDATE_RECORD);
                Equals eqDelete = new Equals("acType" , TypeConstants.DELETE_RECORD);
                Equals eqInsert = new Equals("acType" , TypeConstants.INSERT_RECORD);
                Equals eqConditionNo = new Equals("conditionNumber", 
                        new Integer(businessRuleConditionsBean.getConditionNumber()));
                
                cvModifiedExprData.addAll(cvExprData.filter(eqUpdate));
                cvModifiedExprData.addAll(cvExprData.filter(eqDelete));
                cvModifiedExprData.addAll(cvExprData.filter(eqInsert));
                cvModifiedExprData = cvModifiedExprData.filter(eqConditionNo);
                
                //If modified expressions present delete all the expressions for the condition
                if(cvModifiedExprData.size() >0){
                    BusinessRuleExpBean businessRuleExpBean = 
                            (BusinessRuleExpBean)cvModifiedExprData.get(0);
                    procedures.add(deleteRuleConditionExps(businessRuleExpBean.getRuleId(),
                            businessRuleExpBean.getConditionNumber()));
                     
                    //Filter all the expressions whose acTYpe is not 'D' and set the 
                    //acType to 'I' and insert the expressions
                    //The following check is needed for insert after delete.There will be 2 records with same ruleId $conditionNumber.
                    // Added with COEUSQA-2579 - Error modifying an existing business rule
                    if(!TypeConstants.DELETE_RECORD.equals(businessRuleConditionsBean.getAcType())){
                        cvModifiedExprData = cvExprData.filter(eqConditionNo);
                        if(cvModifiedExprData!=null && cvModifiedExprData.size() > 0){
                            for(int i = 0; i < cvModifiedExprData.size(); i++){
                                businessRuleExpBean = (BusinessRuleExpBean)cvModifiedExprData.get(i);
                                if(!TypeConstants.DELETE_RECORD.equals(businessRuleExpBean.getAcType())){
                                    businessRuleExpBean.setAcType(TypeConstants.INSERT_RECORD);
                                    procedures.add(addUpdRuleExpr(businessRuleExpBean));
                                }
                            }
                        }
                        
                        //Filter all the arguments whose acTYpe is not 'D' and set the
                        //acType to 'I' and insert the arguments
                        CoeusVector cvRuleFuncArgs = (CoeusVector)hmData.get(BusinessRuleFuncArgsBean.class);
                        CoeusVector cvArgsForCondition = cvRuleFuncArgs.filter(eqConditionNo);
                        if(cvArgsForCondition != null && cvArgsForCondition.size()>0){
                            for(int j = 0 ; j < cvArgsForCondition.size() ; j++){
                                BusinessRuleFuncArgsBean ruleFuncArgsBean =
                                        (BusinessRuleFuncArgsBean)cvArgsForCondition.get(j);
                                if(!TypeConstants.DELETE_RECORD.equals(ruleFuncArgsBean .getAcType())){
                                    ruleFuncArgsBean.setAcType(TypeConstants.INSERT_RECORD);
                                    procedures.add(addUpdRuleFuncArgs(ruleFuncArgsBean));
                                }
                            }
                        }
                    }
                }else{//If the expressions are not modified, insert all the modified arguments
                     CoeusVector cvRuleFuncArgs = (CoeusVector)hmData.get(BusinessRuleFuncArgsBean.class);
                     CoeusVector cvArgsForCondition = cvRuleFuncArgs.filter(eqConditionNo);
                    if(cvArgsForCondition != null && cvArgsForCondition.size()>0){
                        for(int row = 0 ; row < cvArgsForCondition.size() ; row++){
                            BusinessRuleFuncArgsBean ruleFuncArgsBean =
                                    (BusinessRuleFuncArgsBean)cvArgsForCondition.get(row);
                            if(ruleFuncArgsBean .getAcType() != null){
                                procedures.add(addUpdRuleFuncArgs(ruleFuncArgsBean));
                            }
                        }
                    }
                }
            }
        }
        //Added for case 2418 - Business Rule Evaluation Bug - end
        //Commented for case 2418 - Business Rule Evaluation Bug - start
//        if(cvExprData != null && cvExprData.size()>0){
//            for(int index = 0 ; index < cvExprData.size() ; index++){
//                BusinessRuleExpBean businessRuleExpBean =
//                        (BusinessRuleExpBean)cvExprData.get(index);
//                if(businessRuleExpBean .getAcType() != null
//                        && businessRuleExpBean .getAcType().equals("D")){
//                    procedures.add(addUpdRuleExpr(businessRuleExpBean));
//                }
//            }
//            
//            for(int index = 0 ; index < cvExprData.size() ; index++){
//                BusinessRuleExpBean businessRuleExpBean =
//                        (BusinessRuleExpBean)cvExprData.get(index);
//                if(businessRuleExpBean .getAcType() != null
//                          && !businessRuleExpBean .getAcType().equals("D")){
//                    procedures.add(addUpdRuleExpr(businessRuleExpBean));
//                }
//            }
//        }//End of for
        //Commented for case 2418 - Business Rule Evaluation Bug - end
     
        
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
    }//End of addUpdDelCondtions
    
    /** Method used to update/insert/ Business Rule Conditions
     * <li>To fetch the data, it uses DW_UPDATE_BUSINESSRULECOND procedure.
     *
     * @return ProcReqParameter
     * @param BusinessRuleConditionsBean businessRuleConditionsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public ProcReqParameter addUpdRuleCond(BusinessRuleConditionsBean businessRuleConditionsBean)
    throws CoeusException ,DBException{
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;
        
        param.addElement(new Parameter("AV_RULE_ID",
                DBEngineConstants.TYPE_INT, ""+businessRuleConditionsBean.getRuleId()));
        
        param.addElement(new Parameter("AV_CONDITION_NO",
                DBEngineConstants.TYPE_INT, ""+businessRuleConditionsBean.getConditionNumber()));
        
        param.addElement(new Parameter("AV_CONDITION_SEQ",
                DBEngineConstants.TYPE_INT, ""+businessRuleConditionsBean.getConditionSequence()));
        
        param.addElement(new Parameter("AV_ACTION",
                DBEngineConstants.TYPE_INT, ""+businessRuleConditionsBean.getAction()));
        
        param.addElement(new Parameter("AV_CONDITION_EXP",
                DBEngineConstants.TYPE_STRING, businessRuleConditionsBean.getConditionExp()));
        
        param.addElement(new Parameter("AV_DESCRIPTION",
                DBEngineConstants.TYPE_STRING, businessRuleConditionsBean.getRuleDescription()));
        
        param.addElement(new Parameter("AV_USER",
                DBEngineConstants.TYPE_STRING, userId));
        
        param.addElement(new Parameter("AW_CONDITION_NO",
                DBEngineConstants.TYPE_INT, ""+businessRuleConditionsBean.getAwConditionNumber()));
        
        param.addElement(new Parameter("AW_ORG_USER",
                DBEngineConstants.TYPE_STRING, businessRuleConditionsBean.getUpdateUser()));
        
        param.addElement(new Parameter("AW_ORG_UPD_DATESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,businessRuleConditionsBean.getUpdateTimestamp()));
        
        param.addElement(new Parameter("CHOICE",
                DBEngineConstants.TYPE_STRING, businessRuleConditionsBean.getAcType()));
        
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        param.addElement(new Parameter("AV_USER_MESSAGE",
                DBEngineConstants.TYPE_STRING, businessRuleConditionsBean.getUserMessage()));
        //StringBuffer sql = new StringBuffer("call DW_UPDATE_BUSINESSRULECOND(");
        StringBuffer sql = new StringBuffer("call UPDATE_BUSINESSRULECOND(");
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        sql.append(" <<AV_RULE_ID>> , ");
        sql.append(" <<AV_CONDITION_NO>> , ");
        sql.append(" <<AV_CONDITION_SEQ>> , ");
        sql.append(" <<AV_ACTION>> , ");
        sql.append(" <<AV_CONDITION_EXP>> , ");
        sql.append(" <<AV_DESCRIPTION>> , ");
        sql.append(" <<AV_USER>> , ");
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        sql.append(" <<AV_USER_MESSAGE>> , ");
        //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        sql.append(" <<AW_CONDITION_NO>> , ");
        sql.append(" <<AW_ORG_USER>> , ");
        sql.append(" <<AW_ORG_UPD_DATESTAMP>> , ");
        sql.append(" <<CHOICE>> )");
        
        procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        
        return procReqParameter;
    }//End addUpdRuleCond
    
    /** Method used to update/insert/ Business Rule Expression
     * <li>To fetch the data, it uses UPD_BUSINESS_RULE_EXPRESSION procedure.
     *
     * @return ProcReqParameter
     * @param BusinessRuleExpBean businessRuleExpBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public ProcReqParameter addUpdRuleExpr(BusinessRuleExpBean businessRuleExpBean)
    throws CoeusException ,DBException{
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;
        
        param.addElement(new Parameter("AV_RULE_ID",
                DBEngineConstants.TYPE_INT, ""+businessRuleExpBean.getRuleId()));
        
        param.addElement(new Parameter("AV_CONDITION_NO",
                DBEngineConstants.TYPE_INT, ""+businessRuleExpBean.getConditionNumber()));
        
        param.addElement(new Parameter("AV_EXPRESSION_NO",
                DBEngineConstants.TYPE_INT, ""+businessRuleExpBean.getExpressionNumber()));
        
        param.addElement(new Parameter("AV_LVALUE",
                DBEngineConstants.TYPE_STRING, businessRuleExpBean.getLvalue()));
        
        param.addElement(new Parameter("AV_OPER",
                DBEngineConstants.TYPE_STRING, businessRuleExpBean.getOperator()));
        
        param.addElement(new Parameter("AV_RVALUE",
                DBEngineConstants.TYPE_STRING, businessRuleExpBean.getRvalue()));
        
        param.addElement(new Parameter("AV_LOGICAL_OPER",
                DBEngineConstants.TYPE_STRING, businessRuleExpBean.getLogicalOperator()));
        
        param.addElement(new Parameter("AV_USER",
                DBEngineConstants.TYPE_STRING, userId));
        
        param.addElement(new Parameter("AW_ORG_EXPRESSION_NO",
                DBEngineConstants.TYPE_INT, ""+businessRuleExpBean.getAwExpressionNumber()));
        
        param.addElement(new Parameter("AV_EXP_TYPE",
                DBEngineConstants.TYPE_STRING, businessRuleExpBean.getExpressionType()));
        
        param.addElement(new Parameter("AW_ORG_CONDITION_NO",
                DBEngineConstants.TYPE_INT, ""+businessRuleExpBean.getAwConditionNumber()));
        
        param.addElement(new Parameter("AW_ORG_USER",
                DBEngineConstants.TYPE_STRING, businessRuleExpBean.getUpdateUser()));
        
        param.addElement(new Parameter("AW_ORG_UPD_DATESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,businessRuleExpBean.getUpdateTimestamp()));
        
        param.addElement(new Parameter("CHOICE",
                DBEngineConstants.TYPE_STRING, businessRuleExpBean.getAcType()));
        // Added with COEUSQA-2458-Make 'and' and 'or' work in business rules
        param.addElement(new Parameter("AV_EXPRESSION_PREFIX",
                DBEngineConstants.TYPE_STRING, businessRuleExpBean.getExpressionPrefix()));
        
        param.addElement(new Parameter("AV_EXPRESSION_SUFFIX",
                DBEngineConstants.TYPE_STRING, businessRuleExpBean.getExpressionSuffix()));
        // COEUSQA-2458-End
        StringBuffer sql = new StringBuffer("call UPD_BUSINESS_RULE_EXPRESSION(");
        sql.append(" <<AV_RULE_ID>> , ");
        sql.append(" <<AV_CONDITION_NO>> , ");
        sql.append(" <<AV_EXPRESSION_NO>> , ");
        sql.append(" <<AV_LVALUE>> , ");
        sql.append(" <<AV_OPER>> , ");
        sql.append(" <<AV_RVALUE>> , ");
        sql.append(" <<AV_LOGICAL_OPER>> , ");
        // Added with COEUSQA-2458-Make 'and' and 'or' work in business rules
        sql.append(" <<AV_EXPRESSION_PREFIX>> , ");
        sql.append(" <<AV_EXPRESSION_SUFFIX>> , ");
        // COEUSQA-2458-End
        sql.append(" <<AV_USER>> , ");
        sql.append(" <<AW_ORG_EXPRESSION_NO>> , ");
        sql.append(" <<AV_EXP_TYPE>> , ");
        sql.append(" <<AW_ORG_CONDITION_NO>> , ");
        sql.append(" <<AW_ORG_USER>> , ");
        sql.append(" <<AW_ORG_UPD_DATESTAMP>> , ");
        sql.append(" <<CHOICE>> )");
        
        procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        
        return procReqParameter;
    }//End addUpdRuleCond
    
    
    /** Method used to update/insert/ Business Rule Expression
     * <li>To fetch the data, it uses UPDATE_BUS_RULE_EXP_ARGS procedure.
     *
     * @return ProcReqParameter
     * @param BusinessRuleFuncArgsBean businessRuleFuncArgsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public ProcReqParameter addUpdRuleFuncArgs(BusinessRuleFuncArgsBean businessRuleFuncArgsBean)
    throws CoeusException ,DBException{
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;
        
        param.addElement(new Parameter("AV_RULE_ID",
                DBEngineConstants.TYPE_INT, ""+businessRuleFuncArgsBean.getRuleId()));
        
        param.addElement(new Parameter("AV_CONDITION_NO",
                DBEngineConstants.TYPE_INT, ""+businessRuleFuncArgsBean.getConditionNumber()));
        
        param.addElement(new Parameter("AV_EXPRESSION_NUMBER",
                DBEngineConstants.TYPE_INT, ""+businessRuleFuncArgsBean.getExpressionNumber()));
        
        param.addElement(new Parameter("AV_FUNCTION_NAME",
                DBEngineConstants.TYPE_STRING, businessRuleFuncArgsBean.getFunctionName()));
        
        param.addElement(new Parameter("AV_ARGUMENT_NAME",
                DBEngineConstants.TYPE_STRING, businessRuleFuncArgsBean.getArgumentName()));
        
        param.addElement(new Parameter("AV_VALUE",
                DBEngineConstants.TYPE_STRING, businessRuleFuncArgsBean.getValue()));
        
        param.addElement(new Parameter("AV_DESCRIPTION",
                DBEngineConstants.TYPE_STRING, businessRuleFuncArgsBean.getRuleExpDescription()));
        
        param.addElement(new Parameter("AV_USER",
                DBEngineConstants.TYPE_STRING, userId));
        
        param.addElement(new Parameter("AW_CONDITION_NO",
                DBEngineConstants.TYPE_INT, ""+businessRuleFuncArgsBean.getAwConditionNumber()));
        
        param.addElement(new Parameter("AW_EXPRESSION_NUMBER",
                DBEngineConstants.TYPE_INT, ""+businessRuleFuncArgsBean.getAwExpressionNumber()));
        
        param.addElement(new Parameter("AW_FUNCTION_NAME",
                DBEngineConstants.TYPE_STRING, businessRuleFuncArgsBean.getAwFunctionName()));
        
        param.addElement(new Parameter("AW_ARGUMENT_NAME",
                DBEngineConstants.TYPE_STRING, businessRuleFuncArgsBean.getAwArgumentName()));
        
        param.addElement(new Parameter("AW_ORIG_USER",
                DBEngineConstants.TYPE_STRING, businessRuleFuncArgsBean.getUpdateUser()));
        
        param.addElement(new Parameter("AW_ORIG_UPD_DATESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,businessRuleFuncArgsBean.getUpdateTimestamp()));
        
        param.addElement(new Parameter("CHOICE",
                DBEngineConstants.TYPE_STRING, businessRuleFuncArgsBean.getAcType()));
        
        StringBuffer sql = new StringBuffer("call UPDATE_BUS_RULE_EXP_ARGS(");
        sql.append(" <<AV_RULE_ID>> , ");
        sql.append(" <<AV_CONDITION_NO>> , ");
        sql.append(" <<AV_EXPRESSION_NUMBER>> , ");
        sql.append(" <<AV_FUNCTION_NAME>> , ");
        sql.append(" <<AV_ARGUMENT_NAME>> , ");
        sql.append(" <<AV_VALUE>> , ");
        sql.append(" <<AV_DESCRIPTION>> , ");
        sql.append(" <<AV_USER>> , ");
        sql.append(" <<AW_CONDITION_NO>> , ");
        sql.append(" <<AW_EXPRESSION_NUMBER>> , ");
        sql.append(" <<AW_FUNCTION_NAME>> , ");
        sql.append(" <<AW_ARGUMENT_NAME>> , ");
        sql.append(" <<AW_ORIG_USER>> , ");
        sql.append(" <<AW_ORIG_UPD_DATESTAMP>> , ");
        sql.append(" <<CHOICE>> )");
        
        procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        
        return procReqParameter;
    }//End addUpdRuleFuncArgs
    
    //Added for case 2418 - Business Rule Evaluation Bug - start
    /**
     * Delete all the expression for the given rule and condition
     *
     * @param ruleId rule id
     * @param conditionNumber 
     */
    public ProcReqParameter deleteRuleConditionExps(int ruleId, int conditionNumber)
    throws CoeusException, DBException{
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;
        
        param.addElement(new Parameter("AV_RULE_ID",
                DBEngineConstants.TYPE_INT, ""+ruleId)); 
        
        param.addElement(new Parameter("AV_CONDITION_NO",
                DBEngineConstants.TYPE_INT, ""+conditionNumber));
        StringBuffer sql = new StringBuffer(
                "{ <<OUT INTEGER STATUS>> = call FN_DELETE_RULE_CONDITION_EXPS(");
        sql.append(" <<AV_RULE_ID>> , ");
        sql.append(" <<AV_CONDITION_NO>> )}");
        
        procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        
        return procReqParameter;
    }
    //Added for case 2418 - Business Rule Evaluation Bug - end
}
