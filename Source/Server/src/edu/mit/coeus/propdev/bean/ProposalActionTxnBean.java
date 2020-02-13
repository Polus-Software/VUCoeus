/*
 * @(#)ProposalActionTxnBean.java 1.0 03/10/03 9:50 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 31-MAY-2007
 * by Leena
 */
package edu.mit.coeus.propdev.bean;

import edu.mit.coeus.award.bean.AwardLookUpDataTxnBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceDataTxnBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceDataTxnBean;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceFormBean;
import edu.mit.coeus.subcontract.bean.SubContractTxnBean;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
//import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.rolodexmaint.bean.*;

//import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.exception.CoeusException;
//import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.TypeConstants;
//import edu.mit.coeus.utils.CoeusConstants;

import edu.mit.coeus.utils.CoeusVector;

import java.util.Vector;
import java.util.HashMap;
//import java.util.Hashtable;
//import java.util.Comparator;
//import java.util.TreeSet;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.StringTokenizer;

import edu.mit.coeus.businessrules.bean.BusinessRulesBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.instprop.bean.InstituteProposalLookUpDataTxnBean;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.unit.bean.UnitDataTxnBean;
import edu.mit.coeus.unit.bean.UnitDetailFormBean;
/**
 * This class provides the methods for performing all procedure executions for
 * a Proposal Action . Various methods are used to fetch the Proposal Action
 * details from the Database.
 * All methods are used <code>DBEngineImpl</code> singleton instance for the
 * database interaction.
 *
 * @version 1.0 on March 22, 2004, 9:50 AM
 * @author  Prasanna Kumar K
 */

public class ProposalActionTxnBean implements TypeConstants{
    // Instance of a dbEngine
    private DBEngineImpl dbEngine;
    
    /** Creates a new instance of ProposalDevelopmentTxnBean */
    public ProposalActionTxnBean(){
        dbEngine = new DBEngineImpl();
    }
    
    /**
     *  This method used get individual question count
     *  <li>To fetch the data, it uses the function FN_GET_INDIV_QUEST_COUNT.
     *
     *  @return int count of Questions
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector checkEDIValidation(String proposalNumber)
    throws CoeusException, DBException {
        int count = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        String message = "";
        Vector returnValue = null;
        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber));
        
        param.addElement(new Parameter("MESSAGE",
                DBEngineConstants.TYPE_STRING, null, "out"));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER IS_VALID>> = "
                    +" call FN_CHECK_EDI_VALIDATIONS(<< PROPOSAL_NUMBER >>, << MESSAGE >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            count = Integer.parseInt(rowParameter.get("IS_VALID").toString());
            message = (String)rowParameter.get("MESSAGE");
            returnValue = new Vector(3,2);
            returnValue.addElement(new Integer(count));
            returnValue.addElement(message);
        }
        
        return returnValue;
    }
    
    /**
     *  Method used to Validate the given Proposal
     *  To update the data, it uses FN_PROPOSAL_VALIDATION_CHECK function.
     *
     *  @param proposalNumber Proposal Number
     *  @param unitNumber Unit Number
     *  @return int integer 1 if updated successfully else 0
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector proposalValidation(String proposalNumber , String unitNumber, String userId)
    throws CoeusException, DBException {
        int isUpdate = 0;
        Vector param= new Vector();
        Vector result = new Vector();
//        Vector procedures = new Vector(5,3);
        String ruleIds = "";
        Vector brokenRules = null;
        BusinessRulesBean businessRulesBean = null;
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber));
        param.add(new Parameter("UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING, unitNumber));
        param.add(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        param.add(new Parameter("RULE_IDS",
                DBEngineConstants.TYPE_STRING, ruleIds, "out"));
        /*param.add(new Parameter("RULES_ARRAY",
                                DBEngineConstants.TYPE_ARRAY, userId, "out"));        */
        result = dbEngine.executeFunctions("Coeus",
                "{ <<OUT INTEGER IS_VALID>> = "
                +" call FN_PROPOSAL_VALIDATION_CHECK(<<PROPOSAL_NUMBER>>, <<UNIT_NUMBER>>, <<UPDATE_USER>>, << RULE_IDS >> ) }", param);
        
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            isUpdate = Integer.parseInt(rowParameter.get("IS_VALID").toString());
            ruleIds = (String)rowParameter.get("RULE_IDS");
            //Get corresponding Rule Details
            if(ruleIds!=null){
                brokenRules = new Vector(3,2);
                StringTokenizer stringTokenizer = new StringTokenizer(ruleIds, ",");
                while(stringTokenizer.hasMoreTokens()){
                    String ruleId = stringTokenizer.nextToken();
                    businessRulesBean = getBusinessRules(Integer.parseInt(ruleId));
                    brokenRules.addElement(businessRulesBean);
                }
            }
        }
        return brokenRules;
    }
    
    /**
     *  Method used to Validate the given Proposal
     *  To update the data, it uses FN_PROPOSAL_VALIDATION function.
     *
     *  @param proposalNumber Proposal Number
     *  @param unitNumber Unit Number
     *  @return int integer 1 if updated successfully else 0
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getOwnedByUnit(String proposalNumber)
    throws CoeusException, DBException {
        String ownedByUnit = "";
        Vector param= new Vector();
        Vector result = new Vector();
//        Vector procedures = new Vector(5,3);
        
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber));
        
        result = dbEngine.executeFunctions("Coeus",
                "{ <<OUT STRING OWNED_BY_UNIT>> = "
                +" call FN_GET_OWNED_BY_UNIT(<<PROPOSAL_NUMBER>>) }", param);
        
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            ownedByUnit = rowParameter.get("OWNED_BY_UNIT").toString();
        }
        
        return ownedByUnit;
    }
    

    // JM 7-30-2012 added to get home unit for key person
    public CoeusVector getHomeUnitForKp(String personId)
    	throws CoeusException, DBException {
        Vector result = new Vector(3,2);
        Vector param = new Vector();
        CoeusVector cvHomeUnit = new CoeusVector();
        KeyPersonUnitBean keyPersonHomeUnitBean = new KeyPersonUnitBean();
        HashMap row = null;
        
        param.add(new Parameter("PERSON_ID",DBEngineConstants.TYPE_STRING, personId));
        if (dbEngine != null){
            result = dbEngine.executeRequest("Coeus",
                    "call VU_GET_HOME_UNIT_FOR_KP ( <<PERSON_ID>>,<<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if (result.size() > 0) {
            for(int i=0;i < result.size();i++){
            	keyPersonHomeUnitBean = new KeyPersonUnitBean();
                row = (HashMap) result.elementAt(i);
                keyPersonHomeUnitBean.setUnitNumber((String)row.get("HOME_UNIT"));
                keyPersonHomeUnitBean.setUnitName((String)row.get("UNIT_NAME"));
            }
        }
        cvHomeUnit.add(keyPersonHomeUnitBean);
        
        return cvHomeUnit;
    }
    // JM END
    
    /**
     * This method populates Unit Maps for the given Unit Number
     *
     * To fetch the data, it uses the procedure DW_GET_UNITMAP_HDR.
     *
     *  @param unitNumber Unit Number
     * @return Vector of Unit Map Beans
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getUnitMap(String unitNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        Vector vecMap = null;
        HashMap mapRow = null;
        UnitMapBean unitMapBean = null;
        Vector param= new Vector();
        param.addElement(new Parameter("UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING, unitNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_UNITMAP_HDR ( <<UNIT_NUMBER>> , "
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int mapsCount =result.size();
        if (mapsCount >0){
            vecMap = new Vector();
            for(int rowIndex=0;rowIndex<mapsCount;rowIndex++){
                unitMapBean = new UnitMapBean();
                mapRow = (HashMap) result.elementAt(rowIndex);
                unitMapBean.setMapId(mapRow.get("MAP_ID") == null ? 0 :
                    Integer.parseInt(mapRow.get("MAP_ID").toString()));
                unitMapBean.setMapType(
                        (String)mapRow.get("MAP_TYPE"));
                unitMapBean.setDefaultMapType(
                        (String)mapRow.get("DEFAULT_MAP_FLAG"));
                unitMapBean.setUnitNumber(
                        (String)mapRow.get("UNIT_NUMBER"));
                unitMapBean.setDescription(
                        (String)mapRow.get("DESCRIPTION"));
                unitMapBean.setUpdateTimeStamp(
                        (Timestamp)mapRow.get("UPDATE_TIMESTAMP"));
                unitMapBean.setUpdateUser(
                        (String)mapRow.get("UPDATE_USER"));
                /*
                 * Userid to Username enhancement - Start
                 * Added new property to get username
                 */
                if(mapRow.get("USERNAME") != null) {
                    unitMapBean.setUpdateUserName((String)mapRow.get("USERNAME"));
                } else {
                    unitMapBean.setUpdateUserName((String)mapRow.get("UPDATE_USER"));
                }
                //Userid to Username Enhancement - End
                unitMapBean.setMapDetails(getUnitMapDetails(unitMapBean.getMapId()));
                vecMap.add(unitMapBean);
            }
        }
        return vecMap;
    }
    
    /**
     * This method populates Unit Maps Details for the given Map Id
     *
     * To fetch the data, it uses the procedure DW_GET_UNITMAP_DET.
     *
     * @param mapId int
     * @return Vector of UnitMapDetailBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getUnitMapDetails(int mapId)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        Vector vecMap = null;
        HashMap mapRow = null;
        UnitMapDetailsBean unitMapDetailsBean = null;
        Vector param= new Vector();
        param.addElement(new Parameter("MAP_ID",
                DBEngineConstants.TYPE_INT, ""+mapId));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_UNITMAP_DET ( <<MAP_ID>> , "
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int mapsCount =result.size();
        if (mapsCount >0){
            vecMap = new Vector();
            for(int rowIndex=0;rowIndex<mapsCount;rowIndex++){
                unitMapDetailsBean = new UnitMapDetailsBean();
                mapRow = (HashMap) result.elementAt(rowIndex);
                unitMapDetailsBean.setMapId(mapRow.get("MAP_ID") == null ? 0 :
                    Integer.parseInt(mapRow.get("MAP_ID").toString()));
                unitMapDetailsBean.setLevelNumber(mapRow.get("LEVEL_NUMBER") == null ? 0 :
                    Integer.parseInt(mapRow.get("LEVEL_NUMBER").toString()));
                unitMapDetailsBean.setStopNumber(mapRow.get("STOP_NUMBER") == null ? 0 :
                    Integer.parseInt(mapRow.get("STOP_NUMBER").toString()));
                unitMapDetailsBean.setUserId((String)mapRow.get("USER_ID"));
                unitMapDetailsBean.setPrimaryApproverFlag(
                        mapRow.get("PRIMARY_APPROVER_FLAG") == null ? false :
                            mapRow.get("PRIMARY_APPROVER_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
                unitMapDetailsBean.setDescription(
                        (String)mapRow.get("DETAIL_DESCRIPTION"));
                unitMapDetailsBean.setUpdateTimeStamp(
                        (Timestamp)mapRow.get("UPDATE_TIMESTAMP"));
                unitMapDetailsBean.setUpdateUser(
                        (String)mapRow.get("UPDATE_USER"));
                /*
                 * UserId to UserName Enhancement - Start
                 * Added new property for getting username
                 */
                if(mapRow.get("UPDATE_USER") != null) {
                    unitMapDetailsBean.setUpdateUserName((String)mapRow.get("USERNAME"));
                } else {
                    unitMapDetailsBean.setUpdateUserName((String)mapRow.get("UPDATE_USER"));
                }
                //Userid to Username Enhancement - End
                //Added new fields "ROLE_TYPE_CODE, QUALIFIER_CODE, APPROVER_NUMBER, IS_ROLE
                unitMapDetailsBean.setRoleCode(
                        mapRow.get("ROLE_TYPE_CODE") == null ? "" : ""+Integer.parseInt(mapRow.get("ROLE_TYPE_CODE").toString()));
                unitMapDetailsBean.setRoleDescription((String)mapRow.get("ROLE_DESCRIPTION"));
                unitMapDetailsBean.setQualifierCode(
                        mapRow.get("QUALIFIER_CODE") == null ? "" : ""+Integer.parseInt(mapRow.get("QUALIFIER_CODE").toString()));
                unitMapDetailsBean.setQualifierDescription((String)mapRow.get("QUALIFIER_DESCRIPTION"));
                String roleType = (String)mapRow.get("IS_ROLE");
                unitMapDetailsBean.setRoleType(roleType.equalsIgnoreCase("Y")? true:false);
                unitMapDetailsBean.setApproverNumber(
                        mapRow.get("APPROVER_NUMBER") == null ? 0 : Integer.parseInt(mapRow.get("APPROVER_NUMBER").toString()));
                //Added for Coeus4.3 Enhancement PT ID 2785 -  Routing Enhancement - end
                vecMap.add(unitMapDetailsBean);
            }
        }
        return vecMap;
    }
    
    /**
     * This method returns Proposal Approval Map for the given Proposal Number and Map Id
     *
     * To fetch the data, it uses the procedure DW_GET_PROP_APPR_MAPS_FOR_PK.
     *
     * @param proposalNumber Proposal Number
     * @param mapId Map Number
     * @return ProposalApprovalMapBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProposalApprovalMapBean getProposalApprovalMapForPK(String proposalNumber, int mapId)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        Vector vecMap = null;
        HashMap mapRow = null;
        ProposalApprovalMapBean proposalApprovalMapBean = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber));
        param.addElement(new Parameter("MAP_ID",
                DBEngineConstants.TYPE_INT, ""+mapId));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_PROP_APPR_MAPS_FOR_PK ( <<PROPOSAL_NUMBER>> , <<MAP_ID>> ,  "
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int mapsCount =result.size();
        if (mapsCount >0){
            vecMap = new Vector();
            proposalApprovalMapBean = new ProposalApprovalMapBean();
            mapRow = (HashMap) result.elementAt(0);
            proposalApprovalMapBean.setProposalNumber(
                    (String)mapRow.get("PROPOSAL_NUMBER"));
            proposalApprovalMapBean.setMapId(mapRow.get("MAP_ID") == null ? 0 :
                Integer.parseInt(mapRow.get("MAP_ID").toString()));
            proposalApprovalMapBean.setParentMapId(mapRow.get("PARENT_MAP_ID") == null ? 0 :
                Integer.parseInt(mapRow.get("PARENT_MAP_ID").toString()));
            proposalApprovalMapBean.setDescription(
                    (String)mapRow.get("DESCRIPTION"));
            proposalApprovalMapBean.setSystemFlag(mapRow.get("SYSTEM_FLAG") == null ? false :
                mapRow.get("SYSTEM_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
            proposalApprovalMapBean.setApprovalStatus((String)mapRow.get("APPROVAL_STATUS"));
            proposalApprovalMapBean.setUpdateTimeStamp(
                    (Timestamp)mapRow.get("UPDATE_TIMESTAMP"));
            proposalApprovalMapBean.setUpdateUser(
                    (String)mapRow.get("UPDATE_USER"));
            proposalApprovalMapBean.setProposalApprovals(getProposalApprovalForMap(proposalNumber, mapId));
        }
        return proposalApprovalMapBean;
    }
    
    /**
     * This method populates Proposal Approval Details for the given Map Id
     *
     * To fetch the data, it uses the procedure DW_GET_UNITMAP_DET.
     *
     * @param mapId int
     * @return Vector of UnitMapDetailBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getProposalApprovalForMap(String proposalNumber, int mapId)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        CoeusVector vecMap = null;
        HashMap mapRow = null;
        ProposalApprovalBean proposalApprovalBean = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber));
        param.addElement(new Parameter("MAP_ID",
                DBEngineConstants.TYPE_INT, ""+mapId));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROP_APPR_FOR_PROP_MAP ( << PROPOSAL_NUMBER >> , <<MAP_ID>> , "
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int mapsCount =result.size();
        if (mapsCount >0){
            vecMap = new CoeusVector();
            for(int rowIndex=0;rowIndex<mapsCount;rowIndex++){
                proposalApprovalBean = new ProposalApprovalBean();
                mapRow = (HashMap) result.elementAt(rowIndex);
                proposalApprovalBean.setProposalNumber(
                        (String)mapRow.get("PROPOSAL_NUMBER"));
                proposalApprovalBean.setMapId(mapRow.get("MAP_ID") == null ? 0 :
                    Integer.parseInt(mapRow.get("MAP_ID").toString()));
                proposalApprovalBean.setLevelNumber(mapRow.get("LEVEL_NUMBER") == null ? 0 :
                    Integer.parseInt(mapRow.get("LEVEL_NUMBER").toString()));
                proposalApprovalBean.setStopNumber(mapRow.get("STOP_NUMBER") == null ? 0 :
                    Integer.parseInt(mapRow.get("STOP_NUMBER").toString()));
                proposalApprovalBean.setUserId((String)mapRow.get("USER_ID"));
                proposalApprovalBean.setPrimaryApproverFlag(
                        mapRow.get("PRIMARY_APPROVER_FLAG") == null ? false :
                            mapRow.get("PRIMARY_APPROVER_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
                proposalApprovalBean.setDescription(
                        (String)mapRow.get("DESCRIPTION"));
                proposalApprovalBean.setApprovalStatus(
                        (String)mapRow.get("APPROVAL_STATUS"));
                proposalApprovalBean.setSubmissionDate(
                        mapRow.get("SUBMISSION_DATE") == null ?
                            null : new Date(((Timestamp) mapRow.get(
                        "SUBMISSION_DATE")).getTime()));
                proposalApprovalBean.setApprovalDate(
                        mapRow.get("APPROVAL_DATE") == null ?
                            null : new Date(((Timestamp) mapRow.get(
                        "APPROVAL_DATE")).getTime()));
                proposalApprovalBean.setComments(
                        (String)mapRow.get("COMMENTS"));
                proposalApprovalBean.setUpdateTimeStamp(
                        (Timestamp)mapRow.get("UPDATE_TIMESTAMP"));
                proposalApprovalBean.setUpdateUser(
                        (String)mapRow.get("UPDATE_USER"));
                /*
                 * UserId to Username enhancement - Start
                 * Added new property to get the username
                 */
                if(mapRow.get("UPDATE_USER_NAME") != null) {
                    proposalApprovalBean.setUpdateUserName((String)mapRow.get("UPDATE_USER_NAME"));
                } else {
                    proposalApprovalBean.setUpdateUserName((String)mapRow.get("UPDATE_USER"));
                }
                //UserId to Username enhancement - End
                proposalApprovalBean.setUserName(
                        (String)mapRow.get("USER_NAME"));
                vecMap.add(proposalApprovalBean);
            }
        }
        return vecMap;
    }
    
    /**
     * This method populates Proposal Columns to Alter data for Data Overide
     *
     * To fetch the data, it uses the procedure DW_GET_UNITMAP_DET.
     *
     * @param mapId int
     * @return Vector of UnitMapDetailBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getProposalColumnsToAlter(String proposalNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        CoeusVector vecColumns = null;
        HashMap row = null;
        ProposalColumnsToAlterBean proposalColumnsToAlterBean = null;
        Vector param= new Vector();
        DepartmentPersonTxnBean departmentTxnBean = new DepartmentPersonTxnBean();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROP_COLUMNS_TO_ALTER ( "
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int columnsCount =result.size();
        CoeusVector vctProposalDataOverides = null;
        if (columnsCount >0){
            vecColumns = new CoeusVector();
            for(int rowIndex=0;rowIndex<columnsCount;rowIndex++){
                proposalColumnsToAlterBean = new ProposalColumnsToAlterBean();
                row = (HashMap) result.elementAt(rowIndex);
                proposalColumnsToAlterBean.setColumnName(
                        (String)row.get("COLUMN_NAME"));
                proposalColumnsToAlterBean.setColumnLabel(
                        (String)row.get("COLUMN_LABEL"));
                proposalColumnsToAlterBean.setDataType(
                        (String)row.get("DATA_TYPE"));
                proposalColumnsToAlterBean.setDataLength(row.get("DATA_LENGTH") == null ? 0 :
                    Integer.parseInt(row.get("DATA_LENGTH").toString()));
                proposalColumnsToAlterBean.setHasLookUp(row.get("HAS_LOOKUP") == null ? false :
                    row.get("HAS_LOOKUP").toString().equalsIgnoreCase("Y") ? true : false);
                proposalColumnsToAlterBean.setLookUpArgument(
                        (String)row.get("LOOKUP_ARGUMENT"));
                proposalColumnsToAlterBean.setLookUpWindow(
                        (String)row.get("LOOKUP_WINDOW"));
                proposalColumnsToAlterBean.setUpdateTimestamp(
                        (Timestamp)row.get("UPDATE_TIMESTAMP"));
                proposalColumnsToAlterBean.setUpdateUser(
                        (String)row.get("UPDATE_USER"));
                //Modified for Coeus 4.3 enhancement: Data Override Nullable validation - start
                //Set the nullable field
                proposalColumnsToAlterBean.setNullable(row.get("NULLABLE") == null ? false :
                    row.get("NULLABLE").toString().equalsIgnoreCase("Y") ? true : false);
                proposalColumnsToAlterBean.setDataPrecision(row.get("DATA_PRECISION") == null ? 0 :
                    Integer.parseInt(row.get("DATA_PRECISION").toString()));
                //Modified for Coeus 4.3 enhancement : Data Override Nullable validation - end
                
                //Get corresponding changed data for this column
                vctProposalDataOverides = getProposalDataOverides(proposalNumber, proposalColumnsToAlterBean.getColumnName());
                proposalColumnsToAlterBean.setProposalDataOverides(vctProposalDataOverides);
                if(vctProposalDataOverides==null){
                    proposalColumnsToAlterBean.setDefaultValue(
                            getProposalDataForDataOveride(proposalNumber, proposalColumnsToAlterBean.getColumnName(), proposalColumnsToAlterBean.getDataType()));
                    //If this is a look up
                    //Get the Description for this code
                    if(proposalColumnsToAlterBean.isHasLookUp()){
                        String lookUpCode = proposalColumnsToAlterBean.getDefaultValue();
                        String lookUpWindow = proposalColumnsToAlterBean.getLookUpWindow();
                        String lookUpArgument = proposalColumnsToAlterBean.getLookUpArgument();
                        if(lookUpCode!=null && !lookUpCode.equals("")){
                            if(lookUpWindow!=null){
                                Vector vctArgumentValues = null;
                                if(lookUpWindow.equalsIgnoreCase("w_arg_Value_list")){
                                    vctArgumentValues = departmentTxnBean.getArgumentValueList(lookUpArgument);
                                    ComboBoxBean comboBoxBean = null;
                                    if(vctArgumentValues!=null){
                                        for(int valueRow = 0 ; valueRow < vctArgumentValues.size(); valueRow++){
                                            comboBoxBean = (ComboBoxBean)vctArgumentValues.elementAt(valueRow);
                                            if(comboBoxBean!=null &&
                                                    comboBoxBean.getCode().equalsIgnoreCase(lookUpCode)){
                                                proposalColumnsToAlterBean.setDefaultValue(comboBoxBean.getDescription());
                                                break;
                                            }
                                        }
                                    }
                                }else if(lookUpWindow.equalsIgnoreCase("w_select_cost_element")){
                                    vctArgumentValues = departmentTxnBean.getCostElements();
                                    ComboBoxBean comboBoxBean = null;
                                    if(vctArgumentValues!=null){
                                        for(int valueRow = 0 ; valueRow < vctArgumentValues.size(); valueRow++){
                                            comboBoxBean = (ComboBoxBean)vctArgumentValues.elementAt(valueRow);
                                            if(comboBoxBean!=null &&
                                                    comboBoxBean.getCode().equalsIgnoreCase(lookUpCode)){
                                                proposalColumnsToAlterBean.setDefaultValue(comboBoxBean.getDescription());
                                                break;
                                            }
                                        }
                                    }
                                }else if(lookUpWindow.equalsIgnoreCase("w_Person_Select")){
                                    UserDetailsBean userDetailsBean = new UserDetailsBean();
                                    PersonInfoBean personInfoBean = userDetailsBean.getPersonInfo(lookUpCode);
                                    if(personInfoBean!=null){
                                        proposalColumnsToAlterBean.setDefaultValue(personInfoBean.getFullName());
                                    }
                                }else if(lookUpWindow.equalsIgnoreCase("w_Unit_Select")){
                                    UnitDataTxnBean unitDataTxnBean = new UnitDataTxnBean();
                                    UnitDetailFormBean unitDetailFormBean = unitDataTxnBean.getUnitDetails(lookUpCode);
                                    if(proposalColumnsToAlterBean!=null){
                                        proposalColumnsToAlterBean.setDefaultValue(unitDetailFormBean.getUnitName());
                                    }
                                // 4580: Add organization and sponsor search in custom elements - Start
                                }else if("w_organization_select".equalsIgnoreCase(lookUpWindow)){
                                    OrganizationMaintenanceDataTxnBean OrganizationMaintDataTxnBean = new OrganizationMaintenanceDataTxnBean();
                                    OrganizationMaintenanceFormBean OrganizationMaintFormBean = OrganizationMaintDataTxnBean.getOrganizationMaintenanceDetails(lookUpCode);
                                    if(proposalColumnsToAlterBean!=null && OrganizationMaintFormBean != null){
                                        proposalColumnsToAlterBean.setDefaultValue(OrganizationMaintFormBean.getOrganizationName());
                                    } 
                                 }else if("w_sponsor_select".equalsIgnoreCase(lookUpWindow)){
                                    SponsorMaintenanceDataTxnBean sponsorMaintDataTxnBean = new SponsorMaintenanceDataTxnBean();
                                    SponsorMaintenanceFormBean sponsorMaintFormBean = sponsorMaintDataTxnBean.getSponsorMaintenanceDetails(lookUpCode);
                                    if(proposalColumnsToAlterBean!=null && sponsorMaintFormBean != null){
                                        proposalColumnsToAlterBean.setDefaultValue(sponsorMaintFormBean.getName());
                                    }     
                                // 4580: Add organization and sponsor search in custom elements - End   
                                }else if(lookUpWindow.equalsIgnoreCase("w_Rolodex_Select")){
                                    RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean = new RolodexMaintenanceDataTxnBean();
                                    RolodexDetailsBean rolodexDetailsBean = rolodexMaintenanceDataTxnBean.getRolodexMaintenanceDetails(lookUpCode);
                                    if(rolodexDetailsBean!=null){
                                        proposalColumnsToAlterBean.setDefaultValue(rolodexDetailsBean.getFirstName());
                                    }
                                }else{
                                    vctArgumentValues = departmentTxnBean.getArgumentCodeDescription(lookUpArgument);
                                    ComboBoxBean comboBoxBean = null;
                                    if(vctArgumentValues!=null){
                                        for(int valueRow = 0 ; valueRow < vctArgumentValues.size(); valueRow++){
                                            comboBoxBean = (ComboBoxBean)vctArgumentValues.elementAt(valueRow);
                                            if(comboBoxBean!=null &&
                                                    comboBoxBean.getCode().equalsIgnoreCase(lookUpCode)){
                                                proposalColumnsToAlterBean.setDefaultValue(comboBoxBean.getDescription());
                                                break;
                                            }
                                        }
                                    }
                                }
                                /*
                                if(lookUpWindow.equalsIgnoreCase("w_arg_Value_list") || lookUpWindow.equalsIgnoreCase("w_select_cost_element")){
                                    HashMap valueList = null;
                                    if(vctArgumentValues!=null){
                                        for(int valueRow = 0 ; valueRow < vctArgumentValues.size(); valueRow++){
                                            valueList = (HashMap)vctArgumentValues.elementAt(valueRow);
                                            if(valueList!=null &&
                                                valueList.containsKey(lookUpCode)){
                                                proposalColumnsToAlterBean.setDefaultValue((String)valueList.get(lookUpCode));
                                                break;
                                            }
                                        }
                                    }
                                }else{
                                    ComboBoxBean comboBoxBean = null;
                                    if(vctArgumentValues!=null){
                                        for(int valueRow = 0 ; valueRow < vctArgumentValues.size(); valueRow++){
                                            comboBoxBean = (ComboBoxBean)vctArgumentValues.elementAt(valueRow);
                                            if(comboBoxBean!=null &&
                                                comboBoxBean.getCode().equalsIgnoreCase(lookUpCode)){
                                                proposalColumnsToAlterBean.setDefaultValue(comboBoxBean.getDescription());
                                                break;
                                            }
                                        }
                                    }
                                }*/
                            }
                        }
                    }
                }
                vecColumns.add(proposalColumnsToAlterBean);
            }
        }
        return vecColumns;
    }
    
    /**
     * This method populates Proposal Data Overides data
     *
     * To fetch the data, it uses the procedure DW_GET_PROPOSAL_DATA_OVERRIDES.
     *
     * @param mapId int
     * @return Vector of UnitMapDetailBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getProposalDataOverides(String proposalNumber, String columnName)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        CoeusVector vecColumns = null;
        HashMap row = null;
        ProposalDataOveridesBean proposalDataOveridesBean = null;
        Vector param= new Vector();
        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber));
        param.addElement(new Parameter("COLUMN_NAME",
                DBEngineConstants.TYPE_STRING, columnName));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROPOSAL_DATA_OVERRIDES ( << PROPOSAL_NUMBER >> , << COLUMN_NAME >> ,"
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int columnsCount =result.size();
        if (columnsCount >0){
            vecColumns = new CoeusVector();
            for(int rowIndex=0;rowIndex<columnsCount;rowIndex++){
                proposalDataOveridesBean = new ProposalDataOveridesBean();
                row = (HashMap) result.elementAt(rowIndex);
                proposalDataOveridesBean.setProposalNumber(
                        (String)row.get("PROPOSAL_NUMBER"));
                proposalDataOveridesBean.setColumnName(
                        (String)row.get("COLUMN_NAME"));
                proposalDataOveridesBean.setChangedNumber(
                        row.get("CHANGE_NUMBER") == null ? 0 :
                            Integer.parseInt(row.get("CHANGE_NUMBER").toString()));
                proposalDataOveridesBean.setChangedValue(
                        (String)row.get("CHANGED_VALUE"));
                proposalDataOveridesBean.setDisplayValue(
                        (String)row.get("DISPLAY_VALUE"));
                proposalDataOveridesBean.setOldDisplayValue(
                        (String)row.get("OLD_DISPLAY_VALUE"));
                proposalDataOveridesBean.setComments(
                        (String)row.get("COMMENTS"));
                proposalDataOveridesBean.setUpdateTimestamp(
                        (Timestamp)row.get("UPDATE_TIMESTAMP"));
                proposalDataOveridesBean.setUpdateUser(
                        (String)row.get("UPDATE_USER"));
                proposalDataOveridesBean.setColumnLabel(
                        (String)row.get("COLUMN_LABEL"));
                proposalDataOveridesBean.setHasLookUp(
                        row.get("HAS_LOOKUP") == null ? false :
                            row.get("HAS_LOOKUP").toString().equalsIgnoreCase("Y") ? true : false);
                vecColumns.add(proposalDataOveridesBean);
            }
        }
        return vecColumns;
    }
    
    /**
     * This method returns Proposal Approval Map for the given Proposal Number and Map Id
     *
     * To fetch the data, it uses the procedure DW_GET_PROP_APPR_MAPS_FOR_PK.
     *
     * @param proposalNumber Proposal Number
     * @param mapId Map Number
     * @return ProposalApprovalMapBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getProposalApprovalMaps(String proposalNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        CoeusVector vecMap = null;
        HashMap mapRow = null;
        ProposalApprovalMapBean proposalApprovalMapBean = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_PROPAPPROVALMAPS ( <<PROPOSAL_NUMBER>> , "
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int mapsCount =result.size();
        if (mapsCount >0){
            vecMap = new CoeusVector();
            for(int row = 0; row < mapsCount; row++){
                proposalApprovalMapBean = new ProposalApprovalMapBean();
                mapRow = (HashMap) result.elementAt(row);
                proposalApprovalMapBean.setProposalNumber(
                        (String)mapRow.get("PROPOSAL_NUMBER"));
                proposalApprovalMapBean.setMapId(mapRow.get("MAP_ID") == null ? 0 :
                    Integer.parseInt(mapRow.get("MAP_ID").toString()));
                proposalApprovalMapBean.setParentMapId(mapRow.get("PARENT_MAP_ID") == null ? 0 :
                    Integer.parseInt(mapRow.get("PARENT_MAP_ID").toString()));
                proposalApprovalMapBean.setDescription(
                        (String)mapRow.get("DESCRIPTION"));
                proposalApprovalMapBean.setSystemFlag(mapRow.get("SYSTEM_FLAG") == null ? false :
                    mapRow.get("SYSTEM_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
                proposalApprovalMapBean.setApprovalStatus((String)mapRow.get("APPROVAL_STATUS"));
                proposalApprovalMapBean.setUpdateTimeStamp(
                        (Timestamp)mapRow.get("UPDATE_TIMESTAMP"));
                proposalApprovalMapBean.setUpdateUser(
                        (String)mapRow.get("UPDATE_USER"));
                
                
                
                proposalApprovalMapBean.setProposalApprovals(getProposalApprovalForMap(proposalNumber, proposalApprovalMapBean.getMapId()));
                vecMap.addElement(proposalApprovalMapBean);
            }
        }
        return vecMap;
    }
    
    /**
     * This method populates Proposal Approval Details for the given Map Id
     *
     * To fetch the data, it uses the procedure DW_GET_UNITMAP_DET.
     *
     * @param mapId int
     * @return Vector of UnitMapDetailBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getProposalApprovalStop(String proposalNumber, int mapId, int levelNumber, int stopNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        CoeusVector vecMap = null;
        HashMap mapRow = null;
        ProposalApprovalBean proposalApprovalBean = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber));
        param.addElement(new Parameter("MAP_ID",
                DBEngineConstants.TYPE_INT, ""+mapId));
        param.addElement(new Parameter("LEVEL_NUMBER",
                DBEngineConstants.TYPE_INT, ""+levelNumber));
        param.addElement(new Parameter("STOP_NUMBER",
                DBEngineConstants.TYPE_INT, ""+stopNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_PROP_APPR_STOP ( << PROPOSAL_NUMBER >> , <<MAP_ID>> , <<LEVEL_NUMBER>> , <<STOP_NUMBER>> ,  "
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int mapsCount =result.size();
        if (mapsCount >0){
            vecMap = new CoeusVector();
            for(int rowIndex=0;rowIndex<mapsCount;rowIndex++){
                proposalApprovalBean = new ProposalApprovalBean();
                mapRow = (HashMap) result.elementAt(rowIndex);
                proposalApprovalBean.setProposalNumber(
                        (String)mapRow.get("PROPOSAL_NUMBER"));
                proposalApprovalBean.setMapId(mapRow.get("MAP_ID") == null ? 0 :
                    Integer.parseInt(mapRow.get("MAP_ID").toString()));
                proposalApprovalBean.setLevelNumber(mapRow.get("LEVEL_NUMBER") == null ? 0 :
                    Integer.parseInt(mapRow.get("LEVEL_NUMBER").toString()));
                proposalApprovalBean.setStopNumber(mapRow.get("STOP_NUMBER") == null ? 0 :
                    Integer.parseInt(mapRow.get("STOP_NUMBER").toString()));
                proposalApprovalBean.setUserId((String)mapRow.get("USER_ID"));
                proposalApprovalBean.setPrimaryApproverFlag(
                        mapRow.get("PRIMARY_APPROVER_FLAG") == null ? false :
                            mapRow.get("PRIMARY_APPROVER_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
                proposalApprovalBean.setDescription(
                        (String)mapRow.get("DESCRIPTION"));
                proposalApprovalBean.setApprovalStatus(
                        (String)mapRow.get("APPROVAL_STATUS"));
                proposalApprovalBean.setSubmissionDate(
                        mapRow.get("SUBMISSION_DATE") == null ?
                            null : new Date(((Timestamp) mapRow.get(
                        "SUBMISSION_DATE")).getTime()));
                proposalApprovalBean.setApprovalDate(
                        mapRow.get("APPROVAL_DATE") == null ?
                            null : new Date(((Timestamp) mapRow.get(
                        "APPROVAL_DATE")).getTime()));
                proposalApprovalBean.setComments(
                        (String)mapRow.get("COMMENTS"));
                proposalApprovalBean.setUpdateTimeStamp(
                        (Timestamp)mapRow.get("UPDATE_TIMESTAMP"));
                proposalApprovalBean.setUpdateUser(
                        (String)mapRow.get("UPDATE_USER"));
                vecMap.add(proposalApprovalBean);
            }
        }
        return vecMap;
    }
    
    /**
     *  Method used to Approves a Proposal.
     *  To update the data, it uses FN_CHECK_VALID_PROP_NUMBER procedure.
     *
     *  @param vctPropMaps Vector of ProposalApprovalMapBean
     *  @return boolean true if updated successfully else false
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int checkValidInstProposalNumber(String instProposalNumber)
    throws CoeusException, DBException{
        
        Vector param = new Vector();
//        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        int isValid = 0;
        Vector result = new Vector();
        
        param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, instProposalNumber));
        
        result = dbEngine.executeFunctions("Coeus",
                "{ <<OUT INTEGER IS_VALID>> = "
                +" call FN_CHECK_VALID_PROP_NUMBER( "
                +" << PROPOSAL_NUMBER >> ) }", param);
        
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            isValid = Integer.parseInt(rowParameter.get("IS_VALID").toString());
        }
        
        return isValid;
    }
    
    /**
     * This method gets business rule details for a given rule Id
     *
     * To fetch the data, it uses the procedure DW_GET_BROKEN_RULES.
     *
     * @param ruleId int
     * @return Vector of BusinessRuleBeans
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public BusinessRulesBean getBusinessRules(int ruleId)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
//        Vector vecMap = null;
        HashMap mapRow = null;
        BusinessRulesBean businessRulesBean = null;
        Vector param= new Vector();
        param.addElement(new Parameter("RULE_ID",
                DBEngineConstants.TYPE_INT, ""+ruleId));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_BROKEN_RULES ( << RULE_ID >> ,  "
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int ruleCount =result.size();
        if(ruleCount > 0){
            businessRulesBean = new BusinessRulesBean();
            mapRow = (HashMap) result.elementAt(0);
            businessRulesBean.setDescription(
                    (String)mapRow.get("DESCRIPTION"));
            businessRulesBean.setRuleType(
                    (String)mapRow.get("RULE_TYPE"));
            businessRulesBean.setUnitName(
                    (String)mapRow.get("UNIT_NAME"));
        }
        return businessRulesBean;
    }
    
    /**
     *  Method is used to get Institute Proposal Number from the given Development Proposal Number.
     *  To update the data, it uses FN_GET_INST_PROP_NUM procedure.
     *
     *  @param devProposalNumber String
     *  @return String Institute Proposal Number
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getInstPropNumber(String devProposalNumber)
    throws CoeusException, DBException{
        
        Vector param = new Vector();
//        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        String instituteProposalNumber = null;
        Vector result = new Vector();
        
        param = new Vector();
        param.addElement(new Parameter("DEV_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, devProposalNumber));
        
        result = dbEngine.executeFunctions("Coeus",
                "{ <<OUT STRING INST_PROPOSAL_NUMBER>> = "
                +" call FN_GET_INST_PROP_NUM( "
                +" << DEV_PROPOSAL_NUMBER >> ) }", param);
        
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            instituteProposalNumber = (String)rowParameter.get("INST_PROPOSAL_NUMBER");
        }
        
        return instituteProposalNumber;
    }
    
    
    /**
     *  Method is used to set Post Submission Status of a Development Proposal
     *  To update the data, it uses FN_CHECK_EDI_ALLOWED_FOR_PROP procedure.
     *
     *  @param devProposalNumber String
     *  @return String Institute Proposal Number
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int checkEDIAllowedForProposal(String proposalNumber)
    throws CoeusException, DBException{
        
        Vector param = new Vector();
//        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        int isAllowed = 0;
        Vector result = new Vector();
        
        param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber));
        
        result = dbEngine.executeFunctions("Coeus",
                "{ <<OUT INTEGER IS_ALLOWED>> = "
                +" call FN_CHECK_EDI_ALLOWED_FOR_PROP( "
                +" << PROPOSAL_NUMBER >> ) }", param);
        
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            isAllowed = Integer.parseInt(rowParameter.get("IS_ALLOWED").toString());
        }
        
        return isAllowed;
    }
    
    /**
     * This method populates distinct Proposal Approval Details for the given Proposal Number
     * This is used to insert Approvers into OSP$EPS_PROP_USER_ROLES table is they do not have Approver right
     * To fetch the data, it uses the procedure GET_PROPAPPR_FOR_PROPNO.
     *
     * @param mapId int
     * @return Vector of UnitMapDetailBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getProposalApprovalForProposalNumber(String proposalNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        CoeusVector vecMap = null;
        HashMap mapRow = null;
        ProposalApprovalBean proposalApprovalBean = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROPAPPR_FOR_PROPNO ( << PROPOSAL_NUMBER >> , "
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int mapsCount =result.size();
        if (mapsCount >0){
            vecMap = new CoeusVector();
            for(int rowIndex=0;rowIndex<mapsCount;rowIndex++){
                proposalApprovalBean = new ProposalApprovalBean();
                mapRow = (HashMap) result.elementAt(rowIndex);
                proposalApprovalBean.setProposalNumber(
                        (String)mapRow.get("PROPOSAL_NUMBER"));
                proposalApprovalBean.setUserId((String)mapRow.get("USER_ID"));
                vecMap.add(proposalApprovalBean);
            }
        }
        return vecMap;
    }
    
    /**
     * This method populates Proposal Approval Status Details for the given Proposal Number
     *
     * To fetch the data, it uses the procedure DW_GET_PROPAPPROVAL_STATUS.
     *
     * @param mapId int
     * @return Vector of UnitMapDetailBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProposalApprovalForStatus(String proposalNumber, String userId, String approvalStatus, boolean isPrimaryApprover)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        Vector vecMap = null;
        HashMap mapRow = null;
        ProposalApprovalBean proposalApprovalBean = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber));
        param.addElement(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("APPROVAL_STATUS",
                DBEngineConstants.TYPE_STRING, approvalStatus));
        param.addElement(new Parameter("PRIMARY_APPROVER",
                DBEngineConstants.TYPE_STRING, isPrimaryApprover ? "Y" : "N"));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROP_APPROVAL_FOR_STATUS ( << PROPOSAL_NUMBER >>, << USER_ID >>, << APPROVAL_STATUS >>, << PRIMARY_APPROVER >>,"
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int mapsCount =result.size();
        if (mapsCount >0){
            vecMap = new Vector();
            for(int rowIndex=0;rowIndex<mapsCount;rowIndex++){
                proposalApprovalBean = new ProposalApprovalBean();
                mapRow = (HashMap) result.elementAt(rowIndex);
                proposalApprovalBean.setProposalNumber(
                        (String)mapRow.get("PROPOSAL_NUMBER"));
                proposalApprovalBean.setMapId(mapRow.get("MAP_ID") == null ? 0 :
                    Integer.parseInt(mapRow.get("MAP_ID").toString()));
                proposalApprovalBean.setLevelNumber(mapRow.get("LEVEL_NUMBER") == null ? 0 :
                    Integer.parseInt(mapRow.get("LEVEL_NUMBER").toString()));
                proposalApprovalBean.setStopNumber(mapRow.get("STOP_NUMBER") == null ? 0 :
                    Integer.parseInt(mapRow.get("STOP_NUMBER").toString()));
                proposalApprovalBean.setUserId((String)mapRow.get("USER_ID"));
                proposalApprovalBean.setDescription(
                        (String)mapRow.get("DESCRIPTION"));
                proposalApprovalBean.setApprovalStatus(
                        (String)mapRow.get("APPROVAL_STATUS"));
                proposalApprovalBean.setSubmissionDate(
                        mapRow.get("SUBMISSION_DATE") == null ?
                            null : new Date(((Timestamp) mapRow.get(
                        "SUBMISSION_DATE")).getTime()));
                proposalApprovalBean.setApprovalDate(
                        mapRow.get("APPROVAL_DATE") == null ?
                            null : new Date(((Timestamp) mapRow.get(
                        "APPROVAL_DATE")).getTime()));
                proposalApprovalBean.setComments(
                        (String)mapRow.get("COMMENTS"));
                proposalApprovalBean.setUpdateTimeStamp(
                        (Timestamp)mapRow.get("UPDATE_TIMESTAMP"));
                proposalApprovalBean.setUpdateUser(
                        (String)mapRow.get("UPDATE_USER"));
                vecMap.add(proposalApprovalBean);
            }
        }
        return vecMap;
    }
    
    /**
     * Method used to get users of all Roles for a given proposal number
     * from OSP$EPS_PROP_USER_ROLES and OSP$USER
     * <li>To fetch the data, it uses DW_GET_USERS_FOR_ALLPROPROLES.
     *
     * @param proposalNumber is used as input parameter for the procedure.
     * @return Vector collections of user id
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getUsersForAllPropRoles(String proposalNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        CoeusVector vecProposalUser = null;
        HashMap rightRoleRow=null;
        Vector param= new Vector();
//        String userId ="";
        ProposalUserRoleFormBean proposalUserRoleFormBean = null;
        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_USERS_FOR_ALLPROPROLES ( <<PROPOSAL_NUMBER>> , "
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        int listSize = result.size();
        if (listSize >0){
            vecProposalUser = new CoeusVector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                proposalUserRoleFormBean = new ProposalUserRoleFormBean();
                
                rightRoleRow = (HashMap)result.elementAt(rowIndex);
                proposalUserRoleFormBean.setProposalNumber( (String)
                rightRoleRow.get("PROPOSAL_NUMBER"));
                proposalUserRoleFormBean.setUserId( (String)
                rightRoleRow.get("USER_ID"));
                proposalUserRoleFormBean.setRoleId(
                        Integer.parseInt(rightRoleRow.get("ROLE_ID") == null ? "0" :
                            rightRoleRow.get("ROLE_ID").toString()));
                proposalUserRoleFormBean.setUserName( (String)
                rightRoleRow.get("USER_NAME"));
                proposalUserRoleFormBean.setStatus(
                        rightRoleRow.get("STATUS").toString().charAt(0));
                proposalUserRoleFormBean.setUpdateTimestamp(
                        (Timestamp)rightRoleRow.get("UPDATE_TIMESTAMP"));
                proposalUserRoleFormBean.setUpdateUser( (String)
                rightRoleRow.get("UPDATE_USER"));
                
                vecProposalUser.add(proposalUserRoleFormBean);
            }
        }
        return vecProposalUser;
    }
    
    /**
     *  Method is used to check whether person can submit the given Proposal
     *  To update the data, it uses FN_CHECK_PERSON_CAN_SUBMIT procedure.
     *
     *  @param userId String
     *  @param devProposalNumber String
     *  @return int whether has right
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int checkPersonCanSubmit(String userId, String proposalNumber)
    throws CoeusException, DBException{
        
        Vector param = new Vector();
        int isAllowed = 0;
        Vector result = new Vector();
        
        param = new Vector();
        param.addElement(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber));
        
        result = dbEngine.executeFunctions("Coeus",
                "{ <<OUT INTEGER IS_ALLOWED>> = "
                +" call FN_CHECK_PERSON_CAN_SUBMIT( "
                +" << USER_ID >>, << PROPOSAL_NUMBER >> ) }", param);
        
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            isAllowed = Integer.parseInt(rowParameter.get("IS_ALLOWED").toString());
        }
        
        return isAllowed;
    }
    
    /**
     *  This method used to check if there are direct or indirect maps for the given proposal number
     *  <li>To fetch the data, it uses the function FN_CHECK_MAPS_EXIST.
     *
     *  @return int count for the proposal number     *
     *  @param String proposal number
     *  @param String unit number
     *  @param String Update User
     *  @param String option
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int checkMapsExist(String proposalNumber , String unitNumber, String updateUser, String option)
    throws CoeusException, DBException {
        int isExist = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.add(new Parameter("UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING,unitNumber));
        param.add(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING,updateUser));
        param.add(new Parameter("OPTION",
                DBEngineConstants.TYPE_STRING,option));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER IS_EXIST>> = "
                    +" call FN_CHECK_MAPS_EXIST(<< PROPOSAL_NUMBER >>, << UNIT_NUMBER >>, << UPDATE_USER >>, << OPTION>> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            isExist = Integer.parseInt(rowParameter.get("IS_EXIST").toString());
        }
        return isExist;
    }
    
    /**
     *  This method used get Dev Proposal data for Data Overide. This is called when there are no
     *  Proposal data overide entries and Proposal Data has to be displayed as old value
     *  <li>To fetch the data, it uses the function FN_GET_DATAOVERIDE_PROP_DATA.
     *
     *  @return String value of Proposal data column
     *  @param String Proposal number
     *  @param String Column Name
     *  @param String Data type of the column
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getProposalDataForDataOveride(String proposalNumber, String columnName, String dataType)
    throws CoeusException, DBException {
        String columnValue = "";
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber));
        param.add(new Parameter("COLUMN_NAME",
                DBEngineConstants.TYPE_STRING, columnName));
        param.add(new Parameter("DATA_TYPE",
                DBEngineConstants.TYPE_STRING, dataType));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT STRING COLUMN_VALUE>> = "
                    +" call FN_GET_DATAOVERIDE_PROP_DATA(<< PROPOSAL_NUMBER >>, << COLUMN_NAME >>, << DATA_TYPE >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            columnValue = (String)rowParameter.get("COLUMN_VALUE");
        }
        return columnValue;
    }
    
    
    /** Method used to get Proposal Roles from OSP$MESSAGE for the given Message Id.
     * <li>To fetch the data, it uses DW_GET_MESSAGE.
     *
     * @return MessageBean MessageBean
     * @param messageId is used to get MessageBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public MessageBean getMessage(String messageId) throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap messageRow = null;
        MessageBean messageBean = null;
        
        param.addElement(new Parameter("MESSAGE_ID",
                DBEngineConstants.TYPE_STRING, messageId));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call DW_GET_MESSAGE ( <<MESSAGE_ID>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
//        Vector messageList = null;
        if(listSize>0){
            messageBean = new MessageBean();
            messageRow = (HashMap)result.elementAt(0);
            messageBean.setMessageId(messageId); //Modified for Null value
            messageBean.setMessage((String)
            messageRow.get("MESSAGE"));
            messageBean.setUpdateTimeStamp((Timestamp)
            messageRow.get("UPDATE_TIMESTAMP"));
            messageBean.setUpdateUser( (String)
            messageRow.get("UPDATE_USER"));
        }
        return messageBean;
    }
    
    /** Method used to get Inbox details from OSP$INBOX for the given message Id.
     * <li>To fetch the data, it uses GET_PROPOSAL_ROLES_FOR_UNIT.
     *
     * @return vector
     * @param messageId
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getInboxForMessage(int messageId) throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap inboxRow = null;
        InboxBean inboxBean = null;
        
        param.addElement(new Parameter("MESSAGE_ID",
                DBEngineConstants.TYPE_INT, ""+messageId));
        //Coeus enhancement Case #1828 :start
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_INBOX_FOR_MESSAGE ( <<MESSAGE_ID>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
            //Coeus enhancement Case #1828 :end
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector inboxList = null;
        if(listSize>0){
            inboxList = new Vector(3,2);
            for(int row=0;row < listSize; row++){
                inboxRow = (HashMap)result.elementAt(row);
                inboxBean = new InboxBean();
                //modified for CASE #1828 Begin
                inboxBean.setProposalNumber(inboxRow.get("MODULE_ITEM_KEY").toString());
                inboxBean.setModuleCode(
                        Integer.parseInt(inboxRow.get("MODULE_CODE") == null ? "0" : inboxRow.get("MODULE_CODE").toString()));
                //modified for CASE #1828 End
                inboxBean.setToUser((String)
                inboxRow.get("TO_USER"));
                inboxBean.setMessageId((String)
                inboxRow.get("MESSAGE_ID"));
                inboxBean.setFromUser((String)
                inboxRow.get("FROM_USER"));
                inboxBean.setArrivalDate(
                        (Timestamp) inboxRow.get("ARRIVAL_DATE"));
                inboxBean.setSubjectType(inboxRow.get("SUBJECT_TYPE") == null ?
                    ' ' : inboxRow.get("SUBJECT_TYPE").toString().charAt(0));
                inboxBean.setOpenedFlag(inboxRow.get("OPENED_FLAG") == null ?
                    ' ' : inboxRow.get("OPENED_FLAG").toString().charAt(0));
                inboxBean.setUpdateTimeStamp((Timestamp)
                inboxRow.get("UPDATE_TIMESTAMP"));
                inboxBean.setUpdateUser( (String)
                inboxRow.get("UPDATE_USER"));
                inboxBean.setAw_ArrivalDate(
                        (Timestamp) inboxRow.get("ARRIVAL_DATE"));
                inboxBean.setAw_MessageId(
                        Integer.parseInt(inboxRow.get("MESSAGE_ID") == null ? "0" : inboxRow.get("MESSAGE_ID").toString()));
                inboxBean.setAw_ProposalNumber(inboxRow.get("PROPOSAL_NUMBER") == null ? "" :
                    inboxRow.get("PROPOSAL_NUMBER").toString()); //COEUSQA-2336_Subcontract email moving from unresolved to resolved
                inboxBean.setAw_ToUser((String)
                inboxRow.get("TO_USER"));
                inboxList.addElement(inboxBean);
            }
        }
        return inboxList;
    }
    
    /**
     *  This method used get next Message Id from OSP$MESSAGE table
     *  <li>To fetch the data, it uses the function FN_GET_MESSAGE_ID.
     *
     *  @return int next message Id
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getNextMessageId()
    throws CoeusException, DBException {
        String messageId = "";
        Vector param= new Vector();
        Vector result = new Vector();
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER NEXT_ID>> = "
                    +" call FN_GET_MESSAGE_ID() }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            messageId = (String)rowParameter.get("NEXT_ID");
        }
        return messageId;
    }
    
    /** Method used to get Inbox details from OSP$INBOX for the given message Id.
     * <li>To fetch the data, it uses GET_INBOX_FOR_USER.
     *
     * @return vector
     * @param toUuser
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getInboxForUser(String toUser) throws CoeusException, DBException{
        
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap inboxRow = null;
        InboxBean inboxBean = null;
        
        param.addElement(new Parameter("TO_USER",
                DBEngineConstants.TYPE_STRING, toUser));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_INBOX_FOR_USER ( <<TO_USER>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector inboxList = null;
        if(listSize>0){
            //Added for Coeus enhancement Case #1828 :step 1:start
            ProposalDevelopmentTxnBean proposalDevelopmentTxnBean=new ProposalDevelopmentTxnBean();
            AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
            ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
            InstituteProposalLookUpDataTxnBean instituteProposalLookUpDataTxnBean = new InstituteProposalLookUpDataTxnBean();
            Vector proposalStatusCode= proposalDevelopmentTxnBean.getProposalStatus();
            Vector awardStatusCode= awardLookUpDataTxnBean.getAwardStatus();
            Vector instProposalStatusCode= instituteProposalLookUpDataTxnBean.getProposalStatus();
            Vector protocolStatusCode= protocolDataTxnBean.getProtocolStatus();
            //code added for princeton enhancement case#2802
            // To show the subcontract status
            SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
            Vector subcontractStatusCode= subContractTxnBean.getSubContractStatus();
            //Coeus enhancement Case #1828 : step 1: end
//            Vector vecProposalStatus = null;
//            HashMap hasProposalStatus = null;
            
            inboxList = new Vector(3,2);
            for(int row=0;row < listSize; row++){
                inboxRow = (HashMap)result.elementAt(row);
                inboxBean = new InboxBean();
                /* CASE #748 Begin */
                inboxBean.setSubjectDescription((String)
                inboxRow.get("SUBJECT_DESCRIPTION"));
                /* CASE #748 End */
                
                //modified for CASE #1828 Begin
                inboxBean.setProposalNumber((String)inboxRow.get("MODULE_ITEM_KEY"));
                inboxBean.setModuleCode(
                        inboxRow.get("MODULE_CODE") == null ? 0 : Integer.parseInt(inboxRow.get("MODULE_CODE").toString()));
                //modified for CASE #1828 End
                inboxBean.setToUser((String)
                inboxRow.get("TO_USER"));
                inboxBean.setMessageId((String)inboxRow.get("MESSAGE_ID"));
                inboxBean.setFromUser((String)
                inboxRow.get("FROM_USER"));
                inboxBean.setArrivalDate(
                        (Timestamp) inboxRow.get("ARRIVAL_DATE"));
                inboxBean.setSubjectType(inboxRow.get("SUBJECT_TYPE") == null ?
                    ' ' : inboxRow.get("SUBJECT_TYPE").toString().charAt(0));
                inboxBean.setOpenedFlag(inboxRow.get("OPENED_FLAG") == null ?
                    ' ' : inboxRow.get("OPENED_FLAG").toString().charAt(0));
                inboxBean.setUpdateTimeStamp((Timestamp)
                inboxRow.get("UPDATE_TIMESTAMP"));
                inboxBean.setUpdateUser( (String)
                inboxRow.get("UPDATE_USER"));
                inboxBean.setAw_ArrivalDate(
                        (Timestamp) inboxRow.get("ARRIVAL_DATE"));
                inboxBean.setAw_MessageId(
                        inboxRow.get("MESSAGE_ID") == null ? 0 : Integer.parseInt(inboxRow.get("MESSAGE_ID").toString()));
                //Added by shiji for Coeus enhancement Case #1828 : step 2 : start
                //inboxBean.setAw_ProposalNumber((String)inboxRow.get("MODULE_ITEM_KEY"));
                inboxBean.setItem((String)inboxRow.get("MODULE_ITEM_KEY"));
                inboxBean.setModule((String)inboxRow.get("MODULE_DESC"));
                //Coeus enhancement Case #1828 : step 2 : end
                inboxBean.setAw_ToUser((String)
                inboxRow.get("TO_USER"));
                inboxBean.setUserName((String)
                inboxRow.get("FROM_USER_NAME"));
                inboxBean.setProposalDeadLineDate(
                        inboxRow.get("DEADLINE_DATE") == null ?
                            null : new Date(((Timestamp) inboxRow.get(
                        "DEADLINE_DATE")).getTime()));
                inboxBean.setProposalTitle((String)
                inboxRow.get("PROPOSAL_TITLE"));
                inboxBean.setSponsorCode((String)
                inboxRow.get("SPONSOR_CODE"));
                inboxBean.setSponsorName((String)
                inboxRow.get("SPONSOR_NAME"));
                inboxBean.setSysDate((Timestamp)
                inboxRow.get("SYSTEM_DATE"));
                inboxBean.setUnitNumber((String)
                inboxRow.get("UNIT_NUMBER"));
                inboxBean.setUnitName((String)
                inboxRow.get("UNIT_NAME"));
                inboxBean.setPersonName((String)
                inboxRow.get("PERSON_NAME"));
                inboxBean.setCreationStatus(Integer.parseInt(
                        inboxRow.get("CREATION_STATUS_CODE") == null ? "0" : inboxRow.get("CREATION_STATUS_CODE").toString()));
                //Added for Coeus enhancement Case #1828 :step 3 : start
                int propStatusCodeSize=0;
                if(inboxBean.getModuleCode() == 1) {
                    propStatusCodeSize=awardStatusCode.size();
                    for(int statusCodeRow=0;statusCodeRow < propStatusCodeSize; statusCodeRow++){
                        
                        ComboBoxBean currentStatus = (ComboBoxBean)awardStatusCode.elementAt(statusCodeRow);
                        int statusCode = Integer.parseInt(currentStatus.getCode());
                        if( statusCode == inboxBean.getCreationStatus()) {
                            inboxBean.setCreationStatusDescription(currentStatus.getDescription());
                            break;
                        }
                    }
                }else if(inboxBean.getModuleCode() == 2) {
                    propStatusCodeSize=instProposalStatusCode.size();
                    for(int statusCodeRow=0;statusCodeRow < propStatusCodeSize; statusCodeRow++){
                        
                        ComboBoxBean currentStatus = (ComboBoxBean)instProposalStatusCode.elementAt(statusCodeRow);
                        int statusCode = Integer.parseInt(currentStatus.getCode());
                        if( statusCode == inboxBean.getCreationStatus()) {
                            inboxBean.setCreationStatusDescription(currentStatus.getDescription());
                            break;
                        }
                    }
                }else if(inboxBean.getModuleCode() == 3) {
                    propStatusCodeSize=proposalStatusCode.size();
                    for(int statusCodeRow=0;statusCodeRow < propStatusCodeSize; statusCodeRow++){
                        
                        ComboBoxBean currentStatus = (ComboBoxBean)proposalStatusCode.elementAt(statusCodeRow);
                        int statusCode = Integer.parseInt(currentStatus.getCode());
                        if( statusCode == inboxBean.getCreationStatus()) {
                            inboxBean.setCreationStatusDescription(currentStatus.getDescription());
                            break;
                        }
                    }
                }else if(inboxBean.getModuleCode() == 7) {
                    propStatusCodeSize=protocolStatusCode.size();
                    for(int statusCodeRow=0;statusCodeRow < propStatusCodeSize; statusCodeRow++){
                        
                        ComboBoxBean currentStatus = (ComboBoxBean)protocolStatusCode.elementAt(statusCodeRow);
                        int statusCode = Integer.parseInt(currentStatus.getCode());
                        if( statusCode == inboxBean.getCreationStatus()) {
                            inboxBean.setCreationStatusDescription(currentStatus.getDescription());
                            break;
                        }
                    }
                    //Coeus enhancement Case #1828 : step 3 :end
                }
                //Added for princeton enhancement case#2802 - starts
                //To show the subcontract status in the inbox form
                else if(inboxBean.getModuleCode() == 4) {
                    if(subcontractStatusCode != null && subcontractStatusCode.size()>0){
                        for(int statusCodeRow=0;statusCodeRow < subcontractStatusCode.size() ; statusCodeRow++){
                            ComboBoxBean currentStatus = (ComboBoxBean)subcontractStatusCode.elementAt(statusCodeRow);
                            int statusCode = Integer.parseInt(currentStatus.getCode());
                            if( statusCode == inboxBean.getCreationStatus()) {
                                inboxBean.setCreationStatusDescription(currentStatus.getDescription());
                                break;
                            }
                        }
                    }
                    //Added for princeton enhancement case#2802 - ends
                }
                
                //Modified for Case#3682 - Enhancements related to Delegations  - Start
                else if(inboxBean.getModuleCode() == 0){
                    int status = inboxBean.getCreationStatus();
                    String statusDesc = "";
                    switch(status){
                            case 1:
                                    statusDesc = "Requested";
                                    break;
                            case 2:
                                    statusDesc = "Accepted";
                                    break;
                            case 3:
                                    statusDesc = "Rejected";
                                    break;
                            case 4:
                                    statusDesc = "Closed";
                                    break;
                    }
                    if(statusDesc != null && !statusDesc.equals("")){
                        inboxBean.setCreationStatusDescription(statusDesc);
                    }
                }
                //Modified for Case#3682 - Enhancements related to Delegations - End
                
                
//                for(int statusCodeRow=0;statusCodeRow < propStatusCodeSize; statusCodeRow++){
//
//                    ComboBoxBean currentStatus = (ComboBoxBean)proposalStatusCode.elementAt(statusCodeRow);
//                    int statusCode = Integer.parseInt(currentStatus.getCode());
//                    if( statusCode == inboxBean.getCreationStatus()) {
//                        inboxBean.setCreationStatusDescription(currentStatus.getDescription());
//                        break;
//                    }
//                }
//                inboxBean.setCreationStatusDescription((String)
//                    inboxRow.get("CREATION_STATUS_DESC"));
                inboxBean.setMessageBean(getMessage(inboxBean.getMessageId()));
                inboxList.addElement(inboxBean);
            }
        }
        return inboxList;
    }
    /**
     *  Method used to check whether a given Development Proposal can be modified
     *  To check, it uses FN_CAN_MODIFY_DEV_PROPOSAL function.
     *
     *  @param proposalNumber String
     *  @return int indicating whether Dev Proposal can be modified
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int canModifyDevelopmentProposal(String proposalNumber)
    throws CoeusException, DBException{
        
        Vector param = new Vector();
//        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        int canModify = 0;
        Vector result = new Vector();
        
        param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber));
        
        result = dbEngine.executeFunctions("Coeus",
                "{ <<OUT INTEGER CAN_MODIFY>> = "
                +" call FN_CAN_MODIFY_DEV_PROPOSAL( "
                +" << PROPOSAL_NUMBER >> ) }", param);
        
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            canModify = Integer.parseInt(rowParameter.get("CAN_MODIFY").toString());
        }
        
        return canModify;
    }
    
    //Added for COEUSDEV-346 : PI Certification Question can't be saved in Coeus Lite. - Start
    /*
     * Method to check all investigators are certified by calling fn_is_prop_all_inv_certified
     * @param proposalNumber
     * @return isAllInvestigatorsCertified
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean isAllInvestigatorsCertified(String proposalNumber)
    throws  CoeusException, DBException {
        int investigatorCertified = 1;
        boolean isAllInvestigatorsCertified = false;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT STRING IS_CERTIFIED>> = "
                    +" call fn_is_eps_prop_all_inv_cert(<< PROPOSAL_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            investigatorCertified = Integer.parseInt(rowParameter.get("IS_CERTIFIED").toString());
            if(investigatorCertified == 1){
                isAllInvestigatorsCertified = true;
            }
        }
        
        return isAllInvestigatorsCertified;
    }
    /**/
     public boolean isAllInvKeyCertified(String proposalNumber)
    throws  CoeusException, DBException {
        int investigatorCertified = 1;
        boolean isAllInvestigatorsCertified = false;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT STRING IS_CERTIFIED>> = "
                    +" call FN_IS_EPS_PROP_INVKEY_CERT(<< PROPOSAL_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            investigatorCertified = Integer.parseInt(rowParameter.get("IS_CERTIFIED").toString());
            if(investigatorCertified == 1){
                isAllInvestigatorsCertified = true;
            }
        }

        return isAllInvestigatorsCertified;
    }


    //COEUSDEV-346 : End
    /** To update the inboxBean
     * @param vecResAndUnResInboxList resolved and unresolved inboxlist
     * @return Vecter resovled and unresolved messages 
     */
    // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
    public Vector updateInboxBean(Vector vecResAndUnResInboxList) throws Exception {
        HashMap inboxRow = null;
        int unresolvedMessSize= vecResAndUnResInboxList.size();
        InboxBean inboxBean = null;
        Vector inboxResList = new Vector(3,2);
        for(int i=0; i< unresolvedMessSize; i++){
            inboxRow = (HashMap)vecResAndUnResInboxList.elementAt(i);
            inboxBean = new InboxBean();
            inboxBean.setItem((String)inboxRow.get("MODULE_ITEM_KEY") == null ? "" : (String)inboxRow.get("MODULE_ITEM_KEY"));
            inboxBean.setProposalNumber((String)inboxRow.get("MODULE_ITEM_KEY"));
            inboxBean.setModuleCode(
                    inboxRow.get("MODULE_CODE") == null ? 0 : Integer.parseInt(inboxRow.get("MODULE_CODE").toString()));
            inboxBean.setModule((String)inboxRow.get("MODULE_DESC") == null ? "" : (String)inboxRow.get("MODULE_DESC"));
             inboxBean.setToUser((String)
            inboxRow.get("TO_USER") == null ? "" : (String)
            inboxRow.get("TO_USER"));
            inboxBean.setAw_ToUser((String)
            inboxRow.get("TO_USER") == null ? "" :(String)
            inboxRow.get("TO_USER")); 
            inboxBean.setMessageId((String)inboxRow.get("MESSAGE_ID") == null ? "" :(String)inboxRow.get("MESSAGE_ID"));
            inboxBean.setAw_MessageId(
                    inboxRow.get("MESSAGE_ID") == null ? 0 : Integer.parseInt(inboxRow.get("MESSAGE_ID").toString()));
            inboxBean.setFromUser((String)
            inboxRow.get("FROM_USER") == null ? "" : (String)
            inboxRow.get("FROM_USER"));
            inboxBean.setArrivalDate(
                (Timestamp) inboxRow.get("ARRIVAL_DATE") == null ? null : (Timestamp) inboxRow.get("ARRIVAL_DATE") );
            inboxBean.setAw_ArrivalDate(
                    (Timestamp) inboxRow.get("ARRIVAL_DATE") == null ? null : (Timestamp) inboxRow.get("ARRIVAL_DATE") );
            
            inboxBean.setSubjectType(inboxRow.get("SUBJECT_TYPE") == null ?
                ' ' : inboxRow.get("SUBJECT_TYPE").toString().charAt(0));           
            
            inboxBean.setSubjectDescription((String)
            inboxRow.get("SUBJECT_DESCRIPTION"));
            inboxBean.setOpenedFlag(inboxRow.get("OPENED_FLAG") == null ?
                ' ' : inboxRow.get("OPENED_FLAG").toString().charAt(0));
            inboxBean.setUpdateTimeStamp((Timestamp)
            inboxRow.get("UPDATE_TIMESTAMP"));
            inboxBean.setUpdateUser( (String)
            inboxRow.get("UPDATE_USER"));
            //inboxBean.setMessage((String)inboxRow.get("MESSAGE") == null ? "" :(String)inboxRow.get("MESSAGE"));
            inboxBean.setMessageBean(getMessage(inboxBean.getMessageId()));
            inboxBean.setUserName((String)
            inboxRow.get("FROM_USER_NAME") == null ? "" : (String)
            inboxRow.get("FROM_USER_NAME"));
                inboxBean.setProposalDeadLineDate(
                        inboxRow.get("DEADLINE_DATE") == null ?
                            null : new Date(((Timestamp) inboxRow.get(
                        "DEADLINE_DATE")).getTime()));
            inboxBean.setProposalTitle((String)
            inboxRow.get("TITLE") == null ? "" : (String)
            inboxRow.get("TITLE"));
            inboxBean.setSponsorCode((String)
            inboxRow.get("SPONSOR_CODE") == null ? "" : (String)
            inboxRow.get("SPONSOR_CODE"));
            inboxBean.setSponsorName((String)
            inboxRow.get("SPONSOR_NAME") == null ? "" : (String)
            inboxRow.get("SPONSOR_NAME"));
            inboxBean.setSysDate((Timestamp)
            inboxRow.get("SYSTEM_DATE") == null ? null : (Timestamp)
            inboxRow.get("SYSTEM_DATE"));
            inboxBean.setUnitNumber((String)
            inboxRow.get("UNIT_NUMBER") == null ? "" : (String)
            inboxRow.get("UNIT_NUMBER"));
            inboxBean.setUnitName((String)
            inboxRow.get("UNIT_NAME") == null ? "" : (String)
            inboxRow.get("UNIT_NAME"));
            inboxBean.setPersonName((String)
            inboxRow.get("PI_NAME") == null ? "" : (String)
            inboxRow.get("PI_NAME"));
            inboxBean.setCreationStatus(Integer.parseInt(
                    inboxRow.get("STATUS_CODE") == null ? "0" : inboxRow.get("STATUS_CODE").toString()));
             inboxBean.setCreationStatusDescription((String)
            inboxRow.get("STATUS_DESC") == null ? "" : (String)
            inboxRow.get("STATUS_DESC"));
            inboxResList.addElement(inboxBean);
        }
        return inboxResList;
    }
}//end of class